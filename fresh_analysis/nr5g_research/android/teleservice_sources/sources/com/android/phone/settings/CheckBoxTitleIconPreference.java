package com.android.phone.settings;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import com.android.phone.custompreferences.CustomCheckBoxPreference;

/* JADX INFO: loaded from: classes.dex */
public class CheckBoxTitleIconPreference extends CustomCheckBoxPreference {
    private int mTitleIconResId;

    public CheckBoxTitleIconPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public CheckBoxTitleIconPreference(Context context) {
        super(context);
    }

    public CheckBoxTitleIconPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setTitleIconResource(int i) {
        this.mTitleIconResId = i;
    }

    @Override // com.android.phone.custompreferences.CustomCheckBoxPreference, androidx.preference.CheckBoxPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        TextView textView = (TextView) preferenceViewHolder.itemView.findViewById(R.id.title);
        if (textView != null) {
            textView.setCompoundDrawablePadding(4);
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, this.mTitleIconResId, 0);
        }
    }
}
