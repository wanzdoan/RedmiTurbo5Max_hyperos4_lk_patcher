package com.android.phone.statistics;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.text.TextUtils;
import com.android.ims.ImsCall;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallManager;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneCall;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.statistics.utils.DefaultPreferenceHelper;
import com.android.phone.statistics.utils.ObjectSerializer;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import miui.process.IActivityChangeListener;
import miui.process.ProcessManager;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyStatAdapter;
import miuix.animation.utils.EaseManager;

/* JADX INFO: loaded from: classes.dex */
public class MiuiCallStat {
    private static MiuiCallStat sInstance;
    private final IActivityChangeListener.Stub mActivityChangeListener;
    private boolean mActivityListenerRegisterStatus;
    private ArrayList mCallList;
    private Context mContext;
    private int mDdsPhoneId;
    private boolean mFirstEnterBgCall;
    private MiuiCallStatHandler mHandler;
    private long mIncomingCallTime;
    private final BroadcastReceiver mIntentReceiver;
    private Call.State mLastFgCallState;
    private Call.State mLastRingCallState;
    private TelephonyCallBack mTelephonyCallBack;
    private int[] mViceCardCallStats;

    private boolean isValidDuration(long j) {
        return j > 0 && j < 40000;
    }

    private MiuiCallStat(Context context) {
        Call.State state = Call.State.IDLE;
        this.mLastRingCallState = state;
        this.mLastFgCallState = state;
        this.mTelephonyCallBack = null;
        this.mIncomingCallTime = 0L;
        this.mCallList = new ArrayList();
        this.mFirstEnterBgCall = true;
        this.mDdsPhoneId = -1;
        this.mActivityListenerRegisterStatus = false;
        this.mViceCardCallStats = new int[2];
        this.mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.statistics.MiuiCallStat.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                Utils.logD("MiuiCallStat", "mIntentReceiver action=".concat(action));
                if (action.equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED")) {
                    MiuiCallStat.this.mHandler.obtainMessage(202).sendToTarget();
                }
            }
        };
        this.mActivityChangeListener = new IActivityChangeListener.Stub() { // from class: com.android.phone.statistics.MiuiCallStat.2
            public void onActivityChanged(ComponentName componentName, ComponentName componentName2) {
                if (componentName == null || componentName2 == null) {
                    return;
                }
                String className = componentName.getClassName();
                String className2 = componentName2.getClassName();
                Utils.logD("MiuiCallStat", "preName: " + className + ",  curName: " + className2);
                if ("com.android.incallui.InCallActivity".equals(className) && !"com.android.contacts.activities.PeopleActivity".equals(className2) && MiuiCallStat.this.mFirstEnterBgCall) {
                    Utils.logI("MiuiCallStat", "The incall interface is in the background, the user may use the data");
                    int[] iArr = MiuiCallStat.this.mViceCardCallStats;
                    iArr[1] = iArr[1] + 1;
                    MiuiCallStat.this.mFirstEnterBgCall = false;
                }
            }
        };
        this.mContext = context;
        this.mHandler = new MiuiCallStatHandler(Looper.myLooper());
        reloadCallRecords();
        registerTelephonyCallBack();
        CallManager.getInstance(context).registerForDisconnect(this.mHandler, 203, (Object) null);
        this.mDdsPhoneId = getDefaultDataSlotId();
        registerReceiver();
        Rlog.d("MiuiCallStat", "MiuiCallStat init");
    }

    public static MiuiCallStat getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MiuiCallStat(context);
        }
        return sInstance;
    }

    public class MiuiCallStatHandler extends Handler {
        public MiuiCallStatHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Utils.logD("MiuiCallStat", "msg.what:" + message.what);
            switch (message.what) {
                case EaseManager.EaseStyleDef.PERLIN /* 201 */:
                    MiuiCallStat.this.processPreciseCallStateChanged(message.arg1);
                    break;
                case 202:
                    if (MiuiCallStat.this.getDefaultDataSlotId() != MiuiCallStat.this.mDdsPhoneId) {
                        MiuiCallStat miuiCallStat = MiuiCallStat.this;
                        miuiCallStat.mDdsPhoneId = miuiCallStat.getDefaultDataSlotId();
                    }
                    break;
                case 203:
                    MiuiCallStat.this.processCallDisconnectEvent((Connection) ((AsyncResult) message.obj).result);
                    break;
            }
        }
    }

    private void registerTelephonyCallBack() {
        if (this.mTelephonyCallBack == null) {
            Utils.logD("MiuiCallStat", "register TelephonyCallBack");
            TelephonyCallBack telephonyCallBack = new TelephonyCallBack();
            this.mTelephonyCallBack = telephonyCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyCallBack);
        }
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyCallBack() {
        }

        public void onPreciseCallStateChanged(int i, int i2) {
            if (MiuiPhoneUtils.isValidatePhoneId(i)) {
                Utils.logD("MiuiCallStat", "onPreciseCallStateChanged call state : " + i2);
                MiuiCallStat.this.mHandler.removeMessages(EaseManager.EaseStyleDef.PERLIN);
                MiuiCallStat.this.mHandler.sendMessageDelayed(MiuiCallStat.this.mHandler.obtainMessage(EaseManager.EaseStyleDef.PERLIN, Integer.valueOf(i)), 500L);
            }
        }
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:16:0x0056  */
    public void processPreciseCallStateChanged(int i) {
        Call.State state;
        Call.State state2;
        Call.State state3;
        Call.State state4;
        ImsPhone phone = PhoneFactory.getPhone(i);
        Utils.logD("MiuiCallStat", "phoneId:" + i);
        Call.State state5 = Call.State.IDLE;
        if (phone instanceof ImsPhone) {
            ImsPhone imsPhone = phone;
            ImsPhoneCall foregroundCall = imsPhone.getForegroundCall();
            ImsPhoneCall ringingCall = imsPhone.getRingingCall();
            if (foregroundCall == null || ringingCall == null) {
                state2 = state5;
                state = state2;
            } else {
                state = getCallState(foregroundCall);
                state2 = ringingCall.getState();
            }
        } else if ((phone instanceof GsmCdmaPhone) || phone.getPhoneType() == 7) {
            Call ringingCall2 = phone.getRingingCall();
            Call foregroundCall2 = phone.getForegroundCall();
            if (foregroundCall2 == null || ringingCall2 == null) {
                state2 = state5;
                state = state2;
            } else {
                state = foregroundCall2.getState();
                state2 = ringingCall2.getState();
            }
        } else {
            state2 = state5;
            state = state2;
        }
        if (((this.mLastFgCallState == state5 && state == Call.State.ALERTING) || (this.mLastRingCallState == state5 && (state2 == Call.State.INCOMING || state2 == Call.State.WAITING))) && allowViceCallStat(phone.getPhoneId()) && !this.mActivityListenerRegisterStatus) {
            registerActivityChangeListener();
            this.mActivityListenerRegisterStatus = true;
        }
        Utils.logD("MiuiCallStat", "foregroundCall: " + state + ", ringingCall: " + state2);
        Call.State state6 = this.mLastFgCallState;
        if (state6 == state5 && state == (state4 = Call.State.ALERTING)) {
            updateCallConnectTimeRecord(phone, "MO");
            this.mLastFgCallState = state4;
        } else if (state6 != state5 && state == state5) {
            this.mLastFgCallState = state5;
        }
        Call.State state7 = this.mLastRingCallState;
        if (state7 == state5 && (state2 == (state3 = Call.State.INCOMING) || state2 == Call.State.WAITING)) {
            recordIncomingCallTime();
            this.mLastRingCallState = state3;
        } else if (state7 == Call.State.INCOMING && state2 == state5 && state == state5) {
            updateCallConnectTimeRecord(phone, "MT");
            this.mLastRingCallState = state5;
        }
        Utils.logD("MiuiCallStat", "mLastFgCallState: " + this.mLastFgCallState + ", mLastRingCallState: " + this.mLastRingCallState);
    }

    private Call.State getCallState(ImsPhoneCall imsPhoneCall) {
        Call.State state = imsPhoneCall.getState();
        ImsCall imsCall = imsPhoneCall.getImsCall();
        if (state != Call.State.ALERTING || imsCall == null) {
            return state;
        }
        Utils.logD("MiuiCallStat", "ImsPhoneCall imsCall: " + imsCall.getState());
        return (imsCall.getState() == 1 || imsCall.getState() == 0) ? Call.State.DIALING : state;
    }

    private synchronized void updateCallConnectTimeRecord(Phone phone, String str) {
        if (this.mCallList.size() > 20) {
            resetInCallUICallTime();
            return;
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        long inCallUICallTime = getInCallUICallTime();
        long j = 0;
        if (inCallUICallTime == 0) {
            return;
        }
        HashMap map = new HashMap();
        String networkOperator = getNetworkOperator(phone);
        String callType = getCallType(phone, str);
        Utils.logD("MiuiCallStat", "operator: " + networkOperator + ", callType: " + callType);
        if ("MO".equals(str)) {
            j = jElapsedRealtime - inCallUICallTime;
        } else if ("MT".equals(str)) {
            j = inCallUICallTime - this.mIncomingCallTime;
            resetIncomingCallTime();
        }
        resetInCallUICallTime();
        if (isValidDuration(j)) {
            map.put("Network", networkOperator);
            map.put("CallType", callType);
            map.put("CallDirect", str);
            map.put("time", Long.valueOf(j));
            this.mCallList.add(map);
            Utils.logD("MiuiCallStat", "updateCallConnectTimeRecord: " + this.mCallList);
        }
    }

    private long getInCallUICallTime() {
        long j = Settings.System.getLong(this.mContext.getContentResolver(), "incallui.stat.call_time", 0L);
        Utils.logD("MiuiCallStat", "incallTime: " + j);
        return j;
    }

    private void resetInCallUICallTime() {
        Settings.System.putLong(this.mContext.getContentResolver(), "incallui.stat.call_time", 0L);
    }

    private void recordIncomingCallTime() {
        this.mIncomingCallTime = SystemClock.elapsedRealtime();
    }

    private void resetIncomingCallTime() {
        this.mIncomingCallTime = 0L;
    }

    /* JADX WARN: Code duplicated, block: B:38:0x00a9  */
    /* JADX WARN: Code duplicated, block: B:40:0x00bd  */
    /* JADX WARN: Code duplicated, block: B:41:0x00bf  */
    /* JADX WARN: Code duplicated, block: B:43:0x00c2 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:44:0x00c4  */
    /* JADX WARN: Code duplicated, block: B:45:0x00c7  */
    /* JADX WARN: Code duplicated, block: B:53:0x00dd  */
    /* JADX WARN: Code duplicated, block: B:54:0x00e0 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:56:0x00e3  */
    private String getCallType(Phone phone, String str) {
        boolean z;
        ImsPhoneCall ringingCall;
        boolean zIsWifi;
        String str2;
        if (phone == null) {
            return "NA";
        }
        if (phone.getPhoneType() == 7) {
            return "VoNTN";
        }
        if (phone.getPhoneType() == 5) {
            String str3 = Utils.getDataRat(getServiceState(phone)) == 20 ? "VoNR" : "VoLTE";
            boolean zIsVideo = false;
            if ("MO".equals(str)) {
                ImsPhoneCall foregroundCall = ((ImsPhone) phone).getForegroundCall();
                if (foregroundCall == null || !foregroundCall.hasConnections()) {
                    zIsWifi = false;
                } else {
                    ArrayList connections = foregroundCall.getConnections();
                    int size = connections.size();
                    zIsWifi = false;
                    int i = 0;
                    while (i < size) {
                        Object obj = connections.get(i);
                        i++;
                        Connection connection = (Connection) obj;
                        zIsVideo |= Utils.isVideo(connection.getVideoState());
                        zIsWifi |= connection.isWifi();
                    }
                }
            } else {
                if ("MT".equals(str) && (ringingCall = ((ImsPhone) phone).getRingingCall()) != null && ringingCall.hasConnections()) {
                    ArrayList connections2 = ringingCall.getConnections();
                    int size2 = connections2.size();
                    zIsWifi = false;
                    int i2 = 0;
                    while (i2 < size2) {
                        Object obj2 = connections2.get(i2);
                        i2++;
                        Connection connection2 = (Connection) obj2;
                        zIsVideo |= Utils.isVideo(connection2.getVideoState());
                        zIsWifi |= connection2.isWifi();
                    }
                } else {
                    z = false;
                }
                str2 = "VoWIFI";
                if (zIsVideo) {
                    if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isVoiceLinkRegistered(this.mContext, phone.getPhoneId())) {
                        str3 = "VoDATA";
                    } else {
                        str3 = "VoWIFI";
                    }
                }
                if (z) {
                    if (zIsVideo) {
                        str2 = "ViWIFI";
                    } else if (!str3.equals("VoLTE") || str3.equals("VoNR")) {
                        str2 = "ViLTE";
                    } else if (str3.equals("VoDATA")) {
                        str2 = "ViDATA";
                    } else {
                        str2 = str3;
                    }
                } else if (!zIsVideo) {
                    str2 = str3;
                }
                Utils.logD("MiuiCallStat", "isVideoCall = " + z + "isWifiCall = " + zIsVideo);
                return str2;
            }
            z = zIsVideo;
            zIsVideo = zIsWifi;
            str2 = "VoWIFI";
            if (zIsVideo) {
                if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isVoiceLinkRegistered(this.mContext, phone.getPhoneId())) {
                    str3 = "VoDATA";
                } else {
                    str3 = "VoWIFI";
                }
            }
            if (z) {
                if (zIsVideo) {
                    str2 = "ViWIFI";
                } else if (!str3.equals("VoLTE")) {
                    str2 = "ViLTE";
                } else {
                    str2 = "ViLTE";
                }
            } else if (!zIsVideo) {
                str2 = str3;
            }
            Utils.logD("MiuiCallStat", "isVideoCall = " + z + "isWifiCall = " + zIsVideo);
            return str2;
        }
        return "CSFB";
    }

    private String getNetworkOperator(Phone phone) {
        String operatorNumeric;
        ServiceState serviceState = getServiceState(phone);
        if (serviceState == null) {
            operatorNumeric = "";
        } else {
            operatorNumeric = serviceState.getOperatorNumeric();
        }
        return TextUtils.isEmpty(operatorNumeric) ? TelephonyManager.getDefault().getSimOperatorForSlot(phone.getPhoneId()) : operatorNumeric;
    }

    private ServiceState getServiceState(Phone phone) {
        if (phone == null) {
            return null;
        }
        return MiuiPhoneUtils.getPhone(phone.getPhoneId()).getServiceState();
    }

    public void updateCallRecords() {
        try {
            Utils.logD("MiuiCallStat", "updateCallRecords: " + this.mCallList);
            DefaultPreferenceHelper.getInstance().setCallConnectTimeStats(ObjectSerializer.serialize(this.mCallList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reloadCallRecords() {
        try {
            String callConnectTimeStats = DefaultPreferenceHelper.getInstance().getCallConnectTimeStats();
            if (!TextUtils.isEmpty(callConnectTimeStats)) {
                this.mCallList = (ArrayList) ObjectSerializer.deserialize(callConnectTimeStats);
            }
            Utils.logD("MiuiCallStat", "reloadCallRecords: " + this.mCallList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reportCallConnectTimeDfs() {
        if (this.mCallList.size() > 0) {
            HashMap map = new HashMap();
            ArrayList arrayList = this.mCallList;
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                for (Map.Entry entry : ((HashMap) obj).entrySet()) {
                    map.put((String) entry.getKey(), entry.getValue().toString());
                }
            }
            Utils.logD("MiuiCallStat", "reportCallConnectTimeDfs: " + this.mCallList);
            MiuiTelephonyStatistic.getInstance().recordDFSEventStatistic(915200011, map);
        }
    }

    public void reportCallConnectTimeEvent() {
        if (this.mCallList.size() > 0) {
            HashMap map = new HashMap();
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = this.mCallList;
            int size = arrayList2.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList2.get(i);
                i++;
                arrayList.add((HashMap) obj);
            }
            map.put("Params", arrayList);
            if (MiuiTelephonyStatistic.getInstance() != null) {
                map.put("sceneType", MiuiTelephonyStatistic.getInstance().getSceneType());
            }
            Utils.logD("MiuiCallStat", "reportCallConnectTimeEvent: " + this.mCallList);
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_call_connect_time", map);
            reportCallConnectTimeDfs();
            clearCallRecords();
        }
    }

    public void clearCallRecords() {
        this.mCallList.clear();
        updateCallRecords();
    }

    private void resetEnterBgCallStatus() {
        this.mFirstEnterBgCall = true;
    }

    private void registerActivityChangeListener() {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add("com.android.incallui");
        arrayList.add("com.android.incallui.InCallActivity");
        ProcessManager.registerActivityChangeListener(arrayList2, arrayList, this.mActivityChangeListener);
    }

    private void unregisterActivityChangeListener() {
        ProcessManager.unregisterActivityChanageListener(this.mActivityChangeListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDefaultDataSlotId() {
        return DefaultSimManager.getDefaultDataSlotId();
    }

    public void reportViceCardCallStats() {
        HashMap map = new HashMap();
        map.put("viceCall", Integer.valueOf(this.mViceCardCallStats[0]));
        map.put("viceBgCall", Integer.valueOf(this.mViceCardCallStats[1]));
        Utils.logD("MiuiCallStat", "reportViceCardCallStats params= " + map);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_vice_card_call", map);
        initViceCardCallStats();
    }

    private void initViceCardCallStats() {
        Arrays.fill(this.mViceCardCallStats, 0);
    }

    private Phone getPhone(Connection connection) {
        if (connection == null || connection.getCall() == null) {
            return null;
        }
        return connection.getCall().getPhone();
    }

    private boolean allowViceCallStat(int i) {
        return MiuiPhoneUtils.getInsertedSimCount() == 2 && MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) && MiuiPhoneUtils.isValidatePhoneId(i) && i != this.mDdsPhoneId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processCallDisconnectEvent(Connection connection) {
        Phone phone = getPhone(connection);
        if (phone == null) {
            return;
        }
        int phoneId = phone.getPhoneId();
        if (MiuiPhoneUtils.isValidatePhoneId(phoneId) && allowViceCallStat(phoneId)) {
            int[] iArr = this.mViceCardCallStats;
            iArr[0] = iArr[0] + 1;
            resetEnterBgCallStatus();
            if (this.mActivityListenerRegisterStatus) {
                unregisterActivityChangeListener();
                this.mActivityListenerRegisterStatus = false;
                Utils.logD("MiuiCallStat", "call disconnect, unregister activity change listener");
            }
        }
    }
}
