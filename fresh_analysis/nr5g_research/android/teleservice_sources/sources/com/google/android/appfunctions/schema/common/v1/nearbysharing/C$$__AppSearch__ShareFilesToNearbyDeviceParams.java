package com.google.android.appfunctions.schema.common.v1.nearbysharing;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.google.android.appfunctions.schema.common.v1.types.Uri;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.nearbysharing.$$__AppSearch__ShareFilesToNearbyDeviceParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__ShareFilesToNearbyDeviceParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(ShareFilesToNearbyDeviceParams shareFilesToNearbyDeviceParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(shareFilesToNearbyDeviceParams.getNamespace(), shareFilesToNearbyDeviceParams.getId(), "com.google.android.appfunctions.schema.common.v1.nearbysharing.ShareFilesToNearbyDeviceParams");
        String targetDeviceId = shareFilesToNearbyDeviceParams.getTargetDeviceId();
        if (targetDeviceId != null) {
            builder.setPropertyString("targetDeviceId", targetDeviceId);
        }
        List files = shareFilesToNearbyDeviceParams.getFiles();
        if (files != null) {
            GenericDocument[] genericDocumentArr = new GenericDocument[files.size()];
            Iterator it = files.iterator();
            int i = 0;
            while (it.hasNext()) {
                genericDocumentArr[i] = GenericDocument.fromDocumentClass((Uri) it.next());
                i++;
            }
            builder.setPropertyDocument("files", genericDocumentArr);
        }
        return builder.build();
    }
}
