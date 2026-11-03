package miuix.device;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import dalvik.system.PathClassLoader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes2.dex */
public abstract class DeviceUtils {
    private static int HIGH;
    private static int LOW;
    private static int MIDDLE;
    private static int UNKNOWN;
    private static Application application;
    private static Context applicationContext;
    private static Constructor mCompuConstructor;
    private static Constructor mConstructor;
    private static Method mGetCompuVersion;
    private static Method mGetCompuVersionV2;
    private static Method mGetCurrentCompuLevel;
    private static Method mGetCurrentComputilityLevelV2;
    private static Method mGetMiuiLiteVersion;
    private static Method mGetMiuiMiddleVersion;
    private static Method mInitCompuLevel;
    private static Class sPerfClass;
    private static PathClassLoader sPerfClassLoader;
    private static Class sPerfCompuClass;
    static final Pattern SM_PATTERN = Pattern.compile("Inc ([A-Z]+)([\\d]+)");
    static final Pattern MT_PATTERN = Pattern.compile("MT([\\d]{2})([\\d]+)");
    static Boolean MIUI_LITE_ROM = null;
    static int MIUI_LITE_VERSION = -2;
    static int MIUI_MIDDLE_VERSION = -2;
    static Boolean MIUI_LITE_STOCK_PLUS = null;
    private static int sMultiDisplayType = -1;
    private static Boolean sIsTablet = null;
    private static Boolean sIsPrimaryScreenOled = null;
    private static Boolean sIsSecondaryScreenOled = null;
    static int mLevel = -1;
    static int mCpuLevel = -1;
    static int mGpuLevel = -1;
    static int mRamLevel = -1;
    static int mTotalRam = Integer.MAX_VALUE;
    private static final String[] STOCK_DEVICE = {"cactus", "cereus", "pine", "olive", "ginkgo", "olivelite", "olivewood", "willow", "wayne", "dandelion", "angelica", "angelicain", "whyred", "tulip", "onc", "onclite", "lavender", "lotus", "laurus", "merlinnfc", "merlin", "lancelot", "citrus", "pomelo", "lemon", "shiva", "lime", "cannon", "curtana", "durandal", "excalibur", "joyeuse", "gram", "sunny", "mojito", "rainbow", "cattail", "angelican", "camellia"};
    private static Object mPerf = null;
    private static Object mCompuPerf = null;
    private static Method mGetDeviceLevel = null;
    private static Method mGetDeviceLevelForWhole = null;
    private static Method mIsSupportPrune = null;
    static int mCompuLevel = -1;
    static int mCompuVersion = -1;
    public static int DEV_STANDARD_VERSION = 1;
    private static int mLastVersion = 1;
    private static boolean initDeviceLevelInfo = false;
    public static int TYPE_RAM = 1;
    public static int TYPE_CPU = 2;
    public static int TYPE_GPU = 3;

