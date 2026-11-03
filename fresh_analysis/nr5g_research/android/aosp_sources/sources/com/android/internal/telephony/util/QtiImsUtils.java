package com.android.internal.telephony.util;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.provider.Settings;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.ims.ImsCallProfile;
import android.util.Log;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.Phone;

/* JADX INFO: loaded from: classes.dex */
public class QtiImsUtils {
    public static final int CODE_IS_NOT_PS_ATTACHED = 4002;
    public static final int CODE_IS_PS_ATTACHED = 4001;
    public static final int CODE_RETRY_ON_IMS_WITHOUT_RTT = 3001;
    public static final int DOMAIN_AUTOMATIC = 0;
    public static final int DOMAIN_CS = 1;
    public static final int DOMAIN_PS = 2;
    public static final String EXTRA_CALL_DOMAIN = "org.codeaurora.extra.CALL_DOMAIN";
    public static final String EXTRA_EMERGENCY_SERVICE_CATEGORY = "EmergencyServiceCategory";
    public static final String EXTRA_PHONE_ID = "slotId";
    public static final String EXTRA_RETRY_CALL_FAIL_RADIOTECH = "RetryCallFailRadioTech";
    public static final String EXTRA_RETRY_CALL_FAIL_REASON = "RetryCallFailReason";
    public static final String EXTRA_RETRY_ON_IMS_WITHOUT_RTT = "retryOnImsWithoutRTT";
    public static final String KEY_SHOW_RTT_VISIBILITY_SETTING = "show_rtt_visibility_setting_bool";
    public static final String QTI_IMS_CAN_START_RTT_CALL = "qti.settings.can_start_rtt_call";
    public static final String QTI_IMS_RTT_OPERATING_MODE = "qti.settings.rtt_operation";
    public static final int RETRY_ON_IMS_WITHOUT_RTT = 301;
    public static final int RTT_AUTOMATIC_MODE = 1;
    public static final int RTT_CALL_TYPE_RTT = 0;
    public static final int RTT_DEFAULT_PHONE_ID = 0;
    public static final int RTT_DOWNGRADE_NOT_SUPPORTED = 0;
    public static final int RTT_DOWNGRADE_SUPPORTED = 1;
    public static final int RTT_MODE_DISABLED = 0;
    public static final int RTT_NOT_SUPPORTED = 0;
    public static final int RTT_SUPPORTED = 1;
    public static final int RTT_UPON_REQUEST_MODE = 0;
    public static final String SIMLESS_RTT_DOWNGRADE_SUPPORTED = "simless_rtt_downgrade_supported";
    public static final String SIMLESS_RTT_SUPPORTED = "simless_rtt_supported";

    public static boolean isRttOn(Context context) {
        return isRttOn(0, context);
    }

    public static boolean isRttOn(int i, Context context) {
        return getRttMode(context, i) != 0;
    }

    public static int getRttMode(Context context) {
        return getRttMode(context, 0);
    }

    public static int getRttMode(Context context, int i) {
        return Settings.Secure.getInt(context.getContentResolver(), "rtt_calling_mode" + convertRttPhoneId(i), 0);
    }

    private static String convertRttPhoneId(int i) {
        return i != 0 ? Integer.toString(i) : "";
    }

    public static boolean isRttSupportedOnVtCalls(int i, Context context) {
        PersistableBundle configForPhoneId = getConfigForPhoneId(context, i);
        if (configForPhoneId != null) {
            return configForPhoneId.getBoolean("rtt_supported_for_vt_bool");
        }
        return false;
    }

    public static boolean isRttUpgradeSupported(int i, Context context) {
        PersistableBundle configForPhoneId = getConfigForPhoneId(context, i);
        if (configForPhoneId != null) {
            return configForPhoneId.getBoolean("rtt_upgrade_supported_bool");
        }
        return false;
    }

    public static boolean shallShowRttVisibilitySetting(int i, Context context) {
        PersistableBundle configForPhoneId = getConfigForPhoneId(context, i);
        if (configForPhoneId != null) {
            return configForPhoneId.getBoolean(KEY_SHOW_RTT_VISIBILITY_SETTING);
        }
        return false;
    }

    public static boolean isRttSupported(int i, Context context) {
        PersistableBundle configForPhoneId = getConfigForPhoneId(context, i);
        if (configForPhoneId != null) {
            return configForPhoneId.getBoolean("rtt_supported_bool");
        }
        return false;
    }

    public static boolean isSimLessRttSupported(int i, Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        StringBuilder sb = new StringBuilder(SIMLESS_RTT_SUPPORTED);
        sb.append(convertRttPhoneId(i));
        return Settings.Secure.getInt(contentResolver, sb.toString(), 0) != 0;
    }

