package miuix.miuixbasewidget.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator;
import miuix.animation.property.ViewProperty;
import miuix.internal.util.ViewUtils;
import miuix.miuixbasewidget.R$dimen;
import miuix.miuixbasewidget.R$style;
import miuix.miuixbasewidget.R$styleable;
import miuix.theme.token.BloomStrokeToken;
import miuix.theme.token.ColorBlendToken;
import miuix.theme.token.MaterialDayNightToken;
import miuix.theme.token.MaterialToken;
import miuix.theme.token.ShadowToken;

/* JADX INFO: loaded from: classes2.dex */
public class PageIndicator extends View {
    public static final MaterialToken Page_Indicator_Glass_Dark;
    public static final MaterialDayNightToken Page_Indicator_Glass_DayNight;
    public static final MaterialToken Page_Indicator_Glass_Light;
    private ViewProperty mBackgroundDrawableAlpha;
    private int mCurrentPosition;
    private float mCurrentPositionOffset;
    private final ArgbEvaluator mEvaluator;
    private int mHorizontalPadding;
    private int mIndicatorCount;
    private float mIndicatorGap;
    private float mIndicatorInterval;
    private Paint mIndicatorPaint;
    private float mIndicatorRadius;
    private boolean mIsRtl;
    private float mLargeSizeGap;
    private int mLargeSizeHorizontalPadding;
    private float mLargeSizeRadius;
    private float mLargeSizeVerticalPadding;
    private boolean mNeedBackground;
    private OnPageChangeListener mOnPageChangeListener;
    private boolean mPageScrolling;
    private int mSelectedColor;
    private int mSize;
    private float mSmallSizeGap;
    private int mSmallSizeHorizontalPadding;
    private float mSmallSizeRadius;
    private float mSmallSizeVerticalPadding;
    private int mUnselectedColor;
    private float mVerticalPadding;

    public interface OnPageChangeListener {
        void onPageSelected(int i);
    }

    static {
        MaterialToken materialTokenBuild = new MaterialToken.Builder(30, "page-indicator-glass", "light").setMaskBlur(20).setColorBlend(ColorBlendToken.Colored_Regular_Light).setBloomStroke(BloomStrokeToken.Glass_Stroke_Middle_Light).setShadow(ShadowToken.Regular).build();
        Page_Indicator_Glass_Light = materialTokenBuild;
        MaterialToken materialTokenBuild2 = new MaterialToken.Builder(30, "page-indicator-glass", "dark").setMaskBlur(20).setColorBlend(ColorBlendToken.Colored_Regular_Dark).setBloomStroke(BloomStrokeToken.Glass_Stroke_Middle_Dark).setShadow(ShadowToken.Regular).build();
        Page_Indicator_Glass_Dark = materialTokenBuild2;
        Page_Indicator_Glass_DayNight = new MaterialDayNightToken(materialTokenBuild, materialTokenBuild2);
    }

    public PageIndicator(Context context) {
        this(context, null);
    }

    public PageIndicator(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PageIndicator(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, R$style.Widget_PageIndicator_DayNight);
    }

