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
    private static String[] mSensorAntGroup;
    private static MtkGroupSarController sIntance = null;
    private static int mSarSensorState = 0;
    private static int mAntennaNums = 15;
    private static int mRxDefaultDsi = 3;
    private static String mDefaultAntGroup = "";
    private static String mCurrentDsiStr = "";

    public static void make(Context context, OemHookAgent hookAgent, DeviceSarConfig deviceSarConfig) {
        ModemSarLog.i(LOG_TAG, "make, context: " + context + ", hookAgent: " + hookAgent + ", deviceName: " + ModemUtils.getDeviceName());
        if (sIntance == null) {
            sIntance = new MtkGroupSarController(context, hookAgent, deviceSarConfig);
        }
    }

    private MtkGroupSarController(Context context, OemHookAgent hookAgent, DeviceSarConfig deviceSarConfig) {
        super(context, hookAgent, deviceSarConfig);
        startModemSarProcess();
    }

    @Override // com.xiaomi.internal.telephony.ModemSarController
    protected void startModemSarProcess() {
        if (mCurrentSarConfig != null) {
            mSensorAntGroup = mCurrentSarConfig.getAntennaGroup().split(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE);
            mDefaultAntGroup = mCurrentSarConfig.getDefaultGroup();
            ModemSarLog.i(LOG_TAG, "startModemProcess, mSensorAntGroup = " + Arrays.toString(mSensorAntGroup) + ", mDefaultAntGroup = " + mDefaultAntGroup);
            try {
                mAntennaNums = Integer.parseInt(mCurrentSarConfig.getAntennaNums());
                mRxDefaultDsi = Integer.parseInt(mCurrentSarConfig.getRxDefaultDsi());
                ModemSarLog.i(LOG_TAG, "startModemProcess, mAntennaNums = " + mAntennaNums + ", mRxDefaultIndex = " + mRxDefaultDsi);
            } catch (Exception e) {
                e.printStackTrace();
                ModemSarLog.e(LOG_TAG, "startModemProcess, set antenna nums failed");
            }
            this.mCmdProcHandler = new MtkCmdHandler(this.mCmdProcThread.getLooper());
            if (this.mCmdProcHandler != null) {
                sendMsgToHandler(100);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void sendDsiToModem(String[] dsiStringArray) {
        if (dsiStringArray == null || dsiStringArray.length == 0) {
            ModemSarLog.d(LOG_TAG, "dsiStringArray cannot be null or empty!");
            return;
        }
        int[] dsiIntArray = new int[dsiStringArray.length];
        for (int i = 0; i < dsiStringArray.length; i++) {
            try {
                dsiIntArray[i] = Integer.parseInt(dsiStringArray[i]);
            } catch (Exception e) {
                e.printStackTrace();
                ModemSarLog.e(LOG_TAG, "Invalid integer value in dsiStringArray at index " + i);
            }
        }
        ModemSarLog.d(LOG_TAG, "Sending index command to modem, command = " + Arrays.toString(dsiIntArray));
        mHookAgent.onMtkSendGroupSarDsiAtCommand(dsiIntArray);
    }

    protected static class MtkCmdHandler extends ModemSarController.CmdHandler {
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
            int currentDsi = MtkGroupSarController.getDsiValue(MtkGroupSarController.mRxDefaultDsi);
            String[] dsiStringPerAntGroup = MtkGroupSarController.getAntGroupArray(currentDsi, MtkGroupSarController.mAntennaNums, MtkGroupSarController.mSensorAntGroup, MtkGroupSarController.mDefaultAntGroup);
            String currentDsiStr = String.join(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE, dsiStringPerAntGroup);
            ModemSarLog.d(MtkGroupSarController.LOG_TAG, "indexHandle, str = " + currentDsiStr);
            if (!MtkGroupSarController.mCurrentDsiStr.equals(currentDsiStr) || this.mIsInitDsi) {
                MtkGroupSarController.sendDsiToModem(dsiStringPerAntGroup);
                ModemSarController.saveAndBroadcastDsiChangeEvent(currentDsi);
                MtkGroupSarController.mCurrentDsiStr = currentDsiStr;
            }
            ModemSarController.mHotspotState = 0;
            MtkGroupSarController.mSarSensorState = 0;
            this.mIsInitDsi = false;
        }

        @Override // com.xiaomi.internal.telephony.ModemSarController.CmdHandler, android.os.Handler
        public void handleMessage(Message msg) {
            AsyncResult ar = (AsyncResult) msg.obj;
            byte[] bytes = (byte[]) ar.result;
            ByteBuffer buffer = ModemUtils.getBuffer(bytes);
            if (buffer == null) {
                ModemSarLog.d(MtkGroupSarController.LOG_TAG, "handlerMessage, buffer is null, do nothing");
            }
            switch (msg.what) {
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
                case 100:
                    this.mIsInitDsi = true;
                    ModemSarLog.d(MtkGroupSarController.LOG_TAG, "Need to resend the current DSI to modem.");
                    dsiHandle();
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
                case 19:
                    ModemSarController.mScreenState = buffer.getInt();
                    ModemSarLog.d(MtkGroupSarController.LOG_TAG, "EVENT_SCREEN_STATE_CHANGE, state = " + ModemSarController.mScreenState);
                    if (ModemSarController.isValidListener(17)) {
                        if (ModemSarController.mScreenState == 0) {
                            ModemSarController.mLandScapeGameState = 0;
                            SarDeviceStateListener.unregisterDeviceStateChangeEvent(25, this);
                        } else if (ModemSarController.mScreenState == 1) {
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
                    ModemSarController.mSimMccState = ModemSarController.getSimMccMatchState(new String(bytes, StandardCharsets.UTF_8));
                    ModemSarLog.d(MtkGroupSarController.LOG_TAG, "EVENT_SIM_MCC_STATE_CHANGE, state = " + ModemSarController.mSimMccState);
                    dsiHandle();
                    break;
            }
        }
    }

    protected static int getDsiValue(int defaultDsi) {
        String stateStr = "";
        boolean isHotSpotHandle = false;
        boolean isSarSensorHandle = false;
        if (mRegStateList.size() == 0) {
            return 0;
        }
        for (int i = 0; i < mRegStateList.size(); i++) {
            switch (mRegStateList.get(i).intValue()) {
                case 0:
                    stateStr = stateStr + Integer.toString(mReceiverState);
                    break;
                case 1:
                case 2:
                case 3:
                    if (!isHotSpotHandle) {
                        stateStr = stateStr + Integer.toString(mHotspotState);
                        isHotSpotHandle = true;
                    }
                    break;
                case 4:
                case 5:
                case 14:
                    if (!isSarSensorHandle) {
                        stateStr = stateStr + Integer.toString(mSarSensorState);
                        isSarSensorHandle = true;
                    }
                    break;
                case 6:
                    stateStr = stateStr + Integer.toString(mMccState);
                    break;
                case 7:
                    stateStr = stateStr + Integer.toString(mWifiState);
                    break;
                case 8:
                    stateStr = stateStr + Integer.toString(mCableState);
                    break;
                case 9:
                    stateStr = stateStr + Integer.toString(mHallState);
                    break;
                case 10:
                    stateStr = stateStr + Integer.toString(mUsbState);
                    break;
                case 11:
                    stateStr = stateStr + Integer.toString(mSmartTxEnvState);
                    break;
                case 12:
                    stateStr = stateStr + Integer.toString(mWhiteListState);
                    break;
                case 13:
                    stateStr = stateStr + Integer.toString(mHall2State);
                    break;
                case 18:
                    stateStr = stateStr + Integer.toString(mSimMccState);
                    break;
            }
        }
        ModemSarLog.d(LOG_TAG, "stateStr = " + stateStr);
        int result = findDsiFromXML(stateStr.toCharArray(), defaultDsi);
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String[] getAntGroupArray(int currentDsi, int antNums, String[] sensorAntGroup, String defaultAntGroup) {
        String[] antGroupArray = new String[antNums];
        Arrays.fill(antGroupArray, String.valueOf(mRxDefaultDsi));
        char[] defualtGroupArr = defaultAntGroup.toCharArray();
        int i = 0;
        if (mReceiverState >= 1 || mHotspotState >= 1 || mCableState >= 1) {
            for (char c : defualtGroupArr) {
                antGroupArray[c - '0'] = Integer.toString(currentDsi);
            }
            int i2 = sensorAntGroup.length;
            while (i < i2) {
                String antIndexGroup = sensorAntGroup[i];
                for (char c2 : antIndexGroup.toCharArray()) {
                    antGroupArray[c2 - '0'] = Integer.toString(currentDsi);
                }
                i++;
            }
        } else if (mSarSensorState >= 1) {
            if (mSarSensor1State >= 1 && sensorAntGroup.length > 0) {
                char[] sarSensor1Arr = sensorAntGroup[0].toCharArray();
                for (char c3 : sarSensor1Arr) {
                    antGroupArray[c3 - '0'] = Integer.toString(currentDsi);
                }
            }
            if (mSarSensor2State >= 1 && sensorAntGroup.length > 1) {
                char[] sarSensor2Arr = sensorAntGroup[1].toCharArray();
                for (char c4 : sarSensor2Arr) {
                    antGroupArray[c4 - '0'] = Integer.toString(currentDsi);
                }
            }
            if (mSarSensor3State >= 1 && sensorAntGroup.length > 2) {
                char[] sarSensor3Arr = sensorAntGroup[2].toCharArray();
                for (char c5 : sarSensor3Arr) {
                    antGroupArray[c5 - '0'] = Integer.toString(currentDsi);
                }
            }
        } else {
            for (char c6 : defualtGroupArr) {
                antGroupArray[c6 - '0'] = Integer.toString(currentDsi);
            }
            int i3 = sensorAntGroup.length;
            while (i < i3) {
                String antIndexGroup2 = sensorAntGroup[i];
                char[] antIndexGroupArr = antIndexGroup2.toCharArray();
                for (char c7 : antIndexGroupArr) {
                    antGroupArray[c7 - '0'] = Integer.toString(currentDsi);
                }
                i++;
            }
        }
        ModemSarLog.i(LOG_TAG, "getAntGroupArray = " + Arrays.toString(antGroupArray));
        return antGroupArray;
    }
}
