package com.android.internal.telephony.cat;

import com.android.internal.telephony.GsmAlphabet;
import java.io.ByteArrayOutputStream;

/* JADX INFO: compiled from: ResponseData.java */
/* JADX INFO: loaded from: classes.dex */
class LanguageResponseData extends ResponseData {
    private String mLang;

    public LanguageResponseData(String str) {
        this.mLang = str;
    }

    @Override // com.android.internal.telephony.cat.ResponseData
    public void format(ByteArrayOutputStream byteArrayOutputStream) {
        byte[] bArrStringToGsm8BitPacked;
        if (byteArrayOutputStream == null) {
            return;
        }
        byteArrayOutputStream.write(ComprehensionTlvTag.LANGUAGE.value() | 128);
        String str = this.mLang;
        if (str != null && str.length() > 0) {
            bArrStringToGsm8BitPacked = GsmAlphabet.stringToGsm8BitPacked(this.mLang);
        } else {
            bArrStringToGsm8BitPacked = new byte[0];
        }
        byteArrayOutputStream.write(bArrStringToGsm8BitPacked.length);
        for (byte b : bArrStringToGsm8BitPacked) {
            byteArrayOutputStream.write(b);
        }
    }
}
