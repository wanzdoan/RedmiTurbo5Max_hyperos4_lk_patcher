package com.google.android.appfunctions.schema.common.v1.music;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.android.phone.settings.search.utils.SearchContract;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.music.$$__AppSearch__CreatePersonalPlaylistParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__CreatePersonalPlaylistParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(CreatePersonalPlaylistParams createPersonalPlaylistParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(createPersonalPlaylistParams.getNamespace(), createPersonalPlaylistParams.getId(), "com.google.android.appfunctions.schema.common.v1.music.CreatePersonalPlaylistParams");
        String title = createPersonalPlaylistParams.getTitle();
        if (title != null) {
            builder.setPropertyString(SearchContract.SearchResultColumn.COLUMN_TITLE, title);
        }
        List itemIds = createPersonalPlaylistParams.getItemIds();
        if (itemIds != null) {
            builder.setPropertyString("itemIds", (String[]) itemIds.toArray(new String[0]));
        }
        return builder.build();
    }
}
