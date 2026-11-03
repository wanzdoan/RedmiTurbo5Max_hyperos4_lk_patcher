package com.android.internal.telephony.analytics;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;
import com.android.telephony.Rlog;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.function.BiConsumer;

/* JADX INFO: loaded from: classes.dex */
public class ServiceStateAnalyticsProvider implements TelephonyAnalyticsProvider {
    private static final String TAG = "ServiceStateAnalyticsProvider";
    private String mDateOfDeletedRecordsServiceStateTable;
    private final int mSlotIndex;
    protected TelephonyAnalyticsUtil mTelephonyAnalyticsUtil;
    private static final String[] SERVICE_STATE_INSERTION_COLUMNS = {"_id", TelephonyAnalyticsDatabase.ServiceStateAnalyticsTable.TIME_DURATION};
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    public ServiceStateAnalyticsProvider(TelephonyAnalyticsUtil telephonyAnalyticsUtil, int i) {
        this.mTelephonyAnalyticsUtil = telephonyAnalyticsUtil;
        this.mSlotIndex = i;
        telephonyAnalyticsUtil.createTable("CREATE TABLE IF NOT EXISTS ServiceStateLogs ( _id INTEGER PRIMARY KEY AUTOINCREMENT, LogDate DATE ,SlotID INTEGER , TimeDuration INTEGER ,RAT TEXT ,DeviceStatus TEXT ,ReleaseVersion TEXT );");
    }

    private ContentValues getContentValues(TelephonyAnalytics.ServiceStateAnalytics.TimeStampedServiceState timeStampedServiceState, long j) {
        ContentValues contentValues = new ContentValues();
        long j2 = j - timeStampedServiceState.mTimestampStart;
        contentValues.put("LogDate", TelephonyAnalyticsDatabase.DATE_FORMAT.format(Calendar.getInstance().toInstant()));
        contentValues.put(TelephonyAnalyticsDatabase.ServiceStateAnalyticsTable.TIME_DURATION, Long.valueOf(j2));
        contentValues.put("SlotID", Integer.valueOf(timeStampedServiceState.mSlotIndex));
        contentValues.put("RAT", timeStampedServiceState.mRAT);
        contentValues.put(TelephonyAnalyticsDatabase.ServiceStateAnalyticsTable.DEVICE_STATUS, timeStampedServiceState.mDeviceStatus);
        contentValues.put("ReleaseVersion", Build.VERSION.INCREMENTAL);
        return contentValues;
    }

