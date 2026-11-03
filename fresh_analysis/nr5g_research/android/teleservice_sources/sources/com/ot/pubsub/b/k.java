package com.ot.pubsub.b;

import android.database.Cursor;
import android.text.TextUtils;
import java.util.concurrent.Callable;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
class k implements Callable {
    final /* synthetic */ String a;
    final /* synthetic */ h b;

    k(h hVar, String str) {
        this.b = hVar;
        this.a = str;
    }

    /* JADX WARN: Code duplicated, block: B:36:0x0098 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // java.util.concurrent.Callable
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public l call() throws Exception {
        Cursor cursorQuery;
        Cursor cursor = null;
        try {
            cursorQuery = this.b.b.getWritableDatabase().query(g.b, null, "app_id=?", new String[]{this.a}, null, null, null);
            try {
                try {
                    int columnIndex = cursorQuery.getColumnIndex(g.d);
                    int columnIndex2 = cursorQuery.getColumnIndex(g.e);
                    int columnIndex3 = cursorQuery.getColumnIndex(g.f);
                    int columnIndex4 = cursorQuery.getColumnIndex("timestamp");
                    if (cursorQuery.moveToNext()) {
                        l lVar = new l();
                        lVar.a = cursorQuery.getString(columnIndex);
                        String string = cursorQuery.getString(columnIndex2);
                        if (!TextUtils.isEmpty(string)) {
                            lVar.e = new JSONObject(string);
                        }
                        lVar.b = h.b(lVar.e);
                        lVar.d = cursorQuery.getString(columnIndex3);
                        lVar.c = cursorQuery.getLong(columnIndex4);
                        try {
                            cursorQuery.close();
                            return lVar;
                        } catch (Exception unused) {
                            com.ot.pubsub.util.j.a("ConfigDbManager", "getConfig  cursor.close");
                            return lVar;
                        }
                    }
                } catch (Exception e) {
                    e = e;
                    com.ot.pubsub.util.j.a("ConfigDbManager", e.getMessage());
                    if (cursorQuery != null) {
                    }
                    return null;
                }
            } catch (Throwable th) {
                th = th;
                cursor = cursorQuery;
                if (cursor != null) {
                    try {
                        cursor.close();
                    } catch (Exception unused2) {
                        com.ot.pubsub.util.j.a("ConfigDbManager", "getConfig  cursor.close");
                    }
                }
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            cursorQuery = null;
        } catch (Throwable th2) {
            th = th2;
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
        try {
            cursorQuery.close();
        } catch (Exception unused3) {
            com.ot.pubsub.util.j.a("ConfigDbManager", "getConfig  cursor.close");
        }
        return null;
    }
}
