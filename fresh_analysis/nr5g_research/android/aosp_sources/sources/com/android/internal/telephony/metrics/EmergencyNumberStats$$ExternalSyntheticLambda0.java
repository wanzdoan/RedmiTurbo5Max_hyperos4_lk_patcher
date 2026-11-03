package com.android.internal.telephony.metrics;

import android.util.SparseIntArray;
import java.util.function.Function;

/* JADX INFO: compiled from: R8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class EmergencyNumberStats$$ExternalSyntheticLambda0 implements Function {
    public final /* synthetic */ SparseIntArray f$0;

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return Integer.valueOf(this.f$0.get(((Integer) obj).intValue()));
    }
}
