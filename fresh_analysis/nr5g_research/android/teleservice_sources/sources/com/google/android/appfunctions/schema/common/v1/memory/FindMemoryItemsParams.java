package com.google.android.appfunctions.schema.common.v1.memory;

import com.google.android.appfunctions.schema.common.v1.types.DateTime;

/* JADX INFO: compiled from: Memory.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class FindMemoryItemsParams {
    public abstract String getId();

    public abstract Integer getMaxCount();

    public abstract String getNamespace();

    public abstract String getQuery();

    public abstract String getQueryLocale();

    public abstract DateTime getUpdatedAfter();

    public abstract DateTime getUpdatedBefore();
}
