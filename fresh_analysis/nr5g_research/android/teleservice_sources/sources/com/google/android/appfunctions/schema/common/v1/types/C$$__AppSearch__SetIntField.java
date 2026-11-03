package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__SetIntField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetIntField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetIntField setIntField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setIntField.getNamespace(), setIntField.getId(), "com.google.android.appfunctions.schema.common.v1.types.SetIntField");
        builder.setPropertyLong(a.p, setIntField.getValue().intValue());
        return builder.build();
    }
}
