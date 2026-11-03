package com.android.internal.telephony.nano;

import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class PersistAtomsProto$SipTransportFeatureTagStats extends ExtendableMessageNano<PersistAtomsProto$SipTransportFeatureTagStats> {
    private static volatile PersistAtomsProto$SipTransportFeatureTagStats[] _emptyArray;
    public long associatedMillis;
    public int carrierId;
    public int featureTagName;
    public int sipTransportDeniedReason;
    public int sipTransportDeregisteredReason;
    public int slotId;

    public static PersistAtomsProto$SipTransportFeatureTagStats[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new PersistAtomsProto$SipTransportFeatureTagStats[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public PersistAtomsProto$SipTransportFeatureTagStats() {
        clear();
    }

    public PersistAtomsProto$SipTransportFeatureTagStats clear() {
        this.carrierId = 0;
        this.slotId = 0;
        this.featureTagName = 0;
        this.sipTransportDeniedReason = 0;
        this.sipTransportDeregisteredReason = 0;
        this.associatedMillis = 0L;
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
        int i3 = this.featureTagName;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(3, i3);
        }
        int i4 = this.sipTransportDeniedReason;
        if (i4 != 0) {
            codedOutputByteBufferNano.writeInt32(4, i4);
        }
        int i5 = this.sipTransportDeregisteredReason;
        if (i5 != 0) {
            codedOutputByteBufferNano.writeInt32(5, i5);
        }
        long j = this.associatedMillis;
        if (j != 0) {
            codedOutputByteBufferNano.writeInt64(6, j);
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
        int i3 = this.featureTagName;
        if (i3 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, i3);
        }
        int i4 = this.sipTransportDeniedReason;
        if (i4 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(4, i4);
        }
        int i5 = this.sipTransportDeregisteredReason;
        if (i5 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(5, i5);
        }
        long j = this.associatedMillis;
        return j != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt64Size(6, j) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public PersistAtomsProto$SipTransportFeatureTagStats mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
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
                this.featureTagName = codedInputByteBufferNano.readInt32();
            } else if (tag == 32) {
                this.sipTransportDeniedReason = codedInputByteBufferNano.readInt32();
            } else if (tag == 40) {
                this.sipTransportDeregisteredReason = codedInputByteBufferNano.readInt32();
            } else if (tag != 48) {
                if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    break;
                }
            } else {
                this.associatedMillis = codedInputByteBufferNano.readInt64();
            }
        }
        return this;
    }

    public static PersistAtomsProto$SipTransportFeatureTagStats parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (PersistAtomsProto$SipTransportFeatureTagStats) MessageNano.mergeFrom(new PersistAtomsProto$SipTransportFeatureTagStats(), bArr);
    }

    public static PersistAtomsProto$SipTransportFeatureTagStats parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new PersistAtomsProto$SipTransportFeatureTagStats().mergeFrom(codedInputByteBufferNano);
    }
}
