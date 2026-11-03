package com.android.internal.telephony.nano;

import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class PersistAtomsProto$SipDelegateStats extends ExtendableMessageNano<PersistAtomsProto$SipDelegateStats> {
    private static volatile PersistAtomsProto$SipDelegateStats[] _emptyArray;
    public int carrierId;
    public int destroyReason;
    public int dimension;
    public int slotId;
    public long uptimeMillis;

    public static PersistAtomsProto$SipDelegateStats[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new PersistAtomsProto$SipDelegateStats[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public PersistAtomsProto$SipDelegateStats() {
        clear();
    }

    public PersistAtomsProto$SipDelegateStats clear() {
        this.dimension = 0;
        this.carrierId = 0;
        this.slotId = 0;
        this.destroyReason = 0;
        this.uptimeMillis = 0L;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        int i = this.dimension;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(1, i);
        }
        int i2 = this.carrierId;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(2, i2);
        }
        int i3 = this.slotId;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(3, i3);
        }
        int i4 = this.destroyReason;
        if (i4 != 0) {
            codedOutputByteBufferNano.writeInt32(4, i4);
        }
        long j = this.uptimeMillis;
        if (j != 0) {
            codedOutputByteBufferNano.writeInt64(5, j);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        int i = this.dimension;
        if (i != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
        }
        int i2 = this.carrierId;
        if (i2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i2);
        }
        int i3 = this.slotId;
        if (i3 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, i3);
        }
        int i4 = this.destroyReason;
        if (i4 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(4, i4);
        }
        long j = this.uptimeMillis;
        return j != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt64Size(5, j) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public PersistAtomsProto$SipDelegateStats mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            if (tag == 0) {
                break;
            }
            if (tag == 8) {
                this.dimension = codedInputByteBufferNano.readInt32();
            } else if (tag == 16) {
                this.carrierId = codedInputByteBufferNano.readInt32();
            } else if (tag == 24) {
                this.slotId = codedInputByteBufferNano.readInt32();
            } else if (tag == 32) {
                this.destroyReason = codedInputByteBufferNano.readInt32();
            } else if (tag != 40) {
                if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    break;
                }
            } else {
                this.uptimeMillis = codedInputByteBufferNano.readInt64();
            }
        }
        return this;
    }

    public static PersistAtomsProto$SipDelegateStats parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (PersistAtomsProto$SipDelegateStats) MessageNano.mergeFrom(new PersistAtomsProto$SipDelegateStats(), bArr);
    }

    public static PersistAtomsProto$SipDelegateStats parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new PersistAtomsProto$SipDelegateStats().mergeFrom(codedInputByteBufferNano);
    }
}
