package com.android.internal.telephony.nano;

import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class PersistAtomsProto$SatelliteProvision extends ExtendableMessageNano<PersistAtomsProto$SatelliteProvision> {
    private static volatile PersistAtomsProto$SatelliteProvision[] _emptyArray;
    public int carrierId;
    public boolean isCanceled;
    public boolean isNtnOnlyCarrier;
    public boolean isProvisionRequest;
    public int provisioningTimeSec;
    public int resultCode;
    public int supportedConnectionMode;

    public static PersistAtomsProto$SatelliteProvision[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new PersistAtomsProto$SatelliteProvision[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public PersistAtomsProto$SatelliteProvision() {
        clear();
    }

    public PersistAtomsProto$SatelliteProvision clear() {
        this.resultCode = 0;
        this.provisioningTimeSec = 0;
        this.isProvisionRequest = false;
        this.isCanceled = false;
        this.carrierId = 0;
        this.isNtnOnlyCarrier = false;
        this.supportedConnectionMode = 0;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        int i = this.resultCode;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(1, i);
        }
        int i2 = this.provisioningTimeSec;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(2, i2);
        }
        boolean z = this.isProvisionRequest;
        if (z) {
            codedOutputByteBufferNano.writeBool(3, z);
        }
        boolean z2 = this.isCanceled;
        if (z2) {
            codedOutputByteBufferNano.writeBool(4, z2);
        }
        int i3 = this.carrierId;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(5, i3);
        }
        boolean z3 = this.isNtnOnlyCarrier;
        if (z3) {
            codedOutputByteBufferNano.writeBool(6, z3);
        }
        int i4 = this.supportedConnectionMode;
        if (i4 != 0) {
            codedOutputByteBufferNano.writeInt32(7, i4);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        int i = this.resultCode;
        if (i != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
        }
        int i2 = this.provisioningTimeSec;
        if (i2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i2);
        }
        boolean z = this.isProvisionRequest;
        if (z) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(3, z);
        }
        boolean z2 = this.isCanceled;
        if (z2) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(4, z2);
        }
        int i3 = this.carrierId;
        if (i3 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(5, i3);
        }
        boolean z3 = this.isNtnOnlyCarrier;
        if (z3) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(6, z3);
        }
        int i4 = this.supportedConnectionMode;
        return i4 != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(7, i4) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public PersistAtomsProto$SatelliteProvision mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            if (tag == 0) {
                break;
            }
            if (tag == 8) {
                this.resultCode = codedInputByteBufferNano.readInt32();
            } else if (tag == 16) {
                this.provisioningTimeSec = codedInputByteBufferNano.readInt32();
            } else if (tag == 24) {
                this.isProvisionRequest = codedInputByteBufferNano.readBool();
            } else if (tag == 32) {
                this.isCanceled = codedInputByteBufferNano.readBool();
            } else if (tag == 40) {
                this.carrierId = codedInputByteBufferNano.readInt32();
            } else if (tag == 48) {
                this.isNtnOnlyCarrier = codedInputByteBufferNano.readBool();
            } else if (tag != 56) {
                if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    break;
                }
            } else {
                this.supportedConnectionMode = codedInputByteBufferNano.readInt32();
            }
        }
        return this;
    }

    public static PersistAtomsProto$SatelliteProvision parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (PersistAtomsProto$SatelliteProvision) MessageNano.mergeFrom(new PersistAtomsProto$SatelliteProvision(), bArr);
    }

    public static PersistAtomsProto$SatelliteProvision parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new PersistAtomsProto$SatelliteProvision().mergeFrom(codedInputByteBufferNano);
    }
}
