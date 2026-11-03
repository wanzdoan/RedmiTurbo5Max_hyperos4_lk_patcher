package androidx.appsearch.builtintypes;

import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class Stopwatch extends Thing {
    public abstract long getAccumulatedDurationMillis();

    public abstract long getBaseTimeMillis();

    public abstract long getBaseTimeMillisInElapsedRealtime();

    public abstract int getBootCount();

    public abstract List getLaps();

    public abstract int getStatus();
}
