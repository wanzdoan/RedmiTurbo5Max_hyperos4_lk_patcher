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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/* JADX INFO: loaded from: classes.dex */
public class ModemSarControllerTA implements DynamicSarService.SarControllerClient {
    private static final int BT_PAN_STATE_I = 3;
    private static final int BT_STATE_I = 2;
    private static final int CABLE_STATE_I = 8;
    private static final int EVENT_BT_PAN_CHANGE = 7;
    private static final int EVENT_CABLE_CHANGE = 9;
    private static final int EVENT_HALL_CHANGE = 5;
    private static final int EVENT_MCC_CHANGE = 6;
    private static final int EVENT_RADIO_POWER_CHANGE = 12;
    private static final int EVENT_RECEIVER_CHANGE = 2;
    private static final int EVENT_SAR_SENSOR2_CHANGE = 8;
    private static final int EVENT_SAR_SENSOR_CHANGE = 1;
    private static final int EVENT_SIMCARD_CHANGE = 13;
    private static final int EVENT_WIFI_CHANGE = 4;
    private static final int EVENT_WIFI_HOSP_CHANGE = 3;
    private static final int HALL_STATE_I = 9;
    private static final int I2C_ABNORMAL = 3;
    public static final String LOG_TAG = "ModemSarControllerTA";
    private static final int MCC_STATE_I = 6;
    private static final int OFFSET_CAL_ABNORMAL = 4;
    private static final String PROPERTY_DEVICE_NAME = "ro.product.device";
    private static final String PROPERTY_DYNAMIC_SAR = "persist.vendor.radio.dynamic_sar";
    private static final String PROPERTY_GLOBAL_HW = "ro.boot.hwc";
    private static final String PROPERTY_GLOBAL_VER = "ro.product.mod_device";
    private static final int RECEIVER_STATE_I = 0;
    private static final int SAR_DSI_SCENE_CABLE_ON = 5;
    private static final int SAR_DSI_SCENE_HOTSPOT_ON = 4;
    private static final int SAR_DSI_SCENE_RECEIVER_ON = 0;
    private static final int SAR_DSI_SCENE_SENSOR_OFF = 3;
    private static final int SAR_DSI_SCENE_SENSOR_ON = 1;
    private static final int SAR_MODE = 1;
    private static final int SAR_SCENE_Detect_OFF = 0;
    private static final int SAR_SCENE_Detect_ON = 1;
    private static final int SAR_SENSOR2_STATE_I = 5;
    private static final int SAR_SENSOR_STATE_I = 4;
    private static final int SENSOR_DEFAULT_STATE = 1;
    private static final int WIFI_HOSP_STATE_I = 1;
    private static final int WIFI_STATE_I = 7;
    private static ModemSarControllerTA sIntance;
    CmdProcThread mCmdProc;
    private boolean mSarTestProState = false;
    private DynamicSarService mService;
    private static int SarSensorState = 0;
    private static int ReceiverState = 0;
    private static int HotspotState = 0;
    private static int WIFIState = 0;
    private static int HallState = 0;
    private static int SarSensor2State = 0;
    private static int Cablestate = 0;
    private static int RadioPowerState = 0;
    private static int SIMCardState = 0;
    private static int MccState = 3;
    private static int BtPanState = 0;
    private static int HotspotBtPan = 0;
    private static int DSI_Current = 0;
    private static int DSI_History = 0;
    private static String mDeviceName = null;
    private static String mGlobalHW = null;
    private static String mGlobalSW = null;
    private static DeviceSarConfigLegacy mCurrentSarConfig = null;
    private static ArrayList<Integer> mUsedStateList = new ArrayList<>();
    private static CommandsInterface[] sCi = null;

    public static void make(Context context, CommandsInterface[] cis) {
        sCi = cis;
        if (sIntance == null) {
            sIntance = new ModemSarControllerTA(context);
        }
    }

