package com.android.phone.settings;

import android.app.ActivityThread;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.provider.Telephony;
import android.telephony.Rlog;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.util.VirtualSim;
import android.widget.Toast;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.internal.telephony.HexDump;
import com.android.internal.telephony.MiuiNetworkControllerImpl;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.SettingsObserver;
import com.android.internal.telephony.data.MiuiDataControllerImpl;
import com.android.phone.FiveGManagerBase;
import com.android.phone.MiuiAisSimLockManager;
import com.android.phone.MiuiImsPhoneUtils;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.phone.SmartCFSwitchView;
import com.android.phone.TelephonyCapabilities;
import com.android.phone.custompreferences.MiuixTextPreference;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.module.IUtilityModule;
import com.android.phone.settings.sim.DefaultSimPreference;
import com.android.phone.settings.sim.MultiSimPreference;
import com.android.phone.settings.sim.SimCacheHelper;
import com.android.phone.settings.sim.Utils$PhoneUtil;
import com.android.phone.settings.sim.Utils$VirtualSim;
import com.android.phone.settings.sim.bean.PackageBean;
import com.android.phone.settings.sim.bean.SimInfoBean;
import com.android.phone.utils.FiveGUtils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.MiuiKoreaOperatorCustimizedUtils;
import com.android.phone.utils.ScreenDisplayUtils;
import com.android.phone.utils.Utils;
import com.android.services.telephony.SimpleFeatures;
import com.android.services.telephony.ims.ImsAdapterBase;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.miui.enterprise.PhoneHelper;
import com.miui.enterprise.settings.EnterpriseSettings;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import miui.enterprise.EnterpriseManagerStub;
import miui.enterprise.PhoneHelperStub;
import miui.os.Build;
import miui.telephony.DefaultSimManager;
import miui.telephony.FeatureConfiguration;
import miui.telephony.MiuiTelephony;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyManagerEx;
import miui.telephony.VirtualSimUtils;
import miuix.animation.controller.AnimState;
import miuix.appcompat.app.ActionBar;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.AppCompatActivity;
import miuix.appcompat.app.ProgressDialog;
import miuix.appcompat.app.floatingactivity.multiapp.MultiAppFloatingActivitySwitcher;
import miuix.preference.DropDownPreference;

/* JADX INFO: loaded from: classes.dex */
public class MobileNetworkSettings extends BaseActivity {
    public static final String TAG = "MobileNetworkSettings";
    public static final String VICE_SLOT_VOLTE_DATA_ENABLED;
    private static boolean mIsSupporteGoogleLPAeSim = MiuiPhoneUtils.isSupportGoogleLPAEsim();
    private static final int[] SMALL_SIM_SLOT_ICON = {R.drawable.small_sim_icon_slot_1, R.drawable.small_sim_icon_slot_2};

