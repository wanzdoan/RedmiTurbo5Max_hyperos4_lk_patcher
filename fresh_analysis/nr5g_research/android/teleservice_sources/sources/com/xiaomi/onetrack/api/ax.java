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
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class ax implements ao, bd.a {
    private Configuration e;
    private bd f;
    private x g;

    public ax(Configuration configuration, x xVar) {
        this.e = configuration;
        this.g = xVar;
        bd bdVarA = bd.a();
        this.f = bdVarA;
        bdVarA.a(this);
        j.a(new ay(this, configuration));
    }

    @Override // com.xiaomi.onetrack.api.ao
    public void a(String str, String str2) {
        boolean zA = a(str2);
        x xVar = this.g;
        if (xVar != null && !xVar.a(str) && !zA) {
            com.xiaomi.onetrack.util.r.a("PubSubSystemImp", "The privacy policy is not permitted, and the event is not basic or recommend event or custom dau event, skip it.");
            return;
        }
        if (!a(str, str2, zA)) {
            b.a(this.e.getAppId(), str, "se", "r1", 1);
            c.a(this, this.e, this.g, str, "se", "r1");
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
            com.xiaomi.onetrack.util.r.a("PubSubSystemImp", "track name:" + str + " data :" + str2 + " tid" + Process.myTid());
        }
        String appId = this.e.getAppId();
        if (zA) {
            appId = this.e.getAdEventAppId();
        }
        String str3 = appId;
        String strA = com.xiaomi.onetrack.util.a.a(str3);
        String strA2 = com.xiaomi.onetrack.util.a.a();
        if (this.f.a(this.e.getProjectId(), strA, this.e.getPrivateKeyId(), str3, str, str2, strA2)) {
            return;
        }
        if (com.xiaomi.onetrack.util.r.a) {
            com.xiaomi.onetrack.util.r.a("PubSubSystemImp", "track mIOneTrackService is null! SystemImpCacheManager cache data:" + str2);
        }
        if (!"ot_failed".equals(str)) {
            s.a().a(str3, str, str2, strA2);
        }
        b.a(this.e.getAppId(), str, "se", "r2", 1);
    }

    private boolean a(String str, String str2, boolean z) {
        if (OneTrack.isDisable()) {
            com.xiaomi.onetrack.util.r.a("PubSubSystemImp", "isDisable");
            return false;
        }
        Configuration configuration = this.e;
        if (configuration == null || TextUtils.isEmpty(configuration.getProjectId()) || TextUtils.isEmpty(this.e.getPrivateKeyId())) {
            com.xiaomi.onetrack.util.r.a("PubSubSystemImp", "projectId or pKeyId is empty, so return");
            return false;
        }
        if (str != null && str.equals(a.f)) {
            return true;
        }
        if (z) {
            if (str2 != null && str2.length() > 512000) {
                com.xiaomi.onetrack.util.r.a("PubSubSystemImp", "ad Event size exceed limitation!");
                return false;
            }
        } else if (str2 != null && str2.length() * 2 > 102400) {
            com.xiaomi.onetrack.util.r.a("PubSubSystemImp", "Event size exceed limitation!");
            return false;
        }
        return true;
    }

    @Override // com.xiaomi.onetrack.api.ao
    public void a(int i) {
        this.f.a(i);
    }

    @Override // com.xiaomi.onetrack.api.ao
    public void a(boolean z) {
        if (ar.h) {
            return;
        }
        com.xiaomi.onetrack.c.j.a(this);
    }

    @Override // com.xiaomi.onetrack.api.bd.a
    public void a() {
        b();
    }

    private void b() {
        try {
            s.a().b(this.e.getProjectId(), this.e.getPrivateKeyId(), this.e.getAppId());
            if (TextUtils.isEmpty(this.e.getAdEventAppId())) {
                return;
            }
            s.a().b(this.e.getProjectId(), this.e.getPrivateKeyId(), this.e.getAdEventAppId());
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.a("PubSubSystemImp", "trackCachedEvents: " + e.toString());
        }
    }

    private boolean a(String str) {
        try {
            return new JSONObject(str).optJSONObject(com.ot.pubsub.a.b.b).optBoolean(com.xiaomi.onetrack.f.b.AbstractC0020b.D, false);
        } catch (Throwable unused) {
            com.xiaomi.onetrack.util.r.a("PubSubSystemImp", "");
            return false;
        }
    }
}
