package com.android.internal.telephony.data;

import android.net.NetworkCapabilities;
import java.util.function.Consumer;

/* JADX INFO: compiled from: R8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class DataNetwork$$ExternalSyntheticLambda13 implements Consumer {
    public final /* synthetic */ NetworkCapabilities.Builder f$0;

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        this.f$0.addCapability(((Integer) obj).intValue());
    }
}
