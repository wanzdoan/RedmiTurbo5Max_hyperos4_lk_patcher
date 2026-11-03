package com.android.phone.settings;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import com.android.phone.TelephonyCapabilities;
import com.android.phone.utils.ScreenDisplayUtils;
import java.util.Locale;
import miui.os.Build;
import miuix.appcompat.app.AppCompatActivity;
import miuix.appcompat.app.floatingactivity.multiapp.MultiAppFloatingActivitySwitcher;
import miuix.preference.PreferenceFragment;

/* JADX INFO: loaded from: classes.dex */
public class PrivacySettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    @Override // androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        setPreferencesFromResource(R.xml.privacy_setting, str);
        findPreference("pref_key_preference_check_privacy").setOnPreferenceClickListener(this);
        findPreference("pref_key_permission_description").setOnPreferenceClickListener(this);
        if (Build.IS_INTERNATIONAL_BUILD || !(TelephonyCapabilities.supportsCallRecording(getContext()) || MiuiPhoneUtils.isGeoLocationSupported())) {
            PreferenceCategory preferenceCategory = (PreferenceCategory) findPreference("pref_key_phone_category");
            Preference preferenceFindPreference = findPreference("pref_key_permission_description");
            if (preferenceCategory == null || preferenceFindPreference == null) {
                return;
            }
            preferenceCategory.removePreference(preferenceFindPreference);
        }
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        if ("pref_key_preference_check_privacy".equals(key)) {
            openUrl(getPrivacyPolicyUrl());
            return false;
        }
        if ("pref_key_permission_description".equals(key)) {
            Intent intent = new Intent(getActivity(), (Class<?>) PrivacySettingActivity.class);
            intent.putExtra(":android:show_fragment", PermissionDescriptionFragment.class.getName());
            intent.putExtra(":android:show_fragment_title", getString(R.string.permission_description));
            ScreenDisplayUtils.shouldForceSplit(intent, getActivity().getIntent().getMiuiFlags());
            if (getActivity() instanceof AppCompatActivity) {
                MultiAppFloatingActivitySwitcher.configureFloatingService(intent, (AppCompatActivity) getActivity());
            }
            startActivity(intent);
        }
        return false;
    }

    private void openUrl(String str) {
        if (str == null) {
            Log.w("PrivacySettingFragment", "Cannot open null URL");
            return;
        }
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
        } catch (ActivityNotFoundException e) {
            Log.e("PrivacySettingFragment", "No activity found to handle URL: ".concat(str), e);
        }
    }

    private String getPrivacyPolicyUrl() {
        return String.format("https://privacy.mi.com/all/%1$s_%2$s", Locale.getDefault().getLanguage(), Locale.getDefault().getCountry());
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }
}
