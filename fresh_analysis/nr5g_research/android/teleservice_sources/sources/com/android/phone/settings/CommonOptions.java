package com.android.phone.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.telephony.satellite.SatelliteManager;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.utils.ScreenDisplayUtils;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;
import miuix.preference.PreferenceFragment;

/* JADX INFO: loaded from: classes.dex */
public abstract class CommonOptions {
    private static final String LOG_TAG = "CommonOptions";
    protected Preference mButtonApnPref;
    protected Preference mButtonCdmaSystemSelect;
    protected Preference mButtonOperatorSelection;
    protected Preference mButtonPreferredNetworkType;
    protected Preference mButtonSatelliteSettingKeyAosp;
    protected Enable2GSwitchView mEnable2GSwitchView;
    protected FiveGSwitchTrView mFiveGSwitchTrView;
    protected FourPointFiveSwitchView mFourPointFiveSwitchView;
    protected PreferenceCategory mOptionsCategory;
    protected Phone mPhone;
    protected PreferenceScreen mPrefScreen;
    protected PreferenceFragment mPreferenceFragment;
    private SatelliteManager mSatelliteManager;
    private final BroadcastReceiver mSimStateReceiver = new BroadcastReceiver() { // from class: com.android.phone.settings.CommonOptions.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if (SubscriptionManager.getSlotIdExtra(intent, SubscriptionManager.INVALID_SLOT_ID) != CommonOptions.this.mSlotId) {
                    Log.w(CommonOptions.LOG_TAG, "mSimStateReceiver: slotId from intent is invalid, not equals mSlotId.");
                    return;
                }
                if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                    String stringExtra = intent.getStringExtra("ss");
                    if ("ABSENT".equals(stringExtra)) {
                        CommonOptions.this.setOptionEnabled(false);
                    } else if ("LOADED".equals(stringExtra) && CommonOptions.this.isIccCardActivate()) {
                        CommonOptions.this.enableScreen();
                    }
                }
            }
        }
    };
    protected int mSlotId;
    protected VolteSwitchView mVolteSwitchView;
    protected VonrSwitchView mVonrSwitchView;

    public void enableScreen() {
    }

    public CommonOptions(PreferenceFragment preferenceFragment, PreferenceScreen preferenceScreen, Phone phone) {
        this.mSlotId = 0;
        this.mPreferenceFragment = preferenceFragment;
        this.mPrefScreen = preferenceScreen;
        this.mPhone = phone;
        this.mSlotId = phone.getPhoneId();
    }

    public CommonOptions(PreferenceFragment preferenceFragment, PreferenceScreen preferenceScreen, PreferenceCategory preferenceCategory, Phone phone) {
        this.mSlotId = 0;
        this.mPreferenceFragment = preferenceFragment;
        this.mPrefScreen = preferenceScreen;
        this.mOptionsCategory = preferenceCategory;
        this.mPhone = phone;
        this.mSlotId = phone.getPhoneId();
    }

    protected void create() {
        boolean zIsTrVersion;
        if (this.mOptionsCategory == null) {
            this.mOptionsCategory = (PreferenceCategory) this.mPrefScreen.findPreference("options_category");
        }
        Preference preferenceFindPreference = this.mPrefScreen.findPreference("button_apn_key");
        this.mButtonApnPref = preferenceFindPreference;
        SubscriptionManager.putSlotIdExtra(preferenceFindPreference.getIntent(), this.mSlotId);
        Log.d(LOG_TAG, "create");
        if (MiuiPhoneUtils.DUAL_VOLTE_SUPPORTED && !MiuiPhoneUtils.isTabletDeviceWithVolteCapability()) {
            this.mPreferenceFragment.addPreferencesFromResource(R.xml.volte_switch_per_slot);
            this.mVolteSwitchView = new VolteSwitchView(this.mPreferenceFragment, this.mPrefScreen, this.mPhone);
        }
        if (TelephonyManager.isCustFeatureResolveEnabled()) {
            zIsTrVersion = HyperOSCustFeatureResolve.getBoolean("support_cust_four_point_fiveg_switch", false);
        } else {
            zIsTrVersion = MiuiPhoneUtils.isTrVersion();
        }
        if (zIsTrVersion) {
            if (MiuiPhoneUtils.showFiveGNetworkMode(this.mSlotId)) {
                if (!HyperOSCustFeatureResolve.getBooleanForPhoneId(this.mPhone.getPhoneId(), "config_tr_hide_5g_switch", false)) {
                    this.mPreferenceFragment.addPreferencesFromResource(R.xml.five_switch_per_slot);
                    this.mFiveGSwitchTrView = new FiveGSwitchTrView(this.mPreferenceFragment, this.mPrefScreen, this.mPhone);
                }
            } else {
                this.mPreferenceFragment.addPreferencesFromResource(R.xml.four_point_five_switch_per_slot);
                this.mFourPointFiveSwitchView = new FourPointFiveSwitchView(this.mPreferenceFragment, this.mPrefScreen, this.mPhone, this.mVolteSwitchView);
            }
        }
        this.mPreferenceFragment.addPreferencesFromResource(R.xml.vonr_switch_per_slot);
        this.mVonrSwitchView = new VonrSwitchView(this.mPreferenceFragment, this.mPrefScreen, this.mPhone);
        Preference preferenceFindPreference2 = this.mPrefScreen.findPreference("button_preferred_network_type_key");
        this.mButtonPreferredNetworkType = preferenceFindPreference2;
        if (preferenceFindPreference2 != null) {
            if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isRemoveNetworkModeSettings(this.mPhone)) {
                this.mOptionsCategory.removePreference(this.mButtonPreferredNetworkType);
                this.mButtonPreferredNetworkType = null;
            } else {
                SubscriptionManager.putSlotIdExtra(this.mButtonPreferredNetworkType.getIntent(), this.mSlotId);
            }
        }
        this.mEnable2GSwitchView = new Enable2GSwitchView(this.mPreferenceFragment, this.mPrefScreen, this.mOptionsCategory, this.mPhone);
        this.mPreferenceFragment.getContext().registerReceiver(this.mSimStateReceiver, new IntentFilter("android.intent.action.SIM_STATE_CHANGED"), 2);
        int phoneType = this.mPhone.getPhoneType();
        if (phoneType == 2) {
            Preference preferenceFindPreference3 = this.mPrefScreen.findPreference("button_carrier_sel_key");
            this.mButtonOperatorSelection = preferenceFindPreference3;
            if (preferenceFindPreference3 != null) {
                this.mOptionsCategory.removePreference(preferenceFindPreference3);
            }
        } else if (phoneType == 1) {
            Preference preferenceFindPreference4 = this.mPrefScreen.findPreference("cdma_system_select_key");
            this.mButtonCdmaSystemSelect = preferenceFindPreference4;
            if (preferenceFindPreference4 != null) {
                this.mOptionsCategory.removePreference(preferenceFindPreference4);
            }
        } else {
            CommonOptions$$ExternalSyntheticBUOutline0.m("Unexpected phone type: ", phoneType);
            return;
        }
        this.mButtonSatelliteSettingKeyAosp = this.mPrefScreen.findPreference("telephony_satellite_setting_key");
        updateSatellitePreferenceAosp(true);
    }

    protected void updateSatellitePreferenceAosp(boolean z) {
        PreferenceScreen preferenceScreen;
        try {
            if (this.mButtonSatelliteSettingKeyAosp == null) {
                return;
            }
            PersistableBundle carrierConfigForSubId = PhoneGlobals.getInstance().getCarrierConfigForSubId(this.mPhone.getSubId());
            boolean z2 = carrierConfigForSubId != null && carrierConfigForSubId.getBoolean("satellite_attach_supported_bool", false);
            String str = LOG_TAG;
            Log.i(str, "aosp satellite, support:" + z2 + ",subid:" + this.mPhone.getSubId() + ",enabled:" + z);
            PreferenceScreen preferenceScreen2 = this.mPrefScreen;
            if (!z2) {
                if (preferenceScreen2.findPreference("telephony_satellite_setting_key") != null) {
                    this.mOptionsCategory.removePreference(this.mButtonSatelliteSettingKeyAosp);
                    Log.i(str, "aosp satellite, remove case");
                    return;
                }
                return;
            }
            if (preferenceScreen2.findPreference("telephony_satellite_setting_key") == null) {
                this.mOptionsCategory.addPreference(this.mButtonSatelliteSettingKeyAosp);
                Log.i(str, "aosp satellite, remove -> add case");
            }
            Log.i(str, "aosp satellite normal case");
            Intent intent = this.mButtonSatelliteSettingKeyAosp.getIntent();
            intent.putExtra(":settings:show_fragment_as_subsetting", true);
            intent.putExtra("sub_id", this.mPhone.getSubId());
            Context context = this.mPreferenceFragment.getContext();
            if (this.mSatelliteManager == null) {
                this.mSatelliteManager = (SatelliteManager) context.getSystemService(SatelliteManager.class);
            }
            String string = context.getString(!this.mSatelliteManager.getAttachRestrictionReasonsForCarrier(this.mPhone.getSubId()).contains(2) ? R.string.satellite_setting_enabled_summary : R.string.satellite_setting_disabled_summary);
            boolean z3 = HyperOSCustFeatureResolve.getBoolean("config_aosp_satellite_remove_summary", false);
            Preference preference = this.mButtonSatelliteSettingKeyAosp;
            if (z3) {
                preference.setSummary("");
            } else {
                preference.setSummary(string);
            }
            this.mButtonSatelliteSettingKeyAosp.setEnabled(z);
        } catch (Exception e) {
            if (this.mButtonSatelliteSettingKeyAosp != null && this.mOptionsCategory != null && (preferenceScreen = this.mPrefScreen) != null && preferenceScreen.findPreference("telephony_satellite_setting_key") != null) {
                this.mOptionsCategory.removePreference(this.mButtonSatelliteSettingKeyAosp);
            }
            Log.e(LOG_TAG, "exception:" + e);
        }
    }

    protected void destroy() {
        if (this.mSimStateReceiver != null) {
            this.mPreferenceFragment.getContext().unregisterReceiver(this.mSimStateReceiver);
        }
        VolteSwitchView volteSwitchView = this.mVolteSwitchView;
        if (volteSwitchView != null) {
            volteSwitchView.destroy();
        }
        VonrSwitchView vonrSwitchView = this.mVonrSwitchView;
        if (vonrSwitchView != null) {
            vonrSwitchView.destroy();
        }
        FourPointFiveSwitchView fourPointFiveSwitchView = this.mFourPointFiveSwitchView;
        if (fourPointFiveSwitchView != null) {
            fourPointFiveSwitchView.destroy();
        }
        FiveGSwitchTrView fiveGSwitchTrView = this.mFiveGSwitchTrView;
        if (fiveGSwitchTrView != null) {
            fiveGSwitchTrView.destroy();
        }
        Enable2GSwitchView enable2GSwitchView = this.mEnable2GSwitchView;
        if (enable2GSwitchView != null) {
            enable2GSwitchView.destroy();
        }
    }

    protected void setOptionEnabled(boolean z) {
        boolean zIsVolteSwitchExist;
        boolean zIsVolteSwitchOn;
        Preference preference = this.mButtonApnPref;
        if (preference != null) {
            preference.setEnabled(z);
        }
        Preference preference2 = this.mButtonPreferredNetworkType;
        if (preference2 != null) {
            preference2.setEnabled(z);
        }
        if (this.mButtonSatelliteSettingKeyAosp != null) {
            updateSatellitePreferenceAosp(z);
        }
        VolteSwitchView volteSwitchView = this.mVolteSwitchView;
        if (volteSwitchView != null) {
            volteSwitchView.updateVolteButtonUI();
            zIsVolteSwitchOn = this.mVolteSwitchView.isVolteSwitchOn();
            zIsVolteSwitchExist = this.mVolteSwitchView.isVolteSwitchExist();
        } else {
            zIsVolteSwitchExist = false;
            zIsVolteSwitchOn = true;
        }
        VonrSwitchView vonrSwitchView = this.mVonrSwitchView;
        if (vonrSwitchView != null) {
            vonrSwitchView.updateVonrButtonUI(!zIsVolteSwitchExist || zIsVolteSwitchOn);
        }
        FourPointFiveSwitchView fourPointFiveSwitchView = this.mFourPointFiveSwitchView;
        if (fourPointFiveSwitchView != null) {
            fourPointFiveSwitchView.updateFourPointFiveUI();
        }
        FiveGSwitchTrView fiveGSwitchTrView = this.mFiveGSwitchTrView;
        if (fiveGSwitchTrView != null) {
            fiveGSwitchTrView.updateFiveGUI();
        }
        Enable2GSwitchView enable2GSwitchView = this.mEnable2GSwitchView;
        if (enable2GSwitchView != null) {
            enable2GSwitchView.updateEnable2GSwitchUI();
        }
    }

    protected boolean isIccCardActivate() {
        if (MiuiPhoneUtils.isMultiSimEnabled()) {
            return MiuiPhoneUtils.isIccCardActivated(this.mSlotId);
        }
        return true;
    }

    public boolean preferenceTreeClick(Preference preference) {
        if (preference != this.mButtonPreferredNetworkType && preference != this.mButtonApnPref) {
            return false;
        }
        ScreenDisplayUtils.shouldForceSplit(preference.getIntent(), this.mPreferenceFragment.getActivity().getIntent().getMiuiFlags());
        return false;
    }

    protected void log(String str) {
        Log.d(LOG_TAG, str);
    }
}
