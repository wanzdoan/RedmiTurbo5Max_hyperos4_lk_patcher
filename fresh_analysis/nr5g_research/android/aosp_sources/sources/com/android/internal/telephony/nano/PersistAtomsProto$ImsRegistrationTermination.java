package com.android.internal.telephony.nano;

import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class PersistAtomsProto$ImsRegistrationTermination extends ExtendableMessageNano<PersistAtomsProto$ImsRegistrationTermination> {
    private static volatile PersistAtomsProto$ImsRegistrationTermination[] _emptyArray;
    public int carrierId;
    public int count;
    public int extraCode;
    public String extraMessage;
    public boolean isIwlanCrossSim;
    public boolean isMultiSim;
    public long lastUsedMillis;
    public int ratAtEnd;
    public int reasonCode;
    public boolean setupFailed;

    public static PersistAtomsProto$ImsRegistrationTermination[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new PersistAtomsProto$ImsRegistrationTermination[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public PersistAtomsProto$ImsRegistrationTermination() {
        clear();
    }

    public PersistAtomsProto$ImsRegistrationTermination clear() {
        this.carrierId = 0;
        this.isMultiSim = false;
        this.ratAtEnd = 0;
        this.setupFailed = false;
        this.reasonCode = 0;
        this.extraCode = 0;
        this.extraMessage = "";
        this.count = 0;
        this.isIwlanCrossSim = false;
        this.lastUsedMillis = 0L;
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
        boolean z = this.isMultiSim;
        if (z) {
            codedOutputByteBufferNano.writeBool(2, z);
        }
        int i2 = this.ratAtEnd;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(3, i2);
        }
        boolean z2 = this.setupFailed;
        if (z2) {
            codedOutputByteBufferNano.writeBool(4, z2);
        }
        int i3 = this.reasonCode;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(5, i3);
        }
        int i4 = this.extraCode;
        if (i4 != 0) {
            codedOutputByteBufferNano.writeInt32(6, i4);
        }
        if (!this.extraMessage.equals("")) {
            codedOutputByteBufferNano.writeString(7, this.extraMessage);
        }
        int i5 = this.count;
        if (i5 != 0) {
            codedOutputByteBufferNano.writeInt32(8, i5);
        }
        boolean z3 = this.isIwlanCrossSim;
        if (z3) {
            codedOutputByteBufferNano.writeBool(9, z3);
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
        int i = this.carrierId;
        if (i != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
        }
        boolean z = this.isMultiSim;
        if (z) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(2, z);
        }
        int i2 = this.ratAtEnd;
        if (i2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, i2);
        }
        boolean z2 = this.setupFailed;
        if (z2) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(4, z2);
        }
        int i3 = this.reasonCode;
        if (i3 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(5, i3);
        }
        int i4 = this.extraCode;
        if (i4 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(6, i4);
        }
        if (!this.extraMessage.equals("")) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(7, this.extraMessage);
        }
        int i5 = this.count;
        if (i5 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(8, i5);
        }
        boolean z3 = this.isIwlanCrossSim;
        if (z3) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(9, z3);
        }
        long j = this.lastUsedMillis;
        return j != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt64Size(10001, j) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public PersistAtomsProto$ImsRegistrationTermination mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            switch (tag) {
                case 0:
                    break;
                case 8:
                    this.carrierId = codedInputByteBufferNano.readInt32();
                    break;
                case 16:
                    this.isMultiSim = codedInputByteBufferNano.readBool();
                    break;
                case 24:
                    this.ratAtEnd = codedInputByteBufferNano.readInt32();
                    break;
                case 32:
                    this.setupFailed = codedInputByteBufferNano.readBool();
                    break;
                case 40:
                    this.reasonCode = codedInputByteBufferNano.readInt32();
                    break;
                case 48:
                    this.extraCode = codedInputByteBufferNano.readInt32();
                    break;
                case 58:
                    this.extraMessage = codedInputByteBufferNano.readString();
                    break;
                case 64:
                    this.count = codedInputByteBufferNano.readInt32();
                    break;
                case 72:
                    this.isIwlanCrossSim = codedInputByteBufferNano.readBool();
                    break;
                case 80008:
                    this.lastUsedMillis = codedInputByteBufferNano.readInt64();
                    break;
                default:
                    if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    }
                    break;
            }
        }
        return this;
    }

    public static PersistAtomsProto$ImsRegistrationTermination parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (PersistAtomsProto$ImsRegistrationTermination) MessageNano.mergeFrom(new PersistAtomsProto$ImsRegistrationTermination(), bArr);
    }

    public static PersistAtomsProto$ImsRegistrationTermination parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new PersistAtomsProto$ImsRegistrationTermination().mergeFrom(codedInputByteBufferNano);
    }
}
