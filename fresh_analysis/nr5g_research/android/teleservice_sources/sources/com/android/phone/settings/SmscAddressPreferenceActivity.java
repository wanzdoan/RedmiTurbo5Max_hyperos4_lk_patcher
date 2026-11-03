package com.android.phone.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import com.android.phone.TelephonyCapabilities;
import com.android.phone.custompreferences.MiuixTextPreference;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import miui.telephony.PhoneNumberUtils;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public class SmscAddressPreferenceActivity extends BaseActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            loadFragment(new SmscAddressPreferenceFargment());
        }
    }

    public class SmscAddressPreferenceFargment extends BaseActivity.BaseFragment {
        private Context mContext;
        private PreferenceCategory mSim1Cate;
        private SubscriptionInfo mSim1Info;
        private MiuixTextPreference mSim1LocationPref;
        private MiuixTextPreference mSim1OperatorPref;
        private Phone mSim1Phone;
        private Preference mSim1SmscPref;
        private PreferenceCategory mSim2Cate;
        private SubscriptionInfo mSim2Info;
        private MiuixTextPreference mSim2LocationPref;
        private MiuixTextPreference mSim2OperatorPref;
        private Phone mSim2Phone;
        private Preference mSim2SmscPref;
        private final int WAIT_TIMEOUT = 5000;
        private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.settings.SmscAddressPreferenceActivity.SmscAddressPreferenceFargment.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent != null && "android.intent.action.SIM_STATE_CHANGED".equals(intent.getAction())) {
                    SmscAddressPreferenceFargment.this.updateSimState();
                    SmscAddressPreferenceFargment smscAddressPreferenceFargment = SmscAddressPreferenceFargment.this;
                    if (smscAddressPreferenceFargment.isSimInserted(smscAddressPreferenceFargment.mSim1Phone)) {
                        return;
                    }
                    SmscAddressPreferenceFargment smscAddressPreferenceFargment2 = SmscAddressPreferenceFargment.this;
                    if (smscAddressPreferenceFargment2.isSimInserted(smscAddressPreferenceFargment2.mSim2Phone) || SmscAddressPreferenceFargment.this.getActivity() == null) {
                        return;
                    }
                    SmscAddressPreferenceFargment.this.getActivity().finish();
                }
            }
        };
        private Handler mSim1Handler = new Handler() { // from class: com.android.phone.settings.SmscAddressPreferenceActivity.SmscAddressPreferenceFargment.2
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i = message.what;
                if (i != 1001) {
                    if (i == 1003 && SmscAddressPreferenceFargment.this.getContext() != null) {
                        SmscAddressPreferenceFargment.this.mSim1SmscPref.setTitle(R.string.unknown);
                        return;
                    }
                    return;
                }
                removeMessages(1003);
                if (SmscAddressPreferenceFargment.this.getContext() == null) {
                    return;
                }
                AsyncResult asyncResult = (AsyncResult) message.obj;
                if (asyncResult.exception == null) {
                    String strSmscToString = SmscAddressPreferenceFargment.this.smscToString((String) asyncResult.result);
                    Preference preference = SmscAddressPreferenceFargment.this.mSim1SmscPref;
                    if (TextUtils.isEmpty(strSmscToString)) {
                        strSmscToString = SmscAddressPreferenceFargment.this.mContext.getString(R.string.set_smsc_address_title);
                    }
                    preference.setTitle(strSmscToString);
                }
            }
        };
        private Handler mSim2Handler = new Handler() { // from class: com.android.phone.settings.SmscAddressPreferenceActivity.SmscAddressPreferenceFargment.3
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i = message.what;
                if (i != 1001) {
                    if (i == 1003 && SmscAddressPreferenceFargment.this.getContext() != null) {
                        SmscAddressPreferenceFargment.this.mSim2SmscPref.setTitle(R.string.unknown);
                        return;
                    }
                    return;
                }
                removeMessages(1003);
                if (SmscAddressPreferenceFargment.this.getContext() == null) {
                    return;
                }
                AsyncResult asyncResult = (AsyncResult) message.obj;
                if (asyncResult.exception == null) {
                    String strSmscToString = SmscAddressPreferenceFargment.this.smscToString((String) asyncResult.result);
                    Preference preference = SmscAddressPreferenceFargment.this.mSim2SmscPref;
                    if (TextUtils.isEmpty(strSmscToString)) {
                        strSmscToString = SmscAddressPreferenceFargment.this.mContext.getString(R.string.set_smsc_address_title);
                    }
                    preference.setTitle(strSmscToString);
                }
            }
        };

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            addPreferencesFromResource(R.xml.smsc_address_preference);
            this.mContext = getContext();
            initPreference();
            getContext().registerReceiver(this.mReceiver, new IntentFilter("android.intent.action.SIM_STATE_CHANGED"), 2);
        }

        @Override // androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            updateSimState();
        }

        @Override // androidx.fragment.app.Fragment
        public void onDestroy() {
            this.mSim1Handler.removeCallbacksAndMessages(null);
            this.mSim2Handler.removeCallbacksAndMessages(null);
            getContext().unregisterReceiver(this.mReceiver);
            super.onDestroy();
        }

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, miuix.preference.PreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.preference.PreferenceManager.OnPreferenceTreeClickListener
        public boolean onPreferenceTreeClick(Preference preference) {
            if (preference == this.mSim1SmscPref) {
                Intent intent = new Intent(getContext(), (Class<?>) SmscAddressSettingActivity.class);
                intent.putExtra(SubscriptionManager.SLOT_KEY, 0);
                startActivity(intent);
            } else if (preference == this.mSim2SmscPref) {
                Intent intent2 = new Intent(getContext(), (Class<?>) SmscAddressSettingActivity.class);
                intent2.putExtra(SubscriptionManager.SLOT_KEY, 1);
                startActivity(intent2);
            }
            return super.onPreferenceTreeClick(preference);
        }

        private void initPreference() {
            this.mSim1Cate = (PreferenceCategory) getPreferenceScreen().findPreference("pref_key_sim1_category");
            this.mSim1SmscPref = getPreferenceScreen().findPreference("pref_key_sim1_smsc");
            this.mSim1OperatorPref = (MiuixTextPreference) getPreferenceScreen().findPreference("pref_key_sim1_operator");
            this.mSim1LocationPref = (MiuixTextPreference) getPreferenceScreen().findPreference("pref_key_sim1_location");
            this.mSim2Cate = (PreferenceCategory) getPreferenceScreen().findPreference("pref_key_sim2_category");
            this.mSim2SmscPref = getPreferenceScreen().findPreference("pref_key_sim2_smsc");
            this.mSim2OperatorPref = (MiuixTextPreference) getPreferenceScreen().findPreference("pref_key_sim2_operator");
            this.mSim2LocationPref = (MiuixTextPreference) getPreferenceScreen().findPreference("pref_key_sim2_location");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateSimState() {
            this.mSim1Phone = MiuiPhoneUtils.getPhone(0);
            this.mSim1Info = SubscriptionManager.getDefault().getSubscriptionInfoForSlot(0);
            this.mSim2Phone = MiuiPhoneUtils.getPhone(1);
            this.mSim2Info = SubscriptionManager.getDefault().getSubscriptionInfoForSlot(1);
            updateSimPrefs();
        }

        private void updateSimPrefs() {
            SubscriptionInfo subscriptionInfo;
            SubscriptionInfo subscriptionInfo2;
            String string = getString(R.string.unknown);
            getPreferenceScreen().removeAll();
            if (isSimInserted(this.mSim1Phone) && (subscriptionInfo2 = this.mSim1Info) != null && subscriptionInfo2.isActivated()) {
                getPreferenceScreen().addPreference(this.mSim1Cate);
                this.mSim1Cate.setEnabled(MiuiPhoneUtils.canSetSmscAddress(0));
                String simOperatorNameForSlot = TelephonyManager.getDefault().getSimOperatorNameForSlot(0);
                String string2 = this.mSim1Info.getDisplayNumber() == null ? "" : this.mSim1Info.getDisplayNumber().toString();
                String string3 = this.mSim1Info.getDisplayName() == null ? "" : this.mSim1Info.getDisplayName().toString();
                PreferenceCategory preferenceCategory = this.mSim1Cate;
                StringBuilder sb = new StringBuilder();
                sb.append(string3);
                sb.append(" ");
                sb.append(TextUtils.isEmpty(string2) ? string : string2);
                preferenceCategory.setTitle(sb.toString());
                String location = PhoneNumberUtils.PhoneNumber.parse(string2).getLocation(getContext());
                MiuixTextPreference miuixTextPreference = this.mSim1OperatorPref;
                if (TextUtils.isEmpty(simOperatorNameForSlot)) {
                    simOperatorNameForSlot = string;
                }
                miuixTextPreference.setValue(simOperatorNameForSlot);
                MiuixTextPreference miuixTextPreference2 = this.mSim1LocationPref;
                if (TextUtils.isEmpty(location)) {
                    location = string;
                }
                miuixTextPreference2.setValue(location);
                this.mSim1Handler.removeCallbacksAndMessages(null);
                this.mSim1Phone.getSmscAddress(this.mSim1Handler.obtainMessage(1001));
                this.mSim1Handler.sendEmptyMessageDelayed(1003, MirrorDesktopHelper.TIMEOUT_MILLIS);
            }
            if (isSimInserted(this.mSim2Phone) && (subscriptionInfo = this.mSim2Info) != null && subscriptionInfo.isActivated()) {
                getPreferenceScreen().addPreference(this.mSim2Cate);
                this.mSim2Cate.setEnabled(MiuiPhoneUtils.canSetSmscAddress(1));
                String simOperatorNameForSlot2 = TelephonyManager.getDefault().getSimOperatorNameForSlot(1);
                String string4 = this.mSim2Info.getDisplayNumber() == null ? "" : this.mSim2Info.getDisplayNumber().toString();
                String string5 = this.mSim2Info.getDisplayName() != null ? this.mSim2Info.getDisplayName().toString() : "";
                PreferenceCategory preferenceCategory2 = this.mSim2Cate;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(string5);
                sb2.append(" ");
                sb2.append(TextUtils.isEmpty(string4) ? string : string4);
                preferenceCategory2.setTitle(sb2.toString());
                String location2 = PhoneNumberUtils.PhoneNumber.parse(string4).getLocation(getContext());
                MiuixTextPreference miuixTextPreference3 = this.mSim2OperatorPref;
                if (TextUtils.isEmpty(simOperatorNameForSlot2)) {
                    simOperatorNameForSlot2 = string;
                }
                miuixTextPreference3.setValue(simOperatorNameForSlot2);
                MiuixTextPreference miuixTextPreference4 = this.mSim2LocationPref;
                if (!TextUtils.isEmpty(location2)) {
                    string = location2;
                }
                miuixTextPreference4.setValue(string);
                this.mSim2Handler.removeCallbacksAndMessages(null);
                this.mSim2Phone.getSmscAddress(this.mSim2Handler.obtainMessage(1001));
                this.mSim2Handler.sendEmptyMessageDelayed(1003, MirrorDesktopHelper.TIMEOUT_MILLIS);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isSimInserted(Phone phone) {
            return (phone == null || phone.getIccCard() == null || !phone.getIccCard().hasIccCard()) ? false : true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String smscToString(String str) {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            int iIndexOf = str.indexOf(44);
            if (iIndexOf > 0) {
                str = str.substring(0, iIndexOf).trim().replaceAll("\"", "");
            }
            if (TelephonyCapabilities.isSmscAddressEncoded(getContext())) {
                try {
                    StringBuilder sb = new StringBuilder();
                    int smscInt = getSmscInt(str, 0);
                    int i = 1;
                    if (smscInt >= 1 && getSmscInt(str, 1) == 91) {
                        sb.append('+');
                        while (i < smscInt) {
                            i++;
                            char[] smscChars = getSmscChars(str, i);
                            if (smscChars != null) {
                                for (char c : smscChars) {
                                    if (c != 'f' && c != 'F') {
                                        sb.append(c);
                                    }
                                }
                            }
                        }
                        return sb.toString();
                    }
                } catch (Exception unused) {
                }
            }
            return str;
        }

        private int getSmscInt(String str, int i) {
            int i2 = (i + 1) * 2;
            if (str.length() >= i2) {
                return Integer.parseInt(str.substring(i * 2, i2));
            }
            return -1;
        }

        private char[] getSmscChars(String str, int i) {
            if (str.length() < (i + 1) * 2) {
                return null;
            }
            int i2 = i * 2;
            return new char[]{str.charAt(i2 + 1), str.charAt(i2)};
        }
    }
}
