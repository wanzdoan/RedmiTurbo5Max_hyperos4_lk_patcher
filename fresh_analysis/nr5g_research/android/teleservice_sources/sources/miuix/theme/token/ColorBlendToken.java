package miuix.theme.token;

/* JADX INFO: loaded from: classes2.dex */
public class ColorBlendToken {
    public static ColorBlendToken Colored_Brown;
    public static ColorBlendToken Colored_DeepBlue;
    public static ColorBlendToken Colored_Extra_Thick_Dark;
    public static ColorBlendToken Colored_Extra_Thick_Light;
    public static ColorBlendToken Colored_Extra_Thin_Dark;
    public static ColorBlendToken Colored_Extra_Thin_Light;
    public static ColorBlendToken Colored_Orange;
    public static ColorBlendToken Colored_Purple;
    public static ColorBlendToken Colored_Regular_Dark;
    public static ColorBlendToken Colored_Regular_Light;
    public static ColorBlendToken Colored_Thick_Dark;
    public static ColorBlendToken Colored_Thick_Light;
    public static ColorBlendToken Colored_Thin_Dark;
    public static ColorBlendToken Colored_Thin_Light;
    public static ColorBlendToken Colored_Yellow;
    public static ColorBlendToken ExtraHeavy_Dark;
    public static ColorBlendToken ExtraHeavy_Light;
    public static ColorBlendToken Heavy_Dark;
    public static ColorBlendToken Heavy_Light;
    public static ColorBlendToken Info_Colored_Regular;
    public static ColorBlendToken Info_Extra_Thin_Dark;
    public static ColorBlendToken Info_Extra_Thin_Light;
    public static ColorBlendToken Info_Regular_Dark;
    public static ColorBlendToken Info_Regular_Light;
    public static ColorBlendToken Info_Thick_Dark;
    public static ColorBlendToken Info_Thick_Light;
    public static ColorBlendToken Info_Thin_Dark;
    public static ColorBlendToken Info_Thin_Light;
    public static ColorBlendToken Overlay_Extra_Thick_Light;
    public static ColorBlendToken Overlay_Extra_Thin_Dark;
    public static ColorBlendToken Overlay_Extra_Thin_Light;
    public static ColorBlendToken Overlay_Regular_Light;
    public static ColorBlendToken Overlay_Thick_Dark;
    public static ColorBlendToken Overlay_Thick_Light;
    public static ColorBlendToken Overlay_Thin_Light;
    public static ColorBlendToken Pured_Extra_Thick_Dark;
    public static ColorBlendToken Pured_Extra_Thick_Light;
    public static ColorBlendToken Pured_Extra_Thin_Dark;
    public static ColorBlendToken Pured_Extra_Thin_Light;
    public static ColorBlendToken Pured_Regular_Dark;
    public static ColorBlendToken Pured_Regular_Glass_Dark;
    public static ColorBlendToken Pured_Regular_Glass_Light;
    public static ColorBlendToken Pured_Regular_Light;
    public static ColorBlendToken Pured_Thick_Dark;
    public static ColorBlendToken Pured_Thick_Glass_Dark;
    public static ColorBlendToken Pured_Thick_Glass_Light;
    public static ColorBlendToken Pured_Thick_Light;
    public static ColorBlendToken Pured_Thin_Dark;
    public static ColorBlendToken Pured_Thin_Glass_Dark;
    public static ColorBlendToken Pured_Thin_Glass_Light;
    public static ColorBlendToken Pured_Thin_Light;
    public int[] blendModes;
    public int[] colors;
    public float[] extraBlendParams;
    public int[] fallbackBlendModes;
    public int[] fallbackColors;
    public float[] fallbackExtraBlendParams;

