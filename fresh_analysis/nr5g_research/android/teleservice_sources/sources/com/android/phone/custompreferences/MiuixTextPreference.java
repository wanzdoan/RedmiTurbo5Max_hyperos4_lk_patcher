package com.android.phone.custompreferences;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import androidx.preference.PreferenceViewHolder;
import miuix.preference.R$id;
import miuix.preference.TextPreference;

/* JADX INFO: loaded from: classes.dex */
public class MiuixTextPreference extends TextPreference {
    private Drawable mRightArrowDrawable;
    private boolean mShowRightArrow;
    private int mValueRes;

    public MiuixTextPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public MiuixTextPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MiuixTextPreference(Context context) {
        super(context);
    }

    public void setValue(String str) {
        setText(str);
    }

    public void setValue(int i) {
        setText(i);
        this.mValueRes = i;
    }

    public void setShowRightArrow(boolean z) {
        this.mShowRightArrow = z;
    }

    @Override // miuix.preference.TextPreference, miuix.preference.BasePreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        onBindView(preferenceViewHolder.itemView);
    }

    public void onBindView(View view) {
        ImageView imageView = (ImageView) view.findViewById(R$id.arrow_right);
        if (imageView != null) {
            imageView.setVisibility(this.mShowRightArrow ? 0 : 8);
            if (this.mShowRightArrow) {
                imageView.setVisibility(0);
                Drawable drawable = this.mRightArrowDrawable;
                if (drawable != null) {
                    imageView.setImageDrawable(drawable);
                    return;
                }
                return;
            }
            imageView.setVisibility(8);
        }
    }
}
