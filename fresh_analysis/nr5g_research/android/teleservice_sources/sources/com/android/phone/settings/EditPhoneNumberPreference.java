package com.android.phone.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.DialerKeyListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import com.android.phone.MiuiAnimUtil;
import com.android.phone.R;
import com.android.phone.custompreferences.CustomEditTextPreference;
import com.android.phone.utils.Utils;
import miui.telephony.PhoneNumberUtils;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyManagerEx;
import miuix.appcompat.app.AlertDialog;
import miuix.preference.PreferenceFragment;

/* JADX INFO: loaded from: classes.dex */
public class EditPhoneNumberPreference extends CustomEditTextPreference {
    private int mButtonClicked;
    private CharSequence mChangeNumberText;
    private boolean mChecked;
    private int mConfirmationMode;
    private Intent mContactListIntent;
    private ImageButton mContactPickButton;
    private View.OnFocusChangeListener mDialogFocusChangeListener;
    private OnDialogClosedListener mDialogOnClosedListener;
    private CharSequence mDisableText;
    private CharSequence mEnableText;
    private String mEncodedText;
    protected PreferenceFragment mFragment;
    private GetDefaultNumberListener mGetDefaultNumberListener;
    private String mPhoneNumber;
    private int mPrefId;
    private CharSequence mSummaryOff;
    private CharSequence mSummaryOn;
    private boolean mSwitchMode;

    public interface GetDefaultNumberListener {
        String onGetDefaultNumber(EditPhoneNumberPreference editPhoneNumberPreference);
    }

    public interface OnDialogClosedListener {
        void onDialogClosed(EditPhoneNumberPreference editPhoneNumberPreference, int i);
    }

