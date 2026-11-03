package com.android.phone.utils;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.IActivityTaskManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.telephony.Rlog;
import android.text.TextUtils;
import androidx.emoji2.text.ConcurrencyHelpers$$ExternalSyntheticLambda0;
import com.android.internal.util.ArrayUtils;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.BroadcastEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.xiaomi.hyperradio.HyperRadioManager;
import com.xiaomi.internal.telephony.ExtPhoneConstants;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import miui.app.IFreeformCallback;
import miui.app.MiuiFreeFormManager;
import miui.os.HyperOSCustFeatureResolve;
import miui.process.ForegroundInfo;
import miui.telephony.PhoneDebug;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public class MiuiAppStateMonitor {
    private static volatile MiuiAppStateMonitor sInstance;
    private ForegroundInfo mCurrentForegroundInfo;
    private ForegroundAppInfo mForegroundAppInfo;
    private ExtPhoneConstants.ForegroundAppType mLastReportedAppType;
    private int mWindowingMode;
    private final Handler mWorkHandler;
    private final HandlerThread mWorkThread;
    private static final List GAME_APP_PACKAGES = HyperOSCustFeatureResolve.getStringArray("hyperos_game_app_packages");
    private static final List LIVE_VIDEO_APP_PACKAGES = HyperOSCustFeatureResolve.getStringArray("hyperos_live_video_app_packages");
    private static final List SOCIAL_APP_PACKAGES = HyperOSCustFeatureResolve.getStringArray("hyperos_social_app_packages");
    private static final List BROWSER_APP_PACKAGES = HyperOSCustFeatureResolve.getStringArray("hyperos_browser_app_packages");
    private static final List SMS_APP_PACKAGES = HyperOSCustFeatureResolve.getStringArray("hyperos_sms_app_packages");
    private final HashSet mAppStateObservers = new HashSet();
    private boolean mIsWechatVoipActive = false;
    private ArrayList mCurrentTopActivity = new ArrayList();
    private final BaseEvent.IEventListener mEventListener = new BaseEvent.IEventListener() { // from class: com.android.phone.utils.MiuiAppStateMonitor.1
        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            if (baseEvent instanceof DeviceStateEvent.AppStateEvent) {
                ForegroundInfo foregroundInfo = ((DeviceStateEvent.AppStateEvent) baseEvent).getForegroundInfo();
                if (foregroundInfo != null) {
                    Message messageObtainMessage = MiuiAppStateMonitor.this.mWorkHandler.obtainMessage(101, foregroundInfo);
                    if (!TextUtils.isEmpty(foregroundInfo.mForegroundPackageName)) {
                        MiuiAppStateMonitor.this.setForegroundAppInfo(new ForegroundAppInfo(foregroundInfo.mForegroundPackageName, foregroundInfo.isColdStart()));
                    }
                    messageObtainMessage.sendToTarget();
                    return;
                }
                return;
            }
            if (baseEvent instanceof BroadcastEvent) {
                boolean z = baseEvent.getBundle().getBoolean("wechatVoipEnabled", false);
                MiuiAppStateMonitor.log("WeChat VoIP scene changed: " + z);
                if (MiuiAppStateMonitor.this.mIsWechatVoipActive != z) {
                    MiuiAppStateMonitor.this.mIsWechatVoipActive = z;
                    MiuiAppStateMonitor.this.mWorkHandler.sendEmptyMessage(MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT);
                }
            }
        }
    };
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.utils.MiuiAppStateMonitor.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            MiuiAppStateMonitor.log("action:" + intent.getAction());
            MiuiAppStateMonitor.this.mWorkHandler.sendEmptyMessage(103);
        }
    };
    private final IFreeformCallback.Stub mIFreeformCallback = new IFreeformCallback.Stub() { // from class: com.android.phone.utils.MiuiAppStateMonitor.4
        public void dispatchFreeFormStackModeChanged(int i, MiuiFreeFormManager.MiuiFreeFormStackInfo miuiFreeFormStackInfo) {
            if ((MiuiAppStateMonitor.this.mWindowingMode & 2) == 0 || i == 3 || i == 5) {
                if ((MiuiAppStateMonitor.this.mWindowingMode & 2) == 0 && (i == 3 || i == 5)) {
                    return;
                }
                MiuiAppStateMonitor.this.mWorkHandler.obtainMessage(102, i, 0, miuiFreeFormStackInfo.packageName).sendToTarget();
            }
        }
    };
    private final Context mContext = PhoneGlobals.getInstance();

    public interface TopAppObserver {
        void onTopAppChanged(List list, int i);
    }

    final class MiuiAppStateMonitorHandler extends Handler {
        MiuiAppStateMonitorHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 101:
                    if (ActivityManager.isInSplitScreenWindowingMode()) {
                        MiuiAppStateMonitor.this.mWindowingMode |= 1;
                    }
                    MiuiAppStateMonitor.this.updateForegroundActivity();
                    MiuiAppStateMonitor.this.handleForegroundInfoChanged((ForegroundInfo) message.obj);
                    break;
                case 102:
                    MiuiAppStateMonitor.logV("action:" + message.arg1 + " freeform:" + ((String) message.obj));
                    MiuiAppStateMonitor.this.updateForegroundActivity();
                    break;
                case 103:
                    MiuiAppStateMonitor.this.updateForegroundActivity();
                    break;
                case MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT /* 104 */:
                    if (MiuiAppStateMonitor.this.mCurrentForegroundInfo != null) {
                        MiuiAppStateMonitor miuiAppStateMonitor = MiuiAppStateMonitor.this;
                        miuiAppStateMonitor.notifyForegroundAppTypeChanged(miuiAppStateMonitor.mCurrentForegroundInfo);
                    }
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleForegroundInfoChanged(ForegroundInfo foregroundInfo) {
        this.mCurrentForegroundInfo = foregroundInfo;
        notifyForegroundAppTypeChanged(foregroundInfo);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyForegroundAppTypeChanged(ForegroundInfo foregroundInfo) {
        String str = foregroundInfo.mForegroundPackageName;
        ExtPhoneConstants.ForegroundAppType foregroundAppTypeResolveForegroundAppType = resolveForegroundAppType(str);
        ExtPhoneConstants.ForegroundAppType foregroundAppType = this.mLastReportedAppType;
        if (foregroundAppType == null || !foregroundAppType.equals(foregroundAppTypeResolveForegroundAppType)) {
            this.mLastReportedAppType = foregroundAppTypeResolveForegroundAppType;
            log("foregroundApp: " + str + " mapptypeAppType: " + foregroundAppTypeResolveForegroundAppType + " wechatVoip: " + this.mIsWechatVoipActive);
            int phoneCount = TelephonyManager.getDefault().getPhoneCount();
            for (int i = 0; i < phoneCount; i++) {
                HyperRadioManager hyperRadioManager = HyperRadioManager.getInstance(this.mContext);
                Handler handler = this.mWorkHandler;
                Objects.requireNonNull(handler);
                hyperRadioManager.sendForegroundAppType(i, foregroundAppTypeResolveForegroundAppType, new ConcurrencyHelpers$$ExternalSyntheticLambda0(handler), new HyperRadioManager.GenericResponse(this) { // from class: com.android.phone.utils.MiuiAppStateMonitor.2
                    public void onResponse(int i2) {
                        MiuiAppStateMonitor.log("sendForegroundAppType with error code " + i2);
                    }
                });
            }
        }
    }

    private ExtPhoneConstants.ForegroundAppType resolveForegroundAppType(String str) {
        if (this.mIsWechatVoipActive && "com.tencent.mm".equals(str)) {
            return ExtPhoneConstants.ForegroundAppType.APP_TYPE_WECHAT_AUDIO_VIDEO;
        }
        return getForegroundAppType(str);
    }

    private static ExtPhoneConstants.ForegroundAppType getForegroundAppType(String str) {
        if (TextUtils.isEmpty(str)) {
            return ExtPhoneConstants.ForegroundAppType.APP_TYPE_OTHER;
        }
        if (ArrayUtils.contains(GAME_APP_PACKAGES, str)) {
            return ExtPhoneConstants.ForegroundAppType.APP_TYPE_GAME;
        }
        if (ArrayUtils.contains(LIVE_VIDEO_APP_PACKAGES, str)) {
            return ExtPhoneConstants.ForegroundAppType.APP_TYPE_LIVE;
        }
        if (ArrayUtils.contains(SOCIAL_APP_PACKAGES, str)) {
            return ExtPhoneConstants.ForegroundAppType.APP_TYPE_SOCIAL_COMM;
        }
        if (ArrayUtils.contains(BROWSER_APP_PACKAGES, str)) {
            return ExtPhoneConstants.ForegroundAppType.APP_TYPE_BROWSER;
        }
        if (ArrayUtils.contains(SMS_APP_PACKAGES, str)) {
            return ExtPhoneConstants.ForegroundAppType.APP_TYPE_SMS;
        }
        return ExtPhoneConstants.ForegroundAppType.APP_TYPE_OTHER;
    }

    private MiuiAppStateMonitor() {
        HandlerThread handlerThread = new HandlerThread("MiuiAppStateMonitor", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mWorkHandler = new MiuiAppStateMonitorHandler(handlerThread.getLooper());
    }

    public static MiuiAppStateMonitor init() {
        if (sInstance == null) {
            synchronized (MiuiAppStateMonitor.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new MiuiAppStateMonitor();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Rlog.d("MiuiAppStateMonitor", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateForegroundActivity() {
        String[] strArr;
        String[] strArr2;
        try {
            ArrayList arrayList = this.mCurrentTopActivity;
            int i = this.mWindowingMode;
            IActivityTaskManager service = ActivityTaskManager.getService();
            int i2 = 0;
            ActivityTaskManager.RootTaskInfo rootTaskInfoOnDisplay = service.getRootTaskInfoOnDisplay(1, 0, 0);
            ActivityTaskManager.RootTaskInfo rootTaskInfoOnDisplay2 = service.getRootTaskInfoOnDisplay(5, 0, 0);
            this.mCurrentTopActivity = new ArrayList();
            if (rootTaskInfoOnDisplay != null && (strArr2 = rootTaskInfoOnDisplay.childTaskNames) != null) {
                for (String str : strArr2) {
                    logV("base task:" + str);
                    this.mCurrentTopActivity.add(ComponentName.unflattenFromString(str));
                }
                int size = this.mCurrentTopActivity.size();
                int i3 = this.mWindowingMode;
                if (size > 1) {
                    this.mWindowingMode = i3 | 1;
                } else {
                    this.mWindowingMode = i3 & (-2);
                }
            }
            if (rootTaskInfoOnDisplay2 != null && (strArr = rootTaskInfoOnDisplay2.childTaskNames) != null) {
                int length = strArr.length;
                int i4 = 0;
                while (i2 < length) {
                    String str2 = strArr[i2];
                    logV("floating task:" + str2);
                    this.mCurrentTopActivity.add(ComponentName.unflattenFromString(str2));
                    i2++;
                    i4 = 1;
                }
                i2 = i4;
            }
            int i5 = this.mWindowingMode;
            if (i2 != 0) {
                this.mWindowingMode = i5 | 2;
            } else {
                this.mWindowingMode = i5 & (-3);
            }
            if (this.mCurrentTopActivity.equals(arrayList) && this.mWindowingMode == i) {
                return;
            }
            notifyTopAppChanged(this.mCurrentTopActivity, this.mWindowingMode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void registerAppObserver(TopAppObserver topAppObserver) {
        try {
            if (this.mAppStateObservers.size() < 1) {
                EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "foreground_changed", this.mEventListener);
                EventHubManager.getInstance().registerListener("broadcast", "wechat_voip_scene", this.mEventListener, this.mWorkHandler);
                if (MiuiFreeFormManager.getMiuiFreeformVersion() == 3) {
                    MiuiFreeFormManager.registerFreeformCallback(this.mIFreeformCallback);
                }
                this.mContext.registerReceiver(this.mReceiver, new IntentFilter("android.intent.action.USER_PRESENT"), 2);
            }
            this.mAppStateObservers.add(topAppObserver);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void unregisterAppObserver(TopAppObserver topAppObserver) {
        try {
            this.mAppStateObservers.remove(topAppObserver);
            if (this.mAppStateObservers.size() < 1) {
                EventHubManager.getInstance().unregisterListener(this.mEventListener);
                if (MiuiFreeFormManager.getMiuiFreeformVersion() == 3) {
                    MiuiFreeFormManager.unregisterFreeformCallback(this.mIFreeformCallback);
                }
                this.mContext.unregisterReceiver(this.mReceiver);
                this.mLastReportedAppType = null;
                this.mCurrentForegroundInfo = null;
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void notifyTopAppChanged(ArrayList arrayList, int i) {
        logV("TopAppChanged " + arrayList + " mode:" + i);
        if (arrayList.size() < 1) {
            return;
        }
        Iterator it = this.mAppStateObservers.iterator();
        while (it.hasNext()) {
            ((TopAppObserver) it.next()).onTopAppChanged(arrayList, i);
        }
    }

    public abstract class AppScene implements TopAppObserver {
        private final Handler mInnerHandler;
        private ConcurrentHashMap mLastNonAppScene;
        private String mLastScene;
        private static final List VIDEO_APPS = HyperOSCustFeatureResolve.getStringArray("config_video_app_packages");
        private static final List SHOPPING_APPS = HyperOSCustFeatureResolve.getStringArray("config_shopping_app_packages");
        private static final List SOCIAL_APPS = HyperOSCustFeatureResolve.getStringArray("config_social_app_packages");

        public abstract void sceneEnter(String str);

        public abstract void sceneExit(String str);

        public AppScene(Handler handler) {
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            this.mLastNonAppScene = concurrentHashMap;
            Boolean bool = Boolean.FALSE;
            concurrentHashMap.put("game", bool);
            this.mLastNonAppScene.put("wechat", bool);
            this.mLastNonAppScene.put("wifi", bool);
            MiuiAppStateMonitor.init().registerAppObserver(this);
            this.mInnerHandler = handler;
            PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
            EventHubManager.getInstance().registerListener("broadcast", "wechat_voip_scene", new BaseEvent.IEventListener() { // from class: com.android.phone.utils.MiuiAppStateMonitor$AppScene$$ExternalSyntheticLambda1
                @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
                public final void onEvent(BaseEvent baseEvent) {
                    this.f$0.lambda$new$0(baseEvent);
                }
            }, handler);
            IntentFilter intentFilter = new IntentFilter("com.android.phone.intent.action.CHANGE_LEVEL");
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            phoneGlobals.registerReceiver(new BroadcastReceiver() { // from class: com.android.phone.utils.MiuiAppStateMonitor.AppScene.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    if ("com.android.phone.intent.action.CHANGE_LEVEL".equals(intent.getAction())) {
                        AppScene.this.onNonAppSceneChagned("game", (intent.getLongExtra("Level_DL", 1L) == 1 && intent.getLongExtra("Level_UL", 1L) == 1) ? false : true);
                    } else if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                        AppScene.this.onNonAppSceneChagned("wifi", MiuiPhoneUtils.isWifiConnected());
                    }
                }
            }, intentFilter, null, handler, 2);
            Uri uriFor = Settings.Secure.getUriFor("gb_boosting");
            final ContentResolver contentResolver = phoneGlobals.getContentResolver();
            contentResolver.registerContentObserver(uriFor, false, new ContentObserver(handler) { // from class: com.android.phone.utils.MiuiAppStateMonitor.AppScene.2
                @Override // android.database.ContentObserver
                public void onChange(boolean z) {
                    AppScene.this.onNonAppSceneChagned("game", Settings.Secure.getInt(contentResolver, "gb_boosting", 0) == 1);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(BaseEvent baseEvent) {
            if (baseEvent instanceof BroadcastEvent) {
                onNonAppSceneChagned("wechat", baseEvent.getBundle().getBoolean("wechatVoipEnabled", false));
            }
        }

        public static String getSceneFromPkg(String str) {
            if (ArrayUtils.contains(SOCIAL_APPS, str)) {
                return "social";
            }
            if (ArrayUtils.contains(VIDEO_APPS, str)) {
                return "streaming";
            }
            if (ArrayUtils.contains(SHOPPING_APPS, str)) {
                return "shopping";
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onNonAppSceneChagned(final String str, final boolean z) {
            this.mInnerHandler.removeMessages(10008);
            this.mInnerHandler.postDelayed(new Runnable() { // from class: com.android.phone.utils.MiuiAppStateMonitor$AppScene$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onNonAppSceneChagned$1(str, z);
                }
            }, 10008, MirrorDesktopHelper.TIMEOUT_MILLIS);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNonAppSceneChagned$1(String str, boolean z) {
            if (((Boolean) this.mLastNonAppScene.get(str)).booleanValue() != z) {
                if (!z) {
                    sceneExit(str);
                } else {
                    sceneEnter(str);
                }
                this.mLastNonAppScene.put(str, Boolean.valueOf(z));
            }
        }

        @Override // com.android.phone.utils.MiuiAppStateMonitor.TopAppObserver
        public void onTopAppChanged(List list, int i) {
            this.mInnerHandler.removeMessages(10007);
            final String sceneFromPkg = i == 0 ? getSceneFromPkg(((ComponentName) list.get(0)).getPackageName()) : null;
            this.mInnerHandler.postDelayed(new Runnable() { // from class: com.android.phone.utils.MiuiAppStateMonitor$AppScene$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onTopAppChanged$2(sceneFromPkg);
                }
            }, 10007, MirrorDesktopHelper.TIMEOUT_MILLIS);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTopAppChanged$2(String str) {
            if (TextUtils.equals(this.mLastScene, str)) {
                return;
            }
            if (!TextUtils.isEmpty(this.mLastScene)) {
                sceneExit(this.mLastScene);
            }
            if (!TextUtils.isEmpty(str)) {
                sceneEnter(str);
            }
            this.mLastScene = str;
        }
    }

    public int getCurrentWindowingMode() {
        return this.mWindowingMode;
    }

    public ArrayList getCurrentTopActivities() {
        return this.mCurrentTopActivity;
    }

    public class ForegroundAppInfo {
        public boolean mIsCold;
        public String mPackageName;

        public ForegroundAppInfo(String str, boolean z) {
            this.mPackageName = str;
            this.mIsCold = z;
        }

        public String toString() {
            return "ForegroundAppInfo{mPackageName='" + this.mPackageName + "', mIsCold=" + this.mIsCold + '}';
        }
    }

    public ForegroundAppInfo getForegroundAppInfo() {
        return this.mForegroundAppInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setForegroundAppInfo(ForegroundAppInfo foregroundAppInfo) {
        this.mForegroundAppInfo = foregroundAppInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logV(String str) {
        if (PhoneDebug.VDBG) {
            Rlog.d("MiuiAppStateMonitor", str);
        }
    }
}
