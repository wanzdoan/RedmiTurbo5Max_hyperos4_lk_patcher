package com.google.android.appfunctions.schema.common.v1.email;

import com.google.android.appfunctions.schema.common.v1.types.DateTime;
import java.util.List;

/* JADX INFO: compiled from: Email.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class Email {
    public abstract List getBccRecipientIds();

    public abstract List getCcRecipientIds();

    public abstract DateTime getDateRead();

    public abstract DateTime getDateReceived();

    public abstract DateTime getDateSent();

    public abstract String getId();

    public abstract String getMessage();

    public abstract String getNamespace();

    public abstract List getRecipientIds();

    public abstract String getSenderId();

    public abstract String getSubject();

    public abstract Boolean isDraft();
}
