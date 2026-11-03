package com.android.phone.settings;

import android.content.Intent;
import android.os.Bundle;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.utils.ScreenDisplayUtils;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.app.AppCompatActivity;

/* JADX INFO: loaded from: classes.dex */
public class CallForwardActivity extends AppCompatActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (SimPickerPreference.showSimPicker(this)) {
            return;
        }
        int slotIdExtra = SubscriptionManager.getSlotIdExtra(getIntent(), SubscriptionManager.getDefault().getDefaultDataSlotId());
        Phone phone = MiuiPhoneUtils.getPhone(slotIdExtra);
        int phoneType = phone.getPhoneType();
        Intent intent = new Intent();
        if (phoneType == 2) {
            if (MiuiPhoneUtils.useUtForCdmaCallForward(phone)) {
                intent.setClass(this, GsmUmtsCallForwardOptions.class);
            } else {
                intent.setClass(this, CdmaCallForwardOptions.class);
            }
        } else if (MiuiPhoneUtils.shouldEnableVideoSuppService(phone)) {
            intent.setClass(this, CallForwardTypeActivity.class);
        } else {
            intent.setClass(this, GsmUmtsCallForwardOptions.class);
        }
        if (HyperOSCustFeatureResolve.getBooleanForPhoneId(slotIdExtra, "use_cdma_call_forward", false)) {
            intent.setClass(this, CdmaCallForwardOptions.class);
        }
        ScreenDisplayUtils.shouldForceSplit(intent, getIntent().getMiuiFlags());
        SubscriptionManager.putSlotIdExtra(intent, slotIdExtra);
        startActivity(intent);
        finish();
    }
}
