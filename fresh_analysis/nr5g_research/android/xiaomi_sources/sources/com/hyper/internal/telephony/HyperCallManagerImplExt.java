package com.hyper.internal.telephony;

import com.hyper.internal.telephony.ext.IHyperCallManagerExt;

/* JADX INFO: loaded from: classes.dex */
public class HyperCallManagerImplExt implements IHyperCallManagerExt {
    private static HyperCallManagerImplExt sInstance;

    public static HyperCallManagerImplExt getInstance() {
        HyperCallManagerImplExt hyperCallManagerImplExt;
        synchronized (HyperCallManagerImplExt.class) {
            if (sInstance == null) {
                sInstance = new HyperCallManagerImplExt();
            }
            hyperCallManagerImplExt = sInstance;
        }
        return hyperCallManagerImplExt;
    }
}
