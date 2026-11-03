package androidx.appsearch.builtintypes;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.ot.pubsub.a.a;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: androidx.appsearch.builtintypes.$$__AppSearch__AlarmInstance, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__AlarmInstance implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(AlarmInstance alarmInstance) {
        GenericDocument.Builder builder = new GenericDocument.Builder(alarmInstance.getNamespace(), alarmInstance.getId(), "builtin:AlarmInstance");
        builder.setScore(alarmInstance.getDocumentScore());
        builder.setCreationTimestampMillis(alarmInstance.getCreationTimestampMillis());
        builder.setTtlMillis(alarmInstance.getDocumentTtlMillis());
        String name = alarmInstance.getName();
        if (name != null) {
            builder.setPropertyString("name", name);
        }
        List alternateNames = alarmInstance.getAlternateNames();
        if (alternateNames != null) {
            builder.setPropertyString("alternateNames", (String[]) alternateNames.toArray(new String[0]));
        }
        String description = alarmInstance.getDescription();
        if (description != null) {
            builder.setPropertyString("description", description);
        }
        String image = alarmInstance.getImage();
        if (image != null) {
            builder.setPropertyString("image", image);
        }
        String url = alarmInstance.getUrl();
        if (url != null) {
            builder.setPropertyString("url", url);
        }
        List potentialActions = alarmInstance.getPotentialActions();
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
        String scheduledTime = alarmInstance.getScheduledTime();
        if (scheduledTime != null) {
            builder.setPropertyString("scheduledTime", scheduledTime);
        }
        builder.setPropertyLong(a.J, alarmInstance.getStatus());
        builder.setPropertyLong("snoozeDurationMillis", alarmInstance.getSnoozeDurationMillis());
        return builder.build();
    }
}
