package com.xiaomi.onetrack.a.b;

import android.text.TextUtils;
import com.xiaomi.onetrack.b.j;
import com.xiaomi.onetrack.util.r;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class b extends com.xiaomi.onetrack.f.b {
    private List b = null;

    public b(String str, String str2, String str3, String str4) {
        try {
            b(str);
            d(str3);
            c(str2);
            b(System.currentTimeMillis());
            JSONObject jSONObject = new JSONObject(str4);
            JSONObject jSONObject2 = jSONObject.getJSONObject(com.ot.pubsub.a.b.b);
            b(jSONObject);
            a(j.a().a(str, str3, com.ot.pubsub.b.a.d, 0));
            a(jSONObject2);
            c(jSONObject2);
        } catch (Exception e) {
            r.b("OTAdEvent", "CustomEvent error:" + e.toString());
        }
    }

    public List a() {
        return this.b;
    }

    public void a(JSONObject jSONObject) {
        if (jSONObject == null) {
            return;
        }
        try {
            JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray(com.xiaomi.onetrack.f.b.AbstractC0020b.F);
            int length = jSONArrayOptJSONArray != null ? jSONArrayOptJSONArray.length() : 0;
            String string = "";
            if (j()) {
                string = UUID.randomUUID().toString();
                jSONObject.put(com.xiaomi.onetrack.f.b.AbstractC0020b.J, string);
                jSONObject.put(com.xiaomi.onetrack.f.b.AbstractC0020b.K, length);
            }
            if (jSONArrayOptJSONArray == null || jSONArrayOptJSONArray.length() <= 0) {
                return;
            }
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                String strOptString = jSONArrayOptJSONArray.optString(i);
                if (!TextUtils.isEmpty(strOptString)) {
                    if (strOptString.contains("api.ad.xiaomi.com") && !strOptString.contains("_sn_")) {
                        strOptString = strOptString.contains("?") ? strOptString + "&_sn_=" + UUID.randomUUID().toString() : strOptString + "?_sn_=" + UUID.randomUUID().toString();
                    }
                    a aVar = new a();
                    aVar.c(c());
                    aVar.a(e());
                    aVar.a(h());
                    aVar.d(d());
                    aVar.b(strOptString);
                    aVar.e(string);
                    aVar.c(length);
                    aVar.d(i + 1);
                    arrayList.add(aVar);
                }
            }
            this.b = arrayList;
        } catch (Throwable th) {
            r.a("OTAdEvent", "parseAdMonitor Throwable:" + th.getMessage());
        }
    }

    private boolean j() {
        if (!com.xiaomi.onetrack.a.a.a().f) {
            return false;
        }
        ConcurrentSkipListSet concurrentSkipListSet = com.xiaomi.onetrack.a.a.a().a;
        if (concurrentSkipListSet != null && !concurrentSkipListSet.isEmpty() && !concurrentSkipListSet.contains(d())) {
            return false;
        }
        ConcurrentSkipListSet concurrentSkipListSet2 = com.xiaomi.onetrack.a.a.a().b;
        return concurrentSkipListSet2 == null || concurrentSkipListSet2.isEmpty() || concurrentSkipListSet2.contains(e());
    }

    private void c(JSONObject jSONObject) {
        if (jSONObject != null && jSONObject.has(com.xiaomi.onetrack.f.b.AbstractC0020b.F)) {
            jSONObject.remove(com.xiaomi.onetrack.f.b.AbstractC0020b.F);
        }
    }
}
