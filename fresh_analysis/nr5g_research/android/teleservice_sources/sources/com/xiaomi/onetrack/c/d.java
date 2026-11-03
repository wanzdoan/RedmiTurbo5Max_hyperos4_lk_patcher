package com.xiaomi.onetrack.c;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteBlobTooBigException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.ArrayMap;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.onetrack.api.am;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class d {
    private static d j;
    private static BroadcastReceiver m = new e();
    private a k;

    public static d a() {
        if (j == null) {
            a(com.xiaomi.onetrack.f.a.b());
        }
        return j;
    }

    public static void a(Context context) {
        if (j == null) {
            synchronized (d.class) {
                try {
                    if (j == null) {
                        j = new d();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    private d() {
        Context contextA = com.xiaomi.onetrack.f.a.a();
        this.k = new a(contextA);
        c();
        b(contextA);
    }

    private static void b(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        context.registerReceiver(m, intentFilter);
    }

    public synchronized void a(com.xiaomi.onetrack.f.b bVar, boolean z) {
        c.a(new g(this, bVar, z));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean b(com.xiaomi.onetrack.f.b bVar, boolean z) throws Throwable {
        boolean z2;
        boolean z3 = false;
        try {
            synchronized (this.k) {
                try {
                    if (!bVar.i()) {
                        com.xiaomi.onetrack.util.r.c("EventManager", "addEventToDatabase event is inValid, event:" + bVar.e());
                        return false;
                    }
                    SQLiteDatabase writableDatabase = this.k.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(com.ot.pubsub.g.f.a.d, bVar.c());
                    contentValues.put(com.ot.pubsub.g.f.a.e, bVar.d());
                    contentValues.put(com.ot.pubsub.g.f.a.l, bVar.e());
                    contentValues.put(com.ot.pubsub.g.f.a.m, Integer.valueOf(bVar.f()));
                    contentValues.put("timestamp", Long.valueOf(System.currentTimeMillis()));
                    byte[] bArrB = com.xiaomi.onetrack.d.a.b(bVar.g().toString());
                    if (z || com.ot.pubsub.a.a.f.equals(bVar.e())) {
                        if (bArrB.length > 512000) {
                            com.xiaomi.onetrack.util.r.b("EventManager", "ad or bug Too large data, discard ***");
                            com.xiaomi.onetrack.h.d.b.a(bVar.c(), bVar.e(), "sa", "r3", 1);
                            a(bVar, "sa", "r3");
                            return false;
                        }
                    } else if (bArrB.length > 204800) {
                        com.xiaomi.onetrack.util.r.b("EventManager", "Too large data, discard ***");
                        com.xiaomi.onetrack.h.d.b.a(bVar.c(), bVar.e(), "sa", "r3", 1);
                        a(bVar, "sa", "r3");
                        return false;
                    }
                    contentValues.put("data", bArrB);
                    long jInsert = writableDatabase.insert(com.ot.pubsub.b.a.c, null, contentValues);
                    com.xiaomi.onetrack.util.r.a("EventManager", "DB-Thread: EventManager.addEventToDatabase , row=" + jInsert);
                    z2 = jInsert != -1;
                    if (z2) {
                        try {
                            if (com.xiaomi.onetrack.util.r.a) {
                                com.xiaomi.onetrack.util.r.a("EventManager", "添加后，DB 中事件个数为 " + d());
                            }
                            com.xiaomi.onetrack.b.o.a(false);
                        } catch (Throwable th) {
                            z3 = z2;
                            th = th;
                            throw th;
                        }
                    }
                    if (z2) {
                        com.xiaomi.onetrack.h.d.b.a(bVar.c(), bVar.e(), "sa", "su", 1);
                    } else {
                        com.xiaomi.onetrack.h.d.b.a(bVar.c(), bVar.e(), "sa", "r4", 1);
                        a(bVar, "sa", "r4");
                    }
                    return z2;
                } catch (Throwable th2) {
                    th = th2;
                }
            }
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.b("EventManager", "EventManager.addEventToDatabase exception: ", e);
            z2 = z3;
        }
    }

    public boolean a(ac acVar) {
        synchronized (this.k) {
            SQLiteDatabase sQLiteDatabase = null;
            try {
                if (acVar != null) {
                    try {
                        ArrayList arrayList = acVar.a;
                        if (arrayList != null && arrayList.size() != 0) {
                            SQLiteDatabase writableDatabase = this.k.getWritableDatabase();
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
                                    contentValues.put(com.ot.pubsub.g.f.a.m, Integer.valueOf(yVar.g()));
                                    contentValues.put("timestamp", Long.valueOf(yVar.i()));
                                    contentValues.put("data", yVar.f());
                                    com.xiaomi.onetrack.util.r.a("EventManager", "DB-Thread: EventManager.addTransformDataToDB , row=" + writableDatabase.insert(com.ot.pubsub.b.a.c, null, contentValues));
                                }
                                com.xiaomi.onetrack.b.o.a(false);
                                writableDatabase.setTransactionSuccessful();
                                try {
                                    writableDatabase.endTransaction();
                                } catch (Exception e) {
                                    com.xiaomi.onetrack.util.r.b("EventManager", "addTransformDataToDB Exception while endTransaction:" + e);
                                }
                                return true;
                            } catch (Exception e2) {
                                e = e2;
                                sQLiteDatabase = writableDatabase;
                                com.xiaomi.onetrack.util.r.a("EventManager", "DB-Thread: addTransformDataToDB , exception:" + e.getMessage());
                                if (sQLiteDatabase != null) {
                                    try {
                                        sQLiteDatabase.endTransaction();
                                    } catch (Exception e3) {
                                        com.xiaomi.onetrack.util.r.b("EventManager", "addTransformDataToDB Exception while endTransaction:" + e3);
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
                                        com.xiaomi.onetrack.util.r.b("EventManager", "addTransformDataToDB Exception while endTransaction:" + e4);
                                    }
                                }
                                throw th;
                            }
                        }
                    } catch (Exception e5) {
                        e = e5;
                    }
                }
                com.xiaomi.onetrack.util.r.a("EventManager", "TransformEvent记录为空，即将返回");
                return false;
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:87:0x01c2  */
    /* JADX WARN: Not initialized variable reg: 5, insn: 0x0082: MOVE (r3 I:??[OBJECT, ARRAY]) = (r5 I:??[OBJECT, ARRAY]), block:B:13:0x0082 */
    public i a(int i) {
        Cursor cursor;
        i iVar;
        Cursor cursorQuery;
        Cursor cursor2;
        boolean z;
        boolean z2;
        int i2;
        int i3;
        try {
            try {
                cursorQuery = this.k.getReadableDatabase().query(com.ot.pubsub.b.a.c, null, null, null, null, null, "priority ASC, _id ASC", String.valueOf(MiRilHook.HOOK_ID_START_URC_BY_AT));
                try {
                    int columnIndex = cursorQuery.getColumnIndex("_id");
                    int columnIndex2 = cursorQuery.getColumnIndex(com.ot.pubsub.g.f.a.d);
                    cursorQuery.getColumnIndex(com.ot.pubsub.g.f.a.e);
                    int columnIndex3 = cursorQuery.getColumnIndex(com.ot.pubsub.g.f.a.l);
                    int columnIndex4 = cursorQuery.getColumnIndex(com.ot.pubsub.g.f.a.m);
                    int columnIndex5 = cursorQuery.getColumnIndex("data");
                    int columnIndex6 = cursorQuery.getColumnIndex("timestamp");
                    JSONArray jSONArray = new JSONArray();
                    ArrayList arrayList = new ArrayList();
                    ArrayMap arrayMap = new ArrayMap();
                    boolean z3 = true;
                    boolean z4 = true;
                    int i4 = 0;
                    int i5 = 0;
                    while (true) {
                        if (!cursorQuery.moveToNext()) {
                            iVar = null;
                            break;
                        }
                        long j2 = cursorQuery.getLong(columnIndex);
                        cursorQuery.getLong(columnIndex6);
                        iVar = null;
                        try {
                            String string = cursorQuery.getString(columnIndex2);
                            byte[] blob = cursorQuery.getBlob(columnIndex5);
                            String strA = blob != null ? com.xiaomi.onetrack.d.a.a(blob) : null;
                            if (z4) {
                                int i6 = cursorQuery.getInt(columnIndex4);
                                if (i6 > i) {
                                    com.xiaomi.onetrack.util.r.a("EventManager", "No records of priority[" + i + "], first record priority=" + i6);
                                    cursorQuery.close();
                                    return null;
                                }
                                z2 = false;
                            } else {
                                z2 = z4;
                            }
                            JSONObject jSONObject = new JSONObject(strA);
                            try {
                                jSONArray.put(jSONObject);
                                arrayList.add(Long.valueOf(j2));
                                i4++;
                                if (com.xiaomi.onetrack.util.t.b(cursorQuery.getString(columnIndex3))) {
                                    i2 = columnIndex;
                                    i3 = columnIndex2;
                                } else {
                                    i2 = columnIndex;
                                    i3 = columnIndex2;
                                    try {
                                        if (com.xiaomi.onetrack.util.ae.b(jSONObject.optJSONObject(com.ot.pubsub.a.b.b).optLong(com.xiaomi.onetrack.f.b.AbstractC0020b.p, 0L))) {
                                            arrayMap.put(string, Integer.valueOf((arrayMap.get(string) == null ? 0 : ((Integer) arrayMap.get(string)).intValue()) + 1));
                                        }
                                    } catch (Exception e) {
                                        e = e;
                                        com.xiaomi.onetrack.util.r.b("EventManager", "*** error ***", e);
                                    }
                                }
                            } catch (Exception e2) {
                                e = e2;
                                i2 = columnIndex;
                                i3 = columnIndex2;
                            }
                            int length = i5 + strA.length();
                            if (length >= 307200) {
                                com.xiaomi.onetrack.util.r.a("EventManager", "reached max len: " + length);
                                break;
                            }
                            i5 = length;
                            columnIndex = i2;
                            columnIndex2 = i3;
                            z4 = z2;
                        } catch (SQLiteBlobTooBigException e3) {
                            e = e3;
                            com.xiaomi.onetrack.util.r.b("EventManager", "blob too big ***", e);
                            e();
                            if (cursorQuery != null) {
                            }
                            return iVar;
                        } catch (Exception e4) {
                            e = e4;
                            com.xiaomi.onetrack.util.r.a("EventManager", "", e);
                            if (cursorQuery != null) {
                            }
                            return iVar;
                        }
                    }
                    int i7 = i4;
                    if (arrayList.size() > 0) {
                        if (!cursorQuery.isAfterLast()) {
                            if (cursorQuery.getInt(columnIndex4) <= i) {
                                z3 = false;
                            }
                        } else {
                            if (i7 == 300) {
                                com.xiaomi.onetrack.util.r.a("EventManager", "cursor max one query number");
                                z = false;
                            } else {
                                com.xiaomi.onetrack.util.r.a("EventManager", "cursor isAfterLast");
                            }
                            i iVar2 = new i(jSONArray, i7, arrayList, z, arrayMap);
                            cursorQuery.close();
                            return iVar2;
                        }
                        z = z3;
                        i iVar3 = new i(jSONArray, i7, arrayList, z, arrayMap);
                        cursorQuery.close();
                        return iVar3;
                    }
                } catch (SQLiteBlobTooBigException e5) {
                    e = e5;
                    iVar = null;
                } catch (Exception e6) {
                    e = e6;
                    iVar = null;
                }
            } catch (Throwable th) {
                th = th;
                cursor = cursor2;
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        } catch (SQLiteBlobTooBigException e7) {
            e = e7;
            iVar = null;
            cursorQuery = null;
        } catch (Exception e8) {
            e = e8;
            iVar = null;
            cursorQuery = null;
        } catch (Throwable th2) {
            th = th2;
            cursor = null;
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
        cursorQuery.close();
        return iVar;
    }

    /* JADX WARN: Code duplicated, block: B:70:0x0125 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Not initialized variable reg: 4, insn: 0x0096: MOVE (r2 I:??[OBJECT, ARRAY]) = (r4 I:??[OBJECT, ARRAY]), block:B:13:0x0096 */
    public ac b() throws Throwable {
        Cursor cursor;
        ac acVar;
        Cursor cursorQuery;
        Cursor cursor2;
        boolean z;
        byte[] bArr;
        try {
            try {
                cursorQuery = this.k.getReadableDatabase().query(com.ot.pubsub.b.a.c, null, null, null, null, null, "priority ASC, _id ASC", String.valueOf(MiRilHook.HOOK_ID_START_URC_BY_AT));
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
                        long j2 = cursorQuery.getLong(columnIndex);
                        String string = cursorQuery.getString(columnIndex2);
                        String string2 = cursorQuery.getString(columnIndex4);
                        String string3 = cursorQuery.getString(columnIndex3);
                        byte[] blob = cursorQuery.getBlob(columnIndex5);
                        try {
                            bArr = blob;
                            acVar = null;
                            try {
                                arrayList.add(new y(j2, null, null, string, string3, string2, blob, null, cursorQuery.getLong(columnIndex6), cursorQuery.getInt(columnIndex7)));
                                arrayList2.add(Long.valueOf(j2));
                                i++;
                            } catch (Exception e) {
                                e = e;
                                com.xiaomi.onetrack.util.r.b("EventManager", "*** error ***", e);
                            }
                        } catch (Exception e2) {
                            e = e2;
                            bArr = blob;
                            acVar = null;
                        }
                        try {
                            int length = i2 + bArr.length;
                            if (length >= 5242880) {
                                com.xiaomi.onetrack.util.r.a("EventManager", "reached max len: " + length);
                                break;
                            }
                            i2 = length;
                        } catch (SQLiteBlobTooBigException e3) {
                            e = e3;
                            com.xiaomi.onetrack.util.r.b("EventManager", "blob too big ***", e);
                            e();
                            if (cursorQuery != null) {
                            }
                            return acVar;
                        } catch (Exception e4) {
                            e = e4;
                            com.xiaomi.onetrack.util.r.a("EventManager", "", e);
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
                            com.xiaomi.onetrack.util.r.a("EventManager", "cursor max one query number");
                            z = false;
                        } else {
                            com.xiaomi.onetrack.util.r.a("EventManager", "cursor isAfterLast");
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

    public Boolean a(com.xiaomi.onetrack.a.b.a aVar, int i, String str, Throwable th) {
        Cursor cursor;
        synchronized (this.k) {
            try {
                try {
                    try {
                        SQLiteDatabase writableDatabase = this.k.getWritableDatabase();
                        Cursor cursorQuery = writableDatabase.query(com.ot.pubsub.b.a.c, new String[]{"_id", "data"}, "appid = ? AND event_name = ? AND priority = ?", new String[]{"001", "onetrack_exception", String.valueOf(2)}, null, null, null, "50");
                        if (cursorQuery != null) {
                            try {
                                try {
                                    if (cursorQuery.getCount() != 0) {
                                        int columnIndex = cursorQuery.getColumnIndex("_id");
                                        int columnIndex2 = cursorQuery.getColumnIndex("data");
                                        while (cursorQuery.moveToNext()) {
                                            long j2 = cursorQuery.getLong(columnIndex);
                                            byte[] blob = cursorQuery.getBlob(columnIndex2);
                                            String strA = blob != null ? com.xiaomi.onetrack.d.a.a(blob) : null;
                                            if (com.xiaomi.onetrack.util.ab.b(strA)) {
                                                try {
                                                    JSONObject jSONObject = new JSONObject(strA);
                                                    if (aVar.j().equals(jSONObject.optJSONObject(com.ot.pubsub.a.b.b).optString(com.xiaomi.onetrack.f.b.AbstractC0020b.J))) {
                                                        JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject(com.ot.pubsub.a.b.a);
                                                        String strOptString = jSONObjectOptJSONObject.optString("type");
                                                        int iOptInt = jSONObjectOptJSONObject.optInt("url_index");
                                                        int iOptInt2 = jSONObjectOptJSONObject.optInt("err_code");
                                                        String strOptString2 = jSONObjectOptJSONObject.optString("err_body");
                                                        String strOptString3 = jSONObjectOptJSONObject.optString(com.ot.pubsub.a.a.m);
                                                        if ("adm".equals(strOptString)) {
                                                            if (aVar.i() == iOptInt && i == iOptInt2) {
                                                                try {
                                                                    if (!TextUtils.isEmpty(str)) {
                                                                        try {
                                                                            if (str.equals(strOptString2)) {
                                                                            }
                                                                        } catch (Exception e) {
                                                                            e = e;
                                                                            com.xiaomi.onetrack.util.r.a("EventManager", "hasAndUpdateSameAdMExceptionEvent exception:" + e);
                                                                        }
                                                                    }
                                                                    if (th == null || TextUtils.isEmpty(th.getMessage()) || th.getMessage().equals(strOptString3)) {
                                                                        jSONObjectOptJSONObject.put(com.ot.pubsub.a.a.M, jSONObjectOptJSONObject.optInt(com.ot.pubsub.a.a.M, 0) + 1);
                                                                        byte[] bArrB = com.xiaomi.onetrack.d.a.b(jSONObject.toString());
                                                                        ContentValues contentValues = new ContentValues();
                                                                        contentValues.put("data", bArrB);
                                                                        int iUpdate = writableDatabase.update(com.ot.pubsub.b.a.c, contentValues, "_id = ?", new String[]{String.valueOf(j2)});
                                                                        if (iUpdate > 0) {
                                                                            com.xiaomi.onetrack.util.r.a("EventManager", "updata same adMException rowsupdated:" + iUpdate);
                                                                            Boolean bool = Boolean.TRUE;
                                                                            com.xiaomi.onetrack.util.o.b(cursorQuery);
                                                                            return bool;
                                                                        }
                                                                        continue;
                                                                    }
                                                                } catch (Exception e2) {
                                                                    e = e2;
                                                                    com.xiaomi.onetrack.util.r.a("EventManager", "hasAndUpdateSameAdMExceptionEvent exception:" + e);
                                                                }
                                                            }
                                                            com.xiaomi.onetrack.util.r.a("EventManager", "item exception no same");
                                                        } else {
                                                            com.xiaomi.onetrack.util.r.a("EventManager", "no admException");
                                                        }
                                                    }
                                                } catch (Exception e3) {
                                                    e = e3;
                                                }
                                            }
                                        }
                                        com.xiaomi.onetrack.util.o.b(cursorQuery);
                                        return Boolean.FALSE;
                                    }
                                } catch (Throwable th2) {
                                    th = th2;
                                    cursor = cursorQuery;
                                    com.xiaomi.onetrack.util.o.b(cursor);
                                    throw th;
                                }
                            } catch (SQLiteBlobTooBigException e4) {
                                e = e4;
                                cursor = cursorQuery;
                                com.xiaomi.onetrack.util.r.b("EventManager", "blob too big ***", e);
                                e();
                                com.xiaomi.onetrack.util.o.b(cursor);
                            } catch (Exception e5) {
                                e = e5;
                                cursor = cursorQuery;
                                com.xiaomi.onetrack.util.r.b("EventManager", "getAppIdSingleEventData error, e: ", e);
                                com.xiaomi.onetrack.util.o.b(cursor);
                            }
                        }
                        Boolean bool2 = Boolean.FALSE;
                        com.xiaomi.onetrack.util.o.b(cursorQuery);
                        return bool2;
                    } catch (Throwable th3) {
                        th = th3;
                    }
                } catch (SQLiteBlobTooBigException e6) {
                    e = e6;
                    cursor = null;
                } catch (Exception e7) {
                    e = e7;
                    cursor = null;
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
        synchronized (this.k) {
            if (arrayList != null) {
                try {
                    if (arrayList.size() != 0) {
                        try {
                            SQLiteDatabase writableDatabase = this.k.getWritableDatabase();
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
                            int iDelete = writableDatabase.delete(com.ot.pubsub.b.a.c, sb.toString(), null);
                            com.xiaomi.onetrack.util.r.a("EventManager", "deleted events count " + iDelete);
                            long jD = a().d();
                            if (jD != 0) {
                                z = false;
                            }
                            com.xiaomi.onetrack.b.o.a(z);
                            com.xiaomi.onetrack.util.r.a("EventManager", "after delete DB record remains=" + jD);
                            return iDelete;
                        } catch (Exception e) {
                            com.xiaomi.onetrack.util.r.b("EventManager", "e=" + e);
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
        c.a(new h(this), 1000L);
    }

    public long d() {
        try {
            return DatabaseUtils.queryNumEntries(this.k.getReadableDatabase(), com.ot.pubsub.b.a.c);
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.b("EventManager", "getTotalEventsNumberSync failed with " + e.getMessage());
            return 0L;
        }
    }

    private void e() {
        try {
            this.k.getWritableDatabase().delete(com.ot.pubsub.b.a.c, null, null);
            com.xiaomi.onetrack.util.r.a("EventManager", "delete table events");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void a(com.xiaomi.onetrack.f.b bVar, String str, String str2) {
        if (!com.xiaomi.onetrack.h.a.a.a().b(bVar.c(), bVar.e())) {
            com.xiaomi.onetrack.util.r.a("EventManager", "trackFailedEvent, should not record");
            return;
        }
        try {
            a(am.a(bVar, str, str2), false);
            com.xiaomi.onetrack.h.a.a.a().c(bVar.c(), bVar.e());
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.b("EventManager", "trackFailedEvent error", e);
        }
    }

    class a extends SQLiteOpenHelper {
        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        }

        public a(Context context) {
            super(context, "onetrack", (SQLiteDatabase.CursorFactory) null, 1);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL("CREATE TABLE events (_id INTEGER PRIMARY KEY AUTOINCREMENT,appid TEXT,package TEXT,event_name TEXT,priority INTEGER,data BLOB,timestamp INTEGER)");
        }
    }
}
