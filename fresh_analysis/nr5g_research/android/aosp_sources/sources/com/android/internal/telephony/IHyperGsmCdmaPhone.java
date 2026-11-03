package com.android.internal.telephony;

import com.android.internal.telephony.common.HyperModuleList$HyperIndex;
import com.android.internal.telephony.common.IHyperCommonModule;

/* JADX INFO: loaded from: classes.dex */
public interface IHyperGsmCdmaPhone extends IHyperCommonModule {
    public static final IHyperGsmCdmaPhone DEFAULT = new IHyperGsmCdmaPhone() { // from class: com.android.internal.telephony.IHyperGsmCdmaPhone.1
    };

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default HyperModuleList$HyperIndex index() {
        return HyperModuleList$HyperIndex.IHyperGsmCdmaPhone;
    }

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default IHyperGsmCdmaPhone getDefault() {
        return DEFAULT;
    }
}
