package com.android.internal.telephony;

import android.content.Context;
import com.android.internal.telephony.utils.XmPhoneUtils;
import com.android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public class XmPhoneFactory {
    private static String LOG_TAG = "XmPhoneFactory";
    private static boolean sInitialized = false;
    private static final int PHONE_COUNT = XmPhoneUtils.PHONE_COUNT;
    private static XmPhone[] mPhones = null;

    public static synchronized void init(Context context) {
        if (sInitialized) {
            return;
        }
        if (XmPhoneUtils.IS_DATA_CAPABLE) {
            Rlog.i(LOG_TAG, "init");
            makeXmPhones(context);
            XmPhoneService.init(context);
        } else {
            Rlog.i(LOG_TAG, "init, but not data capable");
        }
        sInitialized = true;
    }

    public static synchronized XmPhone getXmPhone(int phoneId) {
        XmPhone xmPhone;
        xmPhone = null;
        if (mPhones != null && phoneId >= 0 && phoneId < mPhones.length) {
            xmPhone = mPhones[phoneId];
        } else if (phoneId < 0 || phoneId >= mPhones.length) {
            Rlog.e(LOG_TAG, "Invalid PhoneId, Illegal operation");
        } else {
            Rlog.e(LOG_TAG, "XmPhones haven't been made yet!");
        }
        Rlog.d(LOG_TAG, "phoneId=" + phoneId + " xmPhone=" + xmPhone);
        return xmPhone;
    }

    public static synchronized XmPhone[] getXmPhones() {
        return mPhones;
    }

    private static synchronized void makeXmPhones(Context context) {
        mPhones = new XmPhone[PHONE_COUNT];
        for (int i = 0; i < PHONE_COUNT; i++) {
            mPhones[i] = new XmPhone(context, i);
        }
    }
}
