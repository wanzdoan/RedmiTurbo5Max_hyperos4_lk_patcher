package com.android.phone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Resources;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.UserManager;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.Phone;

/* JADX INFO: loaded from: classes.dex */
public class ChangeIccPinScreen extends Activity {
    private TextView mBadPinError;
    private Button mButton;
    private boolean mChangePin2;
    private boolean mDisallowedConfig;
    private LinearLayout mIccPUKPanel;
    private TextView mMismatchError;
    private EditText mNewPin1;
    private EditText mNewPin2;
    private EditText mOldPin;
    private AlertDialog mPUKAlert;
    private EditText mPUKCode;
    private Button mPUKSubmit;
    private Phone mPhone;
    private ScrollView mScrollView;
    private EntryState mState;
    private UserManager mUserManager;
    private Handler mHandler = new Handler() { // from class: com.android.phone.ChangeIccPinScreen.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 100) {
                return;
            }
            ChangeIccPinScreen.this.handleResult((AsyncResult) message.obj);
        }
    };
    private View.OnClickListener mClicked = new View.OnClickListener() { // from class: com.android.phone.ChangeIccPinScreen.2
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            String string;
            EditText editText = ChangeIccPinScreen.this.mOldPin;
            ChangeIccPinScreen changeIccPinScreen = ChangeIccPinScreen.this;
            if (view == editText) {
                changeIccPinScreen.mNewPin1.requestFocus();
                return;
            }
            EditText editText2 = changeIccPinScreen.mNewPin1;
            ChangeIccPinScreen changeIccPinScreen2 = ChangeIccPinScreen.this;
            if (view == editText2) {
                changeIccPinScreen2.mNewPin2.requestFocus();
                return;
            }
            EditText editText3 = changeIccPinScreen2.mNewPin2;
            ChangeIccPinScreen changeIccPinScreen3 = ChangeIccPinScreen.this;
            if (view == editText3) {
                changeIccPinScreen3.mButton.requestFocus();
                return;
            }
            Button button = changeIccPinScreen3.mButton;
            ChangeIccPinScreen changeIccPinScreen4 = ChangeIccPinScreen.this;
            if (view == button) {
                IccCard iccCard = changeIccPinScreen4.mPhone.getIccCard();
                if (iccCard != null) {
                    String string2 = ChangeIccPinScreen.this.mOldPin.getText().toString();
                    String string3 = ChangeIccPinScreen.this.mNewPin1.getText().toString();
                    int iValidateNewPin = ChangeIccPinScreen.this.validateNewPin(string3, ChangeIccPinScreen.this.mNewPin2.getText().toString());
                    if (iValidateNewPin == 1 || iValidateNewPin == 2) {
                        ChangeIccPinScreen.this.mNewPin1.getText().clear();
                        ChangeIccPinScreen.this.mNewPin2.getText().clear();
                        ChangeIccPinScreen.this.mMismatchError.setVisibility(0);
                        Resources resources = ChangeIccPinScreen.this.getResources();
                        if (iValidateNewPin == 1) {
                            string = resources.getString(R.string.mismatchPin);
                        } else {
                            string = resources.getString(R.string.invalidPin);
                        }
                        ChangeIccPinScreen.this.mMismatchError.setText(string);
                        return;
                    }
                    Message messageObtain = Message.obtain(ChangeIccPinScreen.this.mHandler, 100);
                    ChangeIccPinScreen.this.reset();
                    if (ChangeIccPinScreen.this.mChangePin2) {
                        iccCard.changeIccFdnPassword(string2, string3, messageObtain);
                        return;
                    } else {
                        iccCard.changeIccLockPassword(string2, string3, messageObtain);
                        return;
                    }
                }
                return;
            }
            EditText editText4 = changeIccPinScreen4.mPUKCode;
            ChangeIccPinScreen changeIccPinScreen5 = ChangeIccPinScreen.this;
            if (view == editText4) {
                changeIccPinScreen5.mPUKSubmit.requestFocus();
            } else if (view == changeIccPinScreen5.mPUKSubmit) {
                ChangeIccPinScreen.this.mPhone.getIccCard().supplyPuk2(ChangeIccPinScreen.this.mPUKCode.getText().toString(), ChangeIccPinScreen.this.mNewPin1.getText().toString(), Message.obtain(ChangeIccPinScreen.this.mHandler, 100));
            }
        }
    };

    enum EntryState {
        ES_PIN,
        ES_PUK
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addSystemFlags(524288);
        UserManager userManager = (UserManager) getSystemService(UserManager.class);
        this.mUserManager = userManager;
        if (userManager.hasUserRestriction("no_config_mobile_networks")) {
            this.mDisallowedConfig = true;
        }
        this.mPhone = PhoneGlobals.getPhone();
        resolveIntent();
        setContentView(R.layout.change_sim_pin_screen);
        setupView();
        this.mState = EntryState.ES_PIN;
    }

    private void setupView() {
        this.mOldPin = (EditText) findViewById(R.id.old_pin);
        this.mNewPin1 = (EditText) findViewById(R.id.new_pin1);
        this.mNewPin2 = (EditText) findViewById(R.id.new_pin2);
        this.mBadPinError = (TextView) findViewById(R.id.bad_pin);
        this.mMismatchError = (TextView) findViewById(R.id.mismatch);
        this.mButton = (Button) findViewById(R.id.button);
        this.mScrollView = (ScrollView) findViewById(R.id.scroll);
        this.mPUKCode = (EditText) findViewById(R.id.puk_code);
        this.mPUKSubmit = (Button) findViewById(R.id.puk_submit);
        this.mIccPUKPanel = (LinearLayout) findViewById(R.id.puk_panel);
        setTitle(getResources().getText(this.mChangePin2 ? R.string.change_pin2 : R.string.change_pin));
        boolean z = this.mDisallowedConfig;
        EditText editText = this.mOldPin;
        if (z) {
            editText.setEnabled(false);
            this.mOldPin.setAlpha(0.5f);
            this.mNewPin1.setEnabled(false);
            this.mNewPin1.setAlpha(0.5f);
            this.mNewPin2.setEnabled(false);
            this.mNewPin2.setAlpha(0.5f);
            this.mButton.setEnabled(false);
            this.mButton.setAlpha(0.5f);
            this.mPUKCode.setEnabled(false);
            this.mPUKCode.setAlpha(0.5f);
            this.mPUKSubmit.setEnabled(false);
            this.mPUKSubmit.setAlpha(0.5f);
            return;
        }
        editText.setKeyListener(DigitsKeyListener.getInstance());
        this.mOldPin.setMovementMethod(null);
        this.mOldPin.setOnClickListener(this.mClicked);
        this.mNewPin1.setKeyListener(DigitsKeyListener.getInstance());
        this.mNewPin1.setMovementMethod(null);
        this.mNewPin1.setOnClickListener(this.mClicked);
        this.mNewPin2.setKeyListener(DigitsKeyListener.getInstance());
        this.mNewPin2.setMovementMethod(null);
        this.mNewPin2.setOnClickListener(this.mClicked);
        this.mButton.setOnClickListener(this.mClicked);
        this.mPUKCode.setKeyListener(DigitsKeyListener.getInstance());
        this.mPUKCode.setMovementMethod(null);
        this.mPUKCode.setOnClickListener(this.mClicked);
        this.mPUKSubmit.setOnClickListener(this.mClicked);
    }

    private void resolveIntent() {
        this.mChangePin2 = getIntent().getBooleanExtra("pin2", this.mChangePin2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reset() {
        this.mScrollView.scrollTo(0, 0);
        this.mBadPinError.setVisibility(8);
        this.mMismatchError.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int validateNewPin(String str, String str2) {
        if (str == null) {
            return 2;
        }
        if (!str.equals(str2)) {
            return 1;
        }
        int length = str.length();
        return (length < 4 || length > 8) ? 2 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleResult(AsyncResult asyncResult) {
        Throwable th = asyncResult.exception;
        if (th == null) {
            if (this.mState == EntryState.ES_PUK) {
                this.mScrollView.setVisibility(0);
                this.mIccPUKPanel.setVisibility(8);
            }
            showConfirmation();
            this.mHandler.postDelayed(new Runnable() { // from class: com.android.phone.ChangeIccPinScreen.3
                @Override // java.lang.Runnable
                public void run() {
                    ChangeIccPinScreen.this.finish();
                }
            }, 3000L);
            return;
        }
        if (th instanceof CommandException) {
            EntryState entryState = this.mState;
            if (entryState == EntryState.ES_PIN) {
                this.mOldPin.getText().clear();
                this.mBadPinError.setVisibility(0);
                if (asyncResult.exception.getCommandError() == CommandException.Error.SIM_PUK2) {
                    this.mState = EntryState.ES_PUK;
                    displayPUKAlert();
                    this.mScrollView.setVisibility(8);
                    this.mIccPUKPanel.setVisibility(0);
                    this.mPUKCode.requestFocus();
                    return;
                }
                return;
            }
            if (entryState == EntryState.ES_PUK) {
                displayPUKAlert();
                this.mPUKCode.getText().clear();
                this.mPUKCode.requestFocus();
            }
        }
    }

    private void displayPUKAlert() {
        AlertDialog alertDialog = this.mPUKAlert;
        if (alertDialog == null) {
            this.mPUKAlert = FrameworksUtils.makeAlertDialogBuilder(this).setMessage(R.string.puk_requested).setCancelable(false).show();
        } else {
            alertDialog.show();
        }
        this.mHandler.postDelayed(new Runnable() { // from class: com.android.phone.ChangeIccPinScreen.4
            @Override // java.lang.Runnable
            public void run() {
                ChangeIccPinScreen.this.mPUKAlert.dismiss();
            }
        }, 3000L);
    }

    private void showConfirmation() {
        Toast.makeText(this, this.mChangePin2 ? R.string.pin2_changed : R.string.pin_changed, 0).show();
    }
}
