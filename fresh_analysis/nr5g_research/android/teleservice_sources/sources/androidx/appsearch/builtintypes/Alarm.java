package androidx.appsearch.builtintypes;

/* JADX INFO: loaded from: classes.dex */
public abstract class Alarm extends Thing {
    public abstract String getBlackoutPeriodEndDate();

    public abstract String getBlackoutPeriodStartDate();

    public abstract int[] getDaysOfWeek();

    public abstract int getHour();

    public abstract int getMinute();

    public abstract AlarmInstance getNextInstance();

    public abstract int getOriginatingDevice();

    public abstract AlarmInstance getPreviousInstance();

    public abstract String getRingtone();

    public abstract boolean isEnabled();

    public abstract boolean shouldVibrate();
}
