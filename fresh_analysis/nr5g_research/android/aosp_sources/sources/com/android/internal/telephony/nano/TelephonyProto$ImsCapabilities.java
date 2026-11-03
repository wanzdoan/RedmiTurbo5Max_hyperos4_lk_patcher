package com.android.internal.telephony.nano;

import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class TelephonyProto$ImsCapabilities extends ExtendableMessageNano<TelephonyProto$ImsCapabilities> {
    private static volatile TelephonyProto$ImsCapabilities[] _emptyArray;
    public boolean utOverLte;
    public boolean utOverWifi;
    public boolean videoOverLte;
    public boolean videoOverWifi;
    public boolean voiceOverLte;
    public boolean voiceOverWifi;

    public static TelephonyProto$ImsCapabilities[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new TelephonyProto$ImsCapabilities[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public TelephonyProto$ImsCapabilities() {
        clear();
    }

    public TelephonyProto$ImsCapabilities clear() {
        this.voiceOverLte = false;
        this.voiceOverWifi = false;
        this.videoOverLte = false;
        this.videoOverWifi = false;
        this.utOverLte = false;
        this.utOverWifi = false;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        boolean z = this.voiceOverLte;
        if (z) {
            codedOutputByteBufferNano.writeBool(1, z);
        }
        boolean z2 = this.voiceOverWifi;
        if (z2) {
            codedOutputByteBufferNano.writeBool(2, z2);
        }
        boolean z3 = this.videoOverLte;
        if (z3) {
            codedOutputByteBufferNano.writeBool(3, z3);
        }
        boolean z4 = this.videoOverWifi;
        if (z4) {
            codedOutputByteBufferNano.writeBool(4, z4);
        }
        boolean z5 = this.utOverLte;
        if (z5) {
            codedOutputByteBufferNano.writeBool(5, z5);
        }
        boolean z6 = this.utOverWifi;
        if (z6) {
            codedOutputByteBufferNano.writeBool(6, z6);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        boolean z = this.voiceOverLte;
        if (z) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(1, z);
        }
        boolean z2 = this.voiceOverWifi;
        if (z2) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(2, z2);
        }
        boolean z3 = this.videoOverLte;
        if (z3) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(3, z3);
        }
        boolean z4 = this.videoOverWifi;
        if (z4) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(4, z4);
        }
        boolean z5 = this.utOverLte;
        if (z5) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(5, z5);
        }
        boolean z6 = this.utOverWifi;
        return z6 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeBoolSize(6, z6) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public TelephonyProto$ImsCapabilities mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            if (tag == 0) {
                break;
            }
            if (tag == 8) {
                this.voiceOverLte = codedInputByteBufferNano.readBool();
            } else if (tag == 16) {
                this.voiceOverWifi = codedInputByteBufferNano.readBool();
            } else if (tag == 24) {
                this.videoOverLte = codedInputByteBufferNano.readBool();
            } else if (tag == 32) {
                this.videoOverWifi = codedInputByteBufferNano.readBool();
            } else if (tag == 40) {
                this.utOverLte = codedInputByteBufferNano.readBool();
            } else if (tag != 48) {
                if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                    break;
                }
            } else {
                this.utOverWifi = codedInputByteBufferNano.readBool();
            }
        }
        return this;
    }

    public static TelephonyProto$ImsCapabilities parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (TelephonyProto$ImsCapabilities) MessageNano.mergeFrom(new TelephonyProto$ImsCapabilities(), bArr);
    }

    public static TelephonyProto$ImsCapabilities parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new TelephonyProto$ImsCapabilities().mergeFrom(codedInputByteBufferNano);
    }
}
