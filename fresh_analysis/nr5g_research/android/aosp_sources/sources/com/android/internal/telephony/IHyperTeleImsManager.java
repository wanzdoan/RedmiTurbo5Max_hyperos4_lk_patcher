package com.android.internal.telephony;

import com.android.internal.telephony.common.HyperModuleList$HyperIndex;
import com.android.internal.telephony.common.IHyperCommonModule;

/* JADX INFO: loaded from: classes.dex */
public interface IHyperTeleImsManager extends IHyperCommonModule {
    public static final IHyperTeleImsManager DEFAULT = new IHyperTeleImsManager() { // from class: com.android.internal.telephony.IHyperTeleImsManager.1
    };
    public static final String TAG = "IHyperTeleImsManager";

    default boolean isVowifiConnected(int i) {
        return false;
    }

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default HyperModuleList$HyperIndex index() {
        return HyperModuleList$HyperIndex.IHyperTeleImsManager;
    }

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default IHyperCommonModule getDefault() {
        return DEFAULT;
    }
}
