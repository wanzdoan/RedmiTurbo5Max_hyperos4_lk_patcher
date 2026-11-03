package com.ot.pubsub.b;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.ot.pubsub.BuildConfig;
import com.ot.pubsub.util.DeviceUtil;
import com.ot.pubsub.util.t;
import com.ot.pubsub.util.v;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class a {
    public static final String a = "disable_log";
    public static final String b = "event";
    public static final String c = "events";
    public static final String d = "level";
    public static final String e = "sample";
    public static final String f = "needIds";
    public static final String g = "bannedParams";
    public static final String h = "version";
    private static final String i = "AppConfigUpdater";
    private static final long j = 172800000;
    private static final String k = "hash";
    private static final String l = "appId";
    private static final String m = "apps";
    private static final String n = "type";
    private static final String o = "status";
    private static final String p = "deleted";
    private static final String q = "Android";
    private static final int s = 0;
    private static final int t = 1;
    private static final int u = 2;
    private static final int v = 100;
    private static final long x = 1800000;
    private static final int z = 0;
    private static AtomicBoolean r = new AtomicBoolean(false);
    private static ConcurrentHashMap w = new ConcurrentHashMap();
    private static b y = new b(Looper.getMainLooper(), null);
    private static ConcurrentHashMap A = new ConcurrentHashMap();
    private static ConcurrentHashMap B = new ConcurrentHashMap();
    private static String C = "";

    /* synthetic */ a(com.ot.pubsub.b.b bVar) {
        this();
    }

    private a() {
        String strL = t.l();
        if (!TextUtils.isEmpty(strL)) {
            C = strL;
            return;
        }
        String strI = com.ot.pubsub.util.l.i();
        if (TextUtils.isEmpty(strI)) {
            return;
        }
        C = strI;
        t.i(strI);
    }

    /* JADX INFO: renamed from: com.ot.pubsub.b.a$a, reason: collision with other inner class name */
    class C0016a {
        private static final a a = new a(null);

        private C0016a() {
        }
    }

    public static a a() {
        return C0016a.a;
    }

    public void a(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        B.put(str, Boolean.FALSE);
    }

    public void b(String str) {
        com.ot.pubsub.util.e.a(new com.ot.pubsub.b.b(this, str));
    }

    public void c(String str) {
        if (g(str)) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(str);
            b(arrayList);
            return;
        }
        com.ot.pubsub.util.j.a(i, "AppConfigUpdater Does not meet prerequisites for request");
    }

    private static boolean f(String str) {
        ConcurrentHashMap concurrentHashMap = B;
        return concurrentHashMap != null && concurrentHashMap.containsKey(str) && ((Boolean) B.get(str)).booleanValue();
    }

    private boolean g(String str) {
        if (!com.ot.pubsub.util.k.a()) {
            com.ot.pubsub.util.j.a(i, "net is not connected!");
            return false;
        }
        l lVarF = h.a().f(str);
        if (lVarF == null) {
            return true;
        }
        long j2 = lVarF.c;
        return j2 < System.currentTimeMillis() || j2 - System.currentTimeMillis() > j || f(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void b(List list) {
        com.ot.pubsub.util.j.a(i, "pullCloudData start, appIds: " + list.toString());
        if (com.ot.pubsub.util.l.c(i)) {
            return;
        }
        if (r.compareAndSet(false, true)) {
            HashMap map = new HashMap();
            try {
                try {
                    String strI = com.ot.pubsub.util.l.i();
                    if (!TextUtils.isEmpty(strI)) {
                        C = strI;
                        t.i(strI);
                    }
                    map.put(m.b, com.ot.pubsub.util.l.d());
                    map.put(m.c, com.ot.pubsub.util.l.c());
                    map.put(m.d, com.ot.pubsub.util.l.h() ? "1" : "0");
                    map.put(m.e, BuildConfig.SDK_VERSION);
                    map.put(m.m, com.ot.pubsub.util.b.c());
                    map.put(m.f, com.ot.pubsub.util.l.e());
                    map.put(m.g, DeviceUtil.a());
                    map.put(m.h, strI);
                    map.put(m.i, c(list));
                    map.put(m.j, com.ot.pubsub.util.b.e());
                    map.put(m.l, q);
                    map.put(m.n, "1");
                    String strE = com.ot.pubsub.g.l.a().e();
                    com.ot.pubsub.util.j.a(i, "pullData:" + strE);
                    String strA = com.ot.pubsub.i.a.b.a(strE, (Map) map, true);
                    com.ot.pubsub.util.j.a(i, "response:" + strA);
                    a(strA, list);
                } catch (Exception e2) {
                    com.ot.pubsub.util.j.b(i, "pullCloudData error: " + e2.getMessage());
                }
            } finally {
                r.set(false);
            }
        }
    }

    private static String c(List list) {
        JSONArray jSONArray = new JSONArray();
        try {
            JSONObject jSONObject = new JSONObject();
            for (int i2 = 0; i2 < list.size(); i2++) {
                String str = (String) list.get(i2);
                jSONObject.put("appId", str);
                if (f(str)) {
                    jSONObject.put("hash", "");
                } else {
                    jSONObject.put("hash", h.a().d(str));
                }
                jSONArray.put(jSONObject);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return jSONArray.toString();
    }

    public static void a(String str, List list) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.optInt(com.ot.pubsub.i.a.a.d) == 0) {
                d(list);
                a(jSONObject.optJSONObject("data").optJSONArray(m), list);
            }
        } catch (Exception e2) {
            com.ot.pubsub.util.j.a(i, "saveAppCloudData: " + e2.toString());
        }
    }

    private static void d(List list) {
        if (list == null || list.size() == 0) {
            return;
        }
        for (int i2 = 0; i2 < list.size(); i2++) {
            w.put(list.get(i2), Long.valueOf(System.currentTimeMillis() + x));
        }
    }

    private static void a(JSONArray jSONArray, List list) throws JSONException {
        com.ot.pubsub.util.j.a(i, "updateDataToDb start");
        long jCurrentTimeMillis = System.currentTimeMillis() + v.b + ((long) new Random().nextInt(86400000));
        if (jSONArray != null && jSONArray.length() > 0) {
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                JSONObject jSONObjectOptJSONObject = jSONArray.optJSONObject(i2);
                String strOptString = jSONObjectOptJSONObject == null ? "" : jSONObjectOptJSONObject.optString("appId");
                com.ot.pubsub.util.j.a(i, "appId: " + strOptString);
                if (!TextUtils.isEmpty(strOptString)) {
                    arrayList.add(strOptString);
                    a(strOptString, jSONObjectOptJSONObject, jCurrentTimeMillis);
                }
            }
            a(list, jCurrentTimeMillis, arrayList);
            Iterator it = list.iterator();
            while (it.hasNext()) {
                B.put((String) it.next(), Boolean.FALSE);
            }
            return;
        }
        a(list, jCurrentTimeMillis);
    }

    private static void a(String str, JSONObject jSONObject, long j2) throws JSONException {
        int iOptInt = jSONObject == null ? 0 : jSONObject.optInt("version");
        int iE = h.a().e(str);
        com.ot.pubsub.util.j.a(i, "local version: " + iE + ", server version: " + iOptInt);
        if (iE > 0 && iOptInt <= iE && !f(str)) {
            a(jSONObject, j2);
            return;
        }
        int iOptInt2 = jSONObject != null ? jSONObject.optInt("type") : -1;
        com.ot.pubsub.util.j.a(i, "type: " + iOptInt2);
        if (iOptInt2 == 0 || iOptInt2 == 1 || f(str)) {
            a(jSONObject, j2, iOptInt);
        } else if (iOptInt2 == 2) {
            b(jSONObject, j2);
        } else {
            com.ot.pubsub.util.j.a(i, "handleData do nothing!");
        }
    }

    private static void a(JSONObject jSONObject, long j2) {
        ArrayList arrayList = new ArrayList();
        if (jSONObject != null) {
            l lVar = new l();
            lVar.a = jSONObject.optString("appId");
            lVar.c = j2;
            arrayList.add(lVar);
        }
        if (!arrayList.isEmpty()) {
            h.a().a(arrayList);
        } else {
            com.ot.pubsub.util.j.a(i, "updateMinVersionData no timestamp can be updated!");
        }
    }

    private static void a(JSONObject jSONObject, long j2, int i2) throws JSONException {
        l lVarF;
        JSONObject jSONObject2;
        ArrayList arrayList = new ArrayList();
        if (jSONObject != null) {
            l lVar = new l();
            lVar.d = jSONObject.optString("hash");
            lVar.a = jSONObject.optString("appId");
            lVar.b = b(jSONObject);
            lVar.c = j2;
            if (!jSONObject.has(c) && (lVarF = h.a().f(lVar.a)) != null && (jSONObject2 = lVarF.e) != null && jSONObject2.optJSONArray(c) != null) {
                jSONObject.put(c, lVarF.e.optJSONArray(c));
            }
            lVar.e = jSONObject;
            arrayList.add(lVar);
        }
        if (!arrayList.isEmpty()) {
            h.a().a(arrayList);
        } else {
            com.ot.pubsub.util.j.a(i, "handleFullOrNoNewData no configuration can be updated!");
        }
    }

    private static void b(JSONObject jSONObject, long j2) {
        ArrayList arrayList = new ArrayList();
        if (jSONObject == null || !jSONObject.has(c)) {
            com.ot.pubsub.util.j.a(i, "handleIncrementalUpdate config is not change!");
        } else {
            l lVar = new l();
            lVar.d = jSONObject.optString("hash");
            String strOptString = jSONObject.optString("appId");
            lVar.a = strOptString;
            lVar.b = b(jSONObject);
            lVar.c = j2;
            lVar.e = a(strOptString, jSONObject);
            arrayList.add(lVar);
        }
        if (arrayList.isEmpty()) {
            com.ot.pubsub.util.j.a(i, "handleIncrementalUpdate no configuration can be updated!");
        } else {
            h.a().a(arrayList);
        }
    }

    private static int b(JSONObject jSONObject) {
        try {
            int iOptInt = jSONObject.optInt(e, 100);
            if (iOptInt < 0 || iOptInt > 100) {
                return 100;
            }
            return iOptInt;
        } catch (Exception e2) {
            com.ot.pubsub.util.j.a(i, "getCommonSample Exception:" + e2.getMessage());
            return 100;
        }
    }

    private static JSONObject a(String str, JSONObject jSONObject) {
        try {
            l lVarF = h.a().f(str);
            jSONObject.put(c, a(lVarF != null ? lVarF.e.optJSONArray(c) : null, jSONObject.optJSONArray(c)));
            return jSONObject;
        } catch (Exception e2) {
            com.ot.pubsub.util.j.b(i, "mergeConfig: " + e2.toString());
            return null;
        }
    }

    private static JSONArray a(JSONArray jSONArray, JSONArray jSONArray2) {
        int i2 = 0;
        while (jSONArray2 != null) {
            try {
                if (i2 >= jSONArray2.length()) {
                    break;
                }
                JSONObject jSONObjectOptJSONObject = jSONArray2.optJSONObject(i2);
                String strOptString = jSONObjectOptJSONObject.optString(b);
                for (int i3 = 0; jSONArray != null && i3 < jSONArray.length(); i3++) {
                    if (TextUtils.equals(strOptString, jSONArray.optJSONObject(i3).optString(b))) {
                        jSONArray.remove(i3);
                        break;
                    }
                }
                if (!jSONObjectOptJSONObject.has("status") || (jSONObjectOptJSONObject.has("status") && !TextUtils.equals(jSONObjectOptJSONObject.optString("status"), p))) {
                    if (jSONArray == null) {
                        jSONArray = new JSONArray();
                    }
                    jSONArray.put(jSONObjectOptJSONObject);
                }
                i2++;
            } catch (Exception e2) {
                com.ot.pubsub.util.j.b(i, "mergeEventsElement error:" + e2.toString());
            }
        }
        return jSONArray;
    }

    private static void a(List list, long j2, List list2) {
        try {
            if (list.size() != list2.size()) {
                list.removeAll(list2);
                a(list, j2);
            }
        } catch (Exception e2) {
            com.ot.pubsub.util.j.b(i, "handleInvalidAppIds error:" + e2.toString());
        }
    }

    private static void a(List list, long j2) {
        try {
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < list.size(); i2++) {
                l lVar = new l();
                lVar.a = (String) list.get(i2);
                lVar.b = 100L;
                lVar.c = j2;
                arrayList.add(lVar);
            }
            h.a().a(arrayList);
        } catch (Exception e2) {
            com.ot.pubsub.util.j.b(i, "handleError" + e2.toString());
        }
    }

    public void a(JSONObject jSONObject) {
        com.ot.pubsub.util.e.a(new c(this, jSONObject));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean h(String str) {
        Long l2 = (Long) w.get(str);
        return l2 == null || l2.longValue() - System.currentTimeMillis() < 0 || l2.longValue() - System.currentTimeMillis() > x;
    }

    public void d(String str) {
        if (TextUtils.isEmpty(C) || TextUtils.isEmpty(str) || TextUtils.equals(C, str)) {
            return;
        }
        Iterator it = B.entrySet().iterator();
        while (it.hasNext()) {
            ((Map.Entry) it.next()).setValue(Boolean.TRUE);
        }
    }

    class b extends Handler {
        /* synthetic */ b(Looper looper, com.ot.pubsub.b.b bVar) {
            this(looper);
        }

        private b(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            com.ot.pubsub.util.j.a(a.i, "ScheduleCloudHandler.handleMessage, msg.what=" + message.what);
            if (message.what == 0) {
                Object obj = message.obj;
                if (obj != null) {
                    try {
                        String str = (String) obj;
                        com.ot.pubsub.util.j.a(a.i, "ScheduleCloudHandler.handleMessage, appId: ".concat(str));
                        com.ot.pubsub.util.e.a(new d(this, str));
                        return;
                    } catch (Exception e) {
                        com.ot.pubsub.util.j.b(a.i, "handleMessage error: " + e.getMessage());
                        return;
                    }
                }
                com.ot.pubsub.util.j.a(a.i, "ScheduleCloudHandler.handleMessage, msg.obj is null");
            }
        }
    }
}
