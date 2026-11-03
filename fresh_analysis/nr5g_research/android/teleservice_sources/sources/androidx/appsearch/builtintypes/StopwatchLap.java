package androidx.appsearch.builtintypes;

/* JADX INFO: loaded from: classes.dex */
public abstract class StopwatchLap extends Thing {
    public abstract long getAccumulatedLapDurationMillis();

    public abstract long getLapDurationMillis();

    public abstract int getLapNumber();
}
