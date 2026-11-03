package com.android.internal.telephony.satellite;

import android.telephony.SubscriptionInfo;
import java.util.function.ToIntFunction;

/* JADX INFO: compiled from: R8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SatelliteController$$ExternalSyntheticLambda1 implements ToIntFunction {
    @Override // java.util.function.ToIntFunction
    public final int applyAsInt(Object obj) {
        return ((SubscriptionInfo) obj).getSubscriptionId();
    }
}
