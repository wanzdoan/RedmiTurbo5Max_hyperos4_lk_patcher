package com.android.internal.telephony.imsphone;

import android.content.Intent;
import android.os.AsyncResult;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.provider.Settings;
import android.telephony.CarrierConfigManager;
import android.telephony.Rlog;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsReasonInfo;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Phone;
import miui.os.Build;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.PhoneNumberUtils;
import miui.telephony.ServiceProviderUtils;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;
import miui.telephony.phonenumber.CountryCode;
import miui.util.FeatureParser;

/* JADX INFO: loaded from: classes.dex */
public class MiuiImsPhoneUtils {
    public static final boolean IS_QCOM = "qcom".equals(FeatureParser.getString("vendor"));
    private static final String LOG_TAG = "MiuiImsPhoneUtils";
    private static int RIL_RADIO_TECHNOLOGY_LTE_CA;
    private static String[] facilitys;

    private MiuiImsPhoneUtils() {
    }

    static {
        RIL_RADIO_TECHNOLOGY_LTE_CA = 19;
        if ("mediatek".equals(FeatureParser.getString("vendor"))) {
            RIL_RADIO_TECHNOLOGY_LTE_CA = 139;
        }
        facilitys = new String[]{"AO", "OI", "OX", "AI", "IR"};
    }

    public static boolean isRatLTE(Phone phone) {
        int rilDataRadioTech = phone.getServiceState().getRilDataRadioTechnology();
        int rilVoiceRadioTech = phone.getServiceState().getRilVoiceRadioTechnology();
        if (rilDataRadioTech == 14 || rilDataRadioTech == RIL_RADIO_TECHNOLOGY_LTE_CA || rilVoiceRadioTech == 14 || rilVoiceRadioTech == RIL_RADIO_TECHNOLOGY_LTE_CA) {
            return true;
        }
        return false;
    }

    public static boolean isRatNr(Phone phone) {
        int rilDataRadioTech = phone.getServiceState().getRilDataRadioTechnology();
        int rilVoiceRadioTech = phone.getServiceState().getRilVoiceRadioTechnology();
        if (rilDataRadioTech == 20 || rilVoiceRadioTech == 20) {
            return true;
        }
        return false;
    }

    private static boolean isChinaTelecomSim(Phone phone) {
        if (phone == null) {
            return false;
        }
        return ServiceProviderUtils.isChinaTelecom(phone.getOperatorNumeric());
    }

