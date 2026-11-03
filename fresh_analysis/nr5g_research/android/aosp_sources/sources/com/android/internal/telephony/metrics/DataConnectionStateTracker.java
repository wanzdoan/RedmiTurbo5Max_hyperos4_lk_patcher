package com.android.internal.telephony.metrics;

import android.telephony.PhysicalChannelConfig;
import android.telephony.PreciseDataConnectionState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.util.SparseArray;
import com.android.internal.os.BackgroundThread;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.TelephonyStatsLog;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class DataConnectionStateTracker {
    private static final SparseArray sDataConnectionStateTracker = new SparseArray();
    private Phone mPhone;
    private int mSubId;
    private TelephonyListenerImpl mTelephonyListener;
    private HashMap mLastPreciseDataConnectionState = new HashMap();
    private int mActiveDataSubId = -1;
    private int mChannelCountEnum = 0;
    private final SubscriptionManager.OnSubscriptionsChangedListener mSubscriptionsChangedListener = new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.internal.telephony.metrics.DataConnectionStateTracker.1
        @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
        public void onSubscriptionsChanged() {
            int subId;
            if (DataConnectionStateTracker.this.mPhone == null || DataConnectionStateTracker.this.mSubId == (subId = DataConnectionStateTracker.this.mPhone.getSubId()) || subId == -1) {
                return;
            }
            DataConnectionStateTracker.this.unregisterTelephonyListener();
            DataConnectionStateTracker.this.mSubId = subId;
            DataConnectionStateTracker dataConnectionStateTracker = DataConnectionStateTracker.this;
            dataConnectionStateTracker.registerTelephonyListener(dataConnectionStateTracker.mSubId);
        }
    };
    private final Executor mExecutor = BackgroundThread.getExecutor();

    private DataConnectionStateTracker() {
    }

    public static synchronized DataConnectionStateTracker getInstance(int i) {
        SparseArray sparseArray = sDataConnectionStateTracker;
        DataConnectionStateTracker dataConnectionStateTracker = (DataConnectionStateTracker) sparseArray.get(i);
        if (dataConnectionStateTracker != null) {
            return dataConnectionStateTracker;
        }
        DataConnectionStateTracker dataConnectionStateTracker2 = new DataConnectionStateTracker();
        sparseArray.put(i, dataConnectionStateTracker2);
        return dataConnectionStateTracker2;
    }

    public void start(Phone phone) {
        this.mPhone = phone;
        int subId = phone.getSubId();
        this.mSubId = subId;
        registerTelephonyListener(subId);
        SubscriptionManager subscriptionManager = (SubscriptionManager) this.mPhone.getContext().getSystemService(SubscriptionManager.class);
        if (subscriptionManager != null) {
            subscriptionManager.addOnSubscriptionsChangedListener(this.mExecutor, this.mSubscriptionsChangedListener);
        }
    }

    public void stop() {
        Phone phone = this.mPhone;
        if (phone == null) {
            return;
        }
        SubscriptionManager subscriptionManager = (SubscriptionManager) phone.getContext().getSystemService(SubscriptionManager.class);
        if (subscriptionManager != null) {
            subscriptionManager.removeOnSubscriptionsChangedListener(this.mSubscriptionsChangedListener);
        }
        unregisterTelephonyListener();
        this.mPhone = null;
        this.mLastPreciseDataConnectionState.clear();
    }

    public int getDataState(int i) {
        if (this.mLastPreciseDataConnectionState.containsKey(Integer.valueOf(i))) {
            return ((PreciseDataConnectionState) this.mLastPreciseDataConnectionState.get(Integer.valueOf(i))).getState();
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerTelephonyListener(int i) {
        TelephonyManager telephonyManager;
        if (i == -1 || (telephonyManager = (TelephonyManager) this.mPhone.getContext().getSystemService(TelephonyManager.class)) == null) {
            return;
        }
        TelephonyListenerImpl telephonyListenerImpl = new TelephonyListenerImpl(this.mExecutor);
        this.mTelephonyListener = telephonyListenerImpl;
        telephonyListenerImpl.register(telephonyManager.createForSubscriptionId(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterTelephonyListener() {
        TelephonyListenerImpl telephonyListenerImpl = this.mTelephonyListener;
        if (telephonyListenerImpl != null) {
            telephonyListenerImpl.unregister();
            this.mTelephonyListener = null;
        }
    }

    public void notifyDataConnectionStateChanged(PreciseDataConnectionState preciseDataConnectionState) {
        List<Integer> apnTypes = preciseDataConnectionState.getApnSetting().getApnTypes();
        if (apnTypes != null) {
            for (Integer num : apnTypes) {
                num.intValue();
                this.mLastPreciseDataConnectionState.put(num, preciseDataConnectionState);
            }
        }
        if (this.mPhone.getVoiceCallSessionStats() == null) {
            return;
        }
        this.mPhone.getVoiceCallSessionStats().onPreciseDataConnectionStateChanged(preciseDataConnectionState);
    }

    static int getActiveDataSubId() {
        SparseArray sparseArray = sDataConnectionStateTracker;
        if (sparseArray.size() == 0) {
            return -1;
        }
        return ((DataConnectionStateTracker) sparseArray.valueAt(0)).mActiveDataSubId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logRATChanges(int i) {
        Phone phone;
        if (this.mSubId != i || this.mActiveDataSubId == i || (phone = this.mPhone) == null || phone.getServiceStateTracker() == null) {
            return;
        }
        int currentDataRat = this.mPhone.getServiceStateTracker().getServiceStateStats().getCurrentDataRat();
        int i2 = 0;
        while (true) {
            SparseArray sparseArray = sDataConnectionStateTracker;
            if (i2 >= sparseArray.size()) {
                return;
            }
            DataConnectionStateTracker dataConnectionStateTracker = (DataConnectionStateTracker) sparseArray.valueAt(0);
            if (dataConnectionStateTracker.mSubId == this.mActiveDataSubId) {
                Phone phone2 = dataConnectionStateTracker.mPhone;
                if (phone2 == null || phone2.getServiceStateTracker() == null) {
                    return;
                }
                if (currentDataRat != dataConnectionStateTracker.mPhone.getServiceStateTracker().getServiceStateStats().getCurrentDataRat()) {
                    TelephonyStatsLog.write(854, currentDataRat);
                }
            }
            i2++;
        }
    }

    private class TelephonyListenerImpl extends TelephonyCallback implements TelephonyCallback.PreciseDataConnectionStateListener, TelephonyCallback.ActiveDataSubscriptionIdListener, TelephonyCallback.PhysicalChannelConfigListener {
        private final Executor mExecutor;
        private TelephonyManager mTelephonyManager = null;

        TelephonyListenerImpl(Executor executor) {
            this.mExecutor = executor;
        }

        public void register(TelephonyManager telephonyManager) {
            if (telephonyManager == null) {
                return;
            }
            this.mTelephonyManager = telephonyManager;
            telephonyManager.registerTelephonyCallback(this.mExecutor, this);
        }

        public void unregister() {
            TelephonyManager telephonyManager = this.mTelephonyManager;
            if (telephonyManager != null) {
                telephonyManager.unregisterTelephonyCallback(this);
                this.mTelephonyManager = null;
            }
        }

        @Override // android.telephony.TelephonyCallback.PreciseDataConnectionStateListener
        public void onPreciseDataConnectionStateChanged(PreciseDataConnectionState preciseDataConnectionState) {
            DataConnectionStateTracker.this.notifyDataConnectionStateChanged(preciseDataConnectionState);
        }

        @Override // android.telephony.TelephonyCallback.ActiveDataSubscriptionIdListener
        public void onActiveDataSubscriptionIdChanged(int i) {
            DataConnectionStateTracker.this.logRATChanges(i);
            DataConnectionStateTracker.this.mActiveDataSubId = i;
        }

        @Override // android.telephony.TelephonyCallback.PhysicalChannelConfigListener
        public void onPhysicalChannelConfigChanged(List<PhysicalChannelConfig> list) {
            logChannelChange(list);
        }

        private void logChannelChange(List list) {
            int size = list.size();
            int i = 1;
            if (size != 0 && size != 1) {
                i = 2;
                if (size != 2) {
                    i = 3;
                    if (size != 3) {
                        i = 4;
                        if (size != 4) {
                            i = 5;
                        }
                    }
                }
            }
            if (DataConnectionStateTracker.this.mChannelCountEnum != i) {
                if (DataConnectionStateTracker.this.mSubId != DataConnectionStateTracker.this.mActiveDataSubId) {
                    TelephonyStatsLog.write(882, i);
                }
                DataConnectionStateTracker.this.mChannelCountEnum = i;
            }
        }
    }
}
