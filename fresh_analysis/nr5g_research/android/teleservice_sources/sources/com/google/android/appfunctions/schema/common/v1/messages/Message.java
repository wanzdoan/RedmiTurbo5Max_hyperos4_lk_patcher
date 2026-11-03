package com.google.android.appfunctions.schema.common.v1.messages;

import com.google.android.appfunctions.schema.common.v1.types.DateTime;
import java.util.List;

/* JADX INFO: compiled from: Messages.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class Message {
    public abstract String getBody();

    public abstract DateTime getDateRead();

    public abstract DateTime getDateReceived();

    public abstract DateTime getDateSent();

    public abstract String getId();

    public abstract List getMessageAttachments();

    public abstract String getMessageType();

    public abstract String getNamespace();

    public abstract List getRecipientsIds();

    public abstract String getSenderId();
}
