package com.android.phone.ecc.nano;

/* JADX INFO: loaded from: classes.dex */
public final class ProtobufEccData$EccInfo extends ExtendableMessageNano {
    private static volatile ProtobufEccData$EccInfo[] _emptyArray;
    public String[] normalRoutingMncs;
    public String phoneNumber;
    public int routing;
    public int[] types;

    public static ProtobufEccData$EccInfo[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new ProtobufEccData$EccInfo[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public ProtobufEccData$EccInfo() {
        clear();
    }

    public ProtobufEccData$EccInfo clear() {
        this.phoneNumber = "";
        this.types = WireFormatNano.EMPTY_INT_ARRAY;
        this.routing = 0;
        this.normalRoutingMncs = WireFormatNano.EMPTY_STRING_ARRAY;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.phone.ecc.nano.MessageNano
    public ProtobufEccData$EccInfo mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws InvalidProtocolBufferNanoException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            if (tag != 0) {
                if (tag == 10) {
                    this.phoneNumber = codedInputByteBufferNano.readString();
                } else if (tag == 16) {
                    int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 16);
                    int[] iArr = new int[repeatedFieldArrayLength];
                    int i = 0;
                    for (int i2 = 0; i2 < repeatedFieldArrayLength; i2++) {
                        if (i2 != 0) {
                            codedInputByteBufferNano.readTag();
                        }
                        int int32 = codedInputByteBufferNano.readInt32();
                        switch (int32) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                                iArr[i] = int32;
                                i++;
                                break;
                        }
                    }
                    if (i != 0) {
                        int[] iArr2 = this.types;
                        int length = iArr2 == null ? 0 : iArr2.length;
                        if (length == 0 && i == repeatedFieldArrayLength) {
                            this.types = iArr;
                        } else {
                            int[] iArr3 = new int[length + i];
                            if (length != 0) {
                                System.arraycopy(iArr2, 0, iArr3, 0, length);
                            }
                            System.arraycopy(iArr, 0, iArr3, length, i);
                            this.types = iArr3;
                        }
                    }
                } else if (tag == 18) {
                    int iPushLimit = codedInputByteBufferNano.pushLimit(codedInputByteBufferNano.readRawVarint32());
                    int position = codedInputByteBufferNano.getPosition();
                    int i3 = 0;
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
                                i3++;
                                break;
                        }
                    }
                    if (i3 != 0) {
                        codedInputByteBufferNano.rewindToPosition(position);
                        int[] iArr4 = this.types;
                        int length2 = iArr4 == null ? 0 : iArr4.length;
                        int[] iArr5 = new int[i3 + length2];
                        if (length2 != 0) {
                            System.arraycopy(iArr4, 0, iArr5, 0, length2);
                        }
                        while (codedInputByteBufferNano.getBytesUntilLimit() > 0) {
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
                                    iArr5[length2] = int33;
                                    length2++;
                                    break;
                            }
                        }
                        this.types = iArr5;
                    }
                    codedInputByteBufferNano.popLimit(iPushLimit);
                } else if (tag == 24) {
                    int int34 = codedInputByteBufferNano.readInt32();
                    if (int34 == 0 || int34 == 1 || int34 == 2) {
                        this.routing = int34;
                    }
                } else if (tag != 34) {
                    if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    }
                } else {
                    int repeatedFieldArrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 34);
                    String[] strArr = this.normalRoutingMncs;
                    int length3 = strArr == null ? 0 : strArr.length;
                    int i4 = repeatedFieldArrayLength2 + length3;
                    String[] strArr2 = new String[i4];
                    if (length3 != 0) {
                        System.arraycopy(strArr, 0, strArr2, 0, length3);
                    }
                    while (length3 < i4 - 1) {
                        strArr2[length3] = codedInputByteBufferNano.readString();
                        codedInputByteBufferNano.readTag();
                        length3++;
                    }
                    strArr2[length3] = codedInputByteBufferNano.readString();
                    this.normalRoutingMncs = strArr2;
                }
            }
        }
        return this;
    }
}
