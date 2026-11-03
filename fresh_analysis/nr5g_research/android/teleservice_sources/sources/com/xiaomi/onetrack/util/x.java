package com.xiaomi.onetrack.util;

import com.xiaomi.onetrack.Configuration;
import com.xiaomi.onetrack.OneTrack;

/* JADX INFO: loaded from: classes2.dex */
public class x {
    private OneTrack.IEventHook f;
    private Configuration g;
    private Boolean h;
    private boolean i;
    private boolean j = false;
    private long k = 0;

    public x(Configuration configuration) {
        this.g = configuration;
    }

    private boolean c() {
        if (this.h == null) {
            this.h = Boolean.valueOf(ac.j(t.a(this.g)));
        }
        return this.h.booleanValue();
    }

    public boolean a(String str) {
        boolean zD;
        if (this.g.isUseCustomPrivacyPolicy()) {
            r.a("PrivacyManager", "use custom privacy policy, the policy is ".concat(c() ? "open" : "close"));
            zD = c();
        } else {
            zD = d();
            r.a("PrivacyManager", "use system experience plan, the policy is ".concat(zD ? "open" : "close"));
        }
        if (zD) {
            return zD;
        }
        boolean zC = c(str);
        boolean zB = b(str);
        boolean zD2 = d(str);
        StringBuilder sb = new StringBuilder("This event ");
        sb.append(str);
        sb.append(zC ? " is " : " is not ");
        sb.append("basic event and ");
        sb.append(zB ? "is" : "is not");
        sb.append(" recommend event and ");
        sb.append(zD2 ? "is" : "is not");
        sb.append(" custom dau event");
        r.a("PrivacyManager", sb.toString());
        return zC || zB || zD2;
    }

    private boolean c(String str) {
        return "onetrack_dau".equals(str) || com.ot.pubsub.a.a.g.equals(str);
    }

    public boolean b(String str) {
        OneTrack.IEventHook iEventHook = this.f;
        return iEventHook != null && iEventHook.isRecommendEvent(str);
    }

    private boolean d(String str) {
        OneTrack.IEventHook iEventHook = this.f;
        return iEventHook != null && iEventHook.isCustomDauEvent(str);
    }

    public void a(OneTrack.IEventHook iEventHook) {
        this.f = iEventHook;
    }

    public String a() {
        if (this.g.isUseCustomPrivacyPolicy()) {
            if (c()) {
                return "custom_open";
            }
            return "custom_close";
        }
        if (d()) {
            return "exprience_open";
        }
        return "exprience_close";
    }

    private boolean d() {
        if (Math.abs(System.currentTimeMillis() - this.k) > 900000) {
            this.k = System.currentTimeMillis();
            this.i = s.a(com.xiaomi.onetrack.f.a.b());
        }
        return this.i;
    }

    public boolean b() {
        return this.j;
    }
}
