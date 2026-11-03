package com.google.android.appfunctions.schema.common.v1.camera;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.camera.$$__AppSearch__TakePhotoParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__TakePhotoParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(TakePhotoParams takePhotoParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(takePhotoParams.getNamespace(), takePhotoParams.getId(), "com.google.android.appfunctions.schema.common.v1.camera.TakePhotoParams");
        Integer delaySeconds = takePhotoParams.getDelaySeconds();
        if (delaySeconds != null) {
            builder.setPropertyLong("delaySeconds", delaySeconds.intValue());
        }
        return builder.build();
    }
}
