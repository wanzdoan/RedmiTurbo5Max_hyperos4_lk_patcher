package com.google.android.appfunctions.schema.common.v1.photos;

import com.google.android.appfunctions.schema.common.v1.types.SetStringField;

/* JADX INFO: compiled from: Photos.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class UpdateAlbumParams {
    public abstract String getAlbumId();

    public abstract String getId();

    public abstract String getNamespace();

    public abstract SetStringField getTitle();
}
