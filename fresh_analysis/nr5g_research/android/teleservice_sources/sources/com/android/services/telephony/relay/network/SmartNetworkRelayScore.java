package com.android.services.telephony.relay.network;

import android.content.Context;
import com.android.telephony.Rlog;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import miui.telephony.FeatureConfiguration;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public class SmartNetworkRelayScore {
    private static final int CELL_TYPE_2G_CDMA = 1;
    private static final int CELL_TYPE_2G_GSM = 2;
    private static final int CELL_TYPE_3G_TDSCDMA = 4;
    private static final int CELL_TYPE_3G_WCDMA = 3;
    private static final int CELL_TYPE_4G = 5;
    private static final int CELL_TYPE_5G = 6;
    private static final int DURING_TIME_VALID = 5000;
    private static final String LOG_TAG = "SmartNetworkRelayScore";
    private static final int SCORE_CELL_STRENGTH_LEVEL_1_0 = 0;
    private static final int SCORE_CELL_STRENGTH_LEVEL_2 = 2;
    private static final int SCORE_CELL_STRENGTH_LEVEL_4_3 = 8;
    private static final int SCORE_CELL_STRENGTH_LEVEL_5 = 10;
    private static final int SCORE_CELL_TYPE_3G_2G_OOS = 0;
    private static final int SCORE_CELL_TYPE_4G = 8;
    private static final int SCORE_CELL_TYPE_5G = 10;
    private static final int SCORE_CONN_INVALID = 0;
    private static final int SCORE_CONN_VALID = 20;
    private static final int SCORE_NOT_ROAMING = 20;
    public static final int SCORE_PASS = 84;
    private static final int SCORE_ROAMING = 0;
    private static final int SCORE_TRAFFIC_INVALID = 0;
    private static final int SCORE_TRAFFIC_VALID = 20;
    private static final int SCORE_WIFI_STRENGTH_LEVEL_1 = 0;
    private static final int SCORE_WIFI_STRENGTH_LEVEL_2 = 2;
    private static final int SCORE_WIFI_STRENGTH_LEVEL_3 = 8;
    private static final int SCORE_WIFI_STRENGTH_LEVEL_4 = 10;
    private static final int SIGNAL_STRENGTH_GOOD = 4;
    private static final int SIGNAL_STRENGTH_GREAT = 5;
    private static final int SIGNAL_STRENGTH_MODERATE = 3;
    private static final int SIGNAL_STRENGTH_POOR = 2;
    public static final int TRAFFIC_UNIT = 1024;
    public static final double TRAFFIC_VALID = 1.0d;
    private static final int WIFI_RSSI_BAD = 1;
    private static final int WIFI_RSSI_GREAT = 4;
    private static final int WIFI_RSSI_GREAT_VALUE = -45;
    private static final int WIFI_RSSI_INVALID_VALUE = -1;
    private static final int WIFI_RSSI_NORMAL = 3;
    private static final int WIFI_RSSI_NORMAL_VALUE = -58;
    private static final int WIFI_RSSI_POOR = 2;
    private static final int WIFI_RSSI_POOR_VALUE = -62;
    private static long mDuringTime = 0;
    private static boolean mIsBadNetwork = false;

    public static int getWiFiLevel(int i) {
        if (i < -1 && i >= WIFI_RSSI_GREAT_VALUE) {
            return 4;
        }
        if (i < WIFI_RSSI_GREAT_VALUE && i >= WIFI_RSSI_NORMAL_VALUE) {
            return 3;
        }
        if (i >= WIFI_RSSI_NORMAL_VALUE || i < WIFI_RSSI_POOR_VALUE) {
            return WIFI_RSSI_POOR_VALUE > i ? 1 : 4;
        }
        return 2;
    }

    public static synchronized int getScore(NetworkRelayInfo networkRelayInfo) {
        int i;
        i = networkRelayInfo.mIsValid ? 20 : 0;
        if (transferTrafficGb(networkRelayInfo) >= 1.0d) {
            i += 20;
        }
        if (!networkRelayInfo.mIsDataRoaming) {
            i += 20;
        }
        int i2 = networkRelayInfo.mSignalType;
        if (i2 == 6) {
            i += 10;
        } else if (i2 == 5) {
            i += 8;
        }
        int i3 = networkRelayInfo.mSignalLevel;
        if (i3 == 5) {
            i += 10;
        } else if (i3 == 4 || i3 == 3) {
            i += 8;
        } else if (i3 == 2) {
            i += 2;
        }
        int i4 = networkRelayInfo.mWiFiSignalLevel;
        if (i4 == 4) {
            i += 10;
        } else if (i4 == 3) {
            i += 8;
        } else if (i4 == 2) {
            i += 2;
        }
        Rlog.d(LOG_TAG, "getScore >>>> score = " + i + "; \n  mIsValid = " + networkRelayInfo.mIsValid + "; \n  mTraffic = " + transferTrafficGb(networkRelayInfo) + "GB; \n  mIsDataRoaming= " + networkRelayInfo.mIsDataRoaming + "; \n  mSignalType = " + networkRelayInfo.mSignalType + "; \n  mSignalLevel = " + networkRelayInfo.mSignalLevel + "; \n  mWiFiSignalLevel = " + networkRelayInfo.mWiFiSignalLevel);
        return i;
    }

    private static double transferTrafficGb(NetworkRelayInfo networkRelayInfo) {
        if (networkRelayInfo != null) {
            return transferTrafficGb(networkRelayInfo.mTotalTrafficLimit, networkRelayInfo.mUsedMonthTraffic);
        }
        return 0.0d;
    }

    public static double transferTrafficGb(long j, long j2) {
        if (j == 0) {
            return 0.0d;
        }
        try {
            return (((j - j2) / 1024) / 1024) / 1024;
        } catch (Exception e) {
            Rlog.i(LOG_TAG, "transferTrafficGb error = " + e);
            return 0.0d;
        }
    }

    /* JADX WARN: Code duplicated, block: B:19:0x004b A[Catch: all -> 0x003a, TryCatch #0 {all -> 0x003a, blocks: (B:4:0x0009, B:6:0x002d, B:8:0x0031, B:10:0x0035, B:19:0x004b, B:21:0x0055, B:24:0x005a, B:26:0x0092, B:28:0x0096, B:30:0x009a, B:33:0x009f, B:35:0x00a3, B:44:0x00cb, B:46:0x00e2, B:36:0x00ab, B:38:0x00b1, B:40:0x00b5, B:41:0x00bd, B:43:0x00c4, B:15:0x003d, B:17:0x0043), top: B:51:0x0009 }] */
    /* JADX WARN: Code duplicated, block: B:41:0x00bd A[Catch: all -> 0x003a, TryCatch #0 {all -> 0x003a, blocks: (B:4:0x0009, B:6:0x002d, B:8:0x0031, B:10:0x0035, B:19:0x004b, B:21:0x0055, B:24:0x005a, B:26:0x0092, B:28:0x0096, B:30:0x009a, B:33:0x009f, B:35:0x00a3, B:44:0x00cb, B:46:0x00e2, B:36:0x00ab, B:38:0x00b1, B:40:0x00b5, B:41:0x00bd, B:43:0x00c4, B:15:0x003d, B:17:0x0043), top: B:51:0x0009 }] */
    /* JADX WARN: Code duplicated, block: B:43:0x00c4 A[Catch: all -> 0x003a, TryCatch #0 {all -> 0x003a, blocks: (B:4:0x0009, B:6:0x002d, B:8:0x0031, B:10:0x0035, B:19:0x004b, B:21:0x0055, B:24:0x005a, B:26:0x0092, B:28:0x0096, B:30:0x009a, B:33:0x009f, B:35:0x00a3, B:44:0x00cb, B:46:0x00e2, B:36:0x00ab, B:38:0x00b1, B:40:0x00b5, B:41:0x00bd, B:43:0x00c4, B:15:0x003d, B:17:0x0043), top: B:51:0x0009 }] */
    public static synchronized void handleMonitoring(Context context, NetworkRelayInfo networkRelayInfo) {
        try {
            long jCurrentTimeMillis = System.currentTimeMillis();
            Rlog.d(LOG_TAG, "handleMonitoring mDuringTime = " + ((jCurrentTimeMillis - mDuringTime) / 1000));
            if (!networkRelayInfo.mIsValid || networkRelayInfo.mSignalType < 5 || networkRelayInfo.mSignalLevel < 2 || networkRelayInfo.mWiFiSignalLevel < 2) {
                long j = mDuringTime;
                if (j != 0 && jCurrentTimeMillis - j >= MirrorDesktopHelper.TIMEOUT_MILLIS) {
                    mIsBadNetwork = true;
                    mDuringTime = 0L;
                    if (context != null) {
                        NetworkRelayChannelClient.getInstance(context).stopNetworkMonitoring();
                    }
                } else if (transferTrafficGb(networkRelayInfo) >= 1.0d || networkRelayInfo.mIsDataRoaming) {
                    mIsBadNetwork = true;
                    mDuringTime = 0L;
                    if (context != null) {
                        NetworkRelayChannelClient.getInstance(context).stopNetworkMonitoring();
                    }
                } else {
                    mIsBadNetwork = false;
                    Rlog.d(LOG_TAG, "handleMonitoring >>>> ACTIVE-MONITORING mIsValid = " + networkRelayInfo.mIsValid + "; mSignalType = " + networkRelayInfo.mSignalType + "; mSignalLevel = " + networkRelayInfo.mSignalLevel + "; mWiFiSignalLevel = " + networkRelayInfo.mWiFiSignalLevel);
                    if (!networkRelayInfo.mIsValid || networkRelayInfo.mSignalType < 5 || networkRelayInfo.mSignalLevel < 2 || networkRelayInfo.mWiFiSignalLevel < 2) {
                        if (mDuringTime == 0) {
                            mDuringTime = jCurrentTimeMillis;
                        }
                        if (context != null) {
                            NetworkRelayChannelClient.getInstance(context).startNetworkMonitoring();
                        }
                    } else {
                        mDuringTime = 0L;
                        if (context != null) {
                            NetworkRelayChannelClient.getInstance(context).stopNetworkMonitoring();
                        }
                    }
                }
            } else if (transferTrafficGb(networkRelayInfo) >= 1.0d) {
                mIsBadNetwork = true;
                mDuringTime = 0L;
                if (context != null) {
                    NetworkRelayChannelClient.getInstance(context).stopNetworkMonitoring();
                }
            } else {
                mIsBadNetwork = true;
                mDuringTime = 0L;
                if (context != null) {
                    NetworkRelayChannelClient.getInstance(context).stopNetworkMonitoring();
                }
            }
            Rlog.d(LOG_TAG, "handleMonitoring mIsBadNetwork = " + mIsBadNetwork);
            if (mIsBadNetwork) {
                NetworkRelayChannelClient.mSmartNetworkRelayState = 2;
                NetworkRelayClientController.getInstance(context).requestDataUsage();
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public static void stopMonitoring(Context context) {
        mIsBadNetwork = false;
        mDuringTime = 0L;
    }

    public static boolean isSmartNetworkRelaySupport() {
        return TelephonyManager.getDefault().isFeatureSupported(FeatureConfiguration.FeatureType.FEATURE_SMART_RELAY_NETWORK);
    }
}
