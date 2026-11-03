package com.android.phone.settings;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.internal.telephony.CallForwardInfo;
import com.android.internal.telephony.CommandException;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import com.android.phone.settings.handler.FullForwardingHandler;
import gov.nist.javax.sip.address.GenericURI;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;
import miuix.appcompat.app.AlertDialog;
import miuix.preference.RadioButtonPreference;

/* JADX INFO: loaded from: classes.dex */
public class MiuiFullForwardingActivity extends TimeConsumingPreferenceActivity {
    private static final String LOG_TAG = MiuiFullForwardingActivity.class.getSimpleName();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.TimeConsumingPreferenceActivity, com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            loadFragment(new MiuiFullForwardingFragment());
        }
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, (Class<?>) MiuiFullForwardingActivity.class));
    }

    public class MiuiFullForwardingFragment extends TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment implements Preference.OnPreferenceClickListener, EditPhoneNumberPreference.OnDialogClosedListener, FullForwardingHandler.FullForwardingListener {
        private static final String[] NUM_PROJECTION = {"data1"};
        private String mCFNumberPrefixStr = "";
        private String mCurrentForwardNumber;
        private String mCurrentOption;
        private boolean mFirstResume;
        private FullForwardingHandler mHandler;
        private RadioButtonPreference mOptionCF;
        private RadioButtonPreference mOptionOFF;
        private PreferenceCategory mOptionScreen;
        private RadioButtonPreference mOptionVM;
        private PreferenceScreen mPreferenceScreen;
        private EditPhoneNumberPreference mTextCFNumber;

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment, com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            super.onCreatePreferences(bundle, str);
            addPreferencesFromResource(R.xml.miui_full_forwarding_options);
            this.mPreferenceScreen = getPreferenceScreen();
            PreferenceCategory preferenceCategory = (PreferenceCategory) findPreference("option_screen");
            this.mOptionScreen = preferenceCategory;
            this.mOptionVM = (RadioButtonPreference) preferenceCategory.findPreference("option_voicemail");
            this.mOptionCF = (RadioButtonPreference) this.mOptionScreen.findPreference("option_forwarding");
            this.mOptionOFF = (RadioButtonPreference) this.mOptionScreen.findPreference("option_off");
            this.mTextCFNumber = (EditPhoneNumberPreference) findPreference("text_forward_number");
            this.mOptionVM.setOnPreferenceClickListener(this);
            this.mOptionCF.setOnPreferenceClickListener(this);
            this.mOptionOFF.setOnPreferenceClickListener(this);
            this.mTextCFNumber.setDialogOnClosedListener(this);
            this.mTextCFNumber.setIntentFragment(this, 1);
            this.mCFNumberPrefixStr = getString(R.string.full_forwarding_number_prefix);
            this.mHandler = new FullForwardingHandler(this, MiuiPhoneUtils.getPhone(SubscriptionManager.getDefault().getDefaultSlotId()));
            this.mFirstResume = true;
        }

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment, androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            if (this.mFirstResume || TelephonyManager.isCustForJpKd()) {
                this.mHandler.startQuery();
                this.mFirstResume = false;
            }
        }

        private void checkOption(CallForwardInfo callForwardInfo) {
            String str;
            String strReplaceCountryCode = replaceCountryCode(callForwardInfo.number, "\\+81", "0");
            if (callForwardInfo.status == 0 || TextUtils.isEmpty(strReplaceCountryCode) || TextUtils.equals("09044441499", strReplaceCountryCode)) {
                str = "option_off";
                strReplaceCountryCode = "";
            } else if (TextUtils.equals("09044440000", strReplaceCountryCode)) {
                str = "option_voicemail";
            } else {
                str = "option_forwarding";
            }
            checkOptionByKey(str);
            updateTextCFNumber(str, strReplaceCountryCode);
        }

        private void checkOptionByKey(String str) {
            int preferenceCount = this.mOptionScreen.getPreferenceCount();
            for (int i = 0; i < preferenceCount; i++) {
                Preference preference = this.mOptionScreen.getPreference(i);
                if (preference != null && (preference instanceof RadioButtonPreference)) {
                    RadioButtonPreference radioButtonPreference = (RadioButtonPreference) preference;
                    if (str.equals(radioButtonPreference.getKey())) {
                        radioButtonPreference.setChecked(true);
                    } else {
                        radioButtonPreference.setChecked(false);
                    }
                }
            }
        }

        private void updateTextCFNumber(String str, String str2) {
            String str3;
            this.mCurrentOption = str;
            this.mCurrentForwardNumber = str2;
            if (TextUtils.isEmpty(str2)) {
                str3 = "";
            } else {
                str3 = this.mCFNumberPrefixStr + " " + str2;
            }
            this.mTextCFNumber.setTitle(str3);
            str.hashCode();
            switch (str) {
                case "option_voicemail":
                case "option_off":
                    this.mTextCFNumber.setEnabled(false);
                    this.mTextCFNumber.setVisible(false);
                    break;
                case "option_forwarding":
                    this.mTextCFNumber.setEnabled(true);
                    this.mTextCFNumber.setVisible(true);
                    break;
            }
        }

        @Override // com.android.phone.settings.handler.FullForwardingHandler.FullForwardingListener
        public void onCallForwardStart(boolean z) {
            PreferenceScreen preferenceScreen = this.mPreferenceScreen;
            if (preferenceScreen != null) {
                onStarted(preferenceScreen, z);
            }
        }

        @Override // com.android.phone.settings.handler.FullForwardingHandler.FullForwardingListener
        public void onCallForwardSuccess(boolean z, CallForwardInfo callForwardInfo) {
            PreferenceScreen preferenceScreen = this.mPreferenceScreen;
            if (preferenceScreen != null) {
                onFinished(preferenceScreen, z);
                checkOption(callForwardInfo);
            }
        }

        @Override // com.android.phone.settings.handler.FullForwardingHandler.FullForwardingListener
        public void onCallForwardFail(boolean z, CommandException commandException) {
            PreferenceScreen preferenceScreen = this.mPreferenceScreen;
            if (preferenceScreen != null) {
                onFinished(preferenceScreen, z);
                checkOptionByKey("option_off");
                updateTextCFNumber("option_off", "");
                PreferenceScreen preferenceScreen2 = this.mPreferenceScreen;
                if (commandException == null) {
                    onError(preferenceScreen2, 400);
                } else {
                    onException(preferenceScreen2, commandException);
                }
            }
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            if (this.mOptionVM == preference) {
                checkOptionByKey("option_voicemail");
                this.mHandler.enableCallForward("09044440000");
                return true;
            }
            if (this.mOptionCF == preference) {
                checkOptionByKey("option_forwarding");
                this.mTextCFNumber.setEnabled(true);
                this.mTextCFNumber.setVisible(true);
                this.mTextCFNumber.performClick();
                return true;
            }
            if (this.mOptionOFF != preference) {
                return false;
            }
            checkOptionByKey("option_off");
            this.mHandler.disableCallForward();
            return true;
        }

        @Override // com.android.phone.settings.EditPhoneNumberPreference.OnDialogClosedListener
        public void onDialogClosed(EditPhoneNumberPreference editPhoneNumberPreference, int i) {
            if (-1 == i) {
                String phoneNumber = editPhoneNumberPreference.getPhoneNumber();
                if (isValidNumber(phoneNumber)) {
                    this.mHandler.enableCallForward(phoneNumber);
                    return;
                }
                FragmentActivity activity = getActivity();
                if (activity == null) {
                    loge("activity == null");
                    return;
                }
                checkOptionByKey(this.mCurrentOption);
                updateTextCFNumber(this.mCurrentOption, this.mCurrentForwardNumber);
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(getString(R.string.full_forwarding_number_error_title));
                builder.setMessage(getString(R.string.full_forwarding_number_error_msg));
                builder.setPositiveButton(R.string.close_dialog, this.mDismiss);
                builder.setCancelable(false);
                builder.create().show();
            }
        }

        private boolean isValidNumber(String str) {
            return !TextUtils.isEmpty(str) && str.startsWith("0");
        }

        private String replaceCountryCode(String str, String str2, String str3) {
            return TextUtils.isEmpty(str) ? str : str.replaceFirst(str2, str3);
        }

        @Override // androidx.fragment.app.Fragment
        public void onActivityResult(int i, int i2, Intent intent) {
            String string;
            logd("onActivityResult, requestCode: " + i + ", resultCode: " + i2);
            if (getContext() == null || i2 != -1) {
                return;
            }
            Cursor cursorQuery = null;
            try {
                try {
                    try {
                        Uri data = intent.getData();
                        if (!data.getScheme().equals(GenericURI.TEL)) {
                            cursorQuery = getContext().getContentResolver().query(intent.getData(), NUM_PROJECTION, null, null, null);
                            if (cursorQuery != null && cursorQuery.moveToFirst()) {
                                string = cursorQuery.getString(0);
                            }
                            logd("onActivityResult: bad contact data, no results found.");
                            if (cursorQuery != null) {
                                return;
                            } else {
                                return;
                            }
                        }
                        string = data.getSchemeSpecificPart();
                        if (i == 1) {
                            this.mTextCFNumber.onPickActivityResult(string);
                        }
                    } catch (Exception e) {
                        loge("Exception: " + e);
                    }
                } catch (IllegalArgumentException e2) {
                    loge("IllegalArgumentException: " + e2);
                }
            } finally {
                if (0 != 0) {
                    cursorQuery.close();
                }
            }
        }

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment, androidx.fragment.app.Fragment
        public void onDestroy() {
            super.onDestroy();
            FullForwardingHandler fullForwardingHandler = this.mHandler;
            if (fullForwardingHandler != null) {
                fullForwardingHandler.destroy();
            }
        }

        private void logd(String str) {
            Log.d(MiuiFullForwardingActivity.LOG_TAG, str);
        }

        private void loge(String str) {
            Log.e(MiuiFullForwardingActivity.LOG_TAG, str);
        }
    }
}
