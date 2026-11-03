package com.xiaomi.onetrack.api;

import android.os.Process;
import android.text.TextUtils;
import com.ot.pubsub.a.a;
import com.xiaomi.onetrack.Configuration;
import com.xiaomi.onetrack.OneTrack;
import com.xiaomi.onetrack.c.s;
import com.xiaomi.onetrack.h.d.b;
import com.xiaomi.onetrack.util.j;
import com.xiaomi.onetrack.util.x;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class av implements ao, bd.a {
    private final ConcurrentHashMap e = new ConcurrentHashMap();
    private Configuration f;
    private bd g;
    private x h;

    public av(Configuration configuration, x xVar) {
        this.f = configuration;
        this.h = xVar;
        bd bdVarA = bd.a();
        this.g = bdVarA;
        bdVarA.a(this);
        j.a(new aw(this));
    }

    @Override // com.xiaomi.onetrack.api.ao
    public void a(String str, String str2) {
        boolean zA = a(str2);
        x xVar = this.h;
        if (xVar != null && !xVar.a(str) && !zA) {
            com.xiaomi.onetrack.util.r.a("OneTrackSystemImp", "The privacy policy is not permitted, and the event is not basic or recommend event or custom dau event, skip it.");
            return;
        }
        if (!a(str, str2, zA)) {
            b.a(this.f.getAppId(), str, "se", "r1", 1);
            c.a(this, this.f, this.h, str, "se", "r1");
            return;
        }
        if (!com.xiaomi.onetrack.c.j.b()) {
            if (!a.i.equalsIgnoreCase(str)) {
                com.xiaomi.onetrack.c.j.a(str, str2);
                return;
            }
        } else {
            com.xiaomi.onetrack.c.j.a(this);
        }
        if (com.xiaomi.onetrack.util.r.a) {
            com.xiaomi.onetrack.util.r.a("OneTrackSystemImp", "track name:" + str + " data :" + str2 + " tid" + Process.myTid());
        }
        String appId = this.f.getAppId();
        if (zA) {
            appId = this.f.getAdEventAppId();
        }
        if (this.g.a(str, str2, appId)) {
            return;
        }
        if (com.xiaomi.onetrack.util.r.a) {
            com.xiaomi.onetrack.util.r.a("OneTrackSystemImp", "track mIOneTrackService is null! SystemImpCacheManager cache data:" + str2);
        }
        if (!"ot_failed".equals(str)) {
            s.a().a(appId, str, str2);
        }
        b.a(this.f.getAppId(), str, "se", "r2", 1);
    }

    private boolean a(String str, String str2, boolean z) {
        if (OneTrack.isDisable()) {
            return false;
        }
        boolean z2 = str != null && str.equals(a.f);
        if (z || z2) {
            if (str2 != null && str2.length() > 512000) {
                com.xiaomi.onetrack.util.r.a("OneTrackSystemImp", "ad Event size exceed limitation!");
                return false;
            }
        } else if (str2 != null && str2.length() * 2 > 102400) {
            com.xiaomi.onetrack.util.r.a("OneTrackSystemImp", "Event size exceed limitation!");
            return false;
        }
        return true;
    }

    @Override // com.xiaomi.onetrack.api.ao
    public void a(int i) {
        this.g.a(i);
    }

    @Override // com.xiaomi.onetrack.api.ao
    public void a(boolean z) {
        if (ar.h) {
            com.xiaomi.onetrack.c.j.a(this);
        }
    }

    @Override // com.xiaomi.onetrack.api.bd.a
    public void a() {
        b();
    }

    private void b() {
        try {
            s.a().c(this.f.getAppId());
            if (TextUtils.isEmpty(this.f.getAdEventAppId())) {
                return;
            }
            s.a().c(this.f.getAdEventAppId());
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.a("OneTrackSystemImp", "trackCachedEvents: " + e.toString());
        }
    }

    private boolean a(String str) {
        try {
            return new JSONObject(str).optJSONObject(com.ot.pubsub.a.b.b).optBoolean(com.xiaomi.onetrack.f.b.AbstractC0020b.D, false);
        } catch (Throwable unused) {
            com.xiaomi.onetrack.util.r.a("OneTrackSystemImp", "");
            return false;
        }
    }
}
