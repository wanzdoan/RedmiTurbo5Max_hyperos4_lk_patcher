package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__SetTimeOfDayListField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetTimeOfDayListField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetTimeOfDayListField setTimeOfDayListField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setTimeOfDayListField.getNamespace(), setTimeOfDayListField.getId(), "com.google.android.appfunctions.schema.common.v1.types.SetTimeOfDayListField");
        List value = setTimeOfDayListField.getValue();
        if (value != null) {
            GenericDocument[] genericDocumentArr = new GenericDocument[value.size()];
            Iterator it = value.iterator();
            int i = 0;
            while (it.hasNext()) {
                genericDocumentArr[i] = GenericDocument.fromDocumentClass((TimeOfDay) it.next());
                i++;
            }
            builder.setPropertyDocument(a.p, genericDocumentArr);
        }
        return builder.build();
    }
}
