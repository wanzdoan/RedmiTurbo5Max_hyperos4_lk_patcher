package com.android.internal.telephony;

import android.telephony.Rlog;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/* JADX INFO: loaded from: classes.dex */
public class WapPushCache {
    private static final long CACHE_EXPIRY_TIME = 1209600000;
    private static final HashMap sMessageSizes = new LinkedHashMap<String, CacheEntry>() { // from class: com.android.internal.telephony.WapPushCache.1
        @Override // java.util.LinkedHashMap
        protected boolean removeEldestEntry(Map.Entry<String, CacheEntry> entry) {
            return size() > 500;
        }
    };
    public static TelephonyFacade sTelephonyFacade = new TelephonyFacade();

    public static void putWapMessageSize(byte[] bArr, byte[] bArr2, long j) {
        long elapsedSinceBootMillis = sTelephonyFacade.getElapsedSinceBootMillis() + CACHE_EXPIRY_TIME;
        if (j <= 0) {
            Rlog.e("WAP PUSH CACHE", "Invalid message size of " + j + ". Not inserting.");
            return;
        }
        HashMap map = sMessageSizes;
        synchronized (map) {
            map.put(Arrays.toString(bArr), new CacheEntry(j, elapsedSinceBootMillis));
            map.put(Arrays.toString(ByteBuffer.allocate(bArr.length + bArr2.length).put(bArr).put(bArr2).array()), new CacheEntry(j, elapsedSinceBootMillis));
            invalidateOldEntries();
        }
    }

    private static void invalidateOldEntries() {
        long elapsedSinceBootMillis = sTelephonyFacade.getElapsedSinceBootMillis();
        Iterator it = sMessageSizes.values().iterator();
        while (it.hasNext() && ((CacheEntry) it.next()).mExpiry < elapsedSinceBootMillis) {
            it.remove();
        }
    }

    public static long getWapMessageSize(byte[] bArr) {
        if (bArr.length == 0) {
            Phone$$ExternalSyntheticBUOutline0.m("Found empty locationUrl");
            return 0L;
        }
        CacheEntry cacheEntry = (CacheEntry) sMessageSizes.get(Arrays.toString(bArr));
        if (cacheEntry == null) {
            throw new NoSuchElementException("No cached WAP size for locationUrl " + Arrays.toString(bArr));
        }
        return cacheEntry.mSize;
    }

    public static void clear() {
        sMessageSizes.clear();
    }

    public static int size() {
        return sMessageSizes.size();
    }

    private static class CacheEntry {
        private final long mExpiry;
        private final long mSize;

        CacheEntry(long j, long j2) {
            this.mSize = j;
            this.mExpiry = j2;
        }
    }
}
