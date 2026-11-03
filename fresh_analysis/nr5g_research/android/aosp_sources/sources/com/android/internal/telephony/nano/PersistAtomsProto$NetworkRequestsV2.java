package com.android.internal.telephony.nano;

import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class PersistAtomsProto$NetworkRequestsV2 extends ExtendableMessageNano<PersistAtomsProto$NetworkRequestsV2> {
    private static volatile PersistAtomsProto$NetworkRequestsV2[] _emptyArray;
    public int capability;
    public int carrierId;
    public int requestCount;

    public interface NetworkCapability {
        public static final int CBS = 3;
        public static final int ENTERPRISE = 4;
        public static final int PRIORITIZE_BANDWIDTH = 2;
        public static final int PRIORITIZE_LATENCY = 1;
        public static final int PRIORITIZE_UNIFIED_COMMUNICATIONS = 11;
        public static final int SATELLITE_EIMS_RESTRICTED = 9;
        public static final int SATELLITE_IMS_RESTRICTED = 7;
        public static final int SATELLITE_INTERNET_RESTRICTED = 5;
        public static final int SATELLITE_MMS_RESTRICTED = 6;
        public static final int SATELLITE_SUPL_RESTRICTED = 10;
        public static final int SATELLITE_XCAP_RESTRICTED = 8;
        public static final int UNKNOWN = 0;
    }

    public static PersistAtomsProto$NetworkRequestsV2[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new PersistAtomsProto$NetworkRequestsV2[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public PersistAtomsProto$NetworkRequestsV2() {
        clear();
    }

    public PersistAtomsProto$NetworkRequestsV2 clear() {
        this.carrierId = 0;
        this.capability = 0;
        this.requestCount = 0;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        int i = this.carrierId;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(1, i);
        }
        int i2 = this.capability;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(2, i2);
        }
        int i3 = this.requestCount;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(3, i3);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        int i = this.carrierId;
        if (i != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
        }
        int i2 = this.capability;
        if (i2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i2);
        }
        int i3 = this.requestCount;
        return i3 != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(3, i3) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public PersistAtomsProto$NetworkRequestsV2 mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            if (tag != 0) {
                if (tag == 8) {
                    this.carrierId = codedInputByteBufferNano.readInt32();
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
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                            this.capability = int32;
                            break;
                    }
                } else if (tag != 24) {
                    if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    }
                } else {
                    this.requestCount = codedInputByteBufferNano.readInt32();
                }
            }
        }
        return this;
    }

    public static PersistAtomsProto$NetworkRequestsV2 parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (PersistAtomsProto$NetworkRequestsV2) MessageNano.mergeFrom(new PersistAtomsProto$NetworkRequestsV2(), bArr);
    }

    public static PersistAtomsProto$NetworkRequestsV2 parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new PersistAtomsProto$NetworkRequestsV2().mergeFrom(codedInputByteBufferNano);
    }
}
