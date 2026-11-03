package com.xiaomi.onetrack.api;

import android.text.TextUtils;
import com.xiaomi.onetrack.b.a;
import com.xiaomi.onetrack.c.j;
import com.xiaomi.onetrack.util.e;
import com.xiaomi.onetrack.util.s;

/* JADX INFO: loaded from: classes2.dex */
class o implements Runnable {
    final /* synthetic */ c a;

    o(c cVar) {
        this.a = cVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            com.xiaomi.onetrack.util.r.a();
            s.a();
            a.a().a(this.a.e.getAppId());
            if (com.xiaomi.onetrack.util.ab.b(this.a.e.getAdEventAppId())) {
                ah.a().c();
                a.a().a(this.a.e.getAdEventAppId());
            }
            if (com.xiaomi.onetrack.util.ac.w() == 0) {
                com.xiaomi.onetrack.util.ac.h(System.currentTimeMillis());
            }
            if (!TextUtils.isEmpty(this.a.e.getInstanceId())) {
                com.xiaomi.onetrack.util.q.a().a(this.a.e.getInstanceId());
            }
            this.a.h();
            e.a();
            j.c(false);
            com.xiaomi.onetrack.c.s.a().b(this.a.e.getAppId());
        } catch (Throwable th) {
            com.xiaomi.onetrack.util.r.b("BaseOneTrackImp", "init WorkerExecutor execute throwable:" + th.getMessage());
        }
    }
}
