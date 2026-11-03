package com.hyper.internal.telephony.network;

import android.telephony.PhysicalChannelConfig;
import android.telephony.Rlog;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.IHyperNetworkTypeManager;
import com.android.internal.telephony.NetworkTypeController;
import com.android.internal.telephony.Phone;
import com.hyper.internal.telephony.HyperTelephonyInternalManager;
import com.hyper.internal.telephony.ext.IHyperNetworkTypeManagerExt;
import java.util.List;
import miui.os.HyperOSCustFeatureResolve;

/* JADX INFO: loaded from: classes.dex */
public class HyperNetworkTypeManagerImpl implements IHyperNetworkTypeManager {
    private static final boolean DBG = true;
    private static final String KEY_SUPPORT_LTE_ADVANCE_TYPE = "support_lte_advance_type";
    private static final String KEY_SUPPORT_NR_ADVANCE_TYPE = "support_nr_advance_type";
    private static final String KEY_SUPPORT_REGISTER_FOR_LTE_ADVANCE_TYPE = "support_register_for_lte_advance_type";
    private static final String KEY_SUPPORT_REGISTER_FOR_NR_ADVANCE_TYPE = "support_register_for_nr_advance_type";
    private static final String LOG_TAG = "HyperNetworkTypeManagerImpl";
    private static final int PHONE_COUNT = TelephonyManager.getDefault().getActiveModemCount();
    private final IHyperNetworkTypeManagerExt mExt;
    private IHyperNetworkTypeManagerExt.AdvanceNetworkTypeListener mLteAdvanceNetworkTypeListener;
    private final NetworkTypeController mNetworkTypeController;
    private IHyperNetworkTypeManagerExt.AdvanceNetworkTypeListener mNrAdvanceNetworkTypeListener;
    private final Phone mPhone;
    private final int mPhoneId;

    public HyperNetworkTypeManagerImpl(Phone phone, NetworkTypeController networkTypeController) {
        if (phone == null) {
            throw new IllegalArgumentException("Phone is null");
        }
        this.mPhone = phone;
        this.mPhoneId = phone.getPhoneId();
        this.mNetworkTypeController = networkTypeController;
        this.mExt = HyperTelephonyInternalManager.getInstance().getHyperNetworkTypeManager(phone, networkTypeController);
        registerForAllEventsIfNeed();
    }

    private void registerForAllEventsIfNeed() {
        if (this.mPhone == null) {
            return;
        }
        if (isSupportRegisterForLteAdvanceType()) {
            this.mLteAdvanceNetworkTypeListener = new IHyperNetworkTypeManagerExt.AdvanceNetworkTypeListener() { // from class: com.hyper.internal.telephony.network.HyperNetworkTypeManagerImpl.1
                @Override // com.hyper.internal.telephony.ext.IHyperNetworkTypeManagerExt.AdvanceNetworkTypeListener
                public void onAdvanceNetworkTypeChanged(int advanceType) {
                    HyperNetworkTypeManagerImpl.this.updateLteAdvanceTypeChanged();
                }
            };
            this.mExt.registerForLteAdvanceTypeChange(this.mLteAdvanceNetworkTypeListener);
        }
        if (isSupportRegisterForNrAdvanceType()) {
            this.mNrAdvanceNetworkTypeListener = new IHyperNetworkTypeManagerExt.AdvanceNetworkTypeListener() { // from class: com.hyper.internal.telephony.network.HyperNetworkTypeManagerImpl.2
                @Override // com.hyper.internal.telephony.ext.IHyperNetworkTypeManagerExt.AdvanceNetworkTypeListener
                public void onAdvanceNetworkTypeChanged(int advanceType) {
                    HyperNetworkTypeManagerImpl.this.updateNrAdvanceTypeChanged();
                }
            };
            this.mExt.registerForNrAdvanceTypeChange(this.mNrAdvanceNetworkTypeListener);
        }
    }

    public int getCurrentOverrideNetworkType(boolean lte, boolean nrNsa, boolean nrSa, int overrideType) {
        return this.mExt.getCurrentOverrideNetworkType(lte, nrNsa, nrSa, overrideType);
    }

    protected void updateLteAdvanceTypeChanged() {
        if (isSupportLteAdvanceType(this.mPhoneId)) {
            log("updateLteAdvanceTypeChanged");
            this.mNetworkTypeController.sendMessage(0);
        }
    }

    protected void updateNrAdvanceTypeChanged() {
        if (isSupportNrAdvanceType(this.mPhoneId)) {
            log("updateNrAdvanceTypeChanged");
            this.mNetworkTypeController.sendMessage(0);
        }
    }

    public boolean isNrAdvanced(List<PhysicalChannelConfig> physicalChannelConfigs, boolean isPhysicalLinkActive) {
        return this.mExt.isNrAdvanced(physicalChannelConfigs, isPhysicalLinkActive);
    }

    public boolean isNrAdvanceNetworkTypeMatch(int dataNetworkType, List<PhysicalChannelConfig> physicalChannelConfigs) {
        return this.mExt.isNrAdvanceNetworkTypeMatch(dataNetworkType, physicalChannelConfigs);
    }

    private static boolean isSupportRegisterForLteAdvanceType() {
        return HyperOSCustFeatureResolve.getBoolean(KEY_SUPPORT_REGISTER_FOR_LTE_ADVANCE_TYPE, false);
    }

    private static boolean isSupportRegisterForNrAdvanceType() {
        return HyperOSCustFeatureResolve.getBoolean(KEY_SUPPORT_REGISTER_FOR_NR_ADVANCE_TYPE, false);
    }

    private static boolean isSupportLteAdvanceType(int phoneId) {
        return HyperOSCustFeatureResolve.getBooleanForPhoneId(phoneId, KEY_SUPPORT_LTE_ADVANCE_TYPE, false);
    }

    private static boolean isSupportNrAdvanceType(int phoneId) {
        return HyperOSCustFeatureResolve.getBooleanForPhoneId(phoneId, KEY_SUPPORT_NR_ADVANCE_TYPE, false);
    }

    private static boolean isValidatePhoneId(int phoneId) {
        if (phoneId < 0 || phoneId >= PHONE_COUNT) {
            return false;
        }
        return DBG;
    }

    protected void log(String msg) {
        Rlog.d("HyperNetworkTypeManagerImpl[" + this.mPhoneId + "]", msg);
    }

    protected void logw(String msg) {
        Rlog.w("HyperNetworkTypeManagerImpl[" + this.mPhoneId + "]", msg);
    }

    protected void loge(String msg) {
        Rlog.e("HyperNetworkTypeManagerImpl[" + this.mPhoneId + "]", msg);
    }
}
