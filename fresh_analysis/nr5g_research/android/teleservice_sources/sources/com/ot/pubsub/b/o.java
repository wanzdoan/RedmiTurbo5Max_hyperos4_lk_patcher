package com.ot.pubsub.b;

import android.text.TextUtils;
import com.ot.pubsub.Configuration;
import com.ot.pubsub.util.t;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class o {
    private static final String a = "ConfigEntityManager";
    private static volatile o c;
    private ConcurrentHashMap b = new ConcurrentHashMap();
    private String d = "project_id";
    private String e = "private_key_id";
    private String f = g.d;
    private String g = "international";
    private String h = com.ot.pubsub.util.a.d;
    private String i = "override_miui_region_setting";
    private String j = "need_gzip_and_encrypt";

    public static o a() {
        if (c == null) {
            synchronized (o.class) {
                try {
                    if (c == null) {
                        c = new o();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return c;
    }

    private o() {
    }

    public Configuration a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Configuration configuration = (Configuration) this.b.get(str);
        return configuration == null ? b(str) : configuration;
    }

    public void a(Configuration configuration) {
        if (configuration == null || TextUtils.isEmpty(configuration.getProjectId())) {
            return;
        }
        this.b.put(configuration.getProjectId(), configuration);
        a(configuration.getProjectId(), configuration);
    }

    private Configuration b(String str) {
        JSONObject jSONObjectOptJSONObject;
        try {
            String strC = t.c();
            if (TextUtils.isEmpty(strC) || (jSONObjectOptJSONObject = new JSONObject(com.ot.pubsub.c.a.c(strC, com.ot.pubsub.c.a.a)).optJSONObject(str)) == null) {
                return null;
            }
            return new Configuration.Builder().setAppId(jSONObjectOptJSONObject.optString(this.f)).setProjectId(jSONObjectOptJSONObject.optString(this.d)).setPrivateKeyId(jSONObjectOptJSONObject.optString(this.e)).setInternational(jSONObjectOptJSONObject.optBoolean(this.g)).setRegion(jSONObjectOptJSONObject.optString(this.h)).setOverrideMiuiRegionSetting(jSONObjectOptJSONObject.optBoolean(this.i)).setNeedGzipAndEncrypt(jSONObjectOptJSONObject.optBoolean(this.j)).build();
        } catch (Exception unused) {
            return null;
        }
    }

    private void a(String str, Configuration configuration) {
        try {
            if (!TextUtils.isEmpty(str) && configuration != null) {
                com.ot.pubsub.util.e.a(new p(this, configuration, str));
            }
        } catch (Exception e) {
            com.ot.pubsub.util.j.a(a, "putProjectIdConfigBySP Exception:" + e.getMessage());
        }
    }

    public JSONObject b(Configuration configuration) {
        if (configuration == null) {
            return null;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(this.f, configuration.getAppId());
            jSONObject.put(this.d, configuration.getProjectId());
            jSONObject.put(this.e, configuration.getPrivateKeyId());
            jSONObject.put(this.g, configuration.isInternational());
            jSONObject.put(this.h, configuration.getRegion());
            jSONObject.put(this.i, configuration.isOverrideMiuiRegionSetting());
            jSONObject.put(this.j, configuration.isNeedGzipAndEncrypt());
        } catch (Exception unused) {
        }
        return jSONObject;
    }
}
