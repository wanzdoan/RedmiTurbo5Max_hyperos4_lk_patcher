package com.android.internal.telephony;

import com.android.internal.telephony.common.HyperModuleList$HyperIndex;
import com.android.internal.telephony.common.IHyperCommonModule;

/* JADX INFO: loaded from: classes.dex */
public interface IHyperImsPhone extends IHyperCommonModule {
    public static final IHyperImsPhone DEFAULT = new IHyperImsPhone() { // from class: com.android.internal.telephony.IHyperImsPhone.1
    };
    public static final String TAG = "IHyperImsPhone";

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default HyperModuleList$HyperIndex index() {
        return HyperModuleList$HyperIndex.IHyperImsPhone;
    }

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default IHyperCommonModule getDefault() {
        return DEFAULT;
    }
}
