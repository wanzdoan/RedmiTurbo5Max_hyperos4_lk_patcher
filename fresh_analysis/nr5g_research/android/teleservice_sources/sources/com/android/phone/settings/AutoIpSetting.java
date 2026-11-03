package com.android.phone.settings;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MiuiSettings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import com.android.internal.telephony.Phone;
import com.android.phone.R;
import com.android.phone.custompreferences.CustomEditTextPreference;
import gov.nist.javax.sip.address.GenericURI;
import miui.telephony.PhoneNumberUtils;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;
import miuix.appcompat.app.AlertDialog;

/* JADX INFO: loaded from: classes.dex */
public class AutoIpSetting extends BaseActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!SimPickerPreference.showSimPicker(this) && bundle == null) {
            loadFragment(new AutoIpSettingFragment());
        }
    }

    public static String addIpPrefix(Context context, String str, int i) {
        PhoneNumberUtils.PhoneNumber phoneNumber;
        String strReplace;
        String autoIpPrefix = MiuiSettings.Telephony.getAutoIpPrefix(context.getContentResolver(), PhoneNumberUtils.getDefaultIpBySim(context, i), i);
        if (TextUtils.isEmpty(autoIpPrefix) || str.startsWith(autoIpPrefix) || (phoneNumber = PhoneNumberUtils.PhoneNumber.parse(str)) == null || !TextUtils.isEmpty(phoneNumber.getPrefix())) {
            return str;
        }
        if (str.startsWith("+86")) {
            strReplace = str.substring(3);
        } else if (str.startsWith("0086")) {
            strReplace = str.substring(4);
        } else {
            strReplace = str.replace("+", "00");
        }
        String str2 = autoIpPrefix + strReplace;
        phoneNumber.recycle();
        return str2;
    }

    public static String formatNumberWithIp(Context context, String str, int i) {
        String currentAeraCode = MiuiSettings.Telephony.getCurrentAeraCode(context.getContentResolver(), i);
        if (currentAeraCode != null && currentAeraCode.length() >= 2) {
            String autoIpPrefix = MiuiSettings.Telephony.getAutoIpPrefix(context.getContentResolver(), PhoneNumberUtils.getDefaultIpBySim(context, i), i);
            if (!TextUtils.isEmpty(autoIpPrefix)) {
                if (str.startsWith(autoIpPrefix)) {
                    return str;
                }
                PhoneNumberUtils.PhoneNumber phoneNumber = PhoneNumberUtils.PhoneNumber.parse(str);
                if (phoneNumber.getPrefix() != null && phoneNumber.getPrefix().length() == 0) {
                    String countryCode = phoneNumber.getCountryCode();
                    String locationAreaCode = phoneNumber.getLocationAreaCode(context);
                    if (currentAeraCode.startsWith("0")) {
                        currentAeraCode = currentAeraCode.substring(1);
                    }
                    boolean zIsAutoAddZeroPrefix = MiuiSettings.Telephony.isAutoAddZeroPrefix(context.getContentResolver(), i);
                    boolean zIsAutoIpSupportLocalNum = MiuiSettings.Telephony.isAutoIpSupportLocalNum(context.getContentResolver(), i);
                    if ((!TextUtils.isEmpty(locationAreaCode) && (zIsAutoIpSupportLocalNum || !currentAeraCode.equals(locationAreaCode))) || (!TextUtils.isEmpty(countryCode) && !"86".equals(countryCode))) {
                        if (str.startsWith("+86")) {
                            if (phoneNumber.getAreaCode().length() > 0 || (phoneNumber.isNormalMobileNumber() && zIsAutoAddZeroPrefix)) {
                                str = autoIpPrefix + "0" + str.substring(3);
                            } else {
                                str = autoIpPrefix + str.substring(3);
                            }
                        } else if (str.startsWith("0086")) {
                            if (phoneNumber.getAreaCode().length() > 0 || (phoneNumber.isNormalMobileNumber() && zIsAutoAddZeroPrefix)) {
                                str = autoIpPrefix + "0" + str.substring(4);
                            } else {
                                str = autoIpPrefix + str.substring(4);
                            }
                        } else if (phoneNumber.isNormalMobileNumber() && zIsAutoAddZeroPrefix) {
                            str = autoIpPrefix + "0" + str;
                        } else {
                            str = autoIpPrefix + str.replace("+", "00");
                        }
                    }
                }
                phoneNumber.recycle();
            }
        }
        return str;
    }

    public static void updateAutoIpSettings(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        if (MiuiSettings.Telephony.isAutoIpEnable(contentResolver, -1)) {
            for (SubscriptionInfo subscriptionInfo : SubscriptionManager.getDefault().getSubscriptionInfoList()) {
                int slotId = subscriptionInfo.getSlotId();
                MiuiSettings.Telephony.setAutoIpPrefix(contentResolver, MiuiSettings.Telephony.getAutoIpPrefix(contentResolver, PhoneNumberUtils.getDefaultIpBySim(context, slotId), subscriptionInfo.getSubscriptionId()), slotId);
                MiuiSettings.Telephony.setAutoIpEnable(contentResolver, true, slotId);
                MiuiSettings.Telephony.setAutoAddZeroPrefixEnable(contentResolver, MiuiSettings.Telephony.isAutoAddZeroPrefix(contentResolver, -1), slotId);
                MiuiSettings.Telephony.setCurrentAeraCode(contentResolver, MiuiSettings.Telephony.getCurrentAeraCode(contentResolver, -1), slotId);
                MiuiSettings.Telephony.setAutoIpSupportLocalNumEnable(contentResolver, MiuiSettings.Telephony.isAutoIpSupportLocalNum(contentResolver, -1), slotId);
                AutoIpExceptionSetting.saveNumbers(context, slotId, AutoIpExceptionSetting.loadNumbers(context, -1));
            }
            MiuiSettings.Telephony.setAutoIpEnable(contentResolver, false, -1);
        }
    }

    public static Uri handleIpCall(Uri uri, Phone phone, String str, Bundle bundle) {
        Context context = phone.getContext();
        updateAutoIpSettings(context);
        if (!GenericURI.TEL.equals(uri.getScheme())) {
            return uri;
        }
        int phoneId = phone.getPhoneId();
        if (phone.getPhoneType() == 3 || !PhoneNumberUtils.isChineseOperator(TelephonyManager.getDefault().getSimOperatorForSlot(phoneId))) {
            return uri;
        }
        PhoneNumberUtils.PhoneNumber phoneNumber = PhoneNumberUtils.PhoneNumber.parse(str);
        boolean z = (phoneNumber == null || TextUtils.isEmpty(phoneNumber.getPrefix())) ? false : true;
        if (phoneNumber != null) {
            phoneNumber.recycle();
        }
        boolean z2 = bundle.getBoolean("com.android.phone.IS_IPCALL", false);
        Log.i("AutoIpSetting", "handleIpCall: isIpDial=" + z2 + " hasPrefix=" + z);
        String strAddIpPrefix = (!z2 || z) ? str : addIpPrefix(context, str, phoneId);
        if (MiuiSettings.Telephony.isAutoIpEnable(context.getContentResolver(), phoneId) && !AutoIpExceptionSetting.contains(context, strAddIpPrefix, phoneId)) {
            strAddIpPrefix = formatNumberWithIp(context, strAddIpPrefix, phoneId);
        }
        return !str.equals(strAddIpPrefix) ? Uri.fromParts(GenericURI.TEL, strAddIpPrefix, null) : uri;
    }

    public class AutoIpSettingFragment extends BaseActivity.BaseFragment implements Preference.OnPreferenceChangeListener, SubscriptionManager.OnSubscriptionsChangedListener {
        private CheckBoxPreference mAddZeroPrefix;
        private CheckBoxPreference mAutoIpEnable;
        private Preference mAutoIpExceptions;
        private int mSlotId = 0;
        private CheckBoxPreference mSupportLocalNumbers;
        private CustomEditTextPreference mTextCurrentAreaCode;
        private CustomEditTextPreference mTextIpPrefix;

        public AutoIpSettingFragment() {
            SubscriptionManager.getDefault().addOnSubscriptionsChangedListener(this);
        }

        private void removeOnSubscriptionsChangedListener() {
            SubscriptionManager.getDefault().removeOnSubscriptionsChangedListener(this);
        }

        public void onSubscriptionsChanged() {
            updateTextIpPrefix();
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            if (preference == this.mTextIpPrefix) {
                String defaultIpBySim = (String) obj;
                if (TextUtils.isEmpty(defaultIpBySim)) {
                    defaultIpBySim = PhoneNumberUtils.getDefaultIpBySim(getContext(), this.mSlotId);
                }
                MiuiSettings.Telephony.setAutoIpPrefix(getContext().getContentResolver(), defaultIpBySim, this.mSlotId);
                this.mTextIpPrefix.setSummary(getString(R.string.autoip_prefix_summary, defaultIpBySim));
            } else if (preference == this.mAutoIpEnable) {
                boolean zBooleanValue = ((Boolean) obj).booleanValue();
                if (zBooleanValue && TextUtils.isEmpty(MiuiSettings.Telephony.getCurrentAeraCode(getContext().getContentResolver(), this.mSlotId))) {
                    this.mAutoIpEnable.setChecked(true);
                    simulatePreferenceClick(this.mTextCurrentAreaCode);
                } else {
                    MiuiSettings.Telephony.setAutoIpEnable(getContext().getContentResolver(), zBooleanValue, this.mSlotId);
                    closeAreaCodeDialog();
                }
            } else if (preference == this.mTextCurrentAreaCode) {
                String str = (String) obj;
                MiuiSettings.Telephony.setCurrentAeraCode(getContext().getContentResolver(), str, this.mSlotId);
                if (TextUtils.isEmpty(str)) {
                    alertToInputCurrentAreaCode();
                    MiuiSettings.Telephony.setAutoIpEnable(getContext().getContentResolver(), false, this.mSlotId);
                } else {
                    this.mTextCurrentAreaCode.setSummary(getString(R.string.autoip_autoip_current_areacode_summary, str));
                    MiuiSettings.Telephony.setAutoIpEnable(getContext().getContentResolver(), true, this.mSlotId);
                }
            } else if (preference == this.mAddZeroPrefix) {
                MiuiSettings.Telephony.setAutoAddZeroPrefixEnable(getContext().getContentResolver(), ((Boolean) obj).booleanValue(), this.mSlotId);
            } else if (preference == this.mSupportLocalNumbers) {
                MiuiSettings.Telephony.setAutoIpSupportLocalNumEnable(getContext().getContentResolver(), ((Boolean) obj).booleanValue(), this.mSlotId);
            }
            return true;
        }

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, miuix.preference.PreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.preference.PreferenceManager.OnPreferenceTreeClickListener
        public boolean onPreferenceTreeClick(Preference preference) {
            boolean zOnPreferenceTreeClick = super.onPreferenceTreeClick(preference);
            if (preference == this.mAutoIpExceptions) {
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.setClass(getContext(), AutoIpExceptionSetting.class);
                SubscriptionManager.putSlotIdExtra(intent, this.mSlotId);
                startActivity(intent);
            }
            return zOnPreferenceTreeClick;
        }

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            new IntentFilter("com.mediatek.intent.action.ACTION_SUBINFO_RECORD_UPDATED");
            this.mSlotId = SubscriptionManager.getSlotIdExtra(getActivity().getIntent(), SubscriptionManager.getDefault().getDefaultSlotId());
            AutoIpSetting.updateAutoIpSettings(getContext());
            addPreferencesFromResource(R.xml.auto_ip_setting);
            CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference("button_autoip");
            this.mAutoIpEnable = checkBoxPreference;
            checkBoxPreference.setChecked(MiuiSettings.Telephony.isAutoIpEnable(getContext().getContentResolver(), this.mSlotId));
            this.mAutoIpEnable.setOnPreferenceChangeListener(this);
            CustomEditTextPreference customEditTextPreference = (CustomEditTextPreference) findPreference("button_text_autoip_prefix");
            this.mTextIpPrefix = customEditTextPreference;
            customEditTextPreference.setOnPreferenceChangeListener(this);
            CustomEditTextPreference customEditTextPreference2 = (CustomEditTextPreference) findPreference("button_text_autoip_current_areacode");
            this.mTextCurrentAreaCode = customEditTextPreference2;
            customEditTextPreference2.setOnPreferenceChangeListener(this);
            this.mAddZeroPrefix = (CheckBoxPreference) findPreference("button_autoip_add_zero_prefix");
            this.mAddZeroPrefix.setChecked(MiuiSettings.Telephony.isAutoAddZeroPrefix(getContext().getContentResolver(), this.mSlotId));
            this.mAddZeroPrefix.setOnPreferenceChangeListener(this);
            this.mSupportLocalNumbers = (CheckBoxPreference) findPreference("button_autoip_support_local_numbers");
            this.mSupportLocalNumbers.setChecked(MiuiSettings.Telephony.isAutoIpSupportLocalNum(getContext().getContentResolver(), this.mSlotId));
            this.mSupportLocalNumbers.setOnPreferenceChangeListener(this);
            this.mAutoIpExceptions = findPreference("button_autoip_exceptions");
            this.mTextCurrentAreaCode.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.android.phone.settings.AutoIpSetting.AutoIpSettingFragment.1
                @Override // android.content.DialogInterface.OnShowListener
                public void onShow(DialogInterface dialogInterface) {
                    final AlertDialog alertDialog = (AlertDialog) dialogInterface;
                    alertDialog.setCancelable(false);
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.getButton(-2).setOnClickListener(new View.OnClickListener() { // from class: com.android.phone.settings.AutoIpSetting.AutoIpSettingFragment.1.1
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            if (TextUtils.isEmpty(MiuiSettings.Telephony.getCurrentAeraCode(AutoIpSettingFragment.this.getContext().getContentResolver(), AutoIpSettingFragment.this.mSlotId))) {
                                AutoIpSettingFragment.this.mAutoIpEnable.setChecked(false);
                                MiuiSettings.Telephony.setAutoIpEnable(AutoIpSettingFragment.this.getContext().getContentResolver(), false, AutoIpSettingFragment.this.mSlotId);
                                AutoIpSettingFragment.this.mTextCurrentAreaCode.setSummary(R.string.autoip_current_areacode_summary);
                            }
                        }
                    });
                }
            });
        }

        @Override // androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            updateTextIpPrefix();
            String currentAeraCode = MiuiSettings.Telephony.getCurrentAeraCode(getContext().getContentResolver(), this.mSlotId);
            boolean zIsEmpty = TextUtils.isEmpty(currentAeraCode);
            CustomEditTextPreference customEditTextPreference = this.mTextCurrentAreaCode;
            if (zIsEmpty) {
                customEditTextPreference.setSummary(R.string.autoip_current_areacode_summary);
            } else {
                customEditTextPreference.setSummary(getString(R.string.autoip_autoip_current_areacode_summary, currentAeraCode));
                this.mTextCurrentAreaCode.setText(currentAeraCode);
            }
            this.mAutoIpExceptions.setSummary(AutoIpExceptionSetting.getSummary(getContext(), this.mSlotId));
        }

        @Override // androidx.fragment.app.Fragment
        public void onDestroy() {
            super.onDestroy();
            removeOnSubscriptionsChangedListener();
        }

        private void alertToInputCurrentAreaCode() {
            new AlertDialog.Builder(getActivity()).setTitle(R.string.autoip_areacode_required).setIconAttribute(android.R.attr.alertDialogIcon).setCancelable(false).setPositiveButton(R.string.autoip_areacode_required_accept, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.AutoIpSetting.AutoIpSettingFragment.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    AutoIpSettingFragment autoIpSettingFragment = AutoIpSettingFragment.this;
                    autoIpSettingFragment.simulatePreferenceClick(autoIpSettingFragment.mTextCurrentAreaCode);
                }
            }).show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void simulatePreferenceClick(Preference preference) {
            preference.performClick();
        }

        private void closeAreaCodeDialog() {
            Dialog dialog = this.mTextCurrentAreaCode.getDialog();
            if (dialog == null || !dialog.isShowing()) {
                return;
            }
            dialog.dismiss();
        }

        private void updateTextIpPrefix() {
            if (SubscriptionManager.getDefault().getSubscriptionIdForSlot(this.mSlotId) < 0 && getActivity() != null) {
                getActivity().finish();
            }
            if (this.mTextIpPrefix != null || isAdded()) {
                String autoIpPrefix = MiuiSettings.Telephony.getAutoIpPrefix(getContext().getContentResolver(), PhoneNumberUtils.getDefaultIpBySim(getContext(), this.mSlotId), this.mSlotId);
                this.mTextIpPrefix.setText(autoIpPrefix);
                this.mTextIpPrefix.setSummary(getString(R.string.autoip_prefix_summary, autoIpPrefix));
            }
        }
    }
}
