package com.android.phone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;
import android.util.Pair;
import android.util.Xml;
import com.android.phone.performance.PreferenceUtils;
import com.android.phone.signalMap.Point2D;
import com.ot.pubsub.util.v;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import miui.telephony.PhoneDebug;
import org.xmlpull.v1.XmlPullParser;

/* JADX INFO: loaded from: classes.dex */
public class CloudCellDbHelper extends SQLiteOpenHelper {
    private static CloudCellDbHelper mInstance;
    private Context mContext;
    private final String mDataDownloadDir;
    private static final String[] SIGNALMAP_COLUMNS = {"sublocality", "rat", "plmn", "cid", "band", "arfcn", "pci", "tac", "cause", "city"};
    private static final String[] SIGNALMAP_HST_COLUMNS = {"railtrip", "rat", "plmn", "cid", "band", "arfcn", "pci", "tac", "score", "sequences"};
    private static final String[] SIGNALMAP_HST_PREFER_CELL_COLUMNS = {"railtrip", "direction", "rat", "plmn", "cid", "band", "arfcn", "pci", "score", "nextrat", "nextplmn", "nextcid", "nextband", "nextarfcn", "nextpci", "nextscore"};
    private static final String[] SIGNALMAP_SUBWAY_COLUMNS = {"city", "rat", "plmn", "cid", "band", "arfcn", "pci", "tac"};
    private static final String[] SIGNALMAP_NOML_COLUMNS = {"nomlname", "nomltype", "city", "plmn", "lac", "cid"};
    private static final String[] SIGNALMAP_HST_FREQUENCY_COLUMNS = {"railtrip", "operator", "rat", "band", "arfcn", "mcc", "mnc"};
    private static final Map sTypePrefixMap = Map.of(0, "celldb_", 1, "cellmap_", 2, "cellmap_manu_");
    private static final String[] CLOUD_PUSH_COLUMNS = {"_id", "tac", "plmn", "rat", "freq", "pci", "bTime", "eTime", "exp_time", "action", "args"};

    public CloudCellDbHelper(Context context) {
        super(context, "mi_cells.db", (SQLiteDatabase.CursorFactory) null, 36);
        this.mDataDownloadDir = getCellDbDir(context);
        this.mContext = PhoneGlobals.getInstance();
    }

    public static synchronized CloudCellDbHelper getInstance(Context context) {
        try {
            if (mInstance == null) {
                mInstance = new CloudCellDbHelper(context);
            }
        } catch (Throwable th) {
            throw th;
        }
        return mInstance;
    }

    public static String getCellDbDir(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append(context.getFilesDir().getAbsolutePath());
        String str = File.separator;
        sb.append(str);
        sb.append("CloudCell");
        sb.append(str);
        return sb.toString();
    }

    private static void log(String str) {
        Log.d("CloudCellDbHelper", str);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE SignalMapCells (_id INTEGER PRIMARY KEY AUTOINCREMENT,sublocality TEXT,rat INTEGER,plmn INTEGER,cid TEXT,band TEXT,arfcn TEXT,pci INTEGER,tac TEXT,cause INTEGER,city TEXT);");
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS SignalMapHstCells (_id INTEGER PRIMARY KEY AUTOINCREMENT,railtrip TEXT,rat INTEGER,plmn INTEGER,cid TEXT,band TEXT,arfcn TEXT,pci INTEGER,tac TEXT,score INTEGER,sequences INTEGER)");
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS SignalMapHstPreferCells (_id INTEGER PRIMARY KEY AUTOINCREMENT,railtrip TEXT,direction INTEGER,rat INTEGER,plmn INTEGER,cid TEXT,band TEXT,arfcn TEXT,pci INTEGER,score INTEGER,nextrat INTEGER,nextplmn INTEGER,nextcid TEXT,nextband TEXT,nextarfcn TEXT,nextpci INTEGER,nextscore INTEGER)");
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS SignalMapSubwayCells (_id INTEGER PRIMARY KEY AUTOINCREMENT,city TEXT,rat INTEGER,plmn INTEGER,cid TEXT,band TEXT,arfcn TEXT,pci INTEGER,tac TEXT)");
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS SignalMapNOMLCells (_id INTEGER PRIMARY KEY AUTOINCREMENT,nomlname TEXT,nomltype TEXT,city TEXT,plmn INTEGER,lac INTEGER,cid TEXT)");
        sQLiteDatabase.execSQL("CREATE INDEX IF NOT EXISTS idx_noml_cid_type ON SignalMapNOMLCells(cid,nomltype);");
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS SignalMapNOMLGPS (_id INTEGER PRIMARY KEY AUTOINCREMENT,nomlname TEXT,lon REAL,lat REAL)");
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS SignalMapHstFrequency (_id INTEGER PRIMARY KEY AUTOINCREMENT,railtrip TEXT,operator TEXT,rat INTEGER,band TEXT,arfcn TEXT,mcc TEXT,mnc TEXT)");
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS cellmap (_id INTEGER PRIMARY KEY AUTOINCREMENT, scene TEXT, sub_scene TEXT, city TEXT, gci TEXT, tac TEXT, freq INTEGER, pci INTEGER, rat INTEGER, prev_gci TEXT, neigh_gci TEXT, service TEXT, reason TEXT, abnormal_period TEXT, update_time TEXT, prj_type TEXT);");
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS ComDataBlacklist (_id INTEGER PRIMARY KEY AUTOINCREMENT,gci TEXT, act INTEGER, launch_time INTEGER, done_time INTEGER);");
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS cellmap_manual (_id INTEGER PRIMARY KEY AUTOINCREMENT, gci1 TEXT, freq1 INTEGER, pci1 INTEGER, rsrp1 INTEGER, sinr1 INTEGER, rat1 INTEGER, service1 INTEGER, action1 INTEGER, gci2 TEXT, rsrp2 INTEGER, sinr2 INTEGER, freq2 INTEGER, rat2 INTEGER, pci2 INTEGER, service2 INTEGER, action2 INTEGER, type INTEGER, time_extra INTEGER, update_time INTEGER);");
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS CloudPush (_id INTEGER PRIMARY KEY AUTOINCREMENT,cid TEXT UNIQUE ON CONFLICT REPLACE,tac INTEGER,plmn TEXT,rat INTEGER,freq INTEGER,pci INTEGER,exp_time INTEGER,bTime TEXT,eTime TEXT,action INTEGER,args TEXT);");
    }

    public void clearSignalMapTable(SQLiteDatabase sQLiteDatabase) {
        log("clearSignalMapTable");
        sQLiteDatabase.delete("SignalMapCells", null, null);
    }

    public void clearSignalMapHstTable(SQLiteDatabase sQLiteDatabase) {
        log("clearSignalMapHstTable");
        sQLiteDatabase.delete("SignalMapHstCells", null, null);
    }

    public void clearSignalMapNOMLTable(SQLiteDatabase sQLiteDatabase) {
        log("clearSignalMapNOMLTable");
        sQLiteDatabase.delete("SignalMapNOMLCells", null, null);
        sQLiteDatabase.delete("SignalMapNOMLGPS", null, null);
    }

