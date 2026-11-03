package kotlinx.atomicfu;

import com.ot.pubsub.a.a;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: compiled from: AtomicFU.kt */
/* JADX INFO: loaded from: classes2.dex */
public final class AtomicRef {
    private static final Companion Companion = new Companion(null);
    private static final AtomicReferenceFieldUpdater FU = AtomicReferenceFieldUpdater.newUpdater(AtomicRef.class, Object.class, a.p);
    private final TraceBase trace;
    private volatile Object value;

    public AtomicRef(Object obj, TraceBase traceBase) {
        traceBase.getClass();
        this.trace = traceBase;
        this.value = obj;
    }

    public final Object getValue() {
        return this.value;
    }

    public final void setValue(Object obj) {
        this.value = obj;
        TraceBase traceBase = this.trace;
        if (traceBase != TraceBase.None.INSTANCE) {
            traceBase.append("set(" + obj + ")");
        }
    }

    public final boolean compareAndSet(Object obj, Object obj2) {
        TraceBase traceBase;
        boolean zCompareAndSet = FU.compareAndSet(this, obj, obj2);
        if (zCompareAndSet && (traceBase = this.trace) != TraceBase.None.INSTANCE) {
            traceBase.append("CAS(" + obj + ", " + obj2 + ")");
        }
        return zCompareAndSet;
    }

    public final Object getAndSet(Object obj) {
        Object andSet = FU.getAndSet(this, obj);
        TraceBase traceBase = this.trace;
        if (traceBase != TraceBase.None.INSTANCE) {
            traceBase.append("getAndSet(" + obj + "):" + andSet);
        }
        return andSet;
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
