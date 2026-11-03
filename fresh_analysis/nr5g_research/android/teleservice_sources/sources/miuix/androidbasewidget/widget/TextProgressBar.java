package miuix.androidbasewidget.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import miuix.androidbasewidget.R$dimen;
import miuix.androidbasewidget.R$drawable;
import miuix.androidbasewidget.R$id;
import miuix.androidbasewidget.R$layout;
import miuix.androidbasewidget.R$style;
import miuix.androidbasewidget.R$styleable;
import miuix.core.util.HyperBloomStrokeUtils;
import miuix.core.util.HyperMaterialUtils;
import miuix.core.util.MaterialConfig;
import miuix.core.util.MaterialDayNightConfig;
import miuix.core.util.MiShadowUtils;
import miuix.core.util.MiuiBlurUtils;
import miuix.internal.util.ViewUtils;
import miuix.theme.token.BloomStrokeToken;
import miuix.theme.token.ColorBlendToken;
import miuix.theme.token.MaterialDayNightToken;
import miuix.theme.token.MaterialToken;
import miuix.theme.token.ShadowToken;

/* JADX INFO: loaded from: classes2.dex */
public class TextProgressBar extends ViewGroup {
    public static final MaterialToken Text_ProgressBar_Glass_Dark;
    public static final MaterialToken Text_ProgressBar_Glass_Light;
    public static final MaterialDayNightToken Text_ProgressBar_Glass_Material;
    private boolean mMaterialEnabled;
    private int mMaxWidth;
    private int mMinHeight;
    private int mMinWidth;
    private android.widget.ProgressBar mProgressBar;
    private int mTextMarginTop;
    private TextView mTextView;

    static {
        MaterialToken materialTokenBuild = new MaterialToken.Builder(30, "text-progressBar-glass", "light").setColorBlend(ColorBlendToken.Pured_Extra_Thick_Light).setMaskBlur(60).setBloomStroke(BloomStrokeToken.Glass_Stroke_Middle_Light).setShadow(ShadowToken.Regular).build();
        Text_ProgressBar_Glass_Light = materialTokenBuild;
        MaterialToken materialTokenBuild2 = new MaterialToken.Builder(30, "text-progressBar-glass", "dark").setColorBlend(ColorBlendToken.Pured_Extra_Thick_Dark).setMaskBlur(60).setBloomStroke(BloomStrokeToken.Glass_Stroke_Middle_Dark).setShadow(ShadowToken.Regular).build();
        Text_ProgressBar_Glass_Dark = materialTokenBuild2;
        Text_ProgressBar_Glass_Material = new MaterialDayNightToken(materialTokenBuild, materialTokenBuild2);
    }

    public TextProgressBar(Context context) {
        this(context, null);
    }

