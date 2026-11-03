package com.ot.pubsub.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/* JADX INFO: loaded from: classes.dex */
public class t {
    private static final String a = "SysPrefUtil";
    private static final String b = "one_track_pub_sub";
    private static SharedPreferences c = null;
    private static SharedPreferences.Editor d = null;
    private static final String e = "loc_token";
    private static final String f = "loc_config";
    private static final String g = "pub_sub_secret_key_data";
    private static final String h = "last_secret_key_time";
    private static final String i = "pref_custom_privacy_policy_";
    private static final String j = "pref_instance_id";
    private static final String k = "pref_instance_id_last_use_time";
    private static final String l = "custom_id";
    private static final String m = "first_launch_time";
    private static final String n = "next_update_common_conf_time";
    private static final String o = "common_cloud_data";
    private static final String p = "common_config_hash";
    private static final String q = "region_rul";
    private static final String r = "app_config_region";

    private static void m() {
        if (d != null) {
            return;
        }
        synchronized (t.class) {
            try {
                if (d == null) {
                    SharedPreferences sharedPreferences = b.a().getSharedPreferences(b, 0);
                    c = sharedPreferences;
                    d = sharedPreferences.edit();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private static String a(String str, String str2) {
        m();
        return c.getString(str, str2);
    }

    private static void b(String str, String str2) {
        m();
        d.putString(str, str2).apply();
    }

    private static long a(String str, long j2) {
        m();
        return c.getLong(str, j2);
    }

    private static void b(String str, long j2) {
        m();
        d.putLong(str, j2).apply();
    }

    private static boolean b(String str, boolean z) {
        m();
        return c.getBoolean(str, z);
    }

    private static void c(String str, boolean z) {
        m();
        d.putBoolean(str, z).apply();
    }

    private static void a(String str, float f2) {
        m();
        d.putFloat(str, f2).apply();
    }

    private static float b(String str, float f2) {
        m();
        return c.getFloat(str, f2);
    }

    public static long a() {
        return a(k, 0L);
    }

    public static void a(long j2) {
        b(k, j2);
    }

    public static void a(String str) {
        b(e, str);
    }

    public static String b() {
        return a(e, "");
    }

    public static void b(String str) {
        b(f, str);
    }

    public static String c() {
        return a(f, "");
    }

    public static void c(String str) {
        b(g, str);
    }

    public static String d() {
        return a(g, "");
    }

    public static void b(long j2) {
        b(h, j2);
    }

    public static long e() {
        return a(h, 0L);
    }

    public static boolean d(String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        return b(i + str, true);
    }

    public static void a(String str, boolean z) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        c(i + str, z);
    }

    public static String f() {
        return a(j, "");
    }

    public static void e(String str) {
        b(j, str);
        a(v.a());
    }

    public static void a(Context context, String str) {
        b(l, str);
    }

    public static String a(Context context) {
        return a(l, "");
    }

    public static long g() {
        return a(m, 0L);
    }

    public static void c(long j2) {
        b(m, j2);
    }

    public static void f(String str) {
        b(o, str);
    }

    public static String h() {
        return a(o, "");
    }

    public static void d(long j2) {
        b(n, j2);
    }

    public static long i() {
        return a(n, 0L);
    }

    public static void g(String str) {
        b(q, str);
    }

    public static String j() {
        return a(q, "");
    }

    public static void h(String str) {
        b(p, str);
    }

    public static String k() {
        return a(p, "");
    }

    public static void i(String str) {
        b(r, str);
    }

    public static String l() {
        return a(r, "");
    }
}
