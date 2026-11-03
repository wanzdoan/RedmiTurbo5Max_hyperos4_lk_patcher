package com.android.phone.settings;

import android.R;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import com.android.phone.custompreferences.CustomEditTextPreference;
import miui.telephony.PhoneNumberUtils;
import miuix.preference.R$dimen;
import miuix.preference.R$id;

/* JADX INFO: loaded from: classes.dex */
public class LTREditTextPreference extends CustomEditTextPreference {
    private boolean isSummaryLTR;

    public LTREditTextPreference(Context context, AttributeSet attributeSet, int i) {
        super(context);
        this.isSummaryLTR = true;
    }

    public LTREditTextPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.isSummaryLTR = true;
    }

    @Override // com.android.phone.custompreferences.CustomEditTextPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        TextView textView;
        View view = preferenceViewHolder.itemView;
        if (this.isSummaryLTR && (textView = (TextView) view.findViewById(R.id.summary)) != null) {
            textView.setTextDirection(3);
        }
        super.onBindViewHolder(preferenceViewHolder);
        TextView textView2 = (TextView) view.findViewById(R$id.text_right);
        if (textView2 != null) {
            textView2.setMaxWidth(textView2.getResources().getDimensionPixelSize(R$dimen.miuix_preference_widget_layout_max_width));
            textView2.setTextDirection(3);
            textView2.setSingleLine(true);
            textView2.setMaxLines(1);
            textView2.setEllipsize(TextUtils.TruncateAt.END);
            textView2.setHorizontallyScrolling(false);
            String strValueOf = String.valueOf(textView2.getText());
            if (PhoneNumberUtils.isGlobalPhoneNumber(strValueOf) || PhoneNumberUtils.isChinaMobileNumber(strValueOf)) {
                textView2.setContentDescription(PhoneNumberUtils.createTtsSpannable(strValueOf));
            }
        }
    }

    @Override // com.android.phone.custompreferences.CustomEditTextPreference
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        EditText editText = this.mEditText;
        if (editText != null) {
            editText.requestFocus();
            this.mEditText.setTextDirection(3);
            this.mEditText.setTextAlignment(5);
            this.mEditText.setInputType(3);
        }
    }

    public void setSummaryLTR(boolean z) {
        this.isSummaryLTR = z;
    }

    public void refreshRightValue() {
        notifyChanged();
    }
}
