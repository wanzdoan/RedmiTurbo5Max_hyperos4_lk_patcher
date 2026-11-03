package com.android.services.telephony.rcs;

import android.telephony.ims.DelegateRegistrationState;
import android.telephony.ims.SipDelegateConfiguration;
import android.telephony.ims.SipDelegateImsConfiguration;
import android.telephony.ims.SipMessage;
import android.util.Log;
import com.android.internal.telephony.IndentingPrintWriter;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.SipMessageParsingUtils;
import com.android.internal.telephony.metrics.RcsStats;
import com.android.services.telephony.rcs.validator.IncomingTransportStateValidator;
import com.android.services.telephony.rcs.validator.MalformedSipMessageValidator;
import com.android.services.telephony.rcs.validator.OutgoingTransportStateValidator;
import com.android.services.telephony.rcs.validator.RestrictedOutgoingSipRequestValidator;
import com.android.services.telephony.rcs.validator.RestrictedOutgoingSubscribeValidator;
import com.android.services.telephony.rcs.validator.SipMessageValidator;
import com.android.services.telephony.rcs.validator.ValidationResult;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/* JADX INFO: loaded from: classes.dex */
public class TransportSipMessageValidator {
    private Consumer mClosingCompleteConsumer;
    private Set mDeniedFeatureTags;
    private final ScheduledExecutorService mExecutor;
    private final SipMessageValidator mIncomingMessageValidator;
    private final IncomingTransportStateValidator mIncomingTransportStateValidator;
    private final SipMessageValidator mOutgoingMessageValidator;
    private final OutgoingTransportStateValidator mOutgoingTransportStateValidator;
    private PendingTask mPendingClose;
    private PendingRegCleanupTask mPendingRegCleanup;
    private final RcsStats mRcsStats;
    private Consumer mRegistrationAppliedConsumer;
    private final SipSessionTracker mSipSessionTracker;
    private final int mSubId;
    private Set mSupportedFeatureTags;
    private final LocalLog mLocalLog = new LocalLog(50);
    private long mConfigVersion = -1;

    public interface Timeouts {
    }

    abstract class PendingTask implements Runnable {
        private ScheduledFuture mFuture;

        private PendingTask() {
        }

        public void scheduleDelayed(ScheduledExecutorService scheduledExecutorService, int i) {
            this.mFuture = scheduledExecutorService.schedule(this, i, TimeUnit.MILLISECONDS);
        }

        public boolean isDone() {
            ScheduledFuture scheduledFuture = this.mFuture;
            return scheduledFuture != null && scheduledFuture.isDone();
        }

        public void cancel() {
            ScheduledFuture scheduledFuture = this.mFuture;
            if (scheduledFuture == null) {
                return;
            }
            scheduledFuture.cancel(false);
        }
    }

    abstract class PendingRegCleanupTask extends PendingTask {
        public final Set featureTags;
        public final Set pendingCallIds;

        PendingRegCleanupTask(Set set, Set set2) {
            super();
            this.featureTags = set;
            this.pendingCallIds = set2;
        }
    }

    public TransportSipMessageValidator(int i, ScheduledExecutorService scheduledExecutorService) {
        this.mSubId = i;
        this.mExecutor = scheduledExecutorService;
        RcsStats rcsStats = RcsStats.getInstance();
        this.mRcsStats = rcsStats;
        SipSessionTracker sipSessionTracker = new SipSessionTracker(i, rcsStats);
        this.mSipSessionTracker = sipSessionTracker;
        OutgoingTransportStateValidator outgoingTransportStateValidator = new OutgoingTransportStateValidator(sipSessionTracker);
        this.mOutgoingTransportStateValidator = outgoingTransportStateValidator;
        IncomingTransportStateValidator incomingTransportStateValidator = new IncomingTransportStateValidator();
        this.mIncomingTransportStateValidator = incomingTransportStateValidator;
        this.mOutgoingMessageValidator = new MalformedSipMessageValidator().andThen(new RestrictedOutgoingSipRequestValidator()).andThen(new RestrictedOutgoingSubscribeValidator()).andThen(outgoingTransportStateValidator);
        this.mIncomingMessageValidator = incomingTransportStateValidator;
    }

    public TransportSipMessageValidator(int i, ScheduledExecutorService scheduledExecutorService, SipSessionTracker sipSessionTracker, OutgoingTransportStateValidator outgoingTransportStateValidator, IncomingTransportStateValidator incomingTransportStateValidator, RcsStats rcsStats) {
        this.mSubId = i;
        this.mExecutor = scheduledExecutorService;
        this.mSipSessionTracker = sipSessionTracker;
        this.mOutgoingTransportStateValidator = outgoingTransportStateValidator;
        this.mIncomingTransportStateValidator = incomingTransportStateValidator;
        this.mOutgoingMessageValidator = outgoingTransportStateValidator;
        this.mIncomingMessageValidator = incomingTransportStateValidator;
        this.mRcsStats = rcsStats;
    }

