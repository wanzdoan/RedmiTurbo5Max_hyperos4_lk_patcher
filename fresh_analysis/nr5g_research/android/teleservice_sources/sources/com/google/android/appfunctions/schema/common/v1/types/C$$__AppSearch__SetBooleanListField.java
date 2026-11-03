package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__SetBooleanListField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetBooleanListField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetBooleanListField setBooleanListField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setBooleanListField.getNamespace(), setBooleanListField.getId(), "com.google.android.appfunctions.schema.common.v1.types.SetBooleanListField");
        List value = setBooleanListField.getValue();
        if (value != null) {
            boolean[] zArr = new boolean[value.size()];
            Iterator it = value.iterator();
            int i = 0;
            while (it.hasNext()) {
                zArr[i] = ((Boolean) it.next()).booleanValue();
                i++;
            }
            builder.setPropertyBoolean(a.p, zArr);
        }
        return builder.build();
    }
}
