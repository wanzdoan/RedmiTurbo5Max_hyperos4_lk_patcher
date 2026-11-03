package com.android.internal.telephony.uicc;

import android.R;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import com.android.internal.telephony.util.NotificationChannelController;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class InstallCarrierAppUtils {
    private static CarrierAppInstallReceiver sCarrierAppInstallReceiver;

    static void showNotification(Context context, String str) {
        String string;
        Resources system = Resources.getSystem();
        String string2 = system.getString(R.string.mediasize_iso_b5);
        String appNameFromPackageName = getAppNameFromPackageName(context, str);
        if (TextUtils.isEmpty(appNameFromPackageName)) {
            string = system.getString(R.string.mediasize_iso_b3);
        } else {
            string = system.getString(R.string.mediasize_iso_b4, appNameFromPackageName);
        }
        String string3 = system.getString(R.string.mediasize_iso_b2);
        getNotificationManager(context).notify(str, 12, new Notification.Builder(context, NotificationChannelController.CHANNEL_ID_SIM).setContentTitle(string2).setContentText(string).setSmallIcon(R.drawable.ic_zen_mode_icon_group_of_people).addAction(new Notification.Action.Builder((Icon) null, string3, PendingIntent.getActivity(context, 0, getPlayStoreIntent(str), 201326592)).build()).setOngoing(Settings.Global.getInt(context.getContentResolver(), "install_carrier_app_notification_persistent", 1) == 1).setVisibility(-1).build());
    }

    static void hideAllNotifications(Context context) {
        NotificationManager notificationManager = getNotificationManager(context);
        StatusBarNotification[] activeNotifications = notificationManager.getActiveNotifications();
        if (activeNotifications == null) {
            return;
        }
        for (StatusBarNotification statusBarNotification : activeNotifications) {
            if (statusBarNotification.getId() == 12) {
                notificationManager.cancel(statusBarNotification.getTag(), statusBarNotification.getId());
            }
        }
    }

    static void hideNotification(Context context, String str) {
        getNotificationManager(context).cancel(str, 12);
    }

    static Intent getPlayStoreIntent(String str) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse("market://details?id=" + str));
        intent.addFlags(268435456);
        return intent;
    }

    static void showNotificationIfNotInstalledDelayed(Context context, String str, long j) {
        ((AlarmManager) context.getSystemService("alarm")).set(3, SystemClock.elapsedRealtime() + j, PendingIntent.getBroadcast(context, 0, ShowInstallAppNotificationReceiver.get(context, str), 67108864));
    }

    static void registerPackageInstallReceiver(Context context) {
        if (sCarrierAppInstallReceiver == null) {
            sCarrierAppInstallReceiver = new CarrierAppInstallReceiver();
            Context applicationContext = context.getApplicationContext();
            IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
            intentFilter.addDataScheme("package");
            applicationContext.registerReceiver(sCarrierAppInstallReceiver, intentFilter);
        }
    }

    static void unregisterPackageInstallReceiver(Context context) {
        if (sCarrierAppInstallReceiver == null) {
            return;
        }
        context.getApplicationContext().unregisterReceiver(sCarrierAppInstallReceiver);
        sCarrierAppInstallReceiver = null;
    }

    static boolean isPackageInstallNotificationActive(Context context) {
        for (StatusBarNotification statusBarNotification : getNotificationManager(context).getActiveNotifications()) {
            if (statusBarNotification.getId() == 12) {
                return true;
            }
        }
        return false;
    }

    static String getAppNameFromPackageName(Context context, String str) {
        return getAppNameFromPackageName(str, Settings.Global.getString(context.getContentResolver(), "carrier_app_names"));
    }

    public static String getAppNameFromPackageName(String str, String str2) {
        String lowerCase = str.toLowerCase(Locale.ROOT);
        if (TextUtils.isEmpty(str2)) {
            return null;
        }
        List listAsList = Arrays.asList(str2.split("\\s*;\\s*"));
        if (listAsList.isEmpty()) {
            return null;
        }
        Iterator it = listAsList.iterator();
        while (it.hasNext()) {
            String[] strArrSplit = ((String) it.next()).split("\\s*:\\s*");
            if (strArrSplit.length == 2 && strArrSplit[0].equals(lowerCase)) {
                return strArrSplit[1];
            }
        }
        return null;
    }

    private static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService("notification");
    }
}