    public void onRegistrationStateChanged(Consumer consumer, DelegateRegistrationState delegateRegistrationState) {
        if (this.mRegistrationAppliedConsumer != null) {
            logw("onRegistrationStateChanged: pending registration change, completing now.");
            cleanupAndNotifyRegistrationAppliedConsumer(Collections.EMPTY_SET);
        }
        Set set = (Set) Stream.concat(delegateRegistrationState.getDeregisteringFeatureTags().stream(), delegateRegistrationState.getDeregisteredFeatureTags().stream()).map(new SipDelegateController$$ExternalSyntheticLambda7()).collect(Collectors.toSet());
        this.mOutgoingTransportStateValidator.restrictFeatureTags(set);
        this.mRegistrationAppliedConsumer = consumer;
        PendingTask pendingTask = this.mPendingClose;
        if (pendingTask == null || pendingTask.isDone()) {
            updatePendingRegCleanupTask(set);
        } else {
            logi("skipping update reg cleanup due to pending close task.");
        }
    }

    public void onImsConfigurationChanged(SipDelegateImsConfiguration sipDelegateImsConfiguration) {
        if (sipDelegateImsConfiguration.getVersion() == this.mConfigVersion) {
            return;
        }
        logi("onImsConfigurationChanged: " + this.mConfigVersion + "->" + sipDelegateImsConfiguration.getVersion());
        this.mConfigVersion = sipDelegateImsConfiguration.getVersion();
    }

    public void onConfigurationChanged(SipDelegateConfiguration sipDelegateConfiguration) {
        if (sipDelegateConfiguration.getVersion() == this.mConfigVersion) {
            return;
        }
        logi("onConfigurationChanged: " + this.mConfigVersion + "->" + sipDelegateConfiguration.getVersion());
        this.mConfigVersion = sipDelegateConfiguration.getVersion();
    }

    public void onTransportOpened(Set set, Set set2) {
        logi("onTransportOpened: moving to open state");
        this.mSupportedFeatureTags = set;
        this.mDeniedFeatureTags = set2;
        this.mOutgoingTransportStateValidator.open(set, (Set) set2.stream().map(new SipDelegateController$$ExternalSyntheticLambda7()).collect(Collectors.toSet()));
        this.mIncomingTransportStateValidator.open();
    }

    public void onSipSessionCleanup(String str) {
        this.mSipSessionTracker.cleanupSession(str);
        onCallIdsChanged();
    }

    public void closeSessionsGracefully(final Consumer consumer, final int i, int i2) {
        if (consumer == null) {
            logw("closeSessionsGracefully: unexpected - called with null consumer... closing now");
            closeSessions(i2);
            return;
        }
        if (this.mClosingCompleteConsumer != null) {
            logw("closeSessionsGracefully: unexpected - existing close pending, combining consumers.");
            this.mClosingCompleteConsumer = new Consumer() { // from class: com.android.services.telephony.rcs.TransportSipMessageValidator$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$closeSessionsGracefully$0(consumer, (Set) obj);
                }
            };
            return;
        }
        this.mClosingCompleteConsumer = consumer;
        if (getTrackedSipSessionCallIds().isEmpty()) {
            logi("closeSessionsGracefully: moving to closed state now, reason=" + i2);
            closeSessionsInternal(i2);
            cancelClosingTimeoutAndSendComplete(Collections.EMPTY_SET);
            return;
        }
        cancelPendingRegCleanupTask();
        logi("closeSessionsGracefully: moving to restricted state, reason=" + i);
        this.mOutgoingTransportStateValidator.restrict(i);
        PendingTask pendingTask = new PendingTask() { // from class: com.android.services.telephony.rcs.TransportSipMessageValidator.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // java.lang.Runnable
            public void run() {
                TransportSipMessageValidator.this.closeSessions(i);
            }
        };
        this.mPendingClose = pendingTask;
        pendingTask.scheduleDelayed(this.mExecutor, 1000);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$closeSessionsGracefully$0(Consumer consumer, Set set) {
        this.mClosingCompleteConsumer.accept(set);
        consumer.accept(set);
    }

    public Set closeSessions(int i) {
        Set trackedSipSessionCallIds = getTrackedSipSessionCallIds();
        logi("closeSessions: moving to closed state, reason=" + i + ", open call ids: " + trackedSipSessionCallIds);
        closeSessionsInternal(i);
        if (!cancelClosingTimeoutAndSendComplete(trackedSipSessionCallIds)) {
            return trackedSipSessionCallIds;
        }
        logw("closeSessions: call ID closure handled through consumer");
        return Collections.EMPTY_SET;
    }

