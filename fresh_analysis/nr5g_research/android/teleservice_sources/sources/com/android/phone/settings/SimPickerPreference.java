package com.android.phone.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.preference.Preference;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import com.android.phone.utils.ScreenDisplayUtils;
import com.android.phone.utils.Utils;
import java.util.List;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.app.ActionBar;
import miuix.appcompat.app.AppCompatActivity;
import miuix.appcompat.app.floatingactivity.multiapp.MultiAppFloatingActivitySwitcher;

/* JADX INFO: loaded from: classes.dex */
public class SimPickerPreference extends BaseActivity {
    private static final int[] BIG_SIM_SLOT_ICON = {R.drawable.big_sim_icon_slot_1, R.drawable.big_sim_icon_slot_2};

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            loadFragment(new SimPickerPreferenceFragment());
        }
    }

    @Override // com.android.phone.settings.BaseActivity
    protected void setActionBar() {
        super.setActionBar();
        ActionBar appCompatActionBar = getAppCompatActionBar();
        String stringExtra = getIntent().getStringExtra("EXTRA_TITLE");
        if (TextUtils.isEmpty(stringExtra) || appCompatActionBar == null) {
            return;
        }
        appCompatActionBar.setTitle(stringExtra);
    }

    public static boolean showSimPicker(Activity activity) {
        if (MiuiPhoneUtils.hasDcOnlyEsim(activity)) {
            return false;
        }
        List subscriptionInfoList = SubscriptionManager.getDefault().getSubscriptionInfoList();
        if (MiuiPhoneUtils.isMultiSimEnabled() && subscriptionInfoList != null && (subscriptionInfoList.size() > 1 || MiuiPhoneUtils.isDcOnlyVirtualSim(activity))) {
            Intent intent = activity.getIntent();
            int i = SubscriptionManager.INVALID_SLOT_ID;
            if (SubscriptionManager.getSlotIdExtra(intent, i) == i) {
                Intent intent2 = new Intent(activity, (Class<?>) SimPickerPreference.class);
                intent2.putExtra("EXTRA_INTENT", activity.getIntent());
                intent2.putExtra("EXTRA_TITLE", activity.getTitle());
                ScreenDisplayUtils.shouldForceSplit(intent2, activity.getIntent().getMiuiFlags());
                MiuiPhoneUtils.addWindowLable(intent2, activity.getTitle());
                if (activity instanceof AppCompatActivity) {
                    MultiAppFloatingActivitySwitcher.configureFloatingService(intent2, (AppCompatActivity) activity);
                }
                activity.startActivity(intent2);
                activity.finish();
                return true;
            }
        }
        return false;
    }

    public class SimPickerPreferenceFragment extends BaseActivity.BaseFragment {
        private Intent mForwardIntent;
        private int mSimInfoSize;
        private SubscriptionManager.OnSubscriptionsChangedListener mSubChangedListener = new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.phone.settings.SimPickerPreference.SimPickerPreferenceFragment.1
            public void onSubscriptionsChanged() {
                SimPickerPreferenceFragment.this.updateScreen();
            }
        };

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            addPreferencesFromResource(R.xml.sim_picker);
            Intent intent = (Intent) getActivity().getIntent().getExtra("EXTRA_INTENT", null);
            this.mForwardIntent = intent;
            if (intent == null && getActivity() != null) {
                getActivity().finish();
                return;
            }
            List subscriptionInfoList = SubscriptionManager.getDefault().getSubscriptionInfoList();
            this.mSimInfoSize = subscriptionInfoList != null ? subscriptionInfoList.size() : 0;
            updateScreen();
        }

        @Override // androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            SubscriptionManager.getDefault().addOnSubscriptionsChangedListener(this.mSubChangedListener);
            updateScreen();
        }

        @Override // androidx.fragment.app.Fragment
        public void onPause() {
            super.onPause();
            SubscriptionManager.getDefault().removeOnSubscriptionsChangedListener(this.mSubChangedListener);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateScreen() {
            getPreferenceScreen().removeAll();
            List<SubscriptionInfo> subscriptionInfoList = SubscriptionManager.getDefault().getSubscriptionInfoList();
            if ((subscriptionInfoList == null || subscriptionInfoList.isEmpty() || this.mSimInfoSize != subscriptionInfoList.size()) && getActivity() != null) {
                getActivity().finish();
                return;
            }
            if (MiuiPhoneUtils.isDcOnlyVirtualSim(getContext())) {
                int virtualSimSlotId = MiuiSettings.VirtualSim.getVirtualSimSlotId(getContext());
                int size = subscriptionInfoList.size();
                if (size != 1 && size == 2) {
                    SubscriptionManager.putSlotIdExtra(this.mForwardIntent, virtualSimSlotId == 0 ? 1 : 0);
                    this.mForwardIntent.setFlags(131072);
                    startActivity(this.mForwardIntent);
                }
                if (getActivity() != null) {
                    getActivity().finish();
                    return;
                }
                return;
            }
            if (subscriptionInfoList.size() == 1 && getActivity() != null) {
                SubscriptionManager.putSlotIdExtra(this.mForwardIntent, ((SubscriptionInfo) subscriptionInfoList.get(0)).getSlotId());
                startActivity(this.mForwardIntent);
                getActivity().finish();
                return;
            }
            boolean z = Settings.System.getInt(getContext().getContentResolver(), "airplane_mode_on", 0) > 0;
            String className = this.mForwardIntent.getComponent().getClassName();
            for (SubscriptionInfo subscriptionInfo : subscriptionInfoList) {
                Preference preferenceCreateSimInfoPreference = createSimInfoPreference(subscriptionInfo);
                Phone phone = MiuiPhoneUtils.getPhone(subscriptionInfo.getSlotId());
                boolean z2 = isSSActivity(className) && MiuiPhoneUtils.disableSSOverWfcEnableAirplane(phone);
                if (z && phone != null && (!phone.isWifiCallingEnabled() || z2)) {
                    preferenceCreateSimInfoPreference.setEnabled(false);
                }
                getPreferenceScreen().addPreference(preferenceCreateSimInfoPreference);
                if (!CarrierConfigManagerEx.getBooleanConfigForSubId(getContext(), subscriptionInfo.getSubscriptionId(), "call_forwarding_visibility_bool", true) && "com.android.phone.settings.CallForwardActivity".equals(className)) {
                    getPreferenceScreen().removePreference(preferenceCreateSimInfoPreference);
                }
                if (!CarrierConfigManagerEx.getBooleanConfigForSubId(getContext(), subscriptionInfo.getSubscriptionId(), "call_barring_visibility_bool", true) && "com.android.phone.settings.CallBarring".equals(className)) {
                    getPreferenceScreen().removePreference(preferenceCreateSimInfoPreference);
                }
                if (!CarrierConfigManagerEx.getBooleanConfigForSubId(getContext(), subscriptionInfo.getSubscriptionId(), "additional_settings_call_waiting_visibility_bool", true) && "com.android.phone.settings.CallWaitingActivity".equals(className)) {
                    getPreferenceScreen().removePreference(preferenceCreateSimInfoPreference);
                }
            }
        }

        private boolean isSSActivity(String str) {
            return "com.android.phone.settings.CallForwardActivity".equals(str) || "com.android.phone.settings.GsmUmtsCallForwardOptions".equals(str) || "com.android.phone.settings.CallWaitingActivity".equals(str) || "com.android.phone.settings.GsmUmtsCallWaitingSetting".equals(str) || "com.android.phone.settings.CallBarring".equals(str) || "com.android.phone.settings.GsmUmtsCallerIdSetting".equals(str);
        }

        private Preference createSimInfoPreference(SubscriptionInfo subscriptionInfo) {
            LTRValuePreference lTRValuePreference = new LTRValuePreference(getPreferenceManager().getContext());
            lTRValuePreference.setTitle(MiuiPhoneUtils.getDisplayName(subscriptionInfo));
            lTRValuePreference.setSummary(Utils.localizeNumber(subscriptionInfo.getDisplayNumber()));
            lTRValuePreference.setEnabled(MiuiPhoneUtils.isIccCardActivated(subscriptionInfo.getSlotId()));
            lTRValuePreference.setIcon(MiuiPhoneUtils.getSimIconResId(subscriptionInfo.getSlotId(), true));
            lTRValuePreference.setShowRightArrow(true);
            Intent intent = new Intent(this.mForwardIntent);
            intent.setFlags(131072);
            SubscriptionManager.putSlotIdExtra(intent, subscriptionInfo.getSlotId());
            lTRValuePreference.setIntent(intent);
            return lTRValuePreference;
        }
    }
}
