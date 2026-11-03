package miui.telephony;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.telephony.RadioAccessFamily;
import android.telephony.Rlog;
import android.text.TextUtils;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;

/* JADX INFO: loaded from: classes.dex */
public class OtaLteUpdateManager {
    private static final int EVENT_FORCE_SET_LTE = 1;
    private static final int EVENT_FORCE_SET_VOLTE = 2;
    private static final String KEY_BUILD_VERSION_INCREMENTAL = "key_build_version_incremental";
    private static final String LOG_TAG = OtaLteUpdateManager.class.getSimpleName();
    private static final int WAITING_FORCE_SET_LTE_TIME = 10000;
    private static final int WAITING_FORCE_SET_VOLTE_TIME = 1000;
    private Handler mHandler = new Handler() { // from class: miui.telephony.OtaLteUpdateManager.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MiuiTelephony.getInstance().getContext());
                    if (sp != null) {
                        String oldVersion = sp.getString(OtaLteUpdateManager.KEY_BUILD_VERSION_INCREMENTAL, "");
                        String currentVersion = Build.VERSION.INCREMENTAL;
                        boolean isOTAUpdate = !TextUtils.equals(oldVersion, currentVersion);
                        Rlog.d(OtaLteUpdateManager.LOG_TAG, "oldVersion: " + oldVersion + ", currentVersion: " + currentVersion);
                        if (isOTAUpdate) {
                            for (Phone phone : PhoneFactory.getPhones()) {
                                if (phone != null) {
                                    int preferredMode = DefaultSimManager.getPreferredNetworkModeFromDb(MiuiTelephony.getInstance().getContext(), phone.getPhoneId());
                                    OtaLteUpdateManager.this.forceSetLteNetworkMode(preferredMode, phone);
                                }
                            }
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString(OtaLteUpdateManager.KEY_BUILD_VERSION_INCREMENTAL, currentVersion);
                            editor.commit();
                        }
                        break;
                    }
                    break;
                case 2:
                    OtaLteUpdateManager.this.forceEnableVolteSwitch(msg.arg1);
                    break;
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public void forceSetLteNetworkMode(int defaultNetworkMode, Phone phone) {
        String mccmnc = phone.getOperatorNumeric();
        int phoneId = phone.getPhoneId();
        Rlog.d(LOG_TAG, "slot: " + phoneId + ", defaultNetworkMode: " + defaultNetworkMode + ", mccmnc: " + mccmnc);
        if (shouldForceUpdateLte(mccmnc)) {
            if (isNetworkModeLessThanLte(defaultNetworkMode)) {
                DefaultSimManager.setNetworkModeInDb(phoneId, 9);
                phone.setAllowedNetworkTypes(0, RadioAccessFamily.getRafFromNetworkType(9), (Message) null);
                this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(2, phoneId, phoneId), 1000L);
                return;
            }
            forceEnableVolteSwitch(phoneId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void forceEnableVolteSwitch(int slotId) {
        boolean isVolteEnabledByPlatform = TelephonyManager.getDefault().isVolteEnabledByPlatform(slotId);
        boolean isVolteEnabledByUser = TelephonyManager.getDefault().isVolteEnabledByUser(slotId);
        Rlog.d(LOG_TAG, "slot: " + slotId + ", isVolteEnabledByPlatform: " + isVolteEnabledByPlatform + ", isVolteEnabledByUser: " + isVolteEnabledByUser);
        if (isVolteEnabledByPlatform && !isVolteEnabledByUser) {
            int subId = SubscriptionManager.getDefault().getSubscriptionIdForSlot(slotId);
            TelephonyManager.getDefault().setEnhanced4gLteModeSetting(MiuiTelephony.getInstance().getContext(), true, subId);
        }
    }

    private boolean isNetworkModeLessThanLte(int networkMode) {
        if (networkMode <= 7) {
            return true;
        }
        switch (networkMode) {
            case 13:
            case 14:
            case 16:
            case 18:
            case 21:
                return true;
            case 15:
            case 17:
            case 19:
            case 20:
            default:
                return false;
        }
    }

    public OtaLteUpdateManager() {
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1), 10000L);
    }

    private boolean shouldForceUpdateLte(String mccmnc) {
        return TextUtils.equals(mccmnc, "732101");
    }
}