    public TextProgressBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TextProgressBar(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, R$style.Widget_TextProgressBar);
    }

    public TextProgressBar(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context, attributeSet, i, i2);
    }

    private void init(Context context, AttributeSet attributeSet, int i, int i2) {
        ViewGroup.inflate(context, R$layout.miuix_appcompat_text_progressbar_child_bar, this);
        ViewGroup.inflate(context, R$layout.miuix_appcompat_text_progressbar_child_text, this);
        this.mProgressBar = (android.widget.ProgressBar) findViewById(R$id.progressbar);
        this.mTextView = (TextView) findViewById(R$id.progressbar_text);
        this.mTextMarginTop = getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_text_progressbar_text_margin_top);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.TextProgressBar, i, i2);
        String string = typedArrayObtainStyledAttributes.getString(R$styleable.TextProgressBar_android_text);
        boolean z = typedArrayObtainStyledAttributes.getBoolean(R$styleable.TextProgressBar_miShadowEnabled, false);
        boolean z2 = typedArrayObtainStyledAttributes.getBoolean(R$styleable.TextProgressBar_roundCornerBackgroundEnabled, false);
        int color = typedArrayObtainStyledAttributes.getColor(R$styleable.TextProgressBar_roundCornerBackgroundColor, Integer.MIN_VALUE);
        float dimension = typedArrayObtainStyledAttributes.getDimension(R$styleable.TextProgressBar_roundCornerBackgroundRadius, -2.1474836E9f);
        typedArrayObtainStyledAttributes.recycle();
        setText(string);
        setMiShadowEnabled(z);
        setRoundCornerBackgroundEnabled(z2);
        setRoundCornerBackgroundColor(color);
        setRoundCornerBackgroundRadius(dimension);
    }

    private void ensureConstrainedMeasureDimension() {
        if (this.mMaxWidth == 0 && this.mMinWidth == 0 && this.mMinHeight == 0) {
            this.mMaxWidth = getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_text_progressbar_max_width);
            this.mMinWidth = getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_text_progressbar_min_width);
            this.mMinHeight = getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_text_progressbar_min_height);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        ensureConstrainedMeasureDimension();
        measureChildren(i, i2);
        int measuredWidth = this.mTextView.getMeasuredWidth();
        int paddingLeft = getPaddingLeft() + getPaddingRight();
        int iMax = Math.max(this.mProgressBar.getMeasuredWidth(), measuredWidth) + paddingLeft;
        int iMeasureContentHeight = measureContentHeight();
        int iMax2 = Math.max(Math.min(iMax, this.mMaxWidth), this.mMinWidth);
        int iMax3 = Math.max(iMeasureContentHeight, this.mMinHeight);
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(iMax2, 1073741824);
        int iMakeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(iMax3, 1073741824);
        int i3 = this.mMaxWidth;
        if (measuredWidth > i3 - paddingLeft) {
            measureChild(this.mTextView, View.MeasureSpec.makeMeasureSpec(i3, 1073741824), i2);
            iMakeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(measureContentHeight(), 1073741824);
        }
        super.onMeasure(iMakeMeasureSpec, iMakeMeasureSpec2);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int measuredWidth2 = this.mProgressBar.getMeasuredWidth();
        int measuredHeight2 = this.mProgressBar.getMeasuredHeight();
        int measuredWidth3 = this.mTextView.getMeasuredWidth();
        int measuredHeight3 = this.mTextView.getMeasuredHeight();
        int i5 = (measuredWidth - measuredWidth2) / 2;
        int i6 = (((measuredHeight - measuredHeight2) - measuredHeight3) - this.mTextMarginTop) / 2;
        int i7 = measuredHeight2 + i6;
        this.mProgressBar.layout(i5, i6, measuredWidth2 + i5, i7);
        int i8 = (measuredWidth - measuredWidth3) / 2;
        int i9 = i7 + this.mTextMarginTop;
        this.mTextView.layout(i8, i9, measuredWidth3 + i8, measuredHeight3 + i9);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.mMaterialEnabled) {
            if (HyperMaterialUtils.isFeatureEnable(getContext())) {
                applyMaterialEffects();
            } else {
                clearMaterialEffects();
            }
        }
    }

    private int measureContentHeight() {
        return getPaddingTop() + getPaddingBottom() + this.mTextMarginTop + this.mProgressBar.getMeasuredHeight() + this.mTextView.getMeasuredHeight();
    }

    public TextView getTextView() {
        return this.mTextView;
    }

    public void setText(CharSequence charSequence) {
        getTextView().setText(charSequence);
    }

    public void setMiShadowEnabled(boolean z) {
        if (z) {
            ShadowToken shadowToken = ShadowToken.High;
            MiShadowUtils.setMiShadow(this, shadowToken.color, shadowToken.offsetX, shadowToken.offsetY, shadowToken.radius);
        } else {
            MiShadowUtils.clearMiShadow(this);
        }
    }

    public void setRoundCornerBackgroundEnabled(boolean z) {
        setBackground(z ? ContextCompat.getDrawable(getContext(), R$drawable.miuix_appcompat_text_progressbar_bg) : null);
    }

    public void setRoundCornerBackgroundColor(int i) {
        if (i == Integer.MIN_VALUE) {
            return;
        }
        Drawable background = getBackground();
        if (background instanceof GradientDrawable) {
            ((GradientDrawable) background.mutate()).setColor(i);
        }
    }

    public void setRoundCornerBackgroundRadius(float f) {
        if (f == -2.1474836E9f) {
            return;
        }
        Drawable background = getBackground();
        if (background instanceof GradientDrawable) {
            ((GradientDrawable) background.mutate()).setCornerRadius(f);
        }
    }

    private void applyMaterialEffects() {
        Drawable background = getBackground();
        if (background != null) {
            background.setAlpha(0);
        }
        boolean z = !ViewUtils.isNightMode(getContext());
        MaterialDayNightConfig materialDayNightConfigCreate = MaterialDayNightConfig.create(Text_ProgressBar_Glass_Material);
        MaterialConfig materialConfig = materialDayNightConfigCreate != null ? materialDayNightConfigCreate.get(z) : null;
        if (materialConfig != null) {
            applyBlurEffect(materialConfig, getResources().getDisplayMetrics().density);
            applyColorBlend(materialConfig);
            applyBloomStroke(materialConfig);
            applyShadow(materialConfig);
        }
    }

    private void applyBlurEffect(MaterialConfig materialConfig, float f) {
        int i;
        if (materialConfig.getBlurConfig() == null || (i = materialConfig.getBlurConfig().blurRadius) <= 0) {
            return;
        }
        MiuiBlurUtils.setBackgroundBlur(this, (int) ((i * f) + 0.5f));
    }

    private void applyColorBlend(MaterialConfig materialConfig) {
        MiuiBlurUtils.setViewBlurMode(this, 1);
        if (materialConfig.getColorBlendConfig() != null) {
            MiuiBlurUtils.setBackgroundBlendConfig(this, materialConfig.getColorBlendConfig().blendColors, materialConfig.getColorBlendConfig().blendModes);
        }
    }

    private void applyBloomStroke(MaterialConfig materialConfig) {
        if (materialConfig.getBloomStrokeConfig() != null) {
            HyperBloomStrokeUtils.setBloomStrokeConfig(this, materialConfig.getBloomStrokeConfig());
        }
    }

    private void applyShadow(MaterialConfig materialConfig) {
        if (materialConfig.getShadowConfig() != null) {
            MiShadowUtils.setShadowConfig(this, materialConfig.getShadowConfig());
        }
    }

    private void clearMaterialEffects() {
        MiuiBlurUtils.clearBackgroundBlur(this);
        MiuiBlurUtils.setViewBlurMode(this, 0);
        MiuiBlurUtils.clearBackgroundBlendConfig(this);
        HyperBloomStrokeUtils.clearBloomStroke(this);
        Drawable background = getBackground();
        if (background != null) {
            background.setAlpha(255);
        }
    }
}
