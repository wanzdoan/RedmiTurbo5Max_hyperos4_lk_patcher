package androidx.appsearch.builtintypes;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: androidx.appsearch.builtintypes.$$__AppSearch__Alarm, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Alarm implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Alarm alarm) {
        GenericDocument.Builder builder = new GenericDocument.Builder(alarm.getNamespace(), alarm.getId(), "builtin:Alarm");
        builder.setScore(alarm.getDocumentScore());
        builder.setCreationTimestampMillis(alarm.getCreationTimestampMillis());
        builder.setTtlMillis(alarm.getDocumentTtlMillis());
        String name = alarm.getName();
        if (name != null) {
            builder.setPropertyString("name", name);
        }
        List alternateNames = alarm.getAlternateNames();
        if (alternateNames != null) {
            builder.setPropertyString("alternateNames", (String[]) alternateNames.toArray(new String[0]));
        }
        String description = alarm.getDescription();
        if (description != null) {
            builder.setPropertyString("description", description);
        }
        String image = alarm.getImage();
        if (image != null) {
            builder.setPropertyString("image", image);
        }
        String url = alarm.getUrl();
        if (url != null) {
            builder.setPropertyString("url", url);
        }
        List potentialActions = alarm.getPotentialActions();
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
        builder.setPropertyBoolean("enabled", alarm.isEnabled());
        int[] daysOfWeek = alarm.getDaysOfWeek();
        if (daysOfWeek != null) {
            long[] jArr = new long[daysOfWeek.length];
            for (int i2 = 0; i2 < daysOfWeek.length; i2++) {
                jArr[i2] = daysOfWeek[i2];
            }
            builder.setPropertyLong("daysOfWeek", jArr);
        }
        builder.setPropertyLong("hour", alarm.getHour());
        builder.setPropertyLong("minute", alarm.getMinute());
        String blackoutPeriodStartDate = alarm.getBlackoutPeriodStartDate();
        if (blackoutPeriodStartDate != null) {
            builder.setPropertyString("blackoutPeriodStartDate", blackoutPeriodStartDate);
        }
        String blackoutPeriodEndDate = alarm.getBlackoutPeriodEndDate();
        if (blackoutPeriodEndDate != null) {
            builder.setPropertyString("blackoutPeriodEndDate", blackoutPeriodEndDate);
        }
        String ringtone = alarm.getRingtone();
        if (ringtone != null) {
            builder.setPropertyString("ringtone", ringtone);
        }
        builder.setPropertyBoolean("shouldVibrate", alarm.shouldVibrate());
        alarm.getPreviousInstance();
        alarm.getNextInstance();
        builder.setPropertyLong("computingDevice", alarm.getOriginatingDevice());
        return builder.build();
    }
}
