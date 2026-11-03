package com.google.android.appfunctions.schema.common.v1.photos;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.android.phone.settings.search.utils.SearchContract;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.photos.$$__AppSearch__MediaItem, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__MediaItem implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(MediaItem mediaItem) {
        GenericDocument.Builder builder = new GenericDocument.Builder(mediaItem.getNamespace(), mediaItem.getId(), "com.google.android.appfunctions.schema.common.v1.photos.MediaItem");
        String type = mediaItem.getType();
        if (type != null) {
            builder.setPropertyString("type", type);
        }
        mediaItem.getDateCreated();
        String title = mediaItem.getTitle();
        if (title != null) {
            builder.setPropertyString(SearchContract.SearchResultColumn.COLUMN_TITLE, title);
        }
        Boolean starred = mediaItem.getStarred();
        if (starred != null) {
            builder.setPropertyBoolean("starred", starred.booleanValue());
        }
        String location = mediaItem.getLocation();
        if (location != null) {
            builder.setPropertyString("location", location);
        }
        String albumId = mediaItem.getAlbumId();
        if (albumId != null) {
            builder.setPropertyString("albumId", albumId);
        }
        return builder.build();
    }
}
