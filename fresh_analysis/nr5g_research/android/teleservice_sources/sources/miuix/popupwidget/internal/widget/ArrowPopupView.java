package miuix.popupwidget.internal.widget;

import android.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import miuix.internal.util.DeviceHelper;
import miuix.internal.util.ViewUtils;
import miuix.popupwidget.R$attr;
import miuix.popupwidget.R$color;
import miuix.popupwidget.R$dimen;
import miuix.popupwidget.R$id;
import miuix.popupwidget.R$style;
import miuix.popupwidget.R$styleable;
import miuix.popupwidget.widget.ArrowPopupWindow;
import miuix.view.CompatViewMethod;

/* JADX INFO: loaded from: classes2.dex */
public class ArrowPopupView extends FrameLayout implements View.OnTouchListener {
    private boolean mAlphaAnimationEnabled;
    private View mAnchor;
    private View.OnLayoutChangeListener mAnchorTrackListener;
    private AnimationSet mAnimationSet;
    private AnimatorSet mAnimator;
    private AppCompatImageView mArrow;
    private int mArrowBackgroundPaintColor;
    private Drawable mArrowBottom;
    private Drawable mArrowBottomLeft;
    private Drawable mArrowBottomRight;
    private final Runnable mArrowLayoutTask;
    private Drawable mArrowLeft;
    private int mArrowMode;
    private ArrowPopupWindow mArrowPopupWindow;
    private Drawable mArrowRight;
    private int mArrowSpaceLeft;
    private int mArrowSpaceTop;
    private Drawable mArrowTop;
    private Drawable mArrowTopLeft;
    private Drawable mArrowTopRight;
    private Drawable mArrowTopWithTitle;
    private boolean mAutoDismiss;
    private Drawable mBackground;
    private Drawable mBackgroundLeft;
    private Drawable mBackgroundRight;
    private FrameLayout mContentFrame;
    private ArrowPopupContentWrapper mContentFrameWrapper;
    private int mElevation;
    private boolean mEnableTrackAnchor;
    private boolean mHasFirstLayout;
    private Animation.AnimationListener mHideAnimatorListener;
    private boolean mIsDismissing;
    private int mMinBorder;
    private AppCompatButton mNegativeButton;
    private WrapperOnClickListener mNegativeClickListener;
    private int mOffsetX;
    private int mOffsetY;
    private AppCompatButton mPositiveButton;
    private WrapperOnClickListener mPositiveClickListener;
    private final View.OnLayoutChangeListener mRootTrackListener;
    private int mRtlMode;
    private int mShadowCorner;
    private final Paint mShadowPaint;
    private final RectF mShadowRect;
    private Animation.AnimationListener mShowAnimationListener;
    private boolean mShowingAnimation;
    private int mSpaceLeft;
    private int mSpaceTop;
    private Drawable mTitleBackground;
    private LinearLayout mTitleLayout;
    private AppCompatTextView mTitleText;
    private Rect mTmpRect;
    private RectF mTmpRectF;
    private View.OnTouchListener mTouchInterceptor;
    private View mTrackedRoot;
    private int mTranslationValue;

    public ArrowPopupView(Context context) {
        this(context, null);
    }