    public boolean readAndLoadDefaultHstFrequency() throws Throwable {
        InputStream inputStream = null;
        try {
            try {
                InputStream inputStreamOpen = this.mContext.getResources().getAssets().open("sm_hst_frequency.xml");
                try {
                    XmlPullParser xmlPullParserNewPullParser = Xml.newPullParser();
                    xmlPullParserNewPullParser.setInput(inputStreamOpen, "UTF-8");
                    SQLiteDatabase writableDatabase = getWritableDatabase();
                    writableDatabase.beginTransaction();
                    try {
                        writableDatabase.execSQL("DROP TABLE IF EXISTS SignalMapHstFrequency");
                        writableDatabase.execSQL("CREATE TABLE IF NOT EXISTS SignalMapHstFrequency (_id INTEGER PRIMARY KEY AUTOINCREMENT,railtrip TEXT,operator TEXT,rat INTEGER,band TEXT,arfcn TEXT,mcc TEXT,mnc TEXT)");
                        ContentValues contentValues = null;
                        String name = null;
                        for (int eventType = xmlPullParserNewPullParser.getEventType(); eventType != 1; eventType = xmlPullParserNewPullParser.next()) {
                            if (eventType == 2) {
                                name = xmlPullParserNewPullParser.getName();
                                if ("hstfreq".equals(name)) {
                                    contentValues = new ContentValues();
                                }
                            } else if (eventType == 3) {
                                if ("hstfreq".equals(xmlPullParserNewPullParser.getName()) && contentValues != null) {
                                    writableDatabase.insert("SignalMapHstFrequency", null, contentValues);
                                    contentValues = null;
                                }
                                name = null;
                            } else if (eventType == 4 && contentValues != null && name != null) {
                                String strTrim = xmlPullParserNewPullParser.getText().trim();
                                if (!strTrim.isEmpty()) {
                                    try {
                                        switch (name.hashCode()) {
                                            case -500553564:
                                                if (name.equals("operator")) {
                                                    contentValues.put("operator", strTrim);
                                                }
                                                break;
                                            case 107917:
                                                if (name.equals("mcc")) {
                                                    contentValues.put("mcc", strTrim);
                                                }
                                                break;
                                            case 108258:
                                                if (name.equals("mnc")) {
                                                    contentValues.put("mnc", strTrim);
                                                }
                                                break;
                                            case 112677:
                                                if (name.equals("rat")) {
                                                    contentValues.put("rat", Integer.valueOf(Integer.parseInt(strTrim)));
                                                }
                                                break;
                                            case 3016245:
                                                if (name.equals("band")) {
                                                    contentValues.put("band", strTrim);
                                                }
                                                break;
                                            case 93078912:
                                                if (name.equals("arfcn")) {
                                                    contentValues.put("arfcn", strTrim);
                                                }
                                                break;
                                            case 114796215:
                                                if (name.equals("railtrip")) {
                                                    contentValues.put("railtrip", strTrim);
                                                }
                                                break;
                                        }
                                    } catch (NumberFormatException unused) {
                                        log("readAndLoadDefaultHstFrequency parse error: " + name + "=" + strTrim);
                                        contentValues = null;
                                    }
                                }
                            }
                        }
                        writableDatabase.setTransactionSuccessful();
                        writableDatabase.endTransaction();
                        if (inputStreamOpen != null) {
                            try {
                                inputStreamOpen.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return true;
                    } catch (Throwable th) {
                        writableDatabase.endTransaction();
                        throw th;
                    }
                } catch (Exception e2) {
                    e = e2;
                    inputStream = inputStreamOpen;
                    e.printStackTrace();
                    if (inputStream == null) {
                        return false;
                    }
                    try {
                        inputStream.close();
                        return false;
                    } catch (IOException e3) {
                        e3.printStackTrace();
                        return false;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    inputStream = inputStreamOpen;
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Exception e5) {
                e = e5;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    public Set getAllHstFrequencyLines() {
        HashSet hashSet = new HashSet();
        Cursor cursorRawQuery = null;
        try {
            try {
                try {
                    SQLiteDatabase readableDatabase = getReadableDatabase();
                    if (readableDatabase != null) {
                        cursorRawQuery = readableDatabase.rawQuery("SELECT DISTINCT railtrip FROM SignalMapHstFrequency", null);
                        if (cursorRawQuery != null) {
                            while (cursorRawQuery.moveToNext()) {
                                String string = cursorRawQuery.getString(0);
                                if (string != null) {
                                    hashSet.add(string);
                                }
                            }
                        }
                        if (cursorRawQuery != null) {
                            cursorRawQuery.close();
                            return hashSet;
                        }
                    }
                } catch (Throwable th) {
                    if (cursorRawQuery != null) {
                        try {
                            cursorRawQuery.close();
                        } catch (Exception unused) {
                        }
                    }
                    throw th;
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (cursorRawQuery != null) {
                    cursorRawQuery.close();
                }
            }
        } catch (Exception unused2) {
        }
        return hashSet;
    }

    public Cursor getHstFrequencyData(String str, String str2, int i) {
        if (str != null && str2 != null) {
            try {
                SQLiteDatabase readableDatabase = getReadableDatabase();
                if (readableDatabase == null) {
                    return null;
                }
                return readableDatabase.query("SignalMapHstFrequency", SIGNALMAP_HST_FREQUENCY_COLUMNS, "railtrip=? AND operator=? AND rat=?", new String[]{str, str2, String.valueOf(i)}, null, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        log("onUpgrade from " + i + " to " + i2);
        if (i < 33) {
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS cellmap");
            sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS cellmap (_id INTEGER PRIMARY KEY AUTOINCREMENT, scene TEXT, sub_scene TEXT, city TEXT, gci TEXT, tac TEXT, freq INTEGER, pci INTEGER, rat INTEGER, prev_gci TEXT, neigh_gci TEXT, service TEXT, reason TEXT, abnormal_period TEXT, update_time TEXT, prj_type TEXT);");
        }
        if (i < 34) {
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS ComDataBlacklist");
            sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS ComDataBlacklist (_id INTEGER PRIMARY KEY AUTOINCREMENT,gci TEXT, act INTEGER, launch_time INTEGER, done_time INTEGER);");
        }
        if (i < 35) {
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS OTACells");
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS LocalCells");
            sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS cellmap_manual (_id INTEGER PRIMARY KEY AUTOINCREMENT, gci1 TEXT, freq1 INTEGER, pci1 INTEGER, rsrp1 INTEGER, sinr1 INTEGER, rat1 INTEGER, service1 INTEGER, action1 INTEGER, gci2 TEXT, rsrp2 INTEGER, sinr2 INTEGER, freq2 INTEGER, rat2 INTEGER, pci2 INTEGER, service2 INTEGER, action2 INTEGER, type INTEGER, time_extra INTEGER, update_time INTEGER);");
            sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS CloudPush (_id INTEGER PRIMARY KEY AUTOINCREMENT,cid TEXT UNIQUE ON CONFLICT REPLACE,tac INTEGER,plmn TEXT,rat INTEGER,freq INTEGER,pci INTEGER,exp_time INTEGER,bTime TEXT,eTime TEXT,action INTEGER,args TEXT);");
        }
        if (i < 36) {
            sQLiteDatabase.execSQL("CREATE INDEX IF NOT EXISTS idx_noml_cid_type ON SignalMapNOMLCells(cid,nomltype);");
            sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS SignalMapNOMLGPS (_id INTEGER PRIMARY KEY AUTOINCREMENT,nomlname TEXT,lon REAL,lat REAL)");
            sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS SignalMapHstFrequency (_id INTEGER PRIMARY KEY AUTOINCREMENT,railtrip TEXT,operator TEXT,rat INTEGER,band TEXT,arfcn TEXT,mcc TEXT,mnc TEXT)");
        }
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        Log.d("CloudCellDbHelper", "onDowngrade newVer:" + i2 + " oldVer:" + i);
    }

    private File getCloudPushFile(String str, String str2) {
        File[] fileArrListFiles = new File(str).listFiles();
        File file = null;
        if (fileArrListFiles != null && fileArrListFiles.length >= 1) {
            for (File file2 : fileArrListFiles) {
                String name = file2.getName();
                if (name != null && name.matches(str2)) {
                    file = file2;
                    break;
                }
            }
            if (PhoneDebug.VDBG) {
                log("getCloudPushFile:" + file);
            }
        }
        return file;
    }

    private Pair getSigmapNewestFile(String str, String str2, String str3) {
        int intPreference = PreferenceUtils.getInstance().getIntPreference(str3);
        File[] fileArrListFiles = new File(str).listFiles();
        File file = null;
        if (fileArrListFiles == null || fileArrListFiles.length < 1) {
            return null;
        }
        Pattern patternCompile = Pattern.compile("\\d+");
        for (File file2 : fileArrListFiles) {
            String name = file2.getName();
            if (name != null && name.matches(str2)) {
                Matcher matcher = patternCompile.matcher(name);
                if (matcher.find()) {
                    intPreference = Integer.parseInt(matcher.group());
                    file = file2;
                }
            }
        }
        log("current cell ver:" + intPreference);
        return new Pair(Integer.valueOf(intPreference), file);
    }

    public boolean readAndLoadCloudPush() throws Throwable {
        boolean z;
        FileInputStream fileInputStream;
        Throwable th;
        String str;
        String str2 = "action";
        String str3 = "cid";
        File cloudPushFile = getCloudPushFile(this.mDataDownloadDir, getFileRegEx(0));
        if (cloudPushFile == null) {
            return false;
        }
        SQLiteDatabase writableDatabase = getWritableDatabase();
        log("readAndLoad");
        try {
            try {
                FileInputStream fileInputStream2 = new FileInputStream(cloudPushFile);
                try {
                    z = false;
                    try {
                        try {
                            JsonReader jsonReader = new JsonReader(new InputStreamReader(fileInputStream2));
                            try {
                                jsonReader.beginArray();
                                writableDatabase.delete("CloudPush", null, null);
                                while (jsonReader.hasNext()) {
                                    jsonReader.beginObject();
                                    ContentValues contentValues = new ContentValues();
                                    while (jsonReader.hasNext()) {
                                        fileInputStream = fileInputStream2;
                                        try {
                                            String strNextName = jsonReader.nextName();
                                            switch (strNextName.hashCode()) {
                                                case -1992012396:
                                                    str = str2;
                                                    if (!strNextName.equals("duration")) {
                                                        log("unsupported name:" + strNextName);
                                                    } else {
                                                        contentValues.put("exp_time", Long.valueOf(System.currentTimeMillis() + (((long) jsonReader.nextInt()) * v.b)));
                                                    }
                                                    break;
                                                case -1422950858:
                                                    if (!strNextName.equals(str2)) {
                                                        str = str2;
                                                        log("unsupported name:" + strNextName);
                                                    } else {
                                                        contentValues.put(str2, Integer.valueOf(jsonReader.nextInt()));
                                                        str = str2;
                                                    }
                                                    break;
                                                case 98494:
                                                    if (!strNextName.equals(str3)) {
                                                        str = str2;
                                                        log("unsupported name:" + strNextName);
                                                    } else {
                                                        contentValues.put(str3, jsonReader.nextString());
                                                        str = str2;
                                                    }
                                                    break;
                                                case 110806:
                                                    if (!strNextName.equals("pci")) {
                                                        str = str2;
                                                        log("unsupported name:" + strNextName);
                                                    } else {
                                                        contentValues.put("pci", Integer.valueOf(jsonReader.nextInt()));
                                                        str = str2;
                                                    }
                                                    break;
                                                case 112677:
                                                    if (!strNextName.equals("rat")) {
                                                        str = str2;
                                                        log("unsupported name:" + strNextName);
                                                    } else {
                                                        contentValues.put("rat", Integer.valueOf(jsonReader.nextInt()));
                                                        str = str2;
                                                    }
                                                    break;
                                                case 114582:
                                                    if (!strNextName.equals("tac")) {
                                                        str = str2;
                                                        log("unsupported name:" + strNextName);
                                                    } else {
                                                        contentValues.put("tac", Integer.valueOf(jsonReader.nextInt()));
                                                        str = str2;
                                                    }
                                                    break;
                                                case 3002589:
                                                    if (!strNextName.equals("args")) {
                                                        str = str2;
                                                        log("unsupported name:" + strNextName);
                                                    } else {
                                                        contentValues.put("args", jsonReader.nextString());
                                                        str = str2;
                                                    }
                                                    break;
                                                case 3151480:
                                                    if (!strNextName.equals("freq")) {
                                                        str = str2;
                                                        log("unsupported name:" + strNextName);
                                                    } else {
                                                        contentValues.put("freq", Integer.valueOf(jsonReader.nextInt()));
                                                        str = str2;
                                                    }
                                                    break;
                                                case 3443869:
                                                    if (!strNextName.equals("plmn")) {
                                                        str = str2;
                                                        log("unsupported name:" + strNextName);
                                                    } else {
                                                        contentValues.put("plmn", jsonReader.nextString());
                                                        str = str2;
                                                    }
                                                    break;
                                                case 93111887:
                                                    if (!strNextName.equals("bTime")) {
                                                        str = str2;
                                                        log("unsupported name:" + strNextName);
                                                    } else {
                                                        contentValues.put("bTime", jsonReader.nextString());
                                                        str = str2;
                                                    }
                                                    break;
                                                case 95882450:
                                                    if (!strNextName.equals("eTime")) {
                                                        str = str2;
                                                        log("unsupported name:" + strNextName);
                                                    } else {
                                                        contentValues.put("eTime", jsonReader.nextString());
                                                        str = str2;
                                                    }
                                                    break;
                                                default:
                                                    str = str2;
                                                    log("unsupported name:" + strNextName);
                                                    break;
                                            }
                                            fileInputStream2 = fileInputStream;
                                            str2 = str;
                                            str3 = str3;
                                        } catch (Throwable th2) {
                                            th = th2;
                                            Throwable th3 = th;
                                            try {
                                                jsonReader.close();
                                                throw th3;
                                            } catch (Throwable th4) {
                                                th3.addSuppressed(th4);
                                                throw th3;
                                            }
                                        }
                                    }
                                    writableDatabase.insert("CloudPush", null, contentValues);
                                    log("readAndLoad insert:" + contentValues);
                                    jsonReader.endObject();
                                    fileInputStream2 = fileInputStream2;
                                    str2 = str2;
                                    str3 = str3;
                                }
                                fileInputStream = fileInputStream2;
                                jsonReader.endArray();
                                boolean zDelete = cloudPushFile.delete();
                                if (PhoneDebug.VDBG) {
                                    log(cloudPushFile + " deleted:" + zDelete);
                                }
                                jsonReader.close();
                                fileInputStream.close();
                                return true;
                            } catch (Throwable th5) {
                                th = th5;
                                fileInputStream = fileInputStream2;
                            }
                        } catch (Throwable th6) {
                            th = th6;
                            fileInputStream = fileInputStream2;
                            th = th;
                            try {
                                fileInputStream.close();
                                throw th;
                            } catch (Throwable th7) {
                                th.addSuppressed(th7);
                                throw th;
                            }
                        }
                    } catch (Throwable th8) {
                        th = th8;
                        th = th;
                        fileInputStream.close();
                        throw th;
                    }
                } catch (Throwable th9) {
                    th = th9;
                    z = false;
                }
            } catch (Exception e) {
                e = e;
                e.printStackTrace();
                return z;
            }
        } catch (Exception e2) {
            e = e2;
            z = false;
            e.printStackTrace();
            return z;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public boolean readAndLoadCellMap() throws Throwable {
        FileInputStream fileInputStream;
        Throwable th;
        JsonReader jsonReader;
        Throwable th2;
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6 = "sub_scene";
        String str7 = "prev_gci";
        String str8 = "prj_type";
        String str9 = "reason";
        File cloudPushFile = getCloudPushFile(this.mDataDownloadDir, getFileRegEx(1));
        if (cloudPushFile == null) {
            return false;
        }
        SQLiteDatabase writableDatabase = getWritableDatabase();
        log("readAndLoadCellMap");
        try {
            FileInputStream fileInputStream2 = new FileInputStream(cloudPushFile);
            try {
                try {
                    String str10 = "update_time";
                    JsonReader jsonReader2 = new JsonReader(new InputStreamReader(fileInputStream2));
                    try {
                        jsonReader2.beginArray();
                        writableDatabase.delete("cellmap", null, null);
                        while (jsonReader2.hasNext()) {
                            jsonReader2.beginObject();
                            ContentValues contentValues = new ContentValues();
                            while (jsonReader2.hasNext()) {
                                jsonReader = jsonReader2;
                                try {
                                    String strNextName = jsonReader.nextName();
                                    switch (strNextName.hashCode()) {
                                        case -1274081535:
                                            String str11 = str10;
                                            fileInputStream = fileInputStream2;
                                            str = str7;
                                            str2 = str8;
                                            str3 = str9;
                                            str4 = str11;
                                            if (strNextName.equals(str)) {
                                                contentValues.put(str, jsonReader.nextString());
                                                str5 = str;
                                            } else {
                                                try {
                                                    jsonReader.skipValue();
                                                    str5 = str;
                                                    log("unsupported name:" + strNextName);
                                                } catch (Throwable th3) {
                                                    th = th3;
                                                    th2 = th;
                                                    try {
                                                        jsonReader.close();
                                                        throw th2;
                                                    } catch (Throwable th4) {
                                                        th2.addSuppressed(th4);
                                                        throw th2;
                                                    }
                                                }
                                            }
                                            fileInputStream2 = fileInputStream;
                                            jsonReader2 = jsonReader;
                                            str6 = str6;
                                            str10 = str4;
                                            str9 = str3;
                                            str8 = str2;
                                            str7 = str5;
                                            break;
                                        case -1151533455:
                                            String str12 = str10;
                                            fileInputStream = fileInputStream2;
                                            String str13 = str8;
                                            str3 = str9;
                                            str4 = str12;
                                            if (strNextName.equals(str13)) {
                                                contentValues.put(str13, jsonReader.nextString());
                                                str5 = str7;
                                                str2 = str13;
                                            } else {
                                                String str14 = str7;
                                                str2 = str13;
                                                str = str14;
                                                jsonReader.skipValue();
                                                str5 = str;
                                                log("unsupported name:" + strNextName);
                                            }
                                            fileInputStream2 = fileInputStream;
                                            jsonReader2 = jsonReader;
                                            str6 = str6;
                                            str10 = str4;
                                            str9 = str3;
                                            str8 = str2;
                                            str7 = str5;
                                            break;
                                        case -934964668:
                                            String str15 = str10;
                                            fileInputStream = fileInputStream2;
                                            String str16 = str9;
                                            str4 = str15;
                                            if (strNextName.equals(str16)) {
                                                contentValues.put(str16, jsonReader.nextString());
                                                str5 = str7;
                                                str2 = str8;
                                                str3 = str16;
                                            } else {
                                                String str17 = str8;
                                                str3 = str16;
                                                str = str7;
                                                str2 = str17;
                                                jsonReader.skipValue();
                                                str5 = str;
                                                log("unsupported name:" + strNextName);
                                            }
                                            fileInputStream2 = fileInputStream;
                                            jsonReader2 = jsonReader;
                                            str6 = str6;
                                            str10 = str4;
                                            str9 = str3;
                                            str8 = str2;
                                            str7 = str5;
                                            break;
                                        case -573446013:
                                            String str18 = str10;
                                            fileInputStream = fileInputStream2;
                                            if (strNextName.equals(str18)) {
                                                contentValues.put(str18, jsonReader.nextString());
                                                str5 = str7;
                                                str2 = str8;
                                                str3 = str9;
                                                str4 = str18;
                                            } else {
                                                String str19 = str9;
                                                str4 = str18;
                                                str = str7;
                                                str2 = str8;
                                                str3 = str19;
                                                jsonReader.skipValue();
                                                str5 = str;
                                                log("unsupported name:" + strNextName);
                                            }
                                            fileInputStream2 = fileInputStream;
                                            jsonReader2 = jsonReader;
                                            str6 = str6;
                                            str10 = str4;
                                            str9 = str3;
                                            str8 = str2;
                                            str7 = str5;
                                            break;
                                        case -43415091:
                                            if (strNextName.equals(str6)) {
                                                contentValues.put(str6, jsonReader.nextString());
                                                str5 = str7;
                                                str2 = str8;
                                                str3 = str9;
                                                str4 = str10;
                                                fileInputStream = fileInputStream2;
                                                fileInputStream2 = fileInputStream;
                                                jsonReader2 = jsonReader;
                                                str6 = str6;
                                                str10 = str4;
                                                str9 = str3;
                                                str8 = str2;
                                                str7 = str5;
                                            }
                                            String str20 = str10;
                                            fileInputStream = fileInputStream2;
                                            str = str7;
                                            str2 = str8;
                                            str3 = str9;
                                            str4 = str20;
                                            jsonReader.skipValue();
                                            str5 = str;
                                            log("unsupported name:" + strNextName);
                                            fileInputStream2 = fileInputStream;
                                            jsonReader2 = jsonReader;
                                            str6 = str6;
                                            str10 = str4;
                                            str9 = str3;
                                            str8 = str2;
                                            str7 = str5;
                                            break;
                                        case 102157:
                                            if (strNextName.equals("gci")) {
                                                contentValues.put("gci", jsonReader.nextString());
                                                str5 = str7;
                                                str2 = str8;
                                                str3 = str9;
                                                str4 = str10;
                                                fileInputStream = fileInputStream2;
                                                fileInputStream2 = fileInputStream;
                                                jsonReader2 = jsonReader;
                                                str6 = str6;
                                                str10 = str4;
                                                str9 = str3;
                                                str8 = str2;
                                                str7 = str5;
                                            }
                                            String str21 = str10;
                                            fileInputStream = fileInputStream2;
                                            str = str7;
                                            str2 = str8;
                                            str3 = str9;
                                            str4 = str21;
                                            jsonReader.skipValue();
                                            str5 = str;
                                            log("unsupported name:" + strNextName);
                                            fileInputStream2 = fileInputStream;
                                            jsonReader2 = jsonReader;
                                            str6 = str6;
                                            str10 = str4;
                                            str9 = str3;
                                            str8 = str2;
                                            str7 = str5;
                                            break;
                                        case 110806:
                                            if (strNextName.equals("pci")) {
                                                contentValues.put("pci", Integer.valueOf(jsonReader.nextInt()));
                                                str5 = str7;
                                                str2 = str8;
                                                str3 = str9;
                                                str4 = str10;
                                                fileInputStream = fileInputStream2;
                                                fileInputStream2 = fileInputStream;
                                                jsonReader2 = jsonReader;
                                                str6 = str6;
                                                str10 = str4;
                                                str9 = str3;
                                                str8 = str2;
                                                str7 = str5;
                                            }
                                            String str22 = str10;
                                            fileInputStream = fileInputStream2;
                                            str = str7;
                                            str2 = str8;
                                            str3 = str9;
                                            str4 = str22;
                                            jsonReader.skipValue();
                                            str5 = str;
                                            log("unsupported name:" + strNextName);
                                            fileInputStream2 = fileInputStream;
                                            jsonReader2 = jsonReader;
                                            str6 = str6;
                                            str10 = str4;
                                            str9 = str3;
                                            str8 = str2;
                                            str7 = str5;
                                            break;
                                        case 112677:
                                            if (strNextName.equals("rat")) {
                                                contentValues.put("rat", Integer.valueOf(jsonReader.nextInt()));
                                                str5 = str7;
                                                str2 = str8;
                                                str3 = str9;
                                                str4 = str10;
                                                fileInputStream = fileInputStream2;
                                                fileInputStream2 = fileInputStream;
                                                jsonReader2 = jsonReader;
                                                str6 = str6;
                                                str10 = str4;
                                                str9 = str3;
                                                str8 = str2;
                                                str7 = str5;
                                            }
                                            String str23 = str10;
                                            fileInputStream = fileInputStream2;
                                            str = str7;
                                            str2 = str8;
                                            str3 = str9;
                                            str4 = str23;
                                            jsonReader.skipValue();
                                            str5 = str;
                                            log("unsupported name:" + strNextName);
                                            fileInputStream2 = fileInputStream;
                                            jsonReader2 = jsonReader;
                                            str6 = str6;
                                            str10 = str4;
                                            str9 = str3;
                                            str8 = str2;
                                            str7 = str5;
                                            break;
                                        case 114582:
                                            if (strNextName.equals("tac")) {
                                                contentValues.put("tac", jsonReader.nextString());
                                                str5 = str7;
                                                str2 = str8;
                                                str3 = str9;
                                                str4 = str10;
                                                fileInputStream = fileInputStream2;
                                                fileInputStream2 = fileInputStream;
                                                jsonReader2 = jsonReader;
                                                str6 = str6;
                                                str10 = str4;
                                                str9 = str3;
                                                str8 = str2;
                                                str7 = str5;
                                            }
                                            String str24 = str10;
                                            fileInputStream = fileInputStream2;
                                            str = str7;
                                            str2 = str8;
                                            str3 = str9;
                                            str4 = str24;
                                            jsonReader.skipValue();
                                            str5 = str;
                                            log("unsupported name:" + strNextName);
                                            fileInputStream2 = fileInputStream;
                                            jsonReader2 = jsonReader;
                                            str6 = str6;
                                            str10 = str4;
                                            str9 = str3;
                                            str8 = str2;
                                            str7 = str5;
                                            break;
                                        case 3053931:
                                            if (strNextName.equals("city")) {
                                                contentValues.put("city", jsonReader.nextString());
                                                str5 = str7;
                                                str2 = str8;
                                                str3 = str9;
                                                str4 = str10;
                                                fileInputStream = fileInputStream2;
                                                fileInputStream2 = fileInputStream;
                                                jsonReader2 = jsonReader;
                                                str6 = str6;
                                                str10 = str4;
                                                str9 = str3;
                                                str8 = str2;
                                                str7 = str5;
                                            }
                                            String str25 = str10;
                                            fileInputStream = fileInputStream2;
                                            str = str7;
                                            str2 = str8;
                                            str3 = str9;
                                            str4 = str25;
                                            jsonReader.skipValue();
                                            str5 = str;
                                            log("unsupported name:" + strNextName);
                                            fileInputStream2 = fileInputStream;
                                            jsonReader2 = jsonReader;
                                            str6 = str6;
                                            str10 = str4;
                                            str9 = str3;
                                            str8 = str2;
                                            str7 = str5;
                                            break;
                                        case 3151480:
                                            if (strNextName.equals("freq")) {
                                                contentValues.put("freq", Integer.valueOf(jsonReader.nextInt()));
                                                str5 = str7;
                                                str2 = str8;
                                                str3 = str9;
                                                str4 = str10;
                                                fileInputStream = fileInputStream2;
                                                fileInputStream2 = fileInputStream;
                                                jsonReader2 = jsonReader;
                                                str6 = str6;
                                                str10 = str4;
                                                str9 = str3;
                                                str8 = str2;
                                                str7 = str5;
                                            }
                                            String str26 = str10;
                                            fileInputStream = fileInputStream2;
                                            str = str7;
                                            str2 = str8;
                                            str3 = str9;
                                            str4 = str26;
                                            jsonReader.skipValue();
                                            str5 = str;
                                            log("unsupported name:" + strNextName);
                                            fileInputStream2 = fileInputStream;
                                            jsonReader2 = jsonReader;
                                            str6 = str6;
                                            str10 = str4;
                                            str9 = str3;
                                            str8 = str2;
                                            str7 = str5;
                                            break;
                                        case 109254796:
                                            if (strNextName.equals("scene")) {
                                                contentValues.put("scene", jsonReader.nextString());
                                                str5 = str7;
                                                str2 = str8;
                                                str3 = str9;
                                                str4 = str10;
                                                fileInputStream = fileInputStream2;
                                                fileInputStream2 = fileInputStream;
                                                jsonReader2 = jsonReader;
                                                str6 = str6;
                                                str10 = str4;
                                                str9 = str3;
                                                str8 = str2;
                                                str7 = str5;
                                            }
                                            String str27 = str10;
                                            fileInputStream = fileInputStream2;
                                            str = str7;
                                            str2 = str8;
                                            str3 = str9;
                                            str4 = str27;
                                            jsonReader.skipValue();
                                            str5 = str;
                                            log("unsupported name:" + strNextName);
                                            fileInputStream2 = fileInputStream;
                                            jsonReader2 = jsonReader;
                                            str6 = str6;
                                            str10 = str4;
                                            str9 = str3;
                                            str8 = str2;
                                            str7 = str5;
                                            break;
                                        case 230368664:
                                            if (strNextName.equals("abnormal_period")) {
                                                contentValues.put("abnormal_period", jsonReader.nextString());
                                                str5 = str7;
                                                str2 = str8;
                                                str3 = str9;
                                                str4 = str10;
                                                fileInputStream = fileInputStream2;
                                                fileInputStream2 = fileInputStream;
                                                jsonReader2 = jsonReader;
                                                str6 = str6;
                                                str10 = str4;
                                                str9 = str3;
                                                str8 = str2;
                                                str7 = str5;
                                            }
                                            String str28 = str10;
                                            fileInputStream = fileInputStream2;
                                            str = str7;
                                            str2 = str8;
                                            str3 = str9;
                                            str4 = str28;
                                            jsonReader.skipValue();
                                            str5 = str;
                                            log("unsupported name:" + strNextName);
                                            fileInputStream2 = fileInputStream;
                                            jsonReader2 = jsonReader;
                                            str6 = str6;
                                            str10 = str4;
                                            str9 = str3;
                                            str8 = str2;
                                            str7 = str5;
                                            break;
                                        case 425076417:
                                            if (strNextName.equals("neigh_gci")) {
                                                contentValues.put("neigh_gci", jsonReader.nextString());
                                                str5 = str7;
                                                str2 = str8;
                                                str3 = str9;
                                                str4 = str10;
                                                fileInputStream = fileInputStream2;
                                                fileInputStream2 = fileInputStream;
                                                jsonReader2 = jsonReader;
                                                str6 = str6;
                                                str10 = str4;
                                                str9 = str3;
                                                str8 = str2;
                                                str7 = str5;
                                            }
                                            String str29 = str10;
                                            fileInputStream = fileInputStream2;
                                            str = str7;
                                            str2 = str8;
                                            str3 = str9;
                                            str4 = str29;
                                            jsonReader.skipValue();
                                            str5 = str;
                                            log("unsupported name:" + strNextName);
                                            fileInputStream2 = fileInputStream;
                                            jsonReader2 = jsonReader;
                                            str6 = str6;
                                            str10 = str4;
                                            str9 = str3;
                                            str8 = str2;
                                            str7 = str5;
                                            break;
                                        case 1984153269:
                                            try {
                                                if (strNextName.equals("service")) {
                                                    contentValues.put("service", jsonReader.nextString());
                                                    str5 = str7;
                                                    str2 = str8;
                                                    str3 = str9;
                                                    str4 = str10;
                                                    fileInputStream = fileInputStream2;
                                                    fileInputStream2 = fileInputStream;
                                                    jsonReader2 = jsonReader;
                                                    str6 = str6;
                                                    str10 = str4;
                                                    str9 = str3;
                                                    str8 = str2;
                                                    str7 = str5;
                                                }
                                                String str210 = str10;
                                                fileInputStream = fileInputStream2;
                                                str = str7;
                                                str2 = str8;
                                                str3 = str9;
                                                str4 = str210;
                                                jsonReader.skipValue();
                                                str5 = str;
                                                log("unsupported name:" + strNextName);
                                                fileInputStream2 = fileInputStream;
                                                jsonReader2 = jsonReader;
                                                str6 = str6;
                                                str10 = str4;
                                                str9 = str3;
                                                str8 = str2;
                                                str7 = str5;
                                            } catch (Throwable th5) {
                                                th2 = th5;
                                                fileInputStream = fileInputStream2;
                                                jsonReader.close();
                                                throw th2;
                                            }
                                            break;
                                        default:
                                            String str211 = str10;
                                            fileInputStream = fileInputStream2;
                                            str = str7;
                                            str2 = str8;
                                            str3 = str9;
                                            str4 = str211;
                                            jsonReader.skipValue();
                                            str5 = str;
                                            log("unsupported name:" + strNextName);
                                            fileInputStream2 = fileInputStream;
                                            jsonReader2 = jsonReader;
                                            str6 = str6;
                                            str10 = str4;
                                            str9 = str3;
                                            str8 = str2;
                                            str7 = str5;
                                            break;
                                    }
                                } catch (Throwable th6) {
                                    th = th6;
                                    fileInputStream = fileInputStream2;
                                    th2 = th;
                                    jsonReader.close();
                                    throw th2;
                                }
                            }
                            JsonReader jsonReader3 = jsonReader2;
                            String str30 = str7;
                            String str31 = str8;
                            String str32 = str9;
                            String str33 = str10;
                            writableDatabase.insert("cellmap", null, contentValues);
                            log("readAndLoad insert:" + contentValues);
                            jsonReader3.endObject();
                            fileInputStream2 = fileInputStream2;
                            jsonReader2 = jsonReader3;
                            str6 = str6;
                            str10 = str33;
                            str9 = str32;
                            str8 = str31;
                            str7 = str30;
                        }
                        jsonReader = jsonReader2;
                        fileInputStream = fileInputStream2;
                        jsonReader.endArray();
                        boolean zDelete = cloudPushFile.delete();
                        if (PhoneDebug.VDBG) {
                            log(cloudPushFile + " deleted:" + zDelete);
                        }
                        jsonReader.close();
                        fileInputStream.close();
                        return true;
                    } catch (Throwable th7) {
                        th = th7;
                        jsonReader = jsonReader2;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    fileInputStream = fileInputStream2;
                    th = th;
                    try {
                        fileInputStream.close();
                        throw th;
                    } catch (Throwable th9) {
                        th.addSuppressed(th9);
                        throw th;
                    }
                }
            } catch (Throwable th10) {
                th = th10;
                th = th;
                fileInputStream.close();
                throw th;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public boolean readAndLoadManualCellMap() {
        JsonReader jsonReader;
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        String str9;
        String str10;
        String str11 = "action1";
        String str12 = "sinr2";
        String str13 = "action2";
        String str14 = "time_extra";
        String str15 = "update_time";
        String str16 = "gci1";
        String str17 = "gci2";
        String str18 = "pci1";
        String str19 = "pci2";
        File cloudPushFile = getCloudPushFile(this.mDataDownloadDir, getFileRegEx(2));
        if (cloudPushFile == null) {
            return false;
        }
        SQLiteDatabase writableDatabase = getWritableDatabase();
        log("readAndLoadManualCellMap");
        try {
            FileInputStream fileInputStream = new FileInputStream(cloudPushFile);
            try {
                String str20 = "rat1";
                JsonReader jsonReader2 = new JsonReader(new InputStreamReader(fileInputStream));
                try {
                    jsonReader2.beginArray();
                    writableDatabase.delete("cellmap_manual", null, null);
                    while (jsonReader2.hasNext()) {
                        jsonReader2.beginObject();
                        ContentValues contentValues = new ContentValues();
                        while (jsonReader2.hasNext()) {
                            jsonReader = jsonReader2;
                            try {
                                String strNextName = jsonReader.nextName();
                                switch (strNextName.hashCode()) {
                                    case -1161803589:
                                        String str21 = str20;
                                        str = str12;
                                        str2 = str11;
                                        str3 = str13;
                                        str4 = str15;
                                        str5 = str16;
                                        str6 = str17;
                                        str7 = str18;
                                        str8 = str19;
                                        str9 = str21;
                                        if (!strNextName.equals(str2)) {
                                            jsonReader.skipValue();
                                            str10 = str2;
                                            log("unsupported name:" + strNextName);
                                        } else {
                                            contentValues.put(str2, Integer.valueOf(jsonReader.nextInt()));
                                            str10 = str2;
                                        }
                                        break;
                                    case -1161803588:
                                        String str22 = str20;
                                        str = str12;
                                        String str23 = str13;
                                        str4 = str15;
                                        str5 = str16;
                                        str6 = str17;
                                        str7 = str18;
                                        str8 = str19;
                                        str9 = str22;
                                        if (!strNextName.equals(str23)) {
                                            String str24 = str11;
                                            str3 = str23;
                                            str2 = str24;
                                            jsonReader.skipValue();
                                            str10 = str2;
                                            log("unsupported name:" + strNextName);
                                        } else {
                                            contentValues.put(str23, Integer.valueOf(jsonReader.nextInt()));
                                            str10 = str11;
                                            str3 = str23;
                                        }
                                        break;
                                    case -573446013:
                                        String str25 = str20;
                                        str = str12;
                                        String str26 = str15;
                                        str5 = str16;
                                        str6 = str17;
                                        str7 = str18;
                                        str8 = str19;
                                        str9 = str25;
                                        if (!strNextName.equals(str26)) {
                                            String str27 = str13;
                                            str4 = str26;
                                            str2 = str11;
                                            str3 = str27;
                                            jsonReader.skipValue();
                                            str10 = str2;
                                            log("unsupported name:" + strNextName);
                                        } else {
                                            contentValues.put(str26, Long.valueOf(jsonReader.nextLong()));
                                            str10 = str11;
                                            str3 = str13;
                                            str4 = str26;
                                        }
                                        break;
                                    case 3166916:
                                        String str28 = str20;
                                        str = str12;
                                        String str29 = str16;
                                        str6 = str17;
                                        str7 = str18;
                                        str8 = str19;
                                        str9 = str28;
                                        if (!strNextName.equals(str29)) {
                                            String str30 = str15;
                                            str5 = str29;
                                            str2 = str11;
                                            str3 = str13;
                                            str4 = str30;
                                            jsonReader.skipValue();
                                            str10 = str2;
                                            log("unsupported name:" + strNextName);
                                        } else {
                                            contentValues.put(str29, jsonReader.nextString());
                                            str10 = str11;
                                            str3 = str13;
                                            str4 = str15;
                                            str5 = str29;
                                        }
                                        break;
                                    case 3166917:
                                        String str31 = str20;
                                        str = str12;
                                        String str32 = str17;
                                        str7 = str18;
                                        str8 = str19;
                                        str9 = str31;
                                        if (!strNextName.equals(str32)) {
                                            String str33 = str16;
                                            str6 = str32;
                                            str2 = str11;
                                            str3 = str13;
                                            str4 = str15;
                                            str5 = str33;
                                            jsonReader.skipValue();
                                            str10 = str2;
                                            log("unsupported name:" + strNextName);
                                        } else {
                                            contentValues.put(str32, jsonReader.nextString());
                                            str10 = str11;
                                            str3 = str13;
                                            str4 = str15;
                                            str5 = str16;
                                            str6 = str32;
                                        }
                                        break;
                                    case 3435035:
                                        String str34 = str20;
                                        str = str12;
                                        String str35 = str18;
                                        str8 = str19;
                                        str9 = str34;
                                        if (!strNextName.equals(str35)) {
                                            String str36 = str17;
                                            str7 = str35;
                                            str2 = str11;
                                            str3 = str13;
                                            str4 = str15;
                                            str5 = str16;
                                            str6 = str36;
                                            jsonReader.skipValue();
                                            str10 = str2;
                                            log("unsupported name:" + strNextName);
                                        } else {
                                            contentValues.put(str35, Integer.valueOf(jsonReader.nextInt()));
                                            str10 = str11;
                                            str3 = str13;
                                            str4 = str15;
                                            str5 = str16;
                                            str6 = str17;
                                            str7 = str35;
                                        }
                                        break;
                                    case 3435036:
                                        String str37 = str20;
                                        str = str12;
                                        String str38 = str19;
                                        str9 = str37;
                                        if (!strNextName.equals(str38)) {
                                            String str39 = str18;
                                            str8 = str38;
                                            str2 = str11;
                                            str3 = str13;
                                            str4 = str15;
                                            str5 = str16;
                                            str6 = str17;
                                            str7 = str39;
                                            jsonReader.skipValue();
                                            str10 = str2;
                                            log("unsupported name:" + strNextName);
                                        } else {
                                            contentValues.put(str38, Integer.valueOf(jsonReader.nextInt()));
                                            str10 = str11;
                                            str3 = str13;
                                            str4 = str15;
                                            str5 = str16;
                                            str6 = str17;
                                            str7 = str18;
                                            str8 = str38;
                                        }
                                        break;
                                    case 3493036:
                                        String str40 = str20;
                                        str = str12;
                                        if (!strNextName.equals(str40)) {
                                            String str41 = str19;
                                            str9 = str40;
                                            str2 = str11;
                                            str3 = str13;
                                            str4 = str15;
                                            str5 = str16;
                                            str6 = str17;
                                            str7 = str18;
                                            str8 = str41;
                                            jsonReader.skipValue();
                                            str10 = str2;
                                            log("unsupported name:" + strNextName);
                                        } else {
                                            contentValues.put(str40, Integer.valueOf(jsonReader.nextInt()));
                                            str10 = str11;
                                            str3 = str13;
                                            str4 = str15;
                                            str5 = str16;
                                            str6 = str17;
                                            str7 = str18;
                                            str8 = str19;
                                            str9 = str40;
                                        }
                                        break;
                                    case 3493037:
                                        if (strNextName.equals("rat2")) {
                                            contentValues.put("rat2", Integer.valueOf(jsonReader.nextInt()));
                                            str10 = str11;
                                            str3 = str13;
                                            str4 = str15;
                                            str5 = str16;
                                            str6 = str17;
                                            str7 = str18;
                                            str8 = str19;
                                            str9 = str20;
                                            str = str12;
                                        }
                                        String str42 = str20;
                                        str = str12;
                                        str2 = str11;
                                        str3 = str13;
                                        str4 = str15;
                                        str5 = str16;
                                        str6 = str17;
                                        str7 = str18;
                                        str8 = str19;
                                        str9 = str42;
                                        jsonReader.skipValue();
                                        str10 = str2;
                                        log("unsupported name:" + strNextName);
                                        break;
                                    case 3575610:
                                        if (strNextName.equals("type")) {
                                            contentValues.put("type", Integer.valueOf(jsonReader.nextInt()));
                                            str10 = str11;
                                            str3 = str13;
                                            str4 = str15;
                                            str5 = str16;
                                            str6 = str17;
                                            str7 = str18;
                                            str8 = str19;
                                            str9 = str20;
                                            str = str12;
                                        }
                                        String str43 = str20;
                                        str = str12;
                                        str2 = str11;
                                        str3 = str13;
                                        str4 = str15;
                                        str5 = str16;
                                        str6 = str17;
                                        str7 = str18;
                                        str8 = str19;
                                        str9 = str43;
                                        jsonReader.skipValue();
                                        str10 = str2;
                                        log("unsupported name:" + strNextName);
                                        break;
                                    case 97695929:
                                        if (strNextName.equals("freq1")) {
                                            contentValues.put("freq1", Integer.valueOf(jsonReader.nextInt()));
                                            str10 = str11;
                                            str3 = str13;
                                            str4 = str15;
                                            str5 = str16;
                                            str6 = str17;
                                            str7 = str18;
                                            str8 = str19;
                                            str9 = str20;
                                            str = str12;
                                        }
                                        String str44 = str20;
                                        str = str12;
                                        str2 = str11;
                                        str3 = str13;
                                        str4 = str15;
                                        str5 = str16;
                                        str6 = str17;
                                        str7 = str18;
                                        str8 = str19;
                                        str9 = str44;
                                        jsonReader.skipValue();
                                        str10 = str2;
                                        log("unsupported name:" + strNextName);
                                        break;
                                    case 97695930:
                                        if (strNextName.equals("freq2")) {
                                            contentValues.put("freq2", Integer.valueOf(jsonReader.nextInt()));
                                            str10 = str11;
                                            str3 = str13;
                                            str4 = str15;
                                            str5 = str16;
                                            str6 = str17;
                                            str7 = str18;
                                            str8 = str19;
                                            str9 = str20;
                                            str = str12;
                                        }
                                        String str45 = str20;
                                        str = str12;
                                        str2 = str11;
                                        str3 = str13;
                                        str4 = str15;
                                        str5 = str16;
                                        str6 = str17;
                                        str7 = str18;
                                        str8 = str19;
                                        str9 = str45;
                                        jsonReader.skipValue();
                                        str10 = str2;
                                        log("unsupported name:" + strNextName);
                                        break;
                                    case 108820434:
                                        if (strNextName.equals("rsrp1")) {
                                            contentValues.put("rsrp1", Integer.valueOf(jsonReader.nextInt()));
                                            str10 = str11;
                                            str3 = str13;
                                            str4 = str15;
                                            str5 = str16;
                                            str6 = str17;
                                            str7 = str18;
                                            str8 = str19;
                                            str9 = str20;
                                            str = str12;
                                        }
                                        String str46 = str20;
                                        str = str12;
                                        str2 = str11;
                                        str3 = str13;
                                        str4 = str15;
                                        str5 = str16;
                                        str6 = str17;
                                        str7 = str18;
                                        str8 = str19;
                                        str9 = str46;
                                        jsonReader.skipValue();
                                        str10 = str2;
                                        log("unsupported name:" + strNextName);
                                        break;
                                    case 108820435:
                                        if (strNextName.equals("rsrp2")) {
                                            contentValues.put("rsrp2", Integer.valueOf(jsonReader.nextInt()));
                                            str10 = str11;
                                            str3 = str13;
                                            str4 = str15;
                                            str5 = str16;
                                            str6 = str17;
                                            str7 = str18;
                                            str8 = str19;
                                            str9 = str20;
                                            str = str12;
                                        }
                                        String str47 = str20;
                                        str = str12;
                                        str2 = str11;
                                        str3 = str13;
                                        str4 = str15;
                                        str5 = str16;
                                        str6 = str17;
                                        str7 = str18;
                                        str8 = str19;
                                        str9 = str47;
                                        jsonReader.skipValue();
                                        str10 = str2;
                                        log("unsupported name:" + strNextName);
                                        break;
                                    case 109442263:
                                        if (strNextName.equals("sinr1")) {
                                            contentValues.put("sinr1", Integer.valueOf(jsonReader.nextInt()));
                                            str10 = str11;
                                            str3 = str13;
                                            str4 = str15;
                                            str5 = str16;
                                            str6 = str17;
                                            str7 = str18;
                                            str8 = str19;
                                            str9 = str20;
                                            str = str12;
                                        }
                                        String str48 = str20;
                                        str = str12;
                                        str2 = str11;
                                        str3 = str13;
                                        str4 = str15;
                                        str5 = str16;
                                        str6 = str17;
                                        str7 = str18;
                                        str8 = str19;
                                        str9 = str48;
                                        jsonReader.skipValue();
                                        str10 = str2;
                                        log("unsupported name:" + strNextName);
                                        break;
                                    case 109442264:
                                        if (strNextName.equals(str12)) {
                                            contentValues.put(str12, Integer.valueOf(jsonReader.nextInt()));
                                            str10 = str11;
                                            str3 = str13;
                                            str4 = str15;
                                            str5 = str16;
                                            str6 = str17;
                                            str7 = str18;
                                            str8 = str19;
                                            str9 = str20;
                                            str = str12;
                                        }
                                        String str49 = str20;
                                        str = str12;
                                        str2 = str11;
                                        str3 = str13;
                                        str4 = str15;
                                        str5 = str16;
                                        str6 = str17;
                                        str7 = str18;
                                        str8 = str19;
                                        str9 = str49;
                                        jsonReader.skipValue();
                                        str10 = str2;
                                        log("unsupported name:" + strNextName);
                                        break;
                                    case 1123171358:
                                        if (strNextName.equals(str14)) {
                                            contentValues.put(str14, Long.valueOf(jsonReader.nextLong()));
                                            str10 = str11;
                                            str3 = str13;
                                            str4 = str15;
                                            str5 = str16;
                                            str6 = str17;
                                            str7 = str18;
                                            str8 = str19;
                                            str9 = str20;
                                            str = str12;
                                        }
                                        String str410 = str20;
                                        str = str12;
                                        str2 = str11;
                                        str3 = str13;
                                        str4 = str15;
                                        str5 = str16;
                                        str6 = str17;
                                        str7 = str18;
                                        str8 = str19;
                                        str9 = str410;
                                        jsonReader.skipValue();
                                        str10 = str2;
                                        log("unsupported name:" + strNextName);
                                        break;
                                    case 1379209244:
                                        if (strNextName.equals("service1")) {
                                            contentValues.put("service1", Integer.valueOf(jsonReader.nextInt()));
                                            str10 = str11;
                                            str3 = str13;
                                            str4 = str15;
                                            str5 = str16;
                                            str6 = str17;
                                            str7 = str18;
                                            str8 = str19;
                                            str9 = str20;
                                            str = str12;
                                        }
                                        String str411 = str20;
                                        str = str12;
                                        str2 = str11;
                                        str3 = str13;
                                        str4 = str15;
                                        str5 = str16;
                                        str6 = str17;
                                        str7 = str18;
                                        str8 = str19;
                                        str9 = str411;
                                        jsonReader.skipValue();
                                        str10 = str2;
                                        log("unsupported name:" + strNextName);
                                        break;
                                    case 1379209245:
                                        if (strNextName.equals("service2")) {
                                            contentValues.put("service2", Integer.valueOf(jsonReader.nextInt()));
                                            str10 = str11;
                                            str3 = str13;
                                            str4 = str15;
                                            str5 = str16;
                                            str6 = str17;
                                            str7 = str18;
                                            str8 = str19;
                                            str9 = str20;
                                            str = str12;
                                        }
                                        String str412 = str20;
                                        str = str12;
                                        str2 = str11;
                                        str3 = str13;
                                        str4 = str15;
                                        str5 = str16;
                                        str6 = str17;
                                        str7 = str18;
                                        str8 = str19;
                                        str9 = str412;
                                        jsonReader.skipValue();
                                        str10 = str2;
                                        log("unsupported name:" + strNextName);
                                        break;
                                    default:
                                        String str413 = str20;
                                        str = str12;
                                        str2 = str11;
                                        str3 = str13;
                                        str4 = str15;
                                        str5 = str16;
                                        str6 = str17;
                                        str7 = str18;
                                        str8 = str19;
                                        str9 = str413;
                                        jsonReader.skipValue();
                                        str10 = str2;
                                        log("unsupported name:" + strNextName);
                                        break;
                                }
                                str12 = str;
                                jsonReader2 = jsonReader;
                                str14 = str14;
                                str20 = str9;
                                str19 = str8;
                                str18 = str7;
                                str17 = str6;
                                str16 = str5;
                                str15 = str4;
                                str13 = str3;
                                str11 = str10;
                            } catch (Throwable th) {
                                th = th;
                                Throwable th2 = th;
                                try {
                                    jsonReader.close();
                                    throw th2;
                                } catch (Throwable th3) {
                                    th2.addSuppressed(th3);
                                    throw th2;
                                }
                            }
                        }
                        JsonReader jsonReader3 = jsonReader2;
                        String str50 = str11;
                        String str51 = str13;
                        String str52 = str15;
                        String str53 = str16;
                        String str54 = str17;
                        String str55 = str18;
                        writableDatabase.insert("cellmap_manual", null, contentValues);
                        log("readAndLoad insert:" + contentValues);
                        jsonReader3.endObject();
                        str12 = str12;
                        jsonReader2 = jsonReader3;
                        str14 = str14;
                        str20 = str20;
                        str19 = str19;
                        str18 = str55;
                        str17 = str54;
                        str16 = str53;
                        str15 = str52;
                        str13 = str51;
                        str11 = str50;
                    }
                    jsonReader = jsonReader2;
                    jsonReader.endArray();
                    boolean zDelete = cloudPushFile.delete();
                    if (PhoneDebug.VDBG) {
                        log(cloudPushFile + " deleted:" + zDelete);
                    }
                    jsonReader.close();
                    fileInputStream.close();
                    return true;
                } catch (Throwable th4) {
                    th = th4;
                    jsonReader = jsonReader2;
                }
            } catch (Throwable th5) {
                try {
                    fileInputStream.close();
                    throw th5;
                } catch (Throwable th6) {
                    th5.addSuppressed(th6);
                    throw th5;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:24:0x00a4  */
    public boolean readAndLoadSignalMap(boolean z) {
        String str = "sublocality";
        String str2 = "cid";
        Pair sigmapNewestFile = getSigmapNewestFile(this.mDataDownloadDir, "blackcelldb_\\S+?_\\d+\\.json", "signalmap_common_json_version");
        if (sigmapNewestFile == null || sigmapNewestFile.second == null) {
            return false;
        }
        SQLiteDatabase writableDatabase = getWritableDatabase();
        int intPreference = PreferenceUtils.getInstance().getIntPreference("signalmap_common_db_version");
        if (!z && intPreference >= ((Integer) sigmapNewestFile.first).intValue()) {
            log(" no need to update db");
            return false;
        }
        File file = (File) sigmapNewestFile.second;
        log("readAndLoadSignalMap:" + file);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                JsonReader jsonReader = new JsonReader(new InputStreamReader(fileInputStream));
                try {
                    jsonReader.beginArray();
                    clearSignalMapTable(writableDatabase);
                    writableDatabase.beginTransaction();
                    while (jsonReader.hasNext()) {
                        try {
                            jsonReader.beginObject();
                            ContentValues contentValues = new ContentValues();
                            while (jsonReader.hasNext()) {
                                String strNextName = jsonReader.nextName();
                                switch (strNextName.hashCode()) {
                                    case -1144292445:
                                        if (!strNextName.equals(str)) {
                                            log("unsupported name:" + strNextName);
                                        } else {
                                            contentValues.put(str, jsonReader.nextString());
                                        }
                                        break;
                                    case 98494:
                                        if (!strNextName.equals(str2)) {
                                            log("unsupported name:" + strNextName);
                                        } else {
                                            contentValues.put(str2, jsonReader.nextString());
                                        }
                                        break;
                                    case 110806:
                                        if (!strNextName.equals("pci")) {
                                            log("unsupported name:" + strNextName);
                                        } else {
                                            contentValues.put("pci", Integer.valueOf(jsonReader.nextInt()));
                                        }
                                        break;
                                    case 112677:
                                        if (!strNextName.equals("rat")) {
                                            log("unsupported name:" + strNextName);
                                        } else {
                                            contentValues.put("rat", Integer.valueOf(jsonReader.nextInt()));
                                        }
                                        break;
                                    case 114582:
                                        if (!strNextName.equals("tac")) {
                                            log("unsupported name:" + strNextName);
                                        } else {
                                            contentValues.put("tac", jsonReader.nextString());
                                        }
                                        break;
                                    case 3016245:
                                        if (!strNextName.equals("band")) {
                                            log("unsupported name:" + strNextName);
                                        } else {
                                            contentValues.put("band", jsonReader.nextString());
                                        }
                                        break;
                                    case 3053931:
                                        if (!strNextName.equals("city")) {
                                            log("unsupported name:" + strNextName);
                                        } else {
                                            contentValues.put("city", jsonReader.nextString());
                                        }
                                        break;
                                    case 3443869:
                                        if (!strNextName.equals("plmn")) {
                                            log("unsupported name:" + strNextName);
                                        } else {
                                            contentValues.put("plmn", Integer.valueOf(jsonReader.nextInt()));
                                        }
                                        break;
                                    case 93078912:
                                        if (!strNextName.equals("arfcn")) {
                                            log("unsupported name:" + strNextName);
                                        } else {
                                            contentValues.put("arfcn", jsonReader.nextString());
                                        }
                                        break;
                                    case 94434409:
                                        if (!strNextName.equals("cause")) {
                                            log("unsupported name:" + strNextName);
                                        } else {
                                            contentValues.put("cause", Integer.valueOf(jsonReader.nextInt()));
                                        }
                                        break;
                                    default:
                                        log("unsupported name:" + strNextName);
                                        break;
                                }
                                str = str;
                                str2 = str2;
                            }
                            writableDatabase.insert("SignalMapCells", null, contentValues);
                            jsonReader.endObject();
                            str = str;
                            str2 = str2;
                        } catch (Throwable th) {
                            writableDatabase.endTransaction();
                            throw th;
                        }
                    }
                    writableDatabase.setTransactionSuccessful();
                    writableDatabase.endTransaction();
                    jsonReader.endArray();
                    jsonReader.close();
                    fileInputStream.close();
                    PreferenceUtils.getInstance().setIntPreference("signalmap_common_db_version", ((Integer) sigmapNewestFile.first).intValue());
                    PreferenceUtils.getInstance().setIntPreference("signalmap_common_json_version", ((Integer) sigmapNewestFile.first).intValue());
                    return true;
                } catch (Throwable th2) {
                    try {
                        jsonReader.close();
                        throw th2;
                    } catch (Throwable th3) {
                        th2.addSuppressed(th3);
                        throw th2;
                    }
                }
            } catch (Throwable th4) {
                try {
                    fileInputStream.close();
                    throw th4;
                } catch (Throwable th5) {
                    th4.addSuppressed(th5);
                    throw th4;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* JADX WARN: Code duplicated, block: B:112:0x018f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:116:0x019d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:129:? A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:131:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:55:0x00fe A[Catch: all -> 0x0087, TryCatch #5 {all -> 0x0087, blocks: (B:18:0x005d, B:20:0x006b, B:21:0x006f, B:55:0x00fe, B:23:0x0074, B:25:0x007a, B:28:0x008a, B:30:0x0090, B:31:0x0095, B:33:0x009b, B:34:0x00a8, B:36:0x00ae, B:37:0x00b2, B:39:0x00b8, B:40:0x00bc, B:42:0x00c2, B:43:0x00c6, B:45:0x00cc, B:46:0x00d8, B:48:0x00de, B:49:0x00ea, B:51:0x00f0, B:52:0x00f4, B:54:0x00fa, B:66:0x013d, B:57:0x0116, B:60:0x0122, B:63:0x012d, B:65:0x0138, B:67:0x0147), top: B:114:0x005d }] */
    /* JADX WARN: Instruction removed from duplicated block: B:55:0x00fe, please report this as an issue */
    public boolean readAndLoadDefaultSignalMap() throws Throwable {
        InputStream inputStream;
        Throwable th;
        String name;
        String str;
        try {
            InputStream inputStreamOpen = this.mContext.getResources().getAssets().open("sm_default.xml");
            try {
                try {
                    XmlPullParser xmlPullParserNewPullParser = Xml.newPullParser();
                    xmlPullParserNewPullParser.setInput(inputStreamOpen, "UTF-8");
                    SQLiteDatabase writableDatabase = getWritableDatabase();
                    clearSignalMapTable(writableDatabase);
                    writableDatabase.beginTransaction();
                    try {
                        int eventType = xmlPullParserNewPullParser.getEventType();
                        String str2 = null;
                        ContentValues contentValues = null;
                        while (eventType != 1) {
                            XmlPullParser xmlPullParser = xmlPullParserNewPullParser;
                            if (eventType == 2) {
                                name = xmlPullParser.getName();
                                if ("array".equals(name)) {
                                    contentValues = new ContentValues();
                                }
                            } else if (eventType != 3) {
                                if (eventType == 4 && contentValues != null && str2 != null) {
                                    try {
                                        String strTrim = xmlPullParser.getText().trim();
                                        if (!strTrim.isEmpty()) {
                                            switch (str2.hashCode()) {
                                                case -1144292445:
                                                    if (!str2.equals("sublocality")) {
                                                        log("unsupported name:" + str2);
                                                    } else {
                                                        contentValues.put("sublocality", strTrim);
                                                    }
                                                    break;
                                                case 98494:
                                                    if (!str2.equals("cid")) {
                                                        log("unsupported name:" + str2);
                                                    } else {
                                                        contentValues.put("cid", strTrim);
                                                    }
                                                    break;
                                                case 110806:
                                                    if (!str2.equals("pci")) {
                                                        log("unsupported name:" + str2);
                                                    } else {
                                                        contentValues.put("pci", Integer.valueOf(Integer.parseInt(strTrim)));
                                                    }
                                                    break;
                                                case 112677:
                                                    if (!str2.equals("rat")) {
                                                        log("unsupported name:" + str2);
                                                    } else {
                                                        contentValues.put("rat", Integer.valueOf(Integer.parseInt(strTrim)));
                                                    }
                                                    break;
                                                case 114582:
                                                    if (!str2.equals("tac")) {
                                                        log("unsupported name:" + str2);
                                                    } else {
                                                        contentValues.put("tac", strTrim);
                                                    }
                                                    break;
                                                case 3016245:
                                                    if (!str2.equals("band")) {
                                                        log("unsupported name:" + str2);
                                                    } else {
                                                        contentValues.put("band", strTrim);
                                                    }
                                                    break;
                                                case 3053931:
                                                    if (!str2.equals("city")) {
                                                        log("unsupported name:" + str2);
                                                    } else {
                                                        contentValues.put("city", strTrim);
                                                    }
                                                    break;
                                                case 3443869:
                                                    if (!str2.equals("plmn")) {
                                                        log("unsupported name:" + str2);
                                                    } else {
                                                        contentValues.put("plmn", Integer.valueOf(Integer.parseInt(strTrim)));
                                                    }
                                                    break;
                                                case 93078912:
                                                    if (!str2.equals("arfcn")) {
                                                        log("unsupported name:" + str2);
                                                    } else {
                                                        contentValues.put("arfcn", strTrim);
                                                    }
                                                    break;
                                                case 94434409:
                                                    if (!str2.equals("cause")) {
                                                        log("unsupported name:" + str2);
                                                    } else {
                                                        contentValues.put("cause", Integer.valueOf(Integer.parseInt(strTrim)));
                                                    }
                                                    break;
                                                default:
                                                    log("unsupported name:" + str2);
                                                    break;
                                            }
                                        }
                                    } catch (Throwable th2) {
                                        th = th2;
                                        writableDatabase.endTransaction();
                                        throw th;
                                    }
                                }
                                name = str2;
                            } else {
                                if (!"array".equals(xmlPullParser.getName()) || contentValues == null) {
                                    str = null;
                                } else {
                                    str = null;
                                    writableDatabase.insert("SignalMapCells", null, contentValues);
                                    contentValues = null;
                                }
                                name = str;
                            }
                            str2 = name;
                            eventType = xmlPullParser.next();
                            xmlPullParserNewPullParser = xmlPullParser;
                        }
                        writableDatabase.setTransactionSuccessful();
                        writableDatabase.endTransaction();
                        PreferenceUtils.getInstance().setIntPreference("signalmap_common_db_version", 1);
                        PreferenceUtils.getInstance().setIntPreference("signalmap_common_json_version", 1);
                        if (inputStreamOpen != null) {
                            try {
                                inputStreamOpen.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return true;
                    } catch (Throwable th3) {
                        th = th3;
                    }
                } catch (Exception e2) {
                    e = e2;
                    inputStream = inputStreamOpen;
                    try {
                        e.printStackTrace();
                        if (inputStream != null) {
                            return false;
                        }
                        try {
                            inputStream.close();
                            return false;
                        } catch (IOException e3) {
                            e3.printStackTrace();
                            return false;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        th = th;
                        if (inputStream != null) {
                            throw th;
                        }
                        try {
                            inputStream.close();
                            throw th;
                        } catch (IOException e4) {
                            e4.printStackTrace();
                            throw th;
                        }
                    }
                } catch (Throwable th5) {
                    th = th5;
                    th = th;
                    inputStream = inputStreamOpen;
                    if (inputStream != null) {
                        throw th;
                    }
                    inputStream.close();
                    throw th;
                }
            } catch (Exception e5) {
                e = e5;
                inputStream = inputStreamOpen;
                e.printStackTrace();
                if (inputStream != null) {
                    return false;
                }
                inputStream.close();
                return false;
            } catch (Throwable th6) {
                th = th6;
                th = th;
                inputStream = inputStreamOpen;
                if (inputStream != null) {
                    throw th;
                }
                inputStream.close();
                throw th;
            }
        } catch (Exception e6) {
            e = e6;
            inputStream = null;
        } catch (Throwable th7) {
            th = th7;
            inputStream = null;
            th = th;
            if (inputStream != null) {
                throw th;
            }
            inputStream.close();
            throw th;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:26:0x00ab  */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r20v0, types: [android.database.sqlite.SQLiteOpenHelper, com.android.phone.CloudCellDbHelper] */
    /* JADX WARN: Type inference failed for: r20v1 */
    /* JADX WARN: Type inference failed for: r20v2, types: [java.io.FileInputStream] */
    /* JADX WARN: Type inference failed for: r20v3 */
    /* JADX WARN: Type inference failed for: r20v4 */
    /* JADX WARN: Type inference failed for: r20v5 */
    public boolean readAndLoadSignalMapHst(boolean z) throws Throwable {
        Throwable th;
        Throwable th2;
        String str = "sequences";
        String str2 = "cid";
        Pair sigmapNewestFile = getSigmapNewestFile(this.mDataDownloadDir, "blackcelldb_hst_\\d+\\.json", "hstcell_version");
        if (sigmapNewestFile == null || sigmapNewestFile.second == null) {
            return false;
        }
        SQLiteDatabase writableDatabase = getWritableDatabase();
        int intPreference = PreferenceUtils.getInstance().getIntPreference("hstdb_version");
        if (!z && intPreference >= ((Integer) sigmapNewestFile.first).intValue()) {
            log(" no need to update db");
            return false;
        }
        File file = (File) sigmapNewestFile.second;
        log("readAndLoadSignalMapHst:" + file);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                try {
                    JsonReader jsonReader = new JsonReader(new InputStreamReader(fileInputStream));
                    try {
                        try {
                            jsonReader.beginArray();
                            writableDatabase.beginTransaction();
                            try {
                                writableDatabase.execSQL("DROP TABLE IF EXISTS SignalMapHstCells");
                                writableDatabase.execSQL("CREATE TABLE IF NOT EXISTS SignalMapHstCells (_id INTEGER PRIMARY KEY AUTOINCREMENT,railtrip TEXT,rat INTEGER,plmn INTEGER,cid TEXT,band TEXT,arfcn TEXT,pci INTEGER,tac TEXT,score INTEGER,sequences INTEGER)");
                                while (jsonReader.hasNext()) {
                                    jsonReader.beginObject();
                                    ContentValues contentValues = new ContentValues();
                                    while (jsonReader.hasNext()) {
                                        FileInputStream fileInputStream2 = fileInputStream;
                                        try {
                                            String strNextName = jsonReader.nextName();
                                            switch (strNextName.hashCode()) {
                                                case -1113685806:
                                                    if (!strNextName.equals(str)) {
                                                        log("unsupported name:" + strNextName);
                                                    } else {
                                                        contentValues.put(str, Integer.valueOf(jsonReader.nextInt()));
                                                    }
                                                    break;
                                                case 98494:
                                                    if (!strNextName.equals(str2)) {
                                                        log("unsupported name:" + strNextName);
                                                    } else {
                                                        contentValues.put(str2, jsonReader.nextString());
                                                    }
                                                    break;
                                                case 110806:
                                                    if (!strNextName.equals("pci")) {
                                                        log("unsupported name:" + strNextName);
                                                    } else {
                                                        contentValues.put("pci", Integer.valueOf(jsonReader.nextInt()));
                                                    }
                                                    break;
                                                case 112677:
                                                    if (!strNextName.equals("rat")) {
                                                        log("unsupported name:" + strNextName);
                                                    } else {
                                                        contentValues.put("rat", Integer.valueOf(jsonReader.nextInt()));
                                                    }
                                                    break;
                                                case 114582:
                                                    if (!strNextName.equals("tac")) {
                                                        log("unsupported name:" + strNextName);
                                                    } else {
                                                        contentValues.put("tac", jsonReader.nextString());
                                                    }
                                                    break;
                                                case 3016245:
                                                    if (!strNextName.equals("band")) {
                                                        log("unsupported name:" + strNextName);
                                                    } else {
                                                        contentValues.put("band", jsonReader.nextString());
                                                    }
                                                    break;
                                                case 3443869:
                                                    if (!strNextName.equals("plmn")) {
                                                        log("unsupported name:" + strNextName);
                                                    } else {
                                                        contentValues.put("plmn", Integer.valueOf(jsonReader.nextInt()));
                                                    }
                                                    break;
                                                case 93078912:
                                                    if (!strNextName.equals("arfcn")) {
                                                        log("unsupported name:" + strNextName);
                                                    } else {
                                                        contentValues.put("arfcn", jsonReader.nextString());
                                                    }
                                                    break;
                                                case 109264530:
                                                    if (!strNextName.equals("score")) {
                                                        log("unsupported name:" + strNextName);
                                                    } else {
                                                        contentValues.put("score", Integer.valueOf(jsonReader.nextInt()));
                                                    }
                                                    break;
                                                case 114796215:
                                                    if (!strNextName.equals("railtrip")) {
                                                        log("unsupported name:" + strNextName);
                                                    } else {
                                                        contentValues.put("railtrip", jsonReader.nextString());
                                                    }
                                                    break;
                                                default:
                                                    log("unsupported name:" + strNextName);
                                                    break;
                                            }
                                            fileInputStream = fileInputStream2;
                                            str = str;
                                            str2 = str2;
                                        } catch (Throwable th3) {
                                            th = th3;
                                            writableDatabase.endTransaction();
                                            throw th;
                                        }
                                    }
                                    writableDatabase.insert("SignalMapHstCells", null, contentValues);
                                    jsonReader.endObject();
                                    fileInputStream = fileInputStream;
                                    str = str;
                                    str2 = str2;
                                }
                                FileInputStream fileInputStream3 = fileInputStream;
                                writableDatabase.setTransactionSuccessful();
                                writableDatabase.endTransaction();
                                jsonReader.endArray();
                                jsonReader.close();
                                fileInputStream3.close();
                                PreferenceUtils.getInstance().setIntPreference("hstdb_version", ((Integer) sigmapNewestFile.first).intValue());
                                PreferenceUtils.getInstance().setIntPreference("hstcell_version", ((Integer) sigmapNewestFile.first).intValue());
                                return true;
                            } catch (Throwable th4) {
                                th = th4;
                            }
                        } catch (Throwable th5) {
                            th = th5;
                            this = fileInputStream;
                            th2 = th;
                            try {
                                jsonReader.close();
                                throw th2;
                            } catch (Throwable th6) {
                                th2.addSuppressed(th6);
                                throw th2;
                            }
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        th2 = th;
                        jsonReader.close();
                        throw th2;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    th = th;
                    try {
                        this.close();
                        throw th;
                    } catch (Throwable th9) {
                        th.addSuppressed(th9);
                        throw th;
                    }
                }
            } catch (Throwable th10) {
                th = th10;
                this = fileInputStream;
                th = th;
                this.close();
                throw th;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* JADX WARN: Code duplicated, block: B:113:0x019d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:117:0x01ab A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:128:? A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:130:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:55:0x010f A[Catch: all -> 0x0088, TryCatch #8 {all -> 0x0088, blocks: (B:18:0x0066, B:20:0x0074, B:21:0x0078, B:55:0x010f, B:23:0x007d, B:25:0x0083, B:28:0x008b, B:30:0x0091, B:31:0x009e, B:33:0x00a4, B:34:0x00a9, B:36:0x00af, B:37:0x00bb, B:39:0x00c1, B:40:0x00c5, B:42:0x00cb, B:43:0x00cf, B:45:0x00d5, B:46:0x00e1, B:48:0x00e7, B:49:0x00f3, B:51:0x00f9, B:52:0x00fd, B:54:0x0103, B:66:0x014d, B:57:0x0126, B:60:0x0132, B:63:0x013d, B:65:0x0148, B:67:0x0155), top: B:115:0x0066 }] */
    /* JADX WARN: Instruction removed from duplicated block: B:55:0x010f, please report this as an issue */
    public boolean readAndLoadDefaultHSTSignalMap() throws Throwable {
        Throwable th;
        InputStream inputStream;
        String str;
        try {
            InputStream inputStreamOpen = this.mContext.getResources().getAssets().open("sm_hst_default.xml");
            try {
                try {
                    XmlPullParser xmlPullParserNewPullParser = Xml.newPullParser();
                    xmlPullParserNewPullParser.setInput(inputStreamOpen, "UTF-8");
                    SQLiteDatabase writableDatabase = getWritableDatabase();
                    writableDatabase.beginTransaction();
                    try {
                        writableDatabase.execSQL("DROP TABLE IF EXISTS SignalMapHstCells");
                        writableDatabase.execSQL("CREATE TABLE IF NOT EXISTS SignalMapHstCells (_id INTEGER PRIMARY KEY AUTOINCREMENT,railtrip TEXT,rat INTEGER,plmn INTEGER,cid TEXT,band TEXT,arfcn TEXT,pci INTEGER,tac TEXT,score INTEGER,sequences INTEGER)");
                        int eventType = xmlPullParserNewPullParser.getEventType();
                        String name = null;
                        ContentValues contentValues = null;
                        while (eventType != 1) {
                            XmlPullParser xmlPullParser = xmlPullParserNewPullParser;
                            if (eventType == 2) {
                                name = xmlPullParser.getName();
                                if ("highway".equals(name)) {
                                    contentValues = new ContentValues();
                                }
                            } else if (eventType == 3) {
                                if (!"highway".equals(xmlPullParser.getName()) || contentValues == null) {
                                    str = null;
                                } else {
                                    str = null;
                                    writableDatabase.insert("SignalMapHstCells", null, contentValues);
                                    contentValues = null;
                                }
                                name = str;
                            } else if (eventType == 4 && contentValues != null && name != null) {
                                try {
                                    String strTrim = xmlPullParser.getText().trim();
                                    if (!strTrim.isEmpty()) {
                                        switch (name.hashCode()) {
                                            case -1113685806:
                                                if (!name.equals("sequences")) {
                                                    log("unsupported name:" + name);
                                                } else {
                                                    contentValues.put("sequences", Integer.valueOf(Integer.parseInt(strTrim)));
                                                }
                                                break;
                                            case 98494:
                                                if (!name.equals("cid")) {
                                                    log("unsupported name:" + name);
                                                } else {
                                                    contentValues.put("cid", strTrim);
                                                }
                                                break;
                                            case 110806:
                                                if (!name.equals("pci")) {
                                                    log("unsupported name:" + name);
                                                } else {
                                                    contentValues.put("pci", Integer.valueOf(Integer.parseInt(strTrim)));
                                                }
                                                break;
                                            case 112677:
                                                if (!name.equals("rat")) {
                                                    log("unsupported name:" + name);
                                                } else {
                                                    contentValues.put("rat", Integer.valueOf(Integer.parseInt(strTrim)));
                                                }
                                                break;
                                            case 114582:
                                                if (!name.equals("tac")) {
                                                    log("unsupported name:" + name);
                                                } else {
                                                    contentValues.put("tac", strTrim);
                                                }
                                                break;
                                            case 3016245:
                                                if (!name.equals("band")) {
                                                    log("unsupported name:" + name);
                                                } else {
                                                    contentValues.put("band", strTrim);
                                                }
                                                break;
                                            case 3443869:
                                                if (!name.equals("plmn")) {
                                                    log("unsupported name:" + name);
                                                } else {
                                                    contentValues.put("plmn", Integer.valueOf(Integer.parseInt(strTrim)));
                                                }
                                                break;
                                            case 93078912:
                                                if (!name.equals("arfcn")) {
                                                    log("unsupported name:" + name);
                                                } else {
                                                    contentValues.put("arfcn", strTrim);
                                                }
                                                break;
                                            case 109264530:
                                                if (!name.equals("score")) {
                                                    log("unsupported name:" + name);
                                                } else {
                                                    contentValues.put("score", Integer.valueOf(Integer.parseInt(strTrim)));
                                                }
                                                break;
                                            case 114796215:
                                                if (!name.equals("railtrip")) {
                                                    log("unsupported name:" + name);
                                                } else {
                                                    contentValues.put("railtrip", strTrim);
                                                }
                                                break;
                                            default:
                                                log("unsupported name:" + name);
                                                break;
                                        }
                                    }
                                } catch (Throwable th2) {
                                    th = th2;
                                    writableDatabase.endTransaction();
                                    throw th;
                                }
                            }
                            eventType = xmlPullParser.next();
                            xmlPullParserNewPullParser = xmlPullParser;
                        }
                        writableDatabase.setTransactionSuccessful();
                        writableDatabase.endTransaction();
                        PreferenceUtils.getInstance().setIntPreference("hstdb_version", 1);
                        PreferenceUtils.getInstance().setIntPreference("hstcell_version", 1);
                        if (inputStreamOpen != null) {
                            try {
                                inputStreamOpen.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return true;
                    } catch (Throwable th3) {
                        th = th3;
                    }
                } catch (Exception e2) {
                    e = e2;
                    inputStream = inputStreamOpen;
                    try {
                        e.printStackTrace();
                        if (inputStream != null) {
                            return false;
                        }
                        try {
                            inputStream.close();
                            return false;
                        } catch (IOException e3) {
                            e3.printStackTrace();
                            return false;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        if (inputStream == null) {
                            throw th;
                        }
                        try {
                            inputStream.close();
                            throw th;
                        } catch (IOException e4) {
                            e4.printStackTrace();
                            throw th;
                        }
                    }
                } catch (Throwable th5) {
                    th = th5;
                    th = th;
                    inputStream = inputStreamOpen;
                    if (inputStream == null) {
                        throw th;
                    }
                    inputStream.close();
                    throw th;
                }
            } catch (Exception e5) {
                e = e5;
                inputStream = inputStreamOpen;
                e.printStackTrace();
                if (inputStream != null) {
                    return false;
                }
                inputStream.close();
                return false;
            } catch (Throwable th6) {
                th = th6;
                th = th;
                inputStream = inputStreamOpen;
                if (inputStream == null) {
                    throw th;
                }
                inputStream.close();
                throw th;
            }
        } catch (Exception e6) {
            e = e6;
            inputStream = null;
        } catch (Throwable th7) {
            th = th7;
            inputStream = null;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:152:0x02ab A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:166:? A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:16:0x007d  */
    /* JADX WARN: Not initialized variable reg: 20, insn: 0x027c: MOVE (r1 I:??[OBJECT, ARRAY]) = (r20 I:??[OBJECT, ARRAY]), block:B:114:0x027c */
    public boolean readAndLoadHSTPreferCellSignalMap() throws Throwable {
        Throwable th;
        InputStream inputStream;
        InputStream inputStreamOpen;
        InputStream inputStream2;
        SQLiteDatabase sQLiteDatabase;
        String str;
        SQLiteDatabase sQLiteDatabase2;
        String name;
        String str2;
        String str3;
        String str4;
        String str5 = "band";
        String str6 = "cid";
        String str7 = "direction";
        try {
            inputStreamOpen = this.mContext.getResources().getAssets().open("sm_hst_prefer_cell.xml");
            try {
                try {
                    XmlPullParser xmlPullParserNewPullParser = Xml.newPullParser();
                    String str8 = "pci";
                    xmlPullParserNewPullParser.setInput(inputStreamOpen, "UTF-8");
                    SQLiteDatabase writableDatabase = getWritableDatabase();
                    writableDatabase.beginTransaction();
                    try {
                        try {
                            writableDatabase.execSQL("DROP TABLE IF EXISTS SignalMapHstPreferCells");
                            writableDatabase.execSQL("CREATE TABLE IF NOT EXISTS SignalMapHstPreferCells (_id INTEGER PRIMARY KEY AUTOINCREMENT,railtrip TEXT,direction INTEGER,rat INTEGER,plmn INTEGER,cid TEXT,band TEXT,arfcn TEXT,pci INTEGER,score INTEGER,nextrat INTEGER,nextplmn INTEGER,nextcid TEXT,nextband TEXT,nextarfcn TEXT,nextpci INTEGER,nextscore INTEGER)");
                            int eventType = xmlPullParserNewPullParser.getEventType();
                            SQLiteDatabase sQLiteDatabase3 = writableDatabase;
                            String str9 = "rat";
                            ContentValues contentValues = null;
                            String str10 = null;
                            while (eventType != 1) {
                                String str11 = str5;
                                if (eventType == 2) {
                                    String str12 = str7;
                                    str6 = str6;
                                    str = str12;
                                    sQLiteDatabase2 = sQLiteDatabase3;
                                    name = xmlPullParserNewPullParser.getName();
                                    if ("hstcell".equals(name)) {
                                        contentValues = new ContentValues();
                                    }
                                } else if (eventType != 3) {
                                    if (eventType != 4 || contentValues == null || str10 == null) {
                                        str = str7;
                                        str4 = str9;
                                    } else {
                                        try {
                                            String strTrim = xmlPullParserNewPullParser.getText().trim();
                                            if (!strTrim.isEmpty()) {
                                                switch (str10.hashCode()) {
                                                    case -962590849:
                                                        str3 = str6;
                                                        str4 = str9;
                                                        str7 = str7;
                                                        if (!str10.equals(str7)) {
                                                            str6 = str3;
                                                            StringBuilder sb = new StringBuilder();
                                                            str = str7;
                                                            sb.append("unsupported name:");
                                                            sb.append(str10);
                                                            log(sb.toString());
                                                        } else {
                                                            str6 = str3;
                                                            contentValues.put(str7, Integer.valueOf(Integer.parseInt(strTrim)));
                                                            str = str7;
                                                        }
                                                        break;
                                                    case 98494:
                                                        str3 = str6;
                                                        str4 = str9;
                                                        if (!str10.equals(str3)) {
                                                            str7 = str7;
                                                            str6 = str3;
                                                            StringBuilder sb2 = new StringBuilder();
                                                            str = str7;
                                                            sb2.append("unsupported name:");
                                                            sb2.append(str10);
                                                            log(sb2.toString());
                                                        } else {
                                                            contentValues.put(str3, strTrim);
                                                            str = str7;
                                                            str6 = str3;
                                                        }
                                                        break;
                                                    case 110806:
                                                        String str13 = str8;
                                                        str4 = str9;
                                                        if (!str10.equals(str13)) {
                                                            str8 = str13;
                                                            str6 = str6;
                                                            StringBuilder sb3 = new StringBuilder();
                                                            str = str7;
                                                            sb3.append("unsupported name:");
                                                            sb3.append(str10);
                                                            log(sb3.toString());
                                                        } else {
                                                            contentValues.put(str13, Integer.valueOf(Integer.parseInt(strTrim)));
                                                            String str14 = str7;
                                                            str6 = str6;
                                                            str = str14;
                                                            str8 = str13;
                                                        }
                                                        break;
                                                    case 112677:
                                                        str4 = str9;
                                                        if (!str10.equals(str4)) {
                                                            str11 = str11;
                                                            str6 = str6;
                                                            StringBuilder sb4 = new StringBuilder();
                                                            str = str7;
                                                            sb4.append("unsupported name:");
                                                            sb4.append(str10);
                                                            log(sb4.toString());
                                                        } else {
                                                            contentValues.put(str4, Integer.valueOf(Integer.parseInt(strTrim)));
                                                            String str15 = str7;
                                                            str6 = str6;
                                                            str = str15;
                                                            str11 = str11;
                                                        }
                                                        break;
                                                    case 3016245:
                                                        if (!str10.equals(str11)) {
                                                            str11 = str11;
                                                            str4 = str9;
                                                            str6 = str6;
                                                            StringBuilder sb5 = new StringBuilder();
                                                            str = str7;
                                                            sb5.append("unsupported name:");
                                                            sb5.append(str10);
                                                            log(sb5.toString());
                                                        } else {
                                                            contentValues.put(str11, strTrim);
                                                            str = str7;
                                                            str11 = str11;
                                                            str4 = str9;
                                                        }
                                                        break;
                                                    case 3443869:
                                                        if (str10.equals("plmn")) {
                                                            contentValues.put("plmn", Integer.valueOf(Integer.parseInt(strTrim)));
                                                            str = str7;
                                                            str4 = str9;
                                                        }
                                                        str4 = str9;
                                                        str6 = str6;
                                                        StringBuilder sb6 = new StringBuilder();
                                                        str = str7;
                                                        sb6.append("unsupported name:");
                                                        sb6.append(str10);
                                                        log(sb6.toString());
                                                        break;
                                                    case 93078912:
                                                        if (str10.equals("arfcn")) {
                                                            contentValues.put("arfcn", strTrim);
                                                            str = str7;
                                                            str4 = str9;
                                                        }
                                                        str4 = str9;
                                                        str6 = str6;
                                                        StringBuilder sb7 = new StringBuilder();
                                                        str = str7;
                                                        sb7.append("unsupported name:");
                                                        sb7.append(str10);
                                                        log(sb7.toString());
                                                        break;
                                                    case 109264530:
                                                        if (str10.equals("score")) {
                                                            contentValues.put("score", Integer.valueOf(Integer.parseInt(strTrim)));
                                                            str = str7;
                                                            str4 = str9;
                                                        }
                                                        str4 = str9;
                                                        str6 = str6;
                                                        StringBuilder sb8 = new StringBuilder();
                                                        str = str7;
                                                        sb8.append("unsupported name:");
                                                        sb8.append(str10);
                                                        log(sb8.toString());
                                                        break;
                                                    case 114796215:
                                                        if (str10.equals("railtrip")) {
                                                            contentValues.put("railtrip", strTrim);
                                                            str = str7;
                                                            str4 = str9;
                                                        }
                                                        str4 = str9;
                                                        str6 = str6;
                                                        StringBuilder sb9 = new StringBuilder();
                                                        str = str7;
                                                        sb9.append("unsupported name:");
                                                        sb9.append(str10);
                                                        log(sb9.toString());
                                                        break;
                                                    case 1219009133:
                                                        if (str10.equals("nextarfcn")) {
                                                            contentValues.put("nextarfcn", strTrim);
                                                            str = str7;
                                                            str4 = str9;
                                                        }
                                                        str4 = str9;
                                                        str6 = str6;
                                                        StringBuilder sb10 = new StringBuilder();
                                                        str = str7;
                                                        sb10.append("unsupported name:");
                                                        sb10.append(str10);
                                                        log(sb10.toString());
                                                        break;
                                                    case 1235194751:
                                                        if (str10.equals("nextscore")) {
                                                            contentValues.put("nextscore", Integer.valueOf(Integer.parseInt(strTrim)));
                                                            str = str7;
                                                            str4 = str9;
                                                        }
                                                        str4 = str9;
                                                        str6 = str6;
                                                        StringBuilder sb11 = new StringBuilder();
                                                        str = str7;
                                                        sb11.append("unsupported name:");
                                                        sb11.append(str10);
                                                        log(sb11.toString());
                                                        break;
                                                    case 1424809896:
                                                        if (str10.equals("nextband")) {
                                                            contentValues.put("nextband", strTrim);
                                                            str = str7;
                                                            str4 = str9;
                                                        }
                                                        str4 = str9;
                                                        str6 = str6;
                                                        StringBuilder sb12 = new StringBuilder();
                                                        str = str7;
                                                        sb12.append("unsupported name:");
                                                        sb12.append(str10);
                                                        log(sb12.toString());
                                                        break;
                                                    case 1425237520:
                                                        if (str10.equals("nextplmn")) {
                                                            contentValues.put("nextplmn", Integer.valueOf(Integer.parseInt(strTrim)));
                                                            str = str7;
                                                            str4 = str9;
                                                        }
                                                        str4 = str9;
                                                        str6 = str6;
                                                        StringBuilder sb13 = new StringBuilder();
                                                        str = str7;
                                                        sb13.append("unsupported name:");
                                                        sb13.append(str10);
                                                        log(sb13.toString());
                                                        break;
                                                    case 1847078123:
                                                        if (str10.equals("nextcid")) {
                                                            contentValues.put("nextcid", strTrim);
                                                            str = str7;
                                                            str4 = str9;
                                                        }
                                                        str4 = str9;
                                                        str6 = str6;
                                                        StringBuilder sb14 = new StringBuilder();
                                                        str = str7;
                                                        sb14.append("unsupported name:");
                                                        sb14.append(str10);
                                                        log(sb14.toString());
                                                        break;
                                                    case 1847090435:
                                                        if (str10.equals("nextpci")) {
                                                            contentValues.put("nextpci", Integer.valueOf(Integer.parseInt(strTrim)));
                                                            str = str7;
                                                            str4 = str9;
                                                        }
                                                        str4 = str9;
                                                        str6 = str6;
                                                        StringBuilder sb15 = new StringBuilder();
                                                        str = str7;
                                                        sb15.append("unsupported name:");
                                                        sb15.append(str10);
                                                        log(sb15.toString());
                                                        break;
                                                    case 1847092306:
                                                        if (str10.equals("nextrat")) {
                                                            contentValues.put("nextrat", Integer.valueOf(Integer.parseInt(strTrim)));
                                                            str = str7;
                                                            str4 = str9;
                                                        }
                                                        str4 = str9;
                                                        str6 = str6;
                                                        StringBuilder sb16 = new StringBuilder();
                                                        str = str7;
                                                        sb16.append("unsupported name:");
                                                        sb16.append(str10);
                                                        log(sb16.toString());
                                                        break;
                                                    default:
                                                        str4 = str9;
                                                        str6 = str6;
                                                        StringBuilder sb17 = new StringBuilder();
                                                        str = str7;
                                                        sb17.append("unsupported name:");
                                                        sb17.append(str10);
                                                        log(sb17.toString());
                                                        break;
                                                }
                                            } else {
                                                str = str7;
                                                str4 = str9;
                                            }
                                        } catch (Throwable th2) {
                                            th = th2;
                                            sQLiteDatabase = sQLiteDatabase3;
                                            sQLiteDatabase.endTransaction();
                                            throw th;
                                        }
                                    }
                                    sQLiteDatabase2 = sQLiteDatabase3;
                                    name = str10;
                                    str9 = str4;
                                } else {
                                    String str16 = str7;
                                    str6 = str6;
                                    str = str16;
                                    try {
                                        if (!"hstcell".equals(xmlPullParserNewPullParser.getName()) || contentValues == null) {
                                            str2 = null;
                                            sQLiteDatabase2 = sQLiteDatabase3;
                                        } else {
                                            str2 = null;
                                            sQLiteDatabase2 = sQLiteDatabase3;
                                            sQLiteDatabase2.insert("SignalMapHstPreferCells", null, contentValues);
                                            contentValues = null;
                                        }
                                        name = str2;
                                    } catch (Throwable th3) {
                                        th = th3;
                                        sQLiteDatabase2 = sQLiteDatabase3;
                                        sQLiteDatabase = sQLiteDatabase2;
                                        sQLiteDatabase.endTransaction();
                                        throw th;
                                    }
                                }
                                try {
                                    String str17 = str6;
                                    str7 = str;
                                    str6 = str17;
                                    str10 = name;
                                    sQLiteDatabase3 = sQLiteDatabase2;
                                    eventType = xmlPullParserNewPullParser.next();
                                    str5 = str11;
                                } catch (Throwable th4) {
                                    th = th4;
                                    sQLiteDatabase = sQLiteDatabase2;
                                    sQLiteDatabase.endTransaction();
                                    throw th;
                                }
                            }
                            sQLiteDatabase = sQLiteDatabase3;
                            try {
                                sQLiteDatabase.setTransactionSuccessful();
                                sQLiteDatabase.endTransaction();
                                PreferenceUtils.getInstance().setIntPreference("hst_preferPcell_db_version", 1);
                                log("readAndLoadHSTPreferCellSignalMap");
                                if (inputStreamOpen != null) {
                                    try {
                                        inputStreamOpen.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                return true;
                            } catch (Throwable th5) {
                                th = th5;
                                sQLiteDatabase.endTransaction();
                                throw th;
                            }
                        } catch (Throwable th6) {
                            th = th6;
                            sQLiteDatabase = writableDatabase;
                        }
                    } catch (Exception e2) {
                        e = e2;
                        inputStreamOpen = inputStream2;
                        e.printStackTrace();
                        if (inputStreamOpen == null) {
                            return false;
                        }
                        try {
                            inputStreamOpen.close();
                            return false;
                        } catch (IOException e3) {
                            e3.printStackTrace();
                            return false;
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        th = th;
                        if (inputStream != null) {
                            throw th;
                        }
                        try {
                            inputStream.close();
                            throw th;
                        } catch (IOException e4) {
                            e4.printStackTrace();
                            throw th;
                        }
                    }
                } catch (Exception e5) {
                    e = e5;
                }
            } catch (Throwable th8) {
                th = th8;
                inputStream = inputStreamOpen;
            }
        } catch (Exception e6) {
            e = e6;
            inputStreamOpen = null;
        } catch (Throwable th9) {
            th = th9;
            inputStream = null;
            if (inputStream != null) {
                throw th;
            }
            inputStream.close();
            throw th;
        }
    }

    /* JADX WARN: Code duplicated, block: B:113:? A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:49:0x00df A[Catch: all -> 0x007e, TryCatch #8 {all -> 0x007e, blocks: (B:5:0x0037, B:18:0x005c, B:20:0x006a, B:21:0x006e, B:49:0x00df, B:23:0x0073, B:25:0x0079, B:28:0x0081, B:30:0x0087, B:31:0x0093, B:33:0x0099, B:34:0x009d, B:36:0x00a3, B:37:0x00a7, B:39:0x00ad, B:40:0x00b1, B:42:0x00b7, B:43:0x00c3, B:45:0x00c9, B:46:0x00d5, B:48:0x00db, B:60:0x011d, B:51:0x00f6, B:54:0x0102, B:57:0x010d, B:59:0x0118, B:61:0x0124), top: B:103:0x0037, outer: #8 }] */
    /* JADX WARN: Code duplicated, block: B:99:0x016b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Instruction removed from duplicated block: B:49:0x00df, please report this as an issue */
    public boolean readAndLoadDefaultSubwaySignalMap() throws Throwable {
        Throwable th;
        InputStream inputStream;
        String str;
        try {
            InputStream inputStreamOpen = this.mContext.getResources().getAssets().open("sm_subway_default.xml");
            try {
                XmlPullParser xmlPullParserNewPullParser = Xml.newPullParser();
                xmlPullParserNewPullParser.setInput(inputStreamOpen, "UTF-8");
                SQLiteDatabase writableDatabase = getWritableDatabase();
                writableDatabase.beginTransaction();
                try {
                    writableDatabase.execSQL("DROP TABLE IF EXISTS SignalMapSubwayCells");
                    writableDatabase.execSQL("CREATE TABLE IF NOT EXISTS SignalMapSubwayCells (_id INTEGER PRIMARY KEY AUTOINCREMENT,city TEXT,rat INTEGER,plmn INTEGER,cid TEXT,band TEXT,arfcn TEXT,pci INTEGER,tac TEXT)");
                    ContentValues contentValues = null;
                    String name = null;
                    for (int eventType = xmlPullParserNewPullParser.getEventType(); eventType != 1; eventType = xmlPullParserNewPullParser.next()) {
                        if (eventType == 2) {
                            name = xmlPullParserNewPullParser.getName();
                            if ("subway".equals(name)) {
                                contentValues = new ContentValues();
                            }
                        } else if (eventType == 3) {
                            if (!"subway".equals(xmlPullParserNewPullParser.getName()) || contentValues == null) {
                                str = null;
                            } else {
                                str = null;
                                writableDatabase.insert("SignalMapSubwayCells", null, contentValues);
                                contentValues = null;
                            }
                            name = str;
                        } else if (eventType == 4 && contentValues != null && name != null) {
                            String strTrim = xmlPullParserNewPullParser.getText().trim();
                            if (!strTrim.isEmpty()) {
                                switch (name.hashCode()) {
                                    case 98494:
                                        if (name.equals("cid")) {
                                            contentValues.put("cid", strTrim);
                                        } else {
                                            log("unsupported name:" + name);
                                        }
                                        break;
                                    case 110806:
                                        if (name.equals("pci")) {
                                            contentValues.put("pci", Integer.valueOf(Integer.parseInt(strTrim)));
                                        } else {
                                            log("unsupported name:" + name);
                                        }
                                        break;
                                    case 112677:
                                        if (name.equals("rat")) {
                                            contentValues.put("rat", Integer.valueOf(Integer.parseInt(strTrim)));
                                        } else {
                                            log("unsupported name:" + name);
                                        }
                                        break;
                                    case 114582:
                                        if (name.equals("tac")) {
                                            contentValues.put("tac", strTrim);
                                        } else {
                                            log("unsupported name:" + name);
                                        }
                                        break;
                                    case 3016245:
                                        if (name.equals("band")) {
                                            contentValues.put("band", strTrim);
                                        } else {
                                            log("unsupported name:" + name);
                                        }
                                        break;
                                    case 3053931:
                                        if (name.equals("city")) {
                                            contentValues.put("city", strTrim);
                                        } else {
                                            log("unsupported name:" + name);
                                        }
                                        break;
                                    case 3443869:
                                        if (name.equals("plmn")) {
                                            contentValues.put("plmn", Integer.valueOf(Integer.parseInt(strTrim)));
                                        } else {
                                            log("unsupported name:" + name);
                                        }
                                        break;
                                    case 93078912:
                                        if (name.equals("arfcn")) {
                                            contentValues.put("arfcn", strTrim);
                                        } else {
                                            log("unsupported name:" + name);
                                        }
                                        break;
                                    default:
                                        log("unsupported name:" + name);
                                        break;
                                }
                            }
                        }
                    }
                    writableDatabase.setTransactionSuccessful();
                    writableDatabase.endTransaction();
                    PreferenceUtils.getInstance().setIntPreference("subway_db_version", 1);
                    log("readAndLoadDefaultSubwaySignalMap");
                    if (inputStreamOpen != null) {
                        try {
                            inputStreamOpen.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return true;
                } catch (Throwable th2) {
                    writableDatabase.endTransaction();
                    throw th2;
                }
            } catch (Exception e2) {
                e = e2;
                inputStream = inputStreamOpen;
                try {
                    e.printStackTrace();
                    if (inputStream == null) {
                        return false;
                    }
                    try {
                        inputStream.close();
                        return false;
                    } catch (IOException e3) {
                        e3.printStackTrace();
                        return false;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    if (inputStream != null) {
                        throw th;
                    }
                    try {
                        inputStream.close();
                        throw th;
                    } catch (IOException e4) {
                        e4.printStackTrace();
                        throw th;
                    }
                }
            } catch (Throwable th4) {
                th = th4;
                inputStream = inputStreamOpen;
                if (inputStream != null) {
                    throw th;
                }
                inputStream.close();
                throw th;
            }
        } catch (Exception e5) {
            e = e5;
            inputStream = null;
        } catch (Throwable th5) {
            th = th5;
            inputStream = null;
        }
    }

    /* JADX WARN: Code duplicated, block: B:142:0x02b3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:148:0x02c0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:162:? A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:39:0x00dd A[Catch: all -> 0x00b3, TryCatch #4 {all -> 0x00b3, blocks: (B:17:0x0083, B:22:0x0098, B:23:0x009c, B:39:0x00dd, B:25:0x00a0, B:27:0x00a6, B:30:0x00b6, B:32:0x00bc, B:33:0x00c1, B:35:0x00c7, B:36:0x00d3, B:38:0x00d9, B:41:0x00f2, B:52:0x0119, B:46:0x0101, B:48:0x0107, B:99:0x0258, B:49:0x010c, B:51:0x0112, B:53:0x012c, B:58:0x013b, B:64:0x015a, B:68:0x0169, B:71:0x0188, B:74:0x0190, B:77:0x01ae, B:80:0x01bc, B:82:0x01cb, B:87:0x020f, B:89:0x0224, B:92:0x0231, B:94:0x0237, B:95:0x0244, B:100:0x0262), top: B:144:0x0083 }] */
    /* JADX WARN: Instruction removed from duplicated block: B:39:0x00dd, please report this as an issue */
    /* JADX WARN: Switch 'out' block B:56:0x0136 for B:23:0x009c already processed. Defaulting to fallback option. */
    public boolean readAndLoadDefaultNOMLSignalMap() throws Throwable {
        InputStream inputStream;
        boolean z;
        Throwable th;
        ArrayList arrayList;
        double d;
        String str;
        boolean z2;
        double d2;
        String str2 = "cid";
        String str3 = "lac";
        try {
            InputStream inputStreamOpen = this.mContext.getResources().getAssets().open("sm_noml_default.xml");
            try {
                try {
                    XmlPullParser xmlPullParserNewPullParser = Xml.newPullParser();
                    xmlPullParserNewPullParser.setInput(inputStreamOpen, "UTF-8");
                    SQLiteDatabase writableDatabase = getWritableDatabase();
                    writableDatabase.beginTransaction();
                    try {
                        writableDatabase.execSQL("DROP TABLE IF EXISTS SignalMapNOMLCells");
                        writableDatabase.execSQL("CREATE TABLE IF NOT EXISTS SignalMapNOMLCells (_id INTEGER PRIMARY KEY AUTOINCREMENT,nomlname TEXT,nomltype TEXT,city TEXT,plmn INTEGER,lac INTEGER,cid TEXT)");
                        writableDatabase.execSQL("CREATE INDEX IF NOT EXISTS idx_noml_cid_type ON SignalMapNOMLCells(cid,nomltype);");
                        writableDatabase.execSQL("DROP TABLE IF EXISTS SignalMapNOMLGPS");
                        writableDatabase.execSQL("CREATE TABLE IF NOT EXISTS SignalMapNOMLGPS (_id INTEGER PRIMARY KEY AUTOINCREMENT,nomlname TEXT,lon REAL,lat REAL)");
                        int eventType = xmlPullParserNewPullParser.getEventType();
                        String str4 = "";
                        String str5 = str4;
                        double d3 = 0.0d;
                        ArrayList arrayList2 = null;
                        double d4 = 0.0d;
                        String str6 = null;
                        ContentValues contentValues = null;
                        boolean z3 = false;
                        boolean z4 = false;
                        while (eventType != 1) {
                            arrayList2 = arrayList2;
                            double d5 = d3;
                            if (eventType != 2) {
                                double d6 = d4;
                                if (eventType == 3) {
                                    String name = xmlPullParserNewPullParser.getName();
                                    if ("cids".equals(name)) {
                                        if (contentValues != null) {
                                            contentValues.put("nomlname", str4);
                                            contentValues.put("nomltype", str5);
                                            writableDatabase.insert("SignalMapNOMLCells", null, contentValues);
                                            contentValues = null;
                                        }
                                        z4 = false;
                                        z2 = z3;
                                        arrayList = arrayList2;
                                        d = d5;
                                    } else {
                                        if ("gpslist".equals(name)) {
                                            if (arrayList2 != null) {
                                                d = d5;
                                                d2 = d6;
                                                arrayList = arrayList2;
                                                arrayList.add(new Point2D(d, d2));
                                            } else {
                                                arrayList = arrayList2;
                                                d = d5;
                                                d2 = d6;
                                            }
                                            d6 = d2;
                                            z2 = false;
                                        } else {
                                            arrayList = arrayList2;
                                            d = d5;
                                            if ("depopulatedzone".equals(name) && arrayList != null) {
                                                ContentValues contentValues2 = new ContentValues();
                                                int size = arrayList.size();
                                                for (int i = 0; i < size; i++) {
                                                    Point2D point2D = (Point2D) arrayList.get(i);
                                                    contentValues2.clear();
                                                    contentValues2.put("nomlname", str4);
                                                    contentValues2.put("lon", Double.valueOf(point2D.getX()));
                                                    contentValues2.put("lat", Double.valueOf(point2D.getY()));
                                                    writableDatabase.insert("SignalMapNOMLGPS", null, contentValues2);
                                                    str3 = str3;
                                                }
                                            }
                                            str3 = str3;
                                            str = null;
                                            z2 = z3;
                                        }
                                        z3 = z2;
                                        d3 = d;
                                        arrayList2 = arrayList;
                                        d4 = d6;
                                        str6 = str;
                                    }
                                    str = null;
                                    z3 = z2;
                                    d3 = d;
                                    arrayList2 = arrayList;
                                    d4 = d6;
                                    str6 = str;
                                } else if (eventType == 4 && str6 != null) {
                                    try {
                                        String strTrim = xmlPullParserNewPullParser.getText().trim();
                                        if (strTrim.isEmpty()) {
                                            d3 = d5;
                                            d4 = d6;
                                        } else {
                                            if (z4 && contentValues != null) {
                                                switch (str6.hashCode()) {
                                                    case 98494:
                                                        if (str6.equals(str2)) {
                                                            contentValues.put(str2, strTrim);
                                                        } else {
                                                            log("unsupported name:" + str6);
                                                        }
                                                        break;
                                                    case 106894:
                                                        if (str6.equals(str3)) {
                                                            contentValues.put(str3, Integer.valueOf(Integer.parseInt(strTrim)));
                                                        } else {
                                                            log("unsupported name:" + str6);
                                                        }
                                                        break;
                                                    case 3053931:
                                                        if (str6.equals("city")) {
                                                            contentValues.put("city", strTrim);
                                                        } else {
                                                            log("unsupported name:" + str6);
                                                        }
                                                        break;
                                                    case 3443869:
                                                        if (str6.equals("plmn")) {
                                                            contentValues.put("plmn", Integer.valueOf(Integer.parseInt(strTrim)));
                                                        } else {
                                                            log("unsupported name:" + str6);
                                                        }
                                                        break;
                                                    default:
                                                        log("unsupported name:" + str6);
                                                        break;
                                                }
                                            } else if (z3) {
                                                int iHashCode = str6.hashCode();
                                                if (iHashCode != 106911) {
                                                    if (iHashCode == 107339 && str6.equals("lon")) {
                                                        d3 = Double.parseDouble(strTrim);
                                                    }
                                                    d4 = d6;
                                                } else if (str6.equals("lat")) {
                                                    d4 = Double.parseDouble(strTrim);
                                                    d3 = d5;
                                                }
                                                log("unsupported name:" + str6);
                                            } else if ("name".equals(str6)) {
                                                str4 = strTrim;
                                            } else if ("type".equals(str6)) {
                                                str5 = strTrim;
                                            }
                                            d3 = d5;
                                            d4 = d6;
                                        }
                                    } catch (Throwable th2) {
                                        th = th2;
                                        writableDatabase.endTransaction();
                                        throw th;
                                    }
                                } else {
                                    d3 = d5;
                                    d4 = d6;
                                }
                            } else {
                                str2 = str2;
                                d4 = d4;
                                d3 = d5;
                                str3 = str3;
                                String name2 = xmlPullParserNewPullParser.getName();
                                if ("depopulatedzone".equals(name2)) {
                                    arrayList2 = new ArrayList();
                                    str4 = "";
                                    str5 = str4;
                                } else if ("cids".equals(name2)) {
                                    contentValues = new ContentValues();
                                    d3 = d3;
                                    arrayList2 = arrayList2;
                                    z4 = true;
                                    str6 = name2;
                                } else if ("gpslist".equals(name2)) {
                                    str6 = name2;
                                    arrayList2 = arrayList2;
                                    d3 = 0.0d;
                                    d4 = 0.0d;
                                    z3 = true;
                                } else {
                                    arrayList2 = arrayList2;
                                }
                                str6 = name2;
                            }
                            eventType = xmlPullParserNewPullParser.next();
                            str2 = str2;
                            str3 = str3;
                        }
                        writableDatabase.setTransactionSuccessful();
                        writableDatabase.endTransaction();
                        PreferenceUtils.getInstance().setIntPreference("nomldb_version", 1);
                        PreferenceUtils.getInstance().setIntPreference("nomlcell_version", 1);
                        if (inputStreamOpen != null) {
                            try {
                                inputStreamOpen.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return true;
                    } catch (Throwable th3) {
                        th = th3;
                    }
                } catch (Exception e2) {
                    e = e2;
                    z = false;
                    inputStream = inputStreamOpen;
                    try {
                        e.printStackTrace();
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        }
                        return z;
                    } catch (Throwable th4) {
                        th = th4;
                        th = th;
                        if (inputStream != null) {
                            throw th;
                        }
                        try {
                            inputStream.close();
                            throw th;
                        } catch (IOException e4) {
                            e4.printStackTrace();
                            throw th;
                        }
                    }
                } catch (Throwable th5) {
                    th = th5;
                    th = th;
                    inputStream = inputStreamOpen;
                    if (inputStream != null) {
                        throw th;
                    }
                    inputStream.close();
                    throw th;
                }
            } catch (Exception e5) {
                e = e5;
                inputStream = inputStreamOpen;
                e.printStackTrace();
                if (inputStream != null) {
                    inputStream.close();
                }
                return z;
            } catch (Throwable th6) {
                th = th6;
                th = th;
                inputStream = inputStreamOpen;
                if (inputStream != null) {
                    throw th;
                }
                inputStream.close();
                throw th;
            }
        } catch (Exception e6) {
            e = e6;
            z = false;
            inputStream = null;
        } catch (Throwable th7) {
            th = th7;
            inputStream = null;
            th = th;
            if (inputStream != null) {
                throw th;
            }
            inputStream.close();
            throw th;
        }
    }

    /* JADX WARN: Code duplicated, block: B:81:0x01c2 A[Catch: all -> 0x00b9, TryCatch #5 {all -> 0x00b9, blocks: (B:25:0x009d, B:27:0x00ac, B:31:0x00bc, B:33:0x00c5, B:34:0x00ca, B:37:0x00d7, B:38:0x00da, B:40:0x00e0, B:41:0x00e3, B:43:0x00e9, B:44:0x00f1, B:58:0x0135, B:46:0x00f5, B:48:0x00fb, B:49:0x0107, B:51:0x010d, B:52:0x0115, B:54:0x011b, B:55:0x0127, B:57:0x012d, B:59:0x0148, B:60:0x015f, B:62:0x016f, B:64:0x0177, B:65:0x017a, B:67:0x0180, B:68:0x018f, B:70:0x0195, B:81:0x01c2, B:75:0x01ac, B:77:0x01b2, B:78:0x01b7, B:80:0x01bd, B:83:0x01d9, B:84:0x01f4, B:86:0x0217, B:88:0x0226, B:90:0x0233, B:91:0x025e, B:94:0x027a), top: B:133:0x009d }] */
    /* JADX WARN: Instruction removed from duplicated block: B:81:0x01c2, please report this as an issue */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v29, types: [java.util.ArrayList, java.util.List] */
    /* JADX WARN: Type inference failed for: r23v0, types: [android.database.sqlite.SQLiteOpenHelper, com.android.phone.CloudCellDbHelper] */
    /* JADX WARN: Type inference failed for: r23v1 */
    /* JADX WARN: Type inference failed for: r23v2, types: [java.io.FileInputStream] */
    /* JADX WARN: Type inference failed for: r23v3 */
    /* JADX WARN: Type inference failed for: r23v4 */
    /* JADX WARN: Type inference failed for: r23v5 */
    /* JADX WARN: Type inference failed for: r24v0, types: [boolean] */
    /* JADX WARN: Type inference failed for: r24v1 */
    /* JADX WARN: Type inference failed for: r24v2, types: [android.util.JsonReader] */
    /* JADX WARN: Type inference failed for: r3v15 */
    /* JADX WARN: Type inference failed for: r3v4 */
    /* JADX WARN: Type inference failed for: r3v5, types: [int] */
    public boolean readAndLoadNOMLSignalMap(boolean z) throws Throwable {
        Throwable th;
        Throwable th2;
        String str;
        String str2;
        String str3;
        String str4;
        ContentValues contentValues;
        String str5;
        String str6 = "cid";
        String str7 = "lac";
        String str8 = "city";
        String str9 = "plmn";
        String str10 = "nomlcell_version";
        Pair sigmapNewestFile = getSigmapNewestFile(this.mDataDownloadDir, "blackcelldb_noml_\\d+\\.json", "nomlcell_version");
        boolean z2 = false;
        if (sigmapNewestFile == null || sigmapNewestFile.second == null) {
            return false;
        }
        SQLiteDatabase writableDatabase = getWritableDatabase();
        int intPreference = PreferenceUtils.getInstance().getIntPreference("nomldb_version");
        if (z == 0 && intPreference >= ((Integer) sigmapNewestFile.first).intValue()) {
            log(" no need to update db");
            return false;
        }
        try {
            try {
                FileInputStream fileInputStream = new FileInputStream((File) sigmapNewestFile.second);
                try {
                    try {
                        JsonReader jsonReader = new JsonReader(new InputStreamReader(fileInputStream));
                        try {
                            try {
                                jsonReader.beginArray();
                                writableDatabase.beginTransaction();
                                try {
                                    writableDatabase.execSQL("DROP TABLE IF EXISTS SignalMapNOMLCells");
                                    writableDatabase.execSQL("CREATE TABLE IF NOT EXISTS SignalMapNOMLCells (_id INTEGER PRIMARY KEY AUTOINCREMENT,nomlname TEXT,nomltype TEXT,city TEXT,plmn INTEGER,lac INTEGER,cid TEXT)");
                                    writableDatabase.execSQL("CREATE INDEX IF NOT EXISTS idx_noml_cid_type ON SignalMapNOMLCells(cid,nomltype);");
                                    writableDatabase.execSQL("DROP TABLE IF EXISTS SignalMapNOMLGPS");
                                    writableDatabase.execSQL("CREATE TABLE IF NOT EXISTS SignalMapNOMLGPS (_id INTEGER PRIMARY KEY AUTOINCREMENT,nomlname TEXT,lon REAL,lat REAL)");
                                    while (jsonReader.hasNext()) {
                                        jsonReader.beginObject();
                                        ?? arrayList = new ArrayList();
                                        ContentValues contentValues2 = new ContentValues();
                                        String strNextString = "";
                                        String strNextString2 = "";
                                        while (true) {
                                            z2 = z2;
                                            fileInputStream = fileInputStream;
                                            jsonReader = jsonReader;
                                            if (jsonReader.hasNext()) {
                                                try {
                                                    String strNextName = jsonReader.nextName();
                                                    str10 = str10;
                                                    if (strNextName.equals("name")) {
                                                        strNextString = jsonReader.nextString();
                                                    } else if (strNextName.equals("type")) {
                                                        strNextString2 = jsonReader.nextString();
                                                    } else {
                                                        if (strNextName.equals("cids")) {
                                                            jsonReader.beginArray();
                                                            while (jsonReader.hasNext()) {
                                                                jsonReader.beginObject();
                                                                while (jsonReader.hasNext()) {
                                                                    String strNextName2 = jsonReader.nextName();
                                                                    switch (strNextName2.hashCode()) {
                                                                        case 98494:
                                                                            if (strNextName2.equals(str6)) {
                                                                                contentValues2.put(str6, jsonReader.nextString());
                                                                            } else {
                                                                                log("unsupported name:" + strNextName2);
                                                                            }
                                                                            break;
                                                                        case 106894:
                                                                            if (strNextName2.equals(str7)) {
                                                                                contentValues2.put(str7, Integer.valueOf(jsonReader.nextInt()));
                                                                            } else {
                                                                                log("unsupported name:" + strNextName2);
                                                                            }
                                                                            break;
                                                                        case 3053931:
                                                                            if (strNextName2.equals(str8)) {
                                                                                contentValues2.put(str8, jsonReader.nextString());
                                                                            } else {
                                                                                log("unsupported name:" + strNextName2);
                                                                            }
                                                                            break;
                                                                        case 3443869:
                                                                            if (strNextName2.equals(str9)) {
                                                                                contentValues2.put(str9, Integer.valueOf(jsonReader.nextInt()));
                                                                            } else {
                                                                                log("unsupported name:" + strNextName2);
                                                                            }
                                                                            break;
                                                                        default:
                                                                            log("unsupported name:" + strNextName2);
                                                                            break;
                                                                    }
                                                                }
                                                                contentValues2.put("nomlname", strNextString);
                                                                contentValues2.put("nomltype", strNextString2);
                                                                writableDatabase.insert("SignalMapNOMLCells", null, contentValues2);
                                                                contentValues2.clear();
                                                                jsonReader.endObject();
                                                            }
                                                            jsonReader.endArray();
                                                        } else {
                                                            if (strNextName.equals("gpslist")) {
                                                                jsonReader.beginArray();
                                                                while (jsonReader.hasNext()) {
                                                                    jsonReader.beginObject();
                                                                    String str11 = str6;
                                                                    String str12 = str7;
                                                                    double dNextDouble = 0.0d;
                                                                    String str13 = str8;
                                                                    String str14 = str9;
                                                                    double dNextDouble2 = 0.0d;
                                                                    while (jsonReader.hasNext()) {
                                                                        String strNextName3 = jsonReader.nextName();
                                                                        ContentValues contentValues3 = contentValues2;
                                                                        int iHashCode = strNextName3.hashCode();
                                                                        String str15 = strNextString2;
                                                                        if (iHashCode != 106911) {
                                                                            if (iHashCode == 107339 && strNextName3.equals("lon")) {
                                                                                dNextDouble = jsonReader.nextDouble();
                                                                            } else {
                                                                                log("unsupported name:" + strNextName3);
                                                                            }
                                                                        } else if (strNextName3.equals("lat")) {
                                                                            dNextDouble2 = jsonReader.nextDouble();
                                                                        } else {
                                                                            log("unsupported name:" + strNextName3);
                                                                        }
                                                                        contentValues2 = contentValues3;
                                                                        strNextString2 = str15;
                                                                    }
                                                                    arrayList.add(new Point2D(dNextDouble, dNextDouble2));
                                                                    jsonReader.endObject();
                                                                    str6 = str11;
                                                                    str8 = str13;
                                                                    str9 = str14;
                                                                    str7 = str12;
                                                                    contentValues2 = contentValues2;
                                                                    strNextString2 = strNextString2;
                                                                }
                                                                str = str6;
                                                                str2 = str7;
                                                                str3 = str8;
                                                                str4 = str9;
                                                                contentValues = contentValues2;
                                                                str5 = strNextString2;
                                                                jsonReader.endArray();
                                                            }
                                                            str6 = str;
                                                            str8 = str3;
                                                            str9 = str4;
                                                            str7 = str2;
                                                            contentValues2 = contentValues;
                                                            strNextString2 = str5;
                                                        }
                                                        str = str6;
                                                        str2 = str7;
                                                        str3 = str8;
                                                        str4 = str9;
                                                        contentValues = contentValues2;
                                                        str5 = strNextString2;
                                                        str6 = str;
                                                        str8 = str3;
                                                        str9 = str4;
                                                        str7 = str2;
                                                        contentValues2 = contentValues;
                                                        strNextString2 = str5;
                                                    }
                                                } catch (Throwable th3) {
                                                    th = th3;
                                                    writableDatabase.endTransaction();
                                                    throw th;
                                                }
                                            } else {
                                                String str16 = str6;
                                                String str17 = str7;
                                                String str18 = str8;
                                                String str19 = str9;
                                                String str20 = str10;
                                                if (!arrayList.isEmpty()) {
                                                    ContentValues contentValues4 = new ContentValues();
                                                    int size = arrayList.size();
                                                    for (?? r3 = z2; r3 < size; r3++) {
                                                        Point2D point2D = (Point2D) arrayList.get(r3);
                                                        contentValues4.clear();
                                                        contentValues4.put("nomlname", strNextString);
                                                        contentValues4.put("lon", Double.valueOf(point2D.getX()));
                                                        contentValues4.put("lat", Double.valueOf(point2D.getY()));
                                                        writableDatabase.insert("SignalMapNOMLGPS", null, contentValues4);
                                                    }
                                                }
                                                jsonReader.endObject();
                                                fileInputStream = fileInputStream;
                                                jsonReader = jsonReader;
                                                str6 = str16;
                                                z2 = z2;
                                                str10 = str20;
                                                str8 = str18;
                                                str9 = str19;
                                                str7 = str17;
                                            }
                                        }
                                    }
                                    String str21 = str10;
                                    FileInputStream fileInputStream2 = fileInputStream;
                                    JsonReader jsonReader2 = jsonReader;
                                    writableDatabase.setTransactionSuccessful();
                                    writableDatabase.endTransaction();
                                    jsonReader2.endArray();
                                    jsonReader2.close();
                                    fileInputStream2.close();
                                    PreferenceUtils.getInstance().setIntPreference("nomldb_version", ((Integer) sigmapNewestFile.first).intValue());
                                    PreferenceUtils.getInstance().setIntPreference(str21, ((Integer) sigmapNewestFile.first).intValue());
                                    return true;
                                } catch (Throwable th4) {
                                    th = th4;
                                }
                            } catch (Throwable th5) {
                                th = th5;
                                th2 = th;
                                try {
                                    z.close();
                                    throw th2;
                                } catch (Throwable th6) {
                                    th2.addSuppressed(th6);
                                    throw th2;
                                }
                            }
                        } catch (Throwable th7) {
                            th = th7;
                            this = fileInputStream;
                            z = jsonReader;
                            th2 = th;
                            z.close();
                            throw th2;
                        }
                    } catch (Throwable th8) {
                        th = th8;
                        this = fileInputStream;
                        th = th;
                        try {
                            this.close();
                            throw th;
                        } catch (Throwable th9) {
                            th.addSuppressed(th9);
                            throw th;
                        }
                    }
                } catch (Throwable th10) {
                    th = th10;
                    th = th;
                    this.close();
                    throw th;
                }
            } catch (Exception e) {
                e = e;
                z2 = false;
                e.printStackTrace();
                return z2;
            }
        } catch (Exception e2) {
            e = e2;
            e.printStackTrace();
            return z2;
        }
    }

    public Cursor getCommonCellBlacklist() {
        try {
            return getReadableDatabase().query("ComDataBlacklist", new String[]{"gci", "act", "launch_time", "done_time"}, null, null, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void clearCommonCellBlacklist() {
        getWritableDatabase().delete("ComDataBlacklist", null, null);
    }

    public void updateCommonCellBlacklist(String str, int i, long j, long j2) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("gci", str);
        contentValues.put("act", Integer.valueOf(i));
        contentValues.put("launch_time", Long.valueOf(j));
        contentValues.put("done_time", Long.valueOf(j2));
        writableDatabase.insert("ComDataBlacklist", null, contentValues);
    }

    public Cursor getSignalMapTableData(String str) {
        if (!checkSignalMapDataValidity()) {
            return null;
        }
        SQLiteDatabase readableDatabase = getReadableDatabase();
        try {
            if (str == null) {
                return readableDatabase.query("SignalMapCells", SIGNALMAP_COLUMNS, null, null, null, null, null);
            }
            return readableDatabase.query("SignalMapCells", SIGNALMAP_COLUMNS, "city= ?", new String[]{str}, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getSignalMapCellCount() {
        Cursor signalMapTableData = getSignalMapTableData(null);
        if (signalMapTableData == null) {
            return 0;
        }
        int count = signalMapTableData.getCount();
        log("sqlCount:" + count);
        signalMapTableData.close();
        return count;
    }

    public Cursor getNOMLNameByCid(String str) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        try {
            if (str == null) {
                return readableDatabase.query("SignalMapNOMLCells", SIGNALMAP_NOML_COLUMNS, null, null, null, null, null);
            }
            return readableDatabase.query("SignalMapNOMLCells", SIGNALMAP_NOML_COLUMNS, "cid= ?", new String[]{str}, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Cursor getNOMLCidByType(String str) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        try {
            if (str == null) {
                return readableDatabase.query("SignalMapNOMLCells", SIGNALMAP_NOML_COLUMNS, null, null, null, null, null);
            }
            return readableDatabase.query("SignalMapNOMLCells", SIGNALMAP_NOML_COLUMNS, "nomltype= ?", new String[]{str}, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX WARN: Code duplicated, block: B:28:0x0064  */
    /* JADX WARN: Code duplicated, block: B:31:0x0068  */
    /* JADX WARN: Code duplicated, block: B:39:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:40:? A[SYNTHETIC] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0 */
    /* JADX WARN: Type inference failed for: r1v1, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r1v2 */
    public List getGPSDataByZone(String str) throws Throwable {
        Throwable th;
        Exception exc;
        Cursor cursorQuery;
        ?? r1 = 0;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        try {
            try {
                cursorQuery = getReadableDatabase().query("SignalMapNOMLGPS", new String[]{"lon", "lat"}, "nomlname= ?", new String[]{str}, null, null, null);
                if (cursorQuery != null) {
                    while (cursorQuery.moveToNext()) {
                        try {
                            arrayList.add(new Point2D(cursorQuery.getDouble(0), cursorQuery.getDouble(1)));
                        } catch (Exception e) {
                            exc = e;
                            exc.printStackTrace();
                            if (cursorQuery != null) {
                            }
                            if (arrayList.isEmpty()) {
                                return null;
                            }
                            return arrayList;
                        }
                    }
                }
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
            } catch (Throwable th2) {
                th = th2;
                r1 = this;
                if (r1 != 0) {
                    r1.close();
                    throw th;
                }
                throw th;
            }
        } catch (Exception e2) {
            exc = e2;
            cursorQuery = null;
        } catch (Throwable th3) {
            th = th3;
            if (r1 != 0) {
                r1.close();
                throw th;
            }
            throw th;
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return arrayList;
    }

    public boolean isNOMLCidExistsByType(String str, String str2) {
        boolean z = false;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return false;
        }
        Cursor cursorRawQuery = null;
        try {
            try {
                cursorRawQuery = getReadableDatabase().rawQuery("SELECT 1 FROM SignalMapNOMLCells WHERE cid=? AND nomltype=? LIMIT 1", new String[]{str, str2});
                if (cursorRawQuery != null && cursorRawQuery.moveToFirst()) {
                    z = true;
                }
                return z;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } finally {
            if (cursorRawQuery != null) {
                cursorRawQuery.close();
            }
        }
    }

    public boolean checkSignalMapDataValidity() {
        long jCurrentTimeMillis = System.currentTimeMillis() / 1000;
        long longPref = PreferenceUtils.getInstance().getLongPref("signalmap_default_timeout");
        if (jCurrentTimeMillis <= longPref) {
            return true;
        }
        log("SignalMap Data has been invalidated. currentTime = " + jCurrentTimeMillis + " defaultTimeout = " + longPref);
        return false;
    }

    public Cursor getCellByRailtripAndScore(String str, int i) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        if (str != null) {
            try {
                if (!str.isEmpty()) {
                    String[] strArrSplit = str.split(",");
                    if (strArrSplit.length == 1) {
                        return readableDatabase.query("SignalMapHstCells", SIGNALMAP_HST_COLUMNS, "railtrip=? AND score=?", new String[]{strArrSplit[0].trim(), String.valueOf(i)}, null, null, null);
                    }
                    StringBuilder sb = new StringBuilder();
                    String[] strArr = new String[strArrSplit.length + 1];
                    for (int i2 = 0; i2 < strArrSplit.length; i2++) {
                        if (i2 > 0) {
                            sb.append(",");
                        }
                        sb.append("?");
                        strArr[i2] = strArrSplit[i2].trim();
                    }
                    strArr[strArrSplit.length] = String.valueOf(i);
                    return readableDatabase.query("SignalMapHstCells", SIGNALMAP_HST_COLUMNS, "railtrip IN (" + ((Object) sb) + ") AND score=?", strArr, null, null, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("DatabaseHelper", "Error querying cell data: " + e.getMessage());
                return null;
            }
        }
        return readableDatabase.query("SignalMapHstCells", SIGNALMAP_HST_COLUMNS, null, null, null, null, null);
    }

    public Cursor getHstCellByScore(int i) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        try {
            if (i == -1) {
                return readableDatabase.query("SignalMapHstCells", SIGNALMAP_HST_COLUMNS, null, null, null, null, null);
            }
            return readableDatabase.query("SignalMapHstCells", SIGNALMAP_HST_COLUMNS, "score=?", new String[]{String.valueOf(i)}, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Cursor getHstTunnelCellByRailtripAndDirection(int i, String str, int i2) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        try {
            if (i == -1) {
                return readableDatabase.query("SignalMapHstCells", SIGNALMAP_HST_COLUMNS, null, null, null, null, null);
            }
            return readableDatabase.query("SignalMapHstCells", SIGNALMAP_HST_COLUMNS, "score=? AND railtrip=? AND sequences=?", new String[]{String.valueOf(i), str, String.valueOf(i2)}, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getSignalMapHSTCellCount() {
        Cursor cellByRailtripAndScore = getCellByRailtripAndScore(null, 0);
        if (cellByRailtripAndScore == null) {
            return 0;
        }
        int count = cellByRailtripAndScore.getCount();
        log("HST sqlCount:" + count);
        cellByRailtripAndScore.close();
        return count;
    }

    public Cursor getPreferCellTableDataByRailtrip(String str) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor cursorQuery = null;
        try {
            if (str != null) {
                cursorQuery = readableDatabase.query("SignalMapHstPreferCells", SIGNALMAP_HST_PREFER_CELL_COLUMNS, "railtrip=?", new String[]{str}, null, null, null);
            } else {
                cursorQuery = readableDatabase.query("SignalMapHstPreferCells", SIGNALMAP_HST_PREFER_CELL_COLUMNS, null, null, null, null, null);
                log("railtrip is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("CloudCellDbHelper", "getPreferCellTableDataByRailtrip");
        return cursorQuery;
    }

    public Cursor getSignalMapSubwayTableData(String str) {
        Cursor cursorQuery;
        SQLiteDatabase readableDatabase = getReadableDatabase();
        try {
            if (str == null) {
                cursorQuery = readableDatabase.query("SignalMapSubwayCells", SIGNALMAP_SUBWAY_COLUMNS, null, null, null, null, null);
            } else {
                cursorQuery = readableDatabase.query("SignalMapSubwayCells", SIGNALMAP_SUBWAY_COLUMNS, str.concat("=?"), new String[]{str}, null, null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            cursorQuery = null;
        }
        Log.d("CloudCellDbHelper", "getSignalMapSubwayTableData");
        return cursorQuery;
    }

    public Cursor queryOtaCellV3() {
        try {
            return getReadableDatabase().query("CloudPush", CLOUD_PUSH_COLUMNS, null, null, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Cursor getRecordByPlmnTacAction(String str, int i, int i2) {
        try {
            return getReadableDatabase().query("CloudPush", CLOUD_PUSH_COLUMNS, "plmn=? AND tac=? AND action=?", new String[]{str, String.valueOf(i), String.valueOf(i2)}, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List getManualSubCellInfoByGci1(String str) {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQuery = null;
        try {
            try {
                cursorQuery = getReadableDatabase().query("cellmap_manual", new String[]{"_id", "gci1", "freq1", "pci1", "rsrp1", "sinr1", "rat1", "service1", "action1", "gci2", "rsrp2", "sinr2", "freq2", "rat2", "pci2", "service2", "action2", "type", "time_extra", "update_time"}, "gci1 = ?", new String[]{str}, null, null, null);
                if (cursorQuery != null && cursorQuery.moveToFirst()) {
                    do {
                        ManualSubCellInfo manualSubCellInfo = new ManualSubCellInfo();
                        manualSubCellInfo.setId(cursorQuery.getInt(0));
                        manualSubCellInfo.setGci1(cursorQuery.getString(1));
                        manualSubCellInfo.setFreq1(cursorQuery.getInt(2));
                        manualSubCellInfo.setPci1(cursorQuery.getInt(3));
                        manualSubCellInfo.setRsrp1(cursorQuery.getInt(4));
                        manualSubCellInfo.setSinr1(cursorQuery.getInt(5));
                        manualSubCellInfo.setRat1(cursorQuery.getInt(6));
                        manualSubCellInfo.setService1(cursorQuery.getInt(7));
                        manualSubCellInfo.setAction1(cursorQuery.getInt(8));
                        manualSubCellInfo.setGci2(cursorQuery.getString(9));
                        manualSubCellInfo.setRsrp2(cursorQuery.getInt(10));
                        manualSubCellInfo.setSinr2(cursorQuery.getInt(11));
                        manualSubCellInfo.setFreq2(cursorQuery.getInt(12));
                        manualSubCellInfo.setRat2(cursorQuery.getInt(13));
                        manualSubCellInfo.setPci2(cursorQuery.getInt(14));
                        manualSubCellInfo.setService2(cursorQuery.getInt(15));
                        manualSubCellInfo.setAction2(cursorQuery.getInt(16));
                        manualSubCellInfo.setType(cursorQuery.getInt(17));
                        manualSubCellInfo.setTime_extra(cursorQuery.getInt(18));
                        manualSubCellInfo.setUpdate_time(cursorQuery.getLong(19));
                        arrayList.add(manualSubCellInfo);
                    } while (cursorQuery.moveToNext());
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

    public List getManualSubCellInfoByGci2(String str) {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQuery = null;
        try {
            try {
                cursorQuery = getReadableDatabase().query("cellmap_manual", new String[]{"_id", "gci1", "freq1", "pci1", "rsrp1", "sinr1", "rat1", "service1", "action1", "gci2", "rsrp2", "sinr2", "freq2", "rat2", "pci2", "service2", "action2", "type", "time_extra", "update_time"}, "gci2 = ?", new String[]{str}, null, null, null);
                if (cursorQuery != null && cursorQuery.moveToFirst()) {
                    do {
                        ManualSubCellInfo manualSubCellInfo = new ManualSubCellInfo();
                        manualSubCellInfo.setId(cursorQuery.getInt(0));
                        manualSubCellInfo.setGci1(cursorQuery.getString(1));
                        manualSubCellInfo.setFreq1(cursorQuery.getInt(2));
                        manualSubCellInfo.setPci1(cursorQuery.getInt(3));
                        manualSubCellInfo.setRsrp1(cursorQuery.getInt(4));
                        manualSubCellInfo.setSinr1(cursorQuery.getInt(5));
                        manualSubCellInfo.setRat1(cursorQuery.getInt(6));
                        manualSubCellInfo.setService1(cursorQuery.getInt(7));
                        manualSubCellInfo.setAction1(cursorQuery.getInt(8));
                        manualSubCellInfo.setGci2(cursorQuery.getString(9));
                        manualSubCellInfo.setRsrp2(cursorQuery.getInt(10));
                        manualSubCellInfo.setSinr2(cursorQuery.getInt(11));
                        manualSubCellInfo.setFreq2(cursorQuery.getInt(12));
                        manualSubCellInfo.setRat2(cursorQuery.getInt(13));
                        manualSubCellInfo.setPci2(cursorQuery.getInt(14));
                        manualSubCellInfo.setService2(cursorQuery.getInt(15));
                        manualSubCellInfo.setAction2(cursorQuery.getInt(16));
                        manualSubCellInfo.setType(cursorQuery.getInt(17));
                        manualSubCellInfo.setTime_extra(cursorQuery.getInt(18));
                        manualSubCellInfo.setUpdate_time(cursorQuery.getLong(19));
                        arrayList.add(manualSubCellInfo);
                    } while (cursorQuery.moveToNext());
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

    public boolean isCityFoundInCellMap(String str) {
        boolean z = false;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Cursor cursorQuery = null;
        try {
            try {
                cursorQuery = getReadableDatabase().query("cellmap", new String[]{"_id", "scene", "sub_scene", "gci", "tac", "freq", "pci", "rat", "prev_gci", "neigh_gci", "service", "reason", "abnormal_period", "update_time", "prj_type"}, "city=? OR city=?", new String[]{str, "NA"}, null, null, null);
                if (cursorQuery != null && cursorQuery.getCount() > 0) {
                    z = true;
                }
                return z;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } finally {
            if (cursorQuery != null) {
                cursorQuery.close();
            }
        }
    }

    public List getSubCellInfoByGci(String str) {
        log("getSubCellInfoByGci gci=" + str);
        ArrayList arrayList = new ArrayList();
        Cursor cursorQuery = null;
        try {
            try {
                cursorQuery = getReadableDatabase().query("cellmap", new String[]{"_id", "scene", "city", "sub_scene", "gci", "tac", "freq", "pci", "rat", "prev_gci", "neigh_gci", "service", "reason", "abnormal_period", "update_time", "prj_type"}, "gci = ?", new String[]{str}, null, null, null);
                if (cursorQuery != null && cursorQuery.moveToFirst()) {
                    do {
                        SubCellInfo subCellInfo = new SubCellInfo();
                        subCellInfo.setId(cursorQuery.getInt(0));
                        subCellInfo.setScene(cursorQuery.getString(1));
                        subCellInfo.setSubScene(cursorQuery.getString(2));
                        subCellInfo.setCityName(cursorQuery.getString(3));
                        subCellInfo.setGci(cursorQuery.getString(4));
                        subCellInfo.setTac(cursorQuery.getString(5));
                        subCellInfo.setFreq(cursorQuery.getInt(6));
                        subCellInfo.setPci(cursorQuery.getInt(7));
                        subCellInfo.setRat(cursorQuery.getInt(8));
                        subCellInfo.setPrevGci(cursorQuery.getString(9));
                        subCellInfo.setNeighGci(cursorQuery.getString(10));
                        subCellInfo.setService(cursorQuery.getString(11));
                        subCellInfo.setReason(cursorQuery.getString(12));
                        subCellInfo.setAbnormalPeriod(cursorQuery.getString(13));
                        subCellInfo.setUpdateTime(cursorQuery.getString(14));
                        subCellInfo.setProjectType(cursorQuery.getString(15));
                        arrayList.add(subCellInfo);
                    } while (cursorQuery.moveToNext());
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

    public Cursor getRecordsById(int i) {
        try {
            return getReadableDatabase().query("CloudPush", CLOUD_PUSH_COLUMNS, "_id=?", new String[]{String.valueOf(i)}, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getOtaZipName(int i, int i2) {
        return ((String) sTypePrefixMap.getOrDefault(Integer.valueOf(i), "err")) + i2 + ".zip";
    }

    public static String getFileRegEx(int i) {
        return ((String) sTypePrefixMap.getOrDefault(Integer.valueOf(i), "clouderr")) + "\\d+\\.json";
    }
}
