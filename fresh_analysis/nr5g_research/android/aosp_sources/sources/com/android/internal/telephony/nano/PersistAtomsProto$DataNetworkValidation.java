package com.android.internal.telephony.nano;

import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class PersistAtomsProto$DataNetworkValidation extends ExtendableMessageNano<PersistAtomsProto$DataNetworkValidation> {
    private static volatile PersistAtomsProto$DataNetworkValidation[] _emptyArray;
    public int apnTypeBitmask;
    public long elapsedTimeInMillis;
    public boolean handoverAttempted;
    public int networkType;
    public int networkValidationCount;
    public int signalStrength;
    public int validationResult;

    public static PersistAtomsProto$DataNetworkValidation[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new PersistAtomsProto$DataNetworkValidation[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public PersistAtomsProto$DataNetworkValidation() {
        clear();
    }

    public PersistAtomsProto$DataNetworkValidation clear() {
        this.networkType = 0;
        this.apnTypeBitmask = 0;
        this.signalStrength = 0;
        this.validationResult = 0;
        this.elapsedTimeInMillis = 0L;
        this.handoverAttempted = false;
        this.networkValidationCount = 0;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        int i = this.networkType;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(1, i);
        }
        int i2 = this.apnTypeBitmask;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(2, i2);
        }
        int i3 = this.signalStrength;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(3, i3);
        }
        int i4 = this.validationResult;
        if (i4 != 0) {
            codedOutputByteBufferNano.writeInt32(4, i4);
        }
        long j = this.elapsedTimeInMillis;
        if (j != 0) {
            codedOutputByteBufferNano.writeInt64(5, j);
        }
        boolean z = this.handoverAttempted;
        if (z) {
            codedOutputByteBufferNano.writeBool(6, z);
        }
        int i5 = this.networkValidationCount;
        if (i5 != 0) {
            codedOutputByteBufferNano.writeInt32(7, i5);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        int i = this.networkType;
        if (i != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
        }
        int i2 = this.apnTypeBitmask;
        if (i2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i2);
        }
        int i3 = this.signalStrength;
        if (i3 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, i3);
        }
        int i4 = this.validationResult;
        if (i4 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(4, i4);
        }
        long j = this.elapsedTimeInMillis;
        if (j != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(5, j);
        }
        boolean z = this.handoverAttempted;
        if (z) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(6, z);
        }
        int i5 = this.networkValidationCount;
        return i5 != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(7, i5) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public PersistAtomsProto$DataNetworkValidation mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            if (tag == 0) {
                break;
            }
            if (tag == 8) {
                this.networkType = codedInputByteBufferNano.readInt32();
            } else if (tag == 16) {
                this.apnTypeBitmask = codedInputByteBufferNano.readInt32();
            } else if (tag == 24) {
                this.signalStrength = codedInputByteBufferNano.readInt32();
            } else if (tag == 32) {
                this.validationResult = codedInputByteBufferNano.readInt32();
            } else if (tag == 40) {
                this.elapsedTimeInMillis = codedInputByteBufferNano.readInt64();
            } else if (tag == 48) {
                this.handoverAttempted = codedInputByteBufferNano.readBool();
            } else if (tag != 56) {
                if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    break;
                }
            } else {
                this.networkValidationCount = codedInputByteBufferNano.readInt32();
            }
        }
        return this;
    }

    public static PersistAtomsProto$DataNetworkValidation parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (PersistAtomsProto$DataNetworkValidation) MessageNano.mergeFrom(new PersistAtomsProto$DataNetworkValidation(), bArr);
    }

    public static PersistAtomsProto$DataNetworkValidation parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new PersistAtomsProto$DataNetworkValidation().mergeFrom(codedInputByteBufferNano);
    }
}
