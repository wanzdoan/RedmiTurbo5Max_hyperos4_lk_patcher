package com.xiaomi.onetrack.c;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public abstract class r {
    private static String b = "";
    private static final byte[] c = {104, 116, 116, 112, 115, 58, 47, 47, 112, 117, 98, 115, 117, 98, 46, 103, 111, 111, 103, 108, 101, 97, 112, 105, 115, 46, 99, 111, 109};

    public static void a(q qVar, com.xiaomi.onetrack.i.a aVar) {
        try {
            for (Map.Entry entry : d(qVar.a).entrySet()) {
                String str = (String) entry.getKey();
                List list = (List) entry.getValue();
                if (list != null && list.size() != 0) {
                    for (Map.Entry entry2 : e(list).entrySet()) {
                        String str2 = (String) entry2.getKey();
                        List list2 = (List) entry2.getValue();
                        if (list2 != null && list2.size() != 0 && a(str, str2, list2, aVar)) {
                            a(list2);
                        }
                    }
                }
            }
        } catch (Throwable unused) {
        }
    }

    private static void a(List list) {
        try {
            HashMap map = new HashMap();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                JSONObject jSONObjectOptJSONObject = new JSONObject(((m) it.next()).e()).optJSONObject(com.ot.pubsub.a.b.b);
                if (jSONObjectOptJSONObject != null && !com.xiaomi.onetrack.util.t.b(jSONObjectOptJSONObject.optString(com.xiaomi.onetrack.f.b.AbstractC0020b.a)) && com.xiaomi.onetrack.util.ae.b(jSONObjectOptJSONObject.optLong(com.xiaomi.onetrack.f.b.AbstractC0020b.p, 0L))) {
                    String strOptString = jSONObjectOptJSONObject.optString(com.xiaomi.onetrack.f.b.AbstractC0020b.l);
                    map.put(strOptString, Integer.valueOf((map.get(strOptString) == null ? 0 : ((Integer) map.get(strOptString)).intValue()) + 1));
                }
            }
            if (map.size() == 0) {
                return;
            }
            for (String str : map.keySet()) {
                com.xiaomi.onetrack.h.d.b.a(str, "", "up", "", ((Integer) map.get(str)).intValue());
            }
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.b("PSUploadDispense", "recordUploadSuccessStatsToDb exception ", e);
        }
    }

    private static boolean a(String str, String str2, List list, com.xiaomi.onetrack.i.a aVar) throws Throwable {
        if (list == null || list.size() <= 0) {
            return false;
        }
        List listC = c(list);
        if (aVar.h) {
            com.xiaomi.onetrack.util.r.a("PSUploadDispense", "token is inValid,need get new");
            aVar = com.xiaomi.onetrack.b.q.a().b();
        }
        a aVarA = a(aVar, str, str2, listC);
        com.xiaomi.onetrack.util.r.a("PSUploadDispense", "uploadGzipEncryptData success:" + aVarA.a);
        if (aVarA.a) {
            com.xiaomi.onetrack.util.r.a("PSUploadDispense", "uploadGzipEncryptData deleted:" + n.a().a(list));
            return true;
        }
        if (!aVarA.b) {
            return false;
        }
        com.xiaomi.onetrack.util.r.a("PSUploadDispense", "uploadGzipEncryptData deleted:" + n.a().b(list));
        return false;
    }

    private static String b(List list) {
        if (list == null) {
            return "";
        }
        try {
            JSONObject jSONObject = new JSONObject();
            JSONArray jSONArray = new JSONArray();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                m mVar = (m) it.next();
                JSONObject jSONObject2 = new JSONObject();
                Map mapF = mVar.f();
                if (mapF != null) {
                    JSONObject jSONObject3 = new JSONObject();
                    for (Map.Entry entry : mapF.entrySet()) {
                        if (!TextUtils.isEmpty((CharSequence) entry.getKey())) {
                            jSONObject3.put((String) entry.getKey(), entry.getValue());
                        }
                    }
                    jSONObject2.put(com.ot.pubsub.g.i.h, jSONObject3);
                }
                jSONObject2.put("data", mVar.e());
                jSONArray.put(jSONObject2);
            }
            jSONObject.put("messages", jSONArray);
            return jSONObject.toString();
        } catch (Exception unused) {
            return "";
        }
    }

    private static List c(List list) throws Throwable {
        if (list == null || list.size() == 0) {
            com.xiaomi.onetrack.util.r.a("PSUploadDispense", "messages is empty, return null");
            return null;
        }
        HashMap map = new HashMap();
        ArrayList arrayList = new ArrayList();
        String str = com.xiaomi.onetrack.d.f.a().b()[1];
        String str2 = com.xiaomi.onetrack.d.f.a().b()[0];
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        try {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                m mVar = (m) it.next();
                Map mapF = mVar.f();
                if (mapF != null) {
                    if (map.get(mapF) == null) {
                        map.put(mapF, new JSONArray());
                    }
                    ((JSONArray) map.get(mapF)).put(mVar.e());
                }
            }
            for (Map.Entry entry : map.entrySet()) {
                Map map2 = (Map) entry.getKey();
                map2.put(com.ot.pubsub.util.a.a, "true");
                map2.put(com.ot.pubsub.util.a.b, str);
                JSONArray jSONArray = (JSONArray) entry.getValue();
                com.xiaomi.onetrack.util.r.a("PSUploadDispense", "zip before : " + jSONArray.toString().length());
                byte[] bArrA = ah.a(jSONArray.toString());
                com.xiaomi.onetrack.util.r.a("PSUploadDispense", "zip after : " + bArrA.length);
                arrayList.add(new m(-1L, null, null, com.xiaomi.onetrack.d.c.a(ah.a(bArrA, str2)), map2, -1L));
            }
            return arrayList;
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.a("PSUploadDispense", "sortByAttribute error: " + e.getMessage());
            return arrayList;
        }
    }

    private static Map d(List list) {
        HashMap map = new HashMap();
        for (int i = 0; i < list.size(); i++) {
            m mVar = (m) list.get(i);
            String strC = mVar.c();
            if (map.get(strC) == null) {
                map.put(strC, new ArrayList());
            }
            ((List) map.get(strC)).add(mVar);
        }
        return map;
    }

    private static Map e(List list) {
        HashMap map = new HashMap();
        for (int i = 0; i < list.size(); i++) {
            m mVar = (m) list.get(i);
            String strD = mVar.d();
            if (map.get(strD) == null) {
                map.put(strD, new ArrayList());
            }
            ((List) map.get(strD)).add(mVar);
        }
        return map;
    }

    private static a a(com.xiaomi.onetrack.i.a aVar, String str, String str2, List list) throws Throwable {
        if (aVar != null) {
            try {
                if (!TextUtils.isEmpty(aVar.d) && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2) && list != null && list.size() != 0) {
                    com.xiaomi.onetrack.g.d dVarA = com.xiaomi.onetrack.g.b.a(String.format("%s/v1/projects/%s/topics/%s:publish", a(), str, str2), b(list), aVar.d);
                    if (dVarA != null) {
                        int i = dVarA.d;
                        if (i == 401) {
                            aVar.h = true;
                            com.xiaomi.onetrack.i.b.a().e();
                            return a(com.xiaomi.onetrack.b.q.a().c(), str, str2, list);
                        }
                        if (i == 404) {
                            if ("NOT_FOUND".equals(dVarA.g) && !TextUtils.isEmpty(dVarA.f) && (dVarA.f.contains(str) || dVarA.f.contains(str2))) {
                                return new a(false, true);
                            }
                        } else if (i == 200) {
                            com.xiaomi.onetrack.i.b.a().f();
                            return new a(true, false);
                        }
                    }
                    return new a(false, false);
                }
            } catch (Exception e) {
                com.xiaomi.onetrack.util.r.b("PSUploadDispense", "Exception while uploading ", e);
            }
        }
        com.xiaomi.onetrack.util.r.a("PSUploadDispense", "token or data is null,return false");
        return new a(false, false);
    }

    private static String a() {
        if (TextUtils.isEmpty(b)) {
            b = com.xiaomi.onetrack.util.z.a().a(c);
        }
        return b;
    }

    class a {
        public boolean a;
        public boolean b;

        a(boolean z, boolean z2) {
            this.a = z;
            this.b = z2;
        }
    }
}
