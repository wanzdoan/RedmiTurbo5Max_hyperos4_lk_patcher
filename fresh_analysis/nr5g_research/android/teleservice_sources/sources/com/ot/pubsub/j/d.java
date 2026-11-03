package com.ot.pubsub.j;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.ot.pubsub.util.k;
import com.ot.pubsub.util.l;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class d {
    public static final String a = "config";
    public static final String b = "appId";
    public static final String c = "version";
    private static final String d = "UploaderEngine";
    private static volatile d e = null;
    private static final int h = 123;
    private static int i = 900000;
    private com.ot.pubsub.j.b f;
    private a g;
    private volatile boolean j = true;
    private BroadcastReceiver k = new e(this);
    private BroadcastReceiver l = new g(this);

    public static d a() {
        if (e == null) {
            b();
        }
        return e;
    }

    public static void b() {
        if (e == null) {
            synchronized (d.class) {
                try {
                    if (e == null) {
                        e = new d();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public synchronized void a(boolean z) {
        try {
            com.ot.pubsub.j.b bVar = this.f;
            if (bVar != null) {
                bVar.a(z);
            } else {
                com.ot.pubsub.util.j.b(d, "*** impossible, upload timer should not be null");
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void a(int i2, boolean z) {
        try {
            com.ot.pubsub.j.b bVar = this.f;
            if (bVar != null) {
                bVar.a(i2, z);
            } else {
                com.ot.pubsub.util.j.b(d, "*** impossible, upload timer should not be null");
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void c() {
        try {
            com.ot.pubsub.j.b bVar = this.f;
            if (bVar != null) {
                bVar.a();
            } else {
                com.ot.pubsub.util.j.b(d, "*** impossible, isInitCheckUpload timer should not be null");
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public boolean d() throws Throwable {
        ArrayList arrayList;
        try {
            int i2 = 0;
            if (!h()) {
                return false;
            }
            int i3 = 100;
            while (true) {
                if (com.ot.pubsub.util.j.a) {
                    i3 = 1000;
                }
                if (i2 >= i3) {
                    return true;
                }
                com.ot.pubsub.j.a aVarC = com.ot.pubsub.g.c.a().c();
                if (aVarC == null || (arrayList = aVarC.a) == null || arrayList.size() == 0) {
                    com.ot.pubsub.util.j.a(d, "满足条件的记录为空，即将返回");
                } else {
                    c.a(aVarC);
                    if (aVarC.d) {
                        com.ot.pubsub.util.j.a(d, "No more records ");
                    } else {
                        i2++;
                    }
                }
                return true;
            }
        } catch (Exception unused) {
        }
    }

    public boolean a(int i2) throws Throwable {
        ArrayList arrayList;
        try {
            int i3 = 0;
            if (!i()) {
                return false;
            }
            int i4 = 100;
            while (true) {
                if (com.ot.pubsub.util.j.a) {
                    i4 = 1000;
                }
                if (i3 >= i4) {
                    return true;
                }
                com.ot.pubsub.j.a aVarA = com.ot.pubsub.g.f.a().a(i2);
                if (aVarA == null || (arrayList = aVarA.a) == null || arrayList.size() == 0) {
                    com.ot.pubsub.util.j.a(d, "满足条件的记录为空，即将返回");
                } else {
                    c.b(aVarA);
                    if (aVarA.d) {
                        com.ot.pubsub.util.j.a(d, "No more records ");
                    } else {
                        i3++;
                    }
                }
                return true;
            }
        } catch (Exception unused) {
        }
    }

    private d() {
        j();
        m();
        g();
        this.g = new a(this, Looper.getMainLooper(), null);
    }

    private void g() {
        HandlerThread handlerThread = new HandlerThread("mi_ad_pubsub_uploader_worker");
        handlerThread.start();
        this.f = new com.ot.pubsub.j.b(handlerThread.getLooper());
    }

    class b {
        public boolean a;
        public boolean b;

        b(boolean z, boolean z2) {
            this.a = z;
            this.b = z2;
        }
    }

    private boolean h() {
        if (k.a() && !l.c(d) && !com.ot.pubsub.g.c.a().e()) {
            return true;
        }
        com.ot.pubsub.util.j.a(d, "not match the upload status，即将返回");
        return false;
    }

    private boolean i() {
        if (k.a() && !l.c(d) && !com.ot.pubsub.g.f.a().d()) {
            return true;
        }
        com.ot.pubsub.util.j.a(d, "hb data not match the upload status，即将返回");
        return false;
    }

    public void b(int i2) {
        com.ot.pubsub.j.b bVar = this.f;
        if (bVar != null) {
            bVar.a(i2);
        }
    }

    private void j() {
        try {
            Context contextB = com.ot.pubsub.util.b.b();
            if (contextB == null) {
                return;
            }
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            contextB.registerReceiver(this.k, intentFilter);
        } catch (Exception e2) {
            com.ot.pubsub.util.j.b(d, "registerScreenReceiver: %s", e2.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void k() {
        a aVar = this.g;
        if (aVar != null) {
            aVar.sendEmptyMessageDelayed(h, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void l() {
        a aVar = this.g;
        if (aVar != null) {
            aVar.removeCallbacksAndMessages(Integer.valueOf(h));
        }
    }

    class a extends Handler {
        /* synthetic */ a(d dVar, Looper looper, e eVar) {
            this(looper);
        }

        private a(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == d.h) {
                com.ot.pubsub.util.e.a(new j(this));
            }
        }
    }

    private void m() {
        try {
            Context contextB = com.ot.pubsub.util.b.b();
            if (contextB == null) {
                return;
            }
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            contextB.registerReceiver(this.l, intentFilter);
        } catch (Exception e2) {
            com.ot.pubsub.util.j.a(d, "registerNetReceiver: " + e2);
        }
    }

    public void e() {
        com.ot.pubsub.g.a.a(new i(this));
    }
}
