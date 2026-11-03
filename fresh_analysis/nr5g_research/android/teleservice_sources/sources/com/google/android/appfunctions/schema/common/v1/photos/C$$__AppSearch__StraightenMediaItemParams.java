package com.google.android.appfunctions.schema.common.v1.photos;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.photos.$$__AppSearch__StraightenMediaItemParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__StraightenMediaItemParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(StraightenMediaItemParams straightenMediaItemParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(straightenMediaItemParams.getNamespace(), straightenMediaItemParams.getId(), "com.google.android.appfunctions.schema.common.v1.photos.StraightenMediaItemParams");
        String mediaItemId = straightenMediaItemParams.getMediaItemId();
        if (mediaItemId != null) {
            builder.setPropertyString("mediaItemId", mediaItemId);
        }
        straightenMediaItemParams.getMediaItemUri();
        Boolean autoStraighten = straightenMediaItemParams.getAutoStraighten();
        if (autoStraighten != null) {
            builder.setPropertyBoolean("autoStraighten", autoStraighten.booleanValue());
        }
        Double straightenDegrees = straightenMediaItemParams.getStraightenDegrees();
        if (straightenDegrees != null) {
            builder.setPropertyDouble("straightenDegrees", straightenDegrees.doubleValue());
        }
        return builder.build();
    }
}
