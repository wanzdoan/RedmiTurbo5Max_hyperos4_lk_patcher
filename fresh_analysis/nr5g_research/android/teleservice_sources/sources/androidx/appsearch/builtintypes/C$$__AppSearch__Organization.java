package androidx.appsearch.builtintypes;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: androidx.appsearch.builtintypes.$$__AppSearch__Organization, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Organization implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Organization organization) {
        GenericDocument.Builder builder = new GenericDocument.Builder(organization.getNamespace(), organization.getId(), "builtin:Organization");
        builder.setScore(organization.getDocumentScore());
        builder.setCreationTimestampMillis(organization.getCreationTimestampMillis());
        builder.setTtlMillis(organization.getDocumentTtlMillis());
        String name = organization.getName();
        if (name != null) {
            builder.setPropertyString("name", name);
        }
        List alternateNames = organization.getAlternateNames();
        int i = 0;
        if (alternateNames != null) {
            builder.setPropertyString("alternateNames", (String[]) alternateNames.toArray(new String[0]));
        }
        String description = organization.getDescription();
        if (description != null) {
            builder.setPropertyString("description", description);
        }
        String image = organization.getImage();
        if (image != null) {
            builder.setPropertyString("image", image);
        }
        String url = organization.getUrl();
        if (url != null) {
            builder.setPropertyString("url", url);
        }
        List potentialActions = organization.getPotentialActions();
        if (potentialActions != null) {
            GenericDocument[] genericDocumentArr = new GenericDocument[potentialActions.size()];
            Iterator it = potentialActions.iterator();
            while (it.hasNext()) {
                genericDocumentArr[i] = GenericDocument.fromDocumentClass((PotentialAction) it.next());
                i++;
            }
            builder.setPropertyDocument("potentialActions", genericDocumentArr);
        }
        organization.getLogo();
        return builder.build();
    }
}
