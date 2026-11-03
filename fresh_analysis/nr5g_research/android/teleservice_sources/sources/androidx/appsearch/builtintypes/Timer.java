package androidx.appsearch.builtintypes;

/* JADX INFO: loaded from: classes.dex */
public abstract class Timer extends Thing {
    public abstract long getBaseTimeMillis();

    public abstract long getBaseTimeMillisInElapsedRealtime();

    public abstract int getBootCount();

    public abstract long getDurationMillis();

    public abstract long getOriginalDurationMillis();

    public abstract long getRemainingDurationMillis();

    public abstract String getRingtone();

    public abstract long getStartTimeMillis();

    public abstract int getStatus();

    public abstract boolean shouldVibrate();
}
