package com.android.internal.telephony.nano;

import android.telephony.gsm.SmsMessage;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class PersistAtomsProto$IncomingSms extends ExtendableMessageNano<PersistAtomsProto$IncomingSms> {
    private static volatile PersistAtomsProto$IncomingSms[] _emptyArray;
    public boolean blocked;
    public int carrierId;
    public int count;
    public int error;
    public int hashCode;
    public boolean isEmergency;
    public boolean isEsim;
    public boolean isManagedProfile;
    public boolean isMultiSim;
    public boolean isNbIotNtn;
    public boolean isNtn;
    public boolean isRoaming;
    public long messageId;
    public int pduLength;
    public String plmn;
    public int rat;
    public int receivedParts;
    public int simSlotIndex;
    public int smsFormat;
    public int smsTech;
    public int smsType;
    public int totalParts;

    public static PersistAtomsProto$IncomingSms[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new PersistAtomsProto$IncomingSms[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public PersistAtomsProto$IncomingSms() {
        clear();
    }

    public PersistAtomsProto$IncomingSms clear() {
        this.smsFormat = 0;
        this.smsTech = 0;
        this.rat = 0;
        this.smsType = 0;
        this.totalParts = 0;
        this.receivedParts = 0;
        this.blocked = false;
        this.error = 0;
        this.isRoaming = false;
        this.simSlotIndex = 0;
        this.isMultiSim = false;
        this.isEsim = false;
        this.carrierId = 0;
        this.messageId = 0L;
        this.count = 0;
        this.isManagedProfile = false;
        this.isNtn = false;
        this.isEmergency = false;
        this.isNbIotNtn = false;
        this.pduLength = 0;
        this.plmn = "";
        this.hashCode = 0;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        int i = this.smsFormat;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(1, i);
        }
        int i2 = this.smsTech;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(2, i2);
        }
        int i3 = this.rat;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(3, i3);
        }
        int i4 = this.smsType;
        if (i4 != 0) {
            codedOutputByteBufferNano.writeInt32(4, i4);
        }
        int i5 = this.totalParts;
        if (i5 != 0) {
            codedOutputByteBufferNano.writeInt32(5, i5);
        }
        int i6 = this.receivedParts;
        if (i6 != 0) {
            codedOutputByteBufferNano.writeInt32(6, i6);
        }
        boolean z = this.blocked;
        if (z) {
            codedOutputByteBufferNano.writeBool(7, z);
        }
        int i7 = this.error;
        if (i7 != 0) {
            codedOutputByteBufferNano.writeInt32(8, i7);
        }
        boolean z2 = this.isRoaming;
        if (z2) {
            codedOutputByteBufferNano.writeBool(9, z2);
        }
        int i8 = this.simSlotIndex;
        if (i8 != 0) {
            codedOutputByteBufferNano.writeInt32(10, i8);
        }
        boolean z3 = this.isMultiSim;
        if (z3) {
            codedOutputByteBufferNano.writeBool(11, z3);
        }
        boolean z4 = this.isEsim;
        if (z4) {
            codedOutputByteBufferNano.writeBool(12, z4);
        }
        int i9 = this.carrierId;
        if (i9 != 0) {
            codedOutputByteBufferNano.writeInt32(13, i9);
        }
        long j = this.messageId;
        if (j != 0) {
            codedOutputByteBufferNano.writeInt64(14, j);
        }
        int i10 = this.count;
        if (i10 != 0) {
            codedOutputByteBufferNano.writeInt32(15, i10);
        }
        boolean z5 = this.isManagedProfile;
        if (z5) {
            codedOutputByteBufferNano.writeBool(16, z5);
        }
        boolean z6 = this.isNtn;
        if (z6) {
            codedOutputByteBufferNano.writeBool(17, z6);
        }
        boolean z7 = this.isEmergency;
        if (z7) {
            codedOutputByteBufferNano.writeBool(18, z7);
        }
        boolean z8 = this.isNbIotNtn;
        if (z8) {
            codedOutputByteBufferNano.writeBool(19, z8);
        }
        int i11 = this.pduLength;
        if (i11 != 0) {
            codedOutputByteBufferNano.writeInt32(20, i11);
        }
        if (!this.plmn.equals("")) {
            codedOutputByteBufferNano.writeString(21, this.plmn);
        }
        int i12 = this.hashCode;
        if (i12 != 0) {
            codedOutputByteBufferNano.writeInt32(10001, i12);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        int i = this.smsFormat;
        if (i != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
        }
        int i2 = this.smsTech;
        if (i2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i2);
        }
        int i3 = this.rat;
        if (i3 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, i3);
        }
        int i4 = this.smsType;
        if (i4 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(4, i4);
        }
        int i5 = this.totalParts;
        if (i5 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(5, i5);
        }
        int i6 = this.receivedParts;
        if (i6 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(6, i6);
        }
        boolean z = this.blocked;
        if (z) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(7, z);
        }
        int i7 = this.error;
        if (i7 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(8, i7);
        }
        boolean z2 = this.isRoaming;
        if (z2) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(9, z2);
        }
        int i8 = this.simSlotIndex;
        if (i8 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(10, i8);
        }
        boolean z3 = this.isMultiSim;
        if (z3) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(11, z3);
        }
        boolean z4 = this.isEsim;
        if (z4) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(12, z4);
        }
        int i9 = this.carrierId;
        if (i9 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(13, i9);
        }
        long j = this.messageId;
        if (j != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(14, j);
        }
        int i10 = this.count;
        if (i10 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(15, i10);
        }
        boolean z5 = this.isManagedProfile;
        if (z5) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(16, z5);
        }
        boolean z6 = this.isNtn;
        if (z6) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(17, z6);
        }
        boolean z7 = this.isEmergency;
        if (z7) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(18, z7);
        }
        boolean z8 = this.isNbIotNtn;
        if (z8) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(19, z8);
        }
        int i11 = this.pduLength;
        if (i11 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(20, i11);
        }
        if (!this.plmn.equals("")) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(21, this.plmn);
        }
        int i12 = this.hashCode;
        return i12 != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(10001, i12) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public PersistAtomsProto$IncomingSms mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            switch (tag) {
                case 0:
                    break;
                case 8:
                    this.smsFormat = codedInputByteBufferNano.readInt32();
                    break;
                case 16:
                    this.smsTech = codedInputByteBufferNano.readInt32();
                    break;
                case 24:
                    this.rat = codedInputByteBufferNano.readInt32();
                    break;
                case 32:
                    this.smsType = codedInputByteBufferNano.readInt32();
                    break;
                case 40:
                    this.totalParts = codedInputByteBufferNano.readInt32();
                    break;
                case 48:
                    this.receivedParts = codedInputByteBufferNano.readInt32();
                    break;
                case 56:
                    this.blocked = codedInputByteBufferNano.readBool();
                    break;
                case 64:
                    this.error = codedInputByteBufferNano.readInt32();
                    break;
                case 72:
                    this.isRoaming = codedInputByteBufferNano.readBool();
                    break;
                case 80:
                    this.simSlotIndex = codedInputByteBufferNano.readInt32();
                    break;
                case 88:
                    this.isMultiSim = codedInputByteBufferNano.readBool();
                    break;
                case 96:
                    this.isEsim = codedInputByteBufferNano.readBool();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE /* 104 */:
                    this.carrierId = codedInputByteBufferNano.readInt32();
                    break;
                case 112:
                    this.messageId = codedInputByteBufferNano.readInt64();
                    break;
                case 120:
                    this.count = codedInputByteBufferNano.readInt32();
                    break;
                case 128:
                    this.isManagedProfile = codedInputByteBufferNano.readBool();
                    break;
                case 136:
                    this.isNtn = codedInputByteBufferNano.readBool();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_START_KEEPALIVE /* 144 */:
                    this.isEmergency = codedInputByteBufferNano.readBool();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_DEVICE_IMEI /* 152 */:
                    this.isNbIotNtn = codedInputByteBufferNano.readBool();
                    break;
                case SmsMessage.MAX_USER_DATA_SEPTETS /* 160 */:
                    this.pduLength = codedInputByteBufferNano.readInt32();
                    break;
                case 170:
                    this.plmn = codedInputByteBufferNano.readString();
                    break;
                case 80008:
                    this.hashCode = codedInputByteBufferNano.readInt32();
                    break;
                default:
                    if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    }
                    break;
            }
        }
        return this;
    }

    public static PersistAtomsProto$IncomingSms parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (PersistAtomsProto$IncomingSms) MessageNano.mergeFrom(new PersistAtomsProto$IncomingSms(), bArr);
    }

    public static PersistAtomsProto$IncomingSms parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new PersistAtomsProto$IncomingSms().mergeFrom(codedInputByteBufferNano);
    }
}
