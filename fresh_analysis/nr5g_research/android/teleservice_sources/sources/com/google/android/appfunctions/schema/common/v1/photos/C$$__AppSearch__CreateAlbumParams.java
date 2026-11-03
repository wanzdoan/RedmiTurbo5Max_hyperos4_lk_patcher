package com.google.android.appfunctions.schema.common.v1.photos;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.android.phone.settings.search.utils.SearchContract;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.photos.$$__AppSearch__CreateAlbumParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__CreateAlbumParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(CreateAlbumParams createAlbumParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(createAlbumParams.getNamespace(), createAlbumParams.getId(), "com.google.android.appfunctions.schema.common.v1.photos.CreateAlbumParams");
        String title = createAlbumParams.getTitle();
        if (title != null) {
            builder.setPropertyString(SearchContract.SearchResultColumn.COLUMN_TITLE, title);
        }
        return builder.build();
    }
}
