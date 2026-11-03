package com.xiaomi.onetrack.h.b;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;
import com.xiaomi.onetrack.util.o;
import com.xiaomi.onetrack.util.r;
import java.io.Closeable;
import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes2.dex */
class c implements Callable {
    final /* synthetic */ b a;

    c(b bVar) {
        this.a = bVar;
    }

    @Override // java.util.concurrent.Callable
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public com.xiaomi.onetrack.h.c.a call() {
        try {
            SQLiteDatabase readableDatabase = this.a.d.getReadableDatabase();
            Pair pairA = this.a.a(readableDatabase, (Cursor) null);
            if (pairA == null) {
                return null;
            }
            return new com.xiaomi.onetrack.h.c.a.C0021a().a(((Integer) pairA.first).intValue()).a((String) pairA.second).a(this.a.a(readableDatabase, (Cursor) null, ((Integer) pairA.first).intValue(), (String) pairA.second)).b(this.a.b(readableDatabase, null, ((Integer) pairA.first).intValue(), (String) pairA.second)).a();
        } catch (Exception e) {
            r.b("OTMonitorDBManager", "getMonitorInfo error: " + e.getMessage());
            return null;
        } finally {
            o.b((Closeable) null);
        }
    }
}
