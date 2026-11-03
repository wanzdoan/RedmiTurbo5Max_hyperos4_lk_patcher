package com.xiaomi.internal.telephony;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import com.android.internal.telephony.CommandsInterface;
import com.ot.pubsub.g.l;

/* JADX INFO: loaded from: classes.dex */
public class ModemSarControllerM9CN implements DynamicSarService.SarControllerClient {
    private static int BtPanState = 0;
    private static int Cablestate = 0;
    private static final String DEVICE_INFO_HW_INDIA = "IN";
    private static final String DEVICE_INFO_SW_INDIA = "in_global";
    private static final String DEVICE_TYPE_CHOPIN = "chopin";
    private static final String DEVICE_TYPE_M9 = "yuechu";
    private static final String DEVICE_TYPE_PISSARRO = "pissarro";
    private static int[] DSI_ANT = new int[15];
    private static int DSI_Current = -1;
    private static int DSI_History = -1;
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
    private static int HallState = 0;
    private static int HotspotBtPan = 0;
    private static int HotspotState = 0;
    public static final String LOG_TAG = "ModemSarControllerM9CN";
    private static int MccState = 3;
    private static final String PROPERTY_DYNAMIC_SAR = "persist.vendor.radio.dynamic_sar";
    private static final String PROPERTY_GLOBAL_HW = "ro.boot.hwc";
    private static final String PROPERTY_GLOBAL_NEW_SAR = "ro.boot.product.new_sar";
    private static final String PROPERTY_GLOBAL_VER = "ro.product.mod_device";
    private static int RadioPowerState = 0;
    private static int ReceiverState = 0;
    private static final int SAR_MODE = 1;
    private static int SIMCardState = 0;
    private static int SarSensor2State = 0;
    private static int SarSensorState = 0;
    private static int VoltageState = 1;
    private static int WIFIState;
    private static String mDeviceName;
    private static String mGlobalHW;
    private static String mGlobalSW;
    private static String mSarVersion;
    private static CommandsInterface[] sCi;
    private static ModemSarControllerM9CN sIntance;
    CmdProcThread mCmdProc;
    private boolean mSarTestProState;
    private DynamicSarService mService;

    public static void make(Context context, CommandsInterface[] commandsInterfaceArr) {
        sCi = commandsInterfaceArr;
        if (sIntance == null) {
            sIntance = new ModemSarControllerM9CN(context);
        }
    }

    private ModemSarControllerM9CN(Context context) {
        this.mCmdProc = null;
        this.mSarTestProState = false;
        log("start in...");
        int i = SystemProperties.getInt("persist.vendor.radio.dynamic_sar", 0);
        log("mSarSensorProState = " + i);
        if (i == 0) {
            log("Sar feature is disable.");
            return;
        }
        if (i == 2) {
            log("Sar feature is enable for test. Ignore the global HW and SW.");
            this.mSarTestProState = true;
        }
        CmdProcThread cmdProcThread = new CmdProcThread();
        this.mCmdProc = cmdProcThread;
        cmdProcThread.start();
        DynamicSarService dynamicSarService = DynamicSarService.getInstance(context);
        this.mService = dynamicSarService;
        if (dynamicSarService != null) {
            customeSarFunc();
        } else {
            log("DynamicSarService is null!");
        }
    }

