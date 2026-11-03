package com.hyper.internal.telephony.ext;

import android.telephony.PhysicalChannelConfig;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.common.IHyperCommonModule;
import java.util.List;

/* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
 */
/* JADX INFO: loaded from: classes.dex */
public interface IHyperNetworkTypeManagerExt extends IHyperCommonModule {
    public static final int LTE_A_4_5_G = 1;
    public static final int LTE_A_LTE = 0;
    public static final int LTE_CA_PLUS = 2;
    public static final int NR_A_5GA = 1;
    public static final int NR_A_NR = 0;
    public static final IHyperNetworkTypeManagerExt DEFAULT = new IHyperNetworkTypeManagerExt() { // from class: com.hyper.internal.telephony.ext.IHyperNetworkTypeManagerExt.1
    };
    public static final String TAG = IHyperNetworkTypeManagerExt.class.getSimpleName();
    public static final int PHONE_COUNT = TelephonyManager.getDefault().getActiveModemCount();

    /* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
 */
    public interface AdvanceNetworkTypeListener {
        void onAdvanceNetworkTypeChanged(int i);
    }

    default IHyperNetworkTypeManagerExt getDefault() {
        return DEFAULT;
    }

    default void registerForLteAdvanceTypeChange(AdvanceNetworkTypeListener listener) {
    }

    default void unregisterForLteAdvanceTypeChange(AdvanceNetworkTypeListener listener) {
    }

    default void registerForNrAdvanceTypeChange(AdvanceNetworkTypeListener listener) {
    }

    default void unregisterForNrAdvanceTypeChange(AdvanceNetworkTypeListener listener) {
    }

    default int getCurrentOverrideNetworkType(boolean lte, boolean nrNsa, boolean nrSa, int overrideType) {
        return overrideType;
    }

    default boolean isNrAdvanced(List<PhysicalChannelConfig> physicalChannelConfigs, boolean isPhysicalLinkActive) {
        return false;
    }

    default boolean isNrAdvanceNetworkTypeMatch(int dataNetworkType, List<PhysicalChannelConfig> physicalChannelConfigs) {
        return true;
    }
}
