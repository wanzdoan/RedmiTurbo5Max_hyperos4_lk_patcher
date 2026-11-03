package com.android.phone.settings;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.LinkProperties;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.internal.telephony.PhoneConstants;
import com.android.phone.FiveGServiceInfo;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.ModuleDelegate;
import com.android.phone.R;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.utils.MiuiKoreaOperatorCustimizedUtils;
import com.android.phone.utils.ScreenDisplayUtils;
import com.android.services.telephony.SimpleFeatures;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import java.util.regex.Pattern;
import miui.os.Build;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyManager;
import miuix.appcompat.app.ActionBar;
import miuix.appcompat.app.AlertDialog;
import miuix.preference.DropDownPreference;

/* JADX INFO: loaded from: classes.dex */
public class MiuiConfigureMobileSettings extends BaseActivity {
    public static final String TAG = "MiuiConfigureMobileSettings";
    public static final String VICE_SLOT_VOLTE_DATA_ENABLED;

    static {
        VICE_SLOT_VOLTE_DATA_ENABLED = (ModemProvider.IS_MTK || ModemProvider.IS_XRING) ? "data_service_enabled" : "vice_slot_volte_data_enabled";
    }

    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            loadFragment(new MiuiConfigureMobileSettingsFragment());
        }
    }

    @Override // com.android.phone.settings.BaseActivity
    protected void setActionBar() {
        super.setActionBar();
        ActionBar appCompatActionBar = getAppCompatActionBar();
        if (appCompatActionBar != null) {
            appCompatActionBar.setTitle(R.string.advanced_mobile_settings);
        }
    }

    public class MiuiConfigureMobileSettingsFragment extends BaseActivity.BaseFragment implements Preference.OnPreferenceChangeListener {
        private Preference mBtnSmartMultinetworkAcceleration;
        private CheckBoxPreference mBtnSpecialNetworkEnabled;
        private CheckBoxPreference mBtnTrafficModeEnabled;
        private RestrictedPreference mButtonDataRoam;
        private CheckBoxPreference mButtonSmartDualSim;
        private CheckBoxPreference mButtonSmartSwitchDualSim;
        private CheckBoxPreference mButtonViceSlotVolteData;
        private PreferenceCategory mConnectionMgr;
        private Context mContext;
        private Preference mDnsAddrPref;
        private PreferenceCategory mDualIntelligentConnection;
        private FiveGServiceInfoListener mFiveGServiceInfoListener;
        private Preference mGeoLocationPref;
        private Handler mHandler;
        private PreferenceCategory mIntelligentConnection;
        private Preference mIpAddrPref;
        private Preference mMobileNwCheckPref;
        private DropDownPreference mNetworkStatePref;
        private Preference mNrTypePref;
        private PreferenceScreen mPreferenceScreen;
        private PreferenceCategory mProperty;
        private PhoneConstants.DataState mConnectState = PhoneConstants.DataState.DISCONNECTED;
        private boolean mIsAirplaneEnabled = false;
        private boolean mIsSimInserted = true;
        private int mDefaultDataSlotId = -1;
        private AlertDialog mViceSlotVolteDataPromptDialog = null;
        private AlertDialog mSmartDualSimPromptDialog = null;
        private AlertDialog mSmartSwitchDualSimPromptDialog = null;
        final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.settings.MiuiConfigureMobileSettings.MiuiConfigureMobileSettingsFragment.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                    MiuiConfigureMobileSettingsFragment miuiConfigureMobileSettingsFragment = MiuiConfigureMobileSettingsFragment.this;
                    miuiConfigureMobileSettingsFragment.mIsAirplaneEnabled = miuiConfigureMobileSettingsFragment.isAirPlaneModeOn();
                    MiuiConfigureMobileSettingsFragment.this.updatePreferenceScreen();
                    return;
                }
                if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                    MiuiConfigureMobileSettingsFragment miuiConfigureMobileSettingsFragment2 = MiuiConfigureMobileSettingsFragment.this;
                    miuiConfigureMobileSettingsFragment2.mIsSimInserted = miuiConfigureMobileSettingsFragment2.isSimInserted();
                    MiuiConfigureMobileSettingsFragment.this.updatePreferenceScreen();
                    if (MiuiConfigureMobileSettingsFragment.this.mButtonSmartDualSim == null && MiuiConfigureMobileSettingsFragment.this.mButtonViceSlotVolteData == null && MiuiConfigureMobileSettingsFragment.this.mButtonSmartSwitchDualSim == null) {
                        return;
                    }
                    MiuiConfigureMobileSettingsFragment.this.initDualIntelligentConnection();
                    return;
                }
                if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
                    MiuiConfigureMobileSettingsFragment.this.updateMobileNwInfo();
                    return;
                }
                if (action.equals("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED")) {
                    MiuiConfigureMobileSettingsFragment.this.mHandler.obtainMessage(1, intent).sendToTarget();
                    return;
                }
                if ("android.telephony.action.CARRIER_CONFIG_CHANGED".equals(action)) {
                    Log.d(MiuiConfigureMobileSettings.TAG, "ACTION_CARRIER_CONFIG_CHANGED");
                    if (MiuiConfigureMobileSettingsFragment.this.mButtonSmartDualSim == null && MiuiConfigureMobileSettingsFragment.this.mButtonViceSlotVolteData == null && MiuiConfigureMobileSettingsFragment.this.mButtonSmartSwitchDualSim == null) {
                        return;
                    }
                    MiuiConfigureMobileSettingsFragment.this.initDualIntelligentConnection();
                }
            }
        };

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            this.mContext = getContext().getApplicationContext();
            addPreferencesFromResource(R.xml.mobile_configure_settings);
            this.mDefaultDataSlotId = getDefaultDataSlotId();
            initUI();
            this.mHandler = new MobileSettingHandler(getContext().getMainLooper());
            initReceiver();
            this.mIsAirplaneEnabled = isAirPlaneModeOn();
            this.mIsSimInserted = isSimInserted();
        }

        private void initReceiver() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
            intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
            intentFilter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
            intentFilter.addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
            getContext().registerReceiver(this.mReceiver, intentFilter, 2);
        }

        public class MobileSettingHandler extends Handler {
            public MobileSettingHandler(Looper looper) {
                super(looper);
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i = message.what;
                if (i == 0) {
                    MiuiConfigureMobileSettingsFragment.this.updateNrConfigType((FiveGServiceInfo) message.obj);
                } else {
                    if (i != 1) {
                        return;
                    }
                    MiuiConfigureMobileSettingsFragment.this.onDefaultDataChanged();
                }
            }
        }

        @Override // androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            updateMobileNwInfo();
            RestrictedPreference restrictedPreference = this.mButtonDataRoam;
            if (restrictedPreference != null && restrictedPreference.isEnabled()) {
                Context applicationContext = getContext().getApplicationContext();
                boolean zHasBaseUserRestriction = SimpleFeatures.hasBaseUserRestriction(applicationContext);
                RestrictedPreference restrictedPreference2 = this.mButtonDataRoam;
                if (zHasBaseUserRestriction) {
                    restrictedPreference2.setEnabled(false);
                } else {
                    restrictedPreference2.setDisabledByAdmin(SimpleFeatures.checkDataRoamingRestriction(applicationContext));
                }
            }
            updateTrafficMode();
            updateSpecialNetworkMode();
            if (MiuiPhoneUtils.isSmartSwitchDualSimSupported()) {
                updateSmartSwitchDualSimButton();
            } else {
                updateSmartDualSimButton();
                updateViceSlotDataButton();
            }
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            if (preference != this.mNetworkStatePref) {
                return true;
            }
            String string = obj.toString();
            this.mNetworkStatePref.setValue(string);
            int i = Integer.parseInt(string);
            Settings.Global.putInt(this.mContext.getContentResolver(), "setting_network_state_display", i);
            MiuiConfigureMobileSettings.log("mNetworkStatePref change isAvailNetworkDisplay = " + i);
            return true;
        }

        private void initUI() {
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            this.mPreferenceScreen = preferenceScreen;
            this.mIntelligentConnection = (PreferenceCategory) preferenceScreen.findPreference("mobile_intelligent_connection");
            this.mConnectionMgr = (PreferenceCategory) this.mPreferenceScreen.findPreference("mobile_connection_manage");
            this.mProperty = (PreferenceCategory) this.mPreferenceScreen.findPreference("mobile_property");
            this.mMobileNwCheckPref = this.mPreferenceScreen.findPreference("mobile_network_check");
            this.mIpAddrPref = this.mPreferenceScreen.findPreference("mobile_ip_address");
            this.mDnsAddrPref = this.mPreferenceScreen.findPreference("mobile_dns_server");
            this.mNetworkStatePref = (DropDownPreference) this.mPreferenceScreen.findPreference("network_state_display");
            this.mButtonDataRoam = (RestrictedPreference) this.mPreferenceScreen.findPreference("button_roaming_key");
            this.mNetworkStatePref.setOnPreferenceChangeListener(this);
            if (MiuiKoreaOperatorCustimizedUtils.isCustForKrOps()) {
                this.mNetworkStatePref.setOnPreferenceChangeListener(this);
            } else {
                this.mProperty.removePreference(this.mNetworkStatePref);
            }
            displayMobileNwInfo(this.mPreferenceScreen);
            this.mNrTypePref = this.mPreferenceScreen.findPreference("mobile_nr_type");
            if (MiuiPhoneUtils.isFiveGCapable() && !Build.IS_INTERNATIONAL_BUILD) {
                register5GServiceInfoListener();
                MiuiConfigureMobileSettings.log("display nr network config");
            } else {
                this.mProperty.removePreference(this.mNrTypePref);
            }
            RestrictedPreference restrictedPreference = this.mButtonDataRoam;
            if (restrictedPreference != null) {
                if (Build.IS_INTERNATIONAL_BUILD) {
                    restrictedPreference.setTitle(R.string.international_roaming_setting);
                } else {
                    restrictedPreference.setTitle(R.string.overseas_roaming_setting);
                }
                if (MiuiKoreaOperatorCustimizedUtils.isCustForKrOps()) {
                    this.mButtonDataRoam.setSummary(R.string.roaming_disable_kr);
                    this.mButtonDataRoam.setTitle(MiuiKoreaOperatorCustimizedUtils.isCustForKrSkt() ? R.string.international_roaming_setting_kr_skt : R.string.international_roaming_setting_kr);
                }
            }
            this.mBtnTrafficModeEnabled = (CheckBoxPreference) this.mPreferenceScreen.findPreference("button_traffic_mode_enabled_key");
            this.mBtnSpecialNetworkEnabled = (CheckBoxPreference) this.mPreferenceScreen.findPreference("mobile_special_network_optimization_config_enabled_key");
            this.mGeoLocationPref = this.mPreferenceScreen.findPreference("mobile_geo_location_config_key");
            this.mBtnSmartMultinetworkAcceleration = this.mPreferenceScreen.findPreference("button_smart_multinetwork_acceleration_key");
            initIntelligentConnection();
            this.mDualIntelligentConnection = (PreferenceCategory) this.mPreferenceScreen.findPreference("mobile_network_dual_card_optimizationmobile");
            this.mButtonSmartDualSim = (CheckBoxPreference) this.mPreferenceScreen.findPreference("button_smart_dual_sim_key");
            this.mButtonViceSlotVolteData = (CheckBoxPreference) this.mPreferenceScreen.findPreference("button_vice_slot_volte_data");
            this.mButtonSmartSwitchDualSim = (CheckBoxPreference) this.mPreferenceScreen.findPreference("button_smart_switch_dual_sim_key");
            initDualIntelligentConnection();
            initMiuiFifteenUI();
        }

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, miuix.preference.PreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.preference.PreferenceManager.OnPreferenceTreeClickListener
        public boolean onPreferenceTreeClick(Preference preference) {
            if (preference == this.mMobileNwCheckPref) {
                Intent intent = new Intent();
                if (!MiuiPhoneUtils.isSupportSplit()) {
                    intent.addFlags(268435456);
                }
                intent.setClassName("com.miui.securitycenter", "com.miui.networkassistant.ui.activity.NetworkDiagnosticsActivity");
                intent.putExtra("extra_from", "com.android.settings");
                startActivity(intent);
                return true;
            }
            if (preference == this.mButtonDataRoam) {
                Intent intent2 = new Intent();
                if (!MiuiPhoneUtils.isSupportSplit()) {
                    intent2.addFlags(268435456);
                }
                if (MiuiKoreaOperatorCustimizedUtils.isCustForKrOps()) {
                    intent2.setClassName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.settings.MiuiDataRoamingSettings");
                } else {
                    intent2.setClassName("com.miui.securitycenter", "com.miui.networkassistant.ui.activity.InternationalRoamingSettingActivity");
                    intent2.putExtra("extra_from", "com.android.settings");
                }
                ScreenDisplayUtils.shouldForceSplit(intent2, getActivity().getIntent().getMiuiFlags());
                startActivity(intent2);
                return true;
            }
            CheckBoxPreference checkBoxPreference = this.mBtnTrafficModeEnabled;
            if (preference == checkBoxPreference) {
                boolean zIsChecked = checkBoxPreference.isChecked();
                MiuiConfigureMobileSettings.log("onPreferenceTreeClick mBtnTrafficModeEnabled mode=" + (zIsChecked ? 1 : 0));
                MiuiPhoneUtils.setTrafficModeInDb(this.mContext, zIsChecked ? 1 : 0);
                return true;
            }
            CheckBoxPreference checkBoxPreference2 = this.mBtnSpecialNetworkEnabled;
            if (preference == checkBoxPreference2) {
                boolean zIsChecked2 = checkBoxPreference2.isChecked();
                Log.d(MiuiConfigureMobileSettings.TAG, "onPreferenceTreeClick mBtnSpecialNetworkEnabled mode=" + (zIsChecked2 ? 1 : 0));
                MiuiPhoneUtils.setSpecialNetworkModeInDb(getContext(), zIsChecked2 ? 1 : 0);
                Preference preference2 = this.mGeoLocationPref;
                if (preference2 != null) {
                    preference2.setEnabled(this.mBtnSpecialNetworkEnabled.isChecked());
                }
                return true;
            }
            if (preference == this.mButtonSmartSwitchDualSim) {
                if (MiuiPhoneUtils.isPhoneInCall()) {
                    Toast.makeText(getContext(), R.string.msim_set_sub_not_supported_phone_in_call, 0).show();
                    CheckBoxPreference checkBoxPreference3 = this.mButtonSmartSwitchDualSim;
                    checkBoxPreference3.setChecked(!checkBoxPreference3.isChecked());
                    return false;
                }
                if (this.mButtonSmartSwitchDualSim.isChecked()) {
                    alertSmartSwitchDualSimPrompt();
                } else {
                    Settings.Global.putInt(getContext().getApplicationContext().getContentResolver(), "smart_dual_sim", 0);
                    Settings.Global.putInt(getContext().getApplicationContext().getContentResolver(), MiuiConfigureMobileSettings.VICE_SLOT_VOLTE_DATA_ENABLED, 0);
                    Log.d(MiuiConfigureMobileSettings.TAG, "set smartswitchdualsim enable false");
                }
                return true;
            }
            CheckBoxPreference checkBoxPreference4 = this.mButtonSmartDualSim;
            if (preference == checkBoxPreference4) {
                if (checkBoxPreference4.isChecked()) {
                    alertSmartDualSimPrompt();
                } else {
                    Settings.Global.putInt(getContext().getApplicationContext().getContentResolver(), "smart_dual_sim", 0);
                    Log.d(MiuiConfigureMobileSettings.TAG, "set smartdualsim enable false");
                }
                return true;
            }
            if (preference == this.mButtonViceSlotVolteData) {
                if (MiuiPhoneUtils.isPhoneInCall()) {
                    Toast.makeText(getContext(), R.string.msim_set_sub_not_supported_phone_in_call, 0).show();
                    CheckBoxPreference checkBoxPreference5 = this.mButtonViceSlotVolteData;
                    checkBoxPreference5.setChecked(!checkBoxPreference5.isChecked());
                    return false;
                }
                if (this.mButtonViceSlotVolteData.isChecked()) {
                    AlertDialog alertDialogCreate = new AlertDialog.Builder(getActivity()).setMessage(R.string.vice_slot_volte_data_switch_prompt).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MiuiConfigureMobileSettings.MiuiConfigureMobileSettingsFragment.3
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Settings.Global.putInt(MiuiConfigureMobileSettingsFragment.this.getContext().getContentResolver(), MiuiConfigureMobileSettings.VICE_SLOT_VOLTE_DATA_ENABLED, 1);
                            Log.d(MiuiConfigureMobileSettings.TAG, "set ViceSlotVolteData enable true");
                        }
                    }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MiuiConfigureMobileSettings.MiuiConfigureMobileSettingsFragment.2
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MiuiConfigureMobileSettingsFragment.this.mButtonViceSlotVolteData.setChecked(false);
                        }
                    }).setCancelable(false).create();
                    this.mViceSlotVolteDataPromptDialog = alertDialogCreate;
                    alertDialogCreate.show();
                    Log.d(MiuiConfigureMobileSettings.TAG, "show ViceSlotVolteData enable prompt dialog");
                } else {
                    Settings.Global.putInt(getContext().getApplicationContext().getContentResolver(), MiuiConfigureMobileSettings.VICE_SLOT_VOLTE_DATA_ENABLED, 0);
                    Log.d(MiuiConfigureMobileSettings.TAG, "set ViceSlotVolteData enable false");
                }
                return true;
            }
            if (preference != this.mBtnSmartMultinetworkAcceleration) {
                return false;
            }
            Intent intent3 = new Intent();
            if (!MiuiPhoneUtils.isPad()) {
                intent3.addFlags(131072);
            }
            intent3.setClassName("com.android.settings", "com.android.settings.wifi.linkturbo.WifiLinkTurboSettings");
            intent3.putExtra("extra_from", "com.android.settings");
            startActivity(intent3);
            return true;
        }

        @Override // androidx.fragment.app.Fragment
        public void onPause() {
            super.onPause();
        }

        @Override // androidx.fragment.app.Fragment
        public void onDestroy() {
            getContext().unregisterReceiver(this.mReceiver);
            if (MiuiPhoneUtils.isFiveGCapable() && !Build.IS_INTERNATIONAL_BUILD) {
                unregister5GServiceInfoListener();
            }
            dismissViceSlotVolteDataPrompt();
            dismissSmartDualSimPrompt();
            dismissSmartSwitchDualSimPrompt();
            super.onDestroy();
        }

        private void displayMobileNwInfo(PreferenceScreen preferenceScreen) {
            this.mIpAddrPref.setSelectable(false);
            this.mDnsAddrPref.setSelectable(false);
            updateMobileNwInfo();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateMobileNwInfo() {
            LinkProperties mobileLinkProperties = MiuiPhoneUtils.getMobileLinkProperties(this.mContext);
            if (this.mIpAddrPref != null) {
                String mobileIpAddresses = MiuiPhoneUtils.getMobileIpAddresses(mobileLinkProperties);
                Preference preference = this.mIpAddrPref;
                if (mobileIpAddresses == null) {
                    mobileIpAddresses = this.mContext.getString(R.string.status_unavailable);
                }
                preference.setSummary(mobileIpAddresses);
            }
            if (this.mDnsAddrPref != null) {
                String mobileDnsAddresses = MiuiPhoneUtils.getMobileDnsAddresses(mobileLinkProperties);
                if (!TextUtils.isEmpty(mobileDnsAddresses)) {
                    mobileDnsAddresses = filterExceptionChar(mobileDnsAddresses);
                }
                Preference preference2 = this.mDnsAddrPref;
                if (mobileDnsAddresses == null) {
                    mobileDnsAddresses = this.mContext.getString(R.string.status_unavailable);
                }
                preference2.setSummary(mobileDnsAddresses);
            }
            if (MiuiKoreaOperatorCustimizedUtils.isCustForKrOps()) {
                updateNetworkDisplay();
            }
        }

        private void updateNetworkDisplay() {
            String str;
            int i = Settings.Global.getInt(this.mContext.getContentResolver(), "setting_network_state_display", 1);
            DropDownPreference dropDownPreference = this.mNetworkStatePref;
            if (i == 1) {
                str = (String) dropDownPreference.getEntryValues()[0];
            } else {
                str = (String) dropDownPreference.getEntryValues()[1];
            }
            dropDownPreference.setValue(str);
        }

        private String filterExceptionChar(String str) {
            return str != null ? Pattern.compile("/").matcher(str).replaceAll("") : str;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updatePreferenceScreen() {
            setPreferenceScreenEnabled(this.mIsSimInserted && !this.mIsAirplaneEnabled);
        }

        private void setPreferenceScreenEnabled(boolean z) {
            PreferenceCategory preferenceCategory = this.mIntelligentConnection;
            if (preferenceCategory != null) {
                preferenceCategory.setEnabled(z);
            }
            PreferenceCategory preferenceCategory2 = this.mDualIntelligentConnection;
            if (preferenceCategory2 != null) {
                preferenceCategory2.setEnabled(z);
            }
            PreferenceCategory preferenceCategory3 = this.mConnectionMgr;
            if (preferenceCategory3 != null) {
                preferenceCategory3.setEnabled(z);
            }
            PreferenceCategory preferenceCategory4 = this.mProperty;
            if (preferenceCategory4 != null) {
                preferenceCategory4.setEnabled(z);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isAirPlaneModeOn() {
            return Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isSimInserted() {
            return TelephonyManager.getDefault().hasIccCard();
        }

        private void register5GServiceInfoListener() {
            this.mFiveGServiceInfoListener = new FiveGServiceInfoListener();
            EventHubManager.getInstance().registerListener("telephony", "fiveg_service_info_changed", this.mFiveGServiceInfoListener);
            updateNrConfigType(((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getFiveGServiceInfo(getDefaultDataSlotId()));
        }

        private void unregister5GServiceInfoListener() {
            EventHubManager.getInstance().unregisterListener(this.mFiveGServiceInfoListener);
            this.mFiveGServiceInfoListener = null;
        }

        class FiveGServiceInfoListener implements BaseEvent.IEventListener {
            FiveGServiceInfoListener() {
            }

            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public void onEvent(BaseEvent baseEvent) {
                TelephonyEvent.FiveGEvent fiveGEvent;
                Bundle bundle;
                if (!(baseEvent instanceof TelephonyEvent.FiveGEvent) || (bundle = (fiveGEvent = (TelephonyEvent.FiveGEvent) baseEvent).getBundle()) == null) {
                    return;
                }
                int i = bundle.getInt(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID);
                FiveGServiceInfo fiveGServiceInfo = fiveGEvent.getFiveGServiceInfo();
                if (fiveGServiceInfo != null && MiuiConfigureMobileSettingsFragment.this.isValidatePhoneId(i) && i == MiuiConfigureMobileSettingsFragment.this.mDefaultDataSlotId) {
                    MiuiConfigureMobileSettingsFragment.this.mHandler.obtainMessage(0, fiveGServiceInfo).sendToTarget();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateNrConfigType(FiveGServiceInfo fiveGServiceInfo) {
            String string = this.mContext.getString(R.string.status_unavailable);
            if (fiveGServiceInfo != null) {
                MiuiConfigureMobileSettings.log("show 5G = " + fiveGServiceInfo.isShow5GIcon() + ", nrConfigType" + fiveGServiceInfo.getNrConfigType());
                if (fiveGServiceInfo.isShow5GIcon()) {
                    int nrConfigType = fiveGServiceInfo.getNrConfigType();
                    if (nrConfigType == 1) {
                        string = "5G SA";
                    } else if (nrConfigType == 0) {
                        string = "5G NSA";
                    }
                }
            }
            Preference preference = this.mNrTypePref;
            if (preference != null) {
                preference.setSummary(string);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onDefaultDataChanged() {
            if (MiuiPhoneUtils.isFiveGCapable()) {
                int defaultDataSlotId = getDefaultDataSlotId();
                this.mDefaultDataSlotId = defaultDataSlotId;
                if (isValidatePhoneId(defaultDataSlotId)) {
                    updateNrConfigType(((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getFiveGServiceInfo(this.mDefaultDataSlotId));
                }
            }
            updateSmartSwitchDualSimButton();
        }

        private int getDefaultDataSlotId() {
            return DefaultSimManager.getDefaultDataSlotId();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isValidatePhoneId(int i) {
            return i >= 0 && i < MiuiPhoneUtils.PHONE_COUNT;
        }

        private void initMiuiFifteenUI() {
            Preference preference;
            if (MiuiPhoneUtils.overMiuiFifteen()) {
                PreferenceCategory preferenceCategory = this.mProperty;
                if (preferenceCategory != null) {
                    Preference preference2 = this.mIpAddrPref;
                    if (preference2 != null) {
                        preferenceCategory.removePreference(preference2);
                        this.mIpAddrPref = null;
                    }
                    Preference preference3 = this.mDnsAddrPref;
                    if (preference3 != null) {
                        this.mProperty.removePreference(preference3);
                        this.mDnsAddrPref = null;
                    }
                    Preference preference4 = this.mNrTypePref;
                    if (preference4 != null) {
                        this.mProperty.removePreference(preference4);
                        this.mNrTypePref = null;
                    }
                    if (this.mProperty.getPreferenceCount() == 0) {
                        this.mPreferenceScreen.removePreference(this.mProperty);
                    }
                }
                PreferenceCategory preferenceCategory2 = this.mConnectionMgr;
                if (preferenceCategory2 != null && (preference = this.mMobileNwCheckPref) != null) {
                    preferenceCategory2.removePreference(preference);
                    this.mMobileNwCheckPref = null;
                }
                PreferenceCategory preferenceCategory3 = this.mIntelligentConnection;
                if (preferenceCategory3 != null) {
                    CheckBoxPreference checkBoxPreference = this.mBtnSpecialNetworkEnabled;
                    if (checkBoxPreference != null) {
                        preferenceCategory3.removePreference(checkBoxPreference);
                        this.mBtnSpecialNetworkEnabled = null;
                    }
                    CheckBoxPreference checkBoxPreference2 = this.mBtnTrafficModeEnabled;
                    if (checkBoxPreference2 != null) {
                        this.mIntelligentConnection.removePreference(checkBoxPreference2);
                        this.mBtnTrafficModeEnabled = null;
                    }
                    if (this.mIntelligentConnection.getPreferenceCount() == 0) {
                        this.mPreferenceScreen.removePreference(this.mIntelligentConnection);
                    }
                }
            }
        }

        private void initIntelligentConnection() {
            PreferenceCategory preferenceCategory;
            PreferenceCategory preferenceCategory2;
            PreferenceCategory preferenceCategory3;
            PreferenceCategory preferenceCategory4;
            if (!MiuiPhoneUtils.isTrafficControlSupported() && (preferenceCategory4 = this.mIntelligentConnection) != null) {
                preferenceCategory4.removePreference(this.mBtnTrafficModeEnabled);
                this.mBtnTrafficModeEnabled = null;
            }
            if (!MiuiPhoneUtils.isSpecialNetorkOptimizationSupported() && (preferenceCategory3 = this.mIntelligentConnection) != null) {
                preferenceCategory3.removePreference(this.mBtnSpecialNetworkEnabled);
                this.mBtnSpecialNetworkEnabled = null;
            }
            if (!MiuiPhoneUtils.isGeoLocationSupported() && (preferenceCategory2 = this.mIntelligentConnection) != null) {
                preferenceCategory2.removePreference(this.mGeoLocationPref);
                this.mGeoLocationPref = null;
            }
            if (!MiuiPhoneUtils.isSupportedSmartMultinetworkAcceleration() && (preferenceCategory = this.mIntelligentConnection) != null) {
                preferenceCategory.removePreference(this.mBtnSmartMultinetworkAcceleration);
                this.mBtnSmartMultinetworkAcceleration = null;
            }
            PreferenceCategory preferenceCategory5 = this.mIntelligentConnection;
            if (preferenceCategory5 != null && preferenceCategory5.getPreferenceCount() == 0) {
                this.mPreferenceScreen.removePreference(this.mIntelligentConnection);
            } else if (this.mPreferenceScreen.findPreference("mobile_intelligent_connection") == null) {
                this.mPreferenceScreen.addPreference(this.mIntelligentConnection);
            }
        }

        private void updateTrafficMode() {
            if (this.mBtnTrafficModeEnabled != null) {
                boolean zIsTrafficModeEnabled = MiuiPhoneUtils.isTrafficModeEnabled(this.mContext);
                MiuiConfigureMobileSettings.log("updateTrafficMode status=" + zIsTrafficModeEnabled);
                this.mBtnTrafficModeEnabled.setChecked(zIsTrafficModeEnabled);
            }
        }

        private void updateSpecialNetworkMode() {
            if (this.mBtnSpecialNetworkEnabled != null) {
                boolean zIsSpecialNetworkModeEnabled = MiuiPhoneUtils.isSpecialNetworkModeEnabled(getContext());
                Log.d(MiuiConfigureMobileSettings.TAG, "updateSpecialNetworkMode status=" + zIsSpecialNetworkModeEnabled);
                this.mBtnSpecialNetworkEnabled.setChecked(zIsSpecialNetworkModeEnabled);
                Preference preference = this.mGeoLocationPref;
                if (preference != null) {
                    preference.setEnabled(zIsSpecialNetworkModeEnabled);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void initDualIntelligentConnection() {
            PreferenceCategory preferenceCategory;
            PreferenceCategory preferenceCategory2;
            PreferenceCategory preferenceCategory3;
            if (!MiuiPhoneUtils.shouldHideIntelligentDualSimButton() && (preferenceCategory3 = this.mDualIntelligentConnection) != null) {
                this.mPreferenceScreen.removePreference(preferenceCategory3);
                return;
            }
            if (MiuiPhoneUtils.isSmartSwitchDualSimSupported()) {
                if ((MiuiPhoneUtils.shouldHideSmartDualSimButton() || MiuiPhoneUtils.shouldHideViceSlotVolteDataButton()) && (preferenceCategory2 = this.mDualIntelligentConnection) != null) {
                    preferenceCategory2.removePreference(this.mButtonSmartSwitchDualSim);
                    dismissSmartSwitchDualSimPrompt();
                } else {
                    PreferenceCategory preferenceCategory4 = this.mDualIntelligentConnection;
                    if (preferenceCategory4 != null && preferenceCategory4.findPreference("button_smart_switch_dual_sim_key") == null) {
                        this.mDualIntelligentConnection.addPreference(this.mButtonSmartSwitchDualSim);
                    }
                }
                PreferenceCategory preferenceCategory5 = this.mDualIntelligentConnection;
                if (preferenceCategory5 != null) {
                    preferenceCategory5.removePreference(this.mButtonSmartDualSim);
                    dismissSmartDualSimPrompt();
                    this.mDualIntelligentConnection.removePreference(this.mButtonViceSlotVolteData);
                    dismissViceSlotVolteDataPrompt();
                }
            } else {
                if (MiuiPhoneUtils.shouldHideViceSlotVolteDataButton() && (preferenceCategory = this.mDualIntelligentConnection) != null) {
                    preferenceCategory.removePreference(this.mButtonViceSlotVolteData);
                    dismissViceSlotVolteDataPrompt();
                } else {
                    PreferenceCategory preferenceCategory6 = this.mDualIntelligentConnection;
                    if (preferenceCategory6 != null && preferenceCategory6.findPreference("button_vice_slot_volte_data") == null) {
                        this.mDualIntelligentConnection.addPreference(this.mButtonViceSlotVolteData);
                    }
                }
                PreferenceCategory preferenceCategory7 = this.mDualIntelligentConnection;
                if (preferenceCategory7 != null) {
                    preferenceCategory7.removePreference(this.mButtonSmartSwitchDualSim);
                    dismissSmartSwitchDualSimPrompt();
                    this.mDualIntelligentConnection.removePreference(this.mButtonSmartDualSim);
                    dismissSmartDualSimPrompt();
                }
            }
            PreferenceCategory preferenceCategory8 = this.mDualIntelligentConnection;
            if (preferenceCategory8 != null && preferenceCategory8.getPreferenceCount() == 0) {
                this.mPreferenceScreen.removePreference(this.mDualIntelligentConnection);
            } else if (this.mPreferenceScreen.findPreference("mobile_network_dual_card_optimizationmobile") == null) {
                this.mPreferenceScreen.addPreference(this.mDualIntelligentConnection);
            }
        }

        private void updateSmartDualSimButton() {
            CheckBoxPreference checkBoxPreference = this.mButtonSmartDualSim;
            if (checkBoxPreference != null) {
                checkBoxPreference.setChecked(Settings.Global.getInt(getContext().getContentResolver(), "smart_dual_sim", 0) == 1);
                dismissSmartDualSimPrompt();
            }
        }

        private void updateSmartSwitchDualSimButton() {
            if (this.mButtonSmartSwitchDualSim != null) {
                if (Settings.Global.getInt(getContext().getContentResolver(), "smart_dual_sim", 0) == 1 && Settings.Global.getInt(getContext().getContentResolver(), MiuiConfigureMobileSettings.VICE_SLOT_VOLTE_DATA_ENABLED, 0) == 1) {
                    this.mButtonSmartSwitchDualSim.setChecked(true);
                } else {
                    this.mButtonSmartSwitchDualSim.setChecked(false);
                }
                dismissSmartSwitchDualSimPrompt();
            }
        }

        private void dismissSmartDualSimPrompt() {
            AlertDialog alertDialog = this.mSmartDualSimPromptDialog;
            if (alertDialog == null || !alertDialog.isShowing()) {
                return;
            }
            this.mSmartDualSimPromptDialog.dismiss();
            this.mSmartDualSimPromptDialog = null;
        }

        private void dismissSmartSwitchDualSimPrompt() {
            AlertDialog alertDialog = this.mSmartSwitchDualSimPromptDialog;
            if (alertDialog == null || !alertDialog.isShowing()) {
                return;
            }
            this.mSmartSwitchDualSimPromptDialog.dismiss();
            this.mSmartSwitchDualSimPromptDialog = null;
        }

        private void alertSmartDualSimPrompt() {
            final int i = DefaultSimManager.getDefaultDataSlotId() == 0 ? 1 : 0;
            AlertDialog alertDialogCreate = new AlertDialog.Builder(getActivity()).setTitle(String.format(getString(R.string.smart_dual_sim_prompt_title), Integer.valueOf(i + 1))).setMessage(R.string.smart_dual_sim_prompt).setPositiveButton(R.string.smart_dual_sim_prompt_ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MiuiConfigureMobileSettings.MiuiConfigureMobileSettingsFragment.5
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    Settings.Global.putInt(MiuiConfigureMobileSettingsFragment.this.getContext().getContentResolver(), "smart_dual_sim", 1);
                    try {
                        MiuiConfigureMobileSettingsFragment.this.startActivity(MiuiConfigureMobileSettingsFragment.getNetworkAssistantIntent(i));
                    } catch (ActivityNotFoundException unused) {
                        Log.d(MiuiConfigureMobileSettings.TAG, "transferToTrafficAlert Exception");
                    }
                    Log.d(MiuiConfigureMobileSettings.TAG, "set smartdualsim enable true");
                }
            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MiuiConfigureMobileSettings.MiuiConfigureMobileSettingsFragment.4
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    MiuiConfigureMobileSettingsFragment.this.mButtonSmartDualSim.setChecked(false);
                }
            }).setCancelable(false).create();
            this.mSmartDualSimPromptDialog = alertDialogCreate;
            alertDialogCreate.show();
            Log.d(MiuiConfigureMobileSettings.TAG, "show smartdualsim enable prompt dialog");
        }

        private void alertSmartSwitchDualSimPrompt() {
            final int i = DefaultSimManager.getDefaultDataSlotId() == 0 ? 1 : 0;
            int i2 = i + 1;
            AlertDialog alertDialogCreate = new AlertDialog.Builder(getActivity()).setTitle(String.format(getString(R.string.smart_dual_sim_prompt_title), Integer.valueOf(i2))).setMessage(String.format(getString(R.string.smart_switch_dual_sim_prompt), Integer.valueOf(i2))).setPositiveButton(R.string.smart_dual_sim_prompt_ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MiuiConfigureMobileSettings.MiuiConfigureMobileSettingsFragment.7
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i3) {
                    Settings.Global.putInt(MiuiConfigureMobileSettingsFragment.this.getContext().getContentResolver(), "smart_dual_sim", 1);
                    Settings.Global.putInt(MiuiConfigureMobileSettingsFragment.this.getContext().getContentResolver(), MiuiConfigureMobileSettings.VICE_SLOT_VOLTE_DATA_ENABLED, 1);
                    try {
                        MiuiConfigureMobileSettingsFragment.this.startActivity(MiuiConfigureMobileSettingsFragment.getNetworkAssistantIntent(i));
                    } catch (ActivityNotFoundException unused) {
                        Log.d(MiuiConfigureMobileSettings.TAG, "transferToTrafficAlert Exception");
                    }
                    Log.d(MiuiConfigureMobileSettings.TAG, "set smartswitchdualsim enable true");
                }
            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MiuiConfigureMobileSettings.MiuiConfigureMobileSettingsFragment.6
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i3) {
                    MiuiConfigureMobileSettingsFragment.this.mButtonSmartSwitchDualSim.setChecked(false);
                }
            }).setCancelable(false).create();
            this.mSmartSwitchDualSimPromptDialog = alertDialogCreate;
            alertDialogCreate.show();
            Log.d(MiuiConfigureMobileSettings.TAG, "show smartswitchdualsim enable prompt dialog");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Intent getNetworkAssistantIntent(int i) {
            Intent intent = new Intent("miui.intent.action.NETWORKASSISTANT_LIMIT_SETTING");
            intent.setPackage("com.miui.securitycenter");
            intent.putExtra("sim_slot_num_tag", i);
            return intent;
        }

        private void updateViceSlotDataButton() {
            CheckBoxPreference checkBoxPreference = this.mButtonViceSlotVolteData;
            if (checkBoxPreference != null) {
                checkBoxPreference.setChecked(Settings.Global.getInt(getContext().getContentResolver(), MiuiConfigureMobileSettings.VICE_SLOT_VOLTE_DATA_ENABLED, 0) == 1);
                dismissViceSlotVolteDataPrompt();
            }
        }

        private void dismissViceSlotVolteDataPrompt() {
            AlertDialog alertDialog = this.mViceSlotVolteDataPromptDialog;
            if (alertDialog == null || !alertDialog.isShowing()) {
                return;
            }
            this.mViceSlotVolteDataPromptDialog.dismiss();
            this.mViceSlotVolteDataPromptDialog = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d(TAG, str);
    }
}
