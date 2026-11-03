package miuix.theme.token;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/* JADX INFO: loaded from: classes2.dex */
public class MaterialToken implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: miuix.theme.token.MaterialToken.1
        @Override // android.os.Parcelable.Creator
        public MaterialToken createFromParcel(Parcel parcel) {
            return new MaterialToken(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public MaterialToken[] newArray(int i) {
            return new MaterialToken[i];
        }
    };
    public int[] blendColors;
    public float[] blendExtraParams;
    public int[] blendModes;
    public float bloomStrokeColorA;
    public float bloomStrokeColorB;
    public float bloomStrokeColorG;
    public float bloomStrokeColorR;
    public float bloomStrokeGradientDegree;
    public float bloomStrokeWidth;
    public int blurContainerMode;
    public int blurElementMode;
    public float[] blurExtraParams;
    public int blurRadius;
    public int blurSubType;
    public int blurType;
    public int enableBloomStroke;
    public int enableBlur;
    public int enableColorBlend;
    private int enableGlass;
    private int enableShadow;
    public int[] fallbackBlendColors;
    public float[] fallbackBlendExtraParams;
    public int[] fallbackBlendModes;
    public int glassBigBlurRadius;
    public float[] glassParam;
    public int glassSmallBlurRadius;
    public int hasFallbackColorBlend;
    public float normalWidth;
    private int shadowColor;
    private float shadowDispersion;
    private float shadowOffsetX;
    private float shadowOffsetY;
    private float shadowRadius;
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
    public String token;
    public String tokenVariant;
    public final int version;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public MaterialToken(Parcel parcel) {
        this.enableColorBlend = 0;
        this.hasFallbackColorBlend = 0;
        this.enableBlur = 0;
        this.enableShadow = 0;
        this.shadowOffsetX = 0.0f;
        this.shadowOffsetY = 0.0f;
        this.shadowRadius = 0.0f;
        this.shadowDispersion = 1.0f;
        this.enableBloomStroke = 0;
        this.bloomStrokeWidth = 0.0f;
        this.bloomStrokeGradientDegree = 0.0f;
        this.bloomStrokeColorR = 0.0f;
        this.bloomStrokeColorG = 0.0f;
        this.bloomStrokeColorB = 0.0f;
        this.bloomStrokeColorA = 0.0f;
        this.normalWidth = 0.0f;
        this.source1X = 0.0f;
        this.source1Y = 0.0f;
        this.source1Z = 0.0f;
        this.source1R = 0.0f;
        this.source1G = 0.0f;
        this.source1B = 0.0f;
        this.source1A = 0.0f;
        this.source2X = 0.0f;
        this.source2Y = 0.0f;
        this.source2Z = 0.0f;
        this.source2R = 0.0f;
        this.source2G = 0.0f;
        this.source2B = 0.0f;
        this.source2A = 0.0f;
        this.enableGlass = 0;
        this.glassSmallBlurRadius = 0;
        this.glassBigBlurRadius = 0;
        this.version = parcel.readInt();
        this.token = parcel.readString();
        this.tokenVariant = parcel.readString();
        readColorBlendFromParcel(parcel);
        readBlurFromParcel(parcel);
        readShadowFromParcel(parcel);
        readBloomStrokeFromParcel(parcel);
        readGlassFromParcel(parcel);
    }

    private void readColorBlendFromParcel(Parcel parcel) {
        int i = parcel.readInt();
        this.enableColorBlend = i;
        if (i <= 0) {
            return;
        }
        this.blendColors = readIntArrayFromParcel(parcel);
        this.blendModes = readIntArrayFromParcel(parcel);
        this.blendExtraParams = readFloatArrayFromParcel(parcel);
        int i2 = parcel.readInt();
        this.hasFallbackColorBlend = i2;
        if (i2 > 0) {
            this.fallbackBlendColors = readIntArrayFromParcel(parcel);
            this.fallbackBlendModes = readIntArrayFromParcel(parcel);
            this.fallbackBlendExtraParams = readFloatArrayFromParcel(parcel);
        }
    }

    private int[] readIntArrayFromParcel(Parcel parcel) {
        int i;
        if (parcel == null || (i = parcel.readInt()) < 0) {
            return null;
        }
        if (i == 0) {
            return new int[0];
        }
        int[] iArr = new int[i];
        parcel.readIntArray(iArr);
        return iArr;
    }

    private float[] readFloatArrayFromParcel(Parcel parcel) {
        int i;
        if (parcel == null || (i = parcel.readInt()) < 0) {
            return null;
        }
        if (i == 0) {
            return new float[0];
        }
        float[] fArr = new float[i];
        parcel.readFloatArray(fArr);
        return fArr;
    }

    private void readBlurFromParcel(Parcel parcel) {
        int i = parcel.readInt();
        this.enableBlur = i;
        if (i <= 0) {
            return;
        }
        this.blurContainerMode = parcel.readInt();
        this.blurElementMode = parcel.readInt();
        this.blurType = parcel.readInt();
        this.blurRadius = parcel.readInt();
        this.blurSubType = parcel.readInt();
        this.blurExtraParams = readFloatArrayFromParcel(parcel);
    }

    private void readShadowFromParcel(Parcel parcel) {
        int i = parcel.readInt();
        this.enableShadow = i;
        if (i <= 0) {
            return;
        }
        this.shadowColor = parcel.readInt();
        this.shadowOffsetX = parcel.readFloat();
        this.shadowOffsetY = parcel.readFloat();
        this.shadowRadius = parcel.readFloat();
        this.shadowDispersion = parcel.readFloat();
    }

    private void readBloomStrokeFromParcel(Parcel parcel) {
        int i = parcel.readInt();
        this.enableBloomStroke = i;
        if (i <= 0) {
            return;
        }
        this.bloomStrokeWidth = parcel.readFloat();
        this.bloomStrokeGradientDegree = parcel.readFloat();
        this.bloomStrokeColorR = parcel.readFloat();
        this.bloomStrokeColorG = parcel.readFloat();
        this.bloomStrokeColorB = parcel.readFloat();
        this.bloomStrokeColorA = parcel.readFloat();
        this.normalWidth = parcel.readFloat();
        this.source1X = parcel.readFloat();
        this.source1Y = parcel.readFloat();
        this.source1Z = parcel.readFloat();
        this.source1R = parcel.readFloat();
        this.source1G = parcel.readFloat();
        this.source1B = parcel.readFloat();
        this.source1A = parcel.readFloat();
        this.source2X = parcel.readFloat();
        this.source2Y = parcel.readFloat();
        this.source2Z = parcel.readFloat();
        this.source2R = parcel.readFloat();
        this.source2G = parcel.readFloat();
        this.source2B = parcel.readFloat();
        this.source2A = parcel.readFloat();
    }

    private void readGlassFromParcel(Parcel parcel) {
        int i = parcel.readInt();
        this.enableGlass = i;
        if (i <= 0) {
            return;
        }
        this.glassParam = readFloatArrayFromParcel(parcel);
        this.glassSmallBlurRadius = parcel.readInt();
        this.glassBigBlurRadius = parcel.readInt();
    }

    private MaterialToken(int i) {
        this.enableColorBlend = 0;
        this.hasFallbackColorBlend = 0;
        this.enableBlur = 0;
        this.enableShadow = 0;
        this.shadowOffsetX = 0.0f;
        this.shadowOffsetY = 0.0f;
        this.shadowRadius = 0.0f;
        this.shadowDispersion = 1.0f;
        this.enableBloomStroke = 0;
        this.bloomStrokeWidth = 0.0f;
        this.bloomStrokeGradientDegree = 0.0f;
        this.bloomStrokeColorR = 0.0f;
        this.bloomStrokeColorG = 0.0f;
        this.bloomStrokeColorB = 0.0f;
        this.bloomStrokeColorA = 0.0f;
        this.normalWidth = 0.0f;
        this.source1X = 0.0f;
        this.source1Y = 0.0f;
        this.source1Z = 0.0f;
        this.source1R = 0.0f;
        this.source1G = 0.0f;
        this.source1B = 0.0f;
        this.source1A = 0.0f;
        this.source2X = 0.0f;
        this.source2Y = 0.0f;
        this.source2Z = 0.0f;
        this.source2R = 0.0f;
        this.source2G = 0.0f;
        this.source2B = 0.0f;
        this.source2A = 0.0f;
        this.enableGlass = 0;
        this.glassSmallBlurRadius = 0;
        this.glassBigBlurRadius = 0;
        this.version = i;
    }

    public String toString() {
        if (TextUtils.isEmpty(this.token)) {
            return "{no token name}@" + Integer.toHexString(hashCode());
        }
        boolean zIsEmpty = TextUtils.isEmpty(this.tokenVariant);
        String str = this.token;
        if (zIsEmpty) {
            return str;
        }
        return str + "_" + this.tokenVariant;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.version);
        parcel.writeString(TextUtils.isEmpty(this.token) ? "" : this.token);
        parcel.writeString(TextUtils.isEmpty(this.tokenVariant) ? "" : this.tokenVariant);
        writeColorBlendToParcel(parcel);
        writeBlurToParcel(parcel);
        writeShadowToParcel(parcel);
        writeBloomStrokeToParcel(parcel);
        writeGlassToParcel(parcel);
    }

    private void writeColorBlendToParcel(Parcel parcel) {
        parcel.writeInt(this.enableColorBlend);
        if (this.enableColorBlend <= 0) {
            return;
        }
        writeBlendLayerToParcel(parcel, this.blendColors, this.blendModes, this.blendExtraParams);
        parcel.writeInt(this.hasFallbackColorBlend);
        if (this.hasFallbackColorBlend > 0) {
            writeBlendLayerToParcel(parcel, this.fallbackBlendColors, this.fallbackBlendModes, this.fallbackBlendExtraParams);
        }
    }

    private void writeBlendLayerToParcel(Parcel parcel, int[] iArr, int[] iArr2, float[] fArr) {
        if (iArr == null || iArr.length == 0 || iArr2 == null || iArr2.length == 0) {
            parcel.writeInt(0);
            parcel.writeInt(0);
            parcel.writeInt(0);
        } else {
            parcel.writeInt(iArr.length);
            parcel.writeIntArray(iArr);
            parcel.writeInt(iArr2.length);
            parcel.writeIntArray(iArr2);
            writeFloatArrayToParcel(parcel, fArr);
        }
    }

    private void writeBlurToParcel(Parcel parcel) {
        parcel.writeInt(this.enableBlur);
        if (this.enableBlur <= 0) {
            return;
        }
        parcel.writeInt(this.blurContainerMode);
        parcel.writeInt(this.blurElementMode);
        parcel.writeInt(this.blurType);
        parcel.writeInt(this.blurRadius);
        parcel.writeInt(this.blurSubType);
        writeFloatArrayToParcel(parcel, this.blurExtraParams);
    }

    private void writeShadowToParcel(Parcel parcel) {
        parcel.writeInt(this.enableShadow);
        if (this.enableShadow <= 0) {
            return;
        }
        parcel.writeInt(this.shadowColor);
        parcel.writeFloat(this.shadowOffsetX);
        parcel.writeFloat(this.shadowOffsetY);
        parcel.writeFloat(this.shadowRadius);
        parcel.writeFloat(this.shadowDispersion);
    }

    private void writeBloomStrokeToParcel(Parcel parcel) {
        parcel.writeInt(this.enableBloomStroke);
        if (this.enableBloomStroke <= 0) {
            return;
        }
        parcel.writeFloat(this.bloomStrokeWidth);
        parcel.writeFloat(this.bloomStrokeGradientDegree);
        parcel.writeFloat(this.bloomStrokeColorR);
        parcel.writeFloat(this.bloomStrokeColorG);
        parcel.writeFloat(this.bloomStrokeColorB);
        parcel.writeFloat(this.bloomStrokeColorA);
        parcel.writeFloat(this.normalWidth);
        parcel.writeFloat(this.source1X);
        parcel.writeFloat(this.source1Y);
        parcel.writeFloat(this.source1Z);
        parcel.writeFloat(this.source1R);
        parcel.writeFloat(this.source1G);
        parcel.writeFloat(this.source1B);
        parcel.writeFloat(this.source1A);
        parcel.writeFloat(this.source2X);
        parcel.writeFloat(this.source2Y);
        parcel.writeFloat(this.source2Z);
        parcel.writeFloat(this.source2R);
        parcel.writeFloat(this.source2G);
        parcel.writeFloat(this.source2B);
        parcel.writeFloat(this.source2A);
    }

    private void writeGlassToParcel(Parcel parcel) {
        parcel.writeInt(this.enableGlass);
        if (this.enableGlass <= 0) {
            return;
        }
        writeFloatArrayToParcel(parcel, this.glassParam);
        parcel.writeInt(this.glassSmallBlurRadius);
        parcel.writeInt(this.glassBigBlurRadius);
    }

    private void writeFloatArrayToParcel(Parcel parcel, float[] fArr) {
        if (fArr != null) {
            parcel.writeInt(fArr.length);
            if (fArr.length > 0) {
                parcel.writeFloatArray(fArr);
                return;
            }
            return;
        }
        parcel.writeInt(0);
    }

    public class Builder {
        private MaterialToken mToken;

        public Builder(int i, String str, String str2) {
            this.mToken = new MaterialToken(i);
            setTokenInfo(str, str2);
        }

        public Builder setTokenInfo(String str, String str2) {
            MaterialToken materialToken = this.mToken;
            if (str == null) {
                str = "";
            }
            materialToken.token = str;
            if (str2 == null) {
                str2 = "";
            }
            materialToken.tokenVariant = str2;
            return this;
        }

        public Builder setColorBlend(ColorBlendToken colorBlendToken) {
            if (colorBlendToken == null) {
                return this;
            }
            MaterialToken materialToken = this.mToken;
            materialToken.enableColorBlend = 1;
            materialToken.blendColors = colorBlendToken.colors;
            materialToken.blendModes = colorBlendToken.blendModes;
            materialToken.blendExtraParams = colorBlendToken.extraBlendParams;
            int[] iArr = colorBlendToken.fallbackColors;
            if (iArr != null) {
                materialToken.hasFallbackColorBlend = 1;
                materialToken.fallbackBlendColors = iArr;
                materialToken.fallbackBlendModes = colorBlendToken.fallbackBlendModes;
                materialToken.fallbackBlendExtraParams = colorBlendToken.fallbackExtraBlendParams;
                return this;
            }
            materialToken.hasFallbackColorBlend = 0;
            return this;
        }

        public Builder setElementBlur() {
            MaterialToken materialToken = this.mToken;
            materialToken.enableBlur = 1;
            materialToken.blurContainerMode = 0;
            materialToken.blurElementMode = 1;
            materialToken.blurType = 0;
            materialToken.blurRadius = -1;
            return this;
        }

        public Builder setElementBlur(int i) {
            MaterialToken materialToken = this.mToken;
            materialToken.enableBlur = 1;
            materialToken.blurContainerMode = 0;
            materialToken.blurElementMode = 1;
            materialToken.blurType = 0;
            materialToken.blurRadius = i;
            return this;
        }

        public Builder setMaskBlur(int i) {
            MaterialToken materialToken = this.mToken;
            materialToken.enableBlur = 1;
            materialToken.blurContainerMode = 1;
            materialToken.blurElementMode = 1;
            materialToken.blurType = 0;
            materialToken.blurRadius = i;
            return this;
        }

        public Builder setBlur(int i, int i2, int i3, int i4) {
            MaterialToken materialToken = this.mToken;
            materialToken.enableBlur = 1;
            materialToken.blurContainerMode = i;
            materialToken.blurElementMode = i2;
            materialToken.blurType = i3;
            materialToken.blurRadius = i4;
            return this;
        }

        public Builder setShadow(ShadowToken shadowToken) {
            if (shadowToken == null) {
                return this;
            }
            this.mToken.enableShadow = 1;
            this.mToken.shadowColor = shadowToken.color;
            this.mToken.shadowOffsetX = shadowToken.offsetX;
            this.mToken.shadowOffsetY = shadowToken.offsetY;
            this.mToken.shadowRadius = shadowToken.radius;
            this.mToken.shadowDispersion = shadowToken.dispersion;
            return this;
        }

        public Builder setBloomStroke(float[] fArr) {
            MaterialToken materialToken = this.mToken;
            materialToken.enableBloomStroke = 1;
            materialToken.bloomStrokeWidth = fArr[0];
            materialToken.bloomStrokeGradientDegree = fArr[1];
            materialToken.bloomStrokeColorR = fArr[2];
            materialToken.bloomStrokeColorG = fArr[3];
            materialToken.bloomStrokeColorB = fArr[4];
            materialToken.bloomStrokeColorA = fArr[5];
            materialToken.normalWidth = fArr[6];
            materialToken.source1X = fArr[7];
            materialToken.source1Y = fArr[8];
            materialToken.source1Z = fArr[9];
            materialToken.source1R = fArr[10];
            materialToken.source1G = fArr[11];
            materialToken.source1B = fArr[12];
            materialToken.source1A = fArr[13];
            materialToken.source2X = fArr[14];
            materialToken.source2Y = fArr[15];
            materialToken.source2Z = fArr[16];
            materialToken.source2R = fArr[17];
            materialToken.source2G = fArr[18];
            materialToken.source2B = fArr[19];
            materialToken.source2A = fArr[20];
            return this;
        }

        public Builder setGlass(GlassToken glassToken) {
            if (glassToken != null) {
                this.mToken.enableGlass = 1;
                this.mToken.glassParam = glassToken.toShaderParams();
                GlassToken.BlurRadius blurRadius = glassToken.blurRadius;
                if (blurRadius != null) {
                    MaterialToken materialToken = this.mToken;
                    materialToken.glassSmallBlurRadius = blurRadius.smallBlurRadius;
                    materialToken.glassBigBlurRadius = blurRadius.bigBlurRadius;
                }
            }
            return this;
        }

        public MaterialToken build() {
            return this.mToken;
        }
    }
}
