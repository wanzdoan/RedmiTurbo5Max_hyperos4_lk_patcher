package com.android.phone.statistics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.Rlog;
import android.text.TextUtils;
import com.android.id.IdentifierManager;
import com.android.phone.CarrierConfigLoader$$ExternalSyntheticLambda0;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemDumpBase;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneGlobals;
import com.android.phone.module.IUtilityModule;
import com.android.phone.statistics.utils.DigestUtils;
import com.android.phone.statistics.utils.Utils;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.ot.pubsub.a.a;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import miui.mqsas.sdk.MQSEventManagerDelegate;
import miui.os.Build;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class ModemLogUploadToolsBase {
    protected static final String AP_LOG_PATH;
    private static final String DEFAULT_FDS_LOG_PATH;
    private static final String DEVICE;
    protected static final String MODEM_LOG_PATH;
    private static String mApkLogType;
    private static int mApkMask;
    protected static volatile ModemLogUploadToolsBase mInstance;
    protected CloudRuleInfo mCloudRuleInfo;
    protected Context mContext;
    protected WorkerHandler mHandler;
    private boolean mIsModeLogHasAlreadyStarted = false;
    private Map mLimitMap;
    private BroadcastReceiver mModemLogUploadReceiver;
    private HandlerThread mWorkThread;
    private int printLimit;

    protected abstract void startMdServiceInternal(String str);

    protected abstract void stopMdServiceInternal();

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory());
        String str = File.separator;
        sb.append(str);
        sb.append("diag_logs");
        String string = sb.toString();
        MODEM_LOG_PATH = string;
        AP_LOG_PATH = Environment.getExternalStorageDirectory() + str + "MIUI" + str + "debug_log";
        StringBuilder sb2 = new StringBuilder();
        sb2.append(string);
        sb2.append(str);
        sb2.append("FDS_log");
        DEFAULT_FDS_LOG_PATH = sb2.toString();
        DEVICE = Build.DEVICE;
        mApkLogType = "all";
        mApkMask = 7;
    }

    public static ModemLogUploadToolsBase getInstance() {
        return mInstance;
    }

    protected ModemLogUploadToolsBase() {
        HandlerThread handlerThread = new HandlerThread("ModemLogUploadTools", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mHandler = new WorkerHandler(this.mWorkThread.getLooper());
        this.mContext = PhoneGlobals.getInstance();
        this.mLimitMap = new HashMap();
        registerModemLogUploadReceiver();
    }

    public void init() {
        if (MiuiPhoneUtils.isModemLogUploadSupported()) {
            Utils.logD("ModemLogUploadTools", "ModemLogUploadTools init");
        }
    }

    private void registerModemLogUploadReceiver() {
        if (this.mModemLogUploadReceiver == null) {
            this.mModemLogUploadReceiver = new ModemLogUploadReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.xiaomi.modemloguploader.CATCH_AP_LOG");
            intentFilter.addAction("com.xiaomi.modemloguploader.ACTION_UPDATE_FDS_STATUS");
            intentFilter.addAction("com.xiaomi.modemloguploader.ACTION_UPDATE_FDS_LOGTYPE");
            this.mContext.registerReceiver(this.mModemLogUploadReceiver, intentFilter, 2);
        }
    }

    public class ModemLogUploadReceiver extends BroadcastReceiver {
        public ModemLogUploadReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                Utils.logD("ModemLogUploadTools", "null action");
                return;
            }
            String action = intent.getAction();
            Utils.logD("ModemLogUploadTools", "receive action: " + action);
            if ("com.xiaomi.modemloguploader.CATCH_AP_LOG".equals(action)) {
                ModemLogUploadToolsBase.this.catchApLog();
                return;
            }
            if ("com.xiaomi.modemloguploader.ACTION_UPDATE_FDS_STATUS".equals(action)) {
                ModemLogUploadToolsBase.mApkMask = intent.getIntExtra("mask", 0) & 7;
                Utils.logD("ModemLogUploadTools", "update apk mask: " + ModemLogUploadToolsBase.mApkMask);
                return;
            }
            if ("com.xiaomi.modemloguploader.ACTION_UPDATE_FDS_LOGTYPE".equals(action)) {
                ModemLogUploadToolsBase.mApkLogType = intent.getStringExtra("logType");
                Utils.logD("ModemLogUploadTools", "update apk log type: " + ModemLogUploadToolsBase.mApkLogType);
            }
        }
    }

    private CloudRuleInfo getMatchedRule(String str) {
        CloudRuleInfo fDSMatchedRule;
        if (!isFdsApkInstalled("com.xiaomi.modemloguploader")) {
            fDSMatchedRule = getMQSMatchedRule(str);
        } else {
            fDSMatchedRule = getFDSMatchedRule(str);
        }
        if (fDSMatchedRule == null) {
            Utils.logD("ModemLogUploadTools", "get cloudRuleInfo is null..");
            return null;
        }
        if ((fDSMatchedRule.mask & exceptionTypeToMask(str)) == exceptionTypeToMask(str)) {
            return fDSMatchedRule;
        }
        Utils.logD("ModemLogUploadTools", "exception type disabled: " + str);
        return null;
    }

    private CloudRuleInfo getMQSMatchedRule(String str) {
        List rules = MQSEventManagerDelegate.getInstance().getRules("modemPower");
        if (Build.IS_INTERNATIONAL_BUILD) {
            Utils.logD("ModemLogUploadTools", "international build, should not capture log...");
            return null;
        }
        if (rules != null && !rules.isEmpty()) {
            Iterator it = rules.iterator();
            while (it.hasNext()) {
                CloudRuleInfo cloudRuleInfo = new CloudRuleInfo((String) it.next());
                if (cloudRuleInfo.isValid()) {
                    boolean z = TextUtils.isEmpty(cloudRuleInfo.exceptionSet) || "all".equalsIgnoreCase(cloudRuleInfo.exceptionSet) || (!TextUtils.isEmpty(cloudRuleInfo.exceptionSet) && cloudRuleInfo.exceptionSet.contains(str));
                    if (!TextUtils.isEmpty(cloudRuleInfo.deviceList)) {
                        String str2 = cloudRuleInfo.deviceList;
                        String str3 = DEVICE;
                        if (!str2.contains(str3)) {
                            Utils.logD("ModemLogUploadTools", "there is no matched device:" + str3 + " deviceList =" + cloudRuleInfo.deviceList);
                        }
                    }
                    if (!z) {
                        Utils.logD("ModemLogUploadTools", "there is no matched exception type:" + str + " exceptionSet=" + cloudRuleInfo.exceptionSet);
                    } else {
                        Utils.logD("ModemLogUploadTools", "rule id:" + cloudRuleInfo.ruleId);
                        return cloudRuleInfo;
                    }
                }
            }
            return null;
        }
        StringBuilder sb = new StringBuilder("there is no matched rules...rules is empty: ");
        sb.append(rules == null);
        Utils.logD("ModemLogUploadTools", sb.toString());
        return null;
    }

    private CloudRuleInfo getFDSMatchedRule(String str) {
        if ((exceptionTypeToMask(str) & mApkMask) != exceptionTypeToMask(str)) {
            Utils.logD("ModemLogUploadTools", "exception type disabled by apk: " + str);
            return null;
        }
        return new CloudRuleInfo();
    }

    public void StartCaptureModemLog(String str, String str2) {
        if (MiuiPhoneUtils.isModemLogUploadSupported()) {
            String str3 = SystemProperties.get("ro.boot.hwlevel");
            if (!Utils.isUserExperienceProgramEnabled(PhoneGlobals.getInstance()) && "MP".equals(str3)) {
                Utils.logD("ModemLogUploadTools", "modem logs will not be captured: mp and user experience not enabled");
                return;
            }
            boolean zEquals = TextUtils.equals(SystemProperties.get("debug.offline_log.enable", "false"), "true") | this.mIsModeLogHasAlreadyStarted;
            this.mIsModeLogHasAlreadyStarted = zEquals;
            if (zEquals) {
                Utils.logD("ModemLogUploadTools", "modem logs will not be captured: mIsModeLogHasAlreadyStarted = " + this.mIsModeLogHasAlreadyStarted);
                return;
            }
            Utils.logD("ModemLogUploadTools", "StartCaptureModemLog exceptionType=" + str + ", modemDebugLevel=" + str2);
            CloudRuleInfo matchedRule = getMatchedRule(str);
            if (matchedRule == null) {
                Utils.logD("ModemLogUploadTools", "modem logs will not be captured because there are no matching rules");
                return;
            }
            matchedRule.exceptionType = str;
            matchedRule.modemDebugLevel = str2;
            if (!"dataStall".equals(str) && ((Integer) this.mLimitMap.getOrDefault(str, 0)).intValue() >= 3) {
                Utils.logD("ModemLogUploadTools", "modem logs will not be captured: number = " + this.mLimitMap.getOrDefault(str, 0));
                return;
            }
            matchedRule.logPath = createFdsDir(str);
            this.mCloudRuleInfo = matchedRule;
            this.mIsModeLogHasAlreadyStarted = true;
            Intent intent = new Intent("com.xiaomi.modemloguploader.FDS_LOG_START");
            intent.addFlags(16777216);
            this.mContext.sendBroadcast(intent);
            deleteFileOverCaptured(MODEM_LOG_PATH, str);
            if ("modemDump".equals(matchedRule.logType)) {
                WorkerHandler workerHandler = this.mHandler;
                if (workerHandler != null) {
                    workerHandler.sendMessage(workerHandler.obtainMessage(2));
                    return;
                }
                return;
            }
            if ("apLog".equals(matchedRule.logType)) {
                catchApLog();
                WorkerHandler workerHandler2 = this.mHandler;
                if (workerHandler2 != null) {
                    workerHandler2.sendMessageDelayed(workerHandler2.obtainMessage(7), 1200000L);
                    return;
                }
                return;
            }
            startMdService();
            WorkerHandler workerHandler3 = this.mHandler;
            if (workerHandler3 != null) {
                workerHandler3.sendMessage(workerHandler3.obtainMessage(2));
                if (this.mHandler.hasMessages(1)) {
                    this.mHandler.removeMessages(1);
                }
                WorkerHandler workerHandler4 = this.mHandler;
                workerHandler4.sendMessageDelayed(workerHandler4.obtainMessage(1), 300000L);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int exceptionTypeToMask(String str) {
        str.hashCode();
        switch (str) {
            case "pingPong":
                return 4;
            case "dataStall":
                return 1;
            case "all":
                return 7;
            case "oos":
                return 2;
            default:
                return 0;
        }
    }

    private void startMdService() {
        if (this.mCloudRuleInfo == null) {
            return;
        }
        Utils.logD("ModemLogUploadTools", "start capture ModemLog");
        startMdServiceInternal(this.mCloudRuleInfo.modemDebugLevel);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopMdService() {
        if (!this.mIsModeLogHasAlreadyStarted || this.mCloudRuleInfo == null) {
            return;
        }
        Utils.logD("ModemLogUploadTools", "stop capture ModemLog");
        stopMdServiceInternal();
    }

    protected void uploadLogAfterStopMdService(String str) {
        if (isFdsApkInstalled("com.xiaomi.modemloguploader")) {
            Utils.logD("ModemLogUploadTools", "no need upload file to mqs");
            return;
        }
        if (!TextUtils.isEmpty(str)) {
            if (new File(str).exists()) {
                Utils.logD("ModemLogUploadTools", "modem log file:" + str);
                if (this.mCloudRuleInfo.version == 1) {
                    uploadLog(str);
                    return;
                }
                return;
            }
            endCatch();
            return;
        }
        endCatch();
    }

    private String createFdsDir(String str) {
        File file = new File(MODEM_LOG_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        String str2 = DEFAULT_FDS_LOG_PATH + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date(System.currentTimeMillis())) + "_" + str + "_" + getOaid();
        File file2 = new File(str2);
        if (!file2.exists()) {
            file2.mkdir();
        }
        return str2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void catchModemDump(String str) {
        CloudRuleInfo cloudRuleInfo = this.mCloudRuleInfo;
        if (cloudRuleInfo == null) {
            return;
        }
        FileDescriptor fileDescriptorOpenFile = cloudRuleInfo.openFile(str + File.separator + "default_modem_dump.txt");
        catchOtherDumpInfo(fileDescriptorOpenFile, this.mCloudRuleInfo.pw);
        if (ModemDumpBase.getInstance() != null) {
            ModemDumpBase.getInstance().dump(fileDescriptorOpenFile, this.mCloudRuleInfo.pw, null, true);
        }
        Utils.logD("ModemLogUploadTools", "start catch modem dump file and lasts 60 seconds :" + this.mCloudRuleInfo.dumpFile.getAbsolutePath());
        WorkerHandler workerHandler = this.mHandler;
        if (workerHandler != null) {
            workerHandler.sendMessageDelayed(workerHandler.obtainMessage(3, str), RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void catchModemDumpDown(String str) {
        WorkerHandler workerHandler;
        Utils.logD("ModemLogUploadTools", "catch modem dump file down.");
        this.mCloudRuleInfo.closeFile();
        CloudRuleInfo cloudRuleInfo = this.mCloudRuleInfo;
        if (cloudRuleInfo.version == 1) {
            uploadLog(str);
        } else {
            if (!"modemDump".equals(cloudRuleInfo.logType) || (workerHandler = this.mHandler) == null) {
                return;
            }
            workerHandler.sendMessage(workerHandler.obtainMessage(4));
        }
    }

    private void catchOtherDumpInfo(FileDescriptor fileDescriptor, PrintWriter printWriter) {
        if (printWriter == null || fileDescriptor == null || !fileDescriptor.valid()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("build fingerprint : " + Build.FINGERPRINT + "\n");
        sb.append("hwlevel : " + SystemProperties.get("ro.boot.hwlevel") + "\n");
        printWriter.println(sb);
        printWriter.flush();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void catchApLog() {
        Intent intent = new Intent("android.provider.Telephony.SECRET_CODE", Uri.parse("android_secret_code://9434"));
        intent.addFlags(16777216);
        this.mContext.sendBroadcast(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void endApCatch() {
        WorkerHandler workerHandler = this.mHandler;
        if (workerHandler != null) {
            workerHandler.sendMessage(workerHandler.obtainMessage(6));
            WorkerHandler workerHandler2 = this.mHandler;
            workerHandler2.sendMessage(workerHandler2.obtainMessage(4));
        }
    }

    public class WorkerHandler extends Handler {
        WorkerHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            WorkerHandler workerHandler;
            Utils.logD("ModemLogUploadTools", "handleMessage=" + message.what);
            switch (message.what) {
                case 1:
                    ModemLogUploadToolsBase.this.stopMdService();
                    File lastModifiedFile = ModemLogUploadToolsBase.this.getLastModifiedFile(ModemLogUploadToolsBase.MODEM_LOG_PATH);
                    File file = new File(ModemLogUploadToolsBase.this.mCloudRuleInfo.logPath);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    ModemLogUploadToolsBase.this.moveFileToDirectory(lastModifiedFile, file);
                    ModemLogUploadToolsBase.this.catchApLog();
                    WorkerHandler workerHandler2 = ModemLogUploadToolsBase.this.mHandler;
                    if (workerHandler2 != null) {
                        workerHandler2.sendMessageDelayed(workerHandler2.obtainMessage(7), 1200000L);
                    }
                    break;
                case 2:
                    ModemLogUploadToolsBase modemLogUploadToolsBase = ModemLogUploadToolsBase.this;
                    modemLogUploadToolsBase.catchModemDump(modemLogUploadToolsBase.mCloudRuleInfo.logPath);
                    break;
                case 3:
                    try {
                        ModemLogUploadToolsBase.this.catchModemDumpDown((String) message.obj);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    break;
                case 4:
                    ModemLogUploadToolsBase.this.endCatch();
                    break;
                case 5:
                    String oaid = IdentifierManager.getOAID(ModemLogUploadToolsBase.this.mContext);
                    if (TextUtils.isEmpty(oaid) && ModemLogUploadToolsBase.this.printLimit < 3 && (workerHandler = ModemLogUploadToolsBase.this.mHandler) != null) {
                        workerHandler.sendMessageDelayed(workerHandler.obtainMessage(5), RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
                    } else {
                        try {
                            SystemProperties.set("persist.radio.modemlog.index", DigestUtils.getMd5Digest(oaid));
                        } catch (Exception e2) {
                            Rlog.i("ModemLogUploadTools", "set index exception:" + e2);
                        }
                    }
                    ModemLogUploadToolsBase.this.printLimit++;
                    break;
                case 6:
                    Utils.logD("ModemLogUploadTools", "log catch complete, move ap log and compress dir");
                    File lastModifiedFile2 = ModemLogUploadToolsBase.this.getLastModifiedFile(ModemLogUploadToolsBase.AP_LOG_PATH);
                    File file2 = new File(ModemLogUploadToolsBase.this.mCloudRuleInfo.logPath);
                    if (!file2.exists()) {
                        file2.mkdir();
                    }
                    ModemLogUploadToolsBase.this.moveFileToDirectory(lastModifiedFile2, file2);
                    break;
                case 7:
                    ModemLogUploadToolsBase.this.endApCatch();
                    break;
            }
        }
    }

    private void uploadLog(String str) {
        Utils.logD("ModemLogUploadTools", "upload log");
        if (this.mCloudRuleInfo != null) {
            StringBuilder sb = new StringBuilder("uploadLog rules is empty");
            sb.append(this.mCloudRuleInfo == null);
            Utils.logD("ModemLogUploadTools", sb.toString());
            Utils.logD("ModemLogUploadTools", this.mCloudRuleInfo.rule);
            if (MQSEventManagerDelegate.getInstance() != null) {
                MQSEventManagerDelegate.getInstance().commonCaptureLog(String.valueOf(this.mCloudRuleInfo.ruleId), "modemPower", "", str);
            }
            this.mLimitMap.put(this.mCloudRuleInfo.exceptionType, Integer.valueOf(((Integer) this.mLimitMap.getOrDefault(this.mCloudRuleInfo.exceptionType, 0)).intValue() + 1));
            clearLog(str);
            WorkerHandler workerHandler = this.mHandler;
            if (workerHandler != null) {
                workerHandler.sendMessageDelayed(workerHandler.obtainMessage(4), 3600000L);
            }
        }
    }

    private void clearLog(final String str) {
        WorkerHandler workerHandler = this.mHandler;
        if (workerHandler == null) {
            return;
        }
        workerHandler.postDelayed(new Runnable() { // from class: com.android.phone.statistics.ModemLogUploadToolsBase$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$clearLog$0(str);
            }
        }, 30000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearLog$0(String str) {
        Utils.logD("ModemLogUploadTools", "start clear log file");
        deleteFile(new File(str));
        Utils.logD("ModemLogUploadTools", "clear log done!!");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void endCatch() {
        Utils.logD("ModemLogUploadTools", "end catch and clear status");
        CloudRuleInfo cloudRuleInfo = this.mCloudRuleInfo;
        if (cloudRuleInfo != null && cloudRuleInfo.version == 2) {
            Intent intent = new Intent("com.xiaomi.modemloguploader.FDS_LOG_STOP");
            intent.addFlags(16777216);
            intent.putExtra("logPath", this.mCloudRuleInfo.logPath);
            this.mContext.sendBroadcast(intent);
        }
        this.mCloudRuleInfo = null;
        this.mIsModeLogHasAlreadyStarted = false;
    }

    public void performDailyJob() {
        Utils.logD("ModemLogUploadTools", "number of catch log: " + this.mLimitMap.values().stream().mapToInt(new CarrierConfigLoader$$ExternalSyntheticLambda0()).sum());
        this.mLimitMap.clear();
    }

    private boolean isFdsApkInstalled(String str) {
        try {
            if (this.mContext.getPackageManager().getPackageInfo(str, 0) != null) {
                Utils.logD("ModemLogUploadTools", "fds apk installed");
                return true;
            }
        } catch (PackageManager.NameNotFoundException unused) {
        }
        return false;
    }

    private void deleteFileOverCaptured(String str, String str2) {
        File[] fileArrListFiles = new File(str).listFiles();
        if (fileArrListFiles != null) {
            ArrayList arrayList = new ArrayList();
            for (File file : fileArrListFiles) {
                if (file.getName().matches("(.*)" + str2 + "(.*)")) {
                    arrayList.add(file);
                }
            }
            if (arrayList.size() >= 3) {
                Collections.sort(arrayList, new Comparator() { // from class: com.android.phone.statistics.ModemLogUploadToolsBase$$ExternalSyntheticLambda0
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        return Long.compare(((File) obj).lastModified(), ((File) obj2).lastModified());
                    }
                });
                int size = arrayList.size() - 3;
                for (int i = 0; i < size; i++) {
                    File file2 = (File) arrayList.get(0);
                    if (deleteFile(file2)) {
                        Utils.logD("ModemLogUploadTools", "Oldest matching file deleted successfully: " + file2.getName());
                        arrayList.remove(file2);
                    } else {
                        Utils.logD("ModemLogUploadTools", "Failed to delete oldest matching file: " + file2.getName());
                        return;
                    }
                }
                return;
            }
            Utils.logD("ModemLogUploadTools", "No need to delete file path: " + str);
            return;
        }
        Utils.logD("ModemLogUploadTools", "Directory is empty or does not exist: " + str);
    }

    private String getOaid() {
        if (IdentifierManager.isSupported()) {
            return IdentifierManager.getOAID(this.mContext);
        }
        return "null";
    }

    public class CloudRuleInfo {
        String deviceList;
        File dumpFile;
        String exceptionSet;
        String exceptionType;
        FileOutputStream fos;
        String logPath;
        String logType;
        int mask;
        String modemDebugLevel;
        String oaid;
        PrintWriter pw;
        String rule;
        int ruleId;
        private boolean valid;
        int version;

        public CloudRuleInfo(String str) {
            this.oaid = "";
            this.deviceList = "";
            this.exceptionSet = "";
            this.logType = "";
            this.logPath = "";
            this.valid = false;
            this.rule = str;
            initByRule();
            this.version = 1;
        }

        public CloudRuleInfo() {
            this.rule = "";
            this.oaid = "";
            this.deviceList = "";
            this.exceptionSet = "";
            this.logType = "";
            this.logPath = "";
            this.valid = false;
            initByFds();
            this.version = 2;
        }

        private void initByRule() {
            try {
                JSONObject jSONObject = new JSONObject(this.rule);
                this.ruleId = jSONObject.getInt("id");
                this.deviceList = jSONObject.getString("device");
                JSONArray jSONArray = jSONObject.getJSONArray("customized");
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                    String string = jSONObject2.getString("key");
                    String string2 = jSONObject2.getString(a.p);
                    int iHashCode = string.hashCode();
                    if (iHashCode != -268214893) {
                        if (iHashCode != 3403373) {
                            if (iHashCode == 341730462 && string.equals("logType")) {
                                this.logType = string2;
                            }
                        } else if (string.equals("oaid")) {
                            this.oaid = string2;
                        }
                    } else if (string.equals("exceptionSet")) {
                        this.exceptionSet = string2;
                        this.mask = ModemLogUploadToolsBase.exceptionTypeToMask(string2) & 7;
                    }
                }
                this.valid = true;
            } catch (JSONException e) {
                e.printStackTrace();
                Utils.logD("ModemLogUploadTools", "Json exception....");
                this.valid = false;
            }
        }

        private void initByFds() {
            this.logType = ModemLogUploadToolsBase.mApkLogType;
            this.valid = true;
            if (!((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).isSupportFdsLogUploadByCloud()) {
                this.exceptionSet = "all";
                this.mask = ModemLogUploadToolsBase.mApkMask & 7;
            } else {
                Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
                this.exceptionSet = cloudData.getString("fdsparam1");
                this.mask = ModemLogUploadToolsBase.exceptionTypeToMask(cloudData.getString("fdsparam1")) & ModemLogUploadToolsBase.mApkMask & 7;
            }
        }

        public boolean isValid() {
            return this.valid;
        }

        public FileDescriptor openFile(String str) {
            closeFile();
            this.dumpFile = new File(str);
            try {
                this.fos = new FileOutputStream(this.dumpFile);
                this.pw = new PrintWriter(this.fos);
                return this.fos.getFD();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e2) {
                e2.printStackTrace();
                return null;
            }
        }

        public void closeFile() {
            try {
                PrintWriter printWriter = this.pw;
                if (printWriter != null) {
                    printWriter.flush();
                    this.pw.close();
                }
                FileOutputStream fileOutputStream = this.fos;
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                this.pw = null;
                this.fos = null;
                this.dumpFile = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected boolean deleteFile(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        if (file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                if (!deleteFile(file2)) {
                    return false;
                }
            }
        }
        return file.delete();
    }

    protected File getLastModifiedFile(String str) {
        File file = new File(str);
        if (!file.exists()) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        if (file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                if ((MODEM_LOG_PATH.equals(str) && file2.isDirectory()) || (AP_LOG_PATH.equals(str) && file2.getName().endsWith(".zip"))) {
                    arrayList.add(file2);
                }
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        Collections.sort(arrayList, new Comparator() { // from class: com.android.phone.statistics.ModemLogUploadToolsBase$$ExternalSyntheticLambda1
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return ModemLogUploadToolsBase.$r8$lambda$LRwvcAyTTyQ2n9Jtg9ZRX7n3JqA((File) obj, (File) obj2);
            }
        });
        return (File) arrayList.get(arrayList.size() - 1);
    }

    public static /* synthetic */ int $r8$lambda$LRwvcAyTTyQ2n9Jtg9ZRX7n3JqA(File file, File file2) {
        if (file.lastModified() < file2.lastModified()) {
            return -1;
        }
        return file.lastModified() == file2.lastModified() ? 0 : 1;
    }

    protected void moveFileToDirectory(File file, File file2) {
        if (file == null || !file.exists()) {
            Utils.logD("ModemLogUploadTools", "file to move not exist");
            return;
        }
        if (!file2.exists()) {
            file2.mkdirs();
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(file2.getPath());
            String str = File.separator;
            sb.append(str);
            sb.append(file.getName());
            Files.move(file.toPath(), new File(sb.toString()).toPath(), StandardCopyOption.REPLACE_EXISTING);
            Utils.logD("ModemLogUploadTools", "move file complete " + file2.getPath() + str + file.getName());
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
