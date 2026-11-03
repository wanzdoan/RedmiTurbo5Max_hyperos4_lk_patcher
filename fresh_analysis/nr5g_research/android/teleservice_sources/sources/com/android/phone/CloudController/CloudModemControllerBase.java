package com.android.phone.CloudController;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MiuiSettings;
import android.telephony.Rlog;
import android.text.TextUtils;
import com.android.phone.CloudCellDbHelper;
import com.android.phone.CloudModemController;
import com.android.phone.FiveGCloudController;
import com.android.phone.FiveGCloudUtils;
import com.android.phone.PhoneGlobals;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.performance.PreferenceUtils;
import com.android.phone.utils.MiuiThreadUtil;
import com.ot.pubsub.a.a;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import miui.telephony.PhoneDebug;

/* JADX INFO: loaded from: classes.dex */
public abstract class CloudModemControllerBase {
    protected static CloudModemControllerBase sInstance;
    private final Handler mBaseHandler;
    protected boolean mCloudEnable;
    protected Context mContext;
    private BroadcastReceiver mDLReceiver;
    protected CloudCellDbHelper mDbHelper;
    private final Map mDownloadMap = new ConcurrentHashMap();
    private int mDownloadRetryCnt;
    protected BroadcastReceiver mReceiver;
    private final long mTeleStartupTime;

    public abstract void onDatabaseUpdated();

    protected abstract void tryRegisterCallback();

