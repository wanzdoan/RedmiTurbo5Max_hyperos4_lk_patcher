package com.ot.pubsub.j;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.ot.pubsub.b.n;
import java.util.Random;

/* JADX INFO: loaded from: classes.dex */
public class b extends Handler {
    public static final int a = 10;
    public static final int b = 11;
    private static final String c = "UploadTimer";
    private static int d = 10;
    private static int e = 900;
    private static int f = 10000;

    public b(Looper looper) {
        super(looper);
    }

    public void a(boolean z) {
        if (z) {
            removeMessages(11);
        } else if (hasMessages(11)) {
            com.ot.pubsub.util.j.a(c, "has delayed msg, return");
            return;
        }
        a(11, z ? 0L : b());
    }

    public void a(int i, boolean z) {
        if (z) {
            removeMessages(i);
        } else if (hasMessages(i)) {
            com.ot.pubsub.util.j.a(c, "has delayed msg:" + i);
            return;
        }
        long jA = z ? 0L : n.a(i);
        com.ot.pubsub.util.j.a(c, "will check prio=" + i + ", delay=" + jA);
        a(i, jA);
    }

    public void a() {
        if (hasMessages(10)) {
            com.ot.pubsub.util.j.a(c, "has delayed msg, return");
        } else {
            a(10, new Random().nextInt(d) * 1000);
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) throws Throwable {
        super.handleMessage(message);
        int i = message.what;
        if (i == 10) {
            d.a().d();
            d.a().a(0);
            d.a().a(1);
        } else {
            if (i == 11) {
                d.a().d();
                com.ot.pubsub.util.j.a(c, "UploadTimer.handleMessage, msg.what=" + message.what);
                return;
            }
            if (i == 0 || i == 1 || i == 2) {
                d.a().a(i);
                com.ot.pubsub.util.j.a(c, "UploadTimer.handleMessage, msg.what=" + message.what);
            }
        }
    }

    private void a(int i, long j) {
        removeMessages(i);
        com.ot.pubsub.util.j.a(c, "will post msg, prio=" + i + ", delay=" + j);
        sendEmptyMessageDelayed(i, j);
    }

    public int b() {
        return f;
    }

    public void a(int i) {
        if (i >= e) {
            f = 900000;
        } else if (i <= 1) {
            f = 1000;
        } else {
            f = i * 1000;
        }
    }
}
