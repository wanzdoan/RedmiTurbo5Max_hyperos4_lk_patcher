package com.android.phone.settings.fdn;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.mediatek.settings.CallSettingUtils;

/* JADX INFO: loaded from: classes.dex */
public class GetPin2Screen extends Activity implements TextView.OnEditorActionListener, PhoneGlobals.SubInfoUpdateListener {
    private final View.OnClickListener mClicked = new View.OnClickListener() { // from class: com.android.phone.settings.fdn.GetPin2Screen.1
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            GetPin2Screen getPin2Screen = GetPin2Screen.this;
            boolean zValidatePin = getPin2Screen.validatePin(getPin2Screen.mPin2Field.getText());
            GetPin2Screen getPin2Screen2 = GetPin2Screen.this;
            if (!zValidatePin) {
                getPin2Screen2.showStatus(getPin2Screen2.getString(R.string.invalidPin2));
            } else {
                if (TextUtils.isEmpty(getPin2Screen2.mPin2Field.getText())) {
                    return;
                }
                GetPin2Screen.this.returnResult();
            }
        }
    };
    private TextView mEnterPin2TitleTips;
    private Button mOkButton;
    private EditText mPin2Field;
    private int mSubId;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addSystemFlags(524288);
        setContentView(R.layout.get_pin2_screen);
        EditText editText = (EditText) findViewById(R.id.pin);
        this.mPin2Field = editText;
        editText.setKeyListener(DigitsKeyListener.getInstance());
        this.mPin2Field.setOnEditorActionListener(this);
        this.mPin2Field.setInputType(18);
        this.mPin2Field.requestFocus();
        Button button = (Button) findViewById(R.id.ok);
        this.mOkButton = button;
        button.setOnClickListener(this.mClicked);
        onCreateMtk();
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private String getPin2() {
        return this.mPin2Field.getText().toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void returnResult() {
        Bundle bundle = new Bundle();
        bundle.putString("pin2", getPin2());
        Uri data = getIntent().getData();
        Intent intent = new Intent();
        if (data != null) {
            intent.setAction(data.toString());
        }
        setResult(-1, intent.putExtras(bundle));
        finish();
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 6) {
            return false;
        }
        this.mOkButton.performClick();
        return true;
    }

    private void log(String str) {
        Log.d("PhoneGlobals", "[GetPin2] " + str);
    }

    private void onCreateMtk() {
        this.mSubId = getIntent().getIntExtra("com.android.phone.settings.SubscriptionInfoHelper.SubscriptionId", -1);
        log("onCreateMtk mSubId: " + this.mSubId);
        this.mEnterPin2TitleTips = (TextView) findViewById(R.id.get_pin2_title);
        int pin2RetryNumber = CallSettingUtils.getPin2RetryNumber(this.mSubId);
        TextView textView = this.mEnterPin2TitleTips;
        if (pin2RetryNumber == 0) {
            textView.setText(R.string.fdn_puk_need_tips);
        } else {
            textView.append("\n" + CallSettingUtils.getPinPuk2RetryLeftNumTips(this, this.mSubId, true));
        }
        disableActionBar();
        PhoneGlobals.getInstance().addSubInfoUpdateListener(this);
    }

    private void disableActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean validatePin(CharSequence charSequence) {
        return charSequence.length() >= 4 && charSequence.length() <= 8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showStatus(CharSequence charSequence) {
        if (charSequence != null) {
            Toast.makeText(this, charSequence, 0).show();
        }
    }

    @Override // android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        PhoneGlobals.getInstance().removeSubInfoUpdateListener(this);
    }

    @Override // com.android.phone.PhoneGlobals.SubInfoUpdateListener
    public void handleSubInfoUpdate() {
        finish();
    }
}
