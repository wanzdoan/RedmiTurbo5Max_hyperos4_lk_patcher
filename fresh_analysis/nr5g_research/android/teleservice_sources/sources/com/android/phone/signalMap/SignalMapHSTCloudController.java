package com.android.phone.signalMap;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import com.android.phone.CloudCellDbHelper;
import com.android.phone.FiveGCloudUtils;
import com.android.phone.PhoneGlobals;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.performance.PreferenceUtils;
import com.ot.pubsub.a.a;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/* JADX INFO: loaded from: classes.dex */
public class SignalMapHSTCloudController {
    public static boolean DEBUG = false;
    private static Context mContext;
    private static SignalMapHSTCloudController sInstance;
    private BroadcastReceiver mDLReceiver;
    private CloudCellDbHelper mDbHelper;
    private int mDownloadRetryCnt;
    private SignalMapHSTCloudHandler mHandler;
    private HandlerThread mWorkThread;
    private boolean mFddToTddEnabled = false;
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.signalMap.SignalMapHSTCloudController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && "com.android.phone.SignalMapHSTCloudController.debug".equals(action)) {
                SignalMapHSTCloudController.this.mHandler.obtainMessage(103, intent).sendToTarget();
            }
        }
    };

    public static synchronized SignalMapHSTCloudController getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new SignalMapHSTCloudController();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    public SignalMapHSTCloudController() {
        this.mHandler = null;
        this.mWorkThread = null;
        mContext = PhoneGlobals.getInstance();
        HandlerThread handlerThread = new HandlerThread("SignalMapHSTCloudController", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        SignalMapHSTCloudHandler signalMapHSTCloudHandler = new SignalMapHSTCloudHandler(this.mWorkThread.getLooper());
        this.mHandler = signalMapHSTCloudHandler;
        signalMapHSTCloudHandler.obtainMessage(100).sendToTarget();
        EventHubManager.getInstance().registerListener("telephony", "cloud_update", new BaseEvent.IEventListener() { // from class: com.android.phone.signalMap.SignalMapHSTCloudController$$ExternalSyntheticLambda0
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public final void onEvent(BaseEvent baseEvent) {
                this.f$0.lambda$new$0(baseEvent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(BaseEvent baseEvent) {
        if (baseEvent instanceof TelephonyEvent.CloudEvent) {
            SignalMapHSTCloudHandler signalMapHSTCloudHandler = this.mHandler;
            if (signalMapHSTCloudHandler == null) {
                log("handler is null");
            } else {
                signalMapHSTCloudHandler.obtainMessage(102).sendToTarget();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void init() {
        registerReceiver();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.phone.SignalMapHSTCloudController.debug");
        mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    class SignalMapHSTCloudHandler extends Handler {
        SignalMapHSTCloudHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) throws Throwable {
            switch (message.what) {
                case 100:
                    SignalMapHSTCloudController.this.init();
                    SignalMapHSTCloudController.this.mDbHelper = CloudCellDbHelper.getInstance(SignalMapHSTCloudController.mContext);
                    try {
                        int signalMapHSTCellCount = SignalMapHSTCloudController.this.mDbHelper.getSignalMapHSTCellCount();
                        int i = Settings.System.getInt(SignalMapHSTCloudController.mContext.getContentResolver(), "clear_signalmap_db", 0);
                        SignalMapHSTCloudController.this.logD("sqlCount = " + signalMapHSTCellCount + "dbClearFlag = " + i);
                        if (signalMapHSTCellCount == 0 && i == 0) {
                            SignalMapHSTCloudController.this.mDbHelper.readAndLoadDefaultHSTSignalMap();
                        } else {
                            SignalMapHSTCloudController.this.mDbHelper.readAndLoadSignalMapHst(false);
                        }
                        SignalMapHSTCloudController.this.mDbHelper.readAndLoadHSTPreferCellSignalMap();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                    break;
                case 101:
                    SignalMapHSTCloudController.this.log("EVENT_CLOUD_DOWNLOADED");
                    try {
                        if (SignalMapHSTCloudController.this.checkDownloadStatus(message)) {
                            SignalMapHSTCloudController.this.mDbHelper.readAndLoadSignalMapHst(true);
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        return;
                    }
                    break;
                case 102:
                    SignalMapHSTCloudController.this.updateSignalMapHstCloudStatus();
                    break;
                case 103:
                    Intent intent = (Intent) message.obj;
                    String stringExtra = intent.getStringExtra("url");
                    int intExtra = intent.getIntExtra("version", 0);
                    String stringExtra2 = intent.getStringExtra("railtrip");
                    int intExtra2 = intent.getIntExtra("id", -1);
                    String stringExtra3 = intent.getStringExtra("cid");
                    SignalMapHSTCloudController.this.mFddToTddEnabled = intent.getBooleanExtra("fdd2tdd", false);
                    if (intExtra2 != -1) {
                        SignalMapHSTController.getInstance().tryMatchRoute(Long.valueOf(stringExtra3).longValue());
                        SignalMapHSTController.getInstance().processFdd2TddOptimization(Long.parseLong(stringExtra3));
                        SignalMapHSTController.getInstance().processPreloadOptimization(Long.parseLong(stringExtra3));
                        SignalMapHSTController.getInstance().processDnsPrefetchOptimization(Long.parseLong(stringExtra3));
                        SignalMapHSTController.getInstance().processSmartlinkOptimization(Long.parseLong(stringExtra3));
                    } else {
                        SignalMapHSTCloudController.this.downloadCloudFile(stringExtra, intExtra, true);
                        try {
                            if (SignalMapHSTCloudController.this.mDbHelper.readAndLoadSignalMapHst(true)) {
                                Set hSTPreloadCellFromDb = SignalMapHSTCloudController.this.getHSTPreloadCellFromDb();
                                SignalMapHSTCloudController.this.logD("EVENT_TEST: preList = " + hSTPreloadCellFromDb);
                                HashMap hSTDubiousCellFromDb = SignalMapHSTCloudController.this.getHSTDubiousCellFromDb(stringExtra2);
                                SignalMapHSTCloudController.this.logD("EVENT_TEST: dubiousCell = " + hSTDubiousCellFromDb);
                                Set hSTFDD2TDDCellFromDb = SignalMapHSTCloudController.this.getHSTFDD2TDDCellFromDb(stringExtra2);
                                SignalMapHSTCloudController.this.logD("EVENT_TEST: fdd2TddList = " + hSTFDD2TDDCellFromDb);
                            }
                        } catch (Exception e3) {
                            e3.printStackTrace();
                            return;
                        }
                    }
                    break;
            }
        }
    }

    public synchronized void updateSignalMapHstCloudStatus() {
        try {
            try {
                List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(mContext.getContentResolver(), "SignalMapHstCellBlackList");
                if (cloudDataList == null || cloudDataList.size() == 0) {
                    log("CloudCell cloudList is empty");
                } else {
                    for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                        if (cloudData != null) {
                            String string = cloudData.getString("Model", "NA");
                            String string2 = cloudData.getString("SimOperator", "NA");
                            if (FiveGCloudUtils.modelMatched(string) && FiveGCloudUtils.simOperatorMatched(string2)) {
                                String string3 = cloudData.getString("cell_url", "");
                                int i = cloudData.getInt("cell_ver", 0);
                                String string4 = cloudData.getString("ForceUpdate", "NA");
                                if (!TextUtils.isEmpty(string3)) {
                                    if (string4.equals("1")) {
                                        log("force downloadCloudFile!");
                                        downloadCloudFile(string3, i, true);
                                    } else {
                                        downloadCloudFile(string3, i, false);
                                    }
                                }
                            }
                            log("updateSignalMapHstCloudStatus data=" + cloudData);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private int getCursorInt(Cursor cursor, String str) {
        return cursor.getInt(cursor.getColumnIndex(str));
    }

    private String getCursorString(Cursor cursor, String str) {
        return cursor.getString(cursor.getColumnIndex(str));
    }

    private String getSignalMapHstZipName(int i) {
        return "blackcelldb_hst_" + i + ".zip";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void downloadCloudFile(String str, int i, boolean z) {
        long intPreference = PreferenceUtils.getInstance().getIntPreference("hstcell_version");
        if (!z && i <= intPreference) {
            log("invalid cloud version for cell data");
            return;
        }
        try {
            DownloadManager downloadManager = (DownloadManager) mContext.getSystemService("download");
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
            request.setAllowedNetworkTypes(2);
            request.setDestinationInExternalFilesDir(mContext, "CloudCell", getSignalMapHstZipName(i));
            request.setNotificationVisibility(2);
            request.setVisibleInDownloadsUi(false);
            request.setAllowedOverMetered(true);
            request.setBypassRecommendedSizeLimit(true);
            long jEnqueue = downloadManager.enqueue(request);
            log("download cloud file, id = " + jEnqueue);
            registerDownloadCompleteReceiver(i);
            downloadManager.forceDownload(new long[]{jEnqueue});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerDownloadCompleteReceiver(final int i) {
        if (this.mDLReceiver == null) {
            IntentFilter intentFilter = new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE");
            BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.signalMap.SignalMapHSTCloudController.2
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    long longExtra = intent.getLongExtra("extra_download_id", -1L);
                    SignalMapHSTCloudController.this.log(intent.getAction() + " id:" + longExtra);
                    SignalMapHSTCloudController.this.mHandler.obtainMessage(101, 0, i, Long.valueOf(longExtra)).sendToTarget();
                }
            };
            this.mDLReceiver = broadcastReceiver;
            mContext.registerReceiver(broadcastReceiver, intentFilter, 2);
        }
    }

    private void unregisterDownloadCompleteReceiver() {
        BroadcastReceiver broadcastReceiver = this.mDLReceiver;
        if (broadcastReceiver != null) {
            mContext.unregisterReceiver(broadcastReceiver);
            this.mDLReceiver = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkDownloadStatus(Message message) {
        int i;
        File file;
        File parentFile;
        File[] fileArrListFiles;
        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService("download");
        DownloadManager.Query query = new DownloadManager.Query();
        long jLongValue = ((Long) message.obj).longValue();
        boolean z = true;
        boolean z2 = false;
        try {
            Cursor cursorQuery = downloadManager.query(query.setFilterById(jLongValue));
            try {
                if (cursorQuery == null) {
                    log("null download cursor");
                    if (cursorQuery == null) {
                        return false;
                    }
                    cursorQuery.close();
                    return false;
                }
                int i2 = cursorQuery.moveToFirst() ? cursorQuery.getInt(cursorQuery.getColumnIndex(a.J)) : 1;
                log("checkDownloadStatus status:  " + i2 + ", version: " + message.arg2);
                if (i2 == 8) {
                    this.mDownloadRetryCnt = 0;
                    int i3 = message.arg2;
                    String string = cursorQuery.getString(cursorQuery.getColumnIndex("local_uri"));
                    if (!TextUtils.isEmpty(string) && string.startsWith("file://")) {
                        file = new File(Uri.parse(string).getPath());
                    } else {
                        file = new File(mContext.getExternalFilesDir("CloudCell") + File.separator + getSignalMapHstZipName(i3));
                    }
                    if (extractCellZip(file, CloudCellDbHelper.getCellDbDir(mContext)) && (parentFile = file.getParentFile()) != null && (fileArrListFiles = parentFile.listFiles()) != null) {
                        for (File file2 : fileArrListFiles) {
                            log(file2 + " deleted:" + file2.delete());
                        }
                    }
                    unregisterDownloadCompleteReceiver();
                } else {
                    if (i2 == 16 && (i = this.mDownloadRetryCnt) < 3) {
                        this.mDownloadRetryCnt = i + 1;
                        log("retry to download retry:" + this.mDownloadRetryCnt);
                        downloadManager.forceDownload(new long[]{jLongValue});
                    } else if (i2 == 16 && this.mDownloadRetryCnt >= 3) {
                        log("download failed too many times");
                        unregisterDownloadCompleteReceiver();
                    }
                    z = false;
                }
                try {
                    cursorQuery.close();
                    return z;
                } catch (Exception e) {
                    e = e;
                    z2 = z;
                    log("check cell file download status error." + e);
                    e.printStackTrace();
                    return z2;
                }
            } catch (Throwable th) {
                if (cursorQuery != null) {
                    try {
                        cursorQuery.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    /* JADX WARN: Code duplicated, block: B:71:0x00be A[EXC_TOP_SPLITTER, SYNTHETIC] */
    private boolean extractCellZip(File file, String str) {
        File[] fileArrListFiles;
        try {
            ZipFile zipFile = new ZipFile(file);
            try {
                Enumeration<? extends ZipEntry> enumerationEntries = zipFile.entries();
                if (enumerationEntries.hasMoreElements()) {
                    ZipEntry zipEntryNextElement = enumerationEntries.nextElement();
                    if (zipEntryNextElement.isDirectory()) {
                        log("invalid zip file");
                    } else {
                        File file2 = new File(str + zipEntryNextElement.getName());
                        if (!file2.getParentFile().exists()) {
                            file2.getParentFile().mkdirs();
                        } else {
                            File parentFile = file2.getParentFile();
                            if (parentFile != null && (fileArrListFiles = parentFile.listFiles()) != null) {
                                for (File file3 : fileArrListFiles) {
                                    String name = file3.getName();
                                    if (name != null && name.matches("blackcelldb_hst_\\d+\\.json")) {
                                        log(file3 + " deleted:" + file3.delete());
                                    }
                                }
                            }
                        }
                        try {
                            InputStream inputStream = zipFile.getInputStream(zipEntryNextElement);
                            try {
                                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                                try {
                                    byte[] bArr = new byte[8192];
                                    while (true) {
                                        int i = inputStream.read(bArr);
                                        if (i == -1) {
                                            break;
                                        }
                                        fileOutputStream.write(bArr, 0, i);
                                        if (inputStream != null) {
                                            try {
                                                inputStream.close();
                                            } catch (Throwable th) {
                                                th.addSuppressed(th);
                                            }
                                        }
                                        throw th;
                                    }
                                    fileOutputStream.close();
                                    inputStream.close();
                                    log("Done extracting " + file + " to: " + str);
                                    zipFile.close();
                                    return true;
                                } catch (Throwable th2) {
                                    try {
                                        fileOutputStream.close();
                                    } catch (Throwable th3) {
                                        th2.addSuppressed(th3);
                                    }
                                    throw th2;
                                }
                            } catch (Throwable th4) {
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                                throw th4;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    e.printStackTrace();
                    return false;
                }
                zipFile.close();
            } catch (Throwable th5) {
                try {
                    zipFile.close();
                } catch (Throwable th6) {
                    th5.addSuppressed(th6);
                }
                throw th5;
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return false;
    }

    public Set getHSTPreloadCellFromDb() {
        HashSet hashSet = new HashSet();
        Cursor hstCellByScore = null;
        try {
            try {
                hstCellByScore = this.mDbHelper.getHstCellByScore(2);
                if (hstCellByScore != null) {
                    while (hstCellByScore.moveToNext()) {
                        hashSet.add(getCursorString(hstCellByScore, "cid"));
                    }
                }
                if (hstCellByScore != null && !hstCellByScore.isClosed()) {
                    hstCellByScore.close();
                }
            } catch (Exception e) {
                log("Error retrieving data from database: " + e.getMessage());
                if (hstCellByScore != null && !hstCellByScore.isClosed()) {
                }
            }
            logD("cidlist: " + hashSet);
            return hashSet;
        } catch (Throwable th) {
            if (hstCellByScore != null && !hstCellByScore.isClosed()) {
                hstCellByScore.close();
            }
            throw th;
        }
    }

    public Set getHSTTunnelCellFromDbByLineAndDirection(String str, int i) {
        HashSet hashSet = new HashSet();
        Cursor hstTunnelCellByRailtripAndDirection = null;
        try {
            try {
                hstTunnelCellByRailtripAndDirection = this.mDbHelper.getHstTunnelCellByRailtripAndDirection(2, str, i);
                if (hstTunnelCellByRailtripAndDirection != null) {
                    while (hstTunnelCellByRailtripAndDirection.moveToNext()) {
                        hashSet.add(getCursorString(hstTunnelCellByRailtripAndDirection, "cid"));
                    }
                }
                if (hstTunnelCellByRailtripAndDirection != null && !hstTunnelCellByRailtripAndDirection.isClosed()) {
                    hstTunnelCellByRailtripAndDirection.close();
                }
            } catch (Exception e) {
                log("Error retrieving data from database: " + e.getMessage());
                if (hstTunnelCellByRailtripAndDirection != null && !hstTunnelCellByRailtripAndDirection.isClosed()) {
                }
            }
            logD("cidlist: " + hashSet);
            return hashSet;
        } catch (Throwable th) {
            if (hstTunnelCellByRailtripAndDirection != null && !hstTunnelCellByRailtripAndDirection.isClosed()) {
                hstTunnelCellByRailtripAndDirection.close();
            }
            throw th;
        }
    }

    public Set getHSTSmartlinkCellFromDb(String str) {
        Cursor cellByRailtripAndScore = null;
        if (TextUtils.isEmpty(str)) {
            log("railtrip is empty!");
            return null;
        }
        HashSet hashSet = new HashSet();
        try {
            try {
                cellByRailtripAndScore = this.mDbHelper.getCellByRailtripAndScore(str, 2);
                if (cellByRailtripAndScore != null) {
                    while (cellByRailtripAndScore.moveToNext()) {
                        hashSet.add(getCursorString(cellByRailtripAndScore, "cid"));
                    }
                }
                if (cellByRailtripAndScore != null) {
                    cellByRailtripAndScore.close();
                }
            } catch (Exception e) {
                log("Error retrieving data from database: " + e.getMessage());
                if (cellByRailtripAndScore != null) {
                }
            }
            logD("cidlist: " + hashSet);
            return hashSet;
        } catch (Throwable th) {
            if (cellByRailtripAndScore != null) {
                cellByRailtripAndScore.close();
            }
            throw th;
        }
    }

    public Set getHSTNrToLteCellFromDb(int i) {
        HashSet hashSet = new HashSet();
        Cursor hstCellByScore = null;
        try {
            try {
                hstCellByScore = this.mDbHelper.getHstCellByScore(i);
                if (hstCellByScore != null) {
                    while (hstCellByScore.moveToNext()) {
                        hashSet.add(getCursorString(hstCellByScore, "cid"));
                    }
                }
                if (hstCellByScore != null && !hstCellByScore.isClosed()) {
                    hstCellByScore.close();
                }
            } catch (Exception e) {
                log("Error retrieving data from database: " + e.getMessage());
                if (hstCellByScore != null && !hstCellByScore.isClosed()) {
                }
            }
            logD("cidlist: " + hashSet);
            return hashSet;
        } catch (Throwable th) {
            if (hstCellByScore != null && !hstCellByScore.isClosed()) {
                hstCellByScore.close();
            }
            throw th;
        }
    }

    public HashMap getHSTDubiousCellFromDb(String str) {
        String strSubstring;
        String strSubstring2;
        Cursor cellByRailtripAndScore = null;
        if (TextUtils.isEmpty(str)) {
            log("railtrips is empty!");
            return null;
        }
        HashMap map = new HashMap();
        try {
            try {
                cellByRailtripAndScore = this.mDbHelper.getCellByRailtripAndScore(str, 1);
                if (cellByRailtripAndScore != null) {
                    while (cellByRailtripAndScore.moveToNext()) {
                        StringBuilder sb = new StringBuilder();
                        String str2 = getCursorInt(cellByRailtripAndScore, "rat") == 0 ? "lte" : "nr";
                        int cursorInt = getCursorInt(cellByRailtripAndScore, "plmn");
                        if (String.valueOf(cursorInt).length() != 5) {
                            strSubstring = "";
                            strSubstring2 = "";
                        } else {
                            strSubstring = String.valueOf(cursorInt).substring(0, 3);
                            strSubstring2 = String.valueOf(cursorInt).substring(3, 5);
                        }
                        String cursorString = getCursorString(cellByRailtripAndScore, "cid");
                        String cursorString2 = getCursorString(cellByRailtripAndScore, "band");
                        String cursorString3 = getCursorString(cellByRailtripAndScore, "arfcn");
                        int cursorInt2 = getCursorInt(cellByRailtripAndScore, "pci");
                        String str3 = "CEF:RLF:PINGPONG:LOW_DATA_RATE";
                        if (str2.equals("nr")) {
                            str3 = "CEF:RLF:PINGPONG:LOW_DATA_RATE|30";
                        }
                        if (!TextUtils.isEmpty(strSubstring) && !TextUtils.isEmpty(strSubstring2)) {
                            sb.append(str2);
                            sb.append("|");
                            sb.append(strSubstring);
                            sb.append("|");
                            sb.append(strSubstring2);
                            sb.append("|");
                            sb.append(cursorString);
                            sb.append("|");
                            sb.append(cursorString2);
                            sb.append("|");
                            sb.append(cursorString3);
                            sb.append("|");
                            sb.append(String.valueOf(cursorInt2));
                            sb.append("|");
                            sb.append(str3);
                            map.put(cursorString, sb.toString());
                        }
                    }
                }
                if (cellByRailtripAndScore != null) {
                    cellByRailtripAndScore.close();
                }
            } catch (Exception e) {
                log("Error retrieving data from database: " + e.getMessage());
                if (cellByRailtripAndScore != null) {
                }
            }
            logD("SnsBlackCellMap: " + map);
            return map;
        } catch (Throwable th) {
            if (cellByRailtripAndScore != null) {
                cellByRailtripAndScore.close();
            }
            throw th;
        }
    }

    public Set getHSTFDD2TDDCellFromDb(String str) {
        Cursor cellByRailtripAndScore = null;
        if (TextUtils.isEmpty(str)) {
            log("railtrips is empty!");
            return null;
        }
        HashSet hashSet = new HashSet();
        try {
            try {
                cellByRailtripAndScore = this.mDbHelper.getCellByRailtripAndScore(str, 3);
                if (cellByRailtripAndScore != null) {
                    while (cellByRailtripAndScore.moveToNext()) {
                        hashSet.add(getCursorString(cellByRailtripAndScore, "cid"));
                    }
                }
                if (cellByRailtripAndScore != null) {
                    cellByRailtripAndScore.close();
                }
            } catch (Exception e) {
                log("Error retrieving data from database: " + e.getMessage());
                if (cellByRailtripAndScore != null) {
                }
            }
            logD("cidlist: " + hashSet);
            return hashSet;
        } catch (Throwable th) {
            if (cellByRailtripAndScore != null) {
                cellByRailtripAndScore.close();
            }
            throw th;
        }
    }

    public Map getHSTPreferCellFromDb(String str) throws Throwable {
        HashMap map;
        str = str;
        String str2 = "nextscore";
        String str3 = "nextpci";
        String str4 = "nextarfcn";
        String str5 = "nextband";
        String str6 = "nextcid";
        Cursor cursor = null;
        if (TextUtils.isEmpty(str)) {
            log("railtrip is empty!");
            return null;
        }
        HashMap map2 = new HashMap();
        try {
            try {
                Cursor preferCellTableDataByRailtrip = this.mDbHelper.getPreferCellTableDataByRailtrip(str);
                if (preferCellTableDataByRailtrip != null) {
                    while (preferCellTableDataByRailtrip.moveToNext()) {
                        try {
                            try {
                                String str7 = str2;
                                HashMap map3 = new HashMap();
                                String str8 = str3;
                                int cursorInt = getCursorInt(preferCellTableDataByRailtrip, "plmn");
                                int cursorInt2 = getCursorInt(preferCellTableDataByRailtrip, "nextplmn");
                                String str9 = str4;
                                if (String.valueOf(cursorInt).length() == 5 && String.valueOf(cursorInt2).length() == 5) {
                                    String str10 = str6;
                                    map3.put("mcc", String.valueOf(cursorInt).substring(0, 3));
                                    map3.put("mnc", String.valueOf(cursorInt).substring(3, 5));
                                    map3.put("nextmcc", String.valueOf(cursorInt2).substring(0, 3));
                                    map3.put("nextmnc", String.valueOf(cursorInt2).substring(3, 5));
                                    map3.put("railtrip", str);
                                    map3.put("rat", String.valueOf(getCursorInt(preferCellTableDataByRailtrip, "rat")));
                                    String cursorString = getCursorString(preferCellTableDataByRailtrip, "cid");
                                    map3.put("cid", cursorString);
                                    map3.put("direction", String.valueOf(getCursorInt(preferCellTableDataByRailtrip, "direction")));
                                    map3.put("band", getCursorString(preferCellTableDataByRailtrip, "band"));
                                    map3.put("arfcn", getCursorString(preferCellTableDataByRailtrip, "arfcn"));
                                    map3.put("pci", String.valueOf(getCursorInt(preferCellTableDataByRailtrip, "pci")));
                                    map3.put("score", String.valueOf(getCursorInt(preferCellTableDataByRailtrip, "score")));
                                    map3.put("nextrat", String.valueOf(getCursorInt(preferCellTableDataByRailtrip, "nextrat")));
                                    map3.put(str10, getCursorString(preferCellTableDataByRailtrip, str10));
                                    str5 = str5;
                                    map3.put(str5, getCursorString(preferCellTableDataByRailtrip, str5));
                                    map3.put(str9, getCursorString(preferCellTableDataByRailtrip, str9));
                                    map3.put(str8, String.valueOf(getCursorInt(preferCellTableDataByRailtrip, str8)));
                                    map3.put(str7, String.valueOf(getCursorInt(preferCellTableDataByRailtrip, str7)));
                                    map = map2;
                                    try {
                                        map.put(cursorString, map3);
                                        map2 = map;
                                        str4 = str9;
                                        str2 = str7;
                                        str3 = str8;
                                        str6 = str10;
                                    } catch (Exception e) {
                                        e = e;
                                        cursor = preferCellTableDataByRailtrip;
                                        log("Error retrieving data from database: " + e.getMessage());
                                        if (cursor != null) {
                                            cursor.close();
                                        }
                                        logD("preferCellCidlist: " + map);
                                        return map;
                                    }
                                } else {
                                    str2 = str7;
                                    str3 = str8;
                                    str4 = str9;
                                }
                            } catch (Exception e2) {
                                e = e2;
                                map = map2;
                            }
                        } catch (Throwable th) {
                            th = th;
                            cursor = preferCellTableDataByRailtrip;
                            if (cursor != null) {
                                cursor.close();
                            }
                            throw th;
                        }
                    }
                }
                map = map2;
                if (preferCellTableDataByRailtrip != null) {
                    preferCellTableDataByRailtrip.close();
                }
            } catch (Exception e3) {
                e = e3;
                map = map2;
            }
            logD("preferCellCidlist: " + map);
            return map;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public boolean getFDDToTDDStatus() {
        return this.mFddToTddEnabled;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("SignalMapHSTCloudController", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logD(String str) {
        if (DEBUG) {
            Log.d("SignalMapHSTCloudController", str);
        }
    }
}
