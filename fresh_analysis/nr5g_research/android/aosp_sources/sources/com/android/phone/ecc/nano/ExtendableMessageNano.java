package com.android.phone.ecc.nano;

/* JADX INFO: loaded from: classes.dex */
public abstract class ExtendableMessageNano extends MessageNano {
    protected FieldArray unknownFieldData;

    protected final boolean storeUnknownField(CodedInputByteBufferNano codedInputByteBufferNano, int i) {
        FieldData fieldData;
        int position = codedInputByteBufferNano.getPosition();
        if (!codedInputByteBufferNano.skipField(i)) {
            return false;
        }
        int tagFieldNumber = WireFormatNano.getTagFieldNumber(i);
        UnknownFieldData unknownFieldData = new UnknownFieldData(i, codedInputByteBufferNano.getData(position, codedInputByteBufferNano.getPosition() - position));
        FieldArray fieldArray = this.unknownFieldData;
        if (fieldArray == null) {
            this.unknownFieldData = new FieldArray();
            fieldData = null;
        } else {
            fieldData = fieldArray.get(tagFieldNumber);
        }
        if (fieldData == null) {
            fieldData = new FieldData();
            this.unknownFieldData.put(tagFieldNumber, fieldData);
        }
        fieldData.addUnknownField(unknownFieldData);
        return true;
    }

    @Override // com.android.phone.ecc.nano.MessageNano
    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public ExtendableMessageNano m2045clone() {
        ExtendableMessageNano extendableMessageNano = (ExtendableMessageNano) super.m2045clone();
        InternalNano.cloneUnknownFieldData(this, extendableMessageNano);
        return extendableMessageNano;
    }
}
