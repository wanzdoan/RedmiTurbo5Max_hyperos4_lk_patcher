package com.xiaomi.onetrack.b;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Process;
import android.text.TextUtils;
import com.xiaomi.onetrack.util.ab;
import com.xiaomi.onetrack.util.r;
import com.xiaomi.onetrack.util.s;
import com.xiaomi.onetrack.util.z;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes2.dex */
public class q {
    private static volatile q d;
    private ConcurrentHashMap b = new ConcurrentHashMap();
    private ConcurrentHashMap c = new ConcurrentHashMap();
    private a e = new a(com.xiaomi.onetrack.f.a.a());

    public static q a() {
        if (d == null) {
            synchronized (q.class) {
                try {
                    if (d == null) {
                        d = new q();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return d;
    }

    private q() {
    }

    public p a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        p pVar = (p) this.b.get(str);
        return pVar == null ? b("projectId", str) : pVar;
    }

    public p b(String str) {
        String str2 = (String) this.c.get(str);
        if (ab.b(str2)) {
            return a(str2);
        }
        return b(com.ot.pubsub.j.d.b, str);
    }

    public void a(String str, String str2, String str3, String str4) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            return;
        }
        p pVarA = a(str);
        if (pVarA == null) {
            p pVar = new p();
            pVar.b = str;
            pVar.c = str3;
            pVar.d.add(str2);
            pVar.a = str4;
            pVar.f = System.currentTimeMillis();
            a(pVar);
            return;
        }
        if (str.equals(pVarA.b) && str3.equals(pVarA.c) && !pVarA.d.contains(str2)) {
            pVarA.d.add(str2);
            pVarA.f = System.currentTimeMillis();
            a(pVarA);
        }
    }

    public void a(p pVar) {
        if (pVar == null || TextUtils.isEmpty(pVar.b)) {
            return;
        }
        this.c.put(pVar.a, pVar.b);
        this.b.put(pVar.b, pVar);
        a(pVar.b, pVar);
    }

    public com.xiaomi.onetrack.i.a b() {
        com.xiaomi.onetrack.i.a aVarA;
        if (this.b.get("miui-analytics") != null) {
            aVarA = ((p) this.b.get("miui-analytics")).a();
            if (a(aVarA)) {
                r.a("PubSubConfigManager", "getTokenBean memory cache is valid");
                return aVarA;
            }
        } else {
            aVarA = null;
        }
        p pVarB = b("projectId", "miui-analytics");
        if (pVarB == null) {
            pVarB = d();
        }
        if (aVarA == null) {
            com.xiaomi.onetrack.i.a aVarA2 = pVarB.a();
            if (a(aVarA2)) {
                this.b.put("miui-analytics", pVarB);
                r.a("PubSubConfigManager", "getTokenBean db cache is valid");
                return aVarA2;
            }
        }
        com.xiaomi.onetrack.i.a aVarA3 = a("miui-analytics", "6417a1813b3388817cd5b7c34303539534286cb3");
        if (!a(aVarA3)) {
            return null;
        }
        pVarB.a(aVarA3);
        a(pVarB);
        return aVarA3;
    }

    private p d() {
        a().a("miui-analytics", "onetrack_active", "6417a1813b3388817cd5b7c34303539534286cb3", "001");
        return b("projectId", "miui-analytics");
    }

    public com.xiaomi.onetrack.i.a a(String str, String str2) {
        com.xiaomi.onetrack.i.a aVar = null;
        try {
            if (!s.a("PubSubConfigManager") && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2) && com.xiaomi.onetrack.i.b.a().b()) {
                HashMap map = new HashMap();
                map.put("timestamp", String.valueOf(System.currentTimeMillis()));
                String strA = com.xiaomi.onetrack.g.b.a(str2, z.a().g(), map);
                com.xiaomi.onetrack.i.a aVarA = com.xiaomi.onetrack.i.a.a(strA);
                if (aVarA != null) {
                    try {
                        if (aVarA.a == 0) {
                            r.a("PubSubConfigManager", "getToken success");
                            com.xiaomi.onetrack.i.b.a().d();
                            return aVarA;
                        }
                    } catch (Exception e) {
                        e = e;
                        aVar = aVarA;
                        com.xiaomi.onetrack.i.b.a().c();
                        r.a("PubSubConfigManager", "getNewToken e:" + e.getMessage());
                        return aVar;
                    }
                }
                if (aVarA != null && aVarA.a == -10) {
                    com.xiaomi.onetrack.i.b.a().c();
                    r.a("PubSubConfigManager", "postGetToken invalid ,response:" + strA);
                    this.b.remove(str);
                    c(str);
                    return null;
                }
                com.xiaomi.onetrack.i.b.a().c();
                r.a("PubSubConfigManager", "postGetToken responseData response:" + strA);
                return null;
            }
            return null;
        } catch (Exception e2) {
            e = e2;
        }
    }

