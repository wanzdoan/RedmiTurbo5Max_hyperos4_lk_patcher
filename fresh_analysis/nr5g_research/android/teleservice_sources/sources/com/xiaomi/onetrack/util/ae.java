package com.xiaomi.onetrack.util;

import com.ot.pubsub.util.v;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ae {
    public static long a() {
        return System.currentTimeMillis();
    }

    public static boolean a(long j, long j2) {
        return Math.abs(System.currentTimeMillis() - j) >= j2;
    }

    public static boolean a(long j) {
        r.a("TimeUtil", "inTodayClientTime,current ts :" + System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        long timeInMillis = calendar.getTimeInMillis();
        long j2 = timeInMillis + v.b;
        r.a("TimeUtil", "[start]:" + timeInMillis + "\n[end]:" + j2 + "duration" + ((j2 - timeInMillis) - v.b));
        StringBuilder sb = new StringBuilder("is in today:");
        sb.append(timeInMillis <= j && j < j2);
        r.a("TimeUtil", sb.toString());
        return timeInMillis <= j && j < j2;
    }

    public static long b() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTimeInMillis();
    }

    public static boolean b(long j) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        Calendar calendar2 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar2.setTimeInMillis(j);
        return calendar2.get(1) == calendar.get(1) && calendar2.get(2) == calendar.get(2) && calendar2.get(5) == calendar.get(5);
    }

    public static int a(int i) {
        Calendar calendar;
        try {
            calendar = Calendar.getInstance(TimeZone.getTimeZone(s.c()));
        } catch (Exception unused) {
            calendar = Calendar.getInstance();
        }
        calendar.set(6, calendar.get(6) - i);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        try {
            return Integer.parseInt(new SimpleDateFormat(v.g).format(calendar.getTime()));
        } catch (Exception e) {
            r.b("TimeUtil", "getDate error: " + e.getMessage());
            return -1;
        }
    }

    public static long a(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        try {
            return simpleDateFormat.parse(str).getTime() + v.b;
        } catch (ParseException e) {
            r.c("TimeUtil", "getMidnightTime error: " + e.getMessage());
            return -1L;
        }
    }

    public static int c() {
        try {
            return Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai")).get(5);
        } catch (Exception e) {
            r.c("TimeUtil", "getDayOfMonth error: " + e.getMessage());
            return -1;
        }
    }

    public static int d() {
        try {
            int i = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai")).get(7);
            if (i == 1) {
                return 7;
            }
            return i - 1;
        } catch (Exception e) {
            r.c("TimeUtil", "getDayOfWeek error: " + e.getMessage());
            return -1;
        }
    }
}
