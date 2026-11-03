package androidx.appsearch.builtintypes;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import java.util.List;

/* JADX INFO: renamed from: androidx.appsearch.builtintypes.$$__AppSearch__GlobalSearchApplicationInfo, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__GlobalSearchApplicationInfo implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(GlobalSearchApplicationInfo globalSearchApplicationInfo) {
        GenericDocument.Builder builder = new GenericDocument.Builder(globalSearchApplicationInfo.getNamespace(), globalSearchApplicationInfo.getId(), "builtin:GlobalSearchApplicationInfo");
        builder.setPropertyLong("applicationType", globalSearchApplicationInfo.getApplicationType());
        List schemaTypes = globalSearchApplicationInfo.getSchemaTypes();
        if (schemaTypes != null) {
            builder.setPropertyString("schemaTypes", (String[]) schemaTypes.toArray(new String[0]));
        }
        return builder.build();
    }
}
