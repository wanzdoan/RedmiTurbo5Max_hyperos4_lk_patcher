package com.xiaomi.onetrack.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ac {
    private static SharedPreferences c;
    private static SharedPreferences.Editor d;

    private static void G() {
        if (d != null) {
            return;
        }
        synchronized (ac.class) {
            try {
                if (d == null) {
                    SharedPreferences sharedPreferences = com.xiaomi.onetrack.f.a.a().getSharedPreferences("one_track_pref", 0);
                    c = sharedPreferences;
                    d = sharedPreferences.edit();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private static String c(String str, String str2) {
        G();
        return c.getString(str, str2);
    }

    private static void d(String str, String str2) {
        G();
        d.putString(str, str2).apply();
    }

    private static long b(String str, long j) {
        G();
        return c.getLong(str, j);
    }

    private static void c(String str, long j) {
        G();
        d.putLong(str, j).apply();
    }

    private static boolean b(String str, boolean z) {
        G();
        return c.getBoolean(str, z);
    }

    private static void c(String str, boolean z) {
        G();
        d.putBoolean(str, z).apply();
    }

    private static void t(String str) {
        G();
        d.remove(str).apply();
    }

    public static void b(long j) {
        c("last_collect_crash_time", j);
    }

    public static long b() {
        return b("last_collect_crash_time", 0L);
    }

    public static void c(long j) {
        c("report_crash_ticket", j);
    }

    public static long c() {
        return b("report_crash_ticket", 0L);
    }

    public static void a(String str) {
        d("secret_key_data", str);
    }

    public static String d() {
        return c("secret_key_data", "");
    }

    public static void b(String str) {
        d("region_rul", str);
    }

    public static String e() {
        return c("region_rul", "");
    }

    public static void d(long j) {
        c("last_secret_key_time", j);
    }

    public static void e(long j) {
        c("next_update_common_conf_time", j);
    }

    public static long g() {
        return b("next_update_common_conf_time", 0L);
    }

    public static void c(String str) {
        d("common_config_hash", str);
    }

    public static void d(String str) {
        d("common_cloud_data", str);
    }

    public static String i() {
        return c("common_cloud_data", "");
    }

    public static String j() {
        return c("pref_instance_id", "");
    }

    public static void e(String str) {
        d("pref_instance_id", str);
        f(ae.a());
    }

    public static void f(long j) {
        c("pref_instance_id_last_use_time", j);
    }

    public static void c(boolean z) {
        c("onetrack_first_open", z);
    }

    public static boolean n() {
        return b("onetrack_first_open", true);
    }

    public static long o() {
        return b("dau_last_time", 0L);
    }

    public static void g(long j) {
        c("dau_last_time", j);
    }

    public static String p() {
        return c("onetrack_user_id", "");
    }

    public static String r() {
        return c("onetrack_user_type", "");
    }

    public static String a(Context context) {
        return c("custom_id", "");
    }

    public static String u() {
        return c("page_end", "");
    }

    public static void h(String str) {
        d("page_end", str);
    }

    public static void i(String str) {
        d("last_app_version", str);
    }

    public static String v() {
        return c("last_app_version", "");
    }

    public static long w() {
        return b("first_launch_time", 0L);
    }

    public static void h(long j) {
        c("first_launch_time", j);
    }

    public static boolean j(String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        return b("pref_custom_privacy_policy_" + str, true);
    }

    public static void k(String str) {
        d("app_config_region", str);
    }

    public static String x() {
        return c("app_config_region", "");
    }

    public static long n(String str) {
        return b("get_remote_monitor_config_ticket_" + str, 0L);
    }

    public static void a(String str, long j) {
        c("get_remote_monitor_config_ticket_" + str, j);
    }

    public static String o(String str) {
        return c("monitor_config_data" + str, "");
    }

    public static void a(String str, String str2) {
        d("monitor_config_data" + str, str2);
    }

    public static long B() {
        return b("last_track_ot_monitor_event_time", 0L);
    }

    public static void j(long j) {
        c("last_track_ot_monitor_event_time", j);
    }

    public static String p(String str) {
        return c("report_ot_failed_ticket_" + str, "");
    }

    public static void b(String str, String str2) {
        d("report_ot_failed_ticket_" + str, str2);
    }

    public static void q(String str) {
        t("report_ot_failed_ticket_" + str);
    }

    public static void r(String str) {
        d("check_request_token", str);
    }

    public static String C() {
        return c("check_request_token", "");
    }

    public static void k(long j) {
        c("next_update_caught_exception_conf_time", j);
    }

    public static long D() {
        return b("next_update_caught_exception_conf_time", 0L);
    }

    public static void s(String str) {
        d("net_ad_monitor_config_data", str);
    }

    public static String E() {
        return c("net_ad_monitor_config_data", "");
    }

    public static void l(long j) {
        G();
        c("ad_m_caught_exception_day_count", j);
    }

    public static long F() {
        return b("ad_m_caught_exception_day_count", 0L);
    }
}
