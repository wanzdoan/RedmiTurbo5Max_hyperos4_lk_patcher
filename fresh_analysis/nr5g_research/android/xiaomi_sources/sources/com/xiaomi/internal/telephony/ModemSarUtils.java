package com.xiaomi.internal.telephony;

import android.os.SystemProperties;
import com.xiaomi.modem.ModemUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/* JADX INFO: loaded from: classes.dex */
public class ModemSarUtils {
    private static final String LOG_TAG = "ModemRadUtils";
    public static final String PATH_NTN_SAR_CONFIG = "/odm/etc/device_ntn_sar_config.xml";
    public static final String PATH_SAR_CONFIG = "/odm/etc/device_sar_config.xml";
    public static final String PROPERTY_DEVICE_HWC = "ro.boot.hwc";
    public static final String PROPERTY_DEVICE_NAME = "ro.product.device";
    public static final String PROPERTY_DYNAMIC_SAR = "persist.vendor.radio.dynamic_sar";
    public static final String PROPERTY_FACTORY_BUILD = "ro.boot.factorybuild";
    public static final String PROPERTY_NTN_SAR = "persist.vendor.radio.satellite_sar";
    public static final String PROPERTY_PRODUCT_MOD_DEVICE = "ro.product.mod_device";
    public static final String PROPERTY_VALUE_DEFAULT = "null";
    public static final String PROPERTY_VALUE_IS_FACTORY_BUILD = "1";
    public static final String PROPERTY_VALUE_IS_SAR_CLOSED = "0";
    public static final String PROPERTY_VALUE_IS_SAR_ENABLED = "1";
    public static final String PROPERTY_VALUE_IS_SAR_SENSOR_ONE_TYPE = "true";
    public static final String PROPERTY_VALUE_IS_SAR_TEST = "2";
    private static final List<String> PROP_LIST_NAME = new ArrayList(Arrays.asList("ro.boot.hwc"));
    public static final String PROPERTY_SAR_SENSOR_ONE_TYPE = "persist.vendor.radio.sar_sensor_one_type";
    public static final String PROPERTY_HWID_VER = "ro.boot.hwversion";
    private static final List<String> EXTEND_PROP_LIST_NAME = new ArrayList(Arrays.asList("ro.product.mod_device", PROPERTY_SAR_SENSOR_ONE_TYPE, PROPERTY_HWID_VER));
    private static final List<String> PROP_NON_STRONG_MATCH_LIST_NAME = new ArrayList(Arrays.asList(new String[0]));
    private static final List<String> EXTEND_PROP_NON_STRONG_MATCH_LIST_NAME = new ArrayList(Arrays.asList("ro.product.mod_device", PROPERTY_HWID_VER));