    public EditPhoneNumberPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mEncodedText = null;
        setDialogLayoutResource(R.layout.miui_pref_dialog_editphonenumber);
        if (!TelephonyManager.isGoogleCsp() || TelephonyManagerEx.getDefault().isGoogleDialerDeleted(context)) {
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            this.mContactListIntent = intent;
            intent.setType("vnd.android.cursor.item/phone_v2");
            this.mContactListIntent.setPackage("com.android.contacts");
            this.mContactListIntent.putExtra("android.intent.extra.include_unknown_numbers", true);
        } else {
            Intent intent2 = new Intent("android.intent.action.PICK");
            this.mContactListIntent = intent2;
            intent2.setType("vnd.android.cursor.dir/phone_v2");
        }
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.EditPhoneNumberPreference, 0, R.style.EditPhoneNumberPreference);
        this.mEnableText = typedArrayObtainStyledAttributes.getString(R.styleable.EditPhoneNumberPreference_enableButtonText);
        this.mDisableText = typedArrayObtainStyledAttributes.getString(R.styleable.EditPhoneNumberPreference_disableButtonText);
        this.mChangeNumberText = typedArrayObtainStyledAttributes.getString(R.styleable.EditPhoneNumberPreference_changeNumButtonText);
        this.mConfirmationMode = typedArrayObtainStyledAttributes.getInt(R.styleable.EditPhoneNumberPreference_confirmMode, 0);
        typedArrayObtainStyledAttributes.recycle();
        TypedArray typedArrayObtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, android.R.styleable.CheckBoxPreference, 0, 0);
        this.mSummaryOn = typedArrayObtainStyledAttributes2.getString(0);
        this.mSummaryOff = typedArrayObtainStyledAttributes2.getString(1);
        typedArrayObtainStyledAttributes2.recycle();
        setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.android.phone.settings.EditPhoneNumberPreference.1
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                if (EditPhoneNumberPreference.this.mPhoneNumber == null || EditPhoneNumberPreference.this.mPhoneNumber.compareTo("") != 0) {
                    return;
                }
                AlertDialog alertDialog = (AlertDialog) dialogInterface;
                Button button = alertDialog.getButton(-1);
                alertDialog.getButton(-3);
                if (EditPhoneNumberPreference.this.mConfirmationMode == 1) {
                    if (!EditPhoneNumberPreference.this.mChecked) {
                        Editable text = ((CustomEditTextPreference) EditPhoneNumberPreference.this).mEditText != null ? ((CustomEditTextPreference) EditPhoneNumberPreference.this).mEditText.getText() : null;
                        button.setEnabled((text == null || TextUtils.isEmpty(text.toString())) ? false : true);
                    } else {
                        button.setEnabled(false);
                    }
                }
            }
        });
    }

    public EditPhoneNumberPreference(Context context) {
        super(context);
        this.mEncodedText = null;
    }

    @Override // com.android.phone.custompreferences.CustomEditTextPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        CharSequence summary;
        int i;
        super.onBindViewHolder(preferenceViewHolder);
        TextView textView = (TextView) preferenceViewHolder.itemView.findViewById(android.R.id.summary);
        if (textView != null) {
            if (this.mConfirmationMode == 1) {
                if (this.mChecked) {
                    summary = this.mSummaryOn;
                    if (summary == null) {
                        summary = getSummary();
                    }
                } else {
                    summary = this.mSummaryOff;
                    if (summary == null) {
                        summary = getSummary();
                    }
                }
            } else {
                summary = getSummary();
            }
            if (summary != null) {
                textView.setTextDirection(3);
                textView.setText(summary);
                textView.setContentDescription(Utils.getContentDescriptionForNumber(summary.toString()));
                i = 0;
            } else {
                textView.setContentDescription("");
                i = 8;
            }
            if (i != textView.getVisibility()) {
                textView.setVisibility(i);
            }
        }
    }

    @Override // androidx.preference.EditTextPreference, androidx.preference.Preference
    protected Parcelable onSaveInstanceState() {
        CustomEditTextPreference.SavedState savedState = new CustomEditTextPreference.SavedState(super.onSaveInstanceState());
        savedState.mIsChecked = this.mChecked;
        return savedState;
    }

    @Override // androidx.preference.EditTextPreference, androidx.preference.Preference
    protected void onRestoreInstanceState(Parcelable parcelable) {
        CustomEditTextPreference.SavedState savedState = (CustomEditTextPreference.SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mChecked = savedState.mIsChecked;
    }

    @Override // com.android.phone.custompreferences.CustomEditTextPreference
    protected void onBindDialogView(View view) {
        String strOnGetDefaultNumber;
        this.mButtonClicked = -2;
        super.onBindDialogView(view);
        this.mContactPickButton = (ImageButton) view.findViewById(R.id.select_contact);
        if (this.mEditText != null) {
            GetDefaultNumberListener getDefaultNumberListener = this.mGetDefaultNumberListener;
            if (getDefaultNumberListener != null && (strOnGetDefaultNumber = getDefaultNumberListener.onGetDefaultNumber(this)) != null) {
                this.mPhoneNumber = strOnGetDefaultNumber;
            }
            this.mEditText.setTextDirection(3);
            this.mEditText.setTextAlignment(5);
            this.mEditText.setText(TextUtils.isEmpty(this.mPhoneNumber) ? "" : this.mPhoneNumber);
            if (!TextUtils.isEmpty(this.mPhoneNumber)) {
                EditText editText = this.mEditText;
                editText.setContentDescription(Utils.getContentDescriptionForNumber(String.valueOf(editText.getText())));
            }
            this.mEditText.requestFocus();
            this.mEditText.setSelection(TextUtils.isEmpty(this.mPhoneNumber) ? 0 : this.mPhoneNumber.length());
            this.mEditText.setMovementMethod(ArrowKeyMovementMethod.getInstance());
            this.mEditText.setKeyListener(DialerKeyListener.getInstance());
            this.mEditText.setOnFocusChangeListener(this.mDialogFocusChangeListener);
            this.mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
            this.mEditText.addTextChangedListener(new TextWatcher() { // from class: com.android.phone.settings.EditPhoneNumberPreference.2
                private Button currentButton;
                private Button neutralButton;
                private Button positiveButton;

                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    if (TextUtils.isEmpty(charSequence)) {
                        return;
                    }
                    ((CustomEditTextPreference) EditPhoneNumberPreference.this).mEditText.setContentDescription(Utils.getContentDescriptionForNumber(charSequence.toString()));
                }

                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                    if (this.positiveButton == null || this.neutralButton == null) {
                        AlertDialog alertDialog = (AlertDialog) EditPhoneNumberPreference.this.getDialog();
                        if (alertDialog == null) {
                            return;
                        } else {
                            this.positiveButton = alertDialog.getButton(-1);
                        }
                    }
                    if (EditPhoneNumberPreference.this.mConfirmationMode == 1) {
                        this.currentButton = this.positiveButton;
                    }
                    if (this.currentButton == null) {
                        return;
                    }
                    String string = editable.toString();
                    if (TextUtils.isEmpty(string) || string.length() >= 20) {
                        this.currentButton.setEnabled(false);
                    } else {
                        this.currentButton.setEnabled(true);
                    }
                }
            });
        }
        ImageButton imageButton = this.mContactPickButton;
        if (imageButton != null) {
            imageButton.setOnClickListener(new View.OnClickListener() { // from class: com.android.phone.settings.EditPhoneNumberPreference.3
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    EditPhoneNumberPreference editPhoneNumberPreference = EditPhoneNumberPreference.this;
                    PreferenceFragment preferenceFragment = editPhoneNumberPreference.mFragment;
                    if (preferenceFragment != null) {
                        preferenceFragment.startActivityForResult(editPhoneNumberPreference.mContactListIntent, EditPhoneNumberPreference.this.mPrefId);
                    }
                }
            });
            MiuiAnimUtil.alphaTouch(this.mContactPickButton);
        }
    }

    @Override // com.android.phone.custompreferences.CustomEditTextPreference
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder, DialogInterface.OnClickListener onClickListener) {
        if (this.mConfirmationMode == 1) {
            if (this.mChecked) {
                builder.setPositiveButton(this.mChangeNumberText, this);
                builder.setNeutralButton(this.mDisableText, this);
                this.mSwitchMode = false;
            } else {
                builder.setPositiveButton(this.mEnableText, this);
                this.mSwitchMode = true;
            }
        }
    }

    public void setDialogOnClosedListener(OnDialogClosedListener onDialogClosedListener) {
        this.mDialogOnClosedListener = onDialogClosedListener;
    }

    public void setIntentFragment(PreferenceFragment preferenceFragment, int i) {
        this.mFragment = preferenceFragment;
        this.mPrefId = i;
        this.mGetDefaultNumberListener = null;
    }

    public void setIntentFragment(PreferenceFragment preferenceFragment, int i, GetDefaultNumberListener getDefaultNumberListener) {
        this.mFragment = preferenceFragment;
        this.mPrefId = i;
        this.mGetDefaultNumberListener = getDefaultNumberListener;
    }

    public void onPickActivityResult(String str) {
        EditText editText = getEditText();
        Log.i(EditPhoneNumberPreference.class.getSimpleName(), "editText=" + editText + "pickedValue" + str);
        if (editText != null) {
            if (TextUtils.isEmpty(str)) {
                str = "";
            }
            editText.setText(str);
            editText.setSelection(editText.getText().length());
        }
    }

    @Override // com.android.phone.custompreferences.CustomEditTextPreference, android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i) {
        if (this.mSwitchMode) {
            if (i == -2) {
                this.mSwitchMode = false;
            }
        } else if (i == -3) {
            this.mSwitchMode = true;
        }
        if (this.mConfirmationMode == 1 && this.mSwitchMode) {
            if (!isToggled()) {
                setSummaryOn("");
            }
            setToggled(!isToggled());
        }
        this.mButtonClicked = i;
    }

    @Override // com.android.phone.custompreferences.CustomEditTextPreference
    protected void onDialogClosed(boolean z) {
        int i = this.mButtonClicked;
        if (i == -1 || i == -3) {
            setPhoneNumber(getEditText().getText().toString());
            setText(getStringValue());
        } else {
            super.onDialogClosed(z);
        }
        OnDialogClosedListener onDialogClosedListener = this.mDialogOnClosedListener;
        if (onDialogClosedListener != null) {
            onDialogClosedListener.onDialogClosed(this, this.mButtonClicked);
        }
    }

    public boolean isToggled() {
        return this.mChecked;
    }

    public EditPhoneNumberPreference setToggled(boolean z) {
        this.mChecked = z;
        setText(getStringValue());
        notifyChanged();
        return this;
    }

    public String getPhoneNumber() {
        String str = this.mPhoneNumber;
        if (str == null) {
            str = "";
        }
        return PhoneNumberUtils.stripSeparators(str);
    }

    protected String getRawPhoneNumber() {
        return this.mPhoneNumber;
    }

    public EditPhoneNumberPreference setPhoneNumber(String str) {
        this.mPhoneNumber = str;
        setText(getStringValue());
        notifyChanged();
        return this;
    }

    @Override // androidx.preference.Preference
    protected void onSetInitialValue(boolean z, Object obj) {
        String persistedString;
        if (z) {
            persistedString = getPersistedString(getStringValue());
        } else {
            persistedString = (String) obj;
        }
        setValueFromString(persistedString);
    }

    @Override // androidx.preference.EditTextPreference, androidx.preference.Preference
    public boolean shouldDisableDependents() {
        String str;
        if (this.mConfirmationMode != 1 || (str = this.mEncodedText) == null) {
            return TextUtils.isEmpty(this.mPhoneNumber) && this.mConfirmationMode == 0;
        }
        return str.split(":", 2)[0].equals("1");
    }

    @Override // androidx.preference.Preference
    protected boolean persistString(String str) {
        this.mEncodedText = str;
        return super.persistString(str);
    }

    public EditPhoneNumberPreference setSummaryOn(CharSequence charSequence) {
        this.mSummaryOn = charSequence;
        if (isToggled()) {
            notifyChanged();
        }
        return this;
    }

    public CharSequence getSummaryOn() {
        return this.mSummaryOn;
    }

    protected void setValueFromString(String str) {
        String[] strArrSplit = str.split(":", 2);
        setToggled(strArrSplit[0].equals("1"));
        setPhoneNumber(strArrSplit[1]);
    }

    protected String getStringValue() {
        StringBuilder sb = new StringBuilder();
        sb.append(isToggled() ? "1" : "0");
        sb.append(":");
        sb.append(getPhoneNumber());
        return sb.toString();
    }
}
