package com.xiaomi.onetrack.c;

import android.text.TextUtils;

/* JADX INFO: loaded from: classes2.dex */
class w implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ String b;
    final /* synthetic */ String c;
    final /* synthetic */ s d;

    w(s sVar, String str, String str2, String str3) {
        this.d = sVar;
        this.a = str;
        this.b = str2;
        this.c = str3;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (j.b()) {
            String strA = com.xiaomi.onetrack.util.a.a(this.a);
            if (TextUtils.isEmpty(strA)) {
                com.xiaomi.onetrack.util.r.a("SystemImpCacheManager", "trackPubSubSystemImpCache defaultTopic is null");
            } else {
                this.d.a(this.b, strA, this.c, this.a, true);
            }
        }
    }
}
