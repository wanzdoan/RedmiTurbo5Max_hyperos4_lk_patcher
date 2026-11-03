package com.android.phone.utils;

import miuix.theme.token.GlassToken;

/* JADX INFO: loaded from: classes.dex */
public abstract class MaterialGlassToken {
    public static GlassToken create(float[] fArr, float f, float f2, float f3, float f4, float[] fArr2, float f5, float[] fArr3, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float[] fArr4, float f16, float f17, float f18, float f19, float f20, float f21, float f22, float f23, float f24, float f25, float f26, float f27, float[] fArr5, int i, int i2) {
        return new GlassToken.Builder().blend(new GlassToken.Blend.Builder().luminanceValues(fArr).luminanceAmount(f).saturation(f2).brightness(f3).darker(f4).darkerRange(fArr2).build()).inner(new GlassToken.Inner.Builder().bottom(f5).color(fArr3).colorWhite(f6).colorMix(f7).colorPow(f8).alpha(f9).build()).shape(new GlassToken.Shape.Builder().edge(f10).edgePow(f11).thickness(f12).reflectOffset(f13).build()).reflect(new GlassToken.Reflect.Builder().lighten(f14).strength(f15).build()).directionalLight(new GlassToken.DirectionalLight.Builder().direction(fArr4).intensity(f16).oppositeIntensity(f17).angleRange(f18).edgePow(f19).build()).refract(new GlassToken.Refract.Builder().ior(f20).build()).blurBg(new GlassToken.BlurBg.Builder().saturation(f21).brightness(f22).burn(f23).unShade(f24).build()).mask(new GlassToken.Mask.Builder().pressLightCenterFall(f25).pressLightCenterStrength(f26).pressLightCenterPeak(f27).pressRingStrength(fArr5).build()).blurRadius(new GlassToken.BlurRadius.Builder().smallBlurRadius(i).bigBlurRadius(i2).build()).build();
    }
}
