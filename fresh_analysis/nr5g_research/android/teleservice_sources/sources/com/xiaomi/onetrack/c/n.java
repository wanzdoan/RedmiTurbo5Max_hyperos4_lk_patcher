package com.xiaomi.onetrack.c;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteBlobTooBigException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.onetrack.api.am;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class n {
    private static n b = null;
    private static volatile boolean k = false;
    private a c = new a(com.xiaomi.onetrack.f.a.a());

    public static n a() {
        if (b == null) {
            b();
        }
        return b;
    }

    public static void b() {
        if (b == null) {
            synchronized (n.class) {
                try {
                    if (b == null) {
                        b = new n();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    private n() {
        d();
    }

    public synchronized void a(String str, String str2, com.xiaomi.onetrack.f.b bVar, Map map, boolean z) throws Throwable {
        try {
            try {
                c.a(new o(this, str, str2, bVar, map, z));
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
    /* JADX WARN: Code duplicated, block: B:63:0x017d  */
    /* JADX WARN: Code duplicated, block: B:64:0x018d  */
    public boolean b(String str, String str2, com.xiaomi.onetrack.f.b bVar, Map map, boolean z) throws Throwable {
        String str3;
        boolean z2 = false;
        try {
            synchronized (this.c) {
                try {
                    if (bVar != null) {
                        try {
                            if (bVar.i() && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
                                byte[] bArrB = com.xiaomi.onetrack.d.a.b(bVar.g().toString());
                                if (z || com.ot.pubsub.a.a.f.equals(bVar.e())) {
                                    if (bArrB.length > 512000) {
                                        com.xiaomi.onetrack.util.r.b("PubSubEventManager", "ad or bug Too large data, discard ***");
                                        com.xiaomi.onetrack.h.d.b.a(bVar.c(), bVar.e(), "sa", "r3", 1);
                                        try {
                                            a(str, str2, bVar, map, "sa", "r3");
                                            return false;
                                        } catch (Throwable th) {
                                            th = th;
                                            str3 = str2;
                                        }
                                    }
                                    try {
                                        throw th;
                                    } catch (Exception e) {
                                        e = e;
                                        com.xiaomi.onetrack.util.r.b("PubSubEventManager", "addEvent exception: ", e);
                                        if (z2) {
                                            com.xiaomi.onetrack.h.d.b.a(bVar.c(), bVar.e(), "sa", "su", 1);
                                        } else {
                                            com.xiaomi.onetrack.h.d.b.a(bVar.c(), bVar.e(), "sa", "r4", 1);
                                            a(str, str3, bVar, map, "sa", "r4");
                                        }
                                        return z2;
                                    }
                                }
                                if (bArrB.length > 204800) {
                                    com.xiaomi.onetrack.util.r.b("PubSubEventManager", "Too large data, discard ***");
                                    com.xiaomi.onetrack.h.d.b.a(bVar.c(), bVar.e(), "sa", "r3", 1);
                                    a(str, str2, bVar, map, "sa", "r3");
                                    return false;
                                }
                                str3 = str2;
                                SQLiteDatabase writableDatabase = this.c.getWritableDatabase();
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(com.ot.pubsub.g.f.a.d, bVar.c());
                                contentValues.put(com.ot.pubsub.g.f.a.e, bVar.d());
                                contentValues.put(com.ot.pubsub.g.f.a.l, bVar.e());
                                contentValues.put("projectid", str);
                                contentValues.put("topic", str3);
                                contentValues.put("attribute", com.xiaomi.onetrack.util.d.a(map));
                                contentValues.put("timestamp", Long.valueOf(System.currentTimeMillis()));
                                contentValues.put("data", bArrB);
                                contentValues.put(com.ot.pubsub.g.f.a.m, Integer.valueOf(bVar.f()));
                                long jInsert = writableDatabase.insert("otps_events", null, contentValues);
                                com.xiaomi.onetrack.util.r.a("PubSubEventManager", "DB-Thread: addOTMessageToDatabase , row=" + jInsert);
                                boolean z3 = jInsert != -1;
                                if (z3) {
                                    try {
                                        if (com.xiaomi.onetrack.util.r.a) {
                                            com.xiaomi.onetrack.util.r.a("PubSubEventManager", "添加后，DB 中事件个数为 " + f());
                                        }
                                        a(false);
                                    } catch (Throwable th2) {
                                        th = th2;
                                        z2 = z3;
                                        throw th;
                                    }
                                }
                                z2 = z3;
                                if (z2) {
                                    com.xiaomi.onetrack.h.d.b.a(bVar.c(), bVar.e(), "sa", "su", 1);
                                } else {
                                    com.xiaomi.onetrack.h.d.b.a(bVar.c(), bVar.e(), "sa", "r4", 1);
                                    a(str, str3, bVar, map, "sa", "r4");
                                }
                                return z2;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            str3 = str2;
                        }
                    }
                    com.xiaomi.onetrack.util.r.c("PubSubEventManager", "addEventToDatabase message is inValid. topic:" + str2 + ", data EventName:" + bVar.e() + ", projectId:" + str);
                    return false;
                } catch (Throwable th4) {
                    th = th4;
                }
            }
        } catch (Exception e2) {
            e = e2;
            str3 = str2;
        }
    }

    public boolean a(ac acVar) {
        synchronized (this.c) {
            SQLiteDatabase sQLiteDatabase = null;
            try {
                if (acVar != null) {
                    try {
                        ArrayList arrayList = acVar.a;
                        if (arrayList != null && arrayList.size() != 0) {
                            SQLiteDatabase writableDatabase = this.c.getWritableDatabase();
                            try {
                                writableDatabase.beginTransaction();
                                ArrayList arrayList2 = acVar.a;
                                int size = arrayList2.size();
                                int i = 0;
                                while (i < size) {
                                    Object obj = arrayList2.get(i);
                                    i++;
                                    y yVar = (y) obj;
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(com.ot.pubsub.g.f.a.d, yVar.b());
                                    contentValues.put(com.ot.pubsub.g.f.a.e, yVar.c());
                                    contentValues.put(com.ot.pubsub.g.f.a.l, yVar.j());
                                    contentValues.put("projectid", yVar.d());
                                    contentValues.put("topic", yVar.e());
                                    contentValues.put("attribute", com.xiaomi.onetrack.util.d.a(yVar.h()));
                                    contentValues.put("timestamp", Long.valueOf(yVar.i()));
                                    contentValues.put("data", yVar.f());
                                    contentValues.put(com.ot.pubsub.g.f.a.m, Integer.valueOf(yVar.g()));
                                    com.xiaomi.onetrack.util.r.a("PubSubEventManager", "DB-Thread: EventManager.addTransformDataToDB , row=" + writableDatabase.insert("otps_events", null, contentValues));
                                }
                                a(false);
                                writableDatabase.setTransactionSuccessful();
                                try {
                                    writableDatabase.endTransaction();
                                } catch (Exception e) {
                                    com.xiaomi.onetrack.util.r.b("PubSubEventManager", "addTransformDataToDB Exception while endTransaction:" + e);
                                }
                                return true;
                            } catch (Exception e2) {
                                e = e2;
                                sQLiteDatabase = writableDatabase;
                                com.xiaomi.onetrack.util.r.a("PubSubEventManager", "DB-Thread: addTransformDataToDB , exception:" + e.getMessage());
                                if (sQLiteDatabase != null) {
                                    try {
                                        sQLiteDatabase.endTransaction();
                                    } catch (Exception e3) {
                                        com.xiaomi.onetrack.util.r.b("PubSubEventManager", "addTransformDataToDB Exception while endTransaction:" + e3);
                                    }
                                }
                                return false;
                            } catch (Throwable th) {
                                th = th;
                                sQLiteDatabase = writableDatabase;
                                if (sQLiteDatabase != null) {
                                    try {
                                        sQLiteDatabase.endTransaction();
                                    } catch (Exception e4) {
                                        com.xiaomi.onetrack.util.r.b("PubSubEventManager", "addTransformDataToDB Exception while endTransaction:" + e4);
                                    }
                                }
                                throw th;
                            }
                        }
                    } catch (Exception e5) {
                        e = e5;
                    }
                }
                com.xiaomi.onetrack.util.r.a("PubSubEventManager", "TransformEvent记录为空，即将返回");
                return false;
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:110:0x0112 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:111:0x0118 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:48:0x0113  */
    /* JADX WARN: Code duplicated, block: B:52:0x012f A[LOOP:0: B:5:0x0063->B:52:0x012f, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:96:0x0199 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Not initialized variable reg: 5, insn: 0x00aa: MOVE (r3 I:??[OBJECT, ARRAY]) = (r5 I:??[OBJECT, ARRAY]), block:B:15:0x00aa */
    /* JADX WARN: Type inference failed for: r26v0 */
    /* JADX WARN: Type inference failed for: r26v1, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r26v2 */
    /* JADX WARN: Type inference failed for: r27v0 */
    /* JADX WARN: Type inference failed for: r27v1, types: [java.util.Map] */
    /* JADX WARN: Type inference failed for: r27v2 */
    public q a(int i) {
        q qVar;
        Cursor cursorQuery;
        Cursor cursor;
        boolean z;
        boolean z2;
        ?? A;
        Object objA;
        ?? r27;
        int length;
        Map map;
        Cursor cursor2 = null;
        qVar = null;
        qVar = null;
        q qVar2 = null;
        try {
            try {
                try {
                    cursorQuery = this.c.getReadableDatabase().query("otps_events", null, null, null, null, null, "priority ASC, _id ASC", String.valueOf(MiRilHook.HOOK_ID_START_URC_BY_AT));
                    try {
                        int columnIndex = cursorQuery.getColumnIndex("_id");
                        cursorQuery.getColumnIndex(com.ot.pubsub.g.f.a.d);
                        int columnIndex2 = cursorQuery.getColumnIndex("projectid");
                        int columnIndex3 = cursorQuery.getColumnIndex("topic");
                        int columnIndex4 = cursorQuery.getColumnIndex("data");
                        int columnIndex5 = cursorQuery.getColumnIndex("attribute");
                        int columnIndex6 = cursorQuery.getColumnIndex("timestamp");
                        int columnIndex7 = cursorQuery.getColumnIndex(com.ot.pubsub.g.f.a.m);
                        ArrayList arrayList = new ArrayList();
                        ArrayList arrayList2 = new ArrayList();
                        boolean z3 = true;
                        boolean z4 = true;
                        int i2 = 0;
                        int i3 = 0;
                        while (true) {
                            if (!cursorQuery.moveToNext()) {
                                qVar = qVar2;
                                break;
                            }
                            long j = cursorQuery.getLong(columnIndex);
                            String string = cursorQuery.getString(columnIndex2);
                            String string2 = cursorQuery.getString(columnIndex3);
                            byte[] blob = cursorQuery.getBlob(columnIndex5);
                            byte[] blob2 = cursorQuery.getBlob(columnIndex4);
                            long j2 = cursorQuery.getLong(columnIndex6);
                            try {
                                if (z4) {
                                    int i4 = cursorQuery.getInt(columnIndex7);
                                    if (i4 > i) {
                                        com.xiaomi.onetrack.util.r.a("PubSubEventManager", "ps no records of priority[" + i + "], first record priority=" + i4);
                                        try {
                                            cursorQuery.close();
                                        } catch (Exception unused) {
                                        }
                                        return qVar2;
                                    }
                                    z2 = false;
                                    com.xiaomi.onetrack.util.r.b("PubSubEventManager", "blob too big ***", e);
                                    g();
                                    if (cursorQuery != null) {
                                        cursorQuery.close();
                                    }
                                    return qVar;
                                }
                                z2 = z4;
                                if (objA != null) {
                                    qVar = qVar2;
                                    try {
                                        if (objA instanceof Map) {
                                            map = (Map) objA;
                                        }
                                        r27 = map;
                                        arrayList.add(new m(j, string, string2, A, r27, j2));
                                        arrayList2.add(Long.valueOf(j));
                                        i2++;
                                        length = i3 + A.length();
                                        if (arrayList.size() < 1000) {
                                            break;
                                        }
                                        if (length >= 307200) {
                                            com.xiaomi.onetrack.util.r.a("PubSubEventManager", "reached max len: " + length);
                                            break;
                                        }
                                        i3 = length;
                                        qVar2 = qVar;
                                        z4 = z2;
                                    } catch (SQLiteBlobTooBigException e) {
                                        e = e;
                                        com.xiaomi.onetrack.util.r.b("PubSubEventManager", "blob too big ***", e);
                                        g();
                                        if (cursorQuery != null) {
                                        }
                                        return qVar;
                                    } catch (Exception e2) {
                                        e = e2;
                                        com.xiaomi.onetrack.util.r.a("PubSubEventManager", "", e);
                                        if (cursorQuery != null) {
                                        }
                                        return qVar;
                                    }
                                } else {
                                    qVar = qVar2;
                                }
                                arrayList.add(new m(j, string, string2, A, r27, j2));
                                arrayList2.add(Long.valueOf(j));
                                i2++;
                            } catch (Exception e3) {
                                com.xiaomi.onetrack.util.r.b("PubSubEventManager", "*** error ***", e3);
                            }
                            A = blob2 != null ? com.xiaomi.onetrack.d.a.a(blob2) : qVar2;
                            objA = com.xiaomi.onetrack.util.d.a(blob);
                            if (objA == null) {
                                map = new HashMap();
                                r27 = map;
                            } else {
                                r27 = qVar;
                            }
                            length = i3 + A.length();
                            if (arrayList.size() < 1000) {
                                break;
                                break;
                            }
                            if (length >= 307200) {
                                com.xiaomi.onetrack.util.r.a("PubSubEventManager", "reached max len: " + length);
                                break;
                            }
                            i3 = length;
                            qVar2 = qVar;
                            z4 = z2;
                        }
                        int i5 = i2;
                        if (arrayList2.size() > 0) {
                            try {
                                if (!cursorQuery.isAfterLast()) {
                                    if (cursorQuery.getInt(columnIndex7) <= i) {
                                        z3 = false;
                                    }
                                } else {
                                    if (i5 == 300) {
                                        com.xiaomi.onetrack.util.r.a("PubSubEventManager", "cursor max one query number");
                                        z = false;
                                    } else {
                                        com.xiaomi.onetrack.util.r.a("PubSubEventManager", "cursor isAfterLast");
                                    }
                                    q qVar3 = new q(arrayList, i5, arrayList2, z);
                                    cursorQuery.close();
                                    return qVar3;
                                }
                                cursorQuery.close();
                            } catch (Exception unused2) {
                            }
                            z = z3;
                            q qVar4 = new q(arrayList, i5, arrayList2, z);
                            return qVar4;
                        }
                    } catch (SQLiteBlobTooBigException e4) {
                        e = e4;
                        qVar = qVar2;
                    } catch (Exception e5) {
                        e = e5;
                        qVar = qVar2;
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
            } catch (SQLiteBlobTooBigException e6) {
                e = e6;
                qVar = null;
                cursorQuery = null;
            } catch (Exception e7) {
                e = e7;
                qVar = null;
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
        return qVar;
    }

    /* JADX WARN: Code duplicated, block: B:70:0x0125 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Not initialized variable reg: 4, insn: 0x0096: MOVE (r2 I:??[OBJECT, ARRAY]) = (r4 I:??[OBJECT, ARRAY]), block:B:13:0x0096 */
    public ac c() throws Throwable {
        Cursor cursor;
        ac acVar;
        Cursor cursorQuery;
        Cursor cursor2;
        boolean z;
        byte[] bArr;
        try {
            try {
                cursorQuery = this.c.getReadableDatabase().query("otps_events", null, null, null, null, null, "priority ASC, _id ASC", String.valueOf(MiRilHook.HOOK_ID_START_URC_BY_AT));
                try {
                    int columnIndex = cursorQuery.getColumnIndex("_id");
                    int columnIndex2 = cursorQuery.getColumnIndex(com.ot.pubsub.g.f.a.d);
                    int columnIndex3 = cursorQuery.getColumnIndex(com.ot.pubsub.g.f.a.e);
                    int columnIndex4 = cursorQuery.getColumnIndex(com.ot.pubsub.g.f.a.l);
                    int columnIndex5 = cursorQuery.getColumnIndex("data");
                    int columnIndex6 = cursorQuery.getColumnIndex("timestamp");
                    int columnIndex7 = cursorQuery.getColumnIndex(com.ot.pubsub.g.f.a.m);
                    ArrayList arrayList = new ArrayList();
                    ArrayList arrayList2 = new ArrayList();
                    int i = 0;
                    int i2 = 0;
                    while (true) {
                        if (!cursorQuery.moveToNext()) {
                            acVar = null;
                            break;
                        }
                        long j = cursorQuery.getLong(columnIndex);
                        String string = cursorQuery.getString(columnIndex2);
                        String string2 = cursorQuery.getString(columnIndex4);
                        String string3 = cursorQuery.getString(columnIndex3);
                        byte[] blob = cursorQuery.getBlob(columnIndex5);
                        try {
                            bArr = blob;
                            acVar = null;
                            try {
                                arrayList.add(new y(j, null, null, string, string3, string2, blob, null, cursorQuery.getLong(columnIndex6), cursorQuery.getInt(columnIndex7)));
                                arrayList2.add(Long.valueOf(j));
                                i++;
                            } catch (Exception e) {
                                e = e;
                                com.xiaomi.onetrack.util.r.b("PubSubEventManager", "*** error ***", e);
                            }
                        } catch (Exception e2) {
                            e = e2;
                            bArr = blob;
                            acVar = null;
                        }
                        try {
                            int length = i2 + bArr.length;
                            if (length >= 5242880) {
                                com.xiaomi.onetrack.util.r.a("PubSubEventManager", "reached max len: " + length);
                                break;
                            }
                            i2 = length;
                        } catch (SQLiteBlobTooBigException e3) {
                            e = e3;
                            com.xiaomi.onetrack.util.r.b("PubSubEventManager", "blob too big ***", e);
                            g();
                            if (cursorQuery != null) {
                            }
                            return acVar;
                        } catch (Exception e4) {
                            e = e4;
                            com.xiaomi.onetrack.util.r.a("PubSubEventManager", "", e);
                            if (cursorQuery != null) {
                            }
                            return acVar;
                        }
                    }
                    int i3 = i;
                    if (arrayList2.size() > 0) {
                        if (!cursorQuery.isAfterLast()) {
                            z = false;
                        } else if (i3 == 300) {
                            com.xiaomi.onetrack.util.r.a("PubSubEventManager", "cursor max one query number");
                            z = false;
                        } else {
                            com.xiaomi.onetrack.util.r.a("PubSubEventManager", "cursor isAfterLast");
                            z = true;
                        }
                        ac acVar2 = new ac(arrayList, i3, arrayList2, z);
                        try {
                            cursorQuery.close();
                        } catch (Exception unused) {
                        }
                        return acVar2;
                    }
                } catch (SQLiteBlobTooBigException e5) {
                    e = e5;
                    acVar = null;
                } catch (Exception e6) {
                    e = e6;
                    acVar = null;
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
            acVar = null;
            cursorQuery = null;
        } catch (Exception e8) {
            e = e8;
            acVar = null;
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
        return acVar;
    }

    private void g() {
        try {
            this.c.getWritableDatabase().delete("otps_events", null, null);
            a(true);
            com.xiaomi.onetrack.util.r.a("PubSubEventManager", "delete table otps_events");
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.b("PubSubEventManager", "delete table error: " + e.getMessage());
        }
    }

    private void a(String str, String str2, com.xiaomi.onetrack.f.b bVar, Map map, String str3, String str4) throws Throwable {
        if (!com.xiaomi.onetrack.h.a.a.a().b(bVar.c(), bVar.e())) {
            com.xiaomi.onetrack.util.r.a("PubSubEventManager", "trackFailedEvent, should not record");
            return;
        }
        try {
            a(str, str2, am.a(bVar, str3, str4), map, false);
            com.xiaomi.onetrack.h.a.a.a().c(bVar.c(), bVar.e());
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.b("PubSubEventManager", "trackFailedEvent error", e);
        }
    }

    public int a(List list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(Long.valueOf(((m) it.next()).b()));
        }
        return a(arrayList);
    }

    public int a(ArrayList arrayList) {
        int iDelete;
        synchronized (this.c) {
            try {
                try {
                    SQLiteDatabase writableDatabase = this.c.getWritableDatabase();
                    boolean z = true;
                    StringBuilder sb = new StringBuilder(((Long.toString(((Long) arrayList.get(0)).longValue()).length() + 1) * arrayList.size()) + 16);
                    sb.append("_id");
                    sb.append(" in (");
                    sb.append(arrayList.get(0));
                    int size = arrayList.size();
                    for (int i = 1; i < size; i++) {
                        sb.append(",");
                        sb.append(arrayList.get(i));
                    }
                    sb.append(")");
                    iDelete = writableDatabase.delete("otps_events", sb.toString(), null);
                    com.xiaomi.onetrack.util.r.a("PubSubEventManager", "*** *** deleted events count " + iDelete);
                    long jF = a().f();
                    if (jF != 0) {
                        z = false;
                    }
                    a(z);
                    com.xiaomi.onetrack.util.r.a("PubSubEventManager", "after delete DB record remains=" + jF);
                } catch (Exception e) {
                    com.xiaomi.onetrack.util.r.b("PubSubEventManager", "e=" + e);
                    return 0;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return iDelete;
    }

    public int b(List list) {
        synchronized (this.c) {
            if (list != null) {
                try {
                    if (list.size() != 0) {
                        try {
                            SQLiteDatabase writableDatabase = this.c.getWritableDatabase();
                            boolean z = true;
                            StringBuilder sb = new StringBuilder(((Long.toString(((m) list.get(0)).b()).length() + 1) * list.size()) + 16);
                            sb.append("_id");
                            sb.append(" in (");
                            int size = list.size();
                            boolean z2 = false;
                            for (int i = 0; i < size; i++) {
                                if (com.xiaomi.onetrack.util.ae.a(((m) list.get(i)).g(), com.ot.pubsub.util.v.b)) {
                                    if (i == size - 1) {
                                        sb.append(((m) list.get(i)).b());
                                    } else {
                                        sb.append(((m) list.get(i)).b());
                                        sb.append(",");
                                    }
                                    z2 = true;
                                }
                            }
                            sb.append(")");
                            if (!z2) {
                                return 0;
                            }
                            com.xiaomi.onetrack.util.r.a("PubSubEventManager", "*** *** deleted events sb id " + sb.toString());
                            int iDelete = writableDatabase.delete("otps_events", sb.toString(), null);
                            com.xiaomi.onetrack.util.r.a("PubSubEventManager", "*** *** deleted events count " + iDelete);
                            long jF = a().f();
                            if (jF != 0) {
                                z = false;
                            }
                            a(z);
                            com.xiaomi.onetrack.util.r.a("PubSubEventManager", "after delete DB record remains=" + jF);
                            return iDelete;
                        } catch (Exception e) {
                            com.xiaomi.onetrack.util.r.b("PubSubEventManager", "e=" + e);
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
        c.a(new p(this));
    }

    public synchronized void a(boolean z) {
        k = z;
    }

    public synchronized boolean e() {
        return k;
    }

    public long f() {
        return DatabaseUtils.queryNumEntries(this.c.getReadableDatabase(), "otps_events");
    }

    class a extends SQLiteOpenHelper {
        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        }

        public a(Context context) {
            super(context, "ot_ps_sdk", (SQLiteDatabase.CursorFactory) null, 1);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL("CREATE TABLE otps_events (_id INTEGER PRIMARY KEY AUTOINCREMENT,appid TEXT,package TEXT,event_name TEXT,projectid TEXT,topic TEXT,data BLOB,attribute BLOB,priority INTEGER,timestamp INTEGER)");
        }
    }
}
