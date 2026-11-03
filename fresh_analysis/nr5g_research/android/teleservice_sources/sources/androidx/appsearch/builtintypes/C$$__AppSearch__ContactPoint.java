package androidx.appsearch.builtintypes;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: androidx.appsearch.builtintypes.$$__AppSearch__ContactPoint, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__ContactPoint implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(ContactPoint contactPoint) {
        GenericDocument.Builder builder = new GenericDocument.Builder(contactPoint.getNamespace(), contactPoint.getId(), "builtin:ContactPoint");
        builder.setScore(contactPoint.getDocumentScore());
        builder.setCreationTimestampMillis(contactPoint.getCreationTimestampMillis());
        builder.setTtlMillis(contactPoint.getDocumentTtlMillis());
        String name = contactPoint.getName();
        if (name != null) {
            builder.setPropertyString("name", name);
        }
        List alternateNames = contactPoint.getAlternateNames();
        if (alternateNames != null) {
            builder.setPropertyString("alternateNames", (String[]) alternateNames.toArray(new String[0]));
        }
        String description = contactPoint.getDescription();
        if (description != null) {
            builder.setPropertyString("description", description);
        }
        String image = contactPoint.getImage();
        if (image != null) {
            builder.setPropertyString("image", image);
        }
        String url = contactPoint.getUrl();
        if (url != null) {
            builder.setPropertyString("url", url);
        }
        List potentialActions = contactPoint.getPotentialActions();
        if (potentialActions != null) {
            GenericDocument[] genericDocumentArr = new GenericDocument[potentialActions.size()];
            Iterator it = potentialActions.iterator();
            int i = 0;
            while (it.hasNext()) {
                genericDocumentArr[i] = GenericDocument.fromDocumentClass((PotentialAction) it.next());
                i++;
            }
            builder.setPropertyDocument("potentialActions", genericDocumentArr);
        }
        String label = contactPoint.getLabel();
        if (label != null) {
            builder.setPropertyString("label", label);
        }
        List addresses = contactPoint.getAddresses();
        if (addresses != null) {
            builder.setPropertyString("address", (String[]) addresses.toArray(new String[0]));
        }
        List emails = contactPoint.getEmails();
        if (emails != null) {
            builder.setPropertyString("email", (String[]) emails.toArray(new String[0]));
        }
        List telephones = contactPoint.getTelephones();
        if (telephones != null) {
            builder.setPropertyString("telephone", (String[]) telephones.toArray(new String[0]));
        }
        return builder.build();
    }
}
