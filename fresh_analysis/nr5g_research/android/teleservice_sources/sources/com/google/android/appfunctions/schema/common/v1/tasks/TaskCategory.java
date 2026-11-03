package com.google.android.appfunctions.schema.common.v1.tasks;

import java.util.List;

/* JADX INFO: compiled from: Tasks.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class TaskCategory {
    public abstract String getId();

    public abstract String getNamespace();

    public abstract List getTaskIds();

    public abstract String getTitle();

    public abstract Boolean isReadOnly();
}