    public static boolean isCarrierConfigedLocalTone(Phone phone) {
        if (!Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        boolean config = CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_volte_play_local_hold_tone");
        return config;
    }

    public static boolean isCarrierConfigedBusyTone(Phone phone) {
        CarrierConfigManager configLoader = (CarrierConfigManager) phone.getContext().getSystemService("carrier_config");
        if (configLoader == null) {
            return false;
        }
        try {
            PersistableBundle b = configLoader.getConfigForSubId(phone.getSubId());
            if (b == null) {
                return false;
            }
            boolean needPlayBusyTone = b.getBoolean("config_receive_sip_error_play_busy_tone", false);
            return needPlayBusyTone;
        } catch (Exception e) {
            Rlog.e(LOG_TAG, "isCarrierConfigedBusyTone: carrier config error: " + e.toString());
            return false;
        }
    }

    public static void broadcastCallAutoReject(Phone phone, ImsCallProfile callProfile, ImsReasonInfo reason) {
        int callType = callProfile.getCallType();
        String number = callProfile.getCallExtra("oi");
        int failCause = reason.mCode;
        Intent intent = new Intent("miui.intent.action.GWSD_AUTO_REJECT");
        SubscriptionManager.putSlotIdPhoneIdAndSubIdExtra(intent, phone.getPhoneId(), phone.getPhoneId(), phone.getSubId());
        intent.putExtra("extra_callType", callType);
        intent.putExtra("extra_number", number);
        intent.putExtra("extra_failCause", failCause);
        Rlog.i(LOG_TAG, "broadcastCallAutoReject phoneId = " + phone.getPhoneId() + " , callType = " + callType + " , failCause = " + failCause);
        phone.getContext().sendBroadcastAsUser(intent, UserHandle.ALL);
    }

    public static boolean isGwsdMode(Phone phone) {
        boolean isGwsd = Settings.Secure.getIntForUser(phone.getContext().getContentResolver(), "gb_gwsd", 0, -2) == 1;
        Rlog.d(LOG_TAG, "isGwsdMode " + isGwsd);
        return isGwsd;
    }

    public static boolean shouldNotifyAutoRejectedCall(Phone phone, ImsReasonInfo imsReasonInfo) {
        return TelephonyManager.isViceImsAutoRejectSupport() && isGwsdMode(phone);
    }

    public static String getEffectiveNumber(Phone phone, String formattedNumber) {
        PhoneNumberUtils.PhoneNumber pn = PhoneNumberUtils.PhoneNumber.parse(formattedNumber, "86".equals(CountryCode.getNetworkCountryCode()));
        return pn.getEffectiveNumber();
    }

    public static int getClirModeForEmergencyCall(int clirMode, Phone mPhone, boolean isEmergencyNumber, int defaultClirMode) {
        String simOperator = TelephonyManager.getDefault().getSimOperatorForSlot(mPhone.getPhoneId());
        Rlog.d(LOG_TAG, " simOperator:" + simOperator + "defaultClirMode:" + defaultClirMode);
        if (IS_QCOM && "44020".equals(simOperator) && isEmergencyNumber && defaultClirMode == 1) {
            return 1;
        }
        return clirMode;
    }

    public static boolean needExchangeCallsDuringECT(Phone mPhone) {
        String simOperator = TelephonyManager.getDefault().getSimOperatorForSlot(mPhone.getPhoneId());
        Rlog.i(LOG_TAG, "needExchangeCallsDuringECT simOperator:" + simOperator + " ; IS_QCOM = " + IS_QCOM);
        if (IS_QCOM && "21407".equals(simOperator)) {
            return true;
        }
        return false;
    }

    public static boolean isCarrierSupportCbOverIms(Phone phone) {
        CarrierConfigManager configManager = (CarrierConfigManager) phone.getContext().getSystemService("carrier_config");
        PersistableBundle pb = configManager.getConfigForSubId(phone.getSubId());
        return isCarrierSupportCbOverIms(pb);
    }

    public static boolean isCarrierSupportCbOverIms(PersistableBundle pb) {
        if (pb == null) {
            return true;
        }
        boolean isOverIms = pb.getBoolean("config_support_callbarring_over_ims", true);
        boolean isCarrierSupportVolte = pb.getBoolean("carrier_volte_available_bool");
        return isCarrierSupportVolte && isOverIms;
    }

    public static void getCallBarring(final int what, final ImsPhoneMmiCode imsPhoneMmiCode, ImsPhone imsPhone, final CommandsInterface ci, final String facility, final String password, final int serviceClass, final IMiuiImsMmiCodeCallBack callBack) {
        final Message message = imsPhoneMmiCode.obtainMessage(what, imsPhoneMmiCode);
        message.setCallback(new Runnable() { // from class: com.android.internal.telephony.imsphone.MiuiImsPhoneUtils.1
            @Override // java.lang.Runnable
            public void run() {
                AsyncResult ar = (AsyncResult) message.obj;
                if (ar.exception != null) {
                    Rlog.d(MiuiImsPhoneUtils.LOG_TAG, "processCode: get CB by IMS Error , Try RIL");
                    ci.queryFacilityLock(facility, password, serviceClass, imsPhoneMmiCode.obtainMessage(what, imsPhoneMmiCode));
                } else {
                    callBack.onSuppSvcQueryComplete(ar);
                }
            }
        });
        imsPhone.getCallBarring(facility, message, serviceClass);
    }

    public static void setCallBarring(final int what, final ImsPhoneMmiCode imsPhoneMmiCode, final ImsPhone imsPhone, final CommandsInterface ci, final String facility, final boolean lockState, final String password, final int serviceClass, final IMiuiImsMmiCodeCallBack callBack) {
        final Message psMessage = imsPhoneMmiCode.obtainMessage(what, imsPhoneMmiCode);
        psMessage.setCallback(new Runnable() { // from class: com.android.internal.telephony.imsphone.MiuiImsPhoneUtils.2
            @Override // java.lang.Runnable
            public void run() {
                AsyncResult psAr = (AsyncResult) psMessage.obj;
                if (psAr.exception != null) {
                    final Message csMessage = imsPhoneMmiCode.obtainMessage(what, imsPhoneMmiCode);
                    csMessage.setCallback(new Runnable() { // from class: com.android.internal.telephony.imsphone.MiuiImsPhoneUtils.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            PersistableBundle pb;
                            AsyncResult csAr = (AsyncResult) csMessage.obj;
                            boolean supportCancelAllBySequence = false;
                            CarrierConfigManager configManager = (CarrierConfigManager) imsPhone.getContext().getSystemService("carrier_config");
                            if (configManager != null && (pb = configManager.getConfigForSubId(imsPhone.getSubId())) != null) {
                                supportCancelAllBySequence = pb.getBoolean("config_support_callbarring_cancel_all_by_sequence");
                            }
                            if (supportCancelAllBySequence && facility.equals("AB") && csAr.exception != null) {
                                Rlog.d(MiuiImsPhoneUtils.LOG_TAG, "processCode: set CancelAll by Sequence");
                                MiuiImsPhoneUtils.cancelAllBySequence(imsPhoneMmiCode, imsPhone, ci, 0, password, serviceClass, callBack);
                            } else {
                                callBack.onSetComplete(csMessage, csAr);
                            }
                        }
                    });
                    Rlog.d(MiuiImsPhoneUtils.LOG_TAG, "processCode: set CB by IMS Error , Try RIL facility: " + facility + " lockState:" + lockState);
                    ci.setFacilityLock(facility, lockState, password, serviceClass, csMessage);
                    return;
                }
                callBack.onSetComplete(psMessage, psAr);
            }
        });
        Rlog.d(LOG_TAG, "processCode: set CB by IMS facility: " + facility + " lockState:" + lockState);
        imsPhone.setCallBarring(facility, lockState, password, psMessage, serviceClass);
    }

