package com.google.android.appfunctions.schema.common.v1.video;

import com.google.android.appfunctions.schema.common.v1.types.Uri;
import java.util.List;

/* JADX INFO: compiled from: Video.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class VideoItem {
    public abstract String getAttributionText();

    public abstract List getCategories();

    public abstract long getDurationInSecond();

    public abstract String getId();

    public abstract String getNamespace();

    public abstract int getThumbnailHeight();

    public abstract Uri getThumbnailUri();

    public abstract int getThumbnailWidth();

    public abstract String getTitle();

    public abstract String getTranscriptId();
}
