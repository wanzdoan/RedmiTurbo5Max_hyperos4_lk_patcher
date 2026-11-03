package com.xiaomi.onetrack.api;

import android.content.Context;
import android.text.TextUtils;
import com.ot.pubsub.a.a;
import com.ot.pubsub.a.b;
import com.ot.pubsub.util.s;
import com.xiaomi.onetrack.Configuration;
import com.xiaomi.onetrack.OneTrack;
import com.xiaomi.onetrack.c.j;
import com.xiaomi.onetrack.f.i;
import com.xiaomi.onetrack.util.DeviceUtil;
import com.xiaomi.onetrack.util.ae;
import com.xiaomi.onetrack.util.x;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class as implements ao {
    private Configuration e;
    private x f;

    public as(Context context, Configuration configuration, x xVar) {
        i.a(context);
        this.e = configuration;
        this.f = xVar;
    }

    @Override // com.xiaomi.onetrack.api.ao
    public void a(String str, String str2) {
        JSONObject jSONObject;
        JSONObject jSONObjectOptJSONObject;
        JSONObject jSONObjectOptJSONObject2 = null;
        try {
            jSONObject = new JSONObject(str2);
            try {
                jSONObjectOptJSONObject = jSONObject.optJSONObject(b.b);
                try {
                    jSONObjectOptJSONObject2 = jSONObject.optJSONObject(b.a);
                } catch (JSONException e) {
                    e = e;
                    com.xiaomi.onetrack.util.r.a("OneTrackLocalImp", " data JSONException e:" + e.getMessage());
                }
            } catch (JSONException e2) {
                e = e2;
                jSONObjectOptJSONObject = null;
            }
        } catch (JSONException e3) {
            e = e3;
            jSONObject = null;
            jSONObjectOptJSONObject = null;
        }
        JSONObject jSONObject2 = jSONObjectOptJSONObject2;
        JSONObject jSONObject3 = jSONObject;
        JSONObject jSONObject4 = jSONObjectOptJSONObject;
        boolean zA = a(jSONObject4);
        x xVar = this.f;
        if (xVar != null && !xVar.a(str) && !zA) {
            com.xiaomi.onetrack.util.r.a("OneTrackLocalImp", "The privacy policy is not permitted, and the event is not basic or recommend event or custom dau event, skip it.");
            return;
        }
        if (!a(str, str2, zA)) {
            com.xiaomi.onetrack.h.d.b.a(this.e.getAppId(), str, "se", "r1", 1);
            c.a(this, this.e, this.f, str, "se", "r1");
            return;
        }
        if (!j.b()) {
            j.a(str, str2);
            return;
        }
        j.a(this);
        if (com.xiaomi.onetrack.util.r.a && !str.equalsIgnoreCase(a.f)) {
            com.xiaomi.onetrack.util.r.a("OneTrackLocalImp", "track data:" + str2);
        }
        com.xiaomi.onetrack.util.j.a(new at(this, zA));
        a(str, jSONObject3, jSONObject4, jSONObject2, zA);
    }

    @Override // com.xiaomi.onetrack.api.ao
    public void a(int i) {
        com.xiaomi.onetrack.util.j.a(new au(this, i));
    }

    @Override // com.xiaomi.onetrack.api.ao
    public void a(boolean z) {
        if (ar.h) {
            j.a(this);
        }
    }

    public boolean a(String str, String str2, boolean z) {
        if (OneTrack.isDisable() || OneTrack.isUseSystemNetTrafficOnly()) {
            com.xiaomi.onetrack.util.r.a("OneTrackLocalImp", "Tracking data is disabled or onetrack use system net traffic only, skip it.");
            return false;
        }
        boolean z2 = str != null && str.equals(a.f);
        if (z || z2) {
            if (str2 != null && str2.length() > 512000) {
                com.xiaomi.onetrack.util.r.a("OneTrackLocalImp", "ad Event size exceed limitation!");
                return false;
            }
        } else if (str2 != null && str2.length() * 2 > 102400) {
            com.xiaomi.onetrack.util.r.a("OneTrackLocalImp", "Event size exceed limitation!");
            return false;
        }
        return true;
    }

    private void a(String str, JSONObject jSONObject, JSONObject jSONObject2, JSONObject jSONObject3, boolean z) {
        String appId = this.e.getAppId();
        if (z) {
            appId = this.e.getAdEventAppId();
        }
        if (a(appId, str, jSONObject2, jSONObject3)) {
            try {
                if (z) {
                    com.xiaomi.onetrack.f.d.b(appId, com.xiaomi.onetrack.f.a.e(), str, jSONObject.toString());
                } else {
                    com.xiaomi.onetrack.f.d.a(appId, com.xiaomi.onetrack.f.a.e(), str, jSONObject.toString());
                }
            } catch (Throwable th) {
                com.xiaomi.onetrack.util.r.b("OneTrackLocalImp", "checkCloudControl error：" + th.toString());
            }
        }
    }

    private boolean a(String str, String str2, JSONObject jSONObject, JSONObject jSONObject2) {
        String strOptString;
        String strA;
        boolean z;
        JSONArray jSONArrayA;
        if (com.xiaomi.onetrack.b.o.b(str)) {
            com.xiaomi.onetrack.util.r.a("OneTrackLocalImp", str + ",This appId and device in whiteList");
            return true;
        }
        if (com.xiaomi.onetrack.b.o.c(str)) {
            com.xiaomi.onetrack.util.r.a("OneTrackLocalImp", "This app disabled tracking data, skip it.");
            return false;
        }
        if (!com.xiaomi.onetrack.b.o.a(str)) {
            com.xiaomi.onetrack.util.r.a("OneTrackLocalImp", "This appId " + str + " is oldUploadByCloudControl");
            return b(str, str2, jSONObject, jSONObject2);
        }
        if (jSONObject2 == null) {
            strOptString = "";
        } else {
            try {
                strOptString = jSONObject2.optString(a.ac);
            } catch (Exception e) {
                com.xiaomi.onetrack.util.r.b("OneTrackLocalImp", "shouldUploadByCloudControl error：" + e.getMessage());
                return true;
            }
        }
        JSONObject jSONObjectA = com.xiaomi.onetrack.b.j.a().a(str, str2);
        StringBuilder sb = new StringBuilder("eventConfig: ");
        sb.append(jSONObjectA != null ? jSONObjectA.toString() : "");
        com.xiaomi.onetrack.util.r.a("OneTrackLocalImp", sb.toString());
        if (!TextUtils.isEmpty(strOptString) && (jSONArrayA = com.xiaomi.onetrack.b.o.a(jSONObjectA, str2, "nc", (JSONArray) null)) != null) {
            int i = 0;
            while (true) {
                if (i >= jSONArrayA.length()) {
                    strA = "";
                    z = false;
                    break;
                }
                JSONObject jSONObject3 = jSONArrayA.getJSONObject(i);
                String strA2 = com.xiaomi.onetrack.b.o.a(jSONObject3, str2, "needId", "");
                a(strA2, strOptString, "needIds: %s, tip: %s");
                if (b(strOptString, strA2)) {
                    if (!a(jSONObject3, str, str2)) {
                        strA = com.xiaomi.onetrack.b.o.a(jSONObject3, str2, com.ot.pubsub.b.a.g, "");
                        z = true;
                        break;
                    }
                    return false;
                }
                i++;
            }
        } else {
            strA = "";
            z = false;
            break;
        }
        if (!z && jSONObjectA != null) {
            if (a(jSONObjectA, str, str2)) {
                return false;
            }
            strA = com.xiaomi.onetrack.b.o.a(jSONObjectA, str2, com.ot.pubsub.b.a.g, "");
        }
        String strB = com.xiaomi.onetrack.b.j.a().b(str);
        a(strA, strB, "bannedParamsForEvent: %s, bannedParamsForApp: %s");
        Set setA = com.xiaomi.onetrack.util.ab.a(strB, strA, ",");
        a(jSONObject, setA);
        a(jSONObject2, setA);
        return true;
    }

    private boolean a(JSONObject jSONObject, String str, String str2) {
        if (com.xiaomi.onetrack.b.o.a(jSONObject, str2, com.ot.pubsub.b.a.a, false)) {
            com.xiaomi.onetrack.util.r.a("OneTrackLocalImp", "AppId is " + str + ", this event " + str2 + " disabled tracking data, skip it.");
            com.xiaomi.onetrack.h.d.b.a(this.e.getAppId(), str2, "sa", "r1", 1);
            c.a(this, this.e, this.f, str2, "sa", "r1");
            return true;
        }
        if (!a(jSONObject, str2)) {
            com.xiaomi.onetrack.util.r.a("OneTrackLocalImp", "This event should not upload by Cycle , skip it.");
            com.xiaomi.onetrack.h.d.b.a(this.e.getAppId(), str2, "sa", "r2", 1);
            c.a(this, this.e, this.f, str2, "sa", "r5");
            return true;
        }
        if (b(jSONObject, str, str2)) {
            return false;
        }
        com.xiaomi.onetrack.util.r.a("OneTrackLocalImp", " This event should not upload by sampling , skip it.");
        com.xiaomi.onetrack.h.d.b.a(this.e.getAppId(), str2, "sa", "r2", 1);
        c.a(this, this.e, this.f, str2, "sa", "r2");
        return true;
    }

    private boolean a(JSONObject jSONObject, String str) {
        boolean z = true;
        try {
            String strA = com.xiaomi.onetrack.b.o.a(jSONObject, str, "cycle", "");
            if (TextUtils.isEmpty(strA)) {
                return true;
            }
            com.xiaomi.onetrack.util.r.a("OneTrackLocalImp", "shouldUploadByCycle " + str + "_cycle: " + strA);
            String[] strArrSplit = strA.split(",");
            int iD = ae.d();
            int iC = ae.c();
            int length = strArrSplit.length;
            boolean z2 = true;
            int i = 0;
            while (i < length) {
                try {
                    String str2 = strArrSplit[i];
                    int i2 = Integer.parseInt(str2.substring(1));
                    if (str2.startsWith("w")) {
                        if (iD == i2) {
                            return true;
                        }
                    } else if (str2.startsWith("m") && iC == i2) {
                        return true;
                    }
                    i++;
                    z2 = false;
                } catch (Exception e) {
                    e = e;
                    z = z2;
                }
            }
            return z2;
        } catch (Exception e2) {
            e = e2;
        }
        com.xiaomi.onetrack.util.r.a("OneTrackLocalImp", "shouldUploadByCycle " + str + "_exception: " + e.getMessage());
        return z;
    }

    private boolean b(String str, String str2, JSONObject jSONObject, JSONObject jSONObject2) {
        String strOptString;
        String strA = "";
        try {
            JSONObject jSONObjectA = com.xiaomi.onetrack.b.j.a().a(str, str2);
            String strA2 = com.xiaomi.onetrack.b.o.a(jSONObjectA, str2, com.ot.pubsub.b.a.f, "");
            if (jSONObject2 == null) {
                strOptString = "";
            } else {
                strOptString = jSONObject2.optString(a.ac);
            }
            com.xiaomi.onetrack.util.r.a("OneTrackLocalImp", "tip: " + strOptString + ", needIds: " + strA2);
            if (c(strOptString, strA2)) {
                if (com.xiaomi.onetrack.b.o.a(jSONObjectA, str2, com.ot.pubsub.b.a.a, false)) {
                    com.xiaomi.onetrack.util.r.a("OneTrackLocalImp", " This event disabled tracking data , skip it.");
                    com.xiaomi.onetrack.h.d.b.a(this.e.getAppId(), str2, "sa", "r1", 1);
                    c.a(this, this.e, this.f, str2, "sa", "r1");
                    return false;
                }
                if (b(jSONObjectA, str, str2)) {
                    strA = com.xiaomi.onetrack.b.o.a(jSONObjectA, str2, com.ot.pubsub.b.a.g, "");
                } else {
                    com.xiaomi.onetrack.util.r.a("OneTrackLocalImp", " This event should not upload by sampling , skip it.");
                    com.xiaomi.onetrack.h.d.b.a(this.e.getAppId(), str2, "sa", "r2", 1);
                    c.a(this, this.e, this.f, str2, "sa", "r2");
                    return false;
                }
            }
            String strB = com.xiaomi.onetrack.b.j.a().b(str);
            com.xiaomi.onetrack.util.r.a("OneTrackLocalImp", "bannedParamsForApp: " + strB + ", bannedParamsForEvent: " + strA);
            Set setA = com.xiaomi.onetrack.util.ab.a(strB, strA, ",");
            a(jSONObject, setA);
            a(jSONObject2, setA);
            return true;
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.b("OneTrackLocalImp", "oldEventOrNeedIdByCloudControl error：" + e.getMessage());
            return true;
        }
    }

    private boolean b(String str, String str2) {
        List listA;
        if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str)) {
            try {
                String[] strArrSplit = str.split(s.a);
                if (strArrSplit != null && strArrSplit.length >= 5 && (listA = com.xiaomi.onetrack.util.ab.a(str2, ",")) != null && listA.contains(strArrSplit[4])) {
                    return true;
                }
            } catch (Exception e) {
                com.xiaomi.onetrack.util.r.b("OneTrackLocalImp", "isMatchId error：" + e.toString());
            }
        }
        return false;
    }

    private boolean c(String str, String str2) {
        List listA;
        if (TextUtils.isEmpty(str2)) {
            return true;
        }
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            String[] strArrSplit = str.split(s.a);
            return strArrSplit != null && strArrSplit.length >= 5 && (listA = com.xiaomi.onetrack.util.ab.a(str2, ",")) != null && listA.contains(strArrSplit[4]);
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.b("OneTrackLocalImp", "isMatchId error：" + e.toString());
        }
    }

    private void a(JSONObject jSONObject, Set set) {
        if (jSONObject == null || set == null || set.size() == 0) {
            com.xiaomi.onetrack.util.r.a("OneTrackLocalImp", "jsonObject is null or bannedParams is empty");
            return;
        }
        com.xiaomi.onetrack.util.r.a("OneTrackLocalImp", "jsonObject: " + jSONObject.toString() + ", bannedParams: " + set.toString());
        try {
            Iterator<String> itKeys = jSONObject.keys();
            while (itKeys.hasNext()) {
                if (set.contains(itKeys.next())) {
                    itKeys.remove();
                }
            }
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.b("OneTrackLocalImp", "filterParams error：" + e.toString());
        }
    }

    private boolean b(JSONObject jSONObject, String str, String str2) {
        String strA;
        if (com.xiaomi.onetrack.f.b.a(this.f.b())) {
            com.xiaomi.onetrack.util.r.a("OneTrackLocalImp", "basicModeEnable is open,notUploadBySampling return false");
            return true;
        }
        long jA = com.xiaomi.onetrack.b.j.a().a(jSONObject, str, str2);
        Context contextB = com.xiaomi.onetrack.f.a.b();
        if (com.xiaomi.onetrack.util.s.j()) {
            strA = DeviceUtil.b(contextB);
            com.xiaomi.onetrack.util.r.a("OneTrackLocalImp", "gaid: " + strA);
        } else {
            strA = com.xiaomi.onetrack.util.oaid.a.a().a(contextB);
            com.xiaomi.onetrack.util.r.a("OneTrackLocalImp", "oaid: " + strA);
        }
        long jAbs = Math.abs(strA.hashCode()) % 100;
        boolean z = jA > jAbs;
        com.xiaomi.onetrack.util.r.a("OneTrackLocalImp", "shouldUploadBySampling " + str2 + ",  shouldUpload=" + z + ", sample=" + jA + ", val=" + jAbs);
        return z;
    }

    private boolean a(JSONObject jSONObject) {
        try {
            return jSONObject.optBoolean(com.xiaomi.onetrack.f.b.AbstractC0020b.D, false);
        } catch (Throwable unused) {
            com.xiaomi.onetrack.util.r.a("OneTrackLocalImp", "");
            return false;
        }
    }

    private void a(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str) && TextUtils.isEmpty(str2)) {
            return;
        }
        com.xiaomi.onetrack.util.r.a("OneTrackLocalImp", String.format(str3, str, str2));
    }
}
