package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.util.Log;
import com.android.internal.telephony.LocalLog;
import com.android.phone.statistics.FileUploadStats;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import miui.process.IActivityChangeListener;
import miui.process.ProcessManager;

/* JADX INFO: loaded from: classes.dex */
public class FileUploadMonitor {
    private static volatile FileUploadMonitor instance;
    private static final Map sLocalLogs = new HashMap();
    private boolean mIsMobileDataOn;
    private boolean mIsWifiDataOn;
    private final WorkHandler mWorkHandler;
    private final HandlerThread mWorkThread;
    private AtomicLong mFirstStepTime = new AtomicLong(0);
    private volatile boolean mHasPassedFileSelectorStep = false;
    private volatile boolean mIsFileUploading = false;
    private boolean mFileUploadCloudSwitch = true;
    private final List mDefaultPackageWhiteList = new ArrayList();
    private final List mDefaultActivityWhiteList = new ArrayList();
    private IActivityChangeListener.Stub mActivityChangeListener = new IActivityChangeListener.Stub() { // from class: com.android.phone.FileUploadMonitor.1
        public void onActivityChanged(ComponentName componentName, ComponentName componentName2) {
            if (componentName == null || componentName2 == null) {
                return;
            }
            FileUploadMonitor.this.handleActivityChanged(componentName, componentName2);
        }
    };
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.FileUploadMonitor.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            FileUploadMonitor.this.logd("receive action: ".concat(action));
            action.hashCode();
            if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                FileUploadMonitor.this.handleConnectivityChanged(intent);
            } else if (action.equals("com.android.phone.intent.action.CLOUD_FILE_UPLOAD_SWITCH")) {
                FileUploadMonitor.this.handleCloudStateChanged(intent);
            }
        }
    };
    private final Context mContext = PhoneGlobals.getInstance();

    private FileUploadMonitor() {
        HandlerThread handlerThread = new HandlerThread("FileUploadMonitor", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mWorkHandler = new WorkHandler(handlerThread.getLooper());
        initWechatWhiteList();
        updateCloudController();
        registerAllInfo();
        logd("Instance initialized");
    }

    public static synchronized FileUploadMonitor init() {
        try {
            if (instance == null) {
                synchronized (FileUploadMonitor.class) {
                    try {
                        if (instance == null) {
                            instance = new FileUploadMonitor();
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        } catch (Throwable th2) {
            throw th2;
        }
        return instance;
    }

    public static FileUploadMonitor getInstance() {
        return instance;
    }

    private void registerAllInfo() {
        registerReceiver();
        registerActivityChangeListener();
    }

    private void unRegisterAllInfo() {
        unRegisterReceiver();
        unRegisterActivityChangeListener();
    }

    private void registerReceiver() {
        logd("registerReceiver");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("com.android.phone.intent.action.CLOUD_FILE_UPLOAD_SWITCH");
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    private void unRegisterReceiver() {
        logd("unRegisterReceiver");
        BroadcastReceiver broadcastReceiver = this.mIntentReceiver;
        if (broadcastReceiver != null) {
            try {
                this.mContext.unregisterReceiver(broadcastReceiver);
            } catch (IllegalArgumentException e) {
                Log.e("FileUploadMonitor", "unRegisterReceiver failed", e);
            }
        }
    }

    public void registerActivityChangeListener() {
        logd("registerActivityChangeListener");
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        arrayList.addAll(this.mDefaultPackageWhiteList);
        arrayList2.addAll(this.mDefaultActivityWhiteList);
        try {
            ProcessManager.registerActivityChangeListener(arrayList, arrayList2, this.mActivityChangeListener);
        } catch (Exception e) {
            Log.e("FileUploadMonitor", "registerActivityChangeListener failed", e);
        }
    }

    private void unRegisterActivityChangeListener() {
        logd("unRegisterActivityChangeListener");
        try {
            IActivityChangeListener.Stub stub = this.mActivityChangeListener;
            if (stub != null) {
                ProcessManager.unregisterActivityChanageListener(stub);
            }
        } catch (Exception e) {
            Log.e("FileUploadMonitor", "registerActivityChangeListener failed", e);
        }
    }

    private boolean updateCloudController() {
        boolean fileUploadSwitchByCloud = FiveGManagerBase.getFileUploadSwitchByCloud(this.mContext);
        if (this.mFileUploadCloudSwitch == fileUploadSwitchByCloud) {
            logd("updateCloudController invalid same switch: " + this.mFileUploadCloudSwitch);
            return false;
        }
        this.mFileUploadCloudSwitch = fileUploadSwitchByCloud;
        logd("updateCloudController switch = " + this.mFileUploadCloudSwitch);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handleCloudStateChange() {
        try {
            if (updateCloudController()) {
                if (this.mFileUploadCloudSwitch) {
                    registerAllInfo();
                } else {
                    unRegisterAllInfo();
                    if (this.mIsFileUploading) {
                        localLog("FILE_SEND_SCENE", "Update cloud, cloud state: " + this.mFileUploadCloudSwitch);
                        try {
                            sendFileUploadSceneBroadcast(0);
                        } catch (Exception e) {
                            Log.e("FileUploadMonitor", "Failed to send upload end broadcast", e);
                        }
                        this.mIsFileUploading = false;
                    }
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleActivityChanged(ComponentName componentName, ComponentName componentName2) {
        logd("onActivityChanged cur= " + componentName2.toString() + " pre= " + componentName.toString());
        String packageName = componentName.getPackageName();
        String packageName2 = componentName2.getPackageName();
        if (this.mDefaultPackageWhiteList.contains(packageName) && this.mDefaultPackageWhiteList.contains(packageName2)) {
            String className = componentName.getClassName();
            String className2 = componentName2.getClassName();
            if (this.mDefaultActivityWhiteList.size() < 4) {
                logd("mDefaultActivityWhiteList size < 3, skip activity check");
                return;
            }
            String str = (String) this.mDefaultActivityWhiteList.get(0);
            String str2 = (String) this.mDefaultActivityWhiteList.get(1);
            String str3 = (String) this.mDefaultActivityWhiteList.get(2);
            String str4 = (String) this.mDefaultActivityWhiteList.get(3);
            if (checkFirstStep(className, className2, str, str2) || checkSecondStep(className, className2, str2, str3, str4)) {
                return;
            }
            checkOtherCases(className, className2, str2);
        }
    }

    private boolean checkFirstStep(String str, String str2, String str3, String str4) {
        if (!str3.equals(str) || !str4.equals(str2)) {
            return false;
        }
        logd("Detect first step: " + str3 + " -> " + str4);
        this.mFirstStepTime.set(System.currentTimeMillis());
        this.mHasPassedFileSelectorStep = true;
        this.mWorkHandler.removeMessages(1002);
        this.mWorkHandler.sendEmptyMessageDelayed(1002, 500L);
        return true;
    }

    private boolean checkSecondStep(String str, String str2, String str3, String str4, String str5) {
        if (!this.mHasPassedFileSelectorStep || !str3.equals(str)) {
            return false;
        }
        if (!str4.equals(str2) && !str5.equals(str2)) {
            return false;
        }
        logd("Detect second step: " + str3 + " -> " + str2);
        handleSecondStepLogic();
        resetSceneState();
        this.mWorkHandler.removeMessages(1002);
        return true;
    }

    private void handleSecondStepLogic() {
        long jCurrentTimeMillis = System.currentTimeMillis() - this.mFirstStepTime.get();
        if (jCurrentTimeMillis <= 500) {
            localLog("FILE_SEND_SCENE", "Success detected, time cost: " + jCurrentTimeMillis + "ms");
            this.mIsFileUploading = true;
            sendFileUploadSceneBroadcast(1);
            startMonitorTimer();
            return;
        }
        localLog("FILE_SEND_SCENE", "Timeout, time cost: " + jCurrentTimeMillis + "ms");
    }

    private void startMonitorTimer() {
        stopMonitorTimer();
        logd("startMonitorTimer");
        this.mWorkHandler.sendEmptyMessageDelayed(1004, 300000L);
    }

    private void stopMonitorTimer() {
        if (this.mWorkHandler.hasMessages(1004)) {
            logd("stopMonitorTimer");
            this.mWorkHandler.removeMessages(1004);
        }
    }

    private void checkOtherCases(String str, String str2, String str3) {
        if (!str3.equals(str) || !str3.equals(str2)) {
            resetSceneState();
        }
        if (this.mWorkHandler.hasMessages(1002)) {
            this.mWorkHandler.removeMessages(1002);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendFileUploadSceneBroadcast(int i) {
        try {
            if (i == 1) {
                FileUploadStats.getInstance().recordTriggerTimes("mTotalFileUploadCount");
            } else if (i == 0) {
                FileUploadStats.getInstance().recordTriggerTimes("mTotalFileUploadExitCount");
            }
            Intent intent = new Intent("com.android.phone.intent.action.FILE_UPLOAD_SCENE");
            intent.putExtra("extra_file_upload_scene_type", i);
            intent.putExtra("extra_is_wifi_connected", this.mIsWifiDataOn);
            intent.putExtra("extra_is_mobile_connected", this.mIsMobileDataOn);
            this.mContext.sendBroadcastAsUser(intent, UserHandle.CURRENT);
            localLog("sendFileUploadSceneBroadcast", "Broadcast sceneType: " + i);
        } catch (Exception e) {
            Log.e("FileUploadMonitor", "Failed to send file upload scene broadcast", e);
        }
    }

    public void handleSpeedRateUpdate(long j, long j2) {
        if (this.mIsFileUploading) {
            logd("handleSpeedRateUpdate, ulRateBps: " + j);
            if (j < 100) {
                localLog("FILE_SEND_SCENE", "Upload end detected, ulRateBps: " + j);
                try {
                    sendFileUploadSceneBroadcast(0);
                } catch (Exception e) {
                    Log.e("FileUploadMonitor", "Failed to send upload end broadcast", e);
                }
                this.mIsFileUploading = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetSceneState() {
        this.mFirstStepTime.set(0L);
        this.mHasPassedFileSelectorStep = false;
        logd("Reset file send scene detect state");
    }

    private void initWechatWhiteList() {
        try {
            String[] stringArray = this.mContext.getResources().getStringArray(R.array.wechat_package_list_default);
            if (stringArray != null && stringArray.length > 0) {
                this.mDefaultPackageWhiteList.clear();
                this.mDefaultPackageWhiteList.addAll(Arrays.asList(stringArray));
                logd("Loaded wechat package list: " + Arrays.toString(stringArray));
            } else {
                logd("Wechat package list is empty");
            }
            String[] stringArray2 = this.mContext.getResources().getStringArray(R.array.wechat_activity_list_default);
            if (stringArray2 != null && stringArray2.length > 0) {
                this.mDefaultActivityWhiteList.clear();
                this.mDefaultActivityWhiteList.addAll(Arrays.asList(stringArray2));
                logd("Loaded wechat activity list: " + Arrays.toString(stringArray2));
                return;
            }
            logd("Wechat activity list is empty");
        } catch (Exception e) {
            Log.e("FileUploadMonitor", "Failed to load wechat white list", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleConnectivityChanged(Intent intent) {
        this.mWorkHandler.obtainMessage(1001, intent).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCloudStateChanged(Intent intent) {
        this.mWorkHandler.obtainMessage(1003).sendToTarget();
    }

    class WorkHandler extends Handler {
        WorkHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            try {
                switch (message.what) {
                    case 1001:
                        FileUploadMonitor.this.handleConnectivityChange((Intent) message.obj);
                        return;
                    case 1002:
                        FileUploadMonitor.this.logd("File send scene detect timeout, reset state");
                        FileUploadMonitor.this.resetSceneState();
                        return;
                    case 1003:
                        FileUploadMonitor.this.handleCloudStateChange();
                        return;
                    case 1004:
                        if (FileUploadMonitor.this.mIsFileUploading) {
                            try {
                                FileUploadMonitor.this.sendFileUploadSceneBroadcast(0);
                                break;
                            } catch (Exception e) {
                                Log.e("FileUploadMonitor", "Failed to send upload end broadcast", e);
                            }
                            FileUploadMonitor.this.mIsFileUploading = false;
                            return;
                        }
                        return;
                    default:
                        FileUploadMonitor.this.logd("Unhandled message: " + message.what);
                        return;
                }
            } catch (Exception e2) {
                Log.e("FileUploadMonitor", "Error handling message: " + message.what, e2);
            }
            Log.e("FileUploadMonitor", "Error handling message: " + message.what, e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleConnectivityChange(Intent intent) {
        NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
        if (networkInfo == null) {
            return;
        }
        if (networkInfo.getType() == 0) {
            handleMobileNetworkChange(networkInfo);
        } else if (networkInfo.getType() == 1) {
            handleWifiNetworkChange(networkInfo);
        }
    }

    private void handleMobileNetworkChange(NetworkInfo networkInfo) {
        boolean z = networkInfo.getState() == NetworkInfo.State.CONNECTED;
        if (z != this.mIsMobileDataOn) {
            this.mIsMobileDataOn = z;
            logd("Mobile data state changed: ".concat(z ? "CONNECTED" : "DISCONNECTED"));
        }
    }

    private void handleWifiNetworkChange(NetworkInfo networkInfo) {
        boolean z = networkInfo.getState() == NetworkInfo.State.CONNECTED;
        if (z != this.mIsWifiDataOn) {
            this.mIsWifiDataOn = z;
            logd("WiFi state changed: ".concat(z ? "CONNECTED" : "DISCONNECTED"));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String str) {
        Log.d("FileUploadMonitor", str);
    }

    private static void localLog(String str, String str2) {
        localLog("FileUploadMonitor", str, str2);
    }

    private static void localLog(String str, String str2, String str3) {
        String str4 = str + "." + str2;
        Map map = sLocalLogs;
        synchronized (map) {
            try {
                if (!map.containsKey(str4)) {
                    map.put(str4, new LocalLog(100));
                }
                ((LocalLog) map.get(str4)).log(str3);
            } catch (Throwable th) {
                throw th;
            }
        }
        Log.d(str, str2 + " " + str3);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("FileUploadMonitor dump start------");
        Map map = sLocalLogs;
        synchronized (map) {
            try {
                for (Map.Entry entry : map.entrySet()) {
                    printWriter.println((String) entry.getKey());
                    ((LocalLog) entry.getValue()).dump(null, printWriter, null);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        printWriter.println("FileUploadMonitor dump end------");
        printWriter.flush();
    }
}
