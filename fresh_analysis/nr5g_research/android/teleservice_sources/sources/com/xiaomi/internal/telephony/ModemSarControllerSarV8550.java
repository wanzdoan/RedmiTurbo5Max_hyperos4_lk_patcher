package com.xiaomi.internal.telephony;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import com.android.internal.telephony.CommandsInterface;
import com.ot.pubsub.a.a;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/* JADX INFO: loaded from: classes.dex */
public class ModemSarControllerSarV8550 implements DynamicSarServiceSarV8550.SarControllerClient {
    public static final int DEFAULT_DSI_VALUE = 4;
    public static final int EVENT_ABNORMAL = 20;
    public static final String LOG_TAG = "ModemControllerSarV8550";
    private static final String PROPERTY_DEVICE_NAME = "ro.product.device";
    private static final String PROPERTY_DYNAMIC_SAR = "persist.vendor.radio.dynamic_sar";
    private static final String PROPERTY_GLOBAL_HW = "ro.boot.hwc";
    private static final String PROPERTY_GLOBAL_VER = "ro.product.mod_device";
    public static final int TYPE_AUDIO_RECEIVER_STATE = 5;
    public static final int TYPE_BLUETOOTH_PAN_STATE = 7;
    public static final int TYPE_BLUETOOTH_STATE = 8;
    public static final int TYPE_CABLE_STATE = 10;
    public static final int TYPE_FOR_INIT_STATE = 19;
    public static final int TYPE_HALL_SENSOR_STATE = 4;
    public static final int TYPE_HOTSPOT_STATE = 1;
    public static final int TYPE_MCC_STATE = 6;
    public static final int TYPE_MODEM_STATE = 2;
    public static final int TYPE_RADIOPOWER_STATE = 13;
    public static final int TYPE_SAR_SENSOR2_STATE = 9;
    public static final int TYPE_SAR_SENSOR3_STATE = 15;
    public static final int TYPE_SAR_SENSOR_STATE = 3;
    public static final int TYPE_SIMCARD_STATE = 14;
    public static final int TYPE_USB_STATE = 11;
    public static final int TYPE_USE_CASE_TEST = 12;
    public static final int TYPE_WIFI_STATE = 0;
    private static String[] mAntennaGroup = null;
    private static int mAntennaNums = 15;
    private static String mDefaultGroup = "";
    private static String mListeners = "";
    private static SarDeviceConfig mSarDeviceConfig = null;
    private static boolean mSensorStateAbnormal = false;
    private static ModemSarControllerSarV8550 sIntance;
    private static final int[] mAllSensorsState = new int[20];
    private static final int[] sensorSerialNumber = {3, 9, 15};
    private static final int[] sensorHotspotNumber = {1, 7};
    private static final ArrayList mSarSensorArr = new ArrayList();
    private static final ArrayList mHotspotSensorArr = new ArrayList();
    private static CmdProcThread mCmdProc = null;
    private static DynamicSarServiceSarV8550 mService = null;
    private static CommandsInterface[] mCommandInterface = null;
    private static String DSI_Current = "";

    public static void make(Context context, CommandsInterface[] commandsInterfaceArr) {
        mCommandInterface = commandsInterfaceArr;
        if (sIntance == null) {
            sIntance = new ModemSarControllerSarV8550(context);
        }
    }

    private ModemSarControllerSarV8550(Context context) {
        log("ModemController running up...");
        if (isSarEnabled()) {
            CmdProcThread cmdProcThread = new CmdProcThread();
            mCmdProc = cmdProcThread;
            cmdProcThread.start();
            mService = DynamicSarServiceSarV8550.getInstance(context);
            SarDeviceConfig sarDeviceConfigFindSarDeviceConfig = findSarDeviceConfig();
            mSarDeviceConfig = sarDeviceConfigFindSarDeviceConfig;
            if (mService == null || sarDeviceConfigFindSarDeviceConfig == null) {
                return;
            }
            Arrays.fill(mAllSensorsState, 0);
            for (int i = 0; i < mListeners.length(); i++) {
                if (mListeners.charAt(i) == '1') {
                    mService.registerStateChangeListener(i, this);
                }
            }
            initRegExceptionAction();
            sendMsgToHandler(19, 1, 0);
        }
    }

    private boolean isSarEnabled() {
        int i = SystemProperties.getInt("persist.vendor.radio.dynamic_sar", 0);
        if (i == 1) {
            return true;
        }
        log("PropSetting = " + i);
        return false;
    }

