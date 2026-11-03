package com.google.android.appfunctions.schema.common.v1.tasks;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.tasks.$$__AppSearch__UpdateTaskParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__UpdateTaskParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(UpdateTaskParams updateTaskParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(updateTaskParams.getNamespace(), updateTaskParams.getId(), "com.google.android.appfunctions.schema.common.v1.tasks.UpdateTaskParams");
        String taskId = updateTaskParams.getTaskId();
        if (taskId != null) {
            builder.setPropertyString("taskId", taskId);
        }
        updateTaskParams.getTitle();
        updateTaskParams.getDescription();
        updateTaskParams.getCompleted();
        updateTaskParams.getDateTime();
        updateTaskParams.getAllDay();
        updateTaskParams.getRecurrenceSchedule();
        return builder.build();
    }
}
