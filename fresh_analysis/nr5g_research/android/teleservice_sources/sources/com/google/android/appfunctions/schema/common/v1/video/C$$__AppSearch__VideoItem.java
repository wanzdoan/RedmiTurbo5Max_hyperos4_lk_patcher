package com.google.android.appfunctions.schema.common.v1.video;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.android.phone.settings.search.utils.SearchContract;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.video.$$__AppSearch__VideoItem, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__VideoItem implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(VideoItem videoItem) {
        GenericDocument.Builder builder = new GenericDocument.Builder(videoItem.getNamespace(), videoItem.getId(), "com.google.android.appfunctions.schema.common.v1.video.VideoItem");
        String title = videoItem.getTitle();
        if (title != null) {
            builder.setPropertyString(SearchContract.SearchResultColumn.COLUMN_TITLE, title);
        }
        builder.setPropertyLong("durationInSecond", videoItem.getDurationInSecond());
        videoItem.getThumbnailUri();
        builder.setPropertyLong("thumbnailWidth", videoItem.getThumbnailWidth());
        builder.setPropertyLong("thumbnailHeight", videoItem.getThumbnailHeight());
        List categories = videoItem.getCategories();
        if (categories != null) {
            builder.setPropertyString("categories", (String[]) categories.toArray(new String[0]));
        }
        String attributionText = videoItem.getAttributionText();
        if (attributionText != null) {
            builder.setPropertyString("attributionText", attributionText);
        }
        String transcriptId = videoItem.getTranscriptId();
        if (transcriptId != null) {
            builder.setPropertyString("transcriptId", transcriptId);
        }
        return builder.build();
    }
}
