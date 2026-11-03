package com.xiaomi.onetrack.b;

import android.os.Message;
import android.text.TextUtils;
import com.xiaomi.onetrack.util.r;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
class c implements Runnable {
    final /* synthetic */ JSONObject a;
    final /* synthetic */ a b;

    c(a aVar, JSONObject jSONObject) {
        this.b = aVar;
        this.a = jSONObject;
    }

    @Override // java.lang.Runnable
    public void run() {
        r.a("AppConfigUpdater", "checkAppConfigVersion start");
        JSONArray jSONArrayOptJSONArray = this.a.optJSONArray(com.ot.pubsub.j.d.a);
        if (jSONArrayOptJSONArray != null) {
            for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                JSONObject jSONObjectOptJSONObject = jSONArrayOptJSONArray.optJSONObject(i);
                String strOptString = jSONObjectOptJSONObject == null ? "" : jSONObjectOptJSONObject.optString(com.ot.pubsub.j.d.b);
                r.a("AppConfigUpdater", "appId: " + strOptString);
                if (!TextUtils.isEmpty(strOptString)) {
                    int iOptInt = jSONObjectOptJSONObject == null ? 0 : jSONObjectOptJSONObject.optInt("version");
                    int iD = j.a().d(strOptString);
                    boolean zH = a.h(strOptString);
                    boolean z = a.G.containsKey(strOptString) && ((Boolean) a.G.get(strOptString)).booleanValue();
                    r.a("AppConfigUpdater", "local version: " + iD + ", server version: " + iOptInt + ", canUpdate: " + zH + ", isScheduling: " + z);
                    if (iOptInt > 0 && iOptInt > iD && zH && !z) {
                        Message messageObtain = Message.obtain();
                        messageObtain.what = 0;
                        messageObtain.obj = strOptString;
                        long jRandom = (long) (Math.random() * 1800000.0d);
                        r.a("AppConfigUpdater", "the message will be handled after " + jRandom + " ms");
                        a.E.sendMessageDelayed(messageObtain, jRandom);
                        a.G.put(strOptString, Boolean.TRUE);
                    }
                }
            }
        }
    }
}
