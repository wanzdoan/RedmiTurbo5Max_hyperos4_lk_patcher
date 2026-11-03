package com.google.android.appfunctions.schema.common.v1.email;

import com.google.android.appfunctions.schema.common.v1.types.DateTime;
import java.util.List;

/* JADX INFO: compiled from: Email.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class FindEmailsParams {
    public abstract DateTime getEndTime();

    public abstract String getId();

    public abstract List getLabels();

    public abstract int getMaxCount();

    public abstract String getNamespace();

    public abstract String getQuery();

    public abstract Boolean getRead();

    public abstract List getRecipientIds();

    public abstract List getSenderIds();

    public abstract Boolean getSent();

    public abstract DateTime getStartTime();
}
