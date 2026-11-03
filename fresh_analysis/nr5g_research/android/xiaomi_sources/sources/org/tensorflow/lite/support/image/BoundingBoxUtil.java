package org.tensorflow.lite.support.image;

import android.graphics.RectF;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.SupportPreconditions;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

/* JADX INFO: loaded from: classes.dex */
public final class BoundingBoxUtil {

    public enum CoordinateType {
        RATIO,
        PIXEL
    }

    public enum Type {
        BOUNDARIES,
        UPPER_LEFT,
        CENTER
    }

    public static List<RectF> convert(TensorBuffer tensor, int[] valueIndex, int boundingBoxAxis, Type type, CoordinateType coordinateType, int height, int width) {
        int boundingBoxAxis2;
        int[] iArr = valueIndex;
        int[] shape = tensor.getShape();
        SupportPreconditions.checkArgument(boundingBoxAxis >= (-shape.length) && boundingBoxAxis < shape.length, String.format("Axis %d is not in range (-(D+1), D), where D is the number of dimensions of input tensor (shape=%s)", Integer.valueOf(boundingBoxAxis), Arrays.toString(shape)));
        if (boundingBoxAxis >= 0) {
            boundingBoxAxis2 = boundingBoxAxis;
        } else {
            boundingBoxAxis2 = boundingBoxAxis + shape.length;
        }
        int i = 4;
        SupportPreconditions.checkArgument(shape[boundingBoxAxis2] == 4, String.format("Size of bounding box dimension %d is not 4. Got %d in shape %s", Integer.valueOf(boundingBoxAxis2), Integer.valueOf(shape[boundingBoxAxis2]), Arrays.toString(shape)));
        SupportPreconditions.checkArgument(iArr.length == 4, String.format("Bounding box index array length %d is not 4. Got index array %s", Integer.valueOf(iArr.length), Arrays.toString(iArr)));
        SupportPreconditions.checkArgument(tensor.getDataType() == DataType.FLOAT32, "Bounding Boxes only create from FLOAT32 buffers. Got: " + tensor.getDataType().name());
        List<RectF> boundingBoxList = new ArrayList<>();
        int a = 1;
        for (int i2 = 0; i2 < boundingBoxAxis2; i2++) {
            a *= shape[i2];
        }
        int b = 1;
        for (int i3 = boundingBoxAxis2 + 1; i3 < b; i3++) {
            b *= shape[i3];
        }
        float[] values = new float[4];
        ByteBuffer byteBuffer = tensor.getBuffer();
        byteBuffer.rewind();
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        int i4 = 0;
        while (i4 < a) {
            int j = 0;
            while (j < b) {
                for (int k = 0; k < i; k++) {
                    values[k] = floatBuffer.get((((i4 * 4) + k) * b) + j);
                }
                boundingBoxList.add(convertOneBoundingBox(values, iArr, type, coordinateType, height, width));
                j++;
                iArr = valueIndex;
                i = 4;
            }
            i4++;
            iArr = valueIndex;
            i = 4;
        }
        byteBuffer.rewind();
        return boundingBoxList;
    }

    private static RectF convertOneBoundingBox(float[] values, int[] valueIndex, Type type, CoordinateType coordinateType, int height, int width) {
        float[] orderedValues = new float[4];
        for (int i = 0; i < 4; i++) {
            orderedValues[i] = values[valueIndex[i]];
        }
        return convertOneBoundingBox(orderedValues, type, coordinateType, height, width);
    }

    private static RectF convertOneBoundingBox(float[] values, Type type, CoordinateType coordinateType, int height, int width) {
        switch (type) {
            case BOUNDARIES:
                return convertFromBoundaries(values, coordinateType, height, width);
            case UPPER_LEFT:
                return convertFromUpperLeft(values, coordinateType, height, width);
            case CENTER:
                return convertFromCenter(values, coordinateType, height, width);
            default:
                throw new IllegalArgumentException("Cannot recognize BoundingBox.Type " + type);
        }
    }

    private static RectF convertFromBoundaries(float[] values, CoordinateType coordinateType, int imageHeight, int imageWidth) {
        float left = values[0];
        float top = values[1];
        float right = values[2];
        float bottom = values[3];
        return getRectF(left, top, right, bottom, imageHeight, imageWidth, coordinateType);
    }

    private static RectF convertFromUpperLeft(float[] values, CoordinateType coordinateType, int imageHeight, int imageWidth) {
        float left = values[0];
        float top = values[1];
        float right = values[0] + values[2];
        float bottom = values[3] + values[1];
        return getRectF(left, top, right, bottom, imageHeight, imageWidth, coordinateType);
    }

    private static RectF convertFromCenter(float[] values, CoordinateType coordinateType, int imageHeight, int imageWidth) {
        float centerX = values[0];
        float centerY = values[1];
        float w = values[2];
        float h = values[3];
        float left = centerX - (w / 2.0f);
        float top = centerY - (h / 2.0f);
        float right = centerX + (w / 2.0f);
        float bottom = centerY + (h / 2.0f);
        return getRectF(left, top, right, bottom, imageHeight, imageWidth, coordinateType);
    }

    private static RectF getRectF(float left, float top, float right, float bottom, int imageHeight, int imageWidth, CoordinateType coordinateType) {
        if (coordinateType == CoordinateType.PIXEL) {
            return new RectF(left, top, right, bottom);
        }
        if (coordinateType == CoordinateType.RATIO) {
            return new RectF(imageWidth * left, imageHeight * top, imageWidth * right, imageHeight * bottom);
        }
        throw new IllegalArgumentException("Cannot convert coordinate type " + coordinateType);
    }

    private BoundingBoxUtil() {
    }
}
