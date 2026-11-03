package com.android.internal.telephony;

import android.content.Context;
import android.content.Intent;
import com.android.internal.telephony.common.HyperModuleList$HyperIndex;
import com.android.internal.telephony.common.IHyperCommonModule;

/* JADX INFO: loaded from: classes.dex */
public interface IHyperTeleSmsManager extends IHyperCommonModule {
    public static final IHyperTeleSmsManager DEFAULT = new IHyperTeleSmsManager() { // from class: com.android.internal.telephony.IHyperTeleSmsManager.1
    };
    public static final String TAG = "IHyperTeleSmsManager";

    default int carrierTureError(Context context, int i, Phone phone) {
        return i;
    }

    default int dispatchCarrierTrue(Context context, Phone phone, SmsMessageBase smsMessageBase) {
        return -1;
    }

    default int getMaxSendRetries(Context context, int i) {
        return 3;
    }

    default boolean isFdnBlockDisabled(Context context, int i) {
        return false;
    }

    default boolean isVoLTERegisteredForPhone(int i) {
        return false;
    }

    default void sendOmacpBroadcast(Context context, Intent intent) {
    }

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default HyperModuleList$HyperIndex index() {
        return HyperModuleList$HyperIndex.IHyperTeleSmsManager;
    }

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default IHyperCommonModule getDefault() {
        return DEFAULT;
    }
}
