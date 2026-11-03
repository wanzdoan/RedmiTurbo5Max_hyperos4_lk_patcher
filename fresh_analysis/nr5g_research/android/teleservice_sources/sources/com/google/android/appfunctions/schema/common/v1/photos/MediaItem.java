package com.google.android.appfunctions.schema.common.v1.photos;

import com.google.android.appfunctions.schema.common.v1.types.DateTime;

/* JADX INFO: compiled from: Photos.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class MediaItem {
    public abstract String getAlbumId();

    public abstract DateTime getDateCreated();

    public abstract String getId();

    public abstract String getLocation();

    public abstract String getNamespace();

    public abstract Boolean getStarred();

    public abstract String getTitle();

    public abstract String getType();
}
