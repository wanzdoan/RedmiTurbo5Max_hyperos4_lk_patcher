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
public final class TelephonyProto$EmergencyNumberInfo extends ExtendableMessageNano<TelephonyProto$EmergencyNumberInfo> {
    private static volatile TelephonyProto$EmergencyNumberInfo[] _emptyArray;
    public String address;
    public String countryIso;
    public String mnc;
    public int numberSourcesBitmask;
    public int routing;
    public int serviceCategoriesBitmask;
    public String[] urns;

    public static TelephonyProto$EmergencyNumberInfo[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new TelephonyProto$EmergencyNumberInfo[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public TelephonyProto$EmergencyNumberInfo() {
        clear();
    }

    public TelephonyProto$EmergencyNumberInfo clear() {
        this.address = "";
        this.countryIso = "";
        this.mnc = "";
        this.serviceCategoriesBitmask = 0;
        this.urns = WireFormatNano.EMPTY_STRING_ARRAY;
        this.numberSourcesBitmask = 0;
        this.routing = 0;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        if (!this.address.equals("")) {
            codedOutputByteBufferNano.writeString(1, this.address);
        }
        if (!this.countryIso.equals("")) {
            codedOutputByteBufferNano.writeString(2, this.countryIso);
        }
        if (!this.mnc.equals("")) {
            codedOutputByteBufferNano.writeString(3, this.mnc);
        }
        int i = this.serviceCategoriesBitmask;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(4, i);
        }
        String[] strArr = this.urns;
        if (strArr != null && strArr.length > 0) {
            int i2 = 0;
            while (true) {
                String[] strArr2 = this.urns;
                if (i2 >= strArr2.length) {
                    break;
                }
                String str = strArr2[i2];
                if (str != null) {
                    codedOutputByteBufferNano.writeString(5, str);
                }
                i2++;
            }
        }
        int i3 = this.numberSourcesBitmask;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(6, i3);
        }
        int i4 = this.routing;
        if (i4 != 0) {
            codedOutputByteBufferNano.writeInt32(7, i4);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        if (!this.address.equals("")) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.address);
        }
        if (!this.countryIso.equals("")) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(2, this.countryIso);
        }
        if (!this.mnc.equals("")) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(3, this.mnc);
        }
        int i = this.serviceCategoriesBitmask;
        if (i != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(4, i);
        }
        String[] strArr = this.urns;
        if (strArr != null && strArr.length > 0) {
            int i2 = 0;
            int iComputeStringSizeNoTag = 0;
            int i3 = 0;
            while (true) {
                String[] strArr2 = this.urns;
                if (i2 >= strArr2.length) {
                    break;
                }
                String str = strArr2[i2];
                if (str != null) {
                    i3++;
                    iComputeStringSizeNoTag += CodedOutputByteBufferNano.computeStringSizeNoTag(str);
                }
                i2++;
            }
            iComputeSerializedSize = iComputeSerializedSize + iComputeStringSizeNoTag + i3;
        }
        int i4 = this.numberSourcesBitmask;
        if (i4 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(6, i4);
        }
        int i5 = this.routing;
        return i5 != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(7, i5) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public TelephonyProto$EmergencyNumberInfo mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            if (tag == 0) {
                break;
            }
            if (tag == 10) {
                this.address = codedInputByteBufferNano.readString();
            } else if (tag == 18) {
                this.countryIso = codedInputByteBufferNano.readString();
            } else if (tag == 26) {
                this.mnc = codedInputByteBufferNano.readString();
            } else if (tag == 32) {
                this.serviceCategoriesBitmask = codedInputByteBufferNano.readInt32();
            } else if (tag == 42) {
                int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 42);
                String[] strArr = this.urns;
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
                this.urns = strArr2;
            } else if (tag == 48) {
                this.numberSourcesBitmask = codedInputByteBufferNano.readInt32();
            } else if (tag != 56) {
                if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    break;
                }
            } else {
                this.routing = codedInputByteBufferNano.readInt32();
            }
        }
        return this;
    }

    public static TelephonyProto$EmergencyNumberInfo parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (TelephonyProto$EmergencyNumberInfo) MessageNano.mergeFrom(new TelephonyProto$EmergencyNumberInfo(), bArr);
    }

    public static TelephonyProto$EmergencyNumberInfo parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new TelephonyProto$EmergencyNumberInfo().mergeFrom(codedInputByteBufferNano);
    }
}
