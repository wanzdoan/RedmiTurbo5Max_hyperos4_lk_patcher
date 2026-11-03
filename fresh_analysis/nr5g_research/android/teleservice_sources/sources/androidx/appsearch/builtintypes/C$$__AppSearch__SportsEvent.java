package androidx.appsearch.builtintypes;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import androidx.appsearch.serializers.DurationAsLongSerializer;
import androidx.appsearch.serializers.InstantAsLongSerializer;
import com.ot.pubsub.a.a;
import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: androidx.appsearch.builtintypes.$$__AppSearch__SportsEvent, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SportsEvent implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SportsEvent sportsEvent) {
        GenericDocument.Builder builder = new GenericDocument.Builder(sportsEvent.getNamespace(), sportsEvent.getId(), "builtin:SportsEvent");
        builder.setScore(sportsEvent.getDocumentScore());
        builder.setCreationTimestampMillis(sportsEvent.getCreationTimestampMillis());
        builder.setTtlMillis(sportsEvent.getDocumentTtlMillis());
        String name = sportsEvent.getName();
        if (name != null) {
            builder.setPropertyString("name", name);
        }
        List alternateNames = sportsEvent.getAlternateNames();
        if (alternateNames != null) {
            builder.setPropertyString("alternateNames", (String[]) alternateNames.toArray(new String[0]));
        }
        String description = sportsEvent.getDescription();
        if (description != null) {
            builder.setPropertyString("description", description);
        }
        String image = sportsEvent.getImage();
        if (image != null) {
            builder.setPropertyString("image", image);
        }
        String url = sportsEvent.getUrl();
        if (url != null) {
            builder.setPropertyString("url", url);
        }
        List potentialActions = sportsEvent.getPotentialActions();
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
        Instant startDate = sportsEvent.getStartDate();
        if (startDate != null) {
            builder.setPropertyLong("startDate", new InstantAsLongSerializer().serialize(startDate));
        }
        Instant endDate = sportsEvent.getEndDate();
        if (endDate != null) {
            builder.setPropertyLong("endDate", new InstantAsLongSerializer().serialize(endDate));
        }
        Duration duration = sportsEvent.getDuration();
        if (duration != null) {
            builder.setPropertyLong("duration", new DurationAsLongSerializer().serialize(duration));
        }
        String location = sportsEvent.getLocation();
        if (location != null) {
            builder.setPropertyString("location", location);
        }
        sportsEvent.getLogo();
        String sport = sportsEvent.getSport();
        if (sport != null) {
            builder.setPropertyString("sport", sport);
        }
        sportsEvent.getOrganizer();
        builder.setPropertyLong("sportsEventStatus", sportsEvent.getSportsEventStatus());
        String sportsEventStatusLabel = sportsEvent.getSportsEventStatusLabel();
        if (sportsEventStatusLabel != null) {
            builder.setPropertyString("sportsEventStatusLabel", sportsEventStatusLabel);
        }
        String gameTemporalState = sportsEvent.getGameTemporalState();
        if (gameTemporalState != null) {
            builder.setPropertyString("gameTemporalState", gameTemporalState);
        }
        String notableDetail = sportsEvent.getNotableDetail();
        if (notableDetail != null) {
            builder.setPropertyString("notableDetail", notableDetail);
        }
        sportsEvent.getHomeTeam();
        String homeTeamScore = sportsEvent.getHomeTeamScore();
        if (homeTeamScore != null) {
            builder.setPropertyString("homeTeamScore", homeTeamScore);
        }
        String homeTeamAccessoryScore = sportsEvent.getHomeTeamAccessoryScore();
        if (homeTeamAccessoryScore != null) {
            builder.setPropertyString("homeTeamAccessoryScore", homeTeamAccessoryScore);
        }
        builder.setPropertyDouble("homeTeamWinProbability", sportsEvent.getHomeTeamWinProbability());
        sportsEvent.getAwayTeam();
        String awayTeamScore = sportsEvent.getAwayTeamScore();
        if (awayTeamScore != null) {
            builder.setPropertyString("awayTeamScore", awayTeamScore);
        }
        String awayTeamAccessoryScore = sportsEvent.getAwayTeamAccessoryScore();
        if (awayTeamAccessoryScore != null) {
            builder.setPropertyString("awayTeamAccessoryScore", awayTeamAccessoryScore);
        }
        builder.setPropertyDouble("awayTeamWinProbability", sportsEvent.getAwayTeamWinProbability());
        builder.setPropertyBoolean("placeHomeTeamAtStart", sportsEvent.isPlaceHomeTeamAtStart());
        builder.setPropertyLong(a.L, sportsEvent.getResult());
        return builder.build();
    }
}
