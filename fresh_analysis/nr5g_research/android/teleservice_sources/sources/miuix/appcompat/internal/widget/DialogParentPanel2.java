package miuix.appcompat.internal.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;
import miuix.appcompat.R$attr;
import miuix.appcompat.R$dimen;
import miuix.appcompat.R$styleable;
import miuix.appcompat.app.DialogContract;
import miuix.appcompat.app.strategy.IPanelMeasureRule;
import miuix.appcompat.app.strategy.PanelMeasureRuleImpl;
import miuix.core.util.EnvStateManager;
import miuix.core.util.WindowUtils;
import miuix.internal.util.AttributeResolver;
import miuix.smooth.SmoothCornerHelper;
import miuix.view.Fence;

/* JADX INFO: loaded from: classes2.dex */
public class DialogParentPanel2 extends LinearLayout implements Fence {
    private final Path mClipPath;
    private int mDensityDpi;
    private boolean mFenceEnabled;
    private final FloatingABOLayoutSpec mFloatingWindowSize;
    private final RectF mLayer;
    private int mPanelFixedHeight;
    private int mPanelFixedWidth;
    private float mRadius;

    public DialogParentPanel2(Context context) {
        this(context, null);
    }

    public DialogParentPanel2(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DialogParentPanel2(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mLayer = new RectF();
        this.mClipPath = new Path();
        this.mPanelFixedHeight = -1;
        this.mPanelFixedWidth = -1;
        this.mFenceEnabled = false;
        setSmoothCornerEnable(true);
        Resources resources = getResources();
        setCornerRadius(resources.getDimension(R$dimen.miuix_appcompat_dialog_bg_corner_radius));
        this.mDensityDpi = resources.getDisplayMetrics().densityDpi;
        FloatingABOLayoutSpec floatingABOLayoutSpec = new FloatingABOLayoutSpec(context, attributeSet);
        this.mFloatingWindowSize = floatingABOLayoutSpec;
        floatingABOLayoutSpec.mMeasureRule = new PanelMeasureRuleImpl();
        setOutlineProvider(new ViewOutlineProvider() { // from class: miuix.appcompat.internal.widget.DialogParentPanel2.1
            @Override // android.view.ViewOutlineProvider
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(new Rect(0, 0, view.getWidth(), view.getHeight()), DialogParentPanel2.this.mRadius);
            }
        });
    }

    public void setPanelMaxLimitHeight(int i) {
        this.mFloatingWindowSize.mPanelMaxLimitHeight = i;
    }

    public void setIsDebugEnabled(boolean z) {
        this.mFloatingWindowSize.mIsDebugEnabled = z;
    }

    public void setPanelFixedHeight(int i) {
        this.mPanelFixedHeight = i;
    }

    public void setPanelFixedWidth(int i) {
        this.mPanelFixedWidth = i;
    }

    public int getPanelMaxLimitHeight() {
        return this.mFloatingWindowSize.mPanelMaxLimitHeight;
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int i = configuration.densityDpi;
        if (i != this.mDensityDpi) {
            this.mDensityDpi = i;
            setCornerRadius(getResources().getDimension(R$dimen.miuix_appcompat_dialog_bg_corner_radius));
        }
        notifyConfigurationChanged();
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int widthMeasureSpecForDialog;
        int heightMeasureSpecForDialog;
        notifyConfigurationChanged();
        int i3 = this.mPanelFixedWidth;
        if (i3 > 0) {
            widthMeasureSpecForDialog = View.MeasureSpec.makeMeasureSpec(i3, 1073741824);
        } else {
            widthMeasureSpecForDialog = this.mFloatingWindowSize.getWidthMeasureSpecForDialog(i);
        }
        int i4 = this.mPanelFixedHeight;
        if (i4 > 0) {
            heightMeasureSpecForDialog = View.MeasureSpec.makeMeasureSpec(i4, 1073741824);
        } else {
            heightMeasureSpecForDialog = this.mFloatingWindowSize.getHeightMeasureSpecForDialog(i2);
        }
        super.onMeasure(widthMeasureSpecForDialog, heightMeasureSpecForDialog);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mLayer.set(0.0f, 0.0f, i, i2);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        int iSave = canvas.save();
        clipRoundRect(canvas);
        super.draw(canvas);
        canvas.restoreToCount(iSave);
    }

