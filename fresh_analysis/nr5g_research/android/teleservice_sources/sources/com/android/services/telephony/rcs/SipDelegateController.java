package com.android.services.telephony.rcs;

import android.telephony.ims.DelegateRequest;
import android.telephony.ims.aidl.IImsRegistration;
import android.telephony.ims.aidl.ISipDelegate;
import android.telephony.ims.aidl.ISipDelegateConnectionStateCallback;
import android.telephony.ims.aidl.ISipDelegateMessageCallback;
import android.telephony.ims.aidl.ISipTransport;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import com.android.internal.telephony.IndentingPrintWriter;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.metrics.RcsStats;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class SipDelegateController {
    private DelegateBinderStateManager mBinderConnection;
    private final DelegateBinderStateManager.Factory mBinderConnectionFactory;
    private final DelegateStateTracker mDelegateStateTracker;
    private final ScheduledExecutorService mExecutorService;
    private final DelegateRequest mInitialRequest;
    private final LocalLog mLocalLog = new LocalLog(50);
    private final MessageTransportWrapper mMessageTransportWrapper;
    private final String mPackageName;
    private final int mSubId;
    private Set mTrackedFeatureTags;
    private final int mUid;

    private static int getMessageFailReasonFromDestroyReason(int i) {
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2 && i != 3) {
                return 0;
            }
        }
        return i2;
    }

    class BinderConnectionFactory implements DelegateBinderStateManager.Factory {
        private final IImsRegistration mImsRegistrationImpl;
        private final ISipTransport mSipTransportImpl;

        BinderConnectionFactory(ISipTransport iSipTransport, IImsRegistration iImsRegistration) {
            this.mSipTransportImpl = iSipTransport;
            this.mImsRegistrationImpl = iImsRegistration;
        }

        @Override // com.android.services.telephony.rcs.DelegateBinderStateManager.Factory
        public DelegateBinderStateManager create(int i, DelegateRequest delegateRequest, Set set, Executor executor, List list) {
            if (delegateRequest.getFeatureTags().isEmpty()) {
                return new SipDelegateBinderConnectionStub(set, executor, list);
            }
            return new SipDelegateBinderConnection(SipDelegateController.this.mSubId, this.mSipTransportImpl, this.mImsRegistrationImpl, delegateRequest, set, SipDelegateController.this.mExecutorService, list);
        }
    }

    public SipDelegateController(int i, int i2, DelegateRequest delegateRequest, String str, ISipTransport iSipTransport, IImsRegistration iImsRegistration, ScheduledExecutorService scheduledExecutorService, ISipDelegateConnectionStateCallback iSipDelegateConnectionStateCallback, ISipDelegateMessageCallback iSipDelegateMessageCallback) {
        this.mSubId = i;
        this.mUid = i2;
        this.mPackageName = str;
        this.mInitialRequest = delegateRequest;
        this.mExecutorService = scheduledExecutorService;
        this.mBinderConnectionFactory = new BinderConnectionFactory(iSipTransport, iImsRegistration);
        MessageTransportWrapper messageTransportWrapper = new MessageTransportWrapper(i, scheduledExecutorService, iSipDelegateMessageCallback);
        this.mMessageTransportWrapper = messageTransportWrapper;
        this.mDelegateStateTracker = new DelegateStateTracker(i, i2, iSipDelegateConnectionStateCallback, messageTransportWrapper.getDelegateConnection(), RcsStats.getInstance());
    }

    public SipDelegateController(int i, int i2, DelegateRequest delegateRequest, String str, ScheduledExecutorService scheduledExecutorService, MessageTransportWrapper messageTransportWrapper, DelegateStateTracker delegateStateTracker, DelegateBinderStateManager.Factory factory) {
        this.mSubId = i;
        this.mUid = i2;
        this.mInitialRequest = delegateRequest;
        this.mPackageName = str;
        this.mExecutorService = scheduledExecutorService;
        this.mMessageTransportWrapper = messageTransportWrapper;
        this.mDelegateStateTracker = delegateStateTracker;
        this.mBinderConnectionFactory = factory;
    }

    public DelegateRequest getInitialRequest() {
        return this.mInitialRequest;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public ISipDelegate getSipDelegateInterface() {
        return this.mMessageTransportWrapper.getDelegateConnection();
    }

    public ISipDelegateMessageCallback getAppMessageCallback() {
        return this.mMessageTransportWrapper.getAppMessageCallback();
    }

    public CompletableFuture create(final Set set, Set set2) {
        logi("create, supported: " + set + ", denied: " + set2);
        this.mTrackedFeatureTags = set;
        final DelegateBinderStateManager delegateBinderStateManagerCreateBinderConnection = createBinderConnection(set, set2);
        return createSipDelegate(delegateBinderStateManagerCreateBinderConnection).thenApply(new Function() { // from class: com.android.services.telephony.rcs.SipDelegateController$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.lambda$create$0(delegateBinderStateManagerCreateBinderConnection, set, (Pair) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$create$0(DelegateBinderStateManager delegateBinderStateManager, Set set, Pair pair) {
        if (pair == null) {
            logw("create: resultPair returned null");
            return Boolean.FALSE;
        }
        this.mBinderConnection = delegateBinderStateManager;
        logi("create: created, delegate denied: " + pair.second);
        ArraySet arraySet = new ArraySet(set);
        arraySet.removeAll((Collection<?>) ((Set) pair.second).stream().map(new SipDelegateController$$ExternalSyntheticLambda7()).collect(Collectors.toSet()));
        this.mMessageTransportWrapper.openTransport((ISipDelegate) pair.first, arraySet, (Set) pair.second);
        this.mDelegateStateTracker.sipDelegateConnected(arraySet, (Set) pair.second);
        return Boolean.TRUE;
    }

    public CompletableFuture changeSupportedFeatureTags(final Set set, final Set set2) {
        logi("Received feature tag set change, old: [" + this.mTrackedFeatureTags + "], new: " + set + ",denied: [" + set2 + "]");
        Set set3 = this.mTrackedFeatureTags;
        if (set3 != null && set3.equals(set)) {
            logi("changeSupportedFeatureTags: no change, returning");
            return CompletableFuture.completedFuture(Boolean.TRUE);
        }
        this.mTrackedFeatureTags = set;
        return destroySipDelegate(false, 11, 11, 5, 2).thenComposeAsync(new Function() { // from class: com.android.services.telephony.rcs.SipDelegateController$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.lambda$changeSupportedFeatureTags$1(set, set2, (Integer) obj);
            }
        }, (Executor) this.mExecutorService);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ CompletionStage lambda$changeSupportedFeatureTags$1(Set set, Set set2, Integer num) {
        logi("changeSupportedFeatureTags: destroy stage complete, reason reported: " + num);
        return create(set, set2);
    }

    public CompletableFuture destroy(boolean z, int i) {
        logi("destroy, forced " + z + ", destroyReason: " + i);
        return destroySipDelegate(z, 2, getMessageFailReasonFromDestroyReason(i), 6, i).thenApplyAsync(new Function() { // from class: com.android.services.telephony.rcs.SipDelegateController$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.lambda$destroy$2((Integer) obj);
            }
        }, (Executor) this.mExecutorService);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Integer lambda$destroy$2(Integer num) {
        logi("destroy, operation complete, notifying trackers, reason " + num);
        this.mDelegateStateTracker.sipDelegateDestroyed(num.intValue());
        return num;
    }

    public void triggerFullNetworkRegistration(int i, String str) {
        logi("triggerFullNetworkRegistration, code=" + i + ", reason=" + str);
        DelegateBinderStateManager delegateBinderStateManager = this.mBinderConnection;
        if (delegateBinderStateManager != null) {
            delegateBinderStateManager.triggerFullNetworkRegistration(i, str);
        } else {
            logw("triggerFullNetworkRegistration called when binder connection is null");
        }
    }

    private CompletableFuture destroySipDelegate(boolean z, int i, int i2, int i3, final int i4) {
        if (this.mBinderConnection == null) {
            logi("destroySipDelegate, called when binder connection is already null");
            return CompletableFuture.completedFuture(Integer.valueOf(i4));
        }
        final CompletableFuture completableFuture = new CompletableFuture();
        if (z) {
            logi("destroySipDelegate, forced");
            this.mMessageTransportWrapper.close(i2);
            completableFuture.complete(Boolean.TRUE);
        } else {
            this.mMessageTransportWrapper.closeGracefully(i, i2, new Consumer() { // from class: com.android.services.telephony.rcs.SipDelegateController$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    completableFuture.complete((Boolean) obj);
                }
            });
        }
        if (!completableFuture.isDone()) {
            this.mDelegateStateTracker.sipDelegateChanging(i3);
        } else {
            logi("destroySipDelegate, skip DEREGISTERING_REASON_DESTROY_PENDING");
        }
        return completableFuture.thenComposeAsync(new Function() { // from class: com.android.services.telephony.rcs.SipDelegateController$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.lambda$destroySipDelegate$3(i4, (Boolean) obj);
            }
        }, (Executor) this.mExecutorService);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ CompletionStage lambda$destroySipDelegate$3(int i, Boolean bool) {
        logi("destroySipDelegate, transport gracefully closed = " + bool);
        final CompletableFuture completableFuture = new CompletableFuture();
        this.mBinderConnection.destroy(i, new Consumer() { // from class: com.android.services.telephony.rcs.SipDelegateController$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                completableFuture.complete((Integer) obj);
            }
        });
        return completableFuture;
    }

    private CompletableFuture createSipDelegate(DelegateBinderStateManager delegateBinderStateManager) {
        final CompletableFuture completableFuture = new CompletableFuture();
        if (delegateBinderStateManager.create(this.mMessageTransportWrapper.getMessageCallback(), new BiConsumer() { // from class: com.android.services.telephony.rcs.SipDelegateController$$ExternalSyntheticLambda5
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                completableFuture.complete(new Pair((ISipDelegate) obj, (Set) obj2));
            }
        })) {
            return completableFuture;
        }
        logw("Couldn't create binder connection, ImsService is not available.");
        delegateBinderStateManager.destroy(1, null);
        return CompletableFuture.completedFuture(null);
    }

    private DelegateBinderStateManager createBinderConnection(Set set, Set set2) {
        ArrayList arrayList = new ArrayList(2);
        arrayList.add(this.mDelegateStateTracker);
        arrayList.add(this.mMessageTransportWrapper);
        return this.mBinderConnectionFactory.create(this.mSubId, new DelegateRequest(set), set2, this.mExecutorService, arrayList);
    }

    public void setSipDialogsListener(SipDialogsStateListener sipDialogsStateListener, boolean z) {
        this.mMessageTransportWrapper.setSipDialogsListener(sipDialogsStateListener, z);
    }

    public void dump(PrintWriter printWriter) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("SipDelegateController[" + this.mSubId + "]:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("Most recent logs:");
        indentingPrintWriter.increaseIndent();
        this.mLocalLog.dump(indentingPrintWriter);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println();
        indentingPrintWriter.println("DelegateStateTracker:");
        indentingPrintWriter.increaseIndent();
        this.mDelegateStateTracker.dump(indentingPrintWriter);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println();
        indentingPrintWriter.println("MessageStateTracker:");
        indentingPrintWriter.increaseIndent();
        this.mMessageTransportWrapper.dump(indentingPrintWriter);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.decreaseIndent();
    }

    private void logi(String str) {
        Log.i("SipTransportC", "SipDelegateC[" + this.mSubId + "] " + str);
        LocalLog localLog = this.mLocalLog;
        StringBuilder sb = new StringBuilder("[I] ");
        sb.append(str);
        localLog.log(sb.toString());
    }

    private void logw(String str) {
        Log.w("SipTransportC", "SipDelegateC[" + this.mSubId + "] " + str);
        LocalLog localLog = this.mLocalLog;
        StringBuilder sb = new StringBuilder("[W] ");
        sb.append(str);
        localLog.log(sb.toString());
    }
}
