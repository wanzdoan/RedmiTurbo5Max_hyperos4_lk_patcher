package com.xiaomi.onetrack.b;

import android.text.TextUtils;
import com.xiaomi.onetrack.d.f;
import com.xiaomi.onetrack.util.r;
import com.xiaomi.onetrack.util.s;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public abstract class o {
    private static String a = "ConfigProvider";
    private static volatile boolean b = false;
    private static volatile boolean c = true;

    public static boolean a() {
        try {
            String[] strArrB = f.a().b();
            return (!TextUtils.isEmpty(strArrB[0]) && !TextUtils.isEmpty(strArrB[1])) && !s.a(a);
        } catch (Exception e) {
            r.a(a, "ConfigProvider.available", e);
            return false;
        }
    }

    public static int a(int i) {
        int iIntValue;
        if (r.b) {
            r.a(a, "debug upload mode, send events immediately");
            return 0;
        }
        try {
            iIntValue = ((Integer) g.c().get(Integer.valueOf(i + 1))).intValue();
        } catch (Exception unused) {
            iIntValue = 60000;
        }
        r.a(a, "getUploadInterval " + iIntValue);
        return iIntValue;
    }

    public static String a(JSONObject jSONObject, String str, String str2, String str3) {
        if (jSONObject != null) {
            try {
                if (jSONObject.has(str2)) {
                    return jSONObject.optString(str2);
                }
            } catch (Exception e) {
                r.c(a, "getEventString error: " + e.getMessage());
                return str3;
            }
        }
        a("getEventString", str, str2);
        return str3;
    }

    public static JSONArray a(JSONObject jSONObject, String str, String str2, JSONArray jSONArray) {
        if (jSONObject != null) {
            try {
                if (jSONObject.has(str2)) {
                    return jSONObject.optJSONArray(str2);
                }
            } catch (Exception e) {
                r.c(a, "getEventJSONArray error: " + e.getMessage());
                return jSONArray;
            }
        }
        a("getEventJSONArray", str, str2);
        return jSONArray;
    }

    public static boolean a(JSONObject jSONObject, String str, String str2, boolean z) {
        if (jSONObject != null) {
            try {
                if (jSONObject.has(str2)) {
                    return jSONObject.optBoolean(str2);
                }
            } catch (Exception e) {
                r.c(a, "getEventBoolean error: " + e.getMessage());
                return z;
            }
        }
        a("getEventBoolean", str, str2);
        return z;
    }

    public static boolean a(String str) {
        return j.a().e(str) == 2;
    }

    public static boolean b(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return j.a().b(str, "wl");
    }

    public static boolean c(String str) {
        try {
            return j.a().b(str, com.ot.pubsub.b.a.a);
        } catch (Exception e) {
            r.b(a, "isDisableTrackForApp error: " + e.toString());
            return false;
        }
    }

    public static synchronized void a(boolean z) {
        b = z;
    }

    public static synchronized boolean b() {
        return b;
    }

    public static void b(boolean z) {
        c = z;
    }

    public static boolean c() {
        return c;
    }

    private static void a(String str, String str2, String str3) {
        r.a(a, str + " config not available, use default value eventName:" + str2 + " key:" + str3);
    }
}
