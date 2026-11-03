package com.google.android.appfunctions.schema.common.v1.email;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.email.$$__AppSearch__CreateEmailDraftParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__CreateEmailDraftParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(CreateEmailDraftParams createEmailDraftParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(createEmailDraftParams.getNamespace(), createEmailDraftParams.getId(), "com.google.android.appfunctions.schema.common.v1.email.CreateEmailDraftParams");
        String subject = createEmailDraftParams.getSubject();
        if (subject != null) {
            builder.setPropertyString("subject", subject);
        }
        String message = createEmailDraftParams.getMessage();
        if (message != null) {
            builder.setPropertyString(a.m, message);
        }
        String senderAccountId = createEmailDraftParams.getSenderAccountId();
        if (senderAccountId != null) {
            builder.setPropertyString("senderAccountId", senderAccountId);
        }
        List recipientIds = createEmailDraftParams.getRecipientIds();
        if (recipientIds != null) {
            builder.setPropertyString("recipientIds", (String[]) recipientIds.toArray(new String[0]));
        }
        List ccRecipientIds = createEmailDraftParams.getCcRecipientIds();
        if (ccRecipientIds != null) {
            builder.setPropertyString("ccRecipientIds", (String[]) ccRecipientIds.toArray(new String[0]));
        }
        List bccRecipientIds = createEmailDraftParams.getBccRecipientIds();
        if (bccRecipientIds != null) {
            builder.setPropertyString("bccRecipientIds", (String[]) bccRecipientIds.toArray(new String[0]));
        }
        return builder.build();
    }
}
