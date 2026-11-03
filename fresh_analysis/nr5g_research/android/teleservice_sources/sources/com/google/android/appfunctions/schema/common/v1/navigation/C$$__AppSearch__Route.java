package com.google.android.appfunctions.schema.common.v1.navigation;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.navigation.$$__AppSearch__Route, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Route implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Route route) {
        GenericDocument.Builder builder = new GenericDocument.Builder(route.getNamespace(), route.getId(), "com.google.android.appfunctions.schema.common.v1.navigation.Route");
        String routeToken = route.getRouteToken();
        if (routeToken != null) {
            builder.setPropertyString("routeToken", routeToken);
        }
        List legs = route.getLegs();
        int i = 0;
        if (legs != null) {
            GenericDocument[] genericDocumentArr = new GenericDocument[legs.size()];
            Iterator it = legs.iterator();
            int i2 = 0;
            while (it.hasNext()) {
                genericDocumentArr[i2] = GenericDocument.fromDocumentClass((Leg) it.next());
                i2++;
            }
            builder.setPropertyDocument("legs", genericDocumentArr);
        }
        route.getDuration();
        route.getDistance();
        route.getFare();
        String routeLabel = route.getRouteLabel();
        if (routeLabel != null) {
            builder.setPropertyString("routeLabel", routeLabel);
        }
        String summary = route.getSummary();
        if (summary != null) {
            builder.setPropertyString("summary", summary);
        }
        String transportationType = route.getTransportationType();
        if (transportationType != null) {
            builder.setPropertyString("transportationType", transportationType);
        }
        List additionalInfo = route.getAdditionalInfo();
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
