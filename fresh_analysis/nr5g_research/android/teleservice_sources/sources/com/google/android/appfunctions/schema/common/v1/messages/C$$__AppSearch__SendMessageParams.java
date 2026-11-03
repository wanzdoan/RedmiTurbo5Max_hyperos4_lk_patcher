package com.google.android.appfunctions.schema.common.v1.messages;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.google.android.appfunctions.schema.common.v1.types.Attachment;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.messages.$$__AppSearch__SendMessageParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SendMessageParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SendMessageParams sendMessageParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(sendMessageParams.getNamespace(), sendMessageParams.getId(), "com.google.android.appfunctions.schema.common.v1.messages.SendMessageParams");
        String body = sendMessageParams.getBody();
        if (body != null) {
            builder.setPropertyString("body", body);
        }
        List recipientsIds = sendMessageParams.getRecipientsIds();
        int i = 0;
        if (recipientsIds != null) {
            builder.setPropertyString("recipientsIds", (String[]) recipientsIds.toArray(new String[0]));
        }
        List messageAttachments = sendMessageParams.getMessageAttachments();
        if (messageAttachments != null) {
            GenericDocument[] genericDocumentArr = new GenericDocument[messageAttachments.size()];
            Iterator it = messageAttachments.iterator();
            while (it.hasNext()) {
                genericDocumentArr[i] = GenericDocument.fromDocumentClass((Attachment) it.next());
                i++;
            }
            builder.setPropertyDocument("messageAttachments", genericDocumentArr);
        }
        return builder.build();
    }
}
