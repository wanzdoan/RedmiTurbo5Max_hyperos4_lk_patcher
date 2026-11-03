package com.google.android.appfunctions.schema.common.v1.email;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.email.$$__AppSearch__FindEmailsParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__FindEmailsParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(FindEmailsParams findEmailsParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(findEmailsParams.getNamespace(), findEmailsParams.getId(), "com.google.android.appfunctions.schema.common.v1.email.FindEmailsParams");
        String query = findEmailsParams.getQuery();
        if (query != null) {
            builder.setPropertyString("query", query);
        }
        findEmailsParams.getStartTime();
        findEmailsParams.getEndTime();
        Boolean sent = findEmailsParams.getSent();
        if (sent != null) {
            builder.setPropertyBoolean("sent", sent.booleanValue());
        }
        Boolean read = findEmailsParams.getRead();
        if (read != null) {
            builder.setPropertyBoolean("read", read.booleanValue());
        }
        List senderIds = findEmailsParams.getSenderIds();
        if (senderIds != null) {
            builder.setPropertyString("senderIds", (String[]) senderIds.toArray(new String[0]));
        }
        List recipientIds = findEmailsParams.getRecipientIds();
        if (recipientIds != null) {
            builder.setPropertyString("recipientIds", (String[]) recipientIds.toArray(new String[0]));
        }
        List labels = findEmailsParams.getLabels();
        if (labels != null) {
            builder.setPropertyString("labels", (String[]) labels.toArray(new String[0]));
        }
        builder.setPropertyLong("maxCount", findEmailsParams.getMaxCount());
        return builder.build();
    }
}
