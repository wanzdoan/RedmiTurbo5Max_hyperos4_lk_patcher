package com.xiaomi.onetrack.b;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.ot.pubsub.util.v;
import com.xiaomi.onetrack.OneTrack;
import com.xiaomi.onetrack.api.ar;
import com.xiaomi.onetrack.util.DeviceUtil;
import com.xiaomi.onetrack.util.ab;
import com.xiaomi.onetrack.util.ac;
import com.xiaomi.onetrack.util.r;
import com.xiaomi.onetrack.util.s;
import com.xiaomi.onetrack.util.z;
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

/* JADX INFO: loaded from: classes2.dex */
public class a {
    private static AtomicBoolean x = new AtomicBoolean(false);
    private static ConcurrentHashMap C = new ConcurrentHashMap();
    private static b E = new b(Looper.getMainLooper(), null);
    private static ConcurrentHashMap G = new ConcurrentHashMap();
    private static ConcurrentHashMap H = new ConcurrentHashMap();
    private static String I = "";

    /* synthetic */ a(com.xiaomi.onetrack.b.b bVar) {
        this();
    }

    private a() {
        String strX = ac.x();
        if (!TextUtils.isEmpty(strX)) {
            I = strX;
            return;
        }
        String strL = s.l();
        if (TextUtils.isEmpty(strL)) {
            return;
        }
        I = strL;
        ac.k(strL);
    }

    /* JADX INFO: renamed from: com.xiaomi.onetrack.b.a$a, reason: collision with other inner class name */
    abstract class AbstractC0019a {
        private static final a a = new a(null);
    }

    public static a a() {
        return AbstractC0019a.a;
    }

    public void a(String str) {
        H.put(str, Boolean.FALSE);
    }

