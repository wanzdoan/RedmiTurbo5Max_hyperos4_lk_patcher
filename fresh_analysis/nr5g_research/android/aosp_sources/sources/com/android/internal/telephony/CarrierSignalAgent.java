package com.android.internal.telephony;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.data.ApnSetting;
import android.text.TextUtils;
import com.android.internal.telephony.util.ArrayUtils;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class CarrierSignalAgent extends Handler {
    private static final Map COMPAT_ACTION_TO_NEW_MAP;
    private static final String LOG_TAG = "CarrierSignalAgent";
    private static final Map NEW_ACTION_TO_COMPAT_MAP;
    private boolean mDefaultNetworkAvail;
    private ConnectivityManager.NetworkCallback mNetworkCallback;
    private final Phone mPhone;
    private static final boolean VDBG = Rlog.isLoggable(CarrierSignalAgent.class.getSimpleName(), 2);
    private static final Set VALID_CARRIER_SIGNAL_ACTIONS = Set.of("android.telephony.action.CARRIER_SIGNAL_PCO_VALUE", "android.telephony.action.CARRIER_SIGNAL_REDIRECTED", "android.telephony.action.CARRIER_SIGNAL_REQUEST_NETWORK_FAILED", "android.telephony.action.CARRIER_SIGNAL_RESET", "android.telephony.action.CARRIER_SIGNAL_DEFAULT_NETWORK_AVAILABLE");
    private Map mCachedWakeSignalConfigs = new HashMap();
    private Map mCachedNoWakeSignalConfigs = new HashMap();
    private final LocalLog mErrorLocalLog = new LocalLog(16);

    static {
        Map mapOf = Map.of("android.telephony.action.CARRIER_SIGNAL_PCO_VALUE", "com.android.internal.telephony.CARRIER_SIGNAL_PCO_VALUE", "android.telephony.action.CARRIER_SIGNAL_REDIRECTED", "com.android.internal.telephony.CARRIER_SIGNAL_REDIRECTED", "android.telephony.action.CARRIER_SIGNAL_REQUEST_NETWORK_FAILED", "com.android.internal.telephony.CARRIER_SIGNAL_REQUEST_NETWORK_FAILED", "android.telephony.action.CARRIER_SIGNAL_RESET", "com.android.internal.telephony.CARRIER_SIGNAL_RESET", "android.telephony.action.CARRIER_SIGNAL_DEFAULT_NETWORK_AVAILABLE", "com.android.internal.telephony.CARRIER_SIGNAL_DEFAULT_NETWORK_AVAILABLE");
        NEW_ACTION_TO_COMPAT_MAP = mapOf;
        COMPAT_ACTION_TO_NEW_MAP = (Map) mapOf.entrySet().stream().collect(Collectors.toMap(new Function() { // from class: com.android.internal.telephony.CarrierSignalAgent$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return (String) ((Map.Entry) obj).getValue();
            }
        }, new CarrierSignalAgent$$ExternalSyntheticLambda2()));
    }

    public CarrierSignalAgent(Phone phone) {
        this.mPhone = phone;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) phone.getContext().getSystemService(CarrierConfigManager.class);
        loadCarrierConfig();
        if (carrierConfigManager != null) {
            carrierConfigManager.registerCarrierConfigChangeListener(phone.getContext().getMainExecutor(), new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.internal.telephony.CarrierSignalAgent$$ExternalSyntheticLambda0
                @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
                public final void onCarrierConfigChanged(int i, int i2, int i3, int i4) {
                    this.f$0.lambda$new$0(i, i2, i3, i4);
                }
            });
        }
        phone.getCarrierActionAgent().registerForCarrierAction(3, this, 0, null, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, int i2, int i3, int i4) {
        if (i == this.mPhone.getPhoneId()) {
            loadCarrierConfig();
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        if (message.what != 0) {
            return;
        }
        AsyncResult asyncResult = (AsyncResult) message.obj;
        if (asyncResult.exception != null) {
            Rlog.e(LOG_TAG, "Register default network exception: " + asyncResult.exception);
            return;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) this.mPhone.getContext().getSystemService(ConnectivityManager.class);
        if (((Boolean) asyncResult.result).booleanValue()) {
            ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.android.internal.telephony.CarrierSignalAgent.1
                @Override // android.net.ConnectivityManager.NetworkCallback
                public void onAvailable(Network network) {
                    if (CarrierSignalAgent.this.mDefaultNetworkAvail) {
                        return;
                    }
                    CarrierSignalAgent.this.log("Default network available: " + network);
                    Intent intent = new Intent("android.telephony.action.CARRIER_SIGNAL_DEFAULT_NETWORK_AVAILABLE");
                    intent.putExtra("android.telephony.extra.DEFAULT_NETWORK_AVAILABLE", true);
                    CarrierSignalAgent.this.notifyCarrierSignalReceivers(intent);
                    CarrierSignalAgent.this.mDefaultNetworkAvail = true;
                }

                @Override // android.net.ConnectivityManager.NetworkCallback
                public void onLost(Network network) {
                    CarrierSignalAgent.this.log("Default network lost: " + network);
                    Intent intent = new Intent("android.telephony.action.CARRIER_SIGNAL_DEFAULT_NETWORK_AVAILABLE");
                    intent.putExtra("android.telephony.extra.DEFAULT_NETWORK_AVAILABLE", false);
                    CarrierSignalAgent.this.notifyCarrierSignalReceivers(intent);
                    CarrierSignalAgent.this.mDefaultNetworkAvail = false;
                }
            };
            this.mNetworkCallback = networkCallback;
            connectivityManager.registerDefaultNetworkCallback(networkCallback, this.mPhone);
            log("Register default network");
            return;
        }
        ConnectivityManager.NetworkCallback networkCallback2 = this.mNetworkCallback;
        if (networkCallback2 != null) {
            connectivityManager.unregisterNetworkCallback(networkCallback2);
            this.mNetworkCallback = null;
            this.mDefaultNetworkAvail = false;
            log("unregister default network");
        }
    }

    private void loadCarrierConfig() {
        PersistableBundle carrierConfigSubset = CarrierConfigManager.getCarrierConfigSubset(this.mPhone.getContext(), this.mPhone.getSubId(), new String[]{"carrier_app_wake_signal_config", "carrier_app_no_wake_signal_config"});
        if (carrierConfigSubset.isEmpty()) {
            return;
        }
        synchronized (this.mCachedWakeSignalConfigs) {
            try {
                log("Loading carrier config: carrier_app_wake_signal_config");
                Map andCache = parseAndCache(carrierConfigSubset.getStringArray("carrier_app_wake_signal_config"));
                if (!this.mCachedWakeSignalConfigs.isEmpty() && !andCache.equals(this.mCachedWakeSignalConfigs)) {
                    if (VDBG) {
                        log("carrier config changed, reset receivers from old config");
                    }
                    this.mPhone.getCarrierActionAgent().sendEmptyMessage(2);
                }
                this.mCachedWakeSignalConfigs = andCache;
            } catch (Throwable th) {
                throw th;
            }
        }
        synchronized (this.mCachedNoWakeSignalConfigs) {
            try {
                log("Loading carrier config: carrier_app_no_wake_signal_config");
                Map andCache2 = parseAndCache(carrierConfigSubset.getStringArray("carrier_app_no_wake_signal_config"));
                if (!this.mCachedNoWakeSignalConfigs.isEmpty() && !andCache2.equals(this.mCachedNoWakeSignalConfigs)) {
                    if (VDBG) {
                        log("carrier config changed, reset receivers from old config");
                    }
                    this.mPhone.getCarrierActionAgent().sendEmptyMessage(2);
                }
                this.mCachedNoWakeSignalConfigs = andCache2;
            } catch (Throwable th2) {
                throw th2;
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:23:0x0083  */
    /* JADX WARN: Code duplicated, block: B:26:0x0092  */
    /* JADX WARN: Code duplicated, block: B:38:0x00b0 A[SYNTHETIC] */
    /* JADX WARN: Instruction removed from duplicated block: B:26:0x0092, please report this as an issue */
    private Map parseAndCache(String[] strArr) {
        Set hashSet;
        HashMap map = new HashMap();
        if (!ArrayUtils.isEmpty(strArr)) {
            for (String str : strArr) {
                if (!TextUtils.isEmpty(str)) {
                    String[] strArrSplit = str.trim().split("\\s*:\\s*", 2);
                    if (strArrSplit.length == 2) {
                        ComponentName componentNameUnflattenFromString = ComponentName.unflattenFromString(strArrSplit[0]);
                        if (componentNameUnflattenFromString == null) {
                            loge("Invalid component name: " + strArrSplit[0]);
                        } else {
                            for (String str2 : strArrSplit[1].split("\\s*,\\s*")) {
                                if (!VALID_CARRIER_SIGNAL_ACTIONS.contains(str2)) {
                                    Map map2 = COMPAT_ACTION_TO_NEW_MAP;
                                    if (map2.containsKey(str2)) {
                                        str2 = (String) map2.get(str2);
                                        hashSet = (Set) map.get(str2);
                                        if (hashSet == null) {
                                            hashSet = new HashSet();
                                            map.put(str2, hashSet);
                                        }
                                        hashSet.add(componentNameUnflattenFromString);
                                        if (VDBG) {
                                            logv("Add config {signal: " + str2 + " componentName: " + componentNameUnflattenFromString + "}");
                                        }
                                    } else {
                                        loge("Invalid signal name: " + str2);
                                    }
                                } else {
                                    hashSet = (Set) map.get(str2);
                                    if (hashSet == null) {
                                        hashSet = new HashSet();
                                        map.put(str2, hashSet);
                                    }
                                    hashSet.add(componentNameUnflattenFromString);
                                    if (VDBG) {
                                        logv("Add config {signal: " + str2 + " componentName: " + componentNameUnflattenFromString + "}");
                                    }
                                }
                            }
                        }
                    } else {
                        loge("invalid config format: ".concat(str));
                    }
                }
            }
        }
        return map;
    }

    public boolean hasRegisteredReceivers(String str) {
        return this.mCachedWakeSignalConfigs.containsKey(str) || this.mCachedNoWakeSignalConfigs.containsKey(str);
    }

    private void broadcast(Intent intent, Set set, boolean z) {
        PackageManager packageManager = this.mPhone.getContext().getPackageManager();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            ComponentName componentName = (ComponentName) it.next();
            Intent intent2 = new Intent(intent);
            if (z) {
                intent2.setComponent(componentName);
            } else {
                intent2.setPackage(componentName.getPackageName());
            }
            if (z && packageManager.queryBroadcastReceivers(intent2, InboundSmsTracker.DEST_PORT_FLAG_NO_PORT).isEmpty()) {
                loge("Carrier signal receivers are configured but unavailable: " + intent2.getComponent());
            } else if (!z && !packageManager.queryBroadcastReceivers(intent2, InboundSmsTracker.DEST_PORT_FLAG_NO_PORT).isEmpty()) {
                loge("Runtime signals shouldn't be configured in Manifest: " + intent2.getComponent());
            } else {
                SubscriptionManager.putSubscriptionIdExtra(intent2, this.mPhone.getSubId());
                intent2.addFlags(268435456);
                if (!z) {
                    intent2.setFlags(16);
                }
                Intent intentCreateCompatIntent = null;
                try {
                    if (this.mPhone.getContext().getPackageManager().getApplicationInfo(componentName.getPackageName(), 0).targetSdkVersion <= 30) {
                        intentCreateCompatIntent = createCompatIntent(intent2);
                    }
                } catch (PackageManager.NameNotFoundException unused) {
                }
                if (intentCreateCompatIntent != null) {
                    intent2 = intentCreateCompatIntent;
                }
                try {
                    this.mPhone.getContext().sendBroadcastAsUser(intent2, UserHandle.ALL);
                    log("Sending signal " + intent2.getAction() + " to the carrier signal receiver: " + intent2.getComponent());
                } catch (ActivityNotFoundException e) {
                    loge("Send broadcast failed: " + e);
                }
            }
        }
    }

    public void notifyCarrierSignalReceivers(Intent intent) {
        synchronized (this.mCachedWakeSignalConfigs) {
            try {
                Set set = (Set) this.mCachedWakeSignalConfigs.get(intent.getAction());
                if (!ArrayUtils.isEmpty(set)) {
                    broadcast(intent, set, true);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        synchronized (this.mCachedNoWakeSignalConfigs) {
            try {
                Set set2 = (Set) this.mCachedNoWakeSignalConfigs.get(intent.getAction());
                if (!ArrayUtils.isEmpty(set2)) {
                    broadcast(intent, set2, false);
                }
            } catch (Throwable th2) {
                throw th2;
            }
        }
    }

    private static Intent createCompatIntent(Intent intent) {
        String str = (String) NEW_ACTION_TO_COMPAT_MAP.get(intent.getAction());
        if (str == null) {
            Rlog.i(LOG_TAG, "intent action " + intent.getAction() + " does not have a compat alternative for component " + intent.getComponent());
            return null;
        }
        Intent intent2 = new Intent(intent);
        intent2.setAction(str);
        for (String str2 : intent.getExtras().keySet()) {
            str2.hashCode();
            switch (str2) {
                case "android.telephony.extra.APN_PROTOCOL":
                    int intExtra = intent.getIntExtra("android.telephony.extra.APN_PROTOCOL", -1);
                    intent2.putExtra("apnProtoInt", intExtra);
                    intent2.putExtra("apnProto", ApnSetting.getProtocolStringFromInt(intExtra));
                    break;
                case "android.telephony.extra.APN_TYPE":
                    int intExtra2 = intent.getIntExtra("android.telephony.extra.APN_TYPE", 17);
                    intent2.putExtra("apnTypeInt", intExtra2);
                    intent2.putExtra(TelephonyStatAdapter.APN_TYPE_KEY, ApnSetting.getApnTypesStringFromBitmask(intExtra2));
                    break;
                case "android.telephony.extra.DEFAULT_NETWORK_AVAILABLE":
                    intent2.putExtra("defaultNetworkAvailable", intent.getBooleanExtra("android.telephony.extra.DEFAULT_NETWORK_AVAILABLE", false));
                    break;
                case "android.telephony.extra.PCO_ID":
                    intent2.putExtra("pcoId", intent.getIntExtra("android.telephony.extra.PCO_ID", -1));
                    break;
                case "android.telephony.extra.DATA_FAIL_CAUSE":
                    intent2.putExtra(TelephonyStatAdapter.ERROR_CODE_KEY, intent.getIntExtra("android.telephony.extra.DATA_FAIL_CAUSE", -1));
                    break;
                case "android.telephony.extra.REDIRECTION_URL":
                    intent2.putExtra("redirectionUrl", intent.getStringExtra("android.telephony.extra.REDIRECTION_URL"));
                    break;
                case "android.telephony.extra.PCO_VALUE":
                    intent2.putExtra("pcoValue", intent.getByteArrayExtra("android.telephony.extra.PCO_VALUE"));
                    break;
            }
        }
        return intent2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Rlog.d(LOG_TAG, "[" + this.mPhone.getPhoneId() + "]" + str);
    }

    private void loge(String str) {
        this.mErrorLocalLog.log(str);
        Rlog.e(LOG_TAG, "[" + this.mPhone.getPhoneId() + "]" + str);
    }

    private void logv(String str) {
        Rlog.v(LOG_TAG, "[" + this.mPhone.getPhoneId() + "]" + str);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        printWriter.println("mCachedWakeSignalConfigs:");
        indentingPrintWriter.increaseIndent();
        for (Map.Entry entry : this.mCachedWakeSignalConfigs.entrySet()) {
            printWriter.println("signal: " + ((String) entry.getKey()) + " componentName list: " + entry.getValue());
        }
        indentingPrintWriter.decreaseIndent();
        printWriter.println("mCachedNoWakeSignalConfigs:");
        indentingPrintWriter.increaseIndent();
        for (Map.Entry entry2 : this.mCachedNoWakeSignalConfigs.entrySet()) {
            printWriter.println("signal: " + ((String) entry2.getKey()) + " componentName list: " + entry2.getValue());
        }
        indentingPrintWriter.decreaseIndent();
        printWriter.println("mDefaultNetworkAvail: " + this.mDefaultNetworkAvail);
        printWriter.println("error log:");
        indentingPrintWriter.increaseIndent();
        this.mErrorLocalLog.dump(fileDescriptor, printWriter, strArr);
        indentingPrintWriter.decreaseIndent();
    }
}
