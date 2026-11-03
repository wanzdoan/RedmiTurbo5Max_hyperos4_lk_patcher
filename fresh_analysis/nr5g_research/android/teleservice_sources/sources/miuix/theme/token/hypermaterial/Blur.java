package miuix.theme.token.hypermaterial;

import miuix.theme.token.ColorBlendToken;
import miuix.theme.token.MaterialDayNightToken;
import miuix.theme.token.MaterialToken;

/* JADX INFO: loaded from: classes2.dex */
public abstract class Blur {
    public static MaterialToken ExtraHeavy_Light = new MaterialToken.Builder(10, "blur-extraheavy", "light").setColorBlend(ColorBlendToken.ExtraHeavy_Light).setBlur(1, 1, 0, 66).build();
    public static MaterialToken ExtraHeavy_Dark = new MaterialToken.Builder(10, "blur-extraheavy", "dark").setColorBlend(ColorBlendToken.ExtraHeavy_Dark).setBlur(1, 1, 0, 66).build();
    public static MaterialDayNightToken ExtraHeavy = new MaterialDayNightToken(ExtraHeavy_Light, ExtraHeavy_Dark);
    public static MaterialToken Heavy_Light = new MaterialToken.Builder(10, "blur-heavy", "light").setColorBlend(ColorBlendToken.Heavy_Light).setBlur(1, 1, 0, 66).build();
    public static MaterialToken Heavy_Dark = new MaterialToken.Builder(10, "blur-heavy", "dark").setColorBlend(ColorBlendToken.Heavy_Dark).setBlur(1, 1, 0, 66).build();
    public static MaterialDayNightToken Heavy = new MaterialDayNightToken(Heavy_Light, Heavy_Dark);
}
