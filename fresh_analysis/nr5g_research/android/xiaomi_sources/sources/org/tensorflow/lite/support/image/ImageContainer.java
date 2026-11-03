package org.tensorflow.lite.support.image;

import android.graphics.Bitmap;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

/* JADX INFO: loaded from: classes.dex */
interface ImageContainer {
    ImageContainer clone();

    Bitmap getBitmap();

    ColorSpaceType getColorSpaceType();

    int getHeight();

    TensorBuffer getTensorBuffer(DataType dataType);

    int getWidth();
}
