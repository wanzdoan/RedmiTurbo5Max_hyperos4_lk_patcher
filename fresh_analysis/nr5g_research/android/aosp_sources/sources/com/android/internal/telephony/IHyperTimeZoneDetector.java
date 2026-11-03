package com.android.internal.telephony;

import com.android.internal.telephony.common.HyperModuleList$HyperIndex;
import com.android.internal.telephony.common.IHyperCommonModule;

/* JADX INFO: loaded from: classes.dex */
public interface IHyperTimeZoneDetector extends IHyperCommonModule {
    public static final IHyperTimeZoneDetector DEFAULT = new IHyperTimeZoneDetector() { // from class: com.android.internal.telephony.IHyperTimeZoneDetector.1
    };
    public static final String TAG = "IHyperTimeZoneDetector";

    default void onNetworkCountryCodeChanged(String str) {
    }

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default HyperModuleList$HyperIndex index() {
        return HyperModuleList$HyperIndex.IHyperTimeZoneDetector;
    }

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default IHyperTimeZoneDetector getDefault() {
        return DEFAULT;
    }
}
