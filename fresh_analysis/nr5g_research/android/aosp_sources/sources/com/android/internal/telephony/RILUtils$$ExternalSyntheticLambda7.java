package com.android.internal.telephony;

import android.hardware.radio.V1_6.SliceInfo;
import java.util.function.Function;

/* JADX INFO: compiled from: R8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class RILUtils$$ExternalSyntheticLambda7 implements Function {
    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return RILUtils.convertHalSliceInfo((SliceInfo) obj);
    }
}
