package com.google.android.appfunctions.schema.common.v1.clock;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.clock.$$__AppSearch__SetDayPatternListField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetDayPatternListField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetDayPatternListField setDayPatternListField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setDayPatternListField.getNamespace(), setDayPatternListField.getId(), "com.google.android.appfunctions.schema.common.v1.clock.SetDayPatternListField");
        List value = setDayPatternListField.getValue();
        if (value != null) {
            GenericDocument[] genericDocumentArr = new GenericDocument[value.size()];
            Iterator it = value.iterator();
            int i = 0;
            while (it.hasNext()) {
                genericDocumentArr[i] = GenericDocument.fromDocumentClass((DayPattern) it.next());
                i++;
            }
            builder.setPropertyDocument(a.p, genericDocumentArr);
        }
        return builder.build();
    }
}