    static {
        mConstructor = null;
        mCompuConstructor = null;
        mGetMiuiLiteVersion = null;
        mGetMiuiMiddleVersion = null;
        mInitCompuLevel = null;
        mGetCompuVersion = null;
        mGetCompuVersionV2 = null;
        mGetCurrentCompuLevel = null;
        mGetCurrentComputilityLevelV2 = null;
        try {
            PathClassLoader pathClassLoader = new PathClassLoader("/system_ext/framework/MiuiBooster.jar", ClassLoader.getSystemClassLoader());
            sPerfClassLoader = pathClassLoader;
            Class clsLoadClass = pathClassLoader.loadClass("com.miui.performance.DeviceLevelUtils");
            sPerfClass = clsLoadClass;
            mConstructor = clsLoadClass.getConstructor(Context.class);
            Class[] clsArr = new Class[0];
            mGetMiuiLiteVersion = sPerfClass.getDeclaredMethod("getMiuiLiteVersion", null);
        } catch (Exception e) {
            Log.e("DeviceUtils", "static init(): Load Class Exception:" + e);
        }
        PathClassLoader pathClassLoader2 = sPerfClassLoader;
        if (pathClassLoader2 != null) {
            try {
                Class clsLoadClass2 = pathClassLoader2.loadClass("com.miui.performance.ComputilityLevelUtils");
                sPerfCompuClass = clsLoadClass2;
                Class[] clsArr2 = new Class[0];
                mCompuConstructor = clsLoadClass2.getConstructor(null);
                Class[] clsArr3 = new Class[0];
                mInitCompuLevel = sPerfCompuClass.getDeclaredMethod("initComputilityLevel", null);
                Class[] clsArr4 = new Class[0];
                mGetCompuVersion = sPerfCompuClass.getDeclaredMethod("getComputilityVersion", null);
                Class[] clsArr5 = new Class[0];
                mGetCurrentCompuLevel = sPerfCompuClass.getDeclaredMethod("getComputilityLevel", null);
                Class[] clsArr6 = new Class[0];
                mGetCompuVersionV2 = sPerfCompuClass.getDeclaredMethod("getComputilityVersionV2", null);
                Class[] clsArr7 = new Class[0];
                mGetCurrentComputilityLevelV2 = sPerfCompuClass.getDeclaredMethod("getComputilityLevelV2", null);
            } catch (Exception e2) {
                Log.e("DeviceUtils", "static init: ComputilityLevel Load Class Exception:" + e2);
            }
        }
        try {
            Class[] clsArr8 = new Class[0];
            mGetMiuiMiddleVersion = sPerfClass.getDeclaredMethod("getMiuiMiddleVersion", null);
        } catch (Exception e3) {
            Log.e("DeviceUtils", "static init(): Load MiuiMiddle Class Exception:" + e3);
        }
        if (sPerfClass == null) {
            Log.e("DeviceUtils", "static init(): MiuiBooster is not in this rom");
        }
    }

    public class CpuInfo {
        int architecture;
        int id;
        int implementor;
        int maxFreq;
        int part;

        public String toString() {
            return "CpuInfo{id=" + this.id + ", implementor=" + Integer.toHexString(this.implementor) + ", architecture=" + this.architecture + ", part=" + Integer.toHexString(this.part) + ", maxFreq=" + this.maxFreq + '}';
        }
    }

    public class CpuStats {
        int bigCoreCount;
        int level = -1;
        int maxFreq;
        int smallCoreCount;

        public String toString() {
            return "CpuStats{level=" + this.level + ", maxFreq=" + this.maxFreq + ", bigCoreCount=" + this.bigCoreCount + ", smallCoreCount=" + this.smallCoreCount + '}';
        }
    }

    public static int getTotalRam() {
        if (mTotalRam == Integer.MAX_VALUE) {
            try {
                Class[] clsArr = new Class[0];
                mTotalRam = (int) (((((Long) Class.forName("miui.util.HardwareInfo").getMethod("getTotalPhysicalMemory", null).invoke(null, null)).longValue() / 1024) / 1024) / 1024);
            } catch (Throwable th) {
                Log.e("DeviceUtils", th.getMessage());
                mTotalRam = 0;
            }
        }
        return mTotalRam;
    }

    public static boolean isMiuiLiteRom() {
        if (MIUI_LITE_ROM == null) {
            try {
                Boolean bool = (Boolean) Class.forName("miui.os.Build").getDeclaredField("IS_MIUI_LITE_VERSION").get(null);
                bool.booleanValue();
                MIUI_LITE_ROM = bool;
            } catch (Throwable th) {
                Log.i("DeviceUtils", "isMiuiLiteRom failed", th);
                MIUI_LITE_ROM = null;
            }
        }
        return Boolean.TRUE.equals(MIUI_LITE_ROM);
    }

    public static boolean isMiuiLiteV2() {
        return isMiuiLiteRom() && getMiuiLiteVersion() >= 2;
    }

    public static int getMiuiLiteVersion() {
        int iIntValue;
        if (!isMiuiLiteRom()) {
            MIUI_LITE_VERSION = 0;
            return 0;
        }
        int i = MIUI_LITE_VERSION;
        if (i != -2) {
            return i;
        }
        try {
            Object perf = getPerf();
            if (perf != null) {
                iIntValue = ((Integer) getMiuiLiteVersionMethod().invoke(perf, null)).intValue();
                if (iIntValue >= 2) {
                    MIUI_LITE_VERSION = iIntValue;
                } else {
                    MIUI_LITE_VERSION = 1;
                }
                return MIUI_LITE_VERSION;
            }
            throw new Exception("perf is null!");
        } catch (Exception e) {
            Log.e("DeviceUtils", "getMiuiLiteVersion failed , e:" + e.toString());
            iIntValue = -1;
        }
    }

