package androidx.appsearch.serializers;

import java.time.Instant;

/* JADX INFO: loaded from: classes.dex */
public class InstantAsLongSerializer {
    public long serialize(Instant instant) {
        return instant.toEpochMilli();
    }
}
