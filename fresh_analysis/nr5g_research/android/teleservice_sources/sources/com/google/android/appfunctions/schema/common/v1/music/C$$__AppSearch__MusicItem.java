package com.google.android.appfunctions.schema.common.v1.music;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.android.phone.settings.search.utils.SearchContract;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.music.$$__AppSearch__MusicItem, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__MusicItem implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(MusicItem musicItem) {
        GenericDocument.Builder builder = new GenericDocument.Builder(musicItem.getNamespace(), musicItem.getId(), "com.google.android.appfunctions.schema.common.v1.music.MusicItem");
        String type = musicItem.getType();
        if (type != null) {
            builder.setPropertyString("type", type);
        }
        musicItem.getDeeplink();
        String title = musicItem.getTitle();
        if (title != null) {
            builder.setPropertyString(SearchContract.SearchResultColumn.COLUMN_TITLE, title);
        }
        List artistNames = musicItem.getArtistNames();
        if (artistNames != null) {
            builder.setPropertyString("artistNames", (String[]) artistNames.toArray(new String[0]));
        }
        musicItem.getPublishDate();
        Long lengthInSeconds = musicItem.getLengthInSeconds();
        if (lengthInSeconds != null) {
            builder.setPropertyLong("lengthInSeconds", lengthInSeconds.longValue());
        }
        String description = musicItem.getDescription();
        if (description != null) {
            builder.setPropertyString("description", description);
        }
        musicItem.getThumbnail();
        List childMusicItemIds = musicItem.getChildMusicItemIds();
        if (childMusicItemIds != null) {
            builder.setPropertyString("childMusicItemIds", (String[]) childMusicItemIds.toArray(new String[0]));
        }
        return builder.build();
    }
}
