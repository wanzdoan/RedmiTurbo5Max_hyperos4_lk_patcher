package com.xiaomi.onetrack.api;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import com.xiaomi.onetrack.Configuration;
import com.xiaomi.onetrack.OnMainThreadException;
import com.xiaomi.onetrack.OneTrack;
import com.xiaomi.onetrack.f.a;
import com.xiaomi.onetrack.h.d.b;
import com.xiaomi.onetrack.util.ae;
import com.xiaomi.onetrack.util.j;
import com.xiaomi.onetrack.util.s;
import com.xiaomi.onetrack.util.t;
import com.xiaomi.onetrack.util.x;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public abstract class c {
    protected static ExecutorService b;
    protected ao a;
    protected Context c;
    protected ap d;
    protected Configuration e;
    protected x g;
    private Handler i;

    protected abstract void a(Context context);

    protected abstract boolean a();

    public OneTrack.ICommonPropertyProvider d() {
        return null;
    }

    protected abstract ao j();

    public c(Context context, Configuration configuration) {
        Context applicationContext = context.getApplicationContext();
        this.c = applicationContext;
        this.e = configuration;
        e(applicationContext);
        Log.d("BaseOneTrackImp", "OneTrackImp init : " + configuration.toString());
        Log.d("BaseOneTrackImp", "OneTrackImp sdk ver : 3.1.3");
        com.xiaomi.onetrack.util.r.a("BaseOneTrackImp", "BuildConfig.Channel:global");
    }

    private void e(Context context) {
        s.a(this.e.isInternational(), this.e.getRegion(), this.e.getMode());
        OneTrack.Mode mode = this.e.getMode();
        OneTrack.Mode mode2 = OneTrack.Mode.APP;
        if (mode == mode2) {
            s.a(this.e.isOverrideMiuiRegionSetting());
        }
        if (b == null) {
            b = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue());
        }
        this.g = new x(this.e);
        a(context);
        m();
        if (this.e.getMode() == mode2) {
            b(context);
            if (this.e.isExceptionCatcherEnable()) {
                j.a(new d(this, context));
            }
        }
        b.execute(new o(this));
    }

    private void m() {
        Handler handler = new Handler(Looper.getMainLooper());
        this.i = handler;
        if (handler.getLooper() != null) {
            j.a(new z(this));
            this.i.postDelayed(new ab(this), ((long) (Math.random() * 1000.0d * 20.0d)) + 10000);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void n() {
        com.xiaomi.onetrack.util.r.a("BaseOneTrackImp", "triggerOTMonitorBusiness");
        j.a(new ac(this));
        Handler handler = this.i;
        if (handler == null || handler.getLooper() == null) {
            return;
        }
        this.i.postDelayed(new ad(this), 1800000L);
    }

    protected boolean b() {
        if (this.e.isOverrideMiuiRegionSetting()) {
            return TextUtils.equals(s.m(), this.e.getRegion());
        }
        return true;
    }

    public void a(String str, Map map) {
        b.execute(new af(this, str, map));
    }

    public void a(String str, String str2, String str3, String str4, String str5, long j, Map map, boolean z) {
        b.execute(new f(this, z, str3, map, str, str2, str5, str4, j));
    }

    protected void b(Context context) {
        ((Application) context).registerActivityLifecycleCallbacks(new m(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void o() {
        b.execute(new n(this));
    }

    protected void a(String str, boolean z) {
        b.execute(new p(this, str, z));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str, long j) {
        b.execute(new q(this, str, j));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e(boolean z) {
        b.execute(new r(this, z));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean e(String str) {
        boolean zA = t.a(str);
        if (!zA) {
            com.xiaomi.onetrack.util.r.b("BaseOneTrackImp", String.format("Invalid eventname: %s. Eventname can only consist of numbers, letters, underscores ,and can not start with a number or \"onetrack_\" or \"ot_\"", str));
        }
        return !zA;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public JSONObject g(String str) throws Throwable {
        try {
            JSONObject jSONObjectA = t.a((Map) null, false);
            String strA = com.xiaomi.onetrack.util.m.a(t.a(this.e));
            return t.a(jSONObjectA, !TextUtils.isEmpty(strA) ? new JSONObject(strA) : null);
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.b("BaseOneTrackImp", "getCommonProperty failed, e: " + e.getMessage());
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public JSONObject d(Map map) throws Throwable {
        try {
            JSONObject jSONObjectA = t.a(map, false);
            String strA = com.xiaomi.onetrack.util.m.a(t.a(this.e));
            return t.a(jSONObjectA, !TextUtils.isEmpty(strA) ? new JSONObject(strA) : null);
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.b("BaseOneTrackImp", "getCommonPropertyForException failed, e: " + e.getMessage());
            return null;
        }
    }

    public String e() throws OnMainThreadException {
        if (com.xiaomi.onetrack.util.y.a()) {
            throw new OnMainThreadException("Can't call this method on main thread");
        }
        return com.xiaomi.onetrack.util.q.a().b();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void q() {
        if (com.xiaomi.onetrack.c.j.d()) {
            b.execute(new y(this));
        }
    }

    protected void h() {
        try {
            if (!i() && this.e.getMode() == OneTrack.Mode.APP) {
                long jD = a.d();
                String strA = a(jD, a.c());
                String strV = com.xiaomi.onetrack.util.ac.v();
                if (TextUtils.isEmpty(strV)) {
                    com.xiaomi.onetrack.util.ac.i(strA);
                    return;
                }
                JSONObject jSONObject = new JSONObject(strV);
                long jOptLong = jSONObject.optLong(com.ot.pubsub.a.a.X);
                String strOptString = jSONObject.optString(com.ot.pubsub.a.a.Y);
                if (jOptLong != jD) {
                    b.a(this.e.getAppId(), com.ot.pubsub.a.a.j, "tr", "", 1);
                    com.xiaomi.onetrack.util.ac.i(strA);
                    j().a(com.ot.pubsub.a.a.j, am.a(jOptLong, strOptString, jD, a.f(), this.e, this.g));
                }
            }
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.b("BaseOneTrackImp", "trackUpgradeEvent error: " + e.toString());
            if (e instanceof JSONException) {
                b.a(this.e.getAppId(), com.ot.pubsub.a.a.j, "bu", "", 1);
                a(j(), this.e, this.g, com.ot.pubsub.a.a.j, "bu", "");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void r() {
        if (i()) {
            return;
        }
        try {
            long jCurrentTimeMillis = System.currentTimeMillis();
            long jB = com.xiaomi.onetrack.util.ac.B();
            if (jCurrentTimeMillis - jB > 0 && ae.a(jB)) {
                com.xiaomi.onetrack.util.r.a("BaseOneTrackImp", "trackMonitorEvent has been tracked today");
                return;
            }
            int i = 0;
            do {
                com.xiaomi.onetrack.h.c.a aVarB = com.xiaomi.onetrack.h.b.b.a().b();
                if (aVarB != null) {
                    j().a("ot_monitor", am.a(aVarB, this.e, this.g));
                    com.xiaomi.onetrack.h.b.b.a().a(aVarB.b(), aVarB.a());
                }
                i++;
                if (aVarB == null) {
                    break;
                }
            } while (i <= 100);
            com.xiaomi.onetrack.util.ac.j(System.currentTimeMillis());
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.b("BaseOneTrackImp", "trackMonitorEvent error: " + e.toString());
        }
    }

    private String a(long j, String str) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(com.ot.pubsub.a.a.Y, str);
        jSONObject.put(com.ot.pubsub.a.a.X, j);
        return jSONObject.toString();
    }

    public void a(OneTrack.IEventHook iEventHook) {
        this.g.a(iEventHook);
    }

    public boolean i() {
        if (!OneTrack.isDisable()) {
            return false;
        }
        com.xiaomi.onetrack.util.r.a("BaseOneTrackImp", "isDisable is true,Not allowed Track");
        return true;
    }

    protected boolean k() {
        if (com.xiaomi.onetrack.util.r.a) {
            com.xiaomi.onetrack.util.r.a("BaseOneTrackImp", "enable:" + l() + " isSupportEmptyEvent: " + s() + "_isSupportAdMonitor():" + t());
        }
        return l() && s() && t();
    }

    protected boolean l() {
        try {
            int componentEnabledSetting = a.b().getPackageManager().getComponentEnabledSetting(new ComponentName("com.miui.analytics", "com.miui.analytics.onetrack.OneTrackService"));
            return componentEnabledSetting == 1 || componentEnabledSetting == 0;
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.b("BaseOneTrackImp", "enable error:" + e.toString());
            return false;
        }
    }

    private static boolean s() {
        try {
            int i = a.b().getPackageManager().getPackageInfo("com.miui.analytics", 0).versionCode;
            if (i >= 2020062900) {
                return true;
            }
            com.xiaomi.onetrack.util.r.a("BaseOneTrackImp", "system analytics version: " + i);
        } catch (Throwable th) {
            com.xiaomi.onetrack.util.r.b("BaseOneTrackImp", "isSupportEmptyEvent error:" + th.getMessage());
        }
        return false;
    }

    private boolean t() {
        try {
            if (TextUtils.isEmpty(this.e.getAdEventAppId()) || OneTrack.isUseSystemNetTrafficOnly()) {
                return true;
            }
            int i = a.b().getPackageManager().getPackageInfo("com.miui.analytics", 0).versionCode;
            com.xiaomi.onetrack.util.r.a("BaseOneTrackImp", "system analytics version: " + i);
            return i >= 2022042900;
        } catch (Throwable th) {
            com.xiaomi.onetrack.util.r.b("BaseOneTrackImp", "isSupportAdMonitor error:" + th.getMessage());
            return true;
        }
    }

    public static void a(ao aoVar, Configuration configuration, x xVar, String str, String str2, String str3) {
        try {
            if (!com.xiaomi.onetrack.h.a.a.a().b(configuration.getAppId(), str)) {
                com.xiaomi.onetrack.util.r.a("BaseOneTrackImp", "trackFailedEvent, should not record");
            } else {
                aoVar.a("ot_failed", am.a(str, str2, str3, configuration, xVar));
                com.xiaomi.onetrack.h.a.a.a().c(configuration.getAppId(), str);
            }
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.b("BaseOneTrackImp", "trackFailedEvent error: " + e.toString());
        }
    }
}
