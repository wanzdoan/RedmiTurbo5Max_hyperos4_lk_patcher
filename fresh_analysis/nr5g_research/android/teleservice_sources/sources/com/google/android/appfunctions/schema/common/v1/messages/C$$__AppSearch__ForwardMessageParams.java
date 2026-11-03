package com.google.android.appfunctions.schema.common.v1.messages;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.messages.$$__AppSearch__ForwardMessageParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__ForwardMessageParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(ForwardMessageParams forwardMessageParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(forwardMessageParams.getNamespace(), forwardMessageParams.getId(), "com.google.android.appfunctions.schema.common.v1.messages.ForwardMessageParams");
        String messageId = forwardMessageParams.getMessageId();
        if (messageId != null) {
            builder.setPropertyString("messageId", messageId);
        }
        List recipientsIds = forwardMessageParams.getRecipientsIds();
        if (recipientsIds != null) {
            builder.setPropertyString("recipientsIds", (String[]) recipientsIds.toArray(new String[0]));
        }
        return builder.build();
    }
}
