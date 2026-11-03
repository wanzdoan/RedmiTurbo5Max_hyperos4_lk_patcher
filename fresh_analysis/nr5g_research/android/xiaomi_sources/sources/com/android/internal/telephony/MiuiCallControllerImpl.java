package com.android.internal.telephony;

import android.R;
import android.content.Intent;
import android.os.AsyncResult;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.gsm.GsmMmiCode;
import com.android.internal.telephony.gsm.IMiuiMmiCodeCallBack;
import com.android.internal.telephony.gsm.MiuiGsmMmiCodeUtils;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneMmiCode;
import com.android.internal.telephony.uicc.MiuiAdnRecord;
import com.android.internal.telephony.uicc.MiuiUiccControllerImpl;
import com.android.internal.util.ArrayUtils;
import com.xiaomi.internal.telephony.ModemServiceMoniter;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.mirilhook.MiRilHookBase;
import com.xiaomi.modem.ModemUtils;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.FeatureConfiguration;
import miui.telephony.MiuiTelephony;
import miui.telephony.ServiceProviderUtils;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyManagerEx;
import miui.telephony.TelephonyOptCloudController;

/* JADX INFO: loaded from: classes.dex */
public class MiuiCallControllerImpl implements MiuiCallControllerStub.IMiuiCallController {
    private static final String LOG_TAG = "MiuiCallControllerImpl";
    private Phone mPhone;

    public void init(Phone phone, Object... arg) {
        this.mPhone = phone;
        MiuiTelephony.getInstance().registerTelephonyPlugins(this, phone.getPhoneId());
    }

    public void dispose(Phone phone) {
        MiuiTelephony.getInstance().unregisterTelephonyPlugins(this, phone.getPhoneId());
    }

    public void removeAllHangupPendingConnectionIndex(Phone phone) {
    }

    public boolean addHangupPendingConnectionIndex(Phone phone, int index) {
        return Processor.addHangupPendingConnectionIndex(index);
    }

    public boolean sendSpeechCodecIsHDBroadcast(Phone phone, boolean isHD) {
        return Processor.sendSpeechCodecIsHDBroadcast(phone, isHD);
    }

    public boolean isBlockedMmi(Phone phone, String dialString) {
        return MiuiGsmMmiCodeUtils.isBlockedMmi(phone, dialString);
    }

    public boolean isCLIREnableForbidden(Phone phone) {
        return MiuiGsmMmiCodeUtils.isCLIREnableForbidden(phone);
    }

    public boolean isVodafoneFACs(Phone phone, String dialStr) {
        return MiuiGsmMmiCodeUtils.isVodafoneFACs(phone, dialStr);
    }

