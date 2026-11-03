package com.google.android.appfunctions.schema.common.v1.persons;

import com.google.android.appfunctions.schema.common.v1.types.SetStringField;
import com.google.android.appfunctions.schema.common.v1.types.SetStringNullableField;
import com.google.android.appfunctions.schema.common.v1.types.SetUriNullableField;

/* JADX INFO: compiled from: Persons.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class UpdatePersonParams {
    public abstract SetStringField getDisplayName();

    public abstract SetPersonEmailAddressListField getEmailAddresses();

    public abstract SetUriNullableField getExternalUri();

    public abstract SetStringNullableField getFamilyName();

    public abstract SetStringNullableField getGivenName();

    public abstract String getId();

    public abstract SetStringNullableField getMiddleName();

    public abstract String getNamespace();

    public abstract String getPersonId();

    public abstract SetPersonPhoneNumberListField getPhoneNumbers();
}
