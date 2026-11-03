package com.android.internal.telephony;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.WorkSource;
import android.sysprop.TelephonyProperties;
import android.telephony.ClientRequestStats;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.SparseArray;
import com.android.internal.util.FunctionalUtils;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import vendor.xiaomi.hardware.radio.IXmRadio;
import vendor.xiaomi.hardware.radio.RadioResponseInfo;

/* JADX INFO: loaded from: classes.dex */
public class XmRIL implements XmCommandsInterface {
    private static final int DEFAULT_ACK_WAKE_LOCK_TIMEOUT_MS = 200;
    private static final int DEFAULT_WAKE_LOCK_TIMEOUT_MS = 60000;
    static final int EVENT_ACK_WAKE_LOCK_TIMEOUT = 4;
    static final int EVENT_AIDL_PROXY_DEAD = 7;
    static final int EVENT_WAKE_LOCK_TIMEOUT = 2;
    static final int EVENT_XM_RADIO_RETRY_GET_SERVICE = 8;
    public static final int FOR_ACK_WAKELOCK = 1;
    public static final int FOR_WAKELOCK = 0;
    public static final int INVALID_WAKELOCK = -1;
    protected static final int XM_RADIO_GET_SERVICE_DELAY_MILLIS = 1000;
    private static final int XM_RADIO_SERVICE_CONNECTION_RETRY_LIMIT = 3;
    static final String XM_RILJ_ACK_WAKELOCK_NAME = "XM_RILJ_ACK_WL";
    static final boolean XM_RILJ_LOGD = true;
    static final boolean XM_RILJ_LOGV = false;
    static final String XM_RILJ_LOG_TAG = "XmRILJ";
    static final String XM_RILJ_WAKELOCK_TAG = "*xm-telephony-radio*";
    public final PowerManager.WakeLock mAckWakeLock;
    final int mAckWakeLockTimeout;
    private WorkSource mActiveWakelockWorkSource;
    protected Context mContext;
    protected boolean mIsCellularSupported;
    protected final Integer mPhoneId;
    protected WorkSource mRILDefaultWorkSource;
    final RilHandler mRilHandler;
    public final PowerManager.WakeLock mWakeLock;
    int mWakeLockCount;
    final int mWakeLockTimeout;
    private final BinderServiceDeathRecipient mXmRadioDeathRecipients;
    protected HalVersion mXmRadioHalVersion;
    private XmRadioIndication mXmRadioIndication;
    private XmRadioProxy mXmRadioProxy;
    private XmRadioResponse mXmRadioResponse;
    private final AtomicLong mXmRadioServiceCookies;
    static final String[] AIDL_SERVICE_NAME = {"slot1", "slot2", "slot3"};
    public static final HalVersion XM_RADIO_HAL_VERSION_UNKNOWN = HalVersion.UNKNOWN;
    public static final HalVersion XM_RADIO_HAL_VERSION_1 = new HalVersion(1, 0);
    public static final HalVersion XM_RADIO_HAL_VERSION_2 = new HalVersion(2, 0);
    private final ClientWakelockTracker mClientWakelockTracker = new ClientWakelockTracker();
    volatile int mWlSequenceNum = 0;
    volatile int mAckWlSequenceNum = 0;
    SparseArray<XmRILRequest> mRequestList = new SparseArray<>();
    protected RegistrantList mUnsolCommMsgRegistrants = new RegistrantList();
    protected RegistrantList mHstInfoRegistrants = new RegistrantList();
    protected RegistrantList mQosInfoRegistrants = new RegistrantList();
    protected RegistrantList mModemExceptionRegistrants = new RegistrantList();
    protected RegistrantList mModemSceneRegistrants = new RegistrantList();
    private int mCurrentRetryCount = 0;

