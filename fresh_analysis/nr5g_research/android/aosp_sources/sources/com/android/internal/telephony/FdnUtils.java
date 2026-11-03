package com.android.internal.telephony;

import android.text.TextUtils;
import com.android.i18n.phonenumbers.NumberParseException;
import com.android.i18n.phonenumbers.PhoneNumberUtil;
import com.android.i18n.phonenumbers.Phonenumber;
import com.android.internal.telephony.uicc.AdnRecord;
import com.android.internal.telephony.uicc.AdnRecordCache;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccProfile;
import com.android.telephony.Rlog;
import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;

/* JADX INFO: loaded from: classes.dex */
public class FdnUtils {
    private static final String LOG_TAG = "FdnUtils";

    public static boolean isNumberBlockedByFDN(int i, String str, String str2) {
        if (isFdnEnabled(i) && !((IHyperSsManager) HyperTelephonyFactory.getInstance().initModule(IHyperSsManager.DEFAULT, new Object[0])).isSkipFdnCheckForUssd(i, str)) {
            return !isFDN(str, str2, getFdnList(i));
        }
        return false;
    }

    public static boolean isFdnEnabled(int i) {
        UiccCardApplication uiccCardApplication = getUiccCardApplication(i);
        if (uiccCardApplication == null || !uiccCardApplication.getIccFdnAvailable()) {
            return false;
        }
        return uiccCardApplication.getIccFdnEnabled();
    }

    public static boolean isSuppServiceRequestBlockedByFdn(int i, ArrayList<String> arrayList, String str) {
        if (!isFdnEnabled(i)) {
            return false;
        }
        ArrayList fdnList = getFdnList(i);
        int size = arrayList.size();
        int i2 = 0;
        while (i2 < size) {
            String str2 = arrayList.get(i2);
            i2++;
            if (isFDN(str2, str, fdnList)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isFDN(String str, String str2, ArrayList<AdnRecord> arrayList) {
        String str3;
        if (arrayList == null || arrayList.isEmpty() || TextUtils.isEmpty(str)) {
            Rlog.w(LOG_TAG, "isFDN: unexpected null value");
            return false;
        }
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        String strValueOf = null;
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(str, str2);
            str3 = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
            try {
                strValueOf = String.valueOf(phoneNumberUtil.getNationalSignificantNumber(phoneNumber));
            } catch (NumberParseException unused) {
                Rlog.w(LOG_TAG, "isFDN: could not parse dialStr");
                str = extractSMSC(str);
            }
        } catch (NumberParseException unused2) {
            str3 = null;
        }
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            AdnRecord adnRecord = arrayList.get(i);
            i++;
            String number = adnRecord.getNumber();
            if (!TextUtils.isEmpty(number)) {
                if (!TextUtils.isEmpty(str3) && str3.startsWith(number)) {
                    return true;
                }
                if ((!TextUtils.isEmpty(strValueOf) && strValueOf.startsWith(number)) || str.startsWith(number)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static ArrayList getFdnList(int i) {
        IccRecords iccRecords;
        AdnRecordCache adnCache;
        UiccCardApplication uiccCardApplication = getUiccCardApplication(i);
        if (uiccCardApplication == null || (iccRecords = uiccCardApplication.getIccRecords()) == null || (adnCache = iccRecords.getAdnCache()) == null) {
            return null;
        }
        return ((IHyperSsManager) HyperTelephonyFactory.getInstance().initModule(IHyperSsManager.DEFAULT, new Object[0])).getAllFdnList(adnCache, i);
    }

    private static UiccCardApplication getUiccCardApplication(int i) {
        UiccProfile uiccProfileForPhone = UiccController.getInstance().getUiccProfileForPhone(i);
        if (uiccProfileForPhone == null) {
            return null;
        }
        return uiccProfileForPhone.getApplication(1);
    }

    private static String extractSMSC(String str) {
        String[] strArrSplit;
        try {
            if (str.contains(",")) {
                strArrSplit = str.split(",");
            } else {
                strArrSplit = str.contains("@") ? str.split("@") : null;
            }
            if (strArrSplit == null || strArrSplit.length < 1) {
                return str;
            }
            if (strArrSplit[0].contains("\"")) {
                strArrSplit[0] = strArrSplit[0].replaceAll("\"", "");
            }
            return strArrSplit[0];
        } catch (PatternSyntaxException e) {
            Rlog.w(LOG_TAG, "extractSMSC: Could not extract number from dialStr " + e);
            return str;
        }
    }
}
