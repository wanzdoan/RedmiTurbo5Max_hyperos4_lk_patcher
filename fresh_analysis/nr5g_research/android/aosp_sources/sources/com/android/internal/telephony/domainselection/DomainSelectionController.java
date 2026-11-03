package com.android.internal.telephony.domainselection;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.BarringInfo;
import android.telephony.DomainSelectionService;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.telephony.ExponentialBackoff;
import com.android.internal.telephony.IDomainSelectionServiceController;
import com.android.internal.telephony.ITransportSelectorCallback;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.internal.telephony.util.WorkerThread;
import java.io.PrintWriter;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class DomainSelectionController {
    private static final boolean DBG = TelephonyUtils.IS_DEBUGGABLE;
    private ExponentialBackoff mBackoff;
    private boolean mBackoffStarted;
    private BindRetry mBindRetry;
    private ComponentName mComponentName;
    protected final int[] mConnectionCounts;
    private final ArrayList mConnections;
    protected final Context mContext;
    private final Handler mHandler;
    private IDomainSelectionServiceController mIServiceController;
    private boolean mIsBound;
    private final LocalLog mLocalLog;
    protected final Object mLock;
    private Runnable mRestartBindingRunnable;
    private DomainSelectionServiceConnection mServiceConnection;
    private boolean mUnbind;

    public interface BindRetry {
        long getMaximumDelay();

        long getStartDelay();
    }

    /* JADX INFO: Access modifiers changed from: private */
    class DomainSelectionServiceConnection implements ServiceConnection {
        private boolean mIsServiceConnectionDead;

        private DomainSelectionServiceConnection() {
            this.mIsServiceConnectionDead = false;
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, final IBinder iBinder) {
            if (DomainSelectionController.this.mHandler.getLooper().isCurrentThread()) {
                lambda$onServiceConnected$0(iBinder);
            } else {
                DomainSelectionController.this.mHandler.post(new Runnable() { // from class: com.android.internal.telephony.domainselection.DomainSelectionController$DomainSelectionServiceConnection$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onServiceConnected$0(iBinder);
                    }
                });
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            if (DomainSelectionController.this.mHandler.getLooper().isCurrentThread()) {
                lambda$onServiceDisconnected$1();
            } else {
                DomainSelectionController.this.mHandler.post(new Runnable() { // from class: com.android.internal.telephony.domainselection.DomainSelectionController$DomainSelectionServiceConnection$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onServiceDisconnected$1();
                    }
                });
            }
        }

        @Override // android.content.ServiceConnection
        public void onBindingDied(ComponentName componentName) {
            if (DomainSelectionController.this.mHandler.getLooper().isCurrentThread()) {
                lambda$onBindingDied$2();
            } else {
                DomainSelectionController.this.mHandler.post(new Runnable() { // from class: com.android.internal.telephony.domainselection.DomainSelectionController$DomainSelectionServiceConnection$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onBindingDied$2();
                    }
                });
            }
        }

        @Override // android.content.ServiceConnection
        public void onNullBinding(ComponentName componentName) {
            if (DomainSelectionController.this.mHandler.getLooper().isCurrentThread()) {
                lambda$onNullBinding$3();
            } else {
                DomainSelectionController.this.mHandler.post(new Runnable() { // from class: com.android.internal.telephony.domainselection.DomainSelectionController$DomainSelectionServiceConnection$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onNullBinding$3();
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: onServiceConnectedInternal, reason: merged with bridge method [inline-methods] */
        public void lambda$onServiceConnected$0(IBinder iBinder) {
            synchronized (DomainSelectionController.this.mLock) {
                DomainSelectionController.this.stopBackoffTimer();
                DomainSelectionController.this.logi("onServiceConnected with binder: " + iBinder);
                DomainSelectionController.this.setServiceController(iBinder);
            }
            DomainSelectionController.this.notifyServiceConnected();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: onServiceDisconnectedInternal, reason: merged with bridge method [inline-methods] */
        public void lambda$onServiceDisconnected$1() {
            synchronized (DomainSelectionController.this.mLock) {
                DomainSelectionController.this.setServiceController(null);
            }
            DomainSelectionController.this.logi("onServiceDisconnected");
            DomainSelectionController.this.notifyServiceDisconnected();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: onBindingDiedInternal, reason: merged with bridge method [inline-methods] */
        public void lambda$onBindingDied$2() {
            this.mIsServiceConnectionDead = true;
            synchronized (DomainSelectionController.this.mLock) {
                DomainSelectionController.this.mIsBound = false;
                DomainSelectionController.this.setServiceController(null);
                DomainSelectionController.this.unbindService();
                DomainSelectionController.this.notifyBindFailure();
            }
            DomainSelectionController.this.loge("onBindingDied starting retrying in " + DomainSelectionController.this.mBackoff.getCurrentDelay() + " mS");
            DomainSelectionController.this.notifyServiceDisconnected();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: onNullBindingInternal, reason: merged with bridge method [inline-methods] */
        public void lambda$onNullBinding$3() {
            DomainSelectionController.this.loge("onNullBinding serviceDead=" + this.mIsServiceConnectionDead);
            if (this.mIsServiceConnectionDead) {
                return;
            }
            synchronized (DomainSelectionController.this.mLock) {
                DomainSelectionController.this.mIsBound = false;
                DomainSelectionController.this.setServiceController(null);
                DomainSelectionController.this.unbindService();
            }
            DomainSelectionController.this.notifyServiceDisconnected();
        }
    }

    private final class DomainSelectionControllerHandler extends Handler {
        DomainSelectionControllerHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                AsyncResult asyncResult = (AsyncResult) message.obj;
                DomainSelectionController.this.updateServiceState((Phone) asyncResult.userObj, (ServiceState) asyncResult.result);
            } else if (i == 2) {
                AsyncResult asyncResult2 = (AsyncResult) message.obj;
                DomainSelectionController.this.updateBarringInfo((Phone) asyncResult2.userObj, (BarringInfo) asyncResult2.result);
            } else {
                DomainSelectionController.this.loge("unexpected event=" + message.what);
            }
        }
    }

    public DomainSelectionController(Context context) {
        this(context, null, null);
    }

    public DomainSelectionController(Context context, Looper looper, BindRetry bindRetry) {
        this.mLocalLog = new LocalLog(30);
        this.mLock = new Object();
        this.mConnections = new ArrayList();
        this.mIsBound = false;
        this.mBackoffStarted = false;
        this.mUnbind = false;
        this.mRestartBindingRunnable = new Runnable() { // from class: com.android.internal.telephony.domainselection.DomainSelectionController.1
            @Override // java.lang.Runnable
            public void run() {
                DomainSelectionController.this.bind();
            }
        };
        this.mBindRetry = new BindRetry(this) { // from class: com.android.internal.telephony.domainselection.DomainSelectionController.2
            @Override // com.android.internal.telephony.domainselection.DomainSelectionController.BindRetry
            public long getMaximumDelay() {
                return 60000L;
            }

            @Override // com.android.internal.telephony.domainselection.DomainSelectionController.BindRetry
            public long getStartDelay() {
                return 2000L;
            }
        };
        this.mContext = context;
        DomainSelectionControllerHandler domainSelectionControllerHandler = new DomainSelectionControllerHandler(looper == null ? WorkerThread.get().getLooper() : looper);
        this.mHandler = domainSelectionControllerHandler;
        if (bindRetry != null) {
            this.mBindRetry = bindRetry;
        }
        this.mBackoff = new ExponentialBackoff(this.mBindRetry.getStartDelay(), this.mBindRetry.getMaximumDelay(), 2, domainSelectionControllerHandler, this.mRestartBindingRunnable);
        int supportedModemCount = ((TelephonyManager) context.getSystemService(TelephonyManager.class)).getSupportedModemCount();
        logi("numPhones=" + supportedModemCount);
        this.mConnectionCounts = new int[supportedModemCount];
        for (int i = 0; i < supportedModemCount; i++) {
            this.mConnectionCounts[i] = 0;
        }
    }

    public DomainSelectionConnection getDomainSelectionConnection(Phone phone, int i, boolean z) {
        DomainSelectionConnection smsDomainSelectionConnection;
        if (i == 1) {
            if (z) {
                smsDomainSelectionConnection = new EmergencyCallDomainSelectionConnection(phone, this);
            } else {
                smsDomainSelectionConnection = new NormalCallDomainSelectionConnection(phone, this);
            }
        } else if (i != 2) {
            smsDomainSelectionConnection = null;
        } else if (z) {
            smsDomainSelectionConnection = new EmergencySmsDomainSelectionConnection(phone, this);
        } else {
            smsDomainSelectionConnection = new SmsDomainSelectionConnection(phone, this);
        }
        addConnection(smsDomainSelectionConnection);
        return smsDomainSelectionConnection;
    }

    private void addConnection(DomainSelectionConnection domainSelectionConnection) {
        if (domainSelectionConnection == null) {
            return;
        }
        this.mConnections.add(domainSelectionConnection);
        registerForStateChange(domainSelectionConnection);
    }

    public void removeConnection(DomainSelectionConnection domainSelectionConnection) {
        if (domainSelectionConnection == null) {
            return;
        }
        this.mConnections.remove(domainSelectionConnection);
        unregisterForStateChange(domainSelectionConnection);
    }

    public boolean selectDomain(DomainSelectionService.SelectionAttributes selectionAttributes, ITransportSelectorCallback iTransportSelectorCallback) {
        if (selectionAttributes == null) {
            return false;
        }
        if (DBG) {
            logd("selectDomain");
        }
        synchronized (this.mLock) {
            try {
                try {
                    IDomainSelectionServiceController iDomainSelectionServiceController = this.mIServiceController;
                    if (iDomainSelectionServiceController != null) {
                        iDomainSelectionServiceController.selectDomain(selectionAttributes, iTransportSelectorCallback);
                        return true;
                    }
                } catch (RemoteException e) {
                    loge("selectDomain e=" + e);
                }
                return false;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateServiceState(Phone phone, ServiceState serviceState) {
        if (phone == null || serviceState == null) {
            return;
        }
        if (DBG) {
            logd("updateServiceState phoneId=" + phone.getPhoneId());
        }
        synchronized (this.mLock) {
            try {
                IDomainSelectionServiceController iDomainSelectionServiceController = this.mIServiceController;
                if (iDomainSelectionServiceController != null) {
                    iDomainSelectionServiceController.updateServiceState(phone.getPhoneId(), phone.getSubId(), serviceState);
                }
            } catch (RemoteException e) {
                loge("updateServiceState e=" + e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBarringInfo(Phone phone, BarringInfo barringInfo) {
        if (phone == null || barringInfo == null) {
            return;
        }
        if (DBG) {
            logd("updateBarringInfo phoneId=" + phone.getPhoneId());
        }
        synchronized (this.mLock) {
            try {
                IDomainSelectionServiceController iDomainSelectionServiceController = this.mIServiceController;
                if (iDomainSelectionServiceController != null) {
                    iDomainSelectionServiceController.updateBarringInfo(phone.getPhoneId(), phone.getSubId(), barringInfo);
                }
            } catch (RemoteException e) {
                loge("updateBarringInfo e=" + e);
            }
        }
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private void registerForStateChange(DomainSelectionConnection domainSelectionConnection) {
        Phone phone = domainSelectionConnection.getPhone();
        int i = this.mConnectionCounts[phone.getPhoneId()];
        if (i < 0) {
            i = 0;
        }
        this.mConnectionCounts[phone.getPhoneId()] = i + 1;
        if (i > 0) {
            return;
        }
        phone.registerForServiceStateChanged(this.mHandler, 1, phone);
        phone.mCi.registerForBarringInfoChanged(this.mHandler, 2, phone);
        updateServiceState(phone, phone.getServiceStateTracker().getServiceState());
        updateBarringInfo(phone, phone.mCi.getLastBarringInfo());
    }

    private void unregisterForStateChange(DomainSelectionConnection domainSelectionConnection) {
        Phone phone = domainSelectionConnection.getPhone();
        int i = this.mConnectionCounts[phone.getPhoneId()];
        if (i < 1) {
            i = 1;
        }
        this.mConnectionCounts[phone.getPhoneId()] = i - 1;
        if (i > 1) {
            return;
        }
        phone.unregisterForServiceStateChanged(this.mHandler);
        phone.mCi.unregisterForBarringInfoChanged(this.mHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyServiceConnected() {
        ArrayList arrayList = this.mConnections;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            DomainSelectionConnection domainSelectionConnection = (DomainSelectionConnection) obj;
            domainSelectionConnection.onServiceConnected();
            Phone phone = domainSelectionConnection.getPhone();
            updateServiceState(phone, phone.getServiceStateTracker().getServiceState());
            updateBarringInfo(phone, phone.mCi.getLastBarringInfo());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyServiceDisconnected() {
        ArrayList arrayList = this.mConnections;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            ((DomainSelectionConnection) obj).onServiceDisconnected();
        }
    }

    protected void setServiceController(IBinder iBinder) {
        this.mIServiceController = IDomainSelectionServiceController.Stub.asInterface(iBinder);
    }

    public boolean bind(ComponentName componentName) {
        this.mComponentName = componentName;
        this.mUnbind = false;
        return bind();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean bind() {
        logd("bind isBindingOrBound=" + this.mIsBound);
        synchronized (this.mLock) {
            try {
                if (this.mUnbind) {
                    return false;
                }
                if (this.mIsBound) {
                    return false;
                }
                this.mIsBound = true;
                Intent component = new Intent("android.telephony.DomainSelectionService").setComponent(this.mComponentName);
                this.mServiceConnection = new DomainSelectionServiceConnection();
                logi("binding DomainSelectionService");
                try {
                    boolean zBindService = this.mContext.bindService(component, this.mServiceConnection, 67108929);
                    if (!zBindService) {
                        loge("binding failed retrying in " + this.mBackoff.getCurrentDelay() + " mS");
                        this.mIsBound = false;
                        notifyBindFailure();
                    }
                    return zBindService;
                } catch (Exception e) {
                    this.mIsBound = false;
                    notifyBindFailure();
                    loge("binding e=" + e.getMessage() + ", retrying in " + this.mBackoff.getCurrentDelay() + " mS");
                    return false;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void unbind() {
        synchronized (this.mLock) {
            this.mUnbind = true;
            stopBackoffTimer();
            this.mIsBound = false;
            setServiceController(null);
            unbindService();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unbindService() {
        synchronized (this.mLock) {
            try {
                if (this.mServiceConnection != null) {
                    logi("unbinding Service");
                    this.mContext.unbindService(this.mServiceConnection);
                    this.mServiceConnection = null;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public long getBindDelay() {
        return this.mBackoff.getCurrentDelay();
    }

    public void stopBackoffTimer() {
        logi("stopBackoffTimer " + this.mBackoffStarted);
        this.mBackoffStarted = false;
        this.mBackoff.stop();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyBindFailure() {
        logi("notifyBindFailure started=" + this.mBackoffStarted + ", unbind=" + this.mUnbind);
        if (this.mUnbind) {
            return;
        }
        if (this.mBackoffStarted) {
            this.mBackoff.notifyFailed();
        } else {
            this.mBackoffStarted = true;
            this.mBackoff.start();
        }
        logi("notifyBindFailure currentDelay=" + getBindDelay());
    }

    public Handler getHandlerForTest() {
        return this.mHandler;
    }

    public void dump(PrintWriter printWriter) {
        this.mLocalLog.dump(printWriter);
    }

    private void logd(String str) {
        Log.d("DomainSelectionController", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logi(String str) {
        Log.i("DomainSelectionController", str);
        this.mLocalLog.log(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loge(String str) {
        Log.e("DomainSelectionController", str);
        this.mLocalLog.log(str);
    }
}
