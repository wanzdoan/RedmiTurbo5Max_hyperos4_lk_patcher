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
public class SmsMmsAnalyticsProvider implements TelephonyAnalyticsProvider {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    private static final String[] SMS_MMS_INSERTION_PROJECTION = {"_id", "Count"};
    private static final String TAG = "SmsMmsAnalyticsProvider";
    private String mDateOfDeletedRecordsSmsMmsTable;
    private final int mSlotIndex;
    private TelephonyAnalyticsUtil mTelephonyAnalyticsUtil;

    private enum SmsMmsType {
        SMS_OUTGOING("SMS Outgoing"),
        SMS_INCOMING("SMS Incoming"),
        MMS_OUTGOING("MMS Outgoing"),
        MMS_INCOMING("MMS Incoming");

        public final String value;

        SmsMmsType(String str) {
            this.value = str;
        }
    }

    private enum SmsMmsStatus {
        SUCCESS("Success"),
        FAILURE("Failure");

        public final String value;

        SmsMmsStatus(String str) {
            this.value = str;
        }
    }

    public SmsMmsAnalyticsProvider(TelephonyAnalyticsUtil telephonyAnalyticsUtil, int i) {
        this.mTelephonyAnalyticsUtil = telephonyAnalyticsUtil;
        this.mSlotIndex = i;
        telephonyAnalyticsUtil.createTable("CREATE TABLE IF NOT EXISTS SmsMmsDataLogs(_id INTEGER PRIMARY KEY,LogDate DATE ,SmsMmsStatus TEXT DEFAULT '',SmsMmsType TEXT DEFAULT '',SlotID INTEGER , RAT TEXT DEFAULT '',FailureReason TEXT DEFAULT '',ReleaseVersion TEXT DEFAULT '' , Count INTEGER DEFAULT 1 );");
    }

