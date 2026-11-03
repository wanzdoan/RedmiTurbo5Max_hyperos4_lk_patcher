package androidx.appsearch.builtintypes;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: androidx.appsearch.builtintypes.$$__AppSearch__WebPage, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__WebPage implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(WebPage webPage) {
        GenericDocument.Builder builder = new GenericDocument.Builder(webPage.getNamespace(), webPage.getId(), "builtin:WebPage");
        builder.setScore(webPage.getDocumentScore());
        builder.setCreationTimestampMillis(webPage.getCreationTimestampMillis());
        builder.setTtlMillis(webPage.getDocumentTtlMillis());
        String name = webPage.getName();
        if (name != null) {
            builder.setPropertyString("name", name);
        }
        List alternateNames = webPage.getAlternateNames();
        int i = 0;
        if (alternateNames != null) {
            builder.setPropertyString("alternateNames", (String[]) alternateNames.toArray(new String[0]));
        }
        String description = webPage.getDescription();
        if (description != null) {
            builder.setPropertyString("description", description);
        }
        String image = webPage.getImage();
        if (image != null) {
            builder.setPropertyString("image", image);
        }
        String url = webPage.getUrl();
        if (url != null) {
            builder.setPropertyString("url", url);
        }
        List potentialActions = webPage.getPotentialActions();
        if (potentialActions != null) {
            GenericDocument[] genericDocumentArr = new GenericDocument[potentialActions.size()];
            Iterator it = potentialActions.iterator();
            while (it.hasNext()) {
                genericDocumentArr[i] = GenericDocument.fromDocumentClass((PotentialAction) it.next());
                i++;
            }
            builder.setPropertyDocument("potentialActions", genericDocumentArr);
        }
        webPage.getFavicon();
        String source = webPage.getSource();
        if (source != null) {
            builder.setPropertyString("source", source);
        }
        return builder.build();
    }
}
