package com.android.phone;

import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyRFAction {
    public boolean processRFCloudArrayParams(String str, String str2) {
        Log.d("TelephonyRFAction", "processRFCloudArrayParams: param1=" + str + ", param2=" + str2);
        if (!RFValidateBasicParams(str, str2)) {
            Log.e("TelephonyRFAction", "processRFCloudArrayParams: Basic parameter validation failed");
            return false;
        }
        try {
            if (RFIsArrayMode(str, str2)) {
                return RFProcessArrayMode(str, str2);
            }
            return RFProcessSingleMode(str, str2);
        } catch (Exception e) {
            Log.e("TelephonyRFAction", "processRFCloudArrayParams: Exception in processing array parameters: " + e);
            return false;
        }
    }

    private boolean RFValidateBasicParams(String str, String str2) {
        if (str == null || str2 == null) {
            Log.e("TelephonyRFAction", "processRFCloudArrayParams: Null parameters detected - param1Str=" + str + ", param2Str=" + str2);
            return false;
        }
        if (!str.trim().isEmpty() && !str2.trim().isEmpty()) {
            return true;
        }
        Log.e("TelephonyRFAction", "processRFCloudArrayParams: Empty parameters detected - param1Str='" + str + "', param2Str='" + str2 + "'");
        return false;
    }

    private boolean RFIsArrayMode(String str, String str2) {
        if (str == null || str2 == null) {
            return false;
        }
        return str.contains(",") || str2.contains(",");
    }

    private boolean RFProcessArrayMode(String str, String str2) {
        String[] strArrSplit = str.split(",");
        String[] strArrSplit2 = str2.split(",");
        if (!RFValidateArrayLength(strArrSplit, strArrSplit2)) {
            Log.e("TelephonyRFAction", "RFProcessArrayMode: Array length validation failed");
            return false;
        }
        return RFProcessParameterPairs(strArrSplit, strArrSplit2);
    }

    private boolean RFValidateArrayLength(String[] strArr, String[] strArr2) {
        if (strArr.length == strArr2.length) {
            return true;
        }
        Log.e("TelephonyRFAction", "processRFCloudArrayParams: Array length mismatch! param1Array.length=" + strArr.length + ", param2Array.length=" + strArr2.length);
        return false;
    }

    private boolean RFProcessParameterPairs(String[] strArr, String[] strArr2) {
        boolean z = true;
        for (int i = 0; i < strArr.length; i++) {
            String strTrim = strArr[i].trim();
            String strTrim2 = strArr2[i].trim();
            if (RFValidateSinglePair(strTrim, strTrim2, i)) {
                if (!RFExecuteSinglePair(strTrim, strTrim2, i + 1)) {
                    Log.e("TelephonyRFAction", "RFProcessParameterPairs: Parameter pair execution failed at index " + i);
                }
            } else {
                Log.e("TelephonyRFAction", "RFProcessParameterPairs: Parameter pair validation failed at index " + i);
            }
            z = false;
        }
        return z;
    }

    private boolean RFValidateSinglePair(String str, String str2, int i) {
        if (str.isEmpty() || str2.isEmpty()) {
            Log.e("TelephonyRFAction", "processRFCloudArrayParams: Empty item after trim at index " + i + ", param1='" + str + "', param2='" + str2 + "'");
            return false;
        }
        try {
            Integer.parseInt(str);
            Integer.parseInt(str2);
            return true;
        } catch (NumberFormatException unused) {
            Log.e("TelephonyRFAction", "processRFCloudArrayParams: Non-numeric item at index " + i + " - param1=" + str + ", param2=" + str2);
            return false;
        }
    }

    private boolean RFExecuteSinglePair(String str, String str2, int i) {
        boolean zEnableRFCloud = enableRFCloud(str, str2);
        if (!zEnableRFCloud) {
            Log.e("TelephonyRFAction", "processRFCloudArrayParams: Failed to execute pair " + i);
        }
        return zEnableRFCloud;
    }

    private boolean RFProcessSingleMode(String str, String str2) {
        return enableRFCloud(str, str2);
    }

    public boolean disableRFCloud() {
        Log.d("TelephonyRFAction", "disableRFCloud: Applying to all slots");
        try {
            boolean zTeleAction = TelephonyAction.getInstance().teleAction(13, TelephonyActionBase.ActionReason.CLOUD_PUSH, 0, 9, 0);
            try {
                Thread.sleep(100L);
            } catch (InterruptedException unused) {
                Thread.currentThread().interrupt();
            }
            boolean z = zTeleAction && TelephonyAction.getInstance().teleAction(13, TelephonyActionBase.ActionReason.CLOUD_PUSH, 0, 2, 0);
            if (z) {
                Log.d("TelephonyRFAction", "disableRFCloud: All slots disabled successfully");
            } else {
                Log.e("TelephonyRFAction", "disableRFCloud: Failed to disable");
            }
            return z;
        } catch (Exception e) {
            Log.e("TelephonyRFAction", "disableRFCloud: Exception in disable RF cloud feature: " + e);
            return false;
        }
    }

    private boolean enableRFCloud(String str, String str2) {
        try {
            try {
                boolean zTeleAction = TelephonyAction.getInstance().teleAction(13, TelephonyActionBase.ActionReason.CLOUD_PUSH, 0, Integer.valueOf(Integer.parseInt(str)), Integer.valueOf(Integer.parseInt(str2)));
                if (zTeleAction) {
                    Log.d("TelephonyRFAction", "enableRFCloud: All slots executed successfully");
                    return zTeleAction;
                }
                Log.e("TelephonyRFAction", "enableRFCloud: Failed to execute");
                return zTeleAction;
            } catch (NumberFormatException e) {
                Log.e("TelephonyRFAction", "enableRFCloud: Invalid parameters - param1=" + str + ", param2=" + str2 + ", error=" + e);
                return false;
            }
        } catch (Exception e2) {
            Log.e("TelephonyRFAction", "enableRFCloud: Exception occurred: " + e2);
            return false;
        }
    }

    public boolean applyRFCloudStatus(String str, String str2, String str3) {
        Log.d("TelephonyRFAction", "applyRFCloudStatus: Applying to all slots, param1=" + str + ", param2=" + str2 + ", status=" + str3);
        try {
            if ("1".equals(str3)) {
                return processRFCloudArrayParams(str, str2);
            }
            if ("0".equals(str3)) {
                return disableRFCloud();
            }
            Log.e("TelephonyRFAction", "applyRFCloudStatus: Invalid status value: " + str3);
            return false;
        } catch (Exception e) {
            Log.e("TelephonyRFAction", "applyRFCloudStatus: Exception occurred: " + e);
            return false;
        }
    }
}