    static {
        BlendModeToken blendModeToken = BlendModeToken.PLUS_DARKER;
        Info_Extra_Thin_Light = new Builder().setConfig(new int[]{1001501105}, new int[]{blendModeToken.value}).build();
        BlendModeToken blendModeToken2 = BlendModeToken.PLUS_LIGHTER;
        Info_Extra_Thin_Dark = new Builder().setConfig(new int[]{1001501105}, new int[]{blendModeToken2.value}).build();
        Info_Thin_Light = new Builder().setConfig(new int[]{-2137483112}, new int[]{blendModeToken2.value}).build();
        Info_Thin_Dark = new Builder().setConfig(new int[]{-2137483112}, new int[]{blendModeToken.value}).build();
        Info_Regular_Light = new Builder().setConfig(new int[]{-1281845096}, new int[]{blendModeToken2.value}).build();
        Info_Regular_Dark = new Builder().setConfig(new int[]{-1281845096}, new int[]{blendModeToken.value}).build();
        Info_Thick_Light = new Builder().setConfig(new int[]{-6776680}, new int[]{blendModeToken2.value}).build();
        Info_Thick_Dark = new Builder().setConfig(new int[]{-6776680}, new int[]{blendModeToken.value}).build();
        BlendModeToken blendModeToken3 = BlendModeToken.COLOR_DODGE;
        Info_Colored_Regular = new Builder().setConfig(new int[]{-6579301, 268435455}, new int[]{blendModeToken3.value, blendModeToken2.value}).build();
        BlendModeToken blendModeToken4 = BlendModeToken.OVERLAY;
        int i = blendModeToken4.value;
        int i2 = blendModeToken3.value;
        BlendModeToken blendModeToken5 = BlendModeToken.SRC_OVER;
        Colored_Extra_Thin_Light = new Builder().setConfig(new int[]{-2140772762, 653390321, 449958353}, new int[]{i, i2, blendModeToken5.value}).build();
        BlendModeToken blendModeToken6 = BlendModeToken.COLOR_BURN;
        Colored_Extra_Thin_Dark = new Builder().setConfig(new int[]{-942879540, 777146962}, new int[]{blendModeToken6.value, blendModeToken5.value}).build();
        int i3 = blendModeToken4.value;
        BlendModeToken blendModeToken7 = BlendModeToken.SOFT_LIGHT;
        Colored_Thin_Light = new Builder().setConfig(new int[]{-1719697537, -2130772226}, new int[]{i3, blendModeToken7.value}).build();
        Colored_Thin_Dark = new Builder().setConfig(new int[]{442918502, 861427800}, new int[]{blendModeToken6.value, blendModeToken.value}).setFallbackConfig(new int[]{694576742, 1293687836, 436207616}, new int[]{blendModeToken6.value, blendModeToken4.value, blendModeToken5.value}).build();
        Colored_Regular_Light = new Builder().setConfig(new int[]{-2052675930, 486539263}, new int[]{blendModeToken4.value, blendModeToken2.value}).setFallbackConfig(new int[]{-1720355467, 1640022208, 1493172224}, new int[]{blendModeToken4.value, blendModeToken6.value, blendModeToken5.value}).build();
        Colored_Regular_Dark = new Builder().setConfig(new int[]{1879048192, 335544320}, new int[]{blendModeToken4.value, blendModeToken5.value}).build();
        Colored_Thick_Light = new Builder().setConfig(new int[]{-428575628, -1722658222, 869388753}, new int[]{blendModeToken4.value, blendModeToken3.value, blendModeToken5.value}).build();
        Colored_Thick_Dark = new Builder().setConfig(new int[]{1719105399, 863270004, 855638016}, new int[]{blendModeToken6.value, blendModeToken4.value, blendModeToken5.value}).build();
        Colored_Extra_Thick_Light = new Builder().setConfig(new int[]{1301254031, 1807794368}, new int[]{blendModeToken2.value, blendModeToken3.value}).setFallbackConfig(new int[]{1305464783, 1388166589, 1895167477}, new int[]{blendModeToken3.value, blendModeToken4.value, blendModeToken5.value}).build();
        Colored_Extra_Thick_Dark = new Builder().setConfig(new int[]{1718973813, 1640022208}, new int[]{blendModeToken.value, blendModeToken6.value}).setFallbackConfig(new int[]{1299543413, 1640022208, 1493172224}, new int[]{blendModeToken4.value, blendModeToken6.value, blendModeToken5.value}).build();
        Colored_Orange = new Builder().setConfig(new int[]{-1325569755, 452984831}, new int[]{blendModeToken5.value, blendModeToken4.value}).build();
        Colored_Purple = new Builder().setConfig(new int[]{-866563329, 788529151}, new int[]{blendModeToken5.value, blendModeToken4.value}).build();
        int i4 = blendModeToken5.value;
        Colored_DeepBlue = new Builder().setConfig(new int[]{1298300603, -867014968, 486539263}, new int[]{i4, i4, blendModeToken4.value}).build();
        Colored_Yellow = new Builder().setConfig(new int[]{-1409301745, 738182943, 352321535}, new int[]{blendModeToken5.value, blendModeToken4.value, blendModeToken2.value}).build();
        Colored_Brown = new Builder().setConfig(new int[]{871426276, -868931299}, new int[]{blendModeToken5.value, blendModeToken4.value}).build();
        Pured_Extra_Thin_Light = new Builder().setConfig(new int[]{-855638017, 1593835519, 620756991}, new int[]{blendModeToken6.value, blendModeToken2.value, blendModeToken5.value}).setFallbackConfig(new int[]{-855638017, -855638017, 1811939327}, new int[]{blendModeToken6.value, blendModeToken4.value, blendModeToken5.value}).build();
        Pured_Extra_Thin_Dark = new Builder().setConfig(new int[]{-428838800, -871099372}, new int[]{blendModeToken4.value, blendModeToken5.value}).build();
        int i5 = blendModeToken2.value;
        Pured_Thin_Light = new Builder().setConfig(new int[]{812477805, 1593835519, 1728053247}, new int[]{i5, i5, blendModeToken5.value}).setFallbackConfig(new int[]{-1284674195, 1728053247, -1459617793}, new int[]{blendModeToken3.value, blendModeToken4.value, blendModeToken5.value}).build();
        Pured_Thin_Dark = new Builder().setConfig(new int[]{-1803715203, 1711276032}, new int[]{blendModeToken.value, blendModeToken5.value}).setFallbackConfig(new int[]{-1552056963, 864520071, 1761607680}, new int[]{blendModeToken6.value, blendModeToken4.value, blendModeToken5.value}).build();
        int i6 = blendModeToken4.value;
        BlendModeToken blendModeToken8 = BlendModeToken.HARD_LIGHT;
        Pured_Regular_Light = new Builder().setConfig(new int[]{872020473, -1275068417}, new int[]{i6, blendModeToken8.value}).build();
        Pured_Regular_Dark = new Builder().setConfig(new int[]{1962934272, 1375731712}, new int[]{blendModeToken6.value, blendModeToken5.value}).build();
        Pured_Thick_Light = new Builder().setConfig(new int[]{-1711276033, -1073741825}, new int[]{blendModeToken4.value, blendModeToken5.value}).build();
        Pured_Thick_Dark = new Builder().setConfig(new int[]{1291845632, -2146299374}, new int[]{blendModeToken6.value, blendModeToken5.value}).build();
        Pured_Extra_Thick_Light = new Builder().setConfig(new int[]{1728053247, -1711933963}, new int[]{blendModeToken2.value, blendModeToken5.value}).setFallbackConfig(new int[]{-2130706433, 1728053247, -1711933963}, new int[]{blendModeToken4.value, blendModeToken3.value, blendModeToken5.value}).build();
        BlendModeToken blendModeToken9 = BlendModeToken.LUMINOSITY;
        Pured_Extra_Thick_Dark = new Builder().setConfig(new int[]{-870770407, 1412510001}, new int[]{blendModeToken9.value, blendModeToken2.value}).setFallbackConfig(new int[]{-1037687258, 1415733858}, new int[]{blendModeToken9.value, blendModeToken3.value}).build();
        Pured_Thin_Glass_Light = new Builder().setConfig(new int[]{83886080, -1711276033, 1728053247}, new int[]{blendModeToken.value, blendModeToken7.value, blendModeToken8.value}).build();
        Pured_Thin_Glass_Dark = new Builder().setConfig(new int[]{436207616, 1716934230, -1723908289}, new int[]{blendModeToken.value, blendModeToken9.value, blendModeToken4.value}).build();
        Pured_Regular_Glass_Light = new Builder().setConfig(new int[]{83886080, -1711276033, -1711276033}, new int[]{blendModeToken.value, blendModeToken7.value, blendModeToken8.value}).build();
        Pured_Regular_Glass_Dark = new Builder().setConfig(new int[]{436207616, -1720947604, -1727658490}, new int[]{blendModeToken.value, blendModeToken9.value, blendModeToken7.value}).build();
        Pured_Thick_Glass_Light = new Builder().setConfig(new int[]{637534208, -855638017, -855638017}, new int[]{blendModeToken.value, blendModeToken7.value, blendModeToken8.value}).build();
        Pured_Thick_Glass_Dark = new Builder().setConfig(new int[]{436207616, -1722329257, -1727658490}, new int[]{blendModeToken.value, blendModeToken9.value, blendModeToken7.value}).build();
        Overlay_Extra_Thin_Light = new Builder().setConfig(new int[]{261724569}, new int[]{blendModeToken9.value}).build();
        Overlay_Extra_Thin_Dark = new Builder().setConfig(new int[]{1965500199}, new int[]{blendModeToken9.value}).build();
        Overlay_Thin_Light = new Builder().setConfig(new int[]{1301714582, 442918502}, new int[]{blendModeToken9.value, blendModeToken.value}).setFallbackConfig(new int[]{1301714582, 862348902}, new int[]{blendModeToken9.value, blendModeToken6.value}).build();
        Overlay_Regular_Light = new Builder().setConfig(new int[]{1301714582, 436207616}, new int[]{blendModeToken9.value, blendModeToken.value}).setFallbackConfig(new int[]{864191106, 721420288}, new int[]{blendModeToken9.value, blendModeToken6.value}).build();
        Overlay_Thick_Light = new Builder().setConfig(new int[]{-1474355425, -6645094}, new int[]{blendModeToken9.value, blendModeToken4.value}).build();
        Overlay_Thick_Dark = new Builder().setConfig(new int[]{1723052979, -1637456282}, new int[]{blendModeToken9.value, blendModeToken.value}).setFallbackConfig(new int[]{1723052979, -1634824562, 1577058304}, new int[]{blendModeToken9.value, blendModeToken6.value, blendModeToken5.value}).build();
        Overlay_Extra_Thick_Light = new Builder().setConfig(new int[]{-866033311, 1291845632}, new int[]{blendModeToken9.value, blendModeToken6.value}).build();
        ExtraHeavy_Light = new Builder().setConfig(new int[]{-1889443744, -1543503873}, new int[]{blendModeToken3.value, blendModeToken5.value}).build();
        int i7 = blendModeToken6.value;
        int i8 = blendModeToken5.value;
        ExtraHeavy_Dark = new Builder().setConfig(new int[]{1970500467, -1979711488, 184549375}, new int[]{i7, i8, i8}).build();
        Heavy_Light = new Builder().setConfig(new int[]{-1502909589, -856295947}, new int[]{blendModeToken3.value, blendModeToken5.value}).build();
        Heavy_Dark = new Builder().setConfig(new int[]{-2141430692, -1088479457}, new int[]{blendModeToken6.value, blendModeToken5.value}).build();
    }

    public class Builder {
        private final ColorBlendToken mToken = new ColorBlendToken();

        public Builder setConfig(int[] iArr, int[] iArr2) {
            ColorBlendToken colorBlendToken = this.mToken;
            colorBlendToken.colors = iArr;
            colorBlendToken.blendModes = iArr2;
            colorBlendToken.extraBlendParams = null;
            return this;
        }

        public Builder setFallbackConfig(int[] iArr, int[] iArr2) {
            ColorBlendToken colorBlendToken = this.mToken;
            colorBlendToken.fallbackColors = iArr;
            colorBlendToken.fallbackBlendModes = iArr2;
            colorBlendToken.fallbackExtraBlendParams = null;
            return this;
        }

        public ColorBlendToken build() {
            return this.mToken;
        }
    }
}
