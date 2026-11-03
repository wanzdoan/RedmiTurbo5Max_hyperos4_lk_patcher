package com.android.phone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.Map;
import miui.telephony.qms.DubiousCellInfoS;
import miui.telephony.qms.LteDubiousCellInfoS;
import miui.telephony.qms.NrDubiousCellInfoS;
import miui.telephony.qms.PlmnIdS;

/* JADX INFO: loaded from: classes.dex */
public class CellPreferDBHelper extends SQLiteOpenHelper {
    private static long ONE_WEEK_MILLISECOND = 604800000;
    private SQLiteDatabase mDb;
    private static final String[] SQL_QUERY_ORDER_LTE = {"mcc", "mnc", "mnc_include_pcs_digit", "cgi", "activate_band", "arfcn", "pci", "count"};
    private static final String[] SQL_QUERY_ORDER_NR = {"mcc", "mnc", "mnc_include_pcs_digit", "cgi", "activate_band", "arfcn", "pci", "count", "nr5g_scs_type"};
    private static final String[] SQL_QUERY_ORDER_FDD2TDD = {"cgi", "count", "last_time", "tunnel_flag"};
    private static CellPreferDBHelper sInstance = null;

    private CellPreferDBHelper(Context context, int i) {
        super(context, "cell_prefer.db", (SQLiteDatabase.CursorFactory) null, i);
        this.mDb = null;
    }

