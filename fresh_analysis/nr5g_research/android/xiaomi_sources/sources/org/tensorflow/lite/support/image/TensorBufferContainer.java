package org.tensorflow.lite.support.image;

import android.graphics.Bitmap;
import android.util.Log;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

/* JADX INFO: loaded from: classes.dex */
final class TensorBufferContainer implements ImageContainer {
    private static final String TAG = TensorBufferContainer.class.getSimpleName();
    private final TensorBuffer buffer;
    private final ColorSpaceType colorSpaceType;

    static TensorBufferContainer create(TensorBuffer buffer, ColorSpaceType colorSpaceType) {
        return new TensorBufferContainer(buffer, colorSpaceType);
    }

    private TensorBufferContainer(TensorBuffer buffer, ColorSpaceType colorSpaceType) {
        colorSpaceType.assertShape(buffer.getShape());
        this.buffer = buffer;
        this.colorSpaceType = colorSpaceType;
    }

    @Override // org.tensorflow.lite.support.image.ImageContainer
    public TensorBufferContainer clone() {
        return create(TensorBuffer.createFrom(this.buffer, this.buffer.getDataType()), this.colorSpaceType);
    }

    @Override // org.tensorflow.lite.support.image.ImageContainer
    public Bitmap getBitmap() {
        if (this.buffer.getDataType() != DataType.UINT8) {
            Log.w(TAG, "<Warning> TensorBufferContainer is holding a non-uint8 image. The conversion to Bitmap will cause numeric casting and clamping on the data value.");
        }
        return this.colorSpaceType.convertTensorBufferToBitmap(this.buffer);
    }

    @Override // org.tensorflow.lite.support.image.ImageContainer
    public TensorBuffer getTensorBuffer(DataType dataType) {
        DataType dataType2 = this.buffer.getDataType();
        TensorBuffer tensorBuffer = this.buffer;
        return dataType2 == dataType ? tensorBuffer : TensorBuffer.createFrom(tensorBuffer, dataType);
    }

    @Override // org.tensorflow.lite.support.image.ImageContainer
    public int getWidth() {
        return this.colorSpaceType.getWidth(this.buffer.getShape());
    }

    @Override // org.tensorflow.lite.support.image.ImageContainer
    public int getHeight() {
        return this.colorSpaceType.getHeight(this.buffer.getShape());
    }

    @Override // org.tensorflow.lite.support.image.ImageContainer
    public ColorSpaceType getColorSpaceType() {
        return this.colorSpaceType;
    }
}
