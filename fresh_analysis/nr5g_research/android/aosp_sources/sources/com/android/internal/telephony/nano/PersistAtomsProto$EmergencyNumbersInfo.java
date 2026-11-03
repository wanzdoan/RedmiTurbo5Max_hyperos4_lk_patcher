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
public final class PersistAtomsProto$EmergencyNumbersInfo extends ExtendableMessageNano<PersistAtomsProto$EmergencyNumbersInfo> {
    private static volatile PersistAtomsProto$EmergencyNumbersInfo[] _emptyArray;
    public int assetVersion;
    public String countryIso;
    public boolean isDbVersionIgnored;
    public String mnc;
    public String number;
    public int otaVersion;
    public int route;
    public int[] serviceCategories;
    public int[] sources;
    public String[] urns;

    public interface CallRoute {
        public static final int EMERGENCY_CALL_ROUTE_EMERGENCY = 1;
        public static final int EMERGENCY_CALL_ROUTE_NORMAL = 2;
        public static final int EMERGENCY_CALL_ROUTE_UNKNOWN = 0;
    }

    public interface ServiceCategory {
        public static final int EMERGENCY_SERVICE_CATEGORY_AIEC = 7;
        public static final int EMERGENCY_SERVICE_CATEGORY_AMBULANCE = 2;
        public static final int EMERGENCY_SERVICE_CATEGORY_FIRE_BRIGADE = 3;
        public static final int EMERGENCY_SERVICE_CATEGORY_MARINE_GUARD = 4;
        public static final int EMERGENCY_SERVICE_CATEGORY_MIEC = 6;
        public static final int EMERGENCY_SERVICE_CATEGORY_MOUNTAIN_RESCUE = 5;
        public static final int EMERGENCY_SERVICE_CATEGORY_POLICE = 1;
        public static final int EMERGENCY_SERVICE_CATEGORY_UNSPECIFIED = 0;
    }

    public interface Source {
        public static final int EMERGENCY_NUMBER_SOURCE_DATABASE = 2;
        public static final int EMERGENCY_NUMBER_SOURCE_DEFAULT = 4;
        public static final int EMERGENCY_NUMBER_SOURCE_MODEM_CONFIG = 3;
        public static final int EMERGENCY_NUMBER_SOURCE_NETWORK_SIGNALING = 0;
        public static final int EMERGENCY_NUMBER_SOURCE_SIM = 1;
    }

