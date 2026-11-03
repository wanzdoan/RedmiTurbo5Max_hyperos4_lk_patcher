package com.android.internal.telephony.util;

import android.content.Context;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.TelephonyCommonUtilsStub;
import com.miui.misight.MiEventStub;

/* JADX INFO: loaded from: classes.dex */
public class MiEventUtils {
    public static final String EVENT_REASON = "EventReason";
    public static final String PARAM_OPERATOR_NUMERIC = "OperatorNumeric";
    public static final String PARAM_PHONE_ID = "PhoneId";
    public static final int SMS_EXCEPTION_EVENT_CODE = 915700001;

    public static MiEventStub constructEvent(int i) {
        MiEventStub miEventStubNewInstance = MiEventStub.newInstance();
        miEventStubNewInstance.init(i);
        return miEventStubNewInstance;
    }

    public static void trackerMiEvent(int i, int i2) {
        MiEventStub miEventStubConstructEvent = constructEvent(i);
        miEventStubConstructEvent.addStr(PARAM_OPERATOR_NUMERIC, getSimOperator(i2));
        miEventStubConstructEvent.addStr(PARAM_PHONE_ID, String.valueOf(i2));
        sendEvent(miEventStubConstructEvent);
    }

    public static void sendEvent(MiEventStub miEventStub) {
        TelephonyCommonUtilsStub.get().sendEvent(miEventStub);
    }

    public static String getSimOperator(int i) {
        return TelephonyManager.getDefault().getSimOperatorNumericForPhone(i);
    }

    public static int getPhoneId(int i, Context context) {
        SubscriptionManager.from(context);
        return SubscriptionManager.getPhoneId(i);
    }
}
