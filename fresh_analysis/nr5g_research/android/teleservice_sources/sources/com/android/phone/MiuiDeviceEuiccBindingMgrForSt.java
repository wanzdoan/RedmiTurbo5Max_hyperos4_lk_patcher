package com.android.phone;

import ai.onnxruntime.OrtProvider$$ExternalSyntheticBUOutline0;
import android.os.SystemProperties;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccSlot;
import com.android.internal.telephony.uicc.euicc.EuiccCard;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public class MiuiDeviceEuiccBindingMgrForSt extends MiuiDeviceEuiccBindingMgrBase {
    private static String LAST_STEP;
    private static CAEsimServiceForSt mCAEsimServiceForSt;
    private int bindOrPairResult = -1;
    public final Object mLock = new Object();
    private final String GET_INPUT_FOR_BINDING = "F1";
    private final String GET_INPUT_FOR_PAIRING = "F2";
    private final String GET_INPUT_FOR_RESUILT = "F3";
    private final String COMPARE_FAILED_NOT_REACH_MAX_RETRY_COUNTER = "63C";
    private final String COMPARE_FAILED_AND_REACH_MAX_RETRY_COUNTER = "6983";
    private final String TAG_POSTFIX_COMPARE_SUCCESS = "9000";

    public static void init() {
        if (MiuiDeviceEuiccBindingMgrBase.mInstance == null) {
            MiuiDeviceEuiccBindingMgrBase.mInstance = new MiuiDeviceEuiccBindingMgrForSt();
        }
    }

    private MiuiDeviceEuiccBindingMgrForSt() {
        MiuiDeviceEuiccBindingMgrBase.logi("MiuiDeviceSimBindingManagerForSt init");
        CAEsimServiceForSt cAEsimServiceForSt = CAEsimServiceForSt.getInstance();
        mCAEsimServiceForSt = cAEsimServiceForSt;
        if (cAEsimServiceForSt == null) {
            MiuiDeviceEuiccBindingMgrBase.logd("mCAEsimServiceForSt is null.");
        }
        MiuiDeviceEuiccBindingMgrBase.logi("MiuiDeviceSimBindingManagerForSt init end");
    }

    @Override // com.android.phone.MiuiDeviceEuiccBindingMgrBase
    public String processBindingData(List list) {
        MiuiDeviceEuiccBindingMgrBase.logi("processBindingData start");
        String strHandleBindingStepOne = null;
        if (list == null || list.size() != 3) {
            MiuiDeviceEuiccBindingMgrBase.logd("processBindingData failed, args is null or args.size() != 3");
            return null;
        }
        byte b = 0;
        String str = (String) list.get(0);
        String str2 = (String) list.get(1);
        String str3 = (String) list.get(2);
        MiuiDeviceEuiccBindingMgrBase.logd("dataType: " + str2 + " stepFlag: " + str3 + " data: " + str);
        str3.hashCode();
        switch (str3.hashCode()) {
            case 2219:
                if (!str3.equals("F1")) {
                    b = -1;
                }
                break;
            case 2220:
                b = !str3.equals("F2") ? (byte) -1 : (byte) 1;
                break;
            case 2221:
                b = !str3.equals("F3") ? (byte) -1 : (byte) 2;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                LAST_STEP = "F1";
                strHandleBindingStepOne = handleBindingStepOne(str);
                break;
            case 1:
                LAST_STEP = "F2";
                strHandleBindingStepOne = handlePairingStepOne(str);
                break;
            case 2:
                strHandleBindingStepOne = handleResultStep(str);
                break;
            default:
                MiuiDeviceEuiccBindingMgrBase.logd("processBindingData failed, unkown stepFlag");
                break;
        }
        MiuiDeviceEuiccBindingMgrBase.logd("processBindingData end, processedData: " + strHandleBindingStepOne);
        return strHandleBindingStepOne;
    }

    private String handleBindingStepOne(String str) {
        return TextUtils.bytesToHexString(mCAEsimServiceForSt.esimBindStepOne(TextUtils.hexStringToBytes(str)));
    }

    private String handlePairingStepOne(String str) {
        return TextUtils.bytesToHexString(mCAEsimServiceForSt.esimPairStepOne(TextUtils.hexStringToBytes(str)));
    }

    private String handleResultStep(String str) {
        if (!isValidResultData(str)) {
            MiuiDeviceEuiccBindingMgrBase.logd("processBindingData, compare failed, no need to proceed step two.");
            return null;
        }
        return "0" + processStepTwoBasedOnLastStep(str.substring(0, str.length() - 4));
    }

    private boolean isValidResultData(String str) {
        return str != null && str.length() >= 4 && "9000".equals(str.substring(str.length() - 4));
    }

    private int processStepTwoBasedOnLastStep(String str) {
        if ("F1".equals(LAST_STEP)) {
            return handleBindingStepTwo(str);
        }
        if ("F2".equals(LAST_STEP)) {
            return handlePairingStepTwo(str);
        }
        MiuiDeviceEuiccBindingMgrBase.logd("processBindingData failed, LAST_STEP is null or invalid");
        return 1;
    }

    private int handleBindingStepTwo(String str) {
        String eid = getEid();
        if (eid == null) {
            MiuiDeviceEuiccBindingMgrBase.logd("processBindingData failed, eid is null.");
            return 1;
        }
        byte[] bytes = eid.getBytes(StandardCharsets.UTF_8);
        byte[] bArrHexStringToBytes = TextUtils.hexStringToBytes(str);
        if (bArrHexStringToBytes == null || bytes == null) {
            MiuiDeviceEuiccBindingMgrBase.logd("processBindingData failed, dataBytes or eidBytes is null.");
            return 1;
        }
        byte[] bArr = new byte[bArrHexStringToBytes.length + bytes.length];
        System.arraycopy(bArrHexStringToBytes, 0, bArr, 0, bArrHexStringToBytes.length);
        System.arraycopy(bytes, 0, bArr, bArrHexStringToBytes.length, bytes.length);
        return mCAEsimServiceForSt.esimBindStepTwo(bArr);
    }

    private int handlePairingStepTwo(String str) {
        return mCAEsimServiceForSt.esimPairStepTwo(TextUtils.hexStringToBytes(str));
    }

    private String getEid() {
        EuiccCard uiccCard;
        UiccSlot uiccSlot = UiccController.getInstance().getUiccSlot(1);
        String eid = (uiccSlot == null || (uiccCard = uiccSlot.getUiccCard()) == null) ? null : uiccCard.getEid();
        if (eid == null) {
            MiuiDeviceEuiccBindingMgrBase.logd("getEid from UiccSlot failed, try to get from SystemProperties");
            eid = SystemProperties.get("persist.vendor.radio.eid", "");
        }
        MiuiDeviceEuiccBindingMgrBase.logd("getEid: " + eid);
        return eid;
    }

    public String esimReadEid() {
        CAEsimServiceForSt cAEsimServiceForSt = mCAEsimServiceForSt;
        if (cAEsimServiceForSt == null) {
            MiuiDeviceEuiccBindingMgrBase.logd("mCAEsimServiceForSt is null.");
            return null;
        }
        String str = new String(cAEsimServiceForSt.esimReadEid(), StandardCharsets.UTF_8);
        MiuiDeviceEuiccBindingMgrBase.logd("esimReadEid: ".concat(str));
        return str;
    }

    public boolean esimGetBindStatus() {
        CAEsimServiceForSt cAEsimServiceForSt = mCAEsimServiceForSt;
        if (cAEsimServiceForSt == null) {
            MiuiDeviceEuiccBindingMgrBase.logd("mCAEsimServiceForSt is null.");
            return false;
        }
        return cAEsimServiceForSt.esimGetBindStatus();
    }

    public abstract class TextUtils {
        private static final Pattern HEX_STRING_PATTERN = Pattern.compile("^(?:[0-9a-fA-F]{2})*$");

        public static String bytesToHexString(byte[] bArr) {
            if (bArr == null) {
                return null;
            }
            char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
            StringBuilder sb = new StringBuilder(bArr.length * 2);
            for (byte b : bArr) {
                sb.append(cArr[(b & 240) >>> 4]);
                sb.append(cArr[b & 15]);
            }
            return sb.toString();
        }

        public static byte[] hexStringToBytes(String str) {
            if (str == null) {
                return null;
            }
            try {
                checkHexStringOrThrow(str);
                int length = str.length() / 2;
                byte[] bArr = new byte[length];
                for (int i = 0; i < length; i++) {
                    int i2 = i * 2;
                    bArr[i] = (byte) Integer.parseInt(str.substring(i2, i2 + 2), 16);
                }
                return bArr;
            } catch (IllegalArgumentException e) {
                MiuiDeviceEuiccBindingMgrBase.logd("hexStringToBytes failed, hexstring: ".concat(str));
                e.printStackTrace();
                return null;
            }
        }

        public static void checkHexStringOrThrow(String str) {
            if (HEX_STRING_PATTERN.matcher(str).matches()) {
                return;
            }
            OrtProvider$$ExternalSyntheticBUOutline0.m("Hex String is Not Well Formatted ", str);
        }
    }
}
