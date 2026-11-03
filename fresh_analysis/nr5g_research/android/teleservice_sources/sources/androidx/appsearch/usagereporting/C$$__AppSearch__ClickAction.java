package androidx.appsearch.usagereporting;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: androidx.appsearch.usagereporting.$$__AppSearch__ClickAction, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__ClickAction implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(ClickAction clickAction) {
        GenericDocument.Builder builder = new GenericDocument.Builder(clickAction.getNamespace(), clickAction.getId(), "builtin:ClickAction");
        builder.setTtlMillis(clickAction.getDocumentTtlMillis());
        builder.setCreationTimestampMillis(clickAction.getActionTimestampMillis());
        builder.setPropertyLong("actionType", clickAction.getActionType());
        String query = clickAction.getQuery();
        if (query != null) {
            builder.setPropertyString("query", query);
        }
        String referencedQualifiedId = clickAction.getReferencedQualifiedId();
        if (referencedQualifiedId != null) {
            builder.setPropertyString("referencedQualifiedId", referencedQualifiedId);
        }
        builder.setPropertyLong("resultRankInBlock", clickAction.getResultRankInBlock());
        builder.setPropertyLong("resultRankGlobal", clickAction.getResultRankGlobal());
        builder.setPropertyLong("timeStayOnResultMillis", clickAction.getTimeStayOnResultMillis());
        return builder.build();
    }
}