    protected CloudModemControllerBase() {
        Handler handler = new Handler(MiuiThreadUtil.getWorkThreadLooper()) { // from class: com.android.phone.CloudController.CloudModemControllerBase.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i = message.what;
                if (i == 100) {
                    CloudModemControllerBase cloudModemControllerBase = CloudModemControllerBase.this;
                    cloudModemControllerBase.mDbHelper = CloudCellDbHelper.getInstance(cloudModemControllerBase.mContext);
                } else {
                    if (i != 104) {
                        return;
                    }
                    CloudModemControllerBase.log("EVENT_CLOUD_DOWNLOADED");
                    try {
                        CloudModemControllerBase.this.onCloudDataDownloaded(CloudModemControllerBase.this.checkDownloadStatus(message), message.arg2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        this.mBaseHandler = handler;
        this.mContext = PhoneGlobals.getInstance();
        this.mTeleStartupTime = SystemClock.elapsedRealtime();
        initConfigFromCloud();
        EventHubManager.getInstance().registerListener("telephony", "cloud_update", new BaseEvent.IEventListener() { // from class: com.android.phone.CloudController.CloudModemControllerBase$$ExternalSyntheticLambda0
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public final void onEvent(BaseEvent baseEvent) {
                this.f$0.lambda$new$1(baseEvent);
            }
        }, handler);
        handler.sendEmptyMessage(100);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(BaseEvent baseEvent) {
        if (baseEvent instanceof TelephonyEvent.CloudEvent) {
            if (SystemClock.elapsedRealtime() - this.mTeleStartupTime < MirrorDesktopHelper.TIMEOUT_MILLIS) {
                log("ignore cloud update in 5s after startup");
            } else {
                this.mBaseHandler.post(new Runnable() { // from class: com.android.phone.CloudController.CloudModemControllerBase$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$new$0();
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        updateCloudFeatureStatus(false);
    }

    public static synchronized CloudModemControllerBase init() {
        try {
            if (sInstance == null) {
                sInstance = new CloudModemController();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void log(String str) {
        Rlog.d("CloudMdCtrl", str);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void logV(String str) {
        if (PhoneDebug.VDBG) {
            Rlog.d("CloudMdCtrl", str);
        }
    }

    private void initConfigFromCloud() {
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(this.mContext.getContentResolver(), "CloudModemControl");
            if (cloudDataList != null && !cloudDataList.isEmpty()) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    if (cloudData.json().has("CloudMdCtrl_enable")) {
                        this.mCloudEnable = cloudData.getBoolean("CloudMdCtrl_enable", true);
                        log("cloud enable:" + this.mCloudEnable);
                        return;
                    }
                }
                return;
            }
            this.mCloudEnable = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void downloadCloudFile(int i, String str, int i2) {
        if (TextUtils.isEmpty(str) || i < 0) {
            log("invalid args type:" + i + " ver:" + i2);
            return;
        }
        if (i2 <= PreferenceUtils.getInstance().getIntPreference("version_cloud_type" + i)) {
            log("invalid cloud version for cell data");
            return;
        }
        try {
            DownloadManager downloadManager = (DownloadManager) this.mContext.getSystemService("download");
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
            request.setAllowedNetworkTypes(3);
            request.setDestinationInExternalFilesDir(this.mContext, "CloudCell", CloudCellDbHelper.getOtaZipName(i, i2));
            request.setNotificationVisibility(2);
            request.setVisibleInDownloadsUi(false);
            request.setAllowedOverMetered(true);
            request.setBypassRecommendedSizeLimit(true);
            long jEnqueue = downloadManager.enqueue(request);
            log("download cloud file, id = " + jEnqueue);
            this.mDownloadMap.put(Long.valueOf(jEnqueue), new int[]{i, i2});
            registerDownloadCompleteReceiver();
            downloadManager.forceDownload(new long[]{jEnqueue});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int checkDownloadStatus(Message message) {
        int i;
        File file;
        DownloadManager downloadManager = (DownloadManager) this.mContext.getSystemService("download");
        DownloadManager.Query query = new DownloadManager.Query();
        long jLongValue = ((Long) message.obj).longValue();
        int i2 = -1;
        try {
            Cursor cursorQuery = downloadManager.query(query.setFilterById(jLongValue));
            try {
                if (cursorQuery == null) {
                    log("null download cursor");
                    if (cursorQuery == null) {
                        return -1;
                    }
                } else {
                    int i3 = cursorQuery.moveToFirst() ? cursorQuery.getInt(cursorQuery.getColumnIndex(a.J)) : 1;
                    log("checkDownloadStatus status:  " + i3 + ", version: " + message.arg2);
                    if (i3 == 8) {
                        this.mDownloadRetryCnt = 0;
                        int i4 = message.arg1;
                        int i5 = message.arg2;
                        String string = cursorQuery.getString(cursorQuery.getColumnIndex("local_uri"));
                        if (!TextUtils.isEmpty(string) && string.startsWith("file://")) {
                            file = new File(Uri.parse(string).getPath());
                        } else {
                            file = new File(this.mContext.getExternalFilesDir("CloudCell") + File.separator + CloudCellDbHelper.getOtaZipName(i4, i5));
                        }
                        extractAndCleanFiles(file);
                        unregisterDownloadCompleteReceiver(jLongValue);
                        i2 = i4;
                    } else if (i3 == 16 && (i = this.mDownloadRetryCnt) < 3) {
                        this.mDownloadRetryCnt = i + 1;
                        log("retry to download retry:" + this.mDownloadRetryCnt);
                        downloadManager.forceDownload(new long[]{jLongValue});
                    } else if (i3 == 16) {
                        log("download failed too many times");
                        unregisterDownloadCompleteReceiver(jLongValue);
                    }
                }
                cursorQuery.close();
                return i2;
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
        } catch (Exception e) {
            log("check cell file download status error." + e);
            e.printStackTrace();
            return -1;
        }
    }

    private void extractAndCleanFiles(File file) {
        extractCellZip(file, CloudCellDbHelper.getCellDbDir(this.mContext));
        logV(file + " deleted:" + file.delete());
    }

    private void registerDownloadCompleteReceiver() {
        if (this.mDLReceiver == null) {
            IntentFilter intentFilter = new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE");
            BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.CloudController.CloudModemControllerBase.2
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    long longExtra = intent.getLongExtra("extra_download_id", -1L);
                    CloudModemControllerBase.log(intent.getAction() + " id:" + longExtra);
                    int[] iArr = (int[]) CloudModemControllerBase.this.mDownloadMap.get(Long.valueOf(longExtra));
                    if (iArr != null) {
                        CloudModemControllerBase.this.mBaseHandler.obtainMessage(MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT, iArr[0], iArr[1], Long.valueOf(longExtra)).sendToTarget();
                    }
                }
            };
            this.mDLReceiver = broadcastReceiver;
            this.mContext.registerReceiver(broadcastReceiver, intentFilter, 2);
        }
    }

    private void unregisterDownloadCompleteReceiver(long j) {
        BroadcastReceiver broadcastReceiver;
        log("unregisterDownloadCompleteReceiver");
        this.mDownloadMap.remove(Long.valueOf(j));
        if (!this.mDownloadMap.isEmpty() || (broadcastReceiver = this.mDLReceiver) == null) {
            return;
        }
        this.mContext.unregisterReceiver(broadcastReceiver);
        this.mDLReceiver = null;
    }

    /* JADX WARN: Code duplicated, block: B:69:0x00c3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    private boolean extractCellZip(File file, String str) {
        try {
            ZipFile zipFile = new ZipFile(file);
            try {
                Enumeration<? extends ZipEntry> enumerationEntries = zipFile.entries();
                while (enumerationEntries.hasMoreElements()) {
                    ZipEntry zipEntryNextElement = enumerationEntries.nextElement();
                    if (zipEntryNextElement.isDirectory()) {
                        log("invalid zip file");
                    } else {
                        File file2 = new File(str + zipEntryNextElement.getName());
                        logV("extracting " + zipEntryNextElement.getName() + " to " + file2.getPath());
                        File parentFile = file2.getParentFile();
                        if (parentFile != null && !parentFile.exists() && !parentFile.mkdirs()) {
                            log("Failed to create parent directory for: " + file2.getPath());
                            zipFile.close();
                            return false;
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
                            file2.delete();
                            zipFile.close();
                            return false;
                        }
                    }
                }
                logV("Done extracting " + file + " to: " + str);
                zipFile.close();
                return true;
            } catch (Throwable th5) {
                try {
                    zipFile.close();
                } catch (Throwable th6) {
                    th5.addSuppressed(th6);
                }
                throw th5;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCloudDataDownloaded(int i, int i2) {
        if (i == 0) {
            if (this.mDbHelper.readAndLoadCloudPush()) {
                onDatabaseUpdated();
                log("cloud cell updated to ver:" + i2);
                PreferenceUtils.getInstance().setIntPreference("version_cloud_type" + i, i2);
                return;
            }
            return;
        }
        if (i == 1) {
            if (this.mDbHelper.readAndLoadCellMap()) {
                log("cell map updated to ver:" + i2);
                PreferenceUtils.getInstance().setIntPreference("version_cloud_type" + i, i2);
                return;
            }
            return;
        }
        if (i == 2) {
            if (this.mDbHelper.readAndLoadManualCellMap()) {
                log("manual map updated to ver:" + i2);
                PreferenceUtils.getInstance().setIntPreference("version_cloud_type" + i, i2);
                return;
            }
            return;
        }
        log("invalid cloud type:" + i);
    }

    protected void updateCloudFeatureStatus(boolean z) {
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(this.mContext.getContentResolver(), "CloudModemControl");
            if (cloudDataList != null && !cloudDataList.isEmpty()) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    boolean z2 = cloudData.getBoolean("CloudMdCtrl_enable", true);
                    if (z2 != this.mCloudEnable) {
                        this.mCloudEnable = z2;
                        tryRegisterCallback();
                        if (!this.mCloudEnable) {
                            this.mBaseHandler.sendEmptyMessage(106);
                        }
                    }
                    if (!FiveGCloudController.geolocationMatched(cloudData.getString("Province", "NA"), cloudData.getString("City", "NA"))) {
                        log("cloud data is not for current location");
                    } else if (FiveGCloudUtils.simOperatorMatched(cloudData.getString("SimOperator", "NA"))) {
                        downloadCloudFile(cloudData.getInt("cell_type", 0), cloudData.getString("cell_url", ""), cloudData.getInt("cell_ver", 0));
                    }
                    logV("updateCloudFeatureStatus data=" + cloudData);
                }
                return;
            }
            log("cloudList is empty");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
