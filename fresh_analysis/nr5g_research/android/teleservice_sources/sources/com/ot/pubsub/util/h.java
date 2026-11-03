package com.ot.pubsub.util;

import android.text.TextUtils;
import android.util.LruCache;

/* JADX INFO: loaded from: classes.dex */
final class h extends LruCache {
    h(int i) {
        super(i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.util.LruCache
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public int sizeOf(String str, g.a aVar) {
        if (aVar == null || TextUtils.isEmpty(aVar.a)) {
            return 0;
        }
        return aVar.a.length();
    }
}
