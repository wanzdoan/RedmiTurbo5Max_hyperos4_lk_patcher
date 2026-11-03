package com.hyper.internal.telephony.ext;

import android.content.Context;
import android.os.Message;
import android.telephony.ims.ImsReasonInfo;
import com.android.ims.ImsCall;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.common.IHyperCommonModule;
import com.android.internal.telephony.emergency.RadioOnStateListener;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneConnection;

/* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
 */
/* JADX INFO: loaded from: classes.dex */
public interface IHyperCallManagerExt extends IHyperCommonModule {
    public static final IHyperCallManagerExt DEFAULT = new IHyperCallManagerExt() { // from class: com.hyper.internal.telephony.ext.IHyperCallManagerExt.1
    };
    public static final String TAG = "IHyperNetworkManager";

    default IHyperCommonModule getDefault() {
        return DEFAULT;
    }

    default void setVoNrState(int phoneId, Context context, boolean enabled, Message onComplete) {
    }

    default void getVoNrState(int phoneId, Context context, Message onComplete) {
    }

    default boolean isInCsCall(boolean isEmergency, int phoneId) {
        return false;
    }

    default boolean anyPhoneIsOkToCall(Phone phone, RadioOnStateListener.Callback callback, int serviceState, boolean imsVoiceCapable) {
        if (callback == null) {
            return false;
        }
        return callback.isOkToCall(phone, serviceState, imsVoiceCapable);
    }

    default boolean onImsCallStartFailed(ImsCall imsCall, ImsReasonInfo reasonInfo, ImsPhone imsPhone) {
        return false;
    }

    default int getEccClirMode(ImsPhone imsPhone, int clirMode, String origNumber) {
        return clirMode;
    }

    default void onImsCallUpdated(ImsCall imsCall, ImsPhoneConnection conn, boolean isLocalTone, ImsPhone imsPhone) {
    }
}
