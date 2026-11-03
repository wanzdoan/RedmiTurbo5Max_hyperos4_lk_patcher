package com.android.phone.settings;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceViewHolder;
import com.android.phone.R;

/* JADX INFO: loaded from: classes.dex */
public class PreferenceCategoryNoDivider extends PreferenceCategory {
    public PreferenceCategoryNoDivider(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // androidx.preference.PreferenceCategory, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        View view = preferenceViewHolder.itemView;
        view.setBackground(null);
        view.setPadding(view.getPaddingLeft(), getContext().getResources().getDimensionPixelSize(R.dimen.preference_category_padding_top), view.getPaddingRight(), view.getPaddingBottom());
        super.onBindViewHolder(preferenceViewHolder);
    }
}
