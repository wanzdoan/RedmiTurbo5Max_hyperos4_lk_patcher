package com.ot.pubsub.a;

import android.content.Context;
import android.text.TextUtils;
import com.ot.pubsub.Configuration;
import com.ot.pubsub.PubSubTrack;
import com.ot.pubsub.util.DeviceUtil;
import com.ot.pubsub.util.s;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class q implements c {
    private static final String a = "PubSubTrackLocalImp";
    private static final int b = 102400;
    private static final int c = 2;
    private Configuration d;
    private com.ot.pubsub.util.q e;

    public q(Context context, Configuration configuration, com.ot.pubsub.util.q qVar) {
        this.d = configuration;
        this.e = qVar;
    }

    @Override // com.ot.pubsub.a.c
    public void a(String str, String str2, String str3, String str4, Map map) {
        com.ot.pubsub.util.q qVar = this.e;
        if (qVar != null && !qVar.a(str3)) {
            com.ot.pubsub.util.j.a(a, "The privacy policy is not permitted, and the event is not basic or recommend event or custom dau event, skip it.");
            return;
        }
        if (a(str3, str4)) {
            if (!com.ot.pubsub.g.i.a()) {
                com.ot.pubsub.util.j.a(a, "track network is unaccessable, cta");
                com.ot.pubsub.g.i.a(str, str2, str3, str4, map, true);
                return;
            }
            com.ot.pubsub.g.i.a(this);
            if (com.ot.pubsub.util.j.a) {
                com.ot.pubsub.util.j.a(a, "track pb ot data:" + str4);
            }
            com.ot.pubsub.b.e.a();
            com.ot.pubsub.b.a.a().b(this.d.getAppId());
            b(str, str2, str3, str4, map);
        }
    }

    @Override // com.ot.pubsub.a.c
    public void a(String str, String str2, String str3, Map map, boolean z) throws Throwable {
        if (!com.ot.pubsub.g.i.a()) {
            com.ot.pubsub.util.j.a(a, "network is unaccessable, cta");
            com.ot.pubsub.g.i.a(str, str2, "", str3, map, z);
            return;
        }
        com.ot.pubsub.g.i.a(this);
        if (com.ot.pubsub.util.j.a) {
            com.ot.pubsub.util.j.a(a, "track pb data:" + str3);
        }
        com.ot.pubsub.g.c.a().a(str, str2, str3, map, z);
    }

    @Override // com.ot.pubsub.a.c
    public void a(int i) {
        if (i == 2) {
            com.ot.pubsub.e.a.a(new r(this));
        }
    }

    public boolean a(String str, String str2) {
        if (PubSubTrack.isDisable()) {
            com.ot.pubsub.util.j.a(a, "Tracking data is disabled or onetrack use system net traffic only, skip it.");
            return false;
        }
        if (!a()) {
            return false;
        }
        if ((str != null && str.equals(a.f)) || str2 == null || str2.length() * 2 <= b) {
            return true;
        }
        com.ot.pubsub.util.j.a(a, "Event size exceed limitation!");
        return false;
    }

    private boolean a() {
        String strI = com.ot.pubsub.util.l.i();
        if (!TextUtils.isEmpty(strI) && TextUtils.equals(strI.toUpperCase(), com.ot.pubsub.g.l.b)) {
            com.ot.pubsub.util.j.a(a, "region is RU,Not allowed pubsub");
            return false;
        }
        if (TextUtils.isEmpty(strI) || !TextUtils.equals(strI.toUpperCase(), com.ot.pubsub.g.l.a)) {
            return true;
        }
        com.ot.pubsub.util.j.a(a, "region is CN,Not allowed pubsub");
        return false;
    }

    private void b(String str, String str2, String str3, String str4, Map map) {
        String strOptString;
        String strA = "";
        if (a(this.d.getAppId())) {
            com.ot.pubsub.util.j.a(a, "This app disabled tracking data, skip it.");
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str4);
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject(b.b);
            JSONObject jSONObjectOptJSONObject2 = jSONObject.optJSONObject(b.a);
            String strA2 = com.ot.pubsub.b.h.a().a(this.d.getAppId(), str3, com.ot.pubsub.b.a.f, "");
            if (jSONObjectOptJSONObject2 == null) {
                strOptString = "";
            } else {
                strOptString = jSONObjectOptJSONObject2.optString(a.ac);
            }
            com.ot.pubsub.util.j.a(a, "tip: " + strOptString + ", needIds: " + strA2);
            if (b(strOptString, strA2)) {
                if (c(this.d.getAppId(), str3)) {
                    com.ot.pubsub.util.j.a(a, " This event disabled tracking data , skip it.");
                    return;
                } else if (!d(this.d.getAppId(), str3)) {
                    strA = com.ot.pubsub.b.h.a().a(this.d.getAppId(), str3, com.ot.pubsub.b.a.g, "");
                } else {
                    com.ot.pubsub.util.j.a(a, " This event should not upload by sampling , skip it.");
                    return;
                }
            }
            String strC = com.ot.pubsub.b.h.a().c(this.d.getAppId());
            com.ot.pubsub.util.j.a(a, "bannedParamsForApp: " + strC + ", bannedParamsForEvent: " + strA);
            Set setA = s.a(strC, strA, ",");
            a(jSONObjectOptJSONObject, setA);
            a(jSONObjectOptJSONObject2, setA);
            com.ot.pubsub.f.b.a(str, str2, this.d.getAppId(), com.ot.pubsub.util.b.e(), str3, jSONObject.toString(), map);
        } catch (JSONException e) {
            com.ot.pubsub.util.j.b(a, "checkCloudControl error：" + e.toString());
        }
    }

    private boolean b(String str, String str2) {
        List listA;
        if (TextUtils.isEmpty(str2)) {
            return true;
        }
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            String[] strArrSplit = str.split(s.a);
            return strArrSplit != null && strArrSplit.length >= 5 && (listA = s.a(str2, ",")) != null && listA.contains(strArrSplit[4]);
        } catch (Exception e) {
            com.ot.pubsub.util.j.b(a, "isMatchId error：" + e.toString());
        }
    }

    private void a(JSONObject jSONObject, Set set) {
        if (jSONObject == null || set == null || set.size() == 0) {
            com.ot.pubsub.util.j.a(a, "jsonObject is null or bannedParams is empty");
            return;
        }
        com.ot.pubsub.util.j.a(a, "jsonObject: " + jSONObject.toString() + ", bannedParams: " + set.toString());
        try {
            Iterator<String> itKeys = jSONObject.keys();
            while (itKeys.hasNext()) {
                if (set.contains(itKeys.next())) {
                    itKeys.remove();
                }
            }
        } catch (Exception e) {
            com.ot.pubsub.util.j.b(a, "filterParams error：" + e.toString());
        }
    }

    private boolean a(String str) {
        try {
            return com.ot.pubsub.b.h.a().a(str, com.ot.pubsub.b.a.a);
        } catch (Exception e) {
            com.ot.pubsub.util.j.b(a, "isDisableTrackForApp error: " + e.toString());
            return false;
        }
    }

    private boolean c(String str, String str2) {
        try {
            return com.ot.pubsub.b.h.a().a(str, str2, com.ot.pubsub.b.a.a, false);
        } catch (Exception e) {
            com.ot.pubsub.util.j.b(a, "isDisableTrackForEvent error: " + e.toString());
            return false;
        }
    }

    private boolean d(String str, String str2) {
        long jB = com.ot.pubsub.b.h.a().b(str, str2);
        long jAbs = Math.abs(DeviceUtil.a(com.ot.pubsub.util.b.b()).hashCode()) % 100;
        boolean z = jB > jAbs;
        com.ot.pubsub.util.j.a(a, "shouldUploadBySampling " + str2 + ",  shouldUpload=" + z + ", sample=" + jB + ", val=" + jAbs);
        return !z;
    }
}
