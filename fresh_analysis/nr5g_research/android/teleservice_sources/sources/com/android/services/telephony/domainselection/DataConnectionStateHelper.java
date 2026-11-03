package com.android.services.telephony.domainselection;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.CarrierConfigManager;
import android.telephony.PreciseDataConnectionState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.telephony.data.ApnSetting;
import android.util.ArrayMap;
import android.util.Log;
import androidx.emoji2.text.ConcurrencyHelpers$$ExternalSyntheticLambda0;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;

/* JADX INFO: loaded from: classes.dex */
public class DataConnectionStateHelper extends Handler {
    private static final boolean DBG;
    private final CarrierConfigManager.CarrierConfigChangeListener mCarrierConfigChangeListener;
    private final CarrierConfigManager mConfigManager;
    private final Context mContext;
    private final ArrayMap mDataConnectionStateListeners;
    private EmergencyCallDomainSelector mSelector;
    private final TelephonyManager mTelephonyManager;

    static {
        DBG = SystemProperties.getInt("ro.debuggable", 0) == 1;
    }

    final class DataConnectionStateListener extends TelephonyCallback implements TelephonyCallback.PreciseDataConnectionStateListener {
        private final Handler mHandler;
        private final DataConnectionStateHelper mOwner;
        private final int mSlotIndex;
        private final int mSubId;
        private final TelephonyManager mTelephonyManager;
        private int mTransportType = -1;
        private int mState = -1;

        DataConnectionStateListener(Handler handler, TelephonyManager telephonyManager, DataConnectionStateHelper dataConnectionStateHelper, int i, int i2) {
            this.mHandler = handler;
            this.mTelephonyManager = telephonyManager;
            this.mOwner = dataConnectionStateHelper;
            this.mSubId = i;
            this.mSlotIndex = i2;
        }

        @Override // android.telephony.TelephonyCallback.PreciseDataConnectionStateListener
        public void onPreciseDataConnectionStateChanged(PreciseDataConnectionState preciseDataConnectionState) {
            ApnSetting apnSetting = preciseDataConnectionState.getApnSetting();
            if (apnSetting == null || (apnSetting.getApnTypeBitmask() & 512) == 0) {
                return;
            }
            this.mTransportType = preciseDataConnectionState.getTransportType();
            int state = preciseDataConnectionState.getState();
            this.mState = state;
            this.mOwner.notifyDataConnectionStateChange(this.mSlotIndex, state);
            Log.i("DataConnectionStateHelper", "onPreciseDataConnectionStateChanged ePDN state=" + this.mState + ", transport=" + this.mTransportType + ", subId=" + this.mSubId);
        }

        public void registerTelephonyCallback() {
            Log.i("DataConnectionStateHelper", "registerTelephonyCallback subId=" + this.mSubId);
            TelephonyManager telephonyManagerCreateForSubscriptionId = this.mTelephonyManager.createForSubscriptionId(this.mSubId);
            Handler handler = this.mHandler;
            Objects.requireNonNull(handler);
            telephonyManagerCreateForSubscriptionId.registerTelephonyCallback(new ConcurrencyHelpers$$ExternalSyntheticLambda0(handler), this);
        }

        public void unregisterTelephonyCallback() {
            Log.i("DataConnectionStateHelper", "unregisterTelephonyCallback subId=" + this.mSubId);
            this.mTelephonyManager.unregisterTelephonyCallback(this);
        }

        public int getSubId() {
            return this.mSubId;
        }

        public int getTransportType() {
            return this.mTransportType;
        }

        public int getState() {
            return this.mState;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, int i2, int i3, int i4) {
        onCarrierConfigChanged(i, i2, i3);
    }

