package com.xiaomi.mirror.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.xiaomi.internal.telephony.DynamicSarService;

/* JADX INFO: loaded from: classes2.dex */
public class VersionUtil {
    static String KEY_FEATURE_VERSION = "feature_version";
    static String TAG = "VersionUtil";

    public static boolean mirrorSupportOS2Version(Context context) {
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(AppUtil.PACKAGE_NAME_MIRROR, DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "name not found", e);
            applicationInfo = null;
        }
        return applicationInfo != null && applicationInfo.metaData.getInt(KEY_FEATURE_VERSION) >= 1;
    }
}
