package com.xiaomi.onetrack;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import android.content.Context;
import com.xiaomi.onetrack.api.ar;
import com.xiaomi.onetrack.f.a;
import com.xiaomi.onetrack.util.j;
import com.xiaomi.onetrack.util.r;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class OneTrack {
    private static boolean b = false;
    private static volatile int c = 0;
    private static volatile boolean d = false;
    private static boolean e;
    private static boolean f;
    private static HttpRequestProperty h;
    private ar g;

    public interface ICommonPropertyProvider {
    }

    public interface IEventHook {
        boolean isCustomDauEvent(String str);

        boolean isRecommendEvent(String str);
    }

    public enum HttpRequestProperty {
        KEEP_ALIVE("keep-alive"),
        CLOSE("close");

        private String a;

        HttpRequestProperty(String str) {
            this.a = str;
        }

        public String getType() {
            return this.a;
        }
    }

    public enum Mode {
        APP("app"),
        PLUGIN("plugin"),
        SDK("sdk");

        private String a;

        Mode(String str) {
            this.a = str;
        }

        public String getType() {
            return this.a;
        }
    }

    public enum NetType {
        NOT_CONNECTED("NONE"),
        MOBILE_2G("2G"),
        MOBILE_3G("3G"),
        MOBILE_4G("4G"),
        MOBILE_5G("5G"),
        WIFI("WIFI"),
        ETHERNET("ETHERNET"),
        UNKNOWN("UNKNOWN"),
        CONNECTED("CONNECTED");

        private String a;

        NetType(String str) {
            this.a = str;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.a;
        }
    }

    private OneTrack(Context context, Configuration configuration) {
        a.a(context.getApplicationContext());
        this.g = new ar(context, configuration);
        setEventHook(new DefaultEventHook());
    }

    public static OneTrack createInstance(Context context, Configuration configuration) {
        return new OneTrack(context, configuration);
    }

    public static void setDebugMode(boolean z) {
        r.a(z);
    }

    public static boolean isDisable() {
        return b;
    }

    public static void setUseSystemNetTrafficOnly() {
        e = true;
    }

    public static boolean isUseSystemNetTrafficOnly() {
        return e;
    }

    public static void setAccessNetworkEnable(Context context, final boolean z) {
        a(context);
        j.a(new Runnable() { // from class: com.xiaomi.onetrack.OneTrack.1
            @Override // java.lang.Runnable
            public void run() {
                com.xiaomi.onetrack.c.j.a(z);
                com.xiaomi.onetrack.c.j.b(z);
            }
        });
    }

    public static boolean isRestrictGetNetworkInfo() {
        return f;
    }

    public static HttpRequestProperty getHttpReqPropConnection() {
        return h;
    }

    private static void a(Context context) {
        if (context == null) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("context is null!");
        } else {
            a.a(context.getApplicationContext());
        }
    }

    public void track(String str, Map map) {
        this.g.a(str, map);
    }

    public String getInstanceId() {
        return this.g.e();
    }

    public void setEventHook(IEventHook iEventHook) {
        this.g.a(iEventHook);
    }

    public static int getGlobalBasicModeEnable() {
        return c;
    }

    public static boolean getForceDeviceProvisioned() {
        return d;
    }
}
