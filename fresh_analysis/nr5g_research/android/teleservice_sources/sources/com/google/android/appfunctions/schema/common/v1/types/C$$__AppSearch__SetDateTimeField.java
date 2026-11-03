package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__SetDateTimeField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetDateTimeField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetDateTimeField setDateTimeField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setDateTimeField.getNamespace(), setDateTimeField.getId(), "com.google.android.appfunctions.schema.common.v1.types.SetDateTimeField");
        setDateTimeField.getValue();
        return builder.build();
    }
}
