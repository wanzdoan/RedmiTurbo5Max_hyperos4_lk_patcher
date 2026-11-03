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
import android.os.RemoteException;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.tcpka.IModemTcpKeepAliveIndCallback;
import com.android.internal.telephony.LocalLog;
import com.xiaomi.internal.telephony.DynamicSarService;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class ModemTcpKaBase {
    private static final String TAG = "ModemTcpKaBase";
    protected static volatile ModemTcpKaBase mInstance;
    protected static final LocalLog mLocalLog = new LocalLog(DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
    public static volatile boolean sCloudDisabled;
    protected Context mContext;
    protected Map mDataTackingMap;
    protected Map mDataToClientMap;
    protected int mDdsPhoneId;
    protected List mDtKaExcList;
    protected DataTrackingHelper mDth;
    protected volatile boolean mFlgWatingKaInd;
    protected Handler mHandler;
    private final BroadcastReceiver mIntentReceiver;
    protected int mPhoneCount;
    protected List mStartMonitorPhoneIdList;
    protected SubscriptionManager mSubscriptionManager;
    protected HandlerThread mWorkThread;
    protected boolean mTcpKaRegisterFlag = false;
    protected List mListeners = new ArrayList();
    protected int mDlyKaTrigCnt = 0;
    protected int mDlyKaTrigFailCnt = 0;
    protected int mDlyKaTrigOkCnt = 0;
    protected int mDlyKaTrigOkThenFailCnt = 0;
    protected int mDlyKaTrigNoIndCnt = 0;
    protected int mDlyKaTrigFailReason = 0;
    protected int mDlyKaDur = 0;
    protected int mDlyKaDurMin = 0;
    protected int mDlyKaDurMax = 0;
    protected int mRatWhenKaFail = 0;
    protected int mRsrpWhenKaFail = 0;
    protected String mCarrWhenKaFail = "";
    protected volatile long mKaStartTime = 0;
    protected volatile long mKaStopTime = 0;

    protected void onTcpKeepAliveInd(AsyncResult asyncResult) {
    }

    protected void recordStatisticsEvent() {
    }

    public int startTcpKeepAlive(String str, int i, String str2, int i2, int i3, int i4) {
        return 1;
    }

    public int startTcpMonitorRegister(String str, int i, String str2, int i2, int i3) {
        return 1;
    }

    public int stopTcpKeepAlive(int i) {
        return 1;
    }

    public int stopTcpMonitorRegister(int i) {
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX INFO: renamed from: tcpMonitorRelease, reason: merged with bridge method [inline-methods] */
    public void lambda$onCloudChanged$0() {
    }

    public static ModemTcpKaBase getInstance() {
        return mInstance;
    }

    protected static void setInstance(ModemTcpKaBase modemTcpKaBase) {
        mInstance = modemTcpKaBase;
    }

    protected ModemTcpKaBase() {
        this.mPhoneCount = 0;
        this.mDdsPhoneId = 0;
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.ModemTcpKaBase.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                ModemTcpKaBase.logd("Received action: ".concat(action));
                if (action.equals("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED")) {
                    ModemTcpKaBase.this.mHandler.obtainMessage(2).sendToTarget();
                }
            }
        };
        this.mIntentReceiver = broadcastReceiver;
        try {
            this.mContext = PhoneGlobals.getInstance();
            HandlerThread handlerThread = new HandlerThread("ModemTcpKaBase");
            this.mWorkThread = handlerThread;
            handlerThread.start();
            this.mHandler = new WorkHandler(this.mWorkThread.getLooper());
            this.mSubscriptionManager = (SubscriptionManager) this.mContext.getSystemService("telephony_subscription_service");
            this.mPhoneCount = TelephonyManager.getDefault().getPhoneCount();
            this.mDdsPhoneId = DefaultSimManager.getDefaultDataSlotId();
            this.mDth = new DataTrackingHelper();
            this.mDataTackingMap = new LinkedHashMap();
            this.mDtKaExcList = new ArrayList();
            this.mDataToClientMap = new LinkedHashMap();
            this.mStartMonitorPhoneIdList = new ArrayList();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
            this.mContext.registerReceiver(broadcastReceiver, intentFilter);
            this.mDth.registerTelephonyCallBack();
        } catch (Exception e) {
            loge("Error initializing ModemTcpKeepAliveController: " + e.getMessage());
            throw new RuntimeException("ModemTcpKeepAliveController initialization failed", e);
        }
    }

    public class WorkHandler extends Handler {
        WorkHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            ModemTcpKaBase.logd("msg.what = " + i);
            if (i == 1) {
                ModemTcpKaBase.this.onTcpKeepAliveInd((AsyncResult) message.obj);
                return;
            }
            if (i == 2) {
                ModemTcpKaBase.this.lambda$onCloudChanged$0();
                ModemTcpKaBase.this.updateDefaultDataSlotId();
            } else if (i == 3) {
                ModemTcpKaBase.this.mDlyKaTrigNoIndCnt++;
            } else {
                if (i == 4) {
                    ModemTcpKaBase.this.dataTrackingResetData();
                    return;
                }
                ModemTcpKaBase.loge("unknown msg.what = " + i);
            }
        }
    }

    public class DataTrackingHelper {
        private ServiceState ddsPhoneServicesState;
        private int ddsRat;
        private int lteRsrp;
        private int nrRsrp;
        private TelephonyCallBack teleCd;

        private boolean isLteNwType(int i) {
            return i == 13 || i == 19;
        }

        private boolean isNrNwType(int i) {
            return i == 20 || i == 30;
        }

        protected DataTrackingHelper() {
        }

        public int getDdsRat() {
            ServiceState serviceState = this.ddsPhoneServicesState;
            if (serviceState == null) {
                ModemTcpKaBase.logd("DataTrackingHelper-getDdsRat, ddsPhoneServicesState is null");
                return 0;
            }
            this.ddsRat = serviceState.getDataNetworkType();
            ModemTcpKaBase.logd("DataTrackingHelper-getDdsRat, rat = " + this.ddsRat);
            if (isLteNwType(this.ddsRat) || isNrNwType(this.ddsRat)) {
                return this.ddsRat;
            }
            return 0;
        }

        public int getDdsRsrp() {
            if (isLteNwType(this.ddsRat)) {
                return this.lteRsrp;
            }
            if (isNrNwType(this.ddsRat)) {
                return this.nrRsrp;
            }
            return 0;
        }

        public void registerTelephonyCallBack() {
            ModemTcpKaBase.logd("registerTelephonyCallBack");
            if (this.teleCd == null) {
                this.teleCd = new TelephonyCallBack();
            }
            TelephonyStatAdapter.registerCallBack(this.teleCd);
        }

        public String getDdsPhoneCarrName() {
            return ((TelephonyManager) ModemTcpKaBase.this.mContext.getSystemService("phone")).getSimOperatorNameForPhone(ModemTcpKaBase.this.mDdsPhoneId);
        }

        class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
            TelephonyCallBack() {
            }

            public void onSignalStrengthChanged(int i, SignalStrength signalStrength) {
                if (i == ModemTcpKaBase.this.mDdsPhoneId && SubscriptionManager.isValidPhoneId(i)) {
                    for (CellSignalStrength cellSignalStrength : signalStrength.getCellSignalStrengths()) {
                        if (cellSignalStrength instanceof CellSignalStrengthLte) {
                            DataTrackingHelper.this.lteRsrp = ((CellSignalStrengthLte) cellSignalStrength).getRsrp();
                            ModemTcpKaBase.logd("onSignalStrengthChanged lteRsrp = " + DataTrackingHelper.this.lteRsrp);
                        } else if (cellSignalStrength instanceof CellSignalStrengthNr) {
                            DataTrackingHelper.this.nrRsrp = ((CellSignalStrengthNr) cellSignalStrength).getSsRsrp();
                            ModemTcpKaBase.logd("onSignalStrengthChanged nrRsrp = " + DataTrackingHelper.this.nrRsrp);
                        }
                    }
                }
            }

            public void onServiceStateChanged(int i, ServiceState serviceState) {
                if (i == ModemTcpKaBase.this.mDdsPhoneId && SubscriptionManager.isValidPhoneId(i)) {
                    DataTrackingHelper.this.ddsPhoneServicesState = serviceState;
                    ModemTcpKaBase.logd("onServiceStateChanged ddsPhoneServicesState = " + serviceState);
                }
            }
        }
    }

    protected void dataTrackingForKaInd(String str) {
        if (str == null) {
            return;
        }
        str.hashCode();
        switch (str) {
            case "KEEP_ALIVE_RESULT_OK":
                if (this.mFlgWatingKaInd) {
                    this.mDlyKaTrigOkCnt++;
                    this.mFlgWatingKaInd = false;
                    this.mKaStartTime = System.currentTimeMillis();
                    break;
                }
                break;
            case "KEEP_ALIVE_RESULT_ERROR":
                if (this.mFlgWatingKaInd) {
                    this.mDlyKaTrigFailCnt++;
                    this.mDlyKaTrigFailReason = 1;
                    dataTrackingUpdateKaExc();
                    this.mFlgWatingKaInd = false;
                    break;
                }
                break;
            case "KEEP_ALIVE_NETWORK_DOWN_ERROR":
                if (this.mFlgWatingKaInd) {
                    this.mDlyKaTrigFailCnt++;
                    this.mFlgWatingKaInd = false;
                } else {
                    this.mDlyKaTrigOkThenFailCnt++;
                }
                this.mDlyKaTrigFailReason = 2;
                dataTrackingUpdateKaExc();
                if (this.mKaStartTime != 0) {
                    this.mKaStopTime = System.currentTimeMillis();
                    dataTrackingForDuration();
                    break;
                }
                break;
            default:
                logd("unkown ka ind result");
                break;
        }
    }

    protected void dataTrackingForDuration() {
        if (this.mKaStartTime == 0 || this.mKaStopTime == 0 || this.mKaStartTime >= this.mKaStopTime) {
            loge("dataTrackingForDuration: duration error");
            return;
        }
        int i = (int) ((this.mKaStopTime - this.mKaStartTime) / 1000);
        if (this.mDlyKaDurMin == 0) {
            this.mDlyKaDurMin = i;
        }
        if (this.mDlyKaDurMax == 0) {
            this.mDlyKaDurMax = i;
        }
        this.mDlyKaDur += i;
        this.mDlyKaDurMin = Math.min(this.mDlyKaDurMin, i);
        this.mDlyKaDurMax = Math.max(this.mDlyKaDurMax, i);
    }

    protected void notifyListeners(String str) {
        try {
            Iterator it = this.mListeners.iterator();
            while (it.hasNext()) {
                try {
                    ((IModemTcpKeepAliveIndCallback) it.next()).onTcpKaInd(str);
                    logd("notify listener: " + str);
                    localLog("notify listener: " + str);
                } catch (RemoteException | IllegalArgumentException | IllegalStateException e) {
                    Rlog.e(TAG, "Exception in onTcpKaInd: " + e.getClass().getSimpleName(), e);
                    it.remove();
                }
            }
        } catch (Exception e2) {
            Rlog.e(TAG, "Exception in notifyListeners: ".concat(e2.getClass().getSimpleName()), e2);
            e2.printStackTrace();
        }
    }

    protected void dataTrackingResetData() {
        this.mDlyKaTrigCnt = 0;
        this.mDlyKaTrigFailCnt = 0;
        this.mDlyKaTrigOkCnt = 0;
        this.mDlyKaTrigOkThenFailCnt = 0;
        this.mDlyKaTrigFailReason = 0;
        this.mDlyKaTrigNoIndCnt = 0;
        this.mDlyKaDur = 0;
        this.mDlyKaDurMin = 0;
        this.mDlyKaDurMax = 0;
        this.mRatWhenKaFail = 0;
        this.mRsrpWhenKaFail = 0;
        this.mCarrWhenKaFail = "";
        this.mFlgWatingKaInd = false;
        this.mDataTackingMap.clear();
        this.mDtKaExcList.clear();
        logd("Record has been cleared");
    }

    protected static void logd(String str) {
        Rlog.d(TAG, str);
    }

    protected static void loge(String str) {
        Rlog.e(TAG, str);
    }

    protected void localLog(String str) {
        mLocalLog.log(TAG + ": " + str);
    }

    public void registerListener(IModemTcpKeepAliveIndCallback iModemTcpKeepAliveIndCallback) {
        if (iModemTcpKeepAliveIndCallback == null || this.mListeners.contains(iModemTcpKeepAliveIndCallback)) {
            return;
        }
        this.mListeners.add(iModemTcpKeepAliveIndCallback);
        logd("listener added..");
    }

    public void unregisterListener(IModemTcpKeepAliveIndCallback iModemTcpKeepAliveIndCallback) {
        if (iModemTcpKeepAliveIndCallback == null) {
            loge("Attempt to remove null listener");
        } else {
            this.mListeners.clear();
            logd("listener cleared..");
        }
    }

    protected void updateDefaultDataSlotId() {
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        if (!isValidatePhoneId(defaultDataSlotId) || defaultDataSlotId == this.mDdsPhoneId) {
            return;
        }
        this.mDdsPhoneId = defaultDataSlotId;
        logd("default data slot changed to " + defaultDataSlotId);
    }

    public boolean isValidatePhoneId(int i) {
        return i >= 0 && i < this.mPhoneCount;
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("----------------->>>>>>> ModemTcpKeepAliveController Dump Start >>>>>>>>---------");
        mLocalLog.dump(fileDescriptor, printWriter, strArr);
        printWriter.println("----------------->>>>>>> ModemTcpKeepAliveController Dump End >>>>>>>>>>>--------");
    }

    protected void dataTrackingUpdateKaExc() {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("dlyKaTrigFailReason", Integer.valueOf(this.mDlyKaTrigFailReason));
        linkedHashMap.put("ratWhenKaFail", Integer.valueOf(this.mDth.getDdsRat()));
        linkedHashMap.put("rsrpWhenKaFail", Integer.valueOf(this.mDth.getDdsRsrp()));
        linkedHashMap.put("carrWhenKaFail", this.mDth.getDdsPhoneCarrName());
        this.mDtKaExcList.add(linkedHashMap);
        logd("dataTrackingUpdateKaExc, now mDtKaExcList length = " + this.mDtKaExcList.size());
        this.mDlyKaTrigFailReason = -1;
    }

    public void onCloudChanged(boolean z) {
        sCloudDisabled = z;
        logd("onCloudChanged: disabled=" + z);
        if (z) {
            this.mHandler.post(new Runnable() { // from class: com.android.phone.ModemTcpKaBase$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onCloudChanged$0();
                }
            });
        }
    }

    protected void fillReportStatisticsMap() {
        this.mDataTackingMap.put("dlyKaTrigCnt", Integer.valueOf(this.mDlyKaTrigCnt));
        this.mDataTackingMap.put("dlyKaTrigOkCnt", Integer.valueOf(this.mDlyKaTrigOkCnt));
        this.mDataTackingMap.put("dlyKaTrigFailCnt", Integer.valueOf(this.mDlyKaTrigFailCnt));
        this.mDataTackingMap.put("dlyKaTrigOkThenFailCnt", Integer.valueOf(this.mDlyKaTrigOkThenFailCnt));
        this.mDataTackingMap.put("dlyKaTrigNoIndCnt", Integer.valueOf(this.mDlyKaTrigNoIndCnt));
        this.mDataTackingMap.put("dlyKaDur", Integer.valueOf(this.mDlyKaDur));
        this.mDataTackingMap.put("dlyKaDurMin", Integer.valueOf(this.mDlyKaDurMin));
        this.mDataTackingMap.put("dlyKaDurMax", Integer.valueOf(this.mDlyKaDurMax));
        this.mDataTackingMap.put("kaFailDetails", this.mDtKaExcList);
    }

    public void reportStatistics() {
        fillReportStatisticsMap();
        logd("mDataTackingMap: " + this.mDataTackingMap.toString());
        recordStatisticsEvent();
        this.mHandler.sendEmptyMessageDelayed(4, 10000L);
        logd("reportStatistics completed");
    }
}
