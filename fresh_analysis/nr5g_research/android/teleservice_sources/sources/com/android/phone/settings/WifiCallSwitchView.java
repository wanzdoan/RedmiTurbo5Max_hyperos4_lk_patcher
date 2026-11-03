package com.android.phone.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.MiuiSettings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.phone.CloudController.TelephonyCloudController;
import com.android.phone.MiuiImsPhoneUtils;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.phone.WifiCallHelpActivity;
import com.android.phone.custompreferences.MiuixTextPreference;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.services.telephony.DeviceState;
import com.android.services.telephony.ims.ImsAdapterBase;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import java.util.HashMap;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.R$style;
import miuix.appcompat.app.AlertDialog;
import miuix.preference.PreferenceFragment;

/* JADX INFO: loaded from: classes.dex */
public class WifiCallSwitchView {
    private static final boolean DUAL_VOLTE_SUPPORTED = MiuiPhoneUtils.DUAL_VOLTE_SUPPORTED;
    public static final String TAG = "WifiCallSwitchView";
    private CheckBoxPreference mButtonEnableVoiceLink;
    private CheckBoxPreference mButtonEnableWifiCall;
    private MiuixTextPreference mButtonPreferredWifiCall;
    private ContentObserver mContentObserver;
    private boolean mPendingClaroVoiceLinkRetrigger = false;
    private Phone mPhone;
    private int mPhoneId;
    private PreferenceGroup mPrefGroup;
    private PreferenceFragment mPreferenceFragment;
    private MiuixTextPreference mRoamingButtonPreferredWifiCall;

