package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__SetDateTimeNullableField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetDateTimeNullableField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetDateTimeNullableField setDateTimeNullableField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setDateTimeNullableField.getNamespace(), setDateTimeNullableField.getId(), "com.google.android.appfunctions.schema.common.v1.types.SetDateTimeNullableField");
        setDateTimeNullableField.getValue();
        return builder.build();
    }
}
