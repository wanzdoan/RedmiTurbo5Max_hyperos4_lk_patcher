package com.android.phone;

import android.app.ActionBar;
import android.app.Dialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.MenuItem;
import com.android.internal.telephony.Phone;
import com.android.phone.settings.SuppServicesUiUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.ArrayList;
import miuix.animation.utils.EaseManager;

/* JADX INFO: loaded from: classes.dex */
public class GsmUmtsAdditionalCallOptions extends TimeConsumingPreferenceActivity implements PhoneGlobals.SubInfoUpdateListener {
    private CLIRListPreference mCLIRButton;
    private CallWaitingSwitchPreference mCWButton;
    private Phone mPhone;
    private SubscriptionInfoHelper mSubscriptionInfoHelper;
    private final boolean DBG = true;
    private final ArrayList mPreferences = new ArrayList();
    private int mInitIndex = 0;
    private boolean mShowCLIRButton = true;
    private boolean mShowCWButton = true;
    private boolean mCLIROverUtPrecautions = false;
    private boolean mCWOverUtPrecautions = false;

    @Override // android.preference.PreferenceActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        PersistableBundle carrierConfig;
        CLIRListPreference cLIRListPreference;
        CallWaitingSwitchPreference callWaitingSwitchPreference;
        super.onCreate(bundle);
        getWindow().addSystemFlags(524288);
        addPreferencesFromResource(R.xml.gsm_umts_additional_options);
        SubscriptionInfoHelper subscriptionInfoHelper = new SubscriptionInfoHelper(this, getIntent());
        this.mSubscriptionInfoHelper = subscriptionInfoHelper;
        subscriptionInfoHelper.setActionBarTitle(getActionBar(), getResources(), R.string.additional_gsm_call_settings_with_label);
        this.mPhone = this.mSubscriptionInfoHelper.getPhone();
        PhoneGlobals.getInstance().addSubInfoUpdateListener(this);
        if (this.mPhone == null) {
            Log.d("GsmUmtsAdditionalCallOptions", "onCreate: mPhone is null, finish!!!");
            finish();
            return;
        }
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        this.mCLIRButton = (CLIRListPreference) preferenceScreen.findPreference("button_clir_key");
        this.mCWButton = (CallWaitingSwitchPreference) preferenceScreen.findPreference("button_cw_key");
        if (this.mSubscriptionInfoHelper.hasSubId()) {
            carrierConfig = PhoneGlobals.getInstance().getCarrierConfigForSubId(this.mSubscriptionInfoHelper.getSubId());
        } else {
            carrierConfig = PhoneGlobals.getInstance().getCarrierConfig();
        }
        if (carrierConfig != null) {
            this.mShowCLIRButton = carrierConfig.getBoolean("additional_settings_caller_id_visibility_bool");
            this.mShowCWButton = carrierConfig.getBoolean("additional_settings_call_waiting_visibility_bool");
            this.mCLIROverUtPrecautions = this.mShowCLIRButton && carrierConfig.getBoolean("caller_id_over_ut_warning_bool");
            this.mCWOverUtPrecautions = this.mShowCWButton && carrierConfig.getBoolean("call_waiting_over_ut_warning_bool");
            Log.d("GsmUmtsAdditionalCallOptions", "mCLIROverUtPrecautions:" + this.mCLIROverUtPrecautions + ",mCWOverUtPrecautions:" + this.mCWOverUtPrecautions);
        }
        boolean zIsSsOverUtPrecautions = SuppServicesUiUtil.isSsOverUtPrecautions(this, this.mPhone);
        CLIRListPreference cLIRListPreference2 = this.mCLIRButton;
        if (cLIRListPreference2 != null) {
            if (this.mShowCLIRButton) {
                if (this.mCLIROverUtPrecautions && zIsSsOverUtPrecautions) {
                    cLIRListPreference2.setEnabled(false);
                } else {
                    this.mPreferences.add(cLIRListPreference2);
                }
            } else {
                preferenceScreen.removePreference(cLIRListPreference2);
            }
        }
        CallWaitingSwitchPreference callWaitingSwitchPreference2 = this.mCWButton;
        if (callWaitingSwitchPreference2 != null) {
            if (this.mShowCWButton) {
                if (this.mCWOverUtPrecautions && zIsSsOverUtPrecautions) {
                    callWaitingSwitchPreference2.setEnabled(false);
                } else {
                    this.mPreferences.add(callWaitingSwitchPreference2);
                }
            } else {
                preferenceScreen.removePreference(callWaitingSwitchPreference2);
            }
        }
        this.mIsForeground = true;
        if (this.mPreferences.size() != 0) {
            if (bundle == null) {
                Log.d("GsmUmtsAdditionalCallOptions", "start to init ");
                doPreferenceInit(this.mInitIndex);
            } else {
                Log.d("GsmUmtsAdditionalCallOptions", "restore stored states");
                this.mInitIndex = this.mPreferences.size();
                if (this.mShowCWButton && (callWaitingSwitchPreference = this.mCWButton) != null) {
                    callWaitingSwitchPreference.init(this, true, this.mPhone);
                    Bundle bundle2 = (Bundle) bundle.getParcelable(this.mCWButton.getKey());
                    if (bundle2 != null) {
                        this.mCWButton.setChecked(bundle2.getBoolean("toggle"));
                        this.mCWButton.setEnabled(bundle2.getBoolean(MEModuleSignalT1s.EXTRA_STATE));
                    }
                }
                if (this.mShowCLIRButton && (cLIRListPreference = this.mCLIRButton) != null) {
                    cLIRListPreference.init(this, true, this.mPhone);
                    int[] intArray = bundle.getIntArray(this.mCLIRButton.getKey());
                    if (intArray != null) {
                        Log.d("GsmUmtsAdditionalCallOptions", "onCreate:  clirArray[0]=" + intArray[0] + ", clirArray[1]=" + intArray[1]);
                        this.mCLIRButton.handleGetCLIRResult(intArray);
                    } else {
                        this.mCLIRButton.init(this, false, this.mPhone);
                    }
                }
            }
        }
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override // com.android.phone.TimeConsumingPreferenceActivity, android.app.Activity
    public void onResume() {
        CallWaitingSwitchPreference callWaitingSwitchPreference;
        CLIRListPreference cLIRListPreference;
        super.onResume();
        int size = this.mPreferences.size();
        boolean zIsSsOverUtPrecautions = SuppServicesUiUtil.isSsOverUtPrecautions(this, this.mPhone);
        dismissWarningDialog();
        if (this.mShowCLIRButton && this.mCLIROverUtPrecautions && (cLIRListPreference = this.mCLIRButton) != null) {
            if (zIsSsOverUtPrecautions) {
                showWarningDialog(EaseManager.EaseStyleDef.PERLIN);
                if (this.mCLIRButton.isEnabled()) {
                    if (this.mPreferences.contains(this.mCLIRButton)) {
                        this.mPreferences.remove(this.mCLIRButton);
                    }
                    this.mCLIRButton.setEnabled(false);
                }
            } else if (!this.mPreferences.contains(cLIRListPreference)) {
                this.mCLIRButton.setEnabled(true);
                this.mPreferences.add(this.mCLIRButton);
            }
        }
        if (this.mShowCWButton && this.mCWOverUtPrecautions && (callWaitingSwitchPreference = this.mCWButton) != null) {
            if (zIsSsOverUtPrecautions) {
                showWarningDialog(202);
                if (this.mCWButton.isEnabled()) {
                    if (this.mPreferences.contains(this.mCWButton)) {
                        this.mPreferences.remove(this.mCWButton);
                    }
                    this.mCWButton.setEnabled(false);
                }
            } else if (!this.mPreferences.contains(callWaitingSwitchPreference)) {
                this.mCWButton.setEnabled(true);
                this.mPreferences.add(this.mCWButton);
            }
        }
        if (size < this.mPreferences.size()) {
            this.mInitIndex = size;
            doPreferenceInit(size);
        }
    }

