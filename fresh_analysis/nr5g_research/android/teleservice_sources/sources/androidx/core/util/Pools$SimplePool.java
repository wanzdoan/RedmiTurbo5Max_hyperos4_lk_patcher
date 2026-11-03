package androidx.core.util;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: compiled from: Pools.kt */
/* JADX INFO: loaded from: classes.dex */
public class Pools$SimplePool implements Pools$Pool {
    private final Object[] pool;
    private int poolSize;

    public Pools$SimplePool(int i) {
        if (i <= 0) {
            Folme$$ExternalSyntheticBUOutline0.m("The max pool size must be > 0");
            throw null;
        }
        this.pool = new Object[i];
    }

    @Override // androidx.core.util.Pools$Pool
    public Object acquire() {
        int i = this.poolSize;
        if (i <= 0) {
            return null;
        }
        int i2 = i - 1;
        Object obj = this.pool[i2];
        obj.getClass();
        this.pool[i2] = null;
        this.poolSize--;
        return obj;
    }

    @Override // androidx.core.util.Pools$Pool
    public boolean release(Object obj) {
        obj.getClass();
        if (isInPool(obj)) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("Already in the pool!");
            return false;
        }
        int i = this.poolSize;
        Object[] objArr = this.pool;
        if (i >= objArr.length) {
            return false;
        }
        objArr[i] = obj;
        this.poolSize = i + 1;
        return true;
    }

    private final boolean isInPool(Object obj) {
        int i = this.poolSize;
        for (int i2 = 0; i2 < i; i2++) {
            if (this.pool[i2] == obj) {
                return true;
            }
        }
        return false;
    }
}
