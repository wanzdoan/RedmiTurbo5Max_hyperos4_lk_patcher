package androidx.appsearch.usagereporting;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: androidx.appsearch.usagereporting.$$__AppSearch__SearchAction, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SearchAction implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SearchAction searchAction) {
        GenericDocument.Builder builder = new GenericDocument.Builder(searchAction.getNamespace(), searchAction.getId(), "builtin:SearchAction");
        builder.setTtlMillis(searchAction.getDocumentTtlMillis());
        builder.setCreationTimestampMillis(searchAction.getActionTimestampMillis());
        builder.setPropertyLong("actionType", searchAction.getActionType());
        String query = searchAction.getQuery();
        if (query != null) {
            builder.setPropertyString("query", query);
        }
        builder.setPropertyLong("fetchedResultCount", searchAction.getFetchedResultCount());
        return builder.build();
    }
}
