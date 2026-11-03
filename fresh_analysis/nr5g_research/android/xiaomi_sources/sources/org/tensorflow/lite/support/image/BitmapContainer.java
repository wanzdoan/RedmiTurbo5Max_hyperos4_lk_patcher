package org.tensorflow.lite.support.image;

import android.graphics.Bitmap;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.SupportPreconditions;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

/* JADX INFO: loaded from: classes.dex */
final class BitmapContainer implements ImageContainer {
    private final Bitmap bitmap;

    static BitmapContainer create(Bitmap bitmap) {
        return new BitmapContainer(bitmap);
    }

    private BitmapContainer(Bitmap bitmap) {
        SupportPreconditions.checkNotNull(bitmap, "Cannot load null bitmap.");
        SupportPreconditions.checkArgument(bitmap.getConfig().equals(Bitmap.Config.ARGB_8888), "Only supports loading ARGB_8888 bitmaps.");
        this.bitmap = bitmap;
    }

    @Override // org.tensorflow.lite.support.image.ImageContainer
    public BitmapContainer clone() {
        return create(this.bitmap.copy(this.bitmap.getConfig(), this.bitmap.isMutable()));
    }

    @Override // org.tensorflow.lite.support.image.ImageContainer
    public Bitmap getBitmap() {
        return this.bitmap;
    }

    @Override // org.tensorflow.lite.support.image.ImageContainer
    public TensorBuffer getTensorBuffer(DataType dataType) {
        TensorBuffer buffer = TensorBuffer.createDynamic(dataType);
        ImageConversions.convertBitmapToTensorBuffer(this.bitmap, buffer);
        return buffer;
    }

    @Override // org.tensorflow.lite.support.image.ImageContainer
    public int getWidth() {
        return this.bitmap.getWidth();
    }

    @Override // org.tensorflow.lite.support.image.ImageContainer
    public int getHeight() {
        return this.bitmap.getHeight();
    }

    @Override // org.tensorflow.lite.support.image.ImageContainer
    public ColorSpaceType getColorSpaceType() {
        return ColorSpaceType.fromBitmapConfig(this.bitmap.getConfig());
    }
}
