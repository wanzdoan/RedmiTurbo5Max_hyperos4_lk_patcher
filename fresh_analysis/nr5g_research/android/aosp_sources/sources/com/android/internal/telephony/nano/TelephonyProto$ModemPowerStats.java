package com.android.internal.telephony.nano;

import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import com.android.internal.telephony.protobuf.nano.WireFormatNano;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class TelephonyProto$ModemPowerStats extends ExtendableMessageNano<TelephonyProto$ModemPowerStats> {
    private static volatile TelephonyProto$ModemPowerStats[] _emptyArray;
    public long cellularKernelActiveTimeMs;
    public double energyConsumedMah;
    public long idleTimeMs;
    public long loggingDurationMs;
    public double monitoredRailEnergyConsumedMah;
    public long numBytesRx;
    public long numBytesTx;
    public long numPacketsRx;
    public long numPacketsTx;
    public long rxTimeMs;
    public long sleepTimeMs;
    public long[] timeInRatMs;
    public long[] timeInRxSignalStrengthLevelMs;
    public long timeInVeryPoorRxSignalLevelMs;
    public long[] txTimeMs;

    public static TelephonyProto$ModemPowerStats[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new TelephonyProto$ModemPowerStats[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public TelephonyProto$ModemPowerStats() {
        clear();
    }

    public TelephonyProto$ModemPowerStats clear() {
        this.loggingDurationMs = 0L;
        this.energyConsumedMah = 0.0d;
        this.numPacketsTx = 0L;
        this.cellularKernelActiveTimeMs = 0L;
        this.timeInVeryPoorRxSignalLevelMs = 0L;
        this.sleepTimeMs = 0L;
        this.idleTimeMs = 0L;
        this.rxTimeMs = 0L;
        long[] jArr = WireFormatNano.EMPTY_LONG_ARRAY;
        this.txTimeMs = jArr;
        this.numBytesTx = 0L;
        this.numPacketsRx = 0L;
        this.numBytesRx = 0L;
        this.timeInRatMs = jArr;
        this.timeInRxSignalStrengthLevelMs = jArr;
        this.monitoredRailEnergyConsumedMah = 0.0d;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        long j = this.loggingDurationMs;
        if (j != 0) {
            codedOutputByteBufferNano.writeInt64(1, j);
        }
        if (Double.doubleToLongBits(this.energyConsumedMah) != Double.doubleToLongBits(0.0d)) {
            codedOutputByteBufferNano.writeDouble(2, this.energyConsumedMah);
        }
        long j2 = this.numPacketsTx;
        if (j2 != 0) {
            codedOutputByteBufferNano.writeInt64(3, j2);
        }
        long j3 = this.cellularKernelActiveTimeMs;
        if (j3 != 0) {
            codedOutputByteBufferNano.writeInt64(4, j3);
        }
        long j4 = this.timeInVeryPoorRxSignalLevelMs;
        if (j4 != 0) {
            codedOutputByteBufferNano.writeInt64(5, j4);
        }
        long j5 = this.sleepTimeMs;
        if (j5 != 0) {
            codedOutputByteBufferNano.writeInt64(6, j5);
        }
        long j6 = this.idleTimeMs;
        if (j6 != 0) {
            codedOutputByteBufferNano.writeInt64(7, j6);
        }
        long j7 = this.rxTimeMs;
        if (j7 != 0) {
            codedOutputByteBufferNano.writeInt64(8, j7);
        }
        long[] jArr = this.txTimeMs;
        int i = 0;
        if (jArr != null && jArr.length > 0) {
            int i2 = 0;
            while (true) {
                long[] jArr2 = this.txTimeMs;
                if (i2 >= jArr2.length) {
                    break;
                }
                codedOutputByteBufferNano.writeInt64(9, jArr2[i2]);
                i2++;
            }
        }
        long j8 = this.numBytesTx;
        if (j8 != 0) {
            codedOutputByteBufferNano.writeInt64(10, j8);
        }
        long j9 = this.numPacketsRx;
        if (j9 != 0) {
            codedOutputByteBufferNano.writeInt64(11, j9);
        }
        long j10 = this.numBytesRx;
        if (j10 != 0) {
            codedOutputByteBufferNano.writeInt64(12, j10);
        }
        long[] jArr3 = this.timeInRatMs;
        if (jArr3 != null && jArr3.length > 0) {
            int i3 = 0;
            while (true) {
                long[] jArr4 = this.timeInRatMs;
                if (i3 >= jArr4.length) {
                    break;
                }
                codedOutputByteBufferNano.writeInt64(13, jArr4[i3]);
                i3++;
            }
        }
        long[] jArr5 = this.timeInRxSignalStrengthLevelMs;
        if (jArr5 != null && jArr5.length > 0) {
            while (true) {
                long[] jArr6 = this.timeInRxSignalStrengthLevelMs;
                if (i >= jArr6.length) {
                    break;
                }
                codedOutputByteBufferNano.writeInt64(14, jArr6[i]);
                i++;
            }
        }
        if (Double.doubleToLongBits(this.monitoredRailEnergyConsumedMah) != Double.doubleToLongBits(0.0d)) {
            codedOutputByteBufferNano.writeDouble(15, this.monitoredRailEnergyConsumedMah);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        long[] jArr;
        long[] jArr2;
        long[] jArr3;
        int iComputeSerializedSize = super.computeSerializedSize();
        long j = this.loggingDurationMs;
        if (j != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(1, j);
        }
        if (Double.doubleToLongBits(this.energyConsumedMah) != Double.doubleToLongBits(0.0d)) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeDoubleSize(2, this.energyConsumedMah);
        }
        long j2 = this.numPacketsTx;
        if (j2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(3, j2);
        }
        long j3 = this.cellularKernelActiveTimeMs;
        if (j3 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(4, j3);
        }
        long j4 = this.timeInVeryPoorRxSignalLevelMs;
        if (j4 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(5, j4);
        }
        long j5 = this.sleepTimeMs;
        if (j5 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(6, j5);
        }
        long j6 = this.idleTimeMs;
        if (j6 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(7, j6);
        }
        long j7 = this.rxTimeMs;
        if (j7 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(8, j7);
        }
        long[] jArr4 = this.txTimeMs;
        int i = 0;
        if (jArr4 != null && jArr4.length > 0) {
            int i2 = 0;
            int iComputeInt64SizeNoTag = 0;
            while (true) {
                jArr3 = this.txTimeMs;
                if (i2 >= jArr3.length) {
                    break;
                }
                iComputeInt64SizeNoTag += CodedOutputByteBufferNano.computeInt64SizeNoTag(jArr3[i2]);
                i2++;
            }
            iComputeSerializedSize = iComputeSerializedSize + iComputeInt64SizeNoTag + jArr3.length;
        }
        long j8 = this.numBytesTx;
        if (j8 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(10, j8);
        }
        long j9 = this.numPacketsRx;
        if (j9 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(11, j9);
        }
        long j10 = this.numBytesRx;
        if (j10 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(12, j10);
        }
        long[] jArr5 = this.timeInRatMs;
        if (jArr5 != null && jArr5.length > 0) {
            int i3 = 0;
            int iComputeInt64SizeNoTag2 = 0;
            while (true) {
                jArr2 = this.timeInRatMs;
                if (i3 >= jArr2.length) {
                    break;
                }
                iComputeInt64SizeNoTag2 += CodedOutputByteBufferNano.computeInt64SizeNoTag(jArr2[i3]);
                i3++;
            }
            iComputeSerializedSize = iComputeSerializedSize + iComputeInt64SizeNoTag2 + jArr2.length;
        }
        long[] jArr6 = this.timeInRxSignalStrengthLevelMs;
        if (jArr6 != null && jArr6.length > 0) {
            int iComputeInt64SizeNoTag3 = 0;
            while (true) {
                jArr = this.timeInRxSignalStrengthLevelMs;
                if (i >= jArr.length) {
                    break;
                }
                iComputeInt64SizeNoTag3 += CodedOutputByteBufferNano.computeInt64SizeNoTag(jArr[i]);
                i++;
            }
            iComputeSerializedSize = iComputeSerializedSize + iComputeInt64SizeNoTag3 + jArr.length;
        }
        return Double.doubleToLongBits(this.monitoredRailEnergyConsumedMah) != Double.doubleToLongBits(0.0d) ? iComputeSerializedSize + CodedOutputByteBufferNano.computeDoubleSize(15, this.monitoredRailEnergyConsumedMah) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public TelephonyProto$ModemPowerStats mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            switch (tag) {
                case 0:
                    break;
                case 8:
                    this.loggingDurationMs = codedInputByteBufferNano.readInt64();
                    break;
                case 17:
                    this.energyConsumedMah = codedInputByteBufferNano.readDouble();
                    break;
                case 24:
                    this.numPacketsTx = codedInputByteBufferNano.readInt64();
                    break;
                case 32:
                    this.cellularKernelActiveTimeMs = codedInputByteBufferNano.readInt64();
                    break;
                case 40:
                    this.timeInVeryPoorRxSignalLevelMs = codedInputByteBufferNano.readInt64();
                    break;
                case 48:
                    this.sleepTimeMs = codedInputByteBufferNano.readInt64();
                    break;
                case 56:
                    this.idleTimeMs = codedInputByteBufferNano.readInt64();
                    break;
                case 64:
                    this.rxTimeMs = codedInputByteBufferNano.readInt64();
                    break;
                case 72:
                    int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 72);
                    long[] jArr = this.txTimeMs;
                    int length = jArr == null ? 0 : jArr.length;
                    int i = repeatedFieldArrayLength + length;
                    long[] jArr2 = new long[i];
                    if (length != 0) {
                        System.arraycopy(jArr, 0, jArr2, 0, length);
                    }
                    while (length < i - 1) {
                        jArr2[length] = codedInputByteBufferNano.readInt64();
                        codedInputByteBufferNano.readTag();
                        length++;
                    }
                    jArr2[length] = codedInputByteBufferNano.readInt64();
                    this.txTimeMs = jArr2;
                    break;
                case 74:
                    int iPushLimit = codedInputByteBufferNano.pushLimit(codedInputByteBufferNano.readRawVarint32());
                    int position = codedInputByteBufferNano.getPosition();
                    int i2 = 0;
                    while (codedInputByteBufferNano.getBytesUntilLimit() > 0) {
                        codedInputByteBufferNano.readInt64();
                        i2++;
                    }
                    codedInputByteBufferNano.rewindToPosition(position);
                    long[] jArr3 = this.txTimeMs;
                    int length2 = jArr3 == null ? 0 : jArr3.length;
                    int i3 = i2 + length2;
                    long[] jArr4 = new long[i3];
                    if (length2 != 0) {
                        System.arraycopy(jArr3, 0, jArr4, 0, length2);
                    }
                    while (length2 < i3) {
                        jArr4[length2] = codedInputByteBufferNano.readInt64();
                        length2++;
                    }
                    this.txTimeMs = jArr4;
                    codedInputByteBufferNano.popLimit(iPushLimit);
                    break;
                case 80:
                    this.numBytesTx = codedInputByteBufferNano.readInt64();
                    break;
                case 88:
                    this.numPacketsRx = codedInputByteBufferNano.readInt64();
                    break;
                case 96:
                    this.numBytesRx = codedInputByteBufferNano.readInt64();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE /* 104 */:
                    int repeatedFieldArrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE);
                    long[] jArr5 = this.timeInRatMs;
                    int length3 = jArr5 == null ? 0 : jArr5.length;
                    int i4 = repeatedFieldArrayLength2 + length3;
                    long[] jArr6 = new long[i4];
                    if (length3 != 0) {
                        System.arraycopy(jArr5, 0, jArr6, 0, length3);
                    }
                    while (length3 < i4 - 1) {
                        jArr6[length3] = codedInputByteBufferNano.readInt64();
                        codedInputByteBufferNano.readTag();
                        length3++;
                    }
                    jArr6[length3] = codedInputByteBufferNano.readInt64();
                    this.timeInRatMs = jArr6;
                    break;
                case 106:
                    int iPushLimit2 = codedInputByteBufferNano.pushLimit(codedInputByteBufferNano.readRawVarint32());
                    int position2 = codedInputByteBufferNano.getPosition();
                    int i5 = 0;
                    while (codedInputByteBufferNano.getBytesUntilLimit() > 0) {
                        codedInputByteBufferNano.readInt64();
                        i5++;
                    }
                    codedInputByteBufferNano.rewindToPosition(position2);
                    long[] jArr7 = this.timeInRatMs;
                    int length4 = jArr7 == null ? 0 : jArr7.length;
                    int i6 = i5 + length4;
                    long[] jArr8 = new long[i6];
                    if (length4 != 0) {
                        System.arraycopy(jArr7, 0, jArr8, 0, length4);
                    }
                    while (length4 < i6) {
                        jArr8[length4] = codedInputByteBufferNano.readInt64();
                        length4++;
                    }
                    this.timeInRatMs = jArr8;
                    codedInputByteBufferNano.popLimit(iPushLimit2);
                    break;
                case 112:
                    int repeatedFieldArrayLength3 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 112);
                    long[] jArr9 = this.timeInRxSignalStrengthLevelMs;
                    int length5 = jArr9 == null ? 0 : jArr9.length;
                    int i7 = repeatedFieldArrayLength3 + length5;
                    long[] jArr10 = new long[i7];
                    if (length5 != 0) {
                        System.arraycopy(jArr9, 0, jArr10, 0, length5);
                    }
                    while (length5 < i7 - 1) {
                        jArr10[length5] = codedInputByteBufferNano.readInt64();
                        codedInputByteBufferNano.readTag();
                        length5++;
                    }
                    jArr10[length5] = codedInputByteBufferNano.readInt64();
                    this.timeInRxSignalStrengthLevelMs = jArr10;
                    break;
                case 114:
                    int iPushLimit3 = codedInputByteBufferNano.pushLimit(codedInputByteBufferNano.readRawVarint32());
                    int position3 = codedInputByteBufferNano.getPosition();
                    int i8 = 0;
                    while (codedInputByteBufferNano.getBytesUntilLimit() > 0) {
                        codedInputByteBufferNano.readInt64();
                        i8++;
                    }
                    codedInputByteBufferNano.rewindToPosition(position3);
                    long[] jArr11 = this.timeInRxSignalStrengthLevelMs;
                    int length6 = jArr11 == null ? 0 : jArr11.length;
                    int i9 = i8 + length6;
                    long[] jArr12 = new long[i9];
                    if (length6 != 0) {
                        System.arraycopy(jArr11, 0, jArr12, 0, length6);
                    }
                    while (length6 < i9) {
                        jArr12[length6] = codedInputByteBufferNano.readInt64();
                        length6++;
                    }
                    this.timeInRxSignalStrengthLevelMs = jArr12;
                    codedInputByteBufferNano.popLimit(iPushLimit3);
                    break;
                case 121:
                    this.monitoredRailEnergyConsumedMah = codedInputByteBufferNano.readDouble();
                    break;
                default:
                    if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    }
                    break;
            }
        }
        return this;
    }

    public static TelephonyProto$ModemPowerStats parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (TelephonyProto$ModemPowerStats) MessageNano.mergeFrom(new TelephonyProto$ModemPowerStats(), bArr);
    }

    public static TelephonyProto$ModemPowerStats parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new TelephonyProto$ModemPowerStats().mergeFrom(codedInputByteBufferNano);
    }
}
