package com.android.internal.telephony.metrics;

import android.telephony.ServiceState;
import java.util.function.Function;

/* JADX INFO: compiled from: R8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SmsStats$$ExternalSyntheticLambda1 implements Function {
    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return ((ServiceState) obj).getOperatorNumeric();
    }
}
