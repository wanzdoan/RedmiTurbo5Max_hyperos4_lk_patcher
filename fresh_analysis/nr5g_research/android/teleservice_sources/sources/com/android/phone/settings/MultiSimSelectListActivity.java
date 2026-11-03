package com.android.phone.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import com.android.phone.settings.sim.Utils$VirtualSim;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.ArrayList;
import java.util.List;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;

/* JADX INFO: loaded from: classes.dex */
public class MultiSimSelectListActivity extends BaseActivity {
    private static final String[] BUTTON_CARRIER_INFO_KEYS = {"carrier_info_for_card_one", "carrier_info_for_card_two"};
    public static final String TAG = "MultiSimSelectListActivity";

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            loadFragment(new MultiSimSelectListFragment());
        }
    }

    public class MultiSimSelectListFragment extends BaseActivity.BaseFragment implements SubscriptionManager.OnSubscriptionsChangedListener {
        private IntentFilter mIntentFilter;
        private PreferenceScreen mPreferenceScreen;
        private List mListForCards = new ArrayList();
        private List mSimInfoRecordList = new ArrayList();
        private boolean mIsAirplaneEnabled = false;
        private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.settings.MultiSimSelectListActivity.MultiSimSelectListFragment.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                    Log.d(MultiSimSelectListActivity.TAG, "ACTION_AIRPLANE_MODE_CHANGED");
                    MultiSimSelectListFragment.this.mIsAirplaneEnabled = intent.getBooleanExtra(MEModuleSignalT1s.EXTRA_STATE, false);
                    MultiSimSelectListFragment.this.mPreferenceScreen.setEnabled(!MultiSimSelectListFragment.this.mIsAirplaneEnabled);
                    return;
                }
                if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                    Log.d(MultiSimSelectListActivity.TAG, "ACTION_SIM_STATE_CHANGED");
                    if ("ABSENT".equals(intent.getStringExtra("ss"))) {
                        MultiSimSelectListFragment.this.getActivity().finish();
                    }
                }
            }
        };

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, miuix.preference.PreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.preference.PreferenceManager.OnPreferenceTreeClickListener
        public boolean onPreferenceTreeClick(Preference preference) {
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                if (preference == this.mListForCards.get(i)) {
                    Utils$VirtualSim.enterBusinessHall(getContext(), null, i);
                    return true;
                }
            }
            return false;
        }

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            addPreferencesFromResource(R.xml.msim_select_list);
            this.mPreferenceScreen = getPreferenceScreen();
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                this.mListForCards.add((PreferenceScreen) this.mPreferenceScreen.findPreference(MultiSimSelectListActivity.BUTTON_CARRIER_INFO_KEYS[i]));
            }
            getSimInfo();
            updatePreferenceUI();
            IntentFilter intentFilter = new IntentFilter("android.intent.action.AIRPLANE_MODE");
            this.mIntentFilter = intentFilter;
            intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
            getContext().registerReceiver(this.mReceiver, this.mIntentFilter, 2);
        }

        private void getSimInfo() {
            this.mSimInfoRecordList = SubscriptionManager.getDefault().getAvailableSubscriptionInfoList();
        }

        private void updatePreferenceUI() {
            if (this.mPreferenceScreen == null) {
                return;
            }
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                PreferenceScreen preferenceScreen = (PreferenceScreen) this.mListForCards.get(i);
                if (preferenceScreen != null) {
                    int size = this.mSimInfoRecordList.size();
                    PreferenceScreen preferenceScreen2 = this.mPreferenceScreen;
                    if (i < size) {
                        if (preferenceScreen2.findPreference(MultiSimSelectListActivity.BUTTON_CARRIER_INFO_KEYS[i]) == null) {
                            this.mPreferenceScreen.addPreference(preferenceScreen);
                        }
                        preferenceScreen.setTitle(((SubscriptionInfo) this.mSimInfoRecordList.get(i)).getDisplayName());
                        preferenceScreen.setSummary(((SubscriptionInfo) this.mSimInfoRecordList.get(i)).getDisplayNumber());
                    } else {
                        preferenceScreen2.removePreference(preferenceScreen);
                    }
                }
            }
        }

        @Override // androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            boolean z = Settings.System.getInt(getContext().getContentResolver(), "airplane_mode_on", -1) == 1;
            this.mIsAirplaneEnabled = z;
            this.mPreferenceScreen.setEnabled(!z);
            SubscriptionManager.getDefault().addOnSubscriptionsChangedListener(this);
            getSimInfo();
            updatePreferenceUI();
        }

        public void onSubscriptionsChanged() {
            getSimInfo();
            updatePreferenceUI();
        }

        @Override // androidx.fragment.app.Fragment
        public void onPause() {
            super.onPause();
            SubscriptionManager.getDefault().removeOnSubscriptionsChangedListener(this);
        }

        @Override // androidx.fragment.app.Fragment
        public void onDestroy() {
            super.onDestroy();
            getContext().unregisterReceiver(this.mReceiver);
        }
    }
}
