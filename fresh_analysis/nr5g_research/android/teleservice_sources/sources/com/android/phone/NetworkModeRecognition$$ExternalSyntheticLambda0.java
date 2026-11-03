package com.android.phone;

import android.location.Location;
import android.location.LocationListener;
import java.util.function.Consumer;

/* JADX INFO: compiled from: R8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class NetworkModeRecognition$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ LocationListener f$0;

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        this.f$0.onLocationChanged((Location) obj);
    }
}
