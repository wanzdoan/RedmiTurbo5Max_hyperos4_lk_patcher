package androidx.appsearch.builtintypes.properties;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: androidx.appsearch.builtintypes.properties.$$__AppSearch__Keyword, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Keyword implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Keyword keyword) {
        GenericDocument.Builder builder = new GenericDocument.Builder(keyword.mNamespace, keyword.mId, "Keyword");
        String str = keyword.mAsText;
        if (str != null) {
            builder.setPropertyString("asText", str);
        }
        return builder.build();
    }
}
