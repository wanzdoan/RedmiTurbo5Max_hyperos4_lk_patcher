package miui.telephony;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemProperties;
import android.util.Log;
import com.android.internal.telephony.RIL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* JADX INFO: loaded from: classes.dex */
public class MiuiRadioStallDetector {
    private static final String EVENT_OEM_RIL_STALL_DETECTED = "RIL_STALL_DETECTED";
    private static final int EVENT_RIL_CONNECTED = 1;
    private static final int EVENT_RIL_HEART_BEAT_INDICATION = 3;
    private static final int EVENT_RIL_HEART_BEAT_IND_TIMEOUT = 4;
    private static final int EVENT_RIL_HEART_BEAT_RESPONSE = 2;
    private static final String LOG_TAG = "MiuiRadioStallDetector";
    private static final int MAX_RIL_HEART_BEAT_WAIT_MILLIS = 60000;
    private static final int RIL_STATUS_AVAILABLE = 0;
    private static final int RIL_STATUS_RESP_BACK = 2;
    private static final int RIL_STATUS_STALL = 1;
    private static final int RIL_STATUS_UNSL_BACK = 4;
    private static final int TYPE_HEART_BEAT_INDICATION = 2;
    private static final int TYPE_HEART_BEAT_RESPONSE = 1;
    private static final int TYPE_TRIGGER_HEART_BEAT_REASON_DEBUG = 3;
    private static final int TYPE_TRIGGER_HEART_BEAT_REASON_IND_ALARM_UP = 2;
    private static final int TYPE_TRIGGER_HEART_BEAT_REASON_RIL_REQ_TIMEOUT = 1;
    private static final int TYPE_TRIGGER_HEART_BEAT_REASON_UNKNOWN = -1;
    private static final String WAKELOCK_TAG = "*radio-stall-detector*";
    private AlarmManager mAlarmMgr;
    private Context mContext;
    private int mPhoneId;
    private RIL mRil;
    private final PowerManager.WakeLock mWakeLock;
    private static int MAX_CONTINUOUS_RIL_REQ_STALL_TIMES = 10;
    private static int RIL_INDICATION_TIME_OUT_MILLIS = 7200000;
    private volatile boolean mIsStarted = false;
    private volatile int mContinuousRilReqStallTimes = 0;
    private volatile int mRilStat = 0;
    private Handler mHandler = new Handler(Looper.myLooper()) { // from class: miui.telephony.MiuiRadioStallDetector.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            MiuiRadioStallDetector.this.logd("handleMessage, msg = " + msg.what);
            switch (msg.what) {
                case 1:
                    MiuiRadioStallDetector.this.mContinuousRilReqStallTimes = 0;
                    MiuiRadioStallDetector.this.mHeartBeatStatus.resetFlag();
                    MiuiRadioStallDetector.this.clearWakeLock();
                    MiuiRadioStallDetector.this.mHandler.removeMessages(4);
                    MiuiRadioStallDetector.this.mRilStat = 0;
                    break;
                case 2:
                    MiuiRadioStallDetector.this.evaluateHeartBeatResult(msg.arg1, 1);
                    break;
                case 3:
                    byte[] result = (byte[]) msg.obj;
                    int serial = MiuiRadioStallDetector.this.parseHeartBeatSerialInfo(result);
                    MiuiRadioStallDetector.this.evaluateHeartBeatResult(serial, 2);
                    break;
                case 4:
                    MiuiRadioStallDetector.this.clearWakeLock();
                    MiuiRadioStallDetector.this.processHeartBeatIndTimeout(msg.arg1);
                    break;
                default:
                    MiuiRadioStallDetector.this.logd("handleMessage error");
                    break;
            }
        }
    };
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: miui.telephony.MiuiRadioStallDetector.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                return;
            }
            if (intent.getAction().equals(CommenUtils.ACTION_RADIO_HEART_BEAT_IND)) {
                int slotId = intent.getIntExtra(CommenUtils.BROADCAST_SUB, 0);
                if (slotId != MiuiRadioStallDetector.this.mPhoneId || SystemProperties.getBoolean("ril_stall_simulate_no_indication", false)) {
                    return;
                }
                MiuiRadioStallDetector.this.logd("ACTION_RADIO_HEART_BEAT_IND");
                byte[] result = intent.getByteArrayExtra(CommenUtils.BROADCAST_CONTEXT);
                Message msg = MiuiRadioStallDetector.this.mHandler.obtainMessage(3, result);
                MiuiRadioStallDetector.this.mHandler.sendMessage(msg);
                return;
            }
            if (intent.getAction().equals("xiaomi.intent.action.ACTION_TELEPHONY_DEBUG")) {
                boolean extraValue = intent.getBooleanExtra("ril_stall_heart_beat_on", false);
                if (extraValue) {
                    MiuiRadioStallDetector.this.triggerHeartBeat(3);
                }
            }
        }
    };
    private final AlarmManager.OnAlarmListener mAlarmListener = new AlarmManager.OnAlarmListener() { // from class: miui.telephony.MiuiRadioStallDetector.3
        @Override // android.app.AlarmManager.OnAlarmListener
        public void onAlarm() {
            MiuiRadioStallDetector.this.logd("ril indication time up, onAlarm()");
            MiuiRadioStallDetector.this.triggerHeartBeat(2);
            MiuiRadioStallDetector.this.startIndicationAlarm();
        }
    };
    private volatile HeartBeatStatus mHeartBeatStatus = new HeartBeatStatus(this);

    private class HeartBeatStatus {
        boolean mIndicationFlag;
        boolean mResponseFlag;
        int mSerial = 0;
        int mTriggerReason = -1;
        long mTimestamp = 0;

        HeartBeatStatus(MiuiRadioStallDetector miuiRadioStallDetector) {
        }

        public void resetFlag() {
            this.mTriggerReason = -1;
            this.mTimestamp = 0L;
            this.mIndicationFlag = false;
            this.mResponseFlag = false;
        }

        public String toString() {
            return "HeartBeatStatus = [mSerial = " + this.mSerial + ", mTriggerReason = " + this.mTriggerReason + ", mTimestamp = " + this.mTimestamp + ", mIndicationFlag = " + this.mIndicationFlag + ", mResponseFlag = " + this.mResponseFlag + "]";
        }
    }

    public MiuiRadioStallDetector(RIL ril, Context context, int phoneId) {
        this.mRil = ril;
        this.mContext = context;
        this.mPhoneId = phoneId;
        this.mAlarmMgr = (AlarmManager) context.getSystemService("alarm");
        PowerManager pm = (PowerManager) context.getSystemService("power");
        this.mWakeLock = pm.newWakeLock(1, WAKELOCK_TAG);
        this.mWakeLock.setReferenceCounted(false);
        IntentFilter filter = new IntentFilter();
        filter.addAction(CommenUtils.ACTION_RADIO_HEART_BEAT_IND);
        filter.addAction("xiaomi.intent.action.ACTION_TELEPHONY_DEBUG");
        this.mContext.registerReceiver(this.mReceiver, filter, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int parseHeartBeatSerialInfo(byte[] data) {
        if (data == null || data.length == 0) {
            return 0;
        }
        try {
            if (MiuiTelephony.IS_QCOM) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(data);
                byteBuffer.order(ByteOrder.nativeOrder());
                return Long.valueOf(byteBuffer.getLong()).intValue();
            }
            if (MiuiTelephony.IS_XRING) {
                Integer serial = Integer.valueOf(new String(data));
                return serial.intValue();
            }
            return 0;
        } catch (NumberFormatException e) {
            loge("parseHeartBeatSerialInfo error : " + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processHeartBeatIndTimeout(int serial) {
        int errorCode;
        if (this.mHeartBeatStatus.mSerial != serial) {
            return;
        }
        logd("processHeartBeatIndTimeout..." + this.mHeartBeatStatus.toString());
        notifyRadioStallChanged(1);
        this.mRilStat = 1 | this.mRilStat;
        if (!this.mHeartBeatStatus.mIndicationFlag) {
            errorCode = CommenUtils.REASON_RECOVERY_FAIL_MSG_IND_NOT_BACK;
        } else if (!this.mHeartBeatStatus.mResponseFlag) {
            errorCode = CommenUtils.REASON_RECOVERY_FAIL_MSG_RESP_NOT_BACK;
        } else {
            errorCode = CommenUtils.REASON_RECOVERY_FAIL_MSG_BOTH_NOT_BACK;
        }
        recordStatistic(4, errorCode);
        this.mHeartBeatStatus.resetFlag();
    }

    public void start() {
        if (!this.mIsStarted) {
            this.mIsStarted = true;
            this.mContinuousRilReqStallTimes = 0;
            this.mRil.registerForRilConnected(this.mHandler, 1, (Object) null);
        }
    }

    public void stop() {
        if (this.mIsStarted) {
            this.mIsStarted = false;
            this.mContinuousRilReqStallTimes = 0;
            this.mRil.unregisterForRilConnected(this.mHandler);
            this.mHandler.removeCallbacksAndMessages(null);
            if ((this.mRilStat & 1) != 0) {
                notifyRadioStallChanged(0);
            }
        }
    }

    public void doRilMessageTimeOut() {
        if (this.mIsStarted) {
            this.mContinuousRilReqStallTimes++;
            if (this.mContinuousRilReqStallTimes >= MAX_CONTINUOUS_RIL_REQ_STALL_TIMES) {
                this.mContinuousRilReqStallTimes = 0;
                triggerHeartBeat(1);
            }
        }
    }

    public void processResponseInternal() {
        if (this.mIsStarted) {
            this.mContinuousRilReqStallTimes = 0;
            if ((this.mRilStat & 1) != 0) {
                if ((this.mRilStat & 4) != 0) {
                    notifyRadioStallChanged(0);
                    this.mRilStat = 0;
                } else {
                    this.mRilStat |= 2;
                }
            }
        }
    }

    public void processIndication() {
        if (this.mIsStarted && (this.mRilStat & 1) != 0) {
            if ((this.mRilStat & 2) != 0) {
                notifyRadioStallChanged(0);
                this.mRilStat = 0;
            } else {
                this.mRilStat |= 4;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r1v3, types: [miui.telephony.MiuiRadioStallDetector$4] */
    public void triggerHeartBeat(int reason) {
        this.mHandler.removeMessages(4);
        this.mHeartBeatStatus.resetFlag();
        final int serial = (this.mHeartBeatStatus.mSerial + 1) % Integer.MAX_VALUE;
        this.mHeartBeatStatus.mSerial = serial;
        this.mHeartBeatStatus.mTriggerReason = reason;
        this.mHeartBeatStatus.mTimestamp = System.currentTimeMillis();
        logd("triggerHeartBeat " + this.mHeartBeatStatus.toString());
        if (MiuiTelephony.IS_QCOM) {
            new Thread() { // from class: miui.telephony.MiuiRadioStallDetector.4
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    MiuiRadioStallDetector.this.acquireWakeLock();
                    boolean zQueryRilStallStats = MiuiRadioStallDetector.this.mRil.queryRilStallStats(serial, (byte[]) null, (Message) null);
                    MiuiRadioStallDetector miuiRadioStallDetector = MiuiRadioStallDetector.this;
                    if (zQueryRilStallStats) {
                        miuiRadioStallDetector.mHandler.sendMessageDelayed(MiuiRadioStallDetector.this.mHandler.obtainMessage(4, serial, 0), 60000L);
                        MiuiRadioStallDetector.this.mHandler.sendMessage(MiuiRadioStallDetector.this.mHandler.obtainMessage(2, serial, 0));
                    } else {
                        miuiRadioStallDetector.clearWakeLock();
                        MiuiRadioStallDetector.this.loge("failed to invoke queryRilStallStats!");
                    }
                }
            }.start();
            return;
        }
        if (MiuiTelephony.IS_XRING) {
            Message msg = this.mHandler.obtainMessage(2, serial, 0);
            if (this.mRil.queryRilStallStats(serial, new String(EVENT_OEM_RIL_STALL_DETECTED).getBytes(), msg)) {
                this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(4, serial, 0), 60000L);
            } else {
                loge("failed to invoke queryRilStallStats!");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void evaluateHeartBeatResult(int serial, int heartBeatMsgType) {
        if (this.mHeartBeatStatus.mSerial != serial) {
            return;
        }
        if (heartBeatMsgType == 1) {
            this.mHeartBeatStatus.mResponseFlag = true;
        } else if (heartBeatMsgType == 2) {
            this.mHeartBeatStatus.mIndicationFlag = true;
        }
        if (this.mHeartBeatStatus.mResponseFlag && this.mHeartBeatStatus.mIndicationFlag) {
            clearWakeLock();
            this.mHandler.removeMessages(4);
            logd("evaluateHeartBeatResult success " + this.mHeartBeatStatus.toString());
            if ((1 & this.mRilStat) != 0) {
                notifyRadioStallChanged(0);
                this.mRilStat = 0;
            }
            recordStatistic(3, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startIndicationAlarm() {
        long alarmTime = System.currentTimeMillis() + ((long) RIL_INDICATION_TIME_OUT_MILLIS);
        this.mAlarmMgr.setExact(1, alarmTime, LOG_TAG, this.mAlarmListener, null);
    }

    private void stopIndicationAlarm() {
        this.mAlarmMgr.cancel(this.mAlarmListener);
    }

    private void resetIndicationAlarm() {
        stopIndicationAlarm();
        startIndicationAlarm();
    }

    private void recordStatistic(int resultCode, int failCode) {
        Intent intent = new Intent(CommenUtils.ACTION_RADIO_STALL_STATISTIC);
        intent.putExtra(CommenUtils.EXTRA_KEY_REDULT_CODE, resultCode);
        intent.putExtra(CommenUtils.EXTRA_KEY_FAILED_CODE, failCode);
        logd("recordStatistic, resultCode = " + resultCode + ", failedCode = " + failCode);
        this.mContext.sendBroadcast(intent, "android.permission.READ_PRIVILEGED_PHONE_STATE");
    }

    private void notifyRadioStallChanged(int status) {
        Intent intent = new Intent(CommenUtils.ACTION_RADIO_STALL_DETECT);
        intent.putExtra(CommenUtils.EXTRA_STATUS, status);
        this.mContext.sendBroadcast(intent, "android.permission.READ_PRIVILEGED_PHONE_STATE");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void acquireWakeLock() {
        synchronized (this.mWakeLock) {
            clearWakeLock();
            this.mWakeLock.acquire(60000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearWakeLock() {
        synchronized (this.mWakeLock) {
            if (this.mWakeLock.isHeld()) {
                this.mWakeLock.release();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String msg) {
        Log.d("MiuiRadioStallDetector[" + this.mPhoneId + "]", msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loge(String msg) {
        Log.e("MiuiRadioStallDetector[" + this.mPhoneId + "]", msg);
    }
}
