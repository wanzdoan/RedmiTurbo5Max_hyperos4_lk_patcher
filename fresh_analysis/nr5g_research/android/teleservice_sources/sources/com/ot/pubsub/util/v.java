package com.ot.pubsub.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes.dex */
public class v {
    public static final long a = 604800000;
    public static final long b = 86400000;
    public static final long c = 43200000;
    public static final int d = 3600000;
    public static final int e = 60000;
    public static final int f = 1000;
    public static final String g = "yyyyMMdd";
    private static final String h = "TimeUtil";

    public static boolean a(long j) {
        long jF = f();
        return jF <= j && j < b + jF;
    }

    public static long a() {
        return System.currentTimeMillis();
    }

    public static boolean b(long j) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        long timeInMillis = calendar.getTimeInMillis();
        return timeInMillis <= j && j < b + timeInMillis;
    }

    public static long b() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        long timeInMillis = calendar.getTimeInMillis();
        j.a(h, "getTodayLocalBeginTime " + timeInMillis);
        return timeInMillis;
    }

    public static String a(Date date) {
        return new SimpleDateFormat(g).format(date);
    }

    public static Date a(String str) {
        Date date = new Date();
        try {
            return new SimpleDateFormat(g).parse(str);
        } catch (Exception e2) {
            j.b(h, "integerStringToDate exception:", e2);
            return date;
        }
    }

    public static long c() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j = (jCurrentTimeMillis - (jCurrentTimeMillis % b)) - b;
        if (j < 0) {
            return 0L;
        }
        return j;
    }

    public static long d() {
        return c() + 86399999;
    }

    public static String e() {
        return TimeZone.getDefault().getDisplayName(false, 0);
    }

    public static long f() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+0"));
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTimeInMillis();
    }

    public static long g() {
        return f() + 86399999;
    }

    public static boolean a(long j, long j2) {
        return Math.abs(System.currentTimeMillis() - j) >= j2;
    }

    public static int c(long j) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        return calendar.get(11);
    }

    public static long h() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTimeInMillis();
    }

    public static long i() {
        return f() + 86399999;
    }

    public static boolean j() {
        long jF = f();
        long j = b + jF;
        long j2 = jF + 85500000;
        long jCurrentTimeMillis = System.currentTimeMillis();
        return j2 < jCurrentTimeMillis && jCurrentTimeMillis < j;
    }

    public static boolean d(long j) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        Calendar calendar2 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar2.setTimeInMillis(j);
        return calendar2.get(1) == calendar.get(1) && calendar2.get(2) == calendar.get(2) && calendar2.get(5) == calendar.get(5);
    }
}
