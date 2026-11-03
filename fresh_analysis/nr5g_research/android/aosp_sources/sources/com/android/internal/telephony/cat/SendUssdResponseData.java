package com.android.internal.telephony.cat;

import com.android.internal.telephony.EncodeException;
import com.android.internal.telephony.GsmAlphabet;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/* JADX INFO: compiled from: ResponseData.java */
/* JADX INFO: loaded from: classes.dex */
class SendUssdResponseData extends ResponseData {
    private byte mCodingScheme;
    private String mUssdResponse;

    SendUssdResponseData(String str, byte b) {
        this.mUssdResponse = str;
        this.mCodingScheme = b;
    }

    @Override // com.android.internal.telephony.cat.ResponseData
    public void format(ByteArrayOutputStream byteArrayOutputStream) {
        byte[] bArrStringToGsm8BitPacked;
        if (byteArrayOutputStream == null) {
            return;
        }
        try {
            byte b = this.mCodingScheme;
            if (b == 0) {
                byte[] bArrStringToGsm7BitPacked = GsmAlphabet.stringToGsm7BitPacked(this.mUssdResponse);
                byte[] bArr = new byte[bArrStringToGsm7BitPacked.length - 1];
                System.arraycopy(bArrStringToGsm7BitPacked, 1, bArr, 0, bArrStringToGsm7BitPacked.length - 1);
                bArrStringToGsm8BitPacked = bArr;
            } else if (b == 4) {
                bArrStringToGsm8BitPacked = GsmAlphabet.stringToGsm8BitPacked(this.mUssdResponse);
            } else if (b == 8) {
                bArrStringToGsm8BitPacked = this.mUssdResponse.getBytes(StandardCharsets.UTF_16BE);
            } else {
                bArrStringToGsm8BitPacked = new byte[0];
            }
        } catch (EncodeException unused) {
            bArrStringToGsm8BitPacked = new byte[0];
        }
        byteArrayOutputStream.write(ComprehensionTlvTag.TEXT_STRING.value() | 128);
        ResponseData.writeLength(byteArrayOutputStream, bArrStringToGsm8BitPacked.length + 1);
        byteArrayOutputStream.write(this.mCodingScheme);
        byteArrayOutputStream.write(bArrStringToGsm8BitPacked, 0, bArrStringToGsm8BitPacked.length);
    }
}