    public static PersistAtomsProto$EmergencyNumbersInfo[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new PersistAtomsProto$EmergencyNumbersInfo[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public PersistAtomsProto$EmergencyNumbersInfo() {
        clear();
    }

    public PersistAtomsProto$EmergencyNumbersInfo clear() {
        this.isDbVersionIgnored = false;
        this.assetVersion = 0;
        this.otaVersion = 0;
        this.number = "";
        this.countryIso = "";
        this.mnc = "";
        this.route = 0;
        this.urns = WireFormatNano.EMPTY_STRING_ARRAY;
        int[] iArr = WireFormatNano.EMPTY_INT_ARRAY;
        this.serviceCategories = iArr;
        this.sources = iArr;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        boolean z = this.isDbVersionIgnored;
        if (z) {
            codedOutputByteBufferNano.writeBool(1, z);
        }
        int i = this.assetVersion;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(2, i);
        }
        int i2 = this.otaVersion;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(3, i2);
        }
        if (!this.number.equals("")) {
            codedOutputByteBufferNano.writeString(4, this.number);
        }
        if (!this.countryIso.equals("")) {
            codedOutputByteBufferNano.writeString(5, this.countryIso);
        }
        if (!this.mnc.equals("")) {
            codedOutputByteBufferNano.writeString(6, this.mnc);
        }
        int i3 = this.route;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(7, i3);
        }
        String[] strArr = this.urns;
        int i4 = 0;
        if (strArr != null && strArr.length > 0) {
            int i5 = 0;
            while (true) {
                String[] strArr2 = this.urns;
                if (i5 >= strArr2.length) {
                    break;
                }
                String str = strArr2[i5];
                if (str != null) {
                    codedOutputByteBufferNano.writeString(8, str);
                }
                i5++;
            }
        }
        int[] iArr = this.serviceCategories;
        if (iArr != null && iArr.length > 0) {
            int i6 = 0;
            while (true) {
                int[] iArr2 = this.serviceCategories;
                if (i6 >= iArr2.length) {
                    break;
                }
                codedOutputByteBufferNano.writeInt32(9, iArr2[i6]);
                i6++;
            }
        }
        int[] iArr3 = this.sources;
        if (iArr3 != null && iArr3.length > 0) {
            while (true) {
                int[] iArr4 = this.sources;
                if (i4 >= iArr4.length) {
                    break;
                }
                codedOutputByteBufferNano.writeInt32(10, iArr4[i4]);
                i4++;
            }
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int[] iArr;
        int iComputeSerializedSize = super.computeSerializedSize();
        boolean z = this.isDbVersionIgnored;
        if (z) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(1, z);
        }
        int i = this.assetVersion;
        if (i != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i);
        }
        int i2 = this.otaVersion;
        if (i2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, i2);
        }
        if (!this.number.equals("")) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(4, this.number);
        }
        if (!this.countryIso.equals("")) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(5, this.countryIso);
        }
        if (!this.mnc.equals("")) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(6, this.mnc);
        }
        int i3 = this.route;
        if (i3 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(7, i3);
        }
        String[] strArr = this.urns;
        int i4 = 0;
        if (strArr != null && strArr.length > 0) {
            int i5 = 0;
            int iComputeStringSizeNoTag = 0;
            int i6 = 0;
            while (true) {
                String[] strArr2 = this.urns;
                if (i5 >= strArr2.length) {
                    break;
                }
                String str = strArr2[i5];
                if (str != null) {
                    i6++;
                    iComputeStringSizeNoTag += CodedOutputByteBufferNano.computeStringSizeNoTag(str);
                }
                i5++;
            }
            iComputeSerializedSize = iComputeSerializedSize + iComputeStringSizeNoTag + i6;
        }
        int[] iArr2 = this.serviceCategories;
        if (iArr2 != null && iArr2.length > 0) {
            int i7 = 0;
            int iComputeInt32SizeNoTag = 0;
            while (true) {
                iArr = this.serviceCategories;
                if (i7 >= iArr.length) {
                    break;
                }
                iComputeInt32SizeNoTag += CodedOutputByteBufferNano.computeInt32SizeNoTag(iArr[i7]);
                i7++;
            }
            iComputeSerializedSize = iComputeSerializedSize + iComputeInt32SizeNoTag + iArr.length;
        }
        int[] iArr3 = this.sources;
        if (iArr3 == null || iArr3.length <= 0) {
            return iComputeSerializedSize;
        }
        int iComputeInt32SizeNoTag2 = 0;
        while (true) {
            int[] iArr4 = this.sources;
            if (i4 < iArr4.length) {
                iComputeInt32SizeNoTag2 += CodedOutputByteBufferNano.computeInt32SizeNoTag(iArr4[i4]);
                i4++;
            } else {
                return iComputeSerializedSize + iComputeInt32SizeNoTag2 + iArr4.length;
            }
        }
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public PersistAtomsProto$EmergencyNumbersInfo mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            switch (tag) {
                case 0:
                    break;
                case 8:
                    this.isDbVersionIgnored = codedInputByteBufferNano.readBool();
                    break;
                case 16:
                    this.assetVersion = codedInputByteBufferNano.readInt32();
                    break;
                case 24:
                    this.otaVersion = codedInputByteBufferNano.readInt32();
                    break;
                case 34:
                    this.number = codedInputByteBufferNano.readString();
                    break;
                case 42:
                    this.countryIso = codedInputByteBufferNano.readString();
                    break;
                case 50:
                    this.mnc = codedInputByteBufferNano.readString();
                    break;
                case 56:
                    int int32 = codedInputByteBufferNano.readInt32();
                    if (int32 == 0 || int32 == 1 || int32 == 2) {
                        this.route = int32;
                    }
                    break;
                case 66:
                    int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 66);
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
                    break;
                case 72:
                    int repeatedFieldArrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 72);
                    int[] iArr = new int[repeatedFieldArrayLength2];
                    int i2 = 0;
                    for (int i3 = 0; i3 < repeatedFieldArrayLength2; i3++) {
                        if (i3 != 0) {
                            codedInputByteBufferNano.readTag();
                        }
                        int int33 = codedInputByteBufferNano.readInt32();
                        switch (int33) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                                iArr[i2] = int33;
                                i2++;
                                break;
                        }
                    }
                    if (i2 != 0) {
                        int[] iArr2 = this.serviceCategories;
                        int length2 = iArr2 == null ? 0 : iArr2.length;
                        if (length2 == 0 && i2 == repeatedFieldArrayLength2) {
                            this.serviceCategories = iArr;
                        } else {
                            int[] iArr3 = new int[length2 + i2];
                            if (length2 != 0) {
                                System.arraycopy(iArr2, 0, iArr3, 0, length2);
                            }
                            System.arraycopy(iArr, 0, iArr3, length2, i2);
                            this.serviceCategories = iArr3;
                        }
                    }
                    break;
                case 74:
                    int iPushLimit = codedInputByteBufferNano.pushLimit(codedInputByteBufferNano.readRawVarint32());
                    int position = codedInputByteBufferNano.getPosition();
                    int i4 = 0;
                    while (codedInputByteBufferNano.getBytesUntilLimit() > 0) {
                        switch (codedInputByteBufferNano.readInt32()) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                                i4++;
                                break;
                        }
                    }
                    if (i4 != 0) {
                        codedInputByteBufferNano.rewindToPosition(position);
                        int[] iArr4 = this.serviceCategories;
                        int length3 = iArr4 == null ? 0 : iArr4.length;
                        int[] iArr5 = new int[i4 + length3];
                        if (length3 != 0) {
                            System.arraycopy(iArr4, 0, iArr5, 0, length3);
                        }
                        while (codedInputByteBufferNano.getBytesUntilLimit() > 0) {
                            int int34 = codedInputByteBufferNano.readInt32();
                            switch (int34) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                case 5:
                                case 6:
                                case 7:
                                    iArr5[length3] = int34;
                                    length3++;
                                    break;
                            }
                        }
                        this.serviceCategories = iArr5;
                    }
                    codedInputByteBufferNano.popLimit(iPushLimit);
                    break;
                case 80:
                    int repeatedFieldArrayLength3 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 80);
                    int[] iArr6 = new int[repeatedFieldArrayLength3];
                    int i5 = 0;
                    for (int i6 = 0; i6 < repeatedFieldArrayLength3; i6++) {
                        if (i6 != 0) {
                            codedInputByteBufferNano.readTag();
                        }
                        int int35 = codedInputByteBufferNano.readInt32();
                        if (int35 == 0 || int35 == 1 || int35 == 2 || int35 == 3 || int35 == 4) {
                            iArr6[i5] = int35;
                            i5++;
                        }
                    }
                    if (i5 != 0) {
                        int[] iArr7 = this.sources;
                        int length4 = iArr7 == null ? 0 : iArr7.length;
                        if (length4 == 0 && i5 == repeatedFieldArrayLength3) {
                            this.sources = iArr6;
                        } else {
                            int[] iArr8 = new int[length4 + i5];
                            if (length4 != 0) {
                                System.arraycopy(iArr7, 0, iArr8, 0, length4);
                            }
                            System.arraycopy(iArr6, 0, iArr8, length4, i5);
                            this.sources = iArr8;
                        }
                    }
                    break;
                case 82:
                    int iPushLimit2 = codedInputByteBufferNano.pushLimit(codedInputByteBufferNano.readRawVarint32());
                    int position2 = codedInputByteBufferNano.getPosition();
                    int i7 = 0;
                    while (codedInputByteBufferNano.getBytesUntilLimit() > 0) {
                        int int36 = codedInputByteBufferNano.readInt32();
                        if (int36 == 0 || int36 == 1 || int36 == 2 || int36 == 3 || int36 == 4) {
                            i7++;
                        }
                    }
                    if (i7 != 0) {
                        codedInputByteBufferNano.rewindToPosition(position2);
                        int[] iArr9 = this.sources;
                        int length5 = iArr9 == null ? 0 : iArr9.length;
                        int[] iArr10 = new int[i7 + length5];
                        if (length5 != 0) {
                            System.arraycopy(iArr9, 0, iArr10, 0, length5);
                        }
                        while (codedInputByteBufferNano.getBytesUntilLimit() > 0) {
                            int int37 = codedInputByteBufferNano.readInt32();
                            if (int37 == 0 || int37 == 1 || int37 == 2 || int37 == 3 || int37 == 4) {
                                iArr10[length5] = int37;
                                length5++;
                            }
                        }
                        this.sources = iArr10;
                    }
                    codedInputByteBufferNano.popLimit(iPushLimit2);
                    break;
                default:
                    if (storeUnknownField(codedInputByteBufferNano, tag)) {
                    }
                    break;
            }
        }
        return this;
    }

    public static PersistAtomsProto$EmergencyNumbersInfo parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (PersistAtomsProto$EmergencyNumbersInfo) MessageNano.mergeFrom(new PersistAtomsProto$EmergencyNumbersInfo(), bArr);
    }

    public static PersistAtomsProto$EmergencyNumbersInfo parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new PersistAtomsProto$EmergencyNumbersInfo().mergeFrom(codedInputByteBufferNano);
    }
}
