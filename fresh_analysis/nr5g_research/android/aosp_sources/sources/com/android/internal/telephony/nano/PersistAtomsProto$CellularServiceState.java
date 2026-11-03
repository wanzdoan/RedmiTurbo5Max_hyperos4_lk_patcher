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
public final class PersistAtomsProto$CellularServiceState extends ExtendableMessageNano<PersistAtomsProto$CellularServiceState> {
    private static volatile PersistAtomsProto$CellularServiceState[] _emptyArray;
    public int carrierId;
    public int dataRat;
    public int dataRoamingType;
    public int foldState;
    public boolean isDataEnabled;
    public boolean isEmergencyOnly;
    public boolean isEndc;
    public boolean isInternetPdnUp;
    public boolean isIwlanCrossSim;
    public boolean isMultiSim;
    public boolean isNbIotNtn;
    public boolean isNtn;
    public boolean isOpportunistic;
    public long lastUsedMillis;
    public boolean overrideVoiceService;
    public String plmn;
    public int simSlotIndex;
    public long totalTimeMillis;
    public int voiceRat;
    public int voiceRoamingType;

    public static PersistAtomsProto$CellularServiceState[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new PersistAtomsProto$CellularServiceState[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public PersistAtomsProto$CellularServiceState() {
        clear();
    }

    public PersistAtomsProto$CellularServiceState clear() {
        this.voiceRat = 0;
        this.dataRat = 0;
        this.voiceRoamingType = 0;
        this.dataRoamingType = 0;
        this.isEndc = false;
        this.simSlotIndex = 0;
        this.isMultiSim = false;
        this.carrierId = 0;
        this.totalTimeMillis = 0L;
        this.isEmergencyOnly = false;
        this.isInternetPdnUp = false;
        this.foldState = 0;
        this.overrideVoiceService = false;
        this.isDataEnabled = false;
        this.isIwlanCrossSim = false;
        this.isNtn = false;
        this.isNbIotNtn = false;
        this.isOpportunistic = false;
        this.plmn = "";
        this.lastUsedMillis = 0L;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        int i = this.voiceRat;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(1, i);
        }
        int i2 = this.dataRat;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(2, i2);
        }
        int i3 = this.voiceRoamingType;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(3, i3);
        }
        int i4 = this.dataRoamingType;
        if (i4 != 0) {
            codedOutputByteBufferNano.writeInt32(4, i4);
        }
        boolean z = this.isEndc;
        if (z) {
            codedOutputByteBufferNano.writeBool(5, z);
        }
        int i5 = this.simSlotIndex;
        if (i5 != 0) {
            codedOutputByteBufferNano.writeInt32(6, i5);
        }
        boolean z2 = this.isMultiSim;
        if (z2) {
            codedOutputByteBufferNano.writeBool(7, z2);
        }
        int i6 = this.carrierId;
        if (i6 != 0) {
            codedOutputByteBufferNano.writeInt32(8, i6);
        }
        long j = this.totalTimeMillis;
        if (j != 0) {
            codedOutputByteBufferNano.writeInt64(9, j);
        }
        boolean z3 = this.isEmergencyOnly;
        if (z3) {
            codedOutputByteBufferNano.writeBool(10, z3);
        }
        boolean z4 = this.isInternetPdnUp;
        if (z4) {
            codedOutputByteBufferNano.writeBool(11, z4);
        }
        int i7 = this.foldState;
        if (i7 != 0) {
            codedOutputByteBufferNano.writeInt32(12, i7);
        }
        boolean z5 = this.overrideVoiceService;
        if (z5) {
            codedOutputByteBufferNano.writeBool(13, z5);
        }
        boolean z6 = this.isDataEnabled;
        if (z6) {
            codedOutputByteBufferNano.writeBool(14, z6);
        }
        boolean z7 = this.isIwlanCrossSim;
        if (z7) {
            codedOutputByteBufferNano.writeBool(15, z7);
        }
        boolean z8 = this.isNtn;
        if (z8) {
            codedOutputByteBufferNano.writeBool(16, z8);
        }
        boolean z9 = this.isNbIotNtn;
        if (z9) {
            codedOutputByteBufferNano.writeBool(17, z9);
        }
        boolean z10 = this.isOpportunistic;
        if (z10) {
            codedOutputByteBufferNano.writeBool(18, z10);
        }
        if (!this.plmn.equals("")) {
            codedOutputByteBufferNano.writeString(19, this.plmn);
        }
        long j2 = this.lastUsedMillis;
        if (j2 != 0) {
            codedOutputByteBufferNano.writeInt64(10001, j2);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        int i = this.voiceRat;
        if (i != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
        }
        int i2 = this.dataRat;
        if (i2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i2);
        }
        int i3 = this.voiceRoamingType;
        if (i3 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, i3);
        }
        int i4 = this.dataRoamingType;
        if (i4 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(4, i4);
        }
        boolean z = this.isEndc;
        if (z) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(5, z);
        }
        int i5 = this.simSlotIndex;
        if (i5 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(6, i5);
        }
        boolean z2 = this.isMultiSim;
        if (z2) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(7, z2);
        }
        int i6 = this.carrierId;
        if (i6 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(8, i6);
        }
        long j = this.totalTimeMillis;
        if (j != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(9, j);
        }
        boolean z3 = this.isEmergencyOnly;
        if (z3) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(10, z3);
        }
        boolean z4 = this.isInternetPdnUp;
        if (z4) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(11, z4);
        }
        int i7 = this.foldState;
        if (i7 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(12, i7);
        }
        boolean z5 = this.overrideVoiceService;
        if (z5) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(13, z5);
        }
        boolean z6 = this.isDataEnabled;
        if (z6) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(14, z6);
        }
        boolean z7 = this.isIwlanCrossSim;
        if (z7) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(15, z7);
        }
        boolean z8 = this.isNtn;
        if (z8) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(16, z8);
        }
        boolean z9 = this.isNbIotNtn;
        if (z9) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(17, z9);
        }
        boolean z10 = this.isOpportunistic;
        if (z10) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(18, z10);
        }
        if (!this.plmn.equals("")) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(19, this.plmn);
        }
        long j2 = this.lastUsedMillis;
        return j2 != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt64Size(10001, j2) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public PersistAtomsProto$CellularServiceState mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            switch (tag) {
                case 0:
                    break;
                case 8:
                    this.voiceRat = codedInputByteBufferNano.readInt32();
                    break;
                case 16:
                    this.dataRat = codedInputByteBufferNano.readInt32();
                    break;
                case 24:
                    this.voiceRoamingType = codedInputByteBufferNano.readInt32();
                    break;
                case 32:
                    this.dataRoamingType = codedInputByteBufferNano.readInt32();
                    break;
                case 40:
                    this.isEndc = codedInputByteBufferNano.readBool();
                    break;
                case 48:
                    this.simSlotIndex = codedInputByteBufferNano.readInt32();
                    break;
                case 56:
                    this.isMultiSim = codedInputByteBufferNano.readBool();
                    break;
                case 64:
                    this.carrierId = codedInputByteBufferNano.readInt32();
                    break;
                case 72:
                    this.totalTimeMillis = codedInputByteBufferNano.readInt64();
                    break;
                case 80:
                    this.isEmergencyOnly = codedInputByteBufferNano.readBool();
                    break;
                case 88:
                    this.isInternetPdnUp = codedInputByteBufferNano.readBool();
                    break;
                case 96:
                    this.foldState = codedInputByteBufferNano.readInt32();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE /* 104 */:
                    this.overrideVoiceService = codedInputByteBufferNano.readBool();
                    break;
                case 112:
                    this.isDataEnabled = codedInputByteBufferNano.readBool();
                    break;
                case 120:
                    this.isIwlanCrossSim = codedInputByteBufferNano.readBool();
                    break;
                case 128:
                    this.isNtn = codedInputByteBufferNano.readBool();
                    break;
                case 136:
                    this.isNbIotNtn = codedInputByteBufferNano.readBool();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_START_KEEPALIVE /* 144 */:
                    this.isOpportunistic = codedInputByteBufferNano.readBool();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_SUPPLY_IMEI /* 154 */:
                    this.plmn = codedInputByteBufferNano.readString();
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

    public static PersistAtomsProto$CellularServiceState parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (PersistAtomsProto$CellularServiceState) MessageNano.mergeFrom(new PersistAtomsProto$CellularServiceState(), bArr);
    }

    public static PersistAtomsProto$CellularServiceState parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new PersistAtomsProto$CellularServiceState().mergeFrom(codedInputByteBufferNano);
    }
}
