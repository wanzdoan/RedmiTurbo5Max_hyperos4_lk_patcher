package com.xiaomi.internal.telephony;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import com.android.internal.telephony.CommandsInterface;

/* JADX INFO: loaded from: classes.dex */
public class ModemSarControllerM9CN implements DynamicSarService.SarControllerClient {
    private static final String DEVICE_INFO_HW_INDIA = "IN";
    private static final String DEVICE_INFO_SW_INDIA = "in_global";
    private static final String DEVICE_TYPE_CHOPIN = "chopin";
    private static final String DEVICE_TYPE_M9 = "yuechu";
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
    public static final String LOG_TAG = "ModemSarControllerM9CN";
    private static final String PROPERTY_DYNAMIC_SAR = "persist.vendor.radio.dynamic_sar";
    private static final String PROPERTY_GLOBAL_HW = "ro.boot.hwc";
    private static final String PROPERTY_GLOBAL_NEW_SAR = "ro.boot.product.new_sar";
    private static final String PROPERTY_GLOBAL_VER = "ro.product.mod_device";
    private static final int SAR_MODE = 1;
    private static ModemSarControllerM9CN sIntance;
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
    private static String mDeviceName = null;
    private static String mGlobalHW = null;
    private static String mGlobalSW = null;
    private static String mSarVersion = null;
    private static CommandsInterface[] sCi = null;

    public static void make(Context context, CommandsInterface[] cis) {
        sCi = cis;
        if (sIntance == null) {
            sIntance = new ModemSarControllerM9CN(context);
        }
    }

