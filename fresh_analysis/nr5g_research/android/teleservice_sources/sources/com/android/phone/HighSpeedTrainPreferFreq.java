package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.android.phone.statistics.HighSpeedTrainStats;
import com.ot.pubsub.a.a;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import miui.telephony.DefaultSimManager;
import miui.telephony.ServiceProviderUtils;

/* JADX INFO: loaded from: classes.dex */
public class HighSpeedTrainPreferFreq {
    private static final Pattern AT_CMD_PATTERN = Pattern.compile("AT\\+EPLMNFREQ=1,(?:4|128),\"(\\d{5,6})\",(\\d+),((?:\\d+)(?:,\\d+)*)");
    private static volatile HighSpeedTrainPreferFreq mHighSpeedTrainPreferFreq;
    private HighSpeedTrainPreferFreqHandler mHandler;
    private volatile boolean mIsHstPreferFreqCloudOn;
    private HandlerThread mWorkThread;
    private volatile boolean mCurrentHstState = false;
    private volatile boolean DEBUG = false;
    private int slot = 0;
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.HighSpeedTrainPreferFreq.1
        private final WeakReference outer;

        {
            this.outer = new WeakReference(HighSpeedTrainPreferFreq.this);
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            HighSpeedTrainPreferFreq.this.log("receive action".concat(action));
            if ("com.android.phone.intent.action.HIGH_SPEED_TRAIN_PREFER_FREQ_DEBUG_ALL".equals(action)) {
                HighSpeedTrainPreferFreq.this.mHandler.obtainMessage(2, intent).sendToTarget();
                return;
            }
            if ("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED".equals(action)) {
                HighSpeedTrainPreferFreq.this.mCurrentHstState = intent.getBooleanExtra("mIsHstMode", false);
                HighSpeedTrainPreferFreq.this.logD("mCurrentHstState = " + HighSpeedTrainPreferFreq.this.mCurrentHstState);
                return;
            }
            if ("com.android.phone.intent.action.HIT_HST_LINE_STATUS_CHANGED".equals(action)) {
                HighSpeedTrainPreferFreq.this.mHandler.obtainMessage(1, intent).sendToTarget();
                return;
            }
            if (action.equals("com.android.phone.intent.action.CLOUD_HST_PREFER_FREQ_SWITCH")) {
                HighSpeedTrainPreferFreq highSpeedTrainPreferFreq = HighSpeedTrainPreferFreq.this;
                highSpeedTrainPreferFreq.mIsHstPreferFreqCloudOn = highSpeedTrainPreferFreq.getHstPreferFreqSwitch();
                HighSpeedTrainPreferFreq.this.logD("mIsHstPreferFreqCloudOn = " + HighSpeedTrainPreferFreq.this.mIsHstPreferFreqCloudOn);
            }
        }
    };
    private volatile Context mContext = PhoneGlobals.getInstance();

    private HighSpeedTrainPreferFreq() {
        HandlerThread handlerThread = new HandlerThread("HighSpeedTrainPreferFreq", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mHandler = new HighSpeedTrainPreferFreqHandler(this.mWorkThread.getLooper(), this);
        this.mIsHstPreferFreqCloudOn = getHstPreferFreqSwitch();
        registerBroadcastReceiver();
    }

    public static HighSpeedTrainPreferFreq getInstance() {
        if (mHighSpeedTrainPreferFreq == null) {
            synchronized (HighSpeedTrainPreferFreq.class) {
                try {
                    if (mHighSpeedTrainPreferFreq == null) {
                        mHighSpeedTrainPreferFreq = new HighSpeedTrainPreferFreq();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return mHighSpeedTrainPreferFreq;
    }

    class HighSpeedTrainPreferFreqHandler extends Handler {
        private final WeakReference mOuter;

        HighSpeedTrainPreferFreqHandler(Looper looper, HighSpeedTrainPreferFreq highSpeedTrainPreferFreq) {
            super(looper);
            this.mOuter = new WeakReference(highSpeedTrainPreferFreq);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            HighSpeedTrainPreferFreq highSpeedTrainPreferFreq = (HighSpeedTrainPreferFreq) this.mOuter.get();
            if (highSpeedTrainPreferFreq == null) {
                return;
            }
            int i = message.what;
            if (i == 1) {
                Object obj = message.obj;
                if (obj instanceof Intent) {
                    highSpeedTrainPreferFreq.processHstLineStateChangeEvent((Intent) obj);
                    return;
                }
                return;
            }
            if (i != 2) {
                return;
            }
            Intent intent = (Intent) message.obj;
            highSpeedTrainPreferFreq.DEBUG = true;
            if (intent != null) {
                String stringExtra = intent.getStringExtra("name");
                intent.getStringExtra(a.J);
                highSpeedTrainPreferFreq.getPreferFreqData(stringExtra);
            }
        }
    }

    private void registerBroadcastReceiver() {
        synchronized (this) {
            try {
                if (this.mContext == null) {
                    return;
                }
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("com.android.phone.intent.action.HIGH_SPEED_TRAIN_PREFER_FREQ_DEBUG_ALL");
                intentFilter.addAction("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED");
                intentFilter.addAction("com.android.phone.intent.action.HIT_HST_LINE_STATUS_CHANGED");
                intentFilter.addAction("com.android.phone.intent.action.CLOUD_HST_PREFER_FREQ_SWITCH");
                this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processHstLineStateChangeEvent(Intent intent) {
        if (!this.mIsHstPreferFreqCloudOn || intent == null || !this.mCurrentHstState) {
            logD("processHstLine mCurrentHstState = " + this.mCurrentHstState);
            return;
        }
        Map map = (Map) intent.getSerializableExtra("mHitHstLineStatus");
        if (map != null) {
            logD("lineDirections = " + map.toString());
            Iterator it = map.keySet().iterator();
            while (it.hasNext()) {
                getPreferFreqData((String) it.next());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getPreferFreqData(String str) {
        if (str == null) {
            logD("getPreferFreqData: line is null");
            return;
        }
        int defaultDataSlotId = getDefaultDataSlotId();
        if (!MiuiPhoneUtils.isValidatePhoneId(defaultDataSlotId)) {
            logD("getPreferFreqData, invalid phoneId");
            return;
        }
        String networkNumeric = MiuiPhoneUtils.getNetworkNumeric(defaultDataSlotId);
        if (TextUtils.isEmpty(networkNumeric)) {
            logD("getPreferFreqData, plmn is empty");
            return;
        }
        String strOperatorForDds = operatorForDds(defaultDataSlotId);
        logD("getPreferFreqData line = " + str + " plmn = " + networkNumeric + " operator = " + strOperatorForDds);
        NetworkRecoveryStrategyImpl.HstFrequencyData hstFrequencyData = NetworkRecoveryStrategyImpl.getHstFrequencyData(str, strOperatorForDds);
        if (hstFrequencyData != null) {
            setPreferFreqCmd(hstFrequencyData.lteData, hstFrequencyData.nrData, networkNumeric);
        } else {
            logD("NO Hst Line match: ".concat(str));
        }
    }

    private String operatorForDds(int i) {
        String str = "INVALID";
        if (MiuiPhoneUtils.isValidatePhoneId(i)) {
            String networkNumeric = MiuiPhoneUtils.getNetworkNumeric(i);
            if (!TextUtils.isEmpty(networkNumeric)) {
                if (ServiceProviderUtils.isChinaMobile(networkNumeric)) {
                    str = "CM";
                } else if (ServiceProviderUtils.isChinaUnicom(networkNumeric)) {
                    str = "CU";
                } else if (ServiceProviderUtils.isChinaTelecom(networkNumeric)) {
                    str = "CT";
                } else if (MiuiPhoneUtils.isGermanyDTByVPLMN(networkNumeric)) {
                    str = "GM_DT";
                } else if (MiuiPhoneUtils.isSpainVdfByVPLMN(networkNumeric)) {
                    str = "SP_VDF";
                } else if (MiuiPhoneUtils.isSpainMasByVPLMN(networkNumeric)) {
                    str = "SP_MAS";
                } else if (MiuiPhoneUtils.isFranceMasByVPLMN(networkNumeric)) {
                    str = "FR_MAS";
                }
                logD("operatorForDds numeric=" + networkNumeric + " operator=" + str);
                return str;
            }
        }
        return "INVALID";
    }

    private void setPreferFreqCmd(List list, List list2, String str) {
        if (list != null) {
            processCellData(list, str, "AT+EPLMNFREQ=1,4,\"");
        }
        if (list2 != null) {
            processCellData(list2, str, "AT+EPLMNFREQ=1,128,\"");
        }
    }

    private void processCellData(List list, String str, String str2) {
        String strValidateAndJoinFreq = validateAndJoinFreq(list);
        if (strValidateAndJoinFreq == null || strValidateAndJoinFreq.isEmpty() || TextUtils.isEmpty(str)) {
            logD("processCellData: No valid " + str2 + " frequencies found");
            return;
        }
        String str3 = str2 + str + "\"," + list.size() + "," + strValidateAndJoinFreq;
        if (!isValidCommand(str3)) {
            logD("processCellData: invalid command format");
            return;
        }
        sendAtCommand(this.slot, new String[]{str3, ""}, null);
        HighSpeedTrainStats.recordPreferFreq();
        log("setPreferFreqCmd ".concat(str3));
    }

    private String validateAndJoinFreq(List list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            List list2 = (List) it.next();
            if (list2 != null && list2.size() > 3 && list2.get(3) != null) {
                String str = (String) list2.get(3);
                if (!str.matches("\\d+")) {
                    logD("setPreferFreqCmd: freq contains non-digit");
                    return null;
                }
                arrayList.add(str);
            }
        }
        return String.join(",", arrayList);
    }

    private boolean isValidCommand(String str) {
        if (str == null) {
            return false;
        }
        if (AT_CMD_PATTERN.matcher(str).matches()) {
            return true;
        }
        log("isValidCommand: format mismatch");
        return false;
    }

    private void sendAtCommand(int i, String[] strArr, Message message) {
        if (strArr == null) {
            log("sendAtCommand: cmd is null or object destroyed");
            return;
        }
        try {
            FiveGManagerBase.getInstance().sendAtCommand(i, strArr, message);
            logD("sendAtCommand: " + String.join(" ", strArr));
        } catch (Exception e) {
            log("Error sending AT command: " + e.getMessage());
        }
    }

    private int getDefaultDataSlotId() {
        DefaultSimManager.getInstance();
        return DefaultSimManager.getDefaultDataSlotId();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getHstPreferFreqSwitch() {
        if (this.mContext == null) {
            return false;
        }
        boolean hstPreferFreqSwitchByCloud = FiveGManagerBase.getHstPreferFreqSwitchByCloud(this.mContext);
        log("getHstPreferFreqSwitch = " + hstPreferFreqSwitchByCloud);
        return hstPreferFreqSwitchByCloud;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("HighSpeedTrainPreferFreq", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logD(String str) {
        if (this.DEBUG) {
            Log.d("HighSpeedTrainPreferFreq", str);
        }
    }
}
