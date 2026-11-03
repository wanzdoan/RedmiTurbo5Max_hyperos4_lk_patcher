package com.android.internal.telephony.d2d;

import android.util.ArraySet;
import android.util.Pair;
import com.android.internal.telephony.BiMap;
import com.android.telephony.Rlog;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public class DtmfTransport implements TransportProtocol {
    public static final String DMTF_PROBE_MESSAGE = "AAD";
    public static final String DMTF_PROTOCOL_VERSION = "A";
    public static final String DTMF_MESSAGE_BATERY = "C";
    public static final String DTMF_MESSAGE_BATTERY_CHARGING = "C";
    public static final String DTMF_MESSAGE_BATTERY_GOOD = "B";
    public static final String DTMF_MESSAGE_BATTERY_LOW = "A";
    public static final String DTMF_MESSAGE_CODEC = "B";
    public static final String DTMF_MESSAGE_CODEC_AMR_NB = "C";
    public static final String DTMF_MESSAGE_CODEC_AMR_WB = "B";
    public static final String DTMF_MESSAGE_CODEC_EVS = "A";
    public static final char DTMF_MESSAGE_DELIMITER = 'D';
    public static final String DTMF_MESSAGE_RAT = "A";
    public static final String DTMF_MESSAGE_RAT_IWLAN = "B";
    public static final String DTMF_MESSAGE_RAT_LTE = "A";
    public static final String DTMF_MESSAGE_RAT_NR = "C";
    public static final String DTMF_MESSAGE_SERVICE = "AA";
    public static final String DTMF_MESSAGE_SERVICE_GOOD = "A";
    public static final String DTMF_MESSAGE_SERVICE_POOR = "B";
    public static final char DTMF_MESSAGE_START = 'A';
    public static final BiMap<Pair<String, String>, Communicator.Message> DTMF_TO_MESSAGE;
    public static final int RECEIVE_STATE_IDLE = 0;
    public static final int RECEIVE_STATE_MESSAGE_TYPE = 1;
    public static final int RECEIVE_STATE_MESSAGE_VALUE = 2;
    public static final int STATE_IDLE = 0;
    public static final int STATE_NEGOTIATED = 2;
    public static final int STATE_NEGOTIATING = 1;
    public static final int STATE_NEGOTIATION_FAILED = 3;
    private TransportProtocol.Callback mCallback;
    private ScheduledFuture mDigitSendScheduledFuture;
    private final DtmfAdapter mDtmfAdapter;
    private final long mDtmfDurationFuzzMillis;
    private ScheduledFuture mDtmfMessageTimeoutFuture;
    private final long mDurationOfDtmfMessageMillis;
    private final long mIntervalBetweenDigitsMillis;
    private char[] mMessageToSend;
    private ScheduledFuture mNegotiationFuture;
    private final long mNegotiationTimeoutMillis;
    private String mProtocolVersion;
    private final ScheduledExecutorService mScheduledExecutorService;
    private int mTransportState = 0;
    private StringBuffer mProbeDigits = new StringBuffer();
    private int mMessageReceiveState = 0;
    private StringBuffer mMessageTypeDigits = new StringBuffer();
    private StringBuffer mMessageValueDigits = new StringBuffer();
    private final ConcurrentLinkedQueue mPendingMessages = new ConcurrentLinkedQueue();
    private Object mProbeLock = new Object();
    private Object mDtmfMessageTimeoutLock = new Object();
    private Object mDigitSendLock = new Object();
    private Object mNegotiationLock = new Object();
    private Object mDigitsLock = new Object();
    private int mCharToSend = 0;
    private Random mRandom = new Random();

    @Override // com.android.internal.telephony.d2d.TransportProtocol
    public void forceNegotiated() {
    }

    @Override // com.android.internal.telephony.d2d.TransportProtocol
    public void forceNotNegotiated() {
    }

    static {
        BiMap<Pair<String, String>, Communicator.Message> biMap = new BiMap<>();
        DTMF_TO_MESSAGE = biMap;
        biMap.put(new Pair<>("A", "A"), new Communicator.Message(1, 1));
        biMap.put(new Pair<>("A", "B"), new Communicator.Message(1, 2));
        biMap.put(new Pair<>("A", "C"), new Communicator.Message(1, 3));
        biMap.put(new Pair<>("B", "A"), new Communicator.Message(2, 1));
        biMap.put(new Pair<>("B", "B"), new Communicator.Message(2, 2));
        biMap.put(new Pair<>("B", "C"), new Communicator.Message(2, 3));
        biMap.put(new Pair<>("C", "A"), new Communicator.Message(3, 1));
        biMap.put(new Pair<>("C", "B"), new Communicator.Message(3, 2));
        biMap.put(new Pair<>("C", "C"), new Communicator.Message(3, 3));
        biMap.put(new Pair<>(DTMF_MESSAGE_SERVICE, "A"), new Communicator.Message(4, 2));
        biMap.put(new Pair<>(DTMF_MESSAGE_SERVICE, "B"), new Communicator.Message(4, 1));
    }

    public DtmfTransport(DtmfAdapter dtmfAdapter, Timeouts.Adapter adapter, ScheduledExecutorService scheduledExecutorService) {
        this.mDtmfAdapter = dtmfAdapter;
        this.mIntervalBetweenDigitsMillis = adapter.getDtmfMinimumIntervalMillis();
        this.mDurationOfDtmfMessageMillis = adapter.getMaxDurationOfDtmfMessageMillis();
        this.mDtmfDurationFuzzMillis = adapter.getDtmfDurationFuzzMillis();
        this.mNegotiationTimeoutMillis = adapter.getDtmfNegotiationTimeoutMillis();
        this.mScheduledExecutorService = scheduledExecutorService;
    }

    @Override // com.android.internal.telephony.d2d.TransportProtocol
    public void setCallback(TransportProtocol.Callback callback) {
        this.mCallback = callback;
    }

    @Override // com.android.internal.telephony.d2d.TransportProtocol
    public void startNegotiation() {
        if (this.mTransportState != 0) {
            Rlog.w("DtmfTransport", "startNegotiation: can't start negotiation as not idle.");
            return;
        }
        this.mTransportState = 1;
        Rlog.i("DtmfTransport", "startNegotiation: starting negotiation.");
        this.mPendingMessages.offer(new char[]{DTMF_MESSAGE_START, DTMF_MESSAGE_START, DTMF_MESSAGE_DELIMITER});
        maybeScheduleMessageSend();
        scheduleNegotiationTimeout();
    }

    @Override // com.android.internal.telephony.d2d.TransportProtocol
    public void sendMessages(Set<Communicator.Message> set) {
        Iterator<Communicator.Message> it = set.iterator();
        while (it.hasNext()) {
            char[] messageDigits = getMessageDigits(it.next());
            if (messageDigits != null) {
                Rlog.i("DtmfTransport", "sendMessages: queueing message: " + String.valueOf(messageDigits));
                this.mPendingMessages.offer(messageDigits);
            }
        }
        if (this.mPendingMessages.size() > 0) {
            maybeScheduleMessageSend();
        }
    }

    private void maybeScheduleMessageSend() {
        synchronized (this.mDigitSendLock) {
            try {
                if (this.mMessageToSend == null && this.mDigitSendScheduledFuture == null) {
                    char[] cArr = (char[]) this.mPendingMessages.poll();
                    this.mMessageToSend = cArr;
                    this.mCharToSend = 0;
                    if (cArr != null) {
                        Rlog.i("DtmfTransport", "maybeScheduleMessageSend: toSend=" + String.valueOf(this.mMessageToSend));
                        this.mDigitSendScheduledFuture = this.mScheduledExecutorService.scheduleAtFixedRate(new Runnable() { // from class: com.android.internal.telephony.d2d.DtmfTransport$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$maybeScheduleMessageSend$0();
                            }
                        }, this.mDurationOfDtmfMessageMillis + getDtmfDurationFuzzMillis(), this.mIntervalBetweenDigitsMillis, TimeUnit.MILLISECONDS);
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private long getDtmfDurationFuzzMillis() {
        if (this.mDtmfDurationFuzzMillis == 0) {
            return 0L;
        }
        return this.mRandom.nextLong() % this.mDtmfDurationFuzzMillis;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleDtmfSend, reason: merged with bridge method [inline-methods] */
    public void lambda$maybeScheduleMessageSend$0() {
        if (this.mCharToSend < this.mMessageToSend.length) {
            if (this.mDtmfAdapter != null) {
                Rlog.i("DtmfTransport", "handleDtmfSend: char=" + this.mMessageToSend[this.mCharToSend]);
                this.mDtmfAdapter.sendDtmf(this.mMessageToSend[this.mCharToSend]);
            }
            int i = this.mCharToSend + 1;
            this.mCharToSend = i;
            if (i == this.mMessageToSend.length) {
                Rlog.i("DtmfTransport", "handleDtmfSend: done");
                synchronized (this.mDigitSendLock) {
                    try {
                        this.mMessageToSend = null;
                        this.mDigitSendScheduledFuture.cancel(false);
                        this.mDigitSendScheduledFuture = null;
                        if (this.mTransportState == 2) {
                            maybeScheduleMessageSend();
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        }
    }

    public int getTransportState() {
        return this.mTransportState;
    }

    public void onDtmfReceived(char c) {
        if (c < 'A' || c > 'D') {
            Rlog.i("DtmfTransport", "onDtmfReceived: digit = " + c + "; invalid digit; not in A-D");
            return;
        }
        if (this.mTransportState == 1) {
            synchronized (this.mProbeLock) {
                this.mProbeDigits.append(c);
            }
            if (c == 'D') {
                Rlog.i("DtmfTransport", "onDtmfReceived: received message " + ((Object) this.mProbeDigits));
                handleProbeMessage();
                return;
            }
            return;
        }
        handleReceivedDigit(c);
    }

    private void handleProbeMessage() {
        String string;
        synchronized (this.mProbeLock) {
            try {
                string = this.mProbeDigits.toString();
                if (this.mProbeDigits.length() > 0) {
                    StringBuffer stringBuffer = this.mProbeDigits;
                    stringBuffer.delete(0, stringBuffer.length());
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        cancelNegotiationTimeout();
        if (string.startsWith(String.valueOf(DTMF_MESSAGE_START)) && string.endsWith(String.valueOf(DTMF_MESSAGE_DELIMITER)) && string.length() > 2) {
            this.mProtocolVersion = string.substring(1, string.length() - 1);
            Rlog.i("DtmfTransport", "handleProbeMessage: got valid probe, remote version " + string + " negotiated.");
            negotiationSucceeded();
        } else {
            Rlog.i("DtmfTransport", "handleProbeMessage: got invalid probe " + string + " - negotiation failed.");
            negotiationFailed();
        }
        cancelNegotiationTimeout();
    }

    private void scheduleNegotiationTimeout() {
        synchronized (this.mNegotiationLock) {
            this.mNegotiationFuture = this.mScheduledExecutorService.schedule(new Runnable() { // from class: com.android.internal.telephony.d2d.DtmfTransport$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$scheduleNegotiationTimeout$1();
                }
            }, this.mNegotiationTimeoutMillis, TimeUnit.MILLISECONDS);
        }
    }

    private void cancelNegotiationTimeout() {
        Rlog.i("DtmfTransport", "cancelNegotiationTimeout");
        synchronized (this.mNegotiationLock) {
            try {
                ScheduledFuture scheduledFuture = this.mNegotiationFuture;
                if (scheduledFuture != null) {
                    scheduledFuture.cancel(false);
                }
                this.mNegotiationFuture = null;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleNegotiationTimeout, reason: merged with bridge method [inline-methods] */
    public void lambda$scheduleNegotiationTimeout$1() {
        Rlog.i("DtmfTransport", "handleNegotiationTimeout: no probe received, negotiation timeout.");
        synchronized (this.mNegotiationLock) {
            this.mNegotiationFuture = null;
        }
        negotiationFailed();
    }

    private void negotiationFailed() {
        this.mTransportState = 3;
        Rlog.i("DtmfTransport", "notifyNegotiationFailed");
        TransportProtocol.Callback callback = this.mCallback;
        if (callback != null) {
            callback.onNegotiationFailed(this);
        }
    }

    private void negotiationSucceeded() {
        this.mTransportState = 2;
        Rlog.i("DtmfTransport", "negotiationSucceeded");
        TransportProtocol.Callback callback = this.mCallback;
        if (callback != null) {
            callback.onNegotiationSuccess(this);
        }
    }

    private void handleReceivedDigit(char c) {
        String string;
        String string2;
        int i = this.mMessageReceiveState;
        if (i == 0) {
            if (c == 'A') {
                Rlog.i("DtmfTransport", "handleReceivedDigit: digit = " + c + " ; message timeout started.");
                this.mMessageReceiveState = 1;
                scheduleDtmfMessageTimeout();
                return;
            }
            Rlog.w("DtmfTransport", "handleReceivedDigit: digit = " + c + " ; unexpected start digit, ignoring.");
            return;
        }
        if (c != 'D') {
            synchronized (this.mDigitsLock) {
                try {
                    int i2 = this.mMessageReceiveState;
                    if (i2 == 1) {
                        this.mMessageTypeDigits.append(c);
                        Rlog.i("DtmfTransport", "handleReceivedDigit: typeDigit = " + c + " ; msg = " + this.mMessageTypeDigits.toString());
                    } else if (i2 == 2) {
                        this.mMessageValueDigits.append(c);
                        Rlog.i("DtmfTransport", "handleReceivedDigit: valueDigit = " + c + " ; value = " + this.mMessageValueDigits.toString());
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            return;
        }
        if (i == 1) {
            Rlog.i("DtmfTransport", "handleReceivedDigit: digit = " + c + " ; msg = " + this.mMessageTypeDigits.toString() + " ; awaiting value.");
            this.mMessageReceiveState = 2;
            return;
        }
        if (i == 2) {
            maybeCancelDtmfMessageTimeout();
            synchronized (this.mDigitsLock) {
                string = this.mMessageTypeDigits.toString();
                string2 = this.mMessageValueDigits.toString();
            }
            Rlog.i("DtmfTransport", "handleReceivedDigit: digit = " + c + " ; msg = " + string + " ; value = " + string2 + " ; full msg");
            handleIncomingMessage(string, string2);
            resetIncomingMessage();
        }
    }

    private void scheduleDtmfMessageTimeout() {
        synchronized (this.mDtmfMessageTimeoutLock) {
            maybeCancelDtmfMessageTimeout();
            this.mDtmfMessageTimeoutFuture = this.mScheduledExecutorService.schedule(new Runnable() { // from class: com.android.internal.telephony.d2d.DtmfTransport$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$scheduleDtmfMessageTimeout$2();
                }
            }, this.mDurationOfDtmfMessageMillis, TimeUnit.MILLISECONDS);
        }
    }

    private void maybeCancelDtmfMessageTimeout() {
        synchronized (this.mDtmfMessageTimeoutLock) {
            try {
                if (this.mDtmfMessageTimeoutFuture != null) {
                    Rlog.i("DtmfTransport", "scheduleDtmfMessageTimeout: timeout pending; cancelling");
                    this.mDtmfMessageTimeoutFuture.cancel(false);
                    this.mDtmfMessageTimeoutFuture = null;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleDtmfMessageTimeout, reason: merged with bridge method [inline-methods] */
    public void lambda$scheduleDtmfMessageTimeout$2() {
        maybeCancelDtmfMessageTimeout();
        Rlog.i("DtmfTransport", "handleDtmfMessageTimeout: timeout receiving DTMF string; got " + this.mMessageTypeDigits.toString() + "/" + this.mMessageValueDigits.toString() + " so far");
        resetIncomingMessage();
    }

    public char[] getMessageDigits(Communicator.Message message) {
        Pair<String, String> key = DTMF_TO_MESSAGE.getKey(message);
        if (key == null) {
            return null;
        }
        return ("A" + ((String) key.first) + DTMF_MESSAGE_DELIMITER + ((String) key.second) + DTMF_MESSAGE_DELIMITER).toCharArray();
    }

    public Communicator.Message extractMessage(String str, String str2) {
        return DTMF_TO_MESSAGE.getValue(new Pair<>(str, str2));
    }

    private void handleIncomingMessage(String str, String str2) {
        Communicator.Message messageExtractMessage = extractMessage(str, str2);
        if (messageExtractMessage == null) {
            Rlog.w("DtmfTransport", "handleIncomingMessage: msgDigits = " + str + ", msgValueDigits = " + str2 + "; invalid msg");
            return;
        }
        Rlog.i("DtmfTransport", "handleIncomingMessage: msgDigits = " + str + ", msgValueDigits = " + str2);
        ArraySet arraySet = new ArraySet(1);
        arraySet.add(messageExtractMessage);
        TransportProtocol.Callback callback = this.mCallback;
        if (callback != null) {
            callback.onMessagesReceived(arraySet);
        }
    }

    private void resetIncomingMessage() {
        this.mMessageReceiveState = 0;
        synchronized (this.mDigitsLock) {
            try {
                if (this.mMessageTypeDigits.length() != 0) {
                    StringBuffer stringBuffer = this.mMessageTypeDigits;
                    stringBuffer.delete(0, stringBuffer.length());
                }
                if (this.mMessageValueDigits.length() != 0) {
                    StringBuffer stringBuffer2 = this.mMessageValueDigits;
                    stringBuffer2.delete(0, stringBuffer2.length());
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
