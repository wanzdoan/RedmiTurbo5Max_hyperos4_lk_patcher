package com.android.internal.telephony.nano;

import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class PersistAtomsProto$CarrierIdMismatch extends ExtendableMessageNano<PersistAtomsProto$CarrierIdMismatch> {
    private static volatile PersistAtomsProto$CarrierIdMismatch[] _emptyArray;
    public String gid1;
    public String mccMnc;
    public String pnn;
    public String spn;

    public static PersistAtomsProto$CarrierIdMismatch[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new PersistAtomsProto$CarrierIdMismatch[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public PersistAtomsProto$CarrierIdMismatch() {
        clear();
    }

    public PersistAtomsProto$CarrierIdMismatch clear() {
        this.mccMnc = "";
        this.gid1 = "";
        this.spn = "";
        this.pnn = "";
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        if (!this.mccMnc.equals("")) {
            codedOutputByteBufferNano.writeString(1, this.mccMnc);
        }
        if (!this.gid1.equals("")) {
            codedOutputByteBufferNano.writeString(2, this.gid1);
        }
        if (!this.spn.equals("")) {
            codedOutputByteBufferNano.writeString(3, this.spn);
        }
        if (!this.pnn.equals("")) {
            codedOutputByteBufferNano.writeString(4, this.pnn);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        if (!this.mccMnc.equals("")) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.mccMnc);
        }
        if (!this.gid1.equals("")) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(2, this.gid1);
        }
        if (!this.spn.equals("")) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(3, this.spn);
        }
        return !this.pnn.equals("") ? iComputeSerializedSize + CodedOutputByteBufferNano.computeStringSize(4, this.pnn) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public PersistAtomsProto$CarrierIdMismatch mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            if (tag == 0) {
                break;
            }
            if (tag == 10) {
                this.mccMnc = codedInputByteBufferNano.readString();
            } else if (tag == 18) {
                this.gid1 = codedInputByteBufferNano.readString();
            } else if (tag == 26) {
                this.spn = codedInputByteBufferNano.readString();
            } else if (tag != 34) {
                if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    break;
                }
            } else {
                this.pnn = codedInputByteBufferNano.readString();
            }
        }
        return this;
    }

    public static PersistAtomsProto$CarrierIdMismatch parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (PersistAtomsProto$CarrierIdMismatch) MessageNano.mergeFrom(new PersistAtomsProto$CarrierIdMismatch(), bArr);
    }

    public static PersistAtomsProto$CarrierIdMismatch parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new PersistAtomsProto$CarrierIdMismatch().mergeFrom(codedInputByteBufferNano);
    }
}
