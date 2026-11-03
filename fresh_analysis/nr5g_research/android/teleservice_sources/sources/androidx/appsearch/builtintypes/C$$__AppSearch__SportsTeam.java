package androidx.appsearch.builtintypes;

import android.graphics.Color;
import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import androidx.appsearch.serializers.ColorAsLongSerializer;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: androidx.appsearch.builtintypes.$$__AppSearch__SportsTeam, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SportsTeam implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SportsTeam sportsTeam) {
        GenericDocument.Builder builder = new GenericDocument.Builder(sportsTeam.getNamespace(), sportsTeam.getId(), "builtin:SportsTeam");
        builder.setScore(sportsTeam.getDocumentScore());
        builder.setCreationTimestampMillis(sportsTeam.getCreationTimestampMillis());
        builder.setTtlMillis(sportsTeam.getDocumentTtlMillis());
        String name = sportsTeam.getName();
        if (name != null) {
            builder.setPropertyString("name", name);
        }
        List alternateNames = sportsTeam.getAlternateNames();
        if (alternateNames != null) {
            builder.setPropertyString("alternateNames", (String[]) alternateNames.toArray(new String[0]));
        }
        String description = sportsTeam.getDescription();
        if (description != null) {
            builder.setPropertyString("description", description);
        }
        String image = sportsTeam.getImage();
        if (image != null) {
            builder.setPropertyString("image", image);
        }
        String url = sportsTeam.getUrl();
        if (url != null) {
            builder.setPropertyString("url", url);
        }
        List potentialActions = sportsTeam.getPotentialActions();
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
        sportsTeam.getLogo();
        String sport = sportsTeam.getSport();
        if (sport != null) {
            builder.setPropertyString("sport", sport);
        }
        Color accentColor = sportsTeam.getAccentColor();
        if (accentColor != null) {
            builder.setPropertyLong("accentColor", new ColorAsLongSerializer().serialize(accentColor));
        }
        builder.setPropertyLong("wins", sportsTeam.getWins());
        builder.setPropertyLong("losses", sportsTeam.getLosses());
        builder.setPropertyLong("ties", sportsTeam.getTies());
        builder.setPropertyLong("overtimeLosses", sportsTeam.getOvertimeLosses());
        builder.setPropertyLong("overtimeWins", sportsTeam.getOvertimeWins());
        String formattedRecord = sportsTeam.getFormattedRecord();
        if (formattedRecord != null) {
            builder.setPropertyString("formattedRecord", formattedRecord);
        }
        return builder.build();
    }
}
