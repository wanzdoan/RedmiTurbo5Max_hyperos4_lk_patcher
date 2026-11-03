package com.android.phone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextPaint;
import android.widget.TextView;

/* JADX INFO: loaded from: classes.dex */
public class DialpadTextView extends TextView {
    private Rect textBounds;
    private String textStr;

    public DialpadTextView(Context context) {
        super(context);
        this.textBounds = new Rect();
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        TextPaint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        String str = this.textStr;
        Rect rect = this.textBounds;
        canvas.drawText(str, -rect.left, -rect.top, paint);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.textStr = getText().toString();
        TextPaint paint = getPaint();
        String str = this.textStr;
        paint.getTextBounds(str, 0, str.length(), this.textBounds);
        setMeasuredDimension(TextView.resolveSize(this.textBounds.width(), i), TextView.resolveSize(this.textBounds.height(), i2));
    }
}
