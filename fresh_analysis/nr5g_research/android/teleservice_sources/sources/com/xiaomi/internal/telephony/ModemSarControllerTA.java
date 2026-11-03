package com.xiaomi.internal.telephony;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import com.android.internal.telephony.CommandsInterface;
import com.ot.pubsub.g.l;
import java.io.FileInputStream;
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
    private static int BtPanState = 0;
    private static final int CABLE_STATE_I = 8;
    private static int Cablestate = 0;
    private static int DSI_Current = 0;
    private static int DSI_History = 0;
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
    private static int HallState = 0;
    private static int HotspotBtPan = 0;
    private static int HotspotState = 0;
    private static final int I2C_ABNORMAL = 3;
    public static final String LOG_TAG = "ModemSarControllerTA";
    private static final int MCC_STATE_I = 6;
    private static int MccState = 3;
    private static final int OFFSET_CAL_ABNORMAL = 4;
    private static final String PROPERTY_DEVICE_NAME = "ro.product.device";
    private static final String PROPERTY_DYNAMIC_SAR = "persist.vendor.radio.dynamic_sar";
    private static final String PROPERTY_GLOBAL_HW = "ro.boot.hwc";
    private static final String PROPERTY_GLOBAL_VER = "ro.product.mod_device";
    private static final int RECEIVER_STATE_I = 0;
    private static int RadioPowerState = 0;
    private static int ReceiverState = 0;
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
    private static int SIMCardState = 0;
    private static int SarSensor2State = 0;
    private static int SarSensorState = 0;
    private static int WIFIState = 0;
    private static final int WIFI_HOSP_STATE_I = 1;
    private static final int WIFI_STATE_I = 7;
    private static DeviceSarConfigLegacy mCurrentSarConfig;
    private static String mDeviceName;
    private static String mGlobalHW;
    private static String mGlobalSW;
    private static ArrayList mUsedStateList = new ArrayList();
    private static CommandsInterface[] sCi = null;
    private static ModemSarControllerTA sIntance;
    CmdProcThread mCmdProc;
    private boolean mSarTestProState = false;
    private DynamicSarService mService;

    public static void make(Context context, CommandsInterface[] commandsInterfaceArr) {
        sCi = commandsInterfaceArr;
        if (sIntance == null) {
            sIntance = new ModemSarControllerTA(context);
        }
    }

    private ModemSarControllerTA(Context context) {
        this.mCmdProc = null;
        int i = 0;
        log("ModemSarControllerTA start in...");
        String str = SystemProperties.get("ro.product.device");
        String str2 = SystemProperties.get("ro.boot.hwc");
        String strReplace = SystemProperties.get("ro.product.mod_device");
        strReplace = strReplace.contains("_pre") ? strReplace.replace("_pre", "") : strReplace;
        log("Device name = " + str + ", globalHW = " + str2 + ", globalSW = " + strReplace);
        int i2 = SystemProperties.getInt("persist.vendor.radio.dynamic_sar", 0);
        StringBuilder sb = new StringBuilder("mSarSensorProState = ");
        sb.append(i2);
        log(sb.toString());
        if (1 == SystemProperties.getInt(ModemSarUtils.PROPERTY_FACTORY_BUILD, 0)) {
            log("This device is factory build!!!");
        } else {
            i = i2;
        }
        if (i == 1) {
            DeviceSarConfigLegacy xmlBySAX = readXmlBySAX(ModemSarUtils.PATH_SAR_CONFIG, str, str2, strReplace);
            mCurrentSarConfig = xmlBySAX;
            if (xmlBySAX != null) {
                CmdProcThread cmdProcThread = new CmdProcThread();
                this.mCmdProc = cmdProcThread;
                cmdProcThread.start();
                DynamicSarService dynamicSarService = DynamicSarService.getInstance(context);
                this.mService = dynamicSarService;
                if (dynamicSarService != null) {
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
        boolean z = mGlobalHW.equals(l.a) && !mGlobalSW.contains("global");
        log("Device name = " + mDeviceName + ", globalHW = " + mGlobalHW + ", globalSW = " + mGlobalSW + ", mSarSensorEnabled = true");
        if (!z) {
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
        if (z) {
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
    public void onStateChanged(int i, int i2) {
        log("onStateChanged: type = " + i + ", value = " + i2);
        switch (i) {
            case 0:
                sendMsgToHandler(4, i2, 0);
                break;
            case 1:
                sendMsgToHandler(3, i2, 0);
                break;
            case 3:
                sendMsgToHandler(1, i2, 0);
                break;
            case 4:
                sendMsgToHandler(5, i2, 0);
                break;
            case 5:
                sendMsgToHandler(2, i2, 0);
                break;
            case 6:
                sendMsgToHandler(6, i2, 0);
                break;
            case 7:
                sendMsgToHandler(7, i2, 0);
                break;
            case 9:
                sendMsgToHandler(8, i2, 0);
                break;
            case 10:
                sendMsgToHandler(9, i2, 0);
                break;
            case 13:
                sendMsgToHandler(12, i2, 0);
                break;
            case 14:
                sendMsgToHandler(13, i2, 0);
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getDsiVaule() {
        if (mUsedStateList.size() == 0) {
            return 0;
        }
        String str = "";
        boolean z = false;
        for (int i = 0; i < mUsedStateList.size(); i++) {
            switch (((Integer) mUsedStateList.get(i)).intValue()) {
                case 0:
                    str = str + Integer.toString(ReceiverState);
                    break;
                case 1:
                case 2:
                case 3:
                    if (!z) {
                        str = str + Integer.toString(HotspotBtPan);
                        z = true;
                    }
                    break;
                case 4:
                    str = str + Integer.toString(SarSensorState);
                    break;
                case 5:
                    str = str + Integer.toString(SarSensor2State);
                    break;
                case 6:
                    str = str + Integer.toString(MccState);
                    break;
                case 7:
                    str = str + Integer.toString(WIFIState);
                    break;
                case 8:
                    str = str + Integer.toString(Cablestate);
                    break;
                case 9:
                    str = str + Integer.toString(HallState);
                    break;
            }
        }
        log("stateStr = " + str);
        return findDsiFromXML(str.toCharArray());
    }

    private static int findDsiFromXML(char[] cArr) {
        if (cArr.length < 1) {
            Log.d(LOG_TAG, "sourStateList is null. please check the wifi, bluetooth... states");
            return 3;
        }
        for (Map.Entry entry : mCurrentSarConfig.getDsiList().entrySet()) {
            log("DerStateSet = " + ((String) entry.getKey()) + ", dsi = " + ((String) entry.getValue()));
            char[] charArray = ((String) entry.getKey()).toCharArray();
            if (cArr.length == charArray.length) {
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
                log("findDsiFromXML: len = " + i + ", sourStrLen = " + cArr.length);
                if (i == cArr.length - 1 && z) {
                    int i3 = Integer.parseInt((String) entry.getValue());
                    log("findDsiFromXML success: result = " + i3);
                    return i3;
                }
            } else {
                log("sourStateSet size is not same as desStateList,please check again.");
                break;
            }
        }
        return 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int sarExceptionHandle(int i, int i2) {
        if (3 != i && 4 != i) {
            return i;
        }
        log("Sar Sensor Exception: state = " + i);
        return i2;
    }

    private DeviceSarConfigLegacy readXmlBySAX(String str, String str2, String str3, String str4) {
        FileInputStream fileInputStream;
        List deviceSarConfig;
        try {
            fileInputStream = new FileInputStream(str);
        } catch (Exception e) {
            e.printStackTrace();
            fileInputStream = null;
        }
        if (fileInputStream != null) {
            try {
                SAXParser sAXParserNewSAXParser = SAXParserFactory.newInstance().newSAXParser();
                XMLContentHandlerLegacy xMLContentHandlerLegacy = new XMLContentHandlerLegacy();
                sAXParserNewSAXParser.parse(fileInputStream, xMLContentHandlerLegacy);
                fileInputStream.close();
                deviceSarConfig = xMLContentHandlerLegacy.getDeviceSarConfig();
            } catch (Exception e2) {
                e2.printStackTrace();
                deviceSarConfig = null;
            }
        } else {
            deviceSarConfig = null;
        }
        if (deviceSarConfig != null) {
            for (int i = 0; i < deviceSarConfig.size(); i++) {
                DeviceSarConfigLegacy deviceSarConfigLegacy = (DeviceSarConfigLegacy) deviceSarConfig.get(i);
                String deviceName = deviceSarConfigLegacy.getDeviceName();
                String str5 = (String) deviceSarConfigLegacy.getPropList().get("ro.boot.hwc");
                String str6 = (String) Optional.ofNullable((String) deviceSarConfigLegacy.getPropList().get("ro.product.mod_device")).orElse("null");
                log("Get from xml file:  deviceName=" + deviceName + " SWname=" + str6 + " Hwname=" + str5);
                if (deviceName.equalsIgnoreCase(str2) && str5.contains(str3) && str6.contains(str4)) {
                    log("find the config success!!!");
                } else if (deviceName.equalsIgnoreCase(str2) && str5.contains("default") && str6.contains("default")) {
                    log("find the config success, default!!!");
                }
                return deviceSarConfigLegacy;
            }
            return null;
        }
        log("Parse xml failed, don`t have the deviceSarConfigList!");
        return null;
    }

    private void sendMsgToHandler(int i, int i2, int i3) {
        log("sendMsgToHandler, Message = " + i + ", arg1 = " + i2 + ", arg2 = " + i3);
        Handler cmdHandler = this.mCmdProc.getCmdHandler();
        if (cmdHandler != null) {
            cmdHandler.sendMessage(cmdHandler.obtainMessage(i, i2, i3));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void log(String str) {
        Log.d(LOG_TAG, str);
    }

    class CmdProcThread extends Thread {
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
        public static void sendSarToModem(int i) {
            if (ModemSarControllerTA.sCi != null) {
                for (int i2 = 0; i2 < ModemSarControllerTA.sCi.length; i2++) {
                    if (1 == ModemSarControllerTA.sCi[i2].getRadioState()) {
                        ModemSarControllerTA.sCi[i2].invokeOemRilRequestStrings(new String[]{"AT+ERFIDX=1," + i, ""}, (Message) null);
                        return;
                    }
                    ModemSarControllerTA.log("phone[" + i2 + "] radio is unavailable or off");
                }
                return;
            }
            Log.e(ModemSarControllerTA.LOG_TAG, "sCi[0] is NULL!!!");
        }

        class CmdHandler extends Handler {
            private CmdHandler() {
            }

            private void DSI_Handle(int i) {
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
                } else if (i == 13 || i == 12) {
                    ModemSarControllerTA.log("radio power on or SIM card loaded, send the current DSI");
                    CmdProcThread.sendSarToModem(ModemSarControllerTA.DSI_Current);
                } else {
                    ModemSarControllerTA.log("The current DSI have set, don`t need to set it again.");
                }
                ModemSarControllerTA.DSI_History = ModemSarControllerTA.DSI_Current;
                ModemSarControllerTA.HotspotBtPan = 0;
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                ModemSarControllerTA.log("handlerMessage, msg = " + message.what);
                switch (message.what) {
                    case 1:
                        ModemSarControllerTA.SarSensorState = ModemSarControllerTA.sarExceptionHandle(message.arg1, 1);
                        ModemSarControllerTA.log("EVENT_SAR_SENSOR_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 2:
                        ModemSarControllerTA.ReceiverState = message.arg1;
                        ModemSarControllerTA.log("EVENT_RECEIVER_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 3:
                        ModemSarControllerTA.HotspotState = message.arg1;
                        ModemSarControllerTA.log("EVENT_WIFI_HOSP_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 4:
                        ModemSarControllerTA.WIFIState = message.arg1;
                        ModemSarControllerTA.log("EVENT_WIFI_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 5:
                        ModemSarControllerTA.HallState = message.arg1;
                        ModemSarControllerTA.log("EVENT_HALL_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 6:
                        ModemSarControllerTA.MccState = message.arg1;
                        ModemSarControllerTA.log("EVENT_MCC_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 7:
                        ModemSarControllerTA.BtPanState = message.arg1;
                        ModemSarControllerTA.log("EVENT_BT_PAN_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 8:
                        ModemSarControllerTA.SarSensor2State = ModemSarControllerTA.sarExceptionHandle(message.arg1, 1);
                        ModemSarControllerTA.log("EVENT_SAR_SENSOR2_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 9:
                        ModemSarControllerTA.Cablestate = message.arg1;
                        ModemSarControllerTA.log("EVENT_CABLE_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 12:
                        ModemSarControllerTA.log("EVENT_RADIO_POWER_CHANGE");
                        ModemSarControllerTA.RadioPowerState = message.arg1;
                        DSI_Handle(message.what);
                        break;
                    case 13:
                        ModemSarControllerTA.log("EVENT_SIMCARD_CHANGE");
                        ModemSarControllerTA.SIMCardState = message.arg1;
                        DSI_Handle(message.what);
                        break;
                }
            }
        }
    }
}
