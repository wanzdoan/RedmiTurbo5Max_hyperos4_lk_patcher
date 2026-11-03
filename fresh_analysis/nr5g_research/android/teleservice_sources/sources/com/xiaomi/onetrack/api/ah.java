package com.xiaomi.onetrack.api;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.xiaomi.onetrack.OneTrack;
import com.xiaomi.onetrack.a.c.b;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes2.dex */
public class ah {
    private static String b = "onetrack_broadcast_manager";
    private static volatile ah c = null;
    private static volatile boolean h = false;
    private static volatile boolean j = false;
    private Handler d;
    private CopyOnWriteArrayList i = new CopyOnWriteArrayList();
    private AtomicBoolean k = new AtomicBoolean(false);
    private boolean l = false;
    private boolean m = false;
    private BroadcastReceiver n = new ai(this);
    private BroadcastReceiver o = new aj(this);

    public static ah a() {
        if (c == null) {
            b();
        }
        return c;
    }

    public static void b() {
        if (c == null) {
            synchronized (ah.class) {
                try {
                    if (c == null) {
                        c = new ah();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    private ah() {
        try {
            HandlerThread handlerThread = new HandlerThread(b);
            handlerThread.start();
            this.d = new a(this, handlerThread.getLooper(), null);
        } catch (Throwable unused) {
        }
    }

    public void c() {
        this.l = true;
    }

    public void d() {
        this.m = true;
    }

    public void a(ao aoVar) {
        if (this.i.contains(aoVar)) {
            return;
        }
        this.i.add(aoVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i) {
        for (ao aoVar : this.i) {
            if (i == 100) {
                aoVar.a(true);
            } else if (i == 101) {
                aoVar.a(false);
            }
        }
    }

    public void e() {
        if (h) {
            return;
        }
        h = true;
        try {
            g();
        } catch (Throwable unused) {
            h = false;
        }
    }

    private void g() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        com.xiaomi.onetrack.f.a.b().registerReceiver(this.n, intentFilter);
        Log.d(com.xiaomi.onetrack.util.r.a("BroadcastManager"), "register screen receiver");
    }

    class a extends Handler {
        /* synthetic */ a(ah ahVar, Looper looper, ai aiVar) {
            this(looper);
        }

        private a(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            boolean zA;
            if (message == null) {
                return;
            }
            int i = message.what;
            if (i == 100 || i == 101) {
                try {
                    ah.this.a(i);
                } catch (Exception e) {
                    com.xiaomi.onetrack.util.r.a("BroadcastManager", "screenReceiver exception: ", e);
                }
            }
            if (message.what == 10) {
                if (ah.this.k.get()) {
                    try {
                        if (OneTrack.isRestrictGetNetworkInfo()) {
                            zA = com.xiaomi.onetrack.b.o.c();
                            com.xiaomi.onetrack.b.o.b(!zA);
                        } else {
                            zA = com.xiaomi.onetrack.g.c.a();
                            com.xiaomi.onetrack.b.o.b(zA);
                        }
                        com.xiaomi.onetrack.util.r.a("BroadcastManager", "Only one of allowed NetworkInfo :" + OneTrack.isRestrictGetNetworkInfo() + " ,network status changed, isNetworkConnected: " + com.xiaomi.onetrack.b.o.c());
                        if (zA) {
                            if (ah.this.l) {
                                b.a().a(com.xiaomi.onetrack.b.o.c());
                            }
                            if (ah.this.m) {
                                com.xiaomi.onetrack.c.ah.a().a(com.xiaomi.onetrack.b.o.c());
                            }
                        }
                    } catch (Throwable th) {
                        com.xiaomi.onetrack.util.r.b("BroadcastManager", "MESSAGE_BROADCAST_NET_RECEIVER throwable:" + th.getMessage());
                    }
                }
                ah.this.k.set(true);
            }
        }
    }

    public void f() {
        if (j) {
            return;
        }
        j = true;
        boolean zB = com.xiaomi.onetrack.g.c.b();
        com.xiaomi.onetrack.util.r.a("BroadcastManager", "Get network status for the first time, isNetworkConnected: " + zB);
        com.xiaomi.onetrack.b.o.b(zB);
        try {
            h();
        } catch (Throwable unused) {
            j = false;
        }
    }

    private void h() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        com.xiaomi.onetrack.f.a.b().registerReceiver(this.o, intentFilter);
        Log.d(com.xiaomi.onetrack.util.r.a("BroadcastManager"), "register net receiver");
    }
}
