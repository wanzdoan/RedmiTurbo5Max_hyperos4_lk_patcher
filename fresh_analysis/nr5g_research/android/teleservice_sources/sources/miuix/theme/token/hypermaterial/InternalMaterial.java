package miuix.theme.token.hypermaterial;

import miuix.theme.token.BloomStrokeToken;
import miuix.theme.token.ColorBlendToken;
import miuix.theme.token.MaterialDayNightToken;
import miuix.theme.token.MaterialToken;
import miuix.theme.token.ShadowToken;

/* JADX INFO: loaded from: classes2.dex */
public abstract class InternalMaterial {
    public static MaterialToken Pured_Thin_Glass_Light = new MaterialToken.Builder(10, "internal-pured-thin-glass", "light").setColorBlend(ColorBlendToken.Pured_Thin_Glass_Light).setMaskBlur(20).setBloomStroke(BloomStrokeToken.Glass_Stroke_Middle_Light).setShadow(ShadowToken.Regular).build();
    public static MaterialToken Pured_Thin_Glass_Dark = new MaterialToken.Builder(10, "internal-pured-thin-glass", "dark").setColorBlend(ColorBlendToken.Pured_Thin_Glass_Dark).setMaskBlur(20).setBloomStroke(BloomStrokeToken.Glass_Stroke_Middle_Dark).setShadow(ShadowToken.Regular).build();
    public static MaterialDayNightToken Pured_Thin_Glass = new MaterialDayNightToken(Pured_Thin_Glass_Light, Pured_Thin_Glass_Dark);
}
