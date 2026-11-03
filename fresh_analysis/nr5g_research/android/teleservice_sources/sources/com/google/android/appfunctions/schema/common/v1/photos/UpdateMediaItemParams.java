package com.google.android.appfunctions.schema.common.v1.photos;

import com.google.android.appfunctions.schema.common.v1.types.SetBooleanNullableField;
import com.google.android.appfunctions.schema.common.v1.types.SetStringField;
import com.google.android.appfunctions.schema.common.v1.types.SetStringNullableField;

/* JADX INFO: compiled from: Photos.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class UpdateMediaItemParams {
    public abstract SetStringNullableField getAlbumId();

    public abstract String getId();

    public abstract String getMediaItemId();

    public abstract String getNamespace();

    public abstract SetBooleanNullableField getStarred();

    public abstract SetStringField getTitle();
}
