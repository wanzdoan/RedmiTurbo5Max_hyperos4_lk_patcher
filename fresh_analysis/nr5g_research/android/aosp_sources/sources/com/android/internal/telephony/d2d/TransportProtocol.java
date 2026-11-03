package com.android.internal.telephony.d2d;

import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public interface TransportProtocol {

    public interface Callback {
        void onMessagesReceived(Set<Communicator.Message> set);

        void onNegotiationFailed(TransportProtocol transportProtocol);

        void onNegotiationSuccess(TransportProtocol transportProtocol);
    }

    void forceNegotiated();

    void forceNotNegotiated();

    void sendMessages(Set<Communicator.Message> set);

    void setCallback(Callback callback);

    void startNegotiation();
}
