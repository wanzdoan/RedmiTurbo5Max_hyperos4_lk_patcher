package androidx.appsearch.builtintypes;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: androidx.appsearch.builtintypes.$$__AppSearch__Stopwatch, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Stopwatch implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Stopwatch stopwatch) {
        GenericDocument.Builder builder = new GenericDocument.Builder(stopwatch.getNamespace(), stopwatch.getId(), "builtin:Stopwatch");
        builder.setScore(stopwatch.getDocumentScore());
        builder.setCreationTimestampMillis(stopwatch.getCreationTimestampMillis());
        builder.setTtlMillis(stopwatch.getDocumentTtlMillis());
        String name = stopwatch.getName();
        if (name != null) {
            builder.setPropertyString("name", name);
        }
        List alternateNames = stopwatch.getAlternateNames();
        int i = 0;
        if (alternateNames != null) {
            builder.setPropertyString("alternateNames", (String[]) alternateNames.toArray(new String[0]));
        }
        String description = stopwatch.getDescription();
        if (description != null) {
            builder.setPropertyString("description", description);
        }
        String image = stopwatch.getImage();
        if (image != null) {
            builder.setPropertyString("image", image);
        }
        String url = stopwatch.getUrl();
        if (url != null) {
            builder.setPropertyString("url", url);
        }
        List potentialActions = stopwatch.getPotentialActions();
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
        builder.setPropertyLong("baseTimeMillis", stopwatch.getBaseTimeMillis());
        builder.setPropertyLong("baseTimeMillisInElapsedRealtime", stopwatch.getBaseTimeMillisInElapsedRealtime());
        builder.setPropertyLong("bootCount", stopwatch.getBootCount());
        builder.setPropertyLong(a.J, stopwatch.getStatus());
        builder.setPropertyLong("accumulatedDurationMillis", stopwatch.getAccumulatedDurationMillis());
        List laps = stopwatch.getLaps();
        if (laps != null) {
            GenericDocument[] genericDocumentArr2 = new GenericDocument[laps.size()];
            Iterator it2 = laps.iterator();
            while (it2.hasNext()) {
                genericDocumentArr2[i] = GenericDocument.fromDocumentClass((StopwatchLap) it2.next());
                i++;
            }
            builder.setPropertyDocument("laps", genericDocumentArr2);
        }
        return builder.build();
    }
}
