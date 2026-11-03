package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__SetLongField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetLongField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetLongField setLongField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setLongField.getNamespace(), setLongField.getId(), "com.google.android.appfunctions.schema.common.v1.types.SetLongField");
        builder.setPropertyLong(a.p, setLongField.getValue().longValue());
        return builder.build();
    }
}