    /* JADX WARN: Code duplicated, block: B:60:0x0109 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:68:? A[Catch: all -> 0x00c3, SYNTHETIC, TryCatch #1 {, blocks: (B:25:0x00c1, B:24:0x00ac, B:45:0x0105, B:32:0x00ca, B:44:0x0102, B:52:0x0123, B:51:0x010e, B:43:0x00f0, B:29:0x00c5, B:21:0x00a7, B:48:0x0109, B:40:0x00eb), top: B:55:0x000e, inners: #2, #3, #4, #8 }] */
    private p b(String str, String str2) {
        Throwable th;
        Cursor cursorQuery;
        String str3;
        String str4;
        synchronized (this.e) {
            Cursor cursor = null;
            try {
                cursorQuery = this.e.getWritableDatabase().query("project_info", null, str + "=?", new String[]{str2}, null, null, null);
                try {
                    try {
                        int columnIndex = cursorQuery.getColumnIndex("projectId");
                        int columnIndex2 = cursorQuery.getColumnIndex(com.ot.pubsub.j.d.b);
                        int columnIndex3 = cursorQuery.getColumnIndex("privateKeyId");
                        int columnIndex4 = cursorQuery.getColumnIndex("topics");
                        int columnIndex5 = cursorQuery.getColumnIndex("tokenBean");
                        int columnIndex6 = cursorQuery.getColumnIndex("timestamp");
                        if (!cursorQuery.moveToNext()) {
                            try {
                                cursorQuery.close();
                            } catch (Throwable th2) {
                                str3 = "PubSubConfigManager";
                                str4 = "closeSafely e: " + th2.getMessage();
                                r.b(str3, str4);
                            }
                            return null;
                        }
                        p pVar = new p();
                        pVar.b = cursorQuery.getString(columnIndex);
                        pVar.a = cursorQuery.getString(columnIndex2);
                        pVar.c = cursorQuery.getString(columnIndex3);
                        Object objA = com.xiaomi.onetrack.util.d.a(cursorQuery.getBlob(columnIndex4));
                        if (objA instanceof List) {
                            List list = (List) objA;
                            if (!list.isEmpty()) {
                                pVar.d = list;
                            }
                        }
                        String string = cursorQuery.getString(columnIndex5);
                        if (ab.b(string)) {
                            pVar.e = string;
                        }
                        pVar.f = cursorQuery.getLong(columnIndex6);
                        try {
                            cursorQuery.close();
                        } catch (Throwable th3) {
                            r.b("PubSubConfigManager", "closeSafely e: " + th3.getMessage());
                        }
                        return pVar;
                    } catch (Exception e) {
                        e = e;
                        r.a("PubSubConfigManager", "exception while getConfig", e);
                        if (cursorQuery != null) {
                            try {
                                cursorQuery.close();
                            } catch (Throwable th4) {
                                str3 = "PubSubConfigManager";
                                str4 = "closeSafely e: " + th4.getMessage();
                                r.b(str3, str4);
                            }
                        }
                    }
                } catch (Throwable th5) {
                    th = th5;
                    cursor = cursorQuery;
                    if (cursor != null) {
                        throw th;
                    }
                    try {
                        cursor.close();
                        throw th;
                    } catch (Throwable th6) {
                        r.b("PubSubConfigManager", "closeSafely e: " + th6.getMessage());
                        throw th;
                    }
                }
            } catch (Exception e2) {
                e = e2;
                cursorQuery = null;
            } catch (Throwable th7) {
                th = th7;
                if (cursor != null) {
                    throw th;
                }
                cursor.close();
                throw th;
            }
            throw th;
        }
    }

