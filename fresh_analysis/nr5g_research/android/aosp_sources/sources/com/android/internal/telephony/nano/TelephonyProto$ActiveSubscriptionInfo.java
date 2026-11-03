package com.android.internal.telephony.nano;

import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class TelephonyProto$ActiveSubscriptionInfo extends ExtendableMessageNano<TelephonyProto$ActiveSubscriptionInfo> {
    private static volatile TelephonyProto$ActiveSubscriptionInfo[] _emptyArray;
    public int carrierId;
    public int isOpportunistic;
    public String simMccmnc;
    public int slotIndex;

    public static TelephonyProto$ActiveSubscriptionInfo[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new TelephonyProto$ActiveSubscriptionInfo[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public TelephonyProto$ActiveSubscriptionInfo() {
        clear();
    }

    public TelephonyProto$ActiveSubscriptionInfo clear() {
        this.slotIndex = 0;
        this.carrierId = 0;
        this.isOpportunistic = 0;
        this.simMccmnc = "";
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        int i = this.slotIndex;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(1, i);
        }
        int i2 = this.carrierId;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(2, i2);
        }
        int i3 = this.isOpportunistic;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(3, i3);
        }
        if (!this.simMccmnc.equals("")) {
            codedOutputByteBufferNano.writeString(4, this.simMccmnc);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        int i = this.slotIndex;
        if (i != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
        }
        int i2 = this.carrierId;
        if (i2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i2);
        }
        int i3 = this.isOpportunistic;
        if (i3 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, i3);
        }
        return !this.simMccmnc.equals("") ? iComputeSerializedSize + CodedOutputByteBufferNano.computeStringSize(4, this.simMccmnc) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public TelephonyProto$ActiveSubscriptionInfo mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            if (tag == 0) {
                break;
            }
            if (tag == 8) {
                this.slotIndex = codedInputByteBufferNano.readInt32();
            } else if (tag == 16) {
                this.carrierId = codedInputByteBufferNano.readInt32();
            } else if (tag == 24) {
                this.isOpportunistic = codedInputByteBufferNano.readInt32();
            } else if (tag != 34) {
                if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    break;
                }
            } else {
                this.simMccmnc = codedInputByteBufferNano.readString();
            }
        }
        return this;
    }

    public static TelephonyProto$ActiveSubscriptionInfo parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (TelephonyProto$ActiveSubscriptionInfo) MessageNano.mergeFrom(new TelephonyProto$ActiveSubscriptionInfo(), bArr);
    }

    public static TelephonyProto$ActiveSubscriptionInfo parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new TelephonyProto$ActiveSubscriptionInfo().mergeFrom(codedInputByteBufferNano);
    }
}
