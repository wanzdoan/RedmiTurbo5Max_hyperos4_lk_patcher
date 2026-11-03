package com.android.internal.telephony.nano;

import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class PersistAtomsProto$SatelliteConfigUpdater extends ExtendableMessageNano<PersistAtomsProto$SatelliteConfigUpdater> {
    private static volatile PersistAtomsProto$SatelliteConfigUpdater[] _emptyArray;
    public int carrierConfigResult;
    public int configVersion;
    public int count;
    public int oemConfigResult;

    public static PersistAtomsProto$SatelliteConfigUpdater[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new PersistAtomsProto$SatelliteConfigUpdater[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public PersistAtomsProto$SatelliteConfigUpdater() {
        clear();
    }

    public PersistAtomsProto$SatelliteConfigUpdater clear() {
        this.configVersion = 0;
        this.oemConfigResult = 0;
        this.carrierConfigResult = 0;
        this.count = 0;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        int i = this.configVersion;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(1, i);
        }
        int i2 = this.oemConfigResult;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(2, i2);
        }
        int i3 = this.carrierConfigResult;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(3, i3);
        }
        int i4 = this.count;
        if (i4 != 0) {
            codedOutputByteBufferNano.writeInt32(4, i4);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        int i = this.configVersion;
        if (i != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
        }
        int i2 = this.oemConfigResult;
        if (i2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i2);
        }
        int i3 = this.carrierConfigResult;
        if (i3 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, i3);
        }
        int i4 = this.count;
        return i4 != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(4, i4) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public PersistAtomsProto$SatelliteConfigUpdater mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            if (tag == 0) {
                break;
            }
            if (tag == 8) {
                this.configVersion = codedInputByteBufferNano.readInt32();
            } else if (tag == 16) {
                this.oemConfigResult = codedInputByteBufferNano.readInt32();
            } else if (tag == 24) {
                this.carrierConfigResult = codedInputByteBufferNano.readInt32();
            } else if (tag != 32) {
                if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    break;
                }
            } else {
                this.count = codedInputByteBufferNano.readInt32();
            }
        }
        return this;
    }

    public static PersistAtomsProto$SatelliteConfigUpdater parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (PersistAtomsProto$SatelliteConfigUpdater) MessageNano.mergeFrom(new PersistAtomsProto$SatelliteConfigUpdater(), bArr);
    }

    public static PersistAtomsProto$SatelliteConfigUpdater parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new PersistAtomsProto$SatelliteConfigUpdater().mergeFrom(codedInputByteBufferNano);
    }
}
