package com.android.internal.telephony.data;

import java.util.Set;
import java.util.function.IntPredicate;

/* JADX INFO: compiled from: R8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class DataNetworkController$$ExternalSyntheticLambda20 implements IntPredicate {
    public final /* synthetic */ Set f$0;

    @Override // java.util.function.IntPredicate
    public final boolean test(int i) {
        return this.f$0.contains(Integer.valueOf(i));
    }
}
