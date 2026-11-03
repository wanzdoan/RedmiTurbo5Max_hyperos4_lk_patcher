package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__SetFloatField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetFloatField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetFloatField setFloatField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setFloatField.getNamespace(), setFloatField.getId(), "com.google.android.appfunctions.schema.common.v1.types.SetFloatField");
        builder.setPropertyDouble(a.p, setFloatField.getValue().floatValue());
        return builder.build();
    }
}
