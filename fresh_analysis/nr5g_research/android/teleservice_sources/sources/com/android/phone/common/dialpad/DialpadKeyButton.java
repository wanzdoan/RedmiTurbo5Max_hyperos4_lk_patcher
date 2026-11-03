package com.android.phone.common.dialpad;

import android.content.Context;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;

/* JADX INFO: loaded from: classes.dex */
public class DialpadKeyButton extends FrameLayout {
    private static final int LONG_HOVER_TIMEOUT = ViewConfiguration.getLongPressTimeout() * 2;
    private AccessibilityManager mAccessibilityManager;
    private CharSequence mBackupContentDesc;
    private RectF mHoverBounds;
    private CharSequence mLongHoverContentDesc;
    private Runnable mLongHoverRunnable;
    private boolean mLongHovered;
    private boolean mWasClickable;
    private boolean mWasLongClickable;

    public DialpadKeyButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mHoverBounds = new RectF();
        initForAccessibility(context);
    }

    public DialpadKeyButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mHoverBounds = new RectF();
        initForAccessibility(context);
    }

    private void initForAccessibility(Context context) {
        this.mAccessibilityManager = (AccessibilityManager) context.getSystemService("accessibility");
    }

    public void setLongHoverContentDescription(CharSequence charSequence) {
        this.mLongHoverContentDesc = charSequence;
        if (this.mLongHovered) {
            super.setContentDescription(charSequence);
        }
    }

    @Override // android.view.View
    public void setContentDescription(CharSequence charSequence) {
        if (this.mLongHovered) {
            this.mBackupContentDesc = charSequence;
        } else {
            super.setContentDescription(charSequence);
        }
    }

    @Override // android.view.View
    public void setPressed(boolean z) {
        super.setPressed(z);
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mHoverBounds.left = getPaddingLeft();
        this.mHoverBounds.right = i - getPaddingRight();
        this.mHoverBounds.top = getPaddingTop();
        this.mHoverBounds.bottom = i2 - getPaddingBottom();
    }

    @Override // android.view.View
    public boolean performAccessibilityAction(int i, Bundle bundle) {
        if (i == 16) {
            simulateClickForAccessibility();
            return true;
        }
        return super.performAccessibilityAction(i, bundle);
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        if (this.mAccessibilityManager.isEnabled() && this.mAccessibilityManager.isTouchExplorationEnabled()) {
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 9) {
                this.mWasClickable = isClickable();
                boolean zIsLongClickable = isLongClickable();
                this.mWasLongClickable = zIsLongClickable;
                if (zIsLongClickable && this.mLongHoverContentDesc != null) {
                    if (this.mLongHoverRunnable == null) {
                        this.mLongHoverRunnable = new Runnable() { // from class: com.android.phone.common.dialpad.DialpadKeyButton.1
                            @Override // java.lang.Runnable
                            public void run() {
                                DialpadKeyButton.this.setLongHovered(true);
                                DialpadKeyButton dialpadKeyButton = DialpadKeyButton.this;
                                dialpadKeyButton.announceForAccessibility(dialpadKeyButton.mLongHoverContentDesc);
                            }
                        };
                    }
                    postDelayed(this.mLongHoverRunnable, LONG_HOVER_TIMEOUT);
                }
                setClickable(false);
                setLongClickable(false);
            } else if (actionMasked == 10) {
                if (this.mHoverBounds.contains(motionEvent.getX(), motionEvent.getY())) {
                    if (this.mLongHovered) {
                        simulateClickForAccessibility();
                        performLongClick();
                    } else {
                        simulateClickForAccessibility();
                    }
                }
                cancelLongHover();
                setClickable(this.mWasClickable);
                setLongClickable(this.mWasLongClickable);
            }
        }
        return super.onHoverEvent(motionEvent);
    }

    private void simulateClickForAccessibility() {
        if (isPressed()) {
            return;
        }
        setPressed(true);
        sendAccessibilityEvent(1);
        setPressed(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setLongHovered(boolean z) {
        if (this.mLongHovered != z) {
            this.mLongHovered = z;
            if (z) {
                this.mBackupContentDesc = getContentDescription();
                super.setContentDescription(this.mLongHoverContentDesc);
            } else {
                super.setContentDescription(this.mBackupContentDesc);
            }
        }
    }

    private void cancelLongHover() {
        Runnable runnable = this.mLongHoverRunnable;
        if (runnable != null) {
            removeCallbacks(runnable);
        }
        setLongHovered(false);
    }
}
