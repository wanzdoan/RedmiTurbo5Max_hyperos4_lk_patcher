package com.android.phone;

/* JADX INFO: loaded from: classes.dex */
public abstract class NetworkOptimizerBase {
    protected static volatile NetworkOptimizerBase sInstance;

    public abstract boolean isCloudGlobalStreamMediaOn();

    public abstract void reportGlobalSafDetailStatistic();

    public abstract void reportGlobalSafStatistic();

    public abstract void reportGlobalVideoBenefitStatistic();
}
