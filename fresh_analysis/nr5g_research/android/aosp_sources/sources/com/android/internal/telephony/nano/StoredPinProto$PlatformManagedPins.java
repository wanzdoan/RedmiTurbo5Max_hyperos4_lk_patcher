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
public final class StoredPinProto$PlatformManagedPins extends ExtendableMessageNano<StoredPinProto$PlatformManagedPins> {
    private static volatile StoredPinProto$PlatformManagedPins[] _emptyArray;
    public StoredPinProto$PlatformManagedPin[] pins;

    public static StoredPinProto$PlatformManagedPins[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new StoredPinProto$PlatformManagedPins[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public StoredPinProto$PlatformManagedPins() {
        clear();
    }

    public StoredPinProto$PlatformManagedPins clear() {
        this.pins = StoredPinProto$PlatformManagedPin.emptyArray();
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        StoredPinProto$PlatformManagedPin[] storedPinProto$PlatformManagedPinArr = this.pins;
        if (storedPinProto$PlatformManagedPinArr != null && storedPinProto$PlatformManagedPinArr.length > 0) {
            int i = 0;
            while (true) {
                StoredPinProto$PlatformManagedPin[] storedPinProto$PlatformManagedPinArr2 = this.pins;
                if (i >= storedPinProto$PlatformManagedPinArr2.length) {
                    break;
                }
                StoredPinProto$PlatformManagedPin storedPinProto$PlatformManagedPin = storedPinProto$PlatformManagedPinArr2[i];
                if (storedPinProto$PlatformManagedPin != null) {
                    codedOutputByteBufferNano.writeMessage(1, storedPinProto$PlatformManagedPin);
                }
                i++;
            }
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        StoredPinProto$PlatformManagedPin[] storedPinProto$PlatformManagedPinArr = this.pins;
        if (storedPinProto$PlatformManagedPinArr != null && storedPinProto$PlatformManagedPinArr.length > 0) {
            int i = 0;
            while (true) {
                StoredPinProto$PlatformManagedPin[] storedPinProto$PlatformManagedPinArr2 = this.pins;
                if (i >= storedPinProto$PlatformManagedPinArr2.length) {
                    break;
                }
                StoredPinProto$PlatformManagedPin storedPinProto$PlatformManagedPin = storedPinProto$PlatformManagedPinArr2[i];
                if (storedPinProto$PlatformManagedPin != null) {
                    iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(1, storedPinProto$PlatformManagedPin);
                }
                i++;
            }
        }
        return iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public StoredPinProto$PlatformManagedPins mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            if (tag == 0) {
                break;
            }
            if (tag != 10) {
                if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    break;
                }
            } else {
                int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 10);
                StoredPinProto$PlatformManagedPin[] storedPinProto$PlatformManagedPinArr = this.pins;
                int length = storedPinProto$PlatformManagedPinArr == null ? 0 : storedPinProto$PlatformManagedPinArr.length;
                int i = repeatedFieldArrayLength + length;
                StoredPinProto$PlatformManagedPin[] storedPinProto$PlatformManagedPinArr2 = new StoredPinProto$PlatformManagedPin[i];
                if (length != 0) {
                    System.arraycopy(storedPinProto$PlatformManagedPinArr, 0, storedPinProto$PlatformManagedPinArr2, 0, length);
                }
                while (length < i - 1) {
                    StoredPinProto$PlatformManagedPin storedPinProto$PlatformManagedPin = new StoredPinProto$PlatformManagedPin();
                    storedPinProto$PlatformManagedPinArr2[length] = storedPinProto$PlatformManagedPin;
                    codedInputByteBufferNano.readMessage(storedPinProto$PlatformManagedPin);
                    codedInputByteBufferNano.readTag();
                    length++;
                }
                StoredPinProto$PlatformManagedPin storedPinProto$PlatformManagedPin2 = new StoredPinProto$PlatformManagedPin();
                storedPinProto$PlatformManagedPinArr2[length] = storedPinProto$PlatformManagedPin2;
                codedInputByteBufferNano.readMessage(storedPinProto$PlatformManagedPin2);
                this.pins = storedPinProto$PlatformManagedPinArr2;
            }
        }
        return this;
    }

    public static StoredPinProto$PlatformManagedPins parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (StoredPinProto$PlatformManagedPins) MessageNano.mergeFrom(new StoredPinProto$PlatformManagedPins(), bArr);
    }

    public static StoredPinProto$PlatformManagedPins parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new StoredPinProto$PlatformManagedPins().mergeFrom(codedInputByteBufferNano);
    }
}
