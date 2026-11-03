package com.google.android.appfunctions.schema.common.v1.photos;

import com.google.android.appfunctions.schema.common.v1.types.Uri;

/* JADX INFO: compiled from: Photos.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class StraightenMediaItemParams {
    public abstract Boolean getAutoStraighten();

    public abstract String getId();

    public abstract String getMediaItemId();

    public abstract Uri getMediaItemUri();

    public abstract String getNamespace();

    public abstract Double getStraightenDegrees();
}
