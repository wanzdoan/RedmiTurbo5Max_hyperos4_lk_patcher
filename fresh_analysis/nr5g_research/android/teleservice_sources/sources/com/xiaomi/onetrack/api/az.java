package com.xiaomi.onetrack.api;

import android.text.TextUtils;
import com.ot.pubsub.a.a;
import com.ot.pubsub.a.b;
import com.ot.pubsub.util.s;
import com.xiaomi.onetrack.Configuration;
import com.xiaomi.onetrack.OneTrack;
import com.xiaomi.onetrack.util.DeviceUtil;
import com.xiaomi.onetrack.util.ae;
import com.xiaomi.onetrack.util.j;
import com.xiaomi.onetrack.util.x;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class az implements ao {
    private Configuration e;
    private x f;

    public az(Configuration configuration, x xVar) {
        this.e = configuration;
        this.f = xVar;
        j.a(new ba(this, configuration));
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
                    com.xiaomi.onetrack.util.r.a("PubSubTrackLocalImp", " data JSONException e:" + e.getMessage());
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
            com.xiaomi.onetrack.util.r.a("PubSubTrackLocalImp", "The privacy policy is not permitted, and the event is not basic or recommend event or custom dau event, skip it.");
            return;
        }
        if (!a(str, str2, zA)) {
            com.xiaomi.onetrack.h.d.b.a(this.e.getAppId(), str, "se", "r1", 1);
            c.a(this, this.e, this.f, str, "se", "r1");
            return;
        }
        if (!com.xiaomi.onetrack.c.j.b()) {
            com.xiaomi.onetrack.util.r.a("PubSubTrackLocalImp", "track network is unaccessable, cta");
            com.xiaomi.onetrack.c.j.a(str, str2);
            return;
        }
        com.xiaomi.onetrack.c.j.a(this);
        if (com.xiaomi.onetrack.util.r.a && !str.equalsIgnoreCase(a.f)) {
            com.xiaomi.onetrack.util.r.a("PubSubTrackLocalImp", "track data:" + str2);
        }
        j.a(new bb(this, zA));
        a(str, jSONObject3, jSONObject4, jSONObject2, zA);
    }

    @Override // com.xiaomi.onetrack.api.ao
    public void a(int i) {
        if (i == 2) {
            com.xiaomi.onetrack.c.a.a(new bc(this));
        }
    }

    @Override // com.xiaomi.onetrack.api.ao
    public void a(boolean z) {
        if (ar.h) {
            return;
        }
        com.xiaomi.onetrack.c.j.a(this);
    }

    public boolean a(String str, String str2, boolean z) {
        if (OneTrack.isDisable() || OneTrack.isUseSystemNetTrafficOnly()) {
            com.xiaomi.onetrack.util.r.a("PubSubTrackLocalImp", "Tracking data is disabled or onetrack use system net traffic only, skip it.");
            return false;
        }
        Configuration configuration = this.e;
        if (configuration == null || TextUtils.isEmpty(configuration.getProjectId()) || TextUtils.isEmpty(this.e.getPrivateKeyId())) {
            com.xiaomi.onetrack.util.r.a("PubSubTrackLocalImp", "projectId or pKeyId is empty, so return");
            return false;
        }
        if (str != null && str.equals(a.f)) {
            return true;
        }
        if (z) {
            if (str2 != null && str2.length() > 512000) {
                com.xiaomi.onetrack.util.r.a("PubSubTrackLocalImp", "ad Event size exceed limitation!");
                return false;
            }
        } else if (str2 != null && str2.length() * 2 > 102400) {
            com.xiaomi.onetrack.util.r.a("PubSubTrackLocalImp", "Event size exceed limitation!");
            return false;
        }
        return true;
    }

    private void a(String str, JSONObject jSONObject, JSONObject jSONObject2, JSONObject jSONObject3, boolean z) {
        String appId = this.e.getAppId();
        if (z) {
            appId = this.e.getAdEventAppId();
        }
        String str2 = appId;
        if (a(str2, str, jSONObject2, jSONObject3)) {
            String projectId = this.e.getProjectId();
            String strA = com.xiaomi.onetrack.util.a.a(str2);
            Map mapB = com.xiaomi.onetrack.util.a.b();
            if (z) {
                com.xiaomi.onetrack.f.d.b(projectId, strA, str2, com.xiaomi.onetrack.f.a.e(), str, jSONObject.toString(), mapB);
            } else {
                com.xiaomi.onetrack.f.d.a(projectId, strA, str2, com.xiaomi.onetrack.f.a.e(), str, jSONObject.toString(), mapB);
            }
        }
    }

    private boolean a(String str, String str2, JSONObject jSONObject, JSONObject jSONObject2) {
        String strOptString;
        String strA;
        boolean z;
        JSONArray jSONArrayA;
        if (com.xiaomi.onetrack.b.o.b(str)) {
            com.xiaomi.onetrack.util.r.a("PubSubTrackLocalImp", str + ",This appId and device in whiteList");
            return true;
        }
        if (com.xiaomi.onetrack.b.o.c(str)) {
            com.xiaomi.onetrack.util.r.a("PubSubTrackLocalImp", "This app disabled tracking data, skip it.");
            return false;
        }
        if (!com.xiaomi.onetrack.b.o.a(str)) {
            com.xiaomi.onetrack.util.r.a("PubSubTrackLocalImp", "This appId " + str + " is oldUploadByCloudControl");
            return b(str, str2, jSONObject, jSONObject2);
        }
        if (jSONObject2 == null) {
            strOptString = "";
        } else {
            try {
                strOptString = jSONObject2.optString(a.ac);
            } catch (Exception e) {
                com.xiaomi.onetrack.util.r.b("PubSubTrackLocalImp", "shouldUploadByCloudControl error：" + e.getMessage());
                return true;
            }
        }
        JSONObject jSONObjectA = com.xiaomi.onetrack.b.j.a().a(str, str2);
        StringBuilder sb = new StringBuilder("eventConfig: ");
        sb.append(jSONObjectA != null ? jSONObjectA.toString() : "");
        com.xiaomi.onetrack.util.r.a("PubSubTrackLocalImp", sb.toString());
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
        com.xiaomi.onetrack.util.r.a("PubSubTrackLocalImp", "bannedParamsForApp: " + strB + ", bannedParamsForEvent: " + strA);
        Set setA = com.xiaomi.onetrack.util.ab.a(strB, strA, ",");
        a(jSONObject, setA);
        a(jSONObject2, setA);
        return true;
    }

    private boolean a(JSONObject jSONObject, String str, String str2) {
        if (com.xiaomi.onetrack.b.o.a(jSONObject, str2, com.ot.pubsub.b.a.a, false)) {
            com.xiaomi.onetrack.util.r.a("PubSubTrackLocalImp", " This event disabled tracking data , skip it.");
            com.xiaomi.onetrack.h.d.b.a(this.e.getAppId(), str2, "sa", "r1", 1);
            c.a(this, this.e, this.f, str2, "sa", "r1");
            return true;
        }
        if (!a(jSONObject, str2)) {
            com.xiaomi.onetrack.util.r.a("PubSubTrackLocalImp", "This event should not upload by Cycle , skip it.");
            com.xiaomi.onetrack.h.d.b.a(this.e.getAppId(), str2, "sa", "r2", 1);
            c.a(this, this.e, this.f, str2, "sa", "r5");
            return true;
        }
        if (b(jSONObject, str, str2)) {
            return false;
        }
        com.xiaomi.onetrack.util.r.a("PubSubTrackLocalImp", " This event should not upload by sampling , skip it.");
        com.xiaomi.onetrack.h.d.b.a(this.e.getAppId(), str2, "sa", "r2", 1);
        c.a(this, this.e, this.f, str2, "sa", "r2");
        return true;
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
            com.xiaomi.onetrack.util.r.a("PubSubTrackLocalImp", "tip: " + strOptString + ", needIds: " + strA2);
            if (c(strOptString, strA2)) {
                if (com.xiaomi.onetrack.b.o.a(jSONObjectA, str2, com.ot.pubsub.b.a.a, false)) {
                    com.xiaomi.onetrack.util.r.a("PubSubTrackLocalImp", " This event disabled tracking data , skip it.");
                    com.xiaomi.onetrack.h.d.b.a(this.e.getAppId(), str2, "sa", "r1", 1);
                    c.a(this, this.e, this.f, str2, "sa", "r1");
                    return false;
                }
                if (b(jSONObjectA, str, str2)) {
                    strA = com.xiaomi.onetrack.b.o.a(jSONObjectA, str2, com.ot.pubsub.b.a.g, "");
                } else {
                    com.xiaomi.onetrack.util.r.a("PubSubTrackLocalImp", " This event should not upload by sampling , skip it.");
                    com.xiaomi.onetrack.h.d.b.a(this.e.getAppId(), str2, "sa", "r2", 1);
                    c.a(this, this.e, this.f, str2, "sa", "r2");
                    return false;
                }
            }
            String strB = com.xiaomi.onetrack.b.j.a().b(str);
            com.xiaomi.onetrack.util.r.a("PubSubTrackLocalImp", "bannedParamsForApp: " + strB + ", bannedParamsForEvent: " + strA);
            Set setA = com.xiaomi.onetrack.util.ab.a(strB, strA, ",");
            a(jSONObject, setA);
            a(jSONObject2, setA);
            return true;
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.b("PubSubTrackLocalImp", "oldEventOrNeedIdByCloudControl error：" + e.getMessage());
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
                com.xiaomi.onetrack.util.r.b("PubSubTrackLocalImp", "isMatchId error：" + e.toString());
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
            com.xiaomi.onetrack.util.r.b("PubSubTrackLocalImp", "isMatchId error：" + e.toString());
        }
    }

    private void a(JSONObject jSONObject, Set set) {
        if (jSONObject == null || set == null || set.size() == 0) {
            com.xiaomi.onetrack.util.r.a("PubSubTrackLocalImp", "jsonObject is null or bannedParams is empty");
            return;
        }
        com.xiaomi.onetrack.util.r.a("PubSubTrackLocalImp", "jsonObject: " + jSONObject.toString() + ", bannedParams: " + set.toString());
        try {
            Iterator<String> itKeys = jSONObject.keys();
            while (itKeys.hasNext()) {
                if (set.contains(itKeys.next())) {
                    itKeys.remove();
                }
            }
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.b("PubSubTrackLocalImp", "filterParams error：" + e.toString());
        }
    }

    private boolean b(JSONObject jSONObject, String str, String str2) {
        if (com.xiaomi.onetrack.f.b.a(this.f.b())) {
            com.xiaomi.onetrack.util.r.a("PubSubTrackLocalImp", "basicModeEnable is open,notUploadBySampling return false");
            return true;
        }
        long jA = com.xiaomi.onetrack.b.j.a().a(jSONObject, str, str2);
        long jAbs = Math.abs(DeviceUtil.b(com.xiaomi.onetrack.f.a.b()).hashCode()) % 100;
        boolean z = jA > jAbs;
        com.xiaomi.onetrack.util.r.a("PubSubTrackLocalImp", "shouldUploadBySampling " + str2 + ",  shouldUpload=" + z + ", sample=" + jA + ", val=" + jAbs);
        return z;
    }

    private boolean a(JSONObject jSONObject) {
        try {
            return jSONObject.optBoolean(com.xiaomi.onetrack.f.b.AbstractC0020b.D, false);
        } catch (Throwable unused) {
            com.xiaomi.onetrack.util.r.a("PubSubTrackLocalImp", "");
            return false;
        }
    }

    private boolean a(JSONObject jSONObject, String str) {
        boolean z = true;
        try {
            String strA = com.xiaomi.onetrack.b.o.a(jSONObject, str, "cycle", "");
            if (TextUtils.isEmpty(strA)) {
                return true;
            }
            com.xiaomi.onetrack.util.r.a("PubSubTrackLocalImp", "shouldUploadByCycle " + str + "_cycle: " + strA);
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
        com.xiaomi.onetrack.util.r.a("PubSubTrackLocalImp", "shouldUploadByCycle " + str + "_exception: " + e.getMessage());
        return z;
    }

    private void a(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str) && TextUtils.isEmpty(str2)) {
            return;
        }
        com.xiaomi.onetrack.util.r.a("PubSubTrackLocalImp", String.format(str3, str, str2));
    }
}
