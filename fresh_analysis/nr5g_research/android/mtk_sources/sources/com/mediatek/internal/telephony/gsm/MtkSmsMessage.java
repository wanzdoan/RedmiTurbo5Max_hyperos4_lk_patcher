package com.mediatek.internal.telephony.gsm;

import android.R;
import android.content.res.Resources;
import android.os.Build;
import android.os.SystemProperties;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.text.TextUtils;
import android.text.format.Time;
import com.android.internal.telephony.EncodeException;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.Sms7BitEncodingTranslator;
import com.android.internal.telephony.SmsConstants;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.gsm.SmsMessage;
import com.mediatek.internal.telephony.MtkPhoneNumberUtils;
import com.mediatek.internal.telephony.MtkRILConstants;
import com.mediatek.internal.telephony.MtkSmsHeader;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class MtkSmsMessage extends SmsMessage {
    public static final int ENCODING_7BIT_LOCKING = 12;
    public static final int ENCODING_7BIT_LOCKING_SINGLE = 13;
    public static final int ENCODING_7BIT_SINGLE = 11;
    private static final boolean ENG = "eng".equals(Build.TYPE);
    static final String LOG_TAG = "MtkSmsMessage";
    public static final int MASK_MESSAGE_TYPE_INDICATOR = 3;
    public static final int MASK_USER_DATA_HEADER_INDICATOR = 64;
    public static final int MASK_VALIDITY_PERIOD_FORMAT = 24;
    public static final int MASK_VALIDITY_PERIOD_FORMAT_ABSOLUTE = 24;
    public static final int MASK_VALIDITY_PERIOD_FORMAT_ENHANCED = 8;
    public static final int MASK_VALIDITY_PERIOD_FORMAT_NONE = 0;
    public static final int MASK_VALIDITY_PERIOD_FORMAT_RELATIVE = 16;
    protected int absoluteValidityPeriod;
    protected String mDestinationAddress;
    protected int relativeValidityPeriod;
    private int mEncodingType = 0;
    protected int mwiType = -1;
    protected int mwiCount = 0;

    public static class DeliverPdu {
        public byte[] encodedMessage;
        public byte[] encodedScAddress;

        public String toString() {
            return "DeliverPdu: encodedScAddress = " + Arrays.toString(this.encodedScAddress) + ", encodedMessage = " + Arrays.toString(this.encodedMessage);
        }
    }

    public static MtkSmsMessage createFromPdu(byte[] pdu) {
        try {
            MtkSmsMessage msg = new MtkSmsMessage();
            msg.parsePdu(pdu);
            return msg;
        } catch (OutOfMemoryError e) {
            Rlog.e(LOG_TAG, "SMS PDU parsing failed with out of memory: ", e);
            return null;
        } catch (RuntimeException ex) {
            Rlog.e(LOG_TAG, "SMS PDU parsing failed: ", ex);
            return null;
        }
    }

    public static MtkSmsMessage createFromEfRecord(int index, byte[] data) {
        try {
            MtkSmsMessage msg = new MtkSmsMessage();
            msg.mIndexOnIcc = index;
            if ((data[0] & 1) == 0) {
                Rlog.w(LOG_TAG, "SMS parsing failed: Trying to parse a free record");
                return null;
            }
            msg.mStatusOnIcc = data[0] & 7;
            int size = data.length - 1;
            byte[] pdu = new byte[size];
            System.arraycopy(data, 1, pdu, 0, size);
            msg.parsePdu(pdu);
            return msg;
        } catch (RuntimeException ex) {
            Rlog.e(LOG_TAG, "SMS PDU parsing failed: ", ex);
            return null;
        }
    }

    public String getDestinationAddress() {
        if (this.mDestinationAddress == null) {
            return null;
        }
        return this.mDestinationAddress;
    }

    protected void parseSmsStatusReport(SmsMessage.PduParser p, int firstByte) {
        super.parseSmsStatusReport(p, firstByte);
        this.mMessageBody = "";
    }

    protected void parseSmsSubmit(SmsMessage.PduParser p, int firstByte) {
        super.parseSmsSubmit(p, firstByte);
        if (this.mRecipientAddress != null) {
            this.mDestinationAddress = this.mRecipientAddress.getAddressString();
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:22:0x0068  */
    /* JADX WARN: Code duplicated, block: B:94:0x01f4  */
    protected void parseUserData(SmsMessage.PduParser p, boolean hasUserDataHeader) {
        boolean hasMessageClass = false;
        int encodingType = 0;
        int i = 128;
        if ((this.mDataCodingScheme & 128) == 0) {
            boolean userDataCompressed = (this.mDataCodingScheme & 32) != 0;
            hasMessageClass = (this.mDataCodingScheme & 16) != 0;
            if (userDataCompressed) {
                Rlog.w(LOG_TAG, "4 - Unsupported SMS data coding scheme (compression) " + (this.mDataCodingScheme & 255));
            } else {
                switch ((this.mDataCodingScheme >> 2) & 3) {
                    case 0:
                        encodingType = 1;
                        break;
                    case 1:
                        Resources r = Resources.getSystem();
                        if (!r.getBoolean(R.bool.config_useFixedVolume)) {
                            Rlog.w(LOG_TAG, "1 - Unsupported SMS data coding scheme " + (this.mDataCodingScheme & 255));
                            encodingType = 2;
                        } else {
                            encodingType = 2;
                        }
                        break;
                    case 2:
                        encodingType = 3;
                        break;
                    case 3:
                        Rlog.w(LOG_TAG, "1 - Unsupported SMS data coding scheme " + (this.mDataCodingScheme & 255));
                        encodingType = 2;
                        break;
                }
            }
        } else if ((this.mDataCodingScheme & 240) == 240) {
            hasMessageClass = true;
            encodingType = (this.mDataCodingScheme & 4) == 0 ? 1 : 2;
        } else if ((this.mDataCodingScheme & 240) == 192 || (this.mDataCodingScheme & 240) == 208 || (this.mDataCodingScheme & 240) == 224) {
            if ((this.mDataCodingScheme & 240) == 224) {
                encodingType = 3;
            } else {
                encodingType = 1;
            }
            boolean active = (this.mDataCodingScheme & 8) == 8;
            if ((this.mDataCodingScheme & 3) != 0) {
                this.mIsMwi = false;
                Rlog.w(LOG_TAG, "MWI in DCS for fax/email/other: " + (this.mDataCodingScheme & 255));
            } else {
                this.mIsMwi = true;
                this.mMwiSense = active;
                this.mMwiDontStore = (this.mDataCodingScheme & 240) == 192;
                if (active) {
                    this.mVoiceMailCount = -1;
                } else {
                    this.mVoiceMailCount = 0;
                }
                Rlog.w(LOG_TAG, "MWI in DCS for Vmail. DCS = " + (this.mDataCodingScheme & 255) + " Dont store = " + this.mMwiDontStore + " vmail count = " + this.mVoiceMailCount);
            }
        } else if ((this.mDataCodingScheme & 192) != 128) {
            Rlog.w(LOG_TAG, "3 - Unsupported SMS data coding scheme " + (this.mDataCodingScheme & 255));
        } else if (this.mDataCodingScheme != 132) {
            Rlog.w(LOG_TAG, "5 - Unsupported SMS data coding scheme " + (this.mDataCodingScheme & 255));
        } else {
            encodingType = 4;
        }
        int count = p.constructUserData(hasUserDataHeader, encodingType == 1);
        this.mUserData = p.getUserData();
        this.mUserDataHeader = p.getUserDataHeader();
        this.mEncodingType = encodingType;
        if (hasUserDataHeader && this.mUserDataHeader.specialSmsMsgList.size() != 0) {
            for (SmsHeader.SpecialSmsMsg msg : this.mUserDataHeader.specialSmsMsgList) {
                int msgInd = msg.msgIndType & 255;
                if (msgInd == 0 || msgInd == i) {
                    this.mIsMwi = true;
                    if (msgInd == i) {
                        this.mMwiDontStore = false;
                    } else if (!this.mMwiDontStore) {
                        if ((this.mDataCodingScheme & 240) == 208 || (this.mDataCodingScheme & 240) == 224) {
                            if ((this.mDataCodingScheme & 3) != 0) {
                                this.mMwiDontStore = true;
                            }
                        } else {
                            this.mMwiDontStore = true;
                        }
                    }
                    this.mVoiceMailCount = msg.msgCount & 255;
                    if (this.mVoiceMailCount <= 0) {
                        this.mMwiSense = false;
                    } else {
                        this.mMwiSense = true;
                    }
                    Rlog.w(LOG_TAG, "MWI in TP-UDH for Vmail. Msg Ind = " + msgInd + " Dont store = " + this.mMwiDontStore + " Vmail count = " + this.mVoiceMailCount);
                } else {
                    Rlog.w(LOG_TAG, "TP_UDH fax/email/extended msg/multisubscriber profile. Msg Ind = " + msgInd);
                }
                i = 128;
            }
        }
        switch (encodingType) {
            case 0:
                this.mMessageBody = null;
                break;
            case 1:
                this.mMessageBody = p.getUserDataGSM7Bit(count, hasUserDataHeader ? this.mUserDataHeader.languageTable : 0, hasUserDataHeader ? this.mUserDataHeader.languageShiftTable : 0);
                break;
            case 2:
                Resources r2 = Resources.getSystem();
                if (r2.getBoolean(R.bool.config_useFixedVolume)) {
                    this.mMessageBody = p.getUserDataGSM8bit(count);
                } else {
                    this.mMessageBody = null;
                }
                break;
            case 3:
                this.mMessageBody = p.getUserDataUCS2(count);
                break;
            case 4:
                this.mMessageBody = p.getUserDataKSC5601(count);
                break;
        }
        if (this.mMessageBody != null) {
            parseMessageBody();
        }
        if (!hasMessageClass) {
            this.messageClass = SmsConstants.MessageClass.UNKNOWN;
            return;
        }
        switch (this.mDataCodingScheme & 3) {
            case 0:
                this.messageClass = SmsConstants.MessageClass.CLASS_0;
                break;
            case 1:
                this.messageClass = SmsConstants.MessageClass.CLASS_1;
                break;
            case 2:
                this.messageClass = SmsConstants.MessageClass.CLASS_2;
                break;
            case 3:
                this.messageClass = SmsConstants.MessageClass.CLASS_3;
                break;
        }
    }

    public static SmsMessage.SubmitPdu getSubmitPdu(String scAddress, String destinationAddress, int destinationPort, int originalPort, byte[] data, boolean statusReportRequested) {
        byte[] smsHeaderData = MtkSmsHeader.getSubmitPduHeader(destinationPort, originalPort);
        Rlog.d(LOG_TAG, "MtkSmsMessage: get submit pdu originalPort = " + originalPort);
        if (smsHeaderData == null && originalPort != 0) {
            return null;
        }
        if (originalPort == 0) {
            return getSubmitPdu(scAddress, destinationAddress, data, statusReportRequested);
        }
        return getSubmitPdu(scAddress, destinationAddress, data, smsHeaderData, statusReportRequested);
    }

    public static SmsMessage.SubmitPdu getSubmitPdu(String scAddress, String destinationAddress, String message, int destPort, boolean statusReportRequested) {
        int language;
        int encoding;
        int language2 = getCurrentSysLanguage();
        int singleId = -1;
        int lockingId = -1;
        GsmAlphabet.TextEncodingDetails ted = new GsmAlphabet.TextEncodingDetails();
        if (encodeStringWithSpecialLang(message, language2, ted)) {
            if (ted.useLockingShift && ted.useSingleShift) {
                lockingId = language2;
                singleId = language2;
                language = language2;
                encoding = 13;
            } else if (ted.useLockingShift) {
                lockingId = language2;
                language = language2;
                encoding = 12;
            } else if (ted.useSingleShift) {
                singleId = language2;
                language = language2;
                encoding = 11;
            } else {
                language = -1;
                encoding = 1;
            }
        } else {
            language = language2;
            encoding = 3;
        }
        byte[] smsHeaderData = MtkSmsHeader.getSubmitPduHeaderWithLang(destPort, singleId, lockingId);
        return getSubmitPduWithLang(scAddress, destinationAddress, message, statusReportRequested, smsHeaderData, encoding, language, -1);
    }

    public static SmsMessage.SubmitPdu getSubmitPdu(String scAddress, String destinationAddress, byte[] data, byte[] smsHeaderData, boolean statusReportRequested) {
        if (data.length + smsHeaderData.length + 1 > 140) {
            Rlog.e(LOG_TAG, "SMS data message may only contain " + ((140 - smsHeaderData.length) - 1) + " bytes");
            return null;
        }
        SmsMessage.SubmitPdu ret = new SmsMessage.SubmitPdu();
        ByteArrayOutputStream bo = getSubmitPduHead(scAddress, destinationAddress, (byte) 65, statusReportRequested, ret);
        if (bo == null) {
            return null;
        }
        bo.write(4);
        bo.write(data.length + smsHeaderData.length + 1);
        bo.write(smsHeaderData.length);
        bo.write(smsHeaderData, 0, smsHeaderData.length);
        bo.write(data, 0, data.length);
        ret.encodedMessage = bo.toByteArray();
        return ret;
    }

    public static SmsMessage.SubmitPdu getSubmitPdu(String scAddress, String destinationAddress, byte[] data, boolean statusReportRequested) {
        Rlog.d(LOG_TAG, "get SubmitPdu for auto regist data.length = " + data.length + " bytes");
        if (data.length + 1 > 140) {
            Rlog.e(LOG_TAG, "data length is too long,SMS data.length = " + data.length + " bytes");
            return null;
        }
        SmsMessage.SubmitPdu ret = new SmsMessage.SubmitPdu();
        ByteArrayOutputStream bo = getSubmitPduHead(scAddress, destinationAddress, (byte) 1, statusReportRequested, ret);
        if (bo == null) {
            return null;
        }
        bo.write(4);
        bo.write(data.length);
        bo.write(data, 0, data.length);
        ret.encodedMessage = bo.toByteArray();
        return ret;
    }

    public static SmsMessage.SubmitPdu getSubmitPduWithLang(String scAddress, String destinationAddress, String message, boolean statusReportRequested, byte[] header, int encoding, int language, int validityPeriod) {
        int validityPeriodFormat;
        int encoding2;
        byte[] userData;
        Rlog.d(LOG_TAG, "SmsMessage: get submit pdu with Lang");
        if (message != null && destinationAddress != null) {
            SmsMessage.SubmitPdu ret = new SmsMessage.SubmitPdu();
            int relativeValidityPeriod = getRelativeValidityPeriod(validityPeriod);
            if (relativeValidityPeriod < 0) {
                validityPeriodFormat = 0;
            } else {
                validityPeriodFormat = 2;
            }
            byte mtiByte = (byte) ((validityPeriodFormat << 3) | 1 | (header != null ? 64 : 0));
            ByteArrayOutputStream bo = getSubmitPduHead(scAddress, destinationAddress, mtiByte, statusReportRequested, ret);
            if (bo == null) {
                return null;
            }
            if (encoding != 0) {
                encoding2 = encoding;
            } else {
                encoding2 = 1;
            }
            try {
                Rlog.d(LOG_TAG, "Get SubmitPdu with Lang " + encoding2 + " " + language);
                if (encoding2 == 1) {
                    userData = GsmAlphabet.stringToGsm7BitPackedWithHeader(message, header, 0, 0);
                } else if (language > 0 && encoding2 != 3) {
                    if (encoding2 == 12) {
                        userData = GsmAlphabet.stringToGsm7BitPackedWithHeader(message, header, 0, language);
                    } else if (encoding2 == 11) {
                        userData = GsmAlphabet.stringToGsm7BitPackedWithHeader(message, header, language, 0);
                    } else if (encoding2 == 13) {
                        userData = GsmAlphabet.stringToGsm7BitPackedWithHeader(message, header, language, language);
                    } else {
                        userData = GsmAlphabet.stringToGsm7BitPackedWithHeader(message, header, 0, 0);
                    }
                    encoding2 = 1;
                } else {
                    try {
                        try {
                            userData = encodeUCS2(message, header);
                        } catch (EncodeException ucs2Ex) {
                            Rlog.e(LOG_TAG, "Implausible EncodeException ", ucs2Ex);
                            return null;
                        }
                    } catch (UnsupportedEncodingException uex) {
                        Rlog.e(LOG_TAG, "Implausible UnsupportedEncodingException ", uex);
                        return null;
                    }
                }
            } catch (EncodeException e) {
                try {
                    userData = encodeUCS2(message, header);
                    encoding2 = 3;
                } catch (UnsupportedEncodingException uex2) {
                    Rlog.e(LOG_TAG, "Implausible UnsupportedEncodingException ", uex2);
                    return null;
                } catch (EncodeException ucs2Ex2) {
                    Rlog.e(LOG_TAG, "Implausible EncodeException ", ucs2Ex2);
                    return null;
                }
            }
            if (encoding2 == 1) {
                if ((userData[0] & 255) > 160) {
                    return null;
                }
                bo.write(0);
            } else {
                if ((userData[0] & 255) > 140) {
                    return null;
                }
                bo.write(8);
            }
            if (validityPeriodFormat == 2) {
                bo.write(relativeValidityPeriod);
            }
            bo.write(userData, 0, userData.length);
            ret.encodedMessage = bo.toByteArray();
            return ret;
        }
        return null;
    }

    public static DeliverPdu getDeliverPduWithLang(String scAddress, String originalAddress, String message, byte[] header, long timestamp, int encoding, int language) {
        int encoding2;
        DeliverPdu deliverPdu;
        byte[] userData;
        Rlog.d(LOG_TAG, "SmsMessage: get deliver pdu");
        if (message != null && originalAddress != null) {
            DeliverPdu ret = new DeliverPdu();
            Rlog.d(LOG_TAG, "SmsMessage: UDHI = " + (header != null));
            byte mtiByte = (byte) ((header != null ? 64 : 0) | 0);
            ByteArrayOutputStream bo = getDeliverPduHead(scAddress, originalAddress, mtiByte, ret);
            if (encoding != 0) {
                encoding2 = encoding;
            } else {
                encoding2 = 1;
            }
            try {
                deliverPdu = null;
                try {
                    Rlog.d(LOG_TAG, "Get SubmitPdu with Lang " + encoding2 + " " + language);
                    if (encoding2 == 1) {
                        userData = GsmAlphabet.stringToGsm7BitPackedWithHeader(message, header, 0, 0);
                    } else if (language > 0 && encoding2 != 3) {
                        if (encoding2 == 12) {
                            userData = GsmAlphabet.stringToGsm7BitPackedWithHeader(message, header, 0, language);
                        } else if (encoding2 == 11) {
                            userData = GsmAlphabet.stringToGsm7BitPackedWithHeader(message, header, language, 0);
                        } else if (encoding2 == 13) {
                            userData = GsmAlphabet.stringToGsm7BitPackedWithHeader(message, header, language, language);
                        } else {
                            userData = GsmAlphabet.stringToGsm7BitPackedWithHeader(message, header, 0, 0);
                        }
                        encoding2 = 1;
                    } else {
                        try {
                            try {
                                userData = encodeUCS2(message, header);
                            } catch (EncodeException ucs2Ex) {
                                Rlog.e(LOG_TAG, "Implausible EncodeException ", ucs2Ex);
                                return null;
                            }
                        } catch (UnsupportedEncodingException uex) {
                            Rlog.e(LOG_TAG, "Implausible UnsupportedEncodingException ", uex);
                            return null;
                        }
                    }
                } catch (EncodeException e) {
                    e = e;
                    try {
                        userData = encodeUCS2(message, header);
                        encoding2 = 3;
                    } catch (EncodeException ucs2Ex2) {
                        Rlog.e(LOG_TAG, "Implausible EncodeException ", ucs2Ex2);
                        return deliverPdu;
                    } catch (UnsupportedEncodingException uex2) {
                        Rlog.e(LOG_TAG, "Implausible UnsupportedEncodingException ", uex2);
                        return deliverPdu;
                    }
                }
            } catch (EncodeException e2) {
                e = e2;
                deliverPdu = null;
            }
            if ((userData[0] & 255) > 160) {
                Rlog.d(LOG_TAG, "SmsMessage: message is too long");
                return deliverPdu;
            }
            if (encoding2 == 1) {
                bo.write(0);
            } else {
                bo.write(8);
            }
            byte[] scts = parseSCTimestamp(timestamp);
            if (scts != null) {
                bo.write(scts, 0, scts.length);
            } else {
                for (int i = 0; i < 7; i++) {
                    bo.write(0);
                }
            }
            int i2 = userData.length;
            bo.write(userData, 0, i2);
            ret.encodedMessage = bo.toByteArray();
            return ret;
        }
        return null;
    }

    private static byte[] parseSCTimestamp(long millis) {
        Time t = new Time("UTC");
        t.set(millis);
        byte[] scts = {intToGsmBCDByte(t.year), intToGsmBCDByte(t.month + 1), intToGsmBCDByte(t.monthDay), intToGsmBCDByte(t.hour), intToGsmBCDByte(t.minute), intToGsmBCDByte(t.second), intToGsmBCDByte(0)};
        return scts;
    }

    private static byte intToGsmBCDByte(int value) {
        if (value < 0) {
            Rlog.d(LOG_TAG, "[time invalid value: " + value);
            return (byte) 0;
        }
        int value2 = value % 100;
        Rlog.d(LOG_TAG, "[time value: " + value2);
        byte b = (byte) (((value2 / 10) & 15) | (((value2 % 10) << 4) & 240));
        Rlog.d(LOG_TAG, "[time bcd value: " + ((int) b));
        return b;
    }

    private static ByteArrayOutputStream getDeliverPduHead(String scAddress, String originalAddress, byte mtiByte, DeliverPdu ret) {
        ByteArrayOutputStream bo = new ByteArrayOutputStream(180);
        if (scAddress == null) {
            ret.encodedScAddress = null;
        } else {
            ret.encodedScAddress = PhoneNumberUtils.networkPortionToCalledPartyBCDWithLength(scAddress);
        }
        bo.write(mtiByte);
        byte[] oaBytes = PhoneNumberUtils.networkPortionToCalledPartyBCD(originalAddress);
        if (oaBytes != null) {
            bo.write(((oaBytes.length - 1) * 2) - ((oaBytes[oaBytes.length - 1] & 240) != 240 ? 0 : 1));
            bo.write(oaBytes, 0, oaBytes.length);
        } else {
            try {
                oaBytes = GsmAlphabet.stringToGsm7BitPacked(originalAddress);
            } catch (EncodeException ex) {
                Rlog.d(LOG_TAG, "ex:" + ex);
            }
            if (oaBytes == null) {
                Rlog.d(LOG_TAG, "write a empty address for deliver pdu");
                bo.write(0);
                bo.write(MtkPhoneNumberUtils.TOA_International);
            } else {
                bo.write((oaBytes.length - 1) * 2);
                Rlog.d(LOG_TAG, "oaBytes length = " + oaBytes.length);
                bo.write(208);
                bo.write(oaBytes, 1, oaBytes.length - 1);
            }
        }
        bo.write(0);
        return bo;
    }

    private static boolean encodeStringWithSpecialLang(CharSequence msgBody, int language, GsmAlphabet.TextEncodingDetails ted) {
        int septets = GsmAlphabet.countGsmSeptetsUsingTables(msgBody, true, 0, 0);
        if (septets != -1) {
            ted.codeUnitCount = septets;
            if (septets > 160) {
                ted.msgCount = (septets / 153) + 1;
                ted.codeUnitsRemaining = 153 - (septets % 153);
            } else {
                ted.msgCount = 1;
                ted.codeUnitsRemaining = 160 - septets;
            }
            ted.codeUnitSize = 1;
            ted.shiftLangId = -1;
            Rlog.d(LOG_TAG, "Try Default: " + language + " " + ted);
            return true;
        }
        int septets2 = GsmAlphabet.countGsmSeptetsUsingTables(msgBody, true, 0, language);
        if (septets2 != -1) {
            int[] headerElt = {37, 65535};
            int maxLength = computeRemainUserDataLength(true, headerElt);
            ted.codeUnitCount = septets2;
            if (septets2 > maxLength) {
                headerElt[1] = 0;
                int maxLength2 = computeRemainUserDataLength(true, headerElt);
                ted.msgCount = (septets2 / maxLength2) + 1;
                ted.codeUnitsRemaining = maxLength2 - (septets2 % maxLength2);
            } else {
                ted.msgCount = 1;
                ted.codeUnitsRemaining = maxLength - septets2;
            }
            ted.codeUnitSize = 1;
            ted.useLockingShift = true;
            ted.shiftLangId = language;
            Rlog.d(LOG_TAG, "Try Locking Shift: " + language + " " + ted);
            return true;
        }
        int septets3 = GsmAlphabet.countGsmSeptetsUsingTables(msgBody, true, language, 0);
        if (septets3 != -1) {
            int[] headerElt2 = {36, 65535};
            int maxLength3 = computeRemainUserDataLength(true, headerElt2);
            ted.codeUnitCount = septets3;
            if (septets3 > maxLength3) {
                headerElt2[1] = 0;
                int maxLength4 = computeRemainUserDataLength(true, headerElt2);
                ted.msgCount = (septets3 / maxLength4) + 1;
                ted.codeUnitsRemaining = maxLength4 - (septets3 % maxLength4);
            } else {
                ted.msgCount = 1;
                ted.codeUnitsRemaining = maxLength3 - septets3;
            }
            ted.codeUnitSize = 1;
            ted.useSingleShift = true;
            ted.shiftLangId = language;
            Rlog.d(LOG_TAG, "Try Single Shift: " + language + " " + ted);
            return true;
        }
        int septets4 = GsmAlphabet.countGsmSeptetsUsingTables(msgBody, true, language, language);
        if (septets4 == -1) {
            Rlog.d(LOG_TAG, "Use UCS2" + language + " " + ted);
            return false;
        }
        int[] headerElt3 = {37, 36, 65535};
        int maxLength5 = computeRemainUserDataLength(true, headerElt3);
        ted.codeUnitCount = septets4;
        if (septets4 > maxLength5) {
            headerElt3[2] = 0;
            int maxLength6 = computeRemainUserDataLength(true, headerElt3);
            ted.msgCount = (septets4 / maxLength6) + 1;
            ted.codeUnitsRemaining = maxLength6 - (septets4 % maxLength6);
        } else {
            ted.msgCount = 1;
            ted.codeUnitsRemaining = maxLength5 - septets4;
        }
        ted.codeUnitSize = 1;
        ted.useLockingShift = true;
        ted.useSingleShift = true;
        ted.shiftLangId = language;
        Rlog.d(LOG_TAG, "Try Locking & Single Shift: " + language + " " + ted);
        return true;
    }

    private static int getCurrentSysLanguage() {
        String language = SystemProperties.get("persist.sys.language", (String) null);
        if (language == null) {
            language = SystemProperties.get("ro.product.locale.language", (String) null);
        }
        if (language.equals("tr")) {
            return -1;
        }
        return -1;
    }

    public static int computeRemainUserDataLength(boolean inSeptets, int[] headerElt) {
        int headerBytes = 0;
        for (int i : headerElt) {
            switch (i) {
                case 0:
                    headerBytes += 5;
                    break;
                case MtkRILConstants.MIMO_CONFIG_RAS_4RX_2RX_ENABLE /* 36 */:
                    headerBytes += 3;
                    break;
                case 37:
                    headerBytes += 3;
                    break;
            }
        }
        if (headerBytes != 0) {
            headerBytes++;
        }
        int count = 140 - headerBytes;
        if (inSeptets) {
            return (count * 8) / 7;
        }
        return count;
    }

    public static GsmAlphabet.TextEncodingDetails calculateLength(CharSequence msgBody, boolean use7bitOnly, int encodingType) {
        CharSequence newMsgBody = null;
        Resources r = Resources.getSystem();
        if (r.getBoolean(R.bool.config_useGnssHardwareProvider)) {
            newMsgBody = Sms7BitEncodingTranslator.translate(msgBody, false);
        }
        if (TextUtils.isEmpty(newMsgBody)) {
            newMsgBody = msgBody;
        }
        GsmAlphabet.TextEncodingDetails ted = GsmAlphabet.countGsmSeptets(newMsgBody, use7bitOnly);
        if (encodingType == 3) {
            Rlog.d(LOG_TAG, "input mode is unicode");
            ted = null;
        }
        if (ted == null) {
            Rlog.d(LOG_TAG, "7-bit encoding fail");
            return SmsMessageBase.calcUnicodeEncodingDetails(newMsgBody);
        }
        return ted;
    }

    public static SmsMessage.SubmitPdu getSubmitPdu(String scAddress, String destinationAddress, String message, boolean statusReportRequested, byte[] header, int encoding, int languageTable, int languageShiftTable, int validityPeriod, int messageRef) {
        byte[] header2;
        int encoding2;
        int languageTable2;
        int languageShiftTable2;
        int validityPeriodFormat;
        byte[] userData;
        if (message == null || destinationAddress == null) {
            return null;
        }
        if (encoding != 0) {
            header2 = header;
            encoding2 = encoding;
            languageTable2 = languageTable;
            languageShiftTable2 = languageShiftTable;
        } else {
            GsmAlphabet.TextEncodingDetails ted = calculateLength(message, false);
            encoding2 = ted.codeUnitSize;
            languageTable2 = ted.languageTable;
            languageShiftTable2 = ted.languageShiftTable;
            if (encoding2 == 1 && (languageTable2 != 0 || languageShiftTable2 != 0)) {
                if (header != null) {
                    SmsHeader smsHeader = SmsHeader.fromByteArray(header);
                    if (smsHeader.languageTable == languageTable2 && smsHeader.languageShiftTable == languageShiftTable2) {
                        header2 = header;
                    } else {
                        Rlog.w(LOG_TAG, "Updating language table in SMS header: " + smsHeader.languageTable + " -> " + languageTable2 + ", " + smsHeader.languageShiftTable + " -> " + languageShiftTable2);
                        smsHeader.languageTable = languageTable2;
                        smsHeader.languageShiftTable = languageShiftTable2;
                        header2 = SmsHeader.toByteArray(smsHeader);
                    }
                } else {
                    MtkSmsHeader smsHeader2 = (MtkSmsHeader) makeSmsHeader();
                    smsHeader2.languageTable = languageTable2;
                    smsHeader2.languageShiftTable = languageShiftTable2;
                    header2 = SmsHeader.toByteArray(smsHeader2);
                }
            } else {
                header2 = header;
            }
        }
        SmsMessage.SubmitPdu ret = new SmsMessage.SubmitPdu();
        int relativeValidityPeriod = getRelativeValidityPeriod(validityPeriod);
        if (relativeValidityPeriod < 0) {
            validityPeriodFormat = 0;
        } else {
            validityPeriodFormat = 2;
        }
        byte mtiByte = (byte) ((validityPeriodFormat << 3) | 1 | (header2 != null ? 64 : 0));
        int validityPeriodFormat2 = validityPeriodFormat;
        ByteArrayOutputStream bo = getSubmitPduHead(scAddress, destinationAddress, mtiByte, statusReportRequested, ret, messageRef);
        if (bo == null) {
            return null;
        }
        try {
            if (encoding2 == 1) {
                userData = GsmAlphabet.stringToGsm7BitPackedWithHeader(message, header2, languageTable2, languageShiftTable2);
            } else {
                try {
                    userData = encodeUCS2(message, header2);
                } catch (EncodeException ucs2Ex) {
                    Rlog.e(LOG_TAG, "Implausible EncodeException ", ucs2Ex);
                    return null;
                } catch (UnsupportedEncodingException uex) {
                    Rlog.e(LOG_TAG, "Implausible UnsupportedEncodingException ", uex);
                    return null;
                }
            }
        } catch (EncodeException e) {
            try {
                userData = encodeUCS2(message, header2);
                encoding2 = 3;
            } catch (EncodeException ucs2Ex2) {
                Rlog.e(LOG_TAG, "Implausible EncodeException ", ucs2Ex2);
                return null;
            } catch (UnsupportedEncodingException uex2) {
                Rlog.e(LOG_TAG, "Implausible UnsupportedEncodingException ", uex2);
                return null;
            }
        }
        if (encoding2 == 1) {
            if ((userData[0] & 255) > 160) {
                Rlog.e(LOG_TAG, "Message too long (" + (userData[0] & 255) + " septets)");
                return null;
            }
            bo.write(0);
        } else {
            if ((userData[0] & 255) > 140) {
                Rlog.e(LOG_TAG, "Message too long (" + (userData[0] & 255) + " bytes)");
                return null;
            }
            bo.write(8);
        }
        if (validityPeriod >= 0 && validityPeriod <= 255) {
            Rlog.d(LOG_TAG, "write validity period into pdu: " + validityPeriod);
            bo.write(validityPeriod);
        }
        if (validityPeriodFormat2 == 2) {
            bo.write(relativeValidityPeriod);
        }
        bo.write(userData, 0, userData.length);
        ret.encodedMessage = bo.toByteArray();
        return ret;
    }

    public int getEncodingType() {
        return this.mEncodingType;
    }
}
