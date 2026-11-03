package miuix.popupwidget.internal.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import miuix.popupwidget.R$attr;
import miuix.popupwidget.R$id;
import miuix.popupwidget.R$style;
import miuix.popupwidget.R$styleable;
import miuix.popupwidget.widget.GuidePopupWindow;

/* JADX INFO: loaded from: classes2.dex */
public class GuidePopupView extends FrameLayout implements View.OnTouchListener {
    private View mAnchor;
    private int mAnchorHeight;
    private int mAnchorLocationX;
    private int mAnchorLocationY;
    private int mAnchorWidth;
    private ObjectAnimator mAnimator;
    private int mArrowMode;
    private int mColorBackground;
    private Context mContext;
    private int mDefaultOffset;
    private GuidePopupWindow mGuidePopupWindow;
    private Animator.AnimatorListener mHideAnimatorListener;
    private boolean mIsDismissing;
    private boolean mIsMirrored;
    private float mLineLength;
    private int mMinBorder;
    private LinearLayout mMirroredTextGroup;
    private int mOffsetX;
    private int mOffsetY;
    private final Paint mPaint;
    private Animator.AnimatorListener mShowAnimatorListener;
    private float mStartPointRadius;
    private float mTextCircleRadius;
    private ColorStateList mTextColor;
    private LinearLayout mTextGroup;
    private int mTextSize;
    private boolean mUseDefaultOffset;

    public GuidePopupView(Context context) {
        this(context, null);
    }

