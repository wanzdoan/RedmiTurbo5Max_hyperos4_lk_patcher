package com.google.android.appfunctions.schema.common.v1.messages;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.google.android.appfunctions.schema.common.v1.types.Attachment;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.messages.$$__AppSearch__ReplyMessageParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__ReplyMessageParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(ReplyMessageParams replyMessageParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(replyMessageParams.getNamespace(), replyMessageParams.getId(), "com.google.android.appfunctions.schema.common.v1.messages.ReplyMessageParams");
        String originalMessageId = replyMessageParams.getOriginalMessageId();
        if (originalMessageId != null) {
            builder.setPropertyString("originalMessageId", originalMessageId);
        }
        String body = replyMessageParams.getBody();
        if (body != null) {
            builder.setPropertyString("body", body);
        }
        List messageAttachments = replyMessageParams.getMessageAttachments();
        if (messageAttachments != null) {
            GenericDocument[] genericDocumentArr = new GenericDocument[messageAttachments.size()];
            Iterator it = messageAttachments.iterator();
            int i = 0;
            while (it.hasNext()) {
                genericDocumentArr[i] = GenericDocument.fromDocumentClass((Attachment) it.next());
                i++;
            }
            builder.setPropertyDocument("messageAttachments", genericDocumentArr);
        }
        return builder.build();
    }
}
