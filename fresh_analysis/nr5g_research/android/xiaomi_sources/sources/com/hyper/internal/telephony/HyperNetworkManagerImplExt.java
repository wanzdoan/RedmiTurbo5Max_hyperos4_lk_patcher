package com.hyper.internal.telephony;

import com.hyper.internal.telephony.ext.IHyperNetworkManagerExt;

/* JADX INFO: loaded from: classes.dex */
public class HyperNetworkManagerImplExt implements IHyperNetworkManagerExt {
    private static HyperNetworkManagerImplExt sInstance;

    public static HyperNetworkManagerImplExt getInstance() {
        HyperNetworkManagerImplExt hyperNetworkManagerImplExt;
        synchronized (HyperNetworkManagerImplExt.class) {
            if (sInstance == null) {
                sInstance = new HyperNetworkManagerImplExt();
            }
            hyperNetworkManagerImplExt = sInstance;
        }
        return hyperNetworkManagerImplExt;
    }
}