    public WifiCallSwitchView(PreferenceFragment preferenceFragment, PreferenceGroup preferenceGroup, Phone phone) {
        Phone phone2;
        this.mPhoneId = 0;
        this.mPreferenceFragment = preferenceFragment;
        this.mPrefGroup = preferenceGroup;
        this.mPhone = phone;
        this.mButtonEnableWifiCall = (CheckBoxPreference) preferenceGroup.findPreference("enable_wificall");
        this.mButtonEnableVoiceLink = (CheckBoxPreference) this.mPrefGroup.findPreference("enable_voice_link");
        this.mButtonPreferredWifiCall = (MiuixTextPreference) this.mPrefGroup.findPreference("preferred_wificall_key");
        this.mRoamingButtonPreferredWifiCall = (MiuixTextPreference) this.mPrefGroup.findPreference("roaming_preferred_wificall_key");
        if (DUAL_VOLTE_SUPPORTED && (phone2 = this.mPhone) != null) {
            this.mPhoneId = phone2.getPhoneId();
        }
        this.mButtonEnableWifiCall.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.phone.settings.WifiCallSwitchView.1
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                if (WifiCallSwitchView.this.mPreferenceFragment == null) {
                    Log.d(WifiCallSwitchView.TAG, "mPrefActivity == null");
                    return false;
                }
                boolean zCanEditableWfcButton = WifiCallSwitchView.this.canEditableWfcButton();
                WifiCallSwitchView wifiCallSwitchView = WifiCallSwitchView.this;
                if (!zCanEditableWfcButton) {
                    wifiCallSwitchView.mButtonEnableWifiCall.setChecked(!WifiCallSwitchView.this.mButtonEnableWifiCall.isChecked());
                    Toast.makeText(WifiCallSwitchView.this.mPreferenceFragment.getContext(), R.string.msim_set_sub_not_supported_phone_in_call, 0).show();
                    return false;
                }
                boolean booleanForPhoneId = HyperOSCustFeatureResolve.getBooleanForPhoneId(wifiCallSwitchView.mPhone != null ? WifiCallSwitchView.this.mPhone.getPhoneId() : SubscriptionManager.INVALID_PHONE_ID, "show_wifi_calling_first_avaliable_dialog", false);
                if (WifiCallSwitchView.this.mButtonEnableWifiCall.isChecked() && booleanForPhoneId) {
                    Intent errorDialogActivityIntent = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getErrorDialogActivityIntent();
                    errorDialogActivityIntent.putExtra("dialog_type", 17);
                    errorDialogActivityIntent.setFlags(268435456);
                    WifiCallSwitchView.this.mPreferenceFragment.getContext().startActivity(errorDialogActivityIntent);
                    WifiCallSwitchView.this.updateWfcStateAndUI();
                    return true;
                }
                MiuiImsPhoneUtils.updateEmergencyWarningDialogAndCheckState(WifiCallSwitchView.this.mPhone, WifiCallSwitchView.this.mButtonEnableWifiCall.isChecked());
                if (!MiuiSettings.Global.getBoolean(WifiCallSwitchView.this.mPreferenceFragment.getContext().getContentResolver(), "wifi_call_available_dialog_showed") && MiuiImsPhoneUtils.isCustForOperatorVoWifiFeature(WifiCallSwitchView.this.mPhone) && WifiCallSwitchView.this.mButtonEnableWifiCall.isChecked()) {
                    WifiCallSwitchView.this.showWfcFirstAvailableDialog();
                } else if (MiuiPhoneUtils.isDeO2Sim(WifiCallSwitchView.this.mPhone) && WifiCallSwitchView.this.mButtonEnableWifiCall.isChecked()) {
                    Intent errorDialogActivityIntent2 = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getErrorDialogActivityIntent();
                    errorDialogActivityIntent2.putExtra("dialog_type", 23);
                    errorDialogActivityIntent2.setFlags(268435456);
                    WifiCallSwitchView.this.mPreferenceFragment.getContext().startActivity(errorDialogActivityIntent2);
                    WifiCallSwitchView.this.updateWfcStateAndUI();
                } else if (MiuiPhoneUtils.isClaroSim(WifiCallSwitchView.this.mPhone) && WifiCallSwitchView.this.mButtonEnableWifiCall.isChecked()) {
                    boolean zIsClaroWifiCallDialogChecked = MiuiImsPhoneUtils.isClaroWifiCallDialogChecked(WifiCallSwitchView.this.mPhone.getSubId());
                    WifiCallSwitchView wifiCallSwitchView2 = WifiCallSwitchView.this;
                    if (!zIsClaroWifiCallDialogChecked) {
                        wifiCallSwitchView2.mButtonEnableWifiCall.setChecked(false);
                        Intent errorDialogActivityIntent3 = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getErrorDialogActivityIntent();
                        errorDialogActivityIntent3.putExtra("dialog_type", 24);
                        errorDialogActivityIntent3.putExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, WifiCallSwitchView.this.mPhoneId);
                        errorDialogActivityIntent3.setFlags(268435456);
                        WifiCallSwitchView.this.mPendingClaroVoiceLinkRetrigger = true;
                        WifiCallSwitchView.this.mPreferenceFragment.getContext().startActivity(errorDialogActivityIntent3);
                        return true;
                    }
                    wifiCallSwitchView2.updateWfcStateAndUI();
                } else {
                    WifiCallSwitchView.this.updateWfcStateAndUI();
                }
                return true;
            }
        });
        this.mButtonEnableVoiceLink.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.phone.settings.WifiCallSwitchView.2
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                if (WifiCallSwitchView.this.mPreferenceFragment == null) {
                    Log.d(WifiCallSwitchView.TAG, "mPrefActivity == null");
                    return false;
                }
                boolean zIsChecked = WifiCallSwitchView.this.mButtonEnableVoiceLink.isChecked();
                if (!WifiCallSwitchView.this.canEditableWfcButton()) {
                    WifiCallSwitchView.this.mButtonEnableVoiceLink.setChecked(!zIsChecked);
                    Toast.makeText(WifiCallSwitchView.this.mPreferenceFragment.getContext(), R.string.msim_set_sub_not_supported_phone_in_call, 0).show();
                    return false;
                }
                ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setCrossSimCallingEnabled(WifiCallSwitchView.this.mPreferenceFragment.getContext(), WifiCallSwitchView.this.mPhoneId, WifiCallSwitchView.this.mButtonEnableVoiceLink.isChecked());
                if (zIsChecked) {
                    HashMap map = new HashMap();
                    map.put("mcc_mnc", MiuiPhoneUtils.getOperatorNumeric(WifiCallSwitchView.this.mPhone));
                    MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "enable_vocie_link", map);
                }
                return true;
            }
        });
        this.mButtonPreferredWifiCall.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.phone.settings.WifiCallSwitchView.3
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                boolean z = false;
                if (WifiCallSwitchView.this.mPreferenceFragment == null) {
                    Log.d(WifiCallSwitchView.TAG, "mPrefActivity == null");
                    return false;
                }
                boolean zCanEditableWfcButton = WifiCallSwitchView.this.canEditableWfcButton();
                WifiCallSwitchView wifiCallSwitchView = WifiCallSwitchView.this;
                if (!zCanEditableWfcButton) {
                    Toast.makeText(wifiCallSwitchView.mPreferenceFragment.getContext(), R.string.msim_set_sub_not_supported_phone_in_call, 0).show();
                    return false;
                }
                PreferenceFragment preferenceFragment2 = wifiCallSwitchView.mPreferenceFragment;
                WifiCallSwitchView wifiCallSwitchView2 = WifiCallSwitchView.this;
                if (wifiCallSwitchView2.mPhone.getServiceState().getRoaming() && !WifiCallSwitchView.this.extraShowRoamingWfcModeOption()) {
                    z = true;
                }
                preferenceFragment2.startActivity(wifiCallSwitchView2.buildWifiPreferredIntent(z));
                return true;
            }
        });
        this.mRoamingButtonPreferredWifiCall.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.phone.settings.WifiCallSwitchView.4
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                if (WifiCallSwitchView.this.mPreferenceFragment == null) {
                    Log.d(WifiCallSwitchView.TAG, "mPrefActivity == null");
                    return false;
                }
                boolean zCanEditableWfcButton = WifiCallSwitchView.this.canEditableWfcButton();
                WifiCallSwitchView wifiCallSwitchView = WifiCallSwitchView.this;
                if (!zCanEditableWfcButton) {
                    Toast.makeText(wifiCallSwitchView.mPreferenceFragment.getContext(), R.string.msim_set_sub_not_supported_phone_in_call, 0).show();
                    return false;
                }
                wifiCallSwitchView.mPreferenceFragment.startActivity(WifiCallSwitchView.this.buildWifiPreferredIntent(true));
                return true;
            }
        });
        initWifiOption();
        this.mContentObserver = new ContentObserver(new Handler()) { // from class: com.android.phone.settings.WifiCallSwitchView.5
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                super.onChange(z);
                Log.d(WifiCallSwitchView.TAG, "onChange");
                WifiCallSwitchView.this.retriggerVoiceLinkIfNeeded();
                WifiCallSwitchView.this.updateWifiCallPreferredUI();
            }
        };
        this.mPreferenceFragment.getContext().getContentResolver().registerContentObserver(android.telephony.SubscriptionManager.CONTENT_URI, true, this.mContentObserver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Intent buildWifiPreferredIntent(boolean z) {
        Intent intent = new Intent();
        intent.setClass(this.mPreferenceFragment.getContext(), PreferredWifiCallListPreference.class);
        intent.putExtra("wifi_call_roaming_bool", z);
        SubscriptionManager.putPhoneIdExtra(intent, this.mPhoneId);
        return intent;
    }

    private void initWifiOption() {
        if (MiuiImsPhoneUtils.isCustForVodafoneDE(this.mPhone)) {
            this.mPrefGroup.setTitle(R.string.wifi_call_setting_vodafone_de);
        } else if (MiuiImsPhoneUtils.isCustForVodafoneUK(this.mPhone)) {
            this.mPrefGroup.setTitle(R.string.wifi_call_setting_vodafone_uk);
        }
        if (this.mButtonEnableWifiCall == null || this.mPhone == null) {
            return;
        }
        this.mButtonEnableWifiCall.setTitle(android.telephony.SubscriptionManager.getResourcesForSubId(PhoneGlobals.getInstance().getApplicationContext(), this.mPhone.getSubId()).getString(R.string.wificall_switch_title));
    }

    private boolean isWfcSupportWifiOnly() {
        ImsAdapterBase.getInstance();
        return ImsAdapterBase.getInstance().isWfcSupportWifiOnly(this.mPreferenceFragment.getContext(), this.mPhone.getSubId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateWfcStateAndUI() {
        setWfcSetting();
        updateWifiCallPreferredUI();
    }

    private void setWfcSetting() {
        ImsAdapterBase.getInstance().setWfcSetting(this.mPreferenceFragment.getContext(), this.mPhoneId, this.mButtonEnableWifiCall.isChecked());
        if (this.mButtonEnableVoiceLink != null && this.mButtonEnableWifiCall.isChecked() && ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isCrossSimCallingEnabledByUser(this.mPreferenceFragment.getContext(), this.mPhoneId)) {
            ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setCrossSimCallingEnabled(this.mPreferenceFragment.getContext(), this.mPhoneId, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void retriggerVoiceLinkIfNeeded() {
        if (this.mPendingClaroVoiceLinkRetrigger && getWifiCallChecked()) {
            this.mPendingClaroVoiceLinkRetrigger = false;
            if (this.mButtonEnableVoiceLink == null || !((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isCrossSimCallingEnabledByUser(this.mPreferenceFragment.getContext(), this.mPhoneId)) {
                return;
            }
            ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setCrossSimCallingEnabled(this.mPreferenceFragment.getContext(), this.mPhoneId, true);
        }
    }

    private boolean editableWfcWhenAirplaneModeOn() {
        return CarrierConfigManagerEx.getBooleanConfigForSubId(this.mPhone.getContext(), this.mPhone.getSubId(), "editable_wfc_when_airplane_mode_on", true);
    }

    public boolean getWifiCallChecked() {
        return ImsAdapterBase.getInstance().isWfcEnabledByUser(this.mPreferenceFragment.getContext(), this.mPhoneId);
    }

    public void updateWifiCallPreferredUI() {
        CheckBoxPreference checkBoxPreference = this.mButtonEnableWifiCall;
        if (checkBoxPreference == null) {
            return;
        }
        checkBoxPreference.setChecked(getWifiCallChecked());
        this.mButtonEnableWifiCall.setEnabled(true);
        DeviceState deviceState = new DeviceState();
        if (deviceState.isAirplaneModeOn(this.mPhone.getContext())) {
            this.mButtonEnableWifiCall.setEnabled(editableWfcWhenAirplaneModeOn());
        }
        if (this.mPrefGroup == null) {
            return;
        }
        updateVoiceLinkUI();
        if (!this.mButtonEnableWifiCall.isChecked()) {
            this.mPrefGroup.removePreference(this.mButtonPreferredWifiCall);
            this.mPrefGroup.removePreference(this.mRoamingButtonPreferredWifiCall);
            return;
        }
        boolean z = this.mPhone.getServiceState().getRoaming() && !extraShowRoamingWfcModeOption();
        Log.d(TAG, "updateWifiCallPreferredUI isRoaming:" + z);
        boolean zShouldEnableWfcModeOption = shouldEnableWfcModeOption();
        PreferenceGroup preferenceGroup = this.mPrefGroup;
        if (zShouldEnableWfcModeOption) {
            preferenceGroup.addPreference(this.mButtonPreferredWifiCall);
            MiuixTextPreference miuixTextPreference = this.mButtonPreferredWifiCall;
            if (miuixTextPreference != null) {
                initPreferredWiFiCallValue(miuixTextPreference, z, deviceState);
            }
        } else {
            preferenceGroup.removePreference(this.mButtonPreferredWifiCall);
        }
        if (shouldEnableWfcModeOption() && extraShowRoamingWfcModeOption()) {
            this.mPrefGroup.addPreference(this.mRoamingButtonPreferredWifiCall);
            MiuixTextPreference miuixTextPreference2 = this.mRoamingButtonPreferredWifiCall;
            if (miuixTextPreference2 != null) {
                initPreferredWiFiCallValue(miuixTextPreference2, true, deviceState);
                return;
            }
            return;
        }
        this.mPrefGroup.removePreference(this.mRoamingButtonPreferredWifiCall);
    }

    private void initPreferredWiFiCallValue(MiuixTextPreference miuixTextPreference, boolean z, DeviceState deviceState) {
        boolean zIsWfcModeEditable;
        if (z) {
            zIsWfcModeEditable = ImsAdapterBase.getInstance().isWfcRoamingModeEditable(this.mPreferenceFragment.getContext(), this.mPhone.getSubId());
        } else {
            zIsWfcModeEditable = ImsAdapterBase.getInstance().isWfcModeEditable(this.mPreferenceFragment.getContext(), this.mPhone.getSubId());
        }
        miuixTextPreference.setEnabled((deviceState.isAirplaneModeOn(this.mPhone.getContext()) ? editableWfcWhenAirplaneModeOn() : true) & zIsWfcModeEditable);
        if (!isWfcSupportWifiOnly()) {
            miuixTextPreference.setValue(getWfcModeSummary(ImsAdapterBase.getInstance().getWfcMode(this.mPreferenceFragment.getContext(), this.mPhoneId, z)));
        } else {
            miuixTextPreference.setValue(this.mPreferenceFragment.getContext().getResources().getString(R.string.wfcmode_wifi_only));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean extraShowRoamingWfcModeOption() {
        return CarrierConfigManagerEx.getBooleanConfigForSubId(this.mPhone.getContext(), this.mPhone.getSubId(), "extra_show_roaming_wfc_mode_option", false);
    }

    private boolean shouldHideVoiceLinkButton(int i) {
        String[] stringArray = this.mPreferenceFragment.getContext().getResources().getStringArray(R.array.config_should_hide_vodata_sims);
        if (stringArray != null && stringArray.length >= 1) {
            try {
                String simOperatorNumeric = MiuiPhoneUtils.getSimOperatorNumeric(DefaultSimManager.getDefaultDataSlotId());
                String simOperatorNumeric2 = MiuiPhoneUtils.getSimOperatorNumeric(i);
                if (!TextUtils.isEmpty(simOperatorNumeric) && !TextUtils.isEmpty(simOperatorNumeric2)) {
                    for (String str : stringArray) {
                        int iIndexOf = str.indexOf(44);
                        if (iIndexOf >= 0) {
                            String strSubstring = str.substring(0, iIndexOf);
                            String strSubstring2 = str.substring(iIndexOf + 1);
                            boolean z = TextUtils.isEmpty(strSubstring) || TextUtils.equals(simOperatorNumeric, strSubstring);
                            boolean z2 = TextUtils.isEmpty(strSubstring2) || TextUtils.equals(simOperatorNumeric2, strSubstring2);
                            if (z && z2) {
                                if (ImsAdapterBase.getInstance().isCrossSimCallingEnabledByUser(this.mPreferenceFragment.getContext(), i)) {
                                    Log.d(TAG, i + " disable CrossSimCall on " + simOperatorNumeric2);
                                    ImsAdapterBase.getInstance().setCrossSimCallingEnabled(this.mPreferenceFragment.getContext(), i, false);
                                }
                                return true;
                            }
                        }
                    }
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void updateVoiceLinkUI() {
        if (MiuiPhoneUtils.isSupportVoiceLinkFeature() && !MiuiPhoneUtils.isCnOperator(MiuiPhoneUtils.getOperatorNumeric(this.mPhone)) && !TelephonyCloudController.CLOUD_VOICE_LINK_FEATURE_DISABLED && this.mPhoneId != DefaultSimManager.getDefaultDataSlotId() && !shouldHideVoiceLinkButton(this.mPhoneId)) {
            this.mPrefGroup.addPreference(this.mButtonEnableVoiceLink);
            CheckBoxPreference checkBoxPreference = this.mButtonEnableVoiceLink;
            if (checkBoxPreference != null) {
                checkBoxPreference.setChecked(((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isCrossSimCallingEnabledByUser(this.mPreferenceFragment.getContext(), this.mPhoneId));
                this.mButtonEnableVoiceLink.setEnabled(this.mButtonEnableWifiCall.isChecked());
                return;
            }
            return;
        }
        this.mPrefGroup.removePreference(this.mButtonEnableVoiceLink);
    }

    private String getWfcModeSummary(int i) {
        Resources resourcesForSubId = android.telephony.SubscriptionManager.getResourcesForSubId(PhoneGlobals.getInstance(), this.mPhone.getSubId());
        String string = resourcesForSubId.getString(R.string.wfcmode_wifi_only);
        if (i == 0) {
            return resourcesForSubId.getString(R.string.wfcmode_wifi_only);
        }
        if (i == 1) {
            return resourcesForSubId.getString(R.string.wfcmode_mobile_preferred);
        }
        if (i == 2) {
            return resourcesForSubId.getString(R.string.wfcmode_wifi_preferred);
        }
        if (i == 10) {
            return resourcesForSubId.getString(R.string.wfcmode_ims_preferred);
        }
        Log.e(TAG, "Unexpected WFC mode value: " + i);
        return string;
    }

    private boolean shouldShowWfcModeOption() {
        return !CarrierConfigManagerEx.getBooleanConfigForSubId(this.mPhone.getContext(), this.mPhone.getSubId(), "hide_wfc_mode_option", false);
    }

    private boolean shouldEnableWfcModeOption() {
        if (this.mPhone == null) {
            return true;
        }
        return (!shouldShowWfcModeOption() || isWfcSupportWifiOnly() || shouldHideWfcModeInRoaming()) ? false : true;
    }

    private boolean shouldHideWfcModeInRoaming() {
        Phone phone = this.mPhone;
        if (phone != null && phone.getServiceState().getRoaming()) {
            return CarrierConfigManagerEx.getBooleanConfigForSubId(this.mPhone.getContext(), this.mPhone.getSubId(), "hide_wfc_mode_option_in_roaming", false);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean canEditableWfcButton() {
        return this.mPhone.getState() == PhoneConstants.State.IDLE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showWfcFirstAvailableDialog() {
        ScrollView scrollView = new ScrollView(this.mPreferenceFragment.getContext());
        TextView textView = new TextView(this.mPreferenceFragment.getContext());
        textView.setAutoLinkMask(1);
        textView.setTextSize(2, 15.0f);
        Resources resourcesForSubId = android.telephony.SubscriptionManager.getResourcesForSubId(PhoneGlobals.getInstance(), this.mPhone.getSubId());
        textView.setText(resourcesForSubId.getString(R.string.wifi_call_available_message));
        int dimensionPixelSize = this.mPreferenceFragment.getResources().getDimensionPixelSize(R.dimen.appcompat_dialog_pannel_horizontal_margin);
        textView.setPadding(dimensionPixelSize, 0, dimensionPixelSize, 0);
        scrollView.addView(textView);
        new AlertDialog.Builder(this.mPreferenceFragment.getContext(), R$style.AlertDialog_Theme_DayNight).setView(scrollView).setPositiveButton(resourcesForSubId.getString(R.string.wifi_call_accept), new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.WifiCallSwitchView.7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                WifiCallSwitchView.this.updateWfcStateAndUI();
                MiuiSettings.Global.putBoolean(WifiCallSwitchView.this.mPreferenceFragment.getContext().getContentResolver(), "wifi_call_available_dialog_showed", true);
            }
        }).setNegativeButton(resourcesForSubId.getString(R.string.wifi_call_cancel), new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.WifiCallSwitchView.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                WifiCallSwitchView.this.updateWifiCallPreferredUI();
                Intent intent = new Intent(WifiCallSwitchView.this.mPreferenceFragment.getContext(), (Class<?>) WifiCallHelpActivity.class);
                intent.putExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, WifiCallSwitchView.this.mPhoneId);
                if (Build.IS_TABLET) {
                    intent.addMiuiFlags(8);
                }
                WifiCallSwitchView.this.mPreferenceFragment.getContext().startActivity(intent);
            }
        }).setButtonForceVertical(true).setCancelable(false).show();
    }

    public void destroy() {
        PreferenceFragment preferenceFragment = this.mPreferenceFragment;
        if (preferenceFragment != null) {
            preferenceFragment.getContext().getContentResolver().unregisterContentObserver(this.mContentObserver);
            this.mContentObserver = null;
        }
    }
}
