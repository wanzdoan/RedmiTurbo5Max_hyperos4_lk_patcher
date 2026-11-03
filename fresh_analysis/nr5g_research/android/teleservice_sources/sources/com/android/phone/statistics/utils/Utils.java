package com.android.phone.statistics.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.AccessNetworkUtils;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.ims.ImsManager;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.ModuleDelegate;
import com.android.phone.module.ITelephonyModule;
import com.ot.pubsub.a.b;
import com.ot.pubsub.util.v;
import com.xiaomi.modem.sar.DeviceStateListenerBase;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import miui.os.Build;
import miui.telephony.DefaultSimManager;
import miui.telephony.ServiceProviderUtils;
import miui.telephony.TelephonyManagerEx;
import miuix.preference.flexible.PreferenceMarkLevel;

/* JADX INFO: loaded from: classes.dex */
public abstract class Utils {
    public static boolean DEBUG_ALL = false;
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final boolean IS_INTERNATIONAL_BUILD = Build.IS_INTERNATIONAL_BUILD;
    public static final int NW_NR_CA = 30;

    public enum DsdaState {
        NOT_SUPPORT,
        REGISTER_ON,
        REGISTER_OFF
    }

    public static boolean hasRuleEvent(int i, int i2) {
        return (i & (1 << i2)) != 0;
    }

    public static boolean hasState(int i, int i2) {
        return (i & i2) == i2;
    }

