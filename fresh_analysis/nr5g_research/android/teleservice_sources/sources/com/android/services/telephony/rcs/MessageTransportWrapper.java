package com.android.services.telephony.rcs;

import android.os.Binder;
import android.os.RemoteException;
import android.telephony.ims.DelegateRegistrationState;
import android.telephony.ims.SipDelegateConfiguration;
import android.telephony.ims.SipDelegateImsConfiguration;
import android.telephony.ims.SipMessage;
import android.telephony.ims.aidl.ISipDelegate;
import android.telephony.ims.aidl.ISipDelegateMessageCallback;
import android.util.Log;
import com.android.internal.telephony.IndentingPrintWriter;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.SipMessageParsingUtils;
import com.android.phone.RcsProvisioningMonitor;
import com.android.services.telephony.rcs.validator.ValidationResult;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public class MessageTransportWrapper implements DelegateBinderStateManager.StateCallback {
    private final ISipDelegateMessageCallback mAppCallback;
    private final ISipDelegateMessageCallback.Stub mDelegateConnectionMessageCallback;
    private final Executor mExecutor;
    private final LocalLog mLocalLog;
    private ISipDelegate mSipDelegate;
    private final ISipDelegate.Stub mSipDelegateConnection;
    private final TransportSipMessageValidator mSipSessionTracker;
    private final int mSubId;
    private final ValidatorOverride mValidatorOverride;

    public interface ValidatorOverride {
        Boolean getValidatorOverrideState();
    }

    /* JADX INFO: renamed from: com.android.services.telephony.rcs.MessageTransportWrapper$1, reason: invalid class name */
    class AnonymousClass1 extends ISipDelegate.Stub {
        AnonymousClass1() {
        }

        public void notifyMessageReceived(final String str) {
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                MessageTransportWrapper.this.mExecutor.execute(new Runnable() { // from class: com.android.services.telephony.rcs.MessageTransportWrapper$1$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$notifyMessageReceived$0(str);
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyMessageReceived$0(String str) {
            ISipDelegate iSipDelegate = MessageTransportWrapper.this.mSipDelegate;
            MessageTransportWrapper messageTransportWrapper = MessageTransportWrapper.this;
            if (iSipDelegate == null) {
                messageTransportWrapper.logw("notifyMessageReceived called when SipDelegate is not associated for transaction id: " + str);
                return;
            }
            try {
                messageTransportWrapper.mSipSessionTracker.acknowledgePendingMessage(str);
                MessageTransportWrapper.this.mSipDelegate.notifyMessageReceived(str);
            } catch (RemoteException unused) {
                MessageTransportWrapper.this.logw("SipDelegate not available when notifyMessageReceived was called for transaction id: " + str);
            }
        }

        public void notifyMessageReceiveError(final String str, final int i) {
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                MessageTransportWrapper.this.mExecutor.execute(new Runnable() { // from class: com.android.services.telephony.rcs.MessageTransportWrapper$1$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$notifyMessageReceiveError$1(str, i);
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyMessageReceiveError$1(String str, int i) {
            ISipDelegate iSipDelegate = MessageTransportWrapper.this.mSipDelegate;
            MessageTransportWrapper messageTransportWrapper = MessageTransportWrapper.this;
            if (iSipDelegate == null) {
                messageTransportWrapper.logw("notifyMessageReceiveError called when SipDelegate is not associated for transaction id: " + str);
                return;
            }
            try {
                messageTransportWrapper.mSipSessionTracker.notifyPendingMessageFailed(str);
                MessageTransportWrapper.this.mSipDelegate.notifyMessageReceiveError(str, i);
            } catch (RemoteException unused) {
                MessageTransportWrapper.this.logw("SipDelegate not available when notifyMessageReceiveError was called for transaction id: " + str);
            }
        }

        public void sendMessage(final SipMessage sipMessage, final long j) {
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                MessageTransportWrapper.this.mExecutor.execute(new Runnable() { // from class: com.android.services.telephony.rcs.MessageTransportWrapper$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$sendMessage$2(sipMessage, j);
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$sendMessage$2(SipMessage sipMessage, long j) {
            ValidationResult validationResultMaybeOverrideValidationForTesting = MessageTransportWrapper.this.maybeOverrideValidationForTesting(MessageTransportWrapper.this.mSipSessionTracker.verifyOutgoingMessage(sipMessage, j));
            boolean z = validationResultMaybeOverrideValidationForTesting.isValidated;
            MessageTransportWrapper messageTransportWrapper = MessageTransportWrapper.this;
            if (!z) {
                messageTransportWrapper.notifyDelegateSendError("Outgoing - " + validationResultMaybeOverrideValidationForTesting.logReason, sipMessage, validationResultMaybeOverrideValidationForTesting.restrictedReason);
                return;
            }
            try {
                ISipDelegate iSipDelegate = messageTransportWrapper.mSipDelegate;
                MessageTransportWrapper messageTransportWrapper2 = MessageTransportWrapper.this;
                if (iSipDelegate == null) {
                    messageTransportWrapper2.logw("sendMessage called when SipDelegate is not associated." + sipMessage);
                    MessageTransportWrapper.this.notifyDelegateSendError("No SipDelegate", sipMessage, 1);
                    return;
                }
                messageTransportWrapper2.mSipDelegate.sendMessage(sipMessage, j);
            } catch (RemoteException e) {
                MessageTransportWrapper.this.notifyDelegateSendError("RemoteException: " + e, sipMessage, 1);
            }
        }

        public void cleanupSession(final String str) {
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                MessageTransportWrapper.this.mExecutor.execute(new Runnable() { // from class: com.android.services.telephony.rcs.MessageTransportWrapper$1$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$cleanupSession$3(str);
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$cleanupSession$3(String str) {
            MessageTransportWrapper.this.cleanupSessionInternal(str);
        }
    }

    /* JADX INFO: renamed from: com.android.services.telephony.rcs.MessageTransportWrapper$2, reason: invalid class name */
    class AnonymousClass2 extends ISipDelegateMessageCallback.Stub {
        AnonymousClass2() {
        }

        public void onMessageReceived(final SipMessage sipMessage) {
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                MessageTransportWrapper.this.mExecutor.execute(new Runnable() { // from class: com.android.services.telephony.rcs.MessageTransportWrapper$2$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onMessageReceived$0(sipMessage);
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMessageReceived$0(SipMessage sipMessage) {
            ValidationResult validationResultVerifyIncomingMessage = MessageTransportWrapper.this.mSipSessionTracker.verifyIncomingMessage(sipMessage);
            boolean z = validationResultVerifyIncomingMessage.isValidated;
            MessageTransportWrapper messageTransportWrapper = MessageTransportWrapper.this;
            if (!z) {
                messageTransportWrapper.notifyAppReceiveError("Incoming - " + validationResultVerifyIncomingMessage.logReason, sipMessage, validationResultVerifyIncomingMessage.restrictedReason);
                return;
            }
            try {
                messageTransportWrapper.mAppCallback.onMessageReceived(sipMessage);
            } catch (RemoteException e) {
                MessageTransportWrapper.this.notifyAppReceiveError("RemoteException: " + e, sipMessage, 1);
            }
        }

        public void onMessageSent(final String str) {
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                MessageTransportWrapper.this.mExecutor.execute(new Runnable() { // from class: com.android.services.telephony.rcs.MessageTransportWrapper$2$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onMessageSent$1(str);
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMessageSent$1(String str) {
            if (MessageTransportWrapper.this.mSipDelegate == null) {
                MessageTransportWrapper.this.logw("Unexpected state, onMessageSent called when SipDelegate is not associated");
            }
            try {
                MessageTransportWrapper.this.mSipSessionTracker.acknowledgePendingMessage(str);
                MessageTransportWrapper.this.mAppCallback.onMessageSent(str);
            } catch (RemoteException unused) {
                MessageTransportWrapper.this.logw("Error sending onMessageSent to SipDelegateConnection, remote notavailable for transaction ID: " + str);
            }
        }

        public void onMessageSendFailure(final String str, final int i) {
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                MessageTransportWrapper.this.mExecutor.execute(new Runnable() { // from class: com.android.services.telephony.rcs.MessageTransportWrapper$2$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onMessageSendFailure$2(str, i);
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMessageSendFailure$2(String str, int i) {
            if (MessageTransportWrapper.this.mSipDelegate == null) {
                MessageTransportWrapper.this.logw("Unexpected state, onMessageSendFailure called when SipDelegate is notassociated");
            }
            try {
                MessageTransportWrapper.this.mSipSessionTracker.notifyPendingMessageFailed(str);
                MessageTransportWrapper.this.mAppCallback.onMessageSendFailure(str, i);
            } catch (RemoteException unused) {
                MessageTransportWrapper.this.logw("Error sending onMessageSendFailure to SipDelegateConnection, remote not available for transaction ID: " + str);
            }
        }
    }

    public MessageTransportWrapper(int i, ScheduledExecutorService scheduledExecutorService, ISipDelegateMessageCallback iSipDelegateMessageCallback) {
        this.mSipDelegateConnection = new AnonymousClass1();
        this.mDelegateConnectionMessageCallback = new AnonymousClass2();
        this.mLocalLog = new LocalLog(50);
        this.mSubId = i;
        this.mAppCallback = iSipDelegateMessageCallback;
        this.mExecutor = scheduledExecutorService;
        this.mSipSessionTracker = new TransportSipMessageValidator(i, scheduledExecutorService);
        this.mValidatorOverride = new ValidatorOverride() { // from class: com.android.services.telephony.rcs.MessageTransportWrapper$$ExternalSyntheticLambda1
            @Override // com.android.services.telephony.rcs.MessageTransportWrapper.ValidatorOverride
            public final Boolean getValidatorOverrideState() {
                return this.f$0.lambda$new$0();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$new$0() {
        return RcsProvisioningMonitor.getInstance().getImsFeatureValidationOverride(this.mSubId);
    }

    public MessageTransportWrapper(int i, ScheduledExecutorService scheduledExecutorService, ISipDelegateMessageCallback iSipDelegateMessageCallback, TransportSipMessageValidator transportSipMessageValidator) {
        this.mSipDelegateConnection = new AnonymousClass1();
        this.mDelegateConnectionMessageCallback = new AnonymousClass2();
        this.mLocalLog = new LocalLog(50);
        this.mSubId = i;
        this.mAppCallback = iSipDelegateMessageCallback;
        this.mExecutor = scheduledExecutorService;
        this.mSipSessionTracker = transportSipMessageValidator;
        this.mValidatorOverride = new ValidatorOverride() { // from class: com.android.services.telephony.rcs.MessageTransportWrapper$$ExternalSyntheticLambda0
            @Override // com.android.services.telephony.rcs.MessageTransportWrapper.ValidatorOverride
            public final Boolean getValidatorOverrideState() {
                return MessageTransportWrapper.$r8$lambda$tBB47MeuvX7uHsocwBXzprA9Qyg();
            }
        };
    }

    public static /* synthetic */ Boolean $r8$lambda$tBB47MeuvX7uHsocwBXzprA9Qyg() {
        return null;
    }

    @Override // com.android.services.telephony.rcs.DelegateBinderStateManager.StateCallback
    public void onRegistrationStateChanged(DelegateRegistrationState delegateRegistrationState) {
        this.mSipSessionTracker.onRegistrationStateChanged(new Consumer() { // from class: com.android.services.telephony.rcs.MessageTransportWrapper$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$onRegistrationStateChanged$2((Set) obj);
            }
        }, delegateRegistrationState);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRegistrationStateChanged$2(Set set) {
        Iterator it = set.iterator();
        while (it.hasNext()) {
            cleanupSessionInternal((String) it.next());
        }
    }

    @Override // com.android.services.telephony.rcs.DelegateBinderStateManager.StateCallback
    public void onImsConfigurationChanged(SipDelegateImsConfiguration sipDelegateImsConfiguration) {
        this.mSipSessionTracker.onImsConfigurationChanged(sipDelegateImsConfiguration);
    }

    @Override // com.android.services.telephony.rcs.DelegateBinderStateManager.StateCallback
    public void onConfigurationChanged(SipDelegateConfiguration sipDelegateConfiguration) {
        this.mSipSessionTracker.onConfigurationChanged(sipDelegateConfiguration);
    }

    public void openTransport(ISipDelegate iSipDelegate, Set set, Set set2) {
        logi("openTransport: delegate=" + iSipDelegate + ", supportedTags=" + set + ", deniedTags=" + set2);
        this.mSipSessionTracker.onTransportOpened(set, set2);
        this.mSipDelegate = iSipDelegate;
    }

    public void dump(PrintWriter printWriter) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("Most recent logs:");
        this.mLocalLog.dump(printWriter);
        indentingPrintWriter.println();
        indentingPrintWriter.println("Dialog Tracker:");
        indentingPrintWriter.increaseIndent();
        this.mSipSessionTracker.dump(indentingPrintWriter);
        indentingPrintWriter.decreaseIndent();
    }

    public ISipDelegate getDelegateConnection() {
        return this.mSipDelegateConnection;
    }

    public ISipDelegateMessageCallback getAppMessageCallback() {
        return this.mAppCallback;
    }

    public ISipDelegateMessageCallback getMessageCallback() {
        return this.mDelegateConnectionMessageCallback;
    }

    public void closeGracefully(int i, int i2, final Consumer consumer) {
        logi("closeGracefully: closingReason=" + i + ", closedReason=" + i2 + ", resultConsumer(" + consumer.hashCode() + ")");
        this.mSipSessionTracker.closeSessionsGracefully(new Consumer() { // from class: com.android.services.telephony.rcs.MessageTransportWrapper$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$closeGracefully$3(consumer, (Set) obj);
            }
        }, i, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$closeGracefully$3(Consumer consumer, Set set) {
        logi("closeGracefully resultConsumer(" + consumer.hashCode() + "): open call IDs:{" + set + "}");
        closeTransport(set);
        consumer.accept(Boolean.valueOf(set.isEmpty()));
    }

    public void close(int i) {
        Set setCloseSessions = this.mSipSessionTracker.closeSessions(i);
        logi("close: closedReason=" + i + "open call IDs:{" + setCloseSessions + "}");
        closeTransport(setCloseSessions);
    }

    private void closeTransport(Set set) {
        Iterator it = set.iterator();
        while (it.hasNext()) {
            cleanupSessionInternal((String) it.next());
        }
        this.mSipDelegate = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanupSessionInternal(String str) {
        logi("cleanupSessionInternal: clean up session with callId: " + str);
        try {
            ISipDelegate iSipDelegate = this.mSipDelegate;
            if (iSipDelegate == null) {
                logw("cleanupSessionInternal: SipDelegate is not associated, callId: " + str);
            } else {
                iSipDelegate.cleanupSession(str);
            }
        } catch (RemoteException unused) {
            logw("cleanupSessionInternal: remote not available when cleanupSession was called for call id: " + str);
        }
        this.mSipSessionTracker.onSipSessionCleanup(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ValidationResult maybeOverrideValidationForTesting(ValidationResult validationResult) {
        Boolean validatorOverrideState = this.mValidatorOverride.getValidatorOverrideState();
        if (validatorOverrideState == null) {
            return validationResult;
        }
        if (validatorOverrideState.booleanValue()) {
            return ValidationResult.SUCCESS;
        }
        return validationResult.isValidated ? new ValidationResult(0, "validation failed due to a testing override being set") : validationResult;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDelegateSendError(String str, SipMessage sipMessage, int i) {
        String transactionId = SipMessageParsingUtils.getTransactionId(sipMessage.getHeaderSection());
        logi("Error sending SipMessage[id: " + transactionId + ", code: " + i + "] -> SipDelegate for reason: " + str);
        try {
            this.mAppCallback.onMessageSendFailure(transactionId, i);
        } catch (RemoteException e) {
            logw("notifyDelegateSendError, SipDelegate is not available: " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyAppReceiveError(String str, SipMessage sipMessage, int i) {
        String transactionId = SipMessageParsingUtils.getTransactionId(sipMessage.getHeaderSection());
        logi("Error sending SipMessage[id: " + transactionId + ", code: " + i + "] -> SipDelegateConnection for reason: " + str);
        try {
            this.mSipDelegate.notifyMessageReceiveError(transactionId, i);
        } catch (RemoteException e) {
            logw("notifyAppReceiveError, SipDelegate is not available: " + e);
        }
    }

    public void setSipDialogsListener(SipDialogsStateListener sipDialogsStateListener, boolean z) {
        this.mSipSessionTracker.setSipDialogsListener(sipDialogsStateListener, z);
    }

    private void logi(String str) {
        Log.i("SipTransportC", "MessageTW[" + this.mSubId + "] " + str);
        LocalLog localLog = this.mLocalLog;
        StringBuilder sb = new StringBuilder("[I] ");
        sb.append(str);
        localLog.log(sb.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logw(String str) {
        Log.w("SipTransportC", "MessageTW[" + this.mSubId + "] " + str);
        LocalLog localLog = this.mLocalLog;
        StringBuilder sb = new StringBuilder("[W] ");
        sb.append(str);
        localLog.log(sb.toString());
    }
}
