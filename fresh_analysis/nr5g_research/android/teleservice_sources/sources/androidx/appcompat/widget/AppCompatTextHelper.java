package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.fonts.FontVariationAxis;
import android.os.LocaleList;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.TextView;
import androidx.appcompat.R$styleable;
import androidx.collection.LruCache;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Consumer;
import androidx.core.util.TypedValueCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TextViewCompat;
import java.lang.ref.WeakReference;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
class AppCompatTextHelper {
    private boolean mAsyncFontPending;
    private final AppCompatTextViewAutoSizeHelper mAutoSizeTextHelper;
    private TintInfo mDrawableBottomTint;
    private TintInfo mDrawableEndTint;
    private TintInfo mDrawableLeftTint;
    private TintInfo mDrawableRightTint;
    private TintInfo mDrawableStartTint;
    private TintInfo mDrawableTopTint;
    private Typeface mFontTypeface;
    private final TextView mView;
    private int mStyle = 0;
    private int mFontWeight = -1;
    private String mFontVariationSettings = null;

    void populateSurroundingTextIfNeeded(TextView textView, InputConnection inputConnection, EditorInfo editorInfo) {
    }

    AppCompatTextHelper(TextView textView) {
        this.mView = textView;
        this.mAutoSizeTextHelper = new AppCompatTextViewAutoSizeHelper(textView);
    }

