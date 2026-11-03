package com.android.phone.settings;

import android.os.Bundle;
import android.text.TextUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.android.phone.R;

/* JADX INFO: loaded from: classes.dex */
public class PrivacySettingActivity extends BaseActivity {
    /* JADX WARN: Code duplicated, block: B:14:0x0067 A[PHI: r6
  0x0067: PHI (r6v8 androidx.fragment.app.Fragment) = (r6v6 androidx.fragment.app.Fragment), (r6v12 androidx.fragment.app.Fragment) binds: [B:16:0x0073, B:12:0x005f] A[DONT_GENERATE, DONT_INLINE]] */
    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        Fragment fragmentFindFragmentByTag;
        String stringExtra = getIntent().getStringExtra(":android:show_fragment");
        String stringExtra2 = getIntent().getStringExtra(":android:show_fragment_title");
        if (TextUtils.isEmpty(stringExtra)) {
            stringExtra = PrivacySettingFragment.class.getName();
            getIntent().putExtra(":android:show_fragment", PrivacySettingFragment.class.getName());
        }
        if (!TextUtils.isEmpty(stringExtra2)) {
            setTitle(stringExtra2);
        } else {
            setTitle(getString(R.string.privacy_setting));
        }
        super.onCreate(bundle);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        boolean zEquals = TextUtils.equals(PermissionDescriptionFragment.class.getName(), stringExtra);
        boolean z = true;
        if (zEquals) {
            fragmentFindFragmentByTag = supportFragmentManager.findFragmentByTag(PermissionDescriptionFragment.class.getName());
            if (fragmentFindFragmentByTag == null) {
                fragmentFindFragmentByTag = new PermissionDescriptionFragment();
            } else {
                z = false;
            }
        } else {
            fragmentFindFragmentByTag = supportFragmentManager.findFragmentByTag(PrivacySettingFragment.class.getName());
            if (fragmentFindFragmentByTag == null) {
                fragmentFindFragmentByTag = new PrivacySettingFragment();
            } else {
                z = false;
            }
        }
        FragmentTransaction fragmentTransactionBeginTransaction = supportFragmentManager.beginTransaction();
        if (z) {
            fragmentTransactionBeginTransaction.add(android.R.id.content, fragmentFindFragmentByTag, fragmentFindFragmentByTag.getClass().getName());
        }
        fragmentTransactionBeginTransaction.commitAllowingStateLoss();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
    }
}
