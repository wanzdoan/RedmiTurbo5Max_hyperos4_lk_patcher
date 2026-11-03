package com.android.internal.telephony.data;

import java.util.function.ToIntFunction;

/* JADX INFO: compiled from: R8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class DataNetwork$$ExternalSyntheticLambda1 implements ToIntFunction {
    public final /* synthetic */ DataConfigManager f$0;

    public /* synthetic */ DataNetwork$$ExternalSyntheticLambda1(DataConfigManager dataConfigManager) {
        this.f$0 = dataConfigManager;
    }

    @Override // java.util.function.ToIntFunction
    public final int applyAsInt(Object obj) {
        return this.f$0.getNetworkCapabilityPriority(((Integer) obj).intValue());
    }
}