    public static XMLContentHandler readSarXmlBySAX(String url) {
        ModemSarLog.d(LOG_TAG, "start to readXmlConfig");
        XMLContentHandler handler = null;
        try {
            File urlFile = new File(url);
            if (!urlFile.exists()) {
                ModemSarLog.e(LOG_TAG, "File not exist!!!");
                return null;
            }
            InputStream inputStream = new FileInputStream(url);
            try {
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser saxParser = spf.newSAXParser();
                handler = new XMLContentHandler();
                saxParser.parse(inputStream, handler);
                inputStream.close();
                return handler;
            } catch (Exception e) {
                e.printStackTrace();
                ModemSarLog.e(LOG_TAG, "Parse xml failed!!!");
                return handler;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            ModemSarLog.e(LOG_TAG, "Get file stream failed!!!");
            return null;
        }
    }

    public static DeviceSarConfig initModemSarConfig(List<DeviceSarConfig> deviceSarConfigList, String sarSwitch) {
        DeviceSarConfig currentSarConfig = null;
        String deviceName = SystemProperties.get("ro.product.device", "null");
        if (sarSwitch.equals("1")) {
            currentSarConfig = findSarConfig(deviceSarConfigList, deviceName, getPropList(), getExtendPropList());
            ModemSarLog.d(LOG_TAG, "Feature is supported");
        } else if (sarSwitch.equals("2")) {
            currentSarConfig = findSarConfig(deviceSarConfigList, "test", getTestPropList(), getExtendPropList());
            ModemSarLog.d(LOG_TAG, "Feature is test mode");
        } else {
            ModemSarLog.e(LOG_TAG, "unkonw switch value");
        }
        ModemSarLog.d(LOG_TAG, "init modem config done");
        return currentSarConfig;
    }

    public static int findDsiFromXML(char[] sourStateList, DeviceSarConfig currentSarConfig, int defaultDsi) {
        if (sourStateList.length < 1) {
            ModemSarLog.e(LOG_TAG, "findIndexFromConfig, sourStateList is null. please check reg states");
            return defaultDsi;
        }
        Map<String, String> dsiMap = currentSarConfig.getDsiList();
        for (Map.Entry<String, String> entry : dsiMap.entrySet()) {
            ModemSarLog.i(LOG_TAG, "findIndexFromConfig, DerStateSet = " + entry.getKey() + ", index = " + entry.getValue());
            boolean flag = false;
            char[] desStateList = entry.getKey().toCharArray();
            if (sourStateList.length == desStateList.length) {
                int len = 0;
                for (int i = 0; i < sourStateList.length; i++) {
                    len = i;
                    if ('F' == desStateList[i] || sourStateList[i] == desStateList[i]) {
                        flag = true;
                    } else {
                        flag = false;
                        break;
                    }
                }
                ModemSarLog.i(LOG_TAG, "findIndexFromConfig, len = " + len + ", sourStrLen = " + sourStateList.length);
                if (len == sourStateList.length - 1 && flag) {
                    int result = Integer.parseInt(entry.getValue());
                    ModemSarLog.d(LOG_TAG, "findIndexFromConfig success: result = " + result);
                    return result;
                }
            } else {
                ModemSarLog.e(LOG_TAG, "findIndexFromConfig, sourStateSet size is not same as desStateList, please check again.");
                return defaultDsi;
            }
        }
        return defaultDsi;
    }

    public static boolean isModemSarSwitchOff() {
        boolean isSarSwitchOff = false;
        String sarSwitch = getModemSarSwitch();
        if (sarSwitch.equals("0")) {
            isSarSwitchOff = true;
        }
        ModemSarLog.i(LOG_TAG, "isModemSwitchOff, result = " + isSarSwitchOff);
        return isSarSwitchOff;
    }

    public static boolean isNtnSarSwitchOff() {
        boolean isSarSwitchOff = false;
        String sarSwitch = getNtnSarSwitch();
        if (sarSwitch.equals("0")) {
            isSarSwitchOff = true;
        }
        ModemSarLog.i(LOG_TAG, "isNtnSwitchOff, result = " + isSarSwitchOff);
        return isSarSwitchOff;
    }

    public static boolean isFactoryBuild() {
        boolean isFactoryBuild = false;
        String factoryBuild = SystemProperties.get("ro.boot.factorybuild", "null");
        if (factoryBuild.equals("1")) {
            isFactoryBuild = true;
        }
        ModemSarLog.i(LOG_TAG, "isFactoryBuild, result = " + isFactoryBuild);
        return isFactoryBuild;
    }

    public static String getModemSarSwitch() {
        String sarSwitch = SystemProperties.get("persist.vendor.radio.dynamic_sar", "null");
        ModemSarLog.i(LOG_TAG, "getModemSarSwitch, result = " + sarSwitch);
        return sarSwitch;
    }

    public static String getNtnSarSwitch() {
        String sarSwitch = SystemProperties.get(PROPERTY_NTN_SAR, "null");
        ModemSarLog.i(LOG_TAG, "getNtnSarSwitch, result = " + sarSwitch);
        return sarSwitch;
    }

    public static byte[] getBytes(int arg1, int arg2) {
        byte[] bytes = new byte[8];
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        buf.order(ByteOrder.nativeOrder());
        buf.putInt(arg1);
        buf.putInt(arg2);
        return bytes;
    }

    private static DeviceSarConfig findSarConfig(List<DeviceSarConfig> deviceSarConfigList, String deviceName, Map<String, String> propList, Map<String, String> extendPropList) {
        DeviceSarConfig currentSarConfig;
        List<DeviceSarConfig> list = deviceSarConfigList;
        DeviceSarConfig currentSarConfig2 = null;
        if (list == null) {
            ModemSarLog.e(LOG_TAG, "findConfig, Config is null");
            return null;
        }
        TreeMap<Integer, DeviceSarConfig> priorityMap = new TreeMap<>();
        int i = 0;
        while (i < list.size()) {
            DeviceSarConfig sarConfig = list.get(i);
            boolean isCurrentConfig = true;
            boolean isDefualtFlag = true;
            String deviceNameFromConfig = sarConfig.getDeviceName();
            Map<String, String> propListFromConfig = sarConfig.getPropList();
            Map<String, String> extendPropListFromConfig = sarConfig.getExtendPropList();
            ModemSarLog.i(LOG_TAG, "findConfig, propListFromConfig = " + propListFromConfig + ", extendPropListFromConfig" + extendPropListFromConfig);
            if (deviceNameFromConfig.equalsIgnoreCase(deviceName)) {
                Iterator<String> it = PROP_LIST_NAME.iterator();
                while (true) {
                    if (it.hasNext()) {
                        String key = it.next();
                        currentSarConfig = currentSarConfig2;
                        ModemSarLog.i(LOG_TAG, "findConfig, prop = " + key);
                        if (propList.get(key).equalsIgnoreCase(propListFromConfig.get(key))) {
                            ModemSarLog.i(LOG_TAG, "findConfig, value is equal, value = " + propList.get(key));
                            isCurrentConfig &= true;
                            isDefualtFlag &= false;
                        } else if (PROP_NON_STRONG_MATCH_LIST_NAME.contains(key) && (propListFromConfig.get(key).contains(propList.get(key)) || propList.get(key).contains(propListFromConfig.get(key)))) {
                            ModemSarLog.i(LOG_TAG, "findConfig, config value contains device value, config value = " + propListFromConfig.get(key) + "device value = " + propList.get(key));
                            isCurrentConfig &= true;
                            isDefualtFlag &= false;
                        } else if (propListFromConfig.get(key).contains(ModemUtils.PROP_NTN_LOG_LEVEL_DEFAULT)) {
                            ModemSarLog.i(LOG_TAG, "findConfig, default config");
                            isDefualtFlag &= true;
                            isCurrentConfig &= false;
                        } else {
                            ModemSarLog.i(LOG_TAG, "findConfig, prop value is mismatched");
                            isCurrentConfig &= false;
                            isDefualtFlag &= false;
                            break;
                        }
                        currentSarConfig2 = currentSarConfig;
                    } else {
                        currentSarConfig = currentSarConfig2;
                        break;
                    }
                }
                if (isCurrentConfig && !isDefualtFlag) {
                    if (extendPropListFromConfig.isEmpty()) {
                        priorityMap.put(1, sarConfig);
                        ModemSarLog.i(LOG_TAG, "findConfig, a non extend prop config matched");
                    } else {
                        int matchNum = 0;
                        for (String key2 : extendPropListFromConfig.keySet()) {
                            if (extendPropList.containsKey(key2)) {
                                if (extendPropList.get(key2).equalsIgnoreCase(extendPropListFromConfig.get(key2))) {
                                    ModemSarLog.i(LOG_TAG, "findConfig, extend prop value is equal, value = " + extendPropList.get(key2));
                                } else if (EXTEND_PROP_NON_STRONG_MATCH_LIST_NAME.contains(key2) && (extendPropListFromConfig.get(key2).contains(extendPropList.get(key2)) || extendPropList.get(key2).contains(extendPropListFromConfig.get(key2)))) {
                                    ModemSarLog.i(LOG_TAG, "findConfig, extend config value contains device value, config value = " + extendPropListFromConfig.get(key2) + ", device value = " + extendPropList.get(key2));
                                } else {
                                    isCurrentConfig &= false;
                                    ModemSarLog.i(LOG_TAG, "findConfig, extend prop value is mismatched");
                                    break;
                                }
                                matchNum++;
                                isCurrentConfig &= true;
                            } else {
                                isCurrentConfig &= false;
                                ModemSarLog.i(LOG_TAG, "findConfig, extend prop is mismatched");
                                break;
                            }
                        }
                        if (isCurrentConfig) {
                            priorityMap.put(Integer.valueOf(matchNum + 1), sarConfig);
                            ModemSarLog.i(LOG_TAG, "findConfig, a extend prop config matched, matchNum = " + matchNum);
                        }
                    }
                } else if (!isDefualtFlag || isCurrentConfig) {
                    ModemSarLog.i(LOG_TAG, "findConfig, prop mismatch, enter next config");
                } else {
                    priorityMap.put(0, sarConfig);
                    ModemSarLog.i(LOG_TAG, "findConfig, a default config matched");
                }
            } else {
                currentSarConfig = currentSarConfig2;
                ModemSarLog.e(LOG_TAG, "findConfig, deviceName is error, continue");
            }
            i++;
            list = deviceSarConfigList;
            currentSarConfig2 = currentSarConfig;
        }
        DeviceSarConfig currentSarConfig3 = currentSarConfig2;
        if (priorityMap.size() <= 0) {
            return currentSarConfig3;
        }
        DeviceSarConfig currentSarConfig4 = priorityMap.lastEntry().getValue();
        ModemSarLog.d(LOG_TAG, "findConfig, find corresponding config");
        return currentSarConfig4;
    }

    private static Map<String, String> getPropList() {
        Map<String, String> propList = new HashMap<>();
        for (String propName : PROP_LIST_NAME) {
            String value = SystemProperties.get(propName, "null");
            propList.put(propName, value);
            ModemSarLog.i(LOG_TAG, propName + ": " + value);
        }
        ModemSarLog.i(LOG_TAG, "getPropList, propList = " + propList);
        return propList;
    }

    private static Map<String, String> getTestPropList() {
        Map<String, String> testPropList = new HashMap<>();
        for (String propName : PROP_LIST_NAME) {
            testPropList.put(propName, "test");
        }
        ModemSarLog.i(LOG_TAG, "getTestPropList, propList = " + testPropList);
        return testPropList;
    }

    private static Map<String, String> getExtendPropList() {
        Map<String, String> extendPropList = new HashMap<>();
        for (String propName : EXTEND_PROP_LIST_NAME) {
            String value = SystemProperties.get(propName, "null");
            if (!value.equals("null")) {
                extendPropList.put(propName, value);
                ModemSarLog.i(LOG_TAG, propName + ": " + value);
            } else {
                ModemSarLog.i(LOG_TAG, propName + " is null");
            }
        }
        ModemSarLog.i(LOG_TAG, "getExtendPropList, extendPropList = " + extendPropList);
        return extendPropList;
    }

    private static String replacePreString(String propValue) {
        if (propValue.contains(ModemUtils.PROP_DEVICE_MOD_NAME_SUFFIX_PRE)) {
            return propValue.replace(ModemUtils.PROP_DEVICE_MOD_NAME_SUFFIX_PRE, "");
        }
        return propValue;
    }
}
