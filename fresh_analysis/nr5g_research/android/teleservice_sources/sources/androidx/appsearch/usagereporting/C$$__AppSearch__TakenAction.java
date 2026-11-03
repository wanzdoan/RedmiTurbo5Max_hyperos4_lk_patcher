package androidx.appsearch.usagereporting;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: androidx.appsearch.usagereporting.$$__AppSearch__TakenAction, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__TakenAction implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(TakenAction takenAction) {
        GenericDocument.Builder builder = new GenericDocument.Builder(takenAction.getNamespace(), takenAction.getId(), "builtin:TakenAction");
        builder.setTtlMillis(takenAction.getDocumentTtlMillis());
        builder.setCreationTimestampMillis(takenAction.getActionTimestampMillis());
        builder.setPropertyLong("actionType", takenAction.getActionType());
        return builder.build();
    }
}
