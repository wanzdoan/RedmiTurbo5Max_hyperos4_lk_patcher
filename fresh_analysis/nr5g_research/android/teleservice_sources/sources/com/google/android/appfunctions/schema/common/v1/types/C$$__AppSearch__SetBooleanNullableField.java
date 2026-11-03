package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__SetBooleanNullableField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetBooleanNullableField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetBooleanNullableField setBooleanNullableField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setBooleanNullableField.getNamespace(), setBooleanNullableField.getId(), "com.google.android.appfunctions.schema.common.v1.types.SetBooleanNullableField");
        Boolean value = setBooleanNullableField.getValue();
        if (value != null) {
            builder.setPropertyBoolean(a.p, value.booleanValue());
        }
        return builder.build();
    }
}
