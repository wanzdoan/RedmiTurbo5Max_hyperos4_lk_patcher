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
public class ModemSarControllerN11A implements DynamicSarService.SarControllerClient {
    private static final int BT_PAN_STATE_I = 3;
    private static final int BT_STATE_I = 2;
    private static int BtPanState = 0;
    private static final int CABLE_STATE_I = 8;
    private static int Cablestate = 0;
    private static final String DEVICE_TYPE_N11A = "duchamp";
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
    public static final String LOG_TAG = "ModemSarControllerN11A";
    private static final int MCC_STATE_I = 6;
    private static int MccState = 3;
    private static final String PROPERTY_DEVICE_NAME = "ro.product.device";
    private static final String PROPERTY_DYNAMIC_SAR = "persist.vendor.radio.dynamic_sar";
    private static final String PROPERTY_FACTORY_BUILD = "ro.boot.factorybuild";
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
    private static int VoltageState = 1;
    private static int WIFIState = 0;
    private static final int WIFI_HOSP_STATE_I = 1;
    private static final int WIFI_STATE_I = 7;
    private static DeviceSarConfigLegacy mCurrentSarConfig;
    private static String mDeviceName;
    private static String mGlobalHW;
    private static String mGlobalSW;
    private static ModemSarControllerN11A sIntance;
    CmdProcThread mCmdProc;
    private boolean mSarTestProState = false;
    private DynamicSarService mService;
    private static int[] DSI_ANT = new int[15];
    private static ArrayList mUsedStateList = new ArrayList();
    private static CommandsInterface[] sCi = null;

    public static void make(Context context, CommandsInterface[] commandsInterfaceArr) {
        sCi = commandsInterfaceArr;
        if (sIntance == null) {
            sIntance = new ModemSarControllerN11A(context);
        }
    }

