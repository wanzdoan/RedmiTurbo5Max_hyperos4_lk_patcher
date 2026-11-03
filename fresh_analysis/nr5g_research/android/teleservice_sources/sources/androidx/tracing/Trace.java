package androidx.tracing;

/* JADX INFO: compiled from: Trace.android.kt */
/* JADX INFO: loaded from: classes.dex */
public final class Trace {
    public static final Trace INSTANCE = new Trace();

    private Trace() {
    }

    public static final boolean isEnabled() {
        return TraceApi29Impl.INSTANCE.isEnabled();
    }

    public static final void beginSection(String str) {
        android.os.Trace.beginSection(INSTANCE.truncatedTraceSectionLabel(str));
    }

    public static final void endSection() {
        android.os.Trace.endSection();
    }

    private final String truncatedTraceSectionLabel(String str) {
        String str2 = str.length() <= 127 ? str : null;
        return str2 == null ? str.substring(0, 127) : str2;
    }
}