    public static boolean isMiuiMiddle() {
        return getMiuiMiddleVersion() >= 1;
    }

    public static int getMiuiMiddleVersion() {
        if (MIUI_MIDDLE_VERSION == -2) {
            try {
                Object objInvoke = mGetMiuiMiddleVersion.invoke(getPerf(), null);
                if (objInvoke != null) {
                    MIUI_MIDDLE_VERSION = ((Integer) objInvoke).intValue();
                } else {
                    MIUI_MIDDLE_VERSION = -1;
                }
            } catch (Exception e) {
                MIUI_MIDDLE_VERSION = -1;
                Log.e("DeviceUtils", "getMiuiMiddleVersion failed , e:" + e.toString());
            }
        }
        return MIUI_MIDDLE_VERSION;
    }

    public static int getDeviceLevel() {
        return getDeviceLevel(DEV_STANDARD_VERSION);
    }

    public static int getDeviceLevel(int i) {
        int i2;
        if (mLastVersion == i && (i2 = mLevel) != -1) {
            return i2;
        }
        mLastVersion = i;
        int deviceLevel2 = getDeviceLevel2(i);
        mLevel = deviceLevel2;
        return deviceLevel2 != -1 ? deviceLevel2 : getDeviceLevel1();
    }

    public static int getDeviceLevel(int i, int i2) {
        int i3;
        int i4;
        int i5;
        if (i2 == TYPE_CPU) {
            if (mLastVersion == i && (i5 = mCpuLevel) != -1) {
                return i5;
            }
        } else if (i2 == TYPE_GPU) {
            if (mLastVersion == i && (i4 = mGpuLevel) != -1) {
                return i4;
            }
        } else if (i2 == TYPE_RAM && mLastVersion == i && (i3 = mRamLevel) != -1) {
            return i3;
        }
        int deviceLevel2 = getDeviceLevel2(i, i2);
        if (deviceLevel2 != -1) {
            return setDeviceLevel(i, deviceLevel2, i2);
        }
        return setDeviceLevel(i, getDeviceLevel1(i2), i2);
    }

    public static boolean isLiteV1StockPlus() {
        if (MIUI_LITE_STOCK_PLUS == null) {
            MIUI_LITE_STOCK_PLUS = Boolean.valueOf("true".contentEquals(LiteSystemProperties.get("ro.config.low_ram.support_miuilite_plus", "false")));
        }
        return MIUI_LITE_STOCK_PLUS.booleanValue();
    }

    public static boolean isLhasaDevice() {
        return "lhasa".equals(Build.DEVICE);
    }

    public static int getFloatingWindowHeightThreshold(int i) {
        if (isLhasaDevice()) {
            return 620;
        }
        return i;
    }

    private static int setDeviceLevel(int i, int i2, int i3) {
        mLastVersion = i;
        if (i3 == TYPE_CPU) {
            mCpuLevel = i2;
            return i2;
        }
        if (i3 == TYPE_GPU) {
            mGpuLevel = i2;
            return i2;
        }
        if (i3 != TYPE_RAM) {
            return -1;
        }
        mRamLevel = i2;
        return i2;
    }

    private static int getDeviceLevel1(int i) {
        if (i == TYPE_RAM) {
            int totalRam = getTotalRam();
            if (totalRam > 6) {
                return 2;
            }
            if (totalRam > 4) {
                return 1;
            }
            return totalRam > 0 ? 0 : -1;
        }
        if (i == TYPE_CPU) {
            return getCpuLevel();
        }
        return -1;
    }

    private static int getDeviceLevel1() {
        int i = mLevel;
        if (i != -1) {
            return i;
        }
        if (isMiuiLiteRom()) {
            mLevel = 0;
        } else {
            mLevel = getMinLevel(getDeviceLevel1(TYPE_CPU), getDeviceLevel1(TYPE_RAM), getDeviceLevel(DEV_STANDARD_VERSION, TYPE_GPU));
        }
        return mLevel;
    }

    private static int getMinLevel(int... iArr) {
        if (iArr.length == 0) {
            return -1;
        }
        int i = iArr[0];
        for (int i2 : iArr) {
            if (i2 > -1 && i2 < i) {
                i = i2;
            }
        }
        return i;
    }

