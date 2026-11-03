package com.android.phone;

import android.content.Context;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.qti.dpmapi.DpmApi;
import com.qti.dpmapi.DpmApiException;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public class FastDormancy {
    private static String TAG = "FastDormancy";
    protected static volatile FastDormancy mInstance;

    public static FastDormancy make() {
        if (mInstance == null) {
            synchronized (FastDormancy.class) {
                try {
                    if (mInstance == null) {
                        mInstance = new FastDormancy();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return mInstance;
    }

    public void setFDConfigForPhoneId(Context context, int i) {
        String operatorNumeric;
        if (ModemProvider.IS_QCOM) {
            if (!TelephonyManager.isCustForHkH3g() || i != DefaultSimManager.getDefaultDataSlotId()) {
                Log.d(TAG, "setFDConfigForPhoneId, not H3G build or phone id is not dds id, return");
                return;
            }
            Phone phone = MiuiPhoneUtils.getPhone(i);
            if (phone == null) {
                operatorNumeric = "";
            } else {
                operatorNumeric = phone.getOperatorNumeric();
            }
            Log.i(TAG, "setFDConfigForPhoneId, simOperator is" + operatorNumeric);
            if (TextUtils.isEmpty(operatorNumeric)) {
                Log.d(TAG, "setFDConfigForPhoneId, simOperator is null, return");
                return;
            }
            try {
                new DpmApi(context).updateFdConfigParams(0, getCarrierConfigIntParam(context, phone.getSubId(), "fast_dormancy_screen_on_minutes_int", 15), getCarrierConfigIntParam(context, phone.getSubId(), "fast_dormancy_screen_off_minutes_int", 3), getCarrierConfigIntParam(context, phone.getSubId(), "fast_dormancy_tethering_minutes_int", 15));
            } catch (DpmApiException e) {
                Log.d(TAG, "setFDConfigForPhoneId, fail to set fast dormancy:" + e);
            } catch (Exception e2) {
                Log.d(TAG, "setFDConfigForPhoneId, fail to create dpmapi: " + e2);
            }
        }
    }

    private static int getCarrierConfigIntParam(Context context, int i, String str, int i2) {
        PersistableBundle configForSubId;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) context.getSystemService("carrier_config");
        return (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(i)) == null) ? i2 : configForSubId.getInt(str, i2);
    }
}
