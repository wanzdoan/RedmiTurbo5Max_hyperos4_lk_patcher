package com.android.phone.settings;

import android.content.res.Resources;
import android.provider.Settings;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.internal.telephony.Phone;
import com.android.phone.ModuleDelegate;
import com.android.phone.R;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.utils.ScreenDisplayUtils;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;
import miuix.preference.PreferenceFragment;

/* JADX INFO: loaded from: classes.dex */
public class GsmUmtsOptions extends CommonOptions {
    private static final String LOG_TAG = "GsmUmtsOptions";
    private Preference mButtonOperatorSelectionPref;

    public GsmUmtsOptions(PreferenceFragment preferenceFragment, PreferenceScreen preferenceScreen, Phone phone) {
        super(preferenceFragment, preferenceScreen, phone);
        this.mPreferenceFragment.addPreferencesFromResource(R.xml.miui_gsm_umts_options);
        create();
    }

    public GsmUmtsOptions(PreferenceFragment preferenceFragment, PreferenceScreen preferenceScreen, PreferenceCategory preferenceCategory, Phone phone) {
        super(preferenceFragment, preferenceScreen, preferenceCategory, phone);
        create();
    }

    @Override // com.android.phone.settings.CommonOptions
    protected void create() {
        super.create();
        Preference preferenceFindPreference = this.mPrefScreen.findPreference("button_carrier_sel_key");
        this.mButtonOperatorSelectionPref = preferenceFindPreference;
        SubscriptionManager.putSlotIdExtra(preferenceFindPreference.getIntent(), this.mSlotId);
        enableScreen();
    }

    @Override // com.android.phone.settings.CommonOptions
    public void enableScreen() {
        Preference preference;
        if (this.mPhone.getPhoneType() != 1) {
            log("Not a GSM phone, disabling GSM preferences (apn, use2g, select operator)");
            this.mButtonApnPref.setEnabled(false);
            Preference preference2 = this.mButtonOperatorSelectionPref;
            if (preference2 != null) {
                preference2.setEnabled(false);
            }
        } else {
            log("Not a CDMA phone");
            Resources resources = this.mPreferenceFragment.getResources();
            if (!resources.getBoolean(R.bool.config_apn_expand)) {
                this.mOptionsCategory.removePreference(this.mPrefScreen.findPreference("button_apn_key"));
            }
            if (!resources.getBoolean(R.bool.config_operator_selection_expand) && (preference = this.mButtonOperatorSelectionPref) != null) {
                this.mOptionsCategory.removePreference(preference);
                this.mButtonOperatorSelectionPref = null;
            }
        }
        updateOperatorSelectionVisibility();
    }

    private void updateOperatorSelectionVisibility() {
        log("updateOperatorSelectionVisibility: mPhone = " + this.mPhone.getPhoneName());
        Resources resources = this.mPreferenceFragment.getResources();
        if (this.mButtonOperatorSelectionPref == null) {
            Log.e(LOG_TAG, "mButtonOperatorSelectionPref is null");
            return;
        }
        if (!((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isManualNetSelAllowed(this.mPhone)) {
            log("Manual network selection not allowed.Disabling Operator Selection menu.");
            this.mButtonOperatorSelectionPref.setEnabled(false);
        } else if (resources.getBoolean(R.bool.csp_enabled)) {
            if (this.mPhone.isCspPlmnEnabled()) {
                log("Enabling Operator Selection menu.");
                this.mButtonOperatorSelectionPref.setEnabled(!(Settings.System.getInt(this.mPreferenceFragment.getContext().getContentResolver(), "airplane_mode_on", -1) == 1));
            } else {
                log("Disabling Operator Selection menu.");
                Preference preference = this.mButtonOperatorSelectionPref;
                if (preference != null) {
                    preference.setEnabled(false);
                }
            }
        }
        if (TelephonyManager.getDefault().getSimStateForSlot(this.mSlotId) == 5 && isIccCardActivate()) {
            return;
        }
        setOptionEnabled(false);
    }

    private void setGsmUmtsOptionEnable(boolean z) {
        Preference preference = this.mButtonOperatorSelectionPref;
        if (preference != null) {
            preference.setEnabled(z);
        }
    }

    @Override // com.android.phone.settings.CommonOptions
    protected void setOptionEnabled(boolean z) {
        super.setOptionEnabled(z);
        setGsmUmtsOptionEnable(z);
    }

    @Override // com.android.phone.settings.CommonOptions
    public boolean preferenceTreeClick(Preference preference) {
        Preference preference2 = this.mButtonOperatorSelectionPref;
        if (preference == preference2) {
            ScreenDisplayUtils.shouldForceSplit(preference2.getIntent(), this.mPreferenceFragment.getActivity().getIntent().getMiuiFlags());
            return false;
        }
        return super.preferenceTreeClick(preference);
    }
}
