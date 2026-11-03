package com.android.phone;

import android.content.res.Resources;
import android.os.Bundle;
import android.telephony.SubscriptionManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import miui.os.HyperOSCustFeatureResolve;
import miuix.appcompat.app.AppCompatActivity;

/* JADX INFO: loaded from: classes.dex */
public class WifiCallHelpActivity extends AppCompatActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.wifi_call_help_layout);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        ImageView imageView2 = (ImageView) findViewById(R.id.imageView_for_france);
        int intExtra = getIntent().getIntExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, -1);
        Resources resourcesForSubId = SubscriptionManager.getResourcesForSubId(this, MiuiPhoneUtils.getPhone(intExtra).getSubId());
        ((TextView) findViewById(R.id.help_text1)).setText(resourcesForSubId.getString(R.string.wifi_call_help_message_1));
        ((TextView) findViewById(R.id.help_text2)).setText(resourcesForSubId.getString(R.string.wifi_call_help_message_2));
        setTitle(resourcesForSubId.getString(R.string.wifi_call_help_title));
        if (HyperOSCustFeatureResolve.getBooleanForPhoneId(intExtra, "wifi_call_help_view_france", false)) {
            imageView.setVisibility(8);
            imageView2.setVisibility(0);
        } else {
            imageView.setVisibility(0);
            imageView2.setVisibility(8);
        }
    }
}
