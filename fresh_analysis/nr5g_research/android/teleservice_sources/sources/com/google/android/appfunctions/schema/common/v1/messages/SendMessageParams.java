package com.google.android.appfunctions.schema.common.v1.messages;

import java.util.List;

/* JADX INFO: compiled from: Messages.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class SendMessageParams {
    public abstract String getBody();

    public abstract String getId();

    public abstract List getMessageAttachments();

    public abstract String getNamespace();

    public abstract List getRecipientsIds();
}
