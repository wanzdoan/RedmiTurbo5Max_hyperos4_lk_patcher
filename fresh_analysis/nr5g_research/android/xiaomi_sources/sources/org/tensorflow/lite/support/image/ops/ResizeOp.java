package org.tensorflow.lite.support.image.ops;

import android.graphics.Bitmap;
import android.graphics.PointF;
import org.tensorflow.lite.support.image.ImageOperator;
import org.tensorflow.lite.support.image.TensorImage;

/* JADX INFO: loaded from: classes.dex */
public class ResizeOp implements ImageOperator {
    private final int targetHeight;
    private final int targetWidth;
    private final boolean useBilinear;

    public enum ResizeMethod {
        BILINEAR,
        NEAREST_NEIGHBOR
    }

    public ResizeOp(int targetHeight, int targetWidth, ResizeMethod resizeMethod) {
        this.targetHeight = targetHeight;
        this.targetWidth = targetWidth;
        this.useBilinear = resizeMethod == ResizeMethod.BILINEAR;
    }

    @Override // org.tensorflow.lite.support.image.ImageOperator, org.tensorflow.lite.support.common.Operator
    public TensorImage apply(TensorImage image) {
        Bitmap scaled = Bitmap.createScaledBitmap(image.getBitmap(), this.targetWidth, this.targetHeight, this.useBilinear);
        image.load(scaled);
        return image;
    }

    @Override // org.tensorflow.lite.support.image.ImageOperator
    public int getOutputImageHeight(int inputImageHeight, int inputImageWidth) {
        return this.targetHeight;
    }

    @Override // org.tensorflow.lite.support.image.ImageOperator
    public int getOutputImageWidth(int inputImageHeight, int inputImageWidth) {
        return this.targetWidth;
    }

    @Override // org.tensorflow.lite.support.image.ImageOperator
    public PointF inverseTransform(PointF point, int inputImageHeight, int inputImageWidth) {
        return new PointF((point.x * inputImageWidth) / this.targetWidth, (point.y * inputImageHeight) / this.targetHeight);
    }
}
