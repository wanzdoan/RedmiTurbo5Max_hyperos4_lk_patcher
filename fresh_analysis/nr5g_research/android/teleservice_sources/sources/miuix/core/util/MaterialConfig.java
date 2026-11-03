package miuix.core.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/* JADX INFO: loaded from: classes2.dex */
public class MaterialConfig {
    private BloomStrokeConfig mBloomStrokeConfig;
    private BlurConfig mBlurConfig;
    private ColorBlendConfig mColorBlendConfig;
    private ColorBlendConfig mFallbackColorBlendConfig;
    private ShadowConfig mShadowConfig;
    public final String token;
    public final String tokenVariant;
    public final int version;
    private GlassConfig mGlassConfig = null;
    private int mGlassSmallBlurRadius = 0;
    private int mGlassBigBlurRadius = 0;

    public MaterialConfig(Parcel parcel) {
        int i = parcel.readInt();
        this.version = i;
        String string = parcel.readString();
        this.token = string;
        String string2 = parcel.readString();
        this.tokenVariant = string2;
        if (i <= 40) {
            parseCurrentVersion(parcel);
        } else {
            Log.e("MaterialConfig", String.format("Error! Can't read this token: the support version is %d, your token version is %d, tokenInfo:%s_%s", 40, Integer.valueOf(i), string, string2));
        }
    }

    public ColorBlendConfig getColorBlendConfig() {
        return this.mColorBlendConfig;
    }

    public BlurConfig getBlurConfig() {
        return this.mBlurConfig;
    }

    public ShadowConfig getShadowConfig() {
        return this.mShadowConfig;
    }

    public BloomStrokeConfig getBloomStrokeConfig() {
        return this.mBloomStrokeConfig;
    }

    public GlassConfig getGlassConfig() {
        return this.mGlassConfig;
    }

    public static MaterialConfig create(Parcelable parcelable) {
        if (parcelable == null) {
            return null;
        }
        Parcel parcelObtain = Parcel.obtain();
        parcelObtain.setDataPosition(0);
        try {
            parcelable.writeToParcel(parcelObtain, 0);
            parcelObtain.setDataPosition(0);
            MaterialConfig materialConfig = new MaterialConfig(parcelObtain);
            parcelObtain.recycle();
            return materialConfig;
        } catch (Exception unused) {
            parcelObtain.recycle();
            return null;
        }
    }

    private void parseCurrentVersion(Parcel parcel) {
        parseVersionBelow30(parcel);
    }

    private void parseVersionBelow30(Parcel parcel) {
        parseColorBlendConfig(parcel);
        parseBlurConfig(parcel);
        parseShadowConfig(parcel);
        parseBloomStrokeConfig(parcel);
        parseGlassConfig(parcel);
    }

    private void parseColorBlendConfig(Parcel parcel) {
        if (Math.max(0, parcel.readInt()) == 0) {
            return;
        }
        this.mColorBlendConfig = readColorBlendConfigFromParcel(parcel);
        if (Math.max(0, parcel.readInt()) > 0) {
            this.mFallbackColorBlendConfig = readColorBlendConfigFromParcel(parcel);
        }
    }

    private ColorBlendConfig readColorBlendConfigFromParcel(Parcel parcel) {
        return new ColorBlendConfig(readIntArrayFromParcel(parcel), readIntArrayFromParcel(parcel), readFloatArrayFromParcel(parcel));
    }

    private void parseBlurConfig(Parcel parcel) {
        ColorBlendConfig colorBlendConfig;
        if (Math.max(0, parcel.readInt()) == 0) {
            this.mBlurConfig = null;
            return;
        }
        int i = parcel.readInt();
        int i2 = parcel.readInt();
        int i3 = parcel.readInt();
        int i4 = parcel.readInt();
        int i5 = parcel.readInt();
        float[] floatArrayFromParcel = readFloatArrayFromParcel(parcel);
        if (this.version <= RomUtils.getHyperOsVersion() * 10) {
            colorBlendConfig = this.mColorBlendConfig;
        } else {
            colorBlendConfig = this.mFallbackColorBlendConfig;
        }
        this.mBlurConfig = new BlurConfig(i, i2, i3, i4, i5, floatArrayFromParcel, colorBlendConfig);
    }

    private void parseShadowConfig(Parcel parcel) {
        if (Math.max(0, parcel.readInt()) == 0) {
            this.mShadowConfig = null;
        } else {
            this.mShadowConfig = new ShadowConfig(parcel.readInt(), parcel.readFloat(), parcel.readFloat(), parcel.readFloat(), parcel.readFloat());
        }
    }

    private void parseBloomStrokeConfig(Parcel parcel) {
        if (Math.max(0, parcel.readInt()) == 0) {
            this.mBloomStrokeConfig = null;
        } else {
            this.mBloomStrokeConfig = new BloomStrokeConfig(parcel.readFloat(), parcel.readFloat(), parcel.readFloat(), parcel.readFloat(), parcel.readFloat(), parcel.readFloat(), parcel.readFloat(), parcel.readFloat(), parcel.readFloat(), parcel.readFloat(), parcel.readFloat(), parcel.readFloat(), parcel.readFloat(), parcel.readFloat(), parcel.readFloat(), parcel.readFloat(), parcel.readFloat(), parcel.readFloat(), parcel.readFloat(), parcel.readFloat(), parcel.readFloat());
        }
    }

