package com.google.android.appfunctions.schema.common.v1.tasks;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.android.phone.settings.search.utils.SearchContract;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.tasks.$$__AppSearch__CreateTaskParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__CreateTaskParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(CreateTaskParams createTaskParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(createTaskParams.getNamespace(), createTaskParams.getId(), "com.google.android.appfunctions.schema.common.v1.tasks.CreateTaskParams");
        String title = createTaskParams.getTitle();
        if (title != null) {
            builder.setPropertyString(SearchContract.SearchResultColumn.COLUMN_TITLE, title);
        }
        String description = createTaskParams.getDescription();
        if (description != null) {
            builder.setPropertyString("description", description);
        }
        Boolean completed = createTaskParams.getCompleted();
        if (completed != null) {
            builder.setPropertyBoolean("completed", completed.booleanValue());
        }
        createTaskParams.getDateTime();
        Boolean allDay = createTaskParams.getAllDay();
        if (allDay != null) {
            builder.setPropertyBoolean("allDay", allDay.booleanValue());
        }
        String recurrenceSchedule = createTaskParams.getRecurrenceSchedule();
        if (recurrenceSchedule != null) {
            builder.setPropertyString("recurrenceSchedule", recurrenceSchedule);
        }
        String externalId = createTaskParams.getExternalId();
        if (externalId != null) {
            builder.setPropertyString("externalId", externalId);
        }
        String taskCategoryId = createTaskParams.getTaskCategoryId();
        if (taskCategoryId != null) {
            builder.setPropertyString("taskCategoryId", taskCategoryId);
        }
        return builder.build();
    }
}
