package com.google.android.appfunctions.schema.common.v1.tasks;

import com.google.android.appfunctions.schema.common.v1.types.SetBooleanNullableField;
import com.google.android.appfunctions.schema.common.v1.types.SetDateTimeNullableField;
import com.google.android.appfunctions.schema.common.v1.types.SetStringField;
import com.google.android.appfunctions.schema.common.v1.types.SetStringNullableField;

/* JADX INFO: compiled from: Tasks.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class UpdateTaskParams {
    public abstract SetBooleanNullableField getAllDay();

    public abstract SetBooleanNullableField getCompleted();

    public abstract SetDateTimeNullableField getDateTime();

    public abstract SetStringNullableField getDescription();

    public abstract String getId();

    public abstract String getNamespace();

    public abstract SetStringNullableField getRecurrenceSchedule();

    public abstract String getTaskId();

    public abstract SetStringField getTitle();
}
