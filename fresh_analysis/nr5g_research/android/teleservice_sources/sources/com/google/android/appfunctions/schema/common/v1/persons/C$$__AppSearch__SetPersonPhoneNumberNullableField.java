package com.google.android.appfunctions.schema.common.v1.persons;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.persons.$$__AppSearch__SetPersonPhoneNumberNullableField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetPersonPhoneNumberNullableField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetPersonPhoneNumberNullableField setPersonPhoneNumberNullableField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setPersonPhoneNumberNullableField.getNamespace(), setPersonPhoneNumberNullableField.getId(), "com.google.android.appfunctions.schema.common.v1.persons.SetPersonPhoneNumberNullableField");
        setPersonPhoneNumberNullableField.getValue();
        return builder.build();
    }
}
