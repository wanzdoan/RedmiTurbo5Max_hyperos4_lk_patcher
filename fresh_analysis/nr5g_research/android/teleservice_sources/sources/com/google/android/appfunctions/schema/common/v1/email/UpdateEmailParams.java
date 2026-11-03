package com.google.android.appfunctions.schema.common.v1.email;

import com.google.android.appfunctions.schema.common.v1.types.SetBooleanField;
import com.google.android.appfunctions.schema.common.v1.types.SetStringListField;
import com.google.android.appfunctions.schema.common.v1.types.SetStringNullableField;

/* JADX INFO: compiled from: Email.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class UpdateEmailParams {
    public abstract SetStringListField getBccRecipientIds();

    public abstract SetStringListField getCcRecipientIds();

    public abstract String getEmailId();

    public abstract String getId();

    public abstract SetStringNullableField getMessage();

    public abstract String getNamespace();

    public abstract SetStringListField getRecipientIds();

    public abstract SetStringNullableField getSubject();

    public abstract SetBooleanField isRead();
}
