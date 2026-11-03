package com.android.phone;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.telephony.ims.ImsMmTelManager;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ProvisioningManager;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.widget.Toast;
import com.android.ims.ImsException;
import com.android.ims.ImsManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.data.DataSettingsManager;
import com.android.phone.settings.PhoneAccountSettingsFragment;
import com.android.phone.settings.SuppServicesUiUtil;
import com.android.phone.settings.VoicemailSettingsActivity;
import com.android.phone.settings.fdn.FdnSetting;
import com.mediatek.ims.internal.MtkImsManager;
import com.mediatek.settings.CallSettingUtils;
import com.mediatek.settings.SimOnOffSwitchHandler;
import com.mediatek.settings.TelephonyUtils;
import com.mediatek.settings.cdma.TelephonyUtilsEx;
import com.mediatek.telephony.MtkTelephonyManagerEx;
import com.xiaomi.internal.telephony.DynamicSarService;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class CallFeaturesSetting extends PreferenceActivity implements Preference.OnPreferenceChangeListener, PhoneGlobals.SubInfoUpdateListener {
    private SwitchPreference mButtonAutoRetry;
    private SwitchPreference mButtonPlayingToneForMoCallAccepted;
    private SwitchPreference mButtonVibratingForMoCallAccepted;
    private Preference mButtonWifiCalling;
    private SwitchPreference mEnableVideoCalling;
    private boolean mIgnoreDataChanged;
    private ImsManager mImsMgr;
    private Phone mPhone;
    private DataSettingsManager.DataSettingsManagerCallback mSettingsCallback;
    private boolean mSimOnOffEnabled;
    private SubscriptionInfoHelper mSubscriptionInfoHelper;
    private TelecomManager mTelecomManager;
    private TelephonyCallback mTelephonyCallback;
    private PreferenceScreen mVoicemailSettingsScreen;
    private boolean mVtEnabledByPlatform;
    private boolean mVtProvisionedOnDevice;
    private boolean mDisallowedConfig = false;
    private int mCallConnectedIndicator = 0;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.CallFeaturesSetting.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            CallFeaturesSetting.log("onReceive: " + intent.getAction());
            if ("android.intent.action.AIRPLANE_MODE".equals(intent.getAction())) {
                CallFeaturesSetting.log("ACTION_AIRPLANE_MODE_CHANGED");
                intent.getBooleanExtra(MEModuleSignalT1s.EXTRA_STATE, false);
                CallFeaturesSetting.this.updateScreenStatus();
            }
        }
    };
    private final ProvisioningManager.Callback mProvisioningCallback = new ProvisioningManager.Callback() { // from class: com.android.phone.CallFeaturesSetting.3
        public void onProvisioningIntChanged(int i, int i2) {
            if (i == 28 || i == 10 || i == 11) {
                CallFeaturesSetting.this.updateVtWfc();
            }
        }
    };
    private int mImsRegState = 3;
    private MyHandler mHandler = new MyHandler();
    private SimOnOffSwitchHandler mSimOnOffSwitchHandler = null;
    private final ImsMmTelManager.RegistrationCallback mImsRegistrationCallback = new ImsMmTelManager.RegistrationCallback() { // from class: com.android.phone.CallFeaturesSetting.4
        public void onRegistered(int i) {
            CallFeaturesSetting.log("onImsConnected imsRadioTech=" + i + " mImsRegState=" + CallFeaturesSetting.this.mImsRegState);
            if (CallFeaturesSetting.this.mImsRegState != 0) {
                CallFeaturesSetting.this.mImsRegState = 0;
                CallFeaturesSetting.this.mHandler.sendMessage(CallFeaturesSetting.this.mHandler.obtainMessage(0));
            }
        }

        public void onUnregistered(ImsReasonInfo imsReasonInfo) {
            CallFeaturesSetting.log("onImsDisconnected imsReasonInfo=" + imsReasonInfo + " mImsRegState=" + CallFeaturesSetting.this.mImsRegState);
            if (CallFeaturesSetting.this.mImsRegState != 1) {
                CallFeaturesSetting.this.mImsRegState = 1;
                CallFeaturesSetting.this.mHandler.sendMessage(CallFeaturesSetting.this.mHandler.obtainMessage(0));
            }
        }
    };

    @Override // android.preference.PreferenceActivity
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == this.mButtonAutoRetry) {
            Settings.Global.putInt(this.mPhone.getContext().getContentResolver(), "call_auto_retry", this.mButtonAutoRetry.isChecked() ? 1 : 0);
            return true;
        }
        SwitchPreference switchPreference = this.mButtonVibratingForMoCallAccepted;
        if (preference == switchPreference) {
            boolean zIsChecked = switchPreference.isChecked();
            int i = this.mCallConnectedIndicator;
            int i2 = zIsChecked ? i | 2 : i & (-3);
            this.mCallConnectedIndicator = i2;
            this.mTelecomManager.setCallConnectedIndicatorPreference(i2);
            return true;
        }
        SwitchPreference switchPreference2 = this.mButtonPlayingToneForMoCallAccepted;
        if (preference == switchPreference2) {
            boolean zIsChecked2 = switchPreference2.isChecked();
            int i3 = this.mCallConnectedIndicator;
            int i4 = zIsChecked2 ? i3 | 1 : i3 & (-2);
            this.mCallConnectedIndicator = i4;
            this.mTelecomManager.setCallConnectedIndicatorPreference(i4);
            return true;
        }
        if (preference == preferenceScreen.findPreference("call_forwarding_key")) {
            return doSsOverUtPrecautions(preference);
        }
        if (preference == preferenceScreen.findPreference("call_barring_key")) {
            return doSsOverUtPrecautions(preference);
        }
        return onPreferenceTreeClickMTK(preferenceScreen, preference);
    }

    private boolean doSsOverUtPrecautions(Preference preference) {
        PersistableBundle carrierConfig;
        String str;
        Phone phone;
        if (this.mSubscriptionInfoHelper.hasSubId()) {
            carrierConfig = PhoneGlobals.getInstance().getCarrierConfigForSubId(this.mSubscriptionInfoHelper.getSubId());
        } else {
            carrierConfig = PhoneGlobals.getInstance().getCarrierConfig();
        }
        if (preference.getKey().equals("call_forwarding_key")) {
            str = "call_forwarding_over_ut_warning_bool";
        } else {
            str = "call_barring_over_ut_warning_bool";
        }
        if (carrierConfig != null && carrierConfig.getBoolean(str) && (phone = this.mPhone) != null) {
            boolean zIsSsOverUtPrecautions = SuppServicesUiUtil.isSsOverUtPrecautions(this, phone);
            Phone phone2 = this.mPhone;
            if (zIsSsOverUtPrecautions) {
                SuppServicesUiUtil.showBlockingSuppServicesDialog(this, phone2, preference.getKey()).show();
                return true;
            }
            boolean zIsSsOverUtPrecautionsRoaming = SuppServicesUiUtil.isSsOverUtPrecautionsRoaming(this, phone2);
            Phone phone3 = this.mPhone;
            if (zIsSsOverUtPrecautionsRoaming) {
                CallSettingUtils.showDialogTips(this, phone3.getSubId(), CallSettingUtils.DialogType.DATA_ROAMING, preference);
                return true;
            }
            if (SuppServicesUiUtil.isSsOverUtPrecautionsExt(this, phone3)) {
                CallSettingUtils.showDialogTips(this, this.mPhone.getSubId(), CallSettingUtils.DialogType.DATA_TRAFFIC, preference);
                return true;
            }
        }
        log("No Ut Precautions for: ".concat(str));
        return false;
    }

    @Override // android.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        log("onPreferenceChange: \"" + preference + "\" changed to \"" + obj + "\"");
        if (preference != this.mEnableVideoCalling) {
            return true;
        }
        if (this.mImsMgr.isEnhanced4gLteModeSettingEnabledByUser()) {
            this.mImsMgr.setVtSetting(((Boolean) obj).booleanValue());
            return true;
        }
        FrameworksUtils.makeAlertDialogBuilder(this).setMessage(getResources().getString(R.string.enable_video_calling_dialog_msg)).setNeutralButton(getResources().getString(R.string.enable_video_calling_dialog_settings), new DialogInterface.OnClickListener() { // from class: com.android.phone.CallFeaturesSetting.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(CallFeaturesSetting.this.getString(R.string.mobile_network_settings_package), CallFeaturesSetting.this.getString(R.string.mobile_network_settings_class)));
                if (CallFeaturesSetting.this.mPhone != null) {
                    int subId = CallFeaturesSetting.this.mPhone.getSubId();
                    CallFeaturesSetting.log("start mobile network setting, subId=" + subId);
                    intent.putExtra("android.provider.extra.SUB_ID", subId);
                }
                CallFeaturesSetting.this.startActivityAsUser(intent, UserHandle.CURRENT);
            }
        }).setPositiveButton(android.R.string.ok, (DialogInterface.OnClickListener) null).show();
        return false;
    }

    @Override // android.preference.PreferenceActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        log("onCreate: Intent is " + getIntent());
        getWindow().addSystemFlags(524288);
        UserManager userManager = (UserManager) getSystemService("user");
        if (!userManager.isAdminUser()) {
            Toast.makeText(this, R.string.call_settings_admin_user_only, 0).show();
            finish();
            return;
        }
        if (userManager.hasUserRestriction("no_config_mobile_networks")) {
            this.mDisallowedConfig = true;
            Log.i("CallFeaturesSetting", "Mobile network configs are restricted, disabling mobile network settings");
        }
        SubscriptionInfoHelper subscriptionInfoHelper = new SubscriptionInfoHelper(this, getIntent());
        this.mSubscriptionInfoHelper = subscriptionInfoHelper;
        this.mPhone = subscriptionInfoHelper.getPhone();
        this.mSubscriptionInfoHelper.setActionBarTitle(getActionBar(), getResourcesForSubId(), R.string.call_settings_with_label);
        this.mTelecomManager = (TelecomManager) getSystemService(TelecomManager.class);
        this.mTelephonyCallback = new CallFeaturesTelephonyCallback();
        if (this.mPhone == null) {
            log("onCreate: mPhone is null, finish!!!");
            finish();
            return;
        }
        registerEventCallbacks();
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setHomeActionContentDescription(R.string.ota_call_end);
        }
    }

    private void updateImsManager(Phone phone) {
        log("updateImsManager :: phone.getContext()=" + phone.getContext() + " phone.getPhoneId()=" + phone.getPhoneId());
        ImsManager imsManager = ImsManager.getInstance(phone.getContext(), phone.getPhoneId());
        this.mImsMgr = imsManager;
        if (imsManager == null) {
            log("updateImsManager :: Could not get ImsManager instance!");
            return;
        }
        log("updateImsManager :: mImsMgr=" + this.mImsMgr);
    }

    private void listenPhoneState(boolean z) {
        TelephonyManager telephonyManagerCreateForSubscriptionId = ((TelephonyManager) getSystemService(TelephonyManager.class)).createForSubscriptionId(this.mPhone.getSubId());
        if (z) {
            telephonyManagerCreateForSubscriptionId.registerTelephonyCallback(new HandlerExecutor(new Handler(Looper.getMainLooper())), this.mTelephonyCallback);
        } else {
            telephonyManagerCreateForSubscriptionId.unregisterTelephonyCallback(this.mTelephonyCallback);
        }
    }

    final class CallFeaturesTelephonyCallback extends TelephonyCallback implements TelephonyCallback.CallStateListener {
        private CallFeaturesTelephonyCallback() {
        }

        @Override // android.telephony.TelephonyCallback.CallStateListener
        public void onCallStateChanged(int i) {
            CallFeaturesSetting.log("PhoneStateListener onCallStateChanged: state is " + i);
            boolean z = i == 0;
            if (CallFeaturesSetting.this.mEnableVideoCalling != null) {
                CallFeaturesSetting.this.mEnableVideoCalling.setEnabled(z);
            }
            if (CallFeaturesSetting.this.mButtonWifiCalling != null) {
                CallFeaturesSetting.this.mButtonWifiCalling.setEnabled(z);
            }
        }
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        listenPhoneState(false);
        unregisterReceiver(this.mReceiver);
        try {
            ImsManager imsManager = this.mImsMgr;
            if (imsManager != null) {
                imsManager.getConfigInterface().removeConfigCallback(this.mProvisioningCallback.getBinder());
            }
        } catch (ImsException unused) {
            Log.w("CallFeaturesSetting", "onPause: Unable to remove callback for provisioning changes");
        }
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        updateImsManager(this.mPhone);
        listenPhoneState(true);
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            preferenceScreen.removeAll();
        }
        addPreferencesFromResource(R.xml.call_feature_setting);
        TelephonyManager telephonyManagerCreateForSubscriptionId = ((TelephonyManager) getSystemService(TelephonyManager.class)).createForSubscriptionId(this.mPhone.getSubId());
        this.mButtonVibratingForMoCallAccepted = (SwitchPreference) findPreference("button_vibrating_for_outgoing_call_accepted_key");
        this.mButtonPlayingToneForMoCallAccepted = (SwitchPreference) findPreference("button_playing_tone_for_outgoing_call_accepted_key");
        if (!getResources().getBoolean(R.bool.show_call_connected_indicator_preference)) {
            findPreference("phone_account_settings_preference_screen");
            getPreferenceScreen().removePreference(this.mButtonVibratingForMoCallAccepted);
            getPreferenceScreen().removePreference(this.mButtonPlayingToneForMoCallAccepted);
        }
        this.mCallConnectedIndicator = this.mTelecomManager.getCallConnectedIndicatorPreference();
        if (telephonyManagerCreateForSubscriptionId.isMultiSimEnabled()) {
            getPreferenceScreen().removePreference(findPreference("phone_account_settings_preference_screen"));
            getPreferenceScreen().removePreference(this.mButtonVibratingForMoCallAccepted);
            getPreferenceScreen().removePreference(this.mButtonPlayingToneForMoCallAccepted);
        } else {
            this.mButtonVibratingForMoCallAccepted.setChecked((this.mCallConnectedIndicator & 2) > 0);
            this.mButtonVibratingForMoCallAccepted.setOnPreferenceChangeListener(this);
            this.mButtonPlayingToneForMoCallAccepted.setChecked((this.mCallConnectedIndicator & 1) > 0);
            this.mButtonPlayingToneForMoCallAccepted.setOnPreferenceChangeListener(this);
        }
        PreferenceScreen preferenceScreen2 = getPreferenceScreen();
        PreferenceScreen preferenceScreen3 = (PreferenceScreen) findPreference("button_voicemail_category_key");
        this.mVoicemailSettingsScreen = preferenceScreen3;
        preferenceScreen3.setIntent(this.mSubscriptionInfoHelper.getIntent(VoicemailSettingsActivity.class));
        maybeHideVoicemailSettings();
        this.mButtonAutoRetry = (SwitchPreference) findPreference("button_auto_retry_key");
        this.mEnableVideoCalling = (SwitchPreference) findPreference("button_enable_video_calling");
        this.mButtonWifiCalling = findPreference(getResourcesForSubId().getString(R.string.wifi_calling_settings_key));
        PersistableBundle carrierConfigForSubId = PhoneGlobals.getInstance().getCarrierConfigForSubId(this.mPhone.getSubId());
        boolean z = carrierConfigForSubId.getBoolean("auto_retry_enabled_bool");
        SwitchPreference switchPreference = this.mButtonAutoRetry;
        if (z) {
            switchPreference.setOnPreferenceChangeListener(this);
            this.mButtonAutoRetry.setChecked(Settings.Global.getInt(getContentResolver(), "call_auto_retry", 0) != 0);
        } else {
            preferenceScreen2.removePreference(switchPreference);
            this.mButtonAutoRetry = null;
        }
        Preference preferenceFindPreference = preferenceScreen2.findPreference("button_cdma_more_expand_key");
        Preference preferenceFindPreference2 = preferenceScreen2.findPreference("button_gsm_more_expand_key");
        Preference preferenceFindPreference3 = preferenceScreen2.findPreference("button_fdn_key");
        preferenceFindPreference3.setIntent(this.mSubscriptionInfoHelper.getIntent(FdnSetting.class));
        preferenceScreen2.removePreference(preferenceFindPreference2);
        this.mPhone.getPhoneType();
        if (carrierConfigForSubId.getBoolean("hide_carrier_network_settings_bool")) {
            preferenceScreen2.removePreference(preferenceFindPreference3);
        } else {
            preferenceScreen2.removePreference(preferenceFindPreference);
            if (this.mPhone.getIccCard() == null || !this.mPhone.getIccCard().getIccFdnAvailable()) {
                preferenceScreen2.removePreference(preferenceFindPreference3);
            }
            if (carrierConfigForSubId.getBoolean("additional_call_setting_bool")) {
                addPreferencesFromResource(R.xml.gsm_umts_call_options);
                GsmUmtsCallOptions.init(preferenceScreen2, this.mSubscriptionInfoHelper);
            }
        }
        updateVtWfc();
        try {
            ImsManager imsManager = this.mImsMgr;
            if (imsManager != null) {
                imsManager.getConfigInterface().addConfigCallback(this.mProvisioningCallback);
            }
        } catch (ImsException unused) {
            Log.w("CallFeaturesSetting", "onResume: Unable to register callback for provisioning changes.");
        }
        registerReceiver(this.mReceiver, new IntentFilter("android.intent.action.AIRPLANE_MODE"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateVtWfc() {
        int i;
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        TelephonyManager telephonyManagerCreateForSubscriptionId = ((TelephonyManager) getSystemService(TelephonyManager.class)).createForSubscriptionId(this.mPhone.getSubId());
        PersistableBundle carrierConfigForSubId = PhoneGlobals.getInstance().getCarrierConfigForSubId(this.mPhone.getSubId());
        boolean z = false;
        boolean z2 = carrierConfigForSubId.getBoolean("use_wfc_home_network_mode_in_roaming_network_bool", false);
        this.mVtEnabledByPlatform = this.mImsMgr.isVtEnabledByPlatform();
        this.mVtProvisionedOnDevice = this.mImsMgr.isVtProvisionedOnDevice();
        this.mIgnoreDataChanged = carrierConfigForSubId.getBoolean("ignore_data_enabled_changed_for_video_calls");
        boolean zIsDataEnabled = this.mPhone.getDataSettingsManager().isDataEnabled();
        log("isVtEnabledByPlatform:" + this.mVtEnabledByPlatform + "\nisVtProvisionedOnDevice:" + this.mVtProvisionedOnDevice + "\nisIgnoreDataChanged:" + this.mIgnoreDataChanged + "\nisDataEnabled:" + zIsDataEnabled);
        if (this.mVtEnabledByPlatform && this.mVtProvisionedOnDevice && ((this.mIgnoreDataChanged || zIsDataEnabled) && !this.mDisallowedConfig)) {
            if (MtkImsManager.isSupportMims() || TelephonyUtilsEx.isCapabilityPhone(this.mPhone)) {
                boolean zIsEnhanced4gLteModeSettingEnabledByUser = this.mImsMgr.isEnhanced4gLteModeSettingEnabledByUser();
                boolean zIsVtEnabledByUser = zIsEnhanced4gLteModeSettingEnabledByUser ? this.mImsMgr.isVtEnabledByUser() : false;
                log("isEnhanced4gLteModeSettingEnabledByUser:" + zIsEnhanced4gLteModeSettingEnabledByUser + "\nisVtEnabledByUser:" + zIsVtEnabledByUser);
                this.mEnableVideoCalling.setChecked(zIsVtEnabledByUser);
                this.mEnableVideoCalling.setOnPreferenceChangeListener(this);
                preferenceScreen.addPreference(this.mEnableVideoCalling);
            } else {
                preferenceScreen.removePreference(this.mEnableVideoCalling);
            }
        } else {
            preferenceScreen.removePreference(this.mEnableVideoCalling);
        }
        PhoneAccountHandle simCallManagerForSubscription = this.mTelecomManager.getSimCallManagerForSubscription(this.mPhone.getSubId());
        if (simCallManagerForSubscription != null) {
            Intent intentBuildPhoneAccountConfigureIntent = PhoneAccountSettingsFragment.buildPhoneAccountConfigureIntent(this, simCallManagerForSubscription);
            log("simCallManager is not null");
            if (intentBuildPhoneAccountConfigureIntent != null) {
                PackageManager packageManager = this.mPhone.getContext().getPackageManager();
                List<ResolveInfo> listQueryIntentActivities = packageManager.queryIntentActivities(intentBuildPhoneAccountConfigureIntent, 0);
                if (!listQueryIntentActivities.isEmpty()) {
                    log("set wfc by: " + ((Object) listQueryIntentActivities.get(0).loadLabel(packageManager)));
                    this.mButtonWifiCalling.setTitle(listQueryIntentActivities.get(0).loadLabel(packageManager));
                    this.mButtonWifiCalling.setSummary((CharSequence) null);
                    this.mButtonWifiCalling.setIntent(intentBuildPhoneAccountConfigureIntent);
                    preferenceScreen.addPreference(this.mButtonWifiCalling);
                } else {
                    log("Remove WFC Preference since resolutions is empty");
                    preferenceScreen.removePreference(this.mButtonWifiCalling);
                }
            } else {
                log("Remove WFC Preference since PhoneAccountConfigureIntent is null");
                preferenceScreen.removePreference(this.mButtonWifiCalling);
            }
        } else if (!this.mImsMgr.isWfcEnabledByPlatform() || !this.mImsMgr.isWfcProvisionedOnDevice()) {
            log("Remove WFC Preference since wfc is not enabled on the device.");
            preferenceScreen.removePreference(this.mButtonWifiCalling);
        } else {
            this.mButtonWifiCalling.setTitle(getResourcesForSubId().getString(R.string.wifi_calling));
            if (this.mImsMgr.isWfcEnabledByUser()) {
                boolean zIsNetworkRoaming = telephonyManagerCreateForSubscriptionId.isNetworkRoaming();
                ImsManager imsManager = this.mImsMgr;
                if (zIsNetworkRoaming && !z2) {
                    z = true;
                }
                int wfcMode = imsManager.getWfcMode(z);
                if (wfcMode == 0) {
                    i = 17042532;
                } else if (wfcMode == 1) {
                    i = 17042530;
                } else if (wfcMode == 2) {
                    i = 17042533;
                } else if (wfcMode == 4) {
                    i = R.string.wfc_mode_ims_preferred_title;
                } else {
                    log("Unexpected WFC mode value: " + wfcMode);
                    i = 17042564;
                }
            } else {
                i = 17042564;
            }
            this.mButtonWifiCalling.setSummary(getResourcesForSubId().getString(i));
            Intent intent = this.mButtonWifiCalling.getIntent();
            if (intent != null) {
                intent.putExtra("android.provider.extra.SUB_ID", this.mPhone.getSubId());
            }
            preferenceScreen.addPreference(this.mButtonWifiCalling);
        }
        try {
            if (this.mImsMgr.getImsServiceState() != 2) {
                log("Feature state not ready so remove vt and wfc settings for  phone =" + this.mPhone.getPhoneId());
                preferenceScreen.removePreference(this.mButtonWifiCalling);
                preferenceScreen.removePreference(this.mEnableVideoCalling);
            }
        } catch (ImsException e) {
            log("Exception when trying to get ImsServiceStatus: " + e);
            preferenceScreen.removePreference(this.mButtonWifiCalling);
            preferenceScreen.removePreference(this.mEnableVideoCalling);
        }
        updateScreenStatus();
    }

    private void maybeHideVoicemailSettings() {
        String defaultDialerPackage = ((TelecomManager) getSystemService(TelecomManager.class)).getDefaultDialerPackage();
        if (defaultDialerPackage == null) {
            return;
        }
        try {
            Bundle bundle = getPackageManager().getApplicationInfo(defaultDialerPackage, DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY).metaData;
            if (bundle == null) {
                return;
            }
            if (!bundle.getBoolean("android.telephony.HIDE_VOICEMAIL_SETTINGS_MENU", false)) {
                log("maybeHideVoicemailSettings(): not disabled by default dialer");
            } else {
                getPreferenceScreen().removePreference(this.mVoicemailSettingsScreen);
                log("maybeHideVoicemailSettings(): disabled by default dialer");
            }
        } catch (PackageManager.NameNotFoundException unused) {
            log("maybeHideVoicemailSettings(): not controlled by default dialer");
        }
    }

    @Override // android.preference.PreferenceActivity, android.app.Activity
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        SubscriptionInfoHelper subscriptionInfoHelper = new SubscriptionInfoHelper(this, getIntent());
        this.mSubscriptionInfoHelper = subscriptionInfoHelper;
        this.mPhone = subscriptionInfoHelper.getPhone();
        this.mSubscriptionInfoHelper.setActionBarTitle(getActionBar(), getResourcesForSubId(), R.string.call_settings_with_label);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("CallFeaturesSetting", str);
    }

    @Override // android.preference.PreferenceActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public static void goUpToTopLevelSetting(Activity activity, SubscriptionInfoHelper subscriptionInfoHelper) {
        Intent intent = subscriptionInfoHelper.getIntent(CallFeaturesSetting.class);
        intent.setAction("android.intent.action.MAIN");
        intent.addFlags(67108864);
        activity.startActivityAsUser(intent, UserHandle.CURRENT);
        activity.finish();
    }

    private Resources getResourcesForSubId() {
        Phone phone = this.mPhone;
        if (phone != null) {
            return SubscriptionManager.getResourcesForSubId(phone.getContext(), this.mPhone.getSubId());
        }
        return getResources();
    }

    @Override // android.preference.PreferenceActivity, android.app.ListActivity, android.app.Activity
    protected void onDestroy() {
        if (UserManager.get(this).isAdminUser()) {
            unregisterEventCallbacks();
        }
        super.onDestroy();
    }

    @Override // com.android.phone.PhoneGlobals.SubInfoUpdateListener
    public void handleSubInfoUpdate() {
        finish();
    }

    private boolean onPreferenceTreeClickMTK(PreferenceScreen preferenceScreen, Preference preference) {
        log("onPreferenceTreeClickMTK: " + preference.getKey());
        int subId = this.mPhone.getSubId();
        PersistableBundle carrierConfigForSubId = PhoneGlobals.getInstance().getCarrierConfigForSubId(this.mPhone.getSubId());
        if (preference != preferenceScreen.findPreference("additional_gsm_call_settings_key")) {
            return false;
        }
        boolean z = carrierConfigForSubId.getBoolean("caller_id_over_ut_warning_bool");
        boolean z2 = carrierConfigForSubId.getBoolean("call_waiting_over_ut_warning_bool");
        if (!z && !z2) {
            return false;
        }
        if (SuppServicesUiUtil.isSsOverUtPrecautionsRoaming(this, this.mPhone)) {
            CallSettingUtils.showDialogTips(this, subId, CallSettingUtils.DialogType.DATA_ROAMING, preference);
            return true;
        }
        if (!SuppServicesUiUtil.isSsOverUtPrecautionsExt(this, this.mPhone)) {
            return false;
        }
        CallSettingUtils.showDialogTips(this, this.mPhone.getSubId(), CallSettingUtils.DialogType.DATA_TRAFFIC, preference);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateScreenStatus() {
        boolean z;
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen == null) {
            log("updateScreenStatus, screen is null");
            return;
        }
        int subId = this.mPhone.getSubId();
        boolean zIsValidSubscriptionId = SubscriptionManager.isValidSubscriptionId(subId);
        boolean zIsAirplaneModeOn = TelephonyUtils.isAirplaneModeOn(PhoneGlobals.getInstance());
        log("updateScreenStatus, isAirplaneMode=" + zIsAirplaneModeOn + ", hasSubId=" + zIsValidSubscriptionId);
        boolean zIsImsServiceAvailable = false;
        if (this.mSimOnOffEnabled) {
            int slotIndex = SubscriptionManager.getSlotIndex(subId);
            z = MtkTelephonyManagerEx.getDefault().getSimOnOffState(slotIndex) == 11;
            log("updateScreenStatus, slotId=" + slotIndex + ", isSimOn=" + z);
        } else {
            z = true;
        }
        for (int i = 0; i < preferenceScreen.getPreferenceCount(); i++) {
            preferenceScreen.getPreference(i).setEnabled(!zIsAirplaneModeOn && zIsValidSubscriptionId && z);
        }
        if (zIsValidSubscriptionId) {
            int i2 = this.mImsRegState;
            if (i2 == 3) {
                zIsImsServiceAvailable = TelephonyUtils.isImsServiceAvailable(this, subId);
            } else if (i2 == 0) {
                zIsImsServiceAvailable = true;
            }
            if (zIsImsServiceAvailable && (MtkImsManager.isSupportMims() || TelephonyUtilsEx.isCapabilityPhone(this.mPhone))) {
                Preference preferenceFindPreference = getPreferenceScreen().findPreference("call_forwarding_key");
                Preference preferenceFindPreference2 = getPreferenceScreen().findPreference("call_barring_key");
                Preference preferenceFindPreference3 = getPreferenceScreen().findPreference("additional_gsm_call_settings_key");
                if (preferenceFindPreference != null) {
                    log("set SS item enabled when IMS is registered.");
                    preferenceFindPreference.setEnabled(true);
                }
                if (preferenceFindPreference2 != null) {
                    preferenceFindPreference2.setEnabled(true);
                }
                if (preferenceFindPreference3 != null) {
                    preferenceFindPreference3.setEnabled(true);
                }
                Preference preferenceFindPreference4 = getPreferenceScreen().findPreference("button_fdn_key");
                if (preferenceFindPreference4 != null) {
                    log("set FDN item enabled when VOWIFI is registered under airplane mode.");
                    preferenceFindPreference4.setEnabled(true);
                }
            }
        }
        updateOptionsByCallState(subId);
    }

    private void registerEventCallbacks() {
        PhoneGlobals.getInstance().addSubInfoUpdateListener(this);
        try {
            ImsManager imsManager = ImsManager.getInstance(this.mPhone.getContext(), this.mPhone.getPhoneId());
            if (imsManager != null) {
                ImsMmTelManager.RegistrationCallback registrationCallback = this.mImsRegistrationCallback;
                final MyHandler myHandler = this.mHandler;
                Objects.requireNonNull(myHandler);
                imsManager.addRegistrationCallback(registrationCallback, new Executor() { // from class: com.android.phone.CallFeaturesSetting$$ExternalSyntheticLambda0
                    @Override // java.util.concurrent.Executor
                    public final void execute(Runnable runnable) {
                        myHandler.post(runnable);
                    }
                });
                log("addRegistrationCallback:" + this.mImsRegistrationCallback);
            }
        } catch (ImsException e) {
            log("ImsException:" + e);
        }
        final MyHandler myHandler2 = this.mHandler;
        Objects.requireNonNull(myHandler2);
        this.mSettingsCallback = new DataSettingsManager.DataSettingsManagerCallback(new Executor() { // from class: com.android.phone.CallFeaturesSetting$$ExternalSyntheticLambda0
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                myHandler2.post(runnable);
            }
        }) { // from class: com.android.phone.CallFeaturesSetting.5
            public void onDataEnabledChanged(boolean z, int i, String str) {
                CallFeaturesSetting.log("onDataEnabledChanged: enabled=" + z + ", reason=" + i + ", callingPackage=" + str);
                if (i == 0) {
                    CallFeaturesSetting.this.mHandler.handleDataEnabledChanged(z);
                }
            }
        };
        this.mPhone.getDefaultPhone().getDataSettingsManager().registerCallback(this.mSettingsCallback);
        boolean zIsSimOnOffEnabled = MtkTelephonyManagerEx.getDefault().isSimOnOffEnabled();
        this.mSimOnOffEnabled = zIsSimOnOffEnabled;
        if (zIsSimOnOffEnabled) {
            SimOnOffSwitchHandler simOnOffSwitchHandler = new SimOnOffSwitchHandler(this, this.mPhone.getPhoneId());
            this.mSimOnOffSwitchHandler = simOnOffSwitchHandler;
            simOnOffSwitchHandler.registerOnSimOnOffSwitch(new SimOnOffSwitchHandler.OnSimOnOffSwitchListener() { // from class: com.android.phone.CallFeaturesSetting.6
                @Override // com.mediatek.settings.SimOnOffSwitchHandler.OnSimOnOffSwitchListener
                public void onSimOnOffStateChanged() {
                    CallFeaturesSetting.log("onSimOnOffStateChanged, finish activity.");
                    CallFeaturesSetting.this.finish();
                }
            });
        }
    }

    private void unregisterEventCallbacks() {
        PhoneGlobals.getInstance().removeSubInfoUpdateListener(this);
        ImsManager imsManager = ImsManager.getInstance(this.mPhone.getContext(), this.mPhone.getPhoneId());
        if (imsManager != null) {
            imsManager.removeRegistrationListener(this.mImsRegistrationCallback);
            log("removeRegistrationListener:" + this.mImsRegistrationCallback);
        }
        this.mPhone.getDefaultPhone().getDataSettingsManager().unregisterCallback(this.mSettingsCallback);
        SimOnOffSwitchHandler simOnOffSwitchHandler = this.mSimOnOffSwitchHandler;
        if (simOnOffSwitchHandler != null) {
            simOnOffSwitchHandler.unregisterOnSimOnOffSwitch();
            this.mSimOnOffSwitchHandler = null;
        }
    }

    class MyHandler extends Handler {
        private MyHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                CallFeaturesSetting.this.updateScreenStatus();
            } else {
                if (i != 1) {
                    return;
                }
                Object obj = ((AsyncResult) message.obj).result;
                if (obj instanceof Pair) {
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleDataEnabledChanged(boolean z) {
            CallFeaturesSetting.log("handleDataEnabledChanged: enabled=" + z);
            if (CallFeaturesSetting.this.mEnableVideoCalling != null) {
                PreferenceScreen preferenceScreen = CallFeaturesSetting.this.getPreferenceScreen();
                if (CallFeaturesSetting.this.mVtEnabledByPlatform && CallFeaturesSetting.this.mVtProvisionedOnDevice && (CallFeaturesSetting.this.mIgnoreDataChanged || z)) {
                    CallFeaturesSetting.log("handleDataEnabledChanged: addPreference");
                    preferenceScreen.addPreference(CallFeaturesSetting.this.mEnableVideoCalling);
                } else {
                    CallFeaturesSetting.log("handleDataEnabledChanged: removePreference");
                    preferenceScreen.removePreference(CallFeaturesSetting.this.mEnableVideoCalling);
                }
            }
        }
    }

    private void updateOptionsByCallState(int i) {
        if (this.mEnableVideoCalling != null) {
            boolean zIsInCall = TelephonyUtils.isInCall(this, i);
            this.mEnableVideoCalling.setEnabled(!zIsInCall);
            this.mButtonWifiCalling.setEnabled(!zIsInCall);
        }
    }
}
