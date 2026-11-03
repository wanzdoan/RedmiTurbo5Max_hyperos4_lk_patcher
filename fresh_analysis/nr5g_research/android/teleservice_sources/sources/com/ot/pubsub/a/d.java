package com.ot.pubsub.a;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;
import com.ot.pubsub.Configuration;
import com.ot.pubsub.PubSubMainThreadException;
import com.ot.pubsub.PubSubTrack;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class d {
    public static final int a = 1;
    public static final int b = 2;
    private static final String c = "PubSubTrackImp";
    private static ExecutorService e;
    private c d;
    private PubSubTrack.ICommonPropertyProvider f;
    private Context g;
    private Configuration h;
    private com.ot.pubsub.util.q i;
    private PubSubTrack.IEventHook j;
    private BroadcastReceiver k = new k(this);

    public d(Context context, Configuration configuration) {
        Context applicationContext = context.getApplicationContext();
        this.g = applicationContext;
        this.h = configuration;
        a(applicationContext);
    }

    private void a(Context context) {
        com.ot.pubsub.util.l.a(this.h.isInternational(), this.h.getRegion());
        if (e == null) {
            e = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue());
        }
        com.ot.pubsub.util.q qVar = new com.ot.pubsub.util.q(this.h);
        this.i = qVar;
        this.d = new q(this.g, this.h, qVar);
        c();
        com.ot.pubsub.util.l.a(this.h.isOverrideMiuiRegionSetting());
        d();
        e.execute(new e(this));
    }

    public void a(String str, String str2, String str3, Map map, boolean z) {
        e.execute(new h(this, str, str2, str3, map, z));
    }

    public void a(String str, String str2, String str3, Map map, Map map2) {
        e.execute(new i(this, str3, map, str, str2, map2));
    }

    private void c() {
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
            com.ot.pubsub.util.j.b(c, "registerScreenReceiver: %s", e2.toString());
        }
    }

    private void d() {
        try {
            Context contextB = com.ot.pubsub.util.b.b();
            if (contextB == null) {
                return;
            }
            ((Application) contextB).registerActivityLifecycleCallbacks(new j(this));
        } catch (Exception e2) {
            com.ot.pubsub.util.j.a(c, "registerLifecycleCallback: " + e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        if (com.ot.pubsub.g.i.c()) {
            com.ot.pubsub.util.e.a(new m(this));
        }
    }

    public void a(int i) {
        com.ot.pubsub.j.d.a().b(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean c(String str) {
        boolean zA = com.ot.pubsub.util.m.a(str);
        if (!zA) {
            com.ot.pubsub.util.j.b(c, String.format("Invalid eventname: %s. Eventname can only consist of numbers, letters, underscores ,and can not start with a number or \"onetrack_\" or \"ot_\"", str));
        }
        return !zA;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public JSONObject d(String str) throws Throwable {
        try {
            PubSubTrack.ICommonPropertyProvider iCommonPropertyProvider = this.f;
            JSONObject jSONObjectA = com.ot.pubsub.util.m.a(iCommonPropertyProvider != null ? iCommonPropertyProvider.getDynamicProperty(str) : null, false);
            String strA = com.ot.pubsub.util.g.a(com.ot.pubsub.util.m.a(this.h));
            return com.ot.pubsub.util.m.a(jSONObjectA, !TextUtils.isEmpty(strA) ? new JSONObject(strA) : null);
        } catch (Exception e2) {
            com.ot.pubsub.util.j.b(c, "getCommonProperty: " + e2.toString());
            return null;
        }
    }

    public void a(PubSubTrack.IEventHook iEventHook) {
        this.j = iEventHook;
        this.i.a(iEventHook);
    }

    public void a(PubSubTrack.ICommonPropertyProvider iCommonPropertyProvider) {
        this.f = iCommonPropertyProvider;
    }

    public void a(Map map) {
        if (map == null) {
            return;
        }
        e.execute(new n(this, map));
    }

    public void a() {
        e.execute(new o(this));
    }

    public void a(String str) {
        e.execute(new p(this, str));
    }

    public void a(boolean z) {
        if (this.h.isUseCustomPrivacyPolicy()) {
            e.execute(new f(this, z));
        }
    }

    public void b(String str) {
        e.execute(new g(this, str));
    }

    public String b() throws PubSubMainThreadException {
        if (com.ot.pubsub.util.r.a()) {
            if (com.ot.pubsub.util.j.a) {
                throw new PubSubMainThreadException("Don't use it on the main thread");
            }
            Log.e(c, "getInstanceId() throw exception : Don't use it on the main thread");
            return "";
        }
        return com.ot.pubsub.g.b.a().b();
    }
}
