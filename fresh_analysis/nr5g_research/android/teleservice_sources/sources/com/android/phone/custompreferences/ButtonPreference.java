package com.android.phone.custompreferences;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.phone.R;

/* JADX INFO: loaded from: classes.dex */
public class ButtonPreference extends Preference {
    private Preference.OnPreferenceClickListener mListener;
    private String mText;

    public ButtonPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public ButtonPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.buttonPreferenceStyle);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View view = preferenceViewHolder.itemView;
        view.setBackground(null);
        Button button = (Button) view.findViewById(android.R.id.button1);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() { // from class: com.android.phone.custompreferences.ButtonPreference.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    if (ButtonPreference.this.mListener != null) {
                        ButtonPreference.this.mListener.onPreferenceClick(ButtonPreference.this);
                    }
                }
            });
            button.setText(this.mText);
        }
    }

    @Override // androidx.preference.Preference
    public void setOnPreferenceClickListener(Preference.OnPreferenceClickListener onPreferenceClickListener) {
        this.mListener = onPreferenceClickListener;
    }
}
