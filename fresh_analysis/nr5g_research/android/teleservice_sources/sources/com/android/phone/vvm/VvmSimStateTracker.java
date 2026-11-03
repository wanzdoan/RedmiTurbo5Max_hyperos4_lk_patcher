package com.android.phone.vvm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.HandlerThread;
import android.os.SystemProperties;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.android.phone.PhoneUtils;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class VvmSimStateTracker extends BroadcastReceiver {
    private static final Handler sBackgroundHandler;
    private static Map sListeners;
    private static Set sPreBootHandles;
    private static final HandlerThread sWorkerThread;

    static {
        HandlerThread handlerThread = new HandlerThread("VvmSimStateTrackerBackgroundWorker");
        sWorkerThread = handlerThread;
        handlerThread.start();
        sBackgroundHandler = new Handler(handlerThread.getLooper());
        sListeners = new ArrayMap();
        sPreBootHandles = new ArraySet();
    }

    class ServiceStateListener extends TelephonyCallback implements TelephonyCallback.ServiceStateListener {
        private final Context mContext;
        private final PhoneAccountHandle mPhoneAccountHandle;

        public ServiceStateListener(Context context, PhoneAccountHandle phoneAccountHandle) {
            this.mContext = context;
            this.mPhoneAccountHandle = phoneAccountHandle;
        }

        public void listen() {
            TelephonyManager telephonyManager = VvmSimStateTracker.getTelephonyManager(this.mContext, this.mPhoneAccountHandle);
            if (telephonyManager == null) {
                VvmLog.e("VvmSimStateTracker", "Cannot create TelephonyManager from " + this.mPhoneAccountHandle);
                return;
            }
            telephonyManager.registerTelephonyCallback(0, new HandlerExecutor(VvmSimStateTracker.sBackgroundHandler), this);
        }

        public void unlisten() {
            ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).unregisterTelephonyCallback(this);
            VvmSimStateTracker.sListeners.put(this.mPhoneAccountHandle, null);
        }

        @Override // android.telephony.TelephonyCallback.ServiceStateListener
        public void onServiceStateChanged(ServiceState serviceState) {
            if (serviceState.getState() == 0) {
                VvmLog.i("VvmSimStateTracker", "in service");
                VvmSimStateTracker.this.sendConnected(this.mContext, this.mPhoneAccountHandle);
                unlisten();
            }
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(final Context context, final Intent intent) {
        final BroadcastReceiver.PendingResult pendingResultGoAsync = goAsync();
        sBackgroundHandler.post(new Runnable() { // from class: com.android.phone.vvm.VvmSimStateTracker$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onReceive$0(context, intent, pendingResultGoAsync);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onReceive$0(Context context, Intent intent, BroadcastReceiver.PendingResult pendingResult) {
        try {
            handleIntent(context, intent);
        } finally {
            pendingResult.finish();
        }
    }

    private void handleIntent(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) {
            VvmLog.w("VvmSimStateTracker", "onReceive: Null action for intent.");
        }
        VvmLog.i("VvmSimStateTracker", action);
        action.hashCode();
        switch (action) {
            case "android.telephony.action.CARRIER_CONFIG_CHANGED":
                int intExtra = intent.getIntExtra("subscription", -1);
                if (!SubscriptionManager.isValidSubscriptionId(intExtra)) {
                    VvmLog.i("VvmSimStateTracker", "onReceive: Received carrier config for invalid sub id.");
                    checkRemovedSim(context);
                    break;
                } else {
                    if (isBootstrapSubscription(context, intExtra)) {
                        VvmLog.i("VvmSimStateTracker", "onReceive: carrier config changed. Ignore bootstrap SIM with subId=" + intExtra);
                    } else {
                        PhoneAccountHandle phoneAccountHandleFromSubId = PhoneAccountHandleConverter.fromSubId(intExtra);
                        if ("null".equals(phoneAccountHandleFromSubId.getId())) {
                            VvmLog.e("VvmSimStateTracker", "onReceive: null phone account handle ID, possible modem crash. Ignoring carrier config changed event");
                        } else {
                            VvmLog.i("VvmSimStateTracker", "onReceive: ACTION_CARRIER_CONFIG_CHANGED; subId=" + intExtra);
                            onCarrierConfigChanged(context, phoneAccountHandleFromSubId);
                        }
                    }
                    break;
                }
                break;
            case "android.intent.action.SIM_STATE_CHANGED":
                if ("ABSENT".equals(intent.getStringExtra("ss"))) {
                    VvmLog.i("VvmSimStateTracker", "onReceive: ACTION_SIM_STATE_CHANGED");
                    checkRemovedSim(context);
                    break;
                }
                break;
            case "android.intent.action.BOOT_COMPLETED":
                VvmLog.i("VvmSimStateTracker", "onReceive: ACTION_BOOT_COMPLETED");
                onBootCompleted(context);
                break;
        }
    }

    private void onBootCompleted(Context context) {
        for (PhoneAccountHandle phoneAccountHandle : sPreBootHandles) {
            TelephonyManager telephonyManager = getTelephonyManager(context, phoneAccountHandle);
            if (telephonyManager != null) {
                if (telephonyManager.getServiceState().getState() == 0) {
                    sListeners.put(phoneAccountHandle, null);
                    sendConnected(context, phoneAccountHandle);
                } else {
                    listenToAccount(context, phoneAccountHandle);
                }
            }
        }
        sPreBootHandles.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendConnected(Context context, PhoneAccountHandle phoneAccountHandle) {
        VvmLog.i("VvmSimStateTracker", "sendConnected: Service connected on " + phoneAccountHandle);
        RemoteVvmTaskManager.startCellServiceConnected(context, phoneAccountHandle);
    }

    private void checkRemovedSim(Context context) {
        SubscriptionManager subscriptionManagerFrom = SubscriptionManager.from(context);
        if (!isBootCompleted()) {
            for (PhoneAccountHandle phoneAccountHandle : sPreBootHandles) {
                if (!PhoneUtils.isPhoneAccountActive(subscriptionManagerFrom, phoneAccountHandle)) {
                    sPreBootHandles.remove(phoneAccountHandle);
                }
            }
            return;
        }
        ArraySet arraySet = new ArraySet();
        for (PhoneAccountHandle phoneAccountHandle2 : sListeners.keySet()) {
            if (!PhoneUtils.isPhoneAccountActive(subscriptionManagerFrom, phoneAccountHandle2)) {
                arraySet.add(phoneAccountHandle2);
                ServiceStateListener serviceStateListener = (ServiceStateListener) sListeners.get(phoneAccountHandle2);
                if (serviceStateListener != null) {
                    serviceStateListener.unlisten();
                }
                sendSimRemoved(context, phoneAccountHandle2);
            }
        }
        Iterator it = arraySet.iterator();
        while (it.hasNext()) {
            sListeners.remove((PhoneAccountHandle) it.next());
        }
    }

    private boolean isBootCompleted() {
        return SystemProperties.getBoolean("sys.boot_completed", false);
    }

    private void sendSimRemoved(Context context, PhoneAccountHandle phoneAccountHandle) {
        VvmLog.i("VvmSimStateTracker", "sendSimRemoved: Sim removed on " + phoneAccountHandle);
        RemoteVvmTaskManager.startSimRemoved(context, phoneAccountHandle);
    }

    private void onCarrierConfigChanged(Context context, PhoneAccountHandle phoneAccountHandle) {
        if (!isBootCompleted()) {
            sPreBootHandles.add(phoneAccountHandle);
            return;
        }
        TelephonyManager telephonyManager = getTelephonyManager(context, phoneAccountHandle);
        if (telephonyManager == null) {
            VvmLog.e("VvmSimStateTracker", "Cannot create TelephonyManager from " + phoneAccountHandle + ", subId=" + ((TelephonyManager) context.getSystemService(TelephonyManager.class)).getSubIdForPhoneAccount(((TelecomManager) context.getSystemService(TelecomManager.class)).getPhoneAccount(phoneAccountHandle)));
            return;
        }
        ServiceState serviceState = telephonyManager.getServiceState();
        if (serviceState != null && serviceState.getState() == 0) {
            VvmLog.i("VvmSimStateTracker", "onCarrierConfigChanged: in service; send connected " + phoneAccountHandle);
            sendConnected(context, phoneAccountHandle);
            sListeners.put(phoneAccountHandle, null);
            return;
        }
        listenToAccount(context, phoneAccountHandle);
    }

    private void listenToAccount(Context context, PhoneAccountHandle phoneAccountHandle) {
        ServiceStateListener serviceStateListener = new ServiceStateListener(context, phoneAccountHandle);
        serviceStateListener.listen();
        VvmLog.i("VvmSimStateTracker", "listenToAccount: " + phoneAccountHandle);
        sListeners.put(phoneAccountHandle, serviceStateListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static TelephonyManager getTelephonyManager(Context context, PhoneAccountHandle phoneAccountHandle) {
        return ((TelephonyManager) context.getSystemService(TelephonyManager.class)).createForPhoneAccountHandle(phoneAccountHandle);
    }

    private boolean isBootstrapSubscription(Context context, int i) {
        SubscriptionInfo activeSubscriptionInfo;
        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        if (subscriptionManager == null || (activeSubscriptionInfo = subscriptionManager.getActiveSubscriptionInfo(i)) == null || activeSubscriptionInfo.getProfileClass() != 1) {
            return false;
        }
        VvmLog.i("VvmSimStateTracker", "isBootstrapSubscription: Found bootstrap subscription subId=" + i);
        return true;
    }
}
