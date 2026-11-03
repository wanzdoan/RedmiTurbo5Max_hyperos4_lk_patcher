package com.android.internal.telephony.util;

import android.util.LruCache;
import java.util.Objects;
import java.util.function.LongSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

/* JADX INFO: loaded from: classes.dex */
public class LruCacheWithExpiry<K, V> {
    private final long mExpiryDurationMs;
    private final LruCache mMap;
    private final Predicate mShouldCacheValue;
    private final LongSupplier mTimeSupplier;

    public LruCacheWithExpiry(LongSupplier longSupplier, long j, int i, Predicate<V> predicate) {
        this.mTimeSupplier = longSupplier;
        this.mExpiryDurationMs = j;
        this.mMap = new LruCache(i);
        this.mShouldCacheValue = predicate;
    }

    public V get(K k) {
        synchronized (this.mMap) {
            try {
                CacheValue cacheValue = (CacheValue) this.mMap.get(k);
                if (cacheValue != null && !isExpired(cacheValue.timestamp)) {
                    return cacheValue.entry;
                }
                this.mMap.remove(k);
                return null;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public V getOrCompute(K k, Supplier<V> supplier) {
        synchronized (this.mMap) {
            try {
                V v = get(k);
                if (v != null) {
                    return v;
                }
                V v2 = supplier.get();
                if (v2 != null && this.mShouldCacheValue.test(v2)) {
                    put(k, v2);
                }
                return v2;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void put(K k, V v) {
        Objects.requireNonNull(v);
        synchronized (this.mMap) {
            this.mMap.put(k, new CacheValue(this.mTimeSupplier.getAsLong(), v));
        }
    }

    public V putIfAbsent(K k, V v) {
        V v2;
        Objects.requireNonNull(v);
        synchronized (this.mMap) {
            try {
                v2 = get(k);
                if (v2 == null) {
                    put(k, v);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return v2;
    }

    public void clear() {
        synchronized (this.mMap) {
            this.mMap.evictAll();
        }
    }

    private boolean isExpired(long j) {
        return this.mTimeSupplier.getAsLong() > this.mExpiryDurationMs + j || j > this.mTimeSupplier.getAsLong();
    }

    private static class CacheValue<V> {
        public final V entry;
        public final long timestamp;

        /* JADX WARN: Multi-variable type inference failed */
        CacheValue(long j, Object obj) {
            this.timestamp = j;
            this.entry = obj;
        }
    }
}
