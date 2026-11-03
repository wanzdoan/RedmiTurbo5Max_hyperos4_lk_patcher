package com.android.internal.telephony.analytics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.android.telephony.Rlog;
import java.util.Calendar;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyAnalyticsUtil extends SQLiteOpenHelper {
    private static final String TAG = TelephonyAnalyticsUtil.class.getSimpleName();
    private static TelephonyAnalyticsUtil sTelephonyAnalyticsUtil;

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    private TelephonyAnalyticsUtil(Context context) {
        super(context, "telephony_analytics.db", (SQLiteDatabase.CursorFactory) null, 10);
    }

    public static synchronized TelephonyAnalyticsUtil getInstance(Context context) {
        try {
            if (sTelephonyAnalyticsUtil == null) {
                sTelephonyAnalyticsUtil = new TelephonyAnalyticsUtil(context);
            }
        } catch (Throwable th) {
            throw th;
        }
        return sTelephonyAnalyticsUtil;
    }

    public synchronized void createTable(String str) {
        try {
            getWritableDatabase().execSQL(str);
        } catch (Exception e) {
            Rlog.e(TAG, "Error during table creation : " + e);
        }
    }

    public synchronized void insert(String str, ContentValues contentValues) {
        try {
            getWritableDatabase().insert(str, null, contentValues);
        } catch (SQLException unused) {
            Rlog.e(TAG, "error occurred during insertion");
        }
    }

    public synchronized int update(String str, ContentValues contentValues, String str2, String[] strArr) {
        int iUpdate;
        try {
            iUpdate = getWritableDatabase().update(str, contentValues, str2, strArr);
        } catch (SQLException unused) {
            Rlog.e(TAG, "Error during update.");
            iUpdate = -1;
        }
        return iUpdate;
    }

    public synchronized Cursor getCursor(String str, String[] strArr, String str2, String[] strArr2, String str3, String str4, String str5, String str6) {
        Cursor cursorQuery;
        try {
            cursorQuery = getReadableDatabase().query(str, strArr, str2, strArr2, str3, str4, str5, str6);
        } catch (SQLException e) {
            Rlog.e(TAG, "Error during querying for getCursor()" + e);
            cursorQuery = null;
        }
        return cursorQuery;
    }

    /* JADX WARN: Code duplicated, block: B:10:0x0013  */
    public synchronized long getCountFromCursor(Cursor cursor) {
        long j;
        if (cursor == null) {
            j = 0;
        } else if (cursor.moveToFirst()) {
            j = cursor.getInt(0);
        } else {
            j = 0;
        }
        return j;
    }

    public void deleteOverflowAndOldData(String str, String str2, String str3) {
        deleteOverFlowData(str, str2);
        deleteOldData(str, str3);
    }

    protected void deleteOverFlowData(String str, String str2) {
        delete(str, str2, new String[]{Integer.toString(1000)});
    }

    protected void deleteOldData(String str, String str2) {
        delete(str, str2, new String[]{getCutoffDate()});
    }

    public synchronized void delete(String str, String str2, String[] strArr) {
        try {
            getWritableDatabase().delete(str, str2, strArr);
        } catch (SQLException e) {
            Rlog.e(TAG, "Sqlite Operation Error during deletion of Overflow data " + e);
        }
    }

    private String getCutoffDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(2, -2);
        return TelephonyAnalyticsDatabase.DATE_FORMAT.format(calendar.toInstant());
    }
}
