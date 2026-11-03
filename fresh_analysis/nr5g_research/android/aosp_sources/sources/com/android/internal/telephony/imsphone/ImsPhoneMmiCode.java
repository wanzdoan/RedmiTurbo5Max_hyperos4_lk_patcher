package com.android.internal.telephony.imsphone;

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
import android.telephony.ims.ImsCallForwardInfo;
import android.telephony.ims.ImsSsData;
import android.telephony.ims.ImsSsInfo;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import com.android.ims.ImsException;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.CallForwardInfo;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.HyperTelephonyFactory;
import com.android.internal.telephony.IHyperSsManager;
import com.android.internal.telephony.MiuiCallControllerStub;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.Phone$$ExternalSyntheticBUOutline1;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.internal.telephony.gsm.GsmMmiCode;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.telephony.Rlog;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public class ImsPhoneMmiCode extends Handler implements MmiCode {
    protected static final String ACTION_ACTIVATE = "*";
    protected static final String ACTION_DEACTIVATE = "#";
    protected static final String ACTION_ERASURE = "##";
    protected static final String ACTION_INTERROGATE = "*#";
    protected static final String ACTION_REGISTER = "**";
    protected static final char END_OF_USSD_COMMAND = '#';
    protected static final int EVENT_GET_CLIR_COMPLETE = 6;
    protected static final int EVENT_QUERY_CF_COMPLETE = 1;
    protected static final int EVENT_QUERY_COMPLETE = 3;
    protected static final int EVENT_QUERY_ICB_COMPLETE = 10;
    protected static final int EVENT_SET_CFF_COMPLETE = 4;
    protected static final int EVENT_SET_COMPLETE = 0;
    protected static final int EVENT_SUPP_SVC_QUERY_COMPLETE = 7;
    protected static final int EVENT_USSD_CANCEL_COMPLETE = 5;
    protected static final int EVENT_USSD_COMPLETE = 2;
    protected static final String IcbAnonymousMmi = "Anonymous Incoming Call Barring";
    protected static final String IcbDnMmi = "Specific Incoming Call Barring";
    protected static final int MATCH_GROUP_ACTION = 2;
    protected static final int MATCH_GROUP_DIALING_NUMBER = 12;
    protected static final int MATCH_GROUP_POUND_STRING = 1;
    protected static final int MATCH_GROUP_PWD_CONFIRM = 11;
    protected static final int MATCH_GROUP_SERVICE_CODE = 3;
    protected static final int MATCH_GROUP_SIA = 5;
    protected static final int MATCH_GROUP_SIB = 7;
    protected static final int MATCH_GROUP_SIC = 9;
    protected static final int MAX_LENGTH_SHORT_CODE = 2;
    protected static final int NUM_PRESENTATION_ALLOWED = 0;
    protected static final int NUM_PRESENTATION_RESTRICTED = 1;
    protected static final String SC_BAIC = "35";
    protected static final String SC_BAICa = "157";
    protected static final String SC_BAICr = "351";
    protected static final String SC_BAOC = "33";
    protected static final String SC_BAOIC = "331";
    protected static final String SC_BAOICxH = "332";
    protected static final String SC_BA_ALL = "330";
    protected static final String SC_BA_MO = "333";
    protected static final String SC_BA_MT = "353";
    protected static final String SC_BS_MT = "156";
    protected static final String SC_CFB = "67";
    protected static final String SC_CFNR = "62";
    protected static final String SC_CFNRy = "61";
    protected static final String SC_CFU = "21";
    protected static final String SC_CFUT = "22";
    protected static final String SC_CF_All = "002";
    protected static final String SC_CF_All_Conditional = "004";
    protected static final String SC_CLIP = "30";
    protected static final String SC_CLIR = "31";
    protected static final String SC_CNAP = "300";
    protected static final String SC_COLP = "76";
    protected static final String SC_COLR = "77";
    protected static final String SC_PIN = "04";
    protected static final String SC_PIN2 = "042";
    protected static final String SC_PUK = "05";
    protected static final String SC_PUK2 = "052";
    protected static final String SC_PWD = "03";
    protected static final String SC_WAIT = "43";

    @Deprecated
    public static final String UT_BUNDLE_KEY_CLIR = "queryClir";

    @Deprecated
    public static final String UT_BUNDLE_KEY_SSINFO = "imsSsInfo";
    protected static Pattern sPatternSuppService = Pattern.compile("((\\*|#|\\*#|\\*\\*|##)(\\d{2,3})(\\*([^*#]*)(\\*([^*#]*)(\\*([^*#]*)(\\*([^*#]*))?)?)?)?#)(.*)");
    protected static String[] sTwoDigitNumberPattern;
    protected String mAction;
    protected ResultReceiver mCallbackReceiver;
    protected Context mContext;
    protected String mDialingNumber;
    private final FeatureFlags mFeatureFlags;
    IHyperSsManager mIHyperSsManager;
    protected IccRecords mIccRecords;
    protected boolean mIsCallFwdReg;
    protected boolean mIsNetworkInitiatedUSSD;
    protected boolean mIsPendingUSSD;
    protected boolean mIsSsInfo;
    protected boolean mIsUssdRequest;
    protected CharSequence mMessage;
    protected ImsPhone mPhone;
    protected String mPoundString;
    protected String mPwd;
    protected String mSc;
    protected String mSia;
    protected String mSib;
    protected String mSic;
    protected MmiCode.State mState;

    protected boolean isFacToDial(String str) {
        return false;
    }

    protected boolean isServiceClassVoiceVideoOrNone(int i) {
        return i == 0 || i == 1 || i == 80;
    }

    protected boolean isVoiceUnconditionalForwarding(int i, int i2) {
        return i == 0 || i == 4;
    }

    public static ImsPhoneMmiCode newFromDialString(String str, ImsPhone imsPhone, FeatureFlags featureFlags) {
        return newFromDialString(str, imsPhone, null, featureFlags);
    }

    protected static ImsPhoneMmiCode newFromDialString(String str, ImsPhone imsPhone, ResultReceiver resultReceiver, FeatureFlags featureFlags) {
        ImsPhoneMmiCode imsPhoneMmiCodeCreate = null;
        if (!shouldCreateMmiCode(str)) {
            return null;
        }
        if ((imsPhone.getDefaultPhone().getServiceState().getVoiceRoaming() && imsPhone.getDefaultPhone().supportsConversionOfCdmaCallerIdMmiCodesWhileRoaming()) || (isEmergencyNumber(imsPhone, str) && isCarrierSupportCallerIdVerticalServiceCodes(imsPhone))) {
            str = convertCdmaMmiCodesTo3gppMmiCodes(str);
        }
        Matcher matcher = sPatternSuppService.matcher(str);
        if (matcher.matches()) {
            ImsPhoneMmiCode imsPhoneMmiCodeCreate2 = create(imsPhone, featureFlags);
            imsPhoneMmiCodeCreate2.mPoundString = makeEmptyNull(matcher.group(1));
            imsPhoneMmiCodeCreate2.mAction = makeEmptyNull(matcher.group(2));
            imsPhoneMmiCodeCreate2.mSc = makeEmptyNull(matcher.group(3));
            imsPhoneMmiCodeCreate2.mSia = makeEmptyNull(matcher.group(5));
            imsPhoneMmiCodeCreate2.mSib = makeEmptyNull(matcher.group(7));
            imsPhoneMmiCodeCreate2.mSic = makeEmptyNull(matcher.group(9));
            imsPhoneMmiCodeCreate2.mPwd = makeEmptyNull(matcher.group(11));
            String strMakeEmptyNull = makeEmptyNull(matcher.group(12));
            imsPhoneMmiCodeCreate2.mDialingNumber = strMakeEmptyNull;
            imsPhoneMmiCodeCreate2.mCallbackReceiver = resultReceiver;
            if (strMakeEmptyNull != null && strMakeEmptyNull.endsWith(ACTION_DEACTIVATE) && str.endsWith(ACTION_DEACTIVATE)) {
                imsPhoneMmiCodeCreate = create(imsPhone, featureFlags);
                imsPhoneMmiCodeCreate.mPoundString = str;
            } else if (!imsPhoneMmiCodeCreate2.isFacToDial(str)) {
                imsPhoneMmiCodeCreate = imsPhoneMmiCodeCreate2;
            }
        } else if (str.endsWith(ACTION_DEACTIVATE)) {
            imsPhoneMmiCodeCreate = create(imsPhone, featureFlags);
            imsPhoneMmiCodeCreate.mPoundString = str;
        } else if (!GsmMmiCode.isTwoDigitShortCode(imsPhone.getContext(), imsPhone.getSubId(), str) && isShortCode(str, imsPhone)) {
            imsPhoneMmiCodeCreate = create(imsPhone, featureFlags);
            imsPhoneMmiCodeCreate.mDialingNumber = str;
        }
        return MiuiCallControllerStub.get(imsPhone.getPhoneId()).getMmiCodeForKrOperator(imsPhone, str, imsPhoneMmiCodeCreate, isShortCode(str, imsPhone));
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

    public static ImsPhoneMmiCode newNetworkInitiatedUssd(String str, boolean z, ImsPhone imsPhone, FeatureFlags featureFlags) {
        ImsPhoneMmiCode imsPhoneMmiCodeCreate = create(imsPhone, featureFlags);
        imsPhoneMmiCodeCreate.mMessage = str;
        imsPhoneMmiCodeCreate.mIsUssdRequest = z;
        imsPhoneMmiCodeCreate.mIsNetworkInitiatedUSSD = true;
        if (z) {
            imsPhoneMmiCodeCreate.mIsPendingUSSD = true;
            imsPhoneMmiCodeCreate.mState = MmiCode.State.PENDING;
            return imsPhoneMmiCodeCreate;
        }
        imsPhoneMmiCodeCreate.mState = MmiCode.State.COMPLETE;
        return imsPhoneMmiCodeCreate;
    }

    static ImsPhoneMmiCode newFromUssdUserInput(String str, ImsPhone imsPhone, FeatureFlags featureFlags) {
        ImsPhoneMmiCode imsPhoneMmiCodeCreate = create(imsPhone, featureFlags);
        imsPhoneMmiCodeCreate.mMessage = str;
        imsPhoneMmiCodeCreate.mState = MmiCode.State.PENDING;
        imsPhoneMmiCodeCreate.mIsPendingUSSD = true;
        return imsPhoneMmiCodeCreate;
    }

    protected static String makeEmptyNull(String str) {
        if (str == null || str.length() != 0) {
            return str;
        }
        return null;
    }

    protected static boolean isScMatchesSuppServType(String str) {
        Matcher matcher = sPatternSuppService.matcher(str);
        if (!matcher.matches()) {
            return false;
        }
        String strMakeEmptyNull = makeEmptyNull(matcher.group(3));
        return strMakeEmptyNull.equals(SC_CFUT) || strMakeEmptyNull.equals(SC_BS_MT);
    }

    protected static boolean isEmptyOrNull(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    protected static int scToCallForwardReason(String str) {
        if (str == null) {
            Phone$$ExternalSyntheticBUOutline1.m("invalid call forward sc");
            return 0;
        }
        if (isServiceCodeCallForwardingEx(str)) {
            return scToCallForwardReasonEx(str);
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
            return str.equals(SC_CFU) || str.equals(SC_CFB) || str.equals(SC_CFNRy) || str.equals(SC_CFNR) || str.equals(SC_CF_All) || str.equals(SC_CF_All_Conditional) || isServiceCodeCallForwardingEx(str);
        }
        return false;
    }

    protected static boolean isServiceCodeCallBarring(String str) {
        String[] stringArray;
        Resources system = Resources.getSystem();
        if (str != null && (stringArray = system.getStringArray(R.array.config_defaultCloudSearchServices)) != null) {
            for (String str2 : stringArray) {
                if (str.equals(str2)) {
                    return true;
                }
            }
        }
        return false;
    }

    static boolean isPinPukCommand(String str) {
        if (str != null) {
            return str.equals(SC_PIN) || str.equals(SC_PIN2) || str.equals(SC_PUK) || str.equals(SC_PUK2);
        }
        return false;
    }

    public static boolean isSuppServiceCodes(String str, Phone phone) {
        if (phone != null && phone.getServiceState().getVoiceRoaming() && phone.getDefaultPhone().supportsConversionOfCdmaCallerIdMmiCodesWhileRoaming()) {
            str = convertCdmaMmiCodesTo3gppMmiCodes(str);
        }
        Matcher matcher = sPatternSuppService.matcher(str);
        if (!matcher.matches()) {
            return false;
        }
        String strMakeEmptyNull = makeEmptyNull(matcher.group(3));
        if (isServiceCodeCallForwarding(strMakeEmptyNull) || isServiceCodeCallBarring(strMakeEmptyNull)) {
            return true;
        }
        if (strMakeEmptyNull != null && strMakeEmptyNull.equals(SC_CFUT)) {
            return true;
        }
        if (strMakeEmptyNull != null && strMakeEmptyNull.equals(SC_CLIP)) {
            return true;
        }
        if (strMakeEmptyNull != null && strMakeEmptyNull.equals(SC_CLIR)) {
            return true;
        }
        if (strMakeEmptyNull != null && strMakeEmptyNull.equals(SC_COLP)) {
            return true;
        }
        if (strMakeEmptyNull != null && strMakeEmptyNull.equals(SC_COLR)) {
            return true;
        }
        if (strMakeEmptyNull != null && strMakeEmptyNull.equals(SC_CNAP)) {
            return true;
        }
        if (strMakeEmptyNull != null && strMakeEmptyNull.equals(SC_BS_MT)) {
            return true;
        }
        if (strMakeEmptyNull != null && strMakeEmptyNull.equals(SC_BAICa)) {
            return true;
        }
        if (strMakeEmptyNull == null || !strMakeEmptyNull.equals(SC_PWD)) {
            return (strMakeEmptyNull != null && strMakeEmptyNull.equals(SC_WAIT)) || isPinPukCommand(strMakeEmptyNull);
        }
        return true;
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

    public ImsPhoneMmiCode(ImsPhone imsPhone, FeatureFlags featureFlags) {
        super(imsPhone.getHandler().getLooper());
        this.mState = MmiCode.State.PENDING;
        this.mIsSsInfo = false;
        this.mPhone = imsPhone;
        this.mContext = imsPhone.getContext();
        this.mIccRecords = this.mPhone.mDefaultPhone.getIccRecords();
        this.mFeatureFlags = featureFlags;
        this.mIHyperSsManager = (IHyperSsManager) HyperTelephonyFactory.getInstance().initModule(IHyperSsManager.DEFAULT, new Object[0]);
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
        ImsPhone imsPhone = this.mPhone;
        if (z) {
            imsPhone.cancelUSSD(obtainMessage(5, this));
        } else {
            imsPhone.onMMIDone(this);
        }
    }

    @Override // com.android.internal.telephony.MmiCode
    public boolean isCancelable() {
        return this.mIsPendingUSSD;
    }

    public String getDialingNumber() {
        return this.mDialingNumber;
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

    protected static boolean isShortCode(String str, ImsPhone imsPhone) {
        if (str == null || str.length() == 0 || isEmergencyNumber(imsPhone, str)) {
            return false;
        }
        return isShortCodeUSSD(str, imsPhone);
    }

    protected static boolean isShortCodeUSSD(String str, ImsPhone imsPhone) {
        return (str == null || str.length() > 2 || (!imsPhone.isInCall() && str.length() == 2 && str.charAt(0) == '1')) ? false : true;
    }

    @Override // com.android.internal.telephony.MmiCode
    public boolean isPinPukCommand() {
        return isPinPukCommand(this.mSc);
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

    public boolean isSupportedOverImsPhone() {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        if (isShortCode()) {
            return true;
        }
        if (isServiceCodeCallForwarding(this.mSc) || isServiceCodeCallBarring(this.mSc) || (((str = this.mSc) != null && str.equals(SC_WAIT)) || (((str2 = this.mSc) != null && str2.equals(SC_CLIR)) || (((str3 = this.mSc) != null && str3.equals(SC_CLIP)) || (((str4 = this.mSc) != null && str4.equals(SC_COLR)) || (((str5 = this.mSc) != null && str5.equals(SC_COLP)) || (((str6 = this.mSc) != null && str6.equals(SC_BS_MT)) || ((str7 = this.mSc) != null && str7.equals(SC_BAICa))))))))) {
            try {
                int iSiToServiceClass = siToServiceClass(this.mSib);
                if (iSiToServiceClass != 0 && iSiToServiceClass != 1 && iSiToServiceClass != 80) {
                    return false;
                }
                if (!isServiceCodeCallBarring(this.mSc) || MiuiImsControllerStub.get(this.mPhone.getPhoneId()).isCarrierSupportCbOverIms(this.mPhone.mDefaultPhone)) {
                    return true;
                }
                Rlog.d("ImsPhoneMmiCode", "Call barring is not supported over ims");
                return false;
            } catch (RuntimeException e) {
                Rlog.d("ImsPhoneMmiCode", "Invalid service class " + e);
            }
        } else if (!isPinPukCommand() && (((str8 = this.mSc) == null || (!str8.equals(SC_PWD) && !this.mSc.equals(SC_CLIP) && !this.mSc.equals(SC_CLIR))) && this.mPoundString != null)) {
            return true;
        }
        return false;
    }

    public int callBarAction(String str) {
        if (isActivate()) {
            return 1;
        }
        if (isDeactivate()) {
            return 0;
        }
        if (isRegister()) {
            if (!isEmptyOrNull(str)) {
                return 3;
            }
            Phone$$ExternalSyntheticBUOutline1.m("invalid action");
            return 0;
        }
        if (isErasure()) {
            return 4;
        }
        Phone$$ExternalSyntheticBUOutline1.m("invalid action");
        return 0;
    }

    @Override // com.android.internal.telephony.MmiCode
    public void processCode() throws CallStateException {
        int iSiToTime;
        int i;
        try {
            if (MiuiImsControllerStub.get(this.mPhone.getPhoneId()).processCode(this.mPhone, this, this.mSc, this.mSib, this.mSia)) {
                return;
            }
            if (isShortCode()) {
                Rlog.d("ImsPhoneMmiCode", "processCode: isShortCode");
                if (isUssdOverImsAllowed()) {
                    Rlog.d("ImsPhoneMmiCode", "processCode: Sending short code '" + this.mDialingNumber + "' over IMS pipe.");
                    sendUssd(this.mDialingNumber);
                    return;
                }
                Rlog.d("ImsPhoneMmiCode", "processCode: Sending short code '" + this.mDialingNumber + "' over CS pipe.");
                throw new CallStateException(Phone.CS_FALLBACK);
            }
            int i2 = 1;
            if (isServiceCodeCallForwarding(this.mSc)) {
                Rlog.d("ImsPhoneMmiCode", "processCode: is CF");
                String str = this.mSia;
                int iScToCallForwardReason = scToCallForwardReason(this.mSc);
                int iSiToServiceClass = siToServiceClass(this.mSib);
                if (this.mSc.equals(SC_CFNRy) && isEmptyOrNull(this.mSic)) {
                    iSiToTime = MiuiCallControllerStub.get(this.mPhone.getPhoneId()).updateCfnryTime(iScToCallForwardReason, this.mPhone, getCfnryTime());
                } else {
                    iSiToTime = siToTime(this.mSic);
                }
                int i3 = iSiToTime;
                if (isInterrogate()) {
                    this.mIHyperSsManager.setServiceClass(iSiToServiceClass);
                    this.mPhone.getCallForwardingOption(iScToCallForwardReason, iSiToServiceClass, obtainMessage(1, this));
                    return;
                }
                if (isActivate()) {
                    if (isEmptyOrNull(str)) {
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
                boolean zIsVoiceUnconditionalForwarding = isVoiceUnconditionalForwarding(iScToCallForwardReason, iSiToServiceClass);
                if (i != 1 && i != 3) {
                    i2 = 0;
                }
                Rlog.d("ImsPhoneMmiCode", "processCode: is CF setCallForward");
                this.mPhone.setCallForwardingOption(i, iScToCallForwardReason, str, iSiToServiceClass, i3, obtainMessage(4, zIsVoiceUnconditionalForwarding ? 1 : 0, i2, this));
                return;
            }
            if (isServiceCodeCallBarring(this.mSc)) {
                String str2 = this.mSia;
                String strScToBarringFacility = scToBarringFacility(this.mSc);
                int iSiToServiceClass2 = siToServiceClass(this.mSib);
                if (isInterrogate()) {
                    this.mPhone.getCallBarring(strScToBarringFacility, obtainMessage(7, this), iSiToServiceClass2);
                    return;
                }
                if (!isActivate() && !isDeactivate()) {
                    throw new RuntimeException("Invalid or Unsupported MMI Code");
                }
                this.mPhone.setCallBarring(strScToBarringFacility, isActivate(), str2, obtainMessage(0, this), iSiToServiceClass2);
                return;
            }
            String str3 = this.mSc;
            if (str3 != null && str3.equals(SC_CLIR)) {
                if (isActivate() && !this.mPhone.getDefaultPhone().isClirActivationAndDeactivationPrevented()) {
                    try {
                        this.mPhone.setOutgoingCallerIdDisplay(1, obtainMessage(0, this));
                        return;
                    } catch (Exception unused) {
                        Rlog.d("ImsPhoneMmiCode", "processCode: Could not get UT handle for updateCLIR.");
                        return;
                    }
                } else if (isDeactivate() && !this.mPhone.getDefaultPhone().isClirActivationAndDeactivationPrevented()) {
                    try {
                        this.mPhone.setOutgoingCallerIdDisplay(2, obtainMessage(0, this));
                        return;
                    } catch (Exception unused2) {
                        Rlog.d("ImsPhoneMmiCode", "processCode: Could not get UT handle for updateCLIR.");
                        return;
                    }
                } else {
                    if (isInterrogate()) {
                        try {
                            this.mPhone.getOutgoingCallerIdDisplay(obtainMessage(6, this));
                            return;
                        } catch (Exception unused3) {
                            Rlog.d("ImsPhoneMmiCode", "processCode: Could not get UT handle for queryCLIR.");
                            return;
                        }
                    }
                    throw new RuntimeException("Invalid or Unsupported MMI Code");
                }
            }
            String str4 = this.mSc;
            if (str4 != null && str4.equals(SC_CLIP)) {
                if (isInterrogate()) {
                    try {
                        this.mPhone.queryCLIP(obtainMessage(7, this));
                        return;
                    } catch (Exception unused4) {
                        Rlog.d("ImsPhoneMmiCode", "processCode: Could not get UT handle for queryCLIP.");
                        return;
                    }
                } else {
                    if (!isActivate() && !isDeactivate()) {
                        throw new RuntimeException("Invalid or Unsupported MMI Code");
                    }
                    try {
                        this.mPhone.mCT.getUtInterface().updateCLIP(isActivate(), obtainMessage(0, this));
                        return;
                    } catch (ImsException unused5) {
                        Rlog.d("ImsPhoneMmiCode", "processCode: Could not get UT handle for updateCLIP.");
                        return;
                    }
                }
            }
            String str5 = this.mSc;
            if (str5 != null && str5.equals(SC_COLP)) {
                if (isInterrogate()) {
                    try {
                        this.mPhone.mCT.getUtInterface().queryCOLP(obtainMessage(7, this));
                        return;
                    } catch (ImsException unused6) {
                        Rlog.d("ImsPhoneMmiCode", "processCode: Could not get UT handle for queryCOLP.");
                        return;
                    }
                } else {
                    if (!isActivate() && !isDeactivate()) {
                        throw new RuntimeException("Invalid or Unsupported MMI Code");
                    }
                    try {
                        this.mPhone.mCT.getUtInterface().updateCOLP(isActivate(), obtainMessage(0, this));
                        return;
                    } catch (ImsException unused7) {
                        Rlog.d("ImsPhoneMmiCode", "processCode: Could not get UT handle for updateCOLP.");
                        return;
                    }
                }
            }
            String str6 = this.mSc;
            if (str6 != null && str6.equals(SC_COLR)) {
                if (isActivate()) {
                    try {
                        this.mPhone.mCT.getUtInterface().updateCOLR(1, obtainMessage(0, this));
                        return;
                    } catch (ImsException unused8) {
                        Rlog.d("ImsPhoneMmiCode", "processCode: Could not get UT handle for updateCOLR.");
                        return;
                    }
                } else if (isDeactivate()) {
                    try {
                        this.mPhone.mCT.getUtInterface().updateCOLR(0, obtainMessage(0, this));
                        return;
                    } catch (ImsException unused9) {
                        Rlog.d("ImsPhoneMmiCode", "processCode: Could not get UT handle for updateCOLR.");
                        return;
                    }
                } else {
                    if (isInterrogate()) {
                        try {
                            this.mPhone.mCT.getUtInterface().queryCOLR(obtainMessage(7, this));
                            return;
                        } catch (ImsException unused10) {
                            Rlog.d("ImsPhoneMmiCode", "processCode: Could not get UT handle for queryCOLR.");
                            return;
                        }
                    }
                    throw new RuntimeException("Invalid or Unsupported MMI Code");
                }
            }
            String str7 = this.mSc;
            if (str7 != null && str7.equals(SC_BS_MT)) {
                try {
                    if (isInterrogate()) {
                        this.mPhone.mCT.getUtInterface().queryCallBarring(10, obtainMessage(10, this));
                    } else {
                        processIcbMmiCodeForUpdate();
                    }
                    return;
                } catch (ImsException unused11) {
                    Rlog.d("ImsPhoneMmiCode", "processCode: Could not get UT handle for ICB.");
                    return;
                }
            }
            String str8 = this.mSc;
            if (str8 != null && str8.equals(SC_BAICa)) {
                try {
                    if (isInterrogate()) {
                        this.mPhone.mCT.getUtInterface().queryCallBarring(6, obtainMessage(10, this));
                        return;
                    }
                    if (!isActivate()) {
                        isDeactivate();
                        i2 = 0;
                    }
                    this.mPhone.mCT.getUtInterface().updateCallBarring(6, i2, obtainMessage(0, this), (String[]) null);
                    return;
                } catch (ImsException unused12) {
                    Rlog.d("ImsPhoneMmiCode", "processCode: Could not get UT handle for ICBa.");
                    return;
                }
            }
            String str9 = this.mSc;
            if (str9 != null && str9.equals(SC_WAIT)) {
                int iSiToServiceClass3 = siToServiceClass(this.mSia);
                if (!isActivate() && !isDeactivate()) {
                    if (isInterrogate()) {
                        int terminalBasedCallWaitingState = this.mPhone.getTerminalBasedCallWaitingState(false);
                        ImsPhone imsPhone = this.mPhone;
                        if (terminalBasedCallWaitingState != -1) {
                            imsPhone.getDefaultPhone().getCallWaiting(obtainMessage(3, this));
                            return;
                        } else {
                            imsPhone.getCallWaiting(obtainMessage(3, this));
                            return;
                        }
                    }
                    throw new RuntimeException("Invalid or Unsupported MMI Code");
                }
                if ((iSiToServiceClass3 == 0 || (iSiToServiceClass3 & 1) == 1) && this.mPhone.getTerminalBasedCallWaitingState(false) != -1) {
                    this.mPhone.getDefaultPhone().setCallWaiting(isActivate(), iSiToServiceClass3, obtainMessage(0, this));
                    return;
                } else {
                    this.mPhone.setCallWaiting(isActivate(), iSiToServiceClass3, obtainMessage(0, this));
                    return;
                }
            }
            if (this.mPoundString == null) {
                Rlog.d("ImsPhoneMmiCode", "processCode: invalid or unsupported MMI");
                throw new RuntimeException("Invalid or Unsupported MMI Code");
            }
            if (isUssdOverImsAllowed()) {
                Rlog.i("ImsPhoneMmiCode", "processCode: Sending ussd string '" + Rlog.pii("ImsPhoneMmiCode", this.mPoundString) + "' over IMS pipe.");
                sendUssd(this.mPoundString);
                return;
            }
            Rlog.i("ImsPhoneMmiCode", "processCode: Sending ussd string '" + Rlog.pii("ImsPhoneMmiCode", this.mPoundString) + "' over CS pipe.");
            throw new CallStateException(Phone.CS_FALLBACK);
            this.mState = MmiCode.State.FAILED;
            this.mMessage = this.mContext.getText(R.string.permdesc_readPhoneState);
            Rlog.d("ImsPhoneMmiCode", "processCode: RuntimeException = " + e);
            this.mPhone.onMMIDone(this);
        } catch (RuntimeException e) {
            this.mState = MmiCode.State.FAILED;
            this.mMessage = this.mContext.getText(R.string.permdesc_readPhoneState);
            Rlog.d("ImsPhoneMmiCode", "processCode: RuntimeException = " + e);
            this.mPhone.onMMIDone(this);
        }
    }

    private int getCfnryTime() {
        PersistableBundle configForSubId;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService(CarrierConfigManager.class);
        if (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(this.mPhone.getSubId())) == null || !configForSubId.getBoolean("support_no_reply_timer_for_cfnry_bool", true)) {
            return 20;
        }
        int i = configForSubId.getInt("no_reply_timer_for_cfnry_sec_int", 20);
        Rlog.d("ImsPhoneMmiCode", "getCfnryTime: " + i);
        return i;
    }

    private boolean isUssdOverImsAllowed() {
        if (this.mContext.getResources().getBoolean(R.bool.config_allowTheaterModeWakeFromUnplug)) {
            int intCarrierConfig = getIntCarrierConfig("carrier_ussd_method_int");
            if (intCarrierConfig == 0) {
                if (this.mPhone.getDefaultPhone().getServiceStateTracker().mSS.getState() == 0) {
                    return false;
                }
                Rlog.i("ImsPhoneMmiCode", "isUssdOverImsAllowed: CS is out of service");
                return true;
            }
            if (intCarrierConfig != 1) {
                if (intCarrierConfig != 2) {
                    if (intCarrierConfig != 3) {
                        Rlog.i("ImsPhoneMmiCode", "isUssdOverImsAllowed: Unsupported method");
                    }
                }
                return false;
            }
            return true;
        }
        Rlog.i("ImsPhoneMmiCode", "isUssdOverImsAllowed: USSD over IMS pipe is not supported.");
        return false;
    }

    public void onUssdFinished(String str, boolean z) {
        if (this.mState == MmiCode.State.PENDING) {
            if (TextUtils.isEmpty(str)) {
                this.mMessage = this.mContext.getText(R.string.permdesc_readPhoneNumbers);
                Rlog.v("ImsPhoneMmiCode", "onUssdFinished: no message; using: " + ((Object) this.mMessage));
            } else {
                Rlog.v("ImsPhoneMmiCode", "onUssdFinished: message: " + str);
                this.mMessage = str;
            }
            this.mIsUssdRequest = z;
            if (!z) {
                this.mState = MmiCode.State.COMPLETE;
            }
            this.mPhone.onMMIDone(this);
        }
    }

    public void onUssdFinishedError() {
        if (this.mState == MmiCode.State.PENDING) {
            this.mState = MmiCode.State.FAILED;
            if (TextUtils.isEmpty(this.mMessage)) {
                this.mMessage = this.mContext.getText(R.string.permdesc_readPhoneState);
            }
            Rlog.d("ImsPhoneMmiCode", "onUssdFinishedError: mmi=" + this);
            this.mPhone.onMMIDone(this);
        }
    }

    public void sendUssd(String str) {
        this.mIsPendingUSSD = true;
        this.mPhone.sendUSSD(str, obtainMessage(2, this));
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i != 10) {
            switch (i) {
                case 0:
                    onSetComplete(message, (AsyncResult) message.obj);
                    break;
                case 1:
                    onQueryCfComplete((AsyncResult) message.obj);
                    break;
                case 2:
                    AsyncResult asyncResult = (AsyncResult) message.obj;
                    if (asyncResult.exception != null) {
                        this.mState = MmiCode.State.FAILED;
                        this.mMessage = getErrorMessage(asyncResult);
                        this.mPhone.onMMIDone(this);
                    }
                    break;
                case 3:
                    onQueryComplete((AsyncResult) message.obj);
                    break;
                case 4:
                    AsyncResult asyncResult2 = (AsyncResult) message.obj;
                    if (asyncResult2.exception == null && message.arg1 == 1) {
                        boolean z = message.arg2 == 1;
                        IccRecords iccRecords = this.mIccRecords;
                        if (iccRecords != null) {
                            this.mPhone.setVoiceCallForwardingFlag(iccRecords, 1, z, this.mDialingNumber);
                            this.mPhone.notifyCallForwardingIndicator();
                        }
                    }
                    onSetComplete(message, asyncResult2);
                    break;
                case 5:
                    this.mPhone.onMMIDone(this);
                    break;
                case 6:
                    onQueryClirComplete((AsyncResult) message.obj);
                    break;
                case 7:
                    onSuppSvcQueryComplete((AsyncResult) message.obj);
                    break;
            }
            return;
        }
        onIcbQueryComplete((AsyncResult) message.obj);
    }

    protected void processIcbMmiCodeForUpdate() {
        String str = this.mSia;
        try {
            this.mPhone.mCT.getUtInterface().updateCallBarring(10, callBarAction(str), obtainMessage(0, this), str != null ? str.split("\\$") : null);
        } catch (ImsException unused) {
            Rlog.d("ImsPhoneMmiCode", "processIcbMmiCodeForUpdate:Could not get UT handle for updating ICB.");
        }
    }

    protected CharSequence getErrorMessage(AsyncResult asyncResult) {
        CharSequence mmiErrorMessage = getMmiErrorMessage(asyncResult);
        return mmiErrorMessage != null ? mmiErrorMessage : this.mContext.getText(R.string.permdesc_readPhoneState);
    }

    public CharSequence getMmiErrorMessage(AsyncResult asyncResult) {
        ImsException imsException = asyncResult.exception;
        if (imsException instanceof ImsException) {
            int code = imsException.getCode();
            if (code == 241) {
                return this.mContext.getText(R.string.permdesc_readSyncStats);
            }
            switch (code) {
                case 822:
                    return this.mContext.getText(17042316);
                case 823:
                    return this.mContext.getText(17042319);
                case 824:
                    return this.mContext.getText(17042318);
                case 825:
                    return this.mContext.getText(17042317);
                default:
                    return null;
            }
        }
        if (imsException instanceof CommandException) {
            CommandException commandException = (CommandException) imsException;
            if (commandException.getCommandError() == CommandException.Error.FDN_CHECK_FAILURE) {
                return this.mContext.getText(R.string.permdesc_readSyncStats);
            }
            if (commandException.getCommandError() == CommandException.Error.SS_MODIFIED_TO_DIAL) {
                return this.mContext.getText(17042316);
            }
            if (commandException.getCommandError() == CommandException.Error.SS_MODIFIED_TO_USSD) {
                return this.mContext.getText(17042319);
            }
            if (commandException.getCommandError() == CommandException.Error.SS_MODIFIED_TO_SS) {
                return this.mContext.getText(17042318);
            }
            if (commandException.getCommandError() == CommandException.Error.SS_MODIFIED_TO_DIAL_VIDEO) {
                return this.mContext.getText(17042317);
            }
            if (commandException.getCommandError() == CommandException.Error.INTERNAL_ERR) {
                return this.mContext.getText(R.string.permdesc_readPhoneState);
            }
            if (commandException.getCommandError() == CommandException.Error.REQUEST_NOT_SUPPORTED || commandException.getCommandError() == CommandException.Error.OPERATION_NOT_ALLOWED) {
                return this.mContext.getResources().getText(R.string.permdesc_readSms);
            }
        }
        return null;
    }

    protected CharSequence getScString() {
        String str = this.mSc;
        if (str != null) {
            if (isServiceCodeCallBarring(str)) {
                return this.mContext.getText(R.string.BaMmi);
            }
            if (isServiceCodeCallForwarding(this.mSc)) {
                return this.mIHyperSsManager.getCfType(this.mContext, this.mPhone.getSubId(), this.mSc);
            }
            if (this.mSc.equals(SC_PWD)) {
                return this.mContext.getText(R.string.accessibility_autoclick_double_click);
            }
            if (this.mSc.equals(SC_WAIT)) {
                return this.mContext.getText(R.string.EmergencyCallWarningTitle);
            }
            if (this.mSc.equals(SC_CLIP)) {
                return this.mContext.getText(R.string.CnipMmi);
            }
            if (this.mSc.equals(SC_CLIR)) {
                return this.mContext.getText(R.string.CnirMmi);
            }
            if (this.mSc.equals(SC_COLP)) {
                return this.mContext.getText(R.string.DndMmi);
            }
            if (this.mSc.equals(SC_COLR)) {
                return this.mContext.getText(R.string.EmergencyCallWarningSummary);
            }
            if (this.mSc.equals(SC_BS_MT)) {
                return IcbDnMmi;
            }
            if (this.mSc.equals(SC_BAICa)) {
                return IcbAnonymousMmi;
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
                CommandException commandException = (CommandException) th;
                if (commandException.getCommandError() == CommandException.Error.PASSWORD_INCORRECT) {
                    sb.append(this.mContext.getText(R.string.permlab_getTasks));
                } else {
                    CharSequence mmiErrorMessage = getMmiErrorMessage(asyncResult);
                    if (mmiErrorMessage != null) {
                        sb.append(mmiErrorMessage);
                    } else if (commandException.getMessage() != null) {
                        sb.append(commandException.getMessage());
                    } else {
                        sb.append(this.mContext.getText(R.string.permdesc_readPhoneState));
                    }
                }
            } else if (th instanceof ImsException) {
                sb.append(getImsErrorMessage(asyncResult));
            }
        } else {
            Object obj = asyncResult.result;
            if (obj != null && (obj instanceof Integer) && ((Integer) obj).intValue() == 255) {
                this.mState = MmiCode.State.FAILED;
                sb = null;
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
        }
        this.mMessage = sb;
        Rlog.d("ImsPhoneMmiCode", "onSetComplete: mmi=" + this);
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
        charSequenceArr[1] = PhoneNumberUtils.stringFromStringAndTOA(callForwardInfo.number, callForwardInfo.toa);
        charSequenceArr[2] = Integer.toString(callForwardInfo.timeSeconds);
        if (callForwardInfo.reason == 0 && (i & callForwardInfo.serviceClass) == 1) {
            this.mPhone.setVoiceCallForwardingFlag(this.mIccRecords, 1, callForwardInfo.status == 1, callForwardInfo.number);
        }
        return TextUtils.replace(text, strArr, charSequenceArr);
    }

    protected void onQueryCfComplete(AsyncResult asyncResult) {
        StringBuilder sb = new StringBuilder(getScString());
        sb.append("\n");
        Throwable th = asyncResult.exception;
        if (th != null) {
            this.mState = MmiCode.State.FAILED;
            if (th instanceof ImsException) {
                sb.append(getImsErrorMessage(asyncResult));
            } else {
                sb.append(getErrorMessage(asyncResult));
            }
        } else {
            Object obj = asyncResult.result;
            if ((obj instanceof CallForwardInfo[]) && ((CallForwardInfo[]) obj)[0].status == 255) {
                this.mState = MmiCode.State.FAILED;
                sb = null;
            } else {
                CallForwardInfo[] callForwardInfoArr = (CallForwardInfo[]) obj;
                if (callForwardInfoArr == null || callForwardInfoArr.length == 0) {
                    sb.append(this.mContext.getText(17042203));
                    this.mPhone.setVoiceCallForwardingFlag(this.mIccRecords, 1, false, null);
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
        }
        this.mMessage = sb;
        Rlog.d("ImsPhoneMmiCode", "onQueryCfComplete: mmi=" + this);
        this.mPhone.onMMIDone(this);
    }

    protected void onSuppSvcQueryComplete(AsyncResult asyncResult) {
        StringBuilder sb = new StringBuilder(getScString());
        sb.append("\n");
        this.mState = MmiCode.State.FAILED;
        Throwable th = asyncResult.exception;
        if (th != null) {
            if (th instanceof ImsException) {
                sb.append(getImsErrorMessage(asyncResult));
            } else {
                sb.append(getErrorMessage(asyncResult));
            }
        } else if (asyncResult.result instanceof ImsSsInfo) {
            Rlog.d("ImsPhoneMmiCode", "onSuppSvcQueryComplete: Received CLIP/COLP/COLR Response.");
            ImsSsInfo imsSsInfo = (ImsSsInfo) asyncResult.result;
            if (imsSsInfo != null) {
                Rlog.d("ImsPhoneMmiCode", "onSuppSvcQueryComplete: ImsSsInfo mStatus = " + imsSsInfo.getStatus());
                if (imsSsInfo.getProvisionStatus() == 0) {
                    sb.append(this.mContext.getText(17042207));
                    this.mState = MmiCode.State.COMPLETE;
                } else if (imsSsInfo.getStatus() == 0) {
                    sb.append(this.mContext.getText(17042203));
                    this.mState = MmiCode.State.COMPLETE;
                } else {
                    int status = imsSsInfo.getStatus();
                    Context context = this.mContext;
                    if (status == 1) {
                        sb.append(context.getText(17042204));
                        this.mState = MmiCode.State.COMPLETE;
                    } else {
                        sb.append(context.getText(R.string.permdesc_readPhoneState));
                    }
                }
            } else {
                sb.append(this.mContext.getText(R.string.permdesc_readPhoneState));
            }
        } else {
            Rlog.d("ImsPhoneMmiCode", "onSuppSvcQueryComplete: Received Call Barring/CSFB CLIP Response.");
            int[] iArr = (int[]) asyncResult.result;
            if (iArr == null || iArr.length == 0) {
                sb.append(this.mContext.getText(R.string.permdesc_readPhoneState));
            } else {
                int i = iArr[0];
                Context context2 = this.mContext;
                if (i != 0) {
                    sb.append(context2.getText(17042204));
                    this.mState = MmiCode.State.COMPLETE;
                } else {
                    sb.append(context2.getText(17042203));
                    this.mState = MmiCode.State.COMPLETE;
                }
            }
        }
        this.mMessage = sb;
        Rlog.d("ImsPhoneMmiCode", "onSuppSvcQueryComplete mmi=" + this);
        this.mPhone.onMMIDone(this);
    }

    protected void onIcbQueryComplete(AsyncResult asyncResult) {
        List listAsList;
        Rlog.d("ImsPhoneMmiCode", "onIcbQueryComplete mmi=" + this);
        StringBuilder sb = new StringBuilder(getScString());
        sb.append("\n");
        Throwable th = asyncResult.exception;
        if (th != null) {
            this.mState = MmiCode.State.FAILED;
            if (th instanceof ImsException) {
                sb.append(getImsErrorMessage(asyncResult));
            } else {
                sb.append(getErrorMessage(asyncResult));
            }
        } else {
            try {
                listAsList = (List) asyncResult.result;
            } catch (ClassCastException unused) {
                listAsList = Arrays.asList((ImsSsInfo[]) asyncResult.result);
            }
            if (listAsList == null || listAsList.size() == 0) {
                sb.append(this.mContext.getText(17042203));
            } else {
                int size = listAsList.size();
                for (int i = 0; i < size; i++) {
                    ImsSsInfo imsSsInfo = (ImsSsInfo) listAsList.get(i);
                    if (imsSsInfo.getIncomingCommunicationBarringNumber() != null) {
                        sb.append("Num: " + imsSsInfo.getIncomingCommunicationBarringNumber() + " status: " + imsSsInfo.getStatus() + "\n");
                    } else {
                        int status = imsSsInfo.getStatus();
                        Context context = this.mContext;
                        if (status == 1) {
                            sb.append(context.getText(17042204));
                        } else {
                            sb.append(context.getText(17042203));
                        }
                    }
                }
            }
            this.mState = MmiCode.State.COMPLETE;
        }
        this.mMessage = sb;
        this.mPhone.onMMIDone(this);
    }

    protected void onQueryClirComplete(AsyncResult asyncResult) {
        StringBuilder sb = new StringBuilder(getScString());
        sb.append("\n");
        MmiCode.State state = MmiCode.State.FAILED;
        this.mState = state;
        Throwable th = asyncResult.exception;
        if (th != null) {
            if (th instanceof ImsException) {
                sb.append(getImsErrorMessage(asyncResult));
            } else {
                sb.append(getErrorMessage(asyncResult));
            }
        } else {
            int[] iArr = (int[]) asyncResult.result;
            Rlog.d("ImsPhoneMmiCode", "onQueryClirComplete: CLIR param n=" + iArr[0] + " m=" + iArr[1]);
            int i = iArr[1];
            if (i == 0) {
                sb.append(this.mContext.getText(17042207));
                this.mState = MmiCode.State.COMPLETE;
            } else if (i == 1) {
                sb.append(this.mContext.getText(R.string.CLIRPermanent));
                this.mState = MmiCode.State.COMPLETE;
            } else if (i == 3) {
                int i2 = iArr[0];
                if (i2 == 0 || i2 == 1) {
                    sb.append(this.mContext.getText(R.string.CLIRDefaultOnNextCallOn));
                    this.mState = MmiCode.State.COMPLETE;
                } else {
                    Context context = this.mContext;
                    if (i2 == 2) {
                        sb.append(context.getText(R.string.CLIRDefaultOnNextCallOff));
                        this.mState = MmiCode.State.COMPLETE;
                    } else {
                        sb.append(context.getText(R.string.permdesc_readPhoneState));
                        this.mState = state;
                    }
                }
            } else if (i == 4) {
                int i3 = iArr[0];
                if (i3 == 0) {
                    sb.append(this.mContext.getText(R.string.CLIRDefaultOffNextCallOff));
                    this.mState = MmiCode.State.COMPLETE;
                } else if (i3 == 1) {
                    sb.append(this.mContext.getText(R.string.CLIRDefaultOffNextCallOn));
                    this.mState = MmiCode.State.COMPLETE;
                } else {
                    Context context2 = this.mContext;
                    if (i3 == 2) {
                        sb.append(context2.getText(R.string.CLIRDefaultOffNextCallOff));
                        this.mState = MmiCode.State.COMPLETE;
                    } else {
                        sb.append(context2.getText(R.string.permdesc_readPhoneState));
                        this.mState = state;
                    }
                }
            } else {
                sb.append(this.mContext.getText(R.string.permdesc_readPhoneState));
                this.mState = state;
            }
        }
        this.mMessage = sb;
        Rlog.d("ImsPhoneMmiCode", "onQueryClirComplete mmi=" + this);
        this.mPhone.onMMIDone(this);
    }

    protected void onQueryComplete(AsyncResult asyncResult) {
        StringBuilder sb = new StringBuilder(getScString());
        sb.append("\n");
        this.mState = MmiCode.State.FAILED;
        Throwable th = asyncResult.exception;
        if (th != null) {
            if (th instanceof ImsException) {
                sb.append(getImsErrorMessage(asyncResult));
            } else {
                sb.append(getErrorMessage(asyncResult));
            }
        } else {
            Object obj = asyncResult.result;
            if ((obj instanceof int[]) && ((int[]) obj)[0] == 255) {
                sb = null;
            } else {
                int[] iArr = (int[]) obj;
                if (iArr != null && iArr.length != 0) {
                    if (iArr[0] == 0) {
                        sb.append(this.mContext.getText(17042203));
                        this.mState = MmiCode.State.COMPLETE;
                    } else if (this.mSc.equals(SC_WAIT)) {
                        sb.append(createQueryCallWaitingResultMessage(iArr[1]));
                        this.mState = MmiCode.State.COMPLETE;
                    } else {
                        int i = iArr[0];
                        Context context = this.mContext;
                        if (i == 1) {
                            sb.append(context.getText(17042204));
                            this.mState = MmiCode.State.COMPLETE;
                        } else {
                            sb.append(context.getText(R.string.permdesc_readPhoneState));
                        }
                    }
                } else {
                    sb.append(this.mContext.getText(R.string.permdesc_readPhoneState));
                }
            }
        }
        this.mMessage = sb;
        Rlog.d("ImsPhoneMmiCode", "onQueryComplete mmi=" + this);
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

    protected CharSequence getImsErrorMessage(AsyncResult asyncResult) {
        ImsException imsException = asyncResult.exception;
        CharSequence mmiErrorMessage = getMmiErrorMessage(asyncResult);
        if (mmiErrorMessage != null) {
            return mmiErrorMessage;
        }
        if (imsException.getMessage() != null) {
            return imsException.getMessage();
        }
        return getErrorMessage(asyncResult);
    }

    private int getIntCarrierConfig(String str) {
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService("carrier_config");
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

    public void processImsSsData(AsyncResult asyncResult) throws ImsException {
        try {
            parseSsData((ImsSsData) asyncResult.result);
        } catch (ClassCastException | NullPointerException unused) {
            throw new ImsException("Exception in parsing SS Data", 0);
        }
    }

    void parseSsData(ImsSsData imsSsData) {
        ImsException imsException = imsSsData.getResult() != 0 ? new ImsException((String) null, imsSsData.getResult()) : null;
        this.mSc = getScStringFromScType(imsSsData.getServiceType());
        this.mAction = getActionStringFromReqType(imsSsData.getRequestType());
        Rlog.d("ImsPhoneMmiCode", "parseSsData msc = " + this.mSc + ", action = " + this.mAction + ", ex = " + imsException);
        int requestType = imsSsData.getRequestType();
        boolean z = false;
        if (requestType != 0 && requestType != 1) {
            if (requestType == 2) {
                if (imsSsData.isTypeClir()) {
                    Rlog.d("ImsPhoneMmiCode", "CLIR INTERROGATION");
                    onQueryClirComplete(new AsyncResult((Object) null, imsSsData.getSuppServiceInfoCompat(), imsException));
                    return;
                }
                if (imsSsData.isTypeCF()) {
                    Rlog.d("ImsPhoneMmiCode", "CALL FORWARD INTERROGATION");
                    List callForwardInfo = imsSsData.getCallForwardInfo();
                    onQueryCfComplete(new AsyncResult((Object) null, this.mPhone.handleCfQueryResult(callForwardInfo != null ? (ImsCallForwardInfo[]) callForwardInfo.toArray(new ImsCallForwardInfo[callForwardInfo.size()]) : null), imsException));
                    return;
                } else {
                    if (imsSsData.isTypeBarring()) {
                        onSuppSvcQueryComplete(new AsyncResult((Object) null, imsSsData.getSuppServiceInfoCompat(), imsException));
                        return;
                    }
                    if (imsSsData.isTypeColr() || imsSsData.isTypeClip() || imsSsData.isTypeColp()) {
                        onSuppSvcQueryComplete(new AsyncResult((Object) null, imsSsData.getSuppServiceInfo().get(0), imsException));
                        return;
                    } else if (imsSsData.isTypeIcb()) {
                        onIcbQueryComplete(new AsyncResult((Object) null, imsSsData.getSuppServiceInfo(), imsException));
                        return;
                    } else {
                        onQueryComplete(new AsyncResult((Object) null, imsSsData.getSuppServiceInfoCompat(), imsException));
                        return;
                    }
                }
            }
            if (requestType != 3 && requestType != 4) {
                Rlog.e("ImsPhoneMmiCode", "Invaid requestType in SSData : " + imsSsData.getRequestType());
                return;
            }
        }
        if (imsSsData.getResult() == 0 && imsSsData.isTypeUnConditional()) {
            if ((imsSsData.getRequestType() == 0 || imsSsData.getRequestType() == 3) && isServiceClassVoiceVideoOrNone(imsSsData.getServiceClass())) {
                z = true;
            }
            Rlog.d("ImsPhoneMmiCode", "setCallForwardingFlag cffEnabled: " + z);
            if (this.mIccRecords != null) {
                Rlog.d("ImsPhoneMmiCode", "setVoiceCallForwardingFlag done from SS Info.");
                Rlog.d("ImsPhoneMmiCode", "setVoiceCallForwardingFlag done from SS Info.");
                this.mPhone.setVoiceCallForwardingFlag(1, z, null);
            } else {
                Rlog.e("ImsPhoneMmiCode", "setCallForwardingFlag aborted. sim records is null.");
            }
        }
        onSetComplete(null, new AsyncResult((Object) null, imsSsData.getCallForwardInfo(), imsException));
    }

    protected String getScStringFromScType(int i) {
        switch (i) {
            case 0:
                return SC_CFU;
            case 1:
                return SC_CFB;
            case 2:
                return SC_CFNRy;
            case 3:
                return SC_CFNR;
            case 4:
                return SC_CF_All;
            case 5:
                return SC_CF_All_Conditional;
            case 6:
            default:
                return null;
            case 7:
                return SC_CLIP;
            case 8:
                return SC_CLIR;
            case 9:
                return SC_COLP;
            case 10:
                return SC_COLR;
            case 11:
                return SC_CNAP;
            case 12:
                return SC_WAIT;
            case 13:
                return SC_BAOC;
            case 14:
                return SC_BAOIC;
            case 15:
                return SC_BAOICxH;
            case 16:
                return SC_BAIC;
            case 17:
                return SC_BAICr;
            case 18:
                return SC_BA_ALL;
            case 19:
                return SC_BA_MO;
            case 20:
                return SC_BA_MT;
            case 21:
                return SC_BS_MT;
            case 22:
                return SC_BAICa;
        }
    }

    protected String getActionStringFromReqType(int i) {
        if (i == 0) {
            return ACTION_ACTIVATE;
        }
        if (i == 1) {
            return ACTION_DEACTIVATE;
        }
        if (i == 2) {
            return ACTION_INTERROGATE;
        }
        if (i == 3) {
            return ACTION_REGISTER;
        }
        if (i != 4) {
            return null;
        }
        return ACTION_ERASURE;
    }

    public boolean isSsInfo() {
        return this.mIsSsInfo;
    }

    public void setIsSsInfo(boolean z) {
        this.mIsSsInfo = z;
    }

    @Override // android.os.Handler
    public String toString() {
        StringBuilder sb = new StringBuilder("ImsPhoneMmiCode {");
        sb.append("State=" + getState());
        if (this.mAction != null) {
            sb.append(" action=" + this.mAction);
        }
        if (this.mSc != null) {
            sb.append(" sc=" + this.mSc);
        }
        if (this.mSia != null) {
            sb.append(" sia=" + this.mSia);
        }
        if (this.mSib != null) {
            sb.append(" sib=" + this.mSib);
        }
        if (this.mSic != null) {
            sb.append(" sic=" + this.mSic);
        }
        if (this.mPoundString != null) {
            sb.append(" poundString=" + Rlog.pii("ImsPhoneMmiCode", this.mPoundString));
        }
        if (this.mDialingNumber != null) {
            sb.append(" dialingNumber=" + Rlog.pii("ImsPhoneMmiCode", this.mDialingNumber));
        }
        if (this.mPwd != null) {
            sb.append(" pwd=" + Rlog.pii("ImsPhoneMmiCode", this.mPwd));
        }
        if (this.mCallbackReceiver != null) {
            sb.append(" hasReceiver");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override // com.android.internal.telephony.MmiCode
    public boolean isNetworkInitiatedUssd() {
        return this.mIsNetworkInitiatedUSSD;
    }

    protected static boolean shouldCreateMmiCode(String str) {
        if (SystemProperties.get("ro.vendor.mtk_telephony_add_on_policy", "0").equals("0")) {
            try {
                Method method = Class.forName("com.mediatek.internal.telephony.gsm.MtkGsmMmiCode", false, ClassLoader.getSystemClassLoader()).getMethod("shouldCreateMmiCode", String.class);
                method.setAccessible(true);
                return ((Boolean) method.invoke(null, str)).booleanValue();
            } catch (Exception e) {
                Rlog.e("ImsPhoneMmiCode", "shouldCreateMmiCode exception!" + e);
            }
        }
        return true;
    }

    protected static ImsPhoneMmiCode create(ImsPhone imsPhone, FeatureFlags featureFlags) {
        return TelephonyComponentFactory.getInstance().inject(ImsPhoneMmiCode.class.getName()).makeImsPhoneMmiCode(imsPhone, featureFlags);
    }

    protected static boolean isServiceCodeCallForwardingEx(String str) {
        if (SystemProperties.get("ro.vendor.mtk_telephony_add_on_policy", "0").equals("0")) {
            try {
                Method method = Class.forName("com.mediatek.internal.telephony.imsphone.MtkImsPhoneMmiCode", false, ClassLoader.getSystemClassLoader()).getMethod("isServiceCodeCallForwardingEx", String.class);
                method.setAccessible(true);
                return ((Boolean) method.invoke(null, str)).booleanValue();
            } catch (Exception unused) {
                Rlog.e("ImsPhoneMmiCode", "MtkImsPhoneMmiCode.isServiceCodeCallForwardingEx exception!");
            }
        }
        return false;
    }

    protected static int scToCallForwardReasonEx(String str) {
        if (SystemProperties.get("ro.vendor.mtk_telephony_add_on_policy", "0").equals("0")) {
            try {
                Method method = Class.forName("com.mediatek.internal.telephony.imsphone.MtkImsPhoneMmiCode", false, ClassLoader.getSystemClassLoader()).getMethod("scToCallForwardReasonEx", String.class);
                method.setAccessible(true);
                return ((Integer) method.invoke(null, str)).intValue();
            } catch (Exception unused) {
                Rlog.e("ImsPhoneMmiCode", "MtkImsPhoneMmiCode.scToCallForwardReasonEx exception!");
                return 0;
            }
        }
        Phone$$ExternalSyntheticBUOutline1.m("invalid call forward sc, should not be here!");
        return 0;
    }
}
