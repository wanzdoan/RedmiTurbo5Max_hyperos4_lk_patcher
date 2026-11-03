package com.android.phone.settings.fdn;

import android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public class EditPinPreference extends EditTextPreference {
    private OnPinEnteredListener mPinListener;
    private boolean shouldHideButtons;

    public interface OnPinEnteredListener {
        void onPinEntered(EditPinPreference editPinPreference, boolean z);
    }

    public void setOnPinEnteredListener(OnPinEnteredListener onPinEnteredListener) {
        this.mPinListener = onPinEnteredListener;
    }

    public EditPinPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public EditPinPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.preference.EditTextPreference, android.preference.DialogPreference
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        getEditText().setInputType(18);
        this.shouldHideButtons = view.findViewById(R.id.edit) == null;
    }

    @Override // android.preference.DialogPreference
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        if (this.shouldHideButtons) {
            builder.setPositiveButton((CharSequence) null, this);
            builder.setNegativeButton((CharSequence) null, this);
        }
    }

    @Override // android.preference.EditTextPreference, android.preference.DialogPreference
    protected void onDialogClosed(boolean z) {
        super.onDialogClosed(z);
        OnPinEnteredListener onPinEnteredListener = this.mPinListener;
        if (onPinEnteredListener != null) {
            onPinEnteredListener.onPinEntered(this, z);
        }
    }

    public void showPinDialog() {
        showDialog(null);
    }
}
