package com.ot.pubsub.g;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.Calendar;

/* JADX INFO: loaded from: classes.dex */
class e implements Runnable {
    final /* synthetic */ c a;

    e(c cVar) {
        this.a = cVar;
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
                    cursorQuery = writableDatabase.query(c.a.b, new String[]{"timestamp"}, "timestamp < ? ", strArr, null, null, "timestamp ASC");
                    if (cursorQuery.getCount() != 0) {
                        com.ot.pubsub.util.j.a("MessageManager", "*** deleted obsolete item count=" + writableDatabase.delete(c.a.b, "timestamp < ? ", strArr));
                    }
                    long jF = c.a().f();
                    this.a.a(jF == 0);
                    com.ot.pubsub.util.j.a("MessageManager", "after delete obsolete record remains=" + jF);
                } catch (Exception e) {
                    com.ot.pubsub.util.j.d("MessageManager", "remove obsolete messages failed with " + e);
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
