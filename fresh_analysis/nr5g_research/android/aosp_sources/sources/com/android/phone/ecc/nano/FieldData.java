package com.android.phone.ecc.nano;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
class FieldData implements Cloneable {
    private List unknownFieldData = new ArrayList();
    private Object value;

    FieldData() {
    }

    void addUnknownField(UnknownFieldData unknownFieldData) {
        this.unknownFieldData.add(unknownFieldData);
    }

    int computeSerializedSize() {
        if (this.value != null) {
            throw null;
        }
        Iterator it = this.unknownFieldData.iterator();
        int iComputeSerializedSize = 0;
        while (it.hasNext()) {
            iComputeSerializedSize += ((UnknownFieldData) it.next()).computeSerializedSize();
        }
        return iComputeSerializedSize;
    }

    void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws CodedOutputByteBufferNano.OutOfSpaceException {
        if (this.value != null) {
            throw null;
        }
        Iterator it = this.unknownFieldData.iterator();
        while (it.hasNext()) {
            ((UnknownFieldData) it.next()).writeTo(codedOutputByteBufferNano);
        }
    }

    public boolean equals(Object obj) {
        List list;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FieldData)) {
            return false;
        }
        FieldData fieldData = (FieldData) obj;
        if (this.value != null && fieldData.value != null) {
            throw null;
        }
        List list2 = this.unknownFieldData;
        if (list2 != null && (list = fieldData.unknownFieldData) != null) {
            return list2.equals(list);
        }
        try {
            return Arrays.equals(toByteArray(), fieldData.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public int hashCode() {
        try {
            return 527 + Arrays.hashCode(toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private byte[] toByteArray() throws CodedOutputByteBufferNano.OutOfSpaceException {
        byte[] bArr = new byte[computeSerializedSize()];
        writeTo(CodedOutputByteBufferNano.newInstance(bArr));
        return bArr;
    }

    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public final FieldData m2047clone() {
        FieldData fieldData = new FieldData();
        try {
            List list = this.unknownFieldData;
            if (list == null) {
                fieldData.unknownFieldData = null;
            } else {
                fieldData.unknownFieldData.addAll(list);
            }
            Object obj = this.value;
            if (obj != null) {
                if (obj instanceof MessageNano) {
                    fieldData.value = ((MessageNano) obj).clone();
                    return fieldData;
                }
                if (obj instanceof byte[]) {
                    fieldData.value = ((byte[]) obj).clone();
                    return fieldData;
                }
                int i = 0;
                if (obj instanceof byte[][]) {
                    byte[][] bArr = (byte[][]) obj;
                    byte[][] bArr2 = new byte[bArr.length][];
                    fieldData.value = bArr2;
                    while (i < bArr.length) {
                        bArr2[i] = (byte[]) bArr[i].clone();
                        i++;
                    }
                } else {
                    if (obj instanceof boolean[]) {
                        fieldData.value = ((boolean[]) obj).clone();
                        return fieldData;
                    }
                    if (obj instanceof int[]) {
                        fieldData.value = ((int[]) obj).clone();
                        return fieldData;
                    }
                    if (obj instanceof long[]) {
                        fieldData.value = ((long[]) obj).clone();
                        return fieldData;
                    }
                    if (obj instanceof float[]) {
                        fieldData.value = ((float[]) obj).clone();
                        return fieldData;
                    }
                    if (obj instanceof double[]) {
                        fieldData.value = ((double[]) obj).clone();
                        return fieldData;
                    }
                    if (obj instanceof MessageNano[]) {
                        MessageNano[] messageNanoArr = (MessageNano[]) obj;
                        MessageNano[] messageNanoArr2 = new MessageNano[messageNanoArr.length];
                        fieldData.value = messageNanoArr2;
                        while (i < messageNanoArr.length) {
                            messageNanoArr2[i] = messageNanoArr[i].clone();
                            i++;
                        }
                    }
                }
            }
            return fieldData;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
}
