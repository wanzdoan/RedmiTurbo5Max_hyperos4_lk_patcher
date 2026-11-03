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
public class ModemSarControllerM12CN implements DynamicSarService.SarControllerClient {
    private static int BtPanState = 0;
    private static int Cablestate = 0;
    private static final String DEVICE_INFO_HW_INDIA = "IN";
    private static final String DEVICE_INFO_SW_INDIA = "in_global";
    private static final String DEVICE_TYPE_CHOPIN = "chopin";
    private static final String DEVICE_TYPE_M12 = "corot";
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
    public static final String LOG_TAG = "ModemSarControllerM12CN";
    private static int MccState = 3;
    private static final String PROPERTY_DYNAMIC_SAR = "persist.vendor.radio.dynamic_sar";
    private static final String PROPERTY_GLOBAL_HW = "ro.boot.hwc";
    private static final String PROPERTY_GLOBAL_VER = "ro.product.mod_device";
    private static int RadioPowerState = 0;
    private static int ReceiverState = 0;
    private static final int SAR_MODE = 1;
    private static int SIMCardState = 0;
    private static int SarSensor2State = 0;
    private static int SarSensorState = 0;
    private static int VoltageState = 1;
    private static int WIFIState;
    private static String mGlobalHW;
    private static String mGlobalSW;
    private static CommandsInterface[] sCi;
    private static ModemSarControllerM12CN sIntance;
    CmdProcThread mCmdProc;
    private boolean mSarTestProState;
    private DynamicSarService mService;

    public static void make(Context context, CommandsInterface[] commandsInterfaceArr) {
        sCi = commandsInterfaceArr;
        if (sIntance == null) {
            sIntance = new ModemSarControllerM12CN(context);
        }
    }

    private ModemSarControllerM12CN(Context context) {
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
        boolean z = mGlobalHW.equals(l.a) && !mGlobalSW.contains("global");
        if (!mGlobalHW.contains(DEVICE_INFO_HW_INDIA)) {
            mGlobalSW.contains(DEVICE_INFO_SW_INDIA);
        }
        log("IndiaFlag = " + mGlobalHW.contains(DEVICE_INFO_HW_INDIA) + "  +  " + mGlobalSW.contains(DEVICE_INFO_SW_INDIA));
        log("globalHW = " + mGlobalHW + ", globalSW = " + mGlobalSW + ", mSarSensorEnabled = " + z);
        if (z || this.mSarTestProState) {
            log("DSI_Hash_Init_CN_M12");
            this.mService.registerStateChangeListener(5, this);
            this.mService.registerStateChangeListener(13, this);
            this.mService.registerStateChangeListener(14, this);
            CmdProcThread.sendSarToModem(0);
            return;
        }
        log("No need to listen M12 CN. SAR is no need!");
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
                Log.e(ModemSarControllerM12CN.LOG_TAG, "getCmdHandler, handler is NULL!!!");
            }
            return this.mHandler;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Looper.prepare();
            this.mHandler = new CmdHandler();
            ModemSarControllerM12CN.log("CmdProcThread, thread is running up!!!!");
            Looper.loop();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void sendSarToModem(int i) {
            if (ModemSarControllerM12CN.sCi != null) {
                for (int i2 = 0; i2 < ModemSarControllerM12CN.sCi.length; i2++) {
                    if (1 == ModemSarControllerM12CN.sCi[i2].getRadioState()) {
                        ModemSarControllerM12CN.sCi[i2].invokeOemRilRequestStrings(new String[]{"AT+ERFIDX=1," + i, ""}, (Message) null);
                        return;
                    }
                    ModemSarControllerM12CN.log("phone[" + i2 + "] radio is unavailable or off");
                }
                return;
            }
            Log.e(ModemSarControllerM12CN.LOG_TAG, "sCi[0] is NULL!!!");
        }

        class CmdHandler extends Handler {
            private CmdHandler() {
            }

