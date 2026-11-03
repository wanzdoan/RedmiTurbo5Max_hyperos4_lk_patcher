package miui.telephony;

import android.telephony.TelephonyDisplayInfo;
import android.util.Log;
import com.android.internal.telephony.Phone;
import miui.os.Build;
import miui.util.FeatureParser;

/* JADX INFO: loaded from: classes.dex */
public class CommenUtils {
    public static final String ACTION_ALARM_VERIFICATION_CODE = "com.android.app.action.ALARM_VERIFICATION_CODE_EVENT";
    public static final String ACTION_GET_VERIFICATION_CODE = "com.android.app.action.GET_VERIFICATION_CODE_EVENT";
    public static final String ACTION_RADIO_HEART_BEAT_IND = "xiaomi.intent.action.ACTION_RIL_STALL_DETECT_IND";
    public static final String ACTION_RADIO_STALL_DETECT = "miui.intent.action.RADIO_STALL_DETECT";
    public static final String ACTION_RADIO_STALL_STATISTIC = "com.android.phone.intent.action.RADIO_STALL_STATISTIC";
    public static final String ACTION_RECEIVE_VERIFICATION_CODE = "com.android.mms.action.RECEIVE_VERIFICATION_CODE_EVENT";
    public static final String ACTION_RESET_RADIO = "miui.intent.action.RESET_RADIO";
    public static final String ACTION_VERIFICATION_CODE = "com.android.internal.action.VERIFICATION_CODE";
    public static final String ACTIVE_RE_REGISTRATION_NETWORK = "miui.intent.action.ACTIVE_RE_REGISTRATION_NETWORK";
    public static final String AVOID_OOS_FOR_IMS = "IMS";
    public static final String AVOID_OOS_FOR_REFRESH_MBN = "REFRESH_MBN";
    public static final String AVOID_OOS_FOR_SST = "SST";
    public static final String BROADCAST_CONTEXT = "BROADCAST_CONTEXT";
    public static final String BROADCAST_SUB = "BROADCAST_SUB";
    public static final int DELAY_IMS_STATE_UPDATE_TIMER = 5000;
    public static final int DELAY_SERVICE_STATE_UPDATE_TIMER = 6000;
    public static final int DELAY_SERVICE_STATE_UPDATE_TIMER_FOR_O2S = 75000;
    public static final int DELAY_SERVICE_STATE_UPDATE_TIMER_FOR_REFRESH_MBN = 30000;
    public static final int DELAY_SET_MODEM_FEATURE_TIMER = 30000;
    private static final int DISPLAY_LEVEL_0 = 0;
    private static final int DISPLAY_LEVEL_1 = 1;
    private static final int DISPLAY_LEVEL_2 = 2;
    private static final int DISPLAY_LEVEL_UNKNOWN = -1;
    public static final String EXTRA_KEY_FAILED_CODE = "failedCode";
    public static final String EXTRA_KEY_REDULT_CODE = "resultCode";
    public static final String EXTRA_KEY_SCENE_CODE = "sceneCode";
    public static final String EXTRA_RESET_RILD = "reset_rild";
    public static final String EXTRA_RESET_RILD_FEATRUE = "reset_rild_feature";
    public static final String EXTRA_SCENE = "scene";
    public static final String EXTRA_STATUS = "status";
    public static final int HEART_BEAT_FAILED = 4;
    public static final int HEART_BEAT_SUCCESS = 3;
    public static final String KEY_COMMON_SIGNAL_OPTIMIZATION_COUNT = "commonSignalStrengthOptimizationCount";
    public static final String KEY_DISPLAY_ICON_NO_RECOVERY_COUNT = "displayIconNoRecoveryCount";
    public static final String KEY_DISPLAY_ICON_RECOVERY_COUNT = "displayIconRecoveryCount";
    public static final String KEY_NEIGHBOR_SIGNAL_OPTIMIZATION_COUNT = "neighborSignalStrengthOptimizationCount";
    public static final String KEY_OOS_RIL_MESSAGE_TIME_OUT_COUNT = "oosRilMessageTimeOutCount";
    public static final String KEY_REAL_SIGNAL_STRENGTH_BIG_CHANGE_COUNT = "realSignalStrengthBigChangeCount";
    public static final String KEY_REAL_SIGNAL_STRENGTH_POOR_COUNT = "realSignalStrengthPoorCount";
    public static final String KEY_RESET_RILD_COUNT = "resetRildCount";
    public static final String KEY_RESET_RILD_INVAILD_COUNT = "resetRildInvaildCount";
    public static final String KEY_RIL_MESSAGE_TIME_OUT = "rilMessageTimeOut";
    public static final String KEY_RIL_MESSAGE_TIME_OUT_COUNT = "rilMessageTimeOutCount";
    public static final String KEY_SCREEN_SIGNAL_OPTIMIZATION_COUNT = "lockedScreenSignalStrengthOptimizationCount";
    public static final String KEY_SMS_IS_VERIFICATION_SUCCESS = "is_verification_success";
    public static final String KEY_SMS_RECEIVE_VERIFICATION_CODE_PHONE_ID = "phone_id";
    public static final String KEY_SMS_VERIFICATION_CODE_APP_NAME = "app_name";
    public static final String KEY_SMS_VERIFICATION_CODE_NUMBER = "phone_number";
    public static final String KEY_SMS_VERIFICATION_CODE_PACKAGENAME = "app_package_name";
    public static final String KEY_SMS_VERIFICATION_CODE_TIME = "long_time";
    private static final String LOG_TAG = "TelephonyCommenUtils";
    public static final String REASON_RADIO_STALL_DETECT = "radio_stall_detect";
    public static final int REASON_RECOVERY_FAIL_MSG_BOTH_NOT_BACK = 401;
    public static final int REASON_RECOVERY_FAIL_MSG_IND_NOT_BACK = 402;
    public static final int REASON_RECOVERY_FAIL_MSG_RESP_NOT_BACK = 403;
    public static final int REASON_RECOVERY_FAIL_UNKNOWN = 400;
    public static final int RECOVERY_DONE = 2;
    public static final int RECOVERY_FAILED = 6;
    public static final int RECOVERY_IGNORE = 1;
    public static final int RECOVERY_SUCCESS = 5;
    public static final int REFRESH_MBN_DELAY_TIMER = 250;
    public static final String RE_REGISTRATION_NETWORK_REASON = "RE_REGISTRATION_NETWORK_REASON";
    public static final String SCENE_RADIO_INIT = "radioInit";
    public static final int STATUS_RESET_RADIO = 1;
    public static final int STATUS_STOP_RESET = 0;

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:11:0x001d  */
    public static int getReRegistrationDelayTimer(String scene, String reason) {
        byte b;
        int timer = 0;
        switch (scene.hashCode()) {
            case 72623:
                if (!scene.equals(AVOID_OOS_FOR_IMS)) {
                    b = -1;
                } else {
                    b = 0;
                }
                break;
            case 82420:
                if (!scene.equals(AVOID_OOS_FOR_SST)) {
                    b = -1;
                } else {
                    b = 1;
                }
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                timer = AVOID_OOS_FOR_REFRESH_MBN.equals(reason) ? 30000 : DELAY_IMS_STATE_UPDATE_TIMER;
                break;
            case 1:
                timer = AVOID_OOS_FOR_REFRESH_MBN.equals(reason) ? 30000 : DELAY_SERVICE_STATE_UPDATE_TIMER;
                break;
        }
        Log.d(LOG_TAG, "scene = " + scene + " , reason = " + reason + " , timer = " + timer);
        return timer;
    }

