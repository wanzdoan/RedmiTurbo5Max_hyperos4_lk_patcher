package androidx.appsearch.builtintypes;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: androidx.appsearch.builtintypes.$$__AppSearch__Timer, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Timer implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Timer timer) {
        GenericDocument.Builder builder = new GenericDocument.Builder(timer.getNamespace(), timer.getId(), "builtin:Timer");
        builder.setScore(timer.getDocumentScore());
        builder.setCreationTimestampMillis(timer.getCreationTimestampMillis());
        builder.setTtlMillis(timer.getDocumentTtlMillis());
        String name = timer.getName();
        if (name != null) {
            builder.setPropertyString("name", name);
        }
        List alternateNames = timer.getAlternateNames();
        if (alternateNames != null) {
            builder.setPropertyString("alternateNames", (String[]) alternateNames.toArray(new String[0]));
        }
        String description = timer.getDescription();
        if (description != null) {
            builder.setPropertyString("description", description);
        }
        String image = timer.getImage();
        if (image != null) {
            builder.setPropertyString("image", image);
        }
        String url = timer.getUrl();
        if (url != null) {
            builder.setPropertyString("url", url);
        }
        List potentialActions = timer.getPotentialActions();
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
        builder.setPropertyLong("durationMillis", timer.getDurationMillis());
        builder.setPropertyLong("originalDurationMillis", timer.getOriginalDurationMillis());
        builder.setPropertyLong("startTimeMillis", timer.getStartTimeMillis());
        builder.setPropertyLong("baseTimeMillis", timer.getBaseTimeMillis());
        builder.setPropertyLong("baseTimeMillisInElapsedRealtime", timer.getBaseTimeMillisInElapsedRealtime());
        builder.setPropertyLong("bootCount", timer.getBootCount());
        builder.setPropertyLong("remainingDurationMillis", timer.getRemainingDurationMillis());
        String ringtone = timer.getRingtone();
        if (ringtone != null) {
            builder.setPropertyString("ringtone", ringtone);
        }
        builder.setPropertyLong(a.J, timer.getStatus());
        builder.setPropertyBoolean("shouldVibrate", timer.shouldVibrate());
        return builder.build();
    }
}