    private static PersistableBundle getConfigForPhoneId(Context context, int i) {
        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService("telephony_subscription_service");
        if (subscriptionManager == null) {
            Log.e("QtiImsUtils", "getConfigForPhoneId SubscriptionManager is null");
            return null;
        }
        SubscriptionInfo activeSubscriptionInfoForSimSlotIndex = subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(i);
        if (activeSubscriptionInfoForSimSlotIndex == null) {
            Log.e("QtiImsUtils", "getConfigForPhoneId subInfo is null");
            return null;
        }
        int subscriptionId = activeSubscriptionInfoForSimSlotIndex.getSubscriptionId();
        if (!subscriptionManager.isActiveSubscriptionId(subscriptionId)) {
            Log.e("QtiImsUtils", "getConfigForPhoneId subscription is not active");
            return null;
        }
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) context.getSystemService("carrier_config");
        if (carrierConfigManager == null) {
            Log.e("QtiImsUtils", "getConfigForPhoneId CarrierConfigManager is null");
            return null;
        }
        return carrierConfigManager.getConfigForSubId(subscriptionId);
    }

    public static void updateRttConfigCache(Context context, int i, PersistableBundle persistableBundle) {
        Settings.Secure.putInt(context.getContentResolver(), SIMLESS_RTT_SUPPORTED + convertRttPhoneId(i), persistableBundle.getBoolean("rtt_supported_bool") ? 1 : 0);
        Settings.Secure.putInt(context.getContentResolver(), SIMLESS_RTT_DOWNGRADE_SUPPORTED + convertRttPhoneId(i), persistableBundle.getBoolean("rtt_downgrade_supported_bool") ? 1 : 0);
    }

    public static int getRttOperatingMode(Context context) {
        return getRttOperatingMode(0, context);
    }

    public static int getRttOperatingMode(int i, Context context) {
        if (shallShowRttVisibilitySetting(i, context)) {
            return 1;
        }
        return Settings.Global.getInt(context.getContentResolver(), QTI_IMS_RTT_OPERATING_MODE + convertRttPhoneId(i), 1);
    }

    public static boolean canStartRttCall(int i, Context context) {
        if (!shallShowRttVisibilitySetting(i, context)) {
            return true;
        }
        ContentResolver contentResolver = context.getContentResolver();
        StringBuilder sb = new StringBuilder(QTI_IMS_CAN_START_RTT_CALL);
        sb.append(convertRttPhoneId(i));
        return Settings.Global.getInt(contentResolver, sb.toString(), 0) == 0;
    }

    public static boolean isSystemUser() {
        return UserHandle.of(ActivityManager.getCurrentUser()).isSystem();
    }

    public static void throwExceptionForSupplementaryService() throws CallStateException {
        Log.e("QtiImsUtils", "Supplementary service not allowed in non-primary mode");
        throw new CallStateException("Supplementary service is not allowed for non-primary user");
    }

    public static String getSubscriberUriNumber(Uri[] uriArr) {
        String strExtractPhoneNumber;
        if (uriArr == null || uriArr.length == 0) {
            Log.e("QtiImsUtils", "mCurrentSubscriberUris is null");
            return null;
        }
        for (Uri uri : uriArr) {
            if (uri != null && (strExtractPhoneNumber = extractPhoneNumber(uri)) != null) {
                return strExtractPhoneNumber;
            }
        }
        return null;
    }

    private static String extractPhoneNumber(Uri uri) {
        String schemeSpecificPart = uri.getSchemeSpecificPart();
        if (schemeSpecificPart == null) {
            return null;
        }
        String[] strArrSplit = schemeSpecificPart.split("[@;:]");
        if (strArrSplit.length == 0) {
            Log.e("QtiImsUtils", "extractPhoneNumber(N) : no number in uri");
            return null;
        }
        return strArrSplit[0];
    }

    public static boolean canMakeRttCall(ImsCallProfile imsCallProfile, boolean z, Phone phone, boolean z2) {
        if (phone == null) {
            return false;
        }
        Phone defaultPhone = phone.getDefaultPhone();
        boolean zIccCardExist = defaultPhone.getIccCard().getState().iccCardExist();
        int phoneId = phone.getPhoneId();
        Context context = phone.getContext();
        if ((zIccCardExist && !isRttSupported(phoneId, context)) || ((!zIccCardExist && z && !isSimLessRttSupported(phoneId, context)) || !isRttOn(phoneId, context))) {
            return false;
        }
        if (imsCallProfile == null || !imsCallProfile.isVideoCall() || isRttSupportedOnVtCalls(phoneId, context)) {
            return !defaultPhone.getServiceState().getRoaming() || z2 || (phone.getImsRegistrationTech() == 1 || phone.getImsRegistrationTech() == 2) || z;
        }
        return false;
    }
}
