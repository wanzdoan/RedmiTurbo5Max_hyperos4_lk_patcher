package com.android.phone.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.EditText;
import com.android.phone.common.R$styleable;
import com.android.phone.common.util.ViewUtil;

/* JADX INFO: loaded from: classes.dex */
public abstract class ResizingTextEditText extends EditText {
    private boolean mIsResizeEnabled;
    private final int mMinTextSize;
    private final int mOriginalTextSize;

    public ResizingTextEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mIsResizeEnabled = true;
        int textSize = (int) getTextSize();
        this.mOriginalTextSize = textSize;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ResizingText);
        this.mMinTextSize = (int) typedArrayObtainStyledAttributes.getDimension(R$styleable.ResizingText_resizing_text_min_size, textSize);
        typedArrayObtainStyledAttributes.recycle();
    }

    @Override // android.widget.TextView
    protected void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        super.onTextChanged(charSequence, i, i2, i3);
        if (this.mIsResizeEnabled) {
            ViewUtil.resizeText(this, this.mOriginalTextSize, this.mMinTextSize);
        }
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (this.mIsResizeEnabled) {
            ViewUtil.resizeText(this, this.mOriginalTextSize, this.mMinTextSize);
        }
    }
}
