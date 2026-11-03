package com.android.phone.settings;

import android.R;
import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import com.android.phone.custompreferences.MiuixTextPreference;

/* JADX INFO: loaded from: classes.dex */
public class LTRValuePreference extends MiuixTextPreference {
    public LTRValuePreference(Context context) {
        super(context);
    }

    public LTRValuePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.android.phone.custompreferences.MiuixTextPreference, miuix.preference.TextPreference, miuix.preference.BasePreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        TextView textView = (TextView) preferenceViewHolder.itemView.findViewById(R.id.summary);
        if (textView != null) {
            textView.setTextDirection(3);
            textView.setContentDescription(PhoneNumberUtils.createTtsSpannable(textView.getText()));
        }
        super.onBindViewHolder(preferenceViewHolder);
    }
}
