package com.android.internal.telephony.utils;

import android.content.Context;
import android.os.SystemProperties;
import android.telephony.Rlog;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.TelephonyCommonUtilsStub;
import com.android.internal.telephony.ntnphone.NtnPhone;
import com.android.internal.telephony.ntnphone.NtnPhoneConnection;
import com.android.internal.telephony.ntnphone.NtnPhoneFactory;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyCommonUtils {
    public static final int NTN_CHIP_VENDOR_54 = 1;
    public static final int NTN_CHIP_VENDOR_SYLINCOM = 0;
    public static int NTN_EPHEM_HIGH_RAIL = 0;
    public static int NTN_EPHEM_LOW_RAIL = 1;
    public static final int NTN_XINGLI_TIANTONG = 0;
    public static final int NTN_XINGLI_XINGWANG = 1;
    private static int mXingli;

    public static int getNtnChipVendor() {
        return SystemProperties.getInt("ro.vendor.radio.ntn_chip_type", 0);
    }

    public static int getNtnEphemType() {
        int i = SystemProperties.getInt("persist.radio.ntn_stack_on", -1);
        if (i == -1) {
            Rlog.d("TelephonyCommonUtils", "getNtnEphemType ntn ephem type is not set");
            return -1;
        }
        int i2 = (i & 28) >>> 2;
        Rlog.d("TelephonyCommonUtils", "ntn ephem type: " + i2);
        return i2;
    }

    public static boolean isNtnChipVendor_54() {
        boolean z = getNtnChipVendor() == 1;
        Rlog.d("TelephonyCommonUtils", "is Ntn Chip 54-Vendor : " + z);
        return z;
    }

    public static int caculateNtnStackPropertyValue(int i, boolean z, int i2, int i3) {
        mXingli = i3;
        if (!validateParameters(i, z, i2, i3)) {
            Rlog.d("TelephonyCommonUtils", "caculateNtnStackPropertyValue some args is invalid");
            return -1;
        }
        if (i2 == 0 && i3 == 0) {
            return z ? 1 : 0;
        }
        int i4 = (i2 << 5) | (i3 << 2) | (i << 1) | (z ? 1 : 0);
        Rlog.d("TelephonyCommonUtils", "caculateNtnStackPropertyValue phoneId : " + i + ", enable : " + z + ", vendor : " + i2 + ", xingli : " + i3 + ", prop val : " + (i4 == true ? 1 : 0));
        return i4 == true ? 1 : 0;
    }

    private static boolean validateParameters(int i, boolean z, int i2, int i3) {
        if (i < 0 || i > 1) {
            System.err.println("Invalid slot ID: " + i + ", must be 0 or 1");
            return false;
        }
        if (i2 < 0) {
            System.err.println("Invalid vendor code: " + i2 + ", must be 0(ZKJS), 1(54Suo), 2(Jingshang702) or 3(Xingsi)");
            return false;
        }
        if (i3 >= 0) {
            return true;
        }
        System.err.println("Invalid ephemeris type: " + i3 + ", must be 0(Tiantong/High Orbit), 1(Xingwangyi) or 2(Beidou)");
        return false;
    }

    public static void resetNtnStackPropertyValue() {
        int i = SystemProperties.getInt("persist.radio.ntn_stack_on", -1);
        if (i == -1 || (i & 1) == 0) {
            return;
        }
        SystemProperties.set("persist.radio.ntn_stack_on", Integer.toString(i & (-2)));
    }

    public static void dualAntennaSwitch(Context context, int i) {
        try {
            if (!TelephonyCommonUtilsStub.get().isSatelliteEnable(context)) {
                Rlog.d("TelephonyCommonUtils", "setRFMode failed, satellite state is false");
                return;
            }
            NtnPhone ntnPhone = (NtnPhone) NtnPhoneFactory.getDefaultPhone();
            if (ntnPhone != null) {
                int i2 = 1;
                if (i != 1) {
                    i2 = 0;
                }
                ntnPhone.setRFMode(i2, null);
                Rlog.d("TelephonyCommonUtils", "onCallAudioStateChanged antenna mode modify turn on");
            }
        } catch (Exception e) {
            Rlog.e("TelephonyCommonUtils", "setRFMode failed, exception: " + e);
        }
    }

    public static String getNtnVendorInfo() {
        return getNtnChipVendor() + " : " + mXingli;
    }

    public static long getCallSetupDuration(Connection connection) {
        if (connection instanceof NtnPhoneConnection) {
            return ((NtnPhoneConnection) connection).getCallSetupDuration();
        }
        return 0L;
    }

    public static void querySpeechBitrate() {
        try {
            NtnPhone ntnPhone = (NtnPhone) NtnPhoneFactory.getDefaultPhone();
            if (ntnPhone == null) {
                return;
            }
            ntnPhone.querySpeechBitrate();
        } catch (Exception e) {
            Rlog.e("TelephonyCommonUtils", "querySpeechBitrate failed, exception: " + e);
        }
    }
}
