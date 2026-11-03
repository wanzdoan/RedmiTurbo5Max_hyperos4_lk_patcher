package com.android.phone.common.dialpad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

/* JADX INFO: loaded from: classes.dex */
public class DialpadTextView extends TextView {
    private Rect mTextBounds;
    private String mTextStr;

    public DialpadTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mTextBounds = new Rect();
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        TextPaint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        String str = this.mTextStr;
        Rect rect = this.mTextBounds;
        canvas.drawText(str, -rect.left, -rect.top, paint);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.mTextStr = getText().toString();
        TextPaint paint = getPaint();
        String str = this.mTextStr;
        paint.getTextBounds(str, 0, str.length(), this.mTextBounds);
        setMeasuredDimension(TextView.resolveSize(this.mTextBounds.width(), i), TextView.resolveSize(this.mTextBounds.height(), i2));
    }
}
