package com.android.internal.telephony.domainselection;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.DomainSelectionService;
import android.telephony.EmergencyRegistrationResult;
import android.telephony.data.ApnSetting;
import android.telephony.ims.ImsReasonInfo;
import android.util.Log;
import com.android.internal.infra.AndroidFuture;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.IDomainSelector;
import com.android.internal.telephony.ITransportSelectorCallback;
import com.android.internal.telephony.ITransportSelectorResultCallback;
import com.android.internal.telephony.IWwanSelectorCallback;
import com.android.internal.telephony.IWwanSelectorResultCallback;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SomeArgs$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.data.AccessNetworksManager;
import com.android.internal.telephony.util.TelephonyUtils;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/* JADX INFO: loaded from: classes.dex */
public class DomainSelectionConnection {
    private static final boolean DBG = TelephonyUtils.IS_DEBUGGABLE;
    protected static final int EVENT_EMERGENCY_NETWORK_SCAN_RESULT = 1;
    protected static final int EVENT_LAST = 7;
    protected static final int EVENT_MODEM_RESET = 7;
    protected static final int EVENT_QUALIFIED_NETWORKS_CHANGED = 2;
    protected static final int EVENT_RESET_NETWORK_SCAN_DONE = 5;
    protected static final int EVENT_SERVICE_BINDING_TIMEOUT = 4;
    protected static final int EVENT_SERVICE_CONNECTED = 3;
    protected static final int EVENT_TRIGGER_NETWORK_SCAN_DONE = 6;
    private final DomainSelectionController mController;
    private IDomainSelector mDomainSelector;
    protected DomainSelectionConnectionHandler mHandler;
    private final boolean mIsEmergency;
    private ScanRequest mPendingScanRequest;
    protected Phone mPhone;
    private boolean mRegisteredRegistrant;
    private IWwanSelectorResultCallback mResultCallback;
    private DomainSelectionService.SelectionAttributes mSelectionAttributes;
    private int mSelectorType;
    private int mStatus;
    private IWwanSelectorCallback mWwanSelectorCallback;
    protected String mTag = "DomainSelectionConnection";
    private final Object mLock = new Object();
    private final LocalLog mLocalLog = new LocalLog(30);
    private boolean mIsTestMode = false;
    private int mDisconnectCause = -1;
    private int mPreciseDisconnectCause = -1;
    private String mReasonMessage = null;
    private final Looper mLooper = Looper.getMainLooper();
    private final ITransportSelectorCallback mTransportSelectorCallback = new TransportSelectorCallbackAdaptor();
    private AndroidFuture mOnComplete = new AndroidFuture();

    public interface DomainSelectionConnectionCallback {
        void onSelectionTerminated(int i);
    }

    private static int getTransportFromAccessNetwork(int i) {
        return i == 5 ? 2 : 1;
    }

    protected int getTerminationCauseForSelectionTimeout() {
        return 13;
    }

    public void onCreated() {
    }

    public void onSelectionTerminated(int i) {
    }

    public void onWlanSelected() {
    }

    public void onWwanSelected() {
    }

    private static class ScanRequest {
        final int[] mPreferredNetworks;
        final int mScanType;

