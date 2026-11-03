package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__SetFloatNullableField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetFloatNullableField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetFloatNullableField setFloatNullableField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setFloatNullableField.getNamespace(), setFloatNullableField.getId(), "com.google.android.appfunctions.schema.common.v1.types.SetFloatNullableField");
        Float value = setFloatNullableField.getValue();
        if (value != null) {
            builder.setPropertyDouble(a.p, value.floatValue());
        }
        return builder.build();
    }
}
