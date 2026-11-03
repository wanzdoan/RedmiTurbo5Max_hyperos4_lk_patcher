package com.android.internal.telephony.nano;

import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class PersistAtomsProto$ImsRegistrationStats extends ExtendableMessageNano<PersistAtomsProto$ImsRegistrationStats> {
    private static volatile PersistAtomsProto$ImsRegistrationStats[] _emptyArray;
    public int carrierId;
    public boolean isIwlanCrossSim;
    public long lastUsedMillis;
    public int rat;
    public long registeredMillis;
    public int registeredTimes;
    public long registeringMillis;
    public int simSlotIndex;
    public long smsAvailableMillis;
    public long smsCapableMillis;
    public long unregisteredMillis;
    public long utAvailableMillis;
    public long utCapableMillis;
    public long videoAvailableMillis;
    public long videoCapableMillis;
    public long voiceAvailableMillis;
    public long voiceCapableMillis;

    public static PersistAtomsProto$ImsRegistrationStats[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new PersistAtomsProto$ImsRegistrationStats[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public PersistAtomsProto$ImsRegistrationStats() {
        clear();
    }

    public PersistAtomsProto$ImsRegistrationStats clear() {
        this.carrierId = 0;
        this.simSlotIndex = 0;
        this.rat = 0;
        this.registeredMillis = 0L;
        this.voiceCapableMillis = 0L;
        this.voiceAvailableMillis = 0L;
        this.smsCapableMillis = 0L;
        this.smsAvailableMillis = 0L;
        this.videoCapableMillis = 0L;
        this.videoAvailableMillis = 0L;
        this.utCapableMillis = 0L;
        this.utAvailableMillis = 0L;
        this.registeringMillis = 0L;
        this.unregisteredMillis = 0L;
        this.isIwlanCrossSim = false;
        this.registeredTimes = 0;
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
        int i2 = this.simSlotIndex;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(2, i2);
        }
        int i3 = this.rat;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(3, i3);
        }
        long j = this.registeredMillis;
        if (j != 0) {
            codedOutputByteBufferNano.writeInt64(4, j);
        }
        long j2 = this.voiceCapableMillis;
        if (j2 != 0) {
            codedOutputByteBufferNano.writeInt64(5, j2);
        }
        long j3 = this.voiceAvailableMillis;
        if (j3 != 0) {
            codedOutputByteBufferNano.writeInt64(6, j3);
        }
        long j4 = this.smsCapableMillis;
        if (j4 != 0) {
            codedOutputByteBufferNano.writeInt64(7, j4);
        }
        long j5 = this.smsAvailableMillis;
        if (j5 != 0) {
            codedOutputByteBufferNano.writeInt64(8, j5);
        }
        long j6 = this.videoCapableMillis;
        if (j6 != 0) {
            codedOutputByteBufferNano.writeInt64(9, j6);
        }
        long j7 = this.videoAvailableMillis;
        if (j7 != 0) {
            codedOutputByteBufferNano.writeInt64(10, j7);
        }
        long j8 = this.utCapableMillis;
        if (j8 != 0) {
            codedOutputByteBufferNano.writeInt64(11, j8);
        }
        long j9 = this.utAvailableMillis;
        if (j9 != 0) {
            codedOutputByteBufferNano.writeInt64(12, j9);
        }
        long j10 = this.registeringMillis;
        if (j10 != 0) {
            codedOutputByteBufferNano.writeInt64(13, j10);
        }
        long j11 = this.unregisteredMillis;
        if (j11 != 0) {
            codedOutputByteBufferNano.writeInt64(14, j11);
        }
        boolean z = this.isIwlanCrossSim;
        if (z) {
            codedOutputByteBufferNano.writeBool(15, z);
        }
        int i4 = this.registeredTimes;
        if (i4 != 0) {
            codedOutputByteBufferNano.writeInt32(17, i4);
        }
        long j12 = this.lastUsedMillis;
        if (j12 != 0) {
            codedOutputByteBufferNano.writeInt64(10001, j12);
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
        int i2 = this.simSlotIndex;
        if (i2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i2);
        }
        int i3 = this.rat;
        if (i3 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, i3);
        }
        long j = this.registeredMillis;
        if (j != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(4, j);
        }
        long j2 = this.voiceCapableMillis;
        if (j2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(5, j2);
        }
        long j3 = this.voiceAvailableMillis;
        if (j3 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(6, j3);
        }
        long j4 = this.smsCapableMillis;
        if (j4 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(7, j4);
        }
        long j5 = this.smsAvailableMillis;
        if (j5 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(8, j5);
        }
        long j6 = this.videoCapableMillis;
        if (j6 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(9, j6);
        }
        long j7 = this.videoAvailableMillis;
        if (j7 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(10, j7);
        }
        long j8 = this.utCapableMillis;
        if (j8 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(11, j8);
        }
        long j9 = this.utAvailableMillis;
        if (j9 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(12, j9);
        }
        long j10 = this.registeringMillis;
        if (j10 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(13, j10);
        }
        long j11 = this.unregisteredMillis;
        if (j11 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(14, j11);
        }
        boolean z = this.isIwlanCrossSim;
        if (z) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(15, z);
        }
        int i4 = this.registeredTimes;
        if (i4 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(17, i4);
        }
        long j12 = this.lastUsedMillis;
        return j12 != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt64Size(10001, j12) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public PersistAtomsProto$ImsRegistrationStats mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            switch (tag) {
                case 0:
                    break;
                case 8:
                    this.carrierId = codedInputByteBufferNano.readInt32();
                    break;
                case 16:
                    this.simSlotIndex = codedInputByteBufferNano.readInt32();
                    break;
                case 24:
                    this.rat = codedInputByteBufferNano.readInt32();
                    break;
                case 32:
                    this.registeredMillis = codedInputByteBufferNano.readInt64();
                    break;
                case 40:
                    this.voiceCapableMillis = codedInputByteBufferNano.readInt64();
                    break;
                case 48:
                    this.voiceAvailableMillis = codedInputByteBufferNano.readInt64();
                    break;
                case 56:
                    this.smsCapableMillis = codedInputByteBufferNano.readInt64();
                    break;
                case 64:
                    this.smsAvailableMillis = codedInputByteBufferNano.readInt64();
                    break;
                case 72:
                    this.videoCapableMillis = codedInputByteBufferNano.readInt64();
                    break;
                case 80:
                    this.videoAvailableMillis = codedInputByteBufferNano.readInt64();
                    break;
                case 88:
                    this.utCapableMillis = codedInputByteBufferNano.readInt64();
                    break;
                case 96:
                    this.utAvailableMillis = codedInputByteBufferNano.readInt64();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE /* 104 */:
                    this.registeringMillis = codedInputByteBufferNano.readInt64();
                    break;
                case 112:
                    this.unregisteredMillis = codedInputByteBufferNano.readInt64();
                    break;
                case 120:
                    this.isIwlanCrossSim = codedInputByteBufferNano.readBool();
                    break;
                case 136:
                    this.registeredTimes = codedInputByteBufferNano.readInt32();
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

    public static PersistAtomsProto$ImsRegistrationStats parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (PersistAtomsProto$ImsRegistrationStats) MessageNano.mergeFrom(new PersistAtomsProto$ImsRegistrationStats(), bArr);
    }

    public static PersistAtomsProto$ImsRegistrationStats parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new PersistAtomsProto$ImsRegistrationStats().mergeFrom(codedInputByteBufferNano);
    }
}
