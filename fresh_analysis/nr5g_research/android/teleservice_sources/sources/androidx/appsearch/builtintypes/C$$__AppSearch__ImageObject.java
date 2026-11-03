package androidx.appsearch.builtintypes;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import androidx.appsearch.builtintypes.properties.Keyword;
import com.android.phone.settings.search.utils.SearchContract;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: androidx.appsearch.builtintypes.$$__AppSearch__ImageObject, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__ImageObject implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(ImageObject imageObject) {
        GenericDocument.Builder builder = new GenericDocument.Builder(imageObject.getNamespace(), imageObject.getId(), "builtin:ImageObject");
        builder.setScore(imageObject.getDocumentScore());
        builder.setCreationTimestampMillis(imageObject.getCreationTimestampMillis());
        builder.setTtlMillis(imageObject.getDocumentTtlMillis());
        String name = imageObject.getName();
        if (name != null) {
            builder.setPropertyString("name", name);
        }
        List alternateNames = imageObject.getAlternateNames();
        int i = 0;
        if (alternateNames != null) {
            builder.setPropertyString("alternateNames", (String[]) alternateNames.toArray(new String[0]));
        }
        String description = imageObject.getDescription();
        if (description != null) {
            builder.setPropertyString("description", description);
        }
        String image = imageObject.getImage();
        if (image != null) {
            builder.setPropertyString("image", image);
        }
        String url = imageObject.getUrl();
        if (url != null) {
            builder.setPropertyString("url", url);
        }
        List potentialActions = imageObject.getPotentialActions();
        if (potentialActions != null) {
            GenericDocument[] genericDocumentArr = new GenericDocument[potentialActions.size()];
            Iterator it = potentialActions.iterator();
            int i2 = 0;
            while (it.hasNext()) {
                genericDocumentArr[i2] = GenericDocument.fromDocumentClass((PotentialAction) it.next());
                i2++;
            }
            builder.setPropertyDocument("potentialActions", genericDocumentArr);
        }
        List keywords = imageObject.getKeywords();
        if (keywords != null) {
            GenericDocument[] genericDocumentArr2 = new GenericDocument[keywords.size()];
            Iterator it2 = keywords.iterator();
            while (it2.hasNext()) {
                genericDocumentArr2[i] = GenericDocument.fromDocumentClass((Keyword) it2.next());
                i++;
            }
            builder.setPropertyDocument(SearchContract.SearchResultColumn.COLUMN_KEYWORDS, genericDocumentArr2);
        }
        String sha256 = imageObject.getSha256();
        if (sha256 != null) {
            builder.setPropertyString("sha256", sha256);
        }
        String thumbnailSha256 = imageObject.getThumbnailSha256();
        if (thumbnailSha256 != null) {
            builder.setPropertyString("thumbnailSha256", thumbnailSha256);
        }
        byte[] bytes = imageObject.getBytes();
        if (bytes != null) {
            builder.setPropertyBytes("bytes", bytes);
        }
        return builder.build();
    }
}
