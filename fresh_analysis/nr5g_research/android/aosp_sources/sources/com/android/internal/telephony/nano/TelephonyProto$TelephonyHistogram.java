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
public final class TelephonyProto$TelephonyHistogram extends ExtendableMessageNano<TelephonyProto$TelephonyHistogram> {
    private static volatile TelephonyProto$TelephonyHistogram[] _emptyArray;
    public int avgTimeMillis;
    public int bucketCount;
    public int[] bucketCounters;
    public int[] bucketEndPoints;
    public int category;
    public int count;
    public int id;
    public int maxTimeMillis;
    public int minTimeMillis;

    public static TelephonyProto$TelephonyHistogram[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new TelephonyProto$TelephonyHistogram[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public TelephonyProto$TelephonyHistogram() {
        clear();
    }

    public TelephonyProto$TelephonyHistogram clear() {
        this.category = 0;
        this.id = 0;
        this.minTimeMillis = 0;
        this.maxTimeMillis = 0;
        this.avgTimeMillis = 0;
        this.count = 0;
        this.bucketCount = 0;
        int[] iArr = WireFormatNano.EMPTY_INT_ARRAY;
        this.bucketEndPoints = iArr;
        this.bucketCounters = iArr;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        int i = this.category;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(1, i);
        }
        int i2 = this.id;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(2, i2);
        }
        int i3 = this.minTimeMillis;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(3, i3);
        }
        int i4 = this.maxTimeMillis;
        if (i4 != 0) {
            codedOutputByteBufferNano.writeInt32(4, i4);
        }
        int i5 = this.avgTimeMillis;
        if (i5 != 0) {
            codedOutputByteBufferNano.writeInt32(5, i5);
        }
        int i6 = this.count;
        if (i6 != 0) {
            codedOutputByteBufferNano.writeInt32(6, i6);
        }
        int i7 = this.bucketCount;
        if (i7 != 0) {
            codedOutputByteBufferNano.writeInt32(7, i7);
        }
        int[] iArr = this.bucketEndPoints;
        int i8 = 0;
        if (iArr != null && iArr.length > 0) {
            int i9 = 0;
            while (true) {
                int[] iArr2 = this.bucketEndPoints;
                if (i9 >= iArr2.length) {
                    break;
                }
                codedOutputByteBufferNano.writeInt32(8, iArr2[i9]);
                i9++;
            }
        }
        int[] iArr3 = this.bucketCounters;
        if (iArr3 != null && iArr3.length > 0) {
            while (true) {
                int[] iArr4 = this.bucketCounters;
                if (i8 >= iArr4.length) {
                    break;
                }
                codedOutputByteBufferNano.writeInt32(9, iArr4[i8]);
                i8++;
            }
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int[] iArr;
        int iComputeSerializedSize = super.computeSerializedSize();
        int i = this.category;
        if (i != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
        }
        int i2 = this.id;
        if (i2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i2);
        }
        int i3 = this.minTimeMillis;
        if (i3 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, i3);
        }
        int i4 = this.maxTimeMillis;
        if (i4 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(4, i4);
        }
        int i5 = this.avgTimeMillis;
        if (i5 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(5, i5);
        }
        int i6 = this.count;
        if (i6 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(6, i6);
        }
        int i7 = this.bucketCount;
        if (i7 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(7, i7);
        }
        int[] iArr2 = this.bucketEndPoints;
        int i8 = 0;
        if (iArr2 != null && iArr2.length > 0) {
            int i9 = 0;
            int iComputeInt32SizeNoTag = 0;
            while (true) {
                iArr = this.bucketEndPoints;
                if (i9 >= iArr.length) {
                    break;
                }
                iComputeInt32SizeNoTag += CodedOutputByteBufferNano.computeInt32SizeNoTag(iArr[i9]);
                i9++;
            }
            iComputeSerializedSize = iComputeSerializedSize + iComputeInt32SizeNoTag + iArr.length;
        }
        int[] iArr3 = this.bucketCounters;
        if (iArr3 == null || iArr3.length <= 0) {
            return iComputeSerializedSize;
        }
        int iComputeInt32SizeNoTag2 = 0;
        while (true) {
            int[] iArr4 = this.bucketCounters;
            if (i8 < iArr4.length) {
                iComputeInt32SizeNoTag2 += CodedOutputByteBufferNano.computeInt32SizeNoTag(iArr4[i8]);
                i8++;
            } else {
                return iComputeSerializedSize + iComputeInt32SizeNoTag2 + iArr4.length;
            }
        }
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public TelephonyProto$TelephonyHistogram mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            switch (tag) {
                case 0:
                    break;
                case 8:
                    this.category = codedInputByteBufferNano.readInt32();
                    break;
                case 16:
                    this.id = codedInputByteBufferNano.readInt32();
                    break;
                case 24:
                    this.minTimeMillis = codedInputByteBufferNano.readInt32();
                    break;
                case 32:
                    this.maxTimeMillis = codedInputByteBufferNano.readInt32();
                    break;
                case 40:
                    this.avgTimeMillis = codedInputByteBufferNano.readInt32();
                    break;
                case 48:
                    this.count = codedInputByteBufferNano.readInt32();
                    break;
                case 56:
                    this.bucketCount = codedInputByteBufferNano.readInt32();
                    break;
                case 64:
                    int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 64);
                    int[] iArr = this.bucketEndPoints;
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
                    this.bucketEndPoints = iArr2;
                    break;
                case 66:
                    int iPushLimit = codedInputByteBufferNano.pushLimit(codedInputByteBufferNano.readRawVarint32());
                    int position = codedInputByteBufferNano.getPosition();
                    int i2 = 0;
                    while (codedInputByteBufferNano.getBytesUntilLimit() > 0) {
                        codedInputByteBufferNano.readInt32();
                        i2++;
                    }
                    codedInputByteBufferNano.rewindToPosition(position);
                    int[] iArr3 = this.bucketEndPoints;
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
                    this.bucketEndPoints = iArr4;
                    codedInputByteBufferNano.popLimit(iPushLimit);
                    break;
                case 72:
                    int repeatedFieldArrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 72);
                    int[] iArr5 = this.bucketCounters;
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
                    this.bucketCounters = iArr6;
                    break;
                case 74:
                    int iPushLimit2 = codedInputByteBufferNano.pushLimit(codedInputByteBufferNano.readRawVarint32());
                    int position2 = codedInputByteBufferNano.getPosition();
                    int i5 = 0;
                    while (codedInputByteBufferNano.getBytesUntilLimit() > 0) {
                        codedInputByteBufferNano.readInt32();
                        i5++;
                    }
                    codedInputByteBufferNano.rewindToPosition(position2);
                    int[] iArr7 = this.bucketCounters;
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
                    this.bucketCounters = iArr8;
                    codedInputByteBufferNano.popLimit(iPushLimit2);
                    break;
                default:
                    if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    }
                    break;
            }
        }
        return this;
    }

    public static TelephonyProto$TelephonyHistogram parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (TelephonyProto$TelephonyHistogram) MessageNano.mergeFrom(new TelephonyProto$TelephonyHistogram(), bArr);
    }

    public static TelephonyProto$TelephonyHistogram parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new TelephonyProto$TelephonyHistogram().mergeFrom(codedInputByteBufferNano);
    }
}
