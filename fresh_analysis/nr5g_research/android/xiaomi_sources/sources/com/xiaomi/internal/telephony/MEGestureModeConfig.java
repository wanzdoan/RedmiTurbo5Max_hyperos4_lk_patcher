package com.xiaomi.internal.telephony;

import android.os.SystemProperties;
import com.xiaomi.modem.ModemUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* JADX INFO: loaded from: classes.dex */
public class MEGestureModeConfig {
    private static final String TAG = "MEGestureModeConfig";
    private String bitMaskBinary;
    private int bitMask = -1;
    private List<Map<String, Object>> configItems = new ArrayList();
    private List<Map<String, Object>> controlMap = new ArrayList();
    private List<List<Integer>> controlTxForceMap = new ArrayList();
    private boolean isConfigLoaded = false;

    public static boolean initialize(String configPath, MEGestureModeConfig config) {
        try {
            File xmlFile = new File(configPath);
            if (!xmlFile.exists()) {
                MELog.e(TAG, "Configuration file not found: " + configPath);
                return false;
            }
            if (!xmlFile.canRead()) {
                MELog.e(TAG, "Cannot read configuration file: " + configPath);
                return false;
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            String deviceName = SystemProperties.get("ro.product.device", "");
            String hwcName = SystemProperties.get("ro.boot.hwc", "");
            MELog.i(TAG, "Current device: device_name=" + deviceName + ", hwc_name=" + hwcName);
            Element targetDeviceConfig = findMatchingDeviceConfig(doc, deviceName, hwcName);
            if (targetDeviceConfig == null) {
                MELog.e(TAG, "No matching deviceConfig found for device_name=" + deviceName + ", hwc_name=" + hwcName);
                return false;
            }
            return parseDeviceConfig(targetDeviceConfig, config);
        } catch (FileNotFoundException e) {
            MELog.e(TAG, "Configuration file not found: " + configPath);
            e.printStackTrace();
            return false;
        } catch (Exception e2) {
            MELog.e(TAG, "Error parsing configuration file: " + configPath);
            return false;
        }
    }

    private static Element findMatchingDeviceConfig(Document doc, String deviceName, String hwcName) {
        NodeList deviceConfigs = doc.getElementsByTagName("deviceConfig");
        Element matchedDefault = null;
        for (int i = 0; i < deviceConfigs.getLength(); i++) {
            Node node = deviceConfigs.item(i);
            if (node.getNodeType() == 1) {
                Element elem = (Element) node;
                String xmlDeviceName = elem.getAttribute("device_name");
                NodeList hwcNodes = elem.getElementsByTagName("hwc");
                String xmlHwcName = "";
                if (hwcNodes.getLength() > 0) {
                    Node hwcNode = hwcNodes.item(0);
                    if (hwcNode.getNodeType() == 1) {
                        xmlHwcName = ((Element) hwcNode).getAttribute("hwc_name");
                    }
                }
                if (deviceName.equals(xmlDeviceName) && hwcName.equals(xmlHwcName)) {
                    MELog.d(TAG, "Exact match found: device_name=" + xmlDeviceName + ", hwc_name=" + xmlHwcName);
                    return elem;
                }
                if (ModemUtils.PROP_NTN_LOG_LEVEL_DEFAULT.equals(xmlHwcName)) {
                    matchedDefault = elem;
                }
            }
        }
        if (matchedDefault != null) {
            MELog.i(TAG, "Falling back to default configuration");
            return matchedDefault;
        }
        MELog.e(TAG, "No matching configuration found, including default");
        return null;
    }

    private static boolean parseDeviceConfig(Element deviceConfig, MEGestureModeConfig config) {
        boolean z;
        NodeList bitMaskNodes;
        NodeList mapItems;
        boolean z2 = false;
        try {
            NodeList bitMaskNodes2 = deviceConfig.getElementsByTagName("GestureModeMapConfigBitMask");
            if (bitMaskNodes2.getLength() == 0) {
                MELog.e(TAG, "Missing GestureModeMapConfigBitMask element");
                return false;
            }
            Node bitMaskNode = bitMaskNodes2.item(0);
            short s = 1;
            if (bitMaskNode.getNodeType() != 1) {
                z = false;
            } else {
                config.bitMask = safeParseInt(((Element) bitMaskNode).getAttribute("bitMask"));
                config.bitMaskBinary = Integer.toBinaryString(config.bitMask);
                NodeList configItemList = deviceConfig.getElementsByTagName("NWCoexistMapItem");
                Set<String> conditionKeys = new HashSet<>();
                int i = 0;
                while (i < configItemList.getLength()) {
                    Node itemNode = configItemList.item(i);
                    if (itemNode.getNodeType() != 1) {
                        z = z2;
                    } else {
                        NamedNodeMap attrs = itemNode.getAttributes();
                        int j = 0;
                        while (true) {
                            z = z2;
                            try {
                                if (j < attrs.getLength()) {
                                    String attrName = attrs.item(j).getNodeName();
                                    if (!"outMode".equals(attrName)) {
                                        conditionKeys.add(attrName);
                                    }
                                    j++;
                                    z2 = z;
                                }
                            } catch (Exception e) {
                                MELog.e(TAG, "Error parsing deviceConfig");
                                return z;
                            }
                        }
                    }
                    i++;
                    z2 = z;
                }
                z = z2;
                int maxConditions = conditionKeys.size();
                while (config.bitMaskBinary.length() < maxConditions) {
                    config.bitMaskBinary = "0" + config.bitMaskBinary;
                }
            }
            NodeList mapItems2 = deviceConfig.getElementsByTagName("NWCoexistMapItem");
            config.configItems.clear();
            int i2 = 0;
            while (i2 < mapItems2.getLength()) {
                Node itemNode2 = mapItems2.item(i2);
                if (itemNode2.getNodeType() != s) {
                    bitMaskNodes = bitMaskNodes2;
                    mapItems = mapItems2;
                } else {
                    Map<String, Object> item = new HashMap<>();
                    NamedNodeMap attrs2 = ((Element) itemNode2).getAttributes();
                    Map<String, Integer> conditions = new HashMap<>();
                    int j2 = 0;
                    while (j2 < attrs2.getLength()) {
                        Node attr = attrs2.item(j2);
                        String name = attr.getNodeName();
                        int value = safeParseInt(attr.getNodeValue());
                        NodeList bitMaskNodes3 = bitMaskNodes2;
                        if ("outMode".equals(name)) {
                            item.put("mode", Integer.valueOf(value));
                        } else {
                            conditions.put(name, Integer.valueOf(value));
                        }
                        j2++;
                        mapItems2 = mapItems2;
                        bitMaskNodes2 = bitMaskNodes3;
                    }
                    bitMaskNodes = bitMaskNodes2;
                    mapItems = mapItems2;
                    item.put("conditions", conditions);
                    config.configItems.add(item);
                }
                i2++;
                mapItems2 = mapItems;
                bitMaskNodes2 = bitMaskNodes;
                s = 1;
            }
            NodeList controlItems = deviceConfig.getElementsByTagName("NWCoexistControlMapItem");
            config.controlMap.clear();
            for (int i3 = 0; i3 < controlItems.getLength(); i3++) {
                Node itemNode3 = controlItems.item(i3);
                if (itemNode3.getNodeType() == 1) {
                    Element element = (Element) itemNode3;
                    Map<String, Object> item2 = new HashMap<>();
                    item2.put("mode", Integer.valueOf(safeParseInt(element.getAttribute("mode"))));
                    List<Integer> gpios = new ArrayList<>();
                    NamedNodeMap attrs3 = element.getAttributes();
                    for (int j3 = 0; j3 < attrs3.getLength(); j3++) {
                        Node attr2 = attrs3.item(j3);
                        if (attr2.getNodeName().startsWith("outGPIO")) {
                            gpios.add(Integer.valueOf(safeParseInt(attr2.getNodeValue())));
                        }
                    }
                    item2.put("gpios", gpios);
                    config.controlMap.add(item2);
                }
            }
            NodeList controlTxForceItems = deviceConfig.getElementsByTagName("ControlTxForceMapItem");
            config.controlTxForceMap.clear();
            for (int i4 = 0; i4 < controlTxForceItems.getLength(); i4++) {
                Node itemNode4 = controlTxForceItems.item(i4);
                if (itemNode4.getNodeType() == 1) {
                    Element element2 = (Element) itemNode4;
                    List<Integer> txforce = new ArrayList<>();
                    txforce.add(Integer.valueOf(safeParseInt(element2.getAttribute("Rat"))));
                    txforce.add(Integer.valueOf(safeParseInt(element2.getAttribute("Band"))));
                    txforce.add(Integer.valueOf(safeParseInt(element2.getAttribute("Tas_State"))));
                    config.controlTxForceMap.add(txforce);
                }
            }
            config.isConfigLoaded = true;
            MELog.i(TAG, "Successfully loaded deviceConfig with " + config.configItems.size() + " mapping rules");
            return true;
        } catch (Exception e2) {
            z = false;
        }
    }

    public Integer getOutMode(Map<String, Integer> inputParams) {
        Integer num;
        Integer num2 = null;
        if (!this.isConfigLoaded) {
            MELog.e(TAG, "Configuration not loaded, cannot get outMode");
            return null;
        }
        for (Map<String, Object> item : this.configItems) {
            boolean match = true;
            Map<String, Integer> conditions = (Map) item.get("conditions");
            Set<String> keys = conditions.keySet();
            List<String> keyList = new ArrayList<>(keys);
            Iterator<String> it = keyList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    num = num2;
                    break;
                }
                String key = it.next();
                int inputValue = inputParams.getOrDefault(key, -1).intValue();
                int conditionValue = conditions.get(key).intValue();
                int index = (keyList.size() - keyList.indexOf(key)) - 1;
                if (this.bitMaskBinary.length() > index) {
                    num = num2;
                    if (this.bitMaskBinary.charAt((this.bitMaskBinary.length() - 1) - index) == '0') {
                        num2 = num;
                    } else {
                        if (conditionValue != 255 && conditionValue != inputValue) {
                            match = false;
                            break;
                        }
                        num2 = num;
                    }
                }
            }
            if (match) {
                Integer mode = (Integer) item.get("mode");
                MELog.d(TAG, "Match found: " + inputParams + " -> outMode=" + mode);
                return mode;
            }
            num2 = num;
        }
        Integer num3 = num2;
        MELog.d(TAG, "No matching outMode found: " + inputParams);
        return num3;
    }

    public List<List<Integer>> getGsmTxForceSettings() {
        if (this.controlTxForceMap == null) {
            MELog.d(TAG, "controlTxForceMap is NULL !!");
        }
        return this.controlTxForceMap;
    }

    public String getGpioSettings(int outMode) {
        if (!this.isConfigLoaded) {
            MELog.e(TAG, "Configuration not loaded, cannot get GPIO settings");
            return null;
        }
        for (Map<String, Object> item : this.controlMap) {
            if (((Integer) item.get("mode")).intValue() == outMode) {
                List<Integer> gpios = (List) item.get("gpios");
                StringBuilder gpioString = new StringBuilder();
                Iterator<Integer> it = gpios.iterator();
                while (it.hasNext()) {
                    int gpioValue = it.next().intValue();
                    if (gpioValue != 255) {
                        gpioString.append(gpioValue);
                    }
                }
                String result = gpioString.toString();
                MELog.d(TAG, "outMode=" + outMode + " -> GPIO=" + result);
                return result;
            }
        }
        MELog.d(TAG, "No GPIO settings found for mode: " + outMode);
        return null;
    }

    private static int safeParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            MELog.e(TAG, "Invalid integer value: " + value);
            e.printStackTrace();
            return -1;
        }
    }

    public boolean isConfigLoaded() {
        return this.isConfigLoaded;
    }
}
