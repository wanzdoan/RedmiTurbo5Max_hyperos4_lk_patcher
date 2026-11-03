package com.android.phone.dsda;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.data.DataNetwork;
import com.android.internal.telephony.data.DataNetworkController;
import com.android.internal.telephony.data.MiuiDataControllerImpl;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import miui.telephony.DefaultSimManager;
import miui.telephony.MiuiTelephony;

/* JADX INFO: loaded from: classes.dex */
public class MiuiDualMobileTrafficStats {
    private ConnectivityManager mConnectivityManager;
    private Context mContext;
    private DataNetworkController mDataNetworkController;
    private MiuiDataControllerImpl mDataNetworkImpl;
    protected DualMobileTrafficStatsHandler mHandler;
    private long mLastMobileRxBytes;
    private long mLastMobileTxBytes;
    private final Phone mPhone;
    private int mPhoneId;
    private HandlerThread mWorkThread;
    private boolean mIsScreenOn = false;
    private boolean mIsMobileDataOn = false;
    private boolean mIsOnDefaultRoute = false;
    private String mInterface = "";
    private boolean mLastTrafficValid = true;
    private int mDefaultDataSlotId = -1;
    private int mDataActivity = 0;
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.dsda.MiuiDualMobileTrafficStats.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            NetworkInfo networkInfo;
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            MiuiDualMobileTrafficStats.this.log("receive action: ".concat(action));
            if (action.equals("android.net.conn.CONNECTIVITY_CHANGE") && (networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo")) != null && networkInfo.getType() == 0) {
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED && !MiuiDualMobileTrafficStats.this.mIsMobileDataOn) {
                    MiuiDualMobileTrafficStats.this.mIsMobileDataOn = true;
                    MiuiDualMobileTrafficStats.this.mHandler.obtainMessage(4).sendToTarget();
                } else if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED && MiuiDualMobileTrafficStats.this.mIsMobileDataOn) {
                    MiuiDualMobileTrafficStats.this.mIsMobileDataOn = false;
                    MiuiDualMobileTrafficStats.this.mHandler.obtainMessage(4).sendToTarget();
                }
            }
        }
    };
    private final BaseEvent.IEventListener mDisplayListener = new BaseEvent.IEventListener() { // from class: com.android.phone.dsda.MiuiDualMobileTrafficStats.3
        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            if (baseEvent instanceof DeviceStateEvent.DisplayEvent) {
                MiuiDualMobileTrafficStats.this.mHandler.obtainMessage(2).sendToTarget();
            }
        }
    };
    private final ConnectivityManager.NetworkCallback mDefaultNetworkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.android.phone.dsda.MiuiDualMobileTrafficStats.4
        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            MiuiDualMobileTrafficStats.this.mHandler.obtainMessage(3, networkCapabilities).sendToTarget();
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            MiuiDualMobileTrafficStats.this.mHandler.obtainMessage(3, null).sendToTarget();
        }
    };

    public MiuiDualMobileTrafficStats(Phone phone) {
        this.mPhoneId = -1;
        this.mPhone = phone;
        this.mPhoneId = phone.getPhoneId();
        this.mContext = phone.getContext();
        this.mDataNetworkImpl = (MiuiDataControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiDataControllerImpl.class, phone.getPhoneId());
        this.mDataNetworkController = phone.getDataNetworkController();
        HandlerThread handlerThread = new HandlerThread("MiuiDualMobileTrafficStats");
        this.mWorkThread = handlerThread;
        handlerThread.start();
        DualMobileTrafficStatsHandler dualMobileTrafficStatsHandler = new DualMobileTrafficStatsHandler(this.mWorkThread.getLooper());
        this.mHandler = dualMobileTrafficStatsHandler;
        dualMobileTrafficStatsHandler.sendEmptyMessage(1);
    }

    public class DualMobileTrafficStatsHandler extends Handler {
        public DualMobileTrafficStatsHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    MiuiDualMobileTrafficStats.this.initAction();
                    break;
                case 2:
                    MiuiDualMobileTrafficStats.this.handleScreenStateChanged();
                    break;
                case 3:
                    MiuiDualMobileTrafficStats.this.handleDefaultNetworkChanged((NetworkCapabilities) message.obj);
                    break;
                case 4:
                    MiuiDualMobileTrafficStats.this.handleTrafficStatsPollConditionChanged();
                    break;
                case 5:
                    MiuiDualMobileTrafficStats.this.startNetStatPoll();
                    break;
                case 6:
                    MiuiDualMobileTrafficStats.this.updateInterfaceName();
                    MiuiDualMobileTrafficStats.this.handleTrafficStatsPollConditionChanged();
                    break;
                default:
                    Log.e("MiuiDualMobileTrafficStats", "invalid message " + message.what);
                    break;
            }
        }
    }

    public void updateInterfaceName() {
        MiuiDataControllerImpl miuiDataControllerImpl = this.mDataNetworkImpl;
        if (miuiDataControllerImpl == null) {
            return;
        }
        try {
            DataNetwork dataNetworkFindActiveInternetDataNetwork = miuiDataControllerImpl.findActiveInternetDataNetwork();
            if (dataNetworkFindActiveInternetDataNetwork != null) {
                this.mInterface = dataNetworkFindActiveInternetDataNetwork.getLinkProperties().getInterfaceName();
                log("updateInterfaceName, mInterface: " + this.mInterface);
                return;
            }
            this.mInterface = "";
        } catch (ArrayIndexOutOfBoundsException e) {
            this.mInterface = "";
            Log.e("MiuiDualMobileTrafficStats", "Can not findActiveInternetDataNetwork:" + e.getMessage());
        }
    }

    public void updateDefaultDataSlotId() {
        this.mDefaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        handleTrafficStatsPollConditionChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initAction() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService(ConnectivityManager.class);
        this.mConnectivityManager = connectivityManager;
        connectivityManager.registerDefaultNetworkCallback(this.mDefaultNetworkCallback, this.mHandler);
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "display_state_changed", this.mDisplayListener, this.mHandler);
        handleScreenStateChanged();
        registerAllEvents();
    }

    private void registerAllEvents() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
        DataNetworkController dataNetworkController = this.mDataNetworkController;
        final DualMobileTrafficStatsHandler dualMobileTrafficStatsHandler = this.mHandler;
        Objects.requireNonNull(dualMobileTrafficStatsHandler);
        dataNetworkController.registerDataNetworkControllerCallback(new DataNetworkController.DataNetworkControllerCallback(new Executor() { // from class: com.android.phone.dsda.MiuiDualMobileTrafficStats$$ExternalSyntheticLambda0
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                dualMobileTrafficStatsHandler.post(runnable);
            }
        }) { // from class: com.android.phone.dsda.MiuiDualMobileTrafficStats.1
            public void onInternetDataNetworkValidationStatusChanged(int i) {
                MiuiDualMobileTrafficStats.this.log("onInternetDataNetworkValidationStatusChanged validationStatus = " + i);
            }

            public void onConnectedInternetDataNetworksChanged(Set set) {
                MiuiDualMobileTrafficStats.this.mHandler.obtainMessage(6).sendToTarget();
                MiuiDualMobileTrafficStats.this.log("onConnectedInternetDataNetworksChanged");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleScreenStateChanged() {
        boolean zIsScreenOn = MiuiPhoneUtils.isScreenOn(this.mContext);
        if (this.mIsScreenOn == zIsScreenOn) {
            return;
        }
        this.mIsScreenOn = zIsScreenOn;
        handleTrafficStatsPollConditionChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleTrafficStatsPollConditionChanged() {
        log("mIsScreenOn: " + this.mIsScreenOn + ", mIsMobileDataOn: " + this.mIsMobileDataOn + ", mIsOnDefaultRoute: " + this.mIsOnDefaultRoute + ", mDefaultDataSlotId: " + this.mDefaultDataSlotId + ", mPhoneId: " + this.mPhoneId);
        this.mHandler.removeMessages(5);
        if (!(MiuiPhoneUtils.isValidatePhoneId(this.mPhoneId) && this.mPhoneId == this.mDefaultDataSlotId) && this.mIsScreenOn && this.mIsMobileDataOn && this.mIsOnDefaultRoute) {
            startNetStatPoll();
        }
    }

    protected void startNetStatPoll() {
        if (TextUtils.isEmpty(this.mInterface)) {
            return;
        }
        long txBytes = TrafficStats.getTxBytes(this.mInterface);
        long rxBytes = TrafficStats.getRxBytes(this.mInterface);
        long j = txBytes - this.mLastMobileTxBytes;
        long j2 = rxBytes - this.mLastMobileRxBytes;
        this.mHandler.sendEmptyMessageDelayed(5, 1000L);
        this.mLastMobileTxBytes = txBytes;
        this.mLastMobileRxBytes = rxBytes;
        int i = 0;
        boolean z = j >= 0 && j2 >= 0;
        if (!this.mLastTrafficValid || !z) {
            this.mLastTrafficValid = z;
            log(" run into invalid traffic count");
            return;
        }
        if (j > 0 && j2 > 0) {
            i = 3;
        } else if (j2 > 0) {
            i = 1;
        } else if (j > 0) {
            i = 2;
        }
        log("txBytesDelta: " + j + ", rxBytesDelta: " + j2 + ", mDataActivity: " + this.mDataActivity + ", dataActivity: " + i);
        if (this.mDataActivity != i) {
            this.mDataActivity = i;
            this.mDataNetworkController.updateDataActivity(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDefaultNetworkChanged(NetworkCapabilities networkCapabilities) {
        boolean zHasTransport = networkCapabilities != null ? networkCapabilities.hasTransport(0) : false;
        if (this.mIsOnDefaultRoute == zHasTransport) {
            return;
        }
        this.mIsOnDefaultRoute = zHasTransport;
        handleTrafficStatsPollConditionChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("MiuiDualMobileTrafficStats[" + this.mPhoneId + "] ", str);
    }
}
