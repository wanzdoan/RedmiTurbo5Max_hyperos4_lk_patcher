package miuix.view;

import android.R;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import miuix.core.util.HyperMaterialUtils;
import miuix.core.util.MaterialConfig;
import miuix.core.util.MiuiBlurUtils;
import miuix.core.util.MiuixUIUtils;
import miuix.internal.util.AttributeResolver;

/* JADX INFO: loaded from: classes2.dex */
public class MiuiBlurUiHelper implements BlurableWidget {
    private boolean mActualApply;
    private boolean mActualEnable;
    private int[] mBlurBlendColorModes;
    private int[] mBlurBlendColors;
    private int mBlurRadius;
    private final BlurStateCallback mCallback;
    private final Context mContext;
    private boolean mEnableBlurSelf;
    private boolean mExpectedApply;
    private boolean mExpectedEnable;
    private final boolean mIsBlurParamsByCallback;
    private boolean mIsSupportBlur;
    private boolean mMaterialFullyTakenOver;
    private final View mTargetView;
    private int mViewBlurMode;

    public interface BlurStateCallback {
        Drawable getBackground();

        default int getBackgroundColor() {
            return 0;
        }

        default BlurApplier getBlurApplier() {
            return null;
        }

        MaterialConfig.BlurConfig getBlurConfig(boolean z);

        boolean isLightTheme();

        void onBlurApplyStateChanged(boolean z);

        void onBlurEnableStateChanged(boolean z);

        default void onCreateBlurParams(MiuiBlurUiHelper miuiBlurUiHelper) {
        }
    }

    private int dpToPx(float f, float f2) {
        return (int) ((f * f2) + 0.5f);
    }

    public MiuiBlurUiHelper(Context context, View view, boolean z, boolean z2, BlurStateCallback blurStateCallback) {
        this(context, view, z, z2, true, blurStateCallback);
    }

    public MiuiBlurUiHelper(Context context, View view, boolean z, boolean z2, boolean z3, BlurStateCallback blurStateCallback) {
        this.mIsSupportBlur = false;
        this.mExpectedEnable = false;
        this.mActualEnable = false;
        this.mExpectedApply = false;
        this.mActualApply = false;
        this.mBlurBlendColors = null;
        this.mBlurBlendColorModes = null;
        this.mBlurRadius = 0;
        this.mMaterialFullyTakenOver = false;
        this.mContext = context;
        this.mTargetView = view;
        this.mCallback = blurStateCallback;
        this.mIsBlurParamsByCallback = z2;
        this.mEnableBlurSelf = z3;
        this.mViewBlurMode = z ? 2 : 1;
    }

    @Override // miuix.view.BlurableWidget
    public void setSupportBlur(boolean z) {
        this.mIsSupportBlur = z;
    }

    public boolean isSupportBlur() {
        return this.mIsSupportBlur;
    }

    @Override // miuix.view.BlurableWidget
    public void setEnableBlur(boolean z) {
        if (this.mIsSupportBlur) {
            this.mExpectedEnable = z;
            if (HyperMaterialUtils.isFeatureEnable(this.mContext)) {
                setEnableInner(this.mExpectedEnable);
            }
        }
    }

    public boolean isEnableBlur() {
        return this.mExpectedEnable;
    }

    @Override // miuix.view.BlurableWidget
    public void applyBlur(boolean z) {
        this.mExpectedApply = z;
        applyBlurInner(z);
    }

    public boolean isApplyBlur() {
        return this.mExpectedApply;
    }

    public void setBlurParams(int[] iArr, int[] iArr2, int i) {
        this.mBlurBlendColors = iArr;
        this.mBlurBlendColorModes = iArr2;
        this.mBlurRadius = i;
    }

    public void onConfigChanged() {
        resetBlurParams();
        if (!HyperMaterialUtils.isFeatureEnable(this.mContext)) {
            setEnableInner(false);
        } else if (HyperMaterialUtils.isSupport() && isEnableBlur()) {
            setEnableInner(true);
        }
    }

    public void resetBlurParams() {
        this.mBlurBlendColors = null;
        this.mBlurBlendColorModes = null;
        this.mBlurRadius = 0;
    }

    public void refreshBlur() {
        if (this.mActualApply) {
            if (this.mBlurBlendColors == null) {
                createBlurParamsIfNeeded();
            }
            float density = getDensity();
            this.mCallback.onBlurApplyStateChanged(true);
            MiuiBlurUtils.setMiViewMaterialType(this.mTargetView, 0);
            applyBlur(density);
            if (this.mMaterialFullyTakenOver) {
                return;
            }
            applyColorBlend();
        }
    }

    private void setEnableInner(boolean z) {
        if (this.mActualEnable == z) {
            return;
        }
        if (z) {
            this.mActualEnable = true;
            applyBlurInner(this.mExpectedApply);
        } else {
            applyBlurInner(false);
            this.mActualEnable = false;
        }
        this.mCallback.onBlurEnableStateChanged(this.mActualEnable);
    }

    private void applyBlurInner(boolean z) {
        if (this.mIsSupportBlur && this.mActualEnable && this.mActualApply != z) {
            this.mActualApply = z;
            if (z) {
                refreshBlur();
            } else {
                handleCancelBlur();
            }
        }
    }

    private void handleCancelBlur() {
        MiuiBlurUtils.clearBackgroundBlur(this.mTargetView);
        MiuiBlurUtils.clearBackgroundBlendConfig(this.mTargetView);
        this.mCallback.onBlurApplyStateChanged(false);
    }

