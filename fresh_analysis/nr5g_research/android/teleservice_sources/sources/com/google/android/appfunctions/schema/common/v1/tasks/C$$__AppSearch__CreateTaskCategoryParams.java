package com.google.android.appfunctions.schema.common.v1.tasks;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.android.phone.settings.search.utils.SearchContract;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.tasks.$$__AppSearch__CreateTaskCategoryParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__CreateTaskCategoryParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(CreateTaskCategoryParams createTaskCategoryParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(createTaskCategoryParams.getNamespace(), createTaskCategoryParams.getId(), "com.google.android.appfunctions.schema.common.v1.tasks.CreateTaskCategoryParams");
        String title = createTaskCategoryParams.getTitle();
        if (title != null) {
            builder.setPropertyString(SearchContract.SearchResultColumn.COLUMN_TITLE, title);
        }
        List taskIds = createTaskCategoryParams.getTaskIds();
        if (taskIds != null) {
            builder.setPropertyString("taskIds", (String[]) taskIds.toArray(new String[0]));
        }
        return builder.build();
    }
}
