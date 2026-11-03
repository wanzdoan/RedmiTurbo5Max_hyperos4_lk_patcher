package com.android.internal.telephony;

import com.android.internal.telephony.common.HyperModuleList$HyperIndex;
import com.android.internal.telephony.common.IHyperCommonModule;

/* JADX INFO: loaded from: classes.dex */
public interface IHyperPhone extends IHyperCommonModule {
    public static final IHyperPhone DEFAULT = new IHyperPhone() { // from class: com.android.internal.telephony.IHyperPhone.1
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
