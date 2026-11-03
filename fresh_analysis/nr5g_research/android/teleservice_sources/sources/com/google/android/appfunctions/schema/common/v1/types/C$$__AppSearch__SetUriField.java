package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__SetUriField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetUriField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetUriField setUriField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setUriField.getNamespace(), setUriField.getId(), "com.google.android.appfunctions.schema.common.v1.types.SetUriField");
        setUriField.getValue();
        return builder.build();
    }
}
