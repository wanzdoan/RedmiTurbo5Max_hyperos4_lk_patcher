package com.android.phone;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.MenuItem;
import com.android.internal.telephony.Phone;
import com.android.phone.settings.SuppServicesUiUtil;

/* JADX INFO: loaded from: classes.dex */
public class CdmaCallOptions extends TimeConsumingPreferenceActivity implements PhoneGlobals.SubInfoUpdateListener {
    private Phone mPhone;
    private SubscriptionInfoHelper mSubscriptionInfoHelper;

    private void unregisterMmTelCapsCallback() {
    }

    @Override // android.preference.PreferenceActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override // android.preference.PreferenceActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        unregisterMmTelCapsCallback();
    }

    @Override // android.preference.PreferenceActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // android.preference.PreferenceActivity
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference.getKey().equals("button_voice_privacy_key")) {
            return true;
        }
        if (preference == preferenceScreen.findPreference("call_forwarding_key")) {
            return doSsOverUtPrecautions(preference);
        }
        return false;
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

    private boolean doSsOverUtPrecautions(Preference preference) {
        PersistableBundle carrierConfig;
        Phone phone;
        if (this.mSubscriptionInfoHelper.hasSubId()) {
            carrierConfig = PhoneGlobals.getInstance().getCarrierConfigForSubId(this.mSubscriptionInfoHelper.getSubId());
        } else {
            carrierConfig = PhoneGlobals.getInstance().getCarrierConfig();
        }
        if (carrierConfig != null && carrierConfig.getBoolean("call_forwarding_over_ut_warning_bool") && (phone = this.mPhone) != null && SuppServicesUiUtil.isSsOverUtPrecautions(this, phone)) {
            SuppServicesUiUtil.showBlockingSuppServicesDialog(this, this.mPhone, preference.getKey()).show();
            return true;
        }
        Log.d("CdmaCallOptions", "No Ut Precautions for: call_forwarding_over_ut_warning_bool");
        return false;
    }
}
