package com.ot.pubsub.util;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes.dex */
public class l {
    public static final int a = 29;
    public static final int b = 25;
    public static final int c = 24;
    public static final int d = 23;
    public static final int e = 22;
    public static final int f = 21;
    public static final int g = 19;
    public static final int h = 17;
    private static final String i = "OsUtil";
    private static Class j = null;
    private static Method k = null;
    private static Boolean l = null;
    private static final long m = 1024;
    private static final long n = 1024;
    private static final long o = 1048576;
    private static final long p = 1073741824;
    private static final int q = 1;
    private static final int r = 2;
    private static final String s = "";
    private static boolean t = false;
    private static String u = null;
    private static boolean v = false;

    static {
        try {
            j = Class.forName("miui.os.Build");
        } catch (Throwable th) {
            j.b(i, "sMiuiBuild init failed ex: " + th.getMessage());
        }
        try {
            Method declaredMethod = Class.forName("android.provider.MiuiSettings$Secure").getDeclaredMethod("isUserExperienceProgramEnable", ContentResolver.class);
            k = declaredMethod;
            declaredMethod.setAccessible(true);
        } catch (Throwable th2) {
            j.b(i, "sMiuiUEPMethod init failed ex: " + th2.getMessage());
        }
    }

    public static boolean a() {
        Boolean bool = l;
        if (bool != null) {
            return bool.booleanValue();
        }
        if (!TextUtils.isEmpty(u.a("ro.miui.ui.version.code"))) {
            l = Boolean.TRUE;
        } else {
            l = Boolean.FALSE;
        }
        return l.booleanValue();
    }

    public static String b() {
        return a(TimeZone.getDefault().getRawOffset());
    }

    public static String a(int i2) {
        char c2;
        try {
            int i3 = i2 / 60000;
            if (i3 < 0) {
                i3 = -i3;
                c2 = '-';
            } else {
                c2 = '+';
            }
            StringBuilder sb = new StringBuilder(9);
            sb.append("GMT");
            sb.append(c2);
            a(sb, i3 / 60);
            sb.append(':');
            a(sb, i3 % 60);
            return sb.toString();
        } catch (Exception unused) {
            return "";
        }
    }

    private static void a(StringBuilder sb, int i2) {
        String string = Integer.toString(i2);
        for (int i3 = 0; i3 < 2 - string.length(); i3++) {
            sb.append('0');
        }
        sb.append(string);
    }

    public static String c() {
        Class cls = j;
        if (cls != null) {
            try {
                if (((Boolean) cls.getField("IS_ALPHA_BUILD").get(null)).booleanValue()) {
                    return "A";
                }
                if (((Boolean) j.getField("IS_DEVELOPMENT_VERSION").get(null)).booleanValue()) {
                    return "D";
                }
                if (((Boolean) j.getField("IS_STABLE_VERSION").get(null)).booleanValue()) {
                    return "S";
                }
                return "";
            } catch (Exception e2) {
                Log.e(i, "getRomBuildCode failed: " + e2.toString());
                return "";
            }
        }
        return "";
    }

    public static boolean a(Context context) {
        Method method = k;
        if (method == null) {
            return true;
        }
        try {
            return ((Boolean) method.invoke(null, context.getContentResolver())).booleanValue();
        } catch (Exception e2) {
            Log.e(i, "isUserExperiencePlanEnabled failed: " + e2.toString());
            return true;
        }
    }

    public static String d() {
        return Build.VERSION.INCREMENTAL;
    }

    private static String j() {
        try {
            String strA = u.a("ro.miui.region", "");
            if (TextUtils.isEmpty(strA)) {
                strA = u.a("ro.product.locale.region", "");
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
            j.b(i, "getRegion Exception: " + th.getMessage());
        }
        return "";
    }

    public static String e() {
        return Build.VERSION.RELEASE;
    }

    public static int f() {
        Integer num;
        Throwable th;
        try {
            Method declaredMethod = Class.forName("android.os.UserHandle").getDeclaredMethod("getUserId", Integer.TYPE);
            declaredMethod.setAccessible(true);
            int iMyUid = Process.myUid();
            num = (Integer) declaredMethod.invoke(null, Integer.valueOf(iMyUid));
            try {
                j.a(i, String.format("getUserId, uid:%d, userId:%d", Integer.valueOf(iMyUid), num));
            } catch (Throwable th2) {
                th = th2;
                Log.e(j.a(i), "getUserId exception: " + th.getMessage());
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

    public static boolean a(String str) {
        try {
            return ((Boolean) Class.forName("miui.os.MiuiInit").getMethod("isPreinstalledPackage", String.class).invoke(null, str)).booleanValue();
        } catch (ClassNotFoundException e2) {
            if (a()) {
                Log.e(j.a(i), "checkPreinstallApp failed:" + e2.getMessage());
            }
            return false;
        } catch (Throwable th) {
            Log.e(j.a(i), "checkPreinstallApp failed:" + th.getMessage());
            return false;
        }
    }

    public static boolean b(String str) {
        try {
            return ((Boolean) Class.forName("miui.os.MiuiInit").getMethod("isPreinstalledPAIPackage", String.class).invoke(null, str)).booleanValue();
        } catch (Throwable th) {
            Log.e(j.a(i), "isPreinstalledPAIPackage failed:" + th.getMessage());
            return false;
        }
    }

    public static boolean c(String str) {
        if (g()) {
            return false;
        }
        j.c(str, "should not access network or location, not provisioned");
        return true;
    }

    public static boolean g() {
        try {
            return Settings.Global.getInt(b.b().getContentResolver(), "device_provisioned", 0) != 0;
        } catch (Exception e2) {
            j.b(i, "isDeviceProvisioned exception：", e2);
            return true;
        }
    }

    private static boolean k() {
        Class cls = j;
        if (cls != null) {
            try {
                return ((Boolean) cls.getField("IS_INTERNATIONAL_BUILD").get(null)).booleanValue();
            } catch (Exception unused) {
            }
        }
        String strJ = j();
        if (TextUtils.isEmpty(strJ)) {
            return false;
        }
        return !TextUtils.equals(com.ot.pubsub.g.l.a, strJ.toUpperCase());
    }

    public static void a(boolean z, String str) {
        t = z;
        u = str;
    }

    public static boolean h() {
        if (a() && !v) {
            return k();
        }
        return t;
    }

    public static String i() {
        if (!a() || v) {
            return !TextUtils.isEmpty(u) ? u : j();
        }
        return j();
    }

    public static void a(boolean z) {
        v = z;
    }
}