    private ModemSarControllerN11A(Context context) {
        this.mCmdProc = null;
        int i = 0;
        log("ModemSarControllerN11A start in...");
        String str = SystemProperties.get("ro.product.device");
        String str2 = SystemProperties.get("ro.boot.hwc");
        String strReplace = SystemProperties.get("ro.product.mod_device");
        strReplace = strReplace.contains("_pre") ? strReplace.replace("_pre", "") : strReplace;
        log("Device name = " + str + ", globalHW = " + str2 + ", globalSW = " + strReplace);
        int i2 = SystemProperties.getInt("persist.vendor.radio.dynamic_sar", 0);
        StringBuilder sb = new StringBuilder("mSarSensorProState = ");
        sb.append(i2);
        log(sb.toString());
        if (1 == SystemProperties.getInt("ro.boot.factorybuild", 0)) {
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
        if (mDeviceName.contains(DEVICE_TYPE_N11A) && !z) {
            log("DSI_Hash_Init_GL_N11A");
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
            log("No need to listen N11A. SAR is no need!");
        }
        if (mDeviceName.contains(DEVICE_TYPE_N11A) && z) {
            log("DSI_Hash_Init_CN_N11A");
            this.mService.registerStateChangeListener(3, this);
            this.mService.registerStateChangeListener(5, this);
            this.mService.registerStateChangeListener(1, this);
            this.mService.registerStateChangeListener(7, this);
            this.mService.registerStateChangeListener(9, this);
            this.mService.registerStateChangeListener(13, this);
            this.mService.registerStateChangeListener(14, this);
            return;
        }
        log("No need to listen N11A CN. SAR is no need!");
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
                Log.e(ModemSarControllerN11A.LOG_TAG, "getCmdHandler, handler is NULL!!!");
            }
            return this.mHandler;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Looper.prepare();
            this.mHandler = new CmdHandler();
            ModemSarControllerN11A.log("CmdProcThread, thread is running up!!!!");
            Looper.loop();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void sendSarToModem(int i) {
            if (ModemSarControllerN11A.sCi != null) {
                for (int i2 = 0; i2 < ModemSarControllerN11A.sCi.length; i2++) {
                    if (1 == ModemSarControllerN11A.sCi[i2].getRadioState()) {
                        ModemSarControllerN11A.sCi[i2].invokeOemRilRequestStrings(new String[]{"AT+ERFIDX=1," + i, ""}, (Message) null);
                        return;
                    }
                    ModemSarControllerN11A.log("phone[" + i2 + "] radio is unavailable or off");
                }
                return;
            }
            Log.e(ModemSarControllerN11A.LOG_TAG, "sCi[0] is NULL!!!");
        }

        class CmdHandler extends Handler {
            private CmdHandler() {
            }

            private void DSI_Handle(int i) {
                ModemSarControllerN11A.log("hallState is = " + ModemSarControllerN11A.HallState);
                ModemSarControllerN11A.log("WIFIState is = " + ModemSarControllerN11A.WIFIState);
                ModemSarControllerN11A.log("MCC state is = " + ModemSarControllerN11A.MccState);
                ModemSarControllerN11A.log("Bluetooth pan state is = " + ModemSarControllerN11A.BtPanState);
                ModemSarControllerN11A.log("------------------------------------------");
                ModemSarControllerN11A.log("ReceiverState is = " + ModemSarControllerN11A.ReceiverState);
                ModemSarControllerN11A.log("HotspotState is = " + ModemSarControllerN11A.HotspotState);
                ModemSarControllerN11A.log("SarSensorState is = " + ModemSarControllerN11A.SarSensorState);
                ModemSarControllerN11A.log("SarSensor2State is = " + ModemSarControllerN11A.SarSensor2State);
                ModemSarControllerN11A.log("Cablestate is = " + ModemSarControllerN11A.Cablestate);
                ModemSarControllerN11A.log("RadioPowerState is = " + ModemSarControllerN11A.RadioPowerState);
                ModemSarControllerN11A.log("SIMCardState is = " + ModemSarControllerN11A.SIMCardState);
                if (ModemSarControllerN11A.HotspotState == 1 || ModemSarControllerN11A.BtPanState == 1) {
                    ModemSarControllerN11A.HotspotBtPan = 1;
                }
                ModemSarControllerN11A.log("HotspotBtPan is = " + ModemSarControllerN11A.HotspotBtPan);
                ModemSarControllerN11A.DSI_Current = ModemSarControllerN11A.getDsiVaule();
                ModemSarControllerN11A.log("DSI_Current is = " + ModemSarControllerN11A.DSI_Current);
                if (ModemSarControllerN11A.DSI_Current >= 0 && ModemSarControllerN11A.DSI_Current != ModemSarControllerN11A.DSI_History) {
                    CmdProcThread.sendSarToModem(ModemSarControllerN11A.DSI_Current);
                } else if (i == 13 || i == 12) {
                    ModemSarControllerN11A.log("radio power on or SIM card loaded, send the current DSI");
                    CmdProcThread.sendSarToModem(ModemSarControllerN11A.DSI_Current);
                } else {
                    ModemSarControllerN11A.log("The current DSI have set, don`t need to set it again.");
                }
                ModemSarControllerN11A.DSI_History = ModemSarControllerN11A.DSI_Current;
                ModemSarControllerN11A.HotspotBtPan = 0;
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                ModemSarControllerN11A.log("handlerMessage, msg = " + message.what);
                switch (message.what) {
                    case 1:
                        ModemSarControllerN11A.SarSensorState = ModemSarControllerN11A.sarExceptionHandle(message.arg1, 1);
                        ModemSarControllerN11A.log("EVENT_SAR_SENSOR_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 2:
                        ModemSarControllerN11A.ReceiverState = message.arg1;
                        ModemSarControllerN11A.log("EVENT_RECEIVER_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 3:
                        ModemSarControllerN11A.HotspotState = message.arg1;
                        ModemSarControllerN11A.log("EVENT_WIFI_HOSP_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 4:
                        ModemSarControllerN11A.WIFIState = message.arg1;
                        ModemSarControllerN11A.log("EVENT_WIFI_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 5:
                        ModemSarControllerN11A.HallState = message.arg1;
                        ModemSarControllerN11A.log("EVENT_HALL_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 6:
                        ModemSarControllerN11A.MccState = message.arg1;
                        ModemSarControllerN11A.log("EVENT_MCC_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 7:
                        ModemSarControllerN11A.BtPanState = message.arg1;
                        ModemSarControllerN11A.log("EVENT_BT_PAN_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 8:
                        ModemSarControllerN11A.SarSensor2State = ModemSarControllerN11A.sarExceptionHandle(message.arg1, 1);
                        ModemSarControllerN11A.log("EVENT_SAR_SENSOR2_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 9:
                        ModemSarControllerN11A.Cablestate = message.arg1;
                        ModemSarControllerN11A.log("EVENT_CABLE_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 12:
                        ModemSarControllerN11A.log("EVENT_RADIO_POWER_CHANGE");
                        ModemSarControllerN11A.RadioPowerState = message.arg1;
                        DSI_Handle(message.what);
                        break;
                    case 13:
                        ModemSarControllerN11A.log("EVENT_SIMCARD_CHANGE");
                        ModemSarControllerN11A.SIMCardState = message.arg1;
                        DSI_Handle(message.what);
                        break;
                }
            }
        }
    }
}
