package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.settings.BaseActivity;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import miui.telephony.SubscriptionInfo;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public class FiveGNrcaSettingActivity extends BaseActivity {
    public static final String TAG = "FiveGNrcaSettingActivity";

    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.d(TAG, "onCreate");
        if (bundle == null) {
            loadFragment(new FiveGNrcaSettingFragment());
        }
    }

    public class FiveGNrcaSettingFragment extends BaseActivity.BaseFragment {
        private Context mContext;
        private PreferenceScreen mPreferenceScreen;
        private PreferenceCategory[] mSwitchCategory = new PreferenceCategory[2];
        private NrcaSwitchView[] mNrcaSwitchView = new NrcaSwitchView[2];
        private boolean mAirplaneMode = false;
        private final BroadcastReceiver mNrcaReceiver = new BroadcastReceiver() { // from class: com.android.phone.FiveGNrcaSettingActivity.FiveGNrcaSettingFragment.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals("android.intent.action.AIRPLANE_MODE")) {
                    FiveGNrcaSettingFragment.this.mAirplaneMode = intent.getBooleanExtra(MEModuleSignalT1s.EXTRA_STATE, false);
                    Log.d(FiveGNrcaSettingActivity.TAG, "ACTION_AIRPLANE_MODE_CHANGED: " + String.valueOf(FiveGNrcaSettingFragment.this.mAirplaneMode));
                    FiveGNrcaSettingFragment.this.updatePreference();
                    return;
                }
                if (action.equals("android.telephony.action.CARRIER_CONFIG_CHANGED")) {
                    FiveGNrcaSettingFragment.this.updatePreference();
                    return;
                }
                if (action.equals("android.intent.action.SIM_STATE_CHANGED")) {
                    String stringExtra = intent.getStringExtra("ss");
                    Log.d(FiveGNrcaSettingActivity.TAG, "ACTION_SIM_STATE_CHANGED, SIM State:".concat(String.valueOf(stringExtra)));
                    if (stringExtra.equals("ABSENT")) {
                        FiveGNrcaSettingFragment.this.finish();
                    }
                }
            }
        };

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            this.mContext = getContext();
            addPreferencesFromResource(R.xml.fiveg_nrca_setting);
            displayPreference();
        }

        @Override // androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            Log.d(FiveGNrcaSettingActivity.TAG, "onResume");
            registerBroadcastReceiver();
            updatePreference();
        }

        @Override // androidx.fragment.app.Fragment
        public void onPause() {
            super.onPause();
            Log.d(FiveGNrcaSettingActivity.TAG, "onPause");
            this.mContext.unregisterReceiver(this.mNrcaReceiver);
        }

        public void displayPreference() {
            this.mPreferenceScreen = getPreferenceScreen();
            for (int i = 0; i < 2; i++) {
                PreferenceCategory preferenceCategory = (PreferenceCategory) this.mPreferenceScreen.findPreference("sim" + i + "_nrca_category_key");
                this.mPreferenceScreen.addPreference(preferenceCategory);
                SwitchPreference switchPreference = new SwitchPreference(this.mContext);
                switchPreference.setTitle(R.string.nrca_switch_title);
                switchPreference.setKey("nrca_switch" + i);
                preferenceCategory.addPreference(switchPreference);
                this.mSwitchCategory[i] = preferenceCategory;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updatePreference() {
            for (int i = 0; i < getPhoneCount(); i++) {
                boolean zHasIccCard = PhoneFactory.getPhone(i).getIccCard().hasIccCard();
                this.mSwitchCategory[i].setVisible(zHasIccCard);
                String str = FiveGNrcaSettingActivity.TAG;
                Log.d(str, "updatePreference: isSimInsert = " + zHasIccCard);
                if (zHasIccCard) {
                    SubscriptionInfo availableSubscriptionInfoBySlot = MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(i);
                    if (availableSubscriptionInfoBySlot != null) {
                        String string = getPhoneCount() == 1 ? availableSubscriptionInfoBySlot.getDisplayName().toString() : String.format(this.mContext.getString(R.string.sim_nrca_title), Integer.valueOf(i + 1), availableSubscriptionInfoBySlot.getDisplayName().toString());
                        this.mSwitchCategory[i].setTitle(string);
                        Log.d(str, "mSwitchCategoryTitle: " + string);
                    }
                    NrcaSwitchView[] nrcaSwitchViewArr = this.mNrcaSwitchView;
                    if (nrcaSwitchViewArr[i] == null) {
                        nrcaSwitchViewArr[i] = new NrcaSwitchView(this.mSwitchCategory[i], this.mContext, i);
                    }
                    this.mNrcaSwitchView[i].updateNrcaButtonUI();
                }
            }
        }

        private void registerBroadcastReceiver() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
            intentFilter.addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
            intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
            this.mContext.registerReceiver(this.mNrcaReceiver, intentFilter, 2);
            Log.d(FiveGNrcaSettingActivity.TAG, "register broadcastreceiver");
        }

        private int getPhoneCount() {
            return TelephonyManager.getDefault().getPhoneCount();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void finish() {
            if (getActivity() == null) {
                return;
            }
            if (isResumed()) {
                getActivity().onBackPressed();
            } else {
                getActivity().finish();
            }
        }
    }
}
