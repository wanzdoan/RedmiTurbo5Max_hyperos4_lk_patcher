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
public final class PersistAtomsProto$CarrierRoamingSatelliteControllerStats extends ExtendableMessageNano<PersistAtomsProto$CarrierRoamingSatelliteControllerStats> {
    private static volatile PersistAtomsProto$CarrierRoamingSatelliteControllerStats[] _emptyArray;
    public int carrierId;
    public int configDataSource;
    public int countOfEntitlementStatusQueryRequest;
    public int countOfSatelliteConfigUpdateRequest;
    public int countOfSatelliteNotificationDisplayed;
    public int countOfSatelliteSessions;
    public int countOfSessionConnectionModeAutomatic;
    public int countOfSessionConnectionModeManual;
    public int eligibilitySource;
    public boolean isDeviceEntitled;
    public boolean isMultiSim;
    public boolean isNbIotNtn;
    public boolean satelliteAttachSupported;
    public int satelliteSessionGapAvgSec;
    public int satelliteSessionGapMaxSec;
    public int satelliteSessionGapMinSec;
    public int serviceDataPolicy;
    public int supportedConnectionMode;
    public int totalSessionDurationSec;

    public static PersistAtomsProto$CarrierRoamingSatelliteControllerStats[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new PersistAtomsProto$CarrierRoamingSatelliteControllerStats[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public PersistAtomsProto$CarrierRoamingSatelliteControllerStats() {
        clear();
    }

    public PersistAtomsProto$CarrierRoamingSatelliteControllerStats clear() {
        this.configDataSource = 0;
        this.countOfEntitlementStatusQueryRequest = 0;
        this.countOfSatelliteConfigUpdateRequest = 0;
        this.countOfSatelliteNotificationDisplayed = 0;
        this.satelliteSessionGapMinSec = 0;
        this.satelliteSessionGapAvgSec = 0;
        this.satelliteSessionGapMaxSec = 0;
        this.carrierId = 0;
        this.isDeviceEntitled = false;
        this.isMultiSim = false;
        this.countOfSatelliteSessions = 0;
        this.isNbIotNtn = false;
        this.supportedConnectionMode = 0;
        this.countOfSessionConnectionModeAutomatic = 0;
        this.countOfSessionConnectionModeManual = 0;
        this.serviceDataPolicy = 0;
        this.totalSessionDurationSec = 0;
        this.satelliteAttachSupported = false;
        this.eligibilitySource = 0;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        int i = this.configDataSource;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(1, i);
        }
        int i2 = this.countOfEntitlementStatusQueryRequest;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(2, i2);
        }
        int i3 = this.countOfSatelliteConfigUpdateRequest;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(3, i3);
        }
        int i4 = this.countOfSatelliteNotificationDisplayed;
        if (i4 != 0) {
            codedOutputByteBufferNano.writeInt32(4, i4);
        }
        int i5 = this.satelliteSessionGapMinSec;
        if (i5 != 0) {
            codedOutputByteBufferNano.writeInt32(5, i5);
        }
        int i6 = this.satelliteSessionGapAvgSec;
        if (i6 != 0) {
            codedOutputByteBufferNano.writeInt32(6, i6);
        }
        int i7 = this.satelliteSessionGapMaxSec;
        if (i7 != 0) {
            codedOutputByteBufferNano.writeInt32(7, i7);
        }
        int i8 = this.carrierId;
        if (i8 != 0) {
            codedOutputByteBufferNano.writeInt32(8, i8);
        }
        boolean z = this.isDeviceEntitled;
        if (z) {
            codedOutputByteBufferNano.writeBool(9, z);
        }
        boolean z2 = this.isMultiSim;
        if (z2) {
            codedOutputByteBufferNano.writeBool(10, z2);
        }
        int i9 = this.countOfSatelliteSessions;
        if (i9 != 0) {
            codedOutputByteBufferNano.writeInt32(11, i9);
        }
        boolean z3 = this.isNbIotNtn;
        if (z3) {
            codedOutputByteBufferNano.writeBool(12, z3);
        }
        int i10 = this.supportedConnectionMode;
        if (i10 != 0) {
            codedOutputByteBufferNano.writeInt32(13, i10);
        }
        int i11 = this.countOfSessionConnectionModeAutomatic;
        if (i11 != 0) {
            codedOutputByteBufferNano.writeInt32(14, i11);
        }
        int i12 = this.countOfSessionConnectionModeManual;
        if (i12 != 0) {
            codedOutputByteBufferNano.writeInt32(15, i12);
        }
        int i13 = this.serviceDataPolicy;
        if (i13 != 0) {
            codedOutputByteBufferNano.writeInt32(16, i13);
        }
        int i14 = this.totalSessionDurationSec;
        if (i14 != 0) {
            codedOutputByteBufferNano.writeInt32(17, i14);
        }
        boolean z4 = this.satelliteAttachSupported;
        if (z4) {
            codedOutputByteBufferNano.writeBool(18, z4);
        }
        int i15 = this.eligibilitySource;
        if (i15 != 0) {
            codedOutputByteBufferNano.writeInt32(19, i15);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        int i = this.configDataSource;
        if (i != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
        }
        int i2 = this.countOfEntitlementStatusQueryRequest;
        if (i2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i2);
        }
        int i3 = this.countOfSatelliteConfigUpdateRequest;
        if (i3 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, i3);
        }
        int i4 = this.countOfSatelliteNotificationDisplayed;
        if (i4 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(4, i4);
        }
        int i5 = this.satelliteSessionGapMinSec;
        if (i5 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(5, i5);
        }
        int i6 = this.satelliteSessionGapAvgSec;
        if (i6 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(6, i6);
        }
        int i7 = this.satelliteSessionGapMaxSec;
        if (i7 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(7, i7);
        }
        int i8 = this.carrierId;
        if (i8 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(8, i8);
        }
        boolean z = this.isDeviceEntitled;
        if (z) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(9, z);
        }
        boolean z2 = this.isMultiSim;
        if (z2) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(10, z2);
        }
        int i9 = this.countOfSatelliteSessions;
        if (i9 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(11, i9);
        }
        boolean z3 = this.isNbIotNtn;
        if (z3) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(12, z3);
        }
        int i10 = this.supportedConnectionMode;
        if (i10 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(13, i10);
        }
        int i11 = this.countOfSessionConnectionModeAutomatic;
        if (i11 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(14, i11);
        }
        int i12 = this.countOfSessionConnectionModeManual;
        if (i12 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(15, i12);
        }
        int i13 = this.serviceDataPolicy;
        if (i13 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(16, i13);
        }
        int i14 = this.totalSessionDurationSec;
        if (i14 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(17, i14);
        }
        boolean z4 = this.satelliteAttachSupported;
        if (z4) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(18, z4);
        }
        int i15 = this.eligibilitySource;
        return i15 != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(19, i15) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public PersistAtomsProto$CarrierRoamingSatelliteControllerStats mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            switch (tag) {
                case 0:
                    break;
                case 8:
                    this.configDataSource = codedInputByteBufferNano.readInt32();
                    break;
                case 16:
                    this.countOfEntitlementStatusQueryRequest = codedInputByteBufferNano.readInt32();
                    break;
                case 24:
                    this.countOfSatelliteConfigUpdateRequest = codedInputByteBufferNano.readInt32();
                    break;
                case 32:
                    this.countOfSatelliteNotificationDisplayed = codedInputByteBufferNano.readInt32();
                    break;
                case 40:
                    this.satelliteSessionGapMinSec = codedInputByteBufferNano.readInt32();
                    break;
                case 48:
                    this.satelliteSessionGapAvgSec = codedInputByteBufferNano.readInt32();
                    break;
                case 56:
                    this.satelliteSessionGapMaxSec = codedInputByteBufferNano.readInt32();
                    break;
                case 64:
                    this.carrierId = codedInputByteBufferNano.readInt32();
                    break;
                case 72:
                    this.isDeviceEntitled = codedInputByteBufferNano.readBool();
                    break;
                case 80:
                    this.isMultiSim = codedInputByteBufferNano.readBool();
                    break;
                case 88:
                    this.countOfSatelliteSessions = codedInputByteBufferNano.readInt32();
                    break;
                case 96:
                    this.isNbIotNtn = codedInputByteBufferNano.readBool();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE /* 104 */:
                    this.supportedConnectionMode = codedInputByteBufferNano.readInt32();
                    break;
                case 112:
                    this.countOfSessionConnectionModeAutomatic = codedInputByteBufferNano.readInt32();
                    break;
                case 120:
                    this.countOfSessionConnectionModeManual = codedInputByteBufferNano.readInt32();
                    break;
                case 128:
                    this.serviceDataPolicy = codedInputByteBufferNano.readInt32();
                    break;
                case 136:
                    this.totalSessionDurationSec = codedInputByteBufferNano.readInt32();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_START_KEEPALIVE /* 144 */:
                    this.satelliteAttachSupported = codedInputByteBufferNano.readBool();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_DEVICE_IMEI /* 152 */:
                    this.eligibilitySource = codedInputByteBufferNano.readInt32();
                    break;
                default:
                    if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    }
                    break;
            }
        }
        return this;
    }

    public static PersistAtomsProto$CarrierRoamingSatelliteControllerStats parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (PersistAtomsProto$CarrierRoamingSatelliteControllerStats) MessageNano.mergeFrom(new PersistAtomsProto$CarrierRoamingSatelliteControllerStats(), bArr);
    }

    public static PersistAtomsProto$CarrierRoamingSatelliteControllerStats parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new PersistAtomsProto$CarrierRoamingSatelliteControllerStats().mergeFrom(codedInputByteBufferNano);
    }
}
