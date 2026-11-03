package com.android.phone.ecc.nano;

import com.android.internal.telephony.Phone$$ExternalSyntheticBUOutline1;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public abstract class MessageNano {
    protected volatile int cachedSize = -1;

    public abstract MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano);

    public static final MessageNano mergeFrom(MessageNano messageNano, byte[] bArr) {
        return mergeFrom(messageNano, bArr, 0, bArr.length);
    }

    public static final MessageNano mergeFrom(MessageNano messageNano, byte[] bArr, int i, int i2) throws InvalidProtocolBufferNanoException {
        try {
            CodedInputByteBufferNano codedInputByteBufferNanoNewInstance = CodedInputByteBufferNano.newInstance(bArr, i, i2);
            messageNano.mergeFrom(codedInputByteBufferNanoNewInstance);
            codedInputByteBufferNanoNewInstance.checkLastTagWas(0);
            return messageNano;
        } catch (InvalidProtocolBufferNanoException e) {
            throw e;
        } catch (IOException unused) {
            Phone$$ExternalSyntheticBUOutline1.m("Reading from a byte array threw an IOException (should never happen).");
            return null;
        }
    }

    public String toString() {
        return MessageNanoPrinter.print(this);
    }

    public MessageNano clone() {
        return (MessageNano) super.clone();
    }
}
