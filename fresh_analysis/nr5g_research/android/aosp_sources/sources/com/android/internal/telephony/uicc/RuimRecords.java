package com.android.internal.telephony.uicc;

import android.content.Context;
import android.content.res.Resources;
import android.internal.telephony.sysprop.TelephonyProperties;
import android.os.AsyncResult;
import android.os.Message;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyBaseUtilsStub;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.BitwiseInputStream;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.TelephonyCommonUtilsStub;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class RuimRecords extends IccRecords {
    protected static final int EVENT_GET_SST_DONE = 17;
    protected boolean mCsimSpnDisplayCondition;
    private byte[] mEFli;
    private byte[] mEFpl;
    private String mHomeNetworkId;
    private String mHomeSystemId;
    private String mMdn;
    private String mMin;
    protected String mMin2Min1;
    protected String mMyMobileNumber;
    private String mNai;
    private boolean mOtaCommited;
    protected String mPrlVersion;

    private static boolean isPrintableAscii(char c) {
        return (' ' <= c && c <= '~') || c == '\r' || c == '\n';
    }

    protected void onGetImsiDone(String str) {
    }

    protected void onGetSstDone(Message message) {
    }

    @Override // com.android.internal.telephony.uicc.IccRecords, android.os.Handler
    public String toString() {
        return "RuimRecords: " + super.toString() + " m_ota_commited" + this.mOtaCommited + " mMyMobileNumber=xxxx mMin2Min1=" + this.mMin2Min1 + " mPrlVersion=" + this.mPrlVersion + " mEFpl=" + IccUtils.bytesToHexString(this.mEFpl) + " mEFli=" + IccUtils.bytesToHexString(this.mEFli) + " mCsimSpnDisplayCondition=" + this.mCsimSpnDisplayCondition + " mMdn=" + this.mMdn + " mMin=" + this.mMin + " mHomeSystemId=" + this.mHomeSystemId + " mHomeNetworkId=" + this.mHomeNetworkId;
    }

    public RuimRecords(UiccCardApplication uiccCardApplication, Context context, CommandsInterface commandsInterface) {
        super(uiccCardApplication, context, commandsInterface);
        this.mOtaCommited = false;
        this.mEFpl = null;
        this.mEFli = null;
        this.mCsimSpnDisplayCondition = false;
        this.mAdnCache = TelephonyComponentFactory.getInstance().inject(AdnRecordCache.class.getName()).makeAdnRecordCache(this.mFh);
        MiuiUiccControllerStub.get(this.mParentApp.getPhoneId()).initIccRecord(this.mParentApp.getPhoneId(), uiccCardApplication, this.mFh, new Object[0]);
        this.mRecordsRequested = false;
        this.mLockedRecordsReqReason = 0;
        this.mRecordsToLoad = 0;
        resetRecords();
        log("RuimRecords X ctor this=" + this);
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public void dispose() {
        log("Disposing RuimRecords " + this);
        MiuiUiccControllerStub.get(this.mParentApp.getPhoneId()).disposeIccRecord(this.mParentApp.getPhoneId(), this.mParentApp);
        resetRecords();
        super.dispose();
    }

    protected void finalize() {
        log("RuimRecords finalized");
    }

    protected void resetRecords() {
        this.mMncLength = -1;
        log("setting0 mMncLength" + this.mMncLength);
        this.mIccId = null;
        this.mFullIccId = null;
        this.mAdnCache.reset();
        MiuiUiccControllerStub.get(this.mParentApp.getPhoneId()).resetIccRecord(this.mParentApp.getPhoneId(), this.mParentApp);
        this.mRecordsRequested = false;
        this.mLockedRecordsReqReason = 0;
        this.mLoaded.set(false);
    }

    public String getMdnNumber() {
        return this.mMyMobileNumber;
    }

    public String getCdmaMin() {
        return this.mMin2Min1;
    }

    public String getPrlVersion() {
        return this.mPrlVersion;
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public String getNAI() {
        return this.mNai;
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public void setVoiceMailNumber(String str, String str2, Message message) {
        AsyncResult.forMessage(message).exception = new IccException("setVoiceMailNumber not implemented");
        message.sendToTarget();
        loge("method setVoiceMailNumber is not implemented");
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public void onRefresh(boolean z, int[] iArr) {
        if (z) {
            fetchRuimRecords();
        }
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public String getOperatorNumeric() {
        return getRUIMOperatorNumeric();
    }

    public String getRUIMOperatorNumeric() {
        String imsi = getIMSI();
        if (imsi == null) {
            return null;
        }
        int i = this.mMncLength;
        if (i != -1 && i != 0) {
            return imsi.substring(0, i + 3);
        }
        return imsi.substring(0, MccTable.smallestDigitsMccForMnc(Integer.parseInt(imsi.substring(0, 3))) + 3);
    }

    private class EfPlLoaded implements IccRecords.IccRecordLoaded {
        private EfPlLoaded() {
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public String getEfName() {
            return "EF_PL";
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public void onRecordLoaded(AsyncResult asyncResult) {
            Throwable th = asyncResult.exception;
            RuimRecords ruimRecords = RuimRecords.this;
            if (th != null) {
                ruimRecords.loge("Record Load Exception: " + asyncResult.exception);
            } else {
                ruimRecords.mEFpl = (byte[]) asyncResult.result;
                RuimRecords.this.log("EF_PL=" + IccUtils.bytesToHexString(RuimRecords.this.mEFpl));
            }
        }
    }

    private class EfCsimLiLoaded implements IccRecords.IccRecordLoaded {
        private EfCsimLiLoaded() {
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public String getEfName() {
            return "EF_CSIM_LI";
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public void onRecordLoaded(AsyncResult asyncResult) {
            Throwable th = asyncResult.exception;
            RuimRecords ruimRecords = RuimRecords.this;
            if (th != null) {
                ruimRecords.loge("Record Load Exception: " + asyncResult.exception);
                return;
            }
            ruimRecords.mEFli = (byte[]) asyncResult.result;
            int i = 0;
            while (true) {
                int length = RuimRecords.this.mEFli.length;
                RuimRecords ruimRecords2 = RuimRecords.this;
                if (i < length) {
                    byte[] bArr = ruimRecords2.mEFli;
                    int i2 = i + 1;
                    switch (bArr[i2]) {
                        case 1:
                            RuimRecords.this.mEFli[i] = 101;
                            RuimRecords.this.mEFli[i2] = 110;
                            break;
                        case 2:
                            RuimRecords.this.mEFli[i] = 102;
                            RuimRecords.this.mEFli[i2] = 114;
                            break;
                        case 3:
                            RuimRecords.this.mEFli[i] = 101;
                            RuimRecords.this.mEFli[i2] = 115;
                            break;
                        case 4:
                            RuimRecords.this.mEFli[i] = 106;
                            RuimRecords.this.mEFli[i2] = 97;
                            break;
                        case 5:
                            RuimRecords.this.mEFli[i] = 107;
                            RuimRecords.this.mEFli[i2] = 111;
                            break;
                        case 6:
                            RuimRecords.this.mEFli[i] = 122;
                            RuimRecords.this.mEFli[i2] = 104;
                            break;
                        case 7:
                            RuimRecords.this.mEFli[i] = 104;
                            RuimRecords.this.mEFli[i2] = 101;
                            break;
                        default:
                            RuimRecords.this.mEFli[i] = 32;
                            RuimRecords.this.mEFli[i2] = 32;
                            break;
                    }
                    i += 2;
                } else {
                    ruimRecords2.log("EF_LI=" + IccUtils.bytesToHexString(RuimRecords.this.mEFli));
                    return;
                }
            }
        }
    }

    private class EfCsimSpnLoaded implements IccRecords.IccRecordLoaded {
        private EfCsimSpnLoaded() {
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public String getEfName() {
            return "EF_CSIM_SPN";
        }

        /* JADX WARN: Code duplicated, block: B:32:0x007f A[Catch: Exception -> 0x0070, TryCatch #0 {Exception -> 0x0070, blocks: (B:28:0x0067, B:31:0x0072, B:32:0x007f, B:33:0x008c, B:36:0x009b, B:37:0x009f, B:38:0x00c7), top: B:44:0x0056 }] */
        /* JADX WARN: Code duplicated, block: B:38:0x00c7 A[Catch: Exception -> 0x0070, TRY_LEAVE, TryCatch #0 {Exception -> 0x0070, blocks: (B:28:0x0067, B:31:0x0072, B:32:0x007f, B:33:0x008c, B:36:0x009b, B:37:0x009f, B:38:0x00c7), top: B:44:0x0056 }] */
        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public void onRecordLoaded(AsyncResult asyncResult) {
            byte[] bArr = (byte[]) asyncResult.result;
            RuimRecords.this.log("CSIM_SPN=" + IccUtils.bytesToHexString(bArr));
            RuimRecords.this.mCsimSpnDisplayCondition = (bArr[0] & 1) != 0;
            byte b = bArr[1];
            byte b2 = bArr[2];
            byte[] bArr2 = new byte[32];
            System.arraycopy(bArr, 3, bArr2, 0, bArr.length - 3 < 32 ? bArr.length - 3 : 32);
            int i = 0;
            while (i < 32 && (bArr2[i] & 255) != 255) {
                i++;
            }
            if (i == 0) {
                RuimRecords.this.setServiceProviderName("");
                return;
            }
            try {
                if (b == 0) {
                    RuimRecords.this.setServiceProviderName(new String(bArr2, 0, i, "ISO-8859-1"));
                } else if (b == 2) {
                    String str = new String(bArr2, 0, i, "US-ASCII");
                    boolean zIsPrintableAsciiOnly = RuimRecords.isPrintableAsciiOnly(str);
                    RuimRecords ruimRecords = RuimRecords.this;
                    if (zIsPrintableAsciiOnly) {
                        ruimRecords.setServiceProviderName(str);
                    } else {
                        ruimRecords.log("Some corruption in SPN decoding = " + str);
                        RuimRecords.this.log("Using ENCODING_GSM_7BIT_ALPHABET scheme...");
                        RuimRecords.this.setServiceProviderName(GsmAlphabet.gsm7BitPackedToString(bArr2, 0, (i * 8) / 7));
                    }
                } else if (b == 3) {
                    RuimRecords.this.setServiceProviderName(GsmAlphabet.gsm7BitPackedToString(bArr2, 0, (i * 8) / 7));
                } else if (b == 4) {
                    RuimRecords.this.setServiceProviderName(new String(bArr2, 0, i, "utf-16"));
                } else if (b == 8) {
                    RuimRecords.this.setServiceProviderName(new String(bArr2, 0, i, "ISO-8859-1"));
                } else if (b == 9) {
                    RuimRecords.this.setServiceProviderName(GsmAlphabet.gsm7BitPackedToString(bArr2, 0, (i * 8) / 7));
                } else {
                    RuimRecords.this.log("SPN encoding not supported");
                }
            } catch (Exception e) {
                RuimRecords.this.log("spn decode error: " + e);
            }
            RuimRecords.this.log("spn=" + RuimRecords.this.getServiceProviderName());
            RuimRecords.this.log("spnCondition=" + RuimRecords.this.mCsimSpnDisplayCondition);
            String strOnOperatorNumericOrNameSet = MiuiUiccControllerStub.get(RuimRecords.this.mParentApp.getPhoneId()).onOperatorNumericOrNameSet(RuimRecords.this.mParentApp.getPhoneId(), "gsm.sim.operator.alpha", RuimRecords.this.getServiceProviderName());
            RuimRecords ruimRecords2 = RuimRecords.this;
            ruimRecords2.mTelephonyManager.setSimOperatorNameForPhone(ruimRecords2.mParentApp.getPhoneId(), strOnOperatorNumericOrNameSet);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isPrintableAsciiOnly(CharSequence charSequence) {
        int length = charSequence.length();
        for (int i = 0; i < length; i++) {
            if (!isPrintableAscii(charSequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private class EfCsimMdnLoaded implements IccRecords.IccRecordLoaded {
        private EfCsimMdnLoaded() {
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public String getEfName() {
            return "EF_CSIM_MDN";
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public void onRecordLoaded(AsyncResult asyncResult) {
            if (asyncResult.exception != null) {
                RuimRecords.this.loge("Record Load Exception: " + asyncResult.exception);
                return;
            }
            byte[] bArr = (byte[]) asyncResult.result;
            RuimRecords.this.log("CSIM_MDN=" + IccUtils.bytesToHexString(bArr));
            RuimRecords.this.mMdn = IccUtils.cdmaBcdToString(bArr, 1, bArr[0] & 15);
            RuimRecords.this.log("CSIM MDN=" + RuimRecords.this.mMdn);
        }
    }

    public class EfCsimImsimLoaded implements IccRecords.IccRecordLoaded {
        public EfCsimImsimLoaded() {
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public String getEfName() {
            return "EF_CSIM_IMSIM";
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public void onRecordLoaded(AsyncResult asyncResult) {
            RuimRecords ruimRecords = RuimRecords.this;
            ruimRecords.mEssentialRecordsToLoad--;
            if (asyncResult.exception != null) {
                ruimRecords.loge("Record Load Exception: " + asyncResult.exception);
                return;
            }
            byte[] bArr = (byte[]) asyncResult.result;
            if (bArr == null || bArr.length < 10) {
                ruimRecords.loge("Invalid IMSI from EF_CSIM_IMSIM");
                return;
            }
            ruimRecords.log("data=" + Rlog.pii("RuimRecords", IccUtils.bytesToHexString(bArr)));
            if ((bArr[7] & 128) == 128) {
                String strDecodeImsi = decodeImsi(bArr);
                if (TextUtils.isEmpty(RuimRecords.this.mImsi)) {
                    RuimRecords ruimRecords2 = RuimRecords.this;
                    ruimRecords2.mImsi = strDecodeImsi;
                    ruimRecords2.log("IMSI=" + Rlog.pii("RuimRecords", RuimRecords.this.mImsi));
                }
                RuimRecords.this.mMin = strDecodeImsi.substring(5, 15);
                RuimRecords.this.log("min present=" + Rlog.pii("RuimRecords", RuimRecords.this.mMin));
                RuimRecords ruimRecords3 = RuimRecords.this;
                ruimRecords3.onGetImsiDone(ruimRecords3.mImsi);
                return;
            }
            RuimRecords.this.log("min not present");
        }

        private int decodeImsiDigits(int i, int i2) {
            int i3 = 1;
            for (int i4 = 0; i4 < i2; i4++) {
                i += i3;
                if ((i / i3) % 10 == 0) {
                    i -= i3 * 10;
                }
                i3 *= 10;
            }
            return i;
        }

        public String decodeImsi(byte[] bArr) {
            int iDecodeImsiDigits = decodeImsiDigits(((bArr[9] & 3) << 8) | (bArr[8] & 255), 3);
            int iDecodeImsiDigits2 = decodeImsiDigits(bArr[6] & 127, 2);
            int i = ((bArr[2] & 3) << 8) + (bArr[1] & 255);
            int i2 = (bArr[5] & 255) << 8;
            byte b = bArr[4];
            int i3 = (i2 | (b & 255)) >> 6;
            int i4 = (b >> 2) & 15;
            if (i4 > 9) {
                i4 = 0;
            }
            int i5 = (bArr[3] & 255) | ((b & 3) << 8);
            int iDecodeImsiDigits3 = decodeImsiDigits(i, 3);
            int iDecodeImsiDigits4 = decodeImsiDigits(i3, 3);
            int iDecodeImsiDigits5 = decodeImsiDigits(i5, 3);
            StringBuilder sb = new StringBuilder();
            Locale locale = Locale.US;
            sb.append(String.format(locale, "%03d", Integer.valueOf(iDecodeImsiDigits)));
            sb.append(String.format(locale, "%02d", Integer.valueOf(iDecodeImsiDigits2)));
            sb.append(String.format(locale, "%03d", Integer.valueOf(iDecodeImsiDigits3)));
            sb.append(String.format(locale, "%03d", Integer.valueOf(iDecodeImsiDigits4)));
            sb.append(String.format(locale, "%d", Integer.valueOf(i4)));
            sb.append(String.format(locale, "%03d", Integer.valueOf(iDecodeImsiDigits5)));
            return sb.toString();
        }
    }

    private class EfCsimCdmaHomeLoaded implements IccRecords.IccRecordLoaded {
        private EfCsimCdmaHomeLoaded() {
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public String getEfName() {
            return "EF_CSIM_CDMAHOME";
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public void onRecordLoaded(AsyncResult asyncResult) {
            if (asyncResult.exception != null) {
                RuimRecords.this.loge("Record Load Exception: " + asyncResult.exception);
                return;
            }
            ArrayList arrayList = (ArrayList) asyncResult.result;
            RuimRecords.this.log("CSIM_CDMAHOME data size=" + arrayList.size());
            if (arrayList.isEmpty()) {
                return;
            }
            StringBuilder sb = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                byte[] bArr = (byte[]) obj;
                if (bArr.length == 5) {
                    int i2 = ((bArr[1] & 255) << 8) | (bArr[0] & 255);
                    int i3 = (bArr[2] & 255) | ((bArr[3] & 255) << 8);
                    sb.append(i2);
                    sb.append(',');
                    sb2.append(i3);
                    sb2.append(',');
                }
            }
            sb.setLength(sb.length() - 1);
            sb2.setLength(sb2.length() - 1);
            RuimRecords.this.mHomeSystemId = sb.toString();
            RuimRecords.this.mHomeNetworkId = sb2.toString();
        }
    }

    private class EfCsimEprlLoaded implements IccRecords.IccRecordLoaded {
        private EfCsimEprlLoaded() {
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public String getEfName() {
            return "EF_CSIM_EPRL";
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public void onRecordLoaded(AsyncResult asyncResult) {
            Throwable th = asyncResult.exception;
            RuimRecords ruimRecords = RuimRecords.this;
            if (th != null) {
                ruimRecords.loge("Record Load Exception: " + asyncResult.exception);
                return;
            }
            ruimRecords.onGetCSimEprlDone(asyncResult);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onGetCSimEprlDone(AsyncResult asyncResult) {
        byte[] bArr = (byte[]) asyncResult.result;
        log("CSIM_EPRL=" + IccUtils.bytesToHexString(bArr));
        if (bArr.length > 3) {
            this.mPrlVersion = Integer.toString((bArr[3] & 255) | ((bArr[2] & 255) << 8));
        }
        log("CSIM PRL version=" + this.mPrlVersion);
    }

    private class EfCsimMipUppLoaded implements IccRecords.IccRecordLoaded {
        private EfCsimMipUppLoaded() {
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public String getEfName() {
            return "EF_CSIM_MIPUPP";
        }

        boolean checkLengthLegal(int i, int i2) {
            if (i >= i2) {
                return true;
            }
            Log.e("RuimRecords", "CSIM MIPUPP format error, length = " + i + "expected length at least =" + i2);
            return false;
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public void onRecordLoaded(AsyncResult asyncResult) {
            if (asyncResult.exception != null) {
                RuimRecords.this.loge("Record Load Exception: " + asyncResult.exception);
                return;
            }
            byte[] bArr = (byte[]) asyncResult.result;
            if (bArr.length < 1) {
                Log.e("RuimRecords", "MIPUPP read error");
                return;
            }
            BitwiseInputStream bitwiseInputStream = new BitwiseInputStream(bArr);
            try {
                int i = bitwiseInputStream.read(8) << 3;
                if (checkLengthLegal(i, 1)) {
                    int i2 = i - 1;
                    if (bitwiseInputStream.read(1) == 1) {
                        if (!checkLengthLegal(i2, 11)) {
                            return;
                        }
                        bitwiseInputStream.skip(11);
                        i2 = i - 12;
                    }
                    if (checkLengthLegal(i2, 4)) {
                        int i3 = bitwiseInputStream.read(4);
                        int i4 = i2 - 4;
                        for (int i5 = 0; i5 < i3 && checkLengthLegal(i4, 4); i5++) {
                            int i6 = bitwiseInputStream.read(4);
                            if (!checkLengthLegal(i4 - 4, 8)) {
                                return;
                            }
                            int i7 = bitwiseInputStream.read(8);
                            int i8 = i4 - 12;
                            if (i6 == 0) {
                                if (checkLengthLegal(i8, i7 << 3)) {
                                    char[] cArr = new char[i7];
                                    for (int i9 = 0; i9 < i7; i9++) {
                                        cArr[i9] = (char) (bitwiseInputStream.read(8) & 255);
                                    }
                                    RuimRecords.this.mNai = new String(cArr);
                                    if (Log.isLoggable("RuimRecords", 2)) {
                                        Log.v("RuimRecords", "MIPUPP Nai = " + RuimRecords.this.mNai);
                                        return;
                                    }
                                    return;
                                }
                                return;
                            }
                            int i10 = i7 << 3;
                            int i11 = i10 + 102;
                            if (!checkLengthLegal(i8, i11)) {
                                return;
                            }
                            bitwiseInputStream.skip(i10 + 101);
                            int i12 = i8 - i11;
                            if (bitwiseInputStream.read(1) == 1) {
                                if (!checkLengthLegal(i12, 32)) {
                                    return;
                                }
                                bitwiseInputStream.skip(32);
                                i12 -= 32;
                            }
                            if (!checkLengthLegal(i12, 5)) {
                                return;
                            }
                            bitwiseInputStream.skip(4);
                            int i13 = i12 - 5;
                            if (bitwiseInputStream.read(1) != 1) {
                                i4 = i13;
                            } else {
                                if (!checkLengthLegal(i13, 32)) {
                                    return;
                                }
                                bitwiseInputStream.skip(32);
                                i4 = i12 - 37;
                            }
                        }
                    }
                }
            } catch (Exception unused) {
                Log.e("RuimRecords", "MIPUPP read Exception error!");
            }
        }
    }

    @Override // com.android.internal.telephony.uicc.IccRecords, android.os.Handler
    public void handleMessage(Message message) throws Throwable {
        if (this.mDestroyed.get()) {
            loge("Received message " + message + "[" + message.what + "] while being destroyed. Ignoring.");
            return;
        }
        boolean z = false;
        try {
            try {
                int i = message.what;
                if (i == 4) {
                    log("Event EVENT_GET_DEVICE_IDENTITY_DONE Received");
                    return;
                }
                if (i != 5) {
                    if (i == 14) {
                        Throwable th = ((AsyncResult) message.obj).exception;
                        if (th != null) {
                            Rlog.i("RuimRecords", "RuimRecords update failed", th);
                            return;
                        }
                        return;
                    }
                    if (i == 17) {
                        log("Event EVENT_GET_SST_DONE Received");
                        onGetSstDone(message);
                        return;
                    } else {
                        super.handleMessage(message);
                        return;
                    }
                }
                try {
                    this.mEssentialRecordsToLoad--;
                    AsyncResult asyncResult = (AsyncResult) message.obj;
                    byte[] bArr = (byte[]) asyncResult.result;
                    if (asyncResult.exception == null) {
                        this.mIccId = IccUtils.bcdToString(bArr, 0, bArr.length);
                        if (TelephonyCommonUtilsStub.get().isMiuiRom()) {
                            this.mIccId = IccUtils.bchToString(bArr, 0, bArr.length);
                        }
                        this.mFullIccId = IccUtils.bchToString(bArr, 0, bArr.length);
                        log("iccid: " + SubscriptionInfo.getPrintableId(this.mFullIccId));
                    }
                    onRecordLoaded();
                } catch (RuntimeException e) {
                    e = e;
                    z = true;
                    Rlog.w("RuimRecords", "Exception parsing RUIM record", e);
                    if (z) {
                        onRecordLoaded();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    z = true;
                    if (z) {
                        onRecordLoaded();
                    }
                    throw th;
                }
            } catch (RuntimeException e2) {
                e = e2;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    private static String[] getAssetLanguages(Context context) {
        String[] locales = context.getAssets().getLocales();
        String[] strArr = new String[locales.length];
        for (int i = 0; i < locales.length; i++) {
            String str = locales[i];
            int iIndexOf = str.indexOf(45);
            if (iIndexOf < 0) {
                strArr[i] = str;
            } else {
                strArr[i] = str.substring(0, iIndexOf);
            }
        }
        return strArr;
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    protected void onRecordLoaded() {
        this.mRecordsToLoad--;
        log("onRecordLoaded " + this.mRecordsToLoad + " mEssentialRecordsToLoad: " + this.mEssentialRecordsToLoad + " requested: " + this.mRecordsRequested);
        if (getEssentialRecordsLoaded() && !this.mEssentialRecordsListenerNotified) {
            onAllEssentialRecordsLoaded();
        }
        if (getRecordsLoaded()) {
            onAllRecordsLoaded();
            return;
        }
        if (getLockedRecordsLoaded() || getNetworkLockedRecordsLoaded()) {
            onLockedAllRecordsLoaded();
        } else if (this.mRecordsToLoad < 0 || this.mEssentialRecordsToLoad < 0) {
            loge("recordsToLoad <0, programmer error suspected");
            this.mRecordsToLoad = 0;
            this.mEssentialRecordsToLoad = 0;
        }
    }

    private void onLockedAllRecordsLoaded() {
        int i = this.mLockedRecordsReqReason;
        if (i == 1) {
            this.mLockedRecordsLoadedRegistrants.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
        } else {
            if (i == 2) {
                this.mNetworkLockedRecordsLoadedRegistrants.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
                return;
            }
            loge("onLockedAllRecordsLoaded: unexpected mLockedRecordsReqReason " + this.mLockedRecordsReqReason);
        }
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    protected void onAllEssentialRecordsLoaded() {
        log("Essential record load complete");
        this.mEssentialRecordsListenerNotified = true;
        this.mEssentialRecordsLoadedRegistrants.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
    }

    private void fetchEssentialRuimRecords() {
        log("fetchEssentialRuimRecords: mRecordsToLoad = " + this.mRecordsToLoad + " mEssentialRecordsToLoad = " + this.mEssentialRecordsToLoad);
        this.mEssentialRecordsListenerNotified = false;
        this.mFh.loadEFTransparent(IccConstants.EF_ICCID, obtainMessage(5));
        this.mRecordsToLoad = this.mRecordsToLoad + 1;
        this.mEssentialRecordsToLoad = this.mEssentialRecordsToLoad + 1;
        this.mFh.loadEFTransparent(IccConstants.EF_CSIM_IMSIM, obtainMessage(100, new EfCsimImsimLoaded()));
        this.mRecordsToLoad++;
        this.mEssentialRecordsToLoad++;
        this.mFh.loadEFTransparent(28481, obtainMessage(100, new EfCsimSpnLoaded()));
        this.mRecordsToLoad++;
        this.mEssentialRecordsToLoad++;
        log("fetchEssentialRuimRecords " + this.mRecordsToLoad + " requested: " + this.mRecordsRequested);
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    protected void onAllRecordsLoaded() {
        log("record load complete");
        if (Resources.getSystem().getBoolean(17892070)) {
            setSimLanguage(this.mEFli, this.mEFpl);
        }
        this.mLoaded.set(true);
        this.mRecordsLoadedRegistrants.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
        if (TextUtils.isEmpty(this.mMdn)) {
            return;
        }
        int subscriptionId = SubscriptionManager.getSubscriptionId(this.mParentApp.getUiccProfile().getPhoneId());
        if (SubscriptionManager.isValidSubscriptionId(subscriptionId)) {
            if (TelephonyBaseUtilsStub.isMiuiRom()) {
                return;
            }
            SubscriptionManager.from(this.mContext).setDisplayNumber(this.mMdn, subscriptionId);
            return;
        }
        log("Cannot call setDisplayNumber: invalid subId");
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public void onReady() {
        fetchRuimRecords();
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    protected void onLocked() {
        log("only fetch EF_ICCID in locked state");
        super.onLocked();
        this.mFh.loadEFTransparent(IccConstants.EF_ICCID, obtainMessage(5));
        this.mRecordsToLoad++;
    }

    protected void fetchRuimRecords() {
        this.mRecordsRequested = true;
        fetchEssentialRuimRecords();
        log("fetchRuimRecords " + this.mRecordsToLoad);
        this.mFh.loadEFTransparent(IccConstants.EF_PL, obtainMessage(100, new EfPlLoaded()));
        this.mRecordsToLoad++;
        this.mFh.loadEFTransparent(28474, obtainMessage(100, new EfCsimLiLoaded()));
        this.mRecordsToLoad++;
        this.mFh.loadEFLinearFixed(IccConstants.EF_CSIM_MDN, 1, obtainMessage(100, new EfCsimMdnLoaded()));
        this.mRecordsToLoad++;
        this.mFh.loadEFLinearFixedAll(IccConstants.EF_CSIM_CDMAHOME, obtainMessage(100, new EfCsimCdmaHomeLoaded()));
        this.mRecordsToLoad++;
        this.mFh.loadEFTransparent(IccConstants.EF_CSIM_EPRL, 4, obtainMessage(100, new EfCsimEprlLoaded()));
        this.mRecordsToLoad++;
        this.mFh.loadEFTransparent(IccConstants.EF_CSIM_MIPUPP, obtainMessage(100, new EfCsimMipUppLoaded()));
        this.mRecordsToLoad++;
        this.mFh.getEFLinearRecordSize(IccConstants.EF_SMS, obtainMessage(28));
        this.mRecordsToLoad++;
        log("fetchRuimRecords " + this.mRecordsToLoad + " requested: " + this.mRecordsRequested);
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public boolean isProvisioned() {
        if (TelephonyProperties.test_csim().orElse(Boolean.FALSE).booleanValue()) {
            return true;
        }
        UiccCardApplication uiccCardApplication = this.mParentApp;
        if (uiccCardApplication == null) {
            return false;
        }
        return (uiccCardApplication.getType() == IccCardApplicationStatus.AppType.APPTYPE_CSIM && (this.mMdn == null || this.mMin == null)) ? false : true;
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public void setVoiceMessageWaiting(int i, int i2) {
        log("RuimRecords:setVoiceMessageWaiting - NOP for CDMA");
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public int getVoiceMessageCount() {
        log("RuimRecords:getVoiceMessageCount - NOP for CDMA");
        return 0;
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    protected void handleFileUpdate(int i) {
        this.mLoaded.set(false);
        this.mAdnCache.reset();
        MiuiUiccControllerStub.get(this.mParentApp.getPhoneId()).resetIccRecord(this.mParentApp.getPhoneId(), this.mParentApp);
        fetchRuimRecords();
    }

    public String getMdn() {
        return this.mMdn;
    }

    public String getMin() {
        return this.mMin;
    }

    public String getSid() {
        return this.mHomeSystemId;
    }

    public String getNid() {
        return this.mHomeNetworkId;
    }

    public boolean getCsimSpnDisplayCondition() {
        return this.mCsimSpnDisplayCondition;
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    protected void log(String str) {
        if (this.mParentApp != null) {
            Rlog.d("RuimRecords", "[RuimRecords-" + this.mParentApp.getPhoneId() + "] " + str);
            return;
        }
        Rlog.d("RuimRecords", "[RuimRecords] " + str);
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    protected void loge(String str) {
        if (this.mParentApp != null) {
            Rlog.e("RuimRecords", "[RuimRecords-" + this.mParentApp.getPhoneId() + "] " + str);
            return;
        }
        Rlog.e("RuimRecords", "[RuimRecords] " + str);
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("RuimRecords: " + this);
        printWriter.println(" extends:");
        super.dump(fileDescriptor, printWriter, strArr);
        printWriter.println(" mOtaCommited=" + this.mOtaCommited);
        printWriter.println(" mMyMobileNumber=" + this.mMyMobileNumber);
        printWriter.println(" mMin2Min1=" + this.mMin2Min1);
        printWriter.println(" mPrlVersion=" + this.mPrlVersion);
        printWriter.println(" mEFpl[]=" + Arrays.toString(this.mEFpl));
        printWriter.println(" mEFli[]=" + Arrays.toString(this.mEFli));
        printWriter.println(" mCsimSpnDisplayCondition=" + this.mCsimSpnDisplayCondition);
        printWriter.println(" mMdn=" + this.mMdn);
        printWriter.println(" mMin=" + this.mMin);
        printWriter.println(" mHomeSystemId=" + this.mHomeSystemId);
        printWriter.println(" mHomeNetworkId=" + this.mHomeNetworkId);
        printWriter.flush();
    }
}
