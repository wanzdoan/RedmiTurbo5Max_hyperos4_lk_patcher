package com.android.services.telephony.relay.timezone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.settings.TelephonySettings;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.android.telephony.Rlog;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.xiaomi.continuity.networking.NetworkingManager;

/* JADX INFO: loaded from: classes.dex */
public class TimeZoneRelayManager {
    private static final int EVENT_RELAY_DEATH = 1;
    private static final int EVENT_SCREEN_OFF_DELAY = 0;
    private static final int EVENT_SCREEN_STATE_CHANGED = 2;
    private static final String HYPER_CONNECT_OFF = "com.milink.service.connectivity.CONNECTION_SERVICE_CHANGED_OFF";
    private static final String HYPER_CONNECT_ON = "com.milink.service.connectivity.CONNECTION_SERVICE_CHANGED_ON";
    public static final int RETRY_DELAY_TIME = 30000;
    private static final String TAG_MANAGER = "[MiuiLTZP] [RelayManger]";
    private static final int UPDATE_DELAY_TIME = 8000;
    private static String mTimeZone;
    private static TimeZoneRelayManager sInstance;
    private Context mContext;
    private NetworkingManager mNetworkingManager;
    private TimeZoneRelayManagerHandler mThreadHandler;
    private static final String NO_RADIO_NORIL = "ro.radio.noril";
    private static final boolean IS_NOT_SUPPORT_MODEM = SystemProperties.getBoolean(NO_RADIO_NORIL, false);
    private static boolean hasCreateClient = false;
    private static boolean hasCreateServer = false;
    private static boolean screenOffLong = false;
    private static boolean screenOffDelayListen = false;
    private static boolean userPresent = false;
    ContentObserver contentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) { // from class: com.android.services.telephony.relay.timezone.TimeZoneRelayManager.1
        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            TimeZoneRelayManager.this.handleSettingsChanged();
        }
    };
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.services.telephony.relay.timezone.TimeZoneRelayManager.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Rlog.d(TimeZoneRelayManager.TAG_MANAGER, "mBroadcastReceiver action = " + action);
            if (TimeZoneRelayManager.HYPER_CONNECT_ON.equals(action) || TimeZoneRelayManager.HYPER_CONNECT_OFF.equals(action)) {
                TimeZoneRelayManager.this.handleSettingsChanged();
                return;
            }
            if ("android.intent.action.USER_PRESENT".equals(action)) {
                TimeZoneRelayManager.userPresent = true;
                TimeZoneRelayManager.this.handleSettingsChanged();
            } else if ("android.intent.action.TIMEZONE_CHANGED".equals(action)) {
                TimeZoneRelayManager.this.handTimeZoneChanged();
            }
        }
    };
    private final BaseEvent.IEventListener mDisplayListener = new BaseEvent.IEventListener() { // from class: com.android.services.telephony.relay.timezone.TimeZoneRelayManager.3
        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            if (baseEvent instanceof DeviceStateEvent.DisplayEvent) {
                DeviceStateEvent.DisplayEvent displayEvent = (DeviceStateEvent.DisplayEvent) baseEvent;
                if (displayEvent.isDefaultDisplay() && displayEvent.isDefaultDisplay() && TimeZoneRelayManager.this.mThreadHandler != null) {
                    TimeZoneRelayManager.this.mThreadHandler.sendMessage(TimeZoneRelayManager.this.mThreadHandler.obtainMessage(2, displayEvent));
                }
            }
        }
    };
    private final Runnable mRelayDeath = new Runnable() { // from class: com.android.services.telephony.relay.timezone.TimeZoneRelayManager.4
        @Override // java.lang.Runnable
        public void run() {
            if (TimeZoneRelayManager.this.mThreadHandler != null) {
                TimeZoneRelayManager.this.mThreadHandler.sendMessageDelayed(TimeZoneRelayManager.this.mThreadHandler.obtainMessage(1), 30000L);
            }
        }
    };

    public static synchronized TimeZoneRelayManager getInstance(Context context) {
        try {
            if (sInstance == null) {
                sInstance = new TimeZoneRelayManager(context);
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    private TimeZoneRelayManager(Context context) {
        this.mContext = context;
    }

    public void init() {
        registerAutoTimeZoneEnabled();
        registerBroadcastReceiver();
        handleSettingsChanged();
    }

    private void registerAutoTimeZoneEnabled() {
        PhoneGlobals.getInstance().getContentResolver().registerContentObserver(Settings.Global.getUriFor("auto_time_zone"), false, this.contentObserver);
    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        if (this.mContext != null) {
            intentFilter.addAction(HYPER_CONNECT_ON);
            intentFilter.addAction(HYPER_CONNECT_OFF);
            intentFilter.addAction("android.intent.action.USER_PRESENT");
            if (!IS_NOT_SUPPORT_MODEM) {
                intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
            }
            this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter, 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSettingsChanged() {
        if (this.mContext == null) {
            return;
        }
        try {
            boolean z = Settings.Global.getInt(PhoneGlobals.getInstance().getContentResolver(), "auto_time_zone") > 0;
            boolean zIsConnectivityServiceEnable = TelephonySettings.isConnectivityServiceEnable(PhoneGlobals.getInstance().getContentResolver());
            Rlog.d(TAG_MANAGER, "isAutoTimeZoneEnabled = " + z + "; isHyperConnectEnabled = " + zIsConnectivityServiceEnable);
            if (!userPresent) {
                Rlog.i(TAG_MANAGER, "userPresent not ready!!");
                return;
            }
            if (!IS_NOT_SUPPORT_MODEM) {
                if (z && zIsConnectivityServiceEnable) {
                    initTimeZoneRelayManagerHandler();
                    initRelayService();
                    registerHyperDestory();
                    return;
                } else {
                    this.mThreadHandler = null;
                    destoryRelayService();
                    unregisterHyperDestory();
                    return;
                }
            }
            if (z && zIsConnectivityServiceEnable) {
                initTimeZoneRelayManagerHandler();
                registerDisplayState();
                Rlog.d(TAG_MANAGER, "screenOffLong = " + screenOffLong);
                if (!screenOffLong) {
                    initRelayService();
                    registerHyperDestory();
                    return;
                } else {
                    destoryRelayService();
                    return;
                }
            }
            this.mThreadHandler = null;
            destoryRelayService();
            unregisterHyperDestory();
            unregisterDisplayState();
        } catch (Exception e) {
            Rlog.i(TAG_MANAGER, "handleSettingsChanged error = " + e);
        }
    }

    private void initTimeZoneRelayManagerHandler() {
        if (this.mThreadHandler == null) {
            HandlerThread handlerThread = new HandlerThread(TAG_MANAGER, 10);
            handlerThread.start();
            this.mThreadHandler = new TimeZoneRelayManagerHandler(handlerThread.getLooper());
        }
    }

    private synchronized void initRelayService() {
        Context context;
        Context context2;
        try {
            if (IS_NOT_SUPPORT_MODEM) {
                if (!hasCreateClient && (context2 = this.mContext) != null) {
                    hasCreateClient = true;
                    TimeZoneRelayChannelClient.getInstance(context2).addSubscribeService();
                    Rlog.d(TAG_MANAGER, "create client success");
                }
            } else if (!hasCreateServer && (context = this.mContext) != null) {
                hasCreateServer = true;
                TimeZoneRelayChannelServer.getInstance(context).addTimeZoneRelayService();
                Rlog.d(TAG_MANAGER, "create server success");
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private synchronized void destoryRelayService() {
        Context context;
        Context context2;
        try {
            if (IS_NOT_SUPPORT_MODEM) {
                if (hasCreateClient && (context2 = this.mContext) != null) {
                    hasCreateClient = false;
                    TimeZoneRelayChannelClient.getInstance(context2).removeSubscribeService();
                    Rlog.d(TAG_MANAGER, "remove client success");
                }
            } else if (hasCreateServer && (context = this.mContext) != null) {
                hasCreateServer = false;
                TimeZoneRelayChannelServer.getInstance(context).removeTimeZoneRelayService();
                Rlog.d(TAG_MANAGER, "remove server success");
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void registerDisplayState() {
        if (IS_NOT_SUPPORT_MODEM) {
            EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "display_state_changed", this.mDisplayListener);
        }
    }

    private void unregisterDisplayState() {
        if (IS_NOT_SUPPORT_MODEM) {
            EventHubManager.getInstance().unregisterListener(this.mDisplayListener);
            screenOffLong = false;
        }
    }

    private void registerHyperDestory() {
        if (this.mNetworkingManager == null) {
            NetworkingManager networkingManager = NetworkingManager.getInstance(this.mContext);
            this.mNetworkingManager = networkingManager;
            networkingManager.registerDeathCallback(this.mRelayDeath);
        }
    }

    private void unregisterHyperDestory() {
        NetworkingManager networkingManager = this.mNetworkingManager;
        if (networkingManager != null) {
            networkingManager.unregisterDeathCallback(this.mRelayDeath);
            this.mNetworkingManager = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handTimeZoneChanged() {
        destoryRelayService();
        TimeZoneRelayManagerHandler timeZoneRelayManagerHandler = this.mThreadHandler;
        if (timeZoneRelayManagerHandler != null) {
            timeZoneRelayManagerHandler.postDelayed(new Runnable() { // from class: com.android.services.telephony.relay.timezone.TimeZoneRelayManager.5
                @Override // java.lang.Runnable
                public void run() {
                    Rlog.d(TimeZoneRelayManager.TAG_MANAGER, "handTimeZoneChanged delay ready");
                    TimeZoneRelayManager.this.handleSettingsChanged();
                }
            }, 8000L);
        }
    }

    class TimeZoneRelayManagerHandler extends Handler {
        public TimeZoneRelayManagerHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                TimeZoneRelayManager.screenOffDelayListen = false;
                try {
                    if (TimeZoneRelayManager.screenOffLong || !MiuiPhoneUtils.isScreenOff(TimeZoneRelayManager.this.mContext)) {
                        return;
                    }
                    Rlog.d(TimeZoneRelayManager.TAG_MANAGER, "screen close > 60s, close client");
                    TimeZoneRelayManager.screenOffLong = true;
                    TimeZoneRelayManager.this.handleSettingsChanged();
                    return;
                } catch (Exception e) {
                    Rlog.i(TimeZoneRelayManager.TAG_MANAGER, "handleMessage error = " + e);
                    return;
                }
            }
            if (i == 1) {
                Rlog.d(TimeZoneRelayManager.TAG_MANAGER, "relay death, reset");
                TimeZoneRelayManager.hasCreateServer = false;
                TimeZoneRelayManager.hasCreateClient = false;
                TimeZoneRelayManager.screenOffLong = false;
                TimeZoneRelayManager.this.handleSettingsChanged();
                return;
            }
            if (i != 2) {
                return;
            }
            DeviceStateEvent.DisplayEvent displayEvent = (DeviceStateEvent.DisplayEvent) message.obj;
            if (displayEvent.isScreenOn()) {
                TimeZoneRelayManager.screenOffLong = false;
            } else {
                if (!displayEvent.isScreenOff() || TimeZoneRelayManager.this.mThreadHandler == null || TimeZoneRelayManager.screenOffDelayListen) {
                    return;
                }
                TimeZoneRelayManager.screenOffDelayListen = true;
                TimeZoneRelayManager.this.mThreadHandler.sendMessageDelayed(TimeZoneRelayManager.this.mThreadHandler.obtainMessage(0), RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
            }
        }
    }
}
