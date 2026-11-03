package com.android.phone.beidou;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import androidx.preference.PreferenceManager;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.xiaomi.bdsms.IBDSMSCallback;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public class BdSmsController {
    protected static volatile BdSmsController INSTANCE;
    protected BdConnectionService mBdConnectionService;
    private final BdConnectionService.BdConnectionStatusListener mBdConnectionStatusListener;
    protected BdSmsDispatcher mBdSmsDispatcher;
    private BdSmsInfo mBdSmsInfo;
    protected ConnectivityManager mConnectivityManager;
    protected Context mContext;
    protected Handler mHandler;
    public static final long DELAY_RETRY_SHORT_TIMER = 1000;
    public static final long DELAY_RETRY_TIMER = 1800000;
    public static final long SEND_OR_RECEIVE_INTERVAL = RelayUtils.Relay.RETRY_DELAY_IN_MILLIS;
    protected static HashSet mIBdStatusListeners = new HashSet();
    protected HashMap messageUriMap = new HashMap();
    protected int mSysStatus = -1;
    private int mSlotId = -1;
    private int mBdSmsMode = -1;
    private int mBdSmsIdx = Integer.MIN_VALUE;
    private int mPhoneBdServiceState = 0;
    private int mConnectBdServiceRetryTime = 0;
    private boolean mLastAirplaneMode = false;
    private boolean mLastNfcStatus = false;
    private String gnessMode = "persist.sys.gnss.mode";
    private boolean isHdBdStartUP = false;
    private final IBDSMSCallback mIBDSMSCallback = new IBDSMSCallback.Stub() { // from class: com.android.phone.beidou.BdSmsController.1
        @Override // com.xiaomi.bdsms.IBDSMSCallback
        public void BDnotifySysStatus(int i, int i2) {
            Iterator it = BdSmsController.mIBdStatusListeners.iterator();
            while (it.hasNext()) {
                ((IBdStatusListener) it.next()).notifyBdSysStatus(i, i2);
            }
            if (i == 4) {
                int i3 = BdSmsController.this.mBdSmsMode;
                TimeUnit timeUnit = TimeUnit.SECONDS;
                if (i3 == 0) {
                    BdSmsController.this.setBdSendOrReceiveTime(SystemClock.elapsedRealtime(), timeUnit.toMillis(BdSmsController.this.mBdConnectionService.queryAllowedSendTime() + 1));
                } else if (BdSmsController.this.mBdSmsMode == 1) {
                    BdSmsController.this.setBdSendOrReceiveTime(SystemClock.elapsedRealtime(), timeUnit.toMillis(BdSmsController.this.mBdConnectionService.queryAllowedReceiveTime() + 1));
                }
            }
            BdSmsController.logd("BDnotifySysStatus : status = " + i + ", cnt = " + i2);
            BdSmsController.this.mSysStatus = i;
        }

        @Override // com.xiaomi.bdsms.IBDSMSCallback
        public void BDnotifySateInfo(int i, int i2, int i3, int i4, int i5) {
            Iterator it = BdSmsController.mIBdStatusListeners.iterator();
            while (it.hasNext()) {
                ((IBdStatusListener) it.next()).notifyBdSatelliteStatus(i, i2, i3, i4, i5);
            }
            BdSmsController.logd("BDnotifySateInfo : azimuthDelta = " + i + ", pitchAngleDelta = " + i2 + ", cnr = " + i3 + ", sateIdx = " + i4 + ", cnrValue = " + i5);
        }

        @Override // com.xiaomi.bdsms.IBDSMSCallback
        public void BDnotifySendStatus(int i, int i2, int i3, int i4, String str, int i5) {
            if (BdSmsController.this.mPhoneBdServiceState == 1) {
                Iterator it = BdSmsController.mIBdStatusListeners.iterator();
                while (it.hasNext()) {
                    ((IBdStatusListener) it.next()).notifyBdSendSmsStatus(i, i2, i3, i4);
                }
                BdSmsController.logd("BDnotifySendStatus : idx = " + i + ", type = " + i2 + ", status = " + i3 + ", cause = " + i4);
                BdSmsController bdSmsController = BdSmsController.this;
                bdSmsController.mBdSmsDispatcher.dispatchBdSmsSendIntent((int[]) bdSmsController.messageUriMap.get(Integer.valueOf(i)), i, i3, i4, i5, BdSmsController.this.mBdSmsInfo != null && BdSmsController.this.mBdSmsInfo.isMms());
            } else {
                BdSmsController.logi("BDreceiveMessage : The Beidou SMS sending process has ended");
            }
            BdSmsController.this.resetStatus();
        }

        @Override // com.xiaomi.bdsms.IBDSMSCallback
        public void BDreceiveMessage(boolean z, int i, String str, String str2, String str3, int i2, boolean z2) {
            if (BdSmsController.this.mPhoneBdServiceState == 1) {
                Iterator it = BdSmsController.mIBdStatusListeners.iterator();
                while (it.hasNext()) {
                    ((IBdStatusListener) it.next()).notifyBdReceiveSmsStatus(z, i, str, str2);
                }
                BdSmsController.logd("BDreceiveMessage : status = " + z + ", cause = " + i);
                BdSmsController.this.mBdSmsDispatcher.dispatchBdSmsReceiveIntent(z, i, str2, str3, i2);
            } else {
                BdSmsController.logi("BDreceiveMessage : The Beidou SMS receiving process has ended");
            }
            BdSmsController.this.resetStatus();
        }

        @Override // com.xiaomi.bdsms.IBDSMSCallback
        public void BDnotifyEphStatus(boolean z, int i, int i2) {
            BdSmsController.logd("BDnotifyEphStatus: slotId = " + i2 + ", status = " + z + ", cause = " + i);
            if (!z) {
                BdSmsController.this.dispatchFailBDSmsIntent();
            }
            Iterator it = BdSmsController.mIBdStatusListeners.iterator();
            while (it.hasNext()) {
                ((IBdStatusListener) it.next()).notifyBdEphStatus(z, i, i2);
            }
        }

        @Override // com.xiaomi.bdsms.IBDSMSCallback
        public void BDnotifyEnableBDSms(boolean z) {
            BdSmsController.logd("BDnotifyEnableBDSms: result = " + z);
        }

        @Override // com.xiaomi.bdsms.IBDSMSCallback
        public void BDnotifyUpdateAuthKey(boolean z, int i, int i2) {
            Iterator it = BdSmsController.mIBdStatusListeners.iterator();
            while (it.hasNext()) {
                ((IBdStatusListener) it.next()).notifyUpdateAuthKey(z, i);
            }
            BdSmsController.logd("BDnotifyUpdateAuthKey : result = " + z + ", cause = " + i + ", slotId = " + i2);
        }

        @Override // com.xiaomi.bdsms.IBDSMSCallback
        public void BDnotifyUpdateEphData(boolean z, int i, int i2) {
            Iterator it = BdSmsController.mIBdStatusListeners.iterator();
            while (it.hasNext()) {
                ((IBdStatusListener) it.next()).notifyUpdateEphData(z, i);
            }
            BdSmsController.logd("BDnotifyUpdateEphData : result = " + z + ", cause = " + i + ", slotId = " + i2);
        }
    };

    public interface IBdStatusListener {
        default void notifyBdEphStatus(boolean z, int i, int i2) {
        }

        void notifyBdReceiveSmsStatus(boolean z, int i, String str, String str2);

        void notifyBdSatelliteStatus(int i, int i2, int i3, int i4, int i5);

        void notifyBdSendSmsStatus(int i, int i2, int i3, int i4);

        default void notifyBdServiceConnected() {
        }

        default void notifyBdServiceDisconnected() {
        }

        void notifyBdServiceStatus(boolean z, int i);

        void notifyBdSysStatus(int i, int i2);

        void notifyUpdateAuthKey(boolean z, int i);

        void notifyUpdateEphData(boolean z, int i);
    }

    private BdSmsController() {
        BdConnectionService.BdConnectionStatusListener bdConnectionStatusListener = new BdConnectionService.BdConnectionStatusListener() { // from class: com.android.phone.beidou.BdSmsController.2
            @Override // com.android.phone.beidou.BdConnectionService.BdConnectionStatusListener
            public void serviceConnected() {
                if (BdSmsController.this.mBdConnectionService.isBdSmsServiceReady()) {
                    BdSmsController bdSmsController = BdSmsController.this;
                    bdSmsController.mBdConnectionService.setCallBack(bdSmsController.mIBDSMSCallback);
                    Iterator it = BdSmsController.mIBdStatusListeners.iterator();
                    while (it.hasNext()) {
                        ((IBdStatusListener) it.next()).notifyBdServiceConnected();
                    }
                }
                BdSmsController.logd("serviceConnected");
            }

            @Override // com.android.phone.beidou.BdConnectionService.BdConnectionStatusListener
            public void serviceDisconnected() {
                if (BdSmsController.this.mBdSmsMode != -1) {
                    BdSmsController.loge("BdService interruption, end SMS process");
                    BdSmsController.this.dispatchFailBDSmsIntent();
                }
                if (BdSmsUtils.getBdSwitchState() && !BdSmsController.this.mHandler.hasMessages(0) && BdSmsController.this.mConnectBdServiceRetryTime <= 10) {
                    Message messageObtain = Message.obtain(BdSmsController.this.mHandler, 0);
                    long jMin = Math.min(((long) BdSmsController.this.mConnectBdServiceRetryTime) * 300000, BdSmsController.DELAY_RETRY_TIMER);
                    BdSmsController.this.mHandler.sendMessageDelayed(messageObtain, jMin);
                    BdSmsController.this.mConnectBdServiceRetryTime++;
                    BdSmsController.loge("BdService restart, delayTime = " + jMin + ", retryTime = " + BdSmsController.this.mConnectBdServiceRetryTime);
                }
                Iterator it = BdSmsController.mIBdStatusListeners.iterator();
                while (it.hasNext()) {
                    ((IBdStatusListener) it.next()).notifyBdServiceDisconnected();
                }
                BdSmsController.logi("BdServiceDisconnected");
            }
        };
        this.mBdConnectionStatusListener = bdConnectionStatusListener;
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        this.mContext = phoneGlobals;
        this.mConnectivityManager = (ConnectivityManager) phoneGlobals.getSystemService("connectivity");
        this.mHandler = new BdSmsHandler(Looper.getMainLooper());
        this.mBdSmsDispatcher = new BdSmsDispatcher(this.mContext);
        BdConnectionService bdConnectionService = BdConnectionService.getInstance();
        this.mBdConnectionService = bdConnectionService;
        bdConnectionService.registerBdConnectionStatusListener(bdConnectionStatusListener);
        BdSmsStatistics.init();
        BdUpdateManager.init();
        if (BdSmsUtils.getBdSwitchState()) {
            enableBdMonitor();
        }
        initBdSendOrReceiveTime();
        setBDServiceState(0);
    }

    public static void init() {
        if (INSTANCE == null) {
            synchronized (BdSmsController.class) {
                try {
                    if (INSTANCE == null) {
                        INSTANCE = new BdSmsController();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        logi("BdSmsController init");
    }

    public static BdSmsController getInstance() {
        if (INSTANCE == null) {
            init();
        }
        return INSTANCE;
    }

    public boolean getBdServiceStatus(int i) {
        boolean zIsBdSmsServiceReady = this.mBdConnectionService.isBdSmsServiceReady();
        BdConnectionService bdConnectionService = this.mBdConnectionService;
        if (zIsBdSmsServiceReady) {
            boolean bdServiceStatus = bdConnectionService.getBdServiceStatus(i);
            Iterator it = mIBdStatusListeners.iterator();
            while (it.hasNext()) {
                ((IBdStatusListener) it.next()).notifyBdServiceStatus(bdServiceStatus, -1);
            }
            return bdServiceStatus;
        }
        bdConnectionService.initService();
        loge("getBdServiceStatus fail");
        return false;
    }

    public int getBdSysStatus() {
        return this.mSysStatus;
    }

    public int sendBDMessageWithLocation(int i, int[] iArr, String str, int i2, List list, double d, double d2) {
        this.messageUriMap.put(Integer.valueOf(i), iArr);
        this.mBdSmsIdx = i;
        this.mSlotId = i2;
        this.mBdSmsInfo = new BdSmsInfo(i, iArr, str == null ? "" : str, i2, list, BdSmsUtils.getPhoneNumber(this.mContext, i2), d, d2, BdSmsInfo.BdSmsType.BD_TEXT);
        logd("sendBDMessageWithLocation: idx = " + i + ", uris = " + Arrays.toString(iArr) + ", slotId = " + i2);
        if (BDSatelliteMgr.getInstance().isSensorReady()) {
            return sendBDMessageInternal();
        }
        return -1;
    }

    public int sendBDMessage(int i, int[] iArr, String str, int i2, List list) {
        this.messageUriMap.put(Integer.valueOf(i), iArr);
        this.mBdSmsIdx = i;
        this.mSlotId = i2;
        String phoneNumber = BdSmsUtils.getPhoneNumber(this.mContext, i2);
        if (str == null) {
            str = "";
        }
        this.mBdSmsInfo = new BdSmsInfo(i, iArr, str, i2, list, phoneNumber, BdSmsInfo.BdSmsType.BD_TEXT);
        logd("sendBDMessage: idx = " + i + ", uris = " + Arrays.toString(iArr) + ", slotId = " + i2);
        if (BDSatelliteMgr.getInstance().isSensorReady()) {
            return sendBDMessageInternal();
        }
        return -1;
    }

    public int sendBDAudioMessageWithLocation(int i, int[] iArr, Uri uri, Uri uri2, int i2, List list, double d, double d2) {
        this.messageUriMap.put(Integer.valueOf(i), iArr);
        this.mBdSmsIdx = i;
        this.mSlotId = i2;
        this.mBdSmsInfo = new BdSmsInfo(i, iArr, uri, uri2, i2, list, BdSmsUtils.getPhoneNumber(this.mContext, i2), d, d2, BdSmsInfo.BdSmsType.BD_VOICE);
        logd("sendBDAudioMessageWithLocation: idx = " + i + ", uris = " + Arrays.toString(iArr) + ", slotId = " + i2);
        if (BDSatelliteMgr.getInstance().isSensorReady()) {
            return sendBDMessageInternal();
        }
        return -1;
    }

    public int sendBDAudioMessage(int i, int[] iArr, Uri uri, Uri uri2, int i2, List list) {
        this.messageUriMap.put(Integer.valueOf(i), iArr);
        this.mBdSmsIdx = i;
        this.mSlotId = i2;
        this.mBdSmsInfo = new BdSmsInfo(i, iArr, uri, uri2, i2, list, BdSmsUtils.getPhoneNumber(this.mContext, i2), BdSmsInfo.BdSmsType.BD_VOICE);
        logd("sendBDAudioMessage: idx = " + i + ", uris = " + Arrays.toString(iArr) + ", slotId = " + i2);
        if (BDSatelliteMgr.getInstance().isSensorReady()) {
            return sendBDMessageInternal();
        }
        return -1;
    }

    public int sendBDPictureMessage(int i, int[] iArr, Uri uri, Uri uri2, int i2, List list) {
        this.messageUriMap.put(Integer.valueOf(i), iArr);
        this.mBdSmsIdx = i;
        this.mSlotId = i2;
        this.mBdSmsInfo = new BdSmsInfo(i, iArr, uri, uri2, i2, list, BdSmsUtils.getPhoneNumber(this.mContext, i2), BdSmsInfo.BdSmsType.BD_PICTURE);
        logd("sendBDPictureMessage: idx = " + i + ", uris = " + Arrays.toString(iArr) + ", slotId = " + i2);
        if (BDSatelliteMgr.getInstance().isSensorReady()) {
            return sendBDMessageInternal();
        }
        return -1;
    }

    public int sendBDPictureMessageWithLocation(int i, int[] iArr, Uri uri, Uri uri2, int i2, List list, double d, double d2) {
        this.messageUriMap.put(Integer.valueOf(i), iArr);
        this.mBdSmsIdx = i;
        this.mSlotId = i2;
        this.mBdSmsInfo = new BdSmsInfo(i, iArr, uri, uri2, i2, list, BdSmsUtils.getPhoneNumber(this.mContext, i2), d, d2, BdSmsInfo.BdSmsType.BD_PICTURE);
        logd("sendBDPictureMessageWithLocation: idx = " + i + ", uris = " + Arrays.toString(iArr) + ", slotId = " + i2);
        if (BDSatelliteMgr.getInstance().isSensorReady()) {
            return sendBDMessageInternal();
        }
        return -1;
    }

    public int receiveBDMessage(int i) {
        this.mSlotId = i;
        logd("receiveBDMessage: slotId = " + i);
        if (!BDSatelliteMgr.getInstance().isSensorReady()) {
            return 0;
        }
        receiveBDMessageInternal();
        return 0;
    }

    public void triggerSmsAction() {
        if (this.mSlotId != -1) {
            if (this.mBdSmsMode == 0) {
                sendBDMessageInternal();
                return;
            } else {
                receiveBDMessageInternal();
                return;
            }
        }
        loge("triggerSmsAction sms not ready : slotId = " + this.mSlotId);
    }

    public void interruptSendingFlow() {
        this.mBdConnectionService.interruptSendingFlow();
    }

    public int getAllowedBDSmsSendTime() {
        long bdSendOrReceiveTime = getBdSendOrReceiveTime();
        long bdSendOrReceiveIntervalTime = getBdSendOrReceiveIntervalTime();
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        logi("getAllowedBDSmsSendTime: lastTime = " + bdSendOrReceiveTime + ", timeInterval = " + bdSendOrReceiveIntervalTime + ", curTime = " + jElapsedRealtime);
        if (jElapsedRealtime - bdSendOrReceiveTime >= bdSendOrReceiveIntervalTime) {
            return 0;
        }
        return (int) (((bdSendOrReceiveIntervalTime - jElapsedRealtime) + bdSendOrReceiveTime) / 1000);
    }

    public int getAllowedBDSmsReceiveTime() {
        long bdSendOrReceiveTime = getBdSendOrReceiveTime();
        long bdSendOrReceiveIntervalTime = getBdSendOrReceiveIntervalTime();
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        logi("getAllowedBDSmsReceiveTime: lastTime = " + bdSendOrReceiveTime + ", timeInterval = " + bdSendOrReceiveIntervalTime + ", curTime = " + jElapsedRealtime);
        if (jElapsedRealtime - bdSendOrReceiveTime >= bdSendOrReceiveIntervalTime) {
            return 0;
        }
        return (int) (((bdSendOrReceiveIntervalTime - jElapsedRealtime) + bdSendOrReceiveTime) / 1000);
    }

    public int getRemainBDSmsCount(int i) {
        int iQueryRemainMsgCount = this.mBdConnectionService.queryRemainMsgCount(i);
        logi("getRemainBDSmsCount: count = " + iQueryRemainMsgCount);
        return iQueryRemainMsgCount;
    }

    public void syncBDMail(int i) {
        this.mBdConnectionService.syncBDMail(i);
    }

    public void updateAuthKey(int i) {
        this.mBdConnectionService.updateAuthKey(i);
    }

    public void updateEphData(int i) {
        this.mBdConnectionService.updateEphData(i);
    }

    public boolean checkEphData() {
        return this.mBdConnectionService.checkEphData();
    }

    public static void registerBdStatusListener(IBdStatusListener iBdStatusListener) {
        if (iBdStatusListener == null) {
            return;
        }
        mIBdStatusListeners.add(iBdStatusListener);
    }

    public static void unregisterBdStatusListener(IBdStatusListener iBdStatusListener) {
        if (iBdStatusListener == null) {
            return;
        }
        mIBdStatusListeners.remove(iBdStatusListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetStatus() {
        this.mBdSmsMode = -1;
        this.mSlotId = -1;
        this.mSysStatus = -1;
        this.mBdSmsIdx = Integer.MIN_VALUE;
        this.messageUriMap.clear();
        this.mBdSmsInfo = null;
        logd("resetStatus");
    }

    protected void dispatchFailBDSmsIntent() {
        logd("dispatchFailBDSmsIntent : mBdSmsMode = " + this.mBdSmsMode);
        int i = this.mBdSmsMode;
        if (i == -1) {
            return;
        }
        if (i == 0) {
            BdSmsDispatcher bdSmsDispatcher = this.mBdSmsDispatcher;
            int[] iArr = (int[]) this.messageUriMap.get(Integer.valueOf(this.mBdSmsIdx));
            int i2 = this.mBdSmsIdx;
            int i3 = this.mSlotId;
            int i4 = this.mSysStatus;
            BdSmsInfo bdSmsInfo = this.mBdSmsInfo;
            bdSmsDispatcher.dispatchFailBdSmsSendIntent(iArr, i2, i3, i4, bdSmsInfo != null && bdSmsInfo.isMms());
        } else if (i == 1) {
            this.mBdSmsDispatcher.dispatchFailBdSmsReceiveIntent(this.mSlotId);
        }
        resetStatus();
    }

    public void sendEnableBDBroadcast(boolean z) {
        Intent intent = new Intent("miui.intent.action.ACTION_BD_SWITCH_STATE_CHANGE");
        intent.addFlags(268435456);
        intent.putExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_IS_ENABLE, z);
        logi("sendEnableBDBroadcast : isEnabled = " + z);
        this.mContext.sendBroadcast(intent);
    }

    private void sendBDServiceStateBroadcast(int i) {
        Intent intent = new Intent("miui.intent.action.ACTION_BD_SERVICE_STATE_CHANGE");
        intent.addFlags(268435456);
        intent.putExtra(MEModuleSignalT1s.EXTRA_STATE, i);
        logi("sendBDServiceStateBroadcast : state = " + i);
        this.mContext.sendBroadcast(intent);
    }

    private void setBDServiceState(int i) {
        Settings.System.putInt(PhoneGlobals.getInstance().getContentResolver(), "beidou_satellite_state", i);
    }

    private void setAirPlane(boolean z) {
        this.mConnectivityManager.setAirplaneMode(z);
    }

    public void setBdEnabled(boolean z) {
        if (z) {
            enableBdMonitor();
        } else {
            disableBdMonitor();
        }
        sendEnableBDBroadcast(z);
    }

    protected int sendBDMessageInternal() {
        int iSendBDAudioMessage;
        BdSmsInfo bdSmsInfo = this.mBdSmsInfo;
        if (bdSmsInfo == null) {
            dispatchFailBDSmsIntent();
            logd("sendBDMessageInternal: mBdSmsInfo is null");
            return -1;
        }
        if (BdSmsInfo.BdSmsType.BD_TEXT.equals(bdSmsInfo.getType())) {
            boolean zHasLocation = this.mBdSmsInfo.hasLocation();
            BdConnectionService bdConnectionService = this.mBdConnectionService;
            if (zHasLocation) {
                iSendBDAudioMessage = bdConnectionService.sendBDMessageWithLocation(this.mBdSmsInfo.getIdx(), this.mBdSmsInfo.getMessage(), this.mBdSmsInfo.getPhoneNumber(), this.mBdSmsInfo.getSlotId(), this.mBdSmsInfo.getReciveNumberList(), this.mBdSmsInfo.getLongitude(), this.mBdSmsInfo.getLatitude());
            } else {
                iSendBDAudioMessage = bdConnectionService.sendBDMessage(this.mBdSmsInfo.getIdx(), this.mBdSmsInfo.getMessage(), this.mBdSmsInfo.getPhoneNumber(), this.mBdSmsInfo.getSlotId(), this.mBdSmsInfo.getReciveNumberList());
            }
        } else if (BdSmsInfo.BdSmsType.BD_PICTURE.equals(this.mBdSmsInfo.getType())) {
            boolean zHasLocation2 = this.mBdSmsInfo.hasLocation();
            BdConnectionService bdConnectionService2 = this.mBdConnectionService;
            if (zHasLocation2) {
                iSendBDAudioMessage = bdConnectionService2.sendBDPictureMessageWithLocation(this.mBdSmsInfo.getIdx(), this.mBdSmsInfo.getKeyUri(), this.mBdSmsInfo.getFileUri(), this.mBdSmsInfo.getPhoneNumber(), this.mBdSmsInfo.getSlotId(), this.mBdSmsInfo.getReciveNumberList(), this.mBdSmsInfo.getLongitude(), this.mBdSmsInfo.getLatitude());
            } else {
                iSendBDAudioMessage = bdConnectionService2.sendBDPictureMessage(this.mBdSmsInfo.getIdx(), this.mBdSmsInfo.getKeyUri(), this.mBdSmsInfo.getFileUri(), this.mBdSmsInfo.getPhoneNumber(), this.mBdSmsInfo.getSlotId(), this.mBdSmsInfo.getReciveNumberList());
            }
        } else if (BdSmsInfo.BdSmsType.BD_VOICE.equals(this.mBdSmsInfo.getType())) {
            boolean zHasLocation3 = this.mBdSmsInfo.hasLocation();
            BdConnectionService bdConnectionService3 = this.mBdConnectionService;
            if (zHasLocation3) {
                iSendBDAudioMessage = bdConnectionService3.sendBDAudioMessageWithLocation(this.mBdSmsInfo.getIdx(), this.mBdSmsInfo.getKeyUri(), this.mBdSmsInfo.getFileUri(), this.mBdSmsInfo.getPhoneNumber(), this.mBdSmsInfo.getSlotId(), this.mBdSmsInfo.getReciveNumberList(), this.mBdSmsInfo.getLongitude(), this.mBdSmsInfo.getLatitude());
            } else {
                iSendBDAudioMessage = bdConnectionService3.sendBDAudioMessage(this.mBdSmsInfo.getIdx(), this.mBdSmsInfo.getKeyUri(), this.mBdSmsInfo.getFileUri(), this.mBdSmsInfo.getPhoneNumber(), this.mBdSmsInfo.getSlotId(), this.mBdSmsInfo.getReciveNumberList());
            }
        } else {
            iSendBDAudioMessage = -1;
        }
        logd("sendBDMessageInternal: result = " + iSendBDAudioMessage + ", " + this.mBdSmsInfo.toString());
        if (iSendBDAudioMessage == -1 || iSendBDAudioMessage == -2 || iSendBDAudioMessage == -3) {
            dispatchFailBDSmsIntent();
            if (iSendBDAudioMessage == -2) {
                showToast(R.string.beidou_sms_location_fail_toast);
                return iSendBDAudioMessage;
            }
            if (iSendBDAudioMessage == -3) {
                setBdSendOrReceiveTime(SystemClock.elapsedRealtime(), TimeUnit.SECONDS.toMillis(this.mBdConnectionService.queryAllowedSendTime() + 1));
            }
        }
        return iSendBDAudioMessage;
    }

    protected int receiveBDMessageInternal() {
        int iReceiveBDMessage = this.mBdConnectionService.receiveBDMessage(BdSmsUtils.getPhoneNumber(this.mContext, this.mSlotId), this.mSlotId);
        logd("receiveBDMessageInternal: slotId = " + this.mSlotId + ", result = " + iReceiveBDMessage);
        if (iReceiveBDMessage == -1 || iReceiveBDMessage == -2 || iReceiveBDMessage == -3) {
            dispatchFailBDSmsIntent();
            if (iReceiveBDMessage == -2) {
                showToast(R.string.beidou_sms_location_fail_toast);
                return iReceiveBDMessage;
            }
            if (iReceiveBDMessage == -3) {
                setBdSendOrReceiveTime(SystemClock.elapsedRealtime(), TimeUnit.SECONDS.toMillis(this.mBdConnectionService.queryAllowedReceiveTime() + 1));
            }
        }
        return iReceiveBDMessage;
    }

    private void enableBdMonitor() {
        this.mBdConnectionService.initService();
        BdUpdateAuthKeyJobService.initJobScheduler(this.mContext);
        BdUpdateAuthKeyJobService.initWaitWlanJobScheduler(this.mContext);
        BdUpdateEphJobService.initJobScheduler(this.mContext);
        BdUpdateEphJobService.initWaitWlanJobScheduler(this.mContext);
    }

    private void disableBdMonitor() {
        logi("disableBdMonitor");
        BdUpdateAuthKeyJobService.cancelSchedule(this.mContext);
        BdUpdateEphJobService.cancelSchedule(this.mContext);
        this.mBdConnectionService.closeService();
    }

    public synchronized void startConnectSatelliteService(int i) {
        if (this.mContext == null) {
            loge("startConnectSatelliteService: context is null");
            return;
        }
        logd("startConnectSatelliteService : mode = " + i + ", phoneBdServiceState = " + this.mPhoneBdServiceState);
        if (this.mPhoneBdServiceState == 0 && this.mBdConnectionService.isBdSmsServiceReady()) {
            setBdSmsMode(i);
            closeBluetoothIfNeed(this.mContext);
            this.mLastAirplaneMode = Settings.System.getInt(this.mContext.getContentResolver(), "airplane_mode_on", -1) == 1;
            closeNfc(this.mContext);
            setAirPlane(true);
            this.mPhoneBdServiceState = 1;
            sendBDServiceStateBroadcast(1);
            setBDServiceState(1);
            BDSatelliteMgr.getInstance().setUp(this.mContext, -1).init();
        } else {
            loge("startConnectSatelliteService failed");
        }
    }

    public synchronized void closeBdService() {
        try {
            logd("closeBdService: phoneBdServiceState = " + this.mPhoneBdServiceState);
            if (this.mPhoneBdServiceState == 1) {
                this.mPhoneBdServiceState = 0;
                sendBDServiceStateBroadcast(0);
                setBDServiceState(0);
                dispatchFailBDSmsIntent();
                BDSatelliteMgr.getInstance().stop();
                setAirPlane(this.mLastAirplaneMode);
                openNfcIfNeed(this.mContext);
                BdUpdateManager.getInstance().updateKeyAndSyncMail();
                refreshInterval();
                interruptSendingFlow();
                logd("Close BdService");
            } else {
                logd("No need to closeBdService");
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public void updateKeyAndSyncMail() {
        boolean zIsBdSmsServiceReady = this.mBdConnectionService.isBdSmsServiceReady();
        logd("updateKeyAndSyncMail: isBdSmsServiceReady = " + zIsBdSmsServiceReady);
        if (!zIsBdSmsServiceReady) {
            this.mBdConnectionService.initService();
            return;
        }
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            logd("updateKeyAndSyncMail: phoneId = " + i + ", isIccCardActivated = " + MiuiPhoneUtils.isIccCardActivated(i));
            if (MiuiPhoneUtils.isIccCardActivated(i) && BdSmsUtils.isSupportBeiDouByCarrier(i)) {
                syncBDMail(i);
                updateAuthKey(i);
            }
        }
    }

    public void updateAuthKey() {
        boolean zIsBdSmsServiceReady = this.mBdConnectionService.isBdSmsServiceReady();
        logd("updateAuthKey: isBdSmsServiceReady = " + zIsBdSmsServiceReady);
        if (!zIsBdSmsServiceReady) {
            this.mBdConnectionService.initService();
            return;
        }
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            logd("updateAuthKey: phoneId = " + i + ", isIccCardActivated = " + MiuiPhoneUtils.isIccCardActivated(i));
            if (MiuiPhoneUtils.isIccCardActivated(i) && BdSmsUtils.isSupportBeiDouByCarrier(i)) {
                updateAuthKey(i);
            }
        }
    }

    public void updateEphData() {
        boolean zIsBdSmsServiceReady = this.mBdConnectionService.isBdSmsServiceReady();
        logd("updateEphData: isBdSmsServiceReady = " + zIsBdSmsServiceReady);
        if (!zIsBdSmsServiceReady) {
            this.mBdConnectionService.initService();
            return;
        }
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            logd("updateEphData: phoneId = " + i + ", isIccCardActivated = " + MiuiPhoneUtils.isIccCardActivated(i));
            if (MiuiPhoneUtils.isIccCardActivated(i) && BdSmsUtils.isSupportBeiDouByCarrier(i)) {
                updateEphData(i);
            }
        }
    }

    public boolean isSupportedBDPicture() {
        return this.mBdConnectionService.isSupportedBDPicture();
    }

    public boolean isSupportedBDAudio() {
        return this.mBdConnectionService.isSupportedBDAudio();
    }

    public int preLoad(int i) {
        if (i == 1 && "hdbd".equals(Settings.System.getString(this.mContext.getContentResolver(), "GnssMode"))) {
            try {
                logd("setGnessMode: start");
                Settings.System.putString(this.mContext.getContentResolver(), "GnssMode", "qti");
                this.isHdBdStartUP = true;
            } catch (Exception e) {
                loge("setGnessMode failed:" + e);
            }
        } else if (i == 0 && this.isHdBdStartUP) {
            try {
                logd("setGnessMode: stop");
                Settings.System.putString(this.mContext.getContentResolver(), "GnssMode", "hdbd");
                this.isHdBdStartUP = false;
            } catch (Exception e2) {
                loge("setGnessMode failed:" + e2);
            }
        }
        int iPreLoad = this.mBdConnectionService.preLoad(i);
        logd("preLoad : result = " + iPreLoad);
        if (iPreLoad == -3) {
            updateEphData();
        }
        return iPreLoad;
    }

    private void initBdSendOrReceiveTime() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        if (defaultSharedPreferences == null) {
            loge("initBdSendOrReceiveTime fail");
            return;
        }
        SharedPreferences.Editor editorEdit = defaultSharedPreferences.edit();
        editorEdit.remove("bd_send_or_receive_time");
        editorEdit.remove("bd_send_or_receive_time_interval");
        editorEdit.commit();
        logd("initBdSendOrReceiveTime remove time");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setBdSendOrReceiveTime(long j, long j2) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        if (defaultSharedPreferences == null) {
            loge("setBdSendOrReceiveTime fail");
            return;
        }
        SharedPreferences.Editor editorEdit = defaultSharedPreferences.edit();
        editorEdit.putLong("bd_send_or_receive_time", j);
        editorEdit.putLong("bd_send_or_receive_time_interval", j2);
        editorEdit.commit();
        logd("setBdSendOrReceiveTime : time = " + j + ", timeInterval = " + j2);
    }

    private long getBdSendOrReceiveTime() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        if (defaultSharedPreferences == null) {
            loge("getBdSendOrReceiveTime fail");
            return 0L;
        }
        return defaultSharedPreferences.getLong("bd_send_or_receive_time", 0L);
    }

    private long getBdSendOrReceiveIntervalTime() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        if (defaultSharedPreferences == null) {
            loge("getBdSendOrReceiveIntervalTime fail");
            return 0L;
        }
        return defaultSharedPreferences.getLong("bd_send_or_receive_time_interval", 0L);
    }

    private void closeBluetoothIfNeed(Context context) {
        if (BdSmsUtils.isBluetoothEnable(context)) {
            showToast(R.string.beidou_close_bluetooth_toast);
            boolean bluetoothStatus = BdSmsUtils.setBluetoothStatus(context, false);
            logd("closeBluetoothIfNeed result = " + bluetoothStatus);
            if (bluetoothStatus) {
                return;
            }
            showToast(R.string.beidou_close_bluetooth_fail_toast);
        }
    }

    private void closeNfc(Context context) {
        boolean zIsNfcEnabled = BdSmsUtils.isNfcEnabled(context);
        this.mLastNfcStatus = zIsNfcEnabled;
        if (zIsNfcEnabled) {
            BdSmsUtils.setNfcStatus(context, false);
        }
    }

    private void openNfcIfNeed(Context context) {
        if (this.mLastNfcStatus) {
            BdSmsUtils.setNfcStatus(context, true);
        }
    }

    private void showToast(final int i) {
        new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.android.phone.beidou.BdSmsController.3
            @Override // java.lang.Runnable
            public void run() {
                Toast.makeText(BdSmsController.this.mContext, i, 1).show();
            }
        });
    }

    private void refreshInterval() {
        logd("refreshInterval");
        int i = this.mBdSmsMode;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        if (i == 0) {
            setBdSendOrReceiveTime(SystemClock.elapsedRealtime(), timeUnit.toMillis(this.mBdConnectionService.queryAllowedSendTime() + 1));
        } else {
            setBdSendOrReceiveTime(SystemClock.elapsedRealtime(), timeUnit.toMillis(this.mBdConnectionService.queryAllowedReceiveTime() + 1));
        }
    }

    public int getBdSmsMode() {
        return this.mBdSmsMode;
    }

    public void setBdSmsMode(int i) {
        this.mBdSmsMode = i;
    }

    public void setSlotId(int i) {
        this.mSlotId = i;
    }

    public int getPhoneBdServiceState() {
        return this.mPhoneBdServiceState;
    }

    public boolean isBdMMS() {
        if (this.mBdSmsMode != 0) {
            return false;
        }
        BdSmsInfo bdSmsInfo = this.mBdSmsInfo;
        if (bdSmsInfo != null) {
            return bdSmsInfo.isMms();
        }
        loge("isBdMMS : bdSmsInfo is null");
        return true;
    }

    protected static void logi(String str) {
        Log.i("BdSmsController", str);
    }

    protected static void loge(String str) {
        Log.e("BdSmsController", str);
    }

    protected static void logd(String str) {
        Log.d("BdSmsController", str);
    }

    public class BdSmsHandler extends Handler {
        public BdSmsHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            BdSmsController.logi("BdSmsHandler handleMessage what = " + message.what);
            if (message.what != 0) {
                return;
            }
            BdSmsController.this.mBdConnectionService.initService();
        }
    }
}
