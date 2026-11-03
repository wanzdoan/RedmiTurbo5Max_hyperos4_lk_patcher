package com.hyper.internal.telephony;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhysicalChannelConfig;
import android.telephony.Rlog;
import com.android.internal.telephony.NetworkTypeController;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.util.ArrayUtils;
import com.hyper.internal.telephony.ext.IHyperNetworkTypeManagerExt;
import com.mediatek.internal.telephony.MtkRIL;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import java.util.ArrayList;
import java.util.List;
import miui.os.HyperOSCustFeatureResolve;

/* JADX INFO: loaded from: classes.dex */
public class HyperNetworkTypeManagerImplExt implements IHyperNetworkTypeManagerExt {
    private static final int EVENT_LTE_ADVANCE_TYPE_CHANGED = 1;
    private static final int EVENT_PHYSICAL_CHANNEL_CONFIGS_CHANGED = 2;
    private static final int EVENT_PHYSICAL_CHANNEL_CONFIGS_CHANGED_IN_NTC = 11;
    private static final String KEY_MTK_CUSTMIZED_NR_ADVANCED_TYPE = "mtk_custmized_nr_advanced_type";
    public static String TAG = HyperNetworkTypeManagerImplExt.class.getSimpleName();
    private final Handler mHandler;
    private final MtkRIL mMtkRil;
    private final NetworkTypeController mNetworkTypeController;
    private final Phone mPhone;
    private final int mPhoneId;
    private final String mTag;
    private int mLteAdvanceType = 0;
    private int mNrAdvanceType = 0;
    private List<IHyperNetworkTypeManagerExt.AdvanceNetworkTypeListener> mLteAdvanceNetworkTypeListeners = new ArrayList();
    private List<IHyperNetworkTypeManagerExt.AdvanceNetworkTypeListener> mNrAdvanceNetworkTypeListeners = new ArrayList();