        ScanRequest(int[] iArr, int i) {
            this.mPreferredNetworks = iArr;
            this.mScanType = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class TransportSelectorCallbackAdaptor extends ITransportSelectorCallback.Stub {
        private TransportSelectorCallbackAdaptor() {
        }

        public void onCreated(IDomainSelector iDomainSelector) {
            synchronized (DomainSelectionConnection.this.mLock) {
                DomainSelectionConnection.this.mDomainSelector = iDomainSelector;
                if (DomainSelectionConnection.this.checkState(1)) {
                    try {
                        iDomainSelector.finishSelection();
                    } catch (RemoteException unused) {
                    }
                } else {
                    DomainSelectionConnection.this.onCreated();
                }
            }
        }

        public void onWlanSelected(boolean z) {
            synchronized (DomainSelectionConnection.this.mLock) {
                try {
                    if (DomainSelectionConnection.this.checkState(1)) {
                        return;
                    }
                    DomainSelectionConnection.this.setState(2);
                    DomainSelectionConnection.this.onWlanSelected(z);
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        public void onWwanSelectedAsync(final ITransportSelectorResultCallback iTransportSelectorResultCallback) {
            synchronized (DomainSelectionConnection.this.mLock) {
                try {
                    if (DomainSelectionConnection.this.checkState(1)) {
                        return;
                    }
                    if (DomainSelectionConnection.this.mWwanSelectorCallback == null) {
                        DomainSelectionConnection.this.mWwanSelectorCallback = new WwanSelectorCallbackAdaptor();
                    }
                    if (DomainSelectionConnection.this.mIsTestMode || !DomainSelectionConnection.this.mIsEmergency || DomainSelectionConnection.this.mSelectorType != 1) {
                        DomainSelectionConnection.this.initHandler();
                        DomainSelectionConnection.this.mHandler.post(new Runnable() { // from class: com.android.internal.telephony.domainselection.DomainSelectionConnection$TransportSelectorCallbackAdaptor$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$onWwanSelectedAsync$0(iTransportSelectorResultCallback);
                            }
                        });
                    } else {
                        new Thread(new Runnable() { // from class: com.android.internal.telephony.domainselection.DomainSelectionConnection.TransportSelectorCallbackAdaptor.1
                            @Override // java.lang.Runnable
                            public void run() {
                                TransportSelectorCallbackAdaptor.this.lambda$onWwanSelectedAsync$0(iTransportSelectorResultCallback);
                            }
                        }).start();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: onWwanSelectedAsyncInternal, reason: merged with bridge method [inline-methods] */
        public void lambda$onWwanSelectedAsync$0(ITransportSelectorResultCallback iTransportSelectorResultCallback) {
            synchronized (DomainSelectionConnection.this.mLock) {
                try {
                    if (DomainSelectionConnection.this.checkState(1)) {
                        return;
                    }
                    DomainSelectionConnection.this.onWwanSelected();
                    try {
                        iTransportSelectorResultCallback.onCompleted(DomainSelectionConnection.this.mWwanSelectorCallback);
                    } catch (RemoteException e) {
                        DomainSelectionConnection.this.loge("onWwanSelectedAsync executor exception=" + e);
                        synchronized (DomainSelectionConnection.this.mLock) {
                            DomainSelectionConnection.this.waitForServiceBinding(null);
                        }
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        public void onSelectionTerminated(int i) {
            synchronized (DomainSelectionConnection.this.mLock) {
                try {
                    if (DomainSelectionConnection.this.checkState(1)) {
                        return;
                    }
                    DomainSelectionConnection.this.onSelectionTerminated(i);
                    if (!DomainSelectionConnection.this.mIsEmergency || !DomainSelectionConnection.this.checkState(2)) {
                        DomainSelectionConnection.this.dispose();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class WwanSelectorCallbackAdaptor extends IWwanSelectorCallback.Stub {
        private WwanSelectorCallbackAdaptor() {
        }

        public void onRequestEmergencyNetworkScan(final int[] iArr, final int i, final boolean z, IWwanSelectorResultCallback iWwanSelectorResultCallback) {
            synchronized (DomainSelectionConnection.this.mLock) {
                try {
                    if (DomainSelectionConnection.this.checkState(1)) {
                        return;
                    }
                    DomainSelectionConnection.this.mResultCallback = iWwanSelectorResultCallback;
                    DomainSelectionConnection.this.initHandler();
                    DomainSelectionConnection.this.mHandler.post(new Runnable() { // from class: com.android.internal.telephony.domainselection.DomainSelectionConnection$WwanSelectorCallbackAdaptor$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$onRequestEmergencyNetworkScan$0(iArr, i, z);
                        }
                    });
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onRequestEmergencyNetworkScan$0(int[] iArr, int i, boolean z) {
            synchronized (DomainSelectionConnection.this.mLock) {
                DomainSelectionConnection.this.onRequestEmergencyNetworkScan(iArr, i, z);
            }
        }

        public void onDomainSelected(int i, boolean z) {
            synchronized (DomainSelectionConnection.this.mLock) {
                try {
                    if (DomainSelectionConnection.this.checkState(1)) {
                        return;
                    }
                    DomainSelectionConnection.this.setState(2);
                    DomainSelectionConnection.this.onDomainSelected(i, z);
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        public void onCancel() {
            DomainSelectionConnectionHandler domainSelectionConnectionHandler;
            synchronized (DomainSelectionConnection.this.mLock) {
                try {
                    if (!DomainSelectionConnection.this.checkState(1) && (domainSelectionConnectionHandler = DomainSelectionConnection.this.mHandler) != null) {
                        domainSelectionConnectionHandler.post(new Runnable() { // from class: com.android.internal.telephony.domainselection.DomainSelectionConnection$WwanSelectorCallbackAdaptor$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$onCancel$1();
                            }
                        });
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCancel$1() {
            DomainSelectionConnection.this.onCancel();
        }
    }

    protected final class DomainSelectionConnectionHandler extends Handler {
        DomainSelectionConnectionHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Object obj;
            switch (message.what) {
                case 1:
                    EmergencyRegistrationResult emergencyRegistrationResult = (EmergencyRegistrationResult) ((AsyncResult) message.obj).result;
                    if (DomainSelectionConnection.DBG) {
                        DomainSelectionConnection.this.logd("EVENT_EMERGENCY_NETWORK_SCAN_RESULT result=" + emergencyRegistrationResult);
                    }
                    synchronized (DomainSelectionConnection.this.mLock) {
                        DomainSelectionConnection.this.clearState(8);
                        if (DomainSelectionConnection.this.mResultCallback != null) {
                            try {
                                DomainSelectionConnection.this.mResultCallback.onComplete(emergencyRegistrationResult);
                            } catch (RemoteException e) {
                                DomainSelectionConnection.this.loge("EVENT_EMERGENCY_NETWORK_SCAN_RESULT exception=" + e);
                                DomainSelectionConnection.this.waitForServiceBinding(null);
                            }
                        }
                        break;
                    }
                    return;
                case 2:
                    AsyncResult asyncResult = (AsyncResult) message.obj;
                    if (asyncResult == null || (obj = asyncResult.result) == null) {
                        DomainSelectionConnection.this.loge("handleMessage EVENT_QUALIFIED_NETWORKS_CHANGED null result");
                        return;
                    } else {
                        DomainSelectionConnection.this.onQualifiedNetworksChanged((List) obj);
                        return;
                    }
                case 3:
                    synchronized (DomainSelectionConnection.this.mLock) {
                        try {
                            if (!DomainSelectionConnection.this.checkState(1) && DomainSelectionConnection.this.checkState(4)) {
                                if (DomainSelectionConnection.this.mController.selectDomain(DomainSelectionConnection.this.mSelectionAttributes, DomainSelectionConnection.this.mTransportSelectorCallback)) {
                                    DomainSelectionConnection.this.clearWaitingForServiceBinding();
                                }
                                return;
                            }
                            DomainSelectionConnection.this.loge("EVENT_SERVICE_CONNECTED disposed or not waiting for binding");
                            return;
                        } catch (Throwable th) {
                            throw th;
                        }
                    }
                case 4:
                    synchronized (DomainSelectionConnection.this.mLock) {
                        try {
                            if (!DomainSelectionConnection.this.checkState(1) && DomainSelectionConnection.this.checkState(4)) {
                                DomainSelectionConnection.this.onServiceBindingTimeout();
                            }
                        } catch (Throwable th2) {
                            throw th2;
                        }
                        break;
                    }
                    return;
                case 5:
                    synchronized (DomainSelectionConnection.this.mLock) {
                        try {
                            DomainSelectionConnection.this.clearState(16);
                            if (!DomainSelectionConnection.this.checkState(1) && DomainSelectionConnection.this.mPendingScanRequest != null) {
                                DomainSelectionConnection domainSelectionConnection = DomainSelectionConnection.this;
                                domainSelectionConnection.onRequestEmergencyNetworkScan(domainSelectionConnection.mPendingScanRequest.mPreferredNetworks, DomainSelectionConnection.this.mPendingScanRequest.mScanType, false);
                                return;
                            }
                            return;
                        } catch (Throwable th3) {
                            throw th3;
                        }
                    }
                case 6:
                    synchronized (DomainSelectionConnection.this.mLock) {
                        try {
                            if (!DomainSelectionConnection.this.checkState(1) && DomainSelectionConnection.this.checkState(8)) {
                                AsyncResult asyncResult2 = (AsyncResult) message.obj;
                                if (asyncResult2 != null && asyncResult2.exception != null) {
                                    DomainSelectionConnection.this.onTriggerNetworkScanError(((Integer) asyncResult2.userObj).intValue(), ((CommandException) asyncResult2.exception).getCommandError());
                                }
                                return;
                            }
                            return;
                        } catch (Throwable th4) {
                            throw th4;
                        }
                    }
                case 7:
                    synchronized (DomainSelectionConnection.this.mLock) {
                        DomainSelectionConnection.this.onModemReset();
                        break;
                    }
                    return;
                default:
                    DomainSelectionConnection.this.loge("handleMessage unexpected msg=" + message.what);
                    return;
            }
        }
    }

    public DomainSelectionConnection(Phone phone, int i, boolean z, DomainSelectionController domainSelectionController) {
        this.mController = domainSelectionController;
        this.mPhone = phone;
        this.mSelectorType = i;
        this.mIsEmergency = z;
    }

    public DomainSelectionService.SelectionAttributes getSelectionAttributes() {
        return this.mSelectionAttributes;
    }

    public ITransportSelectorCallback getTransportSelectorCallback() {
        return this.mTransportSelectorCallback;
    }

    public IWwanSelectorResultCallback getWwanSelectorResultCallback() {
        return this.mResultCallback;
    }

    public CompletableFuture<Integer> getCompletableFuture() {
        return this.mOnComplete;
    }

    public Phone getPhone() {
        return this.mPhone;
    }

    public void selectDomain(DomainSelectionService.SelectionAttributes selectionAttributes) {
        synchronized (this.mLock) {
            try {
                this.mSelectionAttributes = selectionAttributes;
                if (this.mController.selectDomain(selectionAttributes, this.mTransportSelectorCallback)) {
                    clearWaitingForServiceBinding();
                } else {
                    waitForServiceBinding(selectionAttributes);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void onWlanSelected(boolean z) {
        onWlanSelected();
    }

    public void onRequestEmergencyNetworkScan(int[] iArr, int i, boolean z) {
        synchronized (this.mLock) {
            try {
                if (this.mHandler != null && !checkState(1) && !checkState(8)) {
                    if (checkState(16)) {
                        if (this.mPendingScanRequest != null) {
                            logi("onRequestEmergencyNetworkScan consecutive scan requests");
                            return;
                        } else {
                            logi("onRequestEmergencyNetworkScan reset not completed");
                            this.mPendingScanRequest = new ScanRequest(iArr, i);
                            return;
                        }
                    }
                    if (z) {
                        setState(16);
                        this.mPendingScanRequest = new ScanRequest(iArr, i);
                        this.mPhone.cancelEmergencyNetworkScan(z, this.mHandler.obtainMessage(5));
                        return;
                    }
                    if (!this.mRegisteredRegistrant) {
                        this.mPhone.registerForEmergencyNetworkScan(this.mHandler, 1, null);
                        this.mPhone.mCi.registerForModemReset(this.mHandler, 7, null);
                        this.mRegisteredRegistrant = true;
                    }
                    setState(8);
                    this.mPhone.triggerEmergencyNetworkScan(iArr, i, this.mHandler.obtainMessage(6, Integer.valueOf(i)));
                    this.mPendingScanRequest = null;
                    return;
                }
                logi("onRequestEmergencyNetworkScan waitResult=" + checkState(8));
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void onDomainSelected(int i) {
        getCompletableFuture().complete(Integer.valueOf(i));
    }

    public void onDomainSelected(int i, boolean z) {
        onDomainSelected(i);
    }

    public void onCancel() {
        onCancel(false);
    }

    private void onCancel(boolean z) {
        this.mPendingScanRequest = null;
        if (checkState(8)) {
            clearState(8);
            this.mPhone.cancelEmergencyNetworkScan(z, null);
        }
    }

    public void cancelSelection() {
        finishSelection();
    }

    public CompletableFuture<Integer> reselectDomain(DomainSelectionService.SelectionAttributes selectionAttributes) {
        AndroidFuture androidFuture;
        synchronized (this.mLock) {
            this.mSelectionAttributes = selectionAttributes;
            this.mOnComplete = new AndroidFuture();
            clearState(2);
            try {
                try {
                    IDomainSelector iDomainSelector = this.mDomainSelector;
                    if (iDomainSelector == null) {
                        DomainSelectionService.SelectionAttributes selectionAttributesToRebindService = getSelectionAttributesToRebindService();
                        this.mSelectionAttributes = selectionAttributesToRebindService;
                        if (this.mController.selectDomain(selectionAttributesToRebindService, this.mTransportSelectorCallback)) {
                            clearWaitingForServiceBinding();
                        } else {
                            waitForServiceBinding(null);
                        }
                    } else {
                        iDomainSelector.reselectDomain(selectionAttributes);
                    }
                    androidFuture = this.mOnComplete;
                } catch (RemoteException e) {
                    loge("reselectDomain exception=" + e);
                    waitForServiceBinding(null);
                    return this.mOnComplete;
                }
            } catch (Throwable unused) {
                return this.mOnComplete;
            }
        }
        return androidFuture;
    }

    public void finishSelection() {
        synchronized (this.mLock) {
            try {
                try {
                    IDomainSelector iDomainSelector = this.mDomainSelector;
                    if (iDomainSelector != null) {
                        iDomainSelector.finishSelection();
                    }
                } catch (RemoteException e) {
                    loge("finishSelection exception=" + e);
                }
                dispose();
            } catch (Throwable th) {
                dispose();
                throw th;
            }
        }
    }

    public void onServiceConnected() {
        synchronized (this.mLock) {
            try {
                if (!checkState(1) && checkState(4)) {
                    initHandler();
                    this.mHandler.sendEmptyMessage(3);
                    return;
                }
                logi("onServiceConnected disposed or not waiting for the binding");
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void onServiceDisconnected() {
        synchronized (this.mLock) {
            try {
                DomainSelectionConnectionHandler domainSelectionConnectionHandler = this.mHandler;
                if (domainSelectionConnectionHandler != null) {
                    domainSelectionConnectionHandler.removeMessages(3);
                }
                if (!checkState(1) && !checkState(2)) {
                    waitForServiceBinding(null);
                    return;
                }
                this.mDomainSelector = null;
                this.mResultCallback = null;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void waitForServiceBinding(DomainSelectionService.SelectionAttributes selectionAttributes) {
        if (checkState(1) || checkState(4)) {
            return;
        }
        setState(4);
        this.mDomainSelector = null;
        this.mResultCallback = null;
        if (selectionAttributes == null) {
            selectionAttributes = getSelectionAttributesToRebindService();
        }
        this.mSelectionAttributes = selectionAttributes;
        initHandler();
        this.mHandler.sendEmptyMessageDelayed(4, 4000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearWaitingForServiceBinding() {
        if (checkState(4)) {
            clearState(4);
            DomainSelectionConnectionHandler domainSelectionConnectionHandler = this.mHandler;
            if (domainSelectionConnectionHandler != null) {
                domainSelectionConnectionHandler.removeMessages(4);
            }
        }
    }

    protected void onServiceBindingTimeout() {
        synchronized (this.mLock) {
            try {
                if (checkState(1)) {
                    logi("onServiceBindingTimeout disposed");
                } else {
                    onSelectionTerminated(getTerminationCauseForSelectionTimeout());
                    dispose();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    protected DomainSelectionService.SelectionAttributes getSelectionAttributesToRebindService() {
        return this.mSelectionAttributes;
    }

    public boolean isWaitingForServiceBinding() {
        return checkState(4) && !checkState(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispose() {
        setState(1);
        if (this.mRegisteredRegistrant) {
            this.mPhone.unregisterForEmergencyNetworkScan(this.mHandler);
            this.mPhone.mCi.unregisterForModemReset(this.mHandler);
            this.mRegisteredRegistrant = false;
        }
        onCancel(true);
        this.mController.removeConnection(this);
        DomainSelectionConnectionHandler domainSelectionConnectionHandler = this.mHandler;
        if (domainSelectionConnectionHandler != null) {
            domainSelectionConnectionHandler.removeCallbacksAndMessages(null);
        }
        this.mHandler = null;
    }

    protected void initHandler() {
        if (this.mHandler == null) {
            this.mHandler = new DomainSelectionConnectionHandler(this.mLooper);
        }
    }

    protected void onQualifiedNetworksChanged(List<AccessNetworksManager.QualifiedNetworks> list) {
        if (this.mIsEmergency && this.mSelectorType == 1) {
            SomeArgs$$ExternalSyntheticBUOutline0.m("DomainSelectionConnection for emergency calls should override onQualifiedNetworksChanged()");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTriggerNetworkScanError(int i, CommandException.Error error) {
        loge("onTriggerNetworkScanError scanType=" + i + ", error=" + error);
        if (shouldTerminateCallOnRadioNotAvailable() && error == CommandException.Error.RADIO_NOT_AVAILABLE) {
            clearState(8);
            onSelectionTerminated(17);
            dispose();
        } else if (i == 2) {
            EmergencyRegistrationResult emergencyRegistrationResult = new EmergencyRegistrationResult(0, 4, 0, false, false, 0, 0, "", "", "");
            DomainSelectionConnectionHandler domainSelectionConnectionHandler = this.mHandler;
            if (domainSelectionConnectionHandler != null) {
                domainSelectionConnectionHandler.obtainMessage(1, new AsyncResult((Object) null, emergencyRegistrationResult, (Throwable) null)).sendToTarget();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onModemReset() {
        loge("onModemReset status=" + this.mStatus);
        if (!shouldTerminateCallOnRadioNotAvailable() || checkState(1) || checkState(2)) {
            return;
        }
        onSelectionTerminated(17);
        dispose();
    }

    private boolean shouldTerminateCallOnRadioNotAvailable() {
        return this.mIsEmergency && this.mSelectorType == 1;
    }

    public int getPreferredTransport(int i, List<AccessNetworksManager.QualifiedNetworks> list) {
        for (AccessNetworksManager.QualifiedNetworks qualifiedNetworks : list) {
            int[] iArr = qualifiedNetworks.qualifiedNetworks;
            if (iArr.length > 0 && qualifiedNetworks.apnType == i) {
                return getTransportFromAccessNetwork(iArr[0]);
            }
        }
        loge("getPreferredTransport no network found for " + ApnSetting.getApnTypeString(i));
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setState(int i) {
        this.mStatus = i | this.mStatus;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearState(int i) {
        this.mStatus = (~i) & this.mStatus;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkState(int i) {
        return (this.mStatus & i) == i;
    }

    public void setTestMode(boolean z) {
        this.mIsTestMode = z;
    }

    public void setDisconnectCause(int i, int i2, String str) {
        this.mDisconnectCause = i;
        this.mPreciseDisconnectCause = i2;
        this.mReasonMessage = str;
    }

    public int getDisconnectCause() {
        return this.mDisconnectCause;
    }

    public int getPreciseDisconnectCause() {
        return this.mPreciseDisconnectCause;
    }

    public String getReasonMessage() {
        return this.mReasonMessage;
    }

    public ImsReasonInfo getImsReasonInfo() {
        if (getSelectionAttributes() == null) {
            return null;
        }
        return getSelectionAttributes().getPsDisconnectCause();
    }

    public int getPhoneId() {
        return getPhone().getPhoneId();
    }

    public void dump(PrintWriter printWriter) {
        this.mLocalLog.dump(printWriter);
    }

    protected void logd(String str) {
        Log.d(this.mTag, str);
    }

    protected void logi(String str) {
        Log.i(this.mTag, str);
        this.mLocalLog.log(str);
    }

    protected void loge(String str) {
        Log.e(this.mTag, str);
        this.mLocalLog.log(str);
    }
}
