package com.google.android.appfunctions.schema.common.v1.memory;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.android.phone.settings.search.utils.SearchContract;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.memory.$$__AppSearch__MemoryItem, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__MemoryItem implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(MemoryItem memoryItem) {
        GenericDocument.Builder builder = new GenericDocument.Builder(memoryItem.getNamespace(), memoryItem.getId(), "com.google.android.appfunctions.schema.common.v1.memory.MemoryItem");
        String title = memoryItem.getTitle();
        if (title != null) {
            builder.setPropertyString(SearchContract.SearchResultColumn.COLUMN_TITLE, title);
        }
        String summary = memoryItem.getSummary();
        if (summary != null) {
            builder.setPropertyString("summary", summary);
        }
        String textContent = memoryItem.getTextContent();
        if (textContent != null) {
            builder.setPropertyString("textContent", textContent);
        }
        List keywords = memoryItem.getKeywords();
        if (keywords != null) {
            builder.setPropertyString(SearchContract.SearchResultColumn.COLUMN_KEYWORDS, (String[]) keywords.toArray(new String[0]));
        }
        memoryItem.getCreatedAt();
        memoryItem.getLastUpdatedAt();
        String additionalMetadataJson = memoryItem.getAdditionalMetadataJson();
        if (additionalMetadataJson != null) {
            builder.setPropertyString("additionalMetadataJson", additionalMetadataJson);
        }
        return builder.build();
    }
}
