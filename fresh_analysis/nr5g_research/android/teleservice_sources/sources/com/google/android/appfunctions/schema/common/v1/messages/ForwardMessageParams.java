package com.google.android.appfunctions.schema.common.v1.messages;

import java.util.List;

/* JADX INFO: compiled from: Messages.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class ForwardMessageParams {
    public abstract String getId();

    public abstract String getMessageId();

    public abstract String getNamespace();

    public abstract List getRecipientsIds();
}
