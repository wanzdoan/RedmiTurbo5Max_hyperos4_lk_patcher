package com.android.phone.firewall.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import com.android.phone.PhoneGlobals;
import com.android.phone.firewall.utils.ThreadPool;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.ot.pubsub.g.f;
import com.xiaomi.internal.telephony.DynamicSarService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class AppMonitor {
    private static Context mContext;
    private static HashMap mGroupHeadUidMap;
    private static volatile boolean sGroupHeadUidMapInit;
    private static AppMonitor sInstance;
    private ArrayMap mFilteredAppInfosMap;
    private AppMonitorHandler mHandler;
    private ArrayMap mNetworkAccessedAppsMap;
    private ArrayMap mNonSystemAppMap;
    private PackageManager mPkgManager;
    private ArrayMap mSystemAppMap;
    private HandlerThread mWorkThread;
    private boolean mIsInited = false;
    private Object mAppMapLock = new Object();
    private Object mFillAppLock = new Object();
    private BroadcastReceiver mPackageReceiver = new BroadcastReceiver() { // from class: com.android.phone.firewall.model.AppMonitor.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int intExtra = intent.getIntExtra("android.intent.extra.user_handle", -10000);
            AppMonitor.this.log("mPackageReceiver userId=" + intExtra);
            if (intExtra != 0) {
                return;
            }
            AppMonitor.this.mHandler.obtainMessage(2, intent).sendToTarget();
        }
    };

    static {
        HashMap map = new HashMap();
        mGroupHeadUidMap = map;
        map.put("android", -1);
        mGroupHeadUidMap.put("com.android.providers.telephony", -1);
        mGroupHeadUidMap.put("com.android.contacts", -1);
        mGroupHeadUidMap.put("com.google.android.gsf", -1);
        mGroupHeadUidMap.put("com.android.providers.downloads.ui", -1);
    }

    public static synchronized AppMonitor getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new AppMonitor();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    private AppMonitor() {
        log("AppMonitor Init");
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        mContext = phoneGlobals;
        this.mPkgManager = phoneGlobals.getPackageManager();
        Intent intent = new Intent();
        intent.setAction("com.xiaomi.phone.service.FirewallService");
        intent.setPackage(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME);
        mContext.startService(intent);
        HandlerThread handlerThread = new HandlerThread("PhoneMiuiAppMonitor", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        AppMonitorHandler appMonitorHandler = new AppMonitorHandler(this.mWorkThread.getLooper());
        this.mHandler = appMonitorHandler;
        appMonitorHandler.sendEmptyMessage(1);
    }

    class AppMonitorHandler extends Handler {
        AppMonitorHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                AppMonitor.this.init();
            } else {
                if (i != 2) {
                    return;
                }
                AppMonitor.this.processPackageChanged((Intent) message.obj);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void init() {
        registerPackageReceiver();
        this.mNetworkAccessedAppsMap = new ArrayMap();
        this.mSystemAppMap = new ArrayMap();
        this.mNonSystemAppMap = new ArrayMap();
        this.mFilteredAppInfosMap = new ArrayMap();
        initData();
    }

    void initData() {
        Log.i("PhoneMiuiAppMonitor", "init app list");
        ThreadPool.execute(new Runnable() { // from class: com.android.phone.firewall.model.AppMonitor.1
            @Override // java.lang.Runnable
            public void run() {
                synchronized (AppMonitor.this.mFillAppLock) {
                    try {
                        AppMonitor.this.fillNetworkAccessedApps();
                        int i = 0;
                        while (!AppMonitor.this.mIsInited) {
                            int i2 = i + 1;
                            if (i >= 5) {
                                break;
                            }
                            Log.i("PhoneMiuiAppMonitor", "fillNetworkAccessedApps failed, retryCount:" + i2);
                            AppMonitor.this.fillNetworkAccessedApps();
                            i = i2;
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fillNetworkAccessedApps() {
        new ArrayMap();
        ArrayMap arrayMap = new ArrayMap();
        new ArrayMap();
        ArrayMap arrayMap2 = new ArrayMap();
        ArrayMap arrayMap3 = new ArrayMap();
        HashSet hashSet = new HashSet();
        List<ApplicationInfo> installedApplications = this.mPkgManager.getInstalledApplications(DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
        if (installedApplications == null) {
            log("appList is null");
            return;
        }
        log("fillNetworkAccessedApps appList size=" + installedApplications.size());
        for (ApplicationInfo applicationInfo : installedApplications) {
            if (getAppEnable(applicationInfo.packageName) && hasInternetPermission(applicationInfo.packageName)) {
                String str = applicationInfo.packageName;
                AppInfo appInfoBuildAppInfo = buildAppInfo(str, applicationInfo.uid, isSystemApp(str));
                boolean z = appInfoBuildAppInfo.isSystemApp;
                int i = applicationInfo.uid;
                if (z) {
                    if (arrayMap3.containsKey(Integer.valueOf(i))) {
                        hashSet.add(Integer.valueOf(applicationInfo.uid));
                        if (isGroupHead(applicationInfo.packageName, mContext)) {
                            AppInfo appInfo = (AppInfo) arrayMap3.get(Integer.valueOf(applicationInfo.uid));
                            arrayMap.remove(appInfo.packageName);
                            arrayMap2.remove(appInfo.packageName);
                            arrayMap.put(appInfoBuildAppInfo.packageName.toString(), appInfoBuildAppInfo);
                            arrayMap2.put(appInfoBuildAppInfo.packageName.toString(), appInfoBuildAppInfo);
                            arrayMap3.put(Integer.valueOf(applicationInfo.uid), appInfoBuildAppInfo);
                        }
                    } else {
                        arrayMap.put(appInfoBuildAppInfo.packageName.toString(), appInfoBuildAppInfo);
                        arrayMap2.put(appInfoBuildAppInfo.packageName.toString(), appInfoBuildAppInfo);
                        arrayMap3.put(Integer.valueOf(applicationInfo.uid), appInfoBuildAppInfo);
                    }
                } else if (!arrayMap3.containsKey(Integer.valueOf(i))) {
                    arrayMap2.put(appInfoBuildAppInfo.packageName.toString(), appInfoBuildAppInfo);
                    arrayMap3.put(Integer.valueOf(applicationInfo.uid), appInfoBuildAppInfo);
                } else {
                    hashSet.add(Integer.valueOf(applicationInfo.uid));
                }
            }
        }
        arrayMap3.clear();
        hashSet.clear();
        synchronized (this.mAppMapLock) {
            this.mFilteredAppInfosMap = arrayMap2;
            log("fillNetworkAccessedApps mFilteredAppInfosMap=" + this.mFilteredAppInfosMap);
            this.mIsInited = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processPackageChanged(Intent intent) {
        Uri data = intent.getData();
        String encodedSchemeSpecificPart = data != null ? data.getEncodedSchemeSpecificPart() : null;
        int intExtra = intent.getIntExtra("android.intent.extra.UID", 0);
        log("processPackageChanged packageName=" + encodedSchemeSpecificPart + " uid=" + intExtra);
        if (TextUtils.isEmpty(encodedSchemeSpecificPart) || intExtra == 0) {
            return;
        }
        String action = intent.getAction();
        log("processPackageChanged action=" + action);
        if ("android.intent.action.PACKAGE_ADDED".equals(action)) {
            boolean zIsSystemApp = isSystemApp(encodedSchemeSpecificPart);
            log("processPackageChanged isSystemApp=" + zIsSystemApp);
            if (!(zIsSystemApp && isGroupUid(intExtra, mContext) && !isGroupHead(encodedSchemeSpecificPart, mContext)) && hasInternetPermission(encodedSchemeSpecificPart)) {
                AppInfo appInfoBuildAppInfo = buildAppInfo(encodedSchemeSpecificPart, intExtra, zIsSystemApp);
                synchronized (this.mAppMapLock) {
                    this.mFilteredAppInfosMap.put(appInfoBuildAppInfo.packageName.toString(), appInfoBuildAppInfo);
                }
                return;
            }
            return;
        }
        if ("android.intent.action.PACKAGE_REMOVED".equals(action)) {
            synchronized (this.mAppMapLock) {
                this.mFilteredAppInfosMap.remove(encodedSchemeSpecificPart);
            }
        }
    }

    private void registerPackageReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addDataScheme(f.a.e);
        mContext.registerReceiverAsUser(this.mPackageReceiver, UserHandle.ALL, intentFilter, "android.permission.INTERACT_ACROSS_USERS_FULL", null);
    }

    private AppInfo buildAppInfo(String str, int i, boolean z) {
        AppInfo appInfo = new AppInfo();
        appInfo.uid = i;
        appInfo.packageName = str;
        appInfo.isSystemApp = z;
        return appInfo;
    }

    private boolean getAppEnable(String str) {
        int applicationEnabledSetting;
        try {
            applicationEnabledSetting = this.mPkgManager.getApplicationEnabledSetting(str);
        } catch (Exception e) {
            log("getApplicationEnabledSetting error: " + e);
            applicationEnabledSetting = 0;
        }
        return applicationEnabledSetting == 0 || applicationEnabledSetting == 1;
    }

    private boolean isSystemApp(String str) {
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = this.mPkgManager.getApplicationInfo(str, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            applicationInfo = null;
        }
        if (applicationInfo != null) {
            return (applicationInfo.flags & 1) > 0 || UserHandle.getAppId(applicationInfo.uid) < 10000;
        }
        return false;
    }

    private boolean hasInternetPermission(String str) {
        return this.mPkgManager.checkPermission("android.permission.INTERNET", str) == 0;
    }

    public static void initGroupMap(Context context) {
        initGroupMap(context, mGroupHeadUidMap);
        sGroupHeadUidMapInit = true;
    }

    private static void initGroupMap(Context context, Map map) {
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            String str = (String) ((Map.Entry) it.next()).getKey();
            if (((Integer) map.get(str)).intValue() == -1) {
                map.put(str, Integer.valueOf(UserHandle.getAppId(getUidByPackageName(context, str))));
            }
        }
    }

    public static boolean isGroupHead(String str, Context context) {
        if (!sGroupHeadUidMapInit) {
            initGroupMap(context);
        }
        return mGroupHeadUidMap.containsKey(str);
    }

    public static boolean isGroupUid(int i, Context context) {
        if (!sGroupHeadUidMapInit) {
            initGroupMap(context);
        }
        return mGroupHeadUidMap.containsValue(Integer.valueOf(UserHandle.getAppId(i)));
    }

    public static int getUidByPackageName(Context context, String str) {
        try {
            return context.getPackageManager().getApplicationInfo(str, 0).uid;
        } catch (PackageManager.NameNotFoundException unused) {
            Log.i("PhoneMiuiAppMonitor", "not find packageName :" + str);
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("PhoneMiuiAppMonitor", str);
    }
}