    static {
        VICE_SLOT_VOLTE_DATA_ENABLED = (ModemProvider.IS_MTK || ModemProvider.IS_XRING) ? "data_service_enabled" : "vice_slot_volte_data_enabled";
    }

    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            loadFragment(new MobileNetworkSettingsFragment());
        }
    }

    @Override // com.android.phone.settings.BaseActivity, android.app.Activity, android.view.ContextThemeWrapper, android.content.ContextWrapper, android.content.Context
    public void setTheme(int i) {
        if (MiuiPhoneUtils.isDeviceSupportFold() && !MiuiPhoneUtils.isSupportSplit()) {
            super.setTheme(R.style.Miui_Theme_Settings);
        } else {
            super.setTheme(i);
        }
    }

    @Override // com.android.phone.settings.BaseActivity
    protected void setActionBar() {
        super.setActionBar();
        ActionBar appCompatActionBar = getAppCompatActionBar();
        if (appCompatActionBar != null) {
            appCompatActionBar.setDisplayHomeAsUpEnabled(!(MiuiPhoneUtils.isSupportSplit() && (getIntent().getMiuiFlags() & 4) != 0));
            if (!MiuiPhoneUtils.isMultiSimEnabled() || TelephonyManager.isCustSingleSimDevice()) {
                return;
            }
            appCompatActionBar.setTitle(getResources().getString(R.string.network_settings_title));
            setTitle(R.string.network_settings_title);
        }
    }

    public class MobileNetworkSettingsFragment extends BaseActivity.BaseFragment implements Preference.OnPreferenceChangeListener, SubscriptionManager.OnSubscriptionsChangedListener, DefaultSimManager.DataSlotListener {
        private static boolean isDisableEsimFail = false;
        private static boolean isEnableEsimFail = false;
        private static boolean isNeedRecoveryForResume = false;
        private CheckBoxPreference mButton5gHostedModeEnabled;
        private CheckBoxPreference mButtonDataEnabled;
        private RestrictedPreference mButtonDataRoam;
        private CheckBoxPreference mButtonDataWarning;
        private CheckBoxPreference mButtonDual4G;
        private CheckBoxPreference mButtonEnableEsim;
        private RestrictedPreference mButtonEnterBusinessHall;
        private CheckBoxPreference mButtonIntelligentDualSim;
        private CheckBoxPreference mButtonMmsEnabled;
        private RestrictedPreference mButtonOfflineCall;
        private CheckBoxPreference mButtonSaEnabled;
        private CheckBoxPreference mButtonSmartDualSim;
        private CheckBoxPreference mButtonSmartSwitchDualSim;
        private CheckBoxPreference mButtonUseLastSimCallBack;
        private CheckBoxPreference mButtonViceSlotVolteData;
        private CdmaOptions mCdmaOptions;
        private RestrictedCheckBoxPreference mCheckboxDataRoam;
        private Preference mClickedPreference;
        private ConnectivityManager mConnectManager;
        private ContentObserver mContentObserver;
        private CountDownTimer mCountdownTimer;
        private int mDefaultAlwaysEnableMMS;
        private DefaultSimPreference mDefaultSimPre;
        private AlertDialog mEsimSwitchDialog;
        private MiuixTextPreference mFiveGPreference;
        private GsmUmtsOptions mGsmUmtsOptions;
        private IntentFilter mIntentFilter;
        private Preference mLteDataServicePref;
        private PreferenceCategory mOtherMobileSettings;
        private Phone mPhone;
        private PreferenceCategory mPrefChinaMobilePalm;
        private PreferenceCategory mPrefDefault;
        private DropDownPreference mPrefDefaultData;
        private PreferenceCategory mPrefDefaultSim;
        private DropDownPreference mPrefDefaultVoice;
        private MiuixTextPreference mPrefEsimManager;
        private MiuixTextPreference mPrefEsimServiceSupport;
        private PreferenceCategory mPrefEsimSetting;
        private PreferenceCategory mPrefGeneral;
        private Preference mPrefMiMobile;
        private Preference mPrefMiRoaming;
        private Preference mPrefMiSim;
        private MultiSimDropDownPreference mPrefSMS;
        private PreferenceCategory mPrefSimInfo;
        private PreferenceCategory mPrefSimService;
        private PreferenceCategory mPrefSimVoiceDataInfo;
        private PreferenceCategory mPrefWifiCall;
        private PreferenceScreen mPreferenceScreen;
        private ProgressDialog mProgressDialog;
        private SettingsObserver mSettingsObserver;
        private MultiSimPreference mSimInfoPre;
        private SmartCFSwitchView mSmartCFSwitchView;
        private VolteSwitchView mVolteSwitchView;
        private Bundle mVsimBundle;
        private WifiCallSwitchView mWifiCallSwitchView;
        private static final Handler mHandler = new Handler();
        private static boolean isNeedExecuteDismissRunner = false;
        private final int ESIM_REQUEST_SUCCESS = 0;
        private AlertDialog mViceSlotVolteDataPromptDialog = null;
        private AlertDialog mSwitchDataSimPromptDialog = null;
        private AlertDialog mDataUsagePromptDialog = null;
        private AlertDialog mSmartDualSimPromptDialog = null;
        private AlertDialog mSmartSwitchDualSimPromptDialog = null;
        private List mSimInfoRecordList = new ArrayList();
        private int mSimNum = 0;
        private int mActiveSimCount = 0;
        private boolean mIsAirplaneEnabled = false;
        private boolean mIsSimInserted = true;
        private boolean mIsShowPrefSMSSettings = false;
        private MobileNetworkSettingsFragmentHandler mMobileNetworkSettingsFragmentHandler = null;
        private boolean[] mIsSimActiveAndCarrierConfigOk = new boolean[MiuiPhoneUtils.PHONE_COUNT];
        private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                android.telephony.TelephonyManager telephonyManager;
                String action = intent.getAction();
                String str = MobileNetworkSettings.TAG;
                Log.i(str, "onReceive action " + action);
                int i = 0;
                if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                    MobileNetworkSettingsFragment.this.mIsAirplaneEnabled = intent.getBooleanExtra(MEModuleSignalT1s.EXTRA_STATE, false);
                    MobileNetworkSettingsFragment.this.updatePreferenceUI();
                    MobileNetworkSettingsFragment.this.updateFiveGHoseModeButton();
                    MobileNetworkSettingsFragment.this.mPrefEsimSetting.setEnabled(!MobileNetworkSettingsFragment.this.mIsAirplaneEnabled);
                    return;
                }
                if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                    String stringExtra = intent.getStringExtra("ss");
                    Log.d(str, "Received ACTION_SIM_STATE_CHANGED: " + stringExtra);
                    MobileNetworkSettingsFragment mobileNetworkSettingsFragment = MobileNetworkSettingsFragment.this;
                    mobileNetworkSettingsFragment.mIsSimInserted = mobileNetworkSettingsFragment.isSimInserted();
                    MobileNetworkSettingsFragment.this.updatePreferenceUI();
                    MobileNetworkSettingsFragment.this.updateFiveGHoseModeButton();
                    if (TelephonyManager.isCustSingleSimDevice() && SubscriptionManager.getSlotIdExtra(intent, SubscriptionManager.INVALID_SLOT_ID) == 1) {
                        Log.d(str, "skip slot 2 sim state change for cust single sim device.");
                        return;
                    }
                    if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getNeedShowNotificationValue() == 1 && (telephonyManager = (android.telephony.TelephonyManager) context.getSystemService("phone")) != null && ((telephonyManager.getSimState(1) == 5 || telephonyManager.getSimState(1) == 2 || telephonyManager.getSimState(1) == 3) && ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isEsimActive() && MobileNetworkSettingsFragment.isNeedExecuteDismissRunner)) {
                        Log.d(str, "Execute mDismissRunner is for enable O2 eSIM");
                        MobileNetworkSettingsFragment.mHandler.removeCallbacks(MobileNetworkSettingsFragment.this.mDismissRunner);
                        MobileNetworkSettingsFragment.mHandler.post(MobileNetworkSettingsFragment.this.mDismissRunner);
                        MobileNetworkSettingsFragment.isNeedExecuteDismissRunner = false;
                    }
                    if (!MiuiPhoneUtils.isMultiSimEnabled() || TelephonyManager.isCustSingleSimDevice()) {
                        MobileNetworkSettingsFragment.this.setPreferenceScreenEnabled(("ABSENT".equals(stringExtra) || MobileNetworkSettingsFragment.this.mIsAirplaneEnabled) ? false : true);
                    }
                    MobileNetworkSettingsFragment.this.updateDual4gButton(stringExtra);
                    MobileNetworkSettingsFragment.this.updateDataEnabledButton(stringExtra);
                    MobileNetworkSettingsFragment.this.updateChinaMobilePalmView();
                    int intExtra = intent.getIntExtra("phone", SubscriptionManager.INVALID_SLOT_ID);
                    Log.d(str, "phoneId=" + intExtra);
                    if ("ABSENT".equals(stringExtra)) {
                        MobileNetworkSettingsFragment.this.initIntelligentDualSimView();
                        if (MiuiPhoneUtils.isValidatePhoneId(intExtra)) {
                            MobileNetworkSettingsFragment.this.mIsSimActiveAndCarrierConfigOk[intExtra] = false;
                        }
                    }
                    if (!Utils.IS_MIUI_LITE_DEVICE || MobileNetworkSettingsFragment.this.mPrefDefaultVoice == null || MobileNetworkSettingsFragment.this.mPrefDefaultData == null) {
                        return;
                    }
                    MobileNetworkSettingsFragment.this.updateDropDownList();
                    return;
                }
                if ("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED".equals(action)) {
                    if (MobileNetworkSettingsFragment.this.mDefaultSimPre != null) {
                        MobileNetworkSettingsFragment.this.mDefaultSimPre.notifySimDataChanged();
                    }
                    if (Utils.IS_MIUI_LITE_DEVICE && MobileNetworkSettingsFragment.this.mPrefDefaultVoice != null && MobileNetworkSettingsFragment.this.mPrefDefaultData != null) {
                        MobileNetworkSettingsFragment.this.updateDropDownList();
                    }
                    MobileNetworkSettingsFragment.this.updateDual4gButton("LOADED");
                    MobileNetworkSettingsFragment.this.updateDataEnabledButton("LOADED");
                    if (MiuiPhoneUtils.getInsertedSimCount() <= 1) {
                        MobileNetworkSettingsFragment.this.initIntelligentDualSimView();
                        return;
                    }
                    while (true) {
                        int length = MobileNetworkSettingsFragment.this.mIsSimActiveAndCarrierConfigOk.length;
                        MobileNetworkSettingsFragment mobileNetworkSettingsFragment2 = MobileNetworkSettingsFragment.this;
                        if (i < length) {
                            if (!mobileNetworkSettingsFragment2.mIsSimActiveAndCarrierConfigOk[i]) {
                                return;
                            } else {
                                i++;
                            }
                        } else {
                            mobileNetworkSettingsFragment2.initIntelligentDualSimView();
                            return;
                        }
                    }
                } else {
                    if ("android.intent.action.ACTION_DEFAULT_VOICE_SUBSCRIPTION_CHANGED".equals(action)) {
                        if (MobileNetworkSettingsFragment.this.mDefaultSimPre != null) {
                            MobileNetworkSettingsFragment.this.mDefaultSimPre.notifySimDataChanged();
                            return;
                        }
                        return;
                    }
                    if ("android.telephony.action.DEFAULT_SMS_SUBSCRIPTION_CHANGED".equals(action)) {
                        int defaultSmsSlotId = SubscriptionManager.getDefault().getDefaultSmsSlotId();
                        MobileNetworkSettingsFragment mobileNetworkSettingsFragment3 = MobileNetworkSettingsFragment.this;
                        mobileNetworkSettingsFragment3.setPreferenceSummary(mobileNetworkSettingsFragment3.mPrefSMS, defaultSmsSlotId);
                        if (MobileNetworkSettingsFragment.this.mDefaultSimPre != null) {
                            MobileNetworkSettingsFragment.this.mDefaultSimPre.notifySimDataChanged();
                            return;
                        }
                        return;
                    }
                    if ("miui.intent.action.OMACP_VOWIFI_ENABLED".equals(action)) {
                        int i2 = SubscriptionManager.INVALID_SUBSCRIPTION_ID;
                        int intExtra2 = intent.getIntExtra("subscription", i2);
                        if (intExtra2 == i2) {
                            Rlog.d(str, "ACTION_OMACP_VOWIFI_ENABLED sub is invalid return!!!");
                            return;
                        }
                        boolean booleanExtra = intent.getBooleanExtra("vowifi_enabled", false);
                        MiuiImsPhoneUtils.saveOmacpWifiState(intExtra2, booleanExtra);
                        MobileNetworkSettingsFragment.this.updateWifiCallSwitchView();
                        Rlog.d(str, "ACTION_OMACP_VOWIFI_ENABLED subId:" + intExtra2 + " enabled:" + booleanExtra);
                        return;
                    }
                    if ("android.telephony.action.CARRIER_CONFIG_CHANGED".equals(action)) {
                        int intExtra3 = intent.getIntExtra("android.telephony.extra.SLOT_INDEX", SubscriptionManager.INVALID_SLOT_ID);
                        int intExtra4 = intent.getIntExtra("android.telephony.extra.SUBSCRIPTION_INDEX", SubscriptionManager.INVALID_SUBSCRIPTION_ID);
                        Log.d(str, "ACTION_CARRIER_CONFIG_CHANGED, phoneId=" + intExtra3);
                        MobileNetworkSettingsFragment.this.getSimInfo();
                        SubscriptionInfo subInfo = MobileNetworkSettingsFragment.this.getSubInfo(intExtra3);
                        if (MiuiPhoneUtils.isValidatePhoneId(intExtra3) && SubscriptionManager.isValidSubscriptionId(intExtra4) && subInfo != null && subInfo.isActivated()) {
                            MobileNetworkSettingsFragment.this.mIsSimActiveAndCarrierConfigOk[intExtra3] = true;
                        } else if (MiuiPhoneUtils.isValidatePhoneId(intExtra3)) {
                            MobileNetworkSettingsFragment.this.mIsSimActiveAndCarrierConfigOk[intExtra3] = false;
                        }
                        Log.d(str, "sim count=" + MiuiPhoneUtils.getInsertedSimCount());
                        if (MiuiPhoneUtils.getInsertedSimCount() <= 1) {
                            MobileNetworkSettingsFragment.this.initIntelligentDualSimView();
                            return;
                        }
                        while (true) {
                            int length2 = MobileNetworkSettingsFragment.this.mIsSimActiveAndCarrierConfigOk.length;
                            MobileNetworkSettingsFragment mobileNetworkSettingsFragment4 = MobileNetworkSettingsFragment.this;
                            if (i < length2) {
                                if (!mobileNetworkSettingsFragment4.mIsSimActiveAndCarrierConfigOk[i]) {
                                    return;
                                } else {
                                    i++;
                                }
                            } else {
                                mobileNetworkSettingsFragment4.initIntelligentDualSimView();
                                return;
                            }
                        }
                    } else {
                        if (action.equals("android.net.wifi.WIFI_AP_STATE_CHANGED")) {
                            int intExtra5 = intent.getIntExtra("wifi_state", 0);
                            if (intExtra5 == 11 || intExtra5 == 13) {
                                MobileNetworkSettingsFragment.this.updateWifiCallSwitchView();
                                return;
                            }
                            return;
                        }
                        if (action.equals("action_pad_enter_lpa")) {
                            boolean booleanExtra2 = intent.getBooleanExtra("isAutoEnter", false);
                            Intent intent2 = new Intent();
                            intent2.setAction("android.telephony.euicc.action.MANAGE_EMBEDDED_SUBSCRIPTIONS");
                            intent2.putExtra("isAutoEnter", booleanExtra2);
                            if (MobileNetworkSettingsFragment.this.getActivity() instanceof AppCompatActivity) {
                                MultiAppFloatingActivitySwitcher.configureFloatingService(intent2, (AppCompatActivity) MobileNetworkSettingsFragment.this.getActivity());
                            }
                            MobileNetworkSettingsFragment.this.startActivity(intent2);
                        }
                    }
                }
            }
        };
        private final Runnable mDismissRunner = new Runnable() { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.17
            @Override // java.lang.Runnable
            public void run() {
                MobileNetworkSettingsFragment.this.dismissProgressDialog();
                Context context = MobileNetworkSettingsFragment.this.getContext() != null ? MobileNetworkSettingsFragment.this.getContext() : ActivityThread.currentApplication();
                if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getEsimSecureValue() == 1 && "".equals(((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getProductEid()) && ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getEidFromSlotId(1) != null) {
                    ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).writeProductEid(HexDump.hexStringToByteArray(((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getEidFromSlotId(1)));
                }
                if (MobileNetworkSettingsFragment.isEnableEsimFail) {
                    MobileNetworkSettingsFragment.this.mButtonEnableEsim.setChecked(false);
                    if (!TelephonyManagerEx.getDefault().isPlatform8750() && !TelephonyManagerEx.getDefault().isPlatform7635()) {
                        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setEsimSecureValue(1);
                    }
                    Toast.makeText(context, R.string.enable_esim_error_toast_message, 0).show();
                } else if (MobileNetworkSettingsFragment.isDisableEsimFail) {
                    MobileNetworkSettingsFragment.this.mButtonEnableEsim.setChecked(true);
                    if (!TelephonyManagerEx.getDefault().isPlatform8750() && !TelephonyManagerEx.getDefault().isPlatform7635()) {
                        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setEsimSecureValue(0);
                    }
                    Toast.makeText(context, R.string.disable_esim_error_toast_message, 0).show();
                }
                if (MiuiPhoneUtils.isSupportMep() && !MobileNetworkSettingsFragment.isEnableEsimFail && !MobileNetworkSettingsFragment.isDisableEsimFail && ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getEsimSecureValue() == 1) {
                    if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).shouldShowOsuDialog(Settings.Secure.getLong(context.getContentResolver(), "osu_interval_time", -1L))) {
                        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).showOsuDialog();
                    } else {
                        MiuiPhoneUtils.enterEsimManager(true);
                    }
                }
                MobileNetworkSettingsFragment.isEnableEsimFail = false;
                MobileNetworkSettingsFragment.isDisableEsimFail = false;
                ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setEsimStateChanging(false);
                MobileNetworkSettingsFragment.this.updatePreferenceUI();
            }
        };
        ContentObserver mEsimEnabledObserver = new ContentObserver(mHandler) { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.31
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                MobileNetworkSettingsFragment.this.updateEsimSettingView();
            }
        };

        class MobileNetworkSettingsFragmentHandler extends Handler {
            MobileNetworkSettingsFragmentHandler(Looper looper) {
                super(looper);
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what != 0) {
                    return;
                }
                MobileNetworkSettingsFragment.this.updateIntelligentDualSimButton();
            }
        }

        private void initSimActiveAndCarrierConfigOk() {
            getSimInfo();
            for (int i = 0; i < this.mIsSimActiveAndCarrierConfigOk.length; i++) {
                SubscriptionInfo subInfo = getSubInfo(i);
                if (MiuiPhoneUtils.isValidatePhoneId(i) && subInfo != null && subInfo.isActivated()) {
                    this.mIsSimActiveAndCarrierConfigOk[i] = true;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateEsimSettingView() {
            if (this.mPreferenceScreen == null || this.mPrefEsimSetting == null) {
                return;
            }
            if (ModuleDelegate.get(IUtilityModule.class) != null) {
                ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).updateCloudEsimCountryList("esimview");
            }
            if (MiuiPhoneUtils.isEsimCapable() && ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isSupportForRegion()) {
                if (isNeedRecoveryForResume) {
                    new Thread(new Runnable(this) { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.2
                        @Override // java.lang.Runnable
                        public void run() {
                            try {
                                ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).recoveryForInit();
                            } catch (Exception e) {
                                Log.d(MobileNetworkSettings.TAG, "updateEsimSettingView() has error: " + e);
                            }
                        }
                    }).start();
                    isNeedRecoveryForResume = false;
                }
                this.mPreferenceScreen.addPreference(this.mPrefEsimSetting);
                if (!((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isRemoveEsimSwitch()) {
                    int esimSecureValue = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getEsimSecureValue();
                    PreferenceCategory preferenceCategory = this.mPrefEsimSetting;
                    if (esimSecureValue == 1) {
                        preferenceCategory.addPreference(this.mPrefEsimManager);
                        boolean zShowEsimSupportedCarriersSetting = MiuiPhoneUtils.showEsimSupportedCarriersSetting();
                        PreferenceCategory preferenceCategory2 = this.mPrefEsimSetting;
                        if (zShowEsimSupportedCarriersSetting) {
                            preferenceCategory2.addPreference(this.mPrefEsimServiceSupport);
                        } else {
                            preferenceCategory2.removePreference(this.mPrefEsimServiceSupport);
                        }
                        this.mButtonEnableEsim.setChecked(true);
                        return;
                    }
                    preferenceCategory.removePreference(this.mPrefEsimManager);
                    this.mPrefEsimSetting.removePreference(this.mPrefEsimServiceSupport);
                    this.mButtonEnableEsim.setChecked(false);
                    return;
                }
                this.mPrefEsimSetting.removePreference(this.mButtonEnableEsim);
                if (MiuiPhoneUtils.showEsimSupportedCarriersSetting()) {
                    return;
                }
                this.mPrefEsimSetting.removePreference(this.mPrefEsimServiceSupport);
                return;
            }
            this.mPreferenceScreen.removePreference(this.mPrefEsimSetting);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateWifiCallSwitchView() {
            if (this.mPreferenceScreen == null || this.mPrefWifiCall == null) {
                return;
            }
            if (!MiuiImsPhoneUtils.showWifiCallSwitchView(this.mPhone)) {
                this.mPreferenceScreen.removePreference(this.mPrefWifiCall);
                return;
            }
            if (!MiuiPhoneUtils.DUAL_VOLTE_SUPPORTED && ImsAdapterBase.getInstance().isWfcEnabledByPlatform(getContext(), this.mPhone.getPhoneId()) && !ImsAdapterBase.getInstance().hideWfc(getContext(), this.mPhone.getSubId())) {
                this.mPreferenceScreen.addPreference(this.mPrefWifiCall);
                if (this.mWifiCallSwitchView == null) {
                    this.mWifiCallSwitchView = new WifiCallSwitchView(this, this.mPrefWifiCall, this.mPhone);
                }
                this.mWifiCallSwitchView.updateWifiCallPreferredUI();
                return;
            }
            this.mPreferenceScreen.removePreference(this.mPrefWifiCall);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setPreferenceScreenEnabled(boolean z) {
            MultiSimPreference multiSimPreference = this.mSimInfoPre;
            boolean z2 = false;
            if (multiSimPreference != null) {
                multiSimPreference.setIsEnabled(!this.mIsAirplaneEnabled || MiuiImsPhoneUtils.hasSimSupportWfc());
                this.mDefaultSimPre.setIsEnabled(z || MiuiImsPhoneUtils.hasSimSupportWfc());
            }
            this.mPrefGeneral.setEnabled(z);
            this.mPrefDefault.setEnabled(z);
            PreferenceCategory preferenceCategory = this.mPrefSimService;
            if (preferenceCategory != null) {
                preferenceCategory.setEnabled(true);
            }
            PreferenceCategory preferenceCategory2 = this.mPrefWifiCall;
            if (preferenceCategory2 != null) {
                preferenceCategory2.setEnabled(z || MiuiImsPhoneUtils.shouldShowWfcOptionWhenAirplaneOn(MiuiPhoneUtils.getDataPhone()));
            }
            PreferenceCategory preferenceCategory3 = this.mPrefChinaMobilePalm;
            if (preferenceCategory3 != null) {
                preferenceCategory3.setEnabled(z);
            }
            GsmUmtsOptions gsmUmtsOptions = this.mGsmUmtsOptions;
            if (gsmUmtsOptions != null) {
                gsmUmtsOptions.setOptionEnabled(z);
            }
            CdmaOptions cdmaOptions = this.mCdmaOptions;
            if (cdmaOptions != null) {
                cdmaOptions.setOptionEnabled(z);
            }
            PreferenceCategory preferenceCategory4 = this.mOtherMobileSettings;
            if (preferenceCategory4 != null) {
                preferenceCategory4.setEnabled(true);
                Context currentContext = getCurrentContext();
                if (this.mButtonDataRoam != null && !SimpleFeatures.checkDataRoamingRestriction(currentContext)) {
                    this.mButtonDataRoam.setEnabled(z);
                }
                RestrictedPreference restrictedPreference = this.mButtonOfflineCall;
                if (restrictedPreference != null) {
                    restrictedPreference.setEnabled(true);
                }
            }
            DropDownPreference dropDownPreference = this.mPrefDefaultVoice;
            if (dropDownPreference != null) {
                dropDownPreference.setEnabled(z && this.mActiveSimCount >= 2);
                DropDownPreference dropDownPreference2 = this.mPrefDefaultData;
                if (z && this.mActiveSimCount >= 2) {
                    z2 = true;
                }
                dropDownPreference2.setEnabled(z2);
            }
        }

        protected Context getCurrentContext() {
            Context context = getContext();
            return context != null ? context : ActivityThread.currentApplication();
        }

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, miuix.preference.PreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.preference.PreferenceManager.OnPreferenceTreeClickListener
        public boolean onPreferenceTreeClick(Preference preference) {
            CheckBoxPreference checkBoxPreference;
            if (preference != null) {
                HashMap map = new HashMap();
                map.put("mobile_network_settings_click_preference", preference.getKey());
                MiStatInterfaceUtil.recordCountEvent("event_settings", "key_mobile_network_settings_preference", map);
            }
            AppCompatActivity appCompatActivity = getActivity() instanceof AppCompatActivity ? (AppCompatActivity) getActivity() : null;
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
            if (preference == this.mButtonDataEnabled) {
                if (isHighTemperatureProtect()) {
                    this.mButtonDataEnabled.setChecked(false);
                    Toast.makeText(getActivity(), getResources().getString(R.string.data_for_high_temperature_toast), 1).show();
                    return false;
                }
                if (MiuiAisSimLockManager.getInstance() != null && MiuiAisSimLockManager.getInstance().isRestricted(MiuiPhoneUtils.getDataPhone()) && this.mButtonDataEnabled.isChecked()) {
                    Log.i(MobileNetworkSettings.TAG, "mButtonDataEnabled show Simlock dialog");
                    MiuiAisSimLockManager.getInstance().showSimLockDialog(MiuiPhoneUtils.getDataPhone());
                }
                if (MiuiKoreaOperatorCustimizedUtils.isCustForKrOps()) {
                    showDataUsagePromptDialog(this.mButtonDataEnabled);
                } else {
                    android.telephony.TelephonyManager.from(getContext()).setDataEnabled(this.mButtonDataEnabled.isChecked());
                    MiStatInterfaceUtil.recordSwitchStatusEvent(preference.getKey(), this.mButtonDataEnabled.isChecked());
                }
                return true;
            }
            CheckBoxPreference checkBoxPreference2 = this.mButton5gHostedModeEnabled;
            if (preference == checkBoxPreference2) {
                boolean zIsChecked = checkBoxPreference2.isChecked();
                if (MiuiPhoneUtils.isDuringCall()) {
                    Log.d(MobileNetworkSettings.TAG, "isInCall, cannot switch mButton5gHostedModeEnabled");
                    this.mButton5gHostedModeEnabled.setChecked(!zIsChecked);
                    Toast.makeText(getContext(), R.string.msim_set_sub_not_supported_phone_in_call, 0).show();
                    return false;
                }
                Log.d(MobileNetworkSettings.TAG, "enable5gHostMode requested: " + zIsChecked);
                FiveGManagerBase.getInstance().setUserFiveGHostedModeEnabled(zIsChecked);
                return true;
            }
            CheckBoxPreference checkBoxPreference3 = this.mButtonSmartSwitchDualSim;
            if (preference == checkBoxPreference3) {
                boolean zIsChecked2 = checkBoxPreference3.isChecked();
                if (MiuiPhoneUtils.isDuringCall()) {
                    Log.d(MobileNetworkSettings.TAG, "isInCall, cannot switch mButtonSmartSwitchDualSim, isChecked: " + zIsChecked2);
                    this.mButtonSmartSwitchDualSim.setChecked(zIsChecked2 ^ true);
                    Toast.makeText(getContext(), R.string.msim_set_sub_not_supported_phone_in_call, 0).show();
                    return false;
                }
                String str = MobileNetworkSettings.TAG;
                Log.d(str, "smartswitchdualsim isChecked: " + zIsChecked2);
                if (zIsChecked2) {
                    alertSmartSwitchDualSimPrompt();
                } else {
                    Settings.Global.putInt(getContext().getApplicationContext().getContentResolver(), "smart_dual_sim", 0);
                    Settings.Global.putInt(getContext().getApplicationContext().getContentResolver(), MobileNetworkSettings.VICE_SLOT_VOLTE_DATA_ENABLED, 0);
                    MiStatInterfaceUtil.recordSwitchStatusEvent(preference.getKey(), false);
                    Log.d(str, "set smartswitchdualsim enable false");
                }
                return true;
            }
            CheckBoxPreference checkBoxPreference4 = this.mButtonSmartDualSim;
            if (preference == checkBoxPreference4) {
                if (checkBoxPreference4.isChecked()) {
                    alertSmartDualSimPrompt();
                } else {
                    Settings.Global.putInt(getContext().getApplicationContext().getContentResolver(), "smart_dual_sim", 0);
                    Log.d(MobileNetworkSettings.TAG, "set smartdualsim enable false");
                }
                return true;
            }
            CheckBoxPreference checkBoxPreference5 = this.mButtonViceSlotVolteData;
            if (preference == checkBoxPreference5) {
                boolean zIsChecked3 = checkBoxPreference5.isChecked();
                if (MiuiPhoneUtils.isDuringCall()) {
                    Log.d(MobileNetworkSettings.TAG, "isInCall, cannot switch mButtonViceSlotVolteData, isChecked: " + zIsChecked3);
                    this.mButtonViceSlotVolteData.setChecked(zIsChecked3 ^ true);
                    Toast.makeText(getContext(), R.string.msim_set_sub_not_supported_phone_in_call, 0).show();
                    return false;
                }
                String str2 = MobileNetworkSettings.TAG;
                Log.d(str2, "ViceSlotVolteData isChecked: " + zIsChecked3);
                if (zIsChecked3) {
                    AlertDialog alertDialogCreate = new AlertDialog.Builder(getActivity()).setMessage(R.string.vice_slot_volte_data_switch_prompt).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.4
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Settings.Global.putInt(MobileNetworkSettingsFragment.this.getContext().getContentResolver(), MobileNetworkSettings.VICE_SLOT_VOLTE_DATA_ENABLED, 1);
                            MiStatInterfaceUtil.recordSwitchStatusEvent(MobileNetworkSettingsFragment.this.mButtonViceSlotVolteData.getKey(), true);
                            Log.d(MobileNetworkSettings.TAG, "set ViceSlotVolteData enable true");
                        }
                    }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.3
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MobileNetworkSettingsFragment.this.mButtonViceSlotVolteData.setChecked(false);
                        }
                    }).setCancelable(false).create();
                    this.mViceSlotVolteDataPromptDialog = alertDialogCreate;
                    alertDialogCreate.show();
                    Log.d(str2, "show ViceSlotVolteData enable prompt dialog");
                } else {
                    Settings.Global.putInt(getContext().getApplicationContext().getContentResolver(), MobileNetworkSettings.VICE_SLOT_VOLTE_DATA_ENABLED, 0);
                    MiStatInterfaceUtil.recordSwitchStatusEvent(preference.getKey(), false);
                    Log.d(str2, "set ViceSlotVolteData enable false");
                }
                return true;
            }
            CheckBoxPreference checkBoxPreference6 = this.mButtonIntelligentDualSim;
            if (preference == checkBoxPreference6) {
                if (checkBoxPreference6 == null) {
                    return false;
                }
                boolean zIsChecked4 = checkBoxPreference6.isChecked();
                if (MiuiPhoneUtils.isDuringCall()) {
                    Log.d(MobileNetworkSettings.TAG, "isInCall, cannot switch mButtonIntelligentDualSim, isChecked: " + zIsChecked4);
                    this.mButtonIntelligentDualSim.setChecked(zIsChecked4 ^ true);
                    Toast.makeText(getContext(), R.string.msim_set_sub_not_supported_phone_in_call, 0).show();
                    return false;
                }
                String str3 = MobileNetworkSettings.TAG;
                Log.d(str3, "intelligentdualsim isChecked: " + zIsChecked4);
                if (zIsChecked4) {
                    Settings.Global.putInt(getContext().getContentResolver(), "dual_mobile_data", 1);
                    Settings.Global.putInt(getContext().getContentResolver(), "smart_dual_sim", 1);
                    Settings.Global.putInt(getContext().getContentResolver(), MobileNetworkSettings.VICE_SLOT_VOLTE_DATA_ENABLED, 1);
                    Log.d(str3, "set intelligentdualsim enable true");
                } else {
                    Settings.Global.putInt(getContext().getContentResolver(), "dual_mobile_data", 0);
                    Settings.Global.putInt(getContext().getContentResolver(), "smart_dual_sim", 0);
                    Settings.Global.putInt(getContext().getContentResolver(), MobileNetworkSettings.VICE_SLOT_VOLTE_DATA_ENABLED, 0);
                    Log.d(str3, "set intelligentdualsim enable false");
                }
                MiStatInterfaceUtil.recordSwitchStatusEvent(preference.getKey(), zIsChecked4);
                return true;
            }
            if (preference == this.mLteDataServicePref) {
                String string = Settings.Global.getString(getContext().getContentResolver(), "setup_prepaid_data_service_url");
                if (!TextUtils.isEmpty(string)) {
                    String subscriberId = TelephonyManager.getDefault().getSubscriberId();
                    if (subscriberId == null) {
                        subscriberId = "";
                    }
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(TextUtils.isEmpty(string) ? null : TextUtils.expandTemplate(string, subscriberId).toString()));
                    if (appCompatActivity != null) {
                        MultiAppFloatingActivitySwitcher.configureFloatingService(intent, appCompatActivity);
                    }
                    startActivity(intent);
                } else {
                    Log.e(MobileNetworkSettings.TAG, "Missing SETUP_PREPAID_DATA_SERVICE_URL");
                }
                return true;
            }
            if (preference == this.mButtonMmsEnabled) {
                Settings.System.putInt(PhoneGlobals.getInstance().getContentResolver(), "always_enable_mms", this.mButtonMmsEnabled.isChecked() ? 1 : 0);
                return true;
            }
            if ("pref_mi_sim_key".equals(preference.getKey()) || "pref_sim_roaming_key".equals(preference.getKey())) {
                Intent intentForVirtualSim = Utils.getIntentForVirtualSim(preference.getKey());
                if (appCompatActivity != null) {
                    try {
                        MultiAppFloatingActivitySwitcher.configureFloatingService(intentForVirtualSim, appCompatActivity);
                    } catch (Exception e) {
                        Log.e(MobileNetworkSettings.TAG, "enter mi_sim or roaming service error.", e);
                    }
                }
                startActivity(intentForVirtualSim);
                return true;
            }
            if (preference == this.mButtonDual4G) {
                if (PhoneConstants.State.IDLE != MiuiPhoneUtils.getCallManager().getState()) {
                    Toast.makeText(getContext(), R.string.msim_set_sub_not_supported_phone_in_call, 0).show();
                    CheckBoxPreference checkBoxPreference7 = this.mButtonDual4G;
                    checkBoxPreference7.setChecked(!checkBoxPreference7.isChecked());
                    return false;
                }
                ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setDual4GMode(this.mButtonDual4G.isChecked(), new Utils.Dual4GCallBack() { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.5
                    @Override // com.android.phone.utils.Utils.Dual4GCallBack
                    public void onSetDual4GResult(final boolean z, int i) {
                        Log.d(MobileNetworkSettings.TAG, "onSetDual4GResult enabled=" + z + ", resultCode=" + i);
                        if (i == 0 || MobileNetworkSettingsFragment.this.getActivity() == null) {
                            return;
                        }
                        MobileNetworkSettingsFragment.this.getActivity().runOnUiThread(new Runnable() { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.5.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(MobileNetworkSettingsFragment.this.getContext().getApplicationContext(), z ? R.string.turn_on_dual_4g_failed : R.string.turn_off_dual_4g_failed, 0).show();
                                if (MobileNetworkSettingsFragment.this.mButtonDual4G != null) {
                                    MobileNetworkSettingsFragment.this.mButtonDual4G.setChecked(!z);
                                }
                            }
                        });
                    }
                });
                return true;
            }
            RestrictedCheckBoxPreference restrictedCheckBoxPreference = this.mCheckboxDataRoam;
            if (preference == restrictedCheckBoxPreference) {
                this.mPhone.setDataRoamingEnabled(restrictedCheckBoxPreference.isChecked());
                MiStatInterfaceUtil.recordSwitchStatusEvent(preference.getKey(), this.mCheckboxDataRoam.isChecked());
                return true;
            }
            if (preference == this.mButtonUseLastSimCallBack) {
                MiuiSettings.System.putBoolean(getContext().getContentResolver(), "use_last_sim_callback", this.mButtonUseLastSimCallBack.isChecked());
            } else {
                if (preference == this.mButtonDataRoam) {
                    Intent intent2 = new Intent();
                    if (MiuiKoreaOperatorCustimizedUtils.isCustForKrOps()) {
                        intent2.setClassName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.settings.MiuiDataRoamingSettings");
                    } else {
                        intent2.setClassName("com.miui.securitycenter", "com.miui.networkassistant.ui.activity.InternationalRoamingSettingActivity");
                    }
                    if (appCompatActivity != null) {
                        MultiAppFloatingActivitySwitcher.configureFloatingService(intent2, appCompatActivity);
                    }
                    startActivity(intent2);
                    return true;
                }
                if (preference == this.mButtonOfflineCall) {
                    Log.d(MobileNetworkSettings.TAG, "click ButtonOfflineCall turn to HyperCallActivity");
                    Intent intent3 = new Intent();
                    intent3.setClassName("com.xiaomi.bluetooth", "com.android.bluetooth.ble.app.call.HyperCallActivity");
                    if (appCompatActivity != null) {
                        MultiAppFloatingActivitySwitcher.configureFloatingService(intent3, appCompatActivity);
                    }
                    startActivity(intent3);
                    return true;
                }
                if (preference == this.mButtonDataWarning) {
                    Settings.Global.putInt(getContext().getContentResolver(), "always_alert_data_enable", this.mButtonDataWarning.isChecked() ? 1 : 0);
                } else {
                    MiuixTextPreference miuixTextPreference = this.mFiveGPreference;
                    if (preference == miuixTextPreference) {
                        ScreenDisplayUtils.shouldForceSplit(miuixTextPreference.getIntent(), getActivity().getIntent().getMiuiFlags());
                        return false;
                    }
                    if (preference == this.mButtonEnableEsim) {
                        if (MiuiPhoneUtils.isDuringCall()) {
                            Toast.makeText(getContext(), R.string.set_esim_toast_phone_in_call, 0).show();
                            CheckBoxPreference checkBoxPreference8 = this.mButtonEnableEsim;
                            checkBoxPreference8.setChecked(!checkBoxPreference8.isChecked());
                            return false;
                        }
                        if (this.mButtonEnableEsim.isChecked()) {
                            MiuiPhoneUtils.DualSimType dualSimTypeCheckCurrentDualSimType = MiuiPhoneUtils.checkCurrentDualSimType();
                            if (dualSimTypeCheckCurrentDualSimType == MiuiPhoneUtils.DualSimType.VSIM_SLOT_1 && !MiuiPhoneUtils.isMiRoamingSupportEsim()) {
                                showAlertDialog();
                                this.mButtonEnableEsim.setChecked(false);
                                return true;
                            }
                            showSwtichDialog(0, dualSimTypeCheckCurrentDualSimType);
                        } else {
                            showSwtichDialog(1, MiuiPhoneUtils.DualSimType.OTHER);
                        }
                        return true;
                    }
                    if (preference == this.mButtonEnterBusinessHall) {
                        if (this.mActiveSimCount > 1) {
                            try {
                                Intent intent4 = new Intent(getContext(), (Class<?>) MultiSimSelectListActivity.class);
                                if (appCompatActivity != null) {
                                    MultiAppFloatingActivitySwitcher.configureFloatingService(intent4, appCompatActivity);
                                }
                                startActivity(intent4);
                            } catch (ActivityNotFoundException e2) {
                                Log.e(MobileNetworkSettings.TAG, "enter business hall has error: " + e2);
                            }
                        } else {
                            for (int i = 0; i < this.mSimInfoRecordList.size(); i++) {
                                int slotId = ((SubscriptionInfo) this.mSimInfoRecordList.get(i)).getSlotId();
                                if (MiuiPhoneUtils.isIccCardActivated(slotId)) {
                                    Utils$VirtualSim.enterBusinessHall(getContext(), null, slotId);
                                    break;
                                }
                            }
                        }
                        return true;
                    }
                    if ("button_5g_sa_network_key".equals(preference.getKey()) && (checkBoxPreference = this.mButtonSaEnabled) != null) {
                        boolean zIsChecked5 = checkBoxPreference.isChecked();
                        Rlog.i(MobileNetworkSettings.TAG, "enableSa requested: " + zIsChecked5);
                        if (zIsChecked5) {
                            FiveGManagerBase.getInstance().setUserFiveGNetworkMode(2);
                        } else {
                            FiveGManagerBase.getInstance().setUserFiveGNetworkMode(0);
                        }
                    }
                }
            }
            return false;
        }

        private void alertSmartDualSimPrompt() {
            final int i = DefaultSimManager.getDefaultDataSlotId() == 0 ? 1 : 0;
            final AppCompatActivity appCompatActivity = getActivity() instanceof AppCompatActivity ? (AppCompatActivity) getActivity() : null;
            AlertDialog alertDialogCreate = new AlertDialog.Builder(getActivity()).setTitle(String.format(getString(R.string.smart_dual_sim_prompt_title), Integer.valueOf(i + 1))).setMessage(R.string.smart_dual_sim_prompt).setPositiveButton(R.string.smart_dual_sim_prompt_ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.7
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    Settings.Global.putInt(MobileNetworkSettingsFragment.this.getContext().getContentResolver(), "smart_dual_sim", 1);
                    try {
                        Intent networkAssistantIntent = MobileNetworkSettings.getNetworkAssistantIntent(i);
                        AppCompatActivity appCompatActivity2 = appCompatActivity;
                        if (appCompatActivity2 != null) {
                            MultiAppFloatingActivitySwitcher.configureFloatingService(networkAssistantIntent, appCompatActivity2);
                        }
                        MobileNetworkSettingsFragment.this.startActivity(networkAssistantIntent);
                    } catch (ActivityNotFoundException unused) {
                        Log.d(MobileNetworkSettings.TAG, "transferToTrafficAlert Exception");
                    }
                    Log.d(MobileNetworkSettings.TAG, "set smartdualsim enable true");
                }
            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.6
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    MobileNetworkSettingsFragment.this.mButtonSmartDualSim.setChecked(false);
                }
            }).setCancelable(false).create();
            this.mSmartDualSimPromptDialog = alertDialogCreate;
            alertDialogCreate.show();
            Log.d(MobileNetworkSettings.TAG, "show smartdualsim enable prompt dialog");
        }

        private void alertSmartSwitchDualSimPrompt() {
            final int i = DefaultSimManager.getDefaultDataSlotId() == 0 ? 1 : 0;
            final AppCompatActivity appCompatActivity = getActivity() instanceof AppCompatActivity ? (AppCompatActivity) getActivity() : null;
            int i2 = i + 1;
            AlertDialog alertDialogCreate = new AlertDialog.Builder(getActivity()).setTitle(String.format(getString(R.string.smart_dual_sim_prompt_title), Integer.valueOf(i2))).setMessage(String.format(getString(R.string.smart_switch_dual_sim_prompt), Integer.valueOf(i2))).setPositiveButton(R.string.smart_dual_sim_prompt_ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.9
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i3) {
                    Settings.Global.putInt(MobileNetworkSettingsFragment.this.getContext().getContentResolver(), "smart_dual_sim", 1);
                    Settings.Global.putInt(MobileNetworkSettingsFragment.this.getContext().getContentResolver(), MobileNetworkSettings.VICE_SLOT_VOLTE_DATA_ENABLED, 1);
                    MiStatInterfaceUtil.recordSwitchStatusEvent(MobileNetworkSettingsFragment.this.mButtonSmartSwitchDualSim.getKey(), true);
                    try {
                        Intent networkAssistantIntent = MobileNetworkSettings.getNetworkAssistantIntent(i);
                        AppCompatActivity appCompatActivity2 = appCompatActivity;
                        if (appCompatActivity2 != null) {
                            MultiAppFloatingActivitySwitcher.configureFloatingService(networkAssistantIntent, appCompatActivity2);
                        }
                        MobileNetworkSettingsFragment.this.startActivity(networkAssistantIntent);
                    } catch (ActivityNotFoundException unused) {
                        Log.d(MobileNetworkSettings.TAG, "transferToTrafficAlert Exception");
                    }
                    Log.d(MobileNetworkSettings.TAG, "set smartswitchdualsim enable true");
                }
            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.8
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i3) {
                    MobileNetworkSettingsFragment.this.mButtonSmartSwitchDualSim.setChecked(false);
                }
            }).setCancelable(false).create();
            this.mSmartSwitchDualSimPromptDialog = alertDialogCreate;
            alertDialogCreate.show();
            Log.d(MobileNetworkSettings.TAG, "show smartswitchdualsim enable prompt dialog");
        }

        private void updateSmartSwitchDualSimButton() {
            if (this.mButtonSmartSwitchDualSim != null) {
                boolean z = false;
                if (Settings.Global.getInt(getContext().getContentResolver(), "smart_dual_sim", 0) == 1 && Settings.Global.getInt(getContext().getContentResolver(), MobileNetworkSettings.VICE_SLOT_VOLTE_DATA_ENABLED, 0) == 1) {
                    z = true;
                }
                this.mButtonSmartSwitchDualSim.setChecked(z);
                dismissSmartSwitchDualSimPrompt();
            }
        }

        private void updateViceSlotDataButton() {
            CheckBoxPreference checkBoxPreference = this.mButtonViceSlotVolteData;
            if (checkBoxPreference != null) {
                checkBoxPreference.setChecked(Settings.Global.getInt(getContext().getContentResolver(), MobileNetworkSettings.VICE_SLOT_VOLTE_DATA_ENABLED, 0) == 1);
                dismissViceSlotVolteDataPrompt();
            }
        }

        private void updateSmartDualSimButton() {
            CheckBoxPreference checkBoxPreference = this.mButtonSmartDualSim;
            if (checkBoxPreference != null) {
                checkBoxPreference.setChecked(Settings.Global.getInt(getContext().getContentResolver(), "smart_dual_sim", 0) == 1);
                dismissSmartDualSimPrompt();
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

        private void dismissViceSlotVolteDataPrompt() {
            AlertDialog alertDialog = this.mViceSlotVolteDataPromptDialog;
            if (alertDialog == null || !alertDialog.isShowing()) {
                return;
            }
            this.mViceSlotVolteDataPromptDialog.dismiss();
            this.mViceSlotVolteDataPromptDialog = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateIntelligentDualSimButton() {
            if (this.mButtonIntelligentDualSim == null || getContext() == null || getContext().getContentResolver() == null) {
                return;
            }
            boolean z = false;
            if (Settings.Global.getInt(getContext().getContentResolver(), "dual_mobile_data", 0) == 1 && Settings.Global.getInt(getContext().getContentResolver(), MobileNetworkSettings.VICE_SLOT_VOLTE_DATA_ENABLED, 0) == 1 && Settings.Global.getInt(getContext().getContentResolver(), "smart_dual_sim", 0) == 1) {
                z = true;
            }
            this.mButtonIntelligentDualSim.setChecked(z);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void initIntelligentDualSimView() {
            PreferenceCategory preferenceCategory;
            PreferenceCategory preferenceCategory2;
            if (this.mButtonIntelligentDualSim == null && this.mButtonSmartSwitchDualSim == null && this.mButtonViceSlotVolteData == null && this.mButtonSmartDualSim == null) {
                return;
            }
            if (MiuiPhoneUtils.getInsertedSimCount() < 2 && (preferenceCategory2 = this.mPrefGeneral) != null) {
                preferenceCategory2.removePreference(this.mButtonIntelligentDualSim);
                this.mPrefGeneral.removePreference(this.mButtonViceSlotVolteData);
                this.mPrefGeneral.removePreference(this.mButtonSmartDualSim);
                this.mPrefGeneral.removePreference(this.mButtonSmartSwitchDualSim);
                dismissSmartDualSimPrompt();
                dismissViceSlotVolteDataPrompt();
                dismissSmartSwitchDualSimPrompt();
                Log.d(MobileNetworkSettings.TAG, "inserted one sim card");
                return;
            }
            if (!MiuiPhoneUtils.shouldHideIntelligentDualSimButton() && this.mPrefGeneral != null) {
                this.mSettingsObserver.observe(Settings.Global.getUriFor("dual_mobile_data"), 0);
                this.mPrefGeneral.removePreference(this.mButtonSmartSwitchDualSim);
                this.mPrefGeneral.removePreference(this.mButtonViceSlotVolteData);
                this.mPrefGeneral.removePreference(this.mButtonSmartDualSim);
                dismissSmartSwitchDualSimPrompt();
                dismissSmartDualSimPrompt();
                dismissViceSlotVolteDataPrompt();
                if (this.mPrefGeneral.findPreference("button_intelligent_dual_sim_key") == null) {
                    this.mPrefGeneral.addPreference(this.mButtonIntelligentDualSim);
                    Log.d(MobileNetworkSettings.TAG, "add IntelligentDualSim");
                    return;
                }
                return;
            }
            if (MiuiPhoneUtils.isSmartSwitchDualSimSupported() && (preferenceCategory = this.mPrefGeneral) != null) {
                preferenceCategory.removePreference(this.mButtonIntelligentDualSim);
                if (!MiuiPhoneUtils.shouldHideViceSlotVolteDataButton() && !MiuiPhoneUtils.shouldHideSmartDualSimButton()) {
                    this.mPrefGeneral.removePreference(this.mButtonViceSlotVolteData);
                    this.mPrefGeneral.removePreference(this.mButtonSmartDualSim);
                    dismissViceSlotVolteDataPrompt();
                    dismissSmartDualSimPrompt();
                    if (this.mPrefGeneral.findPreference("button_smart_switch_dual_sim_key") == null) {
                        this.mPrefGeneral.addPreference(this.mButtonSmartSwitchDualSim);
                        Log.d(MobileNetworkSettings.TAG, "add SmartSwitchDualSim");
                        return;
                    }
                    return;
                }
                if (!MiuiPhoneUtils.shouldHideViceSlotVolteDataButton()) {
                    this.mPrefGeneral.removePreference(this.mButtonSmartSwitchDualSim);
                    this.mPrefGeneral.removePreference(this.mButtonSmartDualSim);
                    dismissSmartSwitchDualSimPrompt();
                    dismissSmartDualSimPrompt();
                    if (this.mPrefGeneral.findPreference("button_vice_slot_volte_data") == null) {
                        this.mPrefGeneral.addPreference(this.mButtonViceSlotVolteData);
                        Log.d(MobileNetworkSettings.TAG, "add ViceSlotVolteData");
                        return;
                    }
                    return;
                }
                boolean zShouldHideSmartDualSimButton = MiuiPhoneUtils.shouldHideSmartDualSimButton();
                PreferenceCategory preferenceCategory3 = this.mPrefGeneral;
                if (!zShouldHideSmartDualSimButton) {
                    preferenceCategory3.removePreference(this.mButtonSmartSwitchDualSim);
                    this.mPrefGeneral.removePreference(this.mButtonViceSlotVolteData);
                    dismissViceSlotVolteDataPrompt();
                    dismissSmartSwitchDualSimPrompt();
                    if (this.mPrefGeneral.findPreference("button_smart_dual_sim_key") == null) {
                        this.mPrefGeneral.addPreference(this.mButtonSmartDualSim);
                        Log.d(MobileNetworkSettings.TAG, "add SmartDualSim");
                        return;
                    }
                    return;
                }
                preferenceCategory3.removePreference(this.mButtonSmartDualSim);
                this.mPrefGeneral.removePreference(this.mButtonViceSlotVolteData);
                this.mPrefGeneral.removePreference(this.mButtonSmartSwitchDualSim);
                dismissSmartDualSimPrompt();
                dismissViceSlotVolteDataPrompt();
                dismissSmartSwitchDualSimPrompt();
                Log.d(MobileNetworkSettings.TAG, "hide all dual sim buttons");
                return;
            }
            PreferenceCategory preferenceCategory4 = this.mPrefGeneral;
            if (preferenceCategory4 != null) {
                preferenceCategory4.removePreference(this.mButtonIntelligentDualSim);
                this.mPrefGeneral.removePreference(this.mButtonSmartSwitchDualSim);
                dismissSmartSwitchDualSimPrompt();
                boolean zShouldHideViceSlotVolteDataButton = MiuiPhoneUtils.shouldHideViceSlotVolteDataButton();
                PreferenceCategory preferenceCategory5 = this.mPrefGeneral;
                if (zShouldHideViceSlotVolteDataButton) {
                    preferenceCategory5.removePreference(this.mButtonViceSlotVolteData);
                    dismissViceSlotVolteDataPrompt();
                } else if (preferenceCategory5.findPreference("button_vice_slot_volte_data") == null) {
                    this.mPrefGeneral.addPreference(this.mButtonViceSlotVolteData);
                    Log.d(MobileNetworkSettings.TAG, "add ViceSlotVolteData");
                }
                boolean zShouldHideSmartDualSimButton2 = MiuiPhoneUtils.shouldHideSmartDualSimButton();
                PreferenceCategory preferenceCategory6 = this.mPrefGeneral;
                if (zShouldHideSmartDualSimButton2) {
                    preferenceCategory6.removePreference(this.mButtonSmartDualSim);
                    dismissSmartDualSimPrompt();
                } else if (preferenceCategory6.findPreference("button_smart_dual_sim_key") == null) {
                    this.mPrefGeneral.addPreference(this.mButtonSmartDualSim);
                    Log.d(MobileNetworkSettings.TAG, "add SmartDualSim");
                }
            }
        }

        private void showSwtichDialog(final int i, MiuiPhoneUtils.DualSimType dualSimType) {
            AlertDialog alertDialog = this.mEsimSwitchDialog;
            if (alertDialog == null || !alertDialog.isShowing()) {
                String swtichDialogMessage = getSwtichDialogMessage(i, dualSimType);
                String string = getResources().getString(i == 0 ? R.string.enable_esim_dialog_title : R.string.disable_esim_dialog_title);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(string).setMessage(swtichDialogMessage).setCancelable(false).setPositiveButton(R.string.switch_confirm_btn_text, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.14
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        MobileNetworkSettingsFragment.this.showProgressDialog(i == 0 ? R.string.enable_esim_progress_activating_message : R.string.disable_esim_progress_activating_message);
                        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setEsimStateChanging(true);
                        final boolean zIsDualEsimActive = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isDualEsimActive();
                        new AsyncTask() { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.14.1
                            /* JADX INFO: Access modifiers changed from: protected */
                            @Override // android.os.AsyncTask
                            public Boolean doInBackground(Void... voidArr) {
                                String str = MobileNetworkSettings.TAG;
                                Log.d(str, "doInBackground...");
                                Log.d(str, "setEsimState...");
                                return Boolean.valueOf(((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setEsimState(i) == 0);
                            }

                            /* JADX INFO: Access modifiers changed from: protected */
                            @Override // android.os.AsyncTask
                            public void onPostExecute(Boolean bool) {
                                super.onPostExecute(bool);
                                String str = MobileNetworkSettings.TAG;
                                Log.d(str, "onPostExecute... aBoolean = " + bool);
                                if (!bool.booleanValue()) {
                                    if (i == 0) {
                                        MobileNetworkSettingsFragment.isEnableEsimFail = true;
                                        MobileNetworkSettingsFragment.this.mButtonEnableEsim.setChecked(false);
                                    } else {
                                        MobileNetworkSettingsFragment.isDisableEsimFail = true;
                                        MobileNetworkSettingsFragment.this.mButtonEnableEsim.setChecked(true);
                                    }
                                } else if ((ModemProvider.IS_MTK || ModemProvider.IS_XRING) && MiuiPhoneUtils.isSupportMep() && i == 1 && zIsDualEsimActive) {
                                    ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).resetPortAndStateForSlot(1);
                                }
                                MobileNetworkSettingsFragment.this.updateEsimSettingView();
                                if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getNeedShowNotificationValue() != 1 || i == 1 || MiuiPhoneUtils.isSupportMep()) {
                                    Log.d(str, "Execute mDismissRunner is for eSIM Switch");
                                    MobileNetworkSettingsFragment.mHandler.removeCallbacks(MobileNetworkSettingsFragment.this.mDismissRunner);
                                    MobileNetworkSettingsFragment.mHandler.postDelayed(MobileNetworkSettingsFragment.this.mDismissRunner, 2000L);
                                    return;
                                }
                                MobileNetworkSettingsFragment.isNeedExecuteDismissRunner = true;
                            }
                        }.execute(null, null);
                    }
                }).setNegativeButton(R.string.switch_cancel_btn_text, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.13
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        int i3 = i;
                        if (i3 == 0) {
                            MobileNetworkSettingsFragment.this.mButtonEnableEsim.setChecked(false);
                        } else if (i3 == 1) {
                            MobileNetworkSettingsFragment.this.mButtonEnableEsim.setChecked(true);
                        }
                    }
                });
                AlertDialog alertDialogCreate = builder.create();
                this.mEsimSwitchDialog = alertDialogCreate;
                alertDialogCreate.show();
            }
        }

        private String getSwtichDialogMessage(int i, MiuiPhoneUtils.DualSimType dualSimType) {
            String string = getResources().getString(R.string.sim_roaming_settings_title);
            Bundle bundle = this.mVsimBundle;
            if (bundle != null) {
                string = bundle.getString("vsim_title", string);
            }
            if (i != 0) {
                if (i == 1) {
                    return String.format(getResources().getString(R.string.disable_esim_dialog_summary), "2");
                }
                Log.d(MobileNetworkSettings.TAG, "type is not valid: " + i);
                return "";
            }
            int i2 = AnonymousClass1.$SwitchMap$com$android$phone$MiuiPhoneUtils$DualSimType[dualSimType.ordinal()];
            if (i2 == 1 || i2 == 2) {
                return String.format(getResources().getString(R.string.enable_esim_dialog_summary), "2");
            }
            if (i2 == 3) {
                return String.format(getResources().getString(R.string.enable_esim_dialog_summary_1), string);
            }
            Log.d(MobileNetworkSettings.TAG, "dualSimType is not valid: " + dualSimType);
            return "";
        }

        private void showAlertDialog() {
            String string = getResources().getString(R.string.sim_roaming_settings_title);
            Bundle bundle = this.mVsimBundle;
            if (bundle != null) {
                string = bundle.getString("vsim_title", string);
            }
            String str = String.format(getResources().getString(R.string.disable_vsim_alert_dialog_title), string);
            String str2 = String.format(getResources().getString(R.string.disable_vsim_alert_dialog_summary), string);
            final AppCompatActivity appCompatActivity = getActivity() instanceof AppCompatActivity ? (AppCompatActivity) getActivity() : null;
            new AlertDialog.Builder(getActivity()).setTitle(str).setMessage(str2).setCancelable(false).setPositiveButton(R.string.disable_vsim_alert_dialog_btn_text, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.16
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("com.miui.virtualsim://main?launchfrom=multisimsetting:deactivateVSIM&pageTab=1"));
                    Log.d(MobileNetworkSettings.TAG, "start intent: MI_ROAMING_DEEPLINK");
                    AppCompatActivity appCompatActivity2 = appCompatActivity;
                    if (appCompatActivity2 != null) {
                        MultiAppFloatingActivitySwitcher.configureFloatingService(intent, appCompatActivity2);
                    }
                    MobileNetworkSettingsFragment.this.startActivity(intent);
                }
            }).setNegativeButton(R.string.disable_vsim_alert_dialog_cancel_btn_text, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.15
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    MobileNetworkSettingsFragment.this.mButtonEnableEsim.setChecked(false);
                }
            }).show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void showProgressDialog(int i) {
            dismissProgressDialog();
            CountDownTimer countDownTimer = this.mCountdownTimer;
            if (countDownTimer != null) {
                countDownTimer.cancel();
                this.mCountdownTimer = null;
            }
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            this.mProgressDialog = progressDialog;
            progressDialog.setMessage(getResources().getString(i));
            this.mProgressDialog.setCancelable(false);
            this.mProgressDialog.setIndeterminate(true);
            this.mProgressDialog.show();
            this.mCountdownTimer = new CountDownTimer(30000L, 1000L) { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.18
                @Override // android.os.CountDownTimer
                public void onTick(long j) {
                }

                @Override // android.os.CountDownTimer
                public void onFinish() {
                    Log.d(MobileNetworkSettings.TAG, "dismissProgressDialog for eSIM Loading to longer");
                    MobileNetworkSettingsFragment.this.dismissProgressDialog();
                }
            }.start();
            Log.d(MobileNetworkSettings.TAG, "showProgressDialog for eSIM Switch");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dismissProgressDialog() {
            ProgressDialog progressDialog = this.mProgressDialog;
            if (progressDialog != null) {
                progressDialog.dismiss();
                Log.d(MobileNetworkSettings.TAG, "dismissProgressDialog for eSIM Switch");
            }
            this.mProgressDialog = null;
            CountDownTimer countDownTimer = this.mCountdownTimer;
            if (countDownTimer != null) {
                countDownTimer.cancel();
                this.mCountdownTimer = null;
            }
        }

        private void dismissTurnOnDual4GPromptDialog() {
            AlertDialog alertDialog = this.mSwitchDataSimPromptDialog;
            if (alertDialog == null || !alertDialog.isShowing()) {
                return;
            }
            this.mSwitchDataSimPromptDialog.dismiss();
            this.mSwitchDataSimPromptDialog = null;
        }

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            addPreferencesFromResource(R.xml.miui_network_setting);
            this.mConnectManager = (ConnectivityManager) getContext().getSystemService("connectivity");
            this.mPhone = MiuiPhoneUtils.getPhone(0);
            this.mDefaultAlwaysEnableMMS = MiuiDataControllerImpl.Processor.getDefaultAlwaysEnableMms(getContext());
            this.mMobileNetworkSettingsFragmentHandler = new MobileNetworkSettingsFragmentHandler(getContext().getMainLooper());
            this.mSettingsObserver = new SettingsObserver(getContext(), this.mMobileNetworkSettingsFragmentHandler);
            getSimInfo();
            init();
            this.mIntentFilter = new IntentFilter("android.intent.action.AIRPLANE_MODE");
            if (!MiuiPhoneUtils.isMultiSimEnabled() || TelephonyManager.isCustSingleSimDevice() || MiuiPhoneUtils.isCTDualVolteSupported() || ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isSupportForRegion()) {
                this.mIntentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
            }
            if (MiuiPhoneUtils.isMultiSimEnabled() && !TelephonyManager.isCustSingleSimDevice()) {
                this.mIntentFilter.addAction("android.intent.action.ACTION_DEFAULT_VOICE_SUBSCRIPTION_CHANGED");
                this.mIntentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
                this.mIntentFilter.addAction("android.telephony.action.DEFAULT_SMS_SUBSCRIPTION_CHANGED");
            }
            if (!MiuiPhoneUtils.isMultiSimEnabled() && Build.IS_INTERNATIONAL_BUILD) {
                this.mIntentFilter.addAction("miui.intent.action.OMACP_VOWIFI_ENABLED");
            }
            if (MiuiImsPhoneUtils.disableWfcWhenEnableWifiAp(this.mPhone.getSubId())) {
                this.mIntentFilter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
            }
            this.mIntentFilter.addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
            this.mIntentFilter.addAction("action_pad_enter_lpa");
            this.mIntentFilter.addAction("android.telephony.action.SIM_SLOT_STATUS_CHANGED");
            getContext().registerReceiver(this.mReceiver, this.mIntentFilter, 2);
            HashMap map = new HashMap();
            map.put("mobile_network_settings_enter", "mobile_network_settings_enter");
            MiStatInterfaceUtil.recordCountEvent("event_settings", "key_mobile_network_settings_preference", map);
            this.mIsSimInserted = isSimInserted();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isSimInserted() {
            return TelephonyManager.getDefault().hasIccCard();
        }

        private void init() {
            CheckBoxPreference checkBoxPreference;
            Preference preferenceFindPreference;
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            this.mPreferenceScreen = preferenceScreen;
            this.mPrefSimInfo = (PreferenceCategory) preferenceScreen.findPreference("sim_info");
            this.mPrefSimVoiceDataInfo = (PreferenceCategory) this.mPreferenceScreen.findPreference("sim_voice_data_info");
            this.mPrefSimInfo.setTitle("");
            this.mPrefSimVoiceDataInfo.setTitle("");
            boolean z = Utils.IS_MIUI_LITE_DEVICE;
            if (z) {
                addPreferencesFromResource(R.xml.default_sim_preference);
                this.mPrefDefaultSim = (PreferenceCategory) this.mPreferenceScreen.findPreference("default_sim");
                this.mPrefDefaultVoice = (DropDownPreference) this.mPreferenceScreen.findPreference("pref_default_voice_sim");
                this.mPrefDefaultData = (DropDownPreference) this.mPreferenceScreen.findPreference("pref_default_data_sim");
                updateDropDownList();
                this.mPrefDefaultVoice.setOnPreferenceChangeListener(this);
                this.mPrefDefaultData.setOnPreferenceChangeListener(this);
            } else {
                MultiSimPreference multiSimPreference = new MultiSimPreference(getPreferenceManager().getContext());
                this.mSimInfoPre = multiSimPreference;
                multiSimPreference.setIntent(getActivity().getIntent());
                DefaultSimPreference defaultSimPreference = new DefaultSimPreference(getPreferenceManager().getContext());
                this.mDefaultSimPre = defaultSimPreference;
                defaultSimPreference.setOnPreferenceChangeListener(this);
            }
            this.mPrefGeneral = (PreferenceCategory) this.mPreferenceScreen.findPreference("sim_general");
            this.mPrefDefault = (PreferenceCategory) this.mPreferenceScreen.findPreference("sim_default");
            this.mPrefWifiCall = (PreferenceCategory) this.mPreferenceScreen.findPreference("wifi_call");
            MultiSimDropDownPreference multiSimDropDownPreference = (MultiSimDropDownPreference) this.mPreferenceScreen.findPreference("default_sms_key");
            this.mPrefSMS = multiSimDropDownPreference;
            multiSimDropDownPreference.setOnPreferenceChangeListener(this);
            if (MiuiPhoneUtils.hidePrefSMSSettings()) {
                this.mPrefDefault.removePreference(this.mPrefSMS);
            }
            this.mPrefEsimSetting = (PreferenceCategory) this.mPreferenceScreen.findPreference("esim_setting");
            this.mButtonEnableEsim = (CheckBoxPreference) this.mPreferenceScreen.findPreference("button_enable_esim_key");
            this.mPrefEsimManager = (MiuixTextPreference) this.mPreferenceScreen.findPreference("preference_esim_manager");
            this.mPrefEsimServiceSupport = (MiuixTextPreference) this.mPreferenceScreen.findPreference("preference_esim_service_support");
            if (this.mPrefEsimManager != null) {
                if (MiuiPhoneUtils.isSupportMep()) {
                    this.mPrefEsimManager.setTitle(R.string.enter_mep_esim_manager_title);
                }
                this.mPrefEsimManager.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(this) { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.19
                    @Override // androidx.preference.Preference.OnPreferenceClickListener
                    public boolean onPreferenceClick(Preference preference) {
                        MiuiPhoneUtils.enterEsimManager(false);
                        return true;
                    }
                });
            }
            MiuixTextPreference miuixTextPreference = this.mPrefEsimServiceSupport;
            if (miuixTextPreference != null) {
                miuixTextPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.20
                    @Override // androidx.preference.Preference.OnPreferenceClickListener
                    public boolean onPreferenceClick(Preference preference) {
                        StringBuilder sb = new StringBuilder("https://www.mi.com/global/support/esim?");
                        sb.append("lang=" + SystemProperties.get("persist.sys.locale", "en-001"));
                        sb.append("&");
                        sb.append("tag=" + SystemProperties.get("ro.product.marketname", "Xiaomi-12T-Pro").toLowerCase().replace(" ", "-"));
                        Log.d(MobileNetworkSettings.TAG, "urlAddress:" + ((Object) sb));
                        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(sb.toString()));
                        if (MobileNetworkSettingsFragment.this.getActivity() instanceof AppCompatActivity) {
                            MultiAppFloatingActivitySwitcher.configureFloatingService(intent, (AppCompatActivity) MobileNetworkSettingsFragment.this.getActivity());
                        }
                        MobileNetworkSettingsFragment.this.startActivity(intent);
                        return false;
                    }
                });
            }
            CheckBoxPreference checkBoxPreference2 = this.mButtonEnableEsim;
            if (checkBoxPreference2 != null) {
                checkBoxPreference2.setSummary(String.format(getResources().getString(R.string.enable_esim_summary), "2"));
            }
            if (MiuiPhoneUtils.isEsimCapable()) {
                getContext().getContentResolver().registerContentObserver(Settings.Secure.getUriFor("is_enable_esim_for_user"), false, this.mEsimEnabledObserver);
            } else {
                Log.d(MobileNetworkSettings.TAG, "Remove eSIM settings");
                PreferenceCategory preferenceCategory = this.mPrefEsimSetting;
                if (preferenceCategory != null) {
                    this.mPreferenceScreen.removePreference(preferenceCategory);
                }
            }
            this.mButtonIntelligentDualSim = (CheckBoxPreference) this.mPreferenceScreen.findPreference("button_intelligent_dual_sim_key");
            this.mButtonSmartSwitchDualSim = (CheckBoxPreference) this.mPreferenceScreen.findPreference("button_smart_switch_dual_sim_key");
            this.mButtonViceSlotVolteData = (CheckBoxPreference) this.mPreferenceScreen.findPreference("button_vice_slot_volte_data");
            this.mButtonSmartDualSim = (CheckBoxPreference) this.mPreferenceScreen.findPreference("button_smart_dual_sim_key");
            initIntelligentDualSimView();
            this.mButtonDataEnabled = (CheckBoxPreference) this.mPreferenceScreen.findPreference("button_data_enabled_key");
            this.mButtonDataWarning = (CheckBoxPreference) this.mPreferenceScreen.findPreference("button_data_warning_key");
            this.mButton5gHostedModeEnabled = (CheckBoxPreference) this.mPreferenceScreen.findPreference("button_5g_hosted_mode_key");
            if (!MiuiKoreaOperatorCustimizedUtils.isCustForKrOps()) {
                this.mPrefGeneral.removePreference(this.mButtonDataWarning);
            }
            this.mButtonDual4G = (CheckBoxPreference) this.mPreferenceScreen.findPreference("button_dual_4g");
            this.mButtonDataRoam = (RestrictedPreference) this.mPreferenceScreen.findPreference("button_roaming_key");
            this.mCheckboxDataRoam = (RestrictedCheckBoxPreference) this.mPreferenceScreen.findPreference("button_roaming_checkbox_key");
            CheckBoxPreference checkBoxPreference3 = (CheckBoxPreference) this.mPreferenceScreen.findPreference("button_last_sim_callback");
            this.mButtonUseLastSimCallBack = checkBoxPreference3;
            checkBoxPreference3.setChecked(MiuiSettings.System.getBoolean(getContext().getContentResolver(), "use_last_sim_callback", false));
            this.mSmartCFSwitchView = new SmartCFSwitchView(this, this.mPrefDefault, this.mPhone);
            if (MiuiPhoneUtils.hideSimCallBack(getContext())) {
                this.mPrefDefault.removePreference(this.mButtonUseLastSimCallBack);
            }
            if (this.mPrefDefault.getPreferenceCount() == 0) {
                this.mPreferenceScreen.removePreference(this.mPrefDefault);
            }
            CheckBoxPreference checkBoxPreference4 = (CheckBoxPreference) this.mPreferenceScreen.findPreference("button_mms_enabled_key");
            this.mButtonMmsEnabled = checkBoxPreference4;
            if (checkBoxPreference4 != null && (TelephonyManager.isGoogleCsp() || MiuiPhoneUtils.isHideAlwaysReceiveMms())) {
                this.mPrefGeneral.removePreference(this.mButtonMmsEnabled);
                this.mButtonMmsEnabled = null;
            }
            this.mButtonEnterBusinessHall = (RestrictedPreference) this.mPreferenceScreen.findPreference("button_enter_business_hall_key");
            if (!z || !"cn".equalsIgnoreCase(SystemProperties.get("ro.miui.build.region"))) {
                this.mPrefGeneral.removePreference(this.mButtonEnterBusinessHall);
                this.mButtonEnterBusinessHall = null;
            }
            boolean z2 = MiuiPhoneUtils.DUAL_VOLTE_SUPPORTED;
            if (!z2 && ImsAdapterBase.getInstance().isVolteSupportedByDevice(getContext()) && !MiuiPhoneUtils.isTabletDeviceWithVolteCapability()) {
                this.mVolteSwitchView = new VolteSwitchView(this, this.mPrefGeneral, this.mPhone);
            } else {
                VolteSwitchView.removeVolteSwitch(this.mPrefGeneral);
            }
            if ((!z2 || ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isDisableLte() || MiuiPhoneUtils.isTabletDeviceWithVolteCapability() || MiuiPhoneUtils.isRemoveDual4GSwitch()) && (checkBoxPreference = this.mButtonDual4G) != null) {
                this.mPrefGeneral.removePreference(checkBoxPreference);
                this.mButtonDual4G = null;
            }
            this.mFiveGPreference = (MiuixTextPreference) this.mPreferenceScreen.findPreference("preference_5g_network");
            updateFiveGNetworkPreference();
            this.mButtonSaEnabled = (CheckBoxPreference) this.mPreferenceScreen.findPreference("button_5g_sa_network_key");
            if (TelephonyManager.isCustSingleSimDevice() && FiveGUtils.shouldShow5GSaSwitch()) {
                this.mButtonSaEnabled.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.21
                    @Override // androidx.preference.Preference.OnPreferenceClickListener
                    public boolean onPreferenceClick(Preference preference) {
                        boolean zIsChecked = MobileNetworkSettingsFragment.this.mButtonSaEnabled.isChecked();
                        Rlog.i(MobileNetworkSettings.TAG, "enable main SA to: " + zIsChecked);
                        FiveGManagerBase.getInstance().setUserFiveGSaEnabled(zIsChecked);
                        MiStatInterfaceUtil.recordSwitchStatusEvent(preference.getKey(), zIsChecked);
                        MobileNetworkSettingsFragment.this.updatePreferenceUI();
                        return true;
                    }
                });
            } else {
                this.mPrefGeneral.removePreference(this.mButtonSaEnabled);
                this.mButtonSaEnabled = null;
            }
            this.mLteDataServicePref = this.mPreferenceScreen.findPreference("cdma_lte_data_service_key");
            this.mPrefChinaMobilePalm = (PreferenceCategory) this.mPreferenceScreen.findPreference("china_mobile_palm");
            this.mOtherMobileSettings = (PreferenceCategory) this.mPreferenceScreen.findPreference("additional_mobile_settings");
            this.mButtonOfflineCall = (RestrictedPreference) this.mPreferenceScreen.findPreference("button_offlinecall_key");
            boolean zIsTabletDeviceWithVolteCapability = MiuiPhoneUtils.isTabletDeviceWithVolteCapability();
            PreferenceCategory preferenceCategory2 = this.mOtherMobileSettings;
            if (zIsTabletDeviceWithVolteCapability) {
                if (preferenceCategory2 != null) {
                    preferenceCategory2.removePreference(this.mButtonDataRoam);
                    this.mButtonDataRoam = null;
                }
                boolean z3 = Build.IS_INTERNATIONAL_BUILD;
                RestrictedCheckBoxPreference restrictedCheckBoxPreference = this.mCheckboxDataRoam;
                if (z3) {
                    restrictedCheckBoxPreference.setTitle(R.string.international_roaming_setting);
                } else {
                    restrictedCheckBoxPreference.setTitle(R.string.overseas_roaming_setting);
                }
            } else {
                if (preferenceCategory2 != null) {
                    preferenceCategory2.removePreference(this.mCheckboxDataRoam);
                    this.mCheckboxDataRoam = null;
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
            }
            boolean z4 = Settings.Secure.getInt(getContext().getContentResolver(), "bluetooth_call_enable", 0) != 0;
            boolean z5 = Settings.System.getInt(getContext().getContentResolver(), "power_supersave_mode_open", 0) != 0;
            Log.d(MobileNetworkSettings.TAG, "Check Offlinecall support status, isBluetoothCallEnabled = " + z4 + ", isSuperSaveModeOn = " + z5);
            if (!TelephonyManager.getDefault().isFeatureSupported(FeatureConfiguration.FeatureType.FEATURE_OFFLINE_CALL) || !z4 || z5) {
                PreferenceCategory preferenceCategory3 = this.mOtherMobileSettings;
                if (preferenceCategory3 != null) {
                    preferenceCategory3.removePreference(this.mButtonOfflineCall);
                    this.mButtonOfflineCall = null;
                }
            } else {
                RestrictedPreference restrictedPreference2 = this.mButtonOfflineCall;
                if (restrictedPreference2 != null) {
                    restrictedPreference2.setTitle(R.string.offline_call);
                    this.mButtonOfflineCall.setSummary(R.string.offline_call_summary);
                }
            }
            if (!TelephonyCapabilities.supportShowPLMNPreference() || MiuiPhoneUtils.isMultiSimEnabled()) {
                PreferenceScreen preferenceScreen2 = this.mPreferenceScreen;
                preferenceScreen2.removePreference(preferenceScreen2.findPreference("button_uplmn_key"));
            }
            TextUtils.isEmpty(Settings.Global.getString(getContext().getContentResolver(), "setup_prepaid_data_service_url"));
            this.mPreferenceScreen.removePreference(this.mLteDataServicePref);
            if (!getResources().getBoolean(R.bool.config_carrier_settings_enable) && (preferenceFindPreference = this.mPreferenceScreen.findPreference("carrier_settings_key")) != null) {
                this.mPreferenceScreen.removePreference(preferenceFindPreference);
                Preference preferenceFindPreference2 = this.mPreferenceScreen.findPreference("carrier_settings_key");
                if (preferenceFindPreference2 != null) {
                    this.mPreferenceScreen.removePreference(preferenceFindPreference2);
                }
            }
            updateWifiCallSwitchView();
            this.mContentObserver = new ContentObserver(new Handler(getContext().getMainLooper())) { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.22
                @Override // android.database.ContentObserver
                public void onChange(boolean z6) {
                    super.onChange(z6);
                    if (MobileNetworkSettingsFragment.this.getContext() == null) {
                        return;
                    }
                    if (MobileNetworkSettingsFragment.this.mButtonDataEnabled != null) {
                        MobileNetworkSettingsFragment.this.mButtonDataEnabled.setChecked(MiuiPhoneUtils.isMobileDataEnabled(MobileNetworkSettingsFragment.this.getContext()));
                    }
                    if (MobileNetworkSettingsFragment.this.mButtonMmsEnabled != null) {
                        MobileNetworkSettingsFragment.this.mButtonMmsEnabled.setChecked(Settings.System.getInt(MobileNetworkSettingsFragment.this.getContext().getContentResolver(), "always_enable_mms", MobileNetworkSettingsFragment.this.mDefaultAlwaysEnableMMS) == 1);
                    }
                }
            };
            if (this.mButtonDataEnabled != null) {
                getContext().getContentResolver().registerContentObserver(Settings.Global.getUriFor("mobile_data"), true, this.mContentObserver);
                if (MiuiPhoneUtils.isMultiSimEnabled()) {
                    for (int i = 0; i < TelephonyManager.getDefault().getPhoneCount(); i++) {
                        getContext().getContentResolver().registerContentObserver(Settings.Global.getUriFor("mobile_data" + i), false, this.mContentObserver);
                    }
                }
            }
            if (this.mButtonMmsEnabled != null) {
                getContext().getContentResolver().registerContentObserver(Settings.System.getUriFor("always_enable_mms"), true, this.mContentObserver);
            }
            if (!MiuiPhoneUtils.isMultiSimEnabled() || TelephonyManager.isCustSingleSimDevice()) {
                this.mPreferenceScreen.removePreference(this.mPrefSimInfo);
                this.mPreferenceScreen.removePreference(this.mPrefSimVoiceDataInfo);
                this.mPrefGeneral.setOrder(0);
                this.mPreferenceScreen.removePreference(this.mPrefDefault);
                setGsmCdmaOptions();
                PreferenceCategory preferenceCategory4 = this.mPrefDefaultSim;
                if (preferenceCategory4 != null) {
                    preferenceCategory4.removePreference(this.mPrefDefaultVoice);
                    this.mPrefDefaultSim.removePreference(this.mPrefDefaultData);
                    this.mPreferenceScreen.removePreference(this.mPrefDefaultSim);
                }
            } else if (!Utils.IS_MIUI_LITE_DEVICE) {
                this.mPrefSimInfo.addPreference(this.mSimInfoPre);
                if (this.mSimNum > 1) {
                    this.mPrefSimVoiceDataInfo.addPreference(this.mDefaultSimPre);
                }
            }
            VolteSwitchView volteSwitchView = this.mVolteSwitchView;
            if (volteSwitchView != null) {
                volteSwitchView.updateVolteButtonUI();
            }
            CheckBoxPreference checkBoxPreference5 = this.mButtonDual4G;
            if (checkBoxPreference5 != null) {
                if (this.mSimNum > 1 && MiuiPhoneUtils.DUAL_VOLTE_SUPPORTED) {
                    this.mPrefGeneral.addPreference(checkBoxPreference5);
                } else {
                    this.mPrefGeneral.removePreference(checkBoxPreference5);
                }
            }
            initFivegHostedModeButton();
            initSimActiveAndCarrierConfigOk();
        }

        private void initFivegHostedModeButton() {
            if (this.mPrefGeneral == null || this.mButton5gHostedModeEnabled == null) {
                return;
            }
            if (!MiuiPhoneUtils.isFiveGHostedModeSupported() || (MiuiPhoneUtils.overMiuiFifteen() && !MiuiPhoneUtils.isFivegHostedModeIndia())) {
                this.mPrefGeneral.removePreference(this.mButton5gHostedModeEnabled);
                this.mButton5gHostedModeEnabled = null;
            }
            updateFiveGHoseModeButton();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateFiveGHoseModeButton() {
            boolean z = !this.mIsAirplaneEnabled && this.mIsSimInserted;
            CheckBoxPreference checkBoxPreference = this.mButton5gHostedModeEnabled;
            if (checkBoxPreference != null) {
                if (z) {
                    boolean zIsUserFiveGHostedModeEnabled = FiveGManagerBase.getInstance().isUserFiveGHostedModeEnabled();
                    Log.d(MobileNetworkSettings.TAG, "updateFiveGHoseModeButton status=" + zIsUserFiveGHostedModeEnabled);
                    this.mButton5gHostedModeEnabled.setChecked(zIsUserFiveGHostedModeEnabled);
                    this.mButton5gHostedModeEnabled.setEnabled(FiveGManagerBase.getInstance().isUserFiveGEnabled());
                    return;
                }
                checkBoxPreference.setEnabled(z);
            }
        }

        private void addSimInfoPreference() {
            int i;
            SimInfoBean simInfoBean;
            if (Utils.IS_MIUI_LITE_DEVICE) {
                this.mPrefSimInfo.removeAll();
                this.mPrefSimVoiceDataInfo.removeAll();
                SparseArray sparseArray = new SparseArray();
                int virtualSimSlot = MiuiPhoneUtils.getVirtualSimSlot();
                int i2 = 0;
                while (true) {
                    i = 1;
                    if (i2 >= 2) {
                        break;
                    }
                    SubscriptionInfo subInfo = getSubInfo(i2);
                    if (subInfo != null) {
                        boolean z = virtualSimSlot == subInfo.getSlotId();
                        simInfoBean = new SimInfoBean();
                        if (z) {
                            simInfoBean.mSimType = 101;
                        } else {
                            simInfoBean.mSimType = 100;
                        }
                        simInfoBean.mSlotId = subInfo.getSlotId();
                        simInfoBean.mDisplayName = getSimDisplayName(subInfo, z);
                        simInfoBean.mDisplayNumber = getSimDisplayNumber(subInfo, subInfo.getSlotId());
                        simInfoBean.mIsVirtualSim = z;
                        simInfoBean.mSimIcon = z ? R.drawable.ic_mi_sim_big : SimInfoBean.SIM_ICON[subInfo.getSlotId()];
                    } else {
                        simInfoBean = new SimInfoBean();
                        List list = this.mSimInfoRecordList;
                        if (list != null && list.size() == 1) {
                            simInfoBean.mSimType = 103;
                            simInfoBean.mSlotId = i2;
                        } else {
                            simInfoBean.mSimType = 102;
                            simInfoBean.mSlotId = i2;
                        }
                        simInfoBean.mDisplayName = getContext().getResources().getString(R.string.msim_setting_no_sim_indicator);
                        simInfoBean.mDisplayNumber = getContext().getResources().getString(R.string.msim_setting_no_sim_number);
                        simInfoBean.mIsVirtualSim = false;
                        simInfoBean.mSimIcon = SimInfoBean.SIM_ICON[i2];
                    }
                    if (i2 == 1 && MiuiPhoneUtils.isEsimCapable() && ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isSupportForRegion()) {
                        int esimSecureValue = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getEsimSecureValue();
                        boolean zIsEsimActive = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isEsimActive();
                        if (esimSecureValue == 1) {
                            if (!zIsEsimActive) {
                                simInfoBean.mDisplayName = getContext().getResources().getString(R.string.no_active_esim_text);
                                simInfoBean.mDisplayNumber = "";
                            } else if (!MiuiPhoneUtils.isSupportMep()) {
                                simInfoBean.mSimIcon = R.drawable.ic_esim_global_big;
                            }
                        }
                    }
                    sparseArray.put(simInfoBean.mSlotId, simInfoBean);
                    i2++;
                }
                for (int i3 = 0; i3 < sparseArray.size(); i3++) {
                    SimInfoBean simInfoBean2 = (SimInfoBean) sparseArray.get(i3);
                    Preference preference = new Preference(getContext());
                    int i4 = simInfoBean2.mEsimIcon;
                    if (i4 == 0) {
                        i4 = simInfoBean2.mSimIcon;
                    }
                    preference.setIcon(i4);
                    preference.setTitle(simInfoBean2.mDisplayName);
                    preference.setSummary(simInfoBean2.mDisplayNumber);
                    preference.setIntent(new Intent(getContext(), (Class<?>) MultiSimInfoEditorActivity.class));
                    if (simInfoBean2.mSimType == 104) {
                        preference.setOnPreferenceClickListener(new SimPreferenceClickListener(simInfoBean2, 4));
                    } else {
                        preference.setOnPreferenceClickListener(new SimPreferenceClickListener(simInfoBean2, i));
                    }
                    this.mPrefSimInfo.addPreference(preference);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public SubscriptionInfo getSubInfo(int i) {
            List list = this.mSimInfoRecordList;
            if (list == null || list.size() <= 0) {
                return null;
            }
            for (SubscriptionInfo subscriptionInfo : this.mSimInfoRecordList) {
                if (subscriptionInfo.getSlotId() == i) {
                    return subscriptionInfo;
                }
            }
            return null;
        }

        private PackageBean getPackage(int i) {
            List simInfo = SimCacheHelper.getInstance().getSimInfo(getContext());
            if (simInfo == null || simInfo.size() <= 0) {
                return null;
            }
            for (int i2 = 0; i2 < simInfo.size(); i2++) {
                PackageBean packageBean = (PackageBean) simInfo.get(i2);
                if (packageBean.mSlotId == i) {
                    return packageBean;
                }
            }
            return null;
        }

        private String getSimDisplayName(SubscriptionInfo subscriptionInfo, boolean z) {
            String string;
            if (z) {
                string = MiuiPhoneUtils.getVirtualSimCarrierName();
            } else {
                string = subscriptionInfo.getDisplayName().toString();
            }
            return TextUtils.isEmpty(string) ? getContext().getResources().getString(R.string.msim_setting_no_sim_number) : string;
        }

        private String getSimDisplayNumber(SubscriptionInfo subscriptionInfo, int i) {
            String displayNumber;
            if (!MiuiPhoneUtils.isIccCardActivated(i)) {
                return getContext().getResources().getString(R.string.msim_sim_radio_off);
            }
            if (TextUtils.isEmpty(subscriptionInfo.getDisplayNumber())) {
                displayNumber = getContext().getResources().getString(R.string.msim_setting_no_sim_number);
            } else {
                displayNumber = subscriptionInfo.getDisplayNumber();
            }
            if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isShowCdmaUnavailable() && subscriptionInfo.getSlotId() != DefaultSimManager.getDefaultDataSlotId() && 2 == ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getPhoneType(subscriptionInfo.getSlotId())) {
                displayNumber = displayNumber + getContext().getResources().getString(R.string.can_not_use_for_not_data_sim);
            }
            return Utils.localizeNumber(displayNumber);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateDropDownList() {
            int i;
            if (this.mPrefDefaultVoice == null || this.mPrefDefaultData == null) {
                Log.d(MobileNetworkSettings.TAG, "updateDropDownList mPrefDefaultVoice or mPrefDefaultData is null");
                return;
            }
            String string = getResources().getString(R.string.sim_card_num);
            if (getResources().getConfiguration().locale.getLanguage().equals("zh")) {
                string = "SIM %s";
            }
            int i2 = MiuiPhoneUtils.PHONE_COUNT;
            String[] strArr = new String[i2 + 1];
            String[] strArr2 = new String[i2];
            int i3 = 0;
            while (true) {
                i = MiuiPhoneUtils.PHONE_COUNT;
                if (i3 >= i) {
                    break;
                }
                int i4 = i3 + 1;
                strArr[i3] = String.format(string, Integer.valueOf(i4));
                strArr2[i3] = String.format(string, Integer.valueOf(i4));
                i3 = i4;
            }
            strArr[i] = getResources().getString(R.string.pref_sim_item_no_default);
            int defaultVoiceSlotId = ModemProvider.getInstance().getDefaultVoiceSlotId();
            if (defaultVoiceSlotId >= 0) {
                i = defaultVoiceSlotId;
            }
            int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
            this.mPrefDefaultVoice.setEntries(strArr);
            this.mPrefDefaultVoice.setValueIndex(i);
            this.mPrefDefaultData.setEntries(strArr2);
            this.mPrefDefaultData.setValueIndex(defaultDataSlotId);
            if (this.mActiveSimCount < 2) {
                this.mPrefDefaultVoice.setEnabled(false);
                this.mPrefDefaultData.setEnabled(false);
            }
        }

        private void updateBusinessHallPreference() throws Throwable {
            if (this.mButtonEnterBusinessHall == null) {
                return;
            }
            String[] strArr = new String[this.mSimNum];
            int virtualSimSlot = MiuiPhoneUtils.getVirtualSimSlot();
            int i = 0;
            for (int i2 = 0; i2 < this.mSimNum; i2++) {
                SubscriptionInfo subInfo = getSubInfo(i2);
                if (subInfo != null) {
                    if (virtualSimSlot == subInfo.getSlotId()) {
                        PackageBean dataUsageInfo = SimCacheHelper.getInstance().getDataUsageInfo(getContext());
                        if (dataUsageInfo != null && !TextUtils.isEmpty(dataUsageInfo.mTrafficName) && !TextUtils.isEmpty(dataUsageInfo.mTrafficValue)) {
                            strArr[i2] = dataUsageInfo.mTrafficName + dataUsageInfo.mTrafficValue + dataUsageInfo.mTrafficUnit;
                        }
                    } else {
                        PackageBean packageBean = getPackage(subInfo.getSlotId());
                        if (packageBean != null && !TextUtils.isEmpty(packageBean.mBillName) && !TextUtils.isEmpty(packageBean.mBillValue)) {
                            strArr[i2] = packageBean.mBillName + packageBean.mBillValue + packageBean.mBillUnit;
                        }
                    }
                }
            }
            if (Stream.of((Object[]) strArr).anyMatch(new Predicate() { // from class: com.android.phone.settings.MobileNetworkSettings$MobileNetworkSettingsFragment$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return MobileNetworkSettings.MobileNetworkSettingsFragment.m3200$r8$lambda$VLcPTVgVwGhtHuEMjJTx3tlwhA((String) obj);
                }
            })) {
                String strConcat = "";
                while (i < this.mSimNum) {
                    if (strArr[i] != null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(strConcat);
                        sb.append("SIM");
                        int i3 = i + 1;
                        sb.append(i3);
                        sb.append(" ");
                        sb.append(strArr[i]);
                        String string = sb.toString();
                        strConcat = (i == this.mSimNum + (-1) || strArr[i3] == null) ? string : string.concat(" | ");
                    }
                    i++;
                }
                this.mButtonEnterBusinessHall.setSummary(strConcat);
            }
        }

        /* JADX INFO: renamed from: $r8$lambda$VLcPTVgVwGhtHuE-MjJTx3tlwhA, reason: not valid java name */
        public static /* synthetic */ boolean m3200$r8$lambda$VLcPTVgVwGhtHuEMjJTx3tlwhA(String str) {
            return str != null;
        }

        private void setGsmCdmaOptions() {
            int phoneType = this.mPhone.getPhoneType();
            if (phoneType == 2) {
                this.mCdmaOptions = new CdmaOptions(this, this.mPreferenceScreen, this.mPhone);
            } else if (phoneType == 1) {
                this.mGsmUmtsOptions = new GsmUmtsOptions(this, this.mPreferenceScreen, this.mPhone);
            } else {
                CommonOptions$$ExternalSyntheticBUOutline0.m("Unexpected phone type: ", phoneType);
            }
        }

        private boolean isGoogleMessage() {
            return "com.google.android.apps.messaging".equals(Telephony.Sms.getDefaultSmsPackage(getContext()));
        }

        @Override // androidx.fragment.app.Fragment
        public void onResume() throws Throwable {
            super.onResume();
            this.mIsAirplaneEnabled = Settings.System.getInt(getContext().getContentResolver(), "airplane_mode_on", -1) == 1;
            if (TelephonyManager.isCustSingleSimDevice() && this.mButtonSaEnabled != null && FiveGUtils.shouldShow5GSaSwitch()) {
                this.mButtonSaEnabled.setOnPreferenceChangeListener(this);
                update5gNetworkMode();
            }
            setPreferenceScreenEnabled(!this.mIsAirplaneEnabled);
            this.mButtonDataEnabled.setChecked(MiuiPhoneUtils.isMobileDataEnabled(getContext()));
            this.mButtonDataWarning.setChecked(Settings.Global.getInt(getContext().getContentResolver(), "always_alert_data_enable", 1) == 1);
            if (this.mButtonMmsEnabled != null) {
                if (this.mPrefGeneral.findPreference("button_mms_enabled_key") == null && !isGoogleMessage()) {
                    this.mPrefGeneral.addPreference(this.mButtonMmsEnabled);
                } else if (this.mPrefGeneral.findPreference("button_mms_enabled_key") != null && isGoogleMessage()) {
                    this.mPrefGeneral.removePreference(this.mButtonMmsEnabled);
                }
            }
            CheckBoxPreference checkBoxPreference = this.mButtonMmsEnabled;
            if (checkBoxPreference != null) {
                checkBoxPreference.setChecked(Settings.System.getInt(getContext().getContentResolver(), "always_enable_mms", this.mDefaultAlwaysEnableMMS) == 1);
            }
            if (this.mSwitchDataSimPromptDialog != null) {
                dismissTurnOnDual4GPromptDialog();
            }
            if (this.mDataUsagePromptDialog != null) {
                dismissDataUsagePromptDialog();
            }
            VolteSwitchView volteSwitchView = this.mVolteSwitchView;
            if (volteSwitchView != null) {
                volteSwitchView.updateVolteButtonUI();
            }
            updateWifiCallSwitchView();
            SmartCFSwitchView smartCFSwitchView = this.mSmartCFSwitchView;
            if (smartCFSwitchView != null) {
                smartCFSwitchView.updateSmartCFButtonUI();
            }
            CheckBoxPreference checkBoxPreference2 = this.mButtonDual4G;
            if (checkBoxPreference2 != null) {
                checkBoxPreference2.setChecked(((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isDual4GEnabled());
                boolean zIsCloudSimEnabled = VirtualSimUtils.isCloudSimEnabled();
                CheckBoxPreference checkBoxPreference3 = this.mButtonDual4G;
                if (zIsCloudSimEnabled) {
                    checkBoxPreference3.setEnabled(false);
                    this.mButtonDual4G.setSummary(R.string.cloud_sim_enabled_dual_4g_summary);
                } else {
                    checkBoxPreference3.setEnabled(true);
                    this.mButtonDual4G.setSummary(R.string.dual_4g_switch_summary);
                }
                if (MiuiPhoneUtils.isCTDualVolteSupported() && MiuiPhoneUtils.isDualCTSim()) {
                    this.mButtonDual4G.setEnabled(false);
                    this.mButtonDual4G.setSummary(R.string.enable_dual_4g_if_dual_ct);
                } else if (MiuiPhoneUtils.cannotDisableDual4G()) {
                    this.mButtonDual4G.setChecked(true);
                    this.mButtonDual4G.setEnabled(false);
                }
            }
            if (MiuiPhoneUtils.isMultiSimEnabled() && !TelephonyManager.isCustSingleSimDevice()) {
                SubscriptionManager.getDefault().addOnSubscriptionsChangedListener(this);
                updatePreferenceUI();
            } else if (!TelephonyManager.getDefault().hasIccCard()) {
                setPreferenceScreenEnabled(false);
            }
            RestrictedCheckBoxPreference restrictedCheckBoxPreference = this.mCheckboxDataRoam;
            if (restrictedCheckBoxPreference != null) {
                restrictedCheckBoxPreference.setChecked(this.mPhone.getDataRoamingEnabled());
                if (this.mCheckboxDataRoam.isEnabled()) {
                    Context applicationContext = getContext().getApplicationContext();
                    boolean zHasBaseUserRestriction = SimpleFeatures.hasBaseUserRestriction(applicationContext);
                    RestrictedCheckBoxPreference restrictedCheckBoxPreference2 = this.mCheckboxDataRoam;
                    if (zHasBaseUserRestriction) {
                        restrictedCheckBoxPreference2.setEnabled(false);
                    } else {
                        restrictedCheckBoxPreference2.setDisabledByAdmin(SimpleFeatures.checkDataRoamingRestriction(applicationContext));
                    }
                }
            }
            RestrictedPreference restrictedPreference = this.mButtonDataRoam;
            if (restrictedPreference != null && restrictedPreference.isEnabled()) {
                Context applicationContext2 = getContext().getApplicationContext();
                boolean zHasBaseUserRestriction2 = SimpleFeatures.hasBaseUserRestriction(applicationContext2);
                RestrictedPreference restrictedPreference2 = this.mButtonDataRoam;
                if (zHasBaseUserRestriction2) {
                    restrictedPreference2.setEnabled(false);
                } else {
                    restrictedPreference2.setDisabledByAdmin(SimpleFeatures.checkDataRoamingRestriction(applicationContext2));
                }
            }
            addSimInfoPreference();
            updateBusinessHallPreference();
            updateFiveGNetworkPreference();
            addSimServicePreference();
            updateChinaMobilePalmView();
            updateFiveGHoseModeButton();
            this.mPrefEsimSetting.setEnabled(!this.mIsAirplaneEnabled);
            isNeedRecoveryForResume = true;
            updateEsimSettingView();
            DefaultSimManager.getInstance().addDataSlotListener(this);
            updateIntelligentDualSimButton();
            updateSmartSwitchDualSimButton();
            updateSmartDualSimButton();
            updateViceSlotDataButton();
            initIntelligentDualSimView();
            updateDataEnabledButtonByEnterprise();
        }

        @Override // androidx.fragment.app.Fragment
        public void onPause() {
            super.onPause();
            if (MiuiPhoneUtils.isMultiSimEnabled() && !TelephonyManager.isCustSingleSimDevice()) {
                SubscriptionManager.getDefault().removeOnSubscriptionsChangedListener(this);
            }
            DefaultSimManager.getInstance().removeDataSlotListener(this);
        }

        public void onSubscriptionsChanged() {
            getSimInfo();
            updatePreferenceUI();
        }

        private void showChangeDataSIMDialog(final Preference preference, int i, final int i2) {
            AlertDialog alertDialogCreate = new AlertDialog.Builder(getActivity()).setMessage(String.format(getContext().getApplicationContext().getString(R.string.change_data_sim_4g_only_prompt), Integer.valueOf(i), Integer.valueOf(i))).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.24
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i3) {
                    SubscriptionManager.getDefault().setDefaultDataSlotId(i2);
                    MobileNetworkSettingsFragment.this.setPreferenceSummary(preference, i2);
                }
            }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.23
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i3) {
                    MobileNetworkSettingsFragment.this.updatePreferenceUI();
                    MobileNetworkSettingsFragment.this.mSwitchDataSimPromptDialog.dismiss();
                }
            }).setCancelable(false).create();
            this.mSwitchDataSimPromptDialog = alertDialogCreate;
            alertDialogCreate.show();
        }

        private void openDual4G(final int i) {
            ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setDual4GMode(true, new Utils.Dual4GCallBack() { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.25
                @Override // com.android.phone.utils.Utils.Dual4GCallBack
                public void onSetDual4GResult(final boolean z, final int i2) {
                    if (MobileNetworkSettingsFragment.this.getActivity() == null) {
                        return;
                    }
                    Log.i(MobileNetworkSettings.TAG, "openDual4G onSetDual4GResult enabled=" + z + ", resultCode=" + i2);
                    MobileNetworkSettingsFragment.this.getActivity().runOnUiThread(new Runnable() { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.25.1
                        @Override // java.lang.Runnable
                        public void run() {
                            int i3 = i2;
                            AnonymousClass25 anonymousClass25 = AnonymousClass25.this;
                            if (i3 != 0) {
                                Toast.makeText(MobileNetworkSettingsFragment.this.getContext().getApplicationContext(), z ? R.string.turn_on_dual_4g_failed : R.string.turn_off_dual_4g_failed, 0).show();
                                if (MobileNetworkSettingsFragment.this.mButtonDual4G != null) {
                                    MobileNetworkSettingsFragment.this.mButtonDual4G.setChecked(!z);
                                    return;
                                }
                                return;
                            }
                            if (MobileNetworkSettingsFragment.this.mButtonDual4G != null) {
                                MobileNetworkSettingsFragment.this.mButtonDual4G.setChecked(z);
                            }
                            MiuiPhoneUtils.openedDual4GConfirmPromptWithSlot(i);
                        }
                    });
                }
            });
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            String string = obj.toString();
            MultiSimDropDownPreference multiSimDropDownPreference = this.mPrefSMS;
            if (preference == multiSimDropDownPreference) {
                int iFindIndexOfValue = multiSimDropDownPreference.findIndexOfValue(string);
                SubscriptionManager.getDefault().setDefaultSmsSlotId(iFindIndexOfValue);
                MiStatInterfaceUtil.recordDefaultSlotIdEvent(preference.getKey(), iFindIndexOfValue);
                setPreferenceSummary(preference, iFindIndexOfValue);
            } else {
                DefaultSimPreference defaultSimPreference = this.mDefaultSimPre;
                if (preference == defaultSimPreference) {
                    boolean zIsData = defaultSimPreference.isData(string);
                    boolean zIsVoice = this.mDefaultSimPre.isVoice(string);
                    boolean zIsSms = this.mDefaultSimPre.isSms(string);
                    int slotId = this.mDefaultSimPre.getSlotId(string);
                    if (zIsData) {
                        return changeDefaultDataSim(preference, slotId);
                    }
                    if (zIsVoice) {
                        SubscriptionManager.getDefault().setDefaultVoiceSlotId(slotId);
                    } else if (zIsSms) {
                        SubscriptionManager.getDefault().setDefaultSmsSlotId(slotId);
                        MiStatInterfaceUtil.recordDefaultSlotIdEvent(preference.getKey(), slotId);
                    }
                } else {
                    DropDownPreference dropDownPreference = this.mPrefDefaultVoice;
                    if (preference == dropDownPreference) {
                        int iFindIndexOfValue2 = dropDownPreference.findIndexOfValue((String) obj);
                        if (iFindIndexOfValue2 > 1) {
                            iFindIndexOfValue2 = -1;
                        }
                        SubscriptionManager.getDefault().setDefaultVoiceSlotId(iFindIndexOfValue2);
                        return true;
                    }
                    DropDownPreference dropDownPreference2 = this.mPrefDefaultData;
                    if (preference == dropDownPreference2) {
                        return changeDefaultDataSim(preference, dropDownPreference2.findIndexOfValue((String) obj));
                    }
                }
            }
            return true;
        }

        private boolean changeDefaultDataSim(Preference preference, int i) {
            if (MiuiPhoneUtils.getCallManager().getState() != PhoneConstants.State.IDLE) {
                Toast.makeText(getContext(), R.string.msim_set_sub_not_supported_phone_in_call, 1).show();
                if (Utils.IS_MIUI_LITE_DEVICE && this.mPrefDefaultVoice != null && this.mPrefDefaultData != null) {
                    updateDropDownList();
                }
                return false;
            }
            Log.i(MobileNetworkSettings.TAG, "onPreferenceChange,change default data ,slotId = " + i);
            TelephonyManager telephonyManager = TelephonyManager.getDefault();
            int i2 = -1;
            for (int i3 = 0; i3 < MiuiPhoneUtils.PHONE_COUNT; i3++) {
                if (i3 != i && MiuiPhoneUtils.is4GOnlySim(telephonyManager.getSimOperatorForSlot(i3))) {
                    i2 = i3;
                }
            }
            boolean zIsDualVolteSupported = telephonyManager.isDualVolteSupported();
            if (i2 != -1 && !zIsDualVolteSupported && getActivity() != null) {
                Log.i(MobileNetworkSettings.TAG, "showChangeDataPreferenceDialog");
                showChangeDataSIMDialog(preference, i2 + 1, i);
                return true;
            }
            SubscriptionManager.getDefault().setDefaultDataSlotId(i);
            triggerMtkServiceStateDisplayOptimizate();
            Phone phone = MiuiPhoneUtils.getPhone(i);
            if (MiuiAisSimLockManager.getInstance().isRestricted(phone)) {
                Log.i(MobileNetworkSettings.TAG, "onPreferenceChange checkRestrictedAndShowDialog ,slotId =" + i);
                MiuiAisSimLockManager.getInstance().showSimLockDialog(phone);
            }
            if (i2 != -1 && zIsDualVolteSupported && !((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isDual4GEnabled()) {
                Log.i(MobileNetworkSettings.TAG, "onPreferenceChange, turn on dual 4G ");
                openDual4G(i2 + 1);
            }
            return true;
        }

        private void triggerMtkServiceStateDisplayOptimizate() {
            if (MiuiTelephony.IS_MTK) {
                Phone[] phones = PhoneFactory.getPhones();
                if (phones != null) {
                    for (int i = 0; i < phones.length; i++) {
                        MiuiNetworkControllerImpl miuiNetworkControllerImpl = (MiuiNetworkControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiNetworkControllerImpl.class, i);
                        if (miuiNetworkControllerImpl != null) {
                            miuiNetworkControllerImpl.optimizateServiceStateDisplay("data slot changed");
                        }
                    }
                }
                Log.i(MobileNetworkSettings.TAG, "data slot changed, trigger Mtk service state display optimizate.");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setPreferenceSummary(Preference preference, int i) {
            if (SubscriptionManager.isRealSlotId(i)) {
                SubscriptionInfo subscriptionInfoForSlot = SubscriptionManager.getDefault().getSubscriptionInfoForSlot(i);
                if (subscriptionInfoForSlot != null) {
                    if (MiuiPhoneUtils.isVirtualSim(subscriptionInfoForSlot.getSlotId())) {
                        preference.setSummary(MiuiPhoneUtils.getVirtualSimCarrierName());
                        preference.setIcon(R.drawable.mi_small_sim_icon);
                        return;
                    }
                    String string = subscriptionInfoForSlot.getDisplayName().toString();
                    StringBuilder sb = new StringBuilder();
                    for (int i2 = 0; i2 < string.length(); i2++) {
                        if (string.charAt(i2) == '%') {
                            sb.append("%%");
                        } else {
                            sb.append(string.charAt(i2));
                        }
                    }
                    preference.setSummary(sb);
                    preference.setIcon(MobileNetworkSettings.SMALL_SIM_SLOT_ICON[i]);
                    return;
                }
                Log.d(MobileNetworkSettings.TAG, "setPreferenceSummary SimInfoRecord is null.");
                return;
            }
            preference.setSummary(R.string.msim_not_set_summary);
            preference.setIcon(R.drawable.msim_null_drawable);
        }

        public void getSimInfo() {
            this.mSimInfoRecordList = SubscriptionManager.getDefault().getAvailableSubscriptionInfoList();
            this.mActiveSimCount = SubscriptionManager.getDefault().getSubscriptionInfoCount();
            this.mSimNum = this.mSimInfoRecordList.size();
        }

        /* JADX WARN: Type inference fix 'apply assigned field type' failed
        java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
        	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
        	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
        	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
         */
        public void updatePreferenceUI() {
            CheckBoxPreference checkBoxPreference;
            if (!isVisible()) {
                Log.w(MobileNetworkSettings.TAG, "updatePreferenceUI(): on backgroud no need update preference");
            }
            getSimInfo();
            boolean z = false;
            z = false;
            z = false;
            setPreferenceScreenEnabled(!this.mIsAirplaneEnabled && this.mSimNum > 0);
            int defaultSmsSlotId = SubscriptionManager.getDefault().getDefaultSmsSlotId();
            this.mIsShowPrefSMSSettings = this.mSimNum >= 1 && !MiuiPhoneUtils.hidePrefSMSSettings();
            updateSimInfoPreference();
            int i = this.mSimNum;
            boolean z2 = i > 1;
            boolean z3 = i > 1;
            boolean z4 = z2;
            if (z2 && MiuiPhoneUtils.isDcOnlyVirtualSim(this.mPhone.getContext())) {
                if (this.mSimNum > 2) {
                    z4 = z2;
                    z4 = true;
                } else {
                    z4 = z2;
                    z4 = false;
                }
            }
            z4 = z2;
            DefaultSimPreference defaultSimPreference = this.mDefaultSimPre;
            if (defaultSimPreference != null) {
                defaultSimPreference.setVoiceEnabled(z4 && !this.mIsAirplaneEnabled);
                this.mDefaultSimPre.setDataEnabled(z3 && !this.mIsAirplaneEnabled);
                if (this.mPrefSimInfo != null) {
                    int i2 = this.mSimNum;
                    PreferenceCategory preferenceCategory = this.mPrefSimVoiceDataInfo;
                    if (i2 < 2) {
                        preferenceCategory.removePreference(this.mDefaultSimPre);
                    } else {
                        preferenceCategory.addPreference(this.mDefaultSimPre);
                    }
                }
            }
            DropDownPreference dropDownPreference = this.mPrefDefaultVoice;
            if (dropDownPreference != null) {
                dropDownPreference.setEnabled(this.mActiveSimCount > 1 && !this.mIsAirplaneEnabled);
                this.mPrefDefaultData.setEnabled(this.mActiveSimCount > 1 && !this.mIsAirplaneEnabled);
            }
            boolean z5 = Utils.IS_MIUI_LITE_DEVICE;
            if (z5) {
                updateDropDownList();
            }
            CheckBoxPreference checkBoxPreference2 = this.mButtonDual4G;
            if (checkBoxPreference2 != null) {
                if (this.mSimNum > 1 && MiuiPhoneUtils.DUAL_VOLTE_SUPPORTED) {
                    this.mPrefGeneral.addPreference(checkBoxPreference2);
                } else {
                    this.mPrefGeneral.removePreference(checkBoxPreference2);
                }
            }
            if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isDeviceLockViceNetworkModeForCmcc()) {
                if (SubscriptionManager.INVALID_SUBSCRIPTION_ID != ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getDataSlotForCmcc()) {
                    CheckBoxPreference checkBoxPreference3 = this.mButtonDual4G;
                    if (checkBoxPreference3 != null) {
                        this.mPrefGeneral.removePreference(checkBoxPreference3);
                    }
                } else if (MiuiPhoneUtils.DUAL_VOLTE_SUPPORTED && !MiuiPhoneUtils.isTabletDeviceWithVolteCapability() && this.mSimNum == MiuiPhoneUtils.PHONE_COUNT && (checkBoxPreference = this.mButtonDual4G) != null) {
                    this.mPrefGeneral.addPreference(checkBoxPreference);
                }
            }
            int subscriptionInfoCount = SubscriptionManager.getDefault().getSubscriptionInfoCount();
            if (this.mPrefDefault != null) {
                if (MiuiPhoneUtils.hideSimCallBack(getContext()) || this.mSimNum < 2) {
                    this.mPrefDefault.removePreference(this.mButtonUseLastSimCallBack);
                } else {
                    this.mPrefDefault.addPreference(this.mButtonUseLastSimCallBack);
                }
            }
            this.mButtonUseLastSimCallBack.setEnabled(subscriptionInfoCount > 1);
            SmartCFSwitchView smartCFSwitchView = this.mSmartCFSwitchView;
            if (smartCFSwitchView != null) {
                smartCFSwitchView.updateSmartCFButtonUI();
            }
            if (!this.mIsShowPrefSMSSettings) {
                this.mPrefDefault.removePreference(this.mPrefSMS);
                DefaultSimPreference defaultSimPreference2 = this.mDefaultSimPre;
                if (defaultSimPreference2 != null) {
                    defaultSimPreference2.setSmsEnabled(false);
                }
            } else if (z5) {
                this.mPrefSMS.setEnabled(subscriptionInfoCount > 1);
                if (Utils.isValidatePhoneId(defaultSmsSlotId)) {
                    this.mPrefSMS.setValueIndex(subscriptionInfoCount != 1 ? defaultSmsSlotId : 0);
                }
                setPreferenceSummary(this.mPrefSMS, defaultSmsSlotId);
                this.mPrefDefault.addPreference(this.mPrefSMS);
                this.mPreferenceScreen.addPreference(this.mPrefDefault);
            } else {
                this.mPrefDefault.removePreference(this.mPrefSMS);
                DefaultSimPreference defaultSimPreference3 = this.mDefaultSimPre;
                if (defaultSimPreference3 != null) {
                    if (z4 && !this.mIsAirplaneEnabled && this.mIsShowPrefSMSSettings) {
                        z = true;
                    }
                    defaultSimPreference3.setSmsEnabled(z);
                }
            }
            int preferenceCount = this.mPrefDefault.getPreferenceCount();
            PreferenceScreen preferenceScreen = this.mPreferenceScreen;
            if (preferenceCount == 0) {
                preferenceScreen.removePreference(this.mPrefDefault);
            } else {
                preferenceScreen.addPreference(this.mPrefDefault);
            }
            PreferenceCategory preferenceCategory2 = this.mOtherMobileSettings;
            if (preferenceCategory2 != null && preferenceCategory2.getPreferenceCount() == 0) {
                this.mPreferenceScreen.removePreference(this.mOtherMobileSettings);
            }
            CheckBoxPreference checkBoxPreference4 = this.mButtonSaEnabled;
            if (checkBoxPreference4 != null) {
                checkBoxPreference4.setEnabled(!this.mIsAirplaneEnabled);
            }
        }

        private void updateSimInfoPreference() {
            if (!MiuiPhoneUtils.isMultiSimEnabled() || TelephonyManager.isCustSingleSimDevice()) {
                return;
            }
            SimCacheHelper.getInstance().clear();
            MultiSimPreference multiSimPreference = this.mSimInfoPre;
            if (multiSimPreference != null) {
                multiSimPreference.setSimInfoRecordList(this.mSimInfoRecordList);
                this.mDefaultSimPre.setSimInfoRecordList(this.mSimInfoRecordList);
            }
            if (this.mPrefSimInfo != null) {
                addSimInfoPreference();
            }
            if (this.mIsShowPrefSMSSettings) {
                this.mPrefSMS.notifySimInfoDataChanged();
            }
        }

        private void addSimServicePreference() {
            int i;
            if (this.mPrefSimService != null) {
                return;
            }
            addPreferencesFromResource(R.xml.sim_service);
            this.mPrefSimService = (PreferenceCategory) this.mPreferenceScreen.findPreference("sim_service");
            if (Utils.isSupportMiMobile(getContext())) {
                i = 0;
            } else {
                this.mPrefSimService.removePreference(this.mPreferenceScreen.findPreference("pref_mi_mobile_key"));
                i = 1;
            }
            if (!VirtualSim.isSupportMiSim(getContext())) {
                i++;
                this.mPrefSimService.removePreference(this.mPreferenceScreen.findPreference("pref_mi_sim_key"));
            }
            if (!VirtualSim.isSupportVirtualSim(getContext())) {
                i++;
                this.mPrefSimService.removePreference(this.mPreferenceScreen.findPreference("pref_sim_roaming_key"));
            }
            PreferenceScreen preferenceScreen = this.mPreferenceScreen;
            if (i == 3) {
                preferenceScreen.removePreference(this.mPrefSimService);
                return;
            }
            Preference preferenceFindPreference = preferenceScreen.findPreference("pref_mi_mobile_key");
            this.mPrefMiMobile = preferenceFindPreference;
            if (preferenceFindPreference != null) {
                preferenceFindPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.26
                    @Override // androidx.preference.Preference.OnPreferenceClickListener
                    public boolean onPreferenceClick(Preference preference) {
                        Intent intentForVirtualSim = Utils.getIntentForVirtualSim("pref_mi_mobile_key");
                        if (MobileNetworkSettingsFragment.this.getActivity() instanceof AppCompatActivity) {
                            MultiAppFloatingActivitySwitcher.configureFloatingService(intentForVirtualSim, (AppCompatActivity) MobileNetworkSettingsFragment.this.getActivity());
                        }
                        MobileNetworkSettingsFragment.this.startActivity(intentForVirtualSim);
                        return true;
                    }
                });
            }
            this.mPrefMiSim = this.mPreferenceScreen.findPreference("pref_mi_sim_key");
            this.mPrefMiRoaming = this.mPreferenceScreen.findPreference("pref_sim_roaming_key");
            new Thread(new Runnable() { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.27
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        MobileNetworkSettingsFragment.this.getSimServiceInfo();
                    } catch (Exception e) {
                        Log.d(MobileNetworkSettings.TAG, "getSimServiceInfo() has error: " + e);
                    }
                }
            }).start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateChinaMobilePalmView() {
            if (this.mPrefChinaMobilePalm == null) {
                return;
            }
            if (!MiuiPhoneUtils.supportChinaMobilePalm() || !MiuiPhoneUtils.hasCmccSim()) {
                this.mPreferenceScreen.removePreference(this.mPrefChinaMobilePalm);
            } else {
                this.mPreferenceScreen.addPreference(this.mPrefChinaMobilePalm);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void getSimServiceInfo() {
            final Bundle virtualSimEntranceInfo = MiuiPhoneUtils.getVirtualSimEntranceInfo();
            this.mVsimBundle = virtualSimEntranceInfo;
            if (virtualSimEntranceInfo == null || getActivity() == null) {
                return;
            }
            getActivity().runOnUiThread(new Runnable() { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.28
                @Override // java.lang.Runnable
                public void run() {
                    if (MobileNetworkSettingsFragment.this.getActivity() == null) {
                        return;
                    }
                    if (MobileNetworkSettingsFragment.this.mPrefMiSim != null) {
                        MobileNetworkSettingsFragment.this.mPrefMiSim.setTitle(virtualSimEntranceInfo.getString("msim_title", MobileNetworkSettingsFragment.this.getResources().getString(R.string.mi_sim_settings_title)));
                        MobileNetworkSettingsFragment.this.mPrefMiSim.setSummary(virtualSimEntranceInfo.getString("msim_summary", MobileNetworkSettingsFragment.this.getResources().getString(R.string.mi_sim_settings_summary)));
                    }
                    if (MobileNetworkSettingsFragment.this.mPrefMiRoaming != null) {
                        MobileNetworkSettingsFragment.this.mPrefMiRoaming.setTitle(virtualSimEntranceInfo.getString("vsim_title", MobileNetworkSettingsFragment.this.getResources().getString(R.string.sim_roaming_settings_title)));
                        MobileNetworkSettingsFragment.this.mPrefMiRoaming.setSummary(virtualSimEntranceInfo.getString("vsim_summary", MobileNetworkSettingsFragment.this.getResources().getString(R.string.sim_roaming_settings_summary)));
                    }
                }
            });
        }

        @Override // androidx.fragment.app.Fragment
        public void onActivityResult(int i, int i2, Intent intent) {
            if (i == 17) {
                if (intent.getBooleanExtra("exit_ecm_result", false)) {
                    this.mCdmaOptions.showDialog(this.mClickedPreference);
                }
            } else if (i == 18 && i2 == -1) {
                ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).enableVoiceService(this.mPhone, true);
            }
        }

        @Override // androidx.fragment.app.Fragment
        public void onDestroy() {
            if (this.mContentObserver != null) {
                getContext().getContentResolver().unregisterContentObserver(this.mContentObserver);
                this.mContentObserver = null;
            }
            GsmUmtsOptions gsmUmtsOptions = this.mGsmUmtsOptions;
            if (gsmUmtsOptions != null) {
                gsmUmtsOptions.destroy();
            }
            CdmaOptions cdmaOptions = this.mCdmaOptions;
            if (cdmaOptions != null) {
                cdmaOptions.destroy();
            }
            VolteSwitchView volteSwitchView = this.mVolteSwitchView;
            if (volteSwitchView != null) {
                volteSwitchView.destroy();
            }
            getContext().unregisterReceiver(this.mReceiver);
            dismissTurnOnDual4GPromptDialog();
            WifiCallSwitchView wifiCallSwitchView = this.mWifiCallSwitchView;
            if (wifiCallSwitchView != null) {
                wifiCallSwitchView.destroy();
            }
            SmartCFSwitchView smartCFSwitchView = this.mSmartCFSwitchView;
            if (smartCFSwitchView != null) {
                smartCFSwitchView.destroy();
            }
            SettingsObserver settingsObserver = this.mSettingsObserver;
            if (settingsObserver != null) {
                settingsObserver.unobserve();
                this.mSettingsObserver = null;
            }
            if (MiuiPhoneUtils.isEsimCapable() && this.mEsimEnabledObserver != null) {
                getContext().getContentResolver().unregisterContentObserver(this.mEsimEnabledObserver);
                this.mEsimEnabledObserver = null;
            }
            dismissViceSlotVolteDataPrompt();
            dismissSmartDualSimPrompt();
            dismissSmartSwitchDualSimPrompt();
            dismissProgressDialog();
            super.onDestroy();
        }

        public void onDataSlotReady(boolean z) {
            updatePreferenceUI();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateDual4gButton(String str) {
            if (this.mButtonDual4G != null && MiuiPhoneUtils.isCTDualVolteSupported() && "LOADED".equals(str)) {
                if (MiuiPhoneUtils.isDualCTSim()) {
                    this.mButtonDual4G.setChecked(true);
                    this.mButtonDual4G.setEnabled(false);
                    this.mButtonDual4G.setSummary(R.string.enable_dual_4g_if_dual_ct);
                    return;
                }
                boolean zCannotDisableDual4G = MiuiPhoneUtils.cannotDisableDual4G();
                CheckBoxPreference checkBoxPreference = this.mButtonDual4G;
                if (zCannotDisableDual4G) {
                    checkBoxPreference.setChecked(true);
                    this.mButtonDual4G.setEnabled(false);
                    return;
                }
                checkBoxPreference.setChecked(((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isDual4GEnabled());
                boolean zIsCloudSimEnabled = VirtualSimUtils.isCloudSimEnabled();
                CheckBoxPreference checkBoxPreference2 = this.mButtonDual4G;
                if (zIsCloudSimEnabled) {
                    checkBoxPreference2.setEnabled(false);
                    this.mButtonDual4G.setSummary(R.string.cloud_sim_enabled_dual_4g_summary);
                } else {
                    checkBoxPreference2.setEnabled(true);
                    this.mButtonDual4G.setSummary(R.string.dual_4g_switch_summary);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateDataEnabledButton(String str) {
            CheckBoxPreference checkBoxPreference;
            if (!"LOADED".equals(str) || getContext() == null || (checkBoxPreference = this.mButtonDataEnabled) == null) {
                return;
            }
            checkBoxPreference.setChecked(MiuiPhoneUtils.isMobileDataEnabled(getContext()));
        }

        private void updateDataEnabledButtonByEnterprise() {
            try {
                if (EnterpriseSettings.ENTERPRISE_ACTIVATED || EnterpriseManagerStub.ENTERPRISE_ACTIVATED) {
                    this.mButtonDataEnabled.setEnabled(PhoneHelper.isCellularDataChangeable(getContext()) && PhoneHelperStub.getInstance().isCellularDataChangeable());
                }
            } catch (Exception unused) {
            }
        }

        private void updateFiveGNetworkPreference() {
            if (this.mFiveGPreference == null) {
                return;
            }
            boolean z = false;
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                z = z || MiuiPhoneUtils.showFiveGSwitch(i);
            }
            if (!z || FiveGUtils.isFiveGNetWorkInVisible()) {
                this.mPrefGeneral.removePreference(this.mFiveGPreference);
                this.mFiveGPreference = null;
            }
        }

        private void update5gNetworkMode() {
            boolean zIsUserFiveGEnabled = FiveGManagerBase.getInstance().isUserFiveGEnabled();
            CheckBoxPreference checkBoxPreference = this.mButtonSaEnabled;
            if (checkBoxPreference != null) {
                if (zIsUserFiveGEnabled) {
                    if (this.mPreferenceScreen.findPreference("button_5g_sa_network_key") == null) {
                        this.mPrefGeneral.addPreference(this.mButtonSaEnabled);
                    }
                    this.mButtonSaEnabled.setChecked(FiveGManagerBase.getInstance().isUserFiveGSaEnabled());
                    return;
                }
                this.mPrefGeneral.removePreference(checkBoxPreference);
            }
        }

        private void showDataUsagePromptDialog(final CheckBoxPreference checkBoxPreference) {
            String string;
            if (checkBoxPreference.isChecked()) {
                string = getString(R.string.data_enable_warning);
            } else {
                string = getString(R.string.data_disable_warning_with_mms);
            }
            AlertDialog alertDialogCreate = new AlertDialog.Builder(getContext()).setMessage(string).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.30
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    android.telephony.TelephonyManager.from(MobileNetworkSettingsFragment.this.getContext()).setDataEnabled(checkBoxPreference.isChecked());
                    MiStatInterfaceUtil.recordSwitchStatusEvent(MobileNetworkSettingsFragment.this.mButtonDataEnabled.getKey(), checkBoxPreference.isChecked());
                }
            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MobileNetworkSettings.MobileNetworkSettingsFragment.29
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    CheckBoxPreference checkBoxPreference2 = checkBoxPreference;
                    checkBoxPreference2.setChecked(!checkBoxPreference2.isChecked());
                    MobileNetworkSettingsFragment.this.mDataUsagePromptDialog.dismiss();
                }
            }).setCancelable(false).create();
            this.mDataUsagePromptDialog = alertDialogCreate;
            alertDialogCreate.show();
        }

        private void dismissDataUsagePromptDialog() {
            AlertDialog alertDialog = this.mDataUsagePromptDialog;
            if (alertDialog == null || !alertDialog.isShowing()) {
                return;
            }
            this.mDataUsagePromptDialog.dismiss();
            this.mDataUsagePromptDialog = null;
        }

        private boolean isHighTemperatureProtect() {
            boolean dataEnabled = android.telephony.TelephonyManager.from(getContext()).getDataEnabled();
            if (Build.IS_INTERNATIONAL_BUILD || dataEnabled) {
                return false;
            }
            int millionDigit = getMillionDigit(Settings.Secure.getInt(getContext().getContentResolver(), "thermal_temp_state_value", 0));
            return millionDigit == 1 || millionDigit == 4 || millionDigit == 6 || millionDigit == 7;
        }

        private int getMillionDigit(int i) {
            return (i / AnimState.VIEW_SIZE) % 10;
        }
    }

    /* JADX INFO: renamed from: com.android.phone.settings.MobileNetworkSettings$1, reason: invalid class name */
    abstract /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$phone$MiuiPhoneUtils$DualSimType;

        static {
            int[] iArr = new int[MiuiPhoneUtils.DualSimType.values().length];
            $SwitchMap$com$android$phone$MiuiPhoneUtils$DualSimType = iArr;
            try {
                iArr[MiuiPhoneUtils.DualSimType.NO_VSIM.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$phone$MiuiPhoneUtils$DualSimType[MiuiPhoneUtils.DualSimType.VSIM_SLOT_0.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$phone$MiuiPhoneUtils$DualSimType[MiuiPhoneUtils.DualSimType.VSIM_SLOT_1.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Intent getNetworkAssistantIntent(int i) {
        Intent intent = new Intent("miui.intent.action.NETWORKASSISTANT_LIMIT_SETTING");
        intent.setPackage("com.miui.securitycenter");
        intent.putExtra("sim_slot_num_tag", i);
        return intent;
    }

    public class SimPreferenceClickListener implements Preference.OnPreferenceClickListener {
        private int mNavCode;
        private SimInfoBean mSimInfo;

        private SimPreferenceClickListener(SimInfoBean simInfoBean, int i) {
            this.mSimInfo = simInfoBean;
            this.mNavCode = i;
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            Intent intent = preference.getIntent();
            Context context = preference.getPreferenceManager().getContext();
            if (android.telephony.TelephonyManager.getDefault().getSimState(this.mSimInfo.mSlotId) == 4) {
                Toast.makeText(context, context.getResources().getString(R.string.sim_can_not_read, Integer.valueOf(this.mSimInfo.mSlotId + 1)), 1).show();
            } else {
                int i = this.mNavCode;
                if (i == 1) {
                    Utils$PhoneUtil.enterSimEditor(context, intent, this.mSimInfo.mSlotId);
                } else if (i == 4) {
                    Utils$PhoneUtil.enterESimManager(context, intent, this.mSimInfo.mSlotId);
                }
            }
            return true;
        }
    }
}
