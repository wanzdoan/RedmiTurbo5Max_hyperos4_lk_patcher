package com.android.internal.telephony.nano;

import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class TelephonyProto$Time extends ExtendableMessageNano<TelephonyProto$Time> {
    private static volatile TelephonyProto$Time[] _emptyArray;
    public long elapsedTimestampMillis;
    public long systemTimestampMillis;

    public static TelephonyProto$Time[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new TelephonyProto$Time[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public TelephonyProto$Time() {
        clear();
    }

    public TelephonyProto$Time clear() {
        this.systemTimestampMillis = 0L;
        this.elapsedTimestampMillis = 0L;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        long j = this.systemTimestampMillis;
        if (j != 0) {
            codedOutputByteBufferNano.writeInt64(1, j);
        }
        long j2 = this.elapsedTimestampMillis;
        if (j2 != 0) {
            codedOutputByteBufferNano.writeInt64(2, j2);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        long j = this.systemTimestampMillis;
        if (j != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(1, j);
        }
        long j2 = this.elapsedTimestampMillis;
        return j2 != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt64Size(2, j2) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public TelephonyProto$Time mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            if (tag == 0) {
                break;
            }
            if (tag == 8) {
                this.systemTimestampMillis = codedInputByteBufferNano.readInt64();
            } else if (tag != 16) {
                if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    break;
                }
            } else {
                this.elapsedTimestampMillis = codedInputByteBufferNano.readInt64();
            }
        }
        return this;
    }

    public static TelephonyProto$Time parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (TelephonyProto$Time) MessageNano.mergeFrom(new TelephonyProto$Time(), bArr);
    }

    public static TelephonyProto$Time parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new TelephonyProto$Time().mergeFrom(codedInputByteBufferNano);
    }
}
