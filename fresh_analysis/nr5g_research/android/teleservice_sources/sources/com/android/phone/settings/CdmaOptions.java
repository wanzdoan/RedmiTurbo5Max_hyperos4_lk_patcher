package com.android.phone.settings;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.internal.telephony.Phone;
import com.android.phone.R;
import miuix.preference.PreferenceFragment;

/* JADX INFO: loaded from: classes.dex */
public class CdmaOptions extends CommonOptions {
    private CdmaSystemSelectListPreference mButtonCdmaSystemSelect;

    public CdmaOptions(PreferenceFragment preferenceFragment, PreferenceScreen preferenceScreen, Phone phone) {
        super(preferenceFragment, preferenceScreen, phone);
        this.mPreferenceFragment.addPreferencesFromResource(R.xml.miui_cdma_options);
        create();
    }

    public CdmaOptions(PreferenceFragment preferenceFragment, PreferenceScreen preferenceScreen, PreferenceCategory preferenceCategory, Phone phone) {
        super(preferenceFragment, preferenceScreen, preferenceCategory, phone);
        create();
    }

    @Override // com.android.phone.settings.CommonOptions
    protected void create() {
        super.create();
        CdmaSystemSelectListPreference cdmaSystemSelectListPreference = (CdmaSystemSelectListPreference) this.mPrefScreen.findPreference("cdma_system_select_key");
        this.mButtonCdmaSystemSelect = cdmaSystemSelectListPreference;
        cdmaSystemSelectListPreference.setEnabled(true);
    }

    @Override // com.android.phone.settings.CommonOptions
    public boolean preferenceTreeClick(Preference preference) {
        if (preference.getKey().equals("cdma_system_select_key")) {
            log("preferenceTreeClick: return BUTTON_CDMA_ROAMING_KEY true");
            return true;
        }
        return super.preferenceTreeClick(preference);
    }

    public void showDialog(Preference preference) {
        if (preference.getKey().equals("cdma_system_select_key")) {
            this.mButtonCdmaSystemSelect.showDialog(null);
        }
    }

    @Override // com.android.phone.settings.CommonOptions
    protected void setOptionEnabled(boolean z) {
        super.setOptionEnabled(z);
        setCdmaOptionEnabled(z);
    }

    private void setCdmaOptionEnabled(boolean z) {
        CdmaSystemSelectListPreference cdmaSystemSelectListPreference = this.mButtonCdmaSystemSelect;
        if (cdmaSystemSelectListPreference != null) {
            cdmaSystemSelectListPreference.setEnabled(z);
        }
    }
}
