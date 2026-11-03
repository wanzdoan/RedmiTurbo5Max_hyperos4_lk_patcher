package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__SetLongNullableField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetLongNullableField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetLongNullableField setLongNullableField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setLongNullableField.getNamespace(), setLongNullableField.getId(), "com.google.android.appfunctions.schema.common.v1.types.SetLongNullableField");
        Long value = setLongNullableField.getValue();
        if (value != null) {
            builder.setPropertyLong(a.p, value.longValue());
        }
        return builder.build();
    }
}
