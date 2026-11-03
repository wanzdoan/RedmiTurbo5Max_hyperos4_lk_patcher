package com.ot.pubsub.g;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteBlobTooBigException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import com.ot.pubsub.util.v;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class f {
    private static final String a = "MessageOTManager";
    private static f b = null;
    private static final int d = 1000;
    private static final int e = 512000;
    private static final int f = 1024000;
    private static final int g = 7;
    private static final String h = "priority ASC, _id ASC";
    private static volatile boolean i = false;
    private static volatile int j = 1;
    private static volatile int k;
    private a c = new a(com.ot.pubsub.util.b.a());

    public static f a() {
        if (b == null) {
            b();
        }
        return b;
    }

    public static void b() {
        if (b == null) {
            synchronized (f.class) {
                try {
                    if (b == null) {
                        b = new f();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    private f() {
        c();
    }

    public synchronized void a(String str, String str2, com.ot.pubsub.h.a aVar, Map map) throws Throwable {
        try {
            try {
                com.ot.pubsub.g.a.a(new g(this, map, str, str2, aVar));
            } catch (Throwable th) {
                th = th;
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean b(String str, String str2, com.ot.pubsub.h.a aVar, Map map) {
        try {
            synchronized (this.c) {
                if (aVar != null) {
                    try {
                        if (aVar.i() && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
                            byte[] bArrA = a(aVar.f().toString());
                            if (bArrA.length > e) {
                                com.ot.pubsub.util.j.b(a, "Too large data, discard ***");
                                return false;
                            }
                            SQLiteDatabase writableDatabase = this.c.getWritableDatabase();
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(a.d, aVar.b());
                            contentValues.put(a.e, aVar.c());
                            contentValues.put(a.l, aVar.d());
                            contentValues.put("projectid", str);
                            contentValues.put("topic", str2);
                            contentValues.put("attribute", com.ot.pubsub.util.c.a((Object) map));
                            contentValues.put("gzipandencrypt", Integer.valueOf(j));
                            contentValues.put("timestamp", Long.valueOf(System.currentTimeMillis()));
                            contentValues.put("data", bArrA);
                            contentValues.put(a.m, Integer.valueOf(aVar.e()));
                            long jInsert = writableDatabase.insert(a.b, null, contentValues);
                            com.ot.pubsub.util.j.a(a, "DB-Thread: addOTMessageToDatabase , row=" + jInsert);
                            if (jInsert != -1) {
                                if (com.ot.pubsub.util.j.a) {
                                    com.ot.pubsub.util.j.a(a, "添加后，DB 中事件个数为 " + e());
                                }
                                a(false);
                            }
                            return jInsert != -1;
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
                com.ot.pubsub.util.j.c(a, "addEventToDatabase message is inValid. topic:" + str2 + ", data EventName:" + aVar.d());
                return false;
            }
        } catch (Exception e2) {
            com.ot.pubsub.util.j.b(a, "addEvent exception: ", e2);
            return false;
        }
    }

    /* JADX WARN: Code duplicated, block: B:107:0x011c A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:108:0x0122 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:48:0x011d  */
    /* JADX WARN: Code duplicated, block: B:52:0x013a A[LOOP:0: B:5:0x0069->B:52:0x013a, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:92:0x0198 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Not initialized variable reg: 5, insn: 0x00b4: MOVE (r3 I:??[OBJECT, ARRAY]) = (r5 I:??[OBJECT, ARRAY]), block:B:15:0x00b4 */
    /* JADX WARN: Type inference failed for: r26v0 */
    /* JADX WARN: Type inference failed for: r26v1, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r26v2 */
    /* JADX WARN: Type inference failed for: r27v0 */
    /* JADX WARN: Type inference failed for: r27v1, types: [java.util.Map] */
    /* JADX WARN: Type inference failed for: r27v2 */
    public com.ot.pubsub.j.a a(int i2) throws Throwable {
        com.ot.pubsub.j.a aVar;
        Cursor cursorQuery;
        Cursor cursor;
        boolean z;
        ?? A;
        Object objA;
        ?? r27;
        int length;
        Map map;
        Cursor cursor2 = null;
        aVar = null;
        aVar = null;
        com.ot.pubsub.j.a aVar2 = null;
        try {
            try {
                try {
                    cursorQuery = this.c.getReadableDatabase().query(a.b, null, null, null, null, null, h);
                    try {
                        int columnIndex = cursorQuery.getColumnIndex("_id");
                        cursorQuery.getColumnIndex(a.d);
                        int columnIndex2 = cursorQuery.getColumnIndex("projectid");
                        int columnIndex3 = cursorQuery.getColumnIndex("topic");
                        int columnIndex4 = cursorQuery.getColumnIndex("data");
                        int columnIndex5 = cursorQuery.getColumnIndex("attribute");
                        int columnIndex6 = cursorQuery.getColumnIndex("gzipandencrypt");
                        int columnIndex7 = cursorQuery.getColumnIndex("timestamp");
                        int columnIndex8 = cursorQuery.getColumnIndex(a.m);
                        ArrayList arrayList = new ArrayList();
                        ArrayList arrayList2 = new ArrayList();
                        boolean z2 = true;
                        boolean z3 = true;
                        int i3 = 0;
                        int i4 = 0;
                        while (true) {
                            if (!cursorQuery.moveToNext()) {
                                aVar = aVar2;
                                break;
                            }
                            long j2 = cursorQuery.getLong(columnIndex);
                            String string = cursorQuery.getString(columnIndex2);
                            String string2 = cursorQuery.getString(columnIndex3);
                            byte[] blob = cursorQuery.getBlob(columnIndex5);
                            int i5 = cursorQuery.getInt(columnIndex6);
                            byte[] blob2 = cursorQuery.getBlob(columnIndex4);
                            long j3 = cursorQuery.getLong(columnIndex7);
                            try {
                                if (z3) {
                                    int i6 = cursorQuery.getInt(columnIndex8);
                                    if (i6 > i2) {
                                        com.ot.pubsub.util.j.a(a, "No records of priority[" + i2 + "], first record priority=" + i6);
                                        try {
                                            cursorQuery.close();
                                        } catch (Exception unused) {
                                        }
                                        return aVar2;
                                    }
                                    z = false;
                                    com.ot.pubsub.util.j.b(a, "blob too big ***", e);
                                    f();
                                    if (cursorQuery != null) {
                                        cursorQuery.close();
                                    }
                                    return aVar;
                                }
                                z = z3;
                                if (objA != null) {
                                    aVar = aVar2;
                                    try {
                                        if (objA instanceof Map) {
                                            map = (Map) objA;
                                        }
                                        r27 = map;
                                        arrayList.add(new com.ot.pubsub.d.c(j2, string, string2, A, r27, i5, j3));
                                        arrayList2.add(Long.valueOf(j2));
                                        i3++;
                                        length = i4 + A.length();
                                        if (arrayList.size() < 1000) {
                                            break;
                                        }
                                        if (length >= f) {
                                            com.ot.pubsub.util.j.a(a, "reached max len: " + length);
                                            break;
                                        }
                                        i4 = length;
                                        aVar2 = aVar;
                                        z3 = z;
                                    } catch (SQLiteBlobTooBigException e2) {
                                        e = e2;
                                        com.ot.pubsub.util.j.b(a, "blob too big ***", e);
                                        f();
                                        if (cursorQuery != null) {
                                        }
                                        return aVar;
                                    } catch (Exception e3) {
                                        e = e3;
                                        com.ot.pubsub.util.j.a(a, "", e);
                                        if (cursorQuery != null) {
                                        }
                                        return aVar;
                                    }
                                } else {
                                    aVar = aVar2;
                                }
                                arrayList.add(new com.ot.pubsub.d.c(j2, string, string2, A, r27, i5, j3));
                                arrayList2.add(Long.valueOf(j2));
                                i3++;
                            } catch (Exception e4) {
                                com.ot.pubsub.util.j.b(a, "*** error ***", e4);
                            }
                            A = blob2 != null ? a(blob2) : aVar2;
                            objA = com.ot.pubsub.util.c.a(blob);
                            if (objA == null) {
                                map = new HashMap();
                                r27 = map;
                            } else {
                                r27 = aVar;
                            }
                            length = i4 + A.length();
                            if (arrayList.size() < 1000) {
                                break;
                                break;
                            }
                            if (length >= f) {
                                com.ot.pubsub.util.j.a(a, "reached max len: " + length);
                                break;
                            }
                            i4 = length;
                            aVar2 = aVar;
                            z3 = z;
                        }
                        int i7 = i3;
                        if (arrayList2.size() > 0) {
                            if (cursorQuery.isAfterLast()) {
                                com.ot.pubsub.util.j.a(a, "cursor isAfterLast");
                            } else if (cursorQuery.getInt(columnIndex8) <= i2) {
                                z2 = false;
                            }
                            com.ot.pubsub.j.a aVar3 = new com.ot.pubsub.j.a(arrayList, i7, arrayList2, z2);
                            try {
                                cursorQuery.close();
                            } catch (Exception unused2) {
                            }
                            return aVar3;
                        }
                    } catch (SQLiteBlobTooBigException e5) {
                        e = e5;
                        aVar = aVar2;
                    } catch (Exception e6) {
                        e = e6;
                        aVar = aVar2;
                    }
                } catch (Throwable th) {
                    th = th;
                    cursor2 = cursor;
                    if (cursor2 != null) {
                        try {
                            cursor2.close();
                        } catch (Exception unused3) {
                        }
                    }
                    throw th;
                }
            } catch (SQLiteBlobTooBigException e7) {
                e = e7;
                aVar = null;
                cursorQuery = null;
            } catch (Exception e8) {
                e = e8;
                aVar = null;
                cursorQuery = null;
            } catch (Throwable th2) {
                th = th2;
                if (cursor2 != null) {
                    cursor2.close();
                }
                throw th;
            }
            cursorQuery.close();
        } catch (Exception unused4) {
        }
        return aVar;
    }

    private void f() {
        try {
            this.c.getWritableDatabase().delete(a.b, null, null);
            a(true);
            com.ot.pubsub.util.j.a(a, "delete table othbpubsub");
        } catch (Exception e2) {
            com.ot.pubsub.util.j.b(a, "delete table error: " + e2.getMessage());
        }
    }

    public int a(List list) {
        synchronized (this.c) {
            if (list != null) {
                try {
                    if (list.size() != 0) {
                        try {
                            SQLiteDatabase writableDatabase = this.c.getWritableDatabase();
                            boolean z = true;
                            StringBuilder sb = new StringBuilder(((Long.toString(((com.ot.pubsub.d.c) list.get(0)).b()).length() + 1) * list.size()) + 16);
                            sb.append("_id");
                            sb.append(" in (");
                            sb.append(((com.ot.pubsub.d.c) list.get(0)).b());
                            int size = list.size();
                            for (int i2 = 1; i2 < size; i2++) {
                                sb.append(",");
                                sb.append(((com.ot.pubsub.d.c) list.get(i2)).b());
                            }
                            sb.append(")");
                            int iDelete = writableDatabase.delete(a.b, sb.toString(), null);
                            com.ot.pubsub.util.j.a(a, "*** *** deleted events count " + iDelete);
                            long jE = a().e();
                            if (jE != 0) {
                                z = false;
                            }
                            a(z);
                            com.ot.pubsub.util.j.a(a, "after delete DB record remains=" + jE);
                            return iDelete;
                        } catch (Exception e2) {
                            com.ot.pubsub.util.j.b(a, "e=" + e2);
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

    public int b(List list) {
        synchronized (this.c) {
            if (list != null) {
                try {
                    if (list.size() != 0) {
                        try {
                            SQLiteDatabase writableDatabase = this.c.getWritableDatabase();
                            boolean z = true;
                            StringBuilder sb = new StringBuilder(((Long.toString(((com.ot.pubsub.d.c) list.get(0)).b()).length() + 1) * list.size()) + 16);
                            sb.append("_id");
                            sb.append(" in (");
                            int size = list.size();
                            boolean z2 = false;
                            for (int i2 = 0; i2 < size; i2++) {
                                if (v.a(((com.ot.pubsub.d.c) list.get(i2)).g(), v.b)) {
                                    if (i2 == size - 1) {
                                        sb.append(((com.ot.pubsub.d.c) list.get(i2)).b());
                                    } else {
                                        sb.append(((com.ot.pubsub.d.c) list.get(i2)).b());
                                        sb.append(",");
                                    }
                                    z2 = true;
                                }
                            }
                            sb.append(")");
                            if (!z2) {
                                return 0;
                            }
                            com.ot.pubsub.util.j.a(a, "*** *** deleted events sb id " + sb.toString());
                            int iDelete = writableDatabase.delete(a.b, sb.toString(), null);
                            com.ot.pubsub.util.j.a(a, "*** *** deleted events count " + iDelete);
                            long jE = a().e();
                            if (jE != 0) {
                                z = false;
                            }
                            a(z);
                            com.ot.pubsub.util.j.a(a, "after delete DB record remains=" + jE);
                            return iDelete;
                        } catch (Exception e2) {
                            com.ot.pubsub.util.j.b(a, "e=" + e2);
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

    public void c() {
        com.ot.pubsub.g.a.a(new h(this));
    }

    public synchronized void a(boolean z) {
        i = z;
    }

    public synchronized boolean d() {
        return i;
    }

    public static byte[] a(String str) {
        return com.ot.pubsub.c.a.a(str.getBytes(), com.ot.pubsub.c.d.a(com.ot.pubsub.c.c.a(), true).getBytes());
    }

    public static String a(byte[] bArr) {
        return new String(com.ot.pubsub.c.a.b(bArr, com.ot.pubsub.c.d.a(com.ot.pubsub.c.c.a(), true).getBytes()));
    }

    public long e() {
        return DatabaseUtils.queryNumEntries(this.c.getReadableDatabase(), a.b);
    }

    class a extends SQLiteOpenHelper {
        public static final String a = "othbpubsub.db";
        public static final String b = "othbpubsub";
        public static final String c = "_id";
        public static final String d = "appid";
        public static final String e = "package";
        public static final String f = "projectid";
        public static final String g = "topic";
        public static final String h = "data";
        public static final String i = "attribute";
        public static final String j = "gzipandencrypt";
        public static final String k = "timestamp";
        public static final String l = "event_name";
        public static final String m = "priority";
        private static final int n = 1;
        private static final String o = "CREATE TABLE othbpubsub (_id INTEGER PRIMARY KEY AUTOINCREMENT,appid TEXT,package TEXT,event_name TEXT,projectid TEXT,topic TEXT,data BLOB,attribute BLOB,gzipandencrypt INTEGER,priority INTEGER,timestamp INTEGER)";

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i2, int i3) {
        }

        public a(Context context) {
            super(context, a, (SQLiteDatabase.CursorFactory) null, 1);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL(o);
        }
    }
}
