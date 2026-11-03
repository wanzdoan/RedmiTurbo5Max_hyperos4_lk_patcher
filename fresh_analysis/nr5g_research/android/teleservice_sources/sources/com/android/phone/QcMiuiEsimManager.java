package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Binder;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.se.omapi.Channel;
import android.se.omapi.Reader;
import android.se.omapi.SEService;
import android.se.omapi.Session;
import android.service.euicc.EuiccProfileInfo;
import android.telephony.IccOpenLogicalChannelResponse;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.HexDump;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccController;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.utils.ModemUtils;
import com.android.phone.utils.Utils;
import com.qti.extphone.Client;
import com.qti.extphone.ExtPhoneCallbackListener;
import com.qti.extphone.ExtTelephonyManager;
import com.qti.extphone.QtiSimType;
import com.qti.extphone.ServiceCallback;
import com.qti.extphone.Status;
import com.qti.extphone.Token;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.Executors;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionInfo;
import miui.telephony.TelephonyManagerEx;
import miui.telephony.TelephonyUtils;
import miuix.animation.internal.TransitionInfo;

/* JADX INFO: loaded from: classes.dex */
public class QcMiuiEsimManager extends MiuiEsimManager {
    private static IccOpenLogicalChannelResponse channel;
    private static Context mContext;
    private static ModemUtils mHydra;
    private NfcAdapter mNfcAdapter;
    private QcAidlEsimInner mQcAidlEsimInner;
    private SEService mSeService;
    private MiuiDeviceEuiccBindingMgrForSt miuiDeviceEuiccBindingMgrForSt;
    private static final byte[] UPGRADE_VERSION_COMMAND = {-65, -126, 3, 2, 5, 0};
    private static final byte[] GET_INFO1_COMMAND = {-127, -30, -111, 0, 3, -65, 32, 0};
    private static final byte[] GET_INFO2_COMMAND = {-65, 32, 0};
    private static int mRetryOpenChannelForMissSourceCounts = 0;
    private static final byte[] SELECT_ISD = {-96, 0, 0, 1, 81, 0, 0, 0};
    protected byte[] AID = {-96, 0, 0, 0, 48, -1, 85, 80, 1};
    private Object serviceMutex = new Object();
    private boolean mIsSeServiceConnected = false;
    private boolean mEsimRecoveryFailSeNotReady = false;
    private Channel mEsimChannel = null;
    private Session mEsimSession = null;

