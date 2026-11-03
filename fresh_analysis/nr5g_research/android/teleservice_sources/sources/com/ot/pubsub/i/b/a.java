package com.ot.pubsub.i.b;

import android.text.TextUtils;
import com.ot.pubsub.util.j;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class a {
    private static final int h = 0;
    public int a;
    public String b;
    public String c;
    public String d;
    public int e;
    public long f;
    public String g;

    public static a a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        a aVar = new a();
        try {
            JSONObject jSONObject = new JSONObject(str);
            aVar.a = jSONObject.optInt(com.ot.pubsub.i.a.a.d);
            String strOptString = jSONObject.optString(com.ot.pubsub.i.a.a.c);
            aVar.b = strOptString;
            if (aVar.a == 0) {
                JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("data");
                if (jSONObjectOptJSONObject != null) {
                    String strOptString2 = jSONObjectOptJSONObject.optString("accessToken");
                    if (!TextUtils.isEmpty(strOptString2)) {
                        aVar.d = com.ot.pubsub.c.a.c(strOptString2, com.ot.pubsub.c.a.a);
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
                }
                aVar.g = jSONObject.toString();
                return aVar;
            }
            if (!TextUtils.isEmpty(strOptString)) {
                j.b("TokenBean", "token exception response :" + aVar.b);
            }
            return aVar;
        } catch (Exception unused) {
        }
    }

    public String toString() {
        return "TokenBean{code=" + this.a + ", msg='" + this.b + "', data='" + this.c + "', access_token='" + this.d + "', expires=" + this.e + ", local_time=" + this.f + ", response='" + this.g + "'}";
    }
}
