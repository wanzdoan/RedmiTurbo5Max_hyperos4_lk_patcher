package com.android.internal.telephony.satellite;

import android.R;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.telephony.CarrierConfigManager;
import android.telephony.PersistentLogger;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.SomeArgs;
import com.android.internal.telephony.configupdate.ConfigProviderAdaptor;
import com.android.internal.telephony.util.WorkerThread;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public class UpsellNotificationController extends Handler {
    private static UpsellNotificationController sInstance;
    private final AtomicInteger mActiveDataSubId;
    private final CarrierConfigManager mCarrierConfigManager;
    private final Map mCarrierConfigMap;
    private final Context mContext;
    private final Executor mExecutor;
    private final FeatureFlags mFeatureFlags;
    private final AtomicBoolean mIsNotificationShowing;
    private final BroadcastReceiver mNotificationInteractionBroadcastReceiver;
    private final NotificationManager mNotificationManager;
    private PersistentLogger mPersistentLogger;
    private final SubscriptionManager mSubscriptionManager;
    private final TelephonyManager mTelephonyManager;

    public UpsellNotificationController(Context context, Looper looper, FeatureFlags featureFlags) {
        super(looper);
        this.mActiveDataSubId = new AtomicInteger(-1);
        this.mIsNotificationShowing = new AtomicBoolean(false);
        this.mCarrierConfigMap = new ConcurrentHashMap();
        this.mNotificationInteractionBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.satellite.UpsellNotificationController.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                if (TextUtils.isEmpty(action)) {
                    UpsellNotificationController.loge("Received empty action from the notification");
                }
                UpsellNotificationController.logd("Notification Broadcast received action = " + intent.getAction());
                int intExtra = intent.getIntExtra("android.telephony.extra.SUBSCRIPTION_ID", -1);
                if (intExtra == -1) {
                    UpsellNotificationController.loge("Received intent with INVALID_SUBSCRIPTION_ID for action: " + action);
                    return;
                }
                action.hashCode();
                switch (action) {
                    case "com.android.internal.telephony.satellite.ACTION_UPSELL_DISMISSED":
                        UpsellNotificationController.this.sendMessageAsync(5, Integer.valueOf(intExtra));
                        break;
                    case "com.android.internal.telephony.satellite.ACTION_UPSELL_CLICKED":
                        UpsellNotificationController.this.sendMessageAsync(4, Integer.valueOf(intExtra));
                        break;
                    case "com.android.internal.telephony.satellite.ACTION_UPSELL_SUPPRESSED":
                        UpsellNotificationController.this.sendMessageAsync(6, Integer.valueOf(intExtra));
                        break;
                    default:
                        UpsellNotificationController.this.plogd("Unknown notification action: ".concat(action));
                        break;
                }
            }
        };
        this.mContext = context;
        this.mExecutor = new HandlerExecutor(this);
        this.mTelephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        this.mCarrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        this.mNotificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
        this.mFeatureFlags = featureFlags;
        this.mPersistentLogger = SatelliteServiceUtils.getPersistentLogger(context);
    }

    public static UpsellNotificationController make(Context context, FeatureFlags featureFlags) {
        if (sInstance == null) {
            UpsellNotificationController upsellNotificationController = new UpsellNotificationController(context, WorkerThread.get().getLooper(), featureFlags);
            sInstance = upsellNotificationController;
            upsellNotificationController.sendMessage(upsellNotificationController.obtainMessage(10));
        }
        return sInstance;
    }

    public static UpsellNotificationController getInstance() {
        return sInstance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMessageAsync(int i, Object obj) {
        sendMessage(obtainMessage(i, obj));
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        switch (message.what) {
            case 1:
                logd("EVENT_SERVICE_STATE_CHANGED");
                return;
            case 2:
                logd("EVENT_ACTIVE_DATA_SUB_CHANGED");
                return;
            case 3:
                logd("EVENT_CARRIER_CONFIG_CHANGED");
                SomeArgs someArgs = (SomeArgs) message.obj;
                try {
                    handleCarrierConfigChanged(((Integer) someArgs.arg1).intValue(), ((Integer) someArgs.arg2).intValue(), ((Integer) someArgs.arg3).intValue(), ((Integer) someArgs.arg4).intValue());
                    return;
                } finally {
                    someArgs.recycle();
                }
            case 4:
                logd("EVENT_ACTION_UPSELL_CLICKED");
                return;
            case 5:
                logd("EVENT_ACTION_UPSELL_DISMISSED");
                return;
            case 6:
                logd("EVENT_ACTION_UPSELL_SUPPRESSED");
                return;
            case 7:
                logd("EVENT_HYSTERESIS_TIMER_EXPIRED");
                return;
            case 8:
                logd("EVENT_SCAN_RETRY_TIMER_EXPIRED");
                return;
            case 9:
                logd("EVENT_TN_NETWORK_RESTORED");
                if (message.arg1 == this.mActiveDataSubId.get()) {
                    updateNotificationVisibility(message.arg1, false);
                    return;
                }
                return;
            case 10:
                logd("EVENT_UPSELL_NOTI_INIT");
                init();
                return;
            default:
                loge("Unexpected message: " + message.what);
                return;
        }
    }

    private void init() {
        logd("init()");
        registerForServiceStateChanged();
        registerActiveDataSubIdListener();
        registerCarrierConfigChangeListener();
        registerNotificationInteractionReceiver();
    }

    private void registerForServiceStateChanged() {
        for (Phone phone : PhoneFactory.getPhones()) {
            phone.registerForServiceStateChanged(this, 1, null);
        }
    }

    private void registerActiveDataSubIdListener() {
        this.mSubscriptionManager.addOnSubscriptionsChangedListener(this.mExecutor, new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.internal.telephony.satellite.UpsellNotificationController.1
            @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
            public void onSubscriptionsChanged() {
                int activeDataSubscriptionId = SubscriptionManager.getActiveDataSubscriptionId();
                UpsellNotificationController upsellNotificationController = UpsellNotificationController.this;
                upsellNotificationController.sendMessage(upsellNotificationController.obtainMessage(2, activeDataSubscriptionId, 0));
            }
        });
    }

    private void registerCarrierConfigChangeListener() {
        CarrierConfigManager carrierConfigManager = this.mCarrierConfigManager;
        if (carrierConfigManager != null) {
            carrierConfigManager.registerCarrierConfigChangeListener(this.mExecutor, new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.internal.telephony.satellite.UpsellNotificationController$$ExternalSyntheticLambda0
                @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
                public final void onCarrierConfigChanged(int i, int i2, int i3, int i4) {
                    this.f$0.lambda$registerCarrierConfigChangeListener$0(i, i2, i3, i4);
                }
            });
            logd("CarrierConfigChangeListener registered.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerCarrierConfigChangeListener$0(int i, int i2, int i3, int i4) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = Integer.valueOf(i);
        someArgsObtain.arg2 = Integer.valueOf(i2);
        someArgsObtain.arg3 = Integer.valueOf(i3);
        someArgsObtain.arg4 = Integer.valueOf(i4);
        sendMessageAsync(3, someArgsObtain);
    }

    private void registerNotificationInteractionReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.internal.telephony.satellite.ACTION_UPSELL_CLICKED");
        intentFilter.addAction("com.android.internal.telephony.satellite.ACTION_UPSELL_DISMISSED");
        intentFilter.addAction("com.android.internal.telephony.satellite.ACTION_UPSELL_SUPPRESSED");
        this.mContext.registerReceiver(this.mNotificationInteractionBroadcastReceiver, intentFilter, 2);
        logd("registerNotificationInteractionReceiver registered.");
    }

    private void loadCarrierConfigsForSubId(int i) {
        CarrierConfigManager carrierConfigManager = this.mCarrierConfigManager;
        if (carrierConfigManager == null) {
            loge("loadCarrierConfigsForSubId: mCarrierConfigManager is null return");
            return;
        }
        try {
            PersistableBundle configForSubId = carrierConfigManager.getConfigForSubId(i, "carrier_roaming_satellite_upsell_supported_bool", "carrier_roaming_satellite_upsell_notification_hysteresis_sec_int", "satellite_upsell_notification_throttle_hours_int", "carrier_roaming_satellite_upsell_notification_maximum_daily_count_int", "carrier_roaming_satellite_upsell_notification_maximum_monthly_count_int", "satellite_configs_per_plmn_bundle");
            if (configForSubId == null || configForSubId.equals(PersistableBundle.EMPTY)) {
                configForSubId = CarrierConfigManager.getDefaultConfig();
            }
            this.mCarrierConfigMap.put(Integer.valueOf(i), configForSubId);
            logd("loadCarrierConfigsForSubId: Loaded Upsell CarrierConfigs for subId " + i);
        } catch (Exception e) {
            plogd("loadCarrierConfigsForSubId: " + e);
        }
    }

    private void handleCarrierConfigChanged(int i, int i2, int i3, int i4) {
        logd("handleCarrierConfigChanged(): slotIndex(" + i + "), subId(" + i2 + "), carrierId(" + i3 + "), specificCarrierId(" + i4 + ")");
        if (i2 == -1) {
            return;
        }
        loadCarrierConfigsForSubId(i2);
    }

    private boolean isUpsellEligible(int i) {
        logd("isUpsellEligible: subId=" + i);
        return false;
    }

    public void updateNotificationVisibility(int i, boolean z) {
        logd("updateNotificationVisibility: " + z);
        if (z) {
            if (!isUpsellEligible(i)) {
                logd("Not allowed for subId: " + i);
                return;
            }
            displayUpsellNotification(i);
            return;
        }
        cancelUpsellNotification(i);
    }

    private void createNotificationChannel() {
        logd("createNotificationChannel");
        if (this.mNotificationManager == null) {
            logd("mNotificationManager is null");
            return;
        }
        NotificationChannel notificationChannel = new NotificationChannel(ConfigProviderAdaptor.DOMAIN_SATELLITE, "satelliteChannel", 3);
        this.mNotificationManager.createNotificationChannel(notificationChannel);
        notificationChannel.setSound(null, null);
        notificationChannel.setLockscreenVisibility(1);
        this.mNotificationManager.createNotificationChannel(notificationChannel);
        logd("createNotificationChannel: success");
    }

    private void displayUpsellNotification(int i) {
        logd("displayUpsellNotification: " + i);
        createNotificationChannel();
        this.mNotificationManager.notifyAsUser("SatelliteController", 1, new Notification.Builder(this.mContext, ConfigProviderAdaptor.DOMAIN_SATELLITE).setContentTitle(this.mContext.getString(R.string.wifi_calling_off_summary)).setContentText(this.mContext.getString(R.string.widget_default_package_name)).setSmallIcon(R.drawable.ic_btn_square_browser_zoom_page_overview_normal).setColor(this.mContext.getColor(R.color.system_notification_accent_color)).setVisibility(1).build(), UserHandle.ALL);
        this.mIsNotificationShowing.set(true);
    }

    private void cancelUpsellNotification(int i) {
        logd("cancelUpsellNotification: " + i);
        NotificationManager notificationManager = this.mNotificationManager;
        if (notificationManager != null) {
            notificationManager.cancelAsUser("SatelliteController", 1, UserHandle.ALL);
        }
        this.mIsNotificationShowing.set(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logd(String str) {
        Log.d("UpsellNotificationController", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void loge(String str) {
        Log.e("UpsellNotificationController", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void plogd(String str) {
        Log.d("UpsellNotificationController", str);
        PersistentLogger persistentLogger = this.mPersistentLogger;
        if (persistentLogger != null) {
            persistentLogger.debug("UpsellNotificationController", str);
        }
    }
}
