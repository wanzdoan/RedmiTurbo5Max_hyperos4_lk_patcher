package com.ot.pubsub.g;

import android.text.TextUtils;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
final class k implements Runnable {
    final /* synthetic */ com.ot.pubsub.a.c a;

    k(com.ot.pubsub.a.c cVar) {
        this.a = cVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            List<JSONObject> listB = i.b();
            com.ot.pubsub.util.j.a("NetworkAccessManager", "jsonData :" + listB);
            if (listB != null && listB.size() > 0) {
                for (JSONObject jSONObject : listB) {
                    String strOptString = jSONObject.optString("projectid");
                    String strOptString2 = jSONObject.optString("topic");
                    String strOptString3 = jSONObject.optString(i.f);
                    String strOptString4 = jSONObject.optString("data");
                    Map mapA = com.ot.pubsub.util.c.a(jSONObject.optString(i.h));
                    com.ot.pubsub.util.j.a("NetworkAccessManager", "attributes values : " + mapA.toString());
                    boolean zOptBoolean = jSONObject.optBoolean(i.i, false);
                    boolean zIsEmpty = TextUtils.isEmpty(strOptString3);
                    com.ot.pubsub.a.c cVar = this.a;
                    if (zIsEmpty) {
                        cVar.a(strOptString, strOptString2, strOptString4, mapA, zOptBoolean);
                    } else {
                        cVar.a(strOptString, strOptString2, strOptString3, strOptString4, mapA);
                    }
                }
            }
            i.b(true);
        } catch (Exception e) {
            com.ot.pubsub.util.j.b("NetworkAccessManager", "cta event error: " + e.toString());
        }
        boolean unused = i.o = false;
    }
}
