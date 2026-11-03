package com.android.internal.telephony.uicc;

import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import com.android.internal.util.ArrayUtils;
import com.android.telephony.Rlog;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class AdnRecord implements Parcelable {
    protected static final int ADN_BCD_NUMBER_LENGTH = 0;
    protected static final int ADN_CAPABILITY_ID = 12;
    protected static final int ADN_DIALING_NUMBER_END = 11;
    protected static final int ADN_DIALING_NUMBER_START = 2;
    protected static final int ADN_EXTENSION_ID = 13;
    protected static final int ADN_TON_AND_NPI = 1;
    public static final Parcelable.Creator<AdnRecord> CREATOR = new Parcelable.Creator<AdnRecord>() { // from class: com.android.internal.telephony.uicc.AdnRecord.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AdnRecord createFromParcel(Parcel parcel) {
            return new AdnRecord(parcel.readInt(), parcel.readInt(), parcel.readString(), parcel.readString(), parcel.createStringArray(), parcel.createStringArray());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AdnRecord[] newArray(int i) {
            return new AdnRecord[i];
        }
    };
    protected static final int EXT_RECORD_LENGTH_BYTES = 13;
    protected static final int EXT_RECORD_TYPE_ADDITIONAL_DATA = 2;
    protected static final int EXT_RECORD_TYPE_MASK = 3;
    protected static final int FOOTER_SIZE_BYTES = 14;
    protected static final int MAX_EXT_CALLED_PARTY_LENGTH = 10;
    protected static final int MAX_NUMBER_SIZE_BYTES = 11;
    public String[] mAdditionalNumbers;
    public String mAlphaTag;
    public int mEfid;
    public String[] mEmails;
    public int mExtRecord;
    public String mNumber;
    public int mRecordNumber;

    public static int getMaxPhoneNumberDigits() {
        return 20;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public static int getMaxAlphaTagBytes(int i) {
        return Math.max(0, i - 14);
    }

    public static byte[] encodeAlphaTag(String str) {
        if (TextUtils.isEmpty(str)) {
            return new byte[0];
        }
        return IccUtils.stringToAdnStringField(str);
    }

    public static String decodeAlphaTag(byte[] bArr, int i, int i2) {
        return IccUtils.adnStringFieldToString(bArr, i, i2);
    }

    public AdnRecord(byte[] bArr) {
        this(0, 0, bArr);
    }

    public AdnRecord(int i, int i2, byte[] bArr) {
        this.mAlphaTag = null;
        this.mNumber = null;
        this.mAdditionalNumbers = null;
        this.mExtRecord = 255;
        this.mEfid = i;
        this.mRecordNumber = i2;
        parseRecord(bArr);
    }

    public AdnRecord(String str, String str2) {
        this(0, 0, str, str2);
    }

    public AdnRecord(String str, String str2, String[] strArr) {
        this(0, 0, str, str2, strArr);
    }

    public AdnRecord(String str, String str2, String[] strArr, String[] strArr2) {
        this(0, 0, str, str2, strArr, strArr2);
    }

    public AdnRecord(int i, int i2, String str, String str2, String[] strArr) {
        this.mExtRecord = 255;
        this.mEfid = i;
        this.mRecordNumber = i2;
        this.mAlphaTag = str;
        this.mNumber = str2;
        this.mEmails = strArr;
        this.mAdditionalNumbers = null;
    }

    public AdnRecord(int i, int i2, String str, String str2, String[] strArr, String[] strArr2) {
        this.mExtRecord = 255;
        this.mEfid = i;
        this.mRecordNumber = i2;
        this.mAlphaTag = str;
        this.mNumber = str2;
        this.mEmails = strArr;
        this.mAdditionalNumbers = strArr2;
    }

    public AdnRecord(int i, int i2, String str, String str2) {
        this.mExtRecord = 255;
        this.mEfid = i;
        this.mRecordNumber = i2;
        this.mAlphaTag = str;
        this.mNumber = str2;
        this.mEmails = null;
        this.mAdditionalNumbers = null;
    }

    public String getAlphaTag() {
        return this.mAlphaTag;
    }

    public int getEfid() {
        return this.mEfid;
    }

    public int getRecId() {
        return this.mRecordNumber;
    }

    public void setRecId(int i) {
        this.mRecordNumber = i;
    }

    public String getNumber() {
        return this.mNumber;
    }

    public void setNumber(String str) {
        this.mNumber = str;
    }

    public String[] getEmails() {
        return this.mEmails;
    }

    public void setEmails(String[] strArr) {
        this.mEmails = strArr;
    }

    public String[] getAdditionalNumbers() {
        return this.mAdditionalNumbers;
    }

    public void setAdditionalNumbers(String[] strArr) {
        this.mAdditionalNumbers = strArr;
    }

    public String toString() {
        return "ADN Record '" + this.mAlphaTag + "' '" + Rlog.pii("AdnRecord", this.mNumber) + " " + Rlog.pii("AdnRecord", this.mEmails) + " " + Rlog.pii("AdnRecord", this.mAdditionalNumbers) + "'";
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(this.mAlphaTag) && TextUtils.isEmpty(this.mNumber) && this.mEmails == null && this.mAdditionalNumbers == null;
    }

    public boolean hasExtendedRecord() {
        int i = this.mExtRecord;
        return (i == 0 || i == 255) ? false : true;
    }

    protected static boolean stringCompareNullEqualsEmpty(String str, String str2) {
        if (str == str2) {
            return true;
        }
        if (str == null) {
            str = "";
        }
        if (str2 == null) {
            str2 = "";
        }
        return str.equals(str2);
    }

    private static boolean arrayCompareNullEqualsEmpty(String[] strArr, String[] strArr2) {
        if (strArr == strArr2) {
            return true;
        }
        String[] strArr3 = (String[]) ArrayUtils.emptyIfNull(strArr, String.class);
        String[] strArr4 = (String[]) ArrayUtils.emptyIfNull(strArr2, String.class);
        List listAsList = Arrays.asList(strArr3);
        List listAsList2 = Arrays.asList(strArr4);
        if (listAsList.size() != listAsList2.size()) {
            return false;
        }
        return listAsList.containsAll(listAsList2);
    }

    public boolean isEqual(AdnRecord adnRecord) {
        return stringCompareNullEqualsEmpty(this.mAlphaTag, adnRecord.mAlphaTag) && stringCompareNullEqualsEmpty(this.mNumber, adnRecord.mNumber) && arrayCompareNullEqualsEmpty(this.mEmails, adnRecord.mEmails) && arrayCompareNullEqualsEmpty(this.mAdditionalNumbers, adnRecord.mAdditionalNumbers);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mEfid);
        parcel.writeInt(this.mRecordNumber);
        parcel.writeString(this.mAlphaTag);
        parcel.writeString(this.mNumber);
        parcel.writeStringArray(this.mEmails);
        parcel.writeStringArray(this.mAdditionalNumbers);
    }

    public byte[] buildAdnString(int i) {
        int i2 = i - 14;
        byte[] bArr = new byte[i];
        for (int i3 = 0; i3 < i; i3++) {
            bArr[i3] = -1;
        }
        if (TextUtils.isEmpty(this.mNumber) && TextUtils.isEmpty(this.mAlphaTag)) {
            Rlog.w("AdnRecord", "[buildAdnString] Empty dialing number");
            return bArr;
        }
        String str = this.mNumber;
        if (str != null && str.length() > 20) {
            Rlog.w("AdnRecord", "[buildAdnString] Max length of dialing number is 20");
            return null;
        }
        byte[] bArrEncodeAlphaTag = encodeAlphaTag(this.mAlphaTag);
        if (bArrEncodeAlphaTag.length > i2) {
            Rlog.w("AdnRecord", "[buildAdnString] Max length of tag is " + i2);
            return null;
        }
        if (!TextUtils.isEmpty(this.mNumber)) {
            byte[] bArrNumberToCalledPartyBCD = PhoneNumberUtils.numberToCalledPartyBCD(this.mNumber, 1);
            System.arraycopy(bArrNumberToCalledPartyBCD, 0, bArr, i - 13, bArrNumberToCalledPartyBCD.length);
            bArr[i2] = (byte) bArrNumberToCalledPartyBCD.length;
        }
        bArr[i - 2] = -1;
        bArr[i - 1] = -1;
        if (bArrEncodeAlphaTag.length > 0) {
            System.arraycopy(bArrEncodeAlphaTag, 0, bArr, 0, bArrEncodeAlphaTag.length);
        }
        return bArr;
    }

    public void appendExtRecord(byte[] bArr) {
        try {
            if (bArr.length == 13 && (bArr[0] & 3) == 2 && (bArr[1] & 255) <= 10) {
                this.mNumber += PhoneNumberUtils.calledPartyBCDFragmentToString(bArr, 2, bArr[1] & 255, 1);
            }
        } catch (RuntimeException e) {
            Rlog.w("AdnRecord", "Error parsing AdnRecord ext record", e);
        }
    }

    private void parseRecord(byte[] bArr) {
        try {
            this.mAlphaTag = decodeAlphaTag(bArr, 0, bArr.length - 14);
            int length = bArr.length;
            int i = bArr[length - 14] & 255;
            if (i > 11) {
                this.mNumber = "";
                return;
            }
            this.mNumber = PhoneNumberUtils.calledPartyBCDToString(bArr, length - 13, i, 1);
            this.mExtRecord = bArr[bArr.length - 1] & 255;
            this.mEmails = null;
            this.mAdditionalNumbers = null;
        } catch (RuntimeException e) {
            Rlog.w("AdnRecord", "Error parsing AdnRecord", e);
            this.mNumber = "";
            this.mAlphaTag = "";
            this.mEmails = null;
            this.mAdditionalNumbers = null;
        }
    }
}
