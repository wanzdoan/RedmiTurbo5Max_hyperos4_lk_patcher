package com.xiaomi.mirror.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

/* JADX INFO: loaded from: classes2.dex */
public class AppUtil {
    public static final String PACKAGE_NAME_MIRROR = "com.xiaomi.mirror";
    private static int sMirrorInstalledStatus;

    public @interface InstallStatus {
        public static final int INSTALLED = 1;
        public static final int NOT_INSTALLED = 2;
        public static final int NO_PERMISSION = 3;
        public static final int UNKNOWN = 0;
    }

    public static int getInstallStatus(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return 2;
        }
        PackageManager packageManager = context.getPackageManager();
        if (packageManager.checkPermission("android.permission.QUERY_ALL_PACKAGES", context.getPackageName()) != 0) {
            return 3;
        }
        try {
            packageManager.getPackageInfo(str, 0);
            return 1;
        } catch (PackageManager.NameNotFoundException unused) {
            return 2;
        }
    }

    public static int getMirrorInstallStatus(Context context) {
        if (sMirrorInstalledStatus == 0) {
            sMirrorInstalledStatus = getInstallStatus(context, PACKAGE_NAME_MIRROR);
        }
        return sMirrorInstalledStatus;
    }
}
