package com.google.android.appfunctions.schema.common.v1.persons;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.persons.$$__AppSearch__SetPersonPhoneNumberField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetPersonPhoneNumberField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetPersonPhoneNumberField setPersonPhoneNumberField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setPersonPhoneNumberField.getNamespace(), setPersonPhoneNumberField.getId(), "com.google.android.appfunctions.schema.common.v1.persons.SetPersonPhoneNumberField");
        setPersonPhoneNumberField.getValue();
        return builder.build();
    }
}
