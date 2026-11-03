package com.google.android.appfunctions.schema.common.v1.email;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.email.$$__AppSearch__UpdateEmailParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__UpdateEmailParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(UpdateEmailParams updateEmailParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(updateEmailParams.getNamespace(), updateEmailParams.getId(), "com.google.android.appfunctions.schema.common.v1.email.UpdateEmailParams");
        String emailId = updateEmailParams.getEmailId();
        if (emailId != null) {
            builder.setPropertyString("emailId", emailId);
        }
        updateEmailParams.getSubject();
        updateEmailParams.getMessage();
        updateEmailParams.getRecipientIds();
        updateEmailParams.getCcRecipientIds();
        updateEmailParams.getBccRecipientIds();
        updateEmailParams.isRead();
        return builder.build();
    }
}
