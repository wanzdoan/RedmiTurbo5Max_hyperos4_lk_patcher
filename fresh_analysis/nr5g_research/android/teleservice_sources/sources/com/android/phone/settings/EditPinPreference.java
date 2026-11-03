package com.android.phone.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import miuix.appcompat.app.AlertDialog;

/* JADX INFO: loaded from: classes.dex */
public class EditPinPreference extends LTREditTextPreference {
    private boolean shouldHideButtons;

    public EditPinPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.android.phone.settings.LTREditTextPreference, com.android.phone.custompreferences.CustomEditTextPreference
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        EditText editText = this.mEditText;
        if (editText != null) {
            editText.setInputType(2);
            this.mEditText.requestFocus();
        }
        this.shouldHideButtons = this.mEditText == null;
    }

    @Override // com.android.phone.custompreferences.CustomEditTextPreference
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder, DialogInterface.OnClickListener onClickListener) {
        super.onPrepareDialogBuilder(builder, onClickListener);
        if (this.shouldHideButtons) {
            builder.setPositiveButton((CharSequence) null, this);
            builder.setNegativeButton((CharSequence) null, this);
        }
    }
}
