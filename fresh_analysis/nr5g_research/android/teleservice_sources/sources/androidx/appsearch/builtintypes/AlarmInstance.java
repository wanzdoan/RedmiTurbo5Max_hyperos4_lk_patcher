package androidx.appsearch.builtintypes;

/* JADX INFO: loaded from: classes.dex */
public abstract class AlarmInstance extends Thing {
    public abstract String getScheduledTime();

    public abstract long getSnoozeDurationMillis();

    public abstract int getStatus();
}
