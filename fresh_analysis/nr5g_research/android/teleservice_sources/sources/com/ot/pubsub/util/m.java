package com.ot.pubsub.util;

import android.text.TextUtils;
import com.ot.pubsub.Configuration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class m {
    public static final String a = "onetrack_";
    public static final String b = "ot_";
    private static final String c = "ParamUtil";

    public interface a {
        boolean a(Object obj);
    }

    public static JSONObject a(Map map, boolean z) {
        return a(map, new n(z));
    }

    public static JSONObject a(Map map) {
        return a(map, new o());
    }

    private static JSONObject a(Map map, a aVar) {
        JSONObject jSONObject = new JSONObject();
        if (map != null) {
            try {
                for (Map.Entry entry : map.entrySet()) {
                    String str = (String) entry.getKey();
                    Object value = entry.getValue();
                    if (!aVar.a(value)) {
                        if (j.a) {
                            a(c, (String) entry.getKey());
                        }
                    } else if (b(value)) {
                        jSONObject.put(str, value);
                    } else if (value instanceof List) {
                        jSONObject.put(str, a((List) value));
                    }
                }
            } catch (Exception e) {
                j.b(c, "checkParam error:" + e.toString());
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
        j.b(str, "key is " + str2 + ", the param value is invalid，please change the parameter type to string ,numeric, boolean！");
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
            j.b(c, "merge error：" + e.toString());
            return jSONObject;
        }
    }

    private static boolean b(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean d(Object obj) {
        return obj instanceof Number;
    }

    public static boolean a(Object obj) {
        return b(obj) || (obj instanceof List);
    }

    public static boolean b(Object obj) {
        return (obj instanceof Boolean) || (obj instanceof Number) || (obj instanceof String);
    }

    public static boolean a(String str) {
        if (!c(str)) {
            return false;
        }
        String lowerCase = str.toLowerCase();
        return (lowerCase.startsWith(a) || lowerCase.startsWith(b)) ? false : true;
    }

    private static boolean c(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c2 = charArray[i];
            if (i == 0 && Character.isDigit(c2)) {
                return false;
            }
            if (c2 != '_' && !Character.isDigit(c2) && ((c2 < 'a' || c2 > 'z') && (c2 < 'A' || c2 > 'Z'))) {
                return false;
            }
        }
        return true;
    }

    public static String a(Configuration configuration) {
        StringBuilder sb = new StringBuilder();
        String appId = configuration.getAppId();
        if (!TextUtils.isEmpty(appId)) {
            sb.append(appId);
        }
        return com.ot.pubsub.c.d.h(sb.toString());
    }
}
