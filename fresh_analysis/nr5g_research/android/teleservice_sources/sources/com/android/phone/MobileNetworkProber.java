package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.android.internal.telephony.LocalLog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class MobileNetworkProber {
    private static volatile MobileNetworkProber sInstance;
    private static final HashMap sLocalLogs = new HashMap();
    private ConnectivityManager mConnManager;
    private final Context mContext;
    private boolean mIsMobileDataConnected;
    private MobileNetworkHandler mMobileNetworkHandler;
    private HandlerThread mWorkThread;
    private volatile boolean mNeedNetworkProbe = true;
    private volatile FakeDataTriggers mFakeDataTrigger = FakeDataTriggers.TELEPHONY_CALL_BACK;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.MobileNetworkProber.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                MobileNetworkProber.this.log("Received null intent or action");
                return;
            }
            String action = intent.getAction();
            if ("miui.intent.action.NETWORK_BLOCKED".equals(action)) {
                MobileNetworkProber.this.log("ACTION_NETWORK_BLOCKED");
                MobileNetworkProber.this.mMobileNetworkHandler.sendMessageDelayed(MobileNetworkProber.this.mMobileNetworkHandler.obtainMessage(100), 2000L);
                return;
            }
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
                NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                if (networkInfo == null) {
                    return;
                }
                int type = networkInfo.getType();
                MobileNetworkProber.this.log("CONNECTIVITY_ACTION, networkType = " + type + ", state = " + networkInfo.getState());
                if (type == 0) {
                    MobileNetworkProber.this.handleConnectivityChange(networkInfo);
                    return;
                }
                return;
            }
            if ("com.android.phone.NETWORK_BLOCKED_TEST".equals(action)) {
                MobileNetworkProber.this.log("ACTION_NETWORK_BLOCKED_TEST");
                int intExtra = intent.getIntExtra("triggerCause", 0);
                FakeDataTriggers fakeDataTriggers = FakeDataTriggers.TELEPHONY_CALL_BACK;
                if (intExtra == 1) {
                    fakeDataTriggers = FakeDataTriggers.MOBILE_DIAGNOSTIC;
                } else if (intExtra == 2) {
                    fakeDataTriggers = FakeDataTriggers.NETWORK_SPEED_DATA_STALL;
                }
                MobileNetworkProber.this.startNetworkProber(fakeDataTriggers);
            }
        }
    };

    public enum FakeDataTriggers {
        TELEPHONY_CALL_BACK,
        MOBILE_DIAGNOSTIC,
        NETWORK_SPEED_DATA_STALL
    }

    private MobileNetworkProber(Context context) {
        this.mContext = context;
        this.mConnManager = (ConnectivityManager) context.getSystemService("connectivity");
        HandlerThread handlerThread = new HandlerThread("MobileNetworkProber", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mMobileNetworkHandler = new MobileNetworkHandler(this.mWorkThread.getLooper());
        registerReceiver();
        log("MobileNetworkProber init");
    }

    public static MobileNetworkProber make(Context context) {
        if (sInstance == null) {
            synchronized (MobileNetworkProber.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new MobileNetworkProber(context);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    public static MobileNetworkProber getInstance() {
        return sInstance;
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("miui.intent.action.NETWORK_BLOCKED");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("com.android.phone.NETWORK_BLOCKED_TEST");
        this.mContext.registerReceiver(this.mReceiver, intentFilter, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleConnectivityChange(NetworkInfo networkInfo) {
        boolean z = networkInfo.getState() == NetworkInfo.State.CONNECTED;
        if (z != this.mIsMobileDataConnected) {
            this.mIsMobileDataConnected = z;
        }
    }

    public void startNetworkProber(FakeDataTriggers fakeDataTriggers) {
        NetworkCapabilities networkCapabilities;
        Network activeNetwork = this.mConnManager.getActiveNetwork();
        log("startNetworkProber, network = " + activeNetwork + ", trigger: " + fakeDataTriggers.toString());
        if (activeNetwork == null || (networkCapabilities = this.mConnManager.getNetworkCapabilities(activeNetwork)) == null || !networkCapabilities.hasTransport(0) || !networkCapabilities.hasCapability(12)) {
            return;
        }
        this.mNeedNetworkProbe = false;
        this.mFakeDataTrigger = fakeDataTriggers;
        localLog("startNetworkProber", " trigger: " + fakeDataTriggers.toString());
        this.mConnManager.reportNetworkConnectivity(activeNetwork, false);
    }

    /* JADX INFO: renamed from: com.android.phone.MobileNetworkProber$2, reason: invalid class name */
    abstract /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$miui$telephony$TelephonyStatAdapter$DataStatType;

        static {
            int[] iArr = new int[TelephonyStatAdapter.DataStatType.values().length];
            $SwitchMap$miui$telephony$TelephonyStatAdapter$DataStatType = iArr;
            try {
                iArr[TelephonyStatAdapter.DataStatType.FAKE_CONNECTION_CN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$miui$telephony$TelephonyStatAdapter$DataStatType[TelephonyStatAdapter.DataStatType.DATA_CONNECTION_OK_CN.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public void processDataStatisticChange(TelephonyStatAdapter.DataStatType dataStatType) {
        int i = AnonymousClass2.$SwitchMap$miui$telephony$TelephonyStatAdapter$DataStatType[dataStatType.ordinal()];
        if (i == 1) {
            this.mNeedNetworkProbe = false;
            this.mFakeDataTrigger = FakeDataTriggers.TELEPHONY_CALL_BACK;
        } else if (i == 2) {
            this.mNeedNetworkProbe = true;
            this.mFakeDataTrigger = FakeDataTriggers.TELEPHONY_CALL_BACK;
        } else {
            Log.w("MobileNetworkProber", "processDataStatisticChange: unhandled type=" + dataStatType.name());
        }
    }

    class MobileNetworkHandler extends Handler {
        MobileNetworkHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            try {
                int i = message.what;
                MobileNetworkProber mobileNetworkProber = MobileNetworkProber.this;
                if (i == 100) {
                    mobileNetworkProber.log("MSG_START_MOBILE_NETWORK_PROBER, mNeedNetworkProbe: " + MobileNetworkProber.this.mNeedNetworkProbe + ", mIsMobileDataConnected: " + MobileNetworkProber.this.mIsMobileDataConnected);
                    if (MobileNetworkProber.this.mIsMobileDataConnected && MobileNetworkProber.this.mNeedNetworkProbe) {
                        MobileNetworkProber.this.startNetworkProber(FakeDataTriggers.MOBILE_DIAGNOSTIC);
                        return;
                    }
                    return;
                }
                mobileNetworkProber.log("Unhandled message: " + message.what);
            } catch (Exception e) {
                Log.e("MobileNetworkProber", "Error handling message: " + message.what, e);
            }
        }
    }

    public String getFakeDataTrigger() {
        if (this.mFakeDataTrigger == null) {
            return "";
        }
        return this.mFakeDataTrigger.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("MobileNetworkProber", str);
    }

    private void localLog(String str, String str2) {
        String str3 = "MobileNetworkProber." + str;
        HashMap map = sLocalLogs;
        synchronized (map) {
            try {
                if (!map.containsKey(str3)) {
                    map.put(str3, new LocalLog(250));
                }
                ((LocalLog) map.get(str3)).log(str2);
            } catch (Throwable th) {
                throw th;
            }
        }
        Log.d("MobileNetworkProber", str + " " + str2);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("MobileNetworkProber dump start------");
        HashMap map = sLocalLogs;
        synchronized (map) {
            try {
                for (Map.Entry entry : map.entrySet()) {
                    printWriter.println((String) entry.getKey());
                    ((LocalLog) entry.getValue()).dump(null, printWriter, null);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        printWriter.println("MobileNetworkProber dump end------");
        printWriter.flush();
    }
}