    public ArrowPopupView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.arrowPopupViewStyle);
    }

    public ArrowPopupView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mTmpRect = new Rect();
        this.mTmpRectF = new RectF();
        this.mAutoDismiss = true;
        this.mRtlMode = 2;
        this.mShowingAnimation = false;
        Paint paint = new Paint(1);
        this.mShadowPaint = paint;
        this.mShadowRect = new RectF();
        this.mShowAnimationListener = new Animation.AnimationListener() { // from class: miuix.popupwidget.internal.widget.ArrowPopupView.1
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                ArrowPopupView.this.mAnimationSet = null;
                if (ArrowPopupView.this.mShowingAnimation) {
                    ArrowPopupView.this.animateShowing();
                }
            }
        };
        this.mHideAnimatorListener = new Animation.AnimationListener() { // from class: miuix.popupwidget.internal.widget.ArrowPopupView.2
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                ArrowPopupView.this.mIsDismissing = false;
                ArrowPopupView.this.mAnimationSet = null;
                ArrowPopupView.this.mArrowPopupWindow.dismiss();
            }
        };
        this.mEnableTrackAnchor = true;
        this.mHasFirstLayout = false;
        this.mArrowLayoutTask = new Runnable() { // from class: miuix.popupwidget.internal.widget.ArrowPopupView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.arrowLayout();
            }
        };
        this.mAnchorTrackListener = new View.OnLayoutChangeListener() { // from class: miuix.popupwidget.internal.widget.ArrowPopupView.3
            @Override // android.view.View.OnLayoutChangeListener
            public void onLayoutChange(View view, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
                if (!ArrowPopupView.this.mHasFirstLayout || view == null) {
                    return;
                }
                view.post(ArrowPopupView.this.mArrowLayoutTask);
            }
        };
        this.mRootTrackListener = new View.OnLayoutChangeListener() { // from class: miuix.popupwidget.internal.widget.ArrowPopupView.4
            @Override // android.view.View.OnLayoutChangeListener
            public void onLayoutChange(View view, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
                if (ArrowPopupView.this.mHasFirstLayout) {
                    if ((i4 - i2 == i8 - i6 && i5 - i3 == i9 - i7) || ArrowPopupView.this.mArrowPopupWindow == null || !ArrowPopupView.this.mArrowPopupWindow.isShowing()) {
                        return;
                    }
                    ArrowPopupView.this.mArrowPopupWindow.dismiss();
                }
            }
        };
        this.mArrowMode = 0;
        this.mAlphaAnimationEnabled = true;
        CompatViewMethod.setForceDarkAllowed(this, false);
        this.mAutoDismiss = true;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ArrowPopupView, i, R$style.Widget_ArrowPopupView_DayNight);
        this.mArrowBackgroundPaintColor = typedArrayObtainStyledAttributes.getColor(R$styleable.ArrowPopupView_arrowBackgroundColor, getResources().getColor(R$color.miuix_appcompat_arrow_popup_background_color));
        this.mBackground = typedArrayObtainStyledAttributes.getDrawable(R$styleable.ArrowPopupView_contentBackground);
        this.mBackgroundLeft = typedArrayObtainStyledAttributes.getDrawable(R$styleable.ArrowPopupView_backgroundLeft);
        this.mBackgroundRight = typedArrayObtainStyledAttributes.getDrawable(R$styleable.ArrowPopupView_backgroundRight);
        this.mTitleBackground = typedArrayObtainStyledAttributes.getDrawable(R$styleable.ArrowPopupView_titleBackground);
        this.mArrowTop = typedArrayObtainStyledAttributes.getDrawable(R$styleable.ArrowPopupView_topArrow);
        this.mArrowTopWithTitle = typedArrayObtainStyledAttributes.getDrawable(R$styleable.ArrowPopupView_topArrowWithTitle);
        this.mArrowBottom = typedArrayObtainStyledAttributes.getDrawable(R$styleable.ArrowPopupView_bottomArrow);
        this.mArrowRight = typedArrayObtainStyledAttributes.getDrawable(R$styleable.ArrowPopupView_rightArrow);
        this.mArrowLeft = typedArrayObtainStyledAttributes.getDrawable(R$styleable.ArrowPopupView_leftArrow);
        this.mArrowTopLeft = typedArrayObtainStyledAttributes.getDrawable(R$styleable.ArrowPopupView_topLeftArrow);
        this.mArrowTopRight = typedArrayObtainStyledAttributes.getDrawable(R$styleable.ArrowPopupView_topRightArrow);
        this.mArrowBottomRight = typedArrayObtainStyledAttributes.getDrawable(R$styleable.ArrowPopupView_bottomRightArrow);
        this.mArrowBottomLeft = typedArrayObtainStyledAttributes.getDrawable(R$styleable.ArrowPopupView_bottomLeftArrow);
        this.mElevation = typedArrayObtainStyledAttributes.getDimensionPixelOffset(R$styleable.ArrowPopupView_android_elevation, getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_arrow_popup_window_elevation));
        typedArrayObtainStyledAttributes.recycle();
        this.mMinBorder = context.getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_arrow_popup_window_min_border);
        this.mShadowCorner = context.getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_arrow_popup_view_round_corners);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R$color.miuix_appcompat_drop_down_menu_spot_shadow_color));
        int i2 = this.mElevation;
        paint.setShadowLayer(i2, 0.0f, i2 / 2.0f, paint.getColor());
    }

    public int getPopupElevation() {
        return this.mElevation;
    }

    public void setAutoDismiss(boolean z) {
        this.mAutoDismiss = z;
    }

    public void setLayoutRtlMode(int i) {
        if (i <= 2 && i >= 0) {
            this.mRtlMode = i;
        } else {
            this.mRtlMode = 2;
        }
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mArrow = (AppCompatImageView) findViewById(R$id.popup_arrow);
        this.mContentFrame = (FrameLayout) findViewById(R.id.content);
        ArrowPopupContentWrapper arrowPopupContentWrapper = (ArrowPopupContentWrapper) findViewById(R$id.content_wrapper);
        this.mContentFrameWrapper = arrowPopupContentWrapper;
        arrowPopupContentWrapper.setArrowBackgroundPaintColor(this.mArrowBackgroundPaintColor);
        this.mContentFrameWrapper.setMinimumHeight(getContext().getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_arrow_popup_view_min_height));
        if (this.mBackgroundLeft != null && this.mBackgroundRight != null) {
            Rect rect = new Rect();
            this.mBackgroundLeft.getPadding(rect);
            ArrowPopupContentWrapper arrowPopupContentWrapper2 = this.mContentFrameWrapper;
            int i = rect.top;
            arrowPopupContentWrapper2.setPadding(i, i, i, i);
        }
        LinearLayout linearLayout = (LinearLayout) findViewById(R$id.title_layout);
        this.mTitleLayout = linearLayout;
        linearLayout.setBackground(this.mTitleBackground);
        this.mTitleText = (AppCompatTextView) findViewById(R.id.title);
        this.mPositiveButton = (AppCompatButton) findViewById(R.id.button2);
        this.mNegativeButton = (AppCompatButton) findViewById(R.id.button1);
        this.mPositiveClickListener = new WrapperOnClickListener();
        this.mNegativeClickListener = new WrapperOnClickListener();
        this.mPositiveButton.setOnClickListener(this.mPositiveClickListener);
        this.mNegativeButton.setOnClickListener(this.mNegativeClickListener);
    }

    public int getContentFrameWrapperTopPadding() {
        return this.mContentFrameWrapper.getPaddingTop();
    }

    public int getContentFrameWrapperBottomPadding() {
        return this.mContentFrameWrapper.getPaddingBottom();
    }

    public boolean isTitleEmpty() {
        return TextUtils.isEmpty(this.mTitleText.getText());
    }

    public int getTitleHeight() {
        if (this.mTitleLayout.getVisibility() != 8) {
            return this.mTitleLayout.getMeasuredHeight();
        }
        return 0;
    }

    public void setContentView(View view) {
        setContentView(view, new ViewGroup.LayoutParams(-2, -2));
    }

    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        this.mContentFrame.removeAllViews();
        if (view != null) {
            this.mContentFrame.addView(view, layoutParams);
        }
    }

    public View getContentView() {
        if (this.mContentFrame.getChildCount() > 0) {
            return this.mContentFrame.getChildAt(0);
        }
        return null;
    }

    private boolean isVerticalMode() {
        return isTopMode() || isBottomMode();
    }

    private boolean isCertainMode(int i) {
        return (this.mArrowMode & i) == i;
    }

    private boolean isLeftMode() {
        return isCertainMode(32);
    }

    private boolean isRightMode() {
        return isCertainMode(64);
    }

    private boolean isTopMode() {
        return isCertainMode(8);
    }

    private boolean isBottomMode() {
        return isCertainMode(16);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void arrowLayout() {
        int i = this.mRtlMode;
        int i2 = (i == 1 || (i == 2 && ViewUtils.isLayoutRtl(this))) ? -this.mOffsetX : this.mOffsetX;
        if (isVerticalMode()) {
            arrowVerticalLayout(i2);
        } else {
            arrowHorizontalLayout(i2);
        }
        View contentView = getContentView();
        if (contentView != null) {
            ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
            if (contentView.getMeasuredHeight() > this.mContentFrameWrapper.getHeight() - this.mTitleLayout.getHeight()) {
                layoutParams.height = this.mContentFrameWrapper.getHeight() - this.mTitleLayout.getHeight();
                contentView.setLayoutParams(layoutParams);
            } else if (contentView.getMeasuredWidth() > this.mContentFrameWrapper.getWidth()) {
                layoutParams.width = this.mContentFrameWrapper.getWidth();
                contentView.setLayoutParams(layoutParams);
            }
            if (layoutParams.height <= 0 || layoutParams.width <= 0) {
                Log.w("ArrowPopupView", "Invalid LayoutPrams of content view, please check the anchor view");
            }
        }
    }

    private void arrowVerticalLayout(int i) {
        int i2;
        int i3;
        int i4;
        int width = this.mAnchor.getWidth();
        int height = this.mAnchor.getHeight();
        int width2 = getWidth();
        int height2 = getHeight();
        int iMax = Math.max(this.mContentFrameWrapper.getMeasuredWidth(), this.mContentFrameWrapper.getMinimumWidth());
        int iMax2 = Math.max(this.mContentFrameWrapper.getMeasuredHeight(), this.mContentFrameWrapper.getMinimumHeight());
        float f = getResources().getDisplayMetrics().density;
        int arrowWidth = getArrowWidth(this.mArrowMode);
        int arrowHeight = getArrowHeight(this.mArrowMode);
        int[] iArr = new int[2];
        this.mAnchor.getLocationOnScreen(iArr);
        int i5 = iArr[0];
        int i6 = iArr[1];
        getLocationOnScreen(iArr);
        this.mArrowSpaceLeft = (i5 + ((width - arrowWidth) / 2)) - iArr[0];
        this.mSpaceTop = getTop() + this.mOffsetY;
        if (isBottomMode()) {
            this.mSpaceTop += ((i6 - iArr[1]) - iMax2) + (this.mContentFrameWrapper.getPaddingBottom() - arrowHeight);
        } else if (isTopMode()) {
            this.mSpaceTop += (((i6 + height) - iArr[1]) - this.mContentFrameWrapper.getPaddingTop()) + arrowHeight;
        }
        int i7 = iMax / 2;
        int i8 = iMax - i7;
        int i9 = this.mRtlMode;
        boolean z = i9 == 1 || (i9 == 2 && ViewUtils.isLayoutRtl(this));
        boolean z2 = (!z && this.mArrowMode == 9) || (!z && this.mArrowMode == 18) || ((z && this.mArrowMode == 10) || (z && this.mArrowMode == 17));
        if ((!z && this.mArrowMode == 10) || ((!z && this.mArrowMode == 17) || ((z && this.mArrowMode == 9) || (z && this.mArrowMode == 18)))) {
            this.mSpaceLeft = (int) Math.max(((((i5 + (width / 2)) - iMax) + (f * 28.0f)) + this.mContentFrameWrapper.getPaddingEnd()) - iArr[0], 0.0f);
            i2 = 0;
        } else if (z2) {
            int iMax3 = (int) Math.max((((i5 + (width / 2)) - (f * 28.0f)) - this.mContentFrameWrapper.getPaddingStart()) - iArr[0], 0.0f);
            this.mSpaceLeft = iMax3;
            int i10 = iMax3 + iMax;
            if (i10 > width2) {
                this.mSpaceLeft = iMax3 + (width2 - i10);
            }
            i2 = 0;
            this.mSpaceLeft = Math.max(this.mSpaceLeft, 0);
        } else {
            int i11 = (i5 + (width / 2)) - iArr[0];
            this.mSpaceLeft = i11;
            int i12 = width2 - i11;
            if (i11 >= i7 && i12 >= i8) {
                this.mSpaceLeft = i11 - i7;
            } else if (i12 < i8) {
                this.mSpaceLeft = width2 - iMax;
            } else {
                i2 = 0;
                if (i11 < i7) {
                    this.mSpaceLeft = 0;
                }
            }
            i2 = 0;
        }
        int i13 = this.mSpaceLeft + i;
        this.mSpaceLeft = i13;
        int i14 = this.mArrowSpaceLeft + i;
        this.mArrowSpaceLeft = i14;
        if (i14 < 0) {
            this.mArrowSpaceLeft = i2;
        } else if (i14 + arrowWidth > width2) {
            this.mArrowSpaceLeft = i14 - ((i14 + arrowWidth) - width2);
        }
        if (i13 + iMax > width2) {
            this.mSpaceLeft = width2 - iMax;
        }
        this.mContentFrameWrapper.setArrowMode(this.mArrowMode);
        this.mContentFrameWrapper.setRtlMode(this.mRtlMode);
        ViewGroup.LayoutParams layoutParams = this.mContentFrameWrapper.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            i4 = marginLayoutParams.leftMargin;
            i3 = marginLayoutParams.rightMargin;
        } else {
            i3 = 0;
            i4 = 0;
        }
        int iMax4 = Math.max(this.mSpaceLeft, i4);
        int iMax5 = Math.max(this.mSpaceTop, 0);
        int i15 = iMax4 + iMax;
        int iMin = Math.min(i15, width2 - i3);
        if (iMin < i15) {
            iMax4 = Math.max(i4, iMin - iMax);
            iMin = iMax4 + iMax;
        }
        int i16 = this.mArrowMode;
        if (i16 == 8 || i16 == 16) {
            this.mContentFrameWrapper.setArrowHorizonOffset(((this.mArrowSpaceLeft + (arrowWidth / 2.0f)) - iMax4) - (iMax / 2.0f));
        }
        this.mContentFrameWrapper.layout(iMax4, iMax5, iMin, Math.min(iMax2 + iMax5, height2));
    }

    private void arrowHorizontalLayout(int i) {
        int i2;
        int i3;
        int[] iArr = new int[2];
        this.mAnchor.getLocationOnScreen(iArr);
        int i4 = iArr[0];
        int i5 = iArr[1];
        getLocationOnScreen(iArr);
        int width = this.mAnchor.getWidth();
        int height = this.mAnchor.getHeight();
        int width2 = getWidth();
        int height2 = getHeight();
        int iMax = Math.max(this.mContentFrameWrapper.getMeasuredWidth(), this.mContentFrameWrapper.getMinimumWidth());
        int iMax2 = Math.max(this.mContentFrameWrapper.getMeasuredHeight(), this.mContentFrameWrapper.getMinimumHeight());
        int arrowWidth = getArrowWidth(this.mArrowMode);
        int arrowHeight = getArrowHeight(this.mArrowMode);
        int i6 = iArr[1];
        int i7 = ((height / 2) + i5) - i6;
        this.mSpaceTop = i7;
        int i8 = height2 - i7;
        this.mArrowSpaceTop = ((i5 + ((height - arrowHeight) / 2)) - i6) + ((this.mContentFrameWrapper.getPaddingTop() - this.mContentFrameWrapper.getPaddingBottom()) / 2);
        int i9 = iMax2 / 2;
        int i10 = iMax2 - i9;
        this.mSpaceLeft = getLeft() + i;
        if (isRightMode()) {
            int i11 = this.mRtlMode;
            if (i11 == 1 || (i11 == 2 && ViewUtils.isLayoutRtl(this))) {
                this.mSpaceLeft += (((i4 + width) - this.mContentFrameWrapper.getPaddingLeft()) + arrowWidth) - iArr[0];
                this.mContentFrameWrapper.getPaddingLeft();
            } else {
                this.mSpaceLeft += (((i4 - iMax) + this.mContentFrameWrapper.getPaddingRight()) - arrowWidth) - iArr[0];
            }
        } else if (isLeftMode()) {
            int i12 = this.mRtlMode;
            if (i12 == 1 || (i12 == 2 && ViewUtils.isLayoutRtl(this))) {
                this.mSpaceLeft += ((((i4 - iMax) + this.mContentFrameWrapper.getPaddingRight()) - arrowWidth) - iArr[0]) + 1;
            } else {
                this.mSpaceLeft += (((i4 + width) - this.mContentFrameWrapper.getPaddingLeft()) + arrowWidth) - iArr[0];
                this.mContentFrameWrapper.getPaddingLeft();
            }
        }
        int i13 = this.mSpaceTop;
        if (i13 >= i9 && i8 >= i10) {
            this.mSpaceTop = (i13 - i9) + this.mOffsetY;
        } else if (i8 < i10) {
            this.mSpaceTop = (height2 - iMax2) + this.mOffsetY;
        } else if (i13 < i9) {
            this.mSpaceTop = this.mOffsetY;
        }
        int i14 = this.mArrowSpaceTop + this.mOffsetY;
        this.mArrowSpaceTop = i14;
        if (i14 < 0) {
            this.mArrowSpaceTop = 0;
        } else if (i14 + arrowHeight > height2) {
            this.mArrowSpaceTop = i14 - ((i14 + arrowHeight) - height2);
        }
        this.mContentFrameWrapper.setArrowMode(this.mArrowMode);
        this.mContentFrameWrapper.setRtlMode(this.mRtlMode);
        ViewGroup.LayoutParams layoutParams = this.mContentFrameWrapper.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            i3 = marginLayoutParams.leftMargin;
            i2 = marginLayoutParams.rightMargin;
        } else {
            i2 = 0;
            i3 = 0;
        }
        int iMax3 = Math.max(this.mSpaceLeft, i3);
        int iMax4 = Math.max(this.mSpaceTop, 0);
        int i15 = iMax3 + iMax;
        int iMin = Math.min(i15, width2 - i2);
        int iMin2 = Math.min(iMax4 + iMax2, height2);
        if (iMin < i15) {
            iMax3 = Math.max(i3, iMin - iMax);
            iMin = iMax3 + iMax;
        }
        int i16 = this.mArrowMode;
        if (i16 == 32 || i16 == 64) {
            this.mContentFrameWrapper.setArrowVerticalOffset(((this.mArrowSpaceTop + (arrowHeight / 2.0f)) - iMax4) - (iMax2 / 2.0f));
        }
        this.mContentFrameWrapper.layout(iMax3, iMax4, iMin, iMin2);
    }

    public int getArrowWidth(int i) {
        if (i != 32 && i != 64) {
            switch (i) {
                case 8:
                case 9:
                case 10:
                    break;
                default:
                    switch (i) {
                        case 16:
                        case 17:
                        case 18:
                            break;
                        default:
                            return 0;
                    }
                    break;
            }
            return getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_arrow_popup_arrow_width_vertical);
        }
        return getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_arrow_popup_arrow_width_horizontal);
    }

    public int getArrowHeight(int i) {
        int i2 = this.mArrowMode;
        if (i2 != 32 && i2 != 64) {
            switch (i2) {
                case 8:
                case 9:
                case 10:
                    break;
                default:
                    switch (i2) {
                        case 16:
                        case 17:
                        case 18:
                            break;
                        default:
                            return 0;
                    }
                    break;
            }
            return getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_arrow_popup_arrow_height_vertical);
        }
        return getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_arrow_popup_arrow_height_horizontal);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (!this.mAnchor.isAttachedToWindow()) {
            if (this.mArrowPopupWindow.isShowing()) {
                this.mArrowPopupWindow.dismiss();
                return;
            }
            return;
        }
        if (this.mArrowMode == 0) {
            adjustArrowMode();
        }
        updateArrowDrawable(this.mArrowMode);
        arrowLayout();
        this.mHasFirstLayout = true;
        this.mShadowRect.set(this.mContentFrameWrapper.getLeft() + this.mContentFrameWrapper.getPaddingLeft(), this.mContentFrameWrapper.getTop() + this.mContentFrameWrapper.getPaddingTop(), this.mContentFrameWrapper.getRight() - this.mContentFrameWrapper.getPaddingRight(), this.mContentFrameWrapper.getBottom() - this.mContentFrameWrapper.getPaddingBottom());
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        if (!this.mShadowRect.isEmpty()) {
            RectF rectF = this.mShadowRect;
            int i = this.mShadowCorner;
            canvas.drawRoundRect(rectF, i, i, this.mShadowPaint);
        }
        super.dispatchDraw(canvas);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int right;
        int right2;
        float f;
        if (this.mBackground != null) {
            return;
        }
        int width = this.mSpaceLeft + (this.mContentFrameWrapper.getWidth() / 2);
        int height = this.mSpaceTop + (this.mContentFrameWrapper.getHeight() / 2);
        int i = this.mArrowMode;
        if (i == 8) {
            int measuredWidth = this.mArrowSpaceLeft + (this.mArrow.getMeasuredWidth() / 2);
            right = measuredWidth - this.mSpaceLeft;
            right2 = this.mContentFrameWrapper.getRight() - measuredWidth;
            f = 0.0f;
        } else if (i == 16) {
            int measuredWidth2 = this.mArrowSpaceLeft + (this.mArrow.getMeasuredWidth() / 2);
            right = this.mContentFrameWrapper.getRight() - measuredWidth2;
            right2 = measuredWidth2 - this.mSpaceLeft;
            f = 180.0f;
        } else if (i == 32) {
            int measuredHeight = this.mArrowSpaceTop + (this.mArrow.getMeasuredHeight() / 2);
            right = this.mContentFrameWrapper.getBottom() - measuredHeight;
            right2 = measuredHeight - this.mSpaceTop;
            f = -90.0f;
        } else if (i != 64) {
            f = 0.0f;
            right = 0;
            right2 = 0;
        } else {
            int measuredHeight2 = this.mArrowSpaceTop + (this.mArrow.getMeasuredHeight() / 2);
            right = measuredHeight2 - this.mSpaceTop;
            right2 = this.mContentFrameWrapper.getBottom() - measuredHeight2;
            f = 90.0f;
        }
        int iSave = canvas.save();
        canvas.rotate(f, width, height);
        int i2 = this.mArrowMode;
        if (i2 == 8 || i2 == 16) {
            canvas.translate(this.mSpaceLeft, this.mSpaceTop);
            this.mBackgroundLeft.setBounds(0, 0, right, this.mContentFrameWrapper.getHeight());
            boolean zIsTopMode = isTopMode();
            int i3 = this.mTranslationValue;
            canvas.translate(0.0f, zIsTopMode ? i3 : -i3);
            this.mBackgroundLeft.draw(canvas);
            canvas.translate(right, 0.0f);
            this.mBackgroundRight.setBounds(0, 0, right2, this.mContentFrameWrapper.getHeight());
            this.mBackgroundRight.draw(canvas);
        } else if (i2 == 32 || i2 == 64) {
            canvas.translate(width - (this.mContentFrameWrapper.getHeight() / 2), height - (this.mContentFrameWrapper.getWidth() / 2));
            this.mBackgroundLeft.setBounds(0, 0, right, this.mContentFrameWrapper.getWidth());
            boolean zIsLeftMode = isLeftMode();
            int i4 = this.mTranslationValue;
            canvas.translate(0.0f, zIsLeftMode ? i4 : -i4);
            this.mBackgroundLeft.draw(canvas);
            canvas.translate(right, 0.0f);
            this.mBackgroundRight.setBounds(0, 0, right2, this.mContentFrameWrapper.getWidth());
            this.mBackgroundRight.draw(canvas);
        }
        canvas.restoreToCount(iSave);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        final View contentView = getContentView();
        if (contentView != null) {
            contentView.post(new Runnable() { // from class: miuix.popupwidget.internal.widget.ArrowPopupView.7
                @Override // java.lang.Runnable
                public void run() {
                    contentView.requestLayout();
                    contentView.invalidate();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getAnimationPivot(float[] fArr) {
        float right;
        float bottom;
        int height;
        float f;
        float f2;
        float f3;
        int top;
        int top2 = this.mArrow.getTop();
        int bottom2 = this.mArrow.getBottom();
        int left = this.mArrow.getLeft();
        int right2 = this.mArrow.getRight();
        int i = this.mRtlMode;
        boolean z = i == 1 || (i == 2 && ViewUtils.isLayoutRtl(this));
        int i2 = this.mArrowMode;
        if (i2 == 32) {
            ArrowPopupContentWrapper arrowPopupContentWrapper = this.mContentFrameWrapper;
            right = z ? arrowPopupContentWrapper.getRight() : arrowPopupContentWrapper.getLeft();
            bottom = this.mContentFrameWrapper.getBottom();
            height = this.mContentFrameWrapper.getHeight();
        } else {
            if (i2 == 64) {
                ArrowPopupContentWrapper arrowPopupContentWrapper2 = this.mContentFrameWrapper;
                right = z ? arrowPopupContentWrapper2.getLeft() : arrowPopupContentWrapper2.getRight();
                bottom = this.mContentFrameWrapper.getBottom();
                height = this.mContentFrameWrapper.getHeight();
            } else {
                switch (i2) {
                    case 8:
                        right = this.mContentFrameWrapper.getRight() - (this.mContentFrameWrapper.getWidth() / 2.0f);
                        top = this.mContentFrameWrapper.getTop();
                        f = top;
                        float f4 = right;
                        f2 = f;
                        f3 = f4;
                        break;
                    case 9:
                        ArrowPopupContentWrapper arrowPopupContentWrapper3 = this.mContentFrameWrapper;
                        right = z ? arrowPopupContentWrapper3.getRight() : arrowPopupContentWrapper3.getLeft();
                        top = this.mContentFrameWrapper.getTop();
                        f = top;
                        float f5 = right;
                        f2 = f;
                        f3 = f5;
                        break;
                    case 10:
                        ArrowPopupContentWrapper arrowPopupContentWrapper4 = this.mContentFrameWrapper;
                        right = z ? arrowPopupContentWrapper4.getLeft() : arrowPopupContentWrapper4.getRight();
                        top = this.mContentFrameWrapper.getTop();
                        f = top;
                        float f6 = right;
                        f2 = f;
                        f3 = f6;
                        break;
                    default:
                        switch (i2) {
                            case 16:
                                right = this.mContentFrameWrapper.getRight() - (this.mContentFrameWrapper.getWidth() / 2.0f);
                                top = this.mContentFrameWrapper.getBottom();
                                f = top;
                                float f7 = right;
                                f2 = f;
                                f3 = f7;
                                break;
                            case 17:
                                ArrowPopupContentWrapper arrowPopupContentWrapper5 = this.mContentFrameWrapper;
                                right = z ? arrowPopupContentWrapper5.getLeft() : arrowPopupContentWrapper5.getRight();
                                top = this.mContentFrameWrapper.getBottom();
                                f = top;
                                float f8 = right;
                                f2 = f;
                                f3 = f8;
                                break;
                            case 18:
                                ArrowPopupContentWrapper arrowPopupContentWrapper6 = this.mContentFrameWrapper;
                                right = z ? arrowPopupContentWrapper6.getRight() : arrowPopupContentWrapper6.getLeft();
                                top = this.mContentFrameWrapper.getBottom();
                                f = top;
                                float f9 = right;
                                f2 = f;
                                f3 = f9;
                                break;
                            default:
                                f3 = (right2 + left) / 2;
                                f2 = (bottom2 + top2) / 2;
                                break;
                        }
                        break;
                }
            }
            fArr[0] = f3;
            fArr[1] = f2;
        }
        f = bottom - (height / 2.0f);
        float f10 = right;
        f2 = f;
        f3 = f10;
        fArr[0] = f3;
        fArr[1] = f2;
    }

    public void animateToShow() {
        invalidate();
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: miuix.popupwidget.internal.widget.ArrowPopupView.8
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                ArrowPopupView.this.getViewTreeObserver().removeOnPreDrawListener(this);
                if (ArrowPopupView.this.mAnimator != null) {
                    ArrowPopupView.this.mAnimator.cancel();
                }
                if (ArrowPopupView.this.mAnimationSet != null) {
                    ArrowPopupView.this.mAnimationSet.cancel();
                }
                ArrowPopupView.this.mAnimationSet = new AnimationSet(true);
                float[] fArr = new float[2];
                ArrowPopupView.this.getAnimationPivot(fArr);
                ScaleAnimation scaleAnimation = new ScaleAnimation(0.6f, 1.0f, 0.6f, 1.0f, 0, fArr[0], 0, fArr[1]);
                AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                if (!DeviceHelper.isFeatureWholeAnim()) {
                    ArrowPopupView.this.mAnimationSet.setDuration(0L);
                } else {
                    alphaAnimation.setDuration(100L);
                    scaleAnimation.setDuration(280L);
                }
                ArrowPopupView.this.mAnimationSet.addAnimation(scaleAnimation);
                if (ArrowPopupView.this.mAlphaAnimationEnabled) {
                    ArrowPopupView.this.mAnimationSet.addAnimation(alphaAnimation);
                }
                ArrowPopupView.this.mAnimationSet.setAnimationListener(ArrowPopupView.this.mShowAnimationListener);
                ArrowPopupView.this.mAnimationSet.setInterpolator(new DecelerateInterpolator(1.5f));
                ArrowPopupView arrowPopupView = ArrowPopupView.this;
                arrowPopupView.startAnimation(arrowPopupView.mAnimationSet);
                return true;
            }
        });
    }

    public void animateToDismiss() {
        if (this.mIsDismissing) {
            return;
        }
        AnimatorSet animatorSet = this.mAnimator;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        AnimationSet animationSet = this.mAnimationSet;
        if (animationSet != null) {
            animationSet.cancel();
        }
        this.mAnimationSet = new AnimationSet(true);
        float[] fArr = new float[2];
        getAnimationPivot(fArr);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.6f, 1.0f, 0.6f, 0, fArr[0], 0, fArr[1]);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        if (DeviceHelper.isFeatureWholeAnim()) {
            scaleAnimation.setDuration(150L);
            alphaAnimation.setDuration(150L);
        } else {
            this.mAnimationSet.setDuration(0L);
        }
        this.mAnimationSet.addAnimation(scaleAnimation);
        if (this.mAlphaAnimationEnabled) {
            this.mAnimationSet.addAnimation(alphaAnimation);
        }
        this.mAnimationSet.setAnimationListener(this.mHideAnimatorListener);
        this.mAnimationSet.setInterpolator(new AccelerateInterpolator(2.0f));
        startAnimation(this.mAnimationSet);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void animateShowing() {
        if (DeviceHelper.isFeatureWholeAnim()) {
            AnimationSet animationSet = this.mAnimationSet;
            if (animationSet != null) {
                animationSet.cancel();
            }
            AnimatorSet animatorSet = this.mAnimator;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.mAnimator = animatorSet2;
            animatorSet2.addListener(new AnimatorListenerAdapter() { // from class: miuix.popupwidget.internal.widget.ArrowPopupView.9
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ArrowPopupView.this.mArrowPopupWindow.dismiss();
                }
            });
            float f = getContext().getResources().getDisplayMetrics().density * 4.0f;
            Property property = View.TRANSLATION_Y;
            int i = this.mRtlMode;
            boolean z = i == 1 || (i == 2 && ViewUtils.isLayoutRtl(this));
            int i2 = this.mArrowMode;
            if (i2 == 16) {
                f = -f;
            } else if (i2 == 32) {
                if (z) {
                    f = -f;
                }
                property = View.TRANSLATION_X;
            } else if (i2 == 64) {
                if (!z) {
                    f = -f;
                }
                property = View.TRANSLATION_X;
            }
            ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(this.mContentFrameWrapper, (Property<ArrowPopupContentWrapper, Float>) property, 0.0f, f, 0.0f);
            objectAnimatorOfFloat.setInterpolator(new AccelerateDecelerateInterpolator());
            objectAnimatorOfFloat.setDuration(1200L);
            if (this.mAutoDismiss) {
                objectAnimatorOfFloat.setRepeatCount(8);
            } else {
                objectAnimatorOfFloat.setRepeatCount(-1);
            }
            objectAnimatorOfFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: miuix.popupwidget.internal.widget.ArrowPopupView.10
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ArrowPopupView.this.mTranslationValue = ((Float) valueAnimator.getAnimatedValue()).intValue();
                    int iAbs = Math.abs(ArrowPopupView.this.mTranslationValue);
                    ArrowPopupView arrowPopupView = ArrowPopupView.this;
                    arrowPopupView.invalidate(arrowPopupView.mContentFrameWrapper.getLeft() - iAbs, ArrowPopupView.this.mContentFrameWrapper.getTop() - iAbs, ArrowPopupView.this.mContentFrameWrapper.getRight() + iAbs, ArrowPopupView.this.mContentFrameWrapper.getBottom() + iAbs);
                }
            });
            ObjectAnimator objectAnimatorOfFloat2 = ObjectAnimator.ofFloat(this.mArrow, (Property<AppCompatImageView, Float>) property, 0.0f, f, 0.0f);
            objectAnimatorOfFloat2.setInterpolator(new AccelerateDecelerateInterpolator());
            objectAnimatorOfFloat2.setDuration(1200L);
            if (this.mAutoDismiss) {
                objectAnimatorOfFloat2.setRepeatCount(8);
            } else {
                objectAnimatorOfFloat2.setRepeatCount(-1);
            }
            this.mAnimator.playTogether(objectAnimatorOfFloat, objectAnimatorOfFloat2);
            this.mAnimator.start();
        }
    }

    private void adjustArrowMode() {
        int[] iArr = new int[2];
        this.mAnchor.getLocationInWindow(iArr);
        int width = getWidth();
        int height = getHeight();
        int measuredWidth = this.mContentFrameWrapper.getMeasuredWidth();
        int measuredHeight = this.mContentFrameWrapper.getMeasuredHeight();
        int height2 = this.mAnchor.getHeight();
        int width2 = this.mAnchor.getWidth();
        SparseIntArray sparseIntArray = new SparseIntArray(4);
        int i = 16;
        sparseIntArray.put(16, iArr[1] - measuredHeight);
        sparseIntArray.put(8, ((height - iArr[1]) - height2) - measuredHeight);
        sparseIntArray.put(64, iArr[0] - measuredWidth);
        sparseIntArray.put(32, ((width - iArr[0]) - width2) - measuredWidth);
        int i2 = Integer.MIN_VALUE;
        for (int i3 = 0; i3 < sparseIntArray.size(); i3++) {
            int iKeyAt = sparseIntArray.keyAt(i3);
            if (sparseIntArray.get(iKeyAt) >= this.mMinBorder) {
                i = iKeyAt;
                break;
            }
            if (sparseIntArray.get(iKeyAt) > i2) {
                i2 = sparseIntArray.get(iKeyAt);
                i = iKeyAt;
            }
        }
        setArrowMode(i);
    }

    public int getArrowMode() {
        return this.mArrowMode;
    }

    public void setArrowMode(int i) {
        this.mArrowMode = i;
        updateArrowDrawable(i);
    }

    private void updateArrowDrawable(int i) {
        int i2 = this.mRtlMode;
        boolean z = true;
        if (i2 != 1 && (i2 != 2 || !ViewUtils.isLayoutRtl(this))) {
            z = false;
        }
        if (i == 32) {
            this.mArrow.setImageDrawable(z ? this.mArrowRight : this.mArrowLeft);
            return;
        }
        if (i != 64) {
            switch (i) {
                case 8:
                    this.mArrow.setImageDrawable(this.mTitleLayout.getVisibility() == 0 ? this.mArrowTopWithTitle : this.mArrowTop);
                    break;
                case 9:
                    this.mArrow.setImageDrawable(z ? this.mArrowTopRight : this.mArrowTopLeft);
                    break;
                case 10:
                    this.mArrow.setImageDrawable(z ? this.mArrowTopLeft : this.mArrowTopRight);
                    break;
                default:
                    switch (i) {
                        case 16:
                            this.mArrow.setImageDrawable(this.mArrowBottom);
                            break;
                        case 17:
                            this.mArrow.setImageDrawable(z ? this.mArrowBottomLeft : this.mArrowBottomRight);
                            break;
                        case 18:
                            this.mArrow.setImageDrawable(z ? this.mArrowBottomRight : this.mArrowBottomLeft);
                            break;
                    }
                    break;
            }
            return;
        }
        this.mArrow.setImageDrawable(z ? this.mArrowLeft : this.mArrowRight);
    }

    public void setAnchor(View view) {
        View view2 = this.mAnchor;
        if (view2 != null) {
            view2.removeOnLayoutChangeListener(this.mAnchorTrackListener);
        }
        View view3 = this.mTrackedRoot;
        if (view3 != null) {
            view3.removeOnLayoutChangeListener(this.mRootTrackListener);
            this.mTrackedRoot = null;
        }
        this.mAnchor = view;
        this.mHasFirstLayout = false;
        if (!this.mEnableTrackAnchor || view == null) {
            return;
        }
        view.addOnLayoutChangeListener(this.mAnchorTrackListener);
        View rootView = this.mAnchor.getRootView();
        this.mTrackedRoot = rootView;
        if (rootView != null) {
            rootView.addOnLayoutChangeListener(this.mRootTrackListener);
        }
    }

    public void setOffset(int i, int i2) {
        this.mOffsetX = i;
        this.mOffsetY = i2;
    }

    public void setArrowPopupWindow(ArrowPopupWindow arrowPopupWindow) {
        this.mArrowPopupWindow = arrowPopupWindow;
    }

    public void setTouchInterceptor(View.OnTouchListener onTouchListener) {
        this.mTouchInterceptor = onTouchListener;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        View view = this.mAnchor;
        if (view != null) {
            view.removeOnLayoutChangeListener(this.mAnchorTrackListener);
            this.mAnchor.removeCallbacks(this.mArrowLayoutTask);
            this.mAnchor = null;
        }
        View view2 = this.mTrackedRoot;
        if (view2 != null) {
            view2.removeOnLayoutChangeListener(this.mRootTrackListener);
            this.mTrackedRoot = null;
        }
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        Rect rect = this.mTmpRect;
        this.mContentFrameWrapper.getHitRect(rect);
        if (motionEvent.getAction() == 0 && !rect.contains(x, y)) {
            this.mArrowPopupWindow.dismiss(true);
            return true;
        }
        View.OnTouchListener onTouchListener = this.mTouchInterceptor;
        return onTouchListener != null && onTouchListener.onTouch(view, motionEvent);
    }

    public void enableShowingAnimation(boolean z) {
        this.mShowingAnimation = z;
    }

    class WrapperOnClickListener implements View.OnClickListener {
        public View.OnClickListener mOnClickListener;

        WrapperOnClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            View.OnClickListener onClickListener = this.mOnClickListener;
            if (onClickListener != null) {
                onClickListener.onClick(view);
            }
            ArrowPopupView.this.mArrowPopupWindow.dismiss(true);
        }
    }
}
