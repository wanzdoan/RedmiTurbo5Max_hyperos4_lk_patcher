package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__SetBooleanField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetBooleanField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetBooleanField setBooleanField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setBooleanField.getNamespace(), setBooleanField.getId(), "com.google.android.appfunctions.schema.common.v1.types.SetBooleanField");
        builder.setPropertyBoolean(a.p, setBooleanField.getValue().booleanValue());
        return builder.build();
    }
}
