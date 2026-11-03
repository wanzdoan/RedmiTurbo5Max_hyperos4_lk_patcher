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
public class ModemSarControllerL12A implements DynamicSarService.SarControllerClient {
    private static int BtPanState = 0;
    private static int Cablestate = 0;
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
    public static final String LOG_TAG = "ModemSarControllerL12A";
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
    private static ModemSarControllerL12A sIntance;
    CmdProcThread mCmdProc;
    private boolean mSarTestProState;
    private DynamicSarService mService;

    public static void make(Context context, CommandsInterface[] commandsInterfaceArr) {
        sCi = commandsInterfaceArr;
        if (sIntance == null) {
            sIntance = new ModemSarControllerL12A(context);
        }
    }

    private ModemSarControllerL12A(Context context) {
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
        String str = SystemProperties.get("ro.product.mod_device");
        mGlobalSW = str;
        if (str.contains("_pre")) {
            mGlobalSW = mGlobalSW.replace("_pre", "");
        }
        boolean z = mGlobalHW.equals(l.a) && !mGlobalSW.contains("global");
        log("globalHW = " + mGlobalHW + ", globalSW = " + mGlobalSW + ", mSarSensorEnabled = true");
        if (!z) {
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
        if (z) {
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
    public static int getCurrentDSI(int i, int i2, int i3, int i4, int i5) {
        log("getCurrentDSI");
        if (i3 == 1) {
            return 5;
        }
        if (i2 == 1) {
            return 4;
        }
        return i == 1 ? 0 : 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getCurrentDSIStr(int i, int i2, int i3, int i4, int i5) {
        int[] iArr;
        log("getCurrentDSIStr");
        int i6 = 0;
        while (true) {
            iArr = DSI_ANT;
            if (i6 >= iArr.length) {
                break;
            }
            iArr[i6] = 3;
            i6++;
        }
        if (i3 == 1) {
            iArr[7] = 5;
            iArr[6] = 5;
            iArr[5] = 5;
            iArr[4] = 5;
            iArr[3] = 5;
            iArr[2] = 5;
            iArr[1] = 5;
            iArr[0] = 5;
        } else if (i2 == 1) {
            iArr[7] = 4;
            iArr[6] = 4;
            iArr[5] = 4;
            iArr[4] = 4;
            iArr[3] = 4;
            iArr[2] = 4;
            iArr[1] = 4;
            iArr[0] = 4;
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
            if (SarSensorState == 0) {
                iArr[2] = 3;
                iArr[0] = 3;
            } else {
                iArr[2] = 1;
                iArr[0] = 1;
            }
            if (SarSensor2State == 0) {
                iArr[7] = 3;
                iArr[5] = 3;
                iArr[4] = 3;
                iArr[3] = 3;
                iArr[1] = 3;
            } else {
                iArr[7] = 1;
                iArr[5] = 1;
                iArr[4] = 1;
                iArr[3] = 1;
                iArr[1] = 1;
            }
        }
        StringBuffer stringBuffer = new StringBuffer("AT+EGMC=1,\"sar_scenario_all\"");
        for (int i7 = 0; i7 < DSI_ANT.length; i7++) {
            log("DSI_ANT[" + i7 + "] = " + DSI_ANT[i7]);
            stringBuffer.append(",");
            stringBuffer.append(DSI_ANT[i7]);
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
        public static void sendSarToModem(String str) {
            if (ModemSarControllerL12A.sCi != null) {
                for (int i = 0; i < ModemSarControllerL12A.sCi.length; i++) {
                    if (1 == ModemSarControllerL12A.sCi[i].getRadioState()) {
                        ModemSarControllerL12A.sCi[i].invokeOemRilRequestStrings(new String[]{str, ""}, (Message) null);
                        return;
                    }
                    ModemSarControllerL12A.log("phone[" + i + "] radio is unavailable or off");
                }
                return;
            }
            Log.e(ModemSarControllerL12A.LOG_TAG, "sCi[0] is NULL!!!");
        }

        class CmdHandler extends Handler {
            private CmdHandler() {
            }

            private void DSI_Handle(int i) {
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
                String currentDSIStr = ModemSarControllerL12A.getCurrentDSIStr(ModemSarControllerL12A.ReceiverState, ModemSarControllerL12A.HotspotBtPan, ModemSarControllerL12A.Cablestate, ModemSarControllerL12A.SarSensorState, ModemSarControllerL12A.SarSensor2State);
                if (ModemSarControllerL12A.DSI_Current == 3 || ModemSarControllerL12A.DSI_Current != ModemSarControllerL12A.DSI_History) {
                    CmdProcThread.sendSarToModem(currentDSIStr);
                } else if (i == 13 || i == 12) {
                    ModemSarControllerL12A.log("radio power on or SIM card loaded, send the current DSI");
                    CmdProcThread.sendSarToModem(currentDSIStr);
                } else {
                    ModemSarControllerL12A.log("The current DSI have set, don`t need to set it again.");
                }
                ModemSarControllerL12A.DSI_History = ModemSarControllerL12A.DSI_Current;
                ModemSarControllerL12A.HotspotBtPan = 0;
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                ModemSarControllerL12A.log("handlerMessage, msg = " + message.what);
                switch (message.what) {
                    case 1:
                        ModemSarControllerL12A.SarSensorState = message.arg1;
                        ModemSarControllerL12A.log("EVENT_SAR_SENSOR_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 2:
                        ModemSarControllerL12A.ReceiverState = message.arg1;
                        ModemSarControllerL12A.log("EVENT_RECEIVER_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 3:
                        ModemSarControllerL12A.HotspotState = message.arg1;
                        ModemSarControllerL12A.log("EVENT_WIFI_HOSP_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 4:
                        ModemSarControllerL12A.WIFIState = message.arg1;
                        ModemSarControllerL12A.log("EVENT_WIFI_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 5:
                        ModemSarControllerL12A.HallState = message.arg1;
                        ModemSarControllerL12A.log("EVENT_HALL_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 6:
                        ModemSarControllerL12A.MccState = message.arg1;
                        ModemSarControllerL12A.log("EVENT_MCC_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 7:
                        ModemSarControllerL12A.BtPanState = message.arg1;
                        ModemSarControllerL12A.log("EVENT_BT_PAN_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 8:
                        ModemSarControllerL12A.SarSensor2State = message.arg1;
                        ModemSarControllerL12A.log("EVENT_SAR_SENSOR2_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 9:
                        ModemSarControllerL12A.Cablestate = message.arg1;
                        ModemSarControllerL12A.log("EVENT_CABLE_CHANGE");
                        DSI_Handle(message.what);
                        break;
                    case 12:
                        ModemSarControllerL12A.log("EVENT_RADIO_POWER_CHANGE");
                        ModemSarControllerL12A.RadioPowerState = message.arg1;
                        DSI_Handle(message.what);
                        break;
                    case 13:
                        ModemSarControllerL12A.log("EVENT_SIMCARD_CHANGE");
                        ModemSarControllerL12A.SIMCardState = message.arg1;
                        DSI_Handle(message.what);
                        break;
                }
            }
        }
    }
}
