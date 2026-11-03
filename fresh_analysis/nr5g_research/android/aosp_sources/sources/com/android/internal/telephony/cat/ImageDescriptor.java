package com.android.internal.telephony.cat;

/* JADX INFO: loaded from: classes.dex */
public class ImageDescriptor {
    public static final int CODING_SCHEME_BASIC = 17;
    public static final int CODING_SCHEME_COLOUR = 33;
    public int mWidth = 0;
    public int mHeight = 0;
    public int mCodingScheme = 0;
    public int mImageId = 0;
    public int mHighOffset = 0;
    public int mLowOffset = 0;
    public int mLength = 0;

    ImageDescriptor() {
    }

    public static ImageDescriptor parse(byte[] bArr, int i) {
        ImageDescriptor imageDescriptor = new ImageDescriptor();
        int i2 = i + 1;
        try {
            imageDescriptor.mWidth = bArr[i] & 255;
            imageDescriptor.mHeight = bArr[i2] & 255;
            imageDescriptor.mCodingScheme = bArr[i + 2] & 255;
            int i3 = (bArr[i + 3] & 255) << 8;
            imageDescriptor.mImageId = i3;
            imageDescriptor.mImageId = i3 | (bArr[i + 4] & 255);
            imageDescriptor.mHighOffset = bArr[i + 5] & 255;
            imageDescriptor.mLowOffset = bArr[i + 6] & 255;
            imageDescriptor.mLength = (bArr[i + 8] & 255) | ((bArr[i + 7] & 255) << 8);
            CatLog.d("ImageDescriptor", "parse; Descriptor : " + imageDescriptor.mWidth + ", " + imageDescriptor.mHeight + ", " + imageDescriptor.mCodingScheme + ", 0x" + Integer.toHexString(imageDescriptor.mImageId) + ", " + imageDescriptor.mHighOffset + ", " + imageDescriptor.mLowOffset + ", " + imageDescriptor.mLength);
            return imageDescriptor;
        } catch (IndexOutOfBoundsException unused) {
            CatLog.d("ImageDescriptor", "parse; failed parsing image descriptor");
            return null;
        }
    }
}
