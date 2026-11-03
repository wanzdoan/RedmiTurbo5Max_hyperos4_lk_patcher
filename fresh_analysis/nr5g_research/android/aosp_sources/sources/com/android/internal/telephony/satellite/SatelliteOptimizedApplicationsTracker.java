package com.android.internal.telephony.satellite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.content.pm.UserInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.PackageChangeReceiver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteOptimizedApplicationsTracker {
    private final BroadcastReceiver mBootCompleted;
    private final Context mContext;
    private volatile Handler mCurrentHandler;
    private PackageManager mPackageManager;
    private final ConcurrentHashMap mSatelliteApplications = new ConcurrentHashMap();
    private final UserManager mUserManager;

    private int getTrackerPackageFlags() {
        return 644;
    }

    public SatelliteOptimizedApplicationsTracker(Looper looper, Context context) {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.satellite.SatelliteOptimizedApplicationsTracker.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                SatelliteOptimizedApplicationsTracker.this.logd("new user added");
                SatelliteOptimizedApplicationsTracker.this.mCurrentHandler.obtainMessage(1, null).sendToTarget();
            }
        };
        this.mBootCompleted = broadcastReceiver;
        this.mContext = context;
        this.mPackageManager = context.getPackageManager();
        this.mUserManager = (UserManager) context.getSystemService("user");
        this.mCurrentHandler = new Handler(looper) { // from class: com.android.internal.telephony.satellite.SatelliteOptimizedApplicationsTracker.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i = message.what;
                if (i == 0 || i == 1) {
                    SatelliteOptimizedApplicationsTracker.this.log("HandleInitializeTracker() STARTED");
                    SatelliteOptimizedApplicationsTracker.this.handleInitializeTracker();
                } else if (i == 2 || i == 3 || i == 4) {
                    SatelliteOptimizedApplicationsTracker.this.handlePackageMonitor((String) message.obj);
                } else {
                    if (i != 5) {
                        return;
                    }
                    SatelliteOptimizedApplicationsTracker.this.handlePackageRemoved((String) message.obj);
                }
            }
        };
        this.mCurrentHandler.sendMessage(this.mCurrentHandler.obtainMessage(0));
        UserHandle userHandle = UserHandle.ALL;
        context.createContextAsUser(userHandle, 128).registerReceiver(broadcastReceiver, new IntentFilter("android.intent.action.USER_ADDED"));
        new SatelliteApplicationPackageMonitor().register(context, this.mCurrentHandler.getLooper(), userHandle);
    }

    private PackageInfo getPackageInfo(String str) {
        this.mPackageManager = this.mContext.getPackageManager();
        try {
            PackageInfo packageInfo = this.mPackageManager.getPackageInfo(str, getTrackerPackageFlags());
            if (packageInfo != null) {
                return packageInfo;
            }
            logd("packageInfo is NULL");
            return null;
        } catch (PackageManager.NameNotFoundException e) {
            loge("Exception while reading packageInfo [ " + str + " ] exp = " + e.getMessage());
            return null;
        }
    }

    private class SatelliteApplicationPackageMonitor extends PackageChangeReceiver {
        private SatelliteApplicationPackageMonitor() {
        }

        public void onPackageAdded(String str) {
            SatelliteOptimizedApplicationsTracker.this.log("onPackageAdded : " + str);
            SatelliteOptimizedApplicationsTracker.this.mCurrentHandler.obtainMessage(2, str).sendToTarget();
        }

        public void onPackageRemoved(String str) {
            SatelliteOptimizedApplicationsTracker.this.log("onPackageRemoved : " + str);
            SatelliteOptimizedApplicationsTracker.this.mCurrentHandler.obtainMessage(5, str).sendToTarget();
        }

        public void onPackageUpdateFinished(String str) {
            SatelliteOptimizedApplicationsTracker.this.log("onPackageUpdateFinished : " + str);
            SatelliteOptimizedApplicationsTracker.this.mCurrentHandler.obtainMessage(3, str).sendToTarget();
        }

        public void onPackageModified(String str) {
            SatelliteOptimizedApplicationsTracker.this.log("onPackageModified : " + str);
            SatelliteOptimizedApplicationsTracker.this.mCurrentHandler.obtainMessage(4, str).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePackageRemoved(String str) {
        removeCacheOptimizedSatelliteApplication(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePackageMonitor(String str) {
        PackageInfo packageInfo = getPackageInfo(str);
        if (packageInfo != null) {
            if (isOptimizedSatelliteAppOrService(packageInfo)) {
                addCacheOptimizedSatelliteApplication(str);
            } else {
                removeCacheOptimizedSatelliteApplication(str);
            }
        }
    }

    private boolean isOptimizedSatelliteAppOrService(PackageInfo packageInfo) {
        String string;
        try {
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            boolean z = applicationInfo != null && isOptimizedSatelliteApplication(applicationInfo, packageInfo.packageName);
            if (!z && packageInfo.services != null) {
                int i = 0;
                while (true) {
                    ServiceInfo[] serviceInfoArr = packageInfo.services;
                    if (i >= serviceInfoArr.length) {
                        break;
                    }
                    ServiceInfo serviceInfo = serviceInfoArr[i];
                    Bundle bundle = serviceInfo.metaData;
                    if (bundle != null && (string = bundle.getString("android.telephony.PROPERTY_SATELLITE_DATA_OPTIMIZED")) != null && TextUtils.equals(string, serviceInfo.packageName)) {
                        logd("serviceInfo: " + serviceInfo.packageName + " is optimized");
                        return true;
                    }
                    i++;
                }
            }
            return z;
        } catch (Exception e) {
            loge(e.toString());
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleInitializeTracker() {
        try {
            Iterator it = this.mUserManager.getUsers().iterator();
            while (it.hasNext()) {
                this.mSatelliteApplications.putIfAbsent(Integer.valueOf(((UserInfo) it.next()).getUserHandle().getIdentifier()), new HashSet());
            }
            List<PackageInfo> installedPackages = this.mPackageManager.getInstalledPackages(getTrackerPackageFlags());
            for (int i = 0; i < installedPackages.size(); i++) {
                PackageInfo packageInfo = installedPackages.get(i);
                PackageInfo packageInfo2 = getPackageInfo(packageInfo.packageName);
                if (packageInfo2 != null && isOptimizedSatelliteAppOrService(packageInfo2)) {
                    addCacheOptimizedSatelliteApplication(packageInfo.packageName);
                }
            }
        } catch (Exception unused) {
            loge("Exception while initializing cache and getting packages");
            Iterator it2 = this.mUserManager.getUsers().iterator();
            while (it2.hasNext()) {
                this.mSatelliteApplications.remove(Integer.valueOf(((UserInfo) it2.next()).getUserHandle().getIdentifier()));
            }
        }
    }

    private boolean isOptimizedSatelliteApplication(ApplicationInfo applicationInfo, String str) {
        Bundle bundle = applicationInfo.metaData;
        if (bundle != null) {
            try {
                String string = bundle.getString("android.telephony.PROPERTY_SATELLITE_DATA_OPTIMIZED");
                StringBuilder sb = new StringBuilder("packageName: ");
                sb.append(str);
                sb.append(", value: ");
                sb.append(string == null ? "null" : string);
                logd(sb.toString());
                if (string == null) {
                    return false;
                }
                return TextUtils.equals(string, str);
            } catch (Exception e) {
                loge("Exception while reading metadata [ " + str + " ] exp = " + e.getMessage());
            }
        }
        return false;
    }

    private void addCacheOptimizedSatelliteApplication(String str) {
        Iterator it = this.mUserManager.getUsers().iterator();
        while (it.hasNext()) {
            int identifier = ((UserInfo) it.next()).getUserHandle().getIdentifier();
            try {
                this.mPackageManager.getPackageUidAsUser(str, 128, identifier);
                this.mSatelliteApplications.putIfAbsent(Integer.valueOf(identifier), new HashSet());
                ((Set) this.mSatelliteApplications.get(Integer.valueOf(identifier))).add(str);
            } catch (Exception unused) {
            }
        }
    }

    private void removeCacheOptimizedSatelliteApplication(String str) {
        Iterator it = this.mUserManager.getUsers().iterator();
        while (it.hasNext()) {
            int identifier = ((UserInfo) it.next()).getUserHandle().getIdentifier();
            try {
                this.mPackageManager.getPackageUidAsUser(str, 128, identifier);
            } catch (Exception unused) {
                if (this.mSatelliteApplications.containsKey(Integer.valueOf(identifier)) && ((Set) this.mSatelliteApplications.get(Integer.valueOf(identifier))).contains(str)) {
                    ((Set) this.mSatelliteApplications.get(Integer.valueOf(identifier))).remove(str);
                }
            }
        }
    }

    public List<String> getSatelliteOptimizedApplications(int i) {
        Set set = (Set) this.mSatelliteApplications.get(Integer.valueOf(i));
        if (set != null) {
            return new ArrayList(set);
        }
        handleInitializeTracker();
        return Collections.EMPTY_LIST;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.i("SatelliteAppTracker", str);
    }

    private void loge(String str) {
        Log.e("SatelliteAppTracker", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String str) {
        Log.d("SatelliteAppTracker", str);
    }
}
