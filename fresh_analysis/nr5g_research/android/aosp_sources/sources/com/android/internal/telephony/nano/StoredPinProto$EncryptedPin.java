package com.android.internal.telephony.nano;

import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import com.android.internal.telephony.protobuf.nano.WireFormatNano;
import java.io.IOException;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public final class StoredPinProto$EncryptedPin extends ExtendableMessageNano<StoredPinProto$EncryptedPin> {
    private static volatile StoredPinProto$EncryptedPin[] _emptyArray;
    public byte[] encryptedStoredPin;
    public byte[] iv;

    public static StoredPinProto$EncryptedPin[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new StoredPinProto$EncryptedPin[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public StoredPinProto$EncryptedPin() {
        clear();
    }

    public StoredPinProto$EncryptedPin clear() {
        byte[] bArr = WireFormatNano.EMPTY_BYTES;
        this.encryptedStoredPin = bArr;
        this.iv = bArr;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        byte[] bArr = this.encryptedStoredPin;
        byte[] bArr2 = WireFormatNano.EMPTY_BYTES;
        if (!Arrays.equals(bArr, bArr2)) {
            codedOutputByteBufferNano.writeBytes(1, this.encryptedStoredPin);
        }
        if (!Arrays.equals(this.iv, bArr2)) {
            codedOutputByteBufferNano.writeBytes(2, this.iv);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        byte[] bArr = this.encryptedStoredPin;
        byte[] bArr2 = WireFormatNano.EMPTY_BYTES;
        if (!Arrays.equals(bArr, bArr2)) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBytesSize(1, this.encryptedStoredPin);
        }
        return !Arrays.equals(this.iv, bArr2) ? iComputeSerializedSize + CodedOutputByteBufferNano.computeBytesSize(2, this.iv) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public StoredPinProto$EncryptedPin mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            if (tag == 0) {
                break;
            }
            if (tag == 10) {
                this.encryptedStoredPin = codedInputByteBufferNano.readBytes();
            } else if (tag != 18) {
                if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    break;
                }
            } else {
                this.iv = codedInputByteBufferNano.readBytes();
            }
        }
        return this;
    }

    public static StoredPinProto$EncryptedPin parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (StoredPinProto$EncryptedPin) MessageNano.mergeFrom(new StoredPinProto$EncryptedPin(), bArr);
    }

    public static StoredPinProto$EncryptedPin parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new StoredPinProto$EncryptedPin().mergeFrom(codedInputByteBufferNano);
    }
}