    /* JADX WARN: Code duplicated, block: B:64:0x00d9 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    private void a(String str, p pVar) {
        Throwable th;
        SQLiteDatabase writableDatabase;
        Exception e;
        String str2;
        StringBuilder sb;
        synchronized (this.e) {
            try {
                if (TextUtils.isEmpty(str) || pVar == null || TextUtils.isEmpty(pVar.b)) {
                    return;
                }
                try {
                    writableDatabase = this.e.getWritableDatabase();
                    try {
                        try {
                            writableDatabase.beginTransaction();
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(com.ot.pubsub.j.d.b, pVar.a);
                            contentValues.put("projectId", pVar.b);
                            contentValues.put("privateKeyId", pVar.c);
                            contentValues.put("timestamp", Long.valueOf(pVar.f));
                            List list = pVar.d;
                            if (list != null) {
                                contentValues.put("topics", com.xiaomi.onetrack.util.d.a(list));
                            }
                            String str3 = pVar.e;
                            if (str3 != null) {
                                contentValues.put("tokenBean", str3);
                            }
                            if (DatabaseUtils.queryNumEntries(writableDatabase, "project_info", "projectId=?", new String[]{pVar.b}) > 0) {
                                writableDatabase.update("project_info", contentValues, "projectId=?", new String[]{pVar.b});
                            } else {
                                writableDatabase.insert("project_info", null, contentValues);
                            }
                            writableDatabase.setTransactionSuccessful();
                            try {
                                writableDatabase.endTransaction();
                            } catch (Exception e2) {
                                str2 = "PubSubConfigManager";
                                sb = new StringBuilder("addProjectBeanToDB Exception while endTransaction:");
                                sb.append(e2.getMessage());
                                r.b(str2, sb.toString());
                            }
                        } catch (Exception e3) {
                            e = e3;
                            r.b("PubSubConfigManager", "updateToDb error: ", e);
                            if (writableDatabase != null) {
                                try {
                                    writableDatabase.endTransaction();
                                } catch (Exception e4) {
                                    str2 = "PubSubConfigManager";
                                    sb = new StringBuilder("addProjectBeanToDB Exception while endTransaction:");
                                    sb.append(e4.getMessage());
                                    r.b(str2, sb.toString());
                                }
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (writableDatabase != null) {
                            try {
                                writableDatabase.endTransaction();
                            } catch (Exception e5) {
                                r.b("PubSubConfigManager", "addProjectBeanToDB Exception while endTransaction:" + e5.getMessage());
                            }
                        }
                        throw th;
                    }
                } catch (Exception e6) {
                    e = e6;
                    writableDatabase = null;
                } catch (Throwable th3) {
                    th = th3;
                    writableDatabase = null;
                    if (writableDatabase != null) {
                        writableDatabase.endTransaction();
                    }
                    throw th;
                }
            } catch (Exception e7) {
                r.a("PubSubConfigManager", "addProjectBeanToDB Exception:" + e7.getMessage());
            }
        }
    }

    private void b(String str, p pVar) {
        String str2;
        StringBuilder sb;
        synchronized (this.e) {
            try {
                if (TextUtils.isEmpty(str) || pVar == null || TextUtils.isEmpty(pVar.b)) {
                    return;
                }
                SQLiteDatabase writableDatabase = null;
                try {
                    try {
                        writableDatabase = this.e.getWritableDatabase();
                        writableDatabase.beginTransaction();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("timestamp", Long.valueOf(pVar.f));
                        contentValues.put("tokenBean", pVar.e);
                        if (DatabaseUtils.queryNumEntries(writableDatabase, "project_info", "projectId=?", new String[]{pVar.b}) > 0) {
                            writableDatabase.update("project_info", contentValues, "projectId=?", new String[]{pVar.b});
                        }
                        if (r.a) {
                            r.a("PubSubConfigManager", "forceRefreshTokenToDB: " + contentValues.toString() + "tid:" + Process.myTid());
                        }
                        writableDatabase.setTransactionSuccessful();
                        try {
                            writableDatabase.endTransaction();
                        } catch (Throwable th) {
                            str2 = "PubSubConfigManager";
                            sb = new StringBuilder("forceRefreshTokenToDB endTransactionSafely e: ");
                            sb.append(th.getMessage());
                            r.b(str2, sb.toString());
                        }
                    } catch (Throwable th2) {
                        if (writableDatabase != null) {
                            try {
                                writableDatabase.endTransaction();
                            } catch (Throwable th3) {
                                r.b("PubSubConfigManager", "forceRefreshTokenToDB endTransactionSafely e: " + th3.getMessage());
                            }
                        }
                        throw th2;
                    }
                } catch (Exception e) {
                    r.b("PubSubConfigManager", "updateToDb error: ", e);
                    if (writableDatabase != null) {
                        try {
                            writableDatabase.endTransaction();
                        } catch (Throwable th4) {
                            str2 = "PubSubConfigManager";
                            sb = new StringBuilder("forceRefreshTokenToDB endTransactionSafely e: ");
                            sb.append(th4.getMessage());
                            r.b(str2, sb.toString());
                        }
                    }
                }
            } catch (Exception e2) {
                r.a("PubSubConfigManager", "addProjectBeanToDB Exception:" + e2.getMessage());
            }
        }
    }

    public void c(String str) {
        synchronized (this.e) {
            try {
                this.e.getWritableDatabase().delete("project_info", "projectId=?", new String[]{str});
            } catch (Exception e) {
                r.d("PubSubConfigManager", "delete project id info failed with " + e);
            }
        }
    }

    public com.xiaomi.onetrack.i.a c() {
        try {
            p pVarB = b("projectId", "miui-analytics");
            if (pVarB == null) {
                pVarB = d();
            }
            com.xiaomi.onetrack.i.a aVarA = a("miui-analytics", pVarB.c);
            if (a(aVarA)) {
                pVarB.a(aVarA);
                a(pVarB);
                return aVarA;
            }
            pVarB.b();
            this.b.put("miui-analytics", pVarB);
            b("miui-analytics", pVarB);
            return aVarA;
        } catch (Exception e) {
            r.b("PubSubConfigManager", "forceRefreshToken Exception：" + e.getMessage());
            return null;
        }
    }

    class a extends SQLiteOpenHelper {
        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        }

        public a(Context context) {
            super(context, "ot_ps_project_info", (SQLiteDatabase.CursorFactory) null, 1);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL("CREATE TABLE project_info (_id INTEGER PRIMARY KEY AUTOINCREMENT,appId TEXT,projectId TEXT,privateKeyId TEXT,topics BLOB,tokenBean TEXT,timestamp INTEGER)");
        }
    }

    public boolean a(com.xiaomi.onetrack.i.a aVar) {
        if (aVar == null) {
            return false;
        }
        try {
            if (!TextUtils.isEmpty(aVar.d) && aVar.e > 0) {
                long jCurrentTimeMillis = System.currentTimeMillis();
                int iNextInt = aVar.e - new Random().nextInt(aVar.e / 14);
                long j = ((jCurrentTimeMillis - aVar.f) / 1000) + 180;
                r.a("PubSubConfigManager", "interval:" + j + "_currentTimeMillis:" + jCurrentTimeMillis + "_token.local_time:" + aVar.f);
                if (j >= iNextInt) {
                    r.a("PubSubConfigManager", "interval:" + j + "_localExpiresTime:" + iNextInt + "_isValid:false");
                    return false;
                }
                r.a("PubSubConfigManager", "interval:" + j + "_localExpiresTime:" + iNextInt + "_isValid:true");
                return true;
            }
            return false;
        } catch (Exception e) {
            r.b("PubSubConfigManager", "isLocalTokenValid Exception:" + e.getMessage());
            return true;
        }
    }
}
