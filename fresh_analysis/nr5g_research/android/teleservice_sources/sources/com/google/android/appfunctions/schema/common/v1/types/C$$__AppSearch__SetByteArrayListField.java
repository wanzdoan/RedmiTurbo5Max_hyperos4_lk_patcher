package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__SetByteArrayListField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetByteArrayListField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetByteArrayListField setByteArrayListField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setByteArrayListField.getNamespace(), setByteArrayListField.getId(), "com.google.android.appfunctions.schema.common.v1.types.SetByteArrayListField");
        List value = setByteArrayListField.getValue();
        if (value != null) {
            byte[][] bArr = new byte[value.size()][];
            Iterator it = value.iterator();
            int i = 0;
            while (it.hasNext()) {
                bArr[i] = (byte[]) it.next();
                i++;
            }
            builder.setPropertyBytes(a.p, bArr);
        }
        return builder.build();
    }
}
