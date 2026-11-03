package com.android.internal.telephony.nano;

import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class StoredPinProto$PlatformManagedPin extends ExtendableMessageNano<StoredPinProto$PlatformManagedPin> {
    private static volatile StoredPinProto$PlatformManagedPin[] _emptyArray;
    public String iccid;
    public String oldPin;
    public String pin;

    public static StoredPinProto$PlatformManagedPin[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new StoredPinProto$PlatformManagedPin[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public StoredPinProto$PlatformManagedPin() {
        clear();
    }

    public StoredPinProto$PlatformManagedPin clear() {
        this.iccid = "";
        this.pin = "";
        this.oldPin = "";
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        if (!this.iccid.equals("")) {
            codedOutputByteBufferNano.writeString(1, this.iccid);
        }
        if (!this.pin.equals("")) {
            codedOutputByteBufferNano.writeString(2, this.pin);
        }
        if (!this.oldPin.equals("")) {
            codedOutputByteBufferNano.writeString(3, this.oldPin);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        if (!this.iccid.equals("")) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.iccid);
        }
        if (!this.pin.equals("")) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(2, this.pin);
        }
        return !this.oldPin.equals("") ? iComputeSerializedSize + CodedOutputByteBufferNano.computeStringSize(3, this.oldPin) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public StoredPinProto$PlatformManagedPin mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            if (tag == 0) {
                break;
            }
            if (tag == 10) {
                this.iccid = codedInputByteBufferNano.readString();
            } else if (tag == 18) {
                this.pin = codedInputByteBufferNano.readString();
            } else if (tag != 26) {
                if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    break;
                }
            } else {
                this.oldPin = codedInputByteBufferNano.readString();
            }
        }
        return this;
    }

    public static StoredPinProto$PlatformManagedPin parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (StoredPinProto$PlatformManagedPin) MessageNano.mergeFrom(new StoredPinProto$PlatformManagedPin(), bArr);
    }

    public static StoredPinProto$PlatformManagedPin parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new StoredPinProto$PlatformManagedPin().mergeFrom(codedInputByteBufferNano);
    }
}
