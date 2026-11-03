package org.tensorflow.lite.support.image;

import android.graphics.Bitmap;
import java.nio.ByteBuffer;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.SupportPreconditions;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

/* JADX INFO: loaded from: classes.dex */
public class TensorImage {
    private ImageContainer container;
    private final DataType dataType;

    public TensorImage() {
        this(DataType.UINT8);
    }

    public TensorImage(DataType dataType) {
        this.container = null;
        SupportPreconditions.checkArgument(dataType == DataType.UINT8 || dataType == DataType.FLOAT32, "Illegal data type for TensorImage: Only FLOAT32 and UINT8 are accepted");
        this.dataType = dataType;
    }

    public static TensorImage fromBitmap(Bitmap bitmap) {
        TensorImage image = new TensorImage();
        image.load(bitmap);
        return image;
    }

    public static TensorImage createFrom(TensorImage src, DataType dataType) {
        TensorImage dst = new TensorImage(dataType);
        dst.container = src.container.clone();
        return dst;
    }

    public void load(Bitmap bitmap) {
        this.container = BitmapContainer.create(bitmap);
    }

    public void load(float[] pixels, int[] shape) {
        TensorBuffer buffer = TensorBuffer.createDynamic(getDataType());
        buffer.loadArray(pixels, shape);
        load(buffer);
    }

    public void load(int[] pixels, int[] shape) {
        TensorBuffer buffer = TensorBuffer.createDynamic(getDataType());
        buffer.loadArray(pixels, shape);
        load(buffer);
    }

    public void load(TensorBuffer buffer) {
        load(buffer, ColorSpaceType.RGB);
    }

    public void load(TensorBuffer buffer, ColorSpaceType colorSpaceType) {
        this.container = TensorBufferContainer.create(buffer, colorSpaceType);
    }

    public Bitmap getBitmap() {
        if (this.container == null) {
            throw new IllegalStateException("No image has been loaded yet.");
        }
        return this.container.getBitmap();
    }

    public ByteBuffer getBuffer() {
        return getTensorBuffer().getBuffer();
    }

    public TensorBuffer getTensorBuffer() {
        if (this.container == null) {
            throw new IllegalStateException("No image has been loaded yet.");
        }
        return this.container.getTensorBuffer(this.dataType);
    }

    public DataType getDataType() {
        return this.dataType;
    }

    public ColorSpaceType getColorSpaceType() {
        if (this.container == null) {
            throw new IllegalStateException("No image has been loaded yet.");
        }
        return this.container.getColorSpaceType();
    }

    public int getWidth() {
        if (this.container == null) {
            throw new IllegalStateException("No image has been loaded yet.");
        }
        return this.container.getWidth();
    }

    public int getHeight() {
        if (this.container == null) {
            throw new IllegalStateException("No image has been loaded yet.");
        }
        return this.container.getHeight();
    }
}
