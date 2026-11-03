package com.android.internal.telephony.nano;

import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class PersistAtomsProto$UnmeteredNetworks extends ExtendableMessageNano<PersistAtomsProto$UnmeteredNetworks> {
    private static volatile PersistAtomsProto$UnmeteredNetworks[] _emptyArray;
    public int carrierId;
    public int phoneId;
    public long unmeteredNetworksBitmask;

    public static PersistAtomsProto$UnmeteredNetworks[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new PersistAtomsProto$UnmeteredNetworks[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public PersistAtomsProto$UnmeteredNetworks() {
        clear();
    }

    public PersistAtomsProto$UnmeteredNetworks clear() {
        this.phoneId = 0;
        this.carrierId = 0;
        this.unmeteredNetworksBitmask = 0L;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        int i = this.phoneId;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(1, i);
        }
        int i2 = this.carrierId;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(2, i2);
        }
        long j = this.unmeteredNetworksBitmask;
        if (j != 0) {
            codedOutputByteBufferNano.writeInt64(3, j);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        int i = this.phoneId;
        if (i != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
        }
        int i2 = this.carrierId;
        if (i2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i2);
        }
        long j = this.unmeteredNetworksBitmask;
        return j != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt64Size(3, j) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public PersistAtomsProto$UnmeteredNetworks mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            if (tag == 0) {
                break;
            }
            if (tag == 8) {
                this.phoneId = codedInputByteBufferNano.readInt32();
            } else if (tag == 16) {
                this.carrierId = codedInputByteBufferNano.readInt32();
            } else if (tag != 24) {
                if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    break;
                }
            } else {
                this.unmeteredNetworksBitmask = codedInputByteBufferNano.readInt64();
            }
        }
        return this;
    }

    public static PersistAtomsProto$UnmeteredNetworks parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (PersistAtomsProto$UnmeteredNetworks) MessageNano.mergeFrom(new PersistAtomsProto$UnmeteredNetworks(), bArr);
    }

    public static PersistAtomsProto$UnmeteredNetworks parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new PersistAtomsProto$UnmeteredNetworks().mergeFrom(codedInputByteBufferNano);
    }
}
