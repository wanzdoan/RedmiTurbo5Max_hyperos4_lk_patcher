package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__SetIntNullableField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetIntNullableField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetIntNullableField setIntNullableField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setIntNullableField.getNamespace(), setIntNullableField.getId(), "com.google.android.appfunctions.schema.common.v1.types.SetIntNullableField");
        Integer value = setIntNullableField.getValue();
        if (value != null) {
            builder.setPropertyLong(a.p, value.intValue());
        }
        return builder.build();
    }
}
