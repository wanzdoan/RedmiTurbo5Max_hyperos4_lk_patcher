package com.google.android.appfunctions.schema.common.v1.persons;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.persons.$$__AppSearch__PersonEmailAddress, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__PersonEmailAddress implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(PersonEmailAddress personEmailAddress) {
        GenericDocument.Builder builder = new GenericDocument.Builder(personEmailAddress.getNamespace(), personEmailAddress.getId(), "com.google.android.appfunctions.schema.common.v1.persons.PersonEmailAddress");
        String emailAddress = personEmailAddress.getEmailAddress();
        if (emailAddress != null) {
            builder.setPropertyString("emailAddress", emailAddress);
        }
        String type = personEmailAddress.getType();
        if (type != null) {
            builder.setPropertyString("type", type);
        }
        String customType = personEmailAddress.getCustomType();
        if (customType != null) {
            builder.setPropertyString("customType", customType);
        }
        return builder.build();
    }
}
