package com.android.phone;

import android.provider.DeviceConfig;
import com.android.services.telephony.relay.offlinecall.OfflineUtils;

/* JADX INFO: loaded from: classes.dex */
public abstract class DataCollectorConfig {
    private static String DUMPSYS_PROC_TIMEOUT_MILLIS = "dumpsys_proc_timeout_millis";
    private static String DUMPSYS_READ_TIMEOUT_MILLIS = "dumpsys_read_timeout_millis";
    private static String LOGCAT_PROC_TIMEOUT_MILLIS = "logcat_proc_timeout_millis";
    private static String LOGCAT_READ_TIMEOUT_MILLIS = "logcat_read_timeout_millis";
    private static String MAX_LOGCAT_LINES = "max_logcat_lines";
    private static String MAX_LOGCAT_LINES_LOW_MEM = "max_logcat_lines_low_mem";

    public static int getMaxLogcatLinesForLowMemDevice() {
        return DeviceConfig.getInt("telephony", MAX_LOGCAT_LINES_LOW_MEM, 2000);
    }

    public static int getMaxLogcatLines() {
        return DeviceConfig.getInt("telephony", MAX_LOGCAT_LINES, OfflineUtils.Offline.SAMPLE_RATE_VALUE_8KHZ);
    }

    public static long getLogcatReadTimeoutMillis() {
        return DeviceConfig.getLong("telephony", LOGCAT_READ_TIMEOUT_MILLIS, 500L);
    }

    public static long getDumpsysReadTimeoutMillis() {
        return DeviceConfig.getLong("telephony", DUMPSYS_READ_TIMEOUT_MILLIS, 100L);
    }

    public static long getLogcatProcTimeoutMillis() {
        return DeviceConfig.getLong("telephony", LOGCAT_PROC_TIMEOUT_MILLIS, 500L);
    }

    public static long getDumpsysProcTimeoutMillis() {
        return DeviceConfig.getLong("telephony", DUMPSYS_PROC_TIMEOUT_MILLIS, 100L);
    }

    public class Adapter {
        public int getMaxLogcatLinesForLowMemDevice() {
            return DataCollectorConfig.getMaxLogcatLinesForLowMemDevice();
        }

        public int getMaxLogcatLines() {
            return DataCollectorConfig.getMaxLogcatLines();
        }

        public long getLogcatReadTimeoutMillis() {
            return DataCollectorConfig.getLogcatReadTimeoutMillis();
        }

        public long getDumpsysReadTimeoutMillis() {
            return DataCollectorConfig.getDumpsysReadTimeoutMillis();
        }

        public long getLogcatProcTimeoutMillis() {
            return DataCollectorConfig.getLogcatProcTimeoutMillis();
        }

        public long getDumpsysProcTimeoutMillis() {
            return DataCollectorConfig.getDumpsysProcTimeoutMillis();
        }
    }
}
