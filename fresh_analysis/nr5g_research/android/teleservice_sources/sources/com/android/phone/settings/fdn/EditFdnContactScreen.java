package com.android.phone.settings.fdn;

import android.app.ComponentCaller;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.DialerKeyListener;
import android.util.EventLog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.mediatek.settings.CallSettingUtils;
import com.xiaomi.mirror.synergy.CallMethod;

/* JADX INFO: loaded from: classes.dex */
public class EditFdnContactScreen extends BaseFdnContactScreen implements PhoneGlobals.SubInfoUpdateListener {
    private static final Intent CONTACT_IMPORT_INTENT;
    private static final String[] NUM_PROJECTION = {"display_name", "data1"};
    private boolean mAddContact;
    private Button mButton;
    private boolean mDataBusy;
    private EditText mNameField;
    private EditText mNumberField;
    private LinearLayout mPinFieldContainer;
    private int mFdnNumberLimitLength = 20;
    private final View.OnClickListener mClicked = new View.OnClickListener() { // from class: com.android.phone.settings.fdn.EditFdnContactScreen.1
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (EditFdnContactScreen.this.mPinFieldContainer.getVisibility() != 0) {
                return;
            }
            EditText editText = EditFdnContactScreen.this.mNameField;
            EditFdnContactScreen editFdnContactScreen = EditFdnContactScreen.this;
            if (view == editText) {
                editFdnContactScreen.mButton.requestFocus();
                return;
            }
            EditText editText2 = editFdnContactScreen.mNumberField;
            EditFdnContactScreen editFdnContactScreen2 = EditFdnContactScreen.this;
            if (view == editText2) {
                editFdnContactScreen2.mButton.requestFocus();
                return;
            }
            if (view == editFdnContactScreen2.mButton) {
                boolean zIsValidNumber = EditFdnContactScreen.this.isValidNumber(PhoneNumberUtils.stripSeparators(EditFdnContactScreen.this.getNumberFromTextField()));
                EditFdnContactScreen editFdnContactScreen3 = EditFdnContactScreen.this;
                if (!zIsValidNumber) {
                    editFdnContactScreen3.handleResult(false, true);
                    return;
                }
                if (editFdnContactScreen3.mDataBusy) {
                    return;
                }
                boolean zNeedTipsPIN2Blocked = EditFdnContactScreen.this.needTipsPIN2Blocked();
                EditFdnContactScreen editFdnContactScreen4 = EditFdnContactScreen.this;
                if (!zNeedTipsPIN2Blocked) {
                    editFdnContactScreen4.authenticatePin2();
                } else {
                    editFdnContactScreen4.log("[onClick] PIN2 Locked!!");
                }
            }
        }
    };
    private final View.OnFocusChangeListener mOnFocusChangeHandler = new View.OnFocusChangeListener(this) { // from class: com.android.phone.settings.fdn.EditFdnContactScreen.2
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View view, boolean z) {
            if (z) {
                Selection.selectAll((Spannable) ((TextView) view).getText());
            }
        }
    };
    private final TextWatcher mTextWatcher = new TextWatcher() { // from class: com.android.phone.settings.fdn.EditFdnContactScreen.3
        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            EditFdnContactScreen.this.setButtonEnabled();
        }
    };

    static {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        CONTACT_IMPORT_INTENT = intent;
        intent.setType("vnd.android.cursor.item/phone_v2");
    }

    @Override // com.android.phone.settings.fdn.BaseFdnContactScreen, android.app.Activity
    protected void onCreate(Bundle bundle) {
        PersistableBundle carrierConfig;
        super.onCreate(bundle);
        getWindow().addSystemFlags(524288);
        setContentView(R.layout.edit_fdn_contact_screen);
        setupView();
        setTitle(this.mAddContact ? R.string.add_fdn_contact : R.string.edit_fdn_contact);
        if (this.mSubscriptionInfoHelper.hasSubId()) {
            carrierConfig = PhoneGlobals.getInstance().getCarrierConfigForSubId(this.mSubscriptionInfoHelper.getSubId());
        } else {
            carrierConfig = PhoneGlobals.getInstance().getCarrierConfig();
        }
        if (carrierConfig != null) {
            this.mFdnNumberLimitLength = carrierConfig.getInt("fdn_number_length_limit_int");
        }
        displayProgress(false);
        PhoneGlobals.getInstance().addSubInfoUpdateListener(this);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        log("onActivityResult request:" + i + " result:" + i2);
        Object[] objArr = 0;
        if (i == 100) {
            Bundle extras = intent != null ? intent.getExtras() : null;
            if (extras != null) {
                String string = extras.getString("pin2");
                this.mPin2 = string;
                processPin2(string);
                return;
            } else {
                if (i2 != -1) {
                    log("onActivityResult: cancelled.");
                    finish();
                    return;
                }
                return;
            }
        }
        if (i != 200) {
            return;
        }
        if (i2 != -1) {
            log("onActivityResult: cancelled.");
            return;
        }
        ComponentCaller currentCaller = getCurrentCaller();
        Uri data = intent != null ? intent.getData() : null;
        if (data == null) {
            Log.w("PhoneGlobals", "onActivityResult: Intent data or contact URI is null.");
            return;
        }
        if (currentCaller.checkContentUriPermission(data, 1) == -1) {
            EventLog.writeEvent(1397638484, "337784859", Integer.valueOf(currentCaller.getUid()), "Permission denied, cannot access contact");
            throw new SecurityException(String.format("Permission denial: Caller (uid=%d, pkg=%s) lacks specific permission grant %s to access contact URI %s.", Integer.valueOf(currentCaller.getUid()), currentCaller.getPackage(), "FLAG_GRANT_READ_URI_PERMISSION", data));
        }
        try {
            Cursor cursorQuery = getContentResolver().query(data, NUM_PROJECTION, null, null, null);
            if (cursorQuery != null && cursorQuery.moveToFirst()) {
                this.mNameField.setText(cursorQuery.getString(0));
                this.mNumberField.setText(cursorQuery.getString(1));
                cursorQuery.close();
                return;
            }
            Log.w("PhoneGlobals", "onActivityResult: bad contact data, no results found.");
            if (cursorQuery != null) {
                cursorQuery.close();
            }
        } catch (Throwable th) {
            if (0 != 0) {
                (objArr == true ? 1 : 0).close();
                throw th;
            }
            throw th;
        }
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        Resources resources = getResources();
        menu.add(0, 1, 0, resources.getString(R.string.importToFDNfromContacts)).setIcon(R.drawable.ic_menu_contact);
        menu.add(0, 2, 0, resources.getString(R.string.menu_delete)).setIcon(android.R.drawable.ic_menu_delete);
        return true;
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean zOnPrepareOptionsMenu = super.onPrepareOptionsMenu(menu);
        if (this.mDataBusy) {
            return false;
        }
        return zOnPrepareOptionsMenu;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Log.d("PhoneGlobals", "[onOptionsItemSelected]item text = " + ((Object) menuItem.getTitle()));
        int itemId = menuItem.getItemId();
        if (itemId == 1) {
            startActivityForResult(CONTACT_IMPORT_INTENT, 200);
            return true;
        }
        if (itemId == 2) {
            if (!needTipsPIN2Blocked()) {
                deleteSelected();
            }
            return true;
        }
        if (itemId == 16908332) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // com.android.phone.settings.fdn.BaseFdnContactScreen
    protected void resolveIntent() {
        super.resolveIntent();
        this.mAddContact = getIntent().getBooleanExtra("addContact", false);
    }

    private void setupView() {
        EditText editText = (EditText) findViewById(R.id.fdn_name);
        this.mNameField = editText;
        if (editText != null) {
            editText.setOnFocusChangeListener(this.mOnFocusChangeHandler);
            this.mNameField.setOnClickListener(this.mClicked);
            this.mNameField.addTextChangedListener(this.mTextWatcher);
        }
        EditText editText2 = (EditText) findViewById(R.id.fdn_number);
        this.mNumberField = editText2;
        if (editText2 != null) {
            editText2.setTextDirection(3);
            this.mNumberField.setKeyListener(DialerKeyListener.getInstance());
            this.mNumberField.setOnFocusChangeListener(this.mOnFocusChangeHandler);
            this.mNumberField.setOnClickListener(this.mClicked);
            this.mNumberField.addTextChangedListener(this.mTextWatcher);
        }
        if (!this.mAddContact) {
            EditText editText3 = this.mNameField;
            if (editText3 != null) {
                editText3.setText(this.mName);
            }
            EditText editText4 = this.mNumberField;
            if (editText4 != null) {
                editText4.setText(this.mNumber);
            }
        }
        Button button = (Button) findViewById(R.id.button);
        this.mButton = button;
        if (button != null) {
            button.setOnClickListener(this.mClicked);
            setButtonEnabled();
        }
        this.mPinFieldContainer = (LinearLayout) findViewById(R.id.pinc);
    }

    private String getNameFromTextField() {
        return this.mNameField.getText().toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getNumberFromTextField() {
        return this.mNumberField.getText().toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setButtonEnabled() {
        EditText editText;
        Button button = this.mButton;
        if (button == null || (editText = this.mNameField) == null || this.mNumberField == null) {
            return;
        }
        button.setEnabled(editText.length() > 0 && this.mNumberField.length() > 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isValidNumber(String str) {
        return str.length() > 0;
    }

    private void addContact() {
        log("addContact");
        String strStripSeparators = PhoneNumberUtils.stripSeparators(getNumberFromTextField());
        if (!isValidNumber(strStripSeparators)) {
            handleResult(false, true);
            return;
        }
        Uri contentUri = FdnList.getContentUri(this.mSubscriptionInfoHelper);
        ContentValues contentValues = new ContentValues(3);
        contentValues.put(CallMethod.ARG_SHARE_CALLBACK_TAG, getNameFromTextField());
        contentValues.put("number", strStripSeparators);
        contentValues.put("pin2", this.mPin2);
        BaseFdnContactScreen.QueryHandler queryHandler = new BaseFdnContactScreen.QueryHandler(getContentResolver());
        this.mQueryHandler = queryHandler;
        queryHandler.startInsert(0, null, contentUri, contentValues);
        displayProgress(true);
        showStatus(getResources().getText(R.string.adding_fdn_contact));
    }

    private void updateContact() {
        log("updateContact");
        String nameFromTextField = getNameFromTextField();
        String strStripSeparators = PhoneNumberUtils.stripSeparators(getNumberFromTextField());
        if (!isValidNumber(strStripSeparators)) {
            handleResult(false, true);
            return;
        }
        Uri contentUri = FdnList.getContentUri(this.mSubscriptionInfoHelper);
        ContentValues contentValues = new ContentValues();
        contentValues.put(CallMethod.ARG_SHARE_CALLBACK_TAG, this.mName);
        contentValues.put("number", this.mNumber);
        contentValues.put("newTag", nameFromTextField);
        contentValues.put("newNumber", strStripSeparators);
        contentValues.put("pin2", this.mPin2);
        BaseFdnContactScreen.QueryHandler queryHandler = new BaseFdnContactScreen.QueryHandler(getContentResolver());
        this.mQueryHandler = queryHandler;
        queryHandler.startUpdate(0, null, contentUri, contentValues, null, null);
        displayProgress(true);
        showStatus(getResources().getText(R.string.updating_fdn_contact));
    }

    private void deleteSelected() {
        if (!this.mAddContact) {
            Intent intent = this.mSubscriptionInfoHelper.getIntent(DeleteFdnContactScreen.class);
            intent.putExtra("name", this.mName);
            intent.putExtra("number", this.mNumber);
            startActivityAsUser(intent, UserHandle.CURRENT);
        }
        finish();
    }

    @Override // com.android.phone.settings.fdn.BaseFdnContactScreen
    protected void displayProgress(boolean z) {
        super.displayProgress(z);
        this.mDataBusy = z;
        this.mButton.setClickable(!z);
    }

    @Override // com.android.phone.settings.fdn.BaseFdnContactScreen
    protected void handleResult(boolean z, boolean z2) {
        if (z) {
            log("handleResult: success!");
            showStatus(getResources().getText(this.mAddContact ? R.string.fdn_contact_added : R.string.fdn_contact_updated));
        } else {
            log("handleResult: failed!");
            if (this.mPhone.getIccCard().getIccPin2Blocked()) {
                showFdnErrorDialog(17);
                return;
            } else if (z2) {
                showStatus(getResources().getText(R.string.fdn_contact_number_invalid));
            } else if (this.mSubscriptionInfoHelper.getPhone().getIccCard().getIccPin2Blocked()) {
                showStatus(getResources().getText(R.string.fdn_enable_puk2_requested));
            } else if (this.mSubscriptionInfoHelper.getPhone().getIccCard().getIccPuk2Blocked()) {
                showStatus(getResources().getText(R.string.puk2_blocked));
            } else {
                showStatus(getResources().getText(R.string.pin2_or_fdn_invalid));
            }
        }
        this.mHandler.postDelayed(new Runnable() { // from class: com.android.phone.settings.fdn.EditFdnContactScreen$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$handleResult$0();
            }
        }, 2000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleResult$0() {
        finish();
    }

    @Override // com.android.phone.settings.fdn.BaseFdnContactScreen
    protected void pin2AuthenticationSucceed() {
        if (this.mAddContact) {
            addContact();
        } else {
            updateContact();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean needTipsPIN2Blocked() {
        if (CallSettingUtils.getPin2RetryNumber(this.mSubscriptionInfoHelper.getSubId()) != 0) {
            return false;
        }
        log("[onClick] retry number is 0, tips...");
        Toast.makeText(this, getString(R.string.fdn_puk_need_tips), 0).show();
        return true;
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
