package com.android.internal.telephony.data;

import com.android.internal.telephony.AndroidUtilIndentingPrintWriter;
import java.util.function.Consumer;

/* JADX INFO: compiled from: R8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class DataConfigManager$$ExternalSyntheticLambda4 implements Consumer {
    public final /* synthetic */ AndroidUtilIndentingPrintWriter f$0;

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        this.f$0.println((DataRetryManager.DataSetupRetryRule) obj);
    }
}
