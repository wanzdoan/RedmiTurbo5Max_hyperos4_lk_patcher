package com.android.internal.telephony.util;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Binder;
import android.os.UserHandle;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class LocationPermissionChecker {
    public static final int ERROR_LOCATION_MODE_OFF = 1;
    public static final int ERROR_LOCATION_PERMISSION_MISSING = 2;
    public static final int SUCCEEDED = 0;
    private final AppOpsManager mAppOpsManager;
    private final Context mContext;

    public LocationPermissionChecker(Context context) {
        this.mContext = context;
        this.mAppOpsManager = (AppOpsManager) context.getSystemService(AppOpsManager.class);
    }

    public boolean checkLocationPermission(String str, String str2, int i, String str3) {
        return checkLocationPermissionInternal(str, str2, i, str3) == 0;
    }

    public int checkLocationPermissionInternal(String str, String str2, int i, String str3) {
        try {
            checkPackage(i, str);
            if (!checkNetworkSettingsPermission(i) && !checkNetworkSetupWizardPermission(i) && !checkNetworkStackPermission(i) && !checkMainlineNetworkStackPermission(i)) {
                if (!isLocationModeEnabled()) {
                    return 1;
                }
                if (!checkCallersLocationPermission(str, str2, i, true, str3)) {
                    return 2;
                }
            }
            return 0;
        } catch (SecurityException unused) {
            return 2;
        }
    }

    public boolean checkCallersLocationPermission(String str, String str2, int i, boolean z, String str3) {
        String str4;
        boolean zIsTargetSdkLessThan = isTargetSdkLessThan(str, 29, i);
        if (z && zIsTargetSdkLessThan) {
            str4 = "android.permission.ACCESS_COARSE_LOCATION";
        } else {
            str4 = "android.permission.ACCESS_FINE_LOCATION";
        }
        if (getUidPermission(str4, i) == -1) {
            return false;
        }
        if (noteAppOpAllowed("android:fine_location", str, str2, i, str3)) {
            return true;
        }
        if (z && zIsTargetSdkLessThan) {
            return noteAppOpAllowed("android:coarse_location", str, str2, i, str3);
        }
        return false;
    }

    private boolean isLocationModeEnabled() {
        try {
            return ((LocationManager) this.mContext.getSystemService(LocationManager.class)).isLocationEnabledForUser(UserHandle.of(getCurrentUser()));
        } catch (Exception e) {
            Log.e("LocationPermissionChecker", "Failure to get location mode via API, falling back to settings", e);
            return false;
        }
    }

    private boolean isTargetSdkLessThan(String str, int i, int i2) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (this.mContext.getPackageManager().getApplicationInfoAsUser(str, 0, UserHandle.getUserHandleForUid(i2)).targetSdkVersion < i) {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return true;
            }
        } catch (PackageManager.NameNotFoundException unused) {
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
        Binder.restoreCallingIdentity(jClearCallingIdentity);
        return false;
    }

    private boolean noteAppOpAllowed(String str, String str2, String str3, int i, String str4) {
        try {
            return this.mAppOpsManager.noteOp(str, i, str2, str3, str4) == 0;
        } catch (SecurityException unused) {
        }
    }

    private void checkPackage(int i, String str) {
        if (str == null) {
            throw new SecurityException("Checking UID " + i + " but Package Name is Null");
        }
        this.mAppOpsManager.checkPackage(i, str);
    }

    protected int getCurrentUser() {
        return ActivityManager.getCurrentUser();
    }

    private int getUidPermission(String str, int i) {
        return this.mContext.checkPermission(str, -1, i);
    }

    private boolean checkNetworkSettingsPermission(int i) {
        return getUidPermission("android.permission.NETWORK_SETTINGS", i) == 0;
    }

    private boolean checkNetworkSetupWizardPermission(int i) {
        return getUidPermission("android.permission.NETWORK_SETUP_WIZARD", i) == 0;
    }

    private boolean checkNetworkStackPermission(int i) {
        return getUidPermission("android.permission.NETWORK_STACK", i) == 0;
    }

    private boolean checkMainlineNetworkStackPermission(int i) {
        return getUidPermission("android.permission.MAINLINE_NETWORK_STACK", i) == 0;
    }
}
