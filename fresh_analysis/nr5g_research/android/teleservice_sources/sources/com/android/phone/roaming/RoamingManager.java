package com.android.phone.roaming;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.roaming.utils.RoamingUtils;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import miui.os.Build;
import miui.telephony.ServiceProviderUtils;

/* JADX INFO: loaded from: classes.dex */
public class RoamingManager {
    private static Context mContext;
    private static RoamingManager sInstance;
    private int[] mRoamingMode = new int[MiuiPhoneUtils.PHONE_COUNT];

    private RoamingManager() {
        internalInit();
    }

    public static void init(Context context) {
        if (sInstance == null) {
            sInstance = new RoamingManager();
        }
        mContext = context;
    }

    private void internalInit() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            this.mRoamingMode[i] = -1;
        }
    }

    public static RoamingManager getInstance() {
        return sInstance;
    }

    public void processServiceState(int i, ServiceState serviceState) {
        Phone phone = PhoneFactory.getPhone(i);
        if (serviceState == null || phone == null) {
            Log.i("RoamingManager", "InValid service state update.");
            return;
        }
        int i2 = this.mRoamingMode[i];
        String operatorNumeric = phone.getOperatorNumeric();
        String networkOperatorNumeric = RoamingUtils.getNetworkOperatorNumeric(serviceState);
        boolean zIsHomeNetwork = isHomeNetwork(operatorNumeric, networkOperatorNumeric);
        Log.i("RoamingManager", "processServiceState phoneId: " + i + " simOperatorNumeric:" + operatorNumeric + " networkOperatorNumeric:" + networkOperatorNumeric + " isHome:" + zIsHomeNetwork);
        if (zIsHomeNetwork) {
            this.mRoamingMode[i] = 0;
            if (!Build.IS_INTERNATIONAL_BUILD && ((!TextUtils.isEmpty(operatorNumeric) && !ServiceProviderUtils.isChineseServiceProvider(operatorNumeric)) || (!TextUtils.isEmpty(networkOperatorNumeric) && !ServiceProviderUtils.isChineseServiceProvider(networkOperatorNumeric)))) {
                this.mRoamingMode[i] = 2;
            } else if (TelephonyManager.getSimStateForSlotIndex(i) == 1) {
                this.mRoamingMode[i] = -1;
            }
        } else if (ServiceProviderUtils.isChineseServiceProvider(operatorNumeric)) {
            this.mRoamingMode[i] = 1;
        } else {
            boolean zIsChineseServiceProvider = ServiceProviderUtils.isChineseServiceProvider(networkOperatorNumeric);
            int[] iArr = this.mRoamingMode;
            if (zIsChineseServiceProvider) {
                iArr[i] = 4;
            } else {
                iArr[i] = 3;
            }
        }
        Log.i("RoamingManager", "processServiceState lastRoamingMode: " + i2 + " newRoamingMode:" + this.mRoamingMode[i]);
        int i3 = this.mRoamingMode[i];
        if (i2 == i3 || i3 == -1) {
            return;
        }
        notifyRoamingModeChanged(i, operatorNumeric, networkOperatorNumeric);
    }

    private boolean isHomeNetwork(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || str.length() < 3) {
            return true;
        }
        return str2.startsWith(str.substring(0, 3));
    }

    public int getRoamingModeForPhoneId(int i) {
        if (i < 0 || i >= MiuiPhoneUtils.PHONE_COUNT) {
            return -1;
        }
        return this.mRoamingMode[i];
    }

    public int[] getRoamingMode() {
        return this.mRoamingMode;
    }

    private void notifyRoamingModeChanged(int i, String str, String str2) {
        Log.i("RoamingManager", "notifyRoamingModeChanged");
        Bundle bundle = new Bundle();
        bundle.putInt(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, i);
        bundle.putInt("roaming_mode", this.mRoamingMode[i]);
        EventHubManager.getInstance().postEvent(new TelephonyEvent.NetworkEvent("roaming_mode_changed", bundle));
        Intent intent = new Intent("com.android.phone.roaming_mode_changed");
        intent.putExtra("roamingMode", this.mRoamingMode);
        intent.putExtra("phoneId", i);
        intent.putExtra("sim_operator_numeric", str);
        intent.putExtra("network_operator_numeric", str2);
        mContext.sendBroadcast(intent);
    }
}
