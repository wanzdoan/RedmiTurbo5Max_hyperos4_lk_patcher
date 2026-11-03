package com.android.phone.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.RadioAccessFamily;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import com.android.internal.telephony.Phone;
import com.android.internal.util.ArrayUtils;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.utils.Utils;
import com.android.services.telephony.ims.ImsAdapterBase;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import miui.os.Build;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.app.AlertDialog;
import miuix.preference.PreferenceFragment;

/* JADX INFO: loaded from: classes.dex */
public class VolteSwitchView {
    private static final boolean DUAL_VOLTE_SUPPORTED = MiuiPhoneUtils.DUAL_VOLTE_SUPPORTED;
    public static final String TAG = "VolteSwitchView";
    private CheckBoxPreference mButtonVolte;
    private final Handler mHandler;
    private boolean mIsAirplaneEnabled;
    private Phone mPhone;
    private PreferenceGroup mPrefGroup;
    private PreferenceFragment mPreferenceFragment;
    private AlertDialog mPromptDialog = null;
    private AlertDialog mPromptDialogCdmaVolte = null;
    private AlertDialog mPromptDialogVolteGlobalization = null;
    private final BroadcastReceiver mReceiver;

    public VolteSwitchView(PreferenceFragment preferenceFragment, PreferenceGroup preferenceGroup, Phone phone) {
        this.mPhone = null;
        this.mIsAirplaneEnabled = false;
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.settings.VolteSwitchView.4
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.d(VolteSwitchView.TAG, "onReceive action=" + action);
                if ("android.telephony.action.CARRIER_CONFIG_CHANGED".equals(action)) {
                    int intExtra = intent.getIntExtra("phone", SubscriptionManager.INVALID_PHONE_ID);
                    if (SubscriptionManager.isValidPhoneId(intExtra)) {
                        if ((VolteSwitchView.DUAL_VOLTE_SUPPORTED || intExtra != DefaultSimManager.getDefaultDataSlotId()) && !(VolteSwitchView.DUAL_VOLTE_SUPPORTED && intExtra == VolteSwitchView.this.mPhone.getPhoneId())) {
                            return;
                        }
                        VolteSwitchView.this.updateVolteButtonUI();
                        return;
                    }
                    return;
                }
                if ("com.android.ims.IMS_SERVICE_UP".equals(action)) {
                    VolteSwitchView.this.mHandler.sendMessageDelayed(VolteSwitchView.this.mHandler.obtainMessage(100), 1500L);
                    return;
                }
                if ("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED".equals(action) || "miui.intent.action.ACTION_DEFAULT_DATA_SLOT_READY".equals(action)) {
                    VolteSwitchView.this.updateVolteButtonUI();
                } else if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                    VolteSwitchView.this.mIsAirplaneEnabled = intent.getBooleanExtra(MEModuleSignalT1s.EXTRA_STATE, false);
                    VolteSwitchView.this.updateVolteButtonUI();
                }
            }
        };
        this.mReceiver = broadcastReceiver;
        this.mHandler = new Handler() { // from class: com.android.phone.settings.VolteSwitchView.5
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i = message.what;
                if (i == 100) {
                    VolteSwitchView.this.updateVolteButtonUI();
                } else {
                    if (i != 101) {
                        return;
                    }
                    MiuiPhoneUtils.maybeShowTurnOffVolteForCall(VolteSwitchView.DUAL_VOLTE_SUPPORTED ? VolteSwitchView.this.mPhone : MiuiPhoneUtils.getDataPhone());
                }
            }
        };
        this.mPreferenceFragment = preferenceFragment;
        this.mPrefGroup = preferenceGroup;
        this.mPhone = phone;
        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) preferenceGroup.findPreference("enhanced_4g_lte");
        this.mButtonVolte = checkBoxPreference;
        checkBoxPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.phone.settings.VolteSwitchView.1
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                if (VolteSwitchView.this.mPreferenceFragment == null) {
                    Log.d(VolteSwitchView.TAG, "mPrefActivity == null");
                    return false;
                }
                if (MiuiPhoneUtils.isPhoneInCall()) {
                    Toast.makeText(VolteSwitchView.this.mPreferenceFragment.getContext(), R.string.msim_set_sub_not_supported_phone_in_call, 0).show();
                    VolteSwitchView.this.mButtonVolte.setChecked(!VolteSwitchView.this.mButtonVolte.isChecked());
                    return false;
                }
                if (VolteSwitchView.DUAL_VOLTE_SUPPORTED) {
                    Phone unused = VolteSwitchView.this.mPhone;
                } else {
                    MiuiPhoneUtils.getDataPhone();
                }
                if (VolteSwitchView.this.mButtonVolte.isChecked() && ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getGlobalizationCarrierConfigMap().containsKey(MiuiPhoneUtils.getOperatorNumeric(VolteSwitchView.this.mPhone))) {
                    VolteSwitchView.this.showDialogWhenVolteGlobalizationTurnOn();
                } else {
                    VolteSwitchView.this.showDual4gDialog();
                    VolteSwitchView.this.setEnhanced4gLteMode();
                }
                return true;
            }
        });
        IntentFilter intentFilter = new IntentFilter("com.android.ims.IMS_SERVICE_UP");
        intentFilter.addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        if (!DUAL_VOLTE_SUPPORTED) {
            intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
            intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_READY");
        }
        this.mPreferenceFragment.getContext().registerReceiver(broadcastReceiver, intentFilter, 2);
        this.mIsAirplaneEnabled = Settings.System.getInt(PhoneGlobals.getInstance().getContentResolver(), "airplane_mode_on", -1) == 1;
        initPreferenceUI();
    }

    private void initPreferenceUI() {
        if (this.mButtonVolte != null) {
            Resources resourcesForSubId = android.telephony.SubscriptionManager.getResourcesForSubId(PhoneGlobals.getInstance().getApplicationContext(), this.mPhone.getSubId());
            this.mButtonVolte.setTitle(resourcesForSubId.getString(R.string.volte_switch_title));
            this.mButtonVolte.setSummary(resourcesForSubId.getString(R.string.volte_switch_summary));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDual4gDialog() {
        Phone phone;
        if (DUAL_VOLTE_SUPPORTED) {
            int preferredNetworkModeFromDb = ModemProvider.getInstance().getPreferredNetworkModeFromDb(this.mPhone.getPhoneId());
            int[] supportNetworkMode = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getSupportNetworkMode(7);
            int[] supportNetworkMode2 = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getSupportNetworkMode(9);
            int[] iArr = new int[supportNetworkMode.length + supportNetworkMode2.length];
            System.arraycopy(supportNetworkMode, 0, iArr, 0, supportNetworkMode.length);
            System.arraycopy(supportNetworkMode2, 0, iArr, supportNetworkMode.length, supportNetworkMode2.length);
            if (!this.mButtonVolte.isChecked() || (phone = this.mPhone) == null || phone.getPhoneId() == DefaultSimManager.getDefaultDataSlotId()) {
                return;
            }
            if (!((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isDual4GEnabled()) {
                AlertDialog alertDialogCreate = new AlertDialog.Builder(this.mPreferenceFragment.getContext()).setMessage(R.string.enable_dual_4g_prompt_when_turn_on_volte).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.VolteSwitchView.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setDual4GMode(true, new Utils.Dual4GCallBack() { // from class: com.android.phone.settings.VolteSwitchView.2.1
                            @Override // com.android.phone.utils.Utils.Dual4GCallBack
                            public void onSetDual4GResult(boolean z, int i2) {
                                Log.d(VolteSwitchView.TAG, "onSetDual4GResult enabled=" + z + ", resultCode=" + i2);
                                if (i2 != 0) {
                                    Toast.makeText(VolteSwitchView.this.mPreferenceFragment.getContext(), z ? R.string.turn_on_dual_4g_failed : R.string.turn_off_dual_4g_failed, 0).show();
                                }
                            }
                        });
                    }
                }).setNegativeButton(android.R.string.cancel, (DialogInterface.OnClickListener) null).setCancelable(false).create();
                this.mPromptDialog = alertDialogCreate;
                alertDialogCreate.show();
                Log.d(TAG, "show dual 4g enable dialog");
                return;
            }
            if (ArrayUtils.contains(iArr, preferredNetworkModeFromDb) || !MiuiPhoneUtils.isRemoveDual4GSwitch() || MiuiPhoneUtils.isTrVersion()) {
                return;
            }
            final int subId = this.mPhone.getSubId();
            final int phoneId = this.mPhone.getPhoneId();
            final int i = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getSupportModes()[phoneId][0];
            new Thread() { // from class: com.android.phone.settings.VolteSwitchView.3
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    TelephonyManager telephonyManagerCreateForSubscriptionId;
                    TelephonyManager telephonyManagerFrom = TelephonyManager.from(VolteSwitchView.this.mPreferenceFragment.getContext());
                    if (telephonyManagerFrom == null || (telephonyManagerCreateForSubscriptionId = telephonyManagerFrom.createForSubscriptionId(subId)) == null) {
                        return;
                    }
                    ModemProvider.getInstance().setNetworkModeInDb(phoneId, i);
                    telephonyManagerCreateForSubscriptionId.setAllowedNetworkTypesForReason(0, RadioAccessFamily.getRafFromNetworkType(i));
                }
            }.start();
        }
    }

    public void updateVolteButtonUI() {
        if (this.mButtonVolte == null || this.mPreferenceFragment == null) {
            return;
        }
        boolean z = this.mPrefGroup.findPreference("enhanced_4g_lte") != null;
        if (MiuiPhoneUtils.shouldHideEnhanced4gLteSettings(this.mPreferenceFragment.getContext(), this.mPhone) || !MiuiPhoneUtils.isIccCardActivated(this.mPhone.getPhoneId())) {
            if (z) {
                this.mPrefGroup.removePreference(this.mButtonVolte);
                return;
            }
            return;
        }
        if (!z) {
            this.mPrefGroup.addPreference(this.mButtonVolte);
        }
        this.mButtonVolte.setEnabled(MiuiPhoneUtils.shouldEnableEnhanced4gLteSettings(this.mPreferenceFragment.getContext(), this.mPhone));
        AlertDialog alertDialog = this.mPromptDialog;
        if (alertDialog == null || !alertDialog.isShowing()) {
            AlertDialog alertDialog2 = this.mPromptDialogCdmaVolte;
            if (alertDialog2 == null || !alertDialog2.isShowing()) {
                AlertDialog alertDialog3 = this.mPromptDialogVolteGlobalization;
                if (alertDialog3 == null || !alertDialog3.isShowing()) {
                    initPreferenceUI();
                    boolean zIsVolteEnabledByPlatform = MiuiPhoneUtils.isVolteEnabledByPlatform(this.mPhone);
                    Log.d(TAG, "updateVolteButtonUI simSupported=" + zIsVolteEnabledByPlatform);
                    boolean zHasSimInserted = hasSimInserted();
                    if (zIsVolteEnabledByPlatform || !zHasSimInserted) {
                        Phone dataPhone = DUAL_VOLTE_SUPPORTED ? this.mPhone : MiuiPhoneUtils.getDataPhone();
                        this.mButtonVolte.setChecked(MiuiPhoneUtils.isEnhanced4gLteModeSettingEnabledByUser(dataPhone));
                        if (MiuiPhoneUtils.isCTDualVolteSupported() && MiuiPhoneUtils.isViceSlotOfDualCTSim(dataPhone.getPhoneId())) {
                            this.mButtonVolte.setSummary(R.string.enable_vice_volte_if_dual_ct);
                        } else {
                            this.mButtonVolte.setSummary(MiuiPhoneUtils.isCtVoLTEOnlySupport(dataPhone) ? R.string.volte_switch_summary2 : R.string.volte_switch_summary);
                        }
                    } else {
                        this.mButtonVolte.setChecked(false);
                        boolean z2 = DUAL_VOLTE_SUPPORTED;
                        CheckBoxPreference checkBoxPreference = this.mButtonVolte;
                        if (z2) {
                            checkBoxPreference.setSummary(R.string.volte_switch_not_support_summary2);
                        } else {
                            checkBoxPreference.setSummary(R.string.volte_switch_not_support_summary);
                        }
                    }
                    if (Build.IS_CT_CUSTOMIZATION_TEST) {
                        this.mButtonVolte.setTitle(R.string.volte_switch_title_ct);
                    }
                }
            }
        }
    }

    public boolean isVolteSwitchOn() {
        CheckBoxPreference checkBoxPreference = this.mButtonVolte;
        return checkBoxPreference != null && checkBoxPreference.isChecked();
    }

    public boolean isVolteSwitchExist() {
        return this.mPrefGroup.findPreference("enhanced_4g_lte") != null;
    }

    private void setEnhanced4gLteModeSetting() {
        if (DUAL_VOLTE_SUPPORTED) {
            ImsAdapterBase.getInstance().setEnhanced4gLteModeSetting(this.mPreferenceFragment.getContext(), this.mButtonVolte.isChecked(), this.mPhone.getPhoneId());
            ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).saveUserSelectedVolteState(this.mPhone.getSubId(), this.mButtonVolte.isChecked());
            return;
        }
        ImsAdapterBase.getInstance().setEnhanced4gLteModeSetting(this.mPreferenceFragment.getContext(), this.mButtonVolte.isChecked());
        Phone dataPhone = MiuiPhoneUtils.getDataPhone();
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).saveUserSelectedVolteState(dataPhone.getSubId(), this.mButtonVolte.isChecked());
        PhoneGlobals.getInstance();
        if (this.mButtonVolte.isChecked()) {
            MiuiPhoneUtils.isCTVoLTESupport(dataPhone);
        }
    }

    private boolean hasSimInserted() {
        for (Phone phone : MiuiPhoneUtils.getPhones()) {
            if (phone.getIccCard() != null && phone.getIccCard().hasIccCard()) {
                return true;
            }
        }
        return false;
    }

    public void destroy() {
        PreferenceFragment preferenceFragment;
        this.mHandler.removeMessages(100);
        if (this.mReceiver != null && (preferenceFragment = this.mPreferenceFragment) != null) {
            preferenceFragment.getContext().unregisterReceiver(this.mReceiver);
            this.mPreferenceFragment = null;
        }
        AlertDialog alertDialog = this.mPromptDialog;
        if (alertDialog != null && alertDialog.isShowing()) {
            this.mPromptDialog.dismiss();
        }
        AlertDialog alertDialog2 = this.mPromptDialogCdmaVolte;
        if (alertDialog2 != null && alertDialog2.isShowing()) {
            this.mPromptDialogCdmaVolte.dismiss();
        }
        AlertDialog alertDialog3 = this.mPromptDialogVolteGlobalization;
        if (alertDialog3 == null || !alertDialog3.isShowing()) {
            return;
        }
        this.mPromptDialogVolteGlobalization.dismiss();
        this.mPromptDialogVolteGlobalization = null;
    }

    public static void removeVolteSwitch(PreferenceGroup preferenceGroup) {
        Preference preferenceFindPreference;
        if (preferenceGroup == null || (preferenceFindPreference = preferenceGroup.findPreference("enhanced_4g_lte")) == null) {
            return;
        }
        preferenceGroup.removePreference(preferenceFindPreference);
    }

    public void setVolteSwitch(boolean z) {
        if (this.mButtonVolte != null) {
            Log.d(TAG, "setVolteSwitch enabled=" + z);
            this.mButtonVolte.setChecked(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setEnhanced4gLteMode() {
        int i;
        try {
            setEnhanced4gLteModeSetting();
        } catch (Exception e) {
            Log.e(TAG, "setEnhanced4gLteModeSetting exception=" + e);
            Context context = this.mPreferenceFragment.getContext();
            if (this.mButtonVolte.isChecked()) {
                i = R.string.enhanced_4g_lte_mode_on_failed;
            } else {
                i = R.string.enhanced_4g_lte_mode_off_failed;
            }
            Toast.makeText(context, i, 0).show();
            CheckBoxPreference checkBoxPreference = this.mButtonVolte;
            checkBoxPreference.setChecked(!checkBoxPreference.isChecked());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDialogWhenVolteGlobalizationTurnOn() {
        if (this.mPromptDialogVolteGlobalization == null) {
            this.mPromptDialogVolteGlobalization = new AlertDialog.Builder(this.mPreferenceFragment.getContext()).setMessage(R.string.volte_switch_globalization_prompt).setTitle(R.string.volte_switch_globalization_prompt_title).setCancelable(false).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.VolteSwitchView.10
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    VolteSwitchView.this.setEnhanced4gLteMode();
                }
            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.VolteSwitchView.9
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    VolteSwitchView.this.mButtonVolte.setChecked(!VolteSwitchView.this.mButtonVolte.isChecked());
                }
            }).create();
        }
        this.mPromptDialogVolteGlobalization.show();
        Log.i(TAG, "showDialogWhenVolteGlobalizationTurnOn");
    }
}
