package com.android.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import miui.telephony.MiuiRadioStallDetector;
import miui.telephony.MiuiTelephony;
import miui.telephony.TelephonyManagerEx;
import miui.telephony.TelephonyOptCloudController;

/* JADX INFO: loaded from: classes.dex */
public class MiuiRadioControllerImpl implements MiuiRadioControllerStub.IMiuiRadioController, TelephonyOptCloudController.CloudInfoListener {
    private static final String ACTION_START_AOL_SETTING = "com.android.phone.intent.action.start_aol";
    private static final String LOG_TAG = "MiuiRadioControllerImpl";
    private Context mContext;
    private boolean mIsCloudDisableFeature;
    private RIL mOwner;
    private MiuiRadioStallDetector mRadioStallDetector;
    private int mPhoneId = -1;
    private boolean mIsAolSettingStart = false;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.MiuiRadioControllerImpl.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (MiuiRadioControllerImpl.ACTION_START_AOL_SETTING.equals(intent.getAction())) {
                MiuiRadioControllerImpl.log("received aol setting start action, phoneId = " + MiuiRadioControllerImpl.this.mPhoneId);
                MiuiRadioControllerImpl.this.mIsAolSettingStart = true;
            }
        }
    };

    public void init(RIL owner, Context context, Object... arg) {
        this.mOwner = owner;
        this.mPhoneId = owner.mPhoneId.intValue();
        this.mContext = context;
        if (this.mRadioStallDetector == null) {
            this.mRadioStallDetector = new MiuiRadioStallDetector(owner, context, this.mPhoneId);
        }
        if (InternalTelephonyUtils.isSupportRadioStall()) {
            this.mRadioStallDetector.start();
        }
        TelephonyOptCloudController.getInstance().addCloudInfoListener(this);
        MiuiTelephony.getInstance().registerTelephonyPlugins(this, this.mPhoneId);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_START_AOL_SETTING);
        this.mContext.registerReceiver(this.mReceiver, filter, 2);
    }

    public void dispose(RIL owner) {
        this.mContext.unregisterReceiver(this.mReceiver);
        if (this.mRadioStallDetector != null) {
            this.mRadioStallDetector.stop();
        }
        TelephonyOptCloudController.getInstance().removeCloudInfoListener(this);
        MiuiTelephony.getInstance().unregisterTelephonyPlugins(this, owner.mPhoneId.intValue());
    }

    public void doRilMessageTimeOut() {
        MiuiNetworkControllerImpl networkImpl;
        if (MiuiTelephony.IS_QCOM && (networkImpl = (MiuiNetworkControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiNetworkControllerImpl.class, this.mPhoneId)) != null) {
            networkImpl.doRilMessageTimeOut(this.mOwner.getRilRequestList());
        }
        if (this.mRadioStallDetector != null) {
            this.mRadioStallDetector.doRilMessageTimeOut();
        }
    }

    public void processResponseInternal() {
        if (this.mRadioStallDetector != null) {
            this.mRadioStallDetector.processResponseInternal();
        }
    }

    public void processIndication() {
        if (this.mRadioStallDetector != null) {
            this.mRadioStallDetector.processIndication();
        }
    }

    @Override // miui.telephony.TelephonyOptCloudController.CloudInfoListener
    public void onCloudInfoChange() {
        boolean change = this.mIsCloudDisableFeature != TelephonyOptCloudController.CLOUD_CONTROLLER_RILD_STALL_DISABLE;
        log("cloud info change, Optimization cloud info change:" + change);
        if (change) {
            this.mIsCloudDisableFeature = TelephonyOptCloudController.CLOUD_CONTROLLER_RILD_STALL_DISABLE;
            if (this.mRadioStallDetector != null) {
                boolean z = this.mIsCloudDisableFeature;
                MiuiRadioStallDetector miuiRadioStallDetector = this.mRadioStallDetector;
                if (!z) {
                    miuiRadioStallDetector.start();
                } else {
                    miuiRadioStallDetector.stop();
                }
            }
        }
    }

    public boolean skipRadioStateIfNeed(int radioState) {
        if (radioState != 2) {
            this.mIsAolSettingStart = false;
        }
        if (!this.mIsAolSettingStart || !TelephonyManagerEx.getDefault().isSatelliteEnabled() || radioState != 2) {
            return false;
        }
        log("skip radio unavailable under satellite, phoneId = " + this.mPhoneId);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String msg) {
        Log.d(LOG_TAG, msg);
    }
}
