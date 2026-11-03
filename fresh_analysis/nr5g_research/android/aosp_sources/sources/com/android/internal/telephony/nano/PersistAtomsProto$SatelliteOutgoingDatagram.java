package com.android.internal.telephony.nano;

import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class PersistAtomsProto$SatelliteOutgoingDatagram extends ExtendableMessageNano<PersistAtomsProto$SatelliteOutgoingDatagram> {
    private static volatile PersistAtomsProto$SatelliteOutgoingDatagram[] _emptyArray;
    public int carrierId;
    public int datagramSizeBytes;
    public long datagramTransferTimeMillis;
    public int datagramType;
    public boolean isDemoMode;
    public boolean isNtnOnlyCarrier;
    public String plmn;
    public int resultCode;
    public int sessionConnectionMode;
    public int supportedConnectionMode;

    public static PersistAtomsProto$SatelliteOutgoingDatagram[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new PersistAtomsProto$SatelliteOutgoingDatagram[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public PersistAtomsProto$SatelliteOutgoingDatagram() {
        clear();
    }

    public PersistAtomsProto$SatelliteOutgoingDatagram clear() {
        this.datagramType = 0;
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
        int i = this.datagramType;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(1, i);
        }
        int i2 = this.resultCode;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(2, i2);
        }
        int i3 = this.datagramSizeBytes;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(3, i3);
        }
        long j = this.datagramTransferTimeMillis;
        if (j != 0) {
            codedOutputByteBufferNano.writeInt64(4, j);
        }
        boolean z = this.isDemoMode;
        if (z) {
            codedOutputByteBufferNano.writeBool(5, z);
        }
        int i4 = this.carrierId;
        if (i4 != 0) {
            codedOutputByteBufferNano.writeInt32(6, i4);
        }
        boolean z2 = this.isNtnOnlyCarrier;
        if (z2) {
            codedOutputByteBufferNano.writeBool(7, z2);
        }
        int i5 = this.supportedConnectionMode;
        if (i5 != 0) {
            codedOutputByteBufferNano.writeInt32(8, i5);
        }
        int i6 = this.sessionConnectionMode;
        if (i6 != 0) {
            codedOutputByteBufferNano.writeInt32(9, i6);
        }
        if (!this.plmn.equals("")) {
            codedOutputByteBufferNano.writeString(10, this.plmn);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        int i = this.datagramType;
        if (i != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
        }
        int i2 = this.resultCode;
        if (i2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i2);
        }
        int i3 = this.datagramSizeBytes;
        if (i3 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, i3);
        }
        long j = this.datagramTransferTimeMillis;
        if (j != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(4, j);
        }
        boolean z = this.isDemoMode;
        if (z) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(5, z);
        }
        int i4 = this.carrierId;
        if (i4 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(6, i4);
        }
        boolean z2 = this.isNtnOnlyCarrier;
        if (z2) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(7, z2);
        }
        int i5 = this.supportedConnectionMode;
        if (i5 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(8, i5);
        }
        int i6 = this.sessionConnectionMode;
        if (i6 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(9, i6);
        }
        return !this.plmn.equals("") ? iComputeSerializedSize + CodedOutputByteBufferNano.computeStringSize(10, this.plmn) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public PersistAtomsProto$SatelliteOutgoingDatagram mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            switch (tag) {
                case 0:
                    break;
                case 8:
                    this.datagramType = codedInputByteBufferNano.readInt32();
                    break;
                case 16:
                    this.resultCode = codedInputByteBufferNano.readInt32();
                    break;
                case 24:
                    this.datagramSizeBytes = codedInputByteBufferNano.readInt32();
                    break;
                case 32:
                    this.datagramTransferTimeMillis = codedInputByteBufferNano.readInt64();
                    break;
                case 40:
                    this.isDemoMode = codedInputByteBufferNano.readBool();
                    break;
                case 48:
                    this.carrierId = codedInputByteBufferNano.readInt32();
                    break;
                case 56:
                    this.isNtnOnlyCarrier = codedInputByteBufferNano.readBool();
                    break;
                case 64:
                    this.supportedConnectionMode = codedInputByteBufferNano.readInt32();
                    break;
                case 72:
                    this.sessionConnectionMode = codedInputByteBufferNano.readInt32();
                    break;
                case 82:
                    this.plmn = codedInputByteBufferNano.readString();
                    break;
                default:
                    if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    }
                    break;
            }
        }
        return this;
    }

    public static PersistAtomsProto$SatelliteOutgoingDatagram parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (PersistAtomsProto$SatelliteOutgoingDatagram) MessageNano.mergeFrom(new PersistAtomsProto$SatelliteOutgoingDatagram(), bArr);
    }

    public static PersistAtomsProto$SatelliteOutgoingDatagram parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new PersistAtomsProto$SatelliteOutgoingDatagram().mergeFrom(codedInputByteBufferNano);
    }
}
