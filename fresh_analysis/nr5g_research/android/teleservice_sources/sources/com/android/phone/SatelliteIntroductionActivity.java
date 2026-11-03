package com.android.phone;

import android.os.Bundle;
import android.widget.TextView;
import java.text.NumberFormat;
import miuix.appcompat.app.ActionBar;
import miuix.appcompat.app.AppCompatActivity;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteIntroductionActivity extends AppCompatActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.introducing_tiantong_satellite);
        ActionBar appCompatActionBar = getAppCompatActionBar();
        if (appCompatActionBar != null) {
            appCompatActionBar.setTitle(getString(R.string.satellite_introduction_title));
        }
        TextView textView = (TextView) findViewById(R.id.satellite_use_message);
        TextView textView2 = (TextView) findViewById(R.id.satellite_send_receive_sms_message);
        TextView textView3 = (TextView) findViewById(R.id.satellite_environment_requirement_message);
        TextView textView4 = (TextView) findViewById(R.id.satellite_handle_connection_failure_message);
        textView.setText(String.format(getResources().getString(R.string.satellite_use_message), 180));
        textView2.setText(String.format(getResources().getString(R.string.satellite_send_receive_sms_message), 70));
        textView3.setText(String.format(getResources().getString(R.string.satellite_environment_requirement_message), 50, 25, NumberFormat.getPercentInstance().format(0.019999999552965164d)));
        textView4.setText(String.format(getResources().getString(R.string.satellite_handle_connection_failure_message), 120, 120));
    }
}
