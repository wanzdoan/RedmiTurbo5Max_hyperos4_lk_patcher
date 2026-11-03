package com.android.phone.settings;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

/* JADX INFO: loaded from: classes.dex */
public class TitleIconPreference extends Preference {
    private int mTitleIconResId;

    public TitleIconPreference(Context context) {
        this(context, null);
    }

    public TitleIconPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setTitleIconResource(int i) {
        this.mTitleIconResId = i;
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        TextView textView = (TextView) preferenceViewHolder.itemView.findViewById(R.id.title);
        if (textView != null) {
            textView.setCompoundDrawablePadding(4);
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, this.mTitleIconResId, 0);
        }
    }
}
