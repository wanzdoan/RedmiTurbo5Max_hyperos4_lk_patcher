package com.ot.pubsub.util;

import android.text.TextUtils;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class j {
    public static boolean a = false;
    public static boolean b = false;
    public static boolean c = false;
    private static final int d = 3000;
    private static final String e = "OT-PubSub-";
    private static final int f = 0;
    private static final int g = 1;
    private static final int h = 2;
    private static final int i = 3;
    private static final int j = 4;
    private static boolean k = false;
    private static boolean l = false;
    private static boolean m = false;
    private static boolean n = false;

    public static void a() {
        try {
            String strE = b.e();
            String strA = u.a("debug.pubsub.log");
            boolean z = false;
            l = (TextUtils.isEmpty(strA) || TextUtils.isEmpty(strE) || !TextUtils.equals(strE, strA)) ? false : true;
            String strA2 = u.a("debug.pubsub.upload");
            b = (TextUtils.isEmpty(strA2) || TextUtils.isEmpty(strE) || !TextUtils.equals(strE, strA2)) ? false : true;
            String strA3 = u.a("debug.pubsub.test");
            if (!TextUtils.isEmpty(strA3) && !TextUtils.isEmpty(strE) && TextUtils.equals(strE, strA3)) {
                z = true;
            }
            n = z;
            b();
        } catch (Exception e2) {
            Log.e("OT-PubSub-Sdk", "LogUtil static initializer: " + e2.toString());
        }
        Log.d("OT-PubSub-Sdk", "log on: " + l + ", quick upload on: " + b);
    }

    public static void a(String str, String str2, Object... objArr) {
        if (a) {
            a(a(str), String.format(str2, objArr), 3);
        }
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

    public static void b(String str, String str2, Object... objArr) {
        if (a) {
            a(a(str), String.format(str2, objArr), 0);
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

    public static void c(String str, String str2, Object... objArr) {
        if (a) {
            a(a(str), String.format(str2, objArr), 1);
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

    public static void d(String str, String str2, Object... objArr) {
        if (a) {
            a(a(str), String.format(str2, objArr), 2);
        }
    }

    public static void d(String str, String str2) {
        if (a) {
            a(a(str), str2, 2);
        }
    }

    public static void d(String str, String str2, Throwable th) {
        if (a) {
            Log.i(a(str), str2, th);
        }
    }

    private static void a(String str, String str2, int i2) {
        if (str2 == null) {
            return;
        }
        int i3 = 0;
        while (i3 <= str2.length() / d) {
            int i4 = i3 * d;
            i3++;
            int iMin = Math.min(str2.length(), i3 * d);
            if (i4 < iMin) {
                String strSubstring = str2.substring(i4, iMin);
                if (i2 == 0) {
                    Log.e(str, strSubstring);
                } else if (i2 == 1) {
                    Log.w(str, strSubstring);
                } else if (i2 == 2) {
                    Log.i(str, strSubstring);
                } else if (i2 == 3) {
                    Log.d(str, strSubstring);
                } else if (i2 == 4) {
                    Log.v(str, strSubstring);
                }
            }
        }
    }

    public static String a(String str) {
        return e + str;
    }

    public static void a(boolean z) {
        k = z;
        b();
    }

    public static void b(boolean z) {
        m = z;
        c();
    }

    private static void b() {
        a = k || l;
        Log.d("OT-PubSub-Sdk", "updateDebugSwitch sEnable: " + a + " sDebugMode：" + k + " sDebugProperty：" + l);
    }

    private static void c() {
        c = m || n;
        Log.d("OneTrackSdk", "updateTestSwitch sTestEnable: " + c + " sTestMode：" + m + " sTestProperty：" + n);
    }
}
