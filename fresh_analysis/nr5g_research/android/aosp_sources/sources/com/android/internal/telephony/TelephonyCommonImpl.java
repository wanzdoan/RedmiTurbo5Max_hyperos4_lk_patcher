package com.android.internal.telephony;

import android.content.Context;
import android.provider.Settings;
import android.telephony.Rlog;
import android.telephony.SignalStrength;
import android.telephony.TelephonyRegistryManager;
import com.android.internal.os.BackgroundThread;
import com.android.internal.telephony.utils.NtnUtils;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyCommonImpl implements TelephonyCommonUtilsStub.ITelephonyCommonUtils {
    private Context mContext;
    private NtnUtils mNtnUtils;
    private TelephonyRegistryManager mTelephonyRegistryMgr;

    @Override // com.android.internal.telephony.TelephonyCommonUtilsStub.ITelephonyCommonUtils
    public void init(Context context) {
        this.mContext = context;
        this.mTelephonyRegistryMgr = (TelephonyRegistryManager) context.getSystemService("telephony_registry");
        this.mNtnUtils = new NtnUtils();
        Rlog.d("TelephonyCommonImpl", "TelephonyCommonImpl init!");
    }

    @Override // com.android.internal.telephony.TelephonyCommonUtilsStub.ITelephonyCommonUtils
    public boolean isSatelliteEnable(Context context) {
        try {
            boolean z = true;
            if (Settings.System.getInt(context.getContentResolver(), "satellite_state", 0) == 0 && Settings.System.getInt(context.getContentResolver(), "xingwang_satellite_state", 0) != 1) {
                z = false;
            }
            Rlog.d("TelephonyCommonImpl", "isSatelliteEnable = " + z);
            return z;
        } catch (Exception e) {
            Rlog.e("TelephonyCommonImpl", "isSatelliteEnable e: " + e);
            return false;
        }
    }

    @Override // com.android.internal.telephony.TelephonyCommonUtilsStub.ITelephonyCommonUtils
    public boolean setSatelliteEnable(Context context, int i, boolean z) {
        try {
            return Settings.System.putInt(context.getContentResolver(), "satellite_state", z ? 1 : 0);
        } catch (Exception e) {
            Rlog.e("TelephonyCommonImpl", "setSatelliteEnable e: " + e);
            return false;
        }
    }

    @Override // com.android.internal.telephony.TelephonyCommonUtilsStub.ITelephonyCommonUtils
    public SMSDispatcher getNtnSmsDispatcher(Phone phone, SmsDispatchersController smsDispatchersController) {
        if (this.mNtnUtils == null) {
            this.mNtnUtils = new NtnUtils();
        }
        return this.mNtnUtils.getNtnSmsDispatcher(phone, smsDispatchersController);
    }

    @Override // com.android.internal.telephony.TelephonyCommonUtilsStub.ITelephonyCommonUtils
    public boolean ntnInboundSmsHandlerIsWaiting(Phone phone) {
        if (this.mNtnUtils == null) {
            this.mNtnUtils = new NtnUtils();
        }
        return this.mNtnUtils.ntnInboundSmsHandlerIsWaiting(phone);
    }

    @Override // com.android.internal.telephony.TelephonyCommonUtilsStub.ITelephonyCommonUtils
    public boolean notifySignalStrength(Phone phone) {
        final int phoneId = phone.getPhoneId();
        final int subId = phone.getSubId();
        final SignalStrength signalStrength = phone.getSignalStrength();
        if (this.mNtnUtils == null) {
            this.mNtnUtils = new NtnUtils();
        }
        if (this.mNtnUtils.isDelayNotifySignalStrength(phone)) {
            Rlog.d("TelephonyCommonImpl", "notifySignalStrength: isDelayNotifySignalStrength phoneId=" + phoneId + " subId=" + subId);
            return false;
        }
        signalStrength.setMiuiLevel(this.mNtnUtils.getShowMiuiLevel(signalStrength.getMiuiLevel()));
        BackgroundThread.getHandler().post(new Runnable() { // from class: com.android.internal.telephony.TelephonyCommonImpl.1
            @Override // java.lang.Runnable
            public void run() {
                if (TelephonyCommonImpl.this.mTelephonyRegistryMgr != null) {
                    Rlog.d("TelephonyCommonImpl", "notifySignalStrength: ss=" + signalStrength + " phoneId=" + phoneId + " subId=" + subId);
                    TelephonyCommonImpl.this.mTelephonyRegistryMgr.notifySignalStrengthChanged(phoneId, subId, signalStrength);
                }
            }
        });
        return true;
    }
}
