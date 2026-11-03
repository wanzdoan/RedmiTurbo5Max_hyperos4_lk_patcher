package com.android.phone.utils;

import miuix.animation.Folme;
import miuix.animation.FolmeEase;
import miuix.animation.IStateStyle;
import miuix.animation.base.AnimConfig;
import miuix.animation.property.FloatProperty;
import miuix.miuixbasewidget.widget.CallOvalImageButton;
import miuix.theme.token.GlassToken;

/* JADX INFO: loaded from: classes.dex */
public final class PressLightAnimator {
    private float mProgress;
    private float[] mShaderParams;
    private IStateStyle mStateStyle;
    private final TokenProvider mTokenProvider;
    private static final FloatProperty PRESS_LIGHT_PROGRESS = new FloatProperty("press_light_progress", 0.00390625f) { // from class: com.android.phone.utils.PressLightAnimator.1
        @Override // miuix.animation.property.FloatProperty
        public float getValue(PressLightAnimator pressLightAnimator) {
            if (pressLightAnimator == null) {
                return 0.0f;
            }
            return pressLightAnimator.mProgress;
        }

        @Override // miuix.animation.property.FloatProperty
        public void setValue(PressLightAnimator pressLightAnimator, float f) {
            if (pressLightAnimator != null) {
                pressLightAnimator.setProgress(f);
            }
        }
    };
    private static final AnimConfig DOWN_CONFIG = new AnimConfig().setEase(FolmeEase.spring(1.0f, 0.2f));
    private static final AnimConfig UP_CONFIG = new AnimConfig().setEase(FolmeEase.spring(0.95f, 0.35f));

    public interface TokenProvider {
        CallOvalImageButton getGlassView();

        GlassToken getNormalToken();

        GlassToken getPressToken();
    }

    public PressLightAnimator(TokenProvider tokenProvider) {
        this.mTokenProvider = tokenProvider;
    }

    public void animate(boolean z) {
        TokenProvider tokenProvider = this.mTokenProvider;
        if (tokenProvider == null || tokenProvider.getGlassView() == null || !MaterialUtils.isHyperGlassEnable()) {
            return;
        }
        if (this.mStateStyle == null) {
            this.mStateStyle = Folme.useValue(this);
        }
        this.mStateStyle.to(PRESS_LIGHT_PROGRESS, Float.valueOf(z ? 1.0f : 0.0f), z ? DOWN_CONFIG : UP_CONFIG);
    }

    public void reset() {
        IStateStyle iStateStyle = this.mStateStyle;
        if (iStateStyle != null) {
            iStateStyle.cancel();
        }
        setProgress(0.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setProgress(float f) {
        float fMax = Math.max(0.0f, Math.min(1.0f, f));
        if (this.mProgress == fMax) {
            return;
        }
        this.mProgress = fMax;
        applyProgress(fMax);
    }

    private void applyProgress(float f) {
        CallOvalImageButton glassView;
        TokenProvider tokenProvider = this.mTokenProvider;
        if (tokenProvider == null || (glassView = tokenProvider.getGlassView()) == null) {
            return;
        }
        GlassToken normalToken = this.mTokenProvider.getNormalToken();
        GlassToken pressToken = this.mTokenProvider.getPressToken();
        if (normalToken == null || pressToken == null) {
            return;
        }
        float[] shaderParams = normalToken.toShaderParams();
        float[] shaderParams2 = pressToken.toShaderParams();
        if (shaderParams == null || shaderParams2 == null || shaderParams.length != shaderParams2.length) {
            return;
        }
        float[] fArr = this.mShaderParams;
        if (fArr == null || fArr.length != shaderParams.length) {
            this.mShaderParams = new float[shaderParams.length];
        }
        int i = 0;
        while (true) {
            int length = shaderParams.length;
            float[] fArr2 = this.mShaderParams;
            if (i < length) {
                float f2 = shaderParams[i];
                fArr2[i] = f2 + ((shaderParams2[i] - f2) * f);
                i++;
            } else {
                glassView.setGlassShaderParams(fArr2);
                return;
            }
        }
    }
}
