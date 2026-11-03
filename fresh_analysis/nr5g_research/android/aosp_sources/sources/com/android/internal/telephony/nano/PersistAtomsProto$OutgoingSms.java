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
public final class PersistAtomsProto$OutgoingSms extends ExtendableMessageNano<PersistAtomsProto$OutgoingSms> {
    private static volatile PersistAtomsProto$OutgoingSms[] _emptyArray;
    public int appUid;
    public String callingPackageName;
    public int carrierId;
    public int count;
    public int errorCode;
    public int hashCode;
    public long intervalMillis;
    public boolean isEmergency;
    public boolean isEsim;
    public boolean isFromDefaultApp;
    public boolean isManagedProfile;
    public boolean isMtSmsPolling;
    public boolean isMultiSim;
    public boolean isNbIotNtn;
    public boolean isNtn;
    public boolean isRoaming;
    public long messageId;
    public int networkErrorCode;
    public int pduLength;
    public String plmn;
    public int rat;
    public int retryId;
    public int satelliteMessageTrigger;
    public int sendErrorCode;
    public int sendResult;
    public int simSlotIndex;
    public int smsFormat;
    public int smsTech;

    public static PersistAtomsProto$OutgoingSms[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new PersistAtomsProto$OutgoingSms[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public PersistAtomsProto$OutgoingSms() {
        clear();
    }

    public PersistAtomsProto$OutgoingSms clear() {
        this.smsFormat = 0;
        this.smsTech = 0;
        this.rat = 0;
        this.sendResult = 0;
        this.errorCode = 0;
        this.isRoaming = false;
        this.isFromDefaultApp = false;
        this.simSlotIndex = 0;
        this.isMultiSim = false;
        this.isEsim = false;
        this.carrierId = 0;
        this.messageId = 0L;
        this.retryId = 0;
        this.intervalMillis = 0L;
        this.count = 0;
        this.sendErrorCode = 0;
        this.networkErrorCode = 0;
        this.isManagedProfile = false;
        this.isEmergency = false;
        this.isNtn = false;
        this.isMtSmsPolling = false;
        this.isNbIotNtn = false;
        this.pduLength = 0;
        this.callingPackageName = "";
        this.appUid = 0;
        this.plmn = "";
        this.satelliteMessageTrigger = 0;
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
        int i4 = this.sendResult;
        if (i4 != 0) {
            codedOutputByteBufferNano.writeInt32(4, i4);
        }
        int i5 = this.errorCode;
        if (i5 != 0) {
            codedOutputByteBufferNano.writeInt32(5, i5);
        }
        boolean z = this.isRoaming;
        if (z) {
            codedOutputByteBufferNano.writeBool(6, z);
        }
        boolean z2 = this.isFromDefaultApp;
        if (z2) {
            codedOutputByteBufferNano.writeBool(7, z2);
        }
        int i6 = this.simSlotIndex;
        if (i6 != 0) {
            codedOutputByteBufferNano.writeInt32(8, i6);
        }
        boolean z3 = this.isMultiSim;
        if (z3) {
            codedOutputByteBufferNano.writeBool(9, z3);
        }
        boolean z4 = this.isEsim;
        if (z4) {
            codedOutputByteBufferNano.writeBool(10, z4);
        }
        int i7 = this.carrierId;
        if (i7 != 0) {
            codedOutputByteBufferNano.writeInt32(11, i7);
        }
        long j = this.messageId;
        if (j != 0) {
            codedOutputByteBufferNano.writeInt64(12, j);
        }
        int i8 = this.retryId;
        if (i8 != 0) {
            codedOutputByteBufferNano.writeInt32(13, i8);
        }
        long j2 = this.intervalMillis;
        if (j2 != 0) {
            codedOutputByteBufferNano.writeInt64(14, j2);
        }
        int i9 = this.count;
        if (i9 != 0) {
            codedOutputByteBufferNano.writeInt32(15, i9);
        }
        int i10 = this.sendErrorCode;
        if (i10 != 0) {
            codedOutputByteBufferNano.writeInt32(16, i10);
        }
        int i11 = this.networkErrorCode;
        if (i11 != 0) {
            codedOutputByteBufferNano.writeInt32(17, i11);
        }
        boolean z5 = this.isManagedProfile;
        if (z5) {
            codedOutputByteBufferNano.writeBool(18, z5);
        }
        boolean z6 = this.isEmergency;
        if (z6) {
            codedOutputByteBufferNano.writeBool(19, z6);
        }
        boolean z7 = this.isNtn;
        if (z7) {
            codedOutputByteBufferNano.writeBool(20, z7);
        }
        boolean z8 = this.isMtSmsPolling;
        if (z8) {
            codedOutputByteBufferNano.writeBool(21, z8);
        }
        boolean z9 = this.isNbIotNtn;
        if (z9) {
            codedOutputByteBufferNano.writeBool(22, z9);
        }
        int i12 = this.pduLength;
        if (i12 != 0) {
            codedOutputByteBufferNano.writeInt32(23, i12);
        }
        if (!this.callingPackageName.equals("")) {
            codedOutputByteBufferNano.writeString(24, this.callingPackageName);
        }
        int i13 = this.appUid;
        if (i13 != 0) {
            codedOutputByteBufferNano.writeInt32(25, i13);
        }
        if (!this.plmn.equals("")) {
            codedOutputByteBufferNano.writeString(26, this.plmn);
        }
        int i14 = this.satelliteMessageTrigger;
        if (i14 != 0) {
            codedOutputByteBufferNano.writeInt32(27, i14);
        }
        int i15 = this.hashCode;
        if (i15 != 0) {
            codedOutputByteBufferNano.writeInt32(10001, i15);
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
        int i4 = this.sendResult;
        if (i4 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(4, i4);
        }
        int i5 = this.errorCode;
        if (i5 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(5, i5);
        }
        boolean z = this.isRoaming;
        if (z) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(6, z);
        }
        boolean z2 = this.isFromDefaultApp;
        if (z2) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(7, z2);
        }
        int i6 = this.simSlotIndex;
        if (i6 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(8, i6);
        }
        boolean z3 = this.isMultiSim;
        if (z3) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(9, z3);
        }
        boolean z4 = this.isEsim;
        if (z4) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(10, z4);
        }
        int i7 = this.carrierId;
        if (i7 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(11, i7);
        }
        long j = this.messageId;
        if (j != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(12, j);
        }
        int i8 = this.retryId;
        if (i8 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(13, i8);
        }
        long j2 = this.intervalMillis;
        if (j2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(14, j2);
        }
        int i9 = this.count;
        if (i9 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(15, i9);
        }
        int i10 = this.sendErrorCode;
        if (i10 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(16, i10);
        }
        int i11 = this.networkErrorCode;
        if (i11 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(17, i11);
        }
        boolean z5 = this.isManagedProfile;
        if (z5) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(18, z5);
        }
        boolean z6 = this.isEmergency;
        if (z6) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(19, z6);
        }
        boolean z7 = this.isNtn;
        if (z7) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(20, z7);
        }
        boolean z8 = this.isMtSmsPolling;
        if (z8) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(21, z8);
        }
        boolean z9 = this.isNbIotNtn;
        if (z9) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(22, z9);
        }
        int i12 = this.pduLength;
        if (i12 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(23, i12);
        }
        if (!this.callingPackageName.equals("")) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(24, this.callingPackageName);
        }
        int i13 = this.appUid;
        if (i13 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(25, i13);
        }
        if (!this.plmn.equals("")) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(26, this.plmn);
        }
        int i14 = this.satelliteMessageTrigger;
        if (i14 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(27, i14);
        }
        int i15 = this.hashCode;
        return i15 != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(10001, i15) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public PersistAtomsProto$OutgoingSms mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
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
                    this.sendResult = codedInputByteBufferNano.readInt32();
                    break;
                case 40:
                    this.errorCode = codedInputByteBufferNano.readInt32();
                    break;
                case 48:
                    this.isRoaming = codedInputByteBufferNano.readBool();
                    break;
                case 56:
                    this.isFromDefaultApp = codedInputByteBufferNano.readBool();
                    break;
                case 64:
                    this.simSlotIndex = codedInputByteBufferNano.readInt32();
                    break;
                case 72:
                    this.isMultiSim = codedInputByteBufferNano.readBool();
                    break;
                case 80:
                    this.isEsim = codedInputByteBufferNano.readBool();
                    break;
                case 88:
                    this.carrierId = codedInputByteBufferNano.readInt32();
                    break;
                case 96:
                    this.messageId = codedInputByteBufferNano.readInt64();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE /* 104 */:
                    this.retryId = codedInputByteBufferNano.readInt32();
                    break;
                case 112:
                    this.intervalMillis = codedInputByteBufferNano.readInt64();
                    break;
                case 120:
                    this.count = codedInputByteBufferNano.readInt32();
                    break;
                case 128:
                    this.sendErrorCode = codedInputByteBufferNano.readInt32();
                    break;
                case 136:
                    this.networkErrorCode = codedInputByteBufferNano.readInt32();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_START_KEEPALIVE /* 144 */:
                    this.isManagedProfile = codedInputByteBufferNano.readBool();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_DEVICE_IMEI /* 152 */:
                    this.isEmergency = codedInputByteBufferNano.readBool();
                    break;
                case SmsMessage.MAX_USER_DATA_SEPTETS /* 160 */:
                    this.isNtn = codedInputByteBufferNano.readBool();
                    break;
                case 168:
                    this.isMtSmsPolling = codedInputByteBufferNano.readBool();
                    break;
                case 176:
                    this.isNbIotNtn = codedInputByteBufferNano.readBool();
                    break;
                case 184:
                    this.pduLength = codedInputByteBufferNano.readInt32();
                    break;
                case 194:
                    this.callingPackageName = codedInputByteBufferNano.readString();
                    break;
                case 200:
                    this.appUid = codedInputByteBufferNano.readInt32();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_SYSTEM_SELECTION_CHANNELS /* 210 */:
                    this.plmn = codedInputByteBufferNano.readString();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_RELEASE_PDU_SESSION_ID /* 216 */:
                    this.satelliteMessageTrigger = codedInputByteBufferNano.readInt32();
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

    public static PersistAtomsProto$OutgoingSms parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (PersistAtomsProto$OutgoingSms) MessageNano.mergeFrom(new PersistAtomsProto$OutgoingSms(), bArr);
    }

    public static PersistAtomsProto$OutgoingSms parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new PersistAtomsProto$OutgoingSms().mergeFrom(codedInputByteBufferNano);
    }
}
