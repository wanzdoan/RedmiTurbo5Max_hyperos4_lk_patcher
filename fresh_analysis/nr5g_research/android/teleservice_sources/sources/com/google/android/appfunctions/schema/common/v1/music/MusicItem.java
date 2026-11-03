package com.google.android.appfunctions.schema.common.v1.music;

import com.google.android.appfunctions.schema.common.v1.types.Date;
import com.google.android.appfunctions.schema.common.v1.types.Uri;
import java.util.List;

/* JADX INFO: compiled from: Music.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class MusicItem {
    public abstract List getArtistNames();

    public abstract List getChildMusicItemIds();

    public abstract Uri getDeeplink();

    public abstract String getDescription();

    public abstract String getId();

    public abstract Long getLengthInSeconds();

    public abstract String getNamespace();

    public abstract Date getPublishDate();

    public abstract Uri getThumbnail();

    public abstract String getTitle();

    public abstract String getType();
}
