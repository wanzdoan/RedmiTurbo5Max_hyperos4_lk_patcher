package com.android.services.telephony.rcs;

import android.app.role.OnRoleHoldersChangedListener;
import android.app.role.RoleManager;
import android.content.Context;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.telephony.CarrierConfigManager;
import android.telephony.ims.DelegateRequest;
import android.telephony.ims.FeatureTagState;
import android.telephony.ims.ImsException;
import android.telephony.ims.aidl.IImsRegistration;
import android.telephony.ims.aidl.ISipDelegate;
import android.telephony.ims.aidl.ISipDelegateConnectionStateCallback;
import android.telephony.ims.aidl.ISipDelegateMessageCallback;
import android.telephony.ims.aidl.ISipTransport;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import androidx.profileinstaller.ProfileInstallReceiver$$ExternalSyntheticLambda0;
import com.android.ims.RcsFeatureManager;
import com.android.internal.telephony.ISipDialogStateCallback;
import com.android.internal.telephony.IndentingPrintWriter;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.util.RemoteCallbackListExt;
import com.android.phone.RcsProvisioningMonitor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public class SipTransportController implements RcsFeatureController.Feature, OnRoleHoldersChangedListener {
    private final TrackedAppBinders mActiveAppBinders;
    private String mCachedSmsRolePackageName;
    private CarrierConfigManager mCarrierConfigManager;
    private SipDelegateControllerFactory mDelegateControllerFactory;
    private final List mDelegatePendingCreate;
    private final List mDelegatePendingDestroy;
    private final List mDelegatePriorityQueue;
    private CompletableFuture mEvaluateCompleteFuture;
    private final ScheduledExecutorService mExecutorService;
    private ArraySet mFeatureTagsAllowed;
    private final LocalLog mLocalLog;
    private UserHandle mMainUser;
    private ScheduledFuture mPendingUpdateRegistrationFuture;
    private RcsFeatureManager mRcsManager;
    private final RoleManagerAdapter mRoleManagerAdapter;
    private ScheduledFuture mScheduledEvaluateFuture;
    private final RemoteCallbackListExt mSipDialogStateCallbacks;
    private final SipDialogsStateListener mSipDialogsListener;
    private final int mSlotId;
    private int mSubId;
    private final TimerAdapter mTimerAdapter;

    public interface RoleManagerAdapter {
        void addOnRoleHoldersChangedListenerAsUser(Executor executor, OnRoleHoldersChangedListener onRoleHoldersChangedListener, UserHandle userHandle);

        List getRoleHolders(String str);

        List getRoleHoldersAsUser(String str, UserHandle userHandle);

        void removeOnRoleHoldersChangedListenerAsUser(OnRoleHoldersChangedListener onRoleHoldersChangedListener, UserHandle userHandle);
    }

    public interface SipDelegateControllerFactory {
        SipDelegateController create(int i, int i2, DelegateRequest delegateRequest, String str, ISipTransport iSipTransport, IImsRegistration iImsRegistration, ScheduledExecutorService scheduledExecutorService, ISipDelegateConnectionStateCallback iSipDelegateConnectionStateCallback, ISipDelegateMessageCallback iSipDelegateMessageCallback);
    }

    public interface TimerAdapter {
        int getReevaluateThrottleTimerMilliseconds();

        int getUpdateRegistrationDelayMilliseconds();
    }

    class TimerAdapterImpl implements TimerAdapter {
        @Override // com.android.services.telephony.rcs.SipTransportController.TimerAdapter
        public int getReevaluateThrottleTimerMilliseconds() {
            return 1000;
        }

        @Override // com.android.services.telephony.rcs.SipTransportController.TimerAdapter
        public int getUpdateRegistrationDelayMilliseconds() {
            return 1000;
        }

        private TimerAdapterImpl() {
        }
    }

    class RoleManagerAdapterImpl implements RoleManagerAdapter {
        private final RoleManager mRoleManager;

        private RoleManagerAdapterImpl(Context context) {
            this.mRoleManager = (RoleManager) context.getSystemService(RoleManager.class);
        }

        @Override // com.android.services.telephony.rcs.SipTransportController.RoleManagerAdapter
        public List getRoleHolders(String str) {
            return this.mRoleManager.getRoleHolders(str);
        }

        @Override // com.android.services.telephony.rcs.SipTransportController.RoleManagerAdapter
        public List getRoleHoldersAsUser(String str, UserHandle userHandle) {
            return this.mRoleManager.getRoleHoldersAsUser(str, userHandle);
        }

        @Override // com.android.services.telephony.rcs.SipTransportController.RoleManagerAdapter
        public void addOnRoleHoldersChangedListenerAsUser(Executor executor, OnRoleHoldersChangedListener onRoleHoldersChangedListener, UserHandle userHandle) {
            this.mRoleManager.addOnRoleHoldersChangedListenerAsUser(executor, onRoleHoldersChangedListener, userHandle);
        }

        @Override // com.android.services.telephony.rcs.SipTransportController.RoleManagerAdapter
        public void removeOnRoleHoldersChangedListenerAsUser(OnRoleHoldersChangedListener onRoleHoldersChangedListener, UserHandle userHandle) {
            this.mRoleManager.removeOnRoleHoldersChangedListenerAsUser(onRoleHoldersChangedListener, userHandle);
        }
    }

    class TrackedAppBinders extends RemoteCallbackList {
        private TrackedAppBinders() {
        }

        @Override // android.os.RemoteCallbackList
        public void onCallbackDied(ISipDelegateMessageCallback iSipDelegateMessageCallback, final Object obj) {
            SipTransportController.this.mExecutorService.execute(new Runnable() { // from class: com.android.services.telephony.rcs.SipTransportController$TrackedAppBinders$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onCallbackDied$1(obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCallbackDied$1(Object obj) {
            boolean z = obj instanceof SipDelegateController;
            SipTransportController sipTransportController = SipTransportController.this;
            if (z) {
                final SipDelegateController sipDelegateController = (SipDelegateController) obj;
                sipTransportController.logi("onCallbackDied - Cleaning up delegates associated with package: " + sipDelegateController.getPackageName());
                if (SipTransportController.this.addPendingDestroy(sipDelegateController, 1)) {
                    CompletableFuture completableFuture = new CompletableFuture();
                    SipTransportController.this.lambda$scheduleReevaluateNow$8(completableFuture);
                    completableFuture.thenRun(new Runnable() { // from class: com.android.services.telephony.rcs.SipTransportController$TrackedAppBinders$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$onCallbackDied$0(sipDelegateController);
                        }
                    });
                    return;
                }
                return;
            }
            sipTransportController.logw("onCallbackDied: unexpected - cookie is not an instance of SipDelegateController");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCallbackDied$0(SipDelegateController sipDelegateController) {
            SipTransportController.this.logi("onCallbackDied - clean up complete for package: " + sipDelegateController.getPackageName());
        }
    }

    final class DestroyRequest {
        public final SipDelegateController controller;
        public final int reason;

        DestroyRequest(SipDelegateController sipDelegateController, int i) {
            this.controller = sipDelegateController;
            this.reason = i;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || DestroyRequest.class != obj.getClass()) {
                return false;
            }
            return this.controller.equals(((DestroyRequest) obj).controller);
        }

        public int hashCode() {
            return Objects.hash(this.controller);
        }

        public String toString() {
            return "DestroyRequest{controller=" + this.controller + ", reason=" + this.reason + '}';
        }
    }

    final class SipDialogsStateHandle implements SipDialogsStateListener {
        private Executor mExecutor;
        Map mMapDialogState;

        private SipDialogsStateHandle() {
            this.mExecutor = new ProfileInstallReceiver$$ExternalSyntheticLambda0();
            this.mMapDialogState = new HashMap();
        }

        @Override // com.android.services.telephony.rcs.SipDialogsStateListener
        public void reMappingSipDelegateState(final String str, final List list) {
            this.mExecutor.execute(new Runnable() { // from class: com.android.services.telephony.rcs.SipTransportController$SipDialogsStateHandle$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$reMappingSipDelegateState$0(str, list);
                }
            });
        }

        @Override // com.android.services.telephony.rcs.SipDialogsStateListener
        public void notifySipDialogState() {
            this.mExecutor.execute(new Runnable() { // from class: com.android.services.telephony.rcs.SipTransportController$SipDialogsStateHandle$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$notifySipDialogState$1();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: processReMappingSipDelegateState, reason: merged with bridge method [inline-methods] */
        public void lambda$reMappingSipDelegateState$0(String str, List list) {
            boolean zIsEmpty = list.isEmpty();
            Map map = this.mMapDialogState;
            if (zIsEmpty) {
                map.remove(str);
            } else {
                map.put(str, list);
            }
            notifySipDialogState();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: processNotifySipDialogState, reason: merged with bridge method [inline-methods] */
        public void lambda$notifySipDialogState$1() {
            final ArrayList arrayList = new ArrayList();
            Iterator it = this.mMapDialogState.values().iterator();
            while (it.hasNext()) {
                arrayList.addAll((List) it.next());
            }
            if (SipTransportController.this.mSipDialogStateCallbacks.getRegisteredCallbackCount() == 0) {
                return;
            }
            SipTransportController.this.mSipDialogStateCallbacks.broadcastAction(new Consumer() { // from class: com.android.services.telephony.rcs.SipTransportController$SipDialogsStateHandle$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    SipTransportController.SipDialogsStateHandle.m4492$r8$lambda$nE3IgYA0FgBRMS3wuD2VL8rEl4(arrayList, (ISipDialogStateCallback) obj);
                }
            });
        }

        /* JADX INFO: renamed from: $r8$lambda$nE3IgYA0FgBRMS-3wuD2VL8rEl4, reason: not valid java name */
        public static /* synthetic */ void m4492$r8$lambda$nE3IgYA0FgBRMS3wuD2VL8rEl4(List list, ISipDialogStateCallback iSipDialogStateCallback) {
            try {
                iSipDialogStateCallback.onActiveSipDialogsChanged(list);
            } catch (RemoteException e) {
                Log.e("SipTransportC", "onActiveSipDialogsChanged() - Skipping callback." + e);
            }
        }
    }

    public SipTransportController(Context context, int i, int i2) {
        this.mDelegateControllerFactory = new SipDelegateControllerFactory() { // from class: com.android.services.telephony.rcs.SipTransportController$$ExternalSyntheticLambda1
            @Override // com.android.services.telephony.rcs.SipTransportController.SipDelegateControllerFactory
            public final SipDelegateController create(int i3, int i4, DelegateRequest delegateRequest, String str, ISipTransport iSipTransport, IImsRegistration iImsRegistration, ScheduledExecutorService scheduledExecutorService, ISipDelegateConnectionStateCallback iSipDelegateConnectionStateCallback, ISipDelegateMessageCallback iSipDelegateMessageCallback) {
                return new SipDelegateController(i3, i4, delegateRequest, str, iSipTransport, iImsRegistration, scheduledExecutorService, iSipDelegateConnectionStateCallback, iSipDelegateMessageCallback);
            }
        };
        this.mLocalLog = new LocalLog(50);
        this.mDelegatePriorityQueue = new ArrayList();
        this.mDelegatePendingCreate = new ArrayList();
        this.mDelegatePendingDestroy = new ArrayList();
        this.mSipDialogStateCallbacks = new RemoteCallbackListExt();
        this.mSipDialogsListener = new SipDialogsStateHandle();
        this.mActiveAppBinders = new TrackedAppBinders();
        this.mCachedSmsRolePackageName = "";
        this.mFeatureTagsAllowed = new ArraySet();
        this.mSlotId = i;
        this.mSubId = i2;
        this.mRoleManagerAdapter = new RoleManagerAdapterImpl(context);
        this.mMainUser = ((UserManager) context.getSystemService(UserManager.class)).getMainUser();
        this.mTimerAdapter = new TimerAdapterImpl();
        this.mExecutorService = Executors.newSingleThreadScheduledExecutor();
        this.mCarrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
    }

    public SipTransportController(Context context, int i, int i2, SipDelegateControllerFactory sipDelegateControllerFactory, RoleManagerAdapter roleManagerAdapter, TimerAdapter timerAdapter, ScheduledExecutorService scheduledExecutorService) {
        this.mDelegateControllerFactory = new SipDelegateControllerFactory() { // from class: com.android.services.telephony.rcs.SipTransportController$$ExternalSyntheticLambda1
            @Override // com.android.services.telephony.rcs.SipTransportController.SipDelegateControllerFactory
            public final SipDelegateController create(int i3, int i4, DelegateRequest delegateRequest, String str, ISipTransport iSipTransport, IImsRegistration iImsRegistration, ScheduledExecutorService scheduledExecutorService2, ISipDelegateConnectionStateCallback iSipDelegateConnectionStateCallback, ISipDelegateMessageCallback iSipDelegateMessageCallback) {
                return new SipDelegateController(i3, i4, delegateRequest, str, iSipTransport, iImsRegistration, scheduledExecutorService2, iSipDelegateConnectionStateCallback, iSipDelegateMessageCallback);
            }
        };
        this.mLocalLog = new LocalLog(50);
        this.mDelegatePriorityQueue = new ArrayList();
        this.mDelegatePendingCreate = new ArrayList();
        this.mDelegatePendingDestroy = new ArrayList();
        this.mSipDialogStateCallbacks = new RemoteCallbackListExt();
        this.mSipDialogsListener = new SipDialogsStateHandle();
        this.mActiveAppBinders = new TrackedAppBinders();
        this.mCachedSmsRolePackageName = "";
        this.mFeatureTagsAllowed = new ArraySet();
        this.mSlotId = i;
        this.mSubId = i2;
        this.mRoleManagerAdapter = roleManagerAdapter;
        this.mTimerAdapter = timerAdapter;
        this.mDelegateControllerFactory = sipDelegateControllerFactory;
        this.mExecutorService = scheduledExecutorService;
        this.mCarrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
        logi("created");
    }

    @Override // com.android.services.telephony.rcs.RcsFeatureController.Feature
    public void onRcsConnected(final RcsFeatureManager rcsFeatureManager) {
        this.mExecutorService.submit(new Runnable() { // from class: com.android.services.telephony.rcs.SipTransportController$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onRcsConnected$0(rcsFeatureManager);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRcsDisconnected$1() {
        lambda$onRcsConnected$0(null);
    }

    @Override // com.android.services.telephony.rcs.RcsFeatureController.Feature
    public void onRcsDisconnected() {
        this.mExecutorService.submit(new Runnable() { // from class: com.android.services.telephony.rcs.SipTransportController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onRcsDisconnected$1();
            }
        });
    }

    @Override // com.android.services.telephony.rcs.RcsFeatureController.Feature
    public void onAssociatedSubscriptionUpdated(final int i) {
        this.mExecutorService.submit(new Runnable() { // from class: com.android.services.telephony.rcs.SipTransportController$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onAssociatedSubscriptionUpdated$2(i);
            }
        });
    }

    @Override // com.android.services.telephony.rcs.RcsFeatureController.Feature
    public void onCarrierConfigChanged() {
        this.mExecutorService.submit(new Runnable() { // from class: com.android.services.telephony.rcs.SipTransportController$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.onCarrierConfigChangedInternal();
            }
        });
    }

    @Override // com.android.services.telephony.rcs.RcsFeatureController.Feature
    public void onDestroy() {
        this.mExecutorService.submit(new Runnable() { // from class: com.android.services.telephony.rcs.SipTransportController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onDestroy$3();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDestroy$3() {
        this.mSubId = -1;
        triggerDeregistrationEvent();
        CompletableFuture completableFutureScheduleDestroyDelegates = scheduleDestroyDelegates(4);
        final ScheduledExecutorService scheduledExecutorService = this.mExecutorService;
        Objects.requireNonNull(scheduledExecutorService);
        completableFutureScheduleDestroyDelegates.thenRun(new Runnable() { // from class: com.android.services.telephony.rcs.SipTransportController$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                scheduledExecutorService.shutdown();
            }
        });
    }

    public void createSipDelegate(final int i, final int i2, final DelegateRequest delegateRequest, final String str, final ISipDelegateConnectionStateCallback iSipDelegateConnectionStateCallback, final ISipDelegateMessageCallback iSipDelegateMessageCallback) {
        logi("createSipDelegate: request= " + delegateRequest + ", packageName= " + str);
        final CompletableFuture completableFuture = new CompletableFuture();
        this.mExecutorService.submit(new Runnable() { // from class: com.android.services.telephony.rcs.SipTransportController$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$createSipDelegate$4(i, i2, delegateRequest, str, iSipDelegateConnectionStateCallback, iSipDelegateMessageCallback, completableFuture);
            }
        });
        try {
            ImsException imsException = (ImsException) completableFuture.get();
            logi("createSipDelegate: request finished");
            if (imsException == null) {
            } else {
                throw imsException;
            }
        } catch (InterruptedException | ExecutionException e) {
            logw("createSipDelegate: exception completing future: " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createSipDelegate$4(int i, int i2, DelegateRequest delegateRequest, String str, ISipDelegateConnectionStateCallback iSipDelegateConnectionStateCallback, ISipDelegateMessageCallback iSipDelegateMessageCallback, final CompletableFuture completableFuture) {
        Objects.requireNonNull(completableFuture);
        createSipDelegateInternal(i, i2, delegateRequest, str, iSipDelegateConnectionStateCallback, iSipDelegateMessageCallback, new Consumer() { // from class: com.android.services.telephony.rcs.SipTransportController$$ExternalSyntheticLambda16
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                completableFuture.complete((ImsException) obj);
            }
        });
    }

    public void destroySipDelegate(final int i, final ISipDelegate iSipDelegate, final int i2) {
        this.mExecutorService.execute(new Runnable() { // from class: com.android.services.telephony.rcs.SipTransportController$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$destroySipDelegate$5(i, iSipDelegate, i2);
            }
        });
    }

    public void triggerFullNetworkRegistration(final int i, final ISipDelegate iSipDelegate, final int i2, final String str) {
        this.mExecutorService.execute(new Runnable() { // from class: com.android.services.telephony.rcs.SipTransportController$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$triggerFullNetworkRegistration$6(i, iSipDelegate, i2, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$isSupported$7(int i) {
        return Boolean.valueOf(isSupportedInternal(i));
    }

    public boolean isSupported(final int i) {
        Boolean bool = (Boolean) waitForMethodToComplete(new Callable() { // from class: com.android.services.telephony.rcs.SipTransportController$$ExternalSyntheticLambda5
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return this.f$0.lambda$isSupported$7(i);
            }
        });
        if (bool == null) {
            logw("isSupported, unexpected null result, returning false");
            return false;
        }
        return bool.booleanValue();
    }

    private void createSipDelegateInternal(int i, int i2, DelegateRequest delegateRequest, String str, ISipDelegateConnectionStateCallback iSipDelegateConnectionStateCallback, ISipDelegateMessageCallback iSipDelegateMessageCallback, Consumer consumer) {
        try {
            checkStateOfController(i);
            ISipTransport sipTransport = this.mRcsManager.getSipTransport();
            IImsRegistration imsRegistration = this.mRcsManager.getImsRegistration();
            if (sipTransport == null) {
                logw("createSipDelegateInternal, transport null during request.");
                consumer.accept(new ImsException("SipTransport not supported", 2));
                return;
            }
            consumer.accept(null);
            SipDelegateController sipDelegateControllerCreate = this.mDelegateControllerFactory.create(i, i2, delegateRequest, str, sipTransport, imsRegistration, this.mExecutorService, iSipDelegateConnectionStateCallback, iSipDelegateMessageCallback);
            logi("createSipDelegateInternal: request= " + delegateRequest + ", packageName= " + str + ", controller created: " + sipDelegateControllerCreate);
            addPendingCreateAndEvaluate(sipDelegateControllerCreate);
            if (this.mSipDialogStateCallbacks.getRegisteredCallbackCount() > 0) {
                sipDelegateControllerCreate.setSipDialogsListener(this.mSipDialogsListener, false);
            }
        } catch (ImsException e) {
            logw("createSipDelegateInternal, ImsException during create: " + e);
            consumer.accept(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: destroySipDelegateInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$destroySipDelegate$5(int i, ISipDelegate iSipDelegate, int i2) {
        SipDelegateController sipDelegateController;
        if (i != this.mSubId) {
            logw("destroySipDelegateInternal: ignoring destroy, as this is about to be destroyed anyway due to subId change, delegate=" + iSipDelegate);
            return;
        }
        if (iSipDelegate == null) {
            logw("destroySipDelegateInternal: ignoring destroy, null connection binder.");
            return;
        }
        Iterator it = this.mDelegatePriorityQueue.iterator();
        do {
            if (!it.hasNext()) {
                sipDelegateController = null;
                break;
            }
            sipDelegateController = (SipDelegateController) it.next();
        } while (!com.google.common.base.Objects.equal(iSipDelegate.asBinder(), sipDelegateController.getSipDelegateInterface().asBinder()));
        if (sipDelegateController == null) {
            logw("destroySipDelegateInternal: could not find matching connection=" + iSipDelegate);
        } else {
            logi("destroySipDelegateInternal: destroy queued for connection= " + iSipDelegate);
            addPendingDestroyAndEvaluate(sipDelegateController, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: triggerFullNetworkRegistrationInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$triggerFullNetworkRegistration$6(int i, ISipDelegate iSipDelegate, int i2, String str) {
        SipDelegateController sipDelegateController;
        if (i != this.mSubId) {
            logw("triggerFullNetworkRegistrationInternal: ignoring network reg request, as this isabout to be destroyed anyway due to subId change, delegate=" + iSipDelegate);
        } else {
            if (iSipDelegate == null) {
                logw("triggerFullNetworkRegistrationInternal: ignoring, null connection binder.");
                return;
            }
            Iterator it = this.mDelegatePriorityQueue.iterator();
            do {
                if (!it.hasNext()) {
                    sipDelegateController = null;
                    break;
                }
                sipDelegateController = (SipDelegateController) it.next();
            } while (!com.google.common.base.Objects.equal(iSipDelegate.asBinder(), sipDelegateController.getSipDelegateInterface().asBinder()));
            if (sipDelegateController == null) {
                logw("triggerFullNetworkRegistrationInternal: could not find matching connection, ignoring");
            } else {
                sipDelegateController.triggerFullNetworkRegistration(i2, str);
            }
        }
    }

    private void triggerDeregistrationEvent() {
        logi("triggerDeregistrationEvent: Sending deregister event to ImsService");
        cancelPendingUpdateRegistration();
        IImsRegistration imsRegistration = this.mRcsManager.getImsRegistration();
        if (imsRegistration != null) {
            try {
                imsRegistration.triggerSipDelegateDeregistration();
            } catch (RemoteException e) {
                logi("triggerDeregistrationEvent: received RemoteException: " + e);
            }
        }
    }

    private void scheduleUpdateRegistration() {
        cancelPendingUpdateRegistration();
        ScheduledFuture<?> scheduledFutureSchedule = this.mExecutorService.schedule(new Runnable() { // from class: com.android.services.telephony.rcs.SipTransportController$$ExternalSyntheticLambda25
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.triggerUpdateRegistrationEvent();
            }
        }, this.mTimerAdapter.getUpdateRegistrationDelayMilliseconds(), TimeUnit.MILLISECONDS);
        logi("scheduleUpdateRegistration: scheduling new event: " + scheduledFutureSchedule);
        this.mPendingUpdateRegistrationFuture = scheduledFutureSchedule;
    }

    private void cancelPendingUpdateRegistration() {
        ScheduledFuture scheduledFuture = this.mPendingUpdateRegistrationFuture;
        if (scheduledFuture == null || scheduledFuture.isDone()) {
            return;
        }
        this.mPendingUpdateRegistrationFuture.cancel(false);
        logi("scheduleUpdateRegistration: cancelling existing reg update event: " + this.mPendingUpdateRegistrationFuture);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void triggerUpdateRegistrationEvent() {
        logi("triggerUpdateRegistrationEvent: Sending update registration event to ImsService");
        IImsRegistration imsRegistration = this.mRcsManager.getImsRegistration();
        if (imsRegistration != null) {
            try {
                imsRegistration.triggerUpdateSipDelegateRegistration();
            } catch (RemoteException e) {
                logi("triggerUpdateRegistrationEvent: received RemoteException: " + e);
            }
        }
    }

    private boolean isSupportedInternal(int i) throws ImsException {
        checkStateOfController(i);
        return this.mRcsManager.getSipTransport() != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean addPendingDestroy(SipDelegateController sipDelegateController, int i) {
        DestroyRequest destroyRequest = new DestroyRequest(sipDelegateController, i);
        if (this.mDelegatePendingDestroy.contains(destroyRequest)) {
            return false;
        }
        return this.mDelegatePendingDestroy.add(destroyRequest);
    }

    private void addPendingDestroyAndEvaluate(SipDelegateController sipDelegateController, int i) {
        if (addPendingDestroy(sipDelegateController, i)) {
            scheduleThrottledReevaluate();
        }
    }

    private void addPendingCreateAndEvaluate(SipDelegateController sipDelegateController) {
        this.mDelegatePendingCreate.add(sipDelegateController);
        scheduleThrottledReevaluate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleThrottledReevaluate() {
        if (isEvaluatePendingAndNotInProgress()) {
            logi("scheduleThrottledReevaluate: throttling reevaluate, eval already pending: " + this.mScheduledEvaluateFuture);
        } else {
            this.mScheduledEvaluateFuture = this.mExecutorService.schedule(new Runnable() { // from class: com.android.services.telephony.rcs.SipTransportController$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.reevaluateDelegates();
                }
            }, this.mTimerAdapter.getReevaluateThrottleTimerMilliseconds(), TimeUnit.MILLISECONDS);
            logi("scheduleThrottledReevaluate: new reevaluate scheduled: " + this.mScheduledEvaluateFuture);
        }
    }

    private boolean isEvaluatePendingAndNotInProgress() {
        ScheduledFuture scheduledFuture = this.mScheduledEvaluateFuture;
        boolean z = (scheduledFuture == null || scheduledFuture.isDone()) ? false : true;
        CompletableFuture completableFuture = this.mEvaluateCompleteFuture;
        return z && !(completableFuture != null && !completableFuture.isDone());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: scheduleReevaluateNow, reason: merged with bridge method [inline-methods] */
    public void lambda$scheduleReevaluateNow$8(final CompletableFuture completableFuture) {
        if (isEvaluatePendingAndNotInProgress()) {
            this.mScheduledEvaluateFuture.cancel(false);
            logi("scheduleReevaluateNow: cancelling pending reevaluate: " + this.mScheduledEvaluateFuture);
        }
        CompletableFuture completableFuture2 = this.mEvaluateCompleteFuture;
        if (completableFuture2 != null && !completableFuture2.isDone()) {
            this.mEvaluateCompleteFuture.thenRunAsync(new Runnable() { // from class: com.android.services.telephony.rcs.SipTransportController$$ExternalSyntheticLambda18
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$scheduleReevaluateNow$8(completableFuture);
                }
            }, (Executor) this.mExecutorService);
            return;
        }
        reevaluateDelegates();
        CompletableFuture completableFuture3 = this.mEvaluateCompleteFuture;
        Objects.requireNonNull(completableFuture);
        completableFuture3.thenAccept(new Consumer() { // from class: com.android.services.telephony.rcs.SipTransportController$$ExternalSyntheticLambda19
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                completableFuture.complete((Void) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reevaluateDelegates() {
        cancelPendingUpdateRegistration();
        CompletableFuture completableFuture = this.mEvaluateCompleteFuture;
        if (completableFuture != null && !completableFuture.isDone()) {
            logw("reevaluateDelegates: last evaluate not done, deferring new request");
            this.mEvaluateCompleteFuture.thenRunAsync(new Runnable() { // from class: com.android.services.telephony.rcs.SipTransportController$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.scheduleThrottledReevaluate();
                }
            }, (Executor) this.mExecutorService);
            return;
        }
        for (DestroyRequest destroyRequest : this.mDelegatePendingDestroy) {
            logi("reevaluateDelegates: starting destroy for: " + destroyRequest.controller.getPackageName());
            this.mActiveAppBinders.unregister(destroyRequest.controller.getAppMessageCallback());
        }
        CompletableFuture<Void> completableFutureAllOf = CompletableFuture.allOf((CompletableFuture[]) ((List) this.mDelegatePendingDestroy.stream().map(new Function() { // from class: com.android.services.telephony.rcs.SipTransportController$$ExternalSyntheticLambda10
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.lambda$reevaluateDelegates$9((SipTransportController.DestroyRequest) obj);
            }
        }).collect(Collectors.toList())).toArray(new CompletableFuture[this.mDelegatePendingDestroy.size()]));
        this.mDelegatePriorityQueue.removeAll((Collection) this.mDelegatePendingDestroy.stream().map(new Function() { // from class: com.android.services.telephony.rcs.SipTransportController$$ExternalSyntheticLambda11
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((SipTransportController.DestroyRequest) obj).controller;
            }
        }).collect(Collectors.toList()));
        this.mDelegatePendingDestroy.clear();
        this.mDelegatePriorityQueue.addAll(this.mDelegatePendingCreate);
        for (SipDelegateController sipDelegateController : this.mDelegatePendingCreate) {
            logi("reevaluateDelegates: pending create: " + sipDelegateController.getPackageName());
            this.mActiveAppBinders.register(sipDelegateController.getAppMessageCallback(), sipDelegateController);
        }
        this.mDelegatePendingCreate.clear();
        CompletableFuture completableFutureThenCompose = completableFutureAllOf.thenCompose(new Function() { // from class: com.android.services.telephony.rcs.SipTransportController$$ExternalSyntheticLambda12
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.lambda$reevaluateDelegates$11((Void) obj);
            }
        });
        final String str = this.mCachedSmsRolePackageName;
        for (final SipDelegateController sipDelegateController2 : this.mDelegatePriorityQueue) {
            logi("reevaluateDelegates: pending reeval: " + sipDelegateController2);
            completableFutureThenCompose = completableFutureThenCompose.thenComposeAsync(new Function() { // from class: com.android.services.telephony.rcs.SipTransportController$$ExternalSyntheticLambda13
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return this.f$0.lambda$reevaluateDelegates$12(sipDelegateController2, str, (Set) obj);
                }
            }, (Executor) this.mExecutorService);
        }
        this.mEvaluateCompleteFuture = completableFutureThenCompose.whenComplete(new BiConsumer() { // from class: com.android.services.telephony.rcs.SipTransportController$$ExternalSyntheticLambda14
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                this.f$0.lambda$reevaluateDelegates$13((Set) obj, (Throwable) obj2);
            }
        }).thenAccept(new Consumer() { // from class: com.android.services.telephony.rcs.SipTransportController$$ExternalSyntheticLambda15
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$reevaluateDelegates$14((Set) obj);
            }
        });
        logi("reevaluateDelegates: future created.");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ CompletableFuture lambda$reevaluateDelegates$9(DestroyRequest destroyRequest) {
        return triggerDestroy(destroyRequest.controller, destroyRequest.reason);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ CompletionStage lambda$reevaluateDelegates$11(Void r1) {
        logi("reevaluateDelegates: destroy phase complete");
        return CompletableFuture.completedFuture(new ArraySet());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ CompletionStage lambda$reevaluateDelegates$12(SipDelegateController sipDelegateController, String str, Set set) {
        logi("reevaluateDelegates: last stage completed with result:" + set);
        if (set == null) {
            return CompletableFuture.completedFuture(null);
        }
        return changeSupportedFeatureTags(sipDelegateController, str, set);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reevaluateDelegates$13(Set set, Throwable th) {
        if (th != null) {
            logw("reevaluateDelegates: Exception caught", th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reevaluateDelegates$14(Set set) {
        logi("reevaluateDelegates: reevaluate complete, feature tags associated: " + set);
        scheduleUpdateRegistration();
    }

    private CompletableFuture triggerDestroy(SipDelegateController sipDelegateController, final int i) {
        return sipDelegateController.destroy(isForcedFromReason(i), i).thenAccept(new Consumer() { // from class: com.android.services.telephony.rcs.SipTransportController$$ExternalSyntheticLambda22
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$triggerDestroy$15(i, (Integer) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$triggerDestroy$15(int i, Integer num) {
        logi("destroy triggered with " + i + " and finished with reason= " + num);
    }

    private boolean isForcedFromReason(int i) {
        if (i != 0) {
            if (i != 1) {
                if (i != 2 && i != 3) {
                    if (i != 4) {
                        logw("isForcedFromReason, unexpected reason: " + i);
                        return false;
                    }
                }
            }
            return true;
        }
        logw("isForcedFromReason, unknown reason");
        return false;
    }

    public void onRoleHoldersChanged(String str, UserHandle userHandle) {
        logi("onRoleHoldersChanged, roleName= " + str + ", user= " + userHandle);
        UserHandle userHandle2 = this.mMainUser;
        if (userHandle2 != null ? userHandle2.equals(userHandle) : UserHandle.SYSTEM.equals(userHandle)) {
            if (!"android.app.role.SMS".equals(str)) {
                logi("onRoleHoldersChanged, ignoring non SMS role change");
            } else if (updateRoleCache()) {
                if (!TextUtils.isEmpty(this.mCachedSmsRolePackageName)) {
                    triggerDeregistrationEvent();
                }
                scheduleThrottledReevaluate();
            }
        }
    }

    private boolean updateRoleCache() {
        String str = "";
        try {
            UserHandle userHandle = this.mMainUser;
            RoleManagerAdapter roleManagerAdapter = this.mRoleManagerAdapter;
            str = userHandle != null ? (String) roleManagerAdapter.getRoleHoldersAsUser("android.app.role.SMS", userHandle).stream().findFirst().orElse("") : (String) roleManagerAdapter.getRoleHolders("android.app.role.SMS").stream().findFirst().orElse("");
        } catch (Exception e) {
            logi("updateRoleCache: exception=" + e);
        }
        logi("updateRoleCache: new packageName=" + str);
        if (TextUtils.equals(this.mCachedSmsRolePackageName, str)) {
            logi("updateRoleCache, skipping, role did not change");
            return false;
        }
        this.mCachedSmsRolePackageName = str;
        return true;
    }

    private CompletableFuture changeSupportedFeatureTags(SipDelegateController sipDelegateController, String str, final Set set) {
        Set featureTags = sipDelegateController.getInitialRequest().getFeatureTags();
        String packageName = sipDelegateController.getPackageName();
        logi("changeSupportedFeatureTags smsRolePackageName: " + str);
        if (!str.equals(packageName)) {
            ArraySet arraySet = new ArraySet();
            Iterator it = featureTags.iterator();
            while (it.hasNext()) {
                arraySet.add(new FeatureTagState((String) it.next(), 2));
            }
            CompletableFuture completableFutureChangeSupportedFeatureTags = sipDelegateController.changeSupportedFeatureTags(Collections.EMPTY_SET, arraySet);
            logi("changeSupportedFeatureTags pendingDeny=" + completableFutureChangeSupportedFeatureTags);
            return completableFutureChangeSupportedFeatureTags.thenApply(new Function() { // from class: com.android.services.telephony.rcs.SipTransportController$$ExternalSyntheticLambda23
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return this.f$0.lambda$changeSupportedFeatureTags$16(set, (Boolean) obj);
                }
            });
        }
        final ArraySet arraySet2 = new ArraySet(set);
        ArraySet arraySet3 = new ArraySet(featureTags);
        Set setUpdateSupportedTags = updateSupportedTags(arraySet3, arraySet2);
        arraySet2.addAll(arraySet3);
        CompletableFuture completableFutureChangeSupportedFeatureTags2 = sipDelegateController.changeSupportedFeatureTags(arraySet3, setUpdateSupportedTags);
        logi("changeSupportedFeatureTags pendingChange=" + completableFutureChangeSupportedFeatureTags2);
        return completableFutureChangeSupportedFeatureTags2.thenApply(new Function() { // from class: com.android.services.telephony.rcs.SipTransportController$$ExternalSyntheticLambda24
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.lambda$changeSupportedFeatureTags$17(arraySet2, (Boolean) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Set lambda$changeSupportedFeatureTags$16(Set set, Boolean bool) {
        logi("changeSupportedFeatureTags: deny completed: " + bool);
        if (bool.booleanValue()) {
            return set;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Set lambda$changeSupportedFeatureTags$17(ArraySet arraySet, Boolean bool) {
        logi("changeSupportedFeatureTags: change completed: " + bool);
        if (bool.booleanValue()) {
            return arraySet;
        }
        return null;
    }

    private Set updateSupportedTags(Set set, Set set2) {
        RcsProvisioningMonitor rcsProvisioningMonitor = RcsProvisioningMonitor.getInstance();
        Boolean imsFeatureValidationOverride = rcsProvisioningMonitor == null ? null : rcsProvisioningMonitor.getImsFeatureValidationOverride(this.mSubId);
        ArraySet arraySet = new ArraySet();
        if (imsFeatureValidationOverride == null) {
            Iterator it = set.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                if (set2.contains(str)) {
                    logi(str + " has already been granted previously.");
                    it.remove();
                    arraySet.add(new FeatureTagState(str, 1));
                } else if (!this.mFeatureTagsAllowed.contains(str.trim().toLowerCase(Locale.ROOT))) {
                    logi(str.concat(" is not allowed per config."));
                    it.remove();
                    arraySet.add(new FeatureTagState(str, 2));
                }
            }
        } else if (Boolean.FALSE.equals(imsFeatureValidationOverride)) {
            logi("all features are denied for test purpose.");
            Iterator it2 = set.iterator();
            while (it2.hasNext()) {
                arraySet.add(new FeatureTagState((String) it2.next(), 2));
            }
            set.clear();
        }
        return arraySet;
    }

    private Object waitForMethodToComplete(Callable callable) throws ImsException {
        try {
            return this.mExecutorService.submit(callable).get();
        } catch (InterruptedException unused) {
            return null;
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ImsException) {
                throw ((ImsException) cause);
            }
            logw("Unexpected Exception, returning null: " + cause);
            return null;
        }
    }

    private void checkStateOfController(int i) throws ImsException {
        if (this.mSubId != i) {
            throw new ImsException("subId is no longer valid for this request.", 3);
        }
        if (this.mRcsManager == null) {
            throw new ImsException("Connection to ImsService is not available", 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onRcsManagerChanged, reason: merged with bridge method [inline-methods] */
    public void lambda$onRcsConnected$0(RcsFeatureManager rcsFeatureManager) {
        logi("manager changed, " + this.mRcsManager + "->" + rcsFeatureManager);
        if (this.mRcsManager == rcsFeatureManager) {
            return;
        }
        this.mRcsManager = rcsFeatureManager;
        if (rcsFeatureManager == null) {
            logi("onRcsManagerChanged: lost connection to ImsService, tearing down...");
            unregisterListeners();
            scheduleDestroyDelegates(1);
        } else {
            logi("onRcsManagerChanged: registering listeners/updating role cache...");
            registerListeners();
            updateRoleCache();
        }
    }

    private void registerListeners() {
        try {
            UserHandle userHandle = this.mMainUser;
            if (userHandle == null) {
                userHandle = UserHandle.SYSTEM;
            }
            logi("registerListeners user: " + userHandle);
            this.mRoleManagerAdapter.addOnRoleHoldersChangedListenerAsUser(this.mExecutorService, this, userHandle);
        } catch (Exception e) {
            logi("registerListeners: exception=" + e);
        }
    }

    private void unregisterListeners() {
        this.mCachedSmsRolePackageName = "";
        UserHandle userHandle = this.mMainUser;
        if (userHandle == null) {
            userHandle = UserHandle.SYSTEM;
        }
        logi("unregisterListeners user: " + userHandle);
        this.mRoleManagerAdapter.removeOnRoleHoldersChangedListenerAsUser(this, userHandle);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onSubIdChanged, reason: merged with bridge method [inline-methods] */
    public void lambda$onAssociatedSubscriptionUpdated$2(int i) {
        logi("subId changed, " + this.mSubId + "->" + i);
        if (this.mSubId != i) {
            this.mSubId = i;
            scheduleDestroyDelegates(4);
        }
        onCarrierConfigChangedInternal();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCarrierConfigChangedInternal() {
        logi("Carrier Config changed for subId: " + this.mSubId);
        this.mFeatureTagsAllowed.clear();
        String[] stringArray = this.mCarrierConfigManager.getConfigForSubId(this.mSubId).getStringArray("ims.rcs_feature_tag_allowed_string_array");
        if (stringArray == null || stringArray.length <= 0) {
            return;
        }
        for (String str : stringArray) {
            this.mFeatureTagsAllowed.add(str.trim().toLowerCase(Locale.ROOT));
        }
    }

    private CompletableFuture scheduleDestroyDelegates(int i) {
        boolean zAddPendingDestroy = false;
        for (SipDelegateController sipDelegateController : this.mDelegatePriorityQueue) {
            logi("scheduleDestroyDelegates: Controller pending destroy: " + sipDelegateController);
            zAddPendingDestroy |= addPendingDestroy(sipDelegateController, i);
        }
        if (zAddPendingDestroy) {
            CompletableFuture completableFuture = new CompletableFuture();
            lambda$scheduleReevaluateNow$8(completableFuture);
            return completableFuture;
        }
        return CompletableFuture.completedFuture(null);
    }

    public void addCallbackForSipDialogState(int i, ISipDialogStateCallback iSipDialogStateCallback) {
        if (i != this.mSubId) {
            logw("addCallbackForSipDialogState the subId is not supported");
            return;
        }
        this.mSipDialogStateCallbacks.register(iSipDialogStateCallback);
        if (!this.mDelegatePriorityQueue.isEmpty()) {
            Iterator it = this.mDelegatePriorityQueue.iterator();
            while (it.hasNext()) {
                ((SipDelegateController) it.next()).setSipDialogsListener(this.mSipDialogsListener, true);
            }
            return;
        }
        this.mSipDialogsListener.notifySipDialogState();
    }

    public void removeCallbackForSipDialogState(int i, ISipDialogStateCallback iSipDialogStateCallback) {
        if (i != this.mSubId) {
            logw("addCallbackForSipDialogState the subId is not supported");
            return;
        }
        if (iSipDialogStateCallback == null) {
            Folme$$ExternalSyntheticBUOutline0.m("callback is null");
            return;
        }
        this.mSipDialogStateCallbacks.unregister(iSipDialogStateCallback);
        if (this.mSipDialogStateCallbacks.getRegisteredCallbackCount() != 0 || this.mDelegatePriorityQueue.isEmpty()) {
            return;
        }
        Iterator it = this.mDelegatePriorityQueue.iterator();
        while (it.hasNext()) {
            ((SipDelegateController) it.next()).setSipDialogsListener(null, false);
        }
    }

    @Override // com.android.services.telephony.rcs.RcsFeatureController.Feature
    public void dump(PrintWriter printWriter) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("SipTransportController[" + this.mSlotId + "->" + this.mSubId + "]:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("LocalLog:");
        indentingPrintWriter.increaseIndent();
        this.mLocalLog.dump(indentingPrintWriter);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("SipDelegateControllers (in priority order):");
        indentingPrintWriter.increaseIndent();
        if (this.mDelegatePriorityQueue.isEmpty()) {
            indentingPrintWriter.println("[NONE]");
        } else {
            Iterator it = this.mDelegatePriorityQueue.iterator();
            while (it.hasNext()) {
                ((SipDelegateController) it.next()).dump(indentingPrintWriter);
            }
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.decreaseIndent();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logi(String str) {
        Log.i("SipTransportC", "[" + this.mSlotId + "->" + this.mSubId + "] " + str);
        LocalLog localLog = this.mLocalLog;
        StringBuilder sb = new StringBuilder("[I] ");
        sb.append(str);
        localLog.log(sb.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logw(String str) {
        Log.w("SipTransportC", "[" + this.mSlotId + "->" + this.mSubId + "] " + str);
        LocalLog localLog = this.mLocalLog;
        StringBuilder sb = new StringBuilder("[W] ");
        sb.append(str);
        localLog.log(sb.toString());
    }

    private void logw(String str, Throwable th) {
        Log.w("SipTransportC", "[" + this.mSlotId + "->" + this.mSubId + "] " + str, th);
        LocalLog localLog = this.mLocalLog;
        StringBuilder sb = new StringBuilder("[W] ");
        sb.append(str);
        sb.append(": ");
        sb.append(th);
        localLog.log(sb.toString());
    }
}
