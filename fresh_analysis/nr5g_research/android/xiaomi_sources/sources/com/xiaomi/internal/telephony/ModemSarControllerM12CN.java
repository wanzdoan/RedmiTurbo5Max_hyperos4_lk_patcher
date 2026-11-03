package com.xiaomi.internal.telephony;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import com.android.internal.telephony.CommandsInterface;

/* JADX INFO: loaded from: classes.dex */
public class ModemSarControllerM12CN implements DynamicSarService.SarControllerClient {
    private static final String DEVICE_INFO_HW_INDIA = "IN";
    private static final String DEVICE_INFO_SW_INDIA = "in_global";
    private static final String DEVICE_TYPE_CHOPIN = "chopin";
    private static final String DEVICE_TYPE_M12 = "corot";
    private static final String DEVICE_TYPE_PISSARRO = "pissarro";
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
    public static final String LOG_TAG = "ModemSarControllerM12CN";
    private static final String PROPERTY_DYNAMIC_SAR = "persist.vendor.radio.dynamic_sar";
    private static final String PROPERTY_GLOBAL_HW = "ro.boot.hwc";
    private static final String PROPERTY_GLOBAL_VER = "ro.product.mod_device";
    private static final int SAR_MODE = 1;
    private static ModemSarControllerM12CN sIntance;
    CmdProcThread mCmdProc;
    private boolean mSarTestProState;
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
    private static int[] DSI_ANT = new int[15];
    private static int MccState = 3;
    private static int VoltageState = 1;
    private static int BtPanState = 0;
    private static int HotspotBtPan = 0;
    private static int DSI_Current = -1;
    private static int DSI_History = -1;
    private static String mGlobalHW = null;
    private static String mGlobalSW = null;
    private static CommandsInterface[] sCi = null;

    public static void make(Context context, CommandsInterface[] cis) {
        sCi = cis;
        if (sIntance == null) {
            sIntance = new ModemSarControllerM12CN(context);
        }
    }

    private ModemSarControllerM12CN(Context context) {
        this.mCmdProc = null;
        this.mSarTestProState = false;
        log("start in...");
        int mSarSensorProState = SystemProperties.getInt("persist.vendor.radio.dynamic_sar", 0);
        log("mSarSensorProState = " + mSarSensorProState);
        if (mSarSensorProState == 0) {
            log("Sar feature is disable.");
            return;
        }
        if (mSarSensorProState == 2) {
            log("Sar feature is enable for test. Ignore the global HW and SW.");
            this.mSarTestProState = true;
        }
        this.mCmdProc = new CmdProcThread();
        this.mCmdProc.start();
        this.mService = DynamicSarService.getInstance(context);
        if (this.mService != null) {
            customeSarFunc();
        } else {
            log("DynamicSarService is null!");
        }
    }

    public void customeSarFunc() {
        mGlobalHW = SystemProperties.get("ro.boot.hwc");
        mGlobalSW = SystemProperties.get("ro.product.mod_device");
        boolean mSarSensorEnabled = mGlobalHW.equals("CN") && !mGlobalSW.contains("global");
        if (!mGlobalHW.contains(DEVICE_INFO_HW_INDIA) && !mGlobalSW.contains(DEVICE_INFO_SW_INDIA)) {
        }
        log("IndiaFlag = " + mGlobalHW.contains(DEVICE_INFO_HW_INDIA) + "  +  " + mGlobalSW.contains(DEVICE_INFO_SW_INDIA));
        log("globalHW = " + mGlobalHW + ", globalSW = " + mGlobalSW + ", mSarSensorEnabled = " + mSarSensorEnabled);
        if (mSarSensorEnabled || this.mSarTestProState) {
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
    public static int getCurrentDSI(int receiverState, int cableState) {
        log("getCurrentDSI");
        if (cableState == 1) {
            return 5;
        }
        if (receiverState != 1) {
            return 1;
        }
        return 0;
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
        public static void sendSarToModem(int currentDsi) {
            if (ModemSarControllerM12CN.sCi != null) {
                for (int i = 0; i < ModemSarControllerM12CN.sCi.length; i++) {
                    if (1 == ModemSarControllerM12CN.sCi[i].getRadioState()) {
                        ModemSarControllerM12CN.sCi[i].invokeOemRilRequestStrings(new String[]{"AT+ERFIDX=1," + currentDsi, ""}, (Message) null);
                        return;
                    }
                    ModemSarControllerM12CN.log("phone[" + i + "] radio is unavailable or off");
                }
                return;
            }
            Log.e(ModemSarControllerM12CN.LOG_TAG, "sCi[0] is NULL!!!");
        }

        private static class CmdHandler extends Handler {
            private CmdHandler() {
            }

            private void DSI_Handle(int type) {
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
                } else if (type == 13 || type == 12) {
                    ModemSarControllerM12CN.log("radio power on or SIM card loaded, send the current DSI");
                    CmdProcThread.sendSarToModem(ModemSarControllerM12CN.DSI_Current);
                } else {
                    ModemSarControllerM12CN.log("The current DSI have set, don`t need to set it again.");
                }
                ModemSarControllerM12CN.DSI_History = ModemSarControllerM12CN.DSI_Current;
                ModemSarControllerM12CN.HotspotBtPan = 0;
            }

            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                ModemSarControllerM12CN.log("handlerMessage, msg = " + msg.what);
                switch (msg.what) {
                    case 1:
                        ModemSarControllerM12CN.SarSensorState = msg.arg1;
                        ModemSarControllerM12CN.log("EVENT_SAR_SENSOR_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 2:
                        ModemSarControllerM12CN.ReceiverState = msg.arg1;
                        ModemSarControllerM12CN.log("EVENT_RECEIVER_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 3:
                        ModemSarControllerM12CN.HotspotState = msg.arg1;
                        ModemSarControllerM12CN.log("EVENT_WIFI_HOSP_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 4:
                        ModemSarControllerM12CN.WIFIState = msg.arg1;
                        ModemSarControllerM12CN.log("EVENT_WIFI_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 5:
                        ModemSarControllerM12CN.HallState = msg.arg1;
                        ModemSarControllerM12CN.log("EVENT_HALL_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 6:
                        ModemSarControllerM12CN.MccState = msg.arg1;
                        ModemSarControllerM12CN.log("EVENT_MCC_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 7:
                        ModemSarControllerM12CN.BtPanState = msg.arg1;
                        ModemSarControllerM12CN.log("EVENT_BT_PAN_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 8:
                        ModemSarControllerM12CN.SarSensor2State = msg.arg1;
                        ModemSarControllerM12CN.log("EVENT_SAR_SENSOR2_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 9:
                        ModemSarControllerM12CN.Cablestate = msg.arg1;
                        ModemSarControllerM12CN.log("EVENT_CABLE_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 12:
                        ModemSarControllerM12CN.log("EVENT_RADIO_POWER_CHANGE");
                        ModemSarControllerM12CN.RadioPowerState = msg.arg1;
                        DSI_Handle(msg.what);
                        break;
                    case 13:
                        ModemSarControllerM12CN.log("EVENT_SIMCARD_CHANGE");
                        ModemSarControllerM12CN.SIMCardState = msg.arg1;
                        DSI_Handle(msg.what);
                        break;
                }
            }
        }
    }
}
