package miuix.pickerwidget.internal.widget;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import miuix.internal.util.ViewUtils;
import miuix.pickerwidget.R$styleable;

/* JADX INFO: loaded from: classes2.dex */
public class ProperPaddingViewGroup extends ViewGroup {
    private final float DENSITY;
    private View mChildView;
    private boolean mFixedHorizontalPadding;
    private int mFixedHorizontalPaddingEnd;
    private int mFixedHorizontalPaddingStart;
    private final int mHorizontalPaddingEnd;
    private final int mHorizontalPaddingStart;
    private int mPaddingEnd;
    private int mPaddingStart;
    private final int mSmallHorizontalPaddingEnd;
    private final int mSmallHorizontalPaddingStart;

    public ProperPaddingViewGroup(Context context) {
        this(context, null);
    }

    public ProperPaddingViewGroup(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ProperPaddingViewGroup(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mFixedHorizontalPadding = false;
        this.mFixedHorizontalPaddingStart = Integer.MIN_VALUE;
        this.mFixedHorizontalPaddingEnd = Integer.MIN_VALUE;
        TypedArray typedArrayObtainStyledAttributes = null;
        this.mChildView = null;
        this.DENSITY = context.getResources().getDisplayMetrics().density;
        try {
            typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ProperPaddingViewGroup);
            int dimensionPixelSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(R$styleable.ProperPaddingViewGroup_horizontalPadding, 0);
            this.mHorizontalPaddingStart = typedArrayObtainStyledAttributes.getDimensionPixelSize(R$styleable.ProperPaddingViewGroup_horizontalPaddingStart, dimensionPixelSize);
            this.mHorizontalPaddingEnd = typedArrayObtainStyledAttributes.getDimensionPixelSize(R$styleable.ProperPaddingViewGroup_horizontalPaddingEnd, dimensionPixelSize);
            int dimensionPixelSize2 = typedArrayObtainStyledAttributes.getDimensionPixelSize(R$styleable.ProperPaddingViewGroup_smallHorizontalPadding, 0);
            this.mSmallHorizontalPaddingStart = typedArrayObtainStyledAttributes.getDimensionPixelSize(R$styleable.ProperPaddingViewGroup_smallHorizontalPaddingStart, dimensionPixelSize2);
            this.mSmallHorizontalPaddingEnd = typedArrayObtainStyledAttributes.getDimensionPixelSize(R$styleable.ProperPaddingViewGroup_smallHorizontalPaddingEnd, dimensionPixelSize2);
            typedArrayObtainStyledAttributes.recycle();
        } catch (Throwable th) {
            if (typedArrayObtainStyledAttributes != null) {
                typedArrayObtainStyledAttributes.recycle();
            }
            throw th;
        }
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 1) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("Only one child view can be added into the ProperPaddingViewGroup!");
        } else {
            this.mChildView = getChildAt(0);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int childMeasureSpec;
        int size = View.MeasureSpec.getSize(i);
        float f = size;
        float f2 = this.DENSITY;
        float f3 = f / f2;
        if (this.mFixedHorizontalPadding) {
            this.mPaddingStart = this.mFixedHorizontalPaddingStart;
            this.mPaddingEnd = this.mFixedHorizontalPaddingEnd;
        } else if (f3 <= 340.0f) {
            int i3 = ((int) (f - (f2 * 290.0f))) / 2;
            if (i3 < 0) {
                i3 = 0;
            }
            int i4 = i3 / 2;
            this.mPaddingStart = this.mSmallHorizontalPaddingStart + i4;
            this.mPaddingEnd = this.mSmallHorizontalPaddingEnd + i4;
        } else {
            this.mPaddingStart = this.mHorizontalPaddingStart;
            this.mPaddingEnd = this.mHorizontalPaddingEnd;
        }
        ViewGroup.LayoutParams layoutParams = this.mChildView.getLayoutParams();
        int iMax = Math.max(0, (size - this.mPaddingStart) - this.mPaddingEnd);
        int i5 = layoutParams.width;
        if (i5 == -1) {
            childMeasureSpec = View.MeasureSpec.makeMeasureSpec(iMax, 1073741824);
        } else {
            childMeasureSpec = ViewGroup.getChildMeasureSpec(i, this.mPaddingStart + this.mPaddingEnd, i5);
        }
        View view = this.mChildView;
        view.measure(childMeasureSpec, ViewGroup.getChildMeasureSpec(i2, 0, view.getLayoutParams().height));
        setMeasuredDimension(size, this.mChildView.getMeasuredHeight());
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        if (ViewUtils.isLayoutRtl(this)) {
            i5 = this.mPaddingEnd;
        } else {
            i5 = this.mPaddingStart;
        }
        this.mChildView.layout(i5, 0, this.mChildView.getMeasuredWidth() + i5, this.mChildView.getMeasuredHeight());
    }
}