    private SarDeviceConfig findSarDeviceConfig() {
        String str = SystemProperties.get("ro.product.device");
        String str2 = SystemProperties.get("ro.boot.hwc");
        String strReplace = SystemProperties.get("ro.product.mod_device");
        if (strReplace.contains("_pre")) {
            strReplace = strReplace.replace("_pre", "");
        }
        log("globalHW = " + str2 + ", globalSW = " + strReplace);
        SarDeviceConfig xmlBySAX = readXmlBySAX(str, str2, strReplace);
        if (xmlBySAX != null) {
            mListeners = xmlBySAX.listeners;
            mAntennaNums = Integer.parseInt(xmlBySAX.antennaNum);
            String str3 = xmlBySAX.antennaGroup;
            mAntennaGroup = str3.split(",");
            mDefaultGroup = xmlBySAX.defaultGroup;
            log("mAntennaNums = " + mAntennaNums + ", group = " + str3 + ", mDefaultGroup = " + mDefaultGroup + ", mListeners = " + mListeners);
            ArrayList arrayList = mHotspotSensorArr;
            addEle(arrayList, sensorHotspotNumber);
            ArrayList arrayList2 = mSarSensorArr;
            addEle(arrayList2, sensorSerialNumber);
            StringBuilder sb = new StringBuilder("h len is = ");
            sb.append(arrayList.size());
            Log.d(LOG_TAG, sb.toString());
            Log.d(LOG_TAG, "n len is = " + arrayList2.size());
        }
        return xmlBySAX;
    }

    private void initRegExceptionAction() {
        DynamicSarServiceSarV8550 dynamicSarServiceSarV8550 = mService;
        if (dynamicSarServiceSarV8550 != null) {
            dynamicSarServiceSarV8550.registerStateChangeListener(13, this);
            mService.registerStateChangeListener(14, this);
        }
    }

    @Override // com.xiaomi.internal.telephony.DynamicSarServiceSarV8550.SarControllerClient
    public void onStateChanged(int i, int i2) {
        log("onStateChanged: type = " + i + ", value = " + i2);
        int[] iArr = mAllSensorsState;
        if (i <= iArr.length) {
            iArr[i] = i2;
            sendMsgToHandler(i, i2, 0);
        } else {
            sendMsgToHandler(20, i2, 0);
        }
    }

