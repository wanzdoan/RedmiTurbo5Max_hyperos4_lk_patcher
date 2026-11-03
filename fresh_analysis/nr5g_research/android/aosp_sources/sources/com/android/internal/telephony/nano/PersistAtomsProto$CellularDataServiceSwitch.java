package com.android.internal.telephony.nano;

import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class PersistAtomsProto$CellularDataServiceSwitch extends ExtendableMessageNano<PersistAtomsProto$CellularDataServiceSwitch> {
    private static volatile PersistAtomsProto$CellularDataServiceSwitch[] _emptyArray;
    public int carrierId;
    public boolean isMultiSim;
    public boolean isOpportunistic;
    public long lastUsedMillis;
    public int ratFrom;
    public int ratTo;
    public int simSlotIndex;
    public int switchCount;

    public static PersistAtomsProto$CellularDataServiceSwitch[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new PersistAtomsProto$CellularDataServiceSwitch[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public PersistAtomsProto$CellularDataServiceSwitch() {
        clear();
    }

    public PersistAtomsProto$CellularDataServiceSwitch clear() {
        this.ratFrom = 0;
        this.ratTo = 0;
        this.simSlotIndex = 0;
        this.isMultiSim = false;
        this.carrierId = 0;
        this.switchCount = 0;
        this.isOpportunistic = false;
        this.lastUsedMillis = 0L;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        int i = this.ratFrom;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(1, i);
        }
        int i2 = this.ratTo;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(2, i2);
        }
        int i3 = this.simSlotIndex;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(3, i3);
        }
        boolean z = this.isMultiSim;
        if (z) {
            codedOutputByteBufferNano.writeBool(4, z);
        }
        int i4 = this.carrierId;
        if (i4 != 0) {
            codedOutputByteBufferNano.writeInt32(5, i4);
        }
        int i5 = this.switchCount;
        if (i5 != 0) {
            codedOutputByteBufferNano.writeInt32(6, i5);
        }
        boolean z2 = this.isOpportunistic;
        if (z2) {
            codedOutputByteBufferNano.writeBool(7, z2);
        }
        long j = this.lastUsedMillis;
        if (j != 0) {
            codedOutputByteBufferNano.writeInt64(10001, j);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        int i = this.ratFrom;
        if (i != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
        }
        int i2 = this.ratTo;
        if (i2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i2);
        }
        int i3 = this.simSlotIndex;
        if (i3 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, i3);
        }
        boolean z = this.isMultiSim;
        if (z) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(4, z);
        }
        int i4 = this.carrierId;
        if (i4 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(5, i4);
        }
        int i5 = this.switchCount;
        if (i5 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(6, i5);
        }
        boolean z2 = this.isOpportunistic;
        if (z2) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(7, z2);
        }
        long j = this.lastUsedMillis;
        return j != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt64Size(10001, j) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public PersistAtomsProto$CellularDataServiceSwitch mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            if (tag == 0) {
                break;
            }
            if (tag == 8) {
                this.ratFrom = codedInputByteBufferNano.readInt32();
            } else if (tag == 16) {
                this.ratTo = codedInputByteBufferNano.readInt32();
            } else if (tag == 24) {
                this.simSlotIndex = codedInputByteBufferNano.readInt32();
            } else if (tag == 32) {
                this.isMultiSim = codedInputByteBufferNano.readBool();
            } else if (tag == 40) {
                this.carrierId = codedInputByteBufferNano.readInt32();
            } else if (tag == 48) {
                this.switchCount = codedInputByteBufferNano.readInt32();
            } else if (tag == 56) {
                this.isOpportunistic = codedInputByteBufferNano.readBool();
            } else if (tag != 80008) {
                if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    break;
                }
            } else {
                this.lastUsedMillis = codedInputByteBufferNano.readInt64();
            }
        }
        return this;
    }

    public static PersistAtomsProto$CellularDataServiceSwitch parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (PersistAtomsProto$CellularDataServiceSwitch) MessageNano.mergeFrom(new PersistAtomsProto$CellularDataServiceSwitch(), bArr);
    }

    public static PersistAtomsProto$CellularDataServiceSwitch parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new PersistAtomsProto$CellularDataServiceSwitch().mergeFrom(codedInputByteBufferNano);
    }
}
