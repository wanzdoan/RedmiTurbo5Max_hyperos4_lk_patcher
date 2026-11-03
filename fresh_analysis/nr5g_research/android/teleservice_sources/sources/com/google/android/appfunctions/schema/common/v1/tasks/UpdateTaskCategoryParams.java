package com.google.android.appfunctions.schema.common.v1.tasks;

import com.google.android.appfunctions.schema.common.v1.types.SetStringField;
import com.google.android.appfunctions.schema.common.v1.types.SetStringListField;

/* JADX INFO: compiled from: Tasks.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class UpdateTaskCategoryParams {
    public abstract String getId();

    public abstract String getNamespace();

    public abstract String getTaskCategoryId();

    public abstract SetStringListField getTaskIds();

    public abstract SetStringField getTitle();
}
