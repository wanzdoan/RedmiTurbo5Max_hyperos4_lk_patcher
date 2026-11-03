package androidx.appsearch.serializers;

import java.time.Duration;

/* JADX INFO: loaded from: classes.dex */
public class DurationAsLongSerializer {
    public long serialize(Duration duration) {
        return duration.toMillis();
    }
}
