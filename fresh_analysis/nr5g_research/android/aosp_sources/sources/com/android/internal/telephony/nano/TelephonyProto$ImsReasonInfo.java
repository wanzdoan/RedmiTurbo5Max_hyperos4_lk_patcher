package com.android.internal.telephony.nano;

import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class TelephonyProto$ImsReasonInfo extends ExtendableMessageNano<TelephonyProto$ImsReasonInfo> {
    private static volatile TelephonyProto$ImsReasonInfo[] _emptyArray;
    public int extraCode;
    public String extraMessage;
    public int reasonCode;

    public static TelephonyProto$ImsReasonInfo[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new TelephonyProto$ImsReasonInfo[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public TelephonyProto$ImsReasonInfo() {
        clear();
    }

    public TelephonyProto$ImsReasonInfo clear() {
        this.reasonCode = 0;
        this.extraCode = 0;
        this.extraMessage = "";
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        int i = this.reasonCode;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(1, i);
        }
        int i2 = this.extraCode;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(2, i2);
        }
        if (!this.extraMessage.equals("")) {
            codedOutputByteBufferNano.writeString(3, this.extraMessage);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        int i = this.reasonCode;
        if (i != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
        }
        int i2 = this.extraCode;
        if (i2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i2);
        }
        return !this.extraMessage.equals("") ? iComputeSerializedSize + CodedOutputByteBufferNano.computeStringSize(3, this.extraMessage) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public TelephonyProto$ImsReasonInfo mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            if (tag == 0) {
                break;
            }
            if (tag == 8) {
                this.reasonCode = codedInputByteBufferNano.readInt32();
            } else if (tag == 16) {
                this.extraCode = codedInputByteBufferNano.readInt32();
            } else if (tag != 26) {
                if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    break;
                }
            } else {
                this.extraMessage = codedInputByteBufferNano.readString();
            }
        }
        return this;
    }

    public static TelephonyProto$ImsReasonInfo parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (TelephonyProto$ImsReasonInfo) MessageNano.mergeFrom(new TelephonyProto$ImsReasonInfo(), bArr);
    }

    public static TelephonyProto$ImsReasonInfo parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new TelephonyProto$ImsReasonInfo().mergeFrom(codedInputByteBufferNano);
    }
}
