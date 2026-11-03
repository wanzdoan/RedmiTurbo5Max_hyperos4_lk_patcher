package com.xiaomi.onetrack.util;

import android.text.TextUtils;
import android.util.Log;

/* JADX INFO: loaded from: classes2.dex */
public abstract class r {
    public static boolean a = false;
    public static boolean b = false;
    public static boolean c = false;
    private static boolean k = false;
    private static boolean l = false;
    private static boolean m = false;
    private static boolean n = false;

    public static void a() {
        try {
            String strE = com.xiaomi.onetrack.f.a.e();
            String strA = ad.a("debug.onetrack.log");
            boolean z = false;
            l = (TextUtils.isEmpty(strA) || TextUtils.isEmpty(strE) || !TextUtils.equals(strE, strA)) ? false : true;
            String strA2 = ad.a("debug.onetrack.upload");
            b = (TextUtils.isEmpty(strA2) || TextUtils.isEmpty(strE) || !TextUtils.equals(strE, strA2)) ? false : true;
            String strA3 = ad.a("debug.onetrack.test");
            if (!TextUtils.isEmpty(strA3) && !TextUtils.isEmpty(strE) && TextUtils.equals(strE, strA3)) {
                z = true;
            }
            n = z;
            b();
            c();
        } catch (Exception e) {
            Log.e("OneTrackSdk", "LogUtil static initializer: " + e.toString());
        }
        Log.d("OneTrackSdk", "log on: " + l + ", quick upload on: " + b);
    }

    public static void a(String str, String str2) {
        if (a) {
            a(a(str), str2, 3);
        }
    }

    public static void a(String str, String str2, Throwable th) {
        if (a) {
            Log.d(a(str), str2, th);
        }
    }

    public static void b(String str, String str2) {
        if (a) {
            a(a(str), str2, 0);
        }
    }

    public static void b(String str, String str2, Throwable th) {
        if (a) {
            Log.e(a(str), str2, th);
        }
    }

    public static void c(String str, String str2) {
        if (a) {
            a(a(str), str2, 1);
        }
    }

    public static void c(String str, String str2, Throwable th) {
        if (a) {
            Log.w(a(str), str2, th);
        }
    }

    public static void d(String str, String str2) {
        if (a) {
            a(a(str), str2, 2);
        }
    }

    private static void a(String str, String str2, int i) {
        if (str2 == null) {
            return;
        }
        int i2 = 0;
        while (i2 <= str2.length() / 3000) {
            int i3 = i2 * 3000;
            i2++;
            int iMin = Math.min(str2.length(), i2 * 3000);
            if (i3 < iMin) {
                String strSubstring = str2.substring(i3, iMin);
                if (i == 0) {
                    Log.e(str, strSubstring);
                } else if (i == 1) {
                    Log.w(str, strSubstring);
                } else if (i == 2) {
                    Log.i(str, strSubstring);
                } else if (i == 3) {
                    Log.d(str, strSubstring);
                } else if (i == 4) {
                    Log.v(str, strSubstring);
                }
            }
        }
    }

    public static String a(String str) {
        return "OneTrack-Api-" + str;
    }

    public static void a(boolean z) {
        k = z;
        b();
    }

    private static void b() {
        a = k || l;
        Log.d("OneTrackSdk", "updateDebugSwitch sEnable: " + a + " sDebugMode：" + k + " sDebugProperty：" + l);
    }

    private static void c() {
        c = m || n;
        Log.d("OneTrackSdk", "updateTestSwitch sTestEnable: " + c + " sTestMode：" + m + " sTestProperty：" + n);
    }
}
