package com.xiaomi.internal.telephony;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Looper;
import android.os.Message;
import com.xiaomi.modem.ModemUtils;
import com.xiaomi.modem.OemHookAgent;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class MtkGroupSarController extends ModemSarController {
    private static final int DEFAULT_ANTENNA_NUMS = 15;
    private static final int DSI_DEFAULT_VALUE = 3;
    private static final String LOG_TAG = "MtkGRadController";
    private static int mAntennaNums = 15;
    private static String mCurrentDsiStr = "";
    private static String mDefaultAntGroup = "";
    private static int mRxDefaultDsi = 3;
    private static int mSarSensorState;
    private static String[] mSensorAntGroup;
    private static MtkGroupSarController sIntance;

    public static void make(Context context, OemHookAgent oemHookAgent, DeviceSarConfig deviceSarConfig) {
        ModemSarLog.i(LOG_TAG, "make, context: " + context + ", hookAgent: " + oemHookAgent + ", deviceName: " + ModemUtils.getDeviceName());
        if (sIntance == null) {
            sIntance = new MtkGroupSarController(context, oemHookAgent, deviceSarConfig);
        }
    }

    private MtkGroupSarController(Context context, OemHookAgent oemHookAgent, DeviceSarConfig deviceSarConfig) {
        super(context, oemHookAgent, deviceSarConfig);
        startModemSarProcess();
    }

    @Override // com.xiaomi.internal.telephony.ModemSarController
    protected void startModemSarProcess() {
        DeviceSarConfig deviceSarConfig = ModemSarController.mCurrentSarConfig;
        if (deviceSarConfig != null) {
            mSensorAntGroup = deviceSarConfig.getAntennaGroup().split(",");
            mDefaultAntGroup = ModemSarController.mCurrentSarConfig.getDefaultGroup();
            ModemSarLog.i(LOG_TAG, "startModemProcess, mSensorAntGroup = " + Arrays.toString(mSensorAntGroup) + ", mDefaultAntGroup = " + mDefaultAntGroup);
            try {
                mAntennaNums = Integer.parseInt(ModemSarController.mCurrentSarConfig.getAntennaNums());
                mRxDefaultDsi = Integer.parseInt(ModemSarController.mCurrentSarConfig.getRxDefaultDsi());
                ModemSarLog.i(LOG_TAG, "startModemProcess, mAntennaNums = " + mAntennaNums + ", mRxDefaultIndex = " + mRxDefaultDsi);
            } catch (Exception e) {
                e.printStackTrace();
                ModemSarLog.e(LOG_TAG, "startModemProcess, set antenna nums failed");
            }
            this.mCmdProcHandler = new MtkCmdHandler(this.mCmdProcThread.getLooper());
            sendMsgToHandler(100);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void sendDsiToModem(String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            ModemSarLog.d(LOG_TAG, "dsiStringArray cannot be null or empty!");
            return;
        }
        int[] iArr = new int[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            try {
                iArr[i] = Integer.parseInt(strArr[i]);
            } catch (Exception e) {
                e.printStackTrace();
                ModemSarLog.e(LOG_TAG, "Invalid integer value in dsiStringArray at index " + i);
            }
        }
        ModemSarLog.d(LOG_TAG, "Sending index command to modem, command = " + Arrays.toString(iArr));
        ModemSarController.mHookAgent.onMtkSendGroupSarDsiAtCommand(iArr);
    }

    public class MtkCmdHandler extends ModemSarController.CmdHandler {
        public MtkCmdHandler(Looper looper) {
            super(looper);
            ModemSarLog.d(MtkGroupSarController.LOG_TAG, "MtkCmdHandler Constructor init done");
        }

        @Override // com.xiaomi.internal.telephony.ModemSarController.CmdHandler
        public void dsiHandle() {
            if (ModemSarController.mHookAgent == null) {
                ModemSarLog.e(MtkGroupSarController.LOG_TAG, "mHookAgent is null!!!");
                return;
            }
            ModemSarLog.d(MtkGroupSarController.LOG_TAG, "Sensor1State is = " + ModemSarController.mSarSensor1State + ", ReceiverState is = " + ModemSarController.mReceiverState + ", WifiHotSpotState is = " + ModemSarController.mWifiHotSpotState + ", Bluetooth state is = " + ModemSarController.mBtState + ", Bluetooth pan state is = " + ModemSarController.mBtPanState + ", MCC state is = " + ModemSarController.mMccState + ", WifiState is = " + ModemSarController.mWifiState + ", HallState is = " + ModemSarController.mHallState + ", Hall2State is = " + ModemSarController.mHall2State + ", Sensor2State is = " + ModemSarController.mSarSensor2State + ", CableState is = " + ModemSarController.mCableState + ", RadioPowerState is = " + ModemSarController.mRadioPowerState + ", SIMCardState is = " + ModemSarController.mSIMCardState + ", SmartEnvState is = " + ModemSarController.mSmartTxEnvState + ", whiteListState is = " + ModemSarController.mWhiteListState + ", LandScapeGameState is = " + ModemSarController.mLandScapeGameState);
            if (ModemSarController.mBtState == 0) {
                ModemSarController.mBtPanState = 0;
            }
            if (ModemSarController.mWifiHotSpotState == 1 || ModemSarController.mBtPanState == 1) {
                ModemSarController.mHotspotState = 1;
            }
            if (ModemSarController.mSarSensor1State == 1 || ModemSarController.mSarSensor2State == 1 || ModemSarController.mSarSensor3State == 1) {
                MtkGroupSarController.mSarSensorState = 1;
            }
            int dsiValue = MtkGroupSarController.getDsiValue(MtkGroupSarController.mRxDefaultDsi);
            String[] antGroupArray = MtkGroupSarController.getAntGroupArray(dsiValue, MtkGroupSarController.mAntennaNums, MtkGroupSarController.mSensorAntGroup, MtkGroupSarController.mDefaultAntGroup);
            String strJoin = String.join(",", antGroupArray);
            ModemSarLog.d(MtkGroupSarController.LOG_TAG, "indexHandle, str = " + strJoin);
            if (!MtkGroupSarController.mCurrentDsiStr.equals(strJoin) || this.mIsInitDsi) {
                MtkGroupSarController.sendDsiToModem(antGroupArray);
                ModemSarController.saveAndBroadcastDsiChangeEvent(dsiValue);
                MtkGroupSarController.mCurrentDsiStr = strJoin;
            }
            ModemSarController.mHotspotState = 0;
            MtkGroupSarController.mSarSensorState = 0;
            this.mIsInitDsi = false;
        }

        @Override // com.xiaomi.internal.telephony.ModemSarController.CmdHandler, android.os.Handler
        public void handleMessage(Message message) {
            byte[] bArr = (byte[]) ((AsyncResult) message.obj).result;
            ByteBuffer buffer = ModemUtils.getBuffer(bArr);
            if (buffer == null) {
                ModemSarLog.d(MtkGroupSarController.LOG_TAG, "handlerMessage, buffer is null, do nothing");
                return;
            }
            int i = message.what;
            if (i != 100) {
                switch (i) {
                    case 1:
                        ModemSarController.mReceiverState = buffer.getInt();
                        ModemSarLog.d(MtkGroupSarController.LOG_TAG, "EVENT_RECEIVER_CHANGE, ReceiverState = " + ModemSarController.mReceiverState);
                        dsiHandle();
                        break;
                    case 2:
                        ModemSarController.mWifiState = buffer.getInt();
                        ModemSarLog.d(MtkGroupSarController.LOG_TAG, "EVENT_WIFI_CHANGE, WifiState = " + ModemSarController.mWifiState);
                        dsiHandle();
                        break;
                    case 3:
                        ModemSarController.mWifiHotSpotState = buffer.getInt();
                        ModemSarLog.d(MtkGroupSarController.LOG_TAG, "EVENT_WIFI_HOSP_CHANGE, WifiHotSpotState = " + ModemSarController.mWifiHotSpotState);
                        dsiHandle();
                        break;
                    case 4:
                        ModemSarController.mBtState = buffer.getInt();
                        ModemSarLog.d(MtkGroupSarController.LOG_TAG, "EVENT_BT_CHANGE, BtState = " + ModemSarController.mBtState);
                        dsiHandle();
                        break;
                    case 5:
                        ModemSarController.mBtPanState = buffer.getInt();
                        ModemSarLog.d(MtkGroupSarController.LOG_TAG, "EVENT_BT_PAN_CHANGE, BtPanState = " + ModemSarController.mBtPanState);
                        dsiHandle();
                        break;
                    case 6:
                        ModemSarController.mSarSensor1State = buffer.getInt();
                        ModemSarLog.d(MtkGroupSarController.LOG_TAG, "EVENT_SENSOR1_CHANGE, Sensor1State = " + ModemSarController.mSarSensor1State);
                        dsiHandle();
                        break;
                    case 7:
                        ModemSarController.mSarSensor2State = buffer.getInt();
                        ModemSarLog.d(MtkGroupSarController.LOG_TAG, "EVENT_SENSOR2_CHANGE, Sensor2State = " + ModemSarController.mSarSensor2State);
                        dsiHandle();
                        break;
                    case 8:
                        ModemSarController.mMccState = buffer.getInt();
                        ModemSarLog.d(MtkGroupSarController.LOG_TAG, "EVENT_MCC_CHANGE, MccState = " + ModemSarController.mMccState);
                        dsiHandle();
                        break;
                    case 9:
                        ModemSarController.mCableState = buffer.getInt();
                        ModemSarLog.d(MtkGroupSarController.LOG_TAG, "EVENT_CABLE_CHANGE, CableState = " + ModemSarController.mCableState);
                        dsiHandle();
                        break;
                    case 10:
                        ModemSarController.mHallState = buffer.getInt();
                        ModemSarLog.d(MtkGroupSarController.LOG_TAG, "EVENT_HALL_CHANGE, HallState = " + ModemSarController.mHallState);
                        dsiHandle();
                        break;
                    case 11:
                        ModemSarController.mUsbState = buffer.getInt();
                        ModemSarLog.d(MtkGroupSarController.LOG_TAG, "EVENT_USB_CHANGE, UsbState = " + ModemSarController.mUsbState);
                        dsiHandle();
                        break;
                    case 12:
                    case 13:
                        break;
                    case 14:
                        ModemSarController.mSmartTxEnvState = buffer.getInt();
                        ModemSarLog.d(MtkGroupSarController.LOG_TAG, "EVENT_SMART_ENV_CHANGE, state = " + ModemSarController.mSmartTxEnvState);
                        dsiHandle();
                        break;
                    case 15:
                        ModemSarController.mWhiteListState = buffer.getInt();
                        ModemSarLog.d(MtkGroupSarController.LOG_TAG, "EVENT_WHILTE_LIST_CHANGE, state = " + ModemSarController.mWhiteListState);
                        dsiHandle();
                        break;
                    case 16:
                        ModemSarController.mHall2State = buffer.getInt();
                        ModemSarLog.d(MtkGroupSarController.LOG_TAG, "EVENT_HALL2_CHANGE, Hall2State = " + ModemSarController.mHall2State);
                        dsiHandle();
                        break;
                    case 17:
                        ModemSarController.mSarSensor3State = buffer.getInt();
                        ModemSarLog.d(MtkGroupSarController.LOG_TAG, "EVENT_SENSOR3_CHANGE, Sensor3State = " + ModemSarController.mSarSensor3State);
                        dsiHandle();
                        break;
                    default:
                        switch (i) {
                            case 19:
                                ModemSarController.mScreenState = buffer.getInt();
                                ModemSarLog.d(MtkGroupSarController.LOG_TAG, "EVENT_SCREEN_STATE_CHANGE, state = " + ModemSarController.mScreenState);
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
                                ModemSarLog.d(MtkGroupSarController.LOG_TAG, "EVENT_LANDSCAPE_GAME_STATE_CHANGE, state = " + ModemSarController.mLandScapeGameState);
                                dsiHandle();
                                break;
                            case 21:
                                ModemSarController.mSimMccState = ModemSarController.getSimMccMatchState(new String(bArr, StandardCharsets.UTF_8));
                                ModemSarLog.d(MtkGroupSarController.LOG_TAG, "EVENT_SIM_MCC_STATE_CHANGE, state = " + ModemSarController.mSimMccState);
                                dsiHandle();
                                break;
                        }
                        break;
                }
                return;
            }
            this.mIsInitDsi = true;
            ModemSarLog.d(MtkGroupSarController.LOG_TAG, "Need to resend the current DSI to modem.");
            dsiHandle();
        }
    }

    protected static int getDsiValue(int i) {
        if (ModemSarController.mRegStateList.size() == 0) {
            return 0;
        }
        String str = "";
        boolean z = false;
        boolean z2 = false;
        for (int i2 = 0; i2 < ModemSarController.mRegStateList.size(); i2++) {
            int iIntValue = ((Integer) ModemSarController.mRegStateList.get(i2)).intValue();
            if (iIntValue != 18) {
                switch (iIntValue) {
                    case 0:
                        str = str + Integer.toString(ModemSarController.mReceiverState);
                        break;
                    case 1:
                    case 2:
                    case 3:
                        if (!z) {
                            str = str + Integer.toString(ModemSarController.mHotspotState);
                            z = true;
                        }
                        break;
                    case 4:
                    case 5:
                    case 14:
                        if (!z2) {
                            str = str + Integer.toString(mSarSensorState);
                            z2 = true;
                        }
                        break;
                    case 6:
                        str = str + Integer.toString(ModemSarController.mMccState);
                        break;
                    case 7:
                        str = str + Integer.toString(ModemSarController.mWifiState);
                        break;
                    case 8:
                        str = str + Integer.toString(ModemSarController.mCableState);
                        break;
                    case 9:
                        str = str + Integer.toString(ModemSarController.mHallState);
                        break;
                    case 10:
                        str = str + Integer.toString(ModemSarController.mUsbState);
                        break;
                    case 11:
                        str = str + Integer.toString(ModemSarController.mSmartTxEnvState);
                        break;
                    case 12:
                        str = str + Integer.toString(ModemSarController.mWhiteListState);
                        break;
                    case 13:
                        str = str + Integer.toString(ModemSarController.mHall2State);
                        break;
                }
            } else {
                str = str + Integer.toString(ModemSarController.mSimMccState);
            }
        }
        ModemSarLog.d(LOG_TAG, "stateStr = " + str);
        return ModemSarController.findDsiFromXML(str.toCharArray(), i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String[] getAntGroupArray(int i, int i2, String[] strArr, String str) {
        String[] strArr2 = new String[i2];
        Arrays.fill(strArr2, String.valueOf(mRxDefaultDsi));
        char[] charArray = str.toCharArray();
        if (ModemSarController.mReceiverState >= 1 || ModemSarController.mHotspotState >= 1 || ModemSarController.mCableState >= 1) {
            for (char c : charArray) {
                strArr2[c - '0'] = Integer.toString(i);
            }
            for (String str2 : strArr) {
                for (char c2 : str2.toCharArray()) {
                    strArr2[c2 - '0'] = Integer.toString(i);
                }
            }
        } else if (mSarSensorState >= 1) {
            if (ModemSarController.mSarSensor1State >= 1 && strArr.length > 0) {
                for (char c3 : strArr[0].toCharArray()) {
                    strArr2[c3 - '0'] = Integer.toString(i);
                }
            }
            if (ModemSarController.mSarSensor2State >= 1 && strArr.length > 1) {
                for (char c4 : strArr[1].toCharArray()) {
                    strArr2[c4 - '0'] = Integer.toString(i);
                }
            }
            if (ModemSarController.mSarSensor3State >= 1 && strArr.length > 2) {
                for (char c5 : strArr[2].toCharArray()) {
                    strArr2[c5 - '0'] = Integer.toString(i);
                }
            }
        } else {
            for (char c6 : charArray) {
                strArr2[c6 - '0'] = Integer.toString(i);
            }
            for (String str3 : strArr) {
                for (char c7 : str3.toCharArray()) {
                    strArr2[c7 - '0'] = Integer.toString(i);
                }
            }
        }
        ModemSarLog.i(LOG_TAG, "getAntGroupArray = " + Arrays.toString(strArr2));
        return strArr2;
    }
}
