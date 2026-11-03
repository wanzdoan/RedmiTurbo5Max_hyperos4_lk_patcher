package com.android.phone.freqopt.strategy;

/* JADX INFO: compiled from: R8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class NrLteStrategy$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ NrLteStrategy f$0;

    public /* synthetic */ NrLteStrategy$$ExternalSyntheticLambda0(NrLteStrategy nrLteStrategy) {
        this.f$0 = nrLteStrategy;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.registerRapidFadingIndication();
    }
}