    public static boolean isAnyValidRuleEventExist(int i) {
        for (int i2 = 1; i2 < 7; i2++) {
            if (((1 << i2) & i) != 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCdmaLte(int i) {
        return i == 14 || i == 13 || i == 18 || i == 19;
    }

    public static boolean isDFXForDisconnectCall(int i) {
        return i == 1314 || i == 1315 || i == 1318;
    }

    public static boolean isDFXForHangup(int i) {
        return i == 1314 || i == 1315 || i == 1318;
    }

    public static boolean isGlobalStatEnabled() {
        return true;
    }

    public static boolean isLogicalFailureForDialOutError(int i) {
        switch (i) {
            case 16:
            case 17:
            case 18:
            case 19:
            case 21:
            case DeviceStateListenerBase.TYPE_HORIZONTAL_GAME_LIST_STATE /* 28 */:
            case 31:
            case 1222:
            case 1314:
            case 1315:
            case 1317:
            case 1318:
            case 1340:
            case 1500:
            case 1501:
            case 1504:
            case 1510:
            case 2100:
            case PreferenceMarkLevel.LEVEL_NORMAL_ONLY_TITLE /* 10001 */:
            case PreferenceMarkLevel.LEVEL_NORMAL_ONLY_SUMMARY /* 10002 */:
            case 10003:
            case 10004:
            case 10007:
            case 10016:
            case PreferenceMarkLevel.LEVEL_LARGE_ONLY_TITLE /* 20001 */:
            case PreferenceMarkLevel.LEVEL_LARGE_ONLY_SUMMARY /* 20002 */:
            case 20003:
            case 20007:
            case 20016:
            case 20060:
            case 20061:
                return true;
            default:
                return false;
        }
    }

    public static boolean isLogicalFailureForDisconnectCall(int i) {
        if (i == 16 || i == 1214 || i == 1318 || i == 1500 || i == 1510 || i == 10007 || i == 10016 || i == 20007 || i == 20016 || i == 18 || i == 19 || i == 1314 || i == 1315 || i == 1503 || i == 1504 || i == 20060 || i == 20061) {
            return true;
        }
        switch (i) {
            case PreferenceMarkLevel.LEVEL_NORMAL_ONLY_TITLE /* 10001 */:
            case PreferenceMarkLevel.LEVEL_NORMAL_ONLY_SUMMARY /* 10002 */:
            case 10003:
                return true;
            default:
                switch (i) {
                    case PreferenceMarkLevel.LEVEL_LARGE_ONLY_TITLE /* 20001 */:
                    case PreferenceMarkLevel.LEVEL_LARGE_ONLY_SUMMARY /* 20002 */:
                    case 20003:
                        return true;
                    default:
                        return false;
                }
        }
    }

    public static boolean isLogicalFailureForInComingError(int i) {
        return i == 1214 || i == 1503;
    }

    public static boolean isLte(int i) {
        return i == 13 || i == 19;
    }

    public static String systemTimeToString(long j) {
        return j != 0 ? TIME_FORMAT.format(Long.valueOf(j)) : "never";
    }

    public static boolean isUserExperienceProgramEnabled(Context context) {
        boolean z = false;
        if (context != null) {
            try {
                if (Settings.Secure.getInt(context.getContentResolver(), "upload_log_pref", 0) > 0) {
                    z = true;
                }
            } catch (Exception unused) {
                logW("Utils", "isUserExperienceProgramEnabled exception");
                return z;
            }
        }
        logI("Utils", "isUserExperienceProgramEnabled() enabled:" + z);
        return z;
    }

    public static boolean isValidParams(Map map) {
        return (map == null || map.isEmpty() || map.size() > 50) ? false : true;
    }

    public static int convertEventToBitIndex(String str) {
        if ("call_unexpected_disconnected".equals(str)) {
            return 1;
        }
        if ("data_setup_call_error".equals(str)) {
            return 2;
        }
        if ("data_fake_connection".equals(str)) {
            return 3;
        }
        if ("nw_out_of_service".equals(str)) {
            return 4;
        }
        return "call_dial_out_error".equals(str) ? 6 : -1;
    }

    public static boolean isPermanentUserDisagree(int i) {
        return hasRuleEvent(i, 0);
    }

    public static boolean isNeedToRevokeUserPermission(long j) {
        return j != 0 && System.currentTimeMillis() - j > v.a;
    }

    public static boolean isLogicalFailureForHangup(int i) {
        return isLogicalFailureForDialOutError(i) || isLogicalFailureForInComingError(i);
    }

    public static int getPropertyOperatingMode() {
        try {
            return SystemProperties.getInt("persist.radio.operating_mode", -1);
        } catch (Exception unused) {
            logE("Utils", "getPropertyOperatingMode Try to get persist.radio.operating_mode failed!");
            return -1;
        }
    }

    public static int getRsrpBin(int i) {
        return getRsrpLevel(i, Constants.RSRP_LENIENT, -44, -140);
    }

    public static int getSsrsrpBin(int i) {
        return getRsrpLevel(i, Constants.SSRSRP_THRESH, -44, -140);
    }

    public static int getRsrpLevel(int i, int[] iArr, int i2, int i3) {
        if (iArr == null || iArr.length != Constants.MIUI_NUM_SIGNAL_STRENGTH_BARS || i > i2 || i < i3) {
            logD("Utils", "getRsrpBin: Invalid thresholds or values");
            return -1;
        }
        int length = iArr.length;
        while (length <= Constants.MIUI_NUM_SIGNAL_STRENGTH_BARS && length > 0 && i < iArr[length - 1]) {
            length--;
        }
        logD("Utils", "getRsrpBin = " + length);
        return length;
    }

    public static int getSnrBin(int i) {
        if (i <= 300 && i >= -200) {
            int[] iArr = Constants.SNR_LENIENT;
            if (i >= iArr[0]) {
                return 4;
            }
            if (i >= iArr[1]) {
                return 3;
            }
            if (i >= iArr[2]) {
                return 2;
            }
            if (i >= iArr[3]) {
                return 1;
            }
        }
        return 0;
    }

    public static boolean isNr(int i) {
        return i == 20 || i == NW_NR_CA;
    }

    public static boolean isNonNr(int i) {
        return (i == 0 || isNr(i)) ? false : true;
    }

    public static int getDataRat(ServiceState serviceState) {
        if (serviceState == null) {
            return 0;
        }
        return serviceState.getRilDataRadioTechnology();
    }

    public static int getDataNetworkType(ServiceState serviceState) {
        if (serviceState == null) {
            return 0;
        }
        return serviceState.getDataNetworkType();
    }

    public static String getPreferredApn(Context context, int i) {
        Uri uri;
        if (SubscriptionManager.isValidSubscriptionId(i)) {
            uri = Uri.parse("content://telephony/carriers/preferapn/subId/" + i);
        } else {
            uri = Uri.parse("content://telephony/carriers/preferapn");
        }
        Uri uri2 = uri;
        String string = "none";
        Cursor cursorQuery = null;
        try {
            cursorQuery = context.getContentResolver().query(uri2, null, null, null, "name ASC");
            if (cursorQuery != null && cursorQuery.getCount() > 0 && cursorQuery.moveToFirst()) {
                string = cursorQuery.getString(cursorQuery.getColumnIndexOrThrow("apn"));
            }
            return string;
        } catch (Exception e) {
            e.printStackTrace();
            logD("Utils", "getPreferredApn -exception");
            return "none";
        } finally {
            if (cursorQuery != null) {
                cursorQuery.close();
            }
        }
    }

    public static String getRatDataState(int i, int i2) {
        return i + "_" + (2 == i2 ? "CON" : "NA");
    }

    public static String getNwClassDataState(int i, int i2) {
        return "Rat_" + TelephonyManagerEx.getDefault().getNetworkClassName(i);
    }

    public static int getPreferredNwModeFromDb(int i) {
        return ModemProvider.getInstance().getPreferredNetworkModeFromDb(i);
    }

    public static boolean is5GNetworkMode(int i) {
        for (int i2 : ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getSupportNetworkMode(9)) {
            if (i == i2) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSupportVoWifiDevice(Context context, int i) {
        ImsManager imsManager;
        return Build.IS_INTERNATIONAL_BUILD && (imsManager = ImsManager.getInstance(context, i)) != null && imsManager.isWfcEnabledByPlatform();
    }

    public static int getOperatingBandForNrarfcn(ServiceState serviceState) {
        if (serviceState != null) {
            return getOperationBandForNrarfcn(serviceState.getChannelNumber());
        }
        return -1;
    }

    public static int getOperationBandForNrarfcn(int i) {
        if (i > 2084999) {
            return -1;
        }
        if (i >= 693333 && i <= 733333) {
            return 79;
        }
        if (i >= 620000 && i <= 680000) {
            return 78;
        }
        if (i >= 286400 && i <= 303400) {
            return 75;
        }
        if (i >= 123400 && i <= 130400) {
            return 71;
        }
        if (i >= 422000 && i <= 440000) {
            return i <= 434000 ? 1 : 66;
        }
        if (i >= 636667 && i <= 646666) {
            return 48;
        }
        if (i >= 499200 && i <= 537999) {
            return 41;
        }
        if (i >= 460000 && i <= 480000) {
            return 40;
        }
        if (i >= 514000 && i <= 524000) {
            return 38;
        }
        if (i >= 151200 && i <= 160600) {
            return i >= 151600 ? 28 : 14;
        }
        if (i >= 386000 && i <= 399000) {
            return i <= 398000 ? 2 : 25;
        }
        if (i >= 158200 && i <= 164200) {
            return 20;
        }
        if (i >= 185000 && i <= 192000) {
            return 8;
        }
        if (i >= 524000 && i <= 538000) {
            return 7;
        }
        if (i >= 173800 && i <= 178800) {
            return 5;
        }
        if (i < 361000 || i > 376000) {
            return AccessNetworkUtils.getOperatingBandForNrarfcn(i);
        }
        return 3;
    }

    public static String getBand(int i, ServiceState serviceState) {
        if (serviceState == null) {
            return "";
        }
        int dataNetworkType = serviceState.getDataNetworkType();
        int channelNumber = serviceState.getChannelNumber();
        if (dataNetworkType != 1 && dataNetworkType != 2) {
            if (dataNetworkType != 3) {
                if (dataNetworkType != 13) {
                    if (dataNetworkType != 15) {
                        if (dataNetworkType != 16) {
                            if (dataNetworkType != 19) {
                                if (dataNetworkType != 20) {
                                    switch (dataNetworkType) {
                                        case 8:
                                        case 9:
                                        case 10:
                                            break;
                                        default:
                                            return "";
                                    }
                                } else {
                                    return "N" + getOperationBandForNrarfcn(serviceState.getChannelNumber());
                                }
                            }
                        }
                    }
                }
                return b.a + AccessNetworkUtils.getOperatingBandForEarfcn(channelNumber);
            }
            return b.a + AccessNetworkUtils.getOperatingBandForUarfcn(channelNumber);
        }
        return b.a + AccessNetworkUtils.getOperatingBandForArfcn(channelNumber);
    }

    public static DsdaState getDsdaState() {
        if (!MiuiPhoneUtils.isDeviceDsdaSupportedByQcom() && !MiuiPhoneUtils.isDSDASupport()) {
            return DsdaState.NOT_SUPPORT;
        }
        if (TelephonyManagerEx.getDefault().isConcurrentCallsPossible()) {
            return DsdaState.REGISTER_ON;
        }
        return DsdaState.REGISTER_OFF;
    }

    public static int getVoiceRat(ServiceState serviceState) {
        if (serviceState == null) {
            return 0;
        }
        int rilDataRadioTechnology = serviceState.getRilDataRadioTechnology();
        return rilDataRadioTechnology == 0 ? serviceState.getRilVoiceRadioTechnology() : rilDataRadioTechnology;
    }

    public static int getDsdaUsageScene(int i) {
        if (!TelephonyManagerEx.getDefault().isConcurrentCallsPossible()) {
            return -1;
        }
        for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
            if (i2 != i) {
                Phone phone = MiuiPhoneUtils.getPhone(i2);
                if (phone != null && phone.getState() != PhoneConstants.State.IDLE) {
                    logD("Utils", "DsdaUsageScene: Voice + Voice.");
                    return 2;
                }
                if (MiuiPhoneUtils.isMobileDataConnected(i2)) {
                    logD("Utils", "DsdaUsageScene: Voice + Data.");
                    return 1;
                }
            }
        }
        return -1;
    }

    public static int getNonDefaultDataSlotId() {
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            if (i != defaultDataSlotId) {
                return i;
            }
        }
        return -1;
    }

    public static Phone getPhone(Connection connection) {
        if (connection == null || connection.getCall() == null) {
            return null;
        }
        return connection.getCall().getPhone();
    }

    public static boolean isValidatePhoneId(int i) {
        return i >= 0 && i < MiuiPhoneUtils.PHONE_COUNT;
    }

    public static boolean isVideo(int i) {
        if (IS_INTERNATIONAL_BUILD) {
            return hasState(i, 1) || hasState(i, 2) || hasState(i, 3);
        }
        return hasState(i, 3);
    }

    /* JADX WARN: Code duplicated, block: B:45:0x0087 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    public static long getBoardThermalTemp() throws Throwable {
        BufferedReader bufferedReader;
        Throwable th;
        Exception e;
        StringBuilder sb;
        IOException e2;
        BufferedReader bufferedReader2 = null;
        long j = 0;
        try {
            File file = new File("sys/class/thermal/thermal_message/board_sensor_temp");
            if (file.exists()) {
                StringBuilder sb2 = new StringBuilder();
                bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                try {
                    try {
                        for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                            sb2.append(line);
                        }
                        j = Integer.parseInt(sb2.toString());
                        bufferedReader2 = bufferedReader;
                    } catch (Exception e3) {
                        e = e3;
                        Log.e("Utils", "getBoardThermalTemp" + e);
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException e4) {
                                e2 = e4;
                                sb = new StringBuilder("getBoardThermalTemp");
                                sb.append(e2);
                                Log.e("Utils", sb.toString());
                            }
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e5) {
                            Log.e("Utils", "getBoardThermalTemp" + e5);
                        }
                    }
                    throw th;
                }
            }
            if (bufferedReader2 != null) {
                try {
                    bufferedReader2.close();
                } catch (IOException e6) {
                    e2 = e6;
                    sb = new StringBuilder("getBoardThermalTemp");
                    sb.append(e2);
                    Log.e("Utils", sb.toString());
                }
            }
        } catch (Exception e7) {
            bufferedReader = null;
            e = e7;
        } catch (Throwable th3) {
            bufferedReader = null;
            th = th3;
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            throw th;
        }
        return j;
    }

    /* JADX WARN: Code duplicated, block: B:41:0x0093 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    public static boolean isThermalLimit() throws Throwable {
        Throwable th;
        BufferedReader bufferedReader;
        Exception e;
        int i;
        BufferedReader bufferedReader2 = null;
        try {
            try {
                File file = new File("/sys/class/thermal/thermal_message/modem_limit");
                if (file.exists()) {
                    StringBuilder sb = new StringBuilder();
                    bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                    try {
                        for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                            sb.append(line);
                        }
                        i = Integer.parseInt(sb.toString());
                        bufferedReader2 = bufferedReader;
                    } catch (Exception e2) {
                        e = e2;
                        Log.e("Utils", "getBoardThermalTemp" + e);
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException e3) {
                                Log.e("Utils", "getBoardThermalTemp" + e3);
                            }
                        }
                        i = 0;
                    }
                } else {
                    i = 0;
                }
                if (bufferedReader2 != null) {
                    try {
                        bufferedReader2.close();
                    } catch (IOException e4) {
                        Log.e("Utils", "getBoardThermalTemp" + e4);
                    }
                }
            } catch (Throwable th2) {
                th = th2;
                if (0 != 0) {
                    try {
                        bufferedReader2.close();
                    } catch (IOException e5) {
                        Log.e("Utils", "getBoardThermalTemp" + e5);
                    }
                }
                throw th;
            }
        } catch (Exception e6) {
            bufferedReader = null;
            e = e6;
        } catch (Throwable th3) {
            th = th3;
            if (0 != 0) {
                bufferedReader2.close();
            }
            throw th;
        }
        return i != 0;
    }

    public static int getCustomSimTypeForStatistic(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone == null || !phone.getIccCard().hasIccCard()) {
            return 0;
        }
        if (MiuiPhoneUtils.isMiSim(i)) {
            return 2;
        }
        return MiuiPhoneUtils.isVirtualSim(i) ? 3 : 1;
    }

    public static String getIccidSS(Context context, int i) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (isValidatePhoneId(i) && telephonyManager != null) {
            String simOperatorNumericForPhone = telephonyManager.getSimOperatorNumericForPhone(i);
            Phone phone = MiuiPhoneUtils.getPhone(i);
            String iccSerialNumber = phone != null ? phone.getIccSerialNumber() : "";
            if (!TextUtils.isEmpty(iccSerialNumber) && !TextUtils.isEmpty(simOperatorNumericForPhone) && iccSerialNumber.length() == 20) {
                if (ServiceProviderUtils.isChinaMobile(simOperatorNumericForPhone)) {
                    return iccSerialNumber.substring(8, 10);
                }
                if (ServiceProviderUtils.isChinaUnicom(simOperatorNumericForPhone)) {
                    return iccSerialNumber.substring(9, 11);
                }
                if (ServiceProviderUtils.isChinaTelecom(simOperatorNumericForPhone)) {
                    return iccSerialNumber.substring(10, 13);
                }
            }
        }
        return "";
    }

    public static HashMap getCellParamInfo(CellIdentity cellIdentity) {
        String globalCellId;
        String string;
        String string2;
        HashMap map = new HashMap();
        if (cellIdentity != null) {
            if (cellIdentity instanceof CellIdentityLte) {
                CellIdentityLte cellIdentityLte = (CellIdentityLte) cellIdentity;
                globalCellId = cellIdentityLte.getGlobalCellId();
                string2 = Integer.toString(cellIdentityLte.getEarfcn());
                string = Integer.toString(cellIdentityLte.getPci());
            } else if (cellIdentity instanceof CellIdentityNr) {
                CellIdentityNr cellIdentityNr = (CellIdentityNr) cellIdentity;
                string2 = Integer.toString(cellIdentityNr.getNrarfcn());
                String string3 = Integer.toString(cellIdentityNr.getPci());
                globalCellId = cellIdentityNr.getGlobalCellId();
                string = string3;
            } else {
                globalCellId = null;
                string = null;
                string2 = null;
            }
            if (globalCellId == null) {
                globalCellId = "";
            }
            map.put("Gci", globalCellId);
            if (string == null) {
                string = "";
            }
            map.put("Pci", string);
            if (string2 == null) {
                string2 = "";
            }
            map.put("Arfcn", string2);
        }
        return map;
    }

    public static String getAvailableOperatorNumeric(ServiceState serviceState, ServiceState serviceState2, int i) {
        String operatorNumeric = serviceState != null ? serviceState.getOperatorNumeric() : null;
        if (TextUtils.isEmpty(operatorNumeric) && serviceState2 != null) {
            operatorNumeric = serviceState2.getOperatorNumeric();
        }
        if (TextUtils.isEmpty(operatorNumeric)) {
            operatorNumeric = miui.telephony.TelephonyManager.getDefault().getSimOperatorForSlot(i);
        }
        return TextUtils.isEmpty(operatorNumeric) ? "NoSim" : operatorNumeric;
    }

    public static void setDebugAll(boolean z) {
        DEBUG_ALL = z;
    }

    public static void logD(String str, String str2) {
        if (DEBUG_ALL) {
            Log.d(str, str2);
        }
    }

    public static void logI(String str, String str2) {
        Log.i(str, str2);
    }

    public static void logW(String str, String str2) {
        Log.w(str, str2);
    }

    public static void logE(String str, String str2) {
        Log.e(str, str2);
    }

    public static void logE(String str, String str2, Throwable th) {
        Log.e(str, str2, th);
    }
}
