package com.xiaomi.internal.telephony;

import android.content.Context;
import android.os.SystemProperties;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import com.xiaomi.modem.ModemUtils;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        this.mContext = null;
        this.mContext = context;
        if (this.mContext == null) {
            MELog.e(LOG_TAG, "Constructor, mContext is null");
        } else {
            this.mSubscriptionManager = (SubscriptionManager) this.mContext.getSystemService(SubscriptionManager.class);
            sIntance = this;
        }
    }

    public static MEModuleUim getInstance() {
        return sIntance;
    }

    public boolean startTestCardAuth() {
        List<String> sim_state = getSimState();
        if (sim_state == null) {
            MELog.d(LOG_TAG, "sim_state is null");
            return false;
        }
        for (int i = 0; i < 2; i++) {
            String simState = sim_state.get(i);
            if (simState.equals("LOADED")) {
                MELog.d(LOG_TAG, "SIM card in slot " + i + " is in the LOADED state");
                List<SubscriptionInfo> sis = this.mSubscriptionManager.getAvailableSubscriptionInfoList();
                if (sis == null) {
                    MELog.d(LOG_TAG, "sis is null");
                    return false;
                }
                for (SubscriptionInfo subInfo : sis) {
                    if (subInfo.getSimSlotIndex() == i) {
                        int subId = subInfo.getSubscriptionId();
                        MELog.d(LOG_TAG, "sub id =" + subId);
                        for (int j = 0; j < AUTH_DATA.length; j++) {
                            boolean test_card_auth_success = requestSimAuth(AUTH_DATA[j], subId);
                            MELog.d(LOG_TAG, "AUTH_DATA  = " + AUTH_DATA[j]);
                            if (test_card_auth_success) {
                                MELog.d(LOG_TAG, "test card auth success!" + test_card_auth_success);
                                isAlreadyAuthed = true;
                                return true;
                            }
                        }
                    }
                }
            }
        }
        isAlreadyAuthed = true;
        return false;
    }

    public boolean requestSimAuth(String data, int subId) {
        String encodeData = base64Encode(data);
        MELog.d(LOG_TAG, "requestSimAuth after convert encodeData =" + encodeData);
        TelephonyManager telephonyManager = TelephonyManager.from(this.mContext);
        String rsp = telephonyManager.getIccAuthentication(subId, 2, 129, encodeData);
        MELog.d(LOG_TAG, "sub id =" + telephonyManager.getSubscriptionId());
        MELog.d(LOG_TAG, "requestSimAuth rsp =" + rsp);
        if (!TextUtils.isEmpty(rsp)) {
            MELog.d(LOG_TAG, "requestSimAuth base64Decode(rsp) =" + base64Decode(rsp));
            return true;
        }
        return false;
    }

    private String base64Encode(String input) {
        if (TextUtils.isEmpty(input)) {
            MELog.d(LOG_TAG, "base64Encode, input is empty, input =" + input);
            return "";
        }
        byte[] textByte = hexStringToByteArray(input, 0, input.length());
        MELog.d(LOG_TAG, "base64Encode textByte =" + textByte);
        return Base64.encodeToString(textByte, 2);
    }

    private String base64Decode(String input) {
        if (TextUtils.isEmpty(input)) {
            MELog.d(LOG_TAG, "base64Encode, input is empty, input =" + input);
            return "";
        }
        MELog.d(LOG_TAG, "base64Decode decode text =" + Base64.decode(input.getBytes(StandardCharsets.UTF_8), 0));
        return byteArrayToHexString(Base64.decode(input.getBytes(StandardCharsets.UTF_8), 0));
    }

    public static byte[] hexStringToByteArray(String str, int offset, int length) {
        if (length % 2 != 0) {
            return null;
        }
        String str2 = str.toUpperCase();
        byte[] outputBytes = new byte[str2.length() / 2];
        for (int i = 0; i < length; i += 2) {
            char c1 = str2.charAt(i + offset);
            char c2 = str2.charAt(i + 1 + offset);
            if (!isHexChar(c1) || !isHexChar(c2)) {
                return null;
            }
            outputBytes[i / 2] = (byte) ((Character.digit(c1, 16) << 4) + Character.digit(c2, 16));
        }
        return outputBytes;
    }

    public static String byteArrayToHexString(byte[] byteArray) {
        if (byteArray == null) {
            return "";
        }
        return byteArrayToHexString(byteArray, 0, byteArray.length);
    }

    public static String byteArrayToHexString(byte[] array, int offset, int length) {
        if (array == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%02x", Integer.valueOf(array[offset + i] & ModemUtils.EFS_VALUE_ASDIV_73841_DYNAMIC_SWITCH)));
        }
        return sb.toString();
    }

    public static boolean isHexChar(char c) {
        if (Character.isLowerCase(c)) {
            c = Character.toUpperCase(c);
        }
        return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F');
    }

    private List<String> getSimState() {
        try {
            String simStateProperty = SystemProperties.get("gsm.sim.state", (String) null);
            if (simStateProperty == null) {
                MELog.d(LOG_TAG, "get sim states null");
                return null;
            }
            String[] simStatesArray = simStateProperty.split(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE);
            List<String> sim_state = new ArrayList<>(Arrays.asList(simStatesArray));
            for (String simState : sim_state) {
                MELog.d(LOG_TAG, "sim states = " + simState);
            }
            return sim_state;
        } catch (Exception e) {
            MELog.e(LOG_TAG, "get sim states failed");
            return null;
        }
    }
}