    private static int getCpuLevel() {
        int mtkCpuLevel;
        String hardwareInfo = getHardwareInfo();
        if (hardwareInfo.length() <= 0) {
            mtkCpuLevel = -1;
        } else if (hardwareInfo.contains("Qualcomm")) {
            mtkCpuLevel = getQualcommCpuLevel(hardwareInfo);
        } else {
            mtkCpuLevel = getMtkCpuLevel(hardwareInfo);
        }
        return mtkCpuLevel == -1 ? getCpuStats().level : mtkCpuLevel;
    }

    private static String getHardwareInfo() {
        try {
            Scanner scanner = new Scanner(new File("/proc/cpuinfo"));
            while (scanner.hasNextLine()) {
                try {
                    String strNextLine = scanner.nextLine();
                    if (!scanner.hasNextLine()) {
                        String[] strArrSplit = strNextLine.split(": ");
                        if (strArrSplit.length > 1) {
                            String str = strArrSplit[1];
                            scanner.close();
                            return str;
                        }
                        Log.e("DeviceUtils", "getChipSetFromCpuInfo failed", e);
                        return "";
                    }
                } catch (Throwable th) {
                    try {
                        scanner.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            }
            scanner.close();
            return "";
        } catch (Exception e) {
            Log.e("DeviceUtils", "getChipSetFromCpuInfo failed", e);
            return "";
        }
    }

    public static int getQualcommCpuLevel(String str) {
        String strGroup;
        String strGroup2;
        Matcher matcher = SM_PATTERN.matcher(str);
        if (!matcher.find() || (strGroup = matcher.group(1)) == null || (strGroup2 = matcher.group(2)) == null) {
            return -1;
        }
        String lowerCase = strGroup.toLowerCase(Locale.ENGLISH);
        if (lowerCase.equals("sm")) {
            int i = Integer.parseInt(strGroup2.substring(0, 1));
            if (i >= 8) {
                return 2;
            }
            return i >= 7 ? 1 : 0;
        }
        if (lowerCase.equals("sdm")) {
            return Integer.parseInt(strGroup2.substring(0, 1)) >= 7 ? 1 : 0;
        }
        return lowerCase.equals("msm") ? 0 : -1;
    }

    private static int getMtkCpuLevel(String str) {
        String strGroup;
        String strGroup2;
        Matcher matcher = MT_PATTERN.matcher(str);
        if (!matcher.find() || (strGroup = matcher.group(1)) == null || (strGroup2 = matcher.group(2)) == null) {
            return -1;
        }
        return (Integer.parseInt(strGroup) != 68 || Integer.parseInt(strGroup2) < 73) ? 0 : 1;
    }

    public static CpuStats getCpuStats() {
        List cpuInfoList = getCpuInfoList();
        CpuStats cpuStats = new CpuStats();
        if (cpuInfoList.size() < 8) {
            cpuStats.level = 0;
        }
        doCpuStats(cpuStats, cpuInfoList);
        return cpuStats;
    }

    private static void doCpuStats(CpuStats cpuStats, List list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            CpuInfo cpuInfo = (CpuInfo) it.next();
            if (cpuInfo.architecture < 8) {
                cpuStats.level = 0;
            }
            int i = cpuInfo.maxFreq;
            if (i > cpuStats.maxFreq) {
                cpuStats.maxFreq = i;
            }
            if (i >= 2000000) {
                cpuStats.bigCoreCount++;
            } else {
                cpuStats.smallCoreCount++;
            }
        }
        decideLevel(cpuStats);
    }

    private static void decideLevel(CpuStats cpuStats) {
        if (cpuStats.level != -1) {
            return;
        }
        int i = cpuStats.bigCoreCount;
        int i2 = cpuStats.maxFreq;
        if (i < 4) {
            if (i2 > 2300000) {
                cpuStats.level = 1;
                return;
            } else {
                cpuStats.level = 0;
                return;
            }
        }
        if (i2 > 2700000) {
            cpuStats.level = 2;
        } else if (i2 > 2300000) {
            cpuStats.level = 1;
        } else {
            cpuStats.level = 0;
        }
    }

    public static List getCpuInfoList() {
        ArrayList arrayList = new ArrayList();
        try {
            Scanner scanner = new Scanner(new File("/proc/cpuinfo"));
            CpuInfo line = null;
            while (scanner.hasNextLine()) {
                try {
                    String[] strArrSplit = scanner.nextLine().split(": ");
                    if (strArrSplit.length > 1) {
                        line = parseLine(strArrSplit, arrayList, line);
                    }
                } catch (Throwable th) {
                    try {
                        scanner.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            }
            scanner.close();
            return arrayList;
        } catch (Exception e) {
            Log.e("DeviceUtils", "getChipSetFromCpuInfo failed", e);
            return arrayList;
        }
    }

    private static CpuInfo parseLine(String[] strArr, List list, CpuInfo cpuInfo) throws Throwable {
        String strTrim = strArr[1].trim();
        if (strArr[0].contains("processor") && TextUtils.isDigitsOnly(strTrim)) {
            CpuInfo cpuInfoCreateCpuInfo = createCpuInfo(strTrim);
            list.add(cpuInfoCreateCpuInfo);
            return cpuInfoCreateCpuInfo;
        }
        if (cpuInfo != null) {
            getCpuInfo(strArr[0], strTrim, cpuInfo);
        }
        return cpuInfo;
    }

    private static CpuInfo createCpuInfo(String str) throws Throwable {
        CpuInfo cpuInfo = new CpuInfo();
        int i = Integer.parseInt(str);
        cpuInfo.id = i;
        String contentFromFileInfo = getContentFromFileInfo(String.format(Locale.ENGLISH, "/sys/devices/system/cpu/cpu%d/cpufreq/cpuinfo_max_freq", Integer.valueOf(i)));
        if (contentFromFileInfo != null) {
            cpuInfo.maxFreq = Integer.parseInt(contentFromFileInfo);
        }
        return cpuInfo;
    }

    private static void getCpuInfo(String str, String str2, CpuInfo cpuInfo) {
        if (str.contains("CPU implementer")) {
            cpuInfo.implementor = toInt(str2);
        } else if (str.contains("CPU architecture")) {
            cpuInfo.architecture = toInt(str2);
        } else if (str.contains("CPU part")) {
            cpuInfo.part = toInt(str2);
        }
    }

    private static int toInt(String str) {
        if (str.startsWith("0x")) {
            return Integer.parseInt(str.substring(2), 16);
        }
        return Integer.parseInt(str);
    }

    private static String getContentFromFileInfo(String str) throws Throwable {
        FileInputStream fileInputStream;
        Throwable th;
        BufferedReader bufferedReader;
        try {
            fileInputStream = new FileInputStream(str);
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                try {
                    String line = bufferedReader.readLine();
                    try {
                        fileInputStream.close();
                    } catch (IOException unused) {
                    }
                    try {
                        bufferedReader.close();
                    } catch (IOException unused2) {
                    }
                    return line;
                } catch (IOException unused3) {
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException unused4) {
                        }
                    }
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException unused5) {
                        }
                    }
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException unused6) {
                        }
                    }
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                            throw th;
                        } catch (IOException unused7) {
                            throw th;
                        }
                    }
                    throw th;
                }
            } catch (IOException unused8) {
                bufferedReader = null;
            } catch (Throwable th3) {
                th = th3;
                bufferedReader = null;
            }
        } catch (IOException unused9) {
            bufferedReader = null;
            fileInputStream = null;
        } catch (Throwable th4) {
            fileInputStream = null;
            th = th4;
            bufferedReader = null;
        }
    }

    private static int getDeviceLevel2(int i) {
        int iIntValue = -1;
        if (!initDeviceLevelInfo()) {
            return -1;
        }
        try {
            Object perf = getPerf();
            if (perf != null) {
                iIntValue = ((Integer) mGetDeviceLevelForWhole.invoke(perf, Integer.valueOf(i))).intValue();
                return transDeviceLevel(iIntValue);
            }
            throw new Exception("perf is null!");
        } catch (Exception e) {
            Log.e("DeviceUtils", "getDeviceLevel failed , e:" + e.toString());
        }
    }

    private static int getDeviceLevel2(int i, int i2) {
        int iIntValue = -1;
        if (!initDeviceLevelInfo()) {
            return -1;
        }
        try {
            Object perf = getPerf();
            if (perf != null) {
                iIntValue = ((Integer) mGetDeviceLevel.invoke(perf, Integer.valueOf(i), Integer.valueOf(i2))).intValue();
                return transDeviceLevel(iIntValue);
            }
            throw new Exception("perf is null!");
        } catch (Exception e) {
            Log.e("DeviceUtils", "getDeviceLevel failed , e:" + e.toString());
        }
    }

    private static Class getPerfClass() {
        return sPerfClass;
    }

    private static Object getPerf() {
        Constructor constructor;
        if (mPerf == null) {
            try {
                Context appContext = getAppContext();
                if (appContext != null && (constructor = mConstructor) != null) {
                    mPerf = constructor.newInstance(appContext);
                } else {
                    throw new Exception("getAppContext fail");
                }
            } catch (Exception e) {
                Log.e("DeviceUtils", "getPerf DeviceUtils(): newInstance Exception:" + e);
                e.printStackTrace();
            }
        }
        return mPerf;
    }

    private static Method getMiuiLiteVersionMethod() {
        if (mGetMiuiLiteVersion == null) {
            Class[] clsArr = new Class[0];
            mGetMiuiLiteVersion = getPerfClass().getDeclaredMethod("getMiuiLiteVersion", null);
        }
        return mGetMiuiLiteVersion;
    }

    private static boolean initDeviceLevelInfo() {
        boolean z = initDeviceLevelInfo;
        if (z) {
            return z;
        }
        try {
            Class perfClass = getPerfClass();
            if (perfClass == null) {
                return initDeviceLevelInfo;
            }
            Class<?> cls = Integer.TYPE;
            mGetDeviceLevel = perfClass.getDeclaredMethod("getDeviceLevel", cls, cls);
            mGetDeviceLevelForWhole = perfClass.getDeclaredMethod("getDeviceLevel", cls);
            TYPE_RAM = ((Integer) getStaticObjectField(perfClass, "DEVICE_LEVEL_FOR_RAM", cls)).intValue();
            TYPE_CPU = ((Integer) getStaticObjectField(perfClass, "DEVICE_LEVEL_FOR_CPU", cls)).intValue();
            TYPE_GPU = ((Integer) getStaticObjectField(perfClass, "DEVICE_LEVEL_FOR_GPU", cls)).intValue();
            LOW = ((Integer) getStaticObjectField(perfClass, "LOW_DEVICE", cls)).intValue();
            MIDDLE = ((Integer) getStaticObjectField(perfClass, "MIDDLE_DEVICE", cls)).intValue();
            HIGH = ((Integer) getStaticObjectField(perfClass, "HIGH_DEVICE", cls)).intValue();
            UNKNOWN = ((Integer) getStaticObjectField(perfClass, "DEVICE_LEVEL_UNKNOWN", cls)).intValue();
            initDeviceLevelInfo = true;
            return initDeviceLevelInfo;
        } catch (Exception e) {
            Log.e("DeviceUtils", "initDeviceLevelInfo Fail: Exception:" + e);
        }
    }

    private static Context getAppContext() {
        if (applicationContext == null) {
            try {
                Class[] clsArr = new Class[0];
                Application application2 = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication", null).invoke(null, null);
                application = application2;
                if (application2 != null) {
                    applicationContext = application2.getApplicationContext();
                }
            } catch (Exception e) {
                Log.e("DeviceUtils", "android.app.ActivityThread Exception:" + e);
            }
        }
        if (applicationContext == null) {
            try {
                Class[] clsArr2 = new Class[0];
                Application application3 = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication", null).invoke(null, null);
                application = application3;
                if (application3 != null) {
                    applicationContext = application3.getApplicationContext();
                }
            } catch (Exception e2) {
                Log.e("DeviceUtils", "android.app.AppGlobals Exception:" + e2);
            }
        }
        return applicationContext;
    }

    private static int transDeviceLevel(int i) {
        if (i == LOW) {
            return 0;
        }
        if (i == MIDDLE) {
            return 1;
        }
        return i == HIGH ? 2 : -1;
    }

    private static Object getStaticObjectField(Class cls, String str, Class cls2) throws NoSuchFieldException {
        Field declaredField = cls.getDeclaredField(str);
        declaredField.setAccessible(true);
        return declaredField.get(null);
    }
}