    public HyperNetworkTypeManagerImplExt(Phone phone, NetworkTypeController networkTypeController) {
        if (phone == null) {
            throw new IllegalArgumentException("Phone is null");
        }
        this.mPhone = phone;
        this.mPhoneId = phone.getPhoneId();
        this.mTag = TAG + "[" + this.mPhoneId + "]";
        this.mNetworkTypeController = networkTypeController;
        this.mMtkRil = this.mPhone.mCi;
        this.mHandler = new Handler() { // from class: com.hyper.internal.telephony.HyperNetworkTypeManagerImplExt.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                HyperNetworkTypeManagerImplExt.this.log("handleMessage event = " + msg.what);
                switch (msg.what) {
                    case 1:
                        HyperNetworkTypeManagerImplExt.this.handleLteAdvanceTypeChange((AsyncResult) msg.obj);
                        break;
                    case 2:
                        HyperNetworkTypeManagerImplExt.this.handlePhysicalChannelConfigChange((AsyncResult) msg.obj);
                        break;
                    default:
                        super.handleMessage(msg);
                        break;
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLteAdvanceTypeChange(AsyncResult ar) {
        boolean qam_256;
        if (ar == null) {
            log("handleLteAdvanceTypeChange: ar is null");
            return;
        }
        if (ar.exception != null) {
            log("handleLteAdvanceTypeChange: exception = " + ar.exception);
            return;
        }
        if (ar.result == null) {
            log("handleLteAdvanceTypeChange: result is null");
            return;
        }
        try {
            Boolean[] response = (Boolean[]) ar.result;
            boolean qam_ul64 = false;
            boolean mimo = response[0] != null ? response[0].booleanValue() : false;
            if (response[1] == null) {
                qam_256 = false;
            } else {
                qam_256 = response[1].booleanValue();
            }
            if (response[2] != null) {
                qam_ul64 = response[2].booleanValue();
            }
            log("handleLteAdvanceTypeChange nwcfg mimo = " + mimo + " qam_256 = " + qam_256 + " qam_ul64 = " + qam_ul64 + " phoneId = " + this.mPhoneId);
            int lteAType = 0;
            if (mimo && qam_256 && isSupportLteCaPlus(this.mPhoneId)) {
                lteAType = 2;
            }
            if ((mimo || qam_256) && isSupportFourPointFive(this.mPhoneId)) {
                lteAType = 1;
            }
            if (lteAType != this.mLteAdvanceType) {
                this.mLteAdvanceType = lteAType;
                notifyLteAdvanceNetworkTypeListeners(lteAType);
            }
        } catch (Exception e) {
            loge("handleLteAdvanceTypeChange Error parsing data: " + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePhysicalChannelConfigChange(AsyncResult ar) {
        if (ar == null) {
            log("handlePhysicalChannelConfigChange: ar is null");
            return;
        }
        if (ar.exception != null) {
            log("handlePhysicalChannelConfigChange: exception = " + ar.exception);
            return;
        }
        if (ar.result == null) {
            log("handlePhysicalChannelConfigChange: result is null");
            return;
        }
        int nrAType = 0;
        try {
            List<PhysicalChannelConfig> physicalChannelConfigs = (List) ar.result;
            List<String> custmizedNrAdvanceTypeList = HyperOSCustFeatureResolve.getStringArrayForPhoneId(this.mPhoneId, KEY_MTK_CUSTMIZED_NR_ADVANCED_TYPE);
            if (!ArrayUtils.isEmpty(physicalChannelConfigs) && !ArrayUtils.isEmpty(custmizedNrAdvanceTypeList)) {
                log("handlePhysicalChannelConfigChange: physicalChannelConfigs = " + physicalChannelConfigs);
                int configCount = 0;
                int totalBandWidth = 0;
                int maxBandWidth = 0;
                for (PhysicalChannelConfig config : physicalChannelConfigs) {
                    if (config != null && config.getNetworkType() == 20) {
                        int bandwidth = config.getCellBandwidthDownlinkKhz();
                        configCount++;
                        totalBandWidth += bandwidth;
                        if (bandwidth >= maxBandWidth) {
                            maxBandWidth = bandwidth;
                        }
                    }
                }
                for (int i = 0; i < custmizedNrAdvanceTypeList.size(); i++) {
                    String[] custmizedNrAdvanceType = custmizedNrAdvanceTypeList.get(i).split(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE);
                    int targetConfigCount = Integer.MAX_VALUE;
                    int targetTotalBandWidth = Integer.MAX_VALUE;
                    int targetAnyBandWidth = Integer.MAX_VALUE;
                    if (custmizedNrAdvanceType.length == 3) {
                        targetConfigCount = Integer.parseInt(custmizedNrAdvanceType[0]);
                        targetTotalBandWidth = Integer.parseInt(custmizedNrAdvanceType[1]);
                        targetAnyBandWidth = Integer.parseInt(custmizedNrAdvanceType[2]);
                    }
                    if (configCount >= targetConfigCount && totalBandWidth >= targetTotalBandWidth && maxBandWidth >= targetAnyBandWidth) {
                        log("handlePhysicalChannelConfigChange configCount: " + configCount + "/" + targetConfigCount + ", Bandwidth: " + totalBandWidth + "/" + targetTotalBandWidth + ", maxBandWidth: " + maxBandWidth + "/" + targetAnyBandWidth);
                        nrAType = 1;
                        break;
                    }
                }
                if (nrAType != this.mNrAdvanceType) {
                    this.mNrAdvanceType = nrAType;
                    notifyNrAdvanceNetworkTypeListeners(nrAType);
                }
                return;
            }
            loge("No NR advanced type config found or physicalChannelConfigs is empty");
        } catch (Exception e) {
            loge("handlePhysicalChannelConfigChange Error parsing data: " + e.getMessage());
        }
    }

    @Override // com.hyper.internal.telephony.ext.IHyperNetworkTypeManagerExt
    public int getCurrentOverrideNetworkType(boolean lte, boolean nrNsa, boolean nrSa, int overrideType) {
        if (overrideType == 1 && this.mLteAdvanceType == 2) {
            overrideType = 2;
        } else if (lte && this.mLteAdvanceType == 1) {
            overrideType = 1;
        }
        if (this.mNrAdvanceType == 1) {
            return 5;
        }
        return overrideType;
    }

    @Override // com.hyper.internal.telephony.ext.IHyperNetworkTypeManagerExt
    public boolean isNrAdvanced(List<PhysicalChannelConfig> physicalChannelConfigs, boolean isPhysicalLinkActive) {
        if (!isSupportNrAdvanced(this.mPhoneId) || !isPhysicalLinkActive || physicalChannelConfigs == null || physicalChannelConfigs.isEmpty()) {
            return false;
        }
        for (PhysicalChannelConfig config : physicalChannelConfigs) {
            if (config != null && config.getCellBandwidthDownlinkKhz() >= 100000) {
                return true;
            }
        }
        return false;
    }

    @Override // com.hyper.internal.telephony.ext.IHyperNetworkTypeManagerExt
    public boolean isNrAdvanceNetworkTypeMatch(int dataNetworkType, List<PhysicalChannelConfig> physicalChannelConfigs) {
        if (!isSupportNrAdvanced(this.mPhoneId) || physicalChannelConfigs == null || physicalChannelConfigs.isEmpty()) {
            return true;
        }
        for (PhysicalChannelConfig config : physicalChannelConfigs) {
            if (config != null && config.getConnectionStatus() == 1 && config.getNetworkType() != dataNetworkType) {
                log("isNrAdvanceNetworkTypeMatch: physicalChannelConfigs should update later than servicestate.");
                this.mNetworkTypeController.sendMessageDelayed(this.mNetworkTypeController.obtainMessage(11, new ArrayList(physicalChannelConfigs)), 100L);
                return false;
            }
        }
        return true;
    }

    private boolean isSupportLteCaPlus(int phoneId) {
        return HyperOSCustFeatureResolve.getBooleanForPhoneId(phoneId, "support_lte_ca_plus", false);
    }

    private boolean isSupportFourPointFive(int phoneId) {
        return HyperOSCustFeatureResolve.getBooleanForPhoneId(phoneId, "support_lte_4_5_g", false);
    }

    private boolean isSupportNrAdvanced(int phoneId) {
        return HyperOSCustFeatureResolve.getBooleanForPhoneId(phoneId, "support_nr_advanced", false);
    }

    @Override // com.hyper.internal.telephony.ext.IHyperNetworkTypeManagerExt
    public void registerForLteAdvanceTypeChange(IHyperNetworkTypeManagerExt.AdvanceNetworkTypeListener listener) {
        addAdvanceNetworkTypeListener(this.mLteAdvanceNetworkTypeListeners, listener);
        if (this.mMtkRil != null) {
            log("registerForNwCfgInfoChanged...");
            this.mMtkRil.registerForNwCfgInfoChanged(this.mHandler, 1, this.mPhone);
        }
    }

    @Override // com.hyper.internal.telephony.ext.IHyperNetworkTypeManagerExt
    public void unregisterForLteAdvanceTypeChange(IHyperNetworkTypeManagerExt.AdvanceNetworkTypeListener listener) {
        removeAdvanceNetworkTypeListener(this.mLteAdvanceNetworkTypeListeners, listener);
        if (this.mMtkRil != null) {
            log("unregisterForNwCfgInfoChanged...");
            this.mMtkRil.unregisterForNwCfgInfoChanged(this.mHandler);
        }
    }

    private void notifyLteAdvanceNetworkTypeListeners(int lteAdvancedType) {
        log("notifyLteAdvanceNetworkTypeListeners");
        notifyAdvanceNetworkTypeListeners(this.mLteAdvanceNetworkTypeListeners, lteAdvancedType);
    }

    @Override // com.hyper.internal.telephony.ext.IHyperNetworkTypeManagerExt
    public void registerForNrAdvanceTypeChange(IHyperNetworkTypeManagerExt.AdvanceNetworkTypeListener listener) {
        addAdvanceNetworkTypeListener(this.mNrAdvanceNetworkTypeListeners, listener);
        if (this.mPhone != null) {
            log("registerForPhysicalChannelConfig...");
            this.mPhone.registerForPhysicalChannelConfig(this.mHandler, 2, (Object) null);
        }
    }

    @Override // com.hyper.internal.telephony.ext.IHyperNetworkTypeManagerExt
    public void unregisterForNrAdvanceTypeChange(IHyperNetworkTypeManagerExt.AdvanceNetworkTypeListener listener) {
        removeAdvanceNetworkTypeListener(this.mNrAdvanceNetworkTypeListeners, listener);
        if (this.mPhone != null) {
            log("unregisterForPhysicalChannelConfig...");
            this.mPhone.unregisterForPhysicalChannelConfig(this.mHandler);
        }
    }

    private void notifyNrAdvanceNetworkTypeListeners(int nrAdvancedType) {
        log("notifyNrAdvanceNetworkTypeListeners");
        notifyAdvanceNetworkTypeListeners(this.mNrAdvanceNetworkTypeListeners, nrAdvancedType);
    }

    private void addAdvanceNetworkTypeListener(List<IHyperNetworkTypeManagerExt.AdvanceNetworkTypeListener> listeners, IHyperNetworkTypeManagerExt.AdvanceNetworkTypeListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    private void removeAdvanceNetworkTypeListener(List<IHyperNetworkTypeManagerExt.AdvanceNetworkTypeListener> listeners, IHyperNetworkTypeManagerExt.AdvanceNetworkTypeListener listener) {
        listeners.remove(listener);
    }

    private void clearAdvanceNetworkTypeListeners(List<IHyperNetworkTypeManagerExt.AdvanceNetworkTypeListener> listeners) {
        if (listeners != null) {
            listeners.clear();
        }
    }

    private void notifyAdvanceNetworkTypeListeners(List<IHyperNetworkTypeManagerExt.AdvanceNetworkTypeListener> listeners, int advancedType) {
        for (IHyperNetworkTypeManagerExt.AdvanceNetworkTypeListener listener : new ArrayList(listeners)) {
            listener.onAdvanceNetworkTypeChanged(advancedType);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String msg) {
        Rlog.d(this.mTag, msg);
    }

    private void logw(String msg) {
        Rlog.w(this.mTag, msg);
    }

    private void loge(String msg) {
        Rlog.e(this.mTag, msg);
    }
}
