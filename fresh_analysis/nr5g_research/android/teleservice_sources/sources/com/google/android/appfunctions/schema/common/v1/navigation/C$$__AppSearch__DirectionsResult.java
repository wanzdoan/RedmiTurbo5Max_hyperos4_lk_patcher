package com.google.android.appfunctions.schema.common.v1.navigation;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.navigation.$$__AppSearch__DirectionsResult, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__DirectionsResult implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(DirectionsResult directionsResult) {
        GenericDocument.Builder builder = new GenericDocument.Builder(directionsResult.getNamespace(), directionsResult.getId(), "com.google.android.appfunctions.schema.common.v1.navigation.DirectionsResult");
        List routes = directionsResult.getRoutes();
        int i = 0;
        if (routes != null) {
            GenericDocument[] genericDocumentArr = new GenericDocument[routes.size()];
            Iterator it = routes.iterator();
            int i2 = 0;
            while (it.hasNext()) {
                genericDocumentArr[i2] = GenericDocument.fromDocumentClass((Route) it.next());
                i2++;
            }
            builder.setPropertyDocument("routes", genericDocumentArr);
        }
        List additionalInfo = directionsResult.getAdditionalInfo();
        if (additionalInfo != null) {
            GenericDocument[] genericDocumentArr2 = new GenericDocument[additionalInfo.size()];
            Iterator it2 = additionalInfo.iterator();
            while (it2.hasNext()) {
                genericDocumentArr2[i] = GenericDocument.fromDocumentClass((AdditionalInfo) it2.next());
                i++;
            }
            builder.setPropertyDocument("additionalInfo", genericDocumentArr2);
        }
        return builder.build();
    }
}
