package org.tensorflow.lite.support.image;

import android.graphics.Bitmap;
import android.graphics.Color;
import com.xiaomi.modem.ModemUtils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

/* JADX INFO: loaded from: classes.dex */
class ImageConversions {
    static Bitmap convertRgbTensorBufferToBitmap(TensorBuffer buffer) {
        int[] shape = buffer.getShape();
        ColorSpaceType rgb = ColorSpaceType.RGB;
        rgb.assertShape(shape);
        int h = rgb.getHeight(shape);
        int w = rgb.getWidth(shape);
        Bitmap bitmap = Bitmap.createBitmap(w, h, rgb.toBitmapConfig());
        int[] intValues = new int[w * h];
        int[] rgbValues = buffer.getIntArray();
        int i = 0;
        int j = 0;
        while (i < intValues.length) {
            int j2 = j + 1;
            int r = rgbValues[j];
            int j3 = j2 + 1;
            int g = rgbValues[j2];
            int j4 = j3 + 1;
            int b = rgbValues[j3];
            intValues[i] = Color.rgb(r, g, b);
            i++;
            j = j4;
        }
        bitmap.setPixels(intValues, 0, w, 0, 0, w, h);
        return bitmap;
    }

    static Bitmap convertGrayscaleTensorBufferToBitmap(TensorBuffer buffer) {
        TensorBuffer uint8Buffer;
        if (buffer.getDataType() == DataType.UINT8) {
            uint8Buffer = buffer;
        } else {
            uint8Buffer = TensorBuffer.createFrom(buffer, DataType.UINT8);
        }
        int[] shape = uint8Buffer.getShape();
        ColorSpaceType grayscale = ColorSpaceType.GRAYSCALE;
        grayscale.assertShape(shape);
        Bitmap bitmap = Bitmap.createBitmap(grayscale.getWidth(shape), grayscale.getHeight(shape), grayscale.toBitmapConfig());
        uint8Buffer.getBuffer().rewind();
        bitmap.copyPixelsFromBuffer(uint8Buffer.getBuffer());
        return bitmap;
    }

    static void convertBitmapToTensorBuffer(Bitmap bitmap, TensorBuffer buffer) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] intValues = new int[w * h];
        bitmap.getPixels(intValues, 0, w, 0, 0, w, h);
        int flatSize = w * h * 3;
        int[] shape = {h, w, 3};
        switch (buffer.getDataType()) {
            case UINT8:
                byte[] byteArr = new byte[w * h * 3];
                int i = 0;
                int j = 0;
                while (i < intValues.length) {
                    int j2 = j + 1;
                    byteArr[j] = (byte) ((intValues[i] >> 16) & ModemUtils.MODEM_STATS_DATA_INVALID_3);
                    int j3 = j2 + 1;
                    byteArr[j2] = (byte) ((intValues[i] >> 8) & ModemUtils.MODEM_STATS_DATA_INVALID_3);
                    byteArr[j3] = (byte) (intValues[i] & ModemUtils.MODEM_STATS_DATA_INVALID_3);
                    i++;
                    j = j3 + 1;
                }
                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(flatSize);
                byteBuffer.order(ByteOrder.nativeOrder());
                byteBuffer.put(byteArr);
                buffer.loadBuffer(byteBuffer, shape);
                return;
            case FLOAT32:
                float[] floatArr = new float[w * h * 3];
                int i2 = 0;
                int j4 = 0;
                while (i2 < intValues.length) {
                    int j5 = j4 + 1;
                    floatArr[j4] = (intValues[i2] >> 16) & ModemUtils.MODEM_STATS_DATA_INVALID_3;
                    int j6 = j5 + 1;
                    floatArr[j5] = (intValues[i2] >> 8) & ModemUtils.MODEM_STATS_DATA_INVALID_3;
                    floatArr[j6] = intValues[i2] & ModemUtils.MODEM_STATS_DATA_INVALID_3;
                    i2++;
                    j4 = j6 + 1;
                }
                buffer.loadArray(floatArr, shape);
                return;
            default:
                throw new IllegalStateException("The type of TensorBuffer, " + buffer.getBuffer() + ", is unsupported.");
        }
    }

    private ImageConversions() {
    }
}
