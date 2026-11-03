package com.android.phone.callcomposer;

/* JADX INFO: loaded from: classes.dex */
public class ImageData {
    private String mId;
    private final byte[] mImageBytes;
    private final String mMimeType;

    public ImageData(byte[] bArr, String str, String str2) {
        this.mImageBytes = bArr;
        this.mMimeType = str;
        this.mId = str2;
    }

    public byte[] getImageBytes() {
        return this.mImageBytes;
    }

    public String getMimeType() {
        return this.mMimeType;
    }

    public String getId() {
        return this.mId;
    }

    public void setId(String str) {
        this.mId = str;
    }
}
