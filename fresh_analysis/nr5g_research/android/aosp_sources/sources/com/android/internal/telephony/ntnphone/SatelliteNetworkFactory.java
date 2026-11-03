package com.android.internal.telephony.ntnphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.NetworkScore;
import android.net.TelephonyNetworkSpecifier;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.CallFailCause;
import com.android.internal.telephony.IndentingPrintWriter;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.NetworkFactory;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.data.AccessNetworksManager;
import com.android.internal.telephony.data.TelephonyNetworkRequest;
import com.android.internal.telephony.metrics.NetworkRequestsStats;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteNetworkFactory extends NetworkFactory {
    protected static final int ACTION_NO_OP = 0;
    protected static final int ACTION_RELEASE = 2;
    protected static final int ACTION_REQUEST = 1;
    protected static final boolean DBG = true;
    public static final int EVENT_SATELLITE_DATA_SWITCH = 1;
    public static final int EVENT_SUBSCRIPTION_CHANGED = 2;
    public static final String KEY_SATELLITE_STATE_CHANGE_PHONE_ID = "phone_id";
    protected String LOG_TAG;
    private final AccessNetworksManager mAccessNetworksManager;
    private BroadcastReceiver mBroadcastReceiver;
    private final FeatureFlags mFlags;
    public Handler mInternalHandler;
    private final LocalLog mLocalLog;
    protected final Map<TelephonyNetworkRequest, Integer> mNetworkRequests;
    protected final Phone mPhone;
    ContentObserver mSatelliteDataObserver;
    protected int mSatellitePhoneId;
    protected int mSubscriptionId;

    protected static int getAction(boolean z, boolean z2) {
        if (z || !z2) {
            return (!z || z2) ? 0 : 2;
        }
        return 1;
    }

    public SatelliteNetworkFactory(Looper looper, Phone phone, FeatureFlags featureFlags) {
        super(looper, phone.getContext(), "MiSatelliteNetworkFactory[" + phone.getPhoneId() + "]", null);
        this.mLocalLog = new LocalLog(CallFailCause.RADIO_UPLINK_FAILURE);
        this.mNetworkRequests = new HashMap();
        this.mSatellitePhoneId = -1;
        this.mSatelliteDataObserver = new ContentObserver(this.mInternalHandler) { // from class: com.android.internal.telephony.ntnphone.SatelliteNetworkFactory.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                SatelliteNetworkFactory.this.logl("SatelliteDataChange");
                SatelliteNetworkFactory.this.mInternalHandler.sendEmptyMessage(1);
            }
        };
        this.mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.ntnphone.SatelliteNetworkFactory.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action != null && "com.android.app.action.SATELLITE_STATE_CHANGE".equals(action)) {
                    SatelliteNetworkFactory.this.mSatellitePhoneId = intent.getIntExtra(SatelliteNetworkFactory.KEY_SATELLITE_STATE_CHANGE_PHONE_ID, -1);
                    SatelliteNetworkFactory.this.logl("ACTION_SATELLITE_STATE_CHANGE phoneId: " + SatelliteNetworkFactory.this.mSatellitePhoneId);
                }
            }
        };
        this.mPhone = phone;
        this.mFlags = featureFlags;
        this.mInternalHandler = new InternalHandler(looper);
        this.mAccessNetworksManager = phone.getAccessNetworksManager();
        setCapabilityFilter(makeNetworkFilterByPhoneId(phone.getPhoneId()));
        this.LOG_TAG = "MiSatelliteNetworkFactory[" + phone.getPhoneId() + "]";
        this.mSubscriptionId = -1;
        ((SubscriptionManager) phone.getContext().getSystemService(SubscriptionManager.class)).addOnSubscriptionsChangedListener(new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.internal.telephony.ntnphone.SatelliteNetworkFactory.1
            @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
            public void onSubscriptionsChanged() {
                SatelliteNetworkFactory.this.mInternalHandler.sendEmptyMessage(2);
            }
        });
        register();
        registerListener();
        registerBroadcastReceiver();
    }

    private NetworkCapabilities makeNetworkFilterByPhoneId(int i) {
        return makeNetworkFilter(SubscriptionManager.getSubscriptionId(i));
    }

    public NetworkCapabilities makeNetworkFilter(int i) {
        return new NetworkCapabilities.Builder().addTransportType(10).addCapability(2).addCapability(3).addCapability(5).addCapability(7).addCapability(8).addCapability(33).addCapability(9).addCapability(29).addCapability(10).addCapability(13).addCapability(28).addCapability(12).addCapability(23).addCapability(34).addCapability(35).addEnterpriseId(1).addEnterpriseId(2).addEnterpriseId(3).addEnterpriseId(4).addEnterpriseId(5).setNetworkSpecifier(new TelephonyNetworkSpecifier.Builder().setSubscriptionId(i).build()).build();
    }

    protected class InternalHandler extends Handler {
        protected InternalHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                SatelliteNetworkFactory.this.onSatelliteDataSwitch();
                return;
            }
            if (i == 2) {
                SatelliteNetworkFactory.this.onSubIdChange();
            } else if (i == 3) {
                SatelliteNetworkFactory.this.onNeedNetworkFor(message);
            } else {
                if (i != 4) {
                    return;
                }
                SatelliteNetworkFactory.this.onReleaseNetworkFor(message);
            }
        }
    }

    protected int getTransportTypeFromNetworkRequest(TelephonyNetworkRequest telephonyNetworkRequest) {
        int highestPriorityApnTypeNetworkCapability = telephonyNetworkRequest.getHighestPriorityApnTypeNetworkCapability();
        if (highestPriorityApnTypeNetworkCapability >= 0) {
            return this.mAccessNetworksManager.getPreferredTransportByNetworkCapability(highestPriorityApnTypeNetworkCapability);
        }
        return 1;
    }

    public void registerListener() {
        this.mPhone.getContext().getContentResolver().registerContentObserver(Settings.System.getUriFor("satellite_data_enabled"), false, this.mSatelliteDataObserver);
    }

    public void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.app.action.SATELLITE_STATE_CHANGE");
        this.mPhone.getContext().registerReceiver(this.mBroadcastReceiver, intentFilter, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSatelliteDataSwitch() {
        logl("onSatelliteDataSwitch");
        int i = this.mSubscriptionId;
        SubscriptionManager.from(this.mPhone.getContext());
        if (i == SubscriptionManager.getActiveDataSubscriptionId()) {
            logl("onSatelliteDataSwitch: set primary flag for phoneId: " + this.mPhone.getPhoneId());
            setScoreFilter(new NetworkScore.Builder().setLegacyInt(50).setTransportPrimary(true).build());
        } else {
            setScoreFilter(new NetworkScore.Builder().setLegacyInt(50).setTransportPrimary(false).build());
        }
        logl("onSatelliteDataSwitch: shouldApply = " + isSatelliteDataEnabled() + ", mNetworkRequests = " + this.mNetworkRequests.size());
        for (Map.Entry<TelephonyNetworkRequest, Integer> entry : this.mNetworkRequests.entrySet()) {
            TelephonyNetworkRequest key = entry.getKey();
            boolean z = entry.getValue().intValue() != -1;
            boolean zIsSatelliteDataEnabled = isSatelliteDataEnabled();
            int action = getAction(z, zIsSatelliteDataEnabled);
            if (action != 0) {
                StringBuilder sb = new StringBuilder("onSatelliteDataSwitch: ");
                sb.append(action == 1 ? "Requesting" : "Releasing");
                sb.append(" network request ");
                sb.append(key);
                logl(sb.toString());
                int transportTypeFromNetworkRequest = getTransportTypeFromNetworkRequest(key);
                if (action == 1) {
                    Settings.Global.putInt(this.mPhone.getContext().getContentResolver(), "captive_portal_mode", 0);
                    NetworkRequestsStats.addNetworkRequest(key.getNativeNetworkRequest(), this.mSubscriptionId);
                    this.mPhone.getDataNetworkController().addNetworkRequest(key);
                } else if (action == 2) {
                    Settings.Global.putInt(this.mPhone.getContext().getContentResolver(), "captive_portal_mode", 1);
                    this.mPhone.getDataNetworkController().removeNetworkRequest(key);
                }
                this.mNetworkRequests.put(key, Integer.valueOf(zIsSatelliteDataEnabled ? transportTypeFromNetworkRequest : -1));
                logl("onSatelliteDataSwitch: shouldApply = " + zIsSatelliteDataEnabled + ", transportType = " + transportTypeFromNetworkRequest);
            }
        }
    }

    private boolean isSatelliteDataEnabled() {
        return this.mSatellitePhoneId == this.mPhone.getPhoneId() && Settings.System.getInt(this.mPhone.getContext().getContentResolver(), "satellite_data_enabled", 0) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSubIdChange() {
        int subscriptionId = SubscriptionManager.getSubscriptionId(this.mPhone.getPhoneId());
        if (this.mSubscriptionId != subscriptionId) {
            logl("onSubIdChange " + this.mSubscriptionId + "->" + subscriptionId);
            this.mSubscriptionId = subscriptionId;
            setCapabilityFilter(makeNetworkFilter(subscriptionId));
        }
    }

    @Override // com.android.internal.telephony.NetworkFactory
    public void needNetworkFor(NetworkRequest networkRequest) {
        if (networkRequest.hasTransport(10)) {
            Message messageObtainMessage = this.mInternalHandler.obtainMessage(3);
            messageObtainMessage.obj = networkRequest;
            messageObtainMessage.sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onNeedNetworkFor(Message message) {
        TelephonyNetworkRequest telephonyNetworkRequest = new TelephonyNetworkRequest((NetworkRequest) message.obj, this.mPhone, this.mFlags);
        boolean zIsSatelliteDataEnabled = isSatelliteDataEnabled();
        this.mNetworkRequests.put(telephonyNetworkRequest, Integer.valueOf(zIsSatelliteDataEnabled ? getTransportTypeFromNetworkRequest(telephonyNetworkRequest) : -1));
        logl("onNeedNetworkFor " + telephonyNetworkRequest + " shouldApply " + zIsSatelliteDataEnabled);
        if (zIsSatelliteDataEnabled) {
            NetworkRequestsStats.addNetworkRequest(telephonyNetworkRequest.getNativeNetworkRequest(), this.mSubscriptionId);
            this.mPhone.getDataNetworkController().addNetworkRequest(telephonyNetworkRequest);
        }
    }

    @Override // com.android.internal.telephony.NetworkFactory
    public void releaseNetworkFor(NetworkRequest networkRequest) {
        if (networkRequest.hasTransport(10)) {
            Message messageObtainMessage = this.mInternalHandler.obtainMessage(4);
            messageObtainMessage.obj = networkRequest;
            messageObtainMessage.sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onReleaseNetworkFor(Message message) {
        TelephonyNetworkRequest telephonyNetworkRequest = new TelephonyNetworkRequest((NetworkRequest) message.obj, this.mPhone, this.mFlags);
        if (this.mNetworkRequests.containsKey(telephonyNetworkRequest)) {
            boolean z = this.mNetworkRequests.get(telephonyNetworkRequest).intValue() != -1;
            boolean zIsSatelliteDataEnabled = isSatelliteDataEnabled();
            logl("onReleaseNetworkFor " + telephonyNetworkRequest + " applied = " + z + " shouldApply = " + zIsSatelliteDataEnabled);
            if (zIsSatelliteDataEnabled) {
                return;
            }
            this.mNetworkRequests.remove(telephonyNetworkRequest);
            if (z) {
                this.mPhone.getDataNetworkController().removeNetworkRequest(telephonyNetworkRequest);
            }
        }
    }

    @Override // com.android.internal.telephony.NetworkFactory
    protected void log(String str) {
        Rlog.d(this.LOG_TAG, str);
    }

    protected void logl(String str) {
        log(str);
        this.mLocalLog.log(str);
    }

    @Override // com.android.internal.telephony.NetworkFactory
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("MiSatelliteNetworkFactory-" + this.mPhone.getPhoneId());
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("Network Requests:");
        indentingPrintWriter.increaseIndent();
        for (Map.Entry<TelephonyNetworkRequest, Integer> entry : this.mNetworkRequests.entrySet()) {
            TelephonyNetworkRequest key = entry.getKey();
            int iIntValue = entry.getValue().intValue();
            StringBuilder sb = new StringBuilder();
            sb.append(key);
            sb.append(iIntValue != -1 ? " applied on " + iIntValue : " not applied");
            indentingPrintWriter.println(sb.toString());
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.print("Local logs:");
        indentingPrintWriter.increaseIndent();
        this.mLocalLog.dump(fileDescriptor, indentingPrintWriter, strArr);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.decreaseIndent();
    }
}