    protected QcMiuiEsimManager() {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        mContext = phoneGlobals;
        ModemUtils modemUtilsInit = ModemUtils.init(phoneGlobals);
        mHydra = modemUtilsInit;
        if (modemUtilsInit != null) {
            EventHubManager.getInstance().registerListener("telephony", "ril_ready", new BaseEvent.IEventListener() { // from class: com.android.phone.QcMiuiEsimManager$$ExternalSyntheticLambda0
                @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
                public final void onEvent(BaseEvent baseEvent) {
                    QcMiuiEsimManager.$r8$lambda$PvBn52QQCGx_Dn3S9PpCKe3pXFw(baseEvent);
                }
            });
        }
        MiuiEsimManager.isCanRestUimPowerForOpenChannelFile = true;
        if (TelephonyManagerEx.getDefault().isPlatform8850() || TelephonyManagerEx.getDefault().isPlatform8750() || TelephonyManagerEx.getDefault().isPlatform7635() || HyperOSCustFeatureResolve.getBoolean("config_qc_aidl_esim_support", false)) {
            this.mQcAidlEsimInner = new QcAidlEsimInner();
            this.mNfcAdapter = NfcAdapter.getDefaultAdapter(mContext);
            MiuiEsimManagerBase.logd("mNfcAdapter = " + this.mNfcAdapter);
        }
        this.mSeService = new SEService(mContext.getApplicationContext(), Executors.newFixedThreadPool(1), new SEService.OnConnectedListener() { // from class: com.android.phone.QcMiuiEsimManager.1
            @Override // android.se.omapi.SEService.OnConnectedListener
            public void onConnected() {
                synchronized (QcMiuiEsimManager.this.serviceMutex) {
                    try {
                        MiuiEsimManagerBase.logi("seService is connected, mEsimRecoveryFailSeNotReady = " + QcMiuiEsimManager.this.mEsimRecoveryFailSeNotReady);
                        QcMiuiEsimManager.this.mIsSeServiceConnected = true;
                        if (QcMiuiEsimManager.this.mEsimRecoveryFailSeNotReady) {
                            QcMiuiEsimManager.this.recoveryForInit();
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        });
    }

    public static /* synthetic */ void $r8$lambda$PvBn52QQCGx_Dn3S9PpCKe3pXFw(BaseEvent baseEvent) {
        if ((baseEvent instanceof TelephonyEvent.RilEvent) && ((TelephonyEvent.RilEvent) baseEvent).isRilReady()) {
            MiuiEsimManagerBase.mHandler.sendEmptyMessage(12);
        }
    }

    @Override // com.android.phone.MiuiEsimManager, com.android.phone.MiuiEsimManagerBase
    public boolean isEsimActive() {
        return super.isEsimActive();
    }

    @Override // com.android.phone.MiuiEsimManager, com.android.phone.MiuiEsimManagerBase
    public boolean isSupportEsimForCountry(String str) {
        return super.isSupportEsimForCountry(str);
    }

    @Override // com.android.phone.MiuiEsimManagerBase
    public void setSrvDomain(final boolean z) {
        CommandsInterface commandsInterface;
        MiuiEsimManagerBase.logi("onSubscriptionsChanged P83X , enableEsim:" + z);
        final int i = 1;
        final TelephonyManager telephonyManagerCreateForSubscriptionId = TelephonyManager.from(mContext).createForSubscriptionId(SubscriptionManager.getSubscriptionId(1));
        if (mHydra == null) {
            mHydra = ModemUtils.init(mContext);
        }
        MiuiEsimManagerBase.logi("setSrvDomain");
        mHydra.setSrvDomain(1, z ? 1 : 0);
        MiuiEsimManagerBase.logi("setUsageSetting");
        Phone phone = MiuiPhoneUtils.getPhone(1);
        if (phone != null && (commandsInterface = phone.mCi) != null) {
            commandsInterface.setUsageSetting(MiuiEsimManagerBase.mHandler.obtainMessage(99), z ? 2 : 1);
        }
        MiuiEsimManagerBase.logi("disableIms:".concat(z ? "yes" : "no"));
        if (z) {
            telephonyManagerCreateForSubscriptionId.disableIms(1);
        }
        new Thread(this) { // from class: com.android.phone.QcMiuiEsimManager.2
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                MiuiEsimManagerBase.logi("setAllowedNetworkTypesForReason, orignial:" + DefaultSimManager.getPreferredNetworkModeFromDb(QcMiuiEsimManager.mContext, i));
                if (z) {
                    telephonyManagerCreateForSubscriptionId.setAllowedNetworkTypesForReason(0, 921600L);
                }
            }
        }.start();
    }

    @Override // com.android.phone.MiuiEsimManager, com.android.phone.MiuiEsimManagerBase
    public int setEsimState(int i) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (MiuiEsimManagerBase.isEsimSlot1OnlyMode()) {
                return 0;
            }
            if (i == 1 && MiuiEsimManagerBase.isRemoveEsimSwitch()) {
                return 0;
            }
            if (stopVsimIfNeeded(i)) {
                QcAidlEsimInner qcAidlEsimInner = this.mQcAidlEsimInner;
                return qcAidlEsimInner != null ? qcAidlEsimInner.setEsimState(i) : setEsimStateLegacy(i);
            }
            MiuiEsimManagerBase.logd("setEsimState: stopVsimIfNeeded failed, state = " + i);
            return -1;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    private int setEsimStateLegacy(int i) {
        int i2 = i == 0 ? 1 : 0;
        int i3 = i == 0 ? -5 : -6;
        int i4 = i == 0 ? -2 : -3;
        int i5 = i == 0 ? -7 : -8;
        if (!onUimPowerRequest(0, -1) && !retrySetEsimState(i3, i)) {
            MiuiEsimManagerBase.trackerEsimEvent(i == 0 ? "enable_esim" : "disable_esim", Integer.valueOf(i3));
            return i3;
        }
        if (onSwitchEsimGPIO(i) != 0 && !retrySetEsimState(i4, i)) {
            MiuiEsimManagerBase.trackerEsimEvent(i == 0 ? "enable_esim" : "disable_esim", Integer.valueOf(i4));
            return i4;
        }
        if (!onUimPowerRequest(1, i2) && !retrySetEsimState(i5, i)) {
            MiuiEsimManagerBase.trackerEsimEvent(i == 0 ? "enable_esim" : "disable_esim", Integer.valueOf(i5));
            return i5;
        }
        MiuiEsimManagerBase.setEsimSecureValue(i);
        StringBuilder sb = new StringBuilder("setEsimState for ");
        sb.append(i == 0 ? "enable_esim" : "disable_esim");
        sb.append(" result = ");
        sb.append(0);
        MiuiEsimManagerBase.logi(sb.toString());
        return 0;
    }

    @Override // com.android.phone.MiuiEsimManager, com.android.phone.MiuiEsimManagerBase
    public int getEsimGPIOState() {
        if (MiuiEsimManagerBase.isEsimSlot1OnlyMode()) {
            return 0;
        }
        QcAidlEsimInner qcAidlEsimInner = this.mQcAidlEsimInner;
        if (qcAidlEsimInner != null) {
            return qcAidlEsimInner.getCurrentSimType();
        }
        return getEsimGPIOStateLegacy();
    }

    public int getEsimGPIOStateLegacy() {
        int iIntValue;
        ModemUtils modemUtils = mHydra;
        if (modemUtils != null) {
            Object objCallHook = modemUtils.callHook("onGetEsimStatus", new Object[0]);
            if (objCallHook instanceof Integer) {
                iIntValue = ((Integer) objCallHook).intValue();
            } else {
                MiuiEsimManagerBase.loge("getEsimGPIOStateLegacy: return type not Integer, actual: ".concat(objCallHook != null ? objCallHook.getClass().getName() : "null"));
                iIntValue = -4;
            }
        } else {
            iIntValue = -4;
        }
        if (iIntValue != 0 && iIntValue != 1) {
            MiuiEsimManagerBase.trackerEsimEvent("getEsimState", Integer.valueOf(iIntValue));
        }
        MiuiEsimManagerBase.logd("getEsimGPIOState state == " + iIntValue);
        return iIntValue;
    }

    @Override // com.android.phone.MiuiEsimManager, com.android.phone.MiuiEsimManagerBase
    public String getProductEid() {
        ByteBuffer byteBuffer;
        ModemUtils modemUtils = mHydra;
        String hexString = "";
        if (modemUtils != null) {
            Object objCallHook = modemUtils.callHook("onHookEfsOptSync", 1, "/nv/item_files/modem/uim/uimdrv/esim_eid", 4);
            if (objCallHook instanceof ByteBuffer) {
                byteBuffer = (ByteBuffer) objCallHook;
            } else {
                MiuiEsimManagerBase.loge("getProductEid: return type not ByteBuffer, actual: ".concat(objCallHook != null ? objCallHook.getClass().getName() : "null"));
                byteBuffer = null;
            }
            if (byteBuffer == null) {
                MiuiEsimManagerBase.logi("getProductEid read eid efs failed");
                return "";
            }
            if (byteBuffer.getInt() == 0) {
                byteBuffer.getInt();
                int iRemaining = byteBuffer.remaining();
                byte[] bArr = new byte[iRemaining];
                byteBuffer.get(bArr, 0, iRemaining);
                hexString = HexDump.toHexString(bArr);
            }
        }
        MiuiEsimManagerBase.logi("getProductEid final eid = " + TelephonyUtils.pii(hexString));
        return hexString;
    }

    @Override // com.android.phone.MiuiEsimManager, com.android.phone.MiuiEsimManagerBase
    public void writeProductEid(byte[] bArr) {
        boolean zBooleanValue;
        ModemUtils modemUtils = mHydra;
        if (modemUtils != null) {
            Object objCallHook = modemUtils.callHook("onHookEfsWrite", 1, "/nv/item_files/modem/uim/uimdrv/esim_eid", bArr);
            if (objCallHook instanceof Boolean) {
                zBooleanValue = ((Boolean) objCallHook).booleanValue();
            } else {
                MiuiEsimManagerBase.loge("writeProductEid: return type not Boolean, actual: ".concat(objCallHook != null ? objCallHook.getClass().getName() : "null"));
                zBooleanValue = false;
            }
        } else {
            zBooleanValue = false;
        }
        MiuiEsimManagerBase.trackerEsimEvent("writeProductEid", Boolean.valueOf(zBooleanValue));
    }

    private static int onSwitchEsimGPIO(int i) {
        int i2 = i == 0 ? 0 : 1;
        ModemUtils modemUtils = mHydra;
        if (modemUtils != null) {
            Object objCallHook = modemUtils.callHook("onSetEsimStatus", Integer.valueOf(i2), Boolean.TRUE);
            if (objCallHook instanceof Integer) {
                return ((Integer) objCallHook).intValue();
            }
            MiuiEsimManagerBase.loge("onSwitchEsimGPIO: return type not Integer, actual: ".concat(objCallHook != null ? objCallHook.getClass().getName() : "null"));
        }
        return -1;
    }

    @Override // com.android.phone.MiuiEsimManagerBase
    public void restUimPowerState(int i) {
        MiuiEsimManagerBase.logi("restUimPowerState  reason = " + i);
        MiuiEsimManagerBase.trackerEsimEvent("restUimPowerState", Integer.valueOf(i));
        if (i != 1001) {
            if (i != 1002) {
                return;
            }
            restUimPowerState();
        } else if (MiuiEsimManager.isCanRestUimPowerForOpenChannelFile && MiuiEsimManagerBase.getEsimSecureValue() == 1) {
            restUimPowerState();
            MiuiEsimManager.isCanRestUimPowerForOpenChannelFile = false;
        }
    }

    private void restUimPowerState() {
        if (!onUimPowerRequest(0, -1)) {
            boolean zOnUimPowerRequest = false;
            for (int i = 0; i < 5; i++) {
                zOnUimPowerRequest = onUimPowerRequest(0, -1);
                if (zOnUimPowerRequest) {
                    break;
                }
            }
            if (!zOnUimPowerRequest) {
                MiuiEsimManagerBase.logi("restUimPowerState UIM_POWER_DOWN fail...");
            }
        }
        if (onUimPowerRequest(1, 1)) {
            return;
        }
        boolean zOnUimPowerRequest2 = false;
        for (int i2 = 0; i2 < 5; i2++) {
            zOnUimPowerRequest2 = onUimPowerRequest(1, 1);
            if (zOnUimPowerRequest2) {
                break;
            }
        }
        if (zOnUimPowerRequest2) {
            return;
        }
        MiuiEsimManagerBase.logi("restUimPowerState UIM_POWER_UP fail...");
    }

    private static boolean onUimPowerRequest(int i, int i2) {
        ModemUtils modemUtils = mHydra;
        if (modemUtils != null) {
            Object objCallHook = modemUtils.callHook("onHookUimPowerReqEx", Integer.valueOf(i), 2, Integer.valueOf(i2));
            if (objCallHook instanceof Boolean) {
                return ((Boolean) objCallHook).booleanValue();
            }
            MiuiEsimManagerBase.loge("onUimPowerRequest: return type not Boolean, actual: ".concat(objCallHook != null ? objCallHook.getClass().getName() : "null"));
        }
        return false;
    }

    private static boolean retrySetEsimState(int i, int i2) {
        StringBuilder sb = new StringBuilder("retrySetEsimState ");
        sb.append(i2 == 0 ? "enable" : "disable");
        sb.append(" eSIM  errCode = ");
        sb.append(i);
        MiuiEsimManagerBase.logi(sb.toString());
        if (i == -8 || i == -7) {
            int i3 = i2 == 0 ? 1 : 0;
            boolean zOnUimPowerRequest = false;
            for (int i4 = 0; i4 < 5; i4++) {
                if (i4 < 4) {
                    zOnUimPowerRequest = onUimPowerRequest(1, i3);
                } else {
                    onUimPowerRequest(0, -1);
                    onSwitchEsimGPIO(i2 == 0 ? 1 : 0);
                    onUimPowerRequest(1, i3);
                }
                if (zOnUimPowerRequest) {
                    MiuiEsimManagerBase.setEsimSecureValue(i2);
                    return true;
                }
            }
            return false;
        }
        if (i == -6 || i == -5) {
            for (int i5 = 0; i5 < 5; i5++) {
                if (onUimPowerRequest(0, -1)) {
                    return true;
                }
            }
            return false;
        }
        if (i != -3 && i != -2) {
            return false;
        }
        boolean z = false;
        for (int i6 = 0; i6 < 4; i6++) {
            if (i6 < 3) {
                z = onSwitchEsimGPIO(i2) == 0;
            } else {
                onUimPowerRequest(1, -1);
            }
            if (z) {
                return true;
            }
        }
        return false;
    }

    class QcAidlEsimInner {
        private final BroadcastReceiver mBroadcastReceiver;
        private Client mClient;
        private ExtTelephonyManager mExtTelephonyManager;
        private ServiceCallback mServiceConnection;
        private UiccController mUiccController;
        private boolean mIsBound = false;
        private ExtPhoneCallbackListener mExtPhoneCallbackListener = null;
        private IccCardStatus.CardState mCardState = null;
        public final Object mLock = new Object();
        public final Object mUimPowerReqLock = new Object();
        private boolean mIsSetSimTypeSuccess = false;
        private boolean mIsConnectInProgress = false;
        private Handler handler = new Handler() { // from class: com.android.phone.QcMiuiEsimManager.QcAidlEsimInner.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i = message.what;
                if (i == 0) {
                    MiuiEsimManagerBase.logd("EVENT_ICC_CHANGED");
                    if (QcAidlEsimInner.this.getCurrentSimType() == 0 && MiuiPhoneUtils.isSupportEuiccBinding() && MiuiEsimManagerBase.mIsEuiccPowerOff) {
                        UiccCard uiccCard = QcAidlEsimInner.this.mUiccController.getUiccCard(1);
                        if (uiccCard != null) {
                            MiuiEsimManagerBase.logi("EVENT_ICC_CHANGED CardState == " + uiccCard.getCardState());
                            if (uiccCard.getCardState() == IccCardStatus.CardState.CARDSTATE_PRESENT) {
                                MiuiEsimManagerBase.logi("onHookUimPowerReqEx on slot 1 for P83X CN EuiccBinding success and mCardState: CARDSTATE_PRESENT");
                                MiuiEsimManagerBase.mIsEuiccPowerOff = false;
                                synchronized (QcAidlEsimInner.this.mUimPowerReqLock) {
                                    QcAidlEsimInner.this.mUimPowerReqLock.notify();
                                }
                                return;
                            }
                            return;
                        }
                        MiuiEsimManagerBase.loge("uiccCard == null");
                        return;
                    }
                    return;
                }
                if (i == 1) {
                    QcAidlEsimInner.this.bindService();
                    return;
                }
                if (i != 2) {
                    return;
                }
                boolean zBooleanValue = ((Boolean) message.obj).booleanValue();
                MiuiEsimManagerBase.logd("EVENT_RESET_ESE_ENABLE_NFC, isNeedDisableNfc = " + zBooleanValue);
                if (QcMiuiEsimManager.this.mNfcAdapter != null) {
                    if (QcMiuiEsimManager.this.mNfcAdapter.isEnabled()) {
                        Intent intent = new Intent("com.miui.esim.action.RESET_ESE");
                        intent.setPackage("com.android.nfc");
                        QcMiuiEsimManager.mContext.sendBroadcast(intent);
                        if (zBooleanValue) {
                            QcMiuiEsimManager.this.mNfcAdapter.disable();
                            return;
                        }
                        return;
                    }
                    MiuiEsimManagerBase.logd("mNfcAdapter is also disable");
                    return;
                }
                MiuiEsimManagerBase.logd("mNfcAdapter is null");
            }
        };

        protected QcAidlEsimInner() {
            this.mUiccController = null;
            AnonymousClass2 anonymousClass2 = new AnonymousClass2();
            this.mBroadcastReceiver = anonymousClass2;
            this.mServiceConnection = new ServiceCallback() { // from class: com.android.phone.QcMiuiEsimManager.QcAidlEsimInner.3
                public void onConnected() {
                    MiuiEsimManagerBase.logd("onConnected");
                    try {
                        int[] iArr = {34};
                        if (QcAidlEsimInner.this.mExtPhoneCallbackListener == null) {
                            QcAidlEsimInner qcAidlEsimInner = QcAidlEsimInner.this;
                            qcAidlEsimInner.mExtPhoneCallbackListener = qcAidlEsimInner.onCreateExtPhoneCallbackListener();
                        }
                        QcAidlEsimInner qcAidlEsimInner2 = QcAidlEsimInner.this;
                        qcAidlEsimInner2.mClient = qcAidlEsimInner2.mExtTelephonyManager.registerCallbackWithEvents(QcMiuiEsimManager.mContext.getPackageName(), QcAidlEsimInner.this.mExtPhoneCallbackListener, iArr);
                        QcAidlEsimInner.this.mIsBound = true;
                        QcAidlEsimInner.this.mIsConnectInProgress = false;
                    } catch (Exception e) {
                        MiuiEsimManagerBase.logd("onConnected: Exception = " + e);
                    }
                }

                public void onDisconnected() {
                    MiuiEsimManagerBase.logd("onServiceDisconnected.");
                    if (QcAidlEsimInner.this.mIsBound) {
                        QcAidlEsimInner.this.mExtTelephonyManager.unregisterCallback(QcAidlEsimInner.this.mExtPhoneCallbackListener);
                    }
                    QcAidlEsimInner.this.mExtPhoneCallbackListener = null;
                    QcAidlEsimInner.this.mIsBound = false;
                    QcAidlEsimInner.this.mClient = null;
                    QcAidlEsimInner.this.mIsConnectInProgress = false;
                    QcAidlEsimInner.this.handler.sendEmptyMessageDelayed(1, MirrorDesktopHelper.TIMEOUT_MILLIS);
                }
            };
            MiuiEsimManagerBase.logd("QcAidlEsimInner init");
            this.mExtTelephonyManager = ExtTelephonyManager.getInstance(QcMiuiEsimManager.mContext);
            bindService();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.android.euicc.service.SIM_TYPE_UPDATE_ACTION");
            intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
            QcMiuiEsimManager.mContext.registerReceiver(anonymousClass2, intentFilter, 2);
            UiccController uiccController = UiccController.getInstance();
            this.mUiccController = uiccController;
            uiccController.registerForIccChanged(this.handler, 0, (Object) null);
        }

        /* JADX INFO: renamed from: com.android.phone.QcMiuiEsimManager$QcAidlEsimInner$2, reason: invalid class name */
        class AnonymousClass2 extends BroadcastReceiver {
            AnonymousClass2() {
            }

            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if ("com.android.euicc.service.SIM_TYPE_UPDATE_ACTION".equals(action)) {
                    String str = SystemProperties.get("ro.miui.region", "");
                    if (!QcMiuiEsimManager.this.isSupportEsimForCountry(str)) {
                        MiuiEsimManagerBase.logd("received sim type switch request from gLPA, region is not support, region = " + str);
                        return;
                    }
                    int intExtra = intent.getIntExtra("com.android.euicc.service.extra_slot_id", -1);
                    final int intExtra2 = intent.getIntExtra("com.android.euicc.service.extra_sim_type", -1);
                    MiuiEsimManagerBase.logd("received sim type switch request from gLPA, slotId = " + intExtra + " simType = " + intExtra2);
                    if (intExtra2 == 0 || intExtra2 == 1) {
                        new Thread(new Runnable() { // from class: com.android.phone.QcMiuiEsimManager$QcAidlEsimInner$2$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$onReceive$0(intExtra2);
                            }
                        }).start();
                        return;
                    } else {
                        MiuiEsimManagerBase.loge("ACTION_SIM_TYPE_SWITCH_REQ fail");
                        return;
                    }
                }
                if ("android.intent.action.BOOT_COMPLETED".equals(action)) {
                    MiuiEsimManagerBase.logd("received boot complete action");
                    if (QcAidlEsimInner.this.getCurrentSimType() == 0 && MiuiPhoneUtils.isSupportEuiccBinding()) {
                        QcAidlEsimInner.this.eidCheckforEuiccBinding();
                        return;
                    }
                    return;
                }
                MiuiEsimManagerBase.logd("received invalid action: " + action);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onReceive$0(int i) {
                try {
                    if (i != 0) {
                        if (i == 1) {
                            QcAidlEsimInner.this.setEsimState(0);
                            return;
                        }
                        return;
                    }
                    List allEsimProfiles = QcMiuiEsimManager.this.getAllEsimProfiles();
                    QcAidlEsimInner.this.setEsimState(1);
                    if (allEsimProfiles == null || allEsimProfiles.isEmpty() || (allEsimProfiles.size() == 1 && ((EuiccProfileInfo) allEsimProfiles.get(0)).getProfileClass() == 0)) {
                        MiuiEsimManagerBase.logd("received sim type switch request from gLPA, but no esim profile or profile is testing");
                        MiuiEsimManagerBase.setEsimSecureValue(-1);
                    }
                } catch (Exception e) {
                    MiuiEsimManagerBase.loge("ACTION_SIM_TYPE_SWITCH_REQ fail = " + e);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void bindService() {
            MiuiEsimManagerBase.logd("bindService mIsBound: " + this.mIsBound + ", mIsConnectInProgress = " + this.mIsConnectInProgress);
            if (this.mIsBound || this.mIsConnectInProgress) {
                return;
            }
            this.mExtTelephonyManager.connectService(this.mServiceConnection);
            this.mIsConnectInProgress = true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public ExtPhoneCallbackListener onCreateExtPhoneCallbackListener() {
            return new ExtPhoneCallbackListener() { // from class: com.android.phone.QcMiuiEsimManager.QcAidlEsimInner.4
                public void setSimTypeResponse(Token token, Status status) {
                    MiuiEsimManagerBase.logd("setSimTypeResponse: token = " + token + " status" + status);
                    if (status.get() == 1) {
                        QcAidlEsimInner.this.mIsSetSimTypeSuccess = true;
                        if (QcAidlEsimInner.this.getCurrentSimType() == 0 && MiuiEsimManagerBase.getEsimSecureValue() != 1) {
                            MiuiEsimManagerBase.setEsimSecureValue(0);
                        } else if (QcAidlEsimInner.this.getCurrentSimType() == 1 && MiuiEsimManagerBase.getEsimSecureValue() != 0) {
                            MiuiEsimManagerBase.setEsimSecureValue(1);
                        }
                    }
                    synchronized (QcAidlEsimInner.this.mLock) {
                        QcAidlEsimInner.this.mLock.notify();
                    }
                }
            };
        }

        public int setEsimState(int i) {
            int esimStateEx = setEsimStateEx(i);
            MiuiEsimManagerBase.trackerEsimEvent(i == 0 ? "enable_esim" : "disable_esim", Integer.valueOf(esimStateEx));
            return esimStateEx;
        }

        public int setEsimStateEx(int i) {
            QtiSimType qtiSimType;
            MiuiEsimManagerBase.logd("setEsimState: state = " + i);
            MiuiEsimManagerBase.mHandler.removeMessages(2);
            int i2 = 0;
            if (i == 1 && MiuiEsimManagerBase.isRemoveEsimSwitch()) {
                return 0;
            }
            if (i == getCurrentSimType()) {
                MiuiEsimManagerBase.logd("setEsimState, esim type is issued repeatedly");
                if (MiuiEsimManagerBase.getEsimSecureValue() != -1 && MiuiEsimManagerBase.getEsimSecureValue() == i) {
                    MiuiEsimManagerBase.setEsimSecureValue(i);
                }
                return 0;
            }
            boolean z = i == 0 && MiuiPhoneUtils.shouldWakeUpESEonNewPlatform();
            if (z && !QcMiuiEsimManager.this.openChannel()) {
                MiuiEsimManagerBase.logd("setEsimState: wakeEse fail");
                return -1;
            }
            if (MiuiPhoneUtils.isSupportMep() && i == 1) {
                MiuiEsimManagerBase.disableProfile();
                if (QcMiuiEsimManager.this.isNeedDisableEsimWait()) {
                    synchronized (this.mLock) {
                        try {
                            this.mLock.wait(1000L);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (MiuiPhoneUtils.ShouldSwitchToRemovableSlot()) {
                    MiuiEsimManagerBase.switchToRemovableSlot();
                }
            }
            synchronized (this.mLock) {
                try {
                    if (MiuiEsimManagerBase.isNeedSwitchMapping) {
                        try {
                            boolean zIsNeedDelaySlotMappingTime = MiuiPhoneUtils.isNeedDelaySlotMappingTime();
                            Object obj = this.mLock;
                            if (zIsNeedDelaySlotMappingTime) {
                                obj.wait(MirrorDesktopHelper.TIMEOUT_MILLIS);
                            } else {
                                obj.wait(2000L);
                            }
                            MiuiEsimManagerBase.mIsSwitchingSlotMapping = false;
                            MiuiEsimManagerBase.isNeedSwitchMapping = false;
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            if (i == 1 && MiuiEsimManagerBase.mIsEuiccPowerOff) {
                if (!onHookUimPowerReqEx(1, 2, 1)) {
                    MiuiEsimManagerBase.mIsEuiccPowerOff = true;
                    MiuiEsimManagerBase.logi("onHookUimPowerReqEx on slot 1 for P83X CN EuiccBinding fail");
                    return -1;
                }
                synchronized (this.mUimPowerReqLock) {
                    try {
                        this.mUimPowerReqLock.wait(MirrorDesktopHelper.TIMEOUT_MILLIS);
                    } catch (InterruptedException unused) {
                    }
                }
                if (MiuiEsimManagerBase.mIsEuiccPowerOff) {
                    MiuiEsimManagerBase.logi("onHookUimPowerReqEx on slot 1 for P83X CN EuiccBinding fail");
                    return -1;
                }
                synchronized (this.mLock) {
                    try {
                        this.mLock.wait(1000L);
                    } catch (InterruptedException unused2) {
                    }
                }
            }
            this.mIsSetSimTypeSuccess = false;
            QtiSimType[] qtiSimTypeArr = new QtiSimType[2];
            qtiSimTypeArr[0] = new QtiSimType(0);
            if (i == 1) {
                qtiSimType = new QtiSimType(0);
            } else {
                qtiSimType = new QtiSimType(1);
            }
            qtiSimTypeArr[1] = qtiSimType;
            MiuiEsimManagerBase.logd("setEsimState: qtiSimTypes = " + qtiSimTypeArr);
            try {
                this.mExtTelephonyManager.setSimType(this.mClient, qtiSimTypeArr);
                synchronized (this.mLock) {
                    try {
                        this.mLock.wait(4000L);
                    } catch (InterruptedException unused3) {
                    }
                }
                if (this.mIsSetSimTypeSuccess) {
                    if (i == 0 && MiuiPhoneUtils.isSupportEuiccBinding()) {
                        eidCheckforEuiccBinding();
                    }
                    MiuiEsimManagerBase.setEsimSecureValue(i);
                    if (i == 1) {
                        broadcastResetEse();
                    }
                    if (i == 0 && MiuiEsimManagerBase.isNeedDownGradeSVNDevice()) {
                        MiuiEsimManagerBase.ESimHandler eSimHandler = MiuiEsimManagerBase.mHandler;
                        eSimHandler.sendMessageDelayed(eSimHandler.obtainMessage(11), MirrorDesktopHelper.TIMEOUT_MILLIS);
                    }
                } else {
                    if (MiuiPhoneUtils.isSupportMep() && i == 1) {
                        MiuiEsimManagerBase.switchSlotMappingForCloseEsimFail();
                    }
                    i2 = -1;
                }
                StringBuilder sb = new StringBuilder("setEsimState for ");
                sb.append(i == 0 ? "enable_esim" : "disable_esim");
                sb.append(" result = ");
                sb.append(i2);
                MiuiEsimManagerBase.logd(sb.toString());
                if (z) {
                    QcMiuiEsimManager.this.closeChannel();
                }
                return i2;
            } catch (Exception e3) {
                MiuiEsimManagerBase.logd("setEsimState: Exception = " + e3);
                return -1;
            }
        }

        /* JADX WARN: Code duplicated, block: B:29:0x0084  */
        public int getCurrentSimType() {
            char c;
            int i;
            Object obj;
            boolean z;
            ByteBuffer byteBuffer;
            int i2 = 0;
            if (!HyperOSCustFeatureResolve.getBoolean("config_qc_esim_nv_update", false)) {
                c = 2;
                i = 145;
                obj = "/nv/item_files/modem/uim/uimdrv/uim_hw_config";
                z = false;
            } else {
                i = 3;
                z = true;
                obj = "/nv/item_files/modem/uim/uimdrv/uim_current_sim_type";
                c = 3;
            }
            if (QcMiuiEsimManager.mHydra != null) {
                Object objCallHook = QcMiuiEsimManager.mHydra.callHook("onHookEfsOptSync", 0, obj, 4);
                if (objCallHook instanceof ByteBuffer) {
                    byteBuffer = (ByteBuffer) objCallHook;
                } else {
                    MiuiEsimManagerBase.loge("getCurrentSimType: return type not ByteBuffer, actual: ".concat(objCallHook != null ? objCallHook.getClass().getName() : "null"));
                    byteBuffer = null;
                }
                if (byteBuffer == null) {
                    MiuiEsimManagerBase.logi("getCurrentSimType failed");
                    return -4;
                }
                if (byteBuffer.getInt() != 0 || byteBuffer.getInt() <= 0) {
                    i2 = -4;
                } else {
                    int iRemaining = byteBuffer.remaining();
                    byte[] bArr = new byte[iRemaining];
                    byteBuffer.get(bArr, 0, iRemaining);
                    if (iRemaining <= i) {
                        i2 = -4;
                    } else {
                        int i3 = bArr[i - 1] & TransitionInfo.INIT;
                        if (i3 == z) {
                            i2 = 1;
                        } else if (i3 != c) {
                            i2 = -4;
                        }
                    }
                }
            } else {
                i2 = -4;
            }
            MiuiEsimManagerBase.logi("getCurrentSimType final state = " + i2);
            return i2;
        }

        private void broadcastResetEse() {
            if (HyperOSCustFeatureResolve.getBoolean("config_reset_ese_when_disable_esim", false)) {
                MiuiEsimManagerBase.mHandler.removeMessages(2);
                if (QcMiuiEsimManager.this.mNfcAdapter == null) {
                    QcMiuiEsimManager.this.mNfcAdapter = NfcAdapter.getDefaultAdapter(QcMiuiEsimManager.mContext);
                    MiuiEsimManagerBase.logd("mNfcAdapter = " + QcMiuiEsimManager.this.mNfcAdapter);
                }
                if (QcMiuiEsimManager.this.mNfcAdapter != null && !QcMiuiEsimManager.this.mNfcAdapter.isEnabled()) {
                    MiuiEsimManagerBase.logd("mNfcAdapter is disable need open");
                    QcMiuiEsimManager.this.mNfcAdapter.enable();
                    MiuiEsimManagerBase.ESimHandler eSimHandler = MiuiEsimManagerBase.mHandler;
                    eSimHandler.sendMessageDelayed(eSimHandler.obtainMessage(2, Boolean.TRUE), 2500L);
                    return;
                }
                MiuiEsimManagerBase.logd("mNfcAdapter is enable");
                MiuiEsimManagerBase.ESimHandler eSimHandler2 = MiuiEsimManagerBase.mHandler;
                eSimHandler2.sendMessage(eSimHandler2.obtainMessage(2, Boolean.FALSE));
            }
        }

        private String getModemEid(int i) {
            String modemEid = "";
            if (QcMiuiEsimManager.mHydra == null) {
                QcMiuiEsimManager.mHydra = ModemUtils.init(QcMiuiEsimManager.mContext);
            }
            for (int i2 = 0; i2 < 25; i2++) {
                if (QcMiuiEsimManager.mHydra != null) {
                    modemEid = QcMiuiEsimManager.mHydra.getModemEid(i);
                }
                if (modemEid != null && !"".equals(modemEid)) {
                    MiuiEsimManagerBase.logi("getModemEid success , eid = " + modemEid + " , i = " + i2);
                    return modemEid;
                }
                synchronized (this.mLock) {
                    try {
                        this.mLock.wait(200L);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            MiuiEsimManagerBase.logi("getModemEid fail, eid = " + modemEid);
            return modemEid;
        }

        private boolean onHookUimPowerReqEx(int i, int i2, int i3) {
            MiuiEsimManagerBase.logi("onHookUimPowerReqEx");
            if (QcMiuiEsimManager.mHydra == null) {
                QcMiuiEsimManager.mHydra = ModemUtils.init(QcMiuiEsimManager.mContext);
            }
            if (QcMiuiEsimManager.mHydra != null) {
                return QcMiuiEsimManager.mHydra.onHookUimPowerReqEx(i, i2, i3);
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void eidCheckforEuiccBinding() {
            if (QcMiuiEsimManager.this.miuiDeviceEuiccBindingMgrForSt == null) {
                QcMiuiEsimManager.this.miuiDeviceEuiccBindingMgrForSt = (MiuiDeviceEuiccBindingMgrForSt) MiuiDeviceEuiccBindingMgrBase.getInstance();
            }
            final boolean zEsimGetBindStatus = QcMiuiEsimManager.this.miuiDeviceEuiccBindingMgrForSt.esimGetBindStatus();
            final String strEsimReadEid = QcMiuiEsimManager.this.miuiDeviceEuiccBindingMgrForSt.esimReadEid();
            new Thread(new Runnable() { // from class: com.android.phone.QcMiuiEsimManager$QcAidlEsimInner$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$eidCheckforEuiccBinding$1(zEsimGetBindStatus, strEsimReadEid);
                }
            }).start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$eidCheckforEuiccBinding$1(boolean z, String str) {
            String modemEid = getModemEid(1);
            if (z) {
                if (str == null || str.isEmpty() || modemEid == null || modemEid.isEmpty() || !modemEid.equals(str)) {
                    this.handler.post(new Runnable() { // from class: com.android.phone.QcMiuiEsimManager$QcAidlEsimInner$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$eidCheckforEuiccBinding$0();
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$eidCheckforEuiccBinding$0() {
            if (onHookUimPowerReqEx(0, 2, 1)) {
                MiuiEsimManagerBase.logi("onHookUimPowerReqEx off slot 1 for P83X CN EuiccBinding success");
                MiuiEsimManagerBase.mIsEuiccPowerOff = true;
                Toast.makeText(QcMiuiEsimManager.mContext, R.string.euicc_binding_check_error_toast_message, 1).show();
            } else {
                MiuiEsimManagerBase.mIsEuiccPowerOff = false;
                MiuiEsimManagerBase.logi("onHookUimPowerReqEx off slot 1 for P83X CN EuiccBinding fail");
            }
        }
    }

    @Override // com.android.phone.MiuiEsimManager, com.android.phone.MiuiEsimManagerBase
    public int modifyEuiccSVN(int i) {
        synchronized (MiuiEsimManagerBase.mLock) {
            try {
                int i2 = 1;
                if (!openChannelForSVN()) {
                    return 1;
                }
                if (i == 2) {
                    int iSendAndParseApdu = sendAndParseApdu(i);
                    MiuiEsimManagerBase.logd("The result of sendAndParseApdu actionType :" + i + " , result:" + iSendAndParseApdu);
                    if (iSendAndParseApdu == 0) {
                        MiuiEsimManagerBase.setSVNDowngradeStatus(1);
                    }
                    i2 = iSendAndParseApdu;
                }
                closeChannelForSVN();
                return i2;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private int sendAndParseApdu(int i) {
        if (i == 1) {
            sendApdu(UPGRADE_VERSION_COMMAND);
            return 0;
        }
        if (i == 2) {
            return sendAndCheckApdu(new String[]{"802A1810C97F2181C593056130303032420E5448414C4553204341204B4C4F435F200D47454D414C544F5F5041544348950200805F2504202010025F2404204009277F4946B0410477A4E8A5924ABA246F07C1B198B00730E0D18F50346B10B0A81DC9ABF1376664F76D2B7608DBF8BED42787B78510D7E4AA6F3116BC9EB22F4AF1DDAA0685227DF001005F37407BB35109684D093477DDE8965A8C9C60AFD432A800B3E7E0160C8A0883580CF66C72EBB0310FD593B711CDD95392A3CA3465CDE87214CAD924D5A4C37CC19D7F", "8082181553A60D900211039501748001888101205F494104AB20C09E35105669D37B1D0D0F8A098E1007BC3D350617214D69795AE1A6E55F97252B66188499612A237CF64AE73438314B4CE782BCE1E00C92D3C1A5CAE74A", "84E6200028D961CE8509E84AD98AC28DBC1127C7ABF88E4B1B0F1489495F8BC520E2463ABB3B3FEA5222630233", "84E2100038D2C0F6E7FAB44858B956ADEEF2908AA995DBDE7F35E15591C490043143B336A89D32A9DD4B88D7F18C4563D399070008515592615BE4FC33", "84E6020058BF0453A5F78C2B3CD092733555771DA4417878F4BF4BA76C7911F62A8A4E51B75AB00067A94BC3A25B3CE6D9CF01829639DD39EE07327CDC44E04CB7ECBCEAC0A026388C2EEFDA34028B72A2A774E1AE5D29F431B95EA66C", "84E80000F8F4E13F2C2B5ED7DBDAB91AFB97B4200A2503EEC175F6974D98AA25F5D27D5B0CDDC4C95E6A741CDE6868700C2F504495BD08FA7D302AB9464A55D36F9E16FBE78DBA46AB8B1FB670EB10A68C96BD975F04CFCBA4AB5C5E3AD26E103F27F2C3BCA0AA9DCFBB5103B205EFFFB7B6BBA38014282F2178A579ED2512BC633292C296109E0A5C8F9D880B4A8D978913BE7BAAF861D1D718F650B1F87E7C2DD16F9BB779E0F2607F500CB32A3E2485FEA65E2D180F32E762E06E1534B4D553175FCD3A444C5F68AB481F9C568355C0888D43A777F204563D63FAAE43900CA9AFF0A42A8114F0C6754245DEEB529B487926579F2BD2F588052DDD5A", "84E80001F8C8AC840F19E390589445A84DAF87503C5C97471C549A22A0A45EC07308D7BCDBA4DC9338DDC5D54B4075DE64311769C2789E66E6682DBB2ECA35647A35F7854EF84CB847D9811CAE3A35A726B8DF9B41E60A67DBF307ECBA26D80A8A9183C5D906A7409AA3D292D443FB65B676314B98FEF36F9838C11D389CCAAED4A7FA9937AEBF0398F06AB5E21CC81062B986B7D348E93892CFC7C9A3B7AA127CF6F41E3C2A52A52C42D0DD4E468EE80C3573FF543819F7217AA96BB95AAB2EB9288FC31C5E482833E9452A70B94565B621094F3087FA2442DD35A491423C406C6B6D96160F309257A6C55BE524DD36904618AE94F753BA3F5B574611", "84E80002F8EAE5EA44F39829CC3111403B031EA9D412837B16B143D07F98AD6C5513278A5A7E2368F8030A4A37B5FCAB0A1D80E4A6F6B72FCDFCAB0375C00A37E5E41DA19F94E345B692A5EA96807400FAA2A6AAE96AC74469158F074BE6BF70178C531AFFC2CB2DA04BBDDB9FAC60709CC5533BDF641C362B5D8E822B945EBD85ECE9B40AB658C29F63DF5C6D9D95514E73606836731D9FAB4EE21F3CE30BE2963F6E23E4094771F00FCE7B747DA46A1C9358DC6EAA2A296F7A2D0F8A66A5B10FD226F5A2366FFAC9B7CF88DDAD5EA689A116BAC54AC390D040B1F9241307FB96768A942FFAC5AE76D6F994D98C84575DC0617D4E4D056B2B86AC34EE", "84E8800328FC42FDE0B5014E3CCD744C482963FC2A3F612B919161233CBECA924FCECCF931BF9F40A32FAE77F5"});
        }
        if (i != 3) {
            return 1;
        }
        sendApdu(GET_INFO1_COMMAND);
        return 0;
    }

    private int sendAndCheckApdu(String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            return 1;
        }
        for (String str : strArr) {
            String[] strArrSendApdu = sendApdu(Utils.hexStringToBytes(str));
            if (strArrSendApdu == null || strArrSendApdu.length < 2 || !"9000".equals(strArrSendApdu[1])) {
                return 1;
            }
        }
        return 0;
    }

    private String[] sendApdu(byte[] bArr) {
        String strIccTransmitApduLogicalChannelBySlot = MiuiEsimManagerBase.mTelMgr.iccTransmitApduLogicalChannelBySlot(1, channel.getChannel(), bArr[0] & TransitionInfo.INIT, bArr[1] & TransitionInfo.INIT, bArr[2] & TransitionInfo.INIT, bArr[3] & TransitionInfo.INIT, bArr.length > 4 ? bArr[4] & TransitionInfo.INIT : 0, getDataFromCApdu(bArr));
        if (strIccTransmitApduLogicalChannelBySlot == null) {
            MiuiEsimManagerBase.loge("sendApdu: null response");
            return null;
        }
        int length = strIccTransmitApduLogicalChannelBySlot.length() - 4;
        String[] strArr = {strIccTransmitApduLogicalChannelBySlot.substring(0, length), strIccTransmitApduLogicalChannelBySlot.substring(length)};
        MiuiEsimManagerBase.logi("sendApdu() original command returns: {" + strArr[0] + "}_{" + strArr[1] + "}");
        return strArr;
    }

    public boolean openChannelForSVN() {
        MiuiEsimManagerBase.logi("openChannel()");
        IccOpenLogicalChannelResponse iccOpenLogicalChannelResponseIccOpenLogicalChannelBySlot = MiuiEsimManagerBase.mTelMgr.iccOpenLogicalChannelBySlot(1, Utils.bytesToHexString(this.AID), 4);
        channel = iccOpenLogicalChannelResponseIccOpenLogicalChannelBySlot;
        if (iccOpenLogicalChannelResponseIccOpenLogicalChannelBySlot == null || iccOpenLogicalChannelResponseIccOpenLogicalChannelBySlot.getChannel() == -1) {
            return handleOpenChannelResult(channel);
        }
        MiuiEsimManagerBase.logi("channel getChannel:" + channel.getChannel());
        if (channel.getStatus() != 1) {
            return handleOpenChannelResult(channel);
        }
        if (channel.getSelectResponse() != null) {
            MiuiEsimManagerBase.logi("Select: " + Utils.bytesToHexString(this.AID) + ", response: " + Utils.bytesToHexString(channel.getSelectResponse()));
        }
        mRetryOpenChannelForMissSourceCounts = 0;
        return true;
    }

    private boolean handleOpenChannelResult(IccOpenLogicalChannelResponse iccOpenLogicalChannelResponse) {
        if (iccOpenLogicalChannelResponse == null) {
            mRetryOpenChannelForMissSourceCounts = 0;
            MiuiEsimManagerBase.loge("openChannel: Channel is null (Not detected eSim card)");
            return false;
        }
        if (iccOpenLogicalChannelResponse.getStatus() == 3) {
            MiuiEsimManagerBase.loge("openChannel: Unknown AID " + Utils.bytesToHexString(this.AID));
        } else if (iccOpenLogicalChannelResponse.getStatus() == 2) {
            MiuiEsimManagerBase.loge("openChannel: No logical channel available STATUS_MISSING_RESOURCE counts = " + mRetryOpenChannelForMissSourceCounts);
            if (mRetryOpenChannelForMissSourceCounts > 1) {
                mRetryOpenChannelForMissSourceCounts = 0;
                return false;
            }
            synchronized (MiuiEsimManagerBase.mLock) {
                try {
                    MiuiEsimManagerBase.mLock.wait(2000L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            IccOpenLogicalChannelResponse iccOpenLogicalChannelResponseIccOpenLogicalChannelBySlot = MiuiEsimManagerBase.mTelMgr.iccOpenLogicalChannelBySlot(1, Utils.bytesToHexString(this.AID), 4);
            mRetryOpenChannelForMissSourceCounts++;
            if (iccOpenLogicalChannelResponseIccOpenLogicalChannelBySlot != null && iccOpenLogicalChannelResponseIccOpenLogicalChannelBySlot.getChannel() != -1) {
                if (iccOpenLogicalChannelResponseIccOpenLogicalChannelBySlot.getStatus() != 1) {
                    return handleOpenChannelResult(iccOpenLogicalChannelResponseIccOpenLogicalChannelBySlot);
                }
                mRetryOpenChannelForMissSourceCounts = 0;
                return true;
            }
            MiuiEsimManagerBase.loge("INVALID_CHANNEL retry openChannel: No logical channel available");
        } else {
            MiuiEsimManagerBase.loge("openChannel: Unknown open channel error");
        }
        mRetryOpenChannelForMissSourceCounts = 0;
        return false;
    }

    protected void closeChannelForSVN() {
        MiuiEsimManagerBase.logi("closeChannel()");
        IccOpenLogicalChannelResponse iccOpenLogicalChannelResponse = channel;
        if (iccOpenLogicalChannelResponse != null) {
            MiuiEsimManagerBase.mTelMgr.iccCloseLogicalChannelBySlot(1, iccOpenLogicalChannelResponse.getChannel());
        }
    }

    private String getDataFromCApdu(byte[] bArr) {
        if (bArr.length < 5) {
            return "";
        }
        return Utils.bytesToHexString(bArr).substring(10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isNeedDisableEsimWait() {
        List<SubscriptionInfo> availableSubscriptionInfoList = miui.telephony.SubscriptionManager.getDefault().getAvailableSubscriptionInfoList();
        boolean z = false;
        if (availableSubscriptionInfoList != null && availableSubscriptionInfoList.size() > 0) {
            for (SubscriptionInfo subscriptionInfo : availableSubscriptionInfoList) {
                if (subscriptionInfo.isEmbedded() && miui.telephony.SubscriptionManager.isValidSlotId(subscriptionInfo.getPhoneId())) {
                    z = true;
                }
            }
        }
        MiuiEsimManagerBase.logi("isNeedDisableEsimWait isNeedWait = " + z);
        return z;
    }

    public boolean openChannel() {
        closeChannel();
        this.mEsimSession = null;
        try {
            for (Reader reader : this.mSeService.getReaders()) {
                if (reader.getName().startsWith("eSE")) {
                    this.mEsimSession = reader.openSession();
                    break;
                }
            }
            if (this.mEsimSession == null) {
                MiuiEsimManagerBase.logi("wake up ese and session is null");
                return false;
            }
            MiuiEsimManagerBase.logi("openLogicalChannel");
            this.mEsimChannel = this.mEsimSession.openLogicalChannel(SELECT_ISD);
            MiuiEsimManagerBase.logi("mEsimChannel = " + this.mEsimChannel);
            if (this.mEsimChannel == null) {
                MiuiEsimManagerBase.logi("open esim channel failed");
                this.mEsimSession.close();
                this.mEsimSession = null;
                return false;
            }
            MiuiEsimManagerBase.logi("open esim channel sucess");
            return true;
        } catch (Exception e) {
            closeChannel();
            MiuiEsimManagerBase.logi("Unexpected Exception " + e);
            return false;
        }
    }

    public void closeChannel() {
        MiuiEsimManagerBase.logi("closeChannel mEsimChannel = " + this.mEsimChannel);
        if (this.mEsimChannel != null) {
            MiuiEsimManagerBase.logi("close channel");
            this.mEsimChannel.close();
            this.mEsimChannel = null;
        }
        if (this.mEsimSession != null) {
            MiuiEsimManagerBase.logi("close session");
            this.mEsimSession.close();
            this.mEsimSession = null;
        }
    }
}
