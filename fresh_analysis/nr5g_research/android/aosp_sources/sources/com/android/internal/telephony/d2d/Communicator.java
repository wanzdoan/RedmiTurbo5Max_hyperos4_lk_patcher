package com.android.internal.telephony.d2d;

import com.android.telephony.Rlog;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class Communicator implements TransportProtocol.Callback {
    public static final int AUDIO_CODEC_AMR_NB = 3;
    public static final int AUDIO_CODEC_AMR_WB = 2;
    public static final int AUDIO_CODEC_EVS = 1;
    public static final int BATTERY_STATE_CHARGING = 3;
    public static final int BATTERY_STATE_GOOD = 2;
    public static final int BATTERY_STATE_LOW = 1;
    public static final int COVERAGE_GOOD = 2;
    public static final int COVERAGE_POOR = 1;
    public static final int MESSAGE_CALL_AUDIO_CODEC = 2;
    public static final int MESSAGE_CALL_RADIO_ACCESS_TYPE = 1;
    public static final int MESSAGE_DEVICE_BATTERY_STATE = 3;
    public static final int MESSAGE_DEVICE_NETWORK_COVERAGE = 4;
    public static final int RADIO_ACCESS_TYPE_IWLAN = 2;
    public static final int RADIO_ACCESS_TYPE_LTE = 1;
    public static final int RADIO_ACCESS_TYPE_NR = 3;
    private TransportProtocol mActiveTransport;
    private Callback mCallback;
    private boolean mIsNegotiated;
    private boolean mIsNegotiationAttempted;
    private List mTransportProtocols = new ArrayList();

    public interface Callback {
        void onMessagesReceived(Set<Message> set);
    }

    public static class Message {
        private int mType;
        private int mValue;

        public Message(int i, int i2) {
            this.mType = i;
            this.mValue = i2;
        }

        public int getType() {
            return this.mType;
        }

        public int getValue() {
            return this.mValue;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj != null && getClass() == obj.getClass()) {
                Message message = (Message) obj;
                if (this.mType == message.mType && this.mValue == message.mValue) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.mType), Integer.valueOf(this.mValue));
        }

        public String toString() {
            return "Message{mType=" + Communicator.messageToString(this.mType) + ", mValue=" + Communicator.valueToString(this.mType, this.mValue) + '}';
        }
    }

    public Communicator(List<TransportProtocol> list, Callback callback) {
        Rlog.i("Communicator", "Initializing communicator with transports: " + ((String) list.stream().map(new Function() { // from class: com.android.internal.telephony.d2d.Communicator$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((TransportProtocol) obj).getClass().getSimpleName();
            }
        }).collect(Collectors.joining(","))));
        this.mTransportProtocols.addAll(list);
        this.mTransportProtocols.forEach(new Consumer() { // from class: com.android.internal.telephony.d2d.Communicator$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$new$1((TransportProtocol) obj);
            }
        });
        this.mCallback = callback;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(TransportProtocol transportProtocol) {
        transportProtocol.setCallback(this);
    }

    public TransportProtocol getActiveTransport() {
        return this.mActiveTransport;
    }

    public void onStateChanged(String str, int i) {
        Rlog.i("Communicator", "onStateChanged: id=" + str + ", newState=" + i);
        if (i == 4 && this.mActiveTransport == null && !this.mIsNegotiationAttempted) {
            this.mIsNegotiated = false;
            this.mIsNegotiationAttempted = true;
            Rlog.i("Communicator", "onStateChanged: call active; negotiate D2D.");
            negotiateNextProtocol();
        }
    }

    @Override // com.android.internal.telephony.d2d.TransportProtocol.Callback
    public void onNegotiationSuccess(TransportProtocol transportProtocol) {
        String simpleName;
        TransportProtocol transportProtocol2 = this.mActiveTransport;
        if (transportProtocol != transportProtocol2) {
            if (transportProtocol2 == null) {
                simpleName = "none";
            } else {
                simpleName = transportProtocol2.getClass().getSimpleName();
            }
            Rlog.w("Communicator", "onNegotiationSuccess: ignored - " + transportProtocol.getClass().getSimpleName() + " negotiated but active transport is " + simpleName);
        }
        Rlog.i("Communicator", "onNegotiationSuccess: " + transportProtocol.getClass().getSimpleName() + " negotiated; setting active.");
        this.mIsNegotiated = true;
    }

    @Override // com.android.internal.telephony.d2d.TransportProtocol.Callback
    public void onNegotiationFailed(TransportProtocol transportProtocol) {
        Rlog.i("Communicator", "onNegotiationFailed: " + transportProtocol.getClass().getSimpleName() + " failed to negotiate.");
        this.mIsNegotiated = false;
        negotiateNextProtocol();
    }

    @Override // com.android.internal.telephony.d2d.TransportProtocol.Callback
    public void onMessagesReceived(Set<Message> set) {
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onMessagesReceived(set);
        }
    }

    public void sendMessages(Set<Message> set) {
        if (this.mActiveTransport == null || !this.mIsNegotiated) {
            Rlog.w("Communicator", "sendMessages: no active transport");
            return;
        }
        Rlog.i("Communicator", "sendMessages: msgs=" + set.size() + ", activeTransport=" + this.mActiveTransport.getClass().getSimpleName());
        this.mActiveTransport.sendMessages(set);
    }

    private void negotiateNextProtocol() {
        TransportProtocol nextCandidateProtocol = getNextCandidateProtocol();
        this.mActiveTransport = nextCandidateProtocol;
        if (nextCandidateProtocol == null) {
            Rlog.i("Communicator", "negotiateNextProtocol: no remaining transports.");
        } else {
            Rlog.i("Communicator", "negotiateNextProtocol: trying ".concat(nextCandidateProtocol.getClass().getSimpleName()));
            this.mActiveTransport.startNegotiation();
        }
    }

    private TransportProtocol getNextCandidateProtocol() {
        if (this.mActiveTransport == null) {
            if (this.mTransportProtocols.size() > 0) {
                return (TransportProtocol) this.mTransportProtocols.get(0);
            }
            this.mIsNegotiated = false;
            return null;
        }
        for (int i = 0; i < this.mTransportProtocols.size(); i++) {
            if (((TransportProtocol) this.mTransportProtocols.get(i)) == this.mActiveTransport) {
                int i2 = i + 1;
                if (i2 >= this.mTransportProtocols.size()) {
                    break;
                }
                return (TransportProtocol) this.mTransportProtocols.get(i2);
            }
        }
        return null;
    }

    public static String messageToString(int i) {
        if (i == 1) {
            return "MESSAGE_CALL_RADIO_ACCESS_TYPE";
        }
        if (i == 2) {
            return "MESSAGE_CALL_AUDIO_CODEC";
        }
        if (i == 3) {
            return "MESSAGE_DEVICE_BATTERY_STATE";
        }
        if (i == 4) {
            return "MESSAGE_DEVICE_NETWORK_COVERAGE";
        }
        return "";
    }

    public static String valueToString(int i, int i2) {
        if (i == 1) {
            if (i2 == 1) {
                return "RADIO_ACCESS_TYPE_LTE";
            }
            if (i2 == 2) {
                return "RADIO_ACCESS_TYPE_IWLAN";
            }
            if (i2 != 3) {
                return "";
            }
            return "RADIO_ACCESS_TYPE_NR";
        }
        if (i == 2) {
            if (i2 == 1) {
                return "AUDIO_CODEC_EVS";
            }
            if (i2 == 2) {
                return "AUDIO_CODEC_AMR_WB";
            }
            if (i2 != 3) {
                return "";
            }
            return "AUDIO_CODEC_AMR_NB";
        }
        if (i != 3) {
            if (i != 4) {
                return "";
            }
            if (i2 == 1) {
                return "COVERAGE_POOR";
            }
            if (i2 != 2) {
                return "";
            }
            return "COVERAGE_GOOD";
        }
        if (i2 == 1) {
            return "BATTERY_STATE_LOW";
        }
        if (i2 == 2) {
            return "BATTERY_STATE_GOOD";
        }
        if (i2 != 3) {
            return "";
        }
        return "BATTERY_STATE_CHARGING";
    }

    public void setTransportActive(final String str) {
        final Optional optionalFindFirst = this.mTransportProtocols.stream().filter(new Predicate() { // from class: com.android.internal.telephony.d2d.Communicator$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((TransportProtocol) obj).getClass().getSimpleName().equals(str);
            }
        }).findFirst();
        if (!optionalFindFirst.isPresent()) {
            Rlog.w("Communicator", "setTransportActive: " + str + " is not a valid transport.");
            return;
        }
        this.mTransportProtocols.stream().filter(new Predicate() { // from class: com.android.internal.telephony.d2d.Communicator$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return Communicator.$r8$lambda$C0Xs8w1IEVDWTr5Q1tjTNwGq3v0(optionalFindFirst, (TransportProtocol) obj);
            }
        }).forEach(new Consumer() { // from class: com.android.internal.telephony.d2d.Communicator$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((TransportProtocol) obj).forceNotNegotiated();
            }
        });
        ((TransportProtocol) optionalFindFirst.get()).forceNegotiated();
        this.mActiveTransport = (TransportProtocol) optionalFindFirst.get();
        this.mIsNegotiated = true;
        Rlog.i("Communicator", "setTransportActive: " + str + " has been forced active.");
    }

    public static /* synthetic */ boolean $r8$lambda$C0Xs8w1IEVDWTr5Q1tjTNwGq3v0(Optional optional, TransportProtocol transportProtocol) {
        return transportProtocol != optional.get();
    }

    public List<TransportProtocol> getTransportProtocols() {
        return this.mTransportProtocols;
    }
}
