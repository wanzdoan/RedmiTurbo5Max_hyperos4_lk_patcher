package com.xiaomi.onetrack.util;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import com.xiaomi.onetrack.OneTrack;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes2.dex */
public abstract class s {
    private static Class n = null;
    private static Method o = null;
    private static Boolean p = null;
    private static Method r = null;
    private static boolean s = false;
    private static String t = null;
    private static boolean u = false;
    private static int v = 0;
    private static boolean w = false;
    private static String x;

    static {
        try {
            r = Class.forName("android.os.SystemProperties").getMethod("get", String.class);
        } catch (Throwable th) {
            r.b("OsUtil", "sGetProp init failed ex: " + th.getMessage());
        }
    }

    public static void a() {
        if (w) {
            return;
        }
        w = true;
        try {
            n = Class.forName("miui.os.Build");
        } catch (Throwable th) {
            r.b("OsUtil", "sMiuiBuild init failed ex: " + th.getMessage());
        }
        try {
            Method declaredMethod = Class.forName("android.provider.MiuiSettings$Secure").getDeclaredMethod("isUserExperienceProgramEnable", ContentResolver.class);
            o = declaredMethod;
            declaredMethod.setAccessible(true);
        } catch (Throwable th2) {
            r.b("OsUtil", "sMiuiUEPMethod init failed ex: " + th2.getMessage());
        }
    }

    private static String d(String str) {
        try {
            Method method = r;
            if (method != null) {
                return String.valueOf(method.invoke(null, str));
            }
        } catch (Throwable th) {
            r.b("OsUtil", "getProp failed ex: " + th.getMessage());
        }
        return null;
    }

    public static boolean b() {
        Boolean bool = p;
        if (bool != null) {
            return bool.booleanValue();
        }
        if (!TextUtils.isEmpty(d("ro.miui.ui.version.code"))) {
            p = Boolean.TRUE;
        } else {
            p = Boolean.FALSE;
        }
        return p.booleanValue();
    }

    public static String c() {
        return a(TimeZone.getDefault().getOffset(System.currentTimeMillis()));
    }

    public static String a(int i) {
        char c;
        try {
            int i2 = i / 60000;
            if (i2 < 0) {
                i2 = -i2;
                c = '-';
            } else {
                c = '+';
            }
            StringBuilder sb = new StringBuilder(9);
            sb.append("GMT");
            sb.append(c);
            a(sb, i2 / 60);
            sb.append(':');
            a(sb, i2 % 60);
            return sb.toString();
        } catch (Exception unused) {
            return "";
        }
    }

    private static void a(StringBuilder sb, int i) {
        String string = Integer.toString(i);
        for (int i2 = 0; i2 < 2 - string.length(); i2++) {
            sb.append('0');
        }
        sb.append(string);
    }

    public static String d() {
        if (!TextUtils.isEmpty(x)) {
            return x;
        }
        Class cls = n;
        if (cls != null) {
            try {
                if (((Boolean) cls.getField("IS_ALPHA_BUILD").get(null)).booleanValue()) {
                    x = "A";
                    return "A";
                }
                String str = Build.VERSION.INCREMENTAL;
                boolean zContains = str.contains(".DEV");
                boolean zContains2 = str.contains(".BETA");
                boolean zBooleanValue = ((Boolean) n.getField("IS_DEVELOPMENT_VERSION").get(null)).booleanValue();
                if (((Boolean) n.getField("IS_STABLE_VERSION").get(null)).booleanValue()) {
                    x = "S";
                    return "S";
                }
                if (zBooleanValue && zContains2) {
                    x = com.ot.pubsub.a.b.a;
                    return com.ot.pubsub.a.b.a;
                }
                if (zBooleanValue && zContains) {
                    x = "X";
                    return "X";
                }
                if (zBooleanValue) {
                    x = "D";
                    return "D";
                }
                return "";
            } catch (Exception e) {
                Log.e(r.a("OsUtil"), "getRomBuildCode failed: " + e.getMessage());
                return "";
            }
        }
        return "";
    }

    public static boolean a(Context context) {
        if (o == null) {
            try {
                if (b()) {
                    int i = Settings.Secure.getInt(context.getContentResolver(), "upload_log_pref", -1);
                    r.a("OsUtil", "isUserExperiencePlanEnabled upload_log_value: " + i);
                    if (i != 1 && i == 0) {
                        return false;
                    }
                }
                return true;
            } catch (Throwable th) {
                r.a("OsUtil", "Settings failed: " + th.toString());
            }
        }
        try {
            return ((Boolean) o.invoke(null, context.getContentResolver())).booleanValue();
        } catch (Throwable th2) {
            Log.d("OsUtil", "isUserExperiencePlanEnabled failed: " + th2.getMessage());
            return true;
        }
    }

    public static boolean a(String str) {
        if (OneTrack.isDisable() || OneTrack.isUseSystemNetTrafficOnly()) {
            r.c(str, "should not access network or location, cta");
            return true;
        }
        if (!i()) {
            r.c(str, "should not access network or location, not provisioned");
            return true;
        }
        if (com.xiaomi.onetrack.c.j.b()) {
            return false;
        }
        r.c(str, "should not access network or location, cta");
        return true;
    }

    public static String e() {
        return Build.VERSION.INCREMENTAL;
    }

    public static String f() {
        return ad.a("ro.mi.os.version.incremental", "");
    }

