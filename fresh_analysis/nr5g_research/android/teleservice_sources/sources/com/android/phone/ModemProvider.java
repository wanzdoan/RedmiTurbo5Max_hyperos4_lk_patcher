package com.android.phone;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.provider.MiuiSettings;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.ISub;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccPort;
import gov.nist.javax.sip.address.GenericURI;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionInfo;
import miui.util.FeatureParser;

/* JADX INFO: loaded from: classes.dex */
public class ModemProvider {
    public static final boolean IS_MTK;
    public static final boolean IS_QCOM;
    public static final boolean IS_SPRD;
    public static final boolean IS_XRING;
    public static final String PROPERTY_MODEM_PROVIDER;
    protected static volatile ModemProvider mInstance;
    private Context mContext = PhoneGlobals.getInstance();
    private Handler mMainHandler;

    static {
        String str = SystemProperties.get("ro.vendor.radio.modem.provider", FeatureParser.getString("vendor"));
        PROPERTY_MODEM_PROVIDER = str;
        IS_XRING = "xring".equals(str);
        IS_MTK = "mediatek".equals(str);
        IS_QCOM = "qcom".equals(str);
        IS_SPRD = "sprd".equals(str);
    }

    public static ModemProvider getInstance() {
        if (mInstance == null) {
            synchronized (ModemProvider.class) {
                try {
                    if (mInstance == null) {
                        mInstance = new ModemProvider();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return mInstance;
    }

    private ModemProvider() {
    }

    public int getDefaultVoiceSlotId() {
        return SubscriptionManager.getSlotIndex(SubscriptionManager.getDefaultVoiceSubscriptionId());
    }

    public int getPreferredNetworkModeFromDb(int i) {
        return DefaultSimManager.getPreferredNetworkModeFromDb(this.mContext, i);
    }

    public void setNetworkModeInDb(int i, int i2) {
        DefaultSimManager.setNetworkModeInDb(i, i2);
    }

    public String getSimImsi(int i) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null || phone.getIccRecords() == null) {
            return null;
        }
        return phone.getIccRecords().getIMSI();
    }

    public int getVirtualSimSlot(Context context, boolean z, int i) {
        String virtualSimIccId;
        if (MiuiSettings.VirtualSim.isVirtualSimEnabled(context) && ((!z || i == MiuiSettings.VirtualSim.getVirtualSimType(context)) && (virtualSimIccId = MiuiSettings.VirtualSim.getVirtualSimIccId(context)) != null && !virtualSimIccId.isEmpty())) {
            int virtualSimSlotId = MiuiSettings.VirtualSim.getVirtualSimSlotId(context);
            UiccCard uiccCard = UiccController.getInstance().getUiccCard(virtualSimSlotId);
            UiccPort uiccPortForPhone = uiccCard != null ? uiccCard.getUiccPortForPhone(virtualSimSlotId) : null;
            if (uiccPortForPhone != null && virtualSimIccId.equalsIgnoreCase(uiccPortForPhone.getIccId())) {
                return virtualSimSlotId;
            }
        }
        return -1;
    }

    public boolean setIccCardActivate(int i, boolean z) {
        try {
            ISub iSubAsInterface = ISub.Stub.asInterface(ServiceManager.getService(GenericURI.ISUB));
            if (iSubAsInterface == null) {
                return false;
            }
            iSubAsInterface.setUiccApplicationsEnabled(z, getSubIdFromSlotId(i));
            return true;
        } catch (Exception e) {
            Rlog.e("ModemProvider", "setIccCardActivate of slot " + i + ":" + e);
            return false;
        }
    }

    private int getSubIdFromSlotId(int i) {
        for (SubscriptionInfo subscriptionInfo : miui.telephony.SubscriptionManager.getDefault().getAvailableSubscriptionInfoList()) {
            if (subscriptionInfo.getSlotId() == i) {
                return subscriptionInfo.getSubscriptionId();
            }
        }
        return -1;
    }

    public Handler getMainHandler() {
        if (this.mMainHandler == null) {
            this.mMainHandler = new Handler(Looper.getMainLooper());
        }
        return this.mMainHandler;
    }
}
