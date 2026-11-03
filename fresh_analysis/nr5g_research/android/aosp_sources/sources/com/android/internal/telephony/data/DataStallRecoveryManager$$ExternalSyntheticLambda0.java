package com.android.internal.telephony.data;

import java.util.concurrent.Executor;

/* JADX INFO: compiled from: R8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class DataStallRecoveryManager$$ExternalSyntheticLambda0 implements Executor {
    public final /* synthetic */ DataStallRecoveryManager f$0;

    @Override // java.util.concurrent.Executor
    public final void execute(Runnable runnable) {
        this.f$0.post(runnable);
    }
}
