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
public final class PersistAtomsProto$SatelliteAccessController extends ExtendableMessageNano<PersistAtomsProto$SatelliteAccessController> {
    private static volatile PersistAtomsProto$SatelliteAccessController[] _emptyArray;
    public int accessControlType;
    public int carrierId;
    public int configDataSource;
    public String[] countryCodes;
    public boolean isAllowed;
    public boolean isEmergency;
    public boolean isNtnOnlyCarrier;
    public long locationQueryTimeMillis;
    public long onDeviceLookupTimeMillis;
    public int resultCode;
    public int sessionConnectionMode;
    public int supportedConnectionMode;
    public long totalCheckingTimeMillis;
    public int triggeringEvent;

    public static PersistAtomsProto$SatelliteAccessController[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new PersistAtomsProto$SatelliteAccessController[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public PersistAtomsProto$SatelliteAccessController() {
        clear();
    }

    public PersistAtomsProto$SatelliteAccessController clear() {
        this.accessControlType = 0;
        this.locationQueryTimeMillis = 0L;
        this.onDeviceLookupTimeMillis = 0L;
        this.totalCheckingTimeMillis = 0L;
        this.isAllowed = false;
        this.isEmergency = false;
        this.resultCode = 0;
        this.countryCodes = WireFormatNano.EMPTY_STRING_ARRAY;
        this.configDataSource = 0;
        this.carrierId = 0;
        this.triggeringEvent = 0;
        this.isNtnOnlyCarrier = false;
        this.supportedConnectionMode = 0;
        this.sessionConnectionMode = 0;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        int i = this.accessControlType;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(1, i);
        }
        long j = this.locationQueryTimeMillis;
        if (j != 0) {
            codedOutputByteBufferNano.writeInt64(2, j);
        }
        long j2 = this.onDeviceLookupTimeMillis;
        if (j2 != 0) {
            codedOutputByteBufferNano.writeInt64(3, j2);
        }
        long j3 = this.totalCheckingTimeMillis;
        if (j3 != 0) {
            codedOutputByteBufferNano.writeInt64(4, j3);
        }
        boolean z = this.isAllowed;
        if (z) {
            codedOutputByteBufferNano.writeBool(5, z);
        }
        boolean z2 = this.isEmergency;
        if (z2) {
            codedOutputByteBufferNano.writeBool(6, z2);
        }
        int i2 = this.resultCode;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(7, i2);
        }
        String[] strArr = this.countryCodes;
        if (strArr != null && strArr.length > 0) {
            int i3 = 0;
            while (true) {
                String[] strArr2 = this.countryCodes;
                if (i3 >= strArr2.length) {
                    break;
                }
                String str = strArr2[i3];
                if (str != null) {
                    codedOutputByteBufferNano.writeString(8, str);
                }
                i3++;
            }
        }
        int i4 = this.configDataSource;
        if (i4 != 0) {
            codedOutputByteBufferNano.writeInt32(9, i4);
        }
        int i5 = this.carrierId;
        if (i5 != 0) {
            codedOutputByteBufferNano.writeInt32(10, i5);
        }
        int i6 = this.triggeringEvent;
        if (i6 != 0) {
            codedOutputByteBufferNano.writeInt32(11, i6);
        }
        boolean z3 = this.isNtnOnlyCarrier;
        if (z3) {
            codedOutputByteBufferNano.writeBool(12, z3);
        }
        int i7 = this.supportedConnectionMode;
        if (i7 != 0) {
            codedOutputByteBufferNano.writeInt32(13, i7);
        }
        int i8 = this.sessionConnectionMode;
        if (i8 != 0) {
            codedOutputByteBufferNano.writeInt32(14, i8);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        int i = this.accessControlType;
        if (i != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
        }
        long j = this.locationQueryTimeMillis;
        if (j != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(2, j);
        }
        long j2 = this.onDeviceLookupTimeMillis;
        if (j2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(3, j2);
        }
        long j3 = this.totalCheckingTimeMillis;
        if (j3 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(4, j3);
        }
        boolean z = this.isAllowed;
        if (z) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(5, z);
        }
        boolean z2 = this.isEmergency;
        if (z2) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(6, z2);
        }
        int i2 = this.resultCode;
        if (i2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(7, i2);
        }
        String[] strArr = this.countryCodes;
        if (strArr != null && strArr.length > 0) {
            int i3 = 0;
            int iComputeStringSizeNoTag = 0;
            int i4 = 0;
            while (true) {
                String[] strArr2 = this.countryCodes;
                if (i3 >= strArr2.length) {
                    break;
                }
                String str = strArr2[i3];
                if (str != null) {
                    i4++;
                    iComputeStringSizeNoTag += CodedOutputByteBufferNano.computeStringSizeNoTag(str);
                }
                i3++;
            }
            iComputeSerializedSize = iComputeSerializedSize + iComputeStringSizeNoTag + i4;
        }
        int i5 = this.configDataSource;
        if (i5 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(9, i5);
        }
        int i6 = this.carrierId;
        if (i6 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(10, i6);
        }
        int i7 = this.triggeringEvent;
        if (i7 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(11, i7);
        }
        boolean z3 = this.isNtnOnlyCarrier;
        if (z3) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(12, z3);
        }
        int i8 = this.supportedConnectionMode;
        if (i8 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(13, i8);
        }
        int i9 = this.sessionConnectionMode;
        return i9 != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(14, i9) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public PersistAtomsProto$SatelliteAccessController mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            switch (tag) {
                case 0:
                    break;
                case 8:
                    this.accessControlType = codedInputByteBufferNano.readInt32();
                    break;
                case 16:
                    this.locationQueryTimeMillis = codedInputByteBufferNano.readInt64();
                    break;
                case 24:
                    this.onDeviceLookupTimeMillis = codedInputByteBufferNano.readInt64();
                    break;
                case 32:
                    this.totalCheckingTimeMillis = codedInputByteBufferNano.readInt64();
                    break;
                case 40:
                    this.isAllowed = codedInputByteBufferNano.readBool();
                    break;
                case 48:
                    this.isEmergency = codedInputByteBufferNano.readBool();
                    break;
                case 56:
                    this.resultCode = codedInputByteBufferNano.readInt32();
                    break;
                case 66:
                    int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 66);
                    String[] strArr = this.countryCodes;
                    int length = strArr == null ? 0 : strArr.length;
                    int i = repeatedFieldArrayLength + length;
                    String[] strArr2 = new String[i];
                    if (length != 0) {
                        System.arraycopy(strArr, 0, strArr2, 0, length);
                    }
                    while (length < i - 1) {
                        strArr2[length] = codedInputByteBufferNano.readString();
                        codedInputByteBufferNano.readTag();
                        length++;
                    }
                    strArr2[length] = codedInputByteBufferNano.readString();
                    this.countryCodes = strArr2;
                    break;
                case 72:
                    this.configDataSource = codedInputByteBufferNano.readInt32();
                    break;
                case 80:
                    this.carrierId = codedInputByteBufferNano.readInt32();
                    break;
                case 88:
                    this.triggeringEvent = codedInputByteBufferNano.readInt32();
                    break;
                case 96:
                    this.isNtnOnlyCarrier = codedInputByteBufferNano.readBool();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE /* 104 */:
                    this.supportedConnectionMode = codedInputByteBufferNano.readInt32();
                    break;
                case 112:
                    this.sessionConnectionMode = codedInputByteBufferNano.readInt32();
                    break;
                default:
                    if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    }
                    break;
            }
        }
        return this;
    }

    public static PersistAtomsProto$SatelliteAccessController parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (PersistAtomsProto$SatelliteAccessController) MessageNano.mergeFrom(new PersistAtomsProto$SatelliteAccessController(), bArr);
    }

    public static PersistAtomsProto$SatelliteAccessController parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new PersistAtomsProto$SatelliteAccessController().mergeFrom(codedInputByteBufferNano);
    }
}
