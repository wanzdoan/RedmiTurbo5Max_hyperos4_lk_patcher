package com.ot.pubsub;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.ot.pubsub.a.d;
import com.ot.pubsub.b.o;
import com.ot.pubsub.g.i;
import com.ot.pubsub.util.b;
import com.ot.pubsub.util.e;
import com.ot.pubsub.util.j;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class PubSubTrack {
    private static final String a = "PubSubAnalytics";
    private static volatile PubSubTrack b = null;
    private static volatile boolean c = false;
    private Configuration d;
    private d e;

    public interface ICommonPropertyProvider {
        Map getDynamicProperty(String str);
    }

    public interface IEventHook {
        boolean isCustomDauEvent(String str);

        boolean isRecommendEvent(String str);
    }

    public enum NetType {
        NOT_CONNECTED("NONE"),
        MOBILE_2G("2G"),
        MOBILE_3G("3G"),
        MOBILE_4G("4G"),
        MOBILE_5G("5G"),
        WIFI("WIFI"),
        ETHERNET("ETHERNET"),
        UNKNOWN("UNKNOWN");

        private String a;

        NetType(String str) {
            this.a = str;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.a;
        }
    }

    private PubSubTrack(Context context, Configuration configuration) {
        this.d = configuration;
        o.a().a(configuration);
        b.a(context.getApplicationContext());
        this.e = new d(context, configuration);
    }

    public static PubSubTrack createInstance(Context context, Configuration configuration) {
        if (configuration == null || TextUtils.isEmpty(configuration.getProjectId()) || TextUtils.isEmpty(configuration.getPrivateKeyId())) {
            if (j.a) {
                OrtUtil$$ExternalSyntheticBUOutline0.m("PubSubAnalytics init error Configuration , ProjectId , PrivateKeyId can't null !!!");
                return null;
            }
            Log.e(a, "PubSubAnalytics init error ,Configuration , ProjectId , PrivateKeyId can't null !!!");
        }
        return new PubSubTrack(context, configuration);
    }

    @Deprecated
    public void publish(String str, String str2) {
        publish(str, str2, new HashMap());
    }

    @Deprecated
    public void publish(String str, String str2, Map map) {
        Configuration configuration = this.d;
        if (configuration == null || this.e == null || TextUtils.isEmpty(configuration.getProjectId()) || TextUtils.isEmpty(this.d.getPrivateKeyId()) || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || isDisable()) {
            j.a(a, "The Configuration or ProjectId or PrivateKeyId or Topic or data is empty, skip it!");
            return;
        }
        if (map == null) {
            map = new HashMap();
        }
        this.e.a(this.d.getProjectId(), str, str2, map, this.d.isNeedGzipAndEncrypt());
    }

    public void track(String str, String str2, Map map) {
        track(str, str2, map, new HashMap());
    }

    public void track(String str, String str2, Map map, Map map2) {
        Configuration configuration = this.d;
        if (configuration == null || this.e == null || TextUtils.isEmpty(configuration.getAppId()) || TextUtils.isEmpty(this.d.getProjectId()) || TextUtils.isEmpty(this.d.getPrivateKeyId()) || TextUtils.isEmpty(str) || isDisable()) {
            j.a(a, "The Configuration or ProjectId or PrivateKeyId or AppId or Topic or data is empty, skip it!");
            return;
        }
        if (map == null) {
            map = new HashMap();
        }
        Map map3 = map;
        if (map2 == null) {
            map2 = new HashMap();
        }
        this.e.a(this.d.getProjectId(), str, str2, map3, map2);
    }

    public static void setDebugMode(boolean z) {
        j.a(z);
    }

    public void setUploadInterval(int i) {
        this.e.a(i);
    }

    public static void setDisable(boolean z) {
        c = z;
    }

    public static boolean isDisable() {
        return c;
    }

    public static void setTestMode(boolean z) {
        j.b(z);
    }

    public void setEventHook(IEventHook iEventHook) {
        this.e.a(iEventHook);
    }

    public static void setAccessNetworkEnable(Context context, final boolean z) {
        a(context);
        e.a(new Runnable() { // from class: com.ot.pubsub.PubSubTrack.1
            @Override // java.lang.Runnable
            public void run() {
                i.a(z);
            }
        });
    }

    private static void a(Context context) {
        if (context == null) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("context is null!");
        } else {
            b.a(context.getApplicationContext());
        }
    }

    public void setDynamicCommonProperty(ICommonPropertyProvider iCommonPropertyProvider) {
        this.e.a(iCommonPropertyProvider);
    }

    public void setCommonProperty(Map map) {
        this.e.a(map);
    }

    public void clearCommonProperty() {
        this.e.a();
    }

    public void removeCommonProperty(String str) {
        this.e.a(str);
    }

    public void setCustomPrivacyPolicyAccepted(boolean z) {
        this.e.a(z);
    }

    public void setInstanceId(String str) {
        this.e.b(str);
    }

    public String getInstanceId() throws PubSubMainThreadException {
        return this.e.b();
    }
}
