package com.android.internal.telephony.nano;

import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class TelephonyProto$RilDataCall extends ExtendableMessageNano<TelephonyProto$RilDataCall> {
    private static volatile TelephonyProto$RilDataCall[] _emptyArray;
    public int apnTypeBitmask;
    public int cid;
    public String ifname;
    public int state;
    public int type;

    public interface State {
        public static final int CONNECTED = 1;
        public static final int DISCONNECTED = 2;
        public static final int UNKNOWN = 0;
    }

    public static TelephonyProto$RilDataCall[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new TelephonyProto$RilDataCall[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public TelephonyProto$RilDataCall() {
        clear();
    }

    public TelephonyProto$RilDataCall clear() {
        this.cid = 0;
        this.type = 0;
        this.ifname = "";
        this.state = 0;
        this.apnTypeBitmask = 0;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        int i = this.cid;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(1, i);
        }
        int i2 = this.type;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(2, i2);
        }
        if (!this.ifname.equals("")) {
            codedOutputByteBufferNano.writeString(3, this.ifname);
        }
        int i3 = this.state;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(4, i3);
        }
        int i4 = this.apnTypeBitmask;
        if (i4 != 0) {
            codedOutputByteBufferNano.writeInt32(5, i4);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        int i = this.cid;
        if (i != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
        }
        int i2 = this.type;
        if (i2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i2);
        }
        if (!this.ifname.equals("")) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(3, this.ifname);
        }
        int i3 = this.state;
        if (i3 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(4, i3);
        }
        int i4 = this.apnTypeBitmask;
        return i4 != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(5, i4) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public TelephonyProto$RilDataCall mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            if (tag != 0) {
                if (tag == 8) {
                    this.cid = codedInputByteBufferNano.readInt32();
                } else if (tag == 16) {
                    int int32 = codedInputByteBufferNano.readInt32();
                    switch (int32) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                            this.type = int32;
                            break;
                    }
                } else if (tag == 26) {
                    this.ifname = codedInputByteBufferNano.readString();
                } else if (tag == 32) {
                    int int33 = codedInputByteBufferNano.readInt32();
                    if (int33 == 0 || int33 == 1 || int33 == 2) {
                        this.state = int33;
                    }
                } else if (tag != 40) {
                    if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    }
                } else {
                    this.apnTypeBitmask = codedInputByteBufferNano.readInt32();
                }
            }
        }
        return this;
    }

    public static TelephonyProto$RilDataCall parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (TelephonyProto$RilDataCall) MessageNano.mergeFrom(new TelephonyProto$RilDataCall(), bArr);
    }

    public static TelephonyProto$RilDataCall parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new TelephonyProto$RilDataCall().mergeFrom(codedInputByteBufferNano);
    }
}
