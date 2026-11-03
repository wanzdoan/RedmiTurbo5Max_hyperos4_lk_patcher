package com.android.phone.utils;

import android.text.TextUtils;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public abstract class MiuiJpOperatorCustimizedUtils {
    public static boolean isCustForJpKd() {
        return TelephonyManager.isCustForJpKd();
    }

    public static boolean isCustForJpSb() {
        return TelephonyManager.isCustForJpSb();
    }

    public static boolean isForJp() {
        return TelephonyManager.isForJp();
    }

    public static boolean isDuringCallForJpOperator() {
        Phone[] phones = PhoneFactory.getPhones();
        if (phones != null && phones.length != 0) {
            for (Phone phone : phones) {
                if (phone.getState() != PhoneConstants.State.IDLE) {
                    String operatorNumeric = phone.getOperatorNumeric();
                    if (!TextUtils.isEmpty(operatorNumeric) && operatorNumeric.length() >= 3) {
                        try {
                            if ("440".equals(operatorNumeric.substring(0, 3))) {
                                return true;
                            }
                        } catch (StringIndexOutOfBoundsException unused) {
                            continue;
                        }
                    }
                }
            }
        }
        return false;
    }
}
