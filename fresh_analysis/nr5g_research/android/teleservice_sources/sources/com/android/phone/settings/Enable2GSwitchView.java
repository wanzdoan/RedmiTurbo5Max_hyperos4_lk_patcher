package com.android.phone.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceGroup;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.ModuleDelegate;
import com.android.phone.R;
import com.android.phone.module.ITelephonyModule;
import miui.os.HyperOSCustFeatureResolve;
import miuix.appcompat.app.AlertDialog;
import miuix.preference.PreferenceFragment;

/* JADX INFO: loaded from: classes.dex */
public class Enable2GSwitchView {
    public static final String TAG = "Enable2GSwitchView";
    private CheckBoxPreference mButton2G;
    private boolean mIs2GProtectionMode;
    private boolean mIsAirplaneEnabled;
    private Phone mPhone;
    protected PreferenceCategory mPrefCategory;
    private PreferenceGroup mPrefGroup;
    private PreferenceFragment mPreferenceFragment;
    private AlertDialog mPromptDialog = null;
    final BroadcastReceiver mReceiver;

    public Enable2GSwitchView(PreferenceFragment preferenceFragment, PreferenceGroup preferenceGroup, PreferenceCategory preferenceCategory, Phone phone) {
        this.mPhone = null;
        this.mIsAirplaneEnabled = false;
        this.mIs2GProtectionMode = false;
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.settings.Enable2GSwitchView.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if ("android.telephony.action.CARRIER_CONFIG_CHANGED".equals(action)) {
                    int intExtra = intent.getIntExtra("phone", -1);
                    Enable2GSwitchView.this.log("ACTION_CARRIER_CONFIG_CHANGED phoneId: " + intExtra);
                    if (Enable2GSwitchView.this.mPhone.getPhoneId() != intExtra) {
                        return;
                    }
                    Enable2GSwitchView.this.updateEnable2GSwitchUI();
                    return;
                }
                if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                    Enable2GSwitchView enable2GSwitchView = Enable2GSwitchView.this;
                    enable2GSwitchView.mIsAirplaneEnabled = Settings.Global.getInt(enable2GSwitchView.mPhone.getContext().getContentResolver(), "airplane_mode_on", 0) == 1;
                    Enable2GSwitchView.this.updateEnable2GSwitchUI();
                }
            }
        };
        this.mReceiver = broadcastReceiver;
        this.mPreferenceFragment = preferenceFragment;
        this.mPrefGroup = preferenceGroup;
        this.mPrefCategory = preferenceCategory;
        this.mPhone = phone;
        this.mButton2G = (CheckBoxPreference) preferenceGroup.findPreference("enable_2g");
        this.mIsAirplaneEnabled = Settings.Global.getInt(this.mPhone.getContext().getContentResolver(), "airplane_mode_on", 0) == 1;
        this.mIs2GProtectionMode = HyperOSCustFeatureResolve.getBooleanForPhoneId(this.mPhone.getPhoneId(), "config_show_2g_protection_switch", false);
        this.mButton2G.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.phone.settings.Enable2GSwitchView.1
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                if (Enable2GSwitchView.this.mPreferenceFragment == null) {
                    return false;
                }
                boolean zIsPhoneInCall = MiuiPhoneUtils.isPhoneInCall();
                Enable2GSwitchView enable2GSwitchView = Enable2GSwitchView.this;
                if (zIsPhoneInCall) {
                    Toast.makeText(enable2GSwitchView.mPreferenceFragment.getContext(), R.string.preferred_network_restrict_network_changed_incall, 0).show();
                    Enable2GSwitchView.this.mButton2G.setChecked(!Enable2GSwitchView.this.mButton2G.isChecked());
                    return false;
                }
                boolean z = enable2GSwitchView.mIs2GProtectionMode;
                Enable2GSwitchView enable2GSwitchView2 = Enable2GSwitchView.this;
                if (z) {
                    enable2GSwitchView2.setEnable2G(!enable2GSwitchView2.mButton2G.isChecked());
                } else {
                    boolean zIsChecked = enable2GSwitchView2.mButton2G.isChecked();
                    Enable2GSwitchView enable2GSwitchView3 = Enable2GSwitchView.this;
                    if (zIsChecked) {
                        enable2GSwitchView3.setEnable2G(true);
                    } else {
                        enable2GSwitchView3.setEnable2G(false);
                    }
                }
                return true;
            }
        });
        IntentFilter intentFilter = new IntentFilter("android.telephony.action.CARRIER_CONFIG_CHANGED");
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        this.mPhone.getContext().registerReceiver(broadcastReceiver, intentFilter, 2);
        init2GSwitchOption();
        updateEnable2GSwitchUI();
    }

    private void init2GSwitchOption() {
        CheckBoxPreference checkBoxPreference = this.mButton2G;
        if (checkBoxPreference != null) {
            if (this.mIs2GProtectionMode) {
                checkBoxPreference.setTitle(R.string.enable_2g_protection_title);
                this.mButton2G.setSummary(R.string.enable_2g_protection_summary);
            } else {
                checkBoxPreference.setTitle(R.string.enable_2g_title_eu);
                this.mButton2G.setSummary(R.string.enable_2g_summary_eu);
            }
        }
    }

    public void updateEnable2GSwitchUI() {
        boolean zIsSupportEnable2GSwitch = MiuiPhoneUtils.isSupportEnable2GSwitch(this.mPhone);
        boolean enable2GState = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getEnable2GState(this.mPhone);
        log("updateEnable2GSwitchUI: isSupportEnable2GSwitch =" + zIsSupportEnable2GSwitch + ", isChecked = " + enable2GState + ", mIsAirplaneEnabled = " + this.mIsAirplaneEnabled);
        CheckBoxPreference checkBoxPreference = this.mButton2G;
        if (checkBoxPreference == null || this.mPreferenceFragment == null) {
            return;
        }
        if (!zIsSupportEnable2GSwitch) {
            this.mPrefCategory.removePreference(checkBoxPreference);
            return;
        }
        if (this.mIsAirplaneEnabled) {
            checkBoxPreference.setEnabled(false);
            return;
        }
        if (ModemProvider.getInstance().getPreferredNetworkModeFromDb(this.mPhone.getPhoneId()) == 1) {
            log("select 2g only, set 2g allowed enable. NetwotkType: " + ModemProvider.getInstance().getPreferredNetworkModeFromDb(this.mPhone.getPhoneId()));
            this.mButton2G.setEnabled(false);
            this.mButton2G.setChecked(this.mIs2GProtectionMode ^ true);
            if (enable2GState) {
                return;
            }
            setEnable2G(true);
            return;
        }
        this.mButton2G.setEnabled(true);
        CheckBoxPreference checkBoxPreference2 = this.mButton2G;
        if (this.mIs2GProtectionMode) {
            enable2GState = !enable2GState;
        }
        checkBoxPreference2.setChecked(enable2GState);
    }

    public void setEnable2G(boolean z) {
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setEnable2G(this.mPhone.getPhoneId(), z, null);
    }

    public void destroy() {
        this.mPhone.getContext().unregisterReceiver(this.mReceiver);
        AlertDialog alertDialog = this.mPromptDialog;
        if (alertDialog != null && alertDialog.isShowing()) {
            this.mPromptDialog.dismiss();
        }
        this.mPromptDialog = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d(TAG + "[" + this.mPhone.getPhoneId() + "]", str);
    }
}
