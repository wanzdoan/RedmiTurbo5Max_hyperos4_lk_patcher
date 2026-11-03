package com.android.phone.settings;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.RadioAccessFamily;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.phone.FiveGManagerBase;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.ModuleDelegate;
import com.android.phone.R;
import com.android.phone.SingleFiveGController;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.utils.FiveGUtils;
import com.android.services.telephony.ims.ImsAdapterBase;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.settingsdk.backup.data.KeyStringSettingItem;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.ProgressDialog;
import miuix.preference.R$layout;
import miuix.preference.RadioButtonPreference;

/* JADX INFO: loaded from: classes.dex */
public class PreferredNetworkTypeListPreference extends BaseActivity {
    private static PreferredNetworkTypeListPreferenceFragment mPreferredNetworkTypeListPreferenceFragment;
    private static WeakReference sFragmentWeakRef;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        PreferredNetworkTypeListPreferenceFragment preferredNetworkTypeListPreferenceFragment = (PreferredNetworkTypeListPreferenceFragment) getSupportFragmentManager().findFragmentByTag("miuix.preference.PreferenceFragment.MAIN_FRAGMENT_TAG");
        mPreferredNetworkTypeListPreferenceFragment = preferredNetworkTypeListPreferenceFragment;
        if (bundle == null && preferredNetworkTypeListPreferenceFragment == null) {
            PreferredNetworkTypeListPreferenceFragment preferredNetworkTypeListPreferenceFragment2 = new PreferredNetworkTypeListPreferenceFragment();
            mPreferredNetworkTypeListPreferenceFragment = preferredNetworkTypeListPreferenceFragment2;
            loadFragment(preferredNetworkTypeListPreferenceFragment2);
        }
        sFragmentWeakRef = new WeakReference(mPreferredNetworkTypeListPreferenceFragment);
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int i) {
        if (i != 100) {
            return null;
        }
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(getText(R.string.network_mode_updating_title));
        progressDialog.setMessage(getText(R.string.reading_settings));
        return progressDialog;
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        sFragmentWeakRef = null;
    }

    public static PreferredNetworkTypeListPreferenceFragment getFragment() {
        WeakReference weakReference = sFragmentWeakRef;
        if (weakReference != null) {
            return (PreferredNetworkTypeListPreferenceFragment) weakReference.get();
        }
        return null;
    }

    public class PreferredNetworkTypeListPreferenceFragment extends BaseActivity.BaseFragment implements Preference.OnPreferenceClickListener, FiveGManagerBase.FiveGNetworkModeListener {
        private AlertDialog mAlertDialog = null;
        private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.settings.PreferredNetworkTypeListPreference.PreferredNetworkTypeListPreferenceFragment.5
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                    String stringExtra = intent.getStringExtra("ss");
                    if (SubscriptionManager.getSlotIdExtra(intent, SubscriptionManager.INVALID_SLOT_ID) != PreferredNetworkTypeListPreferenceFragment.this.mSlotId || PreferredNetworkTypeListPreferenceFragment.this.getActivity() == null) {
                        return;
                    }
                    if ("LOADED".equals(stringExtra)) {
                        PreferenceScreen preferenceScreen = PreferredNetworkTypeListPreferenceFragment.this.getPreferenceScreen();
                        PreferredNetworkTypeListPreferenceFragment.this.updatePreferredNetworkTypes(preferenceScreen);
                        PreferredNetworkTypeListPreferenceFragment.this.mItems.clear();
                        PreferredNetworkTypeListPreferenceFragment.this.getAllRadioPreference(preferenceScreen);
                        return;
                    }
                    if (!"ABSENT".equals(stringExtra) || PreferredNetworkTypeListPreferenceFragment.this.getActivity() == null) {
                        return;
                    }
                    PreferredNetworkTypeListPreferenceFragment.this.getActivity().finish();
                    return;
                }
                if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                    boolean booleanExtra = intent.getBooleanExtra(MEModuleSignalT1s.EXTRA_STATE, false);
                    if (!PreferredNetworkTypeListPreferenceFragment.this.mIsAirplaneModeStatus && booleanExtra && PreferredNetworkTypeListPreferenceFragment.this.getActivity() != null) {
                        PreferredNetworkTypeListPreferenceFragment.this.getActivity().finish();
                    }
                    PreferredNetworkTypeListPreferenceFragment.this.mIsAirplaneModeStatus = booleanExtra;
                }
            }
        };
        private int mCurrentNetworkType;
        private MyHandler mHandler;
        private boolean mIsAirplaneModeStatus;
        private ArrayList mItems;
        private int mLteNetworkType;
        private Phone mPhone;
        private int mSlotId;

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            int slotIdExtra = SubscriptionManager.getSlotIdExtra(getActivity().getIntent(), DefaultSimManager.getDefaultDataSlotId());
            this.mSlotId = slotIdExtra;
            this.mPhone = MiuiPhoneUtils.getPhone(slotIdExtra);
            this.mHandler = new MyHandler();
            this.mItems = new ArrayList();
            int phoneType = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getPhoneType(this.mSlotId);
            Log.d("PreferredNetworkTypeListPreference", "PreferredNetworkTypeListPreference: phoneType = " + phoneType);
            loadXml(phoneType);
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            this.mLteNetworkType = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getLteNetworkModeValue(this.mSlotId);
            getAllRadioPreference(preferenceScreen);
            IntentFilter intentFilter = new IntentFilter("android.intent.action.SIM_STATE_CHANGED");
            intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
            getContext().registerReceiver(this.mBroadcastReceiver, intentFilter, 2);
        }

        private void loadXml(int i) {
            PreferenceScreen preferenceScreenCreatePreferenceScreen = getPreferenceManager().createPreferenceScreen(getContext());
            List networkTypeList = getNetworkTypeList();
            if (networkTypeList != null) {
                Resources resourcesForSubId = android.telephony.SubscriptionManager.getResourcesForSubId(this.mPhone.getContext(), this.mPhone.getSubId());
                if (resourcesForSubId != null) {
                    Iterator it = networkTypeList.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            String[] strArrSplit = ((String) it.next()).split(",");
                            if (strArrSplit.length != 2) {
                                Log.e("PreferredNetworkTypeListPreference", "invalid network type list");
                                break;
                            }
                            String str = strArrSplit[0];
                            int identifier = resourcesForSubId.getIdentifier(strArrSplit[1], KeyStringSettingItem.TYPE, getContext().getPackageName());
                            if (identifier == 0) {
                                Log.e("PreferredNetworkTypeListPreference", "Title resource not found for: " + strArrSplit[1]);
                                break;
                            }
                            String string = resourcesForSubId.getString(identifier);
                            Log.i("PreferredNetworkTypeListPreference", "key = " + str + ", title = " + strArrSplit[1] + " : " + string);
                            if (!((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isNrNetworkModeKey(str) || showNrNetworkMode()) {
                                Preference radioButtonPreference = new RadioButtonPreference(getContext());
                                radioButtonPreference.setKey(str);
                                radioButtonPreference.setTitle(string);
                                preferenceScreenCreatePreferenceScreen.addPreference(radioButtonPreference);
                            }
                        } else {
                            setPreferenceScreen(preferenceScreenCreatePreferenceScreen);
                            return;
                        }
                    }
                } else {
                    Log.e("PreferredNetworkTypeListPreference", "get resources failed");
                }
            } else {
                Log.e("PreferredNetworkTypeListPreference", "get preferred_network_type_list failed");
            }
            preferenceScreenCreatePreferenceScreen.removeAll();
            if (i == 2) {
                if (showNrNetworkMode()) {
                    if (MiuiPhoneUtils.isDeviceNotSupportCDMA()) {
                        addPreferencesFromResource(R.xml.preferred_network_type_nr_ct);
                        return;
                    } else {
                        addPreferencesFromResource(R.xml.preferred_network_type_nr_ct_cdma);
                        return;
                    }
                }
                addPreferencesFromResource(R.xml.preferred_network_type_lte_ct);
                return;
            }
            if (MiuiPhoneUtils.isOnlySupportLteNrForSlot(this.mSlotId)) {
                if (showNrNetworkMode()) {
                    addPreferencesFromResource(R.xml.preferred_network_type_nr_lte_only);
                    return;
                } else {
                    addPreferencesFromResource(R.xml.preferred_network_type_lte_only);
                    return;
                }
            }
            if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).showWcdmaOnlyNetworkModes(this.mSlotId)) {
                if (isMtkPlatform() || isXringPlatform()) {
                    if (showNrNetworkMode()) {
                        addPreferencesFromResource(R.xml.preferred_network_type_mtk_nr_lte_wcdma_only);
                        return;
                    } else {
                        addPreferencesFromResource(R.xml.preferred_network_type_mtk_lte_wcdma_only);
                        return;
                    }
                }
                if (showNrNetworkMode()) {
                    addPreferencesFromResource(R.xml.preferred_network_type_nr_lte_wcdma_only);
                    return;
                } else {
                    addPreferencesFromResource(R.xml.preferred_network_type_lte_wcdma_only);
                    return;
                }
            }
            if (isMtkPlatform() || isXringPlatform()) {
                if (showNrNetworkMode()) {
                    addPreferencesFromResource(R.xml.preferred_network_type_mtk_nr);
                    return;
                } else {
                    addPreferencesFromResource(R.xml.preferred_network_type_mtk_m);
                    return;
                }
            }
            if (isSprdPlatform()) {
                if (showNrNetworkMode()) {
                    addPreferencesFromResource(R.xml.preferred_network_type_sprd_nr);
                    return;
                } else {
                    addPreferencesFromResource(R.xml.preferred_network_type_sprd_m);
                    return;
                }
            }
            if (showNrNetworkMode()) {
                addPreferencesFromResource(R.xml.preferred_network_type_nr);
            } else {
                addPreferencesFromResource(R.xml.preferred_network_type_lte);
            }
        }

        private List getNetworkTypeList() {
            List stringArrayForPhoneId = HyperOSCustFeatureResolve.getStringArrayForPhoneId(this.mSlotId, "preferred_network_type_list_for_fiveg_device");
            List stringArrayForPhoneId2 = HyperOSCustFeatureResolve.getStringArrayForPhoneId(this.mSlotId, "preferred_network_type_list_for_fourg_device");
            List stringArrayForPhoneId3 = HyperOSCustFeatureResolve.getStringArrayForPhoneId(this.mSlotId, "preferred_network_type_list");
            if (showNrNetworkMode() && stringArrayForPhoneId != null) {
                Log.i("PreferredNetworkTypeListPreference", "load 5g network type list");
                return stringArrayForPhoneId;
            }
            if (!showNrNetworkMode() && stringArrayForPhoneId2 != null) {
                Log.i("PreferredNetworkTypeListPreference", "load 4g network type list");
                return stringArrayForPhoneId2;
            }
            Log.i("PreferredNetworkTypeListPreference", "load default network type list(Applicable to all models)");
            return stringArrayForPhoneId3;
        }

        private boolean showNrNetworkMode() {
            return MiuiPhoneUtils.showFiveGNetworkMode(this.mSlotId);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void getAllRadioPreference(Preference preference) {
            if (preference != null) {
                if (preference instanceof RadioButtonPreference) {
                    this.mItems.add((RadioButtonPreference) preference);
                    preference.setLayoutResource(R$layout.miuix_preference_radiobutton_two_state_background);
                    preference.setOnPreferenceClickListener(this);
                    String str = (String) preference.getTitle();
                    Resources resourcesForSubId = android.telephony.SubscriptionManager.getResourcesForSubId(this.mPhone.getContext(), this.mPhone.getSubId());
                    if (str.equals(getResources().getString(R.string.preferred_network_type_pref_entries_4g_preferred)) && isShowPrefer4G() && !isUseCustString()) {
                        Log.i("PreferredNetworkTypeListPreference", "config_show_prefer_4g");
                        preference.setTitle(resourcesForSubId.getString(R.string.preferred_network_type_pref_entries_4g_preferred_custom));
                        return;
                    }
                    if (str.equals(getResources().getString(R.string.preferred_network_type_pref_entries_5g_preferred))) {
                        preference.setTitle(resourcesForSubId.getString(R.string.preferred_network_type_pref_entries_5g_preferred));
                        return;
                    }
                    if (str.equals(getResources().getString(R.string.preferred_network_type_pref_entries_4g_preferred))) {
                        preference.setTitle(resourcesForSubId.getString(R.string.preferred_network_type_pref_entries_4g_preferred));
                        return;
                    } else if (str.equals(getResources().getString(R.string.preferred_network_type_pref_entries_3g_preferred))) {
                        preference.setTitle(resourcesForSubId.getString(R.string.preferred_network_type_pref_entries_3g_preferred));
                        return;
                    } else {
                        if (str.equals(getResources().getString(R.string.preferred_network_type_pref_entries_only_2g))) {
                            preference.setTitle(resourcesForSubId.getString(R.string.preferred_network_type_pref_entries_only_2g));
                            return;
                        }
                        return;
                    }
                }
                if (preference instanceof PreferenceGroup) {
                    PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
                    for (int i = 0; i < preferenceGroup.getPreferenceCount(); i++) {
                        getAllRadioPreference(preferenceGroup.getPreference(i));
                    }
                }
            }
        }

        private boolean isUseCustString() {
            return TelephonyManager.isCustForEsVodafone() || TelephonyManager.isCustForFrSfr() || TelephonyManager.isCustForHkH3g();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void checkRadioPreference(String str) {
            checkRadioPreference(str, false);
        }

        private void checkRadioPreference(String str, boolean z) {
            Log.i("PreferredNetworkTypeListPreference", "checkRadioPreference key: " + str + ", forceChecked: " + z + ", mLteNetworkType: " + this.mLteNetworkType + ", is4GNetworkMode: " + is4GNetworkMode(str) + ", is5GNetworkMode: " + is5GNetworkMode(str) + ", isFiveGNetworkModeSelected: " + isFiveGNetworkModeSelected());
            ContentResolver contentResolver = this.mPhone.getContext().getContentResolver();
            StringBuilder sb = new StringBuilder("preferred_network_mode");
            sb.append(this.mPhone.getSubId());
            String strValueOf = String.valueOf(Settings.Global.getInt(contentResolver, sb.toString(), Phone.PREFERRED_NT_MODE));
            if (!z) {
                if (is4GNetworkMode(strValueOf)) {
                    isMasterSimCard(this.mSlotId);
                }
                if (is5GNetworkMode(strValueOf)) {
                    strValueOf = getNrNetworkTypeFromXml();
                }
            }
            ArrayList arrayList = this.mItems;
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                RadioButtonPreference radioButtonPreference = (RadioButtonPreference) obj;
                radioButtonPreference.setChecked(strValueOf.equals(radioButtonPreference.getKey()));
            }
        }

        @Override // androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            if (isAdded()) {
                updatePreferredNetworkTypes(getPreferenceScreen());
                updateNetworkTypeUI();
            }
        }

        public void updateNetworkTypeUI() {
            if (shouldWaitFiveGNetworkModeCompleted() && !FiveGManagerBase.getInstance().isHandle5GNetworkModeCompleted() && getActivity() != null) {
                getActivity().showDialog(100);
                registerFiveGNetworkModeCompleted();
            } else {
                this.mPhone.getAllowedNetworkTypesBitmask(this.mHandler.obtainMessage(0));
            }
        }

        @Override // androidx.fragment.app.Fragment
        public void onPause() {
            super.onPause();
            if (shouldWaitFiveGNetworkModeCompleted()) {
                unregisterFiveGNetworkModeCompleted();
                cancelDialog();
            }
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            for (Phone phone : MiuiPhoneUtils.getPhones()) {
                if (phone != null && phone.getState() != PhoneConstants.State.IDLE) {
                    Toast.makeText(getContext(), R.string.preferred_network_restrict_network_changed_incall, 1).show();
                    checkRadioPreference(String.valueOf(this.mCurrentNetworkType));
                    return true;
                }
            }
            final int i = Integer.parseInt(preference.getKey());
            if (i == 2) {
                if (getActivity() == null || TelephonyManager.isCustForEsVodafone() || TelephonyManager.isCustForHkH3g() || TelephonyManager.isCustForLmClaro() || TelephonyManager.isCustForMxAt()) {
                    handleClick(i);
                    return true;
                }
                new AlertDialog.Builder(getActivity()).setTitle(R.string.preferred_network_type_title).setMessage(R.string.preferred_network_type_3g_only_warning).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.PreferredNetworkTypeListPreference.PreferredNetworkTypeListPreferenceFragment.3
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        PreferredNetworkTypeListPreferenceFragment.this.handleClick(i);
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.PreferredNetworkTypeListPreference.PreferredNetworkTypeListPreferenceFragment.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        PreferredNetworkTypeListPreferenceFragment preferredNetworkTypeListPreferenceFragment = PreferredNetworkTypeListPreferenceFragment.this;
                        preferredNetworkTypeListPreferenceFragment.checkRadioPreference(String.valueOf(preferredNetworkTypeListPreferenceFragment.mCurrentNetworkType));
                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.android.phone.settings.PreferredNetworkTypeListPreference.PreferredNetworkTypeListPreferenceFragment.1
                    @Override // android.content.DialogInterface.OnCancelListener
                    public void onCancel(DialogInterface dialogInterface) {
                        PreferredNetworkTypeListPreferenceFragment preferredNetworkTypeListPreferenceFragment = PreferredNetworkTypeListPreferenceFragment.this;
                        preferredNetworkTypeListPreferenceFragment.checkRadioPreference(String.valueOf(preferredNetworkTypeListPreferenceFragment.mCurrentNetworkType));
                    }
                }).show();
            } else if (i == 7 && MiuiPhoneUtils.isCtVoLTEOnlySupport(this.mPhone) && ImsAdapterBase.getInstance().getEnhanced4gLteModeSetting(this.mPhone.getContext(), this.mSlotId)) {
                checkRadioPreference(String.valueOf(this.mCurrentNetworkType));
                if (this.mAlertDialog == null) {
                    this.mAlertDialog = new AlertDialog.Builder(getActivity()).setMessage(R.string.prefered_network_type_3g_promp).setPositiveButton(R.string.close_volte, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.PreferredNetworkTypeListPreference.PreferredNetworkTypeListPreferenceFragment.4
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i2) {
                            if (MiuiPhoneUtils.DUAL_VOLTE_SUPPORTED) {
                                ImsAdapterBase.getInstance().setEnhanced4gLteModeSetting(PreferredNetworkTypeListPreferenceFragment.this.mPhone.getContext(), false, PreferredNetworkTypeListPreferenceFragment.this.mSlotId);
                            } else {
                                ImsAdapterBase.getInstance().setEnhanced4gLteModeSetting(PreferredNetworkTypeListPreferenceFragment.this.mPhone.getContext(), false);
                            }
                            PreferredNetworkTypeListPreferenceFragment.this.handleClick(i);
                        }
                    }).setNegativeButton(android.R.string.cancel, (DialogInterface.OnClickListener) null).create();
                }
                this.mAlertDialog.show();
            } else {
                handleClick(i);
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleClick(int i) {
            Log.i("PreferredNetworkTypeListPreference", "handleClick networkType=" + i);
            if (handleFiveGModeClick(i)) {
                return;
            }
            boolean zIsSupportEnable2GSwitch = MiuiPhoneUtils.isSupportEnable2GSwitch(this.mPhone);
            boolean enable2GState = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getEnable2GState(this.mPhone);
            Log.i("PreferredNetworkTypeListPreference", " isSupportEnable2GSwitch: " + zIsSupportEnable2GSwitch + ", isChecked: " + enable2GState);
            if (zIsSupportEnable2GSwitch && !enable2GState && i == 1) {
                Log.i("PreferredNetworkTypeListPreference", "2g only, 2g allowed off ");
                Log.i("PreferredNetworkTypeListPreference", "enable 2g ");
                ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setEnable2G(this.mPhone.getPhoneId(), true, null);
            }
            this.mCurrentNetworkType = i;
            checkRadioPreference(String.valueOf(i));
            this.mPhone.setAllowedNetworkTypes(0, RadioAccessFamily.getRafFromNetworkType(this.mCurrentNetworkType), this.mHandler.obtainMessage(1));
        }

        private boolean handleFiveGModeClick(int i) {
            SingleFiveGController singleFiveGController;
            int i2 = 0;
            if ((TelephonyManager.getDefault().isDualNrSupported() || this.mSlotId == DefaultSimManager.getDefaultDataSlotId()) && showNrNetworkMode() && getActivity() != null) {
                if (is5GNetworkMode(i)) {
                    if (TelephonyManager.getDefault().isDualNrSupported() && this.mSlotId != DefaultSimManager.getDefaultDataSlotId()) {
                        FiveGManagerBase.setFivegEnabled(true, this.mSlotId);
                    }
                    if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).shouldAddNrNetworkModes(this.mSlotId)) {
                        this.mCurrentNetworkType = i;
                    } else {
                        this.mCurrentNetworkType = this.mLteNetworkType;
                    }
                    if (FiveGUtils.isSupportSingleFiveG() && (singleFiveGController = SingleFiveGController.getInstance()) != null && singleFiveGController.showEnable5GDialog(this.mCurrentNetworkType)) {
                        ArrayList arrayList = this.mItems;
                        int size = arrayList.size();
                        while (i2 < size) {
                            Object obj = arrayList.get(i2);
                            i2++;
                            RadioButtonPreference radioButtonPreference = (RadioButtonPreference) obj;
                            radioButtonPreference.setChecked(String.valueOf(this.mCurrentNetworkType).equals(radioButtonPreference.getKey()));
                        }
                        return true;
                    }
                    checkRadioPreference(String.valueOf(i), true);
                    Log.i("PreferredNetworkTypeListPreference", "handleFiveGModeClick: from non 5G  to 5G");
                    FiveGManagerBase.getInstance().enableFiveGUserAsync(this.mSlotId, true, this.mCurrentNetworkType);
                    getActivity().finish();
                    return true;
                }
                if (isFiveGNetworkModeSelected()) {
                    if (TelephonyManager.getDefault().isDualNrSupported() && this.mSlotId != DefaultSimManager.getDefaultDataSlotId()) {
                        FiveGManagerBase.setFivegEnabled(false, this.mSlotId);
                        Log.i("PreferredNetworkTypeListPreference", "handleFiveGModeClick dual 5G, vice slot from 5G to none 5G");
                        return false;
                    }
                    this.mCurrentNetworkType = i;
                    checkRadioPreference(String.valueOf(i), true);
                    Log.i("PreferredNetworkTypeListPreference", "handleFiveGModeClick from 5G to none 5G");
                    FiveGManagerBase.getInstance().enableFiveGUserAsync(this.mSlotId, false, i);
                    getActivity().finish();
                    return true;
                }
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isFiveGNetworkModeSelected() {
            int i = this.mCurrentNetworkType;
            return (i == this.mLteNetworkType || is5GNetworkMode(i)) && showNrNetworkMode() && FiveGManagerBase.getInstance().isUserFiveGEnabled(this.mSlotId);
        }

        private boolean is4GNetworkMode(String str) {
            return TextUtils.equals(str, String.valueOf(this.mLteNetworkType));
        }

        private boolean is5GNetworkMode(String str) {
            try {
                return is5GNetworkMode(Integer.valueOf(str).intValue());
            } catch (NumberFormatException unused) {
                Log.e("PreferredNetworkTypeListPreference", "is5GNetworkMode format error, key is " + str);
                return false;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean is5GNetworkMode(int i) {
            for (int i2 : ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getSupportNetworkMode(9)) {
                if (i == i2) {
                    return true;
                }
            }
            return false;
        }

        private boolean isMasterSimCard(int i) {
            return i == DefaultSimManager.getDefaultDataSlotId();
        }

        class MyHandler extends Handler {
            private MyHandler() {
            }

            private void setValue(String str) {
                PreferredNetworkTypeListPreferenceFragment.this.checkRadioPreference(str);
            }

            private void setEnabled(boolean z) {
                PreferredNetworkTypeListPreferenceFragment.this.getPreferenceScreen().setEnabled(z);
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i = message.what;
                if (i == 0) {
                    handleGetPreferredNetworkTypeResponse(message);
                    PreferredNetworkTypeListPreferenceFragment.this.cancelDialog();
                    return;
                }
                if (i == 1) {
                    PreferredNetworkTypeListPreferenceFragment.this.mPhone.setAllowedNetworkTypes(0, RadioAccessFamily.getRafFromNetworkType(PreferredNetworkTypeListPreferenceFragment.this.mCurrentNetworkType), PreferredNetworkTypeListPreferenceFragment.this.mHandler.obtainMessage(2));
                    return;
                }
                if (i == 2) {
                    handleSetPreferredNetworkTypeResponse(message);
                    return;
                }
                if (i == 3) {
                    removeMessages(4);
                    PreferredNetworkTypeListPreferenceFragment.this.mPhone.getAllowedNetworkTypesBitmask(PreferredNetworkTypeListPreferenceFragment.this.mHandler.obtainMessage(0));
                } else {
                    if (i != 4) {
                        return;
                    }
                    PreferredNetworkTypeListPreferenceFragment.this.unregisterFiveGNetworkModeCompleted();
                    PreferredNetworkTypeListPreferenceFragment.this.mPhone.getAllowedNetworkTypesBitmask(PreferredNetworkTypeListPreferenceFragment.this.mHandler.obtainMessage(0));
                }
            }

            private void handleGetPreferredNetworkTypeResponse(Message message) {
                AsyncResult asyncResult = (AsyncResult) message.obj;
                if (asyncResult.exception == null) {
                    PreferredNetworkTypeListPreferenceFragment.this.mCurrentNetworkType = RadioAccessFamily.getNetworkTypeFromRaf(((int[]) asyncResult.result)[0]);
                    forceUpdateNetworkType(PreferredNetworkTypeListPreferenceFragment.this.mCurrentNetworkType);
                    Log.i("PreferredNetworkTypeListPreference", "get preferred network type: " + PreferredNetworkTypeListPreferenceFragment.this.mCurrentNetworkType + ", isFiveGNetworkModeSelected: " + PreferredNetworkTypeListPreferenceFragment.this.isFiveGNetworkModeSelected());
                    setEnabled(true);
                    if (PreferredNetworkTypeListPreferenceFragment.this.isMtkPlatform() || PreferredNetworkTypeListPreferenceFragment.this.isXringPlatform()) {
                        PreferredNetworkTypeListPreferenceFragment preferredNetworkTypeListPreferenceFragment = PreferredNetworkTypeListPreferenceFragment.this;
                        if (preferredNetworkTypeListPreferenceFragment.is5GNetworkMode(preferredNetworkTypeListPreferenceFragment.mCurrentNetworkType)) {
                            String nrNetworkTypeFromXml = PreferredNetworkTypeListPreferenceFragment.this.getNrNetworkTypeFromXml();
                            Log.i("PreferredNetworkTypeListPreference", "get preferred network type, force select 5G mode, mCurrentNetworkType = " + PreferredNetworkTypeListPreferenceFragment.this.mCurrentNetworkType + ", nrMode: " + nrNetworkTypeFromXml);
                            setValue(nrNetworkTypeFromXml);
                            return;
                        }
                    }
                    setValue(String.valueOf(PreferredNetworkTypeListPreferenceFragment.this.mCurrentNetworkType));
                    return;
                }
                Log.i("PreferredNetworkTypeListPreference", "get preferred network type, exception = " + asyncResult.exception);
                setEnabled(false);
                PreferredNetworkTypeListPreferenceFragment.this.checkRadioPreference("");
            }

            private void forceUpdateNetworkType(int i) {
                if (PreferredNetworkTypeListPreferenceFragment.this.mSlotId == DefaultSimManager.getDefaultDataSlotId() || TelephonyManager.getDefault().isDualNrSupported() || !PreferredNetworkTypeListPreferenceFragment.this.is5GNetworkMode(i) || containNetworkType(i) || PreferredNetworkTypeListPreferenceFragment.this.mItems == null || PreferredNetworkTypeListPreferenceFragment.this.mItems.size() == 0) {
                    return;
                }
                PreferredNetworkTypeListPreferenceFragment.this.mCurrentNetworkType = Integer.parseInt(((RadioButtonPreference) PreferredNetworkTypeListPreferenceFragment.this.mItems.get(0)).getKey());
                Log.i("PreferredNetworkTypeListPreference", "force update network type, originNetworkType: " + i + ", currentNetworkType: " + PreferredNetworkTypeListPreferenceFragment.this.mCurrentNetworkType);
            }

            private boolean containNetworkType(int i) {
                ArrayList arrayList = PreferredNetworkTypeListPreferenceFragment.this.mItems;
                int size = arrayList.size();
                int i2 = 0;
                while (i2 < size) {
                    Object obj = arrayList.get(i2);
                    i2++;
                    if (Integer.parseInt(((RadioButtonPreference) obj).getKey()) == i) {
                        return true;
                    }
                }
                return false;
            }

            private void handleSetPreferredNetworkTypeResponse(Message message) {
                AsyncResult asyncResult = (AsyncResult) message.obj;
                if (asyncResult.exception != null) {
                    setEnabled(false);
                    Log.i("PreferredNetworkTypeListPreference", "set preferred network type, exception = " + asyncResult.exception);
                    PreferredNetworkTypeListPreferenceFragment.this.mPhone.getAllowedNetworkTypesBitmask(obtainMessage(0));
                } else {
                    Log.i("PreferredNetworkTypeListPreference", "set preferred network type done");
                    if (PreferredNetworkTypeListPreferenceFragment.this.isMtkPlatform() || PreferredNetworkTypeListPreferenceFragment.this.isXringPlatform()) {
                        Settings.Global.putInt(PreferredNetworkTypeListPreferenceFragment.this.mPhone.getContext().getContentResolver(), "preferred_network_mode" + PreferredNetworkTypeListPreferenceFragment.this.mPhone.getSubId(), PreferredNetworkTypeListPreferenceFragment.this.mCurrentNetworkType);
                        if (MiuiPhoneUtils.DUAL_VOLTE_SUPPORTED) {
                            android.telephony.TelephonyManager.putIntAtIndex(PreferredNetworkTypeListPreferenceFragment.this.mPhone.getContext().getContentResolver(), "preferred_network_mode", PreferredNetworkTypeListPreferenceFragment.this.mSlotId, PreferredNetworkTypeListPreferenceFragment.this.mCurrentNetworkType);
                        }
                    } else {
                        MiuiPhoneUtils.setNetworkModeInDb(null, PreferredNetworkTypeListPreferenceFragment.this.mSlotId, PreferredNetworkTypeListPreferenceFragment.this.mCurrentNetworkType);
                    }
                }
                if (PreferredNetworkTypeListPreferenceFragment.this.getActivity() != null) {
                    PreferredNetworkTypeListPreferenceFragment.this.getActivity().finish();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updatePreferredNetworkTypes(PreferenceScreen preferenceScreen) {
            Preference preferenceFindPreference;
            if (preferenceScreen == null || !isAdded()) {
                return;
            }
            if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isDisableLte() || (this.mSlotId != DefaultSimManager.getDefaultDataSlotId() && !((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isDual4GEnabled() && ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).shouldShowViceNetworkTypePref())) {
                int[] supportNetworkMode = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getSupportNetworkMode(7);
                int[] supportNetworkMode2 = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getSupportNetworkMode(9);
                int length = supportNetworkMode.length + supportNetworkMode2.length;
                int[] iArr = new int[length];
                System.arraycopy(supportNetworkMode, 0, iArr, 0, supportNetworkMode.length);
                System.arraycopy(supportNetworkMode2, 0, iArr, supportNetworkMode.length, supportNetworkMode2.length);
                for (int i = 0; i < length; i++) {
                    Preference preferenceFindPreference2 = findPreference(Integer.toString(iArr[i]));
                    if (preferenceFindPreference2 != null) {
                        preferenceScreen.removePreference(preferenceFindPreference2);
                    }
                }
            }
            if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isNetworkModeLteOnly(this.mSlotId)) {
                int[] supportNetworkMode3 = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getSupportNetworkMode(11);
                int[] supportNetworkMode4 = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getSupportNetworkMode(0);
                int length2 = supportNetworkMode3.length + supportNetworkMode4.length;
                int[] iArr2 = new int[length2];
                System.arraycopy(supportNetworkMode3, 0, iArr2, 0, supportNetworkMode3.length);
                System.arraycopy(supportNetworkMode4, 0, iArr2, supportNetworkMode3.length, supportNetworkMode4.length);
                for (int i2 = 0; i2 < length2; i2++) {
                    Preference preferenceFindPreference3 = findPreference(Integer.toString(iArr2[i2]));
                    if (preferenceFindPreference3 != null) {
                        preferenceScreen.removePreference(preferenceFindPreference3);
                    }
                }
            }
            if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isRemoveCdma(this.mSlotId)) {
                for (int i3 : ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getSupportNetworkMode(3)) {
                    Preference preferenceFindPreference4 = findPreference(Integer.toString(i3));
                    if (preferenceFindPreference4 != null) {
                        preferenceScreen.removePreference(preferenceFindPreference4);
                    }
                }
            }
            if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isRemove2GOnly(this.mSlotId) && (preferenceFindPreference = findPreference(Integer.toString(1))) != null) {
                preferenceScreen.removePreference(preferenceFindPreference);
            }
            if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isRemovePrefer3G(this.mSlotId)) {
                for (int i4 : ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getSupportNetworkMode(11)) {
                    Preference preferenceFindPreference5 = findPreference(Integer.toString(i4));
                    if (preferenceFindPreference5 != null) {
                        preferenceScreen.removePreference(preferenceFindPreference5);
                    }
                }
            }
            if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isRemoveTdScdma(this.mSlotId)) {
                for (int i5 : ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getSupportNetworkMode(10)) {
                    Preference preferenceFindPreference6 = findPreference(Integer.toString(i5));
                    if (preferenceFindPreference6 != null) {
                        preferenceScreen.removePreference(preferenceFindPreference6);
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getNrNetworkTypeFromXml() {
            for (int i : ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getSupportNetworkMode(9)) {
                Preference preferenceFindPreference = findPreference(Integer.toString(i));
                if (preferenceFindPreference != null) {
                    return preferenceFindPreference.getKey();
                }
            }
            return "-1";
        }

        private boolean shouldWaitFiveGNetworkModeCompleted() {
            return showNrNetworkMode() && MiuiPhoneUtils.shouldRadioOffBeforeChangeFiveGState(this.mPhone);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void cancelDialog() {
            if (getActivity() != null) {
                getActivity().removeDialog(100);
            }
        }

        private void registerFiveGNetworkModeCompleted() {
            FiveGManagerBase.getInstance().registerFiveGNetworkModeCompleted(this);
            this.mHandler.sendEmptyMessageDelayed(4, 10000L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void unregisterFiveGNetworkModeCompleted() {
            FiveGManagerBase.getInstance().unregisterFiveGNetworkModeCompleted();
            this.mHandler.removeMessages(4);
        }

        @Override // com.android.phone.FiveGManagerBase.FiveGNetworkModeListener
        public void onFiveGNetworkModeCompleted() {
            unregisterFiveGNetworkModeCompleted();
            this.mHandler.sendEmptyMessage(3);
        }

        @Override // androidx.fragment.app.Fragment
        public void onDestroy() {
            super.onDestroy();
            getContext().unregisterReceiver(this.mBroadcastReceiver);
            AlertDialog alertDialog = this.mAlertDialog;
            if (alertDialog == null || !alertDialog.isShowing()) {
                return;
            }
            this.mAlertDialog.dismiss();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isMtkPlatform() {
            return ModemProvider.IS_MTK;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isXringPlatform() {
            return ModemProvider.IS_XRING;
        }

        private boolean isSprdPlatform() {
            return ModemProvider.IS_SPRD;
        }

        private boolean isShowPrefer4G() {
            return CarrierConfigManagerEx.getBooleanConfigForSubId(this.mPhone.getContext(), this.mPhone.getSubId(), "config_show_prefer_4g", false);
        }
    }
}
