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
public final class StoredPinProto$EncryptedPlatformManagedPins extends ExtendableMessageNano<StoredPinProto$EncryptedPlatformManagedPins> {
    private static volatile StoredPinProto$EncryptedPlatformManagedPins[] _emptyArray;
    public byte[] encryptedPlatformPins;
    public byte[] iv;

    public static StoredPinProto$EncryptedPlatformManagedPins[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new StoredPinProto$EncryptedPlatformManagedPins[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public StoredPinProto$EncryptedPlatformManagedPins() {
        clear();
    }

    public StoredPinProto$EncryptedPlatformManagedPins clear() {
        byte[] bArr = WireFormatNano.EMPTY_BYTES;
        this.encryptedPlatformPins = bArr;
        this.iv = bArr;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        byte[] bArr = this.encryptedPlatformPins;
        byte[] bArr2 = WireFormatNano.EMPTY_BYTES;
        if (!Arrays.equals(bArr, bArr2)) {
            codedOutputByteBufferNano.writeBytes(1, this.encryptedPlatformPins);
        }
        if (!Arrays.equals(this.iv, bArr2)) {
            codedOutputByteBufferNano.writeBytes(2, this.iv);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        byte[] bArr = this.encryptedPlatformPins;
        byte[] bArr2 = WireFormatNano.EMPTY_BYTES;
        if (!Arrays.equals(bArr, bArr2)) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBytesSize(1, this.encryptedPlatformPins);
        }
        return !Arrays.equals(this.iv, bArr2) ? iComputeSerializedSize + CodedOutputByteBufferNano.computeBytesSize(2, this.iv) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public StoredPinProto$EncryptedPlatformManagedPins mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            if (tag == 0) {
                break;
            }
            if (tag == 10) {
                this.encryptedPlatformPins = codedInputByteBufferNano.readBytes();
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

    public static StoredPinProto$EncryptedPlatformManagedPins parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (StoredPinProto$EncryptedPlatformManagedPins) MessageNano.mergeFrom(new StoredPinProto$EncryptedPlatformManagedPins(), bArr);
    }

    public static StoredPinProto$EncryptedPlatformManagedPins parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new StoredPinProto$EncryptedPlatformManagedPins().mergeFrom(codedInputByteBufferNano);
    }
}
