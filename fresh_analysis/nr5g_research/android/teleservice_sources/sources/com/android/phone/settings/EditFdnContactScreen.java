package com.android.phone.settings;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DialerKeyListener;
import android.text.method.DigitsKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.R;
import com.xiaomi.mirror.synergy.CallMethod;
import miui.telephony.PhoneNumberUtils;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.app.ActionBar;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.AppCompatActivity;
import miuix.appcompat.app.LayoutUiModeHelper;
import miuix.appcompat.app.ProgressDialog;

/* JADX INFO: loaded from: classes.dex */
public class EditFdnContactScreen extends AppCompatActivity {
    private Button mCancelButton;
    private Button mDeleteButton;
    private String mName;
    private EditText mNameField;
    private String mNumber;
    private EditText mNumberField;
    private Button mOkButton;
    private EditText mPin2Field;
    private ProgressDialog mProgressDialog;
    private ViewGroup mScrollView;
    private int mType;
    private int mSlotId = 0;
    private boolean mIsFdnUpdate = false;
    private boolean mIsFdnInsert = false;
    private boolean mIsFdnDelete = false;
    private Handler mFDNHandler = new Handler() { // from class: com.android.phone.settings.EditFdnContactScreen.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 100) {
                Throwable th = ((AsyncResult) message.obj).exception;
                EditFdnContactScreen editFdnContactScreen = EditFdnContactScreen.this;
                if (th != null) {
                    editFdnContactScreen.showStatus(editFdnContactScreen.getResources().getText(R.string.pin2_invalid_or_name_is_too_long));
                    EditFdnContactScreen.this.resetFdnFlags();
                    return;
                }
                boolean z = editFdnContactScreen.mIsFdnDelete;
                EditFdnContactScreen editFdnContactScreen2 = EditFdnContactScreen.this;
                if (z) {
                    editFdnContactScreen2.performActions(Actions.Delete);
                } else {
                    boolean z2 = editFdnContactScreen2.mIsFdnInsert;
                    EditFdnContactScreen editFdnContactScreen3 = EditFdnContactScreen.this;
                    if (z2) {
                        editFdnContactScreen3.performActions(Actions.Insert);
                    } else if (editFdnContactScreen3.mIsFdnUpdate) {
                        EditFdnContactScreen.this.performActions(Actions.Update);
                    }
                }
                EditFdnContactScreen.this.resetFdnFlags();
            }
        }
    };
    private View.OnClickListener mClicked = new View.OnClickListener() { // from class: com.android.phone.settings.EditFdnContactScreen.2
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getId() == R.id.fdn_delete) {
                new AlertDialog.Builder(EditFdnContactScreen.this).setIconAttribute(android.R.attr.alertDialogIcon).setTitle(R.string.delete_fdn_contact).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.EditFdnContactScreen.2.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean z = ModemProvider.IS_SPRD;
                        AnonymousClass2 anonymousClass2 = AnonymousClass2.this;
                        if (z) {
                            EditFdnContactScreen.this.resetFdnFlags();
                            EditFdnContactScreen.this.mIsFdnDelete = true;
                            EditFdnContactScreen editFdnContactScreen = EditFdnContactScreen.this;
                            editFdnContactScreen.processPin2(editFdnContactScreen.getPin2FromTextField());
                            return;
                        }
                        EditFdnContactScreen.this.performActions(Actions.Delete);
                    }
                }).setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null).create().show();
                return;
            }
            if (view.getId() == R.id.fdn_ok) {
                boolean z = ModemProvider.IS_SPRD;
                EditFdnContactScreen editFdnContactScreen = EditFdnContactScreen.this;
                if (z) {
                    editFdnContactScreen.resetFdnFlags();
                    int i = EditFdnContactScreen.this.mType;
                    EditFdnContactScreen editFdnContactScreen2 = EditFdnContactScreen.this;
                    if (i == 1) {
                        editFdnContactScreen2.mIsFdnInsert = true;
                    } else {
                        editFdnContactScreen2.mIsFdnUpdate = true;
                    }
                    EditFdnContactScreen editFdnContactScreen3 = EditFdnContactScreen.this;
                    editFdnContactScreen3.processPin2(editFdnContactScreen3.getPin2FromTextField());
                    return;
                }
                editFdnContactScreen.performActions(editFdnContactScreen.mType == 1 ? Actions.Insert : Actions.Update);
                return;
            }
            if (view.getId() == R.id.fdn_cancel) {
                EditFdnContactScreen.this.finish();
            }
        }
    };
    private TextWatcher mTextWatcher = new TextWatcher() { // from class: com.android.phone.settings.EditFdnContactScreen.3
        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            boolean z = EditFdnContactScreen.this.mNameField.getText().length() != 0;
            if (z && EditFdnContactScreen.this.mNumberField.getText().length() == 0) {
                z = false;
            }
            boolean z2 = (z && EditFdnContactScreen.this.mPin2Field.getText().length() == 0) ? false : z;
            EditFdnContactScreen.this.mDeleteButton.setEnabled(z2);
            EditFdnContactScreen.this.mOkButton.setEnabled(z2);
        }
    };
    View.OnFocusChangeListener mOnFocusChangeHandler = new View.OnFocusChangeListener(this) { // from class: com.android.phone.settings.EditFdnContactScreen.4
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View view, boolean z) {
            if (z) {
                Selection.selectAll((Spannable) ((TextView) view).getText());
            }
        }
    };

    enum Actions {
        Insert,
        Update,
        Delete
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        getWindow().requestFeature(5);
        super.onCreate(bundle);
        LayoutUiModeHelper.autoSetLayoutUiMode(this);
        resolveIntent();
        getWindow().setSoftInputMode(32);
        setContentView(R.layout.miui_edit_fdn_contact_screen);
        setupView();
        ActionBar appCompatActionBar = getAppCompatActionBar();
        if (appCompatActionBar != null) {
            int i = this.mType;
            if (i == 1) {
                appCompatActionBar.setTitle(R.string.add_fdn_contact);
            } else if (i == 3) {
                appCompatActionBar.setTitle(R.string.edit_fdn_contact);
            } else if (i == 4) {
                appCompatActionBar.setTitle(R.string.delete_fdn_contact);
            }
        }
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.scrollView);
        this.mScrollView = viewGroup;
        viewGroup.setClipToPadding(false);
        registerCoordinateScrollView(this.mScrollView);
        requestDispatchContentInset();
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog != null && progressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
        }
        unregisterCoordinateScrollView(this.mScrollView);
        super.onDestroy();
    }

    private void resolveIntent() {
        Intent intent = getIntent();
        this.mSlotId = SubscriptionManager.getSlotIdExtra(intent, SubscriptionManager.getDefault().getDefaultSlotId());
        this.mName = intent.getStringExtra("name");
        this.mNumber = intent.getStringExtra("number");
        this.mType = intent.getIntExtra("type", 1);
    }

    private void setupView() {
        EditText editText = (EditText) findViewById(R.id.fdn_name);
        this.mNameField = editText;
        if (editText != null) {
            editText.addTextChangedListener(this.mTextWatcher);
        }
        EditText editText2 = (EditText) findViewById(R.id.fdn_number);
        this.mNumberField = editText2;
        if (editText2 != null) {
            editText2.setKeyListener(DialerKeyListener.getInstance());
            this.mNumberField.addTextChangedListener(this.mTextWatcher);
        }
        EditText editText3 = (EditText) findViewById(R.id.fdn_pin2);
        this.mPin2Field = editText3;
        editText3.setHint(getResources().getString(R.string.enter_pin2_text, 2));
        EditText editText4 = this.mPin2Field;
        if (editText4 != null) {
            editText4.setTransformationMethod(PasswordTransformationMethod.getInstance());
            this.mPin2Field.setKeyListener(DigitsKeyListener.getInstance());
            this.mPin2Field.addTextChangedListener(this.mTextWatcher);
        }
        Button button = (Button) findViewById(R.id.fdn_delete);
        this.mDeleteButton = button;
        if (button != null) {
            button.setOnClickListener(this.mClicked);
        }
        Button button2 = (Button) findViewById(R.id.fdn_ok);
        this.mOkButton = button2;
        if (button2 != null) {
            button2.setOnClickListener(this.mClicked);
        }
        Button button3 = (Button) findViewById(R.id.fdn_cancel);
        this.mCancelButton = button3;
        if (button3 != null) {
            button3.setOnClickListener(this.mClicked);
        }
        boolean zIsEmpty = TextUtils.isEmpty(this.mName);
        EditText editText5 = this.mNameField;
        if (!zIsEmpty) {
            editText5.setText(this.mName);
            this.mNameField.setSelection(this.mName.length());
        } else {
            editText5.setText("");
        }
        boolean zIsEmpty2 = TextUtils.isEmpty(this.mNumber);
        EditText editText6 = this.mNumberField;
        if (!zIsEmpty2) {
            editText6.setText(this.mNumber);
        } else {
            editText6.setText("");
        }
        if (this.mType == 4) {
            this.mPin2Field.requestFocus();
            this.mDeleteButton.setVisibility(0);
            this.mOkButton.setVisibility(8);
            this.mCancelButton.setVisibility(8);
            this.mNameField.setEnabled(false);
            this.mNumberField.setEnabled(false);
            return;
        }
        this.mNameField.requestFocus();
        this.mDeleteButton.setVisibility(8);
    }

    private String getNameFromTextField() {
        return this.mNameField.getText().toString();
    }

    private String getNumberFromTextField() {
        return PhoneNumberUtils.stripSeparators(this.mNumberField.getText().toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getPin2FromTextField() {
        return this.mPin2Field.getText().toString();
    }

    private Uri getContentURI() {
        return MiuiPhoneUtils.getFdnUri(this.mSlotId);
    }

    private boolean isValidNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!PhoneNumberUtils.isNonSeparator(str.charAt(i))) {
                return false;
            }
        }
        return str.length() <= 20;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void performActions(Actions actions) {
        if (!isValidNumber(getNumberFromTextField())) {
            handleErrorResult(true, false, 0);
            return;
        }
        if (!EditPinDialog.validatePin(getPin2FromTextField(), false)) {
            handleErrorResult(false, true, 0);
            return;
        }
        displayProgress(actions);
        Uri contentURI = getContentURI();
        QueryHandler queryHandler = new QueryHandler(getContentResolver());
        ContentValues contentValues = new ContentValues();
        int iOrdinal = actions.ordinal();
        if (iOrdinal == 0) {
            contentValues.put(CallMethod.ARG_SHARE_CALLBACK_TAG, getNameFromTextField());
            contentValues.put("number", getNumberFromTextField());
            contentValues.put("pin2", getPin2FromTextField());
            queryHandler.startInsert(0, null, contentURI, contentValues);
            return;
        }
        if (iOrdinal == 1) {
            contentValues.put(CallMethod.ARG_SHARE_CALLBACK_TAG, this.mName);
            contentValues.put("number", this.mNumber);
            contentValues.put("pin2", getPin2FromTextField());
            contentValues.put("newTag", getNameFromTextField());
            contentValues.put("newNumber", getNumberFromTextField());
            queryHandler.startUpdate(0, null, contentURI, contentValues, null, null);
            return;
        }
        if (iOrdinal != 2) {
            return;
        }
        queryHandler.startDelete(0, null, contentURI, "tag='" + this.mName + "' AND number='" + this.mNumber + "' AND pin2='" + getPin2FromTextField() + "'", null);
    }

    private void displayProgress(Actions actions) {
        if (this.mProgressDialog == null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            this.mProgressDialog = progressDialog;
            progressDialog.setCancelable(false);
            this.mProgressDialog.setIndeterminate(true);
            this.mProgressDialog.getWindow().setType(2008);
            this.mProgressDialog.getWindow().addFlags(2);
        }
        int iOrdinal = actions.ordinal();
        if (iOrdinal == 0) {
            this.mProgressDialog.setMessage(getText(R.string.adding_fdn_contact));
        } else if (iOrdinal == 1) {
            this.mProgressDialog.setMessage(getText(R.string.updating_fdn_contact));
        } else if (iOrdinal == 2) {
            this.mProgressDialog.setMessage(getText(R.string.deleting_fdn_contact));
        }
        this.mProgressDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissProgress() {
        ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog == null || !progressDialog.isShowing() || isFinishing()) {
            return;
        }
        this.mProgressDialog.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showStatus(CharSequence charSequence) {
        if (charSequence != null) {
            Toast.makeText(this, charSequence, 1).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleResult(Actions actions, boolean z, boolean z2) {
        if (z) {
            Intent intent = new Intent("action_fdn_change");
            intent.putExtra(SubscriptionManager.SLOT_KEY, this.mSlotId);
            sendBroadcast(intent);
            int iOrdinal = actions.ordinal();
            if (iOrdinal == 0) {
                showStatus(getText(R.string.fdn_contact_added));
            } else if (iOrdinal == 1) {
                showStatus(getText(R.string.fdn_contact_updated));
            } else if (iOrdinal == 2) {
                showStatus(getText(R.string.fdn_contact_deleted));
            }
            finish();
            return;
        }
        handleLastError();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetFdnFlags() {
        this.mIsFdnDelete = false;
        this.mIsFdnInsert = false;
        this.mIsFdnUpdate = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processPin2(String str) {
        Phone phone = MiuiPhoneUtils.getPhone(this.mSlotId);
        if (phone == null) {
            showStatus(getResources().getText(R.string.pin2_invalid_or_name_is_too_long));
            resetFdnFlags();
        } else {
            phone.getIccCard().supplyPin2(str, this.mFDNHandler.obtainMessage(100));
        }
    }

    class QueryHandler extends AsyncQueryHandler {
        public QueryHandler(ContentResolver contentResolver) {
            super(contentResolver);
        }

        @Override // android.content.AsyncQueryHandler
        protected void onQueryComplete(int i, Object obj, Cursor cursor) {
            int i2 = -1001;
            if (cursor != null) {
                try {
                    i2 = cursor.moveToFirst() ? cursor.getInt(0) : -1001;
                    cursor.close();
                } catch (Throwable th) {
                    cursor.close();
                    throw th;
                }
            }
            EditFdnContactScreen.this.handleErrorResult(false, false, i2);
        }

        @Override // android.content.AsyncQueryHandler
        protected void onInsertComplete(int i, Object obj, Uri uri) {
            EditFdnContactScreen.this.dismissProgress();
            EditFdnContactScreen.this.handleResult(Actions.Insert, uri != null, false);
        }

        @Override // android.content.AsyncQueryHandler
        protected void onUpdateComplete(int i, Object obj, int i2) {
            EditFdnContactScreen.this.dismissProgress();
            EditFdnContactScreen.this.handleResult(Actions.Update, i2 > 0, false);
        }

        @Override // android.content.AsyncQueryHandler
        protected void onDeleteComplete(int i, Object obj, int i2) {
            EditFdnContactScreen.this.dismissProgress();
            EditFdnContactScreen.this.handleResult(Actions.Delete, i2 > 0, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleErrorResult(boolean z, boolean z2, int i) {
        if (z) {
            showStatus(getResources().getString(R.string.fdn_invalid_number, 20));
            return;
        }
        if (z2 || i == -1007) {
            showStatus(getResources().getText(R.string.pin2_invalid));
        } else if (i == -1005) {
            showStatus(getResources().getText(R.string.fdn_full));
        } else {
            showStatus(getResources().getText(R.string.pin2_invalid_or_name_is_too_long));
        }
    }

    private void handleLastError() {
        new QueryHandler(getContentResolver()).startQuery(0, null, Uri.parse("content://icc/lasterror").buildUpon().appendQueryParameter(SubscriptionManager.SLOT_KEY, String.valueOf(this.mSlotId)).build(), null, null, null, null);
    }
}
