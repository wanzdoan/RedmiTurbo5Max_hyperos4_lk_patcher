package com.android.phone.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.preference.Preference;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneProxy;
import com.android.phone.R;
import miui.telephony.SubscriptionManager;

/* JADX INFO: loaded from: classes.dex */
public class CallForwardTypeActivity extends BaseActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            loadFragment(new CallForwardTypeFragment());
        }
    }

    public class CallForwardTypeFragment extends BaseActivity.BaseFragment {
        private Phone mPhone;
        private SubscriptionManager.OnSubscriptionsChangedListener mSubChangedListener = new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.phone.settings.CallForwardTypeActivity.CallForwardTypeFragment.1
            public void onSubscriptionsChanged() {
                if (MiuiPhoneUtils.shouldEnableVideoSuppService(CallForwardTypeFragment.this.mPhone) || CallForwardTypeFragment.this.getActivity() == null) {
                    return;
                }
                CallForwardTypeFragment.this.getActivity().finish();
            }
        };
        private Preference mVideoPreference;
        private Preference mVoicePreference;

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            addPreferencesFromResource(R.xml.call_forward_type);
            final int slotIdExtra = SubscriptionManager.getSlotIdExtra(getActivity().getIntent(), SubscriptionManager.getDefault().getDefaultSlotId());
            this.mPhone = MiuiPhoneUtils.getPhone(slotIdExtra);
            Preference preferenceFindPreference = findPreference("button_cf_key_voice");
            this.mVoicePreference = preferenceFindPreference;
            preferenceFindPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.phone.settings.CallForwardTypeActivity.CallForwardTypeFragment.2
                @Override // androidx.preference.Preference.OnPreferenceClickListener
                public boolean onPreferenceClick(Preference preference) {
                    CallForwardTypeFragment.this.startForwardActivity(1, slotIdExtra);
                    return true;
                }
            });
            Preference preferenceFindPreference2 = findPreference("button_cf_key_video");
            this.mVideoPreference = preferenceFindPreference2;
            preferenceFindPreference2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.phone.settings.CallForwardTypeActivity.CallForwardTypeFragment.3
                @Override // androidx.preference.Preference.OnPreferenceClickListener
                public boolean onPreferenceClick(Preference preference) {
                    CallForwardTypeFragment.this.startForwardActivity(PhoneProxy.getVideoCFServiceClass(), slotIdExtra);
                    return true;
                }
            });
            SubscriptionManager.getDefault().addOnSubscriptionsChangedListener(this.mSubChangedListener);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void startForwardActivity(int i, int i2) {
            Intent intent = new Intent(getContext(), (Class<?>) GsmUmtsCallForwardOptions.class);
            Log.d("CallForwardType", "serviceClass: " + i);
            intent.putExtra("service_class", i);
            SubscriptionManager.putSlotIdExtra(intent, i2);
            startActivity(intent);
        }

        @Override // androidx.fragment.app.Fragment
        public void onDestroy() {
            super.onDestroy();
            SubscriptionManager.getDefault().removeOnSubscriptionsChangedListener(this.mSubChangedListener);
        }
    }
}
