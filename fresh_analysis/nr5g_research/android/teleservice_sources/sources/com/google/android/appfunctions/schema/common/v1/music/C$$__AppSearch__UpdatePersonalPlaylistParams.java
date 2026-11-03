package com.google.android.appfunctions.schema.common.v1.music;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.music.$$__AppSearch__UpdatePersonalPlaylistParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__UpdatePersonalPlaylistParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(UpdatePersonalPlaylistParams updatePersonalPlaylistParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(updatePersonalPlaylistParams.getNamespace(), updatePersonalPlaylistParams.getId(), "com.google.android.appfunctions.schema.common.v1.music.UpdatePersonalPlaylistParams");
        String playlistId = updatePersonalPlaylistParams.getPlaylistId();
        if (playlistId != null) {
            builder.setPropertyString("playlistId", playlistId);
        }
        updatePersonalPlaylistParams.getTitle();
        updatePersonalPlaylistParams.getItemIds();
        return builder.build();
    }
}
