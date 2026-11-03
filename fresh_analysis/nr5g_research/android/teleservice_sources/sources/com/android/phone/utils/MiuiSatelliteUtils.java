package com.android.phone.utils;

import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.TelephonyCommonUtilsStub;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneGlobals;
import com.android.phone.module.ITelephonyModule;
import com.android.services.telephony.PstnIncomingCallNotifier;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public abstract class MiuiSatelliteUtils {
    public static String ALTITUDE = "altitude";
    public static String DESIRED_AZIMUTH = "desiredAzimuth";
    public static String DESIRED_ELEVATION = "desiredElevation";
    public static String IS_CALIBRATED = "isCalibrated";
    public static String REAL_AZIMUTH = "realAzimuth";
    public static String REAL_ELEVATION = "realElevation";
    private static final String TAG = "MiuiSatelliteUtils";
    private static PstnIncomingCallNotifier sPstnIncomingCallNotifier;

    public static void setupOnNtnBoot() {
        if (!TelephonyCommonUtilsStub.get().isSatelliteEnable(PhoneGlobals.getInstance()) && !TelephonyManagerEx.getDefault().isSatelliteBox()) {
            PstnIncomingCallNotifier pstnIncomingCallNotifier = sPstnIncomingCallNotifier;
            if (pstnIncomingCallNotifier != null) {
                pstnIncomingCallNotifier.teardown();
                sPstnIncomingCallNotifier = null;
                return;
            }
            return;
        }
        try {
            Phone ntnPhone = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getNtnPhone(PhoneGlobals.getInstance());
            if (ntnPhone != null) {
                sPstnIncomingCallNotifier = new PstnIncomingCallNotifier(ntnPhone);
            }
        } catch (Exception e) {
            Log.e(TAG, "setupOnNtnBoot: get ntnphone fail: " + e);
        }
    }
}