    void loadFromAttributes(AttributeSet attributeSet, int i) {
        String string;
        boolean z;
        boolean z2;
        int unitFromComplexDimension;
        float dimensionPixelSize;
        Context context = this.mView.getContext();
        AppCompatDrawableManager appCompatDrawableManager = AppCompatDrawableManager.get();
        int[] iArr = R$styleable.AppCompatTextHelper;
        TintTypedArray tintTypedArrayObtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, iArr, i, 0);
        TextView textView = this.mView;
        ViewCompat.saveAttributeDataForStyleable(textView, textView.getContext(), iArr, attributeSet, tintTypedArrayObtainStyledAttributes.getWrappedTypeArray(), i, 0);
        int resourceId = tintTypedArrayObtainStyledAttributes.getResourceId(R$styleable.AppCompatTextHelper_android_textAppearance, -1);
        if (tintTypedArrayObtainStyledAttributes.hasValue(R$styleable.AppCompatTextHelper_android_drawableLeft)) {
            this.mDrawableLeftTint = createTintInfo(context, appCompatDrawableManager, tintTypedArrayObtainStyledAttributes.getResourceId(R$styleable.AppCompatTextHelper_android_drawableLeft, 0));
        }
        if (tintTypedArrayObtainStyledAttributes.hasValue(R$styleable.AppCompatTextHelper_android_drawableTop)) {
            this.mDrawableTopTint = createTintInfo(context, appCompatDrawableManager, tintTypedArrayObtainStyledAttributes.getResourceId(R$styleable.AppCompatTextHelper_android_drawableTop, 0));
        }
        if (tintTypedArrayObtainStyledAttributes.hasValue(R$styleable.AppCompatTextHelper_android_drawableRight)) {
            this.mDrawableRightTint = createTintInfo(context, appCompatDrawableManager, tintTypedArrayObtainStyledAttributes.getResourceId(R$styleable.AppCompatTextHelper_android_drawableRight, 0));
        }
        if (tintTypedArrayObtainStyledAttributes.hasValue(R$styleable.AppCompatTextHelper_android_drawableBottom)) {
            this.mDrawableBottomTint = createTintInfo(context, appCompatDrawableManager, tintTypedArrayObtainStyledAttributes.getResourceId(R$styleable.AppCompatTextHelper_android_drawableBottom, 0));
        }
        if (tintTypedArrayObtainStyledAttributes.hasValue(R$styleable.AppCompatTextHelper_android_drawableStart)) {
            this.mDrawableStartTint = createTintInfo(context, appCompatDrawableManager, tintTypedArrayObtainStyledAttributes.getResourceId(R$styleable.AppCompatTextHelper_android_drawableStart, 0));
        }
        if (tintTypedArrayObtainStyledAttributes.hasValue(R$styleable.AppCompatTextHelper_android_drawableEnd)) {
            this.mDrawableEndTint = createTintInfo(context, appCompatDrawableManager, tintTypedArrayObtainStyledAttributes.getResourceId(R$styleable.AppCompatTextHelper_android_drawableEnd, 0));
        }
        tintTypedArrayObtainStyledAttributes.recycle();
        boolean z3 = this.mView.getTransformationMethod() instanceof PasswordTransformationMethod;
        boolean z4 = true;
        if (resourceId != -1) {
            TintTypedArray tintTypedArrayObtainStyledAttributes2 = TintTypedArray.obtainStyledAttributes(context, resourceId, R$styleable.TextAppearance);
            if (z3 || !tintTypedArrayObtainStyledAttributes2.hasValue(R$styleable.TextAppearance_textAllCaps)) {
                z = false;
                z2 = false;
            } else {
                z = tintTypedArrayObtainStyledAttributes2.getBoolean(R$styleable.TextAppearance_textAllCaps, false);
                z2 = true;
            }
            updateTypefaceAndStyle(context, tintTypedArrayObtainStyledAttributes2);
            string = tintTypedArrayObtainStyledAttributes2.hasValue(R$styleable.TextAppearance_textLocale) ? tintTypedArrayObtainStyledAttributes2.getString(R$styleable.TextAppearance_textLocale) : null;
            tintTypedArrayObtainStyledAttributes2.recycle();
        } else {
            string = null;
            z = false;
            z2 = false;
        }
        TintTypedArray tintTypedArrayObtainStyledAttributes3 = TintTypedArray.obtainStyledAttributes(context, attributeSet, R$styleable.TextAppearance, i, 0);
        if (z3 || !tintTypedArrayObtainStyledAttributes3.hasValue(R$styleable.TextAppearance_textAllCaps)) {
            z4 = z2;
        } else {
            z = tintTypedArrayObtainStyledAttributes3.getBoolean(R$styleable.TextAppearance_textAllCaps, false);
        }
        if (tintTypedArrayObtainStyledAttributes3.hasValue(R$styleable.TextAppearance_textLocale)) {
            string = tintTypedArrayObtainStyledAttributes3.getString(R$styleable.TextAppearance_textLocale);
        }
        if (tintTypedArrayObtainStyledAttributes3.hasValue(R$styleable.TextAppearance_android_textSize) && tintTypedArrayObtainStyledAttributes3.getDimensionPixelSize(R$styleable.TextAppearance_android_textSize, -1) == 0) {
            this.mView.setTextSize(0, 0.0f);
        }
        updateTypefaceAndStyle(context, tintTypedArrayObtainStyledAttributes3);
        tintTypedArrayObtainStyledAttributes3.recycle();
        if (!z3 && z4) {
            setAllCaps(z);
        }
        applyFontAndVariationSettings(false);
        if (string != null) {
            Api24Impl.setTextLocales(this.mView, Api24Impl.forLanguageTags(string));
        }
        this.mAutoSizeTextHelper.loadFromAttributes(attributeSet, i);
        if (ViewUtils.SDK_LEVEL_SUPPORTS_AUTOSIZE && this.mAutoSizeTextHelper.getAutoSizeTextType() != 0) {
            int[] autoSizeTextAvailableSizes = this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
            if (autoSizeTextAvailableSizes.length > 0) {
                float autoSizeStepGranularity = Api26Impl.getAutoSizeStepGranularity(this.mView);
                TextView textView2 = this.mView;
                if (autoSizeStepGranularity != -1.0f) {
                    Api26Impl.setAutoSizeTextTypeUniformWithConfiguration(textView2, this.mAutoSizeTextHelper.getAutoSizeMinTextSize(), this.mAutoSizeTextHelper.getAutoSizeMaxTextSize(), this.mAutoSizeTextHelper.getAutoSizeStepGranularity(), 0);
                } else {
                    Api26Impl.setAutoSizeTextTypeUniformWithPresetSizes(textView2, autoSizeTextAvailableSizes, 0);
                }
            }
        }
        TintTypedArray tintTypedArrayObtainStyledAttributes4 = TintTypedArray.obtainStyledAttributes(context, attributeSet, R$styleable.AppCompatTextView);
        int resourceId2 = tintTypedArrayObtainStyledAttributes4.getResourceId(R$styleable.AppCompatTextView_drawableLeftCompat, -1);
        Drawable drawable = resourceId2 != -1 ? appCompatDrawableManager.getDrawable(context, resourceId2) : null;
        int resourceId3 = tintTypedArrayObtainStyledAttributes4.getResourceId(R$styleable.AppCompatTextView_drawableTopCompat, -1);
        Drawable drawable2 = resourceId3 != -1 ? appCompatDrawableManager.getDrawable(context, resourceId3) : null;
        int resourceId4 = tintTypedArrayObtainStyledAttributes4.getResourceId(R$styleable.AppCompatTextView_drawableRightCompat, -1);
        Drawable drawable3 = resourceId4 != -1 ? appCompatDrawableManager.getDrawable(context, resourceId4) : null;
        int resourceId5 = tintTypedArrayObtainStyledAttributes4.getResourceId(R$styleable.AppCompatTextView_drawableBottomCompat, -1);
        Drawable drawable4 = resourceId5 != -1 ? appCompatDrawableManager.getDrawable(context, resourceId5) : null;
        int resourceId6 = tintTypedArrayObtainStyledAttributes4.getResourceId(R$styleable.AppCompatTextView_drawableStartCompat, -1);
        Drawable drawable5 = resourceId6 != -1 ? appCompatDrawableManager.getDrawable(context, resourceId6) : null;
        int resourceId7 = tintTypedArrayObtainStyledAttributes4.getResourceId(R$styleable.AppCompatTextView_drawableEndCompat, -1);
        setCompoundDrawables(drawable, drawable2, drawable3, drawable4, drawable5, resourceId7 != -1 ? appCompatDrawableManager.getDrawable(context, resourceId7) : null);
        if (tintTypedArrayObtainStyledAttributes4.hasValue(R$styleable.AppCompatTextView_drawableTint)) {
            TextViewCompat.setCompoundDrawableTintList(this.mView, tintTypedArrayObtainStyledAttributes4.getColorStateList(R$styleable.AppCompatTextView_drawableTint));
        }
        if (tintTypedArrayObtainStyledAttributes4.hasValue(R$styleable.AppCompatTextView_drawableTintMode)) {
            TextViewCompat.setCompoundDrawableTintMode(this.mView, DrawableUtils.parseTintMode(tintTypedArrayObtainStyledAttributes4.getInt(R$styleable.AppCompatTextView_drawableTintMode, -1), null));
        }
        int dimensionPixelSize2 = tintTypedArrayObtainStyledAttributes4.getDimensionPixelSize(R$styleable.AppCompatTextView_firstBaselineToTopHeight, -1);
        int dimensionPixelSize3 = tintTypedArrayObtainStyledAttributes4.getDimensionPixelSize(R$styleable.AppCompatTextView_lastBaselineToBottomHeight, -1);
        if (tintTypedArrayObtainStyledAttributes4.hasValue(R$styleable.AppCompatTextView_lineHeight)) {
            TypedValue typedValuePeekValue = tintTypedArrayObtainStyledAttributes4.peekValue(R$styleable.AppCompatTextView_lineHeight);
            if (typedValuePeekValue != null && typedValuePeekValue.type == 5) {
                unitFromComplexDimension = TypedValueCompat.getUnitFromComplexDimension(typedValuePeekValue.data);
                dimensionPixelSize = TypedValue.complexToFloat(typedValuePeekValue.data);
            } else {
                dimensionPixelSize = tintTypedArrayObtainStyledAttributes4.getDimensionPixelSize(R$styleable.AppCompatTextView_lineHeight, -1);
                unitFromComplexDimension = -1;
            }
        } else {
            unitFromComplexDimension = -1;
            dimensionPixelSize = -1.0f;
        }
        tintTypedArrayObtainStyledAttributes4.recycle();
        if (dimensionPixelSize2 != -1) {
            TextViewCompat.setFirstBaselineToTopHeight(this.mView, dimensionPixelSize2);
        }
        if (dimensionPixelSize3 != -1) {
            TextViewCompat.setLastBaselineToBottomHeight(this.mView, dimensionPixelSize3);
        }
        if (dimensionPixelSize != -1.0f) {
            TextView textView3 = this.mView;
            if (unitFromComplexDimension == -1) {
                TextViewCompat.setLineHeight(textView3, (int) dimensionPixelSize);
            } else {
                TextViewCompat.setLineHeight(textView3, unitFromComplexDimension, dimensionPixelSize);
            }
        }
    }

    private void applyFontAndVariationSettings(boolean z) {
        Typeface typeface = this.mFontTypeface;
        if (typeface != null) {
            int i = this.mFontWeight;
            TextView textView = this.mView;
            if (i == -1) {
                textView.setTypeface(typeface, this.mStyle);
            } else {
                textView.setTypeface(typeface);
            }
        } else if (z) {
            this.mView.setTypeface(null);
        }
        String str = this.mFontVariationSettings;
        if (str != null) {
            Api26Impl.setFontVariationSettings(this.mView, str);
        }
    }

    private boolean updateTypefaceAndStyle(Context context, TintTypedArray tintTypedArray) {
        int i;
        String string;
        Typeface typeface;
        this.mStyle = tintTypedArray.getInt(R$styleable.TextAppearance_android_textStyle, this.mStyle);
        int i2 = tintTypedArray.getInt(R$styleable.TextAppearance_android_textFontWeight, -1);
        this.mFontWeight = i2;
        if (i2 != -1) {
            this.mStyle &= 2;
        }
        if (tintTypedArray.hasValue(R$styleable.TextAppearance_fontVariationSettings)) {
            this.mFontVariationSettings = tintTypedArray.getString(R$styleable.TextAppearance_fontVariationSettings);
        }
        if (tintTypedArray.hasValue(R$styleable.TextAppearance_android_fontFamily) || tintTypedArray.hasValue(R$styleable.TextAppearance_fontFamily)) {
            this.mFontTypeface = null;
            if (tintTypedArray.hasValue(R$styleable.TextAppearance_fontFamily)) {
                i = R$styleable.TextAppearance_fontFamily;
            } else {
                i = R$styleable.TextAppearance_android_fontFamily;
            }
            int i3 = this.mFontWeight;
            int i4 = this.mStyle;
            if (!context.isRestricted()) {
                try {
                    Typeface font = tintTypedArray.getFont(i, this.mStyle, makeFontCallback(i3, i4));
                    if (font != null) {
                        if (this.mFontWeight != -1) {
                            this.mFontTypeface = Api28Impl.create(Typeface.create(font, 0), this.mFontWeight, (this.mStyle & 2) != 0);
                        } else {
                            this.mFontTypeface = font;
                        }
                    }
                    this.mAsyncFontPending = this.mFontTypeface == null;
                } catch (Resources.NotFoundException | UnsupportedOperationException unused) {
                }
            }
            if (this.mFontTypeface == null && (string = tintTypedArray.getString(i)) != null) {
                if (this.mFontWeight != -1) {
                    this.mFontTypeface = Api28Impl.create(Typeface.create(string, 0), this.mFontWeight, (this.mStyle & 2) != 0);
                } else {
                    this.mFontTypeface = Typeface.create(string, this.mStyle);
                }
            }
            return true;
        }
        if (tintTypedArray.hasValue(R$styleable.TextAppearance_android_typeface)) {
            this.mAsyncFontPending = false;
            int i5 = tintTypedArray.getInt(R$styleable.TextAppearance_android_typeface, 1);
            if (i5 == 1) {
                this.mFontTypeface = Typeface.SANS_SERIF;
            } else if (i5 == 2) {
                this.mFontTypeface = Typeface.SERIF;
            } else if (i5 == 3) {
                this.mFontTypeface = Typeface.MONOSPACE;
            }
            return true;
        }
        int i6 = this.mFontWeight;
        if (i6 == -1 || (typeface = this.mFontTypeface) == null) {
            return false;
        }
        this.mFontTypeface = Api28Impl.create(typeface, i6, (this.mStyle & 2) != 0);
        return true;
    }

    private ResourcesCompat.FontCallback makeFontCallback(final int i, final int i2) {
        final WeakReference weakReference = new WeakReference(this.mView);
        return new ResourcesCompat.FontCallback() { // from class: androidx.appcompat.widget.AppCompatTextHelper.1
            @Override // androidx.core.content.res.ResourcesCompat.FontCallback
            public void onFontRetrievalFailed(int i3) {
            }

            @Override // androidx.core.content.res.ResourcesCompat.FontCallback
            public void onFontRetrieved(Typeface typeface) {
                int i3 = i;
                if (i3 != -1) {
                    typeface = Api28Impl.create(typeface, i3, (i2 & 2) != 0);
                }
                AppCompatTextHelper.this.onAsyncTypefaceReceived(weakReference, typeface);
            }
        };
    }

    void onAsyncTypefaceReceived(WeakReference weakReference, final Typeface typeface) {
        if (this.mAsyncFontPending) {
            this.mFontTypeface = typeface;
            final TextView textView = (TextView) weakReference.get();
            if (textView != null) {
                boolean zIsAttachedToWindow = textView.isAttachedToWindow();
                final int i = this.mStyle;
                if (zIsAttachedToWindow) {
                    textView.post(new Runnable() { // from class: androidx.appcompat.widget.AppCompatTextHelper.2
                        @Override // java.lang.Runnable
                        public void run() {
                            AppCompatTextHelper.applyNewTypefacePreservingVariationSettings(textView, typeface, i);
                        }
                    });
                } else {
                    applyNewTypefacePreservingVariationSettings(textView, typeface, i);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void applyNewTypefacePreservingVariationSettings(TextView textView, Typeface typeface, int i) {
        String fontVariationSettings = Api26Impl.getFontVariationSettings(textView);
        if (!TextUtils.isEmpty(fontVariationSettings)) {
            Api26Impl.setFontVariationSettings(textView, null);
        }
        textView.setTypeface(typeface, i);
        if (TextUtils.isEmpty(fontVariationSettings)) {
            return;
        }
        Api26Impl.setFontVariationSettings(textView, fontVariationSettings);
    }

    void onSetTextAppearance(Context context, int i) {
        TintTypedArray tintTypedArrayObtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, i, R$styleable.TextAppearance);
        if (tintTypedArrayObtainStyledAttributes.hasValue(R$styleable.TextAppearance_textAllCaps)) {
            setAllCaps(tintTypedArrayObtainStyledAttributes.getBoolean(R$styleable.TextAppearance_textAllCaps, false));
        }
        if (tintTypedArrayObtainStyledAttributes.hasValue(R$styleable.TextAppearance_android_textSize) && tintTypedArrayObtainStyledAttributes.getDimensionPixelSize(R$styleable.TextAppearance_android_textSize, -1) == 0) {
            this.mView.setTextSize(0, 0.0f);
        }
        boolean zUpdateTypefaceAndStyle = updateTypefaceAndStyle(context, tintTypedArrayObtainStyledAttributes);
        tintTypedArrayObtainStyledAttributes.recycle();
        applyFontAndVariationSettings(zUpdateTypefaceAndStyle);
    }

    void setAllCaps(boolean z) {
        this.mView.setAllCaps(z);
    }

    void onSetCompoundDrawables() {
        applyCompoundDrawablesTints();
    }

    void applyCompoundDrawablesTints() {
        if (this.mDrawableLeftTint != null || this.mDrawableTopTint != null || this.mDrawableRightTint != null || this.mDrawableBottomTint != null) {
            Drawable[] compoundDrawables = this.mView.getCompoundDrawables();
            applyCompoundDrawableTint(compoundDrawables[0], this.mDrawableLeftTint);
            applyCompoundDrawableTint(compoundDrawables[1], this.mDrawableTopTint);
            applyCompoundDrawableTint(compoundDrawables[2], this.mDrawableRightTint);
            applyCompoundDrawableTint(compoundDrawables[3], this.mDrawableBottomTint);
        }
        if (this.mDrawableStartTint == null && this.mDrawableEndTint == null) {
            return;
        }
        Drawable[] compoundDrawablesRelative = this.mView.getCompoundDrawablesRelative();
        applyCompoundDrawableTint(compoundDrawablesRelative[0], this.mDrawableStartTint);
        applyCompoundDrawableTint(compoundDrawablesRelative[2], this.mDrawableEndTint);
    }

    private void applyCompoundDrawableTint(Drawable drawable, TintInfo tintInfo) {
        if (drawable == null || tintInfo == null) {
            return;
        }
        AppCompatDrawableManager.tintDrawable(drawable, tintInfo, this.mView.getDrawableState());
    }

    private static TintInfo createTintInfo(Context context, AppCompatDrawableManager appCompatDrawableManager, int i) {
        ColorStateList tintList = appCompatDrawableManager.getTintList(context, i);
        if (tintList == null) {
            return null;
        }
        TintInfo tintInfo = new TintInfo();
        tintInfo.mHasTintList = true;
        tintInfo.mTintList = tintList;
        return tintInfo;
    }

    void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (ViewUtils.SDK_LEVEL_SUPPORTS_AUTOSIZE) {
            return;
        }
        autoSizeText();
    }

    void setTextSize(int i, float f) {
        if (ViewUtils.SDK_LEVEL_SUPPORTS_AUTOSIZE || isAutoSizeEnabled()) {
            return;
        }
        setTextSizeInternal(i, f);
    }

    void autoSizeText() {
        this.mAutoSizeTextHelper.autoSizeText();
    }

    boolean isAutoSizeEnabled() {
        return this.mAutoSizeTextHelper.isAutoSizeEnabled();
    }

    private void setTextSizeInternal(int i, float f) {
        this.mAutoSizeTextHelper.setTextSizeInternal(i, f);
    }

    void setAutoSizeTextTypeWithDefaults(int i) {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeWithDefaults(i);
    }

    void setAutoSizeTextTypeUniformWithConfiguration(int i, int i2, int i3, int i4) {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithConfiguration(i, i2, i3, i4);
    }

    void setAutoSizeTextTypeUniformWithPresetSizes(int[] iArr, int i) {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(iArr, i);
    }

    int getAutoSizeTextType() {
        return this.mAutoSizeTextHelper.getAutoSizeTextType();
    }

    int getAutoSizeStepGranularity() {
        return this.mAutoSizeTextHelper.getAutoSizeStepGranularity();
    }

    int getAutoSizeMinTextSize() {
        return this.mAutoSizeTextHelper.getAutoSizeMinTextSize();
    }

    int getAutoSizeMaxTextSize() {
        return this.mAutoSizeTextHelper.getAutoSizeMaxTextSize();
    }

    int[] getAutoSizeTextAvailableSizes() {
        return this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
    }

    private void setCompoundDrawables(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4, Drawable drawable5, Drawable drawable6) {
        if (drawable5 != null || drawable6 != null) {
            Drawable[] compoundDrawablesRelative = this.mView.getCompoundDrawablesRelative();
            if (drawable5 == null) {
                drawable5 = compoundDrawablesRelative[0];
            }
            if (drawable2 == null) {
                drawable2 = compoundDrawablesRelative[1];
            }
            if (drawable6 == null) {
                drawable6 = compoundDrawablesRelative[2];
            }
            TextView textView = this.mView;
            if (drawable4 == null) {
                drawable4 = compoundDrawablesRelative[3];
            }
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable5, drawable2, drawable6, drawable4);
            return;
        }
        if (drawable == null && drawable2 == null && drawable3 == null && drawable4 == null) {
            return;
        }
        Drawable[] compoundDrawablesRelative2 = this.mView.getCompoundDrawablesRelative();
        Drawable drawable7 = compoundDrawablesRelative2[0];
        if (drawable7 != null || compoundDrawablesRelative2[2] != null) {
            if (drawable2 == null) {
                drawable2 = compoundDrawablesRelative2[1];
            }
            if (drawable4 == null) {
                drawable4 = compoundDrawablesRelative2[3];
            }
            this.mView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable7, drawable2, compoundDrawablesRelative2[2], drawable4);
            return;
        }
        Drawable[] compoundDrawables = this.mView.getCompoundDrawables();
        TextView textView2 = this.mView;
        if (drawable == null) {
            drawable = compoundDrawables[0];
        }
        if (drawable2 == null) {
            drawable2 = compoundDrawables[1];
        }
        if (drawable3 == null) {
            drawable3 = compoundDrawables[2];
        }
        if (drawable4 == null) {
            drawable4 = compoundDrawables[3];
        }
        textView2.setCompoundDrawablesWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
    }

    abstract class Api26Impl {
        private static Paint sPaint;
        private static final LruCache sVariationsCache = new LruCache(30);

        class VarCacheKey {
            private final String mFontVariationSettings;
            private final Typeface mTypeface;
            private final int mWeightAdjustment;

            VarCacheKey(Typeface typeface, String str, int i) {
                this.mTypeface = typeface;
                this.mFontVariationSettings = str;
                this.mWeightAdjustment = i;
            }

            public boolean equals(Object obj) {
                if (!(obj instanceof VarCacheKey)) {
                    return false;
                }
                VarCacheKey varCacheKey = (VarCacheKey) obj;
                return this.mWeightAdjustment == varCacheKey.mWeightAdjustment && Objects.equals(this.mTypeface, varCacheKey.mTypeface) && Objects.equals(this.mFontVariationSettings, varCacheKey.mFontVariationSettings);
            }

            public int hashCode() {
                return Objects.hash(this.mTypeface, this.mFontVariationSettings, Integer.valueOf(this.mWeightAdjustment));
            }
        }

        static String getFontVariationSettings(TextView textView) {
            return textView.getFontVariationSettings();
        }

        static boolean setFontVariationSettings(TextView textView, String str) {
            if (Objects.equals(textView.getFontVariationSettings(), str)) {
                textView.setFontVariationSettings("");
            }
            return textView.setFontVariationSettings(str);
        }

        private static float clampWeight(float f) {
            if (f < 1.0f) {
                return 1.0f;
            }
            return Math.min(f, 1000.0f);
        }

        static String adjustFontVariationSettings(String str, int i) {
            FontVariationAxis[] fontVariationAxisArr;
            if (i != 0) {
                if (TextUtils.isEmpty(str)) {
                    fontVariationAxisArr = new FontVariationAxis[0];
                } else {
                    FontVariationAxis[] fontVariationAxisArrFromFontVariationSettings = FontVariationAxis.fromFontVariationSettings(str);
                    if (fontVariationAxisArrFromFontVariationSettings != null) {
                        fontVariationAxisArr = fontVariationAxisArrFromFontVariationSettings;
                    }
                }
                boolean z = false;
                for (int i2 = 0; i2 < fontVariationAxisArr.length; i2++) {
                    FontVariationAxis fontVariationAxis = fontVariationAxisArr[i2];
                    if ("wght".equals(fontVariationAxis.getTag())) {
                        fontVariationAxisArr[i2] = new FontVariationAxis("wght", clampWeight(fontVariationAxis.getStyleValue() + i));
                        z = true;
                    }
                }
                if (!z) {
                    FontVariationAxis[] fontVariationAxisArr2 = new FontVariationAxis[fontVariationAxisArr.length + 1];
                    System.arraycopy(fontVariationAxisArr, 0, fontVariationAxisArr2, 0, fontVariationAxisArr.length);
                    fontVariationAxisArr2[fontVariationAxisArr.length] = new FontVariationAxis("wght", clampWeight(i + 400));
                    fontVariationAxisArr = fontVariationAxisArr2;
                }
                return FontVariationAxis.toFontVariationSettings(fontVariationAxisArr);
            }
            return str;
        }

        static Typeface createVariationInstance(Typeface typeface, String str, int i) {
            VarCacheKey varCacheKey = new VarCacheKey(typeface, str, i);
            LruCache lruCache = sVariationsCache;
            Typeface typeface2 = (Typeface) lruCache.get(varCacheKey);
            if (typeface2 != null) {
                return typeface2;
            }
            Paint paint = sPaint;
            if (paint == null) {
                paint = new Paint();
                sPaint = paint;
            }
            String strAdjustFontVariationSettings = adjustFontVariationSettings(str, i);
            if (Objects.equals(paint.getFontVariationSettings(), strAdjustFontVariationSettings)) {
                paint.setFontVariationSettings(null);
            }
            paint.setTypeface(typeface);
            if (!paint.setFontVariationSettings(strAdjustFontVariationSettings)) {
                return null;
            }
            Typeface typeface3 = paint.getTypeface();
            lruCache.put(varCacheKey, typeface3);
            return typeface3;
        }

        static int getAutoSizeStepGranularity(TextView textView) {
            return textView.getAutoSizeStepGranularity();
        }

        static void setAutoSizeTextTypeUniformWithConfiguration(TextView textView, int i, int i2, int i3, int i4) {
            textView.setAutoSizeTextTypeUniformWithConfiguration(i, i2, i3, i4);
        }

        static void setAutoSizeTextTypeUniformWithPresetSizes(TextView textView, int[] iArr, int i) {
            textView.setAutoSizeTextTypeUniformWithPresetSizes(iArr, i);
        }
    }

    abstract class Api24Impl {
        static void setTextLocales(TextView textView, LocaleList localeList) {
            textView.setTextLocales(localeList);
        }

        static LocaleList forLanguageTags(String str) {
            return LocaleList.forLanguageTags(str);
        }
    }

    abstract class Api28Impl {
        static Typeface create(Typeface typeface, int i, boolean z) {
            return Typeface.create(typeface, i, z);
        }
    }

    class FontVariationSettingsManager {
        private String mFontVariationSettings;
        private int mFontWeightAdjustmentForTesting = -1;
        private Typeface mLastKnownTypefaceSetOnPaint;
        private Typeface mOriginalTypeface;
        private final TextView mTextView;
        private final Consumer mTypefaceSetter;

        FontVariationSettingsManager(TextView textView, Consumer consumer) {
            this.mTextView = textView;
            this.mTypefaceSetter = consumer;
        }

        public boolean setFontVariationSettings(String str) {
            Typeface typeface = this.mOriginalTypeface;
            TextPaint paint = this.mTextView.getPaint();
            if (this.mLastKnownTypefaceSetOnPaint != paint.getTypeface()) {
                Log.w("FontVarSettings", "getPaint().getTypeface() changed unexpectedly. App code should not modify the result of getPaint().");
                typeface = paint.getTypeface();
            }
            int fontWeightAdjustment = getFontWeightAdjustment();
            if (fontWeightAdjustment == Integer.MAX_VALUE) {
                fontWeightAdjustment = 0;
            }
            Typeface typefaceCreateVariationInstance = Api26Impl.createVariationInstance(typeface, str, fontWeightAdjustment);
            if (typefaceCreateVariationInstance == null) {
                return false;
            }
            setTypefaceInternal(typefaceCreateVariationInstance);
            this.mFontVariationSettings = str;
            return true;
        }

        public String getFontVariationSettings() {
            return this.mFontVariationSettings;
        }

        int getFontWeightAdjustment() {
            int i = this.mFontWeightAdjustmentForTesting;
            if (i != -1) {
                return i;
            }
            int i2 = this.mTextView.getContext().getResources().getConfiguration().fontWeightAdjustment;
            if (i2 == Integer.MAX_VALUE) {
                return 0;
            }
            return i2;
        }

        void setFontWeightAdjustmentForTesting(int i) {
            this.mFontWeightAdjustmentForTesting = i;
        }

        public void setTypeface(Typeface typeface) {
            this.mOriginalTypeface = typeface;
            setTypefaceInternal(typeface);
        }

        public Typeface getTypeface() {
            return this.mOriginalTypeface;
        }

        private void setTypefaceInternal(Typeface typeface) {
            this.mLastKnownTypefaceSetOnPaint = typeface;
            this.mTypefaceSetter.accept(typeface);
        }
    }
}
