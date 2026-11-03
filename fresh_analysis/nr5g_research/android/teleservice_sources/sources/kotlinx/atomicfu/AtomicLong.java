package kotlinx.atomicfu;

import com.ot.pubsub.a.a;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: compiled from: AtomicFU.kt */
/* JADX INFO: loaded from: classes2.dex */
public final class AtomicLong {
    private static final Companion Companion = new Companion(null);
    private static final AtomicLongFieldUpdater FU = AtomicLongFieldUpdater.newUpdater(AtomicLong.class, a.p);
    private final TraceBase trace;
    private volatile long value;

    public AtomicLong(long j, TraceBase traceBase) {
        traceBase.getClass();
        this.trace = traceBase;
        this.value = j;
    }

    public final long getValue() {
        return this.value;
    }

    public final void setValue(long j) {
        this.value = j;
        TraceBase traceBase = this.trace;
        if (traceBase != TraceBase.None.INSTANCE) {
            traceBase.append("set(" + j + ")");
        }
    }

    public final boolean compareAndSet(long j, long j2) {
        TraceBase traceBase;
        boolean zCompareAndSet = FU.compareAndSet(this, j, j2);
        if (zCompareAndSet && (traceBase = this.trace) != TraceBase.None.INSTANCE) {
            traceBase.append("CAS(" + j + ", " + j2 + ")");
        }
        return zCompareAndSet;
    }

    public final long getAndDecrement() {
        long andDecrement = FU.getAndDecrement(this);
        TraceBase traceBase = this.trace;
        if (traceBase != TraceBase.None.INSTANCE) {
            traceBase.append("getAndDec():" + andDecrement);
        }
        return andDecrement;
    }

    public final long addAndGet(long j) {
        long jAddAndGet = FU.addAndGet(this, j);
        TraceBase traceBase = this.trace;
        if (traceBase != TraceBase.None.INSTANCE) {
            traceBase.append("addAndGet(" + j + "):" + jAddAndGet);
        }
        return jAddAndGet;
    }

    public final long incrementAndGet() {
        long jIncrementAndGet = FU.incrementAndGet(this);
        TraceBase traceBase = this.trace;
        if (traceBase != TraceBase.None.INSTANCE) {
            traceBase.append("incAndGet():" + jIncrementAndGet);
        }
        return jIncrementAndGet;
    }

    public String toString() {
        return String.valueOf(this.value);
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
