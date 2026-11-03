package androidx.appsearch.builtintypes;

import java.time.Duration;
import java.time.Instant;

/* JADX INFO: loaded from: classes.dex */
public abstract class Event extends Thing {
    public abstract Duration getDuration();

    public abstract Instant getEndDate();

    public abstract String getLocation();

    public abstract ImageObject getLogo();

    public abstract Instant getStartDate();
}
