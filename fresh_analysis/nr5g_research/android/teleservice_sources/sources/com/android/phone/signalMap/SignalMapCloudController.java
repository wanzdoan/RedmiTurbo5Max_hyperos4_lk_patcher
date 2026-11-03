package com.android.phone.signalMap;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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
import com.android.phone.NetworkRecoveryStrategyImpl;
import com.android.phone.PhoneGlobals;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.performance.PreferenceUtils;
import com.ot.pubsub.a.a;
import com.xiaomi.mirilhook.MiRilHook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/* JADX INFO: loaded from: classes.dex */
public class SignalMapCloudController {
    public static boolean DEBUG = false;
    private static String mCity;
    private static Context mContext;
    private static String mDistrict;
    private static String mLastCity;
    private static String mLastDistrict;
    private static String mLastProvince;
    private static String mProvince;
    private static SignalMapCloudController sInstance;
    private BroadcastReceiver mDLReceiver;
    private CloudCellDbHelper mDbHelper;
    private int mDownloadRetryCnt;
    private SignalMapCloudHandler mHandler;
    private LocationManager mLocationManager;
    private MiuiCellDetectionStrategy mMiuiCellDetectionStrategy;
    private HandlerThread mWorkThread;
    private HashMap mSnsBlackCellMap = null;
    private HashMap mCommonBlackCellMap = null;
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.signalMap.SignalMapCloudController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && "com.android.phone.SignalMapFeatureController.debug".equals(action)) {
                SignalMapCloudController.this.mHandler.obtainMessage(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT, intent).sendToTarget();
            }
        }
    };

    public static synchronized SignalMapCloudController getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new SignalMapCloudController();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    public SignalMapCloudController() {
        this.mHandler = null;
        this.mWorkThread = null;
        mContext = PhoneGlobals.getInstance();
        HandlerThread handlerThread = new HandlerThread("SignalMapCloudController", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        SignalMapCloudHandler signalMapCloudHandler = new SignalMapCloudHandler(this.mWorkThread.getLooper());
        this.mHandler = signalMapCloudHandler;
        signalMapCloudHandler.obtainMessage(100).sendToTarget();
        this.mMiuiCellDetectionStrategy = MiuiCellDetectionStrategy.getInstance();
        this.mLocationManager = (LocationManager) mContext.getSystemService("location");
        EventHubManager.getInstance().registerListener("telephony", "cloud_update", new BaseEvent.IEventListener() { // from class: com.android.phone.signalMap.SignalMapCloudController$$ExternalSyntheticLambda0
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public final void onEvent(BaseEvent baseEvent) {
                this.f$0.lambda$new$0(baseEvent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(BaseEvent baseEvent) {
        if (baseEvent instanceof TelephonyEvent.CloudEvent) {
            SignalMapCloudHandler signalMapCloudHandler = this.mHandler;
            if (signalMapCloudHandler == null) {
                log("handler is null");
            } else {
                signalMapCloudHandler.obtainMessage(102).sendToTarget();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initAction() {
        registerReceiver();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.phone.SignalMapFeatureController.debug");
        mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    class SignalMapCloudHandler extends Handler {
        SignalMapCloudHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) throws Throwable {
            int i = message.what;
            if (i != 105) {
                switch (i) {
                    case 100:
                        SignalMapCloudController.this.initAction();
                        SignalMapCloudController.this.mDbHelper = CloudCellDbHelper.getInstance(SignalMapCloudController.mContext);
                        if (PreferenceUtils.getInstance().getLongPref("signalmap_default_timeout") == 0) {
                            PreferenceUtils.getInstance().setLongPref("signalmap_default_timeout", 1727712000L);
                        }
                        try {
                            int signalMapCellCount = SignalMapCloudController.this.mDbHelper.getSignalMapCellCount();
                            int i2 = Settings.System.getInt(SignalMapCloudController.mContext.getContentResolver(), "clear_signalmap_db", 0);
                            SignalMapCloudController.this.log("isClearDb " + i2);
                            if (signalMapCellCount == 0 && i2 == 0) {
                                SignalMapCloudController.this.mDbHelper.readAndLoadDefaultSignalMap();
                            } else {
                                SignalMapCloudController.this.mDbHelper.readAndLoadSignalMap(false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            SignalMapCloudController.this.mDbHelper.readAndLoadDefaultHstFrequency();
                            NetworkRecoveryStrategyImpl.clearHstFrequencyCache();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            return;
                        }
                        break;
                    case 101:
                        SignalMapCloudController.this.log("EVENT_CLOUD_DOWNLOADED");
                        try {
                            if (SignalMapCloudController.this.checkDownloadStatus(message)) {
                                SignalMapCloudController.this.mDbHelper.readAndLoadSignalMap(true);
                            }
                        } catch (Exception e3) {
                            e3.printStackTrace();
                            return;
                        }
                        break;
                    case 102:
                        SignalMapCloudController.this.updateCloudFeatureStatus(false);
                        break;
                }
                return;
            }
            Intent intent = (Intent) message.obj;
            int intExtra = intent.getIntExtra("type", 0);
            String stringExtra = intent.getStringExtra("url");
            int intExtra2 = intent.getIntExtra("version", 0);
            intent.getStringExtra("sublocality");
            String stringExtra2 = intent.getStringExtra("city");
            SignalMapCloudController.mProvince = intent.getStringExtra("mProvince");
            SignalMapCloudController.mCity = intent.getStringExtra("mCity");
            SignalMapCloudController.mDistrict = intent.getStringExtra("mDistrict");
            if (intExtra == 0) {
                if (!TextUtils.isEmpty(stringExtra)) {
                    SignalMapCloudController.this.downloadCloudFile(stringExtra, intExtra2, true);
                }
                try {
                    if (SignalMapCloudController.this.mDbHelper.readAndLoadSignalMap(true)) {
                        SignalMapCloudController.this.getSignalMapCellBlackList(stringExtra2);
                        SignalMapCloudController.this.getSignalMapSnsCellBlackListByCloud(stringExtra2);
                        return;
                    }
                    return;
                } catch (Exception e4) {
                    e4.printStackTrace();
                    return;
                }
            }
            if (!SignalMapCloudController.mProvince.equals(SignalMapCloudController.mLastProvince) || !SignalMapCloudController.mCity.equals(SignalMapCloudController.mLastCity)) {
                SignalMapCloudController.this.logD("EVENT_TEST: , mProvince = " + SignalMapCloudController.mProvince.toString() + ", mCity = " + SignalMapCloudController.mCity.toString());
                SignalMapCloudController.this.updateCloudFeatureStatus(true);
            }
            if (!SignalMapCloudController.mDistrict.equals(SignalMapCloudController.mLastDistrict)) {
                SignalMapCloudController.this.logD("EVENT_TEST: , mDistrict = " + SignalMapCloudController.mDistrict.toString());
                SignalMapSnsControllerBase.getInstance().updateSignalMapCellCloud();
                SignalMapCloudController.this.mMiuiCellDetectionStrategy.updateBlackCellList();
            }
            SignalMapCloudController.mLastProvince = SignalMapCloudController.mProvince;
            SignalMapCloudController.mLastCity = SignalMapCloudController.mCity;
            SignalMapCloudController.mLastDistrict = SignalMapCloudController.mDistrict;
        }
    }

    public synchronized void updateCloudFeatureStatus(boolean z) {
        try {
            try {
                List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(mContext.getContentResolver(), "SignalFeatureMapCellBlackList");
                if (cloudDataList == null || cloudDataList.size() == 0) {
                    log("CloudCell cloudList is empty");
                } else {
                    for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                        if (cloudData != null) {
                            String string = cloudData.getString("Model", "NA");
                            String string2 = cloudData.getString("SimOperator", "NA");
                            String string3 = cloudData.getString("ForceUpdate", "NA");
                            long j = cloudData.getLong("default_timeout", 0L);
                            if (j != 0) {
                                logD("cloud defaultTimeout =" + j);
                                PreferenceUtils.getInstance().setLongPref("signalmap_default_timeout", j);
                            }
                            String strSubstringAfter = "";
                            if (FiveGCloudUtils.modelMatched(string) && FiveGCloudUtils.simOperatorMatched(string2)) {
                                String string4 = cloudData.getString("cell_url", "");
                                if (!TextUtils.isEmpty(string4) && !TextUtils.isEmpty(mCity)) {
                                    String[] strArrSplit = string4.split(",");
                                    for (int i = 0; i < strArrSplit.length; i++) {
                                        if (strArrSplit[i].contains(mCity)) {
                                            strSubstringAfter = substringAfter(strArrSplit[i], "_");
                                            break;
                                        }
                                    }
                                    int i2 = cloudData.getInt("cell_ver", 0);
                                    if (!TextUtils.isEmpty(strSubstringAfter)) {
                                        if (string3.equals("1") || z) {
                                            logD("force downloadCloudFile!");
                                            downloadCloudFile(strSubstringAfter, i2, true);
                                        } else {
                                            downloadCloudFile(strSubstringAfter, i2, false);
                                        }
                                    }
                                }
                            }
                            log("updateCloudFeatureStatus data=" + cloudData);
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

    private static String substringAfter(String str, String str2) {
        int iIndexOf;
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        if (str2 == null || (iIndexOf = str.indexOf(str2)) == -1) {
            return "";
        }
        return str.substring(iIndexOf + str2.length());
    }

    private int getCursorInt(Cursor cursor, String str) {
        return cursor.getInt(cursor.getColumnIndex(str));
    }

    private String getCursorString(Cursor cursor, String str) {
        return cursor.getString(cursor.getColumnIndex(str));
    }

    private String getSignalMapZipName(int i) {
        return "blackcelldb_" + i + ".zip";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void downloadCloudFile(String str, int i, boolean z) {
        long intPreference = PreferenceUtils.getInstance().getIntPreference("cell_version");
        if (!z && i <= intPreference) {
            log("invalid cloud version for cell data");
            return;
        }
        try {
            DownloadManager downloadManager = (DownloadManager) mContext.getSystemService("download");
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
            request.setAllowedNetworkTypes(2);
            request.setDestinationInExternalFilesDir(mContext, "CloudCell", getSignalMapZipName(i));
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
            BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.signalMap.SignalMapCloudController.2
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    long longExtra = intent.getLongExtra("extra_download_id", -1L);
                    SignalMapCloudController.this.log(intent.getAction() + " id:" + longExtra);
                    SignalMapCloudController.this.mHandler.obtainMessage(101, 0, i, Long.valueOf(longExtra)).sendToTarget();
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
                    if (!TextUtils.isEmpty(string) || !string.startsWith("file://")) {
                        file = new File(Uri.parse(string).getPath());
                    } else {
                        file = new File(mContext.getExternalFilesDir("CloudCell") + File.separator + getSignalMapZipName(i3));
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
            log("check cell file download status error." + e);
            e.printStackTrace();
            return z2;
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
                                    if (name != null && name.matches("blackcelldb_\\S+?_\\d+\\.json")) {
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

    public Set getSignalMapCellBlackList(String str) {
        if (TextUtils.isEmpty(str)) {
            log("city is empty!");
            return null;
        }
        Cursor signalMapTableData = this.mDbHelper.getSignalMapTableData(str);
        HashSet hashSet = new HashSet();
        this.mCommonBlackCellMap = new HashMap();
        if (signalMapTableData != null) {
            while (signalMapTableData.moveToNext()) {
                String cursorString = getCursorString(signalMapTableData, "plmn");
                String cursorString2 = getCursorString(signalMapTableData, "cid");
                String cursorString3 = getCursorString(signalMapTableData, "tac");
                getCursorInt(signalMapTableData, "rat");
                getCursorInt(signalMapTableData, "cause");
                String str2 = cursorString + "_" + cursorString2 + "_" + cursorString3;
                hashSet.add(str2);
                this.mCommonBlackCellMap.put(cursorString2, str2);
            }
        }
        logD("blacklist: " + hashSet);
        if (signalMapTableData != null) {
            signalMapTableData.close();
        }
        return hashSet;
    }

    public HashMap getCommonBlackCellMap() {
        logD("mCommonBlackCellMap: " + this.mCommonBlackCellMap);
        return this.mCommonBlackCellMap;
    }

    public String getSignalMapSnsCellBlackListByCloud(String str) {
        String strSubstring;
        String strSubstring2;
        String str2;
        if (TextUtils.isEmpty(str)) {
            log("city is empty!");
            return null;
        }
        Cursor signalMapTableData = this.mDbHelper.getSignalMapTableData(str);
        HashSet hashSet = new HashSet();
        this.mSnsBlackCellMap = new HashMap();
        if (signalMapTableData != null) {
            while (signalMapTableData.moveToNext()) {
                StringBuilder sb = new StringBuilder();
                String str3 = getCursorInt(signalMapTableData, "rat") == 0 ? "lte" : "nr";
                int cursorInt = getCursorInt(signalMapTableData, "plmn");
                if (String.valueOf(cursorInt).length() != 5) {
                    strSubstring = "";
                    strSubstring2 = "";
                } else {
                    strSubstring = String.valueOf(cursorInt).substring(0, 3);
                    strSubstring2 = String.valueOf(cursorInt).substring(3, 5);
                }
                String cursorString = getCursorString(signalMapTableData, "cid");
                String cursorString2 = getCursorString(signalMapTableData, "band");
                String cursorString3 = getCursorString(signalMapTableData, "arfcn");
                int cursorInt2 = getCursorInt(signalMapTableData, "pci");
                if (!str3.equals("nr")) {
                    str2 = "CEF:RLF:PINGPONG:LOW_DATA_RATE";
                } else {
                    str2 = "CEF:RLF:PINGPONG:LOW_DATA_RATE|30";
                }
                if (!TextUtils.isEmpty(strSubstring) && !TextUtils.isEmpty(strSubstring2)) {
                    sb.append(str3);
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
                    sb.append(str2);
                    String string = sb.toString();
                    hashSet.add(string);
                    this.mSnsBlackCellMap.put(cursorString, string);
                }
            }
        }
        logD("blacklist: " + hashSet);
        StringBuilder sb2 = new StringBuilder();
        Iterator it = hashSet.iterator();
        while (it.hasNext()) {
            sb2.append((String) it.next());
            sb2.append(",");
        }
        String string2 = sb2.toString();
        if (signalMapTableData != null) {
            signalMapTableData.close();
        }
        return string2;
    }

    public HashMap getSnsBlackCellMap() {
        return this.mSnsBlackCellMap;
    }

    public void getAddressFromLocation(Location location) {
        final double latitude = location.getLatitude();
        final double longitude = location.getLongitude();
        new Thread() { // from class: com.android.phone.signalMap.SignalMapCloudController.3
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                List<Address> fromLocation;
                try {
                    fromLocation = new Geocoder(SignalMapCloudController.mContext, Locale.ENGLISH).getFromLocation(latitude, longitude, 1);
                } catch (Exception e) {
                    SignalMapCloudController.this.log("Get Location Error");
                    e.printStackTrace();
                    fromLocation = null;
                }
                List<Address> list = fromLocation;
                if (list == null || list.isEmpty()) {
                    return;
                }
                synchronized (list) {
                    try {
                        for (Address address : list) {
                            if (address != null) {
                                SignalMapCloudController.mProvince = SignalMapCloudController.this.formatAddress(address.getAdminArea());
                                SignalMapCloudController.mCity = SignalMapCloudController.this.formatAddress(address.getLocality());
                                SignalMapCloudController.mDistrict = SignalMapCloudController.this.formatAddress(address.getSubLocality());
                                SignalMapCloudController.this.logD("mProvince: " + SignalMapCloudController.mProvince + ",mCity: " + SignalMapCloudController.mCity + ",mDistrict: " + SignalMapCloudController.mDistrict);
                                if (!SignalMapCloudController.mProvince.equals(SignalMapCloudController.mLastProvince) || !SignalMapCloudController.mCity.equals(SignalMapCloudController.mLastCity)) {
                                    SignalMapCloudController.this.updateCloudFeatureStatus(true);
                                    SignalMapSnsControllerBase.getInstance().updateSignalMapCellCloud();
                                    SignalMapCloudController.this.mMiuiCellDetectionStrategy.updateBlackCellList();
                                }
                                SignalMapCloudController.mLastProvince = SignalMapCloudController.mProvince;
                                SignalMapCloudController.mLastCity = SignalMapCloudController.mCity;
                                SignalMapCloudController.mLastDistrict = SignalMapCloudController.mDistrict;
                            }
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        }.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String formatAddress(String str) {
        if (str != null) {
            return str.contains(" ") ? str.substring(0, str.indexOf(" ")) : str;
        }
        return "";
    }

    public Location getLastKnownLocation() {
        if (this.mLocationManager == null) {
            this.mLocationManager = (LocationManager) mContext.getSystemService("location");
        }
        try {
            Location lastKnownLocation = this.mLocationManager.getLastKnownLocation("gps");
            if (lastKnownLocation != null) {
                return lastKnownLocation;
            }
            Location lastKnownLocation2 = this.mLocationManager.getLastKnownLocation("network");
            return lastKnownLocation2 != null ? lastKnownLocation2 : this.mLocationManager.getLastKnownLocation("passive");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getDistrict() {
        return TextUtils.isEmpty(mDistrict) ? "" : mDistrict;
    }

    public void clearDistrict() {
        logD("clearDistrict");
        mDistrict = "";
    }

    public String getCity() {
        return TextUtils.isEmpty(mCity) ? "" : mCity;
    }

    public int recordCloudIssued() {
        List cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(mContext.getContentResolver(), "SignalFeatureMapCellBlackList");
        int i = (cloudDataList == null || cloudDataList.size() == 0) ? 0 : 1;
        logD("isCloudIssued: " + i);
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("SignalMapCloudController", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logD(String str) {
        if (DEBUG) {
            Log.d("SignalMapCloudController", str);
        }
    }
}
