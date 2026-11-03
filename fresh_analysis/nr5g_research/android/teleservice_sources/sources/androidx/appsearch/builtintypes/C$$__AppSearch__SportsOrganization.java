package androidx.appsearch.builtintypes;

import android.graphics.Color;
import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import androidx.appsearch.serializers.ColorAsLongSerializer;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: androidx.appsearch.builtintypes.$$__AppSearch__SportsOrganization, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SportsOrganization implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SportsOrganization sportsOrganization) {
        GenericDocument.Builder builder = new GenericDocument.Builder(sportsOrganization.getNamespace(), sportsOrganization.getId(), "builtin:SportsOrganization");
        builder.setScore(sportsOrganization.getDocumentScore());
        builder.setCreationTimestampMillis(sportsOrganization.getCreationTimestampMillis());
        builder.setTtlMillis(sportsOrganization.getDocumentTtlMillis());
        String name = sportsOrganization.getName();
        if (name != null) {
            builder.setPropertyString("name", name);
        }
        List alternateNames = sportsOrganization.getAlternateNames();
        if (alternateNames != null) {
            builder.setPropertyString("alternateNames", (String[]) alternateNames.toArray(new String[0]));
        }
        String description = sportsOrganization.getDescription();
        if (description != null) {
            builder.setPropertyString("description", description);
        }
        String image = sportsOrganization.getImage();
        if (image != null) {
            builder.setPropertyString("image", image);
        }
        String url = sportsOrganization.getUrl();
        if (url != null) {
            builder.setPropertyString("url", url);
        }
        List potentialActions = sportsOrganization.getPotentialActions();
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
        sportsOrganization.getLogo();
        String sport = sportsOrganization.getSport();
        if (sport != null) {
            builder.setPropertyString("sport", sport);
        }
        Color accentColor = sportsOrganization.getAccentColor();
        if (accentColor != null) {
            builder.setPropertyLong("accentColor", new ColorAsLongSerializer().serialize(accentColor));
        }
        return builder.build();
    }
}
