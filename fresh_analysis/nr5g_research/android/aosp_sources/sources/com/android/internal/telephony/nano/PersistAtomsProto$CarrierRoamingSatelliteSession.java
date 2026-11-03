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
import com.android.internal.telephony.protobuf.nano.WireFormatNano;
import com.android.internal.telephony.util.DnsPacket;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class PersistAtomsProto$CarrierRoamingSatelliteSession extends ExtendableMessageNano<PersistAtomsProto$CarrierRoamingSatelliteSession> {
    private static volatile PersistAtomsProto$CarrierRoamingSatelliteSession[] _emptyArray;
    public int averageDownlinkBandwidthKbps;
    public int averageUplinkBandwidthKbps;
    public int avgDurationOfSatelliteConnectionSec;
    public int batteryDesignCapacityMah;
    public int batteryLevelDropPercent;
    public int carrierId;
    public int countOfDataConnections;
    public int countOfDataDisconnections;
    public int countOfDataStalls;
    public int countOfEmergencyDialerButtonDisplayed;
    public int countOfIncomingMms;
    public int countOfIncomingSms;
    public int countOfNonEmergencyDialerDialogDisplayed;
    public int countOfOutgoingMms;
    public int countOfOutgoingSms;
    public int countOfSatelliteNotificationDisplayed;
    public int eligibilitySource;
    public long energyConsumedNwh;
    public boolean isMultiSim;
    public boolean isNbIotNtn;
    public boolean isNtnRoamingInHomeCountry;
    public boolean isWfcEnabled;
    public boolean isWfcRegistered;
    public boolean isWifiConnected;
    public boolean isWifiEnabled;
    public int[] lastFailCauses;
    public int maxDownlinkBandwidthKbps;
    public int maxUplinkBandwidthKbps;
    public int minDownlinkBandwidthKbps;
    public int minUplinkBandwidthKbps;
    public int numberOfSatelliteConnections;
    public long[] perAppRxDataBytes;
    public long[] perAppSatelliteDataConsumedBytes;
    public long[] perAppTxDataBytes;
    public String plmn;
    public int rsrpAvg;
    public int rsrpMedian;
    public int rssnrAvg;
    public int rssnrMedian;
    public int satelliteConnectionGapAvgSec;
    public int satelliteConnectionGapMaxSec;
    public int satelliteConnectionGapMinSec;
    public long satelliteDataConsumedBytes;
    public String[] satelliteSupportedApps;
    public int[] satelliteSupportedUids;
    public int screenOnTimeSec;
    public int serviceDataPolicy;
    public int sessionConnectionMode;
    public int supportedConnectionMode;
    public int[] supportedSatelliteServices;
    public long totalRxDataBytes;
    public int totalSatelliteModeTimeSec;
    public long totalTxDataBytes;
    public boolean wasChargingDuringSession;

    public static PersistAtomsProto$CarrierRoamingSatelliteSession[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new PersistAtomsProto$CarrierRoamingSatelliteSession[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public PersistAtomsProto$CarrierRoamingSatelliteSession() {
        clear();
    }

    public PersistAtomsProto$CarrierRoamingSatelliteSession clear() {
        this.carrierId = 0;
        this.isNtnRoamingInHomeCountry = false;
        this.totalSatelliteModeTimeSec = 0;
        this.numberOfSatelliteConnections = 0;
        this.avgDurationOfSatelliteConnectionSec = 0;
        this.satelliteConnectionGapMinSec = 0;
        this.satelliteConnectionGapAvgSec = 0;
        this.satelliteConnectionGapMaxSec = 0;
        this.rsrpAvg = 0;
        this.rsrpMedian = 0;
        this.rssnrAvg = 0;
        this.rssnrMedian = 0;
        this.countOfIncomingSms = 0;
        this.countOfOutgoingSms = 0;
        this.countOfIncomingMms = 0;
        this.countOfOutgoingMms = 0;
        int[] iArr = WireFormatNano.EMPTY_INT_ARRAY;
        this.supportedSatelliteServices = iArr;
        this.serviceDataPolicy = 0;
        this.satelliteDataConsumedBytes = 0L;
        this.isMultiSim = false;
        this.isNbIotNtn = false;
        this.countOfDataConnections = 0;
        this.lastFailCauses = iArr;
        this.countOfDataDisconnections = 0;
        this.countOfDataStalls = 0;
        this.averageUplinkBandwidthKbps = 0;
        this.averageDownlinkBandwidthKbps = 0;
        this.minUplinkBandwidthKbps = 0;
        this.maxUplinkBandwidthKbps = 0;
        this.minDownlinkBandwidthKbps = 0;
        this.maxDownlinkBandwidthKbps = 0;
        this.satelliteSupportedApps = WireFormatNano.EMPTY_STRING_ARRAY;
        this.satelliteSupportedUids = iArr;
        long[] jArr = WireFormatNano.EMPTY_LONG_ARRAY;
        this.perAppSatelliteDataConsumedBytes = jArr;
        this.supportedConnectionMode = 0;
        this.sessionConnectionMode = 0;
        this.plmn = "";
        this.isWifiEnabled = false;
        this.isWfcEnabled = false;
        this.isWfcRegistered = false;
        this.screenOnTimeSec = 0;
        this.batteryLevelDropPercent = 0;
        this.wasChargingDuringSession = false;
        this.batteryDesignCapacityMah = 0;
        this.energyConsumedNwh = 0L;
        this.eligibilitySource = 0;
        this.isWifiConnected = false;
        this.countOfNonEmergencyDialerDialogDisplayed = 0;
        this.countOfEmergencyDialerButtonDisplayed = 0;
        this.countOfSatelliteNotificationDisplayed = 0;
        this.totalRxDataBytes = 0L;
        this.totalTxDataBytes = 0L;
        this.perAppRxDataBytes = jArr;
        this.perAppTxDataBytes = jArr;
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
        boolean z = this.isNtnRoamingInHomeCountry;
        if (z) {
            codedOutputByteBufferNano.writeBool(2, z);
        }
        int i2 = this.totalSatelliteModeTimeSec;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(3, i2);
        }
        int i3 = this.numberOfSatelliteConnections;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(4, i3);
        }
        int i4 = this.avgDurationOfSatelliteConnectionSec;
        if (i4 != 0) {
            codedOutputByteBufferNano.writeInt32(5, i4);
        }
        int i5 = this.satelliteConnectionGapMinSec;
        if (i5 != 0) {
            codedOutputByteBufferNano.writeInt32(6, i5);
        }
        int i6 = this.satelliteConnectionGapAvgSec;
        if (i6 != 0) {
            codedOutputByteBufferNano.writeInt32(7, i6);
        }
        int i7 = this.satelliteConnectionGapMaxSec;
        if (i7 != 0) {
            codedOutputByteBufferNano.writeInt32(8, i7);
        }
        int i8 = this.rsrpAvg;
        if (i8 != 0) {
            codedOutputByteBufferNano.writeInt32(9, i8);
        }
        int i9 = this.rsrpMedian;
        if (i9 != 0) {
            codedOutputByteBufferNano.writeInt32(10, i9);
        }
        int i10 = this.rssnrAvg;
        if (i10 != 0) {
            codedOutputByteBufferNano.writeInt32(11, i10);
        }
        int i11 = this.rssnrMedian;
        if (i11 != 0) {
            codedOutputByteBufferNano.writeInt32(12, i11);
        }
        int i12 = this.countOfIncomingSms;
        if (i12 != 0) {
            codedOutputByteBufferNano.writeInt32(13, i12);
        }
        int i13 = this.countOfOutgoingSms;
        if (i13 != 0) {
            codedOutputByteBufferNano.writeInt32(14, i13);
        }
        int i14 = this.countOfIncomingMms;
        if (i14 != 0) {
            codedOutputByteBufferNano.writeInt32(15, i14);
        }
        int i15 = this.countOfOutgoingMms;
        if (i15 != 0) {
            codedOutputByteBufferNano.writeInt32(16, i15);
        }
        int[] iArr = this.supportedSatelliteServices;
        int i16 = 0;
        if (iArr != null && iArr.length > 0) {
            int i17 = 0;
            while (true) {
                int[] iArr2 = this.supportedSatelliteServices;
                if (i17 >= iArr2.length) {
                    break;
                }
                codedOutputByteBufferNano.writeInt32(17, iArr2[i17]);
                i17++;
            }
        }
        int i18 = this.serviceDataPolicy;
        if (i18 != 0) {
            codedOutputByteBufferNano.writeInt32(18, i18);
        }
        long j = this.satelliteDataConsumedBytes;
        if (j != 0) {
            codedOutputByteBufferNano.writeInt64(19, j);
        }
        boolean z2 = this.isMultiSim;
        if (z2) {
            codedOutputByteBufferNano.writeBool(20, z2);
        }
        boolean z3 = this.isNbIotNtn;
        if (z3) {
            codedOutputByteBufferNano.writeBool(21, z3);
        }
        int i19 = this.countOfDataConnections;
        if (i19 != 0) {
            codedOutputByteBufferNano.writeInt32(22, i19);
        }
        int[] iArr3 = this.lastFailCauses;
        if (iArr3 != null && iArr3.length > 0) {
            int i20 = 0;
            while (true) {
                int[] iArr4 = this.lastFailCauses;
                if (i20 >= iArr4.length) {
                    break;
                }
                codedOutputByteBufferNano.writeInt32(23, iArr4[i20]);
                i20++;
            }
        }
        int i21 = this.countOfDataDisconnections;
        if (i21 != 0) {
            codedOutputByteBufferNano.writeInt32(24, i21);
        }
        int i22 = this.countOfDataStalls;
        if (i22 != 0) {
            codedOutputByteBufferNano.writeInt32(25, i22);
        }
        int i23 = this.averageUplinkBandwidthKbps;
        if (i23 != 0) {
            codedOutputByteBufferNano.writeInt32(26, i23);
        }
        int i24 = this.averageDownlinkBandwidthKbps;
        if (i24 != 0) {
            codedOutputByteBufferNano.writeInt32(27, i24);
        }
        int i25 = this.minUplinkBandwidthKbps;
        if (i25 != 0) {
            codedOutputByteBufferNano.writeInt32(28, i25);
        }
        int i26 = this.maxUplinkBandwidthKbps;
        if (i26 != 0) {
            codedOutputByteBufferNano.writeInt32(29, i26);
        }
        int i27 = this.minDownlinkBandwidthKbps;
        if (i27 != 0) {
            codedOutputByteBufferNano.writeInt32(30, i27);
        }
        int i28 = this.maxDownlinkBandwidthKbps;
        if (i28 != 0) {
            codedOutputByteBufferNano.writeInt32(31, i28);
        }
        String[] strArr = this.satelliteSupportedApps;
        if (strArr != null && strArr.length > 0) {
            int i29 = 0;
            while (true) {
                String[] strArr2 = this.satelliteSupportedApps;
                if (i29 >= strArr2.length) {
                    break;
                }
                String str = strArr2[i29];
                if (str != null) {
                    codedOutputByteBufferNano.writeString(32, str);
                }
                i29++;
            }
        }
        int[] iArr5 = this.satelliteSupportedUids;
        if (iArr5 != null && iArr5.length > 0) {
            int i30 = 0;
            while (true) {
                int[] iArr6 = this.satelliteSupportedUids;
                if (i30 >= iArr6.length) {
                    break;
                }
                codedOutputByteBufferNano.writeInt32(33, iArr6[i30]);
                i30++;
            }
        }
        long[] jArr = this.perAppSatelliteDataConsumedBytes;
        if (jArr != null && jArr.length > 0) {
            int i31 = 0;
            while (true) {
                long[] jArr2 = this.perAppSatelliteDataConsumedBytes;
                if (i31 >= jArr2.length) {
                    break;
                }
                codedOutputByteBufferNano.writeInt64(34, jArr2[i31]);
                i31++;
            }
        }
        int i32 = this.supportedConnectionMode;
        if (i32 != 0) {
            codedOutputByteBufferNano.writeInt32(35, i32);
        }
        int i33 = this.sessionConnectionMode;
        if (i33 != 0) {
            codedOutputByteBufferNano.writeInt32(36, i33);
        }
        if (!this.plmn.equals("")) {
            codedOutputByteBufferNano.writeString(37, this.plmn);
        }
        boolean z4 = this.isWifiEnabled;
        if (z4) {
            codedOutputByteBufferNano.writeBool(38, z4);
        }
        boolean z5 = this.isWfcEnabled;
        if (z5) {
            codedOutputByteBufferNano.writeBool(39, z5);
        }
        boolean z6 = this.isWfcRegistered;
        if (z6) {
            codedOutputByteBufferNano.writeBool(40, z6);
        }
        int i34 = this.screenOnTimeSec;
        if (i34 != 0) {
            codedOutputByteBufferNano.writeInt32(41, i34);
        }
        int i35 = this.batteryLevelDropPercent;
        if (i35 != 0) {
            codedOutputByteBufferNano.writeInt32(42, i35);
        }
        boolean z7 = this.wasChargingDuringSession;
        if (z7) {
            codedOutputByteBufferNano.writeBool(43, z7);
        }
        int i36 = this.batteryDesignCapacityMah;
        if (i36 != 0) {
            codedOutputByteBufferNano.writeInt32(44, i36);
        }
        long j2 = this.energyConsumedNwh;
        if (j2 != 0) {
            codedOutputByteBufferNano.writeInt64(45, j2);
        }
        int i37 = this.eligibilitySource;
        if (i37 != 0) {
            codedOutputByteBufferNano.writeInt32(46, i37);
        }
        boolean z8 = this.isWifiConnected;
        if (z8) {
            codedOutputByteBufferNano.writeBool(47, z8);
        }
        int i38 = this.countOfNonEmergencyDialerDialogDisplayed;
        if (i38 != 0) {
            codedOutputByteBufferNano.writeInt32(48, i38);
        }
        int i39 = this.countOfEmergencyDialerButtonDisplayed;
        if (i39 != 0) {
            codedOutputByteBufferNano.writeInt32(49, i39);
        }
        int i40 = this.countOfSatelliteNotificationDisplayed;
        if (i40 != 0) {
            codedOutputByteBufferNano.writeInt32(50, i40);
        }
        long j3 = this.totalRxDataBytes;
        if (j3 != 0) {
            codedOutputByteBufferNano.writeInt64(51, j3);
        }
        long j4 = this.totalTxDataBytes;
        if (j4 != 0) {
            codedOutputByteBufferNano.writeInt64(52, j4);
        }
        long[] jArr3 = this.perAppRxDataBytes;
        if (jArr3 != null && jArr3.length > 0) {
            int i41 = 0;
            while (true) {
                long[] jArr4 = this.perAppRxDataBytes;
                if (i41 >= jArr4.length) {
                    break;
                }
                codedOutputByteBufferNano.writeInt64(53, jArr4[i41]);
                i41++;
            }
        }
        long[] jArr5 = this.perAppTxDataBytes;
        if (jArr5 != null && jArr5.length > 0) {
            while (true) {
                long[] jArr6 = this.perAppTxDataBytes;
                if (i16 >= jArr6.length) {
                    break;
                }
                codedOutputByteBufferNano.writeInt64(54, jArr6[i16]);
                i16++;
            }
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        long[] jArr;
        long[] jArr2;
        int[] iArr;
        int[] iArr2;
        int[] iArr3;
        int iComputeSerializedSize = super.computeSerializedSize();
        int i = this.carrierId;
        if (i != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
        }
        boolean z = this.isNtnRoamingInHomeCountry;
        if (z) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(2, z);
        }
        int i2 = this.totalSatelliteModeTimeSec;
        if (i2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, i2);
        }
        int i3 = this.numberOfSatelliteConnections;
        if (i3 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(4, i3);
        }
        int i4 = this.avgDurationOfSatelliteConnectionSec;
        if (i4 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(5, i4);
        }
        int i5 = this.satelliteConnectionGapMinSec;
        if (i5 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(6, i5);
        }
        int i6 = this.satelliteConnectionGapAvgSec;
        if (i6 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(7, i6);
        }
        int i7 = this.satelliteConnectionGapMaxSec;
        if (i7 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(8, i7);
        }
        int i8 = this.rsrpAvg;
        if (i8 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(9, i8);
        }
        int i9 = this.rsrpMedian;
        if (i9 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(10, i9);
        }
        int i10 = this.rssnrAvg;
        if (i10 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(11, i10);
        }
        int i11 = this.rssnrMedian;
        if (i11 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(12, i11);
        }
        int i12 = this.countOfIncomingSms;
        if (i12 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(13, i12);
        }
        int i13 = this.countOfOutgoingSms;
        if (i13 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(14, i13);
        }
        int i14 = this.countOfIncomingMms;
        if (i14 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(15, i14);
        }
        int i15 = this.countOfOutgoingMms;
        if (i15 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(16, i15);
        }
        int[] iArr4 = this.supportedSatelliteServices;
        int i16 = 0;
        if (iArr4 != null && iArr4.length > 0) {
            int i17 = 0;
            int iComputeInt32SizeNoTag = 0;
            while (true) {
                iArr3 = this.supportedSatelliteServices;
                if (i17 >= iArr3.length) {
                    break;
                }
                iComputeInt32SizeNoTag += CodedOutputByteBufferNano.computeInt32SizeNoTag(iArr3[i17]);
                i17++;
            }
            iComputeSerializedSize = iComputeSerializedSize + iComputeInt32SizeNoTag + (iArr3.length * 2);
        }
        int i18 = this.serviceDataPolicy;
        if (i18 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(18, i18);
        }
        long j = this.satelliteDataConsumedBytes;
        if (j != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(19, j);
        }
        boolean z2 = this.isMultiSim;
        if (z2) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(20, z2);
        }
        boolean z3 = this.isNbIotNtn;
        if (z3) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(21, z3);
        }
        int i19 = this.countOfDataConnections;
        if (i19 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(22, i19);
        }
        int[] iArr5 = this.lastFailCauses;
        if (iArr5 != null && iArr5.length > 0) {
            int i20 = 0;
            int iComputeInt32SizeNoTag2 = 0;
            while (true) {
                iArr2 = this.lastFailCauses;
                if (i20 >= iArr2.length) {
                    break;
                }
                iComputeInt32SizeNoTag2 += CodedOutputByteBufferNano.computeInt32SizeNoTag(iArr2[i20]);
                i20++;
            }
            iComputeSerializedSize = iComputeSerializedSize + iComputeInt32SizeNoTag2 + (iArr2.length * 2);
        }
        int i21 = this.countOfDataDisconnections;
        if (i21 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(24, i21);
        }
        int i22 = this.countOfDataStalls;
        if (i22 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(25, i22);
        }
        int i23 = this.averageUplinkBandwidthKbps;
        if (i23 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(26, i23);
        }
        int i24 = this.averageDownlinkBandwidthKbps;
        if (i24 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(27, i24);
        }
        int i25 = this.minUplinkBandwidthKbps;
        if (i25 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(28, i25);
        }
        int i26 = this.maxUplinkBandwidthKbps;
        if (i26 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(29, i26);
        }
        int i27 = this.minDownlinkBandwidthKbps;
        if (i27 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(30, i27);
        }
        int i28 = this.maxDownlinkBandwidthKbps;
        if (i28 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(31, i28);
        }
        String[] strArr = this.satelliteSupportedApps;
        if (strArr != null && strArr.length > 0) {
            int i29 = 0;
            int iComputeStringSizeNoTag = 0;
            int i30 = 0;
            while (true) {
                String[] strArr2 = this.satelliteSupportedApps;
                if (i29 >= strArr2.length) {
                    break;
                }
                String str = strArr2[i29];
                if (str != null) {
                    i30++;
                    iComputeStringSizeNoTag += CodedOutputByteBufferNano.computeStringSizeNoTag(str);
                }
                i29++;
            }
            iComputeSerializedSize = iComputeSerializedSize + iComputeStringSizeNoTag + (i30 * 2);
        }
        int[] iArr6 = this.satelliteSupportedUids;
        if (iArr6 != null && iArr6.length > 0) {
            int i31 = 0;
            int iComputeInt32SizeNoTag3 = 0;
            while (true) {
                iArr = this.satelliteSupportedUids;
                if (i31 >= iArr.length) {
                    break;
                }
                iComputeInt32SizeNoTag3 += CodedOutputByteBufferNano.computeInt32SizeNoTag(iArr[i31]);
                i31++;
            }
            iComputeSerializedSize = iComputeSerializedSize + iComputeInt32SizeNoTag3 + (iArr.length * 2);
        }
        long[] jArr3 = this.perAppSatelliteDataConsumedBytes;
        if (jArr3 != null && jArr3.length > 0) {
            int i32 = 0;
            int iComputeInt64SizeNoTag = 0;
            while (true) {
                jArr2 = this.perAppSatelliteDataConsumedBytes;
                if (i32 >= jArr2.length) {
                    break;
                }
                iComputeInt64SizeNoTag += CodedOutputByteBufferNano.computeInt64SizeNoTag(jArr2[i32]);
                i32++;
            }
            iComputeSerializedSize = iComputeSerializedSize + iComputeInt64SizeNoTag + (jArr2.length * 2);
        }
        int i33 = this.supportedConnectionMode;
        if (i33 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(35, i33);
        }
        int i34 = this.sessionConnectionMode;
        if (i34 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(36, i34);
        }
        if (!this.plmn.equals("")) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(37, this.plmn);
        }
        boolean z4 = this.isWifiEnabled;
        if (z4) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(38, z4);
        }
        boolean z5 = this.isWfcEnabled;
        if (z5) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(39, z5);
        }
        boolean z6 = this.isWfcRegistered;
        if (z6) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(40, z6);
        }
        int i35 = this.screenOnTimeSec;
        if (i35 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(41, i35);
        }
        int i36 = this.batteryLevelDropPercent;
        if (i36 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(42, i36);
        }
        boolean z7 = this.wasChargingDuringSession;
        if (z7) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(43, z7);
        }
        int i37 = this.batteryDesignCapacityMah;
        if (i37 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(44, i37);
        }
        long j2 = this.energyConsumedNwh;
        if (j2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(45, j2);
        }
        int i38 = this.eligibilitySource;
        if (i38 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(46, i38);
        }
        boolean z8 = this.isWifiConnected;
        if (z8) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(47, z8);
        }
        int i39 = this.countOfNonEmergencyDialerDialogDisplayed;
        if (i39 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(48, i39);
        }
        int i40 = this.countOfEmergencyDialerButtonDisplayed;
        if (i40 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(49, i40);
        }
        int i41 = this.countOfSatelliteNotificationDisplayed;
        if (i41 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(50, i41);
        }
        long j3 = this.totalRxDataBytes;
        if (j3 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(51, j3);
        }
        long j4 = this.totalTxDataBytes;
        if (j4 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(52, j4);
        }
        long[] jArr4 = this.perAppRxDataBytes;
        if (jArr4 != null && jArr4.length > 0) {
            int i42 = 0;
            int iComputeInt64SizeNoTag2 = 0;
            while (true) {
                jArr = this.perAppRxDataBytes;
                if (i42 >= jArr.length) {
                    break;
                }
                iComputeInt64SizeNoTag2 += CodedOutputByteBufferNano.computeInt64SizeNoTag(jArr[i42]);
                i42++;
            }
            iComputeSerializedSize = iComputeSerializedSize + iComputeInt64SizeNoTag2 + (jArr.length * 2);
        }
        long[] jArr5 = this.perAppTxDataBytes;
        if (jArr5 == null || jArr5.length <= 0) {
            return iComputeSerializedSize;
        }
        int iComputeInt64SizeNoTag3 = 0;
        while (true) {
            long[] jArr6 = this.perAppTxDataBytes;
            if (i16 < jArr6.length) {
                iComputeInt64SizeNoTag3 += CodedOutputByteBufferNano.computeInt64SizeNoTag(jArr6[i16]);
                i16++;
            } else {
                return iComputeSerializedSize + iComputeInt64SizeNoTag3 + (jArr6.length * 2);
            }
        }
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public PersistAtomsProto$CarrierRoamingSatelliteSession mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            switch (tag) {
                case 0:
                    break;
                case 8:
                    this.carrierId = codedInputByteBufferNano.readInt32();
                    break;
                case 16:
                    this.isNtnRoamingInHomeCountry = codedInputByteBufferNano.readBool();
                    break;
                case 24:
                    this.totalSatelliteModeTimeSec = codedInputByteBufferNano.readInt32();
                    break;
                case 32:
                    this.numberOfSatelliteConnections = codedInputByteBufferNano.readInt32();
                    break;
                case 40:
                    this.avgDurationOfSatelliteConnectionSec = codedInputByteBufferNano.readInt32();
                    break;
                case 48:
                    this.satelliteConnectionGapMinSec = codedInputByteBufferNano.readInt32();
                    break;
                case 56:
                    this.satelliteConnectionGapAvgSec = codedInputByteBufferNano.readInt32();
                    break;
                case 64:
                    this.satelliteConnectionGapMaxSec = codedInputByteBufferNano.readInt32();
                    break;
                case 72:
                    this.rsrpAvg = codedInputByteBufferNano.readInt32();
                    break;
                case 80:
                    this.rsrpMedian = codedInputByteBufferNano.readInt32();
                    break;
                case 88:
                    this.rssnrAvg = codedInputByteBufferNano.readInt32();
                    break;
                case 96:
                    this.rssnrMedian = codedInputByteBufferNano.readInt32();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE /* 104 */:
                    this.countOfIncomingSms = codedInputByteBufferNano.readInt32();
                    break;
                case 112:
                    this.countOfOutgoingSms = codedInputByteBufferNano.readInt32();
                    break;
                case 120:
                    this.countOfIncomingMms = codedInputByteBufferNano.readInt32();
                    break;
                case 128:
                    this.countOfOutgoingMms = codedInputByteBufferNano.readInt32();
                    break;
                case 136:
                    int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 136);
                    int[] iArr = this.supportedSatelliteServices;
                    int length = iArr == null ? 0 : iArr.length;
                    int i = repeatedFieldArrayLength + length;
                    int[] iArr2 = new int[i];
                    if (length != 0) {
                        System.arraycopy(iArr, 0, iArr2, 0, length);
                    }
                    while (length < i - 1) {
                        iArr2[length] = codedInputByteBufferNano.readInt32();
                        codedInputByteBufferNano.readTag();
                        length++;
                    }
                    iArr2[length] = codedInputByteBufferNano.readInt32();
                    this.supportedSatelliteServices = iArr2;
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_SEND_DEVICE_STATE /* 138 */:
                    int iPushLimit = codedInputByteBufferNano.pushLimit(codedInputByteBufferNano.readRawVarint32());
                    int position = codedInputByteBufferNano.getPosition();
                    int i2 = 0;
                    while (codedInputByteBufferNano.getBytesUntilLimit() > 0) {
                        codedInputByteBufferNano.readInt32();
                        i2++;
                    }
                    codedInputByteBufferNano.rewindToPosition(position);
                    int[] iArr3 = this.supportedSatelliteServices;
                    int length2 = iArr3 == null ? 0 : iArr3.length;
                    int i3 = i2 + length2;
                    int[] iArr4 = new int[i3];
                    if (length2 != 0) {
                        System.arraycopy(iArr3, 0, iArr4, 0, length2);
                    }
                    while (length2 < i3) {
                        iArr4[length2] = codedInputByteBufferNano.readInt32();
                        length2++;
                    }
                    this.supportedSatelliteServices = iArr4;
                    codedInputByteBufferNano.popLimit(iPushLimit);
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_START_KEEPALIVE /* 144 */:
                    this.serviceDataPolicy = codedInputByteBufferNano.readInt32();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_DEVICE_IMEI /* 152 */:
                    this.satelliteDataConsumedBytes = codedInputByteBufferNano.readInt64();
                    break;
                case SmsMessage.MAX_USER_DATA_SEPTETS /* 160 */:
                    this.isMultiSim = codedInputByteBufferNano.readBool();
                    break;
                case 168:
                    this.isNbIotNtn = codedInputByteBufferNano.readBool();
                    break;
                case 176:
                    this.countOfDataConnections = codedInputByteBufferNano.readInt32();
                    break;
                case 184:
                    int repeatedFieldArrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 184);
                    int[] iArr5 = this.lastFailCauses;
                    int length3 = iArr5 == null ? 0 : iArr5.length;
                    int i4 = repeatedFieldArrayLength2 + length3;
                    int[] iArr6 = new int[i4];
                    if (length3 != 0) {
                        System.arraycopy(iArr5, 0, iArr6, 0, length3);
                    }
                    while (length3 < i4 - 1) {
                        iArr6[length3] = codedInputByteBufferNano.readInt32();
                        codedInputByteBufferNano.readTag();
                        length3++;
                    }
                    iArr6[length3] = codedInputByteBufferNano.readInt32();
                    this.lastFailCauses = iArr6;
                    break;
                case 186:
                    int iPushLimit2 = codedInputByteBufferNano.pushLimit(codedInputByteBufferNano.readRawVarint32());
                    int position2 = codedInputByteBufferNano.getPosition();
                    int i5 = 0;
                    while (codedInputByteBufferNano.getBytesUntilLimit() > 0) {
                        codedInputByteBufferNano.readInt32();
                        i5++;
                    }
                    codedInputByteBufferNano.rewindToPosition(position2);
                    int[] iArr7 = this.lastFailCauses;
                    int length4 = iArr7 == null ? 0 : iArr7.length;
                    int i6 = i5 + length4;
                    int[] iArr8 = new int[i6];
                    if (length4 != 0) {
                        System.arraycopy(iArr7, 0, iArr8, 0, length4);
                    }
                    while (length4 < i6) {
                        iArr8[length4] = codedInputByteBufferNano.readInt32();
                        length4++;
                    }
                    this.lastFailCauses = iArr8;
                    codedInputByteBufferNano.popLimit(iPushLimit2);
                    break;
                case DnsPacket.DnsRecord.NAME_COMPRESSION /* 192 */:
                    this.countOfDataDisconnections = codedInputByteBufferNano.readInt32();
                    break;
                case 200:
                    this.countOfDataStalls = codedInputByteBufferNano.readInt32();
                    break;
                case 208:
                    this.averageUplinkBandwidthKbps = codedInputByteBufferNano.readInt32();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_RELEASE_PDU_SESSION_ID /* 216 */:
                    this.averageDownlinkBandwidthKbps = codedInputByteBufferNano.readInt32();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_SLICING_CONFIG /* 224 */:
                    this.minUplinkBandwidthKbps = codedInputByteBufferNano.readInt32();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_EXIT_EMERGENCY_MODE /* 232 */:
                    this.maxUplinkBandwidthKbps = codedInputByteBufferNano.readInt32();
                    break;
                case 240:
                    this.minDownlinkBandwidthKbps = codedInputByteBufferNano.readInt32();
                    break;
                case 248:
                    this.maxDownlinkBandwidthKbps = codedInputByteBufferNano.readInt32();
                    break;
                case CallFailCause.RADIO_RELEASE_NORMAL /* 258 */:
                    int repeatedFieldArrayLength3 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, CallFailCause.RADIO_RELEASE_NORMAL);
                    String[] strArr = this.satelliteSupportedApps;
                    int length5 = strArr == null ? 0 : strArr.length;
                    int i7 = repeatedFieldArrayLength3 + length5;
                    String[] strArr2 = new String[i7];
                    if (length5 != 0) {
                        System.arraycopy(strArr, 0, strArr2, 0, length5);
                    }
                    while (length5 < i7 - 1) {
                        strArr2[length5] = codedInputByteBufferNano.readString();
                        codedInputByteBufferNano.readTag();
                        length5++;
                    }
                    strArr2[length5] = codedInputByteBufferNano.readString();
                    this.satelliteSupportedApps = strArr2;
                    break;
                case 264:
                    int repeatedFieldArrayLength4 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 264);
                    int[] iArr9 = this.satelliteSupportedUids;
                    int length6 = iArr9 == null ? 0 : iArr9.length;
                    int i8 = repeatedFieldArrayLength4 + length6;
                    int[] iArr10 = new int[i8];
                    if (length6 != 0) {
                        System.arraycopy(iArr9, 0, iArr10, 0, length6);
                    }
                    while (length6 < i8 - 1) {
                        iArr10[length6] = codedInputByteBufferNano.readInt32();
                        codedInputByteBufferNano.readTag();
                        length6++;
                    }
                    iArr10[length6] = codedInputByteBufferNano.readInt32();
                    this.satelliteSupportedUids = iArr10;
                    break;
                case 266:
                    int iPushLimit3 = codedInputByteBufferNano.pushLimit(codedInputByteBufferNano.readRawVarint32());
                    int position3 = codedInputByteBufferNano.getPosition();
                    int i9 = 0;
                    while (codedInputByteBufferNano.getBytesUntilLimit() > 0) {
                        codedInputByteBufferNano.readInt32();
                        i9++;
                    }
                    codedInputByteBufferNano.rewindToPosition(position3);
                    int[] iArr11 = this.satelliteSupportedUids;
                    int length7 = iArr11 == null ? 0 : iArr11.length;
                    int i10 = i9 + length7;
                    int[] iArr12 = new int[i10];
                    if (length7 != 0) {
                        System.arraycopy(iArr11, 0, iArr12, 0, length7);
                    }
                    while (length7 < i10) {
                        iArr12[length7] = codedInputByteBufferNano.readInt32();
                        length7++;
                    }
                    this.satelliteSupportedUids = iArr12;
                    codedInputByteBufferNano.popLimit(iPushLimit3);
                    break;
                case 272:
                    int repeatedFieldArrayLength5 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 272);
                    long[] jArr = this.perAppSatelliteDataConsumedBytes;
                    int length8 = jArr == null ? 0 : jArr.length;
                    int i11 = repeatedFieldArrayLength5 + length8;
                    long[] jArr2 = new long[i11];
                    if (length8 != 0) {
                        System.arraycopy(jArr, 0, jArr2, 0, length8);
                    }
                    while (length8 < i11 - 1) {
                        jArr2[length8] = codedInputByteBufferNano.readInt64();
                        codedInputByteBufferNano.readTag();
                        length8++;
                    }
                    jArr2[length8] = codedInputByteBufferNano.readInt64();
                    this.perAppSatelliteDataConsumedBytes = jArr2;
                    break;
                case 274:
                    int iPushLimit4 = codedInputByteBufferNano.pushLimit(codedInputByteBufferNano.readRawVarint32());
                    int position4 = codedInputByteBufferNano.getPosition();
                    int i12 = 0;
                    while (codedInputByteBufferNano.getBytesUntilLimit() > 0) {
                        codedInputByteBufferNano.readInt64();
                        i12++;
                    }
                    codedInputByteBufferNano.rewindToPosition(position4);
                    long[] jArr3 = this.perAppSatelliteDataConsumedBytes;
                    int length9 = jArr3 == null ? 0 : jArr3.length;
                    int i13 = i12 + length9;
                    long[] jArr4 = new long[i13];
                    if (length9 != 0) {
                        System.arraycopy(jArr3, 0, jArr4, 0, length9);
                    }
                    while (length9 < i13) {
                        jArr4[length9] = codedInputByteBufferNano.readInt64();
                        length9++;
                    }
                    this.perAppSatelliteDataConsumedBytes = jArr4;
                    codedInputByteBufferNano.popLimit(iPushLimit4);
                    break;
                case 280:
                    this.supportedConnectionMode = codedInputByteBufferNano.readInt32();
                    break;
                case 288:
                    this.sessionConnectionMode = codedInputByteBufferNano.readInt32();
                    break;
                case 298:
                    this.plmn = codedInputByteBufferNano.readString();
                    break;
                case 304:
                    this.isWifiEnabled = codedInputByteBufferNano.readBool();
                    break;
                case 312:
                    this.isWfcEnabled = codedInputByteBufferNano.readBool();
                    break;
                case 320:
                    this.isWfcRegistered = codedInputByteBufferNano.readBool();
                    break;
                case 328:
                    this.screenOnTimeSec = codedInputByteBufferNano.readInt32();
                    break;
                case 336:
                    this.batteryLevelDropPercent = codedInputByteBufferNano.readInt32();
                    break;
                case 344:
                    this.wasChargingDuringSession = codedInputByteBufferNano.readBool();
                    break;
                case 352:
                    this.batteryDesignCapacityMah = codedInputByteBufferNano.readInt32();
                    break;
                case 360:
                    this.energyConsumedNwh = codedInputByteBufferNano.readInt64();
                    break;
                case 368:
                    this.eligibilitySource = codedInputByteBufferNano.readInt32();
                    break;
                case 376:
                    this.isWifiConnected = codedInputByteBufferNano.readBool();
                    break;
                case 384:
                    this.countOfNonEmergencyDialerDialogDisplayed = codedInputByteBufferNano.readInt32();
                    break;
                case 392:
                    this.countOfEmergencyDialerButtonDisplayed = codedInputByteBufferNano.readInt32();
                    break;
                case 400:
                    this.countOfSatelliteNotificationDisplayed = codedInputByteBufferNano.readInt32();
                    break;
                case 408:
                    this.totalRxDataBytes = codedInputByteBufferNano.readInt64();
                    break;
                case 416:
                    this.totalTxDataBytes = codedInputByteBufferNano.readInt64();
                    break;
                case 424:
                    int repeatedFieldArrayLength6 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 424);
                    long[] jArr5 = this.perAppRxDataBytes;
                    int length10 = jArr5 == null ? 0 : jArr5.length;
                    int i14 = repeatedFieldArrayLength6 + length10;
                    long[] jArr6 = new long[i14];
                    if (length10 != 0) {
                        System.arraycopy(jArr5, 0, jArr6, 0, length10);
                    }
                    while (length10 < i14 - 1) {
                        jArr6[length10] = codedInputByteBufferNano.readInt64();
                        codedInputByteBufferNano.readTag();
                        length10++;
                    }
                    jArr6[length10] = codedInputByteBufferNano.readInt64();
                    this.perAppRxDataBytes = jArr6;
                    break;
                case 426:
                    int iPushLimit5 = codedInputByteBufferNano.pushLimit(codedInputByteBufferNano.readRawVarint32());
                    int position5 = codedInputByteBufferNano.getPosition();
                    int i15 = 0;
                    while (codedInputByteBufferNano.getBytesUntilLimit() > 0) {
                        codedInputByteBufferNano.readInt64();
                        i15++;
                    }
                    codedInputByteBufferNano.rewindToPosition(position5);
                    long[] jArr7 = this.perAppRxDataBytes;
                    int length11 = jArr7 == null ? 0 : jArr7.length;
                    int i16 = i15 + length11;
                    long[] jArr8 = new long[i16];
                    if (length11 != 0) {
                        System.arraycopy(jArr7, 0, jArr8, 0, length11);
                    }
                    while (length11 < i16) {
                        jArr8[length11] = codedInputByteBufferNano.readInt64();
                        length11++;
                    }
                    this.perAppRxDataBytes = jArr8;
                    codedInputByteBufferNano.popLimit(iPushLimit5);
                    break;
                case 432:
                    int repeatedFieldArrayLength7 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 432);
                    long[] jArr9 = this.perAppTxDataBytes;
                    int length12 = jArr9 == null ? 0 : jArr9.length;
                    int i17 = repeatedFieldArrayLength7 + length12;
                    long[] jArr10 = new long[i17];
                    if (length12 != 0) {
                        System.arraycopy(jArr9, 0, jArr10, 0, length12);
                    }
                    while (length12 < i17 - 1) {
                        jArr10[length12] = codedInputByteBufferNano.readInt64();
                        codedInputByteBufferNano.readTag();
                        length12++;
                    }
                    jArr10[length12] = codedInputByteBufferNano.readInt64();
                    this.perAppTxDataBytes = jArr10;
                    break;
                case 434:
                    int iPushLimit6 = codedInputByteBufferNano.pushLimit(codedInputByteBufferNano.readRawVarint32());
                    int position6 = codedInputByteBufferNano.getPosition();
                    int i18 = 0;
                    while (codedInputByteBufferNano.getBytesUntilLimit() > 0) {
                        codedInputByteBufferNano.readInt64();
                        i18++;
                    }
                    codedInputByteBufferNano.rewindToPosition(position6);
                    long[] jArr11 = this.perAppTxDataBytes;
                    int length13 = jArr11 == null ? 0 : jArr11.length;
                    int i19 = i18 + length13;
                    long[] jArr12 = new long[i19];
                    if (length13 != 0) {
                        System.arraycopy(jArr11, 0, jArr12, 0, length13);
                    }
                    while (length13 < i19) {
                        jArr12[length13] = codedInputByteBufferNano.readInt64();
                        length13++;
                    }
                    this.perAppTxDataBytes = jArr12;
                    codedInputByteBufferNano.popLimit(iPushLimit6);
                    break;
                default:
                    if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    }
                    break;
            }
        }
        return this;
    }

    public static PersistAtomsProto$CarrierRoamingSatelliteSession parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (PersistAtomsProto$CarrierRoamingSatelliteSession) MessageNano.mergeFrom(new PersistAtomsProto$CarrierRoamingSatelliteSession(), bArr);
    }

    public static PersistAtomsProto$CarrierRoamingSatelliteSession parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new PersistAtomsProto$CarrierRoamingSatelliteSession().mergeFrom(codedInputByteBufferNano);
    }
}
