package com.google.android.appfunctions.schema.common.v1.persons;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.persons.$$__AppSearch__PersonPhoneNumber, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__PersonPhoneNumber implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(PersonPhoneNumber personPhoneNumber) {
        GenericDocument.Builder builder = new GenericDocument.Builder(personPhoneNumber.getNamespace(), personPhoneNumber.getId(), "com.google.android.appfunctions.schema.common.v1.persons.PersonPhoneNumber");
        String phoneNumber = personPhoneNumber.getPhoneNumber();
        if (phoneNumber != null) {
            builder.setPropertyString("phoneNumber", phoneNumber);
        }
        String type = personPhoneNumber.getType();
        if (type != null) {
            builder.setPropertyString("type", type);
        }
        String customType = personPhoneNumber.getCustomType();
        if (customType != null) {
            builder.setPropertyString("customType", customType);
        }
        return builder.build();
    }
}
