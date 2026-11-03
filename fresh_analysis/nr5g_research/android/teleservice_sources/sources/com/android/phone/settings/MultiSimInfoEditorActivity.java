package com.android.phone.settings;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.Rlog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.UiccController;
import com.android.phone.FiveGManagerBase;
import com.android.phone.MiuiImsPhoneUtils;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.phone.SingleFiveGController;
import com.android.phone.TelephonyCapabilities;
import com.android.phone.custompreferences.CustomEditTextPreference;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.utils.FiveGUtils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.Utils;
import com.miui.enterprise.PhoneHelper;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import miui.enterprise.PhoneHelperStub;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;
import miui.telephony.SubscriptionManagerEx;
import miui.telephony.TelephonyManager;
import miui.telephony.VirtualSimUtils;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.AppCompatActivity;
import miuix.appcompat.app.ProgressDialog;
import miuix.appcompat.app.floatingactivity.multiapp.MultiAppFloatingActivitySwitcher;

/* JADX INFO: loaded from: classes.dex */
public class MultiSimInfoEditorActivity extends BaseActivity {
    private static int FIVEG_CA_OFF = 0;
    private static int FIVEG_CA_ON = 1;
    private static WeakReference sInstanceRef;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        sInstanceRef = new WeakReference(this);
        if (bundle == null) {
            loadFragment(new MultiSimInfoEditorFragment());
        }
    }

    @Override // com.android.phone.settings.BaseActivity
    protected void setActionBar() {
        super.setActionBar();
        getAppCompatActionBar().setTitle(R.string.msim_setting_categery_title);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        WeakReference weakReference = sInstanceRef;
        if (weakReference == null || weakReference.get() != this) {
            return;
        }
        sInstanceRef.clear();
    }

    public static MultiSimInfoEditorActivity getCurrentInstance() {
        WeakReference weakReference = sInstanceRef;
        if (weakReference != null) {
            return (MultiSimInfoEditorActivity) weakReference.get();
        }
        return null;
    }

    public class MultiSimInfoEditorFragment extends BaseActivity.BaseFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener, TextWatcher, SubscriptionManager.OnSubscriptionsChangedListener {
        private CheckBoxPreference mBtn5GCarrierAggregation;
        private Preference mButtonCarrierSel;
        private Preference mButtonConvertToEsim;
        private Preference mButtonHideStk;
        private CheckBoxPreference mButtonSaEnabled;
        private Preference mButtonStk;
        private CheckBoxPreference mButtonViceSaEnabled;
        private CdmaOptions mCdmaOptions;
        private Preference mClickedPreference;
        private AlertDialog mDisableSimDialog;
        private CheckBoxPreference mEnableSim;
        private GsmUmtsOptions mGsmUmtsOptions;
        private PreferenceCategory mMobileNetworkCategory;
        private String mNotSet;
        private Phone mPhone;
        private PreferenceScreen mPreferenceScreen;
        private ProgressDialog mProgressDialog;
        private SubscriptionInfo mSimInfoRecord;
        private CustomEditTextPreference mSimName;
        private LTREditTextPreference mSimNumber;
        private int mSlotId;
        public PreferenceCategory mWifiCallCategory;
        private WifiCallSwitchView mWifiCallSwitchView;
        private boolean mCommonOptionsEnabled = true;
        private boolean mIsForeground = false;
        private boolean mIsReceiverRegistered = false;
        private int mSubId = SubscriptionManager.INVALID_SUBSCRIPTION_ID;
        private boolean mIsDisableLteDevice = TelephonyManager.getDefault().isDisableLte(false);
        private Handler mHandler = new Handler() { // from class: com.android.phone.settings.MultiSimInfoEditorActivity.MultiSimInfoEditorFragment.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i = message.what;
                if (i == 0) {
                    MultiSimInfoEditorFragment multiSimInfoEditorFragment = MultiSimInfoEditorFragment.this;
                    multiSimInfoEditorFragment.switchRadioState(multiSimInfoEditorFragment.mSlotId, MultiSimInfoEditorFragment.this.mEnableSim.isChecked());
                    return;
                }
                if (i == 1) {
                    MultiSimInfoEditorFragment.this.mEnableSim.setChecked(true);
                    return;
                }
                if (i == 2) {
                    MultiSimInfoEditorFragment.this.updateNetworkTypeAvailable();
                    return;
                }
                if (i != 3) {
                    return;
                }
                MultiSimInfoEditorFragment multiSimInfoEditorFragment2 = MultiSimInfoEditorFragment.this;
                multiSimInfoEditorFragment2.updateInfoSimNumber(multiSimInfoEditorFragment2.mSimInfoRecord);
                if (MultiSimInfoEditorFragment.this.mSimNumber != null) {
                    MultiSimInfoEditorFragment.this.mSimNumber.refreshRightValue();
                }
            }
        };
        private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.settings.MultiSimInfoEditorActivity.MultiSimInfoEditorFragment.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Rlog.i("MultiSimInfoEditorActivity", "onReceive action" + action);
                if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                    MultiSimInfoEditorFragment.this.updateCheckState();
                    return;
                }
                if ("miui.intent.action.OMACP_VOWIFI_ENABLED".equals(action)) {
                    int i = SubscriptionManager.INVALID_SUBSCRIPTION_ID;
                    int intExtra = intent.getIntExtra("subscription", i);
                    if (intExtra == i || intExtra != MultiSimInfoEditorFragment.this.mSubId) {
                        Rlog.d("MultiSimInfoEditorActivity", "ACTION_OMACP_VOWIFI_ENABLED sub is invalid or unequal return!!!");
                        return;
                    }
                    boolean booleanExtra = intent.getBooleanExtra("vowifi_enabled", false);
                    MiuiImsPhoneUtils.saveOmacpWifiState(intExtra, booleanExtra);
                    MultiSimInfoEditorFragment.this.updateWifiCallSwitchView();
                    Rlog.d("MultiSimInfoEditorActivity", "ACTION_OMACP_VOWIFI_ENABLED subId:" + intExtra + " enabled:" + booleanExtra);
                    return;
                }
                if ("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED".equals(action)) {
                    if (MultiSimInfoEditorFragment.this.mDisableSimDialog == null || MultiSimInfoEditorFragment.this.getActivity() == null) {
                        return;
                    }
                    Rlog.d("MultiSimInfoEditorActivity", "default data slot changed  dismiss mDisableSimDialog");
                    MultiSimInfoEditorFragment.this.mDisableSimDialog.dismiss();
                    MultiSimInfoEditorFragment.this.mDisableSimDialog = null;
                    MultiSimInfoEditorFragment.this.updateCheckState();
                    return;
                }
                if ("android.telephony.action.CARRIER_CONFIG_CHANGED".equals(action)) {
                    int intExtra2 = intent.getIntExtra("phone", SubscriptionManager.INVALID_PHONE_ID);
                    Rlog.i("MultiSimInfoEditorActivity", "ACTION_CARRIER_CONFIG_CHANGED phoneId: " + intExtra2);
                    if (SubscriptionManager.isValidPhoneId(intExtra2) && intExtra2 == MultiSimInfoEditorFragment.this.mPhone.getPhoneId()) {
                        MultiSimInfoEditorFragment.this.updateCheckState();
                        return;
                    }
                    return;
                }
                if (action.equals("android.net.wifi.WIFI_AP_STATE_CHANGED")) {
                    int intExtra3 = intent.getIntExtra("wifi_state", 0);
                    if (intExtra3 == 11 || intExtra3 == 13) {
                        MultiSimInfoEditorFragment.this.updateWifiCallSwitchView();
                    }
                }
            }
        };
        private Runnable mDismissRunner = new Runnable() { // from class: com.android.phone.settings.MultiSimInfoEditorActivity.MultiSimInfoEditorFragment.7
            @Override // java.lang.Runnable
            public void run() {
                MultiSimInfoEditorFragment.this.dismissProgressDialog();
            }
        };

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            int subscriptionId;
            addPreferencesFromResource(R.xml.msim_sim_info_editor);
            Intent intent = getActivity().getIntent();
            Bundle extras = intent != null ? intent.getExtras() : null;
            if (extras != null) {
                int i = SubscriptionManager.INVALID_SLOT_ID;
                int slotId = SubscriptionManager.getSlotId(extras, i);
                this.mSlotId = slotId;
                if (slotId == i) {
                    this.mSlotId = Integer.parseInt(extras.getString(SubscriptionManager.SLOT_KEY, "-1"));
                }
            }
            SubscriptionInfo availableSubscriptionInfoBySlot = MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(this.mSlotId);
            this.mSimInfoRecord = availableSubscriptionInfoBySlot;
            if (availableSubscriptionInfoBySlot == null) {
                subscriptionId = SubscriptionManager.INVALID_SUBSCRIPTION_ID;
            } else {
                subscriptionId = availableSubscriptionInfoBySlot.getSubscriptionId();
            }
            this.mSubId = subscriptionId;
            Rlog.d("MultiSimInfoEditorActivity", "onCreatePreferences: " + this.mSlotId + " mSubId " + this.mSubId);
            if (!SubscriptionManager.isValidSubscriptionId(this.mSubId) && getActivity() != null) {
                getActivity().finish();
                return;
            }
            initMobileNetworkSetting();
            this.mIsReceiverRegistered = true;
            IntentFilter intentFilter = new IntentFilter("android.intent.action.AIRPLANE_MODE");
            intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
            if (MiuiImsPhoneUtils.isCustForEsVodafone()) {
                intentFilter.addAction("miui.intent.action.OMACP_VOWIFI_ENABLED");
            }
            if (MiuiImsPhoneUtils.disableWfcWhenEnableWifiAp(this.mSubId)) {
                intentFilter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
            }
            intentFilter.addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
            getContext().registerReceiver(this.mReceiver, intentFilter, 2);
            MiStatInterfaceUtil.PhoneSettings.recordSlotIdInMultiSimInfoEditor("key_multisim_info_editor_preference", this.mSlotId);
        }

        public void initMobileNetworkSetting() {
            Preference preferenceFindPreference;
            Preference preferenceFindPreference2;
            this.mPhone = MiuiPhoneUtils.getPhone(this.mSlotId);
            this.mNotSet = getResources().getString(R.string.msim_apn_not_set);
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            this.mPreferenceScreen = preferenceScreen;
            this.mMobileNetworkCategory = (PreferenceCategory) preferenceScreen.findPreference("msim_mobile_network_setting");
            CheckBoxPreference checkBoxPreference = (CheckBoxPreference) this.mPreferenceScreen.findPreference("button_5g_sa_network_key");
            this.mButtonSaEnabled = checkBoxPreference;
            checkBoxPreference.setOnPreferenceClickListener(this);
            this.mBtn5GCarrierAggregation = (CheckBoxPreference) this.mPreferenceScreen.findPreference("button_5G_carrier_aggregation");
            Preference preferenceFindPreference3 = findPreference("sim_name_number_category_key");
            boolean zIsVirtualSim = MiuiPhoneUtils.isVirtualSim(this.mSlotId);
            if (preferenceFindPreference3 != null) {
                if (zIsVirtualSim) {
                    this.mPreferenceScreen.removePreference(preferenceFindPreference3);
                    this.mSimName = null;
                    this.mSimNumber = null;
                } else {
                    this.mSimName = (CustomEditTextPreference) findPreference("sim_name");
                    this.mSimNumber = (LTREditTextPreference) findPreference("sim_number");
                    this.mSimName.setOnPreferenceChangeListener(this);
                    this.mSimName.setOnPreferenceClickListener(this);
                    this.mSimNumber.setOnPreferenceChangeListener(this);
                    this.mSimNumber.setOnPreferenceClickListener(this);
                    this.mSimName.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.android.phone.settings.MultiSimInfoEditorActivity.MultiSimInfoEditorFragment.3
                        @Override // android.content.DialogInterface.OnShowListener
                        public void onShow(DialogInterface dialogInterface) {
                            MultiSimInfoEditorFragment.this.mSimName.addTextChangeListenner(MultiSimInfoEditorFragment.this);
                        }
                    });
                }
            }
            this.mEnableSim = (CheckBoxPreference) findPreference("enable_sim");
            if (zIsVirtualSim || (MiuiPhoneUtils.getIccCardCountExcludeSlot(this.mSlotId) < 1 && this.mSimInfoRecord.isActivated())) {
                this.mPreferenceScreen.removePreference(this.mEnableSim);
                this.mEnableSim = null;
            } else {
                this.mEnableSim.setOnPreferenceClickListener(this);
            }
            if (this.mEnableSim != null && this.mSimInfoRecord.isEmbedded()) {
                this.mPreferenceScreen.removePreference(this.mEnableSim);
                this.mEnableSim = null;
            }
            if (this.mEnableSim != null && (PhoneHelper.isIccCardDisable() || PhoneHelperStub.getInstance().isIccCardDisable())) {
                Log.w("MultiSimInfoEditorActivity", "Device is in enterprise mode, enable sim is restricted by enterprise!");
                this.mPreferenceScreen.removePreference(this.mEnableSim);
                this.mEnableSim = null;
            }
            Preference preferenceFindPreference4 = findPreference("button_go_to_virtual_sim_key");
            if (zIsVirtualSim) {
                String virtualSimCarrierName = MiuiPhoneUtils.getVirtualSimCarrierName();
                if (!TextUtils.isEmpty(virtualSimCarrierName)) {
                    preferenceFindPreference4.setTitle(virtualSimCarrierName);
                } else {
                    Log.e("MultiSimInfoEditorActivity", "error: update Virtual Sim Title failed");
                }
            } else {
                this.mPreferenceScreen.removePreference(preferenceFindPreference4);
            }
            Preference preferenceFindPreference5 = findPreference("button_go_to_esim_manager");
            if (MiuiPhoneUtils.isSupportGoogleLPAEsim()) {
                boolean zIsEsimActive = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isEsimActive();
                boolean zIsSupportEsim = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isSupportEsim();
                Rlog.i("MultiSimInfoEditorActivity", "isActived = " + zIsEsimActive + ",isSupportEsim=" + zIsSupportEsim);
                if (!zIsSupportEsim || !zIsEsimActive) {
                    this.mMobileNetworkCategory.removePreference(preferenceFindPreference5);
                }
            } else {
                this.mMobileNetworkCategory.removePreference(preferenceFindPreference5);
            }
            this.mButtonCarrierSel = findPreference("button_carrier_sel_key");
            this.mButtonConvertToEsim = findPreference("button_convert_to_esim");
            updateButtonConvertToEsim();
            int phoneType = this.mPhone.getPhoneType();
            if (phoneType == 2) {
                this.mCdmaOptions = new CdmaOptions(this, this.mPreferenceScreen, this.mMobileNetworkCategory, this.mPhone);
            } else if (phoneType == 1) {
                this.mGsmUmtsOptions = new GsmUmtsOptions(this, this.mPreferenceScreen, this.mMobileNetworkCategory, this.mPhone);
            } else {
                CommonOptions$$ExternalSyntheticBUOutline0.m("Unexpected phone type: ", phoneType);
                return;
            }
            if (DefaultSimManager.getDefaultDataSlotId() != this.mSlotId) {
                Preference preferenceFindPreference6 = this.mPreferenceScreen.findPreference("button_apn_key");
                if (preferenceFindPreference6 != null) {
                    this.mMobileNetworkCategory.removePreference(preferenceFindPreference6);
                }
                if (!((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).shouldShowViceNetworkTypePref() && (preferenceFindPreference2 = this.mPreferenceScreen.findPreference("button_preferred_network_type_key")) != null) {
                    this.mMobileNetworkCategory.removePreference(preferenceFindPreference2);
                }
            }
            UiccController.getInstance().registerForIccChanged(this.mHandler, 2, (Object) null);
            updateNetworkTypeAvailable();
            if (!getResources().getBoolean(R.bool.config_carrier_settings_enable) && (preferenceFindPreference = this.mPreferenceScreen.findPreference("carrier_settings_key")) != null) {
                this.mPreferenceScreen.removePreference(preferenceFindPreference);
                Preference preferenceFindPreference7 = this.mPreferenceScreen.findPreference("carrier_settings_key");
                if (preferenceFindPreference7 != null) {
                    this.mPreferenceScreen.removePreference(preferenceFindPreference7);
                }
            }
            Preference preferenceFindPreference8 = this.mPreferenceScreen.findPreference("button_uplmn_key");
            SubscriptionManager.putSlotIdExtra(preferenceFindPreference8.getIntent(), this.mSlotId);
            if (!TelephonyCapabilities.supportShowPLMNPreference()) {
                this.mMobileNetworkCategory.removePreference(preferenceFindPreference8);
            }
            this.mButtonHideStk = this.mPreferenceScreen.findPreference("button_hide_stk_key");
            this.mButtonStk = this.mPreferenceScreen.findPreference("button_stk_key");
            boolean zIsHideStkIcon = MiuiPhoneUtils.isHideStkIcon();
            PreferenceScreen preferenceScreen2 = this.mPreferenceScreen;
            if (zIsHideStkIcon) {
                if (preferenceScreen2.findPreference("button_stk_key") != null) {
                    this.mMobileNetworkCategory.removePreference(this.mButtonStk);
                }
                putStkSlotExtra(this.mButtonHideStk);
            } else if (preferenceScreen2.findPreference("button_hide_stk_key") != null) {
                this.mMobileNetworkCategory.removePreference(this.mButtonHideStk);
            }
            addPreferencesFromResource(R.xml.wificall_switch_per_slot);
            this.mWifiCallCategory = (PreferenceCategory) this.mPreferenceScreen.findPreference("wifi_call_category_key");
            this.mWifiCallCategory.setTitle(android.telephony.SubscriptionManager.getResourcesForSubId(PhoneGlobals.getInstance(), this.mSubId).getString(R.string.wifi_call_setting));
            CheckBoxPreference checkBoxPreference2 = (CheckBoxPreference) findPreference("button_vice_sim_5g_sa_network");
            this.mButtonViceSaEnabled = checkBoxPreference2;
            checkBoxPreference2.setOnPreferenceClickListener(this);
        }

        private void putStkSlotExtra(Preference preference) {
            if (preference == null || preference.getIntent() == null) {
                return;
            }
            Rlog.i("MultiSimInfoEditorActivity", "putStkSlotExtra mSlotId = " + this.mSlotId);
            SubscriptionManager.putSlotIdExtra(preference.getIntent(), this.mSlotId);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateWifiCallSwitchView() {
            if (this.mPreferenceScreen == null || this.mWifiCallCategory == null) {
                return;
            }
            if (!MiuiImsPhoneUtils.showWifiCallSwitchView(this.mPhone) || !MiuiPhoneUtils.isIccCardActivated(this.mPhone.getPhoneId())) {
                this.mPreferenceScreen.removePreference(this.mWifiCallCategory);
                return;
            }
            this.mPreferenceScreen.addPreference(this.mWifiCallCategory);
            if (this.mWifiCallSwitchView == null) {
                this.mWifiCallSwitchView = new WifiCallSwitchView(this, this.mWifiCallCategory, this.mPhone);
            }
            this.mWifiCallSwitchView.updateWifiCallPreferredUI();
        }

        @Override // androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            if (this.mButtonSaEnabled != null && FiveGUtils.shouldShow5GSaSwitch()) {
                this.mButtonSaEnabled.setOnPreferenceChangeListener(this);
            }
            SubscriptionManager.getDefault().addOnSubscriptionsChangedListener(this);
            this.mIsForeground = true;
            if (this.mDisableSimDialog != null && getActivity() != null) {
                Rlog.d("MultiSimInfoEditorActivity", "onResume mDisableSimDialog dismiss");
                this.mDisableSimDialog.dismiss();
                this.mDisableSimDialog = null;
            }
            updateInfo();
            updateCheckState();
            update5gNetworkMode();
            update5gCarrierAggregation();
            updateMobileNetworkCategory();
        }

        @Override // miuix.preference.PreferenceFragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
        public void onConfigurationChanged(Configuration configuration) {
            super.onConfigurationChanged(configuration);
            this.mHandler.removeMessages(3);
            this.mHandler.sendEmptyMessageDelayed(3, 100L);
        }

        public void onSubscriptionsChanged() {
            SubscriptionInfo availableSubscriptionInfoBySlot = MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(this.mSlotId);
            this.mSimInfoRecord = availableSubscriptionInfoBySlot;
            if (this.mIsDisableLteDevice) {
                updateNetworkTypeAvailable();
            }
            if (getActivity() == null) {
                return;
            }
            if (availableSubscriptionInfoBySlot != null) {
                if (availableSubscriptionInfoBySlot.getSlotId() != this.mSlotId || availableSubscriptionInfoBySlot.getSubscriptionId() != this.mSubId) {
                    getActivity().finish();
                }
                updateCheckState();
                updateInfoSimNumber(availableSubscriptionInfoBySlot);
                updateButtonConvertToEsim();
                return;
            }
            getActivity().finish();
        }

        private void update5GCarrierAggregationEnabled(boolean z) {
            CheckBoxPreference checkBoxPreference = this.mBtn5GCarrierAggregation;
            if (checkBoxPreference != null) {
                checkBoxPreference.setEnabled(z);
            }
        }

        private void update5gCarrierAggregation() {
            if (MiuiPhoneUtils.showFiveGCarrierAggregation()) {
                if (this.mBtn5GCarrierAggregation != null) {
                    int fiveGCarrierAggregationFromDb = MiuiPhoneUtils.getFiveGCarrierAggregationFromDb(getContext());
                    Rlog.d("MultiSimInfoEditorActivity", "update5gCarrierAggregation value from DB =" + fiveGCarrierAggregationFromDb);
                    this.mBtn5GCarrierAggregation.setChecked(fiveGCarrierAggregationFromDb == MultiSimInfoEditorActivity.FIVEG_CA_ON);
                    return;
                }
                return;
            }
            this.mMobileNetworkCategory.removePreference(this.mBtn5GCarrierAggregation);
        }

        private void updateMobileNetworkCategory() {
            if (this.mMobileNetworkCategory.getPreferenceCount() == 0) {
                this.mMobileNetworkCategory.setTitle((CharSequence) null);
                this.mPreferenceScreen.removePreference(this.mMobileNetworkCategory);
            }
        }

        private void update5gNetworkMode() {
            if (MiuiPhoneUtils.isFiveGCapable()) {
                boolean zIsUserFiveGEnabled = FiveGManagerBase.getInstance().isUserFiveGEnabled();
                if (this.mButtonSaEnabled != null) {
                    int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
                    Rlog.i("MultiSimInfoEditorActivity", "DefaultSimManager.getDefaultDataSlotId() =" + defaultDataSlotId);
                    if (isMVNOForESOrange(defaultDataSlotId)) {
                        this.mButtonSaEnabled.setTitle(getResources().getString(R.string.fiveg_plus_switch_title));
                    }
                    if (zIsUserFiveGEnabled && FiveGUtils.shouldShow5GSaSwitch() && DefaultSimManager.getDefaultDataSlotId() == this.mSlotId) {
                        if (this.mPreferenceScreen.findPreference("button_5g_sa_network_key") == null) {
                            this.mMobileNetworkCategory.addPreference(this.mButtonSaEnabled);
                        }
                        this.mButtonSaEnabled.setChecked(FiveGManagerBase.getInstance().isUserFiveGSaEnabled());
                        return;
                    }
                    this.mMobileNetworkCategory.removePreference(this.mButtonSaEnabled);
                    return;
                }
                return;
            }
            CheckBoxPreference checkBoxPreference = this.mButtonSaEnabled;
            if (checkBoxPreference != null) {
                this.mMobileNetworkCategory.removePreference(checkBoxPreference);
            }
        }

        private void updateInfo() {
            SubscriptionInfo availableSubscriptionInfoBySlot = MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(this.mSlotId);
            this.mSimInfoRecord = availableSubscriptionInfoBySlot;
            if (availableSubscriptionInfoBySlot != null) {
                if (this.mSimName == null || this.mSimNumber == null) {
                    return;
                }
                CharSequence displayName = availableSubscriptionInfoBySlot.getDisplayName();
                CustomEditTextPreference customEditTextPreference = this.mSimName;
                if (displayName == null) {
                    customEditTextPreference.setRightValue(this.mNotSet);
                    this.mSimName.setText("");
                } else {
                    customEditTextPreference.setRightValue(availableSubscriptionInfoBySlot.getDisplayName().toString());
                    this.mSimName.setText(availableSubscriptionInfoBySlot.getDisplayName().toString());
                }
                updateInfoSimNumber(availableSubscriptionInfoBySlot);
                return;
            }
            if (getActivity() != null) {
                getActivity().finish();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateInfoSimNumber(SubscriptionInfo subscriptionInfo) {
            if (subscriptionInfo == null || this.mSimNumber == null) {
                return;
            }
            if (subscriptionInfo.getDisplayNumber() != null && subscriptionInfo.getDisplayNumber().length() != 0) {
                this.mSimNumber.setRightValue(Utils.localizeNumber(subscriptionInfo.getDisplayNumber()));
                this.mSimNumber.setText(Utils.localizeNumber(subscriptionInfo.getDisplayNumber()));
            } else {
                this.mSimNumber.setRightValue(this.mNotSet);
                this.mSimNumber.setText("");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateCheckState() {
            CheckBoxPreference checkBoxPreference;
            boolean zIsCustForMxTelcel;
            CheckBoxPreference checkBoxPreference2;
            boolean zIsIccCardActivated = MiuiPhoneUtils.isIccCardActivated(this.mSlotId);
            AlertDialog alertDialog = this.mDisableSimDialog;
            if ((alertDialog == null || !alertDialog.isShowing()) && this.mProgressDialog == null && (checkBoxPreference = this.mEnableSim) != null) {
                checkBoxPreference.setChecked(zIsIccCardActivated);
            }
            if (!Build.IS_CM_CUSTOMIZATION_TEST) {
                setEnabledStateOnPreferences(this.mPreferenceScreen, zIsIccCardActivated);
            }
            if (getContext() == null) {
                return;
            }
            boolean z = false;
            boolean z2 = Settings.Global.getInt(getContext().getContentResolver(), "airplane_mode_on", 0) == 0;
            boolean z3 = zIsIccCardActivated && z2;
            this.mCommonOptionsEnabled = z3;
            CdmaOptions cdmaOptions = this.mCdmaOptions;
            if (cdmaOptions != null) {
                cdmaOptions.setOptionEnabled(z3);
            }
            GsmUmtsOptions gsmUmtsOptions = this.mGsmUmtsOptions;
            if (gsmUmtsOptions != null) {
                gsmUmtsOptions.setOptionEnabled(this.mCommonOptionsEnabled);
            }
            if (TelephonyManager.isCustFeatureResolveEnabled()) {
                zIsCustForMxTelcel = HyperOSCustFeatureResolve.getBoolean("disable_sim_name_number", false);
            } else {
                zIsCustForMxTelcel = TelephonyManager.isCustForMxTelcel();
            }
            CustomEditTextPreference customEditTextPreference = this.mSimName;
            if (customEditTextPreference != null) {
                customEditTextPreference.setEnabled(zIsCustForMxTelcel ? false : z2);
            }
            LTREditTextPreference lTREditTextPreference = this.mSimNumber;
            if (lTREditTextPreference != null) {
                lTREditTextPreference.setEnabled(zIsCustForMxTelcel ? false : z2);
            }
            CheckBoxPreference checkBoxPreference3 = this.mEnableSim;
            if (checkBoxPreference3 != null) {
                checkBoxPreference3.setEnabled(z2);
            }
            updateCloudSimNetworkType(this.mCommonOptionsEnabled);
            update5GCarrierAggregationEnabled(this.mCommonOptionsEnabled);
            PreferenceCategory preferenceCategory = this.mWifiCallCategory;
            if (preferenceCategory != null) {
                preferenceCategory.setEnabled(z2 || MiuiImsPhoneUtils.shouldShowWfcOptionWhenAirplaneOn(this.mPhone));
                updateWifiCallSwitchView();
            }
            if (this.mButtonViceSaEnabled != null) {
                if (FiveGManagerBase.isFiveGCapable() && FiveGUtils.shouldShowViceSaSwitch(this.mSlotId)) {
                    if (this.mPreferenceScreen.findPreference("button_vice_sim_5g_sa_network") == null && (checkBoxPreference2 = this.mButtonViceSaEnabled) != null) {
                        this.mMobileNetworkCategory.addPreference(checkBoxPreference2);
                    }
                    this.mButtonViceSaEnabled.setChecked(FiveGManagerBase.getInstance().isDualSaEnabled());
                    CheckBoxPreference checkBoxPreference4 = this.mButtonViceSaEnabled;
                    if (this.mCommonOptionsEnabled && FiveGUtils.isDualSaSupported()) {
                        z = true;
                    }
                    checkBoxPreference4.setEnabled(z);
                } else {
                    this.mMobileNetworkCategory.removePreference(this.mButtonViceSaEnabled);
                }
            }
            CheckBoxPreference checkBoxPreference5 = this.mButtonSaEnabled;
            if (checkBoxPreference5 != null) {
                checkBoxPreference5.setEnabled(this.mCommonOptionsEnabled);
            }
            updateStkPreference(this.mCommonOptionsEnabled);
        }

        private void updateStkPreference(boolean z) {
            Preference preference;
            String str;
            if (this.mPreferenceScreen == null) {
                return;
            }
            if (MiuiPhoneUtils.isHideStkIcon()) {
                preference = this.mButtonHideStk;
                str = "button_hide_stk_key";
            } else {
                preference = this.mButtonStk;
                str = "button_stk_key";
            }
            if (this.mPreferenceScreen.findPreference(str) != null && MiuiPhoneUtils.isRemoveStkSettings(this.mSlotId)) {
                this.mMobileNetworkCategory.removePreference(preference);
            } else if (this.mPreferenceScreen.findPreference(str) == null && !MiuiPhoneUtils.isRemoveStkSettings(this.mSlotId)) {
                this.mMobileNetworkCategory.addPreference(preference);
            }
            if (preference != null) {
                preference.setEnabled(z);
            }
        }

        private void setEnabledStateOnPreferences(Preference preference, boolean z) {
            if (preference instanceof PreferenceGroup) {
                PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
                if (!"sim_info_editor".equals(preferenceGroup.getKey())) {
                    preferenceGroup.setEnabled(z);
                }
                for (int preferenceCount = preferenceGroup.getPreferenceCount() - 1; preferenceCount >= 0; preferenceCount--) {
                    setEnabledStateOnPreferences(preferenceGroup.getPreference(preferenceCount), z);
                }
                return;
            }
            if ("enable_sim".equals(preference.getKey()) || "vonr_switch".equals(preference.getKey())) {
                return;
            }
            preference.setEnabled(z);
        }

        @Override // androidx.fragment.app.Fragment
        public void onPause() {
            super.onPause();
            SubscriptionManager.getDefault().removeOnSubscriptionsChangedListener(this);
            this.mHandler.removeMessages(3);
            this.mIsForeground = false;
        }

        @Override // androidx.fragment.app.Fragment
        public void onDestroy() {
            dismissProgressDialog();
            super.onDestroy();
            GsmUmtsOptions gsmUmtsOptions = this.mGsmUmtsOptions;
            if (gsmUmtsOptions != null) {
                gsmUmtsOptions.destroy();
            }
            CdmaOptions cdmaOptions = this.mCdmaOptions;
            if (cdmaOptions != null) {
                cdmaOptions.destroy();
            }
            UiccController.getInstance().unregisterForIccChanged(this.mHandler);
            this.mHandler.removeCallbacks(this.mDismissRunner);
            this.mHandler.removeCallbacksAndMessages(null);
            if (this.mIsReceiverRegistered) {
                getContext().unregisterReceiver(this.mReceiver);
            }
            WifiCallSwitchView wifiCallSwitchView = this.mWifiCallSwitchView;
            if (wifiCallSwitchView != null) {
                wifiCallSwitchView.destroy();
            }
        }

        private void displayErrorDialog(int i) {
            if (getActivity() != null) {
                new AlertDialog.Builder(getActivity()).setMessage(i).setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MultiSimInfoEditorActivity.MultiSimInfoEditorFragment.5
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        MultiSimInfoEditorFragment.this.updateCheckState();
                    }
                }).show().setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.android.phone.settings.MultiSimInfoEditorActivity.MultiSimInfoEditorFragment.4
                    @Override // android.content.DialogInterface.OnDismissListener
                    public void onDismiss(DialogInterface dialogInterface) {
                        MultiSimInfoEditorFragment.this.updateCheckState();
                    }
                });
            }
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            Dialog dialog = this.mSimName.getDialog();
            if (dialog == null || !(dialog instanceof AlertDialog)) {
                return;
            }
            ((AlertDialog) dialog).getButton(-1).setEnabled(charSequence.toString().trim().length() > 0);
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            SubscriptionInfo subscriptionInfo;
            SingleFiveGController singleFiveGController;
            if ("enable_sim".equals(preference.getKey())) {
                if (this.mEnableSim.isChecked()) {
                    Rlog.i("MultiSimInfoEditorActivity", "User enable sim" + this.mSlotId);
                    if ((!FiveGUtils.isSupportSingleFiveG() || (singleFiveGController = SingleFiveGController.getInstance()) == null || !singleFiveGController.showDisable5GDialog()) && (subscriptionInfo = this.mSimInfoRecord) != null) {
                        switchRadioState(subscriptionInfo.getSlotId(), this.mEnableSim.isChecked());
                    }
                } else {
                    showDisableSimDialog();
                }
            } else {
                if ("button_5g_sa_network_key".equals(preference.getKey())) {
                    boolean zIsChecked = this.mButtonSaEnabled.isChecked();
                    Rlog.i("MultiSimInfoEditorActivity", "enable main SA to: " + zIsChecked);
                    FiveGManagerBase.getInstance().setUserFiveGSaEnabled(zIsChecked);
                    MiStatInterfaceUtil.recordSwitchStatusEvent(preference.getKey(), zIsChecked);
                    updateCheckState();
                    return true;
                }
                if ("button_vice_sim_5g_sa_network".equals(preference.getKey())) {
                    boolean zIsChecked2 = this.mButtonViceSaEnabled.isChecked();
                    Rlog.i("MultiSimInfoEditorActivity", "enable vice SA to: " + zIsChecked2);
                    FiveGManagerBase.getInstance().setDualSaEnabled(zIsChecked2);
                    updateCheckState();
                }
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void switchRadioState(final int i, final boolean z) {
            int i2;
            if (MiuiPhoneUtils.isPhoneInCall()) {
                displayErrorDialog(R.string.msim_set_sub_not_supported_phone_in_call);
                return;
            }
            if (!SubscriptionManager.isValidSlotId(i)) {
                displayErrorDialog(R.string.msim_set_sub_not_supported_slot_id_error);
                return;
            }
            if (this.mIsForeground) {
                if (z) {
                    i2 = R.string.msim_mode_progress_activating_message;
                } else {
                    i2 = R.string.msim_mode_progress_deactivating_message;
                }
                showProgressDialog(i2);
                final long jElapsedRealtime = SystemClock.elapsedRealtime();
                new AsyncTask() { // from class: com.android.phone.settings.MultiSimInfoEditorActivity.MultiSimInfoEditorFragment.6
                    /* JADX INFO: Access modifiers changed from: protected */
                    @Override // android.os.AsyncTask
                    public Void doInBackground(Void... voidArr) {
                        Rlog.i("MultiSimInfoEditorActivity", "doInBackground setIccCardActivate slotId = " + i + " isChecked=" + z);
                        ModemProvider.getInstance().setIccCardActivate(i, z);
                        MultiSimInfoEditorFragment.this.mHandler.removeCallbacks(MultiSimInfoEditorFragment.this.mDismissRunner);
                        MultiSimInfoEditorFragment.this.mHandler.postDelayed(MultiSimInfoEditorFragment.this.mDismissRunner, 3000 - (SystemClock.elapsedRealtime() - jElapsedRealtime));
                        return null;
                    }
                }.execute(null, null);
            }
        }

        public void showProgressDialog(int i) {
            dismissProgressDialog();
            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            this.mProgressDialog = progressDialog;
            progressDialog.setMessage(getResources().getString(i));
            this.mProgressDialog.setCancelable(false);
            this.mProgressDialog.setIndeterminate(true);
            this.mProgressDialog.setEnableImmersive(false);
            this.mProgressDialog.show();
        }

        public void dismissProgressDialog() {
            if (this.mProgressDialog != null && getActivity() != null) {
                this.mProgressDialog.dismiss();
            }
            this.mProgressDialog = null;
        }

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, miuix.preference.PreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.preference.PreferenceManager.OnPreferenceTreeClickListener
        public boolean onPreferenceTreeClick(Preference preference) {
            if (preference != null) {
                HashMap map = new HashMap();
                map.put("multisim_info_editor_click_preference", preference.getKey());
                MiStatInterfaceUtil.recordCountEvent("event_settings", "key_multisim_info_editor_preference", map);
            }
            GsmUmtsOptions gsmUmtsOptions = this.mGsmUmtsOptions;
            if (gsmUmtsOptions != null && gsmUmtsOptions.preferenceTreeClick(preference)) {
                return true;
            }
            CdmaOptions cdmaOptions = this.mCdmaOptions;
            if (cdmaOptions != null && cdmaOptions.preferenceTreeClick(preference)) {
                if (Boolean.parseBoolean(SystemProperties.get("ril.cdma.inecmmode"))) {
                    this.mClickedPreference = preference;
                    startActivityForResult(new Intent("android.telephony.action.SHOW_NOTICE_ECM_BLOCK_OTHERS", (Uri) null), 17);
                }
                return true;
            }
            if ("button_go_to_virtual_sim_key".equals(preference.getKey())) {
                Intent virtualSimIntent = MiuiPhoneUtils.getVirtualSimIntent();
                if (virtualSimIntent != null) {
                    startActivity(virtualSimIntent);
                }
                return true;
            }
            if ("button_go_to_esim_manager".equals(preference.getKey())) {
                MiuiPhoneUtils.enterEsimManager(false);
                return true;
            }
            if ("button_5G_carrier_aggregation".equals(preference.getKey())) {
                int i = this.mBtn5GCarrierAggregation.isChecked() ? MultiSimInfoEditorActivity.FIVEG_CA_ON : MultiSimInfoEditorActivity.FIVEG_CA_OFF;
                Rlog.i("MultiSimInfoEditorActivity", "onPreferenceTreeClick 5G CA mode=" + i);
                FiveGManagerBase.getInstance().setFiveGCarrierAggregationMode(i);
                Toast.makeText(getContext(), R.string.nrca_need_reset, 1).show();
                return true;
            }
            if ("button_5g_sa_network_key".equals(preference.getKey())) {
                boolean zIsChecked = this.mButtonSaEnabled.isChecked();
                Rlog.i("MultiSimInfoEditorActivity", "enableSa requested: " + zIsChecked);
                if (zIsChecked) {
                    FiveGManagerBase.getInstance().setUserFiveGNetworkMode(2);
                } else {
                    FiveGManagerBase.getInstance().setUserFiveGNetworkMode(0);
                }
            } else {
                if ("button_convert_to_esim".equals(preference.getKey())) {
                    Rlog.i("MultiSimInfoEditorActivity", "onPreferenceTreeClick BUTTON_CONVERT_TO_ESIM");
                    Intent intent = new Intent("android.telephony.euicc.action.CONVERT_TO_EMBEDDED_SUBSCRIPTION");
                    intent.putExtra("subId", this.mSubId);
                    startActivity(intent);
                    return true;
                }
                if (preference == this.mButtonCarrierSel) {
                    Intent intent2 = new Intent("android.intent.action.MAIN");
                    intent2.setClassName(getActivity().getPackageName(), NetworkSetting.class.getName());
                    SubscriptionManager.putSlotIdExtra(intent2, this.mSlotId);
                    if (getActivity() instanceof AppCompatActivity) {
                        MultiAppFloatingActivitySwitcher.configureFloatingService(intent2, (AppCompatActivity) getActivity());
                    }
                    startActivity(intent2);
                    return true;
                }
            }
            return false;
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            String key = preference.getKey();
            SubscriptionInfo availableSubscriptionInfoBySlot = MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(this.mSlotId);
            if (availableSubscriptionInfoBySlot == null && getActivity() != null) {
                getActivity().finish();
                return false;
            }
            if ("sim_name".equals(key)) {
                if (obj != null) {
                    String str = (String) obj;
                    if (str.equals(availableSubscriptionInfoBySlot.getDisplayName())) {
                        return false;
                    }
                    if (SubscriptionManagerEx.getDefault().setDisplayNameForSubscription(str, availableSubscriptionInfoBySlot.getSubscriptionId()) > 0) {
                        this.mSimName.setRightValue(str);
                        this.mSimName.setText(str);
                    } else {
                        Toast.makeText(getContext(), getResources().getString(R.string.msim_info_editor_set_name_error), 1).show();
                        if (availableSubscriptionInfoBySlot.getDisplayName() != null) {
                            this.mSimName.setRightValue(availableSubscriptionInfoBySlot.getDisplayName().toString());
                            this.mSimName.setText(availableSubscriptionInfoBySlot.getDisplayName().toString());
                        }
                        return false;
                    }
                }
            } else if ("sim_number".equals(key) && obj != null) {
                String str2 = (String) obj;
                if (SubscriptionManagerEx.getDefault().setDisplayNumberForSubscription(str2, availableSubscriptionInfoBySlot.getSubscriptionId()) > 0) {
                    int length = str2.length();
                    LTREditTextPreference lTREditTextPreference = this.mSimNumber;
                    if (length != 0) {
                        lTREditTextPreference.setRightValue(Utils.localizeNumber(str2));
                        this.mSimNumber.setText(Utils.localizeNumber(str2));
                    } else {
                        lTREditTextPreference.setRightValue(this.mNotSet);
                        this.mSimNumber.setText("");
                    }
                    if (getContext() != null) {
                        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                        editorEdit.putBoolean("update_phone_number_by_user" + availableSubscriptionInfoBySlot.getSubscriptionId(), str2.length() != 0);
                        editorEdit.apply();
                    }
                } else {
                    if (availableSubscriptionInfoBySlot.getDisplayNumber() != null && availableSubscriptionInfoBySlot.getDisplayNumber().length() != 0) {
                        this.mSimNumber.setRightValue(Utils.localizeNumber(availableSubscriptionInfoBySlot.getDisplayNumber()));
                        this.mSimNumber.setText(Utils.localizeNumber(availableSubscriptionInfoBySlot.getDisplayNumber()));
                    } else {
                        this.mSimNumber.setRightValue(this.mNotSet);
                        this.mSimNumber.setText("");
                    }
                    return false;
                }
            }
            return true;
        }

        /* JADX WARN: Code duplicated, block: B:19:0x0085  */
        private void showDisableSimDialog() {
            String string;
            int i;
            boolean z;
            if (this.mSimInfoRecord == null) {
                return;
            }
            int i2 = R.string.msim_disable_sim_card_btn_text;
            boolean z2 = SystemProperties.getBoolean("ril.cdma.inecmmode", false);
            if (ModemProvider.getInstance().getDefaultVoiceSlotId() == this.mSlotId || DefaultSimManager.getDefaultDataSlotId() == this.mSlotId) {
                string = getResources().getString(R.string.msim_disable_sim_card_summary_default_card);
            } else {
                if (DefaultSimManager.getDefaultDataSlotId() != this.mSlotId && z2) {
                    Rlog.i("MultiSimInfoEditorActivity", "In emergency call back mode ,deactivating the telecom vice card is forbidden");
                    string = getResources().getString(R.string.msim_disable_sim_card_summary_telecom_vice_card_inEcm);
                } else {
                    string = getResources().getString(R.string.msim_disable_sim_card_summary_non_default_card, this.mSimInfoRecord.getDisplayName());
                    z = true;
                    i = android.R.string.cancel;
                }
                AlertDialog alertDialogCreate = new AlertDialog.Builder(getActivity()).create();
                this.mDisableSimDialog = alertDialogCreate;
                alertDialogCreate.setTitle(R.string.msim_disable_sim_card_title);
                this.mDisableSimDialog.setMessage(string);
                this.mDisableSimDialog.setCancelable(false);
                if (z) {
                    this.mDisableSimDialog.setButton(-1, getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MultiSimInfoEditorActivity.MultiSimInfoEditorFragment.8
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i3) {
                            if (MultiSimInfoEditorFragment.this.mHandler != null) {
                                Rlog.i("MultiSimInfoEditorActivity", "User disable sim");
                                MultiSimInfoEditorFragment.this.mHandler.obtainMessage(0).sendToTarget();
                            }
                        }
                    });
                }
                this.mDisableSimDialog.setButton(-2, getResources().getString(i), new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MultiSimInfoEditorActivity.MultiSimInfoEditorFragment.9
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        if (MultiSimInfoEditorFragment.this.mHandler != null) {
                            MultiSimInfoEditorFragment.this.mHandler.obtainMessage(1).sendToTarget();
                        }
                    }
                });
                this.mDisableSimDialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.android.phone.settings.MultiSimInfoEditorActivity.MultiSimInfoEditorFragment.10
                    @Override // android.content.DialogInterface.OnShowListener
                    public void onShow(DialogInterface dialogInterface) {
                        MultiSimInfoEditorFragment.this.updateEnableSimChecked(false);
                    }
                });
                this.mDisableSimDialog.show();
            }
            i = i2;
            z = false;
            AlertDialog alertDialogCreate2 = new AlertDialog.Builder(getActivity()).create();
            this.mDisableSimDialog = alertDialogCreate2;
            alertDialogCreate2.setTitle(R.string.msim_disable_sim_card_title);
            this.mDisableSimDialog.setMessage(string);
            this.mDisableSimDialog.setCancelable(false);
            if (z) {
                this.mDisableSimDialog.setButton(-1, getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MultiSimInfoEditorActivity.MultiSimInfoEditorFragment.8
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        if (MultiSimInfoEditorFragment.this.mHandler != null) {
                            Rlog.i("MultiSimInfoEditorActivity", "User disable sim");
                            MultiSimInfoEditorFragment.this.mHandler.obtainMessage(0).sendToTarget();
                        }
                    }
                });
            }
            this.mDisableSimDialog.setButton(-2, getResources().getString(i), new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MultiSimInfoEditorActivity.MultiSimInfoEditorFragment.9
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i3) {
                    if (MultiSimInfoEditorFragment.this.mHandler != null) {
                        MultiSimInfoEditorFragment.this.mHandler.obtainMessage(1).sendToTarget();
                    }
                }
            });
            this.mDisableSimDialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.android.phone.settings.MultiSimInfoEditorActivity.MultiSimInfoEditorFragment.10
                @Override // android.content.DialogInterface.OnShowListener
                public void onShow(DialogInterface dialogInterface) {
                    MultiSimInfoEditorFragment.this.updateEnableSimChecked(false);
                }
            });
            this.mDisableSimDialog.show();
        }

        public void updateEnableSimChecked(boolean z) {
            CheckBoxPreference checkBoxPreference = this.mEnableSim;
            if (checkBoxPreference != null) {
                checkBoxPreference.setChecked(z);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateNetworkTypeAvailable() {
            Preference preferenceFindPreference = this.mPreferenceScreen.findPreference("button_preferred_network_type_key");
            if (preferenceFindPreference != null) {
                boolean z = false;
                boolean z2 = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getPhoneType(this.mSlotId) != 0;
                if (this.mIsDisableLteDevice) {
                    z2 = z2 && isIdentifyAllCardOperator();
                }
                preferenceFindPreference.setEnabled(z2 && this.mCommonOptionsEnabled);
                if (z2 && this.mCommonOptionsEnabled) {
                    z = true;
                }
                updateCloudSimNetworkType(z);
            }
        }

        private void updateCloudSimNetworkType(boolean z) {
            Preference preferenceFindPreference = this.mPreferenceScreen.findPreference("button_preferred_network_type_key");
            if (preferenceFindPreference != null) {
                if (VirtualSimUtils.isCloudSimEnabled()) {
                    preferenceFindPreference.setEnabled(false);
                    preferenceFindPreference.setSummary(R.string.cloud_sim_enabled_preferred_network_summary);
                } else {
                    preferenceFindPreference.setEnabled(z);
                    preferenceFindPreference.setSummary("");
                }
            }
        }

        private boolean isIdentifyAllCardOperator() {
            SubscriptionInfo availableSubscriptionInfoBySlot;
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                IccCardStatus.CardState cardState = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getCardState(i);
                if (cardState == null) {
                    return false;
                }
                if (cardState == IccCardStatus.CardState.CARDSTATE_PRESENT && ((availableSubscriptionInfoBySlot = MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(i)) == null || availableSubscriptionInfoBySlot.getMcc() == 0)) {
                    return false;
                }
            }
            return true;
        }

        @Override // androidx.fragment.app.Fragment
        public void onActivityResult(int i, int i2, Intent intent) {
            if (i == 17 && intent.getBooleanExtra("exit_ecm_result", false)) {
                this.mCdmaOptions.showDialog(this.mClickedPreference);
            }
        }

        private boolean isMVNOForESOrange(int i) {
            String simOperatorNameForPhone = android.telephony.TelephonyManager.from(getContext()).getSimOperatorNameForPhone(i);
            if (HyperOSCustFeatureResolve.getBooleanForPhoneId(i, "es_orange_mvno", false)) {
                return !"Orange".equals(simOperatorNameForPhone);
            }
            return false;
        }

        void updateButtonConvertToEsim() {
            Log.d("MultiSimInfoEditorActivity", "updateButtonConvertToEsim");
            if (this.mButtonConvertToEsim == null) {
                Rlog.d("MultiSimInfoEditorActivity", "updateButtonConvertToEsim mButtonConvertToEsim is null");
            } else if (!MiuiPhoneUtils.isEsimCapable()) {
                Rlog.d("MultiSimInfoEditorActivity", " updateButtonConvertToEsim: esim not capable");
                this.mMobileNetworkCategory.removePreference(this.mButtonConvertToEsim);
            } else {
                this.mButtonConvertToEsim.setVisible(((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isPsim2EsimAvailable(this.mSubId));
            }
        }
    }
}
