package miuix.appcompat.app;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import java.io.IOException;
import miuix.appcompat.R$styleable;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes2.dex */
public class ScrollBarAnimationDrawable extends Drawable {
    private ValueAnimator mAlphaAnimator;
    private float mAlphaTransition;
    private float mInsetsLeft;
    private float mInsetsRight;
    private int mLastAlpha;
    private Runnable mOnAlphaChangedRunnable;
    private int mOriginalAlpha;
    private float mRadius;
    private int mWidth;
    private final RectF mRect = new RectF();
    private final Paint mPaint = new Paint();

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public ScrollBarAnimationDrawable() {
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mAlphaAnimator = valueAnimatorOfFloat;
        valueAnimatorOfFloat.setDuration(250L);
        this.mAlphaAnimator.setInterpolator(new LinearInterpolator());
        this.mAlphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: miuix.appcompat.app.ScrollBarAnimationDrawable$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScrollBarAnimationDrawable.m5448$r8$lambda$db6n3P7p1zr3hbQv7w6gNMvfkU(this.f$0, valueAnimator);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$db6n3P7p1zr3hbQv7w6gNMvfk-U, reason: not valid java name */
    public static /* synthetic */ void m5448$r8$lambda$db6n3P7p1zr3hbQv7w6gNMvfkU(ScrollBarAnimationDrawable scrollBarAnimationDrawable, ValueAnimator valueAnimator) {
        scrollBarAnimationDrawable.getClass();
        scrollBarAnimationDrawable.mAlphaTransition = valueAnimator.getAnimatedFraction();
        Runnable runnable = scrollBarAnimationDrawable.mOnAlphaChangedRunnable;
        if (runnable != null) {
            runnable.run();
        }
        scrollBarAnimationDrawable.invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        RectF rectF = this.mRect;
        float f = this.mRadius;
        canvas.drawRoundRect(rectF, f, f, this.mPaint);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        if (i > this.mLastAlpha && this.mAlphaTransition < 1.0f) {
            this.mAlphaAnimator.start();
        }
        int iMax = Math.max((int) Math.min((i / 255.0f) * this.mOriginalAlpha * this.mAlphaTransition, 255.0f), 0);
        this.mPaint.setAlpha(iMax);
        if (this.mLastAlpha > i && iMax == 0) {
            this.mAlphaAnimator.cancel();
            this.mAlphaTransition = 0.0f;
        }
        this.mLastAlpha = i;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.mWidth;
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        this.mRect.set(rect);
        RectF rectF = this.mRect;
        rectF.left += this.mInsetsLeft;
        rectF.right += this.mInsetsRight;
    }

    @Override // android.graphics.drawable.Drawable
    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws XmlPullParserException, IOException {
        TypedArray typedArrayObtainAttributes;
        super.inflate(resources, xmlPullParser, attributeSet, theme);
        if (theme != null) {
            typedArrayObtainAttributes = theme.obtainStyledAttributes(attributeSet, R$styleable.ScrollBarAnimationDrawable, 0, 0);
        } else {
            typedArrayObtainAttributes = resources.obtainAttributes(attributeSet, R$styleable.ScrollBarAnimationDrawable);
        }
        this.mRadius = typedArrayObtainAttributes.getDimensionPixelSize(R$styleable.ScrollBarAnimationDrawable_android_radius, 0);
        int color = typedArrayObtainAttributes.getColor(R$styleable.ScrollBarAnimationDrawable_solidColor, 0);
        this.mOriginalAlpha = Color.alpha(color);
        this.mWidth = typedArrayObtainAttributes.getDimensionPixelSize(R$styleable.ScrollBarAnimationDrawable_android_width, 0);
        float f = (-resources.getDisplayMetrics().density) * 4.0f;
        this.mInsetsRight = f;
        this.mInsetsLeft = f;
        typedArrayObtainAttributes.recycle();
        this.mPaint.setColor(color);
    }
}
