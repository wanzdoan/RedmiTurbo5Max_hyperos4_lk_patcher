package com.xiaomi.onetrack.api;

import android.content.Context;
import com.xiaomi.onetrack.Configuration;
import com.xiaomi.onetrack.f.a;
import com.xiaomi.onetrack.util.s;

/* JADX INFO: loaded from: classes2.dex */
public class ar extends c {
    public static volatile boolean h = false;
    private ao j;

    public ar(Context context, Configuration configuration) {
        super(context, configuration);
    }

    @Override // com.xiaomi.onetrack.api.c
    protected void a(Context context) {
        if (s.b(null)) {
            h = true;
            if (s.b() && k() && b()) {
                this.a = new av(this.e, this.g);
            } else {
                this.a = new as(context, this.e, this.g);
            }
            ah.a().a(this.a);
            return;
        }
        h = false;
        if (s.b() && m() && b()) {
            this.j = new ax(this.e, this.g);
        } else {
            this.j = new az(this.e, this.g);
        }
        ah.a().a(this.j);
    }

    @Override // com.xiaomi.onetrack.api.c
    protected boolean a() {
        if (h) {
            return s.b() && k() && b();
        }
        return s.b() && m() && b();
    }

    private boolean m() {
        if (com.xiaomi.onetrack.util.r.a) {
            com.xiaomi.onetrack.util.r.a("OneTrackImp", "enable:" + l() + " isSupportPBSystem: " + n());
        }
        return l() && n();
    }

    private static boolean n() {
        try {
            int i = a.b().getPackageManager().getPackageInfo("com.miui.analytics", 0).versionCode;
            if (i >= 2024040800) {
                return true;
            }
            com.xiaomi.onetrack.util.r.a("OneTrackImp", "system analytics version: " + i);
        } catch (Throwable th) {
            com.xiaomi.onetrack.util.r.b("OneTrackImp", "isSupportEmptyEvent error:" + th.getMessage());
        }
        return false;
    }

    @Override // com.xiaomi.onetrack.api.c
    protected ao j() {
        if (h) {
            if (this.a == null) {
                a(this.c);
            }
            com.xiaomi.onetrack.util.r.a("OneTrackImp", "getTrackImp mIOneTrack" + this.a);
            return this.a;
        }
        if (this.j == null) {
            a(this.c);
        }
        com.xiaomi.onetrack.util.r.a("OneTrackImp", "getTrackImp mIPubSubTrack" + this.j);
        return this.j;
    }

    public static void a(String str, String str2) {
        boolean zB = s.b(str2);
        if (h != zB) {
            h = zB;
            com.xiaomi.onetrack.util.r.a("OneTrackImp", "channel changed to ".concat(h ? "OneTrack" : "PubSub"));
        }
        com.xiaomi.onetrack.c.z.a().a(str, zB);
    }
}
