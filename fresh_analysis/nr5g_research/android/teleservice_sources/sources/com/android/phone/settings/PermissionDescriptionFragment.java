package com.android.phone.settings;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import com.android.phone.TelephonyCapabilities;
import miuix.preference.TextPreference;

/* JADX INFO: loaded from: classes.dex */
public class PermissionDescriptionFragment extends BaseActivity.BaseFragment implements Preference.OnPreferenceClickListener {
    private static String stateLocation;
    private static String stateReadContacts;
    private static String stateRecordAudio;
    private TextPreference mObtainLocationPreference;
    private PreferenceCategory mPrefCategory;
    private TextPreference mReadContact;
    private TextPreference mRecordCallPreference;
    private PreferenceScreen mScreenPreference;

    @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        setPreferencesFromResource(R.xml.permission_setting, str);
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        this.mScreenPreference = preferenceScreen;
        this.mPrefCategory = (PreferenceCategory) preferenceScreen.findPreference("pref_permission_description_category");
        TextPreference textPreference = (TextPreference) this.mScreenPreference.findPreference("pref_key_obtain_location");
        this.mObtainLocationPreference = textPreference;
        textPreference.setOnPreferenceClickListener(this);
        TextPreference textPreference2 = (TextPreference) this.mScreenPreference.findPreference("pref_key_read_call_log");
        this.mRecordCallPreference = textPreference2;
        textPreference2.setOnPreferenceClickListener(this);
        TextPreference textPreference3 = (TextPreference) this.mScreenPreference.findPreference("pref_key_read_contact");
        this.mReadContact = textPreference3;
        textPreference3.setOnPreferenceClickListener(this);
        if (!TelephonyCapabilities.supportsCallRecording(getContext())) {
            this.mPrefCategory.removePreference(this.mRecordCallPreference);
        }
        if (MiuiPhoneUtils.isGeoLocationSupported()) {
            return;
        }
        this.mPrefCategory.removePreference(this.mObtainLocationPreference);
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        if ("pref_key_obtain_location".equals(key)) {
            requestPermission("pref_key_obtain_location", stateLocation);
            return true;
        }
        if ("pref_key_read_call_log".equals(key)) {
            requestPermission("pref_key_read_call_log", stateRecordAudio);
            return true;
        }
        if (!"pref_key_read_contact".equals(key)) {
            return false;
        }
        requestPermission("pref_key_read_contact", stateRecordAudio);
        return true;
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        getPermissionText();
        updatePermissionText();
    }

    @Override // miuix.preference.PreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
    }

    public String getPermissionSatus(String str) {
        String str2;
        PackageManager packageManager = getContext().getPackageManager();
        str.hashCode();
        switch (str) {
            case "android.permission.ACCESS_FINE_LOCATION":
                str2 = "com.miui.geolocation";
                break;
            case "android.permission.READ_MEDIA_AUDIO":
            case "android.permission.RECORD_AUDIO":
            case "android.permission.READ_CONTACTS":
                str2 = "com.android.incallui";
                break;
            default:
                str2 = "";
                break;
        }
        int iCheckPermission = packageManager.checkPermission(str, str2);
        if (iCheckPermission == -1) {
            return getString(R.string.notallow);
        }
        if (iCheckPermission == 0) {
            return getString(R.string.allow);
        }
        if (iCheckPermission == 1) {
            return getString(R.string.ask);
        }
        if (iCheckPermission != 2) {
            return null;
        }
        return getString(R.string.allow);
    }

    private void getPermissionText() {
        stateLocation = getPermissionSatus("android.permission.ACCESS_FINE_LOCATION");
        stateRecordAudio = getPermissionSatus("android.permission.RECORD_AUDIO");
        stateReadContacts = getPermissionSatus("android.permission.READ_CONTACTS");
    }

    private void updatePermissionText() {
        this.mObtainLocationPreference.setText(helper(stateLocation));
        this.mRecordCallPreference.setText(helper(stateRecordAudio));
        this.mReadContact.setText(helper(stateReadContacts));
    }

    private String helper(String str) {
        if (str.equals(getString(R.string.allow))) {
            return getString(R.string.allow);
        }
        return getString(R.string.notallow);
    }

    private void requestPermission(String str, String str2) {
        String str3;
        str.hashCode();
        switch (str) {
            case "pref_key_obtain_location":
                str3 = "com.miui.geolocation";
                break;
            case "pref_key_read_call_log":
            case "pref_key_read_contact":
                str3 = "com.android.incallui";
                break;
            default:
                str3 = "";
                break;
        }
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.putExtra("extra_pkgname", str3);
        startActivity(intent);
    }
}
