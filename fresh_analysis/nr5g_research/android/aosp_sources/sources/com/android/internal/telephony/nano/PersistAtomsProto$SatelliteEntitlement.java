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
public final class PersistAtomsProto$SatelliteEntitlement extends ExtendableMessageNano<PersistAtomsProto$SatelliteEntitlement> {
    private static volatile PersistAtomsProto$SatelliteEntitlement[] _emptyArray;
    public int carrierId;
    public int count;
    public int entitlementDataPolicy;
    public int[] entitlementServiceType;
    public int entitlementStatus;
    public int httpStatusCode;
    public boolean isAllowedServiceEntitlement;
    public boolean isRetry;
    public int result;
    public int supportedConnectionMode;
    public int triggerEvent;

    public static PersistAtomsProto$SatelliteEntitlement[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new PersistAtomsProto$SatelliteEntitlement[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public PersistAtomsProto$SatelliteEntitlement() {
        clear();
    }

    public PersistAtomsProto$SatelliteEntitlement clear() {
        this.carrierId = 0;
        this.result = 0;
        this.entitlementStatus = 0;
        this.isRetry = false;
        this.count = 0;
        this.isAllowedServiceEntitlement = false;
        this.entitlementServiceType = WireFormatNano.EMPTY_INT_ARRAY;
        this.entitlementDataPolicy = 0;
        this.supportedConnectionMode = 0;
        this.httpStatusCode = 0;
        this.triggerEvent = 0;
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
        int i2 = this.result;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(2, i2);
        }
        int i3 = this.entitlementStatus;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(3, i3);
        }
        boolean z = this.isRetry;
        if (z) {
            codedOutputByteBufferNano.writeBool(4, z);
        }
        int i4 = this.count;
        if (i4 != 0) {
            codedOutputByteBufferNano.writeInt32(5, i4);
        }
        boolean z2 = this.isAllowedServiceEntitlement;
        if (z2) {
            codedOutputByteBufferNano.writeBool(6, z2);
        }
        int[] iArr = this.entitlementServiceType;
        if (iArr != null && iArr.length > 0) {
            int i5 = 0;
            while (true) {
                int[] iArr2 = this.entitlementServiceType;
                if (i5 >= iArr2.length) {
                    break;
                }
                codedOutputByteBufferNano.writeInt32(7, iArr2[i5]);
                i5++;
            }
        }
        int i6 = this.entitlementDataPolicy;
        if (i6 != 0) {
            codedOutputByteBufferNano.writeInt32(8, i6);
        }
        int i7 = this.supportedConnectionMode;
        if (i7 != 0) {
            codedOutputByteBufferNano.writeInt32(9, i7);
        }
        int i8 = this.httpStatusCode;
        if (i8 != 0) {
            codedOutputByteBufferNano.writeInt32(10, i8);
        }
        int i9 = this.triggerEvent;
        if (i9 != 0) {
            codedOutputByteBufferNano.writeInt32(11, i9);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int[] iArr;
        int iComputeSerializedSize = super.computeSerializedSize();
        int i = this.carrierId;
        if (i != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
        }
        int i2 = this.result;
        if (i2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i2);
        }
        int i3 = this.entitlementStatus;
        if (i3 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, i3);
        }
        boolean z = this.isRetry;
        if (z) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(4, z);
        }
        int i4 = this.count;
        if (i4 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(5, i4);
        }
        boolean z2 = this.isAllowedServiceEntitlement;
        if (z2) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(6, z2);
        }
        int[] iArr2 = this.entitlementServiceType;
        if (iArr2 != null && iArr2.length > 0) {
            int i5 = 0;
            int iComputeInt32SizeNoTag = 0;
            while (true) {
                iArr = this.entitlementServiceType;
                if (i5 >= iArr.length) {
                    break;
                }
                iComputeInt32SizeNoTag += CodedOutputByteBufferNano.computeInt32SizeNoTag(iArr[i5]);
                i5++;
            }
            iComputeSerializedSize = iComputeSerializedSize + iComputeInt32SizeNoTag + iArr.length;
        }
        int i6 = this.entitlementDataPolicy;
        if (i6 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(8, i6);
        }
        int i7 = this.supportedConnectionMode;
        if (i7 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(9, i7);
        }
        int i8 = this.httpStatusCode;
        if (i8 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(10, i8);
        }
        int i9 = this.triggerEvent;
        return i9 != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(11, i9) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public PersistAtomsProto$SatelliteEntitlement mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            switch (tag) {
                case 0:
                    break;
                case 8:
                    this.carrierId = codedInputByteBufferNano.readInt32();
                    break;
                case 16:
                    this.result = codedInputByteBufferNano.readInt32();
                    break;
                case 24:
                    this.entitlementStatus = codedInputByteBufferNano.readInt32();
                    break;
                case 32:
                    this.isRetry = codedInputByteBufferNano.readBool();
                    break;
                case 40:
                    this.count = codedInputByteBufferNano.readInt32();
                    break;
                case 48:
                    this.isAllowedServiceEntitlement = codedInputByteBufferNano.readBool();
                    break;
                case 56:
                    int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 56);
                    int[] iArr = this.entitlementServiceType;
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
                    this.entitlementServiceType = iArr2;
                    break;
                case 58:
                    int iPushLimit = codedInputByteBufferNano.pushLimit(codedInputByteBufferNano.readRawVarint32());
                    int position = codedInputByteBufferNano.getPosition();
                    int i2 = 0;
                    while (codedInputByteBufferNano.getBytesUntilLimit() > 0) {
                        codedInputByteBufferNano.readInt32();
                        i2++;
                    }
                    codedInputByteBufferNano.rewindToPosition(position);
                    int[] iArr3 = this.entitlementServiceType;
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
                    this.entitlementServiceType = iArr4;
                    codedInputByteBufferNano.popLimit(iPushLimit);
                    break;
                case 64:
                    this.entitlementDataPolicy = codedInputByteBufferNano.readInt32();
                    break;
                case 72:
                    this.supportedConnectionMode = codedInputByteBufferNano.readInt32();
                    break;
                case 80:
                    this.httpStatusCode = codedInputByteBufferNano.readInt32();
                    break;
                case 88:
                    this.triggerEvent = codedInputByteBufferNano.readInt32();
                    break;
                default:
                    if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    }
                    break;
            }
        }
        return this;
    }

    public static PersistAtomsProto$SatelliteEntitlement parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (PersistAtomsProto$SatelliteEntitlement) MessageNano.mergeFrom(new PersistAtomsProto$SatelliteEntitlement(), bArr);
    }

    public static PersistAtomsProto$SatelliteEntitlement parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new PersistAtomsProto$SatelliteEntitlement().mergeFrom(codedInputByteBufferNano);
    }
}
