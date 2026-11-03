package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.android.internal.os.BackgroundThread;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.module.ITelephonyModule;
import com.qti.extphone.Client;
import com.qti.extphone.DcParam;
import com.qti.extphone.ExtPhoneCallbackBase;
import com.qti.extphone.ExtTelephonyManager;
import com.qti.extphone.IExtPhoneCallback;
import com.qti.extphone.NrIcon;
import com.qti.extphone.NrIconType;
import com.qti.extphone.ServiceCallback;
import com.qti.extphone.Status;
import com.qti.extphone.Token;
import com.qti.extphone.UpperLayerIndInfo;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import miui.telephony.DefaultSimManager;
import miuix.preference.flexible.PreferenceMarkLevel;

/* JADX INFO: loaded from: classes.dex */
public class QcPhoneConnector {
    private static volatile QcPhoneConnector sInstance;
    private Client mClient;
    private Context mContext;
    private ServiceHandler mHandler;
    private ExtTelephonyManager mNetworkService;
    private String mPackageName;
    private boolean mServiceConnected;
    private int mBindRetryTimes = 0;
    private int mInitRetryTimes = 0;
    private int mDdsPhoneId = -1;
    private CopyOnWriteArrayList mListeners = new CopyOnWriteArrayList();
    private IExtPhoneCallback mCallback = new ExtPhoneCallbackBase(this) { // from class: com.android.phone.QcPhoneConnector.1
        public void onNrIconType(int i, Token token, Status status, NrIconType nrIconType) {
            Log.d("QcPhoneConnector", "onNrIconType: status = " + status + " NrIconType = " + nrIconType + " slotId = " + i);
            if (status.get() != 1) {
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putInt(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, i);
            FiveGServiceInfo fiveGServiceInfo = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getFiveGServiceInfo(i);
            if (fiveGServiceInfo == null) {
                return;
            }
            fiveGServiceInfo.setNrIconType(nrIconType.get());
            FiveGManagerBase.getInstance().setFiveGARegistration(nrIconType.get() == 2);
            EventHubManager.getInstance().postEvent(new TelephonyEvent.FiveGEvent("fiveg_service_info_changed", fiveGServiceInfo, bundle));
        }

        public void onNrIconChange(int i, NrIcon nrIcon) {
            Log.d("QcPhoneConnector", "onNrIconChange: NrIcon = " + nrIcon + " slotId = " + i);
            Bundle bundle = new Bundle();
            bundle.putInt(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, i);
            FiveGServiceInfo fiveGServiceInfo = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getFiveGServiceInfo(i);
            if (fiveGServiceInfo == null) {
                return;
            }
            fiveGServiceInfo.setRxCount(nrIcon.getRxCount());
            fiveGServiceInfo.setNrIconType(nrIcon.getType());
            EventHubManager.getInstance().postEvent(new TelephonyEvent.FiveGEvent("fiveg_service_info_changed", fiveGServiceInfo, bundle));
        }

        public void onUpperLayerIndInfo(int i, Token token, Status status, UpperLayerIndInfo upperLayerIndInfo) {
            Log.d("QcPhoneConnector", "onUpperLayerIndInfo: uilInfo = " + upperLayerIndInfo + " slotId = " + i);
            Bundle bundle = new Bundle();
            bundle.putInt(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, i);
            FiveGServiceInfo fiveGServiceInfo = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getFiveGServiceInfo(i);
            if (fiveGServiceInfo == null) {
                return;
            }
            fiveGServiceInfo.setUpperLayerInd(upperLayerIndInfo.getUpperLayerIndInfoAvailable());
            EventHubManager.getInstance().postEvent(new TelephonyEvent.FiveGEvent("fiveg_service_info_changed", fiveGServiceInfo, bundle));
        }

        public void onNrDcParam(int i, Token token, Status status, DcParam dcParam) {
            Log.d("QcPhoneConnector", "onNrDcParam: dcParam = " + dcParam + " slotId = " + i);
            Bundle bundle = new Bundle();
            bundle.putInt(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, i);
            FiveGServiceInfo fiveGServiceInfo = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getFiveGServiceInfo(i);
            if (fiveGServiceInfo == null) {
                return;
            }
            fiveGServiceInfo.setDcnr(dcParam.getDcnr());
            EventHubManager.getInstance().postEvent(new TelephonyEvent.FiveGEvent("fiveg_service_info_changed", fiveGServiceInfo, bundle));
        }

        public void onEndcStatus(int i, Token token, Status status, boolean z) {
            Log.d("QcPhoneConnector", "onEndcStatus: status=" + status + " endcStatus=" + z + " slotId=" + i);
            if (status.get() != 1) {
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putInt(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, i);
            FiveGServiceInfo fiveGServiceInfo = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getFiveGServiceInfo(i);
            if (fiveGServiceInfo == null) {
                return;
            }
            fiveGServiceInfo.setEndcStatus(z);
            EventHubManager.getInstance().postEvent(new TelephonyEvent.FiveGEvent("fiveg_service_info_changed", fiveGServiceInfo, bundle));
        }
    };
    private ServiceCallback mServiceCallback = new ServiceCallback() { // from class: com.android.phone.QcPhoneConnector.2
        public void onConnected() {
            QcPhoneConnector.this.mHandler.post(new Runnable() { // from class: com.android.phone.QcPhoneConnector.2.1
                @Override // java.lang.Runnable
                public void run() {
                    Log.d("QcPhoneConnector", "QcPhone Service connected");
                    QcPhoneConnector.this.mServiceConnected = true;
                    QcPhoneConnector qcPhoneConnector = QcPhoneConnector.this;
                    qcPhoneConnector.mClient = qcPhoneConnector.mNetworkService.registerCallback(QcPhoneConnector.this.mPackageName, QcPhoneConnector.this.mCallback);
                    QcPhoneConnector.this.queryNrIconType();
                    if (QcPhoneConnector.this.mListeners.isEmpty()) {
                        return;
                    }
                    Iterator it = QcPhoneConnector.this.mListeners.iterator();
                    while (it.hasNext()) {
                        ((IQcPhoneConnectionListener) it.next()).onConnected(QcPhoneConnector.this.mNetworkService, QcPhoneConnector.this.mClient);
                    }
                }
            });
        }

        public void onDisconnected() {
            QcPhoneConnector.this.mHandler.post(new Runnable() { // from class: com.android.phone.QcPhoneConnector.2.2
                @Override // java.lang.Runnable
                public void run() {
                    Log.d("QcPhoneConnector", "QcPhone Service disconnected...");
                    if (QcPhoneConnector.this.mServiceConnected) {
                        QcPhoneConnector.this.mNetworkService.unRegisterCallback(QcPhoneConnector.this.mCallback);
                    }
                    QcPhoneConnector.this.mServiceConnected = false;
                    QcPhoneConnector.this.mHandler.sendEmptyMessageDelayed(PreferenceMarkLevel.LEVEL_NORMAL_ONLY_TITLE, MirrorDesktopHelper.TIMEOUT_MILLIS);
                    if (QcPhoneConnector.this.mListeners.isEmpty()) {
                        return;
                    }
                    Iterator it = QcPhoneConnector.this.mListeners.iterator();
                    while (it.hasNext()) {
                        ((IQcPhoneConnectionListener) it.next()).onDisconnected();
                    }
                }
            });
        }
    };
    private final BroadcastReceiver mDdsChangeReceiver = new AnonymousClass4();

    public interface IQcPhoneConnectionListener {
        void onConnected(ExtTelephonyManager extTelephonyManager, Client client);

        void onDisconnected();
    }

    public static QcPhoneConnector get() {
        if (sInstance == null) {
            synchronized (QcPhoneConnector.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new QcPhoneConnector();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    private QcPhoneConnector() {
    }

    public void init(Context context, Looper looper) {
        this.mContext = context;
        this.mPackageName = context.getPackageName();
        if (looper == null) {
            looper = BackgroundThread.get().getLooper();
        }
        this.mHandler = new ServiceHandler(looper);
        this.mDdsPhoneId = DefaultSimManager.getDefaultDataSlotId();
        this.mNetworkService = ExtTelephonyManager.getInstance(this.mContext);
        this.mHandler.sendEmptyMessage(PreferenceMarkLevel.LEVEL_NORMAL_ONLY_TITLE);
        registerReceiver();
    }

    class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 10001) {
                return;
            }
            QcPhoneConnector.this.connectService();
        }
    }

    public boolean isServiceConnected() {
        return this.mServiceConnected;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connectService() {
        if (isServiceConnected()) {
            return;
        }
        boolean zConnectService = this.mNetworkService.connectService(this.mServiceCallback);
        FiveGManagerBase.localLog("connectService", "connectService success=" + zConnectService + " bindRetryTimes=" + this.mBindRetryTimes + " maxRetryTimes=10");
        if (zConnectService) {
            this.mBindRetryTimes = 0;
        } else {
            if (this.mBindRetryTimes >= 10 || this.mHandler.hasMessages(PreferenceMarkLevel.LEVEL_NORMAL_ONLY_TITLE)) {
                return;
            }
            this.mHandler.sendEmptyMessageDelayed(PreferenceMarkLevel.LEVEL_NORMAL_ONLY_TITLE, (this.mBindRetryTimes * 2000) + 3000);
            this.mBindRetryTimes++;
        }
    }

    public boolean addListener(final IQcPhoneConnectionListener iQcPhoneConnectionListener) {
        if (iQcPhoneConnectionListener == null || this.mListeners.contains(iQcPhoneConnectionListener)) {
            return false;
        }
        this.mListeners.add(iQcPhoneConnectionListener);
        if (!isServiceConnected()) {
            return true;
        }
        this.mHandler.post(new Runnable() { // from class: com.android.phone.QcPhoneConnector.3
            @Override // java.lang.Runnable
            public void run() {
                iQcPhoneConnectionListener.onConnected(QcPhoneConnector.this.mNetworkService, QcPhoneConnector.this.mClient);
            }
        });
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void queryNrIconType() {
        if (this.mNetworkService == null || this.mClient == null) {
            return;
        }
        Log.d("QcPhoneConnector", "queryNrIconType");
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            try {
                this.mNetworkService.queryNrIconType(i, this.mClient);
            } catch (Exception e) {
                Log.d("QcPhoneConnector", "queryNrIconType: Exception = " + e);
            }
        }
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        this.mContext.registerReceiver(this.mDdsChangeReceiver, intentFilter, 2);
    }

    /* JADX INFO: renamed from: com.android.phone.QcPhoneConnector$4, reason: invalid class name */
    class AnonymousClass4 extends BroadcastReceiver {
        AnonymousClass4() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int defaultDataSlotId;
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            Log.d("QcPhoneConnector", "mDdsChangeReceiver action=".concat(action));
            if (!action.equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED") || (defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId()) < 0 || defaultDataSlotId >= MiuiPhoneUtils.PHONE_COUNT || QcPhoneConnector.this.mDdsPhoneId == defaultDataSlotId) {
                return;
            }
            Log.d("QcPhoneConnector", "DDS changed oldDds=" + QcPhoneConnector.this.mDdsPhoneId + ",currentDds=" + defaultDataSlotId);
            QcPhoneConnector.this.mDdsPhoneId = defaultDataSlotId;
            QcPhoneConnector.this.mHandler.post(new Runnable() { // from class: com.android.phone.QcPhoneConnector$4$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onReceive$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0() {
            QcPhoneConnector.this.queryNrIconType();
        }
    }
}
