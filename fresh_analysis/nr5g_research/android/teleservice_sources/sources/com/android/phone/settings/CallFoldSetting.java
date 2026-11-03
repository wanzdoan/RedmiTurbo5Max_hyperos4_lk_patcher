package com.android.phone.settings;

import android.os.Bundle;
import androidx.preference.Preference;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;

/* JADX INFO: loaded from: classes.dex */
public class CallFoldSetting extends BaseActivity {
    private CallFoldSettingFragment callFoldSettingFragment;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        CallFoldSettingFragment callFoldSettingFragment = (CallFoldSettingFragment) getSupportFragmentManager().findFragmentByTag("miuix.preference.PreferenceFragment.MAIN_FRAGMENT_TAG");
        this.callFoldSettingFragment = callFoldSettingFragment;
        if (bundle == null && callFoldSettingFragment == null) {
            CallFoldSettingFragment callFoldSettingFragment2 = new CallFoldSettingFragment();
            this.callFoldSettingFragment = callFoldSettingFragment2;
            loadFragment(callFoldSettingFragment2);
        }
    }

    public class CallFoldSettingFragment extends BaseActivity.BaseFragment implements Preference.OnPreferenceClickListener {
        private CheckBoxTitleIconPreference mFoldAnswerCall;
        private CheckBoxTitleIconPreference mUnFoldCloseCamera;
        private CheckBoxTitleIconPreference mUnFoldShowInCallBubble;
        private CheckBoxTitleIconPreference mUnFoldTurnOnSpeaker;

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            initScreen();
        }

        private void initScreen() {
            addPreferencesFromResource(R.xml.call_fold_setting);
            this.mFoldAnswerCall = (CheckBoxTitleIconPreference) findPreference("pref_fold_answer_call");
            this.mUnFoldTurnOnSpeaker = (CheckBoxTitleIconPreference) findPreference("pref_unfold_turn_on_speaker");
            this.mUnFoldCloseCamera = (CheckBoxTitleIconPreference) findPreference("pref_unfold_close_camera");
            this.mUnFoldShowInCallBubble = (CheckBoxTitleIconPreference) findPreference("pref_unfold_show_incall_bubble");
            CheckBoxTitleIconPreference checkBoxTitleIconPreference = this.mFoldAnswerCall;
            if (checkBoxTitleIconPreference != null) {
                checkBoxTitleIconPreference.setOnPreferenceClickListener(this);
                this.mFoldAnswerCall.setChecked(TelephonySettings.isFoldAnswerEnabled(getContext().getContentResolver()));
            }
            CheckBoxTitleIconPreference checkBoxTitleIconPreference2 = this.mUnFoldTurnOnSpeaker;
            if (checkBoxTitleIconPreference2 != null) {
                checkBoxTitleIconPreference2.setOnPreferenceClickListener(this);
                this.mUnFoldTurnOnSpeaker.setChecked(TelephonySettings.isUnFoldTurnOnSpeakerEnabled(getContext().getContentResolver()));
            }
            if (MiuiPhoneUtils.isNotSupportUnfoldCloseCameraFeature()) {
                CallFeaturesSetting.removePreference(getPreferenceScreen(), "pref_unfold_close_camera");
            }
            CheckBoxTitleIconPreference checkBoxTitleIconPreference3 = this.mUnFoldCloseCamera;
            if (checkBoxTitleIconPreference3 != null) {
                checkBoxTitleIconPreference3.setOnPreferenceClickListener(this);
                this.mUnFoldCloseCamera.setChecked(TelephonySettings.isUnFoldCloseCameraEnabled(getContext().getContentResolver()));
            }
            CheckBoxTitleIconPreference checkBoxTitleIconPreference4 = this.mUnFoldShowInCallBubble;
            if (checkBoxTitleIconPreference4 != null) {
                checkBoxTitleIconPreference4.setOnPreferenceClickListener(this);
                this.mUnFoldShowInCallBubble.setChecked(TelephonySettings.iseUnFoldShowInCallBubble(getContext().getContentResolver()));
            }
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            if (preference == this.mFoldAnswerCall) {
                TelephonySettings.setFoldAnswerEnabled(getContext().getContentResolver(), this.mFoldAnswerCall.isChecked());
                return true;
            }
            if (preference == this.mUnFoldTurnOnSpeaker) {
                TelephonySettings.setUnFoldTurnOnSpeakerEnabled(getContext().getContentResolver(), this.mUnFoldTurnOnSpeaker.isChecked());
                return true;
            }
            if (preference == this.mUnFoldCloseCamera) {
                TelephonySettings.setUnFoldCloseCameraEnabled(getContext().getContentResolver(), this.mUnFoldCloseCamera.isChecked());
                return true;
            }
            if (preference != this.mUnFoldShowInCallBubble) {
                return true;
            }
            TelephonySettings.setUnFoldShowInCallBubbelEnabled(getContext().getContentResolver(), this.mUnFoldShowInCallBubble.isChecked());
            return true;
        }
    }
}
