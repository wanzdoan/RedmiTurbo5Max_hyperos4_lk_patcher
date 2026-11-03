package com.android.internal.telephony.data;

import android.telephony.data.DataProfile;
import java.util.function.ToLongFunction;

/* JADX INFO: compiled from: R8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class DataProfileManager$$ExternalSyntheticLambda20 implements ToLongFunction {
    @Override // java.util.function.ToLongFunction
    public final long applyAsLong(Object obj) {
        return ((DataProfile) obj).getLastSetupTimestamp();
    }
}
