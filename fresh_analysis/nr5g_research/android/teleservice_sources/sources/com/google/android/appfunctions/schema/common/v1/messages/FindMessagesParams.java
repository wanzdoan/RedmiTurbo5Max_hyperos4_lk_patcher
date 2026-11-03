package com.google.android.appfunctions.schema.common.v1.messages;

import com.google.android.appfunctions.schema.common.v1.types.DateTime;
import java.util.List;

/* JADX INFO: compiled from: Messages.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class FindMessagesParams {
    public abstract DateTime getEndTime();

    public abstract String getId();

    public abstract int getMaxCount();

    public abstract String getNamespace();

    public abstract String getQuery();

    public abstract Boolean getRead();

    public abstract List getRecipientsIds();

    public abstract String getSenderId();

    public abstract DateTime getStartTime();
}