    private void parseGlassConfig(Parcel parcel) {
        float[] fArr;
        if (Math.max(0, parcel.readInt()) == 0) {
            return;
        }
        int i = parcel.readInt();
        if (i > 0) {
            fArr = new float[i];
            parcel.readFloatArray(fArr);
        } else {
            fArr = null;
        }
        this.mGlassSmallBlurRadius = parcel.readInt();
        int i2 = parcel.readInt();
        this.mGlassBigBlurRadius = i2;
        if (fArr != null) {
            this.mGlassConfig = new GlassConfig(fArr, this.mGlassSmallBlurRadius, i2);
        }
    }

    private int[] readIntArrayFromParcel(Parcel parcel) {
        int iMax = Math.max(0, parcel.readInt());
        if (iMax == 0) {
            return null;
        }
        int[] iArr = new int[iMax];
        parcel.readIntArray(iArr);
        return iArr;
    }

    private float[] readFloatArrayFromParcel(Parcel parcel) {
        int iMax = Math.max(0, parcel.readInt());
        if (iMax == 0) {
            return null;
        }
        float[] fArr = new float[iMax];
        parcel.readFloatArray(fArr);
        return fArr;
    }

    public class ColorBlendConfig {
        public int[] blendColors;
        public float[] blendExtraParams;
        public int[] blendModes;

        public ColorBlendConfig(int[] iArr, int[] iArr2, float[] fArr) {
            this.blendColors = iArr;
            this.blendModes = iArr2;
            this.blendExtraParams = fArr;
        }
    }

    public class BlurConfig {
        public int blurBgMode;
        public int blurContentMode;
        public float[] blurExtraParams;
        public int blurRadius;
        public int blurSubType;
        public int blurType;
        public ColorBlendConfig colorBlendConfig;

        public BlurConfig(ColorBlendConfig colorBlendConfig) {
            this.blurBgMode = 0;
            this.blurContentMode = 0;
            this.blurType = 0;
            this.blurRadius = 0;
            this.blurSubType = 0;
            this.blurExtraParams = null;
            this.colorBlendConfig = colorBlendConfig;
        }

        public BlurConfig(int i, int i2, int i3, int i4, int i5, float[] fArr, ColorBlendConfig colorBlendConfig) {
            this.blurBgMode = i;
            this.blurContentMode = i2;
            this.blurType = i3;
            this.blurRadius = i4;
            this.blurSubType = i5;
            this.blurExtraParams = fArr;
            this.colorBlendConfig = colorBlendConfig;
        }
    }

    public class ShadowConfig {
        public int shadowColor;
        public float shadowDispersion;
        public float shadowOffsetX;
        public float shadowOffsetY;
        public float shadowRadius;

        public ShadowConfig(int i, float f, float f2, float f3, float f4) {
            this.shadowColor = i;
            this.shadowOffsetX = f;
            this.shadowOffsetY = f2;
            this.shadowRadius = f3;
            this.shadowDispersion = f4;
        }
    }

    public class BloomStrokeConfig {
        public float bloomStrokeColorA;
        public float bloomStrokeColorB;
        public float bloomStrokeColorG;
        public float bloomStrokeColorR;
        public float bloomStrokeGradientDegree;
        public float bloomStrokeWidth;
        public float normalWidth;
        public float source1A;
        public float source1B;
        public float source1G;
        public float source1R;
        public float source1X;
        public float source1Y;
        public float source1Z;
        public float source2A;
        public float source2B;
        public float source2G;
        public float source2R;
        public float source2X;
        public float source2Y;
        public float source2Z;

        public BloomStrokeConfig(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17, float f18, float f19, float f20, float f21) {
            this.bloomStrokeWidth = f;
            this.bloomStrokeGradientDegree = f2;
            this.bloomStrokeColorR = f3;
            this.bloomStrokeColorG = f4;
            this.bloomStrokeColorB = f5;
            this.bloomStrokeColorA = f6;
            this.normalWidth = f7;
            this.source1X = f8;
            this.source1Y = f9;
            this.source1Z = f10;
            this.source1R = f11;
            this.source1G = f12;
            this.source1B = f13;
            this.source1A = f14;
            this.source2X = f15;
            this.source2Y = f16;
            this.source2Z = f17;
            this.source2R = f18;
            this.source2G = f19;
            this.source2B = f20;
            this.source2A = f21;
        }
    }

    public class GlassConfig {
        public final int bigBlurRadius;
        public final float[] shaderParams;
        public final int smallBlurRadius;

        public GlassConfig(float[] fArr, int i, int i2) {
            this.shaderParams = (float[]) fArr.clone();
            this.smallBlurRadius = i;
            this.bigBlurRadius = i2;
        }

        public boolean hasBlurRadius() {
            return this.smallBlurRadius > 0 || this.bigBlurRadius > 0;
        }
    }
}
