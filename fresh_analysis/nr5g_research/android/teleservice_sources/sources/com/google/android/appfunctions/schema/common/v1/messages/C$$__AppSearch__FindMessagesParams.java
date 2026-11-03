package com.google.android.appfunctions.schema.common.v1.messages;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.messages.$$__AppSearch__FindMessagesParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__FindMessagesParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(FindMessagesParams findMessagesParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(findMessagesParams.getNamespace(), findMessagesParams.getId(), "com.google.android.appfunctions.schema.common.v1.messages.FindMessagesParams");
        String senderId = findMessagesParams.getSenderId();
        if (senderId != null) {
            builder.setPropertyString("senderId", senderId);
        }
        List recipientsIds = findMessagesParams.getRecipientsIds();
        if (recipientsIds != null) {
            builder.setPropertyString("recipientsIds", (String[]) recipientsIds.toArray(new String[0]));
        }
        findMessagesParams.getStartTime();
        findMessagesParams.getEndTime();
        String query = findMessagesParams.getQuery();
        if (query != null) {
            builder.setPropertyString("query", query);
        }
        Boolean read = findMessagesParams.getRead();
        if (read != null) {
            builder.setPropertyBoolean("read", read.booleanValue());
        }
        builder.setPropertyLong("maxCount", findMessagesParams.getMaxCount());
        return builder.build();
    }
}