    public PageIndicator(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mEvaluator = ArgbEvaluator.getInstance();
        this.mPageScrolling = false;
        this.mBackgroundDrawableAlpha = new ViewProperty("backgroundDrawableAlpha", 1.0f) { // from class: miuix.miuixbasewidget.widget.PageIndicator.1
            @Override // miuix.animation.property.FloatProperty
            public float getValue(View view) {
                Drawable background = view.getBackground();
                if (background != null) {
                    return background.getAlpha();
                }
                return 0.0f;
            }

            @Override // miuix.animation.property.FloatProperty
            public void setValue(View view, float f) {
                Drawable background;
                if (f < 0.0f || f > 255.0f || (background = view.getBackground()) == null) {
                    return;
                }
                background.setAlpha((int) f);
            }
        };
        this.mSmallSizeHorizontalPadding = context.getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_page_indicator_small_size_horizontal_padding);
        this.mLargeSizeHorizontalPadding = context.getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_page_indicator_large_size_horizontal_padding);
        this.mSmallSizeVerticalPadding = context.getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_page_indicator_small_size__vertical_padding);
        this.mLargeSizeVerticalPadding = context.getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_page_indicator_large_size__vertical_padding);
        this.mIndicatorPaint = createIndicatorPaint();
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.PageIndicator, i, i2);
        this.mIndicatorCount = typedArrayObtainStyledAttributes.getInt(R$styleable.PageIndicator_totalCount, 0);
        this.mNeedBackground = typedArrayObtainStyledAttributes.getBoolean(R$styleable.PageIndicator_needBackground, false);
        this.mSelectedColor = typedArrayObtainStyledAttributes.getColor(R$styleable.PageIndicator_selectedColor, 0);
        this.mUnselectedColor = typedArrayObtainStyledAttributes.getColor(R$styleable.PageIndicator_unselectedColor, 0);
        this.mSize = typedArrayObtainStyledAttributes.getInt(R$styleable.PageIndicator_sizeLevel, 0);
        this.mSmallSizeRadius = typedArrayObtainStyledAttributes.getDimension(R$styleable.PageIndicator_smallSizeRadius, 0.0f);
        this.mLargeSizeRadius = typedArrayObtainStyledAttributes.getDimension(R$styleable.PageIndicator_largeSizeRadius, 0.0f);
        this.mSmallSizeGap = typedArrayObtainStyledAttributes.getDimension(R$styleable.PageIndicator_smallSizeGap, 0.0f);
        this.mLargeSizeGap = typedArrayObtainStyledAttributes.getDimension(R$styleable.PageIndicator_largeSizeGap, 0.0f);
        typedArrayObtainStyledAttributes.recycle();
        setSize(this.mSize);
        if (getBackground() == null || this.mNeedBackground) {
            return;
        }
        getBackground().setAlpha(0);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        float f = (this.mIndicatorCount - 1) * this.mIndicatorInterval;
        float f2 = this.mIndicatorRadius;
        setMeasuredDimension((int) (f + (f2 * 2.0f) + (this.mHorizontalPadding * 2)), (int) ((f2 * 2.0f) + (this.mVerticalPadding * 2.0f)));
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int iIntValue;
        int iIntValue2;
        boolean zIsLayoutRtl = ViewUtils.isLayoutRtl(this);
        this.mIsRtl = zIsLayoutRtl;
        float f = this.mHorizontalPadding;
        float f2 = this.mIndicatorRadius;
        float f3 = f + f2;
        float f4 = this.mVerticalPadding + f2;
        int i = 0;
        if (!zIsLayoutRtl) {
            float f5 = f3;
            while (i < this.mIndicatorCount) {
                int i2 = this.mCurrentPosition;
                if (i == i2) {
                    iIntValue = ((Integer) this.mEvaluator.evaluate(this.mCurrentPositionOffset, Integer.valueOf(this.mSelectedColor), Integer.valueOf(this.mUnselectedColor))).intValue();
                } else if (i == i2 + 1) {
                    iIntValue = ((Integer) this.mEvaluator.evaluate(this.mCurrentPositionOffset, Integer.valueOf(this.mUnselectedColor), Integer.valueOf(this.mSelectedColor))).intValue();
                } else {
                    iIntValue = this.mUnselectedColor;
                }
                drawIndicator(canvas, f5, f4, this.mIndicatorRadius, iIntValue);
                f5 += this.mIndicatorInterval;
                i++;
            }
            return;
        }
        float f6 = f3;
        while (true) {
            int i3 = this.mIndicatorCount;
            if (i >= i3) {
                return;
            }
            int i4 = this.mCurrentPosition;
            if (i == (i3 - i4) - 1) {
                iIntValue2 = ((Integer) this.mEvaluator.evaluate(this.mCurrentPositionOffset, Integer.valueOf(this.mSelectedColor), Integer.valueOf(this.mUnselectedColor))).intValue();
            } else if (i == (i3 - i4) - 2) {
                iIntValue2 = ((Integer) this.mEvaluator.evaluate(this.mCurrentPositionOffset, Integer.valueOf(this.mUnselectedColor), Integer.valueOf(this.mSelectedColor))).intValue();
            } else {
                iIntValue2 = this.mUnselectedColor;
            }
            PageIndicator pageIndicator = this;
            pageIndicator.drawIndicator(canvas, f6, f4, this.mIndicatorRadius, iIntValue2);
            f6 += pageIndicator.mIndicatorInterval;
            i++;
            this = pageIndicator;
        }
    }

    /* JADX WARN: Code duplicated, block: B:12:0x0032  */
    /* JADX WARN: Code duplicated, block: B:16:0x003b  */
    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0 && !this.mPageScrolling) {
            float x = motionEvent.getX();
            int i = this.mCurrentPosition;
            boolean z = this.mIsRtl;
            int i2 = this.mHorizontalPadding;
            if (z) {
                float f = i2;
                int i3 = this.mIndicatorCount;
                float f2 = this.mIndicatorRadius;
                float f3 = f + (((i3 - i) - 1) * ((f2 * 2.0f) + this.mIndicatorGap));
                float f4 = (f2 * 2.0f) + f3;
                if (x < f3 && i < i3 - 1) {
                    i++;
                } else if (x > f4 && i > 0) {
                    i--;
                }
            } else {
                float f5 = this.mIndicatorRadius;
                float f6 = i2 + (i * ((f5 * 2.0f) + this.mIndicatorGap));
                float f7 = (f5 * 2.0f) + f6;
                if (x < f6 && i > 0) {
                    i--;
                } else if (x > f7 && i < this.mIndicatorCount - 1) {
                    i++;
                }
            }
            OnPageChangeListener onPageChangeListener = this.mOnPageChangeListener;
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageSelected(i);
                return true;
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    private void notifyUpdate() {
        if (this.mSize == 0) {
            this.mIndicatorRadius = this.mSmallSizeRadius;
            this.mVerticalPadding = this.mSmallSizeVerticalPadding;
            this.mHorizontalPadding = this.mSmallSizeHorizontalPadding;
            this.mIndicatorGap = this.mSmallSizeGap;
        } else {
            this.mIndicatorRadius = this.mLargeSizeRadius;
            this.mVerticalPadding = this.mLargeSizeVerticalPadding;
            this.mHorizontalPadding = this.mLargeSizeHorizontalPadding;
            this.mIndicatorGap = this.mLargeSizeGap;
        }
        this.mIndicatorInterval = (this.mIndicatorRadius * 2.0f) + this.mIndicatorGap;
        requestLayout();
    }

    public void setIndicatorCount(int i) {
        if (i >= 0) {
            this.mIndicatorCount = i;
            requestLayout();
        }
    }

    public void setSize(int i) {
        if (i == 0 || i == 1) {
            this.mSize = i;
            notifyUpdate();
        }
    }

    public void setCurrentPosition(int i) {
        if (this.mCurrentPosition != i) {
            this.mCurrentPosition = i;
            invalidate();
        }
    }

    public void setCurrentPositionOffset(float f) {
        if (this.mCurrentPositionOffset != f) {
            this.mCurrentPositionOffset = f;
            invalidate();
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.mOnPageChangeListener = onPageChangeListener;
    }

    private void drawIndicator(Canvas canvas, float f, float f2, float f3, int i) {
        this.mIndicatorPaint.setColor(i);
        canvas.drawCircle(f, f2, f3, this.mIndicatorPaint);
    }

    private Paint createIndicatorPaint() {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        return paint;
    }
}
