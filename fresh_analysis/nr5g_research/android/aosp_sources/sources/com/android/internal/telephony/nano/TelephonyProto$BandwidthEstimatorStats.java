package com.android.internal.telephony.nano;

import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import com.android.internal.telephony.protobuf.nano.WireFormatNano;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class TelephonyProto$BandwidthEstimatorStats extends ExtendableMessageNano<TelephonyProto$BandwidthEstimatorStats> {
    private static volatile TelephonyProto$BandwidthEstimatorStats[] _emptyArray;
    public PerRat[] perRatRx;
    public PerRat[] perRatTx;

    public static final class PerLevel extends ExtendableMessageNano<PerLevel> {
        private static volatile PerLevel[] _emptyArray;
        public int avgBwKbps;
        public int bwEstErrorPercent;
        public int count;
        public int signalLevel;
        public int staticBwErrorPercent;

        public static PerLevel[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    try {
                        if (_emptyArray == null) {
                            _emptyArray = new PerLevel[0];
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
            return _emptyArray;
        }

        public PerLevel() {
            clear();
        }

        public PerLevel clear() {
            this.signalLevel = 0;
            this.count = 0;
            this.avgBwKbps = 0;
            this.staticBwErrorPercent = 0;
            this.bwEstErrorPercent = 0;
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }

        @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            int i = this.signalLevel;
            if (i != 0) {
                codedOutputByteBufferNano.writeUInt32(1, i);
            }
            int i2 = this.count;
            if (i2 != 0) {
                codedOutputByteBufferNano.writeUInt32(2, i2);
            }
            int i3 = this.avgBwKbps;
            if (i3 != 0) {
                codedOutputByteBufferNano.writeUInt32(3, i3);
            }
            int i4 = this.staticBwErrorPercent;
            if (i4 != 0) {
                codedOutputByteBufferNano.writeUInt32(4, i4);
            }
            int i5 = this.bwEstErrorPercent;
            if (i5 != 0) {
                codedOutputByteBufferNano.writeUInt32(5, i5);
            }
            super.writeTo(codedOutputByteBufferNano);
        }

        @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            int i = this.signalLevel;
            if (i != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeUInt32Size(1, i);
            }
            int i2 = this.count;
            if (i2 != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeUInt32Size(2, i2);
            }
            int i3 = this.avgBwKbps;
            if (i3 != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeUInt32Size(3, i3);
            }
            int i4 = this.staticBwErrorPercent;
            if (i4 != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeUInt32Size(4, i4);
            }
            int i5 = this.bwEstErrorPercent;
            return i5 != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeUInt32Size(5, i5) : iComputeSerializedSize;
        }

        @Override // com.android.internal.telephony.protobuf.nano.MessageNano
        public PerLevel mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    break;
                }
                if (tag == 8) {
                    this.signalLevel = codedInputByteBufferNano.readUInt32();
                } else if (tag == 16) {
                    this.count = codedInputByteBufferNano.readUInt32();
                } else if (tag == 24) {
                    this.avgBwKbps = codedInputByteBufferNano.readUInt32();
                } else if (tag == 32) {
                    this.staticBwErrorPercent = codedInputByteBufferNano.readUInt32();
                } else if (tag != 40) {
                    if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                        break;
                    }
                } else {
                    this.bwEstErrorPercent = codedInputByteBufferNano.readUInt32();
                }
            }
            return this;
        }

        public static PerLevel parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
            return (PerLevel) MessageNano.mergeFrom(new PerLevel(), bArr);
        }

        public static PerLevel parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new PerLevel().mergeFrom(codedInputByteBufferNano);
        }
    }

    public static final class PerRat extends ExtendableMessageNano<PerRat> {
        private static volatile PerRat[] _emptyArray;
        public int nrMode;
        public PerLevel[] perLevel;
        public int rat;

        public static PerRat[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    try {
                        if (_emptyArray == null) {
                            _emptyArray = new PerRat[0];
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
            return _emptyArray;
        }

        public PerRat() {
            clear();
        }

        public PerRat clear() {
            this.rat = -1;
            this.nrMode = 1;
            this.perLevel = PerLevel.emptyArray();
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }

        @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            int i = this.rat;
            if (i != -1) {
                codedOutputByteBufferNano.writeInt32(1, i);
            }
            int i2 = this.nrMode;
            if (i2 != 1) {
                codedOutputByteBufferNano.writeInt32(2, i2);
            }
            PerLevel[] perLevelArr = this.perLevel;
            if (perLevelArr != null && perLevelArr.length > 0) {
                int i3 = 0;
                while (true) {
                    PerLevel[] perLevelArr2 = this.perLevel;
                    if (i3 >= perLevelArr2.length) {
                        break;
                    }
                    PerLevel perLevel = perLevelArr2[i3];
                    if (perLevel != null) {
                        codedOutputByteBufferNano.writeMessage(3, perLevel);
                    }
                    i3++;
                }
            }
            super.writeTo(codedOutputByteBufferNano);
        }

        @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            int i = this.rat;
            if (i != -1) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            int i2 = this.nrMode;
            if (i2 != 1) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i2);
            }
            PerLevel[] perLevelArr = this.perLevel;
            if (perLevelArr != null && perLevelArr.length > 0) {
                int i3 = 0;
                while (true) {
                    PerLevel[] perLevelArr2 = this.perLevel;
                    if (i3 >= perLevelArr2.length) {
                        break;
                    }
                    PerLevel perLevel = perLevelArr2[i3];
                    if (perLevel != null) {
                        iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(3, perLevel);
                    }
                    i3++;
                }
            }
            return iComputeSerializedSize;
        }

        @Override // com.android.internal.telephony.protobuf.nano.MessageNano
        public PerRat mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                int tag = codedInputByteBufferNano.readTag();
                if (tag != 0) {
                    if (tag == 8) {
                        int int32 = codedInputByteBufferNano.readInt32();
                        switch (int32) {
                            case -1:
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                            case 9:
                            case 10:
                            case 11:
                            case 12:
                            case 13:
                            case 14:
                            case 15:
                            case 16:
                            case 17:
                            case 18:
                            case 19:
                            case 20:
                                this.rat = int32;
                                break;
                        }
                    } else if (tag == 16) {
                        int int33 = codedInputByteBufferNano.readInt32();
                        if (int33 == 1 || int33 == 2 || int33 == 3 || int33 == 4 || int33 == 5) {
                            this.nrMode = int33;
                        }
                    } else if (tag != 26) {
                        if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                        }
                    } else {
                        int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 26);
                        PerLevel[] perLevelArr = this.perLevel;
                        int length = perLevelArr == null ? 0 : perLevelArr.length;
                        int i = repeatedFieldArrayLength + length;
                        PerLevel[] perLevelArr2 = new PerLevel[i];
                        if (length != 0) {
                            System.arraycopy(perLevelArr, 0, perLevelArr2, 0, length);
                        }
                        while (length < i - 1) {
                            PerLevel perLevel = new PerLevel();
                            perLevelArr2[length] = perLevel;
                            codedInputByteBufferNano.readMessage(perLevel);
                            codedInputByteBufferNano.readTag();
                            length++;
                        }
                        PerLevel perLevel2 = new PerLevel();
                        perLevelArr2[length] = perLevel2;
                        codedInputByteBufferNano.readMessage(perLevel2);
                        this.perLevel = perLevelArr2;
                    }
                }
            }
            return this;
        }

        public static PerRat parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
            return (PerRat) MessageNano.mergeFrom(new PerRat(), bArr);
        }

        public static PerRat parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new PerRat().mergeFrom(codedInputByteBufferNano);
        }
    }

    public static TelephonyProto$BandwidthEstimatorStats[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new TelephonyProto$BandwidthEstimatorStats[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public TelephonyProto$BandwidthEstimatorStats() {
        clear();
    }

    public TelephonyProto$BandwidthEstimatorStats clear() {
        this.perRatTx = PerRat.emptyArray();
        this.perRatRx = PerRat.emptyArray();
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        PerRat[] perRatArr = this.perRatTx;
        int i = 0;
        if (perRatArr != null && perRatArr.length > 0) {
            int i2 = 0;
            while (true) {
                PerRat[] perRatArr2 = this.perRatTx;
                if (i2 >= perRatArr2.length) {
                    break;
                }
                PerRat perRat = perRatArr2[i2];
                if (perRat != null) {
                    codedOutputByteBufferNano.writeMessage(1, perRat);
                }
                i2++;
            }
        }
        PerRat[] perRatArr3 = this.perRatRx;
        if (perRatArr3 != null && perRatArr3.length > 0) {
            while (true) {
                PerRat[] perRatArr4 = this.perRatRx;
                if (i >= perRatArr4.length) {
                    break;
                }
                PerRat perRat2 = perRatArr4[i];
                if (perRat2 != null) {
                    codedOutputByteBufferNano.writeMessage(2, perRat2);
                }
                i++;
            }
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        PerRat[] perRatArr = this.perRatTx;
        int i = 0;
        if (perRatArr != null && perRatArr.length > 0) {
            int i2 = 0;
            while (true) {
                PerRat[] perRatArr2 = this.perRatTx;
                if (i2 >= perRatArr2.length) {
                    break;
                }
                PerRat perRat = perRatArr2[i2];
                if (perRat != null) {
                    iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(1, perRat);
                }
                i2++;
            }
        }
        PerRat[] perRatArr3 = this.perRatRx;
        if (perRatArr3 != null && perRatArr3.length > 0) {
            while (true) {
                PerRat[] perRatArr4 = this.perRatRx;
                if (i >= perRatArr4.length) {
                    break;
                }
                PerRat perRat2 = perRatArr4[i];
                if (perRat2 != null) {
                    iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(2, perRat2);
                }
                i++;
            }
        }
        return iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public TelephonyProto$BandwidthEstimatorStats mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            if (tag == 0) {
                break;
            }
            if (tag == 10) {
                int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 10);
                PerRat[] perRatArr = this.perRatTx;
                int length = perRatArr == null ? 0 : perRatArr.length;
                int i = repeatedFieldArrayLength + length;
                PerRat[] perRatArr2 = new PerRat[i];
                if (length != 0) {
                    System.arraycopy(perRatArr, 0, perRatArr2, 0, length);
                }
                while (length < i - 1) {
                    PerRat perRat = new PerRat();
                    perRatArr2[length] = perRat;
                    codedInputByteBufferNano.readMessage(perRat);
                    codedInputByteBufferNano.readTag();
                    length++;
                }
                PerRat perRat2 = new PerRat();
                perRatArr2[length] = perRat2;
                codedInputByteBufferNano.readMessage(perRat2);
                this.perRatTx = perRatArr2;
            } else if (tag != 18) {
                if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    break;
                }
            } else {
                int repeatedFieldArrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 18);
                PerRat[] perRatArr3 = this.perRatRx;
                int length2 = perRatArr3 == null ? 0 : perRatArr3.length;
                int i2 = repeatedFieldArrayLength2 + length2;
                PerRat[] perRatArr4 = new PerRat[i2];
                if (length2 != 0) {
                    System.arraycopy(perRatArr3, 0, perRatArr4, 0, length2);
                }
                while (length2 < i2 - 1) {
                    PerRat perRat3 = new PerRat();
                    perRatArr4[length2] = perRat3;
                    codedInputByteBufferNano.readMessage(perRat3);
                    codedInputByteBufferNano.readTag();
                    length2++;
                }
                PerRat perRat4 = new PerRat();
                perRatArr4[length2] = perRat4;
                codedInputByteBufferNano.readMessage(perRat4);
                this.perRatRx = perRatArr4;
            }
        }
        return this;
    }

    public static TelephonyProto$BandwidthEstimatorStats parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (TelephonyProto$BandwidthEstimatorStats) MessageNano.mergeFrom(new TelephonyProto$BandwidthEstimatorStats(), bArr);
    }

    public static TelephonyProto$BandwidthEstimatorStats parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new TelephonyProto$BandwidthEstimatorStats().mergeFrom(codedInputByteBufferNano);
    }
}
