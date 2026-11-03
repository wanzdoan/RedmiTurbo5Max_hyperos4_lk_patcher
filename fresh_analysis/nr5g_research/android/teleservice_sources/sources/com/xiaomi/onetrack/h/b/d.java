package com.xiaomi.onetrack.h.b;

import android.database.sqlite.SQLiteDatabase;
import com.xiaomi.onetrack.util.ae;
import com.xiaomi.onetrack.util.r;

/* JADX INFO: loaded from: classes2.dex */
class d implements Runnable {
    final /* synthetic */ b a;

    d(b bVar) {
        this.a = bVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            synchronized (this.a.d) {
                SQLiteDatabase writableDatabase = this.a.d.getWritableDatabase();
                writableDatabase.delete("app_stats", "date < ?", new String[]{String.valueOf(ae.a(7))});
                writableDatabase.delete("event_stats", "date < ?", new String[]{String.valueOf(ae.a(7))});
            }
        } catch (Exception e) {
            r.b("OTMonitorDBManager", "removeObsoleteInfo error: " + e.getMessage());
        }
    }
}
