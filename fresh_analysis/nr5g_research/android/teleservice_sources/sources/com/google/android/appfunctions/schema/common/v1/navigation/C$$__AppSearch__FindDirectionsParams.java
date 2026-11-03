package com.google.android.appfunctions.schema.common.v1.navigation;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.navigation.$$__AppSearch__FindDirectionsParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__FindDirectionsParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(FindDirectionsParams findDirectionsParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(findDirectionsParams.getNamespace(), findDirectionsParams.getId(), "com.google.android.appfunctions.schema.common.v1.navigation.FindDirectionsParams");
        findDirectionsParams.getOrigin();
        findDirectionsParams.getDestination();
        List waypoints = findDirectionsParams.getWaypoints();
        if (waypoints != null) {
            GenericDocument[] genericDocumentArr = new GenericDocument[waypoints.size()];
            Iterator it = waypoints.iterator();
            int i = 0;
            while (it.hasNext()) {
                genericDocumentArr[i] = GenericDocument.fromDocumentClass((LocationQuery) it.next());
                i++;
            }
            builder.setPropertyDocument("waypoints", genericDocumentArr);
        }
        String transportationType = findDirectionsParams.getTransportationType();
        if (transportationType != null) {
            builder.setPropertyString("transportationType", transportationType);
        }
        List avoidTypes = findDirectionsParams.getAvoidTypes();
        if (avoidTypes != null) {
            builder.setPropertyString("avoidTypes", (String[]) avoidTypes.toArray(new String[0]));
        }
        return builder.build();
    }
}
