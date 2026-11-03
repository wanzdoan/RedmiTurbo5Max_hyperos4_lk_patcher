package com.google.android.appfunctions.schema.common.v1.tasks;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.tasks.$$__AppSearch__UpdateTaskCategoryParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__UpdateTaskCategoryParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(UpdateTaskCategoryParams updateTaskCategoryParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(updateTaskCategoryParams.getNamespace(), updateTaskCategoryParams.getId(), "com.google.android.appfunctions.schema.common.v1.tasks.UpdateTaskCategoryParams");
        String taskCategoryId = updateTaskCategoryParams.getTaskCategoryId();
        if (taskCategoryId != null) {
            builder.setPropertyString("taskCategoryId", taskCategoryId);
        }
        updateTaskCategoryParams.getTitle();
        updateTaskCategoryParams.getTaskIds();
        return builder.build();
    }
}
