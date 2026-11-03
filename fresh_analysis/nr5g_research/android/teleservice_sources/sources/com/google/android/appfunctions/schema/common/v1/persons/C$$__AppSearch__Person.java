package com.google.android.appfunctions.schema.common.v1.persons;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.persons.$$__AppSearch__Person, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Person implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Person person) {
        GenericDocument.Builder builder = new GenericDocument.Builder(person.getNamespace(), person.getId(), "com.google.android.appfunctions.schema.common.v1.persons.Person");
        String displayName = person.getDisplayName();
        if (displayName != null) {
            builder.setPropertyString("displayName", displayName);
        }
        String givenName = person.getGivenName();
        if (givenName != null) {
            builder.setPropertyString("givenName", givenName);
        }
        String middleName = person.getMiddleName();
        if (middleName != null) {
            builder.setPropertyString("middleName", middleName);
        }
        String familyName = person.getFamilyName();
        if (familyName != null) {
            builder.setPropertyString("familyName", familyName);
        }
        person.getExternalUri();
        List phoneNumbers = person.getPhoneNumbers();
        int i = 0;
        if (phoneNumbers != null) {
            GenericDocument[] genericDocumentArr = new GenericDocument[phoneNumbers.size()];
            Iterator it = phoneNumbers.iterator();
            int i2 = 0;
            while (it.hasNext()) {
                genericDocumentArr[i2] = GenericDocument.fromDocumentClass((PersonPhoneNumber) it.next());
                i2++;
            }
            builder.setPropertyDocument("phoneNumbers", genericDocumentArr);
        }
        List emailAddresses = person.getEmailAddresses();
        if (emailAddresses != null) {
            GenericDocument[] genericDocumentArr2 = new GenericDocument[emailAddresses.size()];
            Iterator it2 = emailAddresses.iterator();
            while (it2.hasNext()) {
                genericDocumentArr2[i] = GenericDocument.fromDocumentClass((PersonEmailAddress) it2.next());
                i++;
            }
            builder.setPropertyDocument("emailAddresses", genericDocumentArr2);
        }
        return builder.build();
    }
}
