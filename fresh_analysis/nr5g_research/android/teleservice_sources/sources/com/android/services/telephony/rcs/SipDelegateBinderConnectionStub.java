package com.android.services.telephony.rcs;

import android.telephony.ims.DelegateRegistrationState;
import android.telephony.ims.aidl.ISipDelegateMessageCallback;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public class SipDelegateBinderConnectionStub implements DelegateBinderStateManager {
    protected final Set mDeniedTags;
    protected final Executor mExecutor;
    protected final List mStateCallbacks;

    @Override // com.android.services.telephony.rcs.DelegateBinderStateManager
    public void triggerFullNetworkRegistration(int i, String str) {
    }

    public SipDelegateBinderConnectionStub(Set set, Executor executor, List list) {
        this.mDeniedTags = set;
        this.mExecutor = executor;
        this.mStateCallbacks = list;
    }

    @Override // com.android.services.telephony.rcs.DelegateBinderStateManager
    public boolean create(ISipDelegateMessageCallback iSipDelegateMessageCallback, final BiConsumer biConsumer) {
        this.mExecutor.execute(new Runnable() { // from class: com.android.services.telephony.rcs.SipDelegateBinderConnectionStub$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$create$0(biConsumer);
            }
        });
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$create$0(BiConsumer biConsumer) {
        biConsumer.accept(null, this.mDeniedTags);
        Iterator it = this.mStateCallbacks.iterator();
        while (it.hasNext()) {
            ((DelegateBinderStateManager.StateCallback) it.next()).onRegistrationStateChanged(new DelegateRegistrationState.Builder().build());
        }
    }

    @Override // com.android.services.telephony.rcs.DelegateBinderStateManager
    public void destroy(final int i, final Consumer consumer) {
        this.mExecutor.execute(new Runnable() { // from class: com.android.services.telephony.rcs.SipDelegateBinderConnectionStub$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$destroy$1(consumer, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$destroy$1(Consumer consumer, int i) {
        this.mStateCallbacks.clear();
        consumer.accept(Integer.valueOf(i));
    }
}