    public boolean processCode(final GsmMmiCode owner, Object... arg) {
        if (!MiuiGsmMmiCodeUtils.isShortCode(owner) && owner.mDialingNumber == null) {
            if (MiuiGsmMmiCodeUtils.SC_CLIR.equals(MiuiGsmMmiCodeUtils.getServiceCode(owner))) {
                log("processCode: is CLIR");
                if (MiuiGsmMmiCodeUtils.isActivate(owner) && !this.mPhone.isClirActivationAndDeactivationPrevented() && MiuiGsmMmiCodeUtils.isCLIREnableForbidden(this.mPhone)) {
                    MiuiGsmMmiCodeUtils.updateMmiCodeState(owner, MmiCode.State.FAILED);
                    MiuiGsmMmiCodeUtils.updateMmiCodeState(owner, this.mPhone.getContext().getText(R.string.CLIRPermanent));
                    log("processCode: CLIR is not allowed to enable");
                    if (this.mPhone instanceof GsmCdmaPhone) {
                        this.mPhone.onMMIDone(owner);
                        return true;
                    }
                    return false;
                }
                return false;
            }
            if (MiuiGsmMmiCodeUtils.isServiceCodeCallBarring(owner)) {
                String password = MiuiGsmMmiCodeUtils.getServicePassword(owner);
                int serviceClass = MiuiGsmMmiCodeUtils.getServiceClass(owner);
                String facility = MiuiGsmMmiCodeUtils.getFacility(owner);
                if (!MiuiGsmMmiCodeUtils.isInterrogate(owner)) {
                    if (MiuiGsmMmiCodeUtils.isActivate(owner) || MiuiGsmMmiCodeUtils.isDeactivate(owner)) {
                        MiuiGsmMmiCodeUtils.setCallBarring(1, owner, this.mPhone.mCi, facility, MiuiGsmMmiCodeUtils.isActivate(owner), password, serviceClass, new IMiuiMmiCodeCallBack(this) { // from class: com.android.internal.telephony.MiuiCallControllerImpl.1
                            @Override // com.android.internal.telephony.gsm.IMiuiMmiCodeCallBack
                            public void onSetComplete(Message msg, AsyncResult ar) {
                                Message cbMsg = owner.obtainMessage(1, ar);
                                owner.sendMessage(cbMsg);
                            }
                        });
                        return true;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public void hangupImsCallIfNeeded(Phone phone, boolean isDeviceShuttingDown) {
        MiuiNetworkControllerImpl.Processor.hangupImsCallIfNeeded(phone, isDeviceShuttingDown);
    }

    public ImsPhoneMmiCode getMmiCodeForKrOperator(Phone phone, String dialString, ImsPhoneMmiCode ret, boolean isShortCode) {
        return MiuiGsmMmiCodeUtils.getMmiCodeForKrOperator(phone, dialString, ret, isShortCode);
    }

    public int updateCfnryTime(int reason, Phone phone, int time) {
        return MiuiGsmMmiCodeUtils.updateCfnryTime(reason, phone, time);
    }

    private void log(String s) {
        Rlog.d("MiuiCallControllerImpl[" + this.mPhone.getPhoneId() + "]", s);
    }

    private void loge(String s) {
        Rlog.e("MiuiCallControllerImpl[" + this.mPhone.getPhoneId() + "]", s);
    }

    public static class Processor {
        private static final String CLARO_MCCMNC = "73003";
        private static final String ENTEL_MCCMNC = "73001";
        public static final int HANGUP_FROEGROUND_RESUME_BACKGROUND_CONN_INDEX = 101;
        public static final int HANGUP_WAITING_OR_BACKGROUND_CONN_INDEX = 100;
        private static final String MCC_CHILE = "730";
        private static final int USSD_MODE_LOCAL_CLIENT = 3;
        private static final String WOM_MCCMNC = "73009";
        private static final String[] MOVISTAR_MCCMNC = {"73002", "73007"};
        private static boolean[] sIsHD = new boolean[MiuiTelephony.PHONE_COUNT];
        private static int[] sDiscountRate = new int[MiuiTelephony.PHONE_COUNT];
        private static int sLastDiscountRate = 100;
        private static boolean sVtQualityPropSupport = FeatureConfiguration.isFeatureSupported(FeatureConfiguration.FeatureType.FEATURE_VT_QUALITY_OPTIMIZATION);

        static {
            for (int i = 0; i < MiuiTelephony.PHONE_COUNT; i++) {
                sDiscountRate[i] = 100;
            }
        }

        private Processor() {
        }

        public static void removeAllHangupPendingConnectionIndex() {
        }

        public static boolean supportVtQualityProperty() {
            return sVtQualityPropSupport;
        }

        public static boolean disabledSetVtQualityPropByCloud() {
            if (TelephonyOptCloudController.CLOUD_CONTROLLER_CALL_VTQUALITY_DISABLED) {
                resetVtQualityProperty();
                return true;
            }
            return false;
        }

        public static void resetVtQualityProperty() {
            if (sLastDiscountRate == 35) {
                for (int i = 0; i < MiuiTelephony.PHONE_COUNT; i++) {
                    sDiscountRate[i] = 100;
                }
                sLastDiscountRate = 100;
                Rlog.d(MiuiCallControllerImpl.LOG_TAG, "[]  resetVtQualityProperty  DiscountRate: 100");
                SystemProperties.set("persist.radio.ims.initial_utilize_factor", String.valueOf(sLastDiscountRate));
            }
        }

        public static void setVtQualityProperty(Phone phone) {
            Phone otherPhone;
            SignalStrength currentSs = phone.getSignalStrengthController().getSignalStrength();
            if (currentSs == null || currentSs.getMiuiLevel() == 0) {
                return;
            }
            int phoneId = phone.getPhoneId();
            sDiscountRate[phoneId] = currentSs.getMiuiLevel() < 4 ? 35 : 100;
            int currentSetRet = sDiscountRate[phoneId];
            for (int i = 0; i < MiuiTelephony.PHONE_COUNT; i++) {
                if (phoneId != i && (otherPhone = PhoneFactory.getPhone(i)) != null && SubscriptionManager.isValidSubscriptionId(otherPhone.getSubId()) && sDiscountRate[i] != sDiscountRate[phoneId]) {
                    currentSetRet = 100;
                }
            }
            int i2 = sLastDiscountRate;
            if (currentSetRet != i2) {
                Rlog.d(MiuiCallControllerImpl.LOG_TAG, "[" + phoneId + "]  setVtQualityProperty  currentSetRet: " + currentSetRet + " LastDiscountRate: " + sLastDiscountRate);
                SystemProperties.set("persist.radio.ims.initial_utilize_factor", String.valueOf(currentSetRet));
                sLastDiscountRate = currentSetRet;
            }
        }

        private static void removeHangupPendingConnectionIndex(int index) {
        }

        public static boolean addHangupPendingConnectionIndex(int index) {
            return false;
        }

        public static boolean sendSpeechCodecIsHDBroadcast(Phone phone, boolean isHD) {
            if (phone == null) {
                return isHD;
            }
            String numeric = phone.getServiceState().getOperatorNumeric();
            boolean isChinaUnicom = ServiceProviderUtils.isChinaUnicom(numeric);
            if (!isChinaUnicom) {
                return isHD;
            }
            boolean localHD = isHD;
            if (isSpecialNumber(phone) || PhoneConstants.State.IDLE == phone.getState()) {
                localHD = false;
            }
            if (sIsHD[phone.getPhoneId()] != localHD) {
                sIsHD[phone.getPhoneId()] = localHD;
                sendSpeedHDIntent(phone);
            }
            return localHD;
        }

        public static void clearSpeedHDIconForPhoneId() {
            for (int i = 0; i < sIsHD.length; i++) {
                if (sIsHD[i]) {
                    sIsHD[i] = false;
                    sendSpeedHDIntent(PhoneFactory.getPhone(i));
                }
            }
        }

        private static void sendSpeedHDIntent(Phone phone) {
            Intent intent = new Intent("android.intent.action.ACTION_SPEECH_CODEC_IS_HD");
            intent.addFlags(536870912);
            intent.putExtra("is_hd", sIsHD[phone.getPhoneId()]);
            SubscriptionManager.putPhoneIdAndSubIdExtra(intent, phone.getPhoneId());
            phone.getContext().sendStickyBroadcast(intent);
            Log.d(MiuiCallControllerImpl.LOG_TAG, "sendSpeechCodecIsHDBroadcast phoneId" + phone.getPhoneId() + "showSpeedHD" + sIsHD[phone.getPhoneId()]);
        }

        private static boolean isSpecialNumber(Phone phone) {
            Call currentCall;
            String[] specialNumArray = {"10010", "114"};
            if (phone.getRingingCall().getState().isAlive()) {
                currentCall = phone.getRingingCall();
            } else if (phone.getForegroundCall().getState().isAlive()) {
                currentCall = phone.getForegroundCall();
            } else {
                if (!phone.getBackgroundCall().getState().isAlive()) {
                    return false;
                }
                currentCall = phone.getBackgroundCall();
            }
            if (currentCall.getConnections().size() > 1) {
                return true;
            }
            if (currentCall.getEarliestConnection() != null) {
                String currentNumber = currentCall.getEarliestConnection().getAddress();
                if (TelephonyManagerEx.isLocalEmergencyNumber(phone.getContext(), currentNumber)) {
                    return true;
                }
                for (String num : specialNumArray) {
                    if (PhoneNumberUtils.compare(num, currentNumber, true)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public static boolean forceUseCsForCall(Phone phone, String dialStr) {
            boolean forceUseCsForCall = false;
            if (phone == null || TextUtils.isEmpty(dialStr)) {
                Rlog.d(MiuiCallControllerImpl.LOG_TAG, "phone is null or dialStr is empty...");
                return false;
            }
            String mccmnc = phone.getOperatorNumeric();
            if (TextUtils.isEmpty(mccmnc)) {
                return false;
            }
            if (mccmnc.startsWith(MCC_CHILE) && !ENTEL_MCCMNC.equals(mccmnc)) {
                boolean z = true;
                boolean isUssd = (dialStr.startsWith("*") || dialStr.startsWith("#")) && dialStr.endsWith("#");
                boolean isVoiceMail = dialStr.equals(phone.getVoiceMailNumber());
                if ((isMovistarSIM(mccmnc) || WOM_MCCMNC.equals(mccmnc)) && isVoiceMail) {
                    Rlog.d(MiuiCallControllerImpl.LOG_TAG, "movistar/WOM/Entel voicemail is dialing");
                    return false;
                }
                if ((isMovistarSIM(mccmnc) || CLARO_MCCMNC.equals(mccmnc)) && TelephonyManagerEx.isLocalEmergencyNumber(phone.getContext(), dialStr)) {
                    Rlog.d(MiuiCallControllerImpl.LOG_TAG, "movistar/claro emergency number is dialing");
                    return false;
                }
                if ((!isServiceNumberForChileOperator(dialStr) || ENTEL_MCCMNC.equals(mccmnc) || CLARO_MCCMNC.equals(mccmnc)) && !isUssd && !isVoiceMail) {
                    z = false;
                }
                forceUseCsForCall = z;
            } else if (isSupportForceCsDial(phone.getPhoneId(), mccmnc)) {
                if (isForceCsDialNumber(phone.getPhoneId(), mccmnc, dialStr)) {
                    forceUseCsForCall = true;
                } else if (isExceptionForceCsDialNumber(phone.getPhoneId(), mccmnc, dialStr)) {
                    forceUseCsForCall = false;
                } else {
                    forceUseCsForCall = isSdn(dialStr, phone);
                }
            }
            Rlog.d(MiuiCallControllerImpl.LOG_TAG, "forceUseCsForCall is " + forceUseCsForCall);
            return forceUseCsForCall;
        }

        private static boolean isMovistarSIM(String mccmnc) {
            for (String movistarMccmnc : MOVISTAR_MCCMNC) {
                if (movistarMccmnc.equals(mccmnc)) {
                    return true;
                }
            }
            return false;
        }

        public static boolean needSendUssdWhenAPM(Phone phone, String dialStr) {
            if (phone == null || TextUtils.isEmpty(dialStr)) {
                Rlog.d(MiuiCallControllerImpl.LOG_TAG, "needSendUssdWhenAPM phone is null or dialStr is empty...");
                return false;
            }
            String mccmnc = phone.getOperatorNumeric();
            Rlog.d(MiuiCallControllerImpl.LOG_TAG, "needSupportOperator mccmnc = " + mccmnc);
            List<String> supportUssdDurringApmPlmn = HyperOSCustFeatureResolve.getStringArray("config_support_ussd_durring_apm_plmn");
            boolean needSupportOperator = ArrayUtils.contains(supportUssdDurringApmPlmn, mccmnc);
            boolean isUssd = (dialStr.startsWith("*") || dialStr.startsWith("#")) && dialStr.endsWith("#");
            ImsPhone imsPhone = phone.getImsPhone();
            boolean forceSendUssd = phone.getServiceStateTracker() != null && phone.getServiceStateTracker().mSS.getState() == 3 && isUssd && imsPhone != null && imsPhone.isWifiCallingEnabled();
            Rlog.d(MiuiCallControllerImpl.LOG_TAG, "ServiceState = " + (phone.getServiceStateTracker() != null ? Integer.valueOf(phone.getServiceStateTracker().mSS.getState()) : "N/A") + "isWifiCallingEnabled = " + (imsPhone != null ? Boolean.valueOf(imsPhone.isWifiCallingEnabled()) : "N/A") + "isUssd = " + isUssd);
            return needSupportOperator && forceSendUssd;
        }

        private static boolean isSdn(final String dialStr, final Phone phone) {
            if (phone == null || TextUtils.isEmpty(dialStr)) {
                return false;
            }
            ExecutorService executor = Executors.newSingleThreadExecutor();
            FutureTask<Boolean> futureTask = new FutureTask<>(new Callable<Boolean>() { // from class: com.android.internal.telephony.MiuiCallControllerImpl.Processor.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Boolean call() {
                    MiuiUiccControllerImpl uiccImpl = (MiuiUiccControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiUiccControllerImpl.class, phone.getPhoneId());
                    MiuiIccPhoneBookInterfaceManager pbm = uiccImpl.getMiuiIccPhoneBookInterfaceManager();
                    List<MiuiAdnRecord> adnList = pbm.getAdnRecordsInEf(28489);
                    if (adnList != null) {
                        for (MiuiAdnRecord adn : adnList) {
                            if (!adn.isEmpty() && dialStr.equals(adn.getNumber())) {
                                return new Boolean(true);
                            }
                        }
                    }
                    return new Boolean(false);
                }
            });
            executor.execute(futureTask);
            try {
                try {
                    Boolean result = futureTask.get(1000L, TimeUnit.MILLISECONDS);
                    if (result != null && result.booleanValue()) {
                        Rlog.d(MiuiCallControllerImpl.LOG_TAG, "calling number is a SDN.");
                        return true;
                    }
                } catch (Exception e) {
                    Rlog.e(MiuiCallControllerImpl.LOG_TAG, "isSDN, exp is ", e);
                    try {
                        futureTask.cancel(true);
                    } catch (Exception e1) {
                        Rlog.e(MiuiCallControllerImpl.LOG_TAG, "isSDN, futureTask.cancel exp is ", e1);
                    }
                }
                return false;
            } finally {
                try {
                    executor.shutdown();
                } catch (Exception e2) {
                    Rlog.e(MiuiCallControllerImpl.LOG_TAG, "isSDN, shutdown exp is ", e2);
                }
            }
        }

        public static int disconnectCauseFromCode(int causeCode) {
            if (MiuiTelephony.IS_QCOM) {
                return disconnectCauseFromCodeForQcom(causeCode);
            }
            if (MiuiTelephony.IS_MTK || MiuiTelephony.IS_XRING) {
                return disconnectCauseFromCodeForMtk(causeCode);
            }
            return -1;
        }

        public static int disconnectCauseFromCodeForQcom(int causeCode) {
            switch (causeCode) {
                case 19:
                    if (!TelephonyManager.isCustForMxTelcel()) {
                        return -1;
                    }
                    return 108;
                default:
                    return -1;
            }
        }

        public static int disconnectCauseFromCodeForMtk(int causeCode) {
            switch (causeCode) {
                case 3:
                    return MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT;
                case 6:
                    return 111;
                case 8:
                    return 106;
                case 18:
                    return 107;
                case 19:
                    if (TelephonyManager.isCustForMxTelcel() || TelephonyManager.isCustForLmClaro()) {
                        return 108;
                    }
                    return 13;
                case 20:
                    return 108;
                case 21:
                    return 112;
                case 22:
                    return ModemServiceMoniter.EVENT_OEMHOOK_NOTIFY_MODEM_SYNC_DONE;
                case 25:
                    return ModemServiceMoniter.EVENT_OEMHOOK_SCREEN_STATUS_NOTIFY_EX;
                case 26:
                    return 126;
                case 27:
                    return 109;
                case 28:
                    return 7;
                case 29:
                    return MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_GET;
                case 30:
                    return MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_SET;
                case 34:
                    return 104;
                case 38:
                    return 117;
                case 41:
                    return 118;
                case 42:
                    return 119;
                case 43:
                    return 120;
                case 44:
                    return 121;
                case 47:
                    return 122;
                case 49:
                    return 123;
                case 50:
                    return ModemUtils.OEM_NV_BYTE_LEN;
                case 55:
                    return 129;
                case 57:
                    return 110;
                case 58:
                    return 83;
                case 63:
                    return 84;
                case 65:
                    return 85;
                case 69:
                    return 86;
                case 70:
                    return 87;
                case 79:
                    return 88;
                case MiRilHookBase.EVENT_OEMHOOK_MTB_CLASS_MASK_GET /* 81 */:
                    return 89;
                case 87:
                    return 90;
                case 88:
                    return 91;
                case 91:
                    return 92;
                case 95:
                    return 93;
                case 96:
                    return 94;
                case 97:
                    return 95;
                case 98:
                    return 96;
                case 99:
                    return 97;
                case 100:
                    return 98;
                case 101:
                    return 99;
                case 102:
                    return 100;
                case 111:
                    return 101;
                case 127:
                    return 102;
                default:
                    return -1;
            }
        }

        public static boolean getVMNumberIgnoreSimRecords(Phone phone, String number) {
            if (phone == null || TextUtils.isEmpty(number)) {
                return false;
            }
            return CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_voicemail_ignore_sim", false);
        }

        public static boolean isUssdRelease(Phone phone, int ussdMode) {
            if (phone == null) {
                return false;
            }
            boolean supportConvUssdMode = HyperOSCustFeatureResolve.getBooleanForPhoneId(phone.getPhoneId(), "support_convert_ussd_mode", false);
            Rlog.d(MiuiCallControllerImpl.LOG_TAG, "isCarrierCustomized() supportConvUssdMode = " + supportConvUssdMode + ", ussdMode = " + ussdMode);
            if (!supportConvUssdMode || ussdMode != 3) {
                return false;
            }
            return true;
        }

        private static boolean isServiceNumberForChileOperator(String dialStr) {
            if (!dialStr.startsWith("1")) {
                return false;
            }
            if (dialStr.length() == 4) {
                return true;
            }
            if (dialStr.length() != 3) {
                return false;
            }
            boolean isServiceNum = dialStr.startsWith("13") || dialStr.startsWith("14");
            return isServiceNum;
        }

        private static boolean isSupportForceCsDial(int phoneId, String mccmnc) {
            boolean isSupportForceCsDial = HyperOSCustFeatureResolve.getBooleanForPhoneId(phoneId, "support_force_cs_dial", false);
            log("isSupportForceCsDial " + isSupportForceCsDial);
            return isSupportForceCsDial;
        }

        private static boolean isForceCsDialNumber(int phoneId, String mccmnc, String dialStr) {
            List<String> numbers = HyperOSCustFeatureResolve.getStringArrayForPhoneId(phoneId, "force_cs_dial_numbers");
            return ArrayUtils.contains(numbers, dialStr);
        }

        private static boolean isExceptionForceCsDialNumber(int phoneId, String mccmnc, String dialStr) {
            List<String> numbers = HyperOSCustFeatureResolve.getStringArrayForPhoneId(phoneId, "exception_force_cs_dial_numbers");
            return ArrayUtils.contains(numbers, dialStr);
        }

        private static void log(String msg) {
            Rlog.d(MiuiCallControllerImpl.LOG_TAG, msg);
        }
    }
}
