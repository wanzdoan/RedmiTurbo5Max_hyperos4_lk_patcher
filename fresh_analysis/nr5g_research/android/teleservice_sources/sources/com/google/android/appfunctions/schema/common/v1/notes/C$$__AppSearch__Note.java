package com.google.android.appfunctions.schema.common.v1.notes;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.android.phone.settings.search.utils.SearchContract;
import com.google.android.appfunctions.schema.common.v1.types.Attachment;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.notes.$$__AppSearch__Note, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Note implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Note note) {
        GenericDocument.Builder builder = new GenericDocument.Builder(note.getNamespace(), note.getId(), "com.google.android.appfunctions.schema.common.v1.notes.Note");
        String title = note.getTitle();
        if (title != null) {
            builder.setPropertyString(SearchContract.SearchResultColumn.COLUMN_TITLE, title);
        }
        String content = note.getContent();
        if (content != null) {
            builder.setPropertyString("content", content);
        }
        List attachments = note.getAttachments();
        if (attachments != null) {
            GenericDocument[] genericDocumentArr = new GenericDocument[attachments.size()];
            Iterator it = attachments.iterator();
            int i = 0;
            while (it.hasNext()) {
                genericDocumentArr[i] = GenericDocument.fromDocumentClass((Attachment) it.next());
                i++;
            }
            builder.setPropertyDocument("attachments", genericDocumentArr);
        }
        String folderId = note.getFolderId();
        if (folderId != null) {
            builder.setPropertyString("folderId", folderId);
        }
        return builder.build();
    }
}
