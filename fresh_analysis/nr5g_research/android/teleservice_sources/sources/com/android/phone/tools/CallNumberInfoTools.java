package com.android.phone.tools;

import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import com.aios.apptoolsdk.AppResult;
import com.android.i18n.phonenumbers.NumberParseException;
import com.android.i18n.phonenumbers.PhoneNumberUtil;
import com.android.i18n.phonenumbers.Phonenumber;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.ot.pubsub.g.l;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public abstract class CallNumberInfoTools {
    public static AppResult getCallerInfo(Context context) {
        String upperCase;
        String displayCountry;
        PhoneAndCall phoneAndCallFindActiveOrRingingCall = findActiveOrRingingCall();
        if (phoneAndCallFindActiveOrRingingCall == null) {
            return AppResult.error("当前没有进行中或响铃中的通话");
        }
        Connection earliestConnection = phoneAndCallFindActiveOrRingingCall.call.getEarliestConnection();
        if (earliestConnection == null) {
            return AppResult.error("无法获取通话连接");
        }
        String address = earliestConnection.getAddress();
        if (address == null || address.isEmpty()) {
            return AppResult.error("号码为空，可能是隐私号码或未知来电");
        }
        String usernameFromUriNumber = PhoneNumberUtils.getUsernameFromUriNumber(address);
        if (usernameFromUriNumber != null && !usernameFromUriNumber.isEmpty()) {
            address = usernameFromUriNumber;
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (telephonyManager != null && telephonyManager.getNetworkCountryIso() != null) {
            upperCase = telephonyManager.getNetworkCountryIso().toUpperCase();
        } else {
            upperCase = l.a;
        }
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(address, upperCase);
            if (!phoneNumberUtil.isValidNumber(phoneNumber)) {
                return AppResult.error("无效号码：".concat(address));
            }
            int countryCode = phoneNumber.getCountryCode();
            String regionCodeForNumber = phoneNumberUtil.getRegionCodeForNumber(phoneNumber);
            if (regionCodeForNumber == null || regionCodeForNumber.equals("ZZ") || (displayCountry = new Locale("", regionCodeForNumber).getDisplayCountry(Locale.CHINA)) == null || displayCountry.isEmpty()) {
                displayCountry = "未知";
            }
            StringBuilder sb = new StringBuilder("{\"number\":\"");
            sb.append(ToolUtils.escapeJson(address));
            sb.append("\",\"country_code\":");
            sb.append(countryCode);
            sb.append(",\"country_name\":\"");
            sb.append(ToolUtils.escapeJson(displayCountry));
            sb.append("\",\"region_code\":\"");
            if (regionCodeForNumber == null) {
                regionCodeForNumber = "";
            }
            sb.append(ToolUtils.escapeJson(regionCodeForNumber));
            sb.append("\"}");
            return AppResult.success(sb.toString());
        } catch (NumberParseException e) {
            return AppResult.error("号码解析失败：" + address + "（" + e.getMessage() + "）");
        }
    }

    class PhoneAndCall {
        final Call call;

        PhoneAndCall(Call call) {
            this.call = call;
        }
    }

    private static PhoneAndCall findActiveOrRingingCall() {
        PhoneAndCall phoneAndCall = null;
        for (Phone imsPhone : PhoneFactory.getPhones()) {
            if (imsPhone.getImsPhone() != null) {
                imsPhone = imsPhone.getImsPhone();
            }
            Call.State state = imsPhone.getRingingCall().getState();
            if (state == Call.State.INCOMING || state == Call.State.WAITING) {
                return new PhoneAndCall(imsPhone.getRingingCall());
            }
            Call.State state2 = imsPhone.getForegroundCall().getState();
            if (state2 == Call.State.ACTIVE || state2.isDialing()) {
                phoneAndCall = new PhoneAndCall(imsPhone.getForegroundCall());
            }
        }
        return phoneAndCall;
    }
}
