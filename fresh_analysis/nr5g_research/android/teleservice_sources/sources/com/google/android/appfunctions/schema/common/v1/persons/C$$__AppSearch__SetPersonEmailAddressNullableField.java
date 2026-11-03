package com.google.android.appfunctions.schema.common.v1.persons;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.persons.$$__AppSearch__SetPersonEmailAddressNullableField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetPersonEmailAddressNullableField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetPersonEmailAddressNullableField setPersonEmailAddressNullableField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setPersonEmailAddressNullableField.getNamespace(), setPersonEmailAddressNullableField.getId(), "com.google.android.appfunctions.schema.common.v1.persons.SetPersonEmailAddressNullableField");
        setPersonEmailAddressNullableField.getValue();
        return builder.build();
    }
}
