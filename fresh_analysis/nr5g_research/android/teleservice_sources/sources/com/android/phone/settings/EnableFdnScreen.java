package com.android.phone.settings;

import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.app.AppCompatActivity;

/* JADX INFO: loaded from: classes.dex */
public class EnableFdnScreen extends AppCompatActivity {
    private boolean mEnable;
    private Phone mPhone;
    private EditText mPin2Field;
    private LinearLayout mPinFieldContainer;
    private TextView mStatusField;
    private Handler mHandler = new Handler() { // from class: com.android.phone.settings.EnableFdnScreen.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 100) {
                return;
            }
            EnableFdnScreen.this.handleResult((AsyncResult) message.obj);
        }
    };
    private View.OnClickListener mClicked = new View.OnClickListener() { // from class: com.android.phone.settings.EnableFdnScreen.3
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (TextUtils.isEmpty(EnableFdnScreen.this.mPin2Field.getText())) {
                return;
            }
            EnableFdnScreen enableFdnScreen = EnableFdnScreen.this;
            enableFdnScreen.showStatus(enableFdnScreen.getResources().getText(R.string.enable_in_progress));
            EnableFdnScreen.this.enableFdn();
        }
    };

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.enable_fdn_screen);
        setupView();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        if (SimPickerPreference.showSimPicker(this)) {
            return;
        }
        Phone phone = MiuiPhoneUtils.getPhone(SubscriptionManager.getSlotIdExtra(getIntent(), SubscriptionManager.getDefault().getDefaultSlotId()));
        this.mPhone = phone;
        boolean iccFdnEnabled = phone.getIccCard().getIccFdnEnabled();
        this.mEnable = !iccFdnEnabled;
        setTitle(getResources().getText(!iccFdnEnabled ? R.string.enable_fdn : R.string.disable_fdn));
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        this.mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private void setupView() {
        EditText editText = (EditText) findViewById(R.id.pin);
        this.mPin2Field = editText;
        editText.setKeyListener(DigitsKeyListener.getInstance());
        this.mPin2Field.setMovementMethod(null);
        this.mPin2Field.setOnClickListener(this.mClicked);
        this.mPinFieldContainer = (LinearLayout) findViewById(R.id.pinc);
        this.mStatusField = (TextView) findViewById(R.id.status);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showStatus(CharSequence charSequence) {
        if (charSequence != null) {
            this.mStatusField.setText(charSequence);
            this.mStatusField.setVisibility(0);
            this.mPinFieldContainer.setVisibility(8);
        } else {
            this.mPinFieldContainer.setVisibility(0);
            this.mStatusField.setVisibility(8);
        }
    }

    private String getPin2() {
        return this.mPin2Field.getText().toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enableFdn() {
        this.mPhone.getIccCard().setIccFdnEnabled(this.mEnable, getPin2(), Message.obtain(this.mHandler, 100));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleResult(AsyncResult asyncResult) {
        Throwable th = asyncResult.exception;
        if (th == null) {
            showStatus(getResources().getText(this.mEnable ? R.string.enable_fdn_ok : R.string.disable_fdn_ok));
        } else if (th instanceof CommandException) {
            showStatus(getResources().getText(R.string.pin_failed));
        }
        this.mHandler.postDelayed(new Runnable() { // from class: com.android.phone.settings.EnableFdnScreen.2
            @Override // java.lang.Runnable
            public void run() {
                EnableFdnScreen.this.finish();
            }
        }, 3000L);
    }
}
