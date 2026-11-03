package com.android.internal.telephony;

import com.android.internal.telephony.common.HyperModuleList$HyperIndex;
import com.android.internal.telephony.common.IHyperCommonModule;

/* JADX INFO: loaded from: classes.dex */
public interface IHyperGsmCdmaCallTracker extends IHyperCommonModule {
    public static final IHyperGsmCdmaCallTracker DEFAULT = new IHyperGsmCdmaCallTracker() { // from class: com.android.internal.telephony.IHyperGsmCdmaCallTracker.1
    };

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default HyperModuleList$HyperIndex index() {
        return HyperModuleList$HyperIndex.IHyperGsmCdmaCallTracker;
    }

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default IHyperGsmCdmaCallTracker getDefault() {
        return DEFAULT;
    }
}
