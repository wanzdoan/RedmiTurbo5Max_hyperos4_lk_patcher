package com.google.android.appfunctions.schema.common.v1.persons;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.persons.$$__AppSearch__SetPersonEmailAddressField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetPersonEmailAddressField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetPersonEmailAddressField setPersonEmailAddressField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setPersonEmailAddressField.getNamespace(), setPersonEmailAddressField.getId(), "com.google.android.appfunctions.schema.common.v1.persons.SetPersonEmailAddressField");
        setPersonEmailAddressField.getValue();
        return builder.build();
    }
}
