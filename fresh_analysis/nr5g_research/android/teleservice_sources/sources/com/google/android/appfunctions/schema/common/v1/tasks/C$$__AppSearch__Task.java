package com.google.android.appfunctions.schema.common.v1.tasks;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.android.phone.settings.search.utils.SearchContract;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.tasks.$$__AppSearch__Task, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Task implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Task task) {
        GenericDocument.Builder builder = new GenericDocument.Builder(task.getNamespace(), task.getId(), "com.google.android.appfunctions.schema.common.v1.tasks.Task");
        String title = task.getTitle();
        if (title != null) {
            builder.setPropertyString(SearchContract.SearchResultColumn.COLUMN_TITLE, title);
        }
        String description = task.getDescription();
        if (description != null) {
            builder.setPropertyString("description", description);
        }
        Boolean completed = task.getCompleted();
        if (completed != null) {
            builder.setPropertyBoolean("completed", completed.booleanValue());
        }
        String taskCategoryId = task.getTaskCategoryId();
        if (taskCategoryId != null) {
            builder.setPropertyString("taskCategoryId", taskCategoryId);
        }
        task.getDateTime();
        Boolean allDay = task.getAllDay();
        if (allDay != null) {
            builder.setPropertyBoolean("allDay", allDay.booleanValue());
        }
        String recurrenceSchedule = task.getRecurrenceSchedule();
        if (recurrenceSchedule != null) {
            builder.setPropertyString("recurrenceSchedule", recurrenceSchedule);
        }
        return builder.build();
    }
}
