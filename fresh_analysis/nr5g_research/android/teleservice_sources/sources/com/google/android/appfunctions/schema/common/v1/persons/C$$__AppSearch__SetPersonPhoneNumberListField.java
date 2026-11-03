package com.google.android.appfunctions.schema.common.v1.persons;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.persons.$$__AppSearch__SetPersonPhoneNumberListField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetPersonPhoneNumberListField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetPersonPhoneNumberListField setPersonPhoneNumberListField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setPersonPhoneNumberListField.getNamespace(), setPersonPhoneNumberListField.getId(), "com.google.android.appfunctions.schema.common.v1.persons.SetPersonPhoneNumberListField");
        List value = setPersonPhoneNumberListField.getValue();
        if (value != null) {
            GenericDocument[] genericDocumentArr = new GenericDocument[value.size()];
            Iterator it = value.iterator();
            int i = 0;
            while (it.hasNext()) {
                genericDocumentArr[i] = GenericDocument.fromDocumentClass((PersonPhoneNumber) it.next());
                i++;
            }
            builder.setPropertyDocument(a.p, genericDocumentArr);
        }
        return builder.build();
    }
}
