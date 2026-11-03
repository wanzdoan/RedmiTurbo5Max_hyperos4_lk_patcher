package com.hyper.internal.telephony.ext;

import com.android.internal.telephony.common.IHyperCommonModule;

/* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
 */
/* JADX INFO: loaded from: classes.dex */
public interface IHyperNetworkManagerExt extends IHyperCommonModule {
    public static final IHyperNetworkManagerExt DEFAULT = new IHyperNetworkManagerExt() { // from class: com.hyper.internal.telephony.ext.IHyperNetworkManagerExt.1
    };
    public static final String TAG = "IHyperDataManager";

    default IHyperNetworkManagerExt getDefault() {
        return DEFAULT;
    }

    default void setGwsdEnabled(int mPhoneId, boolean enabled) {
    }

    default void setGwsdEnabled(boolean enabled) {
    }

    default void setGwsdAutoRejectEnabled(boolean enabled) {
    }

    default void setGwsdListener() {
    }
}
