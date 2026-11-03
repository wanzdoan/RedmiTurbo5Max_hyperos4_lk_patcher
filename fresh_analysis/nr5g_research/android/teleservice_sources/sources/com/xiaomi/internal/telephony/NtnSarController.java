package com.xiaomi.internal.telephony;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.xiaomi.modem.ModemUtils;
import com.xiaomi.modem.OemHookAgent;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class NtnSarController {
    private static final int BATTERY_HIGH_LEVEL = 5;
    private static final int BATTERY_HIGH_LEVEL_TRIGGER = 0;
    private static final int BATTERY_LEVEL_I = 5;
    private static final int BATTERY_LEVEL_NOT_TRIGGER = -1;
    private static final int BATTERY_LOWER_LEVEL = 3;
    private static final int BATTERY_LOW_LEVEL_TRIGGER = 1;
    private static final String BC_INTENT_ACTION_DSI_VALUE_CHANGED = "com.android.phone.intent.action.SATELLITE_DSI_CHANGED";
    private static final String BC_INTENT_EXTRA_DSI_VALUE_STATE = "dsi";
    private static final int DSI_DEFAULT_VALUE = 0;
    private static final int EVENT_BATTERY_LEVEL_CHANGE = 8;
    private static final int EVENT_GPD_SENSOR_CHANGE = 3;
    private static final int EVENT_GRAVITY_Y_AXES_CHANGE = 7;
    private static final int EVENT_NTN_RADIO_POWER_CHANGE = 4;
    private static final int EVENT_RECEIVER_CHANGE = 2;
    private static final int EVENT_SAR_SENSOR1_CHANGE = 5;
    private static final int EVENT_SAR_SENSOR2_CHANGE = 6;
    private static final int EVENT_SATELLITE_STATE_CHANGE = 1;
    private static final int GPD_NON_TRIGGER_STATE = 0;
    private static final int GPD_SENSOR_STATE_DEFAULT = 0;
    private static final int GPD_SENSOR_STATE_I = 1;
    private static final int GPD_TRIGGER_STATE = 1;
    private static final int GRAVITY_SENSOR_Y_AXES_STATE_DEFAULT = 0;
    private static final int GRAVITY_SENSOR_Y_AXES_STATE_I = 4;
    private static final String LOG_TAG = "NtnRadController";
    private static final int RECEIVER_STATE_DEFAULT = 0;
    private static final int RECEIVER_STATE_I = 0;
    private static final int SAR_SENSOR1_STATE_DEFAULT = 0;
    private static final int SAR_SENSOR1_STATE_I = 2;
    private static final int SAR_SENSOR2_STATE_DEFAULT = 0;
    private static final int SAR_SENSOR2_STATE_I = 3;
    private static final int SATELLITE_STATE_DEFAULT = 0;
    private static int mBatteryLevelState;
    private static Context mContext;
    private static DeviceSarConfig mCurrentSarConfig;
    private static int mGPDSensorState;
    private static int mGravitySensorYAxesState;
    private static OemHookAgent mHookAgent;
    private static int mIsSatelliteEnabled;
    private static int mReceiverState;
    private static int mSarSensor1State;
    private static int mSarSensor2State;
    private static NtnSarController sIntance;
    private static ArrayList mRegStateList = new ArrayList();
    private static int mCurrentDsi = 0;
    private HandlerThread mCmdProcThread = new HandlerThread("NtnRadControllerBgThread");
    private Handler mCmdProcHandler = null;

    public static void make(Context context, DeviceSarConfig deviceSarConfig) {
        ModemSarLog.i(LOG_TAG, "make, context: " + context + ", deviceName: " + ModemUtils.getDeviceName());
        if (sIntance == null) {
            sIntance = new NtnSarController(context, deviceSarConfig);
        }
    }

    private NtnSarController(Context context, DeviceSarConfig deviceSarConfig) {
        mContext = context;
        mCurrentSarConfig = deviceSarConfig;
        startNtnSarProcess();
    }

    private void startNtnSarProcess() {
        if (mCurrentSarConfig != null) {
            this.mCmdProcThread.start();
            this.mCmdProcHandler = new NtnCmdHandler(this.mCmdProcThread.getLooper());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void registerSatelliteState(Handler handler) {
        SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 23, handler, 1, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void initRegStateAction(Handler handler) {
        String listeners = mCurrentSarConfig.getListeners();
        ModemSarLog.i(LOG_TAG, "listeners: receiver, gpd = " + listeners);
        ModemSarLog.d(LOG_TAG, "regList: " + listeners);
        if (listeners != null) {
            for (int i = 0; i < listeners.length(); i++) {
                if (listeners.charAt(i) == '1') {
                    if (i == 0) {
                        SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 5, handler, 2, null);
                        mRegStateList.add(0);
                    } else if (i == 1) {
                        SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 17, handler, 3, null);
                        mRegStateList.add(1);
                    } else if (i == 2) {
                        SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 3, handler, 5, null);
                        mRegStateList.add(2);
                    } else if (i == 3) {
                        SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 9, handler, 6, null);
                        mRegStateList.add(3);
                    } else if (i == 4) {
                        SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 18, handler, 7, null);
                        mRegStateList.add(4);
                    } else if (i == 5) {
                        SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 26, handler, 8, null);
                        mRegStateList.add(5);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void removeRegStateAction(Handler handler) {
        String listeners = mCurrentSarConfig.getListeners();
        ModemSarLog.d(LOG_TAG, "unRegList: " + listeners);
        if (listeners != null) {
            for (int i = 0; i < listeners.length(); i++) {
                if (listeners.charAt(i) == '1') {
                    if (i == 0) {
                        SarDeviceStateListener.unregisterDeviceStateChangeEvent(5, handler);
                        mRegStateList.remove((Object) 0);
                    } else if (i == 1) {
                        SarDeviceStateListener.unregisterDeviceStateChangeEvent(17, handler);
                        mRegStateList.remove((Object) 1);
                    } else if (i == 2) {
                        SarDeviceStateListener.unregisterDeviceStateChangeEvent(3, handler);
                        mRegStateList.remove((Object) 2);
                    } else if (i == 3) {
                        SarDeviceStateListener.unregisterDeviceStateChangeEvent(9, handler);
                        mRegStateList.remove((Object) 3);
                    } else if (i == 4) {
                        SarDeviceStateListener.unregisterDeviceStateChangeEvent(18, handler);
                        mRegStateList.remove((Object) 4);
                    } else if (i == 5) {
                        SarDeviceStateListener.unregisterDeviceStateChangeEvent(26, handler);
                        mRegStateList.remove((Object) 5);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void initRegExceptionAction(Handler handler) {
        SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 19, handler, 4, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void removeRegExceptionAction(Handler handler) {
        SarDeviceStateListener.unregisterDeviceStateChangeEvent(19, handler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void resetState() {
        mReceiverState = 0;
        mGPDSensorState = 0;
        mSarSensor1State = 0;
        mSarSensor2State = 0;
        mGravitySensorYAxesState = 0;
        mBatteryLevelState = 0;
        mCurrentDsi = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getDsiValue(int i) {
        StringBuilder sb = new StringBuilder();
        if (mRegStateList.size() == 0) {
            return i;
        }
        for (int i2 = 0; i2 < mRegStateList.size(); i2++) {
            int iIntValue = ((Integer) mRegStateList.get(i2)).intValue();
            if (iIntValue == 0) {
                sb.append(Integer.toString(mReceiverState));
            } else if (iIntValue == 1) {
                sb.append(Integer.toString(mGPDSensorState));
            } else if (iIntValue == 2) {
                sb.append(Integer.toString(mSarSensor1State));
            } else if (iIntValue == 3) {
                sb.append(Integer.toString(mSarSensor2State));
            } else if (iIntValue == 4) {
                sb.append(Integer.toString(mGravitySensorYAxesState));
            } else if (iIntValue == 5) {
                sb.append(Integer.toString(mBatteryLevelState));
            }
        }
        ModemSarLog.d(LOG_TAG, "getIndex, stateStr = ".concat(sb.toString()));
        return findDsiFromXML(sb.toString().toCharArray(), i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int gpdSensorStateHandle(int i) {
        int i2 = i == -1 ? 0 : 1;
        ModemSarLog.i(LOG_TAG, "gpdSensorStateHandle, gpdSensorState = " + i2);
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int batteryLevelHandle(int i) {
        int i2;
        if (i <= 3) {
            i2 = 1;
        } else {
            i2 = i >= 5 ? 0 : -1;
        }
        ModemSarLog.i(LOG_TAG, "batteryLevelHandle, batteryLevelState = " + i2);
        return i2;
    }

    private static int findDsiFromXML(char[] cArr, int i) {
        return ModemSarUtils.findDsiFromXML(cArr, mCurrentSarConfig, i);
    }

    class NtnCmdHandler extends Handler {
        private boolean mIsInitDsi;

        public NtnCmdHandler(Looper looper) {
            super(looper);
            this.mIsInitDsi = false;
            NtnSarController.registerSatelliteState(this);
            ModemSarLog.d(NtnSarController.LOG_TAG, "NtnCmdHandler Constructor init done");
        }

        public void dsiHandle() {
            ModemSarLog.d(NtnSarController.LOG_TAG, "ReceiverState is = " + NtnSarController.mReceiverState + ", GPDSensorState is = " + NtnSarController.mGPDSensorState + ", BatteryLevelState is = " + NtnSarController.mBatteryLevelState);
            int dsiValue = NtnSarController.getDsiValue(0);
            StringBuilder sb = new StringBuilder("currentIndex is = ");
            sb.append(dsiValue);
            ModemSarLog.d(NtnSarController.LOG_TAG, sb.toString());
            if (dsiValue >= 0 && (dsiValue != NtnSarController.mCurrentDsi || this.mIsInitDsi)) {
                NtnSarController.notifyCurrentDsiOnNeed(dsiValue);
                NtnSarController.mCurrentDsi = dsiValue;
            } else {
                ModemSarLog.d(NtnSarController.LOG_TAG, "The current index have set, don`t need to set it again.");
            }
            this.mIsInitDsi = false;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            ModemSarLog.d(NtnSarController.LOG_TAG, "handlerMessage, msg = " + message.what);
            ByteBuffer buffer = ModemUtils.getBuffer((byte[]) ((AsyncResult) message.obj).result);
            if (buffer == null) {
                ModemSarLog.d(NtnSarController.LOG_TAG, "handlerMessage, buffer is null, do nothing");
            }
            switch (message.what) {
                case 1:
                    int i = buffer.getInt();
                    ModemSarLog.d(NtnSarController.LOG_TAG, "EVENT_SATELLITE_STATE_CHANGE, state = " + i);
                    if (1 == i) {
                        NtnSarController.initRegStateAction(this);
                        NtnSarController.initRegExceptionAction(this);
                        dsiHandle();
                    } else if (i == 0) {
                        NtnSarController.removeRegStateAction(this);
                        NtnSarController.removeRegExceptionAction(this);
                        NtnSarController.resetState();
                    }
                    break;
                case 2:
                    NtnSarController.mReceiverState = buffer.getInt();
                    ModemSarLog.d(NtnSarController.LOG_TAG, "EVENT_RECEIVER_CHANGE, ReceiverState = " + NtnSarController.mReceiverState);
                    dsiHandle();
                    break;
                case 3:
                    int iGpdSensorStateHandle = NtnSarController.gpdSensorStateHandle(buffer.getInt());
                    if (iGpdSensorStateHandle != NtnSarController.mGPDSensorState) {
                        NtnSarController.mGPDSensorState = iGpdSensorStateHandle;
                        ModemSarLog.d(NtnSarController.LOG_TAG, "EVENT_GPD_SENSOR_CHANGE, GPDSensorState = " + NtnSarController.mGPDSensorState);
                        dsiHandle();
                    }
                    break;
                case 4:
                    this.mIsInitDsi = true;
                    ModemSarLog.d(NtnSarController.LOG_TAG, "Need to resend the current DSI to modem");
                    dsiHandle();
                    break;
                case 5:
                    NtnSarController.mSarSensor1State = buffer.getInt();
                    ModemSarLog.d(NtnSarController.LOG_TAG, "EVENT_SAR_SENSOR1_CHANGE, SarSensor1State = " + NtnSarController.mSarSensor1State);
                    dsiHandle();
                    break;
                case 6:
                    NtnSarController.mSarSensor2State = buffer.getInt();
                    ModemSarLog.d(NtnSarController.LOG_TAG, "EVENT_SAR_SENSOR2_CHANGE, SarSensor2State = " + NtnSarController.mSarSensor2State);
                    dsiHandle();
                    break;
                case 7:
                    NtnSarController.mGravitySensorYAxesState = buffer.getInt();
                    ModemSarLog.d(NtnSarController.LOG_TAG, "EVENT_GRAVITY_Y_AXES_CHANGE, GravityYAxes = " + NtnSarController.mGravitySensorYAxesState);
                    dsiHandle();
                    break;
                case 8:
                    int iBatteryLevelHandle = NtnSarController.batteryLevelHandle(buffer.getInt());
                    if (iBatteryLevelHandle != -1 && iBatteryLevelHandle != NtnSarController.mBatteryLevelState) {
                        NtnSarController.mBatteryLevelState = iBatteryLevelHandle;
                        ModemSarLog.d(NtnSarController.LOG_TAG, "EVENT_BATTERY_LEVEL_CHANGE, BatteryLevel = " + NtnSarController.mBatteryLevelState);
                        dsiHandle();
                        break;
                    }
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifyCurrentDsiOnNeed(int i) {
        ModemSarLog.i(LOG_TAG, "notifyCurrentDsiOnNeed, index = " + i);
        Intent intent = new Intent(BC_INTENT_ACTION_DSI_VALUE_CHANGED);
        intent.putExtra("dsi", i);
        mContext.sendBroadcast(intent);
    }
}