    public GuidePopupView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.guidePopupViewStyle);
    }

    public GuidePopupView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mUseDefaultOffset = true;
        this.mTextColor = null;
        Paint paint = new Paint();
        this.mPaint = paint;
        this.mShowAnimatorListener = new AnimatorListenerAdapter() { // from class: miuix.popupwidget.internal.widget.GuidePopupView.1
            private boolean mCancel;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                this.mCancel = false;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                this.mCancel = true;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (this.mCancel) {
                    return;
                }
                GuidePopupView.this.mAnimator = null;
            }
        };
        this.mHideAnimatorListener = new AnimatorListenerAdapter() { // from class: miuix.popupwidget.internal.widget.GuidePopupView.2
            private boolean mCancel;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                this.mCancel = false;
                GuidePopupView.this.mIsDismissing = true;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                this.mCancel = true;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (this.mCancel) {
                    return;
                }
                GuidePopupView.this.mIsDismissing = false;
                GuidePopupView.this.mAnimator = null;
                GuidePopupView.this.mGuidePopupWindow.dismiss();
                GuidePopupView.this.setArrowMode(0);
            }
        };
        this.mArrowMode = -1;
        this.mContext = context;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.GuidePopupView, i, R$style.Widget_GuidePopupView_DayNight);
        this.mStartPointRadius = typedArrayObtainStyledAttributes.getDimension(R$styleable.GuidePopupView_startPointRadius, 0.0f);
        this.mLineLength = typedArrayObtainStyledAttributes.getDimension(R$styleable.GuidePopupView_lineLength, 0.0f);
        this.mTextCircleRadius = typedArrayObtainStyledAttributes.getDimension(R$styleable.GuidePopupView_textCircleRadius, 0.0f);
        this.mColorBackground = typedArrayObtainStyledAttributes.getColor(R$styleable.GuidePopupView_android_colorBackground, 0);
        paint.setColor(typedArrayObtainStyledAttributes.getColor(R$styleable.GuidePopupView_paintColor, -1));
        this.mTextSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(R$styleable.GuidePopupView_android_textSize, 15);
        this.mTextColor = typedArrayObtainStyledAttributes.getColorStateList(R$styleable.GuidePopupView_android_textColor);
        typedArrayObtainStyledAttributes.recycle();
        this.mMinBorder = (int) (this.mLineLength + (this.mTextCircleRadius * 2.5f));
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mTextGroup = (LinearLayout) findViewById(R$id.text_group);
        this.mMirroredTextGroup = (LinearLayout) findViewById(R$id.mirrored_text_group);
    }

    /* JADX WARN: Code duplicated, block: B:25:0x0052  */
    /* JADX WARN: Code duplicated, block: B:28:0x005b  */
    /* JADX WARN: Code duplicated, block: B:31:0x0063  */
    /* JADX WARN: Code duplicated, block: B:44:0x0083  */
    private void adjustArrowMode() {
        int i;
        int width = getWidth();
        int height = getHeight();
        int i2 = this.mAnchorLocationY;
        int i3 = this.mAnchorHeight;
        int i4 = this.mAnchorLocationX;
        int i5 = this.mAnchorWidth;
        int[] iArr = {i2, (height - i2) - i3, i4, (width - i4) - i5};
        int i6 = i4 + (i5 / 2);
        int i7 = i2 + (i3 / 2);
        int i8 = 0;
        int i9 = Integer.MIN_VALUE;
        int i10 = 0;
        while (true) {
            i = 4;
            if (i8 >= 4) {
                i8 = i10;
                break;
            }
            int i11 = iArr[i8];
            if (i11 >= this.mMinBorder) {
                break;
            }
            if (i11 > i9) {
                i10 = i8;
                i9 = i11;
            }
            i8++;
        }
        if (i8 == 0) {
            float f = i6;
            float f2 = this.mTextCircleRadius;
            if (f < f2) {
                i = 7;
            } else if (width - i6 < f2) {
                i = 5;
            } else {
                i = i8;
            }
        } else if (i8 != 1) {
            if (i8 == 2) {
                float f3 = i7;
                float f4 = this.mTextCircleRadius;
                if (f3 < f4) {
                    i = 6;
                } else if (height - i7 < f4) {
                    i = 5;
                }
            } else if (i8 == 3) {
                float f5 = i7;
                float f6 = this.mTextCircleRadius;
                if (f5 >= f6) {
                    if (height - i7 < f6) {
                        i = 7;
                    }
                }
            }
            i = i8;
        } else {
            float f7 = i6;
            float f8 = this.mTextCircleRadius;
            if (f7 >= f8) {
                if (width - i6 < f8) {
                    i = 6;
                } else {
                    i = i8;
                }
            }
        }
        setArrowMode(i);
    }

    private void arrowLayout() {
        caculateDefaultOffset();
        drawText(this.mArrowMode, this.mTextGroup, this.mOffsetX, this.mOffsetY);
        if (this.mIsMirrored) {
            drawText(getMirroredMode(), this.mMirroredTextGroup, -this.mOffsetX, -this.mOffsetY);
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.mAnchorWidth == 0 || this.mAnchorHeight == 0) {
            setAnchor(this.mAnchor);
        }
        this.mTextCircleRadius = (float) Math.max(Math.sqrt(Math.pow(this.mTextGroup.getMeasuredWidth(), 2.0d) + Math.pow(this.mTextGroup.getMeasuredHeight(), 2.0d)) / 2.0d, this.mTextCircleRadius);
        if (this.mIsMirrored) {
            this.mTextCircleRadius = (float) Math.max(Math.sqrt(Math.pow(this.mMirroredTextGroup.getMeasuredWidth(), 2.0d) + Math.pow(this.mMirroredTextGroup.getMeasuredHeight(), 2.0d)) / 2.0d, this.mTextCircleRadius);
        }
        if (this.mArrowMode == -1) {
            adjustArrowMode();
        } else {
            arrowLayout();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.save();
        canvas.translate(this.mAnchorLocationX, this.mAnchorLocationY);
        this.mAnchor.setDrawingCacheEnabled(true);
        this.mAnchor.buildDrawingCache();
        Bitmap drawingCache = this.mAnchor.getDrawingCache();
        if (drawingCache != null) {
            canvas.drawBitmap(drawingCache, 0.0f, 0.0f, (Paint) null);
        }
        this.mAnchor.setDrawingCacheEnabled(false);
        canvas.restore();
        drawPopup(canvas, this.mArrowMode, this.mOffsetX, this.mOffsetY);
        if (this.mIsMirrored) {
            drawPopup(canvas, getMirroredMode(), -this.mOffsetX, -this.mOffsetY);
        }
    }

    private void drawPopup(Canvas canvas, int i, int i2, int i3) {
        float f;
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Paint.Style.FILL);
        float f2 = this.mAnchorLocationX + (this.mAnchorWidth / 2) + i2;
        float f3 = this.mAnchorLocationY + (this.mAnchorHeight / 2) + i3;
        switch (i) {
            case 0:
                f = 180.0f;
                break;
            case 1:
            default:
                f = 0.0f;
                break;
            case 2:
                f = 90.0f;
                break;
            case 3:
                f = -90.0f;
                break;
            case 4:
                f = -45.0f;
                break;
            case 5:
                f = 135.0f;
                break;
            case 6:
                f = 45.0f;
                break;
            case 7:
                f = -135.0f;
                break;
        }
        canvas.save();
        canvas.rotate(f, f2, f3);
        canvas.translate(0.0f, this.mDefaultOffset);
        int iSave = canvas.save();
        canvas.clipRect(f2 - 2.0f, f3, f2 + 2.0f, f3 + this.mStartPointRadius, Region.Op.DIFFERENCE);
        canvas.drawCircle(f2, f3, this.mStartPointRadius, this.mPaint);
        canvas.restoreToCount(iSave);
        Paint paint = this.mPaint;
        Paint.Style style = Paint.Style.STROKE;
        paint.setStyle(style);
        this.mPaint.setStrokeWidth(4.0f);
        canvas.drawLine(f2, f3, f2, f3 + this.mLineLength, this.mPaint);
        float f4 = f3 + this.mLineLength + this.mTextCircleRadius;
        this.mPaint.setStyle(style);
        this.mPaint.setStrokeWidth(4.0f);
        canvas.drawCircle(f2, f4, this.mTextCircleRadius, this.mPaint);
        canvas.restore();
    }

    private void drawText(int i, LinearLayout linearLayout, int i2, int i3) {
        int measuredWidth;
        float f;
        int measuredHeight;
        int i4;
        int measuredHeight2;
        float f2 = this.mDefaultOffset + this.mLineLength + this.mTextCircleRadius;
        int i5 = this.mAnchorLocationX + (this.mAnchorWidth / 2);
        int i6 = this.mAnchorLocationY + (this.mAnchorHeight / 2);
        switch (i) {
            case 0:
            case 5:
            case 7:
                measuredWidth = i5 - (linearLayout.getMeasuredWidth() / 2);
                f = i6 - f2;
                measuredHeight = linearLayout.getMeasuredHeight() / 2;
                i4 = (int) (f - measuredHeight);
                break;
            case 1:
            case 4:
            case 6:
                measuredWidth = i5 - (linearLayout.getMeasuredWidth() / 2);
                f = i6 + f2;
                measuredHeight = linearLayout.getMeasuredHeight() / 2;
                i4 = (int) (f - measuredHeight);
                break;
            case 2:
                measuredWidth = (int) ((i5 - f2) - (linearLayout.getMeasuredWidth() / 2));
                measuredHeight2 = linearLayout.getMeasuredHeight() / 2;
                i4 = i6 - measuredHeight2;
                break;
            case 3:
                measuredWidth = (int) ((i5 + f2) - (linearLayout.getMeasuredWidth() / 2));
                measuredHeight2 = linearLayout.getMeasuredHeight() / 2;
                i4 = i6 - measuredHeight2;
                break;
            default:
                measuredWidth = 0;
                i4 = 0;
                break;
        }
        int iSin = (int) (((double) f2) * Math.sin(0.7853981633974483d));
        int i7 = (int) (f2 - iSin);
        if (i != 4) {
            if (i != 5) {
                if (i == 6) {
                    measuredWidth -= iSin;
                } else if (i == 7) {
                    measuredWidth += iSin;
                }
                int i8 = measuredWidth + i2;
                int i9 = i4 + i3;
                linearLayout.layout(i8, i9, linearLayout.getMeasuredWidth() + i8, linearLayout.getMeasuredHeight() + i9);
            }
            measuredWidth -= iSin;
            i4 += i7;
            int i10 = measuredWidth + i2;
            int i11 = i4 + i3;
            linearLayout.layout(i10, i11, linearLayout.getMeasuredWidth() + i10, linearLayout.getMeasuredHeight() + i11);
        }
        measuredWidth += iSin;
        i4 -= i7;
        int i12 = measuredWidth + i2;
        int i13 = i4 + i3;
        linearLayout.layout(i12, i13, linearLayout.getMeasuredWidth() + i12, linearLayout.getMeasuredHeight() + i13);
    }

    private void caculateDefaultOffset() {
        if (!this.mUseDefaultOffset) {
            this.mDefaultOffset = 0;
            return;
        }
        int i = this.mAnchorWidth / 2;
        int i2 = this.mAnchorHeight / 2;
        int iSqrt = (int) Math.sqrt(Math.pow(i, 2.0d) + Math.pow(i2, 2.0d));
        int i3 = this.mArrowMode;
        if (i3 == 0 || i3 == 1) {
            this.mDefaultOffset = i2;
        } else if (i3 == 2 || i3 == 3) {
            this.mDefaultOffset = i;
        } else {
            this.mDefaultOffset = iSqrt;
        }
    }

    private int getMirroredMode() {
        int i = this.mArrowMode;
        if (i == -1) {
            return -1;
        }
        return i % 2 == 0 ? i + 1 : i - 1;
    }

    public void setArrowMode(int i) {
        this.mArrowMode = i;
    }

    public void setAnchor(View view) {
        this.mAnchor = view;
        this.mAnchorWidth = view.getWidth();
        this.mAnchorHeight = this.mAnchor.getHeight();
        int[] iArr = new int[2];
        this.mAnchor.getLocationInWindow(iArr);
        this.mAnchorLocationX = iArr[0];
        this.mAnchorLocationY = iArr[1];
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        int i = this.mAnchorLocationX;
        Rect rect = new Rect(i, this.mAnchorLocationY, this.mAnchor.getWidth() + i, this.mAnchorLocationY + this.mAnchor.getHeight());
        if (motionEvent.getAction() == 0 && rect.contains(x, y)) {
            this.mAnchor.callOnClick();
            return true;
        }
        this.mGuidePopupWindow.dismiss(true);
        return true;
    }
}
