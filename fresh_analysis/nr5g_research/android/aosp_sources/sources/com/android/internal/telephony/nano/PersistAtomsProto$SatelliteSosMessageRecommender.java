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
public final class PersistAtomsProto$SatelliteSosMessageRecommender extends ExtendableMessageNano<PersistAtomsProto$SatelliteSosMessageRecommender> {
    private static volatile PersistAtomsProto$SatelliteSosMessageRecommender[] _emptyArray;
    public int carrierId;
    public int carrierRoamingSatelliteEmergencyMessagingProvider;
    public int cellularServiceState;
    public int count;
    public int countOfTimerStarted;
    public int emergencyNumberSourceUsedInHandoverIntent;
    public boolean isDisplaySosMessageSent;
    public boolean isImsRegistered;
    public boolean isInCarrierRoamingNtnMode;
    public boolean isMultiSim;
    public boolean isNtnOnlyCarrier;
    public boolean isSatelliteAllowedInCurrentLocation;
    public boolean isWifiConnected;
    public String plmn;
    public int recommendingHandoverType;
    public int sessionConnectionMode;
    public int supportedConnectionMode;

    public static PersistAtomsProto$SatelliteSosMessageRecommender[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new PersistAtomsProto$SatelliteSosMessageRecommender[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public PersistAtomsProto$SatelliteSosMessageRecommender() {
        clear();
    }

    public PersistAtomsProto$SatelliteSosMessageRecommender clear() {
        this.isDisplaySosMessageSent = false;
        this.countOfTimerStarted = 0;
        this.isImsRegistered = false;
        this.cellularServiceState = 0;
        this.count = 0;
        this.isMultiSim = false;
        this.recommendingHandoverType = 0;
        this.isSatelliteAllowedInCurrentLocation = false;
        this.isWifiConnected = false;
        this.carrierId = 0;
        this.isNtnOnlyCarrier = false;
        this.supportedConnectionMode = 0;
        this.sessionConnectionMode = 0;
        this.plmn = "";
        this.isInCarrierRoamingNtnMode = false;
        this.carrierRoamingSatelliteEmergencyMessagingProvider = 0;
        this.emergencyNumberSourceUsedInHandoverIntent = 0;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        boolean z = this.isDisplaySosMessageSent;
        if (z) {
            codedOutputByteBufferNano.writeBool(1, z);
        }
        int i = this.countOfTimerStarted;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(2, i);
        }
        boolean z2 = this.isImsRegistered;
        if (z2) {
            codedOutputByteBufferNano.writeBool(3, z2);
        }
        int i2 = this.cellularServiceState;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(4, i2);
        }
        int i3 = this.count;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(5, i3);
        }
        boolean z3 = this.isMultiSim;
        if (z3) {
            codedOutputByteBufferNano.writeBool(6, z3);
        }
        int i4 = this.recommendingHandoverType;
        if (i4 != 0) {
            codedOutputByteBufferNano.writeInt32(7, i4);
        }
        boolean z4 = this.isSatelliteAllowedInCurrentLocation;
        if (z4) {
            codedOutputByteBufferNano.writeBool(8, z4);
        }
        boolean z5 = this.isWifiConnected;
        if (z5) {
            codedOutputByteBufferNano.writeBool(9, z5);
        }
        int i5 = this.carrierId;
        if (i5 != 0) {
            codedOutputByteBufferNano.writeInt32(10, i5);
        }
        boolean z6 = this.isNtnOnlyCarrier;
        if (z6) {
            codedOutputByteBufferNano.writeBool(11, z6);
        }
        int i6 = this.supportedConnectionMode;
        if (i6 != 0) {
            codedOutputByteBufferNano.writeInt32(12, i6);
        }
        int i7 = this.sessionConnectionMode;
        if (i7 != 0) {
            codedOutputByteBufferNano.writeInt32(13, i7);
        }
        if (!this.plmn.equals("")) {
            codedOutputByteBufferNano.writeString(14, this.plmn);
        }
        boolean z7 = this.isInCarrierRoamingNtnMode;
        if (z7) {
            codedOutputByteBufferNano.writeBool(15, z7);
        }
        int i8 = this.carrierRoamingSatelliteEmergencyMessagingProvider;
        if (i8 != 0) {
            codedOutputByteBufferNano.writeInt32(16, i8);
        }
        int i9 = this.emergencyNumberSourceUsedInHandoverIntent;
        if (i9 != 0) {
            codedOutputByteBufferNano.writeInt32(17, i9);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        boolean z = this.isDisplaySosMessageSent;
        if (z) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(1, z);
        }
        int i = this.countOfTimerStarted;
        if (i != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i);
        }
        boolean z2 = this.isImsRegistered;
        if (z2) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(3, z2);
        }
        int i2 = this.cellularServiceState;
        if (i2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(4, i2);
        }
        int i3 = this.count;
        if (i3 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(5, i3);
        }
        boolean z3 = this.isMultiSim;
        if (z3) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(6, z3);
        }
        int i4 = this.recommendingHandoverType;
        if (i4 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(7, i4);
        }
        boolean z4 = this.isSatelliteAllowedInCurrentLocation;
        if (z4) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(8, z4);
        }
        boolean z5 = this.isWifiConnected;
        if (z5) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(9, z5);
        }
        int i5 = this.carrierId;
        if (i5 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(10, i5);
        }
        boolean z6 = this.isNtnOnlyCarrier;
        if (z6) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(11, z6);
        }
        int i6 = this.supportedConnectionMode;
        if (i6 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(12, i6);
        }
        int i7 = this.sessionConnectionMode;
        if (i7 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(13, i7);
        }
        if (!this.plmn.equals("")) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(14, this.plmn);
        }
        boolean z7 = this.isInCarrierRoamingNtnMode;
        if (z7) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(15, z7);
        }
        int i8 = this.carrierRoamingSatelliteEmergencyMessagingProvider;
        if (i8 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(16, i8);
        }
        int i9 = this.emergencyNumberSourceUsedInHandoverIntent;
        return i9 != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(17, i9) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public PersistAtomsProto$SatelliteSosMessageRecommender mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            switch (tag) {
                case 0:
                    break;
                case 8:
                    this.isDisplaySosMessageSent = codedInputByteBufferNano.readBool();
                    break;
                case 16:
                    this.countOfTimerStarted = codedInputByteBufferNano.readInt32();
                    break;
                case 24:
                    this.isImsRegistered = codedInputByteBufferNano.readBool();
                    break;
                case 32:
                    this.cellularServiceState = codedInputByteBufferNano.readInt32();
                    break;
                case 40:
                    this.count = codedInputByteBufferNano.readInt32();
                    break;
                case 48:
                    this.isMultiSim = codedInputByteBufferNano.readBool();
                    break;
                case 56:
                    this.recommendingHandoverType = codedInputByteBufferNano.readInt32();
                    break;
                case 64:
                    this.isSatelliteAllowedInCurrentLocation = codedInputByteBufferNano.readBool();
                    break;
                case 72:
                    this.isWifiConnected = codedInputByteBufferNano.readBool();
                    break;
                case 80:
                    this.carrierId = codedInputByteBufferNano.readInt32();
                    break;
                case 88:
                    this.isNtnOnlyCarrier = codedInputByteBufferNano.readBool();
                    break;
                case 96:
                    this.supportedConnectionMode = codedInputByteBufferNano.readInt32();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE /* 104 */:
                    this.sessionConnectionMode = codedInputByteBufferNano.readInt32();
                    break;
                case 114:
                    this.plmn = codedInputByteBufferNano.readString();
                    break;
                case 120:
                    this.isInCarrierRoamingNtnMode = codedInputByteBufferNano.readBool();
                    break;
                case 128:
                    this.carrierRoamingSatelliteEmergencyMessagingProvider = codedInputByteBufferNano.readInt32();
                    break;
                case 136:
                    this.emergencyNumberSourceUsedInHandoverIntent = codedInputByteBufferNano.readInt32();
                    break;
                default:
                    if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    }
                    break;
            }
        }
        return this;
    }

    public static PersistAtomsProto$SatelliteSosMessageRecommender parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (PersistAtomsProto$SatelliteSosMessageRecommender) MessageNano.mergeFrom(new PersistAtomsProto$SatelliteSosMessageRecommender(), bArr);
    }

    public static PersistAtomsProto$SatelliteSosMessageRecommender parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new PersistAtomsProto$SatelliteSosMessageRecommender().mergeFrom(codedInputByteBufferNano);
    }
}
