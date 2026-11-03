package com.android.phone.settings;

import android.content.Intent;
import android.os.Bundle;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import com.android.phone.utils.ScreenDisplayUtils;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.SubscriptionManager;

/* JADX INFO: loaded from: classes.dex */
public class CallWaitingActivity extends BaseActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!SimPickerPreference.showSimPicker(this) && bundle == null) {
            loadFragment(new CallWaitingActivityFragment());
        }
    }

    public class CallWaitingActivityFragment extends BaseActivity.BaseFragment {
        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            int slotIdExtra = SubscriptionManager.getSlotIdExtra(getActivity().getIntent(), SubscriptionManager.getDefault().getDefaultDataSlotId());
            if (CallWaitingActivity.useCdma(MiuiPhoneUtils.getPhone(slotIdExtra))) {
                addPreferencesFromResource(R.xml.call_waiting);
                ((CdmaCallWaitingPreference) findPreference("button_cdma_cw")).setSlotId(slotIdExtra);
            } else if (getActivity() != null) {
                Intent intent = new Intent().setClass(getContext(), GsmUmtsCallWaitingSetting.class);
                ScreenDisplayUtils.shouldForceSplit(intent, getActivity().getIntent().getMiuiFlags());
                SubscriptionManager.putSlotIdExtra(intent, slotIdExtra);
                startActivity(intent);
                getActivity().finish();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean useCdma(Phone phone) {
        if (phone == null) {
            return false;
        }
        if (HyperOSCustFeatureResolve.getBooleanForPhoneId(phone.getPhoneId(), "use_cdma_call_waiting", false)) {
            return true;
        }
        return phone.getPhoneType() == 2 && !MiuiPhoneUtils.isCmdaCwOperatedByUt(phone);
    }
}
