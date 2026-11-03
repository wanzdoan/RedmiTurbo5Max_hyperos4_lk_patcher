package com.google.android.appfunctions.schema.common.v1.navigation;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.navigation.$$__AppSearch__Fare, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Fare implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Fare fare) {
        GenericDocument.Builder builder = new GenericDocument.Builder(fare.getNamespace(), fare.getId(), "com.google.android.appfunctions.schema.common.v1.navigation.Fare");
        String currency = fare.getCurrency();
        if (currency != null) {
            builder.setPropertyString("currency", currency);
        }
        builder.setPropertyDouble("amount", fare.getAmount());
        return builder.build();
    }
}