    public static int getExpectedDisplayIcon(TelephonyDisplayInfo telephonyDisplayInfo, Phone phone) {
        if (telephonyDisplayInfo == null) {
            return 0;
        }
        int networkType = telephonyDisplayInfo.getNetworkType();
        int overrideNetworkType = telephonyDisplayInfo.getOverrideNetworkType();
        if (networkType == 20) {
            if (overrideNetworkType != 5) {
                if (networkType != 20 || phone.getServiceState() == null || !phone.getServiceState().isUsingCarrierAggregation()) {
                    return 20;
                }
                return 30;
            }
            return 30;
        }
        if (networkType == 13) {
            if (overrideNetworkType == 2) {
                return 20;
            }
            if (overrideNetworkType != 1) {
                if (phone.getServiceState() != null) {
                    if (!phone.getServiceState().isUsingCarrierAggregation()) {
                        if (FeatureParser.getBoolean("support_ca", false) && Build.IS_CT_CUSTOMIZATION_TEST) {
                            return 19;
                        }
                    } else {
                        return 19;
                    }
                }
                return 13;
            }
            return 19;
        }
        return networkType;
    }

    private static int displayIconLevel(int networkType) {
        if (networkType == 30 || networkType == 19) {
            return 0;
        }
        if (networkType == 13) {
            return 1;
        }
        if (networkType == 20) {
            return 2;
        }
        return -1;
    }

    public static boolean isDisplayIconMoreStable(int newDisplayNetworkType, int lastDisplayNetworkType) {
        return displayIconLevel(newDisplayNetworkType) >= displayIconLevel(lastDisplayNetworkType);
    }

    public static boolean isXringModem() {
        try {
            String modemProvider = TelephonyManager.getDefault().getModemProvider();
            return "xring".equals(modemProvider);
        } catch (Exception e) {
            Log.e(LOG_TAG, "getModemProvider exception", e);
            return false;
        }
    }
}
