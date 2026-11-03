package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__SetAttachmentField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetAttachmentField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetAttachmentField setAttachmentField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setAttachmentField.getNamespace(), setAttachmentField.getId(), "com.google.android.appfunctions.schema.common.v1.types.SetAttachmentField");
        setAttachmentField.getValue();
        return builder.build();
    }
}
