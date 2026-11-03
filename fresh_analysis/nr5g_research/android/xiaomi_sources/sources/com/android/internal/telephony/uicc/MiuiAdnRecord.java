package com.android.internal.telephony.uicc;

import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.text.TextUtils;
import com.android.internal.telephony.EncodeException;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.MiuiIccProviderException;
import com.xiaomi.modem.ModemUtils;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class MiuiAdnRecord implements Parcelable {
    static final int ADN_BCD_NUMBER_LENGTH = 0;
    static final int ADN_CAPABILITY_ID = 12;
    static final int ADN_DIALING_NUMBER_END = 11;
    static final int ADN_DIALING_NUMBER_START = 2;
    static final int ADN_EXTENSION_ID = 13;
    static final int ADN_TON_AND_NPI = 1;
    public static final Parcelable.Creator<MiuiAdnRecord> CREATOR = new Parcelable.Creator<MiuiAdnRecord>() { // from class: com.android.internal.telephony.uicc.MiuiAdnRecord.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MiuiAdnRecord createFromParcel(Parcel source) {
            int efid = source.readInt();
            int recordNumber = source.readInt();
            String alphaTag = source.readString();
            String number = source.readString();
            String[] emails = source.readStringArray();
            String anr = source.readString();
            return new MiuiAdnRecord(efid, recordNumber, alphaTag, number, emails, anr);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MiuiAdnRecord[] newArray(int size) {
            return new MiuiAdnRecord[size];
        }
    };
    static final int EXT_RECORD_LENGTH_BYTES = 13;
    static final int EXT_RECORD_TYPE_ADDITIONAL_DATA = 2;
    static final int EXT_RECORD_TYPE_MASK = 3;
    static final int FOOTER_SIZE_BYTES = 14;
    static final String LOG_TAG = "MiuiAdnRecord";
    static final int MAX_BCD_NUMBER_LENGTH = 20;
    static final int MAX_EXT_CALLED_PARTY_LENGTH = 10;
    static final int MAX_NUMBER_SIZE_BYTES = 11;
    String mAlphaTag;
    String mAnr;
    int mEfid;
    String[] mEmails;
    int mExtRecord;
    String mNumber;
    int mRecordNumber;

    public MiuiAdnRecord(byte[] record) {
        this(0, 0, record);
    }

    public MiuiAdnRecord(int efid, int recordNumber, byte[] record) {
        this.mAlphaTag = null;
        this.mNumber = null;
        this.mExtRecord = ModemUtils.MODEM_STATS_DATA_INVALID_3;
        this.mEfid = efid;
        this.mRecordNumber = recordNumber;
        parseRecord(record);
    }

    public MiuiAdnRecord(String alphaTag, String number) {
        this(0, 0, alphaTag, number);
    }

    public MiuiAdnRecord(String alphaTag, String number, String[] emails, String anr) {
        this(0, 0, alphaTag, number, emails, anr);
    }

    public MiuiAdnRecord(int efid, int recordNumber, String alphaTag, String number, String[] emails, String anr) {
        this.mAlphaTag = null;
        this.mNumber = null;
        this.mExtRecord = ModemUtils.MODEM_STATS_DATA_INVALID_3;
        this.mEfid = efid;
        this.mRecordNumber = recordNumber;
        this.mAlphaTag = alphaTag;
        this.mNumber = number;
        this.mEmails = emails;
        this.mAnr = anr;
    }

    public MiuiAdnRecord(int efid, int recordNumber, String alphaTag, String number) {
        this.mAlphaTag = null;
        this.mNumber = null;
        this.mExtRecord = ModemUtils.MODEM_STATS_DATA_INVALID_3;
        this.mEfid = efid;
        this.mRecordNumber = recordNumber;
        this.mAlphaTag = alphaTag;
        this.mNumber = number;
        this.mEmails = null;
        this.mAnr = "";
    }

    public AdnRecord getAdnRecord() {
        int efid = this.mEfid;
        int recordNumber = this.mRecordNumber;
        String alphaTag = this.mAlphaTag;
        String number = this.mNumber;
        String[] emails = this.mEmails;
        AdnRecord adnRecord = new AdnRecord(efid, recordNumber, alphaTag, number, emails, (String[]) null);
        return adnRecord;
    }

    public String getAlphaTag() {
        return this.mAlphaTag;
    }

    public void setAlphaTag(String alphaTag) {
        this.mAlphaTag = alphaTag;
    }

    public void setNumber(String number) {
        this.mNumber = number;
    }

    public String getNumber() {
        return this.mNumber;
    }

    public String[] getEmails() {
        return this.mEmails;
    }

    public void setEmails(String[] emails) {
        this.mEmails = emails;
    }

    public String getAnr() {
        return this.mAnr;
    }

    public void setAnr(String anr) {
        this.mAnr = anr;
    }

    public String toString() {
        return "ADN Record '" + this.mAlphaTag + "' '" + this.mNumber + " " + this.mEmails + " " + this.mAnr + "'";
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(this.mAlphaTag) && TextUtils.isEmpty(this.mNumber) && this.mEmails == null && TextUtils.isEmpty(this.mAnr);
    }

    public boolean hasExtendedRecord() {
        return (this.mExtRecord == 0 || this.mExtRecord == 255) ? false : true;
    }

    private static boolean stringCompareNullEqualsEmpty(String s1, String s2) {
        if (s1 == s2) {
            return true;
        }
        if (s1 == null) {
            s1 = "";
        }
        if (s2 == null) {
            s2 = "";
        }
        return s1.equals(s2);
    }

    public boolean isEqual(MiuiAdnRecord adn) {
        return stringCompareNullEqualsEmpty(this.mAlphaTag, adn.mAlphaTag) && stringCompareNullEqualsEmpty(this.mNumber, adn.mNumber) && Arrays.equals(this.mEmails, adn.mEmails);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mEfid);
        dest.writeInt(this.mRecordNumber);
        dest.writeString(this.mAlphaTag);
        dest.writeString(this.mNumber);
        dest.writeStringArray(this.mEmails);
        dest.writeString(this.mAnr);
    }

    public class BuildAdnResult {
        public byte[] adnStr;
        public int result;

        BuildAdnResult(MiuiAdnRecord this$0, byte[] adnStr, int result) {
            this.adnStr = adnStr;
            this.result = result;
        }
    }

    public static boolean isAdnNumberTooLong(String number) {
        if (number != null && number.length() > 20) {
            return true;
        }
        return false;
    }

    public static boolean isAdnAlphaTagTooLong(String alphaTag, int recordSize) {
        if (alphaTag != null && alphaTag.length() > recordSize - 14) {
            return true;
        }
        return false;
    }

    public BuildAdnResult buildAdnString(int recordSize) {
        int result;
        int footerOffset = recordSize - 14;
        byte[] adnString = new byte[recordSize];
        for (int i = 0; i < recordSize; i++) {
            adnString[i] = -1;
        }
        if (isEmpty()) {
            Rlog.w(LOG_TAG, "[buildAdnString] Empty dialing number");
            return new BuildAdnResult(this, adnString, 0);
        }
        if (isAdnNumberTooLong(this.mNumber)) {
            Rlog.w(LOG_TAG, "[buildAdnString] Max length of dialing number is 20");
            return new BuildAdnResult(this, null, MiuiIccProviderException.ERROR_NUMBER_TOO_LONG);
        }
        if (isAdnAlphaTagTooLong(this.mAlphaTag, recordSize)) {
            Rlog.w(LOG_TAG, "[buildAdnString] Max length of tag is " + footerOffset);
            return new BuildAdnResult(this, null, MiuiIccProviderException.ERROR_TEXT_TOO_LONG);
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
        if (!TextUtils.isEmpty(this.mAlphaTag) && (result = encodeAlphaTag(adnString, this.mAlphaTag, footerOffset)) != 0) {
            return new BuildAdnResult(this, null, result);
        }
        return new BuildAdnResult(this, adnString, 0);
    }

    public static int encodeAlphaTag(byte[] adnString, String alphaTag, int footerOffset) {
        byte[] byteTag;
        try {
            GsmAlphabet.stringToGsm7BitPacked(alphaTag);
            byteTag = GsmAlphabet.stringToGsm8BitPacked(alphaTag);
        } catch (EncodeException e) {
            Rlog.w(LOG_TAG, "[buildAdnString]  can't encode with GSM alphabet, try utf-16be");
            byte[] header = {-128};
            try {
                byte[] data = alphaTag.getBytes("utf-16be");
                byte[] byteTag2 = new byte[data.length + 1];
                System.arraycopy(header, 0, byteTag2, 0, 1);
                System.arraycopy(data, 0, byteTag2, 1, data.length);
                byteTag = byteTag2;
            } catch (UnsupportedEncodingException uex) {
                Rlog.e(LOG_TAG, "Implausible UnsupportedEncodingException ", uex);
                return MiuiIccProviderException.ERROR_GENERIC_FAILURE;
            }
        }
        if (byteTag.length > footerOffset) {
            Rlog.w(LOG_TAG, "[buildAdnString] alpha tag is too long, should <=" + footerOffset);
            return MiuiIccProviderException.ERROR_TEXT_TOO_LONG;
        }
        System.arraycopy(byteTag, 0, adnString, 0, byteTag.length > footerOffset ? footerOffset : byteTag.length);
        return 0;
    }

    public void appendExtRecord(byte[] extRecord) {
        try {
            if (extRecord.length == 13 && (extRecord[0] & 3) == 2 && (extRecord[1] & ModemUtils.EFS_VALUE_ASDIV_73841_DYNAMIC_SWITCH) <= 10) {
                this.mNumber += PhoneNumberUtils.calledPartyBCDFragmentToString(extRecord, 2, extRecord[1] & ModemUtils.EFS_VALUE_ASDIV_73841_DYNAMIC_SWITCH);
            }
        } catch (RuntimeException ex) {
            Rlog.w(LOG_TAG, "Error parsing AdnRecord ext record", ex);
        }
    }

    private void parseRecord(byte[] record) {
        try {
            this.mAlphaTag = IccUtils.adnStringFieldToString(record, 0, record.length - 14);
            int footerOffset = record.length - 14;
            int numberLength = record[footerOffset] & ModemUtils.EFS_VALUE_ASDIV_73841_DYNAMIC_SWITCH;
            if (numberLength > 11) {
                this.mNumber = "";
                return;
            }
            this.mNumber = PhoneNumberUtils.calledPartyBCDToString(record, footerOffset + 1, numberLength);
            this.mExtRecord = record[record.length - 1] & ModemUtils.EFS_VALUE_ASDIV_73841_DYNAMIC_SWITCH;
            this.mEmails = null;
        } catch (RuntimeException ex) {
            Rlog.w(LOG_TAG, "Error parsing AdnRecord", ex);
            this.mNumber = "";
            this.mAlphaTag = "";
            this.mEmails = null;
        }
    }
}
