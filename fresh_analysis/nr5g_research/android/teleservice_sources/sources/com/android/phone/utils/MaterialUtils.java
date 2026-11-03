package com.android.phone.utils;

import android.content.Context;
import android.util.Log;
import com.android.phone.PhoneGlobals;
import miuix.core.util.HyperMaterialUtils;
import miuix.theme.token.BlendModeToken;
import miuix.theme.token.ColorBlendToken;
import miuix.theme.token.GlassToken;
import miuix.theme.token.MaterialToken;

/* JADX INFO: loaded from: classes.dex */
public abstract class MaterialUtils {
    public static final GlassToken Glass_Bionic_Medium_Bold_Motion;
    public static final GlassToken Glass_Bionic_Medium_Bold_Motion_Press;
    public static MaterialToken Glass_Colored_Extra_Thin_Dark;
    public static MaterialToken Glass_Green_Light;
    public static final GlassToken Glass_Tint_Green_Motion;
    public static final GlassToken Glass_Tint_Green_Motion_Press;
    private static final int MASK_GLASS_BLUR_RADIUS_DP;
    public static MaterialToken Mask_Glass_Light;
    public static MaterialToken Mask_Light = new MaterialToken.Builder(10, "system-mask", "light").setColorBlend(new ColorBlendToken.Builder().setConfig(new int[]{1384153216}, new int[]{BlendModeToken.LUMINOSITY.value}).build()).setMaskBlur(44).build();
    private static boolean mIsHyperGlassEnable;
    private static boolean mIsHyperMaterialEnable;

    static {
        int iPx2dip = px2dip(110.0f);
        MASK_GLASS_BLUR_RADIUS_DP = iPx2dip;
        Mask_Glass_Light = new MaterialToken.Builder(10, "system-mask", "light").setColorBlend(new ColorBlendToken.Builder().setConfig(new int[]{-6710887, -15892480}, new int[]{BlendModeToken.SOFT_LIGHT.value, BlendModeToken.LAB.value}).build()).setMaskBlur(iPx2dip).build();
        Glass_Bionic_Medium_Bold_Motion_Press = MaterialGlassToken.create(new float[]{0.0f, 0.0f, 0.09f, 0.8f}, 0.24f, 1.4f, 0.14f, 0.14f, new float[]{0.6f, 1.0f}, 0.03f, new float[]{1.0f, 1.0f, 1.0f, 0.6f}, 0.2f, 0.3f, 1.0f, 1.0f, 35.0f, 2.4f, 80.0f, 1200.0f, 0.6f, 0.8f, new float[]{-0.4f, 0.6f, -0.8f}, 1.0f, 0.8f, 1.4f, 1.0f, 4.0f, 2.6f, 0.0f, 0.0f, 0.0f, 1.3f, 1.4f, 0.08f, new float[]{0.08f, 0.08f}, 120, 120);
        GlassToken glassTokenCreate = MaterialGlassToken.create(new float[]{0.0f, 0.0f, 0.09f, 0.8f}, 0.24f, 1.4f, 0.14f, 0.14f, new float[]{0.6f, 1.0f}, 0.03f, new float[]{1.0f, 1.0f, 1.0f, 0.6f}, 0.2f, 0.3f, 1.0f, 1.0f, 35.0f, 2.4f, 80.0f, 1200.0f, 0.6f, 0.8f, new float[]{-0.4f, 0.6f, -0.8f}, 1.0f, 0.8f, 1.4f, 1.0f, 4.0f, 2.6f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, new float[]{0.0f, 0.0f}, 120, 120);
        Glass_Bionic_Medium_Bold_Motion = glassTokenCreate;
        GlassToken glassTokenCreate2 = MaterialGlassToken.create(new float[]{0.0f, 0.4f, 1.2f, 0.54f}, 0.3f, 2.0f, 0.2f, 0.6f, new float[]{0.6f, 1.0f}, 0.06f, new float[]{0.25f, 0.84f, 0.35f, 1.0f}, 0.3f, 1.0f, 1.2f, 1.0f, 60.0f, 4.0f, 80.0f, 1200.0f, 1.2f, 1.0f, new float[]{-0.4f, 0.6f, -0.8f}, 0.9f, 0.7f, 1.4f, 1.0f, 4.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, new float[]{0.0f, 0.0f}, 120, 120);
        Glass_Tint_Green_Motion = glassTokenCreate2;
        Glass_Tint_Green_Motion_Press = MaterialGlassToken.create(new float[]{0.0f, 0.4f, 1.2f, 0.54f}, 0.3f, 2.0f, 0.2f, 0.6f, new float[]{0.6f, 1.0f}, 0.06f, new float[]{0.25f, 0.84f, 0.35f, 1.0f}, 0.3f, 1.0f, 1.2f, 1.0f, 60.0f, 4.0f, 80.0f, 1200.0f, 1.2f, 1.0f, new float[]{-0.4f, 0.6f, -0.8f}, 0.9f, 0.7f, 1.4f, 1.0f, 4.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.3f, 0.8f, 0.08f, new float[]{0.04f, 0.04f}, 120, 120);
        Glass_Colored_Extra_Thin_Dark = new MaterialToken.Builder(10, "frosted-colored-extra-thin", "dark").setColorBlend(ColorBlendToken.Colored_Extra_Thin_Dark).setElementBlur().setGlass(glassTokenCreate).build();
        Glass_Green_Light = new MaterialToken.Builder(10, "glass_green_light", "light").setGlass(glassTokenCreate2).build();
    }

    public static void updateIsHyperMaterialEnable(Context context) {
        mIsHyperMaterialEnable = HyperMaterialUtils.isFeatureEnable(context);
        mIsHyperGlassEnable = HyperMaterialUtils.isSupportGlass() && HyperMaterialUtils.isEnableBionics(context);
        Log.i("MaterialUtils", "updateIsHyperMaterialEnable, " + mIsHyperMaterialEnable + ", " + mIsHyperGlassEnable);
    }

    public static boolean isHyperMaterialEnable() {
        return mIsHyperMaterialEnable;
    }

    public static boolean isHyperGlassEnable() {
        return mIsHyperGlassEnable;
    }

    public static int px2dip(float f) {
        PhoneGlobals phoneGlobals;
        try {
            phoneGlobals = PhoneGlobals.getInstance();
        } catch (Exception e) {
            Log.e("MaterialUtils", "No PhoneGlobals here!", e);
            phoneGlobals = null;
        }
        return phoneGlobals == null ? (int) f : (int) ((f / phoneGlobals.getResources().getDisplayMetrics().density) + 0.5f);
    }
}
