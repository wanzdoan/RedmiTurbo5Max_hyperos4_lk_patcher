package com.android.phone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import miui.telephony.qms.DubiousCellInfoS;
import miui.telephony.qms.LteDubiousCellInfoS;
import miui.telephony.qms.NrDubiousCellInfoS;
import miui.telephony.qms.PlmnIdS;

/* JADX INFO: loaded from: classes.dex */
public class CellPingPongDBHelper extends SQLiteOpenHelper {
    private static final String[] SQL_QUERY_ORDER_LTE = {"wifi_ssid", "plmn", "mcc", "mnc", "mnc_include_pcs_digit", "cgi", "activate_band", "arfcn", "pci"};
    private static final String[] SQL_QUERY_ORDER_NR = {"wifi_ssid", "plmn", "mcc", "mnc", "mnc_include_pcs_digit", "cgi", "activate_band", "arfcn", "pci", "nr5g_scs_type"};
    private static CellPingPongDBHelper sInstance = null;
    private SQLiteDatabase mDb;

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    private CellPingPongDBHelper(Context context, int i) {
        super(context, "cell_ping_pong.db", (SQLiteDatabase.CursorFactory) null, i);
        this.mDb = null;
    }

    public static synchronized CellPingPongDBHelper getInstance(Context context) {
        try {
            if (sInstance == null) {
                sInstance = new CellPingPongDBHelper(context, 1);
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
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS lte_cell_info (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, wifi_ssid VARCHAR(200) NOT NULL, plmn INTEGER NOT NULL, mcc INTEGER NOT NULL, mnc INTEGER NOT NULL, mnc_include_pcs_digit INTEGER NOT NULL, cgi INTEGER NOT NULL, activate_band INTEGER NOT NULL, arfcn INTEGER NOT NULL, pci INTEGER NOT NULL )");
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS nr_cell_info (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, wifi_ssid VARCHAR(32) NOT NULL, plmn INTEGER NOT NULL, mcc INTEGER NOT NULL, mnc INTEGER NOT NULL, mnc_include_pcs_digit INTEGER NOT NULL, cgi INTEGER NOT NULL, activate_band INTEGER NOT NULL, arfcn INTEGER NOT NULL, pci INTEGER NOT NULL, nr5g_scs_type INTEGER NOT NULL )");
    }

    public synchronized boolean isHasLteCellRecord(long j) {
        Cursor cursorQuery;
        boolean z;
        String[] strArr = {String.valueOf(j)};
        openWriteLink();
        try {
            cursorQuery = this.mDb.query("lte_cell_info", SQL_QUERY_ORDER_LTE, "cgi = ?", strArr, null, null, null);
        } catch (Exception e) {
            log("Exception: " + e);
            cursorQuery = null;
        }
        z = false;
        if (cursorQuery != null) {
            z = cursorQuery.getCount() != 0;
            cursorQuery.close();
        }
        closeLink();
        return z;
    }

    public synchronized boolean isHasNrCellRecord(long j) {
        Cursor cursorQuery;
        boolean z;
        String[] strArr = {String.valueOf(j)};
        openWriteLink();
        try {
            cursorQuery = this.mDb.query("nr_cell_info", SQL_QUERY_ORDER_NR, "cgi = ?", strArr, null, null, null);
        } catch (Exception e) {
            log("Exception: " + e);
            cursorQuery = null;
        }
        z = false;
        if (cursorQuery != null) {
            z = cursorQuery.getCount() != 0;
            cursorQuery.close();
        }
        closeLink();
        return z;
    }

    public synchronized ArrayList getLteDubiousCellInfoSList(String str, String str2) {
        ArrayList arrayList;
        Cursor cursorQuery;
        openWriteLink();
        String[] strArr = {str, str2};
        arrayList = new ArrayList();
        try {
            cursorQuery = this.mDb.query("lte_cell_info", SQL_QUERY_ORDER_LTE, "wifi_ssid = ? and plmn = ?", strArr, null, null, null);
        } catch (Exception e) {
            log("Exception: " + e);
            cursorQuery = null;
        }
        if (cursorQuery != null) {
            while (cursorQuery.moveToNext()) {
                arrayList.add(new CellInfo(cursorQuery, false).getLteDubiousCellInfoS());
            }
            cursorQuery.close();
        }
        closeLink();
        return arrayList;
    }

    public synchronized LteDubiousCellInfoS getLteDubiousCellInfoS(long j) {
        LteDubiousCellInfoS lteDubiousCellInfoS;
        Cursor cursorQuery;
        openWriteLink();
        String[] strArr = {String.valueOf(j)};
        lteDubiousCellInfoS = null;
        try {
            cursorQuery = this.mDb.query("lte_cell_info", SQL_QUERY_ORDER_LTE, "cgi = ?", strArr, null, null, null);
        } catch (Exception e) {
            log("Exception: " + e);
            cursorQuery = null;
        }
        if (cursorQuery != null) {
            lteDubiousCellInfoS = cursorQuery.moveToNext() ? new CellInfo(cursorQuery, false).getLteDubiousCellInfoS() : null;
            cursorQuery.close();
        }
        closeLink();
        return lteDubiousCellInfoS;
    }

    public synchronized ArrayList getNrDubiousCellInfoSList(String str, String str2) {
        ArrayList arrayList;
        Cursor cursorQuery;
        openWriteLink();
        String[] strArr = {str, str2};
        arrayList = new ArrayList();
        try {
            cursorQuery = this.mDb.query("nr_cell_info", SQL_QUERY_ORDER_NR, "wifi_ssid = ? and plmn = ?", strArr, null, null, null);
        } catch (Exception e) {
            log("Exception: " + e);
            cursorQuery = null;
        }
        if (cursorQuery != null) {
            while (cursorQuery.moveToNext()) {
                arrayList.add(new CellInfo(cursorQuery, true).getNrDubiousCellInfoS());
            }
            cursorQuery.close();
        }
        closeLink();
        return arrayList;
    }

    public synchronized NrDubiousCellInfoS getNrDubiousCellInfoS(long j) {
        NrDubiousCellInfoS nrDubiousCellInfoS;
        Cursor cursorQuery;
        openWriteLink();
        String[] strArr = {String.valueOf(j)};
        nrDubiousCellInfoS = null;
        try {
            cursorQuery = this.mDb.query("nr_cell_info", SQL_QUERY_ORDER_NR, "cgi = ?", strArr, null, null, null);
        } catch (Exception e) {
            log("Exception: " + e);
            cursorQuery = null;
        }
        if (cursorQuery != null) {
            nrDubiousCellInfoS = cursorQuery.moveToNext() ? new CellInfo(cursorQuery, true).getNrDubiousCellInfoS() : null;
            cursorQuery.close();
        }
        closeLink();
        return nrDubiousCellInfoS;
    }

    public synchronized int getLteDubiousCellCount(String str, String str2) {
        Cursor cursorQuery;
        int count;
        openWriteLink();
        try {
            cursorQuery = this.mDb.query("lte_cell_info", SQL_QUERY_ORDER_LTE, "wifi_ssid = ? and plmn = ?", new String[]{str, str2}, null, null, null);
        } catch (Exception e) {
            log("Exception: " + e);
            cursorQuery = null;
        }
        if (cursorQuery != null) {
            count = cursorQuery.getCount();
            cursorQuery.close();
        } else {
            count = 0;
        }
        closeLink();
        return count;
    }

    public synchronized int getNrDubiousCellCount(String str, String str2) {
        Cursor cursorQuery;
        int count;
        openWriteLink();
        try {
            cursorQuery = this.mDb.query("nr_cell_info", SQL_QUERY_ORDER_NR, "wifi_ssid = ? and plmn = ?", new String[]{str, str2}, null, null, null);
        } catch (Exception e) {
            log("Exception: " + e);
            cursorQuery = null;
        }
        if (cursorQuery != null) {
            count = cursorQuery.getCount();
            cursorQuery.close();
        } else {
            count = 0;
        }
        closeLink();
        return count;
    }

    public synchronized void insertRecord(CellInfo cellInfo, boolean z) {
        if (cellInfo == null) {
            return;
        }
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("wifi_ssid", cellInfo.mWifiSsid);
            contentValues.put("plmn", cellInfo.mPlmn);
            contentValues.put("mcc", Short.valueOf(cellInfo.mMcc));
            contentValues.put("mnc", Short.valueOf(cellInfo.mMnc));
            contentValues.put("mnc_include_pcs_digit", Boolean.valueOf(cellInfo.mMncIncludesPcsDigit));
            contentValues.put("cgi", Long.valueOf(cellInfo.mCgi));
            contentValues.put("activate_band", Integer.valueOf(cellInfo.mActiveBand));
            contentValues.put("arfcn", Integer.valueOf(cellInfo.mArfcn));
            contentValues.put("pci", Short.valueOf(cellInfo.mPci));
            openWriteLink();
            try {
                if (z) {
                    contentValues.put("nr5g_scs_type", Integer.valueOf(cellInfo.mNr5gScsTypeE));
                    this.mDb.insert("nr_cell_info", null, contentValues);
                } else {
                    this.mDb.insert("lte_cell_info", null, contentValues);
                }
            } catch (Exception e) {
                log("Exception: " + e);
            }
            closeLink();
            log("insert record");
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void deleteRecord(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        String[] strArr = {str};
        openWriteLink();
        try {
            this.mDb.delete("lte_cell_info", "wifi_ssid = ?", strArr);
            this.mDb.delete("nr_cell_info", "wifi_ssid = ?", strArr);
        } catch (Exception e) {
            log("Exception: " + e);
        }
        closeLink();
        log("delete Record, wifiSsid=" + str);
    }

    public synchronized void clearAllRecord() {
        openWriteLink();
        try {
            this.mDb.execSQL("DROP TABLE IF EXISTS lte_cell_info");
            this.mDb.execSQL("DROP TABLE IF EXISTS nr_cell_info");
            this.mDb.execSQL("CREATE TABLE IF NOT EXISTS lte_cell_info (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, wifi_ssid VARCHAR(200) NOT NULL, plmn INTEGER NOT NULL, mcc INTEGER NOT NULL, mnc INTEGER NOT NULL, mnc_include_pcs_digit INTEGER NOT NULL, cgi INTEGER NOT NULL, activate_band INTEGER NOT NULL, arfcn INTEGER NOT NULL, pci INTEGER NOT NULL )");
            this.mDb.execSQL("CREATE TABLE IF NOT EXISTS nr_cell_info (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, wifi_ssid VARCHAR(32) NOT NULL, plmn INTEGER NOT NULL, mcc INTEGER NOT NULL, mnc INTEGER NOT NULL, mnc_include_pcs_digit INTEGER NOT NULL, cgi INTEGER NOT NULL, activate_band INTEGER NOT NULL, arfcn INTEGER NOT NULL, pci INTEGER NOT NULL, nr5g_scs_type INTEGER NOT NULL )");
        } catch (Exception e) {
            log("Exception: " + e);
        }
        closeLink();
    }

    public class CellInfo {
        private int mActiveBand;
        private int mArfcn;
        private long mCgi;
        private short mMcc;
        private short mMnc;
        private boolean mMncIncludesPcsDigit;
        private int mNr5gScsTypeE;
        private short mPci;
        private String mPlmn;
        private String mWifiSsid;

        CellInfo(String str, String str2, short s, short s2, boolean z, long j, int i, int i2, short s3) {
            this.mWifiSsid = str;
            this.mPlmn = str2;
            this.mMcc = s;
            this.mMnc = s2;
            this.mMncIncludesPcsDigit = z;
            this.mCgi = j;
            this.mActiveBand = i;
            this.mArfcn = i2;
            this.mPci = s3;
        }

        CellInfo(String str, String str2, short s, short s2, boolean z, long j, int i, int i2, short s3, int i3) {
            this.mWifiSsid = str;
            this.mPlmn = str2;
            this.mMcc = s;
            this.mMnc = s2;
            this.mMncIncludesPcsDigit = z;
            this.mCgi = j;
            this.mActiveBand = i;
            this.mArfcn = i2;
            this.mPci = s3;
            this.mNr5gScsTypeE = i3;
        }

        CellInfo(Cursor cursor, boolean z) {
            this.mWifiSsid = cursor.getString(0);
            this.mPlmn = cursor.getString(1);
            this.mMcc = cursor.getShort(2);
            this.mMnc = cursor.getShort(3);
            this.mMncIncludesPcsDigit = cursor.getInt(4) == 1;
            this.mCgi = cursor.getLong(5);
            this.mActiveBand = cursor.getInt(6);
            this.mArfcn = cursor.getInt(7);
            this.mPci = cursor.getShort(8);
            if (z) {
                this.mNr5gScsTypeE = cursor.getInt(9);
            }
        }

        public LteDubiousCellInfoS getLteDubiousCellInfoS() {
            PlmnIdS plmnIdS = new PlmnIdS();
            plmnIdS.mcc = this.mMcc;
            plmnIdS.mnc = this.mMnc;
            plmnIdS.mncIncludesPcsDigit = this.mMncIncludesPcsDigit;
            DubiousCellInfoS dubiousCellInfoS = new DubiousCellInfoS();
            dubiousCellInfoS.plmnId = plmnIdS;
            dubiousCellInfoS.activeBand = this.mActiveBand;
            dubiousCellInfoS.arfcn = this.mArfcn;
            dubiousCellInfoS.pci = this.mPci;
            dubiousCellInfoS.causeCodeBitMask = 4;
            LteDubiousCellInfoS lteDubiousCellInfoS = new LteDubiousCellInfoS();
            lteDubiousCellInfoS.cellInfo = dubiousCellInfoS;
            lteDubiousCellInfoS.cgi = (int) this.mCgi;
            return lteDubiousCellInfoS;
        }

        public NrDubiousCellInfoS getNrDubiousCellInfoS() {
            PlmnIdS plmnIdS = new PlmnIdS();
            plmnIdS.mcc = this.mMcc;
            plmnIdS.mnc = this.mMnc;
            plmnIdS.mncIncludesPcsDigit = this.mMncIncludesPcsDigit;
            DubiousCellInfoS dubiousCellInfoS = new DubiousCellInfoS();
            dubiousCellInfoS.plmnId = plmnIdS;
            dubiousCellInfoS.activeBand = this.mActiveBand;
            dubiousCellInfoS.arfcn = this.mArfcn;
            dubiousCellInfoS.pci = this.mPci;
            dubiousCellInfoS.causeCodeBitMask = 4;
            NrDubiousCellInfoS nrDubiousCellInfoS = new NrDubiousCellInfoS();
            nrDubiousCellInfoS.cellInfo = dubiousCellInfoS;
            nrDubiousCellInfoS.cgi = this.mCgi;
            nrDubiousCellInfoS.nrScsType = getNrDubiousCellScs(String.valueOf(this.mNr5gScsTypeE));
            return nrDubiousCellInfoS;
        }

        private int getNrDubiousCellScs(String str) {
            if ("15".equals(str)) {
                return 0;
            }
            if ("30".equals(str)) {
                return 1;
            }
            if ("60".equals(str)) {
                return 2;
            }
            if ("120".equals(str)) {
                return 3;
            }
            return "240".equals(str) ? 4 : 0;
        }
    }

    private void log(String str) {
        Log.d("CPPDBHelper", str);
    }
}
