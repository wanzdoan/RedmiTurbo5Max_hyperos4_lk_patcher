package com.google.android.appfunctions.schema.common.v1.navigation;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.navigation.$$__AppSearch__Leg, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Leg implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Leg leg) {
        GenericDocument.Builder builder = new GenericDocument.Builder(leg.getNamespace(), leg.getId(), "com.google.android.appfunctions.schema.common.v1.navigation.Leg");
        leg.getStartLocation();
        leg.getEndLocation();
        leg.getDuration();
        leg.getDistance();
        return builder.build();
    }
}
