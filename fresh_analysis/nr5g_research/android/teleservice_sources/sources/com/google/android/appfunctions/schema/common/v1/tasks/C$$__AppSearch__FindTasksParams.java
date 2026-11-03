package com.google.android.appfunctions.schema.common.v1.tasks;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.tasks.$$__AppSearch__FindTasksParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__FindTasksParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(FindTasksParams findTasksParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(findTasksParams.getNamespace(), findTasksParams.getId(), "com.google.android.appfunctions.schema.common.v1.tasks.FindTasksParams");
        String query = findTasksParams.getQuery();
        if (query != null) {
            builder.setPropertyString("query", query);
        }
        Boolean completed = findTasksParams.getCompleted();
        if (completed != null) {
            builder.setPropertyBoolean("completed", completed.booleanValue());
        }
        findTasksParams.getStartTime();
        findTasksParams.getEndTime();
        String taskCategoryId = findTasksParams.getTaskCategoryId();
        if (taskCategoryId != null) {
            builder.setPropertyString("taskCategoryId", taskCategoryId);
        }
        builder.setPropertyLong("maxCount", findTasksParams.getMaxCount());
        return builder.build();
    }
}
