package com.google.android.appfunctions.schema.common.v1.music;

import com.google.android.appfunctions.schema.common.v1.types.SetStringField;
import com.google.android.appfunctions.schema.common.v1.types.SetStringListField;

/* JADX INFO: compiled from: Music.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class UpdatePersonalPlaylistParams {
    public abstract String getId();

    public abstract SetStringListField getItemIds();

    public abstract String getNamespace();

    public abstract String getPlaylistId();

    public abstract SetStringField getTitle();
}
