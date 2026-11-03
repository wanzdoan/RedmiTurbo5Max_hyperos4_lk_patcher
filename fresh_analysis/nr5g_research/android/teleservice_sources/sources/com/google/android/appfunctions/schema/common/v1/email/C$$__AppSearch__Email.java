package com.google.android.appfunctions.schema.common.v1.email;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.email.$$__AppSearch__Email, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Email implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Email email) {
        GenericDocument.Builder builder = new GenericDocument.Builder(email.getNamespace(), email.getId(), "com.google.android.appfunctions.schema.common.v1.email.Email");
        String subject = email.getSubject();
        if (subject != null) {
            builder.setPropertyString("subject", subject);
        }
        String message = email.getMessage();
        if (message != null) {
            builder.setPropertyString(a.m, message);
        }
        String senderId = email.getSenderId();
        if (senderId != null) {
            builder.setPropertyString("senderId", senderId);
        }
        List recipientIds = email.getRecipientIds();
        if (recipientIds != null) {
            builder.setPropertyString("recipientIds", (String[]) recipientIds.toArray(new String[0]));
        }
        List ccRecipientIds = email.getCcRecipientIds();
        if (ccRecipientIds != null) {
            builder.setPropertyString("ccRecipientIds", (String[]) ccRecipientIds.toArray(new String[0]));
        }
        List bccRecipientIds = email.getBccRecipientIds();
        if (bccRecipientIds != null) {
            builder.setPropertyString("bccRecipientIds", (String[]) bccRecipientIds.toArray(new String[0]));
        }
        Boolean boolIsDraft = email.isDraft();
        if (boolIsDraft != null) {
            builder.setPropertyBoolean("isDraft", boolIsDraft.booleanValue());
        }
        email.getDateSent();
        email.getDateReceived();
        email.getDateRead();
        return builder.build();
    }
}
