package com.android.phone.utils;

import android.telephony.TelephonyManager;
import com.miui.misight.MiEvent;
import com.miui.misight.MiSight;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public abstract class MiEventUtils {
    public static MiEvent constructEvent(int i) {
        return new MiEvent(i);
    }

    public static void trackerMiEvent(int i, int i2) {
        MiEvent miEventConstructEvent = constructEvent(i);
        miEventConstructEvent.addStr("OperatorNumeric", getSimOperator(i2));
        miEventConstructEvent.addStr("PhoneId", String.valueOf(i2));
        sendEvent(miEventConstructEvent);
    }

    public static void sendEvent(MiEvent miEvent) {
        if (TelephonyManagerEx.getDefault().isMievnetSupported()) {
            MiSight.sendEvent(miEvent);
        }
    }

    public static String getSimOperator(int i) {
        return TelephonyManager.getDefault().getSimOperatorNumericForPhone(i);
    }
}
