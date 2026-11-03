package com.android.phone.settings;

import android.os.Bundle;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;

/* JADX INFO: loaded from: classes.dex */
public class MiuiSatelliteSettings extends BaseActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getIntent() != null && getIntent().hasExtra("nomlMessage") && "nomlNotification".equals(getIntent().getStringExtra("nomlMessage"))) {
            MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "signalmap_noml_to_satellite_notification");
        }
        if (bundle == null) {
            loadFragment(new MiuiSatelliteSettingsFragment());
        }
    }
}
