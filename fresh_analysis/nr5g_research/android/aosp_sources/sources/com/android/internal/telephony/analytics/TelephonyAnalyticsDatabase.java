package com.android.internal.telephony.analytics;

import android.provider.BaseColumns;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/* JADX INFO: loaded from: classes.dex */
public final class TelephonyAnalyticsDatabase {
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());

    public static final class CallAnalyticsTable implements BaseColumns {
        public static final String CALL_STATUS = "CallStatus";
        public static final String CALL_TYPE = "CallType";
        public static final String COUNT = "Count";
        public static final String FAILURE_REASON = "FailureReason";
        public static final String LOG_DATE = "LogDate";
        public static final String RAT = "RAT";
        public static final String RELEASE_VERSION = "ReleaseVersion";
        public static final String SLOT_ID = "SlotID";
        public static final String TABLE_NAME = "CallDataLogs";
    }

    public static final class ServiceStateAnalyticsTable implements BaseColumns {
        public static final String DEVICE_STATUS = "DeviceStatus";
        public static final String LOG_DATE = "LogDate";
        public static final String RAT = "RAT";
        public static final String RELEASE_VERSION = "ReleaseVersion";
        public static final String SLOT_ID = "SlotID";
        public static final String TABLE_NAME = "ServiceStateLogs";
        public static final String TIME_DURATION = "TimeDuration";
    }

    public static final class SmsMmsAnalyticsTable implements BaseColumns {
        public static final String COUNT = "Count";
        public static final String FAILURE_REASON = "FailureReason";
        public static final String LOG_DATE = "LogDate";
        public static final String RAT = "RAT";
        public static final String RELEASE_VERSION = "ReleaseVersion";
        public static final String SLOT_ID = "SlotID";
        public static final String SMS_MMS_STATUS = "SmsMmsStatus";
        public static final String SMS_MMS_TYPE = "SmsMmsType";
        public static final String TABLE_NAME = "SmsMmsDataLogs";
    }
}
