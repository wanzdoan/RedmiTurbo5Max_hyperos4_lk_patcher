package com.google.android.appfunctions.schema.common.v1.email;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.email.$$__AppSearch__SendEmailParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SendEmailParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SendEmailParams sendEmailParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(sendEmailParams.getNamespace(), sendEmailParams.getId(), "com.google.android.appfunctions.schema.common.v1.email.SendEmailParams");
        String emailId = sendEmailParams.getEmailId();
        if (emailId != null) {
            builder.setPropertyString("emailId", emailId);
        }
        return builder.build();
    }
}
