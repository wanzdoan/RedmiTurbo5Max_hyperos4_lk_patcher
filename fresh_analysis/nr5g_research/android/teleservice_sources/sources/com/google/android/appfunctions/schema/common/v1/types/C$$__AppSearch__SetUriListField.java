package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__SetUriListField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetUriListField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetUriListField setUriListField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setUriListField.getNamespace(), setUriListField.getId(), "com.google.android.appfunctions.schema.common.v1.types.SetUriListField");
        List value = setUriListField.getValue();
        if (value != null) {
            GenericDocument[] genericDocumentArr = new GenericDocument[value.size()];
            Iterator it = value.iterator();
            int i = 0;
            while (it.hasNext()) {
                genericDocumentArr[i] = GenericDocument.fromDocumentClass((Uri) it.next());
                i++;
            }
            builder.setPropertyDocument(a.p, genericDocumentArr);
        }
        return builder.build();
    }
}