            private void DSI_Handle(int i) {
                ModemSarControllerM12CN.log("hallState is = " + ModemSarControllerM12CN.HallState);
                ModemSarControllerM12CN.log("WIFIState is = " + ModemSarControllerM12CN.WIFIState);
                ModemSarControllerM12CN.log("MCC state is = " + ModemSarControllerM12CN.MccState);
                ModemSarControllerM12CN.log("Bluetooth pan state is = " + ModemSarControllerM12CN.BtPanState);
                ModemSarControllerM12CN.log("HotspotState is = " + ModemSarControllerM12CN.HotspotState);
                ModemSarControllerM12CN.log("SarSensorState is = " + ModemSarControllerM12CN.SarSensorState);
                ModemSarControllerM12CN.log("SarSensor2State is = " + ModemSarControllerM12CN.SarSensor2State);
                ModemSarControllerM12CN.log("------------------------------------------");
                ModemSarControllerM12CN.log("Cablestate is = " + ModemSarControllerM12CN.Cablestate);
                ModemSarControllerM12CN.log("ReceiverState is = " + ModemSarControllerM12CN.ReceiverState);
                ModemSarControllerM12CN.log("RadioPowerState is = " + ModemSarControllerM12CN.RadioPowerState);
                ModemSarControllerM12CN.log("SIMCardState is = " + ModemSarControllerM12CN.SIMCardState);
                if (ModemSarControllerM12CN.HotspotState == 1 || ModemSarControllerM12CN.BtPanState == 1) {
                    ModemSarControllerM12CN.HotspotBtPan = 1;
                }
                ModemSarControllerM12CN.log("HotspotBtPan is = " + ModemSarControllerM12CN.HotspotBtPan);
                ModemSarControllerM12CN.DSI_Current = ModemSarControllerM12CN.getCurrentDSI(ModemSarControllerM12CN.ReceiverState, ModemSarControllerM12CN.Cablestate);
                ModemSarControllerM12CN.log("DSI_Current is = " + ModemSarControllerM12CN.DSI_Current);
                if (ModemSarControllerM12CN.DSI_Current >= 0 && ModemSarControllerM12CN.DSI_Current != ModemSarControllerM12CN.DSI_History) {
                    CmdProcThread.sendSarToModem(ModemSarControllerM12CN.DSI_Current);
                } else if (i == 13 || i == 12) {
                    ModemSarControllerM12CN.log("radio power on or SIM card loaded, send the current DSI");
                    CmdProcThread.sendSarToModem(ModemSarControllerM12CN.DSI_Current);
                } else {
                    ModemSarControllerM12CN.log("The current DSI have set, don`t need to set it again.");
                }
                ModemSarControllerM12CN.DSI_History = ModemSarControllerM12CN.DSI_Current;
                ModemSarControllerM12CN.HotspotBtPan = 0;
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                ModemSarControllerM12CN.log("handlerMessage, msg = " + message.what);
                switch (message.what) {
                    case 1:
                        ModemSarControllerM12CN.SarSensorState = message.arg1;
                        ModemSarControllerM12CN.log("EVENT_SAR_SENSOR_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 2:
                        ModemSarControllerM12CN.ReceiverState = message.arg1;
                        ModemSarControllerM12CN.log("EVENT_RECEIVER_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 3:
                        ModemSarControllerM12CN.HotspotState = message.arg1;
                        ModemSarControllerM12CN.log("EVENT_WIFI_HOSP_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 4:
                        ModemSarControllerM12CN.WIFIState = message.arg1;
                        ModemSarControllerM12CN.log("EVENT_WIFI_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 5:
                        ModemSarControllerM12CN.HallState = message.arg1;
                        ModemSarControllerM12CN.log("EVENT_HALL_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 6:
                        ModemSarControllerM12CN.MccState = message.arg1;
                        ModemSarControllerM12CN.log("EVENT_MCC_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 7:
                        ModemSarControllerM12CN.BtPanState = message.arg1;
                        ModemSarControllerM12CN.log("EVENT_BT_PAN_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 8:
                        ModemSarControllerM12CN.SarSensor2State = message.arg1;
                        ModemSarControllerM12CN.log("EVENT_SAR_SENSOR2_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 9:
                        ModemSarControllerM12CN.Cablestate = message.arg1;
                        ModemSarControllerM12CN.log("EVENT_CABLE_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 12:
                        ModemSarControllerM12CN.log("EVENT_RADIO_POWER_CHANGE");
                        ModemSarControllerM12CN.RadioPowerState = message.arg1;
                        DSI_Handle(message.what);
                        break;
                    case 13:
                        ModemSarControllerM12CN.log("EVENT_SIMCARD_CHANGE");
                        ModemSarControllerM12CN.SIMCardState = message.arg1;
                        DSI_Handle(message.what);
                        break;
                }
            }
        }
    }
}
