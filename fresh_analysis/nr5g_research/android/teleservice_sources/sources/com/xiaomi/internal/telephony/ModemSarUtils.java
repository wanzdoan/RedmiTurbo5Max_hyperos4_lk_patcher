package com.xiaomi.internal.telephony;

import android.os.SystemProperties;
import java.io.File;
import java.io.FileInputStream;
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
    public static final String PROPERTY_DEVICE_NAME = "ro.product.device";
    public static final String PROPERTY_DYNAMIC_SAR = "persist.vendor.radio.dynamic_sar";
    public static final String PROPERTY_FACTORY_BUILD = "ro.boot.factorybuild";
    public static final String PROPERTY_NTN_SAR = "persist.vendor.radio.satellite_sar";
    public static final String PROPERTY_VALUE_DEFAULT = "null";
    public static final String PROPERTY_VALUE_IS_FACTORY_BUILD = "1";
    public static final String PROPERTY_VALUE_IS_SAR_CLOSED = "0";
    public static final String PROPERTY_VALUE_IS_SAR_ENABLED = "1";
    public static final String PROPERTY_VALUE_IS_SAR_SENSOR_ONE_TYPE = "true";
    public static final String PROPERTY_VALUE_IS_SAR_TEST = "2";
    public static final String PROPERTY_DEVICE_HWC = "ro.boot.hwc";
    private static final List PROP_LIST_NAME = new ArrayList(Arrays.asList(PROPERTY_DEVICE_HWC));
    public static final String PROPERTY_PRODUCT_MOD_DEVICE = "ro.product.mod_device";
    public static final String PROPERTY_SAR_SENSOR_ONE_TYPE = "persist.vendor.radio.sar_sensor_one_type";
    public static final String PROPERTY_HWID_VER = "ro.boot.hwversion";
    private static final List EXTEND_PROP_LIST_NAME = new ArrayList(Arrays.asList(PROPERTY_PRODUCT_MOD_DEVICE, PROPERTY_SAR_SENSOR_ONE_TYPE, PROPERTY_HWID_VER));
    private static final List PROP_NON_STRONG_MATCH_LIST_NAME = new ArrayList(Arrays.asList(new String[0]));
    private static final List EXTEND_PROP_NON_STRONG_MATCH_LIST_NAME = new ArrayList(Arrays.asList(PROPERTY_PRODUCT_MOD_DEVICE, PROPERTY_HWID_VER));

    public static XMLContentHandler readSarXmlBySAX(String str) {
        ModemSarLog.d(LOG_TAG, "start to readXmlConfig");
        XMLContentHandler xMLContentHandler = null;
        try {
            if (!new File(str).exists()) {
                ModemSarLog.e(LOG_TAG, "File not exist!!!");
                return null;
            }
            FileInputStream fileInputStream = new FileInputStream(str);
            try {
                SAXParser sAXParserNewSAXParser = SAXParserFactory.newInstance().newSAXParser();
                XMLContentHandler xMLContentHandler2 = new XMLContentHandler();
                try {
                    sAXParserNewSAXParser.parse(fileInputStream, xMLContentHandler2);
                    fileInputStream.close();
                    return xMLContentHandler2;
                } catch (Exception e) {
                    e = e;
                    xMLContentHandler = xMLContentHandler2;
                    e.printStackTrace();
                    ModemSarLog.e(LOG_TAG, "Parse xml failed!!!");
                    return xMLContentHandler;
                }
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            ModemSarLog.e(LOG_TAG, "Get file stream failed!!!");
            return null;
        }
    }

    public static DeviceSarConfig initModemSarConfig(List list, String str) {
        DeviceSarConfig deviceSarConfigFindSarConfig;
        String str2 = SystemProperties.get(PROPERTY_DEVICE_NAME, "null");
        if (str.equals("1")) {
            deviceSarConfigFindSarConfig = findSarConfig(list, str2, getPropList(), getExtendPropList());
            ModemSarLog.d(LOG_TAG, "Feature is supported");
        } else if (str.equals("2")) {
            deviceSarConfigFindSarConfig = findSarConfig(list, "test", getTestPropList(), getExtendPropList());
            ModemSarLog.d(LOG_TAG, "Feature is test mode");
        } else {
            ModemSarLog.e(LOG_TAG, "unkonw switch value");
            deviceSarConfigFindSarConfig = null;
        }
        ModemSarLog.d(LOG_TAG, "init modem config done");
        return deviceSarConfigFindSarConfig;
    }

    public static int findDsiFromXML(char[] cArr, DeviceSarConfig deviceSarConfig, int i) {
        if (cArr.length < 1) {
            ModemSarLog.e(LOG_TAG, "findIndexFromConfig, sourStateList is null. please check reg states");
            return i;
        }
        for (Map.Entry entry : deviceSarConfig.getDsiList().entrySet()) {
            ModemSarLog.i(LOG_TAG, "findIndexFromConfig, DerStateSet = " + ((String) entry.getKey()) + ", index = " + ((String) entry.getValue()));
            char[] charArray = ((String) entry.getKey()).toCharArray();
            if (cArr.length == charArray.length) {
                boolean z = false;
                int i2 = 0;
                boolean z2 = false;
                int i3 = 0;
                while (true) {
                    if (i2 >= cArr.length) {
                        z = z2;
                        i2 = i3;
                        break;
                    }
                    char c = charArray[i2];
                    if ('F' != c && cArr[i2] != c) {
                        break;
                    }
                    i3 = i2;
                    i2++;
                    z2 = true;
                }
                ModemSarLog.i(LOG_TAG, "findIndexFromConfig, len = " + i2 + ", sourStrLen = " + cArr.length);
                if (i2 == cArr.length - 1 && z) {
                    int i4 = Integer.parseInt((String) entry.getValue());
                    ModemSarLog.d(LOG_TAG, "findIndexFromConfig success: result = " + i4);
                    return i4;
                }
            } else {
                ModemSarLog.e(LOG_TAG, "findIndexFromConfig, sourStateSet size is not same as desStateList, please check again.");
                break;
            }
        }
        return i;
    }

    public static boolean isModemSarSwitchOff() {
        boolean zEquals = getModemSarSwitch().equals("0");
        ModemSarLog.i(LOG_TAG, "isModemSwitchOff, result = " + zEquals);
        return zEquals;
    }

    public static boolean isNtnSarSwitchOff() {
        boolean zEquals = getNtnSarSwitch().equals("0");
        ModemSarLog.i(LOG_TAG, "isNtnSwitchOff, result = " + zEquals);
        return zEquals;
    }

    public static boolean isFactoryBuild() {
        boolean zEquals = SystemProperties.get(PROPERTY_FACTORY_BUILD, "null").equals("1");
        ModemSarLog.i(LOG_TAG, "isFactoryBuild, result = " + zEquals);
        return zEquals;
    }

    public static String getModemSarSwitch() {
        String str = SystemProperties.get(PROPERTY_DYNAMIC_SAR, "null");
        ModemSarLog.i(LOG_TAG, "getModemSarSwitch, result = " + str);
        return str;
    }

    public static String getNtnSarSwitch() {
        String str = SystemProperties.get(PROPERTY_NTN_SAR, "null");
        ModemSarLog.i(LOG_TAG, "getNtnSarSwitch, result = " + str);
        return str;
    }

    public static byte[] getBytes(int i, int i2) {
        byte[] bArr = new byte[8];
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        byteBufferWrap.order(ByteOrder.nativeOrder());
        byteBufferWrap.putInt(i);
        byteBufferWrap.putInt(i2);
        return bArr;
    }

    private static DeviceSarConfig findSarConfig(List list, String str, Map map, Map map2) {
        int i;
        int i2;
        if (list == null) {
            ModemSarLog.e(LOG_TAG, "findConfig, Config is null");
            return null;
        }
        TreeMap treeMap = new TreeMap();
        for (int i3 = 0; i3 < list.size(); i3++) {
            DeviceSarConfig deviceSarConfig = (DeviceSarConfig) list.get(i3);
            String deviceName = deviceSarConfig.getDeviceName();
            Map propList = deviceSarConfig.getPropList();
            Map extendPropList = deviceSarConfig.getExtendPropList();
            ModemSarLog.i(LOG_TAG, "findConfig, propListFromConfig = " + propList + ", extendPropListFromConfig" + extendPropList);
            if (!deviceName.equalsIgnoreCase(str)) {
                ModemSarLog.e(LOG_TAG, "findConfig, deviceName is error, continue");
            } else {
                Iterator it = PROP_LIST_NAME.iterator();
                int i4 = 1;
                boolean z = true;
                while (true) {
                    if (!it.hasNext()) {
                        i = 0;
                        i2 = 1;
                        break;
                    }
                    String str2 = (String) it.next();
                    i = 0;
                    i2 = 1;
                    ModemSarLog.i(LOG_TAG, "findConfig, prop = " + str2);
                    if (((String) map.get(str2)).equalsIgnoreCase((String) propList.get(str2))) {
                        ModemSarLog.i(LOG_TAG, "findConfig, value is equal, value = " + ((String) map.get(str2)));
                    } else if (PROP_NON_STRONG_MATCH_LIST_NAME.contains(str2) && (((String) propList.get(str2)).contains((CharSequence) map.get(str2)) || ((String) map.get(str2)).contains((CharSequence) propList.get(str2)))) {
                        ModemSarLog.i(LOG_TAG, "findConfig, config value contains device value, config value = " + ((String) propList.get(str2)) + "device value = " + ((String) map.get(str2)));
                    } else if (((String) propList.get(str2)).contains("default")) {
                        ModemSarLog.i(LOG_TAG, "findConfig, default config");
                        i4 = 0;
                    } else {
                        ModemSarLog.i(LOG_TAG, "findConfig, prop value is mismatched");
                        i4 = 0;
                        z = false;
                        break;
                    }
                    z = false;
                }
                if (i4 == 0 || z) {
                    if (!z || i4 != 0) {
                        ModemSarLog.i(LOG_TAG, "findConfig, prop mismatch, enter next config");
                    } else {
                        treeMap.put(Integer.valueOf(i), deviceSarConfig);
                        ModemSarLog.i(LOG_TAG, "findConfig, a default config matched");
                    }
                } else if (extendPropList.isEmpty()) {
                    treeMap.put(Integer.valueOf(i2), deviceSarConfig);
                    ModemSarLog.i(LOG_TAG, "findConfig, a non extend prop config matched");
                } else {
                    int i5 = i;
                    for (String str3 : extendPropList.keySet()) {
                        if (!map2.containsKey(str3)) {
                            ModemSarLog.i(LOG_TAG, "findConfig, extend prop is mismatched");
                        } else {
                            if (((String) map2.get(str3)).equalsIgnoreCase((String) extendPropList.get(str3))) {
                                ModemSarLog.i(LOG_TAG, "findConfig, extend prop value is equal, value = " + ((String) map2.get(str3)));
                            } else if (!EXTEND_PROP_NON_STRONG_MATCH_LIST_NAME.contains(str3) || (!((String) extendPropList.get(str3)).contains((CharSequence) map2.get(str3)) && !((String) map2.get(str3)).contains((CharSequence) extendPropList.get(str3)))) {
                                ModemSarLog.i(LOG_TAG, "findConfig, extend prop value is mismatched");
                            } else {
                                ModemSarLog.i(LOG_TAG, "findConfig, extend config value contains device value, config value = " + ((String) extendPropList.get(str3)) + ", device value = " + ((String) map2.get(str3)));
                            }
                            i5++;
                        }
                        i4 = i;
                    }
                    if (i4 != 0) {
                        treeMap.put(Integer.valueOf(i5 + 1), deviceSarConfig);
                        ModemSarLog.i(LOG_TAG, "findConfig, a extend prop config matched, matchNum = " + i5);
                    }
                }
            }
        }
        if (treeMap.size() <= 0) {
            return null;
        }
        DeviceSarConfig deviceSarConfig2 = (DeviceSarConfig) treeMap.lastEntry().getValue();
        ModemSarLog.d(LOG_TAG, "findConfig, find corresponding config");
        return deviceSarConfig2;
    }

    private static Map getPropList() {
        HashMap map = new HashMap();
        for (String str : PROP_LIST_NAME) {
            String str2 = SystemProperties.get(str, "null");
            map.put(str, str2);
            ModemSarLog.i(LOG_TAG, str + ": " + str2);
        }
        ModemSarLog.i(LOG_TAG, "getPropList, propList = " + map);
        return map;
    }

    private static Map getTestPropList() {
        HashMap map = new HashMap();
        Iterator it = PROP_LIST_NAME.iterator();
        while (it.hasNext()) {
            map.put((String) it.next(), "test");
        }
        ModemSarLog.i(LOG_TAG, "getTestPropList, propList = " + map);
        return map;
    }

    private static Map getExtendPropList() {
        HashMap map = new HashMap();
        for (String str : EXTEND_PROP_LIST_NAME) {
            String str2 = SystemProperties.get(str, "null");
            if (str2.equals("null")) {
                ModemSarLog.i(LOG_TAG, str + " is null");
            } else {
                map.put(str, str2);
                ModemSarLog.i(LOG_TAG, str + ": " + str2);
            }
        }
        ModemSarLog.i(LOG_TAG, "getExtendPropList, extendPropList = " + map);
        return map;
    }

    private static String replacePreString(String str) {
        return str.contains("_pre") ? str.replace("_pre", "") : str;
    }
}