    public ValidationResult verifyOutgoingMessage(SipMessage sipMessage, long j) {
        if (this.mConfigVersion != j) {
            return new ValidationResult(10, "stale IMS configuration: " + j + ", expected: " + this.mConfigVersion);
        }
        ValidationResult validationResultValidate = this.mOutgoingMessageValidator.validate(sipMessage);
        logi("verifyOutgoingMessage: " + validationResultValidate + ", message=" + sipMessage);
        if (validationResultValidate.isValidated) {
            this.mSipSessionTracker.filterSipMessage(2, sipMessage);
        }
        updateForMetrics(2, sipMessage, validationResultValidate);
        return validationResultValidate;
    }

    public ValidationResult verifyIncomingMessage(SipMessage sipMessage) {
        ValidationResult validationResultValidate = this.mIncomingMessageValidator.validate(sipMessage);
        logi("verifyIncomingMessage: " + validationResultValidate + ", message=" + sipMessage);
        if (validationResultValidate.isValidated) {
            this.mSipSessionTracker.filterSipMessage(1, sipMessage);
        }
        updateForMetrics(1, sipMessage, validationResultValidate);
        return validationResultValidate;
    }

    public void acknowledgePendingMessage(String str) {
        logi("acknowledgePendingMessage: id=" + str);
        this.mSipSessionTracker.acknowledgePendingMessage(str);
        onCallIdsChanged();
    }

    public void notifyPendingMessageFailed(String str) {
        logi("notifyPendingMessageFailed: id=" + str);
        this.mSipSessionTracker.pendingMessageFailed(str);
    }

