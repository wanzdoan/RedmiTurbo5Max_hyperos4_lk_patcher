package com.xiaomi.onetrack.c;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.Calendar;

/* JADX INFO: loaded from: classes2.dex */
class p implements Runnable {
    final /* synthetic */ n a;

    p(n nVar) {
        this.a = nVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.a.c == null) {
            return;
        }
        synchronized (this.a.c) {
            Cursor cursorQuery = null;
            try {
                try {
                    SQLiteDatabase writableDatabase = this.a.c.getWritableDatabase();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(6, calendar.get(6) - 7);
                    calendar.set(11, 0);
                    calendar.set(12, 0);
                    calendar.set(13, 0);
                    String[] strArr = {Long.toString(calendar.getTimeInMillis())};
                    cursorQuery = writableDatabase.query("otps_events", new String[]{com.ot.pubsub.g.f.a.d, "count(*) as count"}, "timestamp < ? ", strArr, com.ot.pubsub.g.f.a.d, null, null);
                    if (cursorQuery.getCount() != 0) {
                        while (cursorQuery.moveToNext()) {
                            String string = cursorQuery.getString(cursorQuery.getColumnIndex(com.ot.pubsub.g.f.a.d));
                            int i = cursorQuery.getInt(cursorQuery.getColumnIndex("count"));
                            com.xiaomi.onetrack.util.r.a("PubSubEventManager", "removeObsoleteEvents: appId=" + string + ", count=" + i);
                            com.xiaomi.onetrack.h.d.b.a(string, "", "do", "", i);
                        }
                        com.xiaomi.onetrack.util.r.a("PubSubEventManager", "*** deleted obsolete item count=" + writableDatabase.delete("otps_events", "timestamp < ? ", strArr));
                    }
                    long jF = n.a().f();
                    this.a.a(jF == 0);
                    com.xiaomi.onetrack.util.r.a("PubSubEventManager", "after delete obsolete record remains=" + jF);
                } catch (Exception e) {
                    com.xiaomi.onetrack.util.r.d("PubSubEventManager", "remove obsolete messages failed with " + e);
                    if (cursorQuery != null) {
                    }
                }
                try {
                    cursorQuery.close();
                } catch (Exception unused) {
                }
            } catch (Throwable th) {
                if (cursorQuery != null) {
                    try {
                        cursorQuery.close();
                        throw th;
                    } catch (Exception unused2) {
                        throw th;
                    }
                }
                throw th;
            }
        }
    }
}