    private static void sendMsgToHandler(int i, int i2, int i3) {
        Handler cmdHandler = mCmdProc.getCmdHandler();
        if (cmdHandler != null) {
            cmdHandler.sendMessage(cmdHandler.obtainMessage(i, i2, i3));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d(LOG_TAG, str);
    }

    class CmdProcThread extends Thread {
        private Handler mHandler;

        private CmdProcThread() {
            this.mHandler = null;
        }

        public Handler getCmdHandler() {
            if (this.mHandler == null) {
                Log.e(ModemSarControllerSarV8550.LOG_TAG, "getCmdHandler, handler is NULL!!!");
            }
            return this.mHandler;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Looper.prepare();
            this.mHandler = new CmdHandler();
            ModemSarControllerSarV8550.log("CmdProcThread, thread is running up!!!!");
            Looper.loop();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void sendSarToModem(String str) {
            ModemSarControllerSarV8550.log("Send DSI command to modem, cmd = " + str);
            if (ModemSarControllerSarV8550.mCommandInterface != null) {
                for (int i = 0; i < ModemSarControllerSarV8550.mCommandInterface.length; i++) {
                    if (1 == ModemSarControllerSarV8550.mCommandInterface[i].getRadioState()) {
                        ModemSarControllerSarV8550.mCommandInterface[i].invokeOemRilRequestStrings(new String[]{str, ""}, (Message) null);
                        return;
                    }
                    ModemSarControllerSarV8550.log("phone[" + i + "] radio is unavailable or off");
                }
                return;
            }
            Log.e(ModemSarControllerSarV8550.LOG_TAG, "mCommandInterface[0] is NULL!!!");
        }

        class CmdHandler extends Handler {
            private CmdHandler() {
            }

            private void DSI_Handle(int i) {
                if (ModemSarControllerSarV8550.mAllSensorsState[8] == 0) {
                    ModemSarControllerSarV8550.mAllSensorsState[7] = 0;
                }
                ModemSarControllerSarV8550.log("hallState is = " + ModemSarControllerSarV8550.mAllSensorsState[4] + ", WIFIState is = " + ModemSarControllerSarV8550.mAllSensorsState[0] + ", MCC state is = " + ModemSarControllerSarV8550.mAllSensorsState[14] + ", Bluetooth pan state is = " + ModemSarControllerSarV8550.mAllSensorsState[7] + ", ReceiverState is = " + ModemSarControllerSarV8550.mAllSensorsState[5]);
                ModemSarControllerSarV8550.log("HotspotState is = " + ModemSarControllerSarV8550.mAllSensorsState[1] + ", SarSensorState is = " + ModemSarControllerSarV8550.mAllSensorsState[3] + ", SarSensor2State is = " + ModemSarControllerSarV8550.mAllSensorsState[9] + ", SarSensor3State is = " + ModemSarControllerSarV8550.mAllSensorsState[15] + ", Cablestate is = " + ModemSarControllerSarV8550.mAllSensorsState[10]);
                String[] antGroupArray = ModemSarControllerSarV8550.getAntGroupArray(ModemSarControllerSarV8550.getDsiVaule(), ModemSarControllerSarV8550.mAntennaNums, ModemSarControllerSarV8550.mAntennaGroup, ModemSarControllerSarV8550.mDefaultGroup);
                StringBuilder sb = new StringBuilder("AT+EGMC=1,\"sar_scenario_all\"");
                int length = antGroupArray.length;
                for (int i2 = 0; i2 < length; i2++) {
                    String str = antGroupArray[i2];
                    sb.append(",");
                    if (ModemSarControllerSarV8550.mSensorStateAbnormal) {
                        sb.append(4);
                    } else {
                        sb.append(str);
                    }
                }
                String string = sb.toString();
                ModemSarControllerSarV8550.log("dsi_str = ".concat(string));
                if (ModemSarControllerSarV8550.mSensorStateAbnormal) {
                    ModemSarControllerSarV8550.mSensorStateAbnormal = false;
                    CmdProcThread.sendSarToModem(string);
                    ModemSarControllerSarV8550.log("abnormal");
                } else if (!ModemSarControllerSarV8550.DSI_Current.equals(string)) {
                    CmdProcThread.sendSarToModem(string);
                } else if (i == 13 || i == 14) {
                    ModemSarControllerSarV8550.log("radio power on or SIM card loaded, send the current DSI");
                    CmdProcThread.sendSarToModem(string);
                } else {
                    ModemSarControllerSarV8550.log("The current DSI have set, don`t need to set it again.");
                }
                ModemSarControllerSarV8550.DSI_Current = string;
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what == 20) {
                    ModemSarControllerSarV8550.mSensorStateAbnormal = true;
                    DSI_Handle(message.what);
                } else {
                    int[] iArr = ModemSarControllerSarV8550.mAllSensorsState;
                    int i = message.what;
                    iArr[i] = message.arg1;
                    DSI_Handle(i);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getDsiVaule() {
        StringBuilder sb = new StringBuilder();
        if (mSarDeviceConfig == null) {
            Log.d(LOG_TAG, "enable == false");
            return 0;
        }
        int i = 0;
        boolean z = true;
        boolean z2 = true;
        for (char c : mListeners.toCharArray()) {
            if (c == '1') {
                ArrayList arrayList = mHotspotSensorArr;
                if (arrayList.contains(Integer.valueOf(i)) && z) {
                    sb.append(getMaxSensorState(arrayList));
                    z = false;
                } else {
                    ArrayList arrayList2 = mSarSensorArr;
                    if (arrayList2.contains(Integer.valueOf(i)) && z2) {
                        sb.append(getMaxSensorState(arrayList2));
                        z2 = false;
                    } else if (!arrayList.contains(Integer.valueOf(i)) && !arrayList2.contains(Integer.valueOf(i)) && i != 8) {
                        int[] iArr = mAllSensorsState;
                        if (iArr[i] > 1) {
                            Log.d(LOG_TAG, "sensor " + i + " is abnormal, and the abnormal value is " + iArr[i] + "use default value '1'");
                            iArr[i] = 1;
                        }
                        sb.append(iArr[i]);
                    }
                }
            }
            i++;
        }
        int iFindDsiFromXML = findDsiFromXML(sb.toString().toCharArray());
        log("getDsiVaule, getNewDsiVaule = " + iFindDsiFromXML);
        return iFindDsiFromXML;
    }

    public static String getMaxSensorState(List list) {
        Iterator it = list.iterator();
        int iMax = 0;
        while (it.hasNext()) {
            iMax = Math.max(iMax, mAllSensorsState[((Integer) it.next()).intValue()]);
        }
        if (iMax > 1) {
            Log.d(LOG_TAG, "sensor is abnormal and set sensor a safe value = 1(short distance)");
            iMax = 1;
        }
        return Integer.toString(iMax);
    }

    public static void addEle(ArrayList arrayList, int[] iArr) {
        for (int i : iArr) {
            arrayList.add(Integer.valueOf(i));
        }
    }

    private static int findDsiFromXML(char[] cArr) {
        String str = new String(cArr);
        if (cArr.length < 1) {
            Log.d(LOG_TAG, "sourStateList is null. please check the wifi, bluetooth... states");
            return 3;
        }
        for (Map.Entry entry : mSarDeviceConfig.dsiList.entrySet()) {
            log("DesStateSet = " + ((String) entry.getKey()) + ", dsi = " + ((String) entry.getValue()) + ", matching against sourStateList = " + str);
            char[] charArray = ((String) entry.getKey()).toCharArray();
            if (cArr.length != charArray.length) {
                log("sourStateSet size is not same as desStateList,please check again. len = " + cArr.length);
                break;
            }
            boolean z = false;
            int i = 0;
            boolean z2 = false;
            int i2 = 0;
            while (true) {
                if (i >= cArr.length) {
                    z = z2;
                    i = i2;
                    break;
                }
                char c = charArray[i];
                if ('F' != c && cArr[i] != c) {
                    break;
                }
                i2 = i;
                i++;
                z2 = true;
            }
            if (i == cArr.length - 1 && z) {
                int i3 = Integer.parseInt((String) entry.getValue());
                log("findDsiFromXML success: result = " + i3);
                return i3;
            }
        }
        return 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String[] getAntGroupArray(int i, int i2, String[] strArr, String str) {
        String[] strArr2 = new String[i2];
        Arrays.fill(strArr2, "3");
        char[] charArray = str.toCharArray();
        int[] iArr = mAllSensorsState;
        if (iArr[5] >= 1 || iArr[1] >= 1 || iArr[10] >= 1) {
            for (char c : charArray) {
                strArr2[c - '0'] = Integer.toString(i);
            }
            for (String str2 : strArr) {
                for (char c2 : str2.toCharArray()) {
                    strArr2[c2 - '0'] = Integer.toString(i);
                }
            }
        } else {
            int i3 = iArr[3];
            if (i3 >= 1 || iArr[9] >= 1 || iArr[15] >= 1) {
                if (i3 >= 1 && strArr.length > 0) {
                    for (char c3 : strArr[0].toCharArray()) {
                        strArr2[c3 - '0'] = Integer.toString(i);
                    }
                }
                if (mAllSensorsState[9] >= 1 && strArr.length > 1) {
                    for (char c4 : strArr[1].toCharArray()) {
                        strArr2[c4 - '0'] = Integer.toString(i);
                    }
                }
                if (mAllSensorsState[15] >= 1 && strArr.length > 2) {
                    for (char c5 : strArr[2].toCharArray()) {
                        strArr2[c5 - '0'] = Integer.toString(i);
                    }
                }
            } else {
                for (char c6 : charArray) {
                    strArr2[c6 - '0'] = Integer.toString(i);
                }
                for (String str3 : strArr) {
                    for (char c7 : str3.toCharArray()) {
                        strArr2[c7 - '0'] = Integer.toString(i);
                    }
                }
            }
        }
        return strArr2;
    }

    private static SarDeviceConfig readXmlBySAX(String str, String str2, String str3) {
        List xmlConfig;
        try {
            FileInputStream fileInputStream = new FileInputStream("/vendor/etc/device_sar_config.xml");
            SAXParser sAXParserNewSAXParser = SAXParserFactory.newInstance().newSAXParser();
            XMLHandler xMLHandler = new XMLHandler();
            sAXParserNewSAXParser.parse(fileInputStream, xMLHandler);
            fileInputStream.close();
            xmlConfig = xMLHandler.getXmlConfig();
        } catch (Exception e) {
            e.printStackTrace();
            xmlConfig = null;
        }
        if (xmlConfig != null) {
            for (int i = 0; i < xmlConfig.size(); i++) {
                SarDeviceConfig sarDeviceConfig = (SarDeviceConfig) xmlConfig.get(i);
                String str4 = (String) Optional.ofNullable(sarDeviceConfig.deviceName).orElse("null");
                String str5 = (String) Optional.ofNullable((String) sarDeviceConfig.propList.get("ro.boot.hwc")).orElse("null");
                String str6 = (String) Optional.ofNullable((String) sarDeviceConfig.propList.get("ro.product.mod_device")).orElse("null");
                log("Get from xml file:  deviceName=" + str4 + " HWname=" + str5 + " SWname=" + str6);
                if (str4.equalsIgnoreCase(str) && str5.contains(str2) && str6.contains(str3)) {
                    log("find the config success!!! : ".concat(str5));
                } else if (str4.equalsIgnoreCase(str) && str5.contains("default") && str6.contains("default")) {
                    log("use default config!!!");
                }
                return sarDeviceConfig;
            }
            return null;
        }
        log("Parse xml failed, don`t have the deviceSarConfigList!");
        return null;
    }

    public class SarDeviceConfig {
        HashMap dsiList;
        HashMap propList;
        String deviceName = "";
        String listeners = "";
        String antennaNum = "";
        String antennaGroup = "";
        String defaultGroup = "";

        public void setDeviceName(String str) {
            this.deviceName = str;
        }

        public void setListeners(String str) {
            this.listeners = str;
        }

        public void setPropList(String str, String str2) {
            if (this.propList == null) {
                this.propList = new HashMap();
            }
            this.propList.put(str, str2);
        }

        public void setDsiList(String str, String str2) {
            if (this.dsiList == null) {
                this.dsiList = new HashMap();
            }
            this.dsiList.put(str, str2);
        }

        public void setAntennaNum(String str) {
            this.antennaNum = str;
        }

        public void setAntennaGroup(String str) {
            this.antennaGroup = str;
        }

        public void setDefaultGroup(String str) {
            this.defaultGroup = str;
        }
    }

    public class XMLHandler extends DefaultHandler {
        private SarDeviceConfig mCurrentConfig;
        private List mParcel = null;
        private String tagName = null;
        private String tagAttrValue = null;

        public List getXmlConfig() {
            if (this.mParcel == null) {
                Log.d(ModemSarControllerSarV8550.LOG_TAG, "mParcel == null");
            }
            return this.mParcel;
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startDocument() throws SAXException {
            this.mParcel = new ArrayList();
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startElement(String str, String str2, String str3, Attributes attributes) throws SAXException {
            str2.hashCode();
            switch (str2) {
                case "dsimap":
                    this.tagAttrValue = attributes.getValue(a.p);
                    break;
                case "deviceSarConfig":
                    this.mCurrentConfig = new SarDeviceConfig();
                    if (attributes.getValue("device_name") != null) {
                        this.mCurrentConfig.setDeviceName(attributes.getValue("device_name"));
                        break;
                    }
                    break;
                case "prop":
                    this.tagAttrValue = attributes.getValue("prop_name");
                    break;
            }
            this.tagName = str2;
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void characters(char[] cArr, int i, int i2) throws SAXException {
            if (this.tagName != null) {
                String str = new String(cArr, i, i2);
                String str2 = this.tagName;
                str2.hashCode();
                switch (str2) {
                    case "dsimap":
                        this.mCurrentConfig.setDsiList(this.tagAttrValue, str);
                        break;
                    case "listeners":
                        this.mCurrentConfig.setListeners(str);
                        break;
                    case "defaultgroup":
                        this.mCurrentConfig.setDefaultGroup(str);
                        break;
                    case "prop":
                        this.mCurrentConfig.setPropList(this.tagAttrValue, str);
                        break;
                    case "sensorgroup":
                        this.mCurrentConfig.setAntennaGroup(str);
                        break;
                    case "sensornums":
                        this.mCurrentConfig.setAntennaNum(str);
                        break;
                }
            }
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void endElement(String str, String str2, String str3) throws SAXException {
            if (str2.equals("deviceSarConfig")) {
                this.mParcel.add(this.mCurrentConfig);
                this.mCurrentConfig = null;
            }
            this.tagName = null;
            this.tagAttrValue = null;
        }
    }
}
