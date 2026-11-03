package org.tensorflow.lite.support.image.ops;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import org.tensorflow.lite.support.image.ImageOperator;
import org.tensorflow.lite.support.image.TensorImage;

/* JADX INFO: loaded from: classes.dex */
public class ResizeWithCropOrPadOp implements ImageOperator {
    private final Bitmap output;
    private final int targetHeight;
    private final int targetWidth;

    public ResizeWithCropOrPadOp(int targetHeight, int targetWidth) {
        this.targetHeight = targetHeight;
        this.targetWidth = targetWidth;
        this.output = Bitmap.createBitmap(this.targetWidth, this.targetHeight, Bitmap.Config.ARGB_8888);
    }

    @Override // org.tensorflow.lite.support.image.ImageOperator, org.tensorflow.lite.support.common.Operator
    public TensorImage apply(TensorImage image) {
        int dstL;
        int dstR;
        int srcL;
        int srcR;
        int dstT;
        int dstB;
        int srcT;
        int srcB;
        Bitmap input = image.getBitmap();
        int w = input.getWidth();
        int h = input.getHeight();
        if (this.targetWidth > w) {
            srcL = 0;
            srcR = w;
            dstL = (this.targetWidth - w) / 2;
            dstR = dstL + w;
        } else {
            dstL = 0;
            dstR = this.targetWidth;
            srcL = (w - this.targetWidth) / 2;
            srcR = this.targetWidth + srcL;
        }
        if (this.targetHeight > h) {
            srcT = 0;
            srcB = h;
            dstT = (this.targetHeight - h) / 2;
            dstB = dstT + h;
        } else {
            dstT = 0;
            dstB = this.targetHeight;
            srcT = (h - this.targetHeight) / 2;
            srcB = this.targetHeight + srcT;
        }
        Rect src = new Rect(srcL, srcT, srcR, srcB);
        Rect dst = new Rect(dstL, dstT, dstR, dstB);
        new Canvas(this.output).drawBitmap(input, src, dst, (Paint) null);
        image.load(this.output);
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
        return transformImpl(point, this.targetHeight, this.targetWidth, inputImageHeight, inputImageWidth);
    }

    private static PointF transformImpl(PointF point, int srcH, int srcW, int dstH, int dstW) {
        return new PointF(point.x + ((dstW - srcW) / 2), point.y + ((dstH - srcH) / 2));
    }
}
