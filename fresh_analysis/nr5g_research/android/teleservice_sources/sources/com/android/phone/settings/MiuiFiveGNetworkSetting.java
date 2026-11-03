package com.android.phone.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;

/* JADX INFO: loaded from: classes.dex */
public class MiuiFiveGNetworkSetting extends BaseActivity {
    public static final String TAG = "MiuiFiveGNetworkSetting";

    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            loadFragment(new MiuiFiveGNetworkSettingFragment());
        }
    }

    public class MiuiFiveGNetworkSettingFragment extends BaseActivity.BaseFragment implements SubscriptionManager.OnSubscriptionsChangedListener {
        private PreferenceScreen mPreferenceScreen;
        private PreferenceCategory[] mFiveGSwitchCategory = new PreferenceCategory[2];
        private FiveGSwitchView[] mFiveGSwitchView = new FiveGSwitchView[MiuiPhoneUtils.PHONE_COUNT];
        final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.settings.MiuiFiveGNetworkSetting.MiuiFiveGNetworkSettingFragment.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                    Log.d(MiuiFiveGNetworkSetting.TAG, "ACTION_AIRPLANE_MODE_CHANGED");
                    MiuiFiveGNetworkSettingFragment.this.updatePreferenceScreen();
                } else if ("android.telephony.action.CARRIER_CONFIG_CHANGED".equals(action)) {
                    int intExtra = intent.getIntExtra("phone", SubscriptionManager.INVALID_PHONE_ID);
                    Log.d(MiuiFiveGNetworkSetting.TAG, "ACTION_CARRIER_CONFIG_CHANGED phoneId: " + intExtra);
                    if (SubscriptionManager.isValidPhoneId(intExtra)) {
                        MiuiFiveGNetworkSettingFragment.this.updatePreferenceScreen();
                    }
                }
            }
        };

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            addPreferencesFromResource(R.xml.fiveg_network_setting);
            initUI();
            initReceiver();
        }

        @Override // androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            SubscriptionManager.getDefault().addOnSubscriptionsChangedListener(this);
            updatePreferenceScreen();
        }

        public void onSubscriptionsChanged() {
            Log.d(MiuiFiveGNetworkSetting.TAG, "Subscriptions Changed");
            updatePreferenceScreen();
        }

        private void initUI() {
            this.mPreferenceScreen = getPreferenceScreen();
            for (int i = 0; i < 2; i++) {
                this.mFiveGSwitchCategory[i] = (PreferenceCategory) this.mPreferenceScreen.findPreference("sim" + i + "_fiveg_network_category_key");
                if (i == MiuiPhoneUtils.PHONE_COUNT) {
                    Log.d(MiuiFiveGNetworkSetting.TAG, "single sim device");
                    this.mPreferenceScreen.removePreference(this.mFiveGSwitchCategory[i]);
                    this.mFiveGSwitchCategory[i] = null;
                }
            }
        }

        private void updatePreference(int i) {
            if (this.mPreferenceScreen == null) {
                return;
            }
            boolean zHasIccCard = PhoneFactory.getPhone(i).getIccCard().hasIccCard();
            boolean zShowFiveGSwitch = MiuiPhoneUtils.showFiveGSwitch(i);
            Log.d(MiuiFiveGNetworkSetting.TAG, "update Preference isSimInsert :" + zHasIccCard + ", isShowFiveGSwitch :" + zShowFiveGSwitch);
            PreferenceCategory preferenceCategory = this.mFiveGSwitchCategory[i];
            if (preferenceCategory == null || !zHasIccCard || !zShowFiveGSwitch) {
                this.mPreferenceScreen.removePreference(preferenceCategory);
                return;
            }
            SubscriptionInfo availableSubscriptionInfoBySlot = MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(i);
            if (availableSubscriptionInfoBySlot == null) {
                return;
            }
            this.mFiveGSwitchCategory[i].setTitle(MiuiPhoneUtils.PHONE_COUNT == 1 ? availableSubscriptionInfoBySlot.getDisplayName().toString() : String.format(getString(R.string.sim_fiveg_network_title), Integer.valueOf(i + 1), availableSubscriptionInfoBySlot.getDisplayName().toString()));
            this.mPreferenceScreen.addPreference(this.mFiveGSwitchCategory[i]);
            FiveGSwitchView[] fiveGSwitchViewArr = this.mFiveGSwitchView;
            if (fiveGSwitchViewArr[i] == null) {
                fiveGSwitchViewArr[i] = new FiveGSwitchView(this, this.mFiveGSwitchCategory[i], MiuiPhoneUtils.getPhone(i));
            }
            this.mFiveGSwitchView[i].updateSwitchButton();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updatePreferenceScreen() {
            boolean z = false;
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                boolean z2 = true;
                z = z || PhoneFactory.getPhone(i).getIccCard().hasIccCard();
                boolean zIsIccCardActivated = MiuiPhoneUtils.isIccCardActivated(i);
                boolean z3 = Settings.Global.getInt(getContext().getContentResolver(), "airplane_mode_on", 0) == 1;
                if (zIsIccCardActivated && !z3) {
                    z2 = false;
                }
                Log.d(MiuiFiveGNetworkSetting.TAG, "updatePreferenceScreen mFiveGSwitchView[" + i + "] = " + this.mFiveGSwitchView[i] + "enableOptions = " + z2);
                PreferenceCategory preferenceCategory = this.mFiveGSwitchCategory[i];
                if (preferenceCategory != null) {
                    preferenceCategory.setEnabled(!z2);
                    updatePreference(i);
                }
            }
            if (z || getActivity() == null) {
                return;
            }
            getActivity().finish();
        }

        private void initReceiver() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
            intentFilter.addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
            getContext().registerReceiver(this.mReceiver, intentFilter, 2);
        }

        @Override // androidx.fragment.app.Fragment
        public void onPause() {
            super.onPause();
            SubscriptionManager.getDefault().removeOnSubscriptionsChangedListener(this);
        }

        @Override // androidx.fragment.app.Fragment
        public void onDestroy() {
            getContext().unregisterReceiver(this.mReceiver);
            super.onDestroy();
        }
    }
}
