package androidx.appsearch.builtintypes;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: androidx.appsearch.builtintypes.$$__AppSearch__Thing, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Thing implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Thing thing) {
        GenericDocument.Builder builder = new GenericDocument.Builder(thing.getNamespace(), thing.getId(), "builtin:Thing");
        builder.setScore(thing.getDocumentScore());
        builder.setCreationTimestampMillis(thing.getCreationTimestampMillis());
        builder.setTtlMillis(thing.getDocumentTtlMillis());
        String name = thing.getName();
        if (name != null) {
            builder.setPropertyString("name", name);
        }
        List alternateNames = thing.getAlternateNames();
        int i = 0;
        if (alternateNames != null) {
            builder.setPropertyString("alternateNames", (String[]) alternateNames.toArray(new String[0]));
        }
        String description = thing.getDescription();
        if (description != null) {
            builder.setPropertyString("description", description);
        }
        String image = thing.getImage();
        if (image != null) {
            builder.setPropertyString("image", image);
        }
        String url = thing.getUrl();
        if (url != null) {
            builder.setPropertyString("url", url);
        }
        List potentialActions = thing.getPotentialActions();
        if (potentialActions != null) {
            GenericDocument[] genericDocumentArr = new GenericDocument[potentialActions.size()];
            Iterator it = potentialActions.iterator();
            while (it.hasNext()) {
                genericDocumentArr[i] = GenericDocument.fromDocumentClass((PotentialAction) it.next());
                i++;
            }
            builder.setPropertyDocument("potentialActions", genericDocumentArr);
        }
        return builder.build();
    }
}
