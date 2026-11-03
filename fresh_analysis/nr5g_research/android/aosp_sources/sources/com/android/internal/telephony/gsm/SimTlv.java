package com.android.internal.telephony.gsm;

/* JADX INFO: loaded from: classes.dex */
public class SimTlv {
    int mCurDataLength;
    int mCurDataOffset;
    int mCurOffset;
    boolean mHasValidTlvObject = parseCurrentTlvObject();
    byte[] mRecord;
    int mTlvLength;
    int mTlvOffset;

    public SimTlv(byte[] bArr, int i, int i2) {
        this.mRecord = bArr;
        this.mTlvOffset = i;
        this.mTlvLength = i2;
        this.mCurOffset = i;
    }

    public boolean nextObject() {
        if (!this.mHasValidTlvObject) {
            return false;
        }
        this.mCurOffset = this.mCurDataOffset + this.mCurDataLength;
        boolean currentTlvObject = parseCurrentTlvObject();
        this.mHasValidTlvObject = currentTlvObject;
        return currentTlvObject;
    }

    public boolean isValidObject() {
        return this.mHasValidTlvObject;
    }

    public int getTag() {
        if (this.mHasValidTlvObject) {
            return this.mRecord[this.mCurOffset] & 255;
        }
        return 0;
    }

    public byte[] getData() {
        if (!this.mHasValidTlvObject) {
            return null;
        }
        int i = this.mCurDataLength;
        byte[] bArr = new byte[i];
        System.arraycopy(this.mRecord, this.mCurDataOffset, bArr, 0, i);
        return bArr;
    }

    private boolean parseCurrentTlvObject() {
        try {
            byte[] bArr = this.mRecord;
            int i = this.mCurOffset;
            byte b = bArr[i];
            if (b != 0 && (b & 255) != 255) {
                if ((bArr[i + 1] & 255) < 128) {
                    this.mCurDataLength = bArr[i + 1] & 255;
                    this.mCurDataOffset = i + 2;
                } else if ((bArr[i + 1] & 255) == 129) {
                    this.mCurDataLength = bArr[i + 2] & 255;
                    this.mCurDataOffset = i + 3;
                }
                return this.mCurDataLength + this.mCurDataOffset <= this.mTlvOffset + this.mTlvLength;
            }
        } catch (ArrayIndexOutOfBoundsException unused) {
        }
        return false;
    }
}
