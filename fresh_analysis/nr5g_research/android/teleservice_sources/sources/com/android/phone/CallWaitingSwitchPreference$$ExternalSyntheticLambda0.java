package com.android.phone;

import java.util.function.Consumer;

/* JADX INFO: compiled from: R8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class CallWaitingSwitchPreference$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ CallWaitingSwitchPreference f$0;

    public /* synthetic */ CallWaitingSwitchPreference$$ExternalSyntheticLambda0(CallWaitingSwitchPreference callWaitingSwitchPreference) {
        this.f$0 = callWaitingSwitchPreference;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        this.f$0.queryStatusCallBack(((Integer) obj).intValue());
    }
}
