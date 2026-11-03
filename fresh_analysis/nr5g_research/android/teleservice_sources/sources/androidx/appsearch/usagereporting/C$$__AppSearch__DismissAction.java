package androidx.appsearch.usagereporting;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: androidx.appsearch.usagereporting.$$__AppSearch__DismissAction, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__DismissAction implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(DismissAction dismissAction) {
        GenericDocument.Builder builder = new GenericDocument.Builder(dismissAction.getNamespace(), dismissAction.getId(), "builtin:DismissAction");
        builder.setTtlMillis(dismissAction.getDocumentTtlMillis());
        builder.setCreationTimestampMillis(dismissAction.getActionTimestampMillis());
        builder.setPropertyLong("actionType", dismissAction.getActionType());
        String query = dismissAction.getQuery();
        if (query != null) {
            builder.setPropertyString("query", query);
        }
        String referencedQualifiedId = dismissAction.getReferencedQualifiedId();
        if (referencedQualifiedId != null) {
            builder.setPropertyString("referencedQualifiedId", referencedQualifiedId);
        }
        builder.setPropertyLong("resultRankInBlock", dismissAction.getResultRankInBlock());
        builder.setPropertyLong("resultRankGlobal", dismissAction.getResultRankGlobal());
        return builder.build();
    }
}
