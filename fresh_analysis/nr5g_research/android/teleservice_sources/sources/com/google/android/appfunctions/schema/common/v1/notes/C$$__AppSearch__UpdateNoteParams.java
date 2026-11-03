package com.google.android.appfunctions.schema.common.v1.notes;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.notes.$$__AppSearch__UpdateNoteParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__UpdateNoteParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(UpdateNoteParams updateNoteParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(updateNoteParams.getNamespace(), updateNoteParams.getId(), "com.google.android.appfunctions.schema.common.v1.notes.UpdateNoteParams");
        String noteId = updateNoteParams.getNoteId();
        if (noteId != null) {
            builder.setPropertyString("noteId", noteId);
        }
        updateNoteParams.getTitle();
        updateNoteParams.getContent();
        updateNoteParams.getAttachments();
        updateNoteParams.getFolderId();
        return builder.build();
    }
}
