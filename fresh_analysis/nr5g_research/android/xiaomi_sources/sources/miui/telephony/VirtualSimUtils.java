package miui.telephony;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.telephony.Rlog;
import android.text.TextUtils;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccPort;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class VirtualSimUtils {
    public static final String CLOUD_SIM_IMSI_KEY = "cloudsim_sim_imsi";
    public static final String KEY_CARRIER_NAME = "carrierName";
    public static final String KEY_SETTINGS_ENTRANCE = "setting_entrance";
    public static final String METHOD_GET_CARRIER_NAME = "getCarrierName";
    public static final String METHOD_GET_SETTINGS_ENTRANCE_INTENT = "getSettingsEntranceIntent";
    private static final String TAG = "VirtualSimUtils";
    public static final String VIRTUAL_SIM_CONTENT = "content://com.miui.virtualsim.provider.virtualsimInfo";
    private static String sCloudSimImsi;
    private static VirtualSimUtils sInstance;
    private static boolean sIsCloudSimEnabled;
    private ArrayList<VirtualSimListenner> mListeners = new ArrayList<>(2);
    private boolean mIsVirtualSimEnabled = MiuiSettings.VirtualSim.isVirtualSimEnabled(MiuiTelephony.getInstance().getContext());
    private int mVirtualSimStatus = MiuiSettings.VirtualSim.getVirtualSimStatus(MiuiTelephony.getInstance().getContext());

    public interface VirtualSimListenner {
        void onVirtualSimPreciseStateChanged();

        void onVirtualSimStateChanged();
    }

    private VirtualSimUtils() {
        Rlog.d(TAG, "init mIsVirtualSimEnabled=" + this.mIsVirtualSimEnabled + " mVirtualSimStatus=" + this.mVirtualSimStatus);
        Handler handler = null;
        ContentObserver virtualSimObserver = new ContentObserver(handler) { // from class: miui.telephony.VirtualSimUtils.1
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, Uri uri) {
                VirtualSimUtils.this.mIsVirtualSimEnabled = MiuiSettings.VirtualSim.isVirtualSimEnabled(MiuiTelephony.getInstance().getContext());
                VirtualSimUtils.this.mVirtualSimStatus = MiuiSettings.VirtualSim.getVirtualSimStatus(MiuiTelephony.getInstance().getContext());
                if (uri.toString().contains("virtual_sim_imsi")) {
                    Rlog.d(VirtualSimUtils.TAG, "onVirtualSimStateChanged mIsVirtualSimEnabled=" + VirtualSimUtils.this.mIsVirtualSimEnabled);
                    for (VirtualSimListenner listener : VirtualSimUtils.this.mListeners) {
                        listener.onVirtualSimStateChanged();
                    }
                    return;
                }
                if (uri.toString().contains("virtual_sim_status")) {
                    Rlog.d(VirtualSimUtils.TAG, "onVirtualSimPreciseStateChanged mVirtualSimStatus=" + VirtualSimUtils.this.mVirtualSimStatus);
                    for (VirtualSimListenner listener2 : VirtualSimUtils.this.mListeners) {
                        listener2.onVirtualSimPreciseStateChanged();
                    }
                }
            }
        };
        MiuiTelephony.getInstance().getContext().getContentResolver().registerContentObserver(Settings.Secure.getUriFor("virtual_sim_imsi"), false, virtualSimObserver);
        MiuiTelephony.getInstance().getContext().getContentResolver().registerContentObserver(Settings.Secure.getUriFor("virtual_sim_status"), false, virtualSimObserver);
        sCloudSimImsi = Settings.Global.getString(MiuiTelephony.getInstance().getContext().getContentResolver(), CLOUD_SIM_IMSI_KEY);
        sIsCloudSimEnabled = !TextUtils.isEmpty(sCloudSimImsi);
        ContentObserver cloudSimObserver = new ContentObserver(this, handler) { // from class: miui.telephony.VirtualSimUtils.2
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, Uri uri) {
                VirtualSimUtils.sCloudSimImsi = Settings.Global.getString(MiuiTelephony.getInstance().getContext().getContentResolver(), VirtualSimUtils.CLOUD_SIM_IMSI_KEY);
                VirtualSimUtils.sIsCloudSimEnabled = !TextUtils.isEmpty(VirtualSimUtils.sCloudSimImsi);
                Rlog.d(VirtualSimUtils.TAG, "mIsCloudSimEnabled" + VirtualSimUtils.sIsCloudSimEnabled);
            }
        };
        MiuiTelephony.getInstance().getContext().getContentResolver().registerContentObserver(Settings.Global.getUriFor(CLOUD_SIM_IMSI_KEY), false, cloudSimObserver);
    }

    static void init() {
        if (sInstance == null) {
            sInstance = new VirtualSimUtils();
        }
    }

    public static VirtualSimUtils getInstance() {
        return sInstance;
    }

    public static boolean isDcOnlyVirtualSim(Context context) {
        return MiuiSettings.VirtualSim.isVirtualSimEnabled(context) && MiuiSettings.VirtualSim.getVirtualSimType(context) == 1;
    }

    public static boolean isVirtualSim(Context context, int slotId) {
        return MiuiSettings.VirtualSim.isVirtualSimEnabled(context) && slotId == MiuiSettings.VirtualSim.getVirtualSimSlotId(context);
    }

    public static int getVirtualSimSlot(Context context, boolean checkSimType, int virtualSimType) {
        if (MiuiSettings.VirtualSim.isVirtualSimEnabled(context)) {
            if (checkSimType && virtualSimType != MiuiSettings.VirtualSim.getVirtualSimType(context)) {
                return SubscriptionManager.INVALID_SLOT_ID;
            }
            String iccId = MiuiSettings.VirtualSim.getVirtualSimIccId(context);
            if (iccId == null || iccId.isEmpty()) {
                return SubscriptionManager.INVALID_SLOT_ID;
            }
            int virtualSlot = MiuiSettings.VirtualSim.getVirtualSimSlotId(context);
            UiccCard card = UiccController.getInstance().getUiccCard(virtualSlot);
            UiccPort port = card != null ? card.getUiccPortForPhone(virtualSlot) : null;
            if (port != null && iccId.equalsIgnoreCase(port.getIccId())) {
                return virtualSlot;
            }
        }
        return SubscriptionManager.INVALID_SLOT_ID;
    }

    public static String getVirtualSimCarrierName(Context context) {
        Bundle b = null;
        try {
            b = context.getContentResolver().call(Uri.parse(VIRTUAL_SIM_CONTENT), METHOD_GET_CARRIER_NAME, (String) null, (Bundle) null);
        } catch (Exception e) {
            Rlog.d(TAG, "getVirtualSimCarrierName e" + e);
        }
        if (b == null) {
            return null;
        }
        return b.getString(KEY_CARRIER_NAME);
    }

    public static Intent getVirtualSimIntent(Context context) {
        Bundle b = null;
        try {
            b = context.getContentResolver().call(Uri.parse(VIRTUAL_SIM_CONTENT), METHOD_GET_SETTINGS_ENTRANCE_INTENT, (String) null, (Bundle) null);
        } catch (Exception e) {
            Rlog.e(TAG, "getVirtualSimIntent " + e);
        }
        if (b == null) {
            return null;
        }
        return (Intent) b.getParcelable(KEY_SETTINGS_ENTRANCE);
    }

    public static boolean isMiSimEnabled(Context context, int slotId) {
        return MiuiSettings.VirtualSim.isMiSimEnabled(context) && slotId == getVirtualSimSlot(context, false, 1);
    }

    public static boolean isValidApnForMiSim(Context context, int slotId, String apn) {
        if (isMiSimEnabled(context, slotId)) {
            String miSimImsi = MiuiSettings.VirtualSim.getVirtualSimImsi(context);
            if (ServiceProviderUtils.isChinaUnicom(miSimImsi)) {
                return "3gnet".equals(apn);
            }
            return true;
        }
        return true;
    }

    public void addVirtualSimChangedListener(VirtualSimListenner listener) {
        synchronized (this.mListeners) {
            this.mListeners.add(listener);
        }
    }

    public void removeVirtualSimChangedListener(VirtualSimListenner listener) {
        synchronized (this.mListeners) {
            this.mListeners.remove(listener);
        }
    }

    public int getVirtualSimSlotId() {
        if (this.mIsVirtualSimEnabled) {
            return MiuiSettings.VirtualSim.getVirtualSimSlotId(MiuiTelephony.getInstance().getContext());
        }
        return SubscriptionManager.INVALID_SLOT_ID;
    }

    public boolean isVirtualSimEnabled() {
        return this.mIsVirtualSimEnabled;
    }

    public boolean isDisablingVirtualSim() {
        return this.mVirtualSimStatus == 1;
    }

    public boolean isEnablingVirtualSim() {
        return this.mVirtualSimStatus == 0;
    }

    public static boolean isCloudSimEnabled() {
        return sIsCloudSimEnabled;
    }

    public static String getCloudSimImsi() {
        return sCloudSimImsi != null ? sCloudSimImsi : "";
    }
}
