package com.ot.pubsub.util;

import com.ot.pubsub.Configuration;
import com.ot.pubsub.PubSubTrack;

/* JADX INFO: loaded from: classes.dex */
public class q {
    private static final String a = "custom_open";
    private static final String b = "custom_close";
    private static final String c = "exprience_open";
    private static final String d = "exprience_close";
    private static final String e = "PrivacyManager";
    private static final long k = 900000;
    private PubSubTrack.IEventHook f;
    private Configuration g;
    private boolean h;
    private boolean i;
    private long j = 0;

    public q(Configuration configuration) {
        this.g = configuration;
        this.h = t.d(m.a(configuration));
    }

    public boolean a(String str) {
        boolean zB;
        if (this.g.isUseCustomPrivacyPolicy()) {
            j.a(e, "use custom privacy policy, the policy is ".concat(this.h ? "open" : "close"));
            zB = this.h;
        } else {
            zB = b();
            j.a(e, "use system experience plan, the policy is ".concat(zB ? "open" : "close"));
        }
        if (zB) {
            return zB;
        }
        boolean zB2 = b(str);
        boolean zC = c(str);
        boolean zD = d(str);
        StringBuilder sb = new StringBuilder("This event ");
        sb.append(str);
        sb.append(zB2 ? " is " : " is not ");
        sb.append("basic event and ");
        sb.append(zC ? "is" : "is not");
        sb.append(" recommend event and ");
        sb.append(zD ? "is" : "is not");
        sb.append(" custom dau event");
        j.a(e, sb.toString());
        return zB2 || zC || zD;
    }

    private boolean b(String str) {
        return "onetrack_dau".equals(str) || com.ot.pubsub.a.a.g.equals(str);
    }

    private boolean c(String str) {
        PubSubTrack.IEventHook iEventHook = this.f;
        return iEventHook != null && iEventHook.isRecommendEvent(str);
    }

    private boolean d(String str) {
        PubSubTrack.IEventHook iEventHook = this.f;
        return iEventHook != null && iEventHook.isCustomDauEvent(str);
    }

    public void a(PubSubTrack.IEventHook iEventHook) {
        this.f = iEventHook;
    }

    public void a(boolean z) {
        this.h = z;
    }

    public String a() {
        if (this.g.isUseCustomPrivacyPolicy()) {
            if (this.h) {
                return a;
            }
            return b;
        }
        if (b()) {
            return c;
        }
        return d;
    }

    private boolean b() {
        if (Math.abs(System.currentTimeMillis() - this.j) > k) {
            this.j = System.currentTimeMillis();
            this.i = l.a(b.b());
        }
        return this.i;
    }
}