    private void createBlurParamsIfNeeded() {
        boolean z = this.mEnableBlurSelf;
        View view = this.mTargetView;
        if (z) {
            MiuiBlurUtils.clearBackgroundBlur(view);
        } else {
            MiuiBlurUtils.setViewBlurMode(view, 0);
        }
        MiuiBlurUtils.clearBackgroundBlendConfig(this.mTargetView);
        if (this.mIsBlurParamsByCallback) {
            this.mCallback.onCreateBlurParams(this);
        } else {
            createBlurParamsInternal();
        }
    }

    private void createBlurParamsInternal() {
        int[] finalBlendColorForViewByBackgroundColor;
        BlurStateCallback blurStateCallback = this.mCallback;
        MaterialConfig.BlurConfig blurConfig = blurStateCallback.getBlurConfig(blurStateCallback.isLightTheme());
        if (blurConfig == null) {
            return;
        }
        Drawable background = this.mCallback.getBackground();
        int backgroundColor = this.mCallback.getBackgroundColor();
        int i = blurConfig.blurRadius;
        MaterialConfig.ColorBlendConfig colorBlendConfig = blurConfig.colorBlendConfig;
        if (colorBlendConfig != null) {
            int[] iArr = colorBlendConfig.blendColors;
            Context context = this.mContext;
            if (backgroundColor != 0) {
                finalBlendColorForViewByBackgroundColor = getFinalBlendColorForViewByBackgroundColor(context, backgroundColor, iArr, colorBlendConfig.blendModes);
            } else {
                finalBlendColorForViewByBackgroundColor = getFinalBlendColorForViewByBackgroundColor(context, background, iArr, colorBlendConfig.blendModes);
            }
            int length = finalBlendColorForViewByBackgroundColor.length;
            int[] iArr2 = colorBlendConfig.blendModes;
            if (length > iArr2.length) {
                int length2 = finalBlendColorForViewByBackgroundColor.length;
                int[] iArr3 = new int[length2];
                System.arraycopy(iArr2, 0, iArr3, 0, iArr2.length);
                iArr3[length2 - 1] = 3;
                iArr2 = iArr3;
            }
            setBlurParams(finalBlendColorForViewByBackgroundColor, iArr2, i);
            return;
        }
        this.mBlurRadius = i;
    }

    private float getDensity() {
        try {
            return this.mTargetView.getContext().getResources().getDisplayMetrics().density;
        } catch (Exception unused) {
            return 2.75f;
        }
    }

    private void applyBlur(float f) {
        this.mMaterialFullyTakenOver = false;
        if (this.mEnableBlurSelf) {
            applyBlurSelf(f);
        } else {
            applyBlurParent(f);
        }
    }

    private void applyBlurSelf(float f) {
        BlurApplier blurApplier = this.mCallback.getBlurApplier();
        if (blurApplier == null) {
            blurApplier = BlurApplier.UNIFORM;
        }
        int i = this.mBlurRadius;
        if (i > 0) {
            int iDpToPx = dpToPx(i, f);
            BlurStateCallback blurStateCallback = this.mCallback;
            this.mMaterialFullyTakenOver = blurApplier.applyMaterial(this.mTargetView, iDpToPx, this.mViewBlurMode, blurStateCallback.getBlurConfig(blurStateCallback.isLightTheme()));
            return;
        }
        blurApplier.clearBlur(this.mTargetView);
    }

    private void applyBlurParent(float f) {
        MiuiBlurUtils.setViewBlurMode(this.mTargetView, this.mViewBlurMode);
        int i = this.mBlurRadius;
        if (i >= 0) {
            MiuiBlurUtils.setBackgroundBlurRadius(this.mTargetView, dpToPx(i, f));
        }
    }

    private void applyColorBlend() {
        int[] iArr;
        int[] iArr2 = this.mBlurBlendColors;
        if (iArr2 == null || (iArr = this.mBlurBlendColorModes) == null) {
            return;
        }
        MiuiBlurUtils.setBackgroundBlendConfig(this.mTargetView, iArr2, iArr);
    }

    public static int[] getFinalBlendColorForViewByBackgroundColor(Context context, Drawable drawable, int[] iArr, int[] iArr2) {
        Integer colorFromDrawable;
        return (drawable == null || (colorFromDrawable = MiuixUIUtils.getColorFromDrawable(drawable)) == null) ? iArr : getFinalBlendColorForViewByBackgroundColor(context, colorFromDrawable.intValue(), iArr, iArr2);
    }

    public static int[] getFinalBlendColorForViewByBackgroundColor(Context context, int i, int[] iArr, int[] iArr2) {
        int length = iArr.length;
        int[] iArr3 = new int[length];
        System.arraycopy(iArr, 0, iArr3, 0, iArr.length);
        if (i == 0) {
            Drawable drawableResolveDrawable = AttributeResolver.resolveDrawable(context, R.attr.windowBackground);
            if (drawableResolveDrawable instanceof ColorDrawable) {
                i = ((ColorDrawable) drawableResolveDrawable).getColor();
            }
        }
        if (i == 0 || i == iArr[iArr.length - 1]) {
            return iArr3;
        }
        int i2 = (i >> 16) & 255;
        int i3 = i & 255;
        if (i2 == ((i >> 8) & 255) && i2 == i3) {
            int i4 = length - 1;
            iArr3[i4] = (i & 16777215) | ((-16777216) & iArr[i4]);
            return iArr3;
        }
        int length2 = iArr.length;
        int[] iArr4 = new int[length2 + 1];
        System.arraycopy(iArr, 0, iArr4, 0, iArr.length);
        iArr4[length2] = (i & 16777215) | 805306368;
        return iArr4;
    }
}