    public class RilHandler extends Handler {
        public RilHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    synchronized (XmRIL.this.mRequestList) {
                        if (msg.arg1 == XmRIL.this.mWlSequenceNum && XmRIL.this.clearWakeLock(0)) {
                            int count = XmRIL.this.mRequestList.size();
                            XmRIL.this.riljLog("WAKE_LOCK_TIMEOUT mRequestList=" + count);
                            for (int i = 0; i < count; i++) {
                                XmRILRequest rr = XmRIL.this.mRequestList.valueAt(i);
                                XmRIL.this.riljLog(i + ": [" + rr.mSerial + "] " + XmRILUtils.requestToString(rr.mRequest));
                            }
                        }
                        break;
                    }
                    return;
                case 3:
                case 5:
                case 6:
                default:
                    return;
                case 4:
                    if (msg.arg1 == XmRIL.this.mAckWlSequenceNum) {
                        XmRIL.this.clearWakeLock(1);
                        return;
                    }
                    return;
                case 7:
                    long msgCookie = ((Long) msg.obj).longValue();
                    XmRIL.this.riljLog("handleMessage: EVENT_AIDL_PROXY_DEAD cookie = " + msgCookie);
                    if (msgCookie == XmRIL.this.mXmRadioServiceCookies.get()) {
                        XmRIL.this.resetProxyAndRequestList();
                        return;
                    }
                    return;
                case 8:
                    XmRIL.this.riljLog("handleMessage: EVENT_XM_RADIO_RETRY_GET_SERVICE, currentRetryCount = " + XmRIL.this.mCurrentRetryCount);
                    XmRIL.this.mRilHandler.removeMessages(8);
                    boolean zIsRildReady = XmRIL.this.isRildReady();
                    XmRIL xmRIL = XmRIL.this;
                    if (zIsRildReady) {
                        xmRIL.getXmRadioProxy();
                        return;
                    } else {
                        if (xmRIL.mCurrentRetryCount < 3) {
                            XmRIL.this.mRilHandler.sendMessageDelayed(XmRIL.this.mRilHandler.obtainMessage(8), 1000L);
                            XmRIL.this.mCurrentRetryCount++;
                            return;
                        }
                        return;
                    }
            }
        }
    }

    private final class BinderServiceDeathRecipient implements IBinder.DeathRecipient {
        private IBinder mBinder;

        BinderServiceDeathRecipient() {
        }

        public void linkToDeath(IBinder service) throws RemoteException {
            XmRIL xmRIL = XmRIL.this;
            if (service != null) {
                xmRIL.riljLog("Linked to death for service radio");
                this.mBinder = service;
                this.mBinder.linkToDeath(this, (int) XmRIL.this.mXmRadioServiceCookies.incrementAndGet());
                return;
            }
            xmRIL.riljLoge("Unable to link to death for service radio");
        }

        public synchronized void unlinkToDeath() {
            if (this.mBinder != null) {
                this.mBinder.unlinkToDeath(this, 0);
                this.mBinder = null;
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            XmRIL.this.riljLog("Service radio has died.");
            XmRIL.this.mRilHandler.sendMessage(XmRIL.this.mRilHandler.obtainMessage(7, 0, 0, Long.valueOf(XmRIL.this.mXmRadioServiceCookies.get())));
            unlinkToDeath();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void resetProxyAndRequestList() {
        this.mXmRadioProxy.clear();
        this.mXmRadioServiceCookies.incrementAndGet();
        XmRILRequest.resetSerial();
        clearRequestList(1, XM_RILJ_LOGV);
        getXmRadioProxy();
    }

    public synchronized XmRadioProxy getXmRadioProxy() {
        if (!SubscriptionManager.isValidPhoneId(this.mPhoneId.intValue())) {
            return this.mXmRadioProxy;
        }
        boolean z = this.mIsCellularSupported;
        XmRadioProxy xmRadioProxy = this.mXmRadioProxy;
        if (!z) {
            return xmRadioProxy;
        }
        if (!xmRadioProxy.isEmpty()) {
            return this.mXmRadioProxy;
        }
        if (!isRildReady()) {
            this.mRilHandler.removeMessages(8);
            this.mCurrentRetryCount = 0;
            this.mRilHandler.sendMessageDelayed(this.mRilHandler.obtainMessage(8), 1000L);
            return this.mXmRadioProxy;
        }
        try {
            IBinder binder = ServiceManager.waitForDeclaredService(IXmRadio.DESCRIPTOR + "/" + AIDL_SERVICE_NAME[this.mPhoneId.intValue()]);
            if (binder != null) {
                this.mXmRadioHalVersion = this.mXmRadioProxy.setAidl(XM_RADIO_HAL_VERSION_UNKNOWN, IXmRadio.Stub.asInterface(binder));
            }
            if (!this.mXmRadioProxy.isEmpty()) {
                this.mXmRadioDeathRecipients.linkToDeath(this.mXmRadioProxy.getAidl().asBinder());
                this.mXmRadioProxy.getAidl().setResponseFunctions(this.mXmRadioResponse, this.mXmRadioIndication);
            }
        } catch (RemoteException e) {
            this.mXmRadioProxy.clear();
            riljLoge("XmRadioProxy getService/setResponseFunctions: " + e);
        }
        if (this.mXmRadioProxy.isEmpty()) {
            riljLoge("getXmRadioProxy: mXmRadioProxy == null");
        }
        return this.mXmRadioProxy;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isRildReady() {
        if (ServiceManager.checkService(IXmRadio.DESCRIPTOR + "/" + AIDL_SERVICE_NAME[this.mPhoneId.intValue()]) == null) {
            return XM_RILJ_LOGV;
        }
        return XM_RILJ_LOGD;
    }

    public XmRIL(Context context, Integer instanceId) {
        boolean z;
        this.mXmRadioHalVersion = XM_RADIO_HAL_VERSION_UNKNOWN;
        this.mXmRadioProxy = null;
        this.mContext = context;
        this.mPhoneId = Integer.valueOf(instanceId == null ? 0 : instanceId.intValue());
        TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
        boolean noRil = SystemProperties.getBoolean("ro.radio.noril", XM_RILJ_LOGV);
        if (noRil || (!tm.isVoiceCapable() && !tm.isSmsCapable() && !tm.isDataCapable())) {
            z = false;
        } else {
            z = true;
        }
        this.mIsCellularSupported = z;
        this.mXmRadioResponse = new XmRadioResponse(this);
        this.mXmRadioIndication = new XmRadioIndication(this);
        this.mRilHandler = new RilHandler();
        this.mXmRadioProxy = new XmRadioProxy();
        this.mXmRadioHalVersion = XM_RADIO_HAL_VERSION_UNKNOWN;
        this.mXmRadioDeathRecipients = new BinderServiceDeathRecipient();
        this.mXmRadioServiceCookies = new AtomicLong(0L);
        PowerManager pm = (PowerManager) context.getSystemService("power");
        this.mWakeLock = pm.newWakeLock(1, XM_RILJ_WAKELOCK_TAG);
        this.mWakeLock.setReferenceCounted(XM_RILJ_LOGV);
        this.mAckWakeLock = pm.newWakeLock(1, XM_RILJ_ACK_WAKELOCK_NAME);
        this.mAckWakeLock.setReferenceCounted(XM_RILJ_LOGV);
        this.mWakeLockTimeout = ((Integer) TelephonyProperties.wake_lock_timeout().orElse(Integer.valueOf(DEFAULT_WAKE_LOCK_TIMEOUT_MS))).intValue();
        this.mAckWakeLockTimeout = ((Integer) TelephonyProperties.wake_lock_timeout().orElse(200)).intValue();
        this.mWakeLockCount = 0;
        this.mRILDefaultWorkSource = new WorkSource(context.getApplicationInfo().uid, context.getPackageName());
        this.mActiveWakelockWorkSource = new WorkSource();
        getXmRadioProxy();
        riljLog("HAL version of xm radio : " + this.mXmRadioHalVersion);
    }

    private void addRequest(XmRILRequest rr) {
        acquireWakeLock(rr, 0);
        Trace.asyncTraceForTrackBegin(2097152L, "XmRIL", rr.mSerial + "> " + XmRILUtils.requestToString(rr.mRequest), rr.mSerial);
        synchronized (this.mRequestList) {
            rr.mStartTimeMs = SystemClock.elapsedRealtime();
            this.mRequestList.append(rr.mSerial, rr);
        }
    }

    protected XmRILRequest obtainRequest(int request, Message result, WorkSource workSource) {
        XmRILRequest rr = XmRILRequest.obtain(request, result, workSource);
        addRequest(rr);
        return rr;
    }

    private XmRILRequest obtainRequest(int request, Message result, WorkSource workSource, Object... args) {
        XmRILRequest rr = XmRILRequest.obtain(request, result, workSource, args);
        addRequest(rr);
        return rr;
    }

    protected int obtainRequestSerial(int request, Message result, WorkSource workSource) {
        XmRILRequest rr = XmRILRequest.obtain(request, result, workSource);
        addRequest(rr);
        return rr.mSerial;
    }

    protected void handleRadioProxyExceptionForRR(String caller, Exception e) {
        riljLoge(caller + ": " + e);
        e.printStackTrace();
        resetProxyAndRequestList();
    }

    private void radioServiceInvokeHelper(XmRILRequest rr, String methodName, FunctionalUtils.ThrowingRunnable helper) {
        try {
            helper.runOrThrow();
        } catch (RuntimeException e) {
            riljLoge(methodName + " RuntimeException: " + e);
            processResponseInternal(rr.mSerial, 39, 0);
            processResponseDoneInternal(rr, 39, 0, null);
        } catch (Exception e2) {
            handleRadioProxyExceptionForRR(methodName, e2);
        }
    }

    private boolean canMakeRequest(String request, XmRadioProxy proxy, Message result, HalVersion version) {
        if (proxy == null || proxy.isEmpty()) {
            riljLoge(String.format("Unable to complete %s because service radio is not available.", request));
            if (result != null) {
                AsyncResult.forMessage(result, (Object) null, CommandException.fromRilErrno(1));
                result.sendToTarget();
            }
            return XM_RILJ_LOGV;
        }
        if (!this.mXmRadioHalVersion.less(version)) {
            return XM_RILJ_LOGD;
        }
        riljLoge(String.format("%s not supported on service radio < %s.", request, version));
        if (result != null) {
            AsyncResult.forMessage(result, (Object) null, CommandException.fromRilErrno(6));
            result.sendToTarget();
        }
        return XM_RILJ_LOGV;
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void registerForUnsolCommMsg(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mUnsolCommMsgRegistrants.add(r);
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void unRegisterForUnsolCommMsg(Handler h) {
        this.mUnsolCommMsgRegistrants.remove(h);
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void sendCommMsg(final int msgId, final byte[] data, Message result) {
        final XmRadioProxy radioProxy = getXmRadioProxy();
        final XmRILRequest rr = obtainRequest(0, result, this.mRILDefaultWorkSource, Integer.valueOf(msgId));
        riljLog(rr.serialString() + "> " + XmRILUtils.requestToString(rr.mRequest));
        radioServiceInvokeHelper(rr, "sendCommMsg", new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.XmRIL$$ExternalSyntheticLambda4
            public final void runOrThrow() throws RemoteException {
                radioProxy.sendCommMsg(rr.mSerial, msgId, data);
            }
        });
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void setSarDsi(final int dsi, Message result) {
        final XmRadioProxy radioProxy = getXmRadioProxy();
        final XmRILRequest rr = obtainRequest(1, result, this.mRILDefaultWorkSource);
        riljLog(rr.serialString() + "> " + XmRILUtils.requestToString(rr.mRequest));
        radioServiceInvokeHelper(rr, "setSarDsi", new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.XmRIL$$ExternalSyntheticLambda7
            public final void runOrThrow() throws RemoteException {
                radioProxy.setSarDsi(rr.mSerial, dsi);
            }
        });
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void setSarDsiGroup(final int[] dsi, Message result) {
        final XmRadioProxy radioProxy = getXmRadioProxy();
        final XmRILRequest rr = obtainRequest(2, result, this.mRILDefaultWorkSource);
        riljLog(rr.serialString() + "> " + XmRILUtils.requestToString(rr.mRequest));
        radioServiceInvokeHelper(rr, "setSarDsiGroup", new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.XmRIL$$ExternalSyntheticLambda15
            public final void runOrThrow() throws RemoteException {
                radioProxy.setSarDsiGroup(rr.mSerial, dsi);
            }
        });
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void getSarDsi(Message result) {
        final XmRadioProxy radioProxy = getXmRadioProxy();
        final XmRILRequest rr = obtainRequest(3, result, this.mRILDefaultWorkSource);
        riljLog(rr.serialString() + "> " + XmRILUtils.requestToString(rr.mRequest));
        radioServiceInvokeHelper(rr, "getSarDsi", new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.XmRIL$$ExternalSyntheticLambda6
            public final void runOrThrow() throws RemoteException {
                radioProxy.getSarDsi(rr.mSerial);
            }
        });
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void getSarDsiGroup(Message result) {
        final XmRadioProxy radioProxy = getXmRadioProxy();
        final XmRILRequest rr = obtainRequest(4, result, this.mRILDefaultWorkSource);
        riljLog(rr.serialString() + "> " + XmRILUtils.requestToString(rr.mRequest));
        radioServiceInvokeHelper(rr, "getSarDsiGroup", new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.XmRIL$$ExternalSyntheticLambda14
            public final void runOrThrow() throws RemoteException {
                radioProxy.getSarDsiGroup(rr.mSerial);
            }
        });
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void setDeprioritizeSA(final boolean enable, Message result) {
        final XmRadioProxy radioProxy = getXmRadioProxy();
        final XmRILRequest rr = obtainRequest(5, result, this.mRILDefaultWorkSource);
        riljLog(rr.serialString() + "> " + XmRILUtils.requestToString(rr.mRequest));
        radioServiceInvokeHelper(rr, "setDeprioritizeSA", new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.XmRIL$$ExternalSyntheticLambda13
            public final void runOrThrow() throws RemoteException {
                radioProxy.setDeprioritizeSA(rr.mSerial, enable);
            }
        });
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void setNrMode(final int nrStatus, Message result) {
        final XmRadioProxy radioProxy = getXmRadioProxy();
        final XmRILRequest rr = obtainRequest(6, result, this.mRILDefaultWorkSource);
        riljLog(rr.serialString() + "> " + XmRILUtils.requestToString(rr.mRequest));
        radioServiceInvokeHelper(rr, "setNrMode", new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.XmRIL$$ExternalSyntheticLambda9
            public final void runOrThrow() throws RemoteException {
                radioProxy.setNrMode(rr.mSerial, nrStatus);
            }
        });
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void enableEndc(final boolean enable, Message result) {
        final XmRadioProxy radioProxy = getXmRadioProxy();
        final XmRILRequest rr = obtainRequest(7, result, this.mRILDefaultWorkSource);
        riljLog(rr.serialString() + "> " + XmRILUtils.requestToString(rr.mRequest));
        radioServiceInvokeHelper(rr, "enableEndc", new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.XmRIL$$ExternalSyntheticLambda12
            public final void runOrThrow() throws RemoteException {
                radioProxy.enableEndc(rr.mSerial, enable);
            }
        });
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void setDataStallMode(final int mode, final int operation, final int bearerType, final int scene, Message result) {
        final XmRadioProxy radioProxy = getXmRadioProxy();
        if (!canMakeRequest("setDataStallMode", radioProxy, result, XM_RADIO_HAL_VERSION_2)) {
            return;
        }
        final XmRILRequest rr = obtainRequest(8, result, this.mRILDefaultWorkSource);
        riljLog(rr.serialString() + "> " + XmRILUtils.requestToString(rr.mRequest));
        radioServiceInvokeHelper(rr, "setDataStallMode", new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.XmRIL$$ExternalSyntheticLambda16
            public final void runOrThrow() throws RemoteException {
                radioProxy.setDataStallMode(rr.mSerial, mode, operation, bearerType, scene);
            }
        });
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void setMobilityOptParams(final boolean enable, final int rat, final int featureId, Message result) {
        final XmRadioProxy radioProxy = getXmRadioProxy();
        if (!canMakeRequest("setMobilityOptParams", radioProxy, result, XM_RADIO_HAL_VERSION_2)) {
            return;
        }
        final XmRILRequest rr = obtainRequest(9, result, this.mRILDefaultWorkSource);
        riljLog(rr.serialString() + "> " + XmRILUtils.requestToString(rr.mRequest));
        radioServiceInvokeHelper(rr, "setMobilityOptParams", new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.XmRIL$$ExternalSyntheticLambda8
            public final void runOrThrow() throws RemoteException {
                radioProxy.setMobilityOptParams(rr.mSerial, enable, rat, featureId);
            }
        });
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void setPreferBandParams(final int rat, final int[] bands, final int[] arfcns, final int[] rsrpOffsets, final int preferType, Message result) {
        final XmRadioProxy radioProxy = getXmRadioProxy();
        if (!canMakeRequest("setPreferBandParams", radioProxy, result, XM_RADIO_HAL_VERSION_2)) {
            return;
        }
        final XmRILRequest rr = obtainRequest(10, result, this.mRILDefaultWorkSource);
        riljLog(rr.serialString() + "> " + XmRILUtils.requestToString(rr.mRequest));
        radioServiceInvokeHelper(rr, "setPreferBandParams", new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.XmRIL$$ExternalSyntheticLambda5
            public final void runOrThrow() throws RemoteException {
                radioProxy.setPreferBandParams(rr.mSerial, rat, bands, arfcns, rsrpOffsets, preferType);
            }
        });
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void promoteBSR(final int level, Message result) {
        final XmRadioProxy radioProxy = getXmRadioProxy();
        if (!canMakeRequest("promoteBSR", radioProxy, result, XM_RADIO_HAL_VERSION_2)) {
            return;
        }
        final XmRILRequest rr = obtainRequest(11, result, this.mRILDefaultWorkSource);
        riljLog(rr.serialString() + "> " + XmRILUtils.requestToString(rr.mRequest));
        radioServiceInvokeHelper(rr, "promoteBSR", new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.XmRIL$$ExternalSyntheticLambda11
            public final void runOrThrow() throws RemoteException {
                radioProxy.promoteBSR(rr.mSerial, level);
            }
        });
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void getDdsAssistantInfo(Message result) {
        final XmRadioProxy radioProxy = getXmRadioProxy();
        if (!canMakeRequest("getDdsAssistantInfo", radioProxy, result, XM_RADIO_HAL_VERSION_2)) {
            return;
        }
        final XmRILRequest rr = obtainRequest(12, result, this.mRILDefaultWorkSource);
        riljLog(rr.serialString() + "> " + XmRILUtils.requestToString(rr.mRequest));
        radioServiceInvokeHelper(rr, "getDdsAssistantInfo", new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.XmRIL$$ExternalSyntheticLambda18
            public final void runOrThrow() throws RemoteException {
                radioProxy.getDdsAssistantInfo(rr.mSerial);
            }
        });
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void getModemScene(Message result) {
        final XmRadioProxy radioProxy = getXmRadioProxy();
        if (!canMakeRequest("getModemScene", radioProxy, result, XM_RADIO_HAL_VERSION_2)) {
            return;
        }
        final XmRILRequest rr = obtainRequest(13, result, this.mRILDefaultWorkSource);
        riljLog(rr.serialString() + "> " + XmRILUtils.requestToString(rr.mRequest));
        radioServiceInvokeHelper(rr, "getModemScene", new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.XmRIL$$ExternalSyntheticLambda0
            public final void runOrThrow() throws RemoteException {
                radioProxy.getModemScene(rr.mSerial);
            }
        });
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void getModemReason(Message result) {
        final XmRadioProxy radioProxy = getXmRadioProxy();
        if (!canMakeRequest("getModemReason", radioProxy, result, XM_RADIO_HAL_VERSION_2)) {
            return;
        }
        final XmRILRequest rr = obtainRequest(14, result, this.mRILDefaultWorkSource);
        riljLog(rr.serialString() + "> " + XmRILUtils.requestToString(rr.mRequest));
        radioServiceInvokeHelper(rr, "getModemReason", new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.XmRIL$$ExternalSyntheticLambda17
            public final void runOrThrow() throws RemoteException {
                radioProxy.getModemReason(rr.mSerial);
            }
        });
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void setModemModelCfg(final int[] modelCfg, Message result) {
        final XmRadioProxy radioProxy = getXmRadioProxy();
        if (!canMakeRequest("setModemModelCfg", radioProxy, result, XM_RADIO_HAL_VERSION_2)) {
            return;
        }
        final XmRILRequest rr = obtainRequest(15, result, this.mRILDefaultWorkSource);
        riljLog(rr.serialString() + "> " + XmRILUtils.requestToString(rr.mRequest));
        radioServiceInvokeHelper(rr, "setModemModelCfg", new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.XmRIL$$ExternalSyntheticLambda3
            public final void runOrThrow() throws RemoteException {
                radioProxy.setModemModelCfg(rr.mSerial, modelCfg);
            }
        });
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void setModemCallbackSwitch(final boolean enable, Message result) {
        final XmRadioProxy radioProxy = getXmRadioProxy();
        if (!canMakeRequest("setModemCallbackSwitch", radioProxy, result, XM_RADIO_HAL_VERSION_2)) {
            return;
        }
        final XmRILRequest rr = obtainRequest(16, result, this.mRILDefaultWorkSource);
        riljLog(rr.serialString() + "> " + XmRILUtils.requestToString(rr.mRequest));
        radioServiceInvokeHelper(rr, "setModemCallbackSwitch", new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.XmRIL$$ExternalSyntheticLambda2
            public final void runOrThrow() throws RemoteException {
                radioProxy.setModemCallbackSwitch(rr.mSerial, enable);
            }
        });
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void setModemEventIndSwitch(final int event, final boolean isRegister, Message result) {
        final XmRadioProxy radioProxy = getXmRadioProxy();
        if (!canMakeRequest("setModemEventIndSwitch", radioProxy, result, XM_RADIO_HAL_VERSION_2)) {
            return;
        }
        final XmRILRequest rr = obtainRequest(17, result, this.mRILDefaultWorkSource);
        riljLog(rr.serialString() + "> " + XmRILUtils.requestToString(rr.mRequest));
        radioServiceInvokeHelper(rr, "setModemEventIndSwitch", new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.XmRIL$$ExternalSyntheticLambda1
            public final void runOrThrow() throws RemoteException {
                radioProxy.setModemEventIndSwitch(rr.mSerial, event, isRegister);
            }
        });
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void setFiveTupleBlackList(final String rmnetData, final List<String> ipList, final int mode, Message result) {
        final XmRadioProxy radioProxy = getXmRadioProxy();
        if (!canMakeRequest("setFiveTupleBlackList", radioProxy, result, XM_RADIO_HAL_VERSION_2)) {
            return;
        }
        final XmRILRequest rr = obtainRequest(18, result, this.mRILDefaultWorkSource);
        riljLog(rr.serialString() + "> " + XmRILUtils.requestToString(rr.mRequest));
        radioServiceInvokeHelper(rr, "setFiveTupleBlackList", new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.XmRIL$$ExternalSyntheticLambda10
            public final void runOrThrow() throws RemoteException {
                radioProxy.setFiveTupleBlackList(rr.mSerial, rmnetData, ipList, mode);
            }
        });
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void registerHstStateCallback(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mHstInfoRegistrants.add(r);
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void unRegisterHstStateCallback(Handler h) {
        this.mHstInfoRegistrants.remove(h);
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void registerQosStateCallback(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mQosInfoRegistrants.add(r);
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void unRegisterQosStateCallback(Handler h) {
        this.mQosInfoRegistrants.remove(h);
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void registerModemExceptionCallback(Handler h, int what, Object obj) {
        this.mModemExceptionRegistrants.addUnique(h, what, obj);
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void unregisterModemExceptionCallback(Handler h) {
        this.mModemExceptionRegistrants.remove(h);
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void registerModemSceneCallback(Handler h, int what, Object obj) {
        this.mModemSceneRegistrants.addUnique(h, what, obj);
    }

    @Override // com.android.internal.telephony.XmCommandsInterface
    public void unregisterModemSceneCallback(Handler h) {
        this.mModemSceneRegistrants.remove(h);
    }

    void processIndication(int indicationType) {
        if (indicationType == 1) {
            sendAck();
            riljLog("Unsol response received; Sending ack to ril.cpp");
        }
    }

    void processRequestAck(int serial) {
        XmRILRequest rr;
        synchronized (this.mRequestList) {
            rr = this.mRequestList.get(serial);
        }
        if (rr == null) {
            riljLogw("processRequestAck: Unexpected solicited ack response! serial: " + serial);
        } else {
            decrementWakeLock(rr);
            riljLog(rr.serialString() + " Ack < " + XmRILUtils.requestToString(rr.mRequest));
        }
    }

    public XmRILRequest processResponse(RadioResponseInfo responseInfo) {
        return processResponseInternal(responseInfo.serial, responseInfo.error, responseInfo.type);
    }

    public XmRILRequest processResponse(android.hardware.radio.RadioResponseInfo responseInfo) {
        return processResponseInternal(responseInfo.serial, responseInfo.error, responseInfo.type);
    }

    private XmRILRequest processResponseInternal(int serial, int error, int type) {
        XmRILRequest rr;
        if (type == 1) {
            synchronized (this.mRequestList) {
                rr = this.mRequestList.get(serial);
            }
            if (rr == null) {
                riljLogw("Unexpected solicited ack response! sn: " + serial);
            } else {
                decrementWakeLock(rr);
                riljLog(rr.serialString() + " Ack from radio < " + XmRILUtils.requestToString(rr.mRequest));
            }
            return rr;
        }
        XmRILRequest rr2 = findAndRemoveRequestFromList(serial);
        if (rr2 == null) {
            riljLoge("processResponse: Unexpected response! serial: " + serial + ", error: " + error);
            return null;
        }
        Trace.asyncTraceForTrackEnd(2097152L, "XmRIL", rr2.mSerial);
        if (type == 2) {
            sendAck();
            riljLog("Response received from radio for " + rr2.serialString() + " " + XmRILUtils.requestToString(rr2.mRequest) + " Sending ack to ril.cpp");
        }
        return rr2;
    }

    protected Message getMessageFromRequest(Object request) {
        XmRILRequest rr = (XmRILRequest) request;
        if (rr == null) {
            return null;
        }
        Message result = rr.mResult;
        return result;
    }

    public void processResponseDone(XmRILRequest rr, RadioResponseInfo responseInfo, Object ret) {
        processResponseDoneInternal(rr, responseInfo.error, responseInfo.type, ret);
    }

    private void processResponseDoneInternal(XmRILRequest rr, int rilError, int responseType, Object ret) {
        if (rilError == 0) {
            if (isLogOrTrace()) {
                String logStr = rr.serialString() + "< " + XmRILUtils.requestToString(rr.mRequest) + " " + retToString(rr.mRequest, ret);
                riljLog(logStr);
                Trace.instantForTrack(2097152L, "XmRIL", logStr);
            }
        } else {
            if (isLogOrTrace()) {
                String logStr2 = rr.serialString() + "< " + XmRILUtils.requestToString(rr.mRequest) + " error " + rilError;
                riljLog(logStr2);
                Trace.instantForTrack(2097152L, "XmRIL", logStr2);
            }
            rr.onError(rilError, ret);
        }
        processResponseCleanUp(rr, rilError, responseType, ret);
    }

    public void processResponseFallback(XmRILRequest rr, RadioResponseInfo responseInfo, Object ret) {
        if (responseInfo.error == 6) {
            riljLog(rr.serialString() + "< " + XmRILUtils.requestToString(rr.mRequest) + " request not supported, falling back");
        }
        processResponseCleanUp(rr, responseInfo.error, responseInfo.type, ret);
    }

    private void processResponseCleanUp(XmRILRequest rr, int rilError, int responseType, Object ret) {
        if (rr != null) {
            if (responseType == 0) {
                decrementWakeLock(rr);
            }
            rr.release();
        }
    }

    protected void processResponseDone(Object request, RadioResponseInfo responseInfo, Object ret) {
        XmRILRequest rr = (XmRILRequest) request;
        processResponseDone(rr, responseInfo, ret);
    }

    private void sendAck() {
    }

    private WorkSource getDefaultWorkSourceIfInvalid(WorkSource workSource) {
        if (workSource == null) {
            return this.mRILDefaultWorkSource;
        }
        return workSource;
    }

    private void acquireWakeLock(XmRILRequest rr, int wakeLockType) {
        synchronized (rr) {
            if (rr.mWakeLockType != -1) {
                riljLog("Failed to acquire wakelock for " + rr.serialString());
                return;
            }
            switch (wakeLockType) {
                case 0:
                    synchronized (this.mWakeLock) {
                        this.mWakeLock.acquire();
                        this.mWakeLockCount++;
                        this.mWlSequenceNum++;
                        String clientId = rr.getWorkSourceClientId();
                        if (!this.mClientWakelockTracker.isClientActive(clientId)) {
                            this.mActiveWakelockWorkSource.add(rr.mWorkSource);
                            this.mWakeLock.setWorkSource(this.mActiveWakelockWorkSource);
                        }
                        this.mClientWakelockTracker.startTracking(rr.mClientId, rr.mRequest, rr.mSerial, this.mWakeLockCount);
                        Message msg = this.mRilHandler.obtainMessage(2);
                        msg.arg1 = this.mWlSequenceNum;
                        this.mRilHandler.sendMessageDelayed(msg, this.mWakeLockTimeout);
                        break;
                    }
                    rr.mWakeLockType = wakeLockType;
                    return;
                case 1:
                    synchronized (this.mAckWakeLock) {
                        this.mAckWakeLock.acquire();
                        this.mAckWlSequenceNum++;
                        Message msg2 = this.mRilHandler.obtainMessage(4);
                        msg2.arg1 = this.mAckWlSequenceNum;
                        this.mRilHandler.sendMessageDelayed(msg2, this.mAckWakeLockTimeout);
                        break;
                    }
                    rr.mWakeLockType = wakeLockType;
                    return;
                default:
                    riljLogw("Acquiring Invalid Wakelock type " + wakeLockType);
                    return;
            }
        }
    }

    private void decrementWakeLock(XmRILRequest rr) {
        synchronized (rr) {
            switch (rr.mWakeLockType) {
                case -1:
                    rr.mWakeLockType = -1;
                    break;
                case 0:
                    synchronized (this.mWakeLock) {
                        this.mClientWakelockTracker.stopTracking(rr.mClientId, rr.mRequest, rr.mSerial, this.mWakeLockCount > 1 ? this.mWakeLockCount - 1 : 0);
                        String clientId = rr.getWorkSourceClientId();
                        if (!this.mClientWakelockTracker.isClientActive(clientId)) {
                            this.mActiveWakelockWorkSource.remove(rr.mWorkSource);
                            this.mWakeLock.setWorkSource(this.mActiveWakelockWorkSource);
                        }
                        if (this.mWakeLockCount > 1) {
                            this.mWakeLockCount--;
                        } else {
                            this.mWakeLockCount = 0;
                            this.mWakeLock.release();
                        }
                        break;
                    }
                    rr.mWakeLockType = -1;
                    break;
                case 1:
                    rr.mWakeLockType = -1;
                    break;
                default:
                    riljLogw("Decrementing Invalid Wakelock type " + rr.mWakeLockType);
                    rr.mWakeLockType = -1;
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean clearWakeLock(int wakeLockType) {
        if (wakeLockType == 0) {
            synchronized (this.mWakeLock) {
                if (this.mWakeLockCount == 0 && !this.mWakeLock.isHeld()) {
                    return XM_RILJ_LOGV;
                }
                riljLog("NOTE: mWakeLockCount is " + this.mWakeLockCount + " at time of clearing");
                this.mWakeLockCount = 0;
                this.mWakeLock.release();
                this.mClientWakelockTracker.stopTrackingAll();
                this.mActiveWakelockWorkSource = new WorkSource();
                return XM_RILJ_LOGD;
            }
        }
        synchronized (this.mAckWakeLock) {
            if (!this.mAckWakeLock.isHeld()) {
                return XM_RILJ_LOGV;
            }
            this.mAckWakeLock.release();
            return XM_RILJ_LOGD;
        }
    }

    private void clearRequestList(int error, boolean loggable) {
        synchronized (this.mRequestList) {
            int count = this.mRequestList.size();
            if (loggable) {
                riljLog("clearRequestList  mWakeLockCount=" + this.mWakeLockCount + " mRequestList=" + count);
            }
            int i = 0;
            while (true) {
                SparseArray<XmRILRequest> sparseArray = this.mRequestList;
                if (i < count) {
                    XmRILRequest rr = sparseArray.valueAt(i);
                    if (loggable) {
                        riljLog(i + ": [" + rr.mSerial + "] " + XmRILUtils.requestToString(rr.mRequest));
                    }
                    rr.onError(error, null);
                    decrementWakeLock(rr);
                    rr.release();
                    i++;
                } else {
                    sparseArray.clear();
                }
            }
        }
    }

    private XmRILRequest findAndRemoveRequestFromList(int serial) {
        XmRILRequest rr;
        synchronized (this.mRequestList) {
            rr = this.mRequestList.get(serial);
            if (rr != null) {
                this.mRequestList.remove(serial);
            }
        }
        return rr;
    }

    protected static String retToString(int req, Object ret) {
        if (ret == null) {
            return "";
        }
        if (ret instanceof int[]) {
            int[] intArray = (int[]) ret;
            int length = intArray.length;
            StringBuilder sb = new StringBuilder("{");
            if (length > 0) {
                int i = intArray[0];
                sb.append(i);
                for (int i2 = 0 + 1; i2 < length; i2++) {
                    sb.append(", ").append(intArray[i2]);
                }
            }
            sb.append("}");
            String s = sb.toString();
            return s;
        }
        if (ret instanceof String[]) {
            String[] strings = (String[]) ret;
            int length2 = strings.length;
            StringBuilder sb2 = new StringBuilder("{");
            if (length2 > 0) {
                sb2.append(strings[0]);
                for (int i3 = 0 + 1; i3 < length2; i3++) {
                    sb2.append(", ").append(strings[i3]);
                }
            }
            sb2.append("}");
            String s2 = sb2.toString();
            return s2;
        }
        boolean toStringExists = XM_RILJ_LOGV;
        try {
            Class<?> cls = ret.getClass();
            boolean z = XM_RILJ_LOGV;
            if (cls.getMethod("toString", new Class[0]).getDeclaringClass() != Object.class) {
                z = XM_RILJ_LOGD;
            }
            toStringExists = z;
        } catch (NoSuchMethodException e) {
            Rlog.e(XM_RILJ_LOG_TAG, e.getMessage());
        }
        if (toStringExists) {
            String s3 = ret.toString();
            return s3;
        }
        String s4 = XmRILUtils.convertToString(ret) + " [convertToString]";
        return s4;
    }

    public void riljLog(String msg) {
        Rlog.d(XM_RILJ_LOG_TAG, msg + " [PHONE" + this.mPhoneId + "]");
    }

    public void riljLoge(String msg) {
        Rlog.e(XM_RILJ_LOG_TAG, msg + " [PHONE" + this.mPhoneId + "]");
    }

    public void riljLogv(String msg) {
        Rlog.v(XM_RILJ_LOG_TAG, msg + " [PHONE" + this.mPhoneId + "]");
    }

    void riljLogw(String msg) {
        Rlog.w(XM_RILJ_LOG_TAG, msg + " [PHONE" + this.mPhoneId + "]");
    }

    boolean isLogOrTrace() {
        return XM_RILJ_LOGD;
    }

    boolean isLogvOrTrace() {
        return Trace.isTagEnabled(2097152L);
    }

    public void unsljLog(int response) {
        String logStr = XmRILUtils.responseToString(response);
        riljLog("[UNSL]< " + logStr);
        Trace.instantForTrack(2097152L, "XmRIL", logStr);
    }

    public void unsljLogMore(int response, String more) {
        String logStr = XmRILUtils.responseToString(response) + " " + more;
        riljLog("[UNSL]< " + logStr);
        Trace.instantForTrack(2097152L, "XmRIL", logStr);
    }

    public void unsljLogRet(int response, Object ret) {
        String logStr = XmRILUtils.responseToString(response) + " " + retToString(response, ret);
        riljLog("[UNSL]< " + logStr);
        Trace.instantForTrack(2097152L, "XmRIL", logStr);
    }

    public void unsljLogvRet(int response, Object ret) {
        String logStr = XmRILUtils.responseToString(response) + " " + retToString(response, ret);
        Trace.instantForTrack(2097152L, "XmRIL", logStr);
    }

    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.println("XmRIL: " + this);
        pw.println("service radio mWakeLock=" + this.mWakeLock);
        pw.println(" mWakeLockTimeout=" + this.mWakeLockTimeout);
        synchronized (this.mRequestList) {
            synchronized (this.mWakeLock) {
                pw.println(" mWakeLockCount=" + this.mWakeLockCount);
            }
            int count = this.mRequestList.size();
            pw.println(" mRequestList count=" + count);
            for (int i = 0; i < count; i++) {
                XmRILRequest rr = this.mRequestList.valueAt(i);
                pw.println("  [" + rr.mSerial + "] " + XmRILUtils.requestToString(rr.mRequest));
            }
        }
        this.mClientWakelockTracker.dumpClientRequestTracker(pw);
    }

    public List<ClientRequestStats> getClientRequestStats() {
        return this.mClientWakelockTracker.getClientRequestStats();
    }

    public HalVersion getHalVersion() {
        return this.mXmRadioHalVersion;
    }

    public static HalVersion getServiceHalVersion(int interfaceVersion) {
        switch (interfaceVersion) {
            case 1:
                return XM_RADIO_HAL_VERSION_1;
            case 2:
                return XM_RADIO_HAL_VERSION_2;
            default:
                return XM_RADIO_HAL_VERSION_UNKNOWN;
        }
    }
}
