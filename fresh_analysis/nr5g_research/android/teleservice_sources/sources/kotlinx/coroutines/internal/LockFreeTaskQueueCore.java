package kotlinx.coroutines.internal;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlinx.atomicfu.AtomicArray;
import kotlinx.atomicfu.AtomicFU;
import kotlinx.atomicfu.AtomicFU_commonKt;
import kotlinx.atomicfu.AtomicLong;
import kotlinx.atomicfu.AtomicRef;

/* JADX INFO: compiled from: LockFreeTaskQueue.kt */
/* JADX INFO: loaded from: classes2.dex */
public final class LockFreeTaskQueueCore {
    public static final Companion Companion = new Companion(null);
    public static final Symbol REMOVE_FROZEN = new Symbol("REMOVE_FROZEN");
    private final AtomicRef _next = AtomicFU.atomic((Object) null);
    private final AtomicLong _state = AtomicFU.atomic(0L);
    private final AtomicArray array;
    private final int capacity;
    private final int mask;
    private final boolean singleConsumer;

    public LockFreeTaskQueueCore(int i, boolean z) {
        this.capacity = i;
        this.singleConsumer = z;
        int i2 = i - 1;
        this.mask = i2;
        this.array = AtomicFU_commonKt.atomicArrayOfNulls(i);
        if (i2 > 1073741823) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("Check failed.");
            throw null;
        }
        if ((i & i2) == 0) {
            return;
        }
        OrtUtil$$ExternalSyntheticBUOutline0.m("Check failed.");
        throw null;
    }

    public final int getSize() {
        long value = this._state.getValue();
        return 1073741823 & (((int) ((value & 1152921503533105152L) >> 30)) - ((int) (1073741823 & value)));
    }

    public final boolean close() {
        long value;
        AtomicLong atomicLong = this._state;
        do {
            value = atomicLong.getValue();
            if ((value & 2305843009213693952L) != 0) {
                return true;
            }
            if ((1152921504606846976L & value) != 0) {
                return false;
            }
        } while (!atomicLong.compareAndSet(value, 2305843009213693952L | value));
        return true;
    }

    public final int addLast(Object obj) {
        obj.getClass();
        AtomicLong atomicLong = this._state;
        while (true) {
            long value = atomicLong.getValue();
            if ((3458764513820540928L & value) != 0) {
                return Companion.addFailReason(value);
            }
            int i = (int) (1073741823 & value);
            int i2 = (int) ((1152921503533105152L & value) >> 30);
            int i3 = this.mask;
            if (((i2 + 2) & i3) == (i & i3)) {
                return 1;
            }
            if (!this.singleConsumer && this.array.get(i2 & i3).getValue() != null) {
                int i4 = this.capacity;
                if (i4 < 1024 || ((i2 - i) & 1073741823) > (i4 >> 1)) {
                    return 1;
                }
            } else if (this._state.compareAndSet(value, Companion.updateTail(value, (i2 + 1) & 1073741823))) {
                this.array.get(i2 & i3).setValue(obj);
                while ((this._state.getValue() & 1152921504606846976L) != 0 && (this = this.next().fillPlaceholder(i2, obj)) != null) {
                }
                return 0;
            }
        }
    }

    private final LockFreeTaskQueueCore fillPlaceholder(int i, Object obj) {
        Object value = this.array.get(this.mask & i).getValue();
        if (!(value instanceof Placeholder) || ((Placeholder) value).index != i) {
            return null;
        }
        this.array.get(i & this.mask).setValue(obj);
        return this;
    }

    public final Object removeFirstOrNull() {
        AtomicLong atomicLong = this._state;
        while (true) {
            long value = atomicLong.getValue();
            if ((1152921504606846976L & value) != 0) {
                return REMOVE_FROZEN;
            }
            int i = (int) (1073741823 & value);
            int i2 = this.mask;
            if ((((int) ((1152921503533105152L & value) >> 30)) & i2) == (i & i2)) {
                return null;
            }
            Object value2 = this.array.get(i2 & i).getValue();
            if (value2 == null) {
                if (this.singleConsumer) {
                    return null;
                }
            } else {
                if (value2 instanceof Placeholder) {
                    return null;
                }
                int i3 = (i + 1) & 1073741823;
                if (this._state.compareAndSet(value, Companion.updateHead(value, i3))) {
                    this.array.get(this.mask & i).setValue(null);
                    return value2;
                }
                if (this.singleConsumer) {
                    do {
                        this = this.removeSlowPath(i, i3);
                    } while (this != null);
                    return value2;
                }
            }
        }
    }

    private final LockFreeTaskQueueCore removeSlowPath(int i, int i2) {
        long value;
        int i3;
        AtomicLong atomicLong = this._state;
        do {
            value = atomicLong.getValue();
            i3 = (int) (1073741823 & value);
            if ((1152921504606846976L & value) != 0) {
                return next();
            }
        } while (!this._state.compareAndSet(value, Companion.updateHead(value, i2)));
        this.array.get(this.mask & i3).setValue(null);
        return null;
    }

    public final LockFreeTaskQueueCore next() {
        return allocateOrGetNextCopy(markFrozen());
    }

    private final long markFrozen() {
        long value;
        long j;
        AtomicLong atomicLong = this._state;
        do {
            value = atomicLong.getValue();
            if ((value & 1152921504606846976L) != 0) {
                return value;
            }
            j = 1152921504606846976L | value;
        } while (!atomicLong.compareAndSet(value, j));
        return j;
    }

    private final LockFreeTaskQueueCore allocateOrGetNextCopy(long j) {
        AtomicRef atomicRef = this._next;
        while (true) {
            LockFreeTaskQueueCore lockFreeTaskQueueCore = (LockFreeTaskQueueCore) atomicRef.getValue();
            if (lockFreeTaskQueueCore != null) {
                return lockFreeTaskQueueCore;
            }
            this._next.compareAndSet(null, allocateNextCopy(j));
        }
    }

    private final LockFreeTaskQueueCore allocateNextCopy(long j) {
        LockFreeTaskQueueCore lockFreeTaskQueueCore = new LockFreeTaskQueueCore(this.capacity * 2, this.singleConsumer);
        int i = (int) (1073741823 & j);
        int i2 = (int) ((1152921503533105152L & j) >> 30);
        while (true) {
            int i3 = this.mask;
            if ((i & i3) != (i2 & i3)) {
                Object value = this.array.get(i3 & i).getValue();
                if (value == null) {
                    value = new Placeholder(i);
                }
                lockFreeTaskQueueCore.array.get(lockFreeTaskQueueCore.mask & i).setValue(value);
                i++;
            } else {
                lockFreeTaskQueueCore._state.setValue(Companion.wo(j, 1152921504606846976L));
                return lockFreeTaskQueueCore;
            }
        }
    }

    /* JADX INFO: compiled from: LockFreeTaskQueue.kt */
    public final class Placeholder {
        public final int index;

        public Placeholder(int i) {
            this.index = i;
        }
    }

    /* JADX INFO: compiled from: LockFreeTaskQueue.kt */
    public final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final int addFailReason(long j) {
            return (j & 2305843009213693952L) != 0 ? 2 : 1;
        }

        public final long wo(long j, long j2) {
            return j & (~j2);
        }

        private Companion() {
        }

        public final long updateHead(long j, int i) {
            return wo(j, 1073741823L) | ((long) i);
        }

        public final long updateTail(long j, int i) {
            return wo(j, 1152921503533105152L) | (((long) i) << 30);
        }
    }
}
