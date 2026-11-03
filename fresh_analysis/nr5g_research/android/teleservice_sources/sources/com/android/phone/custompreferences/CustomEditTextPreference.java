package com.android.phone.custompreferences;

import android.R;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import miuix.appcompat.app.AlertDialog;
import miuix.preference.EditTextPreferenceDialogFragmentCompat;
import miuix.preference.R$dimen;
import miuix.preference.R$id;
import miuix.preference.R$layout;

/* JADX INFO: loaded from: classes.dex */
public class CustomEditTextPreference extends EditTextPreference implements DialogInterface.OnClickListener {
    protected EditText mEditText;
    private CustomPreferenceDialogFragment mFragment;
    private String mHint;
    private String mInputType;
    private int mMaxLength;
    private DialogInterface.OnShowListener mOnShowListener;
    private String mRightValue;
    private TextWatcher mTextWacther;

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i) {
    }

    protected void onDialogClosed(boolean z) {
    }

    protected void onPrepareDialogBuilder(AlertDialog.Builder builder, DialogInterface.OnClickListener onClickListener) {
    }

    public CustomEditTextPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mMaxLength = Integer.MAX_VALUE;
    }

    public CustomEditTextPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mMaxLength = Integer.MAX_VALUE;
    }

    public CustomEditTextPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mMaxLength = Integer.MAX_VALUE;
        setWidgetLayoutResource(R$layout.miuix_preference_widget_text);
        int attributeResourceValue = attributeSet.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "hint", -1);
        if (attributeResourceValue != -1) {
            this.mHint = context.getResources().getString(attributeResourceValue);
        }
        int attributeIntValue = attributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "maxLength", -1);
        if (attributeIntValue != -1) {
            this.mMaxLength = attributeIntValue;
        }
        this.mInputType = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "inputType");
    }

    public CustomEditTextPreference(Context context) {
        super(context);
        this.mMaxLength = Integer.MAX_VALUE;
    }

    public EditText getEditText() {
        return this.mEditText;
    }

    public Dialog getDialog() {
        CustomPreferenceDialogFragment customPreferenceDialogFragment = this.mFragment;
        if (customPreferenceDialogFragment != null) {
            return customPreferenceDialogFragment.getDialog();
        }
        return null;
    }

    protected void onBindDialogView(View view) {
        EditText editText = (EditText) view.findViewById(R.id.edit);
        this.mEditText = editText;
        if (editText != null) {
            editText.setInputType(16385);
            this.mEditText.requestFocus();
            this.mEditText.setHint(this.mHint);
            this.mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(this.mMaxLength)});
            String str = this.mInputType;
            if (str != null) {
                try {
                    this.mEditText.setInputType(Integer.parseInt(str.substring(2), 16));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addTextChangeListenner(TextWatcher textWatcher) {
        this.mTextWacther = textWatcher;
        this.mEditText.addTextChangedListener(textWatcher);
    }

    public void setOnShowListener(DialogInterface.OnShowListener onShowListener) {
        this.mOnShowListener = onShowListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DialogInterface.OnShowListener getOnShowListener() {
        return this.mOnShowListener;
    }

    @Override // androidx.preference.DialogPreference, androidx.preference.Preference
    protected void onClick() {
        if (getDialog() != null && getDialog().isShowing()) {
            Log.i("CustomEditTextPreference", "Intercept click event due to dialog being showing.");
        } else {
            super.onClick();
        }
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View view = preferenceViewHolder.itemView;
        TextView textView = (TextView) view.findViewById(R$id.text_right);
        if (textView != null) {
            if (!TextUtils.isEmpty(this.mRightValue)) {
                textView.setPaddingRelative(0, 0, textView.getResources().getDimensionPixelSize(R$dimen.miuix_preference_item_padding_inner), 0);
                textView.setText(this.mRightValue);
                textView.setVisibility(0);
            } else {
                textView.setVisibility(8);
            }
        }
        TextView textView2 = (TextView) view.findViewById(R.id.summary);
        CharSequence summary = getSummary();
        if (textView2 != null) {
            if (!TextUtils.isEmpty(summary)) {
                textView2.setVisibility(0);
            } else {
                textView2.setVisibility(8);
            }
        }
        ImageView imageView = (ImageView) view.findViewById(R$id.arrow_right);
        if (imageView != null) {
            imageView.setVisibility(0);
        }
    }

    public void setRightValue(String str) {
        if (TextUtils.equals(this.mRightValue, str)) {
            return;
        }
        this.mRightValue = str;
        notifyChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setFragment(CustomPreferenceDialogFragment customPreferenceDialogFragment) {
        this.mFragment = customPreferenceDialogFragment;
    }

    public class CustomPreferenceDialogFragment extends EditTextPreferenceDialogFragmentCompat {
        public static CustomPreferenceDialogFragment newInstance(String str) {
            CustomPreferenceDialogFragment customPreferenceDialogFragment = new CustomPreferenceDialogFragment();
            Bundle bundle = new Bundle(1);
            bundle.putString("key", str);
            customPreferenceDialogFragment.setArguments(bundle);
            return customPreferenceDialogFragment;
        }

        private CustomEditTextPreference getCustomizablePreference() {
            return (CustomEditTextPreference) getPreference();
        }

        @Override // androidx.preference.EditTextPreferenceDialogFragmentCompat, androidx.preference.PreferenceDialogFragmentCompat
        protected void onBindDialogView(View view) {
            super.onBindDialogView(view);
            getCustomizablePreference().onBindDialogView(view);
        }

        @Override // miuix.preference.EditTextPreferenceDialogFragmentCompat
        protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
            super.onPrepareDialogBuilder(builder);
            getCustomizablePreference().setFragment(this);
            getCustomizablePreference().onPrepareDialogBuilder(builder, this);
        }

        @Override // androidx.preference.EditTextPreferenceDialogFragmentCompat, androidx.preference.PreferenceDialogFragmentCompat
        public void onDialogClosed(boolean z) {
            super.onDialogClosed(z);
            getCustomizablePreference().onDialogClosed(z);
        }

        @Override // androidx.preference.PreferenceDialogFragmentCompat, android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            super.onClick(dialogInterface, i);
            getCustomizablePreference().onClick(dialogInterface, i);
        }

        @Override // miuix.preference.EditTextPreferenceDialogFragmentCompat, androidx.preference.PreferenceDialogFragmentCompat, androidx.fragment.app.DialogFragment
        public Dialog onCreateDialog(Bundle bundle) {
            Dialog dialogOnCreateDialog = super.onCreateDialog(bundle);
            dialogOnCreateDialog.setOnShowListener(getCustomizablePreference().getOnShowListener());
            return dialogOnCreateDialog;
        }
    }

    public class SavedState extends Preference.BaseSavedState {
        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: com.android.phone.custompreferences.CustomEditTextPreference.SavedState.1
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public boolean mIsChecked;

        public SavedState(Parcel parcel) {
            super(parcel);
            this.mIsChecked = parcel.readBoolean();
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override // android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeBoolean(this.mIsChecked);
        }
    }
}
