package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__SetFloatListField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetFloatListField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetFloatListField setFloatListField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setFloatListField.getNamespace(), setFloatListField.getId(), "com.google.android.appfunctions.schema.common.v1.types.SetFloatListField");
        List value = setFloatListField.getValue();
        if (value != null) {
            double[] dArr = new double[value.size()];
            Iterator it = value.iterator();
            int i = 0;
            while (it.hasNext()) {
                dArr[i] = ((Float) it.next()).floatValue();
                i++;
            }
            builder.setPropertyDouble(a.p, dArr);
        }
        return builder.build();
    }
}
