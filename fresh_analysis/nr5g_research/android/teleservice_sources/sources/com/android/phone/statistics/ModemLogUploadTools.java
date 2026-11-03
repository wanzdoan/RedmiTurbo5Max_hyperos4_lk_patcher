package com.android.phone.statistics;

import android.content.Intent;
import android.os.Debug;
import android.os.Environment;
import android.os.Process;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.SystemService;
import android.text.TextUtils;
import android.util.Log;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.statistics.utils.Utils;
import com.ot.pubsub.a.a;
import dalvik.system.VMDebug;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import miui.mqsas.sdk.MQSEventManagerDelegate;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.PhoneDebug;
import miui.util.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class ModemLogUploadTools extends ModemLogUploadToolsBase {
    private static final long CHECK_INTERVAL_MS;
    private static final String MTK_LOG_PATH;
    private static final String MTK_MODEM_LOG_R_PATH;
    private int increaseCount;
    private boolean mDumpHprof;
    private long mLastCheckHeapUptimeMillis;
    private long mLastUsedEwma;
    private final Runnable mOnGCRunnable;
    private String mRuleId;
    private int mThreshold = 6;
    private final Runnable mHeapWatchRunnable = new Runnable() { // from class: com.android.phone.statistics.ModemLogUploadTools$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.lambda$new$0();
        }
    };

    static {
        String str = Environment.getDataDirectoryPath() + File.separator + "debuglogger/";
        MTK_LOG_PATH = str;
        MTK_MODEM_LOG_R_PATH = str + "mdlog1";
        CHECK_INTERVAL_MS = PhoneDebug.VDBG ? 30000L : 300000L;
    }

    public static void make() {
        if (Build.IS_INTERNATIONAL_BUILD || !MiuiPhoneUtils.isModemLogUploadSupported()) {
            return;
        }
        ModemLogUploadToolsBase.mInstance = new ModemLogUploadTools();
    }

    @Override // com.android.phone.statistics.ModemLogUploadToolsBase
    protected void startMdServiceInternal(String str) {
        if (ModemProvider.IS_QCOM) {
            try {
                if (TextUtils.equals(SystemProperties.get("debug.offline_log.enable", "false"), "false")) {
                    SystemProperties.set("debug.offline_log.modem_level", str);
                    SystemProperties.set("debug.sensors.diag_buffer_log", "false");
                    SystemProperties.set("debug.offline_log.enable", "true");
                    SystemProperties.set("debug.offline_log.module", "modem");
                    SystemService.start("diag_mdlog_start");
                    Utils.logD("ModemLogUploadTools", "start qcom modem log service.");
                }
            } catch (Exception e) {
                Utils.logD("ModemLogUploadTools", "Exception for start service: " + e);
            }
        }
    }

    @Override // com.android.phone.statistics.ModemLogUploadToolsBase
    protected void stopMdServiceInternal() {
        boolean z;
        if (ModemProvider.IS_MTK) {
            uploadLogAfterStopMdService("");
            return;
        }
        if (ModemProvider.IS_QCOM) {
            try {
                Utils.logD("ModemLogUploadTools", "stop qcom modem log service.");
                if (TextUtils.equals(SystemProperties.get("debug.offline_log.enable", "false"), "true")) {
                    SystemProperties.set("debug.offline_log.enable", "false");
                    SystemProperties.set("debug.offline_log.module", "null");
                    SystemProperties.set("debug.offline_log.modem_level", "null");
                    SystemService.start("diag_mdlog_stop");
                    SystemProperties.set("debug.sensors.diag_buffer_log", "true");
                }
                z = true;
            } catch (Exception e) {
                Utils.logD("ModemLogUploadTools", "Exception for stop service: " + e);
                z = false;
            }
            File lastModifiedFile = getLastModifiedFile(ModemLogUploadToolsBase.MODEM_LOG_PATH);
            if (z && lastModifiedFile != null && lastModifiedFile.exists()) {
                uploadLogAfterStopMdService(lastModifiedFile.getAbsolutePath());
            } else {
                uploadLogAfterStopMdService("");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        checkHeap(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        Log.d("ModemLogUploadTools", "onGc");
        checkHeap(true);
    }

    private ModemLogUploadTools() {
        Runnable runnable = new Runnable() { // from class: com.android.phone.statistics.ModemLogUploadTools$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$1();
            }
        };
        this.mOnGCRunnable = runnable;
        if (MiuiPhoneUtils.isMPDevice() || !HyperOSCustFeatureResolve.getBoolean("support_modem_log_upload_bool", false)) {
            return;
        }
        try {
            Class.forName("com.android.internal.os.BinderInternal").getMethod("addGcWatcher", Runnable.class).invoke(null, runnable);
            checkHeap(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkHeap(boolean z) {
        updateHeapMqsRules();
        this.mHandler.removeCallbacks(this.mHeapWatchRunnable);
        if (this.mDumpHprof) {
            return;
        }
        long jUptimeMillis = SystemClock.uptimeMillis();
        if (z || jUptimeMillis - this.mLastCheckHeapUptimeMillis >= CHECK_INTERVAL_MS) {
            if (!z || jUptimeMillis - this.mLastCheckHeapUptimeMillis >= 30000) {
                this.mLastCheckHeapUptimeMillis = jUptimeMillis;
                long jMaxMemory = Runtime.getRuntime().maxMemory();
                long j = Runtime.getRuntime().totalMemory();
                long jFreeMemory = Runtime.getRuntime().freeMemory();
                long j2 = j - jFreeMemory;
                Log.d("ModemLogUploadTools", "max=" + jMaxMemory + ", total=" + j + ", free=" + jFreeMemory + ", used=" + j2);
                if (PhoneDebug.VDBG) {
                    Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
                    Debug.getMemoryInfo(memoryInfo);
                    Log.d("ModemLogUploadTools", "memory stats map=" + memoryInfo.getMemoryStats().toString());
                }
                long j3 = (jMaxMemory * ((long) this.mThreshold)) / 10;
                if (j2 > j3) {
                    Log.d("ModemLogUploadTools", "mem used exceed " + j3 + ", threshold=" + this.mThreshold);
                    dumpHprof();
                    return;
                }
                long j4 = this.mLastUsedEwma;
                long j5 = (long) ((j4 * 0.2d) + (j2 * 0.8d));
                if (j5 > j4) {
                    int i = this.increaseCount + 1;
                    this.increaseCount = i;
                    if (i > 3) {
                        logV("heap increaseCount=" + this.increaseCount);
                    }
                    if (this.increaseCount > 10) {
                        Log.d("ModemLogUploadTools", "heap increaseCount=" + this.increaseCount);
                        dumpHprof();
                        this.increaseCount = 0;
                        return;
                    }
                } else {
                    this.increaseCount = 0;
                }
                this.mLastUsedEwma = j5;
                this.mHandler.postDelayed(this.mHeapWatchRunnable, CHECK_INTERVAL_MS);
            }
        }
    }

    private void dumpHprof() {
        if (TextUtils.isEmpty(this.mRuleId)) {
            Log.d("ModemLogUploadTools", "skip dump due to no MQS rules");
            return;
        }
        this.mLastUsedEwma = 0L;
        this.mHandler.removeCallbacks(this.mHeapWatchRunnable);
        if (this.mDumpHprof) {
            return;
        }
        this.mDumpHprof = true;
        new Thread(new Runnable() { // from class: com.android.phone.statistics.ModemLogUploadTools$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$dumpHprof$3();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dumpHprof$3() {
        String str = PhoneDebug.VDBG ? "/data/mqsas/hprof" : "/data/miuilog/stability/hprof";
        if (!FileUtils.ensureDirExistAndWritable(str)) {
            Log.e("ModemLogUploadTools", "Dump hprof failed. Dir not writable: ".concat(str));
            return;
        }
        int iMyPid = Process.myPid();
        final String packageName = this.mContext.getPackageName();
        String str2 = packageName + "_" + iMyPid + "_" + System.currentTimeMillis() + ".hprof";
        try {
            File[] fileArrListFiles = new File(str).listFiles(new FileFilter() { // from class: com.android.phone.statistics.ModemLogUploadTools$$ExternalSyntheticLambda3
                @Override // java.io.FileFilter
                public final boolean accept(File file) {
                    return ModemLogUploadTools.m3943$r8$lambda$6LjxuaVkevqRsu2hZQrBr0AfPU(packageName, file);
                }
            });
            if (fileArrListFiles != null && fileArrListFiles.length > 0) {
                Arrays.stream(fileArrListFiles).forEach(new Consumer() { // from class: com.android.phone.statistics.ModemLogUploadTools$$ExternalSyntheticLambda4
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ((File) obj).delete();
                    }
                });
            }
            String str3 = str + "/" + str2;
            VMDebug.dumpHprofData(str3);
            MQSEventManagerDelegate.getInstance().commonCaptureLog(this.mRuleId, "modemPower", "", str3);
            if (PhoneDebug.VDBG) {
                uploadImmediately();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$6LjxuaV-kevqRsu2hZQrBr0AfPU, reason: not valid java name */
    public static /* synthetic */ boolean m3943$r8$lambda$6LjxuaVkevqRsu2hZQrBr0AfPU(String str, File file) {
        return file.getName().startsWith(str) && file.getName().endsWith(".hprof");
    }

    private void updateHeapMqsRules() {
        List rules = MQSEventManagerDelegate.getInstance().getRules("modemPower");
        logV("updateHeapMqsRules: " + rules);
        if (rules != null) {
            try {
                Iterator it = rules.iterator();
                while (it.hasNext()) {
                    JSONObject jSONObject = new JSONObject((String) it.next());
                    if (jSONObject.has("id") && jSONObject.has("module") && jSONObject.has("device") && "modemPower".equals(jSONObject.getString("module")) && Build.DEVICE.equals(jSONObject.getString("device"))) {
                        this.mRuleId = jSONObject.getString("id");
                        JSONArray jSONArray = jSONObject.getJSONArray("customized");
                        if (jSONArray != null) {
                            for (int i = 0; i < jSONArray.length(); i++) {
                                JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                                if ("threshold".equals(jSONObject2.getString("key"))) {
                                    this.mThreshold = jSONObject2.getInt(a.p);
                                }
                            }
                        } else {
                            continue;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImmediately() throws InterruptedException {
        Thread.sleep(1000L);
        Intent intent = new Intent("mqsas.intent.upload");
        intent.addFlags(268435456);
        intent.putExtra("test", true);
        this.mContext.sendBroadcast(intent);
    }

    private static void logV(String str) {
        if (PhoneDebug.VDBG) {
            Log.d("ModemLogUploadTools", str);
        }
    }
}
