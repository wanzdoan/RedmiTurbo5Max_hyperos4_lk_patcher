package com.android.internal.telephony.analytics;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;
import com.android.telephony.Rlog;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.function.BiConsumer;

/* JADX INFO: loaded from: classes.dex */
public class CallAnalyticsProvider implements TelephonyAnalyticsProvider {
    private static final String TAG = "CallAnalyticsProvider";
    private String mDateOfDeletedRecordsCallTable;
    private final int mSlotIndex;
    private TelephonyAnalyticsUtil mTelephonyAnalyticsUtil;
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    private static final String[] CALL_INSERTION_PROJECTION = {"_id", "Count"};

    private enum CallStatus {
        SUCCESS("Success"),
        FAILURE("Failure");

        public String value;

        CallStatus(String str) {
            this.value = str;
        }
    }

    private enum CallType {
        NORMAL("Normal Call"),
        SOS("SOS Call");

        public String value;

        CallType(String str) {
            this.value = str;
        }
    }

    public CallAnalyticsProvider(TelephonyAnalyticsUtil telephonyAnalyticsUtil, int i) {
        this.mTelephonyAnalyticsUtil = telephonyAnalyticsUtil;
        this.mSlotIndex = i;
        telephonyAnalyticsUtil.createTable("CREATE TABLE IF NOT EXISTS CallDataLogs(_id INTEGER PRIMARY KEY,LogDate DATE ,CallStatus TEXT DEFAULT '',CallType TEXT DEFAULT '',RAT TEXT DEFAULT '',SlotID INTEGER ,FailureReason TEXT DEFAULT '',ReleaseVersion TEXT DEFAULT '' , Count INTEGER DEFAULT 1 );");
    }

