package com.google.android.appfunctions.schema.common.v1.email;

import java.util.List;

/* JADX INFO: compiled from: Email.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class CreateEmailDraftParams {
    public abstract List getBccRecipientIds();

    public abstract List getCcRecipientIds();

    public abstract String getId();

    public abstract String getMessage();

    public abstract String getNamespace();

    public abstract List getRecipientIds();

    public abstract String getSenderAccountId();

    public abstract String getSubject();
}
