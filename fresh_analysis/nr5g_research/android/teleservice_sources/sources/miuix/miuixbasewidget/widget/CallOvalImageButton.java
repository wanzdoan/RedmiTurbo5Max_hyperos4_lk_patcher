package miuix.miuixbasewidget.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import miuix.animation.Folme;
import miuix.animation.FolmeEase;
import miuix.animation.IHoverStyle;
import miuix.animation.ITouchStyle;
import miuix.animation.base.AnimConfig;
import miuix.core.util.HyperBloomStrokeUtils;
import miuix.core.util.HyperMaterialUtils;
import miuix.core.util.MaterialConfig;
import miuix.core.util.MaterialDayNightConfig;
import miuix.core.util.MiShadowUtils;
import miuix.core.util.MiuiBlurUtils;
import miuix.internal.util.AttributeResolver;
import miuix.miuixbasewidget.R$attr;
import miuix.miuixbasewidget.R$color;
import miuix.miuixbasewidget.R$drawable;
import miuix.miuixbasewidget.R$style;
import miuix.miuixbasewidget.R$styleable;
import miuix.theme.token.MaterialDayNightToken;
import miuix.theme.token.MaterialToken;
import miuix.view.BlurableWidget;
import miuix.view.HapticCompat;
import miuix.view.HapticFeedbackConstants;
import miuix.view.ITransformableView;
import miuix.view.MiuiBlurUiHelper;

/* JADX INFO: loaded from: classes2.dex */
public final class CallOvalImageButton extends AppCompatImageView implements BlurableWidget, ITransformableView {
    private OvalDrawable mBackground;
    private final MiuiBlurUiHelper mBlurUiHelper;
    private MaterialConfig mCurrentMaterial;
    private Drawable mDefaultBackground;
    private AnimConfig mDownConfig;
    private final FabEmptyHolder mEmptyHolder;
    private final ITouchStyle mFolmeTouch;
    private boolean mHasOibColor;
    private final boolean mHasTouchColor;
    private int mImageAlpha;
    private final boolean mIsGlassEnabled;
    private boolean mIsLightStyle;
    private final boolean mIsShadowEnabled;
    private MaterialDayNightConfig mMaterial;
    private ColorStateList mOibColor;
    private BaseWidgetDropShadowHelper mShadowHelper;
    private final int mTouchColor;
    private AnimConfig mUpConfig;
    protected int mUserThemeType;

    @Override // miuix.view.ITransformableView
    public View getContainerView() {
        return this;
    }

    @Override // miuix.view.ITransformableView
    public View getContentView() {
        return this;
    }

    @Override // miuix.view.ITransformableView
    public float getCornerRadius() {
        return 100.0f;
    }

    public CallOvalImageButton(Context context) {
        this(context, null);
    }

