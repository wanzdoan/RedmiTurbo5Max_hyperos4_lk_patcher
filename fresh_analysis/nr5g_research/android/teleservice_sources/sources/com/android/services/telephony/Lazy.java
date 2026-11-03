package com.android.services.telephony;

/* JADX INFO: loaded from: classes.dex */
public final class Lazy {
    private volatile Object mValue;

    public void set(Object obj) {
        this.mValue = obj;
    }

    public Object get() {
        return this.mValue;
    }
}
