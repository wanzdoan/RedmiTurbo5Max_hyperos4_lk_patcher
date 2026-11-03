package com.android.services.telephony;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public abstract class MmiCodeUtil {
    static Pattern sPatternSuppService = Pattern.compile("((\\*|#|\\*#|\\*\\*|##)(\\d{2,3})(\\*([^*#]*)(\\*([^*#]*)(\\*([^*#]*)(\\*([^*#]*))?)?)?)?#)(.*)");

    public static String getMmiServiceCode(String str) {
        Matcher matcher = sPatternSuppService.matcher(str);
        if (matcher.matches()) {
            return makeEmptyNull(matcher.group(3));
        }
        return null;
    }

    private static String makeEmptyNull(String str) {
        if (str == null || str.length() != 0) {
            return str;
        }
        return null;
    }

    static boolean isServiceCodeCallForwarding(String str) {
        if (str != null) {
            return str.equals("21") || str.equals("67") || str.equals("61") || str.equals("62") || str.equals("002") || str.equals("004");
        }
        return false;
    }

    static boolean isServiceCodeCallBarring(String str) {
        if (str != null) {
            return str.equals("33") || str.equals("331") || str.equals("332") || str.equals("35") || str.equals("351") || str.equals("330") || str.equals("333") || str.equals("353");
        }
        return false;
    }

    static boolean isPinPukCommand(String str) {
        if (str != null) {
            return str.equals("04") || str.equals("042") || str.equals("05") || str.equals("052");
        }
        return false;
    }

    public static String getSuppServiceKey(String str) {
        String mmiServiceCode = getMmiServiceCode(str);
        if (mmiServiceCode != null && mmiServiceCode.equals("30")) {
            return "";
        }
        if (mmiServiceCode != null && mmiServiceCode.equals("31")) {
            return "button_clir_key";
        }
        if (isServiceCodeCallForwarding(mmiServiceCode)) {
            return "call_forwarding_key";
        }
        if (isServiceCodeCallBarring(mmiServiceCode)) {
            return "call_barring_key";
        }
        if (mmiServiceCode != null && mmiServiceCode.equals("03")) {
            return "";
        }
        if (mmiServiceCode != null && mmiServiceCode.equals("43")) {
            return "button_cw_key";
        }
        if (isPinPukCommand(mmiServiceCode)) {
            return "";
        }
        return null;
    }
}