    @Override // android.preference.PreferenceActivity, android.app.Activity
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (this.mShowCLIRButton) {
            CLIRListPreference cLIRListPreference = this.mCLIRButton;
            if (cLIRListPreference.clirArray != null) {
                bundle.putIntArray(cLIRListPreference.getKey(), this.mCLIRButton.clirArray);
            }
        }
        if (!this.mShowCWButton || this.mCWButton == null) {
            return;
        }
        Bundle bundle2 = new Bundle();
        bundle2.putBoolean("toggle", this.mCWButton.isChecked());
        bundle2.putBoolean(MEModuleSignalT1s.EXTRA_STATE, this.mCWButton.isEnabled());
        bundle.putBundle(this.mCWButton.getKey(), bundle2);
    }

    @Override // com.android.phone.TimeConsumingPreferenceActivity, com.android.phone.TimeConsumingPreferenceListener
    public void onFinished(Preference preference, boolean z) {
        if (this.mInitIndex < this.mPreferences.size() - 1 && !isFinishing()) {
            int i = this.mInitIndex + 1;
            this.mInitIndex = i;
            doPreferenceInit(i);
        }
        super.onFinished(preference, z);
    }

    @Override // android.preference.PreferenceActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            CallFeaturesSetting.goUpToTopLevelSetting(this, this.mSubscriptionInfoHelper);
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void doPreferenceInit(int i) {
        if (this.mPreferences.size() > i) {
            Preference preference = (Preference) this.mPreferences.get(i);
            if (preference instanceof CallWaitingSwitchPreference) {
                ((CallWaitingSwitchPreference) preference).init(this, false, this.mPhone);
            } else if (preference instanceof CLIRListPreference) {
                ((CLIRListPreference) preference).init(this, false, this.mPhone);
            }
        }
    }

    @Override // com.android.phone.TimeConsumingPreferenceActivity, android.app.Activity
    protected Dialog onCreateDialog(int i) {
        if (i == 201) {
            return SuppServicesUiUtil.showBlockingSuppServicesDialog(this, this.mPhone, "button_cw_key");
        }
        if (i == 202) {
            return SuppServicesUiUtil.showBlockingSuppServicesDialog(this, this.mPhone, "button_clir_key");
        }
        return super.onCreateDialog(i);
    }

    private void showWarningDialog(int i) {
        showDialog(i);
    }

    private void dismissWarningDialog() {
        dismissDialogSafely(EaseManager.EaseStyleDef.PERLIN);
        dismissDialogSafely(202);
    }

    @Override // android.preference.PreferenceActivity, android.app.ListActivity, android.app.Activity
    public void onDestroy() {
        PhoneGlobals.getInstance().removeSubInfoUpdateListener(this);
        super.onDestroy();
    }

    @Override // com.android.phone.PhoneGlobals.SubInfoUpdateListener
    public void handleSubInfoUpdate() {
        finish();
    }
}
