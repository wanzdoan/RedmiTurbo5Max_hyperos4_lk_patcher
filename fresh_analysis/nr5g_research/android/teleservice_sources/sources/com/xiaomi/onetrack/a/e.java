package com.xiaomi.onetrack.a;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.xiaomi.onetrack.util.r;
import java.util.Calendar;

/* JADX INFO: loaded from: classes2.dex */
class e implements Runnable {
    final /* synthetic */ c a;

    e(c cVar) {
        this.a = cVar;
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
                    SQLiteDatabase writableDatabase = this.a.i.getWritableDatabase();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(6, calendar.get(6) - 7);
                    calendar.set(11, 0);
                    calendar.set(12, 0);
                    calendar.set(13, 0);
                    String[] strArr = {Long.toString(calendar.getTimeInMillis())};
                    cursorQuery = writableDatabase.query("monitor", new String[]{"timestamp"}, "timestamp < ? ", strArr, null, null, "timestamp ASC");
                    if (cursorQuery.getCount() != 0) {
                        r.a("AdMonitorManager", "*** deleted obsolete ad monitor count=" + writableDatabase.delete("monitor", "timestamp < ? ", strArr));
                    }
                    if (r.a) {
                        r.a("AdMonitorManager", "after delete obsolete ad monitor record remains=" + this.a.e());
                    }
                } catch (Exception e) {
                    r.d("AdMonitorManager", "remove obsolete ad monitor failed with " + e);
                    if (cursorQuery != null) {
                    }
                }
                cursorQuery.close();
            } catch (Throwable th) {
                if (cursorQuery != null) {
                    cursorQuery.close();
                    throw th;
                }
                throw th;
            }
        }
    }
}
