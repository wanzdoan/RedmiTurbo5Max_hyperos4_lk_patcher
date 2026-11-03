package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__SetStringListField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetStringListField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetStringListField setStringListField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setStringListField.getNamespace(), setStringListField.getId(), "com.google.android.appfunctions.schema.common.v1.types.SetStringListField");
        List value = setStringListField.getValue();
        if (value != null) {
            builder.setPropertyString(a.p, (String[]) value.toArray(new String[0]));
        }
        return builder.build();
    }
}
