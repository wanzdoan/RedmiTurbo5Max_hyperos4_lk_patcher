package com.google.android.appfunctions.schema.common.v1.persons;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.persons.$$__AppSearch__UpdatePersonParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__UpdatePersonParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(UpdatePersonParams updatePersonParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(updatePersonParams.getNamespace(), updatePersonParams.getId(), "com.google.android.appfunctions.schema.common.v1.persons.UpdatePersonParams");
        String personId = updatePersonParams.getPersonId();
        if (personId != null) {
            builder.setPropertyString("personId", personId);
        }
        updatePersonParams.getDisplayName();
        updatePersonParams.getGivenName();
        updatePersonParams.getMiddleName();
        updatePersonParams.getFamilyName();
        updatePersonParams.getExternalUri();
        updatePersonParams.getPhoneNumbers();
        updatePersonParams.getEmailAddresses();
        return builder.build();
    }
}
