package com.android.internal.telephony;

import android.telephony.ims.ImsReasonInfo;
import com.android.ims.ImsCall;
import com.android.internal.telephony.common.HyperModuleList$HyperIndex;
import com.android.internal.telephony.common.IHyperCommonModule;
import com.android.internal.telephony.emergency.RadioOnStateListener;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneConnection;

/* JADX INFO: loaded from: classes.dex */
public interface IHyperCallManager extends IHyperCommonModule {
    public static final IHyperCallManager DEFAULT = new IHyperCallManager() { // from class: com.android.internal.telephony.IHyperCallManager.1
    };
    public static final String TAG = "IHyperCallManager";

    default int getEccClirMode(ImsPhone imsPhone, int i, String str) {
        return i;
    }

    default boolean isInCsCall(boolean z, int i) {
        return false;
    }

    default boolean onImsCallStartFailed(ImsCall imsCall, ImsReasonInfo imsReasonInfo, ImsPhone imsPhone) {
        return false;
    }

    default void onImsCallUpdated(ImsCall imsCall, ImsPhoneConnection imsPhoneConnection, boolean z, ImsPhone imsPhone) {
    }

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default HyperModuleList$HyperIndex index() {
        return HyperModuleList$HyperIndex.IHyperCallManager;
    }

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default IHyperCommonModule getDefault() {
        return DEFAULT;
    }

    default boolean anyPhoneIsOkToCall(Phone phone, RadioOnStateListener.Callback callback, int i, boolean z) {
        if (callback == null) {
            return false;
        }
        return callback.isOkToCall(phone, i, z);
    }
}
