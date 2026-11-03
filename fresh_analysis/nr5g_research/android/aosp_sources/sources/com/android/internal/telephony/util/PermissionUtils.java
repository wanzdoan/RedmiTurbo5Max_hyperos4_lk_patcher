package com.android.internal.telephony.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.UserHandle;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class PermissionUtils {
    public static boolean hasAnyPermissionOf(Context context, String... strArr) {
        for (String str : strArr) {
            if (context.checkCallingOrSelfPermission(str) == 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasAnyPermissionOf(Context context, int i, int i2, String... strArr) {
        for (String str : strArr) {
            if (context.checkPermission(str, i, i2) == 0) {
                return true;
            }
        }
        return false;
    }

    public static void enforceAnyPermissionOf(Context context, String... strArr) {
        if (hasAnyPermissionOf(context, strArr)) {
            return;
        }
        throw new SecurityException("Requires one of the following permissions: " + String.join(", ", strArr) + ".");
    }

    public static void enforceNetworkStackPermission(Context context) {
        enforceNetworkStackPermissionOr(context, new String[0]);
    }

    public static void enforceNetworkStackPermissionOr(Context context, String... strArr) {
        ArrayList arrayList = new ArrayList(Arrays.asList(strArr));
        arrayList.add("android.permission.NETWORK_STACK");
        arrayList.add("android.permission.MAINLINE_NETWORK_STACK");
        enforceAnyPermissionOf(context, (String[]) arrayList.toArray(new String[0]));
    }

    public static void enforceRestrictedNetworkPermission(Context context, String str) {
        context.enforceCallingOrSelfPermission("android.permission.CONNECTIVITY_USE_RESTRICTED_NETWORKS", str);
    }

    public static void enforceAccessNetworkStatePermission(Context context, String str) {
        context.enforceCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE", str);
    }

    public static boolean hasDumpPermission(Context context, String str, PrintWriter printWriter) {
        if (context.checkCallingOrSelfPermission("android.permission.DUMP") == 0) {
            return true;
        }
        printWriter.println("Permission Denial: can't dump " + str + " from from pid=" + Binder.getCallingPid() + ", uid=" + Binder.getCallingUid() + " due to missing android.permission.DUMP permission");
        return false;
    }

    public static void enforceSystemFeature(Context context, String str, String str2) {
        if (context.getPackageManager().hasSystemFeature(str)) {
            return;
        }
        if (str2 == null) {
            throw new UnsupportedOperationException();
        }
        throw new UnsupportedOperationException(str2);
    }

    public static List<String> getGrantedPermissions(PackageInfo packageInfo) {
        if (packageInfo.requestedPermissions == null) {
            return Collections.EMPTY_LIST;
        }
        ArrayList arrayList = new ArrayList(packageInfo.requestedPermissions.length);
        int i = 0;
        while (true) {
            String[] strArr = packageInfo.requestedPermissions;
            if (i >= strArr.length) {
                return arrayList;
            }
            if ((packageInfo.requestedPermissionsFlags[i] & 2) != 0) {
                arrayList.add(strArr[i]);
            }
            i++;
        }
    }

    public static void enforcePackageNameMatchesUid(Context context, int i, String str) {
        if (getAppUid(context, str, UserHandle.getUserHandleForUid(i)) == i) {
            return;
        }
        throw new SecurityException(str + " does not belong to uid " + i);
    }

    private static int getAppUid(Context context, String str, UserHandle userHandle) {
        PackageManager packageManager = context.createContextAsUser(userHandle, 0).getPackageManager();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return packageManager.getPackageUid(str, 0);
        } catch (PackageManager.NameNotFoundException unused) {
            return -1;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }
}
