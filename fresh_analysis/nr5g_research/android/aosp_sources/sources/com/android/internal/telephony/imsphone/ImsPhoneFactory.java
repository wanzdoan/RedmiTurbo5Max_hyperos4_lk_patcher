package com.android.internal.telephony.imsphone;

import android.content.Context;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneNotifier;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public class ImsPhoneFactory {
    public static ImsPhone makePhone(Context context, PhoneNotifier phoneNotifier, Phone phone, FeatureFlags featureFlags) {
        try {
            return TelephonyComponentFactory.getInstance().inject(ImsPhone.class.getName()).makeImsPhone(context, phoneNotifier, phone, featureFlags);
        } catch (Exception e) {
            Rlog.e("VoltePhoneFactory", "makePhone", e);
            return null;
        }
    }
}
