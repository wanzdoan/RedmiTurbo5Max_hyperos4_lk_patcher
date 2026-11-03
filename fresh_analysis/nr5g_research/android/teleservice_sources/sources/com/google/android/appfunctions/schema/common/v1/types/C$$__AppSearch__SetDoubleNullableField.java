package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__SetDoubleNullableField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetDoubleNullableField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetDoubleNullableField setDoubleNullableField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setDoubleNullableField.getNamespace(), setDoubleNullableField.getId(), "com.google.android.appfunctions.schema.common.v1.types.SetDoubleNullableField");
        Double value = setDoubleNullableField.getValue();
        if (value != null) {
            builder.setPropertyDouble(a.p, value.doubleValue());
        }
        return builder.build();
    }
}
