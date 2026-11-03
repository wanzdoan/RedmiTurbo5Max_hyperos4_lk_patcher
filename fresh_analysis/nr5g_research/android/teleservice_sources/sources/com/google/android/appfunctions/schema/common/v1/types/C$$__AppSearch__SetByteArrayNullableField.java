package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__SetByteArrayNullableField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetByteArrayNullableField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetByteArrayNullableField setByteArrayNullableField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setByteArrayNullableField.getNamespace(), setByteArrayNullableField.getId(), "com.google.android.appfunctions.schema.common.v1.types.SetByteArrayNullableField");
        byte[] value = setByteArrayNullableField.getValue();
        if (value != null) {
            builder.setPropertyBytes(a.p, value);
        }
        return builder.build();
    }
}
