package com.android.phone.settings;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.WindowManager;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import com.android.phone.custompreferences.CustomEditTextPreference;
import com.android.phone.custompreferences.CustomListPreference;
import com.android.phone.utils.ScreenDisplayUtils;
import miuix.appcompat.app.ActionBar;
import miuix.appcompat.app.AppCompatActivity;
import miuix.appcompat.app.LayoutUiModeHelper;
import miuix.appcompat.app.strategy.CommonActionBarStrategy;
import miuix.preference.PreferenceFragment;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseActivity extends AppCompatActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        LayoutUiModeHelper.autoSetLayoutUiMode(this);
        if (MiuiPhoneUtils.isSupportSplit() && (getIntent().getMiuiFlags() & 4) != 0) {
            WindowManager.LayoutParams attributes = getWindow().getAttributes();
            attributes.layoutInDisplayCutoutMode = 1;
            getWindow().setAttributes(attributes);
        }
        setActionBar();
    }

    @Override // android.app.Activity, android.view.ContextThemeWrapper, android.content.ContextWrapper, android.content.Context
    public void setTheme(int i) {
        if ((getIntent().getMiuiFlags() & 4) != 0) {
            super.setTheme(R.style.Miui_Theme_Settings);
        } else {
            super.setTheme(i);
        }
    }

    protected void setActionBar() {
        ActionBar appCompatActionBar = getAppCompatActionBar();
        if (appCompatActionBar != null) {
            appCompatActionBar.setActionBarStrategy(new CommonActionBarStrategy());
            appCompatActionBar.setHomeButtonEnabled(true);
        }
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    protected void loadFragment(PreferenceFragment preferenceFragment) {
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, preferenceFragment, "miuix.preference.PreferenceFragment.MAIN_FRAGMENT_TAG").commit();
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        LayoutUiModeHelper.autoSetLayoutUiMode(this);
        setActionBar();
    }

    public class BaseFragment extends PreferenceFragment {
        private long mLastPreferenceClickTime = 0;

        @Override // androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
        }

        @Override // androidx.preference.PreferenceFragmentCompat
        public void setPreferenceScreen(PreferenceScreen preferenceScreen) {
            if (preferenceScreen != null && !preferenceScreen.isAttached()) {
                preferenceScreen.setShouldUseGeneratedIds(false);
            }
            super.setPreferenceScreen(preferenceScreen);
        }

        @Override // miuix.preference.PreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.preference.PreferenceManager.OnDisplayPreferenceDialogListener
        public void onDisplayPreferenceDialog(Preference preference) {
            DialogFragment dialogFragmentNewInstance;
            if (preference instanceof CustomEditTextPreference) {
                dialogFragmentNewInstance = CustomEditTextPreference.CustomPreferenceDialogFragment.newInstance(preference.getKey());
            } else {
                dialogFragmentNewInstance = preference instanceof CustomListPreference ? CustomListPreference.CustomListPreferenceDialogFragment.newInstance(preference.getKey()) : null;
            }
            if (dialogFragmentNewInstance != null) {
                dialogFragmentNewInstance.setTargetFragment(this, 0);
                dialogFragmentNewInstance.show(getFragmentManager(), "androidx.preference.PreferenceFragment.DIALOG");
            } else {
                super.onDisplayPreferenceDialog(preference);
            }
        }

        @Override // miuix.preference.PreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.preference.PreferenceManager.OnPreferenceTreeClickListener
        public boolean onPreferenceTreeClick(Preference preference) {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            if (jElapsedRealtime - this.mLastPreferenceClickTime < 500) {
                return true;
            }
            this.mLastPreferenceClickTime = jElapsedRealtime;
            FragmentActivity activity = getActivity();
            if (activity == null) {
                return false;
            }
            ScreenDisplayUtils.shouldForceSplit(preference.getIntent(), activity.getIntent().getMiuiFlags());
            return false;
        }
    }
}
