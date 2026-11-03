package com.android.services.telephony.rcs;

import android.telephony.ims.SipDialogState;
import android.telephony.ims.SipMessage;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.telephony.IndentingPrintWriter;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.SipMessageParsingUtils;
import com.android.internal.telephony.metrics.RcsStats;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class SipSessionTracker {
    public static final String[] SIP_REQUEST_DIALOG_START_METHODS = {"invite"};
    private final RcsStats mRcsStats;
    private SipDialogsStateListener mSipDialogsListener;
    int mSubId;
    private final LocalLog mLocalLog = new LocalLog(50);
    private final ArrayList mTrackedDialogs = new ArrayList();
    private final ArrayMap mPendingAck = new ArrayMap();
    private String mDelegateKey = String.valueOf(UUID.randomUUID());

    public SipSessionTracker(int i, RcsStats rcsStats) {
        this.mSubId = i;
        this.mRcsStats = rcsStats;
    }

    public void filterSipMessage(int i, SipMessage sipMessage) {
        final Runnable dialogStateChangeRunnable;
        if (startsEarlyDialog(sipMessage)) {
            dialogStateChangeRunnable = getCreateDialogRunnable(i, sipMessage);
        } else if (closesDialog(sipMessage)) {
            dialogStateChangeRunnable = getCloseDialogRunnable(sipMessage);
        } else {
            dialogStateChangeRunnable = SipMessageParsingUtils.isSipResponse(sipMessage.getStartLine()) ? getDialogStateChangeRunnable(sipMessage) : null;
        }
        if (dialogStateChangeRunnable != null) {
            boolean zContainsKey = this.mPendingAck.containsKey(sipMessage.getViaBranchParameter());
            ArrayMap arrayMap = this.mPendingAck;
            if (zContainsKey) {
                final Runnable runnable = (Runnable) arrayMap.get(sipMessage.getViaBranchParameter());
                logw("Adding new message when there was already a pending event for branch: " + sipMessage.getViaBranchParameter());
                this.mPendingAck.put(sipMessage.getViaBranchParameter(), new Runnable() { // from class: com.android.services.telephony.rcs.SipSessionTracker$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        SipSessionTracker.$r8$lambda$DwxKONfm4EX35I11GU8XP7IkFqk(runnable, dialogStateChangeRunnable);
                    }
                });
                return;
            }
            arrayMap.put(sipMessage.getViaBranchParameter(), dialogStateChangeRunnable);
        }
    }

    public static /* synthetic */ void $r8$lambda$DwxKONfm4EX35I11GU8XP7IkFqk(Runnable runnable, Runnable runnable2) {
        if (runnable != null) {
            runnable.run();
        }
        runnable2.run();
    }

    public void acknowledgePendingMessage(String str) {
        Runnable runnable = (Runnable) this.mPendingAck.get(str);
        if (runnable != null) {
            this.mPendingAck.remove(str);
            runnable.run();
        }
    }

    public void pendingMessageFailed(String str) {
        this.mPendingAck.remove(str);
    }

    public void cleanupSession(final String str) {
        List<SipDialog> list = (List) this.mTrackedDialogs.stream().filter(new Predicate() { // from class: com.android.services.telephony.rcs.SipSessionTracker$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((SipDialog) obj).getCallId().equals(str);
            }
        }).collect(Collectors.toList());
        if (list.isEmpty()) {
            return;
        }
        logi("Cleanup dialogs associated with call id: " + str);
        for (SipDialog sipDialog : list) {
            this.mRcsStats.onSipTransportSessionClosed(this.mSubId, str, 0, sipDialog.getState() == 2);
            sipDialog.close();
            logi("Dialog closed: " + sipDialog);
        }
        this.mTrackedDialogs.removeAll(list);
        notifySipDialogState();
    }

    public Set getCallIdsAssociatedWithFeatureTag(Set set) {
        if (set.isEmpty()) {
            return Collections.EMPTY_SET;
        }
        ArraySet arraySet = new ArraySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            final String str = (String) it.next();
            ArrayList arrayList = this.mTrackedDialogs;
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                SipDialog sipDialog = (SipDialog) obj;
                if (sipDialog.getAcceptContactFeatureTags().stream().anyMatch(new Predicate() { // from class: com.android.services.telephony.rcs.SipSessionTracker$$ExternalSyntheticLambda2
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj2) {
                        return ((String) obj2).equalsIgnoreCase(str);
                    }
                })) {
                    arraySet.add(sipDialog.getCallId());
                }
            }
        }
        return arraySet;
    }

    /* JADX INFO: renamed from: $r8$lambda$aKMMo0ydYCcVaiYykCyFwy-MCVM, reason: not valid java name */
    public static /* synthetic */ boolean m4476$r8$lambda$aKMMo0ydYCcVaiYykCyFwyMCVM(SipDialog sipDialog) {
        return sipDialog.getState() == 0;
    }

    public Set getEarlyDialogs() {
        return (Set) this.mTrackedDialogs.stream().filter(new Predicate() { // from class: com.android.services.telephony.rcs.SipSessionTracker$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return SipSessionTracker.m4476$r8$lambda$aKMMo0ydYCcVaiYykCyFwyMCVM((SipDialog) obj);
            }
        }).collect(Collectors.toSet());
    }

    public static /* synthetic */ boolean $r8$lambda$r2hvnTHK_0JAO5LpZ78N611MUM0(SipDialog sipDialog) {
        return sipDialog.getState() == 1;
    }

    public Set getConfirmedDialogs() {
        return (Set) this.mTrackedDialogs.stream().filter(new Predicate() { // from class: com.android.services.telephony.rcs.SipSessionTracker$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return SipSessionTracker.$r8$lambda$r2hvnTHK_0JAO5LpZ78N611MUM0((SipDialog) obj);
            }
        }).collect(Collectors.toSet());
    }

    public static /* synthetic */ boolean $r8$lambda$pW69N7NoR1TJk0YL32JNj5zKUlI(SipDialog sipDialog) {
        return sipDialog.getState() == 2;
    }

    public Set getClosedDialogs() {
        return (Set) this.mTrackedDialogs.stream().filter(new Predicate() { // from class: com.android.services.telephony.rcs.SipSessionTracker$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return SipSessionTracker.$r8$lambda$pW69N7NoR1TJk0YL32JNj5zKUlI((SipDialog) obj);
            }
        }).collect(Collectors.toSet());
    }

    public Set getTrackedDialogs() {
        return new ArraySet(this.mTrackedDialogs);
    }

    public void clearAllSessions() {
        ArrayList arrayList = this.mTrackedDialogs;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            this.mRcsStats.onSipTransportSessionClosed(this.mSubId, ((SipDialog) obj).getCallId(), 0, false);
        }
        this.mTrackedDialogs.clear();
        this.mPendingAck.clear();
        notifySipDialogState();
    }

    public void dump(PrintWriter printWriter) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("SipSessionTracker:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.print("Early Call IDs: ");
        indentingPrintWriter.println(getEarlyDialogs().stream().map(new SipSessionTracker$$ExternalSyntheticLambda0()).collect(Collectors.toSet()));
        indentingPrintWriter.print("Confirmed Call IDs: ");
        indentingPrintWriter.println(getConfirmedDialogs().stream().map(new SipSessionTracker$$ExternalSyntheticLambda0()).collect(Collectors.toSet()));
        indentingPrintWriter.print("Closed Call IDs: ");
        indentingPrintWriter.println(getClosedDialogs().stream().map(new SipSessionTracker$$ExternalSyntheticLambda0()).collect(Collectors.toSet()));
        indentingPrintWriter.println("Tracked Dialogs:");
        indentingPrintWriter.increaseIndent();
        ArrayList arrayList = this.mTrackedDialogs;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            indentingPrintWriter.println((SipDialog) obj);
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println();
        indentingPrintWriter.println("Local Logs");
        this.mLocalLog.dump(indentingPrintWriter);
        indentingPrintWriter.decreaseIndent();
    }

    private boolean startsEarlyDialog(SipMessage sipMessage) {
        final String[] strArrSplitStartLineAndVerify;
        if (SipMessageParsingUtils.isSipRequest(sipMessage.getStartLine()) && (strArrSplitStartLineAndVerify = SipMessageParsingUtils.splitStartLineAndVerify(sipMessage.getStartLine())) != null) {
            return Arrays.stream(SIP_REQUEST_DIALOG_START_METHODS).anyMatch(new Predicate() { // from class: com.android.services.telephony.rcs.SipSessionTracker$$ExternalSyntheticLambda7
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return ((String) obj).equalsIgnoreCase(strArrSplitStartLineAndVerify[0]);
                }
            });
        }
        return false;
    }

    private boolean closesDialog(SipMessage sipMessage) {
        String[] strArrSplitStartLineAndVerify;
        if (SipMessageParsingUtils.isSipRequest(sipMessage.getStartLine()) && (strArrSplitStartLineAndVerify = SipMessageParsingUtils.splitStartLineAndVerify(sipMessage.getStartLine())) != null) {
            return "bye".equalsIgnoreCase(strArrSplitStartLineAndVerify[0]);
        }
        return false;
    }

    private Runnable getCreateDialogRunnable(final int i, final SipMessage sipMessage) {
        return new Runnable() { // from class: com.android.services.telephony.rcs.SipSessionTracker$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$getCreateDialogRunnable$8(sipMessage, i);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getCreateDialogRunnable$8(final SipMessage sipMessage, int i) {
        List list = (List) this.mTrackedDialogs.stream().filter(new Predicate() { // from class: com.android.services.telephony.rcs.SipSessionTracker$$ExternalSyntheticLambda11
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((SipDialog) obj).getCallId().equals(sipMessage.getCallIdParameter());
            }
        }).collect(Collectors.toList());
        if (list.size() > 0) {
            logi("trying to create a dialog for a call ID that already exists, skip: " + list);
            return;
        }
        SipDialog sipDialogFromSipMessage = SipDialog.fromSipMessage(sipMessage);
        this.mRcsStats.earlySipTransportSession(SipMessageParsingUtils.splitStartLineAndVerify(sipMessage.getStartLine())[0], sipDialogFromSipMessage.getCallId(), i);
        logi("Starting new SipDialog: " + sipDialogFromSipMessage);
        this.mTrackedDialogs.add(sipDialogFromSipMessage);
    }

    private Runnable getCloseDialogRunnable(final SipMessage sipMessage) {
        return new Runnable() { // from class: com.android.services.telephony.rcs.SipSessionTracker$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$getCloseDialogRunnable$10(sipMessage);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getCloseDialogRunnable$10(final SipMessage sipMessage) {
        List<SipDialog> list = (List) this.mTrackedDialogs.stream().filter(new Predicate() { // from class: com.android.services.telephony.rcs.SipSessionTracker$$ExternalSyntheticLambda14
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((SipDialog) obj).isRequestAssociatedWithDialog(sipMessage);
            }
        }).collect(Collectors.toList());
        if (list.isEmpty()) {
            return;
        }
        logi("Closing dialogs associated with: " + sipMessage);
        this.mRcsStats.onSipTransportSessionClosed(this.mSubId, sipMessage.getCallIdParameter(), 0, true);
        for (SipDialog sipDialog : list) {
            sipDialog.close();
            logi("Dialog closed: " + sipDialog);
        }
        notifySipDialogState();
    }

    private Runnable getDialogStateChangeRunnable(final SipMessage sipMessage) {
        return new Runnable() { // from class: com.android.services.telephony.rcs.SipSessionTracker$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$getDialogStateChangeRunnable$13(sipMessage);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getDialogStateChangeRunnable$13(final SipMessage sipMessage) {
        List list = (List) this.mTrackedDialogs.stream().filter(new Predicate() { // from class: com.android.services.telephony.rcs.SipSessionTracker$$ExternalSyntheticLambda12
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((SipDialog) obj).isResponseAssociatedWithDialog(sipMessage);
            }
        }).collect(Collectors.toList());
        if (list.isEmpty()) {
            return;
        }
        final String toTag = SipMessageParsingUtils.getToTag(sipMessage.getHeaderSection());
        SipDialog sipDialogForkDialog = (SipDialog) list.stream().filter(new Predicate() { // from class: com.android.services.telephony.rcs.SipSessionTracker$$ExternalSyntheticLambda13
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return SipSessionTracker.$r8$lambda$lalNMjZYLmFBqzkvubIZfrkXe9M(toTag, (SipDialog) obj);
            }
        }).findFirst().orElse(null);
        if (sipDialogForkDialog == null) {
            logi("Dialog forked");
            sipDialogForkDialog = ((SipDialog) list.get(0)).forkDialog();
            this.mTrackedDialogs.add(sipDialogForkDialog);
        }
        if (sipDialogForkDialog != null) {
            logi("Dialog: " + sipDialogForkDialog + " is associated with: " + sipMessage);
            updateSipDialogState(sipDialogForkDialog, sipMessage);
            StringBuilder sb = new StringBuilder("Dialog state updated to ");
            sb.append(sipDialogForkDialog);
            logi(sb.toString());
            return;
        }
        logi("No Dialogs are associated with: " + sipMessage);
    }

    public static /* synthetic */ boolean $r8$lambda$lalNMjZYLmFBqzkvubIZfrkXe9M(String str, SipDialog sipDialog) {
        return sipDialog.getToTag() == null || sipDialog.getToTag().equals(str);
    }

    private void updateSipDialogState(SipDialog sipDialog, SipMessage sipMessage) {
        String[] strArrSplitStartLineAndVerify = SipMessageParsingUtils.splitStartLineAndVerify(sipMessage.getStartLine());
        if (strArrSplitStartLineAndVerify == null) {
            logw("Could not parse start line for SIP message: " + sipMessage.getStartLine());
            return;
        }
        try {
            int i = Integer.parseInt(strArrSplitStartLineAndVerify[1]);
            String toTag = SipMessageParsingUtils.getToTag(sipMessage.getHeaderSection());
            logi("updateSipDialogState: message has statusCode: " + i + ", and to tag: " + toTag);
            if (i <= 100) {
                return;
            }
            if (i >= 300) {
                this.mRcsStats.onSipTransportSessionClosed(this.mSubId, sipMessage.getCallIdParameter(), i, true);
                sipDialog.close();
                notifySipDialogState();
                return;
            }
            if (toTag == null) {
                logw("updateSipDialogState: No to tag for message: " + sipMessage);
            }
            if (i >= 200) {
                this.mRcsStats.confirmedSipTransportSession(sipMessage.getCallIdParameter(), i);
                sipDialog.confirm(toTag);
                notifySipDialogState();
            } else {
                sipDialog.earlyResponse(toTag);
                notifySipDialogState();
            }
        } catch (NumberFormatException unused) {
            logw("Could not parse status code for SIP message: " + sipMessage.getStartLine());
        }
    }

    public void setSipDialogsListener(SipDialogsStateListener sipDialogsStateListener, boolean z) {
        this.mSipDialogsListener = sipDialogsStateListener;
        if (sipDialogsStateListener != null && z) {
            notifySipDialogState();
        }
    }

    private void notifySipDialogState() {
        if (this.mSipDialogsListener == null) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = this.mTrackedDialogs;
        int size = arrayList2.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList2.get(i);
            i++;
            arrayList.add(new SipDialogState.Builder(((SipDialog) obj).getState()).build());
        }
        this.mSipDialogsListener.reMappingSipDelegateState(this.mDelegateKey, arrayList);
    }

    private void logi(String str) {
        Log.i("SipTransportC", "SessionT: " + str);
        this.mLocalLog.log("[I] " + str);
    }

    private void logw(String str) {
        Log.w("SipTransportC", "SessionT: " + str);
        this.mLocalLog.log("[W] " + str);
    }
}