    public void c(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (g(str)) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(str);
            b(arrayList);
            return;
        }
        r.a("AppConfigUpdater", "AppConfigUpdater Does not meet prerequisites for request");
    }

    private static boolean f(String str) {
        ConcurrentHashMap concurrentHashMap = H;
        return concurrentHashMap != null && concurrentHashMap.containsKey(str) && ((Boolean) H.get(str)).booleanValue();
    }

    private boolean g(String str) {
        if (!com.xiaomi.onetrack.g.c.a()) {
            r.a("AppConfigUpdater", "net is not connected!");
            return false;
        }
        m mVarF = j.a().f(str);
        if (mVarF == null) {
            return true;
        }
        long j = mVarF.c;
        return j < System.currentTimeMillis() || j - System.currentTimeMillis() > 172800000 || f(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void b(List list) {
        r.a("AppConfigUpdater", "pullCloudData start, appIds: " + list.toString());
        if (s.a("AppConfigUpdater")) {
            return;
        }
        if (x.compareAndSet(false, true)) {
            HashMap map = new HashMap();
            try {
                try {
                    String strL = s.l();
                    if (OneTrack.getGlobalBasicModeEnable() != 2) {
                        Context contextB = com.xiaomi.onetrack.f.a.b();
                        if (s.k()) {
                            String strB = DeviceUtil.b(contextB);
                            if (!TextUtils.isEmpty(strB)) {
                                map.put("ga", com.xiaomi.onetrack.d.d.c(strB));
                            }
                        } else {
                            String strA = com.xiaomi.onetrack.util.oaid.a.a().a(contextB);
                            if (ab.b(strA)) {
                                map.put(com.ot.pubsub.b.m.a, com.xiaomi.onetrack.d.d.c(strA));
                            }
                        }
                    }
                    map.put(com.ot.pubsub.b.m.b, s.e());
                    map.put(com.ot.pubsub.b.m.c, s.d());
                    map.put(com.ot.pubsub.b.m.d, s.k() ? "1" : "0");
                    map.put(com.ot.pubsub.b.m.e, "3.1.3");
                    map.put(com.ot.pubsub.b.m.m, com.xiaomi.onetrack.f.a.c());
                    map.put(com.ot.pubsub.b.m.f, s.g());
                    map.put(com.ot.pubsub.b.m.g, DeviceUtil.b());
                    map.put(com.ot.pubsub.b.m.h, strL);
                    map.put(com.ot.pubsub.b.m.i, c(list));
                    map.put(com.ot.pubsub.b.m.j, com.xiaomi.onetrack.f.a.e());
                    map.put(com.ot.pubsub.b.m.l, "Android");
                    map.put(com.ot.pubsub.b.m.n, "1");
                    map.put("nvc", "2");
                    String strC = z.a().c();
                    r.a("AppConfigUpdater", "pullData:" + strC);
                    String strB2 = com.xiaomi.onetrack.g.b.b(strC, map, true);
                    r.a("AppConfigUpdater", "response:" + strB2);
                    a(strB2, list);
                } catch (Exception e) {
                    r.b("AppConfigUpdater", "pullCloudData error: " + e.getMessage());
                }
            } finally {
                x.set(false);
            }
        }
    }

    private static String c(List list) {
        JSONArray jSONArray = new JSONArray();
        try {
            JSONObject jSONObject = new JSONObject();
            for (int i = 0; i < list.size(); i++) {
                String str = (String) list.get(i);
                jSONObject.put(com.ot.pubsub.j.d.b, str);
                if (f(str)) {
                    jSONObject.put(com.ot.pubsub.i.a.a.e, "");
                } else {
                    jSONObject.put(com.ot.pubsub.i.a.a.e, j.a().c(str));
                }
                jSONArray.put(jSONObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                a(jSONObject.optJSONObject("data").optJSONArray("apps"), list);
            }
        } catch (Exception e) {
            r.a("AppConfigUpdater", "saveAppCloudData: " + e.toString());
        }
    }

    private static void d(List list) {
        if (list == null || list.size() == 0) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            C.put(list.get(i), Long.valueOf(System.currentTimeMillis() + 1800000));
        }
    }

    private static void a(JSONArray jSONArray, List list) throws JSONException {
        r.a("AppConfigUpdater", "updateDataToDb start");
        long jCurrentTimeMillis = System.currentTimeMillis() + v.b + ((long) new Random().nextInt(86400000));
        if (jSONArray != null && jSONArray.length() > 0) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObjectOptJSONObject = jSONArray.optJSONObject(i);
                String strOptString = jSONObjectOptJSONObject == null ? "" : jSONObjectOptJSONObject.optString(com.ot.pubsub.j.d.b);
                r.a("AppConfigUpdater", "appId: " + strOptString);
                if (!TextUtils.isEmpty(strOptString)) {
                    arrayList.add(strOptString);
                    a(strOptString, jSONObjectOptJSONObject, jCurrentTimeMillis);
                }
            }
            a(list, jCurrentTimeMillis, arrayList);
            Iterator it = list.iterator();
            while (it.hasNext()) {
                H.put((String) it.next(), Boolean.FALSE);
            }
            return;
        }
        a(list, jCurrentTimeMillis);
    }

    private static void a(String str, JSONObject jSONObject, long j) throws JSONException {
        int iOptInt = jSONObject == null ? 0 : jSONObject.optInt("version");
        int iD = j.a().d(str);
        r.a("AppConfigUpdater", "local version: " + iD + ", server version: " + iOptInt);
        if (iD > 0 && iOptInt < iD && !f(str)) {
            a(jSONObject, j);
            return;
        }
        int iOptInt2 = jSONObject != null ? jSONObject.optInt("type") : -1;
        r.a("AppConfigUpdater", "type: " + iOptInt2);
        if (iOptInt2 == 0 || iOptInt2 == 1 || f(str)) {
            a(jSONObject, j, iOptInt);
        } else if (iOptInt2 == 2) {
            b(jSONObject, j);
        } else {
            r.a("AppConfigUpdater", "handleData do nothing!");
        }
    }

    private static void a(JSONObject jSONObject, long j) {
        ArrayList arrayList = new ArrayList();
        if (jSONObject != null) {
            m mVar = new m();
            mVar.a = jSONObject.optString(com.ot.pubsub.j.d.b);
            mVar.c = j;
            arrayList.add(mVar);
        }
        if (!arrayList.isEmpty()) {
            j.a().a(arrayList);
        } else {
            r.a("AppConfigUpdater", "updateMinVersionData no timestamp can be updated!");
        }
    }

    private static void a(JSONObject jSONObject, long j, int i) throws JSONException {
        m mVarF;
        JSONObject jSONObject2;
        ArrayList arrayList = new ArrayList();
        if (jSONObject != null) {
            m mVar = new m();
            mVar.d = jSONObject.optString(com.ot.pubsub.i.a.a.e);
            mVar.a = jSONObject.optString(com.ot.pubsub.j.d.b);
            mVar.b = b(jSONObject);
            mVar.c = j;
            if (!jSONObject.has(com.ot.pubsub.b.a.c) && (mVarF = j.a().f(mVar.a)) != null && (jSONObject2 = mVarF.e) != null && jSONObject2.optJSONArray(com.ot.pubsub.b.a.c) != null) {
                jSONObject.put(com.ot.pubsub.b.a.c, mVarF.e.optJSONArray(com.ot.pubsub.b.a.c));
            }
            mVar.e = jSONObject;
            arrayList.add(mVar);
        }
        if (!arrayList.isEmpty()) {
            j.a().a(arrayList);
        } else {
            r.a("AppConfigUpdater", "handleFullOrNoNewData no configuration can be updated!");
        }
    }

    private static void b(JSONObject jSONObject, long j) {
        ArrayList arrayList = new ArrayList();
        if (jSONObject == null || !jSONObject.has(com.ot.pubsub.b.a.c)) {
            r.a("AppConfigUpdater", "handleIncrementalUpdate config is not change!");
        } else {
            m mVar = new m();
            mVar.d = jSONObject.optString(com.ot.pubsub.i.a.a.e);
            String strOptString = jSONObject.optString(com.ot.pubsub.j.d.b);
            mVar.a = strOptString;
            mVar.b = b(jSONObject);
            mVar.c = j;
            mVar.e = a(strOptString, jSONObject);
            arrayList.add(mVar);
        }
        if (arrayList.isEmpty()) {
            r.a("AppConfigUpdater", "handleIncrementalUpdate no configuration can be updated!");
        } else {
            j.a().a(arrayList);
        }
    }

    private static int b(JSONObject jSONObject) {
        try {
            int iOptInt = jSONObject.optInt(com.ot.pubsub.b.a.e, 100);
            if (iOptInt < 0 || iOptInt > 100) {
                return 100;
            }
            return iOptInt;
        } catch (Exception e) {
            r.a("AppConfigUpdater", "getCommonSample Exception:" + e.getMessage());
            return 100;
        }
    }

    private static JSONObject a(String str, JSONObject jSONObject) {
        try {
            m mVarF = j.a().f(str);
            jSONObject.put(com.ot.pubsub.b.a.c, a(mVarF != null ? mVarF.e.optJSONArray(com.ot.pubsub.b.a.c) : null, jSONObject.optJSONArray(com.ot.pubsub.b.a.c)));
            return jSONObject;
        } catch (Exception e) {
            r.b("AppConfigUpdater", "mergeConfig: " + e.toString());
            return null;
        }
    }

    private static JSONArray a(JSONArray jSONArray, JSONArray jSONArray2) {
        int i = 0;
        while (jSONArray2 != null) {
            try {
                if (i >= jSONArray2.length()) {
                    break;
                }
                JSONObject jSONObjectOptJSONObject = jSONArray2.optJSONObject(i);
                String strOptString = jSONObjectOptJSONObject.optString(com.ot.pubsub.b.a.b);
                for (int i2 = 0; jSONArray != null && i2 < jSONArray.length(); i2++) {
                    if (TextUtils.equals(strOptString, jSONArray.optJSONObject(i2).optString(com.ot.pubsub.b.a.b))) {
                        jSONArray.remove(i2);
                        break;
                    }
                }
                if (!jSONObjectOptJSONObject.has(com.ot.pubsub.a.a.J) || (jSONObjectOptJSONObject.has(com.ot.pubsub.a.a.J) && !TextUtils.equals(jSONObjectOptJSONObject.optString(com.ot.pubsub.a.a.J), "deleted"))) {
                    if (jSONArray == null) {
                        jSONArray = new JSONArray();
                    }
                    jSONArray.put(jSONObjectOptJSONObject);
                }
                i++;
            } catch (Exception e) {
                r.b("AppConfigUpdater", "mergeEventsElement error:" + e.toString());
            }
        }
        return jSONArray;
    }

    private static void a(List list, long j, List list2) {
        try {
            if (list.size() != list2.size()) {
                list.removeAll(list2);
                a(list, j);
            }
        } catch (Exception e) {
            r.b("AppConfigUpdater", "handleInvalidAppIds error:" + e.toString());
        }
    }

    private static void a(List list, long j) {
        try {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                m mVar = new m();
                mVar.a = (String) list.get(i);
                mVar.b = 100L;
                mVar.c = j;
                arrayList.add(mVar);
            }
            j.a().a(arrayList);
        } catch (Exception e) {
            r.b("AppConfigUpdater", "handleError" + e.toString());
        }
    }

    public void a(JSONObject jSONObject) {
        com.xiaomi.onetrack.util.j.a(new c(this, jSONObject));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean h(String str) {
        Long l = (Long) C.get(str);
        return l == null || l.longValue() - System.currentTimeMillis() < 0 || l.longValue() - System.currentTimeMillis() > 1800000;
    }

    public void d(String str) {
        if (TextUtils.isEmpty(I) || TextUtils.isEmpty(str) || TextUtils.equals(I, str)) {
            return;
        }
        ar.a(I, str);
        Iterator it = H.entrySet().iterator();
        while (it.hasNext()) {
            ((Map.Entry) it.next()).setValue(Boolean.TRUE);
        }
        I = str;
        ac.k(str);
    }

    class b extends Handler {
        /* synthetic */ b(Looper looper, com.xiaomi.onetrack.b.b bVar) {
            this(looper);
        }

        private b(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            r.a("AppConfigUpdater", "ScheduleCloudHandler.handleMessage, msg.what=" + message.what);
            if (message.what == 0) {
                Object obj = message.obj;
                if (obj != null) {
                    try {
                        String str = (String) obj;
                        r.a("AppConfigUpdater", "ScheduleCloudHandler.handleMessage, appId: ".concat(str));
                        com.xiaomi.onetrack.util.j.a(new d(this, str));
                        return;
                    } catch (Exception e) {
                        r.b("AppConfigUpdater", "handleMessage error: " + e.getMessage());
                        return;
                    }
                }
                r.a("AppConfigUpdater", "ScheduleCloudHandler.handleMessage, msg.obj is null");
            }
        }
    }
}
