package com.xiaomi.onetrack.h.b;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;
import com.ot.pubsub.b.g;
import com.xiaomi.onetrack.util.ae;
import com.xiaomi.onetrack.util.o;
import com.xiaomi.onetrack.util.r;
import java.util.HashMap;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes2.dex */
public class b {
    private static b c;
    private a d = new a(com.xiaomi.onetrack.f.a.a());

    private b() {
        c();
    }

    public static b a() {
        if (c == null) {
            synchronized (b.class) {
                try {
                    if (c == null) {
                        c = new b();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return c;
    }

    public void a(String str, int i, String str2, String str3, int i2) {
        try {
            synchronized (this.d) {
                try {
                    SQLiteDatabase writableDatabase = this.d.getWritableDatabase();
                    int iA = a(writableDatabase, str, i, str2, str3);
                    r.a("OTMonitorDBManager", "addAppStatsRecord countInDB: " + iA);
                    if (iA > 0) {
                        writableDatabase.execSQL("UPDATE app_stats SET count = count + ? WHERE app_id = ? AND date = ? AND stage = ? AND reason = ?", new Object[]{Integer.valueOf(i2), str, Integer.valueOf(i), str2, str3});
                    } else {
                        writableDatabase.execSQL("INSERT INTO app_stats (app_id, date, stage, reason, count) VALUES (?, ?, ?, ?, ?)", new Object[]{str, Integer.valueOf(i), str2, str3, Integer.valueOf(i2)});
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        } catch (Exception e) {
            r.b("OTMonitorDBManager", "addAppStatsRecord error: " + e.getMessage());
        }
    }

    public void a(String str, int i, String str2, int i2) {
        try {
            synchronized (this.d) {
                try {
                    SQLiteDatabase writableDatabase = this.d.getWritableDatabase();
                    int iA = a(writableDatabase, str, i, str2);
                    r.a("OTMonitorDBManager", "addEventStatsRecord countInDB: " + iA);
                    if (iA > 0) {
                        writableDatabase.execSQL("UPDATE event_stats SET count = count + ? WHERE app_id = ? AND date = ? AND event = ?", new Object[]{Integer.valueOf(i2), str, Integer.valueOf(i), str2});
                    } else {
                        writableDatabase.execSQL("INSERT INTO event_stats (app_id, date, event, count) VALUES (?, ?, ?, ?)", new Object[]{str, Integer.valueOf(i), str2, Integer.valueOf(i2)});
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        } catch (Exception e) {
            r.b("OTMonitorDBManager", "addEventStatsRecord error: " + e.getMessage());
        }
    }

    private int a(SQLiteDatabase sQLiteDatabase, String str, int i, String str2, String str3) {
        Cursor cursorQuery = null;
        int i2 = 0;
        try {
            cursorQuery = sQLiteDatabase.query("app_stats", new String[]{"count"}, "app_id = ? AND date = ? AND stage = ? AND reason = ?", new String[]{str, String.valueOf(i), str2, str3}, null, null, null);
            if (cursorQuery != null && cursorQuery.moveToFirst()) {
                i2 = cursorQuery.getInt(cursorQuery.getColumnIndexOrThrow("count"));
            }
            return i2;
        } catch (Exception e) {
            r.b("OTMonitorDBManager", "getAppStatsRecordCount error: " + e.getMessage());
            return 0;
        } finally {
            o.b(cursorQuery);
        }
    }

    private int a(SQLiteDatabase sQLiteDatabase, String str, int i, String str2) {
        Cursor cursorQuery = null;
        int i2 = 0;
        try {
            cursorQuery = sQLiteDatabase.query("event_stats", new String[]{"count"}, "app_id = ? AND date = ? AND event = ?", new String[]{str, String.valueOf(i), str2}, null, null, null);
            if (cursorQuery != null && cursorQuery.moveToFirst()) {
                i2 = cursorQuery.getInt(cursorQuery.getColumnIndexOrThrow("count"));
            }
            return i2;
        } catch (Exception e) {
            r.b("OTMonitorDBManager", "getEventStatsRecordCount error: " + e.getMessage());
            return 0;
        } finally {
            o.b(cursorQuery);
        }
    }

    public com.xiaomi.onetrack.h.c.a b() {
        try {
            FutureTask futureTask = new FutureTask(new c(this));
            com.xiaomi.onetrack.h.d.b.a(futureTask);
            com.xiaomi.onetrack.h.c.a aVar = (com.xiaomi.onetrack.h.c.a) futureTask.get(10L, TimeUnit.SECONDS);
            if (aVar != null) {
                return aVar;
            }
            return null;
        } catch (Exception e) {
            r.b("OTMonitorDBManager", "getRequestFailedInfo futureTask error: " + e.getMessage());
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Pair a(SQLiteDatabase sQLiteDatabase, Cursor cursor) {
        Pair pairB = b(sQLiteDatabase, cursor);
        return pairB == null ? c(sQLiteDatabase, cursor) : pairB;
    }

    private Pair b(SQLiteDatabase sQLiteDatabase, Cursor cursor) {
        try {
            Cursor cursorQuery = sQLiteDatabase.query("app_stats", new String[]{g.d, "date"}, "date < ? ", new String[]{String.valueOf(ae.a(0))}, null, null, "date DESC", "1");
            if (cursorQuery == null || !cursorQuery.moveToNext()) {
                return null;
            }
            return new Pair(Integer.valueOf(cursorQuery.getInt(cursorQuery.getColumnIndexOrThrow("date"))), cursorQuery.getString(cursorQuery.getColumnIndexOrThrow(g.d)));
        } catch (Exception e) {
            r.b("OTMonitorDBManager", "getKeyFromAppStats error: " + e.getMessage());
            return null;
        }
    }

    private Pair c(SQLiteDatabase sQLiteDatabase, Cursor cursor) {
        try {
            Cursor cursorQuery = sQLiteDatabase.query("event_stats", new String[]{g.d, "date"}, "date < ? ", new String[]{String.valueOf(ae.a(0))}, null, null, "date DESC", "1");
            if (cursorQuery == null || !cursorQuery.moveToNext()) {
                return null;
            }
            return new Pair(Integer.valueOf(cursorQuery.getInt(cursorQuery.getColumnIndexOrThrow("date"))), cursorQuery.getString(cursorQuery.getColumnIndexOrThrow(g.d)));
        } catch (Exception e) {
            r.b("OTMonitorDBManager", "getKeyFromEventStats error: " + e.getMessage());
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public HashMap a(SQLiteDatabase sQLiteDatabase, Cursor cursor, int i, String str) {
        HashMap map = new HashMap();
        try {
            Cursor cursorQuery = sQLiteDatabase.query("app_stats", new String[]{"stage", "reason", "count"}, "app_id = ? AND date = ?", new String[]{str, String.valueOf(i)}, "stage, reason", null, null);
            while (cursorQuery.moveToNext()) {
                String string = cursorQuery.getString(cursorQuery.getColumnIndexOrThrow("stage"));
                String string2 = cursorQuery.getString(cursorQuery.getColumnIndexOrThrow("reason"));
                int i2 = cursorQuery.getInt(cursorQuery.getColumnIndexOrThrow("count"));
                if (map.containsKey(string)) {
                    ((HashMap) map.get(string)).put(string2, Integer.valueOf(i2));
                } else {
                    HashMap map2 = new HashMap();
                    map2.put(string2, Integer.valueOf(i2));
                    map.put(string, map2);
                }
            }
            return map;
        } catch (Exception e) {
            r.b("OTMonitorDBManager", "getAppStats error: " + e.getMessage());
            return map;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public HashMap b(SQLiteDatabase sQLiteDatabase, Cursor cursor, int i, String str) {
        HashMap map = new HashMap();
        try {
            Cursor cursorQuery = sQLiteDatabase.query("event_stats", new String[]{com.ot.pubsub.b.a.b, "count"}, "app_id = ? AND date = ?", new String[]{str, String.valueOf(i)}, null, null, null);
            while (cursorQuery.moveToNext()) {
                map.put(cursorQuery.getString(cursorQuery.getColumnIndexOrThrow(com.ot.pubsub.b.a.b)), Integer.valueOf(cursorQuery.getInt(cursorQuery.getColumnIndexOrThrow("count"))));
            }
            return map;
        } catch (Exception e) {
            r.b("OTMonitorDBManager", "getEventStats error: " + e.getMessage());
            return map;
        }
    }

    public void a(int i, String str) {
        try {
            synchronized (this.d) {
                SQLiteDatabase writableDatabase = this.d.getWritableDatabase();
                writableDatabase.delete("app_stats", "date = ? AND app_id = ?", new String[]{String.valueOf(i), str});
                writableDatabase.delete("event_stats", "date = ? AND app_id = ?", new String[]{String.valueOf(i), str});
            }
        } catch (Exception e) {
            r.b("OTMonitorDBManager", "deleteOTMonitorInfo error: " + e.getMessage());
        }
    }

    public void c() {
        com.xiaomi.onetrack.h.d.b.a(new d(this));
    }
}