    public CallOvalImageButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CallOvalImageButton(Context context, AttributeSet attributeSet, int i) {
        CallOvalImageButton callOvalImageButton;
        super(context, attributeSet, i);
        this.mImageAlpha = 255;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.OvalImageButton, i, R$style.Widget_OvalImageButton);
        try {
            this.mIsLightStyle = typedArrayObtainStyledAttributes.getBoolean(R$styleable.OvalImageButton_isLightTheme, true);
            this.mIsShadowEnabled = typedArrayObtainStyledAttributes.getBoolean(R$styleable.OvalImageButton_oibShadowEnabled, true);
            this.mIsGlassEnabled = typedArrayObtainStyledAttributes.getBoolean(R$styleable.OvalImageButton_oibGlassEnabled, false);
            ColorStateList colorStateList = typedArrayObtainStyledAttributes.getColorStateList(R$styleable.OvalImageButton_oibColor);
            this.mOibColor = colorStateList;
            this.mHasOibColor = colorStateList != null;
            boolean zHasValue = typedArrayObtainStyledAttributes.hasValue(R$styleable.OvalImageButton_oibTouchColor);
            int color = typedArrayObtainStyledAttributes.getColor(R$styleable.OvalImageButton_oibTouchColor, 0);
            boolean z = typedArrayObtainStyledAttributes.getBoolean(R$styleable.OvalImageButton_oibTouchScalable, true);
            typedArrayObtainStyledAttributes.recycle();
            this.mHasTouchColor = zHasValue;
            this.mTouchColor = color;
            this.mEmptyHolder = new FabEmptyHolder(this, getContext().getResources().getDrawable(R$drawable.miuix_appcompat_fab_empty_holder));
            initBackground();
            if (HyperMaterialUtils.isSupport()) {
                callOvalImageButton = this;
                MiuiBlurUiHelper miuiBlurUiHelper = new MiuiBlurUiHelper(context, callOvalImageButton, false, false, false, new MiuiBlurUiHelper.BlurStateCallback() { // from class: miuix.miuixbasewidget.widget.CallOvalImageButton.1
                    @Override // miuix.view.MiuiBlurUiHelper.BlurStateCallback
                    public Drawable getBackground() {
                        return null;
                    }

                    @Override // miuix.view.MiuiBlurUiHelper.BlurStateCallback
                    public void onBlurEnableStateChanged(boolean z2) {
                    }

                    @Override // miuix.view.MiuiBlurUiHelper.BlurStateCallback
                    public boolean isLightTheme() {
                        boolean zHasThemeType = CallOvalImageButton.this.hasThemeType();
                        CallOvalImageButton callOvalImageButton2 = CallOvalImageButton.this;
                        if (zHasThemeType) {
                            return callOvalImageButton2.mUserThemeType == 1;
                        }
                        return callOvalImageButton2.mIsLightStyle;
                    }

                    @Override // miuix.view.MiuiBlurUiHelper.BlurStateCallback
                    public MaterialConfig.BlurConfig getBlurConfig(boolean z2) {
                        if (CallOvalImageButton.this.mMaterial == null) {
                            return null;
                        }
                        MaterialConfig.BlurConfig blurConfig = CallOvalImageButton.this.mMaterial.getBlurConfig(z2);
                        MaterialConfig.ColorBlendConfig colorBlendConfig = CallOvalImageButton.this.mMaterial.getColorBlendConfig(z2);
                        return (blurConfig != null || colorBlendConfig == null) ? blurConfig : new MaterialConfig.BlurConfig(colorBlendConfig);
                    }

                    @Override // miuix.view.MiuiBlurUiHelper.BlurStateCallback
                    public void onBlurApplyStateChanged(boolean z2) {
                        Drawable background = CallOvalImageButton.this.getBackground();
                        if (background != null) {
                            background.setAlpha(z2 ? 0 : 255);
                        }
                    }
                });
                callOvalImageButton.mBlurUiHelper = miuiBlurUiHelper;
                miuiBlurUiHelper.setSupportBlur(true);
                callOvalImageButton.setEnableBlur(true);
            } else {
                callOvalImageButton = this;
                callOvalImageButton.mBlurUiHelper = null;
            }
            ITouchStyle iTouchStyle = Folme.use((View) callOvalImageButton).touch();
            callOvalImageButton.mFolmeTouch = iTouchStyle;
            callOvalImageButton.updateTouchTint();
            iTouchStyle.setNoScale(!z);
            iTouchStyle.setTintMode(3).handleTouchOf((View) callOvalImageButton, true, new AnimConfig[0]);
            Folme.use((View) callOvalImageButton).hover().setEffect(IHoverStyle.HoverEffect.FLOATED_WRAPPED).handleHoverOf(callOvalImageButton, new AnimConfig[0]);
            AnimConfig animConfig = new AnimConfig();
            callOvalImageButton.mDownConfig = animConfig;
            animConfig.setEase(FolmeEase.spring(1.0f, 0.2f));
            AnimConfig animConfig2 = new AnimConfig();
            callOvalImageButton.mUpConfig = animConfig2;
            animConfig2.setEase(FolmeEase.spring(0.95f, 0.35f));
            callOvalImageButton.initFolmeTouch();
        } catch (Throwable th) {
            typedArrayObtainStyledAttributes.recycle();
            throw th;
        }
    }

    private void updateTouchTint() {
        if (isGlassEffectActive()) {
            this.mFolmeTouch.clearTintColor();
        } else if (this.mHasTouchColor) {
            this.mFolmeTouch.setTint(this.mTouchColor);
        }
    }

    private void initFolmeTouch() {
        setOnTouchListener(new View.OnTouchListener() { // from class: miuix.miuixbasewidget.widget.CallOvalImageButton.2
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int actionMasked = motionEvent.getActionMasked();
                if (actionMasked == 0) {
                    CallOvalImageButton.this.mFolmeTouch.touchDown(CallOvalImageButton.this.mDownConfig);
                    return false;
                }
                if (actionMasked != 1 && actionMasked != 3) {
                    return false;
                }
                CallOvalImageButton.this.mFolmeTouch.touchUp(CallOvalImageButton.this.mUpConfig);
                return false;
            }
        });
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.mFolmeTouch.setTouchRadius(getMeasuredWidth() / 2.0f);
    }

    @Override // android.widget.ImageView, android.view.View
    public CharSequence getAccessibilityClassName() {
        return ImageButton.class.getName();
    }

    public boolean hasThemeType() {
        return this.mUserThemeType > 0;
    }

    public void setMaterial(MaterialToken materialToken) {
        setMaterial(MaterialDayNightConfig.create(new MaterialDayNightToken(materialToken)));
    }

    public void setMaterial(MaterialDayNightConfig materialDayNightConfig) {
        if (isSupportBlur()) {
            if (materialDayNightConfig == null) {
                this.mMaterial = null;
                clearMaterialEffect();
            } else {
                this.mMaterial = materialDayNightConfig;
                updateMaterialEffect();
            }
        }
    }

    public void updateMaterialEffect() {
        if (this.mMaterial == null) {
            return;
        }
        boolean zResolveIsLightTheme = resolveIsLightTheme();
        MaterialConfig materialConfig = this.mMaterial.get(zResolveIsLightTheme);
        this.mCurrentMaterial = materialConfig;
        if (materialConfig == null || !HyperMaterialUtils.isFeatureEnable(getContext())) {
            clearMaterialEffect();
        } else if (isGlassEffectActive()) {
            applyGlassEffect(zResolveIsLightTheme);
        } else {
            applyBlurEffect(zResolveIsLightTheme);
        }
        updateTouchTint();
    }

    private boolean resolveIsLightTheme() {
        if (hasThemeType()) {
            return this.mUserThemeType == 1;
        }
        return this.mIsLightStyle;
    }

    private boolean isGlassEffectActive() {
        return this.mIsGlassEnabled && HyperMaterialUtils.isSupportGlass() && HyperMaterialUtils.isEnableBionics(getContext());
    }

    private void applyGlassEffect(boolean z) {
        MaterialConfig materialConfig = this.mCurrentMaterial;
        if (materialConfig == null) {
            return;
        }
        applyContainerWithGlass(this, materialConfig.getGlassConfig());
        Drawable background = getBackground();
        if (background != null) {
            background.setAlpha(0);
        }
        processStroke(z);
    }

    private void applyContainerWithGlass(View view, MaterialConfig.GlassConfig glassConfig) {
        if (view == null || glassConfig == null) {
            return;
        }
        MiuiBlurUtils.setViewBlurMode(view, 1);
        MiuiBlurUtils.setMiViewMaterialType(view, 1);
        if (glassConfig.hasBlurRadius()) {
            MiuiBlurUtils.setMiGlassBlurRadius(view, glassConfig.smallBlurRadius, glassConfig.bigBlurRadius);
        }
        MiuiBlurUtils.setGlass(view, glassConfig.shaderParams);
    }

    public void setGlassShaderParams(float[] fArr) {
        if (fArr == null) {
            return;
        }
        MiuiBlurUtils.setGlass(this, fArr);
    }

    private void applyBlurEffect(boolean z) {
        setEnableBlur(true);
        if (this.mBlurUiHelper != null && this.mCurrentMaterial.getBlurConfig() != null) {
            this.mBlurUiHelper.onConfigChanged();
            if (!isApplyBlur()) {
                applyBlur(true);
            } else {
                this.mBlurUiHelper.refreshBlur();
            }
        }
        processStroke(z);
    }

    private void clearMaterialEffect() {
        applyBlur(false);
        setEnableBlur(false);
        HyperBloomStrokeUtils.clearBloomStroke(this);
        HyperMaterialUtils.clearContainer(this);
        Drawable background = getBackground();
        if (background != null) {
            background.setAlpha(this.mImageAlpha);
        }
        updateTouchTint();
    }

    private void processStroke(boolean z) {
        MaterialConfig materialConfig = this.mCurrentMaterial;
        if (materialConfig == null) {
            return;
        }
        MaterialConfig.BloomStrokeConfig bloomStrokeConfig = materialConfig.getBloomStrokeConfig();
        if (bloomStrokeConfig != null) {
            HyperBloomStrokeUtils.setBloomStrokeConfig(this, bloomStrokeConfig);
        } else {
            HyperBloomStrokeUtils.clearBloomStroke(this);
        }
        updateShadow(z);
    }

    public boolean isSupportBlur() {
        MiuiBlurUiHelper miuiBlurUiHelper = this.mBlurUiHelper;
        if (miuiBlurUiHelper == null) {
            return false;
        }
        return miuiBlurUiHelper.isSupportBlur();
    }

    @Override // miuix.view.BlurableWidget
    public void setEnableBlur(boolean z) {
        MiuiBlurUiHelper miuiBlurUiHelper = this.mBlurUiHelper;
        if (miuiBlurUiHelper != null) {
            miuiBlurUiHelper.setEnableBlur(z);
        }
    }

    @Override // miuix.view.BlurableWidget
    public void applyBlur(boolean z) {
        MiuiBlurUiHelper miuiBlurUiHelper = this.mBlurUiHelper;
        if (miuiBlurUiHelper != null) {
            miuiBlurUiHelper.applyBlur(z);
        }
    }

    public boolean isApplyBlur() {
        MiuiBlurUiHelper miuiBlurUiHelper = this.mBlurUiHelper;
        if (miuiBlurUiHelper == null) {
            return false;
        }
        return miuiBlurUiHelper.isApplyBlur();
    }

    private void updateShadow(boolean z) {
        MaterialConfig materialConfig = this.mCurrentMaterial;
        if (materialConfig == null) {
            return;
        }
        MaterialConfig.ShadowConfig shadowConfig = materialConfig.getShadowConfig();
        BaseWidgetDropShadowHelper baseWidgetDropShadowHelper = this.mShadowHelper;
        if (shadowConfig == null) {
            if (baseWidgetDropShadowHelper != null) {
                baseWidgetDropShadowHelper.enableViewShadow(this, false, 2);
            }
        } else if (baseWidgetDropShadowHelper == null) {
            this.mShadowHelper = new BaseWidgetDropShadowHelper(getContext(), new DropShadowConfig.Builder(this.mCurrentMaterial.getShadowConfig()).create(), z);
        } else {
            baseWidgetDropShadowHelper.updateDropShadowConfig(this.mCurrentMaterial.getShadowConfig());
            this.mShadowHelper.updateViewShadow(this, 2);
        }
    }

    private void initBackground() {
        if (getBackground() == null) {
            if (this.mHasOibColor) {
                super.setBackground(createOibBackground());
            } else {
                super.setBackground(getDefaultBackground());
            }
            Drawable background = getBackground();
            if (background != null) {
                background.setAlpha(this.mImageAlpha);
                return;
            }
            return;
        }
        this.mHasOibColor = false;
    }

    private void initEmptyHolder() {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int width = (((getWidth() - paddingLeft) - getPaddingRight()) / 2) * 2;
        this.mEmptyHolder.setBounds(paddingLeft, paddingTop, paddingLeft + width, width + paddingTop);
    }

    @Override // android.widget.ImageView
    public void setImageAlpha(int i) {
        boolean z = this.mImageAlpha != i;
        this.mImageAlpha = i;
        Drawable background = getBackground();
        if (background != null) {
            background.setAlpha(i);
        }
        Drawable drawable = getDrawable();
        if (drawable != null && z) {
            Drawable drawableMutate = drawable.mutate();
            drawableMutate.setAlpha(i);
            setImageDrawable(drawableMutate);
        }
        BaseWidgetDropShadowHelper baseWidgetDropShadowHelper = this.mShadowHelper;
        if (baseWidgetDropShadowHelper != null && baseWidgetDropShadowHelper.mEnableMiShadow) {
            baseWidgetDropShadowHelper.invalidateShadow(this, this.mImageAlpha / 255.0f);
        }
        invalidate();
    }

    @Override // android.widget.ImageView
    public int getImageAlpha() {
        return this.mImageAlpha;
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        BaseWidgetDropShadowHelper baseWidgetDropShadowHelper = this.mShadowHelper;
        if (baseWidgetDropShadowHelper != null) {
            baseWidgetDropShadowHelper.enableViewShadow(this, this.mIsShadowEnabled, 2);
        }
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        initEmptyHolder();
        super.onDraw(canvas);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateMaterialEffect();
    }

    @Override // android.view.View
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (z && HyperMaterialUtils.isSupport()) {
            HyperMaterialUtils.clearFeatureEnable();
            HyperMaterialUtils.isFeatureEnableNoCache(getContext());
            HyperMaterialUtils.getMaterialType(getContext());
            updateMaterialEffect();
        }
    }

    private Drawable getDefaultBackground() {
        if (this.mDefaultBackground == null) {
            this.mOibColor = ContextCompat.getColorStateList(getContext(), R$color.miuix_color_black_level6);
            this.mHasOibColor = true;
            this.mDefaultBackground = createOibBackground();
        }
        return this.mDefaultBackground;
    }

    private Drawable createOibBackground() {
        OvalDrawable ovalDrawable = new OvalDrawable(this.mOibColor);
        if (this.mIsShadowEnabled) {
            boolean zResolveBoolean = AttributeResolver.resolveBoolean(getContext(), R$attr.isLightTheme, true);
            BaseWidgetDropShadowHelper baseWidgetDropShadowHelper = this.mShadowHelper;
            if (baseWidgetDropShadowHelper == null) {
                updateShadow(zResolveBoolean);
            } else {
                baseWidgetDropShadowHelper.onConfigChanged(this, getResources().getConfiguration(), zResolveBoolean);
            }
        } else {
            BaseWidgetDropShadowHelper baseWidgetDropShadowHelper2 = this.mShadowHelper;
            if (baseWidgetDropShadowHelper2 != null) {
                baseWidgetDropShadowHelper2.enableViewShadow(this, false, 2);
            }
            this.mShadowHelper = null;
        }
        this.mBackground = ovalDrawable;
        return ovalDrawable;
    }

    @Override // android.view.View
    public void setBackground(Drawable drawable) {
        this.mHasOibColor = false;
        if (drawable == null) {
            drawable = getDefaultBackground();
        }
        super.setBackground(drawable);
    }

    @Override // androidx.appcompat.widget.AppCompatImageView, android.view.View
    public void setBackgroundResource(int i) {
        this.mHasOibColor = false;
        if (i == 0) {
            super.setBackground(getDefaultBackground());
        } else {
            super.setBackgroundResource(i);
        }
    }

    @Override // android.view.View
    public void setBackgroundColor(int i) {
        this.mOibColor = ColorStateList.valueOf(i);
        super.setBackground(createOibBackground());
        this.mHasOibColor = true;
    }

    @Override // android.view.View
    public boolean performClick() {
        HapticCompat.performHapticFeedback(this, HapticFeedbackConstants.MIUI_BUTTON_MIDDLE, HapticFeedbackConstants.MIUI_TAP_LIGHT);
        return super.performClick();
    }

    @Override // miuix.view.ITransformableView
    public void clearAppearance() {
        setScaleX(1.0f);
        setScaleY(1.0f);
    }

    @Override // miuix.view.ITransformableView
    public void applyTransformElevation(View view) {
        if (MiShadowUtils.SUPPORT_MI_SHADOW) {
            MiShadowUtils.clearMiShadow(view);
        } else {
            view.setElevation(0.0f);
        }
    }
}