    private ModemSarControllerTA(Context context) {
        this.mCmdProc = null;
        log("ModemSarControllerTA start in...");
        String deviceName = SystemProperties.get("ro.product.device");
        String globalHW = SystemProperties.get("ro.boot.hwc");
        String globalSW = SystemProperties.get("ro.product.mod_device");
        globalSW = globalSW.contains(ModemUtils.PROP_DEVICE_MOD_NAME_SUFFIX_PRE) ? globalSW.replace(ModemUtils.PROP_DEVICE_MOD_NAME_SUFFIX_PRE, "") : globalSW;
        log("Device name = " + deviceName + ", globalHW = " + globalHW + ", globalSW = " + globalSW);
        int mSarSensorProState = SystemProperties.getInt("persist.vendor.radio.dynamic_sar", 0);
        log("mSarSensorProState = " + mSarSensorProState);
        if (1 == SystemProperties.getInt("ro.boot.factorybuild", 0)) {
            log("This device is factory build!!!");
            mSarSensorProState = 0;
        }
        if (mSarSensorProState == 1) {
            mCurrentSarConfig = readXmlBySAX(ModemSarUtils.PATH_SAR_CONFIG, deviceName, globalHW, globalSW);
            if (mCurrentSarConfig != null) {
                this.mCmdProc = new CmdProcThread();
                this.mCmdProc.start();
                this.mService = DynamicSarService.getInstance(context);
                if (this.mService != null) {
                    initRegStateAction();
                    initRegExceptionAction();
                    return;
                } else {
                    log("DynamicSarService is null!");
                    return;
                }
            }
            log("Don`t find the corresponding config.");
            return;
        }
        log("Sar feature is disable.");
    }

    public void customeSarFunc() {
        boolean CNFlag = mGlobalHW.equals("CN") && !mGlobalSW.contains("global");
        log("Device name = " + mDeviceName + ", globalHW = " + mGlobalHW + ", globalSW = " + mGlobalSW + ", mSarSensorEnabled = true");
        if ((1 != 0 || this.mSarTestProState) && !CNFlag) {
            log("DSI_Hash_Init_GL_TA");
            this.mService.registerStateChangeListener(3, this);
            this.mService.registerStateChangeListener(5, this);
            this.mService.registerStateChangeListener(1, this);
            this.mService.registerStateChangeListener(0, this);
            this.mService.registerStateChangeListener(6, this);
            this.mService.registerStateChangeListener(7, this);
            this.mService.registerStateChangeListener(9, this);
            this.mService.registerStateChangeListener(13, this);
            this.mService.registerStateChangeListener(14, this);
        } else {
            log("No need to listen. SAR is no need!");
        }
        if ((1 != 0 || this.mSarTestProState) && CNFlag) {
            log("DSI_Hash_Init_CN_TA");
            this.mService.registerStateChangeListener(3, this);
            this.mService.registerStateChangeListener(5, this);
            this.mService.registerStateChangeListener(1, this);
            this.mService.registerStateChangeListener(7, this);
            this.mService.registerStateChangeListener(9, this);
            this.mService.registerStateChangeListener(13, this);
            this.mService.registerStateChangeListener(14, this);
            return;
        }
        log("No need to listen CN. SAR is no need!");
    }

    private void initRegStateAction() {
        String listeners = mCurrentSarConfig.getListeners();
        log("listeners: receiver,hotSpot(wifi_spot,(bluetooth,bluetooth_pan)),sensor1,sensor2,mcc,wifi,cable,hall,usb, smartenv = " + listeners);
        if (listeners != null) {
            for (int i = 0; i < listeners.length(); i++) {
                if (listeners.charAt(i) == '1') {
                    switch (i) {
                        case 0:
                            this.mService.registerStateChangeListener(5, this);
                            mUsedStateList.add(0);
                            break;
                        case 1:
                            this.mService.registerStateChangeListener(1, this);
                            mUsedStateList.add(1);
                            break;
                        case 2:
                            this.mService.registerStateChangeListener(8, this);
                            mUsedStateList.add(2);
                            break;
                        case 3:
                            this.mService.registerStateChangeListener(7, this);
                            mUsedStateList.add(3);
                            break;
                        case 4:
                            this.mService.registerStateChangeListener(3, this);
                            mUsedStateList.add(4);
                            break;
                        case 5:
                            this.mService.registerStateChangeListener(9, this);
                            mUsedStateList.add(5);
                            break;
                        case 6:
                            this.mService.registerStateChangeListener(6, this);
                            mUsedStateList.add(6);
                            break;
                        case 7:
                            this.mService.registerStateChangeListener(0, this);
                            mUsedStateList.add(7);
                            break;
                        case 8:
                            this.mService.registerStateChangeListener(10, this);
                            mUsedStateList.add(8);
                            break;
                        case 9:
                            this.mService.registerStateChangeListener(4, this);
                            mUsedStateList.add(9);
                            break;
                    }
                }
            }
        }
    }

