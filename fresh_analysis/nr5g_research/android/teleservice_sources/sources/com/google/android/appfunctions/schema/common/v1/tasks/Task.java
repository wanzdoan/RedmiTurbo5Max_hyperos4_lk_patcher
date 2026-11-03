package com.google.android.appfunctions.schema.common.v1.tasks;

import com.google.android.appfunctions.schema.common.v1.types.DateTime;

/* JADX INFO: compiled from: Tasks.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class Task {
    public abstract Boolean getAllDay();

    public abstract Boolean getCompleted();

    public abstract DateTime getDateTime();

    public abstract String getDescription();

    public abstract String getId();

    public abstract String getNamespace();

    public abstract String getRecurrenceSchedule();

    public abstract String getTaskCategoryId();

    public abstract String getTitle();
}