    public void insertDataToDb(TelephonyAnalytics.ServiceStateAnalytics.TimeStampedServiceState timeStampedServiceState, long j) {
        ContentValues contentValues = getContentValues(timeStampedServiceState, j);
        Rlog.d(TAG, "  " + contentValues.toString() + "Time = " + System.currentTimeMillis());
        Cursor cursor = null;
        try {
            cursor = this.mTelephonyAnalyticsUtil.getCursor(TelephonyAnalyticsDatabase.ServiceStateAnalyticsTable.TABLE_NAME, SERVICE_STATE_INSERTION_COLUMNS, "LogDate = ? AND SlotID = ? AND RAT = ? AND DeviceStatus = ? AND ReleaseVersion = ? ", new String[]{contentValues.getAsString("LogDate"), contentValues.getAsString("SlotID"), contentValues.getAsString("RAT"), contentValues.getAsString(TelephonyAnalyticsDatabase.ServiceStateAnalyticsTable.DEVICE_STATUS), contentValues.getAsString("ReleaseVersion")}, null, null, null, null);
            updateIfEntryExistsOtherwiseInsert(cursor, contentValues);
            deleteOldAndOverflowData();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void updateIfEntryExistsOtherwiseInsert(Cursor cursor, ContentValues contentValues) {
        if (cursor == null || !cursor.moveToFirst()) {
            this.mTelephonyAnalyticsUtil.insert(TelephonyAnalyticsDatabase.ServiceStateAnalyticsTable.TABLE_NAME, contentValues);
            return;
        }
        int columnIndex = cursor.getColumnIndex("_id");
        int columnIndex2 = cursor.getColumnIndex(TelephonyAnalyticsDatabase.ServiceStateAnalyticsTable.TIME_DURATION);
        if (columnIndex == -1 || columnIndex2 == -1) {
            return;
        }
        int i = cursor.getInt(columnIndex);
        int i2 = cursor.getInt(columnIndex2) + Integer.parseInt(contentValues.getAsString(TelephonyAnalyticsDatabase.ServiceStateAnalyticsTable.TIME_DURATION));
        String[] strArr = {Integer.toString(i)};
        contentValues.put(TelephonyAnalyticsDatabase.ServiceStateAnalyticsTable.TIME_DURATION, Integer.valueOf(i2));
        this.mTelephonyAnalyticsUtil.update(TelephonyAnalyticsDatabase.ServiceStateAnalyticsTable.TABLE_NAME, contentValues, "_id = ? ", strArr);
    }

    private long getTotalUpTime() {
        Cursor cursor = null;
        try {
            cursor = this.mTelephonyAnalyticsUtil.getCursor(TelephonyAnalyticsDatabase.ServiceStateAnalyticsTable.TABLE_NAME, new String[]{"SUM(TimeDuration)"}, "SlotID = ? ", new String[]{Integer.toString(this.mSlotIndex)}, null, null, null, null);
            return (cursor == null || !cursor.moveToFirst()) ? 0L : cursor.getLong(0);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private long outOfServiceDuration() {
        Cursor cursor = null;
        try {
            cursor = this.mTelephonyAnalyticsUtil.getCursor(TelephonyAnalyticsDatabase.ServiceStateAnalyticsTable.TABLE_NAME, new String[]{"SUM(TimeDuration)"}, "DeviceStatus != ?  AND SlotID = ? ", new String[]{"IN_SERVICE", Integer.toString(this.mSlotIndex)}, null, null, null, null);
            return (cursor == null || !cursor.moveToFirst()) ? 0L : cursor.getLong(0);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private HashMap getOutOfServiceDurationByReason() {
        HashMap map = new HashMap();
        Cursor cursor = null;
        try {
            cursor = this.mTelephonyAnalyticsUtil.getCursor(TelephonyAnalyticsDatabase.ServiceStateAnalyticsTable.TABLE_NAME, new String[]{TelephonyAnalyticsDatabase.ServiceStateAnalyticsTable.DEVICE_STATUS, "SUM(TimeDuration) AS totalTime"}, "DeviceStatus != ? AND SlotID = ? ", new String[]{"IN_SERVICE", Integer.toString(this.mSlotIndex)}, TelephonyAnalyticsDatabase.ServiceStateAnalyticsTable.DEVICE_STATUS, null, null, null);
            if (cursor != null) {
                int columnIndex = cursor.getColumnIndex(TelephonyAnalyticsDatabase.ServiceStateAnalyticsTable.DEVICE_STATUS);
                int columnIndex2 = cursor.getColumnIndex("totalTime");
                if (columnIndex != -1 && columnIndex2 != -1) {
                    while (cursor.moveToNext()) {
                        map.put(cursor.getString(columnIndex), Long.valueOf(cursor.getLong(columnIndex2)));
                    }
                }
            }
            return map;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private HashMap getInServiceDurationByRat() {
        HashMap map = new HashMap();
        Cursor cursor = null;
        try {
            cursor = this.mTelephonyAnalyticsUtil.getCursor(TelephonyAnalyticsDatabase.ServiceStateAnalyticsTable.TABLE_NAME, new String[]{"RAT", "SUM(TimeDuration) AS totalTime"}, "RAT != ? AND SlotID = ? ", new String[]{"NA", Integer.toString(this.mSlotIndex)}, "RAT", null, null, null);
            if (cursor != null) {
                int columnIndex = cursor.getColumnIndex("RAT");
                int columnIndex2 = cursor.getColumnIndex("totalTime");
                if (columnIndex != -1 && columnIndex2 != -1) {
                    while (cursor.moveToNext()) {
                        map.put(cursor.getString(columnIndex), Long.valueOf(cursor.getLong(columnIndex2)));
                    }
                }
            }
            return map;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    protected void deleteOldAndOverflowData() {
        String str = TelephonyAnalyticsDatabase.DATE_FORMAT.format(Calendar.getInstance().toInstant());
        String str2 = this.mDateOfDeletedRecordsServiceStateTable;
        if (str2 == null || !str2.equals(str)) {
            this.mTelephonyAnalyticsUtil.deleteOverflowAndOldData(TelephonyAnalyticsDatabase.ServiceStateAnalyticsTable.TABLE_NAME, "_id IN  ( SELECT _id FROM ServiceStateLogs ORDER BY LogDate DESC LIMIT -1 OFFSET ? )", "LogDate < ? ");
            this.mDateOfDeletedRecordsServiceStateTable = str;
        }
    }

    public void setDateOfDeletedRecordsServiceStateTable(String str) {
        this.mDateOfDeletedRecordsServiceStateTable = str;
    }

    private ArrayList dumpInformationInList(final Long l, Long l2, double d, HashMap map, HashMap map2) {
        final ArrayList arrayList = new ArrayList();
        arrayList.add("Total UpTime = " + l + " millis");
        arrayList.add("Out of Service Time = " + l2 + " millis, Percentage " + DECIMAL_FORMAT.format(d) + "%");
        map.forEach(new BiConsumer() { // from class: com.android.internal.telephony.analytics.ServiceStateAnalyticsProvider$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                Long l3 = l;
                arrayList.add("Out of service Reason = " + ((String) obj) + ", Percentage = " + ServiceStateAnalyticsProvider.DECIMAL_FORMAT.format(l3.longValue() == 0 ? 0.0d : (((Long) obj2).longValue() / l3.longValue()) * 100.0d) + "%");
            }
        });
        map2.forEach(new BiConsumer() { // from class: com.android.internal.telephony.analytics.ServiceStateAnalyticsProvider$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                Long l3 = l;
                arrayList.add("IN_SERVICE RAT : " + ((String) obj) + ", Percentage = " + ServiceStateAnalyticsProvider.DECIMAL_FORMAT.format(l3.longValue() == 0 ? 0.0d : (((Long) obj2).longValue() / l3.longValue()) * 100.0d) + "%");
            }
        });
        return arrayList;
    }

    @Override // com.android.internal.telephony.analytics.TelephonyAnalyticsProvider
    public ArrayList<String> aggregate() {
        long totalUpTime = getTotalUpTime();
        long jOutOfServiceDuration = outOfServiceDuration();
        return dumpInformationInList(Long.valueOf(totalUpTime), Long.valueOf(jOutOfServiceDuration), totalUpTime == 0 ? 0.0d : (jOutOfServiceDuration / totalUpTime) * 100.0d, getOutOfServiceDurationByReason(), getInServiceDurationByRat());
    }
}
