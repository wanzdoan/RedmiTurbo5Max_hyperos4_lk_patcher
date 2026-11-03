package com.xiaomi.internal.telephony;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import com.android.internal.telephony.CommandsInterface;
import com.xiaomi.modem.ModemUtils;

/* JADX INFO: loaded from: classes.dex */
public class ModemSarControllerL12A implements DynamicSarService.SarControllerClient {
    private static final String DEVICE_INFO_HW_INDIA = "IN";
    private static final String DEVICE_INFO_SW_INDIA = "in_global";
    private static final String DEVICE_TYPE_ATOM = "atom";
    private static final String DEVICE_TYPE_BOMB = "bomb";
    private static final String DEVICE_TYPE_CEZANNE = "cezanne";
    private static final String DEVICE_TYPE_CHOPIN = "chopin";
    private static final String DEVICE_TYPE_K10A = "chopin";
    private static final String DEVICE_TYPE_K16 = "pissarro";
    private static final String DEVICE_TYPE_L12A = "plato";
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
    public static final String LOG_TAG = "ModemSarControllerL12A";
    private static final String PROPERTY_DYNAMIC_SAR = "persist.vendor.radio.dynamic_sar";
    private static final String PROPERTY_GLOBAL_HW = "ro.boot.hwc";
    private static final String PROPERTY_GLOBAL_VER = "ro.product.mod_device";
    private static final int SAR_MODE = 1;
    private static ModemSarControllerL12A sIntance;
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
            sIntance = new ModemSarControllerL12A(context);
        }
    }

    private ModemSarControllerL12A(Context context) {
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
        if (mGlobalSW.contains(ModemUtils.PROP_DEVICE_MOD_NAME_SUFFIX_PRE)) {
            mGlobalSW = mGlobalSW.replace(ModemUtils.PROP_DEVICE_MOD_NAME_SUFFIX_PRE, "");
        }
        boolean CNFlag = mGlobalHW.equals("CN") && !mGlobalSW.contains("global");
        log("globalHW = " + mGlobalHW + ", globalSW = " + mGlobalSW + ", mSarSensorEnabled = true");
        if ((1 != 0 || this.mSarTestProState) && !CNFlag) {
            log("DSI_Hash_Init_GL_L12A");
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
            log("No need to listen L12A GL. SAR is no need!");
        }
        if ((1 != 0 || this.mSarTestProState) && CNFlag) {
            log("DSI_Hash_Init_CN_L12A");
            this.mService.registerStateChangeListener(3, this);
            this.mService.registerStateChangeListener(5, this);
            this.mService.registerStateChangeListener(9, this);
            this.mService.registerStateChangeListener(13, this);
            this.mService.registerStateChangeListener(14, this);
            return;
        }
        log("No need to listen L12A CN. SAR is no need!");
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
    public static int getCurrentDSI(int receiverState, int hotspotState, int cableState, int sarSensorState, int sarSensor2State) {
        log("getCurrentDSI");
        if (cableState == 1) {
            return 5;
        }
        if (hotspotState == 1) {
            return 4;
        }
        if (receiverState != 1) {
            return 3;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getCurrentDSIStr(int receiverState, int hotspotState, int cableState, int sarSensorState, int sarSensor2State) {
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
        } else if (hotspotState == 1) {
            int[] iArr8 = DSI_ANT;
            int[] iArr9 = DSI_ANT;
            int[] iArr10 = DSI_ANT;
            int[] iArr11 = DSI_ANT;
            int[] iArr12 = DSI_ANT;
            int[] iArr13 = DSI_ANT;
            int[] iArr14 = DSI_ANT;
            DSI_ANT[7] = 4;
            iArr14[6] = 4;
            iArr13[5] = 4;
            iArr12[4] = 4;
            iArr11[3] = 4;
            iArr10[2] = 4;
            iArr9[1] = 4;
            iArr8[0] = 4;
        } else if (receiverState == 1) {
            int[] iArr15 = DSI_ANT;
            int[] iArr16 = DSI_ANT;
            int[] iArr17 = DSI_ANT;
            int[] iArr18 = DSI_ANT;
            int[] iArr19 = DSI_ANT;
            int[] iArr20 = DSI_ANT;
            int[] iArr21 = DSI_ANT;
            DSI_ANT[7] = 0;
            iArr21[6] = 0;
            iArr20[5] = 0;
            iArr19[4] = 0;
            iArr18[3] = 0;
            iArr17[2] = 0;
            iArr16[1] = 0;
            iArr15[0] = 0;
        } else {
            if (SarSensorState == 0) {
                int[] iArr22 = DSI_ANT;
                DSI_ANT[2] = 3;
                iArr22[0] = 3;
            } else {
                int[] iArr23 = DSI_ANT;
                DSI_ANT[2] = 1;
                iArr23[0] = 1;
            }
            if (SarSensor2State == 0) {
                int[] iArr24 = DSI_ANT;
                int[] iArr25 = DSI_ANT;
                int[] iArr26 = DSI_ANT;
                int[] iArr27 = DSI_ANT;
                DSI_ANT[7] = 3;
                iArr27[5] = 3;
                iArr26[4] = 3;
                iArr25[3] = 3;
                iArr24[1] = 3;
            } else {
                int[] iArr28 = DSI_ANT;
                int[] iArr29 = DSI_ANT;
                int[] iArr30 = DSI_ANT;
                int[] iArr31 = DSI_ANT;
                DSI_ANT[7] = 1;
                iArr31[5] = 1;
                iArr30[4] = 1;
                iArr29[3] = 1;
                iArr28[1] = 1;
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
                Log.e(ModemSarControllerL12A.LOG_TAG, "getCmdHandler, handler is NULL!!!");
            }
            return this.mHandler;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Looper.prepare();
            this.mHandler = new CmdHandler();
            ModemSarControllerL12A.log("CmdProcThread, thread is running up!!!!");
            Looper.loop();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void sendSarToModem(String DSI_str) {
            if (ModemSarControllerL12A.sCi != null) {
                for (int i = 0; i < ModemSarControllerL12A.sCi.length; i++) {
                    if (1 == ModemSarControllerL12A.sCi[i].getRadioState()) {
                        ModemSarControllerL12A.sCi[i].invokeOemRilRequestStrings(new String[]{DSI_str, ""}, (Message) null);
                        return;
                    }
                    ModemSarControllerL12A.log("phone[" + i + "] radio is unavailable or off");
                }
                return;
            }
            Log.e(ModemSarControllerL12A.LOG_TAG, "sCi[0] is NULL!!!");
        }

        private static class CmdHandler extends Handler {
            private CmdHandler() {
            }

            private void DSI_Handle(int type) {
                ModemSarControllerL12A.log("hallState is = " + ModemSarControllerL12A.HallState);
                ModemSarControllerL12A.log("WIFIState is = " + ModemSarControllerL12A.WIFIState);
                ModemSarControllerL12A.log("MCC state is = " + ModemSarControllerL12A.MccState);
                ModemSarControllerL12A.log("Bluetooth pan state is = " + ModemSarControllerL12A.BtPanState);
                ModemSarControllerL12A.log("------------------------------------------");
                ModemSarControllerL12A.log("ReceiverState is = " + ModemSarControllerL12A.ReceiverState);
                ModemSarControllerL12A.log("HotspotState is = " + ModemSarControllerL12A.HotspotState);
                ModemSarControllerL12A.log("SarSensorState is = " + ModemSarControllerL12A.SarSensorState);
                ModemSarControllerL12A.log("SarSensor2State is = " + ModemSarControllerL12A.SarSensor2State);
                ModemSarControllerL12A.log("Cablestate is = " + ModemSarControllerL12A.Cablestate);
                ModemSarControllerL12A.log("RadioPowerState is = " + ModemSarControllerL12A.RadioPowerState);
                ModemSarControllerL12A.log("SIMCardState is = " + ModemSarControllerL12A.SIMCardState);
                if (ModemSarControllerL12A.HotspotState == 1 || ModemSarControllerL12A.BtPanState == 1) {
                    ModemSarControllerL12A.HotspotBtPan = 1;
                }
                ModemSarControllerL12A.log("HotspotBtPan is = " + ModemSarControllerL12A.HotspotBtPan);
                ModemSarControllerL12A.DSI_Current = ModemSarControllerL12A.getCurrentDSI(ModemSarControllerL12A.ReceiverState, ModemSarControllerL12A.HotspotBtPan, ModemSarControllerL12A.Cablestate, ModemSarControllerL12A.SarSensorState, ModemSarControllerL12A.SarSensor2State);
                ModemSarControllerL12A.log("DSI_Current is = " + ModemSarControllerL12A.DSI_Current);
                String DSI_str = ModemSarControllerL12A.getCurrentDSIStr(ModemSarControllerL12A.ReceiverState, ModemSarControllerL12A.HotspotBtPan, ModemSarControllerL12A.Cablestate, ModemSarControllerL12A.SarSensorState, ModemSarControllerL12A.SarSensor2State);
                if (ModemSarControllerL12A.DSI_Current == 3 || ModemSarControllerL12A.DSI_Current != ModemSarControllerL12A.DSI_History) {
                    CmdProcThread.sendSarToModem(DSI_str);
                } else if (type == 13 || type == 12) {
                    ModemSarControllerL12A.log("radio power on or SIM card loaded, send the current DSI");
                    CmdProcThread.sendSarToModem(DSI_str);
                } else {
                    ModemSarControllerL12A.log("The current DSI have set, don`t need to set it again.");
                }
                ModemSarControllerL12A.DSI_History = ModemSarControllerL12A.DSI_Current;
                ModemSarControllerL12A.HotspotBtPan = 0;
            }

            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                ModemSarControllerL12A.log("handlerMessage, msg = " + msg.what);
                switch (msg.what) {
                    case 1:
                        ModemSarControllerL12A.SarSensorState = msg.arg1;
                        ModemSarControllerL12A.log("EVENT_SAR_SENSOR_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 2:
                        ModemSarControllerL12A.ReceiverState = msg.arg1;
                        ModemSarControllerL12A.log("EVENT_RECEIVER_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 3:
                        ModemSarControllerL12A.HotspotState = msg.arg1;
                        ModemSarControllerL12A.log("EVENT_WIFI_HOSP_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 4:
                        ModemSarControllerL12A.WIFIState = msg.arg1;
                        ModemSarControllerL12A.log("EVENT_WIFI_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 5:
                        ModemSarControllerL12A.HallState = msg.arg1;
                        ModemSarControllerL12A.log("EVENT_HALL_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 6:
                        ModemSarControllerL12A.MccState = msg.arg1;
                        ModemSarControllerL12A.log("EVENT_MCC_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 7:
                        ModemSarControllerL12A.BtPanState = msg.arg1;
                        ModemSarControllerL12A.log("EVENT_BT_PAN_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 8:
                        ModemSarControllerL12A.SarSensor2State = msg.arg1;
                        ModemSarControllerL12A.log("EVENT_SAR_SENSOR2_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 9:
                        ModemSarControllerL12A.Cablestate = msg.arg1;
                        ModemSarControllerL12A.log("EVENT_CABLE_CHANGE");
                        DSI_Handle(msg.what);
                        break;
                    case 12:
                        ModemSarControllerL12A.log("EVENT_RADIO_POWER_CHANGE");
                        ModemSarControllerL12A.RadioPowerState = msg.arg1;
                        DSI_Handle(msg.what);
                        break;
                    case 13:
                        ModemSarControllerL12A.log("EVENT_SIMCARD_CHANGE");
                        ModemSarControllerL12A.SIMCardState = msg.arg1;
                        DSI_Handle(msg.what);
                        break;
                }
            }
        }
    }
}
