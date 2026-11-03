package com.android.internal.telephony.vsim;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.text.TextUtils;

/* JADX INFO: loaded from: classes.dex */
public class CountryMonitor {
    private static final String TAG = "CountryMonitor";
    private boolean mIsChinaDevice;
    private boolean mIsChinaUser;
    private String mLastMcc = null;
    private boolean mIsInit = false;

    static class Holder {
        static final CountryMonitor INSTANCE = new CountryMonitor();

        private Holder() {
        }
    }

    public static CountryMonitor getInstance() {
        return Holder.INSTANCE;
    }

    private void init(Context context) {
        String deviceRegion = SystemProperties.get("ro.miui.region");
        this.mIsChinaDevice = deviceRegion.equalsIgnoreCase("CN");
        this.mIsChinaUser = this.mIsChinaDevice && getChinaFlag(context) > 0;
        this.mIsInit = true;
    }

    public void handleMccMncChange(Context context, ServiceState newSS, ServiceState ss) {
        String curMcc;
        String newOperatorNumeric = newSS.getOperatorNumeric();
        String operatorNumeric = ss.getOperatorNumeric();
        if ((this.mLastMcc != null && newOperatorNumeric != null && newOperatorNumeric.equals(operatorNumeric)) || SystemClock.elapsedRealtime() < 180000) {
            return;
        }
        if (!this.mIsInit) {
            init(context);
        }
        if (!this.mIsChinaDevice) {
            return;
        }
        if (!TextUtils.isEmpty(newOperatorNumeric) && newOperatorNumeric.length() > 3 && !newOperatorNumeric.startsWith("000")) {
            curMcc = newOperatorNumeric.substring(0, 3);
        } else {
            curMcc = getMccInLimitService();
        }
        checkMccChanged(context, curMcc);
    }

    private void checkMccChanged(Context context, String mcc) {
        if (TextUtils.isEmpty(mcc) || mcc.equals(this.mLastMcc)) {
            return;
        }
        if ("460".equals(mcc)) {
            setChinaFlag(context);
            this.mIsChinaUser = true;
        } else if (this.mIsChinaUser) {
            notifyVirtualSim(context);
        }
        this.mLastMcc = mcc;
    }

    private void notifyVirtualSim(Context context) {
        Intent intent = new Intent("com.miui.virtualsim.action.LAUNCH_RECEIVER");
        intent.setPackage("com.miui.virtualsim");
        intent.putExtra("extra_launch_type", "type_country_change");
        intent.putExtra("extra_launch_channel", "virtualsim_channel_phone");
        intent.addFlags(32);
        context.sendBroadcast(intent);
        Rlog.i(TAG, "send mcc change broadcast");
    }

    public String getMccInLimitService() {
        String network_mcc = SystemProperties.get("ril.limit_service_mnc", "");
        String mcc_mtk = SystemProperties.get("ril.limit_service_mcc", "");
        if (!TextUtils.isEmpty(network_mcc) && network_mcc.length() >= 3) {
            String mcc = network_mcc.substring(network_mcc.length() - 3, network_mcc.length());
            try {
                Integer.parseInt(mcc);
                return mcc;
            } catch (NumberFormatException e) {
                return null;
            }
        }
        if (!TextUtils.isEmpty(mcc_mtk) && mcc_mtk.length() == 3) {
            try {
                Integer.parseInt(mcc_mtk);
                return mcc_mtk;
            } catch (NumberFormatException e2) {
                return null;
            }
        }
        return null;
    }

    private void setChinaFlag(Context context) {
        Settings.System.putInt(context.getContentResolver(), "user_china_flag", 1);
    }

    private int getChinaFlag(Context context) {
        return Settings.System.getInt(context.getContentResolver(), "user_china_flag", 0);
    }
}
