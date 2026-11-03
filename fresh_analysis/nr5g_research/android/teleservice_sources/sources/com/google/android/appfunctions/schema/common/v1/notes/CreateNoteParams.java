package com.google.android.appfunctions.schema.common.v1.notes;

import java.util.List;

/* JADX INFO: compiled from: Notes.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class CreateNoteParams {
    public abstract List getAttachments();

    public abstract String getContent();

    public abstract String getExternalId();

    public abstract String getFolderId();

    public abstract String getId();

    public abstract String getNamespace();

    public abstract String getTitle();
}
