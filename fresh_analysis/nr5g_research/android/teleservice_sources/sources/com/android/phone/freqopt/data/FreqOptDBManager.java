package com.android.phone.freqopt.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.android.phone.freqopt.util.FreqOptReporter;
import com.android.phone.freqopt.util.LogUtils;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public class FreqOptDBManager extends SQLiteOpenHelper {
    private static final String[] ALL_TABLES = {"freq_opt_fdd_tdd", "freq_opt_n79_hotspot", "freq_opt_nr_lte"};
    private static FreqOptDBManager sInstance;
    private SQLiteDatabase mDb;
    private final AtomicInteger mDbErrorCount;

    interface DbCallable {
        Object call(SQLiteDatabase sQLiteDatabase);
    }

    private FreqOptDBManager(Context context) {
        super(context, "freq_opt_learning.db", (SQLiteDatabase.CursorFactory) null, 1);
        this.mDbErrorCount = new AtomicInteger(0);
    }

    public static synchronized FreqOptDBManager getInstance(Context context) {
        try {
            if (sInstance == null) {
                sInstance = new FreqOptDBManager(context.getApplicationContext());
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    private static void createTable(SQLiteDatabase sQLiteDatabase, String str) {
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + str + " (id INTEGER PRIMARY KEY AUTOINCREMENT, source_cell_id TEXT NOT NULL, target_cell_id TEXT NOT NULL, count INTEGER NOT NULL DEFAULT 1, refresh_time BIGINT NOT NULL, action_time BIGINT NOT NULL DEFAULT 0, ext_data TEXT, state INTEGER NOT NULL DEFAULT 0, created_time BIGINT NOT NULL, connected_count INTEGER NOT NULL DEFAULT 0)");
        sQLiteDatabase.execSQL("CREATE INDEX IF NOT EXISTS idx_" + str + "_source ON " + str + " (source_cell_id)");
    }

    static String getTableName(String str) {
        if (str == null) {
            Folme$$ExternalSyntheticBUOutline0.m("strategyId must not be null");
            return null;
        }
        str.hashCode();
        switch (str) {
            case "nr_lte":
                return "freq_opt_nr_lte";
            case "fdd_tdd":
                return "freq_opt_fdd_tdd";
            case "n79_hotspot":
                return "freq_opt_n79_hotspot";
            default:
                Folme$$ExternalSyntheticBUOutline0.m("Unknown strategyId: ".concat(str));
                return null;
        }
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        for (String str : ALL_TABLES) {
            createTable(sQLiteDatabase, str);
        }
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        LogUtils.i("Freq_Opt_DBManager", "onUpgrade from " + i + " to " + i2);
    }

    private synchronized SQLiteDatabase getDb() {
        try {
            SQLiteDatabase sQLiteDatabase = this.mDb;
            if (sQLiteDatabase == null || !sQLiteDatabase.isOpen()) {
                this.mDb = getWritableDatabase();
            }
        } catch (Throwable th) {
            throw th;
        }
        return this.mDb;
    }

    private Object safeQuery(String str, Object obj, DbCallable dbCallable) {
        try {
            return dbCallable.call(getDb());
        } catch (SQLException e) {
            LogUtils.w("Freq_Opt_DBManager", str + " exception", e);
            reportDbErrorIfNeeded(str, e);
            return obj;
        }
    }

    private void safeExecute(String str, DbCallable dbCallable) {
        safeQuery(str, null, dbCallable);
    }

    private Object safeTransaction(String str, Object obj, DbCallable dbCallable) {
        SQLiteDatabase db = getDb();
        db.beginTransaction();
        try {
            Object objCall = dbCallable.call(db);
            db.setTransactionSuccessful();
            return objCall;
        } catch (SQLException e) {
            LogUtils.w("Freq_Opt_DBManager", str + " exception", e);
            reportDbErrorIfNeeded(str, e);
            return obj;
        } finally {
            db.endTransaction();
        }
    }

    private void reportDbErrorIfNeeded(String str, SQLException sQLException) {
        int iIncrementAndGet = this.mDbErrorCount.incrementAndGet();
        if (iIncrementAndGet == 5) {
            FreqOptReporter.reportEvent("freq_opt_db_error", "db_manager", null, null, iIncrementAndGet, "op=" + str + ",err=" + sQLException.getMessage());
            this.mDbErrorCount.set(0);
        }
    }

    public FreqOptLearningRecord queryRecord(final String str, final String str2, final String str3) {
        final String tableName = getTableName(str);
        return (FreqOptLearningRecord) safeQuery("queryRecord", null, new DbCallable() { // from class: com.android.phone.freqopt.data.FreqOptDBManager$$ExternalSyntheticLambda3
            @Override // com.android.phone.freqopt.data.FreqOptDBManager.DbCallable
            public final Object call(SQLiteDatabase sQLiteDatabase) {
                return FreqOptDBManager.m2536$r8$lambda$LsPiJZKDoN3ML8I0SahkumvsS8(tableName, str2, str3, str, sQLiteDatabase);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$LsPiJZKDoN3ML8I0Sahkumv-sS8, reason: not valid java name */
    public static /* synthetic */ FreqOptLearningRecord m2536$r8$lambda$LsPiJZKDoN3ML8I0SahkumvsS8(String str, String str2, String str3, String str4, SQLiteDatabase sQLiteDatabase) {
        Cursor cursorQuery = sQLiteDatabase.query(str, null, "source_cell_id=? AND target_cell_id=?", new String[]{str2, str3}, null, null, null);
        if (cursorQuery != null) {
            try {
                if (cursorQuery.moveToFirst()) {
                    FreqOptLearningRecord freqOptLearningRecord = new FreqOptLearningRecord(cursorQuery);
                    freqOptLearningRecord.setStrategyId(str4);
                    LogUtils.d("Freq_Opt_DBManager", "queryRecord found: " + freqOptLearningRecord);
                    cursorQuery.close();
                    return freqOptLearningRecord;
                }
            } catch (Throwable th) {
                try {
                    cursorQuery.close();
                    throw th;
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                    throw th;
                }
            }
        }
        if (cursorQuery == null) {
            return null;
        }
        cursorQuery.close();
        return null;
    }

    public List queryBySourceCell(final String str, final String str2, final long j) {
        final String tableName = getTableName(str);
        return (List) safeQuery("queryBySourceCell", new ArrayList(), new DbCallable() { // from class: com.android.phone.freqopt.data.FreqOptDBManager$$ExternalSyntheticLambda10
            @Override // com.android.phone.freqopt.data.FreqOptDBManager.DbCallable
            public final Object call(SQLiteDatabase sQLiteDatabase) {
                return FreqOptDBManager.$r8$lambda$okd6qVaUJgJLwfosp7LgSqaaVK4(j, str2, tableName, str, sQLiteDatabase);
            }
        });
    }

    public static /* synthetic */ List $r8$lambda$okd6qVaUJgJLwfosp7LgSqaaVK4(long j, String str, String str2, String str3, SQLiteDatabase sQLiteDatabase) {
        String[] strArr;
        String str4;
        ArrayList arrayList = new ArrayList();
        if (j > 0) {
            strArr = new String[]{str, String.valueOf(System.currentTimeMillis() - j)};
            str4 = "source_cell_id=? AND refresh_time>?";
        } else {
            strArr = new String[]{str};
            str4 = "source_cell_id=?";
        }
        Cursor cursorQuery = sQLiteDatabase.query(str2, null, str4, strArr, null, null, "count DESC");
        if (cursorQuery != null) {
            while (cursorQuery.moveToNext()) {
                try {
                    FreqOptLearningRecord freqOptLearningRecord = new FreqOptLearningRecord(cursorQuery);
                    freqOptLearningRecord.setStrategyId(str3);
                    arrayList.add(freqOptLearningRecord);
                } catch (Throwable th) {
                    try {
                        cursorQuery.close();
                        throw th;
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                        throw th;
                    }
                }
            }
        }
        if (cursorQuery != null) {
            cursorQuery.close();
        }
        return arrayList;
    }

    public List queryBySourceCellForState(final String str, final String str2, final long j, final boolean z) {
        final String tableName = getTableName(str);
        return (List) safeQuery("queryBySourceCellForState", new ArrayList(), new DbCallable() { // from class: com.android.phone.freqopt.data.FreqOptDBManager$$ExternalSyntheticLambda6
            @Override // com.android.phone.freqopt.data.FreqOptDBManager.DbCallable
            public final Object call(SQLiteDatabase sQLiteDatabase) {
                return FreqOptDBManager.m2534$r8$lambda$9NpzYKtykNNRf7Ey1TDCRv9EMg(z, j, str2, tableName, str, sQLiteDatabase);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$9NpzYKtykNN-Rf7Ey1TDCRv9EMg, reason: not valid java name */
    public static /* synthetic */ List m2534$r8$lambda$9NpzYKtykNNRf7Ey1TDCRv9EMg(boolean z, long j, String str, String str2, String str3, SQLiteDatabase sQLiteDatabase) {
        String str4;
        String[] strArr;
        ArrayList arrayList = new ArrayList();
        String str5 = z ? "connected_count" : "count";
        if (j > 0) {
            str4 = "source_cell_id=? AND refresh_time>? AND " + str5 + ">0";
            strArr = new String[]{str, String.valueOf(System.currentTimeMillis() - j)};
        } else {
            str4 = "source_cell_id=? AND " + str5 + ">0";
            strArr = new String[]{str};
        }
        Cursor cursorQuery = sQLiteDatabase.query(str2, null, str4, strArr, null, null, str5.concat(" DESC"));
        if (cursorQuery != null) {
            while (cursorQuery.moveToNext()) {
                try {
                    FreqOptLearningRecord freqOptLearningRecord = new FreqOptLearningRecord(cursorQuery);
                    freqOptLearningRecord.setStrategyId(str3);
                    arrayList.add(freqOptLearningRecord);
                } catch (Throwable th) {
                    try {
                        cursorQuery.close();
                        throw th;
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                        throw th;
                    }
                }
            }
        }
        if (cursorQuery != null) {
            cursorQuery.close();
        }
        return arrayList;
    }

    public List queryAllByStrategy(final String str, final long j) {
        final String tableName = getTableName(str);
        return (List) safeQuery("queryAllByStrategy", new ArrayList(), new DbCallable() { // from class: com.android.phone.freqopt.data.FreqOptDBManager$$ExternalSyntheticLambda5
            @Override // com.android.phone.freqopt.data.FreqOptDBManager.DbCallable
            public final Object call(SQLiteDatabase sQLiteDatabase) {
                return FreqOptDBManager.$r8$lambda$jFP4xuradXP0gRhVliL6kwBVW7E(j, tableName, str, sQLiteDatabase);
            }
        });
    }

    public static /* synthetic */ List $r8$lambda$jFP4xuradXP0gRhVliL6kwBVW7E(long j, String str, String str2, SQLiteDatabase sQLiteDatabase) {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQuery = sQLiteDatabase.query(str, null, "refresh_time>?", new String[]{String.valueOf(System.currentTimeMillis() - j)}, null, null, "refresh_time DESC");
        if (cursorQuery != null) {
            while (cursorQuery.moveToNext()) {
                try {
                    FreqOptLearningRecord freqOptLearningRecord = new FreqOptLearningRecord(cursorQuery);
                    freqOptLearningRecord.setStrategyId(str2);
                    arrayList.add(freqOptLearningRecord);
                } catch (Throwable th) {
                    try {
                        cursorQuery.close();
                        throw th;
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                        throw th;
                    }
                }
            }
        }
        if (cursorQuery != null) {
            cursorQuery.close();
        }
        LogUtils.d("Freq_Opt_DBManager", "queryAllByStrategy: strategyId=" + str2 + ", total=" + arrayList.size());
        for (int i = 0; i < arrayList.size(); i++) {
            LogUtils.d("Freq_Opt_DBManager", "queryAllByStrategy [" + i + "]: " + arrayList.get(i));
        }
        return arrayList;
    }

    public long insertOrUpdate(final FreqOptLearningRecord freqOptLearningRecord, final long j, final boolean z) {
        return ((Long) safeTransaction("insertOrUpdate", -1L, new DbCallable() { // from class: com.android.phone.freqopt.data.FreqOptDBManager$$ExternalSyntheticLambda2
            @Override // com.android.phone.freqopt.data.FreqOptDBManager.DbCallable
            public final Object call(SQLiteDatabase sQLiteDatabase) {
                return this.f$0.lambda$insertOrUpdate$4(freqOptLearningRecord, j, z, sQLiteDatabase);
            }
        })).longValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Long lambda$insertOrUpdate$4(FreqOptLearningRecord freqOptLearningRecord, long j, boolean z, SQLiteDatabase sQLiteDatabase) {
        deleteExpiredRecords(freqOptLearningRecord.getStrategyId(), j);
        FreqOptLearningRecord freqOptLearningRecordQueryRecord = queryRecord(freqOptLearningRecord.getStrategyId(), freqOptLearningRecord.getSourceCellId(), freqOptLearningRecord.getTargetCellId());
        if (freqOptLearningRecordQueryRecord != null) {
            return Long.valueOf(updateExistingRecord(freqOptLearningRecord, freqOptLearningRecordQueryRecord, z));
        }
        return Long.valueOf(insertNewRecord(freqOptLearningRecord, z));
    }

    private long updateExistingRecord(FreqOptLearningRecord freqOptLearningRecord, FreqOptLearningRecord freqOptLearningRecord2, boolean z) {
        long jCurrentTimeMillis = System.currentTimeMillis() - freqOptLearningRecord2.getRefreshTime();
        if (jCurrentTimeMillis < 30000) {
            long jAbs = Math.abs(freqOptLearningRecord2.getRefreshTime() - freqOptLearningRecord2.getActionTime());
            if (freqOptLearningRecord2.getActionTime() > 0 && jAbs < 1000) {
                LogUtils.d("Freq_Opt_DBManager", "insertOrUpdate debounce bypassed: post-execution learning, elapsed=" + jCurrentTimeMillis + "ms");
            } else {
                LogUtils.d("Freq_Opt_DBManager", "insertOrUpdate debounce: elapsed=" + jCurrentTimeMillis + "ms < 30s, deleting existing record id=" + freqOptLearningRecord2.getId());
                deleteRecord(freqOptLearningRecord.getStrategyId(), freqOptLearningRecord2.getId());
                return -1L;
            }
        }
        SQLiteDatabase db = getDb();
        String tableName = getTableName(freqOptLearningRecord.getStrategyId());
        ContentValues contentValues = new ContentValues();
        if (z) {
            contentValues.put("connected_count", Integer.valueOf(freqOptLearningRecord2.getConnectedCount() + 1));
        } else {
            contentValues.put("count", Integer.valueOf(freqOptLearningRecord2.getCount() + 1));
        }
        contentValues.put("refresh_time", Long.valueOf(System.currentTimeMillis()));
        if (freqOptLearningRecord.getExtData() != null) {
            contentValues.put("ext_data", freqOptLearningRecord.getExtData());
        }
        LogUtils.d("Freq_Opt_DBManager", "updateExistingRecord id=" + freqOptLearningRecord2.getId() + ", isConnected=" + z + ", rows=" + db.update(tableName, contentValues, "id=?", new String[]{String.valueOf(freqOptLearningRecord2.getId())}));
        return freqOptLearningRecord2.getId();
    }

    private long insertNewRecord(FreqOptLearningRecord freqOptLearningRecord, boolean z) {
        SQLiteDatabase db = getDb();
        String tableName = getTableName(freqOptLearningRecord.getStrategyId());
        Cursor cursorRawQuery = db.rawQuery("SELECT COUNT(*) FROM " + tableName, null);
        if (cursorRawQuery != null) {
            try {
                if (cursorRawQuery.moveToFirst() && cursorRawQuery.getInt(0) >= 30) {
                    db.execSQL("DELETE FROM " + tableName + " WHERE id = (SELECT id FROM " + tableName + " ORDER BY refresh_time ASC LIMIT 1)");
                    StringBuilder sb = new StringBuilder("ensureCapacity evicted oldest record for ");
                    sb.append(freqOptLearningRecord.getStrategyId());
                    LogUtils.d("Freq_Opt_DBManager", sb.toString());
                }
            } catch (Throwable th) {
                try {
                    cursorRawQuery.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
        if (cursorRawQuery != null) {
            cursorRawQuery.close();
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("source_cell_id", freqOptLearningRecord.getSourceCellId());
        contentValues.put("target_cell_id", freqOptLearningRecord.getTargetCellId());
        int count = z ? 0 : freqOptLearningRecord.getCount();
        int connectedCount = z ? 1 : freqOptLearningRecord.getConnectedCount();
        contentValues.put("count", Integer.valueOf(count));
        contentValues.put("connected_count", Integer.valueOf(connectedCount));
        contentValues.put("refresh_time", Long.valueOf(freqOptLearningRecord.getRefreshTime()));
        contentValues.put("action_time", Long.valueOf(freqOptLearningRecord.getActionTime()));
        contentValues.put("ext_data", freqOptLearningRecord.getExtData());
        contentValues.put(MEModuleSignalT1s.EXTRA_STATE, Integer.valueOf(freqOptLearningRecord.getState()));
        contentValues.put("created_time", Long.valueOf(freqOptLearningRecord.getCreatedTime()));
        long jInsert = db.insert(tableName, null, contentValues);
        if (jInsert != -1) {
            freqOptLearningRecord.setId(jInsert);
        }
        LogUtils.d("Freq_Opt_DBManager", "insertNewRecord: id=" + jInsert + ", source=" + freqOptLearningRecord.getSourceCellId() + ", target=" + freqOptLearningRecord.getTargetCellId() + ", count=" + count + ", connectedCount=" + connectedCount);
        return jInsert;
    }

    public void updateActionTime(final String str, final long j) {
        final String tableName = getTableName(str);
        safeExecute("updateActionTime", new DbCallable() { // from class: com.android.phone.freqopt.data.FreqOptDBManager$$ExternalSyntheticLambda7
            @Override // com.android.phone.freqopt.data.FreqOptDBManager.DbCallable
            public final Object call(SQLiteDatabase sQLiteDatabase) {
                return FreqOptDBManager.$r8$lambda$6JAcGPzZw7E3748pqVs3YeUfb4w(tableName, j, str, sQLiteDatabase);
            }
        });
    }

    public static /* synthetic */ Void $r8$lambda$6JAcGPzZw7E3748pqVs3YeUfb4w(String str, long j, String str2, SQLiteDatabase sQLiteDatabase) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        ContentValues contentValues = new ContentValues();
        contentValues.put("action_time", Long.valueOf(jCurrentTimeMillis));
        contentValues.put("refresh_time", Long.valueOf(jCurrentTimeMillis));
        LogUtils.d("Freq_Opt_DBManager", "updateActionTime: strategyId=" + str2 + ", recordId=" + j + ", rows=" + sQLiteDatabase.update(str, contentValues, "id=?", new String[]{String.valueOf(j)}));
        return null;
    }

    public void deleteRecord(final String str, final long j) {
        final String tableName = getTableName(str);
        safeExecute("deleteRecord", new DbCallable() { // from class: com.android.phone.freqopt.data.FreqOptDBManager$$ExternalSyntheticLambda4
            @Override // com.android.phone.freqopt.data.FreqOptDBManager.DbCallable
            public final Object call(SQLiteDatabase sQLiteDatabase) {
                return FreqOptDBManager.m2535$r8$lambda$KIHTLeGJE1vq21kPfvSXNIHalo(tableName, j, str, sQLiteDatabase);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$KIHTLeGJE1vq2-1kPfvSXNIHalo, reason: not valid java name */
    public static /* synthetic */ Void m2535$r8$lambda$KIHTLeGJE1vq21kPfvSXNIHalo(String str, long j, String str2, SQLiteDatabase sQLiteDatabase) {
        LogUtils.d("Freq_Opt_DBManager", "deleteRecord: strategyId=" + str2 + ", recordId=" + j + ", deleted=" + sQLiteDatabase.delete(str, "id=?", new String[]{String.valueOf(j)}));
        return null;
    }

    public void refreshBySourceCell(final String str, final String str2) {
        final String tableName = getTableName(str);
        safeExecute("refreshBySourceCell", new DbCallable() { // from class: com.android.phone.freqopt.data.FreqOptDBManager$$ExternalSyntheticLambda8
            @Override // com.android.phone.freqopt.data.FreqOptDBManager.DbCallable
            public final Object call(SQLiteDatabase sQLiteDatabase) {
                return FreqOptDBManager.$r8$lambda$Vl0TovieOWpbZ4nyI8egrzJ110o(tableName, str2, str, sQLiteDatabase);
            }
        });
    }

    public static /* synthetic */ Void $r8$lambda$Vl0TovieOWpbZ4nyI8egrzJ110o(String str, String str2, String str3, SQLiteDatabase sQLiteDatabase) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("refresh_time", Long.valueOf(System.currentTimeMillis()));
        LogUtils.d("Freq_Opt_DBManager", "refreshBySourceCell strategyId=" + str3 + ", source=" + str2 + ", updated=" + sQLiteDatabase.update(str, contentValues, "source_cell_id=?", new String[]{str2}));
        return null;
    }

    public void deleteBySourceCell(final String str, final String str2) {
        final String tableName = getTableName(str);
        safeExecute("deleteBySourceCell", new DbCallable() { // from class: com.android.phone.freqopt.data.FreqOptDBManager$$ExternalSyntheticLambda1
            @Override // com.android.phone.freqopt.data.FreqOptDBManager.DbCallable
            public final Object call(SQLiteDatabase sQLiteDatabase) {
                return FreqOptDBManager.$r8$lambda$I2BNC8JMO2zOIijMy9eVwp8fy_A(tableName, str2, str, sQLiteDatabase);
            }
        });
    }

    public static /* synthetic */ Void $r8$lambda$I2BNC8JMO2zOIijMy9eVwp8fy_A(String str, String str2, String str3, SQLiteDatabase sQLiteDatabase) {
        LogUtils.d("Freq_Opt_DBManager", "deleteBySourceCell strategyId=" + str3 + ", source=" + str2 + ", deleted=" + sQLiteDatabase.delete(str, "source_cell_id=?", new String[]{str2}));
        return null;
    }

    public void clearOtherTargetsForState(final String str, final String str2, final String str3, final boolean z) {
        final String tableName = getTableName(str);
        safeTransaction("clearOtherTargetsForState", null, new DbCallable() { // from class: com.android.phone.freqopt.data.FreqOptDBManager$$ExternalSyntheticLambda0
            @Override // com.android.phone.freqopt.data.FreqOptDBManager.DbCallable
            public final Object call(SQLiteDatabase sQLiteDatabase) {
                return FreqOptDBManager.m2537$r8$lambda$XdaX6D_ZlLNd6kcMYv5KpWTiow(z, str2, str3, tableName, str, sQLiteDatabase);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$XdaX6D_Zl-LNd6kcMYv5KpWTiow, reason: not valid java name */
    public static /* synthetic */ Object m2537$r8$lambda$XdaX6D_ZlLNd6kcMYv5KpWTiow(boolean z, String str, String str2, String str3, String str4, SQLiteDatabase sQLiteDatabase) {
        String str5 = z ? "connected_count" : "count";
        ContentValues contentValues = new ContentValues();
        contentValues.put(str5, (Integer) 0);
        String[] strArr = {str, str2};
        int iUpdate = sQLiteDatabase.update(str3, contentValues, "source_cell_id=? AND target_cell_id!=?", strArr);
        if (iUpdate <= 0) {
            return null;
        }
        LogUtils.d("Freq_Opt_DBManager", "clearOtherTargetsForState strategyId=" + str4 + ", source=" + str + ", keep=" + str2 + ", isConnected=" + z + ", zeroed=" + iUpdate + ", deleted=" + sQLiteDatabase.delete(str3, "source_cell_id=? AND target_cell_id!=? AND count=0 AND connected_count=0", strArr));
        return null;
    }

    public void deleteExpiredRecords(final String str, final long j) {
        final String tableName = getTableName(str);
        safeExecute("deleteExpiredRecords", new DbCallable() { // from class: com.android.phone.freqopt.data.FreqOptDBManager$$ExternalSyntheticLambda9
            @Override // com.android.phone.freqopt.data.FreqOptDBManager.DbCallable
            public final Object call(SQLiteDatabase sQLiteDatabase) {
                return FreqOptDBManager.$r8$lambda$5allB0QWdnjr6slrt4s4elmlQkY(j, tableName, str, sQLiteDatabase);
            }
        });
    }

    public static /* synthetic */ Void $r8$lambda$5allB0QWdnjr6slrt4s4elmlQkY(long j, String str, String str2, SQLiteDatabase sQLiteDatabase) {
        int iDelete = sQLiteDatabase.delete(str, "refresh_time<?", new String[]{String.valueOf(System.currentTimeMillis() - j)});
        if (iDelete <= 0) {
            return null;
        }
        LogUtils.d("Freq_Opt_DBManager", "deleteExpiredRecords strategyId=" + str2 + ", deleted=" + iDelete);
        return null;
    }

    public void resetActionTime(String str, String str2) {
        SQLiteDatabase db = getDb();
        String tableName = getTableName(str);
        ContentValues contentValues = new ContentValues();
        contentValues.put("action_time", (Integer) 0);
        if (str2 != null) {
            db.update(tableName, contentValues, "source_cell_id=?", new String[]{str2});
        } else {
            db.update(tableName, contentValues, null, null);
        }
    }

    public int updateRefreshTime(String str, String str2, long j) {
        SQLiteDatabase db = getDb();
        String tableName = getTableName(str);
        ContentValues contentValues = new ContentValues();
        contentValues.put("refresh_time", Long.valueOf(j));
        return db.update(tableName, contentValues, "source_cell_id=?", new String[]{str2});
    }
}
