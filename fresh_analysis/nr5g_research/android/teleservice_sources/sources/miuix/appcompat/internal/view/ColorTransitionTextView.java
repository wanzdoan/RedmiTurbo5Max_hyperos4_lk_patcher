package miuix.appcompat.internal.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import miuix.appcompat.R$color;

/* JADX INFO: loaded from: classes2.dex */
public class ColorTransitionTextView extends TextView {
    private boolean hasTransitedColor;
    private int mAnimateColor;
    private ValueAnimator mAnimator;
    private int mNormalColor;
    private ColorStateList mOriginColor;
    private int mTransitedColor;

    public ColorTransitionTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.hasTransitedColor = false;
        setupColors();
    }

    private void setupColors() {
        ColorStateList textColors = getTextColors();
        this.mOriginColor = textColors;
        int colorForState = textColors.getColorForState(TextView.ENABLED_STATE_SET, getResources().getColor(R$color.miuix_appcompat_action_bar_title_text_color_light));
        this.mNormalColor = colorForState;
        int colorForState2 = this.mOriginColor.getColorForState(TextView.ENABLED_SELECTED_STATE_SET, colorForState);
        this.mTransitedColor = colorForState2;
        if (this.mNormalColor != colorForState2) {
            this.hasTransitedColor = true;
        }
    }

    @Override // android.widget.TextView
    public void setTextColor(ColorStateList colorStateList) {
        super.setTextColor(colorStateList);
        setupColors();
    }

    @Override // android.widget.TextView, android.view.View
    public void setSelected(boolean z) {
        super.setSelected(false);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        ValueAnimator valueAnimator;
        if (!this.hasTransitedColor || (valueAnimator = this.mAnimator) == null || !valueAnimator.isRunning()) {
            super.onDraw(canvas);
        } else {
            setTextColor(this.mAnimateColor);
            super.onDraw(canvas);
        }
    }

    public void startColorTransition(final boolean z, boolean z2) {
        if (!z2) {
            if (z) {
                setTextColor(this.mTransitedColor);
            } else {
                setTextColor(this.mNormalColor);
            }
            invalidate();
            return;
        }
        ValueAnimator valueAnimator = this.mAnimator;
        if (valueAnimator == null) {
            this.mAnimator = new ValueAnimator();
        } else {
            valueAnimator.cancel();
        }
        ValueAnimator valueAnimator2 = this.mAnimator;
        if (z) {
            valueAnimator2.setIntValues(getCurrentTextColor(), this.mTransitedColor);
        } else {
            valueAnimator2.setIntValues(getCurrentTextColor(), this.mNormalColor);
        }
        this.mAnimator.setDuration(50L);
        this.mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        this.mAnimator.setEvaluator(new ArgbEvaluator());
        this.mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: miuix.appcompat.internal.view.ColorTransitionTextView.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator3) {
                ColorTransitionTextView.this.mAnimateColor = ((Integer) valueAnimator3.getAnimatedValue()).intValue();
                ColorTransitionTextView.this.invalidate();
            }
        });
        this.mAnimator.addListener(new AnimatorListenerAdapter() { // from class: miuix.appcompat.internal.view.ColorTransitionTextView.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                boolean z3 = z;
                ColorTransitionTextView colorTransitionTextView = ColorTransitionTextView.this;
                if (z3) {
                    colorTransitionTextView.setTextColor(colorTransitionTextView.mTransitedColor);
                } else {
                    colorTransitionTextView.setTextColor(colorTransitionTextView.mNormalColor);
                }
            }
        });
        this.mAnimator.start();
    }
}
