package com.android.internal.telephony.cat;

import com.android.internal.telephony.EncodeException;
import com.android.internal.telephony.GsmAlphabet;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/* JADX INFO: compiled from: ResponseData.java */
/* JADX INFO: loaded from: classes.dex */
class GetInkeyInputResponseData extends ResponseData {
    protected static final byte GET_INKEY_NO = 0;
    protected static final byte GET_INKEY_YES = 1;
    public String mInData;
    private boolean mIsPacked;
    private boolean mIsUcs2;
    private boolean mIsYesNo;
    private boolean mYesNoResponse;

    public GetInkeyInputResponseData(String str, boolean z, boolean z2) {
        this.mIsUcs2 = z;
        this.mIsPacked = z2;
        this.mInData = str;
        this.mIsYesNo = false;
    }

    public GetInkeyInputResponseData(boolean z) {
        this.mIsUcs2 = false;
        this.mIsPacked = false;
        this.mInData = "";
        this.mIsYesNo = true;
        this.mYesNoResponse = z;
    }

    @Override // com.android.internal.telephony.cat.ResponseData
    public void format(ByteArrayOutputStream byteArrayOutputStream) {
        byte[] bArr;
        byte[] bArrStringToGsm8BitPacked;
        if (byteArrayOutputStream == null) {
            return;
        }
        byteArrayOutputStream.write(ComprehensionTlvTag.TEXT_STRING.value() | 128);
        if (this.mIsYesNo) {
            bArr = new byte[]{this.mYesNoResponse};
        } else {
            String str = this.mInData;
            if (str != null && str.length() > 0) {
                try {
                    if (this.mIsUcs2) {
                        bArrStringToGsm8BitPacked = this.mInData.getBytes("UTF-16BE");
                    } else {
                        boolean z = this.mIsPacked;
                        String str2 = this.mInData;
                        if (z) {
                            byte[] bArrStringToGsm7BitPacked = GsmAlphabet.stringToGsm7BitPacked(str2, 0, 0);
                            bArr = new byte[bArrStringToGsm7BitPacked.length - 1];
                            System.arraycopy(bArrStringToGsm7BitPacked, 1, bArr, 0, bArrStringToGsm7BitPacked.length - 1);
                        } else {
                            bArrStringToGsm8BitPacked = GsmAlphabet.stringToGsm8BitPacked(str2);
                        }
                    }
                    bArr = bArrStringToGsm8BitPacked;
                } catch (EncodeException unused) {
                    bArr = new byte[0];
                } catch (UnsupportedEncodingException unused2) {
                    bArr = new byte[0];
                }
            } else {
                bArr = new byte[0];
            }
        }
        if (bArr.length + 1 <= 255) {
            ResponseData.writeLength(byteArrayOutputStream, bArr.length + 1);
        } else {
            bArr = new byte[0];
        }
        if (this.mIsUcs2) {
            byteArrayOutputStream.write(8);
        } else if (this.mIsPacked) {
            byteArrayOutputStream.write(0);
        } else {
            byteArrayOutputStream.write(4);
        }
        for (byte b : bArr) {
            byteArrayOutputStream.write(b);
        }
    }
}
