package com.android.services.telephony.rcs;

import android.telephony.ims.DelegateRegistrationState;
import android.telephony.ims.DelegateRequest;
import android.telephony.ims.SipDelegateConfiguration;
import android.telephony.ims.SipDelegateImsConfiguration;
import android.telephony.ims.aidl.ISipDelegateMessageCallback;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public interface DelegateBinderStateManager {

    public interface Factory {
        DelegateBinderStateManager create(int i, DelegateRequest delegateRequest, Set set, Executor executor, List list);
    }

    public interface StateCallback {
        void onConfigurationChanged(SipDelegateConfiguration sipDelegateConfiguration);

        void onImsConfigurationChanged(SipDelegateImsConfiguration sipDelegateImsConfiguration);

        void onRegistrationStateChanged(DelegateRegistrationState delegateRegistrationState);
    }

    boolean create(ISipDelegateMessageCallback iSipDelegateMessageCallback, BiConsumer biConsumer);

    void destroy(int i, Consumer consumer);

    void triggerFullNetworkRegistration(int i, String str);
}
