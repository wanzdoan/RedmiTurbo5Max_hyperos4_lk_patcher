package com.xiaomi.onetrack.util;

import android.text.TextUtils;
import com.xiaomi.onetrack.Configuration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public abstract class t {

    public interface a {
        boolean a(Object obj);
    }

    public static JSONObject a(Map map, boolean z) {
        return a(map, new u(z));
    }

    private static JSONObject a(Map map, a aVar) {
        JSONObject jSONObject = new JSONObject();
        if (map != null) {
            try {
                for (Map.Entry entry : map.entrySet()) {
                    String str = (String) entry.getKey();
                    Object value = entry.getValue();
                    if (!aVar.a(value)) {
                        if (r.a) {
                            a("ParamUtil", (String) entry.getKey());
                        }
                    } else if (b(value)) {
                        jSONObject.put(str, value);
                    } else if (value instanceof List) {
                        jSONObject.put(str, a((List) value));
                    }
                }
            } catch (Exception e) {
                r.b("ParamUtil", "checkParam error:" + e.toString());
                return jSONObject;
            }
        }
        return jSONObject;
    }

    public static JSONArray a(List list) throws JSONException {
        if (list == null) {
            return null;
        }
        JSONArray jSONArray = new JSONArray();
        for (Object obj : list) {
            if (b(obj)) {
                jSONArray.put(obj);
            } else if (obj instanceof Map) {
                JSONObject jSONObject = new JSONObject();
                boolean z = false;
                for (Map.Entry entry : ((Map) obj).entrySet()) {
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                    if ((key instanceof String) && b(value)) {
                        jSONObject.put((String) key, value);
                        z = true;
                    }
                }
                if (z) {
                    jSONArray.put(jSONObject);
                }
            }
        }
        return jSONArray;
    }

    public static void a(String str, String str2) {
        r.b(str, "key is " + str2 + ", the param value is invalid，please change the parameter type to string ,numeric, boolean！");
    }

    public static JSONObject a(JSONObject jSONObject, JSONObject jSONObject2) {
        if (jSONObject == null && jSONObject2 == null) {
            return new JSONObject();
        }
        if (jSONObject == null && jSONObject2 != null) {
            return jSONObject2;
        }
        if (jSONObject != null && jSONObject2 == null) {
            return jSONObject;
        }
        try {
            Iterator<String> itKeys = jSONObject.keys();
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                jSONObject2.put(next, jSONObject.opt(next));
            }
            return jSONObject2;
        } catch (Exception e) {
            r.b("ParamUtil", "merge error：" + e.toString());
            return jSONObject;
        }
    }

    private static boolean d(Object obj) {
        if (!(obj instanceof Number)) {
            return false;
        }
        Number number = (Number) obj;
        return (Double.isInfinite(number.doubleValue()) || Double.isNaN(number.doubleValue())) ? false : true;
    }

    public static boolean a(Object obj) {
        return b(obj) || (obj instanceof List);
    }

    public static boolean b(Object obj) {
        return (obj instanceof Boolean) || (obj instanceof String) || d(obj);
    }

    public static boolean a(String str) {
        if (!d(str)) {
            return false;
        }
        String lowerCase = str.toLowerCase();
        return (lowerCase.startsWith(com.ot.pubsub.util.m.a) || lowerCase.startsWith(com.ot.pubsub.util.m.b)) ? false : true;
    }

    private static boolean d(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (i == 0 && Character.isDigit(c)) {
                return false;
            }
            if (c != '_' && !Character.isDigit(c) && ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z'))) {
                return false;
            }
        }
        return true;
    }

    public static String a(Configuration configuration) {
        StringBuilder sb = new StringBuilder();
        String appId = configuration.getAppId();
        String pluginId = configuration.getPluginId();
        if (!TextUtils.isEmpty(appId)) {
            sb.append(appId);
        }
        if (!TextUtils.isEmpty(pluginId)) {
            sb.append(pluginId);
        }
        return com.xiaomi.onetrack.d.d.h(sb.toString());
    }

    public static boolean b(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String lowerCase = str.toLowerCase();
        return "ot_monitor".equals(lowerCase) || "ot_failed".equals(lowerCase);
    }
}
