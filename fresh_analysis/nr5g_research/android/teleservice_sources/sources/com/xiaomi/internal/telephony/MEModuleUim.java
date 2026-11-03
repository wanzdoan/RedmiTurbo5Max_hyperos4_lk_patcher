package com.xiaomi.internal.telephony;

import android.content.Context;
import android.os.SystemProperties;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import miuix.animation.internal.TransitionInfo;

/* JADX INFO: loaded from: classes.dex */
public class MEModuleUim {
    private static final String LOG_TAG = "Modem_MEModuleUim";
    private static final String PROPERTY_SIM_STATE = "gsm.sim.state";
    private static MEModuleUim sIntance;
    private Context mContext;
    private SubscriptionManager mSubscriptionManager;
    private static final String[] AUTH_DATA = {"1032353734363430323537343634304900100772615645BD8000322415077261D645", "1032393534333238323935343332384900100777675E45B18000322817077767DE45", "10D5770C6D363E30C364A4078F1BF8ED3A1021737066CFD7D555943045235F97B1B6", "10FFFE0C6D363E30C364A4078F1BF8D577106E323B36C46CAAABFFFF0E6E323B9C6F", "10373837353633343337383735363334331099E201480CE19001FE2D1106CBE6D8ED"};
    public static boolean isAlreadyAuthed = false;

    public MEModuleUim(Context context) {
        this.mContext = context;
        if (context == null) {
            MELog.e(LOG_TAG, "Constructor, mContext is null");
        } else {
            this.mSubscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
            sIntance = this;
        }
    }

    public static MEModuleUim getInstance() {
        return sIntance;
    }

    public boolean startTestCardAuth() {
        List simState = getSimState();
        if (simState == null) {
            MELog.d(LOG_TAG, "sim_state is null");
            return false;
        }
        for (int i = 0; i < 2; i++) {
            if (((String) simState.get(i)).equals("LOADED")) {
                MELog.d(LOG_TAG, "SIM card in slot " + i + " is in the LOADED state");
                List<SubscriptionInfo> availableSubscriptionInfoList = this.mSubscriptionManager.getAvailableSubscriptionInfoList();
                if (availableSubscriptionInfoList == null) {
                    MELog.d(LOG_TAG, "sis is null");
                    return false;
                }
                for (SubscriptionInfo subscriptionInfo : availableSubscriptionInfoList) {
                    if (subscriptionInfo.getSimSlotIndex() == i) {
                        int subscriptionId = subscriptionInfo.getSubscriptionId();
                        MELog.d(LOG_TAG, "sub id =" + subscriptionId);
                        int i2 = 0;
                        while (true) {
                            String[] strArr = AUTH_DATA;
                            if (i2 >= strArr.length) {
                                break;
                            }
                            boolean zRequestSimAuth = requestSimAuth(strArr[i2], subscriptionId);
                            MELog.d(LOG_TAG, "AUTH_DATA  = " + strArr[i2]);
                            if (zRequestSimAuth) {
                                MELog.d(LOG_TAG, "test card auth success!" + zRequestSimAuth);
                                isAlreadyAuthed = true;
                                return true;
                            }
                            i2++;
                        }
                    }
                }
            }
        }
        isAlreadyAuthed = true;
        return false;
    }

    public boolean requestSimAuth(String str, int i) {
        String strBase64Encode = base64Encode(str);
        MELog.d(LOG_TAG, "requestSimAuth after convert encodeData =" + strBase64Encode);
        TelephonyManager telephonyManagerFrom = TelephonyManager.from(this.mContext);
        String iccAuthentication = telephonyManagerFrom.getIccAuthentication(i, 2, 129, strBase64Encode);
        MELog.d(LOG_TAG, "sub id =" + telephonyManagerFrom.getSubscriptionId());
        MELog.d(LOG_TAG, "requestSimAuth rsp =" + iccAuthentication);
        if (TextUtils.isEmpty(iccAuthentication)) {
            return false;
        }
        MELog.d(LOG_TAG, "requestSimAuth base64Decode(rsp) =" + base64Decode(iccAuthentication));
        return true;
    }

    private String base64Encode(String str) {
        if (TextUtils.isEmpty(str)) {
            MELog.d(LOG_TAG, "base64Encode, input is empty, input =" + str);
            return "";
        }
        byte[] bArrHexStringToByteArray = hexStringToByteArray(str, 0, str.length());
        MELog.d(LOG_TAG, "base64Encode textByte =" + bArrHexStringToByteArray);
        return Base64.encodeToString(bArrHexStringToByteArray, 2);
    }

    private String base64Decode(String str) {
        if (TextUtils.isEmpty(str)) {
            MELog.d(LOG_TAG, "base64Encode, input is empty, input =" + str);
            return "";
        }
        StringBuilder sb = new StringBuilder("base64Decode decode text =");
        Charset charset = StandardCharsets.UTF_8;
        sb.append(Base64.decode(str.getBytes(charset), 0));
        MELog.d(LOG_TAG, sb.toString());
        return byteArrayToHexString(Base64.decode(str.getBytes(charset), 0));
    }

    public static byte[] hexStringToByteArray(String str, int i, int i2) {
        if (i2 % 2 != 0) {
            return null;
        }
        String upperCase = str.toUpperCase();
        byte[] bArr = new byte[upperCase.length() / 2];
        for (int i3 = 0; i3 < i2; i3 += 2) {
            char cCharAt = upperCase.charAt(i3 + i);
            char cCharAt2 = upperCase.charAt(i3 + 1 + i);
            if (!isHexChar(cCharAt) || !isHexChar(cCharAt2)) {
                return null;
            }
            bArr[i3 / 2] = (byte) ((Character.digit(cCharAt, 16) << 4) + Character.digit(cCharAt2, 16));
        }
        return bArr;
    }

    public static String byteArrayToHexString(byte[] bArr) {
        if (bArr == null) {
            return "";
        }
        return byteArrayToHexString(bArr, 0, bArr.length);
    }

    public static String byteArrayToHexString(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i3 = 0; i3 < i2; i3++) {
            sb.append(String.format("%02x", Integer.valueOf(bArr[i + i3] & TransitionInfo.INIT)));
        }
        return sb.toString();
    }

    public static boolean isHexChar(char c) {
        if (Character.isLowerCase(c)) {
            c = Character.toUpperCase(c);
        }
        if (c < '0' || c > '9') {
            return c >= 'A' && c <= 'F';
        }
        return true;
    }

    private List getSimState() {
        try {
            String str = SystemProperties.get(PROPERTY_SIM_STATE, (String) null);
            if (str == null) {
                MELog.d(LOG_TAG, "get sim states null");
                return null;
            }
            ArrayList arrayList = new ArrayList(Arrays.asList(str.split(",")));
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                MELog.d(LOG_TAG, "sim states = " + ((String) obj));
            }
            return arrayList;
        } catch (Exception unused) {
            MELog.e(LOG_TAG, "get sim states failed");
            return null;
        }
    }
}
