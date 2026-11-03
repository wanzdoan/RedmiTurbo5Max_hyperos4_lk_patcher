package com.hyper.internal.telephony.ext;

import android.content.Context;
import android.content.Intent;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.common.IHyperCommonModule;

/* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
 */
/* JADX INFO: loaded from: classes.dex */
public interface IHyperTeleSmsManagerExt extends IHyperCommonModule {
    public static final IHyperTeleSmsManagerExt DEFAULT = new IHyperTeleSmsManagerExt() { // from class: com.hyper.internal.telephony.ext.IHyperTeleSmsManagerExt.1
    };
    public static final String TAG = "IHyperTeleSmsManagerExt";

    default IHyperCommonModule getDefault() {
        return DEFAULT;
    }

    default boolean isFdnBlockDisabled(Context context, int subId) {
        return false;
    }

    default int getMaxSendRetries(Context context, int subId) {
        return 3;
    }

    default int dispatchCarrierTrue(Context context, Phone phone, SmsMessageBase smsb) {
        return -1;
    }

    default boolean isVoLTERegisteredForPhone(int phoneId) {
        return false;
    }

    default int carrierTureError(Context context, int error, Phone phone) {
        return error;
    }

    default void sendOmacpBroadcast(Context context, Intent intent) {
    }
}