    private void initRegExceptionAction() {
        this.mService.registerStateChangeListener(13, this);
        this.mService.registerStateChangeListener(14, this);
    }

    @Override // com.xiaomi.internal.telephony.DynamicSarService.SarControllerClient
    public void onStateChanged(int type, int value) {
        log("onStateChanged: type = " + type + ", value = " + value);
        switch (type) {
            case 0:
                sendMsgToHandler(4, value, 0);
                break;
            case 1:
                sendMsgToHandler(3, value, 0);
                break;
            case 3:
                sendMsgToHandler(1, value, 0);
                break;
            case 4:
                sendMsgToHandler(5, value, 0);
                break;
            case 5:
                sendMsgToHandler(2, value, 0);
                break;
            case 6:
                sendMsgToHandler(6, value, 0);
                break;
            case 7:
                sendMsgToHandler(7, value, 0);
                break;
            case 9:
                sendMsgToHandler(8, value, 0);
                break;
            case 10:
                sendMsgToHandler(9, value, 0);
                break;
            case 13:
                sendMsgToHandler(12, value, 0);
                break;
            case 14:
                sendMsgToHandler(13, value, 0);
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getDsiVaule() {
        String stateStr = "";
        boolean isInput = false;
        if (mUsedStateList.size() == 0) {
            return 0;
        }
        for (int i = 0; i < mUsedStateList.size(); i++) {
            switch (mUsedStateList.get(i).intValue()) {
                case 0:
                    stateStr = stateStr + Integer.toString(ReceiverState);
                    break;
                case 1:
                case 2:
                case 3:
                    if (!isInput) {
                        stateStr = stateStr + Integer.toString(HotspotBtPan);
                        isInput = true;
                    }
                    break;
                case 4:
                    stateStr = stateStr + Integer.toString(SarSensorState);
                    break;
                case 5:
                    stateStr = stateStr + Integer.toString(SarSensor2State);
                    break;
                case 6:
                    stateStr = stateStr + Integer.toString(MccState);
                    break;
                case 7:
                    stateStr = stateStr + Integer.toString(WIFIState);
                    break;
                case 8:
                    stateStr = stateStr + Integer.toString(Cablestate);
                    break;
                case 9:
                    stateStr = stateStr + Integer.toString(HallState);
                    break;
            }
        }
        log("stateStr = " + stateStr);
        int result = findDsiFromXML(stateStr.toCharArray());
        return result;
    }

    private static int findDsiFromXML(char[] sourStateList) {
        if (sourStateList.length < 1) {
            Log.d(LOG_TAG, "sourStateList is null. please check the wifi, bluetooth... states");
            return 3;
        }
        Map<String, String> dsiMap = mCurrentSarConfig.getDsiList();
        for (Map.Entry<String, String> entry : dsiMap.entrySet()) {
            log("DerStateSet = " + entry.getKey() + ", dsi = " + entry.getValue());
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
                log("findDsiFromXML: len = " + len + ", sourStrLen = " + sourStateList.length);
                if (len == sourStateList.length - 1 && flag) {
                    int result = Integer.parseInt(entry.getValue());
                    log("findDsiFromXML success: result = " + result);
                    return result;
                }
            } else {
                log("sourStateSet size is not same as desStateList,please check again.");
                return 3;
            }
        }
        return 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int sarExceptionHandle(int sarSensorState, int value) {
        if (3 == sarSensorState || 4 == sarSensorState) {
            log("Sar Sensor Exception: state = " + sarSensorState);
            return value;
        }
        return sarSensorState;
    }

    private DeviceSarConfigLegacy readXmlBySAX(String url, String deviceName, String hwName, String swName) {
        List<DeviceSarConfigLegacy> deviceSarConfigList = null;
        InputStream mInputStream = null;
        try {
            mInputStream = new FileInputStream(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mInputStream != null) {
            try {
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser saxParser = spf.newSAXParser();
                XMLContentHandlerLegacy handler = new XMLContentHandlerLegacy();
                saxParser.parse(mInputStream, handler);
                mInputStream.close();
                deviceSarConfigList = handler.getDeviceSarConfig();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        if (deviceSarConfigList != null) {
            for (int i = 0; i < deviceSarConfigList.size(); i++) {
                DeviceSarConfigLegacy tag = deviceSarConfigList.get(i);
                String readDeviceName = tag.getDeviceName();
                String readDeviceHWName = tag.getPropList().get("ro.boot.hwc");
                String readDeviceSWNameGet = tag.getPropList().get("ro.product.mod_device");
                String readDeviceSWName = (String) Optional.ofNullable(readDeviceSWNameGet).orElse("null");
                log("Get from xml file:  deviceName=" + readDeviceName + " SWname=" + readDeviceSWName + " Hwname=" + readDeviceHWName);
                if (readDeviceName.equalsIgnoreCase(deviceName) && readDeviceHWName.contains(hwName) && readDeviceSWName.contains(swName)) {
                    log("find the config success!!!");
                    return tag;
                }
                if (readDeviceName.equalsIgnoreCase(deviceName) && readDeviceHWName.contains(ModemUtils.PROP_NTN_LOG_LEVEL_DEFAULT) && readDeviceSWName.contains(ModemUtils.PROP_NTN_LOG_LEVEL_DEFAULT)) {
                    log("find the config success, default!!!");
                    return tag;
                }
            }
            return null;
        }
        log("Parse xml failed, don`t have the deviceSarConfigList!");
        return null;
    }

    private void sendMsgToHandler(int message, int arg1, int arg2) {
        log("sendMsgToHandler, Message = " + message + ", arg1 = " + arg1 + ", arg2 = " + arg2);
        Handler handler = this.mCmdProc.getCmdHandler();
        if (handler != null) {
            Message msg = handler.obtainMessage(message, arg1, arg2);
            handler.sendMessage(msg);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void log(String s) {
        Log.d(LOG_TAG, s);
    }

    private static class CmdProcThread extends Thread {
        private Handler mHandler;

        private CmdProcThread() {
            this.mHandler = null;
        }

        public Handler getCmdHandler() {
            if (this.mHandler == null) {
                Log.e(ModemSarControllerTA.LOG_TAG, "getCmdHandler, handler is NULL!!!");
            }
            return this.mHandler;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Looper.prepare();
            this.mHandler = new CmdHandler();
            ModemSarControllerTA.log("CmdProcThread, thread is running up!!!!");
            Looper.loop();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void sendSarToModem(int currentDsi) {
            if (ModemSarControllerTA.sCi != null) {
                for (int i = 0; i < ModemSarControllerTA.sCi.length; i++) {
                    if (1 == ModemSarControllerTA.sCi[i].getRadioState()) {
                        ModemSarControllerTA.sCi[i].invokeOemRilRequestStrings(new String[]{"AT+ERFIDX=1," + currentDsi, ""}, (Message) null);
                        return;
                    }
                    ModemSarControllerTA.log("phone[" + i + "] radio is unavailable or off");
                }
                return;
            }
            Log.e(ModemSarControllerTA.LOG_TAG, "sCi[0] is NULL!!!");
        }

        private static class CmdHandler extends Handler {
            private CmdHandler() {
            }

            private void DSI_Handle(int type) {
                ModemSarControllerTA.log("hallState is = " + ModemSarControllerTA.HallState);
                ModemSarControllerTA.log("WIFIState is = " + ModemSarControllerTA.WIFIState);
                ModemSarControllerTA.log("MCC state is = " + ModemSarControllerTA.MccState);
                ModemSarControllerTA.log("Bluetooth pan state is = " + ModemSarControllerTA.BtPanState);
                ModemSarControllerTA.log("------------------------------------------");
                ModemSarControllerTA.log("ReceiverState is = " + ModemSarControllerTA.ReceiverState);
                ModemSarControllerTA.log("HotspotState is = " + ModemSarControllerTA.HotspotState);
                ModemSarControllerTA.log("SarSensorState is = " + ModemSarControllerTA.SarSensorState);
                ModemSarControllerTA.log("SarSensor2State is = " + ModemSarControllerTA.SarSensor2State);
                ModemSarControllerTA.log("Cablestate is = " + ModemSarControllerTA.Cablestate);
                ModemSarControllerTA.log("RadioPowerState is = " + ModemSarControllerTA.RadioPowerState);
                ModemSarControllerTA.log("SIMCardState is = " + ModemSarControllerTA.SIMCardState);
                if (ModemSarControllerTA.HotspotState == 1 || ModemSarControllerTA.BtPanState == 1) {
                    ModemSarControllerTA.HotspotBtPan = 1;
                }
                ModemSarControllerTA.log("HotspotBtPan is = " + ModemSarControllerTA.HotspotBtPan);
                ModemSarControllerTA.DSI_Current = ModemSarControllerTA.getDsiVaule();
                ModemSarControllerTA.log("DSI_Current is = " + ModemSarControllerTA.DSI_Current);
                if (ModemSarControllerTA.DSI_Current >= 0 && ModemSarControllerTA.DSI_Current != ModemSarControllerTA.DSI_History) {
                    CmdProcThread.sendSarToModem(ModemSarControllerTA.DSI_Current);
                } else if (type == 13 || type == 12) {
                    ModemSarControllerTA.log("radio power on or SIM card loaded, send the current DSI");
                    CmdProcThread.sendSarToModem(ModemSarControllerTA.DSI_Current);
                } else {
                    ModemSarControllerTA.log("The current DSI have set, don`t need to set it again.");
                }
                ModemSarControllerTA.DSI_History = ModemSarControllerTA.DSI_Current;
                ModemSarControllerTA.HotspotBtPan = 0;
            }

            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                ModemSarControllerTA.log("handlerMessage, msg = " + msg.what);
                switch (msg.what) {
                    case 1:
                        ModemSarControllerTA.SarSensorState = ModemSarControllerTA.sarExceptionHandle(msg.arg1, 1);
                        ModemSarControllerTA.log("EVENT_SAR_SENSOR_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 2:
                        ModemSarControllerTA.ReceiverState = msg.arg1;
                        ModemSarControllerTA.log("EVENT_RECEIVER_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 3:
                        ModemSarControllerTA.HotspotState = msg.arg1;
                        ModemSarControllerTA.log("EVENT_WIFI_HOSP_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 4:
                        ModemSarControllerTA.WIFIState = msg.arg1;
                        ModemSarControllerTA.log("EVENT_WIFI_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 5:
                        ModemSarControllerTA.HallState = msg.arg1;
                        ModemSarControllerTA.log("EVENT_HALL_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 6:
                        ModemSarControllerTA.MccState = msg.arg1;
                        ModemSarControllerTA.log("EVENT_MCC_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 7:
                        ModemSarControllerTA.BtPanState = msg.arg1;
                        ModemSarControllerTA.log("EVENT_BT_PAN_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 8:
                        ModemSarControllerTA.SarSensor2State = ModemSarControllerTA.sarExceptionHandle(msg.arg1, 1);
                        ModemSarControllerTA.log("EVENT_SAR_SENSOR2_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 9:
                        ModemSarControllerTA.Cablestate = msg.arg1;
                        ModemSarControllerTA.log("EVENT_CABLE_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 12:
                        ModemSarControllerTA.log("EVENT_RADIO_POWER_CHANGE");
                        ModemSarControllerTA.RadioPowerState = msg.arg1;
                        DSI_Handle(msg.what);
                        break;
                    case 13:
                        ModemSarControllerTA.log("EVENT_SIMCARD_CHANGE");
                        ModemSarControllerTA.SIMCardState = msg.arg1;
                        DSI_Handle(msg.what);
                        break;
                }
            }
        }
    }
}
