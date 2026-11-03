package com.google.android.appfunctions.schema.common.v1.navigation;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.navigation.$$__AppSearch__AdditionalInfo, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__AdditionalInfo implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(AdditionalInfo additionalInfo) {
        GenericDocument.Builder builder = new GenericDocument.Builder(additionalInfo.getNamespace(), additionalInfo.getId(), "com.google.android.appfunctions.schema.common.v1.navigation.AdditionalInfo");
        String key = additionalInfo.getKey();
        if (key != null) {
            builder.setPropertyString("key", key);
        }
        String value = additionalInfo.getValue();
        if (value != null) {
            builder.setPropertyString(a.p, value);
        }
        return builder.build();
    }
}
