package com.android.internal.telephony;

import android.telephony.PhysicalChannelConfig;
import com.android.internal.telephony.common.HyperModuleList$HyperIndex;
import com.android.internal.telephony.common.IHyperCommonModule;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface IHyperNetworkTypeManager extends IHyperCommonModule {
    public static final IHyperNetworkTypeManager DEFAULT = new IHyperNetworkTypeManager() { // from class: com.android.internal.telephony.IHyperNetworkTypeManager.1
    };
    public static final String TAG = "IHyperNetworkTypeManager";

    default int getCurrentOverrideNetworkType(boolean z, boolean z2, boolean z3, int i) {
        return i;
    }

    default boolean isNrAdvanceNetworkTypeMatch(int i, List<PhysicalChannelConfig> list) {
        return true;
    }

    default boolean isNrAdvanced(List<PhysicalChannelConfig> list, boolean z) {
        return false;
    }

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default HyperModuleList$HyperIndex index() {
        return HyperModuleList$HyperIndex.IHyperNetworkTypeManager;
    }

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default IHyperNetworkTypeManager getDefault() {
        return DEFAULT;
    }
}
