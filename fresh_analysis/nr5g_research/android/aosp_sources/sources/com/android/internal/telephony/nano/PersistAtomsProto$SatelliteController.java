package com.android.internal.telephony.nano;

import android.telephony.gsm.SmsMessage;
import com.android.internal.telephony.CallFailCause;
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
public final class PersistAtomsProto$SatelliteController extends ExtendableMessageNano<PersistAtomsProto$SatelliteController> {
    private static volatile PersistAtomsProto$SatelliteController[] _emptyArray;
    public int carrierId;
    public int carrierRoamingSatelliteConfigVersion;
    public int countOfAllowedSatelliteAccess;
    public int countOfDatagramTypeKeepAliveFail;
    public int countOfDatagramTypeKeepAliveSuccess;
    public int countOfDatagramTypeLocationSharingFail;
    public int countOfDatagramTypeLocationSharingSuccess;
    public int countOfDatagramTypeSosSmsFail;
    public int countOfDatagramTypeSosSmsSuccess;
    public int countOfDemoModeIncomingDatagramFail;
    public int countOfDemoModeIncomingDatagramSuccess;
    public int countOfDemoModeOutgoingDatagramFail;
    public int countOfDemoModeOutgoingDatagramSuccess;
    public int countOfDemoModeSatelliteServiceEnablementsFail;
    public int countOfDemoModeSatelliteServiceEnablementsSuccess;
    public int countOfDeprovisionFail;
    public int countOfDeprovisionSuccess;
    public int countOfDisallowedSatelliteAccess;
    public int countOfFailedLocationQueries;
    public int countOfIncomingDatagramFail;
    public int countOfIncomingDatagramSuccess;
    public int countOfIncomingDatagramTypeSmsFail;
    public int countOfIncomingDatagramTypeSmsSuccess;
    public int countOfIncomingDatagramTypeSosSmsFail;
    public int countOfIncomingDatagramTypeSosSmsSuccess;
    public int countOfOutgoingDatagramFail;
    public int countOfOutgoingDatagramSuccess;
    public int countOfOutgoingDatagramTypeSmsFail;
    public int countOfOutgoingDatagramTypeSmsSuccess;
    public int countOfP2PSmsAvailableNotificationRemoved;
    public int countOfP2PSmsAvailableNotificationShown;
    public int countOfProvisionFail;
    public int countOfProvisionSuccess;
    public int countOfSatelliteAccessCheckFail;
    public int countOfSatelliteAllowedStateChangedEvents;
    public int countOfSatelliteServiceEnablementsFail;
    public int countOfSatelliteServiceEnablementsSuccess;
    public int countOfSuccessfulLocationQueries;
    public boolean isNtnOnlyCarrier;
    public boolean isProvisioned;
    public int maxAllowedDataMode;
    public int supportedConnectionMode;
    public int totalBatteryChargedTimeSec;
    public int totalBatteryConsumptionPercent;
    public int totalServiceUptimeSec;
    public int versionOfSatelliteAccessConfig;

