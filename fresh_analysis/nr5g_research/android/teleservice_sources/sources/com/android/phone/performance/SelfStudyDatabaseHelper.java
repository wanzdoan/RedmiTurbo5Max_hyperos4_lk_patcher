package com.android.phone.performance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.OffsetCell;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class SelfStudyDatabaseHelper extends SQLiteOpenHelper {
    String CREATE_CELL_ROUTE_TABLE;
    String CREATE_CELL_ROUTE_TABLE_V2;
    String CREATE_GAME_BAD_CELL_AI_TABLE;
    String CREATE_GAME_BAD_CELL_MODEM_TABLE;
    String CREATE_GAME_BAD_CELL_TABLE;
    String CREATE_HOME_STREAM_BAD_CELL_TABLE;
    String CREATE_LOW_POWER_CELLS_TABLE;
    String CREATE_LTE_BAD_CELL_TABLE_V2;
    String CREATE_MULTI_ROUTE_BAD_CELL_TABLE;
    String CREATE_NR_BAD_CELL_TABLE_V2;
    String CREATE_OFFSET_CELL_OPT_TABLE;
    String CREATE_RESIDENT_CELLS_TABLE;
    String CREATE_SMART_NETWORK_GAME_CELL_TABLE;
    String CREATE_STREAM_MEDIA_DATA_INFO_TABLE_V3;
    String CREATE_TABLE;
    String CREATE_WECHAT_BAD_CELL_TABLE;
    String CREATE_WECHAT_VIDEO_ACTION_A3_SKIP_CELL_TABLE;
    String CREATE_WECHAT_VIDEO_DUBIOUS_CELL;

    public SelfStudyDatabaseHelper(Context context) {
        super(context, "MiuiTelephony2.db", (SQLiteDatabase.CursorFactory) null, 21);
        this.CREATE_TABLE = "create table cell_table(id integer primary key autoincrement, cid text, freq integer, pci integer, rat integer, scs integer default 90, is_bad_cell integer, current_cell_count integer, rrc_exception integer, high_bler_count integer, low_ul_grant integer, low_dl_grant integer, data_type integer, score integer default 0, rlf_count integer, bad_rsrp_duration integer, bad_rsrq_duration integer, duration integer, have_been_optimized integer default 0)";
        this.CREATE_CELL_ROUTE_TABLE = "create table cell_route_table(id integer primary key autoincrement, compare_count integer, rat_change_count integer, route text)";
        this.CREATE_CELL_ROUTE_TABLE_V2 = "create table cell_route_table_v2(id integer primary key autoincrement, cell1_cid text, cell1_rat integer, cell1_freq integer, cell1_pci integer, cell2_cid text, cell2_rat integer, cell2_freq integer, cell2_pci text, cell3_cid text, cell3_rat integer, cell3_freq integer, cell3_pci text, cell4_cid text, cell4_rat integer, cell4_freq integer, cell4_pci text, cell5_cid text, cell5_rat integer, cell5_freq integer, cell5_pci text)";
        this.CREATE_LTE_BAD_CELL_TABLE_V2 = "create table lte_bad_cell_table_v2(id integer primary key autoincrement, freq integer, pci integer, bad_cell_score integer, is_work_day integer, is_peak integer, have_been_optimized integer default 0, update_time_mills integer default 0)";
        this.CREATE_STREAM_MEDIA_DATA_INFO_TABLE_V3 = "create table stream_media_data_info_v3(id integer primary key autoincrement, cid text, gci text unique, rat integer, freq integer, pci integer, rsrp integer, action integer, action_value integer, bitmask integer, datatype integer, scene_type integer, celltype integer, is_work_day integer, is_peak integer, update_time_mills integer default 0)";
        this.CREATE_OFFSET_CELL_OPT_TABLE = "create table offset_cell_opt_table(id integer primary key autoincrement, gci text unique, pci integer, freq integer, rat integer, offset integer, plmn text, phoneid integer)";
        this.CREATE_HOME_STREAM_BAD_CELL_TABLE = "create table home_stream_bad_cell_table(id integer primary key autoincrement, gci text unique, pci integer, freq integer, plmn text, phoneid integer)";
        this.CREATE_NR_BAD_CELL_TABLE_V2 = "create table nr_bad_cell_table_v2(id integer primary key autoincrement, freq integer, pci integer, bad_cell_score integer, is_work_day integer, is_peak integer, have_been_optimized integer default 0, update_time_mills integer default 0)";
        this.CREATE_SMART_NETWORK_GAME_CELL_TABLE = "create table smart_network_cell_table(id integer primary key autoincrement, global_cell_id text unique, rat integer, update_time_mills integer default 0)";
        this.CREATE_RESIDENT_CELLS_TABLE = "create table resident_cells_table(id integer primary key autoincrement, global_cell_id text unique, plmn text, pci integer, freq integer, rat integer, scene integer, subId integer, have_been_optimized integer default 0, store_time integer, call_score integer, data_score integer,data_count integer, call_count integer, rsrp_avg integer, sinr_avg integer, bar_cell_time integer default 0, bar_cell_flag integer default 0, vonr_limit_flag integer default 0)";
        this.CREATE_LOW_POWER_CELLS_TABLE = "create table lower_power_cell_table(id integer primary key autoincrement, global_cell_id text unique, freq integer, rat integer, scene integer, subId integer, rsrp_avg integer, sinr_avg integer, paging_cycle integer, band integer, avg_pusch_tx_power integer, rrc_connect_time integer, cdrx_inactive_time integer, sub_carrier_spacing integer, pusch_tx_slot_cnt integer, store_time integer, paging_score integer default 0, band_score integer default 0, cdrx_score integer default 0, ulduty_score integer default 0, sleep_dataon_score float default 0.0, sleep_dataoff_score float default 0.0,update_time_mills text default '0')";
        this.CREATE_MULTI_ROUTE_BAD_CELL_TABLE = "create table multi_route_bad_cell_table(id integer primary key autoincrement, last_cell_id text unique, current_bad_cell_id text, is_off_work boolean, scene_type integer default 0, update_time_mills integer default 0)";
        this.CREATE_WECHAT_BAD_CELL_TABLE = "create table wechat_bad_cell_table(id integer primary key autoincrement, freq integer, pci integer, rat integer, data_stall_count float, data_stall_duration float, average_data_stall_count float, update_time_mills integer default 0)";
        this.CREATE_WECHAT_VIDEO_ACTION_A3_SKIP_CELL_TABLE = "create table wechat_video_action_A3_skip_cell_table(id integer primary key autoincrement, freq integer, pci integer, update_time_mills integer default 0)";
        this.CREATE_WECHAT_VIDEO_DUBIOUS_CELL = "create table wechat_video_dubious_cell(id integer primary key autoincrement, rat integer, mcc integer, mnc integer, cgi integer,band integer,arfcn integer,pci  integer,update_time_mills integer default 0)";
        this.CREATE_GAME_BAD_CELL_TABLE = "create table game_bad_cell_table(id integer primary key autoincrement, gci text, rat integer, freq integer,pci  integer,scene  integer,data_stall_count float, data_stall_duration float, average_data_stall_count float, data_stall_lag_duration float, average_game_data_lag_ratio float, update_time_mills integer default 0)";
        this.CREATE_GAME_BAD_CELL_MODEM_TABLE = "create table game_cell_from_modem_table(id integer primary key autoincrement, gci text, rat integer, freq integer,pci  integer,scene  integer,data_stall_count float, data_stall_duration float, average_data_stall_count float, data_stall_lag_duration float, average_game_data_lag_ratio float, update_time_mills integer default 0)";
        this.CREATE_GAME_BAD_CELL_AI_TABLE = "create table game_cell_from_ai_table(id integer primary key autoincrement, gci text, rat integer, freq integer,pci  integer,scene  integer,data_stall_count float, data_stall_duration float, average_data_stall_count float, data_stall_lag_duration float, average_game_data_lag_ratio float, update_time_mills integer default 0)";
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        creatAllTables(sQLiteDatabase);
    }

    public void updateActionCells(int i, int i2) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("have_been_optimized", (Integer) 1);
        update("cell_table", contentValues, "freq = ? and pci = ?", new String[]{String.valueOf(i), String.valueOf(i2)});
    }

    private void creatAllTables(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(this.CREATE_TABLE);
        sQLiteDatabase.execSQL(this.CREATE_CELL_ROUTE_TABLE);
        sQLiteDatabase.execSQL(this.CREATE_CELL_ROUTE_TABLE_V2);
        sQLiteDatabase.execSQL(this.CREATE_LTE_BAD_CELL_TABLE_V2);
        sQLiteDatabase.execSQL(this.CREATE_NR_BAD_CELL_TABLE_V2);
        sQLiteDatabase.execSQL(this.CREATE_SMART_NETWORK_GAME_CELL_TABLE);
        sQLiteDatabase.execSQL(this.CREATE_RESIDENT_CELLS_TABLE);
        sQLiteDatabase.execSQL(this.CREATE_MULTI_ROUTE_BAD_CELL_TABLE);
        sQLiteDatabase.execSQL(this.CREATE_WECHAT_BAD_CELL_TABLE);
        sQLiteDatabase.execSQL(this.CREATE_WECHAT_VIDEO_ACTION_A3_SKIP_CELL_TABLE);
        sQLiteDatabase.execSQL(this.CREATE_WECHAT_VIDEO_DUBIOUS_CELL);
        sQLiteDatabase.execSQL(this.CREATE_GAME_BAD_CELL_TABLE);
        sQLiteDatabase.execSQL(this.CREATE_GAME_BAD_CELL_MODEM_TABLE);
        sQLiteDatabase.execSQL(this.CREATE_GAME_BAD_CELL_AI_TABLE);
        sQLiteDatabase.execSQL(this.CREATE_STREAM_MEDIA_DATA_INFO_TABLE_V3);
        sQLiteDatabase.execSQL(this.CREATE_OFFSET_CELL_OPT_TABLE);
        sQLiteDatabase.execSQL(this.CREATE_HOME_STREAM_BAD_CELL_TABLE);
        sQLiteDatabase.execSQL(this.CREATE_LOW_POWER_CELLS_TABLE);
    }

    public List getActionCells() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQuery = query("cell_table", new String[]{"freq", "pci", "rat"}, "have_been_optimized = ?", new String[]{"1"}, null, null, null);
        if (cursorQuery != null) {
            try {
                if (cursorQuery.moveToFirst()) {
                    while (!cursorQuery.isAfterLast()) {
                        CellInfo cellInfo = new CellInfo();
                        cellInfo.setFreq(cursorQuery.getInt(0));
                        cellInfo.setPci(cursorQuery.getInt(1));
                        cellInfo.setRat(cursorQuery.getInt(2));
                        arrayList.add(cellInfo);
                        cursorQuery.moveToNext();
                    }
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
        if (cursorQuery != null) {
            cursorQuery.close();
        }
        return arrayList;
    }

    public long insert(String str, CellInfo cellInfo) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("cid", String.valueOf(cellInfo.getCid()));
        contentValues.put("freq", Integer.valueOf(cellInfo.getFreq()));
        contentValues.put("pci", Integer.valueOf(cellInfo.getPci()));
        contentValues.put("rat", Integer.valueOf(cellInfo.getRat()));
        contentValues.put("scs", Byte.valueOf(cellInfo.getScs()));
        contentValues.put("is_bad_cell", Integer.valueOf(cellInfo.getIsBadCell()));
        contentValues.put("current_cell_count", Integer.valueOf(cellInfo.getCurrentCellCount()));
        contentValues.put("rrc_exception", Integer.valueOf(cellInfo.getRrcExceptionCount()));
        contentValues.put("high_bler_count", Integer.valueOf(cellInfo.getHighBlerCount()));
        contentValues.put("low_ul_grant", Integer.valueOf(cellInfo.getLowUlGrantCount()));
        contentValues.put("rlf_count", Integer.valueOf(cellInfo.getRlfCount()));
        contentValues.put("bad_rsrp_duration", Integer.valueOf(cellInfo.getBadRsrpDuration()));
        contentValues.put("bad_rsrq_duration", Integer.valueOf(cellInfo.getBadRsrqDuration()));
        contentValues.put("duration", Integer.valueOf(cellInfo.getDuration()));
        contentValues.put("low_dl_grant", Integer.valueOf(cellInfo.getLowDlGrantCount()));
        contentValues.put("data_type", Integer.valueOf(cellInfo.getDataType()));
        contentValues.put("have_been_optimized", (Integer) 0);
        long jInsert = writableDatabase.insert(str, null, contentValues);
        writableDatabase.close();
        return jInsert;
    }

    public long insert(String str, ContentValues contentValues) {
        return getWritableDatabase().insert(str, null, contentValues);
    }

    public int update(String str, ContentValues contentValues, String str2, String[] strArr) {
        return getWritableDatabase().update(str, contentValues, str2, strArr);
    }

    public int delete(String str, String str2, String[] strArr) {
        return getWritableDatabase().delete(str, str2, strArr);
    }

    public Cursor query(String str, String[] strArr, String str2, String[] strArr2, String str3, String str4, String str5) {
        return getReadableDatabase().query(str, strArr, str2, strArr2, str3, str4, str5);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        deleteTable(sQLiteDatabase, "cell_table");
        deleteTable(sQLiteDatabase, "cell_route_table");
        deleteTable(sQLiteDatabase, "cell_route_table_v2");
        deleteTable(sQLiteDatabase, "lte_bad_cell_table_v2");
        deleteTable(sQLiteDatabase, "nr_bad_cell_table_v2");
        deleteTable(sQLiteDatabase, "smart_network_cell_table");
        deleteTable(sQLiteDatabase, "resident_cells_table");
        deleteTable(sQLiteDatabase, "multi_route_bad_cell_table");
        deleteTable(sQLiteDatabase, "wechat_bad_cell_table");
        deleteTable(sQLiteDatabase, "wechat_video_action_A3_skip_cell_table");
        deleteTable(sQLiteDatabase, "wechat_video_dubious_cell");
        deleteTable(sQLiteDatabase, "game_bad_cell_table");
        deleteTable(sQLiteDatabase, "game_cell_from_modem_table");
        deleteTable(sQLiteDatabase, "game_cell_from_ai_table");
        deleteTable(sQLiteDatabase, "stream_media_data_info_v3");
        deleteTable(sQLiteDatabase, "offset_cell_opt_table");
        deleteTable(sQLiteDatabase, "home_stream_bad_cell_table");
        deleteTable(sQLiteDatabase, "lower_power_cell_table");
        creatAllTables(sQLiteDatabase);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        onUpgrade(sQLiteDatabase, i, i2);
    }

    public void resetTables() {
        Log.d("SelfStudyDatabaseHelper", "resetTables");
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.execSQL("DROP TABLE IF exists cell_table");
        writableDatabase.execSQL("DROP TABLE IF exists cell_route_table");
        writableDatabase.execSQL("DROP TABLE IF exists cell_route_table_v2");
        writableDatabase.execSQL("DROP TABLE IF exists cell_id_table");
        writableDatabase.execSQL(this.CREATE_TABLE);
        writableDatabase.execSQL(this.CREATE_CELL_ROUTE_TABLE);
        writableDatabase.execSQL(this.CREATE_CELL_ROUTE_TABLE_V2);
        writableDatabase.close();
    }

    public void resetTable(String str) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.execSQL("DROP TABLE IF exists " + str);
        if ("cell_table".equalsIgnoreCase(str)) {
            writableDatabase.execSQL(this.CREATE_TABLE);
        } else if ("cell_route_table".equalsIgnoreCase(str)) {
            writableDatabase.execSQL(this.CREATE_CELL_ROUTE_TABLE);
        } else if ("cell_route_table_v2".equalsIgnoreCase(str)) {
            writableDatabase.execSQL(this.CREATE_CELL_ROUTE_TABLE_V2);
        }
        writableDatabase.close();
    }

    public void deleteTable(SQLiteDatabase sQLiteDatabase, String str) {
        sQLiteDatabase.execSQL("DROP TABLE IF exists " + str);
    }

    public void resetTable(SQLiteDatabase sQLiteDatabase, String str) {
        sQLiteDatabase.execSQL("DROP TABLE IF exists " + str);
        if ("cell_table".equalsIgnoreCase(str)) {
            sQLiteDatabase.execSQL(this.CREATE_TABLE);
            return;
        }
        if ("cell_route_table".equalsIgnoreCase(str)) {
            sQLiteDatabase.execSQL(this.CREATE_CELL_ROUTE_TABLE);
        } else if ("cell_route_table_v2".equalsIgnoreCase(str)) {
            sQLiteDatabase.execSQL(this.CREATE_CELL_ROUTE_TABLE_V2);
        } else if ("lower_power_cell_table".equalsIgnoreCase(str)) {
            sQLiteDatabase.execSQL(this.CREATE_LOW_POWER_CELLS_TABLE);
        }
    }

    public List getDBRouteList(int i) {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQuery = query("cell_route_table_v2", new String[]{"cell1_rat", "cell1_freq", "cell1_pci", "cell1_cid", "cell2_rat", "cell2_freq", "cell2_pci", "cell2_cid", "cell3_rat", "cell3_freq", "cell3_pci", "cell3_cid", "cell4_rat", "cell4_freq", "cell4_pci", "cell4_cid", "cell5_rat", "cell5_freq", "cell5_pci", "cell5_cid"}, null, null, null, null, null);
        if (cursorQuery == null) {
            if (cursorQuery != null) {
                cursorQuery.close();
            }
            return arrayList;
        }
        if (i != -1) {
            try {
                if (cursorQuery.moveToFirst()) {
                    while (!cursorQuery.isAfterLast()) {
                        ArrayList arrayList2 = new ArrayList();
                        SelfStudyControllerBase.CellRouteInfo cellRouteInfo = new SelfStudyControllerBase.CellRouteInfo();
                        cellRouteInfo.cellRat = cursorQuery.getInt(0);
                        cellRouteInfo.cellFreq = cursorQuery.getInt(1);
                        cellRouteInfo.cellPci = cursorQuery.getInt(2);
                        cellRouteInfo.cellId = Long.parseLong(cursorQuery.getString(3));
                        SelfStudyControllerBase.CellRouteInfo cellRouteInfo2 = new SelfStudyControllerBase.CellRouteInfo();
                        cellRouteInfo2.cellRat = cursorQuery.getInt(4);
                        cellRouteInfo2.cellFreq = cursorQuery.getInt(5);
                        cellRouteInfo2.cellPci = decrypt(cursorQuery.getString(6), i);
                        cellRouteInfo2.cellId = Long.parseLong(cursorQuery.getString(7));
                        SelfStudyControllerBase.CellRouteInfo cellRouteInfo3 = new SelfStudyControllerBase.CellRouteInfo();
                        cellRouteInfo3.cellRat = cursorQuery.getInt(8);
                        cellRouteInfo3.cellFreq = cursorQuery.getInt(9);
                        cellRouteInfo3.cellPci = decrypt(cursorQuery.getString(10), i);
                        cellRouteInfo3.cellId = Long.parseLong(cursorQuery.getString(11));
                        SelfStudyControllerBase.CellRouteInfo cellRouteInfo4 = new SelfStudyControllerBase.CellRouteInfo();
                        cellRouteInfo4.cellRat = cursorQuery.getInt(12);
                        cellRouteInfo4.cellFreq = cursorQuery.getInt(13);
                        cellRouteInfo4.cellPci = decrypt(cursorQuery.getString(14), i);
                        cellRouteInfo4.cellId = Long.parseLong(cursorQuery.getString(15));
                        SelfStudyControllerBase.CellRouteInfo cellRouteInfo5 = new SelfStudyControllerBase.CellRouteInfo();
                        cellRouteInfo5.cellRat = cursorQuery.getInt(16);
                        cellRouteInfo5.cellFreq = cursorQuery.getInt(17);
                        cellRouteInfo5.cellPci = decrypt(cursorQuery.getString(18), i);
                        cellRouteInfo5.cellId = Long.parseLong(cursorQuery.getString(19));
                        arrayList2.add(cellRouteInfo);
                        arrayList2.add(cellRouteInfo2);
                        arrayList2.add(cellRouteInfo3);
                        arrayList2.add(cellRouteInfo4);
                        arrayList2.add(cellRouteInfo5);
                        arrayList.add(arrayList2);
                        cursorQuery.moveToNext();
                    }
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
        cursorQuery.close();
        return arrayList;
    }

    public long getCellRouteInfoCount() {
        return getCountFromTable("cell_route_table_v2");
    }

    private long getCountFromTable(String str) {
        Cursor cursorRawQuery = null;
        try {
            cursorRawQuery = getReadableDatabase().rawQuery("select count(id) from " + str, null);
            return (cursorRawQuery == null || !cursorRawQuery.moveToFirst()) ? 0L : cursorRawQuery.getLong(0);
        } finally {
            if (cursorRawQuery != null) {
                cursorRawQuery.close();
            }
        }
    }

    public Cursor getWechatBadCellByPciAndFreq(long j, int i, int i2) {
        String strValueOf = String.valueOf(j);
        return query("wechat_bad_cell_table", new String[]{"id", "data_stall_count", "data_stall_duration"}, "freq = ? and pci = ? and rat = ?", new String[]{String.valueOf(i), String.valueOf(i2), strValueOf}, null, null, null);
    }

    public Cursor getStreamMediaByGci(String str) {
        Cursor cursorQuery = null;
        try {
            cursorQuery = query("stream_media_data_info_v3", new String[]{"id"}, "gci = ?", new String[]{str}, null, null, null);
            if (cursorQuery == null || cursorQuery.getCount() <= 0) {
                return cursorQuery;
            }
            cursorQuery.moveToFirst();
            return cursorQuery;
        } catch (Exception e) {
            e.printStackTrace();
            return cursorQuery;
        }
    }

    public int[] getActionFromStreamMediaByGciAndType(String str, int i) {
        int[] iArr = {-1, -1};
        try {
            Cursor cursorQuery = query("stream_media_data_info_v3", new String[]{"action", "action_value"}, "gci = ? and datatype = ?", new String[]{str, String.valueOf(i)}, null, null, null);
            if (cursorQuery != null) {
                try {
                    if (cursorQuery.moveToFirst()) {
                        iArr[0] = cursorQuery.getInt(0);
                        iArr[1] = cursorQuery.getInt(1);
                        Log.d("SelfStudyDatabaseHelper", "getAction result = " + iArr[0] + " , " + iArr[1]);
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
            if (cursorQuery != null) {
                cursorQuery.close();
            }
            return iArr;
        } catch (Exception e) {
            Log.e("SelfStudyDatabaseHelper", "getAction Error while querying database", e);
            return iArr;
        }
    }

    public void updateStreamMedia(CellInfo cellInfo, int i) {
        if (cellInfo == null) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.clear();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        constructStreamMediaTableContentValues(contentValues, cellInfo);
        writableDatabase.update("stream_media_data_info_v3", contentValues, "id = ? ", new String[]{String.valueOf(i)});
    }

    /* JADX WARN: Code duplicated, block: B:67:0x015a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:87:? A[SYNTHETIC] */
    public void insertStreamMediaData(List list, int i, int i2) throws Throwable {
        Throwable th;
        Cursor cursor;
        SQLiteDatabase writableDatabase;
        StringBuilder sb;
        try {
            writableDatabase = getWritableDatabase();
            try {
                writableDatabase.beginTransaction();
                ContentValues contentValues = new ContentValues();
                int size = list.size();
                ContentValues contentValues2 = contentValues;
                Cursor cursorQuery = writableDatabase.query("stream_media_data_info_v3", new String[]{"id"}, "cellType = ?", new String[]{String.valueOf(i2)}, null, null, "update_time_mills ASC");
                try {
                    int count = cursorQuery.getCount();
                    int iMin = Math.min(i - count, size);
                    StringBuilder sb2 = new StringBuilder("Inserting ");
                    sb2.append(i2 == 1 ? "Resident" : "Dynamic");
                    sb2.append(" stream media data for cell type ");
                    sb2.append(i2);
                    sb2.append(". Existing: ");
                    sb2.append(count);
                    sb2.append(", Can insert: ");
                    sb2.append(iMin);
                    Log.d("SelfStudyDatabaseHelper", sb2.toString());
                    int i3 = 0;
                    while (i3 < iMin) {
                        contentValues2.clear();
                        ContentValues contentValues3 = contentValues2;
                        constructStreamMediaTableContentValues(contentValues3, (CellInfo) list.get(i3));
                        writableDatabase.insert("stream_media_data_info_v3", null, contentValues3);
                        i3++;
                        contentValues2 = contentValues3;
                    }
                    ContentValues contentValues4 = contentValues2;
                    if (iMin < size && cursorQuery.moveToFirst()) {
                        while (iMin < size) {
                            String[] strArr = {String.valueOf(cursorQuery.getInt(0))};
                            contentValues4.clear();
                            constructStreamMediaTableContentValues(contentValues4, (CellInfo) list.get(iMin));
                            writableDatabase.update("stream_media_data_info_v3", contentValues4, "id = ?", strArr);
                            iMin++;
                            if (!cursorQuery.moveToNext()) {
                                break;
                            }
                        }
                    }
                    writableDatabase.setTransactionSuccessful();
                    if (!cursorQuery.isClosed()) {
                        cursorQuery.close();
                    }
                    try {
                        writableDatabase.endTransaction();
                    } catch (Exception e) {
                        e = e;
                        sb = new StringBuilder("Error closing database for cell type ");
                        sb.append(i2);
                        sb.append(": ");
                        sb.append(e.getMessage());
                        Log.e("SelfStudyDatabaseHelper", sb.toString(), e);
                    }
                } catch (Exception e2) {
                    e = e2;
                    cursor = cursorQuery;
                    try {
                        Log.e("SelfStudyDatabaseHelper", "Database operation failed for cell type " + i2 + ": " + e.getMessage(), e);
                        if (cursor != null && !cursor.isClosed()) {
                            cursor.close();
                        }
                        if (writableDatabase != null) {
                            try {
                                writableDatabase.endTransaction();
                            } catch (Exception e3) {
                                e = e3;
                                sb = new StringBuilder("Error closing database for cell type ");
                                sb.append(i2);
                                sb.append(": ");
                                sb.append(e.getMessage());
                                Log.e("SelfStudyDatabaseHelper", sb.toString(), e);
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (cursor != null && !cursor.isClosed()) {
                            cursor.close();
                        }
                        if (writableDatabase != null) {
                            try {
                                writableDatabase.endTransaction();
                                throw th;
                            } catch (Exception e4) {
                                Log.e("SelfStudyDatabaseHelper", "Error closing database for cell type " + i2 + ": " + e4.getMessage(), e4);
                                throw th;
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    cursor = cursorQuery;
                    if (cursor != null) {
                        cursor.close();
                    }
                    if (writableDatabase != null) {
                        writableDatabase.endTransaction();
                        throw th;
                    }
                    throw th;
                }
            } catch (Exception e5) {
                e = e5;
                cursor = null;
            } catch (Throwable th4) {
                th = th4;
                cursor = null;
            }
        } catch (Exception e6) {
            e = e6;
            cursor = null;
            writableDatabase = null;
        } catch (Throwable th5) {
            th = th5;
            cursor = null;
            writableDatabase = null;
        }
    }

    public int getRsrpFormStreamDataTableByGciAndType(String str, int i, int i2, int i3, boolean z) {
        String str2;
        String[] strArr;
        String[] strArr2 = {"rsrp"};
        if (z) {
            String[] strArr3 = {str, String.valueOf(i)};
            str2 = "gci = ? and datatype = ?";
            strArr = strArr3;
        } else {
            String[] strArr4 = {str, String.valueOf(i), String.valueOf(i2), String.valueOf(i3)};
            str2 = "gci = ? and datatype = ? and is_work_day = ? and is_peak = ?";
            strArr = strArr4;
        }
        String str3 = str2;
        int i4 = Integer.MAX_VALUE;
        try {
            Cursor cursorQuery = query("stream_media_data_info_v3", strArr2, str3, strArr, null, null, null);
            if (cursorQuery != null) {
                try {
                    if (cursorQuery.moveToFirst()) {
                        i4 = cursorQuery.getInt(0);
                        Log.d("SelfStudyDatabaseHelper", "getRsrp = " + i4);
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
            if (cursorQuery != null) {
                cursorQuery.close();
            }
            return i4;
        } catch (Exception e) {
            Log.e("SelfStudyDatabaseHelper", "getRsrp Error while querying database", e);
            return i4;
        }
    }

    private void constructStreamMediaTableContentValues(ContentValues contentValues, CellInfo cellInfo) {
        contentValues.clear();
        contentValues.put("cid", Long.valueOf(cellInfo.cid));
        contentValues.put("gci", cellInfo.globalCellId);
        contentValues.put("rat", Integer.valueOf(cellInfo.rat));
        contentValues.put("freq", Integer.valueOf(cellInfo.freq));
        contentValues.put("pci", Integer.valueOf(cellInfo.pci));
        contentValues.put("rsrp", Integer.valueOf(cellInfo.rsrp));
        contentValues.put("action", Integer.valueOf(cellInfo.action));
        contentValues.put("action_value", Integer.valueOf(cellInfo.actionValue));
        contentValues.put("bitmask", Integer.valueOf(cellInfo.bitmask));
        contentValues.put("datatype", Integer.valueOf(cellInfo.dataType));
        contentValues.put("scene_type", Integer.valueOf(cellInfo.sceneDataType));
        contentValues.put("celltype", Integer.valueOf(cellInfo.cellType));
        contentValues.put("is_work_day", Integer.valueOf(cellInfo.isWorkDay));
        contentValues.put("is_peak", Integer.valueOf(cellInfo.isPeak));
        contentValues.put("update_time_mills", Long.valueOf(cellInfo.updateTimeMills));
    }

    public Cursor getOffsetCellByGci(String str) {
        Cursor cursorQuery = null;
        try {
            cursorQuery = query("offset_cell_opt_table", new String[]{"id"}, "gci = ?", new String[]{str}, null, null, null);
            if (cursorQuery == null || cursorQuery.getCount() <= 0) {
                return cursorQuery;
            }
            cursorQuery.moveToFirst();
            return cursorQuery;
        } catch (Exception e) {
            e.printStackTrace();
            return cursorQuery;
        }
    }

    public void insertOffsetCellData(OffsetCell offsetCell, int i) throws Throwable {
        StringBuilder sb;
        SQLiteDatabase sQLiteDatabase = null;
        try {
            try {
                SQLiteDatabase writableDatabase = getWritableDatabase();
                try {
                    writableDatabase.beginTransaction();
                    ContentValues contentValues = new ContentValues();
                    constructOffsetCellTableContentValues(contentValues, offsetCell, i);
                    writableDatabase.insert("offset_cell_opt_table", null, contentValues);
                    writableDatabase.setTransactionSuccessful();
                    try {
                        writableDatabase.endTransaction();
                    } catch (Exception e) {
                        e = e;
                        sb = new StringBuilder("insertOffsetCellData Error closing database: ");
                        sb.append(e.getMessage());
                        Log.e("SelfStudyDatabaseHelper", sb.toString(), e);
                    }
                } catch (Exception e2) {
                    e = e2;
                    sQLiteDatabase = writableDatabase;
                    Log.e("SelfStudyDatabaseHelper", "insertOffsetCellData Database operation failed: " + e.getMessage(), e);
                    if (sQLiteDatabase != null) {
                        try {
                            sQLiteDatabase.endTransaction();
                        } catch (Exception e3) {
                            e = e3;
                            sb = new StringBuilder("insertOffsetCellData Error closing database: ");
                            sb.append(e.getMessage());
                            Log.e("SelfStudyDatabaseHelper", sb.toString(), e);
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    sQLiteDatabase = writableDatabase;
                    if (sQLiteDatabase != null) {
                        try {
                            sQLiteDatabase.endTransaction();
                        } catch (Exception e4) {
                            Log.e("SelfStudyDatabaseHelper", "insertOffsetCellData Error closing database: " + e4.getMessage(), e4);
                        }
                    }
                    throw th;
                }
            } catch (Exception e5) {
                e = e5;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public void updateOffsetCell(OffsetCell offsetCell, int i, int i2) {
        if (offsetCell == null) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        constructOffsetCellTableContentValues(contentValues, offsetCell, i2);
        writableDatabase.update("offset_cell_opt_table", contentValues, "id = ?", new String[]{String.valueOf(i)});
    }

    public List getOffsetCellsForTable() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQuery = null;
        try {
            try {
                cursorQuery = query("offset_cell_opt_table", new String[]{"gci", "pci", "freq", "rat", "offset", "plmn", "phoneid"}, null, null, null, null, null);
                if (cursorQuery != null) {
                    while (cursorQuery.moveToNext()) {
                        arrayList.add(new OffsetCell(cursorQuery.getInt(cursorQuery.getColumnIndex("freq")), cursorQuery.getInt(cursorQuery.getColumnIndex("pci")), cursorQuery.getInt(cursorQuery.getColumnIndex("offset")), cursorQuery.getString(cursorQuery.getColumnIndex("plmn")), cursorQuery.getInt(cursorQuery.getColumnIndex("rat")), cursorQuery.getString(cursorQuery.getColumnIndex("gci"))));
                    }
                }
                if (cursorQuery != null) {
                    return arrayList;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return arrayList;
        } finally {
            if (cursorQuery != null) {
                cursorQuery.close();
            }
        }
    }

    public void clearOffsetCellTable() {
        try {
            Log.i("SelfStudyDatabaseHelper", "Deleted " + getWritableDatabase().delete("offset_cell_opt_table", null, null) + " rows from offset_cell_opt_table");
        } catch (Exception e) {
            Log.e("SelfStudyDatabaseHelper", "Error clearing offset cell table", e);
        }
    }

    private void constructOffsetCellTableContentValues(ContentValues contentValues, OffsetCell offsetCell, int i) {
        contentValues.clear();
        contentValues.put("gci", offsetCell.globalCellId);
        contentValues.put("pci", Integer.valueOf(offsetCell.pci));
        contentValues.put("freq", Integer.valueOf(offsetCell.freq));
        contentValues.put("rat", Integer.valueOf(offsetCell.rat));
        contentValues.put("offset", Integer.valueOf(offsetCell.offset));
        contentValues.put("plmn", offsetCell.plmn);
        contentValues.put("phoneid", Integer.valueOf(i));
    }

    public void insertHomeStreamBadCellData(String str, int i, int i2, int i3, int i4) throws Throwable {
        Throwable th;
        Exception exc;
        Exception exc2;
        StringBuilder sb;
        SQLiteDatabase sQLiteDatabase = null;
        try {
            try {
                SQLiteDatabase writableDatabase = getWritableDatabase();
                try {
                    writableDatabase.beginTransaction();
                    ContentValues contentValues = new ContentValues();
                    constructHomeStreamBadCellTableContentValues(contentValues, str, i, i2, i3, i4);
                    writableDatabase.insert("home_stream_bad_cell_table", null, contentValues);
                    writableDatabase.setTransactionSuccessful();
                    try {
                        writableDatabase.endTransaction();
                    } catch (Exception e) {
                        exc2 = e;
                        sb = new StringBuilder("insertHomeStreamBadCellData Error closing database: ");
                        sb.append(exc2.getMessage());
                        Log.e("SelfStudyDatabaseHelper", sb.toString(), exc2);
                    }
                } catch (Exception e2) {
                    exc = e2;
                    sQLiteDatabase = writableDatabase;
                    Log.e("SelfStudyDatabaseHelper", "insertHomeStreamBadCellData Database operation failed: " + exc.getMessage(), exc);
                    if (sQLiteDatabase != null) {
                        try {
                            sQLiteDatabase.endTransaction();
                        } catch (Exception e3) {
                            exc2 = e3;
                            sb = new StringBuilder("insertHomeStreamBadCellData Error closing database: ");
                            sb.append(exc2.getMessage());
                            Log.e("SelfStudyDatabaseHelper", sb.toString(), exc2);
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    sQLiteDatabase = writableDatabase;
                    if (sQLiteDatabase != null) {
                        try {
                            sQLiteDatabase.endTransaction();
                            throw th;
                        } catch (Exception e4) {
                            Log.e("SelfStudyDatabaseHelper", "insertHomeStreamBadCellData Error closing database: " + e4.getMessage(), e4);
                            throw th;
                        }
                    }
                    throw th;
                }
            } catch (Exception e5) {
                exc = e5;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    public void updateHomeStreamBadCell(String str, int i, int i2, int i3, int i4, int i5) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        constructHomeStreamBadCellTableContentValues(contentValues, str, i, i2, i3, i5);
        writableDatabase.update("home_stream_bad_cell_table", contentValues, "id = ?", new String[]{String.valueOf(i4)});
    }

    public void deleteHomeStreamBadCell(int i) {
        getWritableDatabase().delete("home_stream_bad_cell_table", "id = ?", new String[]{String.valueOf(i)});
    }

    public Cursor getHomeStreamBadCellByGci(String str) {
        Cursor cursorQuery = null;
        try {
            cursorQuery = query("home_stream_bad_cell_table", new String[]{"id"}, "gci = ?", new String[]{str}, null, null, null);
            if (cursorQuery == null || cursorQuery.getCount() <= 0) {
                return cursorQuery;
            }
            cursorQuery.moveToFirst();
            return cursorQuery;
        } catch (Exception e) {
            e.printStackTrace();
            return cursorQuery;
        }
    }

    private void constructHomeStreamBadCellTableContentValues(ContentValues contentValues, String str, int i, int i2, int i3, int i4) {
        contentValues.clear();
        contentValues.put("gci", str);
        contentValues.put("pci", Integer.valueOf(i));
        contentValues.put("freq", Integer.valueOf(i2));
        contentValues.put("action", Integer.valueOf(i3));
        contentValues.put("phoneid", Integer.valueOf(i4));
    }

    public void updateWechatBadCell(float f, float f2, long j, float f3, int i) {
        ContentValues contentValues = new ContentValues();
        contentValues.clear();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        contentValues.put("data_stall_count", Float.valueOf(f));
        contentValues.put("data_stall_duration", Float.valueOf(f2));
        contentValues.put("update_time_mills", Long.valueOf(j));
        contentValues.put("average_data_stall_count", Float.valueOf(f3));
        writableDatabase.update("wechat_bad_cell_table", contentValues, "id = ? ", new String[]{String.valueOf(i)});
    }

    public void periodicUpdateWechatBadCell(float f, float f2, int i) {
        ContentValues contentValues = new ContentValues();
        contentValues.clear();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        contentValues.put("data_stall_count", Float.valueOf(f));
        contentValues.put("data_stall_duration", Float.valueOf(f2));
        writableDatabase.update("wechat_bad_cell_table", contentValues, "id = ? ", new String[]{String.valueOf(i)});
    }

    public void insertWechatBadCell(List list, int i, int i2) throws Throwable {
        Throwable th;
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        int size = list.size();
        Cursor cursor = null;
        try {
            Cursor cursorQuery = writableDatabase.query("wechat_bad_cell_table", new String[]{"id"}, "rat = ?", new String[]{String.valueOf(i)}, null, null, "update_time_mills ASC");
            if (cursorQuery == null) {
                if (cursorQuery != null) {
                    cursorQuery.close();
                    return;
                }
                return;
            }
            try {
                int count = i2 - cursorQuery.getCount();
                int i3 = 0;
                while (i3 < count && i3 < size) {
                    constructWechatBadCellTableContentValues(contentValues, (CellInfo) list.get(i3), i);
                    writableDatabase.insert("wechat_bad_cell_table", null, contentValues);
                    i3++;
                }
                if (cursorQuery.moveToFirst()) {
                    while (i3 < size && !cursorQuery.isAfterLast()) {
                        String[] strArr = {String.valueOf(cursorQuery.getInt(0))};
                        constructWechatBadCellTableContentValues(contentValues, (CellInfo) list.get(i3), i);
                        writableDatabase.update("wechat_bad_cell_table", contentValues, "id = ? ", strArr);
                        i3++;
                        cursorQuery.moveToNext();
                    }
                }
                cursorQuery.close();
                writableDatabase.close();
            } catch (Throwable th2) {
                th = th2;
                cursor = cursorQuery;
                if (cursor != null) {
                    cursor.close();
                    throw th;
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    private void constructWechatBadCellTableContentValues(ContentValues contentValues, CellInfo cellInfo, int i) {
        contentValues.clear();
        contentValues.put("data_stall_count", Float.valueOf(cellInfo.wechatDataStallCount));
        contentValues.put("data_stall_duration", Float.valueOf(cellInfo.wechatDataStallDur));
        float f = cellInfo.wechatDataStallDur;
        contentValues.put("average_data_stall_count", Float.valueOf(f != 0.0f ? cellInfo.wechatDataStallCount / f : 0.0f));
        contentValues.put("update_time_mills", Long.valueOf(cellInfo.updateTimeMills));
        contentValues.put("pci", Integer.valueOf(cellInfo.pci));
        contentValues.put("freq", Integer.valueOf(cellInfo.freq));
        contentValues.put("rat", Integer.valueOf(i));
    }

    public void insertWechatVideoDubiousCells(int i, long j, short s, short s2, int i2, int i3, int i4, long j2) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String[] strArr = {String.valueOf(i3), String.valueOf(i2), String.valueOf(j)};
        contentValues.put("update_time_mills", Long.valueOf(j2));
        contentValues.put("band", Integer.valueOf(i4));
        contentValues.put("mcc", Short.valueOf(s));
        contentValues.put("mnc", Short.valueOf(s2));
        if (writableDatabase.update("wechat_video_dubious_cell", contentValues, "pci = ? and arfcn = ? and cgi = ? ", strArr) > 0) {
            return;
        }
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("rat", Integer.valueOf(i));
        contentValues2.put("mcc", Short.valueOf(s));
        contentValues2.put("mnc", Short.valueOf(s2));
        contentValues2.put("cgi", Long.valueOf(j));
        contentValues2.put("band", Integer.valueOf(i4));
        contentValues2.put("arfcn", Integer.valueOf(i2));
        contentValues2.put("pci", Integer.valueOf(i3));
        contentValues2.put("update_time_mills", Long.valueOf(j2));
        try {
            Cursor cursorQuery = writableDatabase.query("wechat_video_dubious_cell", new String[]{"id"}, "rat = ? ", new String[]{String.valueOf(i)}, null, null, "update_time_mills ASC");
            try {
                if (cursorQuery.getCount() < 32) {
                    writableDatabase.insert("wechat_video_dubious_cell", null, contentValues2);
                } else if (cursorQuery.moveToFirst() && !cursorQuery.isAfterLast()) {
                    writableDatabase.update("wechat_video_dubious_cell", contentValues2, "id = ?", new String[]{String.valueOf(cursorQuery.getInt(0))});
                }
                cursorQuery.close();
            } catch (Throwable th) {
                if (cursorQuery == null) {
                    throw th;
                }
                try {
                    cursorQuery.close();
                    throw th;
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                    throw th;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateWechatOpActionA3SkipCell(int i, int i2, long j) throws Throwable {
        Throwable th;
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("freq", Integer.valueOf(i));
        contentValues.put("pci", Integer.valueOf(i2));
        contentValues.put("update_time_mills", Long.valueOf(j));
        Cursor cursor = null;
        try {
            Cursor cursorQuery = writableDatabase.query("wechat_video_action_A3_skip_cell_table", new String[]{"id"}, null, null, null, null, "update_time_mills ASC");
            if (cursorQuery == null) {
                if (cursorQuery != null) {
                    cursorQuery.close();
                    return;
                }
                return;
            }
            try {
                if (cursorQuery.getCount() < 20) {
                    writableDatabase.insert("wechat_video_action_A3_skip_cell_table", null, contentValues);
                } else {
                    writableDatabase.update("wechat_video_action_A3_skip_cell_table", contentValues, "id = ? ", new String[]{"1"});
                }
                cursorQuery.close();
            } catch (Throwable th2) {
                th = th2;
                cursor = cursorQuery;
                if (cursor != null) {
                    cursor.close();
                    throw th;
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    public boolean isWechatOptActionA3SkipCell(int i, int i2) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor cursorQuery = null;
        try {
            cursorQuery = readableDatabase.query("wechat_video_action_A3_skip_cell_table", new String[]{"id"}, "freq = ? and pci = ?", new String[]{String.valueOf(i), String.valueOf(i2)}, null, null, null);
            return cursorQuery != null && cursorQuery.moveToFirst();
        } finally {
            if (cursorQuery != null) {
                cursorQuery.close();
            }
        }
    }

    public long insertCellRouteInfo(List list, int i) {
        if (i == -1 || list == null || list.size() < 5) {
            return 0L;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("cell1_cid", String.valueOf(((SelfStudyControllerBase.CellRouteInfo) list.get(0)).cellId));
        contentValues.put("cell1_rat", Integer.valueOf(((SelfStudyControllerBase.CellRouteInfo) list.get(0)).cellRat));
        contentValues.put("cell1_freq", Integer.valueOf(((SelfStudyControllerBase.CellRouteInfo) list.get(0)).cellFreq));
        contentValues.put("cell1_pci", Integer.valueOf(((SelfStudyControllerBase.CellRouteInfo) list.get(0)).cellPci));
        contentValues.put("cell2_cid", String.valueOf(((SelfStudyControllerBase.CellRouteInfo) list.get(1)).cellId));
        contentValues.put("cell2_rat", Integer.valueOf(((SelfStudyControllerBase.CellRouteInfo) list.get(1)).cellRat));
        contentValues.put("cell2_freq", Integer.valueOf(((SelfStudyControllerBase.CellRouteInfo) list.get(1)).cellFreq));
        contentValues.put("cell2_pci", encrypt(((SelfStudyControllerBase.CellRouteInfo) list.get(1)).cellPci, i));
        contentValues.put("cell3_cid", String.valueOf(((SelfStudyControllerBase.CellRouteInfo) list.get(2)).cellId));
        contentValues.put("cell3_rat", Integer.valueOf(((SelfStudyControllerBase.CellRouteInfo) list.get(2)).cellRat));
        contentValues.put("cell3_freq", Integer.valueOf(((SelfStudyControllerBase.CellRouteInfo) list.get(2)).cellFreq));
        contentValues.put("cell3_pci", encrypt(((SelfStudyControllerBase.CellRouteInfo) list.get(2)).cellPci, i));
        contentValues.put("cell4_cid", String.valueOf(((SelfStudyControllerBase.CellRouteInfo) list.get(3)).cellId));
        contentValues.put("cell4_rat", Integer.valueOf(((SelfStudyControllerBase.CellRouteInfo) list.get(3)).cellRat));
        contentValues.put("cell4_freq", Integer.valueOf(((SelfStudyControllerBase.CellRouteInfo) list.get(3)).cellFreq));
        contentValues.put("cell4_pci", encrypt(((SelfStudyControllerBase.CellRouteInfo) list.get(3)).cellPci, i));
        contentValues.put("cell5_cid", String.valueOf(((SelfStudyControllerBase.CellRouteInfo) list.get(4)).cellId));
        contentValues.put("cell5_rat", Integer.valueOf(((SelfStudyControllerBase.CellRouteInfo) list.get(4)).cellRat));
        contentValues.put("cell5_freq", Integer.valueOf(((SelfStudyControllerBase.CellRouteInfo) list.get(4)).cellFreq));
        contentValues.put("cell5_pci", encrypt(((SelfStudyControllerBase.CellRouteInfo) list.get(4)).cellPci, i));
        return insert("cell_route_table_v2", contentValues);
    }

    /* JADX WARN: Code duplicated, block: B:15:0x0052  */
    public int getIdFromCellRouteTable(int i, int i2, int i3, int i4) {
        int i5;
        String strValueOf = String.valueOf(i);
        String strEncrypt = encrypt(i2, i4);
        String strValueOf2 = String.valueOf(i3);
        Cursor cursorQuery = query("cell_route_table_v2", new String[]{"id"}, "cell1_rat = ? and cell1_pci = ? and cell1_freq = ?  or cell2_rat = ? and cell2_pci = ? and cell2_freq = ?  or cell3_rat = ? and cell3_pci = ? and cell3_freq = ?  or cell4_rat = ? and cell4_pci = ? and cell4_freq = ?  or cell5_rat = ? and cell5_pci = ? and cell5_freq = ? ", new String[]{strValueOf, String.valueOf(i2), strValueOf2, strValueOf, strEncrypt, strValueOf2, strValueOf, strEncrypt, strValueOf2, strValueOf, strEncrypt, strValueOf2, strValueOf, strEncrypt, strValueOf2}, null, null, null);
        if (cursorQuery != null) {
            try {
                if (cursorQuery.moveToFirst()) {
                    i5 = cursorQuery.getInt(0);
                } else {
                    i5 = -1;
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
        } else {
            i5 = -1;
        }
        if (cursorQuery != null) {
            cursorQuery.close();
        }
        return i5;
    }

    public void updateBadCellRouteIds(Set set, int i) {
        set.clear();
        Cursor cursorQuery = query("cell_table", new String[]{"rat", "pci", "freq"}, "is_bad_cell >= ? ", new String[]{"1"}, null, null, null);
        if (cursorQuery != null) {
            try {
                if (cursorQuery.moveToFirst()) {
                    while (!cursorQuery.isAfterLast()) {
                        int idFromCellRouteTable = getIdFromCellRouteTable(cursorQuery.getInt(0), cursorQuery.getInt(1), cursorQuery.getInt(2), i);
                        if (idFromCellRouteTable != -1) {
                            set.add(Integer.valueOf(idFromCellRouteTable));
                        }
                        cursorQuery.moveToNext();
                    }
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
        if (cursorQuery != null) {
            cursorQuery.close();
        }
    }

    public void updateMiSmartNetworkActionCells(String str, List list, List list2) {
        if (list.size() != list2.size()) {
            return;
        }
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("have_been_optimized", (Integer) 1);
        int size = list.size();
        for (int i = 0; i < size; i++) {
            writableDatabase.update(str, contentValues, "freq = ? and pci = ?", new String[]{String.valueOf(list2.get(i)), String.valueOf(list.get(i))});
        }
        writableDatabase.close();
    }

    public List getMiSmartNetworkActionCells(int i) {
        String str = i == 1 ? "lte_bad_cell_table_v2" : "nr_bad_cell_table_v2";
        ArrayList arrayList = new ArrayList();
        Cursor cursorQuery = query(str, new String[]{"freq", "pci"}, "have_been_optimized = ? ", new String[]{"1"}, null, null, null);
        if (cursorQuery != null) {
            try {
                if (cursorQuery.moveToFirst()) {
                    while (!cursorQuery.isAfterLast()) {
                        arrayList.add(new Pair(Integer.valueOf(cursorQuery.getInt(0)), Integer.valueOf(cursorQuery.getInt(1))));
                        cursorQuery.moveToNext();
                    }
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
        if (cursorQuery != null) {
            cursorQuery.close();
        }
        return arrayList;
    }

    public void resetMiSmartNetworkActionCells() {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        String[] strArr = {"lte_bad_cell_table_v2", "nr_bad_cell_table_v2"};
        for (int i = 0; i < 2; i++) {
            writableDatabase.execSQL("UPDATE " + strArr[i] + " SET have_been_optimized = 0  where have_been_optimized = 1");
        }
        writableDatabase.close();
    }

    public void inertOrUpdateBadCellTable(int i, List list) {
        if (i == 1) {
            insertOrUpdateBadCell("lte_bad_cell_table_v2", list);
        } else {
            if (i != 2) {
                return;
            }
            insertOrUpdateBadCell("nr_bad_cell_table_v2", list);
        }
    }

    public void insertOrUpdateBadCell(String str, List list) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            CellInfo cellInfo = (CellInfo) it.next();
            contentValues.clear();
            contentValues.put("bad_cell_score", Integer.valueOf(cellInfo.isBadCell));
            contentValues.put("update_time_mills", Long.valueOf(cellInfo.updateTimeMills));
            if (writableDatabase.update(str, contentValues, "pci = ? and freq = ? and is_work_day = ? and is_peak = ? ", new String[]{String.valueOf(cellInfo.pci), String.valueOf(cellInfo.freq), String.valueOf(cellInfo.isWorkDay), String.valueOf(cellInfo.isPeak)}) > 0) {
                it.remove();
            }
        }
        int size = list.size();
        Cursor cursorQuery = writableDatabase.query(str, new String[]{"id"}, null, null, null, null, "update_time_mills ASC");
        if (cursorQuery == null) {
            if (cursorQuery != null) {
                cursorQuery.close();
                return;
            }
            return;
        }
        try {
            int count = 64 - cursorQuery.getCount();
            int i = 0;
            while (i < count && i < size) {
                constructBadCellTableContentValues(contentValues, (CellInfo) list.get(i));
                writableDatabase.insert(str, null, contentValues);
                i++;
            }
            if (cursorQuery.moveToFirst()) {
                while (i < size && !cursorQuery.isAfterLast()) {
                    String[] strArr = {String.valueOf(cursorQuery.getInt(0))};
                    constructBadCellTableContentValues(contentValues, (CellInfo) list.get(i));
                    writableDatabase.update(str, contentValues, "id = ? ", strArr);
                    i++;
                    cursorQuery.moveToNext();
                }
            }
            cursorQuery.close();
            writableDatabase.close();
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

    private void constructBadCellTableContentValues(ContentValues contentValues, CellInfo cellInfo) {
        contentValues.clear();
        contentValues.put("bad_cell_score", Integer.valueOf(cellInfo.isBadCell));
        contentValues.put("is_work_day", Integer.valueOf(cellInfo.isWorkDay));
        contentValues.put("is_peak", Integer.valueOf(cellInfo.isPeak));
        contentValues.put("update_time_mills", Long.valueOf(cellInfo.updateTimeMills));
        contentValues.put("pci", Integer.valueOf(cellInfo.pci));
        contentValues.put("freq", Integer.valueOf(cellInfo.freq));
    }

    public void deleteOutOfDateRows(String str, long j) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.delete(str, "update_time_mills < ? ", new String[]{String.valueOf(System.currentTimeMillis() - j)});
        writableDatabase.close();
    }

    public void deleteOutOfDateRowsForStreamMedia(long j, int i) {
        SQLiteDatabase writableDatabase = null;
        try {
            try {
                writableDatabase = getWritableDatabase();
                writableDatabase.beginTransaction();
                writableDatabase.delete("stream_media_data_info_v3", "update_time_mills < ? and celltype = ?", new String[]{String.valueOf(System.currentTimeMillis() - j), String.valueOf(i)});
                writableDatabase.setTransactionSuccessful();
                writableDatabase.endTransaction();
            } catch (Exception e) {
                Log.e("SelfStudyDatabaseHelper", "delete StreamOutOfDate Error:", e);
                if (writableDatabase != null) {
                    writableDatabase.endTransaction();
                }
            }
        } catch (Throwable th) {
            if (writableDatabase != null) {
                writableDatabase.endTransaction();
            }
            throw th;
        }
    }

    public void insertMultiRouteBadCell(String str, String str2, boolean z, long j, int i) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        Cursor cursorRawQuery = writableDatabase.rawQuery("select count(id) from multi_route_bad_cell_table where scene_type = " + i, null);
        int i2 = cursorRawQuery.moveToFirst() ? cursorRawQuery.getInt(0) : 0;
        cursorRawQuery.close();
        if (i2 < 64 || islastCIdInMultiRouteTable(str, i)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("last_cell_id", str);
            contentValues.put("current_bad_cell_id", str2);
            contentValues.put("is_off_work", Integer.valueOf(z ? 1 : 0));
            contentValues.put("scene_type", Integer.valueOf(i));
            contentValues.put("update_time_mills", Long.valueOf(j));
            writableDatabase.insertWithOnConflict("multi_route_bad_cell_table", null, contentValues, 5);
            writableDatabase.close();
        }
    }

    /* JADX WARN: Code duplicated, block: B:14:0x0034  */
    public boolean islastCIdInMultiRouteTable(String str, int i) {
        boolean z;
        Cursor cursorQuery = getReadableDatabase().query("multi_route_bad_cell_table", new String[]{"id"}, "last_cell_id = ? and scene_type = ? ", new String[]{str, String.valueOf(i)}, null, null, null);
        if (cursorQuery != null) {
            try {
                if (cursorQuery.moveToFirst()) {
                    z = true;
                } else {
                    z = false;
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
        } else {
            z = false;
        }
        if (cursorQuery != null) {
            cursorQuery.close();
        }
        return z;
    }

    /* JADX WARN: Code duplicated, block: B:14:0x0034  */
    public boolean isBadCellInMultiRouteTable(String str, int i) {
        boolean z;
        Cursor cursorQuery = getReadableDatabase().query("multi_route_bad_cell_table", new String[]{"id"}, "current_bad_cell_id = ? and is_off_work = ? ", new String[]{str, String.valueOf(i)}, null, null, null);
        if (cursorQuery != null) {
            try {
                if (cursorQuery.moveToFirst()) {
                    z = true;
                } else {
                    z = false;
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
        } else {
            z = false;
        }
        if (cursorQuery != null) {
            cursorQuery.close();
        }
        return z;
    }

    /* JADX WARN: Code duplicated, block: B:14:0x0034  */
    public boolean isPreviousCellOfBadCell(String str, int i) {
        boolean z;
        Cursor cursorQuery = getReadableDatabase().query("multi_route_bad_cell_table", new String[]{"id"}, "last_cell_id = ? and is_off_work = ? ", new String[]{str, String.valueOf(i)}, null, null, null);
        if (cursorQuery != null) {
            try {
                if (cursorQuery.moveToFirst()) {
                    z = true;
                } else {
                    z = false;
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
        } else {
            z = false;
        }
        if (cursorQuery != null) {
            cursorQuery.close();
        }
        return z;
    }

    /* JADX WARN: Code duplicated, block: B:10:0x0036  */
    /* JADX WARN: Code duplicated, block: B:14:0x005d A[Catch: all -> 0x0030, TRY_ENTER, TryCatch #0 {all -> 0x0030, blocks: (B:4:0x0028, B:11:0x0037, B:14:0x005d, B:16:0x0063), top: B:32:0x0028 }] */
    /* JADX WARN: Code duplicated, block: B:16:0x0063 A[Catch: all -> 0x0030, TRY_LEAVE, TryCatch #0 {all -> 0x0030, blocks: (B:4:0x0028, B:11:0x0037, B:14:0x005d, B:16:0x0063), top: B:32:0x0028 }] */
    /* JADX WARN: Code duplicated, block: B:21:0x00d6  */
    /* JADX WARN: Code duplicated, block: B:22:0x00d9 A[Catch: all -> 0x00d3, TRY_LEAVE, TryCatch #1 {all -> 0x00d3, blocks: (B:18:0x0095, B:22:0x00d9), top: B:35:0x005b }] */
    /* JADX WARN: Code duplicated, block: B:24:0x0153  */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r20v0, types: [com.android.phone.performance.SelfStudyDatabaseHelper] */
    /* JADX WARN: Type inference failed for: r20v1, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r20v2 */
    /* JADX WARN: Type inference failed for: r20v3 */
    /* JADX WARN: Type inference failed for: r20v5, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r20v7 */
    /* JADX WARN: Type inference failed for: r20v8 */
    public boolean insertOrUpdateResidentCells(SQLiteDatabase sQLiteDatabase, String str, String str2, int i, int i2, int i3, int i4, int i5, long j, int i6, int i7, int i8, int i9, int i10, int i11) throws Throwable {
        boolean z;
        ?? r20;
        Throwable th;
        String[] strArr = {str2};
        this = sQLiteDatabase.query("resident_cells_table", new String[]{"store_time", "call_score", "data_score", "data_count", "call_count"}, "global_cell_id = ? ", strArr, null, null, null);
        if (this == 0) {
            z = false;
            Log.d("SelfStudyController", "global_cell_id : " + str2 + " exists : " + z);
            if (z) {
                if (this.isAfterLast()) {
                    this = this;
                } else {
                    long j2 = j + this.getLong(0);
                    int i12 = (i6 + this.getInt(1)) / 2;
                    int i13 = (i7 + this.getInt(2)) / 2;
                    int i14 = (i8 + this.getInt(3)) / 2;
                    int i15 = (i9 + this.getInt(4)) / 2;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("global_cell_id", str2);
                    contentValues.put("store_time", Long.valueOf(j2));
                    contentValues.put("call_score", Integer.valueOf(i12));
                    contentValues.put("data_score", Integer.valueOf(i13));
                    contentValues.put("data_count", Integer.valueOf(i14));
                    contentValues.put("call_count", Integer.valueOf(i15));
                    contentValues.put("rsrp_avg", Integer.valueOf(i10));
                    contentValues.put("sinr_avg", Integer.valueOf(i11));
                    sQLiteDatabase.update("resident_cells_table", contentValues, "global_cell_id = ? ", strArr);
                }
                if (this != 0) {
                    this.close();
                }
                return z;
            }
            ContentValues contentValues2 = new ContentValues();
            contentValues2.put("plmn", str);
            contentValues2.put("global_cell_id", str2);
            contentValues2.put("pci", Integer.valueOf(i));
            contentValues2.put("freq", Integer.valueOf(i2));
            contentValues2.put("rat", Integer.valueOf(i3));
            contentValues2.put("scene", Integer.valueOf(i4));
            contentValues2.put("subId", Integer.valueOf(i5));
            contentValues2.put("store_time", Long.valueOf(j));
            contentValues2.put("call_score", Integer.valueOf(i6));
            contentValues2.put("data_score", Integer.valueOf(i7));
            contentValues2.put("data_count", Integer.valueOf(i8));
            contentValues2.put("call_count", Integer.valueOf(i9));
            contentValues2.put("rsrp_avg", Integer.valueOf(i10));
            contentValues2.put("sinr_avg", Integer.valueOf(i11));
            sQLiteDatabase.insert("resident_cells_table", null, contentValues2);
            if (this != 0) {
                this.close();
            }
            return z;
        }
        try {
            if (this.moveToFirst()) {
                z = true;
            } else {
                z = false;
            }
            Log.d("SelfStudyController", "global_cell_id : " + str2 + " exists : " + z);
            try {
                if (z) {
                    if (this.isAfterLast()) {
                        long j3 = j + this.getLong(0);
                        int i16 = (i6 + this.getInt(1)) / 2;
                        int i17 = (i7 + this.getInt(2)) / 2;
                        int i18 = (i8 + this.getInt(3)) / 2;
                        int i19 = (i9 + this.getInt(4)) / 2;
                        ContentValues contentValues3 = new ContentValues();
                        contentValues3.put("global_cell_id", str2);
                        contentValues3.put("store_time", Long.valueOf(j3));
                        contentValues3.put("call_score", Integer.valueOf(i16));
                        contentValues3.put("data_score", Integer.valueOf(i17));
                        contentValues3.put("data_count", Integer.valueOf(i18));
                        contentValues3.put("call_count", Integer.valueOf(i19));
                        contentValues3.put("rsrp_avg", Integer.valueOf(i10));
                        contentValues3.put("sinr_avg", Integer.valueOf(i11));
                        sQLiteDatabase.update("resident_cells_table", contentValues3, "global_cell_id = ? ", strArr);
                    } else {
                        this = this;
                    }
                    if (this != 0) {
                        this.close();
                    }
                    return z;
                }
                ContentValues contentValues4 = new ContentValues();
                contentValues4.put("plmn", str);
                contentValues4.put("global_cell_id", str2);
                contentValues4.put("pci", Integer.valueOf(i));
                contentValues4.put("freq", Integer.valueOf(i2));
                contentValues4.put("rat", Integer.valueOf(i3));
                contentValues4.put("scene", Integer.valueOf(i4));
                contentValues4.put("subId", Integer.valueOf(i5));
                contentValues4.put("store_time", Long.valueOf(j));
                contentValues4.put("call_score", Integer.valueOf(i6));
                contentValues4.put("data_score", Integer.valueOf(i7));
                contentValues4.put("data_count", Integer.valueOf(i8));
                contentValues4.put("call_count", Integer.valueOf(i9));
                contentValues4.put("rsrp_avg", Integer.valueOf(i10));
                contentValues4.put("sinr_avg", Integer.valueOf(i11));
                sQLiteDatabase.insert("resident_cells_table", null, contentValues4);
                if (this != 0) {
                    this.close();
                }
                return z;
            } catch (Throwable th2) {
                th = th2;
                r20 = this;
            }
        } catch (Throwable th3) {
            th = th3;
            r20 = this;
        }
        Throwable th4 = th;
        if (r20 == 0) {
            throw th4;
        }
        try {
            r20.close();
            throw th4;
        } catch (Throwable th5) {
            th4.addSuppressed(th5);
            throw th4;
        }
    }

    public long insertOrUpdatePowerCells(SQLiteDatabase sQLiteDatabase, String str, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, int i18, double d, double d2, String str2) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("global_cell_id", str);
        contentValues.put("freq", Integer.valueOf(i));
        contentValues.put("rat", Integer.valueOf(i2));
        contentValues.put("scene", Integer.valueOf(i3));
        contentValues.put("subId", Integer.valueOf(i4));
        contentValues.put("rsrp_avg", Integer.valueOf(i5));
        contentValues.put("sinr_avg", Integer.valueOf(i6));
        contentValues.put("paging_cycle", Integer.valueOf(i7));
        contentValues.put("band", Integer.valueOf(i8));
        contentValues.put("avg_pusch_tx_power", Integer.valueOf(i9));
        contentValues.put("rrc_connect_time", Integer.valueOf(i10));
        contentValues.put("cdrx_inactive_time", Integer.valueOf(i11));
        contentValues.put("sub_carrier_spacing", Integer.valueOf(i12));
        contentValues.put("pusch_tx_slot_cnt", Integer.valueOf(i13));
        contentValues.put("store_time", Integer.valueOf(i14));
        contentValues.put("paging_score", Integer.valueOf(i15));
        contentValues.put("band_score", Integer.valueOf(i16));
        contentValues.put("cdrx_score", Integer.valueOf(i17));
        contentValues.put("ulduty_score", Integer.valueOf(i18));
        contentValues.put("sleep_dataon_score", Double.valueOf(d));
        contentValues.put("sleep_dataoff_score", Double.valueOf(d2));
        contentValues.put("update_time_mills", str2);
        return sQLiteDatabase.insertWithOnConflict("lower_power_cell_table", null, contentValues, 5);
    }

    public List getResidentCellIds() {
        ArrayList arrayList = new ArrayList();
        try {
            Cursor cursorQuery = getReadableDatabase().query("resident_cells_table", new String[]{"global_cell_id"}, null, null, null, null, null);
            try {
                int columnIndex = cursorQuery.getColumnIndex("global_cell_id");
                if (columnIndex != -1 && cursorQuery.moveToFirst()) {
                    do {
                        String string = cursorQuery.getString(columnIndex);
                        if (string != null) {
                            arrayList.add(string);
                        }
                    } while (cursorQuery.moveToNext());
                }
                cursorQuery.close();
                return arrayList;
            } catch (Throwable th) {
                if (cursorQuery == null) {
                    throw th;
                }
                try {
                    cursorQuery.close();
                    throw th;
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                    throw th;
                }
            }
        } catch (Exception e) {
            Log.e("SelfStudyController", "Database error: " + e.getMessage(), e);
            return new ArrayList();
        }
    }

    /* JADX WARN: Code duplicated, block: B:10:0x0039  */
    public boolean isCurrentCellInResidentTable(SQLiteDatabase sQLiteDatabase, String str) {
        boolean z;
        try {
            Cursor cursorQuery = sQLiteDatabase.query("resident_cells_table", new String[]{"global_cell_id"}, "global_cell_id LIKE ? ESCAPE '\\'", new String[]{"%\\_" + str}, null, null, null, "1");
            if (cursorQuery != null) {
                try {
                    if (cursorQuery.moveToFirst()) {
                        z = true;
                    } else {
                        z = false;
                    }
                } catch (Throwable th) {
                    if (cursorQuery == null) {
                        throw th;
                    }
                    try {
                        cursorQuery.close();
                        throw th;
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                        throw th;
                    }
                }
            } else {
                z = false;
            }
            Log.d("SelfStudyDatabaseHelper", "isCurrentCellInResidentTable found: " + z);
            if (cursorQuery == null) {
                return z;
            }
            cursorQuery.close();
            return z;
        } catch (Exception e) {
            Log.e("SelfStudyDatabaseHelper", "isCurrentCellInResidentTable error", e);
            return false;
        }
    }

    /* JADX WARN: Code duplicated, block: B:14:0x0032  */
    public float getResidentCellAverageDataScore(SQLiteDatabase sQLiteDatabase) {
        float f;
        Cursor cursorRawQuery = sQLiteDatabase.rawQuery("SELECT AVG(data_score)  FROM resident_cells_table where data_score > 0", null);
        if (cursorRawQuery != null) {
            try {
                if (cursorRawQuery.moveToFirst()) {
                    f = cursorRawQuery.getFloat(0);
                    Log.d("SelfStudyDatabaseHelper", "getResidentCellAverageDataScore: averageDataScore: " + f);
                } else {
                    f = 0.0f;
                }
            } catch (Throwable th) {
                try {
                    cursorRawQuery.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } else {
            f = 0.0f;
        }
        if (cursorRawQuery != null) {
            cursorRawQuery.close();
        }
        return f;
    }

    /* JADX WARN: Code duplicated, block: B:14:0x005f  */
    public float getResidentCellAverageCount(SQLiteDatabase sQLiteDatabase, String str) {
        float f;
        Cursor cursorRawQuery = sQLiteDatabase.rawQuery("SELECT AVG(" + str + ")  FROM resident_cells_table where " + (str + " > 0"), null);
        if (cursorRawQuery != null) {
            try {
                if (cursorRawQuery.moveToFirst()) {
                    f = cursorRawQuery.getFloat(0);
                    Log.d("SelfStudyDatabaseHelper", "getResidentCellAverageCount: column : " + str + " averageCount: " + f);
                } else {
                    f = 0.0f;
                }
            } catch (Throwable th) {
                try {
                    cursorRawQuery.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } else {
            f = 0.0f;
        }
        if (cursorRawQuery != null) {
            cursorRawQuery.close();
        }
        return f;
    }

    public List getResidentCellsStatParams(SQLiteDatabase sQLiteDatabase) {
        ArrayList arrayList = new ArrayList();
        int activeDataSubscriptionId = SubscriptionManager.getActiveDataSubscriptionId();
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            int subId = MiuiPhoneUtils.getPhone(i).getSubId();
            if (miui.telephony.SubscriptionManager.isValidSubscriptionId(subId)) {
                boolean z = activeDataSubscriptionId == subId;
                arrayList.add(getResidentCellsStatParamsByScene(sQLiteDatabase, 0, subId, z));
                arrayList.add(getResidentCellsStatParamsByScene(sQLiteDatabase, 1, subId, z));
            }
        }
        return arrayList;
    }

    private Map getResidentCellsStatParamsByScene(SQLiteDatabase sQLiteDatabase, int i, int i2, boolean z) {
        String[] strArr = {String.valueOf(i), String.valueOf(i2)};
        HashMap map = new HashMap();
        Cursor cursorQuery = sQLiteDatabase.query("resident_cells_table", new String[]{"global_cell_id", "store_time", "have_been_optimized"}, " scene = ? and subId = ? ", strArr, null, null, " store_time DESC");
        if (cursorQuery != null) {
            try {
                if (cursorQuery.moveToFirst()) {
                    map.put("globalCellId", cursorQuery.getString(0));
                    map.put("storeTime", Long.valueOf(cursorQuery.getLong(1)));
                    map.put("haveBeenOptimized", Integer.valueOf(cursorQuery.getInt(2)));
                    map.put("scene", Integer.valueOf(i));
                    map.put("isDataPhone", Boolean.valueOf(z));
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
        if (cursorQuery != null) {
            cursorQuery.close();
        }
        return map;
    }

    public List getLowerPowerCellInfoBySubId(SQLiteDatabase sQLiteDatabase, int i, boolean z) {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQuery = null;
        try {
            try {
                cursorQuery = sQLiteDatabase.query("lower_power_cell_table", new String[]{"freq", "update_time_mills"}, (z ? "subId = ? AND sleep_dataon_score > ?" : "subId = ? AND sleep_dataoff_score > ?").concat(" AND rsrp_avg > -110 AND rsrp_avg < 0 AND sinr_avg > -6"), new String[]{String.valueOf(i), "0.0"}, null, null, z ? "sleep_dataon_score DESC" : "sleep_dataoff_score DESC");
                if (cursorQuery != null && cursorQuery.moveToFirst()) {
                    int columnIndex = cursorQuery.getColumnIndex("freq");
                    int columnIndex2 = cursorQuery.getColumnIndex("update_time_mills");
                    do {
                        HashMap map = new HashMap();
                        map.put("freq", Integer.valueOf(cursorQuery.getInt(columnIndex)));
                        map.put("update_time_mills", cursorQuery.getString(columnIndex2));
                        arrayList.add(map);
                    } while (cursorQuery.moveToNext());
                }
                if (cursorQuery != null && !cursorQuery.isClosed()) {
                    cursorQuery.close();
                    return arrayList;
                }
            } catch (Exception e) {
                Log.e("SelfStudyDatabaseHelper", "getLowerPowerCellInfoBySubId error", e);
                if (cursorQuery != null && !cursorQuery.isClosed()) {
                    cursorQuery.close();
                }
            }
            return arrayList;
        } catch (Throwable th) {
            if (cursorQuery != null && !cursorQuery.isClosed()) {
                cursorQuery.close();
                throw th;
            }
            throw th;
        }
    }

    /* JADX WARN: Code duplicated, block: B:22:0x023c  */
    public List getAllLowerPowerCellDetailsBySubId(SQLiteDatabase sQLiteDatabase, int i) throws Throwable {
        ArrayList arrayList;
        ArrayList arrayList2 = new ArrayList();
        Cursor cursor = null;
        try {
            try {
                ArrayList arrayList3 = arrayList2;
                try {
                    Cursor cursorQuery = sQLiteDatabase.query("lower_power_cell_table", new String[]{"rsrp_avg", "sinr_avg", "paging_cycle", "band", "avg_pusch_tx_power", "rrc_connect_time", "cdrx_inactive_time", "sub_carrier_spacing", "pusch_tx_slot_cnt", "store_time", "paging_score", "band_score", "cdrx_score", "ulduty_score", "sleep_dataon_score", "sleep_dataoff_score", "update_time_mills"}, "subId = ?", new String[]{String.valueOf(i)}, null, null, null);
                    if (cursorQuery != null) {
                        try {
                            try {
                                if (cursorQuery.moveToFirst()) {
                                    int columnIndex = cursorQuery.getColumnIndex("rsrp_avg");
                                    Object obj = "rsrp_avg";
                                    int columnIndex2 = cursorQuery.getColumnIndex("sinr_avg");
                                    Object obj2 = "sinr_avg";
                                    int columnIndex3 = cursorQuery.getColumnIndex("paging_cycle");
                                    Object obj3 = "paging_cycle";
                                    int columnIndex4 = cursorQuery.getColumnIndex("band");
                                    Object obj4 = "band";
                                    int columnIndex5 = cursorQuery.getColumnIndex("avg_pusch_tx_power");
                                    Object obj5 = "avg_pusch_tx_power";
                                    int columnIndex6 = cursorQuery.getColumnIndex("rrc_connect_time");
                                    Object obj6 = "rrc_connect_time";
                                    int columnIndex7 = cursorQuery.getColumnIndex("cdrx_inactive_time");
                                    Object obj7 = "cdrx_inactive_time";
                                    int columnIndex8 = cursorQuery.getColumnIndex("sub_carrier_spacing");
                                    Object obj8 = "sub_carrier_spacing";
                                    int columnIndex9 = cursorQuery.getColumnIndex("pusch_tx_slot_cnt");
                                    Object obj9 = "pusch_tx_slot_cnt";
                                    int columnIndex10 = cursorQuery.getColumnIndex("store_time");
                                    Object obj10 = "store_time";
                                    int columnIndex11 = cursorQuery.getColumnIndex("paging_score");
                                    Object obj11 = "paging_score";
                                    int columnIndex12 = cursorQuery.getColumnIndex("band_score");
                                    Object obj12 = "band_score";
                                    int columnIndex13 = cursorQuery.getColumnIndex("cdrx_score");
                                    Object obj13 = "cdrx_score";
                                    Object obj14 = "ulduty_score";
                                    int columnIndex14 = cursorQuery.getColumnIndex("ulduty_score");
                                    Object obj15 = "sleep_dataon_score";
                                    int columnIndex15 = cursorQuery.getColumnIndex("sleep_dataon_score");
                                    Object obj16 = "sleep_dataoff_score";
                                    int columnIndex16 = cursorQuery.getColumnIndex("sleep_dataoff_score");
                                    int columnIndex17 = cursorQuery.getColumnIndex("update_time_mills");
                                    Object obj17 = "update_time_mills";
                                    while (true) {
                                        HashMap map = new HashMap();
                                        int i2 = columnIndex;
                                        int i3 = columnIndex17;
                                        Object obj18 = obj;
                                        map.put(obj18, Integer.valueOf(cursorQuery.getInt(columnIndex)));
                                        Integer numValueOf = Integer.valueOf(cursorQuery.getInt(columnIndex2));
                                        int i4 = columnIndex2;
                                        Object obj19 = obj2;
                                        map.put(obj19, numValueOf);
                                        obj2 = obj19;
                                        Object obj20 = obj3;
                                        map.put(obj20, Integer.valueOf(cursorQuery.getInt(columnIndex3)));
                                        obj3 = obj20;
                                        Object obj21 = obj4;
                                        map.put(obj21, Integer.valueOf(cursorQuery.getInt(columnIndex4)));
                                        obj4 = obj21;
                                        Object obj22 = obj5;
                                        map.put(obj22, Integer.valueOf(cursorQuery.getInt(columnIndex5)));
                                        obj5 = obj22;
                                        Object obj23 = obj6;
                                        map.put(obj23, Integer.valueOf(cursorQuery.getInt(columnIndex6)));
                                        obj6 = obj23;
                                        Object obj24 = obj7;
                                        map.put(obj24, Integer.valueOf(cursorQuery.getInt(columnIndex7)));
                                        obj7 = obj24;
                                        Object obj25 = obj8;
                                        map.put(obj25, Integer.valueOf(cursorQuery.getInt(columnIndex8)));
                                        obj8 = obj25;
                                        Object obj26 = obj9;
                                        map.put(obj26, Integer.valueOf(cursorQuery.getInt(columnIndex9)));
                                        obj9 = obj26;
                                        Object obj27 = obj10;
                                        map.put(obj27, Integer.valueOf(cursorQuery.getInt(columnIndex10)));
                                        obj10 = obj27;
                                        Object obj28 = obj11;
                                        map.put(obj28, Integer.valueOf(cursorQuery.getInt(columnIndex11)));
                                        obj11 = obj28;
                                        Object obj29 = obj12;
                                        map.put(obj29, Integer.valueOf(cursorQuery.getInt(columnIndex12)));
                                        obj12 = obj29;
                                        Object obj30 = obj13;
                                        map.put(obj30, Integer.valueOf(cursorQuery.getInt(columnIndex13)));
                                        int i5 = columnIndex14;
                                        Integer numValueOf2 = Integer.valueOf(cursorQuery.getInt(i5));
                                        columnIndex14 = i5;
                                        Object obj31 = obj14;
                                        map.put(obj31, numValueOf2);
                                        int i6 = columnIndex15;
                                        columnIndex15 = i6;
                                        obj14 = obj31;
                                        Object obj32 = obj15;
                                        map.put(obj32, Float.valueOf(cursorQuery.getFloat(i6)));
                                        int i7 = columnIndex16;
                                        columnIndex16 = i7;
                                        obj15 = obj32;
                                        Object obj33 = obj16;
                                        map.put(obj33, Float.valueOf(cursorQuery.getFloat(i7)));
                                        obj16 = obj33;
                                        Object obj34 = obj17;
                                        map.put(obj34, cursorQuery.getString(i3));
                                        arrayList = arrayList3;
                                        try {
                                            arrayList.add(map);
                                            if (!cursorQuery.moveToNext()) {
                                                break;
                                            }
                                            obj17 = obj34;
                                            arrayList3 = arrayList;
                                            columnIndex2 = i4;
                                            columnIndex = i2;
                                            obj13 = obj30;
                                            obj = obj18;
                                            columnIndex17 = i3;
                                        } catch (Exception e) {
                                            e = e;
                                            cursor = cursorQuery;
                                            Log.e("SelfStudyDatabaseHelper", "getAllLowerPowerCellDetailsBySubId error", e);
                                            if (cursor != null && !cursor.isClosed()) {
                                                cursor.close();
                                            }
                                        }
                                    }
                                } else {
                                    arrayList = arrayList3;
                                }
                            } catch (Throwable th) {
                                th = th;
                                cursor = cursorQuery;
                                if (cursor != null && !cursor.isClosed()) {
                                    cursor.close();
                                }
                                throw th;
                            }
                        } catch (Exception e2) {
                            e = e2;
                            arrayList = arrayList3;
                        }
                    } else {
                        arrayList = arrayList3;
                    }
                    if (cursorQuery != null && !cursorQuery.isClosed()) {
                        cursorQuery.close();
                        return arrayList;
                    }
                } catch (Exception e3) {
                    e = e3;
                    arrayList = arrayList3;
                }
            } catch (Exception e4) {
                e = e4;
                arrayList = arrayList2;
            }
            return arrayList;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public List getResidentCellsInfoBySceneAndRat(SQLiteDatabase sQLiteDatabase, int i, int i2, int i3) {
        ArrayList arrayList;
        String str = "subId";
        ArrayList arrayList2 = new ArrayList();
        Cursor cursorQuery = null;
        try {
            try {
                cursorQuery = sQLiteDatabase.query("resident_cells_table", new String[]{"global_cell_id", "freq", "pci", "rat", "scene", "subId", "rsrp_avg", "sinr_avg"}, "scene = ? AND rat = ? AND subId = ?", new String[]{String.valueOf(i), String.valueOf(i2), String.valueOf(i3)}, null, null, null);
                if (cursorQuery == null || !cursorQuery.moveToFirst()) {
                    arrayList = arrayList2;
                } else {
                    int columnIndex = cursorQuery.getColumnIndex("global_cell_id");
                    int columnIndex2 = cursorQuery.getColumnIndex("freq");
                    int columnIndex3 = cursorQuery.getColumnIndex("pci");
                    int columnIndex4 = cursorQuery.getColumnIndex("rat");
                    int columnIndex5 = cursorQuery.getColumnIndex("scene");
                    int columnIndex6 = cursorQuery.getColumnIndex("subId");
                    ArrayList arrayList3 = arrayList2;
                    try {
                        int columnIndex7 = cursorQuery.getColumnIndex("rsrp_avg");
                        Object obj = "rsrp_avg";
                        int columnIndex8 = cursorQuery.getColumnIndex("sinr_avg");
                        Object obj2 = "sinr_avg";
                        while (true) {
                            HashMap map = new HashMap();
                            int i4 = columnIndex8;
                            map.put("global_cell_id", cursorQuery.getString(columnIndex));
                            map.put("freq", Integer.valueOf(cursorQuery.getInt(columnIndex2)));
                            map.put("pci", Integer.valueOf(cursorQuery.getInt(columnIndex3)));
                            map.put("rat", Integer.valueOf(cursorQuery.getInt(columnIndex4)));
                            map.put("scene", Integer.valueOf(cursorQuery.getInt(columnIndex5)));
                            map.put(str, Integer.valueOf(cursorQuery.getInt(columnIndex6)));
                            String str2 = str;
                            Object obj3 = obj;
                            map.put(obj3, Integer.valueOf(cursorQuery.getInt(columnIndex7)));
                            obj = obj3;
                            Object obj4 = obj2;
                            map.put(obj4, Integer.valueOf(cursorQuery.getInt(i4)));
                            arrayList = arrayList3;
                            try {
                                arrayList.add(map);
                                if (!cursorQuery.moveToNext()) {
                                    break;
                                }
                                arrayList3 = arrayList;
                                obj2 = obj4;
                                str = str2;
                                columnIndex8 = i4;
                            } catch (Exception e) {
                                e = e;
                                Log.e("SelfStudyDatabaseHelper", "getResidentCellsInfoBySceneAndRat error", e);
                                if (cursorQuery != null && !cursorQuery.isClosed()) {
                                    cursorQuery.close();
                                }
                            }
                        }
                    } catch (Exception e2) {
                        e = e2;
                        arrayList = arrayList3;
                    }
                }
                if (cursorQuery != null && !cursorQuery.isClosed()) {
                    cursorQuery.close();
                    return arrayList;
                }
            } catch (Throwable th) {
                if (0 != 0 && !cursorQuery.isClosed()) {
                    cursorQuery.close();
                }
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            arrayList = arrayList2;
        }
        return arrayList;
    }

    public void deleteOldResidentCells(SQLiteDatabase sQLiteDatabase, List list, int i, int i2) {
        String[] strArr = new String[3];
        strArr[1] = String.valueOf(i);
        strArr[2] = String.valueOf(i2);
        Cursor cursorQuery = sQLiteDatabase.query("resident_cells_table", new String[]{"global_cell_id"}, null, null, null, null, null);
        if (cursorQuery != null) {
            try {
                if (cursorQuery.moveToFirst()) {
                    while (!cursorQuery.isAfterLast()) {
                        String string = cursorQuery.getString(0);
                        if (!list.contains(string)) {
                            strArr[0] = string;
                            sQLiteDatabase.delete("resident_cells_table", "global_cell_id = ? and scene = ? and subId = ? ", strArr);
                        }
                        cursorQuery.moveToNext();
                    }
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
        if (cursorQuery != null) {
            cursorQuery.close();
        }
    }

    public void updateActionResidentCells(SQLiteDatabase sQLiteDatabase, String str, int i) {
        if (sQLiteDatabase == null || !sQLiteDatabase.isOpen()) {
            sQLiteDatabase = getWritableDatabase();
        }
        String[] strArr = {str};
        ContentValues contentValues = new ContentValues();
        contentValues.put("have_been_optimized", Integer.valueOf(i));
        sQLiteDatabase.update("resident_cells_table", contentValues, "global_cell_id = ? ", strArr);
    }

    public void updateBarCellOrVonrLimitFlagForResidentCells(SQLiteDatabase sQLiteDatabase, String str, int i, long j, String str2, boolean z) {
        String[] strArr;
        String str3;
        try {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            if (sQLiteDatabase == null) {
                sQLiteDatabase = getWritableDatabase();
            }
            Log.d("SelfStudyDatabaseHelper", "updateBarCellOrVonrLimitFlagForResidentCells: globalCellId = " + str + " , flag = " + i + " , updateTime = " + j + " , event = " + str2 + " , isReset = " + z);
            ContentValues contentValues = new ContentValues();
            if (z) {
                if ("vonrLimit".equalsIgnoreCase(str2)) {
                    contentValues.put("vonr_limit_flag", (Integer) 0);
                } else if (!"barCell".equalsIgnoreCase(str2)) {
                    return;
                } else {
                    contentValues.put("bar_cell_flag", (Integer) 0);
                }
                str3 = null;
                strArr = null;
            } else {
                String[] strArr2 = {str};
                if ("barCell".equalsIgnoreCase(str2)) {
                    if (i == 1) {
                        contentValues.put("bar_cell_time", Long.valueOf(j));
                    }
                    contentValues.put("bar_cell_flag", Integer.valueOf(i));
                } else if (!"vonrLimit".equalsIgnoreCase(str2)) {
                    return;
                } else {
                    contentValues.put("vonr_limit_flag", Integer.valueOf(i));
                }
                strArr = strArr2;
                str3 = "global_cell_id = ?";
            }
            if (contentValues.size() == 0) {
                return;
            }
            sQLiteDatabase.update("resident_cells_table", contentValues, str3, strArr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void statisticsResidentCellsInfo(SQLiteDatabase sQLiteDatabase, int i, int i2, int i3) {
        String[] strArr = {String.valueOf(i), String.valueOf(i2), String.valueOf(i3)};
        String[] strArr2 = new String[1];
        Cursor cursorQuery = sQLiteDatabase.query("resident_cells_table", new String[]{"global_cell_id"}, " scene = ? and rat = ? and subId = ? ", strArr, null, null, " store_time DESC");
        if (cursorQuery != null) {
            try {
                if (cursorQuery.moveToFirst()) {
                    for (int i4 = 4; !cursorQuery.isAfterLast() && i4 > 0; i4--) {
                        cursorQuery.moveToNext();
                    }
                    while (!cursorQuery.isAfterLast()) {
                        strArr2[0] = cursorQuery.getString(0);
                        sQLiteDatabase.delete("resident_cells_table", " global_cell_id = ? ", strArr2);
                        cursorQuery.moveToNext();
                    }
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
        if (cursorQuery != null) {
            cursorQuery.close();
        }
    }

    private void constructResidentGameCells(ContentValues contentValues, CellInfo cellInfo, int i, int i2) {
        float f;
        contentValues.clear();
        contentValues.put("data_stall_count", Float.valueOf(cellInfo.gameDataStallCount));
        contentValues.put("data_stall_duration", Float.valueOf(cellInfo.gameDataStallDur));
        float f2 = cellInfo.gameDataStallDur;
        float f3 = 0.0f;
        if (f2 != 0.0f) {
            f3 = cellInfo.gameDataStallCount / f2;
            f = cellInfo.gameDataStallLagDur / f2;
        } else {
            f = 0.0f;
        }
        contentValues.put("average_data_stall_count", Float.valueOf(f3));
        contentValues.put("data_stall_lag_duration", Float.valueOf(cellInfo.gameDataStallLagDur));
        contentValues.put("average_game_data_lag_ratio", Float.valueOf(f));
        contentValues.put("update_time_mills", Long.valueOf(cellInfo.updateTimeMills));
        contentValues.put("pci", Integer.valueOf(cellInfo.pci));
        contentValues.put("freq", Integer.valueOf(cellInfo.freq));
        contentValues.put("rat", Integer.valueOf(i));
        contentValues.put("gci", cellInfo.globalCellId);
        contentValues.put("scene", Integer.valueOf(i2));
    }

    public void insertGameBadCell(List list, int i, int i2, int i3, String str) {
        String str2;
        Cursor cursor;
        int i4;
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if ("AP".equalsIgnoreCase(str)) {
            str2 = "game_bad_cell_table";
        } else if ("MODEM".equalsIgnoreCase(str)) {
            str2 = "game_cell_from_modem_table";
        } else {
            str2 = "game_cell_from_ai_table";
        }
        Log.d("SelfStudyDatabaseHelper", "insertGameBadCell: table = " + str);
        list.size();
        String[] strArr = {"id"};
        String[] strArr2 = {String.valueOf(i), String.valueOf(i2)};
        Iterator it = list.iterator();
        while (true) {
            cursor = null;
            if (!it.hasNext()) {
                break;
            }
            constructResidentGameCells(contentValues, (CellInfo) it.next(), i, i2);
            writableDatabase.insert(str2, null, contentValues);
        }
        String str3 = str2;
        try {
            Cursor cursorQuery = writableDatabase.query(str3, strArr, "rat = ? and scene = ?", strArr2, null, null, "update_time_mills ASC");
            if (cursorQuery == null) {
                if (cursorQuery != null) {
                    cursorQuery.close();
                    return;
                }
                return;
            }
            int count = cursorQuery.getCount();
            if (count > i3 && (i4 = count - i3) > 0 && cursorQuery.moveToFirst()) {
                do {
                    writableDatabase.delete(str3, "id = ?", new String[]{String.valueOf(cursorQuery.getInt(0))});
                    if (!cursorQuery.moveToNext()) {
                        break;
                    } else {
                        i4--;
                    }
                } while (i4 > 0);
            }
            cursorQuery.close();
        } catch (Throwable th) {
            if (0 != 0) {
                cursor.close();
                throw th;
            }
            throw th;
        }
    }

    public Cursor getGameBadCellByPciAndFreq(long j, int i, int i2, String str, int i3, String str2) {
        try {
            String[] strArr = {"id", "data_stall_count", "data_stall_duration", "data_stall_lag_duration"};
            String[] strArr2 = {String.valueOf(i), String.valueOf(i2), String.valueOf(j), String.valueOf(str), String.valueOf(i3)};
            if ("AP".equalsIgnoreCase(str2)) {
                return query("game_bad_cell_table", strArr, "freq = ? and pci = ? and rat = ? and gci = ? and scene = ?", strArr2, null, null, null);
            }
            return "MODEM".equalsIgnoreCase(str2) ? query("game_cell_from_modem_table", strArr, "freq = ? and pci = ? and rat = ? and gci = ? and scene = ?", strArr2, null, null, null) : query("game_cell_from_ai_table", strArr, "freq = ? and pci = ? and rat = ? and gci = ? and scene = ?", strArr2, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateGameBadCell(float f, float f2, float f3, long j, float f4, float f5, int i, String str) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.clear();
            SQLiteDatabase writableDatabase = getWritableDatabase();
            contentValues.put("data_stall_count", Float.valueOf(f));
            contentValues.put("data_stall_duration", Float.valueOf(f2));
            contentValues.put("data_stall_lag_duration", Float.valueOf(f3));
            contentValues.put("update_time_mills", Long.valueOf(j));
            contentValues.put("average_data_stall_count", Float.valueOf(f4));
            contentValues.put("average_game_data_lag_ratio", Float.valueOf(f5));
            String[] strArr = {String.valueOf(i)};
            if ("AP".equalsIgnoreCase(str)) {
                writableDatabase.update("game_bad_cell_table", contentValues, "id = ? ", strArr);
            } else if ("MODEM".equalsIgnoreCase(str)) {
                writableDatabase.update("game_cell_from_modem_table", contentValues, "id = ? ", strArr);
            } else {
                writableDatabase.update("game_cell_from_ai_table", contentValues, "id = ? ", strArr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int decrypt(String str, int i) {
        int i2 = Integer.parseInt(str);
        return i2 == 0 ? i2 : i2 ^ i;
    }

    private String encrypt(int i, int i2) {
        if (i != 0) {
            i ^= i2;
        }
        return String.valueOf(i);
    }
}
