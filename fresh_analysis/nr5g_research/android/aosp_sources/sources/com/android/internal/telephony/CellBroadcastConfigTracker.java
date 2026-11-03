package com.android.internal.telephony;

import android.os.AsyncResult;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Message;
import android.telephony.CellBroadcastIdRange;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public final class CellBroadcastConfigTracker extends StateMachine {
    private static final boolean DBG = Build.IS_DEBUGGABLE;
    public static final int EVENT_RADIO_RESET = 6;
    private List mCbRanges3gpp;
    private List mCbRanges3gpp2;
    private CdmaActivatingState mCdmaActivatingState;
    private CdmaConfiguringState mCdmaConfiguringState;
    private DefaultState mDefaultState;
    private GsmActivatingState mGsmActivatingState;
    private GsmConfiguringState mGsmConfiguringState;
    private IdleState mIdleState;
    private final LocalLog mLocalLog;
    private Phone mPhone;
    public final SubscriptionManager.OnSubscriptionsChangedListener mSubChangedListener;
    public int mSubId;

    /* JADX INFO: Access modifiers changed from: private */
    static class Request {
        Consumer mCallback;
        private final List mCbRangesRequest3gpp = new CopyOnWriteArrayList();
        private final List mCbRangesRequest3gpp2 = new CopyOnWriteArrayList();

        Request(List list, Consumer consumer) {
            list.forEach(new Consumer() { // from class: com.android.internal.telephony.CellBroadcastConfigTracker$Request$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$new$0((CellBroadcastIdRange) obj);
                }
            });
            this.mCallback = consumer;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(CellBroadcastIdRange cellBroadcastIdRange) {
            if (cellBroadcastIdRange.getType() == 1) {
                this.mCbRangesRequest3gpp.add(cellBroadcastIdRange);
            } else {
                this.mCbRangesRequest3gpp2.add(cellBroadcastIdRange);
            }
        }

        List get3gppRanges() {
            return this.mCbRangesRequest3gpp;
        }

        List get3gpp2Ranges() {
            return this.mCbRangesRequest3gpp2;
        }

        Consumer getCallback() {
            return this.mCallback;
        }

        public String toString() {
            return "Request[mCbRangesRequest3gpp = " + this.mCbRangesRequest3gpp + ", mCbRangesRequest3gpp2 = " + this.mCbRangesRequest3gpp2 + "]";
        }
    }

    private class DefaultState extends State {
        private DefaultState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            CellBroadcastConfigTracker.this.mPhone.registerForRadioOffOrNotAvailable(CellBroadcastConfigTracker.this.getHandler(), 4, null);
            CellBroadcastConfigTracker.this.mPhone.mCi.registerForModemReset(CellBroadcastConfigTracker.this.getHandler(), 6, null);
            ((SubscriptionManager) CellBroadcastConfigTracker.this.mPhone.getContext().getSystemService(SubscriptionManager.class)).addOnSubscriptionsChangedListener(new HandlerExecutor(CellBroadcastConfigTracker.this.getHandler()), CellBroadcastConfigTracker.this.mSubChangedListener);
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void exit() {
            CellBroadcastConfigTracker.this.mPhone.unregisterForRadioOffOrNotAvailable(CellBroadcastConfigTracker.this.getHandler());
            CellBroadcastConfigTracker.this.mPhone.mCi.unregisterForModemReset(CellBroadcastConfigTracker.this.getHandler());
            ((SubscriptionManager) CellBroadcastConfigTracker.this.mPhone.getContext().getSystemService(SubscriptionManager.class)).removeOnSubscriptionsChangedListener(CellBroadcastConfigTracker.this.mSubChangedListener);
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            if (CellBroadcastConfigTracker.DBG) {
                CellBroadcastConfigTracker.this.logd("DefaultState message:" + message.what);
            }
            int i = message.what;
            if (i != 4) {
                if (i == 5) {
                    int subId = CellBroadcastConfigTracker.this.mPhone.getSubId();
                    CellBroadcastConfigTracker cellBroadcastConfigTracker = CellBroadcastConfigTracker.this;
                    if (cellBroadcastConfigTracker.mSubId != subId) {
                        cellBroadcastConfigTracker.log("SubId changed from " + CellBroadcastConfigTracker.this.mSubId + " to " + subId);
                        CellBroadcastConfigTracker cellBroadcastConfigTracker2 = CellBroadcastConfigTracker.this;
                        cellBroadcastConfigTracker2.mSubId = subId;
                        cellBroadcastConfigTracker2.resetConfig();
                    }
                    return true;
                }
                if (i != 6) {
                    CellBroadcastConfigTracker.this.log("unexpected message!");
                    return true;
                }
            }
            CellBroadcastConfigTracker.this.resetConfig();
            return true;
        }
    }

    private class IdleState extends State {
        private IdleState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            if (CellBroadcastConfigTracker.DBG) {
                CellBroadcastConfigTracker.this.logd("IdleState message:" + message.what);
            }
            if (message.what != 1) {
                return false;
            }
            Request request = (Request) message.obj;
            if (CellBroadcastConfigTracker.DBG) {
                CellBroadcastConfigTracker.this.logd("IdleState handle EVENT_REQUEST with request:" + request);
                CellBroadcastConfigTracker.this.mLocalLog.log("IdleState handle EVENT_REQUEST with request:" + request + ", mCbRanges3gpp:" + CellBroadcastConfigTracker.this.mCbRanges3gpp + ", mCbRanges3gpp2:" + CellBroadcastConfigTracker.this.mCbRanges3gpp2);
            }
            boolean zEquals = CellBroadcastConfigTracker.this.mCbRanges3gpp.equals(request.get3gppRanges());
            CellBroadcastConfigTracker cellBroadcastConfigTracker = CellBroadcastConfigTracker.this;
            if (zEquals) {
                cellBroadcastConfigTracker.logd("Do nothing as the requested ranges are same as now");
                request.getCallback().accept(0);
            } else {
                cellBroadcastConfigTracker.setGsmConfig(request.get3gppRanges(), request);
                CellBroadcastConfigTracker cellBroadcastConfigTracker2 = CellBroadcastConfigTracker.this;
                cellBroadcastConfigTracker2.transitionTo(cellBroadcastConfigTracker2.mGsmConfiguringState);
            }
            return true;
        }
    }

    private class GsmConfiguringState extends State {
        private GsmConfiguringState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            if (CellBroadcastConfigTracker.DBG) {
                CellBroadcastConfigTracker.this.logd("GsmConfiguringState message:" + message.what);
            }
            int i = message.what;
            if (i == 1) {
                CellBroadcastConfigTracker.this.deferMessage(message);
                return true;
            }
            if (i != 2) {
                return false;
            }
            AsyncResult asyncResult = (AsyncResult) message.obj;
            Request request = (Request) asyncResult.userObj;
            if (CellBroadcastConfigTracker.DBG) {
                CellBroadcastConfigTracker.this.logd("GsmConfiguringState handle EVENT_CONFIGURATION_DONE with request:" + request);
            }
            Throwable th = asyncResult.exception;
            CellBroadcastConfigTracker cellBroadcastConfigTracker = CellBroadcastConfigTracker.this;
            if (th != null) {
                cellBroadcastConfigTracker.logd("Failed to set gsm config");
                CellBroadcastConfigTracker.this.mLocalLog.log("GsmConfiguringState Failed to set gsm config:" + request);
                request.getCallback().accept(2);
                CellBroadcastConfigTracker cellBroadcastConfigTracker2 = CellBroadcastConfigTracker.this;
                cellBroadcastConfigTracker2.transitionTo(cellBroadcastConfigTracker2.mIdleState);
            } else {
                cellBroadcastConfigTracker.setActivation(1, !request.get3gppRanges().isEmpty(), request);
                CellBroadcastConfigTracker cellBroadcastConfigTracker3 = CellBroadcastConfigTracker.this;
                cellBroadcastConfigTracker3.transitionTo(cellBroadcastConfigTracker3.mGsmActivatingState);
            }
            return true;
        }
    }

    private class GsmActivatingState extends State {
        private GsmActivatingState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            if (CellBroadcastConfigTracker.DBG) {
                CellBroadcastConfigTracker.this.logd("GsmActivatingState message:" + message.what);
            }
            int i = message.what;
            if (i == 1) {
                CellBroadcastConfigTracker.this.deferMessage(message);
                return true;
            }
            if (i != 3) {
                return false;
            }
            AsyncResult asyncResult = (AsyncResult) message.obj;
            Request request = (Request) asyncResult.userObj;
            if (CellBroadcastConfigTracker.DBG) {
                CellBroadcastConfigTracker.this.logd("GsmActivatingState handle EVENT_ACTIVATION_DONE with request:" + request);
                CellBroadcastConfigTracker.this.mLocalLog.log("GsmActivatingState EVENT_ACTIVATION_DONE, exception:" + asyncResult.exception + ", request:" + request);
            }
            Throwable th = asyncResult.exception;
            CellBroadcastConfigTracker cellBroadcastConfigTracker = CellBroadcastConfigTracker.this;
            if (th != null) {
                cellBroadcastConfigTracker.logd("Failed to set gsm activation");
                request.getCallback().accept(3);
                CellBroadcastConfigTracker cellBroadcastConfigTracker2 = CellBroadcastConfigTracker.this;
                cellBroadcastConfigTracker2.transitionTo(cellBroadcastConfigTracker2.mIdleState);
            } else {
                cellBroadcastConfigTracker.mCbRanges3gpp = request.get3gppRanges();
                CellBroadcastConfigTracker.this.logd("Done as no need to update ranges for 3gpp2");
                request.getCallback().accept(0);
                CellBroadcastConfigTracker cellBroadcastConfigTracker3 = CellBroadcastConfigTracker.this;
                cellBroadcastConfigTracker3.transitionTo(cellBroadcastConfigTracker3.mIdleState);
            }
            return true;
        }
    }

    private class CdmaConfiguringState extends State {
        private CdmaConfiguringState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            if (CellBroadcastConfigTracker.DBG) {
                CellBroadcastConfigTracker.this.logd("CdmaConfiguringState message:" + message.what);
            }
            int i = message.what;
            if (i == 1) {
                CellBroadcastConfigTracker.this.deferMessage(message);
                return true;
            }
            if (i != 2) {
                return false;
            }
            AsyncResult asyncResult = (AsyncResult) message.obj;
            Request request = (Request) asyncResult.userObj;
            if (CellBroadcastConfigTracker.DBG) {
                CellBroadcastConfigTracker.this.logd("CdmaConfiguringState handle EVENT_ACTIVATION_DONE with request:" + request);
            }
            Throwable th = asyncResult.exception;
            CellBroadcastConfigTracker cellBroadcastConfigTracker = CellBroadcastConfigTracker.this;
            if (th != null) {
                cellBroadcastConfigTracker.logd("Failed to set cdma config");
                CellBroadcastConfigTracker.this.mLocalLog.log("CdmaConfiguringState Failed to set cdma config:" + request);
                request.getCallback().accept(2);
                CellBroadcastConfigTracker cellBroadcastConfigTracker2 = CellBroadcastConfigTracker.this;
                cellBroadcastConfigTracker2.transitionTo(cellBroadcastConfigTracker2.mIdleState);
            } else {
                cellBroadcastConfigTracker.setActivation(2, !request.get3gpp2Ranges().isEmpty(), request);
                CellBroadcastConfigTracker cellBroadcastConfigTracker3 = CellBroadcastConfigTracker.this;
                cellBroadcastConfigTracker3.transitionTo(cellBroadcastConfigTracker3.mCdmaActivatingState);
            }
            return true;
        }
    }

    private class CdmaActivatingState extends State {
        private CdmaActivatingState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            if (CellBroadcastConfigTracker.DBG) {
                CellBroadcastConfigTracker.this.logd("CdmaActivatingState message:" + message.what);
            }
            int i = message.what;
            if (i == 1) {
                CellBroadcastConfigTracker.this.deferMessage(message);
                return true;
            }
            if (i != 3) {
                return false;
            }
            AsyncResult asyncResult = (AsyncResult) message.obj;
            Request request = (Request) asyncResult.userObj;
            if (CellBroadcastConfigTracker.DBG) {
                CellBroadcastConfigTracker.this.logd("CdmaActivatingState handle EVENT_ACTIVATION_DONE with request:" + request);
                CellBroadcastConfigTracker.this.mLocalLog.log("CdmaActivatingState EVENT_ACTIVATION_DONE, exception:" + asyncResult.exception + ", request:" + request);
            }
            Throwable th = asyncResult.exception;
            CellBroadcastConfigTracker cellBroadcastConfigTracker = CellBroadcastConfigTracker.this;
            if (th != null) {
                cellBroadcastConfigTracker.logd("Failed to set cdma activation");
                request.getCallback().accept(3);
            } else {
                cellBroadcastConfigTracker.mCbRanges3gpp2 = request.get3gpp2Ranges();
                request.getCallback().accept(0);
            }
            CellBroadcastConfigTracker cellBroadcastConfigTracker2 = CellBroadcastConfigTracker.this;
            cellBroadcastConfigTracker2.transitionTo(cellBroadcastConfigTracker2.mIdleState);
            return true;
        }
    }

    private CellBroadcastConfigTracker(Phone phone) {
        super("CellBroadcastConfigTracker-" + phone.getPhoneId());
        this.mCbRanges3gpp = new CopyOnWriteArrayList();
        this.mCbRanges3gpp2 = new CopyOnWriteArrayList();
        this.mLocalLog = new LocalLog(128);
        this.mSubChangedListener = new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.internal.telephony.CellBroadcastConfigTracker.1
            @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
            public void onSubscriptionsChanged() {
                CellBroadcastConfigTracker.this.sendMessage(5);
            }
        };
        this.mDefaultState = new DefaultState();
        this.mIdleState = new IdleState();
        this.mGsmConfiguringState = new GsmConfiguringState();
        this.mGsmActivatingState = new GsmActivatingState();
        this.mCdmaConfiguringState = new CdmaConfiguringState();
        this.mCdmaActivatingState = new CdmaActivatingState();
        init(phone);
    }

    private CellBroadcastConfigTracker(Phone phone, Handler handler) {
        super("CellBroadcastConfigTracker-" + phone.getPhoneId(), handler);
        this.mCbRanges3gpp = new CopyOnWriteArrayList();
        this.mCbRanges3gpp2 = new CopyOnWriteArrayList();
        this.mLocalLog = new LocalLog(128);
        this.mSubChangedListener = new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.internal.telephony.CellBroadcastConfigTracker.1
            @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
            public void onSubscriptionsChanged() {
                CellBroadcastConfigTracker.this.sendMessage(5);
            }
        };
        this.mDefaultState = new DefaultState();
        this.mIdleState = new IdleState();
        this.mGsmConfiguringState = new GsmConfiguringState();
        this.mGsmActivatingState = new GsmActivatingState();
        this.mCdmaConfiguringState = new CdmaConfiguringState();
        this.mCdmaActivatingState = new CdmaActivatingState();
        init(phone);
    }

    private void init(Phone phone) {
        logd("init");
        this.mPhone = phone;
        this.mSubId = phone.getSubId();
        addState(this.mDefaultState);
        addState(this.mIdleState, this.mDefaultState);
        addState(this.mGsmConfiguringState, this.mDefaultState);
        addState(this.mGsmActivatingState, this.mDefaultState);
        addState(this.mCdmaConfiguringState, this.mDefaultState);
        addState(this.mCdmaActivatingState, this.mDefaultState);
        setInitialState(this.mIdleState);
    }

    public static CellBroadcastConfigTracker make(Phone phone, Handler handler, boolean z) {
        CellBroadcastConfigTracker cellBroadcastConfigTracker;
        if (handler == null) {
            cellBroadcastConfigTracker = new CellBroadcastConfigTracker(phone);
        } else {
            cellBroadcastConfigTracker = new CellBroadcastConfigTracker(phone, handler);
        }
        if (z) {
            cellBroadcastConfigTracker.start();
        }
        return cellBroadcastConfigTracker;
    }

    public List<CellBroadcastIdRange> getCellBroadcastIdRanges() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.mCbRanges3gpp);
        arrayList.addAll(this.mCbRanges3gpp2);
        return arrayList;
    }

    public void setCellBroadcastIdRanges(List<CellBroadcastIdRange> list, Consumer<Integer> consumer) {
        if (DBG) {
            logd("setCellBroadcastIdRanges with ranges:" + list);
        }
        sendMessage(1, new Request(mergeRangesAsNeeded(list), consumer));
    }

    public static /* synthetic */ int $r8$lambda$dfqyyLJAU5VeqtMpuwq_1ZBmXu4(CellBroadcastIdRange cellBroadcastIdRange, CellBroadcastIdRange cellBroadcastIdRange2) {
        int startId;
        int startId2;
        if (cellBroadcastIdRange.getType() != cellBroadcastIdRange2.getType()) {
            startId = cellBroadcastIdRange.getType();
            startId2 = cellBroadcastIdRange2.getType();
        } else {
            if (cellBroadcastIdRange.getStartId() == cellBroadcastIdRange2.getStartId()) {
                return cellBroadcastIdRange2.getEndId() - cellBroadcastIdRange.getEndId();
            }
            startId = cellBroadcastIdRange.getStartId();
            startId2 = cellBroadcastIdRange2.getStartId();
        }
        return startId - startId2;
    }

    public static List<CellBroadcastIdRange> mergeRangesAsNeeded(List<CellBroadcastIdRange> list) throws IllegalArgumentException {
        list.sort(new Comparator() { // from class: com.android.internal.telephony.CellBroadcastConfigTracker$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return CellBroadcastConfigTracker.$r8$lambda$dfqyyLJAU5VeqtMpuwq_1ZBmXu4((CellBroadcastIdRange) obj, (CellBroadcastIdRange) obj2);
            }
        });
        final ArrayList arrayList = new ArrayList();
        list.forEach(new Consumer() { // from class: com.android.internal.telephony.CellBroadcastConfigTracker$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                CellBroadcastConfigTracker.m82$r8$lambda$kUUshudH8818dlh5hrjs8GC8ZA(arrayList, (CellBroadcastIdRange) obj);
            }
        });
        return arrayList;
    }

    /* JADX INFO: renamed from: $r8$lambda$kUUshudH8818dlh5hrjs-8GC8ZA, reason: not valid java name */
    public static /* synthetic */ void m82$r8$lambda$kUUshudH8818dlh5hrjs8GC8ZA(List list, CellBroadcastIdRange cellBroadcastIdRange) {
        if (list.isEmpty() || ((CellBroadcastIdRange) list.get(list.size() - 1)).getType() != cellBroadcastIdRange.getType() || ((CellBroadcastIdRange) list.get(list.size() - 1)).getEndId() + 1 < cellBroadcastIdRange.getStartId() || (((CellBroadcastIdRange) list.get(list.size() - 1)).getEndId() + 1 == cellBroadcastIdRange.getStartId() && ((CellBroadcastIdRange) list.get(list.size() - 1)).isEnabled() != cellBroadcastIdRange.isEnabled())) {
            list.add(new CellBroadcastIdRange(cellBroadcastIdRange.getStartId(), cellBroadcastIdRange.getEndId(), cellBroadcastIdRange.getType(), cellBroadcastIdRange.isEnabled()));
            return;
        }
        if (((CellBroadcastIdRange) list.get(list.size() - 1)).isEnabled() != cellBroadcastIdRange.isEnabled()) {
            RILUtils$$ExternalSyntheticBUOutline1.m("range conflict ", cellBroadcastIdRange);
        } else if (cellBroadcastIdRange.getEndId() > ((CellBroadcastIdRange) list.get(list.size() - 1)).getEndId()) {
            CellBroadcastIdRange cellBroadcastIdRange2 = (CellBroadcastIdRange) list.get(list.size() - 1);
            list.set(list.size() - 1, new CellBroadcastIdRange(cellBroadcastIdRange2.getStartId(), cellBroadcastIdRange.getEndId(), cellBroadcastIdRange2.getType(), cellBroadcastIdRange2.isEnabled()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetConfig() {
        this.mCbRanges3gpp.clear();
        this.mCbRanges3gpp2.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setGsmConfig(List list, Request request) {
        if (DBG) {
            logd("setGsmConfig with " + list);
        }
        int size = list.size();
        SmsBroadcastConfigInfo[] smsBroadcastConfigInfoArr = new SmsBroadcastConfigInfo[size];
        for (int i = 0; i < size; i++) {
            CellBroadcastIdRange cellBroadcastIdRange = (CellBroadcastIdRange) list.get(i);
            smsBroadcastConfigInfoArr[i] = new SmsBroadcastConfigInfo(cellBroadcastIdRange.getStartId(), cellBroadcastIdRange.getEndId(), 0, 255, cellBroadcastIdRange.isEnabled());
        }
        try {
            this.mPhone.mCi.setGsmBroadcastConfig(smsBroadcastConfigInfoArr, obtainMessage(2, request));
        } catch (RuntimeException e) {
            log("setGsmConfig: Exception " + e);
            removeMessages(2);
            Message messageObtainMessage = obtainMessage(2, request);
            AsyncResult.forMessage(messageObtainMessage, (Object) null, e);
            messageObtainMessage.sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setActivation(int i, boolean z, Request request) {
        if (DBG) {
            logd("setActivation(" + i + "." + z + ')');
        }
        Message messageObtainMessage = obtainMessage(3, request);
        try {
            if (i == 1) {
                this.mPhone.mCi.setGsmBroadcastActivation(z, messageObtainMessage);
                return;
            }
            if (i == 2) {
                this.mPhone.mCi.setCdmaBroadcastActivation(z, messageObtainMessage);
                return;
            }
            log("setActivation: Invalid type " + i);
            AsyncResult.forMessage(messageObtainMessage, (Object) null, new IllegalArgumentException("Invalid message format type " + i));
            messageObtainMessage.sendToTarget();
        } catch (RuntimeException e) {
            log("setActivation: Exception " + e);
            removeMessages(3);
            Message messageObtainMessage2 = obtainMessage(3, request);
            AsyncResult.forMessage(messageObtainMessage2, (Object) null, e);
            messageObtainMessage2.sendToTarget();
        }
    }

    @Override // com.android.internal.telephony.StateMachine
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        AndroidUtilIndentingPrintWriter androidUtilIndentingPrintWriter = new AndroidUtilIndentingPrintWriter(printWriter, "  ");
        androidUtilIndentingPrintWriter.println(CellBroadcastConfigTracker.class.getSimpleName() + "-" + this.mPhone.getPhoneId() + ":");
        androidUtilIndentingPrintWriter.increaseIndent();
        StringBuilder sb = new StringBuilder("Current mCbRanges3gpp:");
        sb.append(this.mCbRanges3gpp);
        androidUtilIndentingPrintWriter.println(sb.toString());
        androidUtilIndentingPrintWriter.println("Current mCbRanges3gpp2:" + this.mCbRanges3gpp2);
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.println("Local logs:");
        androidUtilIndentingPrintWriter.increaseIndent();
        this.mLocalLog.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
        androidUtilIndentingPrintWriter.decreaseIndent();
    }
}
