package com.android.phone.performance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class WifiToDataSelfStudyDBHelper extends SQLiteOpenHelper {
    private static long ONE_WEEK_MILLISECOND = 604800000;
    private static WifiToDataSelfStudyDBHelper sInstance;
    private SQLiteDatabase mDb;

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    private WifiToDataSelfStudyDBHelper(Context context, int i) {
        super(context, "wifi_to_data.db", (SQLiteDatabase.CursorFactory) null, i);
        this.mDb = null;
    }

    public static synchronized WifiToDataSelfStudyDBHelper getInstance(Context context) {
        try {
            if (sInstance == null) {
                sInstance = new WifiToDataSelfStudyDBHelper(context, 1);
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    private void openWriteLink() {
        SQLiteDatabase sQLiteDatabase = this.mDb;
        if (sQLiteDatabase == null || !sQLiteDatabase.isOpen()) {
            this.mDb = sInstance.getWritableDatabase();
        }
    }

    private void closeLink() {
        SQLiteDatabase sQLiteDatabase = this.mDb;
        if (sQLiteDatabase == null || !sQLiteDatabase.isOpen()) {
            return;
        }
        this.mDb.close();
        this.mDb = null;
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        initDB(sQLiteDatabase);
    }

    private void initDB(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table wifi_ssid_info(_id integer primary key autoincrement, wifi_ssid varchar(32), update_time_mills integer default 0)");
    }

    public boolean queryRecord(String str) {
        openWriteLink();
        deleteRecordOutOfDate();
        boolean z = false;
        try {
            Cursor cursorQuery = this.mDb.query("wifi_ssid_info", new String[]{"wifi_ssid", "update_time_mills"}, "wifi_ssid = ?", new String[]{str}, null, null, null);
            z = cursorQuery.getCount() == 1;
            log("query Record " + z);
            cursorQuery.close();
        } catch (Exception e) {
            log("Exception: " + e);
        }
        closeLink();
        return z;
    }

    public void insertOrUpdateRecord(String str, long j) {
        openWriteLink();
        deleteRecordOutOfDate();
        try {
            String[] strArr = {str};
            Cursor cursorQuery = this.mDb.query("wifi_ssid_info", new String[]{"wifi_ssid", "update_time_mills"}, "wifi_ssid = ?", strArr, null, null, null);
            if (cursorQuery.moveToFirst()) {
                if (!cursorQuery.isAfterLast()) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("update_time_mills", Long.valueOf(j));
                    this.mDb.update("wifi_ssid_info", contentValues, "wifi_ssid = ?", strArr);
                    log("update Record");
                }
            } else {
                ContentValues contentValues2 = new ContentValues();
                contentValues2.put("wifi_ssid", str);
                contentValues2.put("update_time_mills", Long.valueOf(j));
                this.mDb.insert("wifi_ssid_info", null, contentValues2);
                log("insert Record");
            }
            cursorQuery.close();
        } catch (Exception e) {
            log("Exception: " + e);
        }
        closeLink();
    }

    private void deleteRecordOutOfDate() {
        try {
            this.mDb.delete("wifi_ssid_info", "update_time_mills < ? ", new String[]{String.valueOf(System.currentTimeMillis() - ONE_WEEK_MILLISECOND)});
            log("delete Record Out Of Date");
        } catch (Exception e) {
            log("Exception: " + e);
        }
    }

    private void log(String str) {
        Log.d("WifiToDataSelfStudyDBHelper", str);
    }
}
