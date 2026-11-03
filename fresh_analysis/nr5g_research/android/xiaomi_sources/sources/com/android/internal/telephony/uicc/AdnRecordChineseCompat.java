package com.android.internal.telephony.uicc;

import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import com.android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public class AdnRecordChineseCompat extends AdnRecord {
    public AdnRecordChineseCompat(int efid, int recordNumber, String alphaTag, String number) {
        super(efid, recordNumber, alphaTag, number);
    }

    public byte[] buildAdnString(int recordSize) {
        int footerOffset = recordSize - 14;
        byte[] adnString = new byte[recordSize];
        for (int i = 0; i < recordSize; i++) {
            adnString[i] = -1;
        }
        if (isEmpty()) {
            Rlog.w("AdnRecord", "[buildAdnString] Empty dialing number");
            return adnString;
        }
        if (MiuiAdnRecord.isAdnNumberTooLong(this.mNumber)) {
            Rlog.w("AdnRecord", "[buildAdnString] Max length of dialing number is 20");
            return null;
        }
        if (MiuiAdnRecord.isAdnAlphaTagTooLong(this.mAlphaTag, recordSize)) {
            Rlog.w("AdnRecord", "[buildAdnString] Max length of tag is " + footerOffset);
            return null;
        }
        byte[] bcdNumber = TextUtils.isEmpty(this.mNumber) ? null : PhoneNumberUtils.numberToCalledPartyBCD(this.mNumber);
        if (bcdNumber != null) {
            System.arraycopy(bcdNumber, 0, adnString, footerOffset + 1, bcdNumber.length);
            adnString[footerOffset + 0] = (byte) bcdNumber.length;
            adnString[footerOffset + 12] = -1;
            adnString[footerOffset + 13] = -1;
        } else {
            adnString[footerOffset + 0] = 0;
        }
        if (!TextUtils.isEmpty(this.mAlphaTag)) {
            int result = MiuiAdnRecord.encodeAlphaTag(adnString, this.mAlphaTag, footerOffset);
            if (result != 0) {
                return null;
            }
        }
        return adnString;
    }
}
