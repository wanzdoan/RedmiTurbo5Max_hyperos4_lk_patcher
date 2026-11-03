package com.google.android.appfunctions.schema.common.v1.tasks;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.android.phone.settings.search.utils.SearchContract;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.tasks.$$__AppSearch__TaskCategory, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__TaskCategory implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(TaskCategory taskCategory) {
        GenericDocument.Builder builder = new GenericDocument.Builder(taskCategory.getNamespace(), taskCategory.getId(), "com.google.android.appfunctions.schema.common.v1.tasks.TaskCategory");
        String title = taskCategory.getTitle();
        if (title != null) {
            builder.setPropertyString(SearchContract.SearchResultColumn.COLUMN_TITLE, title);
        }
        List taskIds = taskCategory.getTaskIds();
        if (taskIds != null) {
            builder.setPropertyString("taskIds", (String[]) taskIds.toArray(new String[0]));
        }
        Boolean boolIsReadOnly = taskCategory.isReadOnly();
        if (boolIsReadOnly != null) {
            builder.setPropertyBoolean("isReadOnly", boolIsReadOnly.booleanValue());
        }
        return builder.build();
    }
}