    public void customeSarFunc() {
        mGlobalHW = SystemProperties.get("ro.boot.hwc");
        mGlobalSW = SystemProperties.get("ro.product.mod_device");
        mSarVersion = SystemProperties.get(PROPERTY_GLOBAL_NEW_SAR);
        boolean z = mGlobalHW.equals(l.a) && !mGlobalSW.contains("global");
        if (!mGlobalHW.contains(DEVICE_INFO_HW_INDIA)) {
            mGlobalSW.contains(DEVICE_INFO_SW_INDIA);
        }
        boolean zEquals = mSarVersion.equals("SARSensor");
        log("IndiaFlag = " + mGlobalHW.contains(DEVICE_INFO_HW_INDIA) + "  +  " + mGlobalSW.contains(DEVICE_INFO_SW_INDIA));
        log("globalHW = " + mGlobalHW + ", globalSW = " + mGlobalSW + ", mSarSensorEnabled = " + z + ", mNewSarFlag = " + zEquals);
        if (z || this.mSarTestProState) {
            log("DSI_Hash_Init_CN_M9");
            this.mService.registerStateChangeListener(5, this);
            this.mService.registerStateChangeListener(13, this);
            this.mService.registerStateChangeListener(14, this);
            if (zEquals) {
                this.mService.registerStateChangeListener(3, this);
                this.mService.registerStateChangeListener(9, this);
                return;
            } else {
                CmdProcThread.sendSarToModem(0);
                return;
            }
        }
        log("No need to listen M9 CN. SAR is no need!");
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
    public static int getCurrentDSI(int i, int i2) {
        log("getCurrentDSI");
        if (i2 == 1) {
            return 5;
        }
        return i == 1 ? 0 : 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int newGetCurrentDSI(int i, int i2, int i3, int i4) {
        log("newGetCurrentDSI");
        if (i2 == 1) {
            return 5;
        }
        if (i == 1) {
            return 0;
        }
        return (i3 == 0 && i4 == 0) ? 3 : 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getCurrentDSIStr(int i, int i2, int i3, int i4) {
        int[] iArr;
        log("getCurrentDSIStr");
        int i5 = 0;
        while (true) {
            iArr = DSI_ANT;
            if (i5 >= iArr.length) {
                break;
            }
            iArr[i5] = 3;
            i5++;
        }
        if (i2 == 1) {
            iArr[7] = 5;
            iArr[6] = 5;
            iArr[5] = 5;
            iArr[4] = 5;
            iArr[3] = 5;
            iArr[2] = 5;
            iArr[1] = 5;
            iArr[0] = 5;
        } else if (i == 1) {
            iArr[7] = 0;
            iArr[6] = 0;
            iArr[5] = 0;
            iArr[4] = 0;
            iArr[3] = 0;
            iArr[2] = 0;
            iArr[1] = 0;
            iArr[0] = 0;
        } else {
            if (i3 != 0) {
                iArr[2] = 1;
                iArr[0] = 1;
            }
            if (i4 != 0) {
                iArr[7] = 1;
                iArr[4] = 1;
                iArr[3] = 1;
                iArr[1] = 1;
            }
        }
        StringBuffer stringBuffer = new StringBuffer("AT+EGMC=1,\"sar_scenario_all\"");
        for (int i6 = 0; i6 < DSI_ANT.length; i6++) {
            log("DSI_ANT[" + i6 + "] = " + DSI_ANT[i6]);
            stringBuffer.append(",");
            stringBuffer.append(DSI_ANT[i6]);
        }
        String string = stringBuffer.toString();
        log("dsi_tempstr = " + string);
        return string;
    }

    private void sendMsgToHandler(int i, int i2, int i3) {
        log("sendMsgToHandler, Message = " + i + ", arg1 = " + i2 + ", arg2 = " + i3);
        Handler cmdHandler = this.mCmdProc.getCmdHandler();
        cmdHandler.sendMessage(cmdHandler.obtainMessage(i, i2, i3));
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
                Log.e(ModemSarControllerM9CN.LOG_TAG, "getCmdHandler, handler is NULL!!!");
            }
            return this.mHandler;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Looper.prepare();
            this.mHandler = new CmdHandler();
            ModemSarControllerM9CN.log("CmdProcThread, thread is running up!!!!");
            Looper.loop();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void sendSarToModem(int i) {
            if (ModemSarControllerM9CN.sCi != null) {
                for (int i2 = 0; i2 < ModemSarControllerM9CN.sCi.length; i2++) {
                    if (1 == ModemSarControllerM9CN.sCi[i2].getRadioState()) {
                        ModemSarControllerM9CN.sCi[i2].invokeOemRilRequestStrings(new String[]{"AT+ERFIDX=1," + i, ""}, (Message) null);
                        return;
                    }
                    ModemSarControllerM9CN.log("phone[" + i2 + "] radio is unavailable or off");
                }
                return;
            }
            Log.e(ModemSarControllerM9CN.LOG_TAG, "sCi[0] is NULL!!!");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void newSendSarToModem(String str) {
            if (ModemSarControllerM9CN.sCi != null) {
                for (int i = 0; i < ModemSarControllerM9CN.sCi.length; i++) {
                    if (1 == ModemSarControllerM9CN.sCi[i].getRadioState()) {
                        ModemSarControllerM9CN.sCi[i].invokeOemRilRequestStrings(new String[]{str, ""}, (Message) null);
                        return;
                    }
                    ModemSarControllerM9CN.log("phone[" + i + "] radio is unavailable or off");
                }
                return;
            }
            Log.e(ModemSarControllerM9CN.LOG_TAG, "sCi[0] is NULL!!!");
        }

        class CmdHandler extends Handler {
            private CmdHandler() {
            }

            private void DSI_Handle(int i) {
                ModemSarControllerM9CN.log("hallState is = " + ModemSarControllerM9CN.HallState);
                ModemSarControllerM9CN.log("WIFIState is = " + ModemSarControllerM9CN.WIFIState);
                ModemSarControllerM9CN.log("MCC state is = " + ModemSarControllerM9CN.MccState);
                ModemSarControllerM9CN.log("Bluetooth pan state is = " + ModemSarControllerM9CN.BtPanState);
                ModemSarControllerM9CN.log("HotspotState is = " + ModemSarControllerM9CN.HotspotState);
                ModemSarControllerM9CN.log("SarSensorState is = " + ModemSarControllerM9CN.SarSensorState);
                ModemSarControllerM9CN.log("SarSensor2State is = " + ModemSarControllerM9CN.SarSensor2State);
                ModemSarControllerM9CN.log("------------------------------------------");
                ModemSarControllerM9CN.log("Cablestate is = " + ModemSarControllerM9CN.Cablestate);
                ModemSarControllerM9CN.log("ReceiverState is = " + ModemSarControllerM9CN.ReceiverState);
                ModemSarControllerM9CN.log("RadioPowerState is = " + ModemSarControllerM9CN.RadioPowerState);
                ModemSarControllerM9CN.log("SIMCardState is = " + ModemSarControllerM9CN.SIMCardState);
                if (ModemSarControllerM9CN.HotspotState == 1 || ModemSarControllerM9CN.BtPanState == 1) {
                    ModemSarControllerM9CN.HotspotBtPan = 1;
                }
                ModemSarControllerM9CN.log("HotspotBtPan is = " + ModemSarControllerM9CN.HotspotBtPan);
                ModemSarControllerM9CN.mSarVersion = SystemProperties.get(ModemSarControllerM9CN.PROPERTY_GLOBAL_NEW_SAR);
                if (ModemSarControllerM9CN.mSarVersion.equals("SARSensor")) {
                    ModemSarControllerM9CN.DSI_Current = ModemSarControllerM9CN.newGetCurrentDSI(ModemSarControllerM9CN.ReceiverState, ModemSarControllerM9CN.Cablestate, ModemSarControllerM9CN.SarSensorState, ModemSarControllerM9CN.SarSensor2State);
                    ModemSarControllerM9CN.log("DSI_Current is = " + ModemSarControllerM9CN.DSI_Current);
                    String currentDSIStr = ModemSarControllerM9CN.getCurrentDSIStr(ModemSarControllerM9CN.ReceiverState, ModemSarControllerM9CN.Cablestate, ModemSarControllerM9CN.SarSensorState, ModemSarControllerM9CN.SarSensor2State);
                    if (ModemSarControllerM9CN.DSI_Current == 1 || ModemSarControllerM9CN.DSI_Current != ModemSarControllerM9CN.DSI_History) {
                        CmdProcThread.newSendSarToModem(currentDSIStr);
                    } else if (i == 13 || i == 12) {
                        ModemSarControllerM9CN.log("radio power on or SIM card loaded, send the current DSI");
                        CmdProcThread.newSendSarToModem(currentDSIStr);
                    } else {
                        ModemSarControllerM9CN.log("The current DSI have set, don`t need to set it again.");
                    }
                } else {
                    ModemSarControllerM9CN.DSI_Current = ModemSarControllerM9CN.getCurrentDSI(ModemSarControllerM9CN.ReceiverState, ModemSarControllerM9CN.Cablestate);
                    ModemSarControllerM9CN.log("DSI_Current is = " + ModemSarControllerM9CN.DSI_Current);
                    if (ModemSarControllerM9CN.DSI_Current >= 0 && ModemSarControllerM9CN.DSI_Current != ModemSarControllerM9CN.DSI_History) {
                        CmdProcThread.sendSarToModem(ModemSarControllerM9CN.DSI_Current);
                    } else if (i == 13 || i == 12) {
                        ModemSarControllerM9CN.log("radio power on or SIM card loaded, send the current DSI");
                        CmdProcThread.sendSarToModem(ModemSarControllerM9CN.DSI_Current);
                    } else {
                        ModemSarControllerM9CN.log("The current DSI have set, don`t need to set it again.");
                    }
                }
                ModemSarControllerM9CN.DSI_History = ModemSarControllerM9CN.DSI_Current;
                ModemSarControllerM9CN.HotspotBtPan = 0;
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                ModemSarControllerM9CN.log("handlerMessage, msg = " + message.what);
                switch (message.what) {
                    case 1:
                        ModemSarControllerM9CN.SarSensorState = message.arg1;
                        ModemSarControllerM9CN.log("EVENT_SAR_SENSOR_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 2:
                        ModemSarControllerM9CN.ReceiverState = message.arg1;
                        ModemSarControllerM9CN.log("EVENT_RECEIVER_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 3:
                        ModemSarControllerM9CN.HotspotState = message.arg1;
                        ModemSarControllerM9CN.log("EVENT_WIFI_HOSP_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 4:
                        ModemSarControllerM9CN.WIFIState = message.arg1;
                        ModemSarControllerM9CN.log("EVENT_WIFI_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 5:
                        ModemSarControllerM9CN.HallState = message.arg1;
                        ModemSarControllerM9CN.log("EVENT_HALL_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 6:
                        ModemSarControllerM9CN.MccState = message.arg1;
                        ModemSarControllerM9CN.log("EVENT_MCC_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 7:
                        ModemSarControllerM9CN.BtPanState = message.arg1;
                        ModemSarControllerM9CN.log("EVENT_BT_PAN_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 8:
                        ModemSarControllerM9CN.SarSensor2State = message.arg1;
                        ModemSarControllerM9CN.log("EVENT_SAR_SENSOR2_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 9:
                        ModemSarControllerM9CN.Cablestate = message.arg1;
                        ModemSarControllerM9CN.log("EVENT_CABLE_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 12:
                        ModemSarControllerM9CN.log("EVENT_RADIO_POWER_CHANGE");
                        ModemSarControllerM9CN.RadioPowerState = message.arg1;
                        DSI_Handle(message.what);
                        break;
                    case 13:
                        ModemSarControllerM9CN.log("EVENT_SIMCARD_CHANGE");
                        ModemSarControllerM9CN.SIMCardState = message.arg1;
                        DSI_Handle(message.what);
                        break;
                }
            }
        }
    }
}
