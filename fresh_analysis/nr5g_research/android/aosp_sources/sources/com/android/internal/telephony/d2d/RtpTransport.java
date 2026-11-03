package com.android.internal.telephony.d2d;

import android.net.Uri;
import android.os.Handler;
import android.telephony.ims.RtpHeaderExtension;
import android.telephony.ims.RtpHeaderExtensionType;
import android.util.ArraySet;
import com.android.internal.telephony.BiMap;
import com.android.telephony.Rlog;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class RtpTransport implements TransportProtocol, RtpAdapter.Callback {
    private static final BiMap BATTERY_STATE_VALUE_TO_RTP_BITS;
    private static final BiMap CALL_STATE_MSG_TYPE_TO_RTP_BITS;
    private static final BiMap CODEC_VALUE_TO_RTP_BITS;
    private static final BiMap DEVICE_STATE_MSG_TYPE_TO_RTP_BITS;
    private static final BiMap NETWORK_COVERAGE_VALUE_TO_RTP_BITS;
    public static final int PROTOCOL_STATUS_NEGOTIATION_COMPLETE = 3;
    public static final int PROTOCOL_STATUS_NEGOTIATION_FAILED = 4;
    public static final int PROTOCOL_STATUS_NEGOTIATION_REQUIRED = 1;
    public static final int PROTOCOL_STATUS_NEGOTIATION_WAITING_ON_PACKET = 2;
    private static final BiMap RAT_VALUE_TO_RTP_BITS;
    public static final byte RTP_BATTERY_STATE_CHARGING_BITS = 48;
    public static final byte RTP_BATTERY_STATE_GOOD_BITS = 16;
    public static final byte RTP_BATTERY_STATE_LOW_BITS = 0;
    public static final byte RTP_CALL_STATE_MSG_CODEC_BITS = 2;
    public static final byte RTP_CALL_STATE_MSG_RADIO_ACCESS_TYPE_BITS = 1;
    public static final byte RTP_CODEC_VALUE_AMR_NB_BITS = 48;
    public static final byte RTP_CODEC_VALUE_AMR_WB_BITS = 32;
    public static final byte RTP_CODEC_VALUE_EVS_BITS = 16;
    public static final byte RTP_DEVICE_STATE_MSG_BATTERY_BITS = 1;
    public static final byte RTP_DEVICE_STATE_MSG_NETWORK_COVERAGE_BITS = 2;
    public static final byte RTP_NETWORK_COVERAGE_GOOD_BITS = 16;
    public static final byte RTP_NETWORK_COVERAGE_POOR_BITS = 0;
    public static final int RTP_PARAMETER_BIT_OFFSET = 4;
    public static final byte RTP_RAT_VALUE_LTE_BITS = 16;
    public static final byte RTP_RAT_VALUE_NR_BITS = 48;
    public static final byte RTP_RAT_VALUE_WLAN_BITS = 32;
    private TransportProtocol.Callback mCallback;
    private final Handler mHandler;
    private final boolean mIsSdpNegotiationSupported;
    private final RtpAdapter mRtpAdapter;
    private final Timeouts.Adapter mTimeoutsAdapter;
    public static Uri DEVICE_STATE_RTP_HEADER_EXTENSION = Uri.parse("http://develop.android.com/122020/d2dcomm#device-state");
    public static Uri CALL_STATE_RTP_HEADER_EXTENSION = Uri.parse("http://develop.android.com/122020/d2dcomm#call-state");
    public static int DEVICE_STATE_LOCAL_IDENTIFIER = 10;
    public static int CALL_STATE_LOCAL_IDENTIFIER = 11;
    public static RtpHeaderExtensionType DEVICE_STATE_RTP_HEADER_EXTENSION_TYPE = new RtpHeaderExtensionType(DEVICE_STATE_LOCAL_IDENTIFIER, DEVICE_STATE_RTP_HEADER_EXTENSION);
    public static RtpHeaderExtensionType CALL_STATE_RTP_HEADER_EXTENSION_TYPE = new RtpHeaderExtensionType(CALL_STATE_LOCAL_IDENTIFIER, CALL_STATE_RTP_HEADER_EXTENSION);
    private int mProtocolStatus = 1;
    private ArraySet mSupportedRtpHeaderExtensionTypes = new ArraySet();

    static {
        BiMap biMap = new BiMap();
        CALL_STATE_MSG_TYPE_TO_RTP_BITS = biMap;
        BiMap biMap2 = new BiMap();
        DEVICE_STATE_MSG_TYPE_TO_RTP_BITS = biMap2;
        biMap.put(1, (byte) 1);
        biMap.put(2, (byte) 2);
        biMap2.put(3, (byte) 1);
        biMap2.put(4, (byte) 2);
        BiMap biMap3 = new BiMap();
        RAT_VALUE_TO_RTP_BITS = biMap3;
        biMap3.put(2, (byte) 32);
        biMap3.put(1, (byte) 16);
        biMap3.put(3, (byte) 48);
        BiMap biMap4 = new BiMap();
        CODEC_VALUE_TO_RTP_BITS = biMap4;
        biMap4.put(1, (byte) 16);
        biMap4.put(2, (byte) 32);
        biMap4.put(3, (byte) 48);
        BiMap biMap5 = new BiMap();
        BATTERY_STATE_VALUE_TO_RTP_BITS = biMap5;
        biMap5.put(1, (byte) 0);
        biMap5.put(2, (byte) 16);
        biMap5.put(3, (byte) 48);
        BiMap biMap6 = new BiMap();
        NETWORK_COVERAGE_VALUE_TO_RTP_BITS = biMap6;
        biMap6.put(1, (byte) 0);
        biMap6.put(2, (byte) 16);
    }

    public RtpTransport(RtpAdapter rtpAdapter, Timeouts.Adapter adapter, Handler handler, boolean z) {
        this.mRtpAdapter = rtpAdapter;
        this.mTimeoutsAdapter = adapter;
        this.mHandler = handler;
        this.mIsSdpNegotiationSupported = z;
    }

    @Override // com.android.internal.telephony.d2d.TransportProtocol
    public void setCallback(TransportProtocol.Callback callback) {
        this.mCallback = callback;
    }

    @Override // com.android.internal.telephony.d2d.TransportProtocol
    public void startNegotiation() {
        Set<RtpHeaderExtensionType> acceptedRtpHeaderExtensions = this.mRtpAdapter.getAcceptedRtpHeaderExtensions();
        this.mSupportedRtpHeaderExtensionTypes.addAll(acceptedRtpHeaderExtensions);
        Rlog.i("RtpTransport", "startNegotiation: supportedExtensions=" + ((String) this.mSupportedRtpHeaderExtensionTypes.stream().map(new Function() { // from class: com.android.internal.telephony.d2d.RtpTransport$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((RtpHeaderExtensionType) obj).toString();
            }
        }).collect(Collectors.joining(","))));
        if (!this.mIsSdpNegotiationSupported) {
            Rlog.i("RtpTransport", "startNegotiation: SDP negotiation not supported; negotiation complete");
            notifyProtocolReady();
        } else if (acceptedRtpHeaderExtensions.stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.d2d.RtpTransport$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((RtpHeaderExtensionType) obj).getUri().equals(RtpTransport.DEVICE_STATE_RTP_HEADER_EXTENSION);
            }
        }) && acceptedRtpHeaderExtensions.stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.d2d.RtpTransport$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((RtpHeaderExtensionType) obj).getUri().equals(RtpTransport.CALL_STATE_RTP_HEADER_EXTENSION);
            }
        })) {
            this.mProtocolStatus = 3;
            Rlog.i("RtpTransport", "startNegotiation: header extensions available, negotiation success");
            notifyProtocolReady();
        } else {
            this.mProtocolStatus = 4;
            Rlog.i("RtpTransport", "startNegotiation: header extensions not available; negotiation failed");
            notifyProtocolUnavailable();
        }
    }

    @Override // com.android.internal.telephony.d2d.TransportProtocol
    public void sendMessages(Set<Communicator.Message> set) {
        Set<RtpHeaderExtension> set2 = (Set) set.stream().map(new Function() { // from class: com.android.internal.telephony.d2d.RtpTransport$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.lambda$sendMessages$3((Communicator.Message) obj);
            }
        }).collect(Collectors.toSet());
        Rlog.i("RtpTransport", "sendMessages: sending=" + set);
        this.mRtpAdapter.sendRtpHeaderExtensions(set2);
    }

    @Override // com.android.internal.telephony.d2d.TransportProtocol
    public void forceNegotiated() {
        ArraySet arraySet = this.mSupportedRtpHeaderExtensionTypes;
        if (arraySet == null || arraySet.isEmpty()) {
            this.mSupportedRtpHeaderExtensionTypes.add(DEVICE_STATE_RTP_HEADER_EXTENSION_TYPE);
            this.mSupportedRtpHeaderExtensionTypes.add(CALL_STATE_RTP_HEADER_EXTENSION_TYPE);
        }
        this.mProtocolStatus = 3;
    }

    @Override // com.android.internal.telephony.d2d.TransportProtocol
    public void forceNotNegotiated() {
        this.mProtocolStatus = 1;
    }

    @Override // com.android.internal.telephony.d2d.RtpAdapter.Callback
    public void onRtpHeaderExtensionsReceived(Set<RtpHeaderExtension> set) {
        Set<Communicator.Message> set2 = (Set) set.stream().map(new Function() { // from class: com.android.internal.telephony.d2d.RtpTransport$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.lambda$onRtpHeaderExtensionsReceived$4((RtpHeaderExtension) obj);
            }
        }).filter(new Predicate() { // from class: com.android.internal.telephony.d2d.RtpTransport$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return Objects.nonNull((Communicator.Message) obj);
            }
        }).collect(Collectors.toSet());
        if (set2.size() == 0) {
            return;
        }
        this.mCallback.onMessagesReceived(set2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: extractMessage, reason: merged with bridge method [inline-methods] */
    public Communicator.Message lambda$onRtpHeaderExtensionsReceived$4(final RtpHeaderExtension rtpHeaderExtension) {
        int iIntValue;
        int iIntValue2;
        Optional optionalFindFirst = this.mSupportedRtpHeaderExtensionTypes.stream().filter(new Predicate() { // from class: com.android.internal.telephony.d2d.RtpTransport$$ExternalSyntheticLambda7
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return RtpTransport.m633$r8$lambda$_VU1Ie2JwNtbzmA99Vhl9nEc6o(rtpHeaderExtension, (RtpHeaderExtensionType) obj);
            }
        }).map(new Function() { // from class: com.android.internal.telephony.d2d.RtpTransport$$ExternalSyntheticLambda8
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((RtpHeaderExtensionType) obj).getUri();
            }
        }).findFirst();
        if (!optionalFindFirst.isPresent()) {
            Rlog.w("RtpTransport", "extractMessage: localIdentifier=" + rtpHeaderExtension.getLocalIdentifier() + " not supported.");
            return null;
        }
        if (rtpHeaderExtension.getExtensionData() == null || rtpHeaderExtension.getExtensionData().length != 1) {
            Rlog.w("RtpTransport", "extractMessage: localIdentifier=" + rtpHeaderExtension.getLocalIdentifier() + "  message with invalid data length.");
            return null;
        }
        Uri uri = (Uri) optionalFindFirst.get();
        byte b = (byte) (rtpHeaderExtension.getExtensionData()[0] & 15);
        byte b2 = (byte) (rtpHeaderExtension.getExtensionData()[0] & 240);
        if (DEVICE_STATE_RTP_HEADER_EXTENSION.equals(uri)) {
            Integer num = (Integer) DEVICE_STATE_MSG_TYPE_TO_RTP_BITS.getKey(Byte.valueOf(b));
            if (num == null) {
                Rlog.w("RtpTransport", "extractMessage: localIdentifier=" + rtpHeaderExtension.getLocalIdentifier() + " message with invalid type= " + Integer.toBinaryString(b));
                return null;
            }
            iIntValue = num.intValue();
            if (iIntValue == 3) {
                Integer num2 = (Integer) BATTERY_STATE_VALUE_TO_RTP_BITS.getKey(Byte.valueOf(b2));
                if (num2 == null) {
                    Rlog.w("RtpTransport", "extractMessage: localIdentifier=" + rtpHeaderExtension.getLocalIdentifier() + ", battery state msg with invalid value=" + Integer.toBinaryString(b2));
                    return null;
                }
                iIntValue2 = num2.intValue();
            } else if (iIntValue == 4) {
                Integer num3 = (Integer) NETWORK_COVERAGE_VALUE_TO_RTP_BITS.getKey(Byte.valueOf(b2));
                if (num3 == null) {
                    Rlog.w("RtpTransport", "extractMessage: localIdentifier=" + rtpHeaderExtension.getLocalIdentifier() + ", network coverage msg with invalid value=" + Integer.toBinaryString(b2));
                    return null;
                }
                iIntValue2 = num3.intValue();
            } else {
                Rlog.w("RtpTransport", "messageType=" + Integer.toBinaryString(b) + ", value=" + Integer.toBinaryString(b2) + "; invalid value");
                return null;
            }
        } else if (CALL_STATE_RTP_HEADER_EXTENSION.equals(uri)) {
            Integer num4 = (Integer) CALL_STATE_MSG_TYPE_TO_RTP_BITS.getKey(Byte.valueOf(b));
            if (num4 == null) {
                Rlog.w("RtpTransport", "extractMessage: localIdentifier=" + rtpHeaderExtension.getLocalIdentifier() + ", network coverage msg with invalid type=" + Integer.toBinaryString(b));
                return null;
            }
            iIntValue = num4.intValue();
            if (iIntValue == 1) {
                Integer num5 = (Integer) RAT_VALUE_TO_RTP_BITS.getKey(Byte.valueOf(b2));
                if (num5 == null) {
                    Rlog.w("RtpTransport", "extractMessage: localIdentifier=" + rtpHeaderExtension.getLocalIdentifier() + ", rat type msg with invalid value=" + Integer.toBinaryString(b2));
                    return null;
                }
                iIntValue2 = num5.intValue();
            } else if (iIntValue == 2) {
                Integer num6 = (Integer) CODEC_VALUE_TO_RTP_BITS.getKey(Byte.valueOf(b2));
                if (num6 == null) {
                    Rlog.w("RtpTransport", "extractMessage: localIdentifier=" + rtpHeaderExtension.getLocalIdentifier() + ", audio codec msg with invalid value=" + Integer.toBinaryString(b2));
                    return null;
                }
                iIntValue2 = num6.intValue();
            } else {
                Rlog.w("RtpTransport", "messageType=" + Integer.toBinaryString(b) + ", value=" + Integer.toBinaryString(b2) + "; invalid value");
                return null;
            }
        } else {
            Rlog.w("RtpTransport", "invalid uri=" + uri);
            return null;
        }
        Rlog.i("RtpTransport", "extractMessage: messageType=" + Integer.toBinaryString(b) + ", value=" + Integer.toBinaryString(b2) + " --> message=" + iIntValue + ", value=" + iIntValue2);
        return new Communicator.Message(iIntValue, iIntValue2);
    }

    /* JADX INFO: renamed from: $r8$lambda$_VU1Ie2JwNtbzmA99Vhl9-nEc6o, reason: not valid java name */
    public static /* synthetic */ boolean m633$r8$lambda$_VU1Ie2JwNtbzmA99Vhl9nEc6o(RtpHeaderExtension rtpHeaderExtension, RtpHeaderExtensionType rtpHeaderExtensionType) {
        return rtpHeaderExtensionType.getLocalIdentifier() == rtpHeaderExtension.getLocalIdentifier();
    }

    /* JADX INFO: renamed from: generateRtpHeaderExtension, reason: merged with bridge method [inline-methods] */
    public RtpHeaderExtension lambda$sendMessages$3(Communicator.Message message) {
        byte[] bArr = new byte[1];
        int type = message.getType();
        if (type == 1) {
            byte bByteValue = (byte) (bArr[0] | ((Byte) CALL_STATE_MSG_TYPE_TO_RTP_BITS.getValue(Integer.valueOf(message.getType()))).byteValue());
            bArr[0] = bByteValue;
            bArr[0] = (byte) (((Byte) RAT_VALUE_TO_RTP_BITS.getValue(Integer.valueOf(message.getValue()))).byteValue() | bByteValue);
            return new RtpHeaderExtension(getRtpHeaderExtensionIdentifier(CALL_STATE_RTP_HEADER_EXTENSION), bArr);
        }
        if (type == 2) {
            byte bByteValue2 = (byte) (bArr[0] | ((Byte) CALL_STATE_MSG_TYPE_TO_RTP_BITS.getValue(Integer.valueOf(message.getType()))).byteValue());
            bArr[0] = bByteValue2;
            bArr[0] = (byte) (((Byte) CODEC_VALUE_TO_RTP_BITS.getValue(Integer.valueOf(message.getValue()))).byteValue() | bByteValue2);
            return new RtpHeaderExtension(getRtpHeaderExtensionIdentifier(CALL_STATE_RTP_HEADER_EXTENSION), bArr);
        }
        if (type == 3) {
            byte bByteValue3 = (byte) (bArr[0] | ((Byte) DEVICE_STATE_MSG_TYPE_TO_RTP_BITS.getValue(Integer.valueOf(message.getType()))).byteValue());
            bArr[0] = bByteValue3;
            bArr[0] = (byte) (((Byte) BATTERY_STATE_VALUE_TO_RTP_BITS.getValue(Integer.valueOf(message.getValue()))).byteValue() | bByteValue3);
            return new RtpHeaderExtension(getRtpHeaderExtensionIdentifier(DEVICE_STATE_RTP_HEADER_EXTENSION), bArr);
        }
        if (type != 4) {
            return null;
        }
        byte bByteValue4 = (byte) (bArr[0] | ((Byte) DEVICE_STATE_MSG_TYPE_TO_RTP_BITS.getValue(Integer.valueOf(message.getType()))).byteValue());
        bArr[0] = bByteValue4;
        bArr[0] = (byte) (((Byte) NETWORK_COVERAGE_VALUE_TO_RTP_BITS.getValue(Integer.valueOf(message.getValue()))).byteValue() | bByteValue4);
        return new RtpHeaderExtension(getRtpHeaderExtensionIdentifier(DEVICE_STATE_RTP_HEADER_EXTENSION), bArr);
    }

    private int getRtpHeaderExtensionIdentifier(final Uri uri) {
        return ((RtpHeaderExtensionType) this.mSupportedRtpHeaderExtensionTypes.stream().filter(new Predicate() { // from class: com.android.internal.telephony.d2d.RtpTransport$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((RtpHeaderExtensionType) obj).getUri().equals(uri);
            }
        }).findFirst().get()).getLocalIdentifier();
    }

    private void notifyProtocolReady() {
        TransportProtocol.Callback callback = this.mCallback;
        if (callback != null) {
            callback.onNegotiationSuccess(this);
        }
    }

    private void notifyProtocolUnavailable() {
        TransportProtocol.Callback callback = this.mCallback;
        if (callback != null) {
            callback.onNegotiationFailed(this);
        }
    }
}
