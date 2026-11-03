package com.android.phone.settings;

import android.content.Context;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import com.android.internal.telephony.Phone;
import com.android.phone.FiveGManagerBase;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.R;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.utils.FiveGUtils;
import miuix.preference.PreferenceFragment;

/* JADX INFO: loaded from: classes.dex */
public class VonrSwitchView implements Preference.OnPreferenceClickListener {
    public static final String TAG = "VonrSwitchView";
    private CheckBoxPreference mButtonVonr;
    private Context mContext;
    private Handler mHandler;
    private Phone mPhone;
    private PreferenceGroup mPrefGroup;
    private PreferenceFragment mPreferenceFragment;
    private int mSlotId;
    private int mSubId;

    public void destroy() {
    }

    public VonrSwitchView(PreferenceFragment preferenceFragment, PreferenceGroup preferenceGroup, Phone phone) {
        this.mPreferenceFragment = preferenceFragment;
        this.mPrefGroup = preferenceGroup;
        this.mPhone = phone;
        this.mContext = preferenceFragment.getContext();
        this.mSubId = this.mPhone.getSubId();
        this.mSlotId = this.mPhone.getPhoneId();
        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) this.mPrefGroup.findPreference("vonr_switch");
        this.mButtonVonr = checkBoxPreference;
        checkBoxPreference.setOnPreferenceClickListener(this);
        this.mHandler = new Handler();
    }

    public void updateVonrButtonUI(boolean z) {
        if (this.mButtonVonr == null || this.mPreferenceFragment == null) {
            return;
        }
        boolean z2 = false;
        boolean z3 = this.mPrefGroup.findPreference("vonr_switch") != null;
        if (!FiveGUtils.isVonrSwitchVisiable(this.mSlotId) || !MiuiPhoneUtils.isIccCardActivated(this.mPhone.getPhoneId())) {
            if (z3) {
                this.mPrefGroup.removePreference(this.mButtonVonr);
                return;
            }
            return;
        }
        if (!z3) {
            this.mPrefGroup.addPreference(this.mButtonVonr);
        }
        boolean zShouldEnableVonrSwitch = shouldEnableVonrSwitch();
        CheckBoxPreference checkBoxPreference = this.mButtonVonr;
        if (z && zShouldEnableVonrSwitch) {
            z2 = true;
        }
        checkBoxPreference.setEnabled(z2);
        new Thread(new Runnable() { // from class: com.android.phone.settings.VonrSwitchView.1
            @Override // java.lang.Runnable
            public void run() {
                VonrSwitchView vonrSwitchView = VonrSwitchView.this;
                vonrSwitchView.mSubId = vonrSwitchView.mPhone.getSubId();
                final boolean zIsVoNREnabled = FiveGManagerBase.getInstance().isVoNREnabled(VonrSwitchView.this.mSubId);
                VonrSwitchView.this.mHandler.post(new Runnable() { // from class: com.android.phone.settings.VonrSwitchView.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        ITelephonyModule iTelephonyModule = (ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class);
                        if (iTelephonyModule.isCallOptimizeInjected() && (iTelephonyModule.isModemVonrOptimizeOperator() || iTelephonyModule.shouldDoCallVonrOptimization())) {
                            boolean zIsDisableVoNrByOptimization = iTelephonyModule.isDisableVoNrByOptimization(VonrSwitchView.this.mSubId);
                            if (!zIsVoNREnabled && zIsDisableVoNrByOptimization) {
                                VonrSwitchView.this.mButtonVonr.setChecked(true);
                                return;
                            } else {
                                VonrSwitchView.this.mButtonVonr.setChecked(zIsVoNREnabled);
                                return;
                            }
                        }
                        VonrSwitchView.this.mButtonVonr.setChecked(zIsVoNREnabled);
                    }
                });
            }
        }).start();
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        if (this.mPreferenceFragment == null) {
            log("mPreferenceFragment == null");
            return false;
        }
        if (MiuiPhoneUtils.isPhoneInCall()) {
            Toast.makeText(this.mPreferenceFragment.getContext(), R.string.msim_set_sub_not_supported_phone_in_call, 0).show();
            CheckBoxPreference checkBoxPreference = this.mButtonVonr;
            checkBoxPreference.setChecked(!checkBoxPreference.isChecked());
            return false;
        }
        if (preference == this.mButtonVonr) {
            new Thread(new Runnable() { // from class: com.android.phone.settings.VonrSwitchView.2
                @Override // java.lang.Runnable
                public void run() {
                    FiveGManagerBase.getInstance().setUserVoNREnabled(VonrSwitchView.this.mPhone.getPhoneId(), VonrSwitchView.this.mButtonVonr.isChecked());
                    ITelephonyModule iTelephonyModule = (ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class);
                    if (iTelephonyModule.isCallOptimizeInjected()) {
                        if (iTelephonyModule.isModemVonrOptimizeOperator() || iTelephonyModule.shouldDoCallVonrOptimization()) {
                            iTelephonyModule.updateVoNrSwitchState(VonrSwitchView.this.mPhone.getPhoneId(), VonrSwitchView.this.mButtonVonr.isChecked());
                        }
                    }
                }
            }).start();
        }
        return true;
    }

    public boolean shouldEnableVonrSwitch() {
        if (Settings.System.getInt(this.mContext.getContentResolver(), "airplane_mode_on", -1) == 1) {
            return false;
        }
        if (FiveGManagerBase.getInstance().isUserFiveGSaEnabled(this.mPhone.getPhoneId())) {
            return true;
        }
        log("disable vonr button because sa is turned off");
        return false;
    }

    public void log(String str) {
        Log.d(TAG, str);
    }
}
