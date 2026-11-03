package androidx.appsearch.usagereporting;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: androidx.appsearch.usagereporting.$$__AppSearch__ImpressionAction, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__ImpressionAction implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(ImpressionAction impressionAction) {
        GenericDocument.Builder builder = new GenericDocument.Builder(impressionAction.getNamespace(), impressionAction.getId(), "builtin:ImpressionAction");
        builder.setTtlMillis(impressionAction.getDocumentTtlMillis());
        builder.setCreationTimestampMillis(impressionAction.getActionTimestampMillis());
        builder.setPropertyLong("actionType", impressionAction.getActionType());
        String query = impressionAction.getQuery();
        if (query != null) {
            builder.setPropertyString("query", query);
        }
        String referencedQualifiedId = impressionAction.getReferencedQualifiedId();
        if (referencedQualifiedId != null) {
            builder.setPropertyString("referencedQualifiedId", referencedQualifiedId);
        }
        builder.setPropertyLong("resultRankInBlock", impressionAction.getResultRankInBlock());
        builder.setPropertyLong("resultRankGlobal", impressionAction.getResultRankGlobal());
        return builder.build();
    }
}
