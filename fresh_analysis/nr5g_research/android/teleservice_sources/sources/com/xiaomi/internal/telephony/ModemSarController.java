package com.xiaomi.internal.telephony;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import com.xiaomi.modem.ModemUtils;
import com.xiaomi.modem.OemHookAgent;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class ModemSarController {
    public static final String BC_INTENT_ACTION_DSI_VALUE_CHANGED = "com.android.phone.intent.action.dsi_value_changed";
    public static final String BC_INTENT_EXTRA_DSI_VALUE = "dsi";
    public static final int BT_PAN_STATE_I = 3;
    public static final int BT_STATE_I = 2;
    public static final int CABLE_STATE_I = 8;
    public static final int EVENT_BT_CHANGE = 4;
    public static final int EVENT_BT_PAN_CHANGE = 5;
    public static final int EVENT_CABLE_CHANGE = 9;
    public static final int EVENT_GPD_SENSOR_CHANGE = 18;
    public static final int EVENT_HALL2_CHANGE = 16;
    public static final int EVENT_HALL_CHANGE = 10;
    public static final int EVENT_HORIZONTAL_GAME_STATE_CHANGE = 22;
    public static final int EVENT_HST_ENHANCEMENT_STATE_CHANGE = 23;
    public static final int EVENT_INITIAL_DSI_SEND = 100;
    public static final int EVENT_LANDSCAPE_GAME_STATE_CHANGE = 20;
    public static final int EVENT_MCC_CHANGE = 8;
    public static final int EVENT_RADIOPOWER_CHANGE = 12;
    public static final int EVENT_RECEIVER_CHANGE = 1;
    public static final int EVENT_SAR_SENSOR2_CHANGE = 7;
    public static final int EVENT_SAR_SENSOR3_CHANGE = 17;
    public static final int EVENT_SAR_SENSOR_CHANGE = 6;
    public static final int EVENT_SCREEN_STATE_CHANGE = 19;
    public static final int EVENT_SIMCARD_CHANGE = 13;
    public static final int EVENT_SIM_MCC_STATE_CHANGE = 21;
    public static final int EVENT_SMART_TX_ENV_CHANGE = 14;
    public static final int EVENT_USB_CHANGE = 11;
    public static final int EVENT_WHITE_LIST_CHANGE = 15;
    public static final int EVENT_WIFI_CHANGE = 2;
    public static final int EVENT_WIFI_HOSP_CHANGE = 3;
    public static final int GPD_SENSOR_STATE_I = 15;
    public static final int HALL2_STATE_I = 13;
    public static final int HALL_STATE_I = 9;
    public static final int HORIZONTAL_GAME_STATE_I = 19;
    public static final int HST_ENHANCEMENT_STATE_I = 20;
    public static final int LANDSCAPE_GAME_STATE_I = 17;
    private static final String LOG_TAG = "ModemRadController";
    public static final int MCC_STATE_I = 6;
    private static final String PROPERTY_SAR_CURRENT_JUDGE_DSI = "debug.modem.sar.judge_dsi";
    private static final String PROPERTY_SAR_EXCEPTION_COUNT = "persist.radio.sar.excpt_cnt";
    public static final int RECEIVER_STATE_I = 0;
    public static final int SAR_SENSOR2_STATE_I = 5;
    public static final int SAR_SENSOR3_STATE_I = 14;
    public static final int SAR_SENSOR_STATE_I = 4;
    public static final int SCREEN_STATE_I = 16;
    public static final int SIM_MCC_STATE_I = 18;
    public static final int SMART_TX_ENV_I = 11;
    public static final int USB_STATE_I = 10;
    public static final int WHITE_LIST_I = 12;
    public static final int WIFI_HOSP_STATE_I = 1;
    public static final int WIFI_STATE_I = 7;
    protected static int mBtPanState = 0;
    protected static int mBtState = 0;
    protected static int mCableState = 0;
    protected static Context mContext = null;
    protected static DeviceSarConfig mCurrentSarConfig = null;
    protected static int mGpdSensorState = 0;
    protected static int mHall2State = 0;
    protected static int mHallState = 0;
    protected static OemHookAgent mHookAgent = null;
    protected static int mHorizontalGameState = 0;
    protected static int mHotspotState = 0;
    protected static int mHstEnhancementState = 0;
    protected static int mLandScapeGameState = 0;
    protected static int mMccState = 3;
    protected static int mRadioPowerState = 0;
    protected static int mReceiverState = 0;
    protected static int mSIMCardState = 0;
    protected static int mSarSensor1State = 0;
    protected static int mSarSensor2State = 0;
    protected static int mSarSensor3State = 0;
    protected static int mScreenState = 1;
    protected static int mSimMccState = 0;
    protected static int mSmartTxEnvState = 0;
    protected static int mUsbState = 0;
    protected static int mVoltageState = 1;
    protected static int mWhiteListState;
    protected static int mWifiHotSpotState;
    protected static int mWifiState;
    private static ModemSarController sIntance;
    protected static ArrayList mRegStateList = new ArrayList();
    protected static int mCurrentDsi = 0;
    protected HandlerThread mCmdProcThread = new HandlerThread("ModemRadControllerBgThread");
    protected Handler mCmdProcHandler = null;

    protected static int gpdStateProcess(int i) {
        if (i == -1 || i == -2 || i == 0 || i == 1 || i == 2 || i == 4 || i == 8 || i == 1111) {
            return 0;
        }
        return (i == 5 || i == 6 || i == 12 || i == 16 || i == 9 || i == 10) ? 1 : 0;
    }

    protected void startModemSarProcess() {
    }

    public ModemSarController(Context context, OemHookAgent oemHookAgent, DeviceSarConfig deviceSarConfig) {
        mContext = context;
        mHookAgent = oemHookAgent;
        mCurrentSarConfig = deviceSarConfig;
        ModemSarLog.d(LOG_TAG, "Constructor, mContext: " + mContext + ", mHookAgent: " + mHookAgent + ", mCurrentConfig: " + mCurrentSarConfig);
        if (mCurrentSarConfig != null) {
            this.mCmdProcThread.start();
        }
    }

    protected void sendMsgToHandler(int i) {
        if (this.mCmdProcHandler != null) {
            Message messageObtain = Message.obtain();
            messageObtain.what = i;
            messageObtain.obj = new AsyncResult((Object) null, ModemUtils.getBytes(0), (Throwable) null);
            this.mCmdProcHandler.sendMessage(messageObtain);
        }
    }

    protected void sendMsgToHandler(int i, int i2) {
        if (this.mCmdProcHandler != null) {
            Message messageObtain = Message.obtain();
            messageObtain.what = i;
            messageObtain.obj = new AsyncResult((Object) null, ModemUtils.getBytes(i2), (Throwable) null);
            this.mCmdProcHandler.sendMessage(messageObtain);
        }
    }

    protected static boolean isValidListener(int i) {
        String listeners = mCurrentSarConfig.getListeners();
        return listeners != null && i < listeners.length() && listeners.charAt(i) == '1';
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void initRegStateAction(Handler handler) {
        String listeners = mCurrentSarConfig.getListeners();
        ModemSarLog.i(LOG_TAG, "listeners: receiver,hotSpot(wifi_spot,(bluetooth,bluetooth_pan)),sensor1,sensor2,mcc,wifi,cable,hall,usb,smartenv = " + listeners);
        ModemSarLog.d(LOG_TAG, "regList: " + listeners);
        if (listeners != null) {
            for (int i = 0; i < listeners.length(); i++) {
                if (listeners.charAt(i) == '1') {
                    switch (i) {
                        case 0:
                            SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 5, handler, 1, null);
                            mRegStateList.add(0);
                            continue;
                        case 1:
                            SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 1, handler, 3, null);
                            mRegStateList.add(1);
                            break;
                        case 2:
                            SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 8, handler, 4, null);
                            mRegStateList.add(2);
                            break;
                        case 3:
                            SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 7, handler, 5, null);
                            mRegStateList.add(3);
                            break;
                        case 4:
                            SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 3, handler, 6, null);
                            mRegStateList.add(4);
                            break;
                        case 5:
                            if (mCurrentSarConfig.getExtendPropList().containsKey(ModemSarUtils.PROPERTY_SAR_SENSOR_ONE_TYPE) && ((String) mCurrentSarConfig.getExtendPropList().get(ModemSarUtils.PROPERTY_SAR_SENSOR_ONE_TYPE)).equals("true")) {
                                SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 21, handler, 7, null);
                            } else {
                                SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 9, handler, 7, null);
                            }
                            mRegStateList.add(5);
                            break;
                        case 6:
                            SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 6, handler, 8, null);
                            mRegStateList.add(6);
                            break;
                        case 7:
                            SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 0, handler, 2, null);
                            mRegStateList.add(7);
                            break;
                        case 8:
                            SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 10, handler, 9, null);
                            mRegStateList.add(8);
                            break;
                        case 9:
                            SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 4, handler, 10, null);
                            mRegStateList.add(9);
                            break;
                        case 10:
                            SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 11, handler, 11, null);
                            mRegStateList.add(10);
                            break;
                        case 11:
                            SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 15, handler, 14, null);
                            mRegStateList.add(11);
                            break;
                        case 12:
                            SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 16, handler, 15, null);
                            mRegStateList.add(12);
                            break;
                        case 13:
                            SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 20, handler, 16, null);
                            mRegStateList.add(13);
                            break;
                        case 14:
                            SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 22, handler, 17, null);
                            mRegStateList.add(14);
                            break;
                        case 15:
                            SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 17, handler, 18, null);
                            mRegStateList.add(15);
                            break;
                        case 16:
                            SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 24, handler, 19, null);
                            mRegStateList.add(16);
                            break;
                        case 17:
                            SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 25, handler, 20, null);
                            mRegStateList.add(17);
                            break;
                        case 18:
                            SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 27, handler, 21, null);
                            mRegStateList.add(18);
                            break;
                        case 19:
                            SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 28, handler, 22, null);
                            if (ModemSarStatistics.getHandler() != null) {
                                SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 28, ModemSarStatistics.getHandler(), 1, null);
                            }
                            mRegStateList.add(19);
                            break;
                        case 20:
                            SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 30, handler, 23, null);
                            mRegStateList.add(20);
                            break;
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void initRegExceptionAction(Handler handler) {
        SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 13, handler, 12, null);
        SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 14, handler, 13, null);
    }

    protected static int getDsiValue(int i) {
        StringBuilder sb = new StringBuilder();
        if (mRegStateList.size() == 0) {
            return i;
        }
        boolean z = false;
        for (int i2 = 0; i2 < mRegStateList.size(); i2++) {
            switch (((Integer) mRegStateList.get(i2)).intValue()) {
                case 0:
                    sb.append(Integer.toString(mReceiverState));
                    break;
                case 1:
                case 2:
                case 3:
                    if (!z) {
                        sb.append(Integer.toString(mHotspotState));
                        z = true;
                    }
                    break;
                case 4:
                    sb.append(Integer.toString(mSarSensor1State));
                    break;
                case 5:
                    sb.append(Integer.toString(mSarSensor2State));
                    break;
                case 6:
                    sb.append(Integer.toString(mMccState));
                    break;
                case 7:
                    sb.append(Integer.toString(mWifiState));
                    break;
                case 8:
                    sb.append(Integer.toString(mCableState));
                    break;
                case 9:
                    sb.append(Integer.toString(mHallState));
                    break;
                case 10:
                    sb.append(Integer.toString(mUsbState));
                    break;
                case 11:
                    sb.append(Integer.toString(mSmartTxEnvState));
                    break;
                case 12:
                    sb.append(Integer.toString(mWhiteListState));
                    break;
                case 13:
                    sb.append(Integer.toString(mHall2State));
                    break;
                case 14:
                    sb.append(Integer.toString(mSarSensor3State));
                    break;
                case 15:
                    sb.append(Integer.toString(mGpdSensorState));
                    break;
                case 17:
                    sb.append(Integer.toString(mLandScapeGameState));
                    break;
                case 18:
                    sb.append(Integer.toString(mSimMccState));
                    break;
                case 19:
                    sb.append(Integer.toString(mHorizontalGameState));
                    break;
                case 20:
                    sb.append(Integer.toString(mHstEnhancementState));
                    break;
            }
        }
        ModemSarLog.d(LOG_TAG, "stateStr = ".concat(sb.toString()));
        return findDsiFromXML(sb.toString().toCharArray(), i);
    }

    protected static int findDsiFromXML(char[] cArr, int i) {
        return ModemSarUtils.findDsiFromXML(cArr, mCurrentSarConfig, i);
    }

    protected static int sarExceptionHandle(int i, int i2) {
        if (3 != i && 4 != i) {
            return i;
        }
        recordSarException(i);
        return i2;
    }

    protected static int getSimMccMatchState(String str) {
        ModemSarLog.d(LOG_TAG, "getSimMccMatchState, actualSimMcc: " + str);
        int i = 0;
        if (mCurrentSarConfig.getSimMccList() != null) {
            String simMccList = mCurrentSarConfig.getSimMccList();
            ModemSarLog.d(LOG_TAG, "getSimMccMatchState, simMccList: " + simMccList);
            if (simMccList != null && str != null) {
                for (String str2 : str.split(",")) {
                    ModemSarLog.d(LOG_TAG, "getSimMccMatchState, simMccArray: " + str2);
                    if (str2 != null && !str2.isEmpty() && simMccList.contains(str2)) {
                        i = 1;
                        break;
                    }
                }
            }
        }
        ModemSarLog.d(LOG_TAG, "getSimMccMatchState, state: " + i);
        return i;
    }

    public class CmdHandler extends Handler {
        protected boolean mIsInitDsi;

        public void dsiHandle() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
        }

        public CmdHandler(Looper looper) {
            super(looper);
            this.mIsInitDsi = false;
            ModemSarLog.i(ModemSarController.LOG_TAG, "CmdHandler Constructor init start");
            ModemSarController.initRegStateAction(this);
            ModemSarController.initRegExceptionAction(this);
            ModemSarLog.d(ModemSarController.LOG_TAG, "CmdHandler Constructor init done");
        }
    }

    private static void recordSarException(int i) {
        int i2;
        int i3;
        String str = SystemProperties.get(PROPERTY_SAR_EXCEPTION_COUNT);
        int i4 = 0;
        if (str == null || str.isEmpty()) {
            i2 = 0;
        } else {
            String[] strArrSplit = str.split(",");
            if (strArrSplit.length >= 1) {
                try {
                    i3 = Integer.parseInt(strArrSplit[0]);
                } catch (Exception e) {
                    ModemSarLog.e(LOG_TAG, "recordSarException happens Exception, sarSensorState = " + i);
                    e.printStackTrace();
                    i3 = 0;
                }
            } else {
                i3 = 0;
            }
            if (strArrSplit.length > 1) {
                try {
                    i4 = Integer.parseInt(strArrSplit[1]);
                } catch (Exception e2) {
                    ModemSarLog.e(LOG_TAG, "recordSarException happens Exception, sarSensorState = " + i);
                    e2.printStackTrace();
                }
            }
            i2 = i4;
            i4 = i3;
        }
        if (3 == i) {
            i4++;
        } else if (4 == i) {
            i2++;
        }
        String str2 = i4 + "," + i2;
        try {
            SystemProperties.set(PROPERTY_SAR_EXCEPTION_COUNT, str2);
        } catch (Exception e3) {
            ModemSarLog.e(LOG_TAG, "Failed to set SAR exception count property: " + str2 + ", sarSensorState = " + i);
            e3.printStackTrace();
        }
    }

    protected static void saveAndBroadcastDsiChangeEvent(int i) {
        try {
            ModemSarLog.d(LOG_TAG, "saveAndBroadcastIndexChangeEvent, index = " + i);
            SystemProperties.set(PROPERTY_SAR_CURRENT_JUDGE_DSI, "" + i);
            notifyCurrentDsiOnNeed(i);
        } catch (Exception e) {
            ModemSarLog.e(LOG_TAG, "saveAndBroadcastIndexChangeEvent happens Exception, index = " + i);
            e.printStackTrace();
        }
    }

    protected static void notifyCurrentDsiOnNeed(int i) {
        Intent intent = new Intent(BC_INTENT_ACTION_DSI_VALUE_CHANGED);
        intent.putExtra(BC_INTENT_EXTRA_DSI_VALUE, i);
        mContext.sendBroadcast(intent);
    }
}
