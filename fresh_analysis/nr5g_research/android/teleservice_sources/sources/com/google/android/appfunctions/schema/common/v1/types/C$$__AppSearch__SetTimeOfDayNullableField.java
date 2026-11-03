package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__SetTimeOfDayNullableField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetTimeOfDayNullableField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetTimeOfDayNullableField setTimeOfDayNullableField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setTimeOfDayNullableField.getNamespace(), setTimeOfDayNullableField.getId(), "com.google.android.appfunctions.schema.common.v1.types.SetTimeOfDayNullableField");
        setTimeOfDayNullableField.getValue();
        return builder.build();
    }
}