    public static synchronized CellPreferDBHelper getInstance(Context context) {
        try {
            if (sInstance == null) {
                sInstance = new CellPreferDBHelper(context, 2);
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

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        log("onUpgrade");
        try {
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS fdd2tdd_cell_info");
            sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS fdd2tdd_cell_info (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, cgi INTEGER NOT NULL, count INTEGER NOT NULL, last_time INTEGER NOT NULL, tunnel_flag INTEGER NOT NULL )");
        } catch (Exception e) {
            log("Exception: " + e);
        }
    }

    private void initDB(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS fdd2tdd_cell_info (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, cgi INTEGER NOT NULL, count INTEGER NOT NULL, last_time INTEGER NOT NULL, tunnel_flag INTEGER NOT NULL )");
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS lte_cell_info (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, mcc INTEGER NOT NULL, mnc INTEGER NOT NULL, mnc_include_pcs_digit INTEGER NOT NULL, cgi INTEGER NOT NULL, activate_band INTEGER NOT NULL, arfcn INTEGER NOT NULL, pci INTEGER NOT NULL, count INTEGER NOT NULL, last_time INTEGER NOT NULL )");
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS nr_cell_info (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, mcc INTEGER NOT NULL, mnc INTEGER NOT NULL, mnc_include_pcs_digit INTEGER NOT NULL, cgi INTEGER NOT NULL, activate_band INTEGER NOT NULL, arfcn INTEGER NOT NULL, pci INTEGER NOT NULL, nr5g_scs_type INTEGER NOT NULL, count INTEGER NOT NULL, last_time INTEGER NOT NULL )");
    }

    public void saveFddInfoToDb(Map map) {
        clearAllRecordByTable("fdd2tdd_cell_info");
        openWriteLink();
        this.mDb.beginTransaction();
        try {
            try {
                for (Map.Entry entry : map.entrySet()) {
                    ContentValues contentValues = new ContentValues();
                    Fdd2TddCellInfo fdd2TddCellInfo = (Fdd2TddCellInfo) entry.getValue();
                    contentValues.put("cgi", (Long) entry.getKey());
                    contentValues.put("count", Integer.valueOf(fdd2TddCellInfo.getCount()));
                    contentValues.put("last_time", Long.valueOf(fdd2TddCellInfo.getLastTime()));
                    contentValues.put("tunnel_flag", Integer.valueOf(fdd2TddCellInfo.getTunnelFlag()));
                    this.mDb.insert("fdd2tdd_cell_info", null, contentValues);
                }
                this.mDb.setTransactionSuccessful();
                log("saveFddInfoToDb success");
            } catch (Exception e) {
                log("Exception: " + e);
            }
        } finally {
            this.mDb.endTransaction();
            closeLink();
        }
    }

    private void clearAllRecordByTable(String str) {
        openWriteLink();
        try {
            if ("fdd2tdd_cell_info".equals(str)) {
                this.mDb.execSQL("DROP TABLE IF EXISTS fdd2tdd_cell_info");
                this.mDb.execSQL("CREATE TABLE IF NOT EXISTS fdd2tdd_cell_info (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, cgi INTEGER NOT NULL, count INTEGER NOT NULL, last_time INTEGER NOT NULL, tunnel_flag INTEGER NOT NULL )");
            }
        } catch (Exception e) {
            log("Exception: " + e);
        } finally {
            closeLink();
        }
    }

    public ArrayList getAllFdd2TddCellInfo() {
        ArrayList arrayList = new ArrayList();
        openWriteLink();
        Cursor cursorQuery = null;
        try {
            try {
                cursorQuery = this.mDb.query("fdd2tdd_cell_info", SQL_QUERY_ORDER_FDD2TDD, null, null, null, null, null);
                if (cursorQuery != null) {
                    while (cursorQuery.moveToNext()) {
                        arrayList.add(new Fdd2TddCellInfo(cursorQuery));
                    }
                }
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
            } catch (Exception e) {
                log("Exception: " + e);
                if (cursorQuery != null) {
                }
            }
            closeLink();
            log("getAllFdd2TddCellInfo");
            return arrayList;
        } catch (Throwable th) {
            if (cursorQuery != null) {
                cursorQuery.close();
            }
            closeLink();
            throw th;
        }
    }

    public class Fdd2TddCellInfo {
        private long mCgi;
        private int mCount;
        private long mLastTime;
        private int mTunnelFlag;

        Fdd2TddCellInfo(Cursor cursor) {
            this.mCgi = cursor.getLong(0);
            this.mCount = cursor.getInt(1);
            this.mLastTime = cursor.getLong(2);
            this.mTunnelFlag = cursor.getInt(3);
        }

        Fdd2TddCellInfo(long j, int i, long j2) {
            this.mCgi = j;
            this.mCount = i;
            this.mLastTime = j2;
            this.mTunnelFlag = 0;
        }

        public long getCgi() {
            return this.mCgi;
        }

        public int getCount() {
            return this.mCount;
        }

        public void setCount(int i) {
            this.mCount = i;
        }

        public long getLastTime() {
            return this.mLastTime;
        }

        public void setLastTime(long j) {
            this.mLastTime = j;
        }

        public int getTunnelFlag() {
            return this.mTunnelFlag;
        }

        public void setTunnelFlag(int i) {
            this.mTunnelFlag = i;
        }

        public String toString() {
            return "Fdd2TddCellInfo: {mCgi=" + this.mCgi + ", mCount=" + this.mCount + ", mLastTime=" + this.mLastTime + ", mTunnelFlag=" + this.mTunnelFlag + '}';
        }
    }

    public boolean isExist(long j, boolean z) {
        String[] strArr = {String.valueOf(j)};
        openWriteLink();
        boolean z2 = false;
        Cursor cursorQuery = null;
        try {
            if (z) {
                deleteRecordOutOfDateByTable("nr_cell_info");
                cursorQuery = this.mDb.query("nr_cell_info", SQL_QUERY_ORDER_NR, "cgi = ?", strArr, null, null, null);
            } else {
                deleteRecordOutOfDateByTable("lte_cell_info");
                cursorQuery = this.mDb.query("lte_cell_info", SQL_QUERY_ORDER_LTE, "cgi = ?", strArr, null, null, null);
            }
            if (cursorQuery != null && cursorQuery.getCount() != 0) {
                z2 = true;
            }
            if (cursorQuery != null) {
                cursorQuery.close();
            }
            closeLink();
            return z2;
        } catch (Exception e) {
            log("Exception: " + e);
            return false;
        } finally {
            if (cursorQuery != null) {
                cursorQuery.close();
            }
            closeLink();
        }
    }

    public int getCellBarringCountByCgi(long j, boolean z) {
        String[] strArr = {String.valueOf(j)};
        openWriteLink();
        SQLiteDatabase sQLiteDatabase = this.mDb;
        int i = 0;
        Cursor cursorQuery = null;
        try {
            if (z) {
                cursorQuery = sQLiteDatabase.query("nr_cell_info", SQL_QUERY_ORDER_NR, "cgi = ?", strArr, null, null, null);
            } else {
                cursorQuery = sQLiteDatabase.query("lte_cell_info", SQL_QUERY_ORDER_LTE, "cgi = ?", strArr, null, null, null);
            }
            if (cursorQuery != null && cursorQuery.moveToFirst()) {
                i = cursorQuery.getInt(7);
            }
            if (cursorQuery != null) {
                cursorQuery.close();
            }
            closeLink();
            return i;
        } catch (Exception e) {
            log("Exception: " + e);
            return 0;
        } finally {
            if (cursorQuery != null) {
                cursorQuery.close();
            }
            closeLink();
        }
    }

    public boolean updateCellInfoAndFeedbackNeedOpt(long j, boolean z) {
        int cellBarringCountByCgi = getCellBarringCountByCgi(j, z) + 1;
        ContentValues contentValues = new ContentValues();
        contentValues.put("count", Integer.valueOf(cellBarringCountByCgi));
        String[] strArr = {String.valueOf(j)};
        openWriteLink();
        SQLiteDatabase sQLiteDatabase = this.mDb;
        try {
            try {
                if (z) {
                    sQLiteDatabase.update("nr_cell_info", contentValues, "cgi = ?", strArr);
                } else {
                    sQLiteDatabase.update("lte_cell_info", contentValues, "cgi = ?", strArr);
                }
                log("updateCellInfo success");
            } catch (Exception e) {
                log("Exception: " + e);
            }
            closeLink();
            return cellBarringCountByCgi > 10;
        } catch (Throwable th) {
            closeLink();
            throw th;
        }
    }

    public void insertCellBarringRecord(CellInfo cellInfo, boolean z, long j) {
        if (cellInfo == null) {
            log("insertCellBarringRecord exception");
            return;
        }
        if (getAllCellBarringRecordCount(z) >= 32) {
            if (z) {
                deleteOneOldestRecord("nr_cell_info");
            } else {
                deleteOneOldestRecord("lte_cell_info");
            }
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("mcc", Short.valueOf(cellInfo.mMcc));
        contentValues.put("mnc", Short.valueOf(cellInfo.mMnc));
        contentValues.put("mnc_include_pcs_digit", Boolean.valueOf(cellInfo.mMncIncludesPcsDigit));
        contentValues.put("cgi", Long.valueOf(cellInfo.mCgi));
        contentValues.put("activate_band", Integer.valueOf(cellInfo.mActiveBand));
        contentValues.put("arfcn", Integer.valueOf(cellInfo.mArfcn));
        contentValues.put("pci", Short.valueOf(cellInfo.mPci));
        contentValues.put("count", (Integer) 1);
        contentValues.put("last_time", Long.valueOf(j));
        openWriteLink();
        try {
            if (z) {
                contentValues.put("nr5g_scs_type", Integer.valueOf(cellInfo.mNr5gScsTypeE));
                this.mDb.insert("nr_cell_info", null, contentValues);
            } else {
                this.mDb.insert("lte_cell_info", null, contentValues);
            }
            log("insert record");
            closeLink();
        } catch (Exception e) {
            log("Exception: " + e);
        } finally {
            closeLink();
        }
    }

    private int getAllCellBarringRecordCount(boolean z) {
        Cursor cursorQuery;
        openWriteLink();
        SQLiteDatabase sQLiteDatabase = this.mDb;
        int count = 0;
        Cursor cursor = null;
        try {
            if (z) {
                cursorQuery = sQLiteDatabase.query("nr_cell_info", SQL_QUERY_ORDER_NR, null, null, null, null, null);
            } else {
                cursorQuery = sQLiteDatabase.query("lte_cell_info", SQL_QUERY_ORDER_LTE, null, null, null, null, null);
            }
            cursor = cursorQuery;
            if (cursor != null) {
                count = cursor.getCount();
                log("getAllCellBarringRecordCount rst=" + count);
            }
            if (cursor != null) {
                cursor.close();
            }
            closeLink();
            return count;
        } catch (Exception e) {
            log("Exception: " + e);
            return count;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            closeLink();
        }
    }

    private void deleteOneOldestRecord(String str) {
        openWriteLink();
        Cursor cursorRawQuery = null;
        try {
            cursorRawQuery = this.mDb.rawQuery("select min(last_time) from " + str, null);
            long j = (cursorRawQuery == null || !cursorRawQuery.moveToFirst()) ? -1L : cursorRawQuery.getLong(0);
            if (j != -1) {
                this.mDb.delete(str, "last_time = ?", new String[]{String.valueOf(j)});
                log("deleteOneOldestRecord which time=" + j);
            }
        } catch (Exception e) {
            log("Exception: " + e);
        } finally {
            if (cursorRawQuery != null) {
                cursorRawQuery.close();
            }
            closeLink();
        }
    }

    private void deleteRecordOutOfDateByTable(String str) {
        try {
            this.mDb.delete(str, "last_time < ?", new String[]{String.valueOf(System.currentTimeMillis() - ONE_WEEK_MILLISECOND)});
            log("delete record out of date");
        } catch (Exception e) {
            log("Exception: " + e);
        }
    }

    public ArrayList getLteDubiousCellInfoSList() {
        openWriteLink();
        deleteRecordOutOfDateByTable("lte_cell_info");
        ArrayList arrayList = new ArrayList();
        Cursor cursorQuery = null;
        try {
            try {
                cursorQuery = this.mDb.query("lte_cell_info", SQL_QUERY_ORDER_LTE, "count > 10", null, null, null, null);
                if (cursorQuery != null) {
                    while (cursorQuery.moveToNext()) {
                        arrayList.add(new CellInfo(cursorQuery, false).getLteDubiousCellInfoS());
                    }
                }
                return arrayList;
            } catch (Exception e) {
                log("Exception: " + e);
                return arrayList;
            }
        } finally {
            if (cursorQuery != null) {
                cursorQuery.close();
            }
            closeLink();
        }
    }

    /* JADX WARN: Code duplicated, block: B:27:0x0060  */
    public LteDubiousCellInfoS getLteDubiousCellInfoS(long j) throws Throwable {
        Throwable th;
        Cursor cursorQuery;
        openWriteLink();
        String[] strArr = {String.valueOf(j)};
        LteDubiousCellInfoS lteDubiousCellInfoS = null;
        try {
            try {
                cursorQuery = this.mDb.query("lte_cell_info", SQL_QUERY_ORDER_LTE, "cgi = ?", strArr, null, null, null);
                if (cursorQuery != null) {
                    try {
                        if (cursorQuery.moveToFirst()) {
                            lteDubiousCellInfoS = new CellInfo(cursorQuery, false).getLteDubiousCellInfoS();
                        }
                    } catch (Exception e) {
                        e = e;
                        log("Exception: " + e);
                        if (cursorQuery != null) {
                            cursorQuery.close();
                        }
                        closeLink();
                        return null;
                    }
                }
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
                closeLink();
                return lteDubiousCellInfoS;
            } catch (Throwable th2) {
                th = th2;
                if (0 != 0) {
                    lteDubiousCellInfoS.close();
                }
                closeLink();
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            cursorQuery = null;
        } catch (Throwable th3) {
            th = th3;
            if (0 != 0) {
                lteDubiousCellInfoS.close();
            }
            closeLink();
            throw th;
        }
    }

    public ArrayList getNrDubiousCellInfoSList() {
        openWriteLink();
        deleteRecordOutOfDateByTable("nr_cell_info");
        ArrayList arrayList = new ArrayList();
        Cursor cursorQuery = null;
        try {
            try {
                cursorQuery = this.mDb.query("nr_cell_info", SQL_QUERY_ORDER_NR, "count > 10", null, null, null, null);
                if (cursorQuery != null) {
                    while (cursorQuery.moveToNext()) {
                        arrayList.add(new CellInfo(cursorQuery, true).getNrDubiousCellInfoS());
                    }
                }
                return arrayList;
            } catch (Exception e) {
                log("Exception: " + e);
                return arrayList;
            }
        } finally {
            if (cursorQuery != null) {
                cursorQuery.close();
            }
            closeLink();
        }
    }

    /* JADX WARN: Code duplicated, block: B:27:0x0061  */
    public NrDubiousCellInfoS getNrDubiousCellInfoS(long j) throws Throwable {
        Throwable th;
        Cursor cursorQuery;
        openWriteLink();
        String[] strArr = {String.valueOf(j)};
        NrDubiousCellInfoS nrDubiousCellInfoS = null;
        try {
            try {
                cursorQuery = this.mDb.query("nr_cell_info", SQL_QUERY_ORDER_NR, "cgi = ?", strArr, null, null, null);
                if (cursorQuery != null) {
                    try {
                        if (cursorQuery.moveToFirst()) {
                            nrDubiousCellInfoS = new CellInfo(cursorQuery, true).getNrDubiousCellInfoS();
                        }
                    } catch (Exception e) {
                        e = e;
                        log("Exception: " + e);
                        if (cursorQuery != null) {
                            cursorQuery.close();
                        }
                        closeLink();
                        return null;
                    }
                }
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
                closeLink();
                return nrDubiousCellInfoS;
            } catch (Throwable th2) {
                th = th2;
                if (0 != 0) {
                    nrDubiousCellInfoS.close();
                }
                closeLink();
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            cursorQuery = null;
        } catch (Throwable th3) {
            th = th3;
            if (0 != 0) {
                nrDubiousCellInfoS.close();
            }
            closeLink();
            throw th;
        }
    }

    public class CellInfo {
        public int mActiveBand;
        public int mArfcn;
        public long mCgi;
        public short mMcc;
        public short mMnc;
        public boolean mMncIncludesPcsDigit;
        public int mNr5gScsTypeE;
        public short mPci;

        CellInfo(short s, short s2, boolean z, long j, int i, int i2, short s3) {
            this.mMcc = s;
            this.mMnc = s2;
            this.mMncIncludesPcsDigit = z;
            this.mCgi = j;
            this.mActiveBand = i;
            this.mArfcn = i2;
            this.mPci = s3;
        }

        CellInfo(short s, short s2, boolean z, long j, int i, int i2, short s3, int i3) {
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
            this.mMcc = cursor.getShort(0);
            this.mMnc = cursor.getShort(1);
            this.mMncIncludesPcsDigit = cursor.getInt(2) == 1;
            this.mCgi = cursor.getLong(3);
            this.mActiveBand = cursor.getInt(4);
            this.mArfcn = cursor.getInt(5);
            this.mPci = cursor.getShort(6);
            if (z) {
                this.mNr5gScsTypeE = cursor.getInt(8);
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
            dubiousCellInfoS.causeCodeBitMask = 32;
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
            dubiousCellInfoS.causeCodeBitMask = 32;
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
        Log.d("CellPreferDBHelper", str);
    }
}
