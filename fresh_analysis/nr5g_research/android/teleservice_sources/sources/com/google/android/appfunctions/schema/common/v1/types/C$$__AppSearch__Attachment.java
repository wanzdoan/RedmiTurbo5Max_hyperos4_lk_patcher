package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__Attachment, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Attachment implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Attachment attachment) {
        GenericDocument.Builder builder = new GenericDocument.Builder(attachment.getNamespace(), attachment.getId(), "com.google.android.appfunctions.schema.common.v1.types.Attachment");
        String displayName = attachment.getDisplayName();
        if (displayName != null) {
            builder.setPropertyString("displayName", displayName);
        }
        String mimeType = attachment.getMimeType();
        if (mimeType != null) {
            builder.setPropertyString("mimeType", mimeType);
        }
        attachment.getUri();
        return builder.build();
    }
}
