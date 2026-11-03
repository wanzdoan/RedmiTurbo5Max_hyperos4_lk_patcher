package com.xiaomi.onetrack.util;

import android.text.TextUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public abstract class a {
    public static final Set n = new HashSet(Arrays.asList("AT", "BE", "BG", "HR", "CY", "CZ", "DK", "EE", "FI", "FR", "DE", "EL", "GR", "HU", "IS", "IE", "IT", "LV", "LI", "LT", "LU", "MT", "NL", "NO", "PL", "PT", "RO", "SK", "SI", "ES", "SE", "UK", "GB"));
    public static final Set o = new HashSet(Arrays.asList(com.ot.pubsub.g.l.a, com.ot.pubsub.g.l.b, "CU", "IR", "KP", "SD", "SY", "TR"));

    public static String a() {
        try {
            String strL = s.l();
            JSONObject jSONObject = new JSONObject();
            if (!TextUtils.isEmpty(strL)) {
                jSONObject.put(com.ot.pubsub.util.a.d, strL);
                if (n.contains(strL.toUpperCase())) {
                    jSONObject.put(com.ot.pubsub.util.a.e, com.ot.pubsub.util.a.f);
                }
            }
            return jSONObject.toString();
        } catch (Exception e) {
            r.a("Constants", "getDefaultAttributes exception:" + e.getMessage());
            return "";
        }
    }

    public static Map b() {
        HashMap map = new HashMap();
        try {
            String strL = s.l();
            if (!TextUtils.isEmpty(strL)) {
                map.put(com.ot.pubsub.util.a.d, strL);
                if (n.contains(strL.toUpperCase())) {
                    map.put(com.ot.pubsub.util.a.e, com.ot.pubsub.util.a.f);
                    return map;
                }
            }
            return map;
        } catch (Exception e) {
            r.a("Constants", "getDefaultAttributes exception:" + e.getMessage());
            return map;
        }
    }

    public static String a(String str) {
        if (r.a) {
            r.a("Constants", "getDefaultTopic：ot_" + str + "_" + s.n());
        }
        return com.ot.pubsub.util.m.b + str + "_" + s.n();
    }
}
