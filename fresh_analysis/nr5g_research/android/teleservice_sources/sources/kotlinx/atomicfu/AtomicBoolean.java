package kotlinx.atomicfu;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: compiled from: AtomicFU.kt */
/* JADX INFO: loaded from: classes2.dex */
public final class AtomicBoolean {
    private static final Companion Companion = new Companion(null);
    private static final AtomicIntegerFieldUpdater FU = AtomicIntegerFieldUpdater.newUpdater(AtomicBoolean.class, "_value");
    private volatile int _value;
    private final TraceBase trace;

    public AtomicBoolean(boolean z, TraceBase traceBase) {
        traceBase.getClass();
        this.trace = traceBase;
        this._value = z ? 1 : 0;
    }

    public final boolean getValue() {
        return this._value != 0;
    }

    public final boolean compareAndSet(boolean z, boolean z2) {
        TraceBase traceBase;
        boolean zCompareAndSet = FU.compareAndSet(this, z ? 1 : 0, z2 ? 1 : 0);
        if (zCompareAndSet && (traceBase = this.trace) != TraceBase.None.INSTANCE) {
            traceBase.append("CAS(" + z + ", " + z2 + ")");
        }
        return zCompareAndSet;
    }

    public String toString() {
        return String.valueOf(getValue());
    }

    /* JADX INFO: compiled from: AtomicFU.kt */
    final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
