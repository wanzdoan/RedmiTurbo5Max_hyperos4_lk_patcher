package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__SetUriNullableField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetUriNullableField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetUriNullableField setUriNullableField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setUriNullableField.getNamespace(), setUriNullableField.getId(), "com.google.android.appfunctions.schema.common.v1.types.SetUriNullableField");
        setUriNullableField.getValue();
        return builder.build();
    }
}
