package com.android.internal.telephony.nano;

import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class TelephonyProto$ImsConnectionState extends ExtendableMessageNano<TelephonyProto$ImsConnectionState> {
    private static volatile TelephonyProto$ImsConnectionState[] _emptyArray;
    public TelephonyProto$ImsReasonInfo reasonInfo;
    public int state;

    public interface State {
        public static final int CONNECTED = 1;
        public static final int DISCONNECTED = 3;
        public static final int PROGRESSING = 2;
        public static final int RESUMED = 4;
        public static final int STATE_UNKNOWN = 0;
        public static final int SUSPENDED = 5;
    }

    public static TelephonyProto$ImsConnectionState[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new TelephonyProto$ImsConnectionState[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public TelephonyProto$ImsConnectionState() {
        clear();
    }

    public TelephonyProto$ImsConnectionState clear() {
        this.state = 0;
        this.reasonInfo = null;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        int i = this.state;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(1, i);
        }
        TelephonyProto$ImsReasonInfo telephonyProto$ImsReasonInfo = this.reasonInfo;
        if (telephonyProto$ImsReasonInfo != null) {
            codedOutputByteBufferNano.writeMessage(2, telephonyProto$ImsReasonInfo);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        int i = this.state;
        if (i != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
        }
        TelephonyProto$ImsReasonInfo telephonyProto$ImsReasonInfo = this.reasonInfo;
        return telephonyProto$ImsReasonInfo != null ? iComputeSerializedSize + CodedOutputByteBufferNano.computeMessageSize(2, telephonyProto$ImsReasonInfo) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public TelephonyProto$ImsConnectionState mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            if (tag == 0) {
                break;
            }
            if (tag == 8) {
                int int32 = codedInputByteBufferNano.readInt32();
                if (int32 == 0 || int32 == 1 || int32 == 2 || int32 == 3 || int32 == 4 || int32 == 5) {
                    this.state = int32;
                }
            } else if (tag != 18) {
                if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    break;
                }
            } else {
                if (this.reasonInfo == null) {
                    this.reasonInfo = new TelephonyProto$ImsReasonInfo();
                }
                codedInputByteBufferNano.readMessage(this.reasonInfo);
            }
        }
        return this;
    }

    public static TelephonyProto$ImsConnectionState parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (TelephonyProto$ImsConnectionState) MessageNano.mergeFrom(new TelephonyProto$ImsConnectionState(), bArr);
    }

    public static TelephonyProto$ImsConnectionState parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new TelephonyProto$ImsConnectionState().mergeFrom(codedInputByteBufferNano);
    }
}
