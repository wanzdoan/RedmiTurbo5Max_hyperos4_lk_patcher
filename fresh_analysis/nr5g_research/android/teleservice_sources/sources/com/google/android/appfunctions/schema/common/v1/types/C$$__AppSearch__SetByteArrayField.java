package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__SetByteArrayField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetByteArrayField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetByteArrayField setByteArrayField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setByteArrayField.getNamespace(), setByteArrayField.getId(), "com.google.android.appfunctions.schema.common.v1.types.SetByteArrayField");
        byte[] value = setByteArrayField.getValue();
        if (value != null) {
            builder.setPropertyBytes(a.p, value);
        }
        return builder.build();
    }
}
