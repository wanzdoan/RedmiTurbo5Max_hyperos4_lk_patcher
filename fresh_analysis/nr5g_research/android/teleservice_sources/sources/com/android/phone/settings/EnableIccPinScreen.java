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
public class EnableIccPinScreen extends AppCompatActivity {
    private boolean mEnable;
    private Phone mPhone;
    private EditText mPinField;
    private LinearLayout mPinFieldContainer;
    private TextView mStatusField;
    private Handler mHandler = new Handler() { // from class: com.android.phone.settings.EnableIccPinScreen.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 100) {
                return;
            }
            EnableIccPinScreen.this.handleResult((AsyncResult) message.obj);
        }
    };
    private View.OnClickListener mClicked = new View.OnClickListener() { // from class: com.android.phone.settings.EnableIccPinScreen.3
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (TextUtils.isEmpty(EnableIccPinScreen.this.mPinField.getText())) {
                return;
            }
            EnableIccPinScreen enableIccPinScreen = EnableIccPinScreen.this;
            enableIccPinScreen.showStatus(enableIccPinScreen.getResources().getText(R.string.enable_in_progress));
            EnableIccPinScreen.this.enableIccPin();
        }
    };

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (SimPickerPreference.showSimPicker(this)) {
            return;
        }
        this.mPhone = MiuiPhoneUtils.getPhone(SubscriptionManager.getSlotIdExtra(getIntent(), SubscriptionManager.getDefault().getDefaultSlotId()));
        setContentView(R.layout.enable_sim_pin_screen);
        setupView();
        boolean iccLockEnabled = this.mPhone.getIccCard().getIccLockEnabled();
        this.mEnable = !iccLockEnabled;
        setTitle(getResources().getText(!iccLockEnabled ? R.string.enable_sim_pin : R.string.disable_sim_pin));
    }

    private void setupView() {
        EditText editText = (EditText) findViewById(R.id.pin);
        this.mPinField = editText;
        editText.setKeyListener(DigitsKeyListener.getInstance());
        this.mPinField.setMovementMethod(null);
        this.mPinField.setOnClickListener(this.mClicked);
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

    private String getPin() {
        return this.mPinField.getText().toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enableIccPin() {
        this.mPhone.getIccCard().setIccLockEnabled(this.mEnable, getPin(), Message.obtain(this.mHandler, 100));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleResult(AsyncResult asyncResult) {
        Throwable th = asyncResult.exception;
        if (th == null) {
            showStatus(getResources().getText(this.mEnable ? R.string.enable_pin_ok : R.string.disable_pin_ok));
        } else if (th instanceof CommandException) {
            showStatus(getResources().getText(R.string.pin_failed));
        }
        this.mHandler.postDelayed(new Runnable() { // from class: com.android.phone.settings.EnableIccPinScreen.2
            @Override // java.lang.Runnable
            public void run() {
                EnableIccPinScreen.this.finish();
            }
        }, 3000L);
    }
}
