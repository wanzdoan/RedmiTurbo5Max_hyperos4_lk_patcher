package com.android.phone;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import miui.os.Build;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public abstract class MiuiNotificationMgrUtils {
    public static Bitmap getVoiceMailStatusIcon(Context context) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) context.getDrawable(R.drawable.icon_stat_sys_phone_call_voicemail);
        if (bitmapDrawable != null) {
            return bitmapDrawable.getBitmap();
        }
        return null;
    }

    public static String getSimCardDesp(Context context, int i) {
        StringBuilder sb = new StringBuilder();
        if (MiuiPhoneUtils.isMultiSimEnabled() && TelephonyManager.getDefault().getIccCardCount() > 1) {
            String str = String.format(context.getString(R.string.sim_card_num), Integer.valueOf(i + 1));
            sb.append("  |  ");
            sb.append(str);
        }
        return sb.toString();
    }

    public static String getCallForwardNotiContentText(Context context, int i) {
        if (MiuiPhoneUtils.isMultiSimEnabled() && TelephonyManager.getDefault().getIccCardCount() > 1) {
            return String.format(context.getString(R.string.sum_cfu_enabled_indicator_by_sim), Integer.valueOf(i + 1));
        }
        return context.getString(R.string.sum_cfu_enabled_indicator);
    }

    public static void configNetworkSelectionNotification(Context context, Notification.Builder builder, int i) {
        builder.addAction(0, context.getString(R.string.miui_network_selection_reset_text), createAutoPendingIntent(context, i)).setContentTitle(getNetworkSelectionNotiContentTitle(context, i));
        Bundle bundle = new Bundle();
        bundle.putBoolean("miui.showAction", true);
        builder.setExtras(bundle);
    }

    private static String getNetworkSelectionNotiContentTitle(Context context, int i) {
        if (MiuiPhoneUtils.isMultiSimEnabled() && TelephonyManager.getDefault().getIccCardCount() > 1) {
            return String.format(context.getString(R.string.miui_notification_network_selection_title_by_sim), Integer.valueOf(i + 1));
        }
        return context.getString(R.string.miui_notification_network_selection_title);
    }

    private static PendingIntent createAutoPendingIntent(Context context, int i) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setFlags(335544320);
        intent.putExtra("restore_to_automatic_mode", true);
        intent.setComponent(new ComponentName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.settings.NetworkSetting"));
        SubscriptionManager.putSlotIdExtra(intent, i);
        return PendingIntent.getActivity(context, i + 50, intent, 67108864);
    }

    public static void setChannelId(Notification.Builder builder, String str) {
        try {
            Notification.Builder.class.getMethod("setChannelId", String.class).invoke(builder, str);
        } catch (Exception e) {
            Log.d("MiuiNotificationMgrUtils", "setChannelId", e);
        }
    }

    public static void createNotificationChannelsList(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        ArrayList arrayList = new ArrayList();
        try {
            Method method = NotificationManager.class.getMethod("createNotificationChannels", List.class);
            Class<?> cls = Class.forName("android.app.NotificationChannel");
            Class<?> cls2 = Integer.TYPE;
            arrayList.add(cls.getConstructor(String.class, CharSequence.class, cls2).newInstance("voiceRoamingOn", context.getText(R.string.telocation_contacts_countrycode), 2));
            if (MiuiPhoneUtils.isTabletDeviceWithVolteCapability()) {
                arrayList.add(cls.getConstructor(String.class, CharSequence.class, cls2).newInstance("voiceNotAvailable", context.getText(R.string.voice_service_switch_title), 2));
            }
            if (Build.IS_DEMO_BUILD) {
                arrayList.add(cls.getConstructor(String.class, CharSequence.class, cls2).newInstance("demo_Version", context.getText(R.string.notification_channel_demo), 3));
            }
            if (MiuiPhoneUtils.isEsimCapable()) {
                arrayList.add(cls.getConstructor(String.class, CharSequence.class, cls2).newInstance("eSimInActiveNotificationForGlobal", context.getText(R.string.esim_inactive_notification_channel_for_global), 4));
                arrayList.add(cls.getConstructor(String.class, CharSequence.class, cls2).newInstance("eSimIsActivatingNotificationForO2", context.getText(R.string.esim_is_activating_notification_channel_for_o2), 4));
            } else if (MiuiPhoneUtils.isSupportGoogleLPAEsim()) {
                arrayList.add(cls.getConstructor(String.class, CharSequence.class, cls2).newInstance("eSimInActiveNotification", context.getText(R.string.esim_inactive_notification_channel), 3));
            }
            if (MiuiPhoneUtils.isEsimCapable()) {
                arrayList.add(cls.getConstructor(String.class, CharSequence.class, cls2).newInstance("osuNotification", context.getText(R.string.osu_notification_channel), 4));
            }
            Object objNewInstance = cls.getConstructor(String.class, CharSequence.class, cls2).newInstance("ai_call_summary", context.getText(R.string.ai_call_summary_notification_channel), 4);
            cls.getMethod("enableVibration", Boolean.TYPE).invoke(objNewInstance, Boolean.FALSE);
            arrayList.add(objNewInstance);
            method.invoke(notificationManager, arrayList);
        } catch (Exception e) {
            Log.d("MiuiNotificationMgrUtils", "createNotificationChannels", e);
        }
    }
}
