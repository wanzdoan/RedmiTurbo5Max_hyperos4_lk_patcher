package com.google.android.appfunctions.schema.common.v1.persons;

import com.google.android.appfunctions.schema.common.v1.types.Uri;
import java.util.List;

/* JADX INFO: compiled from: Persons.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class CreatePersonParams {
    public abstract String getDisplayName();

    public abstract List getEmailAddresses();

    public abstract Uri getExternalUri();

    public abstract String getFamilyName();

    public abstract String getGivenName();

    public abstract String getId();

    public abstract String getMiddleName();

    public abstract String getNamespace();

    public abstract List getPhoneNumbers();
}
