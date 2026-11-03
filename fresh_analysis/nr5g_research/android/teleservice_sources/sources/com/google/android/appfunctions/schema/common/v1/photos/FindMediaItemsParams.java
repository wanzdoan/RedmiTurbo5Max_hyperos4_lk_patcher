package com.google.android.appfunctions.schema.common.v1.photos;

import com.google.android.appfunctions.schema.common.v1.types.DateTime;

/* JADX INFO: compiled from: Photos.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class FindMediaItemsParams {
    public abstract String getAlbumId();

    public abstract DateTime getEndTime();

    public abstract String getId();

    public abstract String getLocation();

    public abstract int getMaxCount();

    public abstract String getNamespace();

    public abstract String getQuery();

    public abstract Boolean getStarred();

    public abstract DateTime getStartTime();
}
