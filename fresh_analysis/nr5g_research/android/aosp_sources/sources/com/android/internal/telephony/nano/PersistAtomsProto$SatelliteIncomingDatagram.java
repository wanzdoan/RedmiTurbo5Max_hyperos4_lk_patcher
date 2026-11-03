package com.android.internal.telephony.nano;

import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class PersistAtomsProto$SatelliteIncomingDatagram extends ExtendableMessageNano<PersistAtomsProto$SatelliteIncomingDatagram> {
    private static volatile PersistAtomsProto$SatelliteIncomingDatagram[] _emptyArray;
    public int carrierId;
    public int datagramSizeBytes;
    public long datagramTransferTimeMillis;
    public boolean isDemoMode;
    public boolean isNtnOnlyCarrier;
    public String plmn;
    public int resultCode;
    public int sessionConnectionMode;
    public int supportedConnectionMode;

    public static PersistAtomsProto$SatelliteIncomingDatagram[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new PersistAtomsProto$SatelliteIncomingDatagram[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public PersistAtomsProto$SatelliteIncomingDatagram() {
        clear();
    }

    public PersistAtomsProto$SatelliteIncomingDatagram clear() {
        this.resultCode = 0;
        this.datagramSizeBytes = 0;
        this.datagramTransferTimeMillis = 0L;
        this.isDemoMode = false;
        this.carrierId = 0;
        this.isNtnOnlyCarrier = false;
        this.supportedConnectionMode = 0;
        this.sessionConnectionMode = 0;
        this.plmn = "";
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
        int i2 = this.datagramSizeBytes;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(2, i2);
        }
        long j = this.datagramTransferTimeMillis;
        if (j != 0) {
            codedOutputByteBufferNano.writeInt64(3, j);
        }
        boolean z = this.isDemoMode;
        if (z) {
            codedOutputByteBufferNano.writeBool(4, z);
        }
        int i3 = this.carrierId;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(5, i3);
        }
        boolean z2 = this.isNtnOnlyCarrier;
        if (z2) {
            codedOutputByteBufferNano.writeBool(6, z2);
        }
        int i4 = this.supportedConnectionMode;
        if (i4 != 0) {
            codedOutputByteBufferNano.writeInt32(7, i4);
        }
        int i5 = this.sessionConnectionMode;
        if (i5 != 0) {
            codedOutputByteBufferNano.writeInt32(8, i5);
        }
        if (!this.plmn.equals("")) {
            codedOutputByteBufferNano.writeString(9, this.plmn);
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
        int i2 = this.datagramSizeBytes;
        if (i2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i2);
        }
        long j = this.datagramTransferTimeMillis;
        if (j != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(3, j);
        }
        boolean z = this.isDemoMode;
        if (z) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(4, z);
        }
        int i3 = this.carrierId;
        if (i3 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(5, i3);
        }
        boolean z2 = this.isNtnOnlyCarrier;
        if (z2) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(6, z2);
        }
        int i4 = this.supportedConnectionMode;
        if (i4 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(7, i4);
        }
        int i5 = this.sessionConnectionMode;
        if (i5 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(8, i5);
        }
        return !this.plmn.equals("") ? iComputeSerializedSize + CodedOutputByteBufferNano.computeStringSize(9, this.plmn) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public PersistAtomsProto$SatelliteIncomingDatagram mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            if (tag == 0) {
                break;
            }
            if (tag == 8) {
                this.resultCode = codedInputByteBufferNano.readInt32();
            } else if (tag == 16) {
                this.datagramSizeBytes = codedInputByteBufferNano.readInt32();
            } else if (tag == 24) {
                this.datagramTransferTimeMillis = codedInputByteBufferNano.readInt64();
            } else if (tag == 32) {
                this.isDemoMode = codedInputByteBufferNano.readBool();
            } else if (tag == 40) {
                this.carrierId = codedInputByteBufferNano.readInt32();
            } else if (tag == 48) {
                this.isNtnOnlyCarrier = codedInputByteBufferNano.readBool();
            } else if (tag == 56) {
                this.supportedConnectionMode = codedInputByteBufferNano.readInt32();
            } else if (tag == 64) {
                this.sessionConnectionMode = codedInputByteBufferNano.readInt32();
            } else if (tag != 74) {
                if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    break;
                }
            } else {
                this.plmn = codedInputByteBufferNano.readString();
            }
        }
        return this;
    }

    public static PersistAtomsProto$SatelliteIncomingDatagram parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (PersistAtomsProto$SatelliteIncomingDatagram) MessageNano.mergeFrom(new PersistAtomsProto$SatelliteIncomingDatagram(), bArr);
    }

    public static PersistAtomsProto$SatelliteIncomingDatagram parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new PersistAtomsProto$SatelliteIncomingDatagram().mergeFrom(codedInputByteBufferNano);
    }
}
