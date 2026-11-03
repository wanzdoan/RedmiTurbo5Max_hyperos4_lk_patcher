package com.hyper.internal.telephony.ext;

import com.android.internal.telephony.common.IHyperCommonModule;

/* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
 */
/* JADX INFO: loaded from: classes.dex */
public interface IHyperTeleImsManagerExt extends IHyperCommonModule {
    public static final IHyperTeleImsManagerExt DEFAULT = new IHyperTeleImsManagerExt() { // from class: com.hyper.internal.telephony.ext.IHyperTeleImsManagerExt.1
    };
    public static final String TAG = "IHyperTeleImsManagerExt";

    default IHyperCommonModule getDefault() {
        return DEFAULT;
    }

    default boolean isVowifiConnected(int phoneId) {
        return false;
    }
}
