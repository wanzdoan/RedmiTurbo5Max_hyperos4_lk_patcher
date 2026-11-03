package com.xiaomi.onetrack.h.b;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* JADX INFO: loaded from: classes2.dex */
public class a extends SQLiteOpenHelper {
    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public a(Context context) {
        super(context, "universal_ot_monitor_db", (SQLiteDatabase.CursorFactory) null, 1);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        if (sQLiteDatabase == null) {
            return;
        }
        a(sQLiteDatabase);
    }

    private void a(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE app_stats (_id INTEGER PRIMARY KEY AUTOINCREMENT,app_id TEXT,date INTEGER,stage TEXT,reason TEXT,count INTEGER)");
        sQLiteDatabase.execSQL("CREATE TABLE event_stats (_id INTEGER PRIMARY KEY AUTOINCREMENT,app_id TEXT,date INTEGER,event TEXT,count INTEGER)");
    }
}
