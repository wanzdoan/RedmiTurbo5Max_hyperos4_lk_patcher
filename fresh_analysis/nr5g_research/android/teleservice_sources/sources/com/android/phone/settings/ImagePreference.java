package com.android.phone.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceViewHolder;
import com.android.phone.R;

/* JADX INFO: loaded from: classes.dex */
public class ImagePreference extends PreferenceCategory {
    private Drawable mImage;

    public ImagePreference(Context context) {
        this(context, null);
    }

    public ImagePreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setLayoutResource(R.layout.image_preference);
        initialize(context, attributeSet);
    }

    public ImagePreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @Override // androidx.preference.PreferenceCategory, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ((ImageView) preferenceViewHolder.findViewById(R.id.img)).setImageDrawable(this.mImage);
    }

    private void initialize(Context context, AttributeSet attributeSet) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ImagePreference);
        if (typedArrayObtainStyledAttributes != null) {
            this.mImage = context.getResources().getDrawable(typedArrayObtainStyledAttributes.getResourceId(R.styleable.ImagePreference_image, 0));
            typedArrayObtainStyledAttributes.recycle();
        }
    }
}
