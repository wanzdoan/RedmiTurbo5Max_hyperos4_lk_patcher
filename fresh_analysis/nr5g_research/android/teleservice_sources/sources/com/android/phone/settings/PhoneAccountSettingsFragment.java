package com.android.phone.settings;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Binder;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.phone.PhoneUtils;
import com.android.phone.R;
import com.android.phone.SubscriptionInfoHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class PhoneAccountSettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, AccountSelectionPreference.AccountSelectionListener, Preference.OnPreferenceClickListener {
    private static final String LOG_TAG = PhoneAccountSettingsFragment.class.getSimpleName();
    private PreferenceCategory mAccountList;
    private Preference mAllCallingAccounts;
    private SwitchPreference mButtonPlayingToneForMoCallAccepted;
    private SwitchPreference mButtonVibratingForMoCallAccepted;
    private AccountSelectionPreference mDefaultOutgoingAccount;
    private PreferenceCategory mMakeAndReceiveCallsCategory;
    private boolean mMakeAndReceiveCallsCategoryPresent;
    private SubscriptionManager mSubscriptionManager;
    private TelecomManager mTelecomManager;
    private TelephonyManager mTelephonyManager;
    private PreferenceCategory mVoipCallLogIntegrationCategory;
    private int mCallConnectedIndicator = 0;
    private final SubscriptionManager.OnSubscriptionsChangedListener mOnSubscriptionsChangeListener = new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.phone.settings.PhoneAccountSettingsFragment.1
        @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
        public void onSubscriptionsChanged() {
            Activity activity = PhoneAccountSettingsFragment.this.getActivity();
            if (activity == null || activity.isFinishing()) {
                Log.e(PhoneAccountSettingsFragment.LOG_TAG, "Activity is null or finishing");
            } else {
                PhoneAccountSettingsFragment.this.updateAccounts();
            }
        }
    };

    @Override // com.android.phone.settings.AccountSelectionPreference.AccountSelectionListener
    public void onAccountChanged(AccountSelectionPreference accountSelectionPreference) {
    }

    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mTelecomManager = (TelecomManager) getActivity().getSystemService(TelecomManager.class);
        this.mTelephonyManager = TelephonyManager.from(getActivity());
        this.mSubscriptionManager = SubscriptionManager.from(getActivity());
    }

    @Override // android.app.Fragment
    public void onResume() {
        super.onResume();
        if (getPreferenceScreen() != null) {
            getPreferenceScreen().removeAll();
        }
        addPreferencesFromResource(R.xml.phone_account_settings);
        getView().setFitsSystemWindows(true);
        this.mAccountList = (PreferenceCategory) getPreferenceScreen().findPreference("phone_accounts_accounts_list_category_key");
        this.mVoipCallLogIntegrationCategory = (PreferenceCategory) getPreferenceScreen().findPreference("voip_call_log_integration_category_key");
        this.mDefaultOutgoingAccount = (AccountSelectionPreference) getPreferenceScreen().findPreference("default_outgoing_account");
        Preference preferenceFindPreference = getPreferenceScreen().findPreference("phone_accounts_all_calling_accounts");
        this.mAllCallingAccounts = preferenceFindPreference;
        if (preferenceFindPreference != null) {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setComponent(new ComponentName(getTelecomUiPackageName(), "com.android.server.telecomui.settings.EnableAccountPreferenceActivity"));
            this.mAllCallingAccounts.setIntent(intent);
        }
        PreferenceCategory preferenceCategory = (PreferenceCategory) getPreferenceScreen().findPreference("make_and_receive_calls_settings_category_key");
        this.mMakeAndReceiveCallsCategory = preferenceCategory;
        this.mButtonVibratingForMoCallAccepted = (SwitchPreference) preferenceCategory.findPreference("button_vibrating_for_outgoing_call_accepted_key");
        this.mButtonPlayingToneForMoCallAccepted = (SwitchPreference) this.mMakeAndReceiveCallsCategory.findPreference("button_playing_tone_for_outgoing_call_accepted_key");
        this.mCallConnectedIndicator = this.mTelecomManager.getCallConnectedIndicatorPreference();
        this.mMakeAndReceiveCallsCategoryPresent = false;
        updateAccounts();
        updateVoipCallLogIntegrationSettings();
        updateMakeCallsOptions();
        SubscriptionManager.from(getActivity()).addOnSubscriptionsChangedListener(this.mOnSubscriptionsChangeListener);
    }

    @Override // android.app.Fragment
    public void onPause() {
        SubscriptionManager.from(getActivity()).removeOnSubscriptionsChangedListener(this.mOnSubscriptionsChangeListener);
        super.onPause();
        AccountSelectionPreference accountSelectionPreference = this.mDefaultOutgoingAccount;
        if (accountSelectionPreference != null) {
            accountSelectionPreference.doPause();
        }
    }

    @Override // android.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (!(preference instanceof SwitchPreference)) {
            return false;
        }
        String key = preference.getKey();
        boolean zBooleanValue = ((Boolean) obj).booleanValue();
        if (this.mVoipCallLogIntegrationCategory.findPreference(key) == null) {
            return false;
        }
        this.mTelecomManager.setVoipCallLogIntegrationEnabled(key, zBooleanValue);
        return true;
    }

    @Override // android.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
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
        if (preference != switchPreference2) {
            return false;
        }
        boolean zIsChecked2 = switchPreference2.isChecked();
        int i3 = this.mCallConnectedIndicator;
        int i4 = zIsChecked2 ? i3 | 1 : i3 & (-2);
        this.mCallConnectedIndicator = i4;
        this.mTelecomManager.setCallConnectedIndicatorPreference(i4);
        return true;
    }

    @Override // com.android.phone.settings.AccountSelectionPreference.AccountSelectionListener
    public boolean onAccountSelected(AccountSelectionPreference accountSelectionPreference, PhoneAccountHandle phoneAccountHandle) {
        String str = LOG_TAG;
        Log.d(str, String.format("onAccountSelected:  pref=[%s], account=[%s]", accountSelectionPreference, phoneAccountHandle));
        if (accountSelectionPreference != this.mDefaultOutgoingAccount) {
            return false;
        }
        this.mTelecomManager.setUserSelectedOutgoingPhoneAccount(phoneAccountHandle);
        Log.d(str, "onAccountSelected:" + phoneAccountHandle);
        return true;
    }

    @Override // com.android.phone.settings.AccountSelectionPreference.AccountSelectionListener
    public void onAccountSelectionDialogShow(AccountSelectionPreference accountSelectionPreference) {
        if (accountSelectionPreference == this.mDefaultOutgoingAccount) {
            updateDefaultOutgoingAccountsModel();
        }
    }

    private void updateDefaultOutgoingAccountsModel() {
        this.mDefaultOutgoingAccount.setModel(this.mTelecomManager, getCallingAccounts(true, false), this.mTelecomManager.getUserSelectedOutgoingPhoneAccount(), getString(R.string.phone_accounts_ask_every_time));
    }

    private void initAccountList(List list) {
        Intent intentBuildPhoneAccountConfigureIntent;
        SubscriptionInfo activeSubscriptionInfo;
        boolean zIsMultiSimEnabled = this.mTelephonyManager.isMultiSimEnabled();
        int i = 0;
        if (zIsMultiSimEnabled || !getCallingAccounts(false, false).isEmpty()) {
            ArrayList arrayList = new ArrayList();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                PhoneAccount phoneAccount = this.mTelecomManager.getPhoneAccount((PhoneAccountHandle) it.next());
                if (phoneAccount != null) {
                    arrayList.add(phoneAccount);
                }
            }
            Collections.sort(arrayList, new Comparator() { // from class: com.android.phone.settings.PhoneAccountSettingsFragment.2
                @Override // java.util.Comparator
                public int compare(PhoneAccount phoneAccount2, PhoneAccount phoneAccount3) {
                    int iCompareTo;
                    boolean zHasCapabilities = phoneAccount2.hasCapabilities(4);
                    if (zHasCapabilities != phoneAccount3.hasCapabilities(4)) {
                        iCompareTo = zHasCapabilities ? -1 : 1;
                    } else {
                        iCompareTo = 0;
                    }
                    int subIdForPhoneAccount = PhoneAccountSettingsFragment.this.mTelephonyManager.getSubIdForPhoneAccount(phoneAccount2);
                    int subIdForPhoneAccount2 = PhoneAccountSettingsFragment.this.mTelephonyManager.getSubIdForPhoneAccount(phoneAccount3);
                    if (subIdForPhoneAccount != -1 && subIdForPhoneAccount2 != -1) {
                        SubscriptionManager unused = PhoneAccountSettingsFragment.this.mSubscriptionManager;
                        int slotIndex = SubscriptionManager.getSlotIndex(subIdForPhoneAccount);
                        SubscriptionManager unused2 = PhoneAccountSettingsFragment.this.mSubscriptionManager;
                        iCompareTo = slotIndex < SubscriptionManager.getSlotIndex(subIdForPhoneAccount2) ? -1 : 1;
                    }
                    if (iCompareTo == 0) {
                        iCompareTo = phoneAccount2.getAccountHandle().getComponentName().getPackageName().compareTo(phoneAccount3.getAccountHandle().getComponentName().getPackageName());
                    }
                    if (iCompareTo == 0) {
                        iCompareTo = PhoneAccountSettingsFragment.this.nullToEmpty(phoneAccount2.getLabel().toString()).compareTo(PhoneAccountSettingsFragment.this.nullToEmpty(phoneAccount3.getLabel().toString()));
                    }
                    return iCompareTo == 0 ? phoneAccount2.hashCode() - phoneAccount3.hashCode() : iCompareTo;
                }
            });
            int size = arrayList.size();
            int i2 = 100;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                PhoneAccount phoneAccount2 = (PhoneAccount) obj;
                PhoneAccountHandle accountHandle = phoneAccount2.getAccountHandle();
                if (phoneAccount2.hasCapabilities(4)) {
                    intentBuildPhoneAccountConfigureIntent = null;
                    if (zIsMultiSimEnabled && (activeSubscriptionInfo = this.mSubscriptionManager.getActiveSubscriptionInfo(this.mTelephonyManager.getSubIdForPhoneAccount(phoneAccount2))) != null) {
                        intentBuildPhoneAccountConfigureIntent = new Intent("android.telecom.action.SHOW_CALL_SETTINGS");
                        intentBuildPhoneAccountConfigureIntent.setFlags(67108864);
                        SubscriptionInfoHelper.addExtrasToIntent(intentBuildPhoneAccountConfigureIntent, activeSubscriptionInfo);
                    }
                } else {
                    intentBuildPhoneAccountConfigureIntent = buildPhoneAccountConfigureIntent(getActivity(), accountHandle);
                }
                Preference preference = new Preference(getActivity());
                CharSequence label = phoneAccount2.getLabel();
                preference.setTitle((TextUtils.isEmpty(label) && phoneAccount2.hasCapabilities(4)) ? getString(R.string.phone_accounts_default_account_label) : label);
                Icon icon = phoneAccount2.getIcon();
                if (icon != null) {
                    Drawable drawableLoadDrawable = icon.loadDrawable(getActivity());
                    if (drawableLoadDrawable != null) {
                        Log.d("PhoneAccountSettingsFragment", "icon width:" + drawableLoadDrawable.getIntrinsicWidth() + "icon height:" + drawableLoadDrawable.getIntrinsicHeight() + "account label:" + ((Object) label));
                    }
                    preference.setIcon(drawableLoadDrawable);
                }
                if (intentBuildPhoneAccountConfigureIntent != null) {
                    preference.setIntent(intentBuildPhoneAccountConfigureIntent);
                }
                preference.setOrder(i2);
                this.mAccountList.addPreference(preference);
                i2++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAccounts() {
        PreferenceCategory preferenceCategory = this.mAccountList;
        if (preferenceCategory != null) {
            preferenceCategory.removeAll();
            List callingAccounts = getCallingAccounts(false, true);
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder("allNonSimAccounts size: ");
            sb.append(callingAccounts != null ? Integer.valueOf(callingAccounts.size()) : null);
            Log.d(str, sb.toString());
            List callingAccounts2 = getCallingAccounts(true, false);
            StringBuilder sb2 = new StringBuilder("enabledAccounts size: ");
            sb2.append(callingAccounts2 != null ? Integer.valueOf(callingAccounts2.size()) : null);
            Log.d(str, sb2.toString());
            initAccountList(callingAccounts2);
            int size = callingAccounts2.size();
            PreferenceCategory preferenceCategory2 = this.mMakeAndReceiveCallsCategory;
            if (size > 1) {
                preferenceCategory2.addPreference(this.mDefaultOutgoingAccount);
                this.mMakeAndReceiveCallsCategoryPresent = true;
                this.mDefaultOutgoingAccount.setListener(this);
                updateDefaultOutgoingAccountsModel();
                this.mDefaultOutgoingAccount.setEnabled(true);
            } else {
                preferenceCategory2.removePreference(this.mDefaultOutgoingAccount);
            }
            boolean zIsEmpty = callingAccounts.isEmpty();
            PreferenceCategory preferenceCategory3 = this.mAccountList;
            if (!zIsEmpty) {
                preferenceCategory3.addPreference(this.mAllCallingAccounts);
            } else {
                preferenceCategory3.removePreference(this.mAllCallingAccounts);
            }
        }
    }

    private void updateVoipCallLogIntegrationSettings() {
        PreferenceCategory preferenceCategory = this.mVoipCallLogIntegrationCategory;
        if (preferenceCategory == null) {
            return;
        }
        preferenceCategory.removeAll();
        Map voipCallLogIntegrationStatus = this.mTelecomManager.getVoipCallLogIntegrationStatus();
        if (voipCallLogIntegrationStatus.isEmpty()) {
            getPreferenceScreen().removePreference(this.mVoipCallLogIntegrationCategory);
            return;
        }
        getPreferenceScreen().addPreference(this.mVoipCallLogIntegrationCategory);
        Preference preference = new Preference(getActivity());
        preference.setKey("voip_call_log_pref_summary_key");
        preference.setSummary(R.string.voip_call_log_integration_summary);
        preference.setSelectable(false);
        this.mVoipCallLogIntegrationCategory.addPreference(preference);
        PackageManager packageManager = getActivity().getPackageManager();
        for (Map.Entry entry : voipCallLogIntegrationStatus.entrySet()) {
            String str = (String) entry.getKey();
            boolean zBooleanValue = ((Boolean) entry.getValue()).booleanValue();
            try {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(str, 0);
                SwitchPreference switchPreference = new SwitchPreference(getActivity());
                switchPreference.setKey(str);
                switchPreference.setTitle(packageManager.getApplicationLabel(applicationInfo));
                switchPreference.setIcon(packageManager.getApplicationIcon(applicationInfo));
                switchPreference.setChecked(zBooleanValue);
                switchPreference.setOnPreferenceChangeListener(this);
                this.mVoipCallLogIntegrationCategory.addPreference(switchPreference);
            } catch (PackageManager.NameNotFoundException e) {
                Log.w(LOG_TAG, "Could not find package for VoIP call log integration: " + str, e);
            }
        }
    }

    private List getCallingAccounts(boolean z, boolean z2) {
        PhoneAccountHandle emergencyPhoneAccount = getEmergencyPhoneAccount();
        List callCapablePhoneAccounts = this.mTelecomManager.getCallCapablePhoneAccounts(z2);
        Iterator it = callCapablePhoneAccounts.iterator();
        while (it.hasNext()) {
            PhoneAccountHandle phoneAccountHandle = (PhoneAccountHandle) it.next();
            UserHandle userHandle = phoneAccountHandle.getUserHandle();
            if (phoneAccountHandle.equals(emergencyPhoneAccount)) {
                it.remove();
            } else {
                PhoneAccount phoneAccount = this.mTelecomManager.getPhoneAccount(phoneAccountHandle);
                if (phoneAccount == null) {
                    it.remove();
                } else if (!z && phoneAccount.hasCapabilities(4)) {
                    it.remove();
                } else if (!userHandle.equals(Binder.getCallingUserHandle()) && !phoneAccount.hasCapabilities(32)) {
                    it.remove();
                }
            }
        }
        return callCapablePhoneAccounts;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

    private PhoneAccountHandle getEmergencyPhoneAccount() {
        return PhoneUtils.makePstnPhoneAccountHandleWithPrefix(null, "", true, null);
    }

    public static Intent buildPhoneAccountConfigureIntent(Context context, PhoneAccountHandle phoneAccountHandle) {
        Intent intentBuildConfigureIntent = buildConfigureIntent(context, phoneAccountHandle, "android.telecom.action.CONFIGURE_PHONE_ACCOUNT");
        if (intentBuildConfigureIntent == null && (intentBuildConfigureIntent = buildConfigureIntent(context, phoneAccountHandle, "android.telecom.action.CONNECTION_SERVICE_CONFIGURE")) != null) {
            Log.w(LOG_TAG, "Phone account using old configuration intent: " + phoneAccountHandle);
        }
        Log.d(LOG_TAG, "get intent: " + intentBuildConfigureIntent);
        return intentBuildConfigureIntent;
    }

    private static Intent buildConfigureIntent(Context context, PhoneAccountHandle phoneAccountHandle, String str) {
        if (phoneAccountHandle == null || phoneAccountHandle.getComponentName() == null || TextUtils.isEmpty(phoneAccountHandle.getComponentName().getPackageName())) {
            return null;
        }
        Intent intent = new Intent(str);
        intent.setPackage(phoneAccountHandle.getComponentName().getPackageName());
        intent.addCategory("android.intent.category.DEFAULT");
        intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", phoneAccountHandle);
        if (context.getPackageManager().queryIntentActivities(intent, 0).size() == 0) {
            return null;
        }
        return intent;
    }

    private void updateMakeCallsOptions() {
        if (this.mMakeAndReceiveCallsCategory == null) {
            return;
        }
        Intent launchSmartForwardingMenuIntent = getLaunchSmartForwardingMenuIntent();
        PreferenceCategory preferenceCategory = this.mMakeAndReceiveCallsCategory;
        if (launchSmartForwardingMenuIntent != null) {
            preferenceCategory.findPreference("smart_forwarding_configuration_key").setIntent(launchSmartForwardingMenuIntent);
            this.mMakeAndReceiveCallsCategoryPresent = true;
        } else {
            preferenceCategory.removePreference(getPreferenceScreen().findPreference("smart_forwarding_configuration_key"));
        }
        if (this.mButtonVibratingForMoCallAccepted != null) {
            if (this.mTelephonyManager.isMultiSimEnabled() && getResources().getBoolean(R.bool.show_call_connected_indicator_preference)) {
                this.mButtonVibratingForMoCallAccepted.setChecked((this.mCallConnectedIndicator & 2) > 0);
                this.mButtonVibratingForMoCallAccepted.setOnPreferenceClickListener(this);
                this.mMakeAndReceiveCallsCategoryPresent = true;
            } else {
                this.mMakeAndReceiveCallsCategory.removePreference(this.mButtonVibratingForMoCallAccepted);
            }
        }
        if (this.mButtonPlayingToneForMoCallAccepted != null) {
            if (this.mTelephonyManager.isMultiSimEnabled() && getResources().getBoolean(R.bool.show_call_connected_indicator_preference)) {
                this.mButtonPlayingToneForMoCallAccepted.setChecked((this.mCallConnectedIndicator & 1) > 0);
                this.mButtonPlayingToneForMoCallAccepted.setOnPreferenceClickListener(this);
                this.mMakeAndReceiveCallsCategoryPresent = true;
            } else {
                this.mMakeAndReceiveCallsCategory.removePreference(this.mButtonPlayingToneForMoCallAccepted);
            }
        }
        if (this.mMakeAndReceiveCallsCategoryPresent) {
            return;
        }
        getPreferenceScreen().removePreference(this.mMakeAndReceiveCallsCategory);
    }

    private String getTelecomUiPackageName() {
        Intent intentCreateManageBlockedNumbersIntent = this.mTelecomManager.createManageBlockedNumbersIntent();
        if (intentCreateManageBlockedNumbersIntent != null && intentCreateManageBlockedNumbersIntent.getPackage() != null) {
            return intentCreateManageBlockedNumbersIntent.getPackage();
        }
        Log.w(LOG_TAG, "getTelecomUiPackageName: couldn't resolve");
        return "com.android.server.telecomui";
    }

    private Intent getLaunchSmartForwardingMenuIntent() {
        final CarrierConfigManager carrierConfigManager;
        List<SubscriptionInfo> activeSubscriptionInfoList;
        if (this.mTelephonyManager.getPhoneCount() <= 1 || (carrierConfigManager = (CarrierConfigManager) getActivity().getSystemService("carrier_config")) == null || (activeSubscriptionInfoList = this.mSubscriptionManager.getActiveSubscriptionInfoList()) == null) {
            return null;
        }
        List list = (List) activeSubscriptionInfoList.stream().filter(new Predicate() { // from class: com.android.phone.settings.PhoneAccountSettingsFragment$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return PhoneAccountSettingsFragment.$r8$lambda$Qgoj7HqTxRtYZDxMSkOgwp6xqVU((SubscriptionInfo) obj);
            }
        }).collect(Collectors.toList());
        if (list.size() < 2) {
            return null;
        }
        String str = null;
        for (String str2 : (List) list.stream().map(new Function() { // from class: com.android.phone.settings.PhoneAccountSettingsFragment$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return carrierConfigManager.getConfigForSubId(((SubscriptionInfo) obj).getSubscriptionId());
            }
        }).filter(new Predicate() { // from class: com.android.phone.settings.PhoneAccountSettingsFragment$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return PhoneAccountSettingsFragment.m3304$r8$lambda$BuA4qOG8n4_5QnUv0ZX6ENGqVY((PersistableBundle) obj);
            }
        }).map(new Function() { // from class: com.android.phone.settings.PhoneAccountSettingsFragment$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((PersistableBundle) obj).getString("smart_forwarding_config_component_name_string");
            }
        }).filter(new Predicate() { // from class: com.android.phone.settings.PhoneAccountSettingsFragment$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return PhoneAccountSettingsFragment.$r8$lambda$QJkxc8q_BNViHN166FD_a9lCiNw((String) obj);
            }
        }).collect(Collectors.toList())) {
            if (str == null) {
                str = str2;
            } else if (!str.equals(str2)) {
                Log.w(LOG_TAG, "ignore smart forward component: " + str2);
            }
        }
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setComponent(ComponentName.unflattenFromString(str));
        if (getActivity().getPackageManager().queryIntentActivities(intent, 0).size() == 0) {
            return null;
        }
        return intent;
    }

    public static /* synthetic */ boolean $r8$lambda$Qgoj7HqTxRtYZDxMSkOgwp6xqVU(SubscriptionInfo subscriptionInfo) {
        return !subscriptionInfo.isOpportunistic();
    }

    /* JADX INFO: renamed from: $r8$lambda$BuA4qOG8n4_5QnUv0ZX6EN-GqVY, reason: not valid java name */
    public static /* synthetic */ boolean m3304$r8$lambda$BuA4qOG8n4_5QnUv0ZX6ENGqVY(PersistableBundle persistableBundle) {
        return persistableBundle != null;
    }

    public static /* synthetic */ boolean $r8$lambda$QJkxc8q_BNViHN166FD_a9lCiNw(String str) {
        return !TextUtils.isEmpty(str);
    }
}
