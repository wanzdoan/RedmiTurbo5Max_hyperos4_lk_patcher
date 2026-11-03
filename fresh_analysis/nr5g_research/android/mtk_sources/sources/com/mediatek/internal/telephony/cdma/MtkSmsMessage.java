package com.mediatek.internal.telephony.cdma;

import android.R;
import android.content.res.Resources;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.SmsAddress;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.cdma.SmsMessage;
import com.android.internal.telephony.cdma.sms.BearerData;
import com.android.internal.telephony.cdma.sms.CdmaSmsAddress;
import com.android.internal.telephony.cdma.sms.CdmaSmsSubaddress;
import com.android.internal.telephony.cdma.sms.SmsEnvelope;
import com.android.internal.telephony.cdma.sms.UserData;
import com.android.internal.util.BitwiseInputStream;
import com.android.internal.util.HexDump;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public class MtkSmsMessage extends SmsMessage {
    private static final String LOGGABLE_TAG = "CDMA:SMS";
    private static final String LOG_TAG = "MtkCdmaSmsMessage";
    private static final int RETURN_ACK = 1;

    public static SmsMessage createFromEfRecord(int index, byte[] data) {
        try {
            SmsMessage msg = new MtkSmsMessage();
            msg.mIndexOnIcc = index;
            if ((data[0] & 1) == 0) {
                Rlog.w(LOG_TAG, "SMS parsing failed: Trying to parse a free record");
                return null;
            }
            msg.mStatusOnIcc = data[0] & 7;
            int size = data[1] & 255;
            byte[] pdu = new byte[size];
            System.arraycopy(data, 2, pdu, 0, size);
            msg.parsePduFromEfRecord(pdu);
            return msg;
        } catch (RuntimeException ex) {
            Rlog.e(LOG_TAG, "SMS PDU parsing failed: ", ex);
            return null;
        }
    }

    public void parsePduFromEfRecord(byte[] pdu) {
        ByteArrayInputStream bais;
        DataInputStream dis;
        ByteArrayInputStream bais2 = new ByteArrayInputStream(pdu);
        DataInputStream dis2 = new DataInputStream(bais2);
        SmsEnvelope env = new SmsEnvelope();
        CdmaSmsAddress addr = new CdmaSmsAddress();
        CdmaSmsSubaddress subAddr = new CdmaSmsSubaddress();
        try {
            env.messageType = dis2.readByte();
            while (dis2.available() > 0) {
                int parameterId = dis2.readByte();
                int parameterLen = dis2.readUnsignedByte();
                byte[] parameterData = new byte[parameterLen];
                switch (parameterId) {
                    case 0:
                        bais = bais2;
                        dis = dis2;
                        env.teleService = dis.readUnsignedShort();
                        Rlog.i(LOG_TAG, "teleservice = " + env.teleService);
                        continue;
                        bais2 = bais;
                        dis2 = dis;
                        break;
                    case 1:
                        bais = bais2;
                        dis = dis2;
                        env.serviceCategory = dis.readUnsignedShort();
                        continue;
                        bais2 = bais;
                        dis2 = dis;
                        break;
                    case 2:
                    case 4:
                        bais = bais2;
                        try {
                            dis2.read(parameterData, 0, parameterLen);
                            BitwiseInputStream addrBis = new BitwiseInputStream(parameterData);
                            addr.digitMode = addrBis.read(1);
                            addr.numberMode = addrBis.read(1);
                            int numberType = 0;
                            if (addr.digitMode == 1) {
                                numberType = addrBis.read(3);
                                addr.ton = numberType;
                                if (addr.numberMode == 0) {
                                    addr.numberPlan = addrBis.read(4);
                                }
                            }
                            addr.numberOfDigits = addrBis.read(8);
                            byte[] data = new byte[addr.numberOfDigits];
                            if (addr.digitMode == 0) {
                                for (int index = 0; index < addr.numberOfDigits; index++) {
                                    byte b = (byte) (addrBis.read(4) & 15);
                                    data[index] = convertDtmfToAscii(b);
                                }
                                dis = dis2;
                            } else if (addr.digitMode != 1) {
                                dis = dis2;
                                Rlog.e(LOG_TAG, "Incorrect Digit mode");
                            } else if (addr.numberMode == 0) {
                                int index2 = 0;
                                while (index2 < addr.numberOfDigits) {
                                    DataInputStream dis3 = dis2;
                                    byte b2 = (byte) (addrBis.read(8) & 255);
                                    data[index2] = b2;
                                    index2++;
                                    dis2 = dis3;
                                    break;
                                }
                                dis = dis2;
                            } else {
                                dis = dis2;
                                if (addr.numberMode != 1) {
                                    Rlog.e(LOG_TAG, "Addr is of incorrect type");
                                } else if (numberType == 2) {
                                    Rlog.e(LOG_TAG, "TODO: Addr is email id");
                                } else {
                                    Rlog.e(LOG_TAG, "TODO: Addr is data network address");
                                }
                            }
                            try {
                                addr.origBytes = data;
                                Rlog.pii(LOG_TAG, "Addr=" + addr.toString());
                                env.origAddress = addr;
                                this.mOriginatingAddress = addr;
                                if (parameterId == 4) {
                                    env.destAddress = addr;
                                    this.mRecipientAddress = addr;
                                } else {
                                    continue;
                                }
                                bais2 = bais;
                                dis2 = dis;
                            } catch (Exception e) {
                                ex = e;
                            }
                        } catch (Exception e2) {
                            ex = e2;
                        }
                        break;
                    case 3:
                    case 5:
                        try {
                            dis2.read(parameterData, 0, parameterLen);
                            BitwiseInputStream subAddrBis = new BitwiseInputStream(parameterData);
                            subAddr.type = subAddrBis.read(3);
                            subAddr.odd = subAddrBis.readByteArray(1)[0];
                            int subAddrLen = subAddrBis.read(8);
                            byte[] subdata = new byte[subAddrLen];
                            int index3 = 0;
                            while (index3 < subAddrLen) {
                                ByteArrayInputStream bais3 = bais2;
                                try {
                                    byte b3 = (byte) (subAddrBis.read(4) & 255);
                                    subdata[index3] = convertDtmfToAscii(b3);
                                    index3++;
                                    bais2 = bais3;
                                } catch (Exception e3) {
                                    ex = e3;
                                }
                                break;
                            }
                            bais = bais2;
                            subAddr.origBytes = subdata;
                            if (parameterId == 3) {
                                env.origSubaddress = subAddr;
                                dis = dis2;
                                continue;
                            } else {
                                env.destSubaddress = subAddr;
                                dis = dis2;
                            }
                            bais2 = bais;
                            dis2 = dis;
                        } catch (Exception e4) {
                            ex = e4;
                        }
                        break;
                    case 6:
                        dis2.read(parameterData, 0, parameterLen);
                        BitwiseInputStream replyOptBis = new BitwiseInputStream(parameterData);
                        env.bearerReply = replyOptBis.read(6);
                        bais = bais2;
                        dis = dis2;
                        continue;
                        bais2 = bais;
                        dis2 = dis;
                        break;
                    case 7:
                        dis2.read(parameterData, 0, parameterLen);
                        BitwiseInputStream ccBis = new BitwiseInputStream(parameterData);
                        env.replySeqNo = ccBis.readByteArray(6)[0];
                        env.errorClass = ccBis.readByteArray(2)[0];
                        if (env.errorClass != 0) {
                            env.causeCode = ccBis.readByteArray(8)[0];
                            bais = bais2;
                            dis = dis2;
                            continue;
                        } else {
                            bais = bais2;
                            dis = dis2;
                        }
                        bais2 = bais;
                        dis2 = dis;
                        break;
                    case 8:
                        try {
                            dis2.read(parameterData, 0, parameterLen);
                            env.bearerData = parameterData;
                            bais = bais2;
                            dis = dis2;
                            continue;
                            bais2 = bais;
                            dis2 = dis;
                        } catch (Exception e5) {
                            ex = e5;
                        }
                        break;
                    default:
                        throw new Exception("unsupported parameterId (" + parameterId + ")");
                }
                Rlog.e(LOG_TAG, "parsePduFromEfRecord: conversion from pdu to SmsMessage failed" + ex);
                this.mEnvelope = env;
                this.mPdu = pdu;
                parseSms();
            }
            bais2.close();
            dis2.close();
        } catch (Exception e6) {
            ex = e6;
        }
        this.mEnvelope = env;
        this.mPdu = pdu;
        parseSms();
    }

    public String getOriginatingAddress() {
        replaceIddNddWithPluscode(this.mOriginatingAddress);
        return super.getOriginatingAddress();
    }

    public static SmsMessage.SubmitPdu getSubmitPdu(String scAddr, String destAddr, int destPort, int originalPort, byte[] data, boolean statusReportRequested) {
        SmsHeader.PortAddrs portAddrs = new SmsHeader.PortAddrs();
        portAddrs.destPort = destPort;
        portAddrs.origPort = originalPort;
        portAddrs.areEightBits = false;
        SmsHeader smsHeader = new SmsHeader();
        smsHeader.portAddrs = portAddrs;
        UserData uData = new UserData();
        if (originalPort == 0) {
            uData.userDataHeader = null;
            Rlog.d(LOG_TAG, "getSubmitPdu(with dest&original port), clear the header.");
        } else {
            uData.userDataHeader = smsHeader;
        }
        uData.msgEncoding = 0;
        uData.msgEncodingSet = true;
        uData.payload = data;
        return privateGetSubmitPdu(destAddr, statusReportRequested, uData);
    }

    public static SmsMessage.SubmitPdu getSubmitPdu(String destAddr, UserData userData, boolean statusReportRequested) {
        return privateGetPdu(destAddr, statusReportRequested, userData, 0L, -1, -1);
    }

    public static SmsMessage.SubmitPdu createEfPdu(String destinationAddress, String message, long timeStamp) {
        long scTimeMillis;
        if (destinationAddress == null || message == null) {
            return null;
        }
        UserData uData = new UserData();
        uData.payloadStr = message;
        uData.userDataHeader = null;
        if (timeStamp > 0) {
            scTimeMillis = timeStamp;
            Rlog.d(LOG_TAG, "createEfPdu, input timeStamp = " + timeStamp + ", out scTimeMillis = " + scTimeMillis);
        } else {
            Rlog.d(LOG_TAG, "createEfPdu, input timeStamp = " + timeStamp + ", dont assign time zone to this invalid value");
            scTimeMillis = timeStamp;
        }
        return privateGetPdu(destinationAddress, false, uData, scTimeMillis, -1, -1);
    }

    private static SmsMessage.SubmitPdu privateGetPdu(String destAddrStr, boolean statusReportRequested, UserData userData, long timeStamp, int validityPeriod, int priority) {
        CdmaSmsAddress destAddr = CdmaSmsAddress.parse(PhoneNumberUtils.cdmaCheckAndProcessPlusCodeForSms(destAddrStr));
        if (destAddr == null) {
            return null;
        }
        if (destAddr.numberOfDigits > 36) {
            Rlog.d(LOG_TAG, "number of digit exceeds the SMS_ADDRESS_MAX");
            return null;
        }
        BearerData bearerData = new BearerData();
        bearerData.messageType = 2;
        bearerData.messageId = getNextMessageId();
        bearerData.deliveryAckReq = statusReportRequested;
        bearerData.userAckReq = false;
        bearerData.readAckReq = false;
        bearerData.reportReq = false;
        bearerData.userData = userData;
        if (timeStamp > 0) {
            bearerData.msgCenterTimeStamp = BearerData.TimeStamp.fromMillis(timeStamp);
        }
        if (validityPeriod >= 0) {
            bearerData.validityPeriodRelativeSet = true;
            bearerData.validityPeriodRelative = validityPeriod;
        } else {
            bearerData.validityPeriodRelativeSet = false;
        }
        if (priority >= 0) {
            bearerData.priorityIndicatorSet = true;
            bearerData.priority = priority;
        } else {
            bearerData.priorityIndicatorSet = false;
        }
        byte[] encodedBearerData = BearerData.encode(bearerData);
        if (Rlog.isLoggable(LOGGABLE_TAG, 2)) {
            Rlog.d(LOG_TAG, "MO (encoded) BearerData = " + bearerData);
            if (encodedBearerData != null) {
                Rlog.d(LOG_TAG, "MO raw BearerData = '" + HexDump.toHexString(encodedBearerData) + "'");
            }
        }
        if (encodedBearerData == null) {
            return null;
        }
        int teleservice = bearerData.hasUserDataHeader ? 4101 : 4098;
        SmsEnvelope envelope = new SmsEnvelope();
        envelope.messageType = 0;
        envelope.teleService = teleservice;
        envelope.destAddress = destAddr;
        envelope.bearerReply = 1;
        envelope.bearerData = encodedBearerData;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(100);
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(envelope.teleService);
            dos.writeInt(0);
            dos.writeInt(0);
            dos.write(destAddr.digitMode);
            dos.write(destAddr.numberMode);
            dos.write(destAddr.ton);
            dos.write(destAddr.numberPlan);
            dos.write(destAddr.numberOfDigits);
            dos.write(destAddr.origBytes, 0, destAddr.origBytes.length);
            dos.write(0);
            dos.write(0);
            dos.write(0);
            dos.write(encodedBearerData.length);
            dos.write(encodedBearerData, 0, encodedBearerData.length);
            dos.close();
            SmsMessage.SubmitPdu pdu = new SmsMessage.SubmitPdu();
            pdu.encodedMessage = baos.toByteArray();
            pdu.encodedScAddress = null;
            return pdu;
        } catch (IOException ex) {
            Rlog.e(LOG_TAG, "creating SubmitPdu failed: " + ex);
            return null;
        }
    }

    public static SmsMessage.SubmitPdu getSubmitPdu(String scAddress, String destAddrStr, String message, boolean statusReportRequested, SmsHeader smsHeader, int encodingtype, int validityPeriod, int priority, boolean use7BitAscii) {
        int validityPeriod2;
        if (destAddrStr == null || message == null) {
            Log.e(LOG_TAG, "getSubmitPdu, null sms text or destination address. do nothing.");
            return null;
        }
        if (destAddrStr.isEmpty()) {
            Log.e(LOG_TAG, "getSubmitPdu, destination address is empty. do nothing.");
            return null;
        }
        if (message.isEmpty()) {
            Log.e(LOG_TAG, "getSubmitPdu, message text is empty. do nothing.");
            return null;
        }
        if (validityPeriod > 244 && validityPeriod <= 255) {
            validityPeriod2 = 244;
        } else {
            validityPeriod2 = validityPeriod;
        }
        UserData uData = new UserData();
        uData.payloadStr = message;
        uData.userDataHeader = smsHeader;
        int i = 2;
        if (encodingtype == 1) {
            if (!use7BitAscii) {
                i = 9;
            }
            uData.msgEncoding = i;
        } else if (encodingtype == 2) {
            uData.msgEncoding = 0;
        } else {
            uData.msgEncoding = 4;
        }
        uData.msgEncodingSet = true;
        return privateGetPdu(destAddrStr, statusReportRequested, uData, 0L, validityPeriod2, priority);
    }

    public static GsmAlphabet.TextEncodingDetails calculateLength(CharSequence messageBody, boolean use7bitOnly, int encodingType) {
        Resources r = Resources.getSystem();
        if (r.getBoolean(R.bool.config_useGnssHardwareProvider)) {
            Rlog.d(LOG_TAG, "here use BearerData.calcTextEncodingDetails, but divide in parent class will use Sms7BitEncodingTranslator.translate(messageBody) returned string instead again in this case, Caution!!");
            Rlog.d(LOG_TAG, "search calculateLengthCDMA for help!", new Throwable());
        }
        return BearerData.calcTextEncodingDetails(messageBody, use7bitOnly, true);
    }

    public static MtkSmsMessage newMtkSmsMessage(SmsMessage sms) {
        if (sms == null) {
            return null;
        }
        MtkSmsMessage mtkSms = new MtkSmsMessage();
        mtkSms.mScAddress = sms.getServiceCenterAddress();
        mtkSms.mOriginatingAddress = sms.mOriginatingAddress;
        mtkSms.mMessageBody = sms.getMessageBody();
        mtkSms.mPseudoSubject = sms.getPseudoSubject();
        mtkSms.mEmailFrom = sms.getEmailFrom();
        mtkSms.mEmailBody = sms.getEmailBody();
        mtkSms.mIsEmail = sms.isEmail();
        mtkSms.mScTimeMillis = sms.getTimestampMillis();
        mtkSms.mPdu = sms.getPdu();
        mtkSms.mUserData = sms.getUserData();
        mtkSms.mUserDataHeader = sms.getUserDataHeader();
        mtkSms.mIsMwi = false;
        mtkSms.mMwiSense = false;
        mtkSms.mMwiDontStore = false;
        mtkSms.mStatusOnIcc = sms.getStatusOnIcc();
        mtkSms.mIndexOnIcc = sms.getIndexOnIcc();
        mtkSms.mMessageRef = sms.mMessageRef;
        mtkSms.status = sms.getStatus() >> 16;
        mtkSms.mEnvelope = sms.mEnvelope;
        mtkSms.mBearerData = sms.mBearerData;
        return mtkSms;
    }

    private static String getMaskString(String str) {
        if (str == null) {
            return "null";
        }
        if (str.length() > 2) {
            return str.substring(0, str.length() >> 1) + "xxxxx";
        }
        return "xx";
    }

    private static String handlePlusCodeInternal(int ton, String number) {
        String ret = number;
        if (TextUtils.isEmpty(ret)) {
            return null;
        }
        if (ton == 1 && number.charAt(0) != '+') {
            ret = "+" + ret;
        }
        Rlog.d(LOG_TAG, "handlePlusCodeInternal, ton = " + ton + " number = " + getMaskString(number) + " the address = " + getMaskString(ret));
        return ret;
    }

    private static void replaceIddNddWithPluscode(SmsAddress addr) {
        if (addr == null) {
            return;
        }
        String orignalStr = new String(addr.origBytes);
        String number = handlePlusCodeInternal(addr.ton, orignalStr);
        if (TextUtils.isEmpty(number)) {
            return;
        }
        boolean changed = !number.equals(orignalStr);
        if (changed) {
            addr.origBytes = number.getBytes();
            addr.address = number;
        }
    }

    public String getDestinationAddress() {
        return getOriginatingAddress();
    }

    public void parseSms() {
        if (this.mEnvelope.teleService == 262144) {
            this.mBearerData = new BearerData();
            if (this.mEnvelope.bearerData != null) {
                this.mBearerData.numberOfMessages = this.mEnvelope.bearerData[0] & 255;
            }
            Rlog.d(LOG_TAG, "parseSms: get MWI " + Integer.toString(this.mBearerData.numberOfMessages));
            return;
        }
        this.mBearerData = BearerData.decode(this.mEnvelope.bearerData);
        if (Rlog.isLoggable(LOGGABLE_TAG, 2)) {
            Rlog.d(LOG_TAG, "MT raw BearerData = '" + HexDump.toHexString(this.mEnvelope.bearerData) + "'");
            Rlog.d(LOG_TAG, "MT (decoded) BearerData = " + this.mBearerData);
        }
        if (this.mBearerData == null) {
            throw new RuntimeException("Unsupported message: BearerData decode failed.");
        }
        this.mMessageRef = this.mBearerData.messageId;
        if (this.mBearerData.userData != null) {
            this.mUserData = this.mBearerData.userData.payload;
            this.mUserDataHeader = this.mBearerData.userData.userDataHeader;
            this.mMessageBody = this.mBearerData.userData.payloadStr;
        }
        if (this.mOriginatingAddress != null) {
            decodeSmsDisplayAddress(this.mOriginatingAddress);
        }
        if (this.mRecipientAddress != null) {
            decodeSmsDisplayAddress(this.mRecipientAddress);
        }
        if (this.mBearerData.msgCenterTimeStamp != null) {
            this.mScTimeMillis = this.mBearerData.msgCenterTimeStamp.toMillis();
        }
        if (this.mBearerData.messageType == 4) {
            if (!this.mBearerData.messageStatusSet) {
                Rlog.d(LOG_TAG, "DELIVERY_ACK message without msgStatus (" + (this.mUserData == null ? "also missing" : "does have") + " userData).");
                this.status = 2;
            } else {
                this.status = this.mBearerData.errorClass << 8;
                this.status |= this.mBearerData.messageStatus;
            }
        } else if (this.mBearerData.messageType != 1 && this.mBearerData.messageType != 2) {
            throw new RuntimeException("Unsupported message type: " + this.mBearerData.messageType);
        }
        if (this.mMessageBody != null) {
            parseMessageBody();
        }
    }
}
