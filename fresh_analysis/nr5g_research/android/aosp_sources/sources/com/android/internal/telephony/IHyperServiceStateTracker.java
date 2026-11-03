package com.android.internal.telephony;

import com.android.internal.telephony.common.HyperModuleList$HyperIndex;
import com.android.internal.telephony.common.IHyperCommonModule;

/* JADX INFO: loaded from: classes.dex */
public interface IHyperServiceStateTracker extends IHyperCommonModule {
    public static final IHyperServiceStateTracker DEFAULT = new IHyperServiceStateTracker() { // from class: com.android.internal.telephony.IHyperServiceStateTracker.1
    };
    public static final String TAG = "IHyperPhone";

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default HyperModuleList$HyperIndex index() {
        return HyperModuleList$HyperIndex.IHyperPhone;
    }

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default IHyperCommonModule getDefault() {
        return DEFAULT;
    }
}
