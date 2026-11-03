package com.xiaomi.onetrack.c;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes2.dex */
public class ae extends Handler {
    private final int e;
    private final int f;
    private final int g;
    private int h;
    private AtomicBoolean i;

    public ae(Looper looper) {
        super(looper);
        this.e = 1000;
        this.f = 10000;
        this.g = 1200000;
        this.h = 10000;
        this.i = new AtomicBoolean(false);
    }

    public void a(int i, boolean z) {
        if (hasMessages(1000)) {
            com.xiaomi.onetrack.util.r.a("UploadTimer", "in retry mode, return, prio=" + i);
            return;
        }
        if (z) {
            removeMessages(i);
        }
        if (hasMessages(i)) {
            return;
        }
        long jA = z ? 0L : com.xiaomi.onetrack.b.o.a(i);
        com.xiaomi.onetrack.util.r.a("UploadTimer", "will check prio=" + i + ", delay=" + jA);
        a(i, jA);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        super.handleMessage(message);
        int i = message.what;
        if (i == 1000) {
            b();
            return;
        }
        boolean zA = ah.a().a(i);
        com.xiaomi.onetrack.util.r.a("UploadTimer", "handleCheckUpload ret=" + zA + ", prio=" + i);
        if (zA) {
            return;
        }
        com.xiaomi.onetrack.util.r.a("UploadTimer", "handleCheckUpload failed, will check if need to send retry msg");
        if (hasMessages(1000)) {
            return;
        }
        sendEmptyMessageDelayed(1000, this.h);
        com.xiaomi.onetrack.util.r.a("UploadTimer", "fire retry timer after " + this.h);
    }

    private void b() {
        if (!ah.a().a(2)) {
            removeMessages(1000);
            int i = this.h * 2;
            this.h = i;
            if (i > 1200000) {
                this.h = 1200000;
            }
            com.xiaomi.onetrack.util.r.a("UploadTimer", "will restart retry msg after " + this.h);
            sendEmptyMessageDelayed(1000, (long) this.h);
            return;
        }
        this.h = 10000;
        com.xiaomi.onetrack.util.r.a("UploadTimer", "retry success");
    }

    private void a(int i, long j) {
        removeMessages(i);
        com.xiaomi.onetrack.util.r.a("UploadTimer", "will post msg, prio=" + i + ", delay=" + j);
        sendEmptyMessageDelayed(i, j);
    }

    public void a(boolean z) {
        a.a(new af(this, z));
    }

    public void a() {
        com.xiaomi.onetrack.util.j.a(new ag(this));
    }
}
