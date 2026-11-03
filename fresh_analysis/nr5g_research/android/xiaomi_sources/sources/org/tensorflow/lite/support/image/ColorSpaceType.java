package org.tensorflow.lite.support.image;

import android.graphics.Bitmap;
import java.util.Arrays;
import org.tensorflow.lite.support.common.SupportPreconditions;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: loaded from: classes.dex */
public abstract class ColorSpaceType {
    private static final /* synthetic */ ColorSpaceType[] $VALUES = $values();
    private static final int BATCH_DIM = 0;
    private static final int BATCH_VALUE = 1;
    private static final int CHANNEL_DIM = 3;
    public static final ColorSpaceType GRAYSCALE;
    private static final int HEIGHT_DIM = 1;
    public static final ColorSpaceType RGB;
    private static final int WIDTH_DIM = 2;

    abstract Bitmap convertTensorBufferToBitmap(TensorBuffer tensorBuffer);

    abstract int getChannelValue();

    abstract int[] getNormalizedShape(int[] iArr);

    abstract String getShapeInfoMessage();

    abstract Bitmap.Config toBitmapConfig();

    private static /* synthetic */ ColorSpaceType[] $values() {
        return new ColorSpaceType[]{RGB, GRAYSCALE};
    }

    private ColorSpaceType(String str, int i) {
        super(str, i);
    }

    public static ColorSpaceType valueOf(String name) {
        return (ColorSpaceType) Enum.valueOf(ColorSpaceType.class, name);
    }

    public static ColorSpaceType[] values() {
        return (ColorSpaceType[]) $VALUES.clone();
    }

    /* JADX INFO: renamed from: org.tensorflow.lite.support.image.ColorSpaceType$1, reason: invalid class name */
    final enum AnonymousClass1 extends ColorSpaceType {
        private static final int CHANNEL_VALUE = 3;

        private AnonymousClass1(String str, int i) {
            super(str, i);
        }

        @Override // org.tensorflow.lite.support.image.ColorSpaceType
        Bitmap convertTensorBufferToBitmap(TensorBuffer buffer) {
            return ImageConversions.convertRgbTensorBufferToBitmap(buffer);
        }

        @Override // org.tensorflow.lite.support.image.ColorSpaceType
        int getChannelValue() {
            return 3;
        }

        @Override // org.tensorflow.lite.support.image.ColorSpaceType
        int[] getNormalizedShape(int[] shape) {
            switch (shape.length) {
                case 3:
                    return ColorSpaceType.insertValue(shape, 0, 1);
                case 4:
                    return shape;
                default:
                    throw new IllegalArgumentException(getShapeInfoMessage() + "The provided image shape is " + Arrays.toString(shape));
            }
        }

        @Override // org.tensorflow.lite.support.image.ColorSpaceType
        String getShapeInfoMessage() {
            return "The shape of a RGB image should be (h, w, c) or (1, h, w, c), and channels representing R, G, B in order. ";
        }

        @Override // org.tensorflow.lite.support.image.ColorSpaceType
        Bitmap.Config toBitmapConfig() {
            return Bitmap.Config.ARGB_8888;
        }
    }

    static {
        RGB = new AnonymousClass1("RGB", 0);
        GRAYSCALE = new AnonymousClass2("GRAYSCALE", 1);
    }

    /* JADX INFO: renamed from: org.tensorflow.lite.support.image.ColorSpaceType$2, reason: invalid class name */
    final enum AnonymousClass2 extends ColorSpaceType {
        private static final int CHANNEL_VALUE = 1;

        private AnonymousClass2(String str, int i) {
            super(str, i);
        }

        @Override // org.tensorflow.lite.support.image.ColorSpaceType
        Bitmap convertTensorBufferToBitmap(TensorBuffer buffer) {
            return ImageConversions.convertGrayscaleTensorBufferToBitmap(buffer);
        }

        @Override // org.tensorflow.lite.support.image.ColorSpaceType
        int getChannelValue() {
            return 1;
        }

        @Override // org.tensorflow.lite.support.image.ColorSpaceType
        int[] getNormalizedShape(int[] shape) {
            switch (shape.length) {
                case 2:
                    int[] shapeWithBatch = ColorSpaceType.insertValue(shape, 0, 1);
                    return ColorSpaceType.insertValue(shapeWithBatch, 3, 1);
                case 3:
                default:
                    throw new IllegalArgumentException(getShapeInfoMessage() + "The provided image shape is " + Arrays.toString(shape));
                case 4:
                    return shape;
            }
        }

        @Override // org.tensorflow.lite.support.image.ColorSpaceType
        String getShapeInfoMessage() {
            return "The shape of a grayscale image should be (h, w) or (1, h, w, 1). ";
        }

        @Override // org.tensorflow.lite.support.image.ColorSpaceType
        Bitmap.Config toBitmapConfig() {
            return Bitmap.Config.ALPHA_8;
        }
    }

    /* JADX INFO: renamed from: org.tensorflow.lite.support.image.ColorSpaceType$3, reason: invalid class name */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$android$graphics$Bitmap$Config = new int[Bitmap.Config.values().length];

        static {
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Bitmap.Config.ARGB_8888.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Bitmap.Config.ALPHA_8.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    static ColorSpaceType fromBitmapConfig(Bitmap.Config config) {
        switch (AnonymousClass3.$SwitchMap$android$graphics$Bitmap$Config[config.ordinal()]) {
            case 1:
                return RGB;
            case 2:
                return GRAYSCALE;
            default:
                throw new IllegalArgumentException("Bitmap configuration: " + config + ", is not supported yet.");
        }
    }

    void assertShape(int[] shape) {
        int[] normalizedShape = getNormalizedShape(shape);
        SupportPreconditions.checkArgument(isValidNormalizedShape(normalizedShape), getShapeInfoMessage() + "The provided image shape is " + Arrays.toString(shape));
    }

    int getWidth(int[] shape) {
        assertShape(shape);
        return getNormalizedShape(shape)[2];
    }

    int getHeight(int[] shape) {
        assertShape(shape);
        return getNormalizedShape(shape)[1];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int[] insertValue(int[] array, int pos, int value) {
        int[] newArray = new int[array.length + 1];
        for (int i = 0; i < pos; i++) {
            newArray[i] = array[i];
        }
        newArray[pos] = value;
        for (int i2 = pos + 1; i2 < newArray.length; i2++) {
            newArray[i2] = array[i2 - 1];
        }
        return newArray;
    }

    protected boolean isValidNormalizedShape(int[] shape) {
        return shape[0] == 1 && shape[1] > 0 && shape[2] > 0 && shape[3] == getChannelValue();
    }
}
