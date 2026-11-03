package com.xiaomi.onetrack.c;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.Calendar;

/* JADX INFO: loaded from: classes2.dex */
class x implements Runnable {
    final /* synthetic */ s a;

    x(s sVar) {
        this.a = sVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.a.i == null) {
            return;
        }
        synchronized (this.a.i) {
            Cursor cursorQuery = null;
            try {
                try {
                    try {
                        SQLiteDatabase writableDatabase = this.a.i.getWritableDatabase();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.set(6, calendar.get(6) - 7);
                        calendar.set(11, 0);
                        calendar.set(12, 0);
                        calendar.set(13, 0);
                        String[] strArr = {Long.toString(calendar.getTimeInMillis())};
                        cursorQuery = writableDatabase.query(com.ot.pubsub.b.a.c, new String[]{"timestamp"}, "timestamp < ? ", strArr, null, null, "timestamp ASC");
                        if (cursorQuery.getCount() != 0) {
                            com.xiaomi.onetrack.util.r.a("SystemImpCacheManager", "*** deleted obsolete item count=" + writableDatabase.delete(com.ot.pubsub.b.a.c, "timestamp < ? ", strArr));
                        }
                        com.xiaomi.onetrack.util.r.a("SystemImpCacheManager", "after delete obsolete record remains=" + this.a.d());
                    } catch (Throwable th) {
                        if (cursorQuery != null) {
                            try {
                                cursorQuery.close();
                                throw th;
                            } catch (Exception unused) {
                                throw th;
                            }
                        }
                        throw th;
                    }
                } catch (Exception e) {
                    com.xiaomi.onetrack.util.r.d("SystemImpCacheManager", "remove obsolete messages failed with " + e);
                    if (cursorQuery != null) {
                    }
                }
                cursorQuery.close();
            } catch (Exception unused2) {
            }
        }
    }
}
