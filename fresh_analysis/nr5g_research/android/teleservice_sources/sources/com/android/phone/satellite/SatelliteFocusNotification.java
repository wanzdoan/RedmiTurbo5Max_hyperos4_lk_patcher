package com.android.phone.satellite;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import com.android.phone.R;
import com.android.phone.settings.search.utils.SearchContract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteFocusNotification {
    private Context mContext;
    private NotificationManager mNotificationManager;

    public SatelliteFocusNotification(Context context) {
        this.mContext = context;
        createNotificationChannel();
    }

    private Notification.Builder getNotificationBuilder() {
        Notification.Builder builder = new Notification.Builder(this.mContext, "satellite_default_channel");
        builder.setSmallIcon(R.drawable.ic_satellite);
        builder.setPriority(1);
        builder.setTimeoutAfter(15000L);
        return builder;
    }

    private void createNotificationChannel() {
        this.mNotificationManager = (NotificationManager) this.mContext.getSystemService("notification");
        NotificationChannel notificationChannel = new NotificationChannel("satellite_default_channel", this.mContext.getResources().getString(R.string.satellite_enhanced_paging_notification_title), 4);
        notificationChannel.enableVibration(true);
        this.mNotificationManager.createNotificationChannel(notificationChannel);
    }

    public void sendNotification() {
        String string = this.mContext.getResources().getString(R.string.satellite_enhanced_paging_notification_title);
        String string2 = this.mContext.getResources().getString(R.string.satellite_enhanced_paging_notification_message);
        Notification.Builder notificationBuilder = getNotificationBuilder();
        notificationBuilder.setContentIntent(getReceiveIntent()).setContentTitle(string).setContentText(string2);
        Bundle bundle = new Bundle();
        bundle.putParcelable("miui.appIcon", Icon.createWithResource(this.mContext, R.drawable.ic_satellite));
        String strCreateFocusNotifcationBundle = createFocusNotifcationBundle(string, string2);
        Bundle bundle2 = new Bundle();
        bundle2.putParcelable("miui.focus.pic_profile", Icon.createWithResource(this.mContext, R.drawable.ic_satellite));
        bundle2.putParcelable("miui.focus.pic_aod", Icon.createWithResource(this.mContext, R.drawable.ic_satellite));
        bundle.putBundle("miui.focus.pics", bundle2);
        notificationBuilder.addExtras(bundle);
        Notification notificationBuild = notificationBuilder.build();
        notificationBuild.extras.putString("miui.focus.param", strCreateFocusNotifcationBundle);
        this.mNotificationManager.notify(2, notificationBuild);
    }

    private String createFocusNotifcationBundle(String str, String str2) {
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        JSONObject jSONObject3 = new JSONObject();
        JSONObject jSONObject4 = new JSONObject();
        JSONObject jSONObject5 = new JSONObject();
        JSONObject jSONObject6 = new JSONObject();
        Object jSONArray = new JSONArray();
        try {
            jSONObject2.put("protocol", 1);
            jSONObject2.put("enableFloat", true);
            jSONObject2.put("islandFirstFloat", true);
            jSONObject2.put("updatable", true);
            jSONObject2.put("reopen", "reopen");
            jSONObject2.put("business", "satelliteEnhancedPaging");
            jSONObject2.put("tickerPic", "miui.focus.pic_profile");
            jSONObject2.put("ticker", this.mContext.getResources().getString(R.string.satellite_enhanced_paging_notification_title));
            jSONObject2.put("aodPic", "miui.focus.pic_aod");
            jSONObject2.put("aodTitle", this.mContext.getResources().getString(R.string.satellite_enhanced_paging_notification_title));
            jSONObject3.put("islandProperty", 1);
            jSONObject3.put("islandTimeout", 15);
            jSONObject3.put("highlightColor", "#F97A1F");
            JSONObject jSONObject7 = new JSONObject();
            jSONObject7.put("type", 1);
            JSONObject jSONObject8 = new JSONObject();
            jSONObject8.put("type", 1);
            jSONObject8.put("pic", "miui.focus.pic_profile");
            jSONObject7.put("picInfo", jSONObject8);
            JSONObject jSONObject9 = new JSONObject();
            jSONObject9.put(SearchContract.SearchResultColumn.COLUMN_TITLE, this.mContext.getResources().getString(R.string.satellite_setting_title));
            jSONObject9.put("colorTitle", "#000000");
            jSONObject9.put("showHighlightColor", true);
            jSONObject7.put("textInfo", jSONObject9);
            jSONObject4.put("imageTextInfoLeft", jSONObject7);
            jSONObject3.put("bigIslandArea", jSONObject4);
            JSONObject jSONObject10 = new JSONObject();
            jSONObject10.put("type", 1);
            jSONObject10.put("pic", "miui.focus.pic_profile");
            jSONObject5.put("picInfo", jSONObject10);
            jSONObject3.put("smallIslandArea", jSONObject5);
            jSONObject2.put("param_island", jSONObject3);
            jSONObject6.put("picProfile", "miui.focus.pic_profile");
            jSONObject6.put(SearchContract.SearchResultColumn.COLUMN_TITLE, str);
            jSONObject6.put("content", str2);
            jSONObject6.put("appIconPkg", "miui.focus.pic_appIconPkg");
            jSONObject2.put("chatInfo", jSONObject6);
            jSONObject2.put("actions", jSONArray);
            jSONObject.put("param_v2", jSONObject2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject.toString();
    }

    public PendingIntent getReceiveIntent() {
        Intent intent = new Intent("com.xiaomi.phone.ACTION_SATELLITE");
        intent.setPackage("com.xiaomi.phone");
        intent.setComponent(new ComponentName("com.xiaomi.phone", "com.xiaomi.phone.satellite.SatelliteDialogActivity"));
        intent.putExtra("com.xiaomi.phone.satellite.dialog.action", 1);
        intent.addFlags(268500992);
        return PendingIntent.getActivity(this.mContext, 0, intent, 67108864);
    }
}
