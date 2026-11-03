package com.android.phone.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.devicestate.DeviceStateManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.telephony.Rlog;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import miui.os.HyperOSCustFeatureResolve;
import miui.process.ForegroundInfo;
import miui.process.ProcessManager;

/* JADX INFO: loaded from: classes.dex */
public class MiuiCurrentStateMonitor {
    private static MiuiCurrentStateMonitor mInstance;
    private List CONV_VIDEO_WHITE_LIST_DEFAULT;
    private List FILE_TRANSFER_WHITE_LIST_DEFAULT;
    private List GAMING_WHITE_LIST_DEFAULT;
    private List SHORT_VIDEO_WHITE_LIST_DEFAULT;
    private List STREAMING_VIDEO_WHITE_LIST_DEFAULT;
    private List WEB_BROWSING_WHITE_LIST_DEFAULT;
    private ConnectivityManager mConnManager;
    private boolean mFoldState;
    private ForegroundInfo mForegroundInfo;
    private Handler mHandler;
    private boolean mIsScreenOn;
    private boolean mIsSleepMode;
    private Location mLocation;
    private Network mNetwork;
    private long mRxBytesBefore;
    private double mRxRate;
    private long mTxBytesBefore;
    private double mTxRate;
    private long mlastTrafficTime;
    private DeviceStateManager mDeviceStateManager = null;
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.utils.MiuiCurrentStateMonitor.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null || !intent.getAction().equals("com.miui.powerkeeper_sleep_changed")) {
                return;
            }
            MiuiCurrentStateMonitor.this.mHandler.sendMessage(MiuiCurrentStateMonitor.this.mHandler.obtainMessage(2, intent));
        }
    };
    private final ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback(1) { // from class: com.android.phone.utils.MiuiCurrentStateMonitor.2
        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(Network network) {
            if (network != null) {
                MiuiCurrentStateMonitor.this.mNetwork = network;
                MiuiCurrentStateMonitor.this.mHandler.sendMessageDelayed(MiuiCurrentStateMonitor.this.mHandler.obtainMessage(5), 2000L);
            }
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            MiuiCurrentStateMonitor.this.mNetwork = null;
            MiuiCurrentStateMonitor.this.mHandler.removeMessages(5);
        }
    };
    private final BaseEvent.IEventListener mEventListener = new BaseEvent.IEventListener() { // from class: com.android.phone.utils.MiuiCurrentStateMonitor.3
        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            if (baseEvent instanceof DeviceStateEvent.DisplayEvent) {
                if (((DeviceStateEvent.DisplayEvent) baseEvent).isDefaultDisplay()) {
                    MiuiCurrentStateMonitor.this.mHandler.sendMessage(MiuiCurrentStateMonitor.this.mHandler.obtainMessage(1));
                }
            } else if (baseEvent instanceof DeviceStateEvent.AppStateEvent) {
                MiuiCurrentStateMonitor.this.mForegroundInfo = ((DeviceStateEvent.AppStateEvent) baseEvent).getForegroundInfo();
            }
        }
    };
    private LocationListener mLocationListener = new LocationListener() { // from class: com.android.phone.utils.MiuiCurrentStateMonitor.4
        @Override // android.location.LocationListener
        public void onProviderDisabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onProviderEnabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onStatusChanged(String str, int i, Bundle bundle) {
        }

        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) {
            if (location != null) {
                MiuiCurrentStateMonitor.this.mHandler.sendMessage(MiuiCurrentStateMonitor.this.mHandler.obtainMessage(4, location));
            }
        }
    };
    private Context mContext = PhoneGlobals.getInstance();

    /* JADX INFO: Access modifiers changed from: private */
    public void startUpdateLocationNow() {
    }

    private MiuiCurrentStateMonitor() {
        HandlerThread handlerThread = new HandlerThread("MiuiNetworkMonitor");
        handlerThread.start();
        this.mHandler = new WorkHandler(handlerThread.getLooper());
        this.mConnManager = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        this.mForegroundInfo = ProcessManager.getForegroundInfo();
        register();
        initValue();
        initWhilteListDefault();
    }

    private void initValue() {
        this.mRxBytesBefore = TrafficStats.getMobileRxBytes();
        this.mTxBytesBefore = TrafficStats.getMobileTxBytes();
        this.mlastTrafficTime = System.currentTimeMillis();
    }

    public static MiuiCurrentStateMonitor getInstance() {
        if (mInstance == null) {
            mInstance = new MiuiCurrentStateMonitor();
        }
        return mInstance;
    }

    private void register() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.miui.powerkeeper_sleep_changed");
        this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter, 2);
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "display_state_changed", this.mEventListener);
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "foreground_changed", this.mEventListener, this.mHandler);
        startUpdateLocation();
        if (MiuiPhoneUtils.isDeviceSupportFlip() || MiuiPhoneUtils.isDeviceSupportFold()) {
            logd("registerCallback: FoldStateListener");
            DeviceStateManager deviceStateManager = (DeviceStateManager) this.mContext.getSystemService(DeviceStateManager.class);
            this.mDeviceStateManager = deviceStateManager;
            deviceStateManager.registerCallback(new HandlerExecutor(this.mHandler), new DeviceStateManager.FoldStateListener(this.mContext, new Consumer() { // from class: com.android.phone.utils.MiuiCurrentStateMonitor$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$register$0((Boolean) obj);
                }
            }));
        }
        this.mConnManager.registerNetworkCallback(new NetworkRequest.Builder().addTransportType(0).addCapability(12).build(), this.mNetworkCallback, this.mHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$register$0(Boolean bool) {
        updateFoldState(bool.booleanValue());
    }

    class WorkHandler extends Handler {
        public WorkHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            MiuiCurrentStateMonitor.this.logd("handleMessage event:" + message.what);
            int i = message.what;
            if (i == 1) {
                boolean zIsScreenOn = MiuiPhoneUtils.isScreenOn(MiuiCurrentStateMonitor.this.mContext);
                if (zIsScreenOn != MiuiCurrentStateMonitor.this.mIsScreenOn) {
                    MiuiCurrentStateMonitor.this.mIsScreenOn = zIsScreenOn;
                    boolean z = MiuiCurrentStateMonitor.this.mIsScreenOn;
                    MiuiCurrentStateMonitor miuiCurrentStateMonitor = MiuiCurrentStateMonitor.this;
                    if (z) {
                        miuiCurrentStateMonitor.mHandler.sendMessageDelayed(MiuiCurrentStateMonitor.this.mHandler.obtainMessage(5), 2000L);
                        return;
                    } else {
                        miuiCurrentStateMonitor.mHandler.removeMessages(5);
                        return;
                    }
                }
                return;
            }
            if (i == 2) {
                MiuiCurrentStateMonitor.this.mIsSleepMode = 1 == ((Intent) message.obj).getIntExtra(MEModuleSignalT1s.EXTRA_STATE, 2);
            } else {
                if (i == 3) {
                    MiuiCurrentStateMonitor.this.startUpdateLocationNow();
                    return;
                }
                if (i != 4) {
                    if (i != 5) {
                        return;
                    }
                    MiuiCurrentStateMonitor.this.pullNetworkSpeed();
                } else {
                    LocationManager locationManager = (LocationManager) MiuiCurrentStateMonitor.this.mContext.getSystemService("location");
                    if (locationManager != null) {
                        locationManager.removeUpdates(MiuiCurrentStateMonitor.this.mLocationListener);
                    }
                    MiuiCurrentStateMonitor.this.mLocation = (Location) message.obj;
                }
            }
        }
    }

    public void startUpdateLocation() {
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(3));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pullNetworkSpeed() {
        if (!this.mIsScreenOn || this.mNetwork == null) {
            StringBuilder sb = new StringBuilder("mIsScreenOn:");
            sb.append(this.mIsScreenOn);
            sb.append(", mNetwork is null:");
            sb.append(this.mNetwork == null);
            logd(sb.toString());
            return;
        }
        long mobileRxBytes = TrafficStats.getMobileRxBytes();
        long mobileTxBytes = TrafficStats.getMobileTxBytes();
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j = mobileRxBytes - this.mRxBytesBefore;
        long j2 = mobileTxBytes - this.mTxBytesBefore;
        double d = (jCurrentTimeMillis - this.mlastTrafficTime) / 1000.0d;
        this.mRxRate = j / d;
        this.mTxRate = j2 / d;
        this.mRxBytesBefore = mobileRxBytes;
        this.mTxBytesBefore = mobileTxBytes;
        this.mlastTrafficTime = jCurrentTimeMillis;
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(5), 2000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String str) {
        Rlog.d("MiuiCurrentStateMonitor", str);
    }

    private List safeGetStringArray(String str) {
        List stringArray = HyperOSCustFeatureResolve.getStringArray(str);
        return stringArray == null ? new ArrayList() : stringArray;
    }

    private void initWhilteListDefault() {
        this.CONV_VIDEO_WHITE_LIST_DEFAULT = safeGetStringArray("sns_datastall_conv_video_white_list_default");
        this.STREAMING_VIDEO_WHITE_LIST_DEFAULT = safeGetStringArray("sns_datastall_streaming_video_white_list_default");
        this.WEB_BROWSING_WHITE_LIST_DEFAULT = safeGetStringArray("sns_datastall_web_browsing_white_list_default");
        this.FILE_TRANSFER_WHITE_LIST_DEFAULT = safeGetStringArray("sns_datastall_file_transfer_white_list_default");
        this.GAMING_WHITE_LIST_DEFAULT = safeGetStringArray("sns_datastall_gaming_white_list_default");
        this.SHORT_VIDEO_WHITE_LIST_DEFAULT = safeGetStringArray("sns_datastall_short_video_white_list_default");
    }

    public enum AppType {
        NONE(0),
        CONV_AUDIO(1),
        CONV_VIDEO(2),
        STREAMING_AUDIO(3),
        STREAMING_VIDEO(4),
        TYPE_GAMING(5),
        WEB_BROWSING(6),
        FILE_TRANSFER(7);

        private int value;

        AppType(int i) {
            this.value = i;
        }

        public int getValue() {
            return this.value;
        }
    }

    public AppType getForegroundAppType() {
        ForegroundInfo foregroundInfo = this.mForegroundInfo;
        if (foregroundInfo != null) {
            return getAppTypeByPckName(foregroundInfo.mForegroundPackageName);
        }
        return AppType.NONE;
    }

    public AppType getAppTypeByPckName(String str) {
        AppType appType = AppType.NONE;
        if (this.CONV_VIDEO_WHITE_LIST_DEFAULT.contains(str)) {
            return AppType.CONV_VIDEO;
        }
        if (this.STREAMING_VIDEO_WHITE_LIST_DEFAULT.contains(str) || this.SHORT_VIDEO_WHITE_LIST_DEFAULT.contains(str)) {
            return AppType.STREAMING_VIDEO;
        }
        if (this.WEB_BROWSING_WHITE_LIST_DEFAULT.contains(str)) {
            return AppType.WEB_BROWSING;
        }
        if (this.FILE_TRANSFER_WHITE_LIST_DEFAULT.contains(str)) {
            return AppType.FILE_TRANSFER;
        }
        return this.GAMING_WHITE_LIST_DEFAULT.contains(str) ? AppType.TYPE_GAMING : appType;
    }

    public String getForegroundPackageName() {
        ForegroundInfo foregroundInfo = this.mForegroundInfo;
        if (foregroundInfo != null) {
            return foregroundInfo.mForegroundPackageName;
        }
        return null;
    }

    public Location getLocation() {
        return this.mLocation;
    }

    public double getRxRate() {
        return this.mRxRate;
    }

    public void updateFoldState(boolean z) {
        if (this.mFoldState != z) {
            this.mFoldState = z;
            logd("Fold state changed to: " + z);
        }
    }

    public boolean getFoldState() {
        return this.mFoldState;
    }
}
