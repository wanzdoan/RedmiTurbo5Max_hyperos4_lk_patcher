package miuix.appcompat.internal.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.LinearInterpolator;
import java.util.Arrays;
import miuix.appcompat.R$id;
import miuix.appcompat.internal.graphics.drawable.RoundStateDrawable;
import miuix.core.util.HyperBloomStrokeUtils;
import miuix.core.util.HyperMaterialUtils;
import miuix.core.util.MaterialConfig;
import miuix.core.util.MaterialDayNightConfig;
import miuix.core.util.MiShadowUtils;
import miuix.core.util.MiuiBlurUtils;
import miuix.theme.token.BloomStrokeToken;
import miuix.theme.token.ColorBlendToken;
import miuix.theme.token.MaterialDayNightToken;
import miuix.theme.token.MaterialToken;
import miuix.theme.token.ShadowToken;

/* JADX INFO: loaded from: classes2.dex */
public abstract class FloatingEffectHelper {
    private static final MaterialDayNightToken Pured_Thin_Glass;
    private static final MaterialToken Pured_Thin_Glass_Dark;
    private static final MaterialToken Pured_Thin_Glass_Light;
    private static final MaterialDayNightConfig sMaterial;

    static {
        MaterialToken materialTokenBuild = new MaterialToken.Builder(10, "internal-pured-thin-glass", "light").setColorBlend(ColorBlendToken.Pured_Thin_Glass_Light).setMaskBlur(20).setBloomStroke(BloomStrokeToken.Glass_Stroke_Small_Light).setShadow(ShadowToken.Regular).build();
        Pured_Thin_Glass_Light = materialTokenBuild;
        MaterialToken materialTokenBuild2 = new MaterialToken.Builder(10, "internal-pured-thin-glass", "dark").setColorBlend(ColorBlendToken.Pured_Thin_Glass_Dark).setMaskBlur(20).setBloomStroke(BloomStrokeToken.Glass_Stroke_Small_Dark).setShadow(ShadowToken.Regular).build();
        Pured_Thin_Glass_Dark = materialTokenBuild2;
        MaterialDayNightToken materialDayNightToken = new MaterialDayNightToken(materialTokenBuild, materialTokenBuild2);
        Pured_Thin_Glass = materialDayNightToken;
        sMaterial = MaterialDayNightConfig.create(materialDayNightToken);
    }

    private static boolean isMaterialEffectEnabled(View view, boolean z) {
        return z && HyperMaterialUtils.isSupport() && HyperMaterialUtils.isFeatureEnable(view.getContext());
    }

    public static void updateViewFloatingState(View view, boolean z, boolean z2, boolean z3, int i) {
        if (view == null) {
            return;
        }
        FloatingAppliedState floatingAppliedState = new FloatingAppliedState(z, z2, z3, i);
        FloatingAppliedState appliedState = getAppliedState(view);
        if (floatingAppliedState.equals(appliedState)) {
            return;
        }
        if (appliedState == null || appliedState.floatingMode != z) {
            if (z) {
                handleAnimatedFloatingOn(view, z2, z3, i);
            } else {
                handleAnimatedFloatingOff(view, z2, z3, i);
            }
        } else if (z) {
            handleFloatingOn(view, z2, z3, i);
        } else {
            handleFloatingOff(view, z2, z3);
        }
        setAppliedState(view, floatingAppliedState);
    }

    public static void setFloatingState(View view, boolean z, boolean z2, boolean z3, int i) {
        if (view == null) {
            return;
        }
        FloatingAppliedState floatingAppliedState = new FloatingAppliedState(z, z2, z3, i);
        if (floatingAppliedState.equals(getAppliedState(view))) {
            return;
        }
        if (z) {
            handleFloatingOn(view, z2, z3, i);
        } else {
            handleFloatingOff(view, z2, z3);
        }
        setAppliedState(view, floatingAppliedState);
    }

