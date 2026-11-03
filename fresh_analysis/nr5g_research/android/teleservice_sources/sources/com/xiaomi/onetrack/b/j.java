package com.xiaomi.onetrack.b;

import android.content.ContentValues;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import com.xiaomi.onetrack.util.r;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class j {
    private i c;
    private ConcurrentHashMap d;
    private ConcurrentHashMap e;

    /* synthetic */ j(k kVar) {
        this();
    }

    private j() {
        this.d = new ConcurrentHashMap();
        this.e = new ConcurrentHashMap();
        this.c = new i(com.xiaomi.onetrack.f.a.a());
    }

    abstract class a {
        private static final j a = new j(null);
    }

    public static j a() {
        return a.a;
    }

    public void a(ArrayList arrayList) {
        com.xiaomi.onetrack.c.c.a(new k(this, arrayList));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(ArrayList arrayList) throws Throwable {
        StringBuilder sb;
        SQLiteDatabase sQLiteDatabase = null;
        try {
            try {
                SQLiteDatabase writableDatabase = this.c.getWritableDatabase();
                try {
                    writableDatabase.beginTransaction();
                    int size = arrayList.size();
                    int i = 0;
                    while (i < size) {
                        Object obj = arrayList.get(i);
                        i++;
                        m mVar = (m) obj;
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(com.ot.pubsub.b.g.d, mVar.a);
                        contentValues.put("timestamp", Long.valueOf(mVar.c));
                        JSONObject jSONObject = mVar.e;
                        if (jSONObject != null) {
                            contentValues.put(com.ot.pubsub.b.g.e, jSONObject.toString());
                        }
                        String str = mVar.d;
                        if (str != null) {
                            contentValues.put(com.ot.pubsub.b.g.f, str);
                        }
                        if (DatabaseUtils.queryNumEntries(writableDatabase, com.ot.pubsub.b.g.b, "app_id=?", new String[]{mVar.a}) > 0) {
                            r.a("ConfigDbManager", "database updated, row: " + writableDatabase.update(com.ot.pubsub.b.g.b, contentValues, "app_id=?", new String[]{mVar.a}));
                        } else {
                            r.a("ConfigDbManager", "database inserted, row: " + writableDatabase.insert(com.ot.pubsub.b.g.b, null, contentValues));
                        }
                        this.e.put(mVar.a, Boolean.TRUE);
                    }
                    writableDatabase.setTransactionSuccessful();
                    try {
                        writableDatabase.endTransaction();
                    } catch (Exception e) {
                        e = e;
                        sb = new StringBuilder("Exception while endTransaction:");
                        sb.append(e);
                        r.b("ConfigDbManager", sb.toString());
                    }
                } catch (Exception e2) {
                    e = e2;
                    sQLiteDatabase = writableDatabase;
                    r.b("ConfigDbManager", "updateToDb error: ", e);
                    if (sQLiteDatabase != null) {
                        try {
                            sQLiteDatabase.endTransaction();
                        } catch (Exception e3) {
                            e = e3;
                            sb = new StringBuilder("Exception while endTransaction:");
                            sb.append(e);
                            r.b("ConfigDbManager", sb.toString());
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    sQLiteDatabase = writableDatabase;
                    if (sQLiteDatabase != null) {
                        try {
                            sQLiteDatabase.endTransaction();
                        } catch (Exception e4) {
                            r.b("ConfigDbManager", "Exception while endTransaction:" + e4);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e5) {
            e = e5;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(ArrayList arrayList) {
        JSONObject jSONObjectOptJSONObject;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            m mVar = (m) obj;
            JSONObject jSONObject = mVar.e;
            if (jSONObject != null && (jSONObjectOptJSONObject = jSONObject.optJSONObject("quality")) != null) {
                com.xiaomi.onetrack.h.a.a.a().a(mVar.a, jSONObjectOptJSONObject);
            }
        }
    }

    public synchronized void a(String str) {
        if (!this.e.containsKey(str) || ((Boolean) this.e.get(str)).booleanValue() || this.d.get(str) == null) {
            FutureTask futureTask = new FutureTask(new l(this, str));
            com.xiaomi.onetrack.c.c.a(futureTask);
            try {
                m mVar = (m) futureTask.get(5L, TimeUnit.SECONDS);
                if (mVar != null) {
                    this.d.put(str, mVar);
                    this.e.put(str, Boolean.FALSE);
                }
            } catch (Exception e) {
                r.b("ConfigDbManager", "getConfig error: " + e.toString());
            }
        }
    }

    public int a(String str, String str2, String str3, int i) {
        try {
            JSONObject jSONObjectA = a(str, str2);
            if (jSONObjectA == null) {
                r.a("ConfigDbManager", "config not available, use default value");
                return i;
            }
            return jSONObjectA.getInt(str3);
        } catch (Exception e) {
            r.b("ConfigDbManager", "getInt: " + e.toString());
            return i;
        }
    }

    public JSONObject a(String str, String str2) {
        JSONObject jSONObject;
        JSONArray jSONArrayOptJSONArray;
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            try {
                a(str);
                m mVar = (m) this.d.get(str);
                if (mVar != null && (jSONObject = mVar.e) != null && (jSONArrayOptJSONArray = jSONObject.optJSONArray(com.ot.pubsub.b.a.c)) != null) {
                    for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                        JSONObject jSONObject2 = jSONArrayOptJSONArray.getJSONObject(i);
                        if (TextUtils.equals(str2, jSONObject2.optString(com.ot.pubsub.b.a.b))) {
                            if (!r.a) {
                                return jSONObject2;
                            }
                            r.a("ConfigDbManager", "getEventConfig:" + jSONObject2.toString());
                            return jSONObject2;
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("ConfigDbManager", "getEventConfig error: " + e.toString());
            }
        }
        return null;
    }

    public String b(String str) {
        JSONObject jSONObject;
        m mVarF = f(str);
        if (mVarF != null && (jSONObject = mVarF.e) != null) {
            return jSONObject.optString(com.ot.pubsub.b.a.g);
        }
        return "";
    }

    public String c(String str) {
        m mVarF = f(str);
        if (mVarF != null) {
            return mVarF.d;
        }
        return "";
    }

    public int d(String str) {
        JSONObject jSONObject;
        m mVarF = f(str);
        if (mVarF == null || (jSONObject = mVarF.e) == null) {
            return 0;
        }
        return jSONObject.optInt("version");
    }

    public int e(String str) {
        JSONObject jSONObject;
        try {
            m mVarF = f(str);
            if (mVarF != null && (jSONObject = mVarF.e) != null && jSONObject.has("nvc")) {
                return mVarF.e.optInt("nvc", 0);
            }
        } catch (Exception e) {
            r.b("ConfigDbManager", "getNewVersionConfig error: " + e.getMessage());
        }
        return 0;
    }

    public boolean b(String str, String str2) {
        JSONObject jSONObject;
        try {
            m mVarF = f(str);
            if (mVarF == null || (jSONObject = mVarF.e) == null || !jSONObject.has(str2)) {
                return false;
            }
            return mVarF.e.optBoolean(str2);
        } catch (Exception e) {
            r.b("ConfigDbManager", "getAppLevelBoolean" + e.toString());
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int b(JSONObject jSONObject) {
        try {
            int iOptInt = jSONObject.optInt(com.ot.pubsub.b.a.e, 100);
            if (iOptInt < 0 || iOptInt > 100) {
                return 100;
            }
            return iOptInt;
        } catch (Exception e) {
            r.a("ConfigDbManager", "getCommonSample Exception:" + e.getMessage());
            return 100;
        }
    }

    public long a(JSONObject jSONObject, String str, String str2) {
        int iOptInt;
        int iOptInt2;
        m mVar;
        if (TextUtils.isEmpty(str)) {
            return 100L;
        }
        try {
            a(str);
            if (this.d.get(str) != null) {
                if (jSONObject != null) {
                    iOptInt2 = jSONObject.has("h_sample") ? jSONObject.optInt("h_sample", -1) : -1;
                    iOptInt = jSONObject.has(com.ot.pubsub.b.a.e) ? jSONObject.optInt(com.ot.pubsub.b.a.e, -1) : -1;
                } else {
                    iOptInt = -1;
                    iOptInt2 = -1;
                }
                if (iOptInt2 != -1) {
                    iOptInt = iOptInt2;
                } else if (iOptInt == -1) {
                    iOptInt = -1;
                }
                if (iOptInt == -1 && (mVar = (m) this.d.get(str)) != null) {
                    r.a("ConfigDbManager", "will return common sample " + mVar.b);
                    return mVar.b;
                }
                r.a("ConfigDbManager", "will return event sample " + iOptInt);
                return iOptInt;
            }
        } catch (Exception e) {
            r.b("ConfigDbManager", "getAppEventSample" + e.toString());
        }
        r.a("ConfigDbManager", "will return def sample");
        return 100L;
    }

    public m f(String str) {
        r.a("ConfigDbManager", "getAppConfigData start, appId: " + str);
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            a(str);
        } catch (Exception e) {
            r.b("ConfigDbManager", "getConfig error: " + e.getMessage());
        }
        return (m) this.d.get(str);
    }

    public JSONObject g(String str) {
        JSONObject jSONObject;
        JSONObject jSONObject2 = new JSONObject();
        m mVarF = f(str);
        return (mVarF == null || (jSONObject = mVarF.e) == null) ? jSONObject2 : jSONObject.optJSONObject("quality");
    }
}