    private ContentValues getContentValues(String str, String str2, String str3, String str4) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("LogDate", TelephonyAnalyticsDatabase.DATE_FORMAT.format(Calendar.getInstance().toInstant()));
        contentValues.put(TelephonyAnalyticsDatabase.SmsMmsAnalyticsTable.SMS_MMS_STATUS, str);
        contentValues.put(TelephonyAnalyticsDatabase.SmsMmsAnalyticsTable.SMS_MMS_TYPE, str2);
        contentValues.put("RAT", str3);
        contentValues.put("SlotID", Integer.valueOf(this.mSlotIndex));
        contentValues.put("FailureReason", str4);
        contentValues.put("ReleaseVersion", Build.VERSION.INCREMENTAL);
        return contentValues;
    }

    public void insertDataToDb(String str, String str2, String str3, String str4) {
        Cursor cursor;
        ContentValues contentValues = getContentValues(str, str2, str3, str4);
        String str5 = TAG;
        Rlog.d(str5, contentValues.toString());
        Cursor cursor2 = null;
        try {
            if (contentValues.getAsString(TelephonyAnalyticsDatabase.SmsMmsAnalyticsTable.SMS_MMS_STATUS).equals(SmsMmsStatus.SUCCESS.value)) {
                Rlog.d(str5, "Success Entry Data for Sms/Mms: " + contentValues.toString());
                cursor = this.mTelephonyAnalyticsUtil.getCursor(TelephonyAnalyticsDatabase.SmsMmsAnalyticsTable.TABLE_NAME, SMS_MMS_INSERTION_PROJECTION, "LogDate = ? AND SmsMmsType = ? AND SmsMmsStatus = ? AND SlotID = ? ", new String[]{contentValues.getAsString("LogDate"), contentValues.getAsString(TelephonyAnalyticsDatabase.SmsMmsAnalyticsTable.SMS_MMS_TYPE), contentValues.getAsString(TelephonyAnalyticsDatabase.SmsMmsAnalyticsTable.SMS_MMS_STATUS), contentValues.getAsString("SlotID")}, null, null, null, null);
            } else {
                cursor = this.mTelephonyAnalyticsUtil.getCursor(TelephonyAnalyticsDatabase.SmsMmsAnalyticsTable.TABLE_NAME, SMS_MMS_INSERTION_PROJECTION, "LogDate = ? AND SmsMmsStatus = ? AND SmsMmsType = ? AND RAT = ? AND SlotID = ? AND FailureReason = ? AND ReleaseVersion = ? ", new String[]{contentValues.getAsString("LogDate"), contentValues.getAsString(TelephonyAnalyticsDatabase.SmsMmsAnalyticsTable.SMS_MMS_STATUS), contentValues.getAsString(TelephonyAnalyticsDatabase.SmsMmsAnalyticsTable.SMS_MMS_TYPE), contentValues.getAsString("RAT"), contentValues.getAsString("SlotID"), contentValues.getAsString("FailureReason"), contentValues.getAsString("ReleaseVersion")}, null, null, null, null);
            }
            cursor2 = cursor;
            updateIfEntryExistsOtherwiseInsert(cursor2, contentValues);
            deleteOldAndOverflowData();
        } catch (Exception e) {
            Rlog.e(TAG, "Exception during Sms/Mms Insertion [insertDataToDb()] " + e);
        } finally {
            if (cursor2 != null) {
                cursor2.close();
            }
        }
    }

    public void updateIfEntryExistsOtherwiseInsert(Cursor cursor, ContentValues contentValues) {
        if (cursor == null || !cursor.moveToFirst()) {
            this.mTelephonyAnalyticsUtil.insert(TelephonyAnalyticsDatabase.SmsMmsAnalyticsTable.TABLE_NAME, contentValues);
            return;
        }
        int columnIndex = cursor.getColumnIndex("_id");
        int columnIndex2 = cursor.getColumnIndex("Count");
        if (columnIndex == -1 || columnIndex2 == -1) {
            return;
        }
        int i = cursor.getInt(columnIndex);
        contentValues.put("Count", Integer.valueOf(cursor.getInt(columnIndex2) + 1));
        String[] strArr = {String.valueOf(i)};
        Rlog.d(TAG, "Updated Count = " + contentValues.getAsString("Count"));
        this.mTelephonyAnalyticsUtil.update(TelephonyAnalyticsDatabase.SmsMmsAnalyticsTable.TABLE_NAME, contentValues, "_id = ? ", strArr);
    }

    public long getCount(String[] strArr, String str, String[] strArr2) {
        Cursor cursor = null;
        try {
            cursor = this.mTelephonyAnalyticsUtil.getCursor(TelephonyAnalyticsDatabase.SmsMmsAnalyticsTable.TABLE_NAME, strArr, str, strArr2, null, null, null, null);
            return this.mTelephonyAnalyticsUtil.getCountFromCursor(cursor);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private long getSmsMmsOfTypeAndStatus(String str, String str2) {
        String str3;
        String[] strArr = {"SUM(Count)"};
        String[] strArr2 = {Integer.toString(this.mSlotIndex)};
        if (str != null && str2 != null) {
            strArr2 = new String[]{str, str2, Integer.toString(this.mSlotIndex)};
            str3 = "SmsMmsType = ? AND SmsMmsStatus = ? AND SlotID = ? ";
        } else if (str != null) {
            strArr2 = new String[]{str, Integer.toString(this.mSlotIndex)};
            str3 = "SmsMmsType = ? AND SlotID = ? ";
        } else if (str2 == null) {
            str3 = "SlotID = ? ";
        } else {
            strArr2 = new String[]{str2, Integer.toString(this.mSlotIndex)};
            str3 = "SmsMmsStatus = ? AND SlotID = ? ";
        }
        return getCount(strArr, str3, strArr2);
    }

    private long getSmsOutgoingCount() {
        return getSmsMmsOfTypeAndStatus(SmsMmsType.SMS_OUTGOING.value, null);
    }

    private long getSmsIncomingCount() {
        return getSmsMmsOfTypeAndStatus(SmsMmsType.SMS_INCOMING.value, null);
    }

    private long getMmsOutgoingCount() {
        return getSmsMmsOfTypeAndStatus(SmsMmsType.MMS_OUTGOING.value, null);
    }

    private long getMmsIncomingCount() {
        return getSmsMmsOfTypeAndStatus(SmsMmsType.MMS_INCOMING.value, null);
    }

    private long getFailedOutgoingSms() {
        return getSmsMmsOfTypeAndStatus(SmsMmsType.SMS_OUTGOING.value, SmsMmsStatus.FAILURE.value);
    }

    private long getFailedIncomingSms() {
        return getSmsMmsOfTypeAndStatus(SmsMmsType.SMS_INCOMING.value, SmsMmsStatus.FAILURE.value);
    }

    private long getFailedOutgoingMms() {
        return getSmsMmsOfTypeAndStatus(SmsMmsType.MMS_OUTGOING.value, SmsMmsStatus.FAILURE.value);
    }

    private long getFailedIncomingMms() {
        return getSmsMmsOfTypeAndStatus(SmsMmsType.MMS_INCOMING.value, SmsMmsStatus.FAILURE.value);
    }

    private HashMap getSmsFailedCountByRat() {
        HashMap map = new HashMap();
        Cursor cursor = null;
        try {
            cursor = this.mTelephonyAnalyticsUtil.getCursor(TelephonyAnalyticsDatabase.SmsMmsAnalyticsTable.TABLE_NAME, new String[]{"RAT", "SUM(Count) as count "}, "SlotID = ? AND SmsMmsStatus = ? AND SmsMmsType IN ('" + SmsMmsType.SMS_INCOMING.value + "', '" + SmsMmsType.SMS_OUTGOING.value + "' ) ", new String[]{Integer.toString(this.mSlotIndex), SmsMmsStatus.FAILURE.value}, "RAT", null, null, null);
            if (cursor != null) {
                int columnIndex = cursor.getColumnIndex("RAT");
                int columnIndex2 = cursor.getColumnIndex("count");
                if (columnIndex != -1 && columnIndex2 != -1) {
                    while (cursor.moveToNext()) {
                        map.put(cursor.getString(columnIndex), Integer.valueOf(cursor.getInt(columnIndex2)));
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
        String str2 = this.mDateOfDeletedRecordsSmsMmsTable;
        if (str2 == null || !str2.equals(str)) {
            this.mTelephonyAnalyticsUtil.deleteOverflowAndOldData(TelephonyAnalyticsDatabase.SmsMmsAnalyticsTable.TABLE_NAME, "_id IN  ( SELECT _id FROM SmsMmsDataLogs ORDER BY LogDate DESC LIMIT -1 OFFSET ? ) ", "LogDate < ? ");
            this.mDateOfDeletedRecordsSmsMmsTable = str;
        }
    }

    public void setDateOfDeletedRecordsSmsMmsTable(String str) {
        this.mDateOfDeletedRecordsSmsMmsTable = str;
    }

    public ArrayList<String> dumpInformationInList(final long j, final long j2, long j3, long j4, long j5, long j6, double d, double d2, double d3, double d4, HashMap<String, Integer> map) {
        final ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Total Outgoing Sms Count = " + j);
        arrayList.add("Total Incoming Sms Count = " + j2);
        StringBuilder sb = new StringBuilder("Failed Outgoing SMS Count = ");
        sb.append(j3);
        sb.append(" Percentage failure rate for Outgoing SMS :");
        DecimalFormat decimalFormat = DECIMAL_FORMAT;
        sb.append(decimalFormat.format(d));
        sb.append("%");
        arrayList.add(sb.toString());
        arrayList.add("Failed Incoming SMS Count = " + j4 + " Percentage failure rate for Incoming SMS :" + decimalFormat.format(d2) + "%");
        StringBuilder sb2 = new StringBuilder("Overall Fail Percentage = ");
        sb2.append(decimalFormat.format((((double) (j3 + j4)) / ((double) (j2 + j))) * 100.0d));
        sb2.append("%");
        arrayList.add(sb2.toString());
        try {
            try {
                BiConsumer<? super String, ? super Integer> biConsumer = new BiConsumer() { // from class: com.android.internal.telephony.analytics.SmsMmsAnalyticsProvider$$ExternalSyntheticLambda0
                    @Override // java.util.function.BiConsumer
                    public final void accept(Object obj, Object obj2) {
                        Integer num = (Integer) obj2;
                        arrayList.add("Failed SMS Count for RAT : " + ((String) obj) + " = " + num + ", Percentage = " + SmsMmsAnalyticsProvider.DECIMAL_FORMAT.format((((double) num.intValue()) / (j2 + j)) * 100.0d) + "%");
                    }
                };
                arrayList = arrayList;
                map.forEach(biConsumer);
                return arrayList;
            } catch (Exception e) {
                e = e;
                arrayList = arrayList;
                Rlog.d(TAG, "Exception in adding to List = " + e);
                return arrayList;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    @Override // com.android.internal.telephony.analytics.TelephonyAnalyticsProvider
    public ArrayList<String> aggregate() {
        double d;
        long j;
        double d2;
        long smsOutgoingCount = getSmsOutgoingCount();
        long smsIncomingCount = getSmsIncomingCount();
        long mmsOutgoingCount = getMmsOutgoingCount();
        long mmsIncomingCount = getMmsIncomingCount();
        long failedOutgoingSms = getFailedOutgoingSms();
        long failedIncomingSms = getFailedIncomingSms();
        long failedOutgoingMms = getFailedOutgoingMms();
        long failedIncomingMms = getFailedIncomingMms();
        HashMap smsFailedCountByRat = getSmsFailedCountByRat();
        double d3 = smsOutgoingCount != 0 ? (failedOutgoingSms / smsOutgoingCount) * 100.0d : 0.0d;
        double d4 = smsIncomingCount != 0 ? (failedIncomingSms / smsIncomingCount) * 100.0d : 0.0d;
        double d5 = mmsOutgoingCount != 0 ? (failedOutgoingMms / mmsOutgoingCount) * 100.0d : 0.0d;
        if (mmsIncomingCount != 0) {
            double d6 = (failedIncomingMms / mmsIncomingCount) * 100.0d;
            j = mmsIncomingCount;
            d = d6;
            d2 = d5;
        } else {
            d = 0.0d;
            j = mmsIncomingCount;
            d2 = d5;
        }
        return dumpInformationInList(smsOutgoingCount, smsIncomingCount, failedOutgoingSms, failedIncomingSms, mmsOutgoingCount, j, d3, d4, d2, d, smsFailedCountByRat);
    }
}
