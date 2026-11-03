package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__SetAttachmentNullableField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetAttachmentNullableField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetAttachmentNullableField setAttachmentNullableField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setAttachmentNullableField.getNamespace(), setAttachmentNullableField.getId(), "com.google.android.appfunctions.schema.common.v1.types.SetAttachmentNullableField");
        setAttachmentNullableField.getValue();
        return builder.build();
    }
}
