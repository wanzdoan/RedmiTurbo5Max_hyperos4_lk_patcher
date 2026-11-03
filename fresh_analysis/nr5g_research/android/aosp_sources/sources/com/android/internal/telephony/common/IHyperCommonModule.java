package com.android.internal.telephony.common;

/* JADX INFO: loaded from: classes.dex */
public interface IHyperCommonModule {
    default IHyperCommonModule getDefault() {
        return null;
    }

    default HyperModuleList$HyperIndex index() {
        return HyperModuleList$HyperIndex.End;
    }
}
