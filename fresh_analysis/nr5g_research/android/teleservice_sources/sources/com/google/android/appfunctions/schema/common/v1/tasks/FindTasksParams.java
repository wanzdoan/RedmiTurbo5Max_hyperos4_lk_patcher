package com.google.android.appfunctions.schema.common.v1.tasks;

import com.google.android.appfunctions.schema.common.v1.types.DateTime;

/* JADX INFO: compiled from: Tasks.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class FindTasksParams {
    public abstract Boolean getCompleted();

    public abstract DateTime getEndTime();

    public abstract String getId();

    public abstract int getMaxCount();

    public abstract String getNamespace();

    public abstract String getQuery();

    public abstract DateTime getStartTime();

    public abstract String getTaskCategoryId();
}
