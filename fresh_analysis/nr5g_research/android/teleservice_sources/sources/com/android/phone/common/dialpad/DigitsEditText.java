package com.android.phone.common.dialpad;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import com.android.phone.common.widget.ResizingTextEditText;

/* JADX INFO: loaded from: classes.dex */
public class DigitsEditText extends ResizingTextEditText {
    public DigitsEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setInputType(getInputType() | 524288);
        setShowSoftInputOnFocus(false);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onFocusChanged(boolean z, int i, Rect rect) {
        super.onFocusChanged(z, i, rect);
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService("input_method");
        if (inputMethodManager == null || !inputMethodManager.isActive(this)) {
            return;
        }
        inputMethodManager.hideSoftInputFromWindow(getApplicationWindowToken(), 0);
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean zOnTouchEvent = super.onTouchEvent(motionEvent);
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService("input_method");
        if (inputMethodManager != null && inputMethodManager.isActive(this)) {
            inputMethodManager.hideSoftInputFromWindow(getApplicationWindowToken(), 0);
        }
        return zOnTouchEvent;
    }
}
