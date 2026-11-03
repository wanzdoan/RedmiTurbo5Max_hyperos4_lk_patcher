package com.android.internal.telephony.uicc;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncResult;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.telephony.CarrierConfigManager;
import android.telephony.MiuiTelephonyManagerStub;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyBaseUtilsStub;
import android.text.TextUtils;
import android.util.Pair;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.internal.telephony.gsm.SimTlv;
import com.android.internal.telephony.satellite.SatelliteSessionController;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class SIMRecords extends IccRecords {
    protected static final int CFF_LINE1_MASK = 15;
    protected static final int CFF_LINE1_RESET = 240;
    protected static final int CFF_UNCONDITIONAL_ACTIVE = 10;
    protected static final int CFF_UNCONDITIONAL_DEACTIVE = 5;
    protected static final int CFIS_ADN_CAPABILITY_ID_OFFSET = 14;
    protected static final int CFIS_ADN_EXTENSION_ID_OFFSET = 15;
    protected static final int CFIS_BCD_NUMBER_LENGTH_OFFSET = 2;
    protected static final int CFIS_TON_NPI_OFFSET = 3;
    protected static final int EVENT_GET_AD_DONE = 9;
    protected static final int EVENT_GET_CFF_DONE = 24;
    protected static final int EVENT_GET_CFIS_DONE = 32;
    protected static final int EVENT_GET_CPHS_MAILBOX_DONE = 11;
    protected static final int EVENT_GET_CSP_CPHS_DONE = 33;
    protected static final int EVENT_GET_EHPLMN_DONE = 40;
    protected static final int EVENT_GET_FPLMN_DONE = 41;
    protected static final int EVENT_GET_GID1_DONE = 34;
    protected static final int EVENT_GET_GID2_DONE = 36;
    protected static final int EVENT_GET_HPLMN_W_ACT_DONE = 39;
    protected static final int EVENT_GET_IARI_DONE = 48;
    protected static final int EVENT_GET_ICCID_DONE = 4;
    protected static final int EVENT_GET_IMSI_DONE = 3;
    protected static final int EVENT_GET_INFO_CPHS_DONE = 26;
    protected static final int EVENT_GET_MBDN_DONE = 6;
    protected static final int EVENT_GET_MBI_DONE = 5;
    protected static final int EVENT_GET_MSISDN_DONE = 10;
    protected static final int EVENT_GET_MWIS_DONE = 7;
    protected static final int EVENT_GET_OPLMN_W_ACT_DONE = 38;
    protected static final int EVENT_GET_OPL_DONE = 16;
    protected static final int EVENT_GET_PLMN_W_ACT_DONE = 37;
    protected static final int EVENT_GET_PNN_DONE = 15;
    protected static final int EVENT_GET_PSISMSC_DONE = 47;
    protected static final int EVENT_GET_SMSS_RECORD_DONE = 46;
    protected static final int EVENT_GET_SPDI_DONE = 13;
    protected static final int EVENT_GET_SPN_DONE = 12;
    protected static final int EVENT_GET_SST_DONE = 17;
    protected static final int EVENT_GET_VOICE_MAIL_INDICATOR_CPHS_DONE = 8;
    protected static final int EVENT_NTN_GET_ICCID_DONE = 1000;
    protected static final int EVENT_RETRY_GET_IMSI = 50;
    protected static final int EVENT_SET_MSISDN_DONE = 30;
    protected static final int EVENT_UPDATE_DONE = 14;
    protected static final int MAX_GET_IMSI_RETRY_COUNT = 5;
    protected static final int RETRY_GET_IMSI_DELAY_TIME = 3000;
    protected static final int TAG_FULL_NETWORK_NAME = 67;
    protected static final int TAG_SHORT_NETWORK_NAME = 69;
    protected int mCallForwardingStatus;
    protected byte[] mCphsInfo;
    protected boolean mCspPlmnEnabled;
    protected byte[] mEfCPHS_MWI;
    protected byte[] mEfCff;
    protected byte[] mEfCfis;
    protected byte[] mEfLi;
    protected byte[] mEfMWIS;
    protected byte[] mEfPl;
    protected int mGetImsiRetryCount;
    protected GetSpnFsmState mSpnState;
    private String[] mUsimIari;
    protected UsimServiceTable mUsimServiceTable;
    protected VoiceMailConstants mVmConfig;
    protected static final String LOG_TAG = "SIMRecords";
    private static final boolean VDBG = Rlog.isLoggable(LOG_TAG, 2);

    protected enum GetSpnFsmState {
        IDLE,
        INIT,
        READ_SPN_3GPP,
        READ_SPN_CPHS,
        READ_SPN_SHORT_CPHS
    }

    protected boolean checkCdma3gCard() {
        return true;
    }

    protected void loadMailboxCphs() {
    }

    protected void notifyMsisdnRecordChanged() {
    }

    protected void setSSTdata(byte[] bArr) {
    }

    protected void setValidMBIStatus(boolean z) {
    }

    protected boolean shallIgnoreMessage(Message message) {
        return true;
    }

    @Override // com.android.internal.telephony.uicc.IccRecords, android.os.Handler
    public String toString() {
        return "SimRecords: " + super.toString() + " mVmConfig" + this.mVmConfig + " callForwardingEnabled=" + this.mCallForwardingStatus + " spnState=" + this.mSpnState + " mCphsInfo=" + IccUtils.bytesToHexString(this.mCphsInfo) + " mCspPlmnEnabled=" + this.mCspPlmnEnabled + " efMWIS=" + IccUtils.bytesToHexString(this.mEfMWIS) + " efCPHS_MWI=" + IccUtils.bytesToHexString(this.mEfCPHS_MWI) + " mEfCff=" + IccUtils.bytesToHexString(this.mEfCff) + " mEfCfis=" + IccUtils.bytesToHexString(this.mEfCfis) + " getOperatorNumeric=" + getOperatorNumeric() + " mPsiSmsc=" + this.mPsiSmsc + " mUsimIari=" + Arrays.toString(this.mUsimIari) + " TPMR=" + getSmssTpmrValue();
    }

    public SIMRecords(UiccCardApplication uiccCardApplication, Context context, CommandsInterface commandsInterface) {
        super(uiccCardApplication, context, commandsInterface);
        this.mCphsInfo = null;
        this.mCspPlmnEnabled = true;
        this.mEfMWIS = null;
        this.mEfCPHS_MWI = null;
        this.mEfCff = null;
        this.mEfCfis = null;
        this.mEfLi = null;
        this.mEfPl = null;
        this.mGetImsiRetryCount = 0;
        this.mAdnCache = TelephonyComponentFactory.getInstance().inject(AdnRecordCache.class.getName()).makeAdnRecordCache(this.mFh);
        MiuiUiccControllerStub.get(uiccCardApplication.getPhoneId()).initIccRecord(uiccCardApplication.getPhoneId(), uiccCardApplication, this.mFh, new Object[0]);
        this.mVmConfig = new VoiceMailConstants();
        this.mRecordsRequested = false;
        this.mLockedRecordsReqReason = 0;
        this.mRecordsToLoad = 0;
        this.mCi.setOnSmsOnSim(this, 21, null);
        resetRecords();
        log("SIMRecords X ctor this=" + this);
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public void dispose() {
        log("Disposing SIMRecords this=" + this);
        this.mCi.unSetOnSmsOnSim(this);
        MiuiUiccControllerStub.get(this.mParentApp.getPhoneId()).disposeIccRecord(this.mParentApp.getPhoneId(), this.mParentApp);
        resetRecords();
        super.dispose();
    }

    protected void finalize() {
        log("finalized");
    }

    protected void resetRecords() {
        this.mImsi = null;
        this.mMsisdn = null;
        this.mVoiceMailNum = null;
        this.mMncLength = -1;
        log("setting0 mMncLength" + this.mMncLength);
        this.mIccId = null;
        this.mFullIccId = null;
        this.mCarrierNameDisplayCondition = 0;
        this.mEfMWIS = null;
        this.mEfCPHS_MWI = null;
        this.mSpdi = null;
        this.mPnnHomeName = null;
        this.mPnns = null;
        this.mOpl = null;
        this.mGid1 = null;
        this.mGid2 = null;
        this.mPlmnActRecords = null;
        this.mOplmnActRecords = null;
        this.mHplmnActRecords = null;
        this.mFplmns = null;
        this.mEhplmns = null;
        this.mUsimIari = null;
        this.mAdnCache.reset();
        MiuiUiccControllerStub.get(this.mParentApp.getPhoneId()).resetIccRecord(this.mParentApp.getPhoneId(), this.mParentApp);
        log("SIMRecords: onRadioOffOrNotAvailable set 'gsm.sim.operator.numeric' to operator=null");
        log("update icc_operator_numeric=null");
        this.mTelephonyManager.setSimOperatorNumericForPhone(this.mParentApp.getPhoneId(), "");
        this.mTelephonyManager.setSimOperatorNameForPhone(this.mParentApp.getPhoneId(), "");
        this.mTelephonyManager.setSimCountryIsoForPhone(this.mParentApp.getPhoneId(), "");
        MiuiUiccControllerStub.get(this.mParentApp.getPhoneId()).setTelephonyProperty(this.mParentApp.getPhoneId(), "gsm.sim.operator.orig.alpha", "");
        MiuiUiccControllerStub.get(this.mParentApp.getPhoneId()).setTelephonyProperty(this.mParentApp.getPhoneId(), "gsm.operator.orig.alpha", "");
        this.mRecordsRequested = false;
        this.mLockedRecordsReqReason = 0;
        this.mLoaded.set(false);
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public String getMsisdnNumber() {
        return this.mMsisdn;
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public UsimServiceTable getUsimServiceTable() {
        return this.mUsimServiceTable;
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public String[] getUiccIari() {
        String[] strArr = this.mUsimIari;
        if (strArr != null) {
            return (String[]) strArr.clone();
        }
        return null;
    }

    public String getSimServiceTable() {
        UsimServiceTable usimServiceTable = this.mUsimServiceTable;
        if (usimServiceTable != null) {
            return IccUtils.bytesToHexString(usimServiceTable.getUSIMServiceTable());
        }
        return null;
    }

    protected int getExtFromEf(int i) {
        if (i != 28475) {
            return (i == 28480 && this.mParentApp.getType() == IccCardApplicationStatus.AppType.APPTYPE_USIM) ? IccConstants.EF_EXT5 : IccConstants.EF_EXT1;
        }
        return IccConstants.EF_EXT2;
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public void setMsisdnNumber(String str, String str2, Message message) {
        if (this.mDestroyed.get()) {
            return;
        }
        this.mNewMsisdn = str2;
        this.mNewMsisdnTag = str;
        log("Set MSISDN: " + this.mNewMsisdnTag + " " + Rlog.pii(LOG_TAG, this.mNewMsisdn));
        new AdnRecordLoader(this.mFh).updateEF(new AdnRecord(this.mNewMsisdnTag, this.mNewMsisdn), IccConstants.EF_MSISDN, getExtFromEf(IccConstants.EF_MSISDN), 1, null, obtainMessage(30, message));
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public String getMsisdnAlphaTag() {
        return this.mMsisdnTag;
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public String getVoiceMailNumber() {
        return this.mVoiceMailNum;
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public void setVoiceMailNumber(String str, String str2, Message message) {
        if (this.mDestroyed.get()) {
            return;
        }
        if (this.mIsVoiceMailFixed) {
            AsyncResult.forMessage(message).exception = new IccVmFixedException("Voicemail number is fixed by operator");
            message.sendToTarget();
            return;
        }
        this.mNewVoiceMailNum = str2;
        this.mNewVoiceMailTag = str;
        AdnRecord adnRecord = new AdnRecord(this.mNewVoiceMailTag, this.mNewVoiceMailNum);
        int i = this.mMailboxIndex;
        if (i != 0 && i != 255) {
            new AdnRecordLoader(this.mFh).updateEF(adnRecord, IccConstants.EF_MBDN, IccConstants.EF_EXT6, this.mMailboxIndex, null, obtainMessage(20, 1, 0, message));
        } else {
            if (isCphsMailboxEnabled()) {
                new AdnRecordLoader(this.mFh).updateEF(adnRecord, IccConstants.EF_MAILBOX_CPHS, IccConstants.EF_EXT1, 1, null, obtainMessage(25, 1, 0, message));
                return;
            }
            AsyncResult.forMessage(message).exception = new IccVmNotSupportedException("Update SIM voice mailbox error");
            message.sendToTarget();
        }
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public String getVoiceMailAlphaTag() {
        return this.mVoiceMailTag;
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public void setVoiceMessageWaiting(int i, int i2) {
        if (!this.mDestroyed.get() && i == 1) {
            try {
                byte[] bArr = this.mEfMWIS;
                if (bArr != null) {
                    bArr[0] = (byte) ((bArr[0] & 254) | (i2 == 0 ? 0 : 1));
                    if (i2 < 0) {
                        bArr[1] = 0;
                    } else {
                        bArr[1] = (byte) i2;
                    }
                    this.mFh.updateEFLinearFixed(IccConstants.EF_MWIS, 1, bArr, null, obtainMessage(14, IccConstants.EF_MWIS, 0));
                }
                if (this.mParentApp.getType() == IccCardApplicationStatus.AppType.APPTYPE_USIM) {
                    log("[setVoiceMessageWaiting] It is USIM card, skip write CPHS file");
                    return;
                }
                byte[] bArr2 = this.mEfCPHS_MWI;
                if (bArr2 != null) {
                    bArr2[0] = (byte) ((i2 == 0 ? 5 : 10) | (bArr2[0] & 240));
                    this.mFh.updateEFTransparent(IccConstants.EF_VOICE_MAIL_INDICATOR_CPHS, bArr2, obtainMessage(14, Integer.valueOf(IccConstants.EF_VOICE_MAIL_INDICATOR_CPHS)));
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                logw("Error saving voice mail state to SIM. Probably malformed SIM record", e);
            }
        }
    }

    protected boolean validEfCfis(byte[] bArr) {
        if (bArr != null) {
            byte b = bArr[0];
            if (b < 1 || b > 4) {
                logw("MSP byte: " + ((int) bArr[0]) + " is not between 1 and 4", null);
            }
            for (byte b2 : bArr) {
                if (b2 != -1) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public int getVoiceMessageCount() {
        byte[] bArr = this.mEfMWIS;
        int i = -1;
        if (bArr != null) {
            boolean z = (bArr[0] & 1) != 0;
            int i2 = bArr[1] & 255;
            if (!z || (i2 != 0 && i2 != 255)) {
                i = i2;
            }
            log(" VoiceMessageCount from SIM MWIS = " + i);
            return i;
        }
        byte[] bArr2 = this.mEfCPHS_MWI;
        if (bArr2 == null) {
            return -2;
        }
        int i3 = bArr2[0] & 15;
        if (i3 != 10) {
            i = i3 == 5 ? 0 : -2;
        }
        log(" VoiceMessageCount from SIM CPHS = " + i);
        return i;
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public int getVoiceCallForwardingFlag() {
        return this.mCallForwardingStatus;
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public void setVoiceCallForwardingFlag(int i, boolean z, String str) {
        if (!this.mDestroyed.get() && i == 1) {
            this.mCallForwardingStatus = z ? 1 : 0;
            this.mRecordsEventsRegistrants.notifyResult(1);
            try {
                if (validEfCfis(this.mEfCfis)) {
                    byte[] bArr = this.mEfCfis;
                    if (z) {
                        bArr[1] = (byte) (bArr[1] | 1);
                    } else {
                        bArr[1] = (byte) (bArr[1] & 254);
                    }
                    log("setVoiceCallForwardingFlag: enable=" + z + " mEfCfis=" + IccUtils.bytesToHexString(this.mEfCfis));
                    if (z && !TextUtils.isEmpty(str)) {
                        logv("EF_CFIS: updating cf number, " + Rlog.pii(LOG_TAG, str));
                        String strExtractNetworkPortion = PhoneNumberUtils.extractNetworkPortion(str);
                        if (!TextUtils.isEmpty(strExtractNetworkPortion)) {
                            byte[] bArrNumberToCalledPartyBCD = PhoneNumberUtils.numberToCalledPartyBCD(strExtractNetworkPortion, 1);
                            if (bArrNumberToCalledPartyBCD == null) {
                                return;
                            }
                            System.arraycopy(bArrNumberToCalledPartyBCD, 0, this.mEfCfis, 3, bArrNumberToCalledPartyBCD.length);
                            byte[] bArr2 = this.mEfCfis;
                            bArr2[2] = (byte) bArrNumberToCalledPartyBCD.length;
                            bArr2[14] = -1;
                            bArr2[15] = -1;
                        }
                    }
                    this.mFh.updateEFLinearFixed(IccConstants.EF_CFIS, 1, this.mEfCfis, null, obtainMessage(14, Integer.valueOf(IccConstants.EF_CFIS)));
                } else {
                    log("setVoiceCallForwardingFlag: ignoring enable=" + z + " invalid mEfCfis=" + IccUtils.bytesToHexString(this.mEfCfis));
                }
                byte[] bArr3 = this.mEfCff;
                if (bArr3 != null) {
                    if (z) {
                        bArr3[0] = (byte) ((bArr3[0] & 240) | 10);
                    } else {
                        bArr3[0] = (byte) ((bArr3[0] & 240) | 5);
                    }
                    this.mFh.updateEFTransparent(IccConstants.EF_CFF_CPHS, bArr3, obtainMessage(14, Integer.valueOf(IccConstants.EF_CFF_CPHS)));
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                logw("Error saving call forwarding flag to SIM. Probably malformed SIM record", e);
            }
        }
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public void onRefresh(boolean z, int[] iArr) {
        if (z) {
            fetchSimRecords();
        }
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public String getOperatorNumeric() {
        String imsi = getIMSI();
        if (imsi == null) {
            log("getOperatorNumeric: IMSI == null");
            return null;
        }
        int i = this.mMncLength;
        if (i == -1 || i == 0) {
            if (TextUtils.isEmpty(imsi)) {
                log("getSIMOperatorNumeric: bad mncLength");
                return null;
            }
            this.mMncLength = MiuiUiccControllerStub.get(this.mParentApp.getPhoneId()).getMncLength(this.mParentApp.getPhoneId(), imsi, IccRecords.MCCMNC_CODES_HAVING_3DIGITS_MNC);
        }
        this.mMncLength = MiuiUiccControllerStub.get(this.mParentApp.getPhoneId()).getRecoveryMncLength(this.mParentApp.getPhoneId(), imsi, this.mMncLength);
        int length = imsi.length();
        int i2 = this.mMncLength;
        if (length >= i2 + 3) {
            return imsi.substring(0, i2 + 3);
        }
        return null;
    }

    /* JADX WARN: Code duplicated, block: B:322:0x09a1  */
    /* JADX WARN: Code duplicated, block: B:368:0x0b06  */
    /* JADX WARN: Code duplicated, block: B:372:0x0b0d  */
    /* JADX WARN: Code duplicated, block: B:387:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Not initialized variable reg: 51, insn: 0x0120: MOVE (r3 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r51 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:32:0x0120 */
    /* JADX WARN: Not initialized variable reg: 51, insn: 0x0125: MOVE (r3 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r51 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:34:0x0125 */
    /* JADX WARN: Type inference failed for: r3v10 */
    /* JADX WARN: Type inference failed for: r3v15 */
    /* JADX WARN: Type inference failed for: r3v16 */
    /* JADX WARN: Type inference failed for: r3v17 */
    /* JADX WARN: Type inference failed for: r3v18 */
    /* JADX WARN: Type inference failed for: r3v57 */
    /* JADX WARN: Type inference failed for: r3v58 */
    /* JADX WARN: Type inference failed for: r3v7 */
    /* JADX WARN: Type inference failed for: r3v8 */
    /* JADX WARN: Type inference failed for: r3v9 */
    /* JADX WARN: Type inference failed for: r52v0, types: [android.os.Handler, com.android.internal.telephony.uicc.IccRecords, com.android.internal.telephony.uicc.SIMRecords] */
    @Override // com.android.internal.telephony.uicc.IccRecords, android.os.Handler
    public void handleMessage(Message message) throws Throwable {
        ?? r3;
        boolean z;
        boolean z2;
        boolean z3;
        AdnRecord adnRecord;
        String[] strArr;
        Pair<Message, Object> pairRetrievePendingTransaction;
        Message message2;
        Object obj;
        AdnRecord adnRecord2;
        String str = "HplmnActRecord[]=";
        if (this.mDestroyed.get() && shallIgnoreMessage(message)) {
            loge("Received message " + message + "[" + message.what + "]  while being destroyed. Ignoring.");
            return;
        }
        try {
            int i = message.what;
            try {
                if (i == 30) {
                    AsyncResult asyncResult = (AsyncResult) message.obj;
                    if (asyncResult.exception == null) {
                        this.mMsisdn = this.mNewMsisdn;
                        this.mMsisdnTag = this.mNewMsisdnTag;
                        notifyMsisdnRecordChanged();
                        log("Success to update EF[MSISDN]");
                    }
                    Object obj2 = asyncResult.userObj;
                    if (obj2 != null) {
                        AsyncResult.forMessage((Message) obj2).exception = asyncResult.exception;
                        ((Message) asyncResult.userObj).sendToTarget();
                        return;
                    }
                    return;
                }
                if (i == 50) {
                    this.mCi.getIMSIForApp(this.mParentApp.getAid(), obtainMessage(3));
                    this.mGetImsiRetryCount++;
                    return;
                }
                try {
                    if (i != 1000) {
                        try {
                            switch (i) {
                                case 3:
                                    this.mEssentialRecordsToLoad--;
                                    AsyncResult asyncResult2 = (AsyncResult) message.obj;
                                    if (asyncResult2.exception != null) {
                                        loge("Exception querying IMSI, Exception:" + asyncResult2.exception);
                                        if (TelephonyBaseUtilsStub.isMiuiRom() && this.mGetImsiRetryCount < 5) {
                                            sendMessageDelayed(obtainMessage(50), SatelliteSessionController.DEMO_MODE_SATELLITE_STAY_AT_LISTENING_MILLIS);
                                            return;
                                        }
                                    } else {
                                        setImsi((String) asyncResult2.result);
                                    }
                                    break;
                                case 4:
                                    this.mEssentialRecordsToLoad--;
                                    AsyncResult asyncResult3 = (AsyncResult) message.obj;
                                    byte[] bArr = (byte[]) asyncResult3.result;
                                    if (asyncResult3.exception == null) {
                                        this.mIccId = IccUtils.bcdToString(bArr, 0, bArr.length);
                                        this.mIccId = MiuiUiccControllerStub.get(this.mParentApp.getPhoneId()).resetIccid(this.mParentApp.getPhoneId(), this.mIccId, bArr);
                                        this.mFullIccId = IccUtils.bchToString(bArr, 0, bArr.length);
                                        log("iccid: " + SubscriptionInfo.getPrintableId(this.mFullIccId));
                                    }
                                    break;
                                case 5:
                                    AsyncResult asyncResult4 = (AsyncResult) message.obj;
                                    byte[] bArr2 = (byte[]) asyncResult4.result;
                                    if (asyncResult4.exception == null) {
                                        log("EF_MBI: " + IccUtils.bytesToHexString(bArr2));
                                        int i2 = bArr2[0] & 255;
                                        this.mMailboxIndex = i2;
                                        if (i2 == 0 || i2 == 255) {
                                            z = false;
                                        } else {
                                            log("Got valid mailbox number for MBDN");
                                            setValidMBIStatus(true);
                                            z = true;
                                        }
                                    } else {
                                        z = false;
                                    }
                                    this.mRecordsToLoad++;
                                    if (z) {
                                        log("EVENT_GET_MBI_DONE, to load EF_MBDN");
                                        new AdnRecordLoader(this.mFh).loadFromEF(IccConstants.EF_MBDN, IccConstants.EF_EXT6, this.mMailboxIndex, obtainMessage(6));
                                    } else {
                                        handleMBIEvent();
                                    }
                                    break;
                                case 6:
                                case 11:
                                    this.mVoiceMailNum = null;
                                    this.mVoiceMailTag = null;
                                    AsyncResult asyncResult5 = (AsyncResult) message.obj;
                                    if (asyncResult5.exception != null) {
                                        log("Invalid or missing EF".concat(i == 11 ? "[MAILBOX]" : "[MBDN]"));
                                        if (message.what == 6) {
                                            this.mRecordsToLoad++;
                                            new AdnRecordLoader(this.mFh).loadFromEF(IccConstants.EF_MAILBOX_CPHS, IccConstants.EF_EXT1, 1, obtainMessage(11));
                                        }
                                    } else {
                                        AdnRecord adnRecord3 = (AdnRecord) asyncResult5.result;
                                        if (TelephonyBaseUtilsStub.canShowPrivateInfo()) {
                                            StringBuilder sb = new StringBuilder("VM: ");
                                            sb.append(adnRecord3);
                                            sb.append(message.what == 11 ? " EF[MAILBOX]" : " EF[MBDN]");
                                            log(sb.toString());
                                        }
                                        if (adnRecord3.isEmpty() && message.what == 6) {
                                            this.mRecordsToLoad++;
                                            new AdnRecordLoader(this.mFh).loadFromEF(IccConstants.EF_MAILBOX_CPHS, IccConstants.EF_EXT1, 1, obtainMessage(11));
                                        } else {
                                            this.mVoiceMailNum = adnRecord3.getNumber();
                                            this.mVoiceMailTag = adnRecord3.getAlphaTag();
                                        }
                                    }
                                    break;
                                case 7:
                                    AsyncResult asyncResult6 = (AsyncResult) message.obj;
                                    byte[] bArr3 = (byte[]) asyncResult6.result;
                                    log("EF_MWIS : " + IccUtils.bytesToHexString(bArr3));
                                    if (asyncResult6.exception != null) {
                                        log("EVENT_GET_MWIS_DONE exception = " + asyncResult6.exception);
                                    } else if ((bArr3[0] & 255) == 255) {
                                        log("SIMRecords: Uninitialized record MWIS");
                                    } else {
                                        this.mEfMWIS = bArr3;
                                    }
                                    break;
                                case 8:
                                    AsyncResult asyncResult7 = (AsyncResult) message.obj;
                                    byte[] bArr4 = (byte[]) asyncResult7.result;
                                    log("EF_CPHS_MWI: " + IccUtils.bytesToHexString(bArr4));
                                    if (asyncResult7.exception != null) {
                                        log("EVENT_GET_VOICE_MAIL_INDICATOR_CPHS_DONE exception = " + asyncResult7.exception);
                                    } else {
                                        this.mEfCPHS_MWI = bArr4;
                                    }
                                    break;
                                case 9:
                                    this.mMncLength = 0;
                                    try {
                                        if (!this.mCarrierTestOverride.isInTestMode()) {
                                            AsyncResult asyncResult8 = (AsyncResult) message.obj;
                                            byte[] bArr5 = (byte[]) asyncResult8.result;
                                            if (asyncResult8.exception == null) {
                                                log("EF_AD: " + IccUtils.bytesToHexString(bArr5));
                                                if (bArr5.length < 3) {
                                                    log("Corrupt AD data on SIM");
                                                } else if (bArr5.length == 3) {
                                                    log("MNC length not present in EF_AD");
                                                } else {
                                                    int i3 = bArr5[3] & 15;
                                                    if (i3 == 2 || i3 == 3) {
                                                        this.mMncLength = i3;
                                                    } else {
                                                        log("Received invalid or unset MNC Length=" + i3);
                                                    }
                                                }
                                            }
                                        }
                                        updateOperatorPlmn();
                                    } catch (Throwable th) {
                                        updateOperatorPlmn();
                                        throw th;
                                    }
                                    break;
                                case 10:
                                    this.mEssentialRecordsToLoad--;
                                    AsyncResult asyncResult9 = (AsyncResult) message.obj;
                                    if (asyncResult9.exception != null) {
                                        log("Invalid or missing EF[MSISDN]");
                                    } else {
                                        AdnRecord adnRecord4 = (AdnRecord) asyncResult9.result;
                                        this.mMsisdn = adnRecord4.getNumber();
                                        this.mMsisdnTag = adnRecord4.getAlphaTag();
                                        notifyMsisdnRecordChanged();
                                        log("MSISDN: " + Rlog.pii(LOG_TAG, this.mMsisdn));
                                    }
                                    break;
                                case 12:
                                    getSpnFsm(false, (AsyncResult) message.obj);
                                    break;
                                case 13:
                                    AsyncResult asyncResult10 = (AsyncResult) message.obj;
                                    byte[] bArr6 = (byte[]) asyncResult10.result;
                                    if (asyncResult10.exception == null) {
                                        parseEfSpdi(bArr6);
                                    }
                                    break;
                                case 14:
                                    Throwable th2 = ((AsyncResult) message.obj).exception;
                                    if (th2 != null) {
                                        logw("update failed. ", th2);
                                        return;
                                    }
                                    return;
                                case 15:
                                    AsyncResult asyncResult11 = (AsyncResult) message.obj;
                                    if (asyncResult11.exception == null) {
                                        parseEfPnn((ArrayList) asyncResult11.result);
                                    }
                                    break;
                                case 16:
                                    AsyncResult asyncResult12 = (AsyncResult) message.obj;
                                    if (asyncResult12.exception == null) {
                                        parseEfOpl((ArrayList) asyncResult12.result);
                                    }
                                    break;
                                case 17:
                                    AsyncResult asyncResult13 = (AsyncResult) message.obj;
                                    byte[] bArr7 = (byte[]) asyncResult13.result;
                                    if (asyncResult13.exception == null) {
                                        this.mUsimServiceTable = new UsimServiceTable(bArr7);
                                        log("SST: " + this.mUsimServiceTable);
                                        setSSTdata(bArr7);
                                    }
                                    break;
                                default:
                                    switch (i) {
                                        case 20:
                                            AsyncResult asyncResult14 = (AsyncResult) message.obj;
                                            log("EVENT_SET_MBDN_DONE ex:" + asyncResult14.exception);
                                            if (asyncResult14.exception == null) {
                                                Object obj3 = asyncResult14.result;
                                                if (obj3 != null && (adnRecord2 = (AdnRecord) obj3) != null) {
                                                    this.mNewVoiceMailTag = adnRecord2.mAlphaTag;
                                                }
                                                this.mVoiceMailNum = this.mNewVoiceMailNum;
                                                this.mVoiceMailTag = this.mNewVoiceMailTag;
                                            }
                                            if (isCphsMailboxEnabled()) {
                                                AdnRecord adnRecord5 = new AdnRecord(this.mVoiceMailTag, this.mVoiceMailNum);
                                                Object obj4 = asyncResult14.userObj;
                                                Message message3 = (Message) obj4;
                                                if (asyncResult14.exception == null && obj4 != null) {
                                                    AsyncResult.forMessage((Message) obj4).exception = null;
                                                    ((Message) asyncResult14.userObj).sendToTarget();
                                                    log("Callback with MBDN successful.");
                                                    message3 = null;
                                                }
                                                new AdnRecordLoader(this.mFh).updateEF(adnRecord5, IccConstants.EF_MAILBOX_CPHS, IccConstants.EF_EXT1, 1, null, obtainMessage(25, 1, 0, message3));
                                                return;
                                            }
                                            if (asyncResult14.userObj != null) {
                                                CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService("carrier_config");
                                                if (asyncResult14.exception != null && carrierConfigManager != null) {
                                                    PersistableBundle configForSubId = carrierConfigManager.getConfigForSubId(SubscriptionManager.getSubscriptionId(this.mParentApp.getPhoneId()));
                                                    if (configForSubId != null && configForSubId.getBoolean("editable_voicemail_number_bool")) {
                                                        AsyncResult.forMessage((Message) asyncResult14.userObj).exception = new IccVmNotSupportedException("Update SIM voice mailbox error");
                                                    } else {
                                                        AsyncResult.forMessage((Message) asyncResult14.userObj).exception = asyncResult14.exception;
                                                    }
                                                } else {
                                                    AsyncResult.forMessage((Message) asyncResult14.userObj).exception = asyncResult14.exception;
                                                }
                                                ((Message) asyncResult14.userObj).sendToTarget();
                                                return;
                                            }
                                            return;
                                        case 21:
                                            AsyncResult asyncResult15 = (AsyncResult) message.obj;
                                            Integer num = (Integer) asyncResult15.result;
                                            if (asyncResult15.exception == null && num != null) {
                                                log("READ EF_SMS RECORD index=" + num);
                                                this.mFh.loadEFLinearFixed(IccConstants.EF_SMS, num.intValue(), obtainMessage(22));
                                                return;
                                            }
                                            loge("Error on SMS_ON_SIM with exp " + asyncResult15.exception + " index " + num);
                                            return;
                                        case 22:
                                            AsyncResult asyncResult16 = (AsyncResult) message.obj;
                                            if (asyncResult16.exception == null) {
                                                handleSms((byte[]) asyncResult16.result);
                                                return;
                                            }
                                            loge("Error on GET_SMS with exp " + asyncResult16.exception);
                                            return;
                                        default:
                                            try {
                                                switch (i) {
                                                    case 24:
                                                        AsyncResult asyncResult17 = (AsyncResult) message.obj;
                                                        byte[] bArr8 = (byte[]) asyncResult17.result;
                                                        if (asyncResult17.exception != null) {
                                                            this.mEfCff = null;
                                                        } else {
                                                            log("EF_CFF_CPHS: " + IccUtils.bytesToHexString(bArr8));
                                                            this.mEfCff = bArr8;
                                                        }
                                                        break;
                                                    case 25:
                                                        AsyncResult asyncResult18 = (AsyncResult) message.obj;
                                                        if (asyncResult18.exception == null) {
                                                            Object obj5 = asyncResult18.result;
                                                            if (obj5 != null && (adnRecord = (AdnRecord) obj5) != null) {
                                                                this.mNewVoiceMailTag = adnRecord.mAlphaTag;
                                                            }
                                                            this.mVoiceMailNum = this.mNewVoiceMailNum;
                                                            this.mVoiceMailTag = this.mNewVoiceMailTag;
                                                        } else {
                                                            log("Set CPHS MailBox with exception: " + asyncResult18.exception);
                                                        }
                                                        if (asyncResult18.userObj != null) {
                                                            log("Callback with CPHS MB successful.");
                                                            AsyncResult.forMessage((Message) asyncResult18.userObj).exception = asyncResult18.exception;
                                                            ((Message) asyncResult18.userObj).sendToTarget();
                                                            return;
                                                        }
                                                        return;
                                                    case 26:
                                                        AsyncResult asyncResult19 = (AsyncResult) message.obj;
                                                        if (asyncResult19.exception == null) {
                                                            this.mCphsInfo = (byte[]) asyncResult19.result;
                                                            log("iCPHS: " + IccUtils.bytesToHexString(this.mCphsInfo));
                                                            loadMailboxCphs();
                                                        }
                                                        break;
                                                    default:
                                                        switch (i) {
                                                            case 32:
                                                                AsyncResult asyncResult20 = (AsyncResult) message.obj;
                                                                byte[] bArr9 = (byte[]) asyncResult20.result;
                                                                if (asyncResult20.exception != null) {
                                                                    this.mEfCfis = null;
                                                                } else {
                                                                    log("EF_CFIS: " + IccUtils.bytesToHexString(bArr9));
                                                                    this.mEfCfis = bArr9;
                                                                }
                                                                break;
                                                            case 33:
                                                                AsyncResult asyncResult21 = (AsyncResult) message.obj;
                                                                if (asyncResult21.exception != null) {
                                                                    loge("Exception in fetching EF_CSP data " + asyncResult21.exception);
                                                                } else {
                                                                    byte[] bArr10 = (byte[]) asyncResult21.result;
                                                                    log("EF_CSP: " + IccUtils.bytesToHexString(bArr10));
                                                                    handleEfCspData(bArr10);
                                                                }
                                                                break;
                                                            case 34:
                                                                this.mEssentialRecordsToLoad--;
                                                                AsyncResult asyncResult22 = (AsyncResult) message.obj;
                                                                byte[] bArr11 = (byte[]) asyncResult22.result;
                                                                if (asyncResult22.exception != null) {
                                                                    loge("Exception in get GID1 " + asyncResult22.exception);
                                                                    this.mGid1 = null;
                                                                } else {
                                                                    this.mGid1 = IccUtils.bytesToHexString(bArr11);
                                                                    log("GID1: " + this.mGid1);
                                                                }
                                                                break;
                                                            default:
                                                                switch (i) {
                                                                    case 36:
                                                                        this.mEssentialRecordsToLoad--;
                                                                        AsyncResult asyncResult23 = (AsyncResult) message.obj;
                                                                        byte[] bArr12 = (byte[]) asyncResult23.result;
                                                                        if (asyncResult23.exception != null) {
                                                                            loge("Exception in get GID2 " + asyncResult23.exception);
                                                                            this.mGid2 = null;
                                                                        } else {
                                                                            this.mGid2 = IccUtils.bytesToHexString(bArr12);
                                                                            log("GID2: " + this.mGid2);
                                                                        }
                                                                        break;
                                                                    case 37:
                                                                        AsyncResult asyncResult24 = (AsyncResult) message.obj;
                                                                        byte[] bArr13 = (byte[]) asyncResult24.result;
                                                                        if (asyncResult24.exception != null || bArr13 == null) {
                                                                            loge("Failed getting User PLMN with Access Tech Records: " + asyncResult24.exception);
                                                                        } else {
                                                                            log("Received a PlmnActRecord, raw=" + IccUtils.bytesToHexString(bArr13));
                                                                            this.mPlmnActRecords = PlmnActRecord.getRecords(bArr13);
                                                                            if (VDBG) {
                                                                                log("PlmnActRecords=" + Arrays.toString(this.mPlmnActRecords));
                                                                            }
                                                                        }
                                                                        break;
                                                                    case 38:
                                                                        AsyncResult asyncResult25 = (AsyncResult) message.obj;
                                                                        byte[] bArr14 = (byte[]) asyncResult25.result;
                                                                        if (asyncResult25.exception != null || bArr14 == null) {
                                                                            loge("Failed getting Operator PLMN with Access Tech Records: " + asyncResult25.exception);
                                                                        } else {
                                                                            log("Received a PlmnActRecord, raw=" + IccUtils.bytesToHexString(bArr14));
                                                                            this.mOplmnActRecords = PlmnActRecord.getRecords(bArr14);
                                                                            if (VDBG) {
                                                                                log("OplmnActRecord[]=" + Arrays.toString(this.mOplmnActRecords));
                                                                            }
                                                                        }
                                                                        break;
                                                                    case 39:
                                                                        AsyncResult asyncResult26 = (AsyncResult) message.obj;
                                                                        byte[] bArr15 = (byte[]) asyncResult26.result;
                                                                        if (asyncResult26.exception != null || bArr15 == null) {
                                                                            loge("Failed getting Home PLMN with Access Tech Records: " + asyncResult26.exception);
                                                                        } else {
                                                                            log("Received a PlmnActRecord, raw=" + IccUtils.bytesToHexString(bArr15));
                                                                            this.mHplmnActRecords = PlmnActRecord.getRecords(bArr15);
                                                                            log("HplmnActRecord[]=" + Arrays.toString(this.mHplmnActRecords));
                                                                        }
                                                                        break;
                                                                    case 40:
                                                                        AsyncResult asyncResult27 = (AsyncResult) message.obj;
                                                                        byte[] bArr16 = (byte[]) asyncResult27.result;
                                                                        if (asyncResult27.exception != null || bArr16 == null) {
                                                                            loge("Failed getting Equivalent Home PLMNs: " + asyncResult27.exception);
                                                                        } else {
                                                                            this.mEhplmns = parseBcdPlmnList(bArr16, "Equivalent Home");
                                                                        }
                                                                        break;
                                                                    case 41:
                                                                        AsyncResult asyncResult28 = (AsyncResult) message.obj;
                                                                        byte[] bArr17 = (byte[]) asyncResult28.result;
                                                                        if (asyncResult28.exception != null || bArr17 == null) {
                                                                            loge("Failed getting Forbidden PLMNs: " + asyncResult28.exception);
                                                                        } else {
                                                                            this.mFplmns = parseBcdPlmnList(bArr17, "Forbidden");
                                                                        }
                                                                        if (message.arg1 == 1238273) {
                                                                            if (VDBG) {
                                                                                logv("getForbiddenPlmns(): send async response");
                                                                            }
                                                                            Message message4 = (Message) retrievePendingTransaction(Integer.valueOf(message.arg2)).first;
                                                                            if (message4 != null) {
                                                                                Throwable th3 = asyncResult28.exception;
                                                                                if (th3 == null && bArr17 != null && (strArr = this.mFplmns) != null) {
                                                                                    AsyncResult.forMessage(message4, Arrays.copyOf(strArr, strArr.length), (Throwable) null);
                                                                                } else {
                                                                                    AsyncResult.forMessage(message4, (Object) null, th3);
                                                                                }
                                                                                message4.sendToTarget();
                                                                                return;
                                                                            }
                                                                            loge("Failed to retrieve a response message for FPLMN");
                                                                            return;
                                                                        }
                                                                        break;
                                                                    case 42:
                                                                        AsyncResult asyncResult29 = (AsyncResult) message.obj;
                                                                        int i4 = message.arg2;
                                                                        if (asyncResult29.exception != null && (obj = asyncResult29.userObj) != null) {
                                                                            message2 = (Message) obj;
                                                                            pairRetrievePendingTransaction = null;
                                                                        } else {
                                                                            pairRetrievePendingTransaction = retrievePendingTransaction(Integer.valueOf(i4));
                                                                            message2 = (Message) pairRetrievePendingTransaction.first;
                                                                            Objects.requireNonNull(message2);
                                                                        }
                                                                        if (asyncResult29.exception != null) {
                                                                            AsyncResult.forMessage(message2).exception = asyncResult29.exception;
                                                                            message2.sendToTarget();
                                                                            return;
                                                                        }
                                                                        List list = (List) pairRetrievePendingTransaction.second;
                                                                        int iIntValue = ((Integer) asyncResult29.result).intValue();
                                                                        if (iIntValue >= 0 && iIntValue % 3 == 0) {
                                                                            int i5 = iIntValue / 3;
                                                                            this.mFh.updateEFTransparent(IccConstants.EF_FPLMN, IccUtils.encodeFplmns(list, iIntValue), obtainMessage(43, message.arg1, storePendingTransaction(message2, list.size() <= i5 ? list : list.subList(0, i5))));
                                                                            return;
                                                                        }
                                                                        loge("Failed to retrieve a correct fplmn size: " + iIntValue);
                                                                        AsyncResult.forMessage(message2, -1, (Throwable) null);
                                                                        message2.sendToTarget();
                                                                        return;
                                                                    case 43:
                                                                        AsyncResult asyncResult30 = (AsyncResult) message.obj;
                                                                        if (asyncResult30.exception != null) {
                                                                            loge("Failed setting Forbidden PLMNs: " + asyncResult30.exception);
                                                                            return;
                                                                        }
                                                                        Pair<Message, Object> pairRetrievePendingTransaction2 = retrievePendingTransaction(Integer.valueOf(message.arg2));
                                                                        Message message5 = (Message) pairRetrievePendingTransaction2.first;
                                                                        String[] strArr2 = (String[]) ((List) pairRetrievePendingTransaction2.second).toArray(new String[0]);
                                                                        this.mFplmns = strArr2;
                                                                        if (message.arg1 == 1238273) {
                                                                            AsyncResult.forMessage(message5, Integer.valueOf(strArr2.length), (Throwable) null);
                                                                            message5.sendToTarget();
                                                                        }
                                                                        log("Successfully setted fplmns " + asyncResult30.result);
                                                                        return;
                                                                    default:
                                                                        switch (i) {
                                                                            case 46:
                                                                                AsyncResult asyncResult31 = (AsyncResult) message.obj;
                                                                                if (asyncResult31.exception != null) {
                                                                                    loge("Failed to read USIM EF_SMSS field error=" + asyncResult31.exception);
                                                                                } else {
                                                                                    this.mSmssValues = (byte[]) asyncResult31.result;
                                                                                    if (VDBG) {
                                                                                        log("SIMRecords - EF_SMSS TPMR value = " + getSmssTpmrValue());
                                                                                    }
                                                                                }
                                                                                break;
                                                                            case 47:
                                                                                AsyncResult asyncResult32 = (AsyncResult) message.obj;
                                                                                if (asyncResult32.exception != null) {
                                                                                    loge("Failed to read USIM EF_PSISMSC field error=" + asyncResult32.exception);
                                                                                } else {
                                                                                    byte[] bArr18 = (byte[]) asyncResult32.result;
                                                                                    if (bArr18 != null && bArr18.length > 0) {
                                                                                        this.mPsiSmsc = parseEfPsiSmsc(bArr18);
                                                                                        if (VDBG) {
                                                                                            log("SIMRecords - EF_PSISMSC value = " + this.mPsiSmsc);
                                                                                        }
                                                                                    }
                                                                                }
                                                                                break;
                                                                            case 48:
                                                                                AsyncResult asyncResult33 = (AsyncResult) message.obj;
                                                                                if (asyncResult33.exception != null) {
                                                                                    loge("Failed to read USIM EF_IARI field error=" + asyncResult33.exception);
                                                                                } else {
                                                                                    List list2 = (List) asyncResult33.result;
                                                                                    if (list2 != null && list2.size() > 0) {
                                                                                        parseEfIari(list2);
                                                                                    }
                                                                                }
                                                                                break;
                                                                            default:
                                                                                super.handleMessage(message);
                                                                                return;
                                                                        }
                                                                        break;
                                                                }
                                                                break;
                                                        }
                                                        break;
                                                }
                                            } catch (RuntimeException e) {
                                                e = e;
                                                r3 = z3;
                                                try {
                                                    logw("Exception parsing SIM record", e);
                                                    if (r3 != 0) {
                                                        onRecordLoaded();
                                                    }
                                                } catch (Throwable th4) {
                                                    th = th4;
                                                    if (r3 != 0) {
                                                        onRecordLoaded();
                                                    }
                                                    throw th;
                                                }
                                            } catch (Throwable th5) {
                                                th = th5;
                                                r3 = z2;
                                                if (r3 != 0) {
                                                    onRecordLoaded();
                                                }
                                                throw th;
                                            }
                                            break;
                                    }
                                    break;
                            }
                        } catch (RuntimeException e2) {
                            e = e2;
                            r3 = 0;
                        } catch (Throwable th6) {
                            th = th6;
                            r3 = 0;
                        }
                    } else {
                        AsyncResult asyncResult34 = (AsyncResult) message.obj;
                        if (asyncResult34.exception != null) {
                            loge("Exception querying ICCID, Exception:" + asyncResult34.exception);
                        } else {
                            String str2 = (String) asyncResult34.result;
                            this.mIccId = str2;
                            this.mFullIccId = str2;
                            log("ntn iccid: " + SubscriptionInfo.getPrintableId(this.mFullIccId));
                        }
                    }
                    onRecordLoaded();
                } catch (RuntimeException e3) {
                    e = e3;
                    r3 = 1;
                    logw("Exception parsing SIM record", e);
                    if (r3 != 0) {
                        onRecordLoaded();
                    }
                } catch (Throwable th7) {
                    th = th7;
                    r3 = 1;
                    if (r3 != 0) {
                        onRecordLoaded();
                    }
                    throw th;
                }
            } catch (RuntimeException e4) {
                e = e4;
                r3 = str;
                logw("Exception parsing SIM record", e);
                if (r3 != 0) {
                    onRecordLoaded();
                }
            } catch (Throwable th8) {
                th = th8;
                r3 = str;
                if (r3 != 0) {
                    onRecordLoaded();
                }
                throw th;
            }
        } catch (RuntimeException e5) {
            e = e5;
            str = null;
        } catch (Throwable th9) {
            th = th9;
            str = null;
        }
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
            SIMRecords sIMRecords = SIMRecords.this;
            if (th != null) {
                sIMRecords.loge("Record Load Exception: " + asyncResult.exception);
            } else {
                sIMRecords.mEfPl = (byte[]) asyncResult.result;
                sIMRecords.log("EF_PL=" + IccUtils.bytesToHexString(SIMRecords.this.mEfPl));
            }
        }
    }

    private class EfUsimLiLoaded implements IccRecords.IccRecordLoaded {
        private EfUsimLiLoaded() {
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public String getEfName() {
            return "EF_LI";
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public void onRecordLoaded(AsyncResult asyncResult) {
            Throwable th = asyncResult.exception;
            SIMRecords sIMRecords = SIMRecords.this;
            if (th != null) {
                sIMRecords.loge("Record Load Exception: " + asyncResult.exception);
            } else {
                sIMRecords.mEfLi = (byte[]) asyncResult.result;
                sIMRecords.log("EF_LI=" + IccUtils.bytesToHexString(SIMRecords.this.mEfLi));
            }
        }
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    protected void handleFileUpdate(int i) {
        if (i != 28435) {
            if (i == 28437) {
                this.mRecordsToLoad++;
                log("[CSP] SIM Refresh for EF_CSP_CPHS");
                this.mFh.loadEFTransparent(IccConstants.EF_CSP_CPHS, obtainMessage(33));
                return;
            }
            if (i == 28439) {
                this.mRecordsToLoad++;
                new AdnRecordLoader(this.mFh).loadFromEF(IccConstants.EF_MAILBOX_CPHS, IccConstants.EF_EXT1, 1, obtainMessage(11));
                return;
            }
            if (i == 28475) {
                log("SIM Refresh called for EF_FDN");
                this.mParentApp.queryFdn();
                this.mAdnCache.reset();
                return;
            }
            if (i == 28480) {
                this.mRecordsToLoad++;
                log("SIM Refresh called for EF_MSISDN");
                new AdnRecordLoader(this.mFh).loadFromEF(IccConstants.EF_MSISDN, getExtFromEf(IccConstants.EF_MSISDN), 1, obtainMessage(10));
                return;
            } else if (i == 28615) {
                this.mRecordsToLoad++;
                new AdnRecordLoader(this.mFh).loadFromEF(IccConstants.EF_MBDN, IccConstants.EF_EXT6, this.mMailboxIndex, obtainMessage(6));
                return;
            } else if (i != 28619) {
                if (MiuiUiccControllerStub.get(this.mParentApp.getPhoneId()).resetIccRecordFileUpdate(this.mParentApp.getPhoneId(), this.mParentApp, i)) {
                    log("File update, mLoaded should not be set to false");
                    return;
                }
                this.mLoaded.set(false);
                this.mAdnCache.reset();
                MiuiUiccControllerStub.get(this.mParentApp.getPhoneId()).resetIccRecord(this.mParentApp.getPhoneId(), this.mParentApp);
                fetchSimRecords();
                return;
            }
        }
        log("SIM Refresh called for EF_CFIS or EF_CFF_CPHS");
        loadCallForwardingRecords();
    }

    private void dispatchGsmMessage(SmsMessage smsMessage) {
        this.mNewSmsRegistrants.notifyResult(smsMessage);
    }

    private void handleSms(byte[] bArr) {
        log("handleSms status : " + ((int) bArr[0]));
        if ((bArr[0] & 7) == 3) {
            int length = bArr.length - 1;
            byte[] bArr2 = new byte[length];
            System.arraycopy(bArr, 1, bArr2, 0, length);
            dispatchGsmMessage(SmsMessage.createFromPdu(bArr2, "3gpp"));
        }
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    protected void onRecordLoaded() {
        this.mRecordsToLoad--;
        log("onRecordLoaded " + this.mRecordsToLoad + " requested: " + this.mRecordsRequested + ",mEssentialRecordsToLoad:" + this.mEssentialRecordsToLoad);
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

    protected void setVoiceCallForwardingFlagFromSimRecords() {
        if (validEfCfis(this.mEfCfis)) {
            this.mCallForwardingStatus = this.mEfCfis[1] & 1;
            log("EF_CFIS: callForwardingEnabled=" + this.mCallForwardingStatus);
            return;
        }
        byte[] bArr = this.mEfCff;
        if (bArr != null && bArr.length != 0) {
            this.mCallForwardingStatus = (bArr[0] & 15) != 10 ? 0 : 1;
            log("EF_CFF: callForwardingEnabled=" + this.mCallForwardingStatus);
            return;
        }
        this.mCallForwardingStatus = -1;
        log("EF_CFIS and EF_CFF not valid. callForwardingEnabled=" + this.mCallForwardingStatus);
    }

    protected void setSimLanguageFromEF() {
        if (Resources.getSystem().getBoolean(17892070)) {
            setSimLanguage(this.mEfLi, this.mEfPl);
        } else {
            log("Not using EF LI/EF PL");
        }
    }

    protected void onLockedAllRecordsLoaded() {
        setSimLanguageFromEF();
        setVoiceCallForwardingFlagFromSimRecords();
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
        String operatorNumeric = getOperatorNumeric();
        if (!TextUtils.isEmpty(operatorNumeric)) {
            log("onAllEssentialRecordsLoaded set 'gsm.sim.operator.numeric' to operator='" + operatorNumeric + "'");
            this.mTelephonyManager.setSimOperatorNumericForPhone(this.mParentApp.getPhoneId(), operatorNumeric);
        } else {
            log("onAllEssentialRecordsLoaded empty 'gsm.sim.operator.numeric' skipping");
        }
        String imsi = getIMSI();
        if (!TextUtils.isEmpty(imsi) && imsi.length() >= 3) {
            log("onEssentialAllRecordsLoaded set mcc imsi".concat(VDBG ? "=".concat(imsi) : ""));
            this.mTelephonyManager.setSimCountryIsoForPhone(this.mParentApp.getPhoneId(), MccTable.countryCodeForMcc(imsi.substring(0, 3)));
        } else {
            log("onEssentialAllRecordsLoaded empty imsi skipping setting mcc");
        }
        setVoiceMailByCountry(operatorNumeric);
        this.mEssentialRecordsListenerNotified = true;
        this.mEssentialRecordsLoadedRegistrants.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
    }

    private void fetchEssentialSimRecords() {
        log("fetchEssentialSimRecords: mRecordsToLoad = " + this.mRecordsToLoad + " mEssentialRecordsToLoad = " + this.mEssentialRecordsToLoad);
        this.mCi.getIMSIForApp(this.mParentApp.getAid(), obtainMessage(3));
        this.mRecordsToLoad = this.mRecordsToLoad + 1;
        this.mEssentialRecordsToLoad = this.mEssentialRecordsToLoad + 1;
        this.mFh.loadEFTransparent(IccConstants.EF_ICCID, obtainMessage(4));
        this.mRecordsToLoad++;
        this.mEssentialRecordsToLoad++;
        new AdnRecordLoader(this.mFh).loadFromEF(IccConstants.EF_MSISDN, getExtFromEf(IccConstants.EF_MSISDN), 1, obtainMessage(10));
        this.mRecordsToLoad++;
        this.mEssentialRecordsToLoad++;
        this.mFh.loadEFTransparent(IccConstants.EF_GID1, obtainMessage(34));
        this.mRecordsToLoad++;
        this.mEssentialRecordsToLoad++;
        this.mFh.loadEFTransparent(IccConstants.EF_GID2, obtainMessage(36));
        this.mRecordsToLoad++;
        this.mEssentialRecordsToLoad++;
        log("fetchEssentialSimRecords " + this.mRecordsToLoad + " requested: " + this.mRecordsRequested);
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    protected void onAllRecordsLoaded() {
        log("record load complete");
        setSimLanguageFromEF();
        setVoiceCallForwardingFlagFromSimRecords();
        String operatorNumeric = getOperatorNumeric();
        if (!TextUtils.isEmpty(operatorNumeric) && checkCdma3gCard()) {
            String strOnOperatorNumericOrNameSet = MiuiUiccControllerStub.get(this.mParentApp.getPhoneId()).onOperatorNumericOrNameSet(this.mParentApp.getPhoneId(), "gsm.sim.operator.numeric", operatorNumeric);
            log("onAllRecordsLoaded set 'gsm.sim.operator.numeric' to operator='" + strOnOperatorNumericOrNameSet + "'");
            operatorNumeric = MiuiUiccControllerStub.get(this.mParentApp.getPhoneId()).onOperatorNumericOrNameSet(this.mParentApp.getPhoneId(), "gsm.sim.operator.numeric", strOnOperatorNumericOrNameSet);
            this.mTelephonyManager.setSimOperatorNumericForPhone(this.mParentApp.getPhoneId(), operatorNumeric);
        } else {
            log("onAllRecordsLoaded empty 'gsm.sim.operator.numeric' skipping");
        }
        String imsi = getIMSI();
        if (!TextUtils.isEmpty(imsi) && imsi.length() >= 3 && checkCdma3gCard()) {
            log("onAllRecordsLoaded set mcc imsi".concat(VDBG ? "=".concat(imsi) : ""));
            this.mTelephonyManager.setSimCountryIsoForPhone(this.mParentApp.getPhoneId(), MccTable.countryCodeForMcc(imsi.substring(0, 3)));
        } else {
            log("onAllRecordsLoaded empty imsi skipping setting mcc");
        }
        setVoiceMailByCountry(operatorNumeric);
        this.mLoaded.set(true);
        this.mRecordsLoadedRegistrants.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
    }

    protected void setVoiceMailByCountry(String str) {
        if (!this.mDestroyed.get() && this.mVmConfig.containsCarrier(str)) {
            this.mIsVoiceMailFixed = true;
            this.mVoiceMailNum = this.mVmConfig.getVoiceMailNumber(str);
            this.mVoiceMailTag = this.mVmConfig.getVoiceMailTag(str);
        }
    }

    public void getForbiddenPlmns(Message message) {
        this.mFh.loadEFTransparent(IccConstants.EF_FPLMN, obtainMessage(41, 1238273, storePendingTransaction(message)));
    }

    public void setForbiddenPlmns(Message message, List<String> list) {
        this.mFh.getEFTransparentRecordSize(IccConstants.EF_FPLMN, obtainMessage(42, 1238273, storePendingTransaction(message, list)));
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public void onReady() {
        fetchSimRecords();
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    protected void onLocked() {
        log("only fetch EF_LI, EF_PL and EF_ICCID in locked state");
        super.onLocked();
        loadEfLiAndEfPl();
        this.mFh.loadEFTransparent(IccConstants.EF_ICCID, obtainMessage(4));
        this.mRecordsToLoad++;
        if (MiuiTelephonyManagerStub.get().isSatelliteEnabled() && isNtnChipVendor_54()) {
            ((RIL) this.mCi).getIccidForApp(this.mParentApp.getAid(), obtainMessage(1000));
            this.mRecordsToLoad++;
        }
    }

    protected void loadEfLiAndEfPl() {
        if (this.mParentApp.getType() == IccCardApplicationStatus.AppType.APPTYPE_USIM) {
            this.mFh.loadEFTransparent(IccConstants.EF_LI, obtainMessage(100, new EfUsimLiLoaded()));
            this.mRecordsToLoad++;
            this.mFh.loadEFTransparent(IccConstants.EF_PL, obtainMessage(100, new EfPlLoaded()));
            this.mRecordsToLoad++;
        }
    }

    protected void loadCallForwardingRecords() {
        this.mRecordsRequested = true;
        loadCfIfFile();
        loadCffFile();
    }

    protected void fetchSimRecords() {
        this.mRecordsRequested = true;
        fetchEssentialSimRecords();
        log("fetchSimRecords " + this.mRecordsToLoad);
        if (MiuiTelephonyManagerStub.get().isSatelliteEnabled() && isNtnChipVendor_54()) {
            ((RIL) this.mCi).getIccidForApp(this.mParentApp.getAid(), obtainMessage(1000));
            this.mRecordsToLoad++;
        }
        this.mFh.loadEFLinearFixed(IccConstants.EF_MBI, 1, obtainMessage(5));
        this.mRecordsToLoad++;
        this.mFh.loadEFTransparent(IccConstants.EF_AD, obtainMessage(9));
        this.mRecordsToLoad++;
        this.mFh.loadEFLinearFixed(IccConstants.EF_MWIS, 1, obtainMessage(7));
        this.mRecordsToLoad++;
        this.mFh.loadEFTransparent(IccConstants.EF_VOICE_MAIL_INDICATOR_CPHS, obtainMessage(8));
        this.mRecordsToLoad++;
        loadCallForwardingRecords();
        getSpnFsm(true, null);
        this.mFh.loadEFTransparent(IccConstants.EF_SPDI, obtainMessage(13));
        this.mRecordsToLoad++;
        this.mFh.loadEFLinearFixedAll(IccConstants.EF_PNN, obtainMessage(15));
        this.mRecordsToLoad++;
        this.mFh.loadEFLinearFixedAll(IccConstants.EF_OPL, obtainMessage(16));
        this.mRecordsToLoad++;
        this.mFh.loadEFTransparent(IccConstants.EF_SST, obtainMessage(17));
        this.mRecordsToLoad++;
        this.mFh.loadEFTransparent(IccConstants.EF_INFO_CPHS, obtainMessage(26));
        this.mRecordsToLoad++;
        this.mFh.loadEFTransparent(IccConstants.EF_CSP_CPHS, obtainMessage(33));
        this.mRecordsToLoad++;
        this.mFh.loadEFTransparent(IccConstants.EF_PLMN_W_ACT, obtainMessage(37));
        this.mRecordsToLoad++;
        this.mFh.loadEFTransparent(IccConstants.EF_OPLMN_W_ACT, obtainMessage(38));
        this.mRecordsToLoad++;
        this.mFh.loadEFTransparent(IccConstants.EF_HPLMN_W_ACT, obtainMessage(39));
        this.mRecordsToLoad++;
        this.mFh.loadEFTransparent(IccConstants.EF_EHPLMN, obtainMessage(40));
        this.mRecordsToLoad++;
        this.mFh.loadEFTransparent(IccConstants.EF_FPLMN, obtainMessage(41, 1238272, -1));
        this.mRecordsToLoad++;
        loadEfLiAndEfPl();
        this.mFh.getEFLinearRecordSize(IccConstants.EF_SMS, obtainMessage(28));
        this.mRecordsToLoad++;
        this.mFh.loadEFLinearFixed(IccConstants.EF_PSISMSC, 1, obtainMessage(47));
        this.mRecordsToLoad++;
        this.mFh.loadEFTransparent(IccConstants.EF_SMSS, obtainMessage(46));
        this.mRecordsToLoad++;
        this.mFh.loadEFLinearFixedAll(IccConstants.EF_IARI, obtainMessage(48));
        this.mRecordsToLoad++;
        log("fetchSimRecords " + this.mRecordsToLoad + " requested: " + this.mRecordsRequested);
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public int getCarrierNameDisplayCondition() {
        return this.mCarrierNameDisplayCondition;
    }

    protected void getSpnFsm(boolean z, AsyncResult asyncResult) {
        GetSpnFsmState getSpnFsmState;
        if (z) {
            GetSpnFsmState getSpnFsmState2 = this.mSpnState;
            if (getSpnFsmState2 == GetSpnFsmState.READ_SPN_3GPP || getSpnFsmState2 == GetSpnFsmState.READ_SPN_CPHS || getSpnFsmState2 == GetSpnFsmState.READ_SPN_SHORT_CPHS || getSpnFsmState2 == (getSpnFsmState = GetSpnFsmState.INIT)) {
                this.mSpnState = GetSpnFsmState.INIT;
                return;
            }
            this.mSpnState = getSpnFsmState;
        }
        int iOrdinal = this.mSpnState.ordinal();
        if (iOrdinal == 1) {
            setServiceProviderName(null);
            this.mFh.loadEFTransparent(IccConstants.EF_SPN, obtainMessage(12));
            this.mRecordsToLoad++;
            this.mSpnState = GetSpnFsmState.READ_SPN_3GPP;
            return;
        }
        if (iOrdinal == 2) {
            if (asyncResult != null && asyncResult.exception == null) {
                byte[] bArr = (byte[]) asyncResult.result;
                this.mCarrierNameDisplayCondition = IccRecords.convertSpnDisplayConditionToBitmask(bArr[0] & 255);
                setServiceProviderName(IccUtils.adnStringFieldToString(bArr, 1, bArr.length - 1));
                String serviceProviderName = getServiceProviderName();
                if (serviceProviderName == null || serviceProviderName.length() == 0) {
                    this.mSpnState = GetSpnFsmState.READ_SPN_CPHS;
                } else {
                    String strOnOperatorNumericOrNameSet = MiuiUiccControllerStub.get(this.mParentApp.getPhoneId()).onOperatorNumericOrNameSet(this.mParentApp.getPhoneId(), "gsm.sim.operator.alpha", serviceProviderName);
                    log("Load EF_SPN: " + strOnOperatorNumericOrNameSet + " carrierNameDisplayCondition: " + this.mCarrierNameDisplayCondition);
                    this.mTelephonyManager.setSimOperatorNameForPhone(this.mParentApp.getPhoneId(), MiuiUiccControllerStub.get(this.mParentApp.getPhoneId()).onOperatorNumericOrNameSet(this.mParentApp.getPhoneId(), "gsm.sim.operator.alpha", strOnOperatorNumericOrNameSet));
                    this.mSpnState = GetSpnFsmState.IDLE;
                }
            } else {
                this.mSpnState = GetSpnFsmState.READ_SPN_CPHS;
            }
            if (this.mSpnState == GetSpnFsmState.READ_SPN_CPHS) {
                this.mFh.loadEFTransparent(IccConstants.EF_SPN_CPHS, obtainMessage(12));
                this.mRecordsToLoad++;
                this.mCarrierNameDisplayCondition = 0;
                return;
            }
            return;
        }
        if (iOrdinal == 3) {
            if (asyncResult != null && asyncResult.exception == null) {
                byte[] bArr2 = (byte[]) asyncResult.result;
                setServiceProviderName(IccUtils.adnStringFieldToString(bArr2, 0, bArr2.length));
                String serviceProviderName2 = getServiceProviderName();
                if (serviceProviderName2 == null || serviceProviderName2.length() == 0) {
                    this.mSpnState = GetSpnFsmState.READ_SPN_SHORT_CPHS;
                } else {
                    this.mCarrierNameDisplayCondition = 0;
                    log("Load EF_SPN_CPHS: ".concat(serviceProviderName2));
                    this.mTelephonyManager.setSimOperatorNameForPhone(this.mParentApp.getPhoneId(), MiuiUiccControllerStub.get(this.mParentApp.getPhoneId()).onOperatorNumericOrNameSet(this.mParentApp.getPhoneId(), "gsm.sim.operator.alpha", serviceProviderName2));
                    this.mSpnState = GetSpnFsmState.IDLE;
                }
            } else {
                this.mSpnState = GetSpnFsmState.READ_SPN_SHORT_CPHS;
            }
            if (this.mSpnState == GetSpnFsmState.READ_SPN_SHORT_CPHS) {
                this.mFh.loadEFTransparent(IccConstants.EF_SPN_SHORT_CPHS, obtainMessage(12));
                this.mRecordsToLoad++;
                return;
            }
            return;
        }
        if (iOrdinal == 4) {
            if (asyncResult != null && asyncResult.exception == null) {
                byte[] bArr3 = (byte[]) asyncResult.result;
                setServiceProviderName(IccUtils.adnStringFieldToString(bArr3, 0, bArr3.length));
                String serviceProviderName3 = getServiceProviderName();
                if (serviceProviderName3 == null || serviceProviderName3.length() == 0) {
                    log("No SPN loaded in either CHPS or 3GPP");
                } else {
                    this.mCarrierNameDisplayCondition = 0;
                    String strOnOperatorNumericOrNameSet2 = MiuiUiccControllerStub.get(this.mParentApp.getPhoneId()).onOperatorNumericOrNameSet(this.mParentApp.getPhoneId(), "gsm.sim.operator.alpha", serviceProviderName3);
                    log("Load EF_SPN_SHORT_CPHS: " + strOnOperatorNumericOrNameSet2);
                    this.mTelephonyManager.setSimOperatorNameForPhone(this.mParentApp.getPhoneId(), MiuiUiccControllerStub.get(this.mParentApp.getPhoneId()).onOperatorNumericOrNameSet(this.mParentApp.getPhoneId(), "gsm.sim.operator.alpha", strOnOperatorNumericOrNameSet2));
                }
            } else {
                setServiceProviderName(null);
                log("No SPN loaded in either CHPS or 3GPP");
            }
            this.mSpnState = GetSpnFsmState.IDLE;
            return;
        }
        this.mSpnState = GetSpnFsmState.IDLE;
    }

    protected void parseEfSpdi(byte[] bArr) {
        byte[] data;
        SimTlv simTlv = new SimTlv(bArr, 0, bArr.length);
        while (true) {
            if (!simTlv.isValidObject()) {
                data = null;
                break;
            }
            if (simTlv.getTag() == 163) {
                simTlv = new SimTlv(simTlv.getData(), 0, simTlv.getData().length);
            }
            if (simTlv.getTag() != 128) {
                simTlv.nextObject();
            } else {
                data = simTlv.getData();
                break;
            }
        }
        if (data == null) {
            return;
        }
        ArrayList arrayList = new ArrayList(data.length / 3);
        for (int i = 0; i + 2 < data.length; i += 3) {
            String strBcdPlmnToString = IccUtils.bcdPlmnToString(data, i);
            if (!TextUtils.isEmpty(strBcdPlmnToString)) {
                arrayList.add(strBcdPlmnToString);
            }
        }
        log("parseEfSpdi: " + arrayList);
        this.mSpdi = (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    private void parseEfPnn(ArrayList arrayList) {
        if (arrayList == null) {
            return;
        }
        int size = arrayList.size();
        ArrayList arrayList2 = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            byte[] bArr = (byte[]) arrayList.get(i);
            SimTlv simTlv = new SimTlv(bArr, 0, bArr.length);
            String strNetworkNameToString = null;
            String strNetworkNameToString2 = null;
            while (simTlv.isValidObject()) {
                if (simTlv.getData() != null && simTlv.getData().length != 0) {
                    int tag = simTlv.getTag();
                    if (tag == 67) {
                        strNetworkNameToString = IccUtils.networkNameToString(simTlv.getData(), 0, simTlv.getData().length);
                    } else if (tag == 69) {
                        strNetworkNameToString2 = IccUtils.networkNameToString(simTlv.getData(), 0, simTlv.getData().length);
                    }
                }
                simTlv.nextObject();
            }
            arrayList2.add(new IccRecords.PlmnNetworkName(strNetworkNameToString, strNetworkNameToString2));
        }
        logi("parseEfPnn: " + arrayList2);
        IccRecords.PlmnNetworkName[] plmnNetworkNameArr = (IccRecords.PlmnNetworkName[]) arrayList2.toArray(new IccRecords.PlmnNetworkName[0]);
        this.mPnns = plmnNetworkNameArr;
        if (plmnNetworkNameArr.length > 0) {
            this.mPnnHomeName = plmnNetworkNameArr[0].getName();
        }
    }

    protected void parseEfOpl(ArrayList<byte[]> arrayList) {
        if (arrayList == null) {
            return;
        }
        int size = arrayList.size();
        ArrayList arrayList2 = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            byte[] bArr = arrayList.get(i);
            if (bArr.length < 8) {
                loge("Invalid length for OPL record " + IccUtils.bytesToHexString(bArr));
            } else {
                String strBcdPlmnToString = IccUtils.bcdPlmnToString(bArr, 0);
                if (strBcdPlmnToString.length() < 5) {
                    loge("Invalid length for decoded PLMN ".concat(strBcdPlmnToString));
                } else {
                    arrayList2.add(new IccRecords.OperatorPlmnInfo(strBcdPlmnToString, IccUtils.bytesToInt(bArr, 3, 2), IccUtils.bytesToInt(bArr, 5, 2), IccUtils.bytesToInt(bArr, 7, 1)));
                }
            }
        }
        logi("parseEfOpl: " + arrayList2);
        this.mOpl = (IccRecords.OperatorPlmnInfo[]) arrayList2.toArray(new IccRecords.OperatorPlmnInfo[0]);
    }

    protected String[] parseBcdPlmnList(byte[] bArr, String str) {
        log("Received " + str + " PLMNs, raw=" + IccUtils.bytesToHexString(bArr));
        if (bArr.length == 0 || bArr.length % 3 != 0) {
            loge("Received invalid " + str + " PLMN list");
            return null;
        }
        int length = bArr.length / 3;
        String[] strArr = new String[length];
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            String strBcdPlmnToString = IccUtils.bcdPlmnToString(bArr, i2 * 3);
            strArr[i] = strBcdPlmnToString;
            if (!TextUtils.isEmpty(strBcdPlmnToString)) {
                i++;
            }
        }
        String[] strArr2 = (String[]) Arrays.copyOf(strArr, i);
        if (VDBG) {
            logv(str + " PLMNs: " + Arrays.toString(strArr2));
        }
        return strArr2;
    }

    private void parseEfIari(List list) {
        int size = list.size();
        this.mUsimIari = new String[size];
        for (int i = 0; i < size; i++) {
            byte[] bArr = (byte[]) list.get(i);
            SimTlv simTlv = new SimTlv(bArr, 0, bArr.length);
            int tag = simTlv.getTag();
            String[] strArr = this.mUsimIari;
            if (tag == 128) {
                strArr[i] = new String(simTlv.getData(), StandardCharsets.UTF_8);
            } else {
                strArr[i] = null;
            }
        }
        if (VDBG) {
            logv("IARIs: " + Arrays.toString(this.mUsimIari));
        }
    }

    protected boolean isCphsMailboxEnabled() {
        byte[] bArr = this.mCphsInfo;
        return bArr != null && (bArr[1] & 48) == 48;
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    protected void log(String str) {
        try {
            if (this.mParentApp != null) {
                Rlog.d(LOG_TAG, "[SIMRecords-" + this.mParentApp.getPhoneId() + "] " + str);
                return;
            }
            Rlog.d(LOG_TAG, "[SIMRecords] " + str);
        } catch (Exception unused) {
            Rlog.d(LOG_TAG, "[SIMRecords] " + str);
        }
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    protected void loge(String str) {
        if (this.mParentApp != null) {
            Rlog.e(LOG_TAG, "[SIMRecords-" + this.mParentApp.getPhoneId() + "] " + str);
            return;
        }
        Rlog.e(LOG_TAG, "[SIMRecords] " + str);
    }

    protected void logw(String str, Throwable th) {
        if (this.mParentApp != null) {
            Rlog.w(LOG_TAG, "[SIMRecords-" + this.mParentApp.getPhoneId() + "] " + str, th);
            return;
        }
        Rlog.w(LOG_TAG, "[SIMRecords] " + str, th);
    }

    protected void logv(String str) {
        if (this.mParentApp != null) {
            Rlog.v(LOG_TAG, "[SIMRecords-" + this.mParentApp.getPhoneId() + "] " + str);
            return;
        }
        Rlog.v(LOG_TAG, "[SIMRecords] " + str);
    }

    protected void logi(String str) {
        if (this.mParentApp != null) {
            Rlog.i(LOG_TAG, "[SIMRecords-" + this.mParentApp.getPhoneId() + "] " + str);
            return;
        }
        Rlog.i(LOG_TAG, "[SIMRecords] " + str);
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public boolean isCspPlmnEnabled() {
        return this.mCspPlmnEnabled;
    }

    protected void handleEfCspData(byte[] bArr) {
        int length = bArr.length / 2;
        this.mCspPlmnEnabled = true;
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            if (bArr[i2] == -64) {
                StringBuilder sb = new StringBuilder("[CSP] found ValueAddedServicesGroup, value ");
                int i3 = i2 + 1;
                sb.append((int) bArr[i3]);
                log(sb.toString());
                if ((bArr[i3] & 128) == 128) {
                    this.mCspPlmnEnabled = true;
                    return;
                }
                this.mCspPlmnEnabled = false;
                log("[CSP] Set Automatic Network Selection");
                this.mNetworkSelectionModeAutomaticRegistrants.notifyRegistrants();
                return;
            }
        }
        log("[CSP] Value Added Service Group (0xC0), not found!");
    }

    public void setMailboxIndex(int i) {
        this.mMailboxIndex = i;
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("SIMRecords: " + this);
        printWriter.println(" extends:");
        super.dump(fileDescriptor, printWriter, strArr);
        printWriter.println(" mVmConfig=" + this.mVmConfig);
        printWriter.println(" mCallForwardingStatus=" + this.mCallForwardingStatus);
        printWriter.println(" mSpnState=" + this.mSpnState);
        printWriter.println(" mCphsInfo=" + IccUtils.bytesToHexString(this.mCphsInfo));
        printWriter.println(" mCspPlmnEnabled=" + this.mCspPlmnEnabled);
        printWriter.println(" mEfMWIS[]=" + Arrays.toString(this.mEfMWIS));
        printWriter.println(" mEfCPHS_MWI[]=" + Arrays.toString(this.mEfCPHS_MWI));
        printWriter.println(" mEfCff[]=" + Arrays.toString(this.mEfCff));
        printWriter.println(" mEfCfis[]=" + Arrays.toString(this.mEfCfis));
        printWriter.println(" mCarrierNameDisplayCondition=" + this.mCarrierNameDisplayCondition);
        printWriter.println(" mSpdi[]=" + Arrays.toString(this.mSpdi));
        printWriter.println(" mUsimServiceTable=" + this.mUsimServiceTable);
        printWriter.println(" mGid1=" + this.mGid1);
        if (this.mCarrierTestOverride.isInTestMode()) {
            printWriter.println(" mFakeGid1=" + this.mCarrierTestOverride.getFakeGid1());
        }
        printWriter.println(" mGid2=" + this.mGid2);
        if (this.mCarrierTestOverride.isInTestMode()) {
            printWriter.println(" mFakeGid2=" + this.mCarrierTestOverride.getFakeGid2());
        }
        printWriter.println(" mPnnHomeName=" + this.mPnnHomeName);
        if (this.mCarrierTestOverride.isInTestMode()) {
            printWriter.println(" mFakePnnHomeName=" + this.mCarrierTestOverride.getFakePnnHomeName());
        }
        printWriter.println(" mPlmnActRecords[]=" + Arrays.toString(this.mPlmnActRecords));
        printWriter.println(" mOplmnActRecords[]=" + Arrays.toString(this.mOplmnActRecords));
        printWriter.println(" mHplmnActRecords[]=" + Arrays.toString(this.mHplmnActRecords));
        printWriter.println(" mFplmns[]=" + Arrays.toString(this.mFplmns));
        printWriter.println(" mEhplmns[]=" + Arrays.toString(this.mEhplmns));
        printWriter.println(" mPsismsc=" + this.mPsiSmsc);
        printWriter.println(" mUsimIari=" + Arrays.toString(this.mUsimIari));
        printWriter.println(" TPMR=" + getSmssTpmrValue());
        printWriter.flush();
    }

    protected void handleMBIEvent() {
        new AdnRecordLoader(this.mFh).loadFromEF(IccConstants.EF_MAILBOX_CPHS, IccConstants.EF_EXT1, 1, obtainMessage(11));
    }

    protected void loadCfIfFile() {
        this.mFh.loadEFLinearFixed(IccConstants.EF_CFIS, 1, obtainMessage(32));
        this.mRecordsToLoad++;
    }

    protected void loadCffFile() {
        this.mFh.loadEFTransparent(IccConstants.EF_CFF_CPHS, obtainMessage(24));
        this.mRecordsToLoad++;
    }

    private boolean isNtnChipVendor_54() {
        boolean z = SystemProperties.getInt("ro.vendor.radio.ntn_chip_type", 0) == 1;
        log("is Ntn Chip 54-Vendor : " + z);
        return z;
    }
}
