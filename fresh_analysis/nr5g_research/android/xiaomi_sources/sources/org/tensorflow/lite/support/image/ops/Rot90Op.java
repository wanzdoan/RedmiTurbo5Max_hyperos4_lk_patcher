package org.tensorflow.lite.support.image.ops;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import org.tensorflow.lite.support.image.ImageOperator;
import org.tensorflow.lite.support.image.TensorImage;

/* JADX INFO: loaded from: classes.dex */
public class Rot90Op implements ImageOperator {
    private final int numRotation;

    public Rot90Op() {
        this(1);
    }

    public Rot90Op(int k) {
        this.numRotation = k % 4;
    }

    @Override // org.tensorflow.lite.support.image.ImageOperator, org.tensorflow.lite.support.common.Operator
    public TensorImage apply(TensorImage image) {
        Bitmap input = image.getBitmap();
        if (this.numRotation == 0) {
            return image;
        }
        int w = input.getWidth();
        int h = input.getHeight();
        Matrix matrix = new Matrix();
        matrix.postTranslate(w * 0.5f, h * 0.5f);
        matrix.postRotate(this.numRotation * (-90));
        int newW = this.numRotation % 2 == 0 ? w : h;
        int newH = this.numRotation % 2 == 0 ? h : w;
        matrix.postTranslate(newW * 0.5f, newH * 0.5f);
        Bitmap output = Bitmap.createBitmap(input, 0, 0, w, h, matrix, false);
        image.load(output);
        return image;
    }

    @Override // org.tensorflow.lite.support.image.ImageOperator
    public int getOutputImageHeight(int inputImageHeight, int inputImageWidth) {
        return this.numRotation % 2 == 0 ? inputImageHeight : inputImageWidth;
    }

    @Override // org.tensorflow.lite.support.image.ImageOperator
    public int getOutputImageWidth(int inputImageHeight, int inputImageWidth) {
        return this.numRotation % 2 == 0 ? inputImageWidth : inputImageHeight;
    }

    @Override // org.tensorflow.lite.support.image.ImageOperator
    public PointF inverseTransform(PointF point, int inputImageHeight, int inputImageWidth) {
        int inverseNumRotation = (4 - this.numRotation) % 4;
        int height = getOutputImageHeight(inputImageHeight, inputImageWidth);
        int width = getOutputImageWidth(inputImageHeight, inputImageWidth);
        return transformImpl(point, height, width, inverseNumRotation);
    }

    private static PointF transformImpl(PointF point, int height, int width, int numRotation) {
        if (numRotation == 0) {
            return point;
        }
        if (numRotation == 1) {
            return new PointF(point.y, width - point.x);
        }
        if (numRotation == 2) {
            return new PointF(width - point.x, height - point.y);
        }
        return new PointF(height - point.y, point.x);
    }
}
