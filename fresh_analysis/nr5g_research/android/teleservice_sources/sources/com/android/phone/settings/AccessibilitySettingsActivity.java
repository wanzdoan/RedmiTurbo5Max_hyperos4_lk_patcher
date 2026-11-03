package com.android.phone.settings;

import android.app.ActionBar;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;
import com.android.phone.R;

/* JADX INFO: loaded from: classes.dex */
public class AccessibilitySettingsActivity extends PreferenceActivity {
    @Override // android.preference.PreferenceActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getPackageManager().hasSystemFeature("android.hardware.telephony")) {
            getWindow().addSystemFlags(524288);
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setTitle(R.string.accessibility_settings_activity_title);
            }
            SettingsConstants.setupEdgeToEdge(this);
            getFragmentManager().beginTransaction().replace(android.R.id.content, new AccessibilitySettingsFragment()).commit();
        }
    }

    @Override // android.preference.PreferenceActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
