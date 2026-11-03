package com.android.phone.beidou;

import android.os.Bundle;
import android.widget.TextView;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import miuix.appcompat.app.ActionBar;
import miuix.appcompat.app.AppCompatActivity;

/* JADX INFO: loaded from: classes.dex */
public class BeiDouIntroductionActivity extends AppCompatActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        int i;
        super.onCreate(bundle);
        setContentView(R.layout.introducing_beidou_satellite);
        ActionBar appCompatActionBar = getAppCompatActionBar();
        if (appCompatActionBar != null) {
            appCompatActionBar.setTitle(getString(R.string.beidou_satellite_introduction_title));
        }
        TextView textView = (TextView) findViewById(R.id.satellite_use_message);
        TextView textView2 = (TextView) findViewById(R.id.satellite_environment_requirement_message);
        TextView textView3 = (TextView) findViewById(R.id.satellite_enable_message);
        if (textView3 != null) {
            if (MiuiPhoneUtils.isOnlySupportCmccBeiDou()) {
                i = R.string.beidou_satellite_enable_message;
            } else {
                i = R.string.beidou_satellite_enable_message_all;
            }
            textView3.setText(i);
        }
        textView.setText(String.format(getResources().getString(R.string.beidou_satellite_use_message), 180));
        textView2.setText(String.format(getResources().getString(R.string.beidou_satellite_environment_requirement_message), 50, 25));
    }
}
