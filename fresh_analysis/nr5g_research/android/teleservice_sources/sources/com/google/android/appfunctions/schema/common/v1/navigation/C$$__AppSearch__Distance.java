package com.google.android.appfunctions.schema.common.v1.navigation;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.navigation.$$__AppSearch__Distance, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Distance implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Distance distance) {
        GenericDocument.Builder builder = new GenericDocument.Builder(distance.getNamespace(), distance.getId(), "com.google.android.appfunctions.schema.common.v1.navigation.Distance");
        Long distanceMeters = distance.getDistanceMeters();
        if (distanceMeters != null) {
            builder.setPropertyLong("distanceMeters", distanceMeters.longValue());
        }
        String distanceDisplayString = distance.getDistanceDisplayString();
        if (distanceDisplayString != null) {
            builder.setPropertyString("distanceDisplayString", distanceDisplayString);
        }
        return builder.build();
    }
}
