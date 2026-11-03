package com.android.phone;

import android.app.role.RoleManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import java.util.List;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public abstract class EmergencyAssistanceHelper {
    private static final String TAG = "EmergencyAssistanceHelper";

    public static String getIntentAction() {
        return "android.telephony.action.EMERGENCY_ASSISTANCE";
    }

    public static List resolveAssistPackageAndQueryActivities(Context context) {
        List listQueryAssistActivities = queryAssistActivities(context, getDefaultEmergencyPackage(context));
        if (listQueryAssistActivities != null && !listQueryAssistActivities.isEmpty()) {
            return listQueryAssistActivities;
        }
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> listQueryIntentActivities = packageManager.queryIntentActivities(new Intent(getIntentAction()), 0);
        PackageInfo packageInfo = null;
        for (int i = 0; i < listQueryIntentActivities.size(); i++) {
            if (listQueryIntentActivities.get(i).activityInfo != null) {
                try {
                    PackageInfo packageInfo2 = packageManager.getPackageInfo(listQueryIntentActivities.get(i).activityInfo.packageName, 0);
                    if (isSystemApp(packageInfo2) && (packageInfo == null || packageInfo.firstInstallTime > packageInfo2.firstInstallTime)) {
                        packageInfo = packageInfo2;
                    }
                } catch (PackageManager.NameNotFoundException unused) {
                }
            }
        }
        if (packageInfo == null) {
            return null;
        }
        setDefaultEmergencyPackageAsync(context, packageInfo.packageName);
        return queryAssistActivities(context, packageInfo.packageName);
    }

    public static ComponentName getComponentName(ResolveInfo resolveInfo) {
        if (resolveInfo == null || resolveInfo.activityInfo == null) {
            return null;
        }
        ActivityInfo activityInfo = resolveInfo.activityInfo;
        return new ComponentName(activityInfo.packageName, activityInfo.name);
    }

    private static List queryAssistActivities(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return context.getPackageManager().queryIntentActivities(new Intent(getIntentAction()).setPackage(str), 0);
    }

    private static boolean isSystemApp(PackageInfo packageInfo) {
        ApplicationInfo applicationInfo = packageInfo.applicationInfo;
        return (applicationInfo == null || (applicationInfo.flags & 1) == 0) ? false : true;
    }

    private static String getDefaultEmergencyPackage(Context context) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            List roleHolders = ((RoleManager) context.getSystemService(RoleManager.class)).getRoleHolders("android.app.role.EMERGENCY");
            return roleHolders.isEmpty() ? null : (String) roleHolders.get(0);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    private static boolean setDefaultEmergencyPackageAsync(Context context, String str) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ((RoleManager) context.getSystemService(RoleManager.class)).addRoleHolderAsUser("android.app.role.EMERGENCY", str, 0, Process.myUserHandle(), AsyncTask.THREAD_POOL_EXECUTOR, new Consumer() { // from class: com.android.phone.EmergencyAssistanceHelper$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    EmergencyAssistanceHelper.$r8$lambda$pMv8PyErLokGn0Xj7OjsoH1S4F8((Boolean) obj);
                }
            });
            return true;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public static /* synthetic */ void $r8$lambda$pMv8PyErLokGn0Xj7OjsoH1S4F8(Boolean bool) {
        if (bool.booleanValue()) {
            return;
        }
        Log.e(TAG, "Failed to set emergency default app.");
    }
}
