package com.google.android.appfunctions.schema.common.v1.messages;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.google.android.appfunctions.schema.common.v1.types.Attachment;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.messages.$$__AppSearch__Message, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Message implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Message message) {
        GenericDocument.Builder builder = new GenericDocument.Builder(message.getNamespace(), message.getId(), "com.google.android.appfunctions.schema.common.v1.messages.Message");
        String messageType = message.getMessageType();
        if (messageType != null) {
            builder.setPropertyString("messageType", messageType);
        }
        String body = message.getBody();
        if (body != null) {
            builder.setPropertyString("body", body);
        }
        String senderId = message.getSenderId();
        if (senderId != null) {
            builder.setPropertyString("senderId", senderId);
        }
        List recipientsIds = message.getRecipientsIds();
        int i = 0;
        if (recipientsIds != null) {
            builder.setPropertyString("recipientsIds", (String[]) recipientsIds.toArray(new String[0]));
        }
        message.getDateSent();
        message.getDateReceived();
        message.getDateRead();
        List messageAttachments = message.getMessageAttachments();
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
