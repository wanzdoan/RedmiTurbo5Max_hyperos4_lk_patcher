package com.android.internal.telephony.data;

import java.util.function.Function;

/* JADX INFO: compiled from: R8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class DataConfigManager$$ExternalSyntheticLambda3 implements Function {
    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return DataUtils.networkCapabilityToString(((Integer) obj).intValue());
    }
}
