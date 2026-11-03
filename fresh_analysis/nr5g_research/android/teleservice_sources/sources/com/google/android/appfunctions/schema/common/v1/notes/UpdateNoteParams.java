package com.google.android.appfunctions.schema.common.v1.notes;

import com.google.android.appfunctions.schema.common.v1.types.SetAttachmentListField;
import com.google.android.appfunctions.schema.common.v1.types.SetStringField;
import com.google.android.appfunctions.schema.common.v1.types.SetStringNullableField;

/* JADX INFO: compiled from: Notes.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class UpdateNoteParams {
    public abstract SetAttachmentListField getAttachments();

    public abstract SetStringNullableField getContent();

    public abstract SetStringNullableField getFolderId();

    public abstract String getId();

    public abstract String getNamespace();

    public abstract String getNoteId();

    public abstract SetStringField getTitle();
}