    private ModemSarControllerM9CN(Context context) {
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
        mSarVersion = SystemProperties.get(PROPERTY_GLOBAL_NEW_SAR);
        boolean mSarSensorEnabled = mGlobalHW.equals("CN") && !mGlobalSW.contains("global");
        if (!mGlobalHW.contains(DEVICE_INFO_HW_INDIA) && !mGlobalSW.contains(DEVICE_INFO_SW_INDIA)) {
        }
        boolean mNewSarFlag = mSarVersion.equals("SARSensor");
        log("IndiaFlag = " + mGlobalHW.contains(DEVICE_INFO_HW_INDIA) + "  +  " + mGlobalSW.contains(DEVICE_INFO_SW_INDIA));
        log("globalHW = " + mGlobalHW + ", globalSW = " + mGlobalSW + ", mSarSensorEnabled = " + mSarSensorEnabled + ", mNewSarFlag = " + mNewSarFlag);
        if (mSarSensorEnabled || this.mSarTestProState) {
            log("DSI_Hash_Init_CN_M9");
            this.mService.registerStateChangeListener(5, this);
            this.mService.registerStateChangeListener(13, this);
            this.mService.registerStateChangeListener(14, this);
            if (mNewSarFlag) {
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

    /* JADX INFO: Access modifiers changed from: private */
    public static int newGetCurrentDSI(int receiverState, int cableState, int sarSensorState, int sarSensor2State) {
        log("newGetCurrentDSI");
        if (cableState == 1) {
            return 5;
        }
        if (receiverState == 1) {
            return 0;
        }
        if (sarSensorState == 0 && sarSensor2State == 0) {
            return 3;
        }
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getCurrentDSIStr(int receiverState, int cableState, int sarSensorState, int sarSensor2State) {
        log("getCurrentDSIStr");
        for (int i = 0; i < DSI_ANT.length; i++) {
            DSI_ANT[i] = 3;
        }
        if (cableState == 1) {
            int[] iArr = DSI_ANT;
            int[] iArr2 = DSI_ANT;
            int[] iArr3 = DSI_ANT;
            int[] iArr4 = DSI_ANT;
            int[] iArr5 = DSI_ANT;
            int[] iArr6 = DSI_ANT;
            int[] iArr7 = DSI_ANT;
            DSI_ANT[7] = 5;
            iArr7[6] = 5;
            iArr6[5] = 5;
            iArr5[4] = 5;
            iArr4[3] = 5;
            iArr3[2] = 5;
            iArr2[1] = 5;
            iArr[0] = 5;
        } else if (receiverState == 1) {
            int[] iArr8 = DSI_ANT;
            int[] iArr9 = DSI_ANT;
            int[] iArr10 = DSI_ANT;
            int[] iArr11 = DSI_ANT;
            int[] iArr12 = DSI_ANT;
            int[] iArr13 = DSI_ANT;
            int[] iArr14 = DSI_ANT;
            DSI_ANT[7] = 0;
            iArr14[6] = 0;
            iArr13[5] = 0;
            iArr12[4] = 0;
            iArr11[3] = 0;
            iArr10[2] = 0;
            iArr9[1] = 0;
            iArr8[0] = 0;
        } else {
            if (sarSensorState != 0) {
                int[] iArr15 = DSI_ANT;
                DSI_ANT[2] = 1;
                iArr15[0] = 1;
            }
            if (sarSensor2State != 0) {
                int[] iArr16 = DSI_ANT;
                int[] iArr17 = DSI_ANT;
                int[] iArr18 = DSI_ANT;
                DSI_ANT[7] = 1;
                iArr18[4] = 1;
                iArr17[3] = 1;
                iArr16[1] = 1;
            }
        }
        StringBuffer dsi_cmd_sb = new StringBuffer("AT+EGMC=1,\"sar_scenario_all\"");
        for (int j = 0; j < DSI_ANT.length; j++) {
            log("DSI_ANT[" + j + "] = " + DSI_ANT[j]);
            dsi_cmd_sb.append(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE);
            dsi_cmd_sb.append(DSI_ANT[j]);
        }
        String dsi_tempstr = dsi_cmd_sb.toString();
        log("dsi_tempstr = " + dsi_tempstr);
        return dsi_tempstr;
    }

    private void sendMsgToHandler(int message, int arg1, int arg2) {
        log("sendMsgToHandler, Message = " + message + ", arg1 = " + arg1 + ", arg2 = " + arg2);
        Handler handler = this.mCmdProc.getCmdHandler();
        Message msg = handler.obtainMessage(message, arg1, arg2);
        handler.sendMessage(msg);
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
        public static void sendSarToModem(int currentDsi) {
            if (ModemSarControllerM9CN.sCi != null) {
                for (int i = 0; i < ModemSarControllerM9CN.sCi.length; i++) {
                    if (1 == ModemSarControllerM9CN.sCi[i].getRadioState()) {
                        ModemSarControllerM9CN.sCi[i].invokeOemRilRequestStrings(new String[]{"AT+ERFIDX=1," + currentDsi, ""}, (Message) null);
                        return;
                    }
                    ModemSarControllerM9CN.log("phone[" + i + "] radio is unavailable or off");
                }
                return;
            }
            Log.e(ModemSarControllerM9CN.LOG_TAG, "sCi[0] is NULL!!!");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void newSendSarToModem(String DSI_str) {
            if (ModemSarControllerM9CN.sCi != null) {
                for (int i = 0; i < ModemSarControllerM9CN.sCi.length; i++) {
                    if (1 == ModemSarControllerM9CN.sCi[i].getRadioState()) {
                        ModemSarControllerM9CN.sCi[i].invokeOemRilRequestStrings(new String[]{DSI_str, ""}, (Message) null);
                        return;
                    }
                    ModemSarControllerM9CN.log("phone[" + i + "] radio is unavailable or off");
                }
                return;
            }
            Log.e(ModemSarControllerM9CN.LOG_TAG, "sCi[0] is NULL!!!");
        }

        private static class CmdHandler extends Handler {
            private CmdHandler() {
            }

            private void DSI_Handle(int type) {
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
                boolean mNewSarFlag = ModemSarControllerM9CN.mSarVersion.equals("SARSensor");
                if (mNewSarFlag) {
                    ModemSarControllerM9CN.DSI_Current = ModemSarControllerM9CN.newGetCurrentDSI(ModemSarControllerM9CN.ReceiverState, ModemSarControllerM9CN.Cablestate, ModemSarControllerM9CN.SarSensorState, ModemSarControllerM9CN.SarSensor2State);
                    ModemSarControllerM9CN.log("DSI_Current is = " + ModemSarControllerM9CN.DSI_Current);
                    String DSI_str = ModemSarControllerM9CN.getCurrentDSIStr(ModemSarControllerM9CN.ReceiverState, ModemSarControllerM9CN.Cablestate, ModemSarControllerM9CN.SarSensorState, ModemSarControllerM9CN.SarSensor2State);
                    if (ModemSarControllerM9CN.DSI_Current == 1 || ModemSarControllerM9CN.DSI_Current != ModemSarControllerM9CN.DSI_History) {
                        CmdProcThread.newSendSarToModem(DSI_str);
                    } else if (type == 13 || type == 12) {
                        ModemSarControllerM9CN.log("radio power on or SIM card loaded, send the current DSI");
                        CmdProcThread.newSendSarToModem(DSI_str);
                    } else {
                        ModemSarControllerM9CN.log("The current DSI have set, don`t need to set it again.");
                    }
                } else {
                    ModemSarControllerM9CN.DSI_Current = ModemSarControllerM9CN.getCurrentDSI(ModemSarControllerM9CN.ReceiverState, ModemSarControllerM9CN.Cablestate);
                    ModemSarControllerM9CN.log("DSI_Current is = " + ModemSarControllerM9CN.DSI_Current);
                    if (ModemSarControllerM9CN.DSI_Current >= 0 && ModemSarControllerM9CN.DSI_Current != ModemSarControllerM9CN.DSI_History) {
                        CmdProcThread.sendSarToModem(ModemSarControllerM9CN.DSI_Current);
                    } else if (type == 13 || type == 12) {
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
            public void handleMessage(Message msg) {
                ModemSarControllerM9CN.log("handlerMessage, msg = " + msg.what);
                switch (msg.what) {
                    case 1:
                        ModemSarControllerM9CN.SarSensorState = msg.arg1;
                        ModemSarControllerM9CN.log("EVENT_SAR_SENSOR_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 2:
                        ModemSarControllerM9CN.ReceiverState = msg.arg1;
                        ModemSarControllerM9CN.log("EVENT_RECEIVER_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 3:
                        ModemSarControllerM9CN.HotspotState = msg.arg1;
                        ModemSarControllerM9CN.log("EVENT_WIFI_HOSP_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 4:
                        ModemSarControllerM9CN.WIFIState = msg.arg1;
                        ModemSarControllerM9CN.log("EVENT_WIFI_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 5:
                        ModemSarControllerM9CN.HallState = msg.arg1;
                        ModemSarControllerM9CN.log("EVENT_HALL_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 6:
                        ModemSarControllerM9CN.MccState = msg.arg1;
                        ModemSarControllerM9CN.log("EVENT_MCC_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 7:
                        ModemSarControllerM9CN.BtPanState = msg.arg1;
                        ModemSarControllerM9CN.log("EVENT_BT_PAN_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 8:
                        ModemSarControllerM9CN.SarSensor2State = msg.arg1;
                        ModemSarControllerM9CN.log("EVENT_SAR_SENSOR2_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 9:
                        ModemSarControllerM9CN.Cablestate = msg.arg1;
                        ModemSarControllerM9CN.log("EVENT_CABLE_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 12:
                        ModemSarControllerM9CN.log("EVENT_RADIO_POWER_CHANGE");
                        ModemSarControllerM9CN.RadioPowerState = msg.arg1;
                        DSI_Handle(msg.what);
                        break;
                    case 13:
                        ModemSarControllerM9CN.log("EVENT_SIMCARD_CHANGE");
                        ModemSarControllerM9CN.SIMCardState = msg.arg1;
                        DSI_Handle(msg.what);
                        break;
                }
            }
        }
    }
}
