package com.xiaomi.onetrack.a;

import com.xiaomi.onetrack.util.r;

/* JADX INFO: loaded from: classes2.dex */
class b implements Runnable {
    final /* synthetic */ com.xiaomi.onetrack.a.b.a a;
    final /* synthetic */ int b;
    final /* synthetic */ Throwable c;
    final /* synthetic */ String d;
    final /* synthetic */ a e;

    b(a aVar, com.xiaomi.onetrack.a.b.a aVar2, int i, Throwable th, String str) {
        this.e = aVar;
        this.a = aVar2;
        this.b = i;
        this.c = th;
        this.d = str;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            if (this.e.f()) {
                return;
            }
            if (this.e.a(this.a, this.b, this.c)) {
                r.a(a.g, "AdMCaughtExceptionEvent not allow track , is filter by cloud ");
            } else {
                if (!this.e.g() || com.xiaomi.onetrack.c.d.a().a(this.a, this.b, this.d, this.c).booleanValue()) {
                    return;
                }
                com.xiaomi.onetrack.c.d.a().a(com.xiaomi.onetrack.e.c.a(this.a, this.b, this.d, this.c), false);
                this.e.c();
            }
        } catch (Exception e) {
            r.a(a.g, "trackAdMCaughtExceptionEvent Exception:" + e.getMessage());
        }
    }
}
