package com.ot.pubsub.g;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.Calendar;

/* JADX INFO: loaded from: classes.dex */
class h implements Runnable {
    final /* synthetic */ f a;

    h(f fVar) {
        this.a = fVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        synchronized (this.a.c) {
            if (this.a.c == null) {
                return;
            }
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
                    cursorQuery = writableDatabase.query(f.a.b, new String[]{"timestamp"}, "timestamp < ? ", strArr, null, null, "timestamp ASC");
                    if (cursorQuery.getCount() != 0) {
                        com.ot.pubsub.util.j.a("MessageOTManager", "*** deleted obsolete item count=" + writableDatabase.delete(f.a.b, "timestamp < ? ", strArr));
                    }
                    long jE = f.a().e();
                    this.a.a(jE == 0);
                    com.ot.pubsub.util.j.a("MessageOTManager", "after delete obsolete record remains=" + jE);
                } catch (Exception e) {
                    com.ot.pubsub.util.j.d("MessageOTManager", "remove obsolete messages failed with " + e);
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
