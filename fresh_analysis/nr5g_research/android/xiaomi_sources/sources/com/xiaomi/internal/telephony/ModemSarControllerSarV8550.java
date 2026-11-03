package com.xiaomi.internal.telephony;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import com.android.internal.telephony.CommandsInterface;
import com.xiaomi.modem.ModemUtils;
import java.io.FileInputStream;
import java.io.InputStream;
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
    private static String[] mAntennaGroup;
    private static ModemSarControllerSarV8550 sIntance;
    private static String mListeners = "";
    private static SarDeviceConfig mSarDeviceConfig = null;
    private static int mAntennaNums = 15;
    private static String mDefaultGroup = "";
    private static boolean mSensorStateAbnormal = false;
    private static final int[] mAllSensorsState = new int[20];
    private static final int[] sensorSerialNumber = {3, 9, 15};
    private static final int[] sensorHotspotNumber = {1, 7};
    private static final ArrayList<Integer> mSarSensorArr = new ArrayList<>();
    private static final ArrayList<Integer> mHotspotSensorArr = new ArrayList<>();
    private static CmdProcThread mCmdProc = null;
    private static DynamicSarServiceSarV8550 mService = null;
    private static CommandsInterface[] mCommandInterface = null;
    private static String DSI_Current = "";

    public static void make(Context context, CommandsInterface[] cis) {
        mCommandInterface = cis;
        if (sIntance == null) {
            sIntance = new ModemSarControllerSarV8550(context);
        }
    }

    private ModemSarControllerSarV8550(Context context) {
        log("ModemController running up...");
        if (!isSarEnabled()) {
            return;
        }
        mCmdProc = new CmdProcThread();
        mCmdProc.start();
        mService = DynamicSarServiceSarV8550.getInstance(context);
        mSarDeviceConfig = findSarDeviceConfig();
        if (mService != null && mSarDeviceConfig != null) {
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
        int sarPropSetting = SystemProperties.getInt("persist.vendor.radio.dynamic_sar", 0);
        if (sarPropSetting == 1) {
            return true;
        }
        log("PropSetting = " + sarPropSetting);
        return false;
    }

    private SarDeviceConfig findSarDeviceConfig() {
        String mDeviceName = SystemProperties.get("ro.product.device");
        String mGlobalHW = SystemProperties.get("ro.boot.hwc");
        String mGlobalSW = SystemProperties.get("ro.product.mod_device");
        if (mGlobalSW.contains(ModemUtils.PROP_DEVICE_MOD_NAME_SUFFIX_PRE)) {
            mGlobalSW = mGlobalSW.replace(ModemUtils.PROP_DEVICE_MOD_NAME_SUFFIX_PRE, "");
        }
        log("globalHW = " + mGlobalHW + ", globalSW = " + mGlobalSW);
        SarDeviceConfig matchedDeviceConfig = readXmlBySAX(mDeviceName, mGlobalHW, mGlobalSW);
        if (matchedDeviceConfig != null) {
            mListeners = matchedDeviceConfig.listeners;
            mAntennaNums = Integer.parseInt(matchedDeviceConfig.antennaNum);
            String group = matchedDeviceConfig.antennaGroup;
            mAntennaGroup = group.split(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE);
            mDefaultGroup = matchedDeviceConfig.defaultGroup;
            log("mAntennaNums = " + mAntennaNums + ", group = " + group + ", mDefaultGroup = " + mDefaultGroup + ", mListeners = " + mListeners);
            addEle(mHotspotSensorArr, sensorHotspotNumber);
            addEle(mSarSensorArr, sensorSerialNumber);
            Log.d(LOG_TAG, "h len is = " + mHotspotSensorArr.size());
            Log.d(LOG_TAG, "n len is = " + mSarSensorArr.size());
        }
        return matchedDeviceConfig;
    }

    private void initRegExceptionAction() {
        if (mService != null) {
            mService.registerStateChangeListener(13, this);
            mService.registerStateChangeListener(14, this);
        }
    }

    @Override // com.xiaomi.internal.telephony.DynamicSarServiceSarV8550.SarControllerClient
    public void onStateChanged(int type, int value) {
        log("onStateChanged: type = " + type + ", value = " + value);
        if (type <= mAllSensorsState.length) {
            mAllSensorsState[type] = value;
            sendMsgToHandler(type, value, 0);
        } else {
            sendMsgToHandler(20, value, 0);
        }
    }

    private static void sendMsgToHandler(int message, int arg1, int arg2) {
        Handler handler = mCmdProc.getCmdHandler();
        if (handler != null) {
            Message msg = handler.obtainMessage(message, arg1, arg2);
            handler.sendMessage(msg);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String s) {
        Log.d(LOG_TAG, s);
    }

    private static class CmdProcThread extends Thread {
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
        public static void sendSarToModem(String DSI_str) {
            ModemSarControllerSarV8550.log("Send DSI command to modem, cmd = " + DSI_str);
            if (ModemSarControllerSarV8550.mCommandInterface != null) {
                for (int i = 0; i < ModemSarControllerSarV8550.mCommandInterface.length; i++) {
                    if (1 == ModemSarControllerSarV8550.mCommandInterface[i].getRadioState()) {
                        ModemSarControllerSarV8550.mCommandInterface[i].invokeOemRilRequestStrings(new String[]{DSI_str, ""}, (Message) null);
                        return;
                    }
                    ModemSarControllerSarV8550.log("phone[" + i + "] radio is unavailable or off");
                }
                return;
            }
            Log.e(ModemSarControllerSarV8550.LOG_TAG, "mCommandInterface[0] is NULL!!!");
        }

        private static class CmdHandler extends Handler {
            private CmdHandler() {
            }

            private void DSI_Handle(int type) {
                if (ModemSarControllerSarV8550.mAllSensorsState[8] == 0) {
                    ModemSarControllerSarV8550.mAllSensorsState[7] = 0;
                }
                ModemSarControllerSarV8550.log("hallState is = " + ModemSarControllerSarV8550.mAllSensorsState[4] + ", WIFIState is = " + ModemSarControllerSarV8550.mAllSensorsState[0] + ", MCC state is = " + ModemSarControllerSarV8550.mAllSensorsState[14] + ", Bluetooth pan state is = " + ModemSarControllerSarV8550.mAllSensorsState[7] + ", ReceiverState is = " + ModemSarControllerSarV8550.mAllSensorsState[5]);
                ModemSarControllerSarV8550.log("HotspotState is = " + ModemSarControllerSarV8550.mAllSensorsState[1] + ", SarSensorState is = " + ModemSarControllerSarV8550.mAllSensorsState[3] + ", SarSensor2State is = " + ModemSarControllerSarV8550.mAllSensorsState[9] + ", SarSensor3State is = " + ModemSarControllerSarV8550.mAllSensorsState[15] + ", Cablestate is = " + ModemSarControllerSarV8550.mAllSensorsState[10]);
                int currentDsi = ModemSarControllerSarV8550.getDsiVaule();
                String[] DSIStringPerAntGroup = ModemSarControllerSarV8550.getAntGroupArray(currentDsi, ModemSarControllerSarV8550.mAntennaNums, ModemSarControllerSarV8550.mAntennaGroup, ModemSarControllerSarV8550.mDefaultGroup);
                StringBuilder dsi_cmd_sb = new StringBuilder("AT+EGMC=1,\"sar_scenario_all\"");
                for (String s : DSIStringPerAntGroup) {
                    dsi_cmd_sb.append(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE);
                    if (ModemSarControllerSarV8550.mSensorStateAbnormal) {
                        dsi_cmd_sb.append(4);
                    } else {
                        dsi_cmd_sb.append(s);
                    }
                }
                String dsi_str = dsi_cmd_sb.toString();
                ModemSarControllerSarV8550.log("dsi_str = " + dsi_str);
                if (ModemSarControllerSarV8550.mSensorStateAbnormal) {
                    ModemSarControllerSarV8550.mSensorStateAbnormal = false;
                    CmdProcThread.sendSarToModem(dsi_str);
                    ModemSarControllerSarV8550.log("abnormal");
                } else if (!ModemSarControllerSarV8550.DSI_Current.equals(dsi_str)) {
                    CmdProcThread.sendSarToModem(dsi_str);
                } else if (type == 13 || type == 14) {
                    ModemSarControllerSarV8550.log("radio power on or SIM card loaded, send the current DSI");
                    CmdProcThread.sendSarToModem(dsi_str);
                } else {
                    ModemSarControllerSarV8550.log("The current DSI have set, don`t need to set it again.");
                }
                ModemSarControllerSarV8550.DSI_Current = dsi_str;
            }

            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                if (msg.what == 20) {
                    ModemSarControllerSarV8550.mSensorStateAbnormal = true;
                    DSI_Handle(msg.what);
                } else {
                    ModemSarControllerSarV8550.mAllSensorsState[msg.what] = msg.arg1;
                    DSI_Handle(msg.what);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getDsiVaule() {
        StringBuilder mStateStr = new StringBuilder();
        if (mSarDeviceConfig == null) {
            Log.d(LOG_TAG, "enable == false");
            return 0;
        }
        boolean f1 = true;
        boolean f2 = true;
        int j = 0;
        for (char i : mListeners.toCharArray()) {
            if (i == '1') {
                if (mHotspotSensorArr.contains(Integer.valueOf(j)) && f1) {
                    mStateStr.append(getMaxSensorState(mHotspotSensorArr));
                    f1 = false;
                } else if (mSarSensorArr.contains(Integer.valueOf(j)) && f2) {
                    mStateStr.append(getMaxSensorState(mSarSensorArr));
                    f2 = false;
                } else if (!mHotspotSensorArr.contains(Integer.valueOf(j)) && !mSarSensorArr.contains(Integer.valueOf(j)) && j != 8) {
                    if (mAllSensorsState[j] > 1) {
                        Log.d(LOG_TAG, "sensor " + j + " is abnormal, and the abnormal value is " + mAllSensorsState[j] + "use default value '1'");
                        mAllSensorsState[j] = 1;
                    }
                    mStateStr.append(mAllSensorsState[j]);
                }
            }
            j++;
        }
        int newDsiValue = findDsiFromXML(mStateStr.toString().toCharArray());
        log("getDsiVaule, getNewDsiVaule = " + newDsiValue);
        return newDsiValue;
    }

    public static String getMaxSensorState(List<Integer> n) {
        int ret = 0;
        Iterator<Integer> it = n.iterator();
        while (it.hasNext()) {
            int i = it.next().intValue();
            ret = Math.max(ret, mAllSensorsState[i]);
        }
        if (ret > 1) {
            ret = 1;
            Log.d(LOG_TAG, "sensor is abnormal and set sensor a safe value = 1(short distance)");
        }
        return Integer.toString(ret);
    }

    public static void addEle(ArrayList<Integer> a, int[] b) {
        for (int i : b) {
            a.add(Integer.valueOf(i));
        }
    }

    private static int findDsiFromXML(char[] sourStateList) {
        String sourceStateStr = new String(sourStateList);
        if (sourStateList.length < 1) {
            Log.d(LOG_TAG, "sourStateList is null. please check the wifi, bluetooth... states");
            return 3;
        }
        Map<String, String> mDsiMap = mSarDeviceConfig.dsiList;
        for (Map.Entry<String, String> entry : mDsiMap.entrySet()) {
            log("DesStateSet = " + entry.getKey() + ", dsi = " + entry.getValue() + ", matching against sourStateList = " + sourceStateStr);
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
                int i2 = sourStateList.length;
                if (len == i2 - 1 && flag) {
                    int matchedDsi = Integer.parseInt(entry.getValue());
                    log("findDsiFromXML success: result = " + matchedDsi);
                    return matchedDsi;
                }
            } else {
                log("sourStateSet size is not same as desStateList,please check again. len = " + sourStateList.length);
                return 3;
            }
        }
        return 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String[] getAntGroupArray(int currentDsi, int antNums, String[] antGroup, String defaultGroup) {
        String[] antGroupArray = new String[antNums];
        Arrays.fill(antGroupArray, "3");
        char[] defualtGroupArr = defaultGroup.toCharArray();
        int i = 0;
        if (mAllSensorsState[5] >= 1 || mAllSensorsState[1] >= 1 || mAllSensorsState[10] >= 1) {
            for (char c : defualtGroupArr) {
                antGroupArray[c - '0'] = Integer.toString(currentDsi);
            }
            int i2 = antGroup.length;
            while (i < i2) {
                String antGrpItem = antGroup[i];
                for (char c2 : antGrpItem.toCharArray()) {
                    antGroupArray[c2 - '0'] = Integer.toString(currentDsi);
                }
                i++;
            }
        } else if (mAllSensorsState[3] >= 1 || mAllSensorsState[9] >= 1 || mAllSensorsState[15] >= 1) {
            if (mAllSensorsState[3] >= 1 && antGroup.length > 0) {
                for (char c3 : antGroup[0].toCharArray()) {
                    antGroupArray[c3 - '0'] = Integer.toString(currentDsi);
                }
            }
            if (mAllSensorsState[9] >= 1 && antGroup.length > 1) {
                for (char c4 : antGroup[1].toCharArray()) {
                    antGroupArray[c4 - '0'] = Integer.toString(currentDsi);
                }
            }
            if (mAllSensorsState[15] >= 1 && antGroup.length > 2) {
                char[] sarSensorArr = antGroup[2].toCharArray();
                for (char c5 : sarSensorArr) {
                    antGroupArray[c5 - '0'] = Integer.toString(currentDsi);
                }
            }
        } else {
            for (char c6 : defualtGroupArr) {
                antGroupArray[c6 - '0'] = Integer.toString(currentDsi);
            }
            int i3 = antGroup.length;
            while (i < i3) {
                String antGrpItem2 = antGroup[i];
                char[] antGrpItemArr = antGrpItem2.toCharArray();
                for (char c7 : antGrpItemArr) {
                    antGroupArray[c7 - '0'] = Integer.toString(currentDsi);
                }
                i++;
            }
        }
        return antGroupArray;
    }

    private static SarDeviceConfig readXmlBySAX(String deviceName, String hwName, String swName) {
        List<SarDeviceConfig> sarConfigList = null;
        try {
            InputStream inputStream = new FileInputStream("/vendor/etc/device_sar_config.xml");
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser saxParser = spf.newSAXParser();
            XMLHandler handler = new XMLHandler();
            saxParser.parse(inputStream, handler);
            inputStream.close();
            sarConfigList = handler.getXmlConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (sarConfigList != null) {
            for (int i = 0; i < sarConfigList.size(); i++) {
                SarDeviceConfig sarConfigItem = sarConfigList.get(i);
                String xmlDeviceName = (String) Optional.ofNullable(sarConfigItem.deviceName).orElse("null");
                String xmlHWName = (String) Optional.ofNullable(sarConfigItem.propList.get("ro.boot.hwc")).orElse("null");
                String xmlSWName = (String) Optional.ofNullable(sarConfigItem.propList.get("ro.product.mod_device")).orElse("null");
                log("Get from xml file:  deviceName=" + xmlDeviceName + " HWname=" + xmlHWName + " SWname=" + xmlSWName);
                if (xmlDeviceName.equalsIgnoreCase(deviceName) && xmlHWName.contains(hwName) && xmlSWName.contains(swName)) {
                    log("find the config success!!! : " + xmlHWName);
                    return sarConfigItem;
                }
                if (xmlDeviceName.equalsIgnoreCase(deviceName) && xmlHWName.contains(ModemUtils.PROP_NTN_LOG_LEVEL_DEFAULT) && xmlSWName.contains(ModemUtils.PROP_NTN_LOG_LEVEL_DEFAULT)) {
                    log("use default config!!!");
                    return sarConfigItem;
                }
            }
            return null;
        }
        log("Parse xml failed, don`t have the deviceSarConfigList!");
        return null;
    }

    public static class SarDeviceConfig {
        HashMap<String, String> dsiList;
        HashMap<String, String> propList;
        String deviceName = "";
        String listeners = "";
        String antennaNum = "";
        String antennaGroup = "";
        String defaultGroup = "";

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public void setListeners(String listeners) {
            this.listeners = listeners;
        }

        public void setPropList(String key, String value) {
            if (this.propList == null) {
                this.propList = new HashMap<>();
            }
            this.propList.put(key, value);
        }

        public void setDsiList(String key, String value) {
            if (this.dsiList == null) {
                this.dsiList = new HashMap<>();
            }
            this.dsiList.put(key, value);
        }

        public void setAntennaNum(String value) {
            this.antennaNum = value;
        }

        public void setAntennaGroup(String value) {
            this.antennaGroup = value;
        }

        public void setDefaultGroup(String value) {
            this.defaultGroup = value;
        }
    }

    public static class XMLHandler extends DefaultHandler {
        private SarDeviceConfig mCurrentConfig;
        private List<SarDeviceConfig> mParcel = null;
        private String tagName = null;
        private String tagAttrValue = null;

        public List<SarDeviceConfig> getXmlConfig() {
            if (this.mParcel == null) {
                Log.d(ModemSarControllerSarV8550.LOG_TAG, "mParcel == null");
            }
            return this.mParcel;
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startDocument() throws SAXException {
            this.mParcel = new ArrayList();
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Code duplicated, block: B:14:0x0026  */
        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            byte b;
            switch (localName.hashCode()) {
                case -1322611358:
                    if (!localName.equals("dsimap")) {
                        b = -1;
                    } else {
                        b = 2;
                    }
                    break;
                case -1181781360:
                    if (!localName.equals("deviceSarConfig")) {
                        b = -1;
                    } else {
                        b = 0;
                    }
                    break;
                case 3449699:
                    if (!localName.equals("prop")) {
                        b = -1;
                    } else {
                        b = 1;
                    }
                    break;
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    this.mCurrentConfig = new SarDeviceConfig();
                    if (attributes.getValue("device_name") != null) {
                        this.mCurrentConfig.setDeviceName(attributes.getValue("device_name"));
                    }
                    break;
                case 1:
                    this.tagAttrValue = attributes.getValue("prop_name");
                    break;
                case 2:
                    this.tagAttrValue = attributes.getValue("value");
                    break;
            }
            this.tagName = localName;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Code duplicated, block: B:25:0x004f  */
        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void characters(char[] ch, int start, int length) throws SAXException {
            byte b;
            if (this.tagName != null) {
                String data = new String(ch, start, length);
                String str = this.tagName;
                switch (str.hashCode()) {
                    case -1322611358:
                        if (!str.equals("dsimap")) {
                            b = -1;
                        } else {
                            b = 2;
                        }
                        break;
                    case -1218719169:
                        if (!str.equals("listeners")) {
                            b = -1;
                        } else {
                            b = 0;
                        }
                        break;
                    case -642916130:
                        if (!str.equals("defaultgroup")) {
                            b = -1;
                        } else {
                            b = 5;
                        }
                        break;
                    case 3449699:
                        if (!str.equals("prop")) {
                            b = -1;
                        } else {
                            b = 1;
                        }
                        break;
                    case 1535239941:
                        if (!str.equals("sensorgroup")) {
                            b = -1;
                        } else {
                            b = 4;
                        }
                        break;
                    case 1712303207:
                        if (!str.equals("sensornums")) {
                            b = -1;
                        } else {
                            b = 3;
                        }
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        this.mCurrentConfig.setListeners(data);
                        break;
                    case 1:
                        this.mCurrentConfig.setPropList(this.tagAttrValue, data);
                        break;
                    case 2:
                        this.mCurrentConfig.setDsiList(this.tagAttrValue, data);
                        break;
                    case 3:
                        this.mCurrentConfig.setAntennaNum(data);
                        break;
                    case 4:
                        this.mCurrentConfig.setAntennaGroup(data);
                        break;
                    case 5:
                        this.mCurrentConfig.setDefaultGroup(data);
                        break;
                }
            }
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (localName.equals("deviceSarConfig")) {
                this.mParcel.add(this.mCurrentConfig);
                this.mCurrentConfig = null;
            }
            this.tagName = null;
            this.tagAttrValue = null;
        }
    }
}