    public static void cancelAllBySequence(final ImsPhoneMmiCode imsPhoneMmiCode, final ImsPhone imsPhone, final CommandsInterface ci, final int index, final String password, final int serviceClass, final IMiuiImsMmiCodeCallBack callBack) {
        final Message psMessage = imsPhoneMmiCode.obtainMessage(index, imsPhoneMmiCode);
        psMessage.setCallback(new Runnable() { // from class: com.android.internal.telephony.imsphone.MiuiImsPhoneUtils.3
            @Override // java.lang.Runnable
            public void run() {
                AsyncResult psAr = (AsyncResult) psMessage.obj;
                if (psAr.exception != null) {
                    final Message csMessage = imsPhoneMmiCode.obtainMessage(index, imsPhoneMmiCode);
                    csMessage.setCallback(new Runnable() { // from class: com.android.internal.telephony.imsphone.MiuiImsPhoneUtils.3.1
                        @Override // java.lang.Runnable
                        public void run() {
                            AsyncResult csAr = (AsyncResult) csMessage.obj;
                            if (csAr.exception != null) {
                                Rlog.d(MiuiImsPhoneUtils.LOG_TAG, "processCode: set CB by RIL Error , facility: " + MiuiImsPhoneUtils.facilitys[index] + " lockState: false");
                            }
                            int i = index;
                            int length = MiuiImsPhoneUtils.facilitys.length - 1;
                            AnonymousClass3 anonymousClass3 = AnonymousClass3.this;
                            if (i < length) {
                                MiuiImsPhoneUtils.cancelAllBySequence(imsPhoneMmiCode, imsPhone, ci, index + 1, password, serviceClass, callBack);
                            } else {
                                callBack.onSetComplete(csMessage, csAr);
                            }
                        }
                    });
                    Rlog.d(MiuiImsPhoneUtils.LOG_TAG, "processCode: set CB by IMS Error , Try RIL facility: " + MiuiImsPhoneUtils.facilitys[index] + " lockState: false");
                    ci.queryFacilityLock(MiuiImsPhoneUtils.facilitys[index], password, serviceClass, csMessage);
                    return;
                }
                if (index < MiuiImsPhoneUtils.facilitys.length - 1) {
                    MiuiImsPhoneUtils.cancelAllBySequence(imsPhoneMmiCode, imsPhone, ci, index + 1, password, serviceClass, callBack);
                } else {
                    callBack.onSetComplete(psMessage, psAr);
                }
            }
        });
        Rlog.d(LOG_TAG, "processCode: set CB by IMS facility: " + facilitys[index] + " lockState: false");
        imsPhone.setCallBarring(facilitys[index], false, password, psMessage, serviceClass);
    }

    public static boolean isCustForKrAndKrSimInserted() {
        return TelephonyManager.getDefault().isCustForKrAndKrSimInserted();
    }

    public static int siToServiceClass(String si) {
        if (si == null || si.length() == 0) {
            return 0;
        }
        int serviceCode = Integer.parseInt(si, 10);
        switch (serviceCode) {
            case 10:
                return 13;
            case 11:
                return 1;
            case 12:
                return 12;
            case 13:
                return 4;
            case 16:
                return 8;
            case 19:
                return 5;
            case 20:
                return 48;
            case 21:
                return 160;
            case 22:
                return 80;
            case 24:
                return 16;
            case 25:
                return 32;
            case 26:
                return 17;
            case 99:
                return 64;
            default:
                throw new RuntimeException("unsupported MMI service code " + si);
        }
    }
}
