package com.android.internal.telephony;

import android.telephony.CellIdentity;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyRegistryManager;
import com.android.internal.os.BackgroundThread;

/* JADX INFO: loaded from: classes.dex */
public class CommonDefaultPhoneNotifierImpl implements MiuiDefaultPhoneNotifierStub.IMiuiDefaultPhoneNotifier {
    private TelephonyRegistryManager mTelephonyRegistryMgr;

    @Override // com.android.internal.telephony.MiuiDefaultPhoneNotifierStub.IMiuiDefaultPhoneNotifier
    public void init(TelephonyRegistryManager telephonyRegistryManager) {
        this.mTelephonyRegistryMgr = telephonyRegistryManager;
    }

    @Override // com.android.internal.telephony.MiuiDefaultPhoneNotifierStub.IMiuiDefaultPhoneNotifier
    public void notifyServiceStateForSubId(Phone phone, final ServiceState serviceState, final int i) {
        final int phoneId = phone.getPhoneId();
        BackgroundThread.getHandler().post(new Runnable() { // from class: com.android.internal.telephony.CommonDefaultPhoneNotifierImpl.1
            @Override // java.lang.Runnable
            public void run() {
                if (CommonDefaultPhoneNotifierImpl.this.mTelephonyRegistryMgr != null) {
                    CommonDefaultPhoneNotifierImpl.this.mTelephonyRegistryMgr.notifyServiceStateChanged(phoneId, i, serviceState);
                }
                TelephonyStatAdapter.notifyServiceState(phoneId, serviceState);
            }
        });
    }

    @Override // com.android.internal.telephony.MiuiDefaultPhoneNotifierStub.IMiuiDefaultPhoneNotifier
    public void notifyPreciseCallState(Phone phone) {
        int phoneId = phone.getPhoneId();
        Call foregroundCall = phone.getForegroundCall();
        if (foregroundCall != null) {
            TelephonyStatAdapter.notifyPreciseCallState(phoneId, DefaultPhoneNotifier.convertPreciseCallState(foregroundCall.getState()));
        }
    }

    @Override // com.android.internal.telephony.MiuiDefaultPhoneNotifierStub.IMiuiDefaultPhoneNotifier
    public boolean notifySignalStrength(Phone phone) {
        final int phoneId = phone.getPhoneId();
        final int subId = phone.getSubId();
        final SignalStrength signalStrength = phone.getSignalStrength();
        BackgroundThread.getHandler().post(new Runnable() { // from class: com.android.internal.telephony.CommonDefaultPhoneNotifierImpl.2
            @Override // java.lang.Runnable
            public void run() {
                if (CommonDefaultPhoneNotifierImpl.this.mTelephonyRegistryMgr != null) {
                    Rlog.d("CommonDefaultPhoneNotifierImpl", "notifySignalStrength: ss=" + signalStrength + " phoneId=" + phoneId + " subId=" + subId);
                    CommonDefaultPhoneNotifierImpl.this.mTelephonyRegistryMgr.notifySignalStrengthChanged(phoneId, subId, signalStrength);
                }
                TelephonyStatAdapter.notifySignalStrength(phoneId, signalStrength);
            }
        });
        return true;
    }

    @Override // com.android.internal.telephony.MiuiDefaultPhoneNotifierStub.IMiuiDefaultPhoneNotifier
    public void notifyCellLocation(Phone phone, final CellIdentity cellIdentity) {
        final int phoneId = phone.getPhoneId();
        final int subId = phone.getSubId();
        BackgroundThread.getHandler().post(new Runnable() { // from class: com.android.internal.telephony.CommonDefaultPhoneNotifierImpl.3
            @Override // java.lang.Runnable
            public void run() {
                if (CommonDefaultPhoneNotifierImpl.this.mTelephonyRegistryMgr != null) {
                    CommonDefaultPhoneNotifierImpl.this.mTelephonyRegistryMgr.notifyCellLocation(subId, cellIdentity);
                }
                TelephonyStatAdapter.notifyCellLocation(phoneId, cellIdentity.asCellLocation());
            }
        });
    }
}
