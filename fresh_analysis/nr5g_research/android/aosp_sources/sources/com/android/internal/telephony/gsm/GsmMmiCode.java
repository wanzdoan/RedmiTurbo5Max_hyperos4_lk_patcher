package com.android.internal.telephony.gsm;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.ResultReceiver;
import android.os.SystemProperties;
import android.telephony.CarrierConfigManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsMessage;
import android.text.BidiFormatter;
import android.text.SpannableStringBuilder;
import android.text.TextDirectionHeuristics;
import android.text.TextUtils;
import com.android.internal.telephony.CallForwardInfo;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.MiuiCallControllerStub;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.Phone$$ExternalSyntheticBUOutline1;
import com.android.internal.telephony.TelephonyCommonUtilsStub;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.util.ArrayUtils;
import com.android.telephony.Rlog;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public class GsmMmiCode extends Handler implements MmiCode {
    protected static final String ACTION_ACTIVATE = "*";
    protected static final String ACTION_DEACTIVATE = "#";
    protected static final String ACTION_ERASURE = "##";
    protected static final String ACTION_INTERROGATE = "*#";
    protected static final String ACTION_REGISTER = "**";
    protected static final char END_OF_USSD_COMMAND = '#';
    protected static final int EVENT_GET_CLIR_COMPLETE = 2;
    protected static final int EVENT_QUERY_CF_COMPLETE = 3;
    protected static final int EVENT_QUERY_COMPLETE = 5;
    protected static final int EVENT_SET_CFF_COMPLETE = 6;
    protected static final int EVENT_SET_COMPLETE = 1;
    protected static final int EVENT_USSD_CANCEL_COMPLETE = 7;
    protected static final int EVENT_USSD_COMPLETE = 4;
    protected static final int MATCH_GROUP_ACTION = 2;
    protected static final int MATCH_GROUP_DIALING_NUMBER = 12;
    protected static final int MATCH_GROUP_PWD_CONFIRM = 11;
    protected static final int MATCH_GROUP_SERVICE_CODE = 3;
    protected static final int MATCH_GROUP_SIA = 5;
    protected static final int MATCH_GROUP_SIB = 7;
    protected static final int MATCH_GROUP_SIC = 9;
    protected static final int MAX_LENGTH_SHORT_CODE = 2;
    protected static final String SC_BAIC = "35";
    protected static final String SC_BAICr = "351";
    protected static final String SC_BAOC = "33";
    protected static final String SC_BAOIC = "331";
    protected static final String SC_BAOICxH = "332";
    protected static final String SC_BA_ALL = "330";
    protected static final String SC_BA_MO = "333";
    protected static final String SC_BA_MT = "353";
    protected static final String SC_CFB = "67";
    protected static final String SC_CFNR = "62";
    protected static final String SC_CFNRy = "61";
    protected static final String SC_CFU = "21";
    protected static final String SC_CF_All = "002";
    protected static final String SC_CF_All_Conditional = "004";
    protected static final String SC_CLIP = "30";
    protected static final String SC_CLIR = "31";
    protected static final String SC_PIN = "04";
    protected static final String SC_PIN2 = "042";
    protected static final String SC_PUK = "05";
    protected static final String SC_PUK2 = "052";
    protected static final String SC_PWD = "03";
    protected static final String SC_WAIT = "43";
    protected static String[] sTwoDigitNumberPattern;
    protected String mAction;
    protected ResultReceiver mCallbackReceiver;
    protected Context mContext;
    public String mDialingNumber;
    protected IccRecords mIccRecords;
    protected boolean mIsCallFwdReg;
    protected boolean mIsNetworkInitiatedUSSD;
    protected boolean mIsPendingUSSD;
    protected boolean mIsSsInfo;
    protected boolean mIsUssdRequest;
    protected CharSequence mMessage;
    protected GsmCdmaPhone mPhone;
    protected String mPoundString;
    protected String mPwd;
    protected String mSc;
    protected String mSia;
    protected String mSib;
    protected String mSic;
    protected MmiCode.State mState;
    protected UiccCardApplication mUiccApplication;
    protected static Pattern sPatternSuppService = Pattern.compile("((\\*|#|\\*#|\\*\\*|##)(\\d{2,3})(\\*([^*#]*)(\\*([^*#]*)(\\*([^*#]*)(\\*([^*#]*))?)?)?)?#)(.*)");
    static Pattern sFac = Pattern.compile("^\\#\\d+\\#[^*]+\\*$");

    public static boolean isVoiceUnconditionalForwarding(int i, int i2) {
        if (i == 0 || i == 4) {
            return (i2 & 1) != 0 || i2 == 0;
        }
        return false;
    }

    protected boolean isExtensionCode(String str) {
        return false;
    }

    protected boolean isServiceClassVoiceorNone(int i) {
        return (i & 1) != 0 || i == 0;
    }

    protected void processExtensionCode() {
    }

    public static GsmMmiCode newFromDialString(String str, GsmCdmaPhone gsmCdmaPhone, UiccCardApplication uiccCardApplication) {
        return newFromDialString(str, gsmCdmaPhone, uiccCardApplication, null);
    }

    public static GsmMmiCode newFromDialString(String str, GsmCdmaPhone gsmCdmaPhone, UiccCardApplication uiccCardApplication, ResultReceiver resultReceiver) {
        GsmMmiCode gsmMmiCodeCreate = null;
        if (!shouldCreateMmiCode(str)) {
            return null;
        }
        if (MiuiCallControllerStub.get(gsmCdmaPhone.getPhoneId()).isVodafoneFACs(gsmCdmaPhone, str)) {
            Rlog.d("GsmMmiCode", "dialString is FACs, GsmMmiCode return null");
            return null;
        }
        if ((gsmCdmaPhone.getServiceState().getVoiceRoaming() && gsmCdmaPhone.supportsConversionOfCdmaCallerIdMmiCodesWhileRoaming()) || (isEmergencyNumber(gsmCdmaPhone, str) && isCarrierSupportCallerIdVerticalServiceCodes(gsmCdmaPhone))) {
            str = convertCdmaMmiCodesTo3gppMmiCodes(str);
        }
        Matcher matcher = sPatternSuppService.matcher(str);
        if (matcher.matches()) {
            GsmMmiCode gsmMmiCodeCreate2 = create(gsmCdmaPhone, uiccCardApplication);
            gsmMmiCodeCreate2.mPoundString = makeEmptyNull(matcher.group(1));
            gsmMmiCodeCreate2.mAction = makeEmptyNull(matcher.group(2));
            gsmMmiCodeCreate2.mSc = makeEmptyNull(matcher.group(3));
            gsmMmiCodeCreate2.mSia = makeEmptyNull(matcher.group(5));
            gsmMmiCodeCreate2.mSib = makeEmptyNull(matcher.group(7));
            gsmMmiCodeCreate2.mSic = makeEmptyNull(matcher.group(9));
            gsmMmiCodeCreate2.mPwd = makeEmptyNull(matcher.group(11));
            String strMakeEmptyNull = makeEmptyNull(matcher.group(12));
            gsmMmiCodeCreate2.mDialingNumber = strMakeEmptyNull;
            if (strMakeEmptyNull != null && strMakeEmptyNull.endsWith(ACTION_DEACTIVATE) && str.endsWith(ACTION_DEACTIVATE)) {
                gsmMmiCodeCreate = create(gsmCdmaPhone, uiccCardApplication);
                gsmMmiCodeCreate.mPoundString = str;
            } else if (!gsmMmiCodeCreate2.isFacToDial(str)) {
                gsmMmiCodeCreate = gsmMmiCodeCreate2;
            }
        } else if (str.endsWith(ACTION_DEACTIVATE)) {
            gsmMmiCodeCreate = create(gsmCdmaPhone, uiccCardApplication);
            gsmMmiCodeCreate.mPoundString = str;
        } else if (!isTwoDigitShortCode(gsmCdmaPhone.getContext(), gsmCdmaPhone.getSubId(), str) && isShortCode(str, gsmCdmaPhone)) {
            gsmMmiCodeCreate = create(gsmCdmaPhone, uiccCardApplication);
            gsmMmiCodeCreate.mDialingNumber = str;
        }
        if (gsmMmiCodeCreate != null) {
            gsmMmiCodeCreate.mCallbackReceiver = resultReceiver;
        }
        return gsmMmiCodeCreate;
    }

    protected static String convertCdmaMmiCodesTo3gppMmiCodes(String str) {
        Matcher matcher = MmiCode.sPatternCdmaMmiCodeWhileRoaming.matcher(str);
        if (!matcher.matches()) {
            return str;
        }
        String strMakeEmptyNull = makeEmptyNull(matcher.group(1));
        String strGroup = matcher.group(2);
        String strMakeEmptyNull2 = makeEmptyNull(matcher.group(3));
        if (strMakeEmptyNull.equals(SC_CFB) && strMakeEmptyNull2 != null) {
            return "#31#" + strGroup + strMakeEmptyNull2;
        }
        if (!strMakeEmptyNull.equals("82") || strMakeEmptyNull2 == null) {
            return str;
        }
        return "*31#" + strGroup + strMakeEmptyNull2;
    }

    public static GsmMmiCode newNetworkInitiatedUssd(String str, boolean z, GsmCdmaPhone gsmCdmaPhone, UiccCardApplication uiccCardApplication) {
        GsmMmiCode gsmMmiCodeCreate = create(gsmCdmaPhone, uiccCardApplication);
        gsmMmiCodeCreate.mMessage = str;
        gsmMmiCodeCreate.mIsUssdRequest = z;
        gsmMmiCodeCreate.mIsNetworkInitiatedUSSD = true;
        if (z) {
            gsmMmiCodeCreate.mIsPendingUSSD = true;
            gsmMmiCodeCreate.mState = MmiCode.State.PENDING;
            return gsmMmiCodeCreate;
        }
        gsmMmiCodeCreate.mState = MmiCode.State.COMPLETE;
        return gsmMmiCodeCreate;
    }

    public static GsmMmiCode newFromUssdUserInput(String str, GsmCdmaPhone gsmCdmaPhone, UiccCardApplication uiccCardApplication) {
        GsmMmiCode gsmMmiCodeCreate = create(gsmCdmaPhone, uiccCardApplication);
        gsmMmiCodeCreate.mMessage = str;
        gsmMmiCodeCreate.mState = MmiCode.State.PENDING;
        gsmMmiCodeCreate.mIsPendingUSSD = true;
        return gsmMmiCodeCreate;
    }

    public void processSsData(AsyncResult asyncResult) {
        Rlog.d("GsmMmiCode", "In processSsData");
        this.mIsSsInfo = true;
        try {
            parseSsData((SsData) asyncResult.result);
        } catch (ClassCastException e) {
            Rlog.e("GsmMmiCode", "Class Cast Exception in parsing SS Data : " + e);
        } catch (NullPointerException e2) {
            Rlog.e("GsmMmiCode", "Null Pointer Exception in parsing SS Data : " + e2);
        }
    }

    protected void parseSsData(SsData ssData) {
        CommandException commandExceptionFromRilErrno = CommandException.fromRilErrno(ssData.result);
        this.mSc = getScStringFromScType(ssData.serviceType);
        this.mAction = getActionStringFromReqType(ssData.requestType);
        Rlog.d("GsmMmiCode", "parseSsData msc = " + this.mSc + ", action = " + this.mAction + ", ex = " + commandExceptionFromRilErrno);
        int i = AnonymousClass1.$SwitchMap$com$android$internal$telephony$gsm$SsData$RequestType[ssData.requestType.ordinal()];
        if (i == 1 || i == 2 || i == 3 || i == 4) {
            if (ssData.result == 0 && ssData.serviceType.isTypeUnConditional()) {
                SsData.RequestType requestType = ssData.requestType;
                boolean z = (requestType == SsData.RequestType.SS_ACTIVATION || requestType == SsData.RequestType.SS_REGISTRATION) && isServiceClassVoiceorNone(ssData.serviceClass);
                Rlog.d("GsmMmiCode", "setVoiceCallForwardingFlag cffEnabled: " + z);
                this.mPhone.setVoiceCallForwardingFlag(1, z, null);
            }
            onSetComplete(null, new AsyncResult((Object) null, ssData.cfInfo, commandExceptionFromRilErrno));
            return;
        }
        if (i == 5) {
            if (ssData.serviceType.isTypeClir()) {
                Rlog.d("GsmMmiCode", "CLIR INTERROGATION");
                onGetClirComplete(new AsyncResult((Object) null, ssData.ssInfo, commandExceptionFromRilErrno));
                return;
            } else if (ssData.serviceType.isTypeCF()) {
                Rlog.d("GsmMmiCode", "CALL FORWARD INTERROGATION");
                onQueryCfComplete(new AsyncResult((Object) null, ssData.cfInfo, commandExceptionFromRilErrno));
                return;
            } else {
                onQueryComplete(new AsyncResult((Object) null, ssData.ssInfo, commandExceptionFromRilErrno));
                return;
            }
        }
        Rlog.e("GsmMmiCode", "Invaid requestType in SSData : " + ssData.requestType);
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.gsm.GsmMmiCode$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$gsm$SsData$RequestType;
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$gsm$SsData$ServiceType;

        static {
            int[] iArr = new int[SsData.ServiceType.values().length];
            $SwitchMap$com$android$internal$telephony$gsm$SsData$ServiceType = iArr;
            try {
                iArr[SsData.ServiceType.SS_CFU.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$gsm$SsData$ServiceType[SsData.ServiceType.SS_CF_BUSY.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$gsm$SsData$ServiceType[SsData.ServiceType.SS_CF_NO_REPLY.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$gsm$SsData$ServiceType[SsData.ServiceType.SS_CF_NOT_REACHABLE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$gsm$SsData$ServiceType[SsData.ServiceType.SS_CF_ALL.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$gsm$SsData$ServiceType[SsData.ServiceType.SS_CF_ALL_CONDITIONAL.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$gsm$SsData$ServiceType[SsData.ServiceType.SS_CLIP.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$gsm$SsData$ServiceType[SsData.ServiceType.SS_CLIR.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$gsm$SsData$ServiceType[SsData.ServiceType.SS_WAIT.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$gsm$SsData$ServiceType[SsData.ServiceType.SS_BAOC.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$gsm$SsData$ServiceType[SsData.ServiceType.SS_BAOIC.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$gsm$SsData$ServiceType[SsData.ServiceType.SS_BAOIC_EXC_HOME.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$gsm$SsData$ServiceType[SsData.ServiceType.SS_BAIC.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$gsm$SsData$ServiceType[SsData.ServiceType.SS_BAIC_ROAMING.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$gsm$SsData$ServiceType[SsData.ServiceType.SS_ALL_BARRING.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$gsm$SsData$ServiceType[SsData.ServiceType.SS_OUTGOING_BARRING.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$gsm$SsData$ServiceType[SsData.ServiceType.SS_INCOMING_BARRING.ordinal()] = 17;
            } catch (NoSuchFieldError unused17) {
            }
            int[] iArr2 = new int[SsData.RequestType.values().length];
            $SwitchMap$com$android$internal$telephony$gsm$SsData$RequestType = iArr2;
            try {
                iArr2[SsData.RequestType.SS_ACTIVATION.ordinal()] = 1;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$gsm$SsData$RequestType[SsData.RequestType.SS_DEACTIVATION.ordinal()] = 2;
            } catch (NoSuchFieldError unused19) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$gsm$SsData$RequestType[SsData.RequestType.SS_REGISTRATION.ordinal()] = 3;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$gsm$SsData$RequestType[SsData.RequestType.SS_ERASURE.ordinal()] = 4;
            } catch (NoSuchFieldError unused21) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$gsm$SsData$RequestType[SsData.RequestType.SS_INTERROGATION.ordinal()] = 5;
            } catch (NoSuchFieldError unused22) {
            }
        }
    }

    protected String getScStringFromScType(SsData.ServiceType serviceType) {
        switch (AnonymousClass1.$SwitchMap$com$android$internal$telephony$gsm$SsData$ServiceType[serviceType.ordinal()]) {
            case 1:
                return SC_CFU;
            case 2:
                return SC_CFB;
            case 3:
                return SC_CFNRy;
            case 4:
                return SC_CFNR;
            case 5:
                return SC_CF_All;
            case 6:
                return SC_CF_All_Conditional;
            case 7:
                return SC_CLIP;
            case 8:
                return SC_CLIR;
            case 9:
                return SC_WAIT;
            case 10:
                return SC_BAOC;
            case 11:
                return SC_BAOIC;
            case 12:
                return SC_BAOICxH;
            case 13:
                return SC_BAIC;
            case 14:
                return SC_BAICr;
            case 15:
                return SC_BA_ALL;
            case 16:
                return SC_BA_MO;
            case 17:
                return SC_BA_MT;
            default:
                return "";
        }
    }

    protected static String getActionStringFromReqType(SsData.RequestType requestType) {
        int i = AnonymousClass1.$SwitchMap$com$android$internal$telephony$gsm$SsData$RequestType[requestType.ordinal()];
        if (i == 1) {
            return ACTION_ACTIVATE;
        }
        if (i == 2) {
            return ACTION_DEACTIVATE;
        }
        if (i == 3) {
            return ACTION_REGISTER;
        }
        if (i == 4) {
            return ACTION_ERASURE;
        }
        if (i == 5) {
            return ACTION_INTERROGATE;
        }
        return "";
    }

    protected static String makeEmptyNull(String str) {
        if (str == null || str.length() != 0) {
            return str;
        }
        return null;
    }

    protected static boolean isEmptyOrNull(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    protected static int scToCallForwardReason(String str) {
        if (str == null) {
            Phone$$ExternalSyntheticBUOutline1.m("invalid call forward sc");
            return 0;
        }
        if (str.equals(SC_CF_All)) {
            return 4;
        }
        if (str.equals(SC_CFU)) {
            return 0;
        }
        if (str.equals(SC_CFB)) {
            return 1;
        }
        if (str.equals(SC_CFNR)) {
            return 3;
        }
        if (str.equals(SC_CFNRy)) {
            return 2;
        }
        if (str.equals(SC_CF_All_Conditional)) {
            return 5;
        }
        Phone$$ExternalSyntheticBUOutline1.m("invalid call forward sc");
        return 0;
    }

    public static SsData.ServiceType cfReasonToServiceType(int i) {
        if (i == 0) {
            return SsData.ServiceType.SS_CFU;
        }
        if (i == 1) {
            return SsData.ServiceType.SS_CF_BUSY;
        }
        if (i == 2) {
            return SsData.ServiceType.SS_CF_NO_REPLY;
        }
        if (i == 3) {
            return SsData.ServiceType.SS_CF_NOT_REACHABLE;
        }
        if (i == 4) {
            return SsData.ServiceType.SS_CF_ALL;
        }
        if (i != 5) {
            return null;
        }
        return SsData.ServiceType.SS_CF_ALL_CONDITIONAL;
    }

    public static SsData.RequestType cfActionToRequestType(int i) {
        if (i == 0) {
            return SsData.RequestType.SS_DEACTIVATION;
        }
        if (i == 1) {
            return SsData.RequestType.SS_ACTIVATION;
        }
        if (i == 3) {
            return SsData.RequestType.SS_REGISTRATION;
        }
        if (i != 4) {
            return null;
        }
        return SsData.RequestType.SS_ERASURE;
    }

    protected static int siToServiceClass(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        int i = Integer.parseInt(str, 10);
        if (i == 16) {
            return 8;
        }
        if (i == 99) {
            return 64;
        }
        switch (i) {
            case 10:
                return 13;
            case 11:
                return 1;
            case 12:
                return 12;
            case 13:
                return 4;
            default:
                switch (i) {
                    case 19:
                        return 5;
                    case 20:
                        return 48;
                    case 21:
                        return SmsMessage.MAX_USER_DATA_SEPTETS;
                    case 22:
                        return 80;
                    default:
                        switch (i) {
                            case 24:
                                return 16;
                            case 25:
                                return 32;
                            case 26:
                                return 17;
                            default:
                                Phone$$ExternalSyntheticBUOutline1.m("unsupported MMI service code ".concat(str));
                                return 0;
                        }
                }
        }
    }

    protected static int siToTime(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        return Integer.parseInt(str, 10);
    }

    protected static boolean isServiceCodeCallForwarding(String str) {
        if (str != null) {
            return str.equals(SC_CFU) || str.equals(SC_CFB) || str.equals(SC_CFNRy) || str.equals(SC_CFNR) || str.equals(SC_CF_All) || str.equals(SC_CF_All_Conditional);
        }
        return false;
    }

    protected static boolean isServiceCodeCallBarring(String str) {
        String[] stringArray;
        Resources system = Resources.getSystem();
        if (str != null && (stringArray = system.getStringArray(R.array.config_defaultAmbientContextServices)) != null) {
            for (String str2 : stringArray) {
                if (str.equals(str2)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected static String scToBarringFacility(String str) {
        if (str == null) {
            Phone$$ExternalSyntheticBUOutline1.m("invalid call barring sc");
            return null;
        }
        if (str.equals(SC_BAOC)) {
            return CommandsInterface.CB_FACILITY_BAOC;
        }
        if (str.equals(SC_BAOIC)) {
            return CommandsInterface.CB_FACILITY_BAOIC;
        }
        if (str.equals(SC_BAOICxH)) {
            return CommandsInterface.CB_FACILITY_BAOICxH;
        }
        if (str.equals(SC_BAIC)) {
            return CommandsInterface.CB_FACILITY_BAIC;
        }
        if (str.equals(SC_BAICr)) {
            return CommandsInterface.CB_FACILITY_BAICr;
        }
        if (str.equals(SC_BA_ALL)) {
            return CommandsInterface.CB_FACILITY_BA_ALL;
        }
        if (str.equals(SC_BA_MO)) {
            return CommandsInterface.CB_FACILITY_BA_MO;
        }
        if (str.equals(SC_BA_MT)) {
            return CommandsInterface.CB_FACILITY_BA_MT;
        }
        Phone$$ExternalSyntheticBUOutline1.m("invalid call barring sc");
        return null;
    }

    public static SsData.ServiceType cbFacilityToServiceType(String str) {
        str.hashCode();
        switch (str) {
            case "AB":
                return SsData.ServiceType.SS_ALL_BARRING;
            case "AC":
                return SsData.ServiceType.SS_INCOMING_BARRING;
            case "AG":
                return SsData.ServiceType.SS_OUTGOING_BARRING;
            case "AI":
                return SsData.ServiceType.SS_BAIC;
            case "AO":
                return SsData.ServiceType.SS_BAOC;
            case "IR":
                return SsData.ServiceType.SS_BAIC_ROAMING;
            case "OI":
                return SsData.ServiceType.SS_BAOIC;
            case "OX":
                return SsData.ServiceType.SS_BAOIC_EXC_HOME;
            default:
                return null;
        }
    }

    public GsmMmiCode(GsmCdmaPhone gsmCdmaPhone, UiccCardApplication uiccCardApplication) {
        super(gsmCdmaPhone.getHandler().getLooper());
        this.mState = MmiCode.State.PENDING;
        this.mIsSsInfo = false;
        this.mPhone = gsmCdmaPhone;
        this.mContext = gsmCdmaPhone.getContext();
        this.mUiccApplication = uiccCardApplication;
        if (uiccCardApplication != null) {
            this.mIccRecords = uiccCardApplication.getIccRecords();
        }
    }

    @Override // com.android.internal.telephony.MmiCode
    public MmiCode.State getState() {
        return this.mState;
    }

    @Override // com.android.internal.telephony.MmiCode
    public CharSequence getMessage() {
        return this.mMessage;
    }

    @Override // com.android.internal.telephony.MmiCode
    public Phone getPhone() {
        return this.mPhone;
    }

    @Override // com.android.internal.telephony.MmiCode
    public void cancel() {
        MmiCode.State state = this.mState;
        if (state == MmiCode.State.COMPLETE || state == MmiCode.State.FAILED) {
            return;
        }
        this.mState = MmiCode.State.CANCELLED;
        boolean z = this.mIsPendingUSSD;
        GsmCdmaPhone gsmCdmaPhone = this.mPhone;
        if (z) {
            gsmCdmaPhone.mCi.cancelPendingUssd(obtainMessage(7, this));
        } else {
            gsmCdmaPhone.onMMIDone(this);
        }
    }

    @Override // com.android.internal.telephony.MmiCode
    public boolean isCancelable() {
        return this.mIsPendingUSSD;
    }

    @Override // com.android.internal.telephony.MmiCode
    public boolean isNetworkInitiatedUssd() {
        return this.mIsNetworkInitiatedUSSD;
    }

    protected boolean isMMI() {
        return this.mPoundString != null;
    }

    protected boolean isShortCode() {
        String str;
        return this.mPoundString == null && (str = this.mDialingNumber) != null && str.length() <= 2;
    }

    @Override // com.android.internal.telephony.MmiCode
    public String getDialString() {
        return this.mPoundString;
    }

    public static boolean isTwoDigitShortCode(Context context, int i, String str) {
        Rlog.d("GsmMmiCode", "isTwoDigitShortCode");
        if (str != null && str.length() <= 2) {
            if (sTwoDigitNumberPattern == null) {
                sTwoDigitNumberPattern = getTwoDigitNumberPattern(context, i);
            }
            for (String str2 : sTwoDigitNumberPattern) {
                Rlog.d("GsmMmiCode", "Two Digit Number Pattern " + str2);
                if (str.equals(str2)) {
                    Rlog.d("GsmMmiCode", "Two Digit Number Pattern -true");
                    return true;
                }
            }
            Rlog.d("GsmMmiCode", "Two Digit Number Pattern -false");
        }
        return false;
    }

    private static String[] getTwoDigitNumberPattern(Context context, int i) {
        String[] stringArray;
        PersistableBundle configForSubId;
        Rlog.d("GsmMmiCode", "Get two digit number pattern: subId=" + i);
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) context.getSystemService("carrier_config");
        if (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(i)) == null) {
            stringArray = null;
        } else {
            Rlog.d("GsmMmiCode", "Two Digit Number Pattern from carrir config");
            stringArray = configForSubId.getStringArray("mmi_two_digit_number_pattern_string_array");
        }
        return stringArray == null ? new String[0] : stringArray;
    }

    protected static boolean isShortCode(String str, GsmCdmaPhone gsmCdmaPhone) {
        if (str == null || str.length() == 0 || isEmergencyNumber(gsmCdmaPhone, str)) {
            return false;
        }
        return isShortCodeUSSD(str, gsmCdmaPhone);
    }

    protected static boolean isShortCodeUSSD(String str, GsmCdmaPhone gsmCdmaPhone) {
        if (str != null && str.length() <= 2) {
            if (gsmCdmaPhone.isInCall()) {
                return true;
            }
            if (Pattern.compile("[0-9]*").matcher(str).matches() && (str.length() != 2 || str.charAt(0) != '1')) {
                return true;
            }
        }
        return false;
    }

    @Override // com.android.internal.telephony.MmiCode
    public boolean isPinPukCommand() {
        String str = this.mSc;
        if (str != null) {
            return str.equals(SC_PIN) || this.mSc.equals(SC_PIN2) || this.mSc.equals(SC_PUK) || this.mSc.equals(SC_PUK2);
        }
        return false;
    }

    public boolean isTemporaryModeCLIR() {
        String str = this.mSc;
        if (str == null || !str.equals(SC_CLIR) || this.mDialingNumber == null) {
            return false;
        }
        return isActivate() || isDeactivate();
    }

    public static boolean isEmergencyNumber(Phone phone, String str) {
        try {
            return ((TelephonyManager) phone.getContext().getSystemService(TelephonyManager.class)).isEmergencyNumber(str);
        } catch (RuntimeException unused) {
            return false;
        }
    }

    public static boolean isCarrierSupportCallerIdVerticalServiceCodes(Phone phone) {
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) phone.getContext().getSystemService(CarrierConfigManager.class);
        PersistableBundle configForSubId = carrierConfigManager != null ? carrierConfigManager.getConfigForSubId(phone.getSubId()) : null;
        if (configForSubId != null) {
            return configForSubId.getBoolean("carrier_supports_caller_id_vertical_service_codes_bool");
        }
        return false;
    }

    public int getCLIRMode() {
        String str = this.mSc;
        if (str == null || !str.equals(SC_CLIR)) {
            return 0;
        }
        if (isActivate()) {
            return 2;
        }
        return isDeactivate() ? 1 : 0;
    }

    public static SsData.RequestType clirModeToRequestType(int i) {
        if (i == 1) {
            return SsData.RequestType.SS_DEACTIVATION;
        }
        if (i != 2) {
            return null;
        }
        return SsData.RequestType.SS_ACTIVATION;
    }

    private static boolean isFacFormatNumber(String str) {
        return sFac.matcher(str).matches();
    }

    private boolean isFacToDial(String str) {
        PersistableBundle configForSubId;
        if (isFacFormatNumber(str) && (configForSubId = ((CarrierConfigManager) this.mPhone.getContext().getSystemService("carrier_config")).getConfigForSubId(this.mPhone.getSubId())) != null) {
            String[] stringArray = configForSubId.getStringArray("feature_access_codes_string_array");
            if (!ArrayUtils.isEmpty(stringArray)) {
                for (String str2 : stringArray) {
                    if (str2.equals(this.mSc)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected boolean isActivate() {
        String str = this.mAction;
        return str != null && str.equals(ACTION_ACTIVATE);
    }

    protected boolean isDeactivate() {
        String str = this.mAction;
        return str != null && str.equals(ACTION_DEACTIVATE);
    }

    protected boolean isInterrogate() {
        String str = this.mAction;
        return str != null && str.equals(ACTION_INTERROGATE);
    }

    protected boolean isRegister() {
        String str = this.mAction;
        return str != null && str.equals(ACTION_REGISTER);
    }

    protected boolean isErasure() {
        String str = this.mAction;
        return str != null && str.equals(ACTION_ERASURE);
    }

    public boolean isPendingUSSD() {
        return this.mIsPendingUSSD;
    }

    @Override // com.android.internal.telephony.MmiCode
    public boolean isUssdRequest() {
        return this.mIsUssdRequest;
    }

    public boolean isSsInfo() {
        return this.mIsSsInfo;
    }

    @Override // com.android.internal.telephony.MmiCode
    public void processCode() throws CallStateException {
        UiccCardApplication uiccCardApplication;
        String strScToBarringFacility;
        int i;
        try {
            if (MiuiCallControllerStub.get(this.mPhone.getPhoneId()).processCode(this, new Object[0])) {
                return;
            }
            if (isShortCode()) {
                Rlog.d("GsmMmiCode", "processCode: isShortCode");
                sendUssd(this.mDialingNumber);
                return;
            }
            if (this.mDialingNumber != null) {
                throw new RuntimeException("Invalid or Unsupported MMI Code");
            }
            String str = this.mSc;
            if (str != null && isExtensionCode(str)) {
                processExtensionCode();
                return;
            }
            String str2 = this.mSc;
            if (str2 != null && str2.equals(SC_CLIP)) {
                Rlog.d("GsmMmiCode", "processCode: is CLIP");
                if (isInterrogate()) {
                    this.mPhone.mCi.queryCLIP(obtainMessage(5, this));
                    return;
                }
                throw new RuntimeException("Invalid or Unsupported MMI Code");
            }
            String str3 = this.mSc;
            if (str3 != null && str3.equals(SC_CLIR)) {
                Rlog.d("GsmMmiCode", "processCode: is CLIR");
                if (isActivate() && !this.mPhone.isClirActivationAndDeactivationPrevented()) {
                    this.mPhone.mCi.setCLIR(1, obtainMessage(1, this));
                    return;
                }
                if (isDeactivate() && !this.mPhone.isClirActivationAndDeactivationPrevented()) {
                    this.mPhone.mCi.setCLIR(2, obtainMessage(1, this));
                    return;
                } else {
                    if (isInterrogate()) {
                        this.mPhone.mCi.getCLIR(obtainMessage(2, this));
                        return;
                    }
                    throw new RuntimeException("Invalid or Unsupported MMI Code");
                }
            }
            if (isServiceCodeCallForwarding(this.mSc)) {
                Rlog.d("GsmMmiCode", "processCode: is CF");
                String str4 = this.mSia;
                int iSiToServiceClass = siToServiceClass(this.mSib);
                int iScToCallForwardReason = scToCallForwardReason(this.mSc);
                int iUpdateCfnryTime = MiuiCallControllerStub.get(this.mPhone.getPhoneId()).updateCfnryTime(iScToCallForwardReason, this.mPhone, siToTime(this.mSic));
                if (isInterrogate()) {
                    this.mPhone.mCi.queryCallForwardStatus(iScToCallForwardReason, iSiToServiceClass, str4, obtainMessage(3, this));
                    return;
                }
                if (isActivate()) {
                    if (isEmptyOrNull(str4)) {
                        this.mIsCallFwdReg = false;
                        i = 1;
                    } else {
                        this.mIsCallFwdReg = true;
                        i = 3;
                    }
                } else if (isDeactivate()) {
                    i = 0;
                } else if (isRegister()) {
                    i = 3;
                } else {
                    if (!isErasure()) {
                        throw new RuntimeException("invalid action");
                    }
                    i = 4;
                }
                int i2 = (i == 1 || i == 3) ? 1 : 0;
                Rlog.d("GsmMmiCode", "processCode: is CF setCallForward");
                this.mPhone.mCi.setCallForward(i, iScToCallForwardReason, iSiToServiceClass, str4, iUpdateCfnryTime, obtainMessage(6, isVoiceUnconditionalForwarding(iScToCallForwardReason, iSiToServiceClass) ? 1 : 0, i2, this));
                return;
            }
            if (isServiceCodeCallBarring(this.mSc)) {
                String str5 = this.mSia;
                int iSiToServiceClass2 = siToServiceClass(this.mSib);
                String strScToBarringFacility2 = scToBarringFacility(this.mSc);
                if (isInterrogate()) {
                    this.mPhone.mCi.queryFacilityLock(strScToBarringFacility2, str5, iSiToServiceClass2, obtainMessage(5, this));
                    return;
                }
                if (!isActivate() && !isDeactivate()) {
                    throw new RuntimeException("Invalid or Unsupported MMI Code");
                }
                this.mPhone.mCi.setFacilityLock(strScToBarringFacility2, isActivate(), str5, iSiToServiceClass2, obtainMessage(1, this));
                return;
            }
            String str6 = this.mSc;
            if (str6 != null && str6.equals(SC_PWD)) {
                String str7 = this.mSib;
                String str8 = this.mSic;
                if (!isActivate() && !isRegister()) {
                    throw new RuntimeException("Invalid or Unsupported MMI Code");
                }
                this.mAction = ACTION_REGISTER;
                String str9 = this.mSia;
                if (str9 == null) {
                    strScToBarringFacility = CommandsInterface.CB_FACILITY_BA_ALL;
                } else {
                    strScToBarringFacility = scToBarringFacility(str9);
                }
                if (str8.equals(this.mPwd)) {
                    this.mPhone.mCi.changeBarringPassword(strScToBarringFacility, str7, str8, obtainMessage(1, this));
                    return;
                } else {
                    handlePasswordError(R.string.permlab_getTasks);
                    return;
                }
            }
            String str10 = this.mSc;
            if (str10 != null && str10.equals(SC_WAIT)) {
                int iSiToServiceClass3 = siToServiceClass(this.mSia);
                if (!isActivate() && !isDeactivate()) {
                    if (isInterrogate()) {
                        int terminalBasedCallWaitingState = this.mPhone.getTerminalBasedCallWaitingState(true);
                        GsmCdmaPhone gsmCdmaPhone = this.mPhone;
                        if (terminalBasedCallWaitingState != -1) {
                            gsmCdmaPhone.getCallWaiting(obtainMessage(5, this));
                            return;
                        } else {
                            gsmCdmaPhone.mCi.queryCallWaiting(iSiToServiceClass3, obtainMessage(5, this));
                            return;
                        }
                    }
                    throw new RuntimeException("Invalid or Unsupported MMI Code");
                }
                if ((iSiToServiceClass3 == 0 || (iSiToServiceClass3 & 1) == 1) && this.mPhone.getTerminalBasedCallWaitingState(true) != -1) {
                    this.mPhone.setCallWaiting(isActivate(), iSiToServiceClass3, obtainMessage(1, this));
                    return;
                } else {
                    this.mPhone.mCi.setCallWaiting(isActivate(), iSiToServiceClass3, obtainMessage(1, this));
                    return;
                }
            }
            if (isPinPukCommand()) {
                String str11 = this.mSia;
                String str12 = this.mSib;
                int length = str12.length();
                if (isRegister()) {
                    if (!str12.equals(this.mSic)) {
                        handlePasswordError(R.string.permdesc_readCalendar);
                        return;
                    }
                    if (length >= 4 && length <= 8) {
                        if (this.mSc.equals(SC_PIN) && (uiccCardApplication = this.mUiccApplication) != null && uiccCardApplication.getState() == IccCardApplicationStatus.AppState.APPSTATE_PUK) {
                            handlePasswordError(R.string.permdesc_scene_understanding_coarse);
                            return;
                        }
                        if (this.mUiccApplication != null) {
                            Rlog.d("GsmMmiCode", "processCode: process mmi service code using UiccApp sc=" + this.mSc);
                            if (this.mSc.equals(SC_PIN)) {
                                this.mUiccApplication.changeIccLockPassword(str11, str12, obtainMessage(1, this));
                                return;
                            }
                            if (this.mSc.equals(SC_PIN2)) {
                                this.mUiccApplication.changeIccFdnPassword(str11, str12, obtainMessage(1, this));
                                return;
                            }
                            if (this.mSc.equals(SC_PUK)) {
                                this.mUiccApplication.supplyPuk(str11, str12, obtainMessage(1, this));
                                return;
                            } else if (this.mSc.equals(SC_PUK2)) {
                                this.mUiccApplication.supplyPuk2(str11, str12, obtainMessage(1, this));
                                return;
                            } else {
                                throw new RuntimeException("uicc unsupported service code=" + this.mSc);
                            }
                        }
                        throw new RuntimeException("No application mUiccApplicaiton is null");
                    }
                    handlePasswordError(R.string.mediasize_iso_b6);
                    return;
                }
                throw new RuntimeException("Ivalid register/action=" + this.mAction);
            }
            if (this.mPoundString != null) {
                if (this.mContext.getResources().getBoolean(R.bool.config_allowTheaterModeWakeFromUnplug)) {
                    if (getIntCarrierConfig("carrier_ussd_method_int") != 3) {
                        sendUssd(this.mPoundString);
                        return;
                    }
                    throw new RuntimeException("The USSD request is not allowed over CS");
                }
                sendUssd(this.mPoundString);
                return;
            }
            Rlog.d("GsmMmiCode", "processCode: Invalid or Unsupported MMI Code");
            throw new RuntimeException("Invalid or Unsupported MMI Code");
        } catch (RuntimeException e) {
            this.mState = MmiCode.State.FAILED;
            this.mMessage = this.mContext.getText(R.string.permdesc_readPhoneState);
            Rlog.d("GsmMmiCode", "processCode: RuntimeException=" + e);
            this.mPhone.onMMIDone(this);
        }
    }

    protected void handlePasswordError(int i) {
        this.mState = MmiCode.State.FAILED;
        StringBuilder sb = new StringBuilder(getScString());
        sb.append("\n");
        sb.append(this.mContext.getText(i));
        this.mMessage = sb;
        this.mPhone.onMMIDone(this);
    }

    public void onUssdFinished(String str, boolean z) {
        if (this.mState == MmiCode.State.PENDING) {
            if (TextUtils.isEmpty(str)) {
                Rlog.d("GsmMmiCode", "onUssdFinished: no network provided message; using default.");
                this.mMessage = this.mContext.getText(R.string.permdesc_readPhoneNumbers);
            } else {
                this.mMessage = str;
            }
            this.mIsUssdRequest = z;
            if (!z) {
                this.mState = MmiCode.State.COMPLETE;
            }
            Rlog.d("GsmMmiCode", "onUssdFinished: ussdMessage=" + TelephonyCommonUtilsStub.get().pii(str));
            this.mPhone.onMMIDone(this);
        }
    }

    public void onUssdFinishedError() {
        if (this.mState == MmiCode.State.PENDING) {
            this.mState = MmiCode.State.FAILED;
            if (TextUtils.isEmpty(this.mMessage)) {
                this.mMessage = this.mContext.getText(R.string.permdesc_readPhoneState);
            }
            Rlog.d("GsmMmiCode", "onUssdFinishedError");
            this.mPhone.onMMIDone(this);
        }
    }

    public void onUssdRelease() {
        if (this.mState == MmiCode.State.PENDING) {
            this.mState = MmiCode.State.COMPLETE;
            this.mMessage = null;
            Rlog.d("GsmMmiCode", "onUssdRelease");
            this.mPhone.onMMIDone(this);
        }
    }

    public void sendUssd(String str) {
        this.mIsPendingUSSD = true;
        this.mPhone.mCi.sendUSSD(str, obtainMessage(4, this));
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        switch (message.what) {
            case 1:
                onSetComplete(message, (AsyncResult) message.obj);
                break;
            case 2:
                onGetClirComplete((AsyncResult) message.obj);
                break;
            case 3:
                onQueryCfComplete((AsyncResult) message.obj);
                break;
            case 4:
                AsyncResult asyncResult = (AsyncResult) message.obj;
                if (asyncResult.exception != null) {
                    this.mState = MmiCode.State.FAILED;
                    this.mMessage = getErrorMessage(asyncResult);
                    this.mPhone.onMMIDone(this);
                }
                break;
            case 5:
                onQueryComplete((AsyncResult) message.obj);
                break;
            case 6:
                AsyncResult asyncResult2 = (AsyncResult) message.obj;
                if (asyncResult2.exception == null && message.arg1 == 1) {
                    this.mPhone.setVoiceCallForwardingFlag(1, message.arg2 == 1, this.mDialingNumber);
                }
                onSetComplete(message, asyncResult2);
                break;
            case 7:
                this.mPhone.onMMIDone(this);
                break;
        }
    }

    public CharSequence getErrorMessage(AsyncResult asyncResult) {
        Throwable th = asyncResult.exception;
        if (th instanceof CommandException) {
            CommandException.Error commandError = ((CommandException) th).getCommandError();
            if (commandError == CommandException.Error.FDN_CHECK_FAILURE) {
                Rlog.i("GsmMmiCode", "FDN_CHECK_FAILURE");
                return this.mContext.getText(R.string.permdesc_readSyncStats);
            }
            if (commandError == CommandException.Error.USSD_MODIFIED_TO_DIAL) {
                Rlog.i("GsmMmiCode", "USSD_MODIFIED_TO_DIAL");
                return this.mContext.getText(17042320);
            }
            if (commandError == CommandException.Error.USSD_MODIFIED_TO_SS) {
                Rlog.i("GsmMmiCode", "USSD_MODIFIED_TO_SS");
                return this.mContext.getText(17042322);
            }
            if (commandError == CommandException.Error.USSD_MODIFIED_TO_USSD) {
                Rlog.i("GsmMmiCode", "USSD_MODIFIED_TO_USSD");
                return this.mContext.getText(17042323);
            }
            if (commandError == CommandException.Error.SS_MODIFIED_TO_DIAL) {
                Rlog.i("GsmMmiCode", "SS_MODIFIED_TO_DIAL");
                return this.mContext.getText(17042316);
            }
            if (commandError == CommandException.Error.SS_MODIFIED_TO_USSD) {
                Rlog.i("GsmMmiCode", "SS_MODIFIED_TO_USSD");
                return this.mContext.getText(17042319);
            }
            if (commandError == CommandException.Error.SS_MODIFIED_TO_SS) {
                Rlog.i("GsmMmiCode", "SS_MODIFIED_TO_SS");
                return this.mContext.getText(17042318);
            }
            if (commandError == CommandException.Error.OEM_ERROR_1) {
                Rlog.i("GsmMmiCode", "OEM_ERROR_1 USSD_MODIFIED_TO_DIAL_VIDEO");
                return this.mContext.getText(17042321);
            }
            if (commandError == CommandException.Error.REQUEST_NOT_SUPPORTED || commandError == CommandException.Error.OPERATION_NOT_ALLOWED) {
                Rlog.i("GsmMmiCode", "REQUEST_NOT_SUPPORTED/OPERATION_NOT_ALLOWED");
                return this.mContext.getResources().getText(R.string.permdesc_readSms);
            }
        }
        return this.mContext.getText(R.string.permdesc_readPhoneState);
    }

    protected CharSequence getScString() {
        String str = this.mSc;
        if (str != null) {
            if (isServiceCodeCallBarring(str)) {
                return this.mContext.getText(R.string.BaMmi);
            }
            if (isServiceCodeCallForwarding(this.mSc)) {
                return this.mContext.getText(R.string.CfMmi);
            }
            if (this.mSc.equals(SC_CLIP)) {
                return this.mContext.getText(R.string.CnipMmi);
            }
            if (this.mSc.equals(SC_CLIR)) {
                return this.mContext.getText(R.string.CnirMmi);
            }
            if (this.mSc.equals(SC_PWD)) {
                return this.mContext.getText(R.string.accessibility_autoclick_double_click);
            }
            if (this.mSc.equals(SC_WAIT)) {
                return this.mContext.getText(R.string.EmergencyCallWarningTitle);
            }
            if (isPinPukCommand()) {
                return this.mContext.getText(R.string.SetupCallDefault);
            }
            return "";
        }
        return "";
    }

    protected void onSetComplete(Message message, AsyncResult asyncResult) {
        StringBuilder sb = new StringBuilder(getScString());
        sb.append("\n");
        Throwable th = asyncResult.exception;
        if (th != null) {
            this.mState = MmiCode.State.FAILED;
            if (th instanceof CommandException) {
                CommandException.Error commandError = ((CommandException) th).getCommandError();
                if (commandError == CommandException.Error.PASSWORD_INCORRECT) {
                    if (isPinPukCommand()) {
                        if (this.mSc.equals(SC_PUK) || this.mSc.equals(SC_PUK2)) {
                            sb.append(this.mContext.getText(R.string.config_customVpnAlwaysOnDisconnectedDialogComponent));
                        } else {
                            sb.append(this.mContext.getText(R.string.config_customResolverActivity));
                        }
                        int i = message.arg1;
                        if (i <= 0) {
                            Rlog.d("GsmMmiCode", "onSetComplete: PUK locked, cancel as lock screen will handle this");
                            this.mState = MmiCode.State.CANCELLED;
                        } else if (i > 0) {
                            Rlog.d("GsmMmiCode", "onSetComplete: attemptsRemaining=" + i);
                            sb.append(this.mContext.getResources().getQuantityString(R.plurals.selected_count, i, Integer.valueOf(i)));
                        }
                    } else {
                        sb.append(this.mContext.getText(R.string.permlab_getTasks));
                    }
                } else if (commandError == CommandException.Error.SIM_PUK2) {
                    sb.append(this.mContext.getText(R.string.config_customResolverActivity));
                    sb.append("\n");
                    sb.append(this.mContext.getText(R.string.permdesc_scene_understanding_fine));
                } else if (commandError == CommandException.Error.REQUEST_NOT_SUPPORTED) {
                    if (this.mSc.equals(SC_PIN)) {
                        sb.append(this.mContext.getText(R.string.global_action_voice_assist));
                    }
                } else if (commandError == CommandException.Error.FDN_CHECK_FAILURE) {
                    Rlog.i("GsmMmiCode", "FDN_CHECK_FAILURE");
                    sb.append(this.mContext.getText(R.string.permdesc_readSyncStats));
                } else if (commandError == CommandException.Error.MODEM_ERR && isServiceCodeCallForwarding(this.mSc) && this.mPhone.getServiceState().getVoiceRoaming() && !this.mPhone.supports3gppCallForwardingWhileRoaming()) {
                    sb.append(this.mContext.getText(R.string.permdesc_readSyncSettings));
                } else {
                    sb.append(getErrorMessage(asyncResult));
                }
            } else {
                sb.append(this.mContext.getText(R.string.permdesc_readPhoneState));
            }
        } else if (isActivate()) {
            this.mState = MmiCode.State.COMPLETE;
            boolean z = this.mIsCallFwdReg;
            Context context = this.mContext;
            if (z) {
                sb.append(context.getText(17042209));
            } else {
                sb.append(context.getText(17042204));
            }
            if (this.mSc.equals(SC_CLIR)) {
                this.mPhone.saveClirSetting(1);
            }
        } else if (isDeactivate()) {
            this.mState = MmiCode.State.COMPLETE;
            sb.append(this.mContext.getText(17042203));
            if (this.mSc.equals(SC_CLIR)) {
                this.mPhone.saveClirSetting(2);
            }
        } else if (isRegister()) {
            this.mState = MmiCode.State.COMPLETE;
            sb.append(this.mContext.getText(17042209));
        } else if (isErasure()) {
            this.mState = MmiCode.State.COMPLETE;
            sb.append(this.mContext.getText(17042206));
        } else {
            this.mState = MmiCode.State.FAILED;
            sb.append(this.mContext.getText(R.string.permdesc_readPhoneState));
        }
        this.mMessage = sb;
        Rlog.d("GsmMmiCode", "onSetComplete mmi=" + this);
        this.mPhone.onMMIDone(this);
    }

    protected void onGetClirComplete(AsyncResult asyncResult) {
        StringBuilder sb = new StringBuilder(getScString());
        sb.append("\n");
        if (asyncResult.exception != null) {
            this.mState = MmiCode.State.FAILED;
            sb.append(getErrorMessage(asyncResult));
        } else {
            int[] iArr = (int[]) asyncResult.result;
            int i = iArr[1];
            if (i == 0) {
                sb.append(this.mContext.getText(17042207));
                this.mState = MmiCode.State.COMPLETE;
            } else if (i == 1) {
                sb.append(this.mContext.getText(R.string.CLIRPermanent));
                this.mState = MmiCode.State.COMPLETE;
            } else if (i == 2) {
                sb.append(this.mContext.getText(R.string.permdesc_readPhoneState));
                this.mState = MmiCode.State.FAILED;
            } else if (i == 3) {
                int i2 = iArr[0];
                if (i2 == 1) {
                    sb.append(this.mContext.getText(R.string.CLIRDefaultOnNextCallOn));
                } else {
                    Context context = this.mContext;
                    if (i2 != 2) {
                        sb.append(context.getText(R.string.CLIRDefaultOnNextCallOn));
                    } else {
                        sb.append(context.getText(R.string.CLIRDefaultOnNextCallOff));
                    }
                }
                this.mState = MmiCode.State.COMPLETE;
            } else if (i == 4) {
                int i3 = iArr[0];
                if (i3 == 1) {
                    sb.append(this.mContext.getText(R.string.CLIRDefaultOffNextCallOn));
                } else {
                    Context context2 = this.mContext;
                    if (i3 != 2) {
                        sb.append(context2.getText(R.string.CLIRDefaultOffNextCallOff));
                    } else {
                        sb.append(context2.getText(R.string.CLIRDefaultOffNextCallOff));
                    }
                }
                this.mState = MmiCode.State.COMPLETE;
            }
        }
        this.mMessage = sb;
        Rlog.d("GsmMmiCode", "onGetClirComplete: mmi=" + this);
        this.mPhone.onMMIDone(this);
    }

    protected CharSequence serviceClassToCFString(int i) {
        if (i == 1) {
            return this.mContext.getText(17042202);
        }
        if (i == 2) {
            return this.mContext.getText(17042194);
        }
        if (i == 4) {
            return this.mContext.getText(17042197);
        }
        if (i == 8) {
            return this.mContext.getText(17042200);
        }
        if (i == 16) {
            return this.mContext.getText(17042196);
        }
        if (i == 32) {
            return this.mContext.getText(17042195);
        }
        if (i == 64) {
            return this.mContext.getText(17042199);
        }
        if (i != 128) {
            return null;
        }
        return this.mContext.getText(17042198);
    }

    protected CharSequence makeCFQueryResultMessage(CallForwardInfo callForwardInfo, int i) {
        CharSequence text;
        String[] strArr = {"{0}", "{1}", "{2}"};
        CharSequence[] charSequenceArr = new CharSequence[3];
        boolean z = callForwardInfo.reason == 2 && callForwardInfo.timeSeconds > 0;
        int i2 = callForwardInfo.status;
        if (i2 == 1) {
            Context context = this.mContext;
            if (z) {
                text = context.getText(R.string.config_extensionFallbackPackageName);
            } else {
                text = context.getText(R.string.config_ethernet_tcp_buffers);
            }
        } else if (i2 == 0 && isEmptyOrNull(callForwardInfo.number)) {
            text = this.mContext.getText(R.string.config_extensionFallbackServiceName);
        } else {
            Context context2 = this.mContext;
            if (z) {
                text = context2.getText(R.string.config_fallbackCredentialManagerDialogComponent);
            } else {
                text = context2.getText(R.string.config_factoryResetPackage);
            }
        }
        charSequenceArr[0] = serviceClassToCFString(callForwardInfo.serviceClass & i);
        charSequenceArr[1] = formatLtr(PhoneNumberUtils.stringFromStringAndTOA(callForwardInfo.number, callForwardInfo.toa));
        charSequenceArr[2] = Integer.toString(callForwardInfo.timeSeconds);
        if (callForwardInfo.reason == 0 && (i & callForwardInfo.serviceClass) == 1) {
            this.mPhone.setVoiceCallForwardingFlag(1, callForwardInfo.status == 1, callForwardInfo.number);
        }
        return TextUtils.replace(text, strArr, charSequenceArr);
    }

    protected String formatLtr(String str) {
        return str == null ? str : BidiFormatter.getInstance().unicodeWrap(str, TextDirectionHeuristics.LTR, true);
    }

    protected void onQueryCfComplete(AsyncResult asyncResult) {
        StringBuilder sb = new StringBuilder(getScString());
        sb.append("\n");
        if (asyncResult.exception != null) {
            this.mState = MmiCode.State.FAILED;
            sb.append(getErrorMessage(asyncResult));
        } else {
            CallForwardInfo[] callForwardInfoArr = (CallForwardInfo[]) asyncResult.result;
            if (callForwardInfoArr == null || callForwardInfoArr.length == 0) {
                sb.append(this.mContext.getText(17042203));
                this.mPhone.setVoiceCallForwardingFlag(1, false, null);
            } else {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                for (int i = 1; i <= 128; i <<= 1) {
                    for (CallForwardInfo callForwardInfo : callForwardInfoArr) {
                        if ((callForwardInfo.serviceClass & i) != 0) {
                            spannableStringBuilder.append(makeCFQueryResultMessage(callForwardInfo, i));
                            spannableStringBuilder.append((CharSequence) "\n");
                        }
                    }
                }
                sb.append((CharSequence) spannableStringBuilder);
            }
            this.mState = MmiCode.State.COMPLETE;
        }
        this.mMessage = sb;
        Rlog.d("GsmMmiCode", "onQueryCfComplete: mmi=" + this);
        this.mPhone.onMMIDone(this);
    }

    protected void onQueryComplete(AsyncResult asyncResult) {
        StringBuilder sb = new StringBuilder(getScString());
        sb.append("\n");
        if (asyncResult.exception != null) {
            this.mState = MmiCode.State.FAILED;
            sb.append(getErrorMessage(asyncResult));
        } else {
            int[] iArr = (int[]) asyncResult.result;
            if (iArr.length != 0) {
                if (iArr[0] == 0) {
                    sb.append(this.mContext.getText(17042203));
                } else if (this.mSc.equals(SC_WAIT)) {
                    sb.append(createQueryCallWaitingResultMessage(iArr[1]));
                } else if (isServiceCodeCallBarring(this.mSc)) {
                    sb.append(createQueryCallBarringResultMessage(iArr[0]));
                } else {
                    int i = iArr[0];
                    Context context = this.mContext;
                    if (i == 1) {
                        sb.append(context.getText(17042204));
                    } else {
                        sb.append(context.getText(R.string.permdesc_readPhoneState));
                    }
                }
            } else {
                sb.append(this.mContext.getText(R.string.permdesc_readPhoneState));
            }
            this.mState = MmiCode.State.COMPLETE;
        }
        this.mMessage = sb;
        Rlog.d("GsmMmiCode", "onQueryComplete: mmi=" + this);
        this.mPhone.onMMIDone(this);
    }

    protected CharSequence createQueryCallWaitingResultMessage(int i) {
        StringBuilder sb = new StringBuilder(this.mContext.getText(17042205));
        for (int i2 = 1; i2 <= 128; i2 <<= 1) {
            int i3 = i2 & i;
            if (i3 != 0) {
                sb.append("\n");
                sb.append(serviceClassToCFString(i3));
            }
        }
        return sb;
    }

    protected CharSequence createQueryCallBarringResultMessage(int i) {
        StringBuilder sb = new StringBuilder(this.mContext.getText(17042205));
        for (int i2 = 1; i2 <= 128; i2 <<= 1) {
            int i3 = i2 & i;
            if (i3 != 0) {
                sb.append("\n");
                sb.append(serviceClassToCFString(i3));
            }
        }
        return sb;
    }

    private int getIntCarrierConfig(String str) {
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService(CarrierConfigManager.class);
        PersistableBundle configForSubId = carrierConfigManager != null ? carrierConfigManager.getConfigForSubId(this.mPhone.getSubId()) : null;
        if (configForSubId != null) {
            return configForSubId.getInt(str);
        }
        return CarrierConfigManager.getDefaultConfig().getInt(str);
    }

    @Override // com.android.internal.telephony.MmiCode
    public ResultReceiver getUssdCallbackReceiver() {
        return this.mCallbackReceiver;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static ArrayList<String> getControlStrings(SsData.RequestType requestType, SsData.ServiceType serviceType) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (requestType != null && serviceType != null) {
            String actionStringFromReqType = getActionStringFromReqType(requestType);
            switch (AnonymousClass1.$SwitchMap$com$android$internal$telephony$gsm$SsData$ServiceType[serviceType.ordinal()]) {
                case 1:
                    arrayList.add(actionStringFromReqType + SC_CFU);
                    arrayList.add(actionStringFromReqType + SC_CF_All);
                    break;
                case 2:
                    arrayList.add(actionStringFromReqType + SC_CFB);
                    arrayList.add(actionStringFromReqType + SC_CF_All_Conditional);
                    arrayList.add(actionStringFromReqType + SC_CF_All);
                    return arrayList;
                case 3:
                    arrayList.add(actionStringFromReqType + SC_CFNRy);
                    arrayList.add(actionStringFromReqType + SC_CF_All_Conditional);
                    arrayList.add(actionStringFromReqType + SC_CF_All);
                    return arrayList;
                case 4:
                    arrayList.add(actionStringFromReqType + SC_CFNR);
                    arrayList.add(actionStringFromReqType + SC_CF_All_Conditional);
                    arrayList.add(actionStringFromReqType + SC_CF_All);
                    return arrayList;
                case 5:
                    arrayList.add(actionStringFromReqType + SC_CF_All);
                    return arrayList;
                case 6:
                    arrayList.add(actionStringFromReqType + SC_CF_All_Conditional);
                    arrayList.add(actionStringFromReqType + SC_CF_All);
                    return arrayList;
                case 7:
                    arrayList.add(actionStringFromReqType + SC_CLIP);
                    return arrayList;
                case 8:
                    arrayList.add(actionStringFromReqType + SC_CLIR);
                    return arrayList;
                case 9:
                    arrayList.add(actionStringFromReqType + SC_WAIT);
                    return arrayList;
                case 10:
                    arrayList.add(actionStringFromReqType + SC_BAOC);
                    arrayList.add(actionStringFromReqType + SC_BA_MO);
                    arrayList.add(actionStringFromReqType + SC_BA_ALL);
                    return arrayList;
                case 11:
                    arrayList.add(actionStringFromReqType + SC_BAOIC);
                    arrayList.add(actionStringFromReqType + SC_BA_MO);
                    arrayList.add(actionStringFromReqType + SC_BA_ALL);
                    return arrayList;
                case 12:
                    arrayList.add(actionStringFromReqType + SC_BAOICxH);
                    arrayList.add(actionStringFromReqType + SC_BA_MO);
                    arrayList.add(actionStringFromReqType + SC_BA_ALL);
                    return arrayList;
                case 13:
                    arrayList.add(actionStringFromReqType + SC_BAIC);
                    arrayList.add(actionStringFromReqType + SC_BA_MT);
                    arrayList.add(actionStringFromReqType + SC_BA_ALL);
                    return arrayList;
                case 14:
                    arrayList.add(actionStringFromReqType + SC_BAICr);
                    arrayList.add(actionStringFromReqType + SC_BA_MT);
                    arrayList.add(actionStringFromReqType + SC_BA_ALL);
                    return arrayList;
                case 15:
                    arrayList.add(actionStringFromReqType + SC_BA_ALL);
                    return arrayList;
                case 16:
                    arrayList.add(actionStringFromReqType + SC_BA_MO);
                    arrayList.add(actionStringFromReqType + SC_BA_ALL);
                    return arrayList;
                case 17:
                    arrayList.add(actionStringFromReqType + SC_BA_MT);
                    arrayList.add(actionStringFromReqType + SC_BA_ALL);
                    return arrayList;
            }
        }
        return arrayList;
    }

    public static ArrayList<String> getControlStringsForPwd(SsData.RequestType requestType, SsData.ServiceType serviceType) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (requestType != null && serviceType != null) {
            ArrayList<String> controlStrings = getControlStrings(SsData.RequestType.SS_ACTIVATION, serviceType);
            String actionStringFromReqType = getActionStringFromReqType(requestType);
            arrayList = new ArrayList<>();
            int size = controlStrings.size();
            int i = 0;
            while (i < size) {
                String str = controlStrings.get(i);
                i++;
                arrayList.add(actionStringFromReqType + SC_PWD + str);
            }
        }
        return arrayList;
    }

    @Override // android.os.Handler
    public String toString() {
        StringBuilder sb = new StringBuilder("GsmMmiCode {");
        sb.append("State=" + getState());
        if (this.mAction != null) {
            sb.append(" action=" + this.mAction);
        }
        if (this.mSc != null) {
            sb.append(" sc=" + this.mSc);
        }
        if (this.mSia != null) {
            sb.append(" sia=" + Rlog.pii("GsmMmiCode", this.mSia));
        }
        if (this.mSib != null) {
            sb.append(" sib=" + Rlog.pii("GsmMmiCode", this.mSib));
        }
        if (this.mSic != null) {
            sb.append(" sic=" + Rlog.pii("GsmMmiCode", this.mSic));
        }
        if (this.mPoundString != null) {
            sb.append(" poundString=" + Rlog.pii("GsmMmiCode", this.mPoundString));
        }
        if (this.mDialingNumber != null) {
            sb.append(" dialingNumber=" + Rlog.pii("GsmMmiCode", this.mDialingNumber));
        }
        if (this.mPwd != null) {
            sb.append(" pwd=" + Rlog.pii("GsmMmiCode", this.mPwd));
        }
        if (this.mCallbackReceiver != null) {
            sb.append(" hasReceiver");
        }
        sb.append("}");
        return sb.toString();
    }

    protected static boolean shouldCreateMmiCode(String str) {
        if (SystemProperties.get("ro.vendor.mtk_telephony_add_on_policy", "0").equals("0")) {
            try {
                Method method = getMtkGsmMmiCodeClass().getMethod("shouldCreateMmiCode", String.class);
                method.setAccessible(true);
                Rlog.d("GsmMmiCode", "invoke shouldCreateMmiCode");
                return ((Boolean) method.invoke(null, str)).booleanValue();
            } catch (Exception e) {
                Rlog.e("GsmMmiCode", "shouldCreateMmiCode exception!" + e);
            }
        }
        return true;
    }

    protected static GsmMmiCode create(GsmCdmaPhone gsmCdmaPhone, UiccCardApplication uiccCardApplication) {
        if (SystemProperties.get("ro.vendor.mtk_telephony_add_on_policy", "0").equals("0")) {
            try {
                Constructor constructor = getMtkGsmMmiCodeClass().getConstructor(GsmCdmaPhone.class, UiccCardApplication.class);
                Rlog.d("GsmMmiCode", "constructor function = " + constructor);
                constructor.setAccessible(true);
                return (GsmMmiCode) constructor.newInstance(gsmCdmaPhone, uiccCardApplication);
            } catch (Exception e) {
                Rlog.e("GsmMmiCode", "create MtkGsmMmiCode exception! Use AOSP instead!" + e);
            }
        }
        return new GsmMmiCode(gsmCdmaPhone, uiccCardApplication);
    }

    private static Class getMtkGsmMmiCodeClass() {
        try {
            return Class.forName("com.mediatek.internal.telephony.gsm.MtkGsmMmiCode", false, ClassLoader.getSystemClassLoader());
        } catch (ClassNotFoundException e) {
            Rlog.e("GsmMmiCode", "Can't find MtkGsmMmiCode!" + e);
            return null;
        }
    }
}
