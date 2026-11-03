package androidx.appsearch.builtintypes;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import androidx.appsearch.serializers.DurationAsLongSerializer;
import androidx.appsearch.serializers.InstantAsLongSerializer;
import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: androidx.appsearch.builtintypes.$$__AppSearch__Event, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Event implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Event event) {
        GenericDocument.Builder builder = new GenericDocument.Builder(event.getNamespace(), event.getId(), "builtin:Event");
        builder.setScore(event.getDocumentScore());
        builder.setCreationTimestampMillis(event.getCreationTimestampMillis());
        builder.setTtlMillis(event.getDocumentTtlMillis());
        String name = event.getName();
        if (name != null) {
            builder.setPropertyString("name", name);
        }
        List alternateNames = event.getAlternateNames();
        if (alternateNames != null) {
            builder.setPropertyString("alternateNames", (String[]) alternateNames.toArray(new String[0]));
        }
        String description = event.getDescription();
        if (description != null) {
            builder.setPropertyString("description", description);
        }
        String image = event.getImage();
        if (image != null) {
            builder.setPropertyString("image", image);
        }
        String url = event.getUrl();
        if (url != null) {
            builder.setPropertyString("url", url);
        }
        List potentialActions = event.getPotentialActions();
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
        Instant startDate = event.getStartDate();
        if (startDate != null) {
            builder.setPropertyLong("startDate", new InstantAsLongSerializer().serialize(startDate));
        }
        Instant endDate = event.getEndDate();
        if (endDate != null) {
            builder.setPropertyLong("endDate", new InstantAsLongSerializer().serialize(endDate));
        }
        Duration duration = event.getDuration();
        if (duration != null) {
            builder.setPropertyLong("duration", new DurationAsLongSerializer().serialize(duration));
        }
        String location = event.getLocation();
        if (location != null) {
            builder.setPropertyString("location", location);
        }
        event.getLogo();
        return builder.build();
    }
}
