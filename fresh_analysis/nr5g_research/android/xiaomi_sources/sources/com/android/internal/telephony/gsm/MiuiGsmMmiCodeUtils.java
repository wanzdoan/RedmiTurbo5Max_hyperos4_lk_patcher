package com.android.internal.telephony.gsm;

import android.os.AsyncResult;
import android.os.Message;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneMmiCode;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import miui.os.Build;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public class MiuiGsmMmiCodeUtils {
    public static final int EVENT_SET_COMPLETE = 1;
    public static final String SC_CLIP = "30";
    public static final String SC_CLIR = "31";
    private static final String SC_CNAP = "300";
    private static final String SC_COLP = "76";
    private static final String SC_COLR = "77";
    private static final String TAG = "MiuiGsmMmiCodeUtils";
    private static String[] facilitys = {"AO", "OI", "OX", "AI", "IR"};
    private static final String[] FACs_HEAD_FOR_VDF_PL_UK = {"#23#", "#45#", "#46#", "#132#", "#112#", "#121#", "#154*", "#155*", "#155#", "#157#", "#21#", "#22#", "#32#", "#51#", "#114#"};

    public interface CallBack {
        void onSetComplete(Message message, AsyncResult asyncResult);
    }

    private MiuiGsmMmiCodeUtils() {
    }

    public static boolean isBlockedMmi(Phone phone, String dialString) {
        boolean bIsBlockedMmi = false;
        if (phone.getPhoneType() == 1 && (phone instanceof GsmCdmaPhone)) {
            int slot = phone.getPhoneId();
            UiccCardApplication cardApp = UiccController.getInstance().getUiccCardApplication(slot, 1);
            String newDialString = PhoneNumberUtils.stripSeparators(dialString);
            String networkPortion = PhoneNumberUtils.extractNetworkPortionAlt(newDialString);
            GsmMmiCode mmi = GsmMmiCode.newFromDialString(networkPortion, (GsmCdmaPhone) phone, cardApp);
            if (mmi != null && !mmi.isTemporaryModeCLIR() && mmi.mSc != null && (mmi.mSc.equals(SC_CNAP) || mmi.mSc.equals(SC_CLIP) || mmi.mSc.equals(SC_CLIR) || mmi.mSc.equals(SC_COLP) || mmi.mSc.equals(SC_COLR) || GsmMmiCode.isServiceCodeCallForwarding(mmi.mSc) || GsmMmiCode.isServiceCodeCallBarring(mmi.mSc) || mmi.mSc.equals("03") || mmi.mSc.equals("43") || mmi.isPinPukCommand())) {
                bIsBlockedMmi = true;
            }
        }
        Log.d(TAG, "isBlockedMmi result=" + bIsBlockedMmi);
        return bIsBlockedMmi;
    }

    public static boolean isCLIREnableForbidden(Phone phone) {
        String numeric = phone.getServiceState().getOperatorNumeric();
        if (TextUtils.isEmpty(numeric)) {
            return false;
        }
        if (numeric.startsWith("404") || numeric.startsWith("405")) {
            return true;
        }
        return false;
    }

    public static void setCallBarring(int what, final GsmMmiCode mmiCode, final CommandsInterface ci, final String facility, boolean lockState, final String password, final int serviceClass, final IMiuiMmiCodeCallBack callBack) {
        final Message message = mmiCode.obtainMessage(what, mmiCode);
        message.setCallback(new Runnable() { // from class: com.android.internal.telephony.gsm.MiuiGsmMmiCodeUtils.1
            @Override // java.lang.Runnable
            public void run() {
                PersistableBundle pb;
                AsyncResult csAr = (AsyncResult) message.obj;
                boolean supportCancelAllBySequence = false;
                CarrierConfigManager configManager = (CarrierConfigManager) mmiCode.getPhone().getContext().getSystemService("carrier_config");
                if (configManager != null && (pb = configManager.getConfigForSubId(mmiCode.getPhone().getSubId())) != null) {
                    supportCancelAllBySequence = pb.getBoolean("config_support_callbarring_cancel_all_by_sequence");
                }
                if (supportCancelAllBySequence && facility.equals("AB") && csAr.exception != null) {
                    Rlog.d(MiuiGsmMmiCodeUtils.TAG, "processCode: set CancelAll by Sequence");
                    MiuiGsmMmiCodeUtils.cancelAllBySequence(mmiCode, ci, 0, password, serviceClass, callBack);
                } else {
                    callBack.onSetComplete(message, csAr);
                }
            }
        });
        Rlog.d(TAG, "processCode: set CB by RIL facility: " + facility + " lockState: " + lockState);
        ci.setFacilityLock(facility, lockState, password, serviceClass, message);
    }

    public static void cancelAllBySequence(final GsmMmiCode mmiCode, final CommandsInterface ci, final int index, final String password, final int serviceClass, final IMiuiMmiCodeCallBack callBack) {
        final Message csMessage = mmiCode.obtainMessage(index, mmiCode);
        csMessage.setCallback(new Runnable() { // from class: com.android.internal.telephony.gsm.MiuiGsmMmiCodeUtils.2
            @Override // java.lang.Runnable
            public void run() {
                AsyncResult csAr = (AsyncResult) csMessage.obj;
                if (csAr.exception != null) {
                    Rlog.d(MiuiGsmMmiCodeUtils.TAG, "processCode: set CB by RIL Error , facility: " + MiuiGsmMmiCodeUtils.facilitys[index] + " lockState: false");
                }
                if (index < MiuiGsmMmiCodeUtils.facilitys.length - 1) {
                    MiuiGsmMmiCodeUtils.cancelAllBySequence(mmiCode, ci, index + 1, password, serviceClass, callBack);
                } else {
                    callBack.onSetComplete(csMessage, csAr);
                }
            }
        });
        Rlog.d(TAG, "processCode: set CB by RIL facility: " + facilitys[index] + " lockState: false");
        ci.setFacilityLock(facilitys[index], false, password, serviceClass, csMessage);
    }

    public static boolean isVodafoneFACs(Phone phone, String dialStr) {
        if (phone == null || TextUtils.isEmpty(dialStr)) {
            Rlog.d(TAG, "isVodafoneFACs phone is null or dialStr is empty...");
            return false;
        }
        String mccmnc = phone.getOperatorNumeric();
        Rlog.d(TAG, "isVodafoneFACs MccMnc = " + mccmnc);
        if (("26801".equals(mccmnc) || "23415".equals(mccmnc)) && dialStr.endsWith("*")) {
            for (String dialStringHEAD : FACs_HEAD_FOR_VDF_PL_UK) {
                if (dialStr.startsWith(dialStringHEAD)) {
                    Rlog.d(TAG, "isVodafoneFACs is true, and Dial string is head with " + dialStringHEAD);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isActivate(GsmMmiCode mmiCode) {
        return mmiCode != null && mmiCode.isActivate();
    }

    public static boolean isDeactivate(GsmMmiCode mmiCode) {
        return mmiCode != null && mmiCode.isDeactivate();
    }

    public static boolean isShortCode(GsmMmiCode mmiCode) {
        return mmiCode != null && mmiCode.isShortCode();
    }

    public static String getServiceCode(GsmMmiCode mmiCode) {
        if (mmiCode != null) {
            return mmiCode.mSc;
        }
        return null;
    }

    public static boolean isServiceCodeCallBarring(GsmMmiCode mmiCode) {
        return GsmMmiCode.isServiceCodeCallBarring(mmiCode.mSc);
    }

    public static boolean isInterrogate(GsmMmiCode mmiCode) {
        return mmiCode != null && mmiCode.isInterrogate();
    }

    public static String getServicePassword(GsmMmiCode mmiCode) {
        if (mmiCode != null) {
            return mmiCode.mSia;
        }
        return null;
    }

    public static int getServiceClass(GsmMmiCode mmiCode) {
        if (mmiCode.mSib == null || mmiCode.mSib.length() == 0) {
            return 0;
        }
        int serviceCode = Integer.parseInt(mmiCode.mSib, 10);
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
                throw new RuntimeException("unsupported MMI service code " + mmiCode.mSib);
        }
    }

    public static String getFacility(GsmMmiCode mmiCode) {
        if (mmiCode.mSc == null) {
            throw new RuntimeException("invalid call barring sc");
        }
        if (mmiCode.mSc.equals("33")) {
            return "AO";
        }
        if (mmiCode.mSc.equals("331")) {
            return "OI";
        }
        if (mmiCode.mSc.equals("332")) {
            return "OX";
        }
        if (mmiCode.mSc.equals("35")) {
            return "AI";
        }
        if (mmiCode.mSc.equals("351")) {
            return "IR";
        }
        if (mmiCode.mSc.equals("330")) {
            return "AB";
        }
        if (mmiCode.mSc.equals("333")) {
            return "AG";
        }
        if (mmiCode.mSc.equals("353")) {
            return "AC";
        }
        throw new RuntimeException("invalid call barring sc");
    }

    public static void updateMmiCodeState(GsmMmiCode mmiCode, MmiCode.State state) {
        mmiCode.mState = state;
    }

    public static void updateMmiCodeState(GsmMmiCode mmiCode, CharSequence message) {
        mmiCode.mMessage = message;
    }

    public static ImsPhoneMmiCode getMmiCodeForKrOperator(Phone phone, String dialString, ImsPhoneMmiCode ret, boolean isShortCode) {
        if (isCustForKrAndKrSimInserted(phone) && !TextUtils.isEmpty(dialString)) {
            if (dialString.contains("*23#") || dialString.contains("*230#")) {
                return null;
            }
            if (dialString.endsWith("#") && !isShortCode && ret != null && !ret.isPinPukCommand()) {
                return null;
            }
        }
        return ret;
    }

    public static boolean isCustForKrAndKrSimInserted(Phone phone) {
        return (TelephonyManager.isCustForKrOps() || Build.IS_INTERNATIONAL_BUILD) && isKrSim(phone.getPhoneId());
    }

    public static boolean isKrSim(int phoneId) {
        String numeric = TelephonyManager.getDefault().getSimOperatorForSlot(phoneId);
        return "45008".equals(numeric) || "45005".equals(numeric) || "45006".equals(numeric);
    }

    public static boolean isTwSim(int phoneId) {
        String numeric = TelephonyManager.getDefault().getSimOperatorForSlot(phoneId);
        return "46601".equals(numeric) || "46602".equals(numeric) || "46603".equals(numeric);
    }

    public static int updateCfnryTime(int reason, Phone phone, int time) {
        Log.d(TAG, "updateCfnryTime time = " + time);
        if (!isTwSim(phone.getPhoneId())) {
            return time;
        }
        boolean isImsPhone = phone instanceof ImsPhone;
        int time2 = isImsPhone ? 40 : 20;
        Log.d(TAG, "updateCfnryTime after time = " + time2);
        return time2;
    }
}
