package com.android.internal.telephony.subscription;

import java.util.function.Predicate;

/* JADX INFO: compiled from: R8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SubscriptionManagerService$$ExternalSyntheticLambda11 implements Predicate {
    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        return ((SubscriptionInfoInternal) obj).isEmbedded();
    }
}
