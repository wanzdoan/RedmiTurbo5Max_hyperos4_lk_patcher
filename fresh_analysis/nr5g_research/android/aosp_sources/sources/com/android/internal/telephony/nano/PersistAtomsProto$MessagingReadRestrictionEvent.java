package com.android.internal.telephony.nano;

import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class PersistAtomsProto$MessagingReadRestrictionEvent extends ExtendableMessageNano<PersistAtomsProto$MessagingReadRestrictionEvent> {
    private static volatile PersistAtomsProto$MessagingReadRestrictionEvent[] _emptyArray;
    public int callerUid;
    public int contentProvider;
    public int count;
    public int eventType;
    public int readRestrictedMessagesAppOpMode;

    public static PersistAtomsProto$MessagingReadRestrictionEvent[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new PersistAtomsProto$MessagingReadRestrictionEvent[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public PersistAtomsProto$MessagingReadRestrictionEvent() {
        clear();
    }

    public PersistAtomsProto$MessagingReadRestrictionEvent clear() {
        this.callerUid = 0;
        this.contentProvider = 0;
        this.eventType = 0;
        this.readRestrictedMessagesAppOpMode = 0;
        this.count = 0;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        int i = this.callerUid;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(1, i);
        }
        int i2 = this.contentProvider;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(2, i2);
        }
        int i3 = this.eventType;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(3, i3);
        }
        int i4 = this.readRestrictedMessagesAppOpMode;
        if (i4 != 0) {
            codedOutputByteBufferNano.writeInt32(4, i4);
        }
        int i5 = this.count;
        if (i5 != 0) {
            codedOutputByteBufferNano.writeInt32(5, i5);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        int i = this.callerUid;
        if (i != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
        }
        int i2 = this.contentProvider;
        if (i2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i2);
        }
        int i3 = this.eventType;
        if (i3 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, i3);
        }
        int i4 = this.readRestrictedMessagesAppOpMode;
        if (i4 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(4, i4);
        }
        int i5 = this.count;
        return i5 != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(5, i5) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public PersistAtomsProto$MessagingReadRestrictionEvent mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            if (tag == 0) {
                break;
            }
            if (tag == 8) {
                this.callerUid = codedInputByteBufferNano.readInt32();
            } else if (tag == 16) {
                this.contentProvider = codedInputByteBufferNano.readInt32();
            } else if (tag == 24) {
                this.eventType = codedInputByteBufferNano.readInt32();
            } else if (tag == 32) {
                this.readRestrictedMessagesAppOpMode = codedInputByteBufferNano.readInt32();
            } else if (tag != 40) {
                if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    break;
                }
            } else {
                this.count = codedInputByteBufferNano.readInt32();
            }
        }
        return this;
    }

    public static PersistAtomsProto$MessagingReadRestrictionEvent parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (PersistAtomsProto$MessagingReadRestrictionEvent) MessageNano.mergeFrom(new PersistAtomsProto$MessagingReadRestrictionEvent(), bArr);
    }

    public static PersistAtomsProto$MessagingReadRestrictionEvent parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new PersistAtomsProto$MessagingReadRestrictionEvent().mergeFrom(codedInputByteBufferNano);
    }
}
