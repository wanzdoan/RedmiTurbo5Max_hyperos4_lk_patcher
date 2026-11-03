package com.android.internal.telephony.nano;

import android.telephony.gsm.SmsMessage;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import com.android.internal.telephony.util.DnsPacket;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class PersistAtomsProto$SatelliteSession extends ExtendableMessageNano<PersistAtomsProto$SatelliteSession> {
    private static volatile PersistAtomsProto$SatelliteSession[] _emptyArray;
    public int batteryDesignCapacityMah;
    public int batteryLevelDropPercent;
    public int carrierId;
    public int count;
    public int countOfAutoExitDueToScreenOff;
    public int countOfAutoExitDueToTnNetwork;
    public int countOfIncomingDatagramFailed;
    public int countOfIncomingDatagramSuccess;
    public int countOfOutgoingDatagramFailed;
    public int countOfOutgoingDatagramSuccess;
    public int countOfSatelliteNotificationDisplayed;
    public long energyConsumedNwh;
    public long initializationProcessingTimeMillis;
    public boolean isDemoMode;
    public boolean isEmergency;
    public boolean isNtnOnlyCarrier;
    public int maxInactivityDurationSec;
    public int maxNtnSignalStrengthLevel;
    public String plmn;
    public int satelliteServiceInitializationResult;
    public int satelliteServiceTerminationResult;
    public int satelliteTechnology;
    public int screenOnTimeSec;
    public int sessionConnectionMode;
    public int sessionDurationSeconds;
    public int supportedConnectionMode;
    public long terminationProcessingTimeMillis;
    public boolean wasChargingDuringSession;

    public static PersistAtomsProto$SatelliteSession[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new PersistAtomsProto$SatelliteSession[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public PersistAtomsProto$SatelliteSession() {
        clear();
    }

    public PersistAtomsProto$SatelliteSession clear() {
        this.satelliteServiceInitializationResult = 0;
        this.satelliteTechnology = 0;
        this.count = 0;
        this.satelliteServiceTerminationResult = 0;
        this.initializationProcessingTimeMillis = 0L;
        this.terminationProcessingTimeMillis = 0L;
        this.sessionDurationSeconds = 0;
        this.countOfOutgoingDatagramSuccess = 0;
        this.countOfOutgoingDatagramFailed = 0;
        this.countOfIncomingDatagramSuccess = 0;
        this.countOfIncomingDatagramFailed = 0;
        this.isDemoMode = false;
        this.maxNtnSignalStrengthLevel = 0;
        this.carrierId = 0;
        this.countOfSatelliteNotificationDisplayed = 0;
        this.countOfAutoExitDueToScreenOff = 0;
        this.countOfAutoExitDueToTnNetwork = 0;
        this.isEmergency = false;
        this.isNtnOnlyCarrier = false;
        this.maxInactivityDurationSec = 0;
        this.supportedConnectionMode = 0;
        this.sessionConnectionMode = 0;
        this.plmn = "";
        this.screenOnTimeSec = 0;
        this.batteryLevelDropPercent = 0;
        this.wasChargingDuringSession = false;
        this.batteryDesignCapacityMah = 0;
        this.energyConsumedNwh = 0L;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        int i = this.satelliteServiceInitializationResult;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(1, i);
        }
        int i2 = this.satelliteTechnology;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(2, i2);
        }
        int i3 = this.count;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(3, i3);
        }
        int i4 = this.satelliteServiceTerminationResult;
        if (i4 != 0) {
            codedOutputByteBufferNano.writeInt32(4, i4);
        }
        long j = this.initializationProcessingTimeMillis;
        if (j != 0) {
            codedOutputByteBufferNano.writeInt64(5, j);
        }
        long j2 = this.terminationProcessingTimeMillis;
        if (j2 != 0) {
            codedOutputByteBufferNano.writeInt64(6, j2);
        }
        int i5 = this.sessionDurationSeconds;
        if (i5 != 0) {
            codedOutputByteBufferNano.writeInt32(7, i5);
        }
        int i6 = this.countOfOutgoingDatagramSuccess;
        if (i6 != 0) {
            codedOutputByteBufferNano.writeInt32(8, i6);
        }
        int i7 = this.countOfOutgoingDatagramFailed;
        if (i7 != 0) {
            codedOutputByteBufferNano.writeInt32(9, i7);
        }
        int i8 = this.countOfIncomingDatagramSuccess;
        if (i8 != 0) {
            codedOutputByteBufferNano.writeInt32(10, i8);
        }
        int i9 = this.countOfIncomingDatagramFailed;
        if (i9 != 0) {
            codedOutputByteBufferNano.writeInt32(11, i9);
        }
        boolean z = this.isDemoMode;
        if (z) {
            codedOutputByteBufferNano.writeBool(12, z);
        }
        int i10 = this.maxNtnSignalStrengthLevel;
        if (i10 != 0) {
            codedOutputByteBufferNano.writeInt32(13, i10);
        }
        int i11 = this.carrierId;
        if (i11 != 0) {
            codedOutputByteBufferNano.writeInt32(14, i11);
        }
        int i12 = this.countOfSatelliteNotificationDisplayed;
        if (i12 != 0) {
            codedOutputByteBufferNano.writeInt32(15, i12);
        }
        int i13 = this.countOfAutoExitDueToScreenOff;
        if (i13 != 0) {
            codedOutputByteBufferNano.writeInt32(16, i13);
        }
        int i14 = this.countOfAutoExitDueToTnNetwork;
        if (i14 != 0) {
            codedOutputByteBufferNano.writeInt32(17, i14);
        }
        boolean z2 = this.isEmergency;
        if (z2) {
            codedOutputByteBufferNano.writeBool(18, z2);
        }
        boolean z3 = this.isNtnOnlyCarrier;
        if (z3) {
            codedOutputByteBufferNano.writeBool(19, z3);
        }
        int i15 = this.maxInactivityDurationSec;
        if (i15 != 0) {
            codedOutputByteBufferNano.writeInt32(20, i15);
        }
        int i16 = this.supportedConnectionMode;
        if (i16 != 0) {
            codedOutputByteBufferNano.writeInt32(21, i16);
        }
        int i17 = this.sessionConnectionMode;
        if (i17 != 0) {
            codedOutputByteBufferNano.writeInt32(22, i17);
        }
        if (!this.plmn.equals("")) {
            codedOutputByteBufferNano.writeString(23, this.plmn);
        }
        int i18 = this.screenOnTimeSec;
        if (i18 != 0) {
            codedOutputByteBufferNano.writeInt32(24, i18);
        }
        int i19 = this.batteryLevelDropPercent;
        if (i19 != 0) {
            codedOutputByteBufferNano.writeInt32(25, i19);
        }
        boolean z4 = this.wasChargingDuringSession;
        if (z4) {
            codedOutputByteBufferNano.writeBool(26, z4);
        }
        int i20 = this.batteryDesignCapacityMah;
        if (i20 != 0) {
            codedOutputByteBufferNano.writeInt32(27, i20);
        }
        long j3 = this.energyConsumedNwh;
        if (j3 != 0) {
            codedOutputByteBufferNano.writeInt64(28, j3);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        int i = this.satelliteServiceInitializationResult;
        if (i != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
        }
        int i2 = this.satelliteTechnology;
        if (i2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i2);
        }
        int i3 = this.count;
        if (i3 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, i3);
        }
        int i4 = this.satelliteServiceTerminationResult;
        if (i4 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(4, i4);
        }
        long j = this.initializationProcessingTimeMillis;
        if (j != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(5, j);
        }
        long j2 = this.terminationProcessingTimeMillis;
        if (j2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(6, j2);
        }
        int i5 = this.sessionDurationSeconds;
        if (i5 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(7, i5);
        }
        int i6 = this.countOfOutgoingDatagramSuccess;
        if (i6 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(8, i6);
        }
        int i7 = this.countOfOutgoingDatagramFailed;
        if (i7 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(9, i7);
        }
        int i8 = this.countOfIncomingDatagramSuccess;
        if (i8 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(10, i8);
        }
        int i9 = this.countOfIncomingDatagramFailed;
        if (i9 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(11, i9);
        }
        boolean z = this.isDemoMode;
        if (z) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(12, z);
        }
        int i10 = this.maxNtnSignalStrengthLevel;
        if (i10 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(13, i10);
        }
        int i11 = this.carrierId;
        if (i11 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(14, i11);
        }
        int i12 = this.countOfSatelliteNotificationDisplayed;
        if (i12 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(15, i12);
        }
        int i13 = this.countOfAutoExitDueToScreenOff;
        if (i13 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(16, i13);
        }
        int i14 = this.countOfAutoExitDueToTnNetwork;
        if (i14 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(17, i14);
        }
        boolean z2 = this.isEmergency;
        if (z2) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(18, z2);
        }
        boolean z3 = this.isNtnOnlyCarrier;
        if (z3) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(19, z3);
        }
        int i15 = this.maxInactivityDurationSec;
        if (i15 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(20, i15);
        }
        int i16 = this.supportedConnectionMode;
        if (i16 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(21, i16);
        }
        int i17 = this.sessionConnectionMode;
        if (i17 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(22, i17);
        }
        if (!this.plmn.equals("")) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(23, this.plmn);
        }
        int i18 = this.screenOnTimeSec;
        if (i18 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(24, i18);
        }
        int i19 = this.batteryLevelDropPercent;
        if (i19 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(25, i19);
        }
        boolean z4 = this.wasChargingDuringSession;
        if (z4) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(26, z4);
        }
        int i20 = this.batteryDesignCapacityMah;
        if (i20 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(27, i20);
        }
        long j3 = this.energyConsumedNwh;
        return j3 != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt64Size(28, j3) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public PersistAtomsProto$SatelliteSession mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            switch (tag) {
                case 0:
                    break;
                case 8:
                    this.satelliteServiceInitializationResult = codedInputByteBufferNano.readInt32();
                    break;
                case 16:
                    this.satelliteTechnology = codedInputByteBufferNano.readInt32();
                    break;
                case 24:
                    this.count = codedInputByteBufferNano.readInt32();
                    break;
                case 32:
                    this.satelliteServiceTerminationResult = codedInputByteBufferNano.readInt32();
                    break;
                case 40:
                    this.initializationProcessingTimeMillis = codedInputByteBufferNano.readInt64();
                    break;
                case 48:
                    this.terminationProcessingTimeMillis = codedInputByteBufferNano.readInt64();
                    break;
                case 56:
                    this.sessionDurationSeconds = codedInputByteBufferNano.readInt32();
                    break;
                case 64:
                    this.countOfOutgoingDatagramSuccess = codedInputByteBufferNano.readInt32();
                    break;
                case 72:
                    this.countOfOutgoingDatagramFailed = codedInputByteBufferNano.readInt32();
                    break;
                case 80:
                    this.countOfIncomingDatagramSuccess = codedInputByteBufferNano.readInt32();
                    break;
                case 88:
                    this.countOfIncomingDatagramFailed = codedInputByteBufferNano.readInt32();
                    break;
                case 96:
                    this.isDemoMode = codedInputByteBufferNano.readBool();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE /* 104 */:
                    this.maxNtnSignalStrengthLevel = codedInputByteBufferNano.readInt32();
                    break;
                case 112:
                    this.carrierId = codedInputByteBufferNano.readInt32();
                    break;
                case 120:
                    this.countOfSatelliteNotificationDisplayed = codedInputByteBufferNano.readInt32();
                    break;
                case 128:
                    this.countOfAutoExitDueToScreenOff = codedInputByteBufferNano.readInt32();
                    break;
                case 136:
                    this.countOfAutoExitDueToTnNetwork = codedInputByteBufferNano.readInt32();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_START_KEEPALIVE /* 144 */:
                    this.isEmergency = codedInputByteBufferNano.readBool();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_DEVICE_IMEI /* 152 */:
                    this.isNtnOnlyCarrier = codedInputByteBufferNano.readBool();
                    break;
                case SmsMessage.MAX_USER_DATA_SEPTETS /* 160 */:
                    this.maxInactivityDurationSec = codedInputByteBufferNano.readInt32();
                    break;
                case 168:
                    this.supportedConnectionMode = codedInputByteBufferNano.readInt32();
                    break;
                case 176:
                    this.sessionConnectionMode = codedInputByteBufferNano.readInt32();
                    break;
                case 186:
                    this.plmn = codedInputByteBufferNano.readString();
                    break;
                case DnsPacket.DnsRecord.NAME_COMPRESSION /* 192 */:
                    this.screenOnTimeSec = codedInputByteBufferNano.readInt32();
                    break;
                case 200:
                    this.batteryLevelDropPercent = codedInputByteBufferNano.readInt32();
                    break;
                case 208:
                    this.wasChargingDuringSession = codedInputByteBufferNano.readBool();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_RELEASE_PDU_SESSION_ID /* 216 */:
                    this.batteryDesignCapacityMah = codedInputByteBufferNano.readInt32();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_SLICING_CONFIG /* 224 */:
                    this.energyConsumedNwh = codedInputByteBufferNano.readInt64();
                    break;
                default:
                    if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    }
                    break;
            }
        }
        return this;
    }

    public static PersistAtomsProto$SatelliteSession parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (PersistAtomsProto$SatelliteSession) MessageNano.mergeFrom(new PersistAtomsProto$SatelliteSession(), bArr);
    }

    public static PersistAtomsProto$SatelliteSession parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new PersistAtomsProto$SatelliteSession().mergeFrom(codedInputByteBufferNano);
    }
}
