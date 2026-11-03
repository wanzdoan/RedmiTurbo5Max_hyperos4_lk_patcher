package com.xiaomi.internal.telephony;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Looper;
import android.os.Message;
import com.xiaomi.modem.ModemUtils;
import com.xiaomi.modem.OemHookAgent;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/* JADX INFO: loaded from: classes.dex */
public class MtkTaSarController extends ModemSarController {
    private static final int DSI_DEFAULT_VALUE = 4;
    private static final String LOG_TAG = "MtkTRadController";
    private static MtkTaSarController sIntance;

    public static void make(Context context, OemHookAgent oemHookAgent, DeviceSarConfig deviceSarConfig) {
        ModemSarLog.i(LOG_TAG, "make, context: " + context + ", hookAgent: " + oemHookAgent + ", deviceName: " + ModemUtils.getDeviceName());
        if (sIntance == null) {
            sIntance = new MtkTaSarController(context, oemHookAgent, deviceSarConfig);
        }
    }

    private MtkTaSarController(Context context, OemHookAgent oemHookAgent, DeviceSarConfig deviceSarConfig) {
        super(context, oemHookAgent, deviceSarConfig);
        startModemSarProcess();
    }

    @Override // com.xiaomi.internal.telephony.ModemSarController
    protected void startModemSarProcess() {
        if (ModemSarController.mCurrentSarConfig != null) {
            this.mCmdProcHandler = new MtkCmdHandler(this.mCmdProcThread.getLooper());
            sendMsgToHandler(100);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void sendDsiToModem(int i) {
        ModemSarLog.d(LOG_TAG, "Send index to modem, index = " + i);
        ModemSarController.mHookAgent.onMtkSendTASarDsiAtCommand(i);
    }

    public class MtkCmdHandler extends ModemSarController.CmdHandler {
        public MtkCmdHandler(Looper looper) {
            super(looper);
            ModemSarLog.d(MtkTaSarController.LOG_TAG, "MtkCmdHandler Constructor init done");
        }

        @Override // com.xiaomi.internal.telephony.ModemSarController.CmdHandler
        public void dsiHandle() {
            if (ModemSarController.mHookAgent == null) {
                ModemSarLog.e(MtkTaSarController.LOG_TAG, "mHookAgent is null!!!");
                return;
            }
            ModemSarLog.d(MtkTaSarController.LOG_TAG, "Sensor1State is = " + ModemSarController.mSarSensor1State + ", ReceiverState is = " + ModemSarController.mReceiverState + ", WifiHotSpotState is = " + ModemSarController.mWifiHotSpotState + ", Bluetooth state is = " + ModemSarController.mBtState + ", Bluetooth pan state is = " + ModemSarController.mBtPanState + ", MCC state is = " + ModemSarController.mMccState + ", WifiState is = " + ModemSarController.mWifiState + ", HallState is = " + ModemSarController.mHallState + ", Hall2State is = " + ModemSarController.mHall2State + ", Sensor2State is = " + ModemSarController.mSarSensor2State + ", CableState is = " + ModemSarController.mCableState + ", RadioPowerState is = " + ModemSarController.mRadioPowerState + ", SIMCardState is = " + ModemSarController.mSIMCardState + ", SmartEnvState is = " + ModemSarController.mSmartTxEnvState + ", whiteListState is = " + ModemSarController.mWhiteListState + ", LandScapeGameState is = " + ModemSarController.mLandScapeGameState);
            if (ModemSarController.mBtState == 0) {
                ModemSarController.mBtPanState = 0;
            }
            if (ModemSarController.mWifiHotSpotState == 1 || ModemSarController.mBtPanState == 1) {
                ModemSarController.mHotspotState = 1;
            }
            int dsiValue = ModemSarController.getDsiValue(4);
            ModemSarLog.d(MtkTaSarController.LOG_TAG, "currentIndex is = " + dsiValue);
            if (dsiValue >= 0 && (dsiValue != ModemSarController.mCurrentDsi || this.mIsInitDsi)) {
                MtkTaSarController.sendDsiToModem(dsiValue);
                ModemSarController.saveAndBroadcastDsiChangeEvent(dsiValue);
                ModemSarController.mCurrentDsi = dsiValue;
            } else {
                ModemSarLog.d(MtkTaSarController.LOG_TAG, "The current index have set, don`t need to set it again.");
            }
            ModemSarController.mHotspotState = 0;
            this.mIsInitDsi = false;
        }

        @Override // com.xiaomi.internal.telephony.ModemSarController.CmdHandler, android.os.Handler
        public void handleMessage(Message message) {
            ModemSarLog.d(MtkTaSarController.LOG_TAG, "handlerMessage, msg = " + message.what);
            byte[] bArr = (byte[]) ((AsyncResult) message.obj).result;
            ByteBuffer buffer = ModemUtils.getBuffer(bArr);
            if (buffer == null) {
                ModemSarLog.d(MtkTaSarController.LOG_TAG, "handlerMessage, buffer is null, do nothing");
                return;
            }
            int i = message.what;
            if (i != 100) {
                switch (i) {
                    case 1:
                        ModemSarController.mReceiverState = buffer.getInt();
                        ModemSarLog.d(MtkTaSarController.LOG_TAG, "EVENT_RECEIVER_CHANGE, ReceiverState = " + ModemSarController.mReceiverState);
                        dsiHandle();
                        break;
                    case 2:
                        ModemSarController.mWifiState = buffer.getInt();
                        ModemSarLog.d(MtkTaSarController.LOG_TAG, "EVENT_WIFI_CHANGE, WifiState = " + ModemSarController.mWifiState);
                        dsiHandle();
                        break;
                    case 3:
                        ModemSarController.mWifiHotSpotState = buffer.getInt();
                        ModemSarLog.d(MtkTaSarController.LOG_TAG, "EVENT_WIFI_HOSP_CHANGE, WifiHotSpotState = " + ModemSarController.mWifiHotSpotState);
                        dsiHandle();
                        break;
                    case 4:
                        ModemSarController.mBtState = buffer.getInt();
                        ModemSarLog.d(MtkTaSarController.LOG_TAG, "EVENT_BT_CHANGE, BtState = " + ModemSarController.mBtState);
                        dsiHandle();
                        break;
                    case 5:
                        ModemSarController.mBtPanState = buffer.getInt();
                        ModemSarLog.d(MtkTaSarController.LOG_TAG, "EVENT_BT_PAN_CHANGE, BtPanState = " + ModemSarController.mBtPanState);
                        dsiHandle();
                        break;
                    case 6:
                        ModemSarController.mSarSensor1State = ModemSarController.sarExceptionHandle(buffer.getInt(), 1);
                        ModemSarLog.d(MtkTaSarController.LOG_TAG, "EVENT_SENSOR1_CHANGE, Sensor1State = " + ModemSarController.mSarSensor1State);
                        dsiHandle();
                        break;
                    case 7:
                        ModemSarController.mSarSensor2State = ModemSarController.sarExceptionHandle(buffer.getInt(), 1);
                        ModemSarLog.d(MtkTaSarController.LOG_TAG, "EVENT_SENSOR2_CHANGE, Sensor2State = " + ModemSarController.mSarSensor2State);
                        dsiHandle();
                        break;
                    case 8:
                        ModemSarController.mMccState = buffer.getInt();
                        ModemSarLog.d(MtkTaSarController.LOG_TAG, "EVENT_MCC_CHANGE, MccState = " + ModemSarController.mMccState);
                        dsiHandle();
                        break;
                    case 9:
                        ModemSarController.mCableState = buffer.getInt();
                        ModemSarLog.d(MtkTaSarController.LOG_TAG, "EVENT_CABLE_CHANGE, CableState = " + ModemSarController.mCableState);
                        dsiHandle();
                        break;
                    case 10:
                        ModemSarController.mHallState = buffer.getInt();
                        ModemSarLog.d(MtkTaSarController.LOG_TAG, "EVENT_HALL_CHANGE, HallState = " + ModemSarController.mHallState);
                        dsiHandle();
                        break;
                    case 11:
                        ModemSarController.mUsbState = buffer.getInt();
                        ModemSarLog.d(MtkTaSarController.LOG_TAG, "EVENT_USB_CHANGE, UsbState = " + ModemSarController.mUsbState);
                        dsiHandle();
                        break;
                    case 12:
                    case 13:
                        break;
                    case 14:
                        ModemSarController.mSmartTxEnvState = buffer.getInt();
                        ModemSarLog.d(MtkTaSarController.LOG_TAG, "EVENT_SMART_ENV_CHANGE, state = " + ModemSarController.mSmartTxEnvState);
                        dsiHandle();
                        break;
                    case 15:
                        ModemSarController.mWhiteListState = buffer.getInt();
                        ModemSarLog.d(MtkTaSarController.LOG_TAG, "EVENT_WHILTE_LIST_CHANGE, state = " + ModemSarController.mWhiteListState);
                        dsiHandle();
                        break;
                    case 16:
                        ModemSarController.mHall2State = buffer.getInt();
                        ModemSarLog.d(MtkTaSarController.LOG_TAG, "EVENT_HALL2_CHANGE, Hall2State = " + ModemSarController.mHall2State);
                        dsiHandle();
                        break;
                    case 17:
                        ModemSarController.mSarSensor3State = ModemSarController.sarExceptionHandle(buffer.getInt(), 1);
                        ModemSarLog.d(MtkTaSarController.LOG_TAG, "EVENT_SENSOR3_CHANGE, Sensor3State = " + ModemSarController.mSarSensor3State);
                        dsiHandle();
                        break;
                    default:
                        switch (i) {
                            case 19:
                                ModemSarController.mScreenState = buffer.getInt();
                                ModemSarLog.d(MtkTaSarController.LOG_TAG, "EVENT_SCREEN_STATE_CHANGE, state = " + ModemSarController.mScreenState);
                                if (ModemSarController.isValidListener(17)) {
                                    int i2 = ModemSarController.mScreenState;
                                    if (i2 == 0) {
                                        ModemSarController.mLandScapeGameState = 0;
                                        SarDeviceStateListener.unregisterDeviceStateChangeEvent(25, this);
                                    } else if (i2 == 1) {
                                        SarDeviceStateListener.registerDeviceStateChangeEvent(ModemSarController.mContext, 25, this, 20, null);
                                    }
                                }
                                dsiHandle();
                                break;
                            case 20:
                                ModemSarController.mLandScapeGameState = buffer.getInt();
                                ModemSarLog.d(MtkTaSarController.LOG_TAG, "EVENT_LANDSCAPE_GAME_STATE_CHANGE, state = " + ModemSarController.mLandScapeGameState);
                                dsiHandle();
                                break;
                            case 21:
                                ModemSarController.mSimMccState = ModemSarController.getSimMccMatchState(new String(bArr, StandardCharsets.UTF_8));
                                ModemSarLog.d(MtkTaSarController.LOG_TAG, "EVENT_SIM_MCC_STATE_CHANGE, state = " + ModemSarController.mSimMccState);
                                dsiHandle();
                                break;
                            case 22:
                                ModemSarController.mHorizontalGameState = buffer.getInt();
                                ModemSarLog.d(MtkTaSarController.LOG_TAG, "EVENT_HORIZONTAL_GAME_STATE_CHANGE, state = " + ModemSarController.mHorizontalGameState);
                                dsiHandle();
                                break;
                            case 23:
                                ModemSarController.mHstEnhancementState = buffer.getInt();
                                ModemSarLog.d(MtkTaSarController.LOG_TAG, "EVENT_HST_ENHANCEMENT_STATE_CHANGE, state = " + ModemSarController.mHstEnhancementState);
                                dsiHandle();
                                break;
                        }
                        break;
                }
                return;
            }
            this.mIsInitDsi = true;
            ModemSarLog.d(MtkTaSarController.LOG_TAG, "Need to resend the current DSI to modem.");
            dsiHandle();
        }
    }
}
