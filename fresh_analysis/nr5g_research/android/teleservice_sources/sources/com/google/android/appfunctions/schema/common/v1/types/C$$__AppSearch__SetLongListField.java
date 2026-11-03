package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__SetLongListField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetLongListField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetLongListField setLongListField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setLongListField.getNamespace(), setLongListField.getId(), "com.google.android.appfunctions.schema.common.v1.types.SetLongListField");
        List value = setLongListField.getValue();
        if (value != null) {
            long[] jArr = new long[value.size()];
            Iterator it = value.iterator();
            int i = 0;
            while (it.hasNext()) {
                jArr[i] = ((Long) it.next()).longValue();
                i++;
            }
            builder.setPropertyLong(a.p, jArr);
        }
        return builder.build();
    }
}
