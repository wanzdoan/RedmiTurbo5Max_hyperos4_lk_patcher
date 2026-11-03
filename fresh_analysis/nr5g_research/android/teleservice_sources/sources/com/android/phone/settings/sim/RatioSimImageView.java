package com.android.phone.settings.sim;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import com.android.phone.R;

/* JADX INFO: loaded from: classes.dex */
public class RatioSimImageView extends ImageView {
    private int mOptionType;

    public RatioSimImageView(Context context) {
        this(context, null);
    }

    public RatioSimImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RatioSimImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initialize(context, attributeSet);
    }

    private void initialize(Context context, AttributeSet attributeSet) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.RatioSimImageView);
        if (typedArrayObtainStyledAttributes != null) {
            this.mOptionType = typedArrayObtainStyledAttributes.getInteger(R.styleable.RatioSimImageView_optionType, 1);
            typedArrayObtainStyledAttributes.recycle();
        }
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int i = this.mOptionType;
        int i2 = 3;
        if (i != 1) {
            if (i == 2) {
                i2 = 2;
            } else if (i != 3) {
                i2 = 0;
            }
        }
        setMaxWidth((((Math.min(point.x, point.y) - (getPixelSize(R.dimen.sim_divider_width) * 2)) - (getPixelSize(R.dimen.default_sim_stroke_width) * 2)) - (getPixelSize(R.dimen.default_sim_margin) * (i2 - 1))) / i2);
    }

    private int getPixelSize(int i) {
        return getResources().getDimensionPixelSize(i);
    }
}
