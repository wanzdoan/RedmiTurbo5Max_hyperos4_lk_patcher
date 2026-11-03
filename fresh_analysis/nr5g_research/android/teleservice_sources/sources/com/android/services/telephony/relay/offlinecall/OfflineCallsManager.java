package com.android.services.telephony.relay.offlinecall;

import android.accounts.AccountManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;
import com.android.bluetooth.ble.app.IVirtualCallConnectionService;
import com.android.bluetooth.ble.app.IVirtualCallEventListener;
import com.android.bluetooth.ble.app.virtualCall.VirtualCallDevice;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.android.telephony.Rlog;
import gov.nist.javax.sip.address.GenericURI;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import miui.telephony.SubscriptionInfo;
import miuix.animation.internal.TransitionInfo;

/* JADX INFO: loaded from: classes.dex */
public class OfflineCallsManager {
    private static final String ACTION_OFFLINE_CALL_STATE = "android.telephony.offlinecall.state";
    private static final String ACTION_XIAOMI_ACCOUNT_LOGIN_POST_CHANGED = "android.accounts.LOGIN_ACCOUNTS_POST_CHANGED";
    private static final String ACTION_XIAOMI_ACCOUNT_LOGIN_PRE_CHANGED = "android.accounts.LOGIN_ACCOUNTS_PRE_CHANGED";
    private static final int ALLOW_ALL = 2;
    private static final int ALLOW_CLOSED = 0;
    private static final int ALLOW_FRIEND = 1;
    private static final String BLUETOOTH_OFFLINE_CHAT = "log.tag.VIRTUALCALL_ENABLE";
    private static final int CONNECTION_ROLE_CLIENT = 1;
    private static final int CONNECTION_ROLE_SERVER = 2;
    private static final int EVENT_CALL_RINGING_HAS_CALL = 3;
    private static final int EVENT_CALL_RINGING_NOT_MATCH_CONTACTS = 2;
    private static final int EVENT_NO_ANSWER = 0;
    private static final int EVENT_NO_DIALING = 1;
    private static final String EXTRA_STATUS = "state";
    private static final String LOG_TAG = "OfflineCallsManager";
    private static final int NO_ANSWER_TIME = 60000;
    private static final int NO_DIALING_TIME = 32000;
    private static final String OFFLINE_CALLS_ENABLED = "offline_chat";
    private static final String OFFLINE_CALLS_ENABLED_TYPE = "bluetooth_call_self";
    private static final String OFFLINE_CALLS_RSSI = "offline_calls_rssi";
    private static final int OFFLINE_CALL_ACTIVE = 4;
    private static final int OFFLINE_CALL_ANSWERED = 3;
    private static final int OFFLINE_CALL_CHANNEL_CONNECTED = 2;
    private static final int OFFLINE_CALL_CHANNEL_DISCONNECTED = 0;
    private static final int OFFLINE_CALL_CONNECTING = 0;
    private static final int OFFLINE_CALL_DIALING = 1;
    private static final int OFFLINE_CALL_DISCONNECTED = 6;
    private static final int OFFLINE_CALL_DISCONNECTING = 5;
    private static final int OFFLINE_CALL_IDLE = -1;
    private static final int OFFLINE_CALL_RINGING = 2;
    private static final int OFFLINE_CALL_RINGING_HAS_CALL = 3;
    private static final int OFFLINE_CALL_RINGING_MATCH = 0;
    private static final int OFFLINE_CALL_RINGING_NOT_MATCH_CONTACTS = 2;
    private static final int OFFLINE_CALL_RSSI_GREAT = 1;
    private static final int OFFLINE_CALL_RSSI_POOR = 0;
    private static final int OFFLINE_CALL_RSSI_THRESHOLD = -70;
    private static final String PACKAGE_BLUETOOTH = "com.xiaomi.bluetooth";
    private static final int REJECT_TIME = 500;
    private static final int SCAN_MODE_BALANCED = 1;
    private static final int SCAN_MODE_LOW_LATENCY = 2;
    private static final int SCAN_MODE_LOW_POWER = 0;
    private static final String SERVICE_BLUETOOTH = "com.android.bluetooth.ble.app.virtualCall.MiuiVirtualCallService";
    private static final int SLOT_0 = 0;
    private static final int SLOT_1 = 1;
    private static final int SUB_PHONE_NUMBER_LENGTH = 10;
    private static OfflineCallsManager sInstance;
    private Context mContext;
    private OfflineCallsPlayer mOfflineCallsPlayer;
    private OfflineCallsRecorder mOfflineCallsRecorder;
    private IVirtualCallConnectionService mOfflineConnectionService;
    private OfflineManager mOfflineManager;
    private TelephonyManager mTelephonyManager;
    private OfflineCallsManagerHandler mThreadHandler;
    private VitrualCallEventListener mVitrualCallEventListener;
    private int mOfflineCallState = -1;
    private String mSim1Num = "";
    private String mSim2Num = "";
    private String mConnectCallNum = "";
    private String mMoBleAddr = "";
    private String mMtBleAddr = "";
    private boolean mHasBinded = false;
    ContentObserver contentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) { // from class: com.android.services.telephony.relay.offlinecall.OfflineCallsManager.1
        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            OfflineCallsManager.this.handleSettingsChanged();
        }
    };
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.services.telephony.relay.offlinecall.OfflineCallsManager.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Rlog.d(OfflineCallsManager.LOG_TAG, "mBroadcastReceiver action = " + action);
            if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(action)) {
                OfflineCallsManager.this.handleSettingsChanged();
                return;
            }
            if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                String stringExtra = intent.getStringExtra("ss");
                Rlog.d(OfflineCallsManager.LOG_TAG, "mBroadcastReceiver iccState = " + stringExtra);
                if ("ABSENT".equals(stringExtra) || "LOADED".equals(stringExtra)) {
                    OfflineCallsManager.this.handleSettingsChanged();
                    return;
                }
                return;
            }
            if (OfflineCallsManager.ACTION_XIAOMI_ACCOUNT_LOGIN_PRE_CHANGED.equals(action) || OfflineCallsManager.ACTION_XIAOMI_ACCOUNT_LOGIN_POST_CHANGED.equals(action)) {
                OfflineCallsManager.this.handleSettingsChanged();
            }
        }
    };
    private final ServiceConnection mServiceConnection = new ServiceConnection() { // from class: com.android.services.telephony.relay.offlinecall.OfflineCallsManager.3
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (iBinder != null) {
                OfflineCallsManager.this.mOfflineConnectionService = IVirtualCallConnectionService.Stub.asInterface(iBinder);
                Rlog.d(OfflineCallsManager.LOG_TAG, "connection created, binding local service.");
                try {
                    if (OfflineCallsManager.this.mOfflineConnectionService != null) {
                        OfflineCallsManager.this.mVitrualCallEventListener = OfflineCallsManager.this.new VitrualCallEventListener();
                        OfflineCallsManager.this.mOfflineConnectionService.registerEventListener(OfflineCallsManager.this.mVitrualCallEventListener);
                    }
                } catch (Exception e) {
                    Rlog.e(OfflineCallsManager.LOG_TAG, "onServiceConnected error = " + e);
                }
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            Rlog.d(OfflineCallsManager.LOG_TAG, "connection disconnected, cleaning local binding.");
            OfflineCallsManager.this.mOfflineConnectionService = null;
            OfflineCallsManager.this.mVitrualCallEventListener = null;
            OfflineCallsManager.this.reset();
        }
    };

    public static synchronized OfflineCallsManager getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new OfflineCallsManager();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    private OfflineCallsManager() {
    }

    public OfflineManager getOfflineManager() {
        return this.mOfflineManager;
    }

    public void init(Context context) {
        this.mContext = context;
        this.mOfflineManager = new OfflineManager();
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
        HandlerThread handlerThread = new HandlerThread(LOG_TAG, 10);
        handlerThread.start();
        this.mThreadHandler = new OfflineCallsManagerHandler(handlerThread.getLooper());
        registerOfflineCallsEnabled();
        registerBroadcastReceiver();
        handleSettingsChanged();
    }

    private void registerOfflineCallsEnabled() {
        PhoneGlobals.getInstance().getContentResolver().registerContentObserver(Settings.Global.getUriFor(OFFLINE_CALLS_ENABLED), false, this.contentObserver);
    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        if (this.mContext != null) {
            intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
            intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
            intentFilter.addAction(ACTION_XIAOMI_ACCOUNT_LOGIN_PRE_CHANGED);
            intentFilter.addAction(ACTION_XIAOMI_ACCOUNT_LOGIN_POST_CHANGED);
            this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter, null, this.mThreadHandler, 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSettingsChanged() {
        try {
            int i = SystemProperties.getInt(BLUETOOTH_OFFLINE_CHAT, -1);
            Context context = this.mContext;
            boolean z = true;
            boolean z2 = context != null && Settings.Global.getInt(context.getContentResolver(), OFFLINE_CALLS_ENABLED, 0) == 1;
            BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
            boolean zIsEnabled = defaultAdapter != null ? defaultAdapter.isEnabled() : false;
            boolean z3 = MiuiPhoneUtils.getInsertedSimCount() != 0;
            AccountManager accountManager = AccountManager.get(this.mContext);
            boolean z4 = accountManager != null && accountManager.getAccountsByType("com.xiaomi").length > 0;
            Rlog.d(LOG_TAG, "handleSettingsChanged isOfflineCallsEnabled = " + z2 + ",\n isOfflineCallsEnabledProp = " + i + ",\n isBluetoothEnabled = " + zIsEnabled + ",\n isHasSim = " + z3 + ",\n isXiaomiAccountLoggedIn = " + z4);
            if (this.mOfflineManager == null || !OfflineManager.isDemoDevice()) {
                z = false;
            }
            if (z2 && i != -1 && zIsEnabled && (z || (z3 && z4))) {
                bindBluetoothService();
            } else {
                unbindBluetoothService();
            }
        } catch (Exception e) {
            Rlog.e(LOG_TAG, "handleSettingsChanged error = " + e);
        }
    }

    private void bindBluetoothService() {
        Rlog.d(LOG_TAG, "bindBluetoothService mHasBinded = " + this.mHasBinded);
        if (this.mContext == null || this.mHasBinded) {
            return;
        }
        this.mHasBinded = true;
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(PACKAGE_BLUETOOTH, SERVICE_BLUETOOTH));
        this.mContext.bindService(intent, this.mServiceConnection, 1);
    }

    private void unbindBluetoothService() {
        ServiceConnection serviceConnection;
        Rlog.d(LOG_TAG, "unbindBluetoothService mHasBinded = " + this.mHasBinded);
        if (this.mHasBinded) {
            hangupOfflineCall();
            this.mHasBinded = false;
            Context context = this.mContext;
            if (context == null || (serviceConnection = this.mServiceConnection) == null) {
                return;
            }
            context.unbindService(serviceConnection);
        }
    }

    public class VitrualCallEventListener extends IVirtualCallEventListener.Stub {
        public VitrualCallEventListener() {
        }

        @Override // com.android.bluetooth.ble.app.IVirtualCallEventListener
        public void onConnectStateChanged(String str, int i, int i2) {
            Rlog.d(OfflineCallsManager.LOG_TAG, "onConnectStateChanged state = " + i + "; isClientOrServer = " + i2 + "; mOfflineCallState = " + OfflineCallsManager.this.mOfflineCallState);
            if (TextUtils.isEmpty(OfflineCallsManager.this.mMoBleAddr) || !OfflineCallsManager.this.mMoBleAddr.equals(str) || i2 != 1) {
                if (i2 == 2) {
                    if (2 == i) {
                        OfflineCallsManager.this.mMtBleAddr = str;
                        if (OfflineCallsManager.this.mOfflineManager != null) {
                            OfflineCallsManager.this.mOfflineManager.enableOfflinePhoneAccount(true);
                            return;
                        }
                        return;
                    }
                    if (i == 0) {
                        OfflineCallsManager.this.reset();
                        if (OfflineCallsManager.this.mOfflineManager != null) {
                            OfflineCallsManager.this.mOfflineManager.enableOfflinePhoneAccount(false);
                            OfflineCallsManager.this.disconnectCall();
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            if (2 != i) {
                if (i == 0) {
                    OfflineCallsManager.this.reset();
                    if (OfflineCallsManager.this.mOfflineManager != null) {
                        OfflineCallsManager.this.mOfflineManager.enableOfflinePhoneAccount(false);
                        OfflineCallsManager.this.disconnectCall();
                        return;
                    }
                    return;
                }
                return;
            }
            if (OfflineCallsManager.this.mOfflineConnectionService == null || OfflineCallsManager.this.mOfflineCallState != 0) {
                return;
            }
            try {
                OfflineCallsManager.this.updateOfflineCallState(1);
                OfflineCallsManager.this.mOfflineConnectionService.setScanMode(1);
                OfflineCallsManager.this.mOfflineConnectionService.startScan();
                StringBuilder sb = new StringBuilder("onConnectStateChanged requestTheCall mSim1Num = ");
                OfflineCallsManager offlineCallsManager = OfflineCallsManager.this;
                sb.append(offlineCallsManager.getPhoneNumberSubString(offlineCallsManager.mSim1Num));
                Rlog.d(OfflineCallsManager.LOG_TAG, sb.toString());
                OfflineCallsManager.this.mOfflineConnectionService.requestTheCall(OfflineCallsManager.this.mSim1Num.getBytes(), OfflineCallsManager.this.mSim2Num.getBytes());
            } catch (Exception e) {
                Rlog.e(OfflineCallsManager.LOG_TAG, "[MO] onConnectStateChanged error = " + e);
            }
            if (OfflineCallsManager.this.mOfflineManager != null) {
                OfflineCallsManager.this.mOfflineManager.enableOfflinePhoneAccount(true);
                OfflineCallsManager offlineCallsManager2 = OfflineCallsManager.this;
                offlineCallsManager2.handleOfflineDialing(offlineCallsManager2.mConnectCallNum);
            }
        }

        @Override // com.android.bluetooth.ble.app.IVirtualCallEventListener
        public void onCallRequest(byte[] bArr, byte[] bArr2) {
            Rlog.d(OfflineCallsManager.LOG_TAG, "[MT] onCallRequest mOfflineCallState =" + OfflineCallsManager.this.mOfflineCallState);
            if (OfflineCallsManager.this.mOfflineConnectionService != null) {
                if (OfflineCallsManager.this.mOfflineCallState == -1 || OfflineCallsManager.this.mOfflineCallState == 5) {
                    try {
                        int i = Settings.Global.getInt(OfflineCallsManager.this.mContext.getContentResolver(), OfflineCallsManager.OFFLINE_CALLS_ENABLED_TYPE, 0);
                        int matchRingingCondition = OfflineCallsManager.this.getMatchRingingCondition(bArr, bArr2);
                        Rlog.d(OfflineCallsManager.LOG_TAG, "[MT] onCallRequest matchRingingCondition = " + matchRingingCondition + "; offlineType = " + i);
                        if (matchRingingCondition != 0 && (i != 2 || matchRingingCondition != 2)) {
                            OfflineCallsManager.this.rejectOfflineCall(matchRingingCondition);
                            return;
                        }
                        OfflineCallsManager.this.updateOfflineCallState(2);
                        OfflineCallsManager.this.mOfflineConnectionService.setScanMode(1);
                        OfflineCallsManager.this.mOfflineConnectionService.startScan();
                        OfflineCallsManager offlineCallsManager = OfflineCallsManager.this;
                        offlineCallsManager.mConnectCallNum = offlineCallsManager.getMatchRingingNum(bArr, bArr2);
                        OfflineCallsManager offlineCallsManager2 = OfflineCallsManager.this;
                        offlineCallsManager2.handleOfflineRinging(offlineCallsManager2.mConnectCallNum);
                    } catch (Exception e) {
                        Rlog.e(OfflineCallsManager.LOG_TAG, "[MT] onCallRequest error = " + e);
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override // com.android.bluetooth.ble.app.IVirtualCallEventListener
        public void onCallAnswer(byte[] bArr) {
            OfflineConnectionsManager offlineConnectionsManager;
            OfflineConnection connectionOfflineCall;
            Rlog.d(OfflineCallsManager.LOG_TAG, "[MO] onCallAnswer mOfflineCallState = " + OfflineCallsManager.this.mOfflineCallState);
            if (OfflineCallsManager.this.mOfflineCallState == 1) {
                OfflineCallsManager.this.updateOfflineCallState(4);
                if (OfflineCallsManager.this.mOfflineCallsRecorder != null) {
                    OfflineCallsManager.this.mOfflineCallsRecorder.startRecorder();
                }
                if (OfflineCallsManager.this.mOfflineManager == null || (offlineConnectionsManager = OfflineCallsManager.this.mOfflineManager.getOfflineConnectionsManager()) == null || (connectionOfflineCall = offlineConnectionsManager.getConnectionOfflineCall()) == null) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putBoolean("telecomm.EXTRA_OFFLINE_ANSWERED", true);
                connectionOfflineCall.putExtras(bundle);
                connectionOfflineCall.updateState(4);
                return;
            }
            Rlog.i(OfflineCallsManager.LOG_TAG, "[MO] onCallAnswer call state not match!");
        }

        @Override // com.android.bluetooth.ble.app.IVirtualCallEventListener
        public void onCallReject(byte[] bArr) {
            int iByteArrayToInt = OfflineCallsManager.this.byteArrayToInt(bArr);
            Rlog.d(OfflineCallsManager.LOG_TAG, "[MO] onCallReject mOfflineCallState = " + OfflineCallsManager.this.mOfflineCallState + "; rejectReason = " + iByteArrayToInt);
            if (OfflineCallsManager.this.mOfflineConnectionService != null && (OfflineCallsManager.this.mOfflineCallState == 1 || OfflineCallsManager.this.mOfflineCallState == 0)) {
                try {
                    if (iByteArrayToInt != 2) {
                        OfflineCallsManager offlineCallsManager = OfflineCallsManager.this;
                        if (iByteArrayToInt == 3) {
                            if (offlineCallsManager.mThreadHandler != null) {
                                OfflineCallsManager.this.mThreadHandler.sendMessageDelayed(OfflineCallsManager.this.mThreadHandler.obtainMessage(3), 500L);
                            }
                        } else {
                            offlineCallsManager.mOfflineConnectionService.disConnect();
                            OfflineCallsManager.this.disconnectCall();
                        }
                    } else if (OfflineCallsManager.this.mThreadHandler != null) {
                        OfflineCallsManager.this.mThreadHandler.sendMessageDelayed(OfflineCallsManager.this.mThreadHandler.obtainMessage(2), 500L);
                    }
                    OfflineCallsManager.this.updateOfflineCallState(5);
                    OfflineCallsManager.this.mOfflineConnectionService.disConnect();
                    OfflineCallsManager.this.disconnectCall();
                    return;
                } catch (Exception e) {
                    Rlog.e(OfflineCallsManager.LOG_TAG, "[MO] onCallReject error = " + e);
                    return;
                }
            }
            Rlog.i(OfflineCallsManager.LOG_TAG, "[MO] onCallReject call state not match!");
        }

        @Override // com.android.bluetooth.ble.app.IVirtualCallEventListener
        public void onDeviceFound(VirtualCallDevice virtualCallDevice) {
            if (virtualCallDevice != null) {
                StringBuilder sb = new StringBuilder("onDeviceFound getPhoneNum1 = ");
                sb.append(OfflineCallsManager.this.getPhoneNumberSubString(virtualCallDevice.getPhoneNum1()));
                sb.append("; getPhoneNum2 = ");
                sb.append(OfflineCallsManager.this.getPhoneNumberSubString(virtualCallDevice.getPhoneNum2()));
                sb.append("; mConnectCallNum = ");
                OfflineCallsManager offlineCallsManager = OfflineCallsManager.this;
                sb.append(offlineCallsManager.getPhoneNumberSubString(offlineCallsManager.mConnectCallNum));
                Rlog.d(OfflineCallsManager.LOG_TAG, sb.toString());
            }
            if (virtualCallDevice != null) {
                if (virtualCallDevice.getBleAddr().equals(OfflineCallsManager.this.mMoBleAddr) || virtualCallDevice.getPhoneNum1().equals(OfflineCallsManager.this.mConnectCallNum) || virtualCallDevice.getPhoneNum2().equals(OfflineCallsManager.this.mConnectCallNum) || virtualCallDevice.getBleAddr().equals(OfflineCallsManager.this.mMtBleAddr)) {
                    int rssi = virtualCallDevice.getRssi();
                    Rlog.d(OfflineCallsManager.LOG_TAG, "onDeviceFound rssi = " + rssi);
                    int i = rssi > OfflineCallsManager.OFFLINE_CALL_RSSI_THRESHOLD ? 1 : 0;
                    if (i != OfflineCallsManager.this.getOfflineCallRssi()) {
                        OfflineCallsManager.this.saveOfflineCallRssi(i);
                    }
                }
            }
        }

        @Override // com.android.bluetooth.ble.app.IVirtualCallEventListener
        public void onAuidioPlayerData(byte[] bArr) {
            if (OfflineCallsManager.this.mOfflineConnectionService != null && OfflineCallsManager.this.mOfflineCallState == 4) {
                if (OfflineCallsManager.this.mOfflineCallsPlayer != null) {
                    Rlog.i(OfflineCallsManager.LOG_TAG, "onAuidioPlayerData mAudioCount = " + OfflineCallsPlayer.mAudioCount);
                    int i = OfflineCallsPlayer.mAudioCount;
                    if (i < 3) {
                        OfflineCallsPlayer.mAudioCount = i + 1;
                        OfflineCallsManager.this.mOfflineCallsPlayer.startPlayer(bArr);
                        return;
                    }
                    return;
                }
                return;
            }
            Rlog.i(OfflineCallsManager.LOG_TAG, "onAuidioPlayerData call state not match!");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disconnectCall() {
        OfflineConnectionsManager offlineConnectionsManager;
        OfflineConnection connectionOfflineCall;
        OfflineManager offlineManager = this.mOfflineManager;
        if (offlineManager == null || (offlineConnectionsManager = offlineManager.getOfflineConnectionsManager()) == null || (connectionOfflineCall = offlineConnectionsManager.getConnectionOfflineCall()) == null) {
            return;
        }
        connectionOfflineCall.localDisconnected();
    }

    class OfflineCallsManagerHandler extends Handler {
        public OfflineCallsManagerHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                Rlog.d(OfflineCallsManager.LOG_TAG, "DIALING more 60S no answer, hangup call");
                if (OfflineCallsManager.this.mOfflineConnectionService == null || OfflineCallsManager.this.mOfflineCallState != 1) {
                    return;
                }
                try {
                    OfflineCallsManager.this.updateOfflineCallState(5);
                    OfflineCallsManager.this.mOfflineConnectionService.disConnect();
                    OfflineCallsManager.this.mOfflineConnectionService.stopScan();
                    return;
                } catch (Exception e) {
                    Rlog.e(OfflineCallsManager.LOG_TAG, "EVENT_NO_ANSWER error = " + e);
                    return;
                }
            }
            if (i == 1) {
                Rlog.d(OfflineCallsManager.LOG_TAG, "CONNECTING more 15S no dialing, hangup call");
                if (OfflineCallsManager.this.mOfflineConnectionService == null || OfflineCallsManager.this.mOfflineCallState != 0) {
                    return;
                }
                try {
                    OfflineCallsManager.this.updateOfflineCallState(5);
                    OfflineCallsManager.this.mOfflineConnectionService.disConnect();
                    OfflineCallsManager.this.mOfflineConnectionService.stopScan();
                    OfflineCallsManager.this.disconnectCall();
                    return;
                } catch (Exception e2) {
                    Rlog.e(OfflineCallsManager.LOG_TAG, "EVENT_NO_DIALING error = " + e2);
                    return;
                }
            }
            if (i == 2) {
                Rlog.d(OfflineCallsManager.LOG_TAG, "RINGING not match contacts, hangup call");
                try {
                    Toast.makeText(OfflineCallsManager.this.mContext, OfflineCallsManager.this.mContext.getResources().getString(R.string.offline_call_not_match_contact), 0).show();
                    OfflineCallsManager.this.mOfflineConnectionService.disConnect();
                    OfflineCallsManager.this.disconnectCall();
                    return;
                } catch (Exception e3) {
                    Rlog.e(OfflineCallsManager.LOG_TAG, "EVENT_CALL_RINGING_NOT_MATCH_CONTACTS error = " + e3);
                    return;
                }
            }
            if (i != 3) {
                return;
            }
            Rlog.d(OfflineCallsManager.LOG_TAG, "RINGING has call, hangup call");
            try {
                Toast.makeText(OfflineCallsManager.this.mContext, OfflineCallsManager.this.mContext.getResources().getString(R.string.offline_call_has_call), 0).show();
                OfflineCallsManager.this.mOfflineConnectionService.disConnect();
                OfflineCallsManager.this.disconnectCall();
            } catch (Exception e4) {
                Rlog.e(OfflineCallsManager.LOG_TAG, "EVENT_CALL_RINGING_HAS_CALL error = " + e4);
            }
        }
    }

    public void dialOfflineCall(String str, String str2, String str3) {
        this.mMoBleAddr = str3;
        this.mConnectCallNum = str;
        Rlog.d(LOG_TAG, "[MO] dialOfflineCall mOfflineCallState = " + this.mOfflineCallState + "mConnectCallNum = " + getPhoneNumberSubString(this.mConnectCallNum));
        if (this.mOfflineConnectionService != null) {
            int i = this.mOfflineCallState;
            if (i == -1 || i == 5) {
                try {
                    updateOfflineCallState(0);
                    this.mSim1Num = getPhoneNumber(0);
                    this.mSim2Num = getPhoneNumber(1);
                    handleSimNumForDemodevice();
                    Rlog.d(LOG_TAG, "[MO] dialOfflineCall mSim1Num = " + getPhoneNumberSubString(this.mSim1Num) + "mSim2Num = " + getPhoneNumberSubString(this.mSim2Num));
                    this.mOfflineConnectionService.connect(new VirtualCallDevice(this.mSim1Num, this.mSim2Num, str3, -1, -1, -1, -1));
                } catch (Exception e) {
                    Rlog.e(LOG_TAG, "[MO] dialOfflineCall error = " + e);
                }
            }
        }
    }

    public void answerOfflineCall() {
        Rlog.d(LOG_TAG, "[MT] answerOfflineCall mOfflineCallState = " + this.mOfflineCallState);
        if (this.mOfflineConnectionService != null && this.mOfflineCallState == 2) {
            try {
                updateOfflineCallState(4);
                this.mOfflineConnectionService.answerTheCall();
                OfflineCallsRecorder offlineCallsRecorder = this.mOfflineCallsRecorder;
                if (offlineCallsRecorder != null) {
                    offlineCallsRecorder.startRecorder();
                    return;
                }
                return;
            } catch (Exception e) {
                Rlog.e(LOG_TAG, "[MT] answerOfflineCall error = " + e);
                return;
            }
        }
        Rlog.i(LOG_TAG, "[MT] answerOfflineCall call state not match!");
    }

    public void hangupOfflineCall() {
        Rlog.d(LOG_TAG, "hangupOfflineCall mOfflineCallState = " + this.mOfflineCallState);
        if (this.mOfflineCallState != -1) {
            updateOfflineCallState(5);
            disconnectCall();
            IVirtualCallConnectionService iVirtualCallConnectionService = this.mOfflineConnectionService;
            if (iVirtualCallConnectionService != null) {
                try {
                    iVirtualCallConnectionService.disConnect();
                    this.mOfflineConnectionService.stopScan();
                } catch (Exception e) {
                    Rlog.e(LOG_TAG, "hangupOfflineCall error = " + e);
                }
            }
            Rlog.i(LOG_TAG, "hangupOfflineCall call state not match!");
        }
    }

    public void rejectOfflineCall(int i) {
        Rlog.d(LOG_TAG, "[MT] rejectOfflineCall reason = " + i);
        if (this.mOfflineConnectionService != null) {
            try {
                updateOfflineCallState(5);
                this.mOfflineConnectionService.rejectTheCall(intToByteArray(i));
                this.mOfflineConnectionService.stopScan();
            } catch (Exception e) {
                Rlog.e(LOG_TAG, "[MT] rejectOfflineCall error = " + e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateOfflineCallState(int i) {
        Rlog.d(LOG_TAG, "updateOfflineCallState state >>> " + getOfflineCallStateStr(i));
        if (this.mOfflineCallState != i) {
            this.mOfflineCallState = i;
            OfflineCallsManagerHandler offlineCallsManagerHandler = this.mThreadHandler;
            if (i == 0) {
                if (offlineCallsManagerHandler != null) {
                    offlineCallsManagerHandler.sendMessageDelayed(offlineCallsManagerHandler.obtainMessage(1), 32000L);
                }
            } else if (offlineCallsManagerHandler != null && offlineCallsManagerHandler.hasMessages(1)) {
                this.mThreadHandler.removeMessages(1);
            }
            int i2 = this.mOfflineCallState;
            OfflineCallsManagerHandler offlineCallsManagerHandler2 = this.mThreadHandler;
            if (i2 == 1) {
                if (offlineCallsManagerHandler2 != null) {
                    offlineCallsManagerHandler2.sendMessageDelayed(offlineCallsManagerHandler2.obtainMessage(0), RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
                }
            } else if (offlineCallsManagerHandler2 != null && offlineCallsManagerHandler2.hasMessages(0)) {
                this.mThreadHandler.removeMessages(0);
            }
            int i3 = this.mOfflineCallState;
            if (i3 == -1 || i3 == 5 || i3 == 6) {
                releaseRecordAndPlayer();
            } else if (i3 == 1 || i3 == 2) {
                startRecordAndPlayerInit();
            }
            Intent intent = new Intent(ACTION_OFFLINE_CALL_STATE);
            intent.putExtra("state", this.mOfflineCallState);
            Context context = this.mContext;
            if (context != null) {
                context.sendBroadcast(intent);
            }
        }
    }

    private String getOfflineCallStateStr(int i) {
        switch (i) {
            case -1:
                return "IDLE";
            case 0:
                return "CONNECTING";
            case 1:
                return "DIALING";
            case 2:
                return "RINGING";
            case 3:
                return "ANSWERED";
            case 4:
                return "ACTIVE";
            case 5:
                return "DISCONNECTING";
            case 6:
                return "DISCONNECTED";
            default:
                return "";
        }
    }

    public void sendRecordData(byte[] bArr) {
        if (this.mOfflineConnectionService != null && this.mOfflineCallState == 4) {
            try {
                Rlog.i(LOG_TAG, "sendRecordData recordData length = " + bArr.length);
                this.mOfflineConnectionService.sendRecordData(bArr);
                return;
            } catch (Exception e) {
                Rlog.e(LOG_TAG, "sendRecordData error = " + e);
                return;
            }
        }
        Rlog.i(LOG_TAG, "sendRecordData call state not match!");
    }

    private void startRecordAndPlayerInit() {
        if (this.mOfflineCallsRecorder == null) {
            this.mOfflineCallsRecorder = new OfflineCallsRecorder(new Consumer() { // from class: com.android.services.telephony.relay.offlinecall.OfflineCallsManager$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.sendRecordData((byte[]) obj);
                }
            });
        }
        if (this.mOfflineCallsPlayer == null) {
            this.mOfflineCallsPlayer = new OfflineCallsPlayer();
        }
    }

    private void releaseRecordAndPlayer() {
        OfflineCallsRecorder offlineCallsRecorder = this.mOfflineCallsRecorder;
        if (offlineCallsRecorder != null) {
            offlineCallsRecorder.release();
            this.mOfflineCallsRecorder = null;
        }
        OfflineCallsPlayer offlineCallsPlayer = this.mOfflineCallsPlayer;
        if (offlineCallsPlayer != null) {
            offlineCallsPlayer.release();
            this.mOfflineCallsPlayer = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reset() {
        if (this.mOfflineCallState != -1) {
            updateOfflineCallState(-1);
        }
        this.mSim1Num = "";
        this.mSim2Num = "";
        this.mConnectCallNum = "";
        this.mMoBleAddr = "";
        this.mMtBleAddr = "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getOfflineCallRssi() {
        Context context = this.mContext;
        if (context != null) {
            return Settings.Global.getInt(context.getContentResolver(), OFFLINE_CALLS_RSSI, 1);
        }
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveOfflineCallRssi(int i) {
        if (this.mContext != null) {
            Rlog.d(LOG_TAG, "saveOfflineCallRssi rssiQuality = " + i);
            Settings.Global.putInt(this.mContext.getContentResolver(), OFFLINE_CALLS_RSSI, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getMatchRingingCondition(byte[] bArr, byte[] bArr2) {
        if (bArr == null && bArr2 == null) {
            return 2;
        }
        TelephonyManager telephonyManager = this.mTelephonyManager;
        if (telephonyManager != null) {
            int callState = telephonyManager.getCallState();
            Rlog.d(LOG_TAG, "getMatchRingingCondition callState = " + callState);
            if (callState != 0) {
                return 3;
            }
        }
        if (bArr != null) {
            String str = new String(bArr, StandardCharsets.UTF_8);
            Rlog.d(LOG_TAG, "getMatchRingingCondition sim1NumStr = " + getPhoneNumberSubString(str));
            if (!TextUtils.isEmpty(RelayUtils.System.queryContactName(this.mContext, str))) {
                return 0;
            }
        }
        if (bArr2 != null) {
            String str2 = new String(bArr2, StandardCharsets.UTF_8);
            Rlog.d(LOG_TAG, "getMatchRingingCondition sim2NumStr = " + getPhoneNumberSubString(str2));
            if (!TextUtils.isEmpty(RelayUtils.System.queryContactName(this.mContext, str2))) {
                return 0;
            }
        }
        return 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getMatchRingingNum(byte[] bArr, byte[] bArr2) {
        int i = Settings.Global.getInt(this.mContext.getContentResolver(), OFFLINE_CALLS_ENABLED_TYPE, 0);
        if (bArr != null) {
            String str = new String(bArr, StandardCharsets.UTF_8);
            if (!TextUtils.isEmpty(RelayUtils.System.queryContactName(this.mContext, str)) || (!TextUtils.isEmpty(str) && i == 2)) {
                return str;
            }
        }
        if (bArr2 != null) {
            String str2 = new String(bArr2, StandardCharsets.UTF_8);
            return (!TextUtils.isEmpty(RelayUtils.System.queryContactName(this.mContext, str2)) || (!TextUtils.isEmpty(str2) && i == 2)) ? str2 : "";
        }
        return "";
    }

    private String getPhoneNumber(int i) {
        TelephonyManager telephonyManager;
        if (i >= TelephonyManager.getDefault().getPhoneCount()) {
            Rlog.i(LOG_TAG, "getPhoneNumber slotId more than phone count");
            return "";
        }
        SubscriptionInfo availableSubscriptionInfoBySlot = MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(i);
        if (availableSubscriptionInfoBySlot == null || (telephonyManager = this.mTelephonyManager) == null) {
            return "";
        }
        String line1Number = telephonyManager.createForSubscriptionId(availableSubscriptionInfoBySlot.getSubscriptionId()).getLine1Number();
        return TextUtils.isEmpty(line1Number) ? availableSubscriptionInfoBySlot.getDisplayNumber() : line1Number;
    }

    private byte[] intToByteArray(int i) {
        try {
            return new byte[]{(byte) (i >> 24), (byte) (i >> 16), (byte) (i >> 8), (byte) i};
        } catch (Exception e) {
            Rlog.e(LOG_TAG, "intToByteArray error = " + e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int byteArrayToInt(byte[] bArr) {
        try {
            Rlog.d(LOG_TAG, "byteArrayToInt input length = " + bArr.length);
            if (bArr.length == 4) {
                return (bArr[3] & TransitionInfo.INIT) | (bArr[0] << 24) | ((bArr[1] & TransitionInfo.INIT) << 16) | ((bArr[2] & TransitionInfo.INIT) << 8);
            }
            if (bArr.length != 1) {
                Rlog.e(LOG_TAG, "byteArrayToInt input data error");
                return -1;
            }
            return bArr[0] & TransitionInfo.INIT;
        } catch (Exception e) {
            Rlog.e(LOG_TAG, "byteArrayToInt error = " + e);
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getPhoneNumberSubString(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return str.length() > 10 ? str.substring(10) : str;
    }

    public void handleOfflineRinging(String str) {
        Bundle bundle = new Bundle();
        if (!TextUtils.isEmpty(str)) {
            bundle.putParcelable("android.telecom.extra.INCOMING_CALL_ADDRESS", Uri.fromParts(GenericURI.TEL, str, null));
        }
        bundle.putBoolean("telecomm.EXTRA_OFFLINE_CALL", true);
        try {
            ((TelecomManager) OfflineUtils.System.getContext().getSystemService(TelecomManager.class)).addNewIncomingCall(OfflinePhoneAccountRegistry.PHONE_ACCOUNT_HANDLE, bundle);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        OfflineCallLog.logi(LOG_TAG, "handleOfflineRinging");
    }

    public void handleOfflineDialing(String str) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("android.telecom.extra.PHONE_ACCOUNT_HANDLE", OfflinePhoneAccountRegistry.PHONE_ACCOUNT_HANDLE);
        Bundle bundle2 = new Bundle();
        bundle2.putBoolean("telecomm.EXTRA_OFFLINE_CALL", true);
        Uri uriFromParts = Uri.fromParts(GenericURI.TEL, str, null);
        bundle2.putParcelable("android.telecom.extra.INCOMING_CALL_ADDRESS", uriFromParts);
        bundle.putBundle("android.telecom.extra.OUTGOING_CALL_EXTRAS", bundle2);
        try {
            ((TelecomManager) OfflineUtils.System.getContext().getSystemService(TelecomManager.class)).placeCall(uriFromParts, bundle);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        OfflineUtils.Offline.notifyConnectionComplete();
        OfflineCallLog.logi(LOG_TAG, "handleOfflineDialing");
    }

    private void handleSimNumForDemodevice() {
        if (this.mOfflineManager == null || !OfflineManager.isDemoDevice()) {
            return;
        }
        String deviceName = RelayUtils.System.getDeviceName();
        String demoDeviceLocalnumber = this.mOfflineManager.getDemoDeviceLocalnumber();
        Rlog.d(LOG_TAG, "[MO] TestDevice mode : Demo " + deviceName + " " + demoDeviceLocalnumber);
        this.mSim1Num = "Demo " + deviceName + " " + demoDeviceLocalnumber;
        this.mSim2Num = "";
    }
}
