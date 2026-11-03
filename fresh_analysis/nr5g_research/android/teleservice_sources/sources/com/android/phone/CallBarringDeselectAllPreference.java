package com.android.phone;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import com.android.phone.settings.fdn.EditPinPreference;

/* JADX INFO: loaded from: classes.dex */
public class CallBarringDeselectAllPreference extends EditPinPreference {
    public CallBarringDeselectAllPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.preference.EditTextPreference, android.preference.DialogPreference
    protected void showDialog(Bundle bundle) {
        setDialogMessage(getContext().getString(R.string.messageCallBarring));
        super.showDialog(bundle);
    }

    @Override // com.android.phone.settings.fdn.EditPinPreference, android.preference.EditTextPreference, android.preference.DialogPreference
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        EditText editText = (EditText) view.findViewById(android.R.id.edit);
        if (editText != null) {
            editText.setVisibility(0);
        }
    }
}
