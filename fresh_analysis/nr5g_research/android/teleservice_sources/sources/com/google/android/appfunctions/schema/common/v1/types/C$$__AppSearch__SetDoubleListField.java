package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__SetDoubleListField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetDoubleListField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetDoubleListField setDoubleListField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setDoubleListField.getNamespace(), setDoubleListField.getId(), "com.google.android.appfunctions.schema.common.v1.types.SetDoubleListField");
        List value = setDoubleListField.getValue();
        if (value != null) {
            double[] dArr = new double[value.size()];
            Iterator it = value.iterator();
            int i = 0;
            while (it.hasNext()) {
                dArr[i] = ((Double) it.next()).doubleValue();
                i++;
            }
            builder.setPropertyDouble(a.p, dArr);
        }
        return builder.build();
    }
}
