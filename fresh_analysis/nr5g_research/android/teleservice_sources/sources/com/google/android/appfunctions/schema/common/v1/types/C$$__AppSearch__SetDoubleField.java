package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__SetDoubleField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetDoubleField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetDoubleField setDoubleField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setDoubleField.getNamespace(), setDoubleField.getId(), "com.google.android.appfunctions.schema.common.v1.types.SetDoubleField");
        builder.setPropertyDouble(a.p, setDoubleField.getValue().doubleValue());
        return builder.build();
    }
}
