package androidx.appsearch.builtintypes;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: androidx.appsearch.builtintypes.$$__AppSearch__PotentialAction, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__PotentialAction implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(PotentialAction potentialAction) {
        GenericDocument.Builder builder = new GenericDocument.Builder(potentialAction.mNamespace, potentialAction.mId, "builtin:PotentialAction");
        String name = potentialAction.getName();
        if (name != null) {
            builder.setPropertyString("name", name);
        }
        String description = potentialAction.getDescription();
        if (description != null) {
            builder.setPropertyString("description", description);
        }
        String uri = potentialAction.getUri();
        if (uri != null) {
            builder.setPropertyString("uri", uri);
        }
        return builder.build();
    }
}