    public void dump(PrintWriter printWriter) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("Supported Tags:" + this.mSupportedFeatureTags);
        indentingPrintWriter.println("Denied Tags:" + this.mDeniedFeatureTags);
        indentingPrintWriter.println(this.mOutgoingTransportStateValidator);
        indentingPrintWriter.println(this.mIncomingTransportStateValidator);
        StringBuilder sb = new StringBuilder("Reg consumer pending: ");
        sb.append(this.mRegistrationAppliedConsumer != null);
        indentingPrintWriter.println(sb.toString());
        StringBuilder sb2 = new StringBuilder("Close consumer pending: ");
        sb2.append(this.mClosingCompleteConsumer != null);
        indentingPrintWriter.println(sb2.toString());
        indentingPrintWriter.println();
        this.mSipSessionTracker.dump(indentingPrintWriter);
        indentingPrintWriter.println();
        indentingPrintWriter.println("Most recent logs:");
        this.mLocalLog.dump(printWriter);
    }

    private void onCallIdsChanged() {
        PendingTask pendingTask;
        if (getTrackedSipSessionCallIds().isEmpty() && (pendingTask = this.mPendingClose) != null && !pendingTask.isDone()) {
            logi("onCallIdsChanged: no open sessions, completing any pending close events.");
            this.mPendingClose.cancel();
            this.mPendingClose.run();
        }
        PendingRegCleanupTask pendingRegCleanupTask = this.mPendingRegCleanup;
        if (pendingRegCleanupTask == null || pendingRegCleanupTask.isDone()) {
            return;
        }
        logi("onCallIdsChanged: updating pending reg cleanup task.");
        updatePendingRegCleanupTask(this.mPendingRegCleanup.featureTags);
    }

    private void cancelPendingRegCleanupTask() {
        PendingRegCleanupTask pendingRegCleanupTask = this.mPendingRegCleanup;
        if (pendingRegCleanupTask != null && !pendingRegCleanupTask.isDone()) {
            logi("cancelPendingRegCleanupTask: cancelling...");
            this.mPendingRegCleanup.cancel();
        }
        cleanupAndNotifyRegistrationAppliedConsumer(Collections.EMPTY_SET);
    }

    private void updatePendingRegCleanupTask(Set set) {
        Set callIdsAssociatedWithFeatureTag = this.mSipSessionTracker.getCallIdsAssociatedWithFeatureTag(set);
        boolean zIsEmpty = callIdsAssociatedWithFeatureTag.isEmpty();
        PendingRegCleanupTask pendingRegCleanupTask = this.mPendingRegCleanup;
        if (zIsEmpty) {
            if (pendingRegCleanupTask != null && !pendingRegCleanupTask.isDone()) {
                logi("updatePendingRegCleanupTask: no remaining call ids, finish cleanup task now.");
                this.mPendingRegCleanup.cancel();
                this.mPendingRegCleanup.run();
                return;
            } else {
                if (this.mRegistrationAppliedConsumer != null) {
                    logi("updatePendingRegCleanupTask: notify no pending call ids.");
                    cleanupAndNotifyRegistrationAppliedConsumer(Collections.EMPTY_SET);
                    return;
                }
                return;
            }
        }
        if (pendingRegCleanupTask != null && !pendingRegCleanupTask.isDone()) {
            if (this.mPendingRegCleanup.pendingCallIds.equals(callIdsAssociatedWithFeatureTag)) {
                logi("updatePendingRegCleanupTask: pending reg change has same set of pending call IDs, so keeping pending task");
                return;
            } else {
                logi("updatePendingRegCleanupTask: cancelling, call ids have changed.");
                this.mPendingRegCleanup.cancel();
            }
        }
        this.mPendingRegCleanup = new PendingRegCleanupTask(set, callIdsAssociatedWithFeatureTag) { // from class: com.android.services.telephony.rcs.TransportSipMessageValidator.2
            @Override // java.lang.Runnable
            public void run() {
                TransportSipMessageValidator.this.cleanupAndNotifyRegistrationAppliedConsumer(this.pendingCallIds);
            }
        };
        logi("updatePendingRegCleanupTask: scheduling for call ids: " + callIdsAssociatedWithFeatureTag);
        this.mPendingRegCleanup.scheduleDelayed(this.mExecutor, 1000);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanupAndNotifyRegistrationAppliedConsumer(Set set) {
        Consumer consumer = this.mRegistrationAppliedConsumer;
        if (consumer != null) {
            consumer.accept(set);
            this.mRegistrationAppliedConsumer = null;
        }
    }

    private boolean cancelClosingTimeoutAndSendComplete(Set set) {
        PendingTask pendingTask = this.mPendingClose;
        if (pendingTask != null && !pendingTask.isDone()) {
            logi("completing pending close consumer");
            this.mPendingClose.cancel();
        }
        Consumer consumer = this.mClosingCompleteConsumer;
        if (consumer == null) {
            return false;
        }
        consumer.accept(set);
        this.mClosingCompleteConsumer = null;
        return true;
    }

    private void closeSessionsInternal(int i) {
        cancelPendingRegCleanupTask();
        this.mOutgoingTransportStateValidator.close(i);
        this.mIncomingTransportStateValidator.close(i);
        this.mSipSessionTracker.clearAllSessions();
    }

    private Set getTrackedSipSessionCallIds() {
        return (Set) this.mSipSessionTracker.getTrackedDialogs().stream().map(new SipSessionTracker$$ExternalSyntheticLambda0()).collect(Collectors.toSet());
    }

    private void updateForMetrics(int i, SipMessage sipMessage, ValidationResult validationResult) {
        String[] strArrSplitStartLineAndVerify = SipMessageParsingUtils.splitStartLineAndVerify(sipMessage.getStartLine());
        if (SipMessageParsingUtils.isSipRequest(sipMessage.getStartLine())) {
            boolean z = validationResult.isValidated;
            RcsStats rcsStats = this.mRcsStats;
            if (z) {
                rcsStats.onSipMessageRequest(sipMessage.getCallIdParameter(), strArrSplitStartLineAndVerify[0], i);
                return;
            } else {
                rcsStats.invalidatedMessageResult(sipMessage.getCallIdParameter(), this.mSubId, strArrSplitStartLineAndVerify[0], i, validationResult.restrictedReason);
                return;
            }
        }
        if (SipMessageParsingUtils.isSipResponse(sipMessage.getStartLine())) {
            this.mRcsStats.onSipMessageResponse(this.mSubId, sipMessage.getCallIdParameter(), Integer.parseInt(strArrSplitStartLineAndVerify[1]), validationResult.restrictedReason);
        } else {
            logw("Message is Restricted");
        }
    }

    public void setSipDialogsListener(SipDialogsStateListener sipDialogsStateListener, boolean z) {
        this.mSipSessionTracker.setSipDialogsListener(sipDialogsStateListener, z);
    }

    private void logi(String str) {
        Log.i("SipTransportC", "SipMessageV[" + this.mSubId + "] " + str);
        LocalLog localLog = this.mLocalLog;
        StringBuilder sb = new StringBuilder("[I] ");
        sb.append(str);
        localLog.log(sb.toString());
    }

    private void logw(String str) {
        Log.w("SipTransportC", "SipMessageV[" + this.mSubId + "] " + str);
        LocalLog localLog = this.mLocalLog;
        StringBuilder sb = new StringBuilder("[W] ");
        sb.append(str);
        localLog.log(sb.toString());
    }
}
