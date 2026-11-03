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
public class c {
    private static final String a = "MessageManager";
    private static c b = null;
    private static final int d = 1000;
    private static final int e = 512000;
    private static final int f = 1024000;
    private static final int g = 7;
    private static final String h = "timestamp";
    private static volatile boolean i = false;
    private static volatile int j = 1;
    private static volatile int k;
    private a c = new a(com.ot.pubsub.util.b.a());

    public static c a() {
        if (b == null) {
            b();
        }
        return b;
    }

    public static void b() {
        if (b == null) {
            synchronized (c.class) {
                try {
                    if (b == null) {
                        b = new c();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    private c() {
        d();
    }

    public synchronized void a(String str, String str2, String str3, Map map, boolean z) throws Throwable {
        try {
            try {
                com.ot.pubsub.g.a.a(new d(this, map, str, str2, str3, z));
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
    public boolean b(String str, String str2, String str3, Map map, boolean z) {
        try {
            synchronized (this.c) {
                try {
                    if (TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str3)) {
                        com.ot.pubsub.util.j.c(a, "addEventToDatabase message is inValid. topic:" + str2 + ", data:" + str3);
                        return false;
                    }
                    if (!z) {
                        str3 = com.ot.pubsub.c.c.b(str3);
                    }
                    byte[] bArrA = a(str3);
                    if (bArrA.length > e) {
                        com.ot.pubsub.util.j.b(a, "Too large data, discard ***");
                        return false;
                    }
                    int i2 = k;
                    if (z) {
                        i2 = j;
                    }
                    SQLiteDatabase writableDatabase = this.c.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("projectid", str);
                    contentValues.put("topic", str2);
                    contentValues.put("attribute", com.ot.pubsub.util.c.a((Object) map));
                    contentValues.put("gzipandencrypt", Integer.valueOf(i2));
                    contentValues.put("timestamp", Long.valueOf(System.currentTimeMillis()));
                    contentValues.put("data", bArrA);
                    long jInsert = writableDatabase.insert(a.b, null, contentValues);
                    com.ot.pubsub.util.j.a(a, "DB-Thread: EventManager.addEventToDatabase , row=" + jInsert);
                    if (jInsert != -1) {
                        if (com.ot.pubsub.util.j.a) {
                            com.ot.pubsub.util.j.a(a, "添加后，DB 中事件个数为 " + f());
                        }
                        a(false);
                    }
                    return jInsert != -1;
                } catch (Throwable th) {
                    throw th;
                }
            }
        } catch (Exception e2) {
            com.ot.pubsub.util.j.b(a, "EventManager.addEvent exception: ", e2);
            return false;
        }
    }

    /* JADX WARN: Code duplicated, block: B:40:0x00da  */
    /* JADX WARN: Code duplicated, block: B:43:0x00f5 A[LOOP:0: B:5:0x0053->B:43:0x00f5, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:75:0x0144 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:90:0x00d9 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:91:0x00df A[SYNTHETIC] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Not initialized variable reg: 4, insn: 0x007f: MOVE (r2 I:??[OBJECT, ARRAY]) = (r4 I:??[OBJECT, ARRAY]), block:B:11:0x007f */
    /* JADX WARN: Type inference failed for: r23v0 */
    /* JADX WARN: Type inference failed for: r23v1, types: [java.util.Map] */
    /* JADX WARN: Type inference failed for: r23v2 */
    public com.ot.pubsub.j.a c() throws Throwable {
        Cursor cursor;
        com.ot.pubsub.j.a aVar;
        Cursor cursorQuery;
        Cursor cursor2;
        boolean z;
        ?? r23;
        Map map;
        int length;
        try {
            try {
                cursorQuery = this.c.getReadableDatabase().query(a.b, null, null, null, null, null, "timestamp");
                try {
                    int columnIndex = cursorQuery.getColumnIndex("_id");
                    int columnIndex2 = cursorQuery.getColumnIndex("projectid");
                    int columnIndex3 = cursorQuery.getColumnIndex("topic");
                    int columnIndex4 = cursorQuery.getColumnIndex("data");
                    int columnIndex5 = cursorQuery.getColumnIndex("attribute");
                    int columnIndex6 = cursorQuery.getColumnIndex("gzipandencrypt");
                    int columnIndex7 = cursorQuery.getColumnIndex("timestamp");
                    ArrayList arrayList = new ArrayList();
                    ArrayList arrayList2 = new ArrayList();
                    int i2 = 0;
                    int i3 = 0;
                    while (true) {
                        if (!cursorQuery.moveToNext()) {
                            aVar = null;
                            break;
                        }
                        long j2 = cursorQuery.getLong(columnIndex);
                        String string = cursorQuery.getString(columnIndex2);
                        String string2 = cursorQuery.getString(columnIndex3);
                        byte[] blob = cursorQuery.getBlob(columnIndex5);
                        int i4 = cursorQuery.getInt(columnIndex6);
                        byte[] blob2 = cursorQuery.getBlob(columnIndex4);
                        long j3 = cursorQuery.getLong(columnIndex7);
                        String strA = blob2 != null ? a(blob2) : null;
                        Object objA = com.ot.pubsub.util.c.a(blob);
                        try {
                            if (objA != null) {
                                aVar = null;
                                try {
                                    if (objA instanceof Map) {
                                        map = (Map) objA;
                                    }
                                    r23 = map;
                                    arrayList.add(new com.ot.pubsub.d.c(j2, string, string2, strA, r23, i4, j3));
                                    arrayList2.add(Long.valueOf(j2));
                                    i2++;
                                    length = i3 + strA.length();
                                    if (arrayList.size() < 1000) {
                                        break;
                                    }
                                    if (length >= f) {
                                        com.ot.pubsub.util.j.a(a, "reached max len: " + length);
                                        break;
                                    }
                                    i3 = length;
                                } catch (SQLiteBlobTooBigException e2) {
                                    e = e2;
                                    com.ot.pubsub.util.j.b(a, "blob too big ***", e);
                                    g();
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
                                aVar = null;
                            }
                            arrayList.add(new com.ot.pubsub.d.c(j2, string, string2, strA, r23, i4, j3));
                            arrayList2.add(Long.valueOf(j2));
                            i2++;
                        } catch (Exception e4) {
                            com.ot.pubsub.util.j.b(a, "*** error ***", e4);
                        }
                        if (objA == null) {
                            map = new HashMap();
                            r23 = map;
                        } else {
                            r23 = aVar;
                        }
                        length = i3 + strA.length();
                        if (arrayList.size() < 1000) {
                            break;
                            break;
                        }
                        if (length >= f) {
                            com.ot.pubsub.util.j.a(a, "reached max len: " + length);
                            break;
                        }
                        i3 = length;
                    }
                    if (arrayList2.size() > 0) {
                        if (cursorQuery.isAfterLast()) {
                            com.ot.pubsub.util.j.a(a, "cursor isAfterLast");
                            z = true;
                        } else {
                            z = false;
                        }
                        com.ot.pubsub.j.a aVar2 = new com.ot.pubsub.j.a(arrayList, i2, arrayList2, z);
                        try {
                            cursorQuery.close();
                        } catch (Exception unused) {
                        }
                        return aVar2;
                    }
                } catch (SQLiteBlobTooBigException e5) {
                    e = e5;
                    aVar = null;
                } catch (Exception e6) {
                    e = e6;
                    aVar = null;
                }
            } catch (Throwable th) {
                th = th;
                cursor = cursor2;
                if (cursor != null) {
                    try {
                        cursor.close();
                    } catch (Exception unused2) {
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
            cursor = null;
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
        try {
            cursorQuery.close();
        } catch (Exception unused3) {
        }
        return aVar;
    }

    private void g() {
        try {
            this.c.getWritableDatabase().delete(a.b, null, null);
            a(true);
            com.ot.pubsub.util.j.a(a, "delete table otpubsub");
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
                            long jF = a().f();
                            if (jF != 0) {
                                z = false;
                            }
                            a(z);
                            com.ot.pubsub.util.j.a(a, "after delete DB record remains=" + jF);
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
                            long jF = a().f();
                            if (jF != 0) {
                                z = false;
                            }
                            a(z);
                            com.ot.pubsub.util.j.a(a, "after delete DB record remains=" + jF);
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

    public void d() {
        com.ot.pubsub.g.a.a(new e(this));
    }

    public synchronized void a(boolean z) {
        i = z;
    }

    public synchronized boolean e() {
        return i;
    }

    public static byte[] a(String str) {
        return com.ot.pubsub.c.a.a(str.getBytes(), com.ot.pubsub.c.d.a(com.ot.pubsub.c.c.a(), true).getBytes());
    }

    public static String a(byte[] bArr) {
        return new String(com.ot.pubsub.c.a.b(bArr, com.ot.pubsub.c.d.a(com.ot.pubsub.c.c.a(), true).getBytes()));
    }

    public long f() {
        return DatabaseUtils.queryNumEntries(this.c.getReadableDatabase(), a.b);
    }

    class a extends SQLiteOpenHelper {
        public static final String a = "otpubsub.db";
        public static final String b = "otpubsub";
        public static final String c = "_id";
        public static final String d = "projectid";
        public static final String e = "topic";
        public static final String f = "data";
        public static final String g = "attribute";
        public static final String h = "gzipandencrypt";
        public static final String i = "timestamp";
        private static final int j = 2;
        private static final String k = "CREATE TABLE otpubsub (_id INTEGER PRIMARY KEY AUTOINCREMENT,projectid TEXT,topic TEXT,data BLOB,attribute BLOB,gzipandencrypt INTEGER,timestamp INTEGER)";

        public a(Context context) {
            super(context, a, (SQLiteDatabase.CursorFactory) null, 2);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL(k);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i2, int i3) {
            com.ot.pubsub.util.j.a(c.a, "onUpgrade, old=" + i2 + ", new = " + i3);
            if (i3 == 2 && i2 == 1) {
                try {
                    sQLiteDatabase.execSQL("ALTER TABLE otpubsub ADD COLUMN gzipandencrypt INTEGER DEFAULT 0");
                } catch (Throwable th) {
                    com.ot.pubsub.util.j.b(c.a, "onUpgrade Throwable" + th.getMessage());
                }
            }
        }
    }
}
