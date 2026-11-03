package com.android.phone;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.os.UserManager;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import com.mediatek.settings.vtss.GsmUmtsVTCBOptions;
import com.mediatek.settings.vtss.GsmUmtsVTCFOptions;

/* JADX INFO: loaded from: classes.dex */
public class GsmUmtsCallOptions extends PreferenceActivity implements PhoneGlobals.SubInfoUpdateListener {
    private final boolean DBG = true;

    @Override // android.preference.PreferenceActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addSystemFlags(524288);
        addPreferencesFromResource(R.xml.gsm_umts_call_options);
        SubscriptionInfoHelper subscriptionInfoHelper = new SubscriptionInfoHelper(this, getIntent());
        subscriptionInfoHelper.setActionBarTitle(getActionBar(), getResources(), R.string.labelGsmMore_with_label);
        init(getPreferenceScreen(), subscriptionInfoHelper);
        PhoneGlobals.getInstance().addSubInfoUpdateListener(this);
    }

    @Override // android.preference.PreferenceActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public static void init(PreferenceScreen preferenceScreen, SubscriptionInfoHelper subscriptionInfoHelper) {
        PersistableBundle carrierConfig;
        if (subscriptionInfoHelper.hasSubId()) {
            carrierConfig = PhoneGlobals.getInstance().getCarrierConfigForSubId(subscriptionInfoHelper.getSubId());
        } else {
            carrierConfig = PhoneGlobals.getInstance().getCarrierConfig();
        }
        boolean z = true;
        if (carrierConfig != null && carrierConfig.getBoolean("disable_supplementary_services_in_airplane_mode_bool") && 1 == Settings.Global.getInt(subscriptionInfoHelper.getPhone().getContext().getContentResolver(), "airplane_mode_on", 0)) {
            z = false;
        }
        boolean zHasUserRestriction = ((UserManager) subscriptionInfoHelper.getPhone().getContext().getSystemService("user")).hasUserRestriction("no_config_mobile_networks");
        if (zHasUserRestriction) {
            Log.i("GsmUmtsCallOptions", "Mobile network configs are restricted, hiding GSM call forwarding, additional call settings, and call options.");
        }
        Preference preferenceFindPreference = preferenceScreen.findPreference("call_forwarding_key");
        if (preferenceFindPreference != null) {
            if (carrierConfig != null && carrierConfig.getBoolean("call_forwarding_visibility_bool") && !zHasUserRestriction) {
                if (carrierConfig.getBoolean("mtk_support_vt_ss_bool") || SystemProperties.get("persist.vendor.video.ss_dbg").equals("1")) {
                    preferenceFindPreference.setIntent(subscriptionInfoHelper.getIntent(GsmUmtsVTCFOptions.class));
                } else {
                    preferenceFindPreference.setIntent(subscriptionInfoHelper.getIntent(GsmUmtsCallForwardOptions.class));
                }
                preferenceFindPreference.setEnabled(z);
            } else {
                preferenceScreen.removePreference(preferenceFindPreference);
            }
        }
        Preference preferenceFindPreference2 = preferenceScreen.findPreference("additional_gsm_call_settings_key");
        if (preferenceFindPreference2 != null) {
            if (carrierConfig != null && ((carrierConfig.getBoolean("additional_settings_call_waiting_visibility_bool") || carrierConfig.getBoolean("additional_settings_caller_id_visibility_bool")) && !zHasUserRestriction)) {
                preferenceFindPreference2.setIntent(subscriptionInfoHelper.getIntent(GsmUmtsAdditionalCallOptions.class));
                preferenceFindPreference2.setEnabled(z);
            } else {
                preferenceScreen.removePreference(preferenceFindPreference2);
            }
        }
        Preference preferenceFindPreference3 = preferenceScreen.findPreference("call_barring_key");
        if (preferenceFindPreference3 != null) {
            if (carrierConfig != null && carrierConfig.getBoolean("call_barring_visibility_bool") && !zHasUserRestriction) {
                if (carrierConfig.getBoolean("mtk_support_vt_ss_bool") || SystemProperties.get("persist.vendor.video.ss_dbg").equals("1")) {
                    preferenceFindPreference3.setIntent(subscriptionInfoHelper.getIntent(GsmUmtsVTCBOptions.class));
                } else {
                    preferenceFindPreference3.setIntent(subscriptionInfoHelper.getIntent(GsmUmtsCallBarringOptions.class));
                }
                preferenceFindPreference3.setEnabled(z);
                return;
            }
            preferenceScreen.removePreference(preferenceFindPreference3);
        }
    }

    @Override // android.preference.PreferenceActivity, android.app.ListActivity, android.app.Activity
    protected void onDestroy() {
        PhoneGlobals.getInstance().removeSubInfoUpdateListener(this);
        super.onDestroy();
    }

    @Override // com.android.phone.PhoneGlobals.SubInfoUpdateListener
    public void handleSubInfoUpdate() {
        finish();
    }
}
