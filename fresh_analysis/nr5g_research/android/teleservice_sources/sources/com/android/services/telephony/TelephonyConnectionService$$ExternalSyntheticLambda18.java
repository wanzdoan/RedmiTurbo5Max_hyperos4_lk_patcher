package com.android.services.telephony;

import com.android.internal.telephony.Phone;
import java.util.function.Consumer;

/* JADX INFO: compiled from: R8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class TelephonyConnectionService$$ExternalSyntheticLambda18 implements Consumer {
    public final /* synthetic */ TelephonyConnection f$0;

    public /* synthetic */ TelephonyConnectionService$$ExternalSyntheticLambda18(TelephonyConnection telephonyConnection) {
        this.f$0 = telephonyConnection;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        this.f$0.registerForCallEvents((Phone) obj);
    }
}
