package com.hyper.internal.telephony;

import com.hyper.internal.telephony.ext.IHyperDataManagerExt;

/* JADX INFO: loaded from: classes.dex */
public class HyperDataManagerImplExt implements IHyperDataManagerExt {
    private static HyperDataManagerImplExt sInstance;

    public static HyperDataManagerImplExt getInstance() {
        HyperDataManagerImplExt hyperDataManagerImplExt;
        synchronized (HyperDataManagerImplExt.class) {
            if (sInstance == null) {
                sInstance = new HyperDataManagerImplExt();
            }
            hyperDataManagerImplExt = sInstance;
        }
        return hyperDataManagerImplExt;
    }
}
