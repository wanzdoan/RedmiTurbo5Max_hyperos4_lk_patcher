package com.xiaomi.modem;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Process;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.xiaomi.internal.telephony.ModemSarUtils;
import com.xiaomi.mirilhook.MiRilHookBase;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import miuix.animation.internal.TransitionInfo;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ModemUtils {
    protected static Context mContext = null;
    protected static OemHookAgent mHookAgent = null;
    private static boolean mTmpLogPrintFlag = false;
    public static final byte[] EFS_VALUE_ASDIV_73971_OPEN = {2, 1, 1, 1, 1, 1, 1, 2, 1, 1, 0, 0, 0, 0};
    public static final byte[] EFS_VALUE_ASDIV_73971_CLOSE = {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0};
    protected static String mBuildTags = "NULL";
    protected static String mBuildVersion = "";
    protected static String mRootType = "NULL";
    protected static String mCertType = "none";
    protected static String mBuildProfile = "NULL";
    protected static String mBuildRegion = "none";
    public static final int NETWORK_TYPE_NR_CA = 30;
    protected static String mFactoryBuild = "NULL";
    protected static String mDeviceHwInfo = "NULL";
    protected static String mDeviceModName = "NULL";
    protected static String mDeviceHwLevel = "NULL";
    protected static int mHalChipId = -1;
    public static boolean mXringHalFlag = false;
    public static boolean mIsHalDecoupled = false;
    protected static int mBpChipId = -1;
    protected static int mManufacturerId = 0;
    protected static int mOemProjectFlag = 0;
    protected static int mSdkVersion = Build.VERSION.SDK_INT;
    protected static int mAndroidVersion = SystemProperties.getInt("ro.build.version.release", 0);
    protected static String mDeviceName = "NULL";
    private static String mProcessName = null;
    private static final Object GET_CUR_PROCESS_NAME_LOCK = new Object();
    private static int mProcessPid = -1;
    private static final HashMap sDumpStacktraceRingMap = new HashMap();
    private static final HashMap sDumpStacktraceLockMap = new HashMap();

    public static void onInitContext(Context context) {
        mContext = context;
        log("onInitContext, mContext = " + mContext);
    }

    public static void onInitHook(OemHookAgent oemHookAgent) {
        log("onInitHook, hook = " + oemHookAgent);
        mHookAgent = oemHookAgent;
    }

    public static String exeAdbCmd(String str) {
        log("exeAdbCmd command: " + str);
        try {
            Process processExec = Runtime.getRuntime().exec(str);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(processExec.getInputStream()));
            char[] cArr = new char[4096];
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                int i = bufferedReader.read(cArr);
                if (i > 0) {
                    stringBuffer.append(cArr, 0, i);
                } else {
                    bufferedReader.close();
                    processExec.waitFor();
                    String string = stringBuffer.toString();
                    log("outputStr: " + string);
                    return string;
                }
            }
        } catch (IOException e) {
            log("RuntimeException: " + e);
            e.printStackTrace();
            return "";
        } catch (InterruptedException e2) {
            log("InterruptedException: " + e2);
            e2.printStackTrace();
            return "";
        }
    }

    public static String byteBufferToString(ByteBuffer byteBuffer) {
        try {
            CharBuffer charBufferDecode = Charset.forName("UTF-8").newDecoder().decode(byteBuffer);
            byteBuffer.flip();
            return charBufferDecode.toString();
        } catch (Exception e) {
            e.printStackTrace();
            log("byteBufferToString Exception: " + e);
            return null;
        }
    }

    public static Date getCurrentTimeWithData() {
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return new Date(System.currentTimeMillis());
    }

    public static long getCurrentTimeWithLong() {
        return timeDateToLong(getCurrentTimeWithData());
    }

    public static Date timeLongToDate(long j) {
        return new Date(j);
    }

    public static String timeLongToString(long j, String str) {
        return timeDateToString(timeLongToDate(j), str);
    }

    public static String timeLongToString(long j) {
        return timeLongToString(j, "yyyy-MM-dd HH:mm:ss.SSS");
    }

    public static String timeDateToString(Date date, String str) {
        return new SimpleDateFormat(str).format(date);
    }

    public static long timeDateToLong(Date date) {
        return date.getTime();
    }

    public static ByteBuffer getHexDataBufferFromString(String str, boolean z) {
        String strSubstring;
        if (str == null) {
            log("getHexDataBufferFromString, str is null");
            return null;
        }
        String strTrim = str.trim();
        byte[] bArr = new byte[20];
        int length = strTrim.length();
        int i = 0;
        int i2 = 0;
        while (true) {
            int i3 = i + 2;
            if (i3 < length) {
                strSubstring = strTrim.substring((length - 2) - i, length - i);
                if (z) {
                    tmpLog("byteStr = ".concat(strSubstring));
                }
            } else {
                strSubstring = strTrim.substring(0, length - i);
                if (z) {
                    tmpLog("end, byteStr = ".concat(strSubstring));
                }
            }
            try {
                int iIntValue = Integer.valueOf(strSubstring, 16).intValue();
                byte b = (byte) iIntValue;
                if (z) {
                    tmpLog("bVal[" + i2 + "] = " + ((int) b) + ", iVal = " + iIntValue + ", byteStr = " + strSubstring);
                }
                bArr[i2] = b;
                i2++;
                if (i3 >= length || i2 >= 20) {
                    break;
                }
                i = i3;
            } catch (ArithmeticException e) {
                log("ArithmeticException, str = ".concat(str));
                log("ArithmeticException: " + e);
                e.printStackTrace();
                return null;
            } catch (NumberFormatException e2) {
                log("NumberFormatException, str = ".concat(str));
                log("NumberFormatException: " + e2);
                e2.printStackTrace();
                return null;
            }
        }
        while (i2 < 20) {
            bArr[i2] = 0;
            i2++;
        }
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        byteBufferWrap.order(ByteOrder.nativeOrder());
        return byteBufferWrap;
    }

    public static int getInt(ByteBuffer byteBuffer, int i) {
        return byteBuffer != null ? byteBuffer.getInt() : i;
    }

    public static String getString(byte[] bArr, int i, String str) {
        String str2 = "";
        if (bArr == null) {
            return "";
        }
        for (int i2 = 0; i2 < bArr.length; i2++) {
            if (i2 != 0 && i != 0 && i2 % i == 0 && str != null) {
                str2 = str2 + str;
            }
            str2 = str2 + getString(bArr[i2], true, true, true, true);
        }
        return str2;
    }

    public static String getString(byte b, boolean z, boolean z2, boolean z3, boolean z4) {
        String string;
        if (z3) {
            tmpLog("getString, byte, val = " + ((int) b) + ", unSigned = " + z + ", bPrint = " + z3 + ", hexNotDec = " + z2 + ", fillHexZero = " + z4);
        }
        if (z2) {
            string = Integer.toHexString(b & TransitionInfo.INIT);
            if (1 == string.length() && z4) {
                string = "0".concat(string);
            }
            if (z3) {
                tmpLog("HEX, strVal = ".concat(string));
            }
        } else {
            String str = "" + ((int) b);
            if (!z || b >= 0) {
                string = str;
            } else {
                string = BigDecimal.valueOf((byte) (b & 127)).add(BigDecimal.valueOf(127L)).add(BigDecimal.valueOf(1L)).toString();
                if (z3) {
                    tmpLog("val < 0, new strVal = " + string);
                }
            }
        }
        return string != null ? string.toUpperCase() : string;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static int getIntFromString(String str, boolean z) {
        tmpLog("getIntFromString, str = " + str + ", hexNotDec = " + z);
        int i = 0;
        i = 0;
        i = 0;
        if (str == 0) {
            tmpLog("str is null, return 0");
            return 0;
        }
        str.getClass();
        if ("".equals(str)) {
            tmpLog("str is empty, return 0");
            return 0;
        }
        try {
            if (z) {
                ByteBuffer hexDataBufferFromString = getHexDataBufferFromString(str, true);
                str = str;
                if (hexDataBufferFromString != null) {
                    i = hexDataBufferFromString.getInt();
                    str = str;
                }
            } else {
                int iIntValue = new BigDecimal(str).intValue();
                i = iIntValue;
                str = iIntValue;
            }
        } catch (ArithmeticException e) {
            log("ArithmeticException, str = ".concat(str));
            log("ArithmeticException: " + e);
            e.printStackTrace();
        } catch (NumberFormatException e2) {
            log("NumberFormatException, str = ".concat(str));
            log("NumberFormatException: " + e2);
            e2.printStackTrace();
        }
        tmpLog("val = " + i);
        return i;
    }

    public static boolean isMobileDataEnabled(Context context) {
        TelephonyManager telephonyManagerCreateForSubscriptionId;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (telephonyManager == null || (telephonyManagerCreateForSubscriptionId = telephonyManager.createForSubscriptionId(SubscriptionManager.getDefaultDataSubscriptionId())) == null) {
            return false;
        }
        return telephonyManagerCreateForSubscriptionId.isDataEnabled();
    }

    public static int getInsertedSimCount() {
        Phone[] phones = PhoneFactory.getPhones();
        if (phones == null) {
            return 0;
        }
        int i = 0;
        for (Phone phone : phones) {
            if (phone.getIccCard().hasIccCard()) {
                i++;
            }
        }
        return i;
    }

    public static long getDiagDeadlineFromProp() {
        log("getDiagDeadlineFromProp");
        long jOnGetLongFromString = onGetLongFromString(SystemProperties.get("persist.radio.mtb_diag_deadline", "0"), 0);
        log("getDiagDeadlineFromProp, deadline = " + jOnGetLongFromString + ", deadlineStr: " + timeLongToString(jOnGetLongFromString));
        return jOnGetLongFromString;
    }

    public static boolean onModemDiagIsOpen() {
        log("onModemDiagIsOpen");
        if (mHookAgent == null) {
            log("onModemDiagIsOpen, mHookAgent is null");
            return false;
        }
        String strExeAdbCmd = exeAdbCmd("getprop sys.usb.config");
        log("usb_prop: " + strExeAdbCmd);
        if (strExeAdbCmd.contains("diag")) {
            log("diag has been opened");
            return true;
        }
        log("diag has been closed");
        return false;
    }

    public static ByteBuffer getBuffer(byte[] bArr) {
        if (bArr == null) {
            tmpLog("bytes is null, getBuffer do nothing");
            return null;
        }
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        byteBufferWrap.order(ByteOrder.nativeOrder());
        return byteBufferWrap;
    }

    public static ByteBuffer getBuffer(byte[] bArr, int i) {
        if (bArr == null) {
            tmpLog("bytes is null, getBuffer do nothing");
            return null;
        }
        if (i > bArr.length) {
            tmpLog("length is larger than bytes.length, getBuffer do nothing");
            return null;
        }
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr, 0, i);
        byteBufferWrap.order(ByteOrder.nativeOrder());
        return byteBufferWrap;
    }

    public static byte[] getBytes(int i) {
        byte[] bArr = new byte[4];
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        byteBufferWrap.order(ByteOrder.nativeOrder());
        byteBufferWrap.putInt(i);
        return bArr;
    }

    public static ByteBuffer onGetHexDataBufferFromString(String str, boolean z) {
        String strSubstring;
        if (str == null) {
            log("onGetHexDataBufferFromString, str is null");
            return null;
        }
        String strTrim = str.trim();
        byte[] bArr = new byte[20];
        int length = strTrim.length();
        int i = 0;
        int i2 = 0;
        while (true) {
            int i3 = i + 2;
            if (i3 < length) {
                strSubstring = strTrim.substring((length - 2) - i, length - i);
                if (z) {
                    tmpLog("byteStr = ".concat(strSubstring));
                }
            } else {
                strSubstring = strTrim.substring(0, length - i);
                if (z) {
                    tmpLog("end, byteStr = ".concat(strSubstring));
                }
            }
            try {
                int iIntValue = Integer.valueOf(strSubstring, 16).intValue();
                byte b = (byte) iIntValue;
                if (z) {
                    tmpLog("bVal[" + i2 + "] = " + ((int) b) + ", iVal = " + iIntValue + ", byteStr = " + strSubstring);
                }
                bArr[i2] = b;
                i2++;
                if (i3 >= length || i2 >= 20) {
                    break;
                }
                i = i3;
            } catch (ArithmeticException e) {
                log("ArithmeticException, str = ".concat(str));
                log("ArithmeticException: " + e);
                e.printStackTrace();
                return null;
            } catch (NumberFormatException e2) {
                log("NumberFormatException, str = ".concat(str));
                log("NumberFormatException: " + e2);
                e2.printStackTrace();
                return null;
            }
        }
        while (i2 < 20) {
            bArr[i2] = 0;
            i2++;
        }
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        byteBufferWrap.order(ByteOrder.nativeOrder());
        return byteBufferWrap;
    }

    public static String onGetStringByDateType(int i, int i2, int i3) {
        String string;
        tmpLog("onGetStringByDateType, int, val = " + i + ", uFlag = " + i2 + ", hexOrDec = " + i3);
        if (1 == i3) {
            string = Integer.toHexString(i);
            tmpLog("HEX, strVal = " + string);
        } else {
            String str = "" + i;
            if (1 != i2 || i >= 0) {
                string = str;
            } else {
                string = BigDecimal.valueOf(i & Integer.MAX_VALUE).add(BigDecimal.valueOf(2147483647L)).add(BigDecimal.valueOf(1L)).toString();
                tmpLog("val < 0, new strVal = " + string);
            }
        }
        return string != null ? string.toUpperCase() : string;
    }

    public static long onGetLongFromString(String str, int i) {
        tmpLog("onGetLongFromString, strIn = " + str + ", hexOrDec = " + i);
        long jLongValue = 0;
        if (str == null) {
            tmpLog("strIn is null, return 0");
            return 0L;
        }
        String strTrim = str.trim();
        if ("".equals(strTrim)) {
            tmpLog("str is empty, return 0");
            return 0L;
        }
        if (1 == i) {
            ByteBuffer byteBufferOnGetHexDataBufferFromString = onGetHexDataBufferFromString(strTrim, true);
            if (byteBufferOnGetHexDataBufferFromString != null) {
                jLongValue = byteBufferOnGetHexDataBufferFromString.getLong();
            }
        } else {
            jLongValue = new BigDecimal(strTrim).longValue();
        }
        tmpLog("val = " + jLongValue);
        return jLongValue;
    }

    public static String getCurProcessName(Context context) {
        return getCurProcessName(context, "Unknown");
    }

    public static String getCurProcessName(Context context, String str) {
        String str2 = mProcessName;
        if (str2 != null) {
            return str2;
        }
        synchronized (GET_CUR_PROCESS_NAME_LOCK) {
            try {
                if (context == null) {
                    Log.e("ModemUtils", "getCurProcessName, context is null, defName: " + str);
                    return str;
                }
                int iMyPid = Process.myPid();
                for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
                    if (runningAppProcessInfo.pid == iMyPid) {
                        mProcessName = runningAppProcessInfo.processName;
                        Log.d("ModemUtils", "getCurProcessName, context: " + context + ", pid: " + iMyPid + ", defName: " + str + ", mProcessName: " + mProcessName);
                        return mProcessName;
                    }
                }
                Log.e("ModemUtils", "getCurProcessName, not find pid, context: " + context + ", pid: " + iMyPid + ", defName: " + str);
                return str;
            } catch (Exception e) {
                Log.e("ModemUtils", "getCurProcessName, Exception: " + e + ", context: " + context + ", defName: " + str);
                e.printStackTrace();
                return str;
            }
        }
    }

    public static boolean isXringHal() {
        boolean z = SystemProperties.getBoolean("persist.vendor.radio.xring", false);
        mXringHalFlag = z;
        if (z) {
            log("Hal is Xring");
        } else {
            log("Hal is non Xring");
        }
        return mXringHalFlag;
    }

    public static int getModemHalChipId(OemHookAgent oemHookAgent) {
        if (-1 != mHalChipId) {
            log("getModemHalChipId, mHalChipId = " + mHalChipId);
            return mHalChipId;
        }
        mHalChipId = 0;
        if (oemHookAgent == null) {
            log("getModemHalChipId, hook is null");
            return mHalChipId;
        }
        ByteBuffer byteBufferOnHookCommonMsgSync = oemHookAgent.onHookCommonMsgSync(82);
        if (byteBufferOnHookCommonMsgSync == null) {
            log("getModemHalChipId, buf is null");
            return mHalChipId;
        }
        int i = byteBufferOnHookCommonMsgSync.getInt();
        if (i != 0) {
            log("getModemHalChipId, ret is invalid, ret = " + i);
            return mHalChipId;
        }
        int i2 = byteBufferOnHookCommonMsgSync.getInt();
        mHalChipId = i2;
        if (i2 <= 0) {
            log("getModemHalChipId, mHalChipId is invalid, mHalChipId = " + mHalChipId);
            mHalChipId = 0;
            return 0;
        }
        log("getModemHalChipId, mHalChipId = " + mHalChipId);
        return mHalChipId;
    }

    public static int getModemChipId(OemHookAgent oemHookAgent) {
        if (-1 != mBpChipId) {
            log("getModemChipId, mBpChipId = " + mBpChipId);
            return mBpChipId;
        }
        mBpChipId = 0;
        if (oemHookAgent == null) {
            log("getModemChipId, hook is null");
            return mBpChipId;
        }
        mBpChipId = oemHookAgent.getXiaomiModemBpChipId();
        log("getModemChipId, mBpChipId = " + mBpChipId);
        return mBpChipId;
    }

    public static int getManufacturerId(OemHookAgent oemHookAgent) {
        if (mManufacturerId != 0) {
            log("getManufacturerId, mManufacturerId = " + mManufacturerId);
            return mManufacturerId;
        }
        if (oemHookAgent == null) {
            log("getManufacturerId, hook is null, mManufacturerId = " + mManufacturerId);
            return mManufacturerId;
        }
        mManufacturerId = oemHookAgent.getXiaomiManufacturerId();
        log("getManufacturerId, mManufacturerId = " + mManufacturerId);
        return mManufacturerId;
    }

    public static boolean isHalDecoupled(OemHookAgent oemHookAgent) {
        if (oemHookAgent == null) {
            log("isHalDecoupled, hook is null, mIsHalDecoupled = " + mIsHalDecoupled);
            return mIsHalDecoupled;
        }
        mIsHalDecoupled = oemHookAgent.isHalDecoupled();
        log("isHalDecoupled, mIsHalDecoupled = " + mIsHalDecoupled);
        return mIsHalDecoupled;
    }

    public static int getOemProductFlag(OemHookAgent oemHookAgent) {
        if (mOemProjectFlag != 0) {
            log("getOemProductFlag, mOemProjectFlag = " + mOemProjectFlag);
            return mOemProjectFlag;
        }
        if (oemHookAgent == null) {
            log("getOemProductFlag, hook is null");
            return mOemProjectFlag;
        }
        if (true == oemHookAgent.isOemProduct()) {
            mOemProjectFlag = 1;
        } else {
            mOemProjectFlag = 2;
        }
        log("getOemProductFlag, mOemProjectFlag = " + mOemProjectFlag);
        return mOemProjectFlag;
    }

    public static String getVerInfoWithName() {
        return "mDeviceName = " + mDeviceName + ", mManufacturerId = " + mManufacturerId + ", mHalChipId = " + mHalChipId + ", mBpChipId = " + mBpChipId + ", mDeviceHwInfo = " + mDeviceHwInfo + ", mDeviceModName = " + mDeviceModName + ", mDeviceHwLevel = " + mDeviceHwLevel + ", mBuildRegion = " + mBuildRegion + ", mBuildProfile = " + mBuildProfile + ", mCertType = " + mCertType + ", mFactoryBuild = " + mFactoryBuild + ", mRootType = " + mRootType + ", mOemProjectFlag = " + mOemProjectFlag + ", mBuildTags = " + mBuildTags + ", mSdkVersion = " + mSdkVersion + ", mAndroidVersion = " + mAndroidVersion + ", mBuildVersion = " + mBuildVersion + ", mXringHalFlag = " + mXringHalFlag + ", mIsHalDecoupled = " + mIsHalDecoupled;
    }

    public static String getVerInfoWithoutName() {
        return mDeviceName + "," + mManufacturerId + "," + mHalChipId + "," + mBpChipId + "," + mDeviceHwInfo + "," + mDeviceModName + "," + mDeviceHwLevel + "," + mBuildRegion + "," + mBuildProfile + "," + mCertType + "," + mFactoryBuild + "," + mRootType + "," + mOemProjectFlag + "," + mBuildTags + "," + mSdkVersion + "," + mAndroidVersion + "," + mBuildVersion + "," + mXringHalFlag + "," + mIsHalDecoupled;
    }

    public static String getDeviceName() {
        String str = SystemProperties.get(ModemSarUtils.PROPERTY_DEVICE_NAME);
        mDeviceName = str;
        return str;
    }

    public static String getDeviceHwInfo() {
        String str = SystemProperties.get(ModemSarUtils.PROPERTY_DEVICE_HWC);
        mDeviceHwInfo = str;
        return str;
    }

    public static String getDeviceSwInfo() {
        String str = SystemProperties.get(ModemSarUtils.PROPERTY_PRODUCT_MOD_DEVICE);
        mDeviceModName = str;
        return str;
    }

    public static String getDeviceHwLevel() {
        String str = SystemProperties.get("ro.boot.hwlevel");
        mDeviceHwLevel = str;
        return str;
    }

    public static String getBuildRegion() {
        String str = SystemProperties.get("ro.miui.build.region");
        mBuildRegion = str;
        return str;
    }

    public static String getCertType() {
        String str = SystemProperties.get("ro.cust.test");
        mCertType = str;
        return str;
    }

    public static String getFactoryBuild() {
        String str = SystemProperties.get(ModemSarUtils.PROPERTY_FACTORY_BUILD);
        mFactoryBuild = str;
        return str;
    }

    public static String getRootType() {
        String str = SystemProperties.get("ro.debuggable");
        mRootType = str;
        return str;
    }

    public static String getBuildVersion() {
        String str = SystemProperties.get("ro.build.version.incremental");
        mBuildVersion = str;
        return str;
    }

    public static String getBuildTags() {
        String str = SystemProperties.get("ro.build.tags");
        mBuildTags = str;
        return str;
    }

    public static int getSdkVersion() {
        int i = Build.VERSION.SDK_INT;
        mSdkVersion = i;
        return i;
    }

    public static int getAndroidVersion() {
        int i = SystemProperties.getInt("ro.build.version.release", 0);
        mAndroidVersion = i;
        return i;
    }

    public static String getBuildProfile(OemHookAgent oemHookAgent) {
        if (oemHookAgent == null) {
            log("getBuildProfile, hook is null");
            return null;
        }
        mBuildProfile = oemHookAgent.onHookPropGetSync("ro.vendor.radio.build_profile", "NULL");
        log("getBuildProfile, val = " + mBuildProfile);
        return mBuildProfile;
    }

    public static boolean isFactoryBuild() {
        String str = SystemProperties.get(ModemSarUtils.PROPERTY_FACTORY_BUILD, "0");
        boolean zEquals = TextUtils.equals(str, "1");
        log("isFactoryBuild, ret = " + zEquals + ", val = " + str);
        return zEquals;
    }

    public static int getManufacturerId() {
        String str = SystemProperties.get("ro.vendor.radio.manufacturer_id", "0");
        mManufacturerId = 0;
        if ("1".equals(str)) {
            mManufacturerId = 1;
        } else if ("2".equals(str)) {
            mManufacturerId = 2;
        } else if ("3".equals(str)) {
            mManufacturerId = 3;
        } else if ("4".equals(str)) {
            mManufacturerId = 4;
        } else if ("5".equals(str)) {
            mManufacturerId = 5;
        }
        log("getManufacturerId, mManufacturerId = " + mManufacturerId);
        return mManufacturerId;
    }

    public static int getHalChipId() {
        mHalChipId = getIntFromString(SystemProperties.get(MiRilHookBase.PROP_RADIO_XIAOMI_HAL_CHIP_ID, "0"), false);
        log("getHalChipId, mHalChipId = " + mHalChipId);
        return mHalChipId;
    }

    public static int getBpChipId() {
        mBpChipId = getIntFromString(SystemProperties.get("ro.vendor.radio.bp_chip_id", "0"), false);
        log("getBpChipId, mBpChipId = " + mBpChipId);
        return mBpChipId;
    }

    public static String getBuildProfile() {
        mBuildProfile = SystemProperties.get("ro.vendor.radio.build_profile", "NULL");
        log("getBuildProfile, mBuildProfile = " + mBuildProfile);
        return mBuildProfile;
    }

    public static void onSendMsgEx(Handler handler, int i, long j) {
        if (handler == null) {
            log("onSendMsgEx, hdl is null, hdl = " + handler + ", msg_id = " + i + ", delayMillis = " + j);
            return;
        }
        onRemoveMsg(handler, i);
        handler.sendEmptyMessageDelayed(i, j);
    }

    public static void onRemoveMsg(Handler handler, int i) {
        if (handler == null) {
            log("onRemoveMsg, hdl is null, hdl = " + handler + ", msg_id = " + i);
            return;
        }
        handler.removeMessages(i);
    }

    public static void dumpLteBandpref(OemHookAgent oemHookAgent, PrintWriter printWriter) {
        if (printWriter == null) {
            log("dumpLteBandpref, pw is null");
            return;
        }
        String str = "LTE user bandpref 1_64(SUB0): inactive";
        String str2 = "LTE user bandpref 1_64(SUB1): inactive";
        if (oemHookAgent == null) {
            log("dumpLteBandpref, hook is null");
        } else {
            try {
                ByteBuffer byteBufferOnHookEfsOptSync = oemHookAgent.onHookEfsOptSync(0, "/nv/item_files/modem/mmode/lte_bandpref", 4);
                if (byteBufferOnHookEfsOptSync == null) {
                    log("dumpLteBandpref, buf is null, SUB0");
                } else {
                    int i = byteBufferOnHookEfsOptSync.getInt();
                    int i2 = byteBufferOnHookEfsOptSync.getInt();
                    if (i != 0) {
                        log("dumpLteBandpref, ret is invalid, can not read efs for SUB0");
                    } else if (8 != i2) {
                        log("dumpLteBandpref, len is invalid, can not read efs for SUB0");
                    } else {
                        byte[] bArr = new byte[8];
                        byteBufferOnHookEfsOptSync.get(bArr);
                        str = "LTE user bandpref 1_64(SUB0): 0x" + getString(bArr, 4, " ");
                        ByteBuffer byteBufferOnHookEfsOptSync2 = oemHookAgent.onHookEfsOptSync(1, "/nv/item_files/modem/mmode/lte_bandpref", 4);
                        if (byteBufferOnHookEfsOptSync2 == null) {
                            log("dumpLteBandpref, buf is null, SUB_1");
                        } else {
                            int i3 = byteBufferOnHookEfsOptSync2.getInt();
                            int i4 = byteBufferOnHookEfsOptSync2.getInt();
                            if (i3 != 0) {
                                log("dumpLteBandpref, ret is invalid, can not read efs for SUB_1");
                            } else if (8 != i4) {
                                log("dumpLteBandpref, len is invalid, can not read efs for SUB_1");
                            } else {
                                byte[] bArr2 = new byte[8];
                                byteBufferOnHookEfsOptSync2.get(bArr2);
                                str2 = "LTE user bandpref 1_64(SUB_1): 0x" + getString(bArr2, 4, " ");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log("dumpLteBandpref, Happen Exception, Exception info: " + e);
                e.printStackTrace();
            }
        }
        dump(printWriter, "------ Modem lte_bandpref dump start ------");
        dump(printWriter, str);
        dump(printWriter, str2);
        dump(printWriter, "------ Modem lte_bandpref dump end ------");
    }

    public static String getComplexItemStringForRffeScan(ByteBuffer byteBuffer, int i) {
        log("getComplexItemStringForRffeScan, buffer = " + byteBuffer);
        if (byteBuffer == null) {
            log("getComplexItemStringForRffeScan, buffer is null");
            return "Data Buffer is empty";
        }
        String str = ((("Header version: " + onGetStringByDateType(getInt(byteBuffer, 0), 1, i) + "\n") + "Header size: " + onGetStringByDateType(getInt(byteBuffer, 0), 1, i) + "\n") + "Body size: " + onGetStringByDateType(getInt(byteBuffer, 0), 1, i) + "\n") + "HWID: " + onGetStringByDateType(getInt(byteBuffer, 0), 1, i) + "\n";
        int i2 = getInt(byteBuffer, 0);
        String str2 = ((str + "Num Devices: " + onGetStringByDateType(i2, 1, i) + "\n") + "Num Devices present: " + onGetStringByDateType(getInt(byteBuffer, 0), 1, i) + "\n") + "Error Code: " + onGetStringByDateType(getInt(byteBuffer, 0), 1, i) + "\n";
        for (int i3 = 0; i3 < i2; i3++) {
            String str3 = (((((((((str2 + "RF Device ID[" + i3 + "]: " + onGetStringByDateType(getInt(byteBuffer, 0), 1, i) + "\n") + "Phy Dev Instance[" + i3 + "]: " + onGetStringByDateType(getInt(byteBuffer, 0), 1, i) + "\n") + "Alt Part[" + i3 + "]: " + onGetStringByDateType(getInt(byteBuffer, 0), 1, i) + "\n") + "Bus ID[" + i3 + "]: " + onGetStringByDateType(getInt(byteBuffer, 0), 1, i) + "\n") + "Mfg ID[" + i3 + "]: " + onGetStringByDateType(getInt(byteBuffer, 0), 1, i) + "\n") + "Prod ID[" + i3 + "]: " + onGetStringByDateType(getInt(byteBuffer, 0), 1, i) + "\n") + "Prod Revision[" + i3 + "]: " + onGetStringByDateType(getInt(byteBuffer, 0), 1, i) + "\n") + "Default USID[" + i3 + "]: " + onGetStringByDateType(getInt(byteBuffer, 0), 1, i) + "\n") + "Default USID[" + i3 + "]: " + onGetStringByDateType(getInt(byteBuffer, 0), 1, i) + "\n") + "Assigned USID[" + i3 + "]: " + onGetStringByDateType(getInt(byteBuffer, 0), 1, i) + "\n";
            int i4 = getInt(byteBuffer, 0);
            if (i4 == 0) {
                str2 = str3 + "Status[" + i3 + "]: (" + i4 + ")RFC_DEVICE_PRESENT\n";
            } else if (1 == i4) {
                str2 = str3 + "Status[" + i3 + "]: (" + i4 + ")RFC_DEVICE_MISSING_NON_FATAL\n";
            } else if (2 == i4) {
                str2 = str3 + "Status[" + i3 + "]: (" + i4 + ")RFC_DEVICE_MISSING_FATAL\n";
            } else if (3 == i4) {
                str2 = str3 + "Status[" + i3 + "]: (" + i4 + ")RFC_DEVICE_SCAN_INCOMPLETE\n";
            } else {
                str2 = str3 + "Status[" + i3 + "]: (" + i4 + ")Unkown\n";
            }
        }
        return str2;
    }

    public static void dumpRffeScanResult(OemHookAgent oemHookAgent, PrintWriter printWriter) {
        String complexItemStringForRffeScan;
        if (printWriter == null) {
            log("dumpRffeScanResult, pw is null");
            return;
        }
        if (oemHookAgent == null) {
            log("dumpRffeScanResult, hook is null");
        } else {
            try {
                ByteBuffer byteBufferOnHookBigEfsReadSync = oemHookAgent.onHookBigEfsReadSync(0, "/rfc/rffe_scan/rffe_scan.dat");
                if (byteBufferOnHookBigEfsReadSync == null) {
                    log("dumpRffeScanResult, buf is null");
                } else {
                    int i = byteBufferOnHookBigEfsReadSync.getInt();
                    int i2 = byteBufferOnHookBigEfsReadSync.getInt();
                    log("dumpRffeScanResult, read config, ret = " + i + ", len = " + i2);
                    if (i2 > 0 && i == 0) {
                        byte[] bArr = new byte[i2];
                        byteBufferOnHookBigEfsReadSync.get(bArr);
                        complexItemStringForRffeScan = getComplexItemStringForRffeScan(getBuffer(bArr), 0);
                    } else {
                        log("dumpRffeScanResult, maybe is empty");
                    }
                    dump(printWriter, "------ RF rffe_scan dump start ------");
                    dump(printWriter, complexItemStringForRffeScan);
                    dump(printWriter, "------ RF rffe_scan dump end ------");
                }
            } catch (Exception e) {
                log("dumpRffeScanResult, Happen Exception, Exception info: " + e);
                e.printStackTrace();
            }
        }
        complexItemStringForRffeScan = "Empty dump";
        dump(printWriter, "------ RF rffe_scan dump start ------");
        dump(printWriter, complexItemStringForRffeScan);
        dump(printWriter, "------ RF rffe_scan dump end ------");
    }

    public static boolean onSendDiagCmdForRfdeviceReport(OemHookAgent oemHookAgent) {
        log("onSendDiagCmdForRfdeviceReport");
        if (oemHookAgent == null) {
            log("onSendDiagCmdForRfdeviceReport, hook is null");
            return false;
        }
        ByteBuffer byteBufferOnHookDiagSendSync = oemHookAgent.onHookDiagSendSync(new byte[]{75, 11, 20, 0, 16, 4, 13, 0, 0, 0, 0});
        SystemClock.sleep(50L);
        if (byteBufferOnHookDiagSendSync != null) {
            return true;
        }
        log("onSendDiagCmdForRfdeviceReport, onHookDiagSendSync failed");
        return false;
    }

    public static void dumpRfdeviceReportResult(OemHookAgent oemHookAgent, PrintWriter printWriter) {
        String strByteBufferToString;
        if (printWriter == null) {
            log("dumpRfdeviceReportResult, pw is null");
            return;
        }
        if (oemHookAgent == null) {
            log("dumpRfdeviceReportResult, hook is null");
        } else if (!onSendDiagCmdForRfdeviceReport(oemHookAgent)) {
            log("dumpRfdeviceReportResult, onSendDiagCmdForRfdeviceReport failed");
        } else {
            try {
                ByteBuffer byteBufferOnHookBigEfsReadSync = oemHookAgent.onHookBigEfsReadSync(0, "/rfc/rfdevice_report.dat");
                if (byteBufferOnHookBigEfsReadSync == null) {
                    log("dumpRfdeviceReportResult, buf is null");
                } else {
                    int i = byteBufferOnHookBigEfsReadSync.getInt();
                    int i2 = byteBufferOnHookBigEfsReadSync.getInt();
                    log("dumpRfdeviceReportResult, read config, ret = " + i + ", len = " + i2);
                    if (i2 > 0 && i == 0) {
                        byte[] bArr = new byte[i2];
                        byteBufferOnHookBigEfsReadSync.get(bArr);
                        strByteBufferToString = byteBufferToString(getBuffer(bArr));
                    } else {
                        log("dumpRfdeviceReportResult, maybe is empty");
                    }
                    dump(printWriter, "------ RF rfdevice_report dump start ------");
                    dump(printWriter, strByteBufferToString);
                    dump(printWriter, "------ RF rfdevice_report dump end ------");
                }
            } catch (Exception e) {
                log("dumpRfdeviceReportResult, Happen Exception, Exception info: " + e);
                e.printStackTrace();
            }
        }
        strByteBufferToString = "Empty dump";
        dump(printWriter, "------ RF rfdevice_report dump start ------");
        dump(printWriter, strByteBufferToString);
        dump(printWriter, "------ RF rfdevice_report dump end ------");
    }

    public static void tmpLog(String str) {
        if (mTmpLogPrintFlag) {
            log(str);
        }
    }

    public static void log(String str) {
        Log.d("ModemUtils(" + getCurProcessName(mContext) + ")", str);
    }

    public static void logd(String str, String str2) {
        Log.d(str + "(" + getCurProcessName(mContext) + ")", str2);
    }

    public static void loge(String str, String str2) {
        Log.e(str + "(" + getCurProcessName(mContext) + ")", str2);
    }

    public static void dump(String str) {
        Log.d("MODEM_DUMP(" + getCurProcessName(mContext) + ")", str);
    }

    public static void dump(PrintWriter printWriter, String str) {
        dump(str);
        if (printWriter != null) {
            printWriter.println(str);
        } else {
            log("pw is null, println do nothing");
        }
    }
}