    public DataConnectionStateHelper(Context context, Looper looper) {
        super(looper);
        this.mDataConnectionStateListeners = new ArrayMap();
        CarrierConfigManager.CarrierConfigChangeListener carrierConfigChangeListener = new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.services.telephony.domainselection.DataConnectionStateHelper$$ExternalSyntheticLambda0
            @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
            public final void onCarrierConfigChanged(int i, int i2, int i3, int i4) {
                this.f$0.lambda$new$0(i, i2, i3, i4);
            }
        };
        this.mCarrierConfigChangeListener = carrierConfigChangeListener;
        this.mContext = context;
        this.mTelephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
        this.mConfigManager = carrierConfigManager;
        carrierConfigManager.registerCarrierConfigChangeListener(new Executor() { // from class: com.android.services.telephony.domainselection.DataConnectionStateHelper$$ExternalSyntheticLambda1
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                this.f$0.post(runnable);
            }
        }, carrierConfigChangeListener);
    }

    public boolean isInEmergencyCallbackMode(int i) {
        Intent intentRegisterReceiver = this.mContext.registerReceiver(null, new IntentFilter("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED"));
        if (intentRegisterReceiver != null && "android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED".equals(intentRegisterReceiver.getAction())) {
            boolean booleanExtra = intentRegisterReceiver.getBooleanExtra("android.telephony.extra.PHONE_IN_ECM_STATE", false);
            int intExtra = intentRegisterReceiver.getIntExtra("android.telephony.extra.SLOT_INDEX", -1);
            Log.i("DataConnectionStateHelper", "isInEmergencyCallbackMode inEcm=" + booleanExtra + ", slotIndex=" + intExtra);
            if (booleanExtra && i == intExtra) {
                return true;
            }
        }
        return false;
    }

    public int getTransportType(int i) {
        DataConnectionStateListener dataConnectionStateListener = (DataConnectionStateListener) this.mDataConnectionStateListeners.get(Integer.valueOf(i));
        if (dataConnectionStateListener == null) {
            return -1;
        }
        Log.i("DataConnectionStateHelper", "getTransportType " + dataConnectionStateListener.getTransportType());
        return dataConnectionStateListener.getTransportType();
    }

    public int getDataConnectionState(int i) {
        DataConnectionStateListener dataConnectionStateListener = (DataConnectionStateListener) this.mDataConnectionStateListeners.get(Integer.valueOf(i));
        if (dataConnectionStateListener == null) {
            return -1;
        }
        Log.i("DataConnectionStateHelper", "getDataConnectionState " + dataConnectionStateListener.getState());
        return dataConnectionStateListener.getState();
    }

    public void setEmergencyCallDomainSelector(EmergencyCallDomainSelector emergencyCallDomainSelector) {
        this.mSelector = emergencyCallDomainSelector;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDataConnectionStateChange(int i, int i2) {
        EmergencyCallDomainSelector emergencyCallDomainSelector = this.mSelector;
        if (emergencyCallDomainSelector != null) {
            Log.i("DataConnectionStateHelper", "notifyDataConnectionStateChange slot=" + i + ", state=" + i2);
            emergencyCallDomainSelector.notifyDataConnectionStateChange(i, i2);
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        super.handleMessage(message);
    }

    private void onCarrierConfigChanged(int i, int i2, int i3) {
        Log.i("DataConnectionStateHelper", "onCarrierConfigChanged slotIndex=" + i + ", subId=" + i2 + ", carrierId=" + i3);
        if (i < 0) {
            return;
        }
        DataConnectionStateListener dataConnectionStateListener = (DataConnectionStateListener) this.mDataConnectionStateListeners.get(Integer.valueOf(i));
        if (dataConnectionStateListener != null && dataConnectionStateListener.getSubId() != i2) {
            dataConnectionStateListener.unregisterTelephonyCallback();
            this.mDataConnectionStateListeners.remove(Integer.valueOf(i));
            dataConnectionStateListener = null;
        }
        if (dataConnectionStateListener == null && SubscriptionManager.isValidSubscriptionId(i2)) {
            DataConnectionStateListener dataConnectionStateListener2 = new DataConnectionStateListener(this, this.mTelephonyManager, this, i2, i);
            dataConnectionStateListener2.registerTelephonyCallback();
            this.mDataConnectionStateListeners.put(Integer.valueOf(i), dataConnectionStateListener2);
        }
    }

    public void destroy() {
        if (DBG) {
            Log.d("DataConnectionStateHelper", "destroy");
        }
        this.mConfigManager.unregisterCarrierConfigChangeListener(this.mCarrierConfigChangeListener);
        this.mDataConnectionStateListeners.forEach(new BiConsumer() { // from class: com.android.services.telephony.domainselection.DataConnectionStateHelper$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((DataConnectionStateHelper.DataConnectionStateListener) obj2).unregisterTelephonyCallback();
            }
        });
    }
}
