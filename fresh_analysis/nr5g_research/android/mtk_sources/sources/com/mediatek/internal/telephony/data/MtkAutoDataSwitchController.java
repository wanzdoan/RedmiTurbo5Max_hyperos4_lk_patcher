package com.mediatek.internal.telephony.data;

import android.content.Context;
import android.os.Looper;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.data.AutoDataSwitchController;
import com.android.internal.telephony.data.PhoneSwitcher;

/* JADX INFO: loaded from: classes.dex */
public class MtkAutoDataSwitchController extends AutoDataSwitchController {
    private static final String LOG_TAG = "MtkADSC";

    public MtkAutoDataSwitchController(Context context, Looper looper, PhoneSwitcher phoneSwitcher, FeatureFlags featureFlags, AutoDataSwitchController.AutoDataSwitchControllerCallback phoneSwitcherCallback) {
        super(context, looper, phoneSwitcher, featureFlags, phoneSwitcherCallback);
    }

    protected boolean isUserDataSettingEnabledOnNonDDS(Phone phone) {
        return phone != null && phone.isUserDataEnabled();
    }

    protected boolean isAutoSwitchEnabledByUser(Phone phone) {
        return MtkDataHelper.getInstance().isAutoSwitchEnabledByUser(phone);
    }

    protected boolean ignoreCheckingAutoSwitchBack() {
        int phoneIdInVoiceCall = ((MtkPhoneSwitcher) this.mPhoneSwitcher).getPhoneIdInVoiceCall();
        if (phoneIdInVoiceCall != -1 && this.mPhoneSwitcher.getPreferredDataPhoneId() == phoneIdInVoiceCall) {
            cancelAnyPendingSwitch();
            return true;
        }
        return false;
    }
}