    private ContentValues getContentValues(String str, String str2, int i, String str3, String str4) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("LogDate", TelephonyAnalyticsDatabase.DATE_FORMAT.format(Calendar.getInstance().toInstant()));
        contentValues.put(TelephonyAnalyticsDatabase.CallAnalyticsTable.CALL_TYPE, str);
        contentValues.put(TelephonyAnalyticsDatabase.CallAnalyticsTable.CALL_STATUS, str2);
        contentValues.put("SlotID", Integer.valueOf(i));
        contentValues.put("RAT", str3);
        contentValues.put("FailureReason", str4);
        contentValues.put("ReleaseVersion", Build.VERSION.INCREMENTAL);
        return contentValues;
    }

    private String[] getSuccessfulCallSelectionArgs(ContentValues contentValues) {
        return new String[]{contentValues.getAsString(TelephonyAnalyticsDatabase.CallAnalyticsTable.CALL_TYPE), contentValues.getAsString("LogDate"), CallStatus.SUCCESS.value, contentValues.getAsString("SlotID")};
    }

    private String[] getFailedCallSelectionArgs(ContentValues contentValues) {
        return new String[]{contentValues.getAsString("LogDate"), contentValues.getAsString(TelephonyAnalyticsDatabase.CallAnalyticsTable.CALL_STATUS), contentValues.getAsString(TelephonyAnalyticsDatabase.CallAnalyticsTable.CALL_TYPE), contentValues.getAsString("SlotID"), contentValues.getAsString("RAT"), contentValues.getAsString("FailureReason"), contentValues.getAsString("ReleaseVersion")};
    }

    public void insertDataToDb(String str, String str2, int i, String str3, String str4) {
        ContentValues contentValues = getContentValues(str, str2, i, str3, str4);
        Cursor cursor = null;
        try {
            if (contentValues.getAsString(TelephonyAnalyticsDatabase.CallAnalyticsTable.CALL_STATUS).equals(CallStatus.SUCCESS.value)) {
                Rlog.d(TAG, "Insertion for Success Call");
                cursor = this.mTelephonyAnalyticsUtil.getCursor(TelephonyAnalyticsDatabase.CallAnalyticsTable.TABLE_NAME, CALL_INSERTION_PROJECTION, "CallType = ? AND LogDate = ? AND CallStatus = ? AND SlotID = ? ", getSuccessfulCallSelectionArgs(contentValues), null, null, null, null);
            } else {
                cursor = this.mTelephonyAnalyticsUtil.getCursor(TelephonyAnalyticsDatabase.CallAnalyticsTable.TABLE_NAME, CALL_INSERTION_PROJECTION, "LogDate = ? AND CallStatus = ? AND CallType = ? AND SlotID = ? AND RAT = ? AND FailureReason = ? AND ReleaseVersion = ? ", getFailedCallSelectionArgs(contentValues), null, null, null, null);
            }
            updateEntryIfExistsOrInsert(cursor, contentValues);
            deleteOldAndOverflowData();
        } catch (Exception unused) {
            Rlog.e(TAG, "Error caught in insertDataToDb while insertion.");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void updateEntryIfExistsOrInsert(Cursor cursor, ContentValues contentValues) {
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("_id");
            int columnIndex2 = cursor.getColumnIndex("Count");
            if (columnIndex == -1 || columnIndex2 == -1) {
                return;
            }
            int i = cursor.getInt(columnIndex);
            contentValues.put("Count", Integer.valueOf(cursor.getInt(columnIndex2) + 1));
            String[] strArr = {String.valueOf(i)};
            Rlog.d(TAG, "Updated Count = " + contentValues.getAsString("Count"));
            this.mTelephonyAnalyticsUtil.update(TelephonyAnalyticsDatabase.CallAnalyticsTable.TABLE_NAME, contentValues, "_id = ? ", strArr);
            return;
        }
        Rlog.d(TAG, "Simple Insertion");
        this.mTelephonyAnalyticsUtil.insert(TelephonyAnalyticsDatabase.CallAnalyticsTable.TABLE_NAME, contentValues);
    }

    public long getCount(String str, String[] strArr, String str2, String[] strArr2, String str3, String str4, String str5, String str6) {
        Cursor cursor = null;
        try {
            cursor = this.mTelephonyAnalyticsUtil.getCursor(TelephonyAnalyticsDatabase.CallAnalyticsTable.TABLE_NAME, strArr, str2, strArr2, str3, str4, str5, str6);
            return this.mTelephonyAnalyticsUtil.getCountFromCursor(cursor);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private String[] getColumnSelectionAndArgs(String str, String str2) {
        if (str != null) {
            if (str2 != null) {
                return new String[]{"CallType = ? AND CallStatus = ? AND SlotID = ? ", str, str2, Integer.toString(this.mSlotIndex)};
            }
            return new String[]{"CallType = ? AND SlotID = ? ", str, Integer.toString(this.mSlotIndex)};
        }
        if (str2 != null) {
            return new String[]{"CallStatus = ? AND SlotID = ? ", str2, Integer.toString(this.mSlotIndex)};
        }
        return new String[]{"SlotID = ? ", Integer.toString(this.mSlotIndex)};
    }

    private long countCallsOfTypeAndStatus(String str, String str2) {
        String[] columnSelectionAndArgs = getColumnSelectionAndArgs(str, str2);
        return getCount(TelephonyAnalyticsDatabase.CallAnalyticsTable.TABLE_NAME, new String[]{"sum(Count)"}, columnSelectionAndArgs[0], (String[]) Arrays.copyOfRange(columnSelectionAndArgs, 1, columnSelectionAndArgs.length), null, null, null, null);
    }

    private long countTotalCalls() {
        return countCallsOfTypeAndStatus(null, null);
    }

    private long countFailedCalls() {
        return countCallsOfTypeAndStatus(null, CallStatus.FAILURE.value);
    }

    private long countNormalCalls() {
        return countCallsOfTypeAndStatus(CallType.NORMAL.value, null);
    }

    private long countFailedNormalCalls() {
        return countCallsOfTypeAndStatus(CallType.NORMAL.value, CallStatus.FAILURE.value);
    }

    private long countSosCalls() {
        return countCallsOfTypeAndStatus(CallType.SOS.value, null);
    }

    private long countFailedSosCalls() {
        return countCallsOfTypeAndStatus(CallType.SOS.value, CallStatus.FAILURE.value);
    }

    private String getMaxFailureVersion() {
        String string;
        int columnIndex;
        Cursor cursor = null;
        try {
            cursor = this.mTelephonyAnalyticsUtil.getCursor(TelephonyAnalyticsDatabase.CallAnalyticsTable.TABLE_NAME, new String[]{"ReleaseVersion"}, "CallStatus = ? AND SlotID = ? ", new String[]{CallStatus.FAILURE.value, Integer.toString(this.mSlotIndex)}, "ReleaseVersion", null, "SUM(Count) DESC ", "1");
            if (cursor != null && cursor.moveToFirst() && (columnIndex = cursor.getColumnIndex("ReleaseVersion")) != -1) {
                string = cursor.getString(columnIndex);
            } else {
                string = "";
            }
            return string;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private String getMaxFailureNetworkType() {
        String string;
        Cursor cursor = null;
        try {
            cursor = this.mTelephonyAnalyticsUtil.getCursor(TelephonyAnalyticsDatabase.CallAnalyticsTable.TABLE_NAME, new String[]{"RAT"}, "CallStatus = ? AND SlotID = ? ", new String[]{CallStatus.FAILURE.value, Integer.toString(this.mSlotIndex)}, "RAT", null, "SUM(Count) DESC ", "1");
            if (cursor != null && cursor.moveToFirst()) {
                string = cursor.getString(cursor.getColumnIndex("RAT"));
            } else {
                string = "";
            }
            return string;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private HashMap getFailureCountByRatForCallType(String str) {
        return getFailureCountByColumnForCallType("RAT", str);
    }

    private HashMap getFailureCountByReasonForCallType(String str) {
        return getFailureCountByColumnForCallType("FailureReason", str);
    }

    private HashMap getFailureCountByColumnForCallType(String str, String str2) {
        String[] strArr = {str, "SUM(Count) AS count"};
        String[] strArr2 = {str2, CallStatus.FAILURE.value, Integer.toString(this.mSlotIndex)};
        HashMap map = new HashMap();
        Cursor cursor = null;
        try {
            cursor = this.mTelephonyAnalyticsUtil.getCursor(TelephonyAnalyticsDatabase.CallAnalyticsTable.TABLE_NAME, strArr, "CallType = ? AND CallStatus = ? AND SlotID = ? ", strArr2, str, null, null, null);
            if (cursor != null) {
                int columnIndex = cursor.getColumnIndex(str);
                int columnIndex2 = cursor.getColumnIndex("count");
                if (columnIndex2 != -1 && columnIndex != -1) {
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
        String str2 = this.mDateOfDeletedRecordsCallTable;
        if (str2 == null || !str2.equals(str)) {
            this.mTelephonyAnalyticsUtil.deleteOverflowAndOldData(TelephonyAnalyticsDatabase.CallAnalyticsTable.TABLE_NAME, "_id IN  ( SELECT _id FROM CallDataLogs ORDER BY LogDate DESC LIMIT -1 OFFSET ? )", "LogDate < ? ");
            this.mDateOfDeletedRecordsCallTable = str;
        }
    }

    public void setDateOfDeletedRecordsCallTable(String str) {
        this.mDateOfDeletedRecordsCallTable = str;
    }

    private ArrayList dumpInformationInList(long j, long j2, double d, long j3, double d2, long j4, double d3, String str, String str2, HashMap map, HashMap map2, HashMap map3, HashMap map4) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("Normal Call Stats");
        arrayList.add("\tTotal Normal Calls = " + j3);
        StringBuilder sb = new StringBuilder("\tPercentage Failure of Normal Calls = ");
        DecimalFormat decimalFormat = DECIMAL_FORMAT;
        sb.append(decimalFormat.format(d2));
        sb.append("%");
        arrayList.add(sb.toString());
        CallType callType = CallType.NORMAL;
        addFailureStatsFromHashMap(map, callType.value, j3, "FailureReason", arrayList);
        addFailureStatsFromHashMap(map3, callType.value, j3, "RAT", arrayList);
        if (j4 > 0) {
            arrayList.add("SOS Call Stats");
            arrayList.add("\tTotal SOS Calls = " + j4);
            arrayList.add("\tPercentage Failure of SOS Calls = " + decimalFormat.format(d3) + "%");
            CallType callType2 = CallType.SOS;
            addFailureStatsFromHashMap(map2, callType2.value, j4, "FailureReason", arrayList);
            addFailureStatsFromHashMap(map4, callType2.value, j4, "RAT", arrayList);
        }
        if (j2 != 0) {
            arrayList.add("\tMax Call(Normal+SOS) Failures at Version : " + str);
            arrayList.add("\tMax Call(Normal+SOS) Failures at Network Type: " + str2);
        }
        return arrayList;
    }

    private void addFailureStatsFromHashMap(HashMap map, final String str, final long j, final String str2, final ArrayList arrayList) {
        map.forEach(new BiConsumer() { // from class: com.android.internal.telephony.analytics.CallAnalyticsProvider$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                Integer num = (Integer) obj2;
                arrayList.add("\tNo. of " + str + " failures at " + str2 + " : " + ((String) obj) + " = " + num + ", Percentage = " + CallAnalyticsProvider.DECIMAL_FORMAT.format((((double) num.intValue()) / j) * 100.0d) + "%");
            }
        });
    }

    @Override // com.android.internal.telephony.analytics.TelephonyAnalyticsProvider
    public ArrayList<String> aggregate() {
        long jCountTotalCalls = countTotalCalls();
        long jCountFailedCalls = countFailedCalls();
        double d = (jCountFailedCalls / jCountTotalCalls) * 100.0d;
        String maxFailureVersion = getMaxFailureVersion();
        String maxFailureNetworkType = getMaxFailureNetworkType();
        CallType callType = CallType.NORMAL;
        HashMap failureCountByReasonForCallType = getFailureCountByReasonForCallType(callType.value);
        CallType callType2 = CallType.SOS;
        HashMap failureCountByReasonForCallType2 = getFailureCountByReasonForCallType(callType2.value);
        HashMap failureCountByRatForCallType = getFailureCountByRatForCallType(callType.value);
        HashMap failureCountByRatForCallType2 = getFailureCountByRatForCallType(callType2.value);
        long jCountNormalCalls = countNormalCalls();
        double dCountFailedNormalCalls = (countFailedNormalCalls() / jCountNormalCalls) * 100.0d;
        long jCountSosCalls = countSosCalls();
        return dumpInformationInList(jCountTotalCalls, jCountFailedCalls, d, jCountNormalCalls, dCountFailedNormalCalls, jCountSosCalls, (countFailedSosCalls() / jCountSosCalls) * 100.0d, maxFailureVersion, maxFailureNetworkType, failureCountByReasonForCallType, failureCountByReasonForCallType2, failureCountByRatForCallType, failureCountByRatForCallType2);
    }
}