    private static String o() {
        try {
            String strA = ad.a("ro.miui.region", "");
            if (TextUtils.isEmpty(strA)) {
                strA = ad.a("ro.product.locale.region", "");
            }
            if (TextUtils.isEmpty(strA)) {
                Class[] clsArr = new Class[0];
                Object objInvoke = Class.forName("android.os.LocaleList").getMethod("getDefault", null).invoke(null, null);
                Class[] clsArr2 = new Class[0];
                Object objInvoke2 = objInvoke.getClass().getMethod("size", null).invoke(objInvoke, null);
                if ((objInvoke2 instanceof Integer) && ((Integer) objInvoke2).intValue() > 0) {
                    Object objInvoke3 = objInvoke.getClass().getMethod("get", Integer.TYPE).invoke(objInvoke, 0);
                    Class[] clsArr3 = new Class[0];
                    Object objInvoke4 = objInvoke3.getClass().getMethod("getCountry", null).invoke(objInvoke3, null);
                    if (objInvoke4 instanceof String) {
                        strA = (String) objInvoke4;
                    }
                }
            }
            if (TextUtils.isEmpty(strA)) {
                strA = Locale.getDefault().getCountry();
            }
            if (!TextUtils.isEmpty(strA)) {
                return strA.trim();
            }
        } catch (Throwable th) {
            r.b("OsUtil", "getRegion Exception: " + th.getMessage());
        }
        return "";
    }

    public static String g() {
        return Build.VERSION.RELEASE;
    }

    public static int h() {
        Integer num;
        Throwable th;
        try {
            Method declaredMethod = Class.forName("android.os.UserHandle").getDeclaredMethod("getUserId", Integer.TYPE);
            declaredMethod.setAccessible(true);
            int iMyUid = Process.myUid();
            num = (Integer) declaredMethod.invoke(null, Integer.valueOf(iMyUid));
            try {
                r.a("OsUtil", String.format("getUserId, uid:%d, userId:%d", Integer.valueOf(iMyUid), num));
            } catch (Throwable th2) {
                th = th2;
                Log.e(r.a("OsUtil"), "getUserId exception: " + th.getMessage());
            }
        } catch (Throwable th3) {
            num = null;
            th = th3;
        }
        if (num == null) {
            num = 0;
        }
        return num.intValue();
    }

    public static boolean i() {
        try {
            if (OneTrack.getForceDeviceProvisioned()) {
                r.c("OsUtil", "force setting device provisioned, don't read the system real attribute value");
                return true;
            }
            boolean z = Settings.Global.getInt(com.xiaomi.onetrack.f.a.b().getContentResolver(), "device_provisioned", 0) != 0;
            if (!z) {
                r.c("OsUtil", "Provisioned: " + z);
            }
            return z;
        } catch (Throwable th) {
            r.b("OsUtil", "isDeviceProvisioned exception：" + th.getMessage());
            return true;
        }
    }

    public static boolean j() {
        Class cls = n;
        if (cls != null) {
            try {
                return ((Boolean) cls.getField("IS_INTERNATIONAL_BUILD").get(null)).booleanValue();
            } catch (Throwable unused) {
            }
        }
        String strO = o();
        if (TextUtils.isEmpty(strO)) {
            return false;
        }
        return !TextUtils.equals(com.ot.pubsub.g.l.a, strO.toUpperCase());
    }

    public static boolean k() {
        if (b() && !u) {
            return j();
        }
        String strO = t;
        if (TextUtils.isEmpty(strO)) {
            strO = o();
        }
        return (TextUtils.isEmpty(strO) || TextUtils.equals(strO, com.ot.pubsub.g.l.a)) ? false : true;
    }

    public static String l() {
        String strO = ((!b() || u) && !TextUtils.isEmpty(t)) ? t : o();
        return ab.b(strO) ? strO.toUpperCase() : strO;
    }

    public static void a(boolean z, String str, OneTrack.Mode mode) {
        int i;
        if (mode == OneTrack.Mode.APP) {
            i = 3;
        } else if (mode == OneTrack.Mode.PLUGIN) {
            i = 2;
        } else {
            i = mode == OneTrack.Mode.SDK ? 1 : 0;
        }
        if (v <= i) {
            s = z;
            t = str;
            v = i;
        }
    }

    public static void a(boolean z) {
        u = z;
    }

    public static String m() {
        String strO = o();
        return ab.b(strO) ? strO.toUpperCase() : strO;
    }

    public static boolean b(String str) {
        if (!ab.b(str)) {
            str = l();
        }
        return a.o.contains(str.toUpperCase());
    }

    public static String n() {
        String strL = l();
        if (ab.b(strL)) {
            if ("IN".equalsIgnoreCase(strL)) {
                return "in";
            }
            if (a.n.contains(strL.toUpperCase())) {
                return "eu";
            }
            r.a("OsUtil", "current region：" + strL + "，no match ,select default sg");
        }
        return "sg";
    }

    public static String c(String str) {
        if (!TextUtils.isEmpty(str)) {
            if (com.ot.pubsub.g.l.a.equalsIgnoreCase(str)) {
                return com.ot.pubsub.g.l.a;
            }
            if (com.ot.pubsub.g.l.b.equalsIgnoreCase(str)) {
                return com.ot.pubsub.g.l.b;
            }
            if ("IN".equalsIgnoreCase(str)) {
                return "IN";
            }
            if (a.n.contains(str.toUpperCase())) {
                return com.ot.pubsub.util.a.f;
            }
            return "SG";
        }
        return "";
    }
}
