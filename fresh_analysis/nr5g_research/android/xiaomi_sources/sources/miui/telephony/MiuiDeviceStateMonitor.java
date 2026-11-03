package miui.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.android.internal.telephony.DeviceStateMonitor;
import com.android.internal.telephony.Phone;
import com.android.telephony.Rlog;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class MiuiDeviceStateMonitor {
    public static final String ACTION_SLEEP_CHANGED = "com.miui.powerkeeper_sleep_changed";
    private static final int EVENT_MIUI_SLEEP_MODE_CHANGED = 100;
    public static final int EVENT_SLEEP_MODE_CHANGED = 1;
    public static final String EXTRA_STATE = "state";
    public static final int STATE_ENTER_SLEEP = 1;
    public static final int STATE_EXIT_SLEEP = 2;
    private Context mContext;
    private Handler mHandler;
    private DeviceStateMonitor mMonitor;
    private Phone mPhone;
    private String LOG_TAG = "MiuiDeviceStateMonitor";
    private boolean mIsSleepMode = false;
    private ArrayList<MiuiDeviceStateListener> mDeviceStateListeners = new ArrayList<>();
    private boolean sIsScreenOn = true;
    private boolean sIsCharging = true;
    private boolean sIsPowerSaveOn = false;
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: miui.telephony.MiuiDeviceStateMonitor.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action;
            if (intent != null && (action = intent.getAction()) != null && action.equals(MiuiDeviceStateMonitor.ACTION_SLEEP_CHANGED)) {
                MiuiDeviceStateMonitor.this.mHandler.sendMessage(MiuiDeviceStateMonitor.this.mHandler.obtainMessage(1, intent));
            }
        }
    };

    public MiuiDeviceStateMonitor(Phone phone) {
        this.mPhone = phone;
    }

    public void registerDeviceStateListener(MiuiDeviceStateListener listener) {
        this.mDeviceStateListeners.add(listener);
    }

    public void unregisterDeviceStateListener(MiuiDeviceStateListener listener) {
        this.mDeviceStateListeners.remove(listener);
    }

    public void notifyDeviceStateListeners(boolean screenOn, boolean charging, boolean powerSaveOn) {
        this.sIsScreenOn = screenOn;
        this.sIsCharging = charging;
        this.sIsPowerSaveOn = powerSaveOn;
        for (MiuiDeviceStateListener listener : this.mDeviceStateListeners) {
            listener.onDeviceStateChange(screenOn, charging, powerSaveOn);
        }
    }

    public boolean shouldTurnOffServiceStateFrequentUpdate() {
        return this.sIsPowerSaveOn || !(this.sIsScreenOn || this.sIsCharging);
    }

    public String stateToString() {
        return "isScreenOn:" + this.sIsScreenOn + ", isCharging:" + this.sIsCharging + ", isPowerSaveOn:" + this.sIsPowerSaveOn;
    }

    public void init(Phone phone, DeviceStateMonitor monitor) {
        this.mContext = phone.getContext();
        this.LOG_TAG += "[" + phone.getPhoneId() + "]";
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SLEEP_CHANGED);
        this.mContext.registerReceiver(this.mBroadcastReceiver, filter, 2);
        HandlerThread mWorkThread = new HandlerThread("MiuiDeviceStateMonitor");
        mWorkThread.start();
        this.mHandler = new WorkHandler(mWorkThread.getLooper());
        this.mMonitor = monitor;
    }

    class WorkHandler extends Handler {
        public WorkHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Intent intent = (Intent) msg.obj;
                    MiuiDeviceStateMonitor.this.mIsSleepMode = 1 == intent.getIntExtra("state", 2);
                    if (MiuiDeviceStateMonitor.this.isSignalStrengthReportEnabled()) {
                        Rlog.d(MiuiDeviceStateMonitor.this.LOG_TAG, "sleep mode : " + MiuiDeviceStateMonitor.this.mIsSleepMode);
                        MiuiDeviceStateMonitor.this.notifyDeviceStateMonitorToUpdate();
                    }
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDeviceStateMonitorToUpdate() {
        if (this.mMonitor != null) {
            this.mMonitor.sendMessage(this.mMonitor.obtainMessage(100));
        }
    }

    public boolean isNeedUpdateDeviceState(int what) {
        if (what == 100 && isSignalStrengthReportEnabled()) {
            return true;
        }
        return false;
    }

    public int generatorDeviceStateNewFilter(int what, int newFilter) {
        if (this.mIsSleepMode && isSignalStrengthReportEnabled()) {
            Rlog.d(this.LOG_TAG, "restrict SIGNAL_STRENGTH report in sleep mode");
            return newFilter & (-2);
        }
        return newFilter;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSignalStrengthReportEnabled() {
        return TelephonyOptCloudController.CLOUD_CONTROLLER_SIGNAL_STRENGTH_REPORT_OPTIMIZATION_ENABLED;
    }
}
