package com.android.phone.satellite;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;
import com.android.internal.telephony.Phone;
import com.android.phone.BatteryStateManager;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.phone.SatelliteManagerBase;
import com.android.phone.ntn.NtnOptimizeControllerBase;
import com.android.phone.utils.MiuiThreadUtil;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteUIController {
    private static String LOG_TAG = "SatelliteUIController";
    private static SatelliteUIController sInstance;
    private final Context mContext;
    private List mUnavailableCameraIds = Collections.synchronizedList(new ArrayList());
    private CameraManager.AvailabilityCallback mAvailabilityCallback = new CameraManager.AvailabilityCallback() { // from class: com.android.phone.satellite.SatelliteUIController.1
        @Override // android.hardware.camera2.CameraManager.AvailabilityCallback
        public void onCameraAvailable(String str) {
            super.onCameraAvailable(str);
            SatelliteUIController.logd("onCameraAvailable:" + str);
            synchronized (SatelliteUIController.this.mUnavailableCameraIds) {
                try {
                    if (SatelliteUIController.this.mUnavailableCameraIds.contains(str)) {
                        SatelliteUIController.this.mUnavailableCameraIds.remove(str);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        @Override // android.hardware.camera2.CameraManager.AvailabilityCallback
        public void onCameraUnavailable(String str) {
            super.onCameraUnavailable(str);
            SatelliteUIController.logd("onCameraUnavailable:" + str);
            synchronized (SatelliteUIController.this.mUnavailableCameraIds) {
                try {
                    if (!SatelliteUIController.this.mUnavailableCameraIds.contains(str)) {
                        SatelliteUIController.this.mUnavailableCameraIds.add(str);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    };
    private MiuiSatelliteBattery mMiuiSatelliteBattery = new MiuiSatelliteBattery();

    public static SatelliteUIController getInstance() {
        if (sInstance == null) {
            loge("SatelliteUIController was not yet initialized.");
        }
        return sInstance;
    }

    public static SatelliteUIController make(Context context) {
        if (sInstance == null) {
            sInstance = new SatelliteUIController(context);
        }
        return sInstance;
    }

    public SatelliteUIController(Context context) {
        this.mContext = context;
        new Handler(MiuiThreadUtil.getWorkThreadLooper()).post(new Runnable() { // from class: com.android.phone.satellite.SatelliteUIController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        CameraManager cameraManager = (CameraManager) PhoneGlobals.getInstance().getSystemService("camera");
        if (cameraManager != null) {
            cameraManager.registerAvailabilityCallback(this.mAvailabilityCallback, new Handler(Looper.getMainLooper()));
            logd("registerAvailabilityCallback...");
        }
    }

    public boolean isCameraEnabled() {
        boolean z = !this.mUnavailableCameraIds.isEmpty();
        Log.i(LOG_TAG, "isCameraEnabled: " + z);
        return z;
    }

    public boolean checkWhetherCameraIsEnabled(boolean z, int i) {
        if (!z || !isCameraEnabled()) {
            return false;
        }
        pullUpControlCenter(i);
        new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.android.phone.satellite.SatelliteUIController.2
            @Override // java.lang.Runnable
            public void run() {
                Toast.makeText(SatelliteUIController.this.mContext, R.string.satellite_camera_enable_toast, 1).show();
                SatelliteUIController.logd("camera is enabled，satellite can not use");
            }
        });
        return true;
    }

    public boolean isSatelliteUpgradingSuccess(int i, int i2) {
        pullUpControlCenter(i2);
        if (i == 2) {
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.android.phone.satellite.SatelliteUIController.3
                @Override // java.lang.Runnable
                public void run() {
                    Toast.makeText(SatelliteUIController.this.mContext, R.string.satellite_flashing_toast, 1).show();
                    SatelliteUIController.logd("satellite ota upgrading, can not use");
                }
            });
            return false;
        }
        if (i != 5) {
            return true;
        }
        logd("satellite ota is failed.");
        startDialogActivity(i2, 14);
        return false;
    }

    public boolean isBatteryWork(int i) {
        int i2 = BatteryStateManager.getInstance().getmBatteryLevel();
        NtnOptimizeControllerBase ntnOptimizeControllerBase = NtnOptimizeControllerBase.getInstance();
        boolean z = ntnOptimizeControllerBase != null && ntnOptimizeControllerBase.isSatellitePaMegathermal();
        if (i2 >= 0) {
            if (i2 <= 1) {
                startDialogActivity(i, 9);
                return false;
            }
            if (z) {
                startDialogActivity(i, 12);
                return false;
            }
        }
        return true;
    }

    public boolean isPrivacyAccept(int i) {
        PreferenceManager.getDefaultSharedPreferences(this.mContext);
        if (isPrivacyStatementAgree()) {
            return true;
        }
        startDialogActivity(i, 4);
        return false;
    }

    public boolean isCarrierSupport(int i) {
        if (TelephonyManagerEx.getDefault().isSupportSatelliteByCarrier()) {
            return true;
        }
        startDialogActivity(i, 5);
        return false;
    }

    public boolean isUserSaveTips(int i) {
        if (!SystemProperties.getBoolean("persist.radio.satellite.showAgain", true)) {
            return true;
        }
        startDialogActivity(i, 1);
        return false;
    }

    public boolean isSuperSaveMode(int i, int i2) {
        int i3 = BatteryStateManager.getInstance().getmBatteryLevel();
        boolean z = Settings.System.getInt(this.mContext.getContentResolver(), "power_supersave_mode_open", 0) != 0;
        if (i3 <= 5 && i3 > 1 && !z) {
            startDialogActivity(i2, 8);
            return false;
        }
        if (SatelliteManagerBase.getInstance() == null) {
            return false;
        }
        SatelliteManagerBase.getInstance().openSatellite(i, i2);
        return true;
    }

    private boolean isSatelliteUiUseIncall() {
        return this.mContext.getResources().getBoolean(R.bool.config_is_satellite_ui_use_incall);
    }

    public void enableSatelliteIncallUi(boolean z, int i, int i2) {
        Intent intent;
        int i3 = z ? 1 : 2;
        boolean zIsSatelliteUiUseIncall = isSatelliteUiUseIncall();
        try {
            logd("enableSatelliteIncallUi, phoneId = " + i + ", enable = " + z + ". isSatelliteUiUseIncall = " + zIsSatelliteUiUseIncall);
            if (zIsSatelliteUiUseIncall) {
                intent = new Intent("com.android.incallui.ACTION_SATELLITE");
                intent.setPackage("com.android.incallui");
                intent.setComponent(new ComponentName("com.android.incallui", "com.android.incallui.SatelliteDialogActivity"));
                intent.putExtra("com.android.incallui.satellite.dialog.action", i3);
            } else {
                intent = new Intent("com.xiaomi.phone.ACTION_SATELLITE");
                intent.setPackage("com.xiaomi.phone");
                intent.setComponent(new ComponentName("com.xiaomi.phone", "com.xiaomi.phone.satellite.SatelliteDialogActivity"));
                intent.putExtra("com.xiaomi.phone.satellite.dialog.action", i3);
            }
            Phone phone = MiuiPhoneUtils.getPhone(i);
            if (phone != null) {
                intent.putExtra("satellite_sub_id", phone.getSubId());
            }
            intent.putExtra("flag_id", i2);
            intent.addFlags(268500992);
            this.mContext.startActivity(intent);
        } catch (Exception e) {
            loge("startSatelliteUI: " + e);
        }
    }

    public boolean isPrivacyStatementAgree() {
        return PreferenceManager.getDefaultSharedPreferences(this.mContext).getBoolean(SatelliteDialogActivity.AGREE_SATELLITE_PRIVACY_STATEMENT, false);
    }

    public void startDialogActivity(int i, int i2) {
        Intent intent = new Intent(this.mContext, (Class<?>) SatelliteDialogActivity.class);
        intent.setFlags(402718720);
        intent.putExtra("flag_id", i);
        intent.putExtra("type", i2);
        this.mContext.startActivity(intent);
    }

    public void sendEnableSatelliteBroadcast(int i, boolean z, boolean z2) {
        Intent intent = new Intent(SarDeviceStateListener.BC_INTENT_ACTION_SATELLITE_STATE_CHANGE);
        intent.addFlags(268435456);
        intent.putExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, i);
        intent.putExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_IS_ENABLE, z);
        intent.putExtra("is_display", z2);
        Log.d(LOG_TAG, "sendEnableSatelliteBroadcast");
        this.mContext.sendBroadcast(intent);
    }

    public void pullUpControlCenter(int i) {
        if (i == 1000) {
            try {
                Object systemService = this.mContext.getSystemService("statusbar");
                Class[] clsArr = new Class[0];
                systemService.getClass().getMethod("collapsePanels", null).invoke(systemService, null);
            } catch (Exception e) {
                loge("pullUpControlCenter " + e);
            }
        }
    }

    public void registerBatteryListener(boolean z, boolean z2) {
        MiuiSatelliteBattery miuiSatelliteBattery = this.mMiuiSatelliteBattery;
        if (z) {
            miuiSatelliteBattery.registerListener(this.mContext, z2);
        } else {
            miuiSatelliteBattery.unregisterListener();
        }
    }

    class MiuiSatelliteBattery implements BatteryStateManager.BatteryStateListener {
        private int mBatteryLevel;
        private Context mContext;

        private MiuiSatelliteBattery() {
            this.mBatteryLevel = 100;
        }

        @Override // com.android.phone.BatteryStateManager.BatteryStateListener
        public void notifyBatteryStateChanged(int i) {
            SatelliteUIController.logd("notifyBatteryStateChanged = " + i);
            this.mBatteryLevel = i;
            if (isLowBattery() && SatelliteManagerBase.getInstance() != null && SatelliteManagerBase.getInstance().isSatelliteEnabled() && SatelliteDialogActivity.mTimer == null) {
                SatelliteUIController.this.enableSatelliteIncallUi(false, SubscriptionManager.INVALID_SUBSCRIPTION_ID, 1005);
                Intent intent = new Intent(this.mContext, (Class<?>) SatelliteDialogActivity.class);
                intent.setFlags(402718720);
                intent.putExtra("type", 3);
                this.mContext.startActivity(intent);
            }
        }

        public boolean isLowBattery() {
            return this.mBatteryLevel <= 1;
        }

        public void registerListener(Context context, boolean z) {
            SatelliteUIController.logd("registerListener....");
            this.mContext = context;
            BatteryStateManager.getInstance().registerBatteryStateListener(this);
        }

        public void unregisterListener() {
            SatelliteUIController.logd("unregisterListener....");
            BatteryStateManager.getInstance().unregisterBatteryStateListener(this);
        }
    }

    public void showSatelliteOtaNotification() {
        NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService("notification");
        for (StatusBarNotification statusBarNotification : notificationManager.getActiveNotifications()) {
            if (statusBarNotification.getId() == 1) {
                return;
            }
        }
        logd("showSatelliteOtaNotification.");
        notificationManager.createNotificationChannel(new NotificationChannel("firmware_upgrade_satellite", this.mContext.getString(R.string.satellite_flash_time_out_notification), 4));
        Bundle bundle = new Bundle();
        bundle.putParcelable("miui.appIcon", Icon.createWithResource(this.mContext, R.drawable.settings_satellite));
        notificationManager.notify("satellite", 1, new Notification.Builder(this.mContext, "firmware_upgrade_satellite").setOngoing(true).setSmallIcon(R.drawable.settings_satellite).addExtras(bundle).setContentText(this.mContext.getString(R.string.satellite_flash_time_out_message)).setContentTitle(this.mContext.getString(R.string.satellite_flash_time_out_title)).setAutoCancel(true).setPriority(1).setChannelId("firmware_upgrade_satellite").build());
    }

    public void sendFocusNotification() {
        logd("sendFocusNotification.");
        new SatelliteFocusNotification(this.mContext).sendNotification();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logd(String str) {
        Log.d(LOG_TAG, str);
    }

    private static void loge(String str) {
        Log.e(LOG_TAG, str);
    }
}