    public static PersistAtomsProto$SatelliteController[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new PersistAtomsProto$SatelliteController[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public PersistAtomsProto$SatelliteController() {
        clear();
    }

    public PersistAtomsProto$SatelliteController clear() {
        this.countOfSatelliteServiceEnablementsSuccess = 0;
        this.countOfSatelliteServiceEnablementsFail = 0;
        this.countOfOutgoingDatagramSuccess = 0;
        this.countOfOutgoingDatagramFail = 0;
        this.countOfIncomingDatagramSuccess = 0;
        this.countOfIncomingDatagramFail = 0;
        this.countOfDatagramTypeSosSmsSuccess = 0;
        this.countOfDatagramTypeSosSmsFail = 0;
        this.countOfDatagramTypeLocationSharingSuccess = 0;
        this.countOfDatagramTypeLocationSharingFail = 0;
        this.countOfProvisionSuccess = 0;
        this.countOfProvisionFail = 0;
        this.countOfDeprovisionSuccess = 0;
        this.countOfDeprovisionFail = 0;
        this.totalServiceUptimeSec = 0;
        this.totalBatteryConsumptionPercent = 0;
        this.totalBatteryChargedTimeSec = 0;
        this.countOfDemoModeSatelliteServiceEnablementsSuccess = 0;
        this.countOfDemoModeSatelliteServiceEnablementsFail = 0;
        this.countOfDemoModeOutgoingDatagramSuccess = 0;
        this.countOfDemoModeOutgoingDatagramFail = 0;
        this.countOfDemoModeIncomingDatagramSuccess = 0;
        this.countOfDemoModeIncomingDatagramFail = 0;
        this.countOfDatagramTypeKeepAliveSuccess = 0;
        this.countOfDatagramTypeKeepAliveFail = 0;
        this.countOfAllowedSatelliteAccess = 0;
        this.countOfDisallowedSatelliteAccess = 0;
        this.countOfSatelliteAccessCheckFail = 0;
        this.isProvisioned = false;
        this.carrierId = 0;
        this.countOfSatelliteAllowedStateChangedEvents = 0;
        this.countOfSuccessfulLocationQueries = 0;
        this.countOfFailedLocationQueries = 0;
        this.countOfP2PSmsAvailableNotificationShown = 0;
        this.countOfP2PSmsAvailableNotificationRemoved = 0;
        this.isNtnOnlyCarrier = false;
        this.versionOfSatelliteAccessConfig = 0;
        this.countOfIncomingDatagramTypeSosSmsSuccess = 0;
        this.countOfIncomingDatagramTypeSosSmsFail = 0;
        this.countOfOutgoingDatagramTypeSmsSuccess = 0;
        this.countOfOutgoingDatagramTypeSmsFail = 0;
        this.countOfIncomingDatagramTypeSmsSuccess = 0;
        this.countOfIncomingDatagramTypeSmsFail = 0;
        this.carrierRoamingSatelliteConfigVersion = 0;
        this.maxAllowedDataMode = 0;
        this.supportedConnectionMode = 0;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        int i = this.countOfSatelliteServiceEnablementsSuccess;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(1, i);
        }
        int i2 = this.countOfSatelliteServiceEnablementsFail;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(2, i2);
        }
        int i3 = this.countOfOutgoingDatagramSuccess;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(3, i3);
        }
        int i4 = this.countOfOutgoingDatagramFail;
        if (i4 != 0) {
            codedOutputByteBufferNano.writeInt32(4, i4);
        }
        int i5 = this.countOfIncomingDatagramSuccess;
        if (i5 != 0) {
            codedOutputByteBufferNano.writeInt32(5, i5);
        }
        int i6 = this.countOfIncomingDatagramFail;
        if (i6 != 0) {
            codedOutputByteBufferNano.writeInt32(6, i6);
        }
        int i7 = this.countOfDatagramTypeSosSmsSuccess;
        if (i7 != 0) {
            codedOutputByteBufferNano.writeInt32(7, i7);
        }
        int i8 = this.countOfDatagramTypeSosSmsFail;
        if (i8 != 0) {
            codedOutputByteBufferNano.writeInt32(8, i8);
        }
        int i9 = this.countOfDatagramTypeLocationSharingSuccess;
        if (i9 != 0) {
            codedOutputByteBufferNano.writeInt32(9, i9);
        }
        int i10 = this.countOfDatagramTypeLocationSharingFail;
        if (i10 != 0) {
            codedOutputByteBufferNano.writeInt32(10, i10);
        }
        int i11 = this.countOfProvisionSuccess;
        if (i11 != 0) {
            codedOutputByteBufferNano.writeInt32(11, i11);
        }
        int i12 = this.countOfProvisionFail;
        if (i12 != 0) {
            codedOutputByteBufferNano.writeInt32(12, i12);
        }
        int i13 = this.countOfDeprovisionSuccess;
        if (i13 != 0) {
            codedOutputByteBufferNano.writeInt32(13, i13);
        }
        int i14 = this.countOfDeprovisionFail;
        if (i14 != 0) {
            codedOutputByteBufferNano.writeInt32(14, i14);
        }
        int i15 = this.totalServiceUptimeSec;
        if (i15 != 0) {
            codedOutputByteBufferNano.writeInt32(15, i15);
        }
        int i16 = this.totalBatteryConsumptionPercent;
        if (i16 != 0) {
            codedOutputByteBufferNano.writeInt32(16, i16);
        }
        int i17 = this.totalBatteryChargedTimeSec;
        if (i17 != 0) {
            codedOutputByteBufferNano.writeInt32(17, i17);
        }
        int i18 = this.countOfDemoModeSatelliteServiceEnablementsSuccess;
        if (i18 != 0) {
            codedOutputByteBufferNano.writeInt32(18, i18);
        }
        int i19 = this.countOfDemoModeSatelliteServiceEnablementsFail;
        if (i19 != 0) {
            codedOutputByteBufferNano.writeInt32(19, i19);
        }
        int i20 = this.countOfDemoModeOutgoingDatagramSuccess;
        if (i20 != 0) {
            codedOutputByteBufferNano.writeInt32(20, i20);
        }
        int i21 = this.countOfDemoModeOutgoingDatagramFail;
        if (i21 != 0) {
            codedOutputByteBufferNano.writeInt32(21, i21);
        }
        int i22 = this.countOfDemoModeIncomingDatagramSuccess;
        if (i22 != 0) {
            codedOutputByteBufferNano.writeInt32(22, i22);
        }
        int i23 = this.countOfDemoModeIncomingDatagramFail;
        if (i23 != 0) {
            codedOutputByteBufferNano.writeInt32(23, i23);
        }
        int i24 = this.countOfDatagramTypeKeepAliveSuccess;
        if (i24 != 0) {
            codedOutputByteBufferNano.writeInt32(24, i24);
        }
        int i25 = this.countOfDatagramTypeKeepAliveFail;
        if (i25 != 0) {
            codedOutputByteBufferNano.writeInt32(25, i25);
        }
        int i26 = this.countOfAllowedSatelliteAccess;
        if (i26 != 0) {
            codedOutputByteBufferNano.writeInt32(26, i26);
        }
        int i27 = this.countOfDisallowedSatelliteAccess;
        if (i27 != 0) {
            codedOutputByteBufferNano.writeInt32(27, i27);
        }
        int i28 = this.countOfSatelliteAccessCheckFail;
        if (i28 != 0) {
            codedOutputByteBufferNano.writeInt32(28, i28);
        }
        boolean z = this.isProvisioned;
        if (z) {
            codedOutputByteBufferNano.writeBool(29, z);
        }
        int i29 = this.carrierId;
        if (i29 != 0) {
            codedOutputByteBufferNano.writeInt32(30, i29);
        }
        int i30 = this.countOfSatelliteAllowedStateChangedEvents;
        if (i30 != 0) {
            codedOutputByteBufferNano.writeInt32(31, i30);
        }
        int i31 = this.countOfSuccessfulLocationQueries;
        if (i31 != 0) {
            codedOutputByteBufferNano.writeInt32(32, i31);
        }
        int i32 = this.countOfFailedLocationQueries;
        if (i32 != 0) {
            codedOutputByteBufferNano.writeInt32(33, i32);
        }
        int i33 = this.countOfP2PSmsAvailableNotificationShown;
        if (i33 != 0) {
            codedOutputByteBufferNano.writeInt32(34, i33);
        }
        int i34 = this.countOfP2PSmsAvailableNotificationRemoved;
        if (i34 != 0) {
            codedOutputByteBufferNano.writeInt32(35, i34);
        }
        boolean z2 = this.isNtnOnlyCarrier;
        if (z2) {
            codedOutputByteBufferNano.writeBool(36, z2);
        }
        int i35 = this.versionOfSatelliteAccessConfig;
        if (i35 != 0) {
            codedOutputByteBufferNano.writeInt32(37, i35);
        }
        int i36 = this.countOfIncomingDatagramTypeSosSmsSuccess;
        if (i36 != 0) {
            codedOutputByteBufferNano.writeInt32(38, i36);
        }
        int i37 = this.countOfIncomingDatagramTypeSosSmsFail;
        if (i37 != 0) {
            codedOutputByteBufferNano.writeInt32(39, i37);
        }
        int i38 = this.countOfOutgoingDatagramTypeSmsSuccess;
        if (i38 != 0) {
            codedOutputByteBufferNano.writeInt32(40, i38);
        }
        int i39 = this.countOfOutgoingDatagramTypeSmsFail;
        if (i39 != 0) {
            codedOutputByteBufferNano.writeInt32(41, i39);
        }
        int i40 = this.countOfIncomingDatagramTypeSmsSuccess;
        if (i40 != 0) {
            codedOutputByteBufferNano.writeInt32(42, i40);
        }
        int i41 = this.countOfIncomingDatagramTypeSmsFail;
        if (i41 != 0) {
            codedOutputByteBufferNano.writeInt32(43, i41);
        }
        int i42 = this.carrierRoamingSatelliteConfigVersion;
        if (i42 != 0) {
            codedOutputByteBufferNano.writeInt32(44, i42);
        }
        int i43 = this.maxAllowedDataMode;
        if (i43 != 0) {
            codedOutputByteBufferNano.writeInt32(45, i43);
        }
        int i44 = this.supportedConnectionMode;
        if (i44 != 0) {
            codedOutputByteBufferNano.writeInt32(46, i44);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        int i = this.countOfSatelliteServiceEnablementsSuccess;
        if (i != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
        }
        int i2 = this.countOfSatelliteServiceEnablementsFail;
        if (i2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i2);
        }
        int i3 = this.countOfOutgoingDatagramSuccess;
        if (i3 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, i3);
        }
        int i4 = this.countOfOutgoingDatagramFail;
        if (i4 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(4, i4);
        }
        int i5 = this.countOfIncomingDatagramSuccess;
        if (i5 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(5, i5);
        }
        int i6 = this.countOfIncomingDatagramFail;
        if (i6 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(6, i6);
        }
        int i7 = this.countOfDatagramTypeSosSmsSuccess;
        if (i7 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(7, i7);
        }
        int i8 = this.countOfDatagramTypeSosSmsFail;
        if (i8 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(8, i8);
        }
        int i9 = this.countOfDatagramTypeLocationSharingSuccess;
        if (i9 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(9, i9);
        }
        int i10 = this.countOfDatagramTypeLocationSharingFail;
        if (i10 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(10, i10);
        }
        int i11 = this.countOfProvisionSuccess;
        if (i11 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(11, i11);
        }
        int i12 = this.countOfProvisionFail;
        if (i12 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(12, i12);
        }
        int i13 = this.countOfDeprovisionSuccess;
        if (i13 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(13, i13);
        }
        int i14 = this.countOfDeprovisionFail;
        if (i14 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(14, i14);
        }
        int i15 = this.totalServiceUptimeSec;
        if (i15 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(15, i15);
        }
        int i16 = this.totalBatteryConsumptionPercent;
        if (i16 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(16, i16);
        }
        int i17 = this.totalBatteryChargedTimeSec;
        if (i17 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(17, i17);
        }
        int i18 = this.countOfDemoModeSatelliteServiceEnablementsSuccess;
        if (i18 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(18, i18);
        }
        int i19 = this.countOfDemoModeSatelliteServiceEnablementsFail;
        if (i19 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(19, i19);
        }
        int i20 = this.countOfDemoModeOutgoingDatagramSuccess;
        if (i20 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(20, i20);
        }
        int i21 = this.countOfDemoModeOutgoingDatagramFail;
        if (i21 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(21, i21);
        }
        int i22 = this.countOfDemoModeIncomingDatagramSuccess;
        if (i22 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(22, i22);
        }
        int i23 = this.countOfDemoModeIncomingDatagramFail;
        if (i23 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(23, i23);
        }
        int i24 = this.countOfDatagramTypeKeepAliveSuccess;
        if (i24 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(24, i24);
        }
        int i25 = this.countOfDatagramTypeKeepAliveFail;
        if (i25 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(25, i25);
        }
        int i26 = this.countOfAllowedSatelliteAccess;
        if (i26 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(26, i26);
        }
        int i27 = this.countOfDisallowedSatelliteAccess;
        if (i27 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(27, i27);
        }
        int i28 = this.countOfSatelliteAccessCheckFail;
        if (i28 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(28, i28);
        }
        boolean z = this.isProvisioned;
        if (z) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(29, z);
        }
        int i29 = this.carrierId;
        if (i29 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(30, i29);
        }
        int i30 = this.countOfSatelliteAllowedStateChangedEvents;
        if (i30 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(31, i30);
        }
        int i31 = this.countOfSuccessfulLocationQueries;
        if (i31 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(32, i31);
        }
        int i32 = this.countOfFailedLocationQueries;
        if (i32 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(33, i32);
        }
        int i33 = this.countOfP2PSmsAvailableNotificationShown;
        if (i33 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(34, i33);
        }
        int i34 = this.countOfP2PSmsAvailableNotificationRemoved;
        if (i34 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(35, i34);
        }
        boolean z2 = this.isNtnOnlyCarrier;
        if (z2) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(36, z2);
        }
        int i35 = this.versionOfSatelliteAccessConfig;
        if (i35 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(37, i35);
        }
        int i36 = this.countOfIncomingDatagramTypeSosSmsSuccess;
        if (i36 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(38, i36);
        }
        int i37 = this.countOfIncomingDatagramTypeSosSmsFail;
        if (i37 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(39, i37);
        }
        int i38 = this.countOfOutgoingDatagramTypeSmsSuccess;
        if (i38 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(40, i38);
        }
        int i39 = this.countOfOutgoingDatagramTypeSmsFail;
        if (i39 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(41, i39);
        }
        int i40 = this.countOfIncomingDatagramTypeSmsSuccess;
        if (i40 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(42, i40);
        }
        int i41 = this.countOfIncomingDatagramTypeSmsFail;
        if (i41 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(43, i41);
        }
        int i42 = this.carrierRoamingSatelliteConfigVersion;
        if (i42 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(44, i42);
        }
        int i43 = this.maxAllowedDataMode;
        if (i43 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(45, i43);
        }
        int i44 = this.supportedConnectionMode;
        return i44 != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(46, i44) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public PersistAtomsProto$SatelliteController mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            switch (tag) {
                case 0:
                    break;
                case 8:
                    this.countOfSatelliteServiceEnablementsSuccess = codedInputByteBufferNano.readInt32();
                    break;
                case 16:
                    this.countOfSatelliteServiceEnablementsFail = codedInputByteBufferNano.readInt32();
                    break;
                case 24:
                    this.countOfOutgoingDatagramSuccess = codedInputByteBufferNano.readInt32();
                    break;
                case 32:
                    this.countOfOutgoingDatagramFail = codedInputByteBufferNano.readInt32();
                    break;
                case 40:
                    this.countOfIncomingDatagramSuccess = codedInputByteBufferNano.readInt32();
                    break;
                case 48:
                    this.countOfIncomingDatagramFail = codedInputByteBufferNano.readInt32();
                    break;
                case 56:
                    this.countOfDatagramTypeSosSmsSuccess = codedInputByteBufferNano.readInt32();
                    break;
                case 64:
                    this.countOfDatagramTypeSosSmsFail = codedInputByteBufferNano.readInt32();
                    break;
                case 72:
                    this.countOfDatagramTypeLocationSharingSuccess = codedInputByteBufferNano.readInt32();
                    break;
                case 80:
                    this.countOfDatagramTypeLocationSharingFail = codedInputByteBufferNano.readInt32();
                    break;
                case 88:
                    this.countOfProvisionSuccess = codedInputByteBufferNano.readInt32();
                    break;
                case 96:
                    this.countOfProvisionFail = codedInputByteBufferNano.readInt32();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE /* 104 */:
                    this.countOfDeprovisionSuccess = codedInputByteBufferNano.readInt32();
                    break;
                case 112:
                    this.countOfDeprovisionFail = codedInputByteBufferNano.readInt32();
                    break;
                case 120:
                    this.totalServiceUptimeSec = codedInputByteBufferNano.readInt32();
                    break;
                case 128:
                    this.totalBatteryConsumptionPercent = codedInputByteBufferNano.readInt32();
                    break;
                case 136:
                    this.totalBatteryChargedTimeSec = codedInputByteBufferNano.readInt32();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_START_KEEPALIVE /* 144 */:
                    this.countOfDemoModeSatelliteServiceEnablementsSuccess = codedInputByteBufferNano.readInt32();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_DEVICE_IMEI /* 152 */:
                    this.countOfDemoModeSatelliteServiceEnablementsFail = codedInputByteBufferNano.readInt32();
                    break;
                case SmsMessage.MAX_USER_DATA_SEPTETS /* 160 */:
                    this.countOfDemoModeOutgoingDatagramSuccess = codedInputByteBufferNano.readInt32();
                    break;
                case 168:
                    this.countOfDemoModeOutgoingDatagramFail = codedInputByteBufferNano.readInt32();
                    break;
                case 176:
                    this.countOfDemoModeIncomingDatagramSuccess = codedInputByteBufferNano.readInt32();
                    break;
                case 184:
                    this.countOfDemoModeIncomingDatagramFail = codedInputByteBufferNano.readInt32();
                    break;
                case DnsPacket.DnsRecord.NAME_COMPRESSION /* 192 */:
                    this.countOfDatagramTypeKeepAliveSuccess = codedInputByteBufferNano.readInt32();
                    break;
                case 200:
                    this.countOfDatagramTypeKeepAliveFail = codedInputByteBufferNano.readInt32();
                    break;
                case 208:
                    this.countOfAllowedSatelliteAccess = codedInputByteBufferNano.readInt32();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_RELEASE_PDU_SESSION_ID /* 216 */:
                    this.countOfDisallowedSatelliteAccess = codedInputByteBufferNano.readInt32();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_SLICING_CONFIG /* 224 */:
                    this.countOfSatelliteAccessCheckFail = codedInputByteBufferNano.readInt32();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_EXIT_EMERGENCY_MODE /* 232 */:
                    this.isProvisioned = codedInputByteBufferNano.readBool();
                    break;
                case 240:
                    this.carrierId = codedInputByteBufferNano.readInt32();
                    break;
                case 248:
                    this.countOfSatelliteAllowedStateChangedEvents = codedInputByteBufferNano.readInt32();
                    break;
                case CallFailCause.RADIO_UPLINK_FAILURE /* 256 */:
                    this.countOfSuccessfulLocationQueries = codedInputByteBufferNano.readInt32();
                    break;
                case 264:
                    this.countOfFailedLocationQueries = codedInputByteBufferNano.readInt32();
                    break;
                case 272:
                    this.countOfP2PSmsAvailableNotificationShown = codedInputByteBufferNano.readInt32();
                    break;
                case 280:
                    this.countOfP2PSmsAvailableNotificationRemoved = codedInputByteBufferNano.readInt32();
                    break;
                case 288:
                    this.isNtnOnlyCarrier = codedInputByteBufferNano.readBool();
                    break;
                case 296:
                    this.versionOfSatelliteAccessConfig = codedInputByteBufferNano.readInt32();
                    break;
                case 304:
                    this.countOfIncomingDatagramTypeSosSmsSuccess = codedInputByteBufferNano.readInt32();
                    break;
                case 312:
                    this.countOfIncomingDatagramTypeSosSmsFail = codedInputByteBufferNano.readInt32();
                    break;
                case 320:
                    this.countOfOutgoingDatagramTypeSmsSuccess = codedInputByteBufferNano.readInt32();
                    break;
                case 328:
                    this.countOfOutgoingDatagramTypeSmsFail = codedInputByteBufferNano.readInt32();
                    break;
                case 336:
                    this.countOfIncomingDatagramTypeSmsSuccess = codedInputByteBufferNano.readInt32();
                    break;
                case 344:
                    this.countOfIncomingDatagramTypeSmsFail = codedInputByteBufferNano.readInt32();
                    break;
                case 352:
                    this.carrierRoamingSatelliteConfigVersion = codedInputByteBufferNano.readInt32();
                    break;
                case 360:
                    this.maxAllowedDataMode = codedInputByteBufferNano.readInt32();
                    break;
                case 368:
                    this.supportedConnectionMode = codedInputByteBufferNano.readInt32();
                    break;
                default:
                    if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    }
                    break;
            }
        }
        return this;
    }

    public static PersistAtomsProto$SatelliteController parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (PersistAtomsProto$SatelliteController) MessageNano.mergeFrom(new PersistAtomsProto$SatelliteController(), bArr);
    }

    public static PersistAtomsProto$SatelliteController parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new PersistAtomsProto$SatelliteController().mergeFrom(codedInputByteBufferNano);
    }
}
