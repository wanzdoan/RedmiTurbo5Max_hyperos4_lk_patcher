package com.xiaomi.internal.telephony;

import android.os.SystemProperties;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
    private List configItems = new ArrayList();
    private List controlMap = new ArrayList();
    private List controlTxForceMap = new ArrayList();
    private boolean isConfigLoaded = false;

    public static boolean initialize(String str, MEGestureModeConfig mEGestureModeConfig) {
        try {
            File file = new File(str);
            if (!file.exists()) {
                MELog.e(TAG, "Configuration file not found: " + str);
                return false;
            }
            if (!file.canRead()) {
                MELog.e(TAG, "Cannot read configuration file: " + str);
                return false;
            }
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
            document.getDocumentElement().normalize();
            String str2 = SystemProperties.get(ModemSarUtils.PROPERTY_DEVICE_NAME, "");
            String str3 = SystemProperties.get(ModemSarUtils.PROPERTY_DEVICE_HWC, "");
            MELog.i(TAG, "Current device: device_name=" + str2 + ", hwc_name=" + str3);
            Element elementFindMatchingDeviceConfig = findMatchingDeviceConfig(document, str2, str3);
            if (elementFindMatchingDeviceConfig == null) {
                MELog.e(TAG, "No matching deviceConfig found for device_name=" + str2 + ", hwc_name=" + str3);
                return false;
            }
            return parseDeviceConfig(elementFindMatchingDeviceConfig, mEGestureModeConfig);
        } catch (FileNotFoundException e) {
            MELog.e(TAG, "Configuration file not found: " + str);
            e.printStackTrace();
            return false;
        } catch (Exception unused) {
            MELog.e(TAG, "Error parsing configuration file: " + str);
            return false;
        }
    }

    /* JADX WARN: Code duplicated, block: B:13:0x0045  */
    private static Element findMatchingDeviceConfig(Document document, String str, String str2) {
        String attribute;
        NodeList elementsByTagName = document.getElementsByTagName("deviceConfig");
        Element element = null;
        for (int i = 0; i < elementsByTagName.getLength(); i++) {
            Node nodeItem = elementsByTagName.item(i);
            if (nodeItem.getNodeType() == 1) {
                Element element2 = (Element) nodeItem;
                String attribute2 = element2.getAttribute("device_name");
                NodeList elementsByTagName2 = element2.getElementsByTagName("hwc");
                if (elementsByTagName2.getLength() > 0) {
                    Node nodeItem2 = elementsByTagName2.item(0);
                    if (nodeItem2.getNodeType() == 1) {
                        attribute = ((Element) nodeItem2).getAttribute("hwc_name");
                    } else {
                        attribute = "";
                    }
                } else {
                    attribute = "";
                }
                if (str.equals(attribute2) && str2.equals(attribute)) {
                    MELog.d(TAG, "Exact match found: device_name=" + attribute2 + ", hwc_name=" + attribute);
                    return element2;
                }
                if ("default".equals(attribute)) {
                    element = element2;
                }
            }
        }
        if (element != null) {
            MELog.i(TAG, "Falling back to default configuration");
            return element;
        }
        MELog.e(TAG, "No matching configuration found, including default");
        return null;
    }

    private static boolean parseDeviceConfig(Element element, MEGestureModeConfig mEGestureModeConfig) {
        try {
            NodeList elementsByTagName = element.getElementsByTagName("GestureModeMapConfigBitMask");
            if (elementsByTagName.getLength() == 0) {
                MELog.e(TAG, "Missing GestureModeMapConfigBitMask element");
                return false;
            }
            Node nodeItem = elementsByTagName.item(0);
            if (nodeItem.getNodeType() == 1) {
                int iSafeParseInt = safeParseInt(((Element) nodeItem).getAttribute("bitMask"));
                mEGestureModeConfig.bitMask = iSafeParseInt;
                mEGestureModeConfig.bitMaskBinary = Integer.toBinaryString(iSafeParseInt);
                NodeList elementsByTagName2 = element.getElementsByTagName("NWCoexistMapItem");
                HashSet hashSet = new HashSet();
                for (int i = 0; i < elementsByTagName2.getLength(); i++) {
                    Node nodeItem2 = elementsByTagName2.item(i);
                    if (nodeItem2.getNodeType() == 1) {
                        NamedNodeMap attributes = nodeItem2.getAttributes();
                        for (int i2 = 0; i2 < attributes.getLength(); i2++) {
                            String nodeName = attributes.item(i2).getNodeName();
                            if (!"outMode".equals(nodeName)) {
                                hashSet.add(nodeName);
                            }
                        }
                    }
                }
                int size = hashSet.size();
                while (mEGestureModeConfig.bitMaskBinary.length() < size) {
                    mEGestureModeConfig.bitMaskBinary = "0" + mEGestureModeConfig.bitMaskBinary;
                }
            }
            NodeList elementsByTagName3 = element.getElementsByTagName("NWCoexistMapItem");
            mEGestureModeConfig.configItems.clear();
            for (int i3 = 0; i3 < elementsByTagName3.getLength(); i3++) {
                Node nodeItem3 = elementsByTagName3.item(i3);
                if (nodeItem3.getNodeType() == 1) {
                    HashMap map = new HashMap();
                    NamedNodeMap attributes2 = ((Element) nodeItem3).getAttributes();
                    HashMap map2 = new HashMap();
                    for (int i4 = 0; i4 < attributes2.getLength(); i4++) {
                        Node nodeItem4 = attributes2.item(i4);
                        String nodeName2 = nodeItem4.getNodeName();
                        int iSafeParseInt2 = safeParseInt(nodeItem4.getNodeValue());
                        if ("outMode".equals(nodeName2)) {
                            map.put("mode", Integer.valueOf(iSafeParseInt2));
                        } else {
                            map2.put(nodeName2, Integer.valueOf(iSafeParseInt2));
                        }
                    }
                    map.put("conditions", map2);
                    mEGestureModeConfig.configItems.add(map);
                }
            }
            NodeList elementsByTagName4 = element.getElementsByTagName("NWCoexistControlMapItem");
            mEGestureModeConfig.controlMap.clear();
            for (int i5 = 0; i5 < elementsByTagName4.getLength(); i5++) {
                Node nodeItem5 = elementsByTagName4.item(i5);
                if (nodeItem5.getNodeType() == 1) {
                    Element element2 = (Element) nodeItem5;
                    HashMap map3 = new HashMap();
                    map3.put("mode", Integer.valueOf(safeParseInt(element2.getAttribute("mode"))));
                    ArrayList arrayList = new ArrayList();
                    NamedNodeMap attributes3 = element2.getAttributes();
                    for (int i6 = 0; i6 < attributes3.getLength(); i6++) {
                        Node nodeItem6 = attributes3.item(i6);
                        if (nodeItem6.getNodeName().startsWith("outGPIO")) {
                            arrayList.add(Integer.valueOf(safeParseInt(nodeItem6.getNodeValue())));
                        }
                    }
                    map3.put("gpios", arrayList);
                    mEGestureModeConfig.controlMap.add(map3);
                }
            }
            NodeList elementsByTagName5 = element.getElementsByTagName("ControlTxForceMapItem");
            mEGestureModeConfig.controlTxForceMap.clear();
            for (int i7 = 0; i7 < elementsByTagName5.getLength(); i7++) {
                Node nodeItem7 = elementsByTagName5.item(i7);
                if (nodeItem7.getNodeType() == 1) {
                    Element element3 = (Element) nodeItem7;
                    ArrayList arrayList2 = new ArrayList();
                    arrayList2.add(Integer.valueOf(safeParseInt(element3.getAttribute("Rat"))));
                    arrayList2.add(Integer.valueOf(safeParseInt(element3.getAttribute("Band"))));
                    arrayList2.add(Integer.valueOf(safeParseInt(element3.getAttribute("Tas_State"))));
                    mEGestureModeConfig.controlTxForceMap.add(arrayList2);
                }
            }
            mEGestureModeConfig.isConfigLoaded = true;
            MELog.i(TAG, "Successfully loaded deviceConfig with " + mEGestureModeConfig.configItems.size() + " mapping rules");
            return true;
        } catch (Exception unused) {
            MELog.e(TAG, "Error parsing deviceConfig");
            return false;
        }
    }

    public Integer getOutMode(Map map) {
        if (!this.isConfigLoaded) {
            MELog.e(TAG, "Configuration not loaded, cannot get outMode");
            return null;
        }
        for (Map map2 : this.configItems) {
            Map map3 = (Map) map2.get("conditions");
            ArrayList arrayList = new ArrayList(map3.keySet());
            int size = arrayList.size();
            int i = 0;
            while (true) {
                if (i < size) {
                    Object obj = arrayList.get(i);
                    i++;
                    String str = (String) obj;
                    int iIntValue = ((Integer) map.getOrDefault(str, -1)).intValue();
                    int iIntValue2 = ((Integer) map3.get(str)).intValue();
                    int size2 = (arrayList.size() - arrayList.indexOf(str)) - 1;
                    if (this.bitMaskBinary.length() > size2) {
                        String str2 = this.bitMaskBinary;
                        if (str2.charAt((str2.length() - 1) - size2) != '0' && iIntValue2 != 255 && iIntValue2 != iIntValue) {
                            break;
                        }
                    }
                } else {
                    Integer num = (Integer) map2.get("mode");
                    MELog.d(TAG, "Match found: " + map + " -> outMode=" + num);
                    return num;
                }
            }
        }
        MELog.d(TAG, "No matching outMode found: " + map);
        return null;
    }

    public List getGsmTxForceSettings() {
        if (this.controlTxForceMap == null) {
            MELog.d(TAG, "controlTxForceMap is NULL !!");
        }
        return this.controlTxForceMap;
    }

    public String getGpioSettings(int i) {
        if (!this.isConfigLoaded) {
            MELog.e(TAG, "Configuration not loaded, cannot get GPIO settings");
            return null;
        }
        for (Map map : this.controlMap) {
            if (((Integer) map.get("mode")).intValue() == i) {
                List list = (List) map.get("gpios");
                StringBuilder sb = new StringBuilder();
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    int iIntValue = ((Integer) it.next()).intValue();
                    if (iIntValue != 255) {
                        sb.append(iIntValue);
                    }
                }
                String string = sb.toString();
                MELog.d(TAG, "outMode=" + i + " -> GPIO=" + string);
                return string;
            }
        }
        MELog.d(TAG, "No GPIO settings found for mode: " + i);
        return null;
    }

    private static int safeParseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            MELog.e(TAG, "Invalid integer value: " + str);
            e.printStackTrace();
            return -1;
        }
    }

    public boolean isConfigLoaded() {
        return this.isConfigLoaded;
    }
}
