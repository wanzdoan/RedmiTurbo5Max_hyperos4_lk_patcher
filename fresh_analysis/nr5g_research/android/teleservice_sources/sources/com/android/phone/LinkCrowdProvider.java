package com.android.phone;

import ai.onnxruntime.OrtProvider$$ExternalSyntheticBUOutline0;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public class LinkCrowdProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher;
    private SQLiteOpenHelper mDbHelper;

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        return 0;
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return 0;
    }

    static {
        UriMatcher uriMatcher = new UriMatcher(-1);
        sUriMatcher = uriMatcher;
        uriMatcher.addURI("com.android.phone.link_crowd", "data", 1);
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        this.mDbHelper = new SQLiteOpenHelper(this, getContext(), "link_crowd.db", null, 1) { // from class: com.android.phone.LinkCrowdProvider.1
            @Override // android.database.sqlite.SQLiteOpenHelper
            public void onCreate(SQLiteDatabase sQLiteDatabase) {
                sQLiteDatabase.execSQL("CREATE TABLE link_crowd (_id INTEGER PRIMARY KEY,loc_precision REAL,grid_x REAL,grid_y REAL,velocity REAL,sample_ts INTEGER,source_type TEXT,zone TEXT,update_ts INTEGER)");
            }

            @Override // android.database.sqlite.SQLiteOpenHelper
            public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
                sQLiteDatabase.execSQL("DROP TABLE IF EXISTS link_crowd");
                onCreate(sQLiteDatabase);
            }
        };
        return true;
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        if (contentValues == null) {
            Folme$$ExternalSyntheticBUOutline0.m("Content values cannot be null");
            return null;
        }
        if (sUriMatcher.match(uri) != 1) {
            OrtProvider$$ExternalSyntheticBUOutline0.m("Unknown URI: ", uri);
            return null;
        }
        contentValues.put("_id", (Integer) 1);
        contentValues.put("update_ts", Long.valueOf(System.currentTimeMillis()));
        this.mDbHelper.getWritableDatabase().insertWithOnConflict("link_crowd", null, contentValues, 5);
        ContentResolver contentResolver = getContext().getContentResolver();
        Uri uri2 = LinkCrowdContract.CONTENT_URI;
        contentResolver.notifyChange(uri2, null);
        return uri2;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        if (sUriMatcher.match(uri) != 1) {
            OrtProvider$$ExternalSyntheticBUOutline0.m("Unknown URI: ", uri);
            return null;
        }
        Cursor cursorQuery = this.mDbHelper.getReadableDatabase().query("link_crowd", strArr, str, strArr2, null, null, str2);
        cursorQuery.setNotificationUri(getContext().getContentResolver(), uri);
        return cursorQuery;
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        return "vnd.android.cursor.item/link_crowd";
    }
}
