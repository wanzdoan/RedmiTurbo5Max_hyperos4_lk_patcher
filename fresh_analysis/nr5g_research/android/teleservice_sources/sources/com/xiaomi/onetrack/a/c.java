package com.xiaomi.onetrack.a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteBlobTooBigException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.ot.pubsub.g.f;
import com.xiaomi.onetrack.b.o;
import com.xiaomi.onetrack.util.r;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes2.dex */
public class c {
    private static c h;
    private final a i = new a(com.xiaomi.onetrack.f.a.a());

    public static c a() {
        if (h == null) {
            a(com.xiaomi.onetrack.f.a.a());
        }
        return h;
    }

    public static void a(Context context) {
        if (h == null) {
            synchronized (c.class) {
                try {
                    if (h == null) {
                        h = new c();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    private c() {
        c();
    }

    public void a(com.xiaomi.onetrack.f.b bVar) {
        try {
            com.xiaomi.onetrack.a.a.a.a(new d(this, bVar));
        } catch (Throwable th) {
            r.a("AdMonitorManager", "filterAdMonitor Throwable：" + th.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long a(com.xiaomi.onetrack.a.b.a aVar) {
        synchronized (this.i) {
            try {
                if (!aVar.k()) {
                    r.c("AdMonitorManager", "addAdMonitorToDatabase event is inValid, event:" + aVar.a());
                    return -1L;
                }
                SQLiteDatabase writableDatabase = this.i.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(f.a.d, aVar.e());
                contentValues.put(f.a.e, aVar.f());
                contentValues.put(f.a.l, aVar.a());
                contentValues.put("timestamp", Long.valueOf(aVar.d()));
                contentValues.put("url", aVar.c());
                contentValues.put("ot_event_id", aVar.j());
                contentValues.put("url_count", Integer.valueOf(aVar.h()));
                contentValues.put("url_index", Integer.valueOf(aVar.i()));
                long jInsert = writableDatabase.insert("monitor", null, contentValues);
                r.a("AdMonitorManager", "DB-Thread: AdMonitorManager.addAdMonitorToDatabase , row=" + jInsert);
                if (r.a) {
                    r.a("AdMonitorManager", "添加后，ad monitor url 中事件个数为 " + e());
                }
                return jInsert;
            } catch (Throwable th) {
                r.a("AdMonitorManager", "addAdMonitorToDatabase Throwable：" + th.getMessage());
                return -1L;
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:56:0x0186  */
    /* JADX WARN: Code duplicated, block: B:61:0x0196 A[Catch: all -> 0x0169, PHI: r9 r21
  0x0196: PHI (r9v5 android.database.Cursor) = (r9v3 android.database.Cursor), (r9v6 android.database.Cursor) binds: [B:56:0x0186, B:60:0x0194] A[DONT_GENERATE, DONT_INLINE]
  0x0196: PHI (r21v5 com.xiaomi.onetrack.a.c.a) = (r21v3 com.xiaomi.onetrack.a.c.a), (r21v6 com.xiaomi.onetrack.a.c.a) binds: [B:56:0x0186, B:60:0x0194] A[DONT_GENERATE, DONT_INLINE], TRY_ENTER, TryCatch #3 {all -> 0x0169, blocks: (B:43:0x0164, B:44:0x0167, B:65:0x019d, B:66:0x01a0, B:61:0x0196, B:62:0x0199), top: B:71:0x0005 }] */
    public com.xiaomi.onetrack.a.c.a b() {
        Cursor cursor;
        com.xiaomi.onetrack.a.c.a aVar;
        Cursor cursorQuery;
        Cursor cursor2;
        int i;
        this = this;
        synchronized (this.i) {
            try {
                try {
                    try {
                        cursorQuery = this.i.getReadableDatabase().query("monitor", null, null, null, null, null, "_id ASC");
                        try {
                            try {
                                int columnIndex = cursorQuery.getColumnIndex("_id");
                                cursorQuery.getColumnIndex(f.a.d);
                                int columnIndex2 = cursorQuery.getColumnIndex(f.a.e);
                                int columnIndex3 = cursorQuery.getColumnIndex(f.a.l);
                                int columnIndex4 = cursorQuery.getColumnIndex("timestamp");
                                int columnIndex5 = cursorQuery.getColumnIndex("url");
                                int columnIndex6 = cursorQuery.getColumnIndex("send_count");
                                int columnIndex7 = cursorQuery.getColumnIndex("ot_event_id");
                                int columnIndex8 = cursorQuery.getColumnIndex("url_count");
                                int columnIndex9 = cursorQuery.getColumnIndex("url_index");
                                ArrayList arrayList = new ArrayList();
                                int i2 = columnIndex8;
                                int i3 = columnIndex9;
                                long jCurrentTimeMillis = System.currentTimeMillis();
                                int i4 = 0;
                                while (true) {
                                    if (!cursorQuery.moveToNext()) {
                                        arrayList = arrayList;
                                        cursor2 = cursorQuery;
                                        aVar = null;
                                        i = i4;
                                        break;
                                    }
                                    int i5 = i3;
                                    int i6 = cursorQuery.getInt(columnIndex6);
                                    ArrayList arrayList2 = arrayList;
                                    int i7 = i2;
                                    long j = cursorQuery.getLong(columnIndex4);
                                    columnIndex4 = columnIndex4;
                                    i3 = i5;
                                    aVar = null;
                                    columnIndex6 = columnIndex6;
                                    arrayList = arrayList2;
                                    try {
                                        if (this.a(jCurrentTimeMillis, j, i6)) {
                                            String string = cursorQuery.getString(columnIndex2);
                                            long j2 = jCurrentTimeMillis;
                                            String string2 = cursorQuery.getString(columnIndex3);
                                            String string3 = cursorQuery.getString(columnIndex7);
                                            int i8 = columnIndex2;
                                            int i9 = cursorQuery.getInt(i7);
                                            int i10 = cursorQuery.getInt(i3);
                                            int i11 = columnIndex3;
                                            int i12 = cursorQuery.getInt(columnIndex);
                                            int i13 = columnIndex;
                                            String string4 = cursorQuery.getString(columnIndex5);
                                            cursor2 = cursorQuery;
                                            try {
                                                com.xiaomi.onetrack.a.b.a aVar2 = new com.xiaomi.onetrack.a.b.a();
                                                aVar2.b(string4);
                                                aVar2.a(i12);
                                                aVar2.a(j);
                                                aVar2.b(i6);
                                                aVar2.a(string2);
                                                aVar2.d(string);
                                                aVar2.e(string3);
                                                aVar2.c(i9);
                                                aVar2.d(i10);
                                                arrayList.add(aVar2);
                                                i = i4 + 1;
                                                if (i >= 100) {
                                                    break;
                                                }
                                                i4 = i;
                                                columnIndex2 = i8;
                                                jCurrentTimeMillis = j2;
                                                i2 = i7;
                                                columnIndex3 = i11;
                                                columnIndex = i13;
                                                cursorQuery = cursor2;
                                            } catch (SQLiteBlobTooBigException e) {
                                                e = e;
                                                cursorQuery = cursor2;
                                                r.b("AdMonitorManager", "blob too big ***", e);
                                                f();
                                                if (cursorQuery != null) {
                                                    cursorQuery.close();
                                                }
                                                return aVar;
                                            } catch (Exception e2) {
                                                e = e2;
                                                cursorQuery = cursor2;
                                                r.a("AdMonitorManager", "", e);
                                                if (cursorQuery != null) {
                                                    cursorQuery.close();
                                                }
                                                return aVar;
                                            } catch (Throwable th) {
                                                th = th;
                                                cursor = cursor2;
                                                if (cursor != null) {
                                                    cursor.close();
                                                }
                                                throw th;
                                            }
                                        } else {
                                            i2 = i7;
                                        }
                                    } catch (SQLiteBlobTooBigException e3) {
                                        e = e3;
                                        r.b("AdMonitorManager", "blob too big ***", e);
                                        f();
                                        if (cursorQuery != null) {
                                            cursorQuery.close();
                                        }
                                        return aVar;
                                    } catch (Exception e4) {
                                        e = e4;
                                        r.a("AdMonitorManager", "", e);
                                        if (cursorQuery != null) {
                                            cursorQuery.close();
                                        }
                                        return aVar;
                                    }
                                }
                                boolean z = true;
                                if (arrayList.size() > 0) {
                                    r.a("AdMonitorManager", "get ad monitor size :" + arrayList.size());
                                    if (cursor2.isAfterLast()) {
                                        r.a("AdMonitorManager", "cursor isAfterLast");
                                    } else {
                                        z = false;
                                    }
                                }
                                com.xiaomi.onetrack.a.c.a aVar3 = new com.xiaomi.onetrack.a.c.a(i, arrayList, z);
                                cursor2.close();
                                return aVar3;
                            } catch (Throwable th2) {
                                th = th2;
                                cursor2 = cursorQuery;
                            }
                        } catch (SQLiteBlobTooBigException e5) {
                            e = e5;
                            aVar = null;
                        } catch (Exception e6) {
                            e = e6;
                            aVar = null;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        cursor = cursorQuery;
                    }
                } catch (SQLiteBlobTooBigException e7) {
                    e = e7;
                    aVar = null;
                    cursorQuery = null;
                } catch (Exception e8) {
                    e = e8;
                    aVar = null;
                    cursorQuery = null;
                } catch (Throwable th4) {
                    th = th4;
                    cursor = null;
                }
            } catch (Throwable th5) {
                throw th5;
            }
        }
    }

    public int a(ArrayList arrayList) {
        synchronized (this.i) {
            if (arrayList != null) {
                try {
                    if (arrayList.size() != 0) {
                        try {
                            SQLiteDatabase writableDatabase = this.i.getWritableDatabase();
                            StringBuilder sb = new StringBuilder(((Long.toString(((Integer) arrayList.get(0)).intValue()).length() + 1) * arrayList.size()) + 16);
                            sb.append("_id");
                            sb.append(" in (");
                            sb.append(arrayList.get(0));
                            int size = arrayList.size();
                            for (int i = 1; i < size; i++) {
                                sb.append(",");
                                sb.append(arrayList.get(i));
                            }
                            sb.append(")");
                            int iDelete = writableDatabase.delete("monitor", sb.toString(), null);
                            r.a("AdMonitorManager", "*** *** deleted ad monitor count " + iDelete);
                            if (r.a) {
                                r.a("AdMonitorManager", "after delete ad monitor record remains=" + e());
                            }
                            return iDelete;
                        } catch (Exception e) {
                            r.b("AdMonitorManager", "e=" + e);
                            return 0;
                        }
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            return 0;
        }
    }

    public void b(ArrayList arrayList) {
        String str;
        StringBuilder sb;
        synchronized (this.i) {
            try {
                if (arrayList != null) {
                    try {
                        if (arrayList.size() > 0) {
                            SQLiteDatabase writableDatabase = null;
                            try {
                                try {
                                    writableDatabase = this.i.getWritableDatabase();
                                    writableDatabase.beginTransaction();
                                    int size = arrayList.size();
                                    int i = 0;
                                    while (i < size) {
                                        Object obj = arrayList.get(i);
                                        i++;
                                        Integer num = (Integer) obj;
                                        num.intValue();
                                        writableDatabase.execSQL(String.format("update %s set %s = %s + 1 where %s = %s", "monitor", "send_count", "send_count", "_id", num));
                                    }
                                    writableDatabase.setTransactionSuccessful();
                                    try {
                                        writableDatabase.endTransaction();
                                        writableDatabase.close();
                                    } catch (Exception e) {
                                        str = "AdMonitorManager";
                                        sb = new StringBuilder();
                                        sb.append("addAdMonitorsRetryCount endTransaction error: ");
                                        sb.append(e.getMessage());
                                        r.b(str, sb.toString());
                                    }
                                } catch (Exception e2) {
                                    r.b("AdMonitorManager", "addAdMonitorsRetryCount Exception: " + e2.getMessage());
                                    if (writableDatabase != null) {
                                        try {
                                            writableDatabase.endTransaction();
                                            writableDatabase.close();
                                        } catch (Exception e3) {
                                            str = "AdMonitorManager";
                                            sb = new StringBuilder();
                                            sb.append("addAdMonitorsRetryCount endTransaction error: ");
                                            sb.append(e3.getMessage());
                                            r.b(str, sb.toString());
                                        }
                                    }
                                }
                            } catch (Throwable th) {
                                if (writableDatabase != null) {
                                    try {
                                        writableDatabase.endTransaction();
                                        writableDatabase.close();
                                    } catch (Exception e4) {
                                        r.b("AdMonitorManager", "addAdMonitorsRetryCount endTransaction error: " + e4.getMessage());
                                    }
                                }
                                throw th;
                            }
                        }
                    } catch (Throwable th2) {
                        r.a("AdMonitorManager", "addAdMonitorsRetryCount Throwable:" + th2.getMessage());
                    }
                }
            } catch (Throwable th3) {
                throw th3;
            }
        }
    }

    public void c() {
        com.xiaomi.onetrack.a.a.a.a(new e(this), 1000L);
    }

    public void d() {
        synchronized (this.i) {
            Cursor cursorQuery = null;
            try {
                try {
                    SQLiteDatabase writableDatabase = this.i.getWritableDatabase();
                    String[] strArr = {Long.toString(4L)};
                    cursorQuery = writableDatabase.query("monitor", new String[]{"timestamp"}, "send_count >= ? ", strArr, null, null, "_id ASC");
                    if (cursorQuery.getCount() != 0) {
                        r.a("AdMonitorManager", "*** deleted obsolete ad monitor count=" + writableDatabase.delete("monitor", "send_count >= ? ", strArr));
                    }
                    if (r.a) {
                        r.a("AdMonitorManager", "after delete obsolete ad monitor record remains=" + e());
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

    public long e() {
        try {
            return DatabaseUtils.queryNumEntries(this.i.getReadableDatabase(), "monitor");
        } catch (Exception e) {
            r.b("AdMonitorManager", "getTotalEventsNumberSync failed with " + e.getMessage());
            return 0L;
        }
    }

    private void f() {
        try {
            this.i.getWritableDatabase().delete("monitor", null, null);
            r.a("AdMonitorManager", "delete table monitor");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean a(long j, long j2, int i) {
        if (i <= 0) {
            return true;
        }
        if (i < 4) {
            if (Math.abs(j - j2) >= o.a(i - 1)) {
                return true;
            }
        }
        return false;
    }

    class a extends SQLiteOpenHelper {
        public a(Context context) {
            super(context, "onetrack_ad", (SQLiteDatabase.CursorFactory) null, 2);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            a(sQLiteDatabase);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            if (i2 == 2 && i == 1) {
                c(sQLiteDatabase);
            }
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            b(sQLiteDatabase);
            a(sQLiteDatabase);
        }

        private void a(SQLiteDatabase sQLiteDatabase) {
            try {
                sQLiteDatabase.execSQL("CREATE TABLE monitor (_id INTEGER PRIMARY KEY AUTOINCREMENT,appid TEXT,package TEXT,event_name TEXT,url TEXT,ot_event_id TEXT,send_count INTEGER DEFAULT 0,url_count INTEGER DEFAULT 0,url_index INTEGER DEFAULT 0,timestamp INTEGER)");
            } catch (Throwable th) {
                r.b("AdMonitorManager", "createTable error:" + th.getMessage());
                th.printStackTrace();
            }
        }

        private void b(SQLiteDatabase sQLiteDatabase) {
            try {
                sQLiteDatabase.execSQL(String.format("drop table if exists %s", "monitor"));
            } catch (Throwable th) {
                r.b("AdMonitorManager", "dropTable error:" + th.getMessage());
                th.printStackTrace();
            }
        }

        private void c(SQLiteDatabase sQLiteDatabase) {
            try {
                sQLiteDatabase.execSQL("ALTER TABLE monitor ADD COLUMN ot_event_id TEXT;");
                sQLiteDatabase.execSQL("ALTER TABLE monitor ADD COLUMN url_count INTEGER DEFAULT 0;");
                sQLiteDatabase.execSQL("ALTER TABLE monitor ADD COLUMN url_index INTEGER DEFAULT 0;");
            } catch (Throwable th) {
                r.b("AdMonitorManager", "adMTableAddColumn error:" + th.getMessage());
                th.printStackTrace();
            }
        }
    }
}
