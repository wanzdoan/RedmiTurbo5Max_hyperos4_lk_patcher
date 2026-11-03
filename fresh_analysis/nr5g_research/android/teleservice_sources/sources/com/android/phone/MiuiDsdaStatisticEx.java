package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.text.TextUtils;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.data.PhoneSwitcher;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.statistics.MiuiDsdaStatistic;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.HashMap;
import miui.telephony.TelephonyManagerEx;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class MiuiDsdaStatisticEx extends MiuiDsdaStatistic implements TelephonyStatAdapter.ICallBack {
    private String[] mBands;
    private long mBandsResidentEnterTime;
    private Context mContext;
    private boolean mCurrentDsdaState;
    private long mDsdaConnStateEnterTime;
    private long mDsdaConnStateTimeDuration;
    private long mDsdaEnterTime;
    private long mDsdaTimeDuration;
    private int mEnterDsdaCnt;
    private int mEnterDsdaConnStateCnt;
    private final Handler mHandler;
    private long mLastRatRecordTime;
    private long mLastRecordTime;
    private long mLastRptTime;
    private String[] mRat;
    private long mRatResidentEnterTime;
    private final BroadcastReceiver mReceiver;
    private long mTxConcurrencyEnterTime;
    private final HandlerThread mWorkThread;
    private final HashMap mStatsMap = new HashMap();
    private int mCurrentDsdaMode = 0;
    private int mDsdaSubState = -1;
    private int mTxConcurrency = -1;
    private int mDsdaSubMode = -1;
    private int mPreDsdaMode = 0;
    private String[] mCurrentBands = {"", ""};
    private String[] mPreBands = {"", ""};
    private String[] mPreRat = {"", ""};
    private final HashMap mStatsBandsMap = new HashMap();
    private final HashMap mTxConcurrencyMap = new HashMap();

    public static MiuiDsdaStatistic make() {
        if (MiuiPhoneUtils.isMtkDx1Platform() || MiuiPhoneUtils.isDeviceDsdaSupportedByQcom()) {
            MiuiDsdaStatistic.mInstance = new MiuiDsdaStatisticEx();
        }
        return MiuiDsdaStatistic.mInstance;
    }

    private MiuiDsdaStatisticEx() {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mBands = new String[i];
        this.mRat = new String[i];
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.MiuiDsdaStatisticEx.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Utils.logD("MiuiDsdaStatistic", "mReceiver action =" + action);
                if (action.equals("org.codeaurora.intent.action.MSIM_VOICE_CAPABILITY_CHANGED")) {
                    MiuiDsdaStatisticEx.this.mHandler.sendEmptyMessage(4);
                } else if (action.equals("qualcomm.intent.action.ACTION_MSIM_SUB_MODE_IND")) {
                    if (MiuiDsdaStatisticEx.this.mHandler.hasMessages(6)) {
                        MiuiDsdaStatisticEx.this.mHandler.removeMessages(6);
                    }
                    MiuiDsdaStatisticEx.this.mHandler.sendMessageDelayed(MiuiDsdaStatisticEx.this.mHandler.obtainMessage(6, intent), 200L);
                }
            }
        };
        this.mReceiver = broadcastReceiver;
        this.mContext = PhoneGlobals.getInstance();
        HandlerThread handlerThread = new HandlerThread("MiuiDsdaStatistic", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mHandler = new WorkHandler(handlerThread.getLooper());
        registerDsdaChange();
        if (ModemProvider.IS_QCOM) {
            initData();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("org.codeaurora.intent.action.MSIM_VOICE_CAPABILITY_CHANGED");
            intentFilter.addAction("qualcomm.intent.action.ACTION_MSIM_SUB_MODE_IND");
            this.mContext.registerReceiver(broadcastReceiver, intentFilter, 2);
        }
        this.mLastRecordTime = SystemClock.elapsedRealtime();
        TelephonyStatAdapter.registerCallBack(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initData() {
        this.mCurrentDsdaState = false;
        this.mEnterDsdaCnt = 0;
        this.mDsdaEnterTime = 0L;
        this.mDsdaTimeDuration = 0L;
        this.mBandsResidentEnterTime = 0L;
        this.mRatResidentEnterTime = 0L;
        this.mEnterDsdaConnStateCnt = 0;
        this.mDsdaConnStateEnterTime = 0L;
        this.mDsdaConnStateTimeDuration = 0L;
        this.mTxConcurrencyEnterTime = 0L;
        this.mLastRptTime = SystemClock.elapsedRealtime();
        this.mStatsBandsMap.clear();
        this.mTxConcurrencyMap.clear();
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            this.mBands[i] = "Unknown";
            this.mRat[i] = "Unknown";
        }
        if (TelephonyManagerEx.getDefault().isConcurrentCallsPossible()) {
            this.mCurrentDsdaState = true;
            this.mEnterDsdaCnt = 1;
            this.mDsdaEnterTime = SystemClock.elapsedRealtime();
            this.mBands = recordBands();
            this.mRat = recordRat();
            this.mBandsResidentEnterTime = SystemClock.elapsedRealtime();
            this.mRatResidentEnterTime = SystemClock.elapsedRealtime();
        }
        if (this.mDsdaSubState == 1) {
            this.mDsdaConnStateEnterTime = SystemClock.elapsedRealtime();
            this.mTxConcurrencyEnterTime = SystemClock.elapsedRealtime();
        }
        log("init data done!");
    }

    private void registerDsdaChange() {
        Phone phone = PhoneFactory.getPhone(0);
        if (phone != null) {
            phone.mCi.registerForDsdaStateChanged(this.mHandler, 4, phone);
        }
    }

    @Override // com.android.phone.statistics.MiuiDsdaStatistic
    public void reportStatistics() {
        if (ModemProvider.IS_QCOM) {
            reportStatisticsForQ();
        } else {
            reportStatisticsForM();
        }
    }

    private void reportStatisticsForM() {
        if (MiuiPhoneUtils.getInsertedSimCount() < 2) {
            log("skip reporting");
            return;
        }
        recordPrevDsdaStats();
        Utils.logD("MiuiDsdaStatistic", "mStatsMap=" + this.mStatsMap);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "mtkDsda", this.mStatsMap);
        this.mHandler.sendEmptyMessageDelayed(5, 2000L);
    }

    private void reportStatisticsForQ() {
        if (MiuiPhoneUtils.getInsertedSimCount() < 2) {
            log("skip reporting for single sim inserted.");
            this.mHandler.sendEmptyMessageDelayed(5, 2000L);
            return;
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        HashMap map = new HashMap();
        map.put("dsdaCnt", String.valueOf(this.mEnterDsdaCnt));
        map.put("dsdaConnCnt", String.valueOf(this.mEnterDsdaConnStateCnt));
        if (TelephonyManagerEx.getDefault().isConcurrentCallsPossible()) {
            Utils.logD("MiuiDsdaStatistic", "reportStatistics current : " + jElapsedRealtime + " , dsda enter : " + this.mDsdaEnterTime + " , last dsda dur : " + this.mDsdaTimeDuration);
            this.mDsdaTimeDuration = this.mDsdaTimeDuration + (jElapsedRealtime - this.mDsdaEnterTime);
            recordBandsResidentDsdaDuration();
            recordRatResidentDsdaDuration();
        }
        if (this.mDsdaSubState == 1 && this.mDsdaConnStateEnterTime >= 0) {
            Utils.logD("MiuiDsdaStatistic", "reportStatistics current : " + jElapsedRealtime + " , connected state enter : " + this.mDsdaConnStateEnterTime + " , last connected dur : " + this.mDsdaConnStateTimeDuration);
            this.mDsdaConnStateTimeDuration = this.mDsdaConnStateTimeDuration + (jElapsedRealtime - this.mDsdaConnStateEnterTime);
            recordTxConcurrencyDuration();
        }
        long j = this.mDsdaTimeDuration / 1000;
        long j2 = this.mDsdaConnStateTimeDuration / 1000;
        Utils.logD("MiuiDsdaStatistic", "reportStatistics current : " + jElapsedRealtime + " , last report : " + this.mLastRptTime + " , dsda dur : " + this.mDsdaTimeDuration + " , dsda connected dur : " + this.mDsdaConnStateTimeDuration);
        long j3 = ((jElapsedRealtime - this.mLastRptTime) - this.mDsdaTimeDuration) / 1000;
        map.put("dsda", String.valueOf(j));
        map.put("dsds", String.valueOf(j3));
        if (j2 >= 1) {
            map.put("dsda_connected", String.valueOf(j2));
        }
        map.put("dsdaEnabled", ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isDsdaEnabled() ? "1" : "0");
        for (String str : this.mStatsBandsMap.keySet()) {
            Long l = (Long) this.mStatsBandsMap.get(str);
            if (l != null && l.longValue() >= 3000) {
                map.put(str, String.valueOf(l.longValue() / 1000));
            }
        }
        for (String str2 : this.mTxConcurrencyMap.keySet()) {
            Long l2 = (Long) this.mTxConcurrencyMap.get(str2);
            if (l2 != null && l2.longValue() >= 1000) {
                map.put(str2, String.valueOf(l2.longValue() / 1000));
            }
        }
        Utils.logD("MiuiDsdaStatistic", "reportStatistics :" + map);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "QcomDsda", map);
        this.mHandler.sendEmptyMessageDelayed(5, 2000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String[] recordBands() {
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        String[] strArr = {"", ""};
        if (activeDataPhone != null) {
            int phoneId = activeDataPhone.getPhoneId();
            int i = phoneId == 0 ? 1 : 0;
            strArr[0] = Utils.getBand(phoneId, activeDataPhone.getServiceState());
            Phone phone = PhoneFactory.getPhone(i);
            if (phone != null) {
                strArr[1] = Utils.getBand(i, phone.getServiceState());
            }
        }
        return strArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String[] recordRat() {
        String[] strArr = new String[2];
        strArr[0] = "Unknown";
        strArr[1] = "Unknown";
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            Phone phone = MiuiPhoneUtils.getPhone(i);
            if (phone != null) {
                strArr[i] = ServiceState.rilRadioTechnologyToString(Utils.getVoiceRat(phone.getServiceState()));
            }
        }
        return strArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Rlog.d("MiuiDsdaStatistic", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long recordPrevDsdaStats() {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        recordPrevDsdaRat(jElapsedRealtime);
        if (TextUtils.isEmpty(this.mPreBands[0]) || TextUtils.isEmpty(this.mPreBands[1])) {
            log("recordDsdaStats bands is empty, return.");
            this.mLastRecordTime = jElapsedRealtime;
            return jElapsedRealtime;
        }
        String str = this.mPreBands[0] + "_" + this.mPreBands[1] + "_" + this.mPreDsdaMode;
        this.mStatsMap.put(str, Long.valueOf((this.mStatsMap.containsKey(str) ? ((Long) this.mStatsMap.get(str)).longValue() : 0L) + ((jElapsedRealtime - this.mLastRecordTime) / 1000)));
        Utils.logD("MiuiDsdaStatistic", "recordDsdaStats " + str + " " + this.mStatsMap.get(str));
        this.mLastRecordTime = jElapsedRealtime;
        return jElapsedRealtime;
    }

    private long recordPrevDsdaRat(long j) {
        if (TextUtils.isEmpty(this.mPreRat[0]) || TextUtils.isEmpty(this.mPreRat[1])) {
            log("recordPrevDsdaRat is empty, return.");
            this.mLastRatRecordTime = j;
            return j;
        }
        String str = this.mPreRat[0] + "_" + this.mPreRat[1] + "_" + this.mPreDsdaMode;
        this.mStatsMap.put(str, Long.valueOf((this.mStatsMap.containsKey(str) ? ((Long) this.mStatsMap.get(str)).longValue() : 0L) + ((j - this.mLastRatRecordTime) / 1000)));
        Utils.logD("MiuiDsdaStatistic", "recordDsdaStats " + str + " " + this.mStatsMap.get(str));
        this.mLastRatRecordTime = j;
        return j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendDsdaModem2ModemEnhance(int i) {
        int i2 = i / 100;
        log("sendDsdaModem2ModemEnhance dsdaMode=" + i2);
        Intent intent = new Intent("com.android.phone.intent.action.DSDA_STATE_CHANGED");
        intent.putExtra("dsdaState", i2);
        this.mContext.sendBroadcast(intent, "android.permission.READ_PRIVILEGED_PHONE_STATE");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long recordBandsResidentDsdaDuration() {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (TextUtils.isEmpty(this.mBands[0]) && TextUtils.isEmpty(this.mBands[1])) {
            log("recordBandsResidentDsdaDuration bands is empty, return.");
            return jElapsedRealtime;
        }
        String str = this.mBands[0] + "_" + this.mBands[1];
        long jLongValue = this.mStatsBandsMap.containsKey(str) ? ((Long) this.mStatsBandsMap.get(str)).longValue() : 0L;
        Utils.logD("MiuiDsdaStatistic", "recordBandsResidentDsdaDuration pre :" + jLongValue + " , current : " + jElapsedRealtime + " , bands enter : " + this.mBandsResidentEnterTime);
        this.mStatsBandsMap.put(str, Long.valueOf((jLongValue + jElapsedRealtime) - this.mBandsResidentEnterTime));
        StringBuilder sb = new StringBuilder("recordBandsResidentDsdaDuration ");
        sb.append(str);
        sb.append(" ");
        sb.append(this.mStatsBandsMap.get(str));
        log(sb.toString());
        return jElapsedRealtime;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long recordRatResidentDsdaDuration() {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (TextUtils.isEmpty(this.mRat[0]) && TextUtils.isEmpty(this.mRat[1])) {
            log("recordRatResidentDsdaDuration rat is empty, return.");
            return jElapsedRealtime;
        }
        String str = this.mRat[0] + "_" + this.mRat[1];
        long jLongValue = this.mStatsBandsMap.containsKey(str) ? ((Long) this.mStatsBandsMap.get(str)).longValue() : 0L;
        Utils.logD("MiuiDsdaStatistic", "recordRatResidentDsdaDuration pre :" + jLongValue + " , current : " + jElapsedRealtime + " , bands enter : " + this.mRatResidentEnterTime);
        this.mStatsBandsMap.put(str, Long.valueOf((jLongValue + jElapsedRealtime) - this.mRatResidentEnterTime));
        StringBuilder sb = new StringBuilder("recordRatResidentDsdaDuration ");
        sb.append(str);
        sb.append(" ");
        sb.append(this.mStatsBandsMap.get(str));
        log(sb.toString());
        return jElapsedRealtime;
    }

    public void onServiceStateChanged(int i, ServiceState serviceState) {
        this.mHandler.sendEmptyMessage(3);
    }

    @Override // com.android.phone.statistics.MiuiDsdaStatistic
    public String[] getCurrentBands() {
        return this.mCurrentBands;
    }

    class WorkHandler extends Handler {
        WorkHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 3) {
                onServiceStateChanged();
                return;
            }
            if (i == 4) {
                onDsdaStateChanged(message);
                return;
            }
            if (i != 5) {
                if (i != 6) {
                    return;
                }
                MiuiDsdaStatisticEx.this.recordDsdaRealState((Intent) message.obj);
            } else if (ModemProvider.IS_MTK) {
                MiuiDsdaStatisticEx.this.mStatsMap.clear();
            } else if (ModemProvider.IS_QCOM) {
                MiuiDsdaStatisticEx.this.initData();
            }
        }

        private void onServiceStateChanged() {
            if (ModemProvider.IS_MTK) {
                MiuiDsdaStatisticEx miuiDsdaStatisticEx = MiuiDsdaStatisticEx.this;
                miuiDsdaStatisticEx.mCurrentBands = miuiDsdaStatisticEx.recordBands();
                MiuiDsdaStatisticEx.this.recordPrevDsdaStats();
                MiuiDsdaStatisticEx miuiDsdaStatisticEx2 = MiuiDsdaStatisticEx.this;
                miuiDsdaStatisticEx2.mPreBands = miuiDsdaStatisticEx2.mCurrentBands;
                MiuiDsdaStatisticEx miuiDsdaStatisticEx3 = MiuiDsdaStatisticEx.this;
                miuiDsdaStatisticEx3.mPreRat = miuiDsdaStatisticEx3.recordRat();
                return;
            }
            if (ModemProvider.IS_QCOM && MiuiDsdaStatisticEx.this.mCurrentDsdaState) {
                String[] strArrRecordBands = MiuiDsdaStatisticEx.this.recordBands();
                String str = strArrRecordBands[0];
                if (str != null && strArrRecordBands[1] != null && (!str.equals(MiuiDsdaStatisticEx.this.mBands[0]) || !strArrRecordBands[1].equals(MiuiDsdaStatisticEx.this.mBands[1]))) {
                    MiuiDsdaStatisticEx.this.log("service state of bands changed");
                    MiuiDsdaStatisticEx miuiDsdaStatisticEx4 = MiuiDsdaStatisticEx.this;
                    miuiDsdaStatisticEx4.mBandsResidentEnterTime = miuiDsdaStatisticEx4.recordBandsResidentDsdaDuration();
                    MiuiDsdaStatisticEx.this.mBands = strArrRecordBands;
                }
                String[] strArrRecordRat = MiuiDsdaStatisticEx.this.recordRat();
                String str2 = strArrRecordRat[0];
                if (str2 == null || strArrRecordRat[1] == null) {
                    return;
                }
                if (str2.equals(MiuiDsdaStatisticEx.this.mRat[0]) && strArrRecordRat[1].equals(MiuiDsdaStatisticEx.this.mRat[1])) {
                    return;
                }
                MiuiDsdaStatisticEx.this.log("service state of rat changed");
                MiuiDsdaStatisticEx miuiDsdaStatisticEx5 = MiuiDsdaStatisticEx.this;
                miuiDsdaStatisticEx5.mRatResidentEnterTime = miuiDsdaStatisticEx5.recordRatResidentDsdaDuration();
                MiuiDsdaStatisticEx.this.mRat = strArrRecordRat;
            }
        }

        private void onDsdaStateChanged(Message message) {
            Object obj;
            if (ModemProvider.IS_MTK) {
                AsyncResult asyncResult = (AsyncResult) message.obj;
                if (asyncResult == null || (obj = asyncResult.result) == null) {
                    return;
                }
                MiuiDsdaStatisticEx.this.mCurrentDsdaMode = ((Integer) obj).intValue();
                MiuiDsdaStatisticEx miuiDsdaStatisticEx = MiuiDsdaStatisticEx.this;
                miuiDsdaStatisticEx.sendDsdaModem2ModemEnhance(miuiDsdaStatisticEx.mCurrentDsdaMode);
                MiuiDsdaStatisticEx miuiDsdaStatisticEx2 = MiuiDsdaStatisticEx.this;
                miuiDsdaStatisticEx2.mCurrentBands = miuiDsdaStatisticEx2.recordBands();
                MiuiDsdaStatisticEx.this.recordPrevDsdaStats();
                MiuiDsdaStatisticEx miuiDsdaStatisticEx3 = MiuiDsdaStatisticEx.this;
                miuiDsdaStatisticEx3.mPreBands = miuiDsdaStatisticEx3.mCurrentBands;
                MiuiDsdaStatisticEx miuiDsdaStatisticEx4 = MiuiDsdaStatisticEx.this;
                miuiDsdaStatisticEx4.mPreDsdaMode = miuiDsdaStatisticEx4.mCurrentDsdaMode;
                MiuiDsdaStatisticEx miuiDsdaStatisticEx5 = MiuiDsdaStatisticEx.this;
                miuiDsdaStatisticEx5.mPreRat = miuiDsdaStatisticEx5.recordRat();
                return;
            }
            if (ModemProvider.IS_QCOM) {
                boolean zIsConcurrentCallsPossible = TelephonyManagerEx.getDefault().isConcurrentCallsPossible();
                MiuiDsdaStatisticEx.this.log("receive msim voice capability changed : " + zIsConcurrentCallsPossible);
                boolean z = MiuiDsdaStatisticEx.this.mCurrentDsdaState;
                MiuiDsdaStatisticEx miuiDsdaStatisticEx6 = MiuiDsdaStatisticEx.this;
                if (z == zIsConcurrentCallsPossible) {
                    miuiDsdaStatisticEx6.log("dsda state no changed, return");
                    return;
                }
                miuiDsdaStatisticEx6.mCurrentDsdaState = zIsConcurrentCallsPossible;
                boolean z2 = MiuiDsdaStatisticEx.this.mCurrentDsdaState;
                MiuiDsdaStatisticEx miuiDsdaStatisticEx7 = MiuiDsdaStatisticEx.this;
                if (z2) {
                    miuiDsdaStatisticEx7.mEnterDsdaCnt++;
                    MiuiDsdaStatisticEx.this.mDsdaEnterTime = SystemClock.elapsedRealtime();
                    MiuiDsdaStatisticEx miuiDsdaStatisticEx8 = MiuiDsdaStatisticEx.this;
                    miuiDsdaStatisticEx8.mBands = miuiDsdaStatisticEx8.recordBands();
                    MiuiDsdaStatisticEx.this.mBandsResidentEnterTime = SystemClock.elapsedRealtime();
                    MiuiDsdaStatisticEx miuiDsdaStatisticEx9 = MiuiDsdaStatisticEx.this;
                    miuiDsdaStatisticEx9.mRat = miuiDsdaStatisticEx9.recordRat();
                    MiuiDsdaStatisticEx.this.mRatResidentEnterTime = SystemClock.elapsedRealtime();
                    Utils.logD("MiuiDsdaStatistic", "dsda changed true, dsda enter :" + MiuiDsdaStatisticEx.this.mDsdaEnterTime + " , bands enter : " + MiuiDsdaStatisticEx.this.mBandsResidentEnterTime + " , rat enter : " + MiuiDsdaStatisticEx.this.mRatResidentEnterTime);
                    return;
                }
                Utils.logD("MiuiDsdaStatistic", "dsda changed false, old dsda duration :" + miuiDsdaStatisticEx7.mDsdaTimeDuration + " , cur time : " + SystemClock.elapsedRealtime() + " , dsda enter : " + MiuiDsdaStatisticEx.this.mDsdaEnterTime);
                MiuiDsdaStatisticEx miuiDsdaStatisticEx10 = MiuiDsdaStatisticEx.this;
                miuiDsdaStatisticEx10.mDsdaTimeDuration = miuiDsdaStatisticEx10.mDsdaTimeDuration + (SystemClock.elapsedRealtime() - MiuiDsdaStatisticEx.this.mDsdaEnterTime);
                MiuiDsdaStatisticEx.this.recordBandsResidentDsdaDuration();
                MiuiDsdaStatisticEx.this.recordRatResidentDsdaDuration();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordDsdaRealState(Intent intent) {
        byte[] byteArrayExtra = intent.getByteArrayExtra("BROADCAST_CONTEXT");
        if (byteArrayExtra == null) {
            Utils.logI("MiuiDsdaStatistic", "Invalid report, sub info is null");
            return;
        }
        if (byteArrayExtra.length != 24 && byteArrayExtra.length != 32) {
            Utils.logI("MiuiDsdaStatistic", "Invalid report, sub info length illegal, length=" + byteArrayExtra.length);
            return;
        }
        Utils.logI("MiuiDsdaStatistic", "DSDA state from HOOK:" + IccUtils.bytesToHexString(byteArrayExtra));
        int iBytesToInt = MiuiPhoneUtils.bytesToInt(byteArrayExtra, 4, 4);
        int iBytesToInt2 = MiuiPhoneUtils.bytesToInt(byteArrayExtra, 12, 4);
        int iBytesToInt3 = MiuiPhoneUtils.bytesToInt(byteArrayExtra, 20, 4);
        boolean z = (iBytesToInt == this.mDsdaSubMode && iBytesToInt2 == this.mTxConcurrency) ? false : true;
        this.mDsdaSubMode = iBytesToInt;
        int i = this.mDsdaSubState;
        if (iBytesToInt3 == 1) {
            if (iBytesToInt3 != i) {
                this.mEnterDsdaConnStateCnt++;
                this.mDsdaSubState = iBytesToInt3;
                this.mDsdaConnStateEnterTime = SystemClock.elapsedRealtime();
                this.mTxConcurrency = iBytesToInt2;
                this.mTxConcurrencyEnterTime = SystemClock.elapsedRealtime();
                Utils.logI("MiuiDsdaStatistic", "dsda connected state, connected state enter :" + this.mDsdaConnStateEnterTime + " , tx concurrency enter : " + this.mTxConcurrencyEnterTime);
            } else if (iBytesToInt2 != this.mTxConcurrency) {
                this.mTxConcurrencyEnterTime = recordTxConcurrencyDuration();
                this.mTxConcurrency = iBytesToInt2;
                Utils.logI("MiuiDsdaStatistic", "tx concurrency changed, enter :" + this.mTxConcurrency + " , time : " + this.mTxConcurrencyEnterTime);
            }
        } else if (i == 1) {
            this.mDsdaSubState = 0;
            Utils.logI("MiuiDsdaStatistic", "dsda non-connected state, old connected duration :" + this.mDsdaConnStateTimeDuration + " , cur time : " + SystemClock.elapsedRealtime() + " , connected state enter : " + this.mDsdaConnStateEnterTime);
            if (this.mDsdaConnStateEnterTime <= 0) {
                Utils.logD("MiuiDsdaStatistic", "Invalid dsda state enter time.");
                return;
            } else {
                this.mDsdaConnStateTimeDuration += SystemClock.elapsedRealtime() - this.mDsdaConnStateEnterTime;
                recordTxConcurrencyDuration();
            }
        } else {
            Utils.logD("MiuiDsdaStatistic", "Invalid report, last sub state: " + this.mDsdaSubState + " , cur sub state: " + iBytesToInt3);
        }
        if (z) {
            PhoneSwitcher phoneSwitcher = PhoneSwitcher.getInstance();
            Utils.logI("MiuiDsdaStatistic", "DSDA changed SubMode:" + this.mDsdaSubMode + " TxConcurrency:" + this.mTxConcurrency);
            if (phoneSwitcher != null) {
                phoneSwitcher.sendEmptyMessage(109);
            }
        }
    }

    private long recordTxConcurrencyDuration() {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (this.mTxConcurrency != -1) {
            if (this.mTxConcurrencyEnterTime > 0) {
                String translatedTxConcurrency = getTranslatedTxConcurrency();
                long jLongValue = this.mTxConcurrencyMap.containsKey(translatedTxConcurrency) ? ((Long) this.mTxConcurrencyMap.get(translatedTxConcurrency)).longValue() : 0L;
                Utils.logD("MiuiDsdaStatistic", "recordTxConcurrencyDuration pre :" + jLongValue + " , current : " + jElapsedRealtime + " , tx concurrency enter : " + this.mTxConcurrencyEnterTime);
                this.mTxConcurrencyMap.put(translatedTxConcurrency, Long.valueOf((jLongValue + jElapsedRealtime) - this.mTxConcurrencyEnterTime));
                StringBuilder sb = new StringBuilder("recordTxConcurrencyDuration ");
                sb.append(translatedTxConcurrency);
                sb.append(" ");
                sb.append(this.mTxConcurrencyMap.get(translatedTxConcurrency));
                log(sb.toString());
                return jElapsedRealtime;
            }
        }
        log("Invalid tx concurrency state or tx concurrency enter time, return.");
        return jElapsedRealtime;
    }

    private String getTranslatedTxConcurrency() {
        int i = this.mTxConcurrency;
        if (i == 0) {
            return "NAS_DSDA_FULL_CONCURRENCY";
        }
        if (i == 1) {
            return "NAS_DSDA_TX_SHARING";
        }
        if (i == 2) {
            return "NAS_DSDA_FASTSTREAM_GAIN";
        }
        return "UNDEFINED";
    }

    @Override // com.android.phone.statistics.MiuiDsdaStatistic
    public boolean isDsdaFullConcurrencyState() {
        int i;
        if (ModemProvider.IS_MTK) {
            return false;
        }
        if (ModemProvider.IS_QCOM) {
            return this.mDsdaSubMode == 1 && ((i = this.mTxConcurrency) == 0 || i == 2);
        }
        if (!ModemProvider.IS_XRING) {
            return false;
        }
        int i2 = SystemProperties.getInt("vendor.radio.dsda.state", 4);
        if (i2 > 100) {
            i2 /= 100;
        }
        return i2 == 2 || i2 == 3;
    }

    @Override // com.android.phone.statistics.MiuiDsdaStatistic
    public boolean isDsdaFullOrTxsConcurrencyState() {
        int i;
        return !ModemProvider.IS_MTK && this.mDsdaSubMode == 1 && ((i = this.mTxConcurrency) == 0 || i == 1 || i == 2);
    }

    @Override // com.android.phone.statistics.MiuiDsdaStatistic
    public int getDsdaType() {
        return this.mTxConcurrency;
    }
}
