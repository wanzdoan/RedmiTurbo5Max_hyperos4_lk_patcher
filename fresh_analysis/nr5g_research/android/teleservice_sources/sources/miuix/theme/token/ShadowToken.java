package miuix.theme.token;

/* JADX INFO: loaded from: classes2.dex */
public class ShadowToken {
    public int color;
    public float dispersion = 0.5f;
    public int offsetX;
    public int offsetY;
    public int radius;
    public static ShadowToken Low = new Builder().setConfig(1493172224, 0, 44).build();
    public static ShadowToken Regular = new Builder().setConfig(1493172224, 2, 64).build();
    public static ShadowToken High = new Builder().setConfig(1493172224, 4, 80).build();
    public static ShadowToken ExtraHigh = new Builder().setConfig(1493172224, 70, 96).build();
    public static ShadowToken Float = new Builder().setConfig(1493172224, 0, 44).build();

    public class Builder {
        private final ShadowToken mToken = new ShadowToken();

        public Builder setConfig(int i, int i2, int i3) {
            ShadowToken shadowToken = this.mToken;
            shadowToken.color = i;
            shadowToken.offsetX = 0;
            shadowToken.offsetY = i2;
            shadowToken.radius = i3;
            return this;
        }

        public ShadowToken build() {
            return this.mToken;
        }
    }
}
