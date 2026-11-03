package androidx.appsearch.builtintypes;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: androidx.appsearch.builtintypes.$$__AppSearch__StopwatchLap, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__StopwatchLap implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(StopwatchLap stopwatchLap) {
        GenericDocument.Builder builder = new GenericDocument.Builder(stopwatchLap.getNamespace(), stopwatchLap.getId(), "builtin:StopwatchLap");
        builder.setScore(stopwatchLap.getDocumentScore());
        builder.setCreationTimestampMillis(stopwatchLap.getCreationTimestampMillis());
        builder.setTtlMillis(stopwatchLap.getDocumentTtlMillis());
        String name = stopwatchLap.getName();
        if (name != null) {
            builder.setPropertyString("name", name);
        }
        List alternateNames = stopwatchLap.getAlternateNames();
        if (alternateNames != null) {
            builder.setPropertyString("alternateNames", (String[]) alternateNames.toArray(new String[0]));
        }
        String description = stopwatchLap.getDescription();
        if (description != null) {
            builder.setPropertyString("description", description);
        }
        String image = stopwatchLap.getImage();
        if (image != null) {
            builder.setPropertyString("image", image);
        }
        String url = stopwatchLap.getUrl();
        if (url != null) {
            builder.setPropertyString("url", url);
        }
        List potentialActions = stopwatchLap.getPotentialActions();
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
        builder.setPropertyLong("lapNumber", stopwatchLap.getLapNumber());
        builder.setPropertyLong("lapDurationMillis", stopwatchLap.getLapDurationMillis());
        builder.setPropertyLong("accumulatedLapDurationMillis", stopwatchLap.getAccumulatedLapDurationMillis());
        return builder.build();
    }
}
