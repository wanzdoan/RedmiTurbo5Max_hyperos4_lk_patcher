package com.android.internal.telephony.nano;

import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class PersistAtomsProto$GbaEvent extends ExtendableMessageNano<PersistAtomsProto$GbaEvent> {
    private static volatile PersistAtomsProto$GbaEvent[] _emptyArray;
    public int carrierId;
    public int count;
    public int failedReason;
    public int slotId;
    public boolean successful;

    public static PersistAtomsProto$GbaEvent[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new PersistAtomsProto$GbaEvent[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public PersistAtomsProto$GbaEvent() {
        clear();
    }

    public PersistAtomsProto$GbaEvent clear() {
        this.carrierId = 0;
        this.slotId = 0;
        this.successful = false;
        this.failedReason = 0;
        this.count = 0;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        int i = this.carrierId;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(1, i);
        }
        int i2 = this.slotId;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(2, i2);
        }
        boolean z = this.successful;
        if (z) {
            codedOutputByteBufferNano.writeBool(3, z);
        }
        int i3 = this.failedReason;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(4, i3);
        }
        int i4 = this.count;
        if (i4 != 0) {
            codedOutputByteBufferNano.writeInt32(5, i4);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        int i = this.carrierId;
        if (i != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
        }
        int i2 = this.slotId;
        if (i2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i2);
        }
        boolean z = this.successful;
        if (z) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(3, z);
        }
        int i3 = this.failedReason;
        if (i3 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(4, i3);
        }
        int i4 = this.count;
        return i4 != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(5, i4) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public PersistAtomsProto$GbaEvent mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            if (tag == 0) {
                break;
            }
            if (tag == 8) {
                this.carrierId = codedInputByteBufferNano.readInt32();
            } else if (tag == 16) {
                this.slotId = codedInputByteBufferNano.readInt32();
            } else if (tag == 24) {
                this.successful = codedInputByteBufferNano.readBool();
            } else if (tag == 32) {
                this.failedReason = codedInputByteBufferNano.readInt32();
            } else if (tag != 40) {
                if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    break;
                }
            } else {
                this.count = codedInputByteBufferNano.readInt32();
            }
        }
        return this;
    }

    public static PersistAtomsProto$GbaEvent parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (PersistAtomsProto$GbaEvent) MessageNano.mergeFrom(new PersistAtomsProto$GbaEvent(), bArr);
    }

    public static PersistAtomsProto$GbaEvent parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new PersistAtomsProto$GbaEvent().mergeFrom(codedInputByteBufferNano);
    }
}
