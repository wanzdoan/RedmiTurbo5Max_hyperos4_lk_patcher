package com.android.services.telephony.rcs;

import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.ims.DelegateRegistrationState;
import android.telephony.ims.DelegateRequest;
import android.telephony.ims.SipDelegateConfiguration;
import android.telephony.ims.SipDelegateImsConfiguration;
import android.telephony.ims.aidl.IImsRegistration;
import android.telephony.ims.aidl.ISipDelegate;
import android.telephony.ims.aidl.ISipDelegateMessageCallback;
import android.telephony.ims.aidl.ISipDelegateStateCallback;
import android.telephony.ims.aidl.ISipTransport;
import android.util.Log;
import com.android.internal.telephony.LocalLog;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public class SipDelegateBinderConnection implements DelegateBinderStateManager, IBinder.DeathRecipient {
    private ISipDelegate mDelegateBinder;
    protected final Set mDeniedTags;
    protected final Executor mExecutor;
    private final IImsRegistration mImsRegistration;
    private BiConsumer mPendingCreatedConsumer;
    private Consumer mPendingDestroyedConsumer;
    private final DelegateRequest mRequestedConfig;
    private final ISipTransport mSipTransport;
    protected final List mStateCallbacks;
    protected final int mSubId;
    private final LocalLog mLocalLog = new LocalLog(50);
    private final ISipDelegateStateCallback mSipDelegateStateCallback = new AnonymousClass1();

    /* JADX INFO: renamed from: com.android.services.telephony.rcs.SipDelegateBinderConnection$1, reason: invalid class name */
    class AnonymousClass1 extends ISipDelegateStateCallback.Stub {
        AnonymousClass1() {
        }

        public void onCreated(final ISipDelegate iSipDelegate, final List list) {
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                SipDelegateBinderConnection.this.mExecutor.execute(new Runnable() { // from class: com.android.services.telephony.rcs.SipDelegateBinderConnection$1$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onCreated$0(iSipDelegate, list);
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreated$0(ISipDelegate iSipDelegate, List list) {
            SipDelegateBinderConnection.this.notifySipDelegateCreated(iSipDelegate, list);
        }

        public void onFeatureTagRegistrationChanged(final DelegateRegistrationState delegateRegistrationState) {
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                SipDelegateBinderConnection.this.mExecutor.execute(new Runnable() { // from class: com.android.services.telephony.rcs.SipDelegateBinderConnection$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onFeatureTagRegistrationChanged$1(delegateRegistrationState);
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onFeatureTagRegistrationChanged$1(DelegateRegistrationState delegateRegistrationState) {
            SipDelegateBinderConnection.this.logi("onFeatureTagRegistrationChanged:" + delegateRegistrationState);
            Iterator it = SipDelegateBinderConnection.this.mStateCallbacks.iterator();
            while (it.hasNext()) {
                ((DelegateBinderStateManager.StateCallback) it.next()).onRegistrationStateChanged(delegateRegistrationState);
            }
        }

        public void onImsConfigurationChanged(final SipDelegateImsConfiguration sipDelegateImsConfiguration) {
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                SipDelegateBinderConnection.this.mExecutor.execute(new Runnable() { // from class: com.android.services.telephony.rcs.SipDelegateBinderConnection$1$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onImsConfigurationChanged$2(sipDelegateImsConfiguration);
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onImsConfigurationChanged$2(SipDelegateImsConfiguration sipDelegateImsConfiguration) {
            SipDelegateBinderConnection.this.logi("onImsConfigurationChanged: version=" + sipDelegateImsConfiguration.getVersion());
            Iterator it = SipDelegateBinderConnection.this.mStateCallbacks.iterator();
            while (it.hasNext()) {
                ((DelegateBinderStateManager.StateCallback) it.next()).onImsConfigurationChanged(sipDelegateImsConfiguration);
            }
        }

        public void onConfigurationChanged(final SipDelegateConfiguration sipDelegateConfiguration) {
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                SipDelegateBinderConnection.this.mExecutor.execute(new Runnable() { // from class: com.android.services.telephony.rcs.SipDelegateBinderConnection$1$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onConfigurationChanged$3(sipDelegateConfiguration);
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onConfigurationChanged$3(SipDelegateConfiguration sipDelegateConfiguration) {
            SipDelegateBinderConnection.this.logi("onConfigurationChanged");
            Iterator it = SipDelegateBinderConnection.this.mStateCallbacks.iterator();
            while (it.hasNext()) {
                ((DelegateBinderStateManager.StateCallback) it.next()).onConfigurationChanged(sipDelegateConfiguration);
            }
        }

        public void onDestroyed(final int i) {
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                SipDelegateBinderConnection.this.mExecutor.execute(new Runnable() { // from class: com.android.services.telephony.rcs.SipDelegateBinderConnection$1$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onDestroyed$4(i);
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDestroyed$4(int i) {
            SipDelegateBinderConnection.this.lambda$destroy$1(i);
        }
    }

    public SipDelegateBinderConnection(int i, ISipTransport iSipTransport, IImsRegistration iImsRegistration, DelegateRequest delegateRequest, Set set, Executor executor, List list) {
        this.mSubId = i;
        this.mSipTransport = iSipTransport;
        this.mImsRegistration = iImsRegistration;
        this.mRequestedConfig = delegateRequest;
        this.mDeniedTags = set;
        this.mExecutor = executor;
        this.mStateCallbacks = list;
    }

    @Override // com.android.services.telephony.rcs.DelegateBinderStateManager
    public boolean create(ISipDelegateMessageCallback iSipDelegateMessageCallback, BiConsumer biConsumer) {
        try {
            this.mSipTransport.createSipDelegate(this.mSubId, this.mRequestedConfig, this.mSipDelegateStateCallback, iSipDelegateMessageCallback);
            this.mSipTransport.asBinder().linkToDeath(this, 0);
            this.mPendingCreatedConsumer = biConsumer;
            return true;
        } catch (RemoteException e) {
            logw("create called on unreachable SipTransport:" + e);
            return false;
        }
    }

    @Override // com.android.services.telephony.rcs.DelegateBinderStateManager
    public void destroy(final int i, Consumer consumer) {
        this.mPendingDestroyedConsumer = consumer;
        try {
            ISipDelegate iSipDelegate = this.mDelegateBinder;
            if (iSipDelegate != null) {
                this.mSipTransport.destroySipDelegate(iSipDelegate, i);
            } else {
                this.mExecutor.execute(new Runnable() { // from class: com.android.services.telephony.rcs.SipDelegateBinderConnection$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$destroy$0(i);
                    }
                });
            }
            this.mStateCallbacks.clear();
        } catch (RemoteException e) {
            logw("destroy called on unreachable SipTransport:" + e);
            this.mExecutor.execute(new Runnable() { // from class: com.android.services.telephony.rcs.SipDelegateBinderConnection$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$destroy$1(i);
                }
            });
        }
        try {
            this.mSipTransport.asBinder().unlinkToDeath(this, 0);
        } catch (NoSuchElementException e2) {
            logw("unlinkToDeath called on already unlinked binder" + e2);
        }
    }

    @Override // com.android.services.telephony.rcs.DelegateBinderStateManager
    public void triggerFullNetworkRegistration(int i, String str) {
        try {
            this.mImsRegistration.triggerFullNetworkRegistration(i, str);
        } catch (RemoteException e) {
            logw("triggerFullNetworkRegistration called on unreachable ImsRegistration:" + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifySipDelegateCreated(ISipDelegate iSipDelegate, List list) {
        logi("Delegate Created: " + iSipDelegate + ", deniedTags:" + list);
        if (iSipDelegate == null) {
            logw("Invalid null delegate returned!");
        }
        this.mDelegateBinder = iSipDelegate;
        if (list != null) {
            this.mDeniedTags.addAll(list);
        }
        BiConsumer biConsumer = this.mPendingCreatedConsumer;
        if (biConsumer == null) {
            return;
        }
        biConsumer.accept(iSipDelegate, this.mDeniedTags);
        this.mPendingCreatedConsumer = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: notifySipDelegateDestroyed, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void lambda$destroy$1(int i) {
        logi("Delegate Destroyed, reason: " + i);
        this.mDelegateBinder = null;
        Consumer consumer = this.mPendingDestroyedConsumer;
        if (consumer == null) {
            return;
        }
        consumer.accept(Integer.valueOf(i));
        this.mPendingDestroyedConsumer = null;
    }

    protected final void logi(String str) {
        Log.i("SipTransportC", "BinderConn[" + this.mSubId + "] " + str);
        LocalLog localLog = this.mLocalLog;
        StringBuilder sb = new StringBuilder("[I] ");
        sb.append(str);
        localLog.log(sb.toString());
    }

    protected final void logw(String str) {
        Log.w("SipTransportC", "BinderConn[" + this.mSubId + "] " + str);
        LocalLog localLog = this.mLocalLog;
        StringBuilder sb = new StringBuilder("[W] ");
        sb.append(str);
        localLog.log(sb.toString());
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        this.mExecutor.execute(new Runnable() { // from class: com.android.services.telephony.rcs.SipDelegateBinderConnection$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$binderDied$2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$binderDied$2() {
        logw("binderDied!");
        notifySipDelegateCreated(null, Collections.EMPTY_LIST);
        lambda$destroy$1(1);
    }
}
