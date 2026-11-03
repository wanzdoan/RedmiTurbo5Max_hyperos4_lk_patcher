package com.xiaomi.onetrack.e;

import com.xiaomi.onetrack.b.j;
import com.xiaomi.onetrack.util.r;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class b extends com.xiaomi.onetrack.f.b {
    public b(String str, String str2, String str3, String str4) {
        try {
            b(str);
            d(str3);
            c(str2);
            b(System.currentTimeMillis());
            b(new JSONObject(str4));
            a(j.a().a(str, str3, com.ot.pubsub.b.a.d, 1));
        } catch (Exception e) {
            r.b("CustomEvent", "CustomEvent error:" + e.toString());
        }
    }
}