    private static void handleAnimatedFloatingOn(View view, boolean z, boolean z2, int i) {
        MaterialConfig materialConfig;
        int radius = getRadius(z);
        boolean z3 = !z;
        boolean z4 = MiShadowUtils.SUPPORT_MI_SHADOW;
        if (isMaterialEffectEnabled(view, z2) && (materialConfig = sMaterial.get(z)) != null) {
            setTransparentBackground(view, z3);
            applyMaterialWithoutShadow(view, materialConfig, true);
            MaterialConfig.ColorBlendConfig colorBlendConfig = materialConfig.getColorBlendConfig();
            if (colorBlendConfig != null) {
                animateFloatingEffect(view, 0.0f, radius, 350L, z, z4, makeTransparentColors(colorBlendConfig.blendColors), colorBlendConfig.blendColors, colorBlendConfig.blendModes, null, null);
                return;
            } else {
                animateFloatingEffect(view, 0.0f, radius, 350L, z, z4, null, null, null, null, null);
                return;
            }
        }
        clearMaterialWithoutShadow(view);
        animateFloatingEffect(view, 0.0f, radius, 350L, z, z4, null, null, null, new BgColorAnimParams(z ? -1 : -13882324, z3, i, z4), null);
    }

    private static void handleAnimatedFloatingOff(final View view, boolean z, boolean z2, int i) {
        int radius = getRadius(z);
        boolean z3 = !z;
        boolean z4 = MiShadowUtils.SUPPORT_MI_SHADOW;
        if (isMaterialEffectEnabled(view, z2)) {
            MaterialConfig materialConfig = sMaterial.get(z);
            MaterialConfig.ColorBlendConfig colorBlendConfig = materialConfig != null ? materialConfig.getColorBlendConfig() : null;
            if (colorBlendConfig != null) {
                int[] iArr = colorBlendConfig.blendColors;
                animateFloatingEffect(view, radius, 0.0f, 350L, z, z4, iArr, makeTransparentColors(iArr), colorBlendConfig.blendModes, null, new Runnable() { // from class: miuix.appcompat.internal.util.FloatingEffectHelper$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        FloatingEffectHelper.$r8$lambda$ru0orCZUye_FAKvX45PPfNdl12Q(view);
                    }
                });
                return;
            } else {
                animateFloatingEffect(view, radius, 0.0f, 350L, z, z4, null, null, null, null, new Runnable() { // from class: miuix.appcompat.internal.util.FloatingEffectHelper$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        FloatingEffectHelper.$r8$lambda$Mau4FSlFGec3j3nvaUZVGYpmvDM(view);
                    }
                });
                return;
            }
        }
        animateFloatingEffect(view, radius, 0.0f, 350L, z, z4, null, null, null, new BgColorAnimParams(!z ? 0 : 16777215, z3, i, z4), new Runnable() { // from class: miuix.appcompat.internal.util.FloatingEffectHelper$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                MiShadowUtils.clearMiShadow(view);
            }
        });
    }

    public static /* synthetic */ void $r8$lambda$ru0orCZUye_FAKvX45PPfNdl12Q(View view) {
        clearMaterialWithoutShadow(view);
        MiShadowUtils.clearMiShadow(view);
    }

    public static /* synthetic */ void $r8$lambda$Mau4FSlFGec3j3nvaUZVGYpmvDM(View view) {
        clearMaterialWithoutShadow(view);
        MiShadowUtils.clearMiShadow(view);
    }

    private static void handleFloatingOn(View view, boolean z, boolean z2, int i) {
        MaterialConfig materialConfig;
        int radius = getRadius(z);
        boolean z3 = !z;
        boolean z4 = MiShadowUtils.SUPPORT_MI_SHADOW;
        if (isMaterialEffectEnabled(view, z2) && (materialConfig = sMaterial.get(z)) != null) {
            setTransparentBackground(view, z3);
            applyMaterialWithoutShadow(view, materialConfig);
            applyShadowEffect(view, radius);
        } else {
            clearMaterialWithoutShadow(view);
            applyTraditionalBackground(view, z, z3, i, z4);
            applyShadowEffect(view, radius);
        }
    }

    private static void handleFloatingOff(View view, boolean z, boolean z2) {
        clearMaterialWithoutShadow(view);
        MiShadowUtils.clearMiShadow(view);
        view.setElevation(0.0f);
        setTransparentBackground(view, !z);
    }

    private static int getRadius(boolean z) {
        if (z) {
            return ShadowToken.Regular.radius;
        }
        return 20;
    }

    private static void applyTraditionalBackground(View view, boolean z, boolean z2, int i, boolean z3) {
        int i2 = z ? -1 : -13882324;
        boolean z4 = !z3;
        Drawable background = view.getBackground();
        if (background instanceof RoundStateDrawable) {
            RoundStateDrawable roundStateDrawable = (RoundStateDrawable) background;
            roundStateDrawable.setColor(i2);
            roundStateDrawable.setDarkMode(z2);
            roundStateDrawable.setOpaqueBlend(z4);
            roundStateDrawable.setOpaqueBlendBgColor(i);
            return;
        }
        RoundStateDrawable roundStateDrawable2 = new RoundStateDrawable(i2, z2);
        roundStateDrawable2.setOpaqueBlend(z4);
        roundStateDrawable2.setOpaqueBlendBgColor(i);
        view.setBackground(roundStateDrawable2);
    }

    private static void applyShadowEffect(View view, int i) {
        if (MiShadowUtils.SUPPORT_MI_SHADOW) {
            ShadowToken shadowToken = ShadowToken.Regular;
            MiShadowUtils.setMiShadow(view, shadowToken.color, shadowToken.offsetX, shadowToken.offsetY, Math.max(i, 2.0f));
        } else {
            view.setElevation(view.getContext().getResources().getDisplayMetrics().density * 8.0f);
        }
    }

    private static void applyMaterialWithoutShadow(View view, MaterialConfig materialConfig) {
        applyMaterialWithoutShadow(view, materialConfig, false);
    }

    private static void applyMaterialWithoutShadow(View view, MaterialConfig materialConfig, boolean z) {
        float f = view.getContext().getResources().getDisplayMetrics().density;
        MaterialConfig.ColorBlendConfig colorBlendConfig = materialConfig.getColorBlendConfig();
        MaterialConfig.BlurConfig blurConfig = materialConfig.getBlurConfig();
        if (colorBlendConfig != null && blurConfig == null) {
            MiuiBlurUtils.setMixEffectEnabled(view, true);
            MiuiBlurUtils.setViewBlurMode(view, 1);
            if (!z) {
                MiuiBlurUtils.setBackgroundBlendConfig(view, colorBlendConfig.blendColors, colorBlendConfig.blendModes);
            }
        } else if (blurConfig != null) {
            MiuiBlurUtils.setMixEffectEnabled(view, false);
            MiuiBlurUtils.setBlurConfig(view, f, blurConfig);
        }
        MaterialConfig.BloomStrokeConfig bloomStrokeConfig = materialConfig.getBloomStrokeConfig();
        if (bloomStrokeConfig != null) {
            HyperBloomStrokeUtils.setBloomStrokeConfig(view, bloomStrokeConfig);
        }
    }

    private static void clearMaterialWithoutShadow(View view) {
        MiuiBlurUtils.setBlurConfig(view, 0.0f, null);
        HyperBloomStrokeUtils.clearBloomStroke(view);
    }

    private static void setTransparentBackground(View view, boolean z) {
        int i = z ? 0 : 16777215;
        Drawable background = view.getBackground();
        if (background instanceof RoundStateDrawable) {
            RoundStateDrawable roundStateDrawable = (RoundStateDrawable) background;
            roundStateDrawable.setColor(i);
            roundStateDrawable.setDarkMode(z);
            roundStateDrawable.setOpaqueBlend(false);
            return;
        }
        view.setBackground(new RoundStateDrawable(i, z));
    }

    private static FloatingAppliedState getAppliedState(View view) {
        Object tag = view.getTag(R$id.floating_applied_state_tag);
        if (tag instanceof FloatingAppliedState) {
            return (FloatingAppliedState) tag;
        }
        return null;
    }

    private static void setAppliedState(View view, FloatingAppliedState floatingAppliedState) {
        view.setTag(R$id.floating_applied_state_tag, floatingAppliedState);
    }

    class BgColorAnimParams {
        final boolean isDarkMode;
        final int opaqueBlendBgColor;
        final int targetColor;
        final boolean useMiShadow;

        BgColorAnimParams(int i, boolean z, int i2, boolean z2) {
            this.targetColor = i;
            this.isDarkMode = z;
            this.opaqueBlendBgColor = i2;
            this.useMiShadow = z2;
        }
    }

    private static int[] makeTransparentColors(int[] iArr) {
        int[] iArr2 = new int[iArr.length];
        for (int i = 0; i < iArr.length; i++) {
            iArr2[i] = iArr[i] & 16777215;
        }
        return iArr2;
    }

    private static void animateFloatingEffect(final View view, float f, float f2, long j, boolean z, boolean z2, int[] iArr, int[] iArr2, int[] iArr3, BgColorAnimParams bgColorAnimParams, final Runnable runnable) {
        if (view == null) {
            return;
        }
        cancelFloatingAnimator(view);
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(f, f2);
        valueAnimatorOfFloat.setDuration(j);
        valueAnimatorOfFloat.setInterpolator(new LinearInterpolator());
        valueAnimatorOfFloat.addUpdateListener(createUpdateListener(view, f, f2, z2, iArr, iArr2, iArr3, bgColorAnimParams));
        valueAnimatorOfFloat.addListener(new AnimatorListenerAdapter() { // from class: miuix.appcompat.internal.util.FloatingEffectHelper.1
            private boolean cancelled = false;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                this.cancelled = true;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                Runnable runnable2;
                FloatingEffectHelper.removeDetachListener(view);
                if (this.cancelled || (runnable2 = runnable) == null) {
                    return;
                }
                runnable2.run();
            }
        });
        registerDetachListener(view, valueAnimatorOfFloat);
        valueAnimatorOfFloat.start();
    }

    private static ValueAnimator.AnimatorUpdateListener createUpdateListener(final View view, final float f, final float f2, final boolean z, final int[] iArr, final int[] iArr2, int[] iArr3, BgColorAnimParams bgColorAnimParams) {
        int color;
        final int i;
        int[] iArrCopyOf = iArr3;
        final ArgbEvaluator argbEvaluator = new ArgbEvaluator();
        final float f3 = view.getContext().getResources().getDisplayMetrics().density * 8.0f;
        final boolean z2 = (iArr == null || iArr2 == null || iArrCopyOf == null || iArr.length <= 0) ? false : true;
        final int iMin = z2 ? Math.min(iArr.length, Math.min(iArr2.length, iArrCopyOf.length)) : 0;
        RoundStateDrawable roundStateDrawableEnsureBgDrawable = null;
        final int[] iArr4 = z2 ? new int[iMin] : null;
        if (z2 && iArrCopyOf.length != iMin) {
            iArrCopyOf = Arrays.copyOf(iArrCopyOf, iMin);
        }
        final int[] iArr5 = iArrCopyOf;
        if (bgColorAnimParams != null) {
            roundStateDrawableEnsureBgDrawable = ensureBgDrawable(view, bgColorAnimParams.isDarkMode, bgColorAnimParams.opaqueBlendBgColor, bgColorAnimParams.useMiShadow);
            color = roundStateDrawableEnsureBgDrawable.getColor();
            i = bgColorAnimParams.targetColor;
        } else {
            color = 0;
            i = 0;
        }
        final RoundStateDrawable roundStateDrawable = roundStateDrawableEnsureBgDrawable;
        final boolean z3 = (bgColorAnimParams == null || color == i) ? false : true;
        final int i2 = color;
        return new ValueAnimator.AnimatorUpdateListener() { // from class: miuix.appcompat.internal.util.FloatingEffectHelper$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                FloatingEffectHelper.$r8$lambda$rZSpOVebNlb_HjALqqB5CgcMBao(z, view, f, f2, f3, z2, iMin, iArr4, argbEvaluator, iArr, iArr2, iArr5, z3, i2, i, roundStateDrawable, valueAnimator);
            }
        };
    }

    public static /* synthetic */ void $r8$lambda$rZSpOVebNlb_HjALqqB5CgcMBao(boolean z, View view, float f, float f2, float f3, boolean z2, int i, int[] iArr, ArgbEvaluator argbEvaluator, int[] iArr2, int[] iArr3, int[] iArr4, boolean z3, int i2, int i3, RoundStateDrawable roundStateDrawable, ValueAnimator valueAnimator) {
        float fFloatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        float animatedFraction = valueAnimator.getAnimatedFraction();
        if (z) {
            ShadowToken shadowToken = ShadowToken.Regular;
            MiShadowUtils.setMiShadow(view, shadowToken.color, shadowToken.offsetX, shadowToken.offsetY, Math.max(fFloatValue, 2.0f));
        } else {
            float fMax = Math.max(f, f2);
            view.setElevation((fMax > 0.0f ? fFloatValue / fMax : 0.0f) * f3);
        }
        if (z2) {
            for (int i4 = 0; i4 < i; i4++) {
                iArr[i4] = ((Integer) argbEvaluator.evaluate(animatedFraction, Integer.valueOf(iArr2[i4]), Integer.valueOf(iArr3[i4]))).intValue();
            }
            MiuiBlurUtils.setBackgroundBlendConfig(view, iArr, iArr4);
        }
        if (z3) {
            roundStateDrawable.setColor(((Integer) argbEvaluator.evaluate(animatedFraction, Integer.valueOf(i2), Integer.valueOf(i3))).intValue());
        }
    }

    private static RoundStateDrawable ensureBgDrawable(View view, boolean z, int i, boolean z2) {
        RoundStateDrawable roundStateDrawable;
        Drawable background = view.getBackground();
        if (background instanceof RoundStateDrawable) {
            roundStateDrawable = (RoundStateDrawable) background;
        } else {
            RoundStateDrawable roundStateDrawable2 = new RoundStateDrawable(z ? 0 : 16777215, z);
            view.setBackground(roundStateDrawable2);
            roundStateDrawable = roundStateDrawable2;
        }
        roundStateDrawable.setDarkMode(z);
        roundStateDrawable.setOpaqueBlend(!z2);
        roundStateDrawable.setOpaqueBlendBgColor(i);
        int color = roundStateDrawable.getColor();
        if ((color >>> 24) == 0) {
            int i2 = z ? 0 : 16777215;
            if (color != i2) {
                roundStateDrawable.setColor(i2);
            }
        }
        return roundStateDrawable;
    }

    private static void registerDetachListener(View view, ValueAnimator valueAnimator) {
        View.OnAttachStateChangeListener onAttachStateChangeListener = new View.OnAttachStateChangeListener() { // from class: miuix.appcompat.internal.util.FloatingEffectHelper.2
            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewAttachedToWindow(View view2) {
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewDetachedFromWindow(View view2) {
                FloatingEffectHelper.cancelFloatingAnimator(view2);
            }
        };
        view.addOnAttachStateChangeListener(onAttachStateChangeListener);
        view.setTag(R$id.floating_animator_detach_listener_tag, onAttachStateChangeListener);
        view.setTag(R$id.floating_animator_tag, valueAnimator);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void cancelFloatingAnimator(View view) {
        Object tag = view.getTag(R$id.floating_animator_tag);
        if (tag instanceof ValueAnimator) {
            ((ValueAnimator) tag).cancel();
            view.setTag(R$id.floating_animator_tag, null);
            setAppliedState(view, null);
        }
        removeDetachListener(view);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void removeDetachListener(View view) {
        Object tag = view.getTag(R$id.floating_animator_detach_listener_tag);
        if (tag instanceof View.OnAttachStateChangeListener) {
            view.removeOnAttachStateChangeListener((View.OnAttachStateChangeListener) tag);
            view.setTag(R$id.floating_animator_detach_listener_tag, null);
        }
    }

    public static void clearElevation(View view) {
        if (view == null) {
            return;
        }
        cancelFloatingAnimator(view);
        clearMaterialWithoutShadow(view);
        MiShadowUtils.clearMiShadow(view);
        view.setElevation(0.0f);
        setAppliedState(view, null);
    }
}