    public void setCornerRadius(float f) {
        if (f < 0.0f) {
            f = 0.0f;
        }
        this.mRadius = f;
        refresh();
    }

    private void refresh() {
        invalidateOutline();
        invalidate();
    }

    private void setSmoothCornerEnable(boolean z) {
        SmoothCornerHelper.setViewSmoothCornerEnable(this, z);
    }

    private void clipRoundRect(Canvas canvas) {
        this.mClipPath.reset();
        Path path = this.mClipPath;
        RectF rectF = this.mLayer;
        float f = this.mRadius;
        path.addRoundRect(rectF, f, f, Path.Direction.CW);
        canvas.clipPath(this.mClipPath);
    }

    public void setIsInTinyScreen(boolean z) {
        FloatingABOLayoutSpec floatingABOLayoutSpec = this.mFloatingWindowSize;
        if (floatingABOLayoutSpec != null) {
            floatingABOLayoutSpec.setIsInTinyScreen(z);
        }
    }

    public void notifyConfigurationChanged() {
        this.mFloatingWindowSize.flushWindowSizeIfNeed(this.mFloatingWindowSize.getScreenHeightDp());
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchConfigurationChanged(Configuration configuration) {
        super.dispatchConfigurationChanged(configuration);
    }

    @Override // miuix.view.Fence
    public boolean isFenceEnabled() {
        return this.mFenceEnabled;
    }

    class FloatingABOLayoutSpec {
        private final Context mContext;
        private TypedValue mFixedHeightMajor;
        private TypedValue mFixedHeightMinor;
        private TypedValue mFixedWidthMajor;
        private TypedValue mFixedWidthMinor;
        private TypedValue mFullHeightMajor;
        private boolean mIsDebugEnabled;
        private boolean mIsFlipTinyScreen;
        private boolean mIsFreeWindowMode;
        private TypedValue mMaxHeightMajor;
        private TypedValue mMaxHeightMinor;
        private TypedValue mMaxWidthMajor;
        private TypedValue mMaxWidthMinor;
        private IPanelMeasureRule mMeasureRule;
        private int mPanelMaxLimitHeight;
        private int mScreenHeightDp;
        private final Point mScreenSize = new Point();

        public FloatingABOLayoutSpec(Context context, AttributeSet attributeSet) {
            this.mContext = context;
            parseWindowSize(context, attributeSet);
            this.mScreenHeightDp = getScreenHeightDp();
            this.mIsFreeWindowMode = EnvStateManager.isFreeFormMode(context);
        }

        private void parseWindowSize(Context context, AttributeSet attributeSet) {
            if (attributeSet == null) {
                return;
            }
            TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.Window);
            if (typedArrayObtainStyledAttributes.hasValue(R$styleable.Window_windowFixedWidthMinor)) {
                TypedValue typedValue = new TypedValue();
                this.mFixedWidthMinor = typedValue;
                typedArrayObtainStyledAttributes.getValue(R$styleable.Window_windowFixedWidthMinor, typedValue);
            }
            if (typedArrayObtainStyledAttributes.hasValue(R$styleable.Window_windowFixedHeightMajor)) {
                TypedValue typedValue2 = new TypedValue();
                this.mFixedHeightMajor = typedValue2;
                typedArrayObtainStyledAttributes.getValue(R$styleable.Window_windowFixedHeightMajor, typedValue2);
            }
            if (typedArrayObtainStyledAttributes.hasValue(R$styleable.Window_windowFixedWidthMajor)) {
                TypedValue typedValue3 = new TypedValue();
                this.mFixedWidthMajor = typedValue3;
                typedArrayObtainStyledAttributes.getValue(R$styleable.Window_windowFixedWidthMajor, typedValue3);
            }
            if (typedArrayObtainStyledAttributes.hasValue(R$styleable.Window_windowFixedHeightMinor)) {
                TypedValue typedValue4 = new TypedValue();
                this.mFixedHeightMinor = typedValue4;
                typedArrayObtainStyledAttributes.getValue(R$styleable.Window_windowFixedHeightMinor, typedValue4);
            }
            if (typedArrayObtainStyledAttributes.hasValue(R$styleable.Window_windowMaxWidthMinor)) {
                TypedValue typedValue5 = new TypedValue();
                this.mMaxWidthMinor = typedValue5;
                typedArrayObtainStyledAttributes.getValue(R$styleable.Window_windowMaxWidthMinor, typedValue5);
            }
            if (typedArrayObtainStyledAttributes.hasValue(R$styleable.Window_windowMaxWidthMajor)) {
                TypedValue typedValue6 = new TypedValue();
                this.mMaxWidthMajor = typedValue6;
                typedArrayObtainStyledAttributes.getValue(R$styleable.Window_windowMaxWidthMajor, typedValue6);
            }
            if (typedArrayObtainStyledAttributes.hasValue(R$styleable.Window_windowMaxHeightMajor)) {
                TypedValue typedValue7 = new TypedValue();
                this.mMaxHeightMajor = typedValue7;
                typedArrayObtainStyledAttributes.getValue(R$styleable.Window_windowMaxHeightMajor, typedValue7);
            }
            if (typedArrayObtainStyledAttributes.hasValue(R$styleable.Window_windowMaxHeightMinor)) {
                TypedValue typedValue8 = new TypedValue();
                this.mMaxHeightMinor = typedValue8;
                typedArrayObtainStyledAttributes.getValue(R$styleable.Window_windowMaxHeightMinor, typedValue8);
            }
            if (typedArrayObtainStyledAttributes.hasValue(R$styleable.Window_windowFullHeightMajor)) {
                TypedValue typedValue9 = new TypedValue();
                this.mFullHeightMajor = typedValue9;
                typedArrayObtainStyledAttributes.getValue(R$styleable.Window_windowFullHeightMajor, typedValue9);
            }
            typedArrayObtainStyledAttributes.recycle();
        }

        private int[] getTypedBaseValue(TypedValue typedValue, TypedValue typedValue2, TypedValue typedValue3, TypedValue typedValue4, TypedValue typedValue5, boolean z) {
            boolean z2 = this.mIsFlipTinyScreen || this.mIsFreeWindowMode;
            return new int[]{resolveDimension(this.mMeasureRule.selectLimitValue(z2, isPortrait(), this.mScreenHeightDp, new DialogContract.ValueList(typedValue, typedValue2, typedValue5)), z), resolveDimension(this.mMeasureRule.selectLimitValue(z2, isPortrait(), this.mScreenHeightDp, new DialogContract.ValueList(typedValue3, typedValue4, typedValue5)), z)};
        }

        public int getWidthMeasureSpecForDialog(int i) {
            int[] typedBaseValue = getTypedBaseValue(this.mFixedWidthMinor, this.mFixedWidthMajor, this.mMaxWidthMinor, this.mMaxWidthMajor, this.mFullHeightMajor, true);
            int iMeasurePanelWidth = this.mMeasureRule.measurePanelWidth(i, typedBaseValue[0], typedBaseValue[1]);
            if (this.mIsDebugEnabled) {
                Log.d("DialogParentPanel2", "getWidthMeasureSpecForDialog: measuredValue = " + iMeasurePanelWidth + ", size = " + View.MeasureSpec.getSize(i) + ", fixedValue = " + typedBaseValue[0] + ", maxValue = " + typedBaseValue[1]);
            }
            return iMeasurePanelWidth;
        }

        public int getHeightMeasureSpecForDialog(int i) {
            boolean z = this.mIsFlipTinyScreen || this.mIsFreeWindowMode;
            int[] typedBaseValue = getTypedBaseValue(this.mFixedHeightMinor, this.mFixedHeightMajor, this.mMaxHeightMinor, this.mMaxHeightMajor, this.mFullHeightMajor, false);
            int iMeasurePanelHeight = this.mMeasureRule.measurePanelHeight(i, typedBaseValue[0], typedBaseValue[1], this.mPanelMaxLimitHeight, z);
            if (this.mIsDebugEnabled) {
                Log.d("DialogParentPanel2", "getHeightMeasureSpecForDialog: measuredValue = " + iMeasurePanelHeight + ", size = " + View.MeasureSpec.getSize(i) + ", fixedValue = " + typedBaseValue[0] + ", maxValue = " + typedBaseValue[1] + ", useMaxLimit = " + z + ", mPanelMaxLimitHeight = " + this.mPanelMaxLimitHeight + ", mIsFlipTinyScreen = " + this.mIsFlipTinyScreen + ", mIsFreeWindowMode = " + this.mIsFreeWindowMode);
            }
            return iMeasurePanelHeight;
        }

        private boolean isPortrait() {
            Point point = this.mScreenSize;
            int i = point.x;
            if (i == 0 && point.y == 0) {
                return WindowUtils.isPortrait(this.mContext);
            }
            return i < point.y;
        }

        private int resolveDimension(TypedValue typedValue, boolean z) {
            int i;
            float fraction;
            if (typedValue == null || (i = typedValue.type) == 0) {
                return 0;
            }
            if (i == 5) {
                fraction = typedValue.getDimension(this.mContext.getResources().getDisplayMetrics());
            } else {
                if (i != 6) {
                    return 0;
                }
                Point point = this.mScreenSize;
                float f = z ? point.x : point.y;
                fraction = typedValue.getFraction(f, f);
            }
            return (int) fraction;
        }

        public void setIsInTinyScreen(boolean z) {
            this.mIsFlipTinyScreen = z;
        }

        public void flushWindowSizeIfNeed(int i) {
            if (this.mScreenHeightDp != i) {
                this.mFixedWidthMinor = AttributeResolver.resolveTypedValue(this.mContext, R$attr.windowFixedWidthMinor);
                this.mFixedHeightMajor = AttributeResolver.resolveTypedValue(this.mContext, R$attr.windowFixedHeightMajor);
                this.mFixedWidthMajor = AttributeResolver.resolveTypedValue(this.mContext, R$attr.windowFixedWidthMajor);
                this.mFixedHeightMinor = AttributeResolver.resolveTypedValue(this.mContext, R$attr.windowFixedHeightMinor);
                this.mMaxWidthMinor = AttributeResolver.resolveTypedValue(this.mContext, R$attr.windowMaxWidthMinor);
                this.mMaxWidthMajor = AttributeResolver.resolveTypedValue(this.mContext, R$attr.windowMaxWidthMajor);
                this.mMaxHeightMinor = AttributeResolver.resolveTypedValue(this.mContext, R$attr.windowMaxHeightMinor);
                this.mFullHeightMajor = AttributeResolver.resolveTypedValue(this.mContext, R$attr.windowFullHeightMajor);
                this.mMaxHeightMajor = AttributeResolver.resolveTypedValue(this.mContext, R$attr.windowMaxHeightMajor);
                this.mScreenHeightDp = i;
            }
            this.mIsFreeWindowMode = EnvStateManager.isFreeFormMode(this.mContext);
        }

        public int getScreenHeightDp() {
            WindowUtils.getScreenSize(this.mContext, this.mScreenSize);
            return (int) (this.mScreenSize.y / this.mContext.getResources().getDisplayMetrics().density);
        }
    }
}
