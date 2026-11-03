package com.android.phone.ecc.nano;

/* JADX INFO: loaded from: classes.dex */
public final class ProtobufEccData$AllInfo extends ExtendableMessageNano {
    public ProtobufEccData$CountryInfo[] countries;
    public int revision;

    public ProtobufEccData$AllInfo() {
        clear();
    }

    public ProtobufEccData$AllInfo clear() {
        this.revision = 0;
        this.countries = ProtobufEccData$CountryInfo.emptyArray();
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.phone.ecc.nano.MessageNano
    public ProtobufEccData$AllInfo mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws InvalidProtocolBufferNanoException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            if (tag == 0) {
                break;
            }
            if (tag == 8) {
                this.revision = codedInputByteBufferNano.readInt32();
            } else if (tag != 18) {
                if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    break;
                }
            } else {
                int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 18);
                ProtobufEccData$CountryInfo[] protobufEccData$CountryInfoArr = this.countries;
                int length = protobufEccData$CountryInfoArr == null ? 0 : protobufEccData$CountryInfoArr.length;
                int i = repeatedFieldArrayLength + length;
                ProtobufEccData$CountryInfo[] protobufEccData$CountryInfoArr2 = new ProtobufEccData$CountryInfo[i];
                if (length != 0) {
                    System.arraycopy(protobufEccData$CountryInfoArr, 0, protobufEccData$CountryInfoArr2, 0, length);
                }
                while (length < i - 1) {
                    ProtobufEccData$CountryInfo protobufEccData$CountryInfo = new ProtobufEccData$CountryInfo();
                    protobufEccData$CountryInfoArr2[length] = protobufEccData$CountryInfo;
                    codedInputByteBufferNano.readMessage(protobufEccData$CountryInfo);
                    codedInputByteBufferNano.readTag();
                    length++;
                }
                ProtobufEccData$CountryInfo protobufEccData$CountryInfo2 = new ProtobufEccData$CountryInfo();
                protobufEccData$CountryInfoArr2[length] = protobufEccData$CountryInfo2;
                codedInputByteBufferNano.readMessage(protobufEccData$CountryInfo2);
                this.countries = protobufEccData$CountryInfoArr2;
            }
        }
        return this;
    }

    public static ProtobufEccData$AllInfo parseFrom(byte[] bArr) {
        return (ProtobufEccData$AllInfo) MessageNano.mergeFrom(new ProtobufEccData$AllInfo(), bArr);
    }
}
