package com.android.phone;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import com.android.phone.custompreferences.CustomEditTextPreference;

/* JADX INFO: loaded from: classes.dex */
public class MultiLineTitleEditTextPreference extends CustomEditTextPreference {
    public MultiLineTitleEditTextPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public MultiLineTitleEditTextPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MultiLineTitleEditTextPreference(Context context) {
        super(context);
    }

    @Override // com.android.phone.custompreferences.CustomEditTextPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        TextView textView = (TextView) preferenceViewHolder.itemView.findViewById(android.R.id.title);
        if (textView != null) {
            textView.setSingleLine(false);
        }
    }

    @Override // com.android.phone.custompreferences.CustomEditTextPreference
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        EditText editText = this.mEditText;
        if (editText != null) {
            editText.setTextAlignment(5);
        }
    }
}
