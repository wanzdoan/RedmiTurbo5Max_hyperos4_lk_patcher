package com.android.phone.settings;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.services.telephony.SimpleFeatures;

/* JADX INFO: loaded from: classes.dex */
public class RestrictedPreference extends Preference {
    private boolean mDisabledByAdmin;

    public RestrictedPreference(Context context) {
        super(context);
    }

    public RestrictedPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public RestrictedPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public RestrictedPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View view = preferenceViewHolder.itemView;
        if (this.mDisabledByAdmin) {
            view.setEnabled(true);
        }
        TextView textView = (TextView) view.findViewById(R.id.summary);
        if (textView == null || !this.mDisabledByAdmin) {
            return;
        }
        textView.setText(isEnabled() ? com.android.phone.R.string.enabled_by_admin : com.android.phone.R.string.disabled_by_admin);
        textView.setVisibility(0);
    }

    @Override // androidx.preference.Preference
    public void setEnabled(boolean z) {
        if (z && this.mDisabledByAdmin) {
            setDisabledByAdmin(false);
        } else {
            super.setEnabled(z);
        }
    }

    public void setDisabledByAdmin(boolean z) {
        if (this.mDisabledByAdmin != z) {
            this.mDisabledByAdmin = z;
            setEnabled(!z);
        }
    }

    @Override // androidx.preference.Preference
    public void performClick(View view) {
        if (this.mDisabledByAdmin) {
            SimpleFeatures.ShowAdminSupportDetailsIntent(getContext());
        } else {
            super.performClick(view);
        }
    }
}
