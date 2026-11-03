package com.xiaomi.onetrack.i;

import android.text.TextUtils;
import com.xiaomi.onetrack.util.r;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class a {
    public int a;
    public String b;
    public String c;
    public String d;
    public int e;
    public long f;
    public String g;
    public boolean h = false;

    public static a a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            a aVar = new a();
            JSONObject jSONObject = new JSONObject(str);
            aVar.a = jSONObject.optInt(com.ot.pubsub.i.a.a.d);
            String strOptString = jSONObject.optString(com.ot.pubsub.i.a.a.c);
            aVar.b = strOptString;
            if (aVar.a == 0) {
                JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("data");
                if (jSONObjectOptJSONObject != null) {
                    String strOptString2 = jSONObjectOptJSONObject.optString("accessToken");
                    if (!TextUtils.isEmpty(strOptString2)) {
                        aVar.d = com.xiaomi.onetrack.d.a.c(strOptString2, com.xiaomi.onetrack.d.a.a);
                        aVar.e = jSONObjectOptJSONObject.optInt("expires");
                        long jOptLong = jSONObjectOptJSONObject.optLong("local_time", 0L);
                        if (jOptLong == 0) {
                            long jCurrentTimeMillis = System.currentTimeMillis();
                            aVar.f = jCurrentTimeMillis;
                            jSONObjectOptJSONObject.put("local_time", jCurrentTimeMillis);
                        } else {
                            aVar.f = jOptLong;
                        }
                    }
                    if (TextUtils.isEmpty(strOptString2) || TextUtils.isEmpty(aVar.d)) {
                        r.b("TokenBean", "parseResponseData response token is null");
                        return null;
                    }
                }
                aVar.g = jSONObject.toString();
                return aVar;
            }
            if (!TextUtils.isEmpty(strOptString)) {
                r.b("TokenBean", "token exception response :" + aVar.b);
            }
            return null;
        } catch (Exception e) {
            r.b("TokenBean", "parseResponseData  e:" + e);
        }
    }

    public String toString() {
        return "TokenBean{code=" + this.a + ", msg='" + this.b + "', data='" + this.c + "', access_token='" + this.d + "', expires=" + this.e + ", local_time=" + this.f + ", response='" + this.g + "'}";
    }
}
