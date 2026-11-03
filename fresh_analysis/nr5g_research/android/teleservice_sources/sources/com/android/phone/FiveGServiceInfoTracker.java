package com.android.phone;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.telephony.CellSignalStrengthNr;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.util.Log;
import android.util.SparseArray;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.utils.FiveGUtils;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import java.util.List;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class FiveGServiceInfoTracker {
    private static volatile FiveGServiceInfoTracker sInstance;
    ContentObserver m5gDataEnabledObserver;
    ContentObserver m5gViceEnabledObserver;
    private Handler mHandler;
    private final HandlerThread mWorkThread;
    private final SparseArray mNewServiceInfos = new SparseArray();
    private final SparseArray mOldServiceInfos = new SparseArray();
    private ServiceState[] mServiceState = new ServiceState[MiuiPhoneUtils.PHONE_COUNT];
    private boolean mIsUserFiveGEnabled = true;
    private TelephonyCallBack mTelephonyCallBack = null;
    private Context mContext = PhoneGlobals.getInstance();

    public static FiveGServiceInfoTracker make() {
        if (sInstance == null) {
            sInstance = new FiveGServiceInfoTracker();
        }
        return sInstance;
    }

    public static FiveGServiceInfo getFiveGServiceInfo(int i) {
        if (sInstance == null) {
            return null;
        }
        return sInstance.getServiceInfoInternal(i);
    }

    private FiveGServiceInfoTracker() {
        this.m5gDataEnabledObserver = new ContentObserver(this.mHandler) { // from class: com.android.phone.FiveGServiceInfoTracker.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                FiveGServiceInfoTracker.this.mHandler.obtainMessage(2, Integer.valueOf(DefaultSimManager.getDefaultDataSlotId())).sendToTarget();
            }
        };
        this.m5gViceEnabledObserver = new ContentObserver(this.mHandler) { // from class: com.android.phone.FiveGServiceInfoTracker.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                FiveGServiceInfoTracker.this.mHandler.obtainMessage(2, Integer.valueOf(FiveGUtils.getViceSlotId())).sendToTarget();
            }
        };
        HandlerThread handlerThread = new HandlerThread("FiveGServiceInfoTracker", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mHandler = new FiveGServiceHandler(handlerThread.getLooper());
        registerFivegEvents();
    }

    public class FiveGServiceHandler extends Handler {
        public FiveGServiceHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Utils.logD("FiveGServiceInfoTracker", "handleMessage msg.what = " + message.what);
            int i = message.what;
            if (i != 1) {
                if (i == 2) {
                    FiveGServiceInfoTracker.this.update5GUserEnable(((Integer) message.obj).intValue());
                    return;
                } else {
                    if (i != 3) {
                        return;
                    }
                    FiveGServiceInfoTracker.this.onSignalStrengthChanged(message.arg1, (SignalStrength) message.obj);
                    return;
                }
            }
            boolean z = ModemProvider.IS_QCOM;
            FiveGServiceInfoTracker fiveGServiceInfoTracker = FiveGServiceInfoTracker.this;
            if (z) {
                fiveGServiceInfoTracker.onServiceStateChangedQ(message.arg1, (ServiceState) message.obj);
            } else {
                fiveGServiceInfoTracker.onServiceStateChanged(message.arg1, (ServiceState) message.obj);
            }
        }
    }

    private void registerFivegEvents() {
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("fiveg_user_enable"), false, this.m5gDataEnabledObserver);
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("dual_nr_enabled"), false, this.m5gViceEnabledObserver);
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            update5GUserEnable(i);
        }
        if (this.mTelephonyCallBack == null) {
            Log.d("FiveGServiceInfoTracker", "register TelephonyCallBack");
            TelephonyCallBack telephonyCallBack = new TelephonyCallBack();
            this.mTelephonyCallBack = telephonyCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyCallBack);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void update5GUserEnable(int i) {
        if (SubscriptionManager.isValidSlotId(i)) {
            int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
            Context context = this.mContext;
            boolean z = false;
            if (i != defaultDataSlotId ? Settings.Global.getInt(context.getContentResolver(), "dual_nr_enabled", 1) == 1 : Settings.Global.getInt(context.getContentResolver(), "fiveg_user_enable", 1) == 1) {
                z = true;
            }
            FiveGServiceInfo newServiceInfo = getNewServiceInfo(i);
            newServiceInfo.setStatus(z);
            notifyListenersIfNecessary(i, newServiceInfo);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void onServiceStateChanged(int i, ServiceState serviceState) {
        int i2;
        try {
            if (isValidatePhoneId(i) && serviceState != null) {
                this.mServiceState[i] = new ServiceState(serviceState);
                int nrState = serviceState.getNrState();
                int rilDataRadioTechnology = serviceState.getRilDataRadioTechnology();
                int i3 = 1;
                if (rilDataRadioTechnology == 20) {
                    i2 = 1;
                } else {
                    i2 = 0;
                    if (nrState != 3 && nrState != 2) {
                        i3 = 0;
                        i2 = -1;
                    }
                }
                Utils.logD("FiveGServiceInfoTracker", "onServiceStateChanged[" + i + "]: rat = " + rilDataRadioTechnology + ", iconType = " + i3 + ", configType = " + i2);
                FiveGServiceInfo newServiceInfo = getNewServiceInfo(i);
                newServiceInfo.setNrConfigType(i2);
                newServiceInfo.setNrIconType(i3);
                if (i2 == 0 && nrState != 3) {
                    updateNrSignalStrength(newServiceInfo, -140, -140, nrState);
                }
                notifyListenersIfNecessary(i, newServiceInfo);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void onServiceStateChangedQ(int i, ServiceState serviceState) {
        int i2;
        if (isValidatePhoneId(i) && serviceState != null) {
            int nrState = serviceState.getNrState();
            int rilDataRadioTechnology = serviceState.getRilDataRadioTechnology();
            int i3 = 1;
            if (rilDataRadioTechnology == 20) {
                i2 = 1;
            } else {
                int i4 = (nrState == 3 || nrState == 2) ? 0 : -1;
                i2 = nrState == 3 ? 1 : -1;
                i3 = i4;
            }
            Utils.logD("FiveGServiceInfoTracker", "onServiceStateChangedQ[" + i + "]: rat = " + rilDataRadioTechnology + ", configType = " + i3 + ", bearAllocStatus = " + i2);
            FiveGServiceInfo newServiceInfo = getNewServiceInfo(i);
            newServiceInfo.setNrConfigType(i3);
            newServiceInfo.setBearAllocated(i2);
            notifyListenersIfNecessary(i, newServiceInfo);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void onSignalStrengthChanged(int i, SignalStrength signalStrength) {
        List cellSignalStrengths;
        try {
            if (isValidatePhoneId(i) && signalStrength != null) {
                FiveGServiceInfo newServiceInfo = getNewServiceInfo(i);
                int i2 = (newServiceInfo == null || !newServiceInfo.is5GConnected()) ? 0 : 3;
                CellSignalStrengthNr cellSignalStrengthNr = (i2 != 3 || (cellSignalStrengths = signalStrength.getCellSignalStrengths(CellSignalStrengthNr.class)) == null || cellSignalStrengths.size() <= 0) ? null : (CellSignalStrengthNr) cellSignalStrengths.get(0);
                Utils.logD("FiveGServiceInfoTracker", "onSignalStrengthChanged [" + i + "]: nrState " + i2);
                if (cellSignalStrengthNr != null && cellSignalStrengthNr.isValid()) {
                    updateNrSignalStrength(newServiceInfo, cellSignalStrengthNr.getSsRsrp(), cellSignalStrengthNr.getSsSinr(), i2);
                } else {
                    updateNrSignalStrength(newServiceInfo, -140, -140, i2);
                }
                notifyListenersIfNecessary(i, newServiceInfo);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void updateNrSignalStrength(FiveGServiceInfo fiveGServiceInfo, int i, int i2, int i3) {
        if (i3 == 3 && i > -140) {
            fiveGServiceInfo.setSnr(i2);
            fiveGServiceInfo.setRsrp(i);
            fiveGServiceInfo.setBearAllocated(1);
        } else {
            fiveGServiceInfo.setSnr(Integer.MAX_VALUE);
            fiveGServiceInfo.setRsrp(Integer.MAX_VALUE);
            fiveGServiceInfo.setBearAllocated(0);
        }
        Utils.logD("FiveGServiceInfoTracker", "rsrp = " + i + ", ssSnr = " + i2);
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyCallBack() {
        }

        public void onSignalStrengthChanged(int i, SignalStrength signalStrength) {
            FiveGServiceInfoTracker.this.mHandler.obtainMessage(3, i, 0, signalStrength).sendToTarget();
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            FiveGServiceInfoTracker.this.mHandler.obtainMessage(1, i, 0, serviceState).sendToTarget();
        }
    }

    private synchronized FiveGServiceInfo getServiceInfo(int i, SparseArray sparseArray) {
        FiveGServiceInfo fiveGServiceInfo;
        fiveGServiceInfo = (FiveGServiceInfo) sparseArray.get(i);
        if (fiveGServiceInfo == null) {
            Log.d("FiveGServiceInfoTracker", "getServiceInfo phoneId = " + i);
            fiveGServiceInfo = new FiveGServiceInfo();
            fiveGServiceInfo.setPhoneId(i);
            sparseArray.put(i, fiveGServiceInfo);
        }
        return fiveGServiceInfo;
    }

    private FiveGServiceInfo getNewServiceInfo(int i) {
        return getServiceInfo(i, this.mNewServiceInfos);
    }

    private FiveGServiceInfo getOldServiceInfo(int i) {
        return getServiceInfo(i, this.mOldServiceInfos);
    }

    private FiveGServiceInfo getServiceInfoInternal(int i) {
        if (isValidatePhoneId(i)) {
            return getNewServiceInfo(i);
        }
        return null;
    }

    private synchronized void notifyListenersIfNecessary(int i, FiveGServiceInfo fiveGServiceInfo) {
        try {
            FiveGServiceInfo oldServiceInfo = getOldServiceInfo(i);
            Utils.logD("FiveGServiceInfoTracker", "notifyInfoChanged newInfo=" + fiveGServiceInfo);
            if (fiveGServiceInfo.isShow5GIcon() != oldServiceInfo.isShow5GIcon()) {
                notifyListeners("fiveg_service_info_changed", i, fiveGServiceInfo);
            }
            if (!fiveGServiceInfo.equals(oldServiceInfo)) {
                oldServiceInfo.copyFrom(fiveGServiceInfo);
                notifyListeners("fiveg_service_info_changed", i, fiveGServiceInfo);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private synchronized void notifyListeners(String str, int i, FiveGServiceInfo fiveGServiceInfo) {
        Bundle bundle = new Bundle();
        bundle.putInt(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, i);
        EventHubManager.getInstance().postEvent(new TelephonyEvent.FiveGEvent(str, fiveGServiceInfo, bundle));
    }

    private boolean isValidatePhoneId(int i) {
        return i >= 0 && i < MiuiPhoneUtils.PHONE_COUNT;
    }
}
