package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__SetStringNullableField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetStringNullableField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetStringNullableField setStringNullableField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setStringNullableField.getNamespace(), setStringNullableField.getId(), "com.google.android.appfunctions.schema.common.v1.types.SetStringNullableField");
        String value = setStringNullableField.getValue();
        if (value != null) {
            builder.setPropertyString(a.p, value);
        }
        return builder.build();
    }
}
